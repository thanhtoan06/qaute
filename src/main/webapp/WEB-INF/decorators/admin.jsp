<%@ page pageEncoding="UTF-8" %>
    <%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
        <%@ page import="org.sitemesh.content.Content" %>
            <% Content pageContent=(Content) request.getAttribute("org.sitemesh.content.Content"); %>
                <c:set var="ctx" value="${pageContext.request.contextPath}" />
                <%@ include file="/WEB-INF/views/common/tv2-workspace-user.jspf" %>

                    <c:set var="rawUri"
                        value="${not empty requestScope['jakarta.servlet.forward.request_uri'] ? requestScope['jakarta.servlet.forward.request_uri'] : pageContext.request.requestURI}" />
                    <c:set var="currentPath"
                        value="${fn:startsWith(rawUri, ctx) ? fn:substring(rawUri, fn:length(ctx), fn:length(rawUri)) : rawUri}" />

                    <c:set var="menuPaths"
                        value="/admin/dashboard,/admin/users,/admin/users/import,/admin/departments,/admin/categories,/admin/managers,/admin/faq,/admin/faq/tags,/admin/faq/synonyms,/admin/tickets,/admin/sla,/admin/settings,/admin/announcements,/admin/audit-logs,/admin/reports,/admin/health,/admin/profile" />
                    <c:set var="activePath" value="" />
                    <c:forTokens items="${menuPaths}" delims="," var="menuPath">
                        <c:if test="${currentPath == menuPath or fn:startsWith(currentPath, menuPath.concat('/'))}">
                            <c:if test="${fn:length(menuPath) gt fn:length(activePath)}">
                                <c:set var="activePath" value="${menuPath}" />
                            </c:if>
                        </c:if>
                    </c:forTokens>

                    <c:set var="grpUsers"
                        value="${activePath == '/admin/users' or activePath == '/admin/users/import'}" />
                    <c:set var="grpOrg"
                        value="${activePath == '/admin/departments' or activePath == '/admin/categories' or activePath == '/admin/managers'}" />
                    <c:set var="grpKnowledge"
                        value="${activePath == '/admin/faq' or activePath == '/admin/faq/tags' or activePath == '/admin/faq/synonyms'}" />
                    <c:set var="grpPolicy" value="${activePath == '/admin/sla' or activePath == '/admin/settings'}" />
                    <c:set var="grpMonitor"
                        value="${activePath == '/admin/audit-logs' or activePath == '/admin/reports' or activePath == '/admin/health'}" />

                    <!DOCTYPE html>
                    <html lang="vi">

                    <head>
                        <meta charset="UTF-8">
                        <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
                            <link rel="stylesheet" href="${ctx}/assets/css/tv2-workspace.css">
<style>
:root { --adm-accent: #3478e5; --adm-accent-dark: #205bc0; --adm-accent-deep: #17458f; --adm-accent-soft: #edf4ff; --adm-ink: #1a3454; --adm-text: #405772; --adm-muted: #8192a9; --adm-border: #e2eaf4; --adm-radius: 16px; --adm-shadow-sm: 0 4px 14px rgba(19, 46, 81, .045); --adm-shadow: 0 12px 30px rgba(19, 46, 81, .075); --adm-shadow-lg: 0 22px 54px rgba(10, 30, 59, .15); --adm-ease: cubic-bezier(.2, .75, .3, 1); }
html { scroll-behavior: smooth; scroll-padding-top: 88px; }
body.qa-workspace--admin { position: relative; isolation: isolate; min-height: 100vh; min-height: 100dvh; overflow-x: hidden; color: var(--adm-text); background-color: #f4f7fc; background-image: radial-gradient(circle at 1px 1px, rgba(52, 120, 229, .065) 1px, transparent 1.5px), radial-gradient(ellipse at 82% 0%, rgba(60, 131, 237, .12), transparent 36%), radial-gradient(ellipse at 0% 80%, rgba(113, 106, 218, .045), transparent 34%), linear-gradient(145deg, #f5f8fd 0%, #fcfdff 52%, #f0f5fc 100%); background-size: 28px 28px, auto, auto, auto; background-attachment: fixed; -webkit-font-smoothing: antialiased; text-rendering: optimizeLegibility; }
body.qa-workspace--admin::before { content: ""; position: fixed; z-index: -1; top: -335px; right: -225px; width: clamp(360px, 44vw, 660px); aspect-ratio: 1; border: 1px solid rgba(52, 120, 229, .09); border-radius: 50%; box-shadow: 0 0 0 34px rgba(52, 120, 229, .018), 0 0 0 78px rgba(52, 120, 229, .012), 0 0 0 132px rgba(52, 120, 229, .008); pointer-events: none; animation: qa-workspace-ambient-drift 28s ease-in-out infinite alternate; }
body.qa-workspace--admin::after { content: ""; position: fixed; z-index: -1; left: -210px; bottom: -255px; width: 360px; aspect-ratio: 1; border: 1px solid rgba(52, 120, 229, .065); border-radius: 50%; box-shadow: 0 0 0 32px rgba(52, 120, 229, .012), 0 0 0 72px rgba(52, 120, 229, .009); pointer-events: none; }
@keyframes adm-online-pulse {
  0%, 100% { box-shadow: 0 0 0 3px rgba(59, 201, 151, .12); }
  50% { box-shadow: 0 0 0 6px rgba(59, 201, 151, .035); }
}
body.qa-workspace--admin ::selection { color: #ffffff; background: #285fbd; }
body.qa-workspace--admin :focus-visible { outline: 3px solid rgba(52, 120, 229, .35); outline-offset: 3px; }
body.qa-workspace--admin .qa-workspace-skip { border-radius: 10px; box-shadow: var(--adm-shadow); }
body.qa-workspace--admin .page { min-height: 100vh; min-height: 100dvh; }
body.qa-workspace--admin .qa-workspace-sidebar { position: fixed; isolation: isolate; background: radial-gradient(ellipse at 90% 0%, rgba(49, 117, 199, .23), transparent 36%), radial-gradient(ellipse at 0% 76%, rgba(37, 87, 147, .13), transparent 40%), linear-gradient(170deg, #102c50 0%, #0a1b34 49%, #07172c 100%); border-right: 1px solid rgba(255, 255, 255, .07); box-shadow: 8px 0 32px rgba(7, 23, 44, .105); scrollbar-width: thin; scrollbar-color: #345172 transparent; }
body.qa-workspace--admin .qa-workspace-sidebar::before { content: ""; position: absolute; z-index: 0; top: 115px; right: -185px; width: 320px; height: 320px; border: 1px solid rgba(255, 255, 255, .065); border-radius: 50%; box-shadow: 0 0 0 29px rgba(255, 255, 255, .017), 0 0 0 66px rgba(255, 255, 255, .012); pointer-events: none; }
body.qa-workspace--admin .qa-workspace-sidebar::after { content: ""; position: absolute; z-index: 0; left: -145px; bottom: 100px; width: 230px; height: 230px; border-radius: 50%; background: radial-gradient(circle, rgba(48, 128, 225, .12), transparent 70%); pointer-events: none; }
body.qa-workspace--admin .qa-workspace-sidebar>.container-fluid { position: relative; z-index: 1; }
body.qa-workspace--admin .qa-workspace-sidebar::-webkit-scrollbar { width: 4px; }
body.qa-workspace--admin .qa-workspace-sidebar::-webkit-scrollbar-thumb { background: rgba(255, 255, 255, .18); border-radius: 5px; }
body.qa-workspace--admin .qa-workspace-sidebar::-webkit-scrollbar-thumb:hover { background: rgba(255, 255, 255, .3); }
body.qa-workspace--admin .navbar-brand { padding-top: 1.15rem; padding-bottom: 1.1rem; }
body.qa-workspace--admin .navbar-brand>a { display: flex; align-items: center; gap: .75rem; width: fit-content; max-width: 100%; text-decoration: none; }
body.qa-workspace--admin .qa-role-logo { width: 40px; height: 40px; padding: 3px; object-fit: contain; background: #ffffff; border: 1px solid rgba(255, 255, 255, .2); border-radius: 12px; box-shadow: 0 6px 18px rgba(0, 0, 0, .15); transition: transform .25s var(--adm-ease), box-shadow .25s ease; }
body.qa-workspace--admin .navbar-brand>a:hover .qa-role-logo { transform: translateY(-2px) rotate(-3deg); box-shadow: 0 10px 23px rgba(0, 0, 0, .21); }
body.qa-workspace--admin .qa-workspace-brand-copy { color: #ffffff; font-size: 1.08rem; font-weight: 850; line-height: 1.1; letter-spacing: -.045em; }
body.qa-workspace--admin .qa-workspace-brand-copy>span { color: #ffffff; }
body.qa-workspace--admin .qa-workspace-brand-copy small { display: block; margin-top: 4px; color: #9eb2cc; font-size: .62rem; line-height: 1.5; letter-spacing: .03em; font-weight: 650; }
body.qa-workspace--admin .qa-workspace-user-card { display: flex; align-items: center; gap: .75rem; margin: .9rem .75rem .3rem; padding: .85rem .9rem; border: 1px solid rgba(255, 255, 255, .07); border-radius: 15px; background: linear-gradient(135deg, rgba(255, 255, 255, .085), rgba(255, 255, 255, .035)); text-decoration: none; transition: background .22s ease, border-color .22s ease, transform .22s ease, box-shadow .22s ease; }
body.qa-workspace--admin .qa-workspace-user-card:hover { background: linear-gradient(135deg, rgba(255, 255, 255, .14), rgba(255, 255, 255, .055)); border-color: rgba(137, 196, 255, .22); transform: translateY(-1px); box-shadow: 0 8px 20px rgba(0, 0, 0, .1); }
body.qa-workspace--admin .qa-workspace-avatar { display: inline-flex; align-items: center; justify-content: center; flex: 0 0 auto; width: 40px; height: 40px; border: 2px solid rgba(255, 255, 255, .19); border-radius: 13px; background: linear-gradient(135deg, #4b96ef, #245fb8); color: #ffffff; font-size: .88rem; font-weight: 850; letter-spacing: -.02em; box-shadow: 0 5px 15px rgba(29, 89, 166, .25); transition: transform .25s ease, box-shadow .25s ease; }
body.qa-workspace--admin .qa-workspace-user-card:hover .qa-workspace-avatar { transform: translateY(-1px) rotate(-3deg); box-shadow: 0 8px 20px rgba(29, 89, 166, .34); }
body.qa-workspace--admin .qa-workspace-user-copy { display: flex; flex: 1 1 auto; flex-direction: column; gap: 4px; min-width: 0; }
body.qa-workspace--admin .qa-workspace-user-copy strong { overflow: hidden; color: #ffffff; font-size: .82rem; font-weight: 700; line-height: 1.45; text-overflow: ellipsis; white-space: nowrap; }
body.qa-workspace--admin .qa-workspace-user-copy small { color: #a2b7d2; font-size: .68rem; }
body.qa-workspace--admin .qa-workspace-user-card>.bi-shield-check { flex-shrink: 0; color: #8fc4ff; font-size: 1rem; transition: transform .2s ease, color .2s ease; }
body.qa-workspace--admin .qa-workspace-user-card:hover>.bi-shield-check { color: #ffffff; transform: scale(1.1); }
body.qa-workspace--admin .qa-workspace-nav-label { padding: 1.3rem 1.25rem .5rem; color: #8099b8; font-size: .63rem; font-weight: 800; letter-spacing: .12em; }
body.qa-workspace--admin .qa-workspace-sidebar .navbar-nav { gap: 4px; }
body.qa-workspace--admin .qa-workspace-sidebar .nav-item { position: relative; margin: 0; border-radius: 11px; }
body.qa-workspace--admin .qa-workspace-sidebar .nav-link { position: relative; display: flex; align-items: center; gap: .75rem; min-height: 43px; margin: 0; padding: .65rem .85rem; border: 1px solid transparent; border-radius: 11px; color: rgba(225, 236, 250, .72); font-size: .81rem; font-weight: 550; text-decoration: none; transition: color .2s ease, background .2s ease, border-color .2s ease, transform .2s ease, box-shadow .2s ease; }
body.qa-workspace--admin .qa-workspace-sidebar .nav-link-icon { display: inline-flex; align-items: center; justify-content: center; width: 21px; min-width: 21px; color: #9cb7d7; font-size: 1.02rem; text-align: center; transition: color .2s ease, transform .2s ease; }
body.qa-workspace--admin .qa-workspace-sidebar .nav-link-title { min-width: 0; line-height: 1.45; }
body.qa-workspace--admin .qa-workspace-sidebar .nav-link:hover { color: #ffffff; background: rgba(255, 255, 255, .075); border-color: rgba(255, 255, 255, .035); transform: translateX(2px); }
body.qa-workspace--admin .qa-workspace-sidebar .nav-link:hover .nav-link-icon { color: #bedcff; transform: scale(1.08); }
body.qa-workspace--admin .qa-workspace-sidebar .nav-item.active>.nav-link, body.qa-workspace--admin .qa-workspace-sidebar .nav-item.dropdown.active>.nav-link { color: #ffffff; font-weight: 700; background: linear-gradient(110deg, #206bc4, #3286db); border-color: rgba(255, 255, 255, .1); box-shadow: 0 6px 16px rgba(9, 61, 121, .28), inset 0 1px 0 rgba(255, 255, 255, .1); }
body.qa-workspace--admin .qa-workspace-sidebar .nav-item.active>.nav-link::after, body.qa-workspace--admin .qa-workspace-sidebar .nav-item.dropdown.active>.nav-link::after { content: ""; position: absolute; top: 9px; right: 0; bottom: 9px; width: 3px; border-radius: 4px 0 0 4px; background: #c1e2ff; box-shadow: 0 0 12px rgba(193, 226, 255, .46); }
body.qa-workspace--admin .qa-workspace-sidebar .nav-item.active>.nav-link .nav-link-icon { color: #ffffff; }
body.qa-workspace--admin .qa-workspace-sidebar .nav-item.dropdown>.nav-link.dropdown-toggle::after { margin-left: auto; color: #8ba8ca; opacity: .75; transition: transform .2s ease, color .2s ease; }
body.qa-workspace--admin .qa-workspace-sidebar .nav-item.dropdown>.nav-link[aria-expanded="true"]::after { color: #d1e5ff; transform: rotate(180deg); }
body.qa-workspace--admin .qa-workspace-sidebar .nav-item.dropdown>.dropdown-menu { position: static !important; inset: auto !important; float: none; transform: none !important; width: auto; min-width: 0; margin: .25rem 0 .55rem 2.15rem; padding: .3rem; border: 1px solid rgba(255, 255, 255, .075); border-radius: 11px; background: rgba(3, 15, 30, .32); box-shadow: inset 0 1px 2px rgba(0, 0, 0, .08); }
body.qa-workspace--admin .qa-workspace-sidebar .nav-item.dropdown>.dropdown-menu:not(.show) { display: none; }
body.qa-workspace--admin .qa-workspace-sidebar .nav-item.dropdown>.dropdown-menu.show { display: block; animation: qa-workspace-page-enter .2s ease both; }
body.qa-workspace--admin .qa-workspace-sidebar .dropdown-menu .dropdown-item { display: flex; align-items: center; min-height: 36px; padding: .52rem .65rem; border: 1px solid transparent; border-radius: 8px; color: rgba(220, 234, 250, .72); background: transparent; font-size: .77rem; line-height: 1.4; white-space: normal; transition: color .18s ease, background .18s ease, border-color .18s ease, transform .18s ease; }
body.qa-workspace--admin .qa-workspace-sidebar .dropdown-menu .dropdown-item:hover, body.qa-workspace--admin .qa-workspace-sidebar .dropdown-menu .dropdown-item:focus { color: #ffffff; background: rgba(255, 255, 255, .085); border-color: rgba(255, 255, 255, .04); transform: translateX(2px); }
body.qa-workspace--admin .qa-workspace-sidebar .dropdown-menu .dropdown-item.active, body.qa-workspace--admin .qa-workspace-sidebar .dropdown-menu .dropdown-item:active { color: #ffffff; background: linear-gradient(110deg, rgba(52, 120, 229, .84), rgba(43, 101, 190, .84)); border-color: rgba(255, 255, 255, .08); box-shadow: 0 3px 9px rgba(0, 0, 0, .1); }
body.qa-workspace--admin .qa-workspace-sidebar .navbar-toggler { width: 39px; height: 39px; padding: .4rem; border: 1px solid rgba(255, 255, 255, .15); border-radius: 11px; background: rgba(255, 255, 255, .075); box-shadow: none; transition: background .2s ease, transform .2s ease, border-color .2s ease; }
body.qa-workspace--admin .qa-workspace-sidebar .navbar-toggler:hover { background: rgba(255, 255, 255, .13); border-color: rgba(255, 255, 255, .23); transform: translateY(-1px); }
body.qa-workspace--admin .qa-workspace-sidebar-footer { display: flex; align-items: center; gap: .55rem; margin-top: auto; padding: .85rem .75rem; border-top: 1px solid rgba(255, 255, 255, .085); color: #9db2cc; font-size: .69rem; }
body.qa-workspace--admin .qa-workspace-role-dot { width: 7px; height: 7px; flex-shrink: 0; border-radius: 50%; background: #43c99b; box-shadow: 0 0 0 3px rgba(67, 201, 155, .12); animation: adm-online-pulse 3s ease-in-out infinite; }
body.qa-workspace--admin .qa-workspace-sidebar-footer a { display: inline-flex; align-items: center; justify-content: center; width: 29px; height: 29px; margin-left: auto; border: 1px solid rgba(255, 255, 255, .1); border-radius: 9px; background: rgba(255, 255, 255, .045); color: #b5c8df; text-decoration: none; transition: background .2s ease, color .2s ease, transform .2s ease; }
body.qa-workspace--admin .qa-workspace-sidebar-footer a:hover { color: #ffffff; background: rgba(255, 255, 255, .12); transform: translateY(-1px); }
body.qa-workspace--admin .qa-workspace-topbar { position: fixed; top: 0; right: 0; left: var(--qa-workspace-sidebar-width); z-index: 1030; height: var(--qa-workspace-topbar-height); min-height: var(--qa-workspace-topbar-height); background: rgba(255, 255, 255, .92); -webkit-backdrop-filter: blur(18px); backdrop-filter: blur(18px); border-bottom: 1px solid rgba(224, 233, 245, .9); box-shadow: 0 5px 23px rgba(16, 43, 77, .045); transition: left .25s ease, background .25s ease, box-shadow .25s ease; }
body.qa-workspace--admin .qa-workspace-topbar::after { content: ""; position: absolute; right: 0; bottom: -1px; left: 0; height: 1px; background: linear-gradient(90deg, transparent, rgba(52, 120, 229, .19), transparent); pointer-events: none; }
body.qa-workspace--admin .qa-workspace-topbar>.container-xl { display: flex; align-items: center; gap: 1rem; min-height: var(--qa-workspace-topbar-height); padding: 0 1.5rem; }
body.qa-workspace--admin .qa-sidebar-toggle { display: inline-flex; align-items: center; justify-content: center; flex: 0 0 39px; width: 39px; height: 39px; padding: 0; border: 1px solid #206bc4; border-radius: 11px; background: linear-gradient(135deg, #287be0, #185caf); color: #ffffff; box-shadow: 0 4px 12px rgba(32, 107, 196, .2); font-size: 1.2rem; transition: color .2s ease, border-color .2s ease, background .2s ease, transform .2s ease, box-shadow .2s ease; }
body.qa-workspace--admin .qa-sidebar-toggle:hover { color: #ffffff; border-color: #154c91; background: linear-gradient(135deg, #206bc4, #154c91); box-shadow: 0 7px 17px rgba(32, 107, 196, .27); transform: translateY(-1px); }
body.qa-workspace--admin .qa-sidebar-toggle:active { transform: scale(.97); }
body.qa-workspace--admin .qa-topbar-brand { display: inline-flex; align-items: center; gap: .45rem; color: var(--adm-ink); text-decoration: none; }
body.qa-workspace--admin .qa-topbar-brand img { width: 31px; height: 31px; padding: 2px; object-fit: contain; border: 1px solid #e1eaf5; border-radius: 9px; background: #ffffff; }
body.qa-workspace--admin .qa-topbar-brand strong { color: #183756; font-size: 1rem; font-weight: 850; letter-spacing: -.04em; }
body.qa-workspace--admin .qa-topbar-brand strong span { color: var(--adm-accent); }
body.qa-workspace--admin .qa-workspace-location { display: flex; flex-direction: column; justify-content: center; gap: 4px; min-width: 0; padding-left: .15rem; }
body.qa-workspace--admin .qa-workspace-location small { color: #8295ae; font-size: .63rem; line-height: 1.3; font-weight: 800; letter-spacing: .1em; }
body.qa-workspace--admin .qa-workspace-location strong { overflow: hidden; color: #1a3555; font-size: 1rem; font-weight: 800; letter-spacing: -.02em; line-height: 1.35; text-overflow: ellipsis; white-space: nowrap; }
body.qa-workspace--admin .qa-workspace-topbar .navbar-nav { gap: .55rem !important; }
body.qa-workspace--admin .qa-workspace-topbar .nav-link { border-radius: 11px; }
body.qa-workspace--admin .qa-workspace-topbar .nav-item.dropdown>.nav-link { align-items: center; gap: .7rem; min-height: 43px; padding: .3rem .5rem; border: 1px solid transparent; transition: background .2s ease, border-color .2s ease; }
body.qa-workspace--admin .qa-workspace-topbar .nav-item.dropdown>.nav-link:hover, body.qa-workspace--admin .qa-workspace-topbar .nav-item.dropdown>.nav-link[aria-expanded="true"] { border-color: #e4ecf6; background: #f5f8fd; }
body.qa-workspace--admin .qa-workspace-avatar--small { width: 36px; height: 36px; border-radius: 12px; font-size: .77rem; }
body.qa-workspace--admin .qa-workspace-topbar .nav-item.dropdown>.nav-link div { color: #2c425c; font-size: .8rem; font-weight: 750; }
body.qa-workspace--admin .qa-workspace-topbar .nav-item.dropdown>.nav-link .small { color: #8a9bb0 !important; font-size: .69rem; font-weight: 500; }
body.qa-workspace--admin .qa-workspace-topbar .dropdown-menu { padding: .5rem; border: 1px solid #e1e9f4; border-radius: 13px; box-shadow: var(--adm-shadow-lg); animation: qa-workspace-page-enter .18s ease both; }
body.qa-workspace--admin .qa-workspace-topbar .dropdown-item { display: flex; align-items: center; min-height: 38px; padding: .6rem .75rem; border-radius: 8px; color: #465c76; font-size: .8rem; transition: background .18s ease, color .18s ease; }
body.qa-workspace--admin .qa-workspace-topbar .dropdown-item:hover, body.qa-workspace--admin .qa-workspace-topbar .dropdown-item:focus { color: #1f63b3; background: #eef5ff; }
body.qa-workspace--admin .qa-workspace-topbar .dropdown-divider { border-color: #eaf0f7; }
body.qa-workspace--admin .page-wrapper { min-width: 0; background: transparent; }
body.qa-workspace--admin .page-header { margin-bottom: 0; padding-top: .9rem; padding-bottom: .5rem; border: 0; background: transparent; }
body.qa-workspace--admin .page-header .container-xl { max-width: 1720px; }
body.qa-workspace--admin .breadcrumb { gap: .15rem; margin-bottom: 0; font-size: .78rem; }
body.qa-workspace--admin .breadcrumb a { color: #6f86a2; text-decoration: none; transition: color .2s ease; }
body.qa-workspace--admin .breadcrumb a:hover { color: var(--adm-accent-dark); }
body.qa-workspace--admin .breadcrumb-item.active { color: #344e6b; font-weight: 650; }
body.qa-workspace--admin .page-body { padding-top: 1.25rem; padding-bottom: 1.7rem; animation: qa-workspace-page-enter .4s var(--adm-ease) both; }
body.qa-workspace--admin .qa-workspace-container { width: 100%; max-width: 1720px; }
body.qa-workspace--admin .qa-workspace-container>.alert { border-radius: 13px; }
body.qa-workspace--admin .page-header .page-pretitle { color: #8095af; font-size: .68rem; font-weight: 800; letter-spacing: .1em; }
body.qa-workspace--admin .page-header .page-title, body.qa-workspace--admin .page-header h1, body.qa-workspace--admin .page-header h2 { color: #1b385a; font-weight: 850; letter-spacing: -.04em; }
body.qa-workspace--admin .page-header .page-title { line-height: 1.3; }
body.qa-workspace--admin .page-header .text-secondary, body.qa-workspace--admin .page-header .text-muted { color: #7e91a9 !important; }
body.qa-workspace--admin .card { overflow: visible; border: 1px solid rgba(224, 233, 244, .96); border-radius: var(--adm-radius); background: rgba(255, 255, 255, .97); box-shadow: var(--adm-shadow-sm); transition: border-color .23s ease, box-shadow .23s ease, transform .23s ease; }
body.qa-workspace--admin .card-header { padding: 1rem 1.2rem; border-bottom: 1px solid #edf1f7; border-radius: var(--adm-radius) var(--adm-radius) 0 0; background: linear-gradient(125deg, #ffffff, #f7faff); }
body.qa-workspace--admin .card-header .card-title, body.qa-workspace--admin .card-title { margin-bottom: 0; color: #2b4462; font-size: .94rem; font-weight: 800; }
body.qa-workspace--admin .card-body { padding: 1.2rem; }
body.qa-workspace--admin .card-footer { padding: .9rem 1.2rem; border-top: 1px solid #edf1f7; border-radius: 0 0 var(--adm-radius) var(--adm-radius); background: #fbfcff; }
body.qa-workspace--admin .card .card { box-shadow: none; }
body.qa-workspace--admin .btn { border-radius: 10px; font-weight: 650; transition: color .2s ease, background-color .2s ease, border-color .2s ease, box-shadow .2s ease, transform .2s ease; }
body.qa-workspace--admin .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
body.qa-workspace--admin .btn:not(:disabled):not(.btn-close):active { transform: translateY(0) scale(.985); }
body.qa-workspace--admin .btn-primary { color: #ffffff; background: linear-gradient(120deg, #3985e8, #2165bd); border-color: #2872ca; box-shadow: 0 4px 12px rgba(33, 101, 189, .14); }
body.qa-workspace--admin .btn-primary:hover, body.qa-workspace--admin .btn-primary:focus { color: #ffffff; background: linear-gradient(120deg, #2c75d3, #174f99); border-color: #1e5ba6; box-shadow: 0 7px 18px rgba(33, 101, 189, .23); }
body.qa-workspace--admin .btn-outline-primary { color: #2467b8; border-color: #c8d9ef; background: rgba(255, 255, 255, .92); }
body.qa-workspace--admin .btn-outline-primary:hover, body.qa-workspace--admin .btn-outline-primary:focus { color: #ffffff; background: linear-gradient(120deg, #3985e8, #2165bd); border-color: #2872ca; }
body.qa-workspace--admin .btn-outline-secondary { color: #5b7089; border-color: #dbe5f0; background: #ffffff; }
body.qa-workspace--admin .btn-outline-secondary:hover { color: #294e7b; border-color: #bdcee3; background: #f3f7fc; }
body.qa-workspace--admin .btn-light { border-color: #e5edf6; background: #ffffff; color: #445b76; }
body.qa-workspace--admin .btn-light:hover { background: #f3f7fd; border-color: #d1deed; color: #244f84; }
body.qa-workspace--admin .btn-danger { box-shadow: 0 4px 11px rgba(210, 57, 78, .1); }
body.qa-workspace--admin .btn-success { box-shadow: 0 4px 11px rgba(24, 150, 103, .1); }
body.qa-workspace--admin .btn .bi, body.qa-workspace--admin .dropdown-item .bi { vertical-align: -.08em; }
body.qa-workspace--admin a.text-primary, body.qa-workspace--admin .text-primary { color: #246dc2 !important; }
body.qa-workspace--admin .bg-primary { background: linear-gradient(120deg, #3985e8, #2165bd) !important; }
body.qa-workspace--admin .border-primary { border-color: #3985e8 !important; }
body.qa-workspace--admin .badge { padding: .38em .62em; border-radius: 7px; font-size: .72em; font-weight: 700; letter-spacing: .01em; }
body.qa-workspace--admin .badge.bg-primary { background: linear-gradient(120deg, #3985e8, #2165bd) !important; }
body.qa-workspace--admin .form-label { margin-bottom: .45rem; color: #425a76; font-size: .81rem; font-weight: 750; }
body.qa-workspace--admin .form-control, body.qa-workspace--admin .form-select { min-height: 41px; color: #2c425d; border: 1px solid #dce6f2; border-radius: 10px; background-color: rgba(255, 255, 255, .97); transition: border-color .2s ease, box-shadow .2s ease, background-color .2s ease; }
body.qa-workspace--admin textarea.form-control { min-height: 110px; }
body.qa-workspace--admin .form-control::placeholder { color: #9baabd; }
body.qa-workspace--admin .form-control:hover, body.qa-workspace--admin .form-select:hover { border-color: #bdd1e9; }
body.qa-workspace--admin .form-control:focus, body.qa-workspace--admin .form-select:focus { border-color: #66a0e5; background-color: #ffffff; box-shadow: 0 0 0 4px rgba(52, 120, 229, .1); }
body.qa-workspace--admin .form-check-input { border-color: #b9cadd; cursor: pointer; }
body.qa-workspace--admin .form-check-input:checked { border-color: #2872ca; background-color: #2872ca; }
body.qa-workspace--admin .input-group-text { color: #7187a1; border-color: #dce6f2; border-radius: 10px; background: #f5f8fc; }
body.qa-workspace--admin .table-responsive { border-radius: 12px; }
body.qa-workspace--admin .table { margin-bottom: 0; color: #435873; vertical-align: middle; }
body.qa-workspace--admin .table> :not(caption)>*>* { padding: .85rem .9rem; border-bottom-color: #edf1f7; }
body.qa-workspace--admin .table thead th { color: #70839c; background: linear-gradient(180deg, #f9fbfe, #f2f6fb); border-bottom: 1px solid #e3eaf4; font-size: .71rem; font-weight: 800; letter-spacing: .055em; text-transform: uppercase; white-space: nowrap; }
body.qa-workspace--admin .table tbody tr { transition: background-color .18s ease; }
body.qa-workspace--admin .table tbody tr:hover>* { background-color: #f5f9ff; color: inherit; }
body.qa-workspace--admin .table tbody tr:last-child>* { border-bottom: 0; }
body.qa-workspace--admin .table .btn { white-space: nowrap; }
body.qa-workspace--admin .nav-tabs { gap: 4px; border-bottom-color: #e4ebf4; }
body.qa-workspace--admin .nav-tabs .nav-link { padding: .7rem .9rem; color: #7387a0; border-radius: 10px 10px 0 0; font-size: .82rem; font-weight: 700; transition: color .2s ease, background .2s ease, border-color .2s ease; }
body.qa-workspace--admin .nav-tabs .nav-link:hover { color: #2165bd; background: #f6f9fe; }
body.qa-workspace--admin .nav-tabs .nav-link.active { color: #2165bd; border-color: #e4ebf4 #e4ebf4 #ffffff; background: #ffffff; }
body.qa-workspace--admin .list-group { overflow: hidden; border-radius: 12px; }
body.qa-workspace--admin .list-group-item { border-color: #eaf0f7; color: #455c77; transition: background-color .18s ease, color .18s ease; }
body.qa-workspace--admin .list-group-item-action:hover { color: #245d9f; background: #f4f8ff; }
body.qa-workspace--admin .list-group-item.active { color: #ffffff; background: linear-gradient(110deg, #2165bd, #3985e8); border-color: #2872ca; }
body.qa-workspace--admin .pagination { gap: 4px; }
body.qa-workspace--admin .pagination .page-link { border-color: #e0e8f2; border-radius: 9px; background: #ffffff; color: #546b87; box-shadow: 0 2px 5px rgba(17, 43, 78, .025); transition: color .18s ease, background .18s ease, border-color .18s ease, transform .18s ease; }
body.qa-workspace--admin .pagination .page-link:hover { color: #2165bd; border-color: #c3d7ef; background: #f3f8ff; transform: translateY(-1px); }
body.qa-workspace--admin .pagination .active .page-link { color: #ffffff; background: linear-gradient(120deg, #3985e8, #2165bd); border-color: #2872ca; }
body.qa-workspace--admin .alert { border-radius: 13px; box-shadow: 0 4px 13px rgba(17, 43, 78, .035); }
body.qa-workspace--admin .progress { overflow: hidden; border-radius: 99px; background: #eaf0f8; }
body.qa-workspace--admin .progress-bar { border-radius: 99px; background-image: linear-gradient(100deg, #2165bd, #4a9bf0, #2165bd); background-size: 200% 100%; animation: qa-progress-shimmer 5s linear infinite; }
body.qa-workspace--admin .page-wrapper .text-secondary, body.qa-workspace--admin .page-wrapper .text-muted { color: #7f91a8 !important; }
body.qa-workspace--admin .page-wrapper .footer { margin-top: .5rem; padding-top: .9rem; padding-bottom: 1.1rem; border-top: 1px solid rgba(222, 232, 244, .8); background: rgba(255, 255, 255, .35); }
body.qa-workspace--admin .qa-workspace-footer { display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: .75rem; color: #8999ad; font-size: .74rem; }
body.qa-workspace--admin .qa-workspace-footer a { display: inline-flex; align-items: center; gap: .35rem; color: #637d9d; font-weight: 700; text-decoration: none; transition: color .2s ease, gap .2s ease; }
body.qa-workspace--admin .qa-workspace-footer a:hover { gap: .5rem; color: #2165bd; }
body.qa-workspace--admin .qa-workspace-overlay[hidden] { display: none !important; }
@media (max-width: 1199.98px) {
  body.qa-workspace--admin .page-body { padding-top: 1.1rem; }
  body.qa-workspace--admin .qa-workspace-topbar>.container-xl { gap: .75rem; }
  body.qa-workspace--admin .qa-workspace-location strong { font-size: .9rem; }
}
@media (max-width: 991.98px) {
  body.qa-workspace--admin { background-attachment: scroll; }
  body.qa-workspace--admin .qa-workspace-sidebar { box-shadow: 16px 0 46px rgba(6, 24, 46, .23); }
  body.qa-workspace--admin .qa-workspace-topbar { left: 0; }
  body.qa-workspace--admin .qa-workspace-topbar>.container-xl { min-height: var(--qa-workspace-topbar-height); padding-right: 1.15rem; padding-left: 1.15rem; }
  body.qa-workspace--admin .qa-workspace-location { flex: 1 1 auto; }
  body.qa-workspace--admin .qa-workspace-container { max-width: 100%; }
}
@media (max-width: 767.98px) {
  body.qa-workspace--admin .qa-workspace-topbar>.container-xl { gap: .55rem; }
  body.qa-workspace--admin .qa-workspace-location small { font-size: .55rem; }
  body.qa-workspace--admin .qa-workspace-location strong { font-size: .85rem; }
  body.qa-workspace--admin .qa-workspace-topbar .navbar-nav { gap: .35rem !important; }
  body.qa-workspace--admin .page-body { padding-top: 1rem; padding-bottom: 1.2rem; }
  body.qa-workspace--admin .card { border-radius: 14px; }
  body.qa-workspace--admin .card-header, body.qa-workspace--admin .card-body, body.qa-workspace--admin .card-footer { padding: 1rem; }
  body.qa-workspace--admin .table> :not(caption)>*>* { padding: .72rem; }
  body.qa-workspace--admin .page-header .page-title { font-size: 1.35rem; }
}
@media (max-width: 575.98px) {
  body.qa-workspace--admin { background-image: radial-gradient(circle at 1px 1px, rgba(52, 120, 229, .05) 1px, transparent 1.5px), radial-gradient(ellipse at 78% 0%, rgba(62, 139, 235, .075), transparent 36%), linear-gradient(145deg, #f5f8fd 0%, #fbfdff 53%, #f0f5fc 100%); background-size: 28px 28px, auto, auto; }
  body.qa-workspace--admin::before { top: -200px; right: -260px; opacity: .65; }
  body.qa-workspace--admin .qa-workspace-topbar>.container-xl { padding-right: .65rem; padding-left: .65rem; gap: .45rem; }
  body.qa-workspace--admin .qa-sidebar-toggle { flex-basis: 36px; width: 36px; height: 36px; border-radius: 10px; }
  body.qa-workspace--admin .qa-workspace-topbar>.container-xl { padding-right: .75rem; padding-left: .75rem; }
  body.qa-workspace--admin .qa-topbar-brand { gap: .3rem; }
  body.qa-workspace--admin .qa-topbar-brand img { width: 27px; height: 27px; }
  body.qa-workspace--admin .qa-topbar-brand strong { font-size: .91rem; }
  body.qa-workspace--admin .qa-workspace-location { gap: 3px; }
  body.qa-workspace--admin .qa-workspace-location small { font-size: .49rem; letter-spacing: .06em; }
  body.qa-workspace--admin .qa-workspace-location strong { max-width: 120px; font-size: .77rem; }
  body.qa-workspace--admin .qa-workspace-topbar .nav-item.dropdown>.nav-link { gap: .25rem; padding: .2rem; }
  body.qa-workspace--admin .qa-workspace-avatar--small { width: 32px; height: 32px; border-radius: 10px; }
  body.qa-workspace--admin .page-header { padding-top: .7rem; }
  body.qa-workspace--admin .page-body { padding: .9rem .7rem 1rem; }
  body.qa-workspace--admin .page-header .page-title { font-size: 1.22rem; }
  body.qa-workspace--admin .card-header, body.qa-workspace--admin .card-body, body.qa-workspace--admin .card-footer { padding: .85rem; }
  body.qa-workspace--admin .table> :not(caption)>*>* { padding: .65rem; }
  body.qa-workspace--admin .qa-workspace-footer { font-size: .69rem; }
}
@media (prefers-reduced-motion: reduce) {
  body.qa-workspace--admin *, body.qa-workspace--admin *::before, body.qa-workspace--admin *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
  body.qa-workspace--admin::before { animation: none !important; }
}
@media print {
  body.qa-workspace--admin { background: #ffffff !important; background-image: none !important; }
  body.qa-workspace--admin::before, body.qa-workspace--admin::after { display: none !important; }
  body.qa-workspace--admin .page-body { animation: none !important; }
  body.qa-workspace--admin .card { box-shadow: none !important; break-inside: avoid; }
}
</style>

                            <title>
                                <%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE
                            </title>
                            <%= pageContent.getExtractedProperties().getChild("head").getValue() %>

                    </head>

                    <body class="qa-workspace qa-workspace--admin">
                        <a class="qa-workspace-skip" href="#workspace-main">Chuyển đến nội dung</a>
                        <button class="qa-workspace-overlay" type="button" data-workspace-close hidden
                            aria-label="Đóng menu điều hướng"></button>
                        <div class="page">

                            <aside class="qa-workspace-sidebar" id="workspace-sidebar" data-bs-theme="dark"
                                aria-label="Điều hướng quản trị viên">
                                <div class="container-fluid">
                                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                                        data-bs-target="#qa-sidebar-menu" aria-controls="qa-sidebar-menu"
                                        aria-expanded="false" aria-label="Mở menu">
                                        <span class="navbar-toggler-icon"></span>
                                    </button>
                                    <h1 class="navbar-brand navbar-brand-autodark">
                                        <a href="${ctx}/admin/dashboard" class="text-decoration-none">
                                            <img src="${ctx}/assets/images/hcmute-logo.png" alt="Logo HCMUTE"
                                                class="qa-role-logo">
                                            <span class="qa-workspace-brand-copy">QA<span>UTE</span><small>CỔNG HỖ TRỢ
                                                    SINH VIÊN</small></span>
                                        </a>
                                    </h1>
                                    <a class="qa-workspace-user-card" href="${ctx}/admin/profile">
                                        <span class="qa-workspace-avatar">
                                            <c:out value="${workspaceInitial}" />
                                        </span>
                                        <span class="qa-workspace-user-copy"><strong>
                                                <c:out value="${workspaceName}" />
                                            </strong><small>Quản trị viên</small></span>
                                        <i class="bi bi-shield-check" aria-hidden="true"></i>
                                    </a>
                                    <div class="collapse navbar-collapse" id="qa-sidebar-menu">
                                        <ul class="navbar-nav pt-lg-3">
                                            <li class="qa-workspace-nav-label">QUẢN TRỊ HỆ THỐNG</li>

                                            <li class="nav-item">
                                                <a class="nav-link" href="${ctx}/" aria-label="Quay về trang chủ Guest">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><i
                                                            class="bi bi-globe2"></i></span>
                                                    <span class="nav-link-title">Trang chủ</span>
                                                </a>
                                            </li>

                                            <li class="nav-item ${activePath == '/admin/dashboard' ? 'active' : ''}">
                                                <a class="nav-link" href="${ctx}/admin/dashboard">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><i
                                                            class="bi bi-speedometer2"></i></span>
                                                    <span class="nav-link-title">Tổng quan</span>
                                                </a>
                                            </li>

                                            <li class="nav-item dropdown ${grpUsers ? 'active' : ''}">
                                                <a class="nav-link dropdown-toggle ${grpUsers ? 'show' : ''}"
                                                    href="#qa-nav-users" data-bs-toggle="dropdown"
                                                    data-bs-auto-close="false" role="button"
                                                    aria-expanded="${grpUsers}">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><i
                                                            class="bi bi-people"></i></span>
                                                    <span class="nav-link-title">Người dùng</span>
                                                </a>
                                                <div class="dropdown-menu ${grpUsers ? 'show' : ''}">
                                                    <a class="dropdown-item ${activePath == '/admin/users' ? 'active' : ''}"
                                                        href="${ctx}/admin/users">Danh sách người dùng</a>
                                                    <a class="dropdown-item ${activePath == '/admin/users/import' ? 'active' : ''}"
                                                        href="${ctx}/admin/users/import">Nhập hàng loạt</a>
                                                </div>
                                            </li>

                                            <li class="nav-item dropdown ${grpOrg ? 'active' : ''}">
                                                <a class="nav-link dropdown-toggle ${grpOrg ? 'show' : ''}"
                                                    href="#qa-nav-org" data-bs-toggle="dropdown"
                                                    data-bs-auto-close="false" role="button" aria-expanded="${grpOrg}">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><i
                                                            class="bi bi-diagram-3"></i></span>
                                                    <span class="nav-link-title">Tổ chức</span>
                                                </a>
                                                <div class="dropdown-menu ${grpOrg ? 'show' : ''}">
                                                    <a class="dropdown-item ${activePath == '/admin/departments' ? 'active' : ''}"
                                                        href="${ctx}/admin/departments">Phòng ban</a>
                                                    <a class="dropdown-item ${activePath == '/admin/categories' ? 'active' : ''}"
                                                        href="${ctx}/admin/categories">Chuyên mục</a>
                                                    <a class="dropdown-item ${activePath == '/admin/managers' ? 'active' : ''}"
                                                        href="${ctx}/admin/managers">Manager và phạm vi</a>
                                                </div>
                                            </li>

                                            <li class="nav-item dropdown ${grpKnowledge ? 'active' : ''}">
                                                <a class="nav-link dropdown-toggle ${grpKnowledge ? 'show' : ''}"
                                                    href="#qa-nav-knowledge" data-bs-toggle="dropdown"
                                                    data-bs-auto-close="false" role="button"
                                                    aria-expanded="${grpKnowledge}">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><i
                                                            class="bi bi-journal-text"></i></span>
                                                    <span class="nav-link-title">Tri thức</span>
                                                </a>
                                                <div class="dropdown-menu ${grpKnowledge ? 'show' : ''}">
                                                    <a class="dropdown-item ${activePath == '/admin/faq' ? 'active' : ''}"
                                                        href="${ctx}/admin/faq">Bài FAQ</a>
                                                    <a class="dropdown-item ${activePath == '/admin/faq/tags' ? 'active' : ''}"
                                                        href="${ctx}/admin/faq/tags">Thẻ (tag)</a>
                                                    <a class="dropdown-item ${activePath == '/admin/faq/synonyms' ? 'active' : ''}"
                                                        href="${ctx}/admin/faq/synonyms">Từ đồng nghĩa</a>
                                                </div>
                                            </li>

                                            <li class="nav-item ${activePath == '/admin/tickets' ? 'active' : ''}">
                                                <a class="nav-link" href="${ctx}/admin/tickets">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><i
                                                            class="bi bi-ticket-detailed"></i></span>
                                                    <span class="nav-link-title">Yêu cầu tư vấn</span>
                                                </a>
                                            </li>

                                            <li class="nav-item dropdown ${grpPolicy ? 'active' : ''}">
                                                <a class="nav-link dropdown-toggle ${grpPolicy ? 'show' : ''}"
                                                    href="#qa-nav-policy" data-bs-toggle="dropdown"
                                                    data-bs-auto-close="false" role="button"
                                                    aria-expanded="${grpPolicy}">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><i
                                                            class="bi bi-sliders"></i></span>
                                                    <span class="nav-link-title">Chính sách</span>
                                                </a>
                                                <div class="dropdown-menu ${grpPolicy ? 'show' : ''}">
                                                    <a class="dropdown-item ${activePath == '/admin/sla' ? 'active' : ''}"
                                                        href="${ctx}/admin/sla">SLA và giờ làm việc</a>
                                                    <a class="dropdown-item ${activePath == '/admin/settings' ? 'active' : ''}"
                                                        href="${ctx}/admin/settings">Cấu hình hệ thống</a>
                                                </div>
                                            </li>

                                            <li
                                                class="nav-item ${activePath == '/admin/announcements' ? 'active' : ''}">
                                                <a class="nav-link" href="${ctx}/admin/announcements">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><i
                                                            class="bi bi-megaphone"></i></span>
                                                    <span class="nav-link-title">Thông báo chính thức</span>
                                                </a>
                                            </li>

                                            <li class="nav-item dropdown ${grpMonitor ? 'active' : ''}">
                                                <a class="nav-link dropdown-toggle ${grpMonitor ? 'show' : ''}"
                                                    href="#qa-nav-monitor" data-bs-toggle="dropdown"
                                                    data-bs-auto-close="false" role="button"
                                                    aria-expanded="${grpMonitor}">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><i
                                                            class="bi bi-shield-check"></i></span>
                                                    <span class="nav-link-title">Giám sát</span>
                                                </a>
                                                <div class="dropdown-menu ${grpMonitor ? 'show' : ''}">
                                                    <a class="dropdown-item ${activePath == '/admin/audit-logs' ? 'active' : ''}"
                                                        href="${ctx}/admin/audit-logs">Nhật ký hệ thống</a>
                                                    <a class="dropdown-item ${activePath == '/admin/reports' ? 'active' : ''}"
                                                        href="${ctx}/admin/reports">Báo cáo</a>
                                                    <a class="dropdown-item ${activePath == '/admin/health' ? 'active' : ''}"
                                                        href="${ctx}/admin/health">Sức khỏe hệ thống</a>
                                                </div>
                                            </li>

                                            <li class="nav-item ${activePath == '/admin/profile' ? 'active' : ''}">
                                                <a class="nav-link" href="${ctx}/admin/profile">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-person-circle"></i></span>
                                                    <span class="nav-link-title">Hồ sơ admin</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                    <div class="qa-workspace-sidebar-footer"><span
                                            class="qa-workspace-role-dot"></span>Không gian quản trị<a
                                            href="${ctx}/guide" aria-label="Hướng dẫn sử dụng"><i
                                                class="bi bi-question-circle"></i></a></div>
                                </div>
                            </aside>

                            <header class="navbar navbar-expand-md d-print-none qa-workspace-topbar">
                                <div class="container-xl">
                                    <button class="btn qa-sidebar-toggle" type="button" data-workspace-toggle
                                        aria-controls="workspace-sidebar" aria-expanded="true"
                                        aria-label="Thu gọn hoặc mở menu">
                                        <i class="bi bi-list"></i>
                                    </button>
                                    <a href="${ctx}/admin/dashboard" class="qa-topbar-brand d-lg-none">
                                        <img src="${ctx}/assets/images/hcmute-logo.png" alt="Logo HCMUTE">
                                        <strong>QA<span>UTE</span></strong>
                                    </a>
                                    <div class="qa-workspace-location"><small>KHÔNG GIAN QUẢN TRỊ</small><strong>
                                            <c:out value="${workspacePageTitle}" />
                                        </strong></div>
                                    <div class="navbar-nav flex-row ms-auto align-items-center gap-3">

                                        <div class="nav-item dropdown">
                                            <a href="#" class="nav-link d-flex lh-1 text-reset p-0"
                                                data-bs-toggle="dropdown" aria-label="Mở menu người dùng">
                                                <span class="qa-workspace-avatar qa-workspace-avatar--small">
                                                    <c:out value="${workspaceInitial}" />
                                                </span>
                                                <div class="d-none d-xl-block ps-2">
                                                    <div>
                                                        <c:out value="${workspaceName}" />
                                                    </div>
                                                    <div class="mt-1 small text-secondary">Quản trị viên</div>
                                                </div>
                                            </a>
                                            <div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
                                                <a class="dropdown-item" href="${ctx}/admin/profile"><i class="bi bi-person-circle me-2"></i>Hồ sơ admin</a>
                                                <form method="post" action="${ctx}/auth/logout" class="m-0">
                                                    <input type="hidden" name="${_csrf.parameterName}"
                                                        value="${_csrf.token}" />
                                                    <button type="submit" class="dropdown-item"><i
                                                            class="bi bi-box-arrow-right me-2"></i>Đăng xuất</button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </header>

                            <main class="page-wrapper" id="workspace-main" tabindex="-1">

                                <c:if test="${not empty requestScope.pageTitle}">
                                    <div class="page-header d-print-none">
                                        <div class="container-xl">
                                            <ol class="breadcrumb breadcrumb-arrows" aria-label="breadcrumbs">
                                                <li class="breadcrumb-item"><a href="${ctx}/admin/dashboard">Quản
                                                        trị</a></li>
                                                <li class="breadcrumb-item active" aria-current="page">
                                                    <c:out value="${requestScope.pageTitle}" />
                                                </li>
                                            </ol>
                                        </div>
                                    </div>
                                </c:if>

                                <div class="page-body">
                                    <div class="container-xl qa-workspace-container">
                                        <qa:alert />
                                        <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
                                    </div>
                                </div>
                                <footer class="footer footer-transparent d-print-none">
                                    <div class="container-xl qa-workspace-footer"><span>&copy; QAUTE · Cổng hỗ trợ sinh
                                            viên</span><a href="${ctx}/guide">Hướng dẫn sử dụng <i
                                                class="bi bi-arrow-up-right"></i></a></div>
                                </footer>
                            </main>
                        </div>

                        <%@ include file="/WEB-INF/views/common/scripts.jspf" %>
                            <script src="${pageContext.request.contextPath}/assets/js/tv2-layout.js" defer></script>
                    </body>

                    </html>