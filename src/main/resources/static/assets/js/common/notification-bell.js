(function (window, document) {
    'use strict';

    let unreadCount = 0;
    let isLoaded = false;
    let needsReload = true;

    function getBadgeElement() {
        return document.getElementById('notification-badge');
    }

    function updateBadge(count) {
        unreadCount = Math.max(0, count || 0);
        const badge = getBadgeElement();
        if (!badge) {
            return;
        }
        if (unreadCount <= 0) {
            badge.classList.add('d-none');
            badge.textContent = '0';
        } else if (unreadCount > 99) {
            badge.classList.remove('d-none');
            badge.textContent = '99+';
        } else {
            badge.classList.remove('d-none');
            badge.textContent = String(unreadCount);
        }
    }

    function fetchUnreadCount() {
        if (!window.Qaute || !window.Qaute.fetchJson) {
            return;
        }
        window.Qaute.fetchJson('/api/notifications/unread-count')
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
            .catch(function () {
                // Silent catch for background polling
            });
    }

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
            .then(function (res) {
                isLoaded = true;
                needsReload = false;
                const items = (res && res.data && Array.isArray(res.data.content))
                    ? res.data.content
                    : (res && Array.isArray(res.data) ? res.data : []);
                renderNotifications(items);
            })
            .catch(function (err) {
                if (loading) {
                    loading.style.display = 'none';
                }
                console.error('Error loading notifications', err);
            });
    }

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

        items.forEach(function (item) {
            const el = createNotificationElement(item);
            list.appendChild(el);
        });
    }

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
            btnHide.addEventListener('click', function (e) {
                e.stopPropagation();
                hideNotification(item.id, div, !item.read);
            });
        }

        div.addEventListener('click', function () {
            handleItemClick(item, div);
        });

        return div;
    }

    function handleItemClick(item, element) {
        const link = item.linkUrl ? String(item.linkUrl).trim() : '';
        const hasValidLink = link.startsWith('/') && !link.startsWith('//');

        if (!item.read) {
            window.Qaute.fetchJson('/api/notifications/' + item.id + '/read', { method: 'POST' })
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

    function hideNotification(id, element, wasUnread) {
        window.Qaute.fetchJson('/api/notifications/' + id, { method: 'DELETE' })
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
            .catch(function () {
                if (window.Qaute && window.Qaute.toast) {
                    window.Qaute.toast('Không thể ẩn thông báo', 'danger');
                }
            });
    }

    function markAllRead() {
        window.Qaute.fetchJson('/api/notifications/read-all', { method: 'POST' })
            .then(function () {
                updateBadge(0);
                const list = document.getElementById('notification-list');
                if (list) {
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
            .catch(function () {
                if (window.Qaute && window.Qaute.toast) {
                    window.Qaute.toast('Có lỗi khi đánh dấu đã đọc', 'danger');
                }
            });
    }

    function setupWebSocket() {
        const hasSockJS = typeof window.SockJS === 'function';
        const hasStompJs = typeof window.StompJs !== 'undefined' && window.StompJs.Client;
        const hasStomp = typeof window.Stomp !== 'undefined';

        if (!hasSockJS || (!hasStompJs && !hasStomp)) {
            return;
        }

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
                    webSocketFactory: function () {
                        return new window.SockJS('/ws');
                    },
                    connectHeaders: headers,
                    reconnectDelay: 10000,
                    onConnect: function () {
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
                stompClient.connect(headers, function () {
                    stompClient.subscribe('/user/queue/notifications', function (message) {
                        onNotificationReceived(message.body);
                    });
                }, function () {
                });
            } catch (err) {
            }
        }
    }

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
        trigger.addEventListener('show.bs.dropdown', function () {
            if (!isLoaded || needsReload) {
                loadRecentNotifications();
            }
        });

        // 3. Mark all as read button
        const btnMarkAll = document.getElementById('btn-mark-all-read');
        if (btnMarkAll) {
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
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})(window, document);

