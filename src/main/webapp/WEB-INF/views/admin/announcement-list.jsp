<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Quản lý thông báo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/announcement-list.css">
</head>
<div class="qa-page-admin-announcement-list" data-qa-view="admin-announcement-list">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- Tiêu đề trang và nút tạo thông báo mới -->
<div class="d-flex justify-content-between align-items-center mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-announcement-list"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-megaphone"></i></span>Quản lý thông báo</h2>
  <a class="btn btn-primary" href="${ctx}/admin/announcements/new">Soạn thông báo</a>
</div>
<%-- Lọc thông báo theo trạng thái và quản lý bản nháp hoặc nội dung đã đăng. --%>
<!-- Tab lọc theo trạng thái: tất cả, nháp, đã đăng, hết hạn -->
<div class="nav nav-tabs mb-3">
  <a class="nav-link ${empty status ? 'active' : ''}" href="${ctx}/admin/announcements">Tất cả</a>
  <c:forEach var="s" items="${statuses}"><a class="nav-link ${status eq s ? 'active' : ''}" href="${ctx}/admin/announcements?status=${s}"><c:out value="${s}"/></a></c:forEach>
</div>
<!-- Danh sách thông báo với thông tin và các nút thao tác -->
<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>Tiêu đề</th><th>Đối tượng</th><th>Trạng thái</th><th>Đăng lúc</th><th>Hết hạn</th><th>Thao tác</th></tr></thead>
  <tbody>
  <c:choose><c:when test="${empty announcements.content}"><tr><td colspan="6" class="text-center text-secondary py-4">Chưa có thông báo.</td></tr></c:when><c:otherwise>
    <c:forEach var="a" items="${announcements.content}"><tr>
      <!-- Hiển thị tiêu đề, badge ghim, tóm tắt -->
      <td><span class="fw-semibold"><c:out value="${a.title}"/></span><c:if test="${a.pinned}"><span class="badge bg-yellow-lt ms-2">Ghim</span></c:if><div class="small text-secondary"><c:out value="${a.summary}"/></div></td>
      <td><c:out value="${a.audience}"/></td>
      <td><span class="badge ${a.status eq 'PUBLISHED' ? 'bg-green-lt' : a.status eq 'EXPIRED' ? 'bg-gray-lt' : 'bg-secondary-lt'}"><c:out value="${a.status}"/></span></td>
      <td><time datetime="${a.publishAt}" data-qaute-datetime><c:out value="${a.publishAt}"/></time></td><td><time datetime="${a.expiresAt}" data-qaute-datetime><c:out value="${a.expiresAt}"/></time></td>
      <!-- Các nút thao tác: sửa, xuất bản, xóa, gỡ đăng -->
      <td><div class="d-flex flex-wrap gap-1"><c:if test="${a.status eq 'DRAFT'}">
        <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/announcements/${a.id}/edit">Sửa</a>
        <form method="post" action="${ctx}/admin/announcements/${a.id}/publish"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-success" type="submit">Xuất bản</button></form>
        <form method="post" action="${ctx}/admin/announcements/${a.id}/delete" data-confirm-message="Xóa bản nháp này?"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-danger" type="submit">Xóa</button></form>
      </c:if><c:if test="${a.status eq 'PUBLISHED'}"><form method="post" action="${ctx}/admin/announcements/${a.id}/unpublish"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-secondary" type="submit">Gỡ đăng</button></form></c:if></div></td>
    </tr></c:forEach>
  </c:otherwise></c:choose>
  </tbody>
</table></div></div>
<!-- Phân trang -->
<c:if test="${announcements.totalPages gt 1}"><nav class="mt-3" aria-label="Phân trang"><ul class="pagination"><c:forEach begin="0" end="${announcements.totalPages - 1}" var="p"><li class="page-item ${p eq announcements.number ? 'active' : ''}"><a class="page-link" href="${ctx}/admin/announcements?page=${p}<c:if test='${not empty status}'>&amp;status=${status}</c:if>">${p + 1}</a></li></c:forEach></ul></nav></c:if>
</div>
