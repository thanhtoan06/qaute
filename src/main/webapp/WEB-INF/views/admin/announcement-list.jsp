<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Quản lý thông báo</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="d-flex justify-content-between align-items-center mb-3">
  <h2 class="page-title mb-0">Quản lý thông báo</h2>
  <a class="btn btn-primary" href="${ctx}/admin/announcements/new">Soạn thông báo</a>
</div>
<div class="nav nav-tabs mb-3">
  <a class="nav-link ${empty status ? 'active' : ''}" href="${ctx}/admin/announcements">Tất cả</a>
  <c:forEach var="s" items="${statuses}"><a class="nav-link ${status eq s ? 'active' : ''}" href="${ctx}/admin/announcements?status=${s}"><c:out value="${s}"/></a></c:forEach>
</div>
<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>Tiêu đề</th><th>Đối tượng</th><th>Trạng thái</th><th>Đăng lúc</th><th>Hết hạn</th><th>Thao tác</th></tr></thead>
  <tbody>
  <c:choose><c:when test="${empty announcements.content}"><tr><td colspan="6" class="text-center text-secondary py-4">Chưa có thông báo.</td></tr></c:when><c:otherwise>
    <c:forEach var="a" items="${announcements.content}"><tr>
      <td><span class="fw-semibold"><c:out value="${a.title}"/></span><c:if test="${a.pinned}"><span class="badge bg-yellow-lt ms-2">Ghim</span></c:if><div class="small text-secondary"><c:out value="${a.summary}"/></div></td>
      <td><c:out value="${a.audience}"/></td>
      <td><span class="badge ${a.status eq 'PUBLISHED' ? 'bg-green-lt' : a.status eq 'EXPIRED' ? 'bg-gray-lt' : 'bg-secondary-lt'}"><c:out value="${a.status}"/></span></td>
      <td><c:out value="${a.publishAt}"/></td><td><c:out value="${a.expiresAt}"/></td>
      <td><div class="d-flex flex-wrap gap-1"><c:if test="${a.status eq 'DRAFT'}">
        <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/announcements/${a.id}/edit">Sửa</a>
        <form method="post" action="${ctx}/admin/announcements/${a.id}/publish"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-success" type="submit">Xuất bản</button></form>
        <form method="post" action="${ctx}/admin/announcements/${a.id}/delete" onsubmit="return confirm('Xóa bản nháp này?')"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-danger" type="submit">Xóa</button></form>
      </c:if><c:if test="${a.status eq 'PUBLISHED'}"><form method="post" action="${ctx}/admin/announcements/${a.id}/unpublish"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-secondary" type="submit">Gỡ đăng</button></form></c:if></div></td>
    </tr></c:forEach>
  </c:otherwise></c:choose>
  </tbody>
</table></div></div>
<c:if test="${announcements.totalPages gt 1}"><nav class="mt-3" aria-label="Phân trang"><ul class="pagination"><c:forEach begin="0" end="${announcements.totalPages - 1}" var="p"><li class="page-item ${p eq announcements.number ? 'active' : ''}"><a class="page-link" href="${ctx}/admin/announcements?page=${p}<c:if test='${not empty status}'>&amp;status=${status}</c:if>">${p + 1}</a></li></c:forEach></ul></nav></c:if>
