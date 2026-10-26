(function (window, document) {
    'use strict';

    // Lấy số sao đang được chọn trong biểu mẫu.
    function selectedRating(form) {
        return Number(form.querySelector('input[name="rating"]:checked')?.value || 0);
    }

    // Đồng bộ giao diện sao và yêu cầu nhận xét.
    function updateRatingState(form) {
        const rating = selectedRating(form);
        const comment = form.querySelector('#ticket-review-comment');
        const label = form.querySelector('#review-comment-label');
        const hint = form.querySelector('#review-comment-hint');
        form.querySelectorAll('.review-star').forEach(function (star) {
            const active = Number(star.dataset.rating) <= rating;
            star.classList.toggle('btn-warning', active);
            star.classList.toggle('btn-outline-warning', !active);
        });
        const needsComment = rating > 0 && rating <= 2;
        comment.required = needsComment;
        label.classList.toggle('required', needsComment);
        hint.textContent = needsComment
            ? 'Đánh giá từ 1 đến 2 sao cần nhận xét ít nhất 20 ký tự.'
            : 'Tối đa 500 ký tự.';
    }

    // Khởi tạo biểu mẫu đánh giá sau khi trang tải xong.
    document.addEventListener('DOMContentLoaded', function () {
        const form = document.querySelector('#ticket-review-form');
        if (!form) return;
        const editButton = document.querySelector('[data-review-edit]');
        // Mở biểu mẫu để chỉnh sửa đánh giá hiện có.
        editButton?.addEventListener('click', function () {
            form.classList.remove('d-none');
            editButton.classList.add('d-none');
            form.querySelector('input[name="rating"]:checked, input[name="rating"]')?.focus();
        });
        form.querySelectorAll('input[name="rating"]').forEach(function (input) {
            input.addEventListener('change', function () { updateRatingState(form); });
        });
        // Ngăn gửi đánh giá sao thấp nếu nhận xét chưa đủ dài.
        form.addEventListener('submit', function (event) {
            const rating = selectedRating(form);
            const comment = form.querySelector('#ticket-review-comment');
            if (rating > 0 && rating <= 2 && comment.value.trim().length < 20) {
                event.preventDefault();
                comment.classList.add('is-invalid');
                comment.focus();
                return;
            }
            comment.classList.remove('is-invalid');
        });
        updateRatingState(form);
    });
})(window, document);
