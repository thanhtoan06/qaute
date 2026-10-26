// Khởi tạo kiểm tra biểu mẫu đăng ký và mật khẩu.
(function () {
    "use strict";

    const form = document.getElementById("register-form");
    if (!form) {
        return;
    }

    const password = document.getElementById("password");
    const confirmPassword = document.getElementById("confirmPassword");
    const strength = document.getElementById("password-strength");
    const strengthText = document.getElementById("password-strength-text");

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

    // Cập nhật thanh đánh giá độ mạnh của mật khẩu.
    function updateStrength() {
        const value = password.value;
        let score = 0;
        if (value.length >= 8) score++;
        if (/[A-ZÀ-Ỵ]/.test(value) && /[a-zà-ỹ]/.test(value)) score++;
        if (/\d/.test(value)) score++;
        if (/[^A-Za-zÀ-ỹ\d]/.test(value)) score++;
        strength.style.width = (score * 25) + "%";
        strength.className = "progress-bar " + (score <= 1 ? "bg-danger"
            : score === 2 ? "bg-warning" : score === 3 ? "bg-info" : "bg-success");
        strengthText.textContent = score === 0 ? "" : ["Yếu", "Yếu", "Trung bình", "Tốt", "Mạnh"][score];
    }

    // Đánh dấu khi mật khẩu xác nhận không khớp.
    function updateMatch() {
        if (confirmPassword.value && password.value !== confirmPassword.value) {
            confirmPassword.classList.add("is-invalid");
        } else {
            confirmPassword.classList.remove("is-invalid");
        }
    }

    password.addEventListener("input",
        // Cập nhật độ mạnh và trạng thái khớp khi mật khẩu thay đổi.
        function () {
            updateStrength();
            updateMatch();
        }
    );
    confirmPassword.addEventListener("input", updateMatch);

    form.addEventListener("submit",
        // Kiểm tra biểu mẫu và khóa nút gửi nếu dữ liệu hợp lệ.
        function (event) {
            updateMatch();
            const acceptTerms = document.getElementById("acceptTerms");
            if (!form.checkValidity() || password.value !== confirmPassword.value
                || (acceptTerms && !acceptTerms.checked)) {
                event.preventDefault();
                form.classList.add("was-validated");
                return;
            }
            const submit = document.getElementById("register-submit");
            if (submit) {
                submit.disabled = true;
                submit.textContent = submit.getAttribute("data-loading-text") || "Đang đăng ký...";
            }
        }
    );
})();
