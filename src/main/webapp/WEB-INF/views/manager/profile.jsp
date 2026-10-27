<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Hồ sơ Manager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/manager/profile.css">
</head>
<div class="qa-page-manager-profile" data-qa-view="manager-profile">


<%-- ==================== HERO BANNER ==================== --%>
<section class="qa-profile-hero" aria-labelledby="manager-profile-title">
  <div class="qa-profile-hero-content">
    <span class="qa-dashboard-eyebrow">
      <span class="qa-pulse-dot" aria-hidden="true"></span>
      <i class="bi bi-person-workspace me-1" aria-hidden="true"></i>Không gian tư vấn viên
    </span>
    <h1 id="manager-profile-title" class="qa-view-title" data-qa-page="manager-profile">
      <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-person-badge-fill"></i></span>
      Hồ sơ chuyên viên tư vấn
    </h1>
    <p>Cập nhật thông tin liên hệ công tác, phòng ban phụ trách, trạng thái trực tuyến và ảnh đại diện phục vụ hỗ trợ sinh viên.</p>
  </div>
  <div class="qa-profile-hero-actions">
    <a class="btn btn-outline-light" href="${ctx}/manager/profile/security">
      <i class="bi bi-shield-lock-fill me-1"></i> Bảo mật tài khoản
    </a>
  </div>
</section>

<%-- ==================== PROFILE CONTENT ==================== --%>
<div class="row g-4">
  <%-- Cột bên trái: Ảnh đại diện & Trạng thái --%>
  <div class="col-12 col-md-4 col-lg-3">
    <div class="card qa-avatar-card">
      <div class="card-body p-0">
        <div class="qa-avatar-halo position-relative d-inline-block">
          <c:choose>
            <c:when test="${not empty profile.avatarUrl()}">
              <img id="avatarPreview" src="<c:out value='${profile.avatarUrl()}'/>" alt="Ảnh đại diện" class="avatar avatar-2xl rounded-circle object-cover border shadow-sm u-width-140px u-height-140px u-object-fit-cover"/>
            </c:when>
            <c:otherwise>
              <div id="avatarPreviewPlaceholder" class="avatar avatar-2xl rounded-circle bg-primary-lt d-flex align-items-center justify-content-center border shadow-sm mx-auto u-width-140px u-height-140px">
                <i class="bi bi-person-fill fs-1 text-primary"></i>
              </div>
              <img id="avatarPreview" src="" alt="Ảnh đại diện" class="avatar avatar-2xl rounded-circle object-cover border shadow-sm d-none u-width-140px u-height-140px u-object-fit-cover"/>
            </c:otherwise>
          </c:choose>
          <span class="qa-avatar-badge" aria-hidden="true"><i class="bi bi-camera-fill"></i></span>
          <div id="avatarSpinner" class="position-absolute top-50 start-50 translate-middle d-none bg-dark bg-opacity-50 rounded-circle u-width-140px u-height-140px u-display-flex u-align-items-center u-justify-content-center">
            <div class="spinner-border text-white" role="status"></div>
          </div>
        </div>

        <h3 class="mb-1 fw-bold"><c:out value="${profile.fullName()}"/></h3>
        <div class="badge bg-primary-lt font-monospace px-3 py-1 mb-2"><c:out value="${profile.employeeCode()}" default="Manager"/></div>
        <div>
          <span class="badge ${profile.availabilityStatus() eq 'ONLINE' ? 'bg-green-lt' : profile.availabilityStatus() eq 'BUSY' ? 'bg-yellow-lt' : 'bg-secondary-lt'} px-3 py-1 mb-3">
            <c:choose>
              <c:when test="${profile.availabilityStatus() eq 'ONLINE'}"><i class="bi bi-broadcast me-1"></i>Trực tuyến</c:when>
              <c:when test="${profile.availabilityStatus() eq 'BUSY'}"><i class="bi bi-clock-history me-1"></i>Bận</c:when>
              <c:otherwise><i class="bi bi-moon-stars me-1"></i>Ngoại tuyến</c:otherwise>
            </c:choose>
          </span>
        </div>

        <button type="button" class="btn btn-outline-primary btn-sm w-100 shadow-sm" id="btnChangeAvatar">
          <i class="bi bi-camera-fill me-1"></i> Đổi ảnh đại diện
        </button>
        <input type="file" id="avatarFileInput" accept="image/png,image/jpeg,image/webp,image/gif" class="d-none"/>
        <form id="avatarForm" method="post" action="${ctx}/manager/profile/avatar" data-upload-url="${ctx}/api/media/upload" class="d-none">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <input type="hidden" name="assetId" id="avatarAssetIdInput"/>
        </form>
        <div id="avatarError" class="alert alert-danger small mt-2 py-1 px-2 d-none" role="alert"></div>

        <div class="qa-identity-chips">
          <div class="qa-chip-item">
            <i class="bi bi-building"></i>
            <div>
              <div class="fw-bold">Phòng ban</div>
              <div class="text-secondary small"><c:out value="${profile.departmentName()}" default="Tư vấn viên"/></div>
            </div>
          </div>
          <div class="qa-chip-item">
            <i class="bi bi-briefcase"></i>
            <div>
              <div class="fw-bold">Chức danh</div>
              <div class="text-secondary small"><c:out value="${profile.jobTitle()}" default="Chuyên viên hỗ trợ"/></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <%-- Cột bên phải: Biểu mẫu thông tin cá nhân --%>
  <div class="col-12 col-md-8 col-lg-9">
    <form method="post" action="${ctx}/manager/profile" novalidate>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div class="card">
        <div class="card-header">
          <div class="d-flex align-items-center gap-2">
            <span class="qa-view-title-icon u-width-38px u-height-38px u-text-size-1-15rem" aria-hidden="true"><i class="bi bi-person-lines-fill"></i></span>
            <h3 class="card-title">Thông tin cá nhân & công tác</h3>
          </div>
          <span class="badge bg-teal-lt"><i class="bi bi-check2-circle me-1"></i>Đã phân quyền</span>
        </div>
        <div class="card-body">
          <c:if test="${not empty errors.global}">
            <div class="alert alert-danger mb-3" role="alert"><c:out value="${errors.global}"/></div>
          </c:if>
          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label" for="manager-email"><i class="bi bi-envelope-fill me-1 text-primary"></i>Địa chỉ Email</label>
              <input class="form-control bg-light" id="manager-email" type="email" value="<c:out value='${profile.email()}'/>" disabled/>
              <small class="form-hint text-secondary"><i class="bi bi-lock-fill me-1"></i>Email đăng nhập do hệ thống cấp</small>
            </div>
            <div class="col-md-6">
              <label class="form-label" for="manager-department"><i class="bi bi-building me-1 text-primary"></i>Phòng ban trực thuộc</label>
              <input class="form-control bg-light" id="manager-department" value="<c:out value='${profile.departmentName()}'/>" disabled/>
              <small class="form-hint text-secondary"><i class="bi bi-lock-fill me-1"></i>Phòng ban do quản trị viên phân công</small>
            </div>
            <div class="col-md-6">
              <label class="form-label required" for="manager-name"><i class="bi bi-person-fill me-1 text-primary"></i>Họ và tên</label>
              <input class="form-control ${not empty errors.fullName ? 'is-invalid' : ''}" id="manager-name" name="fullName" value="<c:out value='${form.fullName}'/>" minlength="2" maxlength="100" required/>
              <qa:formError field="fullName"/>
            </div>
            <div class="col-md-6">
              <label class="form-label" for="manager-phone"><i class="bi bi-telephone-fill me-1 text-primary"></i>Số điện thoại</label>
              <input class="form-control ${not empty errors.phone ? 'is-invalid' : ''}" id="manager-phone" name="phone" type="tel" value="<c:out value='${form.phone}'/>" maxlength="20" placeholder="0xxxxxxxxx hoặc +84xxxxxxxxx"/>
              <qa:formError field="phone"/>
            </div>
            <div class="col-md-6">
              <label class="form-label" for="manager-job"><i class="bi bi-briefcase-fill me-1 text-primary"></i>Chức danh</label>
              <input class="form-control ${not empty errors.jobTitle ? 'is-invalid' : ''}" id="manager-job" name="jobTitle" value="<c:out value='${form.jobTitle}'/>" maxlength="100" placeholder="Ví dụ: Chuyên viên tư vấn học vụ"/>
              <qa:formError field="jobTitle"/>
            </div>
            <div class="col-12">
              <label class="form-label" for="manager-bio"><i class="bi bi-card-text me-1 text-primary"></i>Giới thiệu bản thân & Lĩnh vực hỗ trợ</label>
              <textarea class="form-control ${not empty errors.bio ? 'is-invalid' : ''}" id="manager-bio" name="bio" rows="4" maxlength="500" placeholder="Mô tả ngắn gọn về kinh nghiệm, lĩnh vực chuyên môn phụ trách để sinh viên nắm bắt..."><c:out value="${form.bio}"/></textarea>
              <qa:formError field="bio"/>
            </div>
          </div>
        </div>
        <div class="card-footer text-end">
          <button class="btn btn-primary shadow-sm" type="submit">
            <i class="bi bi-check2-circle me-1"></i> Lưu thay đổi
          </button>
        </div>
      </div>
    </form>
  </div>
</div>
<script src="${ctx}/assets/js/manager/profile.js" defer></script>
</div>
