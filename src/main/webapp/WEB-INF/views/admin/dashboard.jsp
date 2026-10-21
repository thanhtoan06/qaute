<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Tổng quan hệ thống</title>
<div id="admin-dashboard" data-api="${ctx}/api/admin/dashboard" data-context="${ctx}">
  <section class="qa-dashboard-hero" aria-labelledby="admin-welcome"><div class="qa-dashboard-hero-content"><span class="qa-dashboard-eyebrow"><i class="bi bi-shield-check" aria-hidden="true"></i>Không gian quản trị</span><h1 id="admin-welcome">Tổng quan hệ thống QAUTE</h1><p>Theo dõi hoạt động hỗ trợ sinh viên, chất lượng dịch vụ và các vấn đề cần được quan tâm trên toàn hệ thống.</p><div class="d-flex flex-wrap gap-2"><a class="btn btn-white" href="${ctx}/admin/reports"><i class="bi bi-bar-chart-line me-2"></i>Báo cáo chi tiết</a><a class="btn btn-outline-light" href="${ctx}/admin/users/new"><i class="bi bi-person-plus me-2"></i>Thêm người dùng</a></div></div><div class="qa-dashboard-hero-aside" aria-hidden="true"><i class="bi bi-buildings"></i><span class="qa-dashboard-date" data-workspace-date>Hôm nay</span></div></section>
<div id="adm-dashboard-loading" class="qa-dashboard-loading" role="status"><span class="visually-hidden">Đang tải dữ liệu tổng quan…</span><div class="row g-3"><div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div><div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div><div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div><div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div></div></div>
<div id="adm-dashboard-error" class="alert alert-danger d-none" role="alert"></div>
  <div id="adm-dashboard-content" class="d-none"><div class="qa-dashboard-section-label"><h2>Hoạt động & chất lượng dịch vụ</h2><span>Dữ liệu toàn hệ thống</span></div><div class="qa-kpi-grid qa-kpi-grid--admin"><a class="qa-kpi" href="${ctx}/admin/tickets">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Yêu cầu hôm nay</span><span class="qa-kpi-icon"><i class="bi bi-ticket-detailed" aria-hidden="true"></i></span></div>
  <strong id="adm-d-today" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Tiếp nhận trong ngày</span>
</a>
<a class="qa-kpi" href="${ctx}/admin/reports">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Yêu cầu tháng này</span><span class="qa-kpi-icon qa-kpi-icon--purple"><i class="bi bi-calendar3" aria-hidden="true"></i></span></div>
  <strong id="adm-d-month" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Khối lượng tư vấn trong tháng</span>
</a>
<a class="qa-kpi" href="${ctx}/admin/tickets?status=NEW">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Yêu cầu chưa nhận</span><span class="qa-kpi-icon qa-kpi-icon--orange"><i class="bi bi-inbox" aria-hidden="true"></i></span></div>
  <strong id="adm-d-unassigned" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Đang chờ được phân công</span>
</a>
<a class="qa-kpi" href="${ctx}/admin/tickets">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Tồn đọng trên 24 giờ</span><span class="qa-kpi-icon qa-kpi-icon--red"><i class="bi bi-hourglass-split" aria-hidden="true"></i></span></div>
  <strong id="adm-d-backlog" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Các yêu cầu cần được chú ý</span>
</a>
<a class="qa-kpi" href="${ctx}/admin/reports">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">SLA đạt trong 30 ngày</span><span class="qa-kpi-icon qa-kpi-icon--green"><i class="bi bi-shield-check" aria-hidden="true"></i></span></div>
  <strong id="adm-d-sla" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Chất lượng theo thời hạn xử lý</span>
</a>
<a class="qa-kpi" href="${ctx}/admin/reports">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Mức độ hài lòng</span><span class="qa-kpi-icon qa-kpi-icon--orange"><i class="bi bi-star" aria-hidden="true"></i></span></div>
  <strong id="adm-d-csat" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Điểm đánh giá trong 30 ngày</span>
</a>
<a class="qa-kpi" href="${ctx}/admin/reports">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Chat hoạt động / chờ</span><span class="qa-kpi-icon"><i class="bi bi-chat-dots" aria-hidden="true"></i></span></div>
  <strong id="adm-d-chat" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Tình hình tư vấn trực tuyến</span>
</a>
<a class="qa-kpi" href="${ctx}/admin/reports">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Lịch hẹn hôm nay</span><span class="qa-kpi-icon qa-kpi-icon--purple"><i class="bi bi-calendar-check" aria-hidden="true"></i></span></div>
  <strong id="adm-d-appt" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Các buổi tư vấn trong ngày</span>
</a>
<a class="qa-kpi" href="${ctx}/admin/users">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Người dùng hoạt động</span><span class="qa-kpi-icon qa-kpi-icon--green"><i class="bi bi-people" aria-hidden="true"></i></span></div>
  <strong id="adm-d-users" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Đăng nhập trong 7 ngày qua</span>
</a>
<a class="qa-kpi" href="${ctx}/admin/health">
  <div class="qa-kpi-heading"><span class="qa-kpi-label">Email gửi thất bại</span><span class="qa-kpi-icon qa-kpi-icon--red"><i class="bi bi-envelope-exclamation" aria-hidden="true"></i></span></div>
  <strong id="adm-d-email" class="qa-kpi-value">—</strong><span class="qa-kpi-foot">Kiểm tra hàng đợi gửi thư</span>
</a></div>
    <div class="row g-3 mb-4"><div class="col-lg-7"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-graph-up" aria-hidden="true"></i></span><h2 class="card-title mb-0">Yêu cầu theo ngày</h2><span class="qa-card-period">30 ngày gần nhất</span></div><div class="card-body qa-dashboard-chart"><canvas id="adm-d-chart-daily" role="img" aria-label="Yêu cầu theo ngày"></canvas></div></div></div><div class="col-lg-5"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-buildings" aria-hidden="true"></i></span><h2 class="card-title mb-0">Phân bố theo phòng ban</h2><span class="qa-card-period">Trong kỳ báo cáo</span></div><div class="card-body qa-dashboard-chart"><canvas id="adm-d-chart-departments" role="img" aria-label="Phân bố theo phòng ban"></canvas></div></div></div></div>
    <div class="mb-4"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-stopwatch" aria-hidden="true"></i></span><h2 class="card-title mb-0">Thời gian phản hồi đầu theo tuần</h2><span class="qa-card-period">Trung bình · phút làm việc</span></div><div class="card-body qa-dashboard-chart"><canvas id="adm-d-chart-frt" role="img" aria-label="Thời gian phản hồi đầu theo tuần"></canvas></div></div></div>
    <div class="qa-dashboard-section-label"><h2>Cảnh báo & hoạt động gần đây</h2><span>Giúp bạn chủ động điều phối hệ thống</span></div><div class="row g-3"><div class="col-xl-6"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-exclamation-triangle"></i></span><h2 class="card-title mb-0">Yêu cầu vi phạm SLA</h2><a class="qa-card-period" href="${ctx}/admin/tickets?slaStatus=BREACHED" aria-label="Xem Yêu cầu vi phạm SLA"><i class="bi bi-arrow-up-right"></i></a></div><div id="adm-alert-sla" class="list-group list-group-flush"></div></div></div>
<div class="col-xl-6"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-diagram-3"></i></span><h2 class="card-title mb-0">Chuyên mục chưa có tư vấn viên</h2><a class="qa-card-period" href="${ctx}/admin/managers" aria-label="Xem Chuyên mục chưa có tư vấn viên"><i class="bi bi-arrow-up-right"></i></a></div><div id="adm-alert-categories" class="list-group list-group-flush"></div></div></div>
<div class="col-xl-6"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-journal-x"></i></span><h2 class="card-title mb-0">FAQ cần cải thiện</h2><a class="qa-card-period" href="${ctx}/admin/faq" aria-label="Xem FAQ cần cải thiện"><i class="bi bi-arrow-up-right"></i></a></div><div id="adm-alert-faq" class="list-group list-group-flush"></div></div></div>
<div class="col-xl-6"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-search"></i></span><h2 class="card-title mb-0">Tìm kiếm chưa có kết quả</h2><a class="qa-card-period" href="${ctx}/admin/faq/synonyms" aria-label="Xem Tìm kiếm chưa có kết quả"><i class="bi bi-arrow-up-right"></i></a></div><div id="adm-alert-search" class="list-group list-group-flush"></div></div></div>
<div class="col-xl-6"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-star-half"></i></span><h2 class="card-title mb-0">Đánh giá thấp gần đây</h2><a class="qa-card-period" href="${ctx}/admin/tickets?maxRating=2" aria-label="Xem Đánh giá thấp gần đây"><i class="bi bi-arrow-up-right"></i></a></div><div id="adm-alert-ratings" class="list-group list-group-flush"></div></div></div>
<div class="col-xl-6"><div class="card h-100"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-clock-history"></i></span><h2 class="card-title mb-0">Hoạt động gần đây</h2><a class="qa-card-period" href="${ctx}/admin/audit-logs" aria-label="Xem Hoạt động gần đây"><i class="bi bi-arrow-up-right"></i></a></div><div id="adm-alert-audit" class="list-group list-group-flush"></div></div></div><div class="col-12"><div class="card"><div class="card-header"><span class="qa-card-heading-icon"><i class="bi bi-envelope-exclamation"></i></span><h2 class="card-title mb-0">Tình trạng gửi email</h2><a class="qa-card-period" href="${ctx}/admin/health">Kiểm tra hệ thống <i class="bi bi-arrow-right"></i></a></div><div class="card-body text-secondary" id="adm-alert-email"></div></div></div></div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js" defer></script>
<script src="${ctx}/assets/js/admin/dashboard.js" defer></script>
