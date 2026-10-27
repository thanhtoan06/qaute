<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Hồ sơ sinh viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/profile.css">
</head>
<div class="qa-page-student-profile" data-qa-view="student-profile">


<%-- ==================== HERO BANNER ==================== --%>
<section class="qa-profile-hero" aria-labelledby="student-profile-title">
  <div class="qa-profile-hero-content">
    <span class="qa-dashboard-eyebrow">
      <span class="qa-pulse-dot" aria-hidden="true"></span>
      <i class="bi bi-person-badge-fill me-1" aria-hidden="true"></i>Hồ sơ sinh viên
    </span>
    <h1 id="student-profile-title" class="qa-view-title" data-qa-page="student-profile">
      <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-mortarboard-fill"></i></span>
      Thông tin tài khoản sinh viên
    </h1>
    <p>Quản lý thông tin định danh sinh viên, khoa viện đào tạo và ảnh đại diện dùng cho toàn bộ hệ thống hỗ trợ học vụ QAUTE.</p>
  </div>
  <div class="qa-profile-hero-actions">
    <a href="${ctx}/student/profile/security" class="btn btn-outline-light">
      <i class="bi bi-shield-lock-fill me-1"></i> Bảo mật tài khoản
    </a>
    <a href="${ctx}/student/dashboard" class="btn btn-outline-light">
      <i class="bi bi-arrow-left me-1"></i> Quay lại Dashboard
    </a>
  </div>
</section>

<%-- ==================== PROFILE CONTENT ==================== --%>
<div class="row g-4">
    <%-- Cột bên trái: Ảnh đại diện & Tóm tắt nhận diện --%>
    <div class="col-12 col-md-4 col-lg-3">
        <div class="card qa-avatar-card">
            <div class="card-body p-0">
                <div class="qa-avatar-halo position-relative d-inline-block">
                    <c:choose>
                        <c:when test="${not empty profile.avatarUrl()}">
                            <img id="avatarPreview" src="<c:out value='${profile.avatarUrl()}'/>" alt="Avatar" class="avatar avatar-2xl rounded-circle object-cover border shadow-sm u-width-140px u-height-140px u-object-fit-cover"/>
                        </c:when>
                        <c:otherwise>
                            <div id="avatarPreviewPlaceholder" class="avatar avatar-2xl rounded-circle bg-primary-lt d-flex align-items-center justify-content-center border shadow-sm mx-auto u-width-140px u-height-140px">
                                <i class="bi bi-person-fill fs-1 text-primary"></i>
                            </div>
                            <img id="avatarPreview" src="" alt="Avatar" class="avatar avatar-2xl rounded-circle object-cover border shadow-sm d-none u-width-140px u-height-140px u-object-fit-cover"/>
                        </c:otherwise>
                    </c:choose>
                    <span class="qa-avatar-badge" aria-hidden="true"><i class="bi bi-camera-fill"></i></span>
                    <div id="avatarSpinner" class="position-absolute top-50 start-50 translate-middle d-none bg-dark bg-opacity-50 rounded-circle u-width-140px u-height-140px u-display-flex u-align-items-center u-justify-content-center">
                        <div class="spinner-border text-white" role="status"></div>
                    </div>
                </div>

                <h3 class="m-0 mb-1 fw-bold"><c:out value="${profile.fullName()}"/></h3>
                <div class="badge bg-primary-lt font-monospace px-3 py-1 mb-3"><c:out value="${profile.mssv()}"/></div>

                <button type="button" class="btn btn-outline-primary btn-sm w-100 shadow-sm" id="btnChangeAvatar">
                    <i class="bi bi-camera-fill me-1"></i> Đổi ảnh đại diện
                </button>
                <input type="file" id="avatarFileInput" accept="image/png,image/jpeg,image/webp,image/gif" class="d-none">
                <form id="avatarForm" method="post" action="/student/profile/avatar" class="d-none">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="hidden" name="assetId" id="avatarAssetIdInput"/>
                </form>
                <div id="avatarError" class="alert alert-danger small mt-2 py-1 px-2 d-none" role="alert"></div>

                <div class="qa-identity-chips">
                    <div class="qa-chip-item">
                        <i class="bi bi-shield-check"></i>
                        <div>
                            <div class="fw-bold">Tài khoản chính thức</div>
                            <div class="text-secondary small">Xác thực bởi HCMUTE</div>
                        </div>
                    </div>
                    <div class="qa-chip-item">
                        <i class="bi bi-mortarboard"></i>
                        <div>
                            <div class="fw-bold">Hệ đào tạo</div>
                            <div class="text-secondary small"><c:out value="${profile.program()}" default="Chính quy"/></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%-- Cột bên phải: Biểu mẫu thông tin sinh viên --%>
    <div class="col-12 col-md-8 col-lg-9">
        <form method="post" action="/student/profile" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="card">
                <div class="card-header">
                    <div class="d-flex align-items-center gap-2">
                        <span class="qa-view-title-icon u-width-38px u-height-38px u-text-size-1-15rem" aria-hidden="true"><i class="bi bi-person-lines-fill"></i></span>
                        <h3 class="card-title">Thông tin chi tiết sinh viên</h3>
                    </div>
                    <span class="badge bg-azure-lt"><i class="bi bi-check-circle me-1"></i>Hồ sơ học vụ</span>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errors['global']}">
                        <div class="alert alert-danger mb-3" role="alert"><c:out value="${errors['global']}"/></div>
                    </c:if>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label" for="mssv"><i class="bi bi-card-text me-1 text-primary"></i>Mã số sinh viên (MSSV)</label>
                            <input type="text" class="form-control bg-light font-monospace" id="mssv" value="<c:out value='${profile.mssv()}'/>" readonly disabled>
                            <small class="form-hint"><i class="bi bi-lock-fill me-1"></i>MSSV do nhà trường quản lý, không thể thay đổi</small>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="email"><i class="bi bi-envelope-fill me-1 text-primary"></i>Địa chỉ Email</label>
                            <input type="email" class="form-control bg-light" id="email" value="<c:out value='${profile.email()}'/>" readonly disabled>
                            <small class="form-hint"><i class="bi bi-lock-fill me-1"></i>Email tài khoản không thể chỉnh sửa</small>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label required" for="fullName"><i class="bi bi-person-fill me-1 text-primary"></i>Họ và tên</label>
                            <input type="text" class="form-control ${not empty errors['fullName'] ? 'is-invalid' : ''}" id="fullName" name="fullName" value="<c:out value='${form.fullName}'/>" minlength="2" maxlength="100" required>
                            <qa:formError field="fullName"/>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="phone"><i class="bi bi-telephone-fill me-1 text-primary"></i>Số điện thoại</label>
                            <input type="tel" class="form-control ${not empty errors['phone'] ? 'is-invalid' : ''}" id="phone" name="phone" value="<c:out value='${form.phone}'/>" placeholder="0xxxxxxxxx hoặc +84xxxxxxxxx">
                            <qa:formError field="phone"/>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="facultyId"><i class="bi bi-building me-1 text-primary"></i>Khoa / Viện đào tạo</label>
                            <select class="form-select ${not empty errors['facultyId'] ? 'is-invalid' : ''}" id="facultyId" name="facultyId">
                                <option value="">-- Chưa chọn khoa --</option>
                                <c:forEach var="faculty" items="${faculties}">
                                    <option value="${faculty.id}" ${form.facultyId == faculty.id ? 'selected' : ''}><c:out value="${faculty.name}"/></option>
                                </c:forEach>
                            </select>
                            <qa:formError field="facultyId"/>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="classCode"><i class="bi bi-people-fill me-1 text-primary"></i>Lớp sinh hoạt</label>
                            <input type="text" class="form-control ${not empty errors['classCode'] ? 'is-invalid' : ''}" id="classCode" name="classCode" value="<c:out value='${form.classCode}'/>" maxlength="30" placeholder="Ví dụ: 21110CLC">
                            <qa:formError field="classCode"/>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="cohort"><i class="bi bi-calendar3 me-1 text-primary"></i>Khóa học</label>
                            <input type="text" class="form-control ${not empty errors['cohort'] ? 'is-invalid' : ''}" id="cohort" name="cohort" value="<c:out value='${form.cohort}'/>" maxlength="10" placeholder="Ví dụ: K21">
                            <qa:formError field="cohort"/>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="program"><i class="bi bi-award-fill me-1 text-primary"></i>Chương trình / Hệ đào tạo</label>
                            <input type="text" class="form-control ${not empty errors['program'] ? 'is-invalid' : ''}" id="program" name="program" value="<c:out value='${form.program}'/>" maxlength="100" placeholder="Ví dụ: Đại trà, Chất lượng cao...">
                            <qa:formError field="program"/>
                        </div>
                    </div>
                </div>
                <div class="card-footer text-end">
                    <button type="submit" class="btn btn-primary shadow-sm">
                        <i class="bi bi-check2-circle me-1"></i> Lưu thay đổi
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="/assets/js/student/profile.js"></script>
</div>
