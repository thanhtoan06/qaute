<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chi tiết người dùng</title>
<div class="qa-page-admin-user-detail" data-qa-view="admin-user-detail">
<style>
.qa-page-admin-user-detail { --page-accent: #3579c7; --page-accent-dark: #285b94; --page-tint: #edf5ff; --page-line: #d6e6fa; min-width: 0; color: #344c67; }
.qa-page-admin-user-detail > .page-header, .qa-page-admin-user-detail > .qa-page-heading, .qa-page-admin-user-detail > .d-flex:has(> .page-title), .qa-page-admin-user-detail > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #edf5ff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-user-detail > .page-header::after, .qa-page-admin-user-detail > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #d6e6fa; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-user-detail .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-user-detail .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #d6e6fa; border-radius: 12px; color: #285b94; background: linear-gradient(145deg,#fff,#edf5ff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-user-detail .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-user-detail .card:hover { border-color: #d6e6fa; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-user-detail .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#edf5ff); }
.qa-page-admin-user-detail .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-user-detail .card-body { color: #435b75; }
.qa-page-admin-user-detail .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#edf5ff); }
.qa-page-admin-user-detail .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-user-detail .form-control, .qa-page-admin-user-detail .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-user-detail textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-user-detail .form-control:focus, .qa-page-admin-user-detail .form-select:focus { border-color: #3579c7; box-shadow: 0 0 0 4px #edf5ff; }
.qa-page-admin-user-detail .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-user-detail .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-user-detail .btn-primary { border-color: #3579c7; background: linear-gradient(120deg,#3579c7,#285b94); box-shadow: 0 5px 13px #d6e6fa; }
.qa-page-admin-user-detail .btn-outline-primary { border-color: #d6e6fa; color: #285b94; background: #fff; }
.qa-page-admin-user-detail .btn-outline-primary:hover { border-color: #3579c7; color: #fff; background: linear-gradient(120deg,#3579c7,#285b94); }
.qa-page-admin-user-detail .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#edf5ff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-user-detail .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-user-detail .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-user-detail .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#edf5ff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-user-detail .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-user-detail .table tbody tr:hover { background: #edf5ff; }
.qa-page-admin-user-detail .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-user-detail .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-user-detail .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-user-detail .nav-tabs .nav-link.active { color: #285b94; box-shadow: inset 0 -2px #3579c7; }
.qa-page-admin-user-detail .nav-pills .nav-link.active { color: #285b94; background: #edf5ff; box-shadow: inset 0 0 0 1px #d6e6fa; }
.qa-page-admin-user-detail .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-user-detail .progress-bar { border-radius: inherit; }
.qa-page-admin-user-detail .avatar { box-shadow: 0 8px 20px rgba(25,70,120,.12); }
@media (max-width: 767.98px) {
  .qa-page-admin-user-detail > .page-header, .qa-page-admin-user-detail > .qa-page-heading, .qa-page-admin-user-detail > .d-flex:has(> .page-title), .qa-page-admin-user-detail > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-user-detail .card { border-radius: 15px; }
  .qa-page-admin-user-detail .card-body { padding: 1rem; }
  .qa-page-admin-user-detail .table-responsive { border-radius: 11px; }
  .qa-page-admin-user-detail .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-user-detail *, .qa-page-admin-user-detail *::before, .qa-page-admin-user-detail *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
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
          <dt class="col-sm-4 text-secondary">Đăng nhập gần nhất</dt><dd class="col-sm-8"><c:out value="${empty user.lastLoginAt() ? 'Chưa đăng nhập' : user.lastLoginAt()}"/></dd>
          <dt class="col-sm-4 text-secondary">Ngày tạo</dt><dd class="col-sm-8"><c:out value="${user.createdAt()}"/></dd>
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
