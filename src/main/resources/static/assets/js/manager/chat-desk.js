// Khởi tạo giao diện tư vấn chat dành cho quản lý.
(function () {
    'use strict';
    const root = document.getElementById('chat-desk');
    if (!root) return;
    // Tìm phần tử giao diện theo mã định danh.
    const el = id => document.getElementById(id);
    // Cập nhật trạng thái kết nối trên giao diện.
    function connectionStatus(connected) {
        const badge = el('chat-connection-status');
        if (!badge) return;
        badge.className = 'badge ' + (connected ? 'bg-green-lt' : 'bg-orange-lt');
        badge.textContent = connected ? 'Đã kết nối' : 'Đang kết nối lại…';
    }
    const state = {client: null, active: null, mine: new Map(), queue: [], queuePage: 0, subscriptions: new Map(), lastIds: new Map(), lastRead: new Map(), unread: new Map(), seen: new Map(), typingTimer: null, audio: null};
    // Hiển thị thông báo trong giao diện chat.
    const toast = (message, type) => Qaute.toast(message, type || 'info');
    // Gọi API và trả về phần dữ liệu trong phản hồi.
    const api = (url, options) => Qaute.fetchJson(url, options).then(result => result.data);
    // Tạo nút giao diện và gắn thao tác được cung cấp.
    const button = (label, click, className) => {
        const node = document.createElement('button'); node.type = 'button'; node.className = className || 'list-group-item list-group-item-action';
        node.textContent = label; node.addEventListener('click', click); return node;
    };
    // Tạo phần tử văn bản an toàn cho giao diện.
    const text = (tag, value, className) => {
        const node = document.createElement(tag); node.textContent = value == null ? '' : String(value);
        if (className) node.className = className; return node;
    };
    // Phát âm báo khi có sinh viên mới trong hàng chờ.
    function beep() {
        try {
            state.audio = state.audio || new (window.AudioContext || window.webkitAudioContext)();
            const oscillator = state.audio.createOscillator(), gain = state.audio.createGain();
            oscillator.frequency.value = 660; gain.gain.value = 0.035;
            oscillator.connect(gain); gain.connect(state.audio.destination);
            oscillator.start(); oscillator.stop(state.audio.currentTime + 0.12);
        } catch (_) { /* Audio may be disabled until the first user gesture. */ }
    }
    // Hiển thị danh sách sinh viên đang chờ tư vấn.
    function renderQueue() {
        const box = el('chat-queue'); box.replaceChildren(); el('chat-queue-count').textContent = state.queue.length;
        el('chat-queue-prev').disabled = state.queuePage === 0;
        el('chat-queue-next').disabled = state.queue.length < 25;
        el('chat-queue-page').textContent = 'Trang ' + (state.queuePage + 1);
        if (!state.queue.length) box.append(text('div', 'Chưa có sinh viên chờ.', 'list-group-item text-secondary'));
        // Tạo hàng hiển thị cho từng sinh viên đang chờ.
        state.queue.forEach(item => {
            const row = document.createElement('div'); row.className = 'list-group-item';
            row.append(text('div', '#' + item.position + ' · ' + item.studentName, 'fw-bold'));
            row.append(text('div', item.categoryPath, 'small text-secondary'));
            row.append(text('div', item.question, 'small text-break'));
            row.append(text('div', 'Chờ ' + String(Math.floor(item.waitingSeconds / 60)).padStart(2, '0') + ':' + String(item.waitingSeconds % 60).padStart(2, '0'), 'small text-secondary'));
            row.append(button('Nhận',
                // Nhận phiên chat đang chờ tư vấn.
                () => send('/app/chat.accept', {sessionId: item.id}), 'btn btn-primary btn-sm mt-2'));
            box.append(row);
        });
    }
    // Hiển thị danh sách hội thoại đang được quản lý tư vấn.
    function renderMine() {
        const box = el('chat-mine'); box.replaceChildren();
        if (!state.mine.size) box.append(text('div', 'Chưa có phiên đang tư vấn.', 'list-group-item text-secondary'));
        // Tạo nút mở cho từng hội thoại đang phụ trách.
        state.mine.forEach(item => {
            const row = button((item.studentName || 'Sinh viên') + ' · ' + item.categoryPath,
                // Mở phiên chat đã chọn.
                () => select(item.sessionId), 'list-group-item list-group-item-action' + (state.active === item.sessionId ? ' active' : ''));
            const unread = state.unread.get(item.sessionId) || 0;
            if (unread) row.append(text('span', unread, 'badge bg-red ms-2'));
            box.append(row);
        });
    }
    // Chuyển về trang trước của hàng chờ.
    document.getElementById('chat-queue-prev').addEventListener('click', function () {
        if (state.queuePage > 0) { state.queuePage--; loadQueue().catch(error => toast(error.message, 'danger')); }
    });
    // Chuyển sang trang tiếp theo của hàng chờ.
    document.getElementById('chat-queue-next').addEventListener('click', function () {
        state.queuePage++; loadQueue().catch(error => toast(error.message, 'danger'));
    });
    // Tải trang hàng chờ hiện tại từ máy chủ.
    async function loadQueue() {
        state.queue = await api('/api/chat/queue?page=' + state.queuePage) || []; renderQueue();
    }
    // Tải các phiên của quản lý và cập nhật hội thoại đang chọn.
    async function loadMine(preferred) {
        const items = (await api('/api/chat/mine') || []).map(item => ({...item,
            sessionId: item.session.sessionId, categoryId: item.session.categoryId,
            categoryPath: item.session.categoryPath, question: item.session.question}));
        state.mine = new Map(items.map(item => [item.sessionId, item]));
        for (const id of Array.from(state.subscriptions.keys())) if (!state.mine.has(id)) { state.subscriptions.get(id).unsubscribe(); state.subscriptions.delete(id); }
        if (state.client?.connected) items.forEach(item => subscribe(item.sessionId));
        if (preferred && state.mine.has(preferred)) select(preferred);
        else if (!state.mine.has(state.active)) select(items[0]?.sessionId || null);
        renderMine();
    }
    // Đăng ký nhận sự kiện cập nhật cho một phiên chat.
    function subscribe(id) {
        if (!state.client?.connected || state.subscriptions.has(id)) return;
        state.subscriptions.set(id, state.client.subscribe('/topic/chat/' + id,
            // Chuyển tiếp sự kiện nhận được tới bộ xử lý phiên chat.
            frame => onEvent(JSON.parse(frame.body))));
        sync(id);
    }
    // Đồng bộ các tin nhắn mới kể từ lần tải trước.
    async function sync(id) {
        try {
            let after = state.lastIds.get(id) || 0, batch;
            do {
                batch = await api('/api/chat/sessions/' + id + '/messages?afterId=' + after) || [];
                batch.forEach(message => addMessage(message, true));
                after = state.lastIds.get(id) || after;
        } while (batch.length === 100);
        } catch (error) { toast(error.message, 'danger'); }
    }
    // Chọn hội thoại và tải thông tin liên quan của sinh viên.
    function select(id) {
        state.active = id;
        const item = state.mine.get(id), enabled = !!item;
        el('chat-title').textContent = item ? (item.studentName || 'Sinh viên') : 'Chọn hội thoại';
        el('chat-subtitle').textContent = item ? item.categoryPath + ' · ' + item.question : '';
        ['chat-transfer', 'chat-end', 'chat-input', 'chat-file', 'chat-send', 'chat-to-ticket'].forEach(key => el(key).disabled = !enabled);
        el('chat-messages').replaceChildren();
        el('chat-typing').style.display = 'none';
        clearTimeout(state.typingTimer);
        if (enabled) {
            state.unread.delete(id);
            (state.seen.get(id) || []).forEach(addMessageToActive);
            el('chat-student').textContent = 'Đang tải…'; el('chat-recent-tickets').replaceChildren();
            // Hiển thị thông tin sinh viên và ticket gần đây của phiên chat.
            api('/api/chat/sessions/' + id + '/student').then(context => {
                if (state.active !== id) return;
                const student = context.student;
                el('chat-student').replaceChildren(text('strong', student?.user?.fullName), text('div', student?.user?.mssv),
                    text('div', student?.facultyName), text('div', student?.classCode), text('div', student?.user?.email), text('div', student?.phone));
                const recent = el('chat-recent-tickets'); recent.replaceChildren();
                // Tạo liên kết tới các ticket gần đây của sinh viên.
                (context.recentTickets || []).forEach(ticket => {
                    const link = document.createElement('a'); link.href = '/manager/tickets/' + ticket.id;
                    link.className = 'list-group-item list-group-item-action';
                    link.textContent = ticket.code + ' · ' + ticket.title + ' (' + ticket.status + ')'; recent.append(link);
                });
                if (!context.recentTickets?.length) recent.append(text('div', 'Chưa có ticket gần đây.', 'list-group-item text-secondary'));
            }).catch(error => toast(error.message, 'danger'));
            ticketLink(item.ticketId); sync(id); markRead();
        } else {
            el('chat-student').textContent = 'Chọn hội thoại để xem thông tin.';
            el('chat-recent-tickets').replaceChildren(); ticketLink(null);
        }
        renderMine();
    }
    // Cập nhật liên kết đến ticket của hội thoại.
    function ticketLink(id) {
        const box = el('chat-ticket-link'); box.replaceChildren();
        if (!id) return;
        const link = document.createElement('a'); link.href = '/manager/tickets/' + id; link.textContent = 'Mở ticket #' + id; box.append(link);
    }
    // Lưu tin nhắn vào trạng thái và đánh dấu tin chưa đọc.
    function addMessage(message, fromHistory) {
        const id = message.sessionId, list = state.seen.get(id) || [];
        if (!fromHistory && message.id && message.senderType === 'STUDENT' && state.active !== id &&
                !list.some(item => item.id === message.id)) {
            state.unread.set(id, (state.unread.get(id) || 0) + 1); renderMine();
        }
        const previous = list.findIndex(item => item.id === message.id ||
            (message.clientMessageId && item.clientMessageId === message.clientMessageId));
        if (previous >= 0) list[previous] = message; else list.push(message);
        list.sort((a, b) => (a.id || Infinity) - (b.id || Infinity));
        state.seen.set(id, list);
        if (message.id) state.lastIds.set(id, Math.max(state.lastIds.get(id) || 0, message.id));
        if (state.active === id) { el('chat-messages').replaceChildren(); list.forEach(addMessageToActive); markRead(); }
    }
    // Định dạng thời điểm gửi tin theo giờ địa phương.
    function formatTime(value) {
        if (!value) return '';
        try {
            return Qaute.formatTime(value);
        } catch (_) { return ''; }
    }
    // Hiển thị một tin nhắn trong hội thoại đang mở.
    function addMessageToActive(message) {
        const list = el('chat-messages');
        const isSystem = message.senderType === 'SYSTEM' || message.senderType === 'BOT';
        const isMe = message.senderType === 'MANAGER';
        const row = document.createElement('div');
        row.className = 'qa-chat-row ' + (isMe ? 'qa-chat-me' : isSystem ? 'qa-chat-system' : 'qa-chat-other');
        const bubble = document.createElement('div'); bubble.className = 'qa-chat-bubble';
        if (!isMe && !isSystem) bubble.append(text('span', message.senderName || 'Sinh viên', 'qa-chat-sender'));
        if (message.assetId) {
            // Kiểm tra nếu là ảnh hiển thị thumbnail, nếu không thì hiển thị link download
            const content = message.content || 'Tệp đính kèm';
            const isImage = /\.(jpg|jpeg|png|gif|webp|bmp)$/i.test(content);

            if (isImage) {
                const imgContainer = document.createElement('div');
                imgContainer.className = 'qa-chat-image-container';

                const img = document.createElement('img');
                img.src = '/api/media/' + encodeURIComponent(message.assetId) + '/download';
                img.alt = content;
                img.className = 'qa-chat-image';
                img.style.maxWidth = '100%';
                img.style.maxHeight = '300px';
                img.style.borderRadius = '8px';
                img.style.cursor = 'pointer';

                const imgLink = document.createElement('a');
                imgLink.href = '/api/media/' + encodeURIComponent(message.assetId) + '/download';
                imgLink.target = '_blank';
                imgLink.rel = 'noopener noreferrer';
                imgLink.appendChild(img);

                const downloadLink = document.createElement('a');
                downloadLink.href = '/api/media/' + encodeURIComponent(message.assetId) + '/download';
                downloadLink.target = '_blank';
                downloadLink.rel = 'noopener noreferrer';
                downloadLink.className = 'qa-chat-attachment mt-2';
                downloadLink.style.display = 'inline-flex';
                downloadLink.style.alignItems = 'center';
                downloadLink.style.gap = '0.5rem';
                downloadLink.style.fontSize = '0.85rem';

                const downloadIcon = document.createElement('i');
                downloadIcon.className = 'bi bi-download fs-5';
                const downloadName = document.createElement('span');
                downloadName.className = 'text-truncate';
                downloadName.textContent = content;

                downloadLink.appendChild(downloadIcon);
                downloadLink.appendChild(downloadName);

                imgContainer.appendChild(imgLink);
                imgContainer.appendChild(downloadLink);
                bubble.append(imgContainer);
            } else {
                const link = document.createElement('a');
                link.href = '/api/media/' + encodeURIComponent(message.assetId) + '/download';
                link.target = '_blank'; link.rel = 'noopener noreferrer'; link.className = 'qa-chat-attachment';
                const icon = document.createElement('i'); icon.className = 'bi bi-paperclip fs-4';
                link.append(icon, text('span', content, 'text-truncate'));
                bubble.append(link);
            }
        } else bubble.append(text('div', message.content, 'qa-chat-text'));
        // Chỉ hiển thị thời gian và trạng thái gửi cho tin nhắn của chính mình.
        if (!isSystem) {
            const meta = document.createElement('div'); meta.className = 'qa-chat-meta';
            meta.append(text('span', formatTime(message.sentAt)));
            if (isMe) {
                const status = document.createElement('span'); status.className = 'qa-chat-status';
                status.textContent = !message.id ? 'Đang gửi…' : (message.readAt ? 'Đã xem' : 'Đã gửi');
                meta.append(status);
            }
            bubble.append(meta);
        }
        row.append(bubble); list.append(row); list.scrollTop = list.scrollHeight;
    }
    // Gửi một sự kiện tới máy chủ chat.
    function send(destination, payload) {
        if (!state.client?.connected) { toast('Mất kết nối. Đang thử kết nối lại.', 'warning'); return false; }
        state.client.publish({destination, body: JSON.stringify(payload)}); return true;
    }
    // Đánh dấu tin nhắn sinh viên đã đọc khi cửa sổ đang hiển thị.
    function markRead() {
        const id = state.active, list = state.seen.get(id) || [];
        if (!id || document.hidden || !document.hasFocus()) return;
        const last = list.filter(item => item.id && item.senderType === 'STUDENT').at(-1);
        if (last && last.id > (state.lastRead.get(id) || 0) &&
                send('/app/chat.read', {sessionId: id, lastReadMessageId: last.id})) state.lastRead.set(id, last.id);
    }
    // Xử lý sự kiện nhận được trong phiên chat.
    async function onEvent(event) {
        if (event.type === 'NEW_MESSAGE') addMessage(event.message);
        else if (event.type === 'TYPING' && event.sessionId === state.active) {
            // Chỉ hiển thị khi chính sinh viên đang nhập, bỏ qua sự kiện do quản lý tự phát.
            if (event.senderType === 'STUDENT') {
                const indicator = el('chat-typing');
                if (indicator) {
                    if (event.isTyping) {
                        indicator.style.display = 'flex';
                        clearTimeout(state.typingTimer);
                        state.typingTimer = setTimeout(() => {
                            indicator.style.display = 'none';
                        }, 4000);
                    } else {
                        indicator.style.display = 'none';
                        clearTimeout(state.typingTimer);
                    }
                }
            } else {
                const indicator = el('chat-typing');
                if (indicator) indicator.style.display = 'none';
                clearTimeout(state.typingTimer);
            }
        } else if (event.type === 'MESSAGE_READ' && event.sessionId === state.active) {
            const message = (state.seen.get(event.sessionId) || []).find(item => item.id === event.messageId);
            if (message) { message.readAt = event.at; select(event.sessionId); }
        } else if (['SESSION_ENDED', 'SESSION_EXPIRED', 'SESSION_TRANSFERRED', 'AGENT_LEFT'].includes(event.type)) {
            if (event.type === 'SESSION_TRANSFERRED' || event.type === 'SESSION_ENDED' || event.type === 'SESSION_EXPIRED') {
                await loadMine(); toast('Hội thoại đã thay đổi trạng thái.');
            }
        } else if (event.type === 'ERROR') toast(event.message, 'danger');
    }
    // Kết nối tới dịch vụ chat và đăng ký nhận sự kiện.
    function connect() {
        if (!window.StompJs || !window.SockJS) { toast('Không tải được kết nối chat.', 'danger'); return; }
        state.client = new StompJs.Client({webSocketFactory: () => new SockJS('/ws'),
            connectHeaders: {'X-XSRF-TOKEN': Qaute.csrfToken()}, reconnectDelay: 3000,
            beforeConnect: async () => {
                try {
                    await Qaute.refreshSessionIfNeeded();
                } catch (error) {
                    toast(error.message, 'danger');
                    throw error;
                }
            },
            // Tải hàng chờ và các phiên chat sau khi kết nối thành công.
            onConnect: () => {
                connectionStatus(true);
                state.subscriptions.clear();
                // Xử lý cập nhật hàng chờ và yêu cầu nhận chat.
                state.client.subscribe('/user/queue/chat-queue', async frame => {
                    const event = JSON.parse(frame.body);
                    if (event.type === 'CHAT_ACCEPTED') { await loadMine(event.sessionId); return; }
                    if (event.type === 'QUEUE_UPDATED') {
                        const old = new Set(state.queue.map(item => item.id));
                        if (state.queuePage === 0) { state.queue = event.waiting || []; renderQueue(); }
                        else { loadQueue().catch(error => toast(error.message, 'danger')); }
                        if (state.queue.some(item => !old.has(item.id))) { beep(); toast('Có sinh viên mới trong hàng chờ.'); }
                    }
                });
                state.client.subscribe('/user/queue/errors', frame => toast(JSON.parse(frame.body).message, 'danger'));
                loadMine().catch(error => toast(error.message, 'danger'));
                loadQueue().catch(error => toast(error.message, 'danger'));
            // Cập nhật trạng thái khi kết nối WebSocket bị đóng.
            }, onWebSocketClose: () => connectionStatus(false),
            // Thông báo lỗi giao thức và trạng thái kết nối.
            onStompError: () => { connectionStatus(false); toast('Kết nối chat gặp lỗi.', 'danger'); }});
        state.client.activate();
    }
    // Gửi nội dung tin nhắn mới trong hội thoại đang mở.
    el('chat-compose').addEventListener('submit', event => {
        event.preventDefault(); const content = el('chat-input').value.trim();
        if (!state.active || !content) return;
        const clientMessageId = crypto.randomUUID();
        if (send('/app/chat.send', {sessionId: state.active, clientMessageId, content, assetId: null})) {
            addMessage({sessionId: state.active, clientMessageId, content, senderType: 'MANAGER', senderName: 'Bạn'});
            el('chat-input').value = '';
        }
    });
    let stopTyping, typingSession = null;
    // Phát sự kiện đang nhập và tự kết thúc trạng thái sau một khoảng chờ.
    el('chat-input').addEventListener('input', () => {
        if (!state.active) return;
        if (typingSession !== state.active) {
            typingSession = state.active;
            send('/app/chat.typing', {sessionId: state.active, isTyping: true});
        }
        clearTimeout(stopTyping);
        stopTyping = setTimeout(() => {
            send('/app/chat.typing', {sessionId: typingSession, isTyping: false});
            typingSession = null;
        }, 900);
    });
    // Gửi tin nhắn khi nhấn Enter, xuống dòng khi nhấn Shift + Enter.
    el('chat-input').addEventListener('keydown', event => {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            el('chat-compose').requestSubmit();
        }
    });
    // Tải tệp đính kèm lên rồi gửi liên kết trong hội thoại.
    el('chat-file').addEventListener('change', async event => {
        const input = event.target, file = input.files[0]; if (!file || !state.active) return;
        const sessionId = state.active, form = new FormData(); form.append('file', file); form.append('purpose', 'CHAT_ATTACHMENT');
        const uploadStatus = el('chat-upload-status');
        input.disabled = true;
        if (uploadStatus) {
            uploadStatus.textContent = file.type.startsWith('image/')
                ? 'Ảnh đang được đính kèm…'
                : 'Tệp đang được đính kèm…';
            uploadStatus.style.display = 'block';
        }
        try {
            const response = await fetch('/api/media/upload', {method: 'POST', headers: {[Qaute.csrfHeader()]: Qaute.csrfToken()}, body: form});
            const body = await response.json(); if (!response.ok || !body.success) throw new Error(body.message || 'Không tải được tệp.');
            send('/app/chat.send', {sessionId, clientMessageId: crypto.randomUUID(), content: body.data.originalName, assetId: body.data.id});
        } catch (error) { toast(error.message, 'danger'); }
        finally {
            input.value = '';
            input.disabled = !state.mine.has(sessionId);
            if (uploadStatus) uploadStatus.style.display = 'none';
        }
    });
    // Kết thúc hội thoại sau khi người dùng xác nhận.
    el('chat-end').addEventListener('click', () => { if (state.active && confirm('Kết thúc hội thoại này?')) send('/app/chat.end', {sessionId: state.active}); });
    // Tải danh sách tư vấn viên có thể nhận chuyển giao.
    el('chat-transfer').addEventListener('click', async () => {
        const item = state.mine.get(state.active); if (!item) return;
        try {
            const advisors = await api('/api/chat/transfer-targets?sessionId=' + encodeURIComponent(item.sessionId)) || [];
            const select = el('chat-transfer-target'); select.replaceChildren();
            // Đưa các tư vấn viên có thể nhận chuyển giao vào danh sách.
            advisors.forEach(advisor => { const option = document.createElement('option'); option.value = advisor.id; option.textContent = advisor.fullName; select.append(option); });
            el('chat-transfer-confirm').disabled = !advisors.length;
            if (!advisors.length) toast('Chưa có tư vấn viên khác đang sẵn sàng.', 'warning');
            bootstrap.Modal.getOrCreateInstance(el('chat-transfer-modal')).show();
        } catch (error) { toast(error.message, 'danger'); }
    });
    // Gửi yêu cầu chuyển giao tới tư vấn viên đã chọn.
    el('chat-transfer-confirm').addEventListener('click', () => {
        const target = Number(el('chat-transfer-target').value);
        if (state.active && target) send('/app/chat.transfer', {sessionId: state.active, toManagerId: target});
        bootstrap.Modal.getOrCreateInstance(el('chat-transfer-modal')).hide();
    });
    // Tạo ticket từ hội thoại chat đang mở.
    el('chat-to-ticket').addEventListener('click', async () => {
        if (!state.active) return;
        try {
            const result = await api('/api/chat/sessions/' + state.active + '/to-ticket', {method: 'POST'});
            ticketLink(result.ticketId); toast('Đã tạo ticket từ hội thoại.', 'success');
        } catch (error) { toast(error.message, 'danger'); }
    });
    document.addEventListener('visibilitychange', markRead); window.addEventListener('focus', markRead);
    Promise.all([loadQueue(), loadMine()]).catch(error => toast(error.message, 'danger'));
    connect();
}());
