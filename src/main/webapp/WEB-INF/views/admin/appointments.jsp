<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Lịch hẹn toàn hệ thống</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/appointments.css">
</head>
<div class="qa-page-admin-appointments" data-qa-view="admin-appointments">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-appointments"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-calendar-check"></i></span>Lịch hẹn toàn hệ thống</h2>
  <span class="text-secondary">${appointments.totalElements} lịch hẹn</span>
</div>

<form method="get" action="${ctx}/admin/appointments" class="qa-filter-bar mb-3">
  <div><label class="form-label" for="appointment-q">Chủ đề</label><input class="form-control" id="appointment-q" type="search" name="q" maxlength="200" value="<c:out value='${filter.q}'/>" placeholder="Tìm theo chủ đề"/></div>
  <div><label class="form-label" for="appointment-status">Trạng thái</label><select class="form-select" id="appointment-status" name="status"><option value="">Tất cả</option><c:forEach var="item" items="${statuses}"><option value="${item}" ${filter.status eq item ? 'selected' : ''}><c:out value="${item}"/></option></c:forEach></select></div>
  <div><label class="form-label" for="appointment-modality">Hình thức</label><select class="form-select" id="appointment-modality" name="modality"><option value="">Tất cả</option><c:forEach var="item" items="${modalities}"><option value="${item}" ${filter.modality eq item ? 'selected' : ''}><c:choose><c:when test="${item eq 'ONLINE'}">Trực tuyến</c:when><c:when test="${item eq 'IN_PERSON'}">Trực tiếp</c:when><c:otherwise>Linh hoạt</c:otherwise></c:choose></option></c:forEach></select></div>
  <div><label class="form-label" for="appointment-manager">Tư vấn viên</label><select class="form-select" id="appointment-manager" name="managerId"><option value="">Tất cả</option><c:forEach var="item" items="${managers}"><option value="${item.id}" ${filter.managerId eq item.id ? 'selected' : ''}><c:out value="${item.fullName}"/></option></c:forEach></select></div>
  <div><label class="form-label" for="appointment-from">Ngày hẹn từ</label><input class="form-control" id="appointment-from" type="date" name="from" value="<c:out value='${filter.from}'/>"/></div>
  <div><label class="form-label" for="appointment-to">Đến ngày</label><input class="form-control" id="appointment-to" type="date" name="to" value="<c:out value='${filter.to}'/>"/></div>
  <div class="d-flex gap-2"><button class="btn btn-primary" type="submit">Lọc</button><a class="btn btn-outline-secondary" href="${ctx}/admin/appointments">Bỏ lọc</a></div>
</form>

<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table qa-admin-table">
  <thead><tr><th>Thời gian</th><th>Sinh viên</th><th>Tư vấn viên</th><th>Chủ đề</th><th>Chuyên mục</th><th>Hình thức</th><th>Địa điểm</th><th>Trạng thái</th></tr></thead>
  <tbody><c:choose><c:when test="${empty appointments.content}"><tr><td colspan="8"><qa:emptyState icon="bi-calendar-x" title="Chưa có lịch hẹn phù hợp" message="Thử thay đổi điều kiện lọc."/></td></tr></c:when><c:otherwise>
    <c:forEach var="item" items="${appointments.content}"><tr>
      <td class="text-nowrap"><time datetime="<c:out value='${item.startAt()}'/>" data-qaute-datetime><c:out value="${item.startAt()}"/></time><div class="small text-secondary"><time datetime="<c:out value='${item.endAt()}'/>" data-qaute-datetime><c:out value="${item.endAt()}"/></time></div></td>
      <td><div class="fw-semibold"><c:out value="${item.studentName()}"/></div><small class="text-secondary"><c:out value="${item.studentEmail()}"/></small></td>
      <td><div class="fw-semibold"><c:out value="${item.managerName()}"/></div><small class="text-secondary"><c:out value="${item.managerEmail()}"/></small></td>
      <td><c:out value="${item.topic()}"/></td>
      <td><c:out value="${item.categoryPath()}"/></td>
      <td><c:choose><c:when test="${item.modality() eq 'ONLINE'}">Trực tuyến</c:when><c:when test="${item.modality() eq 'IN_PERSON'}">Trực tiếp</c:when><c:otherwise>Linh hoạt</c:otherwise></c:choose></td>
      <td><c:out value="${item.location()}"/></td>
      <td><span class="badge ${item.status() eq 'CONFIRMED' ? 'bg-blue-lt' : item.status() eq 'COMPLETED' ? 'bg-green-lt' : item.status() eq 'CANCELLED' ? 'bg-red-lt' : 'bg-orange-lt'}"><c:choose><c:when test="${item.status() eq 'CONFIRMED'}">Đã xác nhận</c:when><c:when test="${item.status() eq 'COMPLETED'}">Hoàn thành</c:when><c:when test="${item.status() eq 'NO_SHOW'}">Sinh viên vắng</c:when><c:otherwise>Đã hủy</c:otherwise></c:choose></span></td>
    </tr></c:forEach>
  </c:otherwise></c:choose></tbody>
</table></div></div>
<qa:pagination page="${appointments}" baseUrl="${ctx}/admin/appointments" params="${paginationParams}"/>
</div>
