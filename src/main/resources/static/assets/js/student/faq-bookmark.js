(function (window, document) {
    'use strict';

    // Cập nhật giao diện theo trạng thái lưu bài viết.
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

    // Khởi tạo các nút lưu bài viết sau khi trang tải xong.
    document.addEventListener('DOMContentLoaded', function () {
        if (!window.Qaute?.fetchJson) return;
        document.querySelectorAll('[data-faq-bookmark][data-faq-id]').forEach(function (button) {
            // Gửi yêu cầu lưu hoặc bỏ lưu bài viết.
            button.addEventListener('click', function () {
                if (button.disabled) return;
                button.disabled = true;
                window.Qaute.fetchJson('/student/faq/' + button.dataset.faqId + '/bookmark', {method: 'POST'})
                    // Đồng bộ trạng thái lưu với phản hồi máy chủ.
                    .then(function (response) {
                        applyState(button, response.data === true);
                    })
                    // Thông báo khi không thể cập nhật trạng thái lưu.
                    .catch(function (error) {
                        window.Qaute.toast(error.message || 'Không thể cập nhật bài đã lưu.', 'danger');
                    })
                    // Mở lại nút sau khi yêu cầu hoàn tất.
                    .finally(function () {
                        button.disabled = false;
                    });
            });
        });
    });
})(window, document);
