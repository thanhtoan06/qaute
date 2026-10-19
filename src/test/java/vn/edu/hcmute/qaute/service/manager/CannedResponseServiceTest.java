package vn.edu.hcmute.qaute.service.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.CannedResponseScope;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.manager.CannedResponseForm;
import vn.edu.hcmute.qaute.dto.view.StudentSummary;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.manager.CannedResponse;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.manager.CannedResponseRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;

@ExtendWith(MockitoExtension.class)
class CannedResponseServiceTest {
    @Mock CannedResponseRepository repository;
    @Mock ManagerScopeService scopes;
    @Mock TicketAccessService ticketAccess;
    @Mock UserLookupService users;
    @Mock CategoryQueryService categories;
    @InjectMocks CannedResponseService service;

    @Test
    void listIncludesOwnPersonalScopedSharedAndAdminGlobalOnly() {
        CannedResponse personal = item(1L, 7L, CannedResponseScope.PERSONAL, null, true);
        CannedResponse scoped = item(2L, 8L, CannedResponseScope.SHARED, 11L, true);
        CannedResponse globalAdmin = item(3L, 9L, CannedResponseScope.SHARED, null, true);
        CannedResponse globalManager = item(4L, 8L, CannedResponseScope.SHARED, null, true);
        when(scopes.getScopeCategoryIds(7L)).thenReturn(List.of(11L));
        when(repository.findByOwnerIdAndScopeOrderByTitleAsc(7L, CannedResponseScope.PERSONAL))
                .thenReturn(List.of(personal));
        when(repository.findByScopeAndCategoryIdInOrderByTitleAsc(
                CannedResponseScope.SHARED, List.of(11L))).thenReturn(List.of(scoped));
        when(repository.findByScopeAndCategoryIdIsNullOrderByTitleAsc(CannedResponseScope.SHARED))
                .thenReturn(List.of(globalAdmin, globalManager));
        when(users.getSummaries(any())).thenReturn(Map.of(
                9L, user(9L, RoleCode.ADMIN, "Admin"),
                8L, user(8L, RoleCode.MANAGER, "Manager")));

        var ids = service.listActive(7L).stream().map(option -> option.id()).toList();

        assertEquals(3, ids.size());
        assertTrue(ids.containsAll(List.of(1L, 2L, 3L)));
        assertFalse(ids.contains(4L));
    }

    @Test
    void sharedTemplateRequiresCategoryInManagerScope() {
        CannedResponseForm form = form(CannedResponseScope.SHARED, 33L);
        when(scopes.getScopeCategoryIds(7L)).thenReturn(List.of(11L));

        assertThrows(ForbiddenException.class, () -> service.save(form, 7L));
        verify(repository, never()).save(any());
    }

    @Test
    void personalSaveSanitizesHtmlAndDropsCategory() {
        CannedResponseForm form = form(CannedResponseScope.PERSONAL, 33L);
        form.setBodyHtml("<p>Xin chào {{student_name}}</p><script>alert(1)</script>");
        when(repository.save(any())).thenAnswer(invocation -> {
            CannedResponse saved = invocation.getArgument(0);
            saved.setId(4L);
            return saved;
        });

        assertEquals(4L, service.save(form, 7L));

        ArgumentCaptor<CannedResponse> captor = ArgumentCaptor.forClass(CannedResponse.class);
        verify(repository).save(captor.capture());
        assertEquals(7L, captor.getValue().getOwnerId());
        assertEquals(null, captor.getValue().getCategoryId());
        assertFalse(captor.getValue().getBodyHtml().contains("<script>"));
        assertTrue(captor.getValue().getBodyHtml().contains("{{student_name}}"));
    }

    @Test
    void renderEscapesSubstitutionsAndIncrementsUsage() {
        Ticket ticket = Ticket.builder().studentId(20L).categoryId(11L).code("TK<&")
                .build();
        CannedResponse template = item(4L, 7L, CannedResponseScope.PERSONAL, null, true);
        template.setBodyHtml("<p>{{student_name}} / {{ticket_code}} / {{advisor_name}} / {{category}}</p>");
        when(ticketAccess.requireViewable(42L, 7L, RoleCode.MANAGER)).thenReturn(ticket);
        when(repository.findById(4L)).thenReturn(Optional.of(template));
        when(users.getStudentSummary(20L)).thenReturn(new StudentSummary(
                user(20L, RoleCode.STUDENT, "A <B>"), null, null, null, null, null));
        when(users.getSummary(7L)).thenReturn(user(7L, RoleCode.MANAGER, "C & D"));
        when(categories.getDisplayPath(11L)).thenReturn("Khoa > Học vụ");

        String html = service.render(4L, 7L, 42L);

        assertTrue(html.contains("A &lt;B&gt;"));
        assertTrue(html.contains("TK&lt;&amp;"));
        assertTrue(html.contains("C &amp; D"));
        assertTrue(html.contains("Khoa &gt; Học vụ"));
        verify(repository).incrementUsageCount(4L);
    }

    @Test
    void inactiveTemplateCannotRender() {
        Ticket ticket = Ticket.builder().studentId(20L).categoryId(11L).build();
        when(ticketAccess.requireViewable(42L, 7L, RoleCode.MANAGER)).thenReturn(ticket);
        when(repository.findById(4L)).thenReturn(Optional.of(
                item(4L, 7L, CannedResponseScope.PERSONAL, null, false)));

        assertThrows(NotFoundException.class, () -> service.render(4L, 7L, 42L));
        verify(repository, never()).incrementUsageCount(any());
    }

    private static CannedResponseForm form(CannedResponseScope scope, Long categoryId) {
        CannedResponseForm form = new CannedResponseForm();
        form.setTitle("Mẫu trả lời");
        form.setBodyHtml("<p>Xin chào</p>");
        form.setScope(scope);
        form.setCategoryId(categoryId);
        return form;
    }

    private static CannedResponse item(Long id, Long ownerId,
            CannedResponseScope scope, Long categoryId, boolean active) {
        CannedResponse item = CannedResponse.builder().ownerId(ownerId).scope(scope)
                .categoryId(categoryId).title("Mẫu " + id).bodyHtml("<p>Xin chào</p>")
                .active(active).build();
        item.setId(id);
        return item;
    }

    private static UserSummary user(Long id, RoleCode role, String name) {
        return new UserSummary(id, name, null, null, null, role, null);
    }
}
