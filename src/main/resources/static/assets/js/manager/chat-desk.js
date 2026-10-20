(function () {
    'use strict';
    const root = document.getElementById('chat-desk');
    if (!root) return;
    const el = id => document.getElementById(id);
    const state = {client: null, active: null, mine: new Map(), queue: [], subscriptions: new Map(), lastIds: new Map(), lastRead: new Map(), unread: new Map(), seen: new Map(), typingTimer: null, audio: null};
    const toast = (message, type) => Qaute.toast(message, type || 'info');
    const api = (url, options) => Qaute.fetchJson(url, options).then(result => result.data);
    const button = (label, click, className) => {
        const node = document.createElement('button'); node.type = 'button'; node.className = className || 'list-group-item list-group-item-action';
        node.textContent = label; node.addEventListener('click', click); return node;
    };
    const text = (tag, value, className) => {
        const node = document.createElement(tag); node.textContent = value == null ? '' : String(value);
        if (className) node.className = className; return node;
    };
    function beep() {
        try {
            state.audio = state.audio || new (window.AudioContext || window.webkitAudioContext)();
            const oscillator = state.audio.createOscillator(), gain = state.audio.createGain();
            oscillator.frequency.value = 660; gain.gain.value = 0.035;
            oscillator.connect(gain); gain.connect(state.audio.destination);
            oscillator.start(); oscillator.stop(state.audio.currentTime + 0.12);
        } catch (_) { /* Audio may be disabled until the first user gesture. */ }
    }
    function renderQueue() {
        const box = el('chat-queue'); box.replaceChildren(); el('chat-queue-count').textContent = state.queue.length;
        if (!state.queue.length) box.append(text('div', 'Chưa có sinh viên chờ.', 'list-group-item text-secondary'));
        state.queue.forEach(item => {
            const row = document.createElement('div'); row.className = 'list-group-item';
            row.append(text('div', '#' + item.position + ' · ' + item.studentName, 'fw-bold'));
            row.append(text('div', item.categoryPath, 'small text-secondary'));
            row.append(text('div', item.question, 'small text-break'));
            row.append(text('div', 'Chờ ' + String(Math.floor(item.waitingSeconds / 60)).padStart(2, '0') + ':' + String(item.waitingSeconds % 60).padStart(2, '0'), 'small text-secondary'));
            row.append(button('Nhận', () => send('/app/chat.accept', {sessionId: item.id}), 'btn btn-primary btn-sm mt-2'));
            box.append(row);
        });
    }
    function renderMine() {
        const box = el('chat-mine'); box.replaceChildren();
        if (!state.mine.size) box.append(text('div', 'Chưa có phiên đang tư vấn.', 'list-group-item text-secondary'));
        state.mine.forEach(item => {
            const row = button((item.studentName || 'Sinh viên') + ' · ' + item.categoryPath,
                () => select(item.sessionId), 'list-group-item list-group-item-action' + (state.active === item.sessionId ? ' active' : ''));
            const unread = state.unread.get(item.sessionId) || 0;
            if (unread) row.append(text('span', unread, 'badge bg-red ms-2'));
            box.append(row);
        });
    }
    async function loadQueue() {
        state.queue = await api('/api/chat/queue') || []; renderQueue();
    }
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
    function subscribe(id) {
        if (!state.client?.connected || state.subscriptions.has(id)) return;
        state.subscriptions.set(id, state.client.subscribe('/topic/chat/' + id, frame => onEvent(JSON.parse(frame.body))));
        sync(id);
    }
    async function sync(id) {
        try {
            let after = state.lastIds.get(id) || 0, batch;
            do {
                batch = await api('/api/chat/sessions/' + id + '/messages?afterId=' + after) || [];
                batch.forEach(message => addMessage(message, true));
                after = state.lastIds.get(id) || after;
            } while (batch.length === 200);
        } catch (error) { toast(error.message, 'danger'); }
    }
    function select(id) {
        state.active = id;
        const item = state.mine.get(id), enabled = !!item;
        el('chat-title').textContent = item ? (item.studentName || 'Sinh viên') : 'Chọn hội thoại';
        el('chat-subtitle').textContent = item ? item.categoryPath + ' · ' + item.question : '';
        ['chat-transfer', 'chat-end', 'chat-input', 'chat-file', 'chat-send', 'chat-to-ticket'].forEach(key => el(key).disabled = !enabled);
        el('chat-messages').replaceChildren(); el('chat-typing').textContent = '';
        if (enabled) {
            state.unread.delete(id);
            (state.seen.get(id) || []).forEach(addMessageToActive);
            el('chat-student').textContent = 'Đang tải…'; el('chat-recent-tickets').replaceChildren();
            api('/api/chat/sessions/' + id + '/student').then(context => {
                if (state.active !== id) return;
                const student = context.student;
                el('chat-student').replaceChildren(text('strong', student?.user?.fullName), text('div', student?.user?.mssv),
                    text('div', student?.facultyName), text('div', student?.classCode), text('div', student?.user?.email), text('div', student?.phone));
                const recent = el('chat-recent-tickets'); recent.replaceChildren();
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
    function ticketLink(id) {
        const box = el('chat-ticket-link'); box.replaceChildren();
        if (!id) return;
        const link = document.createElement('a'); link.href = '/manager/tickets/' + id; link.textContent = 'Mở ticket #' + id; box.append(link);
    }
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
    function addMessageToActive(message) {
        const row = document.createElement('div');
        row.className = 'my-2 d-flex ' + (message.senderType === 'MANAGER' ? 'justify-content-end' : 'justify-content-start');
        const bubble = document.createElement('div'); bubble.className = 'rounded p-2 ' +
            (message.senderType === 'MANAGER' ? 'bg-blue-lt' : message.senderType === 'SYSTEM' ? 'bg-secondary-lt' : 'bg-light');
        bubble.style.maxWidth = '85%'; bubble.append(text('strong', message.senderName || message.senderType, 'd-block small'));
        if (message.assetId) {
            const link = document.createElement('a'); link.href = '/api/media/' + message.assetId + '/download';
            link.textContent = message.content; bubble.append(link);
        } else bubble.append(text('div', message.content, 'text-break'));
        bubble.append(text('small', message.id ? (message.readAt ? 'Đã xem' : 'Đã gửi') : 'Đang gửi…', 'text-secondary'));
        row.append(bubble); el('chat-messages').append(row); el('chat-messages').scrollTop = el('chat-messages').scrollHeight;
    }
    function send(destination, payload) {
        if (!state.client?.connected) { toast('Mất kết nối. Đang thử kết nối lại.', 'warning'); return false; }
        state.client.publish({destination, body: JSON.stringify(payload)}); return true;
    }
    function markRead() {
        const id = state.active, list = state.seen.get(id) || [];
        if (!id || document.hidden || !document.hasFocus()) return;
        const last = list.filter(item => item.id && item.senderType === 'STUDENT').at(-1);
        if (last && last.id > (state.lastRead.get(id) || 0) &&
                send('/app/chat.read', {sessionId: id, lastReadMessageId: last.id})) state.lastRead.set(id, last.id);
    }
    async function onEvent(event) {
        if (event.type === 'NEW_MESSAGE') addMessage(event.message);
        else if (event.type === 'TYPING' && event.sessionId === state.active) {
            el('chat-typing').textContent = event.isTyping ? 'Đang nhập…' : '';
            clearTimeout(state.typingTimer); if (event.isTyping) state.typingTimer = setTimeout(() => el('chat-typing').textContent = '', 4000);
        } else if (event.type === 'MESSAGE_READ' && event.sessionId === state.active) {
            const message = (state.seen.get(event.sessionId) || []).find(item => item.id === event.messageId);
            if (message) { message.readAt = event.at; select(event.sessionId); }
        } else if (['SESSION_ENDED', 'SESSION_EXPIRED', 'SESSION_TRANSFERRED', 'AGENT_LEFT'].includes(event.type)) {
            if (event.type === 'SESSION_TRANSFERRED' || event.type === 'SESSION_ENDED' || event.type === 'SESSION_EXPIRED') {
                await loadMine(); toast('Hội thoại đã thay đổi trạng thái.');
            }
        } else if (event.type === 'ERROR') toast(event.message, 'danger');
    }
    function connect() {
        if (!window.StompJs || !window.SockJS) { toast('Không tải được kết nối chat.', 'danger'); return; }
        state.client = new StompJs.Client({webSocketFactory: () => new SockJS('/ws'),
            connectHeaders: {'X-XSRF-TOKEN': Qaute.csrfToken()}, reconnectDelay: 3000,
            onConnect: () => {
                state.subscriptions.clear();
                state.client.subscribe('/user/queue/chat-queue', async frame => {
                    const event = JSON.parse(frame.body);
                    if (event.type === 'CHAT_ACCEPTED') { await loadMine(event.sessionId); return; }
                    if (event.type === 'QUEUE_UPDATED') {
                        const old = new Set(state.queue.map(item => item.id));
                        state.queue = event.waiting || []; renderQueue();
                        if (state.queue.some(item => !old.has(item.id))) { beep(); toast('Có sinh viên mới trong hàng chờ.'); }
                    }
                });
                state.client.subscribe('/user/queue/errors', frame => toast(JSON.parse(frame.body).message, 'danger'));
                loadMine().catch(error => toast(error.message, 'danger'));
                loadQueue().catch(error => toast(error.message, 'danger'));
            }, onStompError: () => toast('Kết nối chat gặp lỗi.', 'danger')});
        state.client.activate();
    }
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
    el('chat-file').addEventListener('change', async event => {
        const file = event.target.files[0]; if (!file || !state.active) return;
        const sessionId = state.active, form = new FormData(); form.append('file', file); form.append('purpose', 'CHAT_ATTACHMENT');
        try {
            const response = await fetch('/api/media/upload', {method: 'POST', headers: {[Qaute.csrfHeader()]: Qaute.csrfToken()}, body: form});
            const body = await response.json(); if (!response.ok || !body.success) throw new Error(body.message || 'Không tải được tệp.');
            send('/app/chat.send', {sessionId, clientMessageId: crypto.randomUUID(), content: body.data.originalName, assetId: body.data.id});
        } catch (error) { toast(error.message, 'danger'); }
        event.target.value = '';
    });
    el('chat-end').addEventListener('click', () => { if (state.active && confirm('Kết thúc hội thoại này?')) send('/app/chat.end', {sessionId: state.active}); });
    el('chat-transfer').addEventListener('click', async () => {
        const item = state.mine.get(state.active); if (!item) return;
        try {
            const advisors = await api('/api/chat/transfer-targets?sessionId=' + encodeURIComponent(item.sessionId)) || [];
            const select = el('chat-transfer-target'); select.replaceChildren();
            advisors.forEach(advisor => { const option = document.createElement('option'); option.value = advisor.id; option.textContent = advisor.fullName; select.append(option); });
            el('chat-transfer-confirm').disabled = !advisors.length;
            if (!advisors.length) toast('Chưa có tư vấn viên khác đang sẵn sàng.', 'warning');
            bootstrap.Modal.getOrCreateInstance(el('chat-transfer-modal')).show();
        } catch (error) { toast(error.message, 'danger'); }
    });
    el('chat-transfer-confirm').addEventListener('click', () => {
        const target = Number(el('chat-transfer-target').value);
        if (state.active && target) send('/app/chat.transfer', {sessionId: state.active, toManagerId: target});
        bootstrap.Modal.getOrCreateInstance(el('chat-transfer-modal')).hide();
    });
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
