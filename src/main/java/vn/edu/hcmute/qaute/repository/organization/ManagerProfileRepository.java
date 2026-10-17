package vn.edu.hcmute.qaute.repository.organization;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;

public interface ManagerProfileRepository extends JpaRepository<ManagerProfile, Long> {

    Optional<ManagerProfile> findByUserId(Long userId);

    List<ManagerProfile> findByUserIdIn(Collection<Long> userIds);
}
