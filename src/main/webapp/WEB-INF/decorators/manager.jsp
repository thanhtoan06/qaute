<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page import="org.sitemesh.content.Content" %>
<%
    Content pageContent = (Content) request.getAttribute("org.sitemesh.content.Content");
%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/common/tv2-workspace-user.jspf" %>

<c:set var="rawUri" value="${not empty requestScope['jakarta.servlet.forward.request_uri'] ? requestScope['jakarta.servlet.forward.request_uri'] : pageContext.request.requestURI}"/>
<c:set var="currentPath" value="${fn:startsWith(rawUri, ctx) ? fn:substring(rawUri, fn:length(ctx), fn:length(rawUri)) : rawUri}"/>
<c:set var="navPath" value="${fn:startsWith(currentPath, '/manager/tickets') ? '/manager/queue' : currentPath}"/>

<c:set var="menuPaths" value="/manager/dashboard,/manager/queue,/manager/chat,/manager/schedule,/manager/appointments,/manager/students,/manager/canned-responses,/manager/faq,/manager/reports,/manager/profile"/>
<c:set var="activePath" value=""/>
<c:forTokens items="${menuPaths}" delims="," var="menuPath">
    <c:if test="${navPath == menuPath or fn:startsWith(navPath, menuPath.concat('/'))}">
        <c:if test="${fn:length(menuPath) gt fn:length(activePath)}">
            <c:set var="activePath" value="${menuPath}"/>
        </c:if>
    </c:if>
</c:forTokens>

<c:set var="mgrStatus" value="${fn:toUpperCase(managerStatus)}"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <link rel="stylesheet" href="${ctx}/assets/css/tv2-workspace.css">
<style>
:root { --mgr-accent: #3985e8; --mgr-accent-dark: #2165bd; --mgr-accent-deep: #164b91; --mgr-accent-soft: #edf5ff; --mgr-ink: #1d3553; --mgr-muted: #7e90a8; --mgr-border: #e3ebf5; --mgr-surface: #ffffff; --mgr-radius: 16px; --mgr-shadow-sm: 0 4px 14px rgba(18, 48, 84, .045); --mgr-shadow: 0 10px 28px rgba(18, 48, 84, .075); --mgr-shadow-lg: 0 20px 48px rgba(14, 39, 74, .13); --mgr-ease: cubic-bezier(.2, .75, .3, 1); }
body.qa-workspace--manager { position: relative; isolation: isolate; min-height: 100vh; min-height: 100dvh; color: #344860; background-color: #f4f7fc; background-image: radial-gradient(circle at 1px 1px, rgba(45, 113, 202, .065) 1px, transparent 1.5px), radial-gradient(ellipse at 78% 0%, rgba(62, 139, 235, .105), transparent 36%), radial-gradient(ellipse at 0% 85%, rgba(94, 125, 211, .055), transparent 33%), linear-gradient(145deg, #f5f8fd 0%, #fbfdff 53%, #f0f5fc 100%); background-size: 28px 28px, auto, auto, auto; background-attachment: fixed; -webkit-font-smoothing: antialiased; text-rendering: optimizeLegibility; overflow-x: hidden; }
body.qa-workspace--manager::before { content: ""; position: fixed; z-index: -1; top: -330px; right: -220px; width: clamp(360px, 43vw, 650px); aspect-ratio: 1; border: 1px solid rgba(45, 113, 202, .09); border-radius: 50%; box-shadow: 0 0 0 34px rgba(45, 113, 202, .018), 0 0 0 78px rgba(45, 113, 202, .012), 0 0 0 132px rgba(45, 113, 202, .008); pointer-events: none; animation: qa-layout-orbit 28s ease-in-out infinite alternate; }
body.qa-workspace--manager::after { content: ""; position: fixed; z-index: -1; left: -190px; bottom: -240px; width: 360px; aspect-ratio: 1; border: 1px solid rgba(45, 113, 202, .065); border-radius: 50%; box-shadow: 0 0 0 32px rgba(45, 113, 202, .012), 0 0 0 72px rgba(45, 113, 202, .009); pointer-events: none; }
@keyframes mgr-status-pulse {
  0%, 100% { box-shadow: 0 0 0 3px rgba(39, 183, 130, .12); }
  50% { box-shadow: 0 0 0 6px rgba(39, 183, 130, .035); }
}
body.qa-workspace--manager ::selection { color: #ffffff; background: #246ec9; }
body.qa-workspace--manager :focus-visible { outline: 3px solid rgba(57, 133, 232, .35); outline-offset: 3px; }
.qa-workspace-skip { border-radius: 10px; box-shadow: var(--mgr-shadow); }
body.qa-workspace--manager .page { min-height: 100vh; min-height: 100dvh; }
body.qa-workspace--manager .qa-workspace-sidebar { position: fixed; isolation: isolate; background: radial-gradient(ellipse at 90% 0%, rgba(49, 117, 199, .23), transparent 36%), radial-gradient(ellipse at 0% 76%, rgba(37, 87, 147, .13), transparent 40%), linear-gradient(170deg, #102c50 0%, #0a1b34 49%, #07172c 100%); border-right: 1px solid rgba(255, 255, 255, .07); box-shadow: 8px 0 32px rgba(7, 23, 44, .105); scrollbar-width: thin; scrollbar-color: #345172 transparent; }
body.qa-workspace--manager .qa-workspace-sidebar::before { content: ""; position: absolute; z-index: 0; top: 115px; right: -185px; width: 320px; height: 320px; border: 1px solid rgba(255, 255, 255, .065); border-radius: 50%; box-shadow: 0 0 0 29px rgba(255, 255, 255, .017), 0 0 0 66px rgba(255, 255, 255, .012); pointer-events: none; }
body.qa-workspace--manager .qa-workspace-sidebar::after { content: ""; position: absolute; z-index: 0; left: -145px; bottom: 100px; width: 230px; height: 230px; border-radius: 50%; background: radial-gradient(circle, rgba(48, 128, 225, .12), transparent 70%); pointer-events: none; }
body.qa-workspace--manager .qa-workspace-sidebar > .container-fluid { position: relative; z-index: 1; }
body.qa-workspace--manager .qa-workspace-sidebar::-webkit-scrollbar { width: 4px; }
body.qa-workspace--manager .qa-workspace-sidebar::-webkit-scrollbar-thumb { background: rgba(255, 255, 255, .18); border-radius: 5px; }
body.qa-workspace--manager .qa-workspace-sidebar::-webkit-scrollbar-thumb:hover { background: rgba(255, 255, 255, .3); }
body.qa-workspace--manager .navbar-brand { padding-top: 1.15rem; padding-bottom: 1.1rem; }
body.qa-workspace--manager .navbar-brand > a { display: flex; align-items: center; gap: .75rem; width: fit-content; max-width: 100%; text-decoration: none; }
body.qa-workspace--manager .qa-role-logo { width: 40px; height: 40px; padding: 3px; object-fit: contain; background: #ffffff; border: 1px solid rgba(255, 255, 255, .2); border-radius: 12px; box-shadow: 0 6px 18px rgba(0, 0, 0, .15); transition: transform .25s var(--mgr-ease), box-shadow .25s ease; }
body.qa-workspace--manager .navbar-brand > a:hover .qa-role-logo { transform: translateY(-2px) rotate(-3deg); box-shadow: 0 10px 23px rgba(0, 0, 0, .21); }
body.qa-workspace--manager .qa-workspace-brand-copy { color: #ffffff; font-size: 1.08rem; line-height: 1.1; letter-spacing: -.045em; font-weight: 850; }
body.qa-workspace--manager .qa-workspace-brand-copy > span { color: #ffffff; }
body.qa-workspace--manager .qa-workspace-brand-copy small { display: block; margin-top: 4px; color: #9eb2cc; font-size: .62rem; line-height: 1.5; letter-spacing: .03em; font-weight: 650; }
body.qa-workspace--manager .qa-workspace-user-card { position: relative; display: flex; align-items: center; gap: .75rem; margin: .9rem .75rem .3rem; padding: .85rem .9rem; border: 1px solid rgba(255, 255, 255, .07); border-radius: 15px; background: linear-gradient(135deg, rgba(255, 255, 255, .085), rgba(255, 255, 255, .035)); text-decoration: none; transition: background .22s ease, border-color .22s ease, transform .22s ease, box-shadow .22s ease; }
body.qa-workspace--manager .qa-workspace-user-card:hover { background: linear-gradient(135deg, rgba(255, 255, 255, .14), rgba(255, 255, 255, .055)); border-color: rgba(137, 196, 255, .22); transform: translateY(-1px); box-shadow: 0 8px 20px rgba(0, 0, 0, .1); }
body.qa-workspace--manager .qa-workspace-avatar { display: inline-flex; align-items: center; justify-content: center; flex: 0 0 auto; width: 40px; height: 40px; border-radius: 13px; border: 2px solid rgba(255, 255, 255, .19); background: linear-gradient(135deg, #4a9af0, #2463bb); color: #ffffff; font-size: .88rem; font-weight: 850; letter-spacing: -.02em; box-shadow: 0 5px 15px rgba(29, 89, 166, .25); transition: transform .25s ease, box-shadow .25s ease; }
body.qa-workspace--manager .qa-workspace-user-card:hover .qa-workspace-avatar { transform: translateY(-1px) rotate(-3deg); box-shadow: 0 8px 20px rgba(29, 89, 166, .34); }
body.qa-workspace--manager .qa-workspace-user-copy { display: flex; flex: 1 1 auto; flex-direction: column; gap: 4px; min-width: 0; }
body.qa-workspace--manager .qa-workspace-user-copy strong { overflow: hidden; color: #ffffff; font-size: .82rem; font-weight: 700; line-height: 1.45; text-overflow: ellipsis; white-space: nowrap; }
body.qa-workspace--manager .qa-workspace-user-copy small { color: #a2b7d2; font-size: .68rem; }
body.qa-workspace--manager .qa-workspace-user-card > .bi-chevron-right { flex-shrink: 0; color: #7e9bbc; font-size: .75rem; transition: color .2s ease, transform .2s ease; }
body.qa-workspace--manager .qa-workspace-user-card:hover > .bi-chevron-right { color: #dcecff; transform: translateX(3px); }
body.qa-workspace--manager .qa-workspace-nav-label { padding: 1.3rem 1.25rem .5rem; color: #8099b8; font-size: .63rem; font-weight: 800; letter-spacing: .12em; }
body.qa-workspace--manager .qa-workspace-sidebar .navbar-nav { gap: 4px; }
body.qa-workspace--manager .qa-workspace-sidebar .nav-item { position: relative; margin: 0; border-radius: 11px; }
body.qa-workspace--manager .qa-workspace-sidebar .nav-link { position: relative; display: flex; align-items: center; gap: .75rem; min-height: 43px; margin: 0; padding: .65rem .85rem; border: 1px solid transparent; border-radius: 11px; color: rgba(225, 236, 250, .72); font-size: .81rem; font-weight: 550; text-decoration: none; transition: color .2s ease, background .2s ease, border-color .2s ease, transform .2s ease, box-shadow .2s ease; }
body.qa-workspace--manager .qa-workspace-sidebar .nav-link-icon { display: inline-flex; align-items: center; justify-content: center; width: 21px; min-width: 21px; color: #9cb7d7; font-size: 1.02rem; text-align: center; transition: color .2s ease, transform .2s ease; }
body.qa-workspace--manager .qa-workspace-sidebar .nav-link-title { min-width: 0; line-height: 1.45; }
body.qa-workspace--manager .qa-workspace-sidebar .nav-link:hover { color: #ffffff; background: rgba(255, 255, 255, .075); border-color: rgba(255, 255, 255, .035); transform: translateX(2px); }
body.qa-workspace--manager .qa-workspace-sidebar .nav-link:hover .nav-link-icon { color: #b9dcff; transform: scale(1.08); }
body.qa-workspace--manager .qa-workspace-sidebar .nav-item.active > .nav-link { color: #ffffff; font-weight: 700; background: linear-gradient(110deg, #206bc4, #3286db); border-color: rgba(255, 255, 255, .1); box-shadow: 0 6px 16px rgba(9, 61, 121, .28), inset 0 1px 0 rgba(255, 255, 255, .1); }
body.qa-workspace--manager .qa-workspace-sidebar .nav-item.active > .nav-link::after { content: ""; position: absolute; top: 9px; right: 0; bottom: 9px; width: 3px; border-radius: 4px 0 0 4px; background: #c1e2ff; box-shadow: 0 0 12px rgba(193, 226, 255, .46); }
body.qa-workspace--manager .qa-workspace-sidebar .nav-item.active > .nav-link .nav-link-icon { color: #ffffff; }
body.qa-workspace--manager .qa-workspace-sidebar .navbar-toggler { width: 39px; height: 39px; padding: .4rem; border: 1px solid rgba(255, 255, 255, .15); border-radius: 11px; background: rgba(255, 255, 255, .075); box-shadow: none; transition: background .2s ease, transform .2s ease, border-color .2s ease; }
body.qa-workspace--manager .qa-workspace-sidebar .navbar-toggler:hover { background: rgba(255, 255, 255, .13); border-color: rgba(255, 255, 255, .23); transform: translateY(-1px); }
body.qa-workspace--manager .qa-workspace-sidebar-footer { display: flex; align-items: center; gap: .55rem; margin-top: auto; padding: .85rem .75rem; border-top: 1px solid rgba(255, 255, 255, .085); color: #9db2cc; font-size: .69rem; }
body.qa-workspace--manager .qa-workspace-role-dot { width: 7px; height: 7px; flex-shrink: 0; border-radius: 50%; background: #43c99b; box-shadow: 0 0 0 3px rgba(67, 201, 155, .12); animation: mgr-status-pulse 3s ease-in-out infinite; }
body.qa-workspace--manager .qa-workspace-sidebar-footer a { display: inline-flex; align-items: center; justify-content: center; width: 29px; height: 29px; margin-left: auto; border: 1px solid rgba(255, 255, 255, .1); border-radius: 9px; background: rgba(255, 255, 255, .045); color: #b5c8df; text-decoration: none; transition: background .2s ease, color .2s ease, transform .2s ease; }
body.qa-workspace--manager .qa-workspace-sidebar-footer a:hover { color: #ffffff; background: rgba(255, 255, 255, .12); transform: translateY(-1px); }
body.qa-workspace--manager .qa-workspace-topbar { position: fixed; top: 0; right: 0; left: var(--qa-workspace-sidebar-width); z-index: 1030; height: var(--qa-workspace-topbar-height); min-height: var(--qa-workspace-topbar-height); background: rgba(255, 255, 255, .91); -webkit-backdrop-filter: blur(18px); backdrop-filter: blur(18px); border-bottom: 1px solid rgba(224, 233, 245, .9); box-shadow: 0 5px 23px rgba(16, 43, 77, .045); transition: left .25s ease, background .25s ease, box-shadow .25s ease; }
body.qa-workspace--manager .qa-workspace-topbar::after { content: ""; position: absolute; right: 0; bottom: -1px; left: 0; height: 1px; background: linear-gradient(90deg, transparent, rgba(57, 133, 232, .18), transparent); pointer-events: none; }
body.qa-workspace--manager .qa-workspace-topbar > .container-xl { display: flex; align-items: center; gap: 1rem; min-height: var(--qa-workspace-topbar-height); padding: 0 1.5rem; }
body.qa-workspace--manager .qa-sidebar-toggle { display: inline-flex; align-items: center; justify-content: center; flex: 0 0 39px; width: 39px; height: 39px; padding: 0; border: 1px solid #206bc4; border-radius: 11px; background: linear-gradient(135deg, #287be0, #185caf); color: #ffffff; box-shadow: 0 4px 12px rgba(32, 107, 196, .2); font-size: 1.2rem; transition: color .2s ease, border-color .2s ease, background .2s ease, transform .2s ease, box-shadow .2s ease; }
body.qa-workspace--manager .qa-sidebar-toggle:hover { color: #ffffff; border-color: #154c91; background: linear-gradient(135deg, #206bc4, #154c91); box-shadow: 0 7px 17px rgba(32, 107, 196, .27); transform: translateY(-1px); }
body.qa-workspace--manager .qa-sidebar-toggle:active { transform: scale(.97); }
body.qa-workspace--manager .qa-topbar-brand { display: inline-flex; align-items: center; gap: .45rem; color: var(--mgr-ink); text-decoration: none; }
body.qa-workspace--manager .qa-topbar-brand img { width: 31px; height: 31px; padding: 2px; object-fit: contain; border: 1px solid #e1eaf5; border-radius: 9px; background: #ffffff; }
body.qa-workspace--manager .qa-topbar-brand strong { color: #183756; font-size: 1rem; font-weight: 850; letter-spacing: -.04em; }
body.qa-workspace--manager .qa-topbar-brand strong span { color: var(--mgr-accent); }
body.qa-workspace--manager .qa-workspace-location { display: flex; flex-direction: column; justify-content: center; gap: 4px; min-width: 0; padding-left: .15rem; }
body.qa-workspace--manager .qa-workspace-location small { color: #8295ae; font-size: .63rem; line-height: 1.3; font-weight: 800; letter-spacing: .1em; }
body.qa-workspace--manager .qa-workspace-location strong { overflow: hidden; color: #1a3555; font-size: 1rem; font-weight: 800; letter-spacing: -.02em; line-height: 1.35; text-overflow: ellipsis; white-space: nowrap; }
body.qa-workspace--manager .qa-workspace-presence { position: relative; }
body.qa-workspace--manager .qa-presence-toggle { display: inline-flex; align-items: center; justify-content: center; gap: .5rem; min-height: 38px; padding: .45rem .75rem; border: 1px solid #e0e9f4; border-radius: 11px; background: #ffffff; color: #425a76; box-shadow: 0 3px 9px rgba(16, 43, 77, .035); font-size: .77rem; font-weight: 750; white-space: nowrap; transition: border-color .2s ease, background .2s ease, box-shadow .2s ease, transform .2s ease; }
body.qa-workspace--manager .qa-presence-toggle:hover, body.qa-workspace--manager .qa-presence-toggle[aria-expanded="true"] { border-color: #cbdced; background: #f8fbff; box-shadow: 0 5px 13px rgba(16, 43, 77, .065); transform: translateY(-1px); }
body.qa-workspace--manager .qa-presence-toggle::after { margin-left: .08rem; opacity: .55; font-size: .65rem; }
body.qa-workspace--manager .qa-presence-dot { display: inline-block; width: 8px; height: 8px; flex: 0 0 8px; border-radius: 50%; }
body.qa-workspace--manager .qa-presence-dot--ONLINE { background: #24b985; box-shadow: 0 0 0 3px rgba(36, 185, 133, .12); }
body.qa-workspace--manager .qa-presence-dot--BUSY { background: #f0a43b; box-shadow: 0 0 0 3px rgba(240, 164, 59, .12); }
body.qa-workspace--manager .qa-presence-dot--OFFLINE { background: #92a0b2; box-shadow: 0 0 0 3px rgba(146, 160, 178, .13); }
body.qa-workspace--manager .qa-workspace-presence .dropdown-menu { min-width: 185px; padding: .55rem; border: 1px solid #e0e9f4; border-radius: 14px; box-shadow: var(--mgr-shadow-lg); }
body.qa-workspace--manager .qa-workspace-presence .dropdown-menu .btn { min-height: 38px; border-radius: 9px; font-size: .78rem; font-weight: 700; transition: background .18s ease, color .18s ease, border-color .18s ease, transform .18s ease; }
body.qa-workspace--manager .qa-status-online { color: #13865e; background: #effbf6; border: 1px solid #ccefe0; }
body.qa-workspace--manager .qa-status-online:hover, body.qa-workspace--manager .qa-status-online.active { color: #ffffff; background: #168b63; border-color: #168b63; }
body.qa-workspace--manager .qa-status-busy { color: #a96a11; background: #fff8eb; border: 1px solid #f5dfb6; }
body.qa-workspace--manager .qa-status-busy:hover, body.qa-workspace--manager .qa-status-busy.active { color: #ffffff; background: #d99224; border-color: #d99224; }
body.qa-workspace--manager .qa-status-offline { color: #66788e; background: #f4f7fb; border: 1px solid #e1e8f0; }
body.qa-workspace--manager .qa-status-offline:hover, body.qa-workspace--manager .qa-status-offline.active { color: #ffffff; background: #718198; border-color: #718198; }
body.qa-workspace--manager .qa-workspace-topbar .navbar-nav { gap: .55rem !important; }
body.qa-workspace--manager .qa-workspace-topbar .nav-link { border-radius: 11px; }
body.qa-workspace--manager .qa-workspace-topbar .nav-item.dropdown > .nav-link { align-items: center; gap: .7rem; min-height: 43px; padding: .3rem .5rem; border: 1px solid transparent; transition: background .2s ease, border-color .2s ease; }
body.qa-workspace--manager .qa-workspace-topbar .nav-item.dropdown > .nav-link:hover, body.qa-workspace--manager .qa-workspace-topbar .nav-item.dropdown > .nav-link[aria-expanded="true"] { border-color: #e4ecf6; background: #f5f8fd; }
body.qa-workspace--manager .qa-workspace-avatar--small { width: 36px; height: 36px; border-radius: 12px; font-size: .77rem; }
body.qa-workspace--manager .qa-workspace-topbar .nav-item.dropdown > .nav-link div { color: #2c425c; font-size: .8rem; font-weight: 750; }
body.qa-workspace--manager .qa-workspace-topbar .nav-item.dropdown > .nav-link .small { color: #8a9bb0 !important; font-size: .69rem; font-weight: 500; }
body.qa-workspace--manager .qa-workspace-topbar .dropdown-menu, body.qa-workspace--manager .qa-workspace-sidebar .dropdown-menu { padding: .5rem; border: 1px solid #e1e9f4; border-radius: 13px; box-shadow: var(--mgr-shadow-lg); animation: qa-workspace-page-enter .18s ease both; }
body.qa-workspace--manager .qa-workspace-topbar .dropdown-item, body.qa-workspace--manager .qa-workspace-sidebar .dropdown-item { display: flex; align-items: center; min-height: 38px; padding: .6rem .75rem; border-radius: 8px; color: #465c76; font-size: .8rem; transition: background .18s ease, color .18s ease; }
body.qa-workspace--manager .qa-workspace-topbar .dropdown-item:hover, body.qa-workspace--manager .qa-workspace-sidebar .dropdown-item:hover { color: #1f63b3; background: #eef5ff; }
body.qa-workspace--manager .qa-workspace-topbar .dropdown-divider, body.qa-workspace--manager .qa-workspace-sidebar .dropdown-divider { border-color: #eaf0f7; }
body.qa-workspace--manager .page-wrapper { min-width: 0; background: transparent; }
body.qa-workspace--manager .page-body { padding-top: 1.6rem; padding-bottom: 1.7rem; animation: qa-workspace-page-enter .4s var(--mgr-ease) both; }
body.qa-workspace--manager .qa-workspace-container { width: 100%; max-width: 1720px; }
body.qa-workspace--manager .qa-workspace-container > .alert { border-radius: 13px; }
body.qa-workspace--manager .page-header { margin-bottom: 1.5rem; border: 0; }
body.qa-workspace--manager .page-header .page-pretitle { color: #8095af; font-size: .68rem; font-weight: 800; letter-spacing: .1em; }
body.qa-workspace--manager .page-header .page-title, body.qa-workspace--manager .page-header h1, body.qa-workspace--manager .page-header h2 { color: #1b385a; font-weight: 850; letter-spacing: -.04em; }
body.qa-workspace--manager .page-header .page-title { line-height: 1.3; }
body.qa-workspace--manager .page-header .text-secondary, body.qa-workspace--manager .page-header .text-muted { color: #7e91a9 !important; }
body.qa-workspace--manager .breadcrumb { gap: .15rem; font-size: .78rem; }
body.qa-workspace--manager .breadcrumb a { color: #6f86a2; text-decoration: none; transition: color .2s ease; }
body.qa-workspace--manager .breadcrumb a:hover { color: var(--mgr-accent-dark); }
body.qa-workspace--manager .card { overflow: visible; border: 1px solid rgba(224, 233, 244, .96); border-radius: var(--mgr-radius); background: rgba(255, 255, 255, .96); box-shadow: var(--mgr-shadow-sm); transition: border-color .23s ease, box-shadow .23s ease, transform .23s ease; }
body.qa-workspace--manager .card-header { padding: 1rem 1.2rem; border-bottom: 1px solid #edf1f7; border-radius: var(--mgr-radius) var(--mgr-radius) 0 0; background: linear-gradient(125deg, #ffffff, #f7faff); }
body.qa-workspace--manager .card-header .card-title, body.qa-workspace--manager .card-title { margin-bottom: 0; color: #2b4462; font-size: .94rem; font-weight: 800; }
body.qa-workspace--manager .card-body { padding: 1.2rem; }
body.qa-workspace--manager .card-footer { padding: .9rem 1.2rem; border-top: 1px solid #edf1f7; border-radius: 0 0 var(--mgr-radius) var(--mgr-radius); background: #fbfcff; }
body.qa-workspace--manager .card .card { box-shadow: none; }
body.qa-workspace--manager .card .card-header .btn, body.qa-workspace--manager .card .card-body .btn { border-radius: 10px; }
body.qa-workspace--manager .btn { border-radius: 10px; font-weight: 650; transition: color .2s ease, background-color .2s ease, border-color .2s ease, box-shadow .2s ease, transform .2s ease; }
body.qa-workspace--manager .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
body.qa-workspace--manager .btn:not(:disabled):not(.btn-close):active { transform: translateY(0) scale(.985); }
body.qa-workspace--manager .btn-primary { color: #ffffff; background: linear-gradient(120deg, #3985e8, #2165bd); border-color: #2872ca; box-shadow: 0 4px 12px rgba(33, 101, 189, .14); }
body.qa-workspace--manager .btn-primary:hover, body.qa-workspace--manager .btn-primary:focus { color: #ffffff; background: linear-gradient(120deg, #2c75d3, #174f99); border-color: #1e5ba6; box-shadow: 0 7px 18px rgba(33, 101, 189, .23); }
body.qa-workspace--manager .btn-outline-primary { color: #2467b8; border-color: #c8d9ef; background: rgba(255, 255, 255, .92); }
body.qa-workspace--manager .btn-outline-primary:hover, body.qa-workspace--manager .btn-outline-primary:focus { color: #ffffff; background: linear-gradient(120deg, #3985e8, #2165bd); border-color: #2872ca; }
body.qa-workspace--manager .btn-outline-secondary { color: #5b7089; border-color: #dbe5f0; background: #ffffff; }
body.qa-workspace--manager .btn-outline-secondary:hover { color: #294e7b; border-color: #bdcee3; background: #f3f7fc; }
body.qa-workspace--manager .btn-light { border-color: #e5edf6; background: #ffffff; color: #445b76; }
body.qa-workspace--manager .btn-light:hover { background: #f3f7fd; border-color: #d1deed; color: #244f84; }
body.qa-workspace--manager .btn-danger { box-shadow: 0 4px 11px rgba(210, 57, 78, .1); }
body.qa-workspace--manager .btn-success { box-shadow: 0 4px 11px rgba(24, 150, 103, .1); }
body.qa-workspace--manager .btn .bi, body.qa-workspace--manager .dropdown-item .bi { vertical-align: -.08em; }
body.qa-workspace--manager a { text-underline-offset: 3px; }
body.qa-workspace--manager a.text-primary, body.qa-workspace--manager .text-primary { color: #246dc2 !important; }
body.qa-workspace--manager .bg-primary { background: linear-gradient(120deg, #3985e8, #2165bd) !important; }
body.qa-workspace--manager .border-primary { border-color: #3985e8 !important; }
body.qa-workspace--manager .badge { padding: .38em .62em; border-radius: 7px; font-size: .72em; font-weight: 700; letter-spacing: .01em; }
body.qa-workspace--manager .badge.bg-primary { background: linear-gradient(120deg, #3985e8, #2165bd) !important; }
body.qa-workspace--manager .form-label { margin-bottom: .45rem; color: #425a76; font-size: .81rem; font-weight: 750; }
body.qa-workspace--manager .form-control, body.qa-workspace--manager .form-select { min-height: 41px; color: #2c425d; border: 1px solid #dce6f2; border-radius: 10px; background-color: rgba(255, 255, 255, .97); transition: border-color .2s ease, box-shadow .2s ease, background-color .2s ease; }
body.qa-workspace--manager textarea.form-control { min-height: 110px; }
body.qa-workspace--manager .form-control::placeholder { color: #9baabd; }
body.qa-workspace--manager .form-control:hover, body.qa-workspace--manager .form-select:hover { border-color: #bdd1e9; }
body.qa-workspace--manager .form-control:focus, body.qa-workspace--manager .form-select:focus { border-color: #66a0e5; background-color: #ffffff; box-shadow: 0 0 0 4px rgba(57, 133, 232, .1); }
body.qa-workspace--manager .form-check-input { border-color: #b9cadd; cursor: pointer; }
body.qa-workspace--manager .form-check-input:checked { border-color: #2872ca; background-color: #2872ca; }
body.qa-workspace--manager .input-group-text { color: #7187a1; border-color: #dce6f2; border-radius: 10px; background: #f5f8fc; }
body.qa-workspace--manager .table-responsive { border-radius: 12px; }
body.qa-workspace--manager .table { margin-bottom: 0; color: #435873; vertical-align: middle; }
body.qa-workspace--manager .table > :not(caption) > * > * { padding: .85rem .9rem; border-bottom-color: #edf1f7; }
body.qa-workspace--manager .table thead th { color: #70839c; background: linear-gradient(180deg, #f9fbfe, #f2f6fb); border-bottom: 1px solid #e3eaf4; font-size: .71rem; font-weight: 800; letter-spacing: .055em; text-transform: uppercase; white-space: nowrap; }
body.qa-workspace--manager .table tbody tr { transition: background-color .18s ease; }
body.qa-workspace--manager .table tbody tr:hover > * { background-color: #f5f9ff; color: inherit; }
body.qa-workspace--manager .table tbody tr:last-child > * { border-bottom: 0; }
body.qa-workspace--manager .table .btn { white-space: nowrap; }
body.qa-workspace--manager .nav-tabs { gap: 4px; border-bottom-color: #e4ebf4; }
body.qa-workspace--manager .nav-tabs .nav-link { padding: .7rem .9rem; color: #7387a0; border-radius: 10px 10px 0 0; font-size: .82rem; font-weight: 700; transition: color .2s ease, background .2s ease, border-color .2s ease; }
body.qa-workspace--manager .nav-tabs .nav-link:hover { color: #2165bd; background: #f6f9fe; }
body.qa-workspace--manager .nav-tabs .nav-link.active { color: #2165bd; border-color: #e4ebf4 #e4ebf4 #ffffff; background: #ffffff; }
body.qa-workspace--manager .list-group { overflow: hidden; border-radius: 12px; }
body.qa-workspace--manager .list-group-item { border-color: #eaf0f7; color: #455c77; transition: background-color .18s ease, color .18s ease; }
body.qa-workspace--manager .list-group-item-action:hover { color: #245d9f; background: #f4f8ff; }
body.qa-workspace--manager .list-group-item.active { color: #ffffff; background: linear-gradient(110deg, #2165bd, #3985e8); border-color: #2872ca; }
body.qa-workspace--manager .dropdown-menu { min-width: 185px; }
body.qa-workspace--manager .pagination { gap: 4px; }
body.qa-workspace--manager .pagination .page-link { border-color: #e0e8f2; border-radius: 9px; background: #ffffff; color: #546b87; box-shadow: 0 2px 5px rgba(17, 43, 78, .025); transition: color .18s ease, background .18s ease, border-color .18s ease, transform .18s ease; }
body.qa-workspace--manager .pagination .page-link:hover { color: #2165bd; border-color: #c3d7ef; background: #f3f8ff; transform: translateY(-1px); }
body.qa-workspace--manager .pagination .active .page-link { color: #ffffff; background: linear-gradient(120deg, #3985e8, #2165bd); border-color: #2872ca; }
body.qa-workspace--manager .alert { border-radius: 13px; box-shadow: 0 4px 13px rgba(17, 43, 78, .035); }
body.qa-workspace--manager .progress { overflow: hidden; border-radius: 99px; background: #eaf0f8; }
body.qa-workspace--manager .progress-bar { border-radius: 99px; background-image: linear-gradient(100deg, #2165bd, #4a9bf0, #2165bd); background-size: 200% 100%; animation: qa-progress-shimmer 5s linear infinite; }
body.qa-workspace--manager .page-wrapper .text-secondary, body.qa-workspace--manager .page-wrapper .text-muted { color: #7f91a8 !important; }
body.qa-workspace--manager .page-wrapper .footer { margin-top: .5rem; padding-top: .9rem; padding-bottom: 1.1rem; border-top: 1px solid rgba(222, 232, 244, .8); background: rgba(255, 255, 255, .35); }
body.qa-workspace--manager .qa-workspace-footer { display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: .75rem; color: #8999ad; font-size: .74rem; }
body.qa-workspace--manager .qa-workspace-footer a { display: inline-flex; align-items: center; gap: .35rem; color: #637d9d; font-weight: 700; text-decoration: none; transition: color .2s ease, gap .2s ease; }
body.qa-workspace--manager .qa-workspace-footer a:hover { gap: .5rem; color: #2165bd; }
body.qa-workspace--manager .qa-workspace-overlay[hidden] { display: none !important; }
@media (max-width: 1199.98px) {
  body.qa-workspace--manager .page-body { padding-top: 1.35rem; }
  body.qa-workspace--manager .qa-workspace-topbar > .container-xl { gap: .75rem; }
  body.qa-workspace--manager .qa-workspace-location strong { font-size: .9rem; }
}
@media (max-width: 991.98px) {
  body.qa-workspace--manager { background-attachment: scroll; }
  body.qa-workspace--manager .qa-workspace-sidebar { box-shadow: 16px 0 46px rgba(6, 24, 46, .23); }
  body.qa-workspace--manager .qa-workspace-topbar { left: 0; }
  body.qa-workspace--manager .qa-workspace-topbar > .container-xl { min-height: var(--qa-workspace-topbar-height); padding-right: 1.15rem; padding-left: 1.15rem; }
  body.qa-workspace--manager .qa-workspace-location { flex: 1 1 auto; }
  body.qa-workspace--manager .qa-workspace-container { max-width: 100%; }
}
@media (max-width: 767.98px) {
  body.qa-workspace--manager .qa-workspace-topbar > .container-xl { gap: .55rem; }
  body.qa-workspace--manager .qa-workspace-location small { font-size: .55rem; }
  body.qa-workspace--manager .qa-workspace-location strong { font-size: .85rem; }
  body.qa-workspace--manager .qa-presence-toggle { min-height: 35px; padding: .4rem .55rem; font-size: .72rem; }
  body.qa-workspace--manager .qa-workspace-topbar .navbar-nav { gap: .35rem !important; }
  body.qa-workspace--manager .page-body { padding-top: 1.1rem; padding-bottom: 1.2rem; }
  body.qa-workspace--manager .card { border-radius: 14px; }
  body.qa-workspace--manager .card-header, body.qa-workspace--manager .card-body, body.qa-workspace--manager .card-footer { padding: 1rem; }
  body.qa-workspace--manager .table > :not(caption) > * > * { padding: .72rem; }
  body.qa-workspace--manager .page-header .page-title { font-size: 1.35rem; }
}
@media (max-width: 575.98px) {
  body.qa-workspace--manager { background-image: radial-gradient(circle at 1px 1px, rgba(45, 113, 202, .05) 1px, transparent 1.5px), radial-gradient(ellipse at 78% 0%, rgba(62, 139, 235, .07), transparent 36%), linear-gradient(145deg, #f5f8fd 0%, #fbfdff 53%, #f0f5fc 100%); background-size: 28px 28px, auto, auto; }
  body.qa-workspace--manager::before { top: -200px; right: -260px; opacity: .65; }
  body.qa-workspace--manager .qa-workspace-topbar > .container-xl { padding-right: .75rem; padding-left: .75rem; gap: .45rem; }
  body.qa-workspace--manager .qa-sidebar-toggle { flex-basis: 36px; width: 36px; height: 36px; border-radius: 10px; }
  body.qa-workspace--manager .qa-topbar-brand { gap: .3rem; }
  body.qa-workspace--manager .qa-topbar-brand img { width: 27px; height: 27px; }
  body.qa-workspace--manager .qa-topbar-brand strong { font-size: .91rem; }
  body.qa-workspace--manager .qa-workspace-location { gap: 3px; }
  body.qa-workspace--manager .qa-workspace-location small { font-size: .49rem; letter-spacing: .07em; }
  body.qa-workspace--manager .qa-workspace-location strong { max-width: 125px; font-size: .78rem; }
  body.qa-workspace--manager .qa-presence-toggle { min-height: 33px; padding: .35rem .4rem; gap: .35rem; font-size: .66rem; }
  body.qa-workspace--manager .qa-presence-toggle::after { margin-left: 0; }
  body.qa-workspace--manager .qa-workspace-topbar .navbar-nav { gap: .25rem !important; }
  body.qa-workspace--manager .qa-workspace-topbar .nav-item.dropdown > .nav-link { gap: .25rem; padding: .2rem; }
  body.qa-workspace--manager .qa-workspace-avatar--small { width: 32px; height: 32px; border-radius: 10px; }
  body.qa-workspace--manager .page-body { padding: .95rem .7rem 1rem; }
  body.qa-workspace--manager .page-header { margin-bottom: 1rem; }
  body.qa-workspace--manager .page-header .page-title { font-size: 1.22rem; }
  body.qa-workspace--manager .card-header, body.qa-workspace--manager .card-body, body.qa-workspace--manager .card-footer { padding: .85rem; }
  body.qa-workspace--manager .table > :not(caption) > * > * { padding: .65rem; }
  body.qa-workspace--manager .qa-workspace-footer { font-size: .69rem; }
}
@media (prefers-reduced-motion: reduce) {
  body.qa-workspace--manager *, body.qa-workspace--manager *::before, body.qa-workspace--manager *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
  body.qa-workspace--manager::before { animation: none !important; }
}
@media print {
  body.qa-workspace--manager { background: #ffffff !important; background-image: none !important; }
  body.qa-workspace--manager::before, body.qa-workspace--manager::after { display: none !important; }
  body.qa-workspace--manager .page-body { animation: none !important; }
  body.qa-workspace--manager .card { box-shadow: none !important; break-inside: avoid; }
}
</style>

    <title><%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE</title>
    <%= pageContent.getExtractedProperties().getChild("head").getValue() %>

</head>
<body class="qa-workspace qa-workspace--manager">
<a class="qa-workspace-skip" href="#workspace-main">Chuyển đến nội dung</a>
<button class="qa-workspace-overlay" type="button" data-workspace-close hidden aria-label="Đóng menu điều hướng"></button>
<div class="page">

    <aside class="qa-workspace-sidebar" id="workspace-sidebar" data-bs-theme="dark" aria-label="Điều hướng tư vấn viên">
        <div class="container-fluid">
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#qa-sidebar-menu"
                    aria-controls="qa-sidebar-menu" aria-expanded="false" aria-label="Mở menu">
                <span class="navbar-toggler-icon"></span>
            </button>
            <h1 class="navbar-brand navbar-brand-autodark">
                <a href="${ctx}/manager/dashboard" class="text-decoration-none">
                    <img src="${ctx}/assets/images/hcmute-logo.png" alt="Logo HCMUTE" class="qa-role-logo">
                    <span class="qa-workspace-brand-copy">QA<span>UTE</span><small>CỔNG HỖ TRỢ SINH VIÊN</small></span>
                </a>
            </h1>
            <a class="qa-workspace-user-card" href="${ctx}/manager/profile">
                <span class="qa-workspace-avatar"><c:out value="${workspaceInitial}"/></span>
                <span class="qa-workspace-user-copy"><strong><c:out value="${workspaceName}"/></strong><small>Tư vấn viên</small></span>
                <i class="bi bi-chevron-right" aria-hidden="true"></i>
            </a>
            <div class="collapse navbar-collapse" id="qa-sidebar-menu">
                <ul class="navbar-nav pt-lg-3">
                    <li class="qa-workspace-nav-label">KHÔNG GIAN LÀM VIỆC</li>
                    <li class="nav-item">
                        <a class="nav-link" href="${ctx}/" aria-label="Quay về trang chủ Guest">
                            <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-globe2"></i></span>
                            <span class="nav-link-title">Trang chủ</span>
                        </a>
                    </li>
                    <li class="nav-item ${activePath == '/manager/dashboard' ? 'active' : ''}">
                        <a class="nav-link" href="${ctx}/manager/dashboard">
                            <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-speedometer2"></i></span>
                            <span class="nav-link-title">Tổng quan</span>
                        </a>
                    </li>
                    <li class="nav-item ${activePath == '/manager/queue' ? 'active' : ''}">
                        <a class="nav-link" href="${ctx}/manager/queue">
                            <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-inbox"></i></span>
                            <span class="nav-link-title">Hàng đợi yêu cầu</span>
                        </a>
                    </li>
                    <li class="nav-item ${activePath == '/manager/chat' ? 'active' : ''}">
                        <a class="nav-link" href="${ctx}/manager/chat">
                            <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-chat-dots"></i></span>
                            <span class="nav-link-title">Tư vấn trực tuyến</span>
                        </a>
                    </li>
                    <li class="nav-item ${activePath == '/manager/schedule' ? 'active' : ''}">
                        <a class="nav-link" href="${ctx}/manager/schedule">
                            <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-calendar-week"></i></span>
                            <span class="nav-link-title">Lịch làm việc</span>
                        </a>
                    </li>
                    <li class="nav-item ${activePath == '/manager/appointments' ? 'active' : ''}">
                        <a class="nav-link" href="${ctx}/manager/appointments">
                            <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-calendar-check"></i></span>
                            <span class="nav-link-title">Lịch hẹn</span>
                        </a>
                    </li>
                    <li class="nav-item ${activePath == '/manager/students' ? 'active' : ''}">
                        <a class="nav-link" href="${ctx}/manager/students">
                            <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-people"></i></span>
                            <span class="nav-link-title">Sinh viên</span>
                        </a>
                    </li>
                    <li class="qa-workspace-nav-label">TRI THỨC & CÁ NHÂN</li>
                    <li class="nav-item ${activePath == '/manager/canned-responses' ? 'active' : ''}">
                        <a class="nav-link" href="${ctx}/manager/canned-responses">
                            <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-chat-left-text"></i></span>
                            <span class="nav-link-title">Mẫu trả lời</span>
                        </a>
                    </li>
                    <li class="nav-item ${activePath == '/manager/faq' ? 'active' : ''}">
                        <a class="nav-link" href="${ctx}/manager/faq">
                            <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-lightbulb"></i></span>
                            <span class="nav-link-title">FAQ đề xuất</span>
                        </a>
                    </li>
                    <li class="nav-item ${activePath == '/manager/reports' ? 'active' : ''}">
                        <a class="nav-link" href="${ctx}/manager/reports">
                            <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-bar-chart-line"></i></span>
                            <span class="nav-link-title">Báo cáo</span>
                        </a>
                    </li>
                    <li class="nav-item ${activePath == '/manager/profile' ? 'active' : ''}">
                        <a class="nav-link" href="${ctx}/manager/profile">
                            <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-person-circle"></i></span>
                            <span class="nav-link-title">Hồ sơ</span>
                        </a>
                    </li>
                </ul>
            </div>
            <div class="qa-workspace-sidebar-footer"><span class="qa-workspace-role-dot"></span>Không gian tư vấn viên<a href="${ctx}/guide" aria-label="Hướng dẫn sử dụng"><i class="bi bi-question-circle"></i></a></div>
        </div>
    </aside>

    <header class="navbar navbar-expand-md d-print-none qa-workspace-topbar">
        <div class="container-xl">
            <button class="btn qa-sidebar-toggle" type="button" data-workspace-toggle
                    aria-controls="workspace-sidebar" aria-expanded="true" aria-label="Thu gọn hoặc mở menu">
                <i class="bi bi-list"></i>
            </button>
            <a href="${ctx}/manager/dashboard" class="qa-topbar-brand d-lg-none">
                <img src="${ctx}/assets/images/hcmute-logo.png" alt="Logo HCMUTE">
                <strong>QA<span>UTE</span></strong>
            </a>
            <div class="qa-workspace-location"><small>KHÔNG GIAN TƯ VẤN</small><strong><c:out value="${workspacePageTitle}"/></strong></div>
            <div class="navbar-nav flex-row ms-auto align-items-center gap-3">

                <c:if test="${not empty mgrStatus}">
                    <div class="dropdown qa-workspace-presence">
                        <button class="btn qa-presence-toggle dropdown-toggle" data-bs-toggle="dropdown" type="button" aria-expanded="false"><span class="qa-presence-dot qa-presence-dot--${mgrStatus}"></span><c:choose><c:when test="${mgrStatus == 'ONLINE'}">Trực tuyến</c:when><c:when test="${mgrStatus == 'BUSY'}">Đang bận</c:when><c:otherwise>Ngoại tuyến</c:otherwise></c:choose></button>
                        <form method="post" action="${ctx}/manager/profile/status" class="dropdown-menu dropdown-menu-end p-2">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="hidden" name="redirect" value="<c:out value='${currentPath}'/>"/>
                            <div class="d-grid gap-1" role="group" aria-label="Trạng thái trực">
                                <button type="submit" name="status" value="ONLINE"
                                        class="btn qa-status-online ${mgrStatus == 'ONLINE' ? 'active' : ''}"
                                        aria-pressed="${mgrStatus == 'ONLINE'}" title="Sẵn sàng nhận yêu cầu và chat">Trực tuyến</button>
                                <button type="submit" name="status" value="BUSY"
                                        class="btn qa-status-busy ${mgrStatus == 'BUSY' ? 'active' : ''}"
                                        aria-pressed="${mgrStatus == 'BUSY'}" title="Tạm bận, không nhận chat mới">Bận</button>
                                <button type="submit" name="status" value="OFFLINE"
                                        class="btn qa-status-offline ${mgrStatus == 'OFFLINE' ? 'active' : ''}"
                                        aria-pressed="${mgrStatus == 'OFFLINE'}" title="Không trực">Ngoại tuyến</button>
                            </div>
                        </form>
                    </div>
                </c:if>

                <div class="nav-item dropdown">
                    <a href="#" class="nav-link d-flex lh-1 text-reset p-0" data-bs-toggle="dropdown" aria-label="Mở menu người dùng">
                        <span class="qa-workspace-avatar qa-workspace-avatar--small"><c:out value="${workspaceInitial}"/></span>
                        <div class="d-none d-xl-block ps-2">
                            <div><c:out value="${workspaceName}"/></div>
                            <div class="mt-1 small text-secondary">Tư vấn viên</div>
                        </div>
                    </a>
                    <div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
                        <a href="${ctx}/manager/profile" class="dropdown-item"><i class="bi bi-person me-2"></i>Hồ sơ</a>
                        <a href="${ctx}/manager/profile/security" class="dropdown-item"><i class="bi bi-shield-lock me-2"></i>Bảo mật</a>
                        <div class="dropdown-divider"></div>
                        <form method="post" action="${ctx}/auth/logout" class="m-0">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="dropdown-item"><i class="bi bi-box-arrow-right me-2"></i>Đăng xuất</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <main class="page-wrapper" id="workspace-main" tabindex="-1">
        <div class="page-body">
            <div class="container-xl qa-workspace-container">
                <qa:alert/>
                <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
            </div>
        </div>
        <footer class="footer footer-transparent d-print-none">
            <div class="container-xl qa-workspace-footer"><span>&copy; QAUTE · Cổng hỗ trợ sinh viên</span><a href="${ctx}/guide">Hướng dẫn sử dụng <i class="bi bi-arrow-up-right"></i></a></div>
        </footer>
    </main>
</div>

<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/assets/js/tv2-layout.js" defer></script>
</body>
</html>