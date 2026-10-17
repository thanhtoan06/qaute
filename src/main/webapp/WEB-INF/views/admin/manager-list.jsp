<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Manager và phạm vi</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0">Manager và phạm vi phụ trách</h2>
</div>

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
                <td>
                  <div class="fw-semibold"><c:out value="${manager.fullName()}"/></div>
                  <div class="text-secondary small"><c:out value="${manager.email()}"/></div>
                  <c:if test="${manager.userStatus() != 'ACTIVE'}">
                    <span class="badge bg-secondary-lt mt-1"><c:out value="${manager.userStatus()}"/></span>
                  </c:if>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${not empty manager.departmentName()}"><c:out value="${manager.departmentName()}"/></c:when>
                    <c:otherwise><span class="text-secondary">Chưa gán</span></c:otherwise>
                  </c:choose>
                </td>
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
                <td><span class="badge bg-blue-lt"><c:out value="${manager.openTicketCount()}"/></span></td>
                <td>
                  <c:choose>
                    <c:when test="${manager.availabilityStatus() == 'ONLINE'}"><span class="badge bg-green-lt">Đang trực</span></c:when>
                    <c:when test="${manager.availabilityStatus() == 'BUSY'}"><span class="badge bg-yellow-lt">Đang bận</span></c:when>
                    <c:otherwise><span class="badge bg-secondary-lt">Ngoại tuyến</span></c:otherwise>
                  </c:choose>
                </td>
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
