<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Soạn thông báo</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.css"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="d-flex justify-content-between align-items-center mb-3"><h2 class="page-title mb-0">Soạn thông báo</h2><a href="${ctx}/admin/announcements" class="btn btn-outline-secondary">Danh sách</a></div>
<div class="card"><div class="card-body"><c:if test="${not empty errors.global}"><div class="alert alert-danger"><c:out value="${errors.global}"/></div></c:if>
<form method="post" action="${ctx}/admin/announcements" id="announcement-form">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  <input type="hidden" name="id" value="<c:out value='${form.id}'/>"/>
  <input type="hidden" name="coverAssetId" id="cover-asset-id" value="<c:out value='${form.coverAssetId}'/>"/>
  <div class="mb-3"><label class="form-label" for="announcement-title">Tiêu đề</label><input id="announcement-title" class="form-control" name="title" minlength="5" maxlength="200" required value="<c:out value='${form.title}'/>"/><div class="text-danger small"><c:out value="${errors.title}"/></div></div>
  <div class="mb-3"><label class="form-label" for="announcement-summary">Tóm tắt</label><textarea id="announcement-summary" class="form-control" name="summary" minlength="10" maxlength="500" required><c:out value="${form.summary}"/></textarea><div class="text-danger small"><c:out value="${errors.summary}"/></div></div>
  <div class="mb-3"><label class="form-label" for="announcement-body">Nội dung</label><textarea id="announcement-body" class="form-control" name="bodyHtml" required><c:out value="${form.bodyHtml}"/></textarea><div class="text-danger small"><c:out value="${errors.bodyHtml}"/></div></div>
  <div class="row"><div class="col-md-6 mb-3"><label class="form-label" for="announcement-audience">Đối tượng</label><select id="announcement-audience" name="audience" class="form-select" required><c:forEach var="aud" items="${audiences}"><option value="${aud}" ${form.audience eq aud ? 'selected' : ''}><c:out value="${aud}"/></option></c:forEach></select></div>
  <div class="col-md-6 mb-3"><label class="form-label">Ảnh bìa</label><div id="announcement-cover-uploader"></div><c:if test="${not empty form.coverAssetId}"><div class="small text-secondary">Ảnh hiện tại: #<c:out value="${form.coverAssetId}"/></div></c:if><div id="announcement-upload-error" class="text-danger small"></div></div></div>
  <div class="row"><div class="col-md-6 mb-3"><label class="form-label" for="announcement-publish">Thời gian đăng</label><input id="announcement-publish" class="form-control" type="datetime-local" name="publishAt" value="<c:out value='${form.publishAt}'/>"/></div>
  <div class="col-md-6 mb-3"><label class="form-label" for="announcement-expires">Hết hạn</label><input id="announcement-expires" class="form-control" type="datetime-local" name="expiresAt" value="<c:out value='${form.expiresAt}'/>"/></div></div>
  <div class="form-check mb-2"><input class="form-check-input" type="checkbox" id="announcement-pin" name="pinned" value="true" ${form.pinned ? 'checked' : ''}/><label class="form-check-label" for="announcement-pin">Ghim lên đầu</label></div>
  <div class="form-check mb-2"><input class="form-check-input" type="checkbox" id="announcement-auto" name="autoPublish" value="true" ${form.autoPublish ? 'checked' : ''}/><label class="form-check-label" for="announcement-auto">Tự động đăng đúng thời gian đã chọn</label></div>
  <div class="form-check mb-3"><input class="form-check-input" type="checkbox" id="announcement-notify" name="notifyOnPublish" value="true" ${form.notifyOnPublish ? 'checked' : ''}/><label class="form-check-label" for="announcement-notify">Gửi thông báo đẩy khi đăng</label></div>
  <button type="submit" class="btn btn-primary">Lưu bản nháp</button>
</form></div></div>
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js" defer></script>
<script src="${ctx}/assets/js/common/file-uploader.js" defer></script>
<script src="${ctx}/assets/js/admin/announcement-form.js" defer></script>
