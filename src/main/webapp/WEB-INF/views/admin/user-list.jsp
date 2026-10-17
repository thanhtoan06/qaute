<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Danh sách người dùng</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0">Danh sách người dùng</h2>
  <div class="d-flex align-items-center gap-3"><span class="text-secondary small">${users.totalElements} tài khoản</span>
    <a class="btn btn-outline-primary" href="${ctx}/admin/users/import"><i class="bi bi-upload me-1"></i>Nhập CSV</a>
    <a class="btn btn-primary" href="${ctx}/admin/users/new"><i class="bi bi-plus-lg me-1"></i>Thêm người dùng</a></div>
</div>

<form method="get" action="${ctx}/admin/users" class="qa-filter-bar mb-3">
  <div>
    <label class="form-label" for="userSearch">Tìm kiếm</label>
    <input class="form-control" id="userSearch" name="q" type="search"
           value="<c:out value='${filter.q}'/>" maxlength="150"
           placeholder="Họ tên, email hoặc MSSV"/>
  </div>
  <div>
    <label class="form-label" for="roleFilter">Vai trò</label>
    <select class="form-select" id="roleFilter" name="role">
      <option value="">Tất cả vai trò</option>
      <c:forEach var="role" items="${roles}">
        <option value="${role}" ${filter.role == role ? 'selected' : ''}>
          <c:choose>
            <c:when test="${role == 'STUDENT'}">Sinh viên</c:when>
            <c:when test="${role == 'MANAGER'}">Tư vấn viên</c:when>
            <c:otherwise>Quản trị viên</c:otherwise>
          </c:choose>
        </option>
      </c:forEach>
    </select>
  </div>
  <div>
    <label class="form-label" for="statusFilter">Trạng thái</label>
    <select class="form-select" id="statusFilter" name="status">
      <option value="">Tất cả trạng thái</option>
      <c:forEach var="status" items="${statuses}">
        <option value="${status}" ${filter.status == status ? 'selected' : ''}><c:out value="${status}"/></option>
      </c:forEach>
    </select>
  </div>
  <div>
    <label class="form-label" for="fromFilter">Tạo từ ngày</label>
    <input class="form-control" id="fromFilter" name="from" type="date" value="${filter.from}"/>
  </div>
  <div>
    <label class="form-label" for="toFilter">Đến ngày</label>
    <input class="form-control" id="toFilter" name="to" type="date" value="${filter.to}"/>
  </div>
  <div>
    <label class="form-label" for="sortFilter">Sắp xếp</label>
    <select class="form-select" id="sortFilter" name="sort">
      <option value="createdAt" ${filter.sort == 'createdAt' or empty filter.sort ? 'selected' : ''}>Mới tạo nhất</option>
      <option value="fullName" ${filter.sort == 'fullName' ? 'selected' : ''}>Họ tên A–Z</option>
      <option value="lastLoginAt" ${filter.sort == 'lastLoginAt' ? 'selected' : ''}>Đăng nhập gần nhất</option>
    </select>
  </div>
  <div class="d-flex gap-2">
    <button class="btn btn-primary" type="submit"><i class="bi bi-search me-1"></i>Lọc</button>
    <a class="btn btn-outline-secondary" href="${ctx}/admin/users">Bỏ lọc</a>
  </div>
</form>

<div class="card">
  <div class="table-responsive">
    <table class="table table-vcenter card-table qa-admin-table">
      <thead>
        <tr>
          <th>Người dùng</th>
          <th>Vai trò</th>
          <th>Trạng thái</th>
          <th>Đăng nhập gần nhất</th>
          <th>Ngày tạo</th>
          <th class="text-end">Thao tác</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${empty users.content}">
            <tr><td colspan="6"><qa:emptyState icon="bi-people" title="Không có người dùng phù hợp"
                                                message="Hãy thử thay đổi điều kiện lọc."/></td></tr>
          </c:when>
          <c:otherwise>
            <c:forEach var="item" items="${users.content}">
              <tr>
                <td>
                  <div class="fw-semibold"><c:out value="${item.fullName()}"/></div>
                  <div class="text-secondary small"><c:out value="${item.email()}"/></div>
                  <c:if test="${not empty item.mssv()}"><div class="small">MSSV: <c:out value="${item.mssv()}"/></div></c:if>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${item.role() == 'STUDENT'}"><span class="badge bg-blue-lt">Sinh viên</span></c:when>
                    <c:when test="${item.role() == 'MANAGER'}"><span class="badge bg-purple-lt">Tư vấn viên</span></c:when>
                    <c:otherwise><span class="badge bg-orange-lt">Quản trị viên</span></c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${item.status() == 'ACTIVE'}"><span class="badge bg-green-lt">Hoạt động</span></c:when>
                    <c:when test="${item.status() == 'LOCKED'}"><span class="badge bg-red-lt">Đã khóa</span></c:when>
                    <c:when test="${item.status() == 'DEACTIVATED'}"><span class="badge bg-secondary-lt">Đã tắt</span></c:when>
                    <c:when test="${item.status() == 'PENDING_ACTIVATION'}"><span class="badge bg-yellow-lt">Chờ kích hoạt</span></c:when>
                    <c:otherwise><span class="badge bg-yellow-lt">Chờ xác minh</span></c:otherwise>
                  </c:choose>
                </td>
                <td><c:choose><c:when test="${not empty item.lastLoginAt()}"><c:out value="${item.lastLoginAt()}"/></c:when><c:otherwise><span class="text-secondary">Chưa đăng nhập</span></c:otherwise></c:choose></td>
                <td><c:out value="${item.createdAt()}"/></td>
                <td class="text-end"><a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/users/${item.id()}">Chi tiết</a></td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<qa:pagination page="${users}" baseUrl="${ctx}/admin/users"
               params="${paginationParams}"/>
