package vn.edu.hcmute.qaute.service.identity;

import java.util.Collection;
import java.util.Map;
import vn.edu.hcmute.qaute.dto.view.StudentSummary;
import vn.edu.hcmute.qaute.dto.view.UserSummary;

public interface UserLookupService {

    UserSummary getSummary(Long userId);

    Map<Long, UserSummary> getSummaries(Collection<Long> ids);

    StudentSummary getStudentSummary(Long userId);
}
