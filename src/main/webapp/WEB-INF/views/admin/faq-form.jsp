<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title><c:out value="${pageTitle}"/></title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.css"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0"><c:out value="${pageTitle}"/></h2>
  <div class="d-flex gap-2"><c:if test="${editing}"><a class="btn btn-outline-primary" href="${ctx}/admin/faq/${form.id}/revisions">Phiên bản</a></c:if><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Danh sách FAQ</a></div>
</div>

<div class="row"><div class="col-xl-10"><div class="card"><div class="card-body">
  <c:if test="${not empty errors.global}"><div class="alert alert-danger" role="alert"><c:out value="${errors.global}"/></div></c:if>
  <form method="post" action="${ctx}/admin/faq" id="faq-editor-form" novalidate data-upload-url="${ctx}/api/media/upload">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="id" value="<c:out value='${form.id}'/>"/>
    <input type="hidden" name="version" value="<c:out value='${form.version}'/>"/>
    <%@ include file="/WEB-INF/views/admin/faq-editor-fields.jspf" %>
    <div class="d-flex gap-2"><button class="btn btn-primary" type="submit">${editing ? 'Lưu thay đổi' : 'Lưu bản nháp'}</button><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Hủy</a></div>
  </form>
</div></div></div></div>

<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js" defer></script>
<script src="${ctx}/assets/js/admin/faq-editor.js" defer></script>
