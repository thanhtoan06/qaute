<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Tổng quan Manager</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="manager-dashboard" data-api="${ctx}/api/manager/dashboard" data-context="${ctx}">
  <div class="d-flex justify-content-between align-items-center mb-3"><div><h2 class="page-title">Tổng quan</h2><p class="text-secondary mb-0">Xin chào, <c:out value="${me.fullName}"/>. Công việc và chất lượng tư vấn của bạn.</p></div><a class="btn btn-primary" href="${ctx}/manager/queue">Mở hàng đợi</a></div>
  <div id="mgr-dashboard-loading" class="alert alert-info">Đang tải dữ liệu…</div>
  <div id="mgr-dashboard-error" class="alert alert-danger d-none" role="alert"></div>
  <div id="mgr-dashboard-content" class="d-none">
    <div class="row row-cards mb-3">
      <div class="col-6 col-lg-3"><a href="${ctx}/manager/queue?tab=unassigned" class="card card-body text-reset text-decoration-none"><span class="text-secondary">Chưa nhận trong phạm vi</span><strong id="mgr-k-unassigned" class="h2 mb-0">—</strong></a></div>
      <div class="col-6 col-lg-3"><a href="${ctx}/manager/queue?tab=mine" class="card card-body text-reset text-decoration-none"><span class="text-secondary">Việc của tôi đang mở</span><strong id="mgr-k-mine" class="h2 mb-0">—</strong></a></div>
      <div class="col-6 col-lg-3"><a href="${ctx}/manager/queue?tab=waiting" class="card card-body text-reset text-decoration-none"><span class="text-secondary">Chờ SV phản hồi</span><strong id="mgr-k-waiting" class="h2 mb-0">—</strong></a></div>
      <div class="col-6 col-lg-3"><a href="${ctx}/manager/queue?tab=slarisk" class="card card-body text-reset text-decoration-none"><span class="text-secondary">SLA rủi ro / quá hạn</span><strong id="mgr-k-sla" class="h2 mb-0">—</strong></a></div>
      <div class="col-6 col-lg-3"><a href="${ctx}/manager/appointments?tab=today" class="card card-body text-reset text-decoration-none"><span class="text-secondary">Lịch hẹn hôm nay</span><strong id="mgr-k-appointments" class="h2 mb-0">—</strong></a></div>
      <div class="col-6 col-lg-3"><a href="${ctx}/manager/chat" class="card card-body text-reset text-decoration-none"><span class="text-secondary">Phiên chat đang chờ</span><strong id="mgr-k-chats" class="h2 mb-0">—</strong></a></div>
      <div class="col-6 col-lg-3"><div class="card card-body"><span class="text-secondary">CSAT 30 ngày</span><strong id="mgr-k-csat" class="h2 mb-0">—</strong></div></div>
      <div class="col-6 col-lg-3"><div class="card card-body"><span class="text-secondary">FRT trung vị 30 ngày</span><strong id="mgr-k-frt" class="h2 mb-0">—</strong></div></div>
    </div>
    <div class="row row-cards mb-3"><div class="col-lg-7"><div class="card"><div class="card-header"><h3 class="card-title">Yêu cầu 14 ngày</h3></div><div class="card-body" style="height:300px"><canvas id="mgr-chart-daily"></canvas></div></div></div><div class="col-lg-5"><div class="card"><div class="card-header"><h3 class="card-title">Theo chuyên mục</h3></div><div class="card-body" style="height:300px"><canvas id="mgr-chart-category"></canvas></div></div></div></div>
    <div class="card"><div class="card-header"><h3 class="card-title">Cần làm ngay</h3></div><div id="mgr-urgent-list" class="list-group list-group-flush"></div></div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js" defer></script>
<script src="${ctx}/assets/js/manager/dashboard.js" defer></script>
