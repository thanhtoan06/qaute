<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Đăng ký</title>
<div class="mb-3">
    <h2 class="card-title text-center">Tạo tài khoản</h2>
    <p class="text-secondary text-center">Đăng ký tài khoản sinh viên QAUTE</p>
</div>
<form method="post" action="/auth/register" id="register-form" novalidate>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div class="mb-3">
        <label class="form-label required" for="fullName">Họ và tên</label>
        <input class="form-control ${not empty errors['fullName'] ? 'is-invalid' : ''}"
               id="fullName" name="fullName" value="<c:out value='${form.fullName}'/>"
               maxlength="100" autocomplete="name">
        <qa:formError field="fullName"/>
    </div>
    <div class="mb-3">
        <label class="form-label required" for="email">Email</label>
        <input type="email" class="form-control ${not empty errors['email'] ? 'is-invalid' : ''}"
               id="email" name="email" value="<c:out value='${form.email}'/>"
               maxlength="150" autocomplete="email">
        <qa:formError field="email"/>
    </div>
    <div class="mb-3">
        <label class="form-label" for="mssv">MSSV</label>
        <input class="form-control ${not empty errors['mssv'] ? 'is-invalid' : ''}"
               id="mssv" name="mssv" value="<c:out value='${form.mssv}'/>"
               maxlength="8" inputmode="numeric">
        <qa:formError field="mssv"/>
    </div>
    <div class="mb-3">
        <label class="form-label required" for="password">Mật khẩu</label>
        <div class="input-group input-group-flat">
            <input type="password" class="form-control ${not empty errors['password'] ? 'is-invalid' : ''}"
                   id="password" name="password" autocomplete="new-password">
            <button class="btn btn-icon" type="button" data-toggle-password="#password"
                    aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
        </div>
        <div class="progress mt-2" style="height: 5px;"><div id="password-strength" class="progress-bar"></div></div>
        <small id="password-strength-text" class="form-hint"></small>
        <qa:formError field="password"/>
    </div>
    <div class="mb-3">
        <label class="form-label required" for="confirmPassword">Nhập lại mật khẩu</label>
        <div class="input-group input-group-flat">
            <input type="password" class="form-control ${not empty errors['confirmPassword'] ? 'is-invalid' : ''}"
                   id="confirmPassword" name="confirmPassword" autocomplete="new-password">
            <button class="btn btn-icon" type="button" data-toggle-password="#confirmPassword"
                    aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
        </div>
        <qa:formError field="confirmPassword"/>
    </div>
    <div class="mb-3">
        <label class="form-check">
            <input class="form-check-input" type="checkbox" id="acceptTerms" name="acceptTerms"
                   value="true" ${form.acceptTerms ? 'checked' : ''}>
            <span class="form-check-label">Tôi đồng ý với điều khoản sử dụng</span>
        </label>
        <qa:formError field="acceptTerms"/>
    </div>
    <button type="submit" class="btn btn-primary w-100" id="register-submit">Đăng ký</button>
</form>
<div class="text-center text-secondary mt-3">
    Đã có tài khoản? <a href="/auth/login">Đăng nhập</a>
</div>
<script src="/assets/js/auth/register.js"></script>
