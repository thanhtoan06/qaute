(function (window, document) {
    'use strict';

    function applyState(button, bookmarked) {
        button.dataset.bookmarked = String(bookmarked);
        button.setAttribute('aria-pressed', String(bookmarked));
        button.classList.toggle('btn-danger', bookmarked);
        button.classList.toggle('btn-outline-secondary', !bookmarked);
        button.title = bookmarked ? 'Đã lưu' : 'Lưu bài viết';
        const icon = button.querySelector('i');
        if (icon) {
            icon.classList.toggle('bi-heart-fill', bookmarked);
            icon.classList.toggle('bi-heart', !bookmarked);
        }
    }

    document.addEventListener('DOMContentLoaded', function () {
        if (!window.Qaute?.fetchJson) return;
        document.querySelectorAll('[data-faq-bookmark][data-faq-id]').forEach(function (button) {
            button.addEventListener('click', function () {
                if (button.disabled) return;
                button.disabled = true;
                window.Qaute.fetchJson('/student/faq/' + button.dataset.faqId + '/bookmark', {method: 'POST'})
                    .then(function (response) {
                        applyState(button, response.data === true);
                    })
                    .catch(function (error) {
                        window.Qaute.toast(error.message || 'Không thể cập nhật bài đã lưu.', 'danger');
                    })
                    .finally(function () {
                        button.disabled = false;
                    });
            });
        });
    });
})(window, document);
