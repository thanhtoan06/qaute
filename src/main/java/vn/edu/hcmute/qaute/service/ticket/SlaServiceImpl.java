package vn.edu.hcmute.qaute.service.ticket;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.dto.view.SlaSnapshot;
import vn.edu.hcmute.qaute.entity.governance.BusinessHour;
import vn.edu.hcmute.qaute.entity.governance.SlaPolicy;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.governance.BusinessHourRepository;
import vn.edu.hcmute.qaute.repository.governance.HolidayRepository;
import vn.edu.hcmute.qaute.repository.governance.SlaPolicyRepository;
import vn.edu.hcmute.qaute.service.system.SettingsService;

// Xử lý nghiệp vụ Sla trong hệ thống.
@Service
public class SlaServiceImpl implements SlaService {
    private static final int MAX_DAYS = 400;
    private static final Map<TicketPriority, int[]> DEFAULTS = defaults();
    private final SlaPolicyRepository policies;
    private final BusinessHourRepository hours;
    private final HolidayRepository holidays;
    private final SettingsService settings;

    // Khởi tạo dịch vụ SLA với chính sách, giờ làm việc, ngày nghỉ và cài đặt.
    public SlaServiceImpl(SlaPolicyRepository policies, BusinessHourRepository hours,
                          HolidayRepository holidays, SettingsService settings) {
        this.policies = policies;
        this.hours = hours;
        this.holidays = holidays;
        this.settings = settings;
    }

    @Override
    // Tính thời hạn phản hồi và giải quyết ban đầu cho ticket.
    public SlaSnapshot computeInitial(Long categoryId, TicketPriority priority, LocalDateTime start) {
        if (priority == null || start == null) throw new IllegalArgumentException("Thiếu dữ liệu để tính SLA");
        SlaPolicy policy = categoryId == null ? null : policies
                .findFirstByCategoryIdAndPriorityAndActiveTrue(categoryId, priority).orElse(null);
        if (policy == null) policy = policies.findFirstByCategoryIdIsNullAndPriorityAndActiveTrue(priority).orElse(null);
        int[] fallback = DEFAULTS.get(priority);
        int firstResponse = policy == null ? fallback[0] : policy.getFirstResponseMinutes();
        int resolution = policy == null ? fallback[1] : policy.getResolutionMinutes();
        return new SlaSnapshot(firstResponse, resolution, addBusinessMinutes(start, firstResponse),
                addBusinessMinutes(start, resolution));
    }

    @Override
    // Cộng số phút làm việc vào thời điểm bắt đầu.
    public LocalDateTime addBusinessMinutes(LocalDateTime from, long minutes) {
        if (from == null || minutes < 0) throw new IllegalArgumentException("Thời gian SLA không hợp lệ");
        if (minutes == 0) return from;
        List<BusinessHour> configured = hours.findByActiveTrueOrderByDayOfWeekAscStartTimeAsc();
        long remainingSeconds = Math.multiplyExact(minutes, 60L);
        LocalDateTime cursor = from;
        for (int day = 0; day <= MAX_DAYS; day++) {
            LocalDate date = cursor.toLocalDate();
            if (!holidays.existsByDate(date)) {
                for (TimeWindow window : windows(date, configured)) {
                    LocalDateTime start = LocalDateTime.of(date, window.start());
                    LocalDateTime end = endOf(date, window);
                    LocalDateTime usableStart = cursor.isAfter(start) ? cursor : start;
                    if (!usableStart.isBefore(end)) continue;
                    long availableSeconds = Duration.between(usableStart, end).getSeconds();
                    if (remainingSeconds <= availableSeconds) return usableStart.plusSeconds(remainingSeconds);
                    remainingSeconds -= availableSeconds;
                    cursor = end;
                }
            }
            cursor = date.plusDays(1).atStartOfDay();
        }
        throw new IllegalStateException("Không thể tính SLA quá 400 ngày");
    }

    @Override
    // Tính tổng số phút làm việc trong khoảng thời gian.
    public long businessMinutesBetween(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null || !to.isAfter(from)) return 0;
        List<BusinessHour> configured = hours.findByActiveTrueOrderByDayOfWeekAscStartTimeAsc();
        long seconds = 0;
        LocalDate date = from.toLocalDate();
        LocalDate lastDate = to.toLocalDate();
        for (int day = 0; !date.isAfter(lastDate) && day <= MAX_DAYS; day++, date = date.plusDays(1)) {
            if (holidays.existsByDate(date)) continue;
            for (TimeWindow window : windows(date, configured)) {
                LocalDateTime start = LocalDateTime.of(date, window.start());
                LocalDateTime end = endOf(date, window);
                LocalDateTime overlapStart = from.isAfter(start) ? from : start;
                LocalDateTime overlapEnd = to.isBefore(end) ? to : end;
                if (overlapStart.isBefore(overlapEnd)) seconds += Duration.between(overlapStart, overlapEnd).getSeconds();
            }
        }
        if (date.isBefore(lastDate.plusDays(1))) throw new IllegalStateException("Không thể tính SLA quá 400 ngày");
        return seconds / 60;
    }

    @Override
    // Đánh giá ticket đang đúng hạn, có nguy cơ hay đã vi phạm SLA.
    public SlaStatus evaluate(Ticket ticket, LocalDateTime now) {
        if (ticket == null || now == null) throw new IllegalArgumentException("Thiếu dữ liệu để đánh giá SLA");
        if (ticket.getSlaPausedAt() != null || ticket.getResolvedAt() != null || ticket.getClosedAt() != null
                || (ticket.getStatus() != null && ticket.getStatus().isFinished())) return ticket.getSlaStatus();
        Integer target = ticket.getFirstResponseAt() == null ? ticket.getSlaFirstResponseMinutes() : ticket.getSlaResolutionMinutes();
        if (target == null || target <= 0 || ticket.getCreatedAt() == null) return SlaStatus.ON_TRACK;
        LocalDateTime effectiveNow = now.minusSeconds(Math.max(0, ticket.getSlaPausedSeconds()));
        long elapsed = businessMinutesBetween(ticket.getCreatedAt(), effectiveNow);
        double progress = elapsed * 100.0 / target;
        if (progress >= 100.0) return SlaStatus.BREACHED;
        return progress >= settings.getInt(SettingKeys.SLA_AT_RISK_PERCENT) ? SlaStatus.AT_RISK : SlaStatus.ON_TRACK;
    }

    @Override
    // Ghi nhận thời điểm tạm dừng SLA nếu ticket chưa tạm dừng.
    public void pause(Ticket ticket, LocalDateTime now) {
        if (ticket != null && now != null && ticket.getSlaPausedAt() == null) ticket.setSlaPausedAt(now);
    }

    @Override
    // Tiếp tục SLA và dời các thời hạn theo thời gian tạm dừng.
    public void resume(Ticket ticket, LocalDateTime now) {
        if (ticket == null || now == null || ticket.getSlaPausedAt() == null) return;
        LocalDateTime pausedAt = ticket.getSlaPausedAt();
        long pausedSeconds = Math.max(0, Duration.between(pausedAt, now).getSeconds());
        long pausedBusinessMinutes = businessMinutesBetween(pausedAt, now);
        ticket.setSlaPausedSeconds(ticket.getSlaPausedSeconds() + pausedSeconds);
        if (ticket.getSlaFirstResponseDueAt() != null) ticket.setSlaFirstResponseDueAt(addBusinessMinutes(ticket.getSlaFirstResponseDueAt(), pausedBusinessMinutes));
        if (ticket.getSlaResolutionDueAt() != null) ticket.setSlaResolutionDueAt(addBusinessMinutes(ticket.getSlaResolutionDueAt(), pausedBusinessMinutes));
        ticket.setSlaPausedAt(null);
    }

    @Override
    // Tính lại chỉ tiêu, thời hạn và trạng thái SLA của ticket.
    public void recompute(Ticket ticket, LocalDateTime now) {
        if (ticket == null || ticket.getCreatedAt() == null) throw new IllegalArgumentException("Ticket chưa có thời điểm tạo");
        SlaSnapshot snapshot = computeInitial(ticket.getCategoryId(), ticket.getPriority(), ticket.getCreatedAt());
        long pausedBusinessMinutes = Math.max(0, ticket.getSlaPausedSeconds()) / 60;
        ticket.setSlaFirstResponseMinutes(snapshot.firstResponseMinutes());
        ticket.setSlaResolutionMinutes(snapshot.resolutionMinutes());
        ticket.setSlaFirstResponseDueAt(addBusinessMinutes(snapshot.firstResponseDueAt(), pausedBusinessMinutes));
        ticket.setSlaResolutionDueAt(addBusinessMinutes(snapshot.resolutionDueAt(), pausedBusinessMinutes));
        ticket.setSlaStatus(evaluate(ticket, now == null ? LocalDateTime.now() : now));
    }

    // Lấy các khung giờ làm việc áp dụng cho ngày được chỉ định.
    private static List<TimeWindow> windows(LocalDate date, List<BusinessHour> configured) {
        if (configured.isEmpty()) return List.of(new TimeWindow(LocalTime.MIN, LocalTime.MAX));
        List<TimeWindow> result = new ArrayList<>();
        int day = date.getDayOfWeek().getValue();
        configured.stream().filter(hour -> hour.getDayOfWeek() == day).sorted(Comparator.comparing(BusinessHour::getStartTime))
                .forEach(hour -> result.add(new TimeWindow(hour.getStartTime(), hour.getEndTime())));
        return result;
    }

    // Tính thời điểm kết thúc của khung giờ trong ngày.
    private static LocalDateTime endOf(LocalDate date, TimeWindow window) {
        return window.end().equals(LocalTime.MAX) ? date.plusDays(1).atStartOfDay()
                : LocalDateTime.of(date, window.end());
    }

    // Tạo bảng thời hạn SLA mặc định theo mức ưu tiên.
    private static Map<TicketPriority, int[]> defaults() {
        Map<TicketPriority, int[]> result = new EnumMap<>(TicketPriority.class);
        result.put(TicketPriority.NORMAL, new int[]{480, 2880});
        result.put(TicketPriority.LOW, new int[]{960, 5760});
        result.put(TicketPriority.HIGH, new int[]{240, 1440});
        result.put(TicketPriority.URGENT, new int[]{120, 480});
        return result;
    }

    private record TimeWindow(LocalTime start, LocalTime end) {
    }
}
