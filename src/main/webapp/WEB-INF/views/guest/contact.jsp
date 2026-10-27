<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Thông tin liên hệ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/guest/contact.css">
</head>





<div class="qaute-contact-page">
    <nav aria-label="breadcrumb" class="contact-breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Liên hệ</li>
        </ol>
    </nav>

    <%-- Giới thiệu danh bạ và mục đích trang liên hệ. --%>
    <section class="contact-hero">
        <div class="contact-hero-content">
            <div class="contact-eyebrow">
                <i class="bi bi-buildings"></i>
                QAUTE · STUDENT SUPPORT
            </div>
            <h1>Kết nối với <span>đúng nơi</span>, nhận hỗ trợ kịp thời.</h1>
            <p>
                Thông tin liên hệ của các đơn vị và phòng ban hỗ trợ sinh viên HCMUTE được tập hợp tại đây, giúp bạn thuận tiện tra cứu địa chỉ, số điện thoại, email và lĩnh vực phụ trách.
            </p>
            <div class="contact-hero-bottom">
                <span class="contact-hero-pill"><i class="bi bi-geo-alt-fill"></i> Thông tin địa điểm</span>
                <span class="contact-hero-pill"><i class="bi bi-headset"></i> Kênh hỗ trợ trực tiếp</span>
                <span class="contact-hero-pill"><i class="bi bi-diagram-3-fill"></i> Danh bạ phòng ban</span>
            </div>
        </div>
    </section>

    <%-- Hiển thị các kênh liên hệ chung dành cho sinh viên. --%>
    <section aria-label="Thông tin liên hệ chính">
        <div class="contact-section-heading">
            <div>
                <div class="contact-section-kicker">
                    <i class="bi bi-info-circle-fill"></i>
                    CONTACT INFORMATION
                </div>
                <h2>Thông tin liên hệ chính</h2>
                <p>Các kênh liên hệ tổng quan dành cho sinh viên và người cần hỗ trợ.</p>
            </div>
        </div>

        <div class="row g-4">
            <div class="col-md-4">
                <div class="contact-info-card contact-address">
                    <div class="contact-info-top">
                        <div class="contact-icon icon-blue">
                            <i class="bi bi-geo-alt-fill"></i>
                        </div>
                        <div>
                            <h3 class="contact-info-title">Địa chỉ trường</h3>
                            <div class="contact-info-subtitle">Trụ sở chính</div>
                        </div>
                    </div>
                    <div class="contact-info-divider"></div>
                    <div class="contact-info-value">
                        <c:choose>
                            <c:when test="${not empty contactAddress}">
                                <c:out value="${contactAddress}"/>
                            </c:when>
                            <c:otherwise>
                                Số 1 Võ Văn Ngân, Phường Linh Chiểu, TP. Thủ Đức, TP. Hồ Chí Minh
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="contact-info-card contact-phone">
                    <div class="contact-info-top">
                        <div class="contact-icon icon-green">
                            <i class="bi bi-telephone-fill"></i>
                        </div>
                        <div>
                            <h3 class="contact-info-title">Đường dây nóng</h3>
                            <div class="contact-info-subtitle">Tổng đài hỗ trợ</div>
                        </div>
                    </div>
                    <div class="contact-info-divider"></div>
                    <div class="contact-info-value">
                        <c:choose>
                            <c:when test="${not empty contactPhone}">
                                <a href="tel:<c:out value='${contactPhone}'/>" class="contact-main-value">
                                    <i class="bi bi-telephone-outbound-fill"></i>
                                    <c:out value="${contactPhone}"/>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span class="contact-main-value">(028) 3896 8641</span>
                            </c:otherwise>
                        </c:choose>
                        <div class="contact-meta">
                            <i class="bi bi-clock-fill"></i>
                            <span>Giờ hành chính (07:30 - 17:00)</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="contact-info-card contact-email">
                    <div class="contact-info-top">
                        <div class="contact-icon icon-cyan">
                            <i class="bi bi-envelope-at-fill"></i>
                        </div>
                        <div>
                            <h3 class="contact-info-title">Hộp thư hỗ trợ</h3>
                            <div class="contact-info-subtitle">Tiếp nhận ý kiến</div>
                        </div>
                    </div>
                    <div class="contact-info-divider"></div>
                    <div class="contact-info-value">
                        <c:choose>
                            <c:when test="${not empty contactEmail}">
                                <a href="mailto:<c:out value='${contactEmail}'/>" class="contact-main-value">
                                    <c:out value="${contactEmail}"/>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="mailto:hotrosinhvien@hcmute.edu.vn" class="contact-main-value">
                                    hotrosinhvien@hcmute.edu.vn
                                </a>
                            </c:otherwise>
                        </c:choose>
                        <div class="contact-meta">
                            <i class="bi bi-reply-all-fill"></i>
                            <span>Phản hồi trong vòng 24 - 48 giờ làm việc</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <%-- Danh sách đơn vị, thông tin liên lạc và phạm vi hỗ trợ. --%>
    <section class="contact-directory">
        <div class="contact-directory-topline">
            <div class="contact-section-heading mb-0">
                <div>
                    <div class="contact-section-kicker">
                        <i class="bi bi-diagram-3-fill"></i>
                        DEPARTMENT DIRECTORY
                    </div>
                    <h2>Danh bạ các đơn vị &amp; phòng ban</h2>
                    <p>Liên hệ trực tiếp theo từng mảng công việc để được giải quyết nhanh nhất.</p>
                </div>
            </div>
            <div class="contact-directory-mark">
                <i class="bi bi-buildings-fill"></i>
                Đơn vị hỗ trợ sinh viên
            </div>
        </div>

        <c:choose>
            <c:when test="${empty tree}">
                <div class="contact-empty-state text-center">
                    <div class="contact-empty-state-icon">
                        <i class="bi bi-building-slash"></i>
                    </div>
                    <qa:emptyState icon="bi-building" title="Chưa có dữ liệu đơn vị" message="Hiện tại chưa có thông tin phòng ban nào được công khai."/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-4">
                    <c:forEach var="dept" items="${tree}">
                        <div class="col-lg-6">
                            <div class="contact-department-card">
                                <div class="contact-department-header">
                                    <div class="contact-department-title-wrap">
                                        <div class="contact-department-icon">
                                            <i class="bi bi-building-fill"></i>
                                        </div>
                                        <h3 class="contact-department-title">
                                            <c:out value="${dept.name()}"/>
                                        </h3>
                                    </div>
                                    <c:if test="${not empty dept.code()}">
                                        <span class="contact-department-code">
                                            <c:out value="${dept.code()}"/>
                                        </span>
                                    </c:if>
                                </div>

                                <div class="contact-department-body">
                                    <c:if test="${not empty dept.description()}">
                                        <p class="contact-department-description">
                                            <c:out value="${dept.description()}"/>
                                        </p>
                                    </c:if>

                                    <ul class="contact-details-list">
                                        <c:if test="${not empty dept.location()}">
                                            <li class="contact-details-item">
                                                <span class="contact-details-icon location">
                                                    <i class="bi bi-geo-alt-fill"></i>
                                                </span>
                                                <div class="contact-details-content">
                                                    <strong>Văn phòng</strong>
                                                    <c:out value="${dept.location()}"/>
                                                </div>
                                            </li>
                                        </c:if>

                                        <c:if test="${not empty dept.email()}">
                                            <li class="contact-details-item">
                                                <span class="contact-details-icon email">
                                                    <i class="bi bi-envelope-fill"></i>
                                                </span>
                                                <div class="contact-details-content">
                                                    <strong>Email</strong>
                                                    <a href="mailto:<c:out value='${dept.email()}'/>">
                                                        <c:out value="${dept.email()}"/>
                                                    </a>
                                                </div>
                                            </li>
                                        </c:if>

                                        <c:if test="${not empty dept.phone()}">
                                            <li class="contact-details-item">
                                                <span class="contact-details-icon phone">
                                                    <i class="bi bi-telephone-fill"></i>
                                                </span>
                                                <div class="contact-details-content">
                                                    <strong>Điện thoại</strong>
                                                    <a href="tel:<c:out value='${dept.phone()}'/>">
                                                        <c:out value="${dept.phone()}"/>
                                                    </a>
                                                </div>
                                            </li>
                                        </c:if>
                                    </ul>

                                    <c:if test="${not empty dept.categories()}">
                                        <div class="contact-categories">
                                            <div class="contact-categories-heading">
                                                <i class="bi bi-grid-fill"></i>
                                                Lĩnh vực tiếp nhận hỗ trợ
                                            </div>
                                            <div class="d-flex flex-wrap gap-2">
                                                <c:forEach var="cat" items="${dept.categories()}">
                                                    <a href="/faq?category=<c:out value='${cat.id()}'/>"
                                                       class="contact-category-tag"
                                                       title="Xem câu hỏi FAQ">
                                                        <c:out value="${cat.name()}"/>
                                                    </a>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="contact-footer-note">
            <i class="bi bi-lightbulb-fill"></i>
            <div>
                <strong>Bạn chưa biết cần liên hệ đơn vị nào?</strong>
                Hãy tra cứu mục FAQ để tìm câu trả lời cho những thắc mắc thường gặp hoặc xác định lĩnh vực phù hợp trước khi liên hệ phòng ban.
            </div>
        </div>
    </section>
</div>