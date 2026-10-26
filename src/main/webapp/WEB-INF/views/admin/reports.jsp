<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Báo cáo toàn hệ thống</title>
<div class="qa-page-admin-reports" data-qa-view="admin-reports">
<style>
.qa-page-admin-reports { --page-accent: #3579c7; --page-accent-dark: #285b94; --page-tint: #edf5ff; --page-line: #d6e6fa; min-width: 0; color: #344c67; }
.qa-page-admin-reports > .page-header, .qa-page-admin-reports > .qa-page-heading, .qa-page-admin-reports > .d-flex:has(> .page-title), .qa-page-admin-reports > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #edf5ff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-reports > .page-header::after, .qa-page-admin-reports > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #d6e6fa; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-reports .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-reports .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #d6e6fa; border-radius: 12px; color: #285b94; background: linear-gradient(145deg,#fff,#edf5ff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-reports .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-reports .card:hover { border-color: #d6e6fa; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-reports .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#edf5ff); }
.qa-page-admin-reports .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-reports .card-body { color: #435b75; }
.qa-page-admin-reports .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#edf5ff); }
.qa-page-admin-reports .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-reports .form-control, .qa-page-admin-reports .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-reports textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-reports .form-control:focus, .qa-page-admin-reports .form-select:focus { border-color: #3579c7; box-shadow: 0 0 0 4px #edf5ff; }
.qa-page-admin-reports .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-reports .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-reports .btn-primary { border-color: #3579c7; background: linear-gradient(120deg,#3579c7,#285b94); box-shadow: 0 5px 13px #d6e6fa; }
.qa-page-admin-reports .btn-outline-primary { border-color: #d6e6fa; color: #285b94; background: #fff; }
.qa-page-admin-reports .btn-outline-primary:hover { border-color: #3579c7; color: #fff; background: linear-gradient(120deg,#3579c7,#285b94); }
.qa-page-admin-reports .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#edf5ff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-reports .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-reports .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-reports .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#edf5ff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-reports .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-reports .table tbody tr:hover { background: #edf5ff; }
.qa-page-admin-reports .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-reports .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-reports .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-reports .nav-tabs .nav-link.active { color: #285b94; box-shadow: inset 0 -2px #3579c7; }
.qa-page-admin-reports .nav-pills .nav-link.active { color: #285b94; background: #edf5ff; box-shadow: inset 0 0 0 1px #d6e6fa; }
.qa-page-admin-reports .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-reports .progress-bar { border-radius: inherit; }
.qa-page-admin-reports .card-header { background-image: linear-gradient(90deg,var(--page-accent),transparent 72%); background-repeat: no-repeat; background-size: 100% 2px; background-position: top; }
@media (max-width: 767.98px) {
  .qa-page-admin-reports > .page-header, .qa-page-admin-reports > .qa-page-heading, .qa-page-admin-reports > .d-flex:has(> .page-title), .qa-page-admin-reports > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-reports .card { border-radius: 15px; }
  .qa-page-admin-reports .card-body { padding: 1rem; }
  .qa-page-admin-reports .table-responsive { border-radius: 11px; }
  .qa-page-admin-reports .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-reports *, .qa-page-admin-reports *::before, .qa-page-admin-reports *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
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
        <div class="row row-cards mb-3"><div class="col-xl-7"><div class="card"><div class="card-header"><h3 class="card-title">Xu hướng theo tuần</h3></div><div class="card-body"><div style="height:300px"><canvas id="admin-chart-weekly"></canvas></div></div></div></div><div class="col-xl-5"><div class="card"><div class="card-header"><h3 class="card-title">Tải theo giờ trong ngày</h3></div><div class="card-body"><div style="height:300px"><canvas id="admin-chart-hourly"></canvas></div></div></div></div></div>
      </section>
      <!-- Tab Phòng ban & Manager -->
      <section class="tab-pane fade" id="report-people">
        <div class="d-flex justify-content-end gap-2 mb-3"><button class="btn btn-outline-primary js-report-export" data-type="departments" type="button">Xuất CSV phòng ban</button><button class="btn btn-outline-primary js-report-export" data-type="managers" type="button">Xuất CSV Manager</button></div>
        <div class="card mb-3"><div class="card-header"><h3 class="card-title">So sánh phòng ban</h3></div><div class="card-body"><div style="height:320px"><canvas id="admin-chart-departments"></canvas></div></div></div>
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
