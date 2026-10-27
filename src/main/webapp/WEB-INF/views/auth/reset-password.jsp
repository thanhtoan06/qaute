<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Đặt lại mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/auth/reset-password.css">
</head>


<%-- Hiển thị bước xác minh OTP hoặc bước thiết lập mật khẩu mới. --%>
<div class="reset-recovery">
    <c:choose>
        <c:when test="${step == 'otp'}">
            <%-- Nhập OTP, theo dõi thời hạn và gửi lại mã khi hết thời gian chờ. --%>
            <div class="reset-heading reset-heading--center">
                <div class="reset-icon-wrap">
                    <i class="bi bi-key-fill"></i>
                </div>
                <div class="reset-eyebrow">
                    <i class="bi bi-shield-check"></i>
                    Xác minh danh tính
                </div>
                <h3 class="reset-title">Xác thực mã OTP</h3>
                <p class="reset-description">
                    Nhập mã OTP 6 chữ số đã được gửi tới email
                    <c:choose>
                        <c:when test="${not empty maskedEmail}">
                            <strong><c:out value="${maskedEmail}"/></strong>
                        </c:when>
                        <c:otherwise>của bạn</c:otherwise>
                    </c:choose>.
                </p>
            </div>

            <!-- Đồng hồ đếm ngược thời gian hết hạn OTP -->
            <div class="reset-otp-countdown mb-3">
                <div class="reset-otp-badge">
                    <i class="bi bi-stopwatch text-primary"></i>
                    <span class="text-secondary small">Mã hết hạn sau:</span>
                    <span class="fw-bold text-primary font-monospace" id="otp-timer"
                          data-otp-identifier="<c:out value='${identifier}'/>"
                          data-ttl-minutes="${empty ttlMinutes ? 5 : ttlMinutes}"
                          data-cooldown-seconds="${empty cooldownSeconds ? 60 : cooldownSeconds}">05:00</span>
                </div>
                <div id="otp-expired-alert" class="alert alert-warning py-2 px-3 small mt-2 d-none w-100 text-center" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-1"></i>
                    Mã OTP đã hết hạn. Vui lòng bấm <strong>Gửi lại mã OTP</strong> bên dưới để nhận mã mới.
                </div>
            </div>

            <c:if test="${not empty errors['global']}">
                <div class="alert alert-danger d-flex align-items-center gap-2 reset-alert">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    <span><c:out value="${errors['global']}"/></span>
                </div>
            </c:if>

            <form method="post" action="/auth/reset-password/verify" id="verify-otp-form" novalidate class="reset-form reset-otp-form">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="identifier" value="<c:out value='${identifier}'/>"/>

                <div class="mb-4">
                    <label class="form-label required reset-otp-label" for="otp">Mã xác thực OTP</label>
                    <input class="form-control form-control-lg text-center fw-bold fs-3 reset-otp-input ${not empty errors['otp'] ? 'is-invalid' : ''} u-tracking-0-35em"
                           id="otp" name="otp" maxlength="6" inputmode="numeric" placeholder="------"
                           autocomplete="one-time-code" pattern="\d{6}" autofocus>
                    <div class="reset-otp-hint">
                        <i class="bi bi-envelope-check-fill"></i>
                        Mã xác thực gồm 6 chữ số
                    </div>
                    <qa:formError field="otp"/>
                </div>

                <button type="submit" id="verify-submit-btn" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm mb-3 reset-submit u-bg-color-var-hcmute-blue u-border-color-var-hcmute-blue">
                    <i class="bi bi-arrow-right-circle-fill"></i>
                    Tiếp tục đặt mật khẩu
                </button>
            </form>

            <!-- Form gửi lại mã OTP với countdown cooldown -->
            <form method="post" action="/auth/forgot-password" id="resend-otp-form" class="text-center mb-3 reset-resend">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="identifier" value="<c:out value='${identifier}'/>"/>
                <button type="submit" id="resend-otp-btn" class="btn btn-link p-0 small text-decoration-none" disabled>
                    <i class="bi bi-arrow-repeat me-1"></i>
                    <span id="resend-text">Gửi lại mã OTP (<span id="resend-countdown">60</span>s)</span>
                </button>
            </form>

            <hr class="reset-divider">

            <div class="reset-back">
                <a href="/auth/forgot-password" class="text-secondary text-decoration-none">
                    <i class="bi bi-arrow-left"></i>
                    Quay lại
                </a>
            </div>
        </c:when>

        <c:when test="${step == 'password'}">
            <%-- Thu thập và xác nhận mật khẩu mới sau khi OTP hợp lệ. --%>
            <div class="reset-heading">
                <div class="reset-icon-wrap">
                    <i class="bi bi-shield-lock-fill"></i>
                </div>
                <div class="reset-eyebrow">
                    <i class="bi bi-stars"></i>
                    Hoàn tất khôi phục
                </div>
                <h3 class="reset-title">Thiết lập mật khẩu mới</h3>
                <p class="reset-description">Nhập mật khẩu mới an toàn cho tài khoản QAUTE của bạn.</p>
            </div>

            <c:if test="${not empty errors['global']}">
                <div class="alert alert-danger d-flex align-items-center gap-2 reset-alert">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    <span><c:out value="${errors['global']}"/></span>
                </div>
            </c:if>

            <form method="post" action="/auth/reset-password" id="reset-password-form" novalidate class="reset-form">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="token" value="<c:out value='${passwordForm.token}'/>"/>

                <div class="mb-4">
                    <label class="form-label required" for="newPassword">Mật khẩu mới</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                        <input type="password"
                               class="form-control ps-0 ${not empty errors['newPassword'] ? 'is-invalid' : ''}"
                               id="newPassword" name="newPassword" placeholder="Tối thiểu 8 ký tự" autocomplete="new-password">
                        <button class="btn btn-light border border-start-0 text-muted" type="button" data-toggle-password="#newPassword"
                                aria-label="Hiện hoặc ẩn mật khẩu">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                    <div class="progress mt-2 u-height-4px">
                        <div id="password-strength" class="progress-bar"></div>
                    </div>
                    <small id="password-strength-text" class="form-hint text-muted small u-text-size-0-75rem"></small>
                    <qa:formError field="newPassword"/>
                </div>

                <div class="mb-4">
                    <label class="form-label required" for="confirmPassword">Xác nhận mật khẩu mới</label>
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

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm reset-submit u-bg-color-var-hcmute-blue u-border-color-var-hcmute-blue">
                    <i class="bi bi-check2-circle"></i>
                    Lưu mật khẩu mới
                </button>

                <div class="reset-assurance">
                    <span class="reset-assurance-icon"><i class="bi bi-shield-check"></i></span>
                    <div class="reset-assurance-copy">
                        <p class="reset-assurance-title">Bảo vệ tài khoản của bạn</p>
                        <p class="reset-assurance-text">Hãy sử dụng mật khẩu riêng biệt và tránh chia sẻ thông tin đăng nhập.</p>
                    </div>
                </div>
            </form>
        </c:when>
    </c:choose>
</div>

<script src="${pageContext.request.contextPath}/assets/js/pages/auth/reset-password.js"></script>
