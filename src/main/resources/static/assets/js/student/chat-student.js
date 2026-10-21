(function (window, document) {
    'use strict';

    const QauteChat = {};

    // State
    const state = {
        currentSession: null,
        messages: [],
        lastMessageId: 0,
        lastReadMarkedId: 0,
        pendingFile: null,
        pendingFileAssetId: null,
        selectedRating: 0,
        sendTimestamps: [],
        typingTimer: null,
        isTypingActive: false,
        historyPage: 0
    };

    let stompClient = null;
    let currentSubscription = null;

    // Helper DOM accessor
    const el = id => document.getElementById(id);

    // --- State Switching ---
    function setState(viewState) {
        const states = ['chatStateLoading', 'chatStateStart', 'chatStateBot', 'chatStateQueue', 'chatStateRoom'];
        states.forEach(s => {
            const node = el(s);
            if (node) node.style.display = 'none';
        });

        const badge = el('chatHeaderBadge');

        switch (viewState) {
            case 'LOADING':
                if (el('chatStateLoading')) el('chatStateLoading').style.display = 'block';
                if (badge) badge.textContent = 'Đang tải...';
                break;
            case 'A': // Start form
                if (el('chatStateStart')) el('chatStateStart').style.display = 'block';
                if (badge) {
                    badge.textContent = 'Sẵn sàng';
                    badge.className = 'badge bg-white text-primary';
                }
                break;
            case 'B': // Bot suggestions
                if (el('chatStateBot')) el('chatStateBot').style.display = 'block';
                if (badge) {
                    badge.textContent = 'Trợ lý ảo';
                    badge.className = 'badge bg-azure text-white';
                }
                renderBotView();
                break;
            case 'C': // Queue
                if (el('chatStateQueue')) el('chatStateQueue').style.display = 'block';
                if (badge) {
                    badge.textContent = 'Đang chờ';
                    badge.className = 'badge bg-warning text-white';
                }
                renderQueueView();
                break;
            case 'D': // Room
                if (el('chatStateRoom')) el('chatStateRoom').style.display = 'flex';
                if (badge) {
                    badge.textContent = state.currentSession?.status === 'ENDED' ? 'Đã kết thúc' : 'Đang trực tuyến';
                    badge.className = state.currentSession?.status === 'ENDED' ? 'badge bg-secondary text-white' : 'badge bg-success text-white';
                }
                renderRoomView();
                break;
        }
    }

    // --- State B: Bot Suggestions ---
    function renderBotView() {
        if (!state.currentSession) return;
        el('botQuestionText').textContent = state.currentSession.question || '';
        el('botResolvedSuccess').style.display = 'none';
        el('btnResolved').disabled = false;
        el('btnRequestAgent').disabled = false;

        const list = el('botSuggestionsList');
        const noSugg = el('botNoSuggestions');
        list.replaceChildren();

        const suggestions = state.currentSession.suggestions || [];
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

    // --- State C: Queue View ---
    function renderQueueView() {
        if (!state.currentSession) return;

        const isUnavailable = (state.currentSession.workingNow === false) || (state.currentSession.status === 'EXPIRED');

        if (isUnavailable) {
            el('queueWaitingView').style.display = 'none';
            el('queueUnavailableView').style.display = 'block';

            if (state.currentSession.message) {
                el('unavailableMsg').textContent = state.currentSession.message;
            }

            const catId = state.currentSession.categoryId || '';
            const q = state.currentSession.question || '';
            const ticketUrl = '/student/tickets/new?categoryId=' + encodeURIComponent(catId) + '&q=' + encodeURIComponent(q);
            el('btnConvertToTicket').href = ticketUrl;
        } else {
            el('queueUnavailableView').style.display = 'none';
            el('queueWaitingView').style.display = 'block';

            const pos = state.currentSession.queuePosition != null ? state.currentSession.queuePosition : 1;
            el('queuePositionNum').textContent = pos;
        }
    }

    // --- State D: Room View ---
    function renderRoomView() {
        if (!state.currentSession) return;
        el('roomAdvisorName').textContent = state.currentSession.managerName || 'Tư vấn viên';

        if (state.currentSession.status === 'ENDED') {
            onSessionEnded();
        } else {
            el('chatInputArea').style.display = 'block';
            el('chatPostSessionBlock').style.display = 'none';
            el('btnRoomEndChat').style.display = 'inline-block';
        }

        // Initial sync of room messages
        syncMessages(0);
    }

    function renderMessages() {
        const container = el('chatMessagesList');
        if (!container) return;
        container.replaceChildren();

        state.messages.forEach(msg => {
            const row = document.createElement('div');
            const isMe = msg.senderType === 'STUDENT';
            const isSystem = msg.senderType === 'SYSTEM';

            row.className = 'qa-chat-row ' + (isMe ? 'qa-chat-me' : isSystem ? 'qa-chat-system' : 'qa-chat-other');

            const bubble = document.createElement('div');
            bubble.className = 'qa-chat-bubble';

            if (!isMe && !isSystem) {
                const sender = document.createElement('span');
                sender.className = 'qa-chat-sender';
                sender.textContent = msg.senderName || 'Tư vấn viên';
                bubble.appendChild(sender);
            }

            // Message content / Attachment
            if (msg.assetId) {
                const attachLink = document.createElement('a');
                attachLink.href = '/api/media/' + encodeURIComponent(msg.assetId) + '/download';
                attachLink.className = 'qa-chat-attachment';
                attachLink.target = '_blank';
                attachLink.rel = 'noopener noreferrer';

                const attachIcon = document.createElement('i');
                attachIcon.className = 'bi bi-paperclip fs-4';
                const attachName = document.createElement('span');
                attachName.className = 'text-truncate';
                attachName.textContent = msg.content || 'Tệp đính kèm';

                attachLink.appendChild(attachIcon);
                attachLink.appendChild(attachName);
                bubble.appendChild(attachLink);
            } else {
                const textDiv = document.createElement('div');
                textDiv.className = 'text-break';
                textDiv.textContent = msg.content || '';
                bubble.appendChild(textDiv);
            }

            // Message metadata: time & status (for non-system)
            if (!isSystem) {
                const meta = document.createElement('div');
                meta.className = 'qa-chat-meta';

                const timeSpan = document.createElement('span');
                timeSpan.textContent = formatTime(msg.sentAt);
                meta.appendChild(timeSpan);

                if (isMe) {
                    const statusSpan = document.createElement('span');
                    statusSpan.className = 'qa-chat-status';

                    if (!msg.id) {
                        statusSpan.innerHTML = '<i class="bi bi-clock small" title="Đang gửi..."></i>';
                    } else if (msg.readAt) {
                        statusSpan.innerHTML = '<i class="bi bi-check2-all text-white fw-bold" title="Đã xem"></i>';
                    } else {
                        statusSpan.innerHTML = '<i class="bi bi-check2" title="Đã gửi"></i>';
                    }
                    meta.appendChild(statusSpan);
                }

                bubble.appendChild(meta);
            }

            row.appendChild(bubble);
            container.appendChild(row);
        });

        scrollToBottom();
    }

    function addOrUpdateMessage(msg) {
        if (!msg) return;
        const idx = state.messages.findIndex(m =>
            (msg.id && m.id === msg.id) ||
            (msg.clientMessageId && m.clientMessageId === msg.clientMessageId)
        );

        if (idx >= 0) {
            state.messages[idx] = Object.assign({}, state.messages[idx], msg);
        } else {
            state.messages.push(msg);
        }

        state.messages.sort((a, b) => (a.id || Infinity) - (b.id || Infinity));
        if (msg.id) {
            state.lastMessageId = Math.max(state.lastMessageId, msg.id);
        }

        renderMessages();
        markRead();
    }

    function scrollToBottom(force) {
        const c = el('chatMessagesContainer');
        if (!c) return;
        const isNearBottom = c.scrollHeight - c.scrollTop - c.clientHeight < 150;
        if (force || isNearBottom) {
            c.scrollTop = c.scrollHeight;
        }
    }

    function formatTime(isoString) {
        if (!isoString) return '';
        try {
            const date = new Date(isoString);
            return date.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit', hour12: false });
        } catch (_) {
            return '';
        }
    }

    // --- Message Sending & Rate Limit ---
    async function sendMessage() {
        if (!state.currentSession || !state.currentSession.sessionId) return;
        if (state.currentSession.status === 'ENDED') return;

        const input = el('roomMessageInput');
        const content = input ? input.value.trim() : '';

        if (!content && !state.pendingFile) return;

        // Rate limit: max 20 messages per minute
        const now = Date.now();
        state.sendTimestamps = state.sendTimestamps.filter(ts => now - ts < 60000);
        if (state.sendTimestamps.length >= 20) {
            Qaute.toast('Bạn đang gửi tin quá nhanh (tối đa 20 tin/phút). Vui lòng đợi trong giây lát.', 'warning');
            return;
        }

        if (content.length > 1000) {
            Qaute.toast('Tin nhắn không được vượt quá 1000 ký tự.', 'warning');
            return;
        }

        const clientMessageId = crypto.randomUUID();
        let assetId = null;

        // Upload attachment if any
        if (state.pendingFile) {
            try {
                const formData = new FormData();
                formData.append('file', state.pendingFile);
                formData.append('purpose', 'CHAT_ATTACHMENT');

                const uploadResp = await fetch('/api/media/upload', {
                    method: 'POST',
                    headers: {
                        [Qaute.csrfHeader()]: Qaute.csrfToken()
                    },
                    body: formData
                });
                const body = await uploadResp.json();
                if (!uploadResp.ok || !body.success) {
                    throw new Error(body.message || 'Không thể tải tệp lên.');
                }
                assetId = body.data.id;
            } catch (err) {
                Qaute.toast(err.message || 'Lỗi tải tệp đính kèm.', 'danger');
                return;
            } finally {
                removeAttachment();
            }
        }

        state.sendTimestamps.push(now);

        const textToSend = content || (assetId ? 'Tệp đính kèm' : '');

        // Optimistic UI display
        const tempMsg = {
            id: null,
            sessionId: state.currentSession.sessionId,
            senderType: 'STUDENT',
            senderName: 'Bạn',
            content: textToSend,
            contentType: assetId ? 'ATTACHMENT' : 'TEXT',
            assetId: assetId,
            clientMessageId: clientMessageId,
            sentAt: new Date().toISOString(),
            readAt: null
        };
        addOrUpdateMessage(tempMsg);
        scrollToBottom(true);

        if (input) {
            input.value = '';
            input.style.height = 'auto';
            el('chatInputCharCount').textContent = '0 / 1000';
        }

        // Send via STOMP
        if (!stompClient || !stompClient.connected) {
            Qaute.toast('Mất kết nối với máy chủ chat. Đang thử kết nối lại...', 'warning');
            return;
        }

        stompClient.publish({
            destination: '/app/chat.send',
            body: JSON.stringify({
                sessionId: state.currentSession.sessionId,
                clientMessageId: clientMessageId,
                content: textToSend,
                assetId: assetId
            })
        });

        // Trigger typing off
        sendTyping(false);
    }

    // --- Typing Handler ---
    function handleTypingInput() {
        if (!state.currentSession || state.currentSession.status === 'ENDED') return;

        if (!state.isTypingActive) {
            sendTyping(true);
            state.isTypingActive = true;
        }

        clearTimeout(state.typingTimer);
        state.typingTimer = setTimeout(() => {
            sendTyping(false);
            state.isTypingActive = false;
        }, 1500);
    }

    function sendTyping(isTyping) {
        if (!stompClient || !stompClient.connected || !state.currentSession) return;
        stompClient.publish({
            destination: '/app/chat.typing',
            body: JSON.stringify({
                sessionId: state.currentSession.sessionId,
                isTyping: isTyping
            })
        });
    }

    // --- Read Receipt Handler ---
    function markRead() {
        if (!state.currentSession || !stompClient || !stompClient.connected) return;
        if (document.hidden || !document.hasFocus()) return;

        const lastOther = state.messages
            .filter(m => m.id && (m.senderType === 'MANAGER' || m.senderType === 'BOT'))
            .at(-1);

        if (lastOther && lastOther.id > state.lastReadMarkedId) {
            stompClient.publish({
                destination: '/app/chat.read',
                body: JSON.stringify({
                    sessionId: state.currentSession.sessionId,
                    lastReadMessageId: lastOther.id
                })
            });
            state.lastReadMarkedId = lastOther.id;
        }
    }

    // --- Attachment Handler ---
    function handleFileSelected(event) {
        const file = event.target.files[0];
        if (!file) return;

        if (file.size > 20 * 1024 * 1024) {
            Qaute.toast('Tệp đính kèm không được vượt quá 20MB.', 'warning');
            event.target.value = '';
            return;
        }

        state.pendingFile = file;
        el('chatAttachFileName').textContent = file.name;
        el('chatAttachFileSize').textContent = (file.size / 1024 > 1024)
            ? (file.size / (1024 * 1024)).toFixed(1) + ' MB'
            : Math.round(file.size / 1024) + ' KB';
        el('chatAttachPreview').style.display = 'flex';
    }

    function removeAttachment() {
        state.pendingFile = null;
        el('chatFileInput').value = '';
        el('chatAttachPreview').style.display = 'none';
    }

    // --- STOMP Connection & Subscriptions ---
    function initStomp() {
        if (!window.StompJs || !window.SockJS) {
            console.warn('Thư viện StompJs/SockJS chưa sẵn sàng.');
            return;
        }

        stompClient = new StompJs.Client({
            webSocketFactory: () => new SockJS('/ws'),
            connectHeaders: {
                'X-XSRF-TOKEN': Qaute.csrfToken()
            },
            reconnectDelay: 3000,
            onConnect: () => {
                if (state.currentSession && state.currentSession.sessionId) {
                    subscribeSession(state.currentSession.sessionId);
                    if (state.currentSession.status === 'ACTIVE') {
                        syncMessages(state.lastMessageId);
                    }
                }
            },
            onStompError: frame => {
                console.error('STOMP Error:', frame);
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
                handleStompEvent(event);
            } catch (err) {
                console.error('Lỗi phân tích sự kiện STOMP:', err);
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

    function handleStompEvent(event) {
        if (!event || !event.type) return;

        switch (event.type) {
            case 'NEW_MESSAGE':
                if (event.message) {
                    addOrUpdateMessage(event.message);
                }
                break;

            case 'MESSAGE_READ':
                if (event.messageId) {
                    state.messages.forEach(m => {
                        if (m.senderType === 'STUDENT' && m.id && m.id <= event.messageId) {
                            if (!m.readAt) m.readAt = event.at || new Date().toISOString();
                        }
                    });
                    renderMessages();
                }
                break;

            case 'TYPING':
                const indicator = el('roomTypingIndicator');
                if (indicator) {
                    if (event.isTyping) {
                        el('roomTypingText').textContent = (state.currentSession?.managerName || 'Tư vấn viên') + ' đang nhập...';
                        indicator.style.display = 'block';
                        clearTimeout(indicator._timer);
                        indicator._timer = setTimeout(() => {
                            indicator.style.display = 'none';
                        }, 4000);
                    } else {
                        indicator.style.display = 'none';
                    }
                }
                break;

            case 'QUEUE_POSITION_CHANGED':
                if (event.queuePosition != null && state.currentSession) {
                    state.currentSession.queuePosition = event.queuePosition;
                    el('queuePositionNum').textContent = event.queuePosition;
                    Qaute.toast('Vị trí trong hàng chờ: #' + event.queuePosition, 'info');
                }
                break;

            case 'SESSION_EXPIRED':
                if (state.currentSession) {
                    state.currentSession.status = 'EXPIRED';
                    if (event.message) state.currentSession.message = event.message;
                }
                setState('C');
                Qaute.toast(event.message || 'Phiên chờ đã hết hạn do không có tư vấn viên nhận.', 'warning');
                break;

            case 'AGENT_JOINED':
                if (state.currentSession) {
                    state.currentSession.status = 'ACTIVE';
                    if (event.managerName) {
                        state.currentSession.managerName = event.managerName;
                    }
                }
                setState('D');
                Qaute.toast('Tư vấn viên ' + (state.currentSession?.managerName || '') + ' đã tham gia cuộc trò chuyện!', 'success');
                break;

            case 'SESSION_TRANSFERRED':
                Qaute.toast('Cuộc trò chuyện đã được chuyển cho tư vấn viên khác.', 'info');
                checkCurrentSession();
                break;

            case 'SESSION_ENDED':
                onSessionEnded();
                Qaute.toast('Phiên tư vấn đã kết thúc.', 'info');
                break;

            case 'ERROR':
                Qaute.toast(event.message || 'Đã xảy ra lỗi chat.', 'danger');
                break;
        }
    }

    function onSessionEnded() {
        if (state.currentSession) {
            state.currentSession.status = 'ENDED';
        }
        el('chatInputArea').style.display = 'none';
        el('btnRoomEndChat').style.display = 'none';
        el('roomAdvisorStatus').innerHTML = '<span class="badge badge-dot bg-secondary"></span> Đã kết thúc';
        el('chatPostSessionBlock').style.display = 'block';

        const badge = el('chatHeaderBadge');
        if (badge) {
            badge.textContent = 'Đã kết thúc';
            badge.className = 'badge bg-secondary text-white';
        }

        scrollToBottom(true);
    }

    // --- REST Synchronization ---
    async function syncMessages(afterId) {
        if (!state.currentSession || !state.currentSession.sessionId) return;
        try {
            const url = '/api/chat/sessions/' + state.currentSession.sessionId + '/messages?afterId=' + (afterId || 0);
            const resp = await Qaute.fetchJson(url);
            const batch = resp.data || [];
            batch.forEach(m => addOrUpdateMessage(m));
        } catch (err) {
            console.error('Không thể đồng bộ tin nhắn:', err);
        }
    }

    // --- Session Lifecycle (Start, Request Agent, Cancel, End) ---
    async function checkCurrentSession() {
        setState('LOADING');
        try {
            const resp = await Qaute.fetchJson('/api/chat/sessions/current');
            const data = resp.data;

            if (data && data.sessionId) {
                state.currentSession = data;
                if (stompClient && stompClient.connected) {
                    subscribeSession(state.currentSession.sessionId);
                }

                if (data.status === 'BOT') {
                    setState('B');
                } else if (data.status === 'WAITING' || data.status === 'EXPIRED') {
                    setState('C');
                } else if (data.status === 'ACTIVE' || data.status === 'ENDED') {
                    setState('D');
                } else {
                    state.currentSession = null;
                    setState('A');
                }
            } else {
                state.currentSession = null;
                setState('A');
            }
        } catch (error) {
            console.error('Không thể kiểm tra phiên hiện tại:', error);
            state.currentSession = null;
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

            state.currentSession = resp.data;
            state.messages = [];
            state.lastMessageId = 0;
            state.lastReadMarkedId = 0;

            if (stompClient && stompClient.connected) {
                subscribeSession(state.currentSession.sessionId);
            }

            if (state.currentSession.status === 'BOT') {
                setState('B');
            } else if (state.currentSession.status === 'WAITING') {
                setState('C');
            } else if (state.currentSession.status === 'ACTIVE') {
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
        if (!state.currentSession || !state.currentSession.sessionId) return;

        const btn = el('btnRequestAgent');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang kết nối tư vấn viên...';

        try {
            const resp = await Qaute.fetchJson('/api/chat/sessions/' + state.currentSession.sessionId + '/request-agent', {
                method: 'POST'
            });

            state.currentSession = resp.data;
            if (stompClient && stompClient.connected) {
                subscribeSession(state.currentSession.sessionId);
            }

            if (state.currentSession.status === 'WAITING' || state.currentSession.status === 'EXPIRED') {
                setState('C');
            } else if (state.currentSession.status === 'ACTIVE') {
                setState('D');
            }
        } catch (error) {
            Qaute.toast(error.message || 'Không thể yêu cầu tư vấn viên.', 'danger');
            btn.disabled = false;
            btn.innerHTML = '<i class="bi bi-headset me-1"></i> Vẫn muốn nói chuyện với tư vấn viên';
        }
    }

    async function cancelSession() {
        if (!state.currentSession || !state.currentSession.sessionId) return;

        try {
            await Qaute.fetchJson('/api/chat/sessions/' + state.currentSession.sessionId + '/cancel', {
                method: 'POST'
            });
            Qaute.toast('Đã hủy phiên tư vấn.', 'info');
        } catch (error) {
            console.warn('Lỗi hủy phiên:', error);
        } finally {
            resetSession();
        }
    }

    function endSession() {
        if (!state.currentSession || !state.currentSession.sessionId) return;
        if (!stompClient || !stompClient.connected) {
            Qaute.toast('Mất kết nối với máy chủ.', 'warning');
            return;
        }

        stompClient.publish({
            destination: '/app/chat.end',
            body: JSON.stringify({ sessionId: state.currentSession.sessionId })
        });
    }

    function resetSession() {
        unsubscribeSession();
        state.currentSession = null;
        state.messages = [];
        state.lastMessageId = 0;
        state.lastReadMarkedId = 0;
        state.selectedRating = 0;
        removeAttachment();

        el('chatQuestion').value = '';
        el('chatQuestionCount').textContent = '0 / 500';
        el('postSessionTicketResult').style.display = 'none';
        setState('A');
    }

    // --- Rating & Ticket Convert ---
    function setupRatingStars() {
        const starContainer = el('ratingStars');
        if (!starContainer) return;

        const stars = starContainer.querySelectorAll('.qa-star');
        const ratingLabels = ['', 'Rất không hài lòng', 'Không hài lòng', 'Bình thường', 'Hài lòng', 'Rất hài lòng'];

        stars.forEach(star => {
            star.addEventListener('click', () => {
                state.selectedRating = Number(star.getAttribute('data-rating'));
                updateStarDisplay(state.selectedRating);
                el('ratingLabel').textContent = state.selectedRating + ' sao - ' + ratingLabels[state.selectedRating];
                el('btnSubmitRating').disabled = false;
            });

            star.addEventListener('mouseenter', () => {
                const r = Number(star.getAttribute('data-rating'));
                updateStarDisplay(r);
            });
        });

        starContainer.addEventListener('mouseleave', () => {
            updateStarDisplay(state.selectedRating);
        });
    }

    function updateStarDisplay(rating) {
        const stars = document.querySelectorAll('#ratingStars .qa-star');
        stars.forEach(s => {
            const r = Number(s.getAttribute('data-rating'));
            if (r <= rating) {
                s.classList.add('active');
            } else {
                s.classList.remove('active');
            }
        });
    }

    async function submitRating() {
        if (!state.currentSession || !state.selectedRating) return;
        const comment = el('ratingComment').value.trim();

        const btn = el('btnSubmitRating');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Đang gửi...';

        try {
            await Qaute.fetchJson('/api/chat/sessions/' + state.currentSession.sessionId + '/rate', {
                method: 'POST',
                body: JSON.stringify({
                    rating: state.selectedRating,
                    comment: comment
                })
            });

            btn.innerHTML = '<i class="bi bi-check-lg me-1"></i> Đã đánh giá';
            btn.className = 'btn btn-success';
            el('ratingComment').disabled = true;
            Qaute.toast('Cảm ơn bạn đã gửi đánh giá buổi tư vấn!', 'success');
        } catch (err) {
            Qaute.toast(err.message || 'Không thể lưu đánh giá.', 'danger');
            btn.disabled = false;
            btn.innerHTML = '<i class="bi bi-star me-1"></i> Gửi đánh giá';
        }
    }

    async function convertToTicket() {
        if (!state.currentSession || !state.currentSession.sessionId) return;
        const btn = el('btnConvertToTicketPost');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Đang tạo...';

        try {
            const resp = await Qaute.fetchJson('/api/chat/sessions/' + state.currentSession.sessionId + '/to-ticket', {
                method: 'POST'
            });
            const ticketId = resp.data.ticketId;

            el('postSessionTicketLink').href = '/student/tickets/' + ticketId;
            el('postSessionTicketResult').style.display = 'block';
            Qaute.toast('Đã tạo yêu cầu hỗ trợ thành công!', 'success');
        } catch (err) {
            Qaute.toast(err.message || 'Không thể tạo yêu cầu hỗ trợ.', 'danger');
            btn.disabled = false;
        } finally {
            btn.innerHTML = '<i class="bi bi-ticket-perforated me-1"></i> Tạo yêu cầu từ hội thoại này';
        }
    }

    // --- Chat History Tab ---
    async function loadHistory(page) {
        state.historyPage = Math.max(0, page || 0);

        el('historyLoading').style.display = 'block';
        el('historyEmpty').style.display = 'none';
        el('historyTableContainer').style.display = 'none';
        el('historyPaginationContainer').style.display = 'none';

        try {
            const resp = await Qaute.fetchJson('/api/chat/sessions/history?page=' + state.historyPage);
            const data = resp.data || {};
            const items = data.content || [];
            const totalPages = data.totalPages || 0;

            el('historyLoading').style.display = 'none';

            if (items.length === 0) {
                el('historyEmpty').style.display = 'block';
                return;
            }

            el('historyTableContainer').style.display = 'block';
            const tbody = el('historyTableBody');
            tbody.replaceChildren();

            items.forEach(item => {
                const tr = document.createElement('tr');

                // Started at
                const tdTime = document.createElement('td');
                const timeText = document.createElement('div');
                timeText.className = 'fw-medium';
                timeText.textContent = Qaute.formatDateTime(item.startedAt);
                tdTime.appendChild(timeText);
                if (item.endedAt) {
                    const durationText = document.createElement('div');
                    durationText.className = 'text-secondary small';
                    durationText.textContent = 'Kết thúc: ' + formatTime(item.endedAt);
                    tdTime.appendChild(durationText);
                }
                tr.appendChild(tdTime);

                // Category
                const tdCat = document.createElement('td');
                tdCat.textContent = item.categoryName || '—';
                tr.appendChild(tdCat);

                // Manager
                const tdManager = document.createElement('td');
                tdManager.textContent = item.managerName || 'Chưa nhận';
                tr.appendChild(tdManager);

                // Status
                const tdStatus = document.createElement('td');
                const badge = document.createElement('span');
                badge.className = item.status === 'ENDED' ? 'badge bg-secondary-lt' : 'badge bg-warning-lt';
                badge.textContent = item.status === 'ENDED' ? 'Đã kết thúc' : item.status;
                tdStatus.appendChild(badge);
                tr.appendChild(tdStatus);

                // Action
                const tdAction = document.createElement('td');
                const btnView = document.createElement('button');
                btnView.type = 'button';
                btnView.className = 'btn btn-sm btn-outline-primary';
                btnView.innerHTML = '<i class="bi bi-eye me-1"></i>Xem lại';
                btnView.addEventListener('click', () => viewHistoryDetail(item));
                tdAction.appendChild(btnView);
                tr.appendChild(tdAction);

                tbody.appendChild(tr);
            });

            // Pagination
            if (totalPages > 1) {
                renderHistoryPagination(totalPages, state.historyPage);
                el('historyPaginationContainer').style.display = 'flex';
            }
        } catch (err) {
            el('historyLoading').style.display = 'none';
            Qaute.toast(err.message || 'Không thể tải lịch sử tư vấn.', 'danger');
        }
    }

    function renderHistoryPagination(totalPages, currentPage) {
        const pag = el('historyPagination');
        pag.replaceChildren();

        const addPageItem = (label, targetPage, disabled, active) => {
            const li = document.createElement('li');
            li.className = 'page-item' + (disabled ? ' disabled' : '') + (active ? ' active' : '');
            const a = document.createElement('a');
            a.className = 'page-link';
            a.href = 'javascript:void(0)';
            a.textContent = label;
            if (!disabled && !active) {
                a.addEventListener('click', () => loadHistory(targetPage));
            }
            li.appendChild(a);
            pag.appendChild(li);
        };

        addPageItem('Trước', currentPage - 1, currentPage === 0, false);
        for (let i = 0; i < totalPages; i++) {
            if (i === 0 || i === totalPages - 1 || Math.abs(i - currentPage) <= 1) {
                addPageItem(String(i + 1), i, false, i === currentPage);
            }
        }
        addPageItem('Sau', currentPage + 1, currentPage >= totalPages - 1, false);
    }

    async function viewHistoryDetail(historyItem) {
        const modal = el('chatHistoryModal');
        if (!modal) return;

        el('historyModalTitle').textContent = 'Cuộc hội thoại ngày ' + Qaute.formatDateTime(historyItem.startedAt);
        el('historyModalSubtitle').textContent = 'Chuyên mục: ' + (historyItem.categoryName || '') + ' · Tư vấn viên: ' + (historyItem.managerName || 'Chưa nhận');

        el('historyModalLoading').style.display = 'block';
        el('historyMessagesContainer').style.display = 'none';

        if (window.bootstrap && window.bootstrap.Modal) {
            bootstrap.Modal.getOrCreateInstance(modal).show();
        }

        try {
            const resp = await Qaute.fetchJson('/api/chat/sessions/' + historyItem.id + '/messages');
            const messages = resp.data || [];

            el('historyModalLoading').style.display = 'none';
            el('historyMessagesContainer').style.display = 'flex';

            const container = el('historyMessagesContainer');
            container.replaceChildren();

            messages.forEach(msg => {
                const row = document.createElement('div');
                const isMe = msg.senderType === 'STUDENT';
                const isSystem = msg.senderType === 'SYSTEM';

                row.className = 'qa-chat-row ' + (isMe ? 'qa-chat-me' : isSystem ? 'qa-chat-system' : 'qa-chat-other');

                const bubble = document.createElement('div');
                bubble.className = 'qa-chat-bubble';

                if (!isMe && !isSystem) {
                    const sender = document.createElement('span');
                    sender.className = 'qa-chat-sender';
                    sender.textContent = msg.senderName || 'Tư vấn viên';
                    bubble.appendChild(sender);
                }

                if (msg.assetId) {
                    const attachLink = document.createElement('a');
                    attachLink.href = '/api/media/' + encodeURIComponent(msg.assetId) + '/download';
                    attachLink.className = 'qa-chat-attachment';
                    attachLink.target = '_blank';
                    attachLink.rel = 'noopener noreferrer';
                    attachLink.innerHTML = '<i class="bi bi-paperclip fs-4"></i>';
                    const attachName = document.createElement('span');
                    attachName.textContent = msg.content || 'Tệp đính kèm';
                    attachLink.appendChild(attachName);
                    bubble.appendChild(attachLink);
                } else {
                    const textDiv = document.createElement('div');
                    textDiv.className = 'text-break';
                    textDiv.textContent = msg.content || '';
                    bubble.appendChild(textDiv);
                }

                if (!isSystem) {
                    const meta = document.createElement('div');
                    meta.className = 'qa-chat-meta';
                    meta.textContent = formatTime(msg.sentAt);
                    bubble.appendChild(meta);
                }

                row.appendChild(bubble);
                container.appendChild(row);
            });

            container.scrollTop = container.scrollHeight;
        } catch (err) {
            el('historyModalLoading').style.display = 'none';
            Qaute.toast(err.message || 'Không thể tải tin nhắn lịch sử.', 'danger');
        }
    }

    // --- Init & Event Listeners ---
    function init() {
        // Tab switching: Live Chat vs History
        const tabLive = el('tabLiveChat');
        const tabHist = el('tabHistory');
        const liveView = el('chatLiveView');
        const histView = el('chatHistoryView');

        if (tabLive && tabHist) {
            tabLive.addEventListener('click', () => {
                tabLive.classList.add('active');
                tabHist.classList.remove('active');
                liveView.style.display = 'block';
                histView.style.display = 'none';
            });

            tabHist.addEventListener('click', () => {
                tabHist.classList.add('active');
                tabLive.classList.remove('active');
                liveView.style.display = 'none';
                histView.style.display = 'block';
                loadHistory(0);
            });
        }

        const btnRefreshHist = el('btnRefreshHistory');
        if (btnRefreshHist) {
            btnRefreshHist.addEventListener('click', () => loadHistory(state.historyPage));
        }

        // Start Form
        const questionInput = el('chatQuestion');
        if (questionInput) {
            questionInput.addEventListener('input', () => {
                el('chatQuestionCount').textContent = questionInput.value.length + ' / 500';
            });
        }

        const formStart = el('formStartChat');
        if (formStart) {
            formStart.addEventListener('submit', startSession);
        }

        // State B (Bot) buttons
        const btnResolved = el('btnResolved');
        if (btnResolved) {
            btnResolved.addEventListener('click', async () => {
                if (state.currentSession) {
                    try {
                        await Qaute.fetchJson('/api/chat/sessions/' + state.currentSession.sessionId + '/cancel', { method: 'POST' });
                    } catch (_) {}
                }
                unsubscribeSession();
                state.currentSession = null;
                el('botResolvedSuccess').style.display = 'block';
                el('btnResolved').disabled = true;
                el('btnRequestAgent').disabled = true;
                Qaute.toast('Cảm ơn bạn đã sử dụng hệ thống tư vấn!', 'success');
            });
        }

        const btnStartNewAfterResolved = el('btnStartNewAfterResolved');
        if (btnStartNewAfterResolved) {
            btnStartNewAfterResolved.addEventListener('click', () => {
                resetSession();
            });
        }

        const btnRequestAgent = el('btnRequestAgent');
        if (btnRequestAgent) {
            btnRequestAgent.addEventListener('click', requestAgent);
        }

        // State C (Queue) buttons
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
                resetSession();
            });
        }

        // State D (Room) input & controls
        const roomInput = el('roomMessageInput');
        if (roomInput) {
            roomInput.addEventListener('input', () => {
                handleTypingInput();
                el('chatInputCharCount').textContent = roomInput.value.length + ' / 1000';
            });

            roomInput.addEventListener('keydown', e => {
                if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    sendMessage();
                }
            });
        }

        const btnSend = el('btnRoomSend');
        if (btnSend) {
            btnSend.addEventListener('click', sendMessage);
        }

        const btnAttach = el('btnChatAttach');
        const fileInput = el('chatFileInput');
        if (btnAttach && fileInput) {
            btnAttach.addEventListener('click', () => fileInput.click());
            fileInput.addEventListener('change', handleFileSelected);
        }

        const btnRemoveAttach = el('btnRemoveAttach');
        if (btnRemoveAttach) {
            btnRemoveAttach.addEventListener('click', removeAttachment);
        }

        const btnEndChat = el('btnRoomEndChat');
        if (btnEndChat) {
            btnEndChat.addEventListener('click', () => {
                if (window.confirm('Bạn có chắc chắn muốn kết thúc cuộc trò chuyện này?')) {
                    endSession();
                }
            });
        }

        // Post Session: Rating & Ticket
        setupRatingStars();

        const btnSubmitRate = el('btnSubmitRating');
        if (btnSubmitRate) {
            btnSubmitRate.addEventListener('click', submitRating);
        }

        const btnConvertTicketPost = el('btnConvertToTicketPost');
        if (btnConvertTicketPost) {
            btnConvertTicketPost.addEventListener('click', convertToTicket);
        }

        const btnNewChatAfterEnd = el('btnNewChatAfterEnd');
        if (btnNewChatAfterEnd) {
            btnNewChatAfterEnd.addEventListener('click', () => {
                resetSession();
            });
        }

        // Visibility & focus listeners for read receipts
        window.addEventListener('focus', markRead);
        document.addEventListener('visibilitychange', markRead);

        // Start STOMP and check ongoing session
        initStomp();
        checkCurrentSession();
    }

    QauteChat.init = init;
    QauteChat.getState = () => state;

    window.QauteChat = QauteChat;

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})(window, document);
