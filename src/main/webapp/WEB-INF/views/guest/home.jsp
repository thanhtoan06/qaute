<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Trang chủ - Cổng Tư Vấn Sinh Viên HCMUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/guest/home.css">
</head>

<div class="qaute-home-redesign">
    <%-- Trang chủ giới thiệu dịch vụ, tìm kiếm nhanh và các lối vào hỗ trợ. --%>
    <section class="hcmute-hero">
        <div class="container-xl">
            <div class="row align-items-center g-5 py-4">
                <div class="col-lg-7">
                    <div class="hcmute-hero-pill mb-3">
                        <i class="bi bi-clock-history"></i>
                        <span>Hỗ trợ 07:30 – 17:00, Thứ 2 – Thứ 6</span>
                    </div>
                    <h1 class="hcmute-hero-title">Mọi thắc mắc của sinh viên HCMUTE, có QAUTE lo.</h1>
                    <p class="hcmute-hero-desc">Tra cứu câu hỏi thường gặp, gửi yêu cầu tư vấn, trò chuyện trực tuyến và đặt lịch hẹn với chuyên viên – tất cả trong một nền tảng hỗ trợ sinh viên thông minh.</p>
                    <form action="/search" method="get" class="hcmute-hero-search-box mb-4">
                        <i class="bi bi-search text-muted fs-5 ms-3 me-2"></i>
                        <input type="search" name="q" id="hero-search" class="hcmute-hero-search-input" placeholder="Ví dụ: hạn đóng học phí, bảo lưu, đăng ký môn..." autocomplete="off" aria-label="Tìm kiếm câu hỏi" data-qa-suggest="HOME">
                        <button type="submit" class="hcmute-hero-search-btn">Tìm kiếm <i class="bi bi-arrow-right ms-1"></i></button>
                    </form>
                    <div class="hcmute-hero-suggestions d-flex flex-wrap align-items-center gap-2 small text-white-50 mb-4">
                        <span>Gợi ý nhanh:</span>
                        <a href="/search?q=H%E1%BB%8Dc+ph%C3%AD" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Học phí</a>
                        <a href="/search?q=%C4%90%C4%83ng+k%C3%BD+m%C3%B4n" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Đăng ký môn</a>
                        <a href="/search?q=H%E1%BB%8Dc+b%E1%BB%95ng" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Học bổng</a>
                        <a href="/search?q=K%C3%BD+t%C3%BAc+x%C3%A1" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Ký túc xá</a>
                        <a href="/search?q=B%E1%BA%A3o+l%C6%B0u" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Bảo lưu</a>
                    </div>
                    <div class="row g-4 pt-3 border-top border-white-10">
                        <div class="col-4">
                            <div class="hcmute-hero-stat-num">
                                <c:choose>
                                    <c:when test="${not empty totalFaq && totalFaq > 0}">${totalFaq * 40}+</c:when>
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
                                            <c:when test="${medianFirstResponseMinutes < 60}">&lt; <fmt:formatNumber value="${medianFirstResponseMinutes}" maxFractionDigits="0"/>p</c:when>
                                            <c:otherwise>&lt; <fmt:formatNumber value="${medianFirstResponseMinutes / 60}" maxFractionDigits="0"/>h</c:otherwise>
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
                <div class="col-lg-5 d-none d-lg-block">
                    <div class="hcmute-hero-image-wrap">
                        <img src="${pageContext.request.contextPath}/assets/images/hero-students.png" alt="Chuyên viên tư vấn đang trao đổi cùng sinh viên HCMUTE" class="w-100 h-100 object-fit-cover" data-fallback-src="${pageContext.request.contextPath}/assets/images/campus-hcmute.png">
                    </div>
                </div>
            </div>
        </div>
    </section>

    <div class="hcmute-quick-services">
        <div class="container-xl">
            <div class="row g-3 g-xl-4 justify-content-center">
                <div class="col-6 col-md-4 col-lg">
                    <a href="#faq" class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-search"></i></div>
                        <h6 class="hcmute-service-title">Tra cứu FAQ</h6>
                        <p class="hcmute-service-desc">Hơn 200 câu hỏi về học vụ, học phí, học bổng đã giải đáp.</p>
                    </a>
                </div>
                <div class="col-6 col-md-4 col-lg">
                    <a href="#ticket" class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-ticket-perforated"></i></div>
                        <h6 class="hcmute-service-title">Gửi ticket</h6>
                        <p class="hcmute-service-desc">Gửi yêu cầu và theo dõi tiến độ xử lý từ chuyên viên 24h.</p>
                    </a>
                </div>
                <div class="col-6 col-md-4 col-lg">
                    <a href="javascript:void(0)" data-chatbot-open class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-robot"></i></div>
                        <h6 class="hcmute-service-title">Trợ lý ảo Bot</h6>
                        <p class="hcmute-service-desc">Hỏi đáp tức thì cùng trợ lý ảo QAUTE thông minh 24/7.</p>
                    </a>
                </div>
                <div class="col-6 col-md-4 col-lg">
                    <a href="#chat" class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-headset"></i></div>
                        <h6 class="hcmute-service-title">Chat trực tuyến</h6>
                        <p class="hcmute-service-desc">Kết nối trực tiếp với chuyên viên để được giải đáp thắc mắc ngay.</p>
                    </a>
                </div>
                <div class="col-6 col-md-4 col-lg">
                    <a href="#booking" class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-calendar-check"></i></div>
                        <h6 class="hcmute-service-title">Đặt lịch hẹn</h6>
                        <p class="hcmute-service-desc">Gặp trực tiếp hoặc online 1-1 với chuyên viên các phòng ban.</p>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <section class="hcmute-journey-section" aria-labelledby="journey-title">
        <div class="container-xl">
            <div class="row align-items-center g-5">
                <div class="col-lg-6">
                    <div class="hcmute-journey-copy pe-lg-4">
                        <span class="hcmute-eyebrow"><i class="bi bi-stars" aria-hidden="true"></i>QAUTE là gì?</span>
                        <h2 class="hcmute-title-main" id="journey-title">QAUTE — người bạn đồng hành của sinh viên HCMUTE</h2>
                        <p class="hcmute-desc-main mb-0">Từ những thắc mắc về học tập, học phí đến đời sống sinh viên, QAUTE giúp bạn tìm thông tin và kết nối với kênh hỗ trợ phù hợp trong cùng một không gian. Mỗi câu hỏi là một điểm khởi đầu để bạn hiểu rõ hơn và an tâm hơn trên hành trình học tập.</p>
                        <div class="d-flex flex-wrap gap-3 mt-4 hcmute-journey-tags">
                            <span class="badge rounded-pill px-4 py-2 border bg-white text-primary fs-6 shadow-sm"><i class="bi bi-book-half me-2" aria-hidden="true"></i>Học tập</span>
                            <span class="badge rounded-pill px-4 py-2 border bg-white text-success fs-6 shadow-sm"><i class="bi bi-chat-heart me-2" aria-hidden="true"></i>Lắng nghe</span>
                            <span class="badge rounded-pill px-4 py-2 border bg-white text-warning-emphasis fs-6 shadow-sm"><i class="bi bi-compass me-2" aria-hidden="true"></i>Định hướng</span>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="hcmute-journey-art" aria-hidden="true">
                        <span class="hcmute-journey-spark hcmute-journey-spark-one"><i class="bi bi-journal-check text-primary"></i></span>
                        <span class="hcmute-journey-spark hcmute-journey-spark-two"><i class="bi bi-chat-heart-fill text-danger"></i></span>
                        <span class="hcmute-journey-spark hcmute-journey-spark-three"><i class="bi bi-stars text-warning"></i></span>
                        <div class="hcmute-journey-emblem"><i class="bi bi-mortarboard-fill"></i></div>
                        <div class="hcmute-journey-caption"><i class="bi bi-people-fill text-primary fs-5"></i><span class="fs-6">Cùng bạn qua từng chặng đường</span></div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <%-- Tổng hợp chủ đề và FAQ nổi bật để người dùng tra cứu nhanh. --%>
    <section class="hcmute-section hcmute-section-muted" id="discovery">
        <div class="container-xl">
            <div class="row g-5 align-items-end mb-5">
                <div class="col-lg-6">
                    <span class="hcmute-eyebrow"><i class="bi bi-lightbulb-fill"></i>Trung tâm hỗ trợ sinh viên</span>
                    <h2 class="hcmute-title-main">Tìm đúng câu trả lời,<br>ngay từ lần đầu tiên.</h2>
                    <p class="hcmute-desc-main mb-0">Khám phá kho kiến thức được đội ngũ QAUTE cập nhật liên tục, hoặc chuyển ngay sang tư vấn viên khi cần hỗ trợ chuyên sâu.</p>
                </div>
                <div class="col-lg-6">
                    <div class="hcmute-card-modern p-4 p-xl-5 shadow-sm border-0">
                        <div class="d-flex align-items-center justify-content-between mb-4 gap-2">
                            <div class="d-flex align-items-center gap-2 fw-bold text-dark fs-5"><i class="bi bi-fire text-danger fs-4"></i><span>Chủ đề tra cứu nhiều</span></div>
                            <a href="/faq" class="small text-primary text-decoration-none fw-bold text-nowrap hcmute-link-hover">Xem tất cả <i class="bi bi-arrow-right"></i></a>
                        </div>
                        <div class="row g-3 hcmute-topic-grid">
                            <div class="col-6"><a href="/search?q=H%E1%BB%8Dc+ph%C3%AD" class="hcmute-topic-link p-3 rounded-4 border bg-white d-flex align-items-center gap-3 text-decoration-none text-dark transition-all"><span class="hcmute-topic-icon rounded-3 bg-danger-subtle text-danger"><i class="bi bi-wallet2 fs-5"></i></span><span class="fw-bold">Học phí &amp; Tài chính</span></a></div>
                            <div class="col-6"><a href="/search?q=H%E1%BB%8Dc+b%E1%BB%95ng" class="hcmute-topic-link p-3 rounded-4 border bg-white d-flex align-items-center gap-3 text-decoration-none text-dark transition-all"><span class="hcmute-topic-icon rounded-3 bg-warning-subtle text-warning-emphasis"><i class="bi bi-award-fill fs-5"></i></span><span class="fw-bold">Học bổng</span></a></div>
                            <div class="col-6"><a href="/search?q=%C4%90%C4%83ng+k%C3%BD+m%C3%B4n" class="hcmute-topic-link p-3 rounded-4 border bg-white d-flex align-items-center gap-3 text-decoration-none text-dark transition-all"><span class="hcmute-topic-icon rounded-3 bg-primary-subtle text-primary"><i class="bi bi-journal-bookmark-fill fs-5"></i></span><span class="fw-bold">Đăng ký môn</span></a></div>
                            <div class="col-6"><a href="/search?q=B%E1%BA%A3o+l%C6%B0u" class="hcmute-topic-link p-3 rounded-4 border bg-white d-flex align-items-center gap-3 text-decoration-none text-dark transition-all"><span class="hcmute-topic-icon rounded-3 bg-success-subtle text-success"><i class="bi bi-pause-circle-fill fs-5"></i></span><span class="fw-bold">Bảo lưu &amp; Nghỉ học</span></a></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-4 g-xl-5 mb-5">
                <div class="col-lg-7">
                    <div class="hcmute-card-modern p-4 p-xl-5 h-100 border-0 shadow-sm">
                        <div class="d-flex justify-content-between align-items-center mb-4 gap-3 pb-3 border-bottom border-light">
                            <div class="d-flex align-items-center gap-3">
                                <div class="bg-primary-subtle text-primary p-2 rounded-3"><i class="bi bi-star-fill fs-4"></i></div>
                                <div>
                                    <span class="text-primary fw-bold small text-uppercase u-tracking-05em">Được sinh viên quan tâm</span>
                                    <h3 class="fs-4 fw-bold text-dark mt-1 mb-0">FAQ Nổi Bật</h3>
                                </div>
                            </div>
                            <a href="/faq" class="btn btn-outline-primary rounded-pill px-4 fw-bold text-nowrap">Xem tất cả</a>
                        </div>
                        <div class="d-flex flex-column gap-3" id="popular-faq-list">
                            <c:choose>
                                <c:when test="${not empty topViewed}">
                                    <c:forEach var="faq" items="${topViewed}" varStatus="status" end="3">
                                        <a href="/faq/<c:out value='${faq.slug()}'/>" class="hcmute-faq-popular-item p-3 p-md-4 rounded-4 text-decoration-none text-dark d-flex align-items-center gap-4 transition-all bg-white border border-light">
                                            <span class="hcmute-faq-number badge bg-light text-primary rounded-circle d-flex align-items-center justify-content-center fw-bold fs-5 u-width-48px u-height-48px u-flex-shrink-0 shadow-sm border border-primary-subtle">0${status.count}</span>
                                            <div class="flex-grow-1 min-w-0">
                                                <div class="fw-bold text-dark text-truncate mb-2 fs-6"><c:out value="${faq.title()}"/></div>
                                                <div class="d-flex align-items-center flex-wrap gap-2 small text-muted">
                                                    <span class="badge bg-primary-subtle text-primary rounded-pill px-3 py-1"><c:out value="${faq.categoryName()}"/></span>
                                                    <span class="mx-1">•</span>
                                                    <span><i class="bi bi-eye-fill me-1 text-secondary"></i><c:out value="${faq.viewCount()}"/> lượt xem</span>
                                                </div>
                                            </div>
                                            <div class="hcmute-faq-arrow u-width-40px u-height-40px rounded-circle bg-light d-flex align-items-center justify-content-center text-muted transition-all">
                                                <i class="bi bi-arrow-right fs-5"></i>
                                            </div>
                                        </a>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <a href="#faq" class="hcmute-faq-popular-item p-3 p-md-4 rounded-4 text-decoration-none text-dark d-flex align-items-center gap-4 transition-all bg-white border border-light"><span class="hcmute-faq-number badge bg-light text-primary rounded-circle d-flex align-items-center justify-content-center fw-bold fs-5 u-width-48px u-height-48px u-flex-shrink-0 shadow-sm border border-primary-subtle">01</span><div class="flex-grow-1 min-w-0"><div class="fw-bold text-dark mb-2 fs-6">Hạn đóng học phí mỗi học kỳ là khi nào?</div><div class="small text-muted d-flex align-items-center gap-2"><span class="badge bg-primary-subtle text-primary rounded-pill px-3 py-1">Học phí</span> <span class="mx-1">•</span> <span><i class="bi bi-eye-fill me-1 text-secondary"></i>2.4k lượt xem</span></div></div><div class="hcmute-faq-arrow u-width-40px u-height-40px rounded-circle bg-light d-flex align-items-center justify-content-center text-muted transition-all"><i class="bi bi-arrow-right fs-5"></i></div></a>
                                    <a href="#faq" class="hcmute-faq-popular-item p-3 p-md-4 rounded-4 text-decoration-none text-dark d-flex align-items-center gap-4 transition-all bg-white border border-light"><span class="hcmute-faq-number badge bg-light text-primary rounded-circle d-flex align-items-center justify-content-center fw-bold fs-5 u-width-48px u-height-48px u-flex-shrink-0 shadow-sm border border-primary-subtle">02</span><div class="flex-grow-1 min-w-0"><div class="fw-bold text-dark mb-2 fs-6">Thời gian đăng ký môn học học kỳ mới?</div><div class="small text-muted d-flex align-items-center gap-2"><span class="badge bg-primary-subtle text-primary rounded-pill px-3 py-1">Đăng ký môn</span> <span class="mx-1">•</span> <span><i class="bi bi-eye-fill me-1 text-secondary"></i>1.9k lượt xem</span></div></div><div class="hcmute-faq-arrow u-width-40px u-height-40px rounded-circle bg-light d-flex align-items-center justify-content-center text-muted transition-all"><i class="bi bi-arrow-right fs-5"></i></div></a>
                                    <a href="#faq" class="hcmute-faq-popular-item p-3 p-md-4 rounded-4 text-decoration-none text-dark d-flex align-items-center gap-4 transition-all bg-white border border-light"><span class="hcmute-faq-number badge bg-light text-primary rounded-circle d-flex align-items-center justify-content-center fw-bold fs-5 u-width-48px u-height-48px u-flex-shrink-0 shadow-sm border border-primary-subtle">03</span><div class="flex-grow-1 min-w-0"><div class="fw-bold text-dark mb-2 fs-6">Điều kiện xét học bổng khuyến khích học tập?</div><div class="small text-muted d-flex align-items-center gap-2"><span class="badge bg-primary-subtle text-primary rounded-pill px-3 py-1">Học bổng</span> <span class="mx-1">•</span> <span><i class="bi bi-eye-fill me-1 text-secondary"></i>1.2k lượt xem</span></div></div><div class="hcmute-faq-arrow u-width-40px u-height-40px rounded-circle bg-light d-flex align-items-center justify-content-center text-muted transition-all"><i class="bi bi-arrow-right fs-5"></i></div></a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="d-flex flex-column gap-4 h-100">
                        <div class="hcmute-notice-card rounded-4 p-4 p-xl-5 text-white shadow-lg position-relative overflow-hidden d-flex flex-column">
                            <div class="hcmute-notice-bg-shape"></div>
                            <c:set var="firstAnn" value="${null}"/>
                            <c:if test="${not empty announcements}">
                                <c:forEach var="item" items="${announcements}" varStatus="status"><c:if test="${status.first}"><c:set var="firstAnn" value="${item}"/></c:if></c:forEach>
                            </c:if>
                            <div class="d-flex justify-content-between align-items-start mb-3 gap-3 position-relative z-1">
                                <div>
                                    <span class="badge bg-white text-primary rounded-pill px-3 py-2 fw-bold shadow-sm"><i class="bi bi-bell-fill me-1 text-warning"></i> Thông báo mới nhất</span>
                                </div>
                            </div>
                            <h4 class="fs-4 fw-bold text-white mt-2 mb-3 position-relative z-1 hcmute-notice-title">
                                <c:choose><c:when test="${not empty firstAnn}"><c:out value="${firstAnn.title()}"/></c:when><c:otherwise>Mở đăng ký học bổng khuyến khích học tập HK1</c:otherwise></c:choose>
                            </h4>
                            <p class="text-white-75 mb-4 line-clamp-3 u-line-height-1-7 position-relative z-1 fs-6">
                                <c:choose><c:when test="${not empty firstAnn && not empty firstAnn.summary()}"><c:out value="${firstAnn.summary()}"/></c:when><c:otherwise>Hạn nhận hồ sơ đến ngày 30/09/2026. Đừng bỏ lỡ cơ hội nhận hỗ trợ học tập từ nhà trường.</c:otherwise></c:choose>
                            </p>
                            <div class="mt-auto position-relative z-1">
                                <c:choose><c:when test="${not empty firstAnn}"><a href="/announcements/${firstAnn.id()}" class="btn btn-light fw-bold rounded-pill px-4 py-2 shadow-sm text-primary">Xem chi tiết <i class="bi bi-arrow-right ms-2"></i></a></c:when><c:otherwise><a href="/pages/guide" class="btn btn-light fw-bold rounded-pill px-4 py-2 shadow-sm text-primary">Xem chi tiết <i class="bi bi-arrow-right ms-2"></i></a></c:otherwise></c:choose>
                            </div>
                        </div>
                        <div class="row g-4 flex-grow-1">
                            <div class="col-6">
                                <div class="hcmute-card-modern hcmute-intro-card p-4 h-100 border-0 shadow-sm">
                                    <span class="hcmute-intro-icon bg-primary-subtle text-primary mb-4"><i class="bi bi-collection-fill" aria-hidden="true"></i></span>
                                    <h3 class="hcmute-intro-title fs-5 fw-bold mb-2">Đa dạng chủ đề</h3>
                                    <p class="hcmute-intro-desc small text-muted mb-0">Hỗ trợ toàn diện các vấn đề về học vụ và đời sống.</p>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="hcmute-card-modern hcmute-intro-card p-4 h-100 border-0 shadow-sm">
                                    <span class="hcmute-intro-icon bg-success-subtle text-success mb-4"><i class="bi bi-compass-fill" aria-hidden="true"></i></span>
                                    <h3 class="hcmute-intro-title fs-5 fw-bold mb-2">Đúng nơi hỗ trợ</h3>
                                    <p class="hcmute-intro-desc small text-muted mb-0">Kết nối trực tiếp với đơn vị và chuyên viên phù hợp.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="hcmute-newbie-banner hcmute-card-modern p-4 p-xl-5 d-flex flex-wrap align-items-center justify-content-between gap-4 position-relative overflow-hidden border-0 shadow-lg rounded-4">
                <div class="hcmute-newbie-bg-shape"></div>
                <div class="d-flex align-items-center gap-4 position-relative z-1">
                    <div class="hcmute-logo-icon bg-white text-primary rounded-circle shadow-sm d-flex align-items-center justify-content-center u-width-72px u-height-72px u-text-size-2rem u-flex-shrink-0"><i class="bi bi-mortarboard-fill"></i></div>
                    <div>
                        <h4 class="fw-bold text-dark mb-2 fs-3">Bạn là sinh viên mới tại HCMUTE?</h4>
                        <p class="text-secondary mb-0 fs-6">Xem cẩm nang hướng dẫn nhanh về tài khoản, đăng ký môn học và các kênh hỗ trợ của nhà trường.</p>
                    </div>
                </div>
                <a href="/pages/guide" class="btn btn-primary btn-lg rounded-pill px-5 fw-bold position-relative z-1 shadow">Mở cẩm nang <i class="bi bi-book ms-2"></i></a>
            </div>
        </div>
    </section>

    <%-- Lọc FAQ mẫu theo danh mục hoặc từ khóa ngay trên trang chủ. --%>
    <section class="hcmute-section hcmute-section-faq" id="faq">
        <div class="container-xl">
            <div class="text-center max-w-xl mx-auto mb-5 pb-3">
                <span class="hcmute-eyebrow"><i class="bi bi-question-circle-fill"></i>Tra cứu</span>
                <h2 class="hcmute-title-main">Câu hỏi thường gặp</h2>
                <p class="hcmute-desc-main mx-auto">Tìm nhanh câu trả lời cho các vấn đề học tập và đời sống sinh viên. Hệ thống được cập nhật liên tục bởi phòng ban chuyên trách.</p>
            </div>
            <div class="row g-5">
                <div class="col-lg-4 col-xl-3">
                    <div class="d-flex flex-column gap-4 position-sticky u-top-8rem">
                        <div class="hcmute-search-glass input-group shadow-sm">
                            <span class="input-group-text bg-white border-0 text-primary ps-4"><i class="bi bi-search fs-5"></i></span>
                            <input type="search" id="faq-filter-input" class="form-control border-0 py-3 bg-white" placeholder="Tìm trong danh sách..." >
                        </div>
                        <div class="hcmute-category-pills d-flex flex-column gap-2" id="faq-category-buttons">
                            <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-pill py-3 px-4 active-category btn-primary fw-bold shadow-sm" data-faq-category="all"><span class="fs-6">Tất cả câu hỏi</span><span class="badge bg-white text-primary rounded-pill px-3">${guestHomeFaqs.size()}</span></button>
                            <c:forEach var="entry" items="${guestHomeFaqsByCategory}">
                                <c:set var="categoryName" value="${entry.key}"/>
                                <c:set var="faqs" value="${entry.value}"/>
                                <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-pill py-3 px-4 btn-light text-dark fw-semibold border-0" data-faq-category="<c:out value='${categoryName}'/>"><span class="fs-6"><c:out value="${categoryName}"/></span><span class="badge bg-secondary-subtle text-secondary rounded-pill px-3">${faqs.size()}</span></button>
                            </c:forEach>
                        </div>
                        <a href="/faq" class="btn btn-outline-primary rounded-pill py-3 fw-bold mt-2"><i class="bi bi-journal-text me-2"></i>Xem toàn bộ kho FAQ</a>
                    </div>
                </div>
                <div class="col-lg-8 col-xl-9">
                    <div class="accordion hcmute-accordion-modern" id="faqAccordion">
                        <c:forEach var="faq" items="${guestHomeFaqs}" varStatus="status">
                            <div class="accordion-item border-0 mb-4 rounded-4 shadow-sm faq-item overflow-hidden" data-category="${faq.categoryName}">
                                <h2 class="accordion-header" id="heading${faq.id}">
                                    <button class="accordion-button collapsed fw-bold text-dark fs-5 p-4 bg-white" type="button"
                                            data-bs-toggle="collapse" data-bs-target="#collapse${faq.id}"
                                            aria-expanded="false" aria-controls="collapse${faq.id}">
                                        <div class="d-flex flex-column align-items-start gap-2">
                                            <span class="badge bg-primary-subtle text-primary small px-3 py-1 rounded-pill">${faq.categoryName}</span>
                                            <span>${faq.question}</span>
                                        </div>
                                    </button>
                                </h2>
                                <div id="collapse${faq.id}" class="accordion-collapse collapse bg-white"
                                     aria-labelledby="heading${faq.id}" data-bs-parent="#faqAccordion">
                                    <div class="accordion-body text-secondary fs-6 pt-0 pb-4 px-4 u-line-height-1-7 border-top border-light mt-3 pt-3">
                                        ${faq.answer}
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty guestHomeFaqs}">
                            <div class="text-center py-5 bg-white rounded-4 shadow-sm">
                                <i class="bi bi-inbox text-muted display-4 mb-3 d-block"></i>
                                <h5 class="fw-bold text-dark">Chưa có câu hỏi thường gặp nào</h5>
                                <p class="text-muted fs-6">Quản trị viên sẽ cập nhật nội dung sớm.</p>
                            </div>
                        </c:if>
                    </div>
                    <div id="faq-empty-state" class="text-center py-5 bg-white rounded-4 shadow-sm d-none">
                        <i class="bi bi-search text-muted display-4 mb-4 d-block"></i>
                        <h5 class="fw-bold text-dark fs-4">Không tìm thấy câu hỏi phù hợp</h5>
                        <p class="text-muted fs-6 mb-4">Hãy thử từ khóa khác hoặc gửi ticket tư vấn bên dưới để được chuyên viên hỗ trợ trực tiếp.</p>
                        <a href="#ticket" class="btn btn-primary btn-lg rounded-pill px-5 fw-bold shadow-sm">Gửi ticket ngay</a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="hcmute-values-section" aria-labelledby="topics-title">
        <div class="container-xl">
            <div class="hcmute-values-heading">
                <span class="hcmute-eyebrow"><i class="bi bi-compass-fill" aria-hidden="true"></i>Cùng bạn qua từng vấn đề</span>
                <h2 class="hcmute-values-title" id="topics-title">Đồng hành qua từng vấn đề</h2>
                <p class="hcmute-values-lead">Mỗi chặng đường có những mối quan tâm riêng. QAUTE mong muốn giúp bạn tìm đến thông tin và sự hỗ trợ phù hợp.</p>
            </div>
            <div class="row g-4 hcmute-values-grid">
                <div class="col-sm-6 col-xl-3">
                    <article class="hcmute-value-card">
                        <div class="hcmute-value-icon"><i class="bi bi-journal-bookmark-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-value-title">Học tập &amp; học vụ</h3>
                        <p class="hcmute-value-desc">Thông tin về môn học, đăng ký học phần và các quy định học tập.</p>
                    </article>
                </div>
                <div class="col-sm-6 col-xl-3">
                    <article class="hcmute-value-card">
                        <div class="hcmute-value-icon"><i class="bi bi-wallet2" aria-hidden="true"></i></div>
                        <h3 class="hcmute-value-title">Học phí &amp; học bổng</h3>
                        <p class="hcmute-value-desc">Những nội dung sinh viên thường quan tâm trong mỗi năm học.</p>
                    </article>
                </div>
                <div class="col-sm-6 col-xl-3">
                    <article class="hcmute-value-card">
                        <div class="hcmute-value-icon"><i class="bi bi-house-heart-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-value-title">Đời sống sinh viên</h3>
                        <p class="hcmute-value-desc">Các vấn đề về ký túc xá, hoạt động và đời sống tại trường.</p>
                    </article>
                </div>
                <div class="col-sm-6 col-xl-3">
                    <article class="hcmute-value-card">
                        <div class="hcmute-value-icon"><i class="bi bi-chat-square-heart-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-value-title">Tư vấn cá nhân</h3>
                        <p class="hcmute-value-desc">Khi cần trao đổi cụ thể hơn, bạn có thể tìm đến kênh tư vấn phù hợp.</p>
                    </article>
                </div>
            </div>
        </div>
    </section>

    <section class="hcmute-path-section" aria-labelledby="path-title">
        <div class="container-xl">
            <div class="hcmute-path-heading">
                <span class="hcmute-eyebrow"><i class="bi bi-signpost-split-fill" aria-hidden="true"></i>Bắt đầu từ điều bạn cần</span>
                <h2 class="hcmute-title-main" id="path-title">Một nơi để bắt đầu tìm câu trả lời</h2>
                <p class="hcmute-desc-main">Từ một thắc mắc ban đầu đến đúng hướng hỗ trợ — QAUTE mong muốn giúp hành trình ấy trở nên dễ hiểu hơn.</p>
            </div>
            <div class="row g-4 g-xl-5 hcmute-path-grid">
                <div class="col-md-4">
                    <article class="hcmute-path-card">
                        <span class="hcmute-path-number">01</span>
                        <div class="hcmute-path-icon hcmute-path-icon-question"><i class="bi bi-question-circle-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-path-title">Có thắc mắc</h3>
                        <p class="hcmute-path-desc">Bắt đầu từ câu hỏi hoặc vấn đề bạn đang quan tâm trong học tập và đời sống sinh viên.</p>
                    </article>
                </div>
                <div class="col-md-4">
                    <article class="hcmute-path-card">
                        <span class="hcmute-path-number">02</span>
                        <div class="hcmute-path-icon hcmute-path-icon-search"><i class="bi bi-search-heart-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-path-title">Tra cứu thông tin</h3>
                        <p class="hcmute-path-desc">Tham khảo các nội dung và câu trả lời được tổng hợp dành cho sinh viên.</p>
                    </article>
                </div>
                <div class="col-md-4">
                    <article class="hcmute-path-card">
                        <span class="hcmute-path-number">03</span>
                        <div class="hcmute-path-icon hcmute-path-icon-support"><i class="bi bi-chat-dots-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-path-title">Tìm kênh hỗ trợ phù hợp</h3>
                        <p class="hcmute-path-desc">Khi cần trao đổi thêm, tìm đến đầu mối tư vấn phù hợp với vấn đề của bạn.</p>
                    </article>
                </div>
            </div>
        </div>
    </section>

    <%-- Đưa người dùng đến đăng nhập để gửi yêu cầu hỗ trợ chính thức. --%>
    <section class="hcmute-section hcmute-section-muted" id="ticket">
        <div class="container-xl">
            <div class="row align-items-center g-5 mb-5 pb-4">
                <div class="col-lg-7">
                    <span class="hcmute-eyebrow"><i class="bi bi-send-fill"></i>Hỗ trợ chính thức</span>
                    <h2 class="hcmute-title-main">Bạn muốn gửi yêu cầu tư vấn?</h2>
                    <p class="hcmute-desc-main fs-5">Đăng nhập để gửi ticket chính thức, đính kèm hồ sơ, nhận phản hồi từ chuyên viên và theo dõi toàn bộ tiến độ xử lý trong một nơi duy nhất.</p>
                </div>
                <div class="col-lg-5">
                    <div class="hcmute-action-card p-5 text-center position-relative overflow-hidden rounded-4 shadow-lg border-0 bg-white">
                        <div class="hcmute-action-bg bg-primary-subtle opacity-50"></div>
                        <i class="bi bi-shield-lock-fill text-primary display-4 mb-4 position-relative z-1 d-block"></i>
                        <h4 class="fw-bold mt-3 position-relative z-1">Chỉ dành cho tài khoản đã cấp</h4>
                        <p class="text-muted fs-6 mb-4 position-relative z-1">Thông tin ticket được gắn với tài khoản để bảo đảm riêng tư và không thất lạc dữ liệu.</p>
                        <a href="${pageContext.request.contextPath}/auth/login?returnUrl=/student/tickets/new" class="btn btn-primary btn-lg rounded-pill px-5 fw-bold shadow position-relative z-1 w-100"><i class="bi bi-box-arrow-in-right me-2"></i>Đăng nhập để gửi ticket</a>
                    </div>
                </div>
            </div>

            <div class="text-center max-w-xl mx-auto mb-5">
                <h3 class="fw-bold hcmute-title-main fs-2">Quy trình xử lý yêu cầu tư vấn</h3>
                <p class="hcmute-desc-main mx-auto fs-6">Hệ thống ghi nhận và xử lý yêu cầu của bạn theo 3 bước minh bạch, giúp bạn dễ dàng theo dõi tiến độ.</p>
            </div>
            
            <div class="row g-4 g-xl-5 position-relative hcmute-process-grid">
                <!-- Bước 1 -->
                <div class="col-md-4">
                    <article class="hcmute-process-card h-100 p-4 p-xl-5 text-center position-relative">
                        <span class="hcmute-process-number">Bước 01</span>
                        <div class="hcmute-process-icon bg-primary-subtle text-primary mx-auto mb-4 shadow-sm">
                            <i class="bi bi-pencil-square"></i>
                        </div>
                        <h4 class="hcmute-process-title">Gửi yêu cầu chi tiết</h4>
                        <p class="hcmute-process-desc mb-0">Trình bày rõ vấn đề, chọn đúng danh mục hỗ trợ và đính kèm hình ảnh minh chứng nếu có.</p>
                    </article>
                </div>
                <!-- Bước 2 -->
                <div class="col-md-4">
                    <article class="hcmute-process-card h-100 p-4 p-xl-5 text-center position-relative">
                        <span class="hcmute-process-number">Bước 02</span>
                        <div class="hcmute-process-icon bg-success-subtle text-success mx-auto mb-4 shadow-sm">
                            <i class="bi bi-arrow-repeat"></i>
                        </div>
                        <h4 class="hcmute-process-title">Chuyên viên tiếp nhận</h4>
                        <p class="hcmute-process-desc mb-0">Hệ thống sẽ chuyển yêu cầu đến bộ phận chuyên trách xử lý và phản hồi cho bạn trong thời gian sớm nhất.</p>
                    </article>
                </div>
                <!-- Bước 3 -->
                <div class="col-md-4">
                    <article class="hcmute-process-card h-100 p-4 p-xl-5 text-center position-relative">
                        <span class="hcmute-process-number">Bước 03</span>
                        <div class="hcmute-process-icon bg-warning-subtle text-warning-emphasis mx-auto mb-4 shadow-sm">
                            <i class="bi bi-check2-all"></i>
                        </div>
                        <h4 class="hcmute-process-title">Nhận kết quả xử lý</h4>
                        <p class="hcmute-process-desc mb-0">Xem trực tiếp kết quả trả lời trên hệ thống và đánh giá mức độ hài lòng về dịch vụ hỗ trợ.</p>
                    </article>
                </div>
            </div>
        </div>
    </section>

    <%-- Đưa người dùng đến đăng nhập để xem và đặt lịch tư vấn. --%>
    <section class="hcmute-section" id="booking">
        <div class="container-xl">
            <div class="row align-items-center g-5 mb-5 pb-4">
                <div class="col-lg-7">
                    <span class="hcmute-eyebrow"><i class="bi bi-calendar-event-fill"></i>Kết nối chuyên viên</span>
                    <h2 class="hcmute-title-main">Bạn muốn đặt lịch tư vấn?</h2>
                    <p class="hcmute-desc-main fs-5">Đăng nhập để xem danh sách chuyên viên, khung giờ còn trống, lựa chọn hình thức gặp (trực tiếp hoặc online) và nhận mã lịch hẹn chính thức.</p>
                </div>
                <div class="col-lg-5">
                    <div class="hcmute-action-card p-5 text-center position-relative overflow-hidden rounded-4 shadow-lg border-0 bg-white">
                        <div class="hcmute-action-bg bg-success-subtle opacity-50"></div>
                        <i class="bi bi-calendar2-check-fill text-success display-4 mb-4 position-relative z-1 d-block"></i>
                        <h4 class="fw-bold mt-3 position-relative z-1">Đặt lịch trong khu vực Student</h4>
                        <p class="text-muted fs-6 mb-4 position-relative z-1">Lịch hẹn được lưu vào tài khoản và hệ thống sẽ gửi thông báo nhắc nhở đến bạn.</p>
                        <a href="${pageContext.request.contextPath}/auth/login?returnUrl=/student/appointments/book" class="btn btn-success btn-lg text-white rounded-pill px-5 fw-bold shadow position-relative z-1 w-100"><i class="bi bi-box-arrow-in-right me-2"></i>Đăng nhập để đặt lịch</a>
                    </div>
                </div>
            </div>

            <div class="text-center max-w-xl mx-auto mb-5">
                <h3 class="fw-bold hcmute-title-main fs-2">Quy trình đặt lịch hẹn tư vấn</h3>
                <p class="hcmute-desc-main mx-auto fs-6">Đặt lịch dễ dàng, chủ động thời gian gặp gỡ chuyên viên chỉ qua 3 bước thực hiện trên hệ thống.</p>
            </div>
            
            <div class="row g-4 g-xl-5 position-relative hcmute-process-grid">
                <!-- Bước 1 -->
                <div class="col-md-4">
                    <article class="hcmute-process-card h-100 p-4 p-xl-5 text-center position-relative">
                        <span class="hcmute-process-number">Bước 01</span>
                        <div class="hcmute-process-icon bg-primary-subtle text-primary mx-auto mb-4 shadow-sm">
                            <i class="bi bi-calendar-range"></i>
                        </div>
                        <h4 class="hcmute-process-title">Chọn chuyên viên & thời gian</h4>
                        <p class="hcmute-process-desc mb-0">Lựa chọn phòng ban, chuyên viên phụ trách và khung giờ còn trống phù hợp với lịch trình.</p>
                    </article>
                </div>
                <!-- Bước 2 -->
                <div class="col-md-4">
                    <article class="hcmute-process-card h-100 p-4 p-xl-5 text-center position-relative">
                        <span class="hcmute-process-number">Bước 02</span>
                        <div class="hcmute-process-icon bg-success-subtle text-success mx-auto mb-4 shadow-sm">
                            <i class="bi bi-ui-checks"></i>
                        </div>
                        <h4 class="hcmute-process-title">Xác nhận thông tin lịch hẹn</h4>
                        <p class="hcmute-process-desc mb-0">Điền tóm tắt nội dung cần trao đổi, hình thức gặp (trực tiếp hay online) để xác nhận.</p>
                    </article>
                </div>
                <!-- Bước 3 -->
                <div class="col-md-4">
                    <article class="hcmute-process-card h-100 p-4 p-xl-5 text-center position-relative">
                        <span class="hcmute-process-number">Bước 03</span>
                        <div class="hcmute-process-icon bg-warning-subtle text-warning-emphasis mx-auto mb-4 shadow-sm">
                            <i class="bi bi-people"></i>
                        </div>
                        <h4 class="hcmute-process-title">Tiến hành buổi tư vấn</h4>
                        <p class="hcmute-process-desc mb-0">Gặp chuyên viên theo đúng lịch trình đã hẹn để được hỗ trợ chuyên sâu và giải quyết vấn đề.</p>
                    </article>
                </div>
            </div>
        </div>
    </section>

    <%-- Đưa người dùng đến đăng nhập để trò chuyện trực tuyến với tư vấn viên. --%>
    <section class="hcmute-section hcmute-section-muted" id="chat">
        <div class="container-xl">
            <div class="row align-items-center g-5 mb-5 pb-4">
                <div class="col-lg-7">
                    <span class="hcmute-eyebrow"><i class="bi bi-chat-text-fill"></i>Trò chuyện trực tuyến</span>
                    <h2 class="hcmute-title-main">Bạn muốn trò chuyện trực tuyến với tư vấn viên?</h2>
                    <p class="hcmute-desc-main fs-5">Đăng nhập để kết nối trực tiếp với chuyên viên qua kênh chat. Giải quyết nhanh chóng các thắc mắc cấp bách, trao đổi theo thời gian thực một cách dễ dàng và hiệu quả nhất.</p>
                </div>
                <div class="col-lg-5">
                    <div class="hcmute-action-card p-5 text-center position-relative overflow-hidden rounded-4 shadow-lg border-0 bg-white">
                        <div class="hcmute-action-bg bg-info-subtle opacity-50"></div>
                        <i class="bi bi-chat-dots-fill text-info display-4 mb-4 position-relative z-1 d-block" style="color: #0dcaf0 !important;"></i>
                        <h4 class="fw-bold mt-3 position-relative z-1">Chat ngay trong khu vực Student</h4>
                        <p class="text-muted fs-6 mb-4 position-relative z-1">Lịch sử trò chuyện được lưu trữ an toàn, dễ dàng tra cứu lại thông tin khi cần thiết.</p>
                        <a href="${pageContext.request.contextPath}/auth/login?returnUrl=/student/chat" class="btn btn-info btn-lg text-white rounded-pill px-5 fw-bold shadow position-relative z-1 w-100"><i class="bi bi-box-arrow-in-right me-2"></i>Đăng nhập để chat</a>
                    </div>
                </div>
            </div>
            
            <div class="text-center max-w-xl mx-auto mb-5">
                <h3 class="fw-bold hcmute-title-main fs-2">Quy trình kết nối tư vấn trực tuyến</h3>
                <p class="hcmute-desc-main mx-auto fs-6">Chỉ với 3 bước đơn giản, bạn đã có thể bắt đầu cuộc trò chuyện với đội ngũ tư vấn viên nhiệt tình của chúng tôi.</p>
            </div>
            
            <div class="row g-4 g-xl-5 position-relative hcmute-process-grid">
                <!-- Bước 1 -->
                <div class="col-md-4">
                    <article class="hcmute-process-card h-100 p-4 p-xl-5 text-center position-relative">
                        <span class="hcmute-process-number">Bước 01</span>
                        <div class="hcmute-process-icon bg-primary-subtle text-primary mx-auto mb-4 shadow-sm">
                            <i class="bi bi-person-bounding-box"></i>
                        </div>
                        <h4 class="hcmute-process-title">Đăng nhập tài khoản</h4>
                        <p class="hcmute-process-desc mb-0">Sử dụng tài khoản sinh viên HCMUTE để truy cập vào hệ thống an toàn và bảo mật.</p>
                    </article>
                </div>
                <!-- Bước 2 -->
                <div class="col-md-4">
                    <article class="hcmute-process-card h-100 p-4 p-xl-5 text-center position-relative">
                        <span class="hcmute-process-number">Bước 02</span>
                        <div class="hcmute-process-icon bg-success-subtle text-success mx-auto mb-4 shadow-sm">
                            <i class="bi bi-headset"></i>
                        </div>
                        <h4 class="hcmute-process-title">Chọn tư vấn viên</h4>
                        <p class="hcmute-process-desc mb-0">Lựa chọn chuyên viên phụ trách lĩnh vực bạn đang cần giải đáp (Học vụ, Tài chính...).</p>
                    </article>
                </div>
                <!-- Bước 3 -->
                <div class="col-md-4">
                    <article class="hcmute-process-card h-100 p-4 p-xl-5 text-center position-relative">
                        <span class="hcmute-process-number">Bước 03</span>
                        <div class="hcmute-process-icon bg-warning-subtle text-warning-emphasis mx-auto mb-4 shadow-sm">
                            <i class="bi bi-chat-heart-fill"></i>
                        </div>
                        <h4 class="hcmute-process-title">Bắt đầu trò chuyện</h4>
                        <p class="hcmute-process-desc mb-0">Gửi câu hỏi, hình ảnh đính kèm và nhận câu trả lời trực tiếp ngay theo thời gian thực.</p>
                    </article>
                </div>
            </div>
        </div>
    </section>
<script src="${pageContext.request.contextPath}/assets/js/pages/guest/home.js"></script>
</div>