<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Xác thực OTP</title>
<div class="text-center mb-4">
    <h2 class="card-title">Xác thực tài khoản</h2>
    <p class="text-secondary">
        Mã OTP đã được gửi tới <strong><c:out value="${maskedEmail}"/></strong>
    </p>
</div>
<form method="post" action="/auth/verify-otp" id="verify-otp-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="email" value="<c:out value='${email}'/>"/>
    <div class="mb-3">
        <label class="form-label required" for="otp">Mã OTP</label>
        <input class="form-control text-center ${not empty errors['otp'] ? 'is-invalid' : ''}"
               id="otp" name="otp" maxlength="6" inputmode="numeric"
               autocomplete="one-time-code" pattern="\d{6}" autofocus>
        <qa:formError field="otp"/>
    </div>
    <p class="text-secondary small text-center">
        Mã có hiệu lực trong <c:out value="${ttlMinutes}"/> phút.
    </p>
    <button type="submit" class="btn btn-primary w-100">Xác nhận</button>
</form>
<form method="post" action="/auth/resend-otp" class="text-center mt-3">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="email" value="<c:out value='${email}'/>"/>
    <button type="submit" id="resend-otp" class="btn btn-link p-0"
            data-seconds="${secondsUntilResend}">
        Gửi lại mã
    </button>
</form>
<div class="text-center text-secondary mt-3">
    <a href="/auth/register">← Quay lại đăng ký</a>
</div>
<script src="/assets/js/auth/otp-countdown.js"></script>
