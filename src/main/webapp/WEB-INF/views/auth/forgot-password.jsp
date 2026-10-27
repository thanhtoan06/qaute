<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Quên mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/auth/forgot-password.css">
</head>





<%-- Biểu mẫu yêu cầu gửi OTP đến email hoặc mã số sinh viên đã đăng ký. --%>
<div class="forgot-recovery">
    <div class="recovery-heading">
        <div class="recovery-icon-wrap">
            <i class="bi bi-shield-lock-fill"></i>
        </div>
        <div class="recovery-eyebrow">
            <i class="bi bi-stars"></i>
            Bảo mật tài khoản QAUTE
        </div>
        <h3 class="recovery-title">Khôi phục mật khẩu</h3>
        <p class="recovery-description">Nhập email sinh viên hoặc MSSV của bạn để nhận mã OTP xác thực.</p>
    </div>

    <c:if test="${not empty errors['global']}">
        <div class="alert alert-danger d-flex align-items-center gap-2 recovery-alert">
            <i class="bi bi-exclamation-triangle-fill"></i>
            <span><c:out value="${errors['global']}"/></span>
        </div>
    </c:if>

    <%-- Gửi mã xác minh và hiển thị hướng dẫn bảo mật cùng lỗi trường nhập. --%>
    <form method="post" action="/auth/forgot-password" id="forgot-password-form" novalidate class="recovery-form">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="mb-4">
            <label class="form-label required" for="identifier">Email hoặc MSSV</label>
            <div class="input-group">
                <span class="input-group-text"><i class="bi bi-person-vcard-fill"></i></span>
                <input class="form-control ${not empty errors['identifier'] ? 'is-invalid' : ''}"
                       id="identifier" name="identifier"
                       value="<c:out value='${form.identifier}'/>"
                       placeholder="Nhập email sinh viên hoặc MSSV"
                       maxlength="150" autocomplete="email username" autofocus>
            </div>
            <div class="recovery-field-hint">
                <i class="bi bi-info-circle-fill"></i>
                <span>Sử dụng email sinh viên hoặc mã số sinh viên đã đăng ký với hệ thống.</span>
            </div>
            <qa:formError field="identifier"/>
        </div>

        <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm recovery-submit u-bg-color-var-hcmute-blue u-border-color-var-hcmute-blue">
            <i class="bi bi-send-fill"></i>
            Gửi mã OTP xác nhận
        </button>

        <div class="recovery-assurance">
            <span class="recovery-assurance-icon"><i class="bi bi-shield-check"></i></span>
            <div class="recovery-assurance-copy">
                <p class="recovery-assurance-title">Quy trình xác thực an toàn</p>
                <p class="recovery-assurance-text">Mã OTP giúp xác minh yêu cầu khôi phục trước khi bạn thiết lập mật khẩu mới.</p>
            </div>
        </div>
    </form>

    <hr class="recovery-divider">

    <div class="recovery-back">
        <span>Bạn đã nhớ mật khẩu?</span>
        <a href="/auth/login" class="fw-semibold text-primary text-decoration-none">
            Quay lại đăng nhập
            <i class="bi bi-arrow-right"></i>
        </a>
    </div>
</div>