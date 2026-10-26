<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Phòng ban</title>
<div class="qa-page-admin-department-list" data-qa-view="admin-department-list">
<style>
.qa-page-admin-department-list { --page-accent: #cf7b27; --page-accent-dark: #98571b; --page-tint: #fff4e8; --page-line: #f5dfca; min-width: 0; color: #344c67; }
.qa-page-admin-department-list > .page-header, .qa-page-admin-department-list > .qa-page-heading, .qa-page-admin-department-list > .d-flex:has(> .page-title), .qa-page-admin-department-list > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff4e8, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-department-list > .page-header::after, .qa-page-admin-department-list > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f5dfca; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-department-list .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-department-list .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f5dfca; border-radius: 12px; color: #98571b; background: linear-gradient(145deg,#fff,#fff4e8); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-department-list .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-department-list .card:hover { border-color: #f5dfca; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-department-list .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff4e8); }
.qa-page-admin-department-list .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-department-list .card-body { color: #435b75; }
.qa-page-admin-department-list .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#fff4e8); }
.qa-page-admin-department-list .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-department-list .form-control, .qa-page-admin-department-list .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-department-list textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-department-list .form-control:focus, .qa-page-admin-department-list .form-select:focus { border-color: #cf7b27; box-shadow: 0 0 0 4px #fff4e8; }
.qa-page-admin-department-list .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-department-list .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-department-list .btn-primary { border-color: #cf7b27; background: linear-gradient(120deg,#cf7b27,#98571b); box-shadow: 0 5px 13px #f5dfca; }
.qa-page-admin-department-list .btn-outline-primary { border-color: #f5dfca; color: #98571b; background: #fff; }
.qa-page-admin-department-list .btn-outline-primary:hover { border-color: #cf7b27; color: #fff; background: linear-gradient(120deg,#cf7b27,#98571b); }
.qa-page-admin-department-list .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#fff4e8); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-department-list .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-department-list .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-department-list .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff4e8,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-department-list .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-department-list .table tbody tr:hover { background: #fff4e8; }
.qa-page-admin-department-list .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-department-list .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-department-list .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-department-list .nav-tabs .nav-link.active { color: #98571b; box-shadow: inset 0 -2px #cf7b27; }
.qa-page-admin-department-list .nav-pills .nav-link.active { color: #98571b; background: #fff4e8; box-shadow: inset 0 0 0 1px #f5dfca; }
.qa-page-admin-department-list .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-department-list .progress-bar { border-radius: inherit; }
.qa-page-admin-department-list .table-responsive { overflow: auto; border: 1px solid #e3ebf5; border-radius: 15px; background: #fff; box-shadow: 0 8px 24px rgba(16,48,84,.05); }
.qa-page-admin-department-list .table { margin: 0; } .qa-page-admin-department-list .table thead th { position: sticky; top: 0; z-index: 1; background: linear-gradient(110deg,var(--page-tint),#f7faff); }
.qa-page-admin-department-list .table tbody tr { transition: background-color .18s ease; } .qa-page-admin-department-list .table tbody tr:hover { background: var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-admin-department-list > .page-header, .qa-page-admin-department-list > .qa-page-heading, .qa-page-admin-department-list > .d-flex:has(> .page-title), .qa-page-admin-department-list > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-department-list .card { border-radius: 15px; }
  .qa-page-admin-department-list .card-body { padding: 1rem; }
  .qa-page-admin-department-list .table-responsive { border-radius: 11px; }
  .qa-page-admin-department-list .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-department-list *, .qa-page-admin-department-list *::before, .qa-page-admin-department-list *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>

<!-- Tiêu đề trang và nút thêm phòng ban -->
<div class="d-flex justify-content-between align-items-center mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-department-list"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-buildings"></i></span>Phòng ban</h2>
  <a href="${pageContext.request.contextPath}/admin/departments/new" class="btn btn-primary">
    <i class="bi bi-plus-lg me-1"></i>Thêm phòng ban
  </a>
</div>

<%-- Liệt kê phòng ban và cung cấp thao tác sửa, bật hoặc tắt. --%>
<!-- Bảng danh sách phòng ban -->
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
            <!-- Mã phòng ban -->
            <td><code><c:out value="${department.code()}"/></code></td>
            <!-- Tên, mô tả và địa điểm -->
            <td>
              <div class="fw-bold"><c:out value="${department.name()}"/></div>
              <c:if test="${not empty department.description()}">
                <div class="text-secondary small"><c:out value="${department.description()}"/></div>
              </c:if>
              <c:if test="${not empty department.location()}">
                <div class="text-secondary small"><i class="bi bi-geo-alt me-1"></i><c:out value="${department.location()}"/></div>
              </c:if>
            </td>
            <!-- Email và điện thoại liên hệ -->
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
            <!-- Số chuyên mục -->
            <td class="text-center"><c:out value="${department.categoryCount()}"/></td>
            <!-- Trạng thái hoạt động -->
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
            <!-- Các nút thao tác: sửa, bật/tắt -->
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
        <!-- Trạng thái trống -->
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

<!-- Phân trang -->
<qa:pagination page="${resultPage}" baseUrl="${pageContext.request.contextPath}/admin/departments"/>
</div>
