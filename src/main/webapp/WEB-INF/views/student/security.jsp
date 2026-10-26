<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Bảo mật tài khoản</title>
<div class="qa-page-student-security" data-qa-view="student-security">
<style>
.qa-page-student-security { --page-accent: #c44c65; --page-accent-dark: #96364a; --page-tint: #fff0f3; --page-line: #f5d8df; min-width: 0; color: #344c67; }
.qa-page-student-security > .page-header, .qa-page-student-security > .qa-page-heading, .qa-page-student-security > .d-flex:has(> .page-title), .qa-page-student-security > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff0f3, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-student-security > .page-header::after, .qa-page-student-security > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f5d8df; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-student-security .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-student-security .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f5d8df; border-radius: 12px; color: #96364a; background: linear-gradient(145deg,#fff,#fff0f3); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-student-security .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-student-security .card:hover { border-color: #f5d8df; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-student-security .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff0f3); }
.qa-page-student-security .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-student-security .card-body { color: #435b75; }
.qa-page-student-security .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#fff0f3); }
.qa-page-student-security .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-student-security .form-control, .qa-page-student-security .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-student-security textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-student-security .form-control:focus, .qa-page-student-security .form-select:focus { border-color: #c44c65; box-shadow: 0 0 0 4px #fff0f3; }
.qa-page-student-security .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-student-security .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-student-security .btn-primary { border-color: #c44c65; background: linear-gradient(120deg,#c44c65,#96364a); box-shadow: 0 5px 13px #f5d8df; }
.qa-page-student-security .btn-outline-primary { border-color: #f5d8df; color: #96364a; background: #fff; }
.qa-page-student-security .btn-outline-primary:hover { border-color: #c44c65; color: #fff; background: linear-gradient(120deg,#c44c65,#96364a); }
.qa-page-student-security .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#fff0f3); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-student-security .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-student-security .table { margin-bottom: 0; color: #405872; }
.qa-page-student-security .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff0f3,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-student-security .table tbody tr { transition: background-color .18s ease; }
.qa-page-student-security .table tbody tr:hover { background: #fff0f3; }
.qa-page-student-security .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-student-security .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-student-security .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-student-security .nav-tabs .nav-link.active { color: #96364a; box-shadow: inset 0 -2px #c44c65; }
.qa-page-student-security .nav-pills .nav-link.active { color: #96364a; background: #fff0f3; box-shadow: inset 0 0 0 1px #f5d8df; }
.qa-page-student-security .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-student-security .progress-bar { border-radius: inherit; }
.qa-page-student-security > .d-flex:has(> .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; justify-content: space-between; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff0f3, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-student-security > .d-flex:has(> .page-title)::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f5d8df; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-student-security .security-page-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; margin: 0; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-student-security .security-page-title .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f5d8df; border-radius: 12px; color: #96364a; background: linear-gradient(145deg,#fff,#fff0f3); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-student-security .security-card-title { margin: 0; color: #294664; font-size: 1rem; font-weight: 800; letter-spacing: -.015em; }

@media (max-width: 767.98px) {
  .qa-page-student-security > .page-header, .qa-page-student-security > .qa-page-heading, .qa-page-student-security > .d-flex:has(> .page-title), .qa-page-student-security > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-student-security > .d-flex:has(> .page-title) { flex-wrap: wrap; }
  .qa-page-student-security .card { border-radius: 15px; }
  .qa-page-student-security .card-body { padding: 1rem; }
  .qa-page-student-security .table-responsive { border-radius: 11px; }
  .qa-page-student-security .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-student-security *, .qa-page-student-security *::before, .qa-page-student-security *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="page-title security-page-title" data-qa-page="student-security">
        <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-shield-lock"></i></span>Bảo mật tài khoản
    </h2>
    <a class="btn btn-outline-secondary" href="${ctx}/student/profile">Hồ sơ</a>
</div>

<%-- Đổi mật khẩu và quản lý các phiên đăng nhập trên thiết bị. --%>
<div class="row g-4">
    <div class="col-12 col-lg-5">
        <div class="card">
            <div class="card-header">
                <h3 class="card-title security-card-title">Đổi mật khẩu</h3>
            </div>
            <div class="card-body">
                <form method="post" action="${ctx}/student/profile/security/password" id="register-form" novalidate>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <c:if test="${not empty errors['global']}">
                        <div class="alert alert-danger mb-3"><c:out value="${errors['global']}"/></div>
                    </c:if>
                    <div class="mb-3">
                        <label class="form-label required" for="currentPassword">Mật khẩu hiện tại</label>
                        <div class="input-group input-group-flat">
                            <input type="password" class="form-control ${not empty errors['currentPassword'] ? 'is-invalid' : ''}"
                                   id="currentPassword" name="currentPassword" autocomplete="current-password" required>
                            <button class="btn btn-icon" type="button" data-toggle-password="#currentPassword"
                                    aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
                        </div>
                        <qa:formError field="currentPassword"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label required" for="password">Mật khẩu mới</label>
                        <div class="input-group input-group-flat">
                            <input type="password" class="form-control ${not empty errors['newPassword'] ? 'is-invalid' : ''}"
                                   id="password" name="newPassword" autocomplete="new-password" minlength="8" maxlength="64" required>
                            <button class="btn btn-icon" type="button" data-toggle-password="#password"
                                    aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
                        </div>
                        <div class="progress mt-2" style="height: 5px;"><div id="password-strength" class="progress-bar"></div></div>
                        <small id="password-strength-text" class="form-hint"></small>
                        <qa:formError field="newPassword"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label required" for="confirmPassword">Nhập lại mật khẩu mới</label>
                        <div class="input-group input-group-flat">
                            <input type="password" class="form-control ${not empty errors['confirmPassword'] ? 'is-invalid' : ''}"
                                   id="confirmPassword" name="confirmPassword" autocomplete="new-password" required>
                            <button class="btn btn-icon" type="button" data-toggle-password="#confirmPassword"
                                    aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
                        </div>
                        <qa:formError field="confirmPassword"/>
                    </div>
                    <button type="submit" class="btn btn-primary w-100" id="register-submit" data-loading-text="Đang đổi mật khẩu...">
                        <i class="bi bi-shield-lock me-1"></i> Đổi mật khẩu
                    </button>
                </form>
            </div>
        </div>
    </div>
    <%-- Xem trạng thái phiên và thu hồi một hoặc nhiều thiết bị khác. --%>
    <div class="col-12 col-lg-7">
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h3 class="card-title m-0">Phiên đăng nhập</h3>
                <qa:confirmModal id="revoke-others-modal"
                                 title="Đăng xuất tất cả thiết bị khác"
                                 message="Bạn có chắc chắn muốn đăng xuất khỏi tất cả các thiết bị khác không? Phiên đăng nhập hiện tại sẽ được giữ lại."
                                 actionUrl="${ctx}/student/profile/sessions/revoke-others"
                                 confirmLabel="Đăng xuất tất cả thiết bị khác"
                                 cssClass="btn-outline-danger btn-sm"/>
            </div>
            <div class="table-responsive">
                <table class="table card-table table-vcenter">
                    <thead>
                        <tr>
                            <th>Thiết bị</th>
                            <th>Địa chỉ IP</th>
                            <th>Tạo lúc</th>
                            <th>Hoạt động cuối</th>
                            <th>Trạng thái</th>
                            <th class="w-1"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="session" items="${sessions}">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <i class="bi bi-laptop me-2 fs-3 text-secondary"></i>
                                        <div><c:out value="${empty session.deviceInfo() ? 'Không xác định' : session.deviceInfo()}"/></div>
                                    </div>
                                </td>
                                <td><c:out value="${empty session.ip() ? '—' : session.ip()}"/></td>
                                <td><c:out value="${session.createdAt()}"/></td>
                                <td><c:out value="${empty session.lastUsedAt() ? '—' : session.lastUsedAt()}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${session.current()}">
                                            <span class="badge bg-success-lt"><i class="bi bi-check-circle me-1"></i>Phiên hiện tại</span>
                                        </c:when>
                                        <c:when test="${session.revoked()}">
                                            <span class="badge bg-secondary-lt">Đã đăng xuất</span>
                                        </c:when>
                                        <c:when test="${session.expired()}">
                                            <span class="badge bg-warning-lt">Hết hạn</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-primary-lt">Đang hoạt động</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:if test="${!session.current() and !session.revoked() and !session.expired()}">
                                        <form method="post" action="${ctx}/student/profile/sessions/${session.id()}/revoke" class="d-inline">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <button type="submit" class="btn btn-sm btn-outline-danger text-nowrap">
                                                Đăng xuất thiết bị
                                            </button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty sessions}">
                            <tr>
                                <td colspan="6" class="text-center text-secondary py-3">Chưa có phiên đăng nhập nào.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script src="/assets/js/auth/register.js"></script>
</div>
