package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.manager.ManagerTicketDetailService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;

@ExtendWith(MockitoExtension.class)
class AdminTicketServiceTest {
    @Mock TicketRepository tickets;
    @Mock CategoryRepository categoryRepository;
    @Mock CategoryQueryService categories;
    @Mock UserLookupService users;
    @Mock ManagerTicketDetailService details;
    @Mock TicketWorkflowService workflow;
    @Mock AuditService audit;
    @InjectMocks AdminTicketService service;

    @Test
    void reassignAuditsActualBeforeAndAfterState() {
        Ticket before = Ticket.builder().status(TicketStatus.ASSIGNED)
                .priority(TicketPriority.NORMAL).categoryId(5L).currentAssigneeId(10L).build();
        Ticket after = Ticket.builder().status(TicketStatus.ASSIGNED)
                .priority(TicketPriority.NORMAL).categoryId(5L).currentAssigneeId(20L).build();
        String reason = "Chuyển cho người phụ trách phù hợp";
        when(tickets.findById(7L)).thenReturn(Optional.of(before));
        when(workflow.assign(7L, 20L, 1L, reason)).thenReturn(after);

        service.reassign(7L, 20L, reason, 1L);

        verify(audit).log(eq(AuditActions.TICKET_REASSIGNED), eq("ticket"), eq(7L),
                contains("assigneeId=10"), contains("assigneeId=20"));
    }

    @Test
    void failedWorkflowDoesNotWriteAdminAudit() {
        Ticket before = Ticket.builder().status(TicketStatus.CLOSED).build();
        when(tickets.findById(7L)).thenReturn(Optional.of(before));
        when(workflow.forceClose(7L, 1L, "Lý do đóng bắt buộc"))
                .thenThrow(new BadRequestException("Đã đóng"));

        assertThrows(BadRequestException.class,
                () -> service.forceClose(7L, "Lý do đóng bắt buộc", 1L));

        verify(audit, never()).log(any(), any(), any(), any(), any());
    }
}
