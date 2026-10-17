<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Kích hoạt tài khoản</title>

<c:choose>
    <c:when test="${!tokenValid}">
        <div class="text-center mb-4">
            <h2 class="card-title">Liên kết không hợp lệ</h2>
            <div class="alert alert-danger mt-3">
                <c:out value="${empty errorMessage ? 'Liên kết đã hết hạn, hãy nhờ quản trị gửi lại lời mời' : errorMessage}"/>
            </div>
            <div class="mt-4">
                <a href="/auth/login" class="btn btn-outline-primary">Về trang đăng nhập</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="text-center mb-4">
            <h2 class="card-title">Kích hoạt tài khoản</h2>
            <p class="text-secondary">Thiết lập mật khẩu để hoàn tất kích hoạt tài khoản</p>
        </div>
        <c:if test="${not empty errors['global']}">
            <div class="alert alert-danger"><c:out value="${errors['global']}"/></div>
        </c:if>
        <form method="post" action="/auth/activate" id="register-form" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="token" value="<c:out value='${form.token}'/>"/>
            <input type="checkbox" id="acceptTerms" checked class="d-none" aria-hidden="true"/>

            <div class="mb-3">
                <label class="form-label required" for="password">Mật khẩu mới</label>
                <div class="input-group input-group-flat">
                    <input type="password"
                           class="form-control ${not empty errors['newPassword'] ? 'is-invalid' : ''}"
                           id="password" name="newPassword" autocomplete="new-password">
                    <button class="btn btn-icon" type="button" data-toggle-password="#password"
                            aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
                </div>
                <div class="progress mt-2" style="height: 5px;">
                    <div id="password-strength" class="progress-bar"></div>
                </div>
                <small id="password-strength-text" class="form-hint"></small>
                <qa:formError field="newPassword"/>
            </div>

            <div class="mb-3">
                <label class="form-label required" for="confirmPassword">Nhập lại mật khẩu</label>
                <div class="input-group input-group-flat">
                    <input type="password"
                           class="form-control ${not empty errors['confirmPassword'] ? 'is-invalid' : ''}"
                           id="confirmPassword" name="confirmPassword" autocomplete="new-password">
                    <button class="btn btn-icon" type="button" data-toggle-password="#confirmPassword"
                            aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
                </div>
                <qa:formError field="confirmPassword"/>
            </div>

            <button type="submit" class="btn btn-primary w-100" id="register-submit">Kích hoạt tài khoản</button>
        </form>
        <script src="/assets/js/auth/register.js"></script>
    </c:otherwise>
</c:choose>
