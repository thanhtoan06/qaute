package vn.edu.hcmute.qaute.service.admin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.admin.BusinessHoursForm;
import vn.edu.hcmute.qaute.dto.request.admin.HolidayForm;
import vn.edu.hcmute.qaute.dto.request.admin.SlaPolicyForm;
import vn.edu.hcmute.qaute.dto.view.SlaPolicyMatrix;
import vn.edu.hcmute.qaute.dto.view.SlaPolicyMatrix.CategoryOverride;
import vn.edu.hcmute.qaute.dto.view.SlaPolicyMatrix.PolicyRow;
import vn.edu.hcmute.qaute.entity.governance.SlaPolicy;
import vn.edu.hcmute.qaute.entity.governance.BusinessHour;
import vn.edu.hcmute.qaute.entity.governance.Holiday;
import vn.edu.hcmute.qaute.repository.governance.BusinessHourRepository;
import vn.edu.hcmute.qaute.repository.governance.HolidayRepository;
import vn.edu.hcmute.qaute.repository.governance.SlaPolicyRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Service
@Transactional(readOnly = true)
public class AdminSlaService {
    private static final BigDecimal SIXTY = BigDecimal.valueOf(60);
    private static final BigDecimal MIN_HOURS = new BigDecimal("0.5");
    private static final BigDecimal MAX_RESPONSE_HOURS = new BigDecimal("720");
    private static final BigDecimal MAX_RESOLUTION_HOURS = new BigDecimal("2160");

    private final SlaPolicyRepository policies;
    private final CategoryQueryService categories;
    private final AuditService audit;
    private final BusinessHourRepository hours;
    private final HolidayRepository holidays;

    public AdminSlaService(SlaPolicyRepository policies, CategoryQueryService categories,
                           AuditService audit, BusinessHourRepository hours, HolidayRepository holidays) {
        this.policies = policies;
        this.categories = categories;
        this.audit = audit;
        this.hours = hours;
        this.holidays = holidays;
    }

    public Map<Integer, List<BusinessHour>> businessHours() {
        Map<Integer, List<BusinessHour>> grouped = new LinkedHashMap<>();
        for (int day = 1; day <= 7; day++) {
            grouped.put(day, new ArrayList<>());
        }
        for (BusinessHour hour : hours.findAllByOrderByDayOfWeekAscStartTimeAsc()) {
            grouped.get(hour.getDayOfWeek()).add(hour);
        }
        return grouped;
    }

    @Transactional
    public void saveBusinessHours(BusinessHoursForm form, Long actorId) {
        if (form == null || form.getIntervals() == null) {
            throw new BadRequestException("Danh sách giờ làm việc không hợp lệ");
        }
        Map<String, String> errors = new LinkedHashMap<>();
        Map<Integer, List<BusinessHour>> byDay = new LinkedHashMap<>();
        List<BusinessHour> replacement = new ArrayList<>();
        List<BusinessHoursForm.Interval> intervals = form.getIntervals();
        for (int i = 0; i < intervals.size(); i++) {
            BusinessHoursForm.Interval interval = intervals.get(i);
            String prefix = "intervals[" + i + "].";
            if (interval == null || interval.getDayOfWeek() == null
                    || interval.getDayOfWeek() < 1 || interval.getDayOfWeek() > 7) {
                errors.put(prefix + "dayOfWeek", "Thứ trong tuần phải từ 1 đến 7");
                continue;
            }
            LocalTime start = parseTime(interval.getStart());
            LocalTime end = parseTime(interval.getEnd());
            if (start == null) errors.put(prefix + "start", "Giờ bắt đầu phải theo HH:mm");
            if (end == null) errors.put(prefix + "end", "Giờ kết thúc phải theo HH:mm");
            if (start == null || end == null) continue;
            if (!start.isBefore(end)) {
                errors.put(prefix + "end", "Giờ kết thúc phải sau giờ bắt đầu");
                continue;
            }
            BusinessHour hour = BusinessHour.builder().dayOfWeek(interval.getDayOfWeek())
                    .startTime(start).endTime(end).active(interval.isActive()).build();
            byDay.computeIfAbsent(hour.getDayOfWeek(), ignored -> new ArrayList<>()).add(hour);
            replacement.add(hour);
        }
        byDay.forEach((day, dayHours) -> {
            if (dayHours.size() > 4) {
                errors.put("day" + day, "Mỗi ngày tối đa 4 khoảng giờ");
            }
            dayHours.sort(Comparator.comparing(BusinessHour::getStartTime));
            for (int i = 1; i < dayHours.size(); i++) {
                if (dayHours.get(i - 1).getEndTime().isAfter(dayHours.get(i).getStartTime())) {
                    errors.put("day" + day, "Các khoảng giờ cùng ngày không được chồng lấn");
                }
            }
        });
        if (!errors.isEmpty()) {
            throw new BadRequestException("Vui lòng kiểm tra giờ làm việc", errors);
        }
        String before = summarizeHours(hours.findAllByOrderByDayOfWeekAscStartTimeAsc());
        hours.deleteAllInBatch();
        hours.saveAll(replacement);
        audit.log(AuditActions.BUSINESS_HOURS_CHANGED, "BusinessHour", null,
                before, summarizeHours(replacement));
    }

    public List<Holiday> holidays() {
        return holidays.findAllByOrderByDateAsc();
    }

    @Transactional
    public void addHoliday(HolidayForm form, Long actorId) {
        if (form == null || form.getDate() == null || form.getName() == null
                || form.getName().isBlank() || form.getName().trim().length() > 150) {
            throw new BadRequestException("Ngày nghỉ không hợp lệ");
        }
        if (holidays.existsByDate(form.getDate())) {
            throw new DuplicateException("date", "Ngày nghỉ này đã tồn tại");
        }
        Holiday holiday = Holiday.builder().date(form.getDate()).name(form.getName().trim()).build();
        try {
            holidays.saveAndFlush(holiday);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException("date", "Ngày nghỉ này đã tồn tại");
        }
        audit.log(AuditActions.HOLIDAY_CHANGED, "Holiday", holiday.getId(), null,
                holiday.getDate() + " " + holiday.getName());
    }

    @Transactional
    public void deleteHoliday(Long id, Long actorId) {
        Holiday holiday = holidays.findById(id)
                .orElseThrow(() -> new NotFoundException("Ngày nghỉ không tồn tại"));
        holidays.delete(holiday);
        audit.log(AuditActions.HOLIDAY_CHANGED, "Holiday", id,
                holiday.getDate() + " " + holiday.getName(), null);
    }

    private static LocalTime parseTime(String value) {
        if (value == null || !value.matches("[0-2][0-9]:[0-5][0-9]")) return null;
        try {
            return LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static String summarizeHours(List<BusinessHour> rows) {
        return rows.stream().sorted(Comparator.comparingInt(BusinessHour::getDayOfWeek)
                .thenComparing(BusinessHour::getStartTime))
                .map(row -> row.getDayOfWeek() + "=" + row.getStartTime() + "-"
                        + row.getEndTime() + "/" + row.isActive())
                .reduce((a, b) -> a + ";" + b).orElse("");
    }

    public SlaPolicyMatrix matrix() {
        Map<TicketPriority, SlaPolicy> defaults = new EnumMap<>(TicketPriority.class);
        Map<Long, Map<TicketPriority, SlaPolicy>> overrides = new LinkedHashMap<>();
        for (SlaPolicy policy : policies.findAllByOrderByCategoryIdAscPriorityAsc()) {
            if (policy.getCategoryId() == null) {
                defaults.putIfAbsent(policy.getPriority(), policy);
            } else {
                overrides.computeIfAbsent(policy.getCategoryId(), ignored ->
                        new EnumMap<>(TicketPriority.class)).put(policy.getPriority(), policy);
            }
        }
        List<CategoryOverride> categoryRows = new ArrayList<>();
        overrides.forEach((categoryId, rows) -> {
            String name = categories.getDisplayPath(categoryId);
            categoryRows.add(new CategoryOverride(categoryId,
                    name.isBlank() ? "Chuyên mục #" + categoryId : name, toRows(rows)));
        });
        return new SlaPolicyMatrix(toRows(defaults), List.copyOf(categoryRows));
    }

    public SlaPolicyForm getForm(Long categoryId) {
        requireCategory(categoryId);
        SlaPolicyForm form = new SlaPolicyForm();
        form.setCategoryId(categoryId);
        for (SlaPolicyForm.Row row : form.getRows()) {
            TicketPriority priority = row.getPriority();
            SlaPolicy policy = categoryId == null ? null
                    : policies.findByCategoryIdAndPriority(categoryId, priority).orElse(null);
            if (policy == null) {
                policy = policies.findFirstByCategoryIdIsNullAndPriorityOrderByIdAsc(priority)
                        .orElse(null);
            }
            row.setFirstResponseHours(policy == null ? MIN_HOURS : toHours(policy.getFirstResponseMinutes()));
            row.setResolutionHours(policy == null ? MIN_HOURS : toHours(policy.getResolutionMinutes()));
            row.setActive(policy == null || policy.isActive());
        }
        return form;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void save(SlaPolicyForm form, Long actorId) {
        if (form == null) {
            throw new BadRequestException("Dữ liệu chính sách SLA không hợp lệ");
        }
        requireCategory(form.getCategoryId());
        validate(form);
        String before = summary(form.getCategoryId());
        boolean changed = false;
        for (SlaPolicyForm.Row row : form.getRows()) {
            SlaPolicy policy = findPolicy(form.getCategoryId(), row.getPriority());
            boolean fresh = policy == null;
            if (fresh) {
                policy = SlaPolicy.builder().categoryId(form.getCategoryId())
                        .priority(row.getPriority()).build();
            }
            int responseMinutes = toMinutes(row.getFirstResponseHours());
            int resolutionMinutes = toMinutes(row.getResolutionHours());
            if (fresh || policy.getFirstResponseMinutes() != responseMinutes
                    || policy.getResolutionMinutes() != resolutionMinutes
                    || policy.isActive() != row.isActive()) {
                policy.setFirstResponseMinutes(responseMinutes);
                policy.setResolutionMinutes(resolutionMinutes);
                policy.setActive(row.isActive());
                policies.save(policy);
                changed = true;
            }
        }
        if (changed) {
            audit.log(AuditActions.SLA_POLICY_CHANGED, "SlaPolicy", form.getCategoryId(),
                    before, summary(form.getCategoryId()));
        }
    }

    @Transactional
    public void deleteOverride(Long categoryId, Long actorId) {
        if (categoryId == null) {
            throw new BadRequestException("Không thể xóa chính sách mặc định");
        }
        requireCategory(categoryId);
        List<SlaPolicy> overrideRows = policies.findByCategoryId(categoryId);
        if (overrideRows.isEmpty()) {
            throw new BadRequestException("Chuyên mục chưa có chính sách ghi đè");
        }
        String before = summarize(overrideRows);
        policies.deleteAll(overrideRows);
        audit.log(AuditActions.SLA_POLICY_CHANGED, "SlaPolicy", categoryId,
                before, "override deleted");
    }

    private void requireCategory(Long categoryId) {
        if (categoryId != null && categories.findCategory(categoryId).isEmpty()) {
            throw new BadRequestException("Chuyên mục không tồn tại");
        }
    }

    private SlaPolicy findPolicy(Long categoryId, TicketPriority priority) {
        return categoryId == null
                ? policies.findFirstByCategoryIdIsNullAndPriorityOrderByIdAsc(priority).orElse(null)
                : policies.findByCategoryIdAndPriority(categoryId, priority).orElse(null);
    }

    private String summary(Long categoryId) {
        List<SlaPolicy> rows = categoryId == null
                ? policies.findAllByOrderByCategoryIdAscPriorityAsc().stream()
                        .filter(policy -> policy.getCategoryId() == null).toList()
                : policies.findByCategoryId(categoryId);
        return summarize(rows);
    }

    private static String summarize(List<SlaPolicy> rows) {
        StringBuilder result = new StringBuilder();
        for (SlaPolicy policy : rows) {
            if (result.length() > 0) {
                result.append(';');
            }
            result.append(policy.getPriority()).append('=')
                    .append(policy.getFirstResponseMinutes()).append('/')
                    .append(policy.getResolutionMinutes()).append('/')
                    .append(policy.isActive());
        }
        return result.toString();
    }

    private static List<PolicyRow> toRows(Map<TicketPriority, SlaPolicy> policies) {
        List<PolicyRow> rows = new ArrayList<>();
        for (TicketPriority priority : TicketPriority.values()) {
            SlaPolicy policy = policies.get(priority);
            if (policy != null) {
                rows.add(new PolicyRow(priority, toHours(policy.getFirstResponseMinutes()),
                        toHours(policy.getResolutionMinutes()), policy.isActive()));
            }
        }
        return List.copyOf(rows);
    }

    private static BigDecimal toHours(int minutes) {
        return BigDecimal.valueOf(minutes).divide(SIXTY, 2, RoundingMode.HALF_UP);
    }

    private static int toMinutes(BigDecimal hours) {
        return hours.multiply(SIXTY).setScale(0, RoundingMode.HALF_UP).intValueExact();
    }

    private static void validate(SlaPolicyForm form) {
        Map<String, String> errors = new LinkedHashMap<>();
        List<SlaPolicyForm.Row> rows = form.getRows();
        if (rows == null || rows.size() != TicketPriority.values().length) {
            throw new BadRequestException("Chính sách phải có đủ bốn mức ưu tiên");
        }
        for (int i = 0; i < rows.size(); i++) {
            SlaPolicyForm.Row row = rows.get(i);
            if (row == null || row.getPriority() != TicketPriority.values()[i]) {
                errors.put("rows[" + i + "].priority", "Thứ tự mức ưu tiên không hợp lệ");
                continue;
            }
            if (!inRange(row.getFirstResponseHours(), MIN_HOURS, MAX_RESPONSE_HOURS)) {
                errors.put("rows[" + i + "].firstResponseHours", "Phải từ 0,5 đến 720 giờ");
            }
            if (!inRange(row.getResolutionHours(), MIN_HOURS, MAX_RESOLUTION_HOURS)) {
                errors.put("rows[" + i + "].resolutionHours", "Phải từ 0,5 đến 2160 giờ");
            } else if (row.getFirstResponseHours() != null
                    && row.getResolutionHours().compareTo(row.getFirstResponseHours()) < 0) {
                errors.put("rows[" + i + "].resolutionHours",
                        "Hạn giải quyết phải lớn hơn hoặc bằng hạn phản hồi");
            }
        }
        if (!errors.isEmpty()) {
            throw new BadRequestException("Vui lòng kiểm tra chính sách SLA", errors);
        }
    }

    private static boolean inRange(BigDecimal value, BigDecimal min, BigDecimal max) {
        return value != null && value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }
}
