<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Quản lý FAQ</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0">Quản lý FAQ</h2>
  <a class="btn btn-primary" href="${ctx}/admin/faq/new"><i class="bi bi-plus-lg me-1"></i>Soạn bài mới</a>
</div>

<div class="nav nav-tabs mb-3" role="tablist" aria-label="Trạng thái FAQ">
  <a class="nav-link ${empty filter.status ? 'active' : ''}" href="${ctx}/admin/faq?${tabParams}page=0">Tất cả <span class="badge bg-secondary-lt ms-1">${totalCount}</span></a>
  <c:forEach var="status" items="${statuses}">
    <a class="nav-link ${filter.status eq status ? 'active' : ''}" href="${ctx}/admin/faq?${tabParams}status=${status}&amp;page=0">
      <c:choose><c:when test="${status eq 'DRAFT'}">Nháp</c:when><c:when test="${status eq 'PENDING_REVIEW'}">Chờ duyệt</c:when><c:when test="${status eq 'PUBLISHED'}">Đã xuất bản</c:when><c:otherwise>Lưu trữ</c:otherwise></c:choose>
      <span class="badge bg-secondary-lt ms-1">${counts[status.name()]}</span>
    </a>
  </c:forEach>
</div>

<form method="get" action="${ctx}/admin/faq" class="qa-filter-bar mb-3">
  <c:if test="${not empty filter.status}"><input type="hidden" name="status" value="${filter.status}"/></c:if>
  <div><label class="form-label" for="faqSearch">Tìm kiếm</label><input class="form-control" id="faqSearch" name="q" type="search" maxlength="200" value="<c:out value='${filter.q}'/>" placeholder="Tiêu đề hoặc slug"/></div>
  <div><label class="form-label" for="faqCategory">Chuyên mục</label><select class="form-select" id="faqCategory" name="categoryId"><option value="">Tất cả chuyên mục</option><c:forEach var="option" items="${categoryOptions}"><option value="${option.id()}" ${filter.categoryId eq option.id() ? 'selected' : ''}><c:out value="${option.label()}"/></option></c:forEach></select></div>
  <div><label class="form-label" for="faqAuthor">Tác giả</label><select class="form-select" id="faqAuthor" name="authorId"><option value="">Tất cả tác giả</option><c:forEach var="author" items="${authors}"><option value="${author.key}" ${filter.authorId eq author.key ? 'selected' : ''}><c:out value="${author.value}"/></option></c:forEach></select></div>
  <div><label class="form-label" for="faqFeatured">Nổi bật</label><select class="form-select" id="faqFeatured" name="featured"><option value="">Tất cả</option><option value="true" ${filter.featured eq true ? 'selected' : ''}>Có</option><option value="false" ${filter.featured eq false ? 'selected' : ''}>Không</option></select></div>
  <div><label class="form-label" for="faqSort">Sắp xếp</label><select class="form-select" id="faqSort" name="sort"><option value="updatedAt" ${empty filter.sort or filter.sort eq 'updatedAt' ? 'selected' : ''}>Cập nhật mới nhất</option><option value="viewCount" ${filter.sort eq 'viewCount' ? 'selected' : ''}>Xem nhiều nhất</option><option value="helpfulCount" ${filter.sort eq 'helpfulCount' ? 'selected' : ''}>Hữu ích nhiều nhất</option><option value="title" ${filter.sort eq 'title' ? 'selected' : ''}>Tiêu đề A–Z</option></select></div>
  <div class="d-flex gap-2"><button class="btn btn-primary" type="submit">Lọc</button><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Bỏ lọc</a></div>
</form>

<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table qa-admin-table">
  <thead><tr><th>Tiêu đề</th><th>Chuyên mục</th><th>Tác giả</th><th>Trạng thái</th><th>Lượt xem</th><th>Hữu ích</th><th>Cập nhật</th></tr></thead>
  <tbody>
    <c:choose><c:when test="${empty faqs.content}"><tr><td colspan="7"><qa:emptyState icon="bi-journal-text" title="Không có FAQ phù hợp" message="Hãy thử thay đổi điều kiện lọc."/></td></tr></c:when>
    <c:otherwise><c:forEach var="item" items="${faqs.content}"><tr>
      <td><div class="fw-semibold"><c:choose><c:when test="${item.status() eq 'ARCHIVED'}"><c:out value="${item.title()}"/></c:when><c:otherwise><a href="${ctx}/admin/faq/${item.id()}/edit"><c:out value="${item.title()}"/></a></c:otherwise></c:choose></div><div class="small text-secondary"><c:out value="${item.slug()}"/></div><c:if test="${item.featured()}"><span class="badge bg-yellow-lt mt-1">Nổi bật</span></c:if></td>
      <td><c:out value="${item.categoryPath()}"/></td>
      <td><c:out value="${item.authorName()}"/></td>
      <td><c:choose><c:when test="${item.status() eq 'DRAFT'}"><span class="badge bg-secondary-lt">Nháp</span></c:when><c:when test="${item.status() eq 'PENDING_REVIEW'}"><span class="badge bg-yellow-lt">Chờ duyệt</span></c:when><c:when test="${item.status() eq 'PUBLISHED'}"><span class="badge bg-green-lt">Đã xuất bản</span></c:when><c:otherwise><span class="badge bg-gray-lt">Lưu trữ</span></c:otherwise></c:choose></td>
      <td><c:out value="${item.viewCount()}"/></td>
      <td><c:set var="voteCount" value="${item.helpfulCount() + item.notHelpfulCount()}"/><c:choose><c:when test="${voteCount gt 0}"><fmt:formatNumber value="${100.0 * item.helpfulCount() / voteCount}" maxFractionDigits="0"/>% <span class="small text-secondary">(${voteCount})</span></c:when><c:otherwise><span class="text-secondary">—</span></c:otherwise></c:choose></td>
      <td><c:out value="${item.updatedAt()}"/></td>
    </tr></c:forEach></c:otherwise></c:choose>
  </tbody>
</table></div></div>

<qa:pagination page="${faqs}" baseUrl="${ctx}/admin/faq" params="${paginationParams}"/>
