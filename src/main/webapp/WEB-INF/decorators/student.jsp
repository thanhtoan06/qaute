<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page import="org.sitemesh.content.Content" %>
<%
    Content pageContent = (Content) request.getAttribute("org.sitemesh.content.Content");
%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="currentUri" value="${requestScope['jakarta.servlet.forward.request_uri']}"/>
<c:if test="${empty currentUri}">
    <c:set var="currentUri" value="${pageContext.request.requestURI}"/>
</c:if>
<c:if test="${not empty ctx and fn:startsWith(currentUri, ctx)}">
    <c:set var="currentUri" value="${fn:substring(currentUri, fn:length(ctx), fn:length(currentUri))}"/>
</c:if>

<c:set var="studentName" value="Sinh viên"/>
<c:catch var="principalError">
    <sec:authentication property="principal.fullName" var="authenticatedFullName"/>
    <c:if test="${not empty authenticatedFullName}">
        <c:set var="studentName" value="${authenticatedFullName}"/>
    </c:if>
</c:catch>
<c:set var="studentEmail" value=""/>
<c:catch var="principalEmailError">
    <sec:authentication property="principal.email" var="authenticatedEmail"/>
    <c:if test="${not empty authenticatedEmail}">
        <c:set var="studentEmail" value="${authenticatedEmail}"/>
    </c:if>
</c:catch>
<c:set var="avatarInitial" value="S"/>
<c:if test="${not empty studentName and fn:length(studentName) > 0}">
    <c:set var="nameParts" value="${fn:split(studentName, ' ')}"/>
    <c:choose>
        <c:when test="${fn:length(nameParts) >= 2}">
            <c:set var="avatarInitial" value="${fn:substring(nameParts[0],0,1)}${fn:substring(nameParts[fn:length(nameParts)-1],0,1)}"/>
        </c:when>
        <c:otherwise>
            <c:set var="avatarInitial" value="${fn:substring(studentName,0,1)}"/>
        </c:otherwise>
    </c:choose>
</c:if>

<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
<style>
:root { --stu-red: #206bc4; --stu-red-dark: #154c91; --stu-red-light: #eef6ff; --stu-sidebar-bg: #0a1b34; --stu-sidebar-w: 260px; --stu-topbar-h: 68px; --stu-text-muted: #91a5c0; --stu-text: #223954; --stu-muted: #7789a1; --stu-border: #e2eaf4; --stu-surface: #ffffff; --stu-radius: 16px; --stu-shadow-sm: 0 4px 14px rgba(14, 43, 79, .045); --stu-shadow: 0 10px 28px rgba(14, 43, 79, .075); --stu-shadow-lg: 0 20px 48px rgba(14, 43, 79, .12); --stu-ease: cubic-bezier(.2, .75, .3, 1); }
html { min-height: 100%; scroll-behavior: smooth; scroll-padding-top: 84px; }
body.stu-layout { position: relative; isolation: isolate; margin: 0; min-height: 100vh; min-height: 100dvh; font-family: Inter, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif; background-color: #f5f8fd; background-image: radial-gradient(circle at 1px 1px, rgba(32, 107, 196, .075) 1px, transparent 1.5px), repeating-linear-gradient(135deg, transparent 0, transparent 125px, rgba(32, 107, 196, .019) 126px, rgba(32, 107, 196, .019) 127px, transparent 128px, transparent 195px), radial-gradient(ellipse at 77% 0%, rgba(63, 139, 245, .105), transparent 35%), radial-gradient(ellipse at 4% 88%, rgba(220, 53, 69, .028), transparent 32%), linear-gradient(145deg, #f5f8fd 0%, #fbfdff 52%, #f0f5fc 100%); background-size: 29px 29px, 195px 195px, auto, auto, auto; background-attachment: fixed; color: #2d3d53; -webkit-font-smoothing: antialiased; text-rendering: optimizeLegibility; overflow-x: hidden; }
body.stu-layout::before { content: ""; position: fixed; z-index: -1; top: -330px; right: -190px; width: clamp(350px, 43vw, 620px); aspect-ratio: 1; border: 1px solid rgba(32, 107, 196, .09); border-radius: 50%; box-shadow: 0 0 0 29px rgba(32, 107, 196, .018), 0 0 0 70px rgba(32, 107, 196, .014), 0 0 0 120px rgba(32, 107, 196, .009); pointer-events: none; animation: qa-workspace-ambient-drift 28s ease-in-out infinite alternate; }
body.stu-layout::after { content: ""; position: fixed; z-index: -1; left: -210px; bottom: -240px; width: 340px; aspect-ratio: 1; border: 1px solid rgba(32, 107, 196, .075); border-radius: 50%; box-shadow: 0 0 0 30px rgba(32, 107, 196, .015), 0 0 0 68px rgba(32, 107, 196, .01); pointer-events: none; }
@keyframes stu-glow {
  0%, 100% { box-shadow: 0 5px 16px rgba(32, 107, 196, .16); }
  50% { box-shadow: 0 8px 23px rgba(32, 107, 196, .24); }
}
a { text-underline-offset: 3px; }
img { max-width: 100%; }
::selection { background: #206bc4; color: #ffffff; }
:focus-visible { outline: 3px solid rgba(32, 107, 196, .35); outline-offset: 3px; }
.stu-sidebar { position: fixed; top: 0; left: 0; width: var(--stu-sidebar-w); height: 100vh; height: 100dvh; background: radial-gradient(ellipse at 90% 0%, rgba(49, 117, 199, .23), transparent 36%), radial-gradient(ellipse at 0% 76%, rgba(37, 87, 147, .13), transparent 40%), linear-gradient(170deg, #102c50 0%, #0a1b34 49%, #07172c 100%); display: flex; flex-direction: column; z-index: 1040; transition: transform .3s var(--stu-ease), width .28s var(--stu-ease); overflow-y: auto; overflow-x: hidden; scrollbar-width: thin; scrollbar-color: #345172 transparent; box-shadow: 8px 0 32px rgba(7, 23, 44, .105); }
.stu-sidebar::before { content: ""; position: absolute; z-index: 0; width: 320px; height: 320px; top: 115px; right: -185px; border: 1px solid rgba(255, 255, 255, .065); border-radius: 50%; box-shadow: 0 0 0 29px rgba(255, 255, 255, .017), 0 0 0 66px rgba(255, 255, 255, .012); pointer-events: none; }
.stu-sidebar::after { content: ""; position: absolute; z-index: 0; width: 230px; height: 230px; left: -145px; bottom: 100px; border-radius: 50%; background: radial-gradient(circle, rgba(48, 128, 225, .12), transparent 70%); pointer-events: none; }
.stu-sidebar > * { position: relative; z-index: 1; }
.stu-sidebar::-webkit-scrollbar { width: 4px; }
.stu-sidebar::-webkit-scrollbar-thumb { background: rgba(255, 255, 255, .18); border-radius: 4px; }
.stu-sidebar::-webkit-scrollbar-thumb:hover { background: rgba(255, 255, 255, .3); }
.stu-brand { display: flex; align-items: center; gap: .7rem; padding: 1.25rem 1.3rem 1.15rem; border-bottom: 1px solid rgba(255, 255, 255, .085); text-decoration: none; flex-shrink: 0; transition: background .2s ease; }
.stu-brand:hover { background: rgba(255, 255, 255, .025); }
.stu-brand-icon { width: 40px; height: 40px; background: linear-gradient(135deg, #287be0, #154c91); border: 1px solid rgba(255, 255, 255, .16); border-radius: 12px; display: flex; align-items: center; justify-content: center; color: #fff; font-weight: 800; font-size: 1rem; flex-shrink: 0; box-shadow: 0 5px 15px rgba(32, 107, 196, .24); }
.stu-brand-logo { width: 40px; height: 40px; object-fit: contain; background: #ffffff; border: 1px solid rgba(255, 255, 255, .15); border-radius: 12px; padding: 3px; flex-shrink: 0; box-shadow: 0 5px 15px rgba(0, 0, 0, .13); transition: transform .25s var(--stu-ease), box-shadow .25s ease; }
.stu-brand:hover .stu-brand-logo { transform: translateY(-2px) rotate(-3deg); box-shadow: 0 8px 19px rgba(0, 0, 0, .2); }
.stu-brand-text { min-width: 0; }
.stu-brand-text .t1 { color: #ffffff; font-weight: 850; font-size: 1.08rem; line-height: 1.1; letter-spacing: -.045em; }
.stu-brand-text .t2 { color: #9eb2cc; font-size: .62rem; line-height: 1.5; margin-top: 4px; white-space: nowrap; }
.stu-user-card { display: flex; align-items: center; gap: .75rem; padding: .85rem .9rem; margin: .9rem .75rem .3rem; background: linear-gradient(135deg, rgba(255, 255, 255, .085), rgba(255, 255, 255, .035)); border: 1px solid rgba(255, 255, 255, .07); border-radius: 15px; text-decoration: none; transition: background .22s ease, border-color .22s ease, transform .22s ease, box-shadow .22s ease; }
.stu-user-card:hover { background: linear-gradient(135deg, rgba(255, 255, 255, .13), rgba(255, 255, 255, .05)); border-color: rgba(135, 193, 255, .2); transform: translateY(-1px); box-shadow: 0 7px 18px rgba(0, 0, 0, .09); }
.stu-user-avatar { width: 40px; height: 40px; background: linear-gradient(135deg, #3988e5, #2361b7); border: 2px solid rgba(255, 255, 255, .18); border-radius: 13px; display: flex; align-items: center; justify-content: center; color: #fff; font-weight: 800; font-size: .85rem; flex-shrink: 0; box-shadow: 0 5px 14px rgba(32, 107, 196, .25); }
.stu-user-name { color: #ffffff; font-weight: 700; font-size: .82rem; line-height: 1.45; }
.stu-user-role { display: inline-flex; align-items: center; gap: 6px; color: #a9bdd6; font-size: .68rem; margin-top: 4px; }
.stu-user-role::before { content: ""; width: 6px; height: 6px; flex-shrink: 0; border-radius: 50%; background: #53c6a1; box-shadow: 0 0 0 3px rgba(83, 198, 161, .1); }
.stu-nav-label { color: #8099b8; font-size: .63rem; font-weight: 800; letter-spacing: .12em; text-transform: uppercase; padding: 1.3rem 1.25rem .5rem; }
.stu-nav { list-style: none; padding: 0 .75rem; margin: 0; }
.stu-nav li { margin-bottom: 4px; }
.stu-nav a { position: relative; display: flex; align-items: center; gap: .75rem; min-height: 43px; padding: .65rem .85rem; border: 1px solid transparent; border-radius: 11px; color: rgba(225, 236, 250, .72); font-size: .81rem; font-weight: 550; text-decoration: none; transition: background .2s ease, color .2s ease, border-color .2s ease, transform .2s ease, box-shadow .2s ease; }
.stu-nav a i { display: inline-flex; align-items: center; justify-content: center; width: 21px; min-width: 21px; font-size: 1.02rem; color: #9cb7d7; text-align: center; flex-shrink: 0; transition: color .2s ease, transform .2s ease; }
.stu-nav a:hover { background: rgba(255, 255, 255, .075); border-color: rgba(255, 255, 255, .035); color: #ffffff; transform: translateX(2px); }
.stu-nav a:hover i { color: #b2d9ff; transform: scale(1.08); }
.stu-nav a.active { background: linear-gradient(110deg, #206bc4, #3286db); border-color: rgba(255, 255, 255, .1); color: #ffffff; font-weight: 700; box-shadow: 0 6px 16px rgba(9, 61, 121, .28), inset 0 1px 0 rgba(255, 255, 255, .1); }
.stu-nav a.active::after { content: ""; position: absolute; top: 9px; bottom: 9px; right: 0; width: 3px; border-radius: 4px; background: #b9e0ff; box-shadow: 0 0 12px rgba(185, 224, 255, .5); }
.stu-nav a.active i { color: #ffffff; }
.stu-nav a .nav-badge { margin-left: auto; background: linear-gradient(135deg, #e34c62, #c62d45); color: #fff; font-size: .65rem; padding: .2em .58em; border-radius: 20px; font-weight: 800; min-width: 20px; text-align: center; box-shadow: 0 3px 9px rgba(198, 45, 69, .2); }
.stu-nav a.active .nav-badge { background: rgba(255, 255, 255, .22); box-shadow: none; }
.stu-sidebar-footer { margin-top: auto; padding: .85rem .75rem; border-top: 1px solid rgba(255, 255, 255, .085); }
.stu-sidebar-footer form button { display: flex; align-items: center; gap: .7rem; width: 100%; min-height: 42px; padding: .65rem .85rem; border-radius: 11px; background: rgba(255, 255, 255, .045); border: 1px solid rgba(255, 255, 255, .035); color: rgba(225, 236, 250, .67); font-size: .81rem; font-weight: 550; cursor: pointer; transition: background .2s ease, color .2s ease, border-color .2s ease, transform .2s ease; }
.stu-sidebar-footer form button i { font-size: 1.05rem; }
.stu-sidebar-footer form button:hover { background: rgba(220, 53, 69, .17); border-color: rgba(240, 100, 117, .18); color: #ffffff; transform: translateY(-1px); }
.stu-topbar { position: fixed; top: 0; left: var(--stu-sidebar-w); right: 0; height: var(--stu-topbar-h); background: rgba(255, 255, 255, .92); -webkit-backdrop-filter: blur(18px); backdrop-filter: blur(18px); border-bottom: 1px solid rgba(222, 232, 244, .92); display: flex; align-items: center; padding: 0 1.5rem; z-index: 1030; gap: 1rem; box-shadow: 0 5px 22px rgba(17, 43, 78, .045); transition: left .3s ease, box-shadow .25s ease, background .25s ease; }
.stu-topbar::after { content: ""; position: absolute; bottom: -1px; left: 0; right: 0; height: 1px; background: linear-gradient(90deg, transparent, rgba(32, 107, 196, .16), transparent); pointer-events: none; }
.stu-topbar .topbar-title { font-weight: 800; font-size: 1rem; color: #1a3555; letter-spacing: -.02em; flex: 1; }
.stu-topbar .topbar-right { display: flex; align-items: center; gap: .55rem; min-width: 0; }
.stu-sidebar-toggle { display: inline-flex !important; align-items: center; justify-content: center; width: 39px; height: 39px; background: linear-gradient(135deg, #287be0, #185caf); border: 1px solid #206bc4; font-size: 1.2rem; color: #fff; cursor: pointer; padding: 0; border-radius: 11px; flex: 0 0 39px; box-shadow: 0 4px 12px rgba(32, 107, 196, .2); transition: background .2s ease, transform .2s ease, box-shadow .2s ease; }
.stu-sidebar-toggle:hover { background: linear-gradient(135deg, #206bc4, #154c91); box-shadow: 0 7px 17px rgba(32, 107, 196, .27); transform: translateY(-1px); }
.stu-sidebar-toggle:active { transform: scale(.97); }
.stu-sidebar-toggle:focus-visible { outline: 3px solid rgba(32, 107, 196, .3); outline-offset: 3px; }
@media (min-width: 992px) {
  body.stu-sidebar-collapsed .stu-sidebar { width: 76px; }
  body.stu-sidebar-collapsed .stu-sidebar .stu-brand { justify-content: center; padding-left: .75rem; padding-right: .75rem; }
  body.stu-sidebar-collapsed .stu-brand-text, body.stu-sidebar-collapsed .stu-user-card > div:last-child, body.stu-sidebar-collapsed .stu-nav-label, body.stu-sidebar-collapsed .stu-nav a span, body.stu-sidebar-collapsed .stu-sidebar-footer span { display: none; }
  body.stu-sidebar-collapsed .stu-user-card { justify-content: center; margin-left: .5rem; margin-right: .5rem; padding-left: .5rem; padding-right: .5rem; }
  body.stu-sidebar-collapsed .stu-nav { padding-left: .5rem; padding-right: .5rem; }
  body.stu-sidebar-collapsed .stu-nav a { justify-content: center; padding-left: .6rem; padding-right: .6rem; }
  body.stu-sidebar-collapsed .stu-nav a.active::after { right: -1px; }
  body.stu-sidebar-collapsed .stu-sidebar-footer form button { justify-content: center; }
  body.stu-sidebar-collapsed .stu-topbar { left: 76px; }
  body.stu-sidebar-collapsed .stu-main { margin-left: 76px; }
}
.stu-topbar-user { display: flex; align-items: center; gap: .6rem; cursor: pointer; padding: .32rem .6rem; border: 1px solid transparent; border-radius: 12px; text-decoration: none; color: inherit; transition: background .2s ease, border-color .2s ease, box-shadow .2s ease; }
.stu-topbar-user:hover, .stu-topbar-user[aria-expanded="true"] { background: #f3f7fd; border-color: #e6edf7; box-shadow: 0 3px 9px rgba(17, 43, 78, .035); }
.stu-topbar-user .u-avatar { width: 36px; height: 36px; background: linear-gradient(135deg, #3988e5, #2361b7); border: 2px solid #ffffff; border-radius: 12px; display: flex; align-items: center; justify-content: center; color: #fff; font-weight: 800; font-size: .8rem; box-shadow: 0 3px 9px rgba(32, 107, 196, .17); }
.stu-topbar-user .u-name { font-size: .82rem; font-weight: 700; color: #263c57; max-width: 160px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.stu-topbar-brand { display: inline-flex; align-items: center; gap: .4rem; color: #1f2937; text-decoration: none; }
.stu-topbar-brand img { width: 30px; height: 30px; object-fit: contain; background: #fff; border: 1px solid #e3eaf4; border-radius: 8px; padding: 2px; }
.stu-topbar-brand span { color: var(--stu-red); }
.stu-topbar-brand strong { font-size: 1rem; font-weight: 850; letter-spacing: -.035em; }
.stu-main { position: relative; z-index: 1; margin-left: var(--stu-sidebar-w); padding-top: var(--stu-topbar-h); min-height: 100vh; min-height: 100dvh; transition: margin-left .3s ease; background: transparent; }
.stu-page-body { position: relative; min-height: calc(100vh - var(--stu-topbar-h)); padding: 1.8rem 1.65rem; animation: qaute-enter .42s var(--stu-ease) both; }
.stu-page-header { display: flex; align-items: flex-start; justify-content: space-between; flex-wrap: wrap; gap: .85rem; margin-bottom: 1.65rem; }
.stu-page-header .page-pretitle { font-size: .68rem; color: #8295ae; text-transform: uppercase; font-weight: 800; letter-spacing: .1em; margin-bottom: .35rem; }
.stu-page-header h2 { margin: 0; font-size: clamp(1.35rem, 2.2vw, 1.8rem); font-weight: 850; letter-spacing: -.04em; color: #183759; line-height: 1.3; }
.page-header.d-print-none { padding: 0 !important; border: none !important; margin-bottom: 1.45rem; }
.page-header.d-print-none .container-xl { padding: 0 !important; max-width: 100% !important; }
.page-body { padding: 0 !important; }
.page-body .container-xl { padding: 0 !important; max-width: 100% !important; }
.stu-main .breadcrumb { font-size: .79rem; color: #8797ac; }
.stu-main .breadcrumb a { color: #6480a1; text-decoration: none; transition: color .2s ease; }
.stu-main .breadcrumb a:hover { color: var(--stu-red); }
.stu-main .page-title { color: #19375d; font-size: 1.5rem; font-weight: 850; letter-spacing: -.04em; line-height: 1.35; }
.stu-main .text-secondary, .stu-main .text-muted { color: #7c8da4 !important; }
.stu-main .card { border: 1px solid rgba(222, 232, 244, .95); border-radius: var(--stu-radius); background: rgba(255, 255, 255, .97); box-shadow: var(--stu-shadow-sm); transition: border-color .25s ease, box-shadow .25s ease, transform .25s ease; overflow: visible; }
.stu-main .card:hover { border-color: rgba(32, 107, 196, .17); box-shadow: var(--stu-shadow); }
.stu-main .card-header { padding: 1rem 1.25rem; border-bottom: 1px solid #edf1f7; border-radius: var(--stu-radius) var(--stu-radius) 0 0; background: linear-gradient(120deg, #ffffff, #f7faff); }
.stu-main .card-header .card-title { margin-bottom: 0; font-size: .96rem; font-weight: 800; color: #263f5e; }
.stu-main .card-body { padding: 1.25rem; }
.stu-main .card-footer { padding: .95rem 1.25rem; border-top: 1px solid #edf1f7; border-radius: 0 0 var(--stu-radius) var(--stu-radius); background: #fbfcff; }
.stu-main .card-title { color: #263f5e; font-weight: 800; }
.stu-main .list-group-item { position: relative; display: block; }
.stu-main .list-group-item .d-flex { position: relative; display: flex; align-items: flex-start; gap: 1rem; }
.stu-main .list-group-item .avatar { position: relative; flex-shrink: 0; }
.stu-main .list-group-item form { position: static; }
.stu-main .list-group-item .btn-ghost-secondary { position: relative; flex-shrink: 0; margin-left: auto; }
.stu-stat-card { position: relative; isolation: isolate; overflow: hidden; background: linear-gradient(145deg, #ffffff, #fbfdff); border-radius: 16px; padding: 1.25rem 1.35rem; border: 1px solid #e2eaf4; box-shadow: var(--stu-shadow-sm); display: flex; align-items: center; gap: 1rem; text-decoration: none; color: inherit; transition: transform .25s var(--stu-ease), box-shadow .25s ease, border-color .25s ease; }
.stu-stat-card::after { content: ""; position: absolute; z-index: -1; right: -45px; bottom: -55px; width: 120px; height: 120px; border-radius: 50%; background: rgba(32, 107, 196, .035); transition: transform .3s ease, background .3s ease; pointer-events: none; }
.stu-stat-card:hover { transform: translateY(-4px); border-color: #c5d9f0; box-shadow: 0 13px 28px rgba(21, 64, 112, .095); color: inherit; }
.stu-stat-card:hover::after { transform: scale(1.25); background: rgba(32, 107, 196, .06); }
.stu-stat-icon { width: 51px; height: 51px; border-radius: 15px; display: flex; align-items: center; justify-content: center; font-size: 1.4rem; flex-shrink: 0; background: linear-gradient(135deg, #edf5ff, #dfeeff); color: #206bc4; transition: transform .25s ease, box-shadow .25s ease; }
.stu-stat-card:hover .stu-stat-icon { transform: translateY(-2px) rotate(-4deg); box-shadow: 0 6px 15px rgba(32, 107, 196, .12); }
.stu-stat-value { font-size: clamp(1.45rem, 2vw, 1.8rem); font-weight: 850; line-height: 1.15; letter-spacing: -.045em; color: #203953; }
.stu-stat-label { font-size: .76rem; color: #71839a; margin-top: .35rem; line-height: 1.5; }
.btn-stu-primary { background: linear-gradient(120deg, #287be0, #185caf); color: #fff; border: 1px solid #206bc4; border-radius: 11px; padding: .6rem 1.1rem; font-weight: 700; font-size: .84rem; box-shadow: 0 4px 12px rgba(32, 107, 196, .15); transition: background .2s ease, transform .2s ease, box-shadow .2s ease; text-decoration: none; display: inline-flex; align-items: center; justify-content: center; gap: .45rem; cursor: pointer; }
.btn-stu-primary:hover { background: linear-gradient(120deg, #206bc4, #154c91); color: #fff; transform: translateY(-2px); box-shadow: 0 8px 18px rgba(32, 107, 196, .23); }
.btn-stu-primary:active { transform: translateY(0) scale(.985); }
.btn-stu-outline { background: rgba(255, 255, 255, .9); color: var(--stu-red); border: 1px solid rgba(32, 107, 196, .28); border-radius: 11px; padding: .6rem 1.1rem; font-weight: 700; font-size: .84rem; box-shadow: 0 2px 7px rgba(32, 107, 196, .025); transition: all .2s ease; text-decoration: none; display: inline-flex; align-items: center; justify-content: center; gap: .45rem; cursor: pointer; }
.btn-stu-outline:hover { background: linear-gradient(120deg, #287be0, #185caf); border-color: var(--stu-red); color: #fff; transform: translateY(-1px); box-shadow: 0 6px 15px rgba(32, 107, 196, .16); }
.stu-main .btn { border-radius: 10px; font-weight: 650; transition: transform .2s var(--stu-ease), box-shadow .2s ease, background-color .2s ease, border-color .2s ease; }
.stu-main .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.stu-main .btn:not(:disabled):not(.btn-close):active { transform: translateY(0) scale(.985); }
.stu-main .btn-primary, .btn-primary { background: linear-gradient(120deg, #287be0, #185caf); border-color: #206bc4; box-shadow: 0 4px 12px rgba(32, 107, 196, .13); }
.stu-main .btn-primary:hover, .btn-primary:hover { background: linear-gradient(120deg, #206bc4, #154c91); border-color: #154c91; box-shadow: 0 7px 18px rgba(32, 107, 196, .22); }
.stu-main .btn-outline-primary, .btn-outline-primary { color: var(--stu-red); border-color: rgba(32, 107, 196, .4); background: rgba(255, 255, 255, .9); }
.stu-main .btn-outline-primary:hover, .btn-outline-primary:hover { background: linear-gradient(120deg, #287be0, #185caf); color: #fff; border-color: #206bc4; }
a.text-primary, .text-primary { color: var(--stu-red) !important; }
.bg-primary { background: linear-gradient(120deg, #287be0, #185caf) !important; }
.border-primary { border-color: var(--stu-red) !important; }
.btn-teal { background: linear-gradient(120deg, #11998e, #0d8178); color: #fff; border: 1px solid #0d9488; box-shadow: 0 4px 11px rgba(13, 148, 136, .13); transition: background .2s ease, transform .2s ease, box-shadow .2s ease; }
.btn-teal:hover { background: linear-gradient(120deg, #0d9488, #0f766e); color: #fff; box-shadow: 0 7px 16px rgba(13, 148, 136, .2); }
.badge { font-weight: 650; letter-spacing: .005em; border-radius: 7px; }
.badge.bg-primary { background: linear-gradient(120deg, #287be0, #185caf) !important; }
.nav-tabs { border-bottom-color: #e4ebf4; gap: 4px; }
.nav-tabs .nav-link { border-radius: 10px 10px 0 0; color: #71839a; font-weight: 650; transition: color .2s ease, background .2s ease, border-color .2s ease; }
.nav-tabs .nav-link.active { border-bottom-color: var(--stu-red); color: var(--stu-red); background: rgba(32, 107, 196, .045); }
.nav-tabs .nav-link:hover { color: var(--stu-red); }
.stu-main .form-label { color: #3e5572; font-size: .82rem; font-weight: 700; margin-bottom: .45rem; }
.stu-main .form-control, .stu-main .form-select { min-height: 42px; color: #253b57; background-color: rgba(255, 255, 255, .97); border: 1px solid #dce6f2; border-radius: 11px; transition: border-color .2s ease, box-shadow .2s ease, background-color .2s ease; }
.stu-main textarea.form-control { min-height: 110px; }
.stu-main .form-control::placeholder { color: #98a7b9; }
.stu-main .form-control:hover, .stu-main .form-select:hover { border-color: #b8cce5; }
.stu-main .form-control:focus, .stu-main .form-select:focus { border-color: #5596dd; background-color: #fff; box-shadow: 0 0 0 4px rgba(32, 107, 196, .09); }
.stu-main .form-check-input { border-color: #bdcde0; cursor: pointer; }
.stu-main .form-check-input:checked { background-color: #206bc4; border-color: #206bc4; }
.stu-main .form-check-label { color: #647891; }
.stu-main .table-responsive, .stu-main .table-responsive-card { border-radius: 13px; }
.stu-main .table { margin-bottom: 0; }
.stu-main .table th { padding: .9rem 1rem; font-size: .72rem; text-transform: uppercase; letter-spacing: .055em; color: #71839b; font-weight: 750; background: linear-gradient(180deg, #f8fafd, #f3f7fc); border-bottom: 1px solid #e3eaf3; white-space: nowrap; }
.stu-main .table td { padding: .9rem 1rem; border-color: #edf1f7; vertical-align: middle; color: #425873; }
.stu-main .table tbody tr { transition: background-color .18s ease; }
.stu-main .table tbody tr:hover { background: #f5f9ff; }
.stu-main .table tbody tr:last-child td { border-bottom: 0; }
.stu-main .list-group { border-radius: 13px; overflow: hidden; }
.stu-main .list-group-item { border-color: #eaf0f7; transition: background-color .2s ease, border-color .2s ease; }
.stu-main .list-group-item-action:hover { background: #f5f9ff; }
.stu-main .list-group-item.active { background: linear-gradient(110deg, #206bc4, #3286db); border-color: #206bc4; color: #ffffff; }
.stu-main .dropdown-menu { padding: .5rem; border: 1px solid #e1e9f4; border-radius: 13px; box-shadow: var(--stu-shadow-lg); animation: qaute-enter .18s ease both; }
.stu-main .dropdown-item { padding: .62rem .8rem; border-radius: 8px; font-size: .82rem; transition: color .18s ease, background .18s ease; }
.stu-main .dropdown-item:hover, .stu-main .dropdown-item:focus { background: #eef5ff; color: #185caf; }
.stu-main .modal-content { border: 1px solid #e1e9f4; border-radius: 17px; box-shadow: 0 24px 80px rgba(11, 31, 58, .19); overflow: hidden; }
.stu-main .modal-header, .stu-main .modal-footer { background: linear-gradient(130deg, #ffffff, #f8fbff); border-color: #eaf0f7; }
.stu-main .modal-title { color: #1c395d; font-weight: 800; }
.stu-main .alert { border-radius: 13px; box-shadow: 0 4px 13px rgba(17, 43, 78, .035); }
.alert-important.alert-warning { background: #fffbeb; border-color: #fde68a; color: #92400e; }
.alert-info { background: #eff6ff; border-color: #bfdbfe; color: #1e40af; }
#notification-bell-dropdown .nav-link { color: #52647d; border-radius: 11px; transition: color .2s ease, background .2s ease; }
#notification-bell-dropdown .nav-link:hover { color: var(--stu-red); background: #f0f6ff; }
.stu-main .pagination { gap: 4px; }
.stu-main .pagination .page-link { border: 1px solid #e1e9f4; border-radius: 9px; color: #536b88; background: #ffffff; box-shadow: 0 2px 5px rgba(17, 43, 78, .02); transition: color .2s ease, background .2s ease, border-color .2s ease, transform .2s ease; }
.stu-main .pagination .page-link:hover { color: #185caf; border-color: #c5d9f0; background: #f2f7ff; transform: translateY(-1px); }
.stu-main .pagination .active .page-link { color: #ffffff; background: linear-gradient(120deg, #287be0, #185caf); border-color: #206bc4; }
.stu-sidebar-overlay { display: none; position: fixed; inset: 0; background: rgba(6, 22, 43, .52); -webkit-backdrop-filter: blur(3px); backdrop-filter: blur(3px); z-index: 1039; }
@media (max-width: 1199.98px) {
  .stu-page-body { padding: 1.5rem 1.25rem; }
  .stu-topbar { padding: 0 1.25rem; }
  .stu-main .page-title { font-size: 1.4rem; }
}
@media (max-width: 991.98px) {
  .stu-sidebar { transform: translateX(-100%); box-shadow: 16px 0 44px rgba(7, 23, 44, .22); }
  .stu-sidebar.open { transform: translateX(0); }
  .stu-topbar { left: 0; }
  .stu-main { margin-left: 0; }
  .stu-sidebar-overlay.open { display: block; }
  .stu-topbar { padding: 0 1.15rem; }
  .stu-page-body { padding: 1.4rem 1.15rem; }
  .page-header.d-print-none { margin-bottom: 1.2rem; }
}
@media (max-width: 767.98px) {
  .stu-topbar { gap: .65rem; }
  .stu-topbar .topbar-title { font-size: .92rem; }
  .stu-topbar .topbar-right { gap: .35rem; }
  .stu-page-body { padding: 1.2rem .9rem; }
  .stu-main .card-header, .stu-main .card-body, .stu-main .card-footer { padding: 1rem; }
  .stu-main .table th, .stu-main .table td { padding: .75rem .8rem; }
  .stu-page-header h2 { font-size: 1.35rem; }
  .stu-stat-card { padding: 1rem; gap: .8rem; }
  .stu-stat-icon { width: 44px; height: 44px; border-radius: 13px; font-size: 1.2rem; }
  .stu-stat-value { font-size: 1.5rem; }
}
@media (max-width: 575.98px) {
  html { scroll-padding-top: 70px; }
  body.stu-layout { background-attachment: scroll; background-image: radial-gradient(circle at 1px 1px, rgba(32, 107, 196, .06) 1px, transparent 1.5px), radial-gradient(ellipse at 77% 0%, rgba(63, 139, 245, .075), transparent 35%), linear-gradient(145deg, #f5f8fd, #fbfdff 52%, #f0f5fc); background-size: 29px 29px, auto, auto; }
  body.stu-layout::before { top: -200px; right: -260px; opacity: .7; }
  .stu-topbar { height: 60px; padding: 0 .75rem; gap: .55rem; }
  .stu-topbar .topbar-title { font-size: .85rem; letter-spacing: -.025em; }
  .stu-sidebar-toggle { width: 36px; height: 36px; flex-basis: 36px; border-radius: 10px; }
  .stu-topbar-brand { gap: .3rem; }
  .stu-topbar-brand img { width: 27px; height: 27px; }
  .stu-topbar-brand strong { font-size: .92rem; }
  .stu-topbar-user { gap: .3rem; padding: .25rem; }
  .stu-topbar-user .u-avatar { width: 32px; height: 32px; border-radius: 10px; }
  .stu-topbar-user .bi-chevron-down { margin-left: 0 !important; }
  .stu-main { padding-top: 60px; }
  .stu-page-body { min-height: calc(100dvh - 60px); padding: 1rem .75rem; }
  .stu-main .page-title { font-size: 1.25rem; }
  .stu-page-header { margin-bottom: 1.15rem; gap: .6rem; }
  .stu-main .card { border-radius: 13px; }
  .stu-main .card-header { border-radius: 13px 13px 0 0; }
  .stu-stat-card { align-items: flex-start; padding: .9rem; border-radius: 13px; }
  .stu-stat-icon { width: 38px; height: 38px; border-radius: 11px; font-size: 1.05rem; }
  .stu-stat-value { font-size: 1.3rem; }
  .stu-stat-label { font-size: .7rem; }
  .stu-main .btn { max-width: 100%; }
  .stu-main .form-control, .stu-main .form-select { font-size: .88rem; }
  .stu-main .dropdown-menu { max-width: calc(100vw - 24px); }
}
@media (prefers-reduced-motion: reduce) {
  body.stu-layout::before { animation: none !important; }
}
@media print {
  .stu-sidebar, .stu-topbar, .stu-sidebar-overlay { display: none !important; }
  body.stu-layout { background: #ffffff !important; background-image: none !important; }
  body.stu-layout::before, body.stu-layout::after { display: none !important; }
  .stu-main { margin-left: 0 !important; padding-top: 0 !important; }
  .stu-page-body { padding: 0 !important; animation: none !important; }
  .stu-main .card { box-shadow: none !important; }
}
</style>

    <title><%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE</title>
    <%= pageContent.getExtractedProperties().getChild("head").getValue() %>

</head>
<body class="stu-layout">

<div class="stu-sidebar-overlay" id="stuSidebarOverlay" onclick="closeSidebar()"></div>

<aside class="stu-sidebar" id="stuSidebar">
    <a href="${ctx}/student/dashboard" class="stu-brand">
        <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png"
             alt="Logo HCMUTE" class="stu-brand-logo">
        <div class="stu-brand-text">
            <div class="t1">QAUTE</div>
            <div class="t2">Tư vấn Sinh viên HCMUTE</div>
        </div>
    </a>

    <a href="${ctx}/student/profile" class="stu-user-card">
        <c:choose><c:when test="${not empty studentAvatarUrl}"><img class="stu-user-avatar" src="<c:out value='${studentAvatarUrl}'/>" alt="Avatar" style="object-fit:cover;"/></c:when><c:otherwise><div class="stu-user-avatar"><c:out value="${avatarInitial}"/></div></c:otherwise></c:choose>
        <div class="overflow-hidden">
            <div class="stu-user-name text-truncate"><c:out value="${studentName}"/></div>
            <div class="stu-user-role">Sinh viên</div>
        </div>
    </a>

    <div class="stu-nav-label">Tổng quan</div>
    <ul class="stu-nav">
        <li>
            <a href="${ctx}/student/dashboard" class="${currentUri == '/student/dashboard' ? 'active' : ''}">
                <i class="bi bi-house-door-fill"></i>
                <span>Bảng điều khiển</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/" aria-label="Quay về trang chủ Guest">
                <i class="bi bi-globe2"></i>
                <span>Trang chủ</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/student/notifications" class="${fn:contains(currentUri, '/student/notifications') ? 'active' : ''}">
                <i class="bi bi-bell-fill"></i>
                <span>Thông báo</span>
                <span class="nav-badge d-none" id="sidebar-notif-badge">0</span>
            </a>
        </li>
    </ul>

    <div class="stu-nav-label">Yêu cầu & Hỗ trợ</div>
    <ul class="stu-nav">
        <li>
            <a href="${ctx}/student/tickets" class="${fn:contains(currentUri, '/student/tickets') and not fn:endsWith(currentUri, '/new') ? 'active' : ''}">
                <i class="bi bi-inbox-fill"></i>
                <span>Yêu cầu của tôi</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/student/tickets/new" class="${fn:endsWith(currentUri, '/student/tickets/new') ? 'active' : ''}">
                <i class="bi bi-plus-circle-fill"></i>
                <span>Tạo yêu cầu mới</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/student/chat" class="${fn:contains(currentUri, '/student/chat') ? 'active' : ''}">
                <i class="bi bi-chat-dots-fill"></i>
                <span>Chat tư vấn</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/student/appointments" class="${fn:contains(currentUri, '/student/appointments') and not fn:contains(currentUri, '/book') ? 'active' : ''}">
                <i class="bi bi-calendar-event-fill"></i>
                <span>Lịch hẹn</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/student/appointments/book" class="${fn:contains(currentUri, '/student/appointments/book') ? 'active' : ''}">
                <i class="bi bi-calendar-plus-fill"></i>
                <span>Đặt lịch hẹn</span>
            </a>
        </li>
    </ul>

    <div class="stu-nav-label">Kiến thức</div>
    <ul class="stu-nav">
        <li>
            <a href="${ctx}/student/faq" class="${fn:contains(currentUri, '/student/faq') and not fn:contains(currentUri, 'recent') ? 'active' : ''}">
                <i class="bi bi-question-circle-fill"></i>
                <span>Hỏi đáp FAQ</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/student/faq/recent" class="${fn:contains(currentUri, '/student/faq/recent') ? 'active' : ''}">
                <i class="bi bi-clock-history"></i>
                <span>FAQ đã xem</span>
            </a>
        </li>
    </ul>

    <div class="stu-nav-label">Tài khoản</div>
    <ul class="stu-nav">
        <li>
            <a href="${ctx}/student/profile" class="${fn:contains(currentUri, '/student/profile') ? 'active' : ''}">
                <i class="bi bi-person-fill"></i>
                <span>Hồ sơ cá nhân</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/student/security" class="${fn:contains(currentUri, '/student/security') ? 'active' : ''}">
                <i class="bi bi-shield-lock-fill"></i>
                <span>Bảo mật</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/student/notification-preferences" class="${fn:contains(currentUri, 'notification-preferences') ? 'active' : ''}">
                <i class="bi bi-gear-fill"></i>
                <span>Cài đặt thông báo</span>
            </a>
        </li>
    </ul>

    <div class="stu-sidebar-footer">
        <form method="post" action="${ctx}/auth/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">
                <i class="bi bi-box-arrow-left"></i>
                <span>Đăng xuất</span>
            </button>
        </form>
    </div>
</aside>

<header class="stu-topbar">
    <button class="stu-sidebar-toggle" id="stuSidebarToggle" type="button" onclick="toggleSidebar()"
            aria-expanded="true" aria-label="Thu gọn menu" title="Thu gọn menu">
        <i class="bi bi-list"></i>
    </button>
    <a href="${ctx}/student/dashboard" class="stu-topbar-brand d-md-none" aria-label="Trang tổng quan QAUTE">
        <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png" alt="Logo HCMUTE">
        <strong>QA<span>UTE</span></strong>
    </a>

    <div class="topbar-title d-none d-md-block">
        <c:choose>
            <c:when test="${currentUri == '/student/dashboard'}">Bảng điều khiển</c:when>
            <c:when test="${fn:contains(currentUri, '/student/tickets/new')}">Tạo yêu cầu mới</c:when>
            <c:when test="${fn:contains(currentUri, '/student/tickets')}">Yêu cầu của tôi</c:when>
            <c:when test="${fn:contains(currentUri, '/student/chat')}">Chat tư vấn</c:when>
            <c:when test="${fn:contains(currentUri, '/student/appointments/book')}">Đặt lịch hẹn</c:when>
            <c:when test="${fn:contains(currentUri, '/student/appointments')}">Lịch hẹn</c:when>
            <c:when test="${fn:contains(currentUri, '/student/faq/recent')}">FAQ đã xem</c:when>
            <c:when test="${fn:contains(currentUri, '/student/faq')}">Hỏi đáp FAQ</c:when>
            <c:when test="${fn:contains(currentUri, '/student/notifications')}">Thông báo</c:when>
            <c:when test="${fn:contains(currentUri, '/student/profile')}">Hồ sơ cá nhân</c:when>
            <c:when test="${fn:contains(currentUri, '/student/security')}">Bảo mật tài khoản</c:when>
            <c:otherwise>QAUTE</c:otherwise>
        </c:choose>
    </div>

    <div class="topbar-right ms-auto">
        <%@ include file="/WEB-INF/views/common/notification-bell.jspf" %>

        <div class="dropdown">
            <a href="#" class="stu-topbar-user" data-bs-toggle="dropdown" aria-expanded="false">
                <c:choose><c:when test="${not empty studentAvatarUrl}"><img class="u-avatar" src="<c:out value='${studentAvatarUrl}'/>" alt="Avatar" style="object-fit:cover;"/></c:when><c:otherwise><div class="u-avatar"><c:out value="${avatarInitial}"/></div></c:otherwise></c:choose>
                <div class="u-name d-none d-lg-block"><c:out value="${studentName}"/></div>
                <i class="bi bi-chevron-down ms-1 text-muted" style="font-size:.7rem;"></i>
            </a>
            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0" style="border-radius:12px;min-width:200px;">
                <li>
                    <div class="px-3 py-2 border-bottom">
                        <div class="fw-semibold" style="font-size:.875rem;"><c:out value="${studentName}"/></div>
                        <div class="text-muted" style="font-size:.78rem;"><c:out value="${studentEmail}"/></div>
                    </div>
                </li>
                <li><a class="dropdown-item" href="${ctx}/student/profile"><i class="bi bi-person me-2 text-muted"></i>Hồ sơ cá nhân</a></li>
                <li><a class="dropdown-item" href="${ctx}/student/security"><i class="bi bi-shield-lock me-2 text-muted"></i>Bảo mật</a></li>
                <li><a class="dropdown-item" href="${ctx}/student/notification-preferences"><i class="bi bi-gear me-2 text-muted"></i>Cài đặt thông báo</a></li>
                <li><hr class="dropdown-divider"></li>
                <li>
                    <form method="post" action="${ctx}/auth/logout" class="px-1">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button class="dropdown-item text-danger" type="submit" style="border-radius:8px;">
                            <i class="bi bi-box-arrow-left me-2"></i>Đăng xuất
                        </button>
                    </form>
                </li>
            </ul>
        </div>
    </div>
</header>

<main class="stu-main">
    <div class="stu-page-body">
        <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
<%@ include file="/WEB-INF/views/common/ws-scripts.jspf" %>

<script>
    function toggleSidebar() {
        var toggle = document.getElementById('stuSidebarToggle');
        if (window.innerWidth >= 992) {
            document.body.classList.toggle('stu-sidebar-collapsed');
            var desktopCollapsed = document.body.classList.contains('stu-sidebar-collapsed');
            toggle.setAttribute('aria-expanded', String(!desktopCollapsed));
            toggle.setAttribute('aria-label', desktopCollapsed ? 'Mở rộng menu' : 'Thu gọn menu');
            toggle.setAttribute('title', desktopCollapsed ? 'Mở rộng menu' : 'Thu gọn menu');
            toggle.innerHTML = '<i class="bi ' + (desktopCollapsed ? 'bi-layout-sidebar-inset' : 'bi-layout-sidebar') + '"></i>';
            return;
        }
        document.getElementById('stuSidebar').classList.toggle('open');
        document.getElementById('stuSidebarOverlay').classList.toggle('open');
        var mobileOpen = document.getElementById('stuSidebar').classList.contains('open');
        toggle.setAttribute('aria-expanded', String(mobileOpen));
        toggle.setAttribute('aria-label', mobileOpen ? 'Đóng menu' : 'Mở menu');
        toggle.setAttribute('title', mobileOpen ? 'Đóng menu' : 'Mở menu');
        toggle.innerHTML = '<i class="bi ' + (mobileOpen ? 'bi-x-lg' : 'bi-list') + '"></i>';
    }
    function closeSidebar() {
        document.getElementById('stuSidebar').classList.remove('open');
        document.getElementById('stuSidebarOverlay').classList.remove('open');
    }

    (function syncBadge() {
        const topBadge = document.getElementById('notification-badge');
        const sideBadge = document.getElementById('sidebar-notif-badge');
        if (!topBadge || !sideBadge) return;
        const obs = new MutationObserver(() => {
            const hidden = topBadge.classList.contains('d-none');
            const count  = topBadge.textContent.trim();
            if (!hidden && count && count !== '0') {
                sideBadge.textContent = count;
                sideBadge.classList.remove('d-none');
            } else {
                sideBadge.classList.add('d-none');
            }
        });
        obs.observe(topBadge, { attributes: true, childList: true, subtree: true, characterData: true });
    })();

    document.addEventListener('DOMContentLoaded', function () {
        document.querySelectorAll('.stu-nav a').forEach(function (link) {
            link.addEventListener('click', function () {
                closeSidebar();
            });
        });
    });
</script>

<c:if test="${not fn:contains(currentUri, '/student/chat')}">
    <%@ include file="/WEB-INF/views/common/chatbot-widget.jspf" %>
</c:if>
</body>
</html>