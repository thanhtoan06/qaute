<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Phạm vi phụ trách</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/manager-scope.css">
</head>
<div class="qa-page-admin-manager-scope" data-qa-view="admin-manager-scope">

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
