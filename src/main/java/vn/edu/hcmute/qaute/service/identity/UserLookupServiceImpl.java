package vn.edu.hcmute.qaute.service.identity;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.StudentSummary;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.identity.Faculty;
import vn.edu.hcmute.qaute.entity.identity.StudentProfile;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.identity.FacultyRepository;
import vn.edu.hcmute.qaute.repository.identity.StudentProfileRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;

@Service
@Transactional(readOnly = true)
public class UserLookupServiceImpl implements UserLookupService {

    private final UserRepository userRepository;
    private final MediaAssetRepository mediaAssetRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final FacultyRepository facultyRepository;

    public UserLookupServiceImpl(UserRepository userRepository,
                                 MediaAssetRepository mediaAssetRepository,
                                 StudentProfileRepository studentProfileRepository,
                                 FacultyRepository facultyRepository) {
        this.userRepository = userRepository;
        this.mediaAssetRepository = mediaAssetRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public UserSummary getSummary(Long userId) {
        if (userId == null) {
            throw new NotFoundException();
        }
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        return toSummary(user, loadAvatarUrl(user.getAvatarAssetId()));
    }

    @Override
    public Map<Long, UserSummary> getSummaries(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        List<User> users = userRepository.findAllById(ids);
        Map<Long, MediaAsset> avatars = loadAvatars(users);
        Map<Long, UserSummary> summaries = new HashMap<>();
        for (User user : users) {
            MediaAsset avatar = user.getAvatarAssetId() == null
                    ? null : avatars.get(user.getAvatarAssetId());
            summaries.put(user.getId(), toSummary(user, avatar == null ? null : avatar.getSecureUrl()));
        }
        return Map.copyOf(summaries);
    }

    @Override
    public StudentSummary getStudentSummary(Long userId) {
        if (userId == null) {
            throw new NotFoundException();
        }
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        StudentProfile profile = studentProfileRepository.findByUserId(userId).orElse(null);
        String facultyName = null;
        if (profile != null && profile.getFacultyId() != null) {
            facultyName = facultyRepository.findById(profile.getFacultyId())
                    .map(Faculty::getName)
                    .orElse(null);
        }
        return new StudentSummary(
                toSummary(user, loadAvatarUrl(user.getAvatarAssetId())),
                user.getPhone(),
                facultyName,
                profile == null ? null : profile.getClassCode(),
                profile == null ? null : profile.getCohort(),
                profile == null ? null : profile.getProgram());
    }

    private Map<Long, MediaAsset> loadAvatars(List<User> users) {
        List<Long> avatarIds = users.stream()
                .map(User::getAvatarAssetId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();
        if (avatarIds.isEmpty()) {
            return Map.of();
        }
        Map<Long, MediaAsset> result = new HashMap<>();
        for (MediaAsset asset : mediaAssetRepository.findAllById(avatarIds)) {
            result.put(asset.getId(), asset);
        }
        return result;
    }

    private String loadAvatarUrl(Long avatarAssetId) {
        if (avatarAssetId == null) {
            return null;
        }
        return mediaAssetRepository.findById(avatarAssetId)
                .map(MediaAsset::getSecureUrl)
                .orElse(null);
    }

    private UserSummary toSummary(User user, String avatarUrl) {
        return new UserSummary(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getMssv(),
                avatarUrl,
                user.getRole() == null ? null : user.getRole().getCode(),
                user.getStatus());
    }
}
