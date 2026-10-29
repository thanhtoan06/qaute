<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Bảo mật người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/user-security.css">
</head>
<div class="qa-page-admin-user-security" data-qa-view="admin-user-security">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và nút quay lại -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <div>
    <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-user-security"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-shield-lock"></i></span>Bảo mật: <c:out value="${user.fullName()}"/></h2>
    <div class="text-secondary"><c:out value="${user.email()}"/></div>
  </div>
  <a class="btn btn-outline-secondary" href="${ctx}/admin/users/${user.id()}">Chi tiết người dùng</a>
</div>

<!-- Bảng phiên đăng nhập -->
<div class="card mb-3">
  <div class="card-header d-flex justify-content-between align-items-center">
    <h3 class="card-title">Phiên đăng nhập</h3>
    <!-- Nút thu hồi tất cả phiên -->
    <qa:confirmModal id="revoke-all-user-${user.id()}" title="Thu hồi mọi phiên"
                     message="Thu hồi tất cả phiên đăng nhập của người dùng này?"
                     actionUrl="${ctx}/admin/users/${user.id()}/sessions/revoke-all"
                     confirmLabel="Thu hồi tất cả" cssClass="btn-outline-danger"/>
  </div>
  <div class="table-responsive">
    <table class="table card-table table-vcenter">
      <thead><tr><th>Thiết bị</th><th>IP</th><th>Tạo lúc</th><th>Hoạt động cuối</th><th>Trạng thái</th><th></th></tr></thead>
      <tbody>
        <c:forEach var="session" items="${sessions}">
          <tr>
            <td><c:out value="${empty session.deviceInfo() ? '—' : session.deviceInfo()}"/></td>
            <td><c:out value="${empty session.ip() ? '—' : session.ip()}"/></td>
            <td><time datetime="${session.createdAt()}" data-qaute-datetime><c:out value="${session.createdAt()}"/></time></td>
            <td><time datetime="${session.lastUsedAt()}" data-qaute-datetime><c:out value="${empty session.lastUsedAt() ? '—' : session.lastUsedAt()}"/></time></td>
            <td><c:choose><c:when test="${session.revoked()}"><span class="badge bg-secondary-lt">Đã thu hồi</span></c:when><c:when test="${session.expired()}"><span class="badge bg-secondary-lt">Hết hạn</span></c:when><c:otherwise><span class="badge bg-green-lt">Còn hiệu lực</span></c:otherwise></c:choose></td>
            <!-- Nút thu hồi từng phiên -->
            <td>
              <c:if test="${!session.revoked() and !session.expired()}">
                <form method="post" action="${ctx}/admin/users/${user.id()}/sessions/${session.id()}/revoke">
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                  <button type="submit" class="btn btn-sm btn-outline-danger">Thu hồi</button>
                </form>
              </c:if>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty sessions}"><tr><td colspan="6" class="text-center text-secondary">Chưa có phiên đăng nhập.</td></tr></c:if>
      </tbody>
    </table>
  </div>
</div>

<!-- Bảng lịch sử đăng nhập -->
<div class="card">
  <div class="card-header"><h3 class="card-title">Lịch sử đăng nhập (50 lần gần nhất)</h3></div>
  <div class="table-responsive">
    <table class="table card-table table-vcenter">
      <thead><tr><th>Thời gian</th><th>Kết quả</th><th>IP</th><th>User-agent</th></tr></thead>
      <tbody>
        <c:forEach var="entry" items="${loginHistory}">
          <tr>
            <td><time datetime="${entry.at()}" data-qaute-datetime><c:out value="${entry.at()}"/></time></td>
            <td><c:choose><c:when test="${entry.success()}"><span class="badge bg-green-lt">Thành công</span></c:when><c:otherwise><span class="badge bg-red-lt">Thất bại</span></c:otherwise></c:choose></td>
            <td><c:out value="${empty entry.ip() ? '—' : entry.ip()}"/></td>
            <td class="text-wrap"><c:out value="${empty entry.userAgent() ? '—' : entry.userAgent()}"/></td>
          </tr>
        </c:forEach>
        <c:if test="${empty loginHistory}"><tr><td colspan="4" class="text-center text-secondary">Chưa có lịch sử đăng nhập.</td></tr></c:if>
      </tbody>
    </table>
  </div>
</div>
</div>
