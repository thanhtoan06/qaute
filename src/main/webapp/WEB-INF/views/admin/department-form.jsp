<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="isEdit" value="${not empty form.id}"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>${isEdit ? 'Sửa phòng ban' : 'Thêm phòng ban'}</title>

<div class="mb-3">
  <a href="${ctx}/admin/departments" class="btn btn-link ps-0"><i class="bi bi-arrow-left me-1"></i>Danh sách phòng ban</a>
</div>

<div class="row justify-content-center">
  <div class="col-lg-8">
    <form method="post" action="${ctx}/admin/departments" novalidate>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <c:if test="${isEdit}">
        <input type="hidden" name="id" value="<c:out value='${form.id}'/>"/>
      </c:if>
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">${isEdit ? 'Sửa phòng ban' : 'Thêm phòng ban'}</h3>
        </div>
        <div class="card-body">
          <c:if test="${not empty errors['global']}">
            <div class="alert alert-danger"><c:out value="${errors['global']}"/></div>
          </c:if>
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
          <div class="mb-3">
            <label class="form-label" for="description">Mô tả</label>
            <textarea class="form-control ${not empty errors['description'] ? 'is-invalid' : ''}"
                      id="description" name="description" rows="2" maxlength="500"><c:out value="${form.description}"/></textarea>
            <qa:formError field="description"/>
          </div>
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
          <div class="mb-3">
            <label class="form-check form-switch">
              <input class="form-check-input" type="checkbox" id="active" name="active"
                     value="true" ${form.active ? 'checked' : ''}>
              <span class="form-check-label">Đang hoạt động</span>
            </label>
            <small class="form-hint">Tắt phòng ban khi nó không còn tiếp nhận tư vấn. Không xóa cứng phòng ban đã có dữ liệu.</small>
          </div>
        </div>
        <div class="card-footer d-flex justify-content-end gap-2">
          <a href="${ctx}/admin/departments" class="btn btn-link">Hủy</a>
          <button type="submit" class="btn btn-primary">${isEdit ? 'Lưu thay đổi' : 'Thêm phòng ban'}</button>
        </div>
      </div>
    </form>
  </div>
</div>
