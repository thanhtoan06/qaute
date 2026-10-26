<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Sinh viên liên quan</title>
<div class="qa-page-manager-student-list" data-qa-view="manager-student-list">
<style>
.qa-page-manager-student-list { --page-accent: #258e89; --page-accent-dark: #1e6865; --page-tint: #eaf8f6; --page-line: #d2eeea; min-width: 0; color: #344c67; }
.qa-page-manager-student-list > .page-header, .qa-page-manager-student-list > .qa-page-heading, .qa-page-manager-student-list > .d-flex:has(> .page-title), .qa-page-manager-student-list > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #eaf8f6, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-manager-student-list > .page-header::after, .qa-page-manager-student-list > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #d2eeea; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-manager-student-list .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-manager-student-list .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #d2eeea; border-radius: 12px; color: #1e6865; background: linear-gradient(145deg,#fff,#eaf8f6); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-manager-student-list .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-manager-student-list .card:hover { border-color: #d2eeea; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-manager-student-list .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#eaf8f6); }
.qa-page-manager-student-list .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-manager-student-list .card-body { color: #435b75; }
.qa-page-manager-student-list .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#eaf8f6); }
.qa-page-manager-student-list .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-manager-student-list .form-control, .qa-page-manager-student-list .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-manager-student-list textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-manager-student-list .form-control:focus, .qa-page-manager-student-list .form-select:focus { border-color: #258e89; box-shadow: 0 0 0 4px #eaf8f6; }
.qa-page-manager-student-list .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-manager-student-list .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-manager-student-list .btn-primary { border-color: #258e89; background: linear-gradient(120deg,#258e89,#1e6865); box-shadow: 0 5px 13px #d2eeea; }
.qa-page-manager-student-list .btn-outline-primary { border-color: #d2eeea; color: #1e6865; background: #fff; }
.qa-page-manager-student-list .btn-outline-primary:hover { border-color: #258e89; color: #fff; background: linear-gradient(120deg,#258e89,#1e6865); }
.qa-page-manager-student-list .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#eaf8f6); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-manager-student-list .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-manager-student-list .table { margin-bottom: 0; color: #405872; }
.qa-page-manager-student-list .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#eaf8f6,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-manager-student-list .table tbody tr { transition: background-color .18s ease; }
.qa-page-manager-student-list .table tbody tr:hover { background: #eaf8f6; }
.qa-page-manager-student-list .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-manager-student-list .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-manager-student-list .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-manager-student-list .nav-tabs .nav-link.active { color: #1e6865; box-shadow: inset 0 -2px #258e89; }
.qa-page-manager-student-list .nav-pills .nav-link.active { color: #1e6865; background: #eaf8f6; box-shadow: inset 0 0 0 1px #d2eeea; }
.qa-page-manager-student-list .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-manager-student-list .progress-bar { border-radius: inherit; }
.qa-page-manager-student-list .table-responsive { overflow: auto; border: 1px solid #e3ebf5; border-radius: 15px; background: #fff; box-shadow: 0 8px 24px rgba(16,48,84,.05); }
.qa-page-manager-student-list .table { margin: 0; } .qa-page-manager-student-list .table thead th { position: sticky; top: 0; z-index: 1; background: linear-gradient(110deg,var(--page-tint),#f7faff); }
.qa-page-manager-student-list .table tbody tr { transition: background-color .18s ease; } .qa-page-manager-student-list .table tbody tr:hover { background: var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-manager-student-list > .page-header, .qa-page-manager-student-list > .qa-page-heading, .qa-page-manager-student-list > .d-flex:has(> .page-title), .qa-page-manager-student-list > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-manager-student-list .card { border-radius: 15px; }
  .qa-page-manager-student-list .card-body { padding: 1rem; }
  .qa-page-manager-student-list .table-responsive { border-radius: 11px; }
  .qa-page-manager-student-list .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-student-list *, .qa-page-manager-student-list *::before, .qa-page-manager-student-list *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="manager-student-list"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-person-lines-fill"></i></span>Sinh viên liên quan</h2>
  <span class="text-secondary small">Chỉ hiển thị sinh viên có yêu cầu trong phạm vi, lịch hẹn hoặc phiên chat với bạn.</span>
</div>

<%-- Tìm sinh viên có liên quan và mở hồ sơ chi tiết từ danh sách kết quả. --%>
<form method="get" action="${ctx}/manager/students" class="card card-body mb-3">
  <div class="row g-2 align-items-end">
    <div class="col-12 col-md-8">
      <label class="form-label" for="student-search">Tên, MSSV hoặc email</label>
      <input class="form-control" id="student-search" type="search" name="q" maxlength="150"
             value="<c:out value='${q}'/>" placeholder="Tìm sinh viên liên quan"/>
    </div>
    <div class="col-12 col-md-4 d-flex gap-2">
      <button class="btn btn-primary" type="submit">Tìm kiếm</button>
      <a class="btn btn-outline-secondary" href="${ctx}/manager/students">Bỏ lọc</a>
    </div>
  </div>
</form>

<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>Họ tên</th><th>MSSV</th><th>Email</th><th>Khoa</th><th>Lớp</th><th></th></tr></thead>
  <tbody>
  <c:choose>
    <c:when test="${empty students.content}">
      <tr><td colspan="6" class="text-center text-secondary py-5">Không có sinh viên phù hợp.</td></tr>
    </c:when>
    <c:otherwise>
      <c:forEach var="student" items="${students.content}">
        <tr>
          <td class="fw-semibold"><c:out value="${student.fullName()}"/></td>
          <td><c:out value="${student.mssv()}"/></td>
          <td><c:out value="${student.email()}"/></td>
          <td><c:out value="${student.facultyName()}"/></td>
          <td><c:out value="${student.classCode()}"/></td>
          <td><a class="btn btn-sm btn-outline-primary" href="${ctx}/manager/students/${student.id()}">Xem hồ sơ</a></td>
        </tr>
      </c:forEach>
    </c:otherwise>
  </c:choose>
  </tbody>
</table></div></div>

<qa:pagination page="${students}" baseUrl="${ctx}/manager/students" params="${paginationParams}"/>
</div>
