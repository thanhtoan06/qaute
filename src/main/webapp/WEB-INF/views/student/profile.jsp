<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Hồ sơ sinh viên</title>

<div class="row g-4">
    <div class="col-12 col-md-4 col-lg-3">
        <div class="card text-center p-3">
            <div class="card-body p-0">
                <div class="position-relative d-inline-block mb-3">
                    <c:choose>
                        <c:when test="${not empty profile.avatarUrl()}">
                            <img id="avatarPreview" src="<c:out value='${profile.avatarUrl()}'/>" alt="Avatar" class="avatar avatar-2xl rounded-circle object-cover border shadow-sm" style="width: 140px; height: 140px; object-fit: cover;"/>
                        </c:when>
                        <c:otherwise>
                            <div id="avatarPreviewPlaceholder" class="avatar avatar-2xl rounded-circle bg-primary-lt d-flex align-items-center justify-content-center border shadow-sm mx-auto" style="width: 140px; height: 140px;">
                                <i class="bi bi-person fs-1 text-primary"></i>
                            </div>
                            <img id="avatarPreview" src="" alt="Avatar" class="avatar avatar-2xl rounded-circle object-cover border shadow-sm d-none" style="width: 140px; height: 140px; object-fit: cover;"/>
                        </c:otherwise>
                    </c:choose>
                    <div id="avatarSpinner" class="position-absolute top-50 start-50 translate-middle d-none bg-dark bg-opacity-50 rounded-circle" style="width: 140px; height: 140px; display: flex; align-items: center; justify-content: center;">
                        <div class="spinner-border text-white" role="status"></div>
                    </div>
                </div>
                <h3 class="m-0 mb-1"><c:out value="${profile.fullName()}"/></h3>
                <div class="text-secondary small mb-3"><c:out value="${profile.mssv()}"/></div>
                <button type="button" class="btn btn-outline-primary btn-sm w-100" id="btnChangeAvatar">
                    <i class="bi bi-camera me-1"></i> Đổi ảnh đại diện
                </button>
                <input type="file" id="avatarFileInput" accept="image/png,image/jpeg,image/webp,image/gif" class="d-none">
                <form id="avatarForm" method="post" action="/student/profile/avatar" class="d-none">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="hidden" name="assetId" id="avatarAssetIdInput"/>
                </form>
                <div id="avatarError" class="alert alert-danger small mt-2 py-1 px-2 d-none" role="alert"></div>
            </div>
        </div>
    </div>
    <div class="col-12 col-md-8 col-lg-9">
        <form method="post" action="/student/profile" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Thông tin sinh viên</h3>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errors['global']}">
                        <div class="alert alert-danger mb-3"><c:out value="${errors['global']}"/></div>
                    </c:if>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label" for="mssv">Mã số sinh viên (MSSV)</label>
                            <input type="text" class="form-control bg-light" id="mssv" value="<c:out value='${profile.mssv()}'/>" readonly disabled>
                            <small class="form-hint">MSSV do nhà trường quản lý, không thể thay đổi</small>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="email">Địa chỉ Email</label>
                            <input type="email" class="form-control bg-light" id="email" value="<c:out value='${profile.email()}'/>" readonly disabled>
                            <small class="form-hint">Email tài khoản không thể chỉnh sửa</small>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label required" for="fullName">Họ và tên</label>
                            <input type="text" class="form-control ${not empty errors['fullName'] ? 'is-invalid' : ''}" id="fullName" name="fullName" value="<c:out value='${form.fullName}'/>" minlength="2" maxlength="100" required>
                            <qa:formError field="fullName"/>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="phone">Số điện thoại</label>
                            <input type="tel" class="form-control ${not empty errors['phone'] ? 'is-invalid' : ''}" id="phone" name="phone" value="<c:out value='${form.phone}'/>" placeholder="0xxxxxxxxx hoặc +84xxxxxxxxx">
                            <qa:formError field="phone"/>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="facultyId">Khoa / Viện đào tạo</label>
                            <select class="form-select ${not empty errors['facultyId'] ? 'is-invalid' : ''}" id="facultyId" name="facultyId">
                                <option value="">-- Chưa chọn khoa --</option>
                                <c:forEach var="faculty" items="${faculties}">
                                    <option value="${faculty.id}" ${form.facultyId == faculty.id ? 'selected' : ''}><c:out value="${faculty.name}"/></option>
                                </c:forEach>
                            </select>
                            <qa:formError field="facultyId"/>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="classCode">Lớp sinh hoạt</label>
                            <input type="text" class="form-control ${not empty errors['classCode'] ? 'is-invalid' : ''}" id="classCode" name="classCode" value="<c:out value='${form.classCode}'/>" maxlength="30" placeholder="Ví dụ: 21110CLC">
                            <qa:formError field="classCode"/>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="cohort">Khóa học</label>
                            <input type="text" class="form-control ${not empty errors['cohort'] ? 'is-invalid' : ''}" id="cohort" name="cohort" value="<c:out value='${form.cohort}'/>" maxlength="10" placeholder="Ví dụ: K21">
                            <qa:formError field="cohort"/>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="program">Chương trình / Hệ đào tạo</label>
                            <input type="text" class="form-control ${not empty errors['program'] ? 'is-invalid' : ''}" id="program" name="program" value="<c:out value='${form.program}'/>" maxlength="100" placeholder="Ví dụ: Đại trà, Chất lượng cao...">
                            <qa:formError field="program"/>
                        </div>
                    </div>
                </div>
                <div class="card-footer text-end">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-check-lg me-1"></i> Lưu thay đổi
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="/assets/js/student/profile.js"></script>

