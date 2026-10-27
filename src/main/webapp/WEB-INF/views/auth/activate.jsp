<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Kích hoạt tài khoản</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/auth/activate.css">
</head>





<%-- Chọn nội dung kích hoạt dựa trên tính hợp lệ của liên kết. --%>
<div class="activation-page">
    <c:choose>
        <c:when test="${!tokenValid}">
            <%-- Thông báo liên kết hết hạn/không hợp lệ và cung cấp lối về đăng nhập. --%>
            <div class="activation-invalid">
                <div class="activation-icon-wrap">
                    <i class="bi bi-x-circle-fill"></i>
                </div>
                <div class="activation-eyebrow">
                    <i class="bi bi-shield-exclamation"></i>
                    Xác thực tài khoản
                </div>
                <h3 class="activation-title">Liên kết không hợp lệ</h3>
                <p class="activation-description mb-4">
                    <c:out value="${empty errorMessage ? 'Liên kết kích hoạt đã hết hạn hoặc đã được sử dụng. Vui lòng liên hệ quản trị viên để nhận lại liên kết mới.' : errorMessage}"/>
                </p>
                <a href="/auth/login" class="btn btn-primary rounded-pill px-4 activation-back">
                    <i class="bi bi-box-arrow-in-right"></i>
                    Về trang đăng nhập
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <%-- Biểu mẫu đặt mật khẩu để hoàn tất kích hoạt tài khoản. --%>
            <div class="activation-heading">
                <div class="activation-icon-wrap">
                    <i class="bi bi-shield-lock-fill"></i>
                </div>
                <div class="activation-eyebrow">
                    <i class="bi bi-stars"></i>
                    Hoàn tất thiết lập tài khoản
                </div>
                <h3 class="activation-title">Kích hoạt tài khoản</h3>
                <p class="activation-description">Thiết lập mật khẩu để hoàn tất kích hoạt tài khoản sinh viên QAUTE của bạn.</p>
            </div>

            <c:if test="${not empty errors['global']}">
                <div class="alert alert-danger d-flex align-items-center gap-2 activation-alert">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    <span><c:out value="${errors['global']}"/></span>
                </div>
            </c:if>

            <form method="post" action="/auth/activate" id="register-form" novalidate class="activation-form">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="token" value="<c:out value='${form.token}'/>"/>
                <input type="checkbox" id="acceptTerms" checked class="d-none" aria-hidden="true"/>

                <div class="mb-4">
                    <label class="form-label required" for="password">Mật khẩu mới</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                        <input type="password"
                               class="form-control ps-0 ${not empty errors['newPassword'] ? 'is-invalid' : ''}"
                               id="password" name="newPassword" placeholder="Tối thiểu 8 ký tự" autocomplete="new-password">
                        <button class="btn btn-light border border-start-0 text-muted" type="button" data-toggle-password="#password"
                                aria-label="Hiện hoặc ẩn mật khẩu">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                    <div class="progress mt-2 u-height-4px">
                        <div id="password-strength" class="progress-bar"></div>
                    </div>
                    <small id="password-strength-text" class="form-hint text-muted small u-text-size-0-75rem"></small>
                    <div class="activation-password-hint">
                        <i class="bi bi-info-circle-fill"></i>
                        <span>Sử dụng mật khẩu đủ mạnh để tăng cường bảo vệ tài khoản của bạn.</span>
                    </div>
                    <qa:formError field="newPassword"/>
                </div>

                <div class="mb-4">
                    <label class="form-label required" for="confirmPassword">Nhập lại mật khẩu</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-shield-lock-fill"></i></span>
                        <input type="password"
                               class="form-control ps-0 ${not empty errors['confirmPassword'] ? 'is-invalid' : ''}"
                               id="confirmPassword" name="confirmPassword" placeholder="Nhập lại mật khẩu" autocomplete="new-password">
                        <button class="btn btn-light border border-start-0 text-muted" type="button" data-toggle-password="#confirmPassword"
                                aria-label="Hiện hoặc ẩn mật khẩu">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                    <qa:formError field="confirmPassword"/>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm activation-submit u-bg-color-var-hcmute-blue u-border-color-var-hcmute-blue" id="register-submit">
                    <i class="bi bi-check-circle-fill"></i>
                    Hoàn tất kích hoạt
                </button>

                <div class="activation-assurance">
                    <i class="bi bi-shield-check"></i>
                    <span>Thông tin tài khoản được bảo vệ an toàn</span>
                </div>
            </form>
            <script src="/assets/js/auth/register.js"></script>
        </c:otherwise>
    </c:choose>
</div>