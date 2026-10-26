(function (document) {
    'use strict';

    document.querySelectorAll('[data-toggle-password]').forEach(function (button) {
        const input = document.querySelector(button.dataset.togglePassword);
        const icon = button.querySelector('i');
        if (!input || !icon) return;

        button.addEventListener('click', function () {
            const visible = input.type === 'password';
            input.type = visible ? 'text' : 'password';
            icon.className = visible ? 'bi bi-eye-slash' : 'bi bi-eye';
            button.setAttribute('aria-label', visible ? 'Ẩn mật khẩu' : 'Hiện mật khẩu');
            button.setAttribute('aria-pressed', String(visible));
        });
    });
})(document);
