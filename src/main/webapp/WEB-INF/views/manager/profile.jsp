<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Hồ sơ Manager</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex justify-content-between align-items-center mb-3">
  <h2 class="page-title mb-0">Hồ sơ Manager</h2>
  <a class="btn btn-outline-primary" href="${ctx}/manager/profile/security">Bảo mật tài khoản</a>
</div>

<div class="row g-4">
  <div class="col-12 col-md-4 col-lg-3"><div class="card text-center p-3"><div class="card-body p-0">
    <div class="position-relative d-inline-block mb-3">
      <c:choose><c:when test="${not empty profile.avatarUrl()}"><img id="avatarPreview" src="<c:out value='${profile.avatarUrl()}'/>" alt="Ảnh đại diện" class="avatar rounded-circle border shadow-sm" style="width:140px;height:140px;object-fit:cover"/></c:when>
      <c:otherwise><div id="avatarPreviewPlaceholder" class="avatar rounded-circle bg-primary-lt d-flex align-items-center justify-content-center border shadow-sm mx-auto" style="width:140px;height:140px"><i class="bi bi-person fs-1 text-primary"></i></div><img id="avatarPreview" src="" alt="Ảnh đại diện" class="avatar rounded-circle border shadow-sm d-none" style="width:140px;height:140px;object-fit:cover"/></c:otherwise></c:choose>
      <div id="avatarSpinner" class="position-absolute top-50 start-50 translate-middle d-none bg-dark bg-opacity-50 rounded-circle" style="width:140px;height:140px;align-items:center;justify-content:center"><div class="spinner-border text-white" role="status"></div></div>
    </div>
    <h3 class="mb-1"><c:out value="${profile.fullName()}"/></h3>
    <div class="text-secondary small mb-2"><c:out value="${profile.employeeCode()}" default="Manager"/></div>
    <span class="badge ${profile.availabilityStatus() eq 'ONLINE' ? 'bg-green-lt' : profile.availabilityStatus() eq 'BUSY' ? 'bg-yellow-lt' : 'bg-secondary-lt'} mb-3"><c:choose><c:when test="${profile.availabilityStatus() eq 'ONLINE'}">Trực tuyến</c:when><c:when test="${profile.availabilityStatus() eq 'BUSY'}">Bận</c:when><c:otherwise>Ngoại tuyến</c:otherwise></c:choose></span>
    <button type="button" class="btn btn-outline-primary btn-sm w-100" id="btnChangeAvatar">Đổi ảnh đại diện</button>
    <input type="file" id="avatarFileInput" accept="image/png,image/jpeg,image/webp,image/gif" class="d-none"/>
    <form id="avatarForm" method="post" action="${ctx}/manager/profile/avatar" data-upload-url="${ctx}/api/media/upload" class="d-none">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input type="hidden" name="assetId" id="avatarAssetIdInput"/>
    </form>
    <div id="avatarError" class="alert alert-danger small mt-2 d-none" role="alert"></div>
  </div></div></div>

  <div class="col-12 col-md-8 col-lg-9"><form method="post" action="${ctx}/manager/profile" novalidate>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div class="card"><div class="card-header"><h3 class="card-title">Thông tin cá nhân</h3></div><div class="card-body">
      <c:if test="${not empty errors.global}"><div class="alert alert-danger"><c:out value="${errors.global}"/></div></c:if>
      <div class="row g-3">
        <div class="col-md-6"><label class="form-label" for="manager-email">Email</label><input class="form-control bg-light" id="manager-email" type="email" value="<c:out value='${profile.email()}'/>" disabled/></div>
        <div class="col-md-6"><label class="form-label" for="manager-department">Phòng ban</label><input class="form-control bg-light" id="manager-department" value="<c:out value='${profile.departmentName()}'/>" disabled/></div>
        <div class="col-md-6"><label class="form-label required" for="manager-name">Họ và tên</label><input class="form-control ${not empty errors.fullName ? 'is-invalid' : ''}" id="manager-name" name="fullName" value="<c:out value='${form.fullName}'/>" minlength="2" maxlength="100" required/><qa:formError field="fullName"/></div>
        <div class="col-md-6"><label class="form-label" for="manager-phone">Số điện thoại</label><input class="form-control ${not empty errors.phone ? 'is-invalid' : ''}" id="manager-phone" name="phone" type="tel" value="<c:out value='${form.phone}'/>" maxlength="20" placeholder="0xxxxxxxxx hoặc +84xxxxxxxxx"/><qa:formError field="phone"/></div>
        <div class="col-md-6"><label class="form-label" for="manager-job">Chức danh</label><input class="form-control ${not empty errors.jobTitle ? 'is-invalid' : ''}" id="manager-job" name="jobTitle" value="<c:out value='${form.jobTitle}'/>" maxlength="100"/><qa:formError field="jobTitle"/></div>
        <div class="col-12"><label class="form-label" for="manager-bio">Giới thiệu</label><textarea class="form-control ${not empty errors.bio ? 'is-invalid' : ''}" id="manager-bio" name="bio" rows="5" maxlength="500"><c:out value="${form.bio}"/></textarea><qa:formError field="bio"/></div>
      </div>
    </div><div class="card-footer text-end"><button class="btn btn-primary" type="submit">Lưu thay đổi</button></div></div>
  </form></div>
</div>
<script src="${ctx}/assets/js/manager/profile.js" defer></script>
