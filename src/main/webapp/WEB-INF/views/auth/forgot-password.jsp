<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Quên mật khẩu</title>

<div class="mb-4">
    <h3 class="fw-bold text-dark mb-1">Khôi phục mật khẩu</h3>
    <p class="text-secondary small">Nhập email sinh viên hoặc MSSV của bạn để nhận mã OTP xác thực.</p>
</div>

<c:if test="${not empty errors['global']}">
    <div class="alert alert-danger d-flex align-items-center gap-2 py-2 small">
        <i class="bi bi-exclamation-triangle-fill"></i>
        <span><c:out value="${errors['global']}"/></span>
    </div>
</c:if>

<form method="post" action="/auth/forgot-password" id="forgot-password-form" novalidate>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <div class="mb-4">
        <label class="form-label required fw-semibold small" for="identifier">Email hoặc MSSV</label>
        <div class="input-group">
            <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-envelope"></i></span>
            <input class="form-control border-start-0 ps-0 ${not empty errors['identifier'] ? 'is-invalid' : ''}"
                   id="identifier" name="identifier"
                   value="<c:out value='${form.identifier}'/>"
                   placeholder="Nhập email sinh viên hoặc MSSV"
                   maxlength="150" autocomplete="email username" autofocus>
        </div>
        <qa:formError field="identifier"/>
    </div>

    <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm"
            style="background-color: var(--hcmute-blue); border-color: var(--hcmute-blue);">
        <i class="bi bi-send me-1"></i>Gửi mã OTP xác nhận
    </button>
</form>

<div class="text-center text-secondary small mt-4">
    Bạn đã nhớ mật khẩu? <a href="/auth/login" class="fw-semibold text-primary text-decoration-none">Quay lại đăng nhập</a>
</div>
