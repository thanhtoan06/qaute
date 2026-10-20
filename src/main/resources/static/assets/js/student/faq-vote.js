(function (window, document) {
    'use strict';

    function updateCounts(root, result) {
        document.querySelectorAll('[data-faq-helpful-count]').forEach(function (element) {
            element.textContent = result.helpful;
        });
        document.querySelectorAll('[data-faq-not-helpful-count]').forEach(function (element) {
            element.textContent = result.notHelpful;
        });
    }

    function updateState(root, myVote) {
        const helpful = root.querySelector('[data-faq-vote-helpful]');
        const notHelpful = root.querySelector('[data-faq-vote-not-helpful]');
        const status = root.querySelector('#faq-vote-status');
        helpful.classList.toggle('btn-success', myVote === true);
        helpful.classList.toggle('btn-outline-success', myVote !== true);
        notHelpful.classList.toggle('btn-secondary', myVote === false);
        notHelpful.classList.toggle('btn-outline-secondary', myVote !== false);
        status.textContent = 'Bạn đã bình chọn: ' + (myVote ? 'Hữu ích' : 'Chưa hữu ích') + '. Bạn có thể thay đổi.';
        status.classList.remove('d-none');
    }

    function sendVote(root, payload) {
        return window.Qaute.fetchJson('/student/faq/' + root.dataset.faqId + '/vote', {
            method: 'POST',
            body: JSON.stringify(payload)
        }).then(function (response) {
            const result = response.data;
            updateCounts(root, result);
            updateState(root, result.myVote);
            root.querySelector('#faq-not-helpful-form').classList.add('d-none');
            window.Qaute.toast('Đã ghi nhận đánh giá của bạn.', 'success');
        }).catch(function (error) {
            window.Qaute.toast(error.message || 'Không thể gửi đánh giá.', 'danger');
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        const root = document.querySelector('#faq-vote[data-faq-id]');
        if (!root || !window.Qaute?.fetchJson) return;
        const form = root.querySelector('#faq-not-helpful-form');
        root.querySelector('[data-faq-vote-helpful]').addEventListener('click', function () {
            sendVote(root, {helpful: true});
        });
        root.querySelector('[data-faq-vote-not-helpful]').addEventListener('click', function () {
            form.classList.remove('d-none');
            form.querySelector('#faq-vote-reason').focus();
        });
        root.querySelector('[data-faq-vote-cancel]').addEventListener('click', function () {
            form.classList.add('d-none');
        });
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            const reasonCode = form.querySelector('#faq-vote-reason').value;
            const comment = form.querySelector('#faq-vote-comment').value.trim();
            if (!reasonCode || comment.length < 20) {
                window.Qaute.toast('Vui lòng chọn lý do và nhập nhận xét ít nhất 20 ký tự.', 'warning');
                return;
            }
            sendVote(root, {helpful: false, reasonCode: reasonCode, comment: comment});
        });
    });
})(window, document);
