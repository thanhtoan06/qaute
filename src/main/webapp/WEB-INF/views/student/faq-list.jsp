<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Hỏi đáp FAQ</title>

<div class="page-header d-print-none mb-3">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
        <div>
            <h2 class="page-title mb-1">Hỏi đáp FAQ</h2>
            <div class="text-secondary">Khám phá câu trả lời cho các vấn đề thường gặp.</div>
        </div>
        <span class="text-secondary small">${page.totalElements} bài viết</span>
    </div>
</div>

<section class="card mb-3">
    <div class="card-header p-0">
        <ul class="nav nav-tabs card-header-tabs flex-nowrap overflow-auto" aria-label="Lọc FAQ">
            <li class="nav-item"><a class="nav-link ${tab eq 'latest' ? 'active' : ''}" href="<c:url value='/student/faq'><c:param name='tab' value='latest'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty q}'><c:param name='q' value='${q}'/></c:if></c:url>">Mới nhất</a></li>
            <li class="nav-item"><a class="nav-link ${tab eq 'views' ? 'active' : ''}" href="<c:url value='/student/faq'><c:param name='tab' value='views'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty q}'><c:param name='q' value='${q}'/></c:if></c:url>">Xem nhiều</a></li>
            <li class="nav-item"><a class="nav-link ${tab eq 'helpful' ? 'active' : ''}" href="<c:url value='/student/faq'><c:param name='tab' value='helpful'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty q}'><c:param name='q' value='${q}'/></c:if></c:url>">Hữu ích nhất</a></li>
            <li class="nav-item"><a class="nav-link ${tab eq 'saved' ? 'active' : ''}" href="<c:url value='/student/faq'><c:param name='tab' value='saved'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty q}'><c:param name='q' value='${q}'/></c:if></c:url>">Đã lưu</a></li>
        </ul>
    </div>
    <div class="card-body">
        <form method="get" action="${ctx}/student/faq" class="row g-2 align-items-end">
            <input type="hidden" name="tab" value="${tab}"/>
            <div class="col-12 col-md-4">
                <label class="form-label" for="faq-category">Chuyên mục</label>
                <select id="faq-category" class="form-select" name="category">
                    <option value="">Tất cả chuyên mục</option>
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
                <label class="form-label" for="faq-query">Tìm kiếm</label>
                <input id="faq-query" class="form-control" name="q" maxlength="100" value="<c:out value='${q}'/>" placeholder="Nhập từ khóa cần tìm..."/>
            </div>
            <div class="col-12 col-md-2 d-flex gap-2">
                <button class="btn btn-primary flex-fill" type="submit"><i class="bi bi-search"></i><span class="d-md-none ms-1">Tìm</span></button>
                <a class="btn btn-outline-secondary" href="${ctx}/student/faq" title="Xóa bộ lọc" aria-label="Xóa bộ lọc"><i class="bi bi-x-lg"></i></a>
            </div>
        </form>
    </div>
</section>

<c:choose>
    <c:when test="${not empty page.content}">
        <div class="row row-cards">
            <c:forEach var="faq" items="${page.content}">
                <div class="col-12 col-md-6">
                    <article class="card h-100 qa-card-hover">
                        <div class="card-body d-flex flex-column">
                            <div class="d-flex align-items-start gap-2">
                                <h3 class="card-title flex-fill mb-2">
                                    <a class="text-reset text-decoration-none" href="${ctx}/faq/<c:out value='${faq.slug()}'/>"><c:out value="${faq.title()}"/></a>
                                </h3>
                                <button class="btn btn-sm ${bookmarkedIds.contains(faq.id()) ? 'btn-danger' : 'btn-outline-secondary'}" type="button"
                                        data-faq-bookmark data-faq-id="${faq.id()}" data-bookmarked="${bookmarkedIds.contains(faq.id())}" aria-pressed="${bookmarkedIds.contains(faq.id())}"
                                        title="${bookmarkedIds.contains(faq.id()) ? 'Đã lưu' : 'Lưu bài viết'}">
                                    <i class="bi ${bookmarkedIds.contains(faq.id()) ? 'bi-heart-fill' : 'bi-heart'}"></i>
                                </button>
                            </div>
                            <c:if test="${not empty faq.summary()}">
                                <p class="text-secondary mb-3"><c:out value="${fn:length(faq.summary()) > 180 ? fn:substring(faq.summary(), 0, 180).concat('...') : faq.summary()}"/></p>
                            </c:if>
                            <div class="mt-auto pt-2 border-top d-flex flex-wrap align-items-center gap-2 small">
                                <span class="badge bg-blue-lt"><i class="bi bi-folder me-1"></i><c:out value="${faq.categoryName()}"/></span>
                                <c:forEach var="tag" items="${faq.tags()}"><span class="badge bg-secondary-lt">#<c:out value="${tag}"/></span></c:forEach>
                                <span class="ms-auto text-secondary"><i class="bi bi-eye me-1"></i><c:out value="${faq.viewCount()}"/></span>
                                <span class="text-secondary"><i class="bi bi-hand-thumbs-up me-1"></i><c:out value="${faq.helpfulCount()}"/></span>
                                <c:if test="${viewedIds.contains(faq.id())}"><span class="badge bg-azure-lt">Đã xem</span></c:if>
                            </div>
                        </div>
                    </article>
                </div>
            </c:forEach>
        </div>
        <div class="d-flex justify-content-center mt-4">
            <qa:pagination page="${page}" baseUrl="${ctx}/student/faq" params="${queryString}"/>
        </div>
    </c:when>
    <c:otherwise>
        <div class="card"><div class="card-body">
            <qa:emptyState icon="bi-search" title="Chưa tìm thấy bài viết phù hợp"
                           message="Hãy thử đổi từ khóa, chuyên mục hoặc xem các bài FAQ mới nhất."
                           actionUrl="${ctx}/student/faq" actionLabel="Xem tất cả FAQ"/>
        </div></div>
    </c:otherwise>
</c:choose>

<script src="${ctx}/assets/js/student/faq-bookmark.js"></script>
