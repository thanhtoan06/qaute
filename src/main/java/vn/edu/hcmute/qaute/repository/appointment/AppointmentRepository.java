package vn.edu.hcmute.qaute.repository.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Collection;
import java.util.List;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    List<Appointment> findBySlotIdInAndStatusIn(Collection<Long> slotIds, Collection<AppointmentStatus> statuses);
    List<Appointment> findBySlotIdIn(Collection<Long> slotIds);
}
