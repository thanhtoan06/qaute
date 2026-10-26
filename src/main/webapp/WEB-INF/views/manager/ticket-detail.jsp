<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Không gian xử lý yêu cầu</title>
<div class="qa-page-manager-ticket-detail" data-qa-view="manager-ticket-detail">
<style>
.qa-page-manager-ticket-detail { --page-accent: #c58219; --page-accent-dark: #96600f; --page-tint: #fff6e6; --page-line: #f4e4c7; min-width: 0; color: #344c67; }
.qa-page-manager-ticket-detail > .page-header, .qa-page-manager-ticket-detail > .qa-page-heading, .qa-page-manager-ticket-detail > .d-flex:has(> .page-title), .qa-page-manager-ticket-detail > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff6e6, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-manager-ticket-detail > .page-header::after, .qa-page-manager-ticket-detail > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f4e4c7; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-manager-ticket-detail .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-manager-ticket-detail .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f4e4c7; border-radius: 12px; color: #96600f; background: linear-gradient(145deg,#fff,#fff6e6); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-manager-ticket-detail .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-manager-ticket-detail .card:hover { border-color: #f4e4c7; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-manager-ticket-detail .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff6e6); }
.qa-page-manager-ticket-detail .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-manager-ticket-detail .card-body { color: #435b75; }
.qa-page-manager-ticket-detail .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#fff6e6); }
.qa-page-manager-ticket-detail .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-manager-ticket-detail .form-control, .qa-page-manager-ticket-detail .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-manager-ticket-detail textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-manager-ticket-detail .form-control:focus, .qa-page-manager-ticket-detail .form-select:focus { border-color: #c58219; box-shadow: 0 0 0 4px #fff6e6; }
.qa-page-manager-ticket-detail .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-manager-ticket-detail .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-manager-ticket-detail .btn-primary { border-color: #c58219; background: linear-gradient(120deg,#c58219,#96600f); box-shadow: 0 5px 13px #f4e4c7; }
.qa-page-manager-ticket-detail .btn-outline-primary { border-color: #f4e4c7; color: #96600f; background: #fff; }
.qa-page-manager-ticket-detail .btn-outline-primary:hover { border-color: #c58219; color: #fff; background: linear-gradient(120deg,#c58219,#96600f); }
.qa-page-manager-ticket-detail .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#fff6e6); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-manager-ticket-detail .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-manager-ticket-detail .table { margin-bottom: 0; color: #405872; }
.qa-page-manager-ticket-detail .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff6e6,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-manager-ticket-detail .table tbody tr { transition: background-color .18s ease; }
.qa-page-manager-ticket-detail .table tbody tr:hover { background: #fff6e6; }
.qa-page-manager-ticket-detail .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-manager-ticket-detail .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-manager-ticket-detail .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-manager-ticket-detail .nav-tabs .nav-link.active { color: #96600f; box-shadow: inset 0 -2px #c58219; }
.qa-page-manager-ticket-detail .nav-pills .nav-link.active { color: #96600f; background: #fff6e6; box-shadow: inset 0 0 0 1px #f4e4c7; }
.qa-page-manager-ticket-detail .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-manager-ticket-detail .progress-bar { border-radius: inherit; }
.qa-page-manager-ticket-detail [class*="timeline"], .qa-page-manager-ticket-detail [class*="activity"], .qa-page-manager-ticket-detail [class*="conversation"] { border-color: var(--page-line); }
@media (max-width: 767.98px) {
  .qa-page-manager-ticket-detail > .page-header, .qa-page-manager-ticket-detail > .qa-page-heading, .qa-page-manager-ticket-detail > .d-flex:has(> .page-title), .qa-page-manager-ticket-detail > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-manager-ticket-detail .card { border-radius: 15px; }
  .qa-page-manager-ticket-detail .card-body { padding: 1rem; }
  .qa-page-manager-ticket-detail .table-responsive { border-radius: 11px; }
  .qa-page-manager-ticket-detail .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-ticket-detail *, .qa-page-manager-ticket-detail *::before, .qa-page-manager-ticket-detail *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.css"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ticket" value="${detail.ticket()}"/>
<c:set var="student" value="${detail.student()}"/>

<%-- Hiển thị yêu cầu, thông tin sinh viên, hội thoại và các thao tác xử lý. --%>
<div class="d-flex justify-content-between align-items-center mb-3 gap-2">
  <div><a class="small text-secondary" href="${ctx}/manager/queue">&larr; Hàng đợi</a><h2 class="page-title mb-0 qa-view-title" data-qa-page="manager-ticket-detail"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-ticket-detailed"></i></span><c:out value="${ticket.title()}"/></h2></div>
  <span class="text-secondary"><c:out value="${ticket.code()}"/></span>
</div>

<div class="row g-3">
  <aside class="col-12 col-lg-3">
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Sinh viên</h3></div><div class="card-body">
      <div class="fw-semibold mb-2"><c:out value="${student.user().fullName()}"/></div>
      <dl class="mb-0">
        <dt>MSSV</dt><dd><c:out value="${student.user().mssv()}"/></dd>
        <dt>Khoa</dt><dd><c:out value="${student.facultyName()}" default="—"/></dd>
        <dt>Lớp</dt><dd><c:out value="${student.classCode()}" default="—"/></dd>
        <dt>Email</dt><dd><a href="mailto:${fn:escapeXml(student.user().email())}"><c:out value="${student.user().email()}"/></a></dd>
        <dt>Điện thoại</dt><dd><c:out value="${student.phone()}" default="—"/></dd>
      </dl>
    </div></div>
    <div class="card"><div class="card-header"><h3 class="card-title">Yêu cầu khác của sinh viên</h3></div><div class="list-group list-group-flush">
      <c:choose><c:when test="${empty detail.otherTickets()}"><div class="list-group-item text-secondary">Không có yêu cầu khác trong phạm vi.</div></c:when>
      <c:otherwise><c:forEach var="other" items="${detail.otherTickets()}">
        <a class="list-group-item list-group-item-action" href="${ctx}/manager/tickets/${other.id()}"><span class="small text-secondary"><c:out value="${other.code()}"/></span><br/><c:out value="${other.title()}"/><br/><span class="small text-secondary"><c:out value="${other.status().label}"/></span></a>
      </c:forEach></c:otherwise></c:choose>
    </div></div>
  </aside>

  <main class="col-12 col-lg-6">
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Nội dung yêu cầu</h3></div><div class="card-body">
      <div class="qa-richtext"><c:out value="${ticket.descriptionHtml()}" escapeXml="false"/></div>
      <c:if test="${not empty detail.descriptionAttachments()}"><ul class="list-unstyled mt-3 mb-0"><c:forEach var="asset" items="${detail.descriptionAttachments()}"><li class="mb-2"><c:choose><c:when test="${fn:startsWith(asset.mimeType(), 'image/')}"><a href="${ctx}${asset.downloadUrl()}" target="_blank" rel="noopener noreferrer"><img src="${ctx}${asset.downloadUrl()}" alt="<c:out value='${asset.name()}'/>" class="img-fluid rounded border mt-2" style="max-width:100%;max-height:320px;object-fit:contain"/></a><br/><a href="${ctx}${asset.downloadUrl()}" target="_blank" rel="noopener noreferrer"><i class="bi bi-download" aria-hidden="true"></i> <c:out value="${asset.name()}"/></a></c:when><c:otherwise><a href="${ctx}${asset.downloadUrl()}"><i class="bi bi-paperclip" aria-hidden="true"></i> <c:out value="${asset.name()}"/></a></c:otherwise></c:choose> <span class="text-secondary small">(<c:out value="${asset.sizeBytes()}"/> byte)</span></li></c:forEach></ul></c:if>
      <c:if test="${ticket.urgentRequested()}"><div class="alert alert-warning mt-3 mb-0"><strong>Sinh viên đề nghị gấp.</strong> <c:out value="${ticket.urgentReason()}"/></div></c:if>
      <c:if test="${not empty ticket.resolutionNote()}"><div class="alert alert-success mt-3 mb-0"><strong>Ghi chú giải quyết:</strong> <c:out value="${ticket.resolutionNote()}"/></div></c:if>
    </div></div>
    <%-- Chuyển giữa hội thoại với sinh viên và ghi chú nội bộ của nhân viên. --%>
    <ul class="nav nav-tabs mb-3" role="tablist"><li class="nav-item" role="presentation"><button class="nav-link active" type="button" data-bs-toggle="tab" data-bs-target="#ticket-conversation" role="tab" aria-controls="ticket-conversation" aria-selected="true">Hội thoại</button></li><li class="nav-item" role="presentation"><button class="nav-link" type="button" data-bs-toggle="tab" data-bs-target="#ticket-internal-notes" role="tab" aria-controls="ticket-internal-notes" aria-selected="false">Ghi chú nội bộ <span class="badge bg-yellow-lt ms-1">${fn:length(detail.internalNotes())}</span></button></li></ul>
    <div class="tab-content mb-3"><div class="tab-pane fade show active" id="ticket-conversation" role="tabpanel">
    <div class="card"><div class="card-header"><h3 class="card-title">Hội thoại với sinh viên</h3></div><div class="card-body">
      <c:choose><c:when test="${empty detail.messages()}"><p class="text-secondary mb-0">Chưa có tin nhắn.</p></c:when>
      <c:otherwise><div class="d-flex flex-column gap-3" data-tv2-pageable><c:forEach var="message" items="${detail.messages()}">
        <c:choose><c:when test="${message.senderType() eq 'SYSTEM'}"><div class="text-center small text-secondary border rounded p-2"><c:out value="${message.contentHtml()}" escapeXml="false"/> <time class="d-block" datetime="${message.createdAt()}" data-qaute-datetime><c:out value="${message.createdAt()}"/></time></div></c:when>
        <c:otherwise><div class="d-flex ${message.senderType() eq 'MANAGER' ? 'justify-content-end' : 'justify-content-start'}"><div class="border rounded p-3 ${message.senderType() eq 'MANAGER' ? 'bg-blue-lt' : 'bg-light'}" style="max-width:90%">
          <div class="small fw-semibold"><c:out value="${message.senderName()}"/> <time class="fw-normal text-secondary" datetime="${message.createdAt()}" data-qaute-datetime><c:out value="${message.createdAt()}"/></time></div>
          <div class="qa-richtext mt-2"><c:out value="${message.contentHtml()}" escapeXml="false"/></div>
          <c:if test="${not empty message.attachments()}"><ul class="list-unstyled mt-2 mb-0"><c:forEach var="asset" items="${message.attachments()}"><li class="mb-2"><c:choose><c:when test="${fn:startsWith(asset.mimeType(), 'image/')}"><a href="${ctx}${asset.downloadUrl()}" target="_blank" rel="noopener noreferrer"><img src="${ctx}${asset.downloadUrl()}" alt="<c:out value='${asset.name()}'/>" class="img-fluid rounded border mt-2" style="max-width:100%;max-height:320px;object-fit:contain"/></a><br/><a href="${ctx}${asset.downloadUrl()}" target="_blank" rel="noopener noreferrer"><i class="bi bi-download" aria-hidden="true"></i> <c:out value="${asset.name()}"/></a></c:when><c:otherwise><a href="${ctx}${asset.downloadUrl()}"><i class="bi bi-paperclip" aria-hidden="true"></i> <c:out value="${asset.name()}"/></a></c:otherwise></c:choose> <span class="text-secondary small">(<c:out value="${asset.sizeBytes()}"/> byte)</span></li></c:forEach></ul></c:if>
        </div></div></c:otherwise></c:choose>
      </c:forEach></div></c:otherwise></c:choose>
    </div></div></div>
    <div class="tab-pane fade" id="ticket-internal-notes" role="tabpanel"><div class="card border-warning"><div class="card-header bg-yellow-lt"><h3 class="card-title">Ghi chú nội bộ</h3></div><div class="card-body">
      <p class="text-secondary small">Chỉ Manager và Admin có quyền xem. Nội dung này không gửi cho sinh viên.</p>
      <%-- Tạo và quản lý ghi chú nội bộ không gửi cho sinh viên. --%>
      <form method="post" action="${ctx}/manager/tickets/${ticket.id()}/notes" class="mb-3"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><label class="form-label required" for="internal-note-content">Thêm ghi chú</label><textarea class="form-control" id="internal-note-content" name="contentHtml" rows="3" maxlength="30000" required></textarea><div class="form-hint">Từ 1 đến 5000 ký tự nội dung.</div><button class="btn btn-warning mt-2" type="submit">Lưu ghi chú</button></form>
      <c:choose><c:when test="${empty detail.internalNotes()}"><p class="text-secondary mb-0">Chưa có ghi chú nội bộ.</p></c:when><c:otherwise><div class="d-flex flex-column gap-2" data-tv2-pageable><c:forEach var="note" items="${detail.internalNotes()}"><div class="border rounded bg-yellow-lt p-3"><div class="d-flex justify-content-between gap-2"><div class="small"><strong><c:out value="${note.authorName()}"/></strong> <time class="text-secondary" datetime="${note.createdAt()}" data-qaute-datetime><c:out value="${note.createdAt()}"/></time><c:if test="${note.pinned()}"><span class="badge bg-yellow ms-2">Đã ghim</span></c:if></div><c:if test="${note.mine()}"><div class="d-flex gap-1"><form method="post" action="${ctx}/manager/notes/${note.id()}/pin"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-warning" type="submit">${note.pinned() ? 'Bỏ ghim' : 'Ghim'}</button></form><form method="post" action="${ctx}/manager/notes/${note.id()}/delete"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-danger" type="submit" onclick="return confirm('Xóa ghi chú này?')">Xóa</button></form></div></c:if></div><div class="mt-2" style="white-space:pre-wrap"><c:out value="${note.contentHtml()}" escapeXml="false"/></div></div></c:forEach></div></c:otherwise></c:choose>
    </div></div></div></div>
    <div id="workspace-actions" data-upload-url="${ctx}/api/media/upload" data-draft-url="${ctx}/api/manager/tickets/${ticket.id()}/draft">
      <%-- Soạn phản hồi, chèn mẫu trả lời và đính kèm tệp cho sinh viên. --%>
      <c:if test="${detail.canReply()}">
        <div class="card mb-3"><div class="card-header"><h3 class="card-title">Trả lời sinh viên</h3></div><div class="card-body">
          <form method="post" action="${ctx}/manager/tickets/${ticket.id()}/reply" class="qa-workspace-form" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="mb-3" id="canned-response-picker" data-list-url="${ctx}/api/manager/canned-responses" data-render-base="${ctx}/api/manager/canned-responses" data-ticket-id="${ticket.id()}"><label class="form-label" for="canned-response-select">Mẫu trả lời nhanh</label><div class="d-flex flex-wrap gap-2"><select class="form-select flex-grow-1" id="canned-response-select" style="min-width:12rem"><option value="">Đang tải mẫu…</option></select><button class="btn btn-outline-primary" id="insert-canned-response" type="button" disabled>Chèn mẫu trả lời</button><a class="btn btn-link" href="${ctx}/manager/canned-responses">Quản lý mẫu</a></div><div id="canned-response-error" class="text-danger small" role="alert"></div></div>
            <div class="mb-3"><label class="form-label required" for="reply-content">Nội dung trả lời</label><textarea class="form-control js-ticket-editor" id="reply-content" name="contentHtml" rows="7" required></textarea><div class="js-editor-error text-danger small" role="alert"></div><div id="draft-status" class="small text-secondary" role="status" aria-live="polite"></div></div>
            <div class="mb-3"><label class="form-label" for="reply-files">Tệp đính kèm</label><input class="form-control js-ticket-files" id="reply-files" type="file" multiple/><div class="form-hint">Tệp được tải lên trước khi gửi phản hồi.</div><div class="js-upload-list small mt-2" aria-live="polite"></div><div class="js-upload-error text-danger small" role="alert"></div><div class="js-asset-ids"></div></div>
            <button class="btn btn-primary" type="submit">Gửi phản hồi</button>
          </form>
        </div></div>
      </c:if>
      <%-- Hiển thị thao tác xử lý theo quyền và trạng thái yêu cầu hiện tại. --%>
      <div class="d-flex flex-wrap gap-2 mb-3">
        <c:if test="${detail.canClaim()}"><form method="post" action="${ctx}/manager/tickets/${ticket.id()}/claim" class="qa-workspace-form"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><input type="hidden" name="redirect" value="/manager/tickets/${ticket.id()}"/><button class="btn btn-primary" type="submit">Nhận yêu cầu</button></form></c:if>
        <c:if test="${detail.canRequestInfo()}"><button class="btn btn-outline-primary" type="button" data-bs-toggle="modal" data-bs-target="#request-info-modal">Yêu cầu bổ sung thông tin</button></c:if>
        <c:if test="${detail.canResolve()}"><button class="btn btn-outline-success" type="button" data-bs-toggle="modal" data-bs-target="#resolve-modal">Đánh dấu đã giải quyết</button></c:if>
        <c:if test="${detail.canChangePriority()}"><button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal" data-bs-target="#priority-modal">Đổi ưu tiên</button></c:if>
        <c:if test="${detail.canChangeCategory()}"><button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal" data-bs-target="#category-modal">Đổi chuyên mục</button></c:if>
        <c:if test="${detail.canEscalate()}"><button class="btn btn-outline-warning" type="button" data-bs-toggle="modal" data-bs-target="#escalate-modal">Chuyển cấp</button></c:if>
        <c:if test="${canCreateFaq}"><form method="post" action="${ctx}/manager/tickets/${ticket.id()}/to-faq"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-outline-primary" type="submit">Tạo FAQ từ yêu cầu này</button></form></c:if>
      </div>
      <c:if test="${detail.canRequestInfo()}"><div class="modal modal-blur fade" id="request-info-modal" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-dialog-centered"><div class="modal-content">
        <div class="modal-header"><h3 class="modal-title">Yêu cầu bổ sung thông tin</h3><button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button></div>
        <form method="post" action="${ctx}/manager/tickets/${ticket.id()}/request-info" class="qa-workspace-form" novalidate><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <div class="modal-body"><label class="form-label required" for="request-info-content">Nội dung gửi sinh viên</label><textarea class="form-control js-ticket-editor" id="request-info-content" name="messageHtml" rows="6" required></textarea><div class="js-editor-error text-danger small" role="alert"></div>
            <div class="mt-3"><label class="form-label" for="request-info-files">Tệp đính kèm</label><input class="form-control js-ticket-files" id="request-info-files" type="file" multiple/><div class="js-upload-list small mt-2" aria-live="polite"></div><div class="js-upload-error text-danger small" role="alert"></div><div class="js-asset-ids"></div></div>
          </div><div class="modal-footer"><button class="btn btn-link" type="button" data-bs-dismiss="modal">Hủy</button><button class="btn btn-primary" type="submit">Gửi yêu cầu</button></div>
        </form>
      </div></div></div></c:if>
      <c:if test="${detail.canEscalate()}"><div class="modal modal-blur fade" id="escalate-modal" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-dialog-centered"><div class="modal-content">
        <div class="modal-header"><h3 class="modal-title">Chuyển cấp yêu cầu</h3><button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button></div>
        <form method="post" action="${ctx}/manager/tickets/${ticket.id()}/escalate" class="qa-workspace-form"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <div class="modal-body">
            <div class="mb-3"><label class="form-label required" for="escalate-category">Chuyên mục đích</label><select class="form-select" id="escalate-category" name="toCategoryId" data-advisors-base="${ctx}/api/manager/categories" required><option value="">Chọn chuyên mục</option><c:forEach var="option" items="${categoryOptions}"><option value="${option.key}" ${ticket.categoryId() eq option.key ? 'disabled' : ''}><c:out value="${option.value}"/></option></c:forEach></select></div>
            <div class="mb-3"><label class="form-label" for="escalate-manager">Tư vấn viên đích (tùy chọn)</label><select class="form-select" id="escalate-manager" name="toManagerId" disabled><option value="">Chọn chuyên mục trước</option></select><div class="form-hint">Để trống nếu muốn chuyển vào hàng đợi của chuyên mục đích.</div><div class="text-danger small" id="escalate-advisors-error" role="alert"></div></div>
            <div><label class="form-label required" for="escalate-reason">Lý do chuyển cấp</label><textarea class="form-control" id="escalate-reason" name="reason" minlength="10" maxlength="500" rows="3" required></textarea></div>
          </div><div class="modal-footer"><button class="btn btn-link" type="button" data-bs-dismiss="modal">Hủy</button><button class="btn btn-warning" type="submit">Chuyển cấp</button></div>
        </form>
      </div></div></div></c:if>
      <c:if test="${detail.canResolve()}"><div class="modal modal-blur fade" id="resolve-modal" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-dialog-centered"><div class="modal-content">
        <div class="modal-header"><h3 class="modal-title">Đánh dấu đã giải quyết</h3><button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button></div>
        <form method="post" action="${ctx}/manager/tickets/${ticket.id()}/resolve" class="qa-workspace-form"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><div class="modal-body"><label class="form-label required" for="resolution-note">Tóm tắt giải pháp</label><textarea class="form-control" id="resolution-note" name="resolutionNote" minlength="10" maxlength="2000" rows="5" required></textarea><small class="form-hint">Từ 10 đến 2000 ký tự.</small></div><div class="modal-footer"><button class="btn btn-link" type="button" data-bs-dismiss="modal">Hủy</button><button class="btn btn-success" type="submit">Xác nhận giải quyết</button></div></form>
      </div></div></div></c:if>
      <c:if test="${detail.canChangePriority()}"><div class="modal modal-blur fade" id="priority-modal" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-dialog-centered"><div class="modal-content">
        <div class="modal-header"><h3 class="modal-title">Đổi ưu tiên</h3><button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button></div>
        <form method="post" action="${ctx}/manager/tickets/${ticket.id()}/priority" class="qa-workspace-form"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><div class="modal-body"><div class="mb-3"><label class="form-label required" for="new-priority">Mức ưu tiên mới</label><select class="form-select" id="new-priority" name="priority" required><option value="">Chọn mức ưu tiên</option><c:forEach var="priority" items="${priorities}"><option value="${priority}" ${ticket.priority() eq priority ? 'disabled' : ''}><c:out value="${priority.label}"/></option></c:forEach></select></div><div><label class="form-label required" for="priority-reason">Lý do</label><textarea class="form-control" id="priority-reason" name="reason" minlength="10" maxlength="500" rows="3" required></textarea></div></div><div class="modal-footer"><button class="btn btn-link" type="button" data-bs-dismiss="modal">Hủy</button><button class="btn btn-primary" type="submit">Lưu ưu tiên</button></div></form>
      </div></div></div></c:if>
      <c:if test="${detail.canChangeCategory()}"><div class="modal modal-blur fade" id="category-modal" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-dialog-centered"><div class="modal-content">
        <div class="modal-header"><h3 class="modal-title">Đổi chuyên mục</h3><button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button></div>
        <form method="post" action="${ctx}/manager/tickets/${ticket.id()}/category" class="qa-workspace-form"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><div class="modal-body"><div class="mb-3"><label class="form-label required" for="new-category">Chuyên mục mới</label><select class="form-select" id="new-category" name="newCategoryId" required><option value="">Chọn chuyên mục</option><c:forEach var="option" items="${categoryOptions}"><option value="${option.key}" ${ticket.categoryId() eq option.key ? 'disabled' : ''}><c:out value="${option.value}"/></option></c:forEach></select></div><div><label class="form-label required" for="category-reason">Lý do</label><textarea class="form-control" id="category-reason" name="reason" minlength="10" maxlength="500" rows="3" required></textarea></div></div><div class="modal-footer"><button class="btn btn-link" type="button" data-bs-dismiss="modal">Hủy</button><button class="btn btn-primary" type="submit">Lưu chuyên mục</button></div></form>
      </div></div></div></c:if>
    </div>
  </main>

  <aside class="col-12 col-lg-3">
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Thông tin yêu cầu</h3></div><div class="card-body">
      <dl class="mb-0"><dt>Mã</dt><dd><c:out value="${ticket.code()}"/></dd>
        <dt>Chuyên mục</dt><dd><c:out value="${ticket.categoryPath()}"/></dd>
        <dt>Ưu tiên</dt><dd><qa:priorityBadge priority="${ticket.priority()}"/></dd>
        <dt>Trạng thái</dt><dd><qa:statusBadge status="${ticket.status()}"/></dd>
        <dt>SLA</dt><dd><qa:slaBadge sla="${ticket.slaStatus()}"/></dd>
        <dt>Hạn phản hồi đầu tiên</dt><dd><c:choose><c:when test="${not empty ticket.slaFirstResponseDueAt()}"><time datetime="${ticket.slaFirstResponseDueAt()}" data-qaute-datetime><c:out value="${ticket.slaFirstResponseDueAt()}"/></time></c:when><c:otherwise>—</c:otherwise></c:choose></dd>
        <dt>Hạn giải quyết</dt><dd><c:choose><c:when test="${not empty ticket.slaResolutionDueAt()}"><time datetime="${ticket.slaResolutionDueAt()}" data-qaute-datetime><c:out value="${ticket.slaResolutionDueAt()}"/></time></c:when><c:otherwise>—</c:otherwise></c:choose></dd>
        <dt>Người phụ trách</dt><dd><c:out value="${ticket.assigneeName()}"/></dd>
        <dt>Tạo lúc</dt><dd><time datetime="${ticket.createdAt()}" data-qaute-datetime><c:out value="${ticket.createdAt()}"/></time></dd>
      </dl>
    </div></div>
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Tag yêu cầu</h3></div><div class="card-body"><div class="d-flex flex-wrap gap-1 mb-3"><c:choose><c:when test="${empty detail.tags()}"><span class="text-secondary small">Chưa có tag.</span></c:when><c:otherwise><c:forEach var="tag" items="${detail.tags()}"><form method="post" action="${ctx}/manager/tickets/${ticket.id()}/tags/${tag.id()}/remove"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="badge bg-blue-lt border-0" type="submit" title="Xóa tag khỏi yêu cầu"><c:out value="${tag.name()}"/> ×</button></form></c:forEach></c:otherwise></c:choose></div><c:if test="${fn:length(detail.tags()) lt 6}"><form method="post" action="${ctx}/manager/tickets/${ticket.id()}/tags"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><label class="form-label" for="ticket-tag-name">Thêm tag</label><div class="input-group"><input class="form-control" id="ticket-tag-name" name="name" type="text" minlength="2" maxlength="40" required/><button class="btn btn-outline-primary" type="submit">Thêm</button></div><div class="form-hint">Tối đa 6 tag, mỗi tag 2–40 ký tự.</div></form></c:if></div></div>
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Dòng thời gian</h3></div><div class="card-body">
      <c:choose><c:when test="${empty detail.timeline()}"><p class="text-secondary mb-0">Chưa có sự kiện.</p></c:when><c:otherwise><ol class="ps-3 mb-0" data-tv2-pageable><c:forEach var="event" items="${detail.timeline()}"><li class="mb-2"><div><c:out value="${event.label()}"/></div><time class="small text-secondary" datetime="${event.at()}" data-qaute-datetime><c:out value="${event.at()}"/></time></li></c:forEach></ol></c:otherwise></c:choose>
    </div></div>
    <div class="card"><div class="card-header"><h3 class="card-title">FAQ gợi ý</h3></div><div class="list-group list-group-flush">
      <c:choose><c:when test="${empty detail.suggestions()}"><div class="list-group-item text-secondary">Chưa có bài viết phù hợp.</div></c:when><c:otherwise><c:forEach var="faq" items="${detail.suggestions()}"><div class="list-group-item"><a href="${ctx}/faq/${fn:escapeXml(faq.slug())}"><c:out value="${faq.title()}"/></a><c:if test="${detail.canReply()}"><button class="btn btn-sm btn-outline-primary mt-2 js-insert-faq" type="button" data-faq-url="${ctx}/faq/${fn:escapeXml(faq.slug())}" data-faq-title="${fn:escapeXml(faq.title())}">Chèn liên kết FAQ</button></c:if></div></c:forEach></c:otherwise></c:choose>
    </div></div>
  </aside>
</div>
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js" defer></script>
<script src="${ctx}/assets/js/manager/ticket-workspace.js" defer></script>
</div>
