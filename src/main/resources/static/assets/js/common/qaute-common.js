// Khởi tạo các tiện ích dùng chung cho giao diện.
(function (window, document) {
    'use strict';

    const Qaute = window.Qaute || {};

    // Lấy mã CSRF từ thẻ meta của trang.
    Qaute.csrfToken = function () {
        const element = document.querySelector('meta[name="_csrf"]');
        return element ? element.getAttribute('content') : '';
    };

    // Lấy tên header CSRF từ thẻ meta của trang.
    Qaute.csrfHeader = function () {
        const element = document.querySelector('meta[name="_csrf_header"]');
        return element ? element.getAttribute('content') : 'X-XSRF-TOKEN';
    };

    let sessionRefresh;

    // Giữ phiên đăng nhập còn hiệu lực trước mỗi lần thiết lập WebSocket.
    Qaute.refreshSessionIfNeeded = function () {
        if (!sessionRefresh) {
            sessionRefresh = fetch('/auth/refresh-if-needed', {
                method: 'POST',
                credentials: 'same-origin',
                headers: {
                    [Qaute.csrfHeader()]: Qaute.csrfToken()
                }
            }).then(function (response) {
                if (!response.ok) {
                    throw new Error(response.status === 401
                        ? 'Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.'
                        : 'Không thể xác thực kết nối chat.');
                }
            }).finally(function () {
                sessionRefresh = null;
            });
        }
        return sessionRefresh;
    };

    // Gửi yêu cầu JSON và xử lý phản hồi của máy chủ.
    Qaute.fetchJson = function (url, options) {
        const requestOptions = Object.assign({}, options || {});
        const headers = Object.assign({
            Accept: 'application/json',
            'Content-Type': 'application/json'
        }, requestOptions.headers || {});
        headers[Qaute.csrfHeader()] = Qaute.csrfToken();
        requestOptions.headers = headers;

        // Xử lý phản hồi HTTP và đọc nội dung trả về.
        return fetch(url, requestOptions).then(function (response) {
            // Phân tích nội dung và chuyển lỗi HTTP thành ngoại lệ.
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

    // Hiển thị thông báo ngắn cho người dùng.
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
            // Xóa phần tử sau khi thông báo đóng.
            toast.addEventListener('hidden.bs.toast', function () {
                toast.remove();
            });
            instance.show();
        } else {
            toast.classList.add('show');
            // Tự động xóa thông báo sau thời gian hiển thị.
            window.setTimeout(function () {
                toast.remove();
            }, 4000);
        }
    };

    // Trì hoãn gọi hàm đến khi người dùng ngừng thao tác.
    Qaute.debounce = function (fn, ms) {
        let timeout;
        // Giữ ngữ cảnh và tham số cho lần gọi bị trì hoãn.
        return function () {
            const context = this;
            const args = arguments;
            window.clearTimeout(timeout);
            // Gọi hàm sau khoảng thời gian chờ.
            timeout = window.setTimeout(function () {
                fn.apply(context, args);
            }, ms);
        };
    };

    // Mã hóa nội dung trước khi đưa vào HTML.
    Qaute.escapeHtml = function (value) {
        const element = document.createElement('div');
        element.textContent = value == null ? '' : String(value);
        return element.innerHTML;
    };

    // Định dạng ngày giờ theo cách hiển thị của trang.
    Qaute.formatDateTime = function (isoString) {
        const value = isoString == null ? '' : String(isoString);
        const localDateTime = value.match(/^(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})(\.\d+)?$/);
        const normalizedValue = localDateTime
            ? localDateTime[1] + (localDateTime[2] ? localDateTime[2].slice(0, 4).padEnd(4, '0') : '')
            : value;
        const date = new Date(normalizedValue);
        if (Number.isNaN(date.getTime())) {
            return '';
        }
        // Bổ sung số 0 cho các giá trị ngày giờ một chữ số.
        const pad = function (number) {
            return String(number).padStart(2, '0');
        };
        return pad(date.getDate()) + '/' + pad(date.getMonth() + 1) + '/' +
            date.getFullYear() + ' ' + pad(date.getHours()) + ':' + pad(date.getMinutes());
    };

    // Định dạng các mốc thời gian trong nội dung trang.
    function formatDateTimes() {
        document.querySelectorAll('time[data-qaute-datetime]').forEach(function (element) {
            const formatted = Qaute.formatDateTime(element.dateTime);
            if (formatted) {
                element.textContent = formatted;
            }
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', formatDateTimes);
    } else {
        formatDateTimes();
    }

    // Kiểm tra xác nhận trước khi gửi biểu mẫu.
    Qaute.confirmSubmit = function (form) {
        const question = form.getAttribute('data-confirm');
        if (question && !window.confirm(question)) {
            return false;
        }
        return true;
    };

    // Ngăn gửi biểu mẫu khi xác nhận bị từ chối.
    document.addEventListener('submit', function (event) {
        if (!Qaute.confirmSubmit(event.target)) {
            event.preventDefault();
        }
    });

    // Xử lý hiệu ứng cuộn trang cho Header.
    function handleNavbarScroll() {
        const navbar = document.querySelector('.hcmute-navbar');
        if (navbar) {
            if (window.scrollY > 20) {
                navbar.classList.add('navbar-scrolled');
            } else {
                navbar.classList.remove('navbar-scrolled');
            }
        }
    }

    window.addEventListener('scroll', handleNavbarScroll);
    document.addEventListener('DOMContentLoaded', handleNavbarScroll);

    window.Qaute = Qaute;
}(window, document));
