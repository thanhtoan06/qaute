<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Đổi mật khẩu người dùng</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="qa-page-heading">
  <div><span class="qa-page-eyebrow">Quản trị tài khoản</span><h1 class="page-title mb-0">Đổi mật khẩu người dùng</h1><p>Đặt mật khẩu mới và thu hồi các phiên đăng nhập hiện có.</p></div>
  <a class="btn btn-outline-secondary" href="${ctx}/admin/users/${user.id()}"><i class="bi bi-arrow-left me-2"></i>Chi tiết người dùng</a>
</div>
<div class="row justify-content-center"><div class="col-lg-8 col-xl-6">
  <div class="card"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-key"></i></span><h2 class="card-title mb-0">Đặt mật khẩu mới</h2></div><div class="card-body">
    <div class="border rounded p-3 mb-3"><strong><c:out value="${user.fullName()}"/></strong><div class="text-secondary small mt-1"><c:out value="${user.email()}"/></div></div>
    <div class="alert alert-warning" role="note">Mọi phiên đăng nhập và liên kết đặt lại mật khẩu cũ sẽ bị vô hiệu hóa. Nếu đổi mật khẩu của chính mình, bạn cần đăng nhập lại.</div>
    <c:if test="${user.status() != 'ACTIVE'}"><div class="alert alert-info">Tài khoản vẫn cần được kích hoạt hoặc mở khóa để có thể đăng nhập.</div></c:if>
    <c:if test="${not empty errors.global}"><div class="alert alert-danger" role="alert"><c:out value="${errors.global}"/></div></c:if>
    <form method="post" action="${ctx}/admin/users/${user.id()}/password" autocomplete="off">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div class="mb-3"><label class="form-label required" for="admin-new-password">Mật khẩu mới</label><input class="form-control ${not empty errors.newPassword ? 'is-invalid' : ''}" id="admin-new-password" name="newPassword" type="password" minlength="8" maxlength="64" autocomplete="new-password" required aria-describedby="admin-password-hint"/><qa:formError field="newPassword"/><div id="admin-password-hint" class="form-hint">8–64 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt.</div></div>
      <div class="mb-4"><label class="form-label required" for="admin-confirm-password">Nhập lại mật khẩu mới</label><input class="form-control ${not empty errors.confirmPassword ? 'is-invalid' : ''}" id="admin-confirm-password" name="confirmPassword" type="password" minlength="8" maxlength="64" autocomplete="new-password" required/><qa:formError field="confirmPassword"/></div>
      <div class="d-flex flex-wrap gap-2"><button class="btn btn-primary" type="submit"><i class="bi bi-check2 me-2"></i>Lưu mật khẩu mới</button><a class="btn btn-outline-secondary" href="${ctx}/admin/users/${user.id()}">Hủy</a></div>
    </form>
  </div></div>
</div></div>
