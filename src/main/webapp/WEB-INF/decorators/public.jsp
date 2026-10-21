<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page import="org.sitemesh.content.Content" %>
<%
    Content pageContent = (Content) request.getAttribute("org.sitemesh.content.Content");
%>
<c:set var="currentUri" value="${requestScope['jakarta.servlet.forward.request_uri']}"/>
<c:if test="${empty currentUri}">
    <c:set var="currentUri" value="${pageContext.request.requestURI}"/>
</c:if>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title><%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE HCMUTE</title>
    <%= pageContent.getExtractedProperties().getChild("head").getValue() %>
    <style>
        /* ===== PUBLIC LAYOUT ===== */
        body.public-layout {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
            background: #f8f9fa;
        }
        /* Navbar */
        .hcmute-navbar {
            background: #fff;
            border-bottom: 2px solid #e8e8e8;
            box-shadow: 0 2px 8px rgba(0,0,0,.06);
            padding: 0;
            position: sticky;
            top: 0;
            z-index: 1030;
        }
        .hcmute-navbar .navbar-brand {
            display: flex;
            align-items: center;
            gap: .5rem;
            padding: .75rem 0;
            text-decoration: none;
        }
        .hcmute-navbar .brand-logo-wrap {
            flex-shrink: 0;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .hcmute-navbar .brand-logo-wrap img {
            height: 42px;
            width: auto;
            display: block;
            object-fit: contain;
        }
        .hcmute-navbar .brand-text .brand-name {
            font-weight: 800;
            font-size: 1.1rem;
            color: #206bc4;
            line-height: 1;
        }
        .hcmute-navbar .brand-text .brand-sub {
            font-size: .65rem;
            color: #6c757d;
            line-height: 1;
        }
        .hcmute-navbar .nav-link {
            color: #444;
            font-weight: 500;
            font-size: .9rem;
            padding: 1rem .85rem;
            border-bottom: 3px solid transparent;
            transition: color .2s, border-color .2s;
        }
        .hcmute-navbar .nav-link:hover,
        .hcmute-navbar .nav-link.active {
            color: #206bc4;
            border-bottom-color: #206bc4;
        }
        .hcmute-navbar .nav-search-input {
            border-radius: 20px 0 0 20px;
            border: 1.5px solid #ddd;
            padding: .35rem 1rem;
            font-size: .85rem;
            min-width: 180px;
        }
        .hcmute-navbar .nav-search-btn {
            border-radius: 0 20px 20px 0;
            background: #206bc4;
            color: #fff;
            border: none;
            padding: .35rem .9rem;
            font-size: .85rem;
        }
        .hcmute-navbar .nav-search-btn:hover { background: #1856a2; }
        .hcmute-navbar .btn-login-nav {
            border: 1.5px solid #206bc4;
            color: #206bc4;
            border-radius: 20px;
            padding: .35rem .95rem;
            font-size: .85rem;
            font-weight: 600;
            text-decoration: none;
            transition: background .2s, color .2s;
        }
        .hcmute-navbar .btn-login-nav:hover {
            background: #206bc4;
            color: #fff;
        }
        .hcmute-navbar .btn-register-nav {
            background: #206bc4;
            color: #fff;
            border-radius: 20px;
            padding: .35rem .95rem;
            font-size: .85rem;
            font-weight: 600;
            text-decoration: none;
            transition: background .2s;
        }
        .hcmute-navbar .btn-register-nav:hover { background: #1856a2; }
        /* full-width: no container wrapper for body */
        body.full-width-page main.public-main { padding: 0; }
        body.full-width-page main.public-main > .container-xl { padding: 0; max-width: 100%; }
        /* standard pages */
        main.public-main { flex: 1 1 auto; }
        /* Footer */
        .hcmute-footer {
            background: #1a1a2e;
            color: #a0a8b8;
            padding: 3rem 0 0;
            margin-top: auto;
        }
        .hcmute-footer .footer-logo .brand-name { color: #fff; font-weight: 800; }
        .hcmute-footer .footer-logo .brand-sub  { color: #a0a8b8; font-size: .75rem; }
        .hcmute-footer h6 { color: #fff; font-weight: 700; font-size: .85rem; text-transform: uppercase; letter-spacing: .05em; margin-bottom: 1rem; }
        .hcmute-footer a { color: #a0a8b8; text-decoration: none; font-size: .875rem; }
        .hcmute-footer a:hover { color: #75b7ff; }
        .hcmute-footer .footer-bottom {
            border-top: 1px solid rgba(255,255,255,.1);
            padding: 1.25rem 0;
            margin-top: 2.5rem;
            font-size: .8rem;
        }
        .hcmute-footer .social-link {
            width: 34px; height: 34px;
            background: rgba(255,255,255,.1);
            border-radius: 50%;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            color: #fff;
            transition: background .2s;
        }
        .hcmute-footer .social-link:hover { background: #206bc4; color: #fff; }
        .hcmute-footer .contact-item { display: flex; align-items: flex-start; gap: .5rem; font-size: .85rem; margin-bottom: .6rem; }
        .hcmute-footer .contact-item i { color: #75b7ff; margin-top: .15rem; flex-shrink: 0; }
    </style>
</head>
<body class="public-layout ${requestScope['qaute.fullWidthPage'] == true ? 'full-width-page' : ''}">

<%-- ===== NAVBAR ===== --%>
<nav class="navbar navbar-expand-lg hcmute-navbar">
    <div class="container-xl">
        <a class="navbar-brand" href="/">
            <div class="brand-logo-wrap">
                <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png"
                     alt="Logo HCMUTE"
                     onerror="this.style.display='none';this.nextElementSibling.style.display='flex';">
                <div style="display:none;width:40px;height:40px;background:linear-gradient(135deg,#c0392b,#e74c3c);border-radius:10px;align-items:center;justify-content:center;color:#fff;font-weight:800;font-size:1.1rem;">Q</div>
            </div>
            <div class="brand-text ms-1">
                <div class="brand-name">QAUTE</div>
                <div class="brand-sub">Tư vấn Sinh viên HCMUTE</div>
            </div>
        </a>
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#public-navbar"
                aria-controls="public-navbar" aria-expanded="false" aria-label="Mở trình đơn">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="public-navbar">
            <ul class="navbar-nav me-auto mb-0">
                <li class="nav-item">
                    <a class="nav-link ${currentUri == '/' or currentUri == '' ? 'active' : ''}" href="/">Trang chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${fn:contains(currentUri, '/announcements') ? 'active' : ''}" href="/announcements">Thông báo</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${fn:contains(currentUri, '/faq') ? 'active' : ''}" href="/faq">Hỏi &amp; Đáp</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${fn:contains(currentUri, '/pages/guide') ? 'active' : ''}" href="/pages/guide">Hướng dẫn</a>
                </li>
            </ul>

            <%-- Search --%>
            <form class="d-flex me-3" method="get" action="/search" role="search">
                <div class="input-group">
                    <input class="nav-search-input form-control" type="search" name="q"
                           placeholder="Tìm kiếm…" aria-label="Tìm kiếm"
                           value="${fn:escapeXml(param.q)}"
                           data-qa-suggest="NAVBAR">
                    <button class="nav-search-btn" type="submit" aria-label="Tìm">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </form>

            <%-- Auth buttons --%>
            <div class="d-flex align-items-center gap-2">
                <sec:authorize access="isAnonymous()">
                    <a href="/auth/login" class="btn-login-nav">Đăng nhập</a>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <sec:authorize access="hasRole('STUDENT')">
                        <a href="/student/dashboard" class="btn-register-nav">
                            <i class="bi bi-grid-fill me-1"></i>Vào hệ thống
                        </a>
                    </sec:authorize>
                    <sec:authorize access="hasRole('MANAGER')">
                        <a href="/manager/dashboard" class="btn-register-nav">
                            <i class="bi bi-grid-fill me-1"></i>Vào hệ thống
                        </a>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ADMIN')">
                        <a href="/admin/dashboard" class="btn-register-nav">
                            <i class="bi bi-grid-fill me-1"></i>Vào hệ thống
                        </a>
                    </sec:authorize>
                    <form method="post" action="/auth/logout" class="d-inline m-0">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button type="submit" class="btn btn-outline-danger btn-sm rounded-pill px-3" style="font-weight: 500;" title="Đăng xuất khỏi hệ thống">
                            <i class="bi bi-box-arrow-right me-1"></i>Đăng xuất
                        </button>
                    </form>
                </sec:authorize>
            </div>
        </div>
    </div>
</nav>

<%-- ===== MAIN CONTENT ===== --%>
<main class="public-main">
    <c:set var="isHome" value="${currentUri == '/' or currentUri == '' or fn:endsWith(currentUri, '/home')}"/>
    <c:choose>
        <c:when test="${isHome}">
            <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
        </c:when>
        <c:otherwise>
            <div class="container-xl py-4">
                <qa:alert/>
                <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<%-- ===== FOOTER ===== --%>
<footer class="hcmute-footer">
    <div class="container-xl">
        <div class="row g-4 pb-4">
            <%-- Brand col --%>
            <div class="col-lg-4">
                <div class="footer-logo d-flex align-items-center gap-2 mb-3">
                    <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png" alt="Logo HCMUTE" style="height:42px;width:auto;object-fit:contain;">
                    <div>
                        <div class="brand-name" style="font-size:1.1rem;">QAUTE</div>
                        <div class="brand-sub">Tư vấn Sinh viên HCMUTE</div>
                    </div>
                </div>
                <p class="small mb-3">Hệ thống tư vấn, hỗ trợ sinh viên Trường Đại học Công nghệ Kỹ thuật TP.HCM. Chúng tôi sẵn sàng hỗ trợ bạn trong suốt hành trình học tập.</p>
                <div class="d-flex gap-2">
                    <a href="https://www.facebook.com/hcmute.edu.vn/" class="social-link" aria-label="Facebook" target="_blank" rel="noopener"><i class="bi bi-facebook"></i></a>
                    <a href="https://www.youtube.com/@UTETVChannel" class="social-link" aria-label="YouTube" target="_blank" rel="noopener"><i class="bi bi-youtube"></i></a>
                </div>
            </div>

            <%-- Quick links --%>
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
                    <li><a href="/pages/contact">Liên hệ</a></li>
                    <li><a href="/pages/privacy">Chính sách bảo mật</a></li>
                </ul>
            </div>

            <%-- Contact --%>
            <div class="col-lg-4">
                <h6>Liên hệ</h6>
                <div class="contact-item"><i class="bi bi-geo-alt-fill"></i><span>01 Võ Văn Ngân, Linh Chiểu, TP. Thủ Đức, TP.HCM</span></div>
                <div class="contact-item"><i class="bi bi-telephone-fill"></i><span>(028) 3896 6740</span></div>
                <div class="contact-item"><i class="bi bi-envelope-fill"></i><span>ctsv@hcmute.edu.vn</span></div>
                <div class="contact-item"><i class="bi bi-clock-fill"></i><span>07:30 – 17:00, Thứ 2 – Thứ 6</span></div>
            </div>
        </div>

        <div class="footer-bottom">
            <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
                <span>© <fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy"/> QAUTE – Trường ĐH Công nghệ Kỹ thuật TP.HCM</span>
                <div class="d-flex gap-3">
                    <a href="/pages/privacy">Bảo mật</a>
                    <a href="/pages/terms">Điều khoản</a>
                    <a href="/pages/contact">Hỗ trợ</a>
                </div>
            </div>
        </div>
    </div>
</footer>

<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
<%@ include file="/WEB-INF/views/common/chatbot-widget.jspf" %>
</body>
</html>
