<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Lịch hẹn tư vấn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/manager/appointments.css">
</head>
<div class="qa-page-manager-appointments" data-qa-view="manager-appointments">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%-- Chuyển giữa lịch hẹn theo thời gian và xem chi tiết từng buổi tư vấn. --%>
<h2 class="page-title mb-3 qa-view-title" data-qa-page="manager-appointments"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-calendar-check"></i></span>Lịch hẹn tư vấn</h2>
<c:if test="${not empty flashError}"><div class="alert alert-danger"><c:out value="${flashError}"/></div></c:if>
<c:if test="${not empty flashSuccess}"><div class="alert alert-success"><c:out value="${flashSuccess}"/></div></c:if>
<div class="nav nav-tabs mb-3">
  <a class="nav-link ${tab eq 'today' ? 'active' : ''}" href="${ctx}/manager/appointments?tab=today">Hôm nay</a>
  <a class="nav-link ${tab eq 'upcoming' ? 'active' : ''}" href="${ctx}/manager/appointments?tab=upcoming">Sắp tới</a>
  <a class="nav-link ${tab eq 'past' ? 'active' : ''}" href="${ctx}/manager/appointments?tab=past">Đã qua</a>
</div>
<c:choose>
  <c:when test="${empty result.content}"><div class="card"><div class="card-body text-secondary">Chưa có lịch hẹn trong mục này.</div></div></c:when>
  <c:otherwise><div class="row g-3">
    <c:forEach var="item" items="${result.content}"><div class="col-12 col-lg-6">
      <div class="card h-100"><div class="card-body">
        <div class="d-flex justify-content-between gap-2 mb-2"><strong><time datetime="${item.startAt()}" data-qaute-datetime><c:out value="${item.startAt()}"/></time> – <c:out value="${item.endAt().toLocalTime()}"/></strong><span class="badge bg-blue-lt"><c:out value="${item.status()}"/></span></div>
        <h3 class="card-title"><c:out value="${item.topic()}"/></h3>
        <div>Sinh viên: <strong><c:out value="${item.student().user().fullName()}"/></strong></div>
        <div class="text-secondary"><c:out value="${item.categoryPath()}"/></div>
        <div>Hình thức: <c:out value="${item.modality()}"/></div>
        <c:if test="${not empty item.ticketCode()}"><div>Ticket: <c:out value="${item.ticketCode()}"/></div></c:if>
        <a class="btn btn-outline-primary mt-3" href="${ctx}/manager/appointments/${item.id()}">Xem chi tiết</a>
      </div></div>
    </div></c:forEach>
  </div></c:otherwise>
</c:choose>
<c:set var="paginationParams" value="tab=${tab}"/>
<qa:pagination page="${result}" baseUrl="${ctx}/manager/appointments" params="${paginationParams}"/>
</div>
