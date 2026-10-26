<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chi tiết lịch hẹn</title>
<div class="qa-page-manager-appointment-detail" data-qa-view="manager-appointment-detail">
<style>
.qa-page-manager-appointment-detail { --page-accent: #118c78; --page-accent-dark: #0b6e60; --page-tint: #e8f8f4; --page-line: #ccece3; min-width: 0; color: #344c67; }
.qa-page-manager-appointment-detail > .page-header, .qa-page-manager-appointment-detail > .qa-page-heading, .qa-page-manager-appointment-detail > .d-flex:has(> .page-title), .qa-page-manager-appointment-detail > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #e8f8f4, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-manager-appointment-detail > .page-header::after, .qa-page-manager-appointment-detail > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #ccece3; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-manager-appointment-detail .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-manager-appointment-detail .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #ccece3; border-radius: 12px; color: #0b6e60; background: linear-gradient(145deg,#fff,#e8f8f4); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-manager-appointment-detail .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-manager-appointment-detail .card:hover { border-color: #ccece3; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-manager-appointment-detail .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#e8f8f4); }
.qa-page-manager-appointment-detail .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-manager-appointment-detail .card-body { color: #435b75; }
.qa-page-manager-appointment-detail .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#e8f8f4); }
.qa-page-manager-appointment-detail .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-manager-appointment-detail .form-control, .qa-page-manager-appointment-detail .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-manager-appointment-detail textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-manager-appointment-detail .form-control:focus, .qa-page-manager-appointment-detail .form-select:focus { border-color: #118c78; box-shadow: 0 0 0 4px #e8f8f4; }
.qa-page-manager-appointment-detail .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-manager-appointment-detail .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-manager-appointment-detail .btn-primary { border-color: #118c78; background: linear-gradient(120deg,#118c78,#0b6e60); box-shadow: 0 5px 13px #ccece3; }
.qa-page-manager-appointment-detail .btn-outline-primary { border-color: #ccece3; color: #0b6e60; background: #fff; }
.qa-page-manager-appointment-detail .btn-outline-primary:hover { border-color: #118c78; color: #fff; background: linear-gradient(120deg,#118c78,#0b6e60); }
.qa-page-manager-appointment-detail .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#e8f8f4); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-manager-appointment-detail .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-manager-appointment-detail .table { margin-bottom: 0; color: #405872; }
.qa-page-manager-appointment-detail .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#e8f8f4,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-manager-appointment-detail .table tbody tr { transition: background-color .18s ease; }
.qa-page-manager-appointment-detail .table tbody tr:hover { background: #e8f8f4; }
.qa-page-manager-appointment-detail .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-manager-appointment-detail .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-manager-appointment-detail .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-manager-appointment-detail .nav-tabs .nav-link.active { color: #0b6e60; box-shadow: inset 0 -2px #118c78; }
.qa-page-manager-appointment-detail .nav-pills .nav-link.active { color: #0b6e60; background: #e8f8f4; box-shadow: inset 0 0 0 1px #ccece3; }
.qa-page-manager-appointment-detail .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-manager-appointment-detail .progress-bar { border-radius: inherit; }
.qa-page-manager-appointment-detail .slot-btn, .qa-page-manager-appointment-detail .appointment-choice-card, .qa-page-manager-appointment-detail .advisor-card { border-radius: 14px; transition: transform .2s ease, box-shadow .2s ease, border-color .2s ease; } .qa-page-manager-appointment-detail .slot-btn:hover, .qa-page-manager-appointment-detail .appointment-choice-card:hover, .qa-page-manager-appointment-detail .advisor-card:hover { transform: translateY(-2px); }
@media (max-width: 767.98px) {
  .qa-page-manager-appointment-detail > .page-header, .qa-page-manager-appointment-detail > .qa-page-heading, .qa-page-manager-appointment-detail > .d-flex:has(> .page-title), .qa-page-manager-appointment-detail > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-manager-appointment-detail .card { border-radius: 15px; }
  .qa-page-manager-appointment-detail .card-body { padding: 1rem; }
  .qa-page-manager-appointment-detail .table-responsive { border-radius: 11px; }
  .qa-page-manager-appointment-detail .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-appointment-detail *, .qa-page-manager-appointment-detail *::before, .qa-page-manager-appointment-detail *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%-- Hiển thị thông tin lịch hẹn, sinh viên và các thao tác hoàn tất hoặc hủy. --%>
<div class="d-flex align-items-center justify-content-between mb-3"><h2 class="page-title mb-0 qa-view-title" data-qa-page="manager-appointment-detail"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-calendar-check"></i></span>Chi tiết lịch hẹn</h2><a href="${ctx}/manager/appointments" class="btn btn-outline-secondary">Danh sách</a></div>
<c:if test="${not empty flashError}"><div class="alert alert-danger"><c:out value="${flashError}"/></div></c:if>
<c:if test="${not empty flashSuccess}"><div class="alert alert-success"><c:out value="${flashSuccess}"/></div></c:if>
<div class="row g-3">
  <div class="col-12 col-lg-7"><div class="card"><div class="card-header"><h3 class="card-title"><c:out value="${appointment.topic()}"/></h3></div><div class="card-body">
    <dl class="row mb-0">
      <dt class="col-sm-4">Thời gian</dt><dd class="col-sm-8"><c:out value="${appointment.startAt()}"/> – <c:out value="${appointment.endAt().toLocalTime()}"/></dd>
      <dt class="col-sm-4">Trạng thái</dt><dd class="col-sm-8"><span class="badge bg-blue-lt"><c:out value="${appointment.status()}"/></span></dd>
      <dt class="col-sm-4">Hình thức</dt><dd class="col-sm-8"><c:out value="${appointment.modality()}"/></dd>
      <dt class="col-sm-4">Địa điểm</dt><dd class="col-sm-8"><c:out value="${appointment.location()}"/></dd>
      <dt class="col-sm-4">Liên kết họp</dt><dd class="col-sm-8"><c:out value="${appointment.meetingLink()}"/></dd>
      <dt class="col-sm-4">Chuyên mục</dt><dd class="col-sm-8"><c:out value="${appointment.categoryPath()}"/></dd>
      <dt class="col-sm-4">Ticket liên quan</dt><dd class="col-sm-8"><c:if test="${not empty appointment.ticketCode()}"><a href="${ctx}/manager/tickets/${appointment.ticketId()}"><c:out value="${appointment.ticketCode()}"/></a></c:if></dd>
      <dt class="col-sm-4">Ghi chú của SV</dt><dd class="col-sm-8"><c:out value="${appointment.note()}"/></dd>
      <c:if test="${not empty appointment.outcomeSummary()}"><dt class="col-sm-4">Kết luận</dt><dd class="col-sm-8"><c:out value="${appointment.outcomeSummary()}"/></dd></c:if>
      <c:if test="${not empty appointment.studentVisibleSummary()}"><dt class="col-sm-4">Cho sinh viên</dt><dd class="col-sm-8"><c:out value="${appointment.studentVisibleSummary()}"/></dd></c:if>
      <c:if test="${not empty appointment.cancelReason()}"><dt class="col-sm-4">Lý do hủy</dt><dd class="col-sm-8"><c:out value="${appointment.cancelReason()}"/></dd></c:if>
    </dl>
  </div></div></div>
  <div class="col-12 col-lg-5"><div class="card mb-3"><div class="card-header"><h3 class="card-title">Sinh viên</h3></div><div class="card-body">
    <div class="fw-semibold"><c:out value="${appointment.student().user().fullName()}"/></div>
    <div>MSSV: <c:out value="${appointment.student().user().mssv()}"/></div>
    <div>Email: <c:out value="${appointment.student().user().email()}"/></div>
    <div>Điện thoại: <c:out value="${appointment.student().phone()}"/></div>
    <div>Khoa: <c:out value="${appointment.student().facultyName()}"/></div>
    <div>Lớp: <c:out value="${appointment.student().classCode()}"/></div>
  </div></div>
  <%-- Ghi nhận kết luận buổi tư vấn hoặc đánh dấu sinh viên vắng mặt. --%>
  <c:if test="${appointment.canComplete()}"><div class="card mb-3"><div class="card-header"><h3 class="card-title">Kết luận buổi tư vấn</h3></div><div class="card-body">
    <form method="post" action="${ctx}/manager/appointments/${appointment.id()}/complete">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <label class="form-label required" for="outcomeSummary">Kết luận nội bộ</label>
      <textarea class="form-control" id="outcomeSummary" name="outcomeSummary" minlength="10" maxlength="2000" rows="4" required><c:out value="${outcomeForm.outcomeSummary}"/></textarea>
      <c:if test="${not empty outcomeErrors.outcomeSummary}"><div class="text-danger small"><c:out value="${outcomeErrors.outcomeSummary}"/></div></c:if>
      <label class="form-label mt-3" for="studentVisibleSummary">Tóm tắt gửi sinh viên (tùy chọn)</label>
      <textarea class="form-control" id="studentVisibleSummary" name="studentVisibleSummary" maxlength="1000" rows="3"><c:out value="${outcomeForm.studentVisibleSummary}"/></textarea>
      <button class="btn btn-primary mt-3" type="submit">Hoàn tất lịch hẹn</button>
    </form>
    <form method="post" action="${ctx}/manager/appointments/${appointment.id()}/no-show" class="mt-3"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button type="submit" class="btn btn-outline-warning">Sinh viên vắng mặt</button></form>
  </div></div></c:if>
  <c:if test="${appointment.canCancel()}"><qa:confirmModal id="cancel-appointment" title="Hủy lịch hẹn" message="Sinh viên sẽ được thông báo và có thể đặt lại lịch." actionUrl="${ctx}/manager/appointments/${appointment.id()}/cancel" confirmLabel="Hủy lịch" requireReason="${true}"/></c:if>
  </div>
</div>
</div>
