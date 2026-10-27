<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>${isEdit ? 'Sửa người dùng' : 'Thêm người dùng'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/user-form.css">
</head>
<div class="qa-page-admin-user-form" data-qa-view="admin-user-form">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="formAction" value="${ctx}/admin/users"/>
<c:if test="${isEdit}"><c:set var="formAction" value="${ctx}/admin/users/${account.id()}"/></c:if>
<!-- Nút quay lại -->
<div class="mb-3"><a class="btn btn-link ps-0" href="${ctx}/admin/users"><i class="bi bi-arrow-left me-1"></i>Danh sách người dùng</a></div>
<div class="row justify-content-center"><div class="col-lg-8">
  <!-- Form tạo/sửa người dùng -->
  <form method="post" action="${formAction}" novalidate
        data-user-form data-role="${isEdit ? account.role() : form.role}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div class="card">
      <div class="card-header"><h2 class="card-title qa-view-title" data-qa-page="admin-user-form"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-person-vcard"></i></span>${isEdit ? 'Sửa người dùng' : 'Thêm người dùng'}</h2></div>
      <div class="card-body">
        <c:if test="${not empty errors['global']}"><div class="alert alert-danger"><c:out value="${errors['global']}"/></div></c:if>
        <!-- Chế độ sửa: hiển thị vai trò và email -->
        <c:choose>
          <c:when test="${isEdit}">
            <div class="mb-3"><label class="form-label">Vai trò</label><div><c:out value="${account.role()}"/></div></div>
            <div class="mb-3"><label class="form-label">Email</label><div><c:out value="${account.email()}"/></div></div>
          </c:when>
          <!-- Chế độ tạo: chọn vai trò và nhập email -->
          <c:otherwise>
            <div class="mb-3"><label class="form-label required" for="role">Vai trò</label>
              <select class="form-select ${not empty errors['role'] ? 'is-invalid' : ''}" id="role" name="role" required>
                <option value="">Chọn vai trò</option>
                <c:forEach var="role" items="${roles}"><option value="${role}" ${form.role == role ? 'selected' : ''}><c:out value="${role}"/></option></c:forEach>
              </select><qa:formError field="role"/></div>
            <div class="mb-3"><label class="form-label required" for="email">Email</label>
              <input class="form-control ${not empty errors['email'] ? 'is-invalid' : ''}" id="email" name="email" type="email"
                     value="<c:out value='${form.email}'/>" maxlength="150" required/>
              <qa:formError field="email"/></div>
          </c:otherwise>
        </c:choose>
        <!-- Thông tin chung: họ tên và điện thoại -->
        <div class="row">
          <div class="col-md-7 mb-3"><label class="form-label required" for="fullName">Họ và tên</label>
            <input class="form-control ${not empty errors['fullName'] ? 'is-invalid' : ''}" id="fullName" name="fullName"
                   value="<c:out value='${form.fullName}'/>" minlength="2" maxlength="100" required/>
            <qa:formError field="fullName"/></div>
          <div class="col-md-5 mb-3"><label class="form-label" for="phone">Số điện thoại</label>
            <input class="form-control ${not empty errors['phone'] ? 'is-invalid' : ''}" id="phone" name="phone" type="tel"
                   value="<c:out value='${form.phone}'/>" maxlength="20"/>
            <qa:formError field="phone"/></div>
        </div>
        <!-- Hồ sơ sinh viên (hiển thị khi chọn vai trò STUDENT) -->
        <div data-role-fields="STUDENT">
          <h3 class="mb-3">Hồ sơ sinh viên</h3>
          <div class="row">
            <div class="col-md-5 mb-3"><label class="form-label required" for="mssv">MSSV</label>
              <input class="form-control ${not empty errors['mssv'] ? 'is-invalid' : ''}" id="mssv" name="mssv"
                     value="<c:out value='${form.mssv}'/>" inputmode="numeric" pattern="[0-9]{8}" maxlength="8"/>
              <qa:formError field="mssv"/></div>
            <div class="col-md-7 mb-3"><label class="form-label" for="facultyId">Khoa</label>
              <select class="form-select ${not empty errors['facultyId'] ? 'is-invalid' : ''}" id="facultyId" name="facultyId">
                <option value="">Chưa chọn</option>
                <c:forEach var="faculty" items="${faculties}"><option value="${faculty.id}" ${form.facultyId == faculty.id ? 'selected' : ''}><c:out value="${faculty.name}"/></option></c:forEach>
              </select><qa:formError field="facultyId"/></div>
            <div class="col-md-4 mb-3"><label class="form-label" for="classCode">Lớp</label>
              <input class="form-control" id="classCode" name="classCode" value="<c:out value='${form.classCode}'/>" maxlength="30"/><qa:formError field="classCode"/></div>
            <div class="col-md-3 mb-3"><label class="form-label" for="cohort">Khóa</label>
              <input class="form-control" id="cohort" name="cohort" value="<c:out value='${form.cohort}'/>" maxlength="10"/><qa:formError field="cohort"/></div>
            <div class="col-md-5 mb-3"><label class="form-label" for="program">Chương trình</label>
              <input class="form-control" id="program" name="program" value="<c:out value='${form.program}'/>" maxlength="100"/><qa:formError field="program"/></div>
          </div>
        </div>
        <!-- Hồ sơ tư vấn viên (hiển thị khi chọn vai trò MANAGER) -->
        <div data-role-fields="MANAGER">
          <h3 class="mb-3">Hồ sơ tư vấn viên</h3>
          <div class="row">
            <div class="col-md-6 mb-3"><label class="form-label" for="departmentId">Phòng ban</label>
              <select class="form-select" id="departmentId" name="departmentId"><option value="">Chưa chọn</option>
                <c:forEach var="department" items="${departments}"><option value="${department.id}" ${form.departmentId == department.id ? 'selected' : ''}><c:out value="${department.name}"/></option></c:forEach>
              </select><qa:formError field="departmentId"/></div>
            <div class="col-md-6 mb-3"><label class="form-label" for="jobTitle">Chức danh</label>
              <input class="form-control" id="jobTitle" name="jobTitle" value="<c:out value='${form.jobTitle}'/>" maxlength="100"/><qa:formError field="jobTitle"/></div>
            <div class="col-md-6 mb-3"><label class="form-label" for="employeeCode">Mã nhân viên</label>
              <input class="form-control" id="employeeCode" name="employeeCode" value="<c:out value='${form.employeeCode}'/>" maxlength="30"/><qa:formError field="employeeCode"/></div>
          </div>
        </div>
      </div>
      <!-- Nút lưu và hủy -->
      <div class="card-footer d-flex justify-content-end gap-2">
        <a class="btn btn-outline-secondary" href="${ctx}/admin/users">Hủy</a>
        <button class="btn btn-primary" type="submit">${isEdit ? 'Lưu thay đổi' : 'Tạo và gửi lời mời'}</button>
      </div>
    </div>
  </form>
</div></div>
<script src="${ctx}/assets/js/admin/user-form.js" defer></script>
</div>
