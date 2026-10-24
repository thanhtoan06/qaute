<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Hồ sơ admin</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="mb-3">
  <h2 class="page-title mb-1">Hồ sơ admin</h2>
  <p class="text-secondary mb-0">Quản lý thông tin cá nhân và bảo mật tài khoản quản trị của bạn.</p>
</div>

<div class="row g-4">
  <div class="col-12 col-md-4 col-lg-3">
    <div class="card text-center"><div class="card-body">
      <div class="position-relative d-inline-block mb-3">
        <c:choose>
          <c:when test="${not empty profile.avatarUrl()}">
            <img id="avatarPreview" src="<c:out value='${profile.avatarUrl()}'/>" alt="Ảnh đại diện" class="avatar rounded-circle border shadow-sm" style="width:140px;height:140px;object-fit:cover"/>
          </c:when>
          <c:otherwise>
            <span id="avatarPreviewPlaceholder" class="avatar rounded-circle bg-primary-lt border shadow-sm" style="width:140px;height:140px"><i class="bi bi-person fs-1"></i></span>
            <img id="avatarPreview" alt="Ảnh đại diện" class="avatar rounded-circle border shadow-sm d-none" style="width:140px;height:140px;object-fit:cover"/>
          </c:otherwise>
        </c:choose>
        <div id="avatarSpinner" class="position-absolute top-50 start-50 translate-middle d-none" aria-live="polite"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Đang tải ảnh</span></div></div>
      </div>
      <h3 class="mb-1"><c:out value="${profile.fullName()}"/></h3>
      <span class="badge bg-primary-lt mb-3">Quản trị viên</span>
      <button type="button" class="btn btn-outline-primary w-100" id="btnChangeAvatar">Đổi ảnh đại diện</button>
      <input type="file" id="avatarFileInput" accept="image/png,image/jpeg,image/webp,image/gif" class="d-none"/>
      <form id="avatarForm" method="post" action="${ctx}/admin/profile/avatar" data-upload-url="${ctx}/api/media/upload" class="d-none">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="assetId" id="avatarAssetIdInput"/>
      </form>
      <div id="avatarError" class="alert alert-danger small mt-3 d-none" role="alert"></div>
      <div class="text-secondary small mt-2">Ảnh được cắt vuông 400 × 400 trước khi tải lên.</div>
    </div></div>
  </div>
  <div class="col-12 col-md-8 col-lg-9">
    <form method="post" action="${ctx}/admin/profile" class="card mb-4">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div class="card-header"><h3 class="card-title">Thông tin cá nhân</h3></div>
      <div class="card-body">
        <c:if test="${not empty errors.global}"><div class="alert alert-danger" role="alert"><c:out value="${errors.global}"/></div></c:if>
        <div class="row g-3">
          <div class="col-12">
            <label class="form-label required" for="admin-name">Họ và tên</label>
            <input class="form-control ${not empty errors.fullName ? 'is-invalid' : ''}" id="admin-name" name="fullName" autocomplete="name" value="<c:out value='${form.fullName}'/>" minlength="2" maxlength="100" required/>
            <qa:formError field="fullName"/>
          </div>
          <div class="col-md-6">
            <label class="form-label required" for="admin-email">Email</label>
            <input class="form-control ${not empty errors.email ? 'is-invalid' : ''}" id="admin-email" name="email" type="email" autocomplete="email" value="<c:out value='${form.email}'/>" maxlength="150" required/>
            <qa:formError field="email"/>
            <div class="form-hint">Email này được dùng để đăng nhập và nhận thông báo.</div>
          </div>
          <div class="col-md-6">
            <label class="form-label" for="admin-phone">Số điện thoại</label>
            <input class="form-control ${not empty errors.phone ? 'is-invalid' : ''}" id="admin-phone" name="phone" type="tel" autocomplete="tel" value="<c:out value='${form.phone}'/>" maxlength="20" placeholder="0xxxxxxxxx hoặc +84xxxxxxxxx"/>
            <qa:formError field="phone"/>
          </div>
        </div>
      </div>
      <div class="card-footer text-end"><button class="btn btn-primary" type="submit">Lưu thông tin</button></div>
    </form>

    <form method="post" action="${ctx}/admin/profile/password" class="card" id="admin-password-form">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div class="card-header"><h3 class="card-title">Đổi mật khẩu của tôi</h3></div>
      <div class="card-body">
        <c:if test="${not empty passwordErrors.global}"><div class="alert alert-danger" role="alert"><c:out value="${passwordErrors.global}"/></div></c:if>
        <p class="text-secondary">Mật khẩu 8–64 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt. Các thiết bị khác sẽ bị đăng xuất sau khi đổi.</p>
        <div class="mb-3">
          <label class="form-label required" for="admin-current-password">Mật khẩu hiện tại</label>
          <input class="form-control ${not empty passwordErrors.currentPassword ? 'is-invalid' : ''}" id="admin-current-password" name="currentPassword" type="password" autocomplete="current-password" required/>
          <c:if test="${not empty passwordErrors.currentPassword}"><div class="invalid-feedback d-block"><c:out value="${passwordErrors.currentPassword}"/></div></c:if>
        </div>
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label required" for="admin-new-password">Mật khẩu mới</label>
            <input class="form-control ${not empty passwordErrors.newPassword ? 'is-invalid' : ''}" id="admin-new-password" name="newPassword" type="password" autocomplete="new-password" minlength="8" maxlength="64" required/>
            <c:if test="${not empty passwordErrors.newPassword}"><div class="invalid-feedback d-block"><c:out value="${passwordErrors.newPassword}"/></div></c:if>
          </div>
          <div class="col-md-6">
            <label class="form-label required" for="admin-confirm-password">Nhập lại mật khẩu mới</label>
            <input class="form-control ${not empty passwordErrors.confirmPassword ? 'is-invalid' : ''}" id="admin-confirm-password" name="confirmPassword" type="password" autocomplete="new-password" minlength="8" maxlength="64" required/>
            <c:if test="${not empty passwordErrors.confirmPassword}"><div class="invalid-feedback d-block"><c:out value="${passwordErrors.confirmPassword}"/></div></c:if>
          </div>
        </div>
      </div>
      <div class="card-footer text-end"><button class="btn btn-primary" type="submit">Đổi mật khẩu</button></div>
    </form>
  </div>
</div>
<script src="${ctx}/assets/js/admin/profile.js" defer></script>
