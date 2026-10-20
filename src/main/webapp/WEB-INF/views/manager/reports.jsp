<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Báo cáo trong phạm vi</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="manager-reports" data-api-url="${ctx}/api/manager/reports/data">
  <div class="d-flex align-items-center justify-content-between mb-3"><h2 class="page-title mb-0">Báo cáo trong phạm vi</h2></div>
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
