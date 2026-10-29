<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>FAQ của tôi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/manager/faq-list.css">
</head>
<div class="qa-page-manager-faq-list" data-qa-view="manager-faq-list">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="manager-faq-list"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span>FAQ của tôi</h2>
  <a class="btn btn-primary" href="${ctx}/manager/faq/new">Đề xuất FAQ mới</a>
</div>

<%-- Lọc FAQ theo trạng thái và mở thao tác sửa hoặc xem bài đã xuất bản. --%>
<nav class="nav nav-tabs mb-3" aria-label="Lọc trạng thái FAQ">
  <a class="nav-link ${empty status ? 'active' : ''}" href="${ctx}/manager/faq">Tất cả</a>
  <c:forEach var="state" items="${statuses}"><a class="nav-link ${status eq state ? 'active' : ''}" href="${ctx}/manager/faq?status=${state}">
    <c:choose><c:when test="${state eq 'DRAFT'}">Nháp</c:when><c:when test="${state eq 'PENDING_REVIEW'}">Chờ duyệt</c:when><c:when test="${state eq 'PUBLISHED'}">Đã xuất bản</c:when><c:otherwise>Lưu trữ</c:otherwise></c:choose>
  </a></c:forEach>
</nav>

<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>Tiêu đề</th><th>Chuyên mục</th><th>Trạng thái</th><th>Cập nhật</th><th>Thao tác</th></tr></thead>
  <tbody><c:choose><c:when test="${empty faqs.content}"><tr><td colspan="5" class="text-secondary">Chưa có FAQ ở trạng thái này.</td></tr></c:when><c:otherwise>
    <c:forEach var="item" items="${faqs.content}"><tr>
      <td class="fw-semibold"><c:out value="${item.title()}"/></td>
      <td><c:out value="${item.categoryPath()}"/></td>
      <td><c:choose><c:when test="${item.status() eq 'DRAFT'}"><span class="badge bg-secondary-lt">Nháp</span></c:when><c:when test="${item.status() eq 'PENDING_REVIEW'}"><span class="badge bg-yellow-lt">Chờ duyệt</span></c:when><c:when test="${item.status() eq 'PUBLISHED'}"><span class="badge bg-green-lt">Đã xuất bản</span></c:when><c:otherwise><span class="badge bg-gray-lt">Lưu trữ</span></c:otherwise></c:choose></td>
      <td><time datetime="${item.updatedAt()}" data-qaute-datetime><c:out value="${item.updatedAt()}"/></time></td>
      <td><c:choose><c:when test="${item.status() eq 'DRAFT'}"><a class="btn btn-sm btn-outline-primary" href="${ctx}/manager/faq/${item.id()}/edit">Sửa và gửi duyệt</a></c:when><c:when test="${item.status() eq 'PUBLISHED'}"><a class="btn btn-sm btn-outline-primary" href="${ctx}/faq/${fn:escapeXml(item.slug())}">Xem bài</a></c:when><c:otherwise><span class="text-secondary">—</span></c:otherwise></c:choose></td>
    </tr></c:forEach>
  </c:otherwise></c:choose></tbody>
</table></div></div>
<c:set var="paginationParams" value=""/>
<c:if test="${not empty status}"><c:set var="paginationParams" value="status=${status}"/></c:if>
<qa:pagination page="${faqs}" baseUrl="${ctx}/manager/faq" params="${paginationParams}"/>
</div>
