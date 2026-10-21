<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Kích hoạt tài khoản</title>

<c:choose>
    <c:when test="${!tokenValid}">
        <div class="text-center py-4">
            <div class="rounded-circle bg-danger-subtle text-danger d-inline-flex align-items-center justify-content-center mb-3" style="width: 54px; height: 54px;">
                <i class="bi bi-x-circle-fill fs-2"></i>
            </div>
            <h3 class="fw-bold text-dark mb-2">Liên kết không hợp lệ</h3>
            <p class="text-secondary small mb-4">
                <c:out value="${empty errorMessage ? 'Liên kết kích hoạt đã hết hạn hoặc đã được sử dụng. Vui lòng liên hệ quản trị viên để nhận lại liên kết mới.' : errorMessage}"/>
            </p>
            <a href="/auth/login" class="btn btn-primary rounded-pill px-4">
                <i class="bi bi-box-arrow-in-right me-1"></i>Về trang đăng nhập
            </a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="mb-4">
            <h3 class="fw-bold text-dark mb-1">Kích hoạt tài khoản</h3>
            <p class="text-secondary small">Thiết lập mật khẩu để hoàn tất kích hoạt tài khoản sinh viên QAUTE của bạn.</p>
        </div>

        <c:if test="${not empty errors['global']}">
            <div class="alert alert-danger d-flex align-items-center gap-2 py-2 small mb-3">
                <i class="bi bi-exclamation-triangle-fill"></i>
                <span><c:out value="${errors['global']}"/></span>
            </div>
        </c:if>

        <form method="post" action="/auth/activate" id="register-form" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="token" value="<c:out value='${form.token}'/>"/>
            <input type="checkbox" id="acceptTerms" checked class="d-none" aria-hidden="true"/>

            <div class="mb-3">
                <label class="form-label required fw-semibold small" for="password">Mật khẩu mới</label>
                <div class="input-group">
                    <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-lock"></i></span>
                    <input type="password"
                           class="form-control border-start-0 border-end-0 ps-0 ${not empty errors['newPassword'] ? 'is-invalid' : ''}"
                           id="password" name="newPassword" placeholder="Tối thiểu 8 ký tự" autocomplete="new-password">
                    <button class="btn btn-light border border-start-0 text-muted" type="button" data-toggle-password="#password"
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
                <label class="form-label required fw-semibold small" for="confirmPassword">Nhập lại mật khẩu</label>
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

            <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm" id="register-submit"
                    style="background-color: var(--hcmute-blue); border-color: var(--hcmute-blue);">
                <i class="bi bi-check-circle me-1"></i>Hoàn tất kích hoạt
            </button>
        </form>
        <script src="/assets/js/auth/register.js"></script>
    </c:otherwise>
</c:choose>
