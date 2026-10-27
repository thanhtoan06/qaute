<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title><c:out value="${pageTitle}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/faq-form.css">
</head>
<div class="qa-page-admin-faq-form" data-qa-view="admin-faq-form">

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.css"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và các nút điều hướng -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-faq-form"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span><c:out value="${pageTitle}"/></h2>
  <div class="d-flex gap-2"><c:if test="${editing}"><a class="btn btn-outline-primary" href="${ctx}/admin/faq/${form.id}/revisions">Phiên bản</a></c:if><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Danh sách FAQ</a></div>
</div>

<%-- Soạn hoặc cập nhật nội dung FAQ và lưu thành bản nháp. --%>
<!-- Form soạn thảo FAQ -->
<div class="row"><div class="col-xl-10"><div class="card"><div class="card-body">
  <c:if test="${not empty errors.global}"><div class="alert alert-danger" role="alert"><c:out value="${errors.global}"/></div></c:if>
  <form method="post" action="${ctx}/admin/faq" id="faq-editor-form" novalidate data-upload-url="${ctx}/api/media/upload">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="id" value="<c:out value='${form.id}'/>"/>
    <input type="hidden" name="version" value="<c:out value='${form.version}'/>"/>
    <!-- Các trường nhập liệu FAQ -->
    <%@ include file="/WEB-INF/views/common/faq-editor-fields.jspf" %>
    <!-- Nút lưu và hủy -->
    <div class="d-flex gap-2"><button class="btn btn-primary" type="submit">${editing ? 'Lưu thay đổi' : 'Lưu bản nháp'}</button><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Hủy</a></div>
  </form>
</div></div></div></div>

<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js" defer></script>
<script src="${ctx}/assets/js/admin/faq-editor.js" defer></script>
</div>
