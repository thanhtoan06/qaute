<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Tổng quan tư vấn viên</title>
<div id="manager-dashboard" data-api="${ctx}/api/manager/dashboard" data-context="${ctx}">
  <section class="qa-dashboard-hero" aria-labelledby="manager-welcome">
    <div class="qa-dashboard-hero-content"><span class="qa-dashboard-eyebrow"><i class="bi bi-person-workspace" aria-hidden="true"></i>Không gian tư vấn viên</span>
      <h1 id="manager-welcome">Xin chào, <c:out value="${me.fullName}"/>!</h1>
      <p>Cùng kết nối và hỗ trợ sinh viên. Theo dõi công việc, lịch tư vấn và những yêu cầu cần bạn ưu tiên hôm nay.</p>
      <div class="d-flex flex-wrap gap-2"><a class="btn btn-white" href="${ctx}/manager/queue"><i class="bi bi-inbox me-2"></i>Mở hàng đợi</a><a class="btn btn-outline-light" href="${ctx}/manager/chat"><i class="bi bi-chat-dots me-2"></i>Tư vấn trực tuyến</a></div>
    </div><div class="qa-dashboard-hero-aside" aria-hidden="true"><i class="bi bi-chat-square-heart"></i><span class="qa-dashboard-date" data-workspace-date>Hôm nay</span></div>
  </section>
<div id="mgr-dashboard-loading" class="qa-dashboard-loading" role="status"><span class="visually-hidden">Đang tải dữ liệu tổng quan…</span><div class="row g-3"><div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div><div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div><div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div><div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div></div></div>
<div id="mgr-dashboard-error" class="alert alert-danger d-none" role="alert"></div>
  <div id="mgr-dashboard-content" class="d-none">
    <div class="qa-dashboard-section-label"><h2>Công việc & chất lượng tư vấn</h2><span>Trong phạm vi phụ trách của bạn</span></div>
    <div class="qa-kpi-grid"><a class="qa-kpi" href="${ctx}/manager/queue?tab=unassigned">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Chưa nhận trong phạm vi</span><span class="qa-kpi-icon qa-kpi-icon--orange"><i class="bi bi-inbox" aria-hidden="true"></i></span></div>
  <strong id="mgr-k-unassigned" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Sẵn sàng để tiếp nhận</span>
</a>
<a class="qa-kpi" href="${ctx}/manager/queue?tab=mine">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Yêu cầu đang phụ trách</span><span class="qa-kpi-icon"><i class="bi bi-person-workspace" aria-hidden="true"></i></span></div>
  <strong id="mgr-k-mine" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Công việc hiện tại của bạn</span>
</a>
<a class="qa-kpi" href="${ctx}/manager/queue?tab=waiting">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Chờ sinh viên phản hồi</span><span class="qa-kpi-icon qa-kpi-icon--purple"><i class="bi bi-hourglass-split" aria-hidden="true"></i></span></div>
  <strong id="mgr-k-waiting" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Theo dõi thông tin bổ sung</span>
</a>
<a class="qa-kpi" href="${ctx}/manager/queue?tab=slarisk">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">SLA rủi ro / quá hạn</span><span class="qa-kpi-icon qa-kpi-icon--red"><i class="bi bi-exclamation-triangle" aria-hidden="true"></i></span></div>
  <strong id="mgr-k-sla" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Ưu tiên kiểm tra và xử lý</span>
</a>
<a class="qa-kpi" href="${ctx}/manager/appointments?tab=today">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Lịch hẹn hôm nay</span><span class="qa-kpi-icon"><i class="bi bi-calendar-check" aria-hidden="true"></i></span></div>
  <strong id="mgr-k-appointments" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Lịch tư vấn trong ngày</span>
</a>
<a class="qa-kpi" href="${ctx}/manager/chat">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Hội thoại đang chờ</span><span class="qa-kpi-icon qa-kpi-icon--green"><i class="bi bi-chat-dots" aria-hidden="true"></i></span></div>
  <strong id="mgr-k-chats" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Kết nối và hỗ trợ sinh viên</span>
</a>
<a class="qa-kpi" href="${ctx}/manager/reports">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Mức độ hài lòng</span><span class="qa-kpi-icon qa-kpi-icon--orange"><i class="bi bi-star" aria-hidden="true"></i></span></div>
  <strong id="mgr-k-csat" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Điểm đánh giá trong 30 ngày</span>
</a>
<a class="qa-kpi" href="${ctx}/manager/reports">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Phản hồi đầu trung vị</span><span class="qa-kpi-icon qa-kpi-icon--purple"><i class="bi bi-stopwatch" aria-hidden="true"></i></span></div>
  <strong id="mgr-k-frt" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Thời gian làm việc · 30 ngày</span>
</a></div>
    <div class="row g-3 mb-4"><div class="col-lg-7"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-graph-up" aria-hidden="true"></i></span><h2 class="card-title mb-0">Yêu cầu theo ngày</h2><span class="qa-card-period">14 ngày gần nhất</span></div><div class="card-body qa-dashboard-chart"><canvas id="mgr-chart-daily" role="img" aria-label="Yêu cầu theo ngày"></canvas></div></div></div><div class="col-lg-5"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-bar-chart" aria-hidden="true"></i></span><h2 class="card-title mb-0">Phân bố theo chuyên mục</h2><span class="qa-card-period">14 ngày gần nhất</span></div><div class="card-body qa-dashboard-chart"><canvas id="mgr-chart-category" role="img" aria-label="Phân bố theo chuyên mục"></canvas></div></div></div></div>
    <div class="row g-3"><div class="col-xl-8"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-lightning-charge"></i></span><h2 class="card-title mb-0">Cần làm ngay</h2><a class="qa-card-period" href="${ctx}/manager/queue?tab=slarisk">Xem hàng đợi <i class="bi bi-arrow-right"></i></a></div><div id="mgr-urgent-list" class="list-group list-group-flush"></div></div></div>
    <div class="col-xl-4"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-grid"></i></span><h2 class="card-title mb-0">Truy cập nhanh</h2></div><div class="list-group list-group-flush">
      <a class="list-group-item list-group-item-action d-flex align-items-center gap-3" href="${ctx}/manager/schedule"><span class="qa-kpi-icon"><i class="bi bi-calendar-week"></i></span><div><strong>Lịch làm việc</strong><div class="small text-secondary mt-1">Quản lý thời gian tư vấn của bạn</div></div></a>
      <a class="list-group-item list-group-item-action d-flex align-items-center gap-3" href="${ctx}/manager/canned-responses"><span class="qa-kpi-icon qa-kpi-icon--purple"><i class="bi bi-chat-left-text"></i></span><div><strong>Mẫu trả lời</strong><div class="small text-secondary mt-1">Hỗ trợ sinh viên nhanh và nhất quán</div></div></a>
      <a class="list-group-item list-group-item-action d-flex align-items-center gap-3" href="${ctx}/manager/faq"><span class="qa-kpi-icon qa-kpi-icon--green"><i class="bi bi-journal-text"></i></span><div><strong>FAQ đề xuất</strong><div class="small text-secondary mt-1">Chia sẻ kiến thức hữu ích cho sinh viên</div></div></a>
    </div></div></div></div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js" defer></script>
<script src="${ctx}/assets/js/manager/dashboard.js" defer></script>
