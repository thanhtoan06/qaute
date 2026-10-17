<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chi tiết người dùng</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <div>
    <h2 class="page-title mb-0"><c:out value="${user.fullName()}"/></h2>
    <div class="text-secondary"><c:out value="${user.email()}"/></div>
  </div>
  <a class="btn btn-outline-secondary" href="${ctx}/admin/users"><i class="bi bi-arrow-left me-1"></i>Danh sách người dùng</a>
</div>

<div class="row row-cards">
  <div class="col-lg-7">
    <div class="card">
      <div class="card-header"><h3 class="card-title">Thông tin tài khoản</h3></div>
      <div class="card-body">
        <dl class="row mb-0">
          <dt class="col-sm-4 text-secondary">Họ và tên</dt><dd class="col-sm-8"><c:out value="${user.fullName()}"/></dd>
          <dt class="col-sm-4 text-secondary">Email</dt><dd class="col-sm-8"><c:out value="${user.email()}"/></dd>
          <dt class="col-sm-4 text-secondary">MSSV</dt><dd class="col-sm-8"><c:out value="${empty user.mssv() ? '—' : user.mssv()}"/></dd>
          <dt class="col-sm-4 text-secondary">Số điện thoại</dt><dd class="col-sm-8"><c:out value="${empty user.phone() ? '—' : user.phone()}"/></dd>
          <dt class="col-sm-4 text-secondary">Vai trò</dt>
          <dd class="col-sm-8"><c:choose><c:when test="${user.role() == 'STUDENT'}">Sinh viên</c:when><c:when test="${user.role() == 'MANAGER'}">Tư vấn viên</c:when><c:otherwise>Quản trị viên</c:otherwise></c:choose></dd>
          <dt class="col-sm-4 text-secondary">Trạng thái</dt><dd class="col-sm-8"><span class="badge bg-secondary-lt"><c:out value="${user.status()}"/></span></dd>
          <dt class="col-sm-4 text-secondary">Đăng nhập gần nhất</dt><dd class="col-sm-8"><c:out value="${empty user.lastLoginAt() ? 'Chưa đăng nhập' : user.lastLoginAt()}"/></dd>
          <dt class="col-sm-4 text-secondary">Ngày tạo</dt><dd class="col-sm-8"><c:out value="${user.createdAt()}"/></dd>
          <dt class="col-sm-4 text-secondary">Ticket liên quan</dt><dd class="col-sm-8"><span class="badge bg-blue-lt"><c:out value="${user.relatedTicketCount()}"/></span></dd>
        </dl>
      </div>
    </div>
  </div>

  <div class="col-lg-5">
    <c:if test="${user.role() == 'STUDENT'}">
      <div class="card">
        <div class="card-header"><h3 class="card-title">Hồ sơ sinh viên</h3></div>
        <div class="card-body">
          <dl class="row mb-0">
            <dt class="col-sm-5 text-secondary">Khoa</dt><dd class="col-sm-7"><c:out value="${empty user.facultyName() ? '—' : user.facultyName()}"/></dd>
            <dt class="col-sm-5 text-secondary">Lớp</dt><dd class="col-sm-7"><c:out value="${empty user.classCode() ? '—' : user.classCode()}"/></dd>
            <dt class="col-sm-5 text-secondary">Khóa</dt><dd class="col-sm-7"><c:out value="${empty user.cohort() ? '—' : user.cohort()}"/></dd>
            <dt class="col-sm-5 text-secondary">Chương trình</dt><dd class="col-sm-7"><c:out value="${empty user.program() ? '—' : user.program()}"/></dd>
          </dl>
        </div>
      </div>
    </c:if>

    <c:if test="${user.role() == 'MANAGER'}">
      <div class="card">
        <div class="card-header"><h3 class="card-title">Hồ sơ tư vấn viên</h3></div>
        <div class="card-body">
          <dl class="row mb-3">
            <dt class="col-sm-5 text-secondary">Phòng ban</dt><dd class="col-sm-7"><c:out value="${empty user.departmentName() ? '—' : user.departmentName()}"/></dd>
            <dt class="col-sm-5 text-secondary">Chức danh</dt><dd class="col-sm-7"><c:out value="${empty user.jobTitle() ? '—' : user.jobTitle()}"/></dd>
          </dl>
          <div class="text-secondary small mb-1">Chuyên mục phụ trách</div>
          <c:choose>
            <c:when test="${empty user.categoryNames()}"><span class="text-secondary">Chưa được phân công</span></c:when>
            <c:otherwise><ul class="mb-0 ps-3"><c:forEach var="categoryName" items="${user.categoryNames()}"><li><c:out value="${categoryName}"/></li></c:forEach></ul></c:otherwise>
          </c:choose>
        </div>
      </div>
    </c:if>
  </div>
</div>

<div id="actions" class="mt-3 d-flex flex-wrap gap-2">
  <a class="btn btn-primary" href="${ctx}/admin/users/${user.id()}/edit"><i class="bi bi-pencil me-1"></i>Sửa</a>
  <c:if test="${user.status() == 'PENDING_ACTIVATION'}">
    <form method="post" action="${ctx}/admin/users/${user.id()}/resend-invitation">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <button class="btn btn-outline-primary" type="submit">Gửi lại lời mời</button>
    </form>
  </c:if>
  <c:if test="${user.status() == 'ACTIVE'}">
    <c:if test="${user.id() != currentUserId}">
      <qa:confirmModal id="lock-user-${user.id()}" title="Khóa tài khoản"
                       message="Khóa tài khoản của ${user.fullName()} và thu hồi mọi phiên đăng nhập?"
                       actionUrl="${ctx}/admin/users/${user.id()}/lock" confirmLabel="Khóa"
                       requireReason="true"/>
    </c:if>
    <qa:confirmModal id="reset-user-${user.id()}" title="Buộc đặt lại mật khẩu"
                     message="Gửi liên kết đặt lại mật khẩu và thu hồi mọi phiên đăng nhập của ${user.fullName()}?"
                     actionUrl="${ctx}/admin/users/${user.id()}/force-reset"
                     confirmLabel="Gửi liên kết" cssClass="btn-outline-warning"/>
  </c:if>
  <c:if test="${user.status() == 'LOCKED'}">
    <qa:confirmModal id="unlock-user-${user.id()}" title="Mở khóa tài khoản"
                     message="Mở khóa tài khoản của ${user.fullName()}?"
                     actionUrl="${ctx}/admin/users/${user.id()}/unlock"
                     confirmLabel="Mở khóa" cssClass="btn-success"/>
  </c:if>
  <c:if test="${user.status() != 'DEACTIVATED' and user.id() != currentUserId}">
    <qa:confirmModal id="deactivate-user-${user.id()}" title="Vô hiệu hóa tài khoản"
                     message="Vô hiệu hóa tài khoản của ${user.fullName()} và thu hồi mọi phiên đăng nhập?"
                     actionUrl="${ctx}/admin/users/${user.id()}/deactivate"
                     confirmLabel="Vô hiệu hóa" requireReason="true"/>
  </c:if>
  <c:if test="${user.status() == 'DEACTIVATED'}">
    <qa:confirmModal id="reactivate-user-${user.id()}" title="Kích hoạt lại tài khoản"
                     message="Kích hoạt lại tài khoản của ${user.fullName()}?"
                     actionUrl="${ctx}/admin/users/${user.id()}/reactivate"
                     confirmLabel="Kích hoạt lại" cssClass="btn-success"/>
  </c:if>
</div>
