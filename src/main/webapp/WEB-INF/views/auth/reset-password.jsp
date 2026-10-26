<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Đặt lại mật khẩu</title>
<style>
.reset-recovery { --reset-blue: #2369c7; --reset-blue-dark: #174b96; --reset-ink: #1c3555; --reset-muted: #7c8ea6; --reset-border: #e0e9f4; position: relative; isolation: isolate; color: var(--reset-ink); animation: qaute-enter .45s cubic-bezier(.2, .75, .3, 1) both; }
.reset-recovery *, .reset-recovery *::before, .reset-recovery *::after { box-sizing: border-box; }
.reset-recovery::before { content: ""; position: absolute; z-index: -1; top: -40px; right: -25px; width: 145px; height: 145px; border: 1px solid rgba(35, 105, 199, .09); border-radius: 50%; box-shadow: 0 0 0 17px rgba(35, 105, 199, .025), 0 0 0 36px rgba(35, 105, 199, .015); pointer-events: none; }
.reset-recovery::after { content: ""; position: absolute; z-index: -1; left: -32px; bottom: -35px; width: 105px; height: 105px; border-radius: 50%; background: radial-gradient(circle, rgba(35, 105, 199, .065), transparent 72%); pointer-events: none; }
.reset-heading { position: relative; margin-bottom: 1.65rem; }
.reset-heading--center { text-align: center; }
.reset-icon-wrap { position: relative; display: inline-flex; align-items: center; justify-content: center; width: 67px; height: 67px; margin-bottom: 1.2rem; border: 1px solid rgba(255, 255, 255, .95); border-radius: 21px; background: linear-gradient(145deg, #ffffff, #eaf3ff); color: var(--reset-blue); box-shadow: 0 9px 24px rgba(35, 105, 199, .16), 0 0 0 8px rgba(35, 105, 199, .035); animation: qaute-auth-icon-enter .5s cubic-bezier(.2, .75, .3, 1) both, qaute-auth-icon-glow 4s ease-in-out 1s infinite; }
.reset-icon-wrap .bi { font-size: 1.8rem; line-height: 1; }
.reset-icon-wrap::after { content: ""; position: absolute; top: 5px; right: 5px; width: 7px; height: 7px; border-radius: 50%; background: #43c59a; box-shadow: 0 0 0 3px rgba(67, 197, 154, .12); }
.reset-eyebrow { display: inline-flex; align-items: center; gap: .45rem; margin-bottom: .8rem; padding: .38rem .72rem; border: 1px solid #dce9fa; border-radius: 999px; background: linear-gradient(135deg, #f4f8ff, #eaf3ff); color: #326db6; font-size: .66rem; font-weight: 800; letter-spacing: .075em; text-transform: uppercase; }
.reset-eyebrow .bi { font-size: .78rem; }
.reset-title { margin: 0 0 .55rem; color: var(--reset-ink); font-size: clamp(1.5rem, 4vw, 1.9rem); font-weight: 850; line-height: 1.25; letter-spacing: -.045em; }
.reset-description { max-width: 440px; margin: 0; color: var(--reset-muted); font-size: .85rem; line-height: 1.8; }
.reset-heading--center .reset-description { margin-right: auto; margin-left: auto; }
.reset-alert { margin-bottom: 1.2rem; padding: .8rem .9rem; border: 1px solid #f3c7ce; border-radius: 12px; background: linear-gradient(120deg, #fff7f8, #fffdfd); color: #a83c4e; box-shadow: 0 4px 12px rgba(173, 57, 78, .04); font-size: .79rem; line-height: 1.65; }
.reset-alert .bi { flex-shrink: 0; align-self: flex-start; margin-top: 2px; font-size: 1rem; }
.reset-form .form-label { display: flex; align-items: center; gap: .35rem; margin-bottom: .55rem; color: #344e6d; font-size: .81rem; font-weight: 750; }
.reset-form .form-label.required::after { color: #e05b6b; }
.reset-form .input-group { position: relative; display: flex; flex-wrap: nowrap; min-height: 51px; border: 1px solid #dce6f2; border-radius: 13px; background: #ffffff; box-shadow: 0 3px 10px rgba(22, 54, 91, .025); transition: border-color .22s ease, box-shadow .22s ease, background .22s ease; }
.reset-form .input-group:hover { border-color: #bfd3eb; box-shadow: 0 4px 14px rgba(22, 54, 91, .045); }
.reset-form .input-group:focus-within { border-color: #6aa0e4; background: #ffffff; box-shadow: 0 0 0 4px rgba(35, 105, 199, .09), 0 7px 17px rgba(22, 54, 91, .045); }
.reset-form .input-group:has(.form-control.is-invalid) { border-color: #e8a5ad; background: #fffafb; }
.reset-form .input-group:has(.form-control.is-invalid):focus-within { border-color: #da7581; box-shadow: 0 0 0 4px rgba(220, 68, 88, .075); }
.reset-form .input-group-text { display: inline-flex; align-items: center; justify-content: center; min-width: 46px; padding: 0 .85rem; border: 0 !important; border-radius: 12px 0 0 12px !important; background: transparent !important; color: #8197b3 !important; font-size: 1.05rem; transition: color .2s ease; }
.reset-form .input-group:focus-within .input-group-text { color: var(--reset-blue) !important; }
.reset-form .form-control { min-width: 0; min-height: 49px; padding: .72rem .4rem .72rem 0; border: 0 !important; border-radius: 0 !important; background: transparent !important; color: #243c5a; outline: 0; box-shadow: none !important; font-size: .86rem; }
.reset-form .form-control::placeholder { color: #a0afc1; opacity: 1; }
.reset-form .form-control.is-invalid { background-image: none; }
.reset-form .input-group button[data-toggle-password] { display: inline-flex; align-items: center; justify-content: center; flex: 0 0 45px; width: 45px; min-height: 49px; padding: .5rem; border: 0 !important; border-radius: 0 12px 12px 0 !important; background: transparent !important; color: #8294ab !important; box-shadow: none !important; transition: color .2s ease, background .2s ease; cursor: pointer; }
.reset-form .input-group button[data-toggle-password]:hover { color: var(--reset-blue) !important; background: #f3f7fd !important; }
.reset-form .input-group button[data-toggle-password] .bi { font-size: 1.05rem; }
.reset-form .invalid-feedback, .reset-form .form-hint { font-size: .76rem; line-height: 1.6; }
.reset-form .progress { height: 5px !important; margin-top: .75rem !important; overflow: hidden; border-radius: 99px; background: #eaf0f8; }
.reset-form .progress-bar { border-radius: 99px; transition: width .3s ease, background-color .3s ease; }
.reset-form #password-strength-text { display: block; min-height: 1.2em; margin-top: .4rem; color: #8999ad; font-size: .72rem !important; line-height: 1.5; }
.reset-form .reset-submit { position: relative; display: flex; align-items: center; justify-content: center; gap: .5rem; min-height: 50px; padding: .75rem 1.1rem; overflow: hidden; border: 1px solid #2467c5; border-radius: 13px !important; background: linear-gradient(115deg, #2d78d4 0%, #205eb8 52%, #184e9d 100%) !important; color: #ffffff; box-shadow: 0 7px 18px rgba(35, 105, 199, .2), inset 0 1px 0 rgba(255, 255, 255, .15); font-size: .86rem; font-weight: 750; transition: transform .22s ease, box-shadow .22s ease, filter .22s ease; }
.reset-form .reset-submit::before { content: ""; position: absolute; top: 0; left: -120%; width: 70%; height: 100%; background: linear-gradient(105deg, transparent, rgba(255, 255, 255, .2), transparent); transform: skewX(-18deg); pointer-events: none; }
.reset-form .reset-submit:hover:not(:disabled) { color: #ffffff; filter: saturate(1.08); transform: translateY(-2px); box-shadow: 0 11px 24px rgba(35, 105, 199, .27), inset 0 1px 0 rgba(255, 255, 255, .2); }
.reset-form .reset-submit:hover:not(:disabled)::before { animation: qaute-shine .75s ease; }
.reset-form .reset-submit:active:not(:disabled) { transform: translateY(0) scale(.99); }
.reset-form .reset-submit:disabled { opacity: 0.6; cursor: not-allowed; }
.reset-form .reset-submit:focus-visible { outline-offset: 4px; }
.reset-form .reset-submit .bi { font-size: 1rem; }
.reset-assurance { display: flex; align-items: flex-start; gap: .65rem; margin-top: 1.2rem; padding: .85rem .9rem; border: 1px solid #e2ecf8; border-radius: 12px; background: linear-gradient(125deg, rgba(245, 249, 255, .95), rgba(255, 255, 255, .9)); }
.reset-assurance-icon { display: inline-flex; align-items: center; justify-content: center; flex: 0 0 32px; width: 32px; height: 32px; border-radius: 10px; background: #e5f1ff; color: #3478c8; font-size: .95rem; }
.reset-assurance-copy { min-width: 0; }
.reset-assurance-title { margin: 0 0 .2rem; color: #385571; font-size: .75rem; font-weight: 750; }
.reset-assurance-text { margin: 0; color: #8595a9; font-size: .7rem; line-height: 1.6; }
.reset-otp-form { margin-bottom: 1rem; }
.reset-otp-label { justify-content: center; margin-bottom: .7rem !important; text-align: center; }
.reset-otp-input { min-height: 68px !important; padding: .7rem .5rem !important; border: 1px solid #dce6f2 !important; border-radius: 14px !important; background: linear-gradient(145deg, #ffffff, #f8fbff) !important; color: #205aa7 !important; box-shadow: 0 4px 13px rgba(22, 54, 91, .035) !important; font-size: clamp(1.5rem, 5vw, 2rem) !important; font-weight: 850 !important; letter-spacing: .35em !important; font-variant-numeric: tabular-nums; transition: border-color .22s ease, box-shadow .22s ease, background .22s ease; }
.reset-otp-input::placeholder { color: #c3d1e2 !important; opacity: 1; }
.reset-otp-input:hover { border-color: #bfd3eb !important; }
.reset-otp-input:focus { border-color: #6aa0e4 !important; background: #ffffff !important; box-shadow: 0 0 0 4px rgba(35, 105, 199, .09), 0 7px 18px rgba(22, 54, 91, .05) !important; }
.reset-otp-input.is-invalid { border-color: #e8a5ad !important; background: #fffafb !important; }
.reset-otp-hint { display: flex; align-items: center; justify-content: center; gap: .4rem; margin-top: .7rem; color: #8a9bb0; font-size: .72rem; line-height: 1.6; text-align: center; }
.reset-otp-hint .bi { flex-shrink: 0; color: #7296c3; }
.reset-otp-countdown { display: flex; flex-direction: column; align-items: center; justify-content: center; }
.reset-otp-badge { display: inline-flex; align-items: center; gap: 8px; padding: 7px 14px; border: 1px solid #dbe6f4; border-radius: 999px; background: linear-gradient(135deg, #f8fbff, #edf4fd); }
.reset-resend { display: flex; align-items: center; justify-content: center; margin: 0 0 1rem; }
.reset-resend .btn-link { display: inline-flex; align-items: center; justify-content: center; gap: .4rem; min-height: 38px; padding: .4rem .7rem; border-radius: 9px; color: #286bbd; font-size: .78rem; font-weight: 750; text-decoration: none !important; transition: color .2s ease, background .2s ease; }
.reset-resend .btn-link:hover:not(:disabled) { color: #174b96; background: #edf4ff; }
.reset-resend .btn-link:disabled { color: #9bb0c7 !important; cursor: not-allowed; }
.reset-back { display: flex; align-items: center; justify-content: center; gap: .4rem; color: #8191a6; font-size: .77rem; line-height: 1.7; }
.reset-back a { display: inline-flex; align-items: center; gap: .35rem; color: #637d9b !important; text-decoration: none !important; transition: color .2s ease, gap .2s ease; }
.reset-back a:hover { gap: .55rem; color: #205bc0 !important; }
.reset-back .bi { font-size: .9rem; }
.reset-divider { height: 1px; margin: 1.35rem 0 1rem; border: 0; background: linear-gradient(90deg, transparent, #e1eaf4 18%, #e1eaf4 82%, transparent); }
@media (max-width: 575.98px) {
  .reset-recovery::before { top: -25px; right: -24px; width: 105px; height: 105px; }
  .reset-recovery::after { left: -20px; bottom: -25px; width: 75px; height: 75px; }
  .reset-heading { margin-bottom: 1.35rem; }
  .reset-title { font-size: 1.5rem; }
  .reset-icon-wrap { width: 60px; height: 60px; margin-bottom: 1rem; border-radius: 18px; }
  .reset-icon-wrap .bi { font-size: 1.6rem; }
  .reset-form .input-group { min-height: 48px; }
  .reset-form .input-group-text { min-width: 42px; padding-right: .7rem; padding-left: .7rem; }
  .reset-form .form-control, .reset-form .input-group button[data-toggle-password] { min-height: 46px; }
  .reset-form .reset-submit { min-height: 48px; }
  .reset-otp-input { min-height: 61px !important; letter-spacing: .28em !important; }
  .reset-assurance { padding: .75rem; }
}
@media (prefers-reduced-motion: reduce) {
  .reset-recovery, .reset-recovery *, .reset-recovery *::before, .reset-recovery *::after { animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>

<%-- Hiển thị bước xác minh OTP hoặc bước thiết lập mật khẩu mới. --%>
<div class="reset-recovery">
    <c:choose>
        <c:when test="${step == 'otp'}">
            <%-- Nhập OTP, theo dõi thời hạn và gửi lại mã khi hết thời gian chờ. --%>
            <div class="reset-heading reset-heading--center">
                <div class="reset-icon-wrap">
                    <i class="bi bi-key-fill"></i>
                </div>
                <div class="reset-eyebrow">
                    <i class="bi bi-shield-check"></i>
                    Xác minh danh tính
                </div>
                <h3 class="reset-title">Xác thực mã OTP</h3>
                <p class="reset-description">
                    Nhập mã OTP 6 chữ số đã được gửi tới email
                    <c:choose>
                        <c:when test="${not empty maskedEmail}">
                            <strong><c:out value="${maskedEmail}"/></strong>
                        </c:when>
                        <c:otherwise>của bạn</c:otherwise>
                    </c:choose>.
                </p>
            </div>

            <!-- Đồng hồ đếm ngược thời gian hết hạn OTP -->
            <div class="reset-otp-countdown mb-3">
                <div class="reset-otp-badge">
                    <i class="bi bi-stopwatch text-primary"></i>
                    <span class="text-secondary small">Mã hết hạn sau:</span>
                    <span class="fw-bold text-primary font-monospace" id="otp-timer">05:00</span>
                </div>
                <div id="otp-expired-alert" class="alert alert-warning py-2 px-3 small mt-2 d-none w-100 text-center" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-1"></i>
                    Mã OTP đã hết hạn. Vui lòng bấm <strong>Gửi lại mã OTP</strong> bên dưới để nhận mã mới.
                </div>
            </div>

            <c:if test="${not empty errors['global']}">
                <div class="alert alert-danger d-flex align-items-center gap-2 reset-alert">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    <span><c:out value="${errors['global']}"/></span>
                </div>
            </c:if>

            <form method="post" action="/auth/reset-password/verify" id="verify-otp-form" novalidate class="reset-form reset-otp-form">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="identifier" value="<c:out value='${identifier}'/>"/>

                <div class="mb-4">
                    <label class="form-label required reset-otp-label" for="otp">Mã xác thực OTP</label>
                    <input class="form-control form-control-lg text-center fw-bold fs-3 reset-otp-input ${not empty errors['otp'] ? 'is-invalid' : ''}"
                           id="otp" name="otp" maxlength="6" inputmode="numeric" placeholder="------"
                           style="letter-spacing: 0.35em;"
                           autocomplete="one-time-code" pattern="\d{6}" autofocus>
                    <div class="reset-otp-hint">
                        <i class="bi bi-envelope-check-fill"></i>
                        Mã xác thực gồm 6 chữ số
                    </div>
                    <qa:formError field="otp"/>
                </div>

                <button type="submit" id="verify-submit-btn" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm mb-3 reset-submit"
                        style="background-color: var(--hcmute-blue); border-color: var(--hcmute-blue);">
                    <i class="bi bi-arrow-right-circle-fill"></i>
                    Tiếp tục đặt mật khẩu
                </button>
            </form>

            <!-- Form gửi lại mã OTP với countdown cooldown -->
            <form method="post" action="/auth/forgot-password" id="resend-otp-form" class="text-center mb-3 reset-resend">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="identifier" value="<c:out value='${identifier}'/>"/>
                <button type="submit" id="resend-otp-btn" class="btn btn-link p-0 small text-decoration-none" disabled>
                    <i class="bi bi-arrow-repeat me-1"></i>
                    <span id="resend-text">Gửi lại mã OTP (<span id="resend-countdown">60</span>s)</span>
                </button>
            </form>

            <hr class="reset-divider">

            <div class="reset-back">
                <a href="/auth/forgot-password" class="text-secondary text-decoration-none">
                    <i class="bi bi-arrow-left"></i>
                    Quay lại
                </a>
            </div>
        </c:when>

        <c:when test="${step == 'password'}">
            <%-- Thu thập và xác nhận mật khẩu mới sau khi OTP hợp lệ. --%>
            <div class="reset-heading">
                <div class="reset-icon-wrap">
                    <i class="bi bi-shield-lock-fill"></i>
                </div>
                <div class="reset-eyebrow">
                    <i class="bi bi-stars"></i>
                    Hoàn tất khôi phục
                </div>
                <h3 class="reset-title">Thiết lập mật khẩu mới</h3>
                <p class="reset-description">Nhập mật khẩu mới an toàn cho tài khoản QAUTE của bạn.</p>
            </div>

            <c:if test="${not empty errors['global']}">
                <div class="alert alert-danger d-flex align-items-center gap-2 reset-alert">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    <span><c:out value="${errors['global']}"/></span>
                </div>
            </c:if>

            <form method="post" action="/auth/reset-password" id="reset-password-form" novalidate class="reset-form">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="token" value="<c:out value='${passwordForm.token}'/>"/>

                <div class="mb-4">
                    <label class="form-label required" for="newPassword">Mật khẩu mới</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                        <input type="password"
                               class="form-control ps-0 ${not empty errors['newPassword'] ? 'is-invalid' : ''}"
                               id="newPassword" name="newPassword" placeholder="Tối thiểu 8 ký tự" autocomplete="new-password">
                        <button class="btn btn-light border border-start-0 text-muted" type="button" data-toggle-password="#newPassword"
                                aria-label="Hiện hoặc ẩn mật khẩu">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                    <div class="progress mt-2" style="height: 4px;">
                        <div id="password-strength" class="progress-bar"></div>
                    </div>
                    <small id="password-strength-text" class="form-hint text-muted small" style="font-size: 0.75rem;"></small>
                    <qa:formError field="newPassword"/>
                </div>

                <div class="mb-4">
                    <label class="form-label required" for="confirmPassword">Xác nhận mật khẩu mới</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-shield-lock-fill"></i></span>
                        <input type="password"
                               class="form-control ps-0 ${not empty errors['confirmPassword'] ? 'is-invalid' : ''}"
                               id="confirmPassword" name="confirmPassword" placeholder="Nhập lại mật khẩu" autocomplete="new-password">
                        <button class="btn btn-light border border-start-0 text-muted" type="button" data-toggle-password="#confirmPassword"
                                aria-label="Hiện hoặc ẩn mật khẩu">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                    <qa:formError field="confirmPassword"/>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm reset-submit"
                        style="background-color: var(--hcmute-blue); border-color: var(--hcmute-blue);">
                    <i class="bi bi-check2-circle"></i>
                    Lưu mật khẩu mới
                </button>

                <div class="reset-assurance">
                    <span class="reset-assurance-icon"><i class="bi bi-shield-check"></i></span>
                    <div class="reset-assurance-copy">
                        <p class="reset-assurance-title">Bảo vệ tài khoản của bạn</p>
                        <p class="reset-assurance-text">Hãy sử dụng mật khẩu riêng biệt và tránh chia sẻ thông tin đăng nhập.</p>
                    </div>
                </div>
            </form>
        </c:when>
    </c:choose>
</div>

<script>
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

        const identifier = "<c:out value='${identifier}'/>";
        const ttlSeconds = ${empty ttlMinutes ? 5 : ttlMinutes} * 60;
        const cooldownSeconds = ${empty cooldownSeconds ? 60 : cooldownSeconds};

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
</script>
