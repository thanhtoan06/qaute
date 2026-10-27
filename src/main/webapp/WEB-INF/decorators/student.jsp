<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page import="org.sitemesh.content.Content" %>
<%-- Lấy nội dung trang hiện tại để hiển thị trong giao diện sinh viên.
--%><%
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layouts/student.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/role-shell.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/chatbot-widget.css">

    <title><%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE</title>
    <%= pageContent.getExtractedProperties().getChild("head").getValue() %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/inline-utilities.css">

</head>
<body class="stu-layout">

<%-- Lớp phủ đóng menu bên khi người dùng chạm ra ngoài trên thiết bị di động. --%>
<div class="stu-sidebar-overlay" id="stuSidebarOverlay" data-sidebar-action="close"></div>

<%-- Thanh bên hiển thị hồ sơ sinh viên và các nhóm chức năng điều hướng. --%>
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
        <c:choose><c:when test="${not empty studentAvatarUrl}"><img class="stu-user-avatar u-object-fit-cover" src="<c:out value='${studentAvatarUrl}'/>" alt="Avatar"/></c:when><c:otherwise><div class="stu-user-avatar"><c:out value="${avatarInitial}"/></div></c:otherwise></c:choose>
        <div class="overflow-hidden">
            <div class="stu-user-name text-truncate"><c:out value="${studentName}"/></div>
            <div class="stu-user-role">Sinh viên</div>
        </div>
    </a>

    <%-- Lối tắt đến tổng quan, trang chủ và thông báo. --%>
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
                <span class="nav-badge qa-sidebar-notification-badge d-none"
                    id="sidebar-notif-badge" data-sidebar-notification-badge>0</span>
            </a>
        </li>
    </ul>

    <%-- Các chức năng gửi yêu cầu, chat và đặt lịch tư vấn. --%>
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
            <a href="${ctx}/student/chat" class="${fn:contains(currentUri, '/student/chat') and param.view ne 'history' ? 'active' : ''}">
                <i class="bi bi-chat-dots-fill"></i>
                <span>Tư vấn trực tuyến</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/student/chat?view=history" class="${fn:contains(currentUri, '/student/chat') and param.view == 'history' ? 'active' : ''}">
                <i class="bi bi-clock-history"></i>
                <span>Lịch sử tư vấn trực tuyến</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/student/appointments" class="${fn:contains(currentUri, '/student/appointments') and not fn:contains(currentUri, '/book') ? 'active' : ''}">
                <i class="bi bi-calendar-check"></i>
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

    <%-- Tra cứu FAQ và xem lại các câu hỏi đã truy cập. --%>
    <div class="stu-nav-label">Kiến thức</div>
    <ul class="stu-nav">
        <li>
            <a href="${ctx}/student/faq" class="${fn:contains(currentUri, '/student/faq') and not fn:contains(currentUri, 'recent') ? 'active' : ''}">
                <i class="bi bi-question-circle-fill"></i>
                <span>Tra cứu FAQ</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/student/faq/recent" class="${fn:contains(currentUri, '/student/faq/recent') ? 'active' : ''}">
                <i class="bi bi-clock-history"></i>
                <span>FAQ đã xem</span>
            </a>
        </li>
    </ul>

    <%-- Các trang hồ sơ, bảo mật và tùy chọn thông báo. --%>
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

    <%-- Gửi biểu mẫu đăng xuất có kèm token CSRF. --%>
    <div class="stu-sidebar-footer">
        <div class="qa-sidebar-workspace-status">
            <span class="qa-workspace-role-dot"></span>
            <span class="qa-sidebar-workspace-copy">Không gian sinh viên</span>
        </div>
        <form method="post" action="${ctx}/auth/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">
                <i class="bi bi-box-arrow-left"></i>
                <span>Đăng xuất</span>
            </button>
        </form>
    </div>
</aside>

<%-- Thanh công cụ có nút mở menu, tên trang và menu tài khoản sinh viên. --%>
<header class="stu-topbar">
    <button class="stu-sidebar-toggle" id="stuSidebarToggle" type="button" data-sidebar-action="toggle"
            aria-expanded="true" aria-label="Thu gọn menu" title="Thu gọn menu">
        <i class="bi bi-list"></i>
    </button>
    <a href="${ctx}/student/dashboard" class="stu-topbar-brand d-md-none" aria-label="Trang tổng quan QAUTE">
        <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png" alt="Logo HCMUTE">
        <strong>QA<span>UTE</span></strong>
    </a>

    <div class="topbar-title qa-workspace-location d-none d-md-flex">
        <small>KHÔNG GIAN SINH VIÊN</small>
        <strong>
            <c:choose>
                <c:when test="${currentUri == '/student/dashboard'}">Bảng điều khiển</c:when>
                <c:when test="${fn:contains(currentUri, '/student/tickets/new')}">Tạo yêu cầu mới</c:when>
                <c:when test="${fn:contains(currentUri, '/student/tickets')}">Yêu cầu của tôi</c:when>
                <c:when test="${fn:contains(currentUri, '/student/chat') and param.view == 'history'}">Lịch sử tư vấn trực tuyến</c:when>
                <c:when test="${fn:contains(currentUri, '/student/chat')}">Tư vấn trực tuyến</c:when>
                <c:when test="${fn:contains(currentUri, '/student/appointments/book')}">Đặt lịch hẹn</c:when>
                <c:when test="${fn:contains(currentUri, '/student/appointments')}">Lịch hẹn</c:when>
                <c:when test="${fn:contains(currentUri, '/student/faq/recent')}">FAQ đã xem</c:when>
                <c:when test="${fn:contains(currentUri, '/student/faq')}">Tra cứu FAQ</c:when>
                <c:when test="${fn:contains(currentUri, '/student/notifications')}">Thông báo</c:when>
                <c:when test="${fn:contains(currentUri, '/student/profile')}">Hồ sơ cá nhân</c:when>
                <c:when test="${fn:contains(currentUri, '/student/security')}">Bảo mật tài khoản</c:when>
                <c:otherwise>QAUTE</c:otherwise>
            </c:choose>
        </strong>
    </div>

    <%-- Menu hồ sơ, bảo mật, cài đặt thông báo và đăng xuất. --%>
    <div class="topbar-right ms-auto">
        <%@ include file="/WEB-INF/views/common/notification-bell.jspf" %>

        <div class="dropdown">
            <a href="#" class="stu-topbar-user" data-bs-toggle="dropdown" aria-expanded="false">
                <c:choose><c:when test="${not empty studentAvatarUrl}"><img class="u-avatar u-object-fit-cover" src="<c:out value='${studentAvatarUrl}'/>" alt="Avatar"/></c:when><c:otherwise><div class="u-avatar"><c:out value="${avatarInitial}"/></div></c:otherwise></c:choose>
                <div class="d-none d-lg-block text-start">
                    <div class="u-name"><c:out value="${studentName}"/></div>
                    <div class="u-role">Sinh viên</div>
                </div>
                <i class="bi bi-chevron-down ms-1 text-muted u-text-size-7rem"></i>
            </a>
            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 u-rounded-12px u-min-w-200px">
                <li>
                    <div class="px-3 py-2 border-bottom">
                        <div class="fw-semibold u-text-size-875rem"><c:out value="${studentName}"/></div>
                        <div class="text-muted u-text-size-78rem"><c:out value="${studentEmail}"/></div>
                    </div>
                </li>
                <li><a class="dropdown-item" href="${ctx}/student/profile"><i class="bi bi-person me-2 text-muted"></i>Hồ sơ cá nhân</a></li>
                <li><a class="dropdown-item" href="${ctx}/student/security"><i class="bi bi-shield-lock me-2 text-muted"></i>Bảo mật</a></li>
                <li><a class="dropdown-item" href="${ctx}/student/notification-preferences"><i class="bi bi-gear me-2 text-muted"></i>Cài đặt thông báo</a></li>
                <li><hr class="dropdown-divider"></li>
                <li>
                    <form method="post" action="${ctx}/auth/logout" class="px-1">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button class="dropdown-item text-danger u-rounded-8px" type="submit">
                            <i class="bi bi-box-arrow-left me-2"></i>Đăng xuất
                        </button>
                    </form>
                </li>
            </ul>
        </div>
    </div>
</header>

<%-- Hiển thị nội dung của trang sinh viên hiện tại. --%>
<main class="stu-main">
    <div class="stu-page-body">
        <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
    </div>
    <footer class="stu-footer">
        <span>&copy; QAUTE · Cổng hỗ trợ sinh viên</span>
    </footer>
</main>

<script src="${pageContext.request.contextPath}/assets/js/layouts/student-1.js"></script>

<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
<%@ include file="/WEB-INF/views/common/ws-scripts.jspf" %>

<%-- Quản lý trạng thái thanh bên và đồng bộ huy hiệu thông báo giữa các vị trí. --%>
<script src="${pageContext.request.contextPath}/assets/js/layouts/student-2.js"></script>

<c:if test="${not fn:contains(currentUri, '/student/chat')}">
    <%@ include file="/WEB-INF/views/common/chatbot-widget.jspf" %>
</c:if>
</body>
</html>