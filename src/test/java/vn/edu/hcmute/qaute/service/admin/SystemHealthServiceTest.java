package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cloudinary.Cloudinary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import vn.edu.hcmute.qaute.common.constant.EmailStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.config.AppProperties;
import vn.edu.hcmute.qaute.config.CloudinaryConfig.CloudinaryProperties;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.websocket.PresenceService;

class SystemHealthServiceTest {
    private final EntityManager entityManager = mock(EntityManager.class);
    private final EmailService emails = mock(EmailService.class);
    private final Cloudinary cloudinary = mock(Cloudinary.class);
    private final CloudinaryProperties cloudinaryProperties = new CloudinaryProperties(false);
    private final PresenceService presence = mock(PresenceService.class);
    private final AppProperties appProperties = new AppProperties();
    @SuppressWarnings("unchecked")
    private final ObjectProvider<BuildProperties> buildProperties = mock(ObjectProvider.class);

    @Test
    void checksShowQueueCountsWithoutLeakingConfiguration() {
        Query ping = mock(Query.class);
        @SuppressWarnings("unchecked") TypedQuery<Object[]> counts = mock(TypedQuery.class);
        when(entityManager.createNativeQuery("SELECT 1")).thenReturn(ping);
        when(ping.getSingleResult()).thenReturn(1);
        when(entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(counts);
        when(counts.setParameter(eq("statuses"), any())).thenReturn(counts);
        when(counts.getResultList()).thenReturn(List.<Object[]>of(
                new Object[]{EmailStatus.PENDING, 2L}, new Object[]{EmailStatus.FAILED, 1L}));
        when(presence.connectionCount()).thenReturn(3);
        SystemHealthService service = service("mailer@example.com");

        Map<String, SystemHealthService.HealthCheck> checks = service.checks();

        assertEquals(SystemHealthService.Status.OK, checks.get("database").status());
        assertEquals(SystemHealthService.Status.WARN, checks.get("email").status());
        assertTrue(checks.get("email").detail().contains("2 thư đang chờ · 1 thư thất bại"));
        assertEquals(SystemHealthService.Status.WARN, checks.get("cloudinary").status());
        assertTrue(checks.get("websocket").detail().contains("3 kết nối"));
        assertFalse(checks.toString().contains("mailer@example.com"));
    }

    @Test
    void testEmailQueuesGenericTemplateAndStripsUrlSecrets() {
        appProperties.setBaseUrl("https://user:secret@example.com/private?api_key=secret#part");
        SystemHealthService service = service("mailer@example.com");

        service.queueTestEmail("admin@example.com");

        @SuppressWarnings("unchecked") ArgumentCaptor<Map<String, String>> model =
                ArgumentCaptor.forClass(Map.class);
        verify(emails).sendTemplated(eq("admin@example.com"), anyString(), eq("generic"), model.capture());
        assertEquals("https://example.com/admin/health", model.getValue().get("linkUrl"));
    }

    @Test
    void testEmailRejectsMissingMailConfiguration() {
        assertThrows(BadRequestException.class,
                () -> service("").queueTestEmail("admin@example.com"));
    }

    private SystemHealthService service(String mailUsername) {
        return new SystemHealthService(entityManager, emails, cloudinary, cloudinaryProperties,
                presence, appProperties, buildProperties, mailUsername, "0.0.1-SNAPSHOT");
    }
}
