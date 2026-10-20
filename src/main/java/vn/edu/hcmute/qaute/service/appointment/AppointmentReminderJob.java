package vn.edu.hcmute.qaute.service.appointment;

import java.time.LocalDateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentRepository;
import vn.edu.hcmute.qaute.service.notification.NotificationService;

@Component
public class AppointmentReminderJob {
    private final JobExecutionTemplate jobs;
    private final ReminderSender sender;

    public AppointmentReminderJob(JobExecutionTemplate jobs, ReminderSender sender) {
        this.jobs = jobs;
        this.sender = sender;
    }

    @Scheduled(fixedDelay = 300000)
    public void remind() {
        jobs.run("appointment-reminders", sender::sendDue);
    }

    @Component
    public static class ReminderSender {
        private final AppointmentRepository appointments;
        private final NotificationService notifications;

        public ReminderSender(AppointmentRepository appointments, NotificationService notifications) {
            this.appointments = appointments;
            this.notifications = notifications;
        }

        @Transactional
        public void sendDue() {
            LocalDateTime now = LocalDateTime.now();
            appointments.findDue24h(now, now.plusHours(24), PageRequest.of(0, 500)).forEach(appointment -> {
                if (appointments.markReminder24h(appointment.getId()) == 1) notifyBoth(appointment, "24 giờ");
            });
            appointments.findDue1h(now, now.plusHours(1), PageRequest.of(0, 500)).forEach(appointment -> {
                if (appointments.markReminder1h(appointment.getId()) == 1) notifyBoth(appointment, "1 giờ");
            });
        }

        private void notifyBoth(Appointment appointment, String when) {
            String title = "Nhắc lịch hẹn sau " + when;
            String body = appointment.getTopic();
            notifications.notifyUser(appointment.getStudentId(), NotificationType.APPOINTMENT_REMINDER,
                    title, body, "/student/appointments/" + appointment.getId(),
                    "APPOINTMENT", appointment.getId());
            notifications.notifyUser(appointment.getManagerId(), NotificationType.APPOINTMENT_REMINDER,
                    title, body, "/manager/appointments", "APPOINTMENT", appointment.getId());
        }
    }
}
