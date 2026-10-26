<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Nhật ký hệ thống</title>
<div class="qa-page-admin-audit-logs" data-qa-view="admin-audit-logs">
<style>
.qa-page-admin-audit-logs { --page-accent: #c44c65; --page-accent-dark: #96364a; --page-tint: #fff0f3; --page-line: #f5d8df; min-width: 0; color: #344c67; }
.qa-page-admin-audit-logs > .page-header, .qa-page-admin-audit-logs > .qa-page-heading, .qa-page-admin-audit-logs > .d-flex:has(> .page-title), .qa-page-admin-audit-logs > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff0f3, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-audit-logs > .page-header::after, .qa-page-admin-audit-logs > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f5d8df; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-audit-logs .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-audit-logs .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f5d8df; border-radius: 12px; color: #96364a; background: linear-gradient(145deg,#fff,#fff0f3); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-audit-logs .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-audit-logs .card:hover { border-color: #f5d8df; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-audit-logs .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff0f3); }
.qa-page-admin-audit-logs .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-audit-logs .card-body { color: #435b75; }
.qa-page-admin-audit-logs .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#fff0f3); }
.qa-page-admin-audit-logs .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-audit-logs .form-control, .qa-page-admin-audit-logs .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-audit-logs textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-audit-logs .form-control:focus, .qa-page-admin-audit-logs .form-select:focus { border-color: #c44c65; box-shadow: 0 0 0 4px #fff0f3; }
.qa-page-admin-audit-logs .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-audit-logs .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-audit-logs .btn-primary { border-color: #c44c65; background: linear-gradient(120deg,#c44c65,#96364a); box-shadow: 0 5px 13px #f5d8df; }
.qa-page-admin-audit-logs .btn-outline-primary { border-color: #f5d8df; color: #96364a; background: #fff; }
.qa-page-admin-audit-logs .btn-outline-primary:hover { border-color: #c44c65; color: #fff; background: linear-gradient(120deg,#c44c65,#96364a); }
.qa-page-admin-audit-logs .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#fff0f3); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-audit-logs .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-audit-logs .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-audit-logs .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff0f3,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-audit-logs .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-audit-logs .table tbody tr:hover { background: #fff0f3; }
.qa-page-admin-audit-logs .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-audit-logs .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-audit-logs .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-audit-logs .nav-tabs .nav-link.active { color: #96364a; box-shadow: inset 0 -2px #c44c65; }
.qa-page-admin-audit-logs .nav-pills .nav-link.active { color: #96364a; background: #fff0f3; box-shadow: inset 0 0 0 1px #f5d8df; }
.qa-page-admin-audit-logs .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-audit-logs .progress-bar { border-radius: inherit; }
.qa-page-admin-audit-logs .card-header { background-image: linear-gradient(90deg,var(--page-accent),transparent 72%); background-repeat: no-repeat; background-size: 100% 2px; background-position: top; }
@media (max-width: 767.98px) {
  .qa-page-admin-audit-logs > .page-header, .qa-page-admin-audit-logs > .qa-page-heading, .qa-page-admin-audit-logs > .d-flex:has(> .page-title), .qa-page-admin-audit-logs > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-audit-logs .card { border-radius: 15px; }
  .qa-page-admin-audit-logs .card-body { padding: 1rem; }
  .qa-page-admin-audit-logs .table-responsive { border-radius: 11px; }
  .qa-page-admin-audit-logs .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-audit-logs *, .qa-page-admin-audit-logs *::before, .qa-page-admin-audit-logs *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang -->
<h2 class="page-title mb-3 qa-view-title" data-qa-page="admin-audit-logs"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-journal-text"></i></span>Nhật ký hệ thống</h2>

<%-- Lọc nhật ký theo người dùng, hành động, đối tượng và khoảng ngày; hỗ trợ xuất CSV. --%>
<!-- Form lọc nhật ký: người thực hiện, hành động, đối tượng, khoảng thời gian -->
<form method="get" action="${ctx}/admin/audit-logs" class="card card-body mb-3">
  <div class="row g-3">
    <div class="col-12 col-md-3"><label class="form-label" for="audit-actor">Tên hoặc email người thực hiện</label><input class="form-control" id="audit-actor" name="actorQ" value="<c:out value='${filter.actorQ}'/>"/></div>
    <div class="col-6 col-md-2"><label class="form-label" for="audit-actor-id">ID người thực hiện</label><input class="form-control" id="audit-actor-id" name="actorId" type="number" min="1" value="<c:out value='${filter.actorId}'/>"/></div>
    <div class="col-6 col-md-3"><label class="form-label" for="audit-action">Hành động</label><select class="form-select" id="audit-action" name="action"><option value="">Tất cả</option><c:forEach var="action" items="${actions}"><option value="<c:out value='${action.code()}'/>" ${filter.action eq action.code() ? 'selected' : ''}><c:out value="${action.label()}"/> (<c:out value="${action.code()}"/>)</option></c:forEach></select></div>
    <div class="col-6 col-md-2"><label class="form-label" for="audit-type">Loại đối tượng</label><input class="form-control" id="audit-type" name="entityType" value="<c:out value='${filter.entityType}'/>"/></div>
    <div class="col-6 col-md-2"><label class="form-label" for="audit-entity-id">ID đối tượng</label><input class="form-control" id="audit-entity-id" name="entityId" type="number" min="1" value="<c:out value='${filter.entityId}'/>"/></div>
    <div class="col-6 col-md-2"><label class="form-label" for="audit-from">Từ ngày</label><input class="form-control" id="audit-from" name="from" type="date" value="<c:out value='${filter.from}'/>"/></div>
    <div class="col-6 col-md-2"><label class="form-label" for="audit-to">Đến ngày</label><input class="form-control" id="audit-to" name="to" type="date" value="<c:out value='${filter.to}'/>"/></div>
    <div class="col-12 d-flex flex-wrap gap-2 align-items-end">
      <button class="btn btn-primary" type="submit">Lọc</button>
      <a class="btn btn-outline-secondary" href="${ctx}/admin/audit-logs">Bỏ lọc</a>
      <button class="btn btn-outline-primary ms-auto" type="submit" formaction="${ctx}/admin/audit-logs/export">Xuất CSV (tối đa 5.000 dòng)</button>
    </div>
  </div>
</form>

<%-- Liệt kê nhật ký và mở hộp thoại xem thông tin chi tiết từng sự kiện. --%>
<!-- Bảng danh sách nhật ký hệ thống -->
<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>Thời gian</th><th>Người thực hiện</th><th>Hành động</th><th>Đối tượng</th><th>IP</th><th></th></tr></thead>
  <tbody><c:choose><c:when test="${empty logs.content}"><tr><td colspan="6" class="text-center text-secondary py-5">Không có dòng nhật ký phù hợp.</td></tr></c:when>
  <c:otherwise><c:forEach var="log" items="${logs.content}"><tr>
    <td><c:out value="${log.createdAt()}"/></td>
    <td><div class="fw-semibold"><c:out value="${log.actorName()}"/></div><small class="text-secondary"><c:out value="${log.actorRole()}"/></small></td>
    <td><div><c:out value="${log.actionLabel()}"/></div><small class="text-secondary"><c:out value="${log.action()}"/></small></td>
    <td><c:out value="${log.entityType()}"/> <c:if test="${not empty log.entityId()}">#<c:out value="${log.entityId()}"/></c:if></td>
    <td><c:out value="${log.ip()}"/></td>
    <!-- Nút xem chi tiết trong modal -->
    <td><button class="btn btn-sm btn-outline-primary js-audit-detail" type="button" data-bs-toggle="modal" data-bs-target="#audit-detail-modal" data-url="${ctx}/admin/audit-logs/${log.id()}">Chi tiết</button></td>
  </tr></c:forEach></c:otherwise></c:choose></tbody>
</table></div></div>
<qa:pagination page="${logs}" baseUrl="${ctx}/admin/audit-logs" params="${paginationParams}"/>

<!-- Modal hiển thị chi tiết nhật ký: thông tin cơ bản và dữ liệu trước/sau -->
<div class="modal modal-blur fade" id="audit-detail-modal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered"><div class="modal-content">
    <div class="modal-header"><h3 class="modal-title">Chi tiết nhật ký</h3><button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button></div>
    <div class="modal-body">
      <div id="audit-detail-error" class="alert alert-danger d-none" role="alert"></div>
      <dl class="row mb-3">
        <dt class="col-sm-3">Thời gian</dt><dd class="col-sm-9" id="audit-detail-at"></dd>
        <dt class="col-sm-3">Người thực hiện</dt><dd class="col-sm-9" id="audit-detail-actor"></dd>
        <dt class="col-sm-3">Hành động</dt><dd class="col-sm-9" id="audit-detail-action"></dd>
        <dt class="col-sm-3">Đối tượng</dt><dd class="col-sm-9" id="audit-detail-entity"></dd>
        <dt class="col-sm-3">IP / User-Agent</dt><dd class="col-sm-9" id="audit-detail-client"></dd>
        <dt class="col-sm-3">Trace ID</dt><dd class="col-sm-9" id="audit-detail-trace"></dd>
      </dl>
      <!-- So sánh dữ liệu trước và sau thay đổi -->
      <div class="row g-3">
        <div class="col-md-6"><h4>Trước</h4><pre class="border rounded bg-light p-3 text-wrap mb-0" id="audit-detail-before"></pre></div>
        <div class="col-md-6"><h4>Sau</h4><pre class="border rounded bg-light p-3 text-wrap mb-0" id="audit-detail-after"></pre></div>
      </div>
    </div>
  </div></div>
</div>
<script src="${ctx}/assets/js/admin/audit-logs.js" defer></script>
</div>
