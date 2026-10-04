<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ taglib prefix="sitemesh" uri="http://www.sitemesh.org/sitemesh-3.0" %>
<c:set var="currentUri" value="${requestScope['jakarta.servlet.forward.request_uri']}"/>
<c:if test="${empty currentUri}">
    <c:set var="currentUri" value="${pageContext.request.requestURI}"/>
</c:if>
<c:set var="studentName" value="Sinh viên"/>
<c:catch var="principalError">
    <sec:authentication property="principal.fullName" var="authenticatedFullName"/>
    <c:if test="${not empty authenticatedFullName}">
        <c:set var="studentName" value="${authenticatedFullName}"/>
    </c:if>
</c:catch>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title><sitemesh:write property="title"/> - QAUTE</title>
    <sitemesh:write property="head"/>
</head>
<body>
<div class="page">
    <aside class="navbar navbar-vertical navbar-expand-lg" data-bs-theme="dark">
        <div class="container-fluid">
            <h1 class="navbar-brand navbar-brand-autodark">
                <a href="/student/dashboard">QAUTE</a>
            </h1>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#student-sidebar"
                    aria-controls="student-sidebar" aria-expanded="false" aria-label="Mở trình đơn">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="student-sidebar">
                <ul class="navbar-nav pt-lg-3">
                    <li class="nav-item">
                        <a class="nav-link${currentUri == '/student/dashboard' ? ' active' : ''}" href="/student/dashboard">
                            <span class="nav-link-icon"><i class="bi bi-house"></i></span><span class="nav-link-title">Tổng quan</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${currentUri.startsWith('/student/tickets') and currentUri != '/student/tickets/new' ? ' active' : ''}" href="/student/tickets">
                            <span class="nav-link-icon"><i class="bi bi-inbox"></i></span><span class="nav-link-title">Yêu cầu của tôi</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${currentUri == '/student/tickets/new' ? ' active' : ''}" href="/student/tickets/new">
                            <span class="nav-link-icon"><i class="bi bi-plus-circle"></i></span><span class="nav-link-title">Tạo yêu cầu mới</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${currentUri.startsWith('/student/faq') ? ' active' : ''}" href="/student/faq">
                            <span class="nav-link-icon"><i class="bi bi-question-circle"></i></span><span class="nav-link-title">Hỏi đáp FAQ</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${currentUri.startsWith('/student/chat') ? ' active' : ''}" href="/student/chat">
                            <span class="nav-link-icon"><i class="bi bi-chat-dots"></i></span><span class="nav-link-title">Chat tư vấn</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${currentUri.startsWith('/student/appointments') ? ' active' : ''}" href="/student/appointments">
                            <span class="nav-link-icon"><i class="bi bi-calendar-event"></i></span><span class="nav-link-title">Lịch hẹn</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${currentUri.startsWith('/student/notifications') ? ' active' : ''}" href="/student/notifications">
                            <span class="nav-link-icon"><i class="bi bi-bell"></i></span><span class="nav-link-title">Thông báo</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${currentUri.startsWith('/student/profile') ? ' active' : ''}" href="/student/profile">
                            <span class="nav-link-icon"><i class="bi bi-person"></i></span><span class="nav-link-title">Hồ sơ</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </aside>
    <div class="page-wrapper">
        <header class="navbar navbar-expand-md d-print-none">
            <div class="container-xl">
                <div class="navbar-nav flex-row order-md-last">
                    <div class="nav-item dropdown">
                        <a href="#" class="nav-link d-flex lh-1 text-reset p-0" data-bs-toggle="dropdown"
                           aria-label="Tài khoản sinh viên">
                            <span class="avatar avatar-sm"><i class="bi bi-person"></i></span>
                            <div class="d-none d-xl-block ps-2">
                                <div><c:out value="${studentName}"/></div>
                                <div class="mt-1 small text-secondary">Sinh viên</div>
                            </div>
                        </a>
                        <div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
                            <a class="dropdown-item" href="/student/profile">Hồ sơ</a>
                            <a class="dropdown-item" href="/student/security">Bảo mật</a>
                            <div class="dropdown-divider"></div>
                            <form method="post" action="/auth/logout">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="dropdown-item" type="submit">Đăng xuất</button>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="navbar-nav">
                    <a class="nav-link" href="/student/notifications" aria-label="Thông báo">
                        <i class="bi bi-bell"></i><span class="badge bg-red"></span>
                    </a>
                </div>
            </div>
        </header>
        <div class="page-body">
            <div class="container-xl py-4">
                <qa:alert/>
                <sitemesh:write property="body"/>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>
