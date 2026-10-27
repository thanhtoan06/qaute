<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title><c:out value="${pageTitle}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/category-form.css">
</head>
<div class="qa-page-admin-category-form" data-qa-view="admin-category-form">

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
