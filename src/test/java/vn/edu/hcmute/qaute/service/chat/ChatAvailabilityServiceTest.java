package vn.edu.hcmute.qaute.service.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
class ChatAvailabilityServiceTest {
    @Mock SettingsService settings;
    @Mock BusinessHourRepository hours;
    @Mock HolidayRepository holidays;
    @Mock ManagerScopeService scopes;
    @Mock ManagerProfileRepository profiles;
    @Mock PresenceService presence;
    @Mock ChatSessionRepository sessions;
    @InjectMocks ChatAvailabilityService service;

    @Test
    void holidayOverridesBusinessHours() {
        when(settings.getBoolean(SettingKeys.CHAT_USE_BUSINESS_HOURS)).thenReturn(true);
        when(holidays.existsByDate(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")))).thenReturn(true);
        assertFalse(service.isWorkingNow());
    }

    @Test
    void eligibleManagerMustBeOnlineConnectedAndBelowCapacity() {
        ManagerOption advisor = new ManagerOption(7L, "Tư vấn viên", null, null, AvailabilityStatus.ONLINE);
        when(scopes.findAdvisors(4L)).thenReturn(List.of(advisor));
        ManagerProfile profile = new ManagerProfile();
        profile.setUserId(7L);
        profile.setAvailabilityStatus(AvailabilityStatus.ONLINE);
        profile.setMaxConcurrentChats(2);
        when(profiles.findByUserIdIn(List.of(7L))).thenReturn(List.of(profile));
        when(settings.getInt(SettingKeys.CHAT_MAX_CONCURRENT)).thenReturn(3);
        when(presence.isOnline(7L)).thenReturn(true);
        when(sessions.countByManagerIdAndStatus(7L, ChatSessionStatus.ACTIVE)).thenReturn(1L);
        assertEquals(List.of(7L), service.eligibleManagerIds(4L));
    }
}
