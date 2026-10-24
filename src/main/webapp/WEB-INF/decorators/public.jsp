<%@ page pageEncoding="UTF-8" %>
    <%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
        <%@ page import="org.sitemesh.content.Content" %>
            <% Content pageContent=(Content) request.getAttribute("org.sitemesh.content.Content"); %>
                <c:set var="currentUri" value="${requestScope['jakarta.servlet.forward.request_uri']}" />
                <c:if test="${empty currentUri}">
                    <c:set var="currentUri" value="${pageContext.request.requestURI}" />
                </c:if>
                <!DOCTYPE html>
                <html lang="vi">

                <head>
                    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
<style>
:root { --public-navy: #071d3a; --public-navy-light: #102f58; --public-blue: #206bc4; --public-blue-dark: #154c91; --public-blue-bright: #3988e5; --public-red: #e34255; --public-ink: #172b4d; --public-muted: #70829a; --public-border: #e1e9f4; --public-surface: #ffffff; --public-ease: cubic-bezier(.2, .75, .3, 1); --guest-hero-padding: clamp(1.5rem, 4vw, 3rem); --guest-hero-gap: 2rem; --guest-hero-art-size: 155px; --guest-hero-art-size-tablet: 115px; --guest-hero-icon-size: 4.2rem; --guest-hero-radius-mobile: 22px; }
body.public-layout { position: relative; isolation: isolate; display: flex; flex-direction: column; min-height: 100vh; min-height: 100dvh; margin: 0; color: var(--public-ink); font-family: Inter, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif; background-color: #f7faff; background-image: radial-gradient(circle at 1px 1px, rgba(32, 107, 196, .085) 1px, transparent 1.5px), repeating-linear-gradient(135deg, transparent 0, transparent 126px, rgba(32, 107, 196, .023) 127px, rgba(32, 107, 196, .023) 128px, transparent 129px, transparent 195px), radial-gradient(ellipse at 0% 0%, rgba(66, 153, 241, .105), transparent 36%), radial-gradient(ellipse at 100% 55%, rgba(112, 158, 238, .07), transparent 32%), linear-gradient(145deg, #f7faff 0%, #ffffff 48%, #f4f8ff 100%); background-size: 28px 28px, 195px 195px, auto, auto, auto; background-attachment: fixed; -webkit-font-smoothing: antialiased; text-rendering: optimizeLegibility; overflow-x: clip; }
body.public-layout::before { content: ""; position: fixed; z-index: -1; width: clamp(360px, 46vw, 680px); aspect-ratio: 1; top: -370px; right: -220px; border: 1px solid rgba(32, 107, 196, .09); border-radius: 50%; box-shadow: 0 0 0 28px rgba(32, 107, 196, .022), 0 0 0 72px rgba(32, 107, 196, .016), 0 0 0 125px rgba(32, 107, 196, .01); pointer-events: none; animation: qa-layout-orbit 30s ease-in-out infinite alternate; }
body.public-layout::after { content: ""; position: fixed; z-index: -1; width: 350px; aspect-ratio: 1; left: -220px; bottom: -235px; border: 1px solid rgba(227, 66, 85, .09); border-radius: 50%; box-shadow: 0 0 0 30px rgba(227, 66, 85, .018), 0 0 0 70px rgba(32, 107, 196, .016); pointer-events: none; }
body.public-layout .container, body.public-layout .container-sm, body.public-layout .container-md, body.public-layout .container-lg, body.public-layout .container-xl, body.public-layout .container-xxl { width: calc(100% - clamp(2rem, 4vw, 7rem)); max-width: none !important; }
body.public-layout main.public-main > .container-xl .container { width: 100%; max-width: 100%; }
body.full-width-page main.public-main { padding: 0; }
body.full-width-page main.public-main>.container-xl { padding: 0; max-width: 100%; }
main.public-main { position: relative; z-index: 1; flex: 1 1 auto; min-width: 0; padding: 0; }
main.public-main>.container-xl { position: relative; padding-top: clamp(24px, 4vw, 46px) !important; padding-bottom: clamp(36px, 5vw, 60px) !important; }
main.public-main>.container-xl>.alert { border-radius: 14px; border: 1px solid rgba(32, 107, 196, .12); box-shadow: 0 6px 20px rgba(17, 43, 78, .045); }
main.public-main .card { border-color: rgba(222, 232, 244, .95); border-radius: 18px; box-shadow: 0 6px 24px rgba(17, 43, 78, .045); transition: border-color .25s ease, box-shadow .25s ease, transform .25s ease; }
main.public-main .card-header { background: linear-gradient(120deg, #ffffff, #f6f9ff); border-bottom-color: #e9eff7; }
main.public-main .card-title { color: #213d60; font-weight: 750; }
main.public-main .table-responsive { border-radius: 13px; }
main.public-main .table thead th { color: #647995; background: #f5f8fd; border-bottom-color: #e1e9f4; font-size: .78rem; font-weight: 750; }
main.public-main .table td { border-color: #edf1f7; vertical-align: middle; }
main.public-main .form-control, main.public-main .form-select { border-radius: 11px; border-color: #dce6f2; transition: border-color .2s ease, box-shadow .2s ease; }
main.public-main .form-control:focus, main.public-main .form-select:focus { border-color: #438de0; box-shadow: 0 0 0 4px rgba(32, 107, 196, .1); }
main.public-main .btn { border-radius: 10px; font-weight: 650; transition: transform .2s var(--public-ease), box-shadow .2s ease, background-color .2s ease; }
main.public-main .btn:not(:disabled):hover { transform: translateY(-1px); }
main.public-main .btn-primary { background: linear-gradient(120deg, #287be0, #1857a5); border-color: #206bc4; box-shadow: 0 5px 14px rgba(32, 107, 196, .15); }
main.public-main .btn-primary:hover { background: linear-gradient(120deg, #206bc4, #124b91); box-shadow: 0 8px 21px rgba(32, 107, 196, .23); }
.hcmute-navbar { position: sticky; top: 0; z-index: 1030; min-height: 132px !important; padding: 0; background: rgba(255, 255, 255, .89); -webkit-backdrop-filter: blur(20px); backdrop-filter: blur(20px); border: 0; border-bottom: 1px solid rgba(219, 230, 244, .9); box-shadow: 0 5px 26px rgba(13, 43, 79, .045); transition: background .25s ease, box-shadow .25s ease; }
.hcmute-navbar::after { content: ""; position: absolute; bottom: -1px; left: 0; width: 100%; height: 1px; background: linear-gradient(90deg, transparent, rgba(32, 107, 196, .28), rgba(227, 66, 85, .13), transparent); pointer-events: none; }
.hcmute-navbar>.container-xl { min-height: 132px !important; gap: 20px; }
.hcmute-navbar .navbar-brand { display: flex; align-items: center; gap: .6rem; padding: .65rem 0; margin-right: 1.25rem; text-decoration: none; flex-shrink: 0; }
.hcmute-navbar .brand-logo-wrap { position: relative; flex-shrink: 0; display: flex; align-items: center; justify-content: center; width: 46px; height: 46px; border: 1px solid #e0eafa; border-radius: 14px; background: linear-gradient(145deg, #ffffff, #f0f6ff); box-shadow: 0 5px 14px rgba(22, 72, 131, .075); transition: transform .25s var(--public-ease), box-shadow .25s ease; }
.hcmute-navbar .navbar-brand:hover .brand-logo-wrap { transform: translateY(-2px) rotate(-2deg); box-shadow: 0 9px 20px rgba(22, 72, 131, .13); }
.hcmute-navbar .brand-logo-wrap img { width: 38px; height: 38px; display: block; object-fit: contain; }
.hcmute-navbar .brand-logo-wrap>div { border-radius: 11px !important; background: linear-gradient(135deg, #0b3d82, #287be0) !important; box-shadow: 0 5px 13px rgba(32, 107, 196, .2); }
.hcmute-navbar .brand-text { display: flex; flex-direction: column; gap: 5px; }
.hcmute-navbar .brand-text .brand-name { font-size: 1.27rem; font-weight: 900; letter-spacing: -.055em; color: #0b3d82; line-height: 1; }
.hcmute-navbar .brand-text .brand-sub { font-size: .65rem; font-weight: 600; color: #74849a; line-height: 1.2; white-space: nowrap; }
.hcmute-navbar .navbar-collapse { align-items: center; }
.hcmute-navbar .navbar-nav { align-items: center; gap: 3px; }
.hcmute-navbar .nav-item { display: flex; align-items: center; }
.hcmute-navbar .nav-link { position: relative; display: inline-flex; align-items: center; gap: 7px; color: #53647c; font-weight: 650; font-size: .83rem; padding: .75rem .8rem; border: 0; border-radius: 11px; white-space: nowrap; transition: color .22s ease, background .22s ease, transform .22s ease; }
.hcmute-navbar .nav-link::after { content: ""; position: absolute; bottom: 4px; left: 14px; right: 14px; height: 2px; background: linear-gradient(90deg, #287be0, #64b4ff); border-radius: 3px; transform: scaleX(0); transform-origin: left; transition: transform .25s var(--public-ease); }
.hcmute-navbar .nav-link:hover, .hcmute-navbar .nav-link.active { color: #1555a0; background: rgba(32, 107, 196, .06); border-bottom-color: transparent; }
.hcmute-navbar .nav-link:hover { transform: translateY(-1px); }
.hcmute-navbar .nav-link:hover::after, .hcmute-navbar .nav-link.active::after { transform: scaleX(1); }
.hcmute-navbar .navbar-toggler { width: 43px; height: 43px; display: none; align-items: center; justify-content: center; padding: .45rem; border: 1px solid #e0e8f3 !important; border-radius: 12px; background: linear-gradient(145deg, #ffffff, #f3f7fd); box-shadow: 0 3px 9px rgba(17, 43, 78, .035); transition: border-color .2s ease, box-shadow .2s ease, transform .2s ease; }
.hcmute-navbar .navbar-toggler:hover { border-color: #b8cfea !important; box-shadow: 0 5px 13px rgba(32, 107, 196, .1); }
.hcmute-navbar .navbar-toggler:focus { box-shadow: 0 0 0 3px rgba(32, 107, 196, .12); }
.hcmute-navbar .nav-search-input { min-width: 155px; flex: 1 1 auto; width: 1%; border-radius: 12px 0 0 12px; border: 1px solid #dce6f2; border-right: 0; background: rgba(255, 255, 255, .95); padding: .58rem .8rem; font-size: .8rem; box-shadow: none; transition: border-color .2s ease, background .2s ease, box-shadow .2s ease; }
.hcmute-navbar .navbar-collapse > form[role="search"] { flex: 0 1 280px; min-width: 0; }
.hcmute-navbar .navbar-collapse > form[role="search"] .input-group { width: 100%; min-width: 0; flex-wrap: nowrap; }
.hcmute-navbar .navbar-collapse > form[role="search"] .input-group > .qa-suggest-wrapper { display: flex; flex: 1 1 0; align-items: stretch; width: 100%; min-width: 0; }
.hcmute-navbar .navbar-collapse > form[role="search"] .qa-suggest-wrapper > .nav-search-input { flex: 1 1 0; width: 100%; min-width: 0; pointer-events: auto; }
.hcmute-navbar .nav-search-input:focus { position: relative; z-index: 2; border-color: #80b0e7; background: #ffffff; box-shadow: 0 0 0 3px rgba(32, 107, 196, .08); }
.hcmute-navbar .nav-search-input::placeholder { color: #8d9cb0; }
.hcmute-navbar .nav-search-btn { display: inline-flex; flex: 0 0 auto; align-items: center; justify-content: center; min-width: 42px; border-radius: 0 12px 12px 0; background: linear-gradient(135deg, #287be0, #1857a5); color: #ffffff; border: 1px solid #206bc4; padding: .5rem .75rem; font-size: .85rem; box-shadow: 0 4px 10px rgba(32, 107, 196, .12); transition: background .2s ease, box-shadow .2s ease, transform .2s ease; }
.hcmute-navbar .nav-search-btn:hover { background: linear-gradient(135deg, #206bc4, #124b91); box-shadow: 0 6px 15px rgba(32, 107, 196, .22); }
.hcmute-navbar .btn-login-nav, .hcmute-navbar .btn-register-nav { display: inline-flex; align-items: center; justify-content: center; gap: 6px; min-height: 38px; white-space: nowrap; border-radius: 999px; padding: .48rem .9rem; font-size: .8rem; font-weight: 750; text-decoration: none; transition: color .22s ease, background .22s ease, border-color .22s ease, transform .22s ease, box-shadow .22s ease; }
.hcmute-navbar .btn-login-nav { border: 1px solid #c8daef; color: #1757a0; background: rgba(255, 255, 255, .85); }
.hcmute-navbar .btn-login-nav:hover { color: #ffffff; border-color: #206bc4; background: linear-gradient(135deg, #287be0, #1857a5); transform: translateY(-1px); box-shadow: 0 6px 15px rgba(32, 107, 196, .18); }
.hcmute-navbar .btn-register-nav { color: #ffffff; border: 1px solid #206bc4; background: linear-gradient(135deg, #287be0, #1857a5); box-shadow: 0 5px 13px rgba(32, 107, 196, .15); }
.hcmute-navbar .btn-register-nav:hover { color: #ffffff; background: linear-gradient(135deg, #206bc4, #124b91); transform: translateY(-1px); box-shadow: 0 8px 19px rgba(32, 107, 196, .24); }
.hcmute-navbar .btn-outline-danger { display: inline-flex; align-items: center; justify-content: center; min-height: 36px; font-weight: 650 !important; border-color: #f0cbd0; background: #fffafa; transition: color .2s ease, background .2s ease, box-shadow .2s ease, transform .2s ease; }
.hcmute-navbar .btn-outline-danger:hover { box-shadow: 0 5px 13px rgba(220, 53, 69, .12); }
.hcmute-navbar .d-flex.align-items-center.gap-2 { flex-shrink: 0; }
.hcmute-navbar .navbar-collapse .d-flex.me-3 { margin-right: .9rem !important; }
.hcmute-footer { position: relative; flex-shrink: 0; isolation: isolate; overflow: hidden; background: radial-gradient(ellipse at 90% 0%, rgba(53, 119, 198, .2), transparent 36%), radial-gradient(ellipse at 0% 100%, rgba(227, 66, 85, .095), transparent 34%), linear-gradient(150deg, #10294a 0%, #091d37 54%, #07162b 100%); color: #b8c7da; padding: 3.8rem 0 0; margin-top: auto; }
.hcmute-footer::before { content: ""; position: absolute; z-index: -1; top: -220px; right: -140px; width: 460px; height: 460px; border: 1px solid rgba(255, 255, 255, .1); border-radius: 50%; box-shadow: 0 0 0 30px rgba(255, 255, 255, .018), 0 0 0 70px rgba(255, 255, 255, .012); pointer-events: none; }
.hcmute-footer::after { content: ""; position: absolute; z-index: -1; inset: 0; opacity: .2; background-image: radial-gradient(circle, rgba(180, 210, 246, .28) .7px, transparent 1px); background-size: 23px 23px; -webkit-mask-image: linear-gradient(110deg, black, transparent 80%); mask-image: linear-gradient(110deg, black, transparent 80%); pointer-events: none; }
.hcmute-footer>.container-xl { position: relative; z-index: 1; }
.hcmute-footer .row { row-gap: 2.1rem !important; }
.hcmute-footer .footer-logo { width: fit-content; max-width: 100%; gap: 13px !important; }
.hcmute-footer .footer-logo img { width: 48px !important; height: 48px !important; padding: 4px; background: #ffffff; border: 1px solid rgba(255, 255, 255, .2); border-radius: 13px; box-shadow: 0 6px 17px rgba(0, 0, 0, .15); }
.hcmute-footer .footer-logo .brand-name { color: #ffffff; font-weight: 900; font-size: 1.22rem !important; letter-spacing: -.04em; }
.hcmute-footer .footer-logo .brand-sub { color: #9eb2cc; font-size: .72rem; margin-top: 4px; }
.hcmute-footer p.small { max-width: 390px; color: #a7b8cd; font-size: .83rem; line-height: 1.9; }
.hcmute-footer h6 { color: #ffffff; font-weight: 800; font-size: .78rem; text-transform: uppercase; letter-spacing: .09em; margin-bottom: 1.2rem; }
.hcmute-footer h6::after { content: ""; display: block; width: 30px; height: 2px; border-radius: 4px; margin-top: 11px; background: linear-gradient(90deg, #61b5ff, #e34255); }
.hcmute-footer ul { gap: .8rem !important; }
.hcmute-footer a { color: #a9bad0; text-decoration: none; font-size: .83rem; transition: color .2s ease, transform .2s ease; }
.hcmute-footer ul a { display: inline-flex; align-items: center; position: relative; }
.hcmute-footer ul a:hover { color: #ffffff; transform: translateX(3px); }
.hcmute-footer .social-link { width: 40px; height: 40px; background: rgba(255, 255, 255, .075); border: 1px solid rgba(255, 255, 255, .13); border-radius: 12px; display: inline-flex; align-items: center; justify-content: center; color: #ffffff; font-size: 1rem; transition: background .22s ease, color .22s ease, transform .22s ease, box-shadow .22s ease, border-color .22s ease; }
.hcmute-footer .social-link:hover { background: linear-gradient(135deg, #287be0, #1857a5); border-color: rgba(255, 255, 255, .2); color: #ffffff; transform: translateY(-3px); box-shadow: 0 8px 19px rgba(32, 107, 196, .26); }
.hcmute-footer .contact-item { display: flex; align-items: flex-start; gap: .7rem; font-size: .82rem; line-height: 1.75; margin-bottom: .9rem; color: #aebed2; }
.hcmute-footer .contact-item i { display: inline-flex; align-items: center; justify-content: center; flex: 0 0 27px; width: 27px; height: 27px; color: #84c4ff; background: rgba(98, 173, 255, .1); border: 1px solid rgba(98, 173, 255, .12); border-radius: 8px; margin-top: 0; font-size: .86rem; }
.hcmute-footer .footer-bottom { position: relative; border-top: 1px solid rgba(255, 255, 255, .11); padding: 1.25rem 0; margin-top: 2.5rem; font-size: .76rem; color: #8fa4bf; }
.hcmute-footer .footer-bottom .d-flex { gap: 12px; }
.hcmute-footer .footer-bottom span { line-height: 1.8; }
@media (min-width: 1200px) {
  .hcmute-navbar>.container-xl { max-width: none; }
}
@media (max-width: 1199.98px) {
  .hcmute-navbar>.container-xl { gap: 10px; }
  .hcmute-navbar .navbar-brand { margin-right: .45rem; }
  .hcmute-navbar .nav-link { padding: .7rem .62rem; font-size: .79rem; }
  .hcmute-navbar .nav-search-input { min-width: 120px; }
  .hcmute-navbar .navbar-collapse > form[role="search"] { flex-basis: 220px; }
  .hcmute-navbar .navbar-collapse .d-flex.me-3 { margin-right: .6rem !important; }
  .hcmute-navbar .btn-login-nav, .hcmute-navbar .btn-register-nav { padding: .45rem .72rem; font-size: .77rem; }
}
@media (max-width: 991.98px) {
  html { scroll-padding-top: 78px; }
  .hcmute-navbar { height: auto; min-height: 104px !important; flex-shrink: 0; background: #ffffff; }
  .hcmute-navbar>.container-xl { min-height: 104px !important; padding-left: 22px; padding-right: 22px; }
  .hcmute-navbar .navbar-brand { margin-right: auto; }
  .hcmute-navbar .navbar-toggler { display: inline-flex; }
  .hcmute-navbar .navbar-collapse { padding: 12px 0 18px; border-top: 1px solid #e9eff7; margin-top: 0; background: #ffffff; }
  .hcmute-navbar .navbar-nav { align-items: stretch; gap: 3px; margin-bottom: 12px !important; }
  .hcmute-navbar .nav-item { display: block; }
  .hcmute-navbar .nav-link { display: flex; min-height: 42px; padding: .7rem .9rem; border-radius: 10px; font-size: .88rem; }
  .hcmute-navbar .nav-link::after { display: none; }
  .hcmute-navbar .nav-link.active { background: linear-gradient(100deg, rgba(32, 107, 196, .1), rgba(66, 153, 241, .045)); box-shadow: inset 3px 0 0 #287be0; }
  .hcmute-navbar .navbar-collapse .d-flex.me-3 { margin: 0 0 13px !important; }
  .hcmute-navbar .navbar-collapse > form[role="search"] { flex-basis: auto; width: 100%; }
  .hcmute-navbar .nav-search-input { min-width: 0; }
  .hcmute-navbar .navbar-collapse>.d-flex.align-items-center.gap-2 { display: flex; flex-wrap: wrap; gap: 8px !important; padding-top: 5px; }
  .hcmute-navbar .btn-login-nav, .hcmute-navbar .btn-register-nav { min-height: 39px; padding-left: 1rem; padding-right: 1rem; }
  .hcmute-footer { padding-top: 3.2rem; }
}
@media (max-width: 575.98px) {
  html { scroll-padding-top: 68px; }
  body.public-layout, body.full-width-page { background-attachment: scroll; }
  body.public-layout { background-image: radial-gradient(circle at 1px 1px, rgba(32, 107, 196, .06) 1px, transparent 1.5px), radial-gradient(ellipse at 0% 0%, rgba(66, 153, 241, .075), transparent 36%), linear-gradient(145deg, #f7faff, #ffffff 52%, #f4f8ff); background-size: 28px 28px, auto, auto; }
  .hcmute-navbar { min-height: 92px !important; }
  .hcmute-navbar>.container-xl { min-height: 92px !important; padding-left: 15px; padding-right: 15px; }
  .hcmute-navbar .brand-logo-wrap { width: 41px; height: 41px; border-radius: 12px; }
  .hcmute-navbar .brand-logo-wrap img { width: 34px; height: 34px; }
  .hcmute-navbar .brand-text .brand-name { font-size: 1.15rem; }
  .hcmute-navbar .brand-text .brand-sub { font-size: .61rem; }
  .hcmute-navbar .navbar-toggler { width: 40px; height: 40px; }
  .hcmute-navbar .navbar-collapse { padding-top: 10px; padding-bottom: 15px; }
  .hcmute-navbar .navbar-collapse>.d-flex.align-items-center.gap-2 { align-items: stretch !important; }
  .hcmute-navbar .btn-login-nav, .hcmute-navbar .btn-register-nav { min-height: 40px; }
  main.public-main>.container-xl { padding-top: 20px !important; padding-bottom: 34px !important; }
  .hcmute-footer { padding-top: 2.7rem; }
  .hcmute-footer .footer-logo img { width: 43px !important; height: 43px !important; }
  .hcmute-footer .footer-bottom { margin-top: 1.7rem; }
}
@media print {
  body.public-layout, .hcmute-navbar, .hcmute-footer { background: #ffffff !important; background-image: none !important; color: #172b4d !important; box-shadow: none !important; }
  body.public-layout::before, body.public-layout::after, .hcmute-footer::before, .hcmute-footer::after { display: none !important; }
  .hcmute-navbar { position: static; }
}
</style>

                        <title>
                            <%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE HCMUTE
                        </title>
                        <%= pageContent.getExtractedProperties().getChild("head").getValue() %>

                </head>

                <body class="public-layout ${requestScope['qaute.fullWidthPage'] == true ? 'full-width-page' : ''}">

                    <nav class="navbar navbar-expand-lg hcmute-navbar">
                        <div class="container-xl">
                            <a class="navbar-brand" href="/">
                                <div class="brand-logo-wrap">
                                    <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png"
                                        alt="Logo HCMUTE"
                                        onerror="this.style.display='none';this.nextElementSibling.style.display='flex';">
                                    <div
                                        style="display:none;width:40px;height:40px;background:linear-gradient(135deg,#c0392b,#e74c3c);border-radius:10px;align-items:center;justify-content:center;color:#fff;font-weight:800;font-size:1.1rem;">
                                        Q</div>
                                </div>
                                <div class="brand-text ms-1">
                                    <div class="brand-name">QAUTE</div>
                                    <div class="brand-sub">Tư vấn Sinh viên HCMUTE</div>
                                </div>
                            </a>

                            <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse"
                                data-bs-target="#public-navbar" aria-controls="public-navbar" aria-expanded="false"
                                aria-label="Mở trình đơn">
                                <span class="navbar-toggler-icon"></span>
                            </button>

                            <div class="collapse navbar-collapse" id="public-navbar">
                                <ul class="navbar-nav me-auto mb-0">
                                    <li class="nav-item">
                                        <a class="nav-link ${currentUri == '/' or currentUri == '' ? 'active' : ''}"
                                            href="/">
                                            <i class="bi bi-house-door me-1" aria-hidden="true"></i>Trang chủ
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link ${fn:contains(currentUri, '/announcements') ? 'active' : ''}"
                                            href="/announcements">
                                            <i class="bi bi-megaphone me-1" aria-hidden="true"></i>Thông báo
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link ${fn:contains(currentUri, '/faq') ? 'active' : ''}"
                                            href="/faq">
                                            <i class="bi bi-chat-square-text me-1" aria-hidden="true"></i>Hỏi &amp; Đáp
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link ${fn:contains(currentUri, '/topics') ? 'active' : ''}"
                                            href="/topics">
                                            <i class="bi bi-diagram-3 me-1" aria-hidden="true"></i>Chủ đề tư vấn
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link ${fn:contains(currentUri, '/pages/guide') ? 'active' : ''}"
                                            href="/pages/guide">
                                            <i class="bi bi-compass me-1" aria-hidden="true"></i>Hướng dẫn
                                        </a>
                                    </li>
                                </ul>

                                <form class="d-flex me-3" method="get" action="/search" role="search">
                                    <div class="input-group">
                                        <input class="nav-search-input form-control" type="search" name="q"
                                            placeholder="Tìm kiếm…" aria-label="Tìm kiếm"
                                            value="${fn:escapeXml(param.q)}" data-qa-suggest="NAVBAR">
                                        <button class="nav-search-btn" type="submit" aria-label="Tìm">
                                            <i class="bi bi-search" aria-hidden="true"></i>
                                        </button>
                                    </div>
                                </form>

                                <div class="d-flex align-items-center gap-2">
                                    <sec:authorize access="isAnonymous()">
                                        <a href="/auth/login" class="btn-login-nav">
                                            <i class="bi bi-box-arrow-in-right me-1" aria-hidden="true"></i>Đăng nhập
                                        </a>
                                    </sec:authorize>
                                    <sec:authorize access="isAuthenticated()">
                                        <sec:authorize access="hasRole('STUDENT')">
                                            <a href="/student/dashboard" class="btn-register-nav">
                                                <i class="bi bi-grid-fill me-1" aria-hidden="true"></i>Vào hệ thống
                                            </a>
                                        </sec:authorize>
                                        <sec:authorize access="hasRole('MANAGER')">
                                            <a href="/manager/dashboard" class="btn-register-nav">
                                                <i class="bi bi-grid-fill me-1" aria-hidden="true"></i>Vào hệ thống
                                            </a>
                                        </sec:authorize>
                                        <sec:authorize access="hasRole('ADMIN')">
                                            <a href="/admin/dashboard" class="btn-register-nav">
                                                <i class="bi bi-grid-fill me-1" aria-hidden="true"></i>Vào hệ thống
                                            </a>
                                        </sec:authorize>
                                        <form method="post" action="/auth/logout" class="d-inline m-0">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                            <button type="submit"
                                                class="btn btn-outline-danger btn-sm rounded-pill px-3"
                                                style="font-weight: 500;" title="Đăng xuất khỏi hệ thống">
                                                <i class="bi bi-box-arrow-right me-1" aria-hidden="true"></i>Đăng xuất
                                            </button>
                                        </form>
                                    </sec:authorize>
                                </div>
                            </div>
                        </div>
                    </nav>

                    <main class="public-main">
                        <c:set var="isHome"
                            value="${currentUri == '/' or currentUri == '' or fn:endsWith(currentUri, '/home')}" />
                        <c:choose>
                            <c:when test="${isHome}">
                                <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
                            </c:when>
                            <c:otherwise>
                                <div class="container-xl py-4">
                                    <qa:alert />
                                    <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </main>

                    <footer class="hcmute-footer">
                        <div class="container-xl">
                            <div class="row g-4 pb-4">
                                <div class="col-lg-4">
                                    <div class="footer-logo d-flex align-items-center gap-2 mb-3">
                                        <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png"
                                            alt="Logo HCMUTE" style="height:42px;width:auto;object-fit:contain;">
                                        <div>
                                            <div class="brand-name" style="font-size:1.1rem;">QAUTE</div>
                                            <div class="brand-sub">Tư vấn Sinh viên HCMUTE</div>
                                        </div>
                                    </div>
                                    <p class="small mb-3">Hệ thống tư vấn, hỗ trợ sinh viên Trường Đại học Công nghệ Kỹ
                                        thuật TP.HCM. Chúng tôi sẵn sàng hỗ trợ bạn trong suốt hành trình học tập.</p>
                                    <div class="d-flex gap-2">
                                        <a href="https://www.facebook.com/hcmute.edu.vn/" class="social-link"
                                            aria-label="Facebook" target="_blank" rel="noopener"><i
                                                class="bi bi-facebook" aria-hidden="true"></i></a>
                                        <a href="https://www.youtube.com/@UTETVChannel" class="social-link"
                                            aria-label="YouTube" target="_blank" rel="noopener"><i class="bi bi-youtube"
                                                aria-hidden="true"></i></a>
                                    </div>
                                </div>

                                <div class="col-6 col-lg-2">
                                    <h6>Dịch vụ</h6>
                                    <ul class="list-unstyled d-flex flex-column gap-2">
                                        <li><a href="/faq">Hỏi &amp; Đáp theo chủ đề</a></li>
                                        <li><a href="/student/tickets/new">Gửi yêu cầu</a></li>
                                        <li><a href="/student/appointments/book">Đặt lịch hẹn</a></li>
                                    </ul>
                                </div>

                                <div class="col-6 col-lg-2">
                                    <h6>Về chúng tôi</h6>
                                    <ul class="list-unstyled d-flex flex-column gap-2">
                                        <li><a href="/pages/about">Giới thiệu</a></li>
                                        <li><a href="/pages/guide">Hướng dẫn sử dụng</a></li>
                                        <li><a href="/pages/privacy">Chính sách bảo mật</a></li>
                                    </ul>
                                </div>

                                <div class="col-lg-4">
                                    <h6>Liên hệ</h6>
                                    <div class="contact-item"><i class="bi bi-geo-alt-fill"
                                            aria-hidden="true"></i><span>01 Võ Văn Ngân, Linh Chiểu, TP. Thủ Đức,
                                            TP.HCM</span></div>
                                    <div class="contact-item"><i class="bi bi-telephone-fill"
                                            aria-hidden="true"></i><span>(028) 3896 6740</span></div>
                                    <div class="contact-item"><i class="bi bi-envelope-fill"
                                            aria-hidden="true"></i><span>ctsv@hcmute.edu.vn</span></div>
                                    <div class="contact-item"><i class="bi bi-clock-fill"
                                            aria-hidden="true"></i><span>07:30 – 17:00, Thứ 2 – Thứ 6</span></div>
                                </div>
                            </div>

                            <div class="footer-bottom">
                                <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
                                    <span>©
                                        <fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy" /> QAUTE –
                                        Trường ĐH Công nghệ Kỹ thuật TP.HCM
                                    </span>
                                </div>
                            </div>
                        </div>
                    </footer>

                    <%@ include file="/WEB-INF/views/common/scripts.jspf" %>
                        <%@ include file="/WEB-INF/views/common/chatbot-widget.jspf" %>
                </body>

                </html>