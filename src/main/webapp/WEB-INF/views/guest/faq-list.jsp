<%@ page pageEncoding="UTF-8" %>
    <%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Hỏi đáp FAQ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/guest/faq-list.css">
</head>



        <div class="qaute-faq-pro">
            <nav aria-label="breadcrumb" class="faq-breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Hỏi &amp; Đáp</li>
                </ol>
            </nav>
            <%-- Giới thiệu kho FAQ và cách tìm câu trả lời phù hợp. --%>
            <section class="faq-hero" aria-label="Giới thiệu kho hỏi đáp">
                <div class="faq-hero-copy">
                    <div class="faq-eyebrow"><i class="bi bi-stars"></i> QAUTE KNOWLEDGE HUB</div>
                    <h2>Khám phá tri thức.<br><span>Giải đáp điều bạn cần.</span></h2>
                    <p>Không gian hỏi đáp dành cho sinh viên HCMUTE — tìm thông tin theo chuyên mục, khám phá chủ đề nổi
                        bật và chọn câu trả lời hữu ích nhất.</p>
                </div>
                <div class="faq-hero-art" aria-hidden="true">
                    <i class="bi bi-journal-richtext"></i>
                    <span class="faq-hero-bubble"><i class="bi bi-lightbulb-fill"></i></span>
                    <span class="faq-hero-bubble second"><i class="bi bi-stars"></i></span>
                </div>
            </section>
            <%-- Bố trí bộ lọc chuyên mục/chủ đề cạnh danh sách câu hỏi. --%>
            <div class="row g-4 faq-layout">
                <div class="col-lg-3 mb-4 faq-sidebar">
                    <div class="accordion" id="faqFilterAccordion">
                        <div class="accordion-item border-0 bg-transparent">
                            <div class="d-lg-none mb-3">
                                <button class="accordion-button collapsed p-3" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#collapseFilter" aria-expanded="false"
                                    aria-controls="collapseFilter">
                                    <i class="bi bi-sliders2-vertical me-2"></i>Bộ lọc tìm kiếm
                                    <c:if test="${not empty category or not empty tag}">
                                        <span class="badge bg-primary ms-auto me-2">Đang lọc</span>
                                    </c:if>
                                </button>
                            </div>
                            <div id="collapseFilter" class="accordion-collapse collapse d-lg-block"
                                data-bs-parent="#faqFilterAccordion">
                                <div class="faq-filter-sticky">
                                    <c:if
                                        test="${not empty category or not empty tag or (not empty sort and sort != 'LATEST')}">
                                        <a href="/faq" class="btn btn-outline-danger btn-sm w-100 mb-3 faq-clear"><i
                                                class="bi bi-arrow-counterclockwise"></i>Xóa bộ lọc</a>
                                    </c:if>
                                    <div class="card faq-panel mb-3">
                                        <div class="card-header">
                                            <h3 class="card-title"><i class="bi bi-grid-1x2-fill"></i>Chuyên mục</h3>
                                        </div>
                                        <div class="list-group list-group-flush">
                                            <a href="<c:url value='/faq'><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                                                class="list-group-item list-group-item-action ${empty category ? 'active fw-bold' : ''}">
                                                <i class="bi bi-collection me-2"></i>Tất cả chuyên mục
                                            </a>
                                            <c:forEach var="dept" items="${tree}">
                                                <div class="list-group-item faq-dept text-uppercase fw-bold small">
                                                    <c:out value="${dept.name()}" />
                                                </div>
                                                <c:forEach var="cat" items="${dept.categories()}">
                                                    <a href="<c:url value='/faq'><c:param name='category' value='${cat.id()}'/><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                                                        class="list-group-item list-group-item-action ps-4 ${category == cat.id() ? 'active fw-bold' : ''}">
                                                        <i class="bi <c:out value='${empty cat.icon() ? "
                                                            bi-folder2-open" : cat.icon()}' /> me-2"></i>
                                                        <c:out value="${cat.name()}" />
                                                    </a>
                                                </c:forEach>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <c:if test="${not empty tags}">
                                        <div class="card faq-panel mb-3">
                                            <div class="card-header">
                                                <h3 class="card-title"><i class="bi bi-tags-fill"></i>Chủ đề phổ biến
                                                </h3>
                                            </div>
                                            <div class="card-body faq-tag-body">
                                                <div class="d-flex flex-wrap gap-2">
                                                    <c:forEach var="t" items="${tags}">
                                                        <c:choose>
                                                            <c:when test="${tag == t.slug()}">
                                                                <a href="<c:url value='/faq'><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                                                                    class="badge bg-primary text-white text-decoration-none">
                                                                    <c:out value="${t.name()}" /><i
                                                                        class="bi bi-x-lg ms-1"></i>
                                                                </a>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <a href="<c:url value='/faq'><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:param name='tag' value='${t.slug()}'/><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                                                                    class="badge bg-blue-lt text-decoration-none">
                                                                    <c:out value="${t.name()}" /><span
                                                                        class="text-secondary ms-1">(
                                                                        <c:out value="${t.faqCount()}" />)
                                                                    </span>
                                                                </a>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-9">
                    <div class="faq-content-head">
                        <div class="faq-content-title">
                            <div class="faq-content-kicker"><i class="bi bi-compass-fill"></i> Trung tâm tri thức</div>
                            <h1>
                                <c:out value="${empty selectedCategoryName ? 'Hỏi đáp FAQ' : selectedCategoryName}" />
                            </h1>
                            <div class="faq-result-line">
                                <span class="faq-result-number"><i class="bi bi-collection-fill"></i>
                                    <c:out value="${page.totalElements}" />
                                </span>
                                <span>câu hỏi phù hợp</span>
                                <c:if test="${not empty tag}">
                                    <span>·</span><span>Thẻ:</span><span class="faq-result-tag">
                                        <c:out value="${tag}" />
                                    </span>
                                </c:if>
                            </div>
                        </div>
                        <%-- Cho phép sắp xếp câu hỏi theo ngày, lượt xem hoặc mức hữu ích. --%>
                        <div class="btn-group faq-sort" role="group" aria-label="Sắp xếp câu hỏi">
                            <a href="<c:url value='/faq'><c:if test='${not empty searchQuery}'><c:param name='q' value='${searchQuery}'/></c:if><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='LATEST'/></c:url>"
                                class="btn btn-sm ${empty sort or sort == 'LATEST' ? 'btn-primary' : 'btn-outline-secondary'}"><i
                                    class="bi bi-clock-history me-1"></i>Mới cập nhật</a>
                            <a href="<c:url value='/faq'><c:if test='${not empty searchQuery}'><c:param name='q' value='${searchQuery}'/></c:if><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='MOST_VIEWED'/></c:url>"
                                class="btn btn-sm ${sort == 'MOST_VIEWED' ? 'btn-primary' : 'btn-outline-secondary'}"><i
                                    class="bi bi-fire me-1"></i>Xem nhiều</a>
                            <a href="<c:url value='/faq'><c:if test='${not empty searchQuery}'><c:param name='q' value='${searchQuery}'/></c:if><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='MOST_HELPFUL'/></c:url>"
                                class="btn btn-sm ${sort == 'MOST_HELPFUL' ? 'btn-primary' : 'btn-outline-secondary'}"><i
                                    class="bi bi-hand-thumbs-up-fill me-1"></i>Hữu ích nhất</a>
                        </div>
                    </div>

                    <%-- Ô tìm kiếm FAQ --%>
                    <div class="mb-4">
                        <form action="/faq" method="get" class="input-group">
                            <input type="search" name="q" class="form-control" placeholder="Tìm kiếm câu hỏi..." value="${searchQuery}" aria-label="Tìm kiếm câu hỏi">
                            <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i></button>
                            <c:if test="${isSearch}">
                                <a href="/faq" class="btn btn-outline-secondary">Xóa tìm kiếm</a>
                            </c:if>
                        </form>
                    </div>

                    <c:if test="${isSearch && empty page.content}">
                        <div class="alert alert-info">
                            <i class="bi bi-info-circle"></i> Không tìm thấy câu hỏi nào phù hợp với từ khóa "${searchQuery}".
                        </div>
                    </c:if>

                    <c:choose>
                        <c:when test="${not empty page.content}">
                            <%-- Liệt kê kết quả FAQ cùng thông tin danh mục và lượt xem. --%>
                            <div class="faq-list mb-4">
                                <c:forEach var="faq" items="${page.content}">
                                    <div class="card faq-item-card">
                                        <div class="card-body">
                                            <div class="faq-item-title-row">
                                                <span class="faq-item-mark" aria-hidden="true"><i
                                                        class="bi bi-chat-square-text-fill"></i></span>
                                                <h3 class="card-title"><a href="/faq/<c:out value='${faq.slug()}'/>"
                                                        class="faq-title-link">
                                                        <c:out value="${faq.title()}" />
                                                    </a></h3>
                                            </div>
                                            <c:if test="${not empty faq.summary()}">
                                                <c:set var="sum" value="${faq.summary()}" />
                                                <p class="faq-summary">
                                                    <c:out
                                                        value="${fn:length(sum) > 160 ? fn:substring(sum, 0, 160).concat('...') : sum}" />
                                                </p>
                                            </c:if>
                                            <div class="faq-item-meta">
                                                <div class="faq-item-tags">
                                                    <span class="badge faq-category"><i class="bi bi-folder2-open"></i>
                                                        <c:out value="${faq.categoryName()}" />
                                                    </span>
                                                    <c:forEach var="tName" items="${faq.tags()}">
                                                        <span class="badge">#
                                                            <c:out value="${tName}" />
                                                        </span>
                                                    </c:forEach>
                                                </div>
                                                <div class="faq-item-stats">
                                                    <span><i class="bi bi-eye-fill"></i>
                                                        <c:out value="${faq.viewCount()}" />
                                                    </span>
                                                    <span><i class="bi bi-hand-thumbs-up-fill"></i>
                                                        <c:out value="${faq.helpfulCount()}" />
                                                    </span>
                                                    <c:if test="${not empty faq.updatedAt()}">
                                                        <span><i
                                                                class="bi bi-calendar3"></i>${faq.updatedAt().dayOfMonth}/${faq.updatedAt().monthValue}/${faq.updatedAt().year}</span>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="d-flex justify-content-center faq-pagination qa-guest-pagination">
                                <qa:pagination page="${page}" baseUrl="/faq" params="${queryString}" />
                            </div>
                        </c:when>
                        <%-- Hiển thị trạng thái rỗng và lối tạo yêu cầu khi không có kết quả. --%>
                        <c:otherwise>
                            <div class="card faq-empty-wrap">
                                <qa:emptyState icon="bi-search" title="Không tìm thấy câu hỏi phù hợp"
                                    message="Hãy thử bỏ bớt bộ lọc hoặc gửi câu hỏi mới đến ban tư vấn của chúng tôi"
                                    actionUrl="/student/tickets/new" actionLabel="Tạo yêu cầu tư vấn" />
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>