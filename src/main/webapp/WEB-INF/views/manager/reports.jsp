<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Báo cáo trong phạm vi</title>
<div class="qa-page-manager-reports" data-qa-view="manager-reports">
<style>
.qa-page-manager-reports { --page-accent: #7456bd; --page-accent-dark: #584091; --page-tint: #f3efff; --page-line: #e1d7fb; min-width: 0; color: #344c67; }
.qa-page-manager-reports > .page-header, .qa-page-manager-reports > .qa-page-heading, .qa-page-manager-reports > .d-flex:has(> .page-title), .qa-page-manager-reports > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #f3efff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-manager-reports > .page-header::after, .qa-page-manager-reports > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #e1d7fb; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-manager-reports .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-manager-reports .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #e1d7fb; border-radius: 12px; color: #584091; background: linear-gradient(145deg,#fff,#f3efff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-manager-reports .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-manager-reports .card:hover { border-color: #e1d7fb; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-manager-reports .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#f3efff); }
.qa-page-manager-reports .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-manager-reports .card-body { color: #435b75; }
.qa-page-manager-reports .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#f3efff); }
.qa-page-manager-reports .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-manager-reports .form-control, .qa-page-manager-reports .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-manager-reports textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-manager-reports .form-control:focus, .qa-page-manager-reports .form-select:focus { border-color: #7456bd; box-shadow: 0 0 0 4px #f3efff; }
.qa-page-manager-reports .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-manager-reports .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-manager-reports .btn-primary { border-color: #7456bd; background: linear-gradient(120deg,#7456bd,#584091); box-shadow: 0 5px 13px #e1d7fb; }
.qa-page-manager-reports .btn-outline-primary { border-color: #e1d7fb; color: #584091; background: #fff; }
.qa-page-manager-reports .btn-outline-primary:hover { border-color: #7456bd; color: #fff; background: linear-gradient(120deg,#7456bd,#584091); }
.qa-page-manager-reports .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#f3efff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-manager-reports .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-manager-reports .table { margin-bottom: 0; color: #405872; }
.qa-page-manager-reports .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#f3efff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-manager-reports .table tbody tr { transition: background-color .18s ease; }
.qa-page-manager-reports .table tbody tr:hover { background: #f3efff; }
.qa-page-manager-reports .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-manager-reports .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-manager-reports .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-manager-reports .nav-tabs .nav-link.active { color: #584091; box-shadow: inset 0 -2px #7456bd; }
.qa-page-manager-reports .nav-pills .nav-link.active { color: #584091; background: #f3efff; box-shadow: inset 0 0 0 1px #e1d7fb; }
.qa-page-manager-reports .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-manager-reports .progress-bar { border-radius: inherit; }
.qa-page-manager-reports .card-header { background-image: linear-gradient(90deg,var(--page-accent),transparent 72%); background-repeat: no-repeat; background-size: 100% 2px; background-position: top; }
@media (max-width: 767.98px) {
  .qa-page-manager-reports > .page-header, .qa-page-manager-reports > .qa-page-heading, .qa-page-manager-reports > .d-flex:has(> .page-title), .qa-page-manager-reports > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-manager-reports .card { border-radius: 15px; }
  .qa-page-manager-reports .card-body { padding: 1rem; }
  .qa-page-manager-reports .table-responsive { border-radius: 11px; }
  .qa-page-manager-reports .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-reports *, .qa-page-manager-reports *::before, .qa-page-manager-reports *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="manager-reports" data-api-url="${ctx}/api/manager/reports/data" class="">
  <div class="d-flex align-items-center justify-content-between mb-3"><h2 class="page-title mb-0 qa-view-title" data-qa-page="manager-reports"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-bar-chart-line"></i></span>Báo cáo trong phạm vi</h2></div>
  <%-- Lọc báo cáo theo kỳ, chuyên mục và phạm vi phụ trách. --%>
  <form id="report-filter" class="card card-body mb-3" action="${ctx}/manager/reports" method="get">
    <div class="row g-3 align-items-end">
      <div class="col-sm-6 col-lg-2"><label class="form-label" for="report-range">Khoảng thời gian</label><select class="form-select" id="report-range" name="range">
        <option value="7" ${range eq '7' ? 'selected' : ''}>7 ngày</option><option value="30" ${range eq '30' ? 'selected' : ''}>30 ngày</option><option value="90" ${range eq '90' ? 'selected' : ''}>90 ngày</option><option value="custom" ${range eq 'custom' ? 'selected' : ''}>Tùy chọn</option>
      </select></div>
      <div class="col-sm-6 col-lg-2 report-custom-date"><label class="form-label" for="report-from">Từ ngày</label><input class="form-control" id="report-from" name="from" type="date" value="<c:out value='${from}'/>"/></div>
      <div class="col-sm-6 col-lg-2 report-custom-date"><label class="form-label" for="report-to">Đến ngày</label><input class="form-control" id="report-to" name="to" type="date" value="<c:out value='${to}'/>"/></div>
      <div class="col-sm-6 col-lg-3"><label class="form-label" for="report-category">Chuyên mục</label><select class="form-select" id="report-category" name="categoryId"><option value="">Tất cả trong phạm vi</option><c:forEach var="item" items="${categories}"><option value="${item.key}" ${categoryId eq item.key ? 'selected' : ''}><c:out value="${item.value}"/></option></c:forEach></select></div>
      <div class="col-sm-6 col-lg-2"><label class="form-check mb-2"><input class="form-check-input" id="report-mine" name="mine" value="true" type="checkbox" ${mine ? 'checked' : ''}/><span class="form-check-label">Chỉ của tôi</span></label></div>
      <div class="col-sm-6 col-lg-1"><button class="btn btn-primary w-100" type="submit">Xem</button></div>
    </div>
    <div class="small text-secondary mt-2">Ticket được giao cho bạn luôn nằm trong báo cáo phạm vi. Chế độ “Chỉ của tôi” chỉ tính ticket được giao và lịch/chat của bạn; bộ lọc chuyên mục tạm ẩn trong chế độ này.</div>
  </form>
  <div id="report-loading" class="alert alert-info" role="status">Đang tải báo cáo…</div>
  <div id="report-error" class="alert alert-danger d-none" role="alert"></div>
  <div id="report-empty" class="alert alert-secondary d-none">Không có dữ liệu trong khoảng đã chọn.</div>
  <%-- Hiển thị chỉ số, biểu đồ và bảng tải công việc khi dữ liệu sẵn sàng. --%>
  <div id="report-content" class="d-none">
    <div class="row row-cards mb-3">
      <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">Yêu cầu</div><div class="h2 mb-0" id="kpi-tickets">—</div></div></div>
      <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">FRT trung vị</div><div class="h2 mb-0" id="kpi-frt">—</div></div></div>
      <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">Giải quyết trung vị</div><div class="h2 mb-0" id="kpi-resolution">—</div></div></div>
      <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">SLA đạt</div><div class="h2 mb-0" id="kpi-sla">—</div></div></div>
      <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">CSAT</div><div class="h2 mb-0" id="kpi-csat">—</div></div></div>
      <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">Tồn đọng</div><div class="h2 mb-0" id="kpi-backlog">—</div></div></div>
      <div class="col-6 col-lg"><div class="card card-body"><div class="text-secondary">Mở lại</div><div class="h2 mb-0" id="kpi-reopen">—</div></div></div>
    </div>
    <div class="row row-cards mb-3">
      <div class="col-xl-8"><div class="card"><div class="card-header"><h3 class="card-title">Yêu cầu theo ngày</h3></div><div class="card-body"><div style="height:280px"><canvas id="chart-daily" aria-label="Biểu đồ yêu cầu theo ngày"></canvas></div></div></div></div>
      <div class="col-xl-4"><div class="card"><div class="card-header"><h3 class="card-title">Theo trạng thái</h3></div><div class="card-body"><div style="height:280px"><canvas id="chart-status" aria-label="Biểu đồ trạng thái"></canvas></div></div></div></div>
    </div>
    <div class="row row-cards mb-3">
      <div class="col-xl-8"><div class="card"><div class="card-header"><h3 class="card-title">Theo chuyên mục</h3></div><div class="card-body"><div style="height:300px"><canvas id="chart-category" aria-label="Biểu đồ chuyên mục"></canvas></div></div></div></div>
      <div class="col-xl-4"><div class="card"><div class="card-header"><h3 class="card-title">Phân phối CSAT</h3></div><div class="card-body"><div style="height:300px"><canvas id="chart-csat" aria-label="Biểu đồ đánh giá một đến năm sao"></canvas></div></div></div></div>
    </div>
    <div class="row row-cards mb-3">
      <div class="col-xl-8"><div class="card"><div class="card-header"><h3 class="card-title">Tải công việc</h3></div><div class="table-responsive"><table class="table table-vcenter card-table"><thead><tr><th>Tư vấn viên</th><th>Đang mở</th><th>Giới hạn</th><th>Giải quyết trong kỳ</th></tr></thead><tbody id="report-workload"></tbody></table></div></div></div>
      <div class="col-xl-4"><div class="card"><div class="card-header"><h3 class="card-title">Chat và lịch hẹn</h3></div><div class="card-body" id="report-other"></div></div></div>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js" defer></script>
<script src="${ctx}/assets/js/manager/reports.js" defer></script>
</div>
