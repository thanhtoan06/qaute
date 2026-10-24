<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Đăng nhập</title>

<div class="mb-4">
    <h3 class="fw-bold text-dark mb-1">Chào mừng trở lại!</h3>
    <p class="text-secondary small">Vui lòng đăng nhập vào tài khoản QAUTE của bạn để tiếp tục.</p>
</div>

<c:if test="${verified}">
    <div class="alert alert-success d-flex align-items-center gap-2 py-2 small">
        <i class="bi bi-check-circle-fill"></i>
        <span>Xác thực tài khoản thành công. Vui lòng đăng nhập.</span>
    </div>
</c:if>

<c:if test="${param.passwordChanged eq 'true'}">
    <div class="alert alert-success py-2 small" role="status">
        Đã đổi mật khẩu và đăng xuất các phiên cũ. Vui lòng đăng nhập bằng mật khẩu mới.
    </div>
</c:if>

<c:if test="${not empty errors['global']}">
    <div class="alert alert-danger d-flex align-items-center gap-2 py-2 small">
        <i class="bi bi-exclamation-triangle-fill"></i>
        <span><c:out value="${errors['global']}"/></span>
    </div>
</c:if>
<c:if test="${not empty param.oauthError}">
    <div class="alert alert-danger d-flex align-items-center gap-2 py-2 small">
        <i class="bi bi-exclamation-triangle-fill"></i>
        <span><c:out value="${param.oauthError}"/></span>
    </div>
</c:if>

<form method="post" action="/auth/login" id="login-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="returnUrl" value="<c:out value='${returnUrl}'/>"/>

    <div class="mb-3">
        <label class="form-label required fw-semibold small" for="identifier">Email hoặc MSSV</label>
        <div class="input-group">
            <span class="input-group-text bg-light border-end-0 text-muted">
                <i class="bi bi-person"></i>
            </span>
            <input class="form-control border-start-0 ps-0 ${not empty errors['identifier'] ? 'is-invalid' : ''}"
                   id="identifier" name="identifier" value="<c:out value='${form.identifier}'/>"
                    placeholder="Ví dụ: MSSV, email hoặc tên đăng nhập"
                   maxlength="150" autocomplete="username" autofocus>
        </div>
        <qa:formError field="identifier"/>
    </div>

    <div class="mb-3">
        <div class="d-flex justify-content-between align-items-center mb-1">
            <label class="form-label required fw-semibold small mb-0" for="password">Mật khẩu</label>
            <a href="/auth/forgot-password" class="text-primary text-decoration-none small">Quên mật khẩu?</a>
        </div>
        <div class="input-group">
            <span class="input-group-text bg-light border-end-0 text-muted">
                <i class="bi bi-lock"></i>
            </span>
            <input type="password" class="form-control border-start-0 border-end-0 ps-0 ${not empty errors['password'] ? 'is-invalid' : ''}"
                   id="password" name="password" placeholder="Nhập mật khẩu của bạn" autocomplete="current-password">
            <button class="btn btn-light border border-start-0 text-muted" type="button" data-toggle-password="#password"
                    aria-label="Hiện hoặc ẩn mật khẩu">
                <i class="bi bi-eye"></i>
            </button>
        </div>
        <qa:formError field="password"/>
    </div>

    <div class="d-flex align-items-center justify-content-between mb-4">
        <div class="form-check">
            <input class="form-check-input" type="checkbox" name="remember" id="remember" value="true"
                   ${form.remember ? 'checked' : ''}>
            <label class="form-check-label small text-secondary" for="remember">
                Ghi nhớ đăng nhập 30 ngày
            </label>
        </div>
    </div>

    <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm" id="login-submit"
            style="background-color: var(--hcmute-blue); border-color: var(--hcmute-blue);">
        <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập
    </button>
</form>

<c:if test="${googleSsoEnabled}">
    <div class="d-flex align-items-center gap-2 my-3">
        <hr class="flex-grow-1 m-0"><span class="text-secondary small">hoặc</span><hr class="flex-grow-1 m-0">
    </div>
    <a href="/oauth2/authorization/google" class="btn btn-outline-primary w-100 py-2 rounded-3">
        <i class="bi bi-google me-2"></i>Đăng nhập bằng Google
    </a>
</c:if>

<div class="text-center text-secondary small mt-4">
    Đăng nhập tài khoản để tiếp tục.
</div>

<script src="/assets/js/auth/login.js"></script>
