<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Mẫu trả lời nhanh</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.css"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0">Mẫu trả lời nhanh</h2>
  <a class="btn btn-outline-primary" href="${ctx}/manager/canned-responses">Soạn mẫu mới</a>
</div>

<div class="row g-3">
  <div class="col-12 col-xl-7"><div class="card"><div class="card-header"><h3 class="card-title">Mẫu có thể sử dụng</h3></div><div class="table-responsive"><table class="table table-vcenter card-table">
    <thead><tr><th>Tiêu đề</th><th>Phạm vi</th><th>Chuyên mục</th><th>Trạng thái</th><th>Lượt dùng</th><th>Thao tác</th></tr></thead>
    <tbody><c:choose><c:when test="${empty items}"><tr><td colspan="6" class="text-secondary">Chưa có mẫu trả lời.</td></tr></c:when><c:otherwise><c:forEach var="item" items="${items}"><tr>
      <td class="fw-semibold"><c:out value="${item.title()}"/></td>
      <td>${item.scope() eq 'PERSONAL' ? 'Cá nhân' : 'Dùng chung'}</td>
      <td><c:choose><c:when test="${empty item.categoryPath()}">Tất cả chuyên mục</c:when><c:otherwise><c:out value="${item.categoryPath()}"/></c:otherwise></c:choose></td>
      <td><span class="badge ${item.active() ? 'bg-green-lt' : 'bg-secondary-lt'}">${item.active() ? 'Đang dùng' : 'Đã tắt'}</span></td>
      <td>${item.usageCount()}</td>
      <td><c:if test="${item.mine()}"><div class="d-flex gap-1"><a class="btn btn-sm btn-outline-primary" href="${ctx}/manager/canned-responses?edit=${item.id()}">Sửa</a><form method="post" action="${ctx}/manager/canned-responses/${item.id()}/delete"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-danger" type="submit" onclick="return confirm('Xóa mẫu trả lời này?')">Xóa</button></form></div></c:if></td>
    </tr></c:forEach></c:otherwise></c:choose></tbody>
  </table></div></div></div>

  <div class="col-12 col-xl-5"><div class="card"><div class="card-header"><h3 class="card-title">${empty form.id ? 'Tạo mẫu mới' : 'Sửa mẫu trả lời'}</h3></div><div class="card-body">
    <form method="post" action="${ctx}/manager/canned-responses" id="canned-response-form" novalidate>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input type="hidden" name="id" value="<c:out value='${form.id}'/>"/>
      <div class="mb-3"><label class="form-label required" for="canned-title">Tiêu đề</label><input class="form-control" id="canned-title" name="title" type="text" minlength="2" maxlength="120" value="<c:out value='${form.title}'/>" required/></div>
      <div class="mb-3"><label class="form-label required" for="canned-scope">Phạm vi</label><select class="form-select" id="canned-scope" name="scope" required><option value="PERSONAL" ${form.scope eq 'PERSONAL' ? 'selected' : ''}>Cá nhân</option><option value="SHARED" ${form.scope eq 'SHARED' ? 'selected' : ''}>Dùng chung</option></select></div>
      <div class="mb-3" id="canned-category-group"><label class="form-label required" for="canned-category">Chuyên mục</label><select class="form-select" id="canned-category" name="categoryId"><option value="">Chọn chuyên mục trong phạm vi</option><c:forEach var="option" items="${categoryOptions}"><option value="${option.key}" ${form.categoryId eq option.key ? 'selected' : ''}><c:out value="${option.value}"/></option></c:forEach></select><div class="form-hint">Mẫu dùng chung chỉ hiển thị cho Manager được giao chuyên mục này.</div></div>
      <div class="mb-3"><label class="form-label required" for="canned-body">Nội dung mẫu</label><textarea class="form-control" id="canned-body" name="bodyHtml" rows="9" maxlength="10000" required><c:out value="${form.bodyHtml}"/></textarea><div class="form-hint">Biến có thể dùng: <code>{{student_name}}</code>, <code>{{ticket_code}}</code>, <code>{{advisor_name}}</code>, <code>{{category}}</code>.</div><div id="canned-body-error" class="text-danger small" role="alert"></div></div>
      <label class="form-check form-switch mb-3"><input type="hidden" name="_active" value="on"/><input class="form-check-input" name="active" type="checkbox" value="true" ${form.active ? 'checked' : ''}/><span class="form-check-label">Đang dùng</span></label>
      <button class="btn btn-primary" type="submit">Lưu mẫu</button>
    </form>
  </div></div></div>
</div>

<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js" defer></script>
<script src="${ctx}/assets/js/manager/canned-responses.js" defer></script>
