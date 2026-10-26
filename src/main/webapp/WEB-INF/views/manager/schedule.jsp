<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Lịch làm việc</title>
<div class="qa-page-manager-schedule" data-qa-view="manager-schedule">
<style>
.qa-page-manager-schedule { --page-accent: #118c78; --page-accent-dark: #0b6e60; --page-tint: #e8f8f4; --page-line: #ccece3; min-width: 0; color: #344c67; }
.qa-page-manager-schedule > .page-header, .qa-page-manager-schedule > .qa-page-heading, .qa-page-manager-schedule > .d-flex:has(> .page-title), .qa-page-manager-schedule > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #e8f8f4, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-manager-schedule > .page-header::after, .qa-page-manager-schedule > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #ccece3; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-manager-schedule .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-manager-schedule .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #ccece3; border-radius: 12px; color: #0b6e60; background: linear-gradient(145deg,#fff,#e8f8f4); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-manager-schedule .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-manager-schedule .card:hover { border-color: #ccece3; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-manager-schedule .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#e8f8f4); }
.qa-page-manager-schedule .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-manager-schedule .card-body { color: #435b75; }
.qa-page-manager-schedule .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#e8f8f4); }
.qa-page-manager-schedule .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-manager-schedule .form-control, .qa-page-manager-schedule .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-manager-schedule textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-manager-schedule .form-control:focus, .qa-page-manager-schedule .form-select:focus { border-color: #118c78; box-shadow: 0 0 0 4px #e8f8f4; }
.qa-page-manager-schedule .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-manager-schedule .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-manager-schedule .btn-primary { border-color: #118c78; background: linear-gradient(120deg,#118c78,#0b6e60); box-shadow: 0 5px 13px #ccece3; }
.qa-page-manager-schedule .btn-outline-primary { border-color: #ccece3; color: #0b6e60; background: #fff; }
.qa-page-manager-schedule .btn-outline-primary:hover { border-color: #118c78; color: #fff; background: linear-gradient(120deg,#118c78,#0b6e60); }
.qa-page-manager-schedule .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#e8f8f4); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-manager-schedule .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-manager-schedule .table { margin-bottom: 0; color: #405872; }
.qa-page-manager-schedule .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#e8f8f4,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-manager-schedule .table tbody tr { transition: background-color .18s ease; }
.qa-page-manager-schedule .table tbody tr:hover { background: #e8f8f4; }
.qa-page-manager-schedule .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-manager-schedule .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-manager-schedule .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-manager-schedule .nav-tabs .nav-link.active { color: #0b6e60; box-shadow: inset 0 -2px #118c78; }
.qa-page-manager-schedule .nav-pills .nav-link.active { color: #0b6e60; background: #e8f8f4; box-shadow: inset 0 0 0 1px #ccece3; }
.qa-page-manager-schedule .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-manager-schedule .progress-bar { border-radius: inherit; }
.qa-page-manager-schedule .slot-btn, .qa-page-manager-schedule .appointment-choice-card, .qa-page-manager-schedule .advisor-card { border-radius: 14px; transition: transform .2s ease, box-shadow .2s ease, border-color .2s ease; } .qa-page-manager-schedule .slot-btn:hover, .qa-page-manager-schedule .appointment-choice-card:hover, .qa-page-manager-schedule .advisor-card:hover { transform: translateY(-2px); }
@media (max-width: 767.98px) {
  .qa-page-manager-schedule > .page-header, .qa-page-manager-schedule > .qa-page-heading, .qa-page-manager-schedule > .d-flex:has(> .page-title), .qa-page-manager-schedule > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-manager-schedule .card { border-radius: 15px; }
  .qa-page-manager-schedule .card-body { padding: 1rem; }
  .qa-page-manager-schedule .table-responsive { border-radius: 11px; }
  .qa-page-manager-schedule .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-schedule *, .qa-page-manager-schedule *::before, .qa-page-manager-schedule *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="manager-schedule"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-calendar-week"></i></span>Lịch làm việc</h2>
  <div class="btn-group">
    <a class="btn btn-outline-primary" href="${ctx}/manager/schedule?week=${previousWeek}">Tuần trước</a>
    <a class="btn btn-outline-primary" href="${ctx}/manager/schedule">Tuần này</a>
    <a class="btn btn-outline-primary" href="${ctx}/manager/schedule?week=${nextWeek}">Tuần sau</a>
  </div>
</div>
<c:if test="${not empty flashError}"><div class="alert alert-danger"><c:out value="${flashError}"/></div></c:if>
<c:if test="${not empty flashSuccess}"><div class="alert alert-success"><c:out value="${flashSuccess}"/></div></c:if>

<%-- Hiển thị lịch tư vấn theo tuần và trạng thái từng khung giờ. --%>
<div class="card mb-3">
  <div class="card-header"><h3 class="card-title">Tuần từ <c:out value="${weekStart}"/></h3></div>
  <div class="table-responsive">
    <table class="table table-bordered table-vcenter mb-0" style="min-width:1050px;table-layout:fixed">
      <thead><tr><th style="width:72px">Giờ</th><c:forEach var="day" items="${days}"><th><c:out value="${day}"/></th></c:forEach></tr></thead>
      <tbody><c:forEach var="row" items="${rows}"><tr>
        <th><c:out value="${row.hour()}"/>:00</th>
        <c:forEach var="cell" items="${row.cells()}"><td class="p-1 align-top">
          <c:forEach var="slot" items="${cell}">
            <c:choose>
              <c:when test="${slot.status() eq 'BOOKED'}"><c:set var="slotClass" value="bg-purple-lt"/></c:when>
              <c:when test="${slot.status() eq 'BLOCKED'}"><c:set var="slotClass" value="bg-secondary-lt"/></c:when>
              <c:when test="${slot.status() eq 'AVAILABLE'}"><c:set var="slotClass" value="bg-green-lt"/></c:when>
              <c:otherwise><c:set var="slotClass" value="bg-light"/></c:otherwise>
            </c:choose>
            <div class="rounded p-2 mb-1 ${slotClass} ${slot.past() ? 'opacity-50' : ''}">
              <strong><c:out value="${slot.startAt().toLocalTime()}"/>–<c:out value="${slot.endAt().toLocalTime()}"/></strong>
              <div class="small"><c:choose><c:when test="${slot.status() eq 'BOOKED'}">Đã đặt</c:when><c:when test="${slot.status() eq 'BLOCKED'}">Đã chặn</c:when><c:when test="${slot.status() eq 'EXPIRED'}">Đã qua</c:when><c:otherwise>Còn trống</c:otherwise></c:choose></div>
              <c:if test="${not empty slot.studentName()}"><div class="small"><c:out value="${slot.studentName()}"/></div></c:if>
              <c:if test="${not empty slot.appointmentId()}"><a class="small" href="${ctx}/manager/appointments/${slot.appointmentId()}">Xem lịch hẹn</a></c:if>
            </div>
          </c:forEach>
        </td></c:forEach>
      </tr></c:forEach></tbody>
    </table>
  </div>
</div>

<%-- Quản lý quy tắc lặp, giờ chặn và các khoảng thời gian không khả dụng. --%>
<div class="row g-3">
  <div class="col-12 col-xl-7">
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Quy tắc lặp hàng tuần</h3></div>
      <div class="table-responsive"><table class="table card-table table-vcenter">
        <thead><tr><th>Thứ</th><th>Giờ</th><th>Thời hạn</th><th>Hình thức</th><th></th></tr></thead>
        <tbody><c:choose><c:when test="${empty rules}"><tr><td colspan="5" class="text-secondary">Chưa có quy tắc.</td></tr></c:when><c:otherwise><c:forEach var="rule" items="${rules}"><tr>
          <td><c:out value="${rule.dayOfWeek}"/></td>
          <td><c:out value="${rule.startTime}"/>–<c:out value="${rule.endTime}"/> (<c:out value="${rule.slotMinutes}"/> phút)</td>
          <td><c:out value="${rule.validFrom}"/> → <c:out value="${empty rule.validTo ? 'Không giới hạn' : rule.validTo}"/></td>
          <td><c:out value="${rule.modality}"/></td>
          <td><form method="post" action="${ctx}/manager/schedule/rules/${rule.id}/delete"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-danger" onclick="return confirm('Tắt quy tắc này?')">Tắt</button></form></td>
        </tr></c:forEach></c:otherwise></c:choose></tbody>
      </table></div>
    </div>
    <div class="card"><div class="card-header"><h3 class="card-title">Thêm quy tắc</h3></div><div class="card-body">
      <form method="post" action="${ctx}/manager/schedule/rules">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <c:if test="${not empty ruleErrors}"><div class="alert alert-danger">Vui lòng kiểm tra các trường trong quy tắc.</div></c:if>
        <div class="row g-2">
          <div class="col-md-3"><label class="form-label required">Thứ</label><select class="form-select" name="dayOfWeek" required><c:forEach begin="1" end="7" var="n"><option value="${n}" ${ruleForm.dayOfWeek eq n ? 'selected' : ''}>${n eq 7 ? 'Chủ nhật' : n eq 1 ? 'Thứ Hai' : n eq 2 ? 'Thứ Ba' : n eq 3 ? 'Thứ Tư' : n eq 4 ? 'Thứ Năm' : n eq 5 ? 'Thứ Sáu' : 'Thứ Bảy'}</option></c:forEach></select></div>
          <div class="col-md-3"><label class="form-label required">Bắt đầu</label><input class="form-control" type="time" name="startTime" value="<c:out value='${ruleForm.startTime}'/>" required/></div>
          <div class="col-md-3"><label class="form-label required">Kết thúc</label><input class="form-control" type="time" name="endTime" value="<c:out value='${ruleForm.endTime}'/>" required/></div>
          <div class="col-md-3"><label class="form-label required">Phút/slot</label><select class="form-select" name="slotMinutes"><c:forEach items="15,30,45,60" var="minutes"><option value="${minutes}" ${ruleForm.slotMinutes eq minutes ? 'selected' : ''}>${minutes}</option></c:forEach></select></div>
          <div class="col-md-4"><label class="form-label required">Hình thức</label><select class="form-select" name="modality"><option value="ONLINE" ${ruleForm.modality eq 'ONLINE' ? 'selected' : ''}>Trực tuyến</option><option value="IN_PERSON" ${ruleForm.modality eq 'IN_PERSON' ? 'selected' : ''}>Trực tiếp</option><option value="BOTH" ${ruleForm.modality eq 'BOTH' ? 'selected' : ''}>Cả hai</option></select></div>
          <div class="col-md-4"><label class="form-label">Địa điểm</label><input class="form-control" name="location" maxlength="200" value="<c:out value='${ruleForm.location}'/>"/></div>
          <div class="col-md-4"><label class="form-label">Liên kết họp HTTPS</label><input class="form-control" type="url" name="meetingLink" maxlength="300" value="<c:out value='${ruleForm.meetingLink}'/>"/></div>
          <div class="col-md-4"><label class="form-label required">Có hiệu lực từ</label><input class="form-control" type="date" name="validFrom" value="<c:out value='${ruleForm.validFrom}'/>" required/></div>
          <div class="col-md-4"><label class="form-label">Đến</label><input class="form-control" type="date" name="validTo" value="<c:out value='${ruleForm.validTo}'/>"/></div>
        </div>
        <button class="btn btn-primary mt-3">Lưu quy tắc</button>
      </form>
    </div></div>
  </div>
  <div class="col-12 col-xl-5">
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Khung chặn</h3></div><div class="card-body">
      <c:choose><c:when test="${empty blocks}"><p class="text-secondary">Chưa có khung chặn sắp tới.</p></c:when><c:otherwise><c:forEach var="block" items="${blocks}">
        <div class="d-flex justify-content-between gap-2 border-bottom py-2"><div><strong><c:out value="${block.startAt}"/> – <c:out value="${block.endAt}"/></strong><div class="small text-secondary"><c:out value="${block.reason}"/></div></div>
        <form method="post" action="${ctx}/manager/schedule/blocks/${block.id}/delete"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-danger">Bỏ chặn</button></form></div>
      </c:forEach></c:otherwise></c:choose>
    </div></div>
    <div class="card"><div class="card-header"><h3 class="card-title">Thêm khung chặn</h3></div><div class="card-body">
      <form method="post" action="${ctx}/manager/schedule/blocks">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <c:if test="${not empty blockErrors}"><div class="alert alert-danger">Vui lòng kiểm tra khoảng thời gian.</div></c:if>
        <div class="mb-2"><label class="form-label required">Từ</label><input class="form-control" type="datetime-local" name="startAt" value="<c:out value='${blockForm.startAt}'/>" required/></div>
        <div class="mb-2"><label class="form-label required">Đến</label><input class="form-control" type="datetime-local" name="endAt" value="<c:out value='${blockForm.endAt}'/>" required/></div>
        <div class="mb-2"><label class="form-label">Lý do</label><input class="form-control" name="reason" maxlength="200" value="<c:out value='${blockForm.reason}'/>"/></div>
        <button class="btn btn-primary">Chặn khung giờ</button>
      </form>
    </div></div>
  </div>
</div>
</div>
