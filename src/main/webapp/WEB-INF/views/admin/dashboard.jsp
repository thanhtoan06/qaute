<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Tổng quan hệ thống</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="admin-dashboard" data-api="${ctx}/api/admin/dashboard" data-context="${ctx}">
  <div class="d-flex justify-content-between align-items-center mb-3"><div><h2 class="page-title">Tổng quan hệ thống</h2><p class="text-secondary mb-0">Tình hình vận hành và các cảnh báo cần xử lý.</p></div><a class="btn btn-primary" href="${ctx}/admin/reports">Báo cáo chi tiết</a></div>
  <div id="adm-dashboard-loading" class="alert alert-info">Đang tải dữ liệu…</div>
  <div id="adm-dashboard-error" class="alert alert-danger d-none" role="alert"></div>
  <div id="adm-dashboard-content" class="d-none">
    <div class="row row-cards mb-3">
      <div class="col-6 col-lg-2"><div class="card card-body"><span class="text-secondary">Ticket hôm nay</span><strong id="adm-d-today" class="h2 mb-0">—</strong></div></div>
      <div class="col-6 col-lg-2"><div class="card card-body"><span class="text-secondary">Ticket tháng này</span><strong id="adm-d-month" class="h2 mb-0">—</strong></div></div>
      <div class="col-6 col-lg-2"><div class="card card-body"><span class="text-secondary">Chưa nhận</span><strong id="adm-d-unassigned" class="h2 mb-0">—</strong></div></div>
      <div class="col-6 col-lg-2"><div class="card card-body"><span class="text-secondary">Tồn đọng &gt; 24 giờ</span><strong id="adm-d-backlog" class="h2 mb-0">—</strong></div></div>
      <div class="col-6 col-lg-2"><div class="card card-body"><span class="text-secondary">SLA đạt 30 ngày</span><strong id="adm-d-sla" class="h2 mb-0">—</strong></div></div>
      <div class="col-6 col-lg-2"><div class="card card-body"><span class="text-secondary">CSAT 30 ngày</span><strong id="adm-d-csat" class="h2 mb-0">—</strong></div></div>
      <div class="col-6 col-lg-3"><div class="card card-body"><span class="text-secondary">Chat hoạt động / chờ</span><strong id="adm-d-chat" class="h2 mb-0">—</strong></div></div>
      <div class="col-6 col-lg-3"><div class="card card-body"><span class="text-secondary">Lịch hẹn hôm nay</span><strong id="adm-d-appt" class="h2 mb-0">—</strong></div></div>
      <div class="col-6 col-lg-3"><div class="card card-body"><span class="text-secondary">Người dùng hoạt động 7 ngày</span><strong id="adm-d-users" class="h2 mb-0">—</strong></div></div>
      <div class="col-6 col-lg-3"><div class="card card-body"><span class="text-secondary">Email gửi thất bại</span><strong id="adm-d-email" class="h2 mb-0">—</strong></div></div>
    </div>
    <div class="row row-cards mb-3"><div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">Ticket 30 ngày</h3></div><div class="card-body" style="height:280px"><canvas id="adm-d-chart-daily"></canvas></div></div></div><div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">Phân bố phòng ban</h3></div><div class="card-body" style="height:280px"><canvas id="adm-d-chart-departments"></canvas></div></div></div></div>
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">FRT trung bình theo tuần</h3></div><div class="card-body" style="height:260px"><canvas id="adm-d-chart-frt"></canvas></div></div>
    <div class="row row-cards mb-3">
      <div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">Ticket vi phạm SLA</h3></div><div id="adm-alert-sla" class="list-group list-group-flush"></div></div></div>
      <div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">Chuyên mục chưa có Manager</h3></div><div id="adm-alert-categories" class="list-group list-group-flush"></div></div></div>
      <div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">FAQ hữu ích thấp</h3></div><div id="adm-alert-faq" class="list-group list-group-flush"></div></div></div>
      <div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">Truy vấn không có kết quả</h3></div><div id="adm-alert-search" class="list-group list-group-flush"></div></div></div>
      <div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">Đánh giá ≤ 2 sao gần đây</h3></div><div id="adm-alert-ratings" class="list-group list-group-flush"></div></div></div>
      <div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">Email gửi thất bại</h3></div><div class="card-body" id="adm-alert-email"></div></div></div>
      <div class="col-lg-6"><div class="card"><div class="card-header"><h3 class="card-title">Hoạt động gần đây</h3></div><div id="adm-alert-audit" class="list-group list-group-flush"></div></div></div>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js" defer></script>
<script src="${ctx}/assets/js/admin/dashboard.js" defer></script>
