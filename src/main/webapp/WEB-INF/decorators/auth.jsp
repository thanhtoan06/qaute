<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page import="org.sitemesh.content.Content" %>
<%-- Lấy nội dung trang, xác định tiêu đề và URI hiện tại cho trang xác thực.
--%><%
    Content pageContent = (Content) request.getAttribute("org.sitemesh.content.Content");
    String pageTitle = "Xác thực tài khoản - QAUTE";
    if (pageContent != null && pageContent.getExtractedProperties().getChild("title") != null) {
        String t = pageContent.getExtractedProperties().getChild("title").getValue();
        if (t != null && !"null".equals(t) && !t.isBlank()) {
            pageTitle = t + " - QAUTE HCMUTE";
        }
    }
    String currentUri = request.getRequestURI();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
<style>
:root { --auth-navy: #071d3a; --auth-blue: #0b3d82; --auth-blue-light: #3988e5; --auth-red: #e34255; --auth-ink: #182f50; --auth-muted: #70829a; --auth-border: #e4ebf5; --auth-ease: cubic-bezier(.2, .75, .3, 1); }
body.bg-light { min-height: 100vh; margin: 0; color: var(--auth-ink); font-family: Inter, "Segoe UI", -apple-system, BlinkMacSystemFont, sans-serif; -webkit-font-smoothing: antialiased; text-rendering: optimizeLegibility; overflow-x: hidden; }
body.bg-light::before { content: ""; position: fixed; inset: 0; z-index: 0; pointer-events: none; background-image: radial-gradient(circle, rgba(32, 107, 196, .15) 1px, transparent 1.5px), repeating-linear-gradient( 135deg, transparent 0, transparent 130px, rgba(32, 107, 196, .022) 131px, rgba(32, 107, 196, .022) 132px, transparent 133px, transparent 200px ); background-size: 28px 28px, 200px 200px; opacity: .48; }
.hcmute-auth-wrapper { position: relative; isolation: isolate; display: flex; align-items: center; justify-content: center; min-height: 100vh; min-height: 100dvh; padding: clamp(20px, 4vw, 54px); overflow: hidden; background: radial-gradient(ellipse at 8% 10%, rgba(66, 153, 241, .16), transparent 35%), radial-gradient(ellipse at 92% 88%, rgba(227, 66, 85, .075), transparent 32%), radial-gradient(ellipse at 52% 50%, rgba(255, 255, 255, .96), rgba(247, 250, 255, .86) 70%), #f2f6fd; }
.hcmute-auth-wrapper::before { content: ""; position: absolute; z-index: -1; width: clamp(350px, 48vw, 680px); aspect-ratio: 1; top: -36%; right: -15%; border: 1px solid rgba(32, 107, 196, .12); border-radius: 50%; box-shadow: 0 0 0 25px rgba(32, 107, 196, .025), 0 0 0 65px rgba(32, 107, 196, .02), 0 0 0 115px rgba(32, 107, 196, .014); pointer-events: none; animation: qaute-ambient-drift 28s ease-in-out infinite alternate; }
.hcmute-auth-wrapper::after { content: ""; position: absolute; z-index: -1; width: 330px; aspect-ratio: 1; left: -160px; bottom: -200px; border: 1px solid rgba(227, 66, 85, .13); border-radius: 50%; box-shadow: 0 0 0 27px rgba(227, 66, 85, .025), 0 0 0 60px rgba(32, 107, 196, .018); pointer-events: none; }
.hcmute-auth-card { position: relative; z-index: 1; display: flex; width: 100%; max-width: 1160px; min-height: 650px; background: rgba(255, 255, 255, .96); border: 1px solid rgba(255, 255, 255, .92); border-radius: 30px; box-shadow: 0 35px 100px rgba(9, 42, 86, .13), 0 10px 30px rgba(9, 42, 86, .055), inset 0 1px 0 rgba(255, 255, 255, .95); overflow: hidden; animation: auth-card-enter .65s var(--auth-ease) both; }
.hcmute-auth-left { position: relative; isolation: isolate; display: flex; flex: 0 0 44%; flex-direction: column; justify-content: space-between; gap: 30px; min-width: 0; padding: clamp(30px, 4vw, 54px) clamp(25px, 3.3vw, 43px) 29px; color: #ffffff; background: radial-gradient(ellipse at 87% 5%, rgba(91, 166, 255, .31), transparent 35%), radial-gradient(ellipse at 0% 100%, rgba(227, 66, 85, .21), transparent 36%), linear-gradient(150deg, #06172f 0%, #0b3268 48%, #1255a4 100%); overflow: hidden; }
.hcmute-auth-left::before { content: ""; position: absolute; z-index: -1; width: 380px; height: 380px; top: -150px; right: -175px; border: 1px solid rgba(255, 255, 255, .17); border-radius: 50%; box-shadow: 0 0 0 28px rgba(255, 255, 255, .035), 0 0 0 67px rgba(255, 255, 255, .023), 0 0 0 112px rgba(255, 255, 255, .014); pointer-events: none; }
.hcmute-auth-left::after { content: ""; position: absolute; z-index: -1; width: 360px; height: 360px; bottom: -210px; left: -185px; border-radius: 50%; background: radial-gradient(circle, rgba(69, 146, 245, .23), transparent 70%); pointer-events: none; }
.hcmute-auth-left-bg { position: absolute; inset: 0; z-index: -2; background-size: cover; background-position: center; opacity: .14; mix-blend-mode: luminosity; pointer-events: none; }
.hcmute-auth-left-content { position: relative; z-index: 1; }
.hcmute-auth-left-content .d-flex.align-items-center.gap-2.mb-4 { flex-wrap: wrap; row-gap: 12px; }
.hcmute-auth-left-content img[alt="HCMUTE Logo"] { height: 45px !important; width: 45px !important; object-fit: contain; border: 1px solid rgba(255, 255, 255, .24); border-radius: 13px !important; box-shadow: 0 7px 20px rgba(0, 0, 0, .14); }
.hcmute-auth-left .hcmute-logo-icon { width: 45px; height: 45px; display: inline-flex; align-items: center; justify-content: center; flex-shrink: 0; color: #ffffff; border: 1px solid rgba(255, 255, 255, .22); border-radius: 13px; box-shadow: 0 7px 20px rgba(0, 0, 0, .14); transition: transform .3s var(--auth-ease), box-shadow .3s ease; }
.hcmute-auth-left .hcmute-logo-icon:hover { transform: translateY(-3px) rotate(-4deg); box-shadow: 0 12px 26px rgba(0, 0, 0, .2); }
.hcmute-auth-left .fs-4.fw-bold { font-size: 1.7rem !important; font-weight: 850 !important; letter-spacing: -.055em; }
.hcmute-brand-red { color: #ff7181 !important; }
.hcmute-auth-left .badge.bg-white { display: inline-flex; align-items: center; width: fit-content; max-width: 100%; gap: 3px; white-space: normal; line-height: 1.65; color: #164e91 !important; background: rgba(255, 255, 255, .94) !important; border: 1px solid rgba(255, 255, 255, .64); border-radius: 999px !important; padding: .55rem .95rem !important; box-shadow: 0 8px 20px rgba(0, 0, 0, .09); font-size: .76rem; }
.hcmute-auth-left h2 { color: #ffffff; font-size: clamp(1.85rem, 2.6vw, 2.55rem); font-weight: 850; line-height: 1.2 !important; letter-spacing: -.045em; margin-top: 14px; text-wrap: balance; }
.hcmute-auth-left p { font-size: .91rem; line-height: 1.85; }
.hcmute-auth-left .text-white-50 { color: rgba(235, 243, 255, .74) !important; }
.hcmute-auth-left .d-flex.flex-column.gap-3.pt-2 { gap: 16px !important; margin-top: 24px; }
.hcmute-auth-left .d-flex.flex-column.gap-3.pt-2 > .d-flex { align-items: flex-start !important; gap: 12px !important; padding: 10px 12px; border: 1px solid rgba(255, 255, 255, .075); border-radius: 13px; background: rgba(255, 255, 255, .045); transition: background .25s ease, border-color .25s ease, transform .25s ease; }
.hcmute-auth-left .d-flex.flex-column.gap-3.pt-2 > .d-flex:hover { transform: translateX(4px); background: rgba(255, 255, 255, .09); border-color: rgba(255, 255, 255, .16); }
.hcmute-auth-left .d-flex.flex-column.gap-3.pt-2 .rounded-circle { width: 31px !important; height: 31px !important; border: 1px solid rgba(255, 255, 255, .45); background: linear-gradient(135deg, #ffffff, #e5f1ff) !important; color: #1555a0 !important; box-shadow: 0 4px 12px rgba(0, 0, 0, .1); }
.hcmute-auth-left .d-flex.flex-column.gap-3.pt-2 span { padding-top: 4px; color: rgba(245, 249, 255, .91); font-size: .79rem !important; line-height: 1.65; }
.hcmute-auth-left-content.pt-4.border-top { padding-top: 20px !important; border-color: rgba(255, 255, 255, .15) !important; }
.hcmute-auth-left-content.pt-4.border-top p { margin: 0; font-size: .76rem; line-height: 1.8; }
.hcmute-auth-right { position: relative; display: flex; flex: 1 1 56%; flex-direction: column; justify-content: center; min-width: 0; padding: clamp(28px, 4.3vw, 60px); background: radial-gradient(ellipse at 100% 0%, rgba(66, 153, 241, .055), transparent 34%), linear-gradient(155deg, rgba(255, 255, 255, .99), rgba(250, 252, 255, .98)); }
.hcmute-auth-right > .d-flex.justify-content-between.align-items-center.mb-4 { gap: 15px; margin-bottom: 34px !important; }
.hcmute-auth-right > .d-flex.justify-content-between.align-items-center.mb-4 > a { display: inline-flex; align-items: center; gap: 7px; color: #74839a !important; text-decoration: none; font-size: .79rem !important; font-weight: 600; transition: color .22s ease, transform .22s ease; }
.hcmute-auth-right > .d-flex.justify-content-between.align-items-center.mb-4 > a:hover { color: #206bc4 !important; transform: translateX(-3px); }
.hcmute-auth-right > .d-flex.justify-content-between.align-items-center.mb-4 > a .bi { font-size: 1rem; }
.hcmute-auth-right .d-lg-none .fw-bold { font-size: 1.13rem; letter-spacing: -.04em; }
.hcmute-auth-right > qa\:alert, .hcmute-auth-right .alert { border-radius: 13px; }
.hcmute-auth-right .alert { border: 1px solid rgba(32, 107, 196, .12); box-shadow: 0 5px 16px rgba(17, 43, 78, .04); }
.hcmute-auth-right h1, .hcmute-auth-right h2, .hcmute-auth-right h3 { color: #142f52; font-weight: 850; letter-spacing: -.04em; line-height: 1.25; text-wrap: balance; }
.hcmute-auth-right h1 { font-size: clamp(1.85rem, 3vw, 2.45rem); margin-bottom: 12px; }
.hcmute-auth-right h2 { font-size: clamp(1.45rem, 2.4vw, 1.9rem); }
.hcmute-auth-right p { color: #72829a; line-height: 1.8; }
.hcmute-auth-right .form-label { color: #344b68; font-size: .82rem; font-weight: 700; margin-bottom: 8px; }
.hcmute-auth-right .form-control, .hcmute-auth-right .form-select { min-height: 48px; color: #203953; border: 1px solid #dce6f2; border-radius: 12px; background-color: #ffffff; box-shadow: 0 2px 5px rgba(20, 47, 85, .015); transition: border-color .22s ease, box-shadow .22s ease, background-color .22s ease; }
.hcmute-auth-right .form-control:hover, .hcmute-auth-right .form-select:hover { border-color: #b9cde7; }
.hcmute-auth-right .form-control:focus, .hcmute-auth-right .form-select:focus { border-color: #438de0; background: #ffffff; box-shadow: 0 0 0 4px rgba(32, 107, 196, .105); outline: 0; }
.hcmute-auth-right .form-control::placeholder { color: #9aa8ba; }
.hcmute-auth-right .input-group { flex-wrap: nowrap; }
.hcmute-auth-right .input-group > .form-control { min-width: 0; }
.hcmute-auth-right .input-group .btn, .hcmute-auth-right .input-group-text { min-height: 48px; border-color: #dce6f2; background: #f8faff; color: #6c7f99; }
.hcmute-auth-right .input-group > .form-control:not(:last-child), .hcmute-auth-right .input-group > .form-select:not(:last-child) { border-top-right-radius: 0; border-bottom-right-radius: 0; }
.hcmute-auth-right .input-group > .btn:last-child, .hcmute-auth-right .input-group > .input-group-text:last-child { border-radius: 0 12px 12px 0; }
.hcmute-auth-right .input-group .btn:hover { color: #206bc4; background: #edf5ff; }
.hcmute-auth-right .btn { border-radius: 11px; font-weight: 700; transition: transform .22s var(--auth-ease), box-shadow .22s ease, background-color .22s ease; }
.hcmute-auth-right .btn:not(:disabled):hover { transform: translateY(-1px); }
.hcmute-auth-right .btn-primary { color: #ffffff; border-color: #206bc4; background: linear-gradient(115deg, #287be0, #1857a5); box-shadow: 0 7px 17px rgba(32, 107, 196, .19); }
.hcmute-auth-right .btn-primary:hover { color: #ffffff; border-color: #154c91; background: linear-gradient(115deg, #206bc4, #123f81); box-shadow: 0 10px 24px rgba(32, 107, 196, .26); }
.hcmute-auth-right .btn-outline-primary { color: #206bc4; border-color: #c8daef; background: #ffffff; }
.hcmute-auth-right .btn-outline-primary:hover { color: #ffffff; border-color: #206bc4; background: #206bc4; }
.hcmute-auth-right .form-check-input { border-color: #bdcde0; cursor: pointer; }
.hcmute-auth-right .form-check-input:checked { border-color: #206bc4; background-color: #206bc4; }
.hcmute-auth-right .form-check-label { color: #64748b; font-size: .82rem; line-height: 1.65; }
.hcmute-auth-right a:not(.btn) { text-underline-offset: 3px; transition: color .2s ease; }
.hcmute-auth-right .progress { height: 6px; overflow: hidden; border-radius: 999px; background: #eaf0f8; }
.hcmute-auth-right .progress-bar { border-radius: 999px; transition: width .3s ease, background-color .3s ease; }
.hcmute-auth-right .invalid-feedback { font-size: .77rem; line-height: 1.6; }
.hcmute-auth-right .text-muted { color: #8594a8 !important; }
@keyframes auth-card-enter {
  from { opacity: 0; transform: translateY(17px) scale(.993); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}
@media (min-width: 1400px) {
  .hcmute-auth-card { max-width: 1200px; min-height: 690px; }
  .hcmute-auth-left { flex-basis: 45%; padding: 58px 48px 32px; }
  .hcmute-auth-right { padding: 64px; }
}
@media (max-width: 991.98px) {
  .hcmute-auth-wrapper { padding: 28px; }
  .hcmute-auth-card { max-width: 590px; min-height: auto; border-radius: 25px; }
  .hcmute-auth-left { display: none !important; }
  .hcmute-auth-right { flex: 1 1 100%; padding: clamp(28px, 6vw, 48px); }
  .hcmute-auth-right > .d-flex.justify-content-between.align-items-center.mb-4 { margin-bottom: 30px !important; }
}
@media (max-width: 575.98px) {
  .hcmute-auth-wrapper { align-items: flex-start; min-height: 100vh; min-height: 100dvh; padding: 13px; }
  .hcmute-auth-card { width: 100%; margin: auto 0; border-radius: 21px; box-shadow: 0 20px 55px rgba(9, 42, 86, .11); }
  .hcmute-auth-right { padding: 24px 19px 27px; }
  .hcmute-auth-right > .d-flex.justify-content-between.align-items-center.mb-4 { margin-bottom: 27px !important; }
  .hcmute-auth-right h1 { font-size: 1.8rem; }
  .hcmute-auth-right .form-control, .hcmute-auth-right .form-select { min-height: 46px; font-size: .9rem; }
  .hcmute-auth-right .input-group .btn, .hcmute-auth-right .input-group-text { min-height: 46px; }
  .hcmute-auth-right .btn-primary { box-shadow: 0 5px 14px rgba(32, 107, 196, .15); }
  .hcmute-auth-wrapper::before { top: -10%; right: -70%; opacity: .7; }
}
@media (prefers-reduced-motion: reduce) {
  .hcmute-auth-wrapper::before, .hcmute-auth-card, .hcmute-auth-left .hcmute-logo-icon { animation: none !important; transition: none !important; }
}
</style>

    <title><%= pageTitle %></title>
    <%= pageContent != null && pageContent.getExtractedProperties().getChild("head") != null
            && pageContent.getExtractedProperties().getChild("head").getValue() != null
            && !"null".equals(pageContent.getExtractedProperties().getChild("head").getValue())
            ? pageContent.getExtractedProperties().getChild("head").getValue() : "" %>

</head>
<body class="bg-light">
    <%-- Khung đăng nhập/đăng ký gồm giới thiệu dịch vụ và nội dung xác thực hiện tại. --%>
    <div class="hcmute-auth-wrapper">
        <%-- Bảng giới thiệu thương hiệu, tiện ích hỗ trợ và thông tin trường. --%>
        <div class="hcmute-auth-card">
            <div class="hcmute-auth-left">
                <div class="hcmute-auth-left-bg" style="background-image: url('${pageContext.request.contextPath}/assets/images/campus-hcmute.png');"></div>
                <div class="hcmute-auth-left-content">
                    <div class="d-flex align-items-center gap-2 mb-4">
                        <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png" alt="HCMUTE Logo" class="rounded bg-white p-1" style="height: 42px; width: auto;" onerror="this.style.display='none';">
                        <div class="hcmute-logo-icon" style="background: rgba(255,255,255,0.2);">
                            <i class="bi bi-mortarboard-fill"></i>
                        </div>
                        <span class="fs-4 fw-bold text-white">
                            QA<span class="hcmute-brand-red">UTE</span>
                        </span>
                    </div>

                    <span class="badge bg-white text-primary px-3 py-1.5 rounded-pill fw-semibold mb-3">
                        <i class="bi bi-patch-check-fill me-1 text-primary"></i>Hệ thống tư vấn sinh viên HCMUTE
                    </span>

                    <h2 class="display-6 fw-bold text-white mb-3" style="line-height: 1.25;">
                        Đồng hành cùng sinh viên Công nghệ Kỹ thuật.
                    </h2>

                    <p class="text-white-50 leading-relaxed mb-4">
                        Tra cứu thông tin học vụ, gửi yêu cầu hỗ trợ, trò chuyện trực tuyến và kết nối chuyên viên tư vấn dễ dàng, tiện lợi.
                    </p>

                    <div class="d-flex flex-column gap-3 pt-2">
                        <div class="d-flex align-items-center gap-3">
                            <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center" style="width: 32px; height: 32px; flex-shrink: 0;">
                                <i class="bi bi-check-lg fw-bold"></i>
                            </div>
                            <span class="text-white small">Phản hồi nhanh chóng trong vòng 24 giờ làm việc</span>
                        </div>

                        <div class="d-flex align-items-center gap-3">
                            <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center" style="width: 32px; height: 32px; flex-shrink: 0;">
                                <i class="bi bi-check-lg fw-bold"></i>
                            </div>
                            <span class="text-white small">Đặt lịch hẹn tư vấn 1-1 trực tiếp hoặc Google Meet</span>
                        </div>

                        <div class="d-flex align-items-center gap-3">
                            <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center" style="width: 32px; height: 32px; flex-shrink: 0;">
                                <i class="bi bi-check-lg fw-bold"></i>
                            </div>
                            <span class="text-white small">Bảo mật thông tin sinh viên tuyệt đối</span>
                        </div>
                    </div>
                </div>

                <div class="hcmute-auth-left-content pt-4 border-top border-white-10">
                    <p class="text-white-50 small mb-0">
                        <i class="bi bi-geo-alt me-1 text-warning"></i>01 Võ Văn Ngân, P. Linh Chiểu, TP. Thủ Đức, TP.HCM
                    </p>
                </div>
            </div>

            <%-- Khu vực hiển thị thông báo và biểu mẫu xác thực của trang con. --%>
            <div class="hcmute-auth-right">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <a href="/" class="text-secondary text-decoration-none small d-inline-flex align-items-center hover-primary">
                        <i class="bi bi-arrow-left me-1"></i>Trang chủ QAUTE
                    </a>
                    <div class="d-lg-none d-flex align-items-center gap-1">
                        <span class="fw-bold text-primary">QA<span class="text-danger">UTE</span></span>
                    </div>
                </div>

                <qa:alert/>

                <%= pageContent != null && pageContent.getExtractedProperties().getChild("body") != null
                        && pageContent.getExtractedProperties().getChild("body").getValue() != null
                        && !"null".equals(pageContent.getExtractedProperties().getChild("body").getValue())
                        ? pageContent.getExtractedProperties().getChild("body").getValue() : "" %>
            </div>
        </div>
    </div>

    <%-- Nạp các script dùng chung cho trang xác thực. --%>
    <%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>