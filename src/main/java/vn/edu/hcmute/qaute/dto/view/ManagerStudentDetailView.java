package vn.edu.hcmute.qaute.dto.view;

import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

public record ManagerStudentDetailView(
        StudentSummary student,
        Page<TicketItem> tickets,
        Page<AppointmentItem> appointments,
        Page<ChatItem> chats) {

    public record TicketItem(Long id, String code, String title, TicketStatus status,
                             LocalDateTime createdAt) {
    }

    public record AppointmentItem(Long id, String topic, AppointmentStatus status,
                                  LocalDateTime startAt) {
    }

    public record ChatItem(Long id, String question, ChatSessionStatus status,
                           LocalDateTime startedAt) {
    }
}
