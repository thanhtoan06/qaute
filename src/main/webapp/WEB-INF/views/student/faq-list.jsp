<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Kho tri thức hỏi đáp FAQ | QAUTE</title>

<div class="qa-page-student-faq-list" data-qa-view="student-faq-list">
<style>
/* ==========================================================================
   STUDENT FAQ KNOWLEDGE BASE - MODERN DESIGN SYSTEM 2026
   Palette: Azure Navy (#091e3e) -> Royal Blue (#124991) -> Deep Teal (#087d92)
   Accent: Electric Cyan (#00d2ff), Emerald (#10b981), Amber (#f59e0b)
   ========================================================================== */
.qa-page-student-faq-list {
    --student-primary: #124991;
    --student-primary-dark: #091e3e;
    --student-teal: #087d92;
    --student-cyan: #00d2ff;
    --student-emerald: #10b981;
    --student-amber: #f59e0b;
    --student-border: #e2e8f0;
    --student-card-shadow: 0 10px 30px -5px rgba(9, 30, 62, 0.07), 0 4px 10px -2px rgba(9, 30, 62, 0.04);
    min-width: 0;
    color: #1e293b;
    font-feature-settings: "cv02", "cv03", "cv04", "cv11";
}

/* 1. HERO BANNER */
.qa-page-student-faq-list .student-hero-banner {
    position: relative;
    background: linear-gradient(135deg, #091e3e 0%, #124991 55%, #087d92 100%);
    border-radius: 24px;
    padding: clamp(1.5rem, 3vw, 2.25rem);
    color: #ffffff;
    overflow: hidden;
    margin-bottom: 2rem;
    box-shadow: 0 20px 40px -15px rgba(9, 30, 62, 0.35);
    border: 1px solid rgba(255, 255, 255, 0.12);
}
.qa-page-student-faq-list .student-hero-banner::before {
    content: '';
    position: absolute;
    top: -40%;
    right: -10%;
    width: 420px;
    height: 420px;
    background: radial-gradient(circle, rgba(0, 210, 255, 0.22) 0%, rgba(18, 73, 145, 0) 70%);
    border-radius: 50%;
    pointer-events: none;
}
.qa-page-student-faq-list .student-hero-banner::after {
    content: '';
    position: absolute;
    bottom: -50%;
    left: 20%;
    width: 350px;
    height: 350px;
    background: radial-gradient(circle, rgba(16, 185, 129, 0.18) 0%, rgba(9, 30, 62, 0) 70%);
    border-radius: 50%;
    pointer-events: none;
}
.qa-page-student-faq-list .hero-content-wrap {
    position: relative;
    z-index: 2;
}
.qa-page-student-faq-list .hero-eyebrow-pill {
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
    background: rgba(255, 255, 255, 0.12);
    backdrop-filter: blur(8px);
    -webkit-backdrop-filter: blur(8px);
    border: 1px solid rgba(255, 255, 255, 0.22);
    border-radius: 999px;
    padding: 0.35rem 0.95rem;
    font-size: 0.78rem;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.08em;
    color: #bbf2f6;
    margin-bottom: 0.75rem;
}
.qa-page-student-faq-list .hero-eyebrow-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background-color: var(--student-cyan);
    box-shadow: 0 0 10px var(--student-cyan);
    animation: studentPulse 2s infinite ease-in-out;
}
@keyframes studentPulse {
    0%, 100% { transform: scale(1); opacity: 1; }
    50% { transform: scale(1.35); opacity: 0.7; }
}
.qa-page-student-faq-list .hero-title {
    font-size: clamp(1.45rem, 2.5vw, 2.15rem);
    font-weight: 800;
    letter-spacing: -0.03em;
    line-height: 1.25;
    margin-bottom: 0.5rem;
    color: #ffffff;
}
.qa-page-student-faq-list .hero-subtitle {
    font-size: 0.95rem;
    color: rgba(255, 255, 255, 0.85);
    max-width: 650px;
    line-height: 1.55;
    margin: 0;
}
.qa-page-student-faq-list .hero-action-btn {
    background: rgba(255, 255, 255, 0.14);
    border: 1px solid rgba(255, 255, 255, 0.25);
    color: #ffffff;
    backdrop-filter: blur(6px);
    border-radius: 12px;
    padding: 0.6rem 1.15rem;
    font-size: 0.85rem;
    font-weight: 600;
    transition: all 0.22s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
}
.qa-page-student-faq-list .hero-action-btn:hover {
    background: rgba(255, 255, 255, 0.28);
    color: #ffffff;
    transform: translateY(-2px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

/* 2. FILTER & SEARCH CARD */
.qa-page-student-faq-list .filter-card {
    background: #ffffff;
    border: 1px solid var(--student-border);
    border-radius: 20px;
    box-shadow: var(--student-card-shadow);
    margin-bottom: 2rem;
    overflow: hidden;
}
.qa-page-student-faq-list .filter-card-header {
    background: #f8fafc;
    border-bottom: 1px solid #e2e8f0;
    padding: 0.5rem 1rem 0;
}
.qa-page-student-faq-list .filter-nav-tabs {
    border-bottom: none;
    gap: 0.35rem;
}
.qa-page-student-faq-list .filter-nav-tabs .nav-link {
    border: none;
    border-radius: 12px 12px 0 0;
    font-weight: 700;
    font-size: 0.88rem;
    color: #64748b;
    padding: 0.75rem 1.25rem;
    transition: all 0.2s ease;
    display: inline-flex;
    align-items: center;
    gap: 0.45rem;
    background: transparent;
}
.qa-page-student-faq-list .filter-nav-tabs .nav-link:hover {
    color: #091e3e;
    background: rgba(18, 73, 145, 0.05);
}
.qa-page-student-faq-list .filter-nav-tabs .nav-link.active {
    color: #124991;
    background: #ffffff;
    border-top: 3px solid #124991;
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.03);
}
.qa-page-student-faq-list .filter-card-body {
    padding: 1.35rem 1.5rem;
}

/* 3. FORM CONTROLS */
.qa-page-student-faq-list .form-label {
    color: #1e293b;
    font-size: 0.85rem;
    font-weight: 700;
    margin-bottom: 0.45rem;
}
.qa-page-student-faq-list .form-control,
.qa-page-student-faq-list .form-select {
    border: 1.5px solid #cbd5e1;
    border-radius: 12px;
    padding: 0.65rem 0.95rem;
    color: #1e293b;
    font-size: 0.92rem;
    background-color: #ffffff;
    transition: all 0.2s ease;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
}
.qa-page-student-faq-list .form-control:focus,
.qa-page-student-faq-list .form-select:focus {
    border-color: #124991;
    box-shadow: 0 0 0 4px rgba(18, 73, 145, 0.12);
    outline: none;
}

/* 4. FAQ ARTICLE CARDS */
.qa-page-student-faq-list .faq-card {
    border: 1px solid var(--student-border);
    border-radius: 20px;
    background: #ffffff;
    box-shadow: var(--student-card-shadow);
    transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
    overflow: hidden;
    height: 100%;
    display: flex;
    flex-direction: column;
}
.qa-page-student-faq-list .faq-card:hover {
    transform: translateY(-4px);
    border-color: #cbd5e1;
    box-shadow: 0 16px 36px -8px rgba(9, 30, 62, 0.12), 0 4px 12px -2px rgba(9, 30, 62, 0.06);
}
.qa-page-student-faq-list .faq-card-body {
    padding: 1.35rem;
    display: flex;
    flex-direction: column;
    flex: 1 1 auto;
}
.qa-page-student-faq-list .faq-card-title {
    font-size: 1.08rem;
    font-weight: 800;
    line-height: 1.4;
    color: #0f172a;
    text-decoration: none;
    transition: color 0.18s ease;
    margin-bottom: 0.65rem;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
.qa-page-student-faq-list .faq-card-title:hover {
    color: #124991;
}
.qa-page-student-faq-list .faq-card-summary {
    color: #64748b;
    font-size: 0.88rem;
    line-height: 1.55;
    margin-bottom: 1.15rem;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
.qa-page-student-faq-list .faq-card-footer {
    margin-top: auto;
    padding-top: 0.85rem;
    border-top: 1px solid #f1f5f9;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 0.5rem;
    font-size: 0.8rem;
}
.qa-page-student-faq-list .faq-bookmark-btn {
    width: 36px;
    height: 36px;
    border-radius: 10px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    border: 1.5px solid #cbd5e1;
    background: #ffffff;
    color: #64748b;
    transition: all 0.2s ease;
    padding: 0;
}
.qa-page-student-faq-list .faq-bookmark-btn:hover {
    color: #ef4444;
    border-color: #fca5a5;
    background: #fef2f2;
    transform: scale(1.08);
}
.qa-page-student-faq-list .faq-bookmark-btn.btn-danger {
    background: #fef2f2 !important;
    border-color: #fca5a5 !important;
    color: #ef4444 !important;
    box-shadow: 0 2px 8px rgba(239, 68, 68, 0.2);
}

/* 5. BUTTONS */
.qa-page-student-faq-list .btn {
    border-radius: 12px;
    font-weight: 700;
    transition: all 0.2s ease;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 0.45rem;
}
.qa-page-student-faq-list .btn-primary {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%);
    border: none;
    color: #ffffff;
    box-shadow: 0 4px 12px rgba(18, 73, 145, 0.2);
}
.qa-page-student-faq-list .btn-primary:hover {
    background: linear-gradient(135deg, #091e3e 0%, #124991 100%);
    transform: translateY(-1px);
}
.qa-page-student-faq-list .btn-outline-secondary {
    border-color: #cbd5e1;
    color: #475569;
    background: #ffffff;
}
.qa-page-student-faq-list .btn-outline-secondary:hover {
    background: #f1f5f9;
    color: #0f172a;
}

@media (max-width: 767.98px) {
    .qa-page-student-faq-list .student-hero-banner {
        border-radius: 18px;
        padding: 1.25rem;
    }
    .qa-page-student-faq-list .filter-card-header {
        padding: 0.35rem 0.5rem 0;
    }
    .qa-page-student-faq-list .filter-nav-tabs .nav-link {
        padding: 0.55rem 0.85rem;
        font-size: 0.8rem;
    }
}
</style>

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
                    <div class="avatar avatar-xl bg-light text-muted rounded-circle mx-auto mb-3" style="width: 72px; height: 72px; display: inline-flex; align-items: center; justify-content: center; font-size: 2.2rem;">
                        <i class="bi bi-search"></i>
                    </div>
                    <h3 class="fw-bold text-dark mb-1">Chưa tìm thấy bài viết phù hợp</h3>
                    <p class="text-secondary small mb-4 mx-auto" style="max-width: 440px;">
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
