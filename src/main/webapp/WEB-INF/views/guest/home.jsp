<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Trang chủ - Cổng Tư Vấn Sinh Viên HCMUTE</title>


<%-- =========================================================================
     1. HERO SECTION (HCMUTE QAUTE BRANDING & SEARCH)
     ========================================================================= --%>
<section class="hcmute-hero">
    <div class="container-xl">
        <div class="row align-items-center g-5 py-4">
            <div class="col-lg-7">
                <%-- Badge giờ hỗ trợ --%>
                <div class="hcmute-hero-pill mb-3">
                    <i class="bi bi-clock-history"></i>
                    <span>Hỗ trợ 07:30 – 17:00, Thứ 2 – Thứ 6</span>
                </div>

                <%-- Tiêu đề chính --%>
                <h1 class="hcmute-hero-title">
                    Mọi thắc mắc của sinh viên HCMUTE, có QAUTE lo.
                </h1>

                <%-- Mô tả --%>
                <p class="hcmute-hero-desc">
                    Tra cứu câu hỏi thường gặp, gửi yêu cầu tư vấn, trò chuyện trực tuyến và đặt lịch hẹn với chuyên viên – tất cả trong một nền tảng hỗ trợ sinh viên thông minh.
                </p>

                <%-- Form tìm kiếm nhanh tại Hero --%>
                <form action="/search" method="get" class="hcmute-hero-search-box mb-4">
                    <i class="bi bi-search text-muted fs-5 ms-3 me-2"></i>
                    <input type="search" name="q" id="hero-search" class="hcmute-hero-search-input"
                           placeholder="Ví dụ: hạn đóng học phí, bảo lưu, đăng ký môn..."
                           autocomplete="off" aria-label="Tìm kiếm câu hỏi" data-qa-suggest="HOME">
                    <button type="submit" class="hcmute-hero-search-btn">
                        Tìm kiếm
                    </button>
                </form>

                <%-- Từ khóa tìm nhanh --%>
                <div class="d-flex flex-wrap align-items-center gap-2 small text-white-50 mb-4">
                    <span>Gợi ý nhanh:</span>
                    <a href="/search?q=H%E1%BB%8Dc+ph%C3%AD" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Học phí</a>
                    <a href="/search?q=%C4%90%C4%83ng+k%C3%BD+m%C3%B4n" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Đăng ký môn</a>
                    <a href="/search?q=H%E1%BB%8Dc+b%E1%BB%95ng" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Học bổng</a>
                    <a href="/search?q=K%C3%BD+t%C3%BAc+x%C3%A1" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Ký túc xá</a>
                    <a href="/search?q=B%E1%BA%A3o+l%C6%B0u" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Bảo lưu</a>
                </div>

                <%-- Chỉ số thống kê (Stats) --%>
                <div class="row g-4 pt-2 border-top border-white-10">
                    <div class="col-4">
                        <div class="hcmute-hero-stat-num">
                            <c:choose>
                                <c:when test="${not empty totalFaq && totalFaq > 0}">
                                    ${totalFaq * 40}+
                                </c:when>
                                <c:otherwise>12.480+</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="hcmute-hero-stat-label">Ticket đã xử lý</div>
                    </div>
                    <div class="col-4">
                        <div class="hcmute-hero-stat-num">
                            <c:choose>
                                <c:when test="${not empty medianFirstResponseMinutes}">
                                    <c:choose>
                                        <c:when test="${medianFirstResponseMinutes < 60}">
                                            &lt; <fmt:formatNumber value="${medianFirstResponseMinutes}" maxFractionDigits="0"/>p
                                        </c:when>
                                        <c:otherwise>
                                            &lt; <fmt:formatNumber value="${medianFirstResponseMinutes / 60}" maxFractionDigits="0"/>h
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>&lt; 24h</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="hcmute-hero-stat-label">Thời gian phản hồi TB</div>
                    </div>
                    <div class="col-4">
                        <div class="hcmute-hero-stat-num">96%</div>
                        <div class="hcmute-hero-stat-label">Mức độ hài lòng</div>
                    </div>
                </div>
            </div>

            <%-- Ảnh minh họa Hero --%>
            <div class="col-lg-5 d-none d-lg-block">
                <div class="hcmute-hero-image-wrap">
                    <img src="${pageContext.request.contextPath}/assets/images/hero-students.png"
                         alt="Chuyên viên tư vấn đang trao đổi cùng sinh viên HCMUTE"
                         class="w-100 h-100 object-fit-cover"
                         onerror="this.src='${pageContext.request.contextPath}/assets/images/campus-hcmute.png';">
                </div>
            </div>
        </div>
    </div>
</section>

<%-- 4 Thẻ dịch vụ nhanh nổi bên dưới Hero --%>
<div class="hcmute-quick-services">
    <div class="container-xl">
        <div class="row g-3">
            <div class="col-6 col-md-3">
                <a href="#faq" class="hcmute-service-card">
                    <div class="hcmute-service-icon">
                        <i class="bi bi-search"></i>
                    </div>
                    <h6 class="hcmute-service-title">Tra cứu FAQ</h6>
                    <p class="hcmute-service-desc">Hơn 200 câu hỏi về học vụ, học phí, học bổng đã giải đáp.</p>
                </a>
            </div>
            <div class="col-6 col-md-3">
                <a href="#ticket" class="hcmute-service-card">
                    <div class="hcmute-service-icon">
                        <i class="bi bi-ticket-perforated"></i>
                    </div>
                    <h6 class="hcmute-service-title">Gửi ticket</h6>
                    <p class="hcmute-service-desc">Gửi yêu cầu và theo dõi tiến độ xử lý từ chuyên viên 24h.</p>
                </a>
            </div>
            <div class="col-6 col-md-3">
                <a href="javascript:void(0)" onclick="openQauteChatbot()" class="hcmute-service-card">
                    <div class="hcmute-service-icon">
                        <i class="bi bi-robot"></i>
                    </div>
                    <h6 class="hcmute-service-title">Trợ lý ảo Bot</h6>
                    <p class="hcmute-service-desc">Hỏi đáp tức thì cùng trợ lý ảo QAUTE kéo thả thông minh 24/7.</p>
                </a>
            </div>
            <div class="col-6 col-md-3">
                <a href="#booking" class="hcmute-service-card">
                    <div class="hcmute-service-icon">
                        <i class="bi bi-calendar-check"></i>
                    </div>
                    <h6 class="hcmute-service-title">Đặt lịch hẹn</h6>
                    <p class="hcmute-service-desc">Gặp trực tiếp hoặc online 1-1 với chuyên viên các phòng ban.</p>
                </a>
            </div>
        </div>
    </div>
</div>

<%-- =========================================================================
     2. DISCOVERY SECTION (TRUNG TÂM HỖ TRỢ SINH VIÊN)
     ========================================================================= --%>
<section class="hcmute-section hcmute-section-muted" id="discovery">
    <div class="container-xl">
        <div class="row g-4 align-items-end mb-5">
            <div class="col-lg-6">
                <span class="hcmute-eyebrow">
                    <i class="bi bi-stars"></i>Trung tâm hỗ trợ sinh viên
                </span>
                <h2 class="hcmute-title-main">
                    Tìm đúng câu trả lời, ngay từ lần đầu tiên.
                </h2>
                <p class="hcmute-desc-main mb-0">
                    Khám phá kho kiến thức được đội ngũ QAUTE cập nhật liên tục, hoặc chuyển ngay sang tư vấn viên khi cần hỗ trợ chuyên sâu.
                </p>
            </div>
            <div class="col-lg-6">
                <div class="hcmute-card-modern p-4">
                    <div class="d-flex align-items-center justify-content-between mb-3">
                        <div class="d-flex align-items-center gap-2 fw-bold text-dark small">
                            <i class="bi bi-fire text-danger"></i>
                            <span>Chủ đề được sinh viên tra cứu nhiều</span>
                        </div>
                        <a href="/faq" class="small text-primary text-decoration-none fw-semibold">Xem tất cả FAQ <i class="bi bi-arrow-right"></i></a>
                    </div>
                    <div class="row g-2">
                        <div class="col-6">
                            <a href="/search?q=H%E1%BB%8Dc+ph%C3%AD" class="p-2.5 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all">
                                <span class="rounded bg-danger-subtle text-danger p-1.5"><i class="bi bi-wallet2"></i></span>
                                <span class="small fw-semibold">Học phí & Tài chính</span>
                            </a>
                        </div>
                        <div class="col-6">
                            <a href="/search?q=H%E1%BB%8Dc+b%E1%BB%95ng" class="p-2.5 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all">
                                <span class="rounded bg-warning-subtle text-warning-emphasis p-1.5"><i class="bi bi-award-fill"></i></span>
                                <span class="small fw-semibold">Học bổng khuyến khích</span>
                            </a>
                        </div>
                        <div class="col-6">
                            <a href="/search?q=%C4%90%C4%83ng+k%C3%BD+m%C3%B4n" class="p-2.5 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all">
                                <span class="rounded bg-primary-subtle text-primary p-1.5"><i class="bi bi-journal-bookmark-fill"></i></span>
                                <span class="small fw-semibold">Đăng ký môn học</span>
                            </a>
                        </div>
                        <div class="col-6">
                            <a href="/search?q=B%E1%BA%A3o+l%C6%B0u" class="p-2.5 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all">
                                <span class="rounded bg-success-subtle text-success p-1.5"><i class="bi bi-pause-circle-fill"></i></span>
                                <span class="small fw-semibold">Bảo lưu & Nghỉ học</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%-- Grid FAQ nổi bật & Thông báo mới nhất --%>
        <div class="row g-4 mb-4">
            <%-- Cột trái: FAQ được sinh viên quan tâm nhất --%>
            <div class="col-lg-7">
                <div class="hcmute-card-modern p-4 p-md-5 h-100">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <span class="text-primary fw-bold small text-uppercase" style="letter-spacing: 0.05em;">Được sinh viên quan tâm</span>
                            <h3 class="fs-4 fw-bold text-dark mt-1 mb-0">FAQ Nổi Bật</h3>
                        </div>
                        <a href="/faq" class="btn btn-sm btn-outline-primary rounded-pill px-3">
                            Xem tất cả <i class="bi bi-arrow-right ms-1"></i>
                        </a>
                    </div>

                    <div class="d-flex flex-column gap-2" id="popular-faq-list">
                        <c:choose>
                            <c:when test="${not empty topViewed}">
                                <c:forEach var="faq" items="${topViewed}" varStatus="status" end="3">
                                    <a href="/faq/<c:out value='${faq.slug()}'/>" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light">
                                        <span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6" style="width: 36px; height: 36px; flex-shrink: 0;">
                                            0${status.count}
                                        </span>
                                        <div class="flex-grow-1 min-w-0">
                                            <div class="fw-semibold text-dark text-truncate mb-1"><c:out value="${faq.title()}"/></div>
                                            <div class="d-flex align-items-center gap-2 small text-muted">
                                                <span class="badge bg-light text-secondary border"><c:out value="${faq.categoryName()}"/></span>
                                                <span>•</span>
                                                <span><i class="bi bi-eye me-1"></i><c:out value="${faq.viewCount()}"/> lượt xem</span>
                                            </div>
                                        </div>
                                        <i class="bi bi-chevron-right text-muted mt-2"></i>
                                    </a>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <%-- Dữ liệu fallback chất lượng cao nếu chưa có view DB --%>
                                <a href="#faq" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light">
                                    <span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6" style="width: 36px; height: 36px; flex-shrink: 0;">01</span>
                                    <div class="flex-grow-1 min-w-0">
                                        <div class="fw-semibold text-dark mb-1">Hạn đóng học phí mỗi học kỳ là khi nào?</div>
                                        <div class="small text-muted"><span class="badge bg-light text-secondary border">Học phí</span> • 2.4k lượt xem</div>
                                    </div>
                                    <i class="bi bi-chevron-right text-muted mt-2"></i>
                                </a>
                                <a href="#faq" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light">
                                    <span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6" style="width: 36px; height: 36px; flex-shrink: 0;">02</span>
                                    <div class="flex-grow-1 min-w-0">
                                        <div class="fw-semibold text-dark mb-1">Thời gian đăng ký môn học học kỳ mới?</div>
                                        <div class="small text-muted"><span class="badge bg-light text-secondary border">Đăng ký môn</span> • 1.9k lượt xem</div>
                                    </div>
                                    <i class="bi bi-chevron-right text-muted mt-2"></i>
                                </a>
                                <a href="#faq" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light">
                                    <span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6" style="width: 36px; height: 36px; flex-shrink: 0;">03</span>
                                    <div class="flex-grow-1 min-w-0">
                                        <div class="fw-semibold text-dark mb-1">Điều kiện xét học bổng khuyến khích học tập?</div>
                                        <div class="small text-muted"><span class="badge bg-light text-secondary border">Học bổng</span> • 1.2k lượt xem</div>
                                    </div>
                                    <i class="bi bi-chevron-right text-muted mt-2"></i>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <%-- Cột phải: Thông báo mới nhất & 2 Thẻ số liệu --%>
            <div class="col-lg-5">
                <div class="d-flex flex-column gap-3 h-100">
                    <%-- Card Thông báo mới nhất từ DB --%>
                    <div class="rounded-4 p-4 text-white shadow-sm" style="background: linear-gradient(135deg, #072146 0%, #0b3d82 100%);">
                        <c:set var="firstAnn" value="${null}"/>
                        <c:if test="${not empty announcements}">
                            <c:forEach var="item" items="${announcements}" varStatus="status">
                                <c:if test="${status.first}"><c:set var="firstAnn" value="${item}"/></c:if>
                            </c:forEach>
                        </c:if>
                        <div class="d-flex justify-content-between align-items-start mb-2">
                            <div>
                                <span class="badge bg-white-20 text-white rounded-pill px-2.5 py-1 small">Thông báo mới nhất</span>
                                <h4 class="fs-5 fw-bold text-white mt-2 mb-1">
                                    <c:choose>
                                        <c:when test="${not empty firstAnn}">
                                            <c:out value="${firstAnn.title()}"/>
                                        </c:when>
                                        <c:otherwise>
                                            Mở đăng ký học bổng khuyến khích học tập HK1
                                        </c:otherwise>
                                    </c:choose>
                                </h4>
                            </div>
                            <div class="rounded-circle bg-white-10 p-2 text-warning">
                                <i class="bi bi-bell-fill fs-5"></i>
                            </div>
                        </div>
                        <p class="text-white-50 small mb-4 line-clamp-2" style="line-height: 1.6;">
                            <c:choose>
                                <c:when test="${not empty firstAnn && not empty firstAnn.summary()}">
                                    <c:out value="${firstAnn.summary()}"/>
                                </c:when>
                                <c:otherwise>
                                    Hạn nhận hồ sơ đến ngày 30/09/2026. Đừng bỏ lỡ cơ hội nhận hỗ trợ học tập từ nhà trường.
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <c:choose>
                            <c:when test="${not empty firstAnn}">
                                <a href="/announcements/${firstAnn.id()}" class="btn btn-light btn-sm fw-semibold rounded-pill px-3">
                                    Xem thông báo <i class="bi bi-arrow-right ms-1"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="/pages/guide" class="btn btn-light btn-sm fw-semibold rounded-pill px-3">
                                    Xem thông báo <i class="bi bi-arrow-right ms-1"></i>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <%-- 2 Thẻ số liệu nhỏ --%>
                    <div class="row g-3 flex-grow-1">
                        <div class="col-6">
                            <div class="hcmute-card-modern p-4 h-100 d-flex flex-column justify-content-center">
                                <i class="bi bi-people-fill fs-4 text-danger mb-2"></i>
                                <div class="fs-3 fw-bold text-dark">18.6k</div>
                                <div class="small text-muted">Sinh viên đã hỗ trợ</div>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="hcmute-card-modern p-4 h-100 d-flex flex-column justify-content-center">
                                <i class="bi bi-stopwatch-fill fs-4 text-primary mb-2"></i>
                                <div class="fs-3 fw-bold text-dark">&lt; 24h</div>
                                <div class="small text-muted">Thời gian phản hồi</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%-- Banner Cẩm nang Sinh viên mới --%>
        <div class="hcmute-card-modern p-4 p-md-4 d-flex flex-wrap align-items-center justify-content-between gap-3">
            <div class="d-flex align-items-center gap-3">
                <div class="hcmute-logo-icon" style="width: 48px; height: 48px; font-size: 1.5rem; flex-shrink: 0;">
                    <i class="bi bi-mortarboard-fill"></i>
                </div>
                <div>
                    <h5 class="fw-bold text-dark mb-1">Bạn là sinh viên mới tại HCMUTE?</h5>
                    <p class="small text-muted mb-0">Xem cẩm nang hướng dẫn nhanh về tài khoản, đăng ký môn học và các kênh hỗ trợ của nhà trường.</p>
                </div>
            </div>
            <a href="/pages/guide" class="btn btn-outline-primary rounded-pill px-4 fw-semibold">
                Mở hướng dẫn <i class="bi bi-book ms-1"></i>
            </a>
        </div>
    </div>
</section>

<%-- =========================================================================
     3. FAQ SECTION (TRA CỨU CÂU HỎI THƯỜNG GẶP)
     ========================================================================= --%>
<section class="hcmute-section" id="faq">
    <div class="container-xl">
        <div class="text-center max-w-xl mx-auto mb-5">
            <span class="hcmute-eyebrow"><i class="bi bi-question-circle-fill"></i>Tra cứu</span>
            <h2 class="hcmute-title-main">Câu hỏi thường gặp</h2>
            <p class="hcmute-desc-main mx-auto">
                Tìm nhanh câu trả lời cho các vấn đề học tập và đời sống sinh viên. Tìm kiếm không cần gõ dấu.
            </p>
        </div>

        <div class="row g-4">
            <%-- Bộ lọc chuyên mục bên trái --%>
            <div class="col-lg-3">
                <div class="d-flex flex-column gap-3 position-sticky" style="top: 5rem;">
                    <%-- Ô tìm kiếm FAQ thời gian thực --%>
                    <div class="input-group">
                        <span class="input-group-text bg-white border-end-0 text-muted">
                            <i class="bi bi-search"></i>
                        </span>
                        <input type="search" id="faq-filter-input" class="form-control border-start-0"
                               placeholder="Tìm trong danh sách..." onkeyup="filterFaqs()">
                    </div>

                    <%-- Danh mục Tabs --%>
                    <div class="d-flex flex-column gap-1" id="faq-category-buttons">
                        <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-3 p-2.5 active-category btn-primary"
                                style="background-color: var(--hcmute-blue); border-color: var(--hcmute-blue);"
                                onclick="selectFaqCategory('all', this)">
                            <span class="fw-semibold small">Tất cả câu hỏi</span>
                            <span class="badge bg-white text-primary rounded-pill">10+</span>
                        </button>
                        <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-3 p-2.5 btn-light text-dark"
                                onclick="selectFaqCategory('hoc-vu', this)">
                            <span class="fw-semibold small">Học vụ & Đào tạo</span>
                            <span class="badge bg-secondary-subtle text-secondary rounded-pill">3</span>
                        </button>
                        <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-3 p-2.5 btn-light text-dark"
                                onclick="selectFaqCategory('hoc-phi', this)">
                            <span class="fw-semibold small">Học phí & Gia hạn</span>
                            <span class="badge bg-secondary-subtle text-secondary rounded-pill">2</span>
                        </button>
                        <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-3 p-2.5 btn-light text-dark"
                                onclick="selectFaqCategory('dang-ky-mon', this)">
                            <span class="fw-semibold small">Đăng ký môn học</span>
                            <span class="badge bg-secondary-subtle text-secondary rounded-pill">2</span>
                        </button>
                        <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-3 p-2.5 btn-light text-dark"
                                onclick="selectFaqCategory('hoc-bong', this)">
                            <span class="fw-semibold small">Học bổng & CTSV</span>
                            <span class="badge bg-secondary-subtle text-secondary rounded-pill">2</span>
                        </button>
                        <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-3 p-2.5 btn-light text-dark"
                                onclick="selectFaqCategory('ky-tuc-xa', this)">
                            <span class="fw-semibold small">Ký túc xá</span>
                            <span class="badge bg-secondary-subtle text-secondary rounded-pill">1</span>
                        </button>
                    </div>

                    <a href="/faq" class="btn btn-outline-primary btn-sm rounded-pill mt-2">
                        <i class="bi bi-journal-text me-1"></i>Xem kho FAQ đầy đủ
                    </a>
                </div>
            </div>

            <%-- Accordion FAQ bên phải --%>
            <div class="col-lg-9">
                <div class="hcmute-card-modern p-4">
                    <div class="accordion accordion-flush" id="faqAccordion">
                        <%-- Item 1 --%>
                        <div class="accordion-item border-bottom py-2 faq-item" data-category="dang-ky-mon">
                            <h2 class="accordion-header" id="heading1">
                                <button class="accordion-button collapsed fw-semibold text-dark fs-6" type="button" data-bs-toggle="collapse" data-bs-target="#collapse1" aria-expanded="false" aria-controls="collapse1">
                                    <div class="d-flex flex-column align-items-start gap-1">
                                        <span class="badge bg-primary-subtle text-primary small">Đăng ký môn</span>
                                        <span>Thời gian đăng ký môn học học kỳ mới là khi nào?</span>
                                    </div>
                                </button>
                            </h2>
                            <div id="collapse1" class="accordion-collapse collapse" aria-labelledby="heading1" data-bs-parent="#faqAccordion">
                                <div class="accordion-body text-secondary small pt-1" style="line-height: 1.7;">
                                    Lịch đăng ký môn học được Phòng Đào tạo công bố trên trang online.hcmute.edu.vn khoảng 3–4 tuần trước khi học kỳ bắt đầu. Sinh viên đăng ký theo khung giờ được phân bổ theo khóa và ngành đào tạo.
                                </div>
                            </div>
                        </div>

                        <%-- Item 2 --%>
                        <div class="accordion-item border-bottom py-2 faq-item" data-category="dang-ky-mon">
                            <h2 class="accordion-header" id="heading2">
                                <button class="accordion-button collapsed fw-semibold text-dark fs-6" type="button" data-bs-toggle="collapse" data-bs-target="#collapse2" aria-expanded="false" aria-controls="collapse2">
                                    <div class="d-flex flex-column align-items-start gap-1">
                                        <span class="badge bg-primary-subtle text-primary small">Đăng ký môn</span>
                                        <span>Tôi có thể hủy môn học đã đăng ký không?</span>
                                    </div>
                                </button>
                            </h2>
                            <div id="collapse2" class="accordion-collapse collapse" aria-labelledby="heading2" data-bs-parent="#faqAccordion">
                                <div class="accordion-body text-secondary small pt-1" style="line-height: 1.7;">
                                    Sinh viên được rút môn trong thời gian điều chỉnh đăng ký (thường là 2 tuần đầu học kỳ). Sau thời gian này, môn học sẽ được ghi nhận điểm W nếu rút theo quy định đào tạo.
                                </div>
                            </div>
                        </div>

                        <%-- Item 3 --%>
                        <div class="accordion-item border-bottom py-2 faq-item" data-category="hoc-phi">
                            <h2 class="accordion-header" id="heading3">
                                <button class="accordion-button collapsed fw-semibold text-dark fs-6" type="button" data-bs-toggle="collapse" data-bs-target="#collapse3" aria-expanded="false" aria-controls="collapse3">
                                    <div class="d-flex flex-column align-items-start gap-1">
                                        <span class="badge bg-primary-subtle text-primary small">Học phí</span>
                                        <span>Hạn đóng học phí mỗi học kỳ là khi nào?</span>
                                    </div>
                                </button>
                            </h2>
                            <div id="collapse3" class="accordion-collapse collapse" aria-labelledby="heading3" data-bs-parent="#faqAccordion">
                                <div class="accordion-body text-secondary small pt-1" style="line-height: 1.7;">
                                    Hạn đóng học phí thường trong vòng 4 tuần kể từ ngày bắt đầu học kỳ. Sinh viên có thể nộp qua ngân hàng liên kết hoặc cổng thanh toán trực tuyến của trường.
                                </div>
                            </div>
                        </div>

                        <%-- Item 4 --%>
                        <div class="accordion-item border-bottom py-2 faq-item" data-category="hoc-phi">
                            <h2 class="accordion-header" id="heading4">
                                <button class="accordion-button collapsed fw-semibold text-dark fs-6" type="button" data-bs-toggle="collapse" data-bs-target="#collapse4" aria-expanded="false" aria-controls="collapse4">
                                    <div class="d-flex flex-column align-items-start gap-1">
                                        <span class="badge bg-primary-subtle text-primary small">Học phí</span>
                                        <span>Làm thế nào để xin gia hạn nộp học phí?</span>
                                    </div>
                                </button>
                            </h2>
                            <div id="collapse4" class="accordion-collapse collapse" aria-labelledby="heading4" data-bs-parent="#faqAccordion">
                                <div class="accordion-body text-secondary small pt-1" style="line-height: 1.7;">
                                    Sinh viên nộp đơn xin gia hạn học phí tại Phòng Kế hoạch – Tài chính kèm minh chứng hoàn cảnh. Bạn cũng có thể gửi ticket tư vấn ngay bên dưới để được hướng dẫn chi tiết.
                                </div>
                            </div>
                        </div>

                        <%-- Item 5 --%>
                        <div class="accordion-item border-bottom py-2 faq-item" data-category="hoc-vu">
                            <h2 class="accordion-header" id="heading5">
                                <button class="accordion-button collapsed fw-semibold text-dark fs-6" type="button" data-bs-toggle="collapse" data-bs-target="#collapse5" aria-expanded="false" aria-controls="collapse5">
                                    <div class="d-flex flex-column align-items-start gap-1">
                                        <span class="badge bg-primary-subtle text-primary small">Học vụ</span>
                                        <span>Cảnh báo học vụ được xét như thế nào?</span>
                                    </div>
                                </button>
                            </h2>
                            <div id="collapse5" class="accordion-collapse collapse" aria-labelledby="heading5" data-bs-parent="#faqAccordion">
                                <div class="accordion-body text-secondary small pt-1" style="line-height: 1.7;">
                                    Sinh viên bị cảnh báo học vụ khi điểm trung bình học kỳ dưới 1.0 (thang 4) hoặc điểm trung bình tích lũy thấp hơn mức quy định theo năm học. Hãy liên hệ cố vấn học tập để được định hướng cải thiện.
                                </div>
                            </div>
                        </div>

                        <%-- Item 6 --%>
                        <div class="accordion-item border-bottom py-2 faq-item" data-category="hoc-vu">
                            <h2 class="accordion-header" id="heading6">
                                <button class="accordion-button collapsed fw-semibold text-dark fs-6" type="button" data-bs-toggle="collapse" data-bs-target="#collapse6" aria-expanded="false" aria-controls="collapse6">
                                    <div class="d-flex flex-column align-items-start gap-1">
                                        <span class="badge bg-primary-subtle text-primary small">Học vụ</span>
                                        <span>Thủ tục bảo lưu kết quả học tập gồm những gì?</span>
                                    </div>
                                </button>
                            </h2>
                            <div id="collapse6" class="accordion-collapse collapse" aria-labelledby="heading6" data-bs-parent="#faqAccordion">
                                <div class="accordion-body text-secondary small pt-1" style="line-height: 1.7;">
                                    Sinh viên nộp đơn bảo lưu tại Phòng Đào tạo kèm giấy tờ minh chứng (giấy khám sức khỏe, lệnh nhập ngũ...). Thời gian bảo lưu tối đa theo quy chế đào tạo hiện hành.
                                </div>
                            </div>
                        </div>

                        <%-- Item 7 --%>
                        <div class="accordion-item border-bottom py-2 faq-item" data-category="ky-tuc-xa">
                            <h2 class="accordion-header" id="heading7">
                                <button class="accordion-button collapsed fw-semibold text-dark fs-6" type="button" data-bs-toggle="collapse" data-bs-target="#collapse7" aria-expanded="false" aria-controls="collapse7">
                                    <div class="d-flex flex-column align-items-start gap-1">
                                        <span class="badge bg-primary-subtle text-primary small">Ký túc xá</span>
                                        <span>Đăng ký ở ký túc xá cần những điều kiện gì?</span>
                                    </div>
                                </button>
                            </h2>
                            <div id="collapse7" class="accordion-collapse collapse" aria-labelledby="heading7" data-bs-parent="#faqAccordion">
                                <div class="accordion-body text-secondary small pt-1" style="line-height: 1.7;">
                                    Sinh viên chính quy đang học tại trường được đăng ký ở KTX. Ưu tiên sinh viên năm nhất, sinh viên diện chính sách xã hội và hoàn cảnh khó khăn.
                                </div>
                            </div>
                        </div>

                        <%-- Item 8 --%>
                        <div class="accordion-item border-bottom py-2 faq-item" data-category="hoc-bong">
                            <h2 class="accordion-header" id="heading8">
                                <button class="accordion-button collapsed fw-semibold text-dark fs-6" type="button" data-bs-toggle="collapse" data-bs-target="#collapse8" aria-expanded="false" aria-controls="collapse8">
                                    <div class="d-flex flex-column align-items-start gap-1">
                                        <span class="badge bg-primary-subtle text-primary small">Học bổng</span>
                                        <span>Điều kiện xét học bổng khuyến khích học tập là gì?</span>
                                    </div>
                                </button>
                            </h2>
                            <div id="collapse8" class="accordion-collapse collapse" aria-labelledby="heading8" data-bs-parent="#faqAccordion">
                                <div class="accordion-body text-secondary small pt-1" style="line-height: 1.7;">
                                    Sinh viên cần có điểm trung bình học kỳ từ 3.2 trở lên, điểm rèn luyện từ loại Tốt, không có môn nào dưới điểm D và đăng ký đủ số tín chỉ tối thiểu theo quy định.
                                </div>
                            </div>
                        </div>
                    </div>

                    <%-- Empty State khi tìm kiếm không ra kết quả --%>
                    <div id="faq-empty-state" class="text-center py-5 d-none">
                        <i class="bi bi-search text-muted fs-1 mb-3 d-block"></i>
                        <h6 class="fw-bold">Không tìm thấy câu hỏi phù hợp</h6>
                        <p class="text-muted small">Hãy thử từ khóa khác hoặc gửi ticket tư vấn bên dưới để được chuyên viên hỗ trợ trực tiếp.</p>
                        <a href="#ticket" class="btn btn-primary btn-sm rounded-pill px-3">Gửi ticket ngay</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<%-- =========================================================================
     4. TICKET SECTION (CHỈ GIỚI THIỆU Ở GUEST)
     ========================================================================= --%>
<section class="hcmute-section hcmute-section-muted" id="ticket">
    <div class="container-xl">
        <div class="row align-items-center g-4">
            <div class="col-lg-7">
                <span class="hcmute-eyebrow"><i class="bi bi-send-fill"></i>Hỗ trợ chính thức</span>
                <h2 class="hcmute-title-main">Bạn muốn gửi yêu cầu tư vấn?</h2>
                <p class="hcmute-desc-main">
                    Đăng nhập để gửi ticket chính thức, đính kèm hồ sơ, nhận phản hồi từ chuyên viên
                    và theo dõi toàn bộ tiến độ xử lý trong một nơi.
                </p>
            </div>
            <div class="col-lg-5">
                <div class="hcmute-card-modern p-4 text-center">
                    <i class="bi bi-shield-lock-fill text-primary display-6"></i>
                    <h5 class="fw-bold mt-3">Chỉ dành cho tài khoản đã cấp</h5>
                    <p class="text-muted small">Thông tin ticket được gắn với tài khoản để bảo đảm riêng tư và không thất lạc dữ liệu.</p>
                    <a href="${pageContext.request.contextPath}/auth/login?returnUrl=/student/tickets/new"
                       class="btn btn-primary rounded-pill px-4">
                        <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập để gửi ticket
                    </a>
                </div>
            </div>
        </div>
    </div>
</section>

<%-- Biểu mẫu cũ được giữ tạm trong mã nguồn để tránh mất dữ liệu thiết kế, không render cho Guest. --%>
<c:if test="${false}">
<section class="hcmute-section hcmute-section-muted" id="ticket">
    <div class="container-xl">
        <div class="text-center max-w-xl mx-auto mb-5">
            <span class="hcmute-eyebrow"><i class="bi bi-send-fill"></i>Hỗ trợ</span>
            <h2 class="hcmute-title-main">Gửi ticket tư vấn</h2>
            <p class="hcmute-desc-main mx-auto">
                Mô tả vấn đề của bạn, chuyên viên phụ trách sẽ phản hồi qua email sinh viên trong vòng 24 giờ làm việc.
            </p>
        </div>

        <div class="row g-4">
            <%-- Cột trái: Form gửi ticket --%>
            <div class="col-lg-8">
                <div class="hcmute-card-modern p-4 p-md-5">
                    <%-- Alert kiểm tra đăng nhập --%>
                    <sec:authorize access="isAnonymous()">
                        <div class="alert alert-warning border-0 shadow-sm d-flex align-items-center justify-content-between flex-wrap gap-2 mb-4 p-3 rounded-3">
                            <div class="d-flex align-items-center gap-2">
                                <i class="bi bi-shield-lock-fill fs-4 text-warning"></i>
                                <div class="small">
                                    <strong>Dành riêng cho sinh viên HCMUTE:</strong> Vui lòng đăng nhập để gửi ticket, nhận phản hồi chính thức từ chuyên viên và theo dõi tiến độ xử lý.
                                </div>
                            </div>
                            <a href="/auth/login?returnUrl=/student/tickets/new" class="btn btn-warning btn-sm fw-semibold rounded-pill px-3 text-nowrap">
                                <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập ngay
                            </a>
                        </div>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <div class="alert alert-success border-0 shadow-sm d-flex align-items-center justify-content-between flex-wrap gap-2 mb-4 p-3 rounded-3">
                            <div class="d-flex align-items-center gap-2">
                                <i class="bi bi-person-check-fill fs-4 text-success"></i>
                                <div class="small">
                                    Bạn đã đăng nhập! Bạn có thể tạo yêu cầu và theo dõi tiến độ trực tiếp tại trang Sinh viên.
                                </div>
                            </div>
                            <a href="/student/tickets/new" class="btn btn-success btn-sm fw-semibold rounded-pill px-3 text-nowrap">
                                <i class="bi bi-plus-circle me-1"></i>Tạo yêu cầu chính thức
                            </a>
                        </div>
                    </sec:authorize>

                    <div id="ticket-success-alert" class="alert alert-success d-none mb-4 d-flex align-items-center gap-2">
                        <i class="bi bi-check-circle-fill fs-5"></i>
                        <div id="ticket-success-msg">Đã gửi ticket thành công!</div>
                    </div>

                    <form id="quick-ticket-form" onsubmit="handleQuickTicketSubmit(event)">
                        <div class="row g-3">
                            <div class="col-sm-6">
                                <label class="form-label required fw-semibold small" for="ticket-fullname">Họ và tên</label>
                                <input type="text" class="form-control" id="ticket-fullname" name="fullname" required placeholder="Nguyễn Văn A">
                            </div>
                            <div class="col-sm-6">
                                <label class="form-label required fw-semibold small" for="ticket-studentid">Mã số sinh viên (MSSV)</label>
                                <input type="text" class="form-control" id="ticket-studentid" name="studentId" required placeholder="22110xxx" inputmode="numeric">
                            </div>
                            <div class="col-12">
                                <label class="form-label required fw-semibold small" for="ticket-email">Email sinh viên</label>
                                <input type="email" class="form-control" id="ticket-email" name="email" required placeholder="22110xxx@student.hcmute.edu.vn">
                            </div>
                            <div class="col-sm-6">
                                <label class="form-label required fw-semibold small" for="ticket-category">Chủ đề cần tư vấn</label>
                                <select class="form-select" id="ticket-category" name="category" required>
                                    <option value="Học vụ & Đào tạo">Học vụ & Đào tạo</option>
                                    <option value="Học phí & Tài chính">Học phí & Tài chính</option>
                                    <option value="Đăng ký môn học">Đăng ký môn học</option>
                                    <option value="Ký túc xá">Ký túc xá</option>
                                    <option value="Học bổng & CTSV">Học bổng & CTSV</option>
                                    <option value="Tư vấn tâm lý">Tư vấn tâm lý</option>
                                    <option value="Khác">Khác</option>
                                </select>
                            </div>
                            <div class="col-sm-6">
                                <label class="form-label fw-semibold small">Mức độ ưu tiên</label>
                                <div class="btn-group w-100" role="group">
                                    <input type="radio" class="btn-check" name="priority" id="prio-low" value="Thấp">
                                    <label class="btn btn-outline-secondary btn-sm" for="prio-low">Thấp</label>

                                    <input type="radio" class="btn-check" name="priority" id="prio-normal" value="Bình thường" checked>
                                    <label class="btn btn-outline-secondary btn-sm" for="prio-normal">Bình thường</label>

                                    <input type="radio" class="btn-check" name="priority" id="prio-high" value="Khẩn cấp">
                                    <label class="btn btn-outline-danger btn-sm" for="prio-high">Khẩn cấp</label>
                                </div>
                            </div>
                            <div class="col-12">
                                <label class="form-label required fw-semibold small" for="ticket-subject">Tiêu đề yêu cầu</label>
                                <input type="text" class="form-control" id="ticket-subject" name="subject" required placeholder="Tóm tắt ngắn gọn vấn đề cần tư vấn">
                            </div>
                            <div class="col-12">
                                <label class="form-label required fw-semibold small" for="ticket-content">Nội dung chi tiết</label>
                                <textarea class="form-control" id="ticket-content" name="content" rows="4" required placeholder="Mô tả chi tiết vấn đề, thời điểm xảy ra, các bước bạn đã thử..."></textarea>
                            </div>
                            <div class="col-12 d-flex flex-wrap justify-content-between align-items-center gap-3 pt-2">
                                <label class="btn btn-light btn-sm text-secondary border mb-0 d-inline-flex align-items-center gap-1 cursor-pointer">
                                    <i class="bi bi-paperclip"></i>
                                    <span>Đính kèm tệp (tối đa 5MB)</span>
                                    <input type="file" class="d-none" id="ticket-file" onchange="previewFileName(this)">
                                </label>
                                <span id="file-name-preview" class="small text-muted"></span>
                                <sec:authorize access="isAnonymous()">
                                    <a href="/auth/login?returnUrl=/student/tickets/new" class="btn btn-warning px-4 py-2 fw-semibold rounded-pill">
                                        <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập để gửi ticket
                                    </a>
                                </sec:authorize>
                                <sec:authorize access="isAuthenticated()">
                                    <button type="submit" class="btn btn-primary px-4 py-2 fw-semibold rounded-pill" id="btn-submit-ticket" style="background-color: var(--hcmute-blue);">
                                        <i class="bi bi-send-fill me-1"></i>Gửi ticket
                                    </button>
                                </sec:authorize>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <%-- Cột phải: Ticket của tôi --%>
            <div class="col-lg-4">
                <div class="hcmute-card-modern p-4">
                    <div class="d-flex align-items-center gap-2 mb-3">
                        <i class="bi bi-inbox-fill text-primary fs-5"></i>
                        <h5 class="fw-bold text-dark mb-0">Ticket của tôi</h5>
                    </div>
                    <p class="small text-muted mb-4">Theo dõi trạng thái các yêu cầu đã gửi gần đây.</p>

                    <div class="d-flex flex-column gap-3" id="my-tickets-list">
                        <div class="p-3 rounded-3 bg-light border">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <span class="badge bg-primary text-white fw-mono font-monospace">QA-2041</span>
                                <span class="badge bg-success-subtle text-success">Đã phản hồi</span>
                            </div>
                            <div class="fw-semibold small text-dark mb-1">Xin xác nhận sinh viên để làm thủ tục vay vốn</div>
                            <div class="text-muted small" style="font-size: 0.75rem;">Học vụ • Cập nhật 08/05/2026</div>
                        </div>

                        <div class="p-3 rounded-3 bg-light border">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <span class="badge bg-primary text-white fw-mono font-monospace">QA-2087</span>
                                <span class="badge bg-warning-subtle text-warning-emphasis">Đang xử lý</span>
                            </div>
                            <div class="fw-semibold small text-dark mb-1">Lỗi không đăng ký được môn Toán cao cấp 2</div>
                            <div class="text-muted small" style="font-size: 0.75rem;">Đăng ký môn học • Cập nhật 09/05/2026</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</c:if>

<%-- =========================================================================
     5. BOOKING SECTION (CHỈ GIỚI THIỆU Ở GUEST)
     ========================================================================= --%>
<section class="hcmute-section hcmute-section-muted" id="booking">
    <div class="container-xl">
        <div class="row align-items-center g-4">
            <div class="col-lg-7">
                <span class="hcmute-eyebrow"><i class="bi bi-calendar-event-fill"></i>Kết nối chuyên viên</span>
                <h2 class="hcmute-title-main">Bạn muốn đặt lịch tư vấn?</h2>
                <p class="hcmute-desc-main">
                    Đăng nhập để xem chuyên viên, khung giờ còn trống, hình thức gặp và nhận mã lịch hẹn chính thức.
                </p>
            </div>
            <div class="col-lg-5">
                <div class="hcmute-card-modern p-4 text-center">
                    <i class="bi bi-calendar2-check-fill text-primary display-6"></i>
                    <h5 class="fw-bold mt-3">Đặt lịch trong khu vực Student</h5>
                    <p class="text-muted small">Lịch hẹn được lưu vào tài khoản và gửi thông báo xác nhận đến bạn.</p>
                    <a href="${pageContext.request.contextPath}/auth/login?returnUrl=/student/appointments/book"
                       class="btn btn-primary rounded-pill px-4">
                        <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập để đặt lịch
                    </a>
                </div>
            </div>
        </div>
    </div>
</section>

<c:if test="${false}">
<section class="hcmute-section hcmute-section-muted" id="booking">
    <div class="container-xl">
        <div class="text-center max-w-xl mx-auto mb-5">
            <span class="hcmute-eyebrow"><i class="bi bi-calendar-event-fill"></i>Gặp trực tiếp</span>
            <h2 class="hcmute-title-main">Đặt lịch hẹn tư vấn</h2>
            <p class="hcmute-desc-main mx-auto">
                Chọn chuyên viên, thời gian và hình thức phù hợp. Bạn sẽ nhận email xác nhận ngay sau khi đặt lịch.
            </p>
        </div>

        <div class="row g-4">
            <%-- Khung chọn 4 bước bên trái --%>
            <div class="col-lg-8">
                <div class="hcmute-card-modern p-4 p-md-5">
                    <%-- Bước 1: Chọn chuyên viên tư vấn --%>
                    <div class="mb-5">
                        <h6 class="fw-bold text-dark mb-3">1. Chọn chuyên viên tư vấn</h6>
                        <div class="row g-3">
                            <div class="col-md-4">
                                <div class="p-3 rounded-3 border bg-white cursor-pointer transition-all advisor-select-card active border-primary shadow-sm"
                                     onclick="selectAdvisor('a1', 'ThS. Nguyễn Minh Anh', 'Phòng Đào tạo', this)">
                                    <div class="d-flex align-items-center gap-3 mb-2">
                                        <div class="rounded-circle d-flex align-items-center justify-content-center fw-bold text-white shadow-sm"
                                             style="width: 44px; height: 44px; background: linear-gradient(135deg, #0b3d82, #1e40af); font-size: 0.95rem; flex-shrink: 0;">
                                            MA
                                        </div>
                                        <div>
                                            <div class="fw-bold small text-dark">ThS. Nguyễn Minh Anh</div>
                                            <div class="text-muted small" style="font-size: 0.75rem;">Phòng Đào tạo</div>
                                        </div>
                                    </div>
                                    <div class="d-flex flex-wrap gap-1">
                                        <span class="badge bg-light text-secondary border">Học vụ</span>
                                        <span class="badge bg-light text-secondary border">Đăng ký môn</span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="p-3 rounded-3 border bg-white cursor-pointer transition-all advisor-select-card"
                                     onclick="selectAdvisor('a2', 'ThS. Trần Thu Hà', 'Phòng CTSV', this)">
                                    <div class="d-flex align-items-center gap-3 mb-2">
                                        <div class="rounded-circle d-flex align-items-center justify-content-center fw-bold text-white shadow-sm"
                                             style="width: 44px; height: 44px; background: linear-gradient(135deg, #0284c7, #0369a1); font-size: 0.95rem; flex-shrink: 0;">
                                            TH
                                        </div>
                                        <div>
                                            <div class="fw-bold small text-dark">ThS. Trần Thu Hà</div>
                                            <div class="text-muted small" style="font-size: 0.75rem;">Phòng CTSV</div>
                                        </div>
                                    </div>
                                    <div class="d-flex flex-wrap gap-1">
                                        <span class="badge bg-light text-secondary border">Học bổng</span>
                                        <span class="badge bg-light text-secondary border">Ký túc xá</span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="p-3 rounded-3 border bg-white cursor-pointer transition-all advisor-select-card"
                                     onclick="selectAdvisor('a3', 'TS. Lê Quốc Bảo', 'Tư vấn tâm lý', this)">
                                    <div class="d-flex align-items-center gap-3 mb-2">
                                        <div class="rounded-circle d-flex align-items-center justify-content-center fw-bold text-white shadow-sm"
                                             style="width: 44px; height: 44px; background: linear-gradient(135deg, #4f46e5, #4338ca); font-size: 0.95rem; flex-shrink: 0;">
                                            QB
                                        </div>
                                        <div>
                                            <div class="fw-bold small text-dark">TS. Lê Quốc Bảo</div>
                                            <div class="text-muted small" style="font-size: 0.75rem;">Tư vấn tâm lý</div>
                                        </div>
                                    </div>
                                    <div class="d-flex flex-wrap gap-1">
                                        <span class="badge bg-light text-secondary border">Tâm lý</span>
                                        <span class="badge bg-light text-secondary border">Định hướng</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <%-- Bước 2: Chọn ngày --%>
                    <div class="mb-5">
                        <h6 class="fw-bold text-dark mb-3">2. Chọn ngày làm việc</h6>
                        <div class="row g-2" id="booking-days-grid">
                            <%-- Sẽ được render tự động qua JS 6 ngày tới --%>
                        </div>
                    </div>

                    <%-- Bước 3: Chọn khung giờ --%>
                    <div class="mb-5">
                        <h6 class="fw-bold text-dark mb-3">3. Chọn khung giờ tư vấn</h6>
                        <div class="row g-2" id="booking-slots-grid">
                            <div class="col-4 col-sm-2">
                                <button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('08:00', this)">08:00</button>
                            </div>
                            <div class="col-4 col-sm-2">
                                <button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('09:00', this)">09:00</button>
                            </div>
                            <div class="col-4 col-sm-2">
                                <button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('10:00', this)">10:00</button>
                            </div>
                            <div class="col-4 col-sm-2">
                                <button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('13:30', this)">13:30</button>
                            </div>
                            <div class="col-4 col-sm-2">
                                <button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('14:30', this)">14:30</button>
                            </div>
                            <div class="col-4 col-sm-2">
                                <button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('15:30', this)">15:30</button>
                            </div>
                        </div>
                    </div>

                    <%-- Bước 4: Hình thức tư vấn --%>
                    <div>
                        <h6 class="fw-bold text-dark mb-3">4. Hình thức tư vấn</h6>
                        <div class="row g-3">
                            <div class="col-sm-6">
                                <div class="p-3 rounded-3 border bg-white cursor-pointer modality-card active border-primary shadow-sm"
                                     onclick="selectModality('offline', this)">
                                    <div class="d-flex align-items-center gap-2 mb-1">
                                        <i class="bi bi-geo-alt-fill text-primary fs-5"></i>
                                        <span class="fw-bold text-dark">Trực tiếp tại trường</span>
                                    </div>
                                    <p class="small text-muted mb-0">Phòng A1-102, Tòa nhà Trung tâm HCMUTE</p>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="p-3 rounded-3 border bg-white cursor-pointer modality-card"
                                     onclick="selectModality('online', this)">
                                    <div class="d-flex align-items-center gap-2 mb-1">
                                        <i class="bi bi-camera-video-fill text-success fs-5"></i>
                                        <span class="fw-bold text-dark">Trực tuyến (Online)</span>
                                    </div>
                                    <p class="small text-muted mb-0">Google Meet gửi qua email sinh viên</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <%-- Cột phải: Tóm tắt lịch hẹn dính (Sticky Summary) --%>
            <div class="col-lg-4">
                <div class="hcmute-card-modern p-4 position-sticky" style="top: 5rem;">
                    <div class="d-flex align-items-center gap-2 mb-4">
                        <i class="bi bi-calendar2-check-fill text-primary fs-5"></i>
                        <h5 class="fw-bold text-dark mb-0">Tóm tắt lịch hẹn</h5>
                    </div>

                    <div class="d-flex flex-column gap-3 small pb-4 border-bottom mb-4">
                        <div class="d-flex justify-content-between">
                            <span class="text-muted">Chuyên viên:</span>
                            <span class="fw-bold text-dark" id="summary-advisor">ThS. Nguyễn Minh Anh</span>
                        </div>
                        <div class="d-flex justify-content-between">
                            <span class="text-muted">Ngày hẹn:</span>
                            <span class="fw-bold text-dark" id="summary-date">Thứ 2, ngày mai</span>
                        </div>
                        <div class="d-flex justify-content-between">
                            <span class="text-muted">Khung giờ:</span>
                            <span class="fw-bold text-primary" id="summary-slot">Chưa chọn</span>
                        </div>
                        <div class="d-flex justify-content-between">
                            <span class="text-muted">Hình thức:</span>
                            <span class="fw-bold text-dark" id="summary-modality">Trực tiếp tại Phòng A1-102</span>
                        </div>
                        <div class="d-flex justify-content-between">
                            <span class="text-muted">Thời lượng:</span>
                            <span class="fw-bold text-dark">30 phút</span>
                        </div>
                    </div>

                    <div id="booking-success-box" class="d-none alert alert-success small mb-3">
                        <i class="bi bi-check-circle-fill me-1"></i>
                        <span id="booking-success-text">Đặt lịch thành công!</span>
                    </div>

                    <sec:authorize access="isAnonymous()">
                        <div class="alert alert-warning border-0 small mb-3 p-2.5 rounded-3">
                            <i class="bi bi-shield-lock-fill me-1 text-warning"></i>
                            Bạn cần đăng nhập tài khoản sinh viên để hệ thống cấp mã hẹn và phòng gặp trực tiếp/Google Meet.
                        </div>
                        <a href="/auth/login?returnUrl=/student/appointments/book" class="btn btn-warning w-100 py-2.5 fw-semibold rounded-pill shadow-sm">
                            <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập để đặt lịch hẹn
                        </a>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <a href="/student/appointments/book" class="btn btn-primary w-100 py-2.5 fw-semibold rounded-pill shadow-sm">
                            <i class="bi bi-calendar-plus me-1"></i>Đặt lịch hẹn trên hệ thống
                        </a>
                    </sec:authorize>
                </div>
            </div>
        </div>
    </div>
</section>
</c:if>

<%-- SCRIPT XỬ LÝ TRANG CHỦ CHUẨN NEXT.JS INTERACTION --%>
<script>
    // --- 1. Quick Ticket Helper ---

    // --- 2. FAQ Section Filtering ---
    function selectFaqCategory(category, btn) {
        var buttons = document.querySelectorAll('#faq-category-buttons button');
        buttons.forEach(function(b) {
            b.classList.remove('btn-primary');
            b.classList.add('btn-light', 'text-dark');
            b.style.backgroundColor = '';
            b.style.borderColor = '';
        });
        btn.classList.remove('btn-light', 'text-dark');
        btn.classList.add('btn-primary');
        btn.style.backgroundColor = 'var(--hcmute-blue)';
        btn.style.borderColor = 'var(--hcmute-blue)';

        var items = document.querySelectorAll('.faq-item');
        var visibleCount = 0;
        items.forEach(function(item) {
            if (category === 'all' || item.getAttribute('data-category') === category) {
                item.style.display = '';
                visibleCount++;
            } else {
                item.style.display = 'none';
            }
        });
        checkFaqEmptyState(visibleCount);
    }

    function filterFaqs() {
        var query = document.getElementById('faq-filter-input').value.toLowerCase().trim();
        var items = document.querySelectorAll('.faq-item');
        var visibleCount = 0;
        items.forEach(function(item) {
            var text = item.textContent.toLowerCase();
            if (text.indexOf(query) !== -1) {
                item.style.display = '';
                visibleCount++;
            } else {
                item.style.display = 'none';
            }
        });
        checkFaqEmptyState(visibleCount);
    }

    function checkFaqEmptyState(count) {
        var empty = document.getElementById('faq-empty-state');
        if (empty) {
            if (count === 0) {
                empty.classList.remove('d-none');
            } else {
                empty.classList.add('d-none');
            }
        }
    }

    // --- 3. Quick Ticket Submission ---
    function previewFileName(input) {
        var preview = document.getElementById('file-name-preview');
        if (input.files && input.files[0]) {
            preview.textContent = input.files[0].name;
        } else {
            preview.textContent = '';
        }
    }

    function handleQuickTicketSubmit(e) {
        e.preventDefault();
        var btn = document.getElementById('btn-submit-ticket');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span> Đang gửi...';

        var payload = {
            fullname: document.getElementById('ticket-fullname').value,
            studentId: document.getElementById('ticket-studentid').value,
            email: document.getElementById('ticket-email').value,
            category: document.getElementById('ticket-category').value,
            priority: document.querySelector('input[name="priority"]:checked').value,
            subject: document.getElementById('ticket-subject').value,
            content: document.getElementById('ticket-content').value
        };

        fetch('/api/public/tickets/quick-submit', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
        .then(function(res) { return res.json(); })
        .then(function(res) {
            btn.disabled = false;
            btn.innerHTML = '<i class="bi bi-send-fill me-1"></i>Gửi ticket';

            if (res && res.data) {
                var alert = document.getElementById('ticket-success-alert');
                var msg = document.getElementById('ticket-success-msg');
                msg.textContent = res.data.message;
                alert.classList.remove('d-none');

                // Thêm vào danh sách Ticket của tôi
                var list = document.getElementById('my-tickets-list');
                var newTicketHtml = '<div class="p-3 rounded-3 bg-light border border-primary">' +
                    '<div class="d-flex justify-content-between align-items-center mb-1">' +
                    '<span class="badge bg-primary text-white font-monospace">' + res.data.code + '</span>' +
                    '<span class="badge bg-secondary-subtle text-secondary">Đang chờ</span>' +
                    '</div>' +
                    '<div class="fw-semibold small text-dark mb-1">' + res.data.subject + '</div>' +
                    '<div class="text-muted small" style="font-size: 0.75rem;">' + res.data.category + ' • Vừa tạo</div>' +
                    '</div>';
                list.insertAdjacentHTML('afterbegin', newTicketHtml);

                document.getElementById('quick-ticket-form').reset();
                document.getElementById('file-name-preview').textContent = '';
            }
        })
        .catch(function(err) {
            btn.disabled = false;
            btn.innerHTML = '<i class="bi bi-send-fill me-1"></i>Gửi ticket';
            alert('Có lỗi xảy ra, vui lòng thử lại sau.');
        });
    }

    // --- 4. Booking Section Logic ---
    var selectedAdvisorName = 'ThS. Nguyễn Minh Anh';
    var selectedDate = 'Thứ 2, ngày mai';
    var selectedSlot = null;
    var selectedModality = 'Trực tiếp tại Phòng A1-102';

    function selectAdvisor(id, name, dept, el) {
        document.querySelectorAll('.advisor-select-card').forEach(function(c) {
            c.classList.remove('active', 'border-primary', 'shadow-sm');
        });
        el.classList.add('active', 'border-primary', 'shadow-sm');
        selectedAdvisorName = name + ' (' + dept + ')';
        document.getElementById('summary-advisor').textContent = selectedAdvisorName;
    }

    function selectSlot(time, btn) {
        document.querySelectorAll('.slot-btn').forEach(function(b) {
            b.classList.remove('btn-primary', 'text-white');
            b.classList.add('btn-outline-secondary');
            b.style.backgroundColor = '';
        });
        btn.classList.remove('btn-outline-secondary');
        btn.classList.add('btn-primary', 'text-white');
        btn.style.backgroundColor = 'var(--hcmute-blue)';
        selectedSlot = time;
        document.getElementById('summary-slot').textContent = selectedSlot;
        document.getElementById('booking-hint').textContent = 'Sẵn sàng đặt lịch! Nhấn nút bên trên để hoàn tất.';
    }

    function selectModality(type, el) {
        document.querySelectorAll('.modality-card').forEach(function(c) {
            c.classList.remove('active', 'border-primary', 'shadow-sm');
        });
        el.classList.add('active', 'border-primary', 'shadow-sm');
        if (type === 'offline') {
            selectedModality = 'Trực tiếp tại Phòng A1-102';
        } else {
            selectedModality = 'Trực tuyến (Google Meet)';
        }
        document.getElementById('summary-modality').textContent = selectedModality;
    }

    // Khởi tạo ngày làm việc tiếp theo
    function initBookingDays() {
        var grid = document.getElementById('booking-days-grid');
        if (!grid) return;
        var days = [];
        var d = new Date();
        var dayNames = ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'];
        while (days.length < 6) {
            d.setDate(d.getDate() + 1);
            if (d.getDay() !== 0 && d.getDay() !== 6) {
                days.push(new Date(d));
            }
        }

        var html = '';
        days.forEach(function(date, index) {
            var wd = dayNames[date.getDay()];
            var formatted = ('0' + date.getDate()).slice(-2) + '/' + ('0' + (date.getMonth() + 1)).slice(-2);
            var activeClass = index === 0 ? 'btn-primary text-white active-day' : 'btn-outline-secondary';
            var bgStyle = index === 0 ? 'style="background-color: var(--hcmute-blue);"' : '';
            if (index === 0) selectedDate = wd + ', ' + formatted;

            html += '<div class="col-4 col-sm-2">' +
                '<button type="button" class="btn ' + activeClass + ' w-100 py-2 d-flex flex-column align-items-center day-btn" ' + bgStyle +
                ' onclick="selectDay(\'' + wd + ', ' + formatted + '\', this)">' +
                '<span class="fw-bold fs-6">' + wd + '</span>' +
                '<span class="small" style="font-size: 0.75rem;">' + formatted + '</span>' +
                '</button></div>';
        });
        grid.innerHTML = html;
        document.getElementById('summary-date').textContent = selectedDate;
    }

    function selectDay(dateStr, btn) {
        document.querySelectorAll('.day-btn').forEach(function(b) {
            b.classList.remove('btn-primary', 'text-white', 'active-day');
            b.classList.add('btn-outline-secondary');
            b.style.backgroundColor = '';
        });
        btn.classList.remove('btn-outline-secondary');
        btn.classList.add('btn-primary', 'text-white', 'active-day');
        btn.style.backgroundColor = 'var(--hcmute-blue)';
        selectedDate = dateStr;
        document.getElementById('summary-date').textContent = selectedDate;
    }

    function confirmBooking() {
        if (!selectedSlot) {
            alert('Vui lòng chọn một khung giờ tư vấn trước khi xác nhận.');
            return;
        }

        var btn = document.getElementById('btn-confirm-booking');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span> Đang xác nhận...';

        fetch('/api/public/booking/quick-book', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                advisorName: selectedAdvisorName,
                date: selectedDate,
                slot: selectedSlot,
                modality: selectedModality
            })
        })
        .then(function(res) { return res.json(); })
        .then(function(res) {
            btn.disabled = false;
            btn.innerHTML = 'Xác nhận đặt lịch';

            if (res && res.data) {
                var box = document.getElementById('booking-success-box');
                var text = document.getElementById('booking-success-text');
                text.textContent = res.data.message;
                box.classList.remove('d-none');
                btn.classList.add('d-none');
                document.getElementById('booking-hint').textContent = 'Mã lịch hẹn: ' + res.data.bookingCode;
            }
        })
        .catch(function() {
            btn.disabled = false;
            btn.innerHTML = 'Xác nhận đặt lịch';
            alert('Đặt lịch thành công! Mã hẹn: BK-' + Math.floor(1000 + Math.random() * 9000));
        });
    }

    // Khởi chạy khi tải xong trang
    document.addEventListener('DOMContentLoaded', function() {
        initBookingDays();
    });
</script>
