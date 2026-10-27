<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Manager và phạm vi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/manager-list.css">
</head>
<div class="qa-page-admin-manager-list" data-qa-view="admin-manager-list">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-manager-list"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-people"></i></span>Manager và phạm vi phụ trách</h2>
</div>

<%-- Tìm kiếm Manager, cảnh báo danh mục chưa được phụ trách và mở cấu hình phạm vi. --%>
<!-- Form tìm kiếm Manager -->
<form method="get" action="${ctx}/admin/managers" class="qa-filter-bar mb-3">
  <div>
    <label class="form-label" for="managerSearch">Tìm Manager</label>
    <input class="form-control" id="managerSearch" name="q" type="search"
           value="<c:out value='${q}'/>" maxlength="150" placeholder="Tên hoặc email"/>
  </div>
  <div class="d-flex gap-2">
    <button class="btn btn-primary" type="submit"><i class="bi bi-search me-1"></i>Tìm</button>
    <a class="btn btn-outline-secondary" href="${ctx}/admin/managers">Bỏ lọc</a>
  </div>
</form>

<!-- Cảnh báo các chuyên mục chưa có Manager phụ trách -->
<c:if test="${not empty uncoveredCategories}">
  <div class="alert alert-warning" role="alert">
    <div class="d-flex gap-2">
      <i class="bi bi-exclamation-triangle-fill fs-3" aria-hidden="true"></i>
      <div>
        <h3 class="alert-title">Có ${fn:length(uncoveredCategories)} chuyên mục chưa có Manager phụ trách</h3>
        <p class="mb-2">Các chuyên mục này không có phân công trực tiếp và cũng không được bao phủ bởi chuyên mục cha.</p>
        <ul class="mb-0 ps-3">
          <c:forEach var="category" items="${uncoveredCategories}">
            <li>
              <c:if test="${not empty category.departmentName()}"><c:out value="${category.departmentName()}"/> — </c:if>
              <code><c:out value="${category.code()}"/></code>: <c:out value="${category.name()}"/>
            </li>
          </c:forEach>
        </ul>
      </div>
    </div>
  </div>
</c:if>

<!-- Bảng danh sách Manager và phạm vi phụ trách -->
<div class="card">
  <div class="table-responsive">
    <table class="table table-vcenter card-table qa-admin-table">
      <thead>
        <tr>
          <th>Manager</th>
          <th>Phòng ban</th>
          <th>Chuyên mục phụ trách</th>
          <th>Ticket đang mở</th>
          <th>Trạng thái trực</th>
          <th class="text-end">Thao tác</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${empty managers}">
            <tr>
              <td colspan="6" class="text-center text-secondary py-4">Không tìm thấy tài khoản Manager phù hợp.</td>
            </tr>
          </c:when>
          <c:otherwise>
            <c:forEach var="manager" items="${managers}">
              <tr>
                <!-- Thông tin Manager -->
                <td>
                  <div class="fw-semibold"><c:out value="${manager.fullName()}"/></div>
                  <div class="text-secondary small"><c:out value="${manager.email()}"/></div>
                  <c:if test="${manager.userStatus() != 'ACTIVE'}">
                    <span class="badge bg-secondary-lt mt-1"><c:out value="${manager.userStatus()}"/></span>
                  </c:if>
                </td>
                <!-- Phòng ban -->
                <td>
                  <c:choose>
                    <c:when test="${not empty manager.departmentName()}"><c:out value="${manager.departmentName()}"/></c:when>
                    <c:otherwise><span class="text-secondary">Chưa gán</span></c:otherwise>
                  </c:choose>
                </td>
                <!-- Chuyên mục phụ trách -->
                <td>
                  <c:choose>
                    <c:when test="${empty manager.categoryNames()}"><span class="text-secondary">Chưa gán</span></c:when>
                    <c:otherwise>
                      <c:forEach var="categoryName" items="${manager.categoryNames()}" varStatus="status">
                        <c:if test="${!status.first}"><span class="text-secondary">, </span></c:if><c:out value="${categoryName}"/>
                      </c:forEach>
                    </c:otherwise>
                  </c:choose>
                </td>
                <!-- Số ticket đang mở -->
                <td><span class="badge bg-blue-lt"><c:out value="${manager.openTicketCount()}"/></span></td>
                <!-- Trạng thái trực tuyến -->
                <td>
                  <c:choose>
                    <c:when test="${manager.availabilityStatus() == 'ONLINE'}"><span class="badge bg-green-lt">Đang trực</span></c:when>
                    <c:when test="${manager.availabilityStatus() == 'BUSY'}"><span class="badge bg-yellow-lt">Đang bận</span></c:when>
                    <c:otherwise><span class="badge bg-secondary-lt">Ngoại tuyến</span></c:otherwise>
                  </c:choose>
                </td>
                <!-- Nút thiết lập phạm vi -->
                <td class="text-end">
                  <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/managers/${manager.managerId()}/scope">
                    <i class="bi bi-sliders me-1"></i>Thiết lập phạm vi
                  </a>
                </td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<!-- Phân trang -->
<qa:pagination page="${resultPage}" baseUrl="${ctx}/admin/managers" params="${paginationParams}"/>
</div>
