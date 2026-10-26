<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chính sách SLA</title>
<div class="qa-page-admin-sla" data-qa-view="admin-sla">
<style>
.qa-page-admin-sla { --page-accent: #c58219; --page-accent-dark: #96600f; --page-tint: #fff6e6; --page-line: #f4e4c7; min-width: 0; color: #344c67; }
.qa-page-admin-sla > .page-header, .qa-page-admin-sla > .qa-page-heading, .qa-page-admin-sla > .d-flex:has(> .page-title), .qa-page-admin-sla > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff6e6, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-sla > .page-header::after, .qa-page-admin-sla > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f4e4c7; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-sla .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-sla .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f4e4c7; border-radius: 12px; color: #96600f; background: linear-gradient(145deg,#fff,#fff6e6); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-sla .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-sla .card:hover { border-color: #f4e4c7; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-sla .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff6e6); }
.qa-page-admin-sla .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-sla .card-body { color: #435b75; }
.qa-page-admin-sla .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#fff6e6); }
.qa-page-admin-sla .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-sla .form-control, .qa-page-admin-sla .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-sla textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-sla .form-control:focus, .qa-page-admin-sla .form-select:focus { border-color: #c58219; box-shadow: 0 0 0 4px #fff6e6; }
.qa-page-admin-sla .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-sla .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-sla .btn-primary { border-color: #c58219; background: linear-gradient(120deg,#c58219,#96600f); box-shadow: 0 5px 13px #f4e4c7; }
.qa-page-admin-sla .btn-outline-primary { border-color: #f4e4c7; color: #96600f; background: #fff; }
.qa-page-admin-sla .btn-outline-primary:hover { border-color: #c58219; color: #fff; background: linear-gradient(120deg,#c58219,#96600f); }
.qa-page-admin-sla .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#fff6e6); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-sla .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-sla .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-sla .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff6e6,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-sla .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-sla .table tbody tr:hover { background: #fff6e6; }
.qa-page-admin-sla .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-sla .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-sla .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-sla .nav-tabs .nav-link.active { color: #96600f; box-shadow: inset 0 -2px #c58219; }
.qa-page-admin-sla .nav-pills .nav-link.active { color: #96600f; background: #fff6e6; box-shadow: inset 0 0 0 1px #f4e4c7; }
.qa-page-admin-sla .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-sla .progress-bar { border-radius: inherit; }
.qa-page-admin-sla .card-header { background-image: linear-gradient(90deg,var(--page-accent),transparent 72%); background-repeat: no-repeat; background-size: 100% 2px; background-position: top; }
@media (max-width: 767.98px) {
  .qa-page-admin-sla > .page-header, .qa-page-admin-sla > .qa-page-heading, .qa-page-admin-sla > .d-flex:has(> .page-title), .qa-page-admin-sla > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-sla .card { border-radius: 15px; }
  .qa-page-admin-sla .card-body { padding: 1rem; }
  .qa-page-admin-sla .table-responsive { border-radius: 11px; }
  .qa-page-admin-sla .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-sla *, .qa-page-admin-sla *::before, .qa-page-admin-sla *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang -->
<h2 class="page-title mb-3 qa-view-title" data-qa-page="admin-sla"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-stopwatch"></i></span>Chính sách SLA</h2>
<div class="alert alert-info">Thay đổi chính sách chỉ áp dụng cho ticket tạo mới.</div>
<c:if test="${not empty formError}"><div class="alert alert-danger"><c:out value="${formError}"/></div></c:if>

<div class="card">
  <!-- Tab: Chính sách, Giờ làm việc, Ngày nghỉ -->
  <div class="card-header p-0">
    <div class="nav nav-tabs card-header-tabs px-3" role="tablist">
      <button class="nav-link ${activeTab eq 'policy' ? 'active' : ''}" type="button" data-bs-toggle="tab" data-bs-target="#sla-policy" role="tab" aria-selected="true">Chính sách</button>
      <button class="nav-link ${activeTab eq 'hours' ? 'active' : ''}" type="button" data-bs-toggle="tab" data-bs-target="#sla-hours" role="tab" aria-selected="false">Giờ làm việc</button>
      <button class="nav-link ${activeTab eq 'holidays' ? 'active' : ''}" type="button" data-bs-toggle="tab" data-bs-target="#sla-holidays" role="tab" aria-selected="false">Ngày nghỉ</button>
    </div>
  </div>
  <div class="tab-content">
    <!-- Tab Chính sách -->
    <div class="tab-pane fade ${activeTab eq 'policy' ? 'show active' : ''}" id="sla-policy" role="tabpanel">
      <div class="card-body border-bottom">
        <h3 class="card-title">Mặc định toàn hệ thống</h3>
        <div class="table-responsive">
          <table class="table table-vcenter mb-0">
            <thead><tr><th>Ưu tiên</th><th>Phản hồi đầu tiên</th><th>Giải quyết</th><th>Trạng thái</th></tr></thead>
            <tbody>
              <c:forEach var="policy" items="${matrix.defaults()}">
                <tr>
                  <td><c:out value="${policy.priority().label}"/></td>
                  <td><c:out value="${policy.firstResponseHours()}"/> giờ</td>
                  <td><c:out value="${policy.resolutionHours()}"/> giờ</td>
                  <td><c:choose><c:when test="${policy.active()}"><span class="badge bg-green-lt">Bật</span></c:when><c:otherwise><span class="badge bg-secondary-lt">Tắt</span></c:otherwise></c:choose></td>
                </tr>
              </c:forEach>
              <c:if test="${empty matrix.defaults()}"><tr><td colspan="4" class="text-secondary text-center">Chưa có chính sách mặc định.</td></tr></c:if>
            </tbody>
          </table>
        </div>
      </div>

      <div class="card-body border-bottom">
        <h3 class="card-title">Ghi đè theo chuyên mục</h3>
        <div class="table-responsive">
          <table class="table table-vcenter mb-0">
            <thead><tr><th>Chuyên mục</th><th>Ưu tiên</th><th>Phản hồi đầu tiên</th><th>Giải quyết</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
            <tbody>
              <c:forEach var="override" items="${matrix.overrides()}">
                <c:forEach var="policy" items="${override.rows()}" varStatus="rowStatus">
                  <tr>
                    <c:if test="${rowStatus.first}">
                      <td rowspan="${fn:length(override.rows())}"><c:out value="${override.categoryName()}"/></td>
                    </c:if>
                    <td><c:out value="${policy.priority().label}"/></td>
                    <td><c:out value="${policy.firstResponseHours()}"/> giờ</td>
                    <td><c:out value="${policy.resolutionHours()}"/> giờ</td>
                    <td><c:choose><c:when test="${policy.active()}"><span class="badge bg-green-lt">Bật</span></c:when><c:otherwise><span class="badge bg-secondary-lt">Tắt</span></c:otherwise></c:choose></td>
                    <c:if test="${rowStatus.first}">
                      <td rowspan="${fn:length(override.rows())}">
                        <div class="d-flex flex-wrap gap-1">
                          <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/sla/policy?categoryId=${override.categoryId()}">Sửa</a>
                          <qa:confirmModal id="delete-sla-${override.categoryId()}" title="Xóa ghi đè SLA"
                                           message="Xóa chính sách ghi đè của chuyên mục này? Chính sách mặc định sẽ được áp dụng cho ticket mới."
                                           actionUrl="${ctx}/admin/sla/policy/${override.categoryId()}/delete"
                                           confirmLabel="Xóa"/>
                        </div>
                      </td>
                    </c:if>
                  </tr>
                </c:forEach>
              </c:forEach>
              <c:if test="${empty matrix.overrides()}"><tr><td colspan="6" class="text-secondary text-center">Chưa có chính sách ghi đè.</td></tr></c:if>
            </tbody>
          </table>
        </div>
      </div>

      <div class="card-body border-bottom">
        <h3 class="card-title">Chọn chính sách để sửa</h3>
        <form method="get" action="${ctx}/admin/sla/policy" class="d-flex flex-wrap gap-2 align-items-end">
          <div class="flex-fill">
            <label class="form-label" for="sla-category-choice">Chuyên mục</label>
            <select class="form-select" id="sla-category-choice" name="categoryId">
              <option value="">Mặc định toàn hệ thống</option>
              <c:forEach var="option" items="${categoryOptions}">
                <option value="${option.id()}" ${option.id() eq form.categoryId ? 'selected' : ''}><c:out value="${option.label()}"/></option>
              </c:forEach>
            </select>
          </div>
          <button class="btn btn-outline-primary" type="submit">Chọn</button>
        </form>
      </div>

      <div class="card-body">
        <h3 class="card-title">${empty form.categoryId ? 'Sửa chính sách mặc định' : 'Sửa chính sách chuyên mục'}</h3>
        <form method="post" action="${ctx}/admin/sla/policy">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <input type="hidden" name="categoryId" value="<c:out value='${form.categoryId}'/>"/>
          <div class="table-responsive">
            <table class="table table-vcenter">
              <thead><tr><th>Ưu tiên</th><th>Phản hồi đầu tiên (giờ)</th><th>Giải quyết (giờ)</th><th>Bật</th></tr></thead>
              <tbody>
                <c:forEach var="row" items="${form.rows}" varStatus="rowStatus">
                  <c:set var="responseKey" value="rows[${rowStatus.index}].firstResponseHours"/>
                  <c:set var="resolutionKey" value="rows[${rowStatus.index}].resolutionHours"/>
                  <tr>
                    <td>
                      <c:out value="${row.priority.label}"/>
                      <input type="hidden" name="rows[${rowStatus.index}].priority" value="${row.priority}"/>
                    </td>
                    <td>
                      <input class="form-control ${not empty errors[responseKey] ? 'is-invalid' : ''}"
                             type="number" step="0.01" min="0.5" max="720"
                             name="rows[${rowStatus.index}].firstResponseHours"
                             value="<c:out value='${row.firstResponseHours}'/>" required/>
                      <c:if test="${not empty errors[responseKey]}"><div class="text-danger small mt-1"><c:out value="${errors[responseKey]}"/></div></c:if>
                    </td>
                    <td>
                      <input class="form-control ${not empty errors[resolutionKey] ? 'is-invalid' : ''}"
                             type="number" step="0.01" min="0.5" max="2160"
                             name="rows[${rowStatus.index}].resolutionHours"
                             value="<c:out value='${row.resolutionHours}'/>" required/>
                      <c:if test="${not empty errors[resolutionKey]}"><div class="text-danger small mt-1"><c:out value="${errors[resolutionKey]}"/></div></c:if>
                    </td>
                    <td><input type="hidden" name="_rows[${rowStatus.index}].active" value="on"/>
                        <input class="form-check-input" type="checkbox"
                               name="rows[${rowStatus.index}].active" value="true"
                               ${row.active ? 'checked' : ''}/></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
          <button class="btn btn-primary" type="submit">Lưu chính sách</button>
        </form>
      </div>
    </div>
    <!-- Tab Giờ làm việc -->
    <div class="tab-pane fade ${activeTab eq 'hours' ? 'show active' : ''}" id="sla-hours" role="tabpanel">
      <div class="card-body">
        <h3 class="card-title">Giờ làm việc</h3>
        <p class="text-secondary">Mỗi ngày tối đa 4 khoảng giờ. Để trống ngày không làm việc.</p>
        <c:if test="${not empty errors}"><div class="alert alert-danger"><c:forEach var="entry" items="${errors}"><div><c:out value="${entry.value}"/></div></c:forEach></div></c:if>
        <form method="post" action="${ctx}/admin/sla/hours" id="business-hours-form">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <c:set var="intervalIndex" value="0"/>
          <c:forEach var="day" begin="1" end="7">
            <div class="border rounded p-3 mb-2 hours-day" data-day="${day}">
              <div class="d-flex justify-content-between align-items-center mb-2">
                <strong><c:choose><c:when test="${day eq 1}">Thứ Hai</c:when><c:when test="${day eq 2}">Thứ Ba</c:when><c:when test="${day eq 3}">Thứ Tư</c:when><c:when test="${day eq 4}">Thứ Năm</c:when><c:when test="${day eq 5}">Thứ Sáu</c:when><c:when test="${day eq 6}">Thứ Bảy</c:when><c:otherwise>Chủ nhật</c:otherwise></c:choose></strong>
                <button class="btn btn-sm btn-outline-primary" type="button" data-add-interval>Thêm khoảng giờ</button>
              </div>
              <div class="hour-intervals">
                <c:forEach var="interval" items="${hoursForm.intervals}">
                  <c:if test="${interval.dayOfWeek eq day}">
                    <div class="hour-interval d-flex flex-wrap gap-2 align-items-center mb-2">
                      <input type="hidden" data-field="dayOfWeek" name="intervals[${intervalIndex}].dayOfWeek" value="${day}"/>
                      <input class="form-control w-auto" type="time" data-field="start" name="intervals[${intervalIndex}].start" value="<c:out value='${interval.start}'/>" required aria-label="Giờ bắt đầu"/>
                      <span>đến</span>
                      <input class="form-control w-auto" type="time" data-field="end" name="intervals[${intervalIndex}].end" value="<c:out value='${interval.end}'/>" required aria-label="Giờ kết thúc"/>
                      <input type="hidden" data-field="_active" name="_intervals[${intervalIndex}].active" value="on"/>
                      <label class="form-check mb-0"><input class="form-check-input" type="checkbox" data-field="active" name="intervals[${intervalIndex}].active" value="true" ${interval.active ? 'checked' : ''}/> Bật</label>
                      <button class="btn btn-sm btn-outline-danger" type="button" data-remove-interval>Xóa</button>
                    </div>
                    <c:set var="intervalIndex" value="${intervalIndex + 1}"/>
                  </c:if>
                </c:forEach>
              </div>
              <c:set var="dayErrorKey" value="day${day}"/>
              <c:if test="${not empty errors[dayErrorKey]}"><div class="text-danger small"><c:out value="${errors[dayErrorKey]}"/></div></c:if>
            </div>
          </c:forEach>
          <button class="btn btn-primary" type="submit">Lưu giờ làm việc</button>
        </form>
      </div>
    </div>
    <!-- Tab Ngày nghỉ -->
    <div class="tab-pane fade ${activeTab eq 'holidays' ? 'show active' : ''}" id="sla-holidays" role="tabpanel">
      <div class="card-body">
        <h3 class="card-title">Ngày nghỉ</h3>
        <div class="table-responsive"><table class="table table-vcenter">
          <thead><tr><th>Ngày</th><th>Tên ngày nghỉ</th><th>Thao tác</th></tr></thead>
          <tbody>
            <c:forEach var="holiday" items="${holidays}"><tr>
              <td><c:out value="${holiday.date}"/></td><td><c:out value="${holiday.name}"/></td>
              <td><qa:confirmModal id="delete-holiday-${holiday.id}" title="Xóa ngày nghỉ" message="Xóa ngày nghỉ này?" actionUrl="${ctx}/admin/sla/holidays/${holiday.id}/delete" confirmLabel="Xóa"/></td>
            </tr></c:forEach>
            <c:if test="${empty holidays}"><tr><td colspan="3" class="text-secondary text-center">Chưa có ngày nghỉ.</td></tr></c:if>
          </tbody>
        </table></div>
        <h4>Thêm ngày nghỉ</h4>
        <form method="post" action="${ctx}/admin/sla/holidays" class="row g-2 align-items-end">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <div class="col-md-3"><label class="form-label" for="holiday-date">Ngày</label><input class="form-control ${not empty holidayErrors.date ? 'is-invalid' : ''}" id="holiday-date" type="date" name="date" value="<c:out value='${holidayForm.date}'/>" required/><c:if test="${not empty holidayErrors.date}"><div class="invalid-feedback"><c:out value="${holidayErrors.date}"/></div></c:if></div>
          <div class="col-md-6"><label class="form-label" for="holiday-name">Tên ngày nghỉ</label><input class="form-control ${not empty holidayErrors.name ? 'is-invalid' : ''}" id="holiday-name" type="text" name="name" maxlength="150" value="<c:out value='${holidayForm.name}'/>" required/><c:if test="${not empty holidayErrors.name}"><div class="invalid-feedback"><c:out value="${holidayErrors.name}"/></div></c:if></div>
          <div class="col-md-3"><button class="btn btn-primary" type="submit">Thêm ngày nghỉ</button></div>
        </form>
      </div>
    </div>
  </div>
</div>
<script src="${ctx}/assets/js/admin/business-hours.js" defer></script>
</div>
