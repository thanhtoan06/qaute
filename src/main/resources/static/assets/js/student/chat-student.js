(function (window, document) {
    'use strict';

    const QauteChat = {};

    let currentSession = null;
    let stompClient = null;
    let currentSubscription = null;

    // DOM Elements
    const el = id => document.getElementById(id);

    function setState(state) {
        const states = ['chatStateLoading', 'chatStateStart', 'chatStateBot', 'chatStateQueue', 'chatStateRoom'];
        states.forEach(s => {
            const node = el(s);
            if (node) node.style.display = 'none';
        });

        const badge = el('chatHeaderBadge');

        switch (state) {
            case 'LOADING':
                el('chatStateLoading').style.display = 'block';
                if (badge) badge.textContent = 'Đang tải...';
                break;
            case 'A': // Start form
                el('chatStateStart').style.display = 'block';
                if (badge) {
                    badge.textContent = 'Sẵn sàng';
                    badge.className = 'badge bg-white text-primary';
                }
                break;
            case 'B': // Bot suggestions
                el('chatStateBot').style.display = 'block';
                if (badge) {
                    badge.textContent = 'Trợ lý ảo';
                    badge.className = 'badge bg-azure text-white';
                }
                renderBotView();
                break;
            case 'C': // Queue
                el('chatStateQueue').style.display = 'block';
                if (badge) {
                    badge.textContent = 'Đang chờ';
                    badge.className = 'badge bg-warning text-white';
                }
                renderQueueView();
                break;
            case 'D': // Room
                el('chatStateRoom').style.display = 'block';
                if (badge) {
                    badge.textContent = 'Đang trực tuyến';
                    badge.className = 'badge bg-success text-white';
                }
                renderRoomView();
                break;
        }
    }

    function renderBotView() {
        if (!currentSession) return;
        el('botQuestionText').textContent = currentSession.question || '';
        el('botResolvedSuccess').style.display = 'none';
        el('btnResolved').disabled = false;
        el('btnRequestAgent').disabled = false;

        const list = el('botSuggestionsList');
        const noSugg = el('botNoSuggestions');
        list.replaceChildren();

        const suggestions = currentSession.suggestions || [];
        if (suggestions.length === 0) {
            list.style.display = 'none';
            noSugg.style.display = 'block';
        } else {
            list.style.display = 'block';
            noSugg.style.display = 'none';
            suggestions.slice(0, 3).forEach(item => {
                const a = document.createElement('a');
                const slugOrId = encodeURIComponent(item.slug || item.id);
                a.href = '/faq/' + slugOrId;
                a.target = '_blank';
                a.rel = 'noopener noreferrer';
                a.className = 'list-group-item list-group-item-action d-flex justify-content-between align-items-center py-3';

                const left = document.createElement('div');
                left.className = 'd-flex align-items-center gap-2';
                const icon = document.createElement('i');
                icon.className = 'bi bi-file-earmark-text text-primary fs-3';
                const textWrap = document.createElement('div');
                const title = document.createElement('div');
                title.className = 'fw-medium text-dark';
                title.textContent = item.title || '';
                textWrap.appendChild(title);

                if (item.categoryName) {
                    const cat = document.createElement('div');
                    cat.className = 'small text-secondary';
                    cat.textContent = item.categoryName;
                    textWrap.appendChild(cat);
                }

                left.appendChild(icon);
                left.appendChild(textWrap);

                const right = document.createElement('i');
                right.className = 'bi bi-box-arrow-up-right text-secondary small ms-2';

                a.appendChild(left);
                a.appendChild(right);
                list.appendChild(a);
            });
        }
    }

    function renderQueueView() {
        if (!currentSession) return;

        const isUnavailable = (currentSession.workingNow === false) || (currentSession.status === 'EXPIRED');

        if (isUnavailable) {
            el('queueWaitingView').style.display = 'none';
            el('queueUnavailableView').style.display = 'block';

            if (currentSession.message) {
                el('unavailableMsg').textContent = currentSession.message;
            }

            const catId = currentSession.categoryId || '';
            const q = currentSession.question || '';
            const ticketUrl = '/student/tickets/new?categoryId=' + encodeURIComponent(catId) + '&q=' + encodeURIComponent(q);
            el('btnConvertToTicket').href = ticketUrl;
        } else {
            el('queueUnavailableView').style.display = 'none';
            el('queueWaitingView').style.display = 'block';

            const pos = currentSession.queuePosition != null ? currentSession.queuePosition : 1;
            el('queuePositionNum').textContent = pos;
        }
    }

    function renderRoomView() {
        if (!currentSession) return;
        el('roomAdvisorName').textContent = currentSession.managerName || 'Tư vấn viên';
    }

    // --- STOMP WebSocket Connection ---
    function initStomp() {
        if (!window.StompJs || !window.SockJS) {
            console.warn('StompJs/SockJS chưa sẵn sàng.');
            return;
        }

        stompClient = new StompJs.Client({
            webSocketFactory: () => new SockJS('/ws'),
            connectHeaders: {
                'X-XSRF-TOKEN': Qaute.csrfToken()
            },
            reconnectDelay: 3000,
            onConnect: () => {
                if (currentSession && currentSession.sessionId) {
                    subscribeSession(currentSession.sessionId);
                }
            },
            onStompError: frame => {
                console.error('STOMP error:', frame);
            }
        });

        stompClient.activate();
    }

    function subscribeSession(sessionId) {
        if (!stompClient || !stompClient.connected || !sessionId) return;
        if (currentSubscription) {
            try {
                currentSubscription.unsubscribe();
            } catch (_) {}
            currentSubscription = null;
        }

        currentSubscription = stompClient.subscribe('/topic/chat/' + sessionId, frame => {
            try {
                const event = JSON.parse(frame.body);
                handleEvent(event);
            } catch (err) {
                console.error('Error handling STOMP event', err);
            }
        });
    }

    function unsubscribeSession() {
        if (currentSubscription) {
            try {
                currentSubscription.unsubscribe();
            } catch (_) {}
            currentSubscription = null;
        }
    }

    function handleEvent(event) {
        if (!event || !event.type) return;

        switch (event.type) {
            case 'QUEUE_POSITION_CHANGED':
                if (event.queuePosition != null && currentSession) {
                    currentSession.queuePosition = event.queuePosition;
                    el('queuePositionNum').textContent = event.queuePosition;
                    Qaute.toast('Vị trí hàng chờ của bạn đã cập nhật: #' + event.queuePosition, 'info');
                }
                break;

            case 'SESSION_EXPIRED':
                if (currentSession) {
                    currentSession.status = 'EXPIRED';
                    if (event.message) currentSession.message = event.message;
                }
                setState('C');
                Qaute.toast(event.message || 'Phiên chờ đã hết hạn do không có tư vấn viên tiếp nhận.', 'warning');
                break;

            case 'AGENT_JOINED':
                if (currentSession) {
                    currentSession.status = 'ACTIVE';
                    if (event.managerName) {
                        currentSession.managerName = event.managerName;
                    }
                }
                setState('D');
                Qaute.toast('Tư vấn viên ' + (currentSession?.managerName || '') + ' đã tham gia cuộc trò chuyện!', 'success');
                break;

            case 'SESSION_ENDED':
                Qaute.toast('Phiên tư vấn đã kết thúc.', 'info');
                unsubscribeSession();
                currentSession = null;
                setState('A');
                break;
        }
    }

    // --- API Calls ---
    async function checkCurrentSession() {
        setState('LOADING');
        try {
            const resp = await Qaute.fetchJson('/api/chat/sessions/current');
            const data = resp.data;

            if (data && data.sessionId) {
                currentSession = data;
                if (stompClient && stompClient.connected) {
                    subscribeSession(currentSession.sessionId);
                }

                if (data.status === 'BOT') {
                    setState('B');
                } else if (data.status === 'WAITING' || data.status === 'EXPIRED') {
                    setState('C');
                } else if (data.status === 'ACTIVE') {
                    setState('D');
                } else {
                    currentSession = null;
                    setState('A');
                }
            } else {
                currentSession = null;
                setState('A');
            }
        } catch (error) {
            console.error('Không kiểm tra được phiên hiện tại', error);
            currentSession = null;
            setState('A');
        }
    }

    async function startSession(e) {
        if (e) e.preventDefault();
        const categoryId = el('chatCategoryId').value;
        const question = el('chatQuestion').value.trim();

        if (!categoryId) {
            Qaute.toast('Vui lòng chọn chuyên mục tư vấn.', 'warning');
            el('chatCategoryId').focus();
            return;
        }

        if (question.length < 5) {
            Qaute.toast('Câu hỏi phải có tối thiểu 5 ký tự.', 'warning');
            el('chatQuestion').focus();
            return;
        }

        const btn = el('btnStartChat');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang khởi tạo...';

        try {
            const resp = await Qaute.fetchJson('/api/chat/sessions', {
                method: 'POST',
                body: JSON.stringify({
                    categoryId: Number(categoryId),
                    question: question
                })
            });

            currentSession = resp.data;
            if (stompClient && stompClient.connected) {
                subscribeSession(currentSession.sessionId);
            }

            if (currentSession.status === 'BOT') {
                setState('B');
            } else if (currentSession.status === 'WAITING') {
                setState('C');
            } else if (currentSession.status === 'ACTIVE') {
                setState('D');
            }
        } catch (error) {
            Qaute.toast(error.message || 'Không thể bắt đầu phiên tư vấn.', 'danger');
        } finally {
            btn.disabled = false;
            btn.innerHTML = '<i class="bi bi-send-fill me-1"></i> Bắt đầu tư vấn';
        }
    }

    async function requestAgent() {
        if (!currentSession || !currentSession.sessionId) return;

        const btn = el('btnRequestAgent');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang kết nối tư vấn viên...';

        try {
            const resp = await Qaute.fetchJson('/api/chat/sessions/' + currentSession.sessionId + '/request-agent', {
                method: 'POST'
            });

            currentSession = resp.data;
            if (stompClient && stompClient.connected) {
                subscribeSession(currentSession.sessionId);
            }

            if (currentSession.status === 'WAITING' || currentSession.status === 'EXPIRED') {
                setState('C');
            } else if (currentSession.status === 'ACTIVE') {
                setState('D');
            }
        } catch (error) {
            Qaute.toast(error.message || 'Không thể yêu cầu tư vấn viên.', 'danger');
            btn.disabled = false;
            btn.innerHTML = '<i class="bi bi-headset me-1"></i> Vẫn muốn nói chuyện với tư vấn viên';
        }
    }

    async function cancelSession() {
        if (!currentSession || !currentSession.sessionId) return;

        try {
            await Qaute.fetchJson('/api/chat/sessions/' + currentSession.sessionId + '/cancel', {
                method: 'POST'
            });
            Qaute.toast('Đã hủy phiên tư vấn.', 'info');
        } catch (error) {
            console.warn('Lỗi khi hủy phiên:', error);
        } finally {
            unsubscribeSession();
            currentSession = null;
            setState('A');
            el('chatQuestion').value = '';
            el('chatQuestionCount').textContent = '0 / 500';
        }
    }

    async function resolveSession() {
        if (!currentSession || !currentSession.sessionId) {
            setState('A');
            return;
        }

        try {
            await Qaute.fetchJson('/api/chat/sessions/' + currentSession.sessionId + '/cancel', {
                method: 'POST'
            });
        } catch (_) {}

        unsubscribeSession();
        currentSession = null;
        el('botResolvedSuccess').style.display = 'block';
        el('btnResolved').disabled = true;
        el('btnRequestAgent').disabled = true;
        Qaute.toast('Cảm ơn bạn đã sử dụng hệ thống tư vấn!', 'success');
    }

    // --- Init & Event Listeners ---
    function init() {
        const questionInput = el('chatQuestion');
        if (questionInput) {
            questionInput.addEventListener('input', () => {
                const len = questionInput.value.length;
                el('chatQuestionCount').textContent = len + ' / 500';
            });
        }

        const formStart = el('formStartChat');
        if (formStart) {
            formStart.addEventListener('submit', startSession);
        }

        const btnResolved = el('btnResolved');
        if (btnResolved) {
            btnResolved.addEventListener('click', resolveSession);
        }

        const btnStartNewAfterResolved = el('btnStartNewAfterResolved');
        if (btnStartNewAfterResolved) {
            btnStartNewAfterResolved.addEventListener('click', () => {
                el('chatQuestion').value = '';
                el('chatQuestionCount').textContent = '0 / 500';
                setState('A');
            });
        }

        const btnRequestAgent = el('btnRequestAgent');
        if (btnRequestAgent) {
            btnRequestAgent.addEventListener('click', requestAgent);
        }

        const btnCancelQueue = el('btnCancelQueue');
        if (btnCancelQueue) {
            btnCancelQueue.addEventListener('click', () => {
                if (window.confirm('Bạn có chắc chắn muốn hủy yêu cầu tư vấn này?')) {
                    cancelSession();
                }
            });
        }

        const btnBackToStart = el('btnBackToStart');
        if (btnBackToStart) {
            btnBackToStart.addEventListener('click', () => {
                unsubscribeSession();
                currentSession = null;
                setState('A');
            });
        }

        const btnRoomEndChat = el('btnRoomEndChat');
        if (btnRoomEndChat) {
            btnRoomEndChat.addEventListener('click', () => {
                if (window.confirm('Bạn có chắc chắn muốn rời phòng tư vấn?')) {
                    cancelSession();
                }
            });
        }

        initStomp();
        checkCurrentSession();
    }

    QauteChat.init = init;
    QauteChat.getCurrentSession = () => currentSession;
    QauteChat.setState = setState;

    window.QauteChat = QauteChat;

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})(window, document);

