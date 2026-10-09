<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Phòng ban</title>

<div class="d-flex justify-content-between align-items-center mb-3">
  <h2 class="page-title mb-0">Phòng ban</h2>
  <a href="${pageContext.request.contextPath}/admin/departments/new" class="btn btn-primary">
    <i class="bi bi-plus-lg me-1"></i>Thêm phòng ban
  </a>
</div>

<div class="card">
  <div class="table-responsive">
    <table class="table table-vcenter card-table">
      <thead>
        <tr>
          <th>Mã</th>
          <th>Tên phòng ban</th>
          <th>Liên hệ</th>
          <th class="text-center">Chuyên mục</th>
          <th>Trạng thái</th>
          <th class="text-end">Thao tác</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="department" items="${departments}">
          <tr>
            <td><code><c:out value="${department.code()}"/></code></td>
            <td>
              <div class="fw-bold"><c:out value="${department.name()}"/></div>
              <c:if test="${not empty department.description()}">
                <div class="text-secondary small"><c:out value="${department.description()}"/></div>
              </c:if>
              <c:if test="${not empty department.location()}">
                <div class="text-secondary small"><i class="bi bi-geo-alt me-1"></i><c:out value="${department.location()}"/></div>
              </c:if>
            </td>
            <td>
              <c:if test="${not empty department.email()}">
                <div class="small"><i class="bi bi-envelope me-1"></i><c:out value="${department.email()}"/></div>
              </c:if>
              <c:if test="${not empty department.phone()}">
                <div class="small"><i class="bi bi-telephone me-1"></i><c:out value="${department.phone()}"/></div>
              </c:if>
              <c:if test="${empty department.email() and empty department.phone()}">
                <span class="text-secondary">—</span>
              </c:if>
            </td>
            <td class="text-center"><c:out value="${department.categoryCount()}"/></td>
            <td>
              <c:choose>
                <c:when test="${department.active()}">
                  <span class="badge bg-green-lt">Đang hoạt động</span>
                </c:when>
                <c:otherwise>
                  <span class="badge bg-secondary-lt">Đã tắt</span>
                </c:otherwise>
              </c:choose>
            </td>
            <td class="text-end text-nowrap">
              <a href="${pageContext.request.contextPath}/admin/departments/${department.id()}/edit"
                 class="btn btn-sm btn-outline-primary">Sửa</a>
              <c:choose>
                <c:when test="${department.active()}">
                  <qa:confirmModal id="toggle-${department.id()}"
                                   title="Tắt phòng ban"
                                   message="Tắt phòng ban ${department.code()}? Các chuyên mục thuộc phòng ban sẽ không còn hiển thị cho sinh viên. Chỉ thực hiện được khi không còn yêu cầu đang xử lý."
                                   actionUrl="${pageContext.request.contextPath}/admin/departments/${department.id()}/toggle"
                                   confirmLabel="Tắt"/>
                </c:when>
                <c:otherwise>
                  <qa:confirmModal id="toggle-${department.id()}"
                                   title="Bật phòng ban"
                                   message="Bật lại phòng ban ${department.code()}?"
                                   actionUrl="${pageContext.request.contextPath}/admin/departments/${department.id()}/toggle"
                                   confirmLabel="Bật"
                                   cssClass="btn-success"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty departments}">
          <tr>
            <td colspan="6">
              <qa:emptyState icon="bi-diagram-3" title="Chưa có phòng ban"
                            message="Thêm phòng ban đầu tiên để bắt đầu xây dựng cây chuyên mục tư vấn."/>
            </td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>
</div>
