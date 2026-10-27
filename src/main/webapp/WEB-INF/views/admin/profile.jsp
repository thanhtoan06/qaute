<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Hồ sơ admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/profile.css">
</head>
<div class="qa-page-admin-profile" data-qa-view="admin-profile">


<%-- ==================== HERO BANNER ==================== --%>
<section class="qa-profile-hero" aria-labelledby="admin-profile-title">
  <div class="qa-profile-hero-content">
    <span class="qa-dashboard-eyebrow">
      <span class="qa-pulse-dot" aria-hidden="true"></span>
      <i class="bi bi-shield-lock-fill me-1" aria-hidden="true"></i>Không gian quản trị
    </span>
    <h1 id="admin-profile-title" class="qa-view-title" data-qa-page="admin-profile">
      <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-shield-shaded"></i></span>
      Hồ sơ quản trị viên
    </h1>
    <p>Quản lý thông tin liên hệ và hồ sơ cá nhân của tài khoản quản trị viên QAUTE.</p>
  </div>
  <div class="qa-profile-hero-actions">
    <a href="${ctx}/admin/profile/security" class="btn btn-outline-light">
      <i class="bi bi-shield-lock-fill me-1"></i> Bảo mật tài khoản
    </a>
    <a href="${ctx}/admin/dashboard" class="btn btn-outline-light">
      <i class="bi bi-arrow-left me-1"></i> Quay lại Dashboard
    </a>
  </div>
</section>

<%-- ==================== PROFILE CONTENT ==================== --%>
<div class="row g-4">
  <%-- Cột bên trái: Ảnh đại diện & Nhận diện vai trò --%>
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
                <i class="bi bi-shield-fill-check fs-1 text-primary"></i>
              </div>
              <img id="avatarPreview" alt="Ảnh đại diện" class="avatar avatar-2xl rounded-circle object-cover border shadow-sm d-none u-width-140px u-height-140px u-object-fit-cover"/>
            </c:otherwise>
          </c:choose>
          <span class="qa-avatar-badge" aria-hidden="true"><i class="bi bi-camera-fill"></i></span>
          <div id="avatarSpinner" class="position-absolute top-50 start-50 translate-middle d-none bg-dark bg-opacity-50 rounded-circle u-width-140px u-height-140px u-display-flex u-align-items-center u-justify-content-center" aria-live="polite">
            <div class="spinner-border text-white" role="status"><span class="visually-hidden">Đang tải ảnh</span></div>
          </div>
        </div>

        <h3 class="mb-1 fw-bold"><c:out value="${profile.fullName()}"/></h3>
        <span class="badge bg-purple-lt px-3 py-1 mb-3"><i class="bi bi-shield-check me-1"></i>Quản trị viên toàn quyền</span>

        <button type="button" class="btn btn-outline-primary btn-sm w-100 shadow-sm" id="btnChangeAvatar">
          <i class="bi bi-camera-fill me-1"></i> Đổi ảnh đại diện
        </button>
        <input type="file" id="avatarFileInput" accept="image/png,image/jpeg,image/webp,image/gif" class="d-none"/>
        <form id="avatarForm" method="post" action="${ctx}/admin/profile/avatar" data-upload-url="${ctx}/api/media/upload" class="d-none">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <input type="hidden" name="assetId" id="avatarAssetIdInput"/>
        </form>
        <div id="avatarError" class="alert alert-danger small mt-2 py-1 px-2 d-none" role="alert"></div>
        <div class="text-secondary small mt-2">Ảnh được tự động căn vuông 400 × 400 khi tải lên.</div>

        <div class="qa-identity-chips">
          <div class="qa-chip-item">
            <i class="bi bi-key-fill text-purple"></i>
            <div>
              <div class="fw-bold">Quyền hạn tối cao</div>
              <div class="text-secondary small">Super Admin / Quản trị viên</div>
            </div>
          </div>
          <div class="qa-chip-item">
            <i class="bi bi-shield-lock-fill text-primary"></i>
            <div>
              <div class="fw-bold">Bảo mật hệ thống</div>
              <div class="text-secondary small">Xác thực 2 yếu tố & Nhật ký</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <%-- Cột bên phải: Form thông tin cá nhân --%>
  <div class="col-12 col-md-8 col-lg-9">
    <!-- Form thông tin cá nhân -->
    <form method="post" action="${ctx}/admin/profile" class="card mb-4" novalidate>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div class="card-header">
        <div class="d-flex align-items-center gap-2">
          <span class="qa-view-title-icon u-width-38px u-height-38px u-text-size-1-15rem" aria-hidden="true"><i class="bi bi-person-lines-fill"></i></span>
          <h3 class="card-title">Thông tin quản trị viên</h3>
        </div>
        <span class="badge bg-azure-lt"><i class="bi bi-person-check me-1"></i>Hồ sơ trực tiếp</span>
      </div>
      <div class="card-body">
        <c:if test="${not empty errors.global}">
          <div class="alert alert-danger mb-3" role="alert"><c:out value="${errors.global}"/></div>
        </c:if>
        <div class="row g-3">
          <div class="col-12">
            <label class="form-label required" for="admin-name"><i class="bi bi-person-fill me-1 text-primary"></i>Họ và tên</label>
            <input class="form-control ${not empty errors.fullName ? 'is-invalid' : ''}" id="admin-name" name="fullName" autocomplete="name" value="<c:out value='${form.fullName}'/>" minlength="2" maxlength="100" required/>
            <qa:formError field="fullName"/>
          </div>
          <div class="col-md-6">
            <label class="form-label required" for="admin-email"><i class="bi bi-envelope-fill me-1 text-primary"></i>Địa chỉ Email</label>
            <input class="form-control ${not empty errors.email ? 'is-invalid' : ''}" id="admin-email" name="email" type="email" autocomplete="email" value="<c:out value='${form.email}'/>" maxlength="150" required/>
            <qa:formError field="email"/>
            <div class="form-hint"><i class="bi bi-info-circle me-1"></i>Dùng để nhận cảnh báo SLA và đăng nhập hệ thống.</div>
          </div>
          <div class="col-md-6">
            <label class="form-label" for="admin-phone"><i class="bi bi-telephone-fill me-1 text-primary"></i>Số điện thoại</label>
            <input class="form-control ${not empty errors.phone ? 'is-invalid' : ''}" id="admin-phone" name="phone" type="tel" autocomplete="tel" value="<c:out value='${form.phone}'/>" maxlength="20" placeholder="0xxxxxxxxx hoặc +84xxxxxxxxx"/>
            <qa:formError field="phone"/>
          </div>
        </div>
      </div>
      <div class="card-footer text-end">
        <button class="btn btn-primary shadow-sm" type="submit">
          <i class="bi bi-check2-circle me-1"></i> Lưu thông tin
        </button>
      </div>
    </form>
  </div>
</div>
<script src="${ctx}/assets/js/admin/profile.js" defer></script>
</div>
