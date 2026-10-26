<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Danh sách người dùng</title>
<div class="qa-page-admin-user-list" data-qa-view="admin-user-list">
<style>
.qa-page-admin-user-list { --page-accent: #3579c7; --page-accent-dark: #285b94; --page-tint: #edf5ff; --page-line: #d6e6fa; min-width: 0; color: #344c67; }
.qa-page-admin-user-list > .page-header, .qa-page-admin-user-list > .qa-page-heading, .qa-page-admin-user-list > .d-flex:has(> .page-title), .qa-page-admin-user-list > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #edf5ff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-user-list > .page-header::after, .qa-page-admin-user-list > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #d6e6fa; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-user-list .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-user-list .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #d6e6fa; border-radius: 12px; color: #285b94; background: linear-gradient(145deg,#fff,#edf5ff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-user-list .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-user-list .card:hover { border-color: #d6e6fa; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-user-list .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#edf5ff); }
.qa-page-admin-user-list .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-user-list .card-body { color: #435b75; }
.qa-page-admin-user-list .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#edf5ff); }
.qa-page-admin-user-list .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-user-list .form-control, .qa-page-admin-user-list .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-user-list textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-user-list .form-control:focus, .qa-page-admin-user-list .form-select:focus { border-color: #3579c7; box-shadow: 0 0 0 4px #edf5ff; }
.qa-page-admin-user-list .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-user-list .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-user-list .btn-primary { border-color: #3579c7; background: linear-gradient(120deg,#3579c7,#285b94); box-shadow: 0 5px 13px #d6e6fa; }
.qa-page-admin-user-list .btn-outline-primary { border-color: #d6e6fa; color: #285b94; background: #fff; }
.qa-page-admin-user-list .btn-outline-primary:hover { border-color: #3579c7; color: #fff; background: linear-gradient(120deg,#3579c7,#285b94); }
.qa-page-admin-user-list .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#edf5ff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-user-list .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-user-list .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-user-list .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#edf5ff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-user-list .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-user-list .table tbody tr:hover { background: #edf5ff; }
.qa-page-admin-user-list .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-user-list .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-user-list .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-user-list .nav-tabs .nav-link.active { color: #285b94; box-shadow: inset 0 -2px #3579c7; }
.qa-page-admin-user-list .nav-pills .nav-link.active { color: #285b94; background: #edf5ff; box-shadow: inset 0 0 0 1px #d6e6fa; }
.qa-page-admin-user-list .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-user-list .progress-bar { border-radius: inherit; }
.qa-page-admin-user-list .table-responsive { overflow: auto; border: 1px solid #e3ebf5; border-radius: 15px; background: #fff; box-shadow: 0 8px 24px rgba(16,48,84,.05); }
.qa-page-admin-user-list .table { margin: 0; } .qa-page-admin-user-list .table thead th { position: sticky; top: 0; z-index: 1; background: linear-gradient(110deg,var(--page-tint),#f7faff); }
.qa-page-admin-user-list .table tbody tr { transition: background-color .18s ease; } .qa-page-admin-user-list .table tbody tr:hover { background: var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-admin-user-list > .page-header, .qa-page-admin-user-list > .qa-page-heading, .qa-page-admin-user-list > .d-flex:has(> .page-title), .qa-page-admin-user-list > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-user-list .card { border-radius: 15px; }
  .qa-page-admin-user-list .card-body { padding: 1rem; }
  .qa-page-admin-user-list .table-responsive { border-radius: 11px; }
  .qa-page-admin-user-list .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-user-list *, .qa-page-admin-user-list *::before, .qa-page-admin-user-list *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và các nút hành động -->
<div class="qa-page-heading">
  <div><span class="qa-page-eyebrow">Quản trị tài khoản</span><h1 class="page-title mb-0 qa-view-title" data-qa-page="admin-user-list"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-person-vcard"></i></span>Danh sách người dùng</h1><p>Quản lý thông tin và quyền truy cập của ${users.totalElements} tài khoản.</p></div>
  <div class="qa-page-heading-actions">
    <a class="btn btn-outline-primary" href="${ctx}/admin/users/import"><i class="bi bi-upload me-1"></i>Nhập CSV</a>
    <a class="btn btn-primary" href="${ctx}/admin/users/new"><i class="bi bi-plus-lg me-1"></i>Thêm người dùng</a></div>
</div>

<!-- Form lọc người dùng -->
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

<!-- Bảng danh sách người dùng -->
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
                <!-- Thông tin người dùng -->
                <td>
                  <div class="fw-semibold"><c:out value="${item.fullName()}"/></div>
                  <div class="text-secondary small"><c:out value="${item.email()}"/></div>
                  <c:if test="${not empty item.mssv()}"><div class="small">MSSV: <c:out value="${item.mssv()}"/></div></c:if>
                </td>
                <!-- Vai trò -->
                <td>
                  <c:choose>
                    <c:when test="${item.role() == 'STUDENT'}"><span class="badge bg-blue-lt">Sinh viên</span></c:when>
                    <c:when test="${item.role() == 'MANAGER'}"><span class="badge bg-purple-lt">Tư vấn viên</span></c:when>
                    <c:otherwise><span class="badge bg-orange-lt">Quản trị viên</span></c:otherwise>
                  </c:choose>
                </td>
                <!-- Trạng thái -->
                <td>
                  <c:choose>
                    <c:when test="${item.status() == 'ACTIVE'}"><span class="badge bg-green-lt">Hoạt động</span></c:when>
                    <c:when test="${item.status() == 'LOCKED'}"><span class="badge bg-red-lt">Đã khóa</span></c:when>
                    <c:when test="${item.status() == 'DEACTIVATED'}"><span class="badge bg-secondary-lt">Đã tắt</span></c:when>
                    <c:when test="${item.status() == 'PENDING_ACTIVATION'}"><span class="badge bg-yellow-lt">Chờ kích hoạt</span></c:when>
                    <c:otherwise><span class="badge bg-yellow-lt">Chờ xác minh</span></c:otherwise>
                  </c:choose>
                </td>
                <td><c:choose><c:when test="${not empty item.lastLoginAt()}"><time datetime="${item.lastLoginAt()}" data-workspace-datetime><c:out value="${item.lastLoginAt()}"/></time></c:when><c:otherwise><span class="text-secondary">Chưa đăng nhập</span></c:otherwise></c:choose></td>
                <td><time datetime="${item.createdAt()}" data-workspace-datetime><c:out value="${item.createdAt()}"/></time></td>
                <!-- Các nút thao tác -->
                <td class="text-end"><div class="d-flex flex-wrap gap-1 justify-content-end"><a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/users/${item.id()}">Chi tiết</a><a class="btn btn-sm btn-outline-secondary" href="${ctx}/admin/users/${item.id()}/password"><i class="bi bi-key me-1"></i>Đổi mật khẩu</a></div></td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<!-- Phân trang -->
<qa:pagination page="${users}" baseUrl="${ctx}/admin/users"
               params="${paginationParams}"/>
</div>
