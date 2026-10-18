<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chính sách SLA</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h2 class="page-title mb-3">Chính sách SLA</h2>
<div class="alert alert-info">Thay đổi chính sách chỉ áp dụng cho ticket tạo mới.</div>
<c:if test="${not empty formError}"><div class="alert alert-danger"><c:out value="${formError}"/></div></c:if>

<div class="card">
  <div class="card-header p-0">
    <div class="nav nav-tabs card-header-tabs px-3" role="tablist">
      <button class="nav-link active" type="button" data-bs-toggle="tab" data-bs-target="#sla-policy" role="tab" aria-selected="true">Chính sách</button>
      <button class="nav-link" type="button" data-bs-toggle="tab" data-bs-target="#sla-hours" role="tab" aria-selected="false">Giờ làm việc</button>
      <button class="nav-link" type="button" data-bs-toggle="tab" data-bs-target="#sla-holidays" role="tab" aria-selected="false">Ngày nghỉ</button>
    </div>
  </div>
  <div class="tab-content">
    <div class="tab-pane fade show active" id="sla-policy" role="tabpanel">
      <div class="card-body border-bottom">
        <h3 class="card-title">Mặc định toàn hệ thống</h3>
        <div class="table-responsive">
          <table class="table table-vcenter mb-0">
            <thead><tr><th>Ưu tiên</th><th>Phản hồi đầu tiên</th><th>Giải quyết</th><th>Trạng thái</th></tr></thead>
            <tbody>
              <c:forEach var="policy" items="${matrix.defaults()}">
                <tr>
                  <td><c:out value="${policy.priority().label}"/></td>
                  <td><c:out value="${policy.firstResponseHours()}"/> giờ</td>
                  <td><c:out value="${policy.resolutionHours()}"/> giờ</td>
                  <td><c:choose><c:when test="${policy.active()}"><span class="badge bg-green-lt">Bật</span></c:when><c:otherwise><span class="badge bg-secondary-lt">Tắt</span></c:otherwise></c:choose></td>
                </tr>
              </c:forEach>
              <c:if test="${empty matrix.defaults()}"><tr><td colspan="4" class="text-secondary text-center">Chưa có chính sách mặc định.</td></tr></c:if>
            </tbody>
          </table>
        </div>
      </div>

      <div class="card-body border-bottom">
        <h3 class="card-title">Ghi đè theo chuyên mục</h3>
        <div class="table-responsive">
          <table class="table table-vcenter mb-0">
            <thead><tr><th>Chuyên mục</th><th>Ưu tiên</th><th>Phản hồi đầu tiên</th><th>Giải quyết</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
            <tbody>
              <c:forEach var="override" items="${matrix.overrides()}">
                <c:forEach var="policy" items="${override.rows()}" varStatus="rowStatus">
                  <tr>
                    <c:if test="${rowStatus.first}">
                      <td rowspan="${fn:length(override.rows())}"><c:out value="${override.categoryName()}"/></td>
                    </c:if>
                    <td><c:out value="${policy.priority().label}"/></td>
                    <td><c:out value="${policy.firstResponseHours()}"/> giờ</td>
                    <td><c:out value="${policy.resolutionHours()}"/> giờ</td>
                    <td><c:choose><c:when test="${policy.active()}"><span class="badge bg-green-lt">Bật</span></c:when><c:otherwise><span class="badge bg-secondary-lt">Tắt</span></c:otherwise></c:choose></td>
                    <c:if test="${rowStatus.first}">
                      <td rowspan="${fn:length(override.rows())}">
                        <div class="d-flex flex-wrap gap-1">
                          <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/sla/policy?categoryId=${override.categoryId()}">Sửa</a>
                          <qa:confirmModal id="delete-sla-${override.categoryId()}" title="Xóa ghi đè SLA"
                                           message="Xóa chính sách ghi đè của chuyên mục này? Chính sách mặc định sẽ được áp dụng cho ticket mới."
                                           actionUrl="${ctx}/admin/sla/policy/${override.categoryId()}/delete"
                                           confirmLabel="Xóa"/>
                        </div>
                      </td>
                    </c:if>
                  </tr>
                </c:forEach>
              </c:forEach>
              <c:if test="${empty matrix.overrides()}"><tr><td colspan="6" class="text-secondary text-center">Chưa có chính sách ghi đè.</td></tr></c:if>
            </tbody>
          </table>
        </div>
      </div>

      <div class="card-body border-bottom">
        <h3 class="card-title">Chọn chính sách để sửa</h3>
        <form method="get" action="${ctx}/admin/sla/policy" class="d-flex flex-wrap gap-2 align-items-end">
          <div class="flex-fill">
            <label class="form-label" for="sla-category-choice">Chuyên mục</label>
            <select class="form-select" id="sla-category-choice" name="categoryId">
              <option value="">Mặc định toàn hệ thống</option>
              <c:forEach var="option" items="${categoryOptions}">
                <option value="${option.id()}" ${option.id() eq form.categoryId ? 'selected' : ''}><c:out value="${option.label()}"/></option>
              </c:forEach>
            </select>
          </div>
          <button class="btn btn-outline-primary" type="submit">Chọn</button>
        </form>
      </div>

      <div class="card-body">
        <h3 class="card-title">${empty form.categoryId ? 'Sửa chính sách mặc định' : 'Sửa chính sách chuyên mục'}</h3>
        <form method="post" action="${ctx}/admin/sla/policy">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <input type="hidden" name="categoryId" value="<c:out value='${form.categoryId}'/>"/>
          <div class="table-responsive">
            <table class="table table-vcenter">
              <thead><tr><th>Ưu tiên</th><th>Phản hồi đầu tiên (giờ)</th><th>Giải quyết (giờ)</th><th>Bật</th></tr></thead>
              <tbody>
                <c:forEach var="row" items="${form.rows}" varStatus="rowStatus">
                  <c:set var="responseKey" value="rows[${rowStatus.index}].firstResponseHours"/>
                  <c:set var="resolutionKey" value="rows[${rowStatus.index}].resolutionHours"/>
                  <tr>
                    <td>
                      <c:out value="${row.priority.label}"/>
                      <input type="hidden" name="rows[${rowStatus.index}].priority" value="${row.priority}"/>
                    </td>
                    <td>
                      <input class="form-control ${not empty errors[responseKey] ? 'is-invalid' : ''}"
                             type="number" step="0.01" min="0.5" max="720"
                             name="rows[${rowStatus.index}].firstResponseHours"
                             value="<c:out value='${row.firstResponseHours}'/>" required/>
                      <c:if test="${not empty errors[responseKey]}"><div class="text-danger small mt-1"><c:out value="${errors[responseKey]}"/></div></c:if>
                    </td>
                    <td>
                      <input class="form-control ${not empty errors[resolutionKey] ? 'is-invalid' : ''}"
                             type="number" step="0.01" min="0.5" max="2160"
                             name="rows[${rowStatus.index}].resolutionHours"
                             value="<c:out value='${row.resolutionHours}'/>" required/>
                      <c:if test="${not empty errors[resolutionKey]}"><div class="text-danger small mt-1"><c:out value="${errors[resolutionKey]}"/></div></c:if>
                    </td>
                    <td><input type="hidden" name="_rows[${rowStatus.index}].active" value="on"/>
                        <input class="form-check-input" type="checkbox"
                               name="rows[${rowStatus.index}].active" value="true"
                               ${row.active ? 'checked' : ''}/></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
          <button class="btn btn-primary" type="submit">Lưu chính sách</button>
        </form>
      </div>
    </div>
    <div class="tab-pane fade" id="sla-hours" role="tabpanel"><div class="card-body text-secondary">Giờ làm việc: làm ở bước sau.</div></div>
    <div class="tab-pane fade" id="sla-holidays" role="tabpanel"><div class="card-body text-secondary">Ngày nghỉ: làm ở bước sau.</div></div>
  </div>
</div>
