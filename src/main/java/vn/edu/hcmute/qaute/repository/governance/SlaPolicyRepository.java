package vn.edu.hcmute.qaute.repository.governance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.entity.governance.SlaPolicy;

public interface SlaPolicyRepository extends JpaRepository<SlaPolicy, Long> {

    Optional<SlaPolicy> findFirstByCategoryIdAndPriorityAndActiveTrue(Long categoryId, TicketPriority priority);

    Optional<SlaPolicy> findFirstByCategoryIdIsNullAndPriorityAndActiveTrue(TicketPriority priority);

    List<SlaPolicy> findAllByOrderByCategoryIdAscPriorityAsc();

    Optional<SlaPolicy> findByCategoryIdAndPriority(Long categoryId, TicketPriority priority);
}
