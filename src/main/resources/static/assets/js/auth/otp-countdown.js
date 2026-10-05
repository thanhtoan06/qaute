(function () {
    "use strict";

    const resend = document.getElementById("resend-otp");
    const otp = document.getElementById("otp");
    if (!resend) {
        return;
    }

    const defaultLabel = "Gửi lại mã";
    let seconds = Number.parseInt(resend.dataset.seconds || "0", 10);
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
    const timer = window.setInterval(function () {
        update();
        if (seconds <= 0) {
            window.clearInterval(timer);
        }
    }, 1000);

    if (otp) {
        otp.addEventListener("input", function () {
            otp.value = otp.value.replace(/\D/g, "").slice(0, 6);
        });
    }
})();
