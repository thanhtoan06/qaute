<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Chủ đề tư vấn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/guest/topics.css">
</head>





<div class="qaute-topics-page">
    <nav aria-label="breadcrumb" class="topics-breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Chủ đề tư vấn</li>
        </ol>
    </nav>

    <%-- Giới thiệu danh mục chủ đề và liên kết tra cứu FAQ. --%>
    <section class="topics-hero">
        <div class="topics-hero-copy">
            <div class="topics-eyebrow">
                <i class="bi bi-diagram-3-fill"></i>
                QAUTE · KNOWLEDGE DIRECTORY
            </div>
            <h1>Khám phá chủ đề,<br><span>tìm đúng nơi hỗ trợ.</span></h1>
            <p class="topics-hero-description">
                Danh sách chuyên mục và chủ đề tư vấn được phân theo từng đơn vị, phòng ban, giúp bạn dễ dàng xác định đúng lĩnh vực cần tra cứu và tìm câu trả lời phù hợp.
            </p>
            <div class="topics-hero-actions">
                <a href="/faq" class="btn btn-primary">
                    <i class="bi bi-search"></i>
                    Xem tất cả FAQ
                </a>
                <a href="#topics-department-directory" class="btn btn-outline-light">
                    <i class="bi bi-grid-3x3-gap-fill"></i>
                    Khám phá chủ đề
                </a>
            </div>
        </div>
        <div class="topics-hero-art" aria-hidden="true">
            <i class="bi bi-diagram-3-fill"></i>
        </div>
    </section>

    <div class="topics-content-heading" id="topics-department-directory">
        <div>
            <div class="topics-section-kicker">
                <i class="bi bi-buildings-fill"></i>
                DEPARTMENT DIRECTORY
            </div>
            <h2>Chuyên mục theo đơn vị</h2>
            <p>Chọn lĩnh vực phù hợp để xem các câu hỏi thường gặp hoặc gửi yêu cầu tư vấn.</p>
        </div>
        <div class="topics-directory-mark">
            <i class="bi bi-layers-fill"></i>
            Chủ đề tư vấn QAUTE
        </div>
    </div>

    <%-- Ô tìm kiếm chủ đề --%>
    <div class="mb-4">
        <form action="/topics" method="get" class="input-group">
            <input type="search" name="q" class="form-control" placeholder="Tìm kiếm chủ đề, phòng ban..." value="${searchQuery}" aria-label="Tìm kiếm chủ đề">
            <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i></button>
            <c:if test="${isSearch}">
                <a href="/topics" class="btn btn-outline-secondary">Xóa tìm kiếm</a>
            </c:if>
        </form>
    </div>

    <c:choose>
        <c:when test="${empty tree}">
            <div class="topics-empty text-center">
                <div class="topics-empty-icon">
                    <i class="bi bi-folder2-open"></i>
                </div>
                <qa:emptyState icon="bi-folder2-open" title="Chưa có chủ đề" message="Hiện tại chưa có danh mục phòng ban hay chủ đề tư vấn nào được công khai."/>
            </div>
        </c:when>
        <c:otherwise>
            <c:if test="${isSearch}">
                <c:set var="hasResults" value="false"/>
            </c:if>
            <%-- Liệt kê chủ đề theo từng đơn vị và các câu hỏi tương ứng. --%>
            <c:forEach var="dept" items="${tree}">
                <c:set var="deptMatches" value="${false}"/>
                <c:if test="${not empty searchQuery}">
                    <c:if test="${fn:containsIgnoreCase(dept.name, searchQuery) or fn:containsIgnoreCase(dept.description, searchQuery)}">
                        <c:set var="deptMatches" value="true"/>
                    </c:if>
                </c:if>
                <c:if test="${not isSearch or deptMatches}">
                    <c:if test="${deptMatches}">
                        <c:set var="hasResults" value="true"/>
                    </c:if>
                <section class="topics-department">
                    <div class="topics-department-header">
                        <div class="topics-department-heading">
                            <div class="topics-department-identity">
                                <div class="topics-department-icon">
                                    <i class="bi bi-building-fill"></i>
                                </div>
                                <div class="min-width-0">
                                    <h2 class="topics-department-name">
                                        <c:out value="${dept.name}"/>
                                    </h2>
                                    <c:if test="${not empty dept.description}">
                                        <div class="topics-department-description">
                                            <c:out value="${dept.description}"/>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <c:if test="${not empty dept.code}">
                                <span class="topics-department-code">
                                    <c:out value="${dept.code}"/>
                                </span>
                            </c:if>
                        </div>

                        <c:if test="${not empty dept.location or not empty dept.email or not empty dept.phone}">
                            <div class="topics-contact-strip">
                                <c:if test="${not empty dept.location}">
                                    <span class="topics-contact-item">
                                        <i class="bi bi-geo-alt-fill text-danger"></i>
                                        <c:out value="${dept.location}"/>
                                    </span>
                                </c:if>
                                <c:if test="${not empty dept.email}">
                                    <span class="topics-contact-item">
                                        <i class="bi bi-envelope-fill text-primary"></i>
                                        <a href="mailto:<c:out value='${dept.email}'/>">
                                            <c:out value="${dept.email}"/>
                                        </a>
                                    </span>
                                </c:if>
                                <c:if test="${not empty dept.phone}">
                                    <span class="topics-contact-item">
                                        <i class="bi bi-telephone-fill text-success"></i>
                                        <a href="tel:<c:out value='${dept.phone}'/>">
                                            <c:out value="${dept.phone}"/>
                                        </a>
                                    </span>
                                </c:if>
                            </div>
                        </c:if>
                    </div>

                    <div class="topics-department-body">
                        <c:choose>
                            <c:when test="${not empty dept.categories}">
                                <c:set var="hasMatchingCats" value="false"/>
                                <div class="topics-category-grid">
                                    <c:forEach var="cat" items="${dept.categories}">
                                        <c:set var="catMatches" value="false"/>
                                        <c:if test="${not empty searchQuery}">
                                            <c:if test="${fn:containsIgnoreCase(cat.name, searchQuery)}">
                                                <c:set var="catMatches" value="true"/>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${not isSearch or catMatches}">
                                            <c:set var="hasMatchingCats" value="true"/>
                                            <c:if test="${catMatches}">
                                                <c:set var="hasResults" value="true"/>
                                            </c:if>
                                            <c:set var="faqCount" value="${counts[cat.id] != null ? counts[cat.id] : 0}"/>
                                            <div class="topics-category-card ${faqCount == 0 ? 'topics-category-empty' : ''}">
                                                <div class="topics-category-top">
                                                    <div class="topics-category-icon">
                                                        <i class="bi <c:out value='${empty cat.icon ? "bi-folder" : cat.icon}'/>"></i>
                                                    </div>
                                                    <div class="topics-category-copy">
                                                        <c:choose>
                                                            <c:when test="${faqCount > 0}">
                                                                <h3 class="topics-category-name">
                                                                    <a href="/faq?category=<c:out value='${cat.id}'/>">
                                                                        <c:out value="${cat.name}"/>
                                                                    </a>
                                                                </h3>
                                                                <div class="topics-faq-count">
                                                                    <i class="bi bi-chat-square-text-fill"></i>
                                                                    <c:out value="${faqCount}"/> câu hỏi FAQ
                                                                </div>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <h3 class="topics-category-name">
                                                                    <c:out value="${cat.name}"/>
                                                                </h3>
                                                                <div class="topics-faq-count">
                                                                    <i class="bi bi-chat-square-text"></i>
                                                                    0 câu hỏi FAQ
                                                                </div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>

                                                <c:if test="${not empty cat.children}">
                                                    <div class="topics-subcategories">
                                                        <div class="topics-subcategories-heading">
                                                            <i class="bi bi-diagram-2-fill"></i>
                                                            Chủ đề con
                                                        </div>
                                                        <div class="topics-subcategory-list">
                                                            <c:forEach var="sub" items="${cat.children}">
                                                                <c:set var="subCount" value="${counts[sub.id] != null ? counts[sub.id] : 0}"/>
                                                                <c:choose>
                                                                    <c:when test="${subCount > 0}">
                                                                        <a href="/faq?category=<c:out value='${sub.id}'/>"
                                                                           class="topics-subcategory-link"
                                                                           title="<c:out value='${subCount}'/> câu hỏi">
                                                                            <c:out value="${sub.name}"/>
                                                                            <span class="topics-subcategory-count">(<c:out value="${subCount}"/>)</span>
                                                                        </a>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="topics-subcategory-empty">
                                                                            <c:out value="${sub.name}"/>
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </c:if>

                                                <div class="topics-category-footer">
                                                    <c:choose>
                                                        <c:when test="${faqCount > 0}">
                                                            <a href="/faq?category=<c:out value='${cat.id}'/>" class="topics-category-action">
                                                                Xem FAQ
                                                                <i class="bi bi-arrow-right"></i>
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary-subtle text-secondary">
                                                                Chưa có FAQ
                                                            </span>
                                                            <a href="/student/tickets/new?categoryId=<c:out value='${cat.id}'/>" class="topics-category-action topics-action-ticket">
                                                                <i class="bi bi-chat-dots-fill"></i>
                                                                Hỏi tư vấn viên
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${isSearch and not hasMatchingCats}">
                                        <div class="text-secondary small fst-italic py-2">
                                            <i class="bi bi-info-circle me-1"></i>
                                            Không tìm thấy chuyên mục nào phù hợp với từ khóa "${searchQuery}".
                                        </div>
                                    </c:if>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${not isSearch}">
                                    <div class="text-secondary small fst-italic py-2">
                                        <i class="bi bi-info-circle me-1"></i>
                                        Hiện tại chưa có chuyên mục nào thuộc phòng ban này.
                                    </div>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </section>
                </c:if>
            </c:forEach>
            <c:if test="${isSearch and not hasResults}">
                <div class="alert alert-info">
                    <i class="bi bi-info-circle"></i> Không tìm thấy phòng ban hoặc chuyên mục nào phù hợp với từ khóa "${searchQuery}".
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>

    <%-- Hướng dẫn lựa chọn kênh hỗ trợ khi chưa tìm thấy chủ đề phù hợp. --%>
    <section class="topics-help-banner">
        <div class="topics-help-copy">
            <div class="topics-help-icon">
                <i class="bi bi-lightbulb-fill"></i>
            </div>
            <div>
                <h3>Chưa tìm thấy câu trả lời phù hợp?</h3>
                <p>Tra cứu toàn bộ FAQ hoặc chọn chuyên mục để tìm nội dung hỗ trợ liên quan đến vấn đề của bạn.</p>
            </div>
        </div>
        <a href="/faq" class="topics-help-action">
            Khám phá FAQ
            <i class="bi bi-arrow-up-right"></i>
        </a>
    </section>
</div>