<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Kích hoạt tài khoản</title>
<style>
.activation-page { --activation-blue: #2369c7; --activation-blue-dark: #174c98; --activation-blue-light: #eaf3ff; --activation-ink: #1c3555; --activation-muted: #7d8ea5; --activation-border: #e2eaf4; position: relative; isolation: isolate; color: var(--activation-ink); animation: qaute-enter .45s cubic-bezier(.2, .75, .3, 1) both; }
.activation-page *, .activation-page *::before, .activation-page *::after { box-sizing: border-box; }
.activation-page::before { content: ""; position: absolute; z-index: -1; top: -38px; right: -24px; width: 148px; height: 148px; border-radius: 50%; border: 1px solid rgba(48, 119, 211, .09); box-shadow: 0 0 0 17px rgba(48, 119, 211, .025), 0 0 0 36px rgba(48, 119, 211, .018); pointer-events: none; }
.activation-page::after { content: ""; position: absolute; z-index: -1; left: -30px; bottom: -38px; width: 105px; height: 105px; border-radius: 50%; background: radial-gradient(circle, rgba(48, 119, 211, .065), transparent 72%); pointer-events: none; }
@keyframes activation-icon-enter {
  from { opacity: 0; transform: scale(.8) rotate(-8deg); }
  to { opacity: 1; transform: scale(1) rotate(0); }
}
@keyframes activation-icon-glow {
  0%, 100% { box-shadow: 0 9px 24px rgba(35, 105, 199, .17), 0 0 0 8px rgba(35, 105, 199, .035); }
  50% { box-shadow: 0 12px 29px rgba(35, 105, 199, .23), 0 0 0 12px rgba(35, 105, 199, .018); }
}
.activation-heading { position: relative; margin-bottom: 1.75rem; }
.activation-eyebrow { display: inline-flex; align-items: center; gap: .45rem; margin-bottom: .9rem; padding: .38rem .72rem; border: 1px solid #dce9fa; border-radius: 999px; background: linear-gradient(135deg, #f4f8ff, #eaf3ff); color: #326db6; font-size: .68rem; font-weight: 800; letter-spacing: .075em; text-transform: uppercase; }
.activation-eyebrow .bi { font-size: .78rem; }
.activation-title { margin: 0 0 .55rem; color: var(--activation-ink); font-size: clamp(1.55rem, 4vw, 1.95rem); font-weight: 850; line-height: 1.23; letter-spacing: -.045em; }
.activation-description { max-width: 450px; margin: 0; color: var(--activation-muted); font-size: .875rem; line-height: 1.8; }
.activation-icon-wrap { position: relative; display: inline-flex; align-items: center; justify-content: center; width: 68px; height: 68px; margin-bottom: 1.4rem; border: 1px solid rgba(255, 255, 255, .95); border-radius: 21px; background: linear-gradient(145deg, #ffffff, #edf5ff); color: var(--activation-blue); box-shadow: 0 9px 24px rgba(35, 105, 199, .17), 0 0 0 8px rgba(35, 105, 199, .035); animation: activation-icon-enter .5s cubic-bezier(.2, .75, .3, 1) both, activation-icon-glow 4s ease-in-out 1s infinite; }
.activation-icon-wrap .bi { font-size: 1.85rem; line-height: 1; }
.activation-icon-wrap::after { content: ""; position: absolute; top: 5px; right: 5px; width: 7px; height: 7px; border-radius: 50%; background: #46c69b; box-shadow: 0 0 0 3px rgba(70, 198, 155, .12); }
.activation-form { position: relative; }
.activation-form .form-label { display: flex; align-items: center; gap: .35rem; margin-bottom: .55rem; color: #344e6d; font-size: .81rem; font-weight: 750; }
.activation-form .form-label.required::after { color: #e05b6b; }
.activation-form .input-group { position: relative; flex-wrap: nowrap; min-height: 51px; border: 1px solid #dce6f2; border-radius: 13px; background: #ffffff; box-shadow: 0 3px 10px rgba(22, 54, 91, .025); transition: border-color .22s ease, box-shadow .22s ease, transform .22s ease, background .22s ease; }
.activation-form .input-group:hover { border-color: #bfd3eb; box-shadow: 0 4px 14px rgba(22, 54, 91, .045); }
.activation-form .input-group:focus-within { border-color: #6aa0e4; background: #ffffff; box-shadow: 0 0 0 4px rgba(35, 105, 199, .09), 0 7px 17px rgba(22, 54, 91, .045); }
.activation-form .input-group-text { display: inline-flex; align-items: center; justify-content: center; min-width: 47px; padding: 0 .9rem; border: 0 !important; border-radius: 12px 0 0 12px !important; background: transparent !important; color: #8197b3 !important; font-size: 1.05rem; transition: color .2s ease; }
.activation-form .input-group:focus-within .input-group-text { color: var(--activation-blue) !important; }
.activation-form .form-control { min-width: 0; min-height: 49px; padding: .75rem .3rem; border: 0 !important; border-radius: 0 !important; outline: 0; background: transparent !important; color: #243c5a; box-shadow: none !important; font-size: .875rem; }
.activation-form .form-control::placeholder { color: #a0afc1; opacity: 1; }
.activation-form .form-control.is-invalid { background-image: none; }
.activation-form .input-group:has(.form-control.is-invalid) { border-color: #e8a5ad; background: #fffafb; }
.activation-form .input-group:has(.form-control.is-invalid):focus-within { border-color: #da7581; box-shadow: 0 0 0 4px rgba(220, 68, 88, .075); }
.activation-form .btn-toggle-password { display: inline-flex; align-items: center; justify-content: center; flex: 0 0 45px; width: 45px; min-height: 49px; padding: .5rem; border: 0 !important; border-radius: 0 12px 12px 0 !important; background: transparent !important; color: #8294ab !important; box-shadow: none !important; transition: color .2s ease, background .2s ease; }
.activation-form .input-group button[data-toggle-password] { display: inline-flex; align-items: center; justify-content: center; flex: 0 0 45px; width: 45px; min-height: 49px; padding: .5rem; border: 0 !important; border-radius: 0 12px 12px 0 !important; background: transparent !important; color: #8294ab !important; box-shadow: none !important; transition: color .2s ease, background .2s ease; }
.activation-form .input-group button[data-toggle-password]:hover { color: var(--activation-blue) !important; background: #f3f7fd !important; }
.activation-form .input-group button[data-toggle-password]:focus-visible { outline-offset: -4px; }
.activation-form .input-group button[data-toggle-password] .bi { font-size: 1.05rem; }
.activation-password-hint { display: flex; align-items: flex-start; gap: .4rem; margin-top: .65rem; color: #8a9bb0; font-size: .72rem; line-height: 1.6; }
.activation-password-hint .bi { margin-top: 2px; color: #7296c3; flex-shrink: 0; }
.activation-form .progress { height: 5px !important; margin-top: .75rem !important; overflow: hidden; border-radius: 99px; background: #eaf0f8; }
.activation-form .progress-bar { border-radius: 99px; background-image: linear-gradient(100deg, #2873ce, #67b5ff, #2873ce); background-size: 200% 100%; animation: qa-progress-shimmer 5s linear infinite; transition: width .3s ease, background-color .3s ease; }
.activation-form #password-strength-text { display: block; min-height: 1.2em; margin-top: .4rem; color: #8999ad; font-size: .72rem !important; line-height: 1.5; }
.activation-form .invalid-feedback, .activation-form .form-hint { line-height: 1.6; }
.activation-form .invalid-feedback { font-size: .76rem; }
.activation-form .activation-submit { position: relative; display: flex; align-items: center; justify-content: center; gap: .55rem; min-height: 51px; padding: .8rem 1.2rem; overflow: hidden; border: 1px solid #2467c5; border-radius: 13px !important; background: linear-gradient(115deg, #2d78d4 0%, #205eb8 52%, #184e9d 100%) !important; color: #ffffff; box-shadow: 0 7px 18px rgba(35, 105, 199, .2), inset 0 1px 0 rgba(255, 255, 255, .15); font-size: .88rem; font-weight: 750; letter-spacing: .005em; transition: transform .22s ease, box-shadow .22s ease, filter .22s ease; }
.activation-form .activation-submit::before { content: ""; position: absolute; top: 0; left: -120%; width: 70%; height: 100%; background: linear-gradient(105deg, transparent, rgba(255, 255, 255, .18), transparent); transform: skewX(-18deg); transition: left .6s ease; pointer-events: none; }
.activation-form .activation-submit:hover { color: #ffffff; filter: saturate(1.08); transform: translateY(-2px); box-shadow: 0 11px 24px rgba(35, 105, 199, .27), inset 0 1px 0 rgba(255, 255, 255, .2); }
.activation-form .activation-submit:hover::before { left: 150%; }
.activation-form .activation-submit:active { transform: translateY(0) scale(.99); }
.activation-form .activation-submit:focus-visible { outline-offset: 4px; }
.activation-form .activation-submit .bi { font-size: 1rem; }
.activation-assurance { display: flex; align-items: center; justify-content: center; gap: .55rem; margin-top: 1.25rem; padding-top: 1rem; border-top: 1px solid #edf1f7; color: #8798ad; font-size: .72rem; text-align: center; }
.activation-assurance .bi { color: #398a75; font-size: .95rem; }
.activation-alert { margin-bottom: 1.2rem; padding: .8rem .9rem; border: 1px solid #f4c8ce; border-radius: 12px; background: linear-gradient(120deg, #fff8f9, #fffdfd); color: #a93c4e; box-shadow: 0 4px 12px rgba(173, 57, 78, .04); font-size: .79rem; line-height: 1.65; }
.activation-alert .bi { align-self: flex-start; margin-top: 2px; font-size: 1rem; }
.activation-invalid { position: relative; padding: .7rem .2rem .4rem; text-align: center; }
.activation-invalid .activation-icon-wrap { color: #d94b60; background: linear-gradient(145deg, #ffffff, #fff0f2); box-shadow: 0 9px 24px rgba(217, 75, 96, .12), 0 0 0 8px rgba(217, 75, 96, .035); animation: activation-icon-enter .5s cubic-bezier(.2, .75, .3, 1) both; }
.activation-invalid .activation-icon-wrap::after { display: none; }
.activation-invalid .activation-title { font-size: clamp(1.35rem, 4vw, 1.7rem); }
.activation-invalid .activation-description { max-width: 430px; margin-right: auto; margin-left: auto; line-height: 1.8; }
.activation-invalid .activation-back { display: inline-flex; align-items: center; justify-content: center; gap: .5rem; min-height: 45px; padding: .7rem 1.25rem; border: 1px solid #2467c5; border-radius: 12px !important; background: linear-gradient(115deg, #2d78d4, #184e9d); color: #ffffff; box-shadow: 0 6px 16px rgba(35, 105, 199, .17); font-size: .83rem; font-weight: 750; text-decoration: none; transition: transform .2s ease, box-shadow .2s ease, background .2s ease; }
.activation-invalid .activation-back:hover { color: #ffffff; background: linear-gradient(115deg, #236bc7, #143f82); transform: translateY(-2px); box-shadow: 0 10px 21px rgba(35, 105, 199, .24); }
.activation-invalid .activation-back:active { transform: translateY(0) scale(.99); }
@media (max-width: 575.98px) {
  .activation-page::before { top: -25px; right: -24px; width: 105px; height: 105px; }
  .activation-page::after { left: -20px; bottom: -25px; width: 75px; height: 75px; }
  .activation-heading { margin-bottom: 1.35rem; }
  .activation-title { font-size: 1.55rem; }
  .activation-icon-wrap { width: 60px; height: 60px; margin-bottom: 1.2rem; border-radius: 18px; }
  .activation-icon-wrap .bi { font-size: 1.65rem; }
  .activation-form .input-group { min-height: 48px; }
  .activation-form .input-group-text { min-width: 42px; padding-right: .7rem; padding-left: .7rem; }
  .activation-form .form-control, .activation-form .input-group button[data-toggle-password] { min-height: 46px; }
  .activation-form .activation-submit { min-height: 48px; }
  .activation-invalid { padding-top: .35rem; }
}
@media (prefers-reduced-motion: reduce) {
  .activation-page, .activation-page *, .activation-page *::before, .activation-page *::after { animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>




<div class="activation-page">
    <c:choose>
        <c:when test="${!tokenValid}">
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
                    <div class="progress mt-2" style="height: 4px;">
                        <div id="password-strength" class="progress-bar"></div>
                    </div>
                    <small id="password-strength-text" class="form-hint text-muted small" style="font-size: 0.75rem;"></small>
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

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold rounded-3 shadow-sm activation-submit" id="register-submit"
                        style="background-color: var(--hcmute-blue); border-color: var(--hcmute-blue);">
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