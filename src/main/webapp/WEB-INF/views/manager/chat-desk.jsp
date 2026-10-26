<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Tư vấn trực tuyến | QAUTE</title>
<div class="qa-page-manager-chat-desk" data-qa-view="manager-chat-desk">
<style>
/* ==========================================================================
   MANAGER CHAT DESK - DESIGN SYSTEM ĐỒNG BỘ VỚI SINH VIÊN (2026)
   Palette: Azure Navy (#091e3e) -> Royal Blue (#124991) -> Deep Teal (#087d92)
   Accent: Electric Cyan (#00d2ff), Emerald (#10b981), Amber (#f59e0b)
   ========================================================================== */
body.qa-workspace--manager .qa-page-manager-chat-desk {
    --mgr-primary: #124991;
    --mgr-primary-dark: #091e3e;
    --mgr-teal: #087d92;
    --mgr-cyan: #00d2ff;
    --mgr-emerald: #10b981;
    --mgr-amber: #f59e0b;
    --mgr-border: #e2e8f0;
    --mgr-shadow: 0 10px 30px -5px rgba(9, 30, 62, 0.07), 0 4px 10px -2px rgba(9, 30, 62, 0.04);
    min-width: 0;
    color: #1e293b;
    font-feature-settings: "cv02", "cv03", "cv04", "cv11";
}
body.qa-workspace--manager .qa-page-manager-chat-desk .min-w-0 { min-width: 0; }

/* 1. HERO BANNER */
body.qa-workspace--manager .qa-page-manager-chat-desk .manager-hero-banner {
    position: relative;
    background: linear-gradient(135deg, #091e3e 0%, #124991 55%, #087d92 100%);
    border-radius: 24px;
    padding: clamp(1.25rem, 2.5vw, 2rem);
    color: #ffffff;
    overflow: hidden;
    margin-bottom: 1.5rem;
    box-shadow: 0 20px 40px -15px rgba(9, 30, 62, 0.35);
    border: 1px solid rgba(255, 255, 255, 0.12);
}
body.qa-workspace--manager .qa-page-manager-chat-desk .manager-hero-banner::before {
    content: '';
    position: absolute;
    top: -40%; right: -10%;
    width: 420px; height: 420px;
    background: radial-gradient(circle, rgba(0, 210, 255, 0.22) 0%, rgba(18, 73, 145, 0) 70%);
    border-radius: 50%;
    pointer-events: none;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .manager-hero-banner::after {
    content: '';
    position: absolute;
    bottom: -50%; left: 20%;
    width: 350px; height: 350px;
    background: radial-gradient(circle, rgba(16, 185, 129, 0.18) 0%, rgba(9, 30, 62, 0) 70%);
    border-radius: 50%;
    pointer-events: none;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .hero-content-wrap { position: relative; z-index: 2; }
body.qa-workspace--manager .qa-page-manager-chat-desk .hero-eyebrow-pill {
    display: inline-flex; align-items: center; gap: 0.5rem;
    background: rgba(255, 255, 255, 0.12);
    backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px);
    border: 1px solid rgba(255, 255, 255, 0.22);
    border-radius: 999px; padding: 0.35rem 0.95rem;
    font-size: 0.78rem; font-weight: 700; text-transform: uppercase;
    letter-spacing: 0.08em; color: #bbf2f6; margin-bottom: 0.75rem;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .hero-eyebrow-dot {
    width: 8px; height: 8px; border-radius: 50%;
    background-color: var(--mgr-cyan); box-shadow: 0 0 10px var(--mgr-cyan);
    animation: mgrPulse 2s infinite ease-in-out;
}
@keyframes mgrPulse {
    0%, 100% { transform: scale(1); opacity: 1; }
    50% { transform: scale(1.35); opacity: 0.7; }
}
body.qa-workspace--manager .qa-page-manager-chat-desk .hero-title {
    font-size: clamp(1.4rem, 2.4vw, 2rem); font-weight: 800;
    letter-spacing: -0.03em; line-height: 1.25; margin-bottom: 0.4rem; color: #ffffff;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .hero-subtitle {
    font-size: 0.92rem; color: rgba(255, 255, 255, 0.85);
    max-width: 640px; line-height: 1.55; margin: 0;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .hero-action-btn {
    background: rgba(255, 255, 255, 0.14);
    border: 1px solid rgba(255, 255, 255, 0.25);
    color: #ffffff; backdrop-filter: blur(6px);
    border-radius: 12px; padding: 0.55rem 1.1rem;
    font-size: 0.85rem; font-weight: 600;
    transition: all 0.22s ease; text-decoration: none;
    display: inline-flex; align-items: center; gap: 0.5rem;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .hero-action-btn:hover {
    background: rgba(255, 255, 255, 0.28); color: #ffffff;
    transform: translateY(-2px); box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}
body.qa-workspace--manager .qa-page-manager-chat-desk #chat-connection-status {
    border-radius: 999px; padding: 0.45rem 0.95rem; font-weight: 700;
}

/* 2. CARD STYLING */
body.qa-workspace--manager .qa-page-manager-chat-desk .card {
    position: relative;
    border: 1px solid var(--mgr-border);
    border-radius: 20px;
    background: #ffffff;
    box-shadow: var(--mgr-shadow);
    overflow: hidden;
    transition: border-color 0.25s ease, box-shadow 0.25s ease;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .card:hover { border-color: #cfdbea; }
body.qa-workspace--manager .qa-page-manager-chat-desk .card-title {
    font-size: 1.02rem; font-weight: 800; letter-spacing: -0.02em; margin: 0; color: #0f2547;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .card-header.chat-card-header {
    background: linear-gradient(135deg, #091e3e 0%, #124991 70%, #087d92 100%);
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    color: #ffffff; padding: 1rem 1.25rem;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .card-header.chat-card-header .card-title { color: #ffffff; }
body.qa-workspace--manager .qa-page-manager-chat-desk .card-header.history-card-header {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border-bottom: 1px solid #e2e8f0; color: #091e3e; padding: 0.9rem 1.15rem;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .card-body { color: #334155; }
body.qa-workspace--manager .qa-page-manager-chat-desk .card-footer {
    background: #f8fafc; border-top: 1px solid #e2e8f0; padding: 0.85rem 1.15rem;
}

/* 3. FORM CONTROLS */
body.qa-workspace--manager .qa-page-manager-chat-desk .form-control,
body.qa-workspace--manager .qa-page-manager-chat-desk .form-select {
    border: 1.5px solid #cbd5e1; border-radius: 12px; padding: 0.6rem 0.9rem;
    color: #1e293b; font-size: 0.92rem; background-color: #ffffff;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04); transition: all 0.2s ease;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .form-control:focus,
body.qa-workspace--manager .qa-page-manager-chat-desk .form-select:focus {
    border-color: #124991; box-shadow: 0 0 0 4px rgba(18, 73, 145, 0.12); outline: none;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .input-group > .form-control { border-radius: 0; }
body.qa-workspace--manager .qa-page-manager-chat-desk .input-group > :first-child {
    border-top-left-radius: 12px; border-bottom-left-radius: 12px;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .input-group > :last-child {
    border-top-right-radius: 12px; border-bottom-right-radius: 12px;
}
/* Ô nhập tin nhắn: giữ chiều cao một dòng và tự giãn khi xuống hàng. */
body.qa-workspace--manager .qa-page-manager-chat-desk #chat-input {
    min-height: 44px; max-height: 140px; padding: 0.55rem 0.9rem; line-height: 1.5;
}

/* 4. BUTTONS */
body.qa-workspace--manager .qa-page-manager-chat-desk .btn {
    border-radius: 12px; font-weight: 700;
    transition: all 0.2s cubic-bezier(0.16, 1, 0.3, 1);
    display: inline-flex; align-items: center; gap: 0.4rem;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .btn:not(:disabled):hover { transform: translateY(-1px); }
body.qa-workspace--manager .qa-page-manager-chat-desk .btn-primary {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%);
    border: none; color: #ffffff; box-shadow: 0 5px 14px rgba(18, 73, 145, 0.26);
}
body.qa-workspace--manager .qa-page-manager-chat-desk .btn-primary:not(:disabled):hover {
    background: linear-gradient(135deg, #091e3e 0%, #124991 100%);
    box-shadow: 0 8px 20px rgba(18, 73, 145, 0.34);
}
body.qa-workspace--manager .qa-page-manager-chat-desk .btn-outline-primary {
    border: 1.5px solid #cbd5e1; color: #124991; background: #ffffff;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .btn-outline-primary:not(:disabled):hover {
    background: #f1f5f9; border-color: #94a3b8; color: #0f172a;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .btn-outline-secondary {
    border: 1.5px solid #cbd5e1; color: #475569; background: #ffffff;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .btn-outline-secondary:not(:disabled):hover {
    background: #f1f5f9; border-color: #94a3b8; color: #0f172a;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .btn-outline-danger {
    border: 1.5px solid #fecaca; color: #dc2626; background: #ffffff;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .btn-outline-danger:not(:disabled):hover { background: #fef2f2; }
body.qa-workspace--manager .qa-page-manager-chat-desk .btn-outline-light {
    border: 1px solid rgba(255, 255, 255, 0.4); color: #ffffff; background: transparent;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .btn-outline-light:not(:disabled):hover {
    background: rgba(255, 255, 255, 0.18); color: #ffffff;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .btn-sm { font-size: 0.8rem; padding: 0.4rem 0.8rem; }

/* 5. CHAT ROOM & MESSAGES (đồng bộ với giao diện sinh viên) */
body.qa-workspace--manager .qa-page-manager-chat-desk .chat-body {
    display: flex; flex-direction: column; flex: 1 1 auto; min-height: 0; padding: 0;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .qa-chat-messages {
    height: 460px !important; max-height: 62vh !important; flex: 1 1 auto;
    overflow-y: auto; padding: 1.25rem; gap: 0.85rem;
    background: radial-gradient(circle at 100% 0%, rgba(18, 73, 145, 0.05), transparent 40%), #f8fafc;
    display: flex; flex-direction: column;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .qa-chat-bubble {
    max-width: 78%; font-size: 0.92rem; white-space: pre-wrap;
    padding: 0.75rem 1rem; border-radius: 16px;
    position: relative; word-break: break-word; line-height: 1.55;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .qa-chat-me .qa-chat-bubble {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%) !important;
    color: #ffffff !important; border-bottom-right-radius: 4px;
    box-shadow: 0 5px 15px rgba(18, 73, 145, 0.2);
    margin-left: auto;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .qa-chat-other .qa-chat-bubble {
    background: #ffffff !important; color: #1e293b !important; border: 1px solid #e2e8f0 !important;
    border-bottom-left-radius: 4px; box-shadow: 0 3px 10px rgba(0, 0, 0, 0.04);
    margin-right: auto;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .qa-chat-system .qa-chat-bubble {
    background-color: #f1f5f9 !important; color: #64748b !important; font-size: 0.8rem;
    padding: 0.4rem 0.95rem; border-radius: 999px; border: 1px solid #e2e8f0 !important;
    margin: 0 auto; text-align: center;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .qa-chat-sender { color: #087d92; font-weight: 700; display: block; margin-bottom: 0.25rem; font-size: 0.75rem; }
body.qa-workspace--manager .qa-page-manager-chat-desk .qa-chat-meta {
    font-size: 0.7rem; display: flex; align-items: center; justify-content: flex-end; gap: 0.3rem;
    margin-top: 0.35rem; opacity: 0.85;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .qa-chat-me .qa-chat-meta { color: rgba(255, 255, 255, 0.85); }
body.qa-workspace--manager .qa-page-manager-chat-desk .qa-chat-other .qa-chat-meta { color: #64748b; }
body.qa-workspace--manager .qa-page-manager-chat-desk .qa-chat-typing {
    font-size: 0.82rem; color: #64748b; font-style: italic;
    padding: 0.4rem 1.25rem; min-height: 1.8rem;
    background-color: #f8fafc; border-top: 1px solid #f1f5f9;
    display: flex; align-items: center;
}

/* 6. QUEUE / LISTS */
body.qa-workspace--manager .qa-page-manager-chat-desk .list-group-item { border-color: #f1f5f9; }
body.qa-workspace--manager .qa-page-manager-chat-desk .list-group-item-action:hover { background: #f8faff; }
body.qa-workspace--manager .qa-page-manager-chat-desk .list-group-item.active {
    background: linear-gradient(135deg, #124991, #087d92);
    border-color: transparent; color: #ffffff;
}
body.qa-workspace--manager .qa-page-manager-chat-desk .list-group-item.active .small,
body.qa-workspace--manager .qa-page-manager-chat-desk .list-group-item.active .text-secondary { color: rgba(255, 255, 255, 0.85) !important; }
body.qa-workspace--manager .qa-page-manager-chat-desk .badge.bg-primary-lt { background: rgba(18, 73, 145, 0.1); color: #124991; }

/* RESPONSIVE */
@media (max-width: 991.98px) {
    body.qa-workspace--manager .qa-page-manager-chat-desk .manager-hero-banner { border-radius: 18px; padding: 1.25rem; }
    body.qa-workspace--manager .qa-page-manager-chat-desk .qa-chat-messages { min-height: 340px; }
}
@media (prefers-reduced-motion: reduce) {
    body.qa-workspace--manager .qa-page-manager-chat-desk *,
    body.qa-workspace--manager .qa-page-manager-chat-desk *::before,
    body.qa-workspace--manager .qa-page-manager-chat-desk *::after {
        scroll-behavior: auto !important;
        animation-duration: 0.01ms !important;
        animation-iteration-count: 1 !important;
        transition-duration: 0.01ms !important;
    }
}
</style>

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
<div id="chat-desk" class="row g-3" style="min-height:70vh">
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
          <span class="avatar avatar-md bg-white text-primary rounded-3 shadow-sm" style="width:42px;height:42px;display:inline-flex;align-items:center;justify-content:center;font-size:1.2rem;">
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
        <div id="chat-typing" class="qa-chat-typing" style="display:none;" aria-live="polite">
          <span class="spinner-grow spinner-grow-sm me-1 text-primary" role="status"></span>
          <span id="chat-typing-text">Sinh viên đang nhập…</span>
        </div>
        <form id="chat-compose" class="p-3 bg-light border-top">
          <div class="input-group shadow-sm rounded-3 overflow-hidden">
            <label class="btn btn-outline-secondary bg-white border-end-0 px-3" title="Gửi tệp">
              <i class="bi bi-paperclip fs-5"></i>
              <input id="chat-file" type="file" class="d-none" disabled/>
            </label>
            <textarea id="chat-input" class="form-control border-start-0 border-end-0" rows="1" maxlength="2000"
                      placeholder="Nhập tin nhắn… (Enter để gửi, Shift + Enter để xuống dòng)" disabled
                      aria-label="Nội dung tin nhắn" style="resize:none; max-height:140px;"></textarea>
            <button id="chat-send" class="btn btn-primary px-3" type="submit" disabled title="Gửi tin nhắn">
              <i class="bi bi-send-fill"></i>
            </button>
          </div>
          <div id="chat-upload-status" class="small text-secondary mt-2" style="display:none;" role="status"></div>
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
<script>
  // Script bổ trợ xử lý UI tại chỗ để tránh cache JS cũ
  document.addEventListener('DOMContentLoaded', function() {
    const chatInput = document.getElementById('chat-input');
    if (chatInput) {
      chatInput.addEventListener('input', function() {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
      });
    }
  });
</script>
</div>
