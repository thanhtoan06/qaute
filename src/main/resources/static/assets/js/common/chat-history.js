(function () {
    'use strict';
    const root = document.getElementById('chat-history');
    if (!root) return;

    const apiUrl = root.dataset.apiUrl;
    const context = root.dataset.context || '';
    const list = document.getElementById('chat-history-list');
    const messages = document.getElementById('chat-history-messages');
    const error = document.getElementById('chat-history-error');
    const state = {page: 0, totalPages: 0};
    const text = (tag, value, className) => {
        const node = document.createElement(tag);
        node.textContent = value == null ? '' : String(value);
        if (className) node.className = className;
        return node;
    };
    const formatDate = value => value ? Qaute.formatDateTime(value) : '—';
    const showError = message => {
        error.textContent = message;
        error.classList.remove('d-none');
    };
    const api = url => Qaute.fetchJson(url).then(response => response.data);

    function renderMessages(items) {
        messages.replaceChildren();
        if (!items.length) {
            messages.append(text('div', 'Phiên chat chưa có tin nhắn.', 'text-secondary'));
            return;
        }
        items.forEach(item => {
            const row = document.createElement('div');
            row.className = 'my-2 d-flex ' +
                (item.senderType === 'STUDENT' ? 'justify-content-end' : 'justify-content-start');
            const bubble = document.createElement('div');
            bubble.className = 'rounded p-2 bg-light';
            bubble.style.maxWidth = '85%';
            bubble.append(text('strong', item.senderName || item.senderType, 'd-block small'));
            if (item.assetId) {
                const link = document.createElement('a');
                link.href = context + '/api/media/' + encodeURIComponent(item.assetId) + '/download';
                link.textContent = item.content || 'Tệp đính kèm';
                link.target = '_blank';
                link.rel = 'noopener';
                bubble.append(link);
            } else {
                bubble.append(text('div', item.content, 'text-break'));
            }
            bubble.append(text('small', formatDate(item.sentAt), 'd-block text-secondary mt-1'));
            row.append(bubble);
            messages.append(row);
        });
        messages.scrollTop = messages.scrollHeight;
    }

    async function openSession(item) {
        document.getElementById('chat-history-title').textContent =
            item.studentName || 'Sinh viên';
        document.getElementById('chat-history-subtitle').textContent =
            [item.managerName || 'Chưa phân công', item.categoryName, item.question,
                formatDate(item.startedAt)].filter(Boolean).join(' · ');
        messages.replaceChildren(text('div', 'Đang tải hội thoại…', 'text-secondary'));
        error.classList.add('d-none');
        try {
            let cursor = 0;
            let batch;
            const all = [];
            do {
                batch = await api(context + '/api/chat/sessions/' + item.id +
                    '/messages?afterId=' + cursor);
                batch = batch || [];
                all.push(...batch);
                if (batch.length) cursor = batch[batch.length - 1].id;
            } while (batch.length === 100 || batch.length === 200);
            renderMessages(all);
        } catch (failure) {
            messages.replaceChildren();
            showError(failure.message || 'Không tải được lịch sử hội thoại.');
        }
    }

    async function loadPage(page) {
        error.classList.add('d-none');
        list.replaceChildren(text('div', 'Đang tải danh sách…', 'list-group-item text-secondary'));
        try {
            const data = await api(apiUrl + '?page=' + page);
            state.page = data.number || 0;
            state.totalPages = data.totalPages || 0;
            list.replaceChildren();
            if (!data.content || !data.content.length) {
                list.append(text('div', 'Chưa có lịch sử chat.', 'list-group-item text-secondary'));
            } else {
                data.content.forEach(item => {
                    const button = document.createElement('button');
                    button.type = 'button';
                    button.className = 'list-group-item list-group-item-action text-start';
                    button.append(text('span', item.studentName || 'Sinh viên', 'fw-semibold d-block'));
                    button.append(text('span', item.question || item.categoryName || 'Cuộc hội thoại',
                        'small d-block text-truncate'));
                    button.append(text('span', formatDate(item.startedAt) + ' · ' +
                        (item.managerName || 'Chưa phân công') + ' · ' + (item.status || ''),
                        'small text-secondary'));
                    button.addEventListener('click', () => openSession(item));
                    list.append(button);
                });
            }
            document.getElementById('chat-history-page').textContent =
                'Trang ' + (state.page + 1) + ' / ' + Math.max(state.totalPages, 1);
            document.getElementById('chat-history-prev').disabled = state.page <= 0;
            document.getElementById('chat-history-next').disabled =
                state.totalPages === 0 || state.page >= state.totalPages - 1;
        } catch (failure) {
            list.replaceChildren();
            showError(failure.message || 'Không tải được danh sách lịch sử chat.');
        }
    }

    document.getElementById('chat-history-prev').addEventListener('click', () => {
        if (state.page > 0) loadPage(state.page - 1);
    });
    document.getElementById('chat-history-next').addEventListener('click', () => {
        if (state.page + 1 < state.totalPages) loadPage(state.page + 1);
    });
    loadPage(0);
})();
