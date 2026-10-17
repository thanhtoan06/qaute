<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title><c:out value="${faq.title()}"/></title>

<nav aria-label="breadcrumb" class="mb-3">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
        <li class="breadcrumb-item"><a href="/faq">Hỏi đáp</a></li>
        <c:if test="${not empty faq.categoryPath()}">
            <li class="breadcrumb-item active" aria-current="page">
                <c:out value="${faq.categoryPath()}"/>
            </li>
        </c:if>
    </ol>
</nav>

<div class="row">
    <div class="col-lg-8">
        <article class="card p-4 mb-4">
            <h1 class="h1 mb-3"><c:out value="${faq.title()}"/></h1>

            <div class="d-flex flex-wrap align-items-center gap-3 text-secondary small pb-3 mb-3 border-bottom">
                <c:if test="${not empty faq.categoryPath()}">
                    <span>
                        <i class="bi bi-folder me-1"></i>
                        <a href="/faq?category=<c:out value='${faq.categoryId()}'/>" class="text-reset">
                            <c:out value="${faq.categoryPath()}"/>
                        </a>
                    </span>
                </c:if>
                <c:if test="${not empty faq.updatedAt()}">
                    <span>
                        <i class="bi bi-clock-history me-1"></i>
                        Cập nhật: ${faq.updatedAt().dayOfMonth}/${faq.updatedAt().monthValue}/${faq.updatedAt().year}
                    </span>
                </c:if>
                <span>
                    <i class="bi bi-eye me-1"></i>
                    <c:out value="${faq.viewCount()}"/> lượt xem
                </span>
                <span>
                    <i class="bi bi-hand-thumbs-up me-1"></i>
                    <c:out value="${faq.helpfulCount()}"/> hữu ích
                </span>
            </div>

            <c:if test="${not empty faq.summary()}">
                <div class="lead text-secondary mb-4 p-3 bg-light rounded">
                    <c:out value="${faq.summary()}"/>
                </div>
            </c:if>

            <div class="qa-faq-content lh-lg mb-4">
                <c:out value="${faq.bodyHtml()}" escapeXml="false"/>
            </div>

            <c:if test="${not empty faq.tags()}">
                <div class="d-flex flex-wrap align-items-center gap-1 pt-3 border-top">
                    <span class="text-secondary small me-2"><i class="bi bi-tags me-1"></i>Thẻ:</span>
                    <c:forEach var="t" items="${faq.tags()}">
                        <a href="/faq?tag=<c:out value='${t.slug()}'/>" class="badge bg-blue-lt text-decoration-none">
                            #<c:out value="${t.name()}"/>
                        </a>
                    </c:forEach>
                </div>
            </c:if>

            <div class="card p-3 my-4 bg-light text-center border-0">
                <p class="fw-bold mb-2">Bài viết có hữu ích với bạn không?</p>
                <sec:authorize access="isAnonymous()">
                    <p class="text-secondary small mb-2">Đăng nhập để đánh giá mức độ hữu ích của câu trả lời</p>
                    <div>
                        <a href="/auth/login?returnUrl=/faq/<c:out value='${faq.slug()}'/>" class="btn btn-outline-primary btn-sm">
                            <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập để đánh giá
                        </a>
                    </div>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <div class="btn-list justify-content-center">
                        <button type="button" class="btn btn-sm btn-outline-success" disabled>
                            <i class="bi bi-hand-thumbs-up me-1"></i>Hữu ích (<c:out value="${faq.helpfulCount()}"/>)
                        </button>
                        <button type="button" class="btn btn-sm btn-outline-secondary" disabled>
                            <i class="bi bi-hand-thumbs-down me-1"></i>Chưa hữu ích (<c:out value="${faq.notHelpfulCount()}"/>)
                        </button>
                    </div>
                </sec:authorize>
            </div>
        </article>

        <div class="card p-4 mb-4 bg-blue-lt border-0">
            <h3 class="card-title mb-2"><i class="bi bi-question-circle text-primary me-2"></i>Vẫn cần hỗ trợ?</h3>
            <p class="text-secondary mb-3">Nếu câu hỏi của bạn chưa được giải đáp đầy đủ, hãy liên hệ trực tiếp với các tư vấn viên.</p>
            <div class="d-flex flex-wrap gap-2">
                <a href="/student/tickets/new?categoryId=<c:out value='${faq.categoryId()}'/>" class="btn btn-primary btn-sm">
                    <i class="bi bi-plus-circle me-1"></i>Tạo yêu cầu tư vấn
                </a>
                <a href="/student/chat" class="btn btn-teal text-white btn-sm">
                    <i class="bi bi-chat-dots me-1"></i>Chat với tư vấn viên
                </a>
                <a href="/student/appointments/book" class="btn btn-outline-primary btn-sm">
                    <i class="bi bi-calendar-event me-1"></i>Đặt lịch hẹn
                </a>
            </div>
        </div>
    </div>

    <div class="col-lg-4">
        <div class="card mb-4">
            <div class="card-header py-3">
                <h3 class="card-title fs-4"><i class="bi bi-link-45deg me-2 text-primary"></i>Bài viết liên quan</h3>
            </div>
            <c:choose>
                <c:when test="${not empty related}">
                    <div class="list-group list-group-flush">
                        <c:forEach var="rel" items="${related}">
                            <a href="/faq/<c:out value='${rel.slug()}'/>" class="list-group-item list-group-item-action p-3">
                                <div class="fw-bold mb-1"><c:out value="${rel.title()}"/></div>
                                <div class="text-secondary small d-flex justify-content-between align-items-center">
                                    <span><c:out value="${rel.categoryName()}"/></span>
                                    <span>
                                        <i class="bi bi-eye me-1"></i><c:out value="${rel.viewCount()}"/>
                                        <i class="bi bi-hand-thumbs-up ms-2 me-1"></i><c:out value="${rel.helpfulCount()}"/>
                                    </span>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="card-body text-center text-secondary py-4 small">
                        Không có bài viết liên quan nào.
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

