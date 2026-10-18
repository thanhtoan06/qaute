package vn.edu.hcmute.qaute.service.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.dto.request.manager.QueueFilter;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

@ExtendWith(MockitoExtension.class)
class ManagerQueueServiceTest {
    @Mock TicketRepository tickets;
    @Mock ManagerScopeService scopes;
    @Mock UserLookupService users;
    @Mock CategoryQueryService categories;
    @InjectMocks ManagerQueueService service;

    @Test
    void listEnrichesPageAndUsesCriteriaOrderByDefault() {
        Ticket ticket = Ticket.builder().code("TK-1").title("Cần hỗ trợ")
                .studentId(20L).currentAssigneeId(10L).categoryId(30L)
                .priority(TicketPriority.HIGH).status(TicketStatus.ASSIGNED).build();
        ticket.setId(1L);
        ticket.setCreatedAt(LocalDateTime.now().minusHours(8));
        ticket.setLastActivityAt(LocalDateTime.now().minusHours(1));
        when(scopes.getScopeCategoryIds(10L)).thenReturn(List.of(30L));
        when(tickets.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(ticket)));
        when(users.getSummaries(any())).thenReturn(Map.of(
                20L, summary(20L, "Sinh viên A"), 10L, summary(10L, "Tư vấn viên B")));
        when(categories.getDisplayPath(30L)).thenReturn("Đào tạo › Học phí");

        var result = service.list(10L, new QueueFilter(),
                PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdAt")));

        var item = result.getContent().getFirst();
        assertEquals("Sinh viên A", item.studentName());
        assertEquals("Tư vấn viên B", item.assigneeName());
        assertEquals("Đào tạo › Học phí", item.categoryPath());
        assertTrue(item.mine());
        assertTrue(item.ageHours() >= 8);
        ArgumentCaptor<Pageable> pageable = ArgumentCaptor.forClass(Pageable.class);
        verify(tickets).findAll(any(Specification.class), pageable.capture());
        assertFalse(pageable.getValue().getSort().isSorted());
    }

    @Test
    void missingTabFallsBackToUnassigned() {
        assertEquals("unassigned", ManagerQueueService.tab(null));
        assertEquals("unassigned", ManagerQueueService.tab("unknown"));
    }

    private static UserSummary summary(Long id, String name) {
        return new UserSummary(id, name, null, null, null, null, null);
    }
}
