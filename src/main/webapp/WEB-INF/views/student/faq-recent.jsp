<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Lịch sử bài viết FAQ đã xem | QAUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/faq-recent.css">
</head>

<div class="qa-page-student-faq-recent" data-qa-view="student-faq-recent">


<div class="row justify-content-center">
    <div class="col-12 col-xl-11">

        <!-- 1. HERO BANNER -->
        <div class="student-hero-banner">
            <div class="hero-content-wrap d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3">
                <div>
                    <div class="hero-eyebrow-pill">
                        <span class="hero-eyebrow-dot"></span>
                        <span>Lịch Sử Tra Cứu • Bài Viết Đã Đọc</span>
                    </div>
                    <h1 class="hero-title">
                        <i class="bi bi-clock-history text-info me-2"></i>FAQ Bạn Đã Xem Gần Đây
                    </h1>
                    <p class="hero-subtitle">
                        Xem lại nhanh các bài hướng dẫn hỏi đáp mà bạn đã từng mở đọc để thuận tiện áp dụng cho thủ tục học tập hoặc kiểm tra lại thông tin.
                    </p>
                </div>
                <div class="d-flex flex-wrap gap-2">
                    <a href="${ctx}/student/faq" class="hero-action-btn">
                        <i class="bi bi-search"></i>
                        <span>Khám phá toàn bộ FAQ</span>
                    </a>
                    <a href="${ctx}/student/faq?tab=saved" class="hero-action-btn">
                        <i class="bi bi-heart-fill text-danger"></i>
                        <span>Bài viết đã lưu</span>
                    </a>
                </div>
            </div>
        </div>

        <!-- 2. RECENT FAQ ARTICLES GRID -->
        <c:choose>
            <c:when test="${not empty page.content}">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <span class="text-secondary small fw-bold">
                        <i class="bi bi-journal-bookmark text-primary me-1"></i>Có <strong class="text-dark">${page.totalElements}</strong> bài viết đã xem
                    </span>
                    <span class="badge bg-light text-secondary border">Trang ${page.number + 1} / ${page.totalPages}</span>
                </div>

                <div class="row g-4">
                    <c:forEach var="faq" items="${page.content}">
                        <div class="col-12 col-md-6">
                            <article class="faq-card">
                                <div class="faq-card-body">
                                    <div class="d-flex align-items-start gap-2 mb-2">
                                        <h3 class="card-title flex-fill m-0">
                                            <a class="faq-card-title m-0" href="${ctx}/faq/<c:out value='${faq.slug()}'/>">
                                                <c:out value="${faq.title()}"/>
                                            </a>
                                        </h3>
                                        <button class="faq-bookmark-btn ${bookmarkedIds.contains(faq.id()) ? 'btn-danger' : 'btn-outline-secondary'}" type="button"
                                                data-faq-bookmark data-faq-id="${faq.id()}" data-bookmarked="${bookmarkedIds.contains(faq.id())}"
                                                aria-pressed="${bookmarkedIds.contains(faq.id())}" title="${bookmarkedIds.contains(faq.id()) ? 'Đã lưu bài viết' : 'Lưu bài viết vào danh sách'}">
                                            <i class="bi ${bookmarkedIds.contains(faq.id()) ? 'bi-heart-fill' : 'bi-heart'}"></i>
                                        </button>
                                    </div>

                                    <c:if test="${not empty faq.summary()}">
                                        <p class="faq-card-summary">
                                            <c:out value="${fn:length(faq.summary()) > 180 ? fn:substring(faq.summary(), 0, 180).concat('...') : faq.summary()}"/>
                                        </p>
                                    </c:if>

                                    <div class="faq-card-footer">
                                        <span class="badge bg-blue-lt px-2 py-1 rounded-pill">
                                            <i class="bi bi-folder2 me-1"></i><c:out value="${faq.categoryName()}"/>
                                        </span>
                                        <span class="ms-auto text-secondary small">
                                            <i class="bi bi-clock-history me-1 text-primary"></i>Đã xem: <c:out value="${viewedAt[faq.id()]}"/>
                                        </span>
                                    </div>
                                </div>
                            </article>
                        </div>
                    </c:forEach>
                </div>

                <div class="d-flex justify-content-center mt-5">
                    <qa:pagination page="${page}" baseUrl="${ctx}/student/faq/recent"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card p-5 border-0 shadow-sm rounded-4 text-center">
                    <div class="avatar avatar-xl bg-light text-muted rounded-circle mx-auto mb-3 u-width-72px u-height-72px u-display-inline-flex u-align-items-center u-justify-content-center u-text-size-2-2rem">
                        <i class="bi bi-clock-history"></i>
                    </div>
                    <h3 class="fw-bold text-dark mb-1">Chưa có bài viết FAQ nào đã xem</h3>
                    <p class="text-secondary small mb-4 mx-auto u-max-w-440px">
                        Các bài viết hướng dẫn hỏi đáp mà bạn mở xem sẽ được lưu tự động tại trang này để bạn có thể tìm lại dễ dàng bất cứ khi nào.
                    </p>
                    <div>
                        <a href="${ctx}/student/faq" class="btn btn-primary rounded-pill px-4 py-2 shadow-sm">
                            <i class="bi bi-search me-1"></i> Khám phá kho tri thức FAQ
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>

<script src="${ctx}/assets/js/student/faq-bookmark.js"></script>
</div>
