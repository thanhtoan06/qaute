<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Cài đặt hệ thống</title>
<div class="qa-page-admin-settings" data-qa-view="admin-settings">
<style>
.qa-page-admin-settings { --page-accent: #526c88; --page-accent-dark: #394e68; --page-tint: #eff4f8; --page-line: #dce5ed; min-width: 0; color: #344c67; }
.qa-page-admin-settings > .page-header, .qa-page-admin-settings > .qa-page-heading, .qa-page-admin-settings > .d-flex:has(> .page-title), .qa-page-admin-settings > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #eff4f8, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-settings > .page-header::after, .qa-page-admin-settings > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #dce5ed; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-settings .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-settings .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #dce5ed; border-radius: 12px; color: #394e68; background: linear-gradient(145deg,#fff,#eff4f8); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-settings .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-settings .card:hover { border-color: #dce5ed; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-settings .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#eff4f8); }
.qa-page-admin-settings .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-settings .card-body { color: #435b75; }
.qa-page-admin-settings .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#eff4f8); }
.qa-page-admin-settings .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-settings .form-control, .qa-page-admin-settings .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-settings textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-settings .form-control:focus, .qa-page-admin-settings .form-select:focus { border-color: #526c88; box-shadow: 0 0 0 4px #eff4f8; }
.qa-page-admin-settings .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-settings .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-settings .btn-primary { border-color: #526c88; background: linear-gradient(120deg,#526c88,#394e68); box-shadow: 0 5px 13px #dce5ed; }
.qa-page-admin-settings .btn-outline-primary { border-color: #dce5ed; color: #394e68; background: #fff; }
.qa-page-admin-settings .btn-outline-primary:hover { border-color: #526c88; color: #fff; background: linear-gradient(120deg,#526c88,#394e68); }
.qa-page-admin-settings .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#eff4f8); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-settings .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-settings .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-settings .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#eff4f8,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-settings .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-settings .table tbody tr:hover { background: #eff4f8; }
.qa-page-admin-settings .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-settings .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-settings .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-settings .nav-tabs .nav-link.active { color: #394e68; box-shadow: inset 0 -2px #526c88; }
.qa-page-admin-settings .nav-pills .nav-link.active { color: #394e68; background: #eff4f8; box-shadow: inset 0 0 0 1px #dce5ed; }
.qa-page-admin-settings .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-settings .progress-bar { border-radius: inherit; }
.qa-page-admin-settings .card-header { background-image: linear-gradient(90deg,var(--page-accent),transparent 72%); background-repeat: no-repeat; background-size: 100% 2px; background-position: top; }
@media (max-width: 767.98px) {
  .qa-page-admin-settings > .page-header, .qa-page-admin-settings > .qa-page-heading, .qa-page-admin-settings > .d-flex:has(> .page-title), .qa-page-admin-settings > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-settings .card { border-radius: 15px; }
  .qa-page-admin-settings .card-body { padding: 1rem; }
  .qa-page-admin-settings .table-responsive { border-radius: 11px; }
  .qa-page-admin-settings .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-settings *, .qa-page-admin-settings *::before, .qa-page-admin-settings *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang -->
<h2 class="page-title mb-3 qa-view-title" data-qa-page="admin-settings"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-gear"></i></span>Cài đặt hệ thống</h2>
<!-- Thông báo lỗi -->
<c:if test="${not empty saveError}">
  <div class="alert alert-danger"><c:out value="${saveError}"/></div>
</c:if>

<c:choose>
  <c:when test="${empty groups}"><div class="alert alert-info">Chưa có cài đặt hệ thống.</div></c:when>
  <c:otherwise>
    <div class="card">
      <!-- Tab các nhóm cài đặt -->
      <div class="card-header p-0">
        <div class="nav nav-tabs card-header-tabs flex-wrap px-3" role="tablist">
          <c:forEach var="group" items="${groups}" varStatus="tab">
            <button class="nav-link ${group.key eq activeGroup ? 'active' : ''}" type="button"
                    id="settings-tab-${tab.index}" data-bs-toggle="tab"
                    data-bs-target="#settings-pane-${tab.index}" role="tab"
                    aria-controls="settings-pane-${tab.index}"
                    aria-selected="${group.key eq activeGroup}"><c:out value="${group.key}"/></button>
          </c:forEach>
        </div>
      </div>
      <!-- Nội dung từng nhóm cài đặt -->
      <div class="tab-content">
        <c:forEach var="group" items="${groups}" varStatus="tab">
          <div class="tab-pane fade ${group.key eq activeGroup ? 'show active' : ''}"
               id="settings-pane-${tab.index}" role="tabpanel"
               aria-labelledby="settings-tab-${tab.index}">
            <!-- Form chỉnh sửa các tham số -->
            <form method="post" action="${ctx}/admin/settings/${groupPaths[group.key]}">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
              <div class="table-responsive">
                <table class="table card-table table-vcenter mb-0">
                  <thead><tr><th>Tham số</th><th>Giá trị</th><th>Khoảng cho phép</th></tr></thead>
                  <tbody>
                    <c:forEach var="setting" items="${group.value}">
                      <tr>
                        <td class="w-50">
                          <label class="fw-medium mb-1" for="setting-${setting.key()}"><c:out value="${setting.key()}"/></label>
                          <div class="text-secondary small"><c:out value="${setting.description()}"/></div>
                        </td>
                        <td>
                          <!-- Input theo loại dữ liệu: BOOL, INT, STRING -->
                          <c:choose>
                            <c:when test="${setting.valueType() eq 'BOOL'}">
                              <label class="form-check form-switch mb-0">
                                <input class="form-check-input" type="checkbox" id="setting-${setting.key()}"
                                       name="${setting.key()}" value="true"
                                       ${displayValues[setting.key()] eq 'true' ? 'checked' : ''}/>
                                <span class="form-check-label">Bật</span>
                              </label>
                            </c:when>
                            <c:when test="${setting.valueType() eq 'INT'}">
                              <input class="form-control ${not empty errors[setting.key()] ? 'is-invalid' : ''}"
                                     type="number" step="1" id="setting-${setting.key()}"
                                     name="${setting.key()}" min="${setting.minValue()}" max="${setting.maxValue()}"
                                     value="<c:out value='${displayValues[setting.key()]}'/>" required/>
                            </c:when>
                            <c:otherwise>
                              <input class="form-control ${not empty errors[setting.key()] ? 'is-invalid' : ''}"
                                     type="text" id="setting-${setting.key()}" name="${setting.key()}"
                                     value="<c:out value='${displayValues[setting.key()]}'/>"
                                     maxlength="500" required/>
                            </c:otherwise>
                          </c:choose>
                          <c:if test="${not empty errors[setting.key()]}">
                            <div class="text-danger small mt-1"><c:out value="${errors[setting.key()]}"/></div>
                          </c:if>
                        </td>
                        <!-- Khoảng giá trị cho phép -->
                        <td class="text-secondary">
                          <c:choose>
                            <c:when test="${setting.valueType() eq 'INT'}">
                              <c:out value="${setting.minValue()}"/>–<c:out value="${setting.maxValue()}"/>
                            </c:when>
                            <c:when test="${setting.valueType() eq 'STRING'}">Tối đa 500 ký tự</c:when>
                            <c:otherwise>—</c:otherwise>
                          </c:choose>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
              <!-- Nút lưu nhóm cài đặt -->
              <div class="card-footer text-end"><button class="btn btn-primary" type="submit">Lưu nhóm <c:out value="${group.key}"/></button></div>
            </form>
          </div>
        </c:forEach>
      </div>
    </div>
  </c:otherwise>
</c:choose>
</div>
