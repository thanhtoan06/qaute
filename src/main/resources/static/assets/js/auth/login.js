(function () {
    "use strict";

    const form = document.getElementById("login-form");
    if (!form) {
        return;
    }
    document.querySelectorAll("[data-toggle-password]").forEach(function (button) {
        button.addEventListener("click", function () {
            const input = document.querySelector(button.dataset.togglePassword);
            input.type = input.type === "password" ? "text" : "password";
            button.querySelector("i").className = input.type === "password"
                ? "bi bi-eye" : "bi bi-eye-slash";
        });
    });
    form.addEventListener("submit", function () {
        const submit = document.getElementById("login-submit");
        submit.disabled = true;
        submit.textContent = "Đang đăng nhập...";
    });
})();
