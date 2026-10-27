<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Tổng quan tư vấn viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/manager/dashboard.css">
</head>
<div class="qa-page-manager-dashboard" data-qa-view="manager-dashboard">


<div id="manager-dashboard" data-api="${ctx}/api/manager/dashboard" data-context="${ctx}">
  <%-- ==================== HERO BANNER ==================== --%>
  <section class="qa-dashboard-hero" aria-labelledby="manager-welcome">
    <div class="qa-dashboard-hero-content">
      <span class="qa-dashboard-eyebrow">
        <span class="qa-pulse-dot" aria-hidden="true"></span>
        <i class="bi bi-person-workspace me-1" aria-hidden="true"></i>Không gian tư vấn viên
      </span>
      <h1 id="manager-welcome" class="qa-view-title" data-qa-page="manager-dashboard">
        <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-stars"></i></span>
        Xin chào, <c:out value="${me.fullName}"/>!
      </h1>
      <p>Bảng điều khiển chuyên trách giúp bạn quản lý hàng đợi, theo dõi cam kết SLA, phản hồi tư vấn trực tuyến và nâng cao chất lượng phục vụ sinh viên mỗi ngày.</p>
      <div class="qa-manager-hero-badges">
        <span class="qa-manager-hero-badge"><i class="bi bi-lightning-charge-fill"></i>Ưu tiên việc cần xử lý ngay</span>
        <span class="qa-manager-hero-badge"><i class="bi bi-bar-chart-fill"></i>Theo dõi hiệu suất trực quan</span>
        <span class="qa-manager-hero-badge"><i class="bi bi-shield-check"></i>Đồng bộ dữ liệu thời gian thực</span>
      </div>
      <div class="qa-dashboard-hero-actions">
        <a class="btn btn-white shadow-sm" href="${ctx}/manager/queue"><i class="bi bi-inbox-fill me-2"></i>Mở hàng đợi</a>
        <a class="btn btn-outline-light" href="${ctx}/manager/chat"><i class="bi bi-chat-dots-fill me-2"></i>Tư vấn trực tuyến</a>
      </div>
    </div>
    <div class="qa-dashboard-hero-aside" aria-hidden="true">
      <div class="qa-dashboard-hero-meta">
        <span class="qa-dashboard-date" data-workspace-date><i class="bi bi-calendar3 me-1"></i>Hôm nay</span>
        <span class="qa-dashboard-hero-mark"><i class="bi bi-chat-square-heart-fill"></i></span>
      </div>
      <div class="qa-manager-hero-visual">
        <div class="qa-manager-hero-stack">
          <div class="qa-manager-hero-note">
            <strong><i class="bi bi-clipboard2-pulse-fill"></i>Trọng tâm</strong>
            <p>KPI, biểu đồ và danh sách ưu tiên được gom thành cụm rõ ràng để bạn nắm bắt nhanh nhất.</p>
          </div>
          <div class="qa-manager-hero-note">
            <strong><i class="bi bi-calendar2-check-fill"></i>Nhịp làm việc</strong>
            <p>Lịch tư vấn, chat chờ và hàng đợi xử lý hiển thị trực quan và hỗ trợ thao tác mượt mà.</p>
          </div>
        </div>
      </div>
    </div>
  </section>

  <%-- ==================== 3 HIGHLIGHTS CARDS ==================== --%>
  <section class="qa-manager-overview-strip" aria-label="Điểm nhấn vận hành">
    <div class="qa-manager-overview-card">
      <h3><i class="bi bi-inboxes-fill"></i>Điều phối hàng đợi</h3>
      <p>Thấy nhanh ticket chưa nhận, ticket đang phụ trách và nhóm rủi ro SLA để vào việc đúng thứ tự ưu tiên.</p>
      <div class="qa-manager-overview-meta">
        <span><i class="bi bi-exclamation-triangle-fill"></i>SLA nổi bật</span>
        <span><i class="bi bi-arrow-left-right"></i>Luồng nhận việc rõ</span>
      </div>
    </div>
    <div class="qa-manager-overview-card">
      <h3><i class="bi bi-chat-heart-fill"></i>Hỗ trợ đa kênh</h3>
      <p>Chat trực tuyến, lịch hẹn và queue được đặt trong cùng ngữ cảnh giúp chuyển tác vụ nhanh và liền mạch.</p>
      <div class="qa-manager-overview-meta">
        <span><i class="bi bi-chat-dots-fill"></i>Chat chờ</span>
        <span><i class="bi bi-calendar-week-fill"></i>Lịch trong ngày</span>
      </div>
    </div>
    <div class="qa-manager-overview-card">
      <h3><i class="bi bi-graph-up-arrow"></i>Theo dõi chất lượng</h3>
      <p>Chỉ số CSAT, tốc độ phản hồi đầu và phân bố chuyên mục được biểu thị sinh động nhằm cải thiện dịch vụ liên tục.</p>
      <div class="qa-manager-overview-meta">
        <span><i class="bi bi-star-fill"></i>CSAT</span>
        <span><i class="bi bi-stopwatch-fill"></i>FRT trung vị</span>
      </div>
    </div>
  </section>

  <%-- Trạng thái tải hoặc lỗi trước khi dữ liệu tổng quan sẵn sàng --%>
  <div id="mgr-dashboard-loading" class="qa-dashboard-loading" role="status">
    <span class="visually-hidden">Đang tải dữ liệu tổng quan…</span>
    <div class="row g-3">
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
    </div>
  </div>
  <div id="mgr-dashboard-error" class="alert alert-danger d-none" role="alert"></div>

  <%-- ==================== MAIN DASHBOARD CONTENT ==================== --%>
  <div id="mgr-dashboard-content" class="d-none">
    <div class="qa-dashboard-section-label">
      <h2><i class="bi bi-clipboard2-pulse-fill" aria-hidden="true"></i>Công việc & chất lượng tư vấn</h2>
      <span>Trong phạm vi phụ trách của bạn</span>
    </div>

    <%-- 8 KPI Metric Cards --%>
    <div class="qa-kpi-grid">
      <a class="qa-kpi" href="${ctx}/manager/queue?tab=unassigned">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Chưa nhận trong phạm vi</span>
          <span class="qa-kpi-icon"><i class="bi bi-inbox-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-unassigned" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Các yêu cầu đang sẵn sàng để bạn tiếp nhận.</span>
        <span class="qa-kpi-trail"><i class="bi bi-arrow-up-right-circle-fill"></i>Mở nhóm chưa nhận</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/queue?tab=mine">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Yêu cầu đang phụ trách</span>
          <span class="qa-kpi-icon"><i class="bi bi-person-workspace" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-mine" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Khối lượng công việc hiện tại bạn đang theo dõi trực tiếp.</span>
        <span class="qa-kpi-trail"><i class="bi bi-person-check-fill"></i>Xem ticket của tôi</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/queue?tab=waiting">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Chờ sinh viên phản hồi</span>
          <span class="qa-kpi-icon"><i class="bi bi-hourglass-split" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-waiting" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Nhóm yêu cầu cần theo dõi thông tin bổ sung từ sinh viên.</span>
        <span class="qa-kpi-trail"><i class="bi bi-reply-fill"></i>Theo dõi phản hồi</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/queue?tab=slarisk">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">SLA rủi ro / quá hạn</span>
          <span class="qa-kpi-icon"><i class="bi bi-exclamation-triangle-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-sla" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Những trường hợp cần ưu tiên để tránh trễ hạn hoặc vượt SLA.</span>
        <span class="qa-kpi-trail"><i class="bi bi-fire"></i>Ưu tiên xử lý</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/appointments?tab=today">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Lịch hẹn hôm nay</span>
          <span class="qa-kpi-icon"><i class="bi bi-calendar-check-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-appointments" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Số buổi tư vấn trong ngày bạn cần chuẩn bị và theo dõi.</span>
        <span class="qa-kpi-trail"><i class="bi bi-calendar2-week-fill"></i>Mở lịch trong ngày</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/chat">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Hội thoại đang chờ</span>
          <span class="qa-kpi-icon"><i class="bi bi-chat-dots-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-chats" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Phiên chat đang chờ phản hồi để bạn hỗ trợ sinh viên kịp thời.</span>
        <span class="qa-kpi-trail"><i class="bi bi-chat-left-heart-fill"></i>Vào kênh chat</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/reports">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Mức độ hài lòng</span>
          <span class="qa-kpi-icon"><i class="bi bi-star-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-csat" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Điểm đánh giá dịch vụ trong 30 ngày gần nhất.</span>
        <span class="qa-kpi-trail"><i class="bi bi-bar-chart-line-fill"></i>Xem hiệu quả phục vụ</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/reports">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Phản hồi đầu trung vị</span>
          <span class="qa-kpi-icon"><i class="bi bi-stopwatch-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-frt" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Tốc độ phản hồi đầu trong 30 ngày, theo phút làm việc.</span>
        <span class="qa-kpi-trail"><i class="bi bi-speedometer2"></i>Theo dõi tốc độ</span>
      </a>
    </div>

    <%-- Charts & Operations Grid --%>
    <div class="qa-manager-main-grid">
      <div class="card h-100">
        <div class="card-header">
          <div class="d-flex align-items-center gap-2">
            <span class="qa-card-heading-icon"><i class="bi bi-graph-up" aria-hidden="true"></i></span>
            <h2 class="card-title mb-0">Yêu cầu theo ngày</h2>
          </div>
          <span class="qa-card-period">14 ngày gần nhất</span>
        </div>
        <div class="card-body qa-dashboard-chart"><canvas id="mgr-chart-daily" role="img" aria-label="Yêu cầu theo ngày"></canvas></div>
      </div>
      <div class="qa-manager-side-stack">
        <div class="card qa-manager-side-panel h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-grid-1x2-fill" aria-hidden="true"></i></span>
              <h2 class="card-title mb-0">Bức tranh chuyên mục</h2>
            </div>
            <span class="qa-card-period">14 ngày gần nhất</span>
          </div>
          <div class="card-body qa-dashboard-chart"><canvas id="mgr-chart-category" role="img" aria-label="Phân bố theo chuyên mục"></canvas></div>
        </div>
        <div class="card qa-manager-side-panel">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-compass-fill"></i></span>
              <h2 class="card-title mb-0">Nhịp vận hành</h2>
            </div>
          </div>
          <div class="card-body">
            <p class="text-secondary small mb-3">Truy cập nhanh vào các công cụ quản lý lịch hẹn cá nhân, kho câu trả lời mẫu và đề xuất FAQ cho sinh viên.</p>
            <div class="qa-manager-side-list">
              <a class="qa-manager-side-link" href="${ctx}/manager/schedule">
                <span class="qa-kpi-icon"><i class="bi bi-calendar-week-fill"></i></span>
                <div><strong>Lịch làm việc</strong><div class="small text-secondary">Quản lý thời gian tư vấn của bạn</div></div>
              </a>
              <a class="qa-manager-side-link" href="${ctx}/manager/canned-responses">
                <span class="qa-kpi-icon u-custom-mgr-kpi-accent-8b5cf6"><i class="bi bi-chat-left-text-fill"></i></span>
                <div><strong>Mẫu trả lời</strong><div class="small text-secondary">Hỗ trợ sinh viên nhanh và nhất quán</div></div>
              </a>
              <a class="qa-manager-side-link" href="${ctx}/manager/faq">
                <span class="qa-kpi-icon u-custom-mgr-kpi-accent-10b981"><i class="bi bi-journal-text"></i></span>
                <div><strong>FAQ đề xuất</strong><div class="small text-secondary">Chia sẻ kiến thức hữu ích cho sinh viên</div></div>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <%-- Work List & Quick Access --%>
    <div class="qa-dashboard-work-grid">
      <div class="col-xl-8">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-lightning-charge-fill"></i></span>
              <h2 class="card-title mb-0">Cần làm ngay</h2>
            </div>
            <a class="qa-card-period" href="${ctx}/manager/queue?tab=slarisk">Xem hàng đợi <i class="bi bi-arrow-right ms-1"></i></a>
          </div>
          <div id="mgr-urgent-list" class="list-group list-group-flush"></div>
        </div>
      </div>
      <div class="col-xl-4">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-grid-fill"></i></span>
              <h2 class="card-title mb-0">Truy cập nhanh</h2>
            </div>
          </div>
          <div class="list-group list-group-flush">
            <a class="list-group-item list-group-item-action d-flex align-items-center gap-3" href="${ctx}/manager/schedule">
              <span class="qa-kpi-icon"><i class="bi bi-calendar-week-fill"></i></span>
              <div>
                <strong>Lịch làm việc</strong>
                <div class="small text-secondary mt-1">Quản lý thời gian tư vấn của bạn</div>
              </div>
            </a>
            <a class="list-group-item list-group-item-action d-flex align-items-center gap-3" href="${ctx}/manager/canned-responses">
              <span class="qa-kpi-icon u-custom-mgr-kpi-accent-8b5cf6"><i class="bi bi-chat-left-text-fill"></i></span>
              <div>
                <strong>Mẫu trả lời</strong>
                <div class="small text-secondary mt-1">Hỗ trợ sinh viên nhanh và nhất quán</div>
              </div>
            </a>
            <a class="list-group-item list-group-item-action d-flex align-items-center gap-3" href="${ctx}/manager/faq">
              <span class="qa-kpi-icon u-custom-mgr-kpi-accent-10b981"><i class="bi bi-journal-text"></i></span>
              <div>
                <strong>FAQ đề xuất</strong>
                <div class="small text-secondary mt-1">Chia sẻ kiến thức hữu ích cho sinh viên</div>
              </div>
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js" defer></script>
<script src="${ctx}/assets/js/manager/dashboard.js" defer></script>
</div>
