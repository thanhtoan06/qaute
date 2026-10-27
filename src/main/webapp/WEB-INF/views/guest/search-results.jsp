<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Tìm kiếm FAQ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/guest/search-results.css">
</head>





<div class="qaute-search-page">
    <nav aria-label="breadcrumb" class="search-breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="/faq">Hỏi đáp</a></li>
            <li class="breadcrumb-item active" aria-current="page">Tìm kiếm</li>
        </ol>
    </nav>

    <%-- Nhập từ khóa tìm kiếm và giữ lại các bộ lọc đang áp dụng. --%>
    <section class="search-hero">
        <div class="search-hero-content">
            <div class="search-eyebrow">
                <i class="bi bi-stars"></i>
                QAUTE · KNOWLEDGE SEARCH
            </div>
            <h1>Tìm câu trả lời.<br><span>Khơi thông tri thức.</span></h1>
            <p class="search-hero-description">
                Tra cứu kho câu hỏi thường gặp của QAUTE để tìm thông tin học vụ, thủ tục sinh viên và hướng dẫn hỗ trợ phù hợp.
            </p>

            <div class="search-form-shell">
                <form method="get" action="/search" class="row g-2 align-items-center">
                    <c:if test="${not empty category}">
                        <input type="hidden" name="category" value="<c:out value='${category}'/>"/>
                    </c:if>
                    <c:if test="${not empty tag}">
                        <input type="hidden" name="tag" value="<c:out value='${tag}'/>"/>
                    </c:if>
                    <c:if test="${not empty sort}">
                        <input type="hidden" name="sort" value="<c:out value='${sort}'/>"/>
                    </c:if>
                    <div class="col-md-10">
                        <div class="input-group">
                            <span class="search-field-icon">
                                <i class="bi bi-search"></i>
                            </span>
                            <input type="text" name="q" class="form-control"
                                   placeholder="Nhập từ khóa tìm kiếm FAQ..."
                                   value="<c:out value='${q}'/>"
                                   data-qa-suggest="SEARCH"
                                   autofocus required minlength="2"/>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn w-100">
                            <i class="bi bi-search"></i>
                            Tìm kiếm
                        </button>
                    </div>
                </form>
            </div>

            <div class="search-hero-bottom">
                <span class="search-hero-pill"><i class="bi bi-lightning-charge-fill"></i> Gợi ý tìm kiếm</span>
                <span class="search-hero-pill"><i class="bi bi-folder2-open"></i> Lọc theo chuyên mục</span>
                <span class="search-hero-pill"><i class="bi bi-chat-square-text-fill"></i> Kho tri thức FAQ</span>
            </div>
        </div>
    </section>

    <c:if test="${not empty errorMessage}">
        <div class="search-error mb-4" role="alert">
            <i class="bi bi-exclamation-triangle-fill"></i>
            <div><c:out value="${errorMessage}"/></div>
        </div>
    </c:if>

    <%-- Cung cấp bộ lọc sắp xếp và chuyên mục cạnh kết quả tìm kiếm. --%>
    <div class="row search-workspace g-4">
        <div class="col-lg-3">
            <aside class="search-sidebar">
                <c:if test="${not empty category or not empty tag or (not empty sort and sort != 'RELEVANCE')}">
                    <a href="<c:url value='/search'><c:param name='q' value='${q}'/></c:url>" class="search-clear-filter">
                        <i class="bi bi-x-circle"></i>
                        Xóa bộ lọc
                    </a>
                </c:if>

                <%-- Chọn thứ tự ưu tiên của kết quả tìm kiếm. --%>
                <section class="search-filter-card">
                    <div class="search-filter-heading">
                        <span class="search-filter-icon"><i class="bi bi-sort-down"></i></span>
                        <h2>Sắp xếp kết quả</h2>
                    </div>
                    <div class="search-filter-list list-group list-group-flush">
                        <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='RELEVANCE'/></c:url>"
                           class="list-group-item ${sort == 'RELEVANCE' or empty sort ? 'active fw-bold' : ''}">
                            <i class="bi bi-bullseye"></i>
                            Độ liên quan
                        </a>
                        <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='LATEST'/></c:url>"
                           class="list-group-item ${sort == 'LATEST' ? 'active fw-bold' : ''}">
                            <i class="bi bi-clock-history"></i>
                            Mới cập nhật
                        </a>
                        <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='MOST_VIEWED'/></c:url>"
                           class="list-group-item ${sort == 'MOST_VIEWED' ? 'active fw-bold' : ''}">
                            <i class="bi bi-eye"></i>
                            Xem nhiều nhất
                        </a>
                        <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='MOST_HELPFUL'/></c:url>"
                           class="list-group-item ${sort == 'MOST_HELPFUL' ? 'active fw-bold' : ''}">
                            <i class="bi bi-hand-thumbs-up"></i>
                            Hữu ích nhất
                        </a>
                    </div>
                </section>

                <%-- Lọc kết quả theo chuyên mục và đơn vị phụ trách. --%>
                <section class="search-filter-card">
                    <div class="search-filter-heading">
                        <span class="search-filter-icon"><i class="bi bi-grid-3x3-gap-fill"></i></span>
                        <h2>Chuyên mục</h2>
                    </div>
                    <div class="search-filter-list list-group list-group-flush">
                        <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                           class="list-group-item ${empty category ? 'active fw-bold' : ''}">
                            <i class="bi bi-grid"></i>
                            Tất cả chuyên mục
                        </a>

                        <c:forEach var="dept" items="${tree}">
                            <div class="search-filter-dept">
                                <i class="bi bi-building me-1"></i>
                                <c:out value="${dept.name()}"/>
                            </div>
                            <c:forEach var="cat" items="${dept.categories()}">
                                <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:param name='category' value='${cat.id()}'/><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                                   class="list-group-item search-filter-category ${category == cat.id() ? 'active fw-bold' : ''}">
                                    <i class="bi <c:out value='${empty cat.icon() ? "bi-folder" : cat.icon()}'/>"></i>
                                    <span><c:out value="${cat.name()}"/></span>
                                </a>
                            </c:forEach>
                        </c:forEach>
                    </div>
                </section>
            </aside>
        </div>

        <div class="col-lg-9 search-main">
            <c:choose>
                <c:when test="${not empty q and page.totalElements == 0 and empty errorMessage}">
                    <%-- Gợi ý cách tìm khác và danh mục tham khảo khi không có kết quả. --%>
                    <section class="search-empty">
                        <div class="search-empty-icon">
                            <i class="bi bi-search"></i>
                        </div>
                        <h2>Không tìm thấy kết quả phù hợp</h2>
                        <p class="search-empty-lead">
                            Không tìm thấy kết quả cho từ khóa
                            <strong>“<c:out value="${q}"/>”</strong>.
                            Ông có thể thử từ khóa khác hoặc chọn một chuyên mục gợi ý bên dưới.
                        </p>

                        <div class="search-tips">
                            <div class="search-tips-heading">
                                <i class="bi bi-lightbulb-fill"></i>
                                Mẹo tìm kiếm
                            </div>
                            <ul>
                                <li>Kiểm tra lại chính tả của từ khóa tìm kiếm.</li>
                                <li>Thử dùng các từ khóa ngắn hơn hoặc các từ khóa thông dụng khác.</li>
                                <li>Bỏ bớt các bộ lọc chuyên mục hoặc chọn "Tất cả chuyên mục".</li>
                            </ul>
                        </div>

                        <div class="search-suggestions">
                            <div class="search-suggestions-heading">
                                <i class="bi bi-grid-fill"></i>
                                Các chuyên mục gợi ý
                            </div>
                            <div class="d-flex flex-wrap justify-content-center gap-2">
                                <c:forEach var="dept" items="${tree}">
                                    <c:forEach var="cat" items="${dept.categories()}">
                                        <a href="/faq?category=<c:out value='${cat.id()}'/>" class="search-suggestion-link">
                                            <i class="bi <c:out value='${empty cat.icon() ? "bi-folder" : cat.icon()}'/>"></i>
                                            <c:out value="${cat.name()}"/>
                                        </a>
                                    </c:forEach>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="search-empty-cta">
                            <p>Vẫn chưa tìm thấy thông tin bạn cần?</p>
                            <a href="<c:url value='/student/tickets/new'><c:param name='q' value='${q}'/></c:url>" class="btn btn-primary">
                                <i class="bi bi-pencil-square"></i>
                                Tạo yêu cầu hỗ trợ
                            </a>
                        </div>
                    </section>
                </c:when>

                <c:otherwise>
                    <c:if test="${not empty q and empty errorMessage}">
                        <div class="search-results-toolbar">
                            <div class="search-results-heading">
                                <span class="search-results-heading-icon">
                                    <i class="bi bi-journals"></i>
                                </span>
                                <div>
                                    <h2>Kết quả tìm kiếm</h2>
                                    <p>
                                        Tìm thấy <strong class="text-dark"><c:out value="${page.totalElements}"/></strong>
                                        kết quả cho từ khóa "<strong class="text-dark"><c:out value="${q}"/></strong>"
                                        <c:if test="${not empty selectedCategoryName}">
                                            trong chuyên mục "<strong class="text-dark"><c:out value="${selectedCategoryName}"/></strong>"
                                        </c:if>
                                    </p>
                                </div>
                            </div>
                            <span class="search-results-count">
                                <i class="bi bi-check2-circle"></i>
                                FAQ
                            </span>
                        </div>
                    </c:if>

                    <%-- Hiển thị các FAQ phù hợp và phân trang kết quả. --%>
                    <c:forEach var="item" items="${page.content}">
                        <article class="card search-result-card qa-card-hover">
                            <div class="card-body">
                                <h2 class="search-result-title">
                                    <a href="/faq/<c:out value='${item.slug()}'/>">
                                        <c:out value="${item.titleHtml()}" escapeXml="false"/>
                                    </a>
                                </h2>
                                <p class="search-result-snippet">
                                    <c:out value="${item.snippetHtml()}" escapeXml="false"/>
                                </p>

                                <div class="search-result-meta">
                                    <c:if test="${not empty item.categoryName()}">
                                        <span class="search-result-category">
                                            <i class="bi bi-folder2-open"></i>
                                            <c:out value="${item.categoryName()}"/>
                                        </span>
                                    </c:if>
                                    <span>
                                        <i class="bi bi-eye-fill"></i>
                                        <c:out value="${item.viewCount()}"/> lượt xem
                                    </span>
                                    <span>
                                        <i class="bi bi-hand-thumbs-up-fill"></i>
                                        <c:out value="${item.helpfulCount()}"/> hữu ích
                                    </span>
                                    <c:if test="${not empty item.updatedAt()}">
                                        <span>
                                            <i class="bi bi-clock-history"></i>
                                            Cập nhật: ${item.updatedAt().dayOfMonth}/${item.updatedAt().monthValue}/${item.updatedAt().year}
                                        </span>
                                    </c:if>
                                </div>

                                <div class="search-result-footer">
                                    <span class="text-secondary small">
                                        <i class="bi bi-journal-text me-1"></i>
                                        Bài viết trong kho tri thức QAUTE
                                    </span>
                                    <a href="/faq/<c:out value='${item.slug()}'/>" class="search-read-more">
                                        Đọc bài viết
                                        <i class="bi bi-arrow-right"></i>
                                    </a>
                                </div>
                            </div>
                        </article>
                    </c:forEach>

                    <div class="search-pagination">
                        <qa:pagination page="${page}" baseUrl="/search" params="${queryString}"/>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="search-bottom-help">
                <i class="bi bi-lightbulb-fill"></i>
                <div>
                    <strong>Gợi ý dành cho bạn</strong>
                    <span>Thử kết hợp từ khóa cụ thể với chuyên mục phù hợp để thu hẹp kết quả tìm kiếm.</span>
                </div>
            </div>
        </div>
    </div>
</div>