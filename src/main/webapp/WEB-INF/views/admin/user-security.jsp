<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Bảo mật người dùng</title>
<div class="qa-page-admin-user-security" data-qa-view="admin-user-security">
<style>
.qa-page-admin-user-security { --page-accent: #c44c65; --page-accent-dark: #96364a; --page-tint: #fff0f3; --page-line: #f5d8df; min-width: 0; color: #344c67; }
.qa-page-admin-user-security > .page-header, .qa-page-admin-user-security > .qa-page-heading, .qa-page-admin-user-security > .d-flex:has(> .page-title), .qa-page-admin-user-security > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff0f3, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-user-security > .page-header::after, .qa-page-admin-user-security > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f5d8df; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-user-security .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-user-security .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f5d8df; border-radius: 12px; color: #96364a; background: linear-gradient(145deg,#fff,#fff0f3); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-user-security .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-user-security .card:hover { border-color: #f5d8df; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-user-security .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff0f3); }
.qa-page-admin-user-security .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-user-security .card-body { color: #435b75; }
.qa-page-admin-user-security .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#fff0f3); }
.qa-page-admin-user-security .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-user-security .form-control, .qa-page-admin-user-security .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-user-security textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-user-security .form-control:focus, .qa-page-admin-user-security .form-select:focus { border-color: #c44c65; box-shadow: 0 0 0 4px #fff0f3; }
.qa-page-admin-user-security .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-user-security .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-user-security .btn-primary { border-color: #c44c65; background: linear-gradient(120deg,#c44c65,#96364a); box-shadow: 0 5px 13px #f5d8df; }
.qa-page-admin-user-security .btn-outline-primary { border-color: #f5d8df; color: #96364a; background: #fff; }
.qa-page-admin-user-security .btn-outline-primary:hover { border-color: #c44c65; color: #fff; background: linear-gradient(120deg,#c44c65,#96364a); }
.qa-page-admin-user-security .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#fff0f3); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-user-security .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-user-security .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-user-security .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff0f3,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-user-security .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-user-security .table tbody tr:hover { background: #fff0f3; }
.qa-page-admin-user-security .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-user-security .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-user-security .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-user-security .nav-tabs .nav-link.active { color: #96364a; box-shadow: inset 0 -2px #c44c65; }
.qa-page-admin-user-security .nav-pills .nav-link.active { color: #96364a; background: #fff0f3; box-shadow: inset 0 0 0 1px #f5d8df; }
.qa-page-admin-user-security .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-user-security .progress-bar { border-radius: inherit; }
.qa-page-admin-user-security .card-header { background-image: linear-gradient(90deg,var(--page-accent),transparent 72%); background-repeat: no-repeat; background-size: 100% 2px; background-position: top; }
@media (max-width: 767.98px) {
  .qa-page-admin-user-security > .page-header, .qa-page-admin-user-security > .qa-page-heading, .qa-page-admin-user-security > .d-flex:has(> .page-title), .qa-page-admin-user-security > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-user-security .card { border-radius: 15px; }
  .qa-page-admin-user-security .card-body { padding: 1rem; }
  .qa-page-admin-user-security .table-responsive { border-radius: 11px; }
  .qa-page-admin-user-security .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-user-security *, .qa-page-admin-user-security *::before, .qa-page-admin-user-security *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và nút quay lại -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <div>
    <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-user-security"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-shield-lock"></i></span>Bảo mật: <c:out value="${user.fullName()}"/></h2>
    <div class="text-secondary"><c:out value="${user.email()}"/></div>
  </div>
  <a class="btn btn-outline-secondary" href="${ctx}/admin/users/${user.id()}">Chi tiết người dùng</a>
</div>

<!-- Bảng phiên đăng nhập -->
<div class="card mb-3">
  <div class="card-header d-flex justify-content-between align-items-center">
    <h3 class="card-title">Phiên đăng nhập</h3>
    <!-- Nút thu hồi tất cả phiên -->
    <qa:confirmModal id="revoke-all-user-${user.id()}" title="Thu hồi mọi phiên"
                     message="Thu hồi tất cả phiên đăng nhập của người dùng này?"
                     actionUrl="${ctx}/admin/users/${user.id()}/sessions/revoke-all"
                     confirmLabel="Thu hồi tất cả" cssClass="btn-outline-danger"/>
  </div>
  <div class="table-responsive">
    <table class="table card-table table-vcenter">
      <thead><tr><th>Thiết bị</th><th>IP</th><th>Tạo lúc</th><th>Hoạt động cuối</th><th>Trạng thái</th><th></th></tr></thead>
      <tbody>
        <c:forEach var="session" items="${sessions}">
          <tr>
            <td><c:out value="${empty session.deviceInfo() ? '—' : session.deviceInfo()}"/></td>
            <td><c:out value="${empty session.ip() ? '—' : session.ip()}"/></td>
            <td><c:out value="${session.createdAt()}"/></td>
            <td><c:out value="${empty session.lastUsedAt() ? '—' : session.lastUsedAt()}"/></td>
            <td><c:choose><c:when test="${session.revoked()}"><span class="badge bg-secondary-lt">Đã thu hồi</span></c:when><c:when test="${session.expired()}"><span class="badge bg-secondary-lt">Hết hạn</span></c:when><c:otherwise><span class="badge bg-green-lt">Còn hiệu lực</span></c:otherwise></c:choose></td>
            <!-- Nút thu hồi từng phiên -->
            <td>
              <c:if test="${!session.revoked() and !session.expired()}">
                <form method="post" action="${ctx}/admin/users/${user.id()}/sessions/${session.id()}/revoke">
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                  <button type="submit" class="btn btn-sm btn-outline-danger">Thu hồi</button>
                </form>
              </c:if>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty sessions}"><tr><td colspan="6" class="text-center text-secondary">Chưa có phiên đăng nhập.</td></tr></c:if>
      </tbody>
    </table>
  </div>
</div>

<!-- Bảng lịch sử đăng nhập -->
<div class="card">
  <div class="card-header"><h3 class="card-title">Lịch sử đăng nhập (50 lần gần nhất)</h3></div>
  <div class="table-responsive">
    <table class="table card-table table-vcenter">
      <thead><tr><th>Thời gian</th><th>Kết quả</th><th>IP</th><th>User-agent</th></tr></thead>
      <tbody>
        <c:forEach var="entry" items="${loginHistory}">
          <tr>
            <td><c:out value="${entry.at()}"/></td>
            <td><c:choose><c:when test="${entry.success()}"><span class="badge bg-green-lt">Thành công</span></c:when><c:otherwise><span class="badge bg-red-lt">Thất bại</span></c:otherwise></c:choose></td>
            <td><c:out value="${empty entry.ip() ? '—' : entry.ip()}"/></td>
            <td class="text-wrap"><c:out value="${empty entry.userAgent() ? '—' : entry.userAgent()}"/></td>
          </tr>
        </c:forEach>
        <c:if test="${empty loginHistory}"><tr><td colspan="4" class="text-center text-secondary">Chưa có lịch sử đăng nhập.</td></tr></c:if>
      </tbody>
    </table>
  </div>
</div>
</div>
