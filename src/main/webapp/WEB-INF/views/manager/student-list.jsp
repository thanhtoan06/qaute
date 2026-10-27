<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Sinh viên liên quan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/manager/student-list.css">
</head>
<div class="qa-page-manager-student-list" data-qa-view="manager-student-list">

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
