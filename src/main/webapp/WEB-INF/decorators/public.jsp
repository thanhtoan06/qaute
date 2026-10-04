<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ taglib prefix="sitemesh" uri="http://www.sitemesh.org/sitemesh-3.0" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title><sitemesh:write property="title"/> - QAUTE</title>
    <sitemesh:write property="head"/>
</head>
<body class="d-flex flex-column min-vh-100">
<header class="navbar navbar-expand-md bg-white border-bottom">
    <div class="container-xl">
        <a class="navbar-brand" href="/">QAUTE</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#public-navbar"
                aria-controls="public-navbar" aria-expanded="false" aria-label="Mở trình đơn">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="public-navbar">
            <nav class="navbar-nav">
                <a class="nav-link" href="/">Trang chủ</a>
                <a class="nav-link" href="/topics">Chủ đề</a>
                <a class="nav-link" href="/faq">Hỏi đáp FAQ</a>
                <a class="nav-link" href="/pages/guide">Hướng dẫn</a>
            </nav>
            <form class="ms-auto me-3" method="get" action="/search">
                <div class="input-group input-group-sm">
                    <input class="form-control" type="search" name="q" placeholder="Tìm kiếm" aria-label="Tìm kiếm">
                    <button class="btn btn-outline-secondary" type="submit">Tìm</button>
                </div>
            </form>
            <div class="navbar-nav">
                <sec:authorize access="isAnonymous()">
                    <a class="nav-link" href="/auth/login">Đăng nhập</a>
                    <a class="nav-link" href="/auth/register">Đăng ký</a>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <sec:authorize access="hasRole('STUDENT')">
                        <a class="nav-link" href="/student/dashboard">Vào hệ thống</a>
                    </sec:authorize>
                    <sec:authorize access="hasRole('MANAGER')">
                        <a class="nav-link" href="/manager/dashboard">Vào hệ thống</a>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ADMIN')">
                        <a class="nav-link" href="/admin/dashboard">Vào hệ thống</a>
                    </sec:authorize>
                </sec:authorize>
            </div>
        </div>
    </div>
</header>
<main class="flex-grow-1">
    <div class="container-xl py-4">
        <%-- TODO: Bật qa:alert khi tag thông báo được tạo ở T1-06. --%>
        <sitemesh:write property="body"/>
    </div>
</main>
<footer class="border-top mt-auto py-4">
    <div class="container-xl d-flex justify-content-between">
        <span>Liên hệ: Phòng Công tác Sinh viên HCMUTE</span>
        <span>© QAUTE</span>
    </div>
</footer>
<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>
