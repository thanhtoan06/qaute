<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Lịch làm việc</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/manager/schedule.css">
</head>
<div class="qa-page-manager-schedule" data-qa-view="manager-schedule">

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
    <table class="table table-bordered table-vcenter mb-0 u-min-w-1050px u-table-layout-fixed">
      <thead><tr><th class="u-width-72px">Giờ</th><c:forEach var="day" items="${days}"><th><c:out value="${day}"/></th></c:forEach></tr></thead>
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
          <td><form method="post" action="${ctx}/manager/schedule/rules/${rule.id}/delete"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-danger" data-confirm-message="Tắt quy tắc này?">Tắt</button></form></td>
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
