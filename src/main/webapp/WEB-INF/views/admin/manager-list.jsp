<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Manager và phạm vi</title>
<div class="qa-page-admin-manager-list" data-qa-view="admin-manager-list">
<style>
.qa-page-admin-manager-list { --page-accent: #258e89; --page-accent-dark: #1e6865; --page-tint: #eaf8f6; --page-line: #d2eeea; min-width: 0; color: #344c67; }
.qa-page-admin-manager-list > .page-header, .qa-page-admin-manager-list > .qa-page-heading, .qa-page-admin-manager-list > .d-flex:has(> .page-title), .qa-page-admin-manager-list > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #eaf8f6, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-manager-list > .page-header::after, .qa-page-admin-manager-list > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #d2eeea; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-manager-list .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-manager-list .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #d2eeea; border-radius: 12px; color: #1e6865; background: linear-gradient(145deg,#fff,#eaf8f6); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-manager-list .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-manager-list .card:hover { border-color: #d2eeea; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-manager-list .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#eaf8f6); }
.qa-page-admin-manager-list .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-manager-list .card-body { color: #435b75; }
.qa-page-admin-manager-list .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#eaf8f6); }
.qa-page-admin-manager-list .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-manager-list .form-control, .qa-page-admin-manager-list .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-manager-list textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-manager-list .form-control:focus, .qa-page-admin-manager-list .form-select:focus { border-color: #258e89; box-shadow: 0 0 0 4px #eaf8f6; }
.qa-page-admin-manager-list .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-manager-list .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-manager-list .btn-primary { border-color: #258e89; background: linear-gradient(120deg,#258e89,#1e6865); box-shadow: 0 5px 13px #d2eeea; }
.qa-page-admin-manager-list .btn-outline-primary { border-color: #d2eeea; color: #1e6865; background: #fff; }
.qa-page-admin-manager-list .btn-outline-primary:hover { border-color: #258e89; color: #fff; background: linear-gradient(120deg,#258e89,#1e6865); }
.qa-page-admin-manager-list .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#eaf8f6); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-manager-list .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-manager-list .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-manager-list .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#eaf8f6,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-manager-list .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-manager-list .table tbody tr:hover { background: #eaf8f6; }
.qa-page-admin-manager-list .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-manager-list .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-manager-list .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-manager-list .nav-tabs .nav-link.active { color: #1e6865; box-shadow: inset 0 -2px #258e89; }
.qa-page-admin-manager-list .nav-pills .nav-link.active { color: #1e6865; background: #eaf8f6; box-shadow: inset 0 0 0 1px #d2eeea; }
.qa-page-admin-manager-list .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-manager-list .progress-bar { border-radius: inherit; }
.qa-page-admin-manager-list .table-responsive { overflow: auto; border: 1px solid #e3ebf5; border-radius: 15px; background: #fff; box-shadow: 0 8px 24px rgba(16,48,84,.05); }
.qa-page-admin-manager-list .table { margin: 0; } .qa-page-admin-manager-list .table thead th { position: sticky; top: 0; z-index: 1; background: linear-gradient(110deg,var(--page-tint),#f7faff); }
.qa-page-admin-manager-list .table tbody tr { transition: background-color .18s ease; } .qa-page-admin-manager-list .table tbody tr:hover { background: var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-admin-manager-list > .page-header, .qa-page-admin-manager-list > .qa-page-heading, .qa-page-admin-manager-list > .d-flex:has(> .page-title), .qa-page-admin-manager-list > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-manager-list .card { border-radius: 15px; }
  .qa-page-admin-manager-list .card-body { padding: 1rem; }
  .qa-page-admin-manager-list .table-responsive { border-radius: 11px; }
  .qa-page-admin-manager-list .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-manager-list *, .qa-page-admin-manager-list *::before, .qa-page-admin-manager-list *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
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
