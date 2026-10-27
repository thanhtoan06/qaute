<%@ page pageEncoding="UTF-8" %>
    <%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
        <c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
    <title>
            <c:out value="${announcement.title()}" />
        </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/guest/announcement-detail.css">
</head>





        <div class="qaute-announcement-detail">
            <nav aria-label="breadcrumb" class="mb-3">
                <ol class="breadcrumb qad-breadcrumb">
                    <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Thông báo</li>
                </ol>
            </nav>

            <div class="row g-4 qad-layout">
                <div class="col-lg-8">
                    <%-- Hiển thị tiêu đề, ngày đăng, nội dung và ảnh của thông báo. --%>
                    <article class="qad-main-card mb-4">
                        <header class="qad-article-head">
                            <div class="qad-eyebrow"><i class="bi bi-broadcast-pin"></i> Cổng thông tin QAUTE</div>
                            <c:if test="${announcement.pinned()}">
                                <div class="mb-3">
                                    <span class="qad-pinned"><i class="bi bi-pin-angle-fill"></i> Thông báo quan trọng
                                        (Đã ghim)</span>
                                </div>
                            </c:if>
                            <h1 class="qad-title">
                                <c:out value="${announcement.title()}" />
                            </h1>
                            <div class="qad-title-rule"></div>
                            <c:if test="${not empty announcement.publishAt()}">
                                <div class="qad-meta">
                                    <span class="qad-meta-item"><i class="bi bi-calendar2-check"></i>Ngày đăng:
                                        <time datetime="<c:out value='${announcement.publishAt()}'/>" data-qaute-datetime><c:out value="${announcement.publishAt()}"/></time></span>
                                    <span class="qad-meta-item"><i class="bi bi-shield-check"></i>Thông tin chính
                                        thức</span>
                                </div>
                            </c:if>
                        </header>

                        <c:if test="${not empty announcement.coverUrl()}">
                            <div class="qad-cover-wrap">
                                <img src="<c:out value='${announcement.coverUrl()}'/>"
                                    alt="<c:out value='${announcement.title()}'/>" class="qad-cover">
                            </div>
                        </c:if>

                        <c:if test="${not empty announcement.summary()}">
                            <div class="qad-summary">
                                <span class="qad-summary-icon"><i class="bi bi-stars"></i></span>
                                <div>
                                    <c:out value="${announcement.summary()}" />
                                </div>
                            </div>
                        </c:if>

                        <div class="qad-content announcement-content markdown">
                            <c:out value="${sanitizedBodyHtml}" escapeXml="false" />
                        </div>
                    </article>
                </div>

                <aside class="col-lg-4 qad-sidebar">
                    <%-- Điều hướng về trang chủ hoặc bảng điều khiển theo quyền người dùng. --%>
                    <section class="qad-side-card">
                        <div class="qad-side-head">
                            <span class="qad-side-icon"><i class="bi bi-compass"></i></span>
                            <div>
                                <h2 class="qad-side-title">Điều hướng</h2>
                                <p class="qad-side-subtitle">Tiếp tục khám phá QAUTE</p>
                            </div>
                        </div>
                        <div class="qad-side-body">
                            <a href="/" class="qad-nav-link">
                                <span class="qad-nav-label"><i class="bi bi-house-door"></i>Về trang chủ</span>
                                <i class="bi bi-arrow-up-right qad-arrow"></i>
                            </a>
                            <sec:authorize access="hasRole('STUDENT')">
                                <a href="/student/dashboard" class="qad-nav-link qad-primary">
                                    <span class="qad-nav-label"><i class="bi bi-speedometer2"></i>Về bảng điều khiển
                                        sinh viên</span>
                                    <i class="bi bi-arrow-right qad-arrow"></i>
                                </a>
                            </sec:authorize>
                        </div>
                    </section>

                    <%-- Cung cấp các kênh FAQ, chat và gửi yêu cầu hỗ trợ thêm. --%>
                    <section class="qad-side-card">
                        <div class="qad-side-head">
                            <span class="qad-side-icon qad-teal"><i class="bi bi-headset"></i></span>
                            <div>
                                <h2 class="qad-side-title">Cần trợ giúp thêm?</h2>
                                <p class="qad-side-subtitle">Luôn có kênh hỗ trợ dành cho bạn</p>
                            </div>
                        </div>
                        <div class="qad-side-body">
                            <p class="qad-help-copy">Nếu bạn có câu hỏi liên quan đến thông báo này hoặc cần được tư vấn
                                chi tiết, vui lòng kết nối với chúng tôi.</p>
                            <a href="/faq" class="qad-nav-link">
                                <span class="qad-nav-label"><i class="bi bi-question-circle"></i>Tra cứu câu hỏi thường
                                    gặp (FAQ)</span>
                                <i class="bi bi-chevron-right qad-arrow"></i>
                            </a>
                            <a href="/student/chat" class="qad-nav-link qad-teal-link">
                                <span class="qad-nav-label"><i class="bi bi-chat-dots"></i>Chat trực tuyến với tư vấn
                                    viên</span>
                                <i class="bi bi-chevron-right qad-arrow"></i>
                            </a>
                            <a href="/student/tickets/new" class="qad-nav-link">
                                <span class="qad-nav-label"><i class="bi bi-pencil-square"></i>Gửi yêu cầu hỗ trợ
                                    mới</span>
                                <i class="bi bi-chevron-right qad-arrow"></i>
                            </a>
                            <div class="qad-help-note"><i class="bi bi-lightbulb-fill"></i><span>Hãy chọn kênh phù hợp
                                    để tìm câu trả lời hoặc kết nối với đội ngũ tư vấn.</span></div>
                        </div>
                    </section>
                </aside>
            </div>
        </div>