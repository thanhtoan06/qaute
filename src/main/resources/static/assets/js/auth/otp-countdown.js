// Khởi tạo bộ đếm gửi lại mã và lọc mã OTP.
(function () {
    "use strict";

    const resend = document.getElementById("resend-otp");
    const otp = document.getElementById("otp");
    if (!resend) {
        return;
    }

    const defaultLabel = "Gửi lại mã";
    let seconds = Number.parseInt(resend.dataset.seconds || "0", 10);
    // Cập nhật trạng thái nút gửi lại theo thời gian còn lại.
    function update() {
        if (seconds > 0) {
            resend.disabled = true;
            resend.textContent = defaultLabel + " (" + seconds + "s)";
            seconds--;
        } else {
            resend.disabled = false;
            resend.textContent = defaultLabel;
        }
    }
    update();
    const timer = window.setInterval(
        // Cập nhật bộ đếm mỗi giây và dừng khi hết thời gian.
        function () {
            update();
            if (seconds <= 0) {
                window.clearInterval(timer);
            }
        },
        1000
    );

    if (otp) {
        otp.addEventListener("input",
            // Chỉ giữ tối đa sáu chữ số trong mã OTP.
            function () {
                otp.value = otp.value.replace(/\D/g, "").slice(0, 6);
            }
        );
    }
})();
