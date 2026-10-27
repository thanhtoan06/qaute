<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Cài đặt hệ thống</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/settings.css">
</head>
<div class="qa-page-admin-settings" data-qa-view="admin-settings">

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
