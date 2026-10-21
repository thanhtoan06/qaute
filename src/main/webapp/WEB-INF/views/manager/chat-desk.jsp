<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Tư vấn trực tuyến</title>
<div class="qa-page-heading"><div><span class="qa-page-eyebrow">Kết nối sinh viên</span><h1 class="page-title mb-0">Tư vấn trực tuyến</h1><p>Tiếp nhận hội thoại và đồng hành cùng sinh viên trong thời gian thực.</p></div><span id="chat-connection-status" class="badge bg-secondary-lt" role="status">Đang kết nối…</span></div>
<div id="chat-desk" class="row g-3" style="min-height:70vh">
  <div class="col-12 col-lg-3">
    <div class="card mb-3"><div class="card-header"><h2 class="card-title">Hàng chờ <span id="chat-queue-count" class="badge bg-blue-lt">0</span></h2></div>
      <div id="chat-queue" class="list-group list-group-flush" aria-live="polite"></div>
      <div class="card-footer d-flex flex-wrap align-items-center gap-2"><button id="chat-queue-prev" class="btn btn-sm" type="button">Trước</button><span id="chat-queue-page">Trang 1</span><button id="chat-queue-next" class="btn btn-sm" type="button">Sau</button></div></div>
    <div class="card"><div class="card-header"><h2 class="card-title">Đang tư vấn</h2></div>
      <div id="chat-mine" class="list-group list-group-flush"></div></div>
  </div>
  <div class="col-12 col-lg-6">
    <div class="card h-100">
      <div class="card-header d-flex flex-wrap gap-2 justify-content-between align-items-center"><div><h2 id="chat-title" class="card-title">Chọn hội thoại</h2><div id="chat-subtitle" class="text-secondary small"></div></div>
        <div class="btn-group btn-group-sm"><button id="chat-transfer" class="btn btn-outline-primary" disabled>Chuyển cho…</button><button id="chat-end" class="btn btn-outline-danger" disabled>Kết thúc</button></div></div>
      <div class="card-body d-flex flex-column" style="min-height:480px">
        <div id="chat-messages" class="flex-fill overflow-auto" style="max-height:55vh" aria-live="polite"></div>
        <div id="chat-typing" class="small text-secondary" aria-live="polite"></div>
        <form id="chat-compose" class="mt-3 d-flex gap-2"><input id="chat-input" class="form-control" maxlength="2000" placeholder="Nhập tin nhắn…" disabled aria-label="Nội dung tin nhắn"/><label class="btn btn-outline-secondary" title="Gửi tệp"><i class="bi bi-paperclip"></i><input id="chat-file" type="file" class="d-none" disabled/></label><button id="chat-send" class="btn btn-primary" disabled>Gửi</button></form>
      </div>
      <div class="card-footer"><button id="chat-to-ticket" class="btn btn-outline-primary btn-sm" disabled>Tạo ticket từ hội thoại</button><span id="chat-ticket-link" class="ms-2"></span></div>
    </div>
  </div>
  <div class="col-12 col-lg-3"><div class="card"><div class="card-header"><h2 class="card-title">Sinh viên</h2></div><div id="chat-student" class="card-body text-secondary">Chọn hội thoại để xem thông tin.</div>
    <div class="card-header"><h2 class="card-title">Ticket gần đây</h2></div><div id="chat-recent-tickets" class="list-group list-group-flush"></div></div></div>
</div>
<div class="modal fade" id="chat-transfer-modal" tabindex="-1" aria-labelledby="chat-transfer-label" aria-hidden="true"><div class="modal-dialog modal-dialog-scrollable"><div class="modal-content"><div class="modal-header"><h3 id="chat-transfer-label" class="modal-title">Chuyển hội thoại</h3><button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button></div><div class="modal-body"><label for="chat-transfer-target" class="form-label">Tư vấn viên nhận</label><select id="chat-transfer-target" class="form-select"></select></div><div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button><button id="chat-transfer-confirm" type="button" class="btn btn-primary">Chuyển</button></div></div></div></div>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.6.1/dist/sockjs.min.js" defer></script>
<script src="https://cdn.jsdelivr.net/npm/@stomp/stompjs@7.0.0/bundles/stomp.umd.min.js" defer></script>
<script src="${pageContext.request.contextPath}/assets/js/manager/chat-desk.js" defer></script>
