package vn.edu.hcmute.qaute.service.identity;

import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.student.ProfileForm;
import vn.edu.hcmute.qaute.dto.view.ProfileView;
import vn.edu.hcmute.qaute.entity.identity.Faculty;
import vn.edu.hcmute.qaute.entity.identity.StudentProfile;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.identity.FacultyRepository;
import vn.edu.hcmute.qaute.repository.identity.StudentProfileRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.service.media.MediaService;

@Service
@Transactional
public class StudentProfileServiceImpl implements StudentProfileService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final FacultyRepository facultyRepository;
    private final MediaAssetRepository mediaAssetRepository;
    private final MediaService mediaService;

    public StudentProfileServiceImpl(UserRepository userRepository,
                                     StudentProfileRepository studentProfileRepository,
                                     FacultyRepository facultyRepository,
                                     MediaAssetRepository mediaAssetRepository,
                                     MediaService mediaService) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.facultyRepository = facultyRepository;
        this.mediaAssetRepository = mediaAssetRepository;
        this.mediaService = mediaService;
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileView getProfile(Long userId) {
        if (userId == null) {
            throw new NotFoundException("Người dùng không tồn tại");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        StudentProfile profile = studentProfileRepository.findByUserId(userId).orElse(null);

        String avatarUrl = null;
        if (user.getAvatarAssetId() != null) {
            try {
                avatarUrl = mediaService.publicUrl(user.getAvatarAssetId());
            } catch (Exception ignored) {
            }
        }

        Long facultyId = profile != null ? profile.getFacultyId() : null;
        String facultyName = null;
        if (facultyId != null) {
            facultyName = facultyRepository.findById(facultyId).map(Faculty::getName).orElse(null);
        }

        return new ProfileView(
                user.getFullName(),
                user.getEmail(),
                user.getMssv(),
                user.getPhone(),
                avatarUrl,
                facultyId,
                facultyName,
                profile != null ? profile.getClassCode() : null,
                profile != null ? profile.getCohort() : null,
                profile != null ? profile.getProgram() : null
        );
    }

    @Override
    public void updateProfile(Long userId, ProfileForm form) {
        if (userId == null) {
            throw new NotFoundException("Người dùng không tồn tại");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        if (form.getFacultyId() != null) {
            Faculty faculty = facultyRepository.findById(form.getFacultyId())
                    .orElseThrow(() -> new BadRequestException("Khoa không tồn tại", Map.of("facultyId", "Khoa không tồn tại")));
            if (!faculty.isActive()) {
                throw new BadRequestException("Khoa đã ngừng hoạt động", Map.of("facultyId", "Khoa đã ngừng hoạt động"));
            }
        }

        user.setFullName(form.getFullName().trim());
        user.setPhone(form.getPhone() != null && !form.getPhone().isBlank() ? form.getPhone().trim() : null);
        userRepository.save(user);

        StudentProfile profile = studentProfileRepository.findByUserId(userId)
                .orElseGet(() -> StudentProfile.builder().userId(userId).build());

        profile.setFacultyId(form.getFacultyId());
        profile.setClassCode(form.getClassCode() != null && !form.getClassCode().isBlank() ? form.getClassCode().trim() : null);
        profile.setCohort(form.getCohort() != null && !form.getCohort().isBlank() ? form.getCohort().trim() : null);
        profile.setProgram(form.getProgram() != null && !form.getProgram().isBlank() ? form.getProgram().trim() : null);

        studentProfileRepository.save(profile);
    }

    @Override
    public void updateAvatar(Long userId, Long assetId) {
        if (userId == null) {
            throw new NotFoundException("Người dùng không tồn tại");
        }
        if (assetId == null) {
            throw new BadRequestException("Mã ảnh không hợp lệ");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        MediaAsset asset = mediaAssetRepository.findById(assetId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ảnh"));

        if (asset.getStatus() != MediaStatus.ORPHAN) {
            throw new BadRequestException("Ảnh đại diện không hợp lệ hoặc đã được sử dụng");
        }
        if (asset.getUsageType() != MediaUsageType.AVATAR) {
            throw new BadRequestException("Tệp tải lên không phải ảnh đại diện");
        }
        if (!Objects.equals(asset.getOwnerId(), userId)) {
            throw new ForbiddenException("Bạn không sở hữu ảnh này");
        }

        Long oldAvatarId = user.getAvatarAssetId();
        mediaService.attach(assetId, MediaUsageType.AVATAR, userId);

        if (oldAvatarId != null && !oldAvatarId.equals(assetId)) {
            try {
                mediaService.delete(oldAvatarId);
            } catch (Exception ignored) {
            }
        }

        user.setAvatarAssetId(assetId);
        userRepository.save(user);
    }
}

