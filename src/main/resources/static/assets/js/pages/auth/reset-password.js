// Khởi tạo xác thực OTP và các tiện ích đặt lại mật khẩu.
(function () {
    "use strict";

    // 1. Xử lý Input & Đếm ngược cho bước OTP
    const otpInput = document.getElementById("otp");
    if (otpInput) {
        // Giới hạn mã OTP chỉ gồm sáu chữ số.
        otpInput.addEventListener("input", function () {
            this.value = this.value.replace(/\D/g, "").slice(0, 6);
        });
    }

    const timerEl = document.getElementById("otp-timer");
    if (timerEl) {
        const expiredAlert = document.getElementById("otp-expired-alert");
        const verifyBtn = document.getElementById("verify-submit-btn");
        const resendBtn = document.getElementById("resend-otp-btn");
        const resendText = document.getElementById("resend-text");

        const identifier = timerEl.dataset.otpIdentifier;
        const ttlSeconds = Number(timerEl.dataset.ttlMinutes) * 60;
        const cooldownSeconds = Number(timerEl.dataset.cooldownSeconds);

        const storageKey = "qaute_otp_sent_" + encodeURIComponent(identifier);
        let sentAt = parseInt(sessionStorage.getItem(storageKey), 10);
        const now = Date.now();

        if (!sentAt || (now - sentAt) > ttlSeconds * 1000) {
            sentAt = now;
            sessionStorage.setItem(storageKey, sentAt.toString());
        }

        const resendForm = document.getElementById("resend-otp-form");
        if (resendForm) {
            // Lưu thời điểm gửi lại OTP để cập nhật thời gian đếm ngược.
            resendForm.addEventListener("submit", function () {
                sessionStorage.setItem(storageKey, Date.now().toString());
            });
        }

        // Định dạng số giây còn lại thành phút và giây.
        function formatTime(secs) {
            const m = Math.floor(secs / 60);
            const s = secs % 60;
            return (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;
        }

        // Cập nhật thời gian OTP và trạng thái xác minh, gửi lại mã.
        function tick() {
            const elapsed = Math.floor((Date.now() - sentAt) / 1000);
            const remainingTtl = Math.max(0, ttlSeconds - elapsed);
            const remainingCooldown = Math.max(0, cooldownSeconds - elapsed);

            timerEl.textContent = formatTime(remainingTtl);

            if (remainingTtl <= 0) {
                timerEl.classList.remove("text-primary");
                timerEl.classList.add("text-danger");
                if (expiredAlert) expiredAlert.classList.remove("d-none");
                if (verifyBtn) {
                    verifyBtn.disabled = true;
                    verifyBtn.classList.add("disabled");
                }
            } else {
                timerEl.classList.add("text-primary");
                timerEl.classList.remove("text-danger");
                if (expiredAlert) expiredAlert.classList.add("d-none");
                if (verifyBtn) {
                    verifyBtn.disabled = false;
                    verifyBtn.classList.remove("disabled");
                }
            }

            if (remainingCooldown > 0) {
                if (resendBtn) resendBtn.disabled = true;
                if (resendText) {
                    resendText.textContent = "Gửi lại mã OTP (" + remainingCooldown + "s)";
                }
            } else {
                if (resendBtn) resendBtn.disabled = false;
                if (resendText) {
                    resendText.textContent = "Gửi lại mã OTP";
                }
            }
        }

        tick();
        setInterval(tick, 1000);
    }

    // 2. Xử lý Toggle Password & Password Strength cho bước Đặt mật khẩu mới
    // Gắn thao tác hiện hoặc ẩn mật khẩu cho từng nút.
    document.querySelectorAll("[data-toggle-password]").forEach(function (button) {
        // Đổi kiểu hiển thị mật khẩu và biểu tượng của nút.
        button.addEventListener("click", function () {
            const targetSelector = button.getAttribute("data-toggle-password");
            const input = document.querySelector(targetSelector);
            if (!input) return;
            const isPassword = input.type === "password";
            input.type = isPassword ? "text" : "password";
            const icon = button.querySelector("i");
            if (icon) {
                icon.className = isPassword ? "bi bi-eye-slash" : "bi bi-eye";
            }
        });
    });

    const newPassword = document.getElementById("newPassword");
    const confirmPassword = document.getElementById("confirmPassword");
    const strength = document.getElementById("password-strength");
    const strengthText = document.getElementById("password-strength-text");

    if (newPassword && strength && strengthText) {
        // Cập nhật điểm mạnh và thanh đánh giá mật khẩu.
        function updateStrength() {
            const value = newPassword.value;
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
            if (confirmPassword && confirmPassword.value) {
                if (newPassword.value !== confirmPassword.value) {
                    confirmPassword.classList.add("is-invalid");
                } else {
                    confirmPassword.classList.remove("is-invalid");
                }
            }
        }

        // Cập nhật độ mạnh và đối chiếu khi mật khẩu thay đổi.
        newPassword.addEventListener("input", function () {
            updateStrength();
            updateMatch();
        });

        if (confirmPassword) {
            confirmPassword.addEventListener("input", updateMatch);
        }
    }
})();