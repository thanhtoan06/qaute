<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Thẻ FAQ</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="d-flex justify-content-between align-items-center mb-3"><h2 class="page-title mb-0">Thẻ FAQ</h2><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Bài FAQ</a></div>
<div class="card mb-3"><div class="card-body"><h3 class="card-title">Thêm thẻ</h3>
  <form method="post" action="${ctx}/admin/faq/tags" class="d-flex flex-wrap gap-2">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input class="form-control" style="max-width: 24rem" name="name" minlength="2" maxlength="40" placeholder="Tên thẻ" required/>
    <button class="btn btn-primary" type="submit">Thêm</button>
  </form>
</div></div>
<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>ID / Tên thẻ</th><th>Slug</th><th>Số FAQ dùng</th><th>Đổi tên</th><th>Gộp vào thẻ khác</th><th>Xóa</th></tr></thead>
  <tbody><c:choose><c:when test="${empty tags}"><tr><td colspan="6" class="text-secondary">Chưa có thẻ.</td></tr></c:when>
  <c:otherwise><c:forEach var="tag" items="${tags}"><tr>
    <td>#${tag.id()} · <c:out value="${tag.name()}"/></td><td><code><c:out value="${tag.slug()}"/></code></td><td>${tag.faqCount()}</td>
    <td><form method="post" action="${ctx}/admin/faq/tags/${tag.id()}/rename" class="d-flex gap-1">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input class="form-control form-control-sm" name="name" value="<c:out value='${tag.name()}'/>" minlength="2" maxlength="40" required aria-label="Tên mới"/>
      <button class="btn btn-sm btn-outline-primary" type="submit">Lưu</button>
    </form></td>
    <td><form method="post" action="${ctx}/admin/faq/tags/${tag.id()}/merge" class="d-flex gap-1">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input class="form-control form-control-sm" type="number" name="targetId" min="1" required aria-label="ID thẻ đích" placeholder="ID thẻ đích"/>
      <button class="btn btn-sm btn-outline-warning" type="submit" onclick="return confirm('Gộp mọi FAQ sang thẻ đích và xóa thẻ nguồn?')">Gộp</button>
    </form></td>
    <td><c:if test="${tag.faqCount() eq 0}"><qa:confirmModal id="delete-tag-${tag.id()}" title="Xóa thẻ" message="Xóa thẻ này?" actionUrl="${ctx}/admin/faq/tags/${tag.id()}/delete" confirmLabel="Xóa" cssClass="btn-sm btn-outline-danger"/></c:if></td>
  </tr></c:forEach></c:otherwise></c:choose></tbody>
</table></div></div>

<qa:pagination page="${resultPage}" baseUrl="${ctx}/admin/faq/tags"/>
