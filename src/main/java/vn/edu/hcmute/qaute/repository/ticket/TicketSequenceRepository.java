package vn.edu.hcmute.qaute.repository.ticket;

import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.ticket.TicketSequence;

public interface TicketSequenceRepository extends JpaRepository<TicketSequence, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM TicketSequence s WHERE s.year = :year")
    Optional<TicketSequence> lockByYear(@Param("year") Integer year);
}
