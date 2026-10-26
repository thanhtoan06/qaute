// Khởi tạo chuông và cập nhật thông báo cho người dùng.
(function (window, document) {
    'use strict';

    let unreadCount = 0;
    let isLoaded = false;
    let needsReload = true;

    // Lấy phần tử hiển thị số thông báo chưa đọc.
    function getBadgeElement() {
        return document.getElementById('notification-badge');
    }

    function renderBadge(badge, count) {
        if (!badge) {
            return;
        }
        if (count <= 0) {
            badge.classList.add('d-none');
            badge.textContent = '0';
        } else {
            badge.classList.remove('d-none');
            badge.textContent = count > 99 ? '99+' : String(count);
        }
    }

    // Cập nhật số lượng và trạng thái hiển thị của huy hiệu.
    function updateBadge(count) {
        unreadCount = Math.max(0, count || 0);
        renderBadge(getBadgeElement(), unreadCount);
        document.querySelectorAll('[data-sidebar-notification-badge]').forEach(function (badge) {
            renderBadge(badge, unreadCount);
        });
    }

    // Lấy số thông báo chưa đọc từ máy chủ.
    function fetchUnreadCount() {
        if (!window.Qaute || !window.Qaute.fetchJson) {
            return;
        }
        window.Qaute.fetchJson('/api/notifications/unread-count')
            // Đồng bộ số chưa đọc nhận được với huy hiệu.
            .then(function (res) {
                let count = 0;
                if (res && res.data !== undefined && res.data !== null) {
                    if (typeof res.data === 'number') {
                        count = res.data;
                    } else if (typeof res.data === 'object') {
                        count = res.data.unreadCount !== undefined ? res.data.unreadCount : (res.data.count || 0);
                    }
                }
                updateBadge(count);
            })
            // Bỏ qua lỗi của tác vụ thăm dò nền.
            .catch(function () {
            });
    }

    // Tải danh sách thông báo gần đây vào trình đơn.
    function loadRecentNotifications() {
        const list = document.getElementById('notification-list');
        const empty = document.getElementById('notification-empty');
        const loading = document.getElementById('notification-loading');

        if (loading) {
            loading.style.display = 'block';
        }
        if (empty) {
            empty.style.display = 'none';
        }

        window.Qaute.fetchJson('/api/notifications?page=0&size=8')
            // Lưu trạng thái đã tải và hiển thị các thông báo.
            .then(function (res) {
                isLoaded = true;
                needsReload = false;
                const items = (res && res.data && Array.isArray(res.data.content))
                    ? res.data.content
                    : (res && Array.isArray(res.data) ? res.data : []);
                renderNotifications(items);
            })
            // Ẩn trạng thái tải và ghi lỗi khi yêu cầu thất bại.
            .catch(function (err) {
                if (loading) {
                    loading.style.display = 'none';
                }
                console.error('Error loading notifications', err);
            });
    }

    // Thay thế danh sách hiện tại bằng các thông báo mới.
    function renderNotifications(items) {
        const list = document.getElementById('notification-list');
        const empty = document.getElementById('notification-empty');
        const loading = document.getElementById('notification-loading');
        if (!list) {
            return;
        }

        if (loading) {
            loading.style.display = 'none';
        }

        const existingItems = list.querySelectorAll('.notification-item');
        existingItems.forEach(function (el) {
            el.remove();
        });

        if (!items || items.length === 0) {
            if (empty) {
                empty.style.display = 'block';
            }
            return;
        }
        if (empty) {
            empty.style.display = 'none';
        }

        // Tạo và thêm từng thông báo vào danh sách.
        items.forEach(function (item) {
            const el = createNotificationElement(item);
            list.appendChild(el);
        });
    }

    // Tạo phần tử giao diện cho một thông báo.
    function createNotificationElement(item) {
        const div = document.createElement('div');
        div.className = 'list-group-item list-group-item-action py-2 px-3 notification-item position-relative border-bottom' +
            (item.read ? ' bg-white' : ' bg-light text-dark');
        div.setAttribute('data-id', item.id);
        div.style.cursor = 'pointer';

        const titleHtml = window.Qaute.escapeHtml(item.title || '');
        const bodyHtml = window.Qaute.escapeHtml(item.body || '');
        const timeHtml = item.createdAt ? window.Qaute.formatDateTime(item.createdAt) : '';
        const unreadDot = !item.read
            ? '<span class="badge bg-primary p-1 rounded-circle me-1 d-inline-block align-middle notif-dot" style="width: 7px; height: 7px;"></span>'
            : '';

        div.innerHTML =
            '<div class="d-flex justify-content-between align-items-start">' +
                '<div class="me-2 flex-grow-1 overflow-hidden" style="font-size: 0.85rem;">' +
                    '<div class="fw-semibold text-truncate mb-1">' + unreadDot + titleHtml + '</div>' +
                    (bodyHtml ? '<div class="text-muted small text-truncate mb-1">' + bodyHtml + '</div>' : '') +
                    '<div class="text-secondary" style="font-size: 0.75rem;">' + timeHtml + '</div>' +
                '</div>' +
                '<button type="button" class="btn btn-sm text-secondary btn-hide-notif p-0 ms-1 border-0 bg-transparent" title="Ẩn thông báo" style="font-size: 0.85rem; line-height: 1;">' +
                    '<i class="bi bi-trash"></i>' +
                '</button>' +
            '</div>';

        const btnHide = div.querySelector('.btn-hide-notif');
        if (btnHide) {
            // Ẩn thông báo mà không kích hoạt điều hướng của mục.
            btnHide.addEventListener('click', function (e) {
                e.stopPropagation();
                hideNotification(item.id, div, !item.read);
            });
        }

        // Đánh dấu thông báo đã đọc và mở liên kết hợp lệ.
        div.addEventListener('click', function () {
            handleItemClick(item, div);
        });

        return div;
    }

    // Đánh dấu mục đã đọc rồi điều hướng nếu có liên kết hợp lệ.
    function handleItemClick(item, element) {
        const link = item.linkUrl ? String(item.linkUrl).trim() : '';
        const hasValidLink = link.startsWith('/') && !link.startsWith('//');

        if (!item.read) {
            window.Qaute.fetchJson('/api/notifications/' + item.id + '/read', { method: 'POST' })
                // Cập nhật giao diện sau khi máy chủ đánh dấu đã đọc.
                .then(function () {
                    item.read = true;
                    element.classList.remove('bg-light');
                    element.classList.add('bg-white');
                    const dot = element.querySelector('.notif-dot');
                    if (dot) {
                        dot.remove();
                    }
                    updateBadge(unreadCount - 1);
                    if (hasValidLink) {
                        window.location.href = link;
                    }
                })
                // Vẫn mở liên kết hợp lệ nếu cập nhật đã đọc thất bại.
                .catch(function (err) {
                    console.error('Error marking notification as read', err);
                    if (hasValidLink) {
                        window.location.href = link;
                    }
                });
        } else {
            if (hasValidLink) {
                window.location.href = link;
            }
        }
    }

    // Xóa thông báo khỏi máy chủ và cập nhật danh sách.
    function hideNotification(id, element, wasUnread) {
        window.Qaute.fetchJson('/api/notifications/' + id, { method: 'DELETE' })
            // Gỡ mục đã xóa và điều chỉnh trạng thái danh sách.
            .then(function () {
                element.remove();
                if (wasUnread) {
                    updateBadge(unreadCount - 1);
                }
                const list = document.getElementById('notification-list');
                if (list && list.querySelectorAll('.notification-item').length === 0) {
                    const empty = document.getElementById('notification-empty');
                    if (empty) {
                        empty.style.display = 'block';
                    }
                }
            })
            // Báo cho người dùng nếu không thể ẩn thông báo.
            .catch(function () {
                if (window.Qaute && window.Qaute.toast) {
                    window.Qaute.toast('Không thể ẩn thông báo', 'danger');
                }
            });
    }

    // Đánh dấu toàn bộ thông báo là đã đọc.
    function markAllRead() {
        window.Qaute.fetchJson('/api/notifications/read-all', { method: 'POST' })
            // Đồng bộ toàn bộ mục đang hiển thị sau khi xác nhận.
            .then(function () {
                updateBadge(0);
                const list = document.getElementById('notification-list');
                if (list) {
                    // Cập nhật trạng thái đã đọc của từng mục hiển thị.
                    list.querySelectorAll('.notification-item').forEach(function (el) {
                        el.classList.remove('bg-light');
                        el.classList.add('bg-white');
                        const dot = el.querySelector('.notif-dot');
                        if (dot) {
                            dot.remove();
                        }
                    });
                }
            })
            // Báo lỗi nếu yêu cầu đánh dấu đã đọc thất bại.
            .catch(function () {
                if (window.Qaute && window.Qaute.toast) {
                    window.Qaute.toast('Có lỗi khi đánh dấu đã đọc', 'danger');
                }
            });
    }

    // Kết nối WebSocket nếu thư viện cần thiết đã được nạp.
    function setupWebSocket() {
        const hasSockJS = typeof window.SockJS === 'function';
        const hasStompJs = typeof window.StompJs !== 'undefined' && window.StompJs.Client;
        const hasStomp = typeof window.Stomp !== 'undefined';

        if (!hasSockJS || (!hasStompJs && !hasStomp)) {
            return;
        }

        // Cập nhật huy hiệu và danh sách khi nhận thông báo đẩy.
        function onNotificationReceived(body) {
            try {
                const data = typeof body === 'string' ? JSON.parse(body) : body;
                unreadCount++;
                updateBadge(unreadCount);
                needsReload = true;
                const msg = (data.title ? data.title + ': ' : '') + (data.body || 'Bạn có thông báo mới');
                if (window.Qaute && window.Qaute.toast) {
                    window.Qaute.toast(msg, 'info');
                }
            } catch (e) {
                console.error('Failed to parse push notification', e);
            }
        }

        if (hasStompJs) {
            try {
                const headers = {};
                const csrfToken = window.Qaute ? window.Qaute.csrfToken() : '';
                if (csrfToken) {
                    headers[window.Qaute.csrfHeader()] = csrfToken;
                }
                const stompClient = new window.StompJs.Client({
                    // Tạo kết nối WebSocket tương thích SockJS.
                    webSocketFactory: function () {
                        return new window.SockJS('/ws');
                    },
                    connectHeaders: headers,
                    reconnectDelay: 10000,
                    beforeConnect: function () {
                        return window.Qaute.refreshSessionIfNeeded();
                    },
                    // Đăng ký nhận thông báo sau khi kết nối thành công.
                    onConnect: function () {
                        // Chuyển thông báo nhận được cho bộ xử lý chung.
                        stompClient.subscribe('/user/queue/notifications', function (message) {
                            onNotificationReceived(message.body);
                        });
                    },
                    onStompError: function () {
                    },
                    onWebSocketError: function () {
                    }
                });
                stompClient.activate();
            } catch (err) {
            }
        } else if (hasStomp) {
            try {
                const socket = new window.SockJS('/ws');
                const stompClient = window.Stomp.over(socket);
                stompClient.debug = function () {};
                const headers = {};
                const csrfToken = window.Qaute ? window.Qaute.csrfToken() : '';
                if (csrfToken) {
                    headers[window.Qaute.csrfHeader()] = csrfToken;
                }
                // Đăng ký nhận thông báo sau khi kết nối thành công.
                stompClient.connect(headers, function () {
                    // Chuyển thông báo nhận được cho bộ xử lý chung.
                    stompClient.subscribe('/user/queue/notifications', function (message) {
                        onNotificationReceived(message.body);
                    });
                }, function () {
                });
            } catch (err) {
            }
        }
    }

    // Gắn sự kiện chuông và khởi chạy các tác vụ cập nhật.
    function init() {
        const bellBtn = document.getElementById('notification-bell-btn');
        const bellDropdown = document.getElementById('notification-bell-dropdown');
        if (!bellBtn && !bellDropdown) {
            return;
        }

        // 1. Initial unread count fetch
        fetchUnreadCount();

        // 2. Open dropdown loads notifications
        const trigger = bellDropdown || bellBtn;
        // Tải lại danh sách khi mở trình đơn thông báo.
        trigger.addEventListener('show.bs.dropdown', function () {
            if (!isLoaded || needsReload) {
                loadRecentNotifications();
            }
        });

        // 3. Mark all as read button
        const btnMarkAll = document.getElementById('btn-mark-all-read');
        if (btnMarkAll) {
            // Đánh dấu tất cả thông báo đã đọc khi nhấn nút.
            btnMarkAll.addEventListener('click', function (e) {
                e.preventDefault();
                e.stopPropagation();
                markAllRead();
            });
        }

        // 4. Polling fallback every 60s
        window.setInterval(fetchUnreadCount, 60000);

        // 5. Connect WebSocket if library is available
        setupWebSocket();
    }

    if (document.readyState === 'loading') {
        // Khởi chạy sau khi tài liệu được phân tích xong.
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})(window, document);
