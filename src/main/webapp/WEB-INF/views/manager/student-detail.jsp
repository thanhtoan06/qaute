<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Hồ sơ sinh viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/manager/student-detail.css">
</head>
<div class="qa-page-manager-student-detail" data-qa-view="manager-student-detail">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="mb-3"><a href="${ctx}/manager/students" class="text-decoration-none">← Danh sách sinh viên</a></div>
<h2 class="page-title mb-3 qa-view-title" data-qa-page="manager-student-detail"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-person-lines-fill"></i></span>Hồ sơ sinh viên</h2>

<%-- Hiển thị thông tin sinh viên và các yêu cầu, lịch hẹn, phiên chat liên quan. --%>
<div class="card mb-4"><div class="card-body">
  <div class="row g-3">
    <div class="col-12 col-lg-4">
      <h3 class="card-title mb-2"><c:out value="${detail.student().user().fullName()}"/></h3>
      <div class="text-secondary">MSSV: <c:out value="${detail.student().user().mssv()}"/></div>
    </div>
    <div class="col-12 col-md-6 col-lg-4">
      <div><strong>Email:</strong> <c:out value="${detail.student().user().email()}"/></div>
      <div><strong>Điện thoại:</strong> <c:out value="${detail.student().phone()}" default="Chưa cập nhật"/></div>
      <div><strong>Khoa:</strong> <c:out value="${detail.student().facultyName()}" default="Chưa cập nhật"/></div>
    </div>
    <div class="col-12 col-md-6 col-lg-4">
      <div><strong>Lớp:</strong> <c:out value="${detail.student().classCode()}" default="Chưa cập nhật"/></div>
      <div><strong>Khóa:</strong> <c:out value="${detail.student().cohort()}" default="Chưa cập nhật"/></div>
      <div><strong>Chương trình:</strong> <c:out value="${detail.student().program()}" default="Chưa cập nhật"/></div>
    </div>
  </div>
</div></div>

<ul class="nav nav-tabs mb-3" role="tablist">
  <li class="nav-item"><button class="nav-link active" data-bs-toggle="tab" data-bs-target="#student-tickets" type="button" role="tab">Yêu cầu <span class="badge bg-secondary-lt ms-1">${detail.tickets().totalElements}</span></button></li>
  <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#student-appointments" type="button" role="tab">Lịch hẹn <span class="badge bg-secondary-lt ms-1">${detail.appointments().totalElements}</span></button></li>
  <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#student-chats" type="button" role="tab">Chat <span class="badge bg-secondary-lt ms-1">${detail.chats().totalElements}</span></button></li>
</ul>
<div class="tab-content">
  <div class="tab-pane fade show active" id="student-tickets" role="tabpanel">
    <div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
      <thead><tr><th>Mã</th><th>Tiêu đề</th><th>Trạng thái</th><th>Ngày tạo</th><th></th></tr></thead>
      <tbody><c:choose><c:when test="${empty detail.tickets().content}"><tr><td colspan="5" class="text-center text-secondary py-4">Không có yêu cầu trong phạm vi của bạn.</td></tr></c:when>
      <c:otherwise><c:forEach var="ticket" items="${detail.tickets().content}"><tr>
        <td><c:out value="${ticket.code()}"/></td><td><c:out value="${ticket.title()}"/></td>
        <td><qa:statusBadge status="${ticket.status()}"/></td><td><time datetime="${ticket.createdAt()}" data-qaute-datetime><c:out value="${ticket.createdAt()}"/></time></td>
        <td><a class="btn btn-sm btn-outline-primary" href="${ctx}/manager/tickets/${ticket.id()}">Mở</a></td>
      </tr></c:forEach></c:otherwise></c:choose></tbody>
    </table></div></div>
    <qa:pagination page="${detail.tickets()}" baseUrl="${ctx}/manager/students/${detail.student().user().id()}"/>
  </div>
  <div class="tab-pane fade" id="student-appointments" role="tabpanel">
    <div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
      <thead><tr><th>Thời gian</th><th>Chủ đề</th><th>Trạng thái</th><th></th></tr></thead>
      <tbody><c:choose><c:when test="${empty detail.appointments().content}"><tr><td colspan="4" class="text-center text-secondary py-4">Chưa có lịch hẹn với bạn.</td></tr></c:when>
      <c:otherwise><c:forEach var="appointment" items="${detail.appointments().content}"><tr>
        <td><time datetime="${appointment.startAt()}" data-qaute-datetime><c:out value="${appointment.startAt()}"/></time></td><td><c:out value="${appointment.topic()}"/></td>
        <td><c:out value="${appointment.status()}"/></td>
        <td><a class="btn btn-sm btn-outline-primary" href="${ctx}/manager/appointments/${appointment.id()}">Mở</a></td>
      </tr></c:forEach></c:otherwise></c:choose></tbody>
    </table></div></div>
    <qa:pagination page="${detail.appointments()}" baseUrl="${ctx}/manager/students/${detail.student().user().id()}"/>
  </div>
  <div class="tab-pane fade" id="student-chats" role="tabpanel">
    <div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
      <thead><tr><th>Bắt đầu</th><th>Câu hỏi</th><th>Trạng thái</th></tr></thead>
      <tbody><c:choose><c:when test="${empty detail.chats().content}"><tr><td colspan="3" class="text-center text-secondary py-4">Chưa có phiên chat với bạn.</td></tr></c:when>
      <c:otherwise><c:forEach var="chat" items="${detail.chats().content}"><tr>
        <td><time datetime="${chat.startedAt()}" data-qaute-datetime><c:out value="${chat.startedAt()}"/></time></td><td><c:out value="${chat.question()}"/></td>
        <td><c:out value="${chat.status()}"/></td>
      </tr></c:forEach></c:otherwise></c:choose></tbody>
    </table></div></div>
    <qa:pagination page="${detail.chats()}" baseUrl="${ctx}/manager/students/${detail.student().user().id()}"/>
  </div>
</div>
</div>
