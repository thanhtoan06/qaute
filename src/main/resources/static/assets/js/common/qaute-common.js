(function (window, document) {
    'use strict';

    const Qaute = {};

    Qaute.csrfToken = function () {
        const element = document.querySelector('meta[name="_csrf"]');
        return element ? element.getAttribute('content') : '';
    };

    Qaute.csrfHeader = function () {
        const element = document.querySelector('meta[name="_csrf_header"]');
        return element ? element.getAttribute('content') : 'X-XSRF-TOKEN';
    };

    Qaute.fetchJson = function (url, options) {
        const requestOptions = Object.assign({}, options || {});
        const headers = Object.assign({
            Accept: 'application/json',
            'Content-Type': 'application/json'
        }, requestOptions.headers || {});
        headers[Qaute.csrfHeader()] = Qaute.csrfToken();
        requestOptions.headers = headers;

        return fetch(url, requestOptions).then(function (response) {
            return response.text().then(function (text) {
                let data = {};
                if (text) {
                    try {
                        data = JSON.parse(text);
                    } catch (error) {
                        data = {message: text};
                    }
                }
                if (!response.ok) {
                    const exception = new Error(data.message || 'Có lỗi xảy ra, vui lòng thử lại.');
                    exception.status = response.status;
                    exception.data = data;
                    throw exception;
                }
                return data;
            });
        });
    };

    Qaute.toast = function (message, type) {
        const toastType = type || 'info';
        let container = document.querySelector('.qa-toast-container');
        if (!container) {
            container = document.createElement('div');
            container.className = 'qa-toast-container';
            document.body.appendChild(container);
        }
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-bg-' + toastType + ' border-0';
        toast.setAttribute('role', 'alert');
        toast.setAttribute('aria-live', 'assertive');
        toast.setAttribute('aria-atomic', 'true');
        toast.innerHTML = '<div class="d-flex"><div class="toast-body"></div>' +
            '<button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Đóng"></button></div>';
        toast.querySelector('.toast-body').textContent = message || '';
        container.appendChild(toast);
        if (window.bootstrap && window.bootstrap.Toast) {
            const instance = window.bootstrap.Toast.getOrCreateInstance(toast, {delay: 4000});
            toast.addEventListener('hidden.bs.toast', function () {
                toast.remove();
            });
            instance.show();
        } else {
            toast.classList.add('show');
            window.setTimeout(function () {
                toast.remove();
            }, 4000);
        }
    };

    Qaute.debounce = function (fn, ms) {
        let timeout;
        return function () {
            const context = this;
            const args = arguments;
            window.clearTimeout(timeout);
            timeout = window.setTimeout(function () {
                fn.apply(context, args);
            }, ms);
        };
    };

    Qaute.escapeHtml = function (value) {
        const element = document.createElement('div');
        element.textContent = value == null ? '' : String(value);
        return element.innerHTML;
    };

    Qaute.formatDateTime = function (isoString) {
        const date = new Date(isoString);
        if (Number.isNaN(date.getTime())) {
            return '';
        }
        const pad = function (number) {
            return String(number).padStart(2, '0');
        };
        return pad(date.getDate()) + '/' + pad(date.getMonth() + 1) + '/' +
            date.getFullYear() + ' ' + pad(date.getHours()) + ':' + pad(date.getMinutes());
    };

    Qaute.confirmSubmit = function (form) {
        const question = form.getAttribute('data-confirm');
        if (question && !window.confirm(question)) {
            return false;
        }
        return true;
    };

    document.addEventListener('submit', function (event) {
        if (!Qaute.confirmSubmit(event.target)) {
            event.preventDefault();
        }
    });

    window.Qaute = Qaute;
}(window, document));
