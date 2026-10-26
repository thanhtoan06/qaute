<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Kiểm tra hệ thống</title>
<div class="qa-page-admin-health" data-qa-view="admin-health">
<style>
.qa-page-admin-health { --page-accent: #168b78; --page-accent-dark: #116a5c; --page-tint: #eaf8f4; --page-line: #d2eee6; min-width: 0; color: #344c67; }
.qa-page-admin-health > .page-header, .qa-page-admin-health > .qa-page-heading, .qa-page-admin-health > .d-flex:has(> .page-title), .qa-page-admin-health > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #eaf8f4, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-health > .page-header::after, .qa-page-admin-health > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #d2eee6; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-health .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-health .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #d2eee6; border-radius: 12px; color: #116a5c; background: linear-gradient(145deg,#fff,#eaf8f4); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-health .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-health .card:hover { border-color: #d2eee6; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-health .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#eaf8f4); }
.qa-page-admin-health .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-health .card-body { color: #435b75; }
.qa-page-admin-health .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#eaf8f4); }
.qa-page-admin-health .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-health .form-control, .qa-page-admin-health .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-health textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-health .form-control:focus, .qa-page-admin-health .form-select:focus { border-color: #168b78; box-shadow: 0 0 0 4px #eaf8f4; }
.qa-page-admin-health .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-health .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-health .btn-primary { border-color: #168b78; background: linear-gradient(120deg,#168b78,#116a5c); box-shadow: 0 5px 13px #d2eee6; }
.qa-page-admin-health .btn-outline-primary { border-color: #d2eee6; color: #116a5c; background: #fff; }
.qa-page-admin-health .btn-outline-primary:hover { border-color: #168b78; color: #fff; background: linear-gradient(120deg,#168b78,#116a5c); }
.qa-page-admin-health .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#eaf8f4); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-health .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-health .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-health .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#eaf8f4,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-health .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-health .table tbody tr:hover { background: #eaf8f4; }
.qa-page-admin-health .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-health .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-health .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-health .nav-tabs .nav-link.active { color: #116a5c; box-shadow: inset 0 -2px #168b78; }
.qa-page-admin-health .nav-pills .nav-link.active { color: #116a5c; background: #eaf8f4; box-shadow: inset 0 0 0 1px #d2eee6; }
.qa-page-admin-health .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-health .progress-bar { border-radius: inherit; }
.qa-page-admin-health .card-header { background-image: linear-gradient(90deg,var(--page-accent),transparent 72%); background-repeat: no-repeat; background-size: 100% 2px; background-position: top; }
@media (max-width: 767.98px) {
  .qa-page-admin-health > .page-header, .qa-page-admin-health > .qa-page-heading, .qa-page-admin-health > .d-flex:has(> .page-title), .qa-page-admin-health > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-health .card { border-radius: 15px; }
  .qa-page-admin-health .card-body { padding: 1rem; }
  .qa-page-admin-health .table-responsive { border-radius: 11px; }
  .qa-page-admin-health .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-health *, .qa-page-admin-health *::before, .qa-page-admin-health *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và nút làm mới -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <div>
    <h2 class="page-title mb-1 qa-view-title" data-qa-page="admin-health"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-heart-pulse"></i></span>Kiểm tra hệ thống</h2>
    <div class="text-secondary">Trạng thái được đo khi tải trang này.</div>
  </div>
  <a class="btn btn-outline-primary" href="${ctx}/admin/health">Làm mới</a>
</div>

<!-- Thông báo thành công/lỗi -->
<c:if test="${not empty flashSuccess}"><div class="alert alert-success"><c:out value="${flashSuccess}"/></div></c:if>
<c:if test="${not empty flashError}"><div class="alert alert-danger"><c:out value="${flashError}"/></div></c:if>

<%-- Hiển thị trạng thái các thành phần và kết quả kiểm tra email. --%>
<!-- Grid các kiểm tra sức khỏe hệ thống -->
<div class="row g-3">
  <c:forEach var="entry" items="${checks}">
    <c:set var="check" value="${entry.value}"/>
    <div class="col-12 col-md-6 col-xl-4">
      <div class="card h-100"><div class="card-body">
        <div class="d-flex align-items-start justify-content-between gap-2 mb-3">
          <h3 class="card-title mb-0"><c:out value="${check.name()}"/></h3>
          <!-- Badge trạng thái: Tốt/Cảnh báo/Lỗi -->
          <c:choose>
            <c:when test="${check.status() eq 'OK'}"><span class="badge bg-green-lt">Tốt</span></c:when>
            <c:when test="${check.status() eq 'WARN'}"><span class="badge bg-yellow-lt">Cảnh báo</span></c:when>
            <c:otherwise><span class="badge bg-red-lt">Lỗi</span></c:otherwise>
          </c:choose>
        </div>
        <p class="text-secondary mb-0"><c:out value="${check.detail()}"/></p>
      </div></div>
    </div>
  </c:forEach>
</div>

<!-- Khu vực gửi email thử -->
<div class="card mt-4"><div class="card-body d-flex flex-wrap align-items-center justify-content-between gap-3">
  <div>
    <h3 class="card-title mb-1">Gửi email thử</h3>
    <div class="text-secondary">Thư được thêm vào hàng đợi để gửi tới email của tài khoản Admin hiện tại.</div>
    <c:if test="${not emailConfigured}"><div class="text-warning mt-1">Cần cấu hình tài khoản gửi email trước khi thử.</div></c:if>
  </div>
  <form method="post" action="${ctx}/admin/health/test-email" class="m-0">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button class="btn btn-primary" type="submit" ${emailConfigured ? '' : 'disabled'}>Gửi thư thử</button>
  </form>
</div></div>
</div>
