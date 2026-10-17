<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Quên mật khẩu</title>
<div class="text-center mb-4">
    <h2 class="card-title">Quên mật khẩu</h2>
    <p class="text-secondary">Nhập email hoặc MSSV để nhận mã OTP đặt lại mật khẩu</p>
</div>
<c:if test="${not empty errors['global']}">
    <div class="alert alert-danger"><c:out value="${errors['global']}"/></div>
</c:if>
<form method="post" action="/auth/forgot-password" id="forgot-password-form" novalidate>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div class="mb-3">
        <label class="form-label required" for="identifier">Email hoặc MSSV</label>
        <input class="form-control ${not empty errors['identifier'] ? 'is-invalid' : ''}"
               id="identifier" name="identifier"
               value="<c:out value='${form.identifier}'/>"
               maxlength="150" autocomplete="email username" autofocus>
        <qa:formError field="identifier"/>
    </div>
    <button type="submit" class="btn btn-primary w-100">Gửi mã OTP</button>
</form>
<div class="text-center text-secondary mt-3">
    Nhớ mật khẩu? <a href="/auth/login">Đăng nhập</a>
</div>

