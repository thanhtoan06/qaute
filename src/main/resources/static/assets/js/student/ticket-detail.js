(function (window, document) {
    'use strict';

    // Tạo tiêu đề CSRF cho các kết nối thời gian thực.
    function csrfHeaders() {
        const token = document.querySelector('meta[name="_csrf"]')?.content;
        const header = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-XSRF-TOKEN';
        return token ? { [header]: token } : {};
    }

    // Hiển thị thông báo tải lại khi vé được cập nhật.
    function showReloadToast() {
        if (document.getElementById('ticket-update-toast')) return;
        const toast = document.createElement('div');
        toast.id = 'ticket-update-toast';
        toast.className = 'alert alert-info shadow position-fixed bottom-0 end-0 m-3 d-flex align-items-center gap-3';
        toast.setAttribute('role', 'status');
        toast.innerHTML = '<span>Yêu cầu có cập nhật mới</span><button type="button" class="btn btn-sm btn-primary">Tải lại</button>';
        toast.querySelector('button').addEventListener('click', function () { window.location.reload(); });
        document.body.appendChild(toast);
    }

    // Khởi tạo trình soạn thảo nội dung phản hồi.
    function initEditor() {
        const textarea = document.querySelector('#ticket-reply-content');
        if (!textarea || !window.Qaute?.initRichText) return;
        window.Qaute.initRichText('#ticket-reply-content', { height: 180, placeholder: 'Nhập phản hồi của bạn...' });
        const form = textarea.closest('form');
        // Chặn gửi biểu mẫu khi tệp đính kèm vẫn đang tải lên.
        form?.addEventListener('submit', function (event) {
            const uploader = window.ticketReplyUploader;
            if (uploader?.isUploading()) {
                event.preventDefault();
                window.Qaute?.toast?.('Vui lòng đợi tệp tải lên hoàn tất.', 'warning');
                return;
            }
            if (window.jQuery?.fn?.summernote) textarea.value = window.jQuery(textarea).summernote('code');
        });
    }

    // Khởi tạo bộ tải tệp đính kèm của vé.
    function initUploader() {
        const container = document.querySelector('#ticket-reply-uploader');
        if (container && window.Qaute?.initUploader) {
            window.ticketReplyUploader = window.Qaute.initUploader(container, 'TICKET_ATTACHMENT', 5);
        }
    }

    function moveModalsToBody() {
        document.querySelectorAll('.stu-main .modal').forEach(function (modal) {
            document.body.appendChild(modal);
        });
    }

    // Kết nối kênh thời gian thực để nhận thông báo cập nhật vé.
    function initRealtime() {
        const ticketId = document.querySelector('[data-ticket-id]')?.dataset.ticketId;
        if (!ticketId || typeof window.SockJS !== 'function' || !window.StompJs?.Client) return;
        const headers = csrfHeaders();
        const client = new window.StompJs.Client({
            webSocketFactory: function () { return new window.SockJS('/ws'); },
            connectHeaders: headers,
            reconnectDelay: 5000,
            beforeConnect: async function () {
                await window.Qaute.refreshSessionIfNeeded();
            }
        });
        // Đăng ký kênh của vé sau khi kết nối thành công.
        client.onConnect = function () {
            client.subscribe('/topic/ticket/' + ticketId, showReloadToast, headers);
        };
        client.onStompError = function () {};
        client.activate();
    }

    // Khởi tạo trình soạn thảo, bộ tải tệp, vị trí modal và kết nối thời gian thực.
    document.addEventListener('DOMContentLoaded', function () {
        initEditor();
        initUploader();
        moveModalsToBody();
        initRealtime();
    });
})(window, document);
