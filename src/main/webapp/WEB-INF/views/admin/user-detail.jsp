<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Chi tiết người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/user-detail.css">
</head>
<div class="qa-page-admin-user-detail" data-qa-view="admin-user-detail">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và nút quay lại -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <div>
    <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-user-detail"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-person-vcard"></i></span><c:out value="${user.fullName()}"/></h2>
    <div class="text-secondary"><c:out value="${user.email()}"/></div>
  </div>
  <a class="btn btn-outline-secondary" href="${ctx}/admin/users"><i class="bi bi-arrow-left me-1"></i>Danh sách người dùng</a>
</div>

<div class="row row-cards">
  <!-- Thông tin tài khoản -->
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
          <dt class="col-sm-4 text-secondary">Đăng nhập gần nhất</dt><dd class="col-sm-8"><time datetime="${user.lastLoginAt()}" data-qaute-datetime><c:out value="${empty user.lastLoginAt() ? 'Chưa đăng nhập' : user.lastLoginAt()}"/></time></dd>
          <dt class="col-sm-4 text-secondary">Ngày tạo</dt><dd class="col-sm-8"><time datetime="${user.createdAt()}" data-qaute-datetime><c:out value="${user.createdAt()}"/></time></dd>
          <dt class="col-sm-4 text-secondary">Ticket liên quan</dt><dd class="col-sm-8"><span class="badge bg-blue-lt"><c:out value="${user.relatedTicketCount()}"/></span></dd>
        </dl>
      </div>
    </div>
  </div>

  <!-- Hồ sơ theo vai trò -->
  <div class="col-lg-5">
    <!-- Hồ sơ sinh viên -->
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

    <!-- Hồ sơ tư vấn viên -->
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

<!-- Các nút hành động theo trạng thái -->
<div id="actions" class="mt-3 d-flex flex-wrap gap-2">
  <a class="btn btn-outline-secondary" href="${ctx}/admin/users/${user.id()}/password"><i class="bi bi-key me-1"></i>Đổi mật khẩu</a>
  <a class="btn btn-outline-primary" href="${ctx}/admin/users/${user.id()}/security"><i class="bi bi-shield-lock me-1"></i>Bảo mật</a>
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

<!-- Form đổi vai trò (không thể đổi chính mình) -->
<c:if test="${user.id() != currentUserId}">
  <div class="card mt-3">
    <div class="card-header"><h3 class="card-title">Đổi vai trò</h3></div>
    <div class="card-body">
      <form method="post" action="${ctx}/admin/users/${user.id()}/role">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="row g-3 align-items-end">
          <div class="col-md-4">
            <label class="form-label required" for="new-role">Vai trò mới</label>
            <select class="form-select" id="new-role" name="role" required>
              <c:forEach var="role" items="${roles}">
                <c:if test="${role != user.role()}"><option value="${role}"><c:out value="${role}"/></option></c:if>
              </c:forEach>
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label required" for="role-reason">Lý do</label>
            <textarea class="form-control" id="role-reason" name="reason" rows="2" minlength="10" maxlength="500" required></textarea>
          </div>
          <div class="col-md-2"><button type="submit" class="btn btn-warning w-100">Đổi vai trò</button></div>
        </div>
      </form>
    </div>
  </div>
</c:if>
</div>
