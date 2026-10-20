<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>FAQ đã xem</title>

<div class="page-header d-print-none mb-3">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
        <div>
            <h2 class="page-title mb-1">FAQ đã xem</h2>
            <div class="text-secondary">Các bài viết bạn đã xem gần đây.</div>
        </div>
        <a class="btn btn-outline-primary" href="${ctx}/student/faq"><i class="bi bi-question-circle me-1"></i>Khám phá FAQ</a>
    </div>
</div>

<c:choose>
    <c:when test="${not empty page.content}">
        <div class="row row-cards">
            <c:forEach var="faq" items="${page.content}">
                <div class="col-12 col-md-6">
                    <article class="card h-100 qa-card-hover">
                        <div class="card-body d-flex flex-column">
                            <div class="d-flex align-items-start gap-2">
                                <h3 class="card-title flex-fill mb-2"><a class="text-reset text-decoration-none" href="${ctx}/faq/<c:out value='${faq.slug()}'/>"><c:out value="${faq.title()}"/></a></h3>
                                <button class="btn btn-sm ${bookmarkedIds.contains(faq.id()) ? 'btn-danger' : 'btn-outline-secondary'}" type="button"
                                        data-faq-bookmark data-faq-id="${faq.id()}" data-bookmarked="${bookmarkedIds.contains(faq.id())}"
                                        aria-pressed="${bookmarkedIds.contains(faq.id())}" title="${bookmarkedIds.contains(faq.id()) ? 'Đã lưu' : 'Lưu bài viết'}">
                                    <i class="bi ${bookmarkedIds.contains(faq.id()) ? 'bi-heart-fill' : 'bi-heart'}"></i>
                                </button>
                            </div>
                            <c:if test="${not empty faq.summary()}"><p class="text-secondary mb-3"><c:out value="${fn:length(faq.summary()) > 180 ? fn:substring(faq.summary(), 0, 180).concat('...') : faq.summary()}"/></p></c:if>
                            <div class="mt-auto pt-2 border-top d-flex flex-wrap align-items-center gap-2 small">
                                <span class="badge bg-blue-lt"><i class="bi bi-folder me-1"></i><c:out value="${faq.categoryName()}"/></span>
                                <span class="ms-auto text-secondary"><i class="bi bi-clock-history me-1"></i>Đã xem: <c:out value="${viewedAt[faq.id()]}"/></span>
                            </div>
                        </div>
                    </article>
                </div>
            </c:forEach>
        </div>
        <div class="d-flex justify-content-center mt-4"><qa:pagination page="${page}" baseUrl="${ctx}/student/faq/recent"/></div>
    </c:when>
    <c:otherwise>
        <div class="card"><div class="card-body">
            <qa:emptyState icon="bi-clock-history" title="Chưa có FAQ đã xem"
                           message="Các bài viết bạn mở sẽ xuất hiện tại đây."
                           actionUrl="${ctx}/student/faq" actionLabel="Khám phá FAQ"/>
        </div></div>
    </c:otherwise>
</c:choose>

<script src="${ctx}/assets/js/student/faq-bookmark.js"></script>
