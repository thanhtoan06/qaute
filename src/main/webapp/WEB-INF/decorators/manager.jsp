<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page import="org.sitemesh.content.Content" %>
<%-- Lấy nội dung trang hiện tại để hiển thị trong giao diện quản lý.
--%><%
    Content pageContent = (Content) request.getAttribute("org.sitemesh.content.Content");
%>
<%-- Chuẩn hóa đường dẫn hiện tại để xác định mục menu đang hoạt động. --%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="notificationsUrl" value="${ctx}/manager/notifications"/>
<%@ include file="/WEB-INF/views/common/workspace-user.jspf" %>

<c:set var="rawUri" value="${not empty requestScope['jakarta.servlet.forward.request_uri'] ? requestScope['jakarta.servlet.forward.request_uri'] : pageContext.request.requestURI}"/>
<c:set var="currentPath" value="${fn:startsWith(rawUri, ctx) ? fn:substring(rawUri, fn:length(ctx), fn:length(rawUri)) : rawUri}"/>
<c:set var="navPath" value="${fn:startsWith(currentPath, '/manager/tickets') ? '/manager/queue' : currentPath}"/>

<c:set var="menuPaths" value="/manager/dashboard,/manager/queue,/manager/chat,/manager/chat-history,/manager/schedule,/manager/appointments,/manager/students,/manager/canned-responses,/manager/faq,/manager/reports,/manager/profile,/manager/profile/security,/manager/notifications,/manager/notifications/preferences"/>
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
    <%-- Nạp tài nguyên dùng chung và stylesheet riêng cho không gian tư vấn viên. --%>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <link rel="stylesheet" href="${ctx}/assets/css/workspace.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layouts/manager.css">
<link rel="stylesheet" href="${ctx}/assets/css/role-shell.css">

    <title><%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE</title>
    <%= pageContent.getExtractedProperties().getChild("head").getValue() %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/inline-utilities.css">

</head>
<body class="qa-workspace qa-workspace--manager">
<a class="qa-workspace-skip" href="#workspace-main">Chuyển đến nội dung</a>
<button class="qa-workspace-overlay" type="button" data-workspace-close hidden aria-label="Đóng menu điều hướng"></button>
<div class="page">

    <%-- Thanh bên chứa nhận diện tài khoản và điều hướng các chức năng tư vấn viên. --%>
    <aside class="qa-workspace-sidebar" id="workspace-sidebar" data-bs-theme="dark" aria-label="Điều hướng tư vấn viên">
        <a href="${ctx}/manager/dashboard" class="stu-brand">
            <img src="${ctx}/assets/images/hcmute-logo.png" alt="Logo HCMUTE" class="stu-brand-logo">
            <div class="stu-brand-text">
                <div class="t1">QAUTE</div>
                <div class="t2">CỔNG HỖ TRỢ SINH VIÊN</div>
            </div>
        </a>

        <a href="${ctx}/manager/profile" class="stu-user-card">
            <c:choose>
                <c:when test="${not empty workspaceAvatarUrl}">
                    <img class="stu-user-avatar u-object-fit-cover" src="<c:out value='${workspaceAvatarUrl}'/>" alt="Ảnh đại diện"/>
                </c:when>
                <c:otherwise><div class="stu-user-avatar"><c:out value="${workspaceInitial}"/></div></c:otherwise>
            </c:choose>
            <div class="overflow-hidden">
                <div class="stu-user-name text-truncate"><c:out value="${workspaceName}"/></div>
                <div class="stu-user-role">Tư vấn viên</div>
            </div>
        </a>

        <%-- Các liên kết được nhóm giống menu sinh viên, không bỏ chức năng hiện có. --%>
        <div class="stu-nav-label">Tổng quan</div>
        <ul class="stu-nav">
            <li class="${activePath == '/manager/dashboard' ? 'active' : ''}">
                <a href="${ctx}/manager/dashboard">
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
            <li class="${activePath == '/manager/notifications' ? 'active' : ''}">
                <a href="${ctx}/manager/notifications">
                    <i class="bi bi-bell-fill"></i>
                    <span>Thông báo</span>
                    <span class="nav-badge qa-sidebar-notification-badge d-none"
                        data-sidebar-notification-badge>0</span>
                </a>
            </li>
        </ul>

        <div class="stu-nav-label">Yêu cầu & Hỗ trợ</div>
        <ul class="stu-nav">
            <li class="${activePath == '/manager/queue' ? 'active' : ''}">
                <a href="${ctx}/manager/queue">
                    <i class="bi bi-inbox-fill"></i>
                    <span>Hàng đợi yêu cầu</span>
                </a>
            </li>
            <li class="${activePath == '/manager/chat' ? 'active' : ''}">
                <a href="${ctx}/manager/chat">
                    <i class="bi bi-chat-dots-fill"></i>
                    <span>Tư vấn trực tuyến</span>
                </a>
            </li>
            <li class="${activePath == '/manager/chat-history' ? 'active' : ''}">
                <a href="${ctx}/manager/chat-history">
                    <i class="bi bi-clock-history"></i>
                    <span>Lịch sử tư vấn trực tuyến</span>
                </a>
            </li>
            <li class="${activePath == '/manager/schedule' ? 'active' : ''}">
                <a href="${ctx}/manager/schedule">
                    <i class="bi bi-calendar-week"></i>
                    <span>Lịch làm việc</span>
                </a>
            </li>
            <li class="${activePath == '/manager/appointments' ? 'active' : ''}">
                <a href="${ctx}/manager/appointments">
                    <i class="bi bi-calendar-check"></i>
                    <span>Lịch hẹn</span>
                </a>
            </li>
            <li class="${activePath == '/manager/students' ? 'active' : ''}">
                <a href="${ctx}/manager/students">
                    <i class="bi bi-people"></i>
                    <span>Sinh viên</span>
                </a>
            </li>
            <li class="${activePath == '/manager/reports' ? 'active' : ''}">
                <a href="${ctx}/manager/reports">
                    <i class="bi bi-bar-chart-line"></i>
                    <span>Báo cáo</span>
                </a>
            </li>
        </ul>

        <div class="stu-nav-label">Kiến thức</div>
        <ul class="stu-nav">
            <li class="${activePath == '/manager/canned-responses' ? 'active' : ''}">
                <a href="${ctx}/manager/canned-responses">
                    <i class="bi bi-chat-left-text"></i>
                    <span>Mẫu trả lời</span>
                </a>
            </li>
            <li class="${activePath == '/manager/faq' ? 'active' : ''}">
                <a href="${ctx}/manager/faq">
                    <i class="bi bi-question-circle-fill"></i>
                    <span>FAQ đề xuất</span>
                </a>
            </li>
        </ul>

        <div class="stu-nav-label">Tài khoản</div>
        <ul class="stu-nav">
            <li class="${activePath == '/manager/profile' ? 'active' : ''}">
                <a href="${ctx}/manager/profile">
                    <i class="bi bi-person-fill"></i>
                    <span>Hồ sơ cá nhân</span>
                </a>
            </li>
            <li class="${activePath == '/manager/profile/security' ? 'active' : ''}">
                <a href="${ctx}/manager/profile/security">
                    <i class="bi bi-shield-lock-fill"></i>
                    <span>Bảo mật</span>
                </a>
            </li>
            <li class="${activePath == '/manager/notifications/preferences' ? 'active' : ''}">
                <a href="${ctx}/manager/notifications/preferences">
                    <i class="bi bi-gear-fill"></i>
                    <span>Cài đặt thông báo</span>
                </a>
            </li>
        </ul>

        <div class="stu-sidebar-footer">
            <div class="qa-sidebar-workspace-status"><span class="qa-workspace-role-dot"></span><span class="qa-sidebar-workspace-copy">Không gian tư vấn viên</span></div>
            <form method="post" action="${ctx}/auth/logout">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">
                    <i class="bi bi-box-arrow-left"></i>
                    <span>Đăng xuất</span>
                </button>
            </form>
        </div>
    </aside>

    <%-- Thanh trên cung cấp điều khiển menu, trạng thái trực và tài khoản. --%>
    <header class="navbar navbar-expand-md d-print-none qa-workspace-topbar">
        <div class="container-xl">
            <button class="btn qa-sidebar-toggle" type="button" data-workspace-toggle
                    aria-controls="workspace-sidebar" aria-expanded="true" aria-label="Thu gọn hoặc mở menu">
                <i class="bi bi-layout-sidebar"></i>
            </button>
            <a href="${ctx}/manager/dashboard" class="qa-topbar-brand d-lg-none">
                <img src="${ctx}/assets/images/hcmute-logo.png" alt="Logo HCMUTE">
                <strong>QA<span>UTE</span></strong>
            </a>
            <div class="qa-workspace-location"><small>KHÔNG GIAN TƯ VẤN</small><strong><c:out value="${workspacePageTitle}"/></strong></div>
            <div class="navbar-nav flex-row ms-auto align-items-center gap-3">

                <%@ include file="/WEB-INF/views/common/notification-bell.jspf" %>

                <%-- Cho phép tư vấn viên cập nhật trạng thái nhận yêu cầu và chat. --%>
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

                <%-- Menu tài khoản đồng bộ với sinh viên, giữ nguyên đích hồ sơ và bảo mật của tư vấn viên. --%>
                <div class="nav-item dropdown qa-account-dropdown">
                    <a href="#" class="nav-link qa-account-toggle" data-bs-toggle="dropdown" aria-expanded="false" aria-haspopup="true" aria-label="Mở menu người dùng">
                        <span class="qa-workspace-avatar qa-workspace-avatar--small">
                            <c:choose>
                                <c:when test="${not empty workspaceAvatarUrl}">
                                    <img src="<c:out value='${workspaceAvatarUrl}'/>" alt="Ảnh đại diện" class="u-width-100 u-height-100 u-object-fit-cover u-rounded-inherit"/>
                                </c:when>
                                <c:otherwise><c:out value="${workspaceInitial}"/></c:otherwise>
                            </c:choose>
                        </span>
                        <span class="d-none d-xl-block text-start">
                            <span class="qa-account-name d-block"><c:out value="${workspaceName}"/></span>
                            <span class="qa-account-role d-block">Tư vấn viên</span>
                        </span>
                        <i class="bi bi-chevron-down qa-account-chevron" aria-hidden="true"></i>
                    </a>
                    <div class="dropdown-menu dropdown-menu-end qa-account-menu">
                        <div class="qa-account-menu-heading">
                            <strong><c:out value="${workspaceName}"/></strong>
                            <span><c:out value="${workspaceEmail}"/></span>
                        </div>
                        <a href="${ctx}/manager/profile" class="dropdown-item"><i class="bi bi-person me-2" aria-hidden="true"></i>Hồ sơ cá nhân</a>
                        <a href="${ctx}/manager/profile/security" class="dropdown-item"><i class="bi bi-shield-lock me-2" aria-hidden="true"></i>Bảo mật</a>
                        <a href="${ctx}/manager/notifications/preferences" class="dropdown-item"><i class="bi bi-gear me-2" aria-hidden="true"></i>Cài đặt thông báo</a>
                        <div class="dropdown-divider"></div>
                        <form method="post" action="${ctx}/auth/logout" class="m-0">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="dropdown-item qa-account-logout"><i class="bi bi-box-arrow-left me-2" aria-hidden="true"></i>Đăng xuất</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <%-- Hiển thị thông báo và nội dung trang con do SiteMesh cung cấp. --%>
    <main class="page-wrapper" id="workspace-main" tabindex="-1">
        <div class="page-body">
            <div class="container-xl qa-workspace-container">
                <qa:alert/>
                <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
            </div>
        </div>
        <footer class="footer footer-transparent d-print-none">
            <div class="container-xl qa-workspace-footer"><span>&copy; QAUTE · Cổng hỗ trợ sinh viên</span></div>
        </footer>
    </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/layouts/manager.js"></script>

<%-- Nạp script dùng chung và điều khiển tương tác của workspace. --%>
<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
<%@ include file="/WEB-INF/views/common/ws-scripts.jspf" %>
<script src="${pageContext.request.contextPath}/assets/js/workspace.js?v=20261029a" defer></script>
</body>
</html>