package vn.edu.hcmute.qaute.service.manager;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.HtmlSanitizerUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.view.TicketDraftView;
import vn.edu.hcmute.qaute.dto.view.TicketInternalNoteView;
import vn.edu.hcmute.qaute.dto.view.TicketTagView;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.manager.TicketDraft;
import vn.edu.hcmute.qaute.entity.manager.TicketInternalNote;
import vn.edu.hcmute.qaute.entity.manager.TicketTag;
import vn.edu.hcmute.qaute.entity.manager.TicketTagMap;
import vn.edu.hcmute.qaute.repository.manager.TicketDraftRepository;
import vn.edu.hcmute.qaute.repository.manager.TicketInternalNoteRepository;
import vn.edu.hcmute.qaute.repository.manager.TicketTagMapRepository;
import vn.edu.hcmute.qaute.repository.manager.TicketTagRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;

// Xử lý nghiệp vụ TicketNote trong hệ thống.
@Service
@Transactional
public class TicketNoteService {
    private final TicketAccessService access;
    private final TicketInternalNoteRepository notes;
    private final TicketTagRepository tags;
    private final TicketTagMapRepository tagMap;
    private final TicketDraftRepository drafts;
    private final UserLookupService users;
    private final TicketRepository tickets;

    // Khởi tạo dịch vụ quản lý ghi chú nội bộ, bản nháp và thẻ yêu cầu.
    public TicketNoteService(TicketAccessService access, TicketInternalNoteRepository notes,
            TicketTagRepository tags, TicketTagMapRepository tagMap,
            TicketDraftRepository drafts, UserLookupService users, TicketRepository tickets) {
        this.access = access;
        this.notes = notes;
        this.tags = tags;
        this.tagMap = tagMap;
        this.drafts = drafts;
        this.users = users;
        this.tickets = tickets;
    }

    // Thêm ghi chú nội bộ vào yêu cầu.
    public void addNote(Long ticketId, Long managerId, String html) {
        access.requireViewable(ticketId, managerId, RoleCode.MANAGER);
        String safe = sanitizeNote(html);
        notes.save(TicketInternalNote.builder().ticketId(ticketId).authorId(managerId)
                .contentHtml(safe).pinned(false).build());
    }

    // Đảo trạng thái ghim của ghi chú thuộc tư vấn viên.
    public Long togglePin(Long noteId, Long managerId) {
        TicketInternalNote note = ownedNote(noteId, managerId);
        note.setPinned(!note.isPinned());
        notes.save(note);
        return note.getTicketId();
    }

    // Xóa ghi chú thuộc tư vấn viên.
    public Long deleteNote(Long noteId, Long managerId) {
        TicketInternalNote note = ownedNote(noteId, managerId);
        notes.delete(note);
        return note.getTicketId();
    }

    // Liệt kê ghi chú nội bộ của yêu cầu.
    @Transactional(readOnly = true)
    public List<TicketInternalNoteView> listNotes(Long ticketId, Long userId, RoleCode role) {
        if (role != RoleCode.MANAGER && role != RoleCode.ADMIN) throw new ForbiddenException();
        access.requireViewable(ticketId, userId, role);
        List<TicketInternalNote> entries = notes.findByTicketIdOrderByPinnedDescCreatedAtDesc(ticketId);
        Set<Long> authorIds = entries.stream().map(TicketInternalNote::getAuthorId)
                .filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, UserSummary> authors = authorIds.isEmpty() ? Map.of() : users.getSummaries(authorIds);
        return entries.stream().map(note -> new TicketInternalNoteView(note.getId(),
                authors.containsKey(note.getAuthorId()) ? authors.get(note.getAuthorId()).fullName() : "Tư vấn viên",
                HtmlSanitizerUtil.sanitize(note.getContentHtml()), note.isPinned(),
                userId.equals(note.getAuthorId()), note.getCreatedAt())).toList();
    }

    // Lưu bản nháp phản hồi cho yêu cầu.
    public TicketDraftView saveDraft(Long ticketId, Long managerId, String html) {
        access.requireViewable(ticketId, managerId, RoleCode.MANAGER);
        lockTicket(ticketId);
        String raw = html == null ? "" : html;
        if (raw.length() > 20000) throw new BadRequestException("Bản nháp tối đa 20000 ký tự");
        String safe = HtmlSanitizerUtil.sanitize(raw);
        TicketDraft draft = drafts.findByTicketIdAndManagerId(ticketId, managerId)
                .orElseGet(() -> TicketDraft.builder().ticketId(ticketId).managerId(managerId).build());
        draft.setContentHtml(safe);
        draft = drafts.saveAndFlush(draft);
        return new TicketDraftView(draft.getContentHtml(), draft.getUpdatedAt());
    }

    // Lấy bản nháp phản hồi của tư vấn viên.
    @Transactional(readOnly = true)
    public TicketDraftView getDraft(Long ticketId, Long managerId) {
        access.requireViewable(ticketId, managerId, RoleCode.MANAGER);
        return drafts.findByTicketIdAndManagerId(ticketId, managerId)
                .map(draft -> new TicketDraftView(HtmlSanitizerUtil.sanitize(draft.getContentHtml()),
                        draft.getUpdatedAt())).orElse(new TicketDraftView("", null));
    }

    // Xóa bản nháp phản hồi của tư vấn viên.
    public void deleteDraft(Long ticketId, Long managerId) {
        access.requireViewable(ticketId, managerId, RoleCode.MANAGER);
        drafts.deleteByTicketIdAndManagerId(ticketId, managerId);
    }

    // Thêm thẻ vào yêu cầu nếu chưa vượt giới hạn.
    public void addTag(Long ticketId, Long managerId, String name) {
        access.requireViewable(ticketId, managerId, RoleCode.MANAGER);
        lockTicket(ticketId);
        String clean = name == null ? "" : name.trim().replaceAll("\\s+", " ");
        if (clean.length() < 2 || clean.length() > 40) {
            throw new BadRequestException("Tên tag phải từ 2 đến 40 ký tự");
        }
        String canonical = clean.toLowerCase(Locale.ROOT);
        TicketTag tag = tags.findByNameIgnoreCase(canonical).orElse(null);
        if (tag != null && tagMap.existsByTicketIdAndTagId(ticketId, tag.getId())) return;
        if (tagMap.countByTicketId(ticketId) >= 6) throw new BadRequestException("Mỗi yêu cầu chỉ có tối đa 6 tag");
        try {
            if (tag == null) tag = tags.saveAndFlush(TicketTag.builder().name(canonical).build());
            tagMap.saveAndFlush(TicketTagMap.builder().ticketId(ticketId).tagId(tag.getId()).build());
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateException("name", "Tag đã được thêm; vui lòng tải lại trang");
        }
    }

    // Gỡ thẻ khỏi yêu cầu.
    public void removeTag(Long ticketId, Long managerId, Long tagId) {
        access.requireViewable(ticketId, managerId, RoleCode.MANAGER);
        tagMap.deleteByTicketIdAndTagId(ticketId, tagId);
    }

    // Liệt kê các thẻ gắn với yêu cầu.
    @Transactional(readOnly = true)
    public List<TicketTagView> listTags(Long ticketId, Long userId, RoleCode role) {
        if (role != RoleCode.MANAGER && role != RoleCode.ADMIN) throw new ForbiddenException();
        access.requireViewable(ticketId, userId, role);
        Set<Long> ids = tagMap.findByTicketId(ticketId).stream().map(TicketTagMap::getTagId)
                .collect(Collectors.toSet());
        if (ids.isEmpty()) return List.of();
        return tags.findAllById(ids).stream().map(tag -> new TicketTagView(tag.getId(), tag.getName()))
                .sorted(java.util.Comparator.comparing(TicketTagView::name)).toList();
    }

    // Lấy ghi chú và xác nhận tư vấn viên là tác giả.
    private TicketInternalNote ownedNote(Long noteId, Long managerId) {
        TicketInternalNote note = notes.findById(noteId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ghi chú"));
        access.requireViewable(note.getTicketId(), managerId, RoleCode.MANAGER);
        if (!managerId.equals(note.getAuthorId())) throw new ForbiddenException("Chỉ tác giả được sửa ghi chú");
        return note;
    }

    // Khóa yêu cầu trước khi cập nhật dữ liệu liên quan.
    private void lockTicket(Long ticketId) {
        tickets.lockById(ticketId).orElseThrow(() -> new NotFoundException("Không tìm thấy ticket"));
    }

    // Làm sạch HTML và kiểm tra độ dài ghi chú.
    private static String sanitizeNote(String html) {
        if (html == null || html.length() > 30000) throw new BadRequestException("Ghi chú không hợp lệ");
        String safe = HtmlSanitizerUtil.sanitize(html);
        int length = TextUtil.stripHtml(safe).length();
        if (length < 1 || length > 5000) throw new BadRequestException("Ghi chú phải từ 1 đến 5000 ký tự");
        return safe;
    }
}
