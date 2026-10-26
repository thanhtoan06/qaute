<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Hồ sơ Manager</title>
<div class="qa-page-manager-profile" data-qa-view="manager-profile">
<style>
/* ==========================================================================
   MANAGER PROFILE - MODERN DESIGN SYSTEM 2026
   ========================================================================== */
.qa-page-manager-profile {
  --mgr-primary: #1d60d8;
  --mgr-primary-rgb: 29, 96, 216;
  --mgr-primary-dark: #0f3d8a;
  --mgr-teal: #0d9488;
  --mgr-accent-purple: #8b5cf6;
  --mgr-accent-amber: #f59e0b;
  --mgr-card-bg: linear-gradient(180deg, rgba(255, 255, 255, 0.99) 0%, rgba(248, 251, 255, 0.96) 100%);
  --mgr-border-soft: rgba(180, 206, 238, 0.45);
  --mgr-border-hover: rgba(29, 96, 216, 0.35);
  --mgr-shadow-subtle: 0 10px 30px rgba(15, 45, 84, 0.05);
  --mgr-shadow-card: 0 18px 40px rgba(15, 45, 84, 0.08);
  --mgr-shadow-hover: 0 26px 54px rgba(15, 45, 84, 0.14);
  --mgr-text-main: #163456;
  --mgr-text-muted: #5e7696;
  min-width: 0;
  color: var(--mgr-text-main);
  padding: clamp(1.25rem, 2.5vw, 2.25rem) 0 4rem;
}

/* ---------- Unified Hero Banner ---------- */
.qa-page-manager-profile .qa-profile-hero {
  position: relative;
  isolation: isolate;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1.5rem;
  margin-bottom: 1.75rem;
  padding: clamp(1.6rem, 2.8vw, 2.35rem);
  border: 1px solid rgba(255, 255, 255, 0.32);
  border-radius: 30px;
  color: #ffffff;
  background:
    radial-gradient(circle at 92% 10%, rgba(13, 148, 136, 0.32), transparent 30%),
    radial-gradient(circle at 5% 95%, rgba(139, 92, 246, 0.28), transparent 36%),
    linear-gradient(135deg, #0a1b38 0%, #12458f 48%, #0f766e 100%);
  box-shadow: 0 24px 60px rgba(12, 43, 85, 0.26), inset 0 1px 0 rgba(255, 255, 255, 0.2);
  animation: qaMgrRise 0.55s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.qa-page-manager-profile .qa-profile-hero::before {
  content: "";
  position: absolute;
  z-index: -1;
  top: -120px;
  right: -40px;
  width: 320px;
  height: 320px;
  border-radius: 50%;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 0 0 24px rgba(255, 255, 255, 0.03), 0 0 0 60px rgba(255, 255, 255, 0.015);
  pointer-events: none;
}

.qa-page-manager-profile .qa-profile-hero-content {
  position: relative;
  z-index: 1;
  max-width: 720px;
}

.qa-page-manager-profile .qa-dashboard-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 0.55rem;
  margin-bottom: 0.85rem;
  padding: 0.45rem 1rem;
  border: 1px solid rgba(255, 255, 255, 0.24);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  color: #d8eeff;
  font-size: 0.74rem;
  font-weight: 850;
  letter-spacing: 0.16em;
  text-transform: uppercase;
  backdrop-filter: blur(12px);
}

.qa-page-manager-profile .qa-pulse-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: #2dd4bf;
  box-shadow: 0 0 0 0 rgba(45, 212, 191, 0.7);
  animation: qaMgrPulse 2s infinite;
}

.qa-page-manager-profile .qa-view-title {
  display: inline-flex;
  align-items: center;
  gap: 0.85rem;
  margin: 0 0 0.65rem;
  color: #ffffff;
  font-size: clamp(1.75rem, 2.8vw, 2.5rem);
  font-weight: 850;
  letter-spacing: -0.04em;
  line-height: 1.2;
}

.qa-page-manager-profile .qa-view-title-icon {
  display: inline-grid;
  flex: 0 0 48px;
  width: 48px;
  height: 48px;
  place-items: center;
  border-radius: 16px;
  color: #0c4384;
  background: linear-gradient(145deg, #ffffff, #dff6f5);
  box-shadow: 0 10px 22px rgba(3, 20, 48, 0.26);
  font-size: 1.4rem;
}

.qa-page-manager-profile .qa-profile-hero p {
  margin: 0;
  color: #d7eaff;
  font-size: 0.98rem;
  line-height: 1.7;
}

.qa-page-manager-profile .qa-profile-hero-actions {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

/* ---------- Card & Form Styling ---------- */
.qa-page-manager-profile .card {
  position: relative;
  overflow: hidden;
  border: 1px solid var(--mgr-border-soft);
  border-radius: 28px;
  background: var(--mgr-card-bg);
  box-shadow: var(--mgr-shadow-subtle);
  transition: transform 0.24s ease, box-shadow 0.24s ease, border-color 0.24s ease;
}

.qa-page-manager-profile .card:hover {
  border-color: var(--mgr-border-hover);
  box-shadow: var(--mgr-shadow-card);
}

.qa-page-manager-profile .card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.85rem;
  min-height: 76px;
  padding: 1.2rem 1.45rem;
  border-bottom: 1px solid rgba(215, 230, 248, 0.7);
  border-radius: 28px 28px 0 0;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98) 0%, rgba(240, 246, 255, 0.9) 100%);
}

.qa-page-manager-profile .card-title {
  margin: 0;
  color: #12345e;
  font-size: 1.08rem;
  font-weight: 850;
  letter-spacing: -0.02em;
}

.qa-page-manager-profile .card-body {
  padding: 1.45rem;
}

.qa-page-manager-profile .card-footer {
  padding: 1.2rem 1.45rem;
  border-top: 1px solid rgba(215, 230, 248, 0.7);
  border-radius: 0 0 28px 28px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.6) 0%, rgba(244, 248, 255, 0.9) 100%);
}

/* ---------- Avatar Stage Card ---------- */
.qa-page-manager-profile .qa-avatar-card {
  text-align: center;
  padding: 2rem 1.4rem;
}

.qa-page-manager-profile .qa-avatar-halo {
  position: relative;
  display: inline-block;
  padding: 8px;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(29, 96, 216, 0.25) 0%, rgba(13, 148, 136, 0.2) 100%);
  box-shadow: 0 12px 30px rgba(29, 96, 216, 0.16);
  margin-bottom: 1.25rem;
  transition: transform 0.28s cubic-bezier(0.16, 1, 0.3, 1), box-shadow 0.28s ease;
}

.qa-page-manager-profile .qa-avatar-halo:hover {
  transform: scale(1.03);
  box-shadow: 0 16px 38px rgba(29, 96, 216, 0.24);
}

.qa-page-manager-profile .qa-avatar-badge {
  position: absolute;
  bottom: 8px;
  right: 8px;
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1d60d8 0%, #0d428d 100%);
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px solid #ffffff;
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.15);
  font-size: 1.05rem;
}

.qa-page-manager-profile .qa-identity-chips {
  display: flex;
  flex-direction: column;
  gap: 0.65rem;
  margin-top: 1.5rem;
  text-align: left;
}

.qa-page-manager-profile .qa-chip-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 0.95rem;
  border: 1px solid rgba(215, 230, 248, 0.75);
  border-radius: 16px;
  background: rgba(245, 250, 255, 0.85);
  font-size: 0.82rem;
  color: #274567;
}

.qa-page-manager-profile .qa-chip-item .bi {
  font-size: 1.05rem;
  color: #1d60d8;
}

/* ---------- Form Inputs & Hints ---------- */
.qa-page-manager-profile .form-label {
  color: #1a385c;
  font-size: 0.82rem;
  font-weight: 800;
  letter-spacing: -0.01em;
  margin-bottom: 0.45rem;
}

.qa-page-manager-profile .form-label.required::after {
  content: " *";
  color: #ef4444;
}

.qa-page-manager-profile .form-control,
.qa-page-manager-profile .form-select {
  min-height: 46px;
  border: 1px solid rgba(188, 212, 242, 0.7);
  border-radius: 14px;
  color: #16365b;
  font-size: 0.92rem;
  font-weight: 600;
  background-color: #ffffff;
  box-shadow: 0 2px 6px rgba(15, 45, 84, 0.02);
  transition: all 0.2s ease;
  padding: 0.65rem 1rem;
}

.qa-page-manager-profile textarea.form-control {
  min-height: 120px;
  line-height: 1.65;
}

.qa-page-manager-profile .form-control:focus,
.qa-page-manager-profile .form-select:focus {
  border-color: #1d60d8;
  box-shadow: 0 0 0 4px rgba(29, 96, 216, 0.12);
}

.qa-page-manager-profile .form-control.bg-light,
.qa-page-manager-profile .form-control:disabled,
.qa-page-manager-profile .form-control[readonly] {
  background-color: #f3f7fd !important;
  color: #5a7494 !important;
  border-color: rgba(200, 218, 242, 0.6);
  cursor: not-allowed;
}

/* ---------- Buttons ---------- */
.qa-page-manager-profile .btn {
  border-radius: 14px;
  font-weight: 750;
  padding: 0.68rem 1.35rem;
  transition: all 0.22s cubic-bezier(0.16, 1, 0.3, 1);
}

.qa-page-manager-profile .btn-primary {
  border-color: #1d60d8;
  background: linear-gradient(135deg, #1d60d8 0%, #0d428d 100%);
  box-shadow: 0 10px 22px rgba(29, 96, 216, 0.28);
  color: #ffffff;
}

.qa-page-manager-profile .btn-primary:hover {
  background: linear-gradient(135deg, #246ff0 0%, #0f4bb0 100%);
  box-shadow: 0 14px 28px rgba(29, 96, 216, 0.38);
  transform: translateY(-2px);
  color: #ffffff;
}

.qa-page-manager-profile .btn-outline-primary {
  border-color: rgba(29, 96, 216, 0.35);
  color: #1d60d8;
  background: #ffffff;
}

.qa-page-manager-profile .btn-outline-primary:hover {
  border-color: #1d60d8;
  background: linear-gradient(135deg, #1d60d8 0%, #0d428d 100%);
  color: #ffffff;
  box-shadow: 0 8px 20px rgba(29, 96, 216, 0.25);
  transform: translateY(-2px);
}

.qa-page-manager-profile .btn-outline-light {
  border-color: rgba(255, 255, 255, 0.38);
  background: rgba(255, 255, 255, 0.12);
  color: #ffffff;
  backdrop-filter: blur(12px);
}

.qa-page-manager-profile .btn-outline-light:hover {
  color: #ffffff;
  border-color: rgba(255, 255, 255, 0.65);
  background: rgba(255, 255, 255, 0.22);
  transform: translateY(-2px);
}

/* ---------- Keyframes ---------- */
@keyframes qaMgrPulse {
  0% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(45, 212, 191, 0.7); }
  70% { transform: scale(1); box-shadow: 0 0 0 7px rgba(45, 212, 191, 0); }
  100% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(45, 212, 191, 0); }
}

@keyframes qaMgrRise {
  from { opacity: 0; transform: translateY(14px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 767.98px) {
  .qa-page-manager-profile .qa-profile-hero {
    flex-direction: column;
    align-items: flex-start;
    padding: 1.45rem;
    border-radius: 24px;
  }
}

@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-profile *,
  .qa-page-manager-profile *::before,
  .qa-page-manager-profile *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
    scroll-behavior: auto !important;
  }
}
</style>

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
              <img id="avatarPreview" src="<c:out value='${profile.avatarUrl()}'/>" alt="Ảnh đại diện" class="avatar avatar-2xl rounded-circle object-cover border shadow-sm" style="width:140px;height:140px;object-fit:cover"/>
            </c:when>
            <c:otherwise>
              <div id="avatarPreviewPlaceholder" class="avatar avatar-2xl rounded-circle bg-primary-lt d-flex align-items-center justify-content-center border shadow-sm mx-auto" style="width:140px;height:140px">
                <i class="bi bi-person-fill fs-1 text-primary"></i>
              </div>
              <img id="avatarPreview" src="" alt="Ảnh đại diện" class="avatar avatar-2xl rounded-circle object-cover border shadow-sm d-none" style="width:140px;height:140px;object-fit:cover"/>
            </c:otherwise>
          </c:choose>
          <span class="qa-avatar-badge" aria-hidden="true"><i class="bi bi-camera-fill"></i></span>
          <div id="avatarSpinner" class="position-absolute top-50 start-50 translate-middle d-none bg-dark bg-opacity-50 rounded-circle" style="width:140px;height:140px;display:flex;align-items:center;justify-content:center">
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
            <span class="qa-view-title-icon" style="width: 38px; height: 38px; font-size: 1.15rem;" aria-hidden="true"><i class="bi bi-person-lines-fill"></i></span>
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
