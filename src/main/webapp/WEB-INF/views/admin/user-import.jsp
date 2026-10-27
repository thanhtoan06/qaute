<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Nhập sinh viên từ CSV</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/user-import.css">
</head>
<div class="qa-page-admin-user-import" data-qa-view="admin-user-import">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và nút quay lại -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-user-import"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-person-vcard"></i></span>Nhập sinh viên từ CSV</h2>
  <a class="btn btn-outline-secondary" href="${ctx}/admin/users">Danh sách người dùng</a>
</div>

<!-- Form tải lên tệp CSV -->
<div class="card mb-3">
  <div class="card-header"><h3 class="card-title">Tệp CSV</h3></div>
  <div class="card-body">
    <p class="text-secondary">Dùng tệp UTF-8 tối đa 2 MB và 2.000 dòng dữ liệu. Header phải đúng thứ tự: <code>mssv,fullName,email,facultyCode,classCode,cohort</code>. Mã khoa có thể để trống.</p>
    <a class="btn btn-outline-primary mb-3" href="${ctx}/admin/users/import/template">Tải tệp mẫu</a>
    <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
    <form method="post" action="${ctx}/admin/users/import" enctype="multipart/form-data">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <label class="form-label required" for="import-file">Chọn tệp CSV</label>
      <input class="form-control mb-3" type="file" id="import-file" name="file" accept=".csv,text/csv" required/>
      <div class="d-flex flex-wrap gap-2">
        <button class="btn btn-outline-primary" type="submit" name="dryRun" value="true">Kiểm tra trước</button>
        <button class="btn btn-primary" type="submit" name="dryRun" value="false">Nhập thật</button>
      </div>
    </form>
  </div>
</div>

<!-- Bảng kết quả nhập -->
<c:if test="${not empty report}">
  <div class="card">
    <div class="card-header d-flex flex-wrap justify-content-between align-items-center gap-2">
      <div>
        <h3 class="card-title mb-1"><c:choose><c:when test="${dryRun}">Kết quả kiểm tra</c:when><c:otherwise>Kết quả nhập</c:otherwise></c:choose></h3>
        <span class="text-secondary">Tổng <c:out value="${report.total()}"/> · OK <c:out value="${report.ok()}"/> · Lỗi <c:out value="${report.failed()}"/></span>
      </div>
      <!-- Nút tải CSV lỗi (nếu có lỗi) -->
      <c:if test="${report.failed() > 0}">
        <form method="post" action="${ctx}/admin/users/import/errors">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <textarea name="csv" hidden><c:out value="${errorCsv}"/></textarea>
          <button class="btn btn-outline-danger" type="submit">Tải CSV lỗi</button>
        </form>
      </c:if>
    </div>
    <div class="table-responsive">
      <table class="table card-table table-vcenter">
        <thead><tr><th>Dòng</th><th>MSSV</th><th>Email</th><th>Kết quả</th><th>Chi tiết</th></tr></thead>
        <tbody>
          <c:forEach var="row" items="${report.rows()}">
            <tr>
              <td><c:out value="${row.line()}"/></td>
              <td><c:out value="${row.mssv()}"/></td>
              <td><c:out value="${row.email()}"/></td>
              <td><c:choose><c:when test="${row.ok()}"><span class="badge bg-green-lt">OK</span></c:when><c:otherwise><span class="badge bg-red-lt">Lỗi</span></c:otherwise></c:choose></td>
              <td><c:out value="${row.message()}"/></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</c:if>
</div>
