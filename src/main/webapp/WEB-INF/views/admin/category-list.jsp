<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chuyên mục</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%-- Dữ liệu từ AdminCategoryController.list: trees (AdminCategoryTree), departments (DepartmentOption), selectedDepartmentId --%>
<c:url var="newUrl" value="/admin/categories/new">
  <c:if test="${not empty selectedDepartmentId}">
    <c:param name="departmentId" value="${selectedDepartmentId}"/>
  </c:if>
</c:url>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0">Quản lý chuyên mục</h2>
  <a class="btn btn-primary" href="${newUrl}"><i class="bi bi-plus-lg me-1"></i>Thêm chuyên mục</a>
</div>

<%-- Bộ lọc theo phòng ban --%>
<form method="get" action="${ctx}/admin/categories" class="qa-filter-bar">
  <div>
    <label class="form-label" for="departmentFilter">Phòng ban</label>
    <select class="form-select" id="departmentFilter" name="departmentId">
      <option value="">Tất cả phòng ban</option>
      <c:forEach var="d" items="${departments}">
        <option value="${d.id()}" ${selectedDepartmentId == d.id() ? 'selected' : ''}><c:out value="${d.name()}"/><c:if test="${!d.active()}"> (đã tắt)</c:if></option>
      </c:forEach>
    </select>
  </div>
  <div class="d-flex gap-2">
    <button type="submit" class="btn btn-primary">Lọc</button>
    <a class="btn btn-outline-secondary" href="${ctx}/admin/categories">Bỏ lọc</a>
  </div>
</form>

<c:choose>
  <c:when test="${empty trees}">
    <qa:emptyState icon="bi-diagram-3" title="Chưa có phòng ban"
                   message="Hãy tạo phòng ban trước khi thêm chuyên mục."
                   actionUrl="${ctx}/admin/departments/new" actionLabel="Thêm phòng ban"/>
  </c:when>
  <c:otherwise>
    <c:forEach var="tree" items="${trees}">
      <div class="card mb-3">
        <div class="card-header">
          <h3 class="card-title">
            <c:out value="${tree.departmentName()}"/>
            <small class="text-secondary ms-1"><c:out value="${tree.departmentCode()}"/></small>
          </h3>
          <div class="card-actions d-flex align-items-center gap-2">
            <c:if test="${!tree.departmentActive()}">
              <span class="badge bg-secondary-lt">Phòng ban đã tắt</span>
            </c:if>
            <c:url var="addUrl" value="/admin/categories/new">
              <c:param name="departmentId" value="${tree.departmentId()}"/>
            </c:url>
            <a class="btn btn-sm btn-primary" href="${addUrl}"><i class="bi bi-plus-lg me-1"></i>Thêm chuyên mục</a>
          </div>
        </div>

        <c:choose>
          <c:when test="${empty tree.categories()}">
            <div class="card-body text-secondary">Phòng ban này chưa có chuyên mục nào.</div>
          </c:when>
          <c:otherwise>
            <div class="table-responsive">
              <table class="table table-vcenter card-table qa-admin-table">
                <thead>
                  <tr>
                    <th>Mã</th>
                    <th>Tên</th>
                    <th>Thứ tự</th>
                    <th>FAQ</th>
                    <th>Yêu cầu đang mở</th>
                    <th>Trạng thái</th>
                    <th class="text-end">Thao tác</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="node" items="${tree.categories()}">
                    <%-- Chuyên mục cấp 1 --%>
                    <tr>
                      <td><code><c:out value="${node.code()}"/></code></td>
                      <td>
                        <c:if test="${not empty node.icon()}"><i class="bi <c:out value='${node.icon()}'/> me-2"></i></c:if>
                        <strong><c:out value="${node.name()}"/></strong>
                      </td>
                      <td><c:out value="${node.sortOrder()}"/></td>
                      <td><c:out value="${node.faqCount()}"/></td>
                      <td><c:out value="${node.openTicketCount()}"/></td>
                      <td>
                        <c:choose>
                          <c:when test="${node.active()}"><span class="badge bg-green-lt">Đang hoạt động</span></c:when>
                          <c:otherwise><span class="badge bg-secondary-lt">Đã tắt</span></c:otherwise>
                        </c:choose>
                      </td>
                      <td>
                        <div class="btn-list justify-content-end flex-nowrap">
                          <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/categories/${node.id()}/edit">Sửa</a>
                          <c:url var="addChildUrl" value="/admin/categories/new">
                            <c:param name="parentId" value="${node.id()}"/>
                          </c:url>
                          <a class="btn btn-sm btn-outline-secondary" href="${addChildUrl}">Thêm chủ đề con</a>
                          <c:choose>
                            <c:when test="${node.active()}">
                              <button type="button" class="btn btn-sm btn-outline-danger"
                                      data-bs-toggle="modal" data-bs-target="#toggle-${node.id()}">Tắt</button>
                              <qa:confirmModal id="toggle-${node.id()}" title="Tắt chuyên mục"
                                               message="Tắt chuyên mục ${node.code()}? Sinh viên sẽ không chọn được chuyên mục này khi gửi yêu cầu mới."
                                               actionUrl="${ctx}/admin/categories/${node.id()}/toggle" confirmLabel="Tắt"/>
                            </c:when>
                            <c:otherwise>
                              <button type="button" class="btn btn-sm btn-outline-success"
                                      data-bs-toggle="modal" data-bs-target="#toggle-${node.id()}">Bật</button>
                              <qa:confirmModal id="toggle-${node.id()}" title="Bật lại chuyên mục"
                                               message="Bật lại chuyên mục ${node.code()}?"
                                               actionUrl="${ctx}/admin/categories/${node.id()}/toggle"
                                               confirmLabel="Bật" cssClass="btn-success"/>
                            </c:otherwise>
                          </c:choose>
                        </div>
                      </td>
                    </tr>

                    <%-- Chủ đề cấp 2 của chuyên mục này --%>
                    <c:forEach var="child" items="${node.children()}">
                      <tr>
                        <td><code><c:out value="${child.code()}"/></code></td>
                        <td>
                          <i class="bi bi-arrow-return-right text-secondary ms-3 me-2"></i>
                          <c:if test="${not empty child.icon()}"><i class="bi <c:out value='${child.icon()}'/> me-2"></i></c:if>
                          <c:out value="${child.name()}"/>
                        </td>
                        <td><c:out value="${child.sortOrder()}"/></td>
                        <td><c:out value="${child.faqCount()}"/></td>
                        <td><c:out value="${child.openTicketCount()}"/></td>
                        <td>
                          <c:choose>
                            <c:when test="${child.active()}"><span class="badge bg-green-lt">Đang hoạt động</span></c:when>
                            <c:otherwise><span class="badge bg-secondary-lt">Đã tắt</span></c:otherwise>
                          </c:choose>
                        </td>
                        <td>
                          <div class="btn-list justify-content-end flex-nowrap">
                            <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/categories/${child.id()}/edit">Sửa</a>
                            <c:choose>
                              <c:when test="${child.active()}">
                                <button type="button" class="btn btn-sm btn-outline-danger"
                                        data-bs-toggle="modal" data-bs-target="#toggle-${child.id()}">Tắt</button>
                                <qa:confirmModal id="toggle-${child.id()}" title="Tắt chủ đề"
                                                 message="Tắt chủ đề ${child.code()}? Sinh viên sẽ không chọn được chủ đề này khi gửi yêu cầu mới."
                                                 actionUrl="${ctx}/admin/categories/${child.id()}/toggle" confirmLabel="Tắt"/>
                              </c:when>
                              <c:otherwise>
                                <button type="button" class="btn btn-sm btn-outline-success"
                                        data-bs-toggle="modal" data-bs-target="#toggle-${child.id()}">Bật</button>
                                <qa:confirmModal id="toggle-${child.id()}" title="Bật lại chủ đề"
                                                 message="Bật lại chủ đề ${child.code()}?"
                                                 actionUrl="${ctx}/admin/categories/${child.id()}/toggle"
                                                 confirmLabel="Bật" cssClass="btn-success"/>
                              </c:otherwise>
                            </c:choose>
                          </div>
                        </td>
                      </tr>
                    </c:forEach>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </c:forEach>
    <p class="text-secondary small">
      Số FAQ và số yêu cầu đang mở của chuyên mục cấp 1 đã gồm cả các chủ đề con. Chuyên mục không bị xóa, chỉ được tắt để ẩn khỏi sinh viên.
    </p>
  </c:otherwise>
</c:choose>
