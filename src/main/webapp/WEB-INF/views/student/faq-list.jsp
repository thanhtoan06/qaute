<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Kho tri thức hỏi đáp FAQ | QAUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/faq-list.css">
</head>

<div class="qa-page-student-faq-list" data-qa-view="student-faq-list">


<div class="row justify-content-center">
    <div class="col-12 col-xl-11">

        <!-- 1. HERO BANNER -->
        <div class="student-hero-banner">
            <div class="hero-content-wrap d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3">
                <div>
                    <div class="hero-eyebrow-pill">
                        <span class="hero-eyebrow-dot"></span>
                        <span>Kho Tri Thức Hỏi Đáp • Giải Đáp Tức Thì 24/7</span>
                    </div>
                    <h1 class="hero-title">
                        <i class="bi bi-patch-question-fill text-info me-2"></i>Hỏi Đáp Thường Gặp (FAQ)
                    </h1>
                    <p class="hero-subtitle">
                        Tra cứu nhanh câu trả lời chuẩn xác cho các thắc mắc về học tập, học vụ, học bổng, tốt nghiệp và đời sống sinh viên mà không cần gửi yêu cầu chờ đợi.
                    </p>
                </div>
                <div class="d-flex flex-wrap gap-2">
                    <a href="${ctx}/student/faq/recent" class="hero-action-btn">
                        <i class="bi bi-clock-history"></i>
                        <span>FAQ đã xem</span>
                    </a>
                    <a href="${ctx}/student/tickets/new" class="hero-action-btn">
                        <i class="bi bi-ticket-perforated"></i>
                        <span>Gửi Ticket mới</span>
                    </a>
                </div>
            </div>
        </div>

        <!-- 2. FILTER & SEARCH CARD -->
        <section class="filter-card">
            <div class="filter-card-header">
                <ul class="nav filter-nav-tabs flex-nowrap overflow-auto" aria-label="Lọc FAQ">
                    <li class="nav-item">
                        <a class="nav-link ${tab eq 'latest' ? 'active' : ''}" href="<c:url value='/student/faq'><c:param name='tab' value='latest'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty q}'><c:param name='q' value='${q}'/></c:if></c:url>">
                            <i class="bi bi-sparkles text-primary"></i>
                            <span>Mới nhất</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${tab eq 'views' ? 'active' : ''}" href="<c:url value='/student/faq'><c:param name='tab' value='views'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty q}'><c:param name='q' value='${q}'/></c:if></c:url>">
                            <i class="bi bi-fire text-danger"></i>
                            <span>Xem nhiều nhất</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${tab eq 'helpful' ? 'active' : ''}" href="<c:url value='/student/faq'><c:param name='tab' value='helpful'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty q}'><c:param name='q' value='${q}'/></c:if></c:url>">
                            <i class="bi bi-hand-thumbs-up-fill text-success"></i>
                            <span>Hữu ích nhất</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${tab eq 'saved' ? 'active' : ''}" href="<c:url value='/student/faq'><c:param name='tab' value='saved'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty q}'><c:param name='q' value='${q}'/></c:if></c:url>">
                            <i class="bi bi-heart-fill text-danger"></i>
                            <span>Bài viết đã lưu</span>
                        </a>
                    </li>
                </ul>
            </div>
            <div class="filter-card-body">
                <form method="get" action="${ctx}/student/faq" class="row g-3 align-items-end">
                    <input type="hidden" name="tab" value="${tab}"/>
                    <div class="col-12 col-md-4">
                        <label class="form-label" for="faq-category">
                            <i class="bi bi-diagram-3 me-1 text-primary"></i>Chuyên mục câu hỏi
                        </label>
                        <select id="faq-category" class="form-select" name="category">
                            <option value="">Tất cả các chuyên mục</option>
                            <c:forEach var="department" items="${tree}">
                                <optgroup label="<c:out value='${department.name()}'/>">
                                    <c:forEach var="root" items="${department.categories()}">
                                        <option value="${root.id()}" ${category eq root.id() ? 'selected' : ''}><c:out value="${root.name()}"/></option>
                                        <c:forEach var="child" items="${root.children()}">
                                            <option value="${child.id()}" ${category eq child.id() ? 'selected' : ''}>&nbsp;&nbsp;└ <c:out value="${child.name()}"/></option>
                                        </c:forEach>
                                    </c:forEach>
                                </optgroup>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-12 col-md-6">
                        <label class="form-label" for="faq-query">
                            <i class="bi bi-search me-1 text-primary"></i>Từ khóa tìm kiếm
                        </label>
                        <input id="faq-query" class="form-control" name="q" maxlength="100" value="<c:out value='${q}'/>" placeholder="Nhập từ khóa câu hỏi cần tìm (ví dụ: học phí, bảng điểm, chuẩn đầu ra)..."/>
                    </div>
                    <div class="col-12 col-md-2 d-flex gap-2">
                        <button class="btn btn-primary flex-fill py-2" type="submit" title="Tìm kiếm">
                            <i class="bi bi-search"></i>
                            <span>Tìm kiếm</span>
                        </button>
                        <a class="btn btn-outline-secondary py-2" href="${ctx}/student/faq" title="Xóa bộ lọc" aria-label="Xóa bộ lọc">
                            <i class="bi bi-arrow-counterclockwise"></i>
                        </a>
                    </div>
                </form>
            </div>
        </section>

        <!-- 3. FAQ ARTICLES GRID -->
        <c:choose>
            <c:when test="${not empty page.content}">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <span class="text-secondary small fw-bold">
                        <i class="bi bi-check2-circle text-success me-1"></i>Tìm thấy <strong class="text-dark">${page.totalElements}</strong> bài viết phù hợp
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
                                                data-faq-bookmark data-faq-id="${faq.id()}" data-bookmarked="${bookmarkedIds.contains(faq.id())}" aria-pressed="${bookmarkedIds.contains(faq.id())}"
                                                title="${bookmarkedIds.contains(faq.id()) ? 'Đã lưu bài viết' : 'Lưu bài viết vào danh sách'}">
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
                                        <c:forEach var="tag" items="${faq.tags()}">
                                            <span class="badge bg-secondary-lt px-2 py-1 rounded-pill">#<c:out value="${tag}"/></span>
                                        </c:forEach>
                                        <span class="ms-auto text-secondary small">
                                            <i class="bi bi-eye me-1"></i><c:out value="${faq.viewCount()}"/>
                                        </span>
                                        <span class="text-secondary small">
                                            <i class="bi bi-hand-thumbs-up me-1"></i><c:out value="${faq.helpfulCount()}"/>
                                        </span>
                                        <c:if test="${viewedIds.contains(faq.id())}">
                                            <span class="badge bg-azure-lt rounded-pill">Đã xem</span>
                                        </c:if>
                                    </div>
                                </div>
                            </article>
                        </div>
                    </c:forEach>
                </div>

                <div class="d-flex justify-content-center mt-5">
                    <qa:pagination page="${page}" baseUrl="${ctx}/student/faq" params="${queryString}"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card p-5 border-0 shadow-sm rounded-4 text-center">
                    <div class="avatar avatar-xl bg-light text-muted rounded-circle mx-auto mb-3 u-width-72px u-height-72px u-display-inline-flex u-align-items-center u-justify-content-center u-text-size-2-2rem">
                        <i class="bi bi-search"></i>
                    </div>
                    <h3 class="fw-bold text-dark mb-1">Chưa tìm thấy bài viết phù hợp</h3>
                    <p class="text-secondary small mb-4 mx-auto u-max-w-440px">
                        Hãy thử thay đổi từ khóa tìm kiếm, chọn chuyên mục khác hoặc duyệt qua tất cả các bài hướng dẫn FAQ mới nhất của hệ thống.
                    </p>
                    <div>
                        <a href="${ctx}/student/faq" class="btn btn-primary rounded-pill px-4 py-2 shadow-sm">
                            <i class="bi bi-arrow-repeat me-1"></i> Xem tất cả câu hỏi FAQ
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>

<script src="${ctx}/assets/js/student/faq-bookmark.js"></script>
</div>
