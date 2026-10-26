<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title><c:out value="${pageTitle}"/></title>
<div class="qa-page-admin-category-form" data-qa-view="admin-category-form">
<style>
.qa-page-admin-category-form { --page-accent: #cf7b27; --page-accent-dark: #98571b; --page-tint: #fff4e8; --page-line: #f5dfca; min-width: 0; color: #344c67; }
.qa-page-admin-category-form > .page-header, .qa-page-admin-category-form > .qa-page-heading, .qa-page-admin-category-form > .d-flex:has(> .page-title), .qa-page-admin-category-form > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff4e8, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-category-form > .page-header::after, .qa-page-admin-category-form > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f5dfca; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-category-form .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-category-form .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f5dfca; border-radius: 12px; color: #98571b; background: linear-gradient(145deg,#fff,#fff4e8); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-category-form .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-category-form .card:hover { border-color: #f5dfca; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-category-form .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff4e8); }
.qa-page-admin-category-form .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-category-form .card-body { color: #435b75; }
.qa-page-admin-category-form .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#fff4e8); }
.qa-page-admin-category-form .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-category-form .form-control, .qa-page-admin-category-form .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-category-form textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-category-form .form-control:focus, .qa-page-admin-category-form .form-select:focus { border-color: #cf7b27; box-shadow: 0 0 0 4px #fff4e8; }
.qa-page-admin-category-form .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-category-form .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-category-form .btn-primary { border-color: #cf7b27; background: linear-gradient(120deg,#cf7b27,#98571b); box-shadow: 0 5px 13px #f5dfca; }
.qa-page-admin-category-form .btn-outline-primary { border-color: #f5dfca; color: #98571b; background: #fff; }
.qa-page-admin-category-form .btn-outline-primary:hover { border-color: #cf7b27; color: #fff; background: linear-gradient(120deg,#cf7b27,#98571b); }
.qa-page-admin-category-form .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#fff4e8); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-category-form .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-category-form .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-category-form .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff4e8,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-category-form .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-category-form .table tbody tr:hover { background: #fff4e8; }
.qa-page-admin-category-form .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-category-form .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-category-form .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-category-form .nav-tabs .nav-link.active { color: #98571b; box-shadow: inset 0 -2px #cf7b27; }
.qa-page-admin-category-form .nav-pills .nav-link.active { color: #98571b; background: #fff4e8; box-shadow: inset 0 0 0 1px #f5dfca; }
.qa-page-admin-category-form .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-category-form .progress-bar { border-radius: inherit; }
.qa-page-admin-category-form form:not(.qa-filter-bar) { --form-accent: var(--page-accent); }
.qa-page-admin-category-form form:not(.qa-filter-bar) > .card, .qa-page-admin-category-form form:not(.qa-filter-bar) .card { border-radius: 20px; }
.qa-page-admin-category-form form:not(.qa-filter-bar) .form-control:focus, .qa-page-admin-category-form form:not(.qa-filter-bar) .form-select:focus { border-color: var(--page-accent); box-shadow: 0 0 0 4px var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-admin-category-form > .page-header, .qa-page-admin-category-form > .qa-page-heading, .qa-page-admin-category-form > .d-flex:has(> .page-title), .qa-page-admin-category-form > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-category-form .card { border-radius: 15px; }
  .qa-page-admin-category-form .card-body { padding: 1rem; }
  .qa-page-admin-category-form .table-responsive { border-radius: 11px; }
  .qa-page-admin-category-form .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-category-form *, .qa-page-admin-category-form *::before, .qa-page-admin-category-form *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%-- Dữ liệu từ AdminCategoryController: form (CategoryForm), departments (DepartmentOption), parents (CategoryView cấp 1 của mọi phòng ban), errors (Map trường -> thông báo) --%>
<!-- Tiêu đề trang -->
<h2 class="page-title mb-3 qa-view-title" data-qa-page="admin-category-form"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-diagram-3"></i></span><c:out value="${pageTitle}"/></h2>

<%-- Tạo hoặc chỉnh sửa chuyên mục, cây cha-con và thông tin hiển thị. --%>
<!-- Form tạo/sửa chuyên mục -->
<div class="row">
  <div class="col-lg-8">
    <div class="card">
      <div class="card-body">

        <c:if test="${not empty errors.global}">
          <div class="alert alert-danger" role="alert"><c:out value="${errors.global}"/></div>
        </c:if>

        <form method="post" action="${ctx}/admin/categories" novalidate>
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <input type="hidden" name="id" value="${form.id}"/>

          <!-- Chọn phòng ban -->
          <div class="mb-3">
            <label class="form-label required" for="departmentId">Phòng ban</label>
            <select class="form-select${not empty errors.departmentId ? ' is-invalid' : ''}" id="departmentId" name="departmentId" required>
              <option value="">-- Chọn phòng ban --</option>
              <c:forEach var="d" items="${departments}">
                <option value="${d.id()}" ${form.departmentId == d.id() ? 'selected' : ''}><c:out value="${d.name()}"/><c:if test="${!d.active()}"> (đã tắt)</c:if></option>
              </c:forEach>
            </select>
            <qa:formError field="departmentId"/>
          </div>

          <!-- Chọn chuyên mục cha để tạo cấu trúc 2 cấp -->
          <div class="mb-3">
            <label class="form-label" for="parentId">Chuyên mục cha</label>
            <select class="form-select${not empty errors.parentId ? ' is-invalid' : ''}" id="parentId" name="parentId" data-selected="${form.parentId}">
              <option value="">-- Không có (đây là chuyên mục cấp 1) --</option>
              <c:forEach var="p" items="${parents}">
                <option value="${p.id()}" data-department-id="${p.departmentId()}" ${form.parentId == p.id() ? 'selected' : ''}><c:out value="${p.name()}"/><c:if test="${!p.active()}"> (đã tắt)</c:if></option>
              </c:forEach>
            </select>
            <div class="form-hint">Chọn chuyên mục cha để tạo chủ đề con (tối đa 2 cấp). Danh sách chỉ hiện các chuyên mục cấp 1 của phòng ban đã chọn.</div>
            <qa:formError field="parentId"/>
          </div>

          <!-- Nhập mã và thứ tự hiển thị -->
          <div class="row">
            <div class="col-md-6 mb-3">
              <label class="form-label required" for="code">Mã</label>
              <input type="text" class="form-control${not empty errors.code ? ' is-invalid' : ''}" id="code" name="code"
                     value="<c:out value='${form.code}'/>" maxlength="40" placeholder="VD: DANG_KY_HOC_PHAN" required/>
              <div class="form-hint">2–40 ký tự: chữ in hoa, số hoặc dấu gạch dưới.</div>
              <qa:formError field="code"/>
            </div>
            <div class="col-md-6 mb-3">
              <label class="form-label" for="sortOrder">Thứ tự hiển thị</label>
              <input type="number" class="form-control${not empty errors.sortOrder ? ' is-invalid' : ''}" id="sortOrder" name="sortOrder"
                     value="<c:out value='${form.sortOrder}'/>" min="0"/>
              <qa:formError field="sortOrder"/>
            </div>
          </div>

          <!-- Nhập tên chuyên mục -->
          <div class="mb-3">
            <label class="form-label required" for="name">Tên</label>
            <input type="text" class="form-control${not empty errors.name ? ' is-invalid' : ''}" id="name" name="name"
                   value="<c:out value='${form.name}'/>" maxlength="150" required/>
            <qa:formError field="name"/>
          </div>

          <!-- Nhập mô tả -->
          <div class="mb-3">
            <label class="form-label" for="description">Mô tả</label>
            <textarea class="form-control${not empty errors.description ? ' is-invalid' : ''}" id="description" name="description"
                      rows="3" maxlength="500"><c:out value="${form.description}"/></textarea>
            <qa:formError field="description"/>
          </div>

          <!-- Nhập biểu tượng Bootstrap Icons -->
          <div class="mb-3">
            <label class="form-label" for="icon">Biểu tượng</label>
            <div class="input-group">
              <span class="input-group-text"><i id="iconPreview" class="bi"></i></span>
              <input type="text" class="form-control${not empty errors.icon ? ' is-invalid' : ''}" id="icon" name="icon"
                     value="<c:out value='${form.icon}'/>" maxlength="40" placeholder="bi-journal-text"/>
            </div>
            <div class="form-hint">Tên biểu tượng Bootstrap Icons, ví dụ <code>bi-journal-text</code>. Có thể để trống.</div>
            <qa:formError field="icon"/>
          </div>

          <!-- Chuyển đổi trạng thái hoạt động -->
          <div class="mb-4">
            <label class="form-check form-switch">
              <%-- _active: Spring dùng để biết ô tích bị bỏ (trình duyệt không gửi ô chưa tích) --%>
              <input type="hidden" name="_active" value="on"/>
              <input class="form-check-input" type="checkbox" name="active" value="true" ${form.active ? 'checked' : ''}/>
              <span class="form-check-label">Đang hoạt động</span>
            </label>
          </div>

          <!-- Nút lưu và hủy -->
          <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary">Lưu</button>
            <a class="btn btn-outline-secondary" href="${ctx}/admin/categories">Hủy</a>
          </div>
        </form>

      </div>
    </div>
  </div>
</div>

<script src="${ctx}/assets/js/admin/category-form.js" defer></script>
</div>
