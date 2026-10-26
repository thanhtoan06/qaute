<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Hồ sơ admin</title>
<div class="qa-page-admin-profile" data-qa-view="admin-profile">
<style>
/* ==========================================================================
   ADMIN PROFILE - MODERN DESIGN SYSTEM 2026
   ========================================================================== */
.qa-page-admin-profile {
  --adm-primary: #1e50c4;
  --adm-primary-rgb: 30, 80, 196;
  --adm-primary-dark: #0f2d7a;
  --adm-accent-purple: #7c3aed;
  --adm-accent-amber: #f59e0b;
  --adm-accent-rose: #ef4444;
  --adm-accent-emerald: #10b981;
  --adm-accent-cyan: #06b6d4;
  --adm-card-bg: linear-gradient(180deg, rgba(255, 255, 255, 0.99) 0%, rgba(248, 251, 255, 0.96) 100%);
  --adm-border-soft: rgba(175, 202, 236, 0.45);
  --adm-border-hover: rgba(30, 80, 196, 0.35);
  --adm-shadow-subtle: 0 10px 30px rgba(15, 45, 84, 0.05);
  --adm-shadow-card: 0 18px 40px rgba(15, 45, 84, 0.08);
  --adm-shadow-hover: 0 26px 54px rgba(15, 45, 84, 0.14);
  --adm-text-main: #142f50;
  --adm-text-muted: #5e7696;
  min-width: 0;
  color: var(--adm-text-main);
  padding: clamp(1.25rem, 2.5vw, 2.25rem) 0 4rem;
}

/* ---------- Unified Hero Banner ---------- */
.qa-page-admin-profile .qa-profile-hero {
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
    radial-gradient(circle at 92% 10%, rgba(124, 58, 237, 0.35), transparent 30%),
    radial-gradient(circle at 5% 95%, rgba(6, 182, 212, 0.28), transparent 36%),
    linear-gradient(135deg, #071733 0%, #133b7e 48%, #581c87 100%);
  box-shadow: 0 24px 60px rgba(12, 43, 85, 0.26), inset 0 1px 0 rgba(255, 255, 255, 0.2);
  animation: qaAdmRise 0.55s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.qa-page-admin-profile .qa-profile-hero::before {
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

.qa-page-admin-profile .qa-profile-hero-content {
  position: relative;
  z-index: 1;
  max-width: 720px;
}

.qa-page-admin-profile .qa-dashboard-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 0.55rem;
  margin-bottom: 0.85rem;
  padding: 0.45rem 1rem;
  border: 1px solid rgba(255, 255, 255, 0.24);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  color: #e2e8ff;
  font-size: 0.74rem;
  font-weight: 850;
  letter-spacing: 0.16em;
  text-transform: uppercase;
  backdrop-filter: blur(12px);
}

.qa-page-admin-profile .qa-pulse-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: #a78bfa;
  box-shadow: 0 0 0 0 rgba(167, 139, 250, 0.7);
  animation: qaAdmPulse 2s infinite;
}

.qa-page-admin-profile .qa-view-title {
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

.qa-page-admin-profile .qa-view-title-icon {
  display: inline-grid;
  flex: 0 0 48px;
  width: 48px;
  height: 48px;
  place-items: center;
  border-radius: 16px;
  color: #12377a;
  background: linear-gradient(145deg, #ffffff, #ede9fe);
  box-shadow: 0 10px 22px rgba(3, 20, 48, 0.26);
  font-size: 1.4rem;
}

.qa-page-admin-profile .qa-profile-hero p {
  margin: 0;
  color: #dbe4ff;
  font-size: 0.98rem;
  line-height: 1.7;
}

.qa-page-admin-profile .qa-profile-hero-actions {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

/* ---------- Card & Form Styling ---------- */
.qa-page-admin-profile .card {
  position: relative;
  overflow: hidden;
  border: 1px solid var(--adm-border-soft);
  border-radius: 28px;
  background: var(--adm-card-bg);
  box-shadow: var(--adm-shadow-subtle);
  transition: transform 0.24s ease, box-shadow 0.24s ease, border-color 0.24s ease;
}

.qa-page-admin-profile .card:hover {
  border-color: var(--adm-border-hover);
  box-shadow: var(--adm-shadow-card);
}

.qa-page-admin-profile .card-header {
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

.qa-page-admin-profile .card-title {
  margin: 0;
  color: #123158;
  font-size: 1.08rem;
  font-weight: 850;
  letter-spacing: -0.02em;
}

.qa-page-admin-profile .card-body {
  padding: 1.45rem;
}

.qa-page-admin-profile .card-footer {
  padding: 1.2rem 1.45rem;
  border-top: 1px solid rgba(215, 230, 248, 0.7);
  border-radius: 0 0 28px 28px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.6) 0%, rgba(244, 248, 255, 0.9) 100%);
}

/* ---------- Avatar Stage Card ---------- */
.qa-page-admin-profile .qa-avatar-card {
  text-align: center;
  padding: 2rem 1.4rem;
}

.qa-page-admin-profile .qa-avatar-halo {
  position: relative;
  display: inline-block;
  padding: 8px;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(30, 80, 196, 0.25) 0%, rgba(124, 58, 237, 0.2) 100%);
  box-shadow: 0 12px 30px rgba(30, 80, 196, 0.16);
  margin-bottom: 1.25rem;
  transition: transform 0.28s cubic-bezier(0.16, 1, 0.3, 1), box-shadow 0.28s ease;
}

.qa-page-admin-profile .qa-avatar-halo:hover {
  transform: scale(1.03);
  box-shadow: 0 16px 38px rgba(30, 80, 196, 0.24);
}

.qa-page-admin-profile .qa-avatar-badge {
  position: absolute;
  bottom: 8px;
  right: 8px;
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1e50c4 0%, #7c3aed 100%);
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px solid #ffffff;
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.15);
  font-size: 1.05rem;
}

.qa-page-admin-profile .qa-identity-chips {
  display: flex;
  flex-direction: column;
  gap: 0.65rem;
  margin-top: 1.5rem;
  text-align: left;
}

.qa-page-admin-profile .qa-chip-item {
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

.qa-page-admin-profile .qa-chip-item .bi {
  font-size: 1.05rem;
  color: #1e50c4;
}

/* ---------- Form Inputs & Hints ---------- */
.qa-page-admin-profile .form-label {
  color: #1a385c;
  font-size: 0.82rem;
  font-weight: 800;
  letter-spacing: -0.01em;
  margin-bottom: 0.45rem;
}

.qa-page-admin-profile .form-label.required::after {
  content: " *";
  color: #ef4444;
}

.qa-page-admin-profile .form-control,
.qa-page-admin-profile .form-select {
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

.qa-page-admin-profile .form-control:focus,
.qa-page-admin-profile .form-select:focus {
  border-color: #1e50c4;
  box-shadow: 0 0 0 4px rgba(30, 80, 196, 0.12);
}

.qa-page-admin-profile .form-hint {
  display: block;
  margin-top: 0.4rem;
  color: #728aa8;
  font-size: 0.76rem;
  font-weight: 550;
}

/* ---------- Buttons ---------- */
.qa-page-admin-profile .btn {
  border-radius: 14px;
  font-weight: 750;
  padding: 0.68rem 1.35rem;
  transition: all 0.22s cubic-bezier(0.16, 1, 0.3, 1);
}

.qa-page-admin-profile .btn-primary {
  border-color: #1e50c4;
  background: linear-gradient(135deg, #1e50c4 0%, #10348a 100%);
  box-shadow: 0 10px 22px rgba(30, 80, 196, 0.28);
  color: #ffffff;
}

.qa-page-admin-profile .btn-primary:hover {
  background: linear-gradient(135deg, #2460e6 0%, #123e9e 100%);
  box-shadow: 0 14px 28px rgba(30, 80, 196, 0.38);
  transform: translateY(-2px);
  color: #ffffff;
}

.qa-page-admin-profile .btn-outline-primary {
  border-color: rgba(30, 80, 196, 0.35);
  color: #1e50c4;
  background: #ffffff;
}

.qa-page-admin-profile .btn-outline-primary:hover {
  border-color: #1e50c4;
  background: linear-gradient(135deg, #1e50c4 0%, #10348a 100%);
  color: #ffffff;
  box-shadow: 0 8px 20px rgba(30, 80, 196, 0.25);
  transform: translateY(-2px);
}

.qa-page-admin-profile .btn-outline-light {
  border-color: rgba(255, 255, 255, 0.38);
  background: rgba(255, 255, 255, 0.12);
  color: #ffffff;
  backdrop-filter: blur(12px);
}

.qa-page-admin-profile .btn-outline-light:hover {
  color: #ffffff;
  border-color: rgba(255, 255, 255, 0.65);
  background: rgba(255, 255, 255, 0.22);
  transform: translateY(-2px);
}

/* ---------- Keyframes ---------- */
@keyframes qaAdmPulse {
  0% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(167, 139, 250, 0.7); }
  70% { transform: scale(1); box-shadow: 0 0 0 7px rgba(167, 139, 250, 0); }
  100% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(167, 139, 250, 0); }
}

@keyframes qaAdmRise {
  from { opacity: 0; transform: translateY(14px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 767.98px) {
  .qa-page-admin-profile .qa-profile-hero {
    flex-direction: column;
    align-items: flex-start;
    padding: 1.45rem;
    border-radius: 24px;
  }
}

@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-profile *,
  .qa-page-admin-profile *::before,
  .qa-page-admin-profile *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
    scroll-behavior: auto !important;
  }
}
</style>

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
              <img id="avatarPreview" src="<c:out value='${profile.avatarUrl()}'/>" alt="Ảnh đại diện" class="avatar avatar-2xl rounded-circle object-cover border shadow-sm" style="width:140px;height:140px;object-fit:cover"/>
            </c:when>
            <c:otherwise>
              <div id="avatarPreviewPlaceholder" class="avatar avatar-2xl rounded-circle bg-primary-lt d-flex align-items-center justify-content-center border shadow-sm mx-auto" style="width:140px;height:140px">
                <i class="bi bi-shield-fill-check fs-1 text-primary"></i>
              </div>
              <img id="avatarPreview" alt="Ảnh đại diện" class="avatar avatar-2xl rounded-circle object-cover border shadow-sm d-none" style="width:140px;height:140px;object-fit:cover"/>
            </c:otherwise>
          </c:choose>
          <span class="qa-avatar-badge" aria-hidden="true"><i class="bi bi-camera-fill"></i></span>
          <div id="avatarSpinner" class="position-absolute top-50 start-50 translate-middle d-none bg-dark bg-opacity-50 rounded-circle" style="width:140px;height:140px;display:flex;align-items:center;justify-content:center" aria-live="polite">
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
          <span class="qa-view-title-icon" style="width: 38px; height: 38px; font-size: 1.15rem;" aria-hidden="true"><i class="bi bi-person-lines-fill"></i></span>
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
