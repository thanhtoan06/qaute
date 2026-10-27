// Chuyển đổi trạng thái thanh điều hướng trên máy tính và thiết bị di động.
    function toggleSidebar() {
        var toggle = document.getElementById('stuSidebarToggle');
        if (window.innerWidth >= 992) {
            document.body.classList.toggle('stu-sidebar-collapsed');
            var desktopCollapsed = document.body.classList.contains('stu-sidebar-collapsed');
            toggle.setAttribute('aria-expanded', String(!desktopCollapsed));
            toggle.setAttribute('aria-label', desktopCollapsed ? 'Mở rộng menu' : 'Thu gọn menu');
            toggle.setAttribute('title', desktopCollapsed ? 'Mở rộng menu' : 'Thu gọn menu');
            toggle.innerHTML = '<i class="bi ' + (desktopCollapsed ? 'bi-layout-sidebar-inset' : 'bi-layout-sidebar') + '"></i>';
            return;
        }
        document.getElementById('stuSidebar').classList.toggle('open');
        document.getElementById('stuSidebarOverlay').classList.toggle('open');
        var mobileOpen = document.getElementById('stuSidebar').classList.contains('open');
        toggle.setAttribute('aria-expanded', String(mobileOpen));
        toggle.setAttribute('aria-label', mobileOpen ? 'Đóng menu' : 'Mở menu');
        toggle.setAttribute('title', mobileOpen ? 'Đóng menu' : 'Mở menu');
        toggle.innerHTML = '<i class="bi ' + (mobileOpen ? 'bi-x-lg' : 'bi-list') + '"></i>';
    }
    // Đóng thanh điều hướng trên thiết bị di động.
    function closeSidebar() {
        document.getElementById('stuSidebar').classList.remove('open');
        document.getElementById('stuSidebarOverlay').classList.remove('open');
    }

    // Đồng bộ số thông báo giữa huy hiệu trên cùng và thanh bên.
    (function syncBadge() {
        const topBadge = document.getElementById('notification-badge');
        const sideBadge = document.getElementById('sidebar-notif-badge');
        if (!topBadge || !sideBadge) return;
        // Cập nhật huy hiệu bên khi trạng thái thông báo thay đổi.
        const obs = new MutationObserver(() => {
            const hidden = topBadge.classList.contains('d-none');
            const count  = topBadge.textContent.trim();
            if (!hidden && count && count !== '0') {
                sideBadge.textContent = count;
                sideBadge.classList.remove('d-none');
            } else {
                sideBadge.classList.add('d-none');
            }
        });
        obs.observe(topBadge, { attributes: true, childList: true, subtree: true, characterData: true });
    })();

    // Đăng ký xử lý điều hướng sau khi tài liệu tải xong.
    document.addEventListener('DOMContentLoaded', function () {
        var sidebarToggle = document.getElementById('stuSidebarToggle');
        var sidebarOverlay = document.getElementById('stuSidebarOverlay');
        if (sidebarToggle) sidebarToggle.addEventListener('click', toggleSidebar);
        if (sidebarOverlay) sidebarOverlay.addEventListener('click', closeSidebar);

        // Duyệt các liên kết điều hướng của sinh viên.
        document.querySelectorAll('.stu-nav a').forEach(function (link) {
            // Đóng thanh điều hướng sau khi chọn một liên kết.
            link.addEventListener('click', function () {
                closeSidebar();
            });
        });
    });