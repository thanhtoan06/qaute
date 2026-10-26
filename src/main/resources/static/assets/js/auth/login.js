// Khởi tạo các xử lý đăng nhập và hiển thị mật khẩu.
(function () {
    "use strict";

    const form = document.getElementById("login-form");
    if (!form) {
        return;
    }
    document.querySelectorAll("[data-toggle-password]").forEach(
        // Gắn xử lý ẩn/hiện mật khẩu cho từng nút.
        function (button) {
            button.addEventListener("click",
                // Đổi trạng thái hiển thị mật khẩu khi bấm nút.
                function () {
                    const input = document.querySelector(button.dataset.togglePassword);
                    input.type = input.type === "password" ? "text" : "password";
                    button.querySelector("i").className = input.type === "password"
                        ? "bi bi-eye" : "bi bi-eye-slash";
                }
            );
        }
    );
    form.addEventListener("submit",
        // Khóa nút gửi và cập nhật nhãn khi biểu mẫu được gửi.
        function () {
            const submit = document.getElementById("login-submit");
            submit.disabled = true;
            submit.textContent = "Đang đăng nhập...";
        }
    );
})();
