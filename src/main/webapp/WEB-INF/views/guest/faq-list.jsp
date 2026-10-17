<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Hỏi đáp FAQ</title>

<div class="row">
    <div class="col-lg-3 mb-4">
        <div class="accordion" id="faqFilterAccordion">
            <div class="accordion-item border-0">
                <div class="d-lg-none mb-3">
                    <button class="accordion-button collapsed card p-3 shadow-none border" type="button"
                            data-bs-toggle="collapse" data-bs-target="#collapseFilter"
                            aria-expanded="false" aria-controls="collapseFilter">
                        <i class="bi bi-funnel me-2"></i>Bộ lọc tìm kiếm
                        <c:if test="${not empty category or not empty tag}">
                            <span class="badge bg-primary ms-auto me-2">Đang lọc</span>
                        </c:if>
                    </button>
                </div>
                <div id="collapseFilter" class="accordion-collapse collapse d-lg-block" data-bs-parent="#faqFilterAccordion">
                    <c:if test="${not empty category or not empty tag or (not empty sort and sort != 'LATEST')}">
                        <a href="/faq" class="btn btn-outline-danger btn-sm w-100 mb-3">
                            <i class="bi bi-x-circle me-1"></i>Xóa bộ lọc
                        </a>
                    </c:if>

                    <div class="card mb-3">
                        <div class="card-header py-2">
                            <h3 class="card-title fs-5"><i class="bi bi-grid me-2 text-primary"></i>Chuyên mục</h3>
                        </div>
                        <div class="list-group list-group-flush">
                            <a href="<c:url value='/faq'><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                               class="list-group-item list-group-item-action ${empty category ? 'active fw-bold' : ''}">
                                Tất cả chuyên mục
                            </a>
                            <c:forEach var="dept" items="${tree}">
                                <div class="list-group-item bg-light text-uppercase fw-bold small text-secondary py-1">
                                    <c:out value="${dept.name()}"/>
                                </div>
                                <c:forEach var="cat" items="${dept.categories()}">
                                    <a href="<c:url value='/faq'><c:param name='category' value='${cat.id()}'/><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                                       class="list-group-item list-group-item-action ps-4 ${category == cat.id() ? 'active fw-bold' : ''}">
                                        <i class="bi <c:out value='${empty cat.icon() ? "bi-folder" : cat.icon()}'/> me-1"></i>
                                        <c:out value="${cat.name()}"/>
                                    </a>
                                </c:forEach>
                            </c:forEach>
                        </div>
                    </div>

                    <c:if test="${not empty tags}">
                        <div class="card mb-3">
                            <div class="card-header py-2">
                                <h3 class="card-title fs-5"><i class="bi bi-tags me-2 text-primary"></i>Chủ đề phổ biến</h3>
                            </div>
                            <div class="card-body">
                                <div class="d-flex flex-wrap gap-2">
                                    <c:forEach var="t" items="${tags}">
                                        <c:choose>
                                            <c:when test="${tag == t.slug()}">
                                                <a href="<c:url value='/faq'><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                                                   class="badge bg-primary text-white text-decoration-none py-2 px-2">
                                                    <c:out value="${t.name()}"/> <i class="bi bi-x ms-1"></i>
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="<c:url value='/faq'><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:param name='tag' value='${t.slug()}'/><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                                                   class="badge bg-blue-lt text-decoration-none py-2 px-2">
                                                    <c:out value="${t.name()}"/> <span class="text-secondary ms-1">(<c:out value="${t.faqCount()}"/>)</span>
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

    <div class="col-lg-9">
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 gap-2">
            <div>
                <h1 class="h2 mb-1">
                    <c:out value="${empty selectedCategoryName ? 'Hỏi đáp FAQ' : selectedCategoryName}"/>
                </h1>
                <div class="text-secondary small">
                    Tìm thấy <c:out value="${page.totalElements}"/> câu hỏi
                    <c:if test="${not empty tag}">
                        kèm thẻ: <span class="badge bg-primary"><c:out value="${tag}"/></span>
                    </c:if>
                </div>
            </div>

            <div class="btn-group" role="group" aria-label="Sắp xếp câu hỏi">
                <a href="<c:url value='/faq'><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='LATEST'/></c:url>"
                   class="btn btn-sm ${empty sort or sort == 'LATEST' ? 'btn-primary' : 'btn-outline-secondary'}">
                    Mới cập nhật
                </a>
                <a href="<c:url value='/faq'><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='MOST_VIEWED'/></c:url>"
                   class="btn btn-sm ${sort == 'MOST_VIEWED' ? 'btn-primary' : 'btn-outline-secondary'}">
                    Xem nhiều
                </a>
                <a href="<c:url value='/faq'><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='MOST_HELPFUL'/></c:url>"
                   class="btn btn-sm ${sort == 'MOST_HELPFUL' ? 'btn-primary' : 'btn-outline-secondary'}">
                    Hữu ích nhất
                </a>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty page.content}">
                <div class="row row-cards mb-4">
                    <c:forEach var="faq" items="${page.content}">
                        <div class="col-12">
                            <div class="card qa-card-hover">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-start mb-2">
                                        <h3 class="card-title mb-0 fs-3">
                                            <a href="/faq/<c:out value='${faq.slug()}'/>" class="text-reset text-decoration-none">
                                                <c:out value="${faq.title()}"/>
                                            </a>
                                        </h3>
                                    </div>

                                    <c:if test="${not empty faq.summary()}">
                                        <c:set var="sum" value="${faq.summary()}"/>
                                        <p class="text-secondary mb-3">
                                            <c:out value="${fn:length(sum) > 160 ? fn:substring(sum, 0, 160).concat('...') : sum}"/>
                                        </p>
                                    </c:if>

                                    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 pt-2 border-top">
                                        <div class="d-flex flex-wrap align-items-center gap-1">
                                            <span class="badge bg-blue-lt me-2">
                                                <i class="bi bi-folder me-1"></i><c:out value="${faq.categoryName()}"/>
                                            </span>
                                            <c:forEach var="tName" items="${faq.tags()}">
                                                <span class="badge bg-secondary-lt">#<c:out value="${tName}"/></span>
                                            </c:forEach>
                                        </div>

                                        <div class="text-secondary small d-flex align-items-center gap-3">
                                            <span><i class="bi bi-eye me-1"></i><c:out value="${faq.viewCount()}"/></span>
                                            <span><i class="bi bi-hand-thumbs-up me-1"></i><c:out value="${faq.helpfulCount()}"/></span>
                                            <c:if test="${not empty faq.updatedAt()}">
                                                <span><i class="bi bi-calendar3 me-1"></i>${faq.updatedAt().dayOfMonth}/${faq.updatedAt().monthValue}/${faq.updatedAt().year}</span>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="d-flex justify-content-center">
                    <qa:pagination page="${page}" baseUrl="/faq" params="${queryString}"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card p-4">
                    <qa:emptyState icon="bi-search"
                                   title="Không tìm thấy câu hỏi phù hợp"
                                   message="Hãy thử bỏ bớt bộ lọc hoặc gửi câu hỏi mới đến ban tư vấn của chúng tôi"
                                   actionUrl="/student/tickets/new"
                                   actionLabel="Tạo yêu cầu tư vấn"/>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

