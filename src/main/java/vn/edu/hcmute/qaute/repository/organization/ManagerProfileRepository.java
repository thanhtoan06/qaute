package vn.edu.hcmute.qaute.repository.organization;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;

public interface ManagerProfileRepository extends JpaRepository<ManagerProfile, Long> {

    Optional<ManagerProfile> findByUserId(Long userId);

    List<ManagerProfile> findByUserIdIn(Collection<Long> userIds);

    @Modifying
    @Query("UPDATE ManagerProfile p SET p.lastSeenAt = :seenAt WHERE p.userId = :userId")
    int touchLastSeen(@Param("userId") Long userId, @Param("seenAt") LocalDateTime seenAt);

    @Modifying
    @Query("UPDATE ManagerProfile p SET p.availabilityStatus = :offline "
            + "WHERE p.availabilityStatus IN :active AND "
            + "(p.lastSeenAt IS NULL OR p.lastSeenAt < :cutoff)")
    int markStaleOffline(@Param("active") Collection<AvailabilityStatus> active,
            @Param("offline") AvailabilityStatus offline, @Param("cutoff") LocalDateTime cutoff);
}
