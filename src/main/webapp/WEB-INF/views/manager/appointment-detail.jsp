<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chi tiết lịch hẹn</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="d-flex align-items-center justify-content-between mb-3"><h2 class="page-title mb-0">Chi tiết lịch hẹn</h2><a href="${ctx}/manager/appointments" class="btn btn-outline-secondary">Danh sách</a></div>
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
