<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Tư vấn trực tuyến | QAUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/manager/chat-desk.css">
</head>
<div class="qa-page-manager-chat-desk" data-qa-view="manager-chat-desk">


<%-- Hero banner: tiêu đề, trạng thái kết nối và liên kết lịch sử tư vấn. --%>
<div class="manager-hero-banner">
    <div class="hero-content-wrap d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3">
        <div>
            <div class="hero-eyebrow-pill">
                <span class="hero-eyebrow-dot"></span>
                <span>Kết Nối Sinh Viên • Hỗ Trợ Trực Tuyến</span>
            </div>
            <h1 class="hero-title">
                <i class="bi bi-chat-dots text-info me-2"></i>Tư Vấn Trực Tuyến
            </h1>
            <p class="hero-subtitle">Tiếp nhận hội thoại và đồng hành cùng sinh viên trong thời gian thực.</p>
        </div>
        <div class="d-flex flex-wrap align-items-center gap-2">
            <a class="hero-action-btn" href="${ctx}/manager/chat-history">
                <i class="bi bi-clock-history"></i>
                <span>Lịch sử tư vấn</span>
            </a>
            <span id="chat-connection-status" class="badge bg-secondary-lt" role="status">Đang kết nối…</span>
        </div>
    </div>
</div>

<%-- Bố trí hàng chờ, khung hội thoại, thông tin sinh viên và thao tác chuyển chat. --%>
<div id="chat-desk" class="row g-3 u-min-h-70vh">
  <div class="col-12 col-lg-3 d-flex flex-column gap-3">
    <div class="card">
      <div class="card-header history-card-header d-flex justify-content-between align-items-center">
        <h2 class="card-title">Hàng chờ <span id="chat-queue-count" class="badge bg-primary-lt">0</span></h2>
      </div>
      <div id="chat-queue" class="list-group list-group-flush" aria-live="polite"></div>
      <div class="card-footer d-flex flex-wrap align-items-center justify-content-between gap-2">
        <button id="chat-queue-prev" class="btn btn-sm btn-outline-secondary" type="button"><i class="bi bi-chevron-left"></i>Trước</button>
        <span id="chat-queue-page" class="small text-secondary fw-bold">Trang 1</span>
        <button id="chat-queue-next" class="btn btn-sm btn-outline-secondary" type="button">Sau<i class="bi bi-chevron-right"></i></button>
      </div>
    </div>
    <div class="card">
      <div class="card-header history-card-header"><h2 class="card-title">Đang tư vấn</h2></div>
      <div id="chat-mine" class="list-group list-group-flush"></div>
    </div>
  </div>

  <div class="col-12 col-lg-6 d-flex">
    <div class="card h-100 w-100">
      <div class="card-header chat-card-header d-flex flex-wrap gap-2 justify-content-between align-items-center">
        <div class="d-flex align-items-center gap-2 min-w-0">
          <span class="avatar avatar-md bg-white text-primary rounded-3 shadow-sm u-width-42px u-height-42px u-display-inline-flex u-align-items-center u-justify-content-center u-text-size-1-2rem">
            <i class="bi bi-headset"></i>
          </span>
          <div class="min-w-0">
            <h2 id="chat-title" class="card-title text-truncate">Chọn hội thoại</h2>
            <div id="chat-subtitle" class="text-white-50 small text-truncate"></div>
          </div>
        </div>
        <div class="btn-group btn-group-sm">
          <button id="chat-transfer" class="btn btn-outline-light" disabled><i class="bi bi-arrow-left-right"></i>Chuyển cho…</button>
          <button id="chat-end" class="btn btn-outline-light" disabled><i class="bi bi-box-arrow-right"></i>Kết thúc</button>
        </div>
      </div>
      <div class="card-body chat-body">
        <div id="chat-messages" class="qa-chat-messages" aria-live="polite"></div>
        <div id="chat-typing" class="qa-chat-typing u-display-none" aria-live="polite">
          <span class="spinner-grow spinner-grow-sm me-1 text-primary" role="status"></span>
          <span id="chat-typing-text">Sinh viên đang nhập…</span>
        </div>
        <form id="chat-compose" class="p-3 bg-light border-top">
          <div class="input-group shadow-sm rounded-3 overflow-hidden">
            <label class="btn btn-outline-secondary bg-white border-end-0 px-3" title="Gửi tệp">
              <i class="bi bi-paperclip fs-5"></i>
              <input id="chat-file" type="file" class="d-none" disabled/>
            </label>
            <textarea id="chat-input" class="form-control border-start-0 border-end-0 u-resize-none u-max-h-140px" rows="1" maxlength="2000"
                      placeholder="Nhập tin nhắn… (Enter để gửi, Shift + Enter để xuống dòng)" disabled
                      aria-label="Nội dung tin nhắn"></textarea>
            <button id="chat-send" class="btn btn-primary px-3" type="submit" disabled title="Gửi tin nhắn">
              <i class="bi bi-send-fill"></i>
            </button>
          </div>
          <div id="chat-upload-status" class="small text-secondary mt-2 u-display-none" role="status"></div>
        </form>
      </div>
      <div class="card-footer">
        <button id="chat-to-ticket" class="btn btn-outline-primary btn-sm" disabled><i class="bi bi-ticket-perforated"></i>Tạo ticket từ hội thoại</button>
        <span id="chat-ticket-link" class="ms-2"></span>
      </div>
    </div>
  </div>

  <div class="col-12 col-lg-3 d-flex flex-column gap-3">
    <div class="card">
      <div class="card-header history-card-header"><h2 class="card-title">Sinh viên</h2></div>
      <div id="chat-student" class="card-body text-secondary">Chọn hội thoại để xem thông tin.</div>
    </div>
    <div class="card">
      <div class="card-header history-card-header"><h2 class="card-title">Ticket gần đây</h2></div>
      <div id="chat-recent-tickets" class="list-group list-group-flush"></div>
    </div>
  </div>
</div>

<div class="modal fade" id="chat-transfer-modal" tabindex="-1" aria-labelledby="chat-transfer-label" aria-hidden="true"><div class="modal-dialog modal-dialog-scrollable"><div class="modal-content"><div class="modal-header"><h3 id="chat-transfer-label" class="modal-title">Chuyển hội thoại</h3><button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button></div><div class="modal-body"><label for="chat-transfer-target" class="form-label">Tư vấn viên nhận</label><select id="chat-transfer-target" class="form-select"></select></div><div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button><button id="chat-transfer-confirm" type="button" class="btn btn-primary">Chuyển</button></div></div></div></div>
<script src="${ctx}/assets/js/manager/chat-desk.js?v=20261028" defer></script>
<script src="${pageContext.request.contextPath}/assets/js/pages/manager/chat-desk.js"></script>
</div>
