package vn.edu.hcmute.qaute.service.identity;

import java.util.Collection;
import java.util.Map;
import vn.edu.hcmute.qaute.dto.view.StudentSummary;
import vn.edu.hcmute.qaute.dto.view.UserSummary;

// Xử lý nghiệp vụ UserLookup trong hệ thống.
public interface UserLookupService {

    // Lấy thông tin tóm tắt của người dùng.
    UserSummary getSummary(Long userId);

    // Lấy thông tin tóm tắt cho nhiều người dùng.
    Map<Long, UserSummary> getSummaries(Collection<Long> ids);

    // Lấy thông tin tóm tắt hồ sơ sinh viên.
    StudentSummary getStudentSummary(Long userId);
}
