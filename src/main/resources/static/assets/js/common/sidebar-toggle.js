// Khởi tạo thao tác thu gọn thanh bên nếu trang có nút tương ứng.
    (function () {
        var toggles = document.querySelectorAll("[data-qa-sidebar-toggle]");
        if (!toggles.length) {
            return;
        }
        var page = document.querySelector(".page");
        if (!page) {
            return;
        }
        // Gắn trình xử lý cho từng nút thu gọn thanh bên.
        toggles.forEach(function (toggle) {
            // Đồng bộ trạng thái thu gọn với nhãn truy cập của nút.
            toggle.addEventListener("click", function () {
                var collapsed = page.classList.toggle("qa-sidebar-collapsed");
                toggle.setAttribute("aria-expanded", String(!collapsed));
                toggle.setAttribute("aria-label", collapsed
                    ? "Mở menu" : "Thu gọn menu");
            });
        });
    }());