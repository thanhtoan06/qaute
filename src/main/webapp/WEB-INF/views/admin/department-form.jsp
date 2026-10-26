<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="isEdit" value="${not empty form.id}"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>${isEdit ? 'Sửa phòng ban' : 'Thêm phòng ban'}</title>
<div class="qa-page-admin-department-form" data-qa-view="admin-department-form">
<style>
.qa-page-admin-department-form { --page-accent: #cf7b27; --page-accent-dark: #98571b; --page-tint: #fff4e8; --page-line: #f5dfca; min-width: 0; color: #344c67; }
.qa-page-admin-department-form > .page-header, .qa-page-admin-department-form > .qa-page-heading, .qa-page-admin-department-form > .d-flex:has(> .page-title), .qa-page-admin-department-form > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff4e8, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-department-form > .page-header::after, .qa-page-admin-department-form > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f5dfca; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-department-form .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-department-form .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f5dfca; border-radius: 12px; color: #98571b; background: linear-gradient(145deg,#fff,#fff4e8); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-department-form .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-department-form .card:hover { border-color: #f5dfca; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-department-form .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff4e8); }
.qa-page-admin-department-form .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-department-form .card-body { color: #435b75; }
.qa-page-admin-department-form .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#fff4e8); }
.qa-page-admin-department-form .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-department-form .form-control, .qa-page-admin-department-form .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-department-form textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-department-form .form-control:focus, .qa-page-admin-department-form .form-select:focus { border-color: #cf7b27; box-shadow: 0 0 0 4px #fff4e8; }
.qa-page-admin-department-form .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-department-form .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-department-form .btn-primary { border-color: #cf7b27; background: linear-gradient(120deg,#cf7b27,#98571b); box-shadow: 0 5px 13px #f5dfca; }
.qa-page-admin-department-form .btn-outline-primary { border-color: #f5dfca; color: #98571b; background: #fff; }
.qa-page-admin-department-form .btn-outline-primary:hover { border-color: #cf7b27; color: #fff; background: linear-gradient(120deg,#cf7b27,#98571b); }
.qa-page-admin-department-form .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#fff4e8); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-department-form .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-department-form .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-department-form .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff4e8,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-department-form .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-department-form .table tbody tr:hover { background: #fff4e8; }
.qa-page-admin-department-form .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-department-form .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-department-form .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-department-form .nav-tabs .nav-link.active { color: #98571b; box-shadow: inset 0 -2px #cf7b27; }
.qa-page-admin-department-form .nav-pills .nav-link.active { color: #98571b; background: #fff4e8; box-shadow: inset 0 0 0 1px #f5dfca; }
.qa-page-admin-department-form .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-department-form .progress-bar { border-radius: inherit; }
.qa-page-admin-department-form form:not(.qa-filter-bar) { --form-accent: var(--page-accent); }
.qa-page-admin-department-form form:not(.qa-filter-bar) > .card, .qa-page-admin-department-form form:not(.qa-filter-bar) .card { border-radius: 20px; }
.qa-page-admin-department-form form:not(.qa-filter-bar) .form-control:focus, .qa-page-admin-department-form form:not(.qa-filter-bar) .form-select:focus { border-color: var(--page-accent); box-shadow: 0 0 0 4px var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-admin-department-form > .page-header, .qa-page-admin-department-form > .qa-page-heading, .qa-page-admin-department-form > .d-flex:has(> .page-title), .qa-page-admin-department-form > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-department-form .card { border-radius: 15px; }
  .qa-page-admin-department-form .card-body { padding: 1rem; }
  .qa-page-admin-department-form .table-responsive { border-radius: 11px; }
  .qa-page-admin-department-form .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-department-form *, .qa-page-admin-department-form *::before, .qa-page-admin-department-form *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>

<!-- Nút quay lại danh sách -->
<div class="mb-3">
  <a href="${ctx}/admin/departments" class="btn btn-link ps-0"><i class="bi bi-arrow-left me-1"></i>Danh sách phòng ban</a>
</div>

<div class="row justify-content-center">
  <div class="col-lg-8">
    <%-- Lưu mã, thông tin liên hệ, thứ tự và trạng thái phòng ban. --%>
    <!-- Form tạo/sửa phòng ban -->
    <form method="post" action="${ctx}/admin/departments" novalidate>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <c:if test="${isEdit}">
        <input type="hidden" name="id" value="<c:out value='${form.id}'/>"/>
      </c:if>
      <div class="card">
        <div class="card-header">
          <h3 class="card-title qa-view-title" data-qa-page="admin-department-form"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-buildings"></i></span>${isEdit ? 'Sửa phòng ban' : 'Thêm phòng ban'}</h3>
        </div>
        <div class="card-body">
          <c:if test="${not empty errors['global']}">
            <div class="alert alert-danger"><c:out value="${errors['global']}"/></div>
          </c:if>
          <!-- Nhập mã và tên phòng ban -->
          <div class="row">
            <div class="col-md-4 mb-3">
              <label class="form-label required" for="code">Mã phòng ban</label>
              <input class="form-control ${not empty errors['code'] ? 'is-invalid' : ''}"
                     id="code" name="code" value="<c:out value='${form.code}'/>"
                     maxlength="30" placeholder="VD: DAOTAO">
              <qa:formError field="code"/>
              <small class="form-hint">Chữ hoa, số, gạch dưới.</small>
            </div>
            <div class="col-md-8 mb-3">
              <label class="form-label required" for="name">Tên phòng ban</label>
              <input class="form-control ${not empty errors['name'] ? 'is-invalid' : ''}"
                     id="name" name="name" value="<c:out value='${form.name}'/>"
                     maxlength="150" placeholder="VD: Phòng Đào tạo">
              <qa:formError field="name"/>
            </div>
          </div>
          <!-- Nhập mô tả -->
          <div class="mb-3">
            <label class="form-label" for="description">Mô tả</label>
            <textarea class="form-control ${not empty errors['description'] ? 'is-invalid' : ''}"
                      id="description" name="description" rows="2" maxlength="500"><c:out value="${form.description}"/></textarea>
            <qa:formError field="description"/>
          </div>
          <!-- Nhập thông tin liên hệ -->
          <div class="row">
            <div class="col-md-6 mb-3">
              <label class="form-label" for="email">Email liên hệ</label>
              <input type="email" class="form-control ${not empty errors['email'] ? 'is-invalid' : ''}"
                     id="email" name="email" value="<c:out value='${form.email}'/>"
                     maxlength="150" placeholder="VD: daotao@hcmute.edu.vn">
              <qa:formError field="email"/>
            </div>
            <div class="col-md-6 mb-3">
              <label class="form-label" for="phone">Điện thoại</label>
              <input class="form-control ${not empty errors['phone'] ? 'is-invalid' : ''}"
                     id="phone" name="phone" value="<c:out value='${form.phone}'/>"
                     maxlength="30">
              <qa:formError field="phone"/>
            </div>
          </div>
          <!-- Nhập địa điểm và thứ tự hiển thị -->
          <div class="row">
            <div class="col-md-8 mb-3">
              <label class="form-label" for="location">Địa điểm</label>
              <input class="form-control ${not empty errors['location'] ? 'is-invalid' : ''}"
                     id="location" name="location" value="<c:out value='${form.location}'/>"
                     maxlength="200" placeholder="VD: Tòa A, tầng 1">
              <qa:formError field="location"/>
            </div>
            <div class="col-md-4 mb-3">
              <label class="form-label" for="sortOrder">Thứ tự hiển thị</label>
              <input type="number" min="0" class="form-control ${not empty errors['sortOrder'] ? 'is-invalid' : ''}"
                     id="sortOrder" name="sortOrder" value="<c:out value='${form.sortOrder}'/>">
              <qa:formError field="sortOrder"/>
            </div>
          </div>
          <!-- Chuyển đổi trạng thái hoạt động -->
          <div class="mb-3">
            <label class="form-check form-switch">
              <input class="form-check-input" type="checkbox" id="active" name="active"
                     value="true" ${form.active ? 'checked' : ''}>
              <span class="form-check-label">Đang hoạt động</span>
            </label>
            <small class="form-hint">Tắt phòng ban khi nó không còn tiếp nhận tư vấn. Không xóa cứng phòng ban đã có dữ liệu.</small>
          </div>
        </div>
        <!-- Nút hủy và lưu -->
        <div class="card-footer d-flex justify-content-end gap-2">
          <a href="${ctx}/admin/departments" class="btn btn-link">Hủy</a>
          <button type="submit" class="btn btn-primary">${isEdit ? 'Lưu thay đổi' : 'Thêm phòng ban'}</button>
        </div>
      </div>
    </form>
  </div>
</div>
</div>
