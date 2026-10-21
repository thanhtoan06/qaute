<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Trang chủ</title>

<div class="qa-hero-home text-center">
    <h1 class="qa-hero-title">Bạn cần hỗ trợ gì hôm nay?</h1>
    <div class="qa-home-search-group">
        <form method="get" action="/search">
            <div class="input-group">
                <input type="search" name="q" class="form-control qa-home-search-input"
                       placeholder="Nhập từ khóa tìm kiếm (học phí, học bổng, đăng ký môn học...)"
                       autocomplete="off" aria-label="Tìm kiếm trợ giúp" data-qa-suggest="HOME">
                <button class="btn btn-dark qa-home-search-btn" type="submit">
                    <i class="bi bi-search me-1"></i>Tìm kiếm
                </button>
            </div>
        </form>
    </div>
    <div class="qa-hero-keywords">
        <span class="text-white-50 me-2 small">Gợi ý nhanh:</span>
        <a href="/search?q=H%E1%BB%8Dc+ph%C3%AD">Học phí</a>
        <a href="/search?q=%C4%90%C4%83ng+k%C3%BD+h%E1%BB%8Dc+ph%E1%BA%A7n">Đăng ký học phần</a>
        <a href="/search?q=H%E1%BB%8Dc+b%E1%BB%95ng">Học bổng</a>
        <a href="/search?q=K%C3%BD+t%C3%BAc+x%C3%A1">Ký túc xá</a>
    </div>
</div>

<c:set var="totalCategories" value="0"/>
<c:forEach var="dept" items="${tree}">
    <c:set var="totalCategories" value="${totalCategories + fn:length(dept.categories())}"/>
</c:forEach>

<%-- Banner Announcement (T1-62: hiển thị thông báo chung, ghim trước, có link chi tiết) --%>
<c:if test="${not empty announcements}">
    <div class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2 class="mb-0"><i class="bi bi-megaphone-fill text-danger me-2"></i>Thông báo chung</h2>
        </div>
        <div class="row row-cards">
            <c:forEach var="ann" items="${announcements}">
                <div class="col-12 col-md-4">
                    <div class="card qa-card-hover h-100 shadow-sm border-0 border-top ${ann.pinned() ? 'border-danger' : 'border-primary'} border-3">
                        <c:if test="${not empty ann.coverUrl()}">
                            <div class="card-img-top position-relative" style="height: 140px; overflow: hidden;">
                                <img src="<c:out value='${ann.coverUrl()}'/>" alt="<c:out value='${ann.title()}'/>"
                                     style="width: 100%; height: 100%; object-fit: cover;">
                            </div>
                        </c:if>
                        <div class="card-body d-flex flex-column">
                            <div class="d-flex align-items-center gap-2 mb-2">
                                <c:if test="${ann.pinned()}">
                                    <span class="badge bg-danger-lt"><i class="bi bi-pin-angle-fill me-1"></i>Đã ghim</span>
                                </c:if>
                                <c:if test="${not empty ann.publishAt()}">
                                    <span class="text-secondary small">
                                        <i class="bi bi-calendar3 me-1"></i>${ann.publishAt().dayOfMonth}/${ann.publishAt().monthValue}/${ann.publishAt().year}
                                    </span>
                                </c:if>
                            </div>
                            <h3 class="card-title mb-2">
                                <a href="/announcements/${ann.id()}" class="text-reset text-decoration-none">
                                    <c:out value="${ann.title()}"/>
                                </a>
                            </h3>
                            <c:if test="${not empty ann.summary()}">
                                <p class="text-secondary small mb-3 text-truncate">
                                    <c:out value="${ann.summary()}"/>
                                </p>
                            </c:if>
                            <div class="mt-auto pt-2">
                                <a href="/announcements/${ann.id()}" class="btn btn-sm btn-outline-primary w-100">
                                    Xem chi tiết <i class="bi bi-arrow-right ms-1"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</c:if>

<%-- Khối số liệu (chỉ số tổng hợp) --%>
<div class="card bg-primary-lt border-0 shadow-sm mb-5">
    <div class="card-body py-4">
        <div class="row text-center g-3 align-items-center">
            <div class="col-6 ${not empty medianFirstResponseMinutes ? 'col-md-3' : 'col-md-4'}">
                <div class="text-secondary small mb-1"><i class="bi bi-question-circle me-1"></i>Câu hỏi giải đáp (FAQ)</div>
                <div class="h1 fw-bold text-primary mb-0"><c:out value="${totalFaq != null ? totalFaq : 0}"/>+</div>
                <div class="text-secondary small mt-1">Nội dung hướng dẫn chi tiết</div>
            </div>
            <div class="col-6 ${not empty medianFirstResponseMinutes ? 'col-md-3' : 'col-md-4'}">
                <div class="text-secondary small mb-1"><i class="bi bi-folder2-open me-1"></i>Chuyên mục tư vấn</div>
                <div class="h1 fw-bold text-primary mb-0"><c:out value="${totalCategories}"/></div>
                <div class="text-secondary small mt-1">Bao quát các phòng ban</div>
            </div>
            <c:if test="${not empty medianFirstResponseMinutes}">
                <div class="col-6 col-md-3">
                    <div class="text-secondary small mb-1"><i class="bi bi-lightning-charge me-1"></i>Phản hồi lần đầu</div>
                    <div class="h1 fw-bold text-success mb-0">
                        <c:choose>
                            <c:when test="${medianFirstResponseMinutes < 60}">
                                ~<fmt:formatNumber value="${medianFirstResponseMinutes}" maxFractionDigits="0"/>'
                            </c:when>
                            <c:otherwise>
                                ~<fmt:formatNumber value="${medianFirstResponseMinutes / 60}" maxFractionDigits="1"/>h
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="text-secondary small mt-1">Trung vị trong 30 ngày qua</div>
                </div>
            </c:if>
            <div class="col-6 ${not empty medianFirstResponseMinutes ? 'col-md-3' : 'col-md-4'}">
                <div class="text-secondary small mb-1"><i class="bi bi-shield-check me-1"></i>Hỗ trợ trực tuyến</div>
                <div class="h1 fw-bold text-primary mb-0">24/7</div>
                <div class="text-secondary small mt-1">Chatbot & Tư vấn viên</div>
            </div>
        </div>
    </div>
</div>

<div class="mb-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0"><i class="bi bi-grid text-primary me-2"></i>Chuyên mục hỗ trợ</h2>
        <a href="/topics" class="text-secondary small">Xem tất cả chủ đề →</a>
    </div>

    <c:choose>
        <c:when test="${totalCategories > 0}">
            <div class="row row-cards">
                <c:forEach var="dept" items="${tree}">
                    <c:forEach var="cat" items="${dept.categories()}">
                        <div class="col-sm-6 col-md-4 col-lg-3">
                            <a class="card qa-category-card p-3" href="/faq?category=<c:out value='${cat.id()}'/>">
                                <div class="qa-category-icon">
                                    <i class="bi <c:out value='${empty cat.icon() ? "bi-folder" : cat.icon()}'/>"></i>
                                </div>
                                <div class="fw-bold mb-1 text-truncate"><c:out value="${cat.name()}"/></div>
                                <div class="small text-secondary">
                                    <c:out value="${categoryCounts[cat.id()] != null ? categoryCounts[cat.id()] : 0}"/> câu hỏi
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <qa:emptyState icon="bi-grid" title="Chưa có chuyên mục" message="Hiện tại chưa có chuyên mục hỗ trợ nào được công khai"/>
        </c:otherwise>
    </c:choose>
</div>

<c:if test="${not empty topViewed}">
    <div class="mb-5">
        <h2 class="mb-3"><i class="bi bi-fire text-danger me-2"></i>FAQ được xem nhiều</h2>
        <div class="row row-cards">
            <c:forEach var="faq" items="${topViewed}">
                <div class="col-md-6">
                    <div class="card qa-card-hover h-100">
                        <div class="card-body">
                            <h3 class="card-title mb-2">
                                <a href="/faq/<c:out value='${faq.slug()}'/>" class="text-reset text-decoration-none">
                                    <c:out value="${faq.title()}"/>
                                </a>
                            </h3>
                            <c:if test="${not empty faq.summary()}">
                                <p class="text-secondary small mb-2 text-truncate"><c:out value="${faq.summary()}"/></p>
                            </c:if>
                            <div class="text-secondary small d-flex justify-content-between align-items-center">
                                <span><i class="bi bi-folder me-1"></i><c:out value="${faq.categoryName()}"/></span>
                                <span><i class="bi bi-eye me-1"></i><c:out value="${faq.viewCount()}"/> lượt xem</span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</c:if>

<div class="row row-cards mb-5">
    <div class="col-lg-6">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2 class="mb-0"><i class="bi bi-star-fill text-warning me-2"></i>FAQ nổi bật</h2>
            <a href="/faq" class="text-secondary small">Xem thêm →</a>
        </div>
        <c:choose>
            <c:when test="${not empty featured}">
                <div class="card">
                    <div class="list-group list-group-flush">
                        <c:forEach var="faq" items="${featured}">
                            <a href="/faq/<c:out value='${faq.slug()}'/>" class="list-group-item list-group-item-action p-3">
                                <div class="d-flex justify-content-between align-items-center mb-1">
                                    <span class="badge bg-blue-lt"><c:out value="${faq.categoryName()}"/></span>
                                    <span class="small text-secondary"><i class="bi bi-hand-thumbs-up me-1"></i><c:out value="${faq.helpfulCount()}"/></span>
                                </div>
                                <div class="fw-bold text-reset mb-1"><c:out value="${faq.title()}"/></div>
                                <c:if test="${not empty faq.summary()}">
                                    <div class="small text-secondary text-truncate"><c:out value="${faq.summary()}"/></div>
                                </c:if>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card p-3">
                    <qa:emptyState icon="bi-star" title="Chưa có câu hỏi nổi bật" message="Các câu hỏi được bình chọn hữu ích sẽ hiển thị tại đây"/>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="col-lg-6">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2 class="mb-0"><i class="bi bi-clock-history text-primary me-2"></i>Mới cập nhật</h2>
            <a href="/faq" class="text-secondary small">Xem thêm →</a>
        </div>
        <c:choose>
            <c:when test="${not empty latest}">
                <div class="card">
                    <div class="list-group list-group-flush">
                        <c:forEach var="faq" items="${latest}">
                            <a href="/faq/<c:out value='${faq.slug()}'/>" class="list-group-item list-group-item-action p-3">
                                <div class="d-flex justify-content-between align-items-center mb-1">
                                    <span class="badge bg-blue-lt"><c:out value="${faq.categoryName()}"/></span>
                                    <span class="small text-secondary"><i class="bi bi-eye me-1"></i><c:out value="${faq.viewCount()}"/></span>
                                </div>
                                <div class="fw-bold text-reset mb-1"><c:out value="${faq.title()}"/></div>
                                <c:if test="${not empty faq.summary()}">
                                    <div class="small text-secondary text-truncate"><c:out value="${faq.summary()}"/></div>
                                </c:if>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card p-3">
                    <qa:emptyState icon="bi-clock" title="Chưa có câu hỏi mới" message="Các câu hỏi cập nhật gần đây sẽ hiển thị tại đây"/>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="qa-cta-section text-center mb-5">
    <h2 class="mb-2">Chưa tìm thấy câu trả lời?</h2>
    <p class="text-secondary mb-4">Hãy gửi yêu cầu hỗ trợ hoặc kết nối trực tiếp với đội ngũ tư vấn viên của chúng tôi.</p>
    <div class="d-flex flex-wrap justify-content-center gap-3">
        <a class="btn btn-primary" href="/student/tickets/new">
            <i class="bi bi-plus-circle me-2"></i>Tạo yêu cầu tư vấn
        </a>
        <a class="btn btn-teal text-white" href="/student/chat">
            <i class="bi bi-chat-dots me-2"></i>Chat với tư vấn viên
        </a>
        <a class="btn btn-outline-primary" href="/student/appointments/book">
            <i class="bi bi-calendar-event me-2"></i>Đặt lịch hẹn
        </a>
    </div>
</div>

<div class="card">
    <div class="card-body">
        <div class="row align-items-center">
            <div class="col-md-6 mb-3 mb-md-0">
                <div class="d-flex align-items-center">
                    <div class="avatar avatar-md bg-blue-lt me-3">
                        <i class="bi bi-headset fs-2"></i>
                    </div>
                    <div>
                        <h4 class="mb-1">Hỗ trợ và Giải đáp thắc mắc sinh viên</h4>
                        <div class="text-secondary small">Trường Đại học Sư phạm Kỹ thuật TP.HCM (HCMUTE)</div>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="d-flex flex-wrap justify-content-md-end gap-3 small">
                    <c:if test="${not empty contactEmail}">
                        <div>
                            <i class="bi bi-envelope text-primary me-1"></i>
                            <a href="mailto:<c:out value='${contactEmail}'/>" class="text-reset">
                                <c:out value="${contactEmail}"/>
                            </a>
                        </div>
                    </c:if>
                    <c:if test="${not empty contactPhone}">
                        <div>
                            <i class="bi bi-telephone text-primary me-1"></i>
                            <a href="tel:<c:out value='${contactPhone}'/>" class="text-reset">
                                <c:out value="${contactPhone}"/>
                            </a>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
