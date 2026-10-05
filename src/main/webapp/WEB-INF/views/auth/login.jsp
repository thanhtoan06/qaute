<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Đăng nhập</title>
<div class="mb-3">
    <h2 class="card-title text-center">Đăng nhập</h2>
    <p class="text-secondary text-center">Đăng nhập vào tài khoản QAUTE</p>
</div>
<c:if test="${verified}">
    <div class="alert alert-success">Xác thực tài khoản thành công. Vui lòng đăng nhập.</div>
</c:if>
<c:if test="${not empty errors['global']}">
    <div class="alert alert-danger"><c:out value="${errors['global']}"/></div>
</c:if>
<form method="post" action="/auth/login" id="login-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="returnUrl" value="<c:out value='${returnUrl}'/>"/>
    <div class="mb-3">
        <label class="form-label required" for="identifier">Email hoặc MSSV</label>
        <input class="form-control ${not empty errors['identifier'] ? 'is-invalid' : ''}"
               id="identifier" name="identifier" value="<c:out value='${form.identifier}'/>"
               maxlength="150" autocomplete="username">
        <qa:formError field="identifier"/>
    </div>
    <div class="mb-3">
        <label class="form-label required" for="password">Mật khẩu</label>
        <div class="input-group input-group-flat">
            <input type="password" class="form-control ${not empty errors['password'] ? 'is-invalid' : ''}"
                   id="password" name="password" autocomplete="current-password">
            <button class="btn btn-icon" type="button" data-toggle-password="#password"
                    aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
        </div>
        <qa:formError field="password"/>
    </div>
    <label class="form-check mb-3">
        <input class="form-check-input" type="checkbox" name="remember" value="true"
               ${form.remember ? 'checked' : ''}>
        <span class="form-check-label">Ghi nhớ đăng nhập 30 ngày</span>
    </label>
    <button type="submit" class="btn btn-primary w-100" id="login-submit">Đăng nhập</button>
</form>
<div class="d-flex justify-content-between mt-3">
    <a href="/auth/forgot-password">Quên mật khẩu?</a>
    <a href="/auth/register">Đăng ký</a>
</div>
<script src="/assets/js/auth/login.js"></script>
