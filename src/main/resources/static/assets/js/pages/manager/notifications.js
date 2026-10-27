(function () {
        function relativeTime(value) {
            var date = new Date(value);
            if (Number.isNaN(date.getTime())) return value;
            var seconds = Math.max(0, Math.floor((Date.now() - date.getTime()) / 1000));
            if (seconds < 60) return 'Vừa xong';
            if (seconds < 3600) return Math.floor(seconds / 60) + ' phút trước';
            if (seconds < 86400) return Math.floor(seconds / 3600) + ' giờ trước';
            if (seconds < 604800) return Math.floor(seconds / 86400) + ' ngày trước';
            return date.toLocaleDateString('vi-VN', {day: '2-digit', month: '2-digit', year: 'numeric'});
        }

        document.querySelectorAll('.qa-page-manager-notifications [data-relative-time]').forEach(function (element) {
            element.textContent = relativeTime(element.dataset.relativeTime);
        });

        document.querySelectorAll('.qa-page-manager-notifications .notification-list-item').forEach(function (item, index) {
            item.style.setProperty('--notification-delay', (index * 70) + 'ms');
            requestAnimationFrame(function () {
                item.classList.add('is-visible');
            });
        });
    }());