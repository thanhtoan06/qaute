package vn.edu.hcmute.qaute.service.identity;

import vn.edu.hcmute.qaute.dto.request.student.ProfileForm;
import vn.edu.hcmute.qaute.dto.view.ProfileView;

public interface StudentProfileService {

    ProfileView getProfile(Long userId);

    void updateProfile(Long userId, ProfileForm form);

    void updateAvatar(Long userId, Long assetId);
}

