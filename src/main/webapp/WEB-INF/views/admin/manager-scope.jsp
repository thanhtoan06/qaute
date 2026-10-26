<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Phạm vi phụ trách</title>
<div class="qa-page-admin-manager-scope" data-qa-view="admin-manager-scope">
<style>
.qa-page-admin-manager-scope { --page-accent: #258e89; --page-accent-dark: #1e6865; --page-tint: #eaf8f6; --page-line: #d2eeea; min-width: 0; color: #344c67; }
.qa-page-admin-manager-scope > .page-header, .qa-page-admin-manager-scope > .qa-page-heading, .qa-page-admin-manager-scope > .d-flex:has(> .page-title), .qa-page-admin-manager-scope > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #eaf8f6, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-manager-scope > .page-header::after, .qa-page-admin-manager-scope > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #d2eeea; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-manager-scope .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-manager-scope .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #d2eeea; border-radius: 12px; color: #1e6865; background: linear-gradient(145deg,#fff,#eaf8f6); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-manager-scope .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-manager-scope .card:hover { border-color: #d2eeea; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-manager-scope .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#eaf8f6); }
.qa-page-admin-manager-scope .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-manager-scope .card-body { color: #435b75; }
.qa-page-admin-manager-scope .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#eaf8f6); }
.qa-page-admin-manager-scope .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-manager-scope .form-control, .qa-page-admin-manager-scope .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-manager-scope textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-manager-scope .form-control:focus, .qa-page-admin-manager-scope .form-select:focus { border-color: #258e89; box-shadow: 0 0 0 4px #eaf8f6; }
.qa-page-admin-manager-scope .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-manager-scope .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-manager-scope .btn-primary { border-color: #258e89; background: linear-gradient(120deg,#258e89,#1e6865); box-shadow: 0 5px 13px #d2eeea; }
.qa-page-admin-manager-scope .btn-outline-primary { border-color: #d2eeea; color: #1e6865; background: #fff; }
.qa-page-admin-manager-scope .btn-outline-primary:hover { border-color: #258e89; color: #fff; background: linear-gradient(120deg,#258e89,#1e6865); }
.qa-page-admin-manager-scope .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#eaf8f6); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-manager-scope .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-manager-scope .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-manager-scope .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#eaf8f6,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-manager-scope .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-manager-scope .table tbody tr:hover { background: #eaf8f6; }
.qa-page-admin-manager-scope .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-manager-scope .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-manager-scope .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-manager-scope .nav-tabs .nav-link.active { color: #1e6865; box-shadow: inset 0 -2px #258e89; }
.qa-page-admin-manager-scope .nav-pills .nav-link.active { color: #1e6865; background: #eaf8f6; box-shadow: inset 0 0 0 1px #d2eeea; }
.qa-page-admin-manager-scope .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-manager-scope .progress-bar { border-radius: inherit; }
.qa-page-admin-manager-scope .card-header { background-image: linear-gradient(90deg,var(--page-accent),transparent 72%); background-repeat: no-repeat; background-size: 100% 2px; background-position: top; }
@media (max-width: 767.98px) {
  .qa-page-admin-manager-scope > .page-header, .qa-page-admin-manager-scope > .qa-page-heading, .qa-page-admin-manager-scope > .d-flex:has(> .page-title), .qa-page-admin-manager-scope > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-manager-scope .card { border-radius: 15px; }
  .qa-page-admin-manager-scope .card-body { padding: 1rem; }
  .qa-page-admin-manager-scope .table-responsive { border-radius: 11px; }
  .qa-page-admin-manager-scope .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-manager-scope *, .qa-page-admin-manager-scope *::before, .qa-page-admin-manager-scope *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và nút quay lại -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <div>
    <h2 class="page-title mb-1 qa-view-title" data-qa-page="admin-manager-scope"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-people"></i></span>Phạm vi phụ trách</h2>
    <div class="text-secondary"><c:out value="${manager.fullName()}"/> · <c:out value="${manager.email()}"/></div>
  </div>
  <a class="btn btn-outline-secondary" href="${ctx}/admin/managers"><i class="bi bi-arrow-left me-1"></i>Danh sách Manager</a>
</div>

<!-- Thông báo lỗi toàn cục -->
<c:if test="${not empty errors.global}">
  <div class="alert alert-danger" role="alert"><c:out value="${errors.global}"/></div>
</c:if>

<!-- Form thiết lập phạm vi phụ trách Manager -->
<form method="post" action="${ctx}/admin/managers/${manager.managerId()}/scope" id="managerScopeForm" novalidate>
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  <input type="hidden" name="managerId" value="${manager.managerId()}"/>

  <div class="row">
    <div class="col-lg-5">
      <!-- Hồ sơ Manager -->
      <div class="card mb-3">
        <div class="card-header"><h3 class="card-title">Hồ sơ Manager</h3></div>
        <div class="card-body">
          <!-- Chọn phòng ban -->
          <div class="mb-3">
            <label class="form-label" for="departmentId">Phòng ban</label>
            <select class="form-select${not empty errors.departmentId ? ' is-invalid' : ''}" id="departmentId" name="departmentId">
              <option value="">-- Chưa gán phòng ban --</option>
              <c:forEach var="department" items="${departments}">
                <option value="${department.id()}" ${form.departmentId == department.id() ? 'selected' : ''}>
                  <c:out value="${department.name()}"/><c:if test="${!department.active()}"> (đã tắt)</c:if>
                </option>
              </c:forEach>
            </select>
            <qa:formError field="departmentId"/>
          </div>

          <!-- Nhập chức danh -->
          <div class="mb-3">
            <label class="form-label" for="jobTitle">Chức danh</label>
            <input class="form-control${not empty errors.jobTitle ? ' is-invalid' : ''}" id="jobTitle" name="jobTitle"
                   value="<c:out value='${form.jobTitle}'/>" maxlength="100"/>
            <qa:formError field="jobTitle"/>
          </div>

          <!-- Nhập mã nhân viên -->
          <div class="mb-3">
            <label class="form-label" for="employeeCode">Mã nhân viên</label>
            <input class="form-control${not empty errors.employeeCode ? ' is-invalid' : ''}" id="employeeCode" name="employeeCode"
                   value="<c:out value='${form.employeeCode}'/>" maxlength="30"/>
            <qa:formError field="employeeCode"/>
          </div>

          <!-- Nhập giới thiệu -->
          <div class="mb-3">
            <label class="form-label" for="bio">Giới thiệu</label>
            <textarea class="form-control${not empty errors.bio ? ' is-invalid' : ''}" id="bio" name="bio" rows="4" maxlength="500"><c:out value="${form.bio}"/></textarea>
            <qa:formError field="bio"/>
          </div>

          <!-- Thiết lập giới hạn ticket và chat đồng thời -->
          <div class="row">
            <div class="col-md-6 mb-3">
              <label class="form-label required" for="maxConcurrentTickets">Ticket đồng thời</label>
              <input class="form-control${not empty errors.maxConcurrentTickets ? ' is-invalid' : ''}" id="maxConcurrentTickets"
                     name="maxConcurrentTickets" type="number" min="1" max="50" required value="${form.maxConcurrentTickets}"/>
              <div class="form-hint">Từ 1 đến 50 ticket.</div>
              <qa:formError field="maxConcurrentTickets"/>
            </div>
            <div class="col-md-6 mb-3">
              <label class="form-label required" for="maxConcurrentChats">Chat đồng thời</label>
              <input class="form-control${not empty errors.maxConcurrentChats ? ' is-invalid' : ''}" id="maxConcurrentChats"
                     name="maxConcurrentChats" type="number" min="1" max="10" required value="${form.maxConcurrentChats}"/>
              <div class="form-hint">Từ 1 đến 10 phiên chat.</div>
              <qa:formError field="maxConcurrentChats"/>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="col-lg-7">
      <!-- Chuyên mục phụ trách -->
      <div class="card mb-3">
        <div class="card-header">
          <div>
            <h3 class="card-title mb-1">Chuyên mục phụ trách</h3>
            <div class="text-secondary small">Chọn chuyên mục chính nếu Manager là đầu mối ưu tiên. Chọn chuyên mục cha sẽ tự chọn các chuyên mục con, nhưng bạn vẫn có thể bỏ chọn từng mục con.</div>
          </div>
        </div>
        <div class="card-body">
          <!-- Thông báo lỗi chuyên mục -->
          <c:if test="${not empty errors.categoryIds or not empty errors.primaryCategoryIds or not empty errors.primaryCategoryIdsSubset}">
            <div class="alert alert-danger py-2" role="alert">
              <c:out value="${not empty errors.categoryIds ? errors.categoryIds : (not empty errors.primaryCategoryIds ? errors.primaryCategoryIds : errors.primaryCategoryIdsSubset)}"/>
            </div>
          </c:if>
          <c:choose>
            <c:when test="${empty departmentTrees}">
              <p class="text-secondary mb-0">Chưa có chuyên mục đang hoạt động để phân công.</p>
            </c:when>
            <c:otherwise>
              <!-- Danh sách chuyên mục theo phòng ban -->
              <c:forEach var="departmentTree" items="${departmentTrees}">
                <section class="mb-4" data-scope-department="${departmentTree.departmentId()}">
                  <h4 class="h4 mb-2">
                    <c:out value="${departmentTree.departmentName()}"/>
                    <c:if test="${!departmentTree.departmentActive()}"><span class="badge bg-secondary-lt ms-1">phòng ban đã tắt</span></c:if>
                  </h4>
                  <div class="border rounded">
                    <c:forEach var="category" items="${departmentTree.categories()}">
                      <!-- Chuyên mục cấp 1 -->
                      <div class="p-3${not empty category.children() ? ' border-bottom' : ''}">
                        <div class="d-flex align-items-center justify-content-between gap-3">
                          <label class="form-check mb-0 flex-grow-1" for="category-${category.categoryId()}">
                            <input class="form-check-input js-scope-category js-parent-category" type="checkbox"
                                   id="category-${category.categoryId()}" name="categoryIds" value="${category.categoryId()}"
                                   data-category-id="${category.categoryId()}"
                                   ${form.categoryIds.contains(category.categoryId()) ? 'checked' : ''}/>
                            <span class="form-check-label"><strong><c:out value="${category.name()}"/></strong> <code class="ms-1"><c:out value="${category.code()}"/></code></span>
                          </label>
                          <label class="form-check form-switch mb-0" for="primary-${category.categoryId()}">
                            <input class="form-check-input js-primary-category" type="checkbox"
                                   id="primary-${category.categoryId()}" name="primaryCategoryIds" value="${category.categoryId()}"
                                   data-category-id="${category.categoryId()}"
                                   ${form.primaryCategoryIds.contains(category.categoryId()) ? 'checked' : ''}
                                   ${form.categoryIds.contains(category.categoryId()) ? '' : 'disabled'}/>
                            <span class="form-check-label small">Phụ trách chính</span>
                          </label>
                        </div>

                        <!-- Chuyên mục cấp 2 -->
                        <c:forEach var="child" items="${category.children()}">
                          <div class="d-flex align-items-center justify-content-between gap-3 mt-2 ms-4">
                            <label class="form-check mb-0 flex-grow-1" for="category-${child.categoryId()}">
                              <input class="form-check-input js-scope-category js-child-category" type="checkbox"
                                     id="category-${child.categoryId()}" name="categoryIds" value="${child.categoryId()}"
                                     data-category-id="${child.categoryId()}" data-parent-id="${category.categoryId()}"
                                     ${form.categoryIds.contains(child.categoryId()) ? 'checked' : ''}/>
                              <span class="form-check-label"><i class="bi bi-arrow-return-right text-secondary me-1"></i><c:out value="${child.name()}"/> <code class="ms-1"><c:out value="${child.code()}"/></code></span>
                            </label>
                            <label class="form-check form-switch mb-0" for="primary-${child.categoryId()}">
                              <input class="form-check-input js-primary-category" type="checkbox"
                                     id="primary-${child.categoryId()}" name="primaryCategoryIds" value="${child.categoryId()}"
                                     data-category-id="${child.categoryId()}"
                                     ${form.primaryCategoryIds.contains(child.categoryId()) ? 'checked' : ''}
                                     ${form.categoryIds.contains(child.categoryId()) ? '' : 'disabled'}/>
                              <span class="form-check-label small">Phụ trách chính</span>
                            </label>
                          </div>
                        </c:forEach>
                      </div>
                    </c:forEach>
                  </div>
                </section>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </div>
  </div>

  <!-- Nút lưu và hủy -->
  <div class="d-flex gap-2 mb-4">
    <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg me-1"></i>Lưu phạm vi</button>
    <a class="btn btn-outline-secondary" href="${ctx}/admin/managers">Hủy</a>
  </div>
</form>

<script src="${ctx}/assets/js/admin/manager-scope.js" defer></script>
</div>
