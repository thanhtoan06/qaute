package vn.edu.hcmute.qaute.service.chat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;
import vn.edu.hcmute.qaute.repository.governance.BusinessHourRepository;
import vn.edu.hcmute.qaute.repository.governance.HolidayRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerProfileRepository;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.system.SettingsService;
import vn.edu.hcmute.qaute.websocket.PresenceService;

// Xử lý nghiệp vụ ChatAvailability trong hệ thống.
@Service
@Transactional(readOnly = true)
public class ChatAvailabilityService {
    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private final SettingsService settings;
    private final BusinessHourRepository hours;
    private final HolidayRepository holidays;
    private final ManagerScopeService scopes;
    private final ManagerProfileRepository profiles;
    private final PresenceService presence;
    private final ChatSessionRepository sessions;

    // Khởi tạo dịch vụ với các nguồn cấu hình, lịch làm việc và trạng thái tư vấn viên.
    public ChatAvailabilityService(SettingsService settings, BusinessHourRepository hours,
            HolidayRepository holidays, ManagerScopeService scopes, ManagerProfileRepository profiles,
            PresenceService presence, ChatSessionRepository sessions) {
        this.settings = settings;
        this.hours = hours;
        this.holidays = holidays;
        this.scopes = scopes;
        this.profiles = profiles;
        this.presence = presence;
        this.sessions = sessions;
    }

    // Xác định thời điểm hiện tại có nằm trong giờ làm việc đã cấu hình hay không.
    public boolean isWorkingNow() {
        if (!settings.getBoolean(SettingKeys.CHAT_USE_BUSINESS_HOURS)) {
            return true;
        }
        LocalDateTime now = LocalDateTime.now(ZONE);
        if (holidays.existsByDate(now.toLocalDate())) {
            return false;
        }
        return hours.findByActiveTrueOrderByDayOfWeekAscStartTimeAsc().stream()
                .anyMatch(hour -> hour.getDayOfWeek() == now.getDayOfWeek().getValue()
                        && !now.toLocalTime().isBefore(hour.getStartTime())
                        && now.toLocalTime().isBefore(hour.getEndTime()));
    }

    // Trả về các tư vấn viên đang trực và còn khả năng nhận phiên chat.
    public List<Long> eligibleManagerIds(Long categoryId) {
        List<ManagerOption> advisors = scopes.findAdvisors(categoryId);
        if (advisors.isEmpty()) {
            return List.of();
        }
        Map<Long, ManagerProfile> profileByUser = profiles.findByUserIdIn(
                advisors.stream().map(ManagerOption::id).toList()).stream()
                .collect(Collectors.toMap(ManagerProfile::getUserId, Function.identity()));
        int globalMax = settings.getInt(SettingKeys.CHAT_MAX_CONCURRENT);
        return advisors.stream().map(ManagerOption::id).filter(id -> {
            ManagerProfile profile = profileByUser.get(id);
            return profile != null && profile.getAvailabilityStatus() == AvailabilityStatus.ONLINE
                    && presence.isOnline(id)
                    && sessions.countByManagerIdAndStatus(id, ChatSessionStatus.ACTIVE)
                    < Math.min(globalMax, profile.getMaxConcurrentChats());
        }).toList();
    }
}
