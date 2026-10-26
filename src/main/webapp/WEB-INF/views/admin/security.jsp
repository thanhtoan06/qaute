<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Bảo mật tài khoản</title>
<div class="qa-page-admin-security" data-qa-view="admin-security">
<style>
.qa-page-admin-security { --page-accent: #c44c65; --page-accent-dark: #96364a; --page-tint: #fff0f3; --page-line: #f5d8df; min-width: 0; color: #344c67; }
.qa-page-admin-security > .d-flex:has(> .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; justify-content: space-between; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff0f3, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-security > .d-flex:has(> .page-title)::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f5d8df; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-security .security-page-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; margin: 0; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-security .security-page-title .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f5d8df; border-radius: 12px; color: #96364a; background: linear-gradient(145deg,#fff,#fff0f3); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-security .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-security .card:hover { border-color: #f5d8df; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-security .card-header { display: flex; align-items: center; justify-content: space-between; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff0f3); }
.qa-page-admin-security .card-title { margin: 0; color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-security .card-body { color: #435b75; }
.qa-page-admin-security .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-security .form-control { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-security .form-control:focus { border-color: #c44c65; box-shadow: 0 0 0 4px #fff0f3; }
.qa-page-admin-security .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-security .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-security .btn-primary { border-color: #c44c65; background: linear-gradient(120deg,#c44c65,#96364a); box-shadow: 0 5px 13px #f5d8df; }
.qa-page-admin-security .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-security .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-security .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff0f3,#f8faff); border-bottom-color: #dfe8f3; white-space: nowrap; }
.qa-page-admin-security .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-security .table tbody tr:hover { background: #fff0f3; }
.qa-page-admin-security .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
@media (max-width: 767.98px) {
  .qa-page-admin-security > .d-flex:has(> .page-title) { flex-wrap: wrap; align-items: flex-start; padding: .95rem; }
  .qa-page-admin-security .card { border-radius: 15px; }
  .qa-page-admin-security .card-body { padding: 1rem; }
  .qa-page-admin-security .table-responsive { border-radius: 11px; }
  .qa-page-admin-security .security-page-title .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-security *, .qa-page-admin-security *::before, .qa-page-admin-security *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>

<div class="d-flex justify-content-between align-items-center mb-3">
  <h2 class="page-title security-page-title" data-qa-page="admin-security">
    <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-shield-lock"></i></span>Bảo mật tài khoản
  </h2>
  <a class="btn btn-outline-secondary" href="${ctx}/admin/profile">Hồ sơ</a>
</div>

<div class="row g-4">
  <div class="col-12 col-lg-5">
    <div class="card">
      <div class="card-header"><h3 class="card-title">Đổi mật khẩu</h3></div>
      <div class="card-body">
        <form method="post" action="${ctx}/admin/profile/security/password" novalidate>
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <c:if test="${not empty errors.global}"><div class="alert alert-danger"><c:out value="${errors.global}"/></div></c:if>
          <div class="mb-3">
            <label class="form-label required" for="current-password">Mật khẩu hiện tại</label>
            <div class="input-group">
              <input class="form-control ${not empty errors.currentPassword ? 'is-invalid' : ''}" id="current-password" type="password" name="currentPassword" autocomplete="current-password" required/>
              <button class="btn btn-outline-secondary" type="button" data-toggle-password="#current-password" aria-label="Hiện mật khẩu" aria-pressed="false"><i class="bi bi-eye" aria-hidden="true"></i></button>
            </div>
            <qa:formError field="currentPassword"/>
          </div>
          <div class="mb-3">
            <label class="form-label required" for="new-password">Mật khẩu mới</label>
            <div class="input-group">
              <input class="form-control ${not empty errors.newPassword ? 'is-invalid' : ''}" id="new-password" type="password" name="newPassword" autocomplete="new-password" minlength="8" maxlength="64" required/>
              <button class="btn btn-outline-secondary" type="button" data-toggle-password="#new-password" aria-label="Hiện mật khẩu" aria-pressed="false"><i class="bi bi-eye" aria-hidden="true"></i></button>
            </div>
            <qa:formError field="newPassword"/>
          </div>
          <div class="mb-3">
            <label class="form-label required" for="confirm-password">Xác nhận mật khẩu mới</label>
            <div class="input-group">
              <input class="form-control ${not empty errors.confirmPassword ? 'is-invalid' : ''}" id="confirm-password" type="password" name="confirmPassword" autocomplete="new-password" required/>
              <button class="btn btn-outline-secondary" type="button" data-toggle-password="#confirm-password" aria-label="Hiện mật khẩu" aria-pressed="false"><i class="bi bi-eye" aria-hidden="true"></i></button>
            </div>
            <qa:formError field="confirmPassword"/>
          </div>
          <button class="btn btn-primary w-100" type="submit"><i class="bi bi-shield-lock me-1"></i>Đổi mật khẩu</button>
        </form>
      </div>
    </div>
  </div>

  <div class="col-12 col-lg-7">
    <div class="card">
      <div class="card-header"><h3 class="card-title">Phiên đăng nhập</h3></div>
      <div class="table-responsive">
        <table class="table table-vcenter card-table">
          <thead><tr><th>Thiết bị</th><th>IP</th><th>Tạo lúc</th><th>Hoạt động cuối</th><th>Trạng thái</th><th></th></tr></thead>
          <tbody>
            <c:choose>
              <c:when test="${empty sessions}"><tr><td colspan="6" class="text-secondary">Chưa có phiên đăng nhập.</td></tr></c:when>
              <c:otherwise>
                <c:forEach var="session" items="${sessions}">
                  <tr>
                    <td><c:out value="${session.deviceInfo()}" default="Không xác định"/></td>
                    <td><c:out value="${session.ip()}" default="—"/></td>
                    <td><c:out value="${session.createdAt()}"/></td>
                    <td><c:out value="${session.lastUsedAt()}" default="—"/></td>
                    <td>
                      <c:choose>
                        <c:when test="${session.current()}"><span class="badge bg-green-lt">Hiện tại</span></c:when>
                        <c:when test="${session.revoked()}"><span class="badge bg-secondary-lt">Đã thu hồi</span></c:when>
                        <c:when test="${session.expired()}"><span class="badge bg-yellow-lt">Hết hạn</span></c:when>
                        <c:otherwise><span class="badge bg-blue-lt">Đang hoạt động</span></c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <c:if test="${not session.current() and not session.revoked() and not session.expired()}">
                        <form method="post" action="${ctx}/admin/profile/security/sessions/${session.id()}/revoke">
                          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                          <button class="btn btn-sm btn-outline-danger text-nowrap" type="submit">Thu hồi</button>
                        </form>
                      </c:if>
                    </td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <script src="${ctx}/assets/js/common/password-visibility.js?v=20261026a"></script>
  </div>
</div>
