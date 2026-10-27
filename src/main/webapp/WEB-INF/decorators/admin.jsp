<%@ page pageEncoding="UTF-8" %>
    <%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
        <%@ page import="org.sitemesh.content.Content" %>
<%-- Lấy nội dung trang hiện tại để hiển thị trong giao diện quản trị.
--%>            <% Content pageContent=(Content) request.getAttribute("org.sitemesh.content.Content"); %>
                <c:set var="ctx" value="${pageContext.request.contextPath}" />
                <c:set var="notificationsUrl" value="${ctx}/admin/notifications"/>
                <%@ include file="/WEB-INF/views/common/workspace-user.jspf" %>

                    <c:set var="rawUri"
                        value="${not empty requestScope['jakarta.servlet.forward.request_uri'] ? requestScope['jakarta.servlet.forward.request_uri'] : pageContext.request.requestURI}" />
                    <c:set var="currentPath"
                        value="${fn:startsWith(rawUri, ctx) ? fn:substring(rawUri, fn:length(ctx), fn:length(rawUri)) : rawUri}" />

                    <c:set var="menuPaths"
                        value="/admin/dashboard,/admin/users,/admin/users/import,/admin/departments,/admin/categories,/admin/managers,/admin/faq,/admin/faq/tags,/admin/faq/synonyms,/admin/guest-home-faq,/admin/tickets,/admin/chat-history,/admin/appointments,/admin/notifications,/admin/notifications/preferences,/admin/sla,/admin/settings,/admin/announcements,/admin/audit-logs,/admin/reports,/admin/health,/admin/profile/security,/admin/profile" />
                    <c:set var="activePath" value="" />
                    <c:forTokens items="${menuPaths}" delims="," var="menuPath">
                        <c:if test="${currentPath == menuPath or fn:startsWith(currentPath, menuPath.concat('/'))}">
                            <c:if test="${fn:length(menuPath) gt fn:length(activePath)}">
                                <c:set var="activePath" value="${menuPath}" />
                            </c:if>
                        </c:if>
                    </c:forTokens>

                    <!DOCTYPE html>
                    <html lang="vi">

                    <head>
                        <meta charset="UTF-8">
                        <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
                            <link rel="stylesheet" href="${ctx}/assets/css/workspace.css">
                            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layouts/admin.css">
<link rel="stylesheet" href="${ctx}/assets/css/role-shell.css">

                            <title>
                                <%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE
                            </title>
                            <%= pageContent.getExtractedProperties().getChild("head").getValue() %>
                            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/inline-utilities.css">

                    </head>

                    <body class="qa-workspace qa-workspace--admin">
                        <a class="qa-workspace-skip" href="#workspace-main">Chuyển đến nội dung</a>
                        <button class="qa-workspace-overlay" type="button" data-workspace-close hidden
                            aria-label="Đóng menu điều hướng"></button>
                        <div class="page">

                            <%-- Thanh bên gồm nhận diện quản trị viên và các nhóm quản lý hệ thống. --%>
                            <aside class="qa-workspace-sidebar" id="workspace-sidebar" data-bs-theme="dark"
                                aria-label="Điều hướng quản trị viên">
                                <a href="${ctx}/admin/dashboard" class="stu-brand">
                                    <img src="${ctx}/assets/images/hcmute-logo.png" alt="Logo HCMUTE" class="stu-brand-logo">
                                    <div class="stu-brand-text">
                                        <div class="t1">QAUTE</div>
                                        <div class="t2">CỔNG HỖ TRỢ SINH VIÊN</div>
                                    </div>
                                </a>

                                <a href="${ctx}/admin/dashboard" class="stu-user-card">
                                    <c:choose>
                                        <c:when test="${not empty workspaceAvatarUrl}">
                                            <img class="stu-user-avatar u-object-fit-cover" src="<c:out value='${workspaceAvatarUrl}'/>" alt="Ảnh đại diện"/>
                                        </c:when>
                                        <c:otherwise><div class="stu-user-avatar"><c:out value="${workspaceInitial}"/></div></c:otherwise>
                                    </c:choose>
                                    <div class="overflow-hidden">
                                        <div class="stu-user-name text-truncate"><c:out value="${workspaceName}"/></div>
                                        <div class="stu-user-role">Quản trị viên</div>
                                    </div>
                                </a>

                                <%-- Các nhóm menu đồng nhất với sinh viên, giữ nguyên toàn bộ lối vào hiện có. --%>
                                <div class="stu-nav-label">Tổng quan</div>
                                <ul class="stu-nav">
                                    <li class="${activePath == '/admin/dashboard' ? 'active' : ''}">
                                        <a href="${ctx}/admin/dashboard">
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
                                    <li class="${activePath == '/admin/notifications' ? 'active' : ''}">
                                        <a href="${ctx}/admin/notifications">
                                            <i class="bi bi-bell-fill"></i>
                                            <span>Thông báo</span>
                                            <span class="nav-badge qa-sidebar-notification-badge d-none"
                                                data-sidebar-notification-badge>0</span>
                                        </a>
                                    </li>
                                </ul>

                                <div class="stu-nav-label">Yêu cầu & Hỗ trợ</div>
                                <ul class="stu-nav">
                                    <li class="${activePath == '/admin/tickets' ? 'active' : ''}">
                                        <a href="${ctx}/admin/tickets"><i class="bi bi-inbox-fill"></i><span>Yêu cầu tư vấn</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/chat-history' ? 'active' : ''}">
                                        <a href="${ctx}/admin/chat-history"><i class="bi bi-clock-history"></i><span>Lịch sử tư vấn trực tuyến</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/appointments' ? 'active' : ''}">
                                        <a href="${ctx}/admin/appointments"><i class="bi bi-calendar-check"></i><span>Lịch hẹn</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/audit-logs' ? 'active' : ''}">
                                        <a href="${ctx}/admin/audit-logs"><i class="bi bi-journal-text"></i><span>Nhật ký hệ thống</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/reports' ? 'active' : ''}">
                                        <a href="${ctx}/admin/reports"><i class="bi bi-bar-chart-line"></i><span>Báo cáo</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/health' ? 'active' : ''}">
                                        <a href="${ctx}/admin/health"><i class="bi bi-heart-pulse"></i><span>Sức khỏe hệ thống</span></a>
                                    </li>
                                </ul>

                                <div class="stu-nav-label">Quản trị hệ thống</div>
                                <ul class="stu-nav">
                                    <li class="${activePath == '/admin/users' ? 'active' : ''}">
                                        <a href="${ctx}/admin/users"><i class="bi bi-people"></i><span>Danh sách người dùng</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/users/import' ? 'active' : ''}">
                                        <a href="${ctx}/admin/users/import"><i class="bi bi-person-plus"></i><span>Nhập hàng loạt</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/departments' ? 'active' : ''}">
                                        <a href="${ctx}/admin/departments"><i class="bi bi-building"></i><span>Phòng ban</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/categories' ? 'active' : ''}">
                                        <a href="${ctx}/admin/categories"><i class="bi bi-diagram-3"></i><span>Chuyên mục</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/managers' ? 'active' : ''}">
                                        <a href="${ctx}/admin/managers"><i class="bi bi-person-gear"></i><span>Manager và phạm vi</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/sla' ? 'active' : ''}">
                                        <a href="${ctx}/admin/sla"><i class="bi bi-clock-history"></i><span>SLA và giờ làm việc</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/settings' ? 'active' : ''}">
                                        <a href="${ctx}/admin/settings"><i class="bi bi-sliders"></i><span>Cấu hình hệ thống</span></a>
                                    </li>
                                </ul>

                                <div class="stu-nav-label">Kiến thức</div>
                                <ul class="stu-nav">
                                    <li class="${activePath == '/admin/faq' ? 'active' : ''}">
                                        <a href="${ctx}/admin/faq"><i class="bi bi-question-circle-fill"></i><span>Quản lý FAQ</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/faq/tags' ? 'active' : ''}">
                                        <a href="${ctx}/admin/faq/tags"><i class="bi bi-tags"></i><span>Thẻ (tag)</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/faq/synonyms' ? 'active' : ''}">
                                        <a href="${ctx}/admin/faq/synonyms"><i class="bi bi-arrow-left-right"></i><span>Từ đồng nghĩa</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/guest-home-faq' ? 'active' : ''}">
                                        <a href="${ctx}/admin/guest-home-faq"><i class="bi bi-house-heart"></i><span>FAQ trang chủ</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/announcements' ? 'active' : ''}">
                                        <a href="${ctx}/admin/announcements">
                                            <i class="bi bi-megaphone"></i>
                                            <span>Thông báo chính thức</span>
                                        </a>
                                    </li>
                                </ul>

                                <div class="stu-nav-label">Tài khoản</div>
                                <ul class="stu-nav">
                                    <li class="${activePath == '/admin/profile' ? 'active' : ''}">
                                        <a href="${ctx}/admin/profile"><i class="bi bi-person-fill"></i><span>Hồ sơ cá nhân</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/profile/security' ? 'active' : ''}">
                                        <a href="${ctx}/admin/profile/security"><i class="bi bi-shield-lock-fill"></i><span>Bảo mật</span></a>
                                    </li>
                                    <li class="${activePath == '/admin/notifications/preferences' ? 'active' : ''}">
                                        <a href="${ctx}/admin/notifications/preferences"><i class="bi bi-gear-fill"></i><span>Cài đặt thông báo</span></a>
                                    </li>
                                </ul>

                                <div class="stu-sidebar-footer">
                                    <div class="qa-sidebar-workspace-status"><span class="qa-workspace-role-dot"></span><span class="qa-sidebar-workspace-copy">Không gian quản trị</span></div>
                                    <form method="post" action="${ctx}/auth/logout">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <button type="submit">
                                            <i class="bi bi-box-arrow-left"></i>
                                            <span>Đăng xuất</span>
                                        </button>
                                    </form>
                                </div>
                            </aside>

                            <%-- Thanh trên cung cấp nút thu gọn menu và menu tài khoản quản trị. --%>
                            <header class="navbar navbar-expand-md d-print-none qa-workspace-topbar">
                                <div class="container-xl">
                                    <button class="btn qa-sidebar-toggle" type="button" data-workspace-toggle
                                        aria-controls="workspace-sidebar" aria-expanded="true"
                                        aria-label="Thu gọn hoặc mở menu">
                                        <i class="bi bi-layout-sidebar"></i>
                                    </button>
                                    <a href="${ctx}/admin/dashboard" class="qa-topbar-brand d-lg-none">
                                        <img src="${ctx}/assets/images/hcmute-logo.png" alt="Logo HCMUTE">
                                        <strong>QA<span>UTE</span></strong>
                                    </a>
                                    <div class="qa-workspace-location"><small>KHÔNG GIAN QUẢN TRỊ</small><strong>
                                            <c:out value="${workspacePageTitle}" />
                                        </strong></div>
                                    <div class="navbar-nav flex-row ms-auto align-items-center gap-3">

                                            <%@ include file="/WEB-INF/views/common/notification-bell.jspf" %>

                                            <%-- Menu tài khoản quản trị. --%>
                                            <div class="nav-item dropdown qa-account-dropdown">
                                                <a href="#" class="nav-link qa-account-toggle"
                                                    data-bs-toggle="dropdown" aria-expanded="false" aria-haspopup="true"
                                                    aria-label="Mở menu người dùng">
                                                    <span class="qa-workspace-avatar qa-workspace-avatar--small">
                                                        <c:choose>
                                                            <c:when test="${not empty workspaceAvatarUrl}">
                                                                <img src="<c:out value='${workspaceAvatarUrl}'/>" alt="Ảnh đại diện" class="u-width-100 u-height-100 u-object-fit-cover u-rounded-inherit"/>
                                                            </c:when>
                                                            <c:otherwise><c:out value="${workspaceInitial}" /></c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                    <span class="d-none d-xl-block text-start">
                                                        <span class="qa-account-name d-block"><c:out value="${workspaceName}" /></span>
                                                        <span class="qa-account-role d-block">Quản trị viên</span>
                                                    </span>
                                                    <i class="bi bi-chevron-down qa-account-chevron" aria-hidden="true"></i>
                                                </a>
                                                <div class="dropdown-menu dropdown-menu-end qa-account-menu">
                                                    <div class="qa-account-menu-heading">
                                                        <strong><c:out value="${workspaceName}" /></strong>
                                                        <span><c:out value="${workspaceEmail}" /></span>
                                                    </div>
                                                    <a href="${ctx}/admin/profile" class="dropdown-item">
                                                        <i class="bi bi-person me-2" aria-hidden="true"></i>Hồ sơ cá nhân
                                                    </a>
                                                    <a href="${ctx}/admin/profile/security" class="dropdown-item">
                                                        <i class="bi bi-shield-lock me-2" aria-hidden="true"></i>Bảo mật
                                                    </a>
                                                    <a href="${ctx}/admin/notifications/preferences" class="dropdown-item">
                                                        <i class="bi bi-gear me-2" aria-hidden="true"></i>Cài đặt thông báo
                                                    </a>
                                                    <div class="dropdown-divider"></div>
                                                    <form method="post" action="${ctx}/auth/logout" class="m-0">
                                                        <input type="hidden" name="${_csrf.parameterName}"
                                                            value="${_csrf.token}" />
                                                        <button type="submit" class="dropdown-item qa-account-logout">
                                                            <i class="bi bi-box-arrow-left me-2" aria-hidden="true"></i>Đăng xuất
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                    </div>
                                </div>
                            </header>

                            <%-- Hiển thị đường dẫn trang, thông báo và nội dung do trang con cung cấp. --%>
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

                                <%-- Hiển thị cảnh báo và nội dung trang quản trị hiện tại. --%>
                                <div class="page-body">
                                    <div class="container-xl qa-workspace-container">
                                        <qa:alert />
                                        <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
                                    </div>
                                </div>
                                <footer class="footer footer-transparent d-print-none">
                                    <div class="container-xl qa-workspace-footer"><span>&copy; QAUTE · Cổng hỗ trợ sinh
                                            viên</span></div>
                                </footer>
                            </main>
                        </div>

                                                <script src="${pageContext.request.contextPath}/assets/js/layouts/admin.js"></script>

                        <%@ include file="/WEB-INF/views/common/scripts.jspf" %>
                        <%@ include file="/WEB-INF/views/common/ws-scripts.jspf" %>
                            <script src="${pageContext.request.contextPath}/assets/js/workspace.js" defer></script>
                    </body>

                    </html>