<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chuyên mục</title>
<div class="qa-page-admin-category-list" data-qa-view="admin-category-list">
<style>
.qa-page-admin-category-list { --page-accent: #cf7b27; --page-accent-dark: #98571b; --page-tint: #fff4e8; --page-line: #f5dfca; min-width: 0; color: #344c67; }
.qa-page-admin-category-list > .page-header, .qa-page-admin-category-list > .qa-page-heading, .qa-page-admin-category-list > .d-flex:has(> .page-title), .qa-page-admin-category-list > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff4e8, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-category-list > .page-header::after, .qa-page-admin-category-list > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f5dfca; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-category-list .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-category-list .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f5dfca; border-radius: 12px; color: #98571b; background: linear-gradient(145deg,#fff,#fff4e8); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-category-list .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-category-list .card:hover { border-color: #f5dfca; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-category-list .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff4e8); }
.qa-page-admin-category-list .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-category-list .card-body { color: #435b75; }
.qa-page-admin-category-list .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#fff4e8); }
.qa-page-admin-category-list .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-category-list .form-control, .qa-page-admin-category-list .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-category-list textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-category-list .form-control:focus, .qa-page-admin-category-list .form-select:focus { border-color: #cf7b27; box-shadow: 0 0 0 4px #fff4e8; }
.qa-page-admin-category-list .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-category-list .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-category-list .btn-primary { border-color: #cf7b27; background: linear-gradient(120deg,#cf7b27,#98571b); box-shadow: 0 5px 13px #f5dfca; }
.qa-page-admin-category-list .btn-outline-primary { border-color: #f5dfca; color: #98571b; background: #fff; }
.qa-page-admin-category-list .btn-outline-primary:hover { border-color: #cf7b27; color: #fff; background: linear-gradient(120deg,#cf7b27,#98571b); }
.qa-page-admin-category-list .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#fff4e8); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-category-list .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-category-list .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-category-list .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff4e8,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-category-list .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-category-list .table tbody tr:hover { background: #fff4e8; }
.qa-page-admin-category-list .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-category-list .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-category-list .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-category-list .nav-tabs .nav-link.active { color: #98571b; box-shadow: inset 0 -2px #cf7b27; }
.qa-page-admin-category-list .nav-pills .nav-link.active { color: #98571b; background: #fff4e8; box-shadow: inset 0 0 0 1px #f5dfca; }
.qa-page-admin-category-list .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-category-list .progress-bar { border-radius: inherit; }
.qa-page-admin-category-list .table-responsive { overflow: auto; border: 1px solid #e3ebf5; border-radius: 15px; background: #fff; box-shadow: 0 8px 24px rgba(16,48,84,.05); }
.qa-page-admin-category-list .table { margin: 0; } .qa-page-admin-category-list .table thead th { position: sticky; top: 0; z-index: 1; background: linear-gradient(110deg,var(--page-tint),#f7faff); }
.qa-page-admin-category-list .table tbody tr { transition: background-color .18s ease; } .qa-page-admin-category-list .table tbody tr:hover { background: var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-admin-category-list > .page-header, .qa-page-admin-category-list > .qa-page-heading, .qa-page-admin-category-list > .d-flex:has(> .page-title), .qa-page-admin-category-list > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-category-list .card { border-radius: 15px; }
  .qa-page-admin-category-list .card-body { padding: 1rem; }
  .qa-page-admin-category-list .table-responsive { border-radius: 11px; }
  .qa-page-admin-category-list .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-category-list *, .qa-page-admin-category-list *::before, .qa-page-admin-category-list *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%-- Dữ liệu từ AdminCategoryController.list: trees (AdminCategoryTree), departments (DepartmentOption), selectedDepartmentId --%>
<c:url var="newUrl" value="/admin/categories/new">
  <c:if test="${not empty selectedDepartmentId}">
    <c:param name="departmentId" value="${selectedDepartmentId}"/>
  </c:if>
</c:url>

<!-- Tiêu đề trang và nút thêm chuyên mục -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-category-list"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-diagram-3"></i></span>Quản lý chuyên mục</h2>
  <a class="btn btn-primary" href="${newUrl}"><i class="bi bi-plus-lg me-1"></i>Thêm chuyên mục</a>
</div>

<%-- Bộ lọc theo phòng ban --%>
<!-- Form lọc chuyên mục theo phòng ban -->
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

<!-- Hiển thị danh sách chuyên mục theo phòng ban -->
<c:choose>
  <c:when test="${empty trees}">
    <!-- Trạng thái trống: chưa có phòng ban -->
    <qa:emptyState icon="bi-diagram-3" title="Chưa có phòng ban"
                   message="Hãy tạo phòng ban trước khi thêm chuyên mục."
                   actionUrl="${ctx}/admin/departments/new" actionLabel="Thêm phòng ban"/>
  </c:when>
  <c:otherwise>
    <!-- Duyệt qua từng phòng ban và hiển thị cây chuyên mục -->
    <c:forEach var="tree" items="${trees}">
      <div class="card mb-3">
        <div class="card-header">
          <!-- Tên phòng ban và mã -->
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
            <!-- Bảng danh sách chuyên mục cấp 1 và cấp 2 -->
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
                      <!-- Các nút thao tác: sửa, thêm con, bật/tắt -->
                      <td>
                        <div class="btn-list justify-content-end flex-nowrap">
                          <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/categories/${node.id()}/edit">Sửa</a>
                          <c:url var="addChildUrl" value="/admin/categories/new">
                            <c:param name="parentId" value="${node.id()}"/>
                          </c:url>
                          <a class="btn btn-sm btn-outline-secondary" href="${addChildUrl}">Thêm chủ đề con</a>
                          <c:choose>
                            <c:when test="${node.active()}">
                              <qa:confirmModal id="toggle-${node.id()}" title="Tắt chuyên mục"
                                               message="Tắt chuyên mục ${node.code()}? Sinh viên sẽ không chọn được chuyên mục này khi gửi yêu cầu mới."
                                               actionUrl="${ctx}/admin/categories/${node.id()}/toggle"
                                               confirmLabel="Tắt" cssClass="btn-sm btn-danger"/>
                            </c:when>
                            <c:otherwise>
                              <qa:confirmModal id="toggle-${node.id()}" title="Bật lại chuyên mục"
                                               message="Bật lại chuyên mục ${node.code()}?"
                                               actionUrl="${ctx}/admin/categories/${node.id()}/toggle"
                                               confirmLabel="Bật" cssClass="btn-sm btn-success"/>
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
                        <!-- Các nút thao tác cho chủ đề con -->
                        <td>
                          <div class="btn-list justify-content-end flex-nowrap">
                            <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/categories/${child.id()}/edit">Sửa</a>
                            <c:choose>
                              <c:when test="${child.active()}">
                                <qa:confirmModal id="toggle-${child.id()}" title="Tắt chủ đề"
                                                 message="Tắt chủ đề ${child.code()}? Sinh viên sẽ không chọn được chủ đề này khi gửi yêu cầu mới."
                                                 actionUrl="${ctx}/admin/categories/${child.id()}/toggle"
                                                 confirmLabel="Tắt" cssClass="btn-sm btn-danger"/>
                              </c:when>
                              <c:otherwise>
                                <qa:confirmModal id="toggle-${child.id()}" title="Bật lại chủ đề"
                                                 message="Bật lại chủ đề ${child.code()}?"
                                                 actionUrl="${ctx}/admin/categories/${child.id()}/toggle"
                                                 confirmLabel="Bật" cssClass="btn-sm btn-success"/>
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
    <!-- Ghi chú về cách tính số liệu -->
    <p class="text-secondary small">
      Số FAQ và số yêu cầu đang mở của chuyên mục cấp 1 đã gồm cả các chủ đề con. Chuyên mục không bị xóa, chỉ được tắt để ẩn khỏi sinh viên.
    </p>
  </c:otherwise>
</c:choose>
</div>
