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
                    <div class="d-flex flex-wrap align-items-center gap-2 small text-white-50 mb-4">
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
            <div class="row g-3">
                <div class="col-6 col-md-3">
                    <a href="#faq" class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-search"></i></div>
                        <h6 class="hcmute-service-title">Tra cứu FAQ</h6>
                        <p class="hcmute-service-desc">Hơn 200 câu hỏi về học vụ, học phí, học bổng đã giải đáp.</p>
                    </a>
                </div>
                <div class="col-6 col-md-3">
                    <a href="#ticket" class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-ticket-perforated"></i></div>
                        <h6 class="hcmute-service-title">Gửi ticket</h6>
                        <p class="hcmute-service-desc">Gửi yêu cầu và theo dõi tiến độ xử lý từ chuyên viên 24h.</p>
                    </a>
                </div>
                <div class="col-6 col-md-3">
                    <a href="javascript:void(0)" data-chatbot-open class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-robot"></i></div>
                        <h6 class="hcmute-service-title">Trợ lý ảo Bot</h6>
                        <p class="hcmute-service-desc">Hỏi đáp tức thì cùng trợ lý ảo QAUTE kéo thả thông minh 24/7.</p>
                    </a>
                </div>
                <div class="col-6 col-md-3">
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
            <div class="row align-items-center g-4 g-lg-5">
                <div class="col-lg-6">
                    <div class="hcmute-journey-copy">
                        <span class="hcmute-eyebrow"><i class="bi bi-stars" aria-hidden="true"></i>QAUTE là gì?</span>
                        <h2 class="hcmute-title-main" id="journey-title">QAUTE — người bạn đồng hành của sinh viên HCMUTE</h2>
                        <p class="hcmute-desc-main mb-0">Từ những thắc mắc về học tập, học phí đến đời sống sinh viên, QAUTE giúp bạn tìm thông tin và kết nối với kênh hỗ trợ phù hợp trong cùng một không gian. Mỗi câu hỏi là một điểm khởi đầu để bạn hiểu rõ hơn và an tâm hơn trên hành trình học tập.</p>
                        <div class="d-flex flex-wrap gap-2 mt-4">
                            <span class="badge rounded-pill px-3 py-2 border bg-white text-primary"><i class="bi bi-book-half me-1" aria-hidden="true"></i>Học tập</span>
                            <span class="badge rounded-pill px-3 py-2 border bg-white text-success"><i class="bi bi-chat-heart me-1" aria-hidden="true"></i>Lắng nghe</span>
                            <span class="badge rounded-pill px-3 py-2 border bg-white text-warning-emphasis"><i class="bi bi-compass me-1" aria-hidden="true"></i>Định hướng</span>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="hcmute-journey-art" aria-hidden="true">
                        <span class="hcmute-journey-spark hcmute-journey-spark-one"><i class="bi bi-journal-check"></i></span>
                        <span class="hcmute-journey-spark hcmute-journey-spark-two"><i class="bi bi-chat-heart-fill"></i></span>
                        <span class="hcmute-journey-spark hcmute-journey-spark-three"><i class="bi bi-stars"></i></span>
                        <div class="hcmute-journey-emblem"><i class="bi bi-mortarboard-fill"></i></div>
                        <div class="hcmute-journey-caption"><i class="bi bi-people-fill"></i><span>Cùng bạn qua từng chặng đường</span></div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <%-- Tổng hợp chủ đề và FAQ nổi bật để người dùng tra cứu nhanh. --%>
    <section class="hcmute-section hcmute-section-muted" id="discovery">
        <div class="container-xl">
            <div class="row g-4 align-items-end mb-5">
                <div class="col-lg-6">
                    <span class="hcmute-eyebrow"><i class="bi bi-stars"></i>Trung tâm hỗ trợ sinh viên</span>
                    <h2 class="hcmute-title-main">Tìm đúng câu trả lời, ngay từ lần đầu tiên.</h2>
                    <p class="hcmute-desc-main mb-0">Khám phá kho kiến thức được đội ngũ QAUTE cập nhật liên tục, hoặc chuyển ngay sang tư vấn viên khi cần hỗ trợ chuyên sâu.</p>
                </div>
                <div class="col-lg-6">
                    <div class="hcmute-card-modern p-4">
                        <div class="d-flex align-items-center justify-content-between mb-3 gap-2">
                            <div class="d-flex align-items-center gap-2 fw-bold text-dark small"><i class="bi bi-fire text-danger"></i><span>Chủ đề được sinh viên tra cứu nhiều</span></div>
                            <a href="/faq" class="small text-primary text-decoration-none fw-semibold text-nowrap">Xem tất cả FAQ <i class="bi bi-arrow-right"></i></a>
                        </div>
                        <div class="row g-2">
                            <div class="col-6"><a href="/search?q=H%E1%BB%8Dc+ph%C3%AD" class="p-2 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all"><span class="rounded bg-danger-subtle text-danger p-2"><i class="bi bi-wallet2"></i></span><span class="small fw-semibold">Học phí &amp; Tài chính</span></a></div>
                            <div class="col-6"><a href="/search?q=H%E1%BB%8Dc+b%E1%BB%95ng" class="p-2 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all"><span class="rounded bg-warning-subtle text-warning-emphasis p-2"><i class="bi bi-award-fill"></i></span><span class="small fw-semibold">Học bổng khuyến khích</span></a></div>
                            <div class="col-6"><a href="/search?q=%C4%90%C4%83ng+k%C3%BD+m%C3%B4n" class="p-2 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all"><span class="rounded bg-primary-subtle text-primary p-2"><i class="bi bi-journal-bookmark-fill"></i></span><span class="small fw-semibold">Đăng ký môn học</span></a></div>
                            <div class="col-6"><a href="/search?q=B%E1%BA%A3o+l%C6%B0u" class="p-2 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all"><span class="rounded bg-success-subtle text-success p-2"><i class="bi bi-pause-circle-fill"></i></span><span class="small fw-semibold">Bảo lưu &amp; Nghỉ học</span></a></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-4 mb-4">
                <div class="col-lg-7">
                    <div class="hcmute-card-modern p-4 p-md-5 h-100">
                        <div class="d-flex justify-content-between align-items-center mb-4 gap-3">
                            <div>
                                <span class="text-primary fw-bold small text-uppercase u-tracking-05em">Được sinh viên quan tâm</span>
                                <h3 class="fs-4 fw-bold text-dark mt-1 mb-0">FAQ Nổi Bật</h3>
                            </div>
                            <a href="/faq" class="btn btn-sm btn-outline-primary rounded-pill px-3 text-nowrap">Xem tất cả <i class="bi bi-arrow-right ms-1"></i></a>
                        </div>
                        <div class="d-flex flex-column gap-2" id="popular-faq-list">
                            <c:choose>
                                <c:when test="${not empty topViewed}">
                                    <c:forEach var="faq" items="${topViewed}" varStatus="status" end="3">
                                        <a href="/faq/<c:out value='${faq.slug()}'/>" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light">
                                            <span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6 u-width-36px u-height-36px u-flex-shrink-0">0${status.count}</span>
                                            <div class="flex-grow-1 min-w-0">
                                                <div class="fw-semibold text-dark text-truncate mb-1"><c:out value="${faq.title()}"/></div>
                                                <div class="d-flex align-items-center flex-wrap gap-2 small text-muted"><span class="badge bg-light text-secondary border"><c:out value="${faq.categoryName()}"/></span><span>•</span><span><i class="bi bi-eye me-1"></i><c:out value="${faq.viewCount()}"/> lượt xem</span></div>
                                            </div>
                                            <i class="bi bi-chevron-right text-muted mt-2"></i>
                                        </a>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <a href="#faq" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light"><span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6 u-width-36px u-height-36px u-flex-shrink-0">01</span><div class="flex-grow-1 min-w-0"><div class="fw-semibold text-dark mb-1">Hạn đóng học phí mỗi học kỳ là khi nào?</div><div class="small text-muted"><span class="badge bg-light text-secondary border">Học phí</span> • 2.4k lượt xem</div></div><i class="bi bi-chevron-right text-muted mt-2"></i></a>
                                    <a href="#faq" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light"><span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6 u-width-36px u-height-36px u-flex-shrink-0">02</span><div class="flex-grow-1 min-w-0"><div class="fw-semibold text-dark mb-1">Thời gian đăng ký môn học học kỳ mới?</div><div class="small text-muted"><span class="badge bg-light text-secondary border">Đăng ký môn</span> • 1.9k lượt xem</div></div><i class="bi bi-chevron-right text-muted mt-2"></i></a>
                                    <a href="#faq" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light"><span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6 u-width-36px u-height-36px u-flex-shrink-0">03</span><div class="flex-grow-1 min-w-0"><div class="fw-semibold text-dark mb-1">Điều kiện xét học bổng khuyến khích học tập?</div><div class="small text-muted"><span class="badge bg-light text-secondary border">Học bổng</span> • 1.2k lượt xem</div></div><i class="bi bi-chevron-right text-muted mt-2"></i></a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="d-flex flex-column gap-3 h-100">
                        <div class="rounded-4 p-4 text-white shadow-sm">
                            <c:set var="firstAnn" value="${null}"/>
                            <c:if test="${not empty announcements}">
                                <c:forEach var="item" items="${announcements}" varStatus="status"><c:if test="${status.first}"><c:set var="firstAnn" value="${item}"/></c:if></c:forEach>
                            </c:if>
                            <div class="d-flex justify-content-between align-items-start mb-2 gap-3">
                                <div>
                                    <span class="badge bg-white-20 text-white rounded-pill px-3 py-2 small">Thông báo mới nhất</span>
                                    <h4 class="fs-5 fw-bold text-white mt-3 mb-1">
                                        <c:choose><c:when test="${not empty firstAnn}"><c:out value="${firstAnn.title()}"/></c:when><c:otherwise>Mở đăng ký học bổng khuyến khích học tập HK1</c:otherwise></c:choose>
                                    </h4>
                                </div>
                                <div class="rounded-circle bg-white-10 p-2 text-warning"><i class="bi bi-bell-fill fs-5"></i></div>
                            </div>
                            <p class="text-white-50 small mb-4 line-clamp-2 u-line-height-1-7">
                                <c:choose><c:when test="${not empty firstAnn && not empty firstAnn.summary()}"><c:out value="${firstAnn.summary()}"/></c:when><c:otherwise>Hạn nhận hồ sơ đến ngày 30/09/2026. Đừng bỏ lỡ cơ hội nhận hỗ trợ học tập từ nhà trường.</c:otherwise></c:choose>
                            </p>
                            <c:choose><c:when test="${not empty firstAnn}"><a href="/announcements/${firstAnn.id()}" class="btn btn-light btn-sm fw-semibold rounded-pill px-3">Xem thông báo <i class="bi bi-arrow-right ms-1"></i></a></c:when><c:otherwise><a href="/pages/guide" class="btn btn-light btn-sm fw-semibold rounded-pill px-3">Xem thông báo <i class="bi bi-arrow-right ms-1"></i></a></c:otherwise></c:choose>
                        </div>
                        <div class="row g-3 flex-grow-1">
                            <div class="col-6">
                                <div class="hcmute-card-modern hcmute-intro-card p-3 p-sm-4 h-100">
                                    <span class="hcmute-intro-icon"><i class="bi bi-journals" aria-hidden="true"></i></span>
                                    <h3 class="hcmute-intro-title">Đa dạng chủ đề</h3>
                                    <p class="hcmute-intro-desc">Hỗ trợ các vấn đề về học tập, học phí, học vụ và đời sống sinh viên.</p>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="hcmute-card-modern hcmute-intro-card p-3 p-sm-4 h-100">
                                    <span class="hcmute-intro-icon"><i class="bi bi-signpost-2" aria-hidden="true"></i></span>
                                    <h3 class="hcmute-intro-title">Đúng nơi hỗ trợ</h3>
                                    <p class="hcmute-intro-desc">Kết nối bạn với thông tin hoặc đơn vị phụ trách phù hợp.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="hcmute-card-modern p-4 d-flex flex-wrap align-items-center justify-content-between gap-3">
                <div class="d-flex align-items-center gap-3">
                    <div class="hcmute-logo-icon u-width-48px u-height-48px u-text-size-1-5rem u-flex-shrink-0"><i class="bi bi-mortarboard-fill"></i></div>
                    <div>
                        <h5 class="fw-bold text-dark mb-1">Bạn là sinh viên mới tại HCMUTE?</h5>
                        <p class="small text-muted mb-0">Xem cẩm nang hướng dẫn nhanh về tài khoản, đăng ký môn học và các kênh hỗ trợ của nhà trường.</p>
                    </div>
                </div>
                <a href="/pages/guide" class="btn btn-outline-primary rounded-pill px-4 fw-semibold">Mở hướng dẫn <i class="bi bi-book ms-1"></i></a>
            </div>
        </div>
    </section>

    <%-- Lọc FAQ mẫu theo danh mục hoặc từ khóa ngay trên trang chủ. --%>
    <section class="hcmute-section" id="faq">
        <div class="container-xl">
            <div class="text-center max-w-xl mx-auto mb-5">
                <span class="hcmute-eyebrow"><i class="bi bi-question-circle-fill"></i>Tra cứu</span>
                <h2 class="hcmute-title-main">Câu hỏi thường gặp</h2>
                <p class="hcmute-desc-main mx-auto">Tìm nhanh câu trả lời cho các vấn đề học tập và đời sống sinh viên.</p>
            </div>
            <div class="row g-4">
                <div class="col-lg-3">
                    <div class="d-flex flex-column gap-3 position-sticky u-top-5rem">
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-search"></i></span>
                            <input type="search" id="faq-filter-input" class="form-control border-start-0" placeholder="Tìm trong danh sách..." >
                        </div>
                        <div class="d-flex flex-column gap-1" id="faq-category-buttons">
                            <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-3 p-2 active-category btn-primary u-bg-color-var-hcmute-blue u-border-color-var-hcmute-blue" data-faq-category="all"><span class="fw-semibold small">Tất cả câu hỏi</span><span class="badge bg-white text-primary rounded-pill">${guestHomeFaqs.size()}</span></button>
                            <c:forEach var="entry" items="${guestHomeFaqsByCategory}">
                                <c:set var="categoryName" value="${entry.key}"/>
                                <c:set var="faqs" value="${entry.value}"/>
                                <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-3 p-2 btn-light text-dark" data-faq-category="<c:out value='${categoryName}'/>"><span class="fw-semibold small"><c:out value="${categoryName}"/></span><span class="badge bg-secondary-subtle text-secondary rounded-pill">${faqs.size()}</span></button>
                            </c:forEach>
                        </div>
                        <a href="/faq" class="btn btn-outline-primary btn-sm rounded-pill mt-2"><i class="bi bi-journal-text me-1"></i>Xem kho FAQ đầy đủ</a>
                    </div>
                </div>
                <div class="col-lg-9">
                    <div class="hcmute-card-modern p-4">
                        <div class="accordion accordion-flush" id="faqAccordion">
                            <c:forEach var="faq" items="${guestHomeFaqs}" varStatus="status">
                                <div class="accordion-item border-bottom py-2 faq-item" data-category="${faq.categoryName}">
                                    <h2 class="accordion-header" id="heading${faq.id}">
                                        <button class="accordion-button collapsed fw-semibold text-dark fs-6" type="button"
                                                data-bs-toggle="collapse" data-bs-target="#collapse${faq.id}"
                                                aria-expanded="false" aria-controls="collapse${faq.id}">
                                            <div class="d-flex flex-column align-items-start gap-1">
                                                <span class="badge bg-primary-subtle text-primary small">${faq.categoryName}</span>
                                                <span>${faq.question}</span>
                                            </div>
                                        </button>
                                    </h2>
                                    <div id="collapse${faq.id}" class="accordion-collapse collapse"
                                         aria-labelledby="heading${faq.id}" data-bs-parent="#faqAccordion">
                                        <div class="accordion-body text-secondary small pt-1 u-line-height-1-7">
                                            ${faq.answer}
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <c:if test="${empty guestHomeFaqs}">
                                <div class="text-center py-5">
                                    <i class="bi bi-inbox text-muted fs-1 mb-3 d-block"></i>
                                    <h6 class="fw-bold">Chưa có câu hỏi thường gặp nào</h6>
                                    <p class="text-muted small">Quản trị viên sẽ cập nhật nội dung sớm.</p>
                                </div>
                            </c:if>
                        </div>
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

    <section class="hcmute-values-section" aria-labelledby="topics-title">
        <div class="container-xl">
            <div class="hcmute-values-heading">
                <span class="hcmute-eyebrow"><i class="bi bi-compass-fill" aria-hidden="true"></i>Cùng bạn qua từng vấn đề</span>
                <h2 class="hcmute-values-title" id="topics-title">Đồng hành qua từng vấn đề</h2>
                <p class="hcmute-values-lead">Mỗi chặng đường có những mối quan tâm riêng. QAUTE mong muốn giúp bạn tìm đến thông tin và sự hỗ trợ phù hợp.</p>
            </div>
            <div class="row g-3 g-lg-4 hcmute-values-grid">
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
            <div class="row g-3 g-lg-4 hcmute-path-grid">
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
            <div class="row align-items-center g-4">
                <div class="col-lg-7">
                    <span class="hcmute-eyebrow"><i class="bi bi-send-fill"></i>Hỗ trợ chính thức</span>
                    <h2 class="hcmute-title-main">Bạn muốn gửi yêu cầu tư vấn?</h2>
                    <p class="hcmute-desc-main">Đăng nhập để gửi ticket chính thức, đính kèm hồ sơ, nhận phản hồi từ chuyên viên và theo dõi toàn bộ tiến độ xử lý trong một nơi.</p>
                </div>
                <div class="col-lg-5">
                    <div class="hcmute-card-modern p-4 text-center">
                        <i class="bi bi-shield-lock-fill text-primary display-6"></i>
                        <h5 class="fw-bold mt-3">Chỉ dành cho tài khoản đã cấp</h5>
                        <p class="text-muted small">Thông tin ticket được gắn với tài khoản để bảo đảm riêng tư và không thất lạc dữ liệu.</p>
                        <a href="${pageContext.request.contextPath}/auth/login?returnUrl=/student/tickets/new" class="btn btn-primary rounded-pill px-4"><i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập để gửi ticket</a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <%-- Đưa người dùng đến đăng nhập để xem và đặt lịch tư vấn. --%>
    <section class="hcmute-section hcmute-section-muted" id="booking">
        <div class="container-xl">
            <div class="row align-items-center g-4">
                <div class="col-lg-7">
                    <span class="hcmute-eyebrow"><i class="bi bi-calendar-event-fill"></i>Kết nối chuyên viên</span>
                    <h2 class="hcmute-title-main">Bạn muốn đặt lịch tư vấn?</h2>
                    <p class="hcmute-desc-main">Đăng nhập để xem chuyên viên, khung giờ còn trống, hình thức gặp và nhận mã lịch hẹn chính thức.</p>
                </div>
                <div class="col-lg-5">
                    <div class="hcmute-card-modern p-4 text-center">
                        <i class="bi bi-calendar2-check-fill text-primary display-6"></i>
                        <h5 class="fw-bold mt-3">Đặt lịch trong khu vực Student</h5>
                        <p class="text-muted small">Lịch hẹn được lưu vào tài khoản và gửi thông báo xác nhận đến bạn.</p>
                        <a href="${pageContext.request.contextPath}/auth/login?returnUrl=/student/appointments/book" class="btn btn-primary rounded-pill px-4"><i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập để đặt lịch</a>
                    </div>
                </div>
            </div>
        </div>
    </section>
<script src="${pageContext.request.contextPath}/assets/js/pages/guest/home.js"></script>
</div>
