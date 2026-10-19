package vn.edu.hcmute.qaute.service.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.entity.manager.TicketDraft;
import vn.edu.hcmute.qaute.entity.manager.TicketInternalNote;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.manager.TicketDraftRepository;
import vn.edu.hcmute.qaute.repository.manager.TicketInternalNoteRepository;
import vn.edu.hcmute.qaute.repository.manager.TicketTagMapRepository;
import vn.edu.hcmute.qaute.repository.manager.TicketTagRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;

@ExtendWith(MockitoExtension.class)
class TicketNoteServiceTest {
    @Mock TicketAccessService access;
    @Mock TicketInternalNoteRepository notes;
    @Mock TicketTagRepository tags;
    @Mock TicketTagMapRepository tagMap;
    @Mock TicketDraftRepository drafts;
    @Mock UserLookupService users;
    @Mock TicketRepository tickets;
    @InjectMocks TicketNoteService service;

    @Test
    void addNoteSanitizesHtmlAndRequiresTicketAccess() {
        service.addNote(3L, 7L, "<p>Nội bộ</p><script>alert(1)</script>");

        verify(access).requireViewable(3L, 7L, RoleCode.MANAGER);
        ArgumentCaptor<TicketInternalNote> saved = ArgumentCaptor.forClass(TicketInternalNote.class);
        verify(notes).save(saved.capture());
        assertEquals(7L, saved.getValue().getAuthorId());
        assertTrue(!saved.getValue().getContentHtml().contains("<script>"));
    }

    @Test
    void pinIsLimitedToAuthor() {
        TicketInternalNote note = TicketInternalNote.builder().ticketId(3L).authorId(8L)
                .contentHtml("Ghi chú").build();
        when(notes.findById(5L)).thenReturn(Optional.of(note));

        assertThrows(ForbiddenException.class, () -> service.togglePin(5L, 7L));
        verify(notes, never()).save(any());
    }

    @Test
    void studentCannotReadInternalNotes() {
        assertThrows(ForbiddenException.class,
                () -> service.listNotes(3L, 9L, RoleCode.STUDENT));
        verify(notes, never()).findByTicketIdOrderByPinnedDescCreatedAtDesc(any());
    }

    @Test
    void tagLimitIsCheckedWhileTicketIsLocked() {
        when(tickets.lockById(3L)).thenReturn(Optional.of(new Ticket()));
        when(tagMap.countByTicketId(3L)).thenReturn(6L);

        assertThrows(BadRequestException.class, () -> service.addTag(3L, 7L, "học vụ"));
        verify(tickets).lockById(3L);
        verify(tagMap, never()).saveAndFlush(any());
    }

    @Test
    void savingDraftUpdatesOnlyManagersOwnDraft() {
        TicketDraft draft = TicketDraft.builder().ticketId(3L).managerId(7L)
                .contentHtml("Cũ").build();
        when(tickets.lockById(3L)).thenReturn(Optional.of(new Ticket()));
        when(drafts.findByTicketIdAndManagerId(3L, 7L)).thenReturn(Optional.of(draft));
        when(drafts.saveAndFlush(draft)).thenReturn(draft);

        var result = service.saveDraft(3L, 7L, "<b>Nháp</b><script>x</script>");

        assertEquals("<b>Nháp</b>", result.contentHtml());
        verify(access).requireViewable(3L, 7L, RoleCode.MANAGER);
    }
}
