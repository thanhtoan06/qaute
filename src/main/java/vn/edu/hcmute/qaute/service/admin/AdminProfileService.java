package vn.edu.hcmute.qaute.service.admin;

import jakarta.validation.Validator;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.MediaDeliveryType;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.admin.AdminProfileForm;
import vn.edu.hcmute.qaute.dto.request.student.ChangePasswordForm;
import vn.edu.hcmute.qaute.dto.view.AdminProfileView;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.identity.AccountActivationTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.PasswordResetTokenRepository;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.PasswordService;
import vn.edu.hcmute.qaute.service.media.MediaService;

// Xử lý nghiệp vụ AdminProfile trong hệ thống.
@Service
@Transactional
public class AdminProfileService {
    private final UserRepository users;
    private final MediaAssetRepository assets;
    private final MediaService media;
    private final PasswordService passwords;
    private final AuditService audit;
    private final Validator validator;
    private final PasswordResetTokenRepository resetTokens;
    private final AccountActivationTokenRepository activationTokens;

    // Khởi tạo dịch vụ hồ sơ quản trị viên.
    public AdminProfileService(UserRepository users, MediaAssetRepository assets, MediaService media,
            PasswordService passwords, AuditService audit, Validator validator,
            PasswordResetTokenRepository resetTokens, AccountActivationTokenRepository activationTokens) {
        this.users = users;
        this.assets = assets;
        this.media = media;
        this.passwords = passwords;
        this.audit = audit;
        this.validator = validator;
        this.resetTokens = resetTokens;
        this.activationTokens = activationTokens;
    }

    // Lấy thông tin hồ sơ quản trị viên.
    @Transactional(readOnly = true)
    public AdminProfileView get(Long userId) {
        User user = requireAdmin(userId);
        String avatarUrl = user.getAvatarAssetId() == null ? null
                : assets.findById(user.getAvatarAssetId())
                        .filter(asset -> asset.getDeliveryType() == MediaDeliveryType.UPLOAD
                                && asset.getStatus() != MediaStatus.DELETED
                                && asset.getSecureUrl() != null && !asset.getSecureUrl().isBlank())
                        .map(asset -> media.publicUrl(asset.getId())).orElse(null);
        return new AdminProfileView(user.getFullName(), user.getEmail(), user.getPhone(), avatarUrl);
    }

    // Cập nhật thông tin cá nhân của quản trị viên.
    public void update(Long userId, AdminProfileForm form) {
        User user = requireAdmin(userId);
        if (form == null) throw new BadRequestException("Thông tin hồ sơ không hợp lệ");
        form.setFullName(trim(form.getFullName()));
        form.setEmail(form.getEmail() == null ? null : form.getEmail().trim().toLowerCase(Locale.ROOT));
        form.setPhone(blankToNull(form.getPhone()));
        validate(form);
        users.findByEmailIgnoreCase(form.getEmail())
                .filter(existing -> !Objects.equals(existing.getId(), userId))
                .ifPresent(existing -> {
                    throw new DuplicateException("email", "Email đã được sử dụng bởi tài khoản khác");
                });
        String before = snapshot(user);
        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        try {
            users.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateException("email", "Email đã được sử dụng bởi tài khoản khác");
        }
        audit.log(AuditActions.USER_UPDATED, "user", userId, before, snapshot(user));
    }

    // Gắn ảnh đại diện mới cho quản trị viên.
    public void updateAvatar(Long userId, Long assetId) {
        User user = requireAdmin(userId);
        if (assetId == null || assetId <= 0) throw new BadRequestException("Ảnh đại diện không hợp lệ");
        MediaAsset asset = assets.findById(assetId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ảnh đại diện"));
        if (!Objects.equals(asset.getOwnerId(), userId)) {
            throw new ForbiddenException("Bạn không sở hữu ảnh này");
        }
        if (asset.getStatus() != MediaStatus.ORPHAN || asset.getUsageType() != MediaUsageType.AVATAR) {
            throw new BadRequestException("Ảnh đại diện không hợp lệ hoặc đã được sử dụng");
        }
        Long previous = user.getAvatarAssetId();
        media.attach(assetId, MediaUsageType.AVATAR, userId);
        user.setAvatarAssetId(assetId);
        users.save(user);
        if (previous != null && !previous.equals(assetId)) {
            media.delete(previous);
        }
        audit.log(AuditActions.USER_UPDATED, "user", userId,
                "avatarAssetId=" + previous, "avatarAssetId=" + assetId);
    }

    // Đổi mật khẩu và vô hiệu hóa liên kết cũ.
    public void changePassword(Long userId, ChangePasswordForm form) {
        requireAdmin(userId);
        if (form == null) throw new BadRequestException("Thông tin đổi mật khẩu không hợp lệ");
        validate(form);
        if (!Objects.equals(form.getNewPassword(), form.getConfirmPassword())) {
            throw new BadRequestException("Mật khẩu xác nhận không khớp",
                    Map.of("confirmPassword", "Mật khẩu xác nhận không khớp"));
        }
        if (form.getNewPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new BadRequestException("Mật khẩu mới vượt giới hạn mã hóa",
                    Map.of("newPassword", "Mật khẩu mới không được vượt quá 72 byte UTF-8"));
        }
        passwords.changePassword(userId, form.getCurrentPassword(), form.getNewPassword());
        LocalDateTime now = LocalDateTime.now();
        resetTokens.findByUserIdAndUsedAtIsNull(userId).forEach(token -> token.setUsedAt(now));
        activationTokens.findByUserIdAndUsedAtIsNull(userId).forEach(token -> token.setUsedAt(now));
    }

    // Xác thực quyền truy cập hồ sơ của quản trị viên.
    private User requireAdmin(Long userId) {
        if (!SecurityUtils.hasRole(RoleCode.ADMIN)
                || !Objects.equals(SecurityUtils.requireUserId(), userId)) {
            throw new ForbiddenException("Chỉ quản trị viên được truy cập hồ sơ của chính mình");
        }
        User user = users.findById(userId).orElseThrow(NotFoundException::new);
        if (user.getRole() == null || user.getRole().getCode() != RoleCode.ADMIN) {
            throw new ForbiddenException("Tài khoản không có quyền quản trị");
        }
        return user;
    }

    // Kiểm tra dữ liệu bằng các ràng buộc xác thực.
    private void validate(Object form) {
        Map<String, String> errors = new LinkedHashMap<>();
        validator.validate(form).forEach(error ->
                errors.putIfAbsent(error.getPropertyPath().toString(), error.getMessage()));
        if (!errors.isEmpty()) throw new BadRequestException("Vui lòng kiểm tra thông tin đã nhập", errors);
    }

    // Tạo bản tóm tắt hồ sơ để ghi nhật ký.
    private static String snapshot(User user) {
        return "fullName=" + user.getFullName() + ";email=" + user.getEmail() + ";phone=" + user.getPhone();
    }

    // Cắt khoảng trắng hai đầu chuỗi, giữ nguyên null.
    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    // Chuyển chuỗi rỗng thành null sau khi cắt khoảng trắng.
    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
