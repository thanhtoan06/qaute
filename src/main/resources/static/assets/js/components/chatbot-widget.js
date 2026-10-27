// Khởi tạo widget trò chuyện và các thao tác tương tác của người dùng.
(function() {
    var root = document.getElementById('qaute-chatbot-widget');
    var trigger = document.getElementById('qaute-chat-trigger');
    var panel = document.getElementById('qaute-chat-panel');
    var input = document.getElementById('qaute-chat-input');
    var body = document.getElementById('qaute-chat-body');
    if (!trigger || !panel) return;

    var closeButton = root.querySelector('[data-chat-action="close"]');
    if (closeButton) {
        closeButton.addEventListener('click', function() {
            window.toggleQauteChatbot(false);
        });
    }

    root.querySelectorAll('[data-quick-message]').forEach(function(button) {
        button.addEventListener('click', function() {
            window.sendWidgetQuickMessage(button.getAttribute('data-quick-message'));
        });
    });

    var chatForm = document.getElementById('qaute-chat-form');
    if (chatForm) {
        chatForm.addEventListener('submit', function(event) {
            window.handleWidgetChatSubmit(event);
        });
    }

    // Mở hoặc thu nhỏ khung trò chuyện theo trạng thái được yêu cầu.
    window.toggleQauteChatbot = function(forceState) {
        var isVisible = panel.style.display !== 'none';
        var nextState = typeof forceState === 'boolean' ? forceState : !isVisible;
        if (nextState) {
            panel.style.display = 'flex';
            // Đưa tiêu điểm vào ô nhập sau khi mở khung trò chuyện.
            setTimeout(function() {
                input.focus();
            }, 100);
            body.scrollTop = body.scrollHeight;
        } else {
            panel.style.display = 'none';
        }
    };

    // Mở khung trò chuyện từ các điểm gọi bên ngoài widget.
    window.openQauteChatbot = function() {
        window.toggleQauteChatbot(true);
    };

    var isDragging = false;
    var startX, startY;
    var origX, origY;
    var hasMoved = false;

    // Lấy tọa độ con trỏ hoặc điểm chạm hiện tại.
    function getCoord(e) {
        if (e.touches && e.touches.length > 0) {
            return { x: e.touches[0].clientX, y: e.touches[0].clientY };
        }
        return { x: e.clientX, y: e.clientY };
    }

    // Ghi nhận vị trí ban đầu khi bắt đầu kéo nút widget.
    function onDragStart(e) {
        if (e.type === 'mousedown' && e.button !== 0) return;
        var c = getCoord(e);
        startX = c.x;
        startY = c.y;
        var rect = root.getBoundingClientRect();
        origX = rect.left;
        origY = rect.top;
        hasMoved = false;
        isDragging = true;
        document.addEventListener('mousemove', onDragMove, { passive: false });
        document.addEventListener('mouseup', onDragEnd);
        document.addEventListener('touchmove', onDragMove, { passive: false });
        document.addEventListener('touchend', onDragEnd);
    }

    // Cập nhật vị trí widget trong khi người dùng kéo.
    function onDragMove(e) {
        if (!isDragging) return;
        var c = getCoord(e);
        var dx = c.x - startX;
        var dy = c.y - startY;
        if (Math.abs(dx) > 6 || Math.abs(dy) > 6) {
            hasMoved = true;
        }
        if (hasMoved) {
            e.preventDefault();
            var newLeft = origX + dx;
            var newTop = origY + dy;
            var maxLeft = window.innerWidth - root.offsetWidth - 10;
            var maxTop = window.innerHeight - root.offsetHeight - 10;
            newLeft = Math.max(10, Math.min(newLeft, maxLeft));
            newTop = Math.max(10, Math.min(newTop, maxTop));
            root.style.left = newLeft + 'px';
            root.style.top = newTop + 'px';
            root.style.right = 'auto';
            root.style.bottom = 'auto';
            if (newTop < 550) {
                panel.style.bottom = 'auto';
                panel.style.top = '74px';
            } else {
                panel.style.top = 'auto';
                panel.style.bottom = '74px';
            }
            if (newLeft < 380) {
                panel.style.right = 'auto';
                panel.style.left = '0';
            } else {
                panel.style.left = 'auto';
                panel.style.right = '0';
            }
        }
    }

    // Kết thúc thao tác kéo và mở/đóng widget nếu chỉ nhấn.
    function onDragEnd() {
        if (!isDragging) return;
        isDragging = false;
        document.removeEventListener('mousemove', onDragMove);
        document.removeEventListener('mouseup', onDragEnd);
        document.removeEventListener('touchmove', onDragMove);
        document.removeEventListener('touchend', onDragEnd);
        if (!hasMoved) {
            window.toggleQauteChatbot();
        }
    }

    trigger.addEventListener('mousedown', onDragStart);
    trigger.addEventListener('touchstart', onDragStart, { passive: true });

    // Gửi câu hỏi gợi ý qua cùng luồng xử lý biểu mẫu trò chuyện.
    window.sendWidgetQuickMessage = function(text) {
        input.value = text;
        window.handleWidgetChatSubmit(new Event('submit'));
    };

    // Hiển thị câu hỏi và xử lý phản hồi từ dịch vụ trò chuyện.
    window.handleWidgetChatSubmit = function(e) {
        if (e && e.preventDefault) e.preventDefault();
        var text = (input.value || '').trim();
        if (!text) return;
        var now = new Date().toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' });
        var userHtml = '<div class="qaute-msg-row qaute-msg-user">' +
            '<div class="qaute-bubble qaute-bubble-user">' +
            escapeHtml(text) +
            '<div class="qaute-msg-time">' + now + '</div></div></div>';
        body.insertAdjacentHTML('beforeend', userHtml);
        input.value = '';
        body.scrollTop = body.scrollHeight;

        var typingId = 'qaute-typing-' + Date.now();
        var typingHtml = '<div id="' + typingId + '" class="qaute-msg-row qaute-msg-bot">' +
            '<div class="qaute-bot-avatar"><img src="/assets/images/chatbot-ute.png" alt="Bot" data-fallback-src="/assets/images/hcmute-logo.png"></div>' +
            '<div class="qaute-typing"><span class="qaute-typing-dot"></span><span class="qaute-typing-dot"></span><span class="qaute-typing-dot"></span></div>' +
            '</div>';
        body.insertAdjacentHTML('beforeend', typingHtml);
        body.scrollTop = body.scrollHeight;

        fetch('/api/public/chat/message', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ message: text })
        })
        // Chuyển phản hồi HTTP thành dữ liệu JSON.
        .then(function(res) {
            return res.json();
        })
        // Thay chỉ báo đang nhập bằng nội dung trả lời.
        .then(function(res) {
            var el = document.getElementById(typingId);
            if (el) el.remove();
            var reply = res && res.data && res.data.reply ? res.data.reply : 'QAUTE đã ghi nhận câu hỏi của bạn. Nếu bạn cần hỗ trợ chi tiết hơn, vui lòng gửi yêu cầu hỗ trợ (Ticket).';
            var botHtml = '<div class="qaute-msg-row qaute-msg-bot">' +
                '<div class="qaute-bot-avatar"><img src="/assets/images/chatbot-ute.png" alt="Bot" data-fallback-src="/assets/images/hcmute-logo.png"></div>' +
                '<div class="qaute-bubble qaute-bubble-bot">' +
                reply +
                '<div class="qaute-msg-time">' + now + '</div></div></div>';
            body.insertAdjacentHTML('beforeend', botHtml);
            body.scrollTop = body.scrollHeight;
        })
        // Hiển thị thông báo nếu yêu cầu trò chuyện gặp lỗi.
        .catch(function() {
            var el = document.getElementById(typingId);
            if (el) el.remove();
            var errHtml = '<div class="qaute-msg-row qaute-msg-bot">' +
                '<div class="qaute-bot-avatar"><img src="/assets/images/chatbot-ute.png" alt="Bot"></div>' +
                '<div class="qaute-bubble qaute-bubble-bot text-danger">Có lỗi kết nối tạm thời. Vui lòng thử lại sau giây lát.' +
                '<div class="qaute-msg-time">' + now + '</div></div></div>';
            body.insertAdjacentHTML('beforeend', errHtml);
            body.scrollTop = body.scrollHeight;
        });
    };

    // Mã hóa văn bản người dùng trước khi đưa vào nội dung HTML.
    function escapeHtml(str) {
        var p = document.createElement('p');
        p.textContent = str;
        return p.innerHTML;
    }
})();