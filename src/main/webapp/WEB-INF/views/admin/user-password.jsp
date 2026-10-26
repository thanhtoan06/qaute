<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Đổi mật khẩu người dùng</title>
<div class="qa-page-admin-user-password" data-qa-view="admin-user-password">
<style>
.qa-page-admin-user-password { --page-accent: #c44c65; --page-accent-dark: #96364a; --page-tint: #fff0f3; --page-line: #f5d8df; min-width: 0; color: #344c67; }
.qa-page-admin-user-password > .page-header, .qa-page-admin-user-password > .qa-page-heading, .qa-page-admin-user-password > .d-flex:has(> .page-title), .qa-page-admin-user-password > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff0f3, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-user-password > .page-header::after, .qa-page-admin-user-password > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f5d8df; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-user-password .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-user-password .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f5d8df; border-radius: 12px; color: #96364a; background: linear-gradient(145deg,#fff,#fff0f3); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-user-password .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-user-password .card:hover { border-color: #f5d8df; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-user-password .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff0f3); }
.qa-page-admin-user-password .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-user-password .card-body { color: #435b75; }
.qa-page-admin-user-password .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#fff0f3); }
.qa-page-admin-user-password .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-user-password .form-control, .qa-page-admin-user-password .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-user-password textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-user-password .form-control:focus, .qa-page-admin-user-password .form-select:focus { border-color: #c44c65; box-shadow: 0 0 0 4px #fff0f3; }
.qa-page-admin-user-password .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-user-password .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-user-password .btn-primary { border-color: #c44c65; background: linear-gradient(120deg,#c44c65,#96364a); box-shadow: 0 5px 13px #f5d8df; }
.qa-page-admin-user-password .btn-outline-primary { border-color: #f5d8df; color: #96364a; background: #fff; }
.qa-page-admin-user-password .btn-outline-primary:hover { border-color: #c44c65; color: #fff; background: linear-gradient(120deg,#c44c65,#96364a); }
.qa-page-admin-user-password .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#fff0f3); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-user-password .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-user-password .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-user-password .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff0f3,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-user-password .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-user-password .table tbody tr:hover { background: #fff0f3; }
.qa-page-admin-user-password .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-user-password .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-user-password .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-user-password .nav-tabs .nav-link.active { color: #96364a; box-shadow: inset 0 -2px #c44c65; }
.qa-page-admin-user-password .nav-pills .nav-link.active { color: #96364a; background: #fff0f3; box-shadow: inset 0 0 0 1px #f5d8df; }
.qa-page-admin-user-password .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-user-password .progress-bar { border-radius: inherit; }
.qa-page-admin-user-password form:not(.qa-filter-bar) { --form-accent: var(--page-accent); }
.qa-page-admin-user-password form:not(.qa-filter-bar) > .card, .qa-page-admin-user-password form:not(.qa-filter-bar) .card { border-radius: 20px; }
.qa-page-admin-user-password form:not(.qa-filter-bar) .form-control:focus, .qa-page-admin-user-password form:not(.qa-filter-bar) .form-select:focus { border-color: var(--page-accent); box-shadow: 0 0 0 4px var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-admin-user-password > .page-header, .qa-page-admin-user-password > .qa-page-heading, .qa-page-admin-user-password > .d-flex:has(> .page-title), .qa-page-admin-user-password > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-user-password .card { border-radius: 15px; }
  .qa-page-admin-user-password .card-body { padding: 1rem; }
  .qa-page-admin-user-password .table-responsive { border-radius: 11px; }
  .qa-page-admin-user-password .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-user-password *, .qa-page-admin-user-password *::before, .qa-page-admin-user-password *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- Tiêu đề trang và nút quay lại -->
<div class="qa-page-heading">
  <div><span class="qa-page-eyebrow">Quản trị tài khoản</span><h1 class="page-title mb-0 qa-view-title" data-qa-page="admin-user-password"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-shield-lock"></i></span>Đổi mật khẩu người dùng</h1><p>Đặt mật khẩu mới và thu hồi các phiên đăng nhập hiện có.</p></div>
  <a class="btn btn-outline-secondary" href="${ctx}/admin/users/${user.id()}"><i class="bi bi-arrow-left me-2"></i>Chi tiết người dùng</a>
</div>
<div class="row justify-content-center"><div class="col-lg-8 col-xl-6">
  <!-- Form đổi mật khẩu -->
  <div class="card"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-key"></i></span><h2 class="card-title mb-0">Đặt mật khẩu mới</h2></div><div class="card-body">
    <!-- Thông tin người dùng -->
    <div class="border rounded p-3 mb-3"><strong><c:out value="${user.fullName()}"/></strong><div class="text-secondary small mt-1"><c:out value="${user.email()}"/></div></div>
    <!-- Cảnh báo về phiên đăng nhập -->
    <div class="alert alert-warning" role="note">Mọi phiên đăng nhập và liên kết đặt lại mật khẩu cũ sẽ bị vô hiệu hóa. Nếu đổi mật khẩu của chính mình, bạn cần đăng nhập lại.</div>
    <c:if test="${user.status() != 'ACTIVE'}"><div class="alert alert-info">Tài khoản vẫn cần được kích hoạt hoặc mở khóa để có thể đăng nhập.</div></c:if>
    <c:if test="${not empty errors.global}"><div class="alert alert-danger" role="alert"><c:out value="${errors.global}"/></div></c:if>
    <form method="post" action="${ctx}/admin/users/${user.id()}/password" autocomplete="off">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div class="mb-3"><label class="form-label required" for="admin-new-password">Mật khẩu mới</label><input class="form-control ${not empty errors.newPassword ? 'is-invalid' : ''}" id="admin-new-password" name="newPassword" type="password" minlength="8" maxlength="64" autocomplete="new-password" required aria-describedby="admin-password-hint"/><qa:formError field="newPassword"/><div id="admin-password-hint" class="form-hint">8–64 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt.</div></div>
      <div class="mb-4"><label class="form-label required" for="admin-confirm-password">Nhập lại mật khẩu mới</label><input class="form-control ${not empty errors.confirmPassword ? 'is-invalid' : ''}" id="admin-confirm-password" name="confirmPassword" type="password" minlength="8" maxlength="64" autocomplete="new-password" required/><qa:formError field="confirmPassword"/></div>
      <!-- Nút lưu và hủy -->
      <div class="d-flex flex-wrap gap-2"><button class="btn btn-primary" type="submit"><i class="bi bi-check2 me-2"></i>Lưu mật khẩu mới</button><a class="btn btn-outline-secondary" href="${ctx}/admin/users/${user.id()}">Hủy</a></div>
    </form>
  </div></div>
</div></div>
</div>
