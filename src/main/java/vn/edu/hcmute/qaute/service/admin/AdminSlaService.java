package vn.edu.hcmute.qaute.service.admin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.request.admin.SlaPolicyForm;
import vn.edu.hcmute.qaute.dto.view.SlaPolicyMatrix;
import vn.edu.hcmute.qaute.dto.view.SlaPolicyMatrix.CategoryOverride;
import vn.edu.hcmute.qaute.dto.view.SlaPolicyMatrix.PolicyRow;
import vn.edu.hcmute.qaute.entity.governance.SlaPolicy;
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

    public AdminSlaService(SlaPolicyRepository policies, CategoryQueryService categories,
                           AuditService audit) {
        this.policies = policies;
        this.categories = categories;
        this.audit = audit;
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
