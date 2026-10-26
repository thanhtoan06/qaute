package vn.edu.hcmute.qaute.service.manager;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.HtmlSanitizerUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.request.admin.AdminFaqFilter;
import vn.edu.hcmute.qaute.dto.request.admin.FaqForm;
import vn.edu.hcmute.qaute.dto.view.AdminFaqListItem;
import vn.edu.hcmute.qaute.dto.view.StudentSummary;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.entity.ticket.TicketMessage;
import vn.edu.hcmute.qaute.repository.knowledge.FaqRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketMessageRepository;
import vn.edu.hcmute.qaute.service.admin.AdminFaqService;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;

// Xử lý nghiệp vụ ManagerFaq trong hệ thống.
@Service
@Transactional(readOnly = true)
public class ManagerFaqService {
    private static final Pattern EMAIL = Pattern.compile("(?i)[\\w.%+-]+@[\\w.-]+\\.[a-z]{2,}");
    private static final Pattern MSSV = Pattern.compile("(?<!\\d)\\d{8}(?!\\d)");
    private static final Pattern PHONE = Pattern.compile("(?<!\\d)(?:\\+?84|0)[ .-]?(?:\\d[ .-]?){8,10}(?!\\d)");

    private final AdminFaqService adminFaqs;
    private final FaqRepository faqs;
    private final ManagerScopeService scopes;
    private final TicketAccessService ticketAccess;
    private final TicketMessageRepository messages;
    private final UserLookupService users;
    private final AuditService audit;

    // Khởi tạo dịch vụ FAQ dành cho tư vấn viên.
    public ManagerFaqService(AdminFaqService adminFaqs, FaqRepository faqs,
            ManagerScopeService scopes, TicketAccessService ticketAccess,
            TicketMessageRepository messages, UserLookupService users, AuditService audit) {
        this.adminFaqs = adminFaqs;
        this.faqs = faqs;
        this.scopes = scopes;
        this.ticketAccess = ticketAccess;
        this.messages = messages;
        this.users = users;
        this.audit = audit;
    }

    // Liệt kê FAQ do tư vấn viên tạo theo trạng thái.
    public Page<AdminFaqListItem> list(Long managerId, FaqStatus status, Pageable pageable) {
        AdminFaqFilter filter = new AdminFaqFilter();
        filter.setAuthorId(managerId);
        filter.setStatus(status);
        return adminFaqs.search(filter, pageable);
    }

    // Lấy biểu mẫu của bản nháp thuộc tư vấn viên.
    public FaqForm getForm(Long id, Long managerId) {
        requireOwnDraft(id, managerId);
        return adminFaqs.getForm(id);
    }

    // Lấy mã yêu cầu nguồn của bản nháp FAQ.
    public Long sourceTicketId(Long id, Long managerId) {
        return requireOwnDraft(id, managerId).getSourceTicketId();
    }

    // Lưu FAQ trong phạm vi chuyên mục được giao.
    @Transactional
    public Long save(FaqForm form, Long managerId) {
        if (form == null || form.getCategoryId() == null
                || !scopes.isInScope(managerId, form.getCategoryId())) {
            throw new BadRequestException("Chuyên mục không thuộc phạm vi của bạn");
        }
        if (form.getId() != null) requireOwnDraft(form.getId(), managerId);
        form.setFeatured(false);
        form.setPublishAt(null);
        return adminFaqs.save(form, managerId);
    }

    // Gửi bản nháp FAQ để quản trị viên duyệt.
    @Transactional
    public void submit(Long id, Long managerId, boolean confirmedNoPersonalInfo) {
        requireOwnDraft(id, managerId);
        adminFaqs.submitForReview(id, managerId, confirmedNoPersonalInfo);
    }

    // Tạo FAQ từ yêu cầu đã giải quyết và cho phép công khai.
    @Transactional
    public Long createFromTicket(Long ticketId, Long managerId) {
        Ticket ticket = ticketAccess.requireViewable(ticketId, managerId, RoleCode.MANAGER);
        if ((ticket.getStatus() != TicketStatus.RESOLVED && ticket.getStatus() != TicketStatus.CLOSED)
                || !ticket.isAllowPublicFaq()) {
            throw new BadRequestException("Yêu cầu chưa đủ điều kiện để tạo FAQ");
        }
        if (!scopes.isInScope(managerId, ticket.getCategoryId())) {
            throw new BadRequestException("Chuyên mục không thuộc phạm vi của bạn");
        }
        StudentSummary student = users.getStudentSummary(ticket.getStudentId());
        List<TicketMessage> history = messages.findByTicketIdOrderByCreatedAtAsc(ticketId);
        Optional<TicketMessage> lastReply = history.stream()
                .filter(message -> message.getSenderType() == SenderType.MANAGER)
                .reduce((previous, current) -> current);
        if (lastReply.isEmpty()) {
            throw new BadRequestException("Yêu cầu chưa có phản hồi của tư vấn viên để tạo FAQ");
        }
        String question = redact(ticket.getDescriptionHtml(), student);
        String answer = redact(lastReply.get().getContentHtml(), student);
        FaqForm form = new FaqForm();
        form.setTitle(redact(ticket.getTitle(), student));
        form.setCategoryId(ticket.getCategoryId());
        form.setBodyHtml("<h3>Câu hỏi</h3><p>" + HtmlUtils.htmlEscape(question)
                + "</p><h3>Trả lời</h3><p>" + HtmlUtils.htmlEscape(answer) + "</p>");
        Long faqId = save(form, managerId);
        Faq faq = faqs.findById(faqId).orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));
        faq.setSourceTicketId(ticketId);
        faqs.saveAndFlush(faq);
        audit.log(AuditActions.FAQ_FROM_TICKET, "Faq", faqId, null,
                "ticketId=" + ticketId);
        return faqId;
    }

    // Kiểm tra FAQ là bản nháp thuộc tư vấn viên.
    private Faq requireOwnDraft(Long id, Long managerId) {
        Faq faq = faqs.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));
        if (!managerId.equals(faq.getAuthorId())) {
            throw new NotFoundException("Không tìm thấy FAQ");
        }
        if (faq.getStatus() != FaqStatus.DRAFT) {
            throw new BadRequestException("Chỉ có thể sửa hoặc gửi duyệt bản nháp của mình");
        }
        return faq;
    }

    // Loại bỏ thông tin cá nhân khỏi nội dung HTML.
    private static String redact(String html, StudentSummary student) {
        String result = HtmlUtils.htmlUnescape(TextUtil.stripHtml(HtmlSanitizerUtil.sanitize(html)));
        if (student != null && student.user() != null) {
            result = replaceLiteral(result, student.user().fullName());
            result = replaceLiteral(result, student.user().mssv());
            result = replaceLiteral(result, student.user().email());
            result = replaceLiteral(result, student.phone());
        }
        result = EMAIL.matcher(result).replaceAll("[đã ẩn]");
        result = MSSV.matcher(result).replaceAll("[đã ẩn]");
        return PHONE.matcher(result).replaceAll("[đã ẩn]");
    }

    // Thay chuỗi cá nhân bằng nội dung ẩn danh.
    private static String replaceLiteral(String value, String secret) {
        if (secret == null || secret.isBlank()) return value;
        return Pattern.compile(Pattern.quote(secret.trim()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
                .matcher(value).replaceAll(Matcher.quoteReplacement("[đã ẩn]"));
    }
}
