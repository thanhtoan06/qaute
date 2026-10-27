<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Báo cáo toàn hệ thống</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/reports.css">
</head>
<div class="qa-page-admin-reports" data-qa-view="admin-reports">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- Khu vực báo cáo toàn hệ thống -->
<div id="admin-reports" data-api-url="${ctx}/api/admin/reports/data" data-export-url="${ctx}/admin/reports/export" class="">
  <h2 class="page-title mb-3 qa-view-title" data-qa-page="admin-reports"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-bar-chart-line"></i></span>Báo cáo toàn hệ thống</h2>
  <!-- Form lọc báo cáo theo thời gian, phòng ban, chuyên mục -->
  <form id="admin-report-filter" class="card card-body mb-3" method="get" action="${ctx}/admin/reports">
    <div class="row g-3 align-items-end">
      <div class="col-sm-6 col-xl-2"><label class="form-label" for="admin-report-range">Khoảng thời gian</label><select id="admin-report-range" class="form-select" name="range"><option value="7" ${range eq '7' ? 'selected' : ''}>7 ngày</option><option value="30" ${range eq '30' ? 'selected' : ''}>30 ngày</option><option value="90" ${range eq '90' ? 'selected' : ''}>90 ngày</option><option value="custom" ${range eq 'custom' ? 'selected' : ''}>Tùy chọn</option></select></div>
      <div class="col-sm-6 col-xl-2 admin-custom-date"><label class="form-label" for="admin-report-from">Từ ngày</label><input id="admin-report-from" class="form-control" type="date" name="from" value="<c:out value='${from}'/>"/></div>
      <div class="col-sm-6 col-xl-2 admin-custom-date"><label class="form-label" for="admin-report-to">Đến ngày</label><input id="admin-report-to" class="form-control" type="date" name="to" value="<c:out value='${to}'/>"/></div>
      <div class="col-sm-6 col-xl-2"><label class="form-label" for="admin-report-department">Phòng ban</label><select id="admin-report-department" name="departmentId" class="form-select"><option value="">Toàn trường</option><c:forEach var="department" items="${departments}"><option value="${department.id}" ${departmentId eq department.id ? 'selected' : ''}><c:out value="${department.name}"/></option></c:forEach></select></div>
      <div class="col-sm-6 col-xl-3"><label class="form-label" for="admin-report-category">Chuyên mục</label><select id="admin-report-category" name="categoryId" class="form-select"><option value="">Tất cả chuyên mục</option><c:forEach var="category" items="${categories}"><option value="${category.id()}" data-department="${category.departmentId()}" ${categoryId eq category.id() ? 'selected' : ''}><c:out value="${category.label()}"/></option></c:forEach></select></div>
      <div class="col-sm-6 col-xl-1"><button class="btn btn-primary w-100" type="submit">Xem</button></div>
    </div>
  </form>
  <!-- Trạng thái loading, error, empty -->
  <div id="admin-report-loading" class="alert alert-info" role="status">Đang tải báo cáo…</div>
  <div id="admin-report-error" class="alert alert-danger d-none" role="alert"></div>
  <div id="admin-report-empty" class="alert alert-secondary d-none">Không có ticket, chat hoặc lịch hẹn trong khoảng đã chọn.</div>
  <!-- Nội dung báo cáo với các tab -->
  <div id="admin-report-content" class="d-none">
    <ul class="nav nav-tabs mb-3" role="tablist">
      <li class="nav-item"><button class="nav-link active" data-bs-toggle="tab" data-bs-target="#report-overview" type="button">Tổng quan</button></li>
      <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#report-people" type="button">Phòng ban &amp; Manager</button></li>
      <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#report-knowledge" type="button">Tri thức</button></li>
      <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#report-services" type="button">Chat &amp; Lịch hẹn</button></li>
    </ul>
    <div class="tab-content">
      <!-- Tab Tổng quan -->
      <section class="tab-pane fade show active" id="report-overview">
        <div class="d-flex justify-content-end mb-3"><button class="btn btn-outline-primary js-report-export" data-type="tickets" type="button">Xuất CSV ticket</button></div>
        <!-- Grid KPI tổng quan -->
        <div class="row row-cards mb-3">
          <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">Ticket</div><div class="h2 mb-0" id="adm-kpi-tickets">—</div></div></div>
          <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">FRT trung vị</div><div class="h2 mb-0" id="adm-kpi-frt">—</div></div></div>
          <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">Giải quyết trung vị</div><div class="h2 mb-0" id="adm-kpi-resolution">—</div></div></div>
          <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">SLA đạt</div><div class="h2 mb-0" id="adm-kpi-sla">—</div></div></div>
          <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">CSAT</div><div class="h2 mb-0" id="adm-kpi-csat">—</div></div></div>
          <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">Tồn đọng</div><div class="h2 mb-0" id="adm-kpi-backlog">—</div></div></div>
        </div>
        <!-- Biểu đồ xu hướng và tải theo giờ -->
        <div class="row row-cards mb-3"><div class="col-xl-7"><div class="card"><div class="card-header"><h3 class="card-title">Xu hướng theo tuần</h3></div><div class="card-body"><div class="u-height-300px"><canvas id="admin-chart-weekly"></canvas></div></div></div></div><div class="col-xl-5"><div class="card"><div class="card-header"><h3 class="card-title">Tải theo giờ trong ngày</h3></div><div class="card-body"><div class="u-height-300px"><canvas id="admin-chart-hourly"></canvas></div></div></div></div></div>
      </section>
      <!-- Tab Phòng ban & Manager -->
      <section class="tab-pane fade" id="report-people">
        <div class="d-flex justify-content-end gap-2 mb-3"><button class="btn btn-outline-primary js-report-export" data-type="departments" type="button">Xuất CSV phòng ban</button><button class="btn btn-outline-primary js-report-export" data-type="managers" type="button">Xuất CSV Manager</button></div>
        <div class="card mb-3"><div class="card-header"><h3 class="card-title">So sánh phòng ban</h3></div><div class="card-body"><div class="u-height-320px"><canvas id="admin-chart-departments"></canvas></div></div></div>
        <div class="card mb-3"><div class="card-header"><h3 class="card-title">Hiệu suất Manager</h3></div><div class="table-responsive"><table class="table table-vcenter card-table"><thead><tr><th>Manager</th><th>Đã giải quyết</th><th>FRT trung bình</th><th>CSAT</th><th>Chat</th></tr></thead><tbody id="admin-manager-table"></tbody></table></div></div>
      </section>
      <!-- Tab Tri thức -->
      <section class="tab-pane fade" id="report-knowledge">
        <div class="d-flex justify-content-end gap-2 mb-3"><button class="btn btn-outline-primary js-report-export" data-type="knowledge" type="button">Xuất CSV tri thức</button><button class="btn btn-outline-primary js-report-export" data-type="noresult" type="button">Xuất CSV không kết quả</button></div>
        <!-- KPI tri thức -->
        <div class="row row-cards mb-3"><div class="col-md-3"><div class="card card-body"><div class="text-secondary">Lượt xem FAQ có ghi nhận</div><div class="h2 mb-0" id="adm-k-views">—</div></div></div><div class="col-md-3"><div class="card card-body"><div class="text-secondary">Hữu ích</div><div class="h2 mb-0" id="adm-k-helpful">—</div></div></div><div class="col-md-3"><div class="card card-body"><div class="text-secondary">Tìm kiếm không kết quả</div><div class="h2 mb-0" id="adm-k-noresult">—</div></div></div><div class="col-md-3"><div class="card card-body"><div class="text-secondary">Proxy tránh tạo ticket</div><div class="h2 mb-0" id="adm-k-deflection">—</div></div></div></div>
        <!-- Bảng FAQ hữu ích thấp và truy vấn không kết quả -->
        <div class="row row-cards mb-3"><div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">FAQ hữu ích thấp</h3></div><div class="table-responsive"><table class="table card-table"><thead><tr><th>FAQ</th><th>Phiếu</th></tr></thead><tbody id="admin-low-faq-table"></tbody></table></div></div></div><div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">Truy vấn không có kết quả</h3></div><div class="table-responsive"><table class="table card-table"><thead><tr><th>Truy vấn</th><th>Lượt</th></tr></thead><tbody id="admin-noresult-table"></tbody></table></div></div></div></div>
      </section>
      <!-- Tab Chat & Lịch hẹn -->
      <section class="tab-pane fade" id="report-services">
        <div class="d-flex justify-content-end mb-3"><button class="btn btn-outline-primary js-report-export" data-type="tickets" type="button">Xuất CSV tổng quan</button></div>
        <div class="row row-cards mb-3"><div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">Chat</h3></div><div class="card-body" id="admin-chat-stats"></div></div></div><div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">Lịch hẹn</h3></div><div class="card-body" id="admin-appointment-stats"></div></div></div></div>
      </section>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js" defer></script>
<script src="${ctx}/assets/js/admin/reports.js" defer></script>
</div>
