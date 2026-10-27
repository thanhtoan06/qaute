<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Nhật ký hệ thống</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/audit-logs.css">
</head>
<div class="qa-page-admin-audit-logs" data-qa-view="admin-audit-logs">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang -->
<h2 class="page-title mb-3 qa-view-title" data-qa-page="admin-audit-logs"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-journal-text"></i></span>Nhật ký hệ thống</h2>

<%-- Lọc nhật ký theo người dùng, hành động, đối tượng và khoảng ngày; hỗ trợ xuất CSV. --%>
<!-- Form lọc nhật ký: người thực hiện, hành động, đối tượng, khoảng thời gian -->
<form method="get" action="${ctx}/admin/audit-logs" class="card card-body mb-3">
  <div class="row g-3">
    <div class="col-12 col-md-3"><label class="form-label" for="audit-actor">Tên hoặc email người thực hiện</label><input class="form-control" id="audit-actor" name="actorQ" value="<c:out value='${filter.actorQ}'/>"/></div>
    <div class="col-6 col-md-2"><label class="form-label" for="audit-actor-id">ID người thực hiện</label><input class="form-control" id="audit-actor-id" name="actorId" type="number" min="1" value="<c:out value='${filter.actorId}'/>"/></div>
    <div class="col-6 col-md-3"><label class="form-label" for="audit-action">Hành động</label><select class="form-select" id="audit-action" name="action"><option value="">Tất cả</option><c:forEach var="action" items="${actions}"><option value="<c:out value='${action.code()}'/>" ${filter.action eq action.code() ? 'selected' : ''}><c:out value="${action.label()}"/> (<c:out value="${action.code()}"/>)</option></c:forEach></select></div>
    <div class="col-6 col-md-2"><label class="form-label" for="audit-type">Loại đối tượng</label><input class="form-control" id="audit-type" name="entityType" value="<c:out value='${filter.entityType}'/>"/></div>
    <div class="col-6 col-md-2"><label class="form-label" for="audit-entity-id">ID đối tượng</label><input class="form-control" id="audit-entity-id" name="entityId" type="number" min="1" value="<c:out value='${filter.entityId}'/>"/></div>
    <div class="col-6 col-md-2"><label class="form-label" for="audit-from">Từ ngày</label><input class="form-control" id="audit-from" name="from" type="date" value="<c:out value='${filter.from}'/>"/></div>
    <div class="col-6 col-md-2"><label class="form-label" for="audit-to">Đến ngày</label><input class="form-control" id="audit-to" name="to" type="date" value="<c:out value='${filter.to}'/>"/></div>
    <div class="col-12 d-flex flex-wrap gap-2 align-items-end">
      <button class="btn btn-primary" type="submit">Lọc</button>
      <a class="btn btn-outline-secondary" href="${ctx}/admin/audit-logs">Bỏ lọc</a>
      <button class="btn btn-outline-primary ms-auto" type="submit" formaction="${ctx}/admin/audit-logs/export">Xuất CSV (tối đa 5.000 dòng)</button>
    </div>
  </div>
</form>

<%-- Liệt kê nhật ký và mở hộp thoại xem thông tin chi tiết từng sự kiện. --%>
<!-- Bảng danh sách nhật ký hệ thống -->
<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>Thời gian</th><th>Người thực hiện</th><th>Hành động</th><th>Đối tượng</th><th>IP</th><th></th></tr></thead>
  <tbody><c:choose><c:when test="${empty logs.content}"><tr><td colspan="6" class="text-center text-secondary py-5">Không có dòng nhật ký phù hợp.</td></tr></c:when>
  <c:otherwise><c:forEach var="log" items="${logs.content}"><tr>
    <td><c:out value="${log.createdAt()}"/></td>
    <td><div class="fw-semibold"><c:out value="${log.actorName()}"/></div><small class="text-secondary"><c:out value="${log.actorRole()}"/></small></td>
    <td><div><c:out value="${log.actionLabel()}"/></div><small class="text-secondary"><c:out value="${log.action()}"/></small></td>
    <td><c:out value="${log.entityType()}"/> <c:if test="${not empty log.entityId()}">#<c:out value="${log.entityId()}"/></c:if></td>
    <td><c:out value="${log.ip()}"/></td>
    <!-- Nút xem chi tiết trong modal -->
    <td><button class="btn btn-sm btn-outline-primary js-audit-detail" type="button" data-bs-toggle="modal" data-bs-target="#audit-detail-modal" data-url="${ctx}/admin/audit-logs/${log.id()}">Chi tiết</button></td>
  </tr></c:forEach></c:otherwise></c:choose></tbody>
</table></div></div>
<qa:pagination page="${logs}" baseUrl="${ctx}/admin/audit-logs" params="${paginationParams}"/>

<!-- Modal hiển thị chi tiết nhật ký: thông tin cơ bản và dữ liệu trước/sau -->
<div class="modal modal-blur fade" id="audit-detail-modal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered"><div class="modal-content">
    <div class="modal-header"><h3 class="modal-title">Chi tiết nhật ký</h3><button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button></div>
    <div class="modal-body">
      <div id="audit-detail-error" class="alert alert-danger d-none" role="alert"></div>
      <dl class="row mb-3">
        <dt class="col-sm-3">Thời gian</dt><dd class="col-sm-9" id="audit-detail-at"></dd>
        <dt class="col-sm-3">Người thực hiện</dt><dd class="col-sm-9" id="audit-detail-actor"></dd>
        <dt class="col-sm-3">Hành động</dt><dd class="col-sm-9" id="audit-detail-action"></dd>
        <dt class="col-sm-3">Đối tượng</dt><dd class="col-sm-9" id="audit-detail-entity"></dd>
        <dt class="col-sm-3">IP / User-Agent</dt><dd class="col-sm-9" id="audit-detail-client"></dd>
        <dt class="col-sm-3">Trace ID</dt><dd class="col-sm-9" id="audit-detail-trace"></dd>
      </dl>
      <!-- So sánh dữ liệu trước và sau thay đổi -->
      <div class="row g-3">
        <div class="col-md-6"><h4>Trước</h4><pre class="border rounded bg-light p-3 text-wrap mb-0" id="audit-detail-before"></pre></div>
        <div class="col-md-6"><h4>Sau</h4><pre class="border rounded bg-light p-3 text-wrap mb-0" id="audit-detail-after"></pre></div>
      </div>
    </div>
  </div></div>
</div>
<script src="${ctx}/assets/js/admin/audit-logs.js" defer></script>
</div>
