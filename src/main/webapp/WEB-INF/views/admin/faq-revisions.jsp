<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Phiên bản FAQ</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <div><h2 class="page-title mb-0">Phiên bản FAQ</h2><div class="text-secondary"><c:out value="${faq.title()}"/></div></div>
  <div class="d-flex gap-2"><a class="btn btn-outline-secondary" href="${ctx}/admin/faq/${faq.id()}/review">Xem bài hiện tại</a><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Danh sách FAQ</a></div>
</div>

<div class="card mb-3"><div class="card-body">
  <form method="get" action="${ctx}/admin/faq/${faq.id()}/revisions/compare" class="row g-2 align-items-end">
    <div class="col-sm-5"><label class="form-label" for="version-a">Phiên bản A</label><select class="form-select" id="version-a" name="a" required><c:forEach var="item" items="${history}"><option value="${item.versionNo()}" ${not empty left and left.versionNo() eq item.versionNo() ? 'selected' : ''}>Phiên bản ${item.versionNo()}</option></c:forEach></select></div>
    <div class="col-sm-5"><label class="form-label" for="version-b">Phiên bản B</label><select class="form-select" id="version-b" name="b" required><c:forEach var="item" items="${history}"><option value="${item.versionNo()}" ${not empty right and right.versionNo() eq item.versionNo() ? 'selected' : ''}>Phiên bản ${item.versionNo()}</option></c:forEach></select></div>
    <div class="col-sm-2"><button class="btn btn-outline-primary w-100" type="submit" ${empty history ? 'disabled' : ''}>So sánh</button></div>
  </form>
</div></div>

<c:if test="${not empty left and not empty right}">
  <div class="row g-3 mb-3">
    <c:forEach var="version" items="${comparison}"><div class="col-lg-6"><article class="card card-body h-100">
      <h3>Phiên bản ${version.versionNo()}</h3><h4><c:out value="${version.title()}"/></h4>
      <c:if test="${not empty version.summary()}"><p class="text-secondary"><c:out value="${version.summary()}"/></p></c:if>
      <div class="qa-faq-content border-top pt-3"><c:out value="${version.bodyHtml()}" escapeXml="false"/></div>
    </article></div></c:forEach>
  </div>
</c:if>

<c:if test="${not empty selected}"><article class="card card-body mb-3">
  <h3>Phiên bản ${selected.versionNo()}</h3><h4><c:out value="${selected.title()}"/></h4>
  <c:if test="${not empty selected.summary()}"><p class="text-secondary"><c:out value="${selected.summary()}"/></p></c:if>
  <div class="qa-faq-content border-top pt-3"><c:out value="${selected.bodyHtml()}" escapeXml="false"/></div>
</article></c:if>

<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>Phiên bản</th><th>Người sửa</th><th>Ghi chú</th><th>Thời gian</th><th>Thao tác</th></tr></thead>
  <tbody><c:choose><c:when test="${empty history}"><tr><td colspan="5" class="text-secondary">Chưa có phiên bản.</td></tr></c:when>
  <c:otherwise><c:forEach var="item" items="${history}"><tr>
    <td>#${item.versionNo()}</td><td><c:out value="${item.changedByName()}"/></td><td><c:out value="${item.changeNote()}"/></td><td><c:out value="${item.createdAt()}"/></td>
    <td><div class="d-flex gap-1"><a class="btn btn-sm btn-outline-secondary" href="${ctx}/admin/faq/${faq.id()}/revisions/${item.versionNo()}">Xem</a>
      <c:if test="${faq.status() ne 'ARCHIVED'}"><qa:confirmModal id="restore-revision-${item.versionNo()}" title="Khôi phục phiên bản" message="Tạo phiên bản mới từ phiên bản ${item.versionNo()}? Trạng thái FAQ hiện tại được giữ nguyên." actionUrl="${ctx}/admin/faq/${faq.id()}/revisions/${item.versionNo()}/restore" confirmLabel="Khôi phục" cssClass="btn-sm btn-outline-primary"/></c:if>
    </div></td>
  </tr></c:forEach></c:otherwise></c:choose></tbody>
</table></div></div>
