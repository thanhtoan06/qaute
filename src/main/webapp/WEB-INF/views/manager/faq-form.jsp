<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title><c:out value="${pageTitle}"/></title>
<div class="qa-page-manager-faq-form" data-qa-view="manager-faq-form">
<style>
.qa-page-manager-faq-form { --page-accent: #7456bd; --page-accent-dark: #584091; --page-tint: #f3efff; --page-line: #e1d7fb; min-width: 0; color: #344c67; }
.qa-page-manager-faq-form > .page-header, .qa-page-manager-faq-form > .qa-page-heading, .qa-page-manager-faq-form > .d-flex:has(> .page-title), .qa-page-manager-faq-form > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #f3efff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-manager-faq-form > .page-header::after, .qa-page-manager-faq-form > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #e1d7fb; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-manager-faq-form .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-manager-faq-form .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #e1d7fb; border-radius: 12px; color: #584091; background: linear-gradient(145deg,#fff,#f3efff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-manager-faq-form .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-manager-faq-form .card:hover { border-color: #e1d7fb; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-manager-faq-form .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#f3efff); }
.qa-page-manager-faq-form .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-manager-faq-form .card-body { color: #435b75; }
.qa-page-manager-faq-form .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#f3efff); }
.qa-page-manager-faq-form .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-manager-faq-form .form-control, .qa-page-manager-faq-form .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-manager-faq-form textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-manager-faq-form .form-control:focus, .qa-page-manager-faq-form .form-select:focus { border-color: #7456bd; box-shadow: 0 0 0 4px #f3efff; }
.qa-page-manager-faq-form .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-manager-faq-form .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-manager-faq-form .btn-primary { border-color: #7456bd; background: linear-gradient(120deg,#7456bd,#584091); box-shadow: 0 5px 13px #e1d7fb; }
.qa-page-manager-faq-form .btn-outline-primary { border-color: #e1d7fb; color: #584091; background: #fff; }
.qa-page-manager-faq-form .btn-outline-primary:hover { border-color: #7456bd; color: #fff; background: linear-gradient(120deg,#7456bd,#584091); }
.qa-page-manager-faq-form .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#f3efff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-manager-faq-form .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-manager-faq-form .table { margin-bottom: 0; color: #405872; }
.qa-page-manager-faq-form .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#f3efff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-manager-faq-form .table tbody tr { transition: background-color .18s ease; }
.qa-page-manager-faq-form .table tbody tr:hover { background: #f3efff; }
.qa-page-manager-faq-form .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-manager-faq-form .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-manager-faq-form .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-manager-faq-form .nav-tabs .nav-link.active { color: #584091; box-shadow: inset 0 -2px #7456bd; }
.qa-page-manager-faq-form .nav-pills .nav-link.active { color: #584091; background: #f3efff; box-shadow: inset 0 0 0 1px #e1d7fb; }
.qa-page-manager-faq-form .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-manager-faq-form .progress-bar { border-radius: inherit; }
.qa-page-manager-faq-form form:not(.qa-filter-bar) { --form-accent: var(--page-accent); }
.qa-page-manager-faq-form form:not(.qa-filter-bar) > .card, .qa-page-manager-faq-form form:not(.qa-filter-bar) .card { border-radius: 20px; }
.qa-page-manager-faq-form form:not(.qa-filter-bar) .form-control:focus, .qa-page-manager-faq-form form:not(.qa-filter-bar) .form-select:focus { border-color: var(--page-accent); box-shadow: 0 0 0 4px var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-manager-faq-form > .page-header, .qa-page-manager-faq-form > .qa-page-heading, .qa-page-manager-faq-form > .d-flex:has(> .page-title), .qa-page-manager-faq-form > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-manager-faq-form .card { border-radius: 15px; }
  .qa-page-manager-faq-form .card-body { padding: 1rem; }
  .qa-page-manager-faq-form .table-responsive { border-radius: 11px; }
  .qa-page-manager-faq-form .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-faq-form *, .qa-page-manager-faq-form *::before, .qa-page-manager-faq-form *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.css"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="manager-faq-form"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span><c:out value="${pageTitle}"/></h2>
  <a class="btn btn-outline-secondary" href="${ctx}/manager/faq">FAQ của tôi</a>
</div>

<%-- Soạn FAQ, lưu bản nháp hoặc gửi nội dung hoàn thiện để duyệt. --%>
<div class="row"><div class="col-xl-10"><div class="card"><div class="card-body">
  <c:if test="${not empty errors.global}"><div class="alert alert-danger" role="alert"><c:out value="${errors.global}"/></div></c:if>
  <c:if test="${not empty sourceTicketId}"><div class="alert alert-warning" role="note">Bản nháp được tạo từ ticket #<c:out value="${sourceTicketId}"/>. Hãy đọc lại toàn bộ nội dung và xóa mọi thông tin cá nhân trước khi gửi duyệt.</div></c:if>
  <form method="post" action="${ctx}/manager/faq" id="faq-editor-form" novalidate data-upload-url="${ctx}/api/media/upload">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="id" value="<c:out value='${form.id}'/>"/>
    <input type="hidden" name="version" value="<c:out value='${form.version}'/>"/>
    <%@ include file="/WEB-INF/views/common/faq-editor-fields.jspf" %>
    <button class="btn btn-primary" type="submit">${editing ? 'Lưu thay đổi' : 'Lưu bản nháp'}</button>
  </form>

  <c:if test="${editing}"><hr/><form method="post" action="${ctx}/manager/faq/${form.id}/submit">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <c:if test="${not empty sourceTicketId}"><label class="form-check mb-3"><input class="form-check-input" type="checkbox" name="confirmedNoPersonalInfo" value="true" required/><span class="form-check-label">Tôi đã loại bỏ thông tin cá nhân khỏi FAQ</span></label></c:if>
    <button class="btn btn-success" type="submit">Gửi duyệt</button>
  </form></c:if>
</div></div></div></div>

<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js" defer></script>
<script src="${ctx}/assets/js/admin/faq-editor.js" defer></script>
</div>
