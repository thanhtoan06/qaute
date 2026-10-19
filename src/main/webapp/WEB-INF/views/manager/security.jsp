<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Bảo mật tài khoản Manager</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex justify-content-between align-items-center mb-3"><h2 class="page-title mb-0">Bảo mật tài khoản</h2><a class="btn btn-outline-secondary" href="${ctx}/manager/profile">Hồ sơ</a></div>
<div class="row g-4">
  <div class="col-12 col-lg-5"><div class="card"><div class="card-header"><h3 class="card-title">Đổi mật khẩu</h3></div><div class="card-body">
    <form method="post" action="${ctx}/manager/profile/security/password" novalidate>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <c:if test="${not empty errors.global}"><div class="alert alert-danger"><c:out value="${errors.global}"/></div></c:if>
      <div class="mb-3"><label class="form-label required" for="current-password">Mật khẩu hiện tại</label><input class="form-control ${not empty errors.currentPassword ? 'is-invalid' : ''}" id="current-password" type="password" name="currentPassword" autocomplete="current-password" required/><qa:formError field="currentPassword"/></div>
      <div class="mb-3"><label class="form-label required" for="new-password">Mật khẩu mới</label><input class="form-control ${not empty errors.newPassword ? 'is-invalid' : ''}" id="new-password" type="password" name="newPassword" autocomplete="new-password" minlength="8" maxlength="64" required/><qa:formError field="newPassword"/></div>
      <div class="mb-3"><label class="form-label required" for="confirm-password">Xác nhận mật khẩu mới</label><input class="form-control ${not empty errors.confirmPassword ? 'is-invalid' : ''}" id="confirm-password" type="password" name="confirmPassword" autocomplete="new-password" required/><qa:formError field="confirmPassword"/></div>
      <button class="btn btn-primary w-100" type="submit">Đổi mật khẩu</button>
    </form>
  </div></div></div>
  <div class="col-12 col-lg-7"><div class="card"><div class="card-header"><h3 class="card-title">Phiên đăng nhập</h3></div><div class="table-responsive"><table class="table table-vcenter card-table">
    <thead><tr><th>Thiết bị</th><th>IP</th><th>Tạo lúc</th><th>Hoạt động cuối</th><th>Trạng thái</th><th></th></tr></thead>
    <tbody><c:choose><c:when test="${empty sessions}"><tr><td colspan="6" class="text-secondary">Chưa có phiên đăng nhập.</td></tr></c:when><c:otherwise><c:forEach var="session" items="${sessions}"><tr>
      <td><c:out value="${session.deviceInfo()}" default="Không xác định"/></td><td><c:out value="${session.ip()}" default="—"/></td>
      <td><c:out value="${session.createdAt()}"/></td><td><c:out value="${session.lastUsedAt()}" default="—"/></td>
      <td><c:choose><c:when test="${session.current()}"><span class="badge bg-green-lt">Hiện tại</span></c:when><c:when test="${session.revoked()}"><span class="badge bg-secondary-lt">Đã thu hồi</span></c:when><c:when test="${session.expired()}"><span class="badge bg-yellow-lt">Hết hạn</span></c:when><c:otherwise><span class="badge bg-blue-lt">Đang hoạt động</span></c:otherwise></c:choose></td>
      <td><c:if test="${not session.current() and not session.revoked() and not session.expired()}"><form method="post" action="${ctx}/manager/profile/sessions/${session.id()}/revoke"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-danger" type="submit">Thu hồi</button></form></c:if></td>
    </tr></c:forEach></c:otherwise></c:choose></tbody>
  </table></div></div></div>
</div>
