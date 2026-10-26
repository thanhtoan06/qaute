(function () {
    'use strict';
    // Khởi tạo điều hướng và nội dung ngày trong không gian làm việc.
    function initWorkspace() {
        const body = document.body;
        const sidebar = document.getElementById('workspace-sidebar');
        const toggle = document.querySelector('[data-workspace-toggle]');
        const toggleIcon = toggle?.querySelector('i');
        const overlay = document.querySelector('[data-workspace-close]');
        if (!body.classList.contains('qa-workspace') || !sidebar || !toggle || !toggleIcon || !overlay) return;
        const mobile = window.matchMedia('(max-width: 991.98px)');
        let collapsed = false;
        let menuOpen = false;
        try { collapsed = localStorage.getItem('qaute.workspace.sidebar') === 'collapsed'; } catch (_) { /* Storage is optional. */ }
        // Đồng bộ trạng thái menu với giao diện và thuộc tính trợ năng.
        function sync() {
            const visible = !mobile.matches || menuOpen;
            const expanded = mobile.matches ? menuOpen : !collapsed;
            body.classList.toggle('qa-workspace-collapsed', !mobile.matches && collapsed);
            body.classList.toggle('qa-workspace-menu-open', mobile.matches && menuOpen);
            overlay.hidden = !mobile.matches || !menuOpen;
            sidebar.inert = !visible;
            sidebar.setAttribute('aria-hidden', String(!visible));
            toggle.setAttribute('aria-expanded', String(expanded));
            const label = mobile.matches
                ? (menuOpen ? 'Đóng menu' : 'Mở menu')
                : (collapsed ? 'Mở rộng menu' : 'Thu gọn menu');
            toggle.setAttribute('aria-label', label);
            toggle.setAttribute('title', label);
            toggleIcon.className = 'bi ' + (mobile.matches
                ? (menuOpen ? 'bi-x-lg' : 'bi-list')
                : (collapsed ? 'bi-layout-sidebar-inset' : 'bi-layout-sidebar'));
        }
        // Đóng menu di động và trả tiêu điểm về nút điều khiển.
        function closeMenu() { menuOpen = false; toggle.focus(); sync(); }
        // Xử lý thao tác mở hoặc thu gọn menu điều hướng.
        toggle.addEventListener('click', () => {
            if (mobile.matches) menuOpen = !menuOpen;
            else {
                collapsed = !collapsed;
                try { localStorage.setItem('qaute.workspace.sidebar', collapsed ? 'collapsed' : 'expanded'); } catch (_) { /* Storage is optional. */ }
            }
            sync();
            if (mobile.matches && menuOpen) sidebar.querySelector('a[href]')?.focus();
        });
        overlay.addEventListener('click', closeMenu);
        // Xử lý phím Escape và giữ tiêu điểm trong menu di động.
        document.addEventListener('keydown', event => {
            if (!mobile.matches || !menuOpen) return;
            if (event.key === 'Escape') { event.preventDefault(); closeMenu(); }
            if (event.key === 'Tab') {
                const focusable = Array.from(sidebar.querySelectorAll('a[href], button:not([disabled])'))
                    .filter(element => element.getClientRects().length > 0);
                const first = focusable[0], last = focusable[focusable.length - 1];
                if (event.shiftKey && document.activeElement === first) { event.preventDefault(); last?.focus(); }
                else if (!event.shiftKey && document.activeElement === last) { event.preventDefault(); first?.focus(); }
            }
        });
        // Đóng menu khi điểm ngắt hiển thị thay đổi.
        mobile.addEventListener('change', () => { menuOpen = false; sync(); });
        sidebar.querySelectorAll('.nav-item.active > a[href]:not([href^="#"]), .dropdown-item.active')
            .forEach(link => link.setAttribute('aria-current', 'page'));
        // Hiển thị ngày hiện tại theo múi giờ Việt Nam.
        document.querySelectorAll('[data-workspace-date]').forEach(element => {
            element.textContent = new Intl.DateTimeFormat('vi-VN', {
                weekday: 'long', day: '2-digit', month: '2-digit', year: 'numeric', timeZone: 'Asia/Ho_Chi_Minh'
            }).format(new Date());
        });
        // Định dạng thời điểm hợp lệ trên trang.
        document.querySelectorAll('time[data-workspace-datetime]').forEach(element => {
            const date = new Date(element.dateTime);
            if (!Number.isNaN(date.getTime())) element.textContent = new Intl.DateTimeFormat('vi-VN', {
                day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit'
            }).format(date);
        });
        sync();
    }
    // Configuration tables and small, already loaded histories also need usable pages.
    // Large search screens use their own database pagination.
    // Phân trang các hàng con trong danh sách được chỉ định.
    function paginate(container) {
        const size = 25;
        let page = 0;
        let controls;
        // Cập nhật các hàng hiển thị và điều khiển phân trang.
        function refresh(reset) {
            if (reset) page = 0;
            const rows = Array.from(container.children);
            const total = Math.ceil(rows.length / size);
            page = Math.max(0, Math.min(page, total - 1));
            rows.forEach((row, index) => { row.hidden = total > 1 && Math.floor(index / size) !== page; });
            if (controls) controls.remove();
            if (total <= 1) return;
            controls = document.createElement('nav');
            controls.className = 'd-flex flex-wrap align-items-center gap-2 my-3';
            controls.setAttribute('aria-label', 'Phân trang danh sách');
            // Tạo nút điều hướng đến trang được chỉ định.
            function button(label, target, disabled) {
                const element = document.createElement('button');
                element.type = 'button'; element.className = 'btn btn-sm btn-outline-secondary';
                element.textContent = label; element.disabled = disabled;
                // Chuyển trang rồi làm mới danh sách hiển thị.
                element.addEventListener('click', () => { page = target; refresh(false); });
                return element;
            }
            const label = document.createElement('span');
            label.textContent = 'Trang ' + (page + 1) + '/' + total + ' · ' + rows.length + ' mục';
            controls.append(button('Trước', page - 1, page === 0), label,
                button('Sau', page + 1, page === total - 1));
            const parent = container.tagName === 'TBODY' ? container.closest('.table-responsive') : container;
            parent.after(controls);
        }
        refresh(false);
        // Đặt lại phân trang khi danh sách thay đổi.
        new MutationObserver(() => refresh(true)).observe(container, {childList: true});
    }
    // Khởi tạo các thành phần giao diện trên trang.
    function init() {
        initWorkspace();
        // Bọc bảng chưa có vùng cuộn và phân trang nội dung của bảng.
        document.querySelectorAll('table.table').forEach(table => {
            if (!table.closest('.table-responsive')) {
                const wrapper = document.createElement('div'); wrapper.className = 'table-responsive';
                table.before(wrapper); wrapper.append(table);
            }
            if (table.tBodies[0]) paginate(table.tBodies[0]);
        });
        document.querySelectorAll('[data-tv2-pageable]').forEach(paginate);
        document.querySelectorAll('.modal-dialog').forEach(dialog => dialog.classList.add('modal-dialog-scrollable'));
    }
    if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', init);
    else init();
}());
