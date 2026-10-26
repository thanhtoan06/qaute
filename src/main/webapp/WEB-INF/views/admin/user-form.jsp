<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>${isEdit ? 'Sửa người dùng' : 'Thêm người dùng'}</title>
<div class="qa-page-admin-user-form" data-qa-view="admin-user-form">
<style>
.qa-page-admin-user-form { --page-accent: #3579c7; --page-accent-dark: #285b94; --page-tint: #edf5ff; --page-line: #d6e6fa; min-width: 0; color: #344c67; }
.qa-page-admin-user-form > .page-header, .qa-page-admin-user-form > .qa-page-heading, .qa-page-admin-user-form > .d-flex:has(> .page-title), .qa-page-admin-user-form > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #edf5ff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-user-form > .page-header::after, .qa-page-admin-user-form > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #d6e6fa; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-user-form .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-user-form .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #d6e6fa; border-radius: 12px; color: #285b94; background: linear-gradient(145deg,#fff,#edf5ff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-user-form .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-user-form .card:hover { border-color: #d6e6fa; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-user-form .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#edf5ff); }
.qa-page-admin-user-form .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-user-form .card-body { color: #435b75; }
.qa-page-admin-user-form .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#edf5ff); }
.qa-page-admin-user-form .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-user-form .form-control, .qa-page-admin-user-form .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-user-form textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-user-form .form-control:focus, .qa-page-admin-user-form .form-select:focus { border-color: #3579c7; box-shadow: 0 0 0 4px #edf5ff; }
.qa-page-admin-user-form .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-user-form .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-user-form .btn-primary { border-color: #3579c7; background: linear-gradient(120deg,#3579c7,#285b94); box-shadow: 0 5px 13px #d6e6fa; }
.qa-page-admin-user-form .btn-outline-primary { border-color: #d6e6fa; color: #285b94; background: #fff; }
.qa-page-admin-user-form .btn-outline-primary:hover { border-color: #3579c7; color: #fff; background: linear-gradient(120deg,#3579c7,#285b94); }
.qa-page-admin-user-form .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#edf5ff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-user-form .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-user-form .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-user-form .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#edf5ff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-user-form .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-user-form .table tbody tr:hover { background: #edf5ff; }
.qa-page-admin-user-form .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-user-form .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-user-form .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-user-form .nav-tabs .nav-link.active { color: #285b94; box-shadow: inset 0 -2px #3579c7; }
.qa-page-admin-user-form .nav-pills .nav-link.active { color: #285b94; background: #edf5ff; box-shadow: inset 0 0 0 1px #d6e6fa; }
.qa-page-admin-user-form .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-user-form .progress-bar { border-radius: inherit; }
.qa-page-admin-user-form form:not(.qa-filter-bar) { --form-accent: var(--page-accent); }
.qa-page-admin-user-form form:not(.qa-filter-bar) > .card, .qa-page-admin-user-form form:not(.qa-filter-bar) .card { border-radius: 20px; }
.qa-page-admin-user-form form:not(.qa-filter-bar) .form-control:focus, .qa-page-admin-user-form form:not(.qa-filter-bar) .form-select:focus { border-color: var(--page-accent); box-shadow: 0 0 0 4px var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-admin-user-form > .page-header, .qa-page-admin-user-form > .qa-page-heading, .qa-page-admin-user-form > .d-flex:has(> .page-title), .qa-page-admin-user-form > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-user-form .card { border-radius: 15px; }
  .qa-page-admin-user-form .card-body { padding: 1rem; }
  .qa-page-admin-user-form .table-responsive { border-radius: 11px; }
  .qa-page-admin-user-form .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-user-form *, .qa-page-admin-user-form *::before, .qa-page-admin-user-form *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
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
