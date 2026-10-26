package vn.edu.hcmute.qaute.service.notification;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.config.AppProperties;
import vn.edu.hcmute.qaute.dto.view.NotificationView;
import vn.edu.hcmute.qaute.dto.view.PreferenceView;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.dto.request.student.PreferenceInput;
import vn.edu.hcmute.qaute.entity.notification.Notification;
import vn.edu.hcmute.qaute.entity.notification.NotificationPreference;
import vn.edu.hcmute.qaute.repository.notification.NotificationPreferenceRepository;
import vn.edu.hcmute.qaute.repository.notification.NotificationRepository;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;

// Xử lý nghiệp vụ Notification trong hệ thống.
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static final List<NotificationType> STUDENT_PREFERENCE_TYPES = List.of(
            NotificationType.TICKET_CREATED,
            NotificationType.TICKET_ASSIGNED,
            NotificationType.TICKET_ADVISOR_REPLIED,
            NotificationType.TICKET_INFO_REQUESTED,
            NotificationType.TICKET_REMINDER_WAITING,
            NotificationType.TICKET_RESOLVED,
            NotificationType.TICKET_AUTO_CLOSED,
            NotificationType.TICKET_ESCALATED,
            NotificationType.TICKET_FORCE_CLOSED,
            NotificationType.APPOINTMENT_CONFIRMED,
            NotificationType.APPOINTMENT_CHANGED,
            NotificationType.APPOINTMENT_CANCELLED,
            NotificationType.APPOINTMENT_REMINDER,
            NotificationType.CHAT_MISSED,
            NotificationType.FAQ_FROM_TICKET_PUBLISHED,
            NotificationType.ANNOUNCEMENT_PUBLISHED,
            NotificationType.SECURITY_ACCOUNT_LOCKED,
            NotificationType.SECURITY_PASSWORD_CHANGED,
            NotificationType.SECURITY_ROLE_CHANGED);
    private static final Map<RoleCode, List<NotificationType>> PREFERENCE_TYPES_BY_ROLE = Map.of(
            RoleCode.STUDENT, STUDENT_PREFERENCE_TYPES,
            RoleCode.MANAGER, List.of(
                    NotificationType.TICKET_NEW_IN_QUEUE,
                    NotificationType.TICKET_ASSIGNED,
                    NotificationType.TICKET_STUDENT_REPLIED,
                    NotificationType.TICKET_REOPENED,
                    NotificationType.TICKET_ESCALATED,
                    NotificationType.SLA_AT_RISK,
                    NotificationType.SLA_BREACHED,
                    NotificationType.REVIEW_LOW_RATING,
                    NotificationType.APPOINTMENT_CONFIRMED,
                    NotificationType.APPOINTMENT_CHANGED,
                    NotificationType.APPOINTMENT_CANCELLED,
                    NotificationType.APPOINTMENT_REMINDER,
                    NotificationType.CHAT_WAITING_NEW,
                    NotificationType.ANNOUNCEMENT_PUBLISHED,
                    NotificationType.SECURITY_ACCOUNT_LOCKED,
                    NotificationType.SECURITY_PASSWORD_CHANGED,
                    NotificationType.SECURITY_ROLE_CHANGED),
            RoleCode.ADMIN, List.of(
                    NotificationType.SLA_BREACHED,
                    NotificationType.REVIEW_LOW_RATING,
                    NotificationType.FAQ_REVIEW_REQUESTED,
                    NotificationType.ANNOUNCEMENT_PUBLISHED,
                    NotificationType.SECURITY_ACCOUNT_LOCKED,
                    NotificationType.SECURITY_PASSWORD_CHANGED,
                    NotificationType.SECURITY_ROLE_CHANGED));

    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final EmailService emailService;
    private final UserLookupService userLookupService;
    private final ApplicationEventPublisher eventPublisher;
    private final AppProperties appProperties;

    // Khởi tạo dịch vụ với các thành phần lưu trữ, gửi email và cấu hình ứng dụng.
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
    // Tạo thông báo và gửi email theo tùy chọn của người dùng.
    public void notifyUser(Long userId, NotificationType type, String title, String body,
                           String linkUrl, String entityType, Long entityId) {
        if (userId == null || type == null) {
            return;
        }

        NotificationPreference preference = preferenceRepository.findByUserIdAndEventType(userId, type).orElse(null);
        boolean inAppEnabled = preference != null ? preference.isInAppEnabled() : true;
        boolean emailEnabled = preference != null ? preference.isEmailEnabled() : type.isDefaultEmail();

        // Lưu thông báo trong ứng dụng rồi phát sự kiện cập nhật cho người nhận.
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

        // Gửi email nếu sự kiện bắt buộc hoặc người dùng đã bật tùy chọn này.
        if (type.isMandatoryEmail() || emailEnabled) {
            sendEmail(userId, title, body, linkUrl);
        }
    }

    @Override
    // Gửi cùng một thông báo đến các người dùng không trùng lặp.
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
    // Chuyển thông tin dạng bản đồ thành nội dung thông báo rồi gửi đi.
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

    // Gửi email thông báo kèm liên kết chi tiết đến người dùng.
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

    @Override
    @Transactional(readOnly = true)
    // Lấy trang thông báo của người dùng, có thể chỉ gồm thông báo chưa đọc.
    public Page<NotificationView> list(Long userId, boolean unreadOnly, Pageable p) {
        if (userId == null) {
            return Page.empty(p);
        }
        Page<Notification> page = unreadOnly
                ? notificationRepository.findByUserIdAndDeletedAtIsNullAndReadAtIsNullOrderByIdDesc(userId, p)
                : notificationRepository.findByUserIdAndDeletedAtIsNullOrderByIdDesc(userId, p);
        return page.map(NotificationView::from);
    }

    @Override
    @Transactional(readOnly = true)
    // Đếm số thông báo chưa đọc của người dùng.
    public long unreadCount(Long userId) {
        if (userId == null) {
            return 0L;
        }
        return notificationRepository.countByUserIdAndDeletedAtIsNullAndReadAtIsNull(userId);
    }

    @Override
    // Đánh dấu thông báo thuộc người dùng là đã đọc.
    public void markRead(Long userId, Long id) {
        if (userId == null || id == null) {
            throw new NotFoundException("Thông báo không tồn tại");
        }
        Notification notification = notificationRepository.findById(id)
                .filter(n -> n.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("Thông báo không tồn tại"));

        if (!Objects.equals(notification.getUserId(), userId)) {
            throw new ForbiddenException("Không có quyền thao tác trên thông báo này");
        }

        if (notification.getReadAt() == null) {
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }

    @Override
    // Đánh dấu toàn bộ thông báo của người dùng là đã đọc.
    public void markAllRead(Long userId) {
        if (userId == null) {
            return;
        }
        notificationRepository.markAllRead(userId, LocalDateTime.now());
    }

    @Override
    // Ẩn thông báo thuộc người dùng khỏi danh sách hiển thị.
    public void hide(Long userId, Long id) {
        if (userId == null || id == null) {
            throw new NotFoundException("Thông báo không tồn tại");
        }
        Notification notification = notificationRepository.findById(id)
                .filter(n -> n.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("Thông báo không tồn tại"));

        if (!Objects.equals(notification.getUserId(), userId)) {
            throw new ForbiddenException("Không có quyền thao tác trên thông báo này");
        }

        notification.setDeletedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    // Lấy tùy chọn thông báo hiện có cùng các giá trị mặc định.
    public List<PreferenceView> getPreferences(Long userId) {
        return getPreferences(userId, RoleCode.STUDENT);
    }

    @Override
    @Transactional(readOnly = true)
    // Lấy tùy chọn theo các loại thông báo mà vai trò có thể nhận.
    public List<PreferenceView> getPreferences(Long userId, RoleCode role) {
        if (userId == null) return List.of();
        List<NotificationType> preferenceTypes = preferenceTypesFor(role);
        Map<NotificationType, NotificationPreference> existing = new EnumMap<>(NotificationType.class);
        preferenceRepository.findByUserId(userId).forEach(preference ->
                existing.put(preference.getEventType(), preference));
        return preferenceTypes.stream().map(type -> {
            NotificationPreference preference = existing.get(type);
            boolean emailLocked = type.isMandatoryEmail();
            boolean inApp = preference == null || preference.isInAppEnabled();
            boolean email = emailLocked || (preference == null ? type.isDefaultEmail() : preference.isEmailEnabled());
            return new PreferenceView(type, type.getLabel(), inApp, email, emailLocked);
        }).toList();
    }

    @Override
    // Lưu các tùy chọn nhận thông báo hợp lệ của người dùng.
    public void savePreferences(Long userId, Map<NotificationType, PreferenceInput> input) {
        savePreferences(userId, RoleCode.STUDENT, input);
    }

    @Override
    // Chỉ lưu các loại thông báo được cung cấp cho vai trò người dùng.
    public void savePreferences(Long userId, RoleCode role, Map<NotificationType, PreferenceInput> input) {
        if (userId == null || input == null || input.isEmpty()) return;
        List<NotificationType> preferenceTypes = preferenceTypesFor(role);
        Map<NotificationType, NotificationPreference> existing = new EnumMap<>(NotificationType.class);
        preferenceRepository.findByUserId(userId).forEach(preference ->
                existing.put(preference.getEventType(), preference));

        for (Map.Entry<NotificationType, PreferenceInput> entry : input.entrySet()) {
            NotificationType type = entry.getKey();
            PreferenceInput value = entry.getValue();
            if (type == null || value == null || !preferenceTypes.contains(type)) continue;
            NotificationPreference preference = existing.get(type);
            if (preference == null) {
                preference = NotificationPreference.builder().userId(userId).eventType(type).build();
            }
            preference.setInAppEnabled(value.inApp());
            preference.setEmailEnabled(type.isMandatoryEmail() || value.email());
            preferenceRepository.save(preference);
        }
    }

    private static List<NotificationType> preferenceTypesFor(RoleCode role) {
        List<NotificationType> preferenceTypes = PREFERENCE_TYPES_BY_ROLE.get(Objects.requireNonNull(role, "role"));
        if (preferenceTypes == null) {
            throw new IllegalArgumentException("No notification preferences configured for role " + role);
        }
        return preferenceTypes;
    }
}
