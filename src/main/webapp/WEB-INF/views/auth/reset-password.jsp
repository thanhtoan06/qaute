<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Đặt lại mật khẩu</title>

<c:choose>
    <c:when test="${step == 'otp'}">
        <div class="mb-4 text-center">
            <div class="rounded-circle bg-primary-subtle text-primary d-inline-flex align-items-center justify-content-center mb-3" style="width: 54px; height: 54px;">
                <i class="bi bi-key-fill fs-2"></i>
            </div>
            <h3 class="fw-bold text-dark mb-1">Xác thực mã OTP</h3>
            <p class="text-secondary small">Nhập mã OTP 6 chữ số đã được gửi tới email của bạn.</p>
        </div>

        <c:if test="${not empty errors['global']}">
            <div class="alert alert-danger d-flex align-items-center gap-2 py-2 small mb-3">
                <i class="bi bi-exclamation-triangle-fill"></i>
                <span><c:out value="${errors['global']}"/></span>
            </div>
        </c:if>

        <form method="post" action="/auth/reset-password/verify" id="verify-otp-form" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="identifier" value="<c:out value='${identifier}'/>"/>

            <div class="mb-4">
                <label class="form-label required fw-semibold small text-center d-block" for="otp">Mã xác thực OTP</label>
                <input class="form-control form-control-lg text-center fw-bold fs-3 ${not empty errors['otp'] ? 'is-invalid' : ''}"
                       id="otp" name="otp" maxlength="6" inputmode="numeric" placeholder="------"
                       style="letter-spacing: 0.35em;"
                       autocomplete="one-time-code" pattern="\d{6}" autofocus>
                <qa:formError field="otp"/>
            </div>

            <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm mb-3"
                    style="background-color: var(--hcmute-blue); border-color: var(--hcmute-blue);">
                <i class="bi bi-arrow-right-circle me-1"></i>Tiếp tục đặt mật khẩu
            </button>
        </form>

        <form method="post" action="/auth/forgot-password" class="text-center mb-3">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="identifier" value="<c:out value='${identifier}'/>"/>
            <button type="submit" class="btn btn-link p-0 small text-decoration-none">Gửi lại mã OTP</button>
        </form>

        <div class="text-center text-secondary small">
            <a href="/auth/forgot-password" class="text-secondary text-decoration-none">← Quay lại</a>
        </div>
    </c:when>

    <c:when test="${step == 'password'}">
        <div class="mb-4">
            <h3 class="fw-bold text-dark mb-1">Thiết lập mật khẩu mới</h3>
            <p class="text-secondary small">Nhập mật khẩu mới an toàn cho tài khoản QAUTE của bạn.</p>
        </div>

        <c:if test="${not empty errors['global']}">
            <div class="alert alert-danger d-flex align-items-center gap-2 py-2 small mb-3">
                <i class="bi bi-exclamation-triangle-fill"></i>
                <span><c:out value="${errors['global']}"/></span>
            </div>
        </c:if>

        <form method="post" action="/auth/reset-password" id="reset-password-form" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="token" value="<c:out value='${passwordForm.token}'/>"/>

            <div class="mb-3">
                <label class="form-label required fw-semibold small" for="newPassword">Mật khẩu mới</label>
                <div class="input-group">
                    <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-lock"></i></span>
                    <input type="password"
                           class="form-control border-start-0 border-end-0 ps-0 ${not empty errors['newPassword'] ? 'is-invalid' : ''}"
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
                <label class="form-label required fw-semibold small" for="confirmPassword">Xác nhận mật khẩu mới</label>
                <div class="input-group">
                    <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-shield-lock"></i></span>
                    <input type="password"
                           class="form-control border-start-0 border-end-0 ps-0 ${not empty errors['confirmPassword'] ? 'is-invalid' : ''}"
                           id="confirmPassword" name="confirmPassword" placeholder="Nhập lại mật khẩu" autocomplete="new-password">
                    <button class="btn btn-light border border-start-0 text-muted" type="button" data-toggle-password="#confirmPassword"
                            aria-label="Hiện hoặc ẩn mật khẩu">
                        <i class="bi bi-eye"></i>
                    </button>
                </div>
                <qa:formError field="confirmPassword"/>
            </div>

            <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm"
                    style="background-color: var(--hcmute-blue); border-color: var(--hcmute-blue);">
                <i class="bi bi-check2-circle me-1"></i>Lưu mật khẩu mới
            </button>
        </form>
    </c:when>
</c:choose>

<script src="/assets/js/auth/register.js"></script>
