
<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/auth/login.css">
</head>





<%-- Biểu mẫu đăng nhập bằng tài khoản QAUTE hoặc Google SSO. --%>
<div class="login-premium">
    <div class="login-premium-heading">
        <div class="login-premium-icon">
            <i class="bi bi-person-lock"></i>
        </div>
        <div class="login-premium-eyebrow">
            <i class="bi bi-stars"></i>
            Không gian học tập QAUTE
        </div>
        <h3 class="login-premium-title">Chào mừng trở lại!</h3>
        <p class="login-premium-description">Vui lòng đăng nhập vào tài khoản QAUTE của bạn để tiếp tục.</p>
    </div>

    <c:if test="${verified}">
        <div class="alert alert-success d-flex align-items-center gap-2 login-premium-alert login-premium-alert--success">
            <i class="bi bi-check-circle-fill"></i>
            <span>Xác thực tài khoản thành công. Vui lòng đăng nhập.</span>
        </div>
    </c:if>

    <c:if test="${not empty errors['global']}">
        <div class="alert alert-danger d-flex align-items-center gap-2 login-premium-alert">
            <i class="bi bi-exclamation-triangle-fill"></i>
            <span><c:out value="${errors['global']}"/></span>
        </div>
    </c:if>

    <c:if test="${not empty param.oauthError}">
        <div class="alert alert-danger d-flex align-items-center gap-2 login-premium-alert">
            <i class="bi bi-exclamation-triangle-fill"></i>
            <span><c:out value="${param.oauthError}"/></span>
        </div>
    </c:if>

    <%-- Thu thập thông tin đăng nhập, tùy chọn ghi nhớ và trả lỗi theo trường. --%>
    <form method="post" action="/auth/login" id="login-form" class="login-premium-form">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="returnUrl" value="<c:out value='${returnUrl}'/>"/>

        <div class="mb-3">
            <label class="form-label required" for="identifier">Email, MSSV hoặc họ tên</label>
            <div class="input-group">
                <span class="input-group-text">
                    <i class="bi bi-person-vcard-fill"></i>
                </span>
                <input class="form-control ${not empty errors['identifier'] ? 'is-invalid' : ''}"
                       id="identifier" name="identifier" value="<c:out value='${form.identifier}'/>"
                       placeholder="Nhập MSSV, email hoặc họ tên"
                       maxlength="150" autocomplete="username" autofocus>
            </div>
            <qa:formError field="identifier"/>
        </div>

        <div class="mb-3">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <label class="form-label required mb-0" for="password">Mật khẩu</label>
                <a href="/auth/forgot-password" class="login-premium-forgot">
                    <i class="bi bi-question-circle"></i>Quên mật khẩu?
                </a>
            </div>
            <div class="input-group">
                <span class="input-group-text">
                    <i class="bi bi-lock-fill"></i>
                </span>
                <input type="password" class="form-control ${not empty errors['password'] ? 'is-invalid' : ''}"
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
                <label class="form-check-label" for="remember">
                    Ghi nhớ đăng nhập 30 ngày
                </label>
            </div>
        </div>

        <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm login-premium-submit u-bg-color-var-hcmute-blue u-border-color-var-hcmute-blue" id="login-submit">
            <i class="bi bi-box-arrow-in-right"></i>Đăng nhập
        </button>
    </form>

    <%-- Hiển thị lựa chọn đăng nhập Google khi SSO được bật. --%>
    <c:if test="${googleSsoEnabled}">
        <div class="login-premium-divider">hoặc tiếp tục với</div>
        <a href="/oauth2/authorization/google" class="btn btn-outline-primary w-100 py-2 rounded-3 login-premium-google">
            <i class="bi bi-google"></i>Đăng nhập bằng Google
        </a>
    </c:if>

    <div class="login-premium-assurance">
        <span class="login-premium-assurance-icon"><i class="bi bi-shield-check"></i></span>
        <div class="login-premium-assurance-copy">
            <p class="login-premium-assurance-title">Đăng nhập an toàn</p>
            <p class="login-premium-assurance-description">Sử dụng tài khoản của bạn để truy cập các dịch vụ hỗ trợ sinh viên QAUTE.</p>
        </div>
    </div>

    <div class="login-premium-footer">
        <i class="bi bi-shield-lock-fill"></i>
        <span>Đăng nhập tài khoản để tiếp tục.</span>
    </div>
</div>

<script src="/assets/js/auth/login.js"></script>
