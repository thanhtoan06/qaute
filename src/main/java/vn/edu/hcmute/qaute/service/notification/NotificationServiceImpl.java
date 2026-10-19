package vn.edu.hcmute.qaute.service.notification;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.config.AppProperties;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.notification.Notification;
import vn.edu.hcmute.qaute.entity.notification.NotificationPreference;
import vn.edu.hcmute.qaute.repository.notification.NotificationPreferenceRepository;
import vn.edu.hcmute.qaute.repository.notification.NotificationRepository;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final EmailService emailService;
    private final UserLookupService userLookupService;
    private final ApplicationEventPublisher eventPublisher;
    private final AppProperties appProperties;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   NotificationPreferenceRepository preferenceRepository,
                                   EmailService emailService,
                                   UserLookupService userLookupService,
                                   ApplicationEventPublisher eventPublisher,
                                   AppProperties appProperties) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
        this.emailService = emailService;
        this.userLookupService = userLookupService;
        this.eventPublisher = eventPublisher;
        this.appProperties = appProperties;
    }

    @Override
    public void notifyUser(Long userId, NotificationType type, String title, String body,
                           String linkUrl, String entityType, Long entityId) {
        if (userId == null || type == null) {
            return;
        }

        NotificationPreference preference = preferenceRepository.findByUserIdAndEventType(userId, type).orElse(null);
        boolean inAppEnabled = preference != null ? preference.isInAppEnabled() : true;
        boolean emailEnabled = preference != null ? preference.isEmailEnabled() : type.isDefaultEmail();

        if (inAppEnabled) {
            Notification notification = notificationRepository.save(Notification.builder()
                    .userId(userId)
                    .type(type)
                    .title(title)
                    .body(body)
                    .linkUrl(linkUrl)
                    .entityType(entityType)
                    .entityId(entityId)
                    .build());
            eventPublisher.publishEvent(new NotificationCreatedEvent(
                    notification.getId(),
                    userId,
                    type,
                    title,
                    body,
                    linkUrl,
                    notification.getCreatedAt() != null ? notification.getCreatedAt() : LocalDateTime.now()
            ));
        }

        if (type.isMandatoryEmail() || emailEnabled) {
            sendEmail(userId, title, body, linkUrl);
        }
    }

    @Override
    public void notifyUsers(Collection<Long> userIds, NotificationType type, String title, String body,
                            String linkUrl, String entityType, Long entityId) {
        if (userIds == null || userIds.isEmpty()) {
            return;
        }
        userIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .forEach(userId -> notifyUser(userId, type, title, body, linkUrl, entityType, entityId));
    }

    @Override
    public void notifyUser(Long userId, NotificationType type, Map<String, String> details) {
        if (userId == null || type == null) {
            return;
        }
        String title = details != null && details.containsKey("title") ? details.get("title") : type.getLabel();
        String body = details != null && details.containsKey("reason")
                ? details.get("reason")
                : (details != null && details.containsKey("body") ? details.get("body") : type.getLabel());
        String link = details != null ? (details.get("link") != null ? details.get("link") : details.get("linkUrl")) : null;
        String entityType = details != null ? details.get("entityType") : null;
        Long entityId = null;
        if (details != null && details.containsKey("faqId")) {
            entityType = "FAQ";
            try {
                entityId = Long.valueOf(details.get("faqId"));
            } catch (Exception ignored) {
            }
        } else if (details != null && details.containsKey("entityId")) {
            try {
                entityId = Long.valueOf(details.get("entityId"));
            } catch (Exception ignored) {
            }
        }
        notifyUser(userId, type, title, body, link, entityType, entityId);
    }

    private void sendEmail(Long userId, String title, String body, String linkUrl) {
        try {
            UserSummary user = userLookupService.getSummary(userId);
            if (user == null || user.email() == null || user.email().isBlank()) {
                return;
            }

            String base = appProperties.getBaseUrl();
            if (base == null || base.isBlank()) {
                base = "http://localhost:8090";
            }
            while (base.endsWith("/")) {
                base = base.substring(0, base.length() - 1);
            }

            String fullUrl;
            if (linkUrl == null || linkUrl.isBlank()) {
                fullUrl = base;
            } else if (linkUrl.startsWith("http://") || linkUrl.startsWith("https://")) {
                fullUrl = linkUrl;
            } else {
                fullUrl = base + (linkUrl.startsWith("/") ? linkUrl : "/" + linkUrl);
            }

            Map<String, String> model = Map.of(
                    "title", title != null ? title : "",
                    "body", body != null ? body : "",
                    "linkUrl", fullUrl,
                    "linkLabel", "Xem chi tiết"
            );
            emailService.sendTemplated(user.email(), title, "generic", model);
        } catch (Exception ignored) {
        }
    }
}

