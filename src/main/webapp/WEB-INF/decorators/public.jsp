<%@ page pageEncoding="UTF-8" %>
    <%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
        <%@ page import="org.sitemesh.content.Content" %>
<%-- Lấy nội dung trang hiện tại để hiển thị trong giao diện công khai.
--%>            <% Content pageContent=(Content) request.getAttribute("org.sitemesh.content.Content"); %>
                <c:set var="currentUri" value="${requestScope['jakarta.servlet.forward.request_uri']}" />
                <c:if test="${empty currentUri}">
                    <c:set var="currentUri" value="${pageContext.request.requestURI}" />
                </c:if>
                <!DOCTYPE html>
                <html lang="vi">

                <head>
                    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layouts/public.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components/chatbot-widget.css">

                        <title>
                            <% String pageTitle = pageContent.getExtractedProperties().getChild("title").getValue(); %>
                            <%= (pageTitle != null && !"null".equals(pageTitle) && !pageTitle.isBlank()) ? pageTitle + " - QAUTE HCMUTE" : "QAUTE - Tư vấn Sinh viên HCMUTE" %>
                        </title>
                        <%
                            String headContent = pageContent.getExtractedProperties().getChild("head").getValue();
                            if (headContent != null && !"null".equals(headContent)) {
                                out.print(headContent);
                            }
                        %>
                        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/inline-utilities.css">

                </head>

                <body class="public-layout ${requestScope['qaute.fullWidthPage'] == true ? 'full-width-page' : ''}">

                    <%-- Thanh điều hướng công khai gồm liên kết nội dung, tìm kiếm và tài khoản. --%>
                    <nav class="navbar navbar-expand-lg hcmute-navbar">
                        <div class="container-xl">
                            <a class="navbar-brand" href="/">
                                <div class="brand-logo-wrap">
                                    <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png"
                                        alt="Logo HCMUTE"
                                        data-image-error="show-sibling">
                                    <div
                                        class="u-display-none u-width-40px u-height-40px u-bg-linear-gradient-135deg-c0392b-e74c3c u-rounded-10px u-align-items-center u-justify-content-center u-color-fff u-font-weight-800 u-text-size-1-1rem">
                                        Q</div>
                                </div>
                                <div class="brand-text ms-1">
                                    <div class="brand-name">QAUTE</div>
                                    <div class="brand-sub">Tư vấn Sinh viên HCMUTE</div>
                                </div>
                            </a>

                            <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse"
                                data-bs-target="#public-navbar" aria-controls="public-navbar" aria-expanded="false"
                                aria-label="Mở trình đơn">
                                <span class="navbar-toggler-icon"></span>
                            </button>

                            <div class="collapse navbar-collapse" id="public-navbar">
                                <%-- Các liên kết đến trang chủ, thông báo, hỏi đáp, chủ đề và hướng dẫn căn giữa đều đặn --%>
                                <ul class="navbar-nav mx-auto mb-0">
                                    <li class="nav-item">
                                        <a class="nav-link ${currentUri == '/' or currentUri == '' ? 'active' : ''}"
                                            href="/">
                                            <i class="bi bi-house-door me-1" aria-hidden="true"></i>Trang chủ
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link ${fn:contains(currentUri, '/announcements') ? 'active' : ''}"
                                            href="/announcements">
                                            <i class="bi bi-megaphone me-1" aria-hidden="true"></i>Thông báo
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link ${fn:contains(currentUri, '/faq') ? 'active' : ''}"
                                            href="/faq">
                                            <i class="bi bi-chat-square-text me-1" aria-hidden="true"></i>Hỏi &amp; Đáp
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link ${fn:contains(currentUri, '/topics') ? 'active' : ''}"
                                            href="/topics">
                                            <i class="bi bi-diagram-3 me-1" aria-hidden="true"></i>Chủ đề tư vấn
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link ${fn:contains(currentUri, '/pages/guide') ? 'active' : ''}"
                                            href="/pages/guide">
                                            <i class="bi bi-compass me-1" aria-hidden="true"></i>Hướng dẫn
                                        </a>
                                    </li>
                                </ul>

                                <%-- Tìm kiếm nội dung công khai theo từ khóa người dùng nhập. --%>
                                <%-- Form tìm kiếm đã bị ẩn cho trang guest --%>
                                <%--
                                <form class="d-flex me-3" method="get" action="/search" role="search">
                                    <div class="input-group">
                                        <input class="nav-search-input form-control" type="search" name="q"
                                            placeholder="Tìm kiếm…" aria-label="Tìm kiếm"
                                            value="${fn:escapeXml(param.q)}" data-qa-suggest="NAVBAR">
                                        <button class="nav-search-btn" type="submit" aria-label="Tìm">
                                            <i class="bi bi-search" aria-hidden="true"></i>
                                        </button>
                                    </div>
                                </form>
                                --%>

                                <%-- Hiển thị đăng nhập cho khách hoặc lối vào hệ thống và đăng xuất cho người dùng. --%>
                                <div class="d-flex align-items-center gap-2">
                                    <sec:authorize access="isAnonymous()">
                                        <a href="/auth/login" class="btn-login-nav">
                                            <i class="bi bi-box-arrow-in-right me-1" aria-hidden="true"></i>Đăng nhập
                                        </a>
                                    </sec:authorize>
                                    <sec:authorize access="isAuthenticated()">
                                        <sec:authorize access="hasRole('STUDENT')">
                                            <a href="/student/dashboard" class="btn-register-nav">
                                                <i class="bi bi-grid-fill me-1" aria-hidden="true"></i>Vào hệ thống
                                            </a>
                                        </sec:authorize>
                                        <sec:authorize access="hasRole('MANAGER')">
                                            <a href="/manager/dashboard" class="btn-register-nav">
                                                <i class="bi bi-grid-fill me-1" aria-hidden="true"></i>Vào hệ thống
                                            </a>
                                        </sec:authorize>
                                        <sec:authorize access="hasRole('ADMIN')">
                                            <a href="/admin/dashboard" class="btn-register-nav">
                                                <i class="bi bi-grid-fill me-1" aria-hidden="true"></i>Vào hệ thống
                                            </a>
                                        </sec:authorize>
                                        <form method="post" action="/auth/logout" class="d-inline m-0">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                            <button type="submit"
                                                class="btn btn-outline-danger btn-sm rounded-pill px-3 u-font-weight-500" title="Đăng xuất khỏi hệ thống">
                                                <i class="bi bi-box-arrow-right me-1" aria-hidden="true"></i>Đăng xuất
                                            </button>
                                        </form>
                                    </sec:authorize>
                                </div>
                            </div>
                        </div>
                    </nav>

                    <%-- Khu vực nội dung công khai; trang chủ dùng bố cục riêng, trang khác có thông báo. --%>
                    <main class="public-main">
                        <c:set var="isHome"
                            value="${currentUri == '/' or currentUri == '' or fn:endsWith(currentUri, '/home')}" />
                        <c:choose>
                            <c:when test="${isHome}">
                                <% String bodyVal = pageContent.getExtractedProperties().getChild("body").getValue(); if (bodyVal != null && !"null".equals(bodyVal)) out.print(bodyVal); %>
                            </c:when>
                            <c:otherwise>
                                <div class="container-xl py-4">
                                    <qa:alert />
                                    <% String bodyVal2 = pageContent.getExtractedProperties().getChild("body").getValue(); if (bodyVal2 != null && !"null".equals(bodyVal2)) out.print(bodyVal2); %>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </main>

                    <%-- Chân trang giới thiệu QAUTE, liên kết dịch vụ, thông tin liên hệ và bản quyền. --%>
                    <footer class="hcmute-footer">
                        <div class="container-xl">
                            <div class="row g-4 pb-4">
                                <div class="col-lg-4">
                                    <div class="footer-logo d-flex align-items-center gap-2 mb-3">
                                        <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png"
                                            alt="Logo HCMUTE" class="u-height-42px u-width-auto u-object-fit-contain">
                                        <div>
                                            <div class="brand-name u-text-size-1-1rem">QAUTE</div>
                                            <div class="brand-sub">Tư vấn Sinh viên HCMUTE</div>
                                        </div>
                                    </div>
                                    <p class="small mb-3">Hệ thống tư vấn, hỗ trợ sinh viên Trường Đại học Công nghệ Kỹ
                                        thuật TP.HCM. Chúng tôi sẵn sàng hỗ trợ bạn trong suốt hành trình học tập.</p>
                                    <div class="d-flex gap-2">
                                        <a href="https://www.facebook.com/hcmute.edu.vn/" class="social-link"
                                            aria-label="Facebook" target="_blank" rel="noopener"><i
                                                class="bi bi-facebook" aria-hidden="true"></i></a>
                                        <a href="https://www.youtube.com/@UTETVChannel" class="social-link"
                                            aria-label="YouTube" target="_blank" rel="noopener"><i class="bi bi-youtube"
                                                aria-hidden="true"></i></a>
                                    </div>
                                </div>

                                <%-- Liên kết dịch vụ dành cho sinh viên. --%>
                                <div class="col-6 col-lg-2">
                                    <h6>Dịch vụ</h6>
                                    <ul class="list-unstyled d-flex flex-column gap-2">
                                        <li><a href="/faq">Hỏi &amp; Đáp theo chủ đề</a></li>
                                        <li><a href="/student/tickets/new">Gửi yêu cầu</a></li>
                                        <li><a href="/student/appointments/book">Đặt lịch hẹn</a></li>
                                    </ul>
                                </div>

                                <%-- Liên kết giới thiệu, hướng dẫn và chính sách. --%>
                                <div class="col-6 col-lg-2">
                                    <h6>Về chúng tôi</h6>
                                    <ul class="list-unstyled d-flex flex-column gap-2">
                                        <li><a href="/pages/about">Giới thiệu</a></li>
                                        <li><a href="/pages/guide">Hướng dẫn sử dụng</a></li>
                                        <li><a href="/pages/privacy">Chính sách bảo mật</a></li>
                                    </ul>
                                </div>

                                <%-- Địa chỉ, điện thoại, email và giờ làm việc của trường. --%>
                                <div class="col-lg-4">
                                    <h6>Liên hệ</h6>
                                    <div class="contact-item"><i class="bi bi-geo-alt-fill"
                                            aria-hidden="true"></i><span>01 Võ Văn Ngân, Linh Chiểu, TP. Thủ Đức,
                                            TP.HCM</span></div>
                                    <div class="contact-item"><i class="bi bi-telephone-fill"
                                            aria-hidden="true"></i><span>(028) 3896 6740</span></div>
                                    <div class="contact-item"><i class="bi bi-envelope-fill"
                                            aria-hidden="true"></i><span>ctsv@hcmute.edu.vn</span></div>
                                    <div class="contact-item"><i class="bi bi-clock-fill"
                                            aria-hidden="true"></i><span>07:30 – 17:00, Thứ 2 – Thứ 6</span></div>
                                </div>
                            </div>

                            <div class="footer-bottom">
                                <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
                                    <span>©
                                        <fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy" /> QAUTE –
                                        Trường ĐH Công nghệ Kỹ thuật TP.HCM
                                    </span>
                                </div>
                            </div>
                        </div>
                    </footer>

                    <%-- Nạp script toàn cục và tiện ích chat trên các trang công khai. --%>
                    <%@ include file="/WEB-INF/views/common/scripts.jspf" %>
                        <%@ include file="/WEB-INF/views/common/chatbot-widget.jspf" %>
                </body>

                </html>