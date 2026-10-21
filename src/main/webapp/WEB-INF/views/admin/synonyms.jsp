<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Từ đồng nghĩa FAQ</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="d-flex justify-content-between align-items-center mb-3"><h2 class="page-title mb-0">Từ đồng nghĩa FAQ</h2><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Bài FAQ</a></div>
<div class="card mb-3"><div class="card-body"><h3 class="card-title">Thêm cặp từ</h3>
  <form method="post" action="${ctx}/admin/faq/synonyms" class="row g-2 align-items-end">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div class="col-md-5"><label class="form-label" for="syn-term">Từ khóa</label><input class="form-control" id="syn-term" name="term" value="<c:out value='${prefillTerm}'/>" minlength="2" maxlength="100" required/></div>
    <div class="col-md-5"><label class="form-label" for="syn-other">Từ đồng nghĩa</label><input class="form-control" id="syn-other" name="synonym" minlength="2" maxlength="100" required/></div>
    <div class="col-md-2"><button class="btn btn-primary w-100" type="submit">Thêm</button></div>
  </form>
</div></div>
<div class="card mb-3"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>Từ khóa</th><th>Từ đồng nghĩa</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
  <tbody><c:choose><c:when test="${empty synonyms}"><tr><td colspan="4" class="text-secondary">Chưa có từ đồng nghĩa.</td></tr></c:when>
  <c:otherwise><c:forEach var="item" items="${synonyms}"><tr>
    <td><c:out value="${item.term}"/></td><td><c:out value="${item.synonym}"/></td>
    <td><span class="badge ${item.active ? 'bg-green-lt' : 'bg-secondary-lt'}">${item.active ? 'Bật' : 'Tắt'}</span></td>
    <td><div class="d-flex gap-1"><form method="post" action="${ctx}/admin/faq/synonyms/${item.id}/toggle"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-primary" type="submit">${item.active ? 'Tắt' : 'Bật'}</button></form>
      <qa:confirmModal id="delete-synonym-${item.id}" title="Xóa từ đồng nghĩa" message="Xóa cặp từ này?" actionUrl="${ctx}/admin/faq/synonyms/${item.id}/delete" confirmLabel="Xóa" cssClass="btn-sm btn-outline-danger"/>
    </div></td>
  </tr></c:forEach></c:otherwise></c:choose></tbody>
</table></div></div>
<div class="card"><div class="card-header"><h3 class="card-title">Truy vấn không có kết quả 30 ngày qua</h3></div>
  <div class="table-responsive"><table class="table table-vcenter card-table"><thead><tr><th>Truy vấn</th><th>Số lần</th><th>Thao tác</th></tr></thead>
    <tbody><c:choose><c:when test="${empty noResultQueries}"><tr><td colspan="3" class="text-secondary">Chưa có truy vấn không có kết quả.</td></tr></c:when>
    <c:otherwise><c:forEach var="query" items="${noResultQueries}"><tr>
      <td><c:out value="${query.query()}"/></td><td>${query.count()}</td>
      <td><c:url var="synonymUrl" value="/admin/faq/synonyms"><c:param name="term" value="${query.query()}"/></c:url><c:url var="newFaqUrl" value="/admin/faq/new"><c:param name="title" value="${query.query()}"/></c:url>
        <a class="btn btn-sm btn-outline-primary" href="<c:out value='${synonymUrl}'/>">Thêm từ đồng nghĩa</a>
        <a class="btn btn-sm btn-outline-secondary" href="<c:out value='${newFaqUrl}'/>">Soạn FAQ</a>
      </td>
    </tr></c:forEach></c:otherwise></c:choose></tbody>
  </table></div>
</div>

<qa:pagination page="${resultPage}" baseUrl="${ctx}/admin/faq/synonyms"/>
