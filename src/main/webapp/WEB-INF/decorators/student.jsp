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

<%-- Lấy thông tin người dùng --%>
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
<%-- Tạo chữ viết tắt avatar --%>
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
    <title><%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE</title>
    <%= pageContent.getExtractedProperties().getChild("head").getValue() %>
    <style>
        /* ================================================================
           STUDENT LAYOUT — HCMUTE Theme (Red & White)
           ================================================================ */
        :root {
            --stu-red:        #206bc4;
            --stu-red-dark:   #154c91;
            --stu-red-light:  #eef6ff;
            --stu-sidebar-bg: #0b1f3a;
            --stu-sidebar-w:  260px;
            --stu-topbar-h:   60px;
            --stu-text-muted: #8a9bb5;
        }

        /* ---- Reset & base ---- */
        * { box-sizing: border-box; }
        body.stu-layout {
            margin: 0;
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
            background: #f0f2f5;
            color: #2d3748;
        }

        /* ---- Sidebar ---- */
        .stu-sidebar {
            position: fixed;
            top: 0; left: 0;
            width: var(--stu-sidebar-w);
            height: 100vh;
            background: var(--stu-sidebar-bg);
            display: flex;
            flex-direction: column;
            z-index: 1040;
            transition: transform .3s ease;
            overflow-y: auto;
            overflow-x: hidden;
        }
        .stu-sidebar::-webkit-scrollbar { width: 4px; }
        .stu-sidebar::-webkit-scrollbar-thumb { background: rgba(255,255,255,.15); border-radius: 2px; }

        /* Brand */
        .stu-brand {
            display: flex;
            align-items: center;
            gap: .65rem;
            padding: 1.1rem 1.25rem 1rem;
            border-bottom: 1px solid rgba(255,255,255,.07);
            text-decoration: none;
            flex-shrink: 0;
        }
        .stu-brand-icon {
            width: 38px; height: 38px;
            background: linear-gradient(135deg, var(--stu-red), #e74c3c);
            border-radius: 10px;
            display: flex; align-items: center; justify-content: center;
            color: #fff; font-weight: 800; font-size: 1rem;
            flex-shrink: 0;
        }
        .stu-brand-logo {
            width: 38px;
            height: 38px;
            object-fit: contain;
            background: #fff;
            border-radius: 10px;
            padding: 3px;
            flex-shrink: 0;
        }
        .stu-brand-text .t1 { color: #fff; font-weight: 800; font-size: 1rem; line-height: 1; }
        .stu-brand-text .t2 { color: var(--stu-text-muted); font-size: .62rem; line-height: 1.3; }

        /* User card inside sidebar */
        .stu-user-card {
            display: flex;
            align-items: center;
            gap: .75rem;
            padding: .9rem 1.25rem;
            margin: .75rem .75rem .25rem;
            background: rgba(255,255,255,.06);
            border-radius: 12px;
            text-decoration: none;
            transition: background .2s;
        }
        .stu-user-card:hover { background: rgba(255,255,255,.1); }
        .stu-user-avatar {
            width: 38px; height: 38px;
            background: linear-gradient(135deg, var(--stu-red), #e74c3c);
            border-radius: 50%;
            display: flex; align-items: center; justify-content: center;
            color: #fff; font-weight: 700; font-size: .85rem;
            flex-shrink: 0;
        }
        .stu-user-name { color: #fff; font-weight: 600; font-size: .85rem; line-height: 1.2; }
        .stu-user-role { color: var(--stu-text-muted); font-size: .7rem; }

        /* Nav section label */
        .stu-nav-label {
            color: var(--stu-text-muted);
            font-size: .65rem;
            font-weight: 700;
            letter-spacing: .1em;
            text-transform: uppercase;
            padding: 1.1rem 1.25rem .35rem;
        }

        /* Nav items */
        .stu-nav { list-style: none; padding: 0 .75rem; margin: 0; }
        .stu-nav li { margin-bottom: 2px; }
        .stu-nav a {
            display: flex;
            align-items: center;
            gap: .7rem;
            padding: .6rem .85rem;
            border-radius: 10px;
            color: rgba(255,255,255,.65);
            font-size: .875rem;
            font-weight: 500;
            text-decoration: none;
            transition: background .18s, color .18s;
        }
        .stu-nav a i { font-size: 1.05rem; width: 20px; text-align: center; flex-shrink: 0; }
        .stu-nav a:hover {
            background: rgba(255,255,255,.08);
            color: #fff;
        }
        .stu-nav a.active {
            background: var(--stu-red);
            color: #fff;
            font-weight: 600;
        }
        .stu-nav a .nav-badge {
            margin-left: auto;
            background: var(--stu-red);
            color: #fff;
            font-size: .65rem;
            padding: .15em .55em;
            border-radius: 20px;
            font-weight: 700;
            min-width: 18px;
            text-align: center;
        }
        .stu-nav a.active .nav-badge {
            background: rgba(255,255,255,.3);
        }

        /* Sidebar footer */
        .stu-sidebar-footer {
            margin-top: auto;
            padding: .75rem;
            border-top: 1px solid rgba(255,255,255,.07);
        }
        .stu-sidebar-footer form button {
            display: flex;
            align-items: center;
            gap: .6rem;
            width: 100%;
            padding: .55rem .85rem;
            border-radius: 10px;
            background: rgba(255,255,255,.05);
            border: none;
            color: rgba(255,255,255,.55);
            font-size: .875rem;
            cursor: pointer;
            transition: background .18s, color .18s;
        }
        .stu-sidebar-footer form button:hover {
            background: rgba(192,57,43,.3);
            color: #fff;
        }

        /* ---- Topbar ---- */
        .stu-topbar {
            position: fixed;
            top: 0;
            left: var(--stu-sidebar-w);
            right: 0;
            height: var(--stu-topbar-h);
            background: #fff;
            border-bottom: 1px solid #eaecf0;
            display: flex;
            align-items: center;
            padding: 0 1.5rem;
            z-index: 1030;
            gap: 1rem;
            box-shadow: 0 1px 4px rgba(0,0,0,.05);
            transition: left .3s ease;
        }
        .stu-topbar .topbar-title {
            font-weight: 700;
            font-size: 1rem;
            color: #1a202c;
            flex: 1;
        }
        .stu-topbar .topbar-right {
            display: flex;
            align-items: center;
            gap: .5rem;
        }
        /* Nút thu gọn menu */
        .stu-sidebar-toggle {
            display: inline-flex !important;
            align-items: center;
            justify-content: center;
            width: 38px;
            height: 38px;
            background: #206bc4;
            border: 1px solid #154c91;
            font-size: 1.2rem;
            color: #fff;
            cursor: pointer;
            padding: 0;
            border-radius: 9px;
            flex: 0 0 38px;
            box-shadow: 0 2px 6px rgba(32,107,196,.25);
            transition: background .18s, transform .18s, box-shadow .18s;
        }
        .stu-sidebar-toggle:hover {
            background: #154c91;
            box-shadow: 0 4px 10px rgba(32,107,196,.35);
            transform: translateY(-1px);
        }
        .stu-sidebar-toggle:focus-visible {
            outline: 3px solid rgba(32,107,196,.3);
            outline-offset: 2px;
        }
        @media (min-width: 992px) {
            body.stu-sidebar-collapsed .stu-sidebar {
                width: 76px;
            }
            body.stu-sidebar-collapsed .stu-sidebar .stu-brand {
                justify-content: center;
                padding-left: .75rem;
                padding-right: .75rem;
            }
            body.stu-sidebar-collapsed .stu-brand-text,
            body.stu-sidebar-collapsed .stu-user-card > div:last-child,
            body.stu-sidebar-collapsed .stu-nav-label,
            body.stu-sidebar-collapsed .stu-nav a span,
            body.stu-sidebar-collapsed .stu-sidebar-footer span {
                display: none;
            }
            body.stu-sidebar-collapsed .stu-user-card {
                justify-content: center;
                margin-left: .5rem;
                margin-right: .5rem;
                padding-left: .5rem;
                padding-right: .5rem;
            }
            body.stu-sidebar-collapsed .stu-nav {
                padding-left: .5rem;
                padding-right: .5rem;
            }
            body.stu-sidebar-collapsed .stu-nav a {
                justify-content: center;
                padding-left: .6rem;
                padding-right: .6rem;
            }
            body.stu-sidebar-collapsed .stu-sidebar-footer form button {
                justify-content: center;
            }
            body.stu-sidebar-collapsed .stu-topbar {
                left: 76px;
            }
            body.stu-sidebar-collapsed .stu-main {
                margin-left: 76px;
            }
        }

        /* User button in topbar */
        .stu-topbar-user {
            display: flex;
            align-items: center;
            gap: .5rem;
            cursor: pointer;
            padding: .3rem .6rem;
            border-radius: 10px;
            text-decoration: none;
            color: inherit;
            transition: background .18s;
        }
        .stu-topbar-user:hover { background: #f3f4f6; }
        .stu-topbar-user .u-avatar {
            width: 34px; height: 34px;
            background: linear-gradient(135deg, var(--stu-red), #e74c3c);
            border-radius: 50%;
            display: flex; align-items: center; justify-content: center;
            color: #fff; font-weight: 700; font-size: .8rem;
        }
        .stu-topbar-user .u-name {
            font-size: .85rem;
            font-weight: 600;
            color: #1a202c;
            max-width: 160px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .stu-topbar-brand {
            display: inline-flex;
            align-items: center;
            gap: .4rem;
            color: #1f2937;
            text-decoration: none;
        }
        .stu-topbar-brand img {
            width: 30px;
            height: 30px;
            object-fit: contain;
            background: #fff;
            border-radius: 7px;
            padding: 2px;
        }
        .stu-topbar-brand span { color: var(--stu-red); }

        /* ---- Page wrapper ---- */
        .stu-main {
            margin-left: var(--stu-sidebar-w);
            padding-top: var(--stu-topbar-h);
            min-height: 100vh;
            transition: margin-left .3s ease;
        }
        .stu-page-body {
            padding: 1.75rem 1.5rem;
        }

        /* ---- Page header (used inside views) ---- */
        .stu-page-header {
            display: flex;
            align-items: flex-start;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: .75rem;
            margin-bottom: 1.5rem;
        }
        .stu-page-header .page-pretitle {
            font-size: .7rem;
            color: #9ca3af;
            text-transform: uppercase;
            font-weight: 600;
            letter-spacing: .05em;
            margin-bottom: .2rem;
        }
        .stu-page-header h2 { margin: 0; font-size: 1.4rem; font-weight: 700; }

        /* Override Tabler page-header inside body (views use tabler classes) */
        .page-header.d-print-none { padding: 0 !important; border: none !important; }
        .page-header.d-print-none .container-xl { padding: 0 !important; max-width: 100% !important; }
        .page-body { padding: 0 !important; }
        .page-body .container-xl { padding: 0 !important; max-width: 100% !important; }

        /* ---- Cards ---- */
        .card {
            border: 1px solid #eaecf0;
            border-radius: 14px;
            box-shadow: 0 1px 4px rgba(0,0,0,.05);
        }
        .card-header { border-bottom: 1px solid #eaecf0; background: transparent; }

        /* ---- Stats cards ---- */
        .stu-stat-card {
            background: #fff;
            border-radius: 14px;
            padding: 1.2rem 1.4rem;
            border: 1px solid #eaecf0;
            box-shadow: 0 1px 4px rgba(0,0,0,.05);
            display: flex;
            align-items: center;
            gap: 1rem;
            text-decoration: none;
            color: inherit;
            transition: transform .2s, box-shadow .2s;
        }
        .stu-stat-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0,0,0,.09);
            color: inherit;
        }
        .stu-stat-icon {
            width: 50px; height: 50px;
            border-radius: 12px;
            display: flex; align-items: center; justify-content: center;
            font-size: 1.35rem;
            flex-shrink: 0;
        }
        .stu-stat-value { font-size: 1.75rem; font-weight: 800; line-height: 1; }
        .stu-stat-label { font-size: .78rem; color: #6b7280; margin-top: .15rem; }

        /* ---- Buttons ---- */
        .btn-stu-primary {
            background: var(--stu-red);
            color: #fff;
            border: none;
            border-radius: 10px;
            padding: .5rem 1.1rem;
            font-weight: 600;
            font-size: .875rem;
            transition: background .18s, transform .15s;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: .4rem;
            cursor: pointer;
        }
        .btn-stu-primary:hover { background: var(--stu-red-dark); color: #fff; transform: translateY(-1px); }
        .btn-stu-outline {
            background: transparent;
            color: var(--stu-red);
            border: 1.5px solid var(--stu-red);
            border-radius: 10px;
            padding: .5rem 1.1rem;
            font-weight: 600;
            font-size: .875rem;
            transition: all .18s;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: .4rem;
            cursor: pointer;
        }
        .btn-stu-outline:hover { background: var(--stu-red); color: #fff; }

        /* Keep Bootstrap primary pointing to red */
        .btn-primary { background: var(--stu-red) !important; border-color: var(--stu-red) !important; }
        .btn-primary:hover { background: var(--stu-red-dark) !important; border-color: var(--stu-red-dark) !important; }
        .btn-outline-primary { color: var(--stu-red) !important; border-color: var(--stu-red) !important; }
        .btn-outline-primary:hover { background: var(--stu-red) !important; color: #fff !important; }
        a.text-primary, .text-primary { color: var(--stu-red) !important; }
        .bg-primary { background: var(--stu-red) !important; }
        .border-primary { border-color: var(--stu-red) !important; }

        /* Teal button (chat) */
        .btn-teal {
            background: #0d9488;
            color: #fff;
            border: none;
        }
        .btn-teal:hover { background: #0f766e; color: #fff; }

        /* ---- Badges ---- */
        .badge.bg-primary { background: var(--stu-red) !important; }

        /* ---- Nav tabs ---- */
        .nav-tabs .nav-link.active {
            border-bottom-color: var(--stu-red);
            color: var(--stu-red);
        }
        .nav-tabs .nav-link:hover { color: var(--stu-red); }

        /* ---- Responsive: mobile ---- */
        @media (max-width: 991.98px) {
            .stu-sidebar {
                transform: translateX(-100%);
            }
            .stu-sidebar.open {
                transform: translateX(0);
            }
            .stu-topbar {
                left: 0;
            }
            .stu-main {
                margin-left: 0;
            }
            .stu-sidebar-overlay {
                display: none;
                position: fixed;
                inset: 0;
                background: rgba(0,0,0,.45);
                z-index: 1039;
            }
            .stu-sidebar-overlay.open {
                display: block;
            }
        }

        /* ---- Notification bell tweak ---- */
        #notification-bell-dropdown .nav-link { color: #374151; }
        #notification-bell-dropdown .nav-link:hover { color: var(--stu-red); }

        /* ---- Alert override ---- */
        .alert-important.alert-warning {
            background: #fffbeb;
            border-color: #fde68a;
            color: #92400e;
        }
        .alert-info {
            background: #eff6ff;
            border-color: #bfdbfe;
            color: #1e40af;
        }

        /* ---- Table ---- */
        .table-responsive-card { overflow-x: auto; }
        .table th { font-size: .78rem; text-transform: uppercase; letter-spacing: .04em; color: #6b7280; font-weight: 600; }

        /* ---- Breadcrumb ---- */
        .breadcrumb { font-size: .82rem; }

        /* ---- Print ---- */
        @media print {
            .stu-sidebar, .stu-topbar { display: none !important; }
            .stu-main { margin-left: 0 !important; padding-top: 0 !important; }
        }
    </style>
</head>
<body class="stu-layout">

<%-- Overlay for mobile sidebar --%>
<div class="stu-sidebar-overlay" id="stuSidebarOverlay" onclick="closeSidebar()"></div>

<%-- ===== SIDEBAR ===== --%>
<aside class="stu-sidebar" id="stuSidebar">

    <%-- Brand --%>
    <a href="${ctx}/student/dashboard" class="stu-brand">
        <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png"
             alt="Logo HCMUTE" class="stu-brand-logo">
        <div class="stu-brand-text">
            <div class="t1">QAUTE</div>
            <div class="t2">Tư vấn Sinh viên HCMUTE</div>
        </div>
    </a>

    <%-- User card --%>
    <a href="${ctx}/student/profile" class="stu-user-card">
        <div class="stu-user-avatar"><c:out value="${avatarInitial}"/></div>
        <div class="overflow-hidden">
            <div class="stu-user-name text-truncate"><c:out value="${studentName}"/></div>
            <div class="stu-user-role">Sinh viên</div>
        </div>
    </a>

    <%-- Nav: Tổng quan --%>
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

    <%-- Nav: Yêu cầu & Hỗ trợ --%>
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

    <%-- Nav: Kiến thức --%>
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

    <%-- Nav: Tài khoản --%>
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

    <%-- Sidebar footer: Đăng xuất --%>
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

<%-- ===== TOPBAR ===== --%>
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
        <%-- Notification bell --%>
        <%@ include file="/WEB-INF/views/common/notification-bell.jspf" %>

        <%-- User dropdown --%>
        <div class="dropdown">
            <a href="#" class="stu-topbar-user" data-bs-toggle="dropdown" aria-expanded="false">
                <div class="u-avatar"><c:out value="${avatarInitial}"/></div>
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

<%-- ===== MAIN CONTENT ===== --%>
<main class="stu-main">
    <div class="stu-page-body">
        <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
<%@ include file="/WEB-INF/views/common/ws-scripts.jspf" %>

<script>
    // ---- Sidebar toggle (mobile) ----
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

    // ---- Sync notification badge to sidebar ----
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

    // ---- Highlight active nav link in topbar title ----
    document.addEventListener('DOMContentLoaded', function () {
        // Auto-close mobile sidebar on link click
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
