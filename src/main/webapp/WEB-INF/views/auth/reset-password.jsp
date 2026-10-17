<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Đặt lại mật khẩu</title>

<c:choose>
    <c:when test="${step == 'otp'}">
        <div class="text-center mb-4">
            <h2 class="card-title">Xác thực mã OTP</h2>
            <p class="text-secondary">Nhập mã OTP 6 chữ số đã gửi tới email của bạn</p>
        </div>
        <c:if test="${not empty errors['global']}">
            <div class="alert alert-danger"><c:out value="${errors['global']}"/></div>
        </c:if>
        <form method="post" action="/auth/reset-password/verify" id="verify-otp-form" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="identifier" value="<c:out value='${identifier}'/>"/>
            <div class="mb-3">
                <label class="form-label required" for="otp">Mã OTP</label>
                <input class="form-control text-center ${not empty errors['otp'] ? 'is-invalid' : ''}"
                       id="otp" name="otp" maxlength="6" inputmode="numeric"
                       autocomplete="one-time-code" pattern="\d{6}" autofocus>
                <qa:formError field="otp"/>
            </div>
            <button type="submit" class="btn btn-primary w-100">Xác nhận</button>
        </form>
        <form method="post" action="/auth/forgot-password" class="text-center mt-3">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="identifier" value="<c:out value='${identifier}'/>"/>
            <button type="submit" class="btn btn-link p-0">Gửi lại mã OTP</button>
        </form>
        <div class="text-center text-secondary mt-3">
            <a href="/auth/forgot-password">← Quay lại</a>
        </div>
    </c:when>

    <c:when test="${step == 'password'}">
        <div class="text-center mb-4">
            <h2 class="card-title">Đặt mật khẩu mới</h2>
            <p class="text-secondary">Nhập mật khẩu mới cho tài khoản của bạn</p>
        </div>
        <c:if test="${not empty errors['global']}">
            <div class="alert alert-danger"><c:out value="${errors['global']}"/></div>
        </c:if>
        <form method="post" action="/auth/reset-password" id="reset-password-form" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="token" value="<c:out value='${passwordForm.token}'/>"/>
            <div class="mb-3">
                <label class="form-label required" for="newPassword">Mật khẩu mới</label>
                <div class="input-group input-group-flat">
                    <input type="password"
                           class="form-control ${not empty errors['newPassword'] ? 'is-invalid' : ''}"
                           id="newPassword" name="newPassword" autocomplete="new-password">
                    <button class="btn btn-icon" type="button" data-toggle-password="#newPassword"
                            aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
                </div>
                <div class="progress mt-2" style="height: 5px;">
                    <div id="password-strength" class="progress-bar"></div>
                </div>
                <small id="password-strength-text" class="form-hint"></small>
                <qa:formError field="newPassword"/>
            </div>
            <div class="mb-3">
                <label class="form-label required" for="confirmPassword">Nhập lại mật khẩu</label>
                <div class="input-group input-group-flat">
                    <input type="password"
                           class="form-control ${not empty errors['confirmPassword'] ? 'is-invalid' : ''}"
                           id="confirmPassword" name="confirmPassword" autocomplete="new-password">
                    <button class="btn btn-icon" type="button" data-toggle-password="#confirmPassword"
                            aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
                </div>
                <qa:formError field="confirmPassword"/>
            </div>
            <button type="submit" class="btn btn-primary w-100">Lưu mật khẩu mới</button>
        </form>
    </c:when>
</c:choose>

<script src="/assets/js/auth/register.js"></script>
