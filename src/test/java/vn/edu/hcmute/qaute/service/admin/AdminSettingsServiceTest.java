package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.view.SettingView;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@ExtendWith(MockitoExtension.class)
class AdminSettingsServiceTest {
    @Mock SettingsService settings;
    @Mock AuditService audit;

    @Test
    void groupsUseRequestedTabOrderAndPreserveSettingsOrder() {
        when(settings.listAll()).thenReturn(List.of(
                view("page.size.default", "10", "INT", "CHUNG"),
                view("auth.max.failed.logins", "5", "INT", "BẢO MẬT"),
                view("otp.ttl.minutes", "5", "INT", "OTP"),
                view("otp.max.attempts", "5", "INT", "OTP")));

        Map<String, List<SettingView>> groups = new AdminSettingsService(settings, audit).groups();

        assertEquals(List.of("OTP", "BẢO MẬT", "CHUNG"), List.copyOf(groups.keySet()));
        assertEquals(List.of("otp.ttl.minutes", "otp.max.attempts"),
                groups.get("OTP").stream().map(SettingView::key).toList());
    }

    @Test
    void savesChangedKeysContinuesAfterValidationErrorAndTreatsMissingCheckboxAsFalse() {
        when(settings.listAll()).thenReturn(List.of(
                view("ticket.auto.assign.enabled", "true", "BOOL", "TICKET"),
                view("ticket.reopen.max", "2", "INT", "TICKET"),
                view("ticket.reopen.window.days", "7", "INT", "TICKET")));
        doAnswer(invocation -> {
            if ("ticket.reopen.max".equals(invocation.getArgument(0))) {
                throw new BadRequestException("Vượt giới hạn");
            }
            return null;
        }).when(settings).update(any(), any(), any());
        Map<String, String> submitted = new LinkedHashMap<>();
        submitted.put("ticket.reopen.max", "999");
        submitted.put("ticket.reopen.window.days", "8");

        Map<String, String> errors = new AdminSettingsService(settings, audit)
                .saveGroup("TICKET", submitted, 4L);

        assertEquals(Map.of("ticket.reopen.max", "Vượt giới hạn"), errors);
        verify(settings).update("ticket.auto.assign.enabled", "false", 4L);
        verify(settings).update("ticket.reopen.window.days", "8", 4L);
        verify(audit).log(AuditActions.SETTING_CHANGED, "setting", null,
                "ticket.auto.assign.enabled=true", "ticket.auto.assign.enabled=false");
        verify(audit).log(AuditActions.SETTING_CHANGED, "setting", null,
                "ticket.reopen.window.days=7", "ticket.reopen.window.days=8");
        verify(audit, never()).log(AuditActions.SETTING_CHANGED, "setting", null,
                "ticket.reopen.max=2", "ticket.reopen.max=999");
    }

    @Test
    void unchangedValueIsSkippedAndUnknownGroupIsRejected() {
        when(settings.listAll()).thenReturn(List.of(view("otp.ttl.minutes", "5", "INT", "OTP")));
        AdminSettingsService service = new AdminSettingsService(settings, audit);

        assertTrue(service.saveGroup("OTP", Map.of("otp.ttl.minutes", "5"), 4L).isEmpty());
        verify(settings, never()).update("otp.ttl.minutes", "5", 4L);
        assertThrows(BadRequestException.class,
                () -> service.saveGroup("UNKNOWN", Map.of(), 4L));
    }

    private static SettingView view(String key, String value, String type, String group) {
        return new SettingView(key, value, type, 1L, 100L, group, key);
    }
}
