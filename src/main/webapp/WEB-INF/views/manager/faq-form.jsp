<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title><c:out value="${pageTitle}"/></title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.css"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0"><c:out value="${pageTitle}"/></h2>
  <a class="btn btn-outline-secondary" href="${ctx}/manager/faq">FAQ của tôi</a>
</div>

<div class="row"><div class="col-xl-10"><div class="card"><div class="card-body">
  <c:if test="${not empty errors.global}"><div class="alert alert-danger" role="alert"><c:out value="${errors.global}"/></div></c:if>
  <c:if test="${not empty sourceTicketId}"><div class="alert alert-warning" role="note">Bản nháp được tạo từ ticket #<c:out value="${sourceTicketId}"/>. Hãy đọc lại toàn bộ nội dung và xóa mọi thông tin cá nhân trước khi gửi duyệt.</div></c:if>
  <form method="post" action="${ctx}/manager/faq" id="faq-editor-form" novalidate data-upload-url="${ctx}/api/media/upload">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="id" value="<c:out value='${form.id}'/>"/>
    <input type="hidden" name="version" value="<c:out value='${form.version}'/>"/>
    <%@ include file="/WEB-INF/views/admin/faq-editor-fields.jspf" %>
    <button class="btn btn-primary" type="submit">${editing ? 'Lưu thay đổi' : 'Lưu bản nháp'}</button>
  </form>

  <c:if test="${editing}"><hr/><form method="post" action="${ctx}/manager/faq/${form.id}/submit">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <c:if test="${not empty sourceTicketId}"><label class="form-check mb-3"><input class="form-check-input" type="checkbox" name="confirmedNoPersonalInfo" value="true" required/><span class="form-check-label">Tôi đã loại bỏ thông tin cá nhân khỏi FAQ</span></label></c:if>
    <button class="btn btn-success" type="submit">Gửi duyệt</button>
  </form></c:if>
</div></div></div></div>

<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js" defer></script>
<script src="${ctx}/assets/js/admin/faq-editor.js" defer></script>
