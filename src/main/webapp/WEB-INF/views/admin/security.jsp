<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Bảo mật tài khoản</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/security.css">
</head>
<div class="qa-page-admin-security" data-qa-view="admin-security">


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
                    <td><time datetime="${session.createdAt()}" data-qaute-datetime><c:out value="${session.createdAt()}"/></time></td>
                    <td><time datetime="${session.lastUsedAt()}" data-qaute-datetime><c:out value="${session.lastUsedAt()}" default="—"/></time></td>
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
