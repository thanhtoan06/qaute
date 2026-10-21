package vn.edu.hcmute.qaute.service.admin;

import com.cloudinary.Cloudinary;
import jakarta.persistence.EntityManager;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.qaute.common.constant.EmailStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.config.AppProperties;
import vn.edu.hcmute.qaute.config.CloudinaryConfig.CloudinaryProperties;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.websocket.PresenceService;

@Service
public class SystemHealthService {
    public enum Status { OK, WARN, FAIL }

    public record HealthCheck(String name, Status status, String detail) {
    }

    private final EntityManager entityManager;
    private final EmailService emails;
    private final Cloudinary cloudinary;
    private final CloudinaryProperties cloudinaryProperties;
    private final PresenceService presence;
    private final AppProperties appProperties;
    private final ObjectProvider<BuildProperties> buildProperties;
    private final String mailUsername;
    private final String fallbackVersion;

    public SystemHealthService(EntityManager entityManager, EmailService emails,
            Cloudinary cloudinary, CloudinaryProperties cloudinaryProperties,
            PresenceService presence, AppProperties appProperties,
            ObjectProvider<BuildProperties> buildProperties,
            @Value("${spring.mail.username:}") String mailUsername,
            @Value("${app.version:0.0.1-SNAPSHOT}") String fallbackVersion) {
        this.entityManager = entityManager;
        this.emails = emails;
        this.cloudinary = cloudinary;
        this.cloudinaryProperties = cloudinaryProperties;
        this.presence = presence;
        this.appProperties = appProperties;
        this.buildProperties = buildProperties;
        this.mailUsername = mailUsername;
        this.fallbackVersion = fallbackVersion;
    }

    public Map<String, HealthCheck> checks() {
        Map<String, HealthCheck> result = new LinkedHashMap<>();
        result.put("database", database());
        result.put("email", email());
        result.put("cloudinary", cloudinary());
        result.put("websocket", new HealthCheck("WebSocket", Status.OK,
                presence.connectionCount() + " kết nối hiện tại"));
        result.put("memory", memory());
        result.put("application", application());
        return result;
    }

    public boolean isEmailConfigured() {
        return mailUsername != null && !mailUsername.isBlank();
    }

    public void queueTestEmail(String adminEmail) {
        if (!isEmailConfigured()) {
            throw new BadRequestException("Chưa cấu hình tài khoản gửi email");
        }
        if (adminEmail == null || adminEmail.isBlank()) {
            throw new BadRequestException("Tài khoản Admin chưa có email");
        }
        emails.sendTemplated(adminEmail, "Thư kiểm tra hệ thống QAUTE", "generic",
                Map.of("title", "Kiểm tra email QAUTE",
                        "body", "Đây là thư thử từ trang kiểm tra hệ thống. Hãy kiểm tra trạng thái hàng đợi nếu chưa nhận được thư.",
                        "linkUrl", healthLink(), "linkLabel", "Mở trang kiểm tra"));
    }

    private HealthCheck database() {
        long started = System.nanoTime();
        try {
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
            long millis = (System.nanoTime() - started) / 1_000_000;
            return new HealthCheck("Cơ sở dữ liệu", Status.OK, "Phản hồi trong " + millis + " ms");
        } catch (Exception ignored) {
            return new HealthCheck("Cơ sở dữ liệu", Status.FAIL, "Không thể truy vấn cơ sở dữ liệu");
        }
    }

    private HealthCheck email() {
        try {
            List<Object[]> counts = entityManager.createQuery(
                    "select e.status, count(e) from EmailOutbox e "
                            + "where e.status in :statuses group by e.status", Object[].class)
                    .setParameter("statuses", List.of(EmailStatus.PENDING, EmailStatus.FAILED))
                    .getResultList();
            long pending = 0;
            long failed = 0;
            for (Object[] row : counts) {
                if (row[0] == EmailStatus.PENDING) pending = ((Number) row[1]).longValue();
                if (row[0] == EmailStatus.FAILED) failed = ((Number) row[1]).longValue();
            }
            Status status = !isEmailConfigured() || failed > 0 ? Status.WARN : Status.OK;
            String detail = (isEmailConfigured() ? "Đã cấu hình tài khoản gửi" : "Chưa cấu hình tài khoản gửi")
                    + " · " + pending + " thư đang chờ · " + failed + " thư thất bại";
            return new HealthCheck("Email", status, detail);
        } catch (Exception ignored) {
            return new HealthCheck("Email", Status.FAIL, "Không thể đọc hàng đợi email");
        }
    }

    private HealthCheck cloudinary() {
        if (!cloudinaryProperties.isConfigured()) {
            return new HealthCheck("Cloudinary", Status.WARN, "Chưa cấu hình lưu trữ tệp");
        }
        try {
            cloudinary.api().ping(Map.of());
            return new HealthCheck("Cloudinary", Status.OK, "Kết nối lưu trữ tệp hoạt động");
        } catch (Exception ignored) {
            return new HealthCheck("Cloudinary", Status.FAIL, "Không kết nối được dịch vụ lưu trữ tệp");
        }
    }

    private HealthCheck memory() {
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        long max = runtime.maxMemory();
        int percent = max <= 0 ? 0 : (int) Math.min(100, used * 100 / max);
        Status status = percent >= 95 ? Status.FAIL : percent >= 85 ? Status.WARN : Status.OK;
        String detail = mib(used) + " / " + mib(max) + " MiB đã dùng (" + percent + "%)";
        return new HealthCheck("Bộ nhớ JVM", status, detail);
    }

    private HealthCheck application() {
        BuildProperties build = buildProperties.getIfAvailable();
        String version = build == null ? fallbackVersion : build.getVersion();
        String configuration = appProperties.getBaseUrl() == null || appProperties.getBaseUrl().isBlank()
                ? "URL ứng dụng chưa cấu hình" : "URL ứng dụng đã cấu hình";
        return new HealthCheck("Ứng dụng QAUTE", Status.OK,
                "Phiên bản " + version + " · " + configuration);
    }

    private static long mib(long bytes) {
        return bytes / (1024 * 1024);
    }

    private String healthLink() {
        try {
            URI configured = URI.create(appProperties.getBaseUrl());
            if (configured.getHost() == null || !("https".equalsIgnoreCase(configured.getScheme())
                    || "http".equalsIgnoreCase(configured.getScheme()))) {
                return "/admin/health";
            }
            return new URI(configured.getScheme(), null, configured.getHost(),
                    configured.getPort(), "/admin/health", null, null).toString();
        } catch (Exception ignored) {
            return "/admin/health";
        }
    }
}
