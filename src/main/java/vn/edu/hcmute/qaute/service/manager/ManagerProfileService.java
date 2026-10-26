package vn.edu.hcmute.qaute.service.manager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.manager.ManagerProfileForm;
import vn.edu.hcmute.qaute.dto.view.ManagerProfileView;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerProfileRepository;
import vn.edu.hcmute.qaute.service.media.MediaService;

// Xử lý nghiệp vụ ManagerProfile trong hệ thống.
@Service
@Transactional
public class ManagerProfileService {
    private final UserRepository users;
    private final ManagerProfileRepository profiles;
    private final DepartmentRepository departments;
    private final MediaAssetRepository assets;
    private final MediaService media;

    // Khởi tạo dịch vụ hồ sơ tư vấn viên.
    public ManagerProfileService(UserRepository users, ManagerProfileRepository profiles,
            DepartmentRepository departments, MediaAssetRepository assets, MediaService media) {
        this.users = users;
        this.profiles = profiles;
        this.departments = departments;
        this.assets = assets;
        this.media = media;
    }

    // Lấy hồ sơ và trạng thái trực của tư vấn viên.
    @Transactional(readOnly = true)
    public ManagerProfileView get(Long userId) {
        User user = requireManager(userId);
        ManagerProfile profile = profiles.findByUserId(userId).orElse(null);
        String avatarUrl = null;
        if (user.getAvatarAssetId() != null) {
            try {
                avatarUrl = media.publicUrl(user.getAvatarAssetId());
            } catch (RuntimeException ignored) {
                // Hồ sơ vẫn hiển thị nếu ảnh cũ không còn khả dụng.
            }
        }
        String departmentName = profile == null || profile.getDepartmentId() == null ? null
                : departments.findById(profile.getDepartmentId()).map(department -> department.getName())
                        .orElse(null);
        return new ManagerProfileView(user.getFullName(), user.getEmail(), user.getPhone(),
                avatarUrl, profile == null ? null : profile.getEmployeeCode(),
                profile == null ? null : profile.getJobTitle(), departmentName,
                profile == null ? null : profile.getBio(),
                profile == null ? AvailabilityStatus.OFFLINE : profile.getAvailabilityStatus(),
                profile == null ? null : profile.getLastSeenAt());
    }

    // Cập nhật thông tin hồ sơ tư vấn viên.
    public void update(Long userId, ManagerProfileForm form) {
        if (form == null || form.getFullName() == null
                || form.getFullName().trim().length() < 2
                || form.getFullName().trim().length() > 100) {
            throw new BadRequestException("Họ và tên phải từ 2 đến 100 ký tự");
        }
        if (form.getPhone() != null && !form.getPhone().isBlank()
                && !form.getPhone().matches("^(0|\\+84)\\d{9}$")) {
            throw new BadRequestException("Số điện thoại không đúng định dạng");
        }
        if (form.getJobTitle() != null && form.getJobTitle().length() > 100) {
            throw new BadRequestException("Chức danh tối đa 100 ký tự");
        }
        if (form.getBio() != null && form.getBio().length() > 500) {
            throw new BadRequestException("Giới thiệu tối đa 500 ký tự");
        }
        User user = requireManager(userId);
        user.setFullName(form.getFullName().trim());
        user.setPhone(blankToNull(form.getPhone()));
        users.save(user);

        ManagerProfile profile = profiles.findByUserId(userId)
                .orElseGet(() -> ManagerProfile.builder().userId(userId).build());
        profile.setJobTitle(blankToNull(form.getJobTitle()));
        profile.setBio(blankToNull(form.getBio()));
        profiles.save(profile);
    }

    // Gắn ảnh đại diện mới cho tư vấn viên.
    public void updateAvatar(Long userId, Long assetId) {
        User user = requireManager(userId);
        if (assetId == null) throw new BadRequestException("Ảnh đại diện không hợp lệ");
        MediaAsset asset = assets.findById(assetId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ảnh"));
        if (asset.getStatus() != MediaStatus.ORPHAN || asset.getUsageType() != MediaUsageType.AVATAR) {
            throw new BadRequestException("Ảnh đại diện không hợp lệ hoặc đã được sử dụng");
        }
        if (!Objects.equals(asset.getOwnerId(), userId)) {
            throw new ForbiddenException("Bạn không sở hữu ảnh này");
        }
        Long previous = user.getAvatarAssetId();
        media.attach(assetId, MediaUsageType.AVATAR, userId);
        user.setAvatarAssetId(assetId);
        users.save(user);
        if (previous != null && !previous.equals(assetId)) {
            try {
                media.delete(previous);
            } catch (RuntimeException ignored) {
                // Avatar mới đã được gắn; lỗi dọn ảnh cũ không cản cập nhật hồ sơ.
            }
        }
    }

    // Cập nhật trạng thái trực và thời điểm hoạt động.
    public void setStatus(Long userId, AvailabilityStatus status) {
        requireManager(userId);
        if (status == null) throw new BadRequestException("Trạng thái trực không hợp lệ");
        ManagerProfile profile = profiles.findByUserId(userId)
                .orElseGet(() -> ManagerProfile.builder().userId(userId).build());
        profile.setAvailabilityStatus(status);
        profile.setLastSeenAt(LocalDateTime.now());
        profiles.save(profile);
    }

    // Ghi nhận thời điểm tư vấn viên vừa hoạt động.
    public void touchLastSeen(Long userId) {
        profiles.touchLastSeen(userId, LocalDateTime.now());
    }

    // Lấy trạng thái trực hiện tại của tư vấn viên.
    @Transactional(readOnly = true)
    public AvailabilityStatus currentStatus(Long userId) {
        return profiles.findByUserId(userId).map(ManagerProfile::getAvailabilityStatus)
                .orElse(AvailabilityStatus.OFFLINE);
    }

    // Đưa các trạng thái trực quá hạn về ngoại tuyến.
    public int expireStalePresence(LocalDateTime cutoff) {
        return profiles.markStaleOffline(List.of(AvailabilityStatus.ONLINE, AvailabilityStatus.BUSY),
                AvailabilityStatus.OFFLINE, cutoff);
    }

    // Kiểm tra tài khoản tồn tại và có vai trò tư vấn viên.
    private User requireManager(Long userId) {
        User user = users.findById(userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy tài khoản Manager"));
        if (user.getRole() == null || user.getRole().getCode() != RoleCode.MANAGER) {
            throw new ForbiddenException("Tài khoản không có quyền Manager");
        }
        return user;
    }

    // Chuẩn hóa chuỗi rỗng thành giá trị null.
    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
