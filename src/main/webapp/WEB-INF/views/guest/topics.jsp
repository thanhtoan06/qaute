<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chủ đề tư vấn</title>

<nav aria-label="breadcrumb" class="mb-4">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
        <li class="breadcrumb-item active" aria-current="page">Chủ đề tư vấn</li>
    </ol>
</nav>

<div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-2 border-bottom">
    <div>
        <h1 class="h2 mb-1">Chủ đề tư vấn</h1>
        <p class="text-secondary mb-0">Danh sách chuyên mục và chủ đề tư vấn được phân theo từng đơn vị, phòng ban</p>
    </div>
    <div class="mt-2 mt-md-0">
        <a href="/faq" class="btn btn-outline-primary btn-sm">
            <i class="bi bi-question-circle me-1"></i>Xem tất cả FAQ
        </a>
    </div>
</div>

<c:choose>
    <c:when test="${empty tree}">
        <qa:emptyState icon="bi-folder2-open" title="Chưa có chủ đề" message="Hiện tại chưa có danh mục phòng ban hay chủ đề tư vấn nào được công khai."/>
    </c:when>
    <c:otherwise>
        <c:forEach var="dept" items="${tree}">
            <div class="card mb-4 shadow-sm">
                <div class="card-header bg-light-subtle py-3">
                    <div class="d-flex flex-wrap align-items-center justify-content-between gap-2">
                        <div>
                            <h2 class="card-title h3 mb-1 text-primary">
                                <i class="bi bi-building me-2"></i><c:out value="${dept.name()}"/>
                            </h2>
                            <c:if test="${not empty dept.description()}">
                                <div class="text-secondary small mt-1">
                                    <c:out value="${dept.description()}"/>
                                </div>
                            </c:if>
                        </div>
                        <c:if test="${not empty dept.code()}">
                            <span class="badge bg-secondary-subtle text-secondary border">
                                <c:out value="${dept.code()}"/>
                            </span>
                        </c:if>
                    </div>

                    <c:if test="${not empty dept.location() or not empty dept.email() or not empty dept.phone()}">
                        <div class="d-flex flex-wrap gap-3 small text-secondary mt-3 pt-2 border-top">
                            <c:if test="${not empty dept.location()}">
                                <span>
                                    <i class="bi bi-geo-alt-fill text-danger me-1"></i><c:out value="${dept.location()}"/>
                                </span>
                            </c:if>
                            <c:if test="${not empty dept.email()}">
                                <span>
                                    <i class="bi bi-envelope-fill text-primary me-1"></i><a href="mailto:<c:out value='${dept.email()}'/>" class="text-reset"><c:out value="${dept.email()}"/></a>
                                </span>
                            </c:if>
                            <c:if test="${not empty dept.phone()}">
                                <span>
                                    <i class="bi bi-telephone-fill text-success me-1"></i><a href="tel:<c:out value='${dept.phone()}'/>" class="text-reset"><c:out value="${dept.phone()}"/></a>
                                </span>
                            </c:if>
                        </div>
                    </c:if>
                </div>

                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty dept.categories()}">
                            <div class="row g-3">
                                <c:forEach var="cat" items="${dept.categories()}">
                                    <c:set var="faqCount" value="${counts[cat.id()] != null ? counts[cat.id()] : 0}"/>
                                    <div class="col-md-6 col-lg-4">
                                        <div class="card h-100 p-3 ${faqCount == 0 ? 'bg-light-subtle opacity-75 border-dashed' : 'border'}">
                                            <div class="d-flex align-items-start mb-2">
                                                <div class="me-2 text-primary fs-3">
                                                    <i class="bi <c:out value='${empty cat.icon() ? "bi-folder" : cat.icon()}'/>"></i>
                                                </div>
                                                <div class="flex-grow-1 min-width-0">
                                                    <c:choose>
                                                        <c:when test="${faqCount > 0}">
                                                            <h3 class="h5 mb-1 text-truncate">
                                                                <a href="/faq?category=<c:out value='${cat.id()}'/>" class="text-reset text-decoration-none fw-semibold">
                                                                    <c:out value="${cat.name()}"/>
                                                                </a>
                                                            </h3>
                                                            <div class="small text-secondary">
                                                                <i class="bi bi-question-circle me-1"></i><c:out value="${faqCount}"/> câu hỏi FAQ
                                                            </div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <h3 class="h5 mb-1 text-secondary text-truncate">
                                                                <c:out value="${cat.name()}"/>
                                                            </h3>
                                                            <div class="small text-secondary">
                                                                <i class="bi bi-question-circle me-1"></i>0 câu hỏi FAQ
                                                            </div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>

                                            <c:if test="${not empty cat.children()}">
                                                <div class="my-2 pt-2 border-top">
                                                    <div class="small text-secondary mb-1 fw-semibold">Chủ đề con:</div>
                                                    <div class="d-flex flex-wrap gap-1">
                                                        <c:forEach var="sub" items="${cat.children()}">
                                                            <c:set var="subCount" value="${counts[sub.id()] != null ? counts[sub.id()] : 0}"/>
                                                            <c:choose>
                                                                <c:when test="${subCount > 0}">
                                                                    <a href="/faq?category=<c:out value='${sub.id()}'/>" class="badge bg-white text-dark border text-decoration-none" title="<c:out value='${subCount}'/> câu hỏi">
                                                                        <c:out value="${sub.name()}"/> <span class="text-primary">(<c:out value="${subCount}"/>)</span>
                                                                    </a>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="badge bg-light text-secondary border opacity-75">
                                                                        <c:out value="${sub.name()}"/>
                                                                    </span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </c:if>

                                            <div class="mt-auto pt-3 d-flex justify-content-between align-items-center">
                                                <c:choose>
                                                    <c:when test="${faqCount > 0}">
                                                        <a href="/faq?category=<c:out value='${cat.id()}'/>" class="btn btn-sm btn-outline-primary">
                                                            Xem FAQ <i class="bi bi-arrow-right ms-1"></i>
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary-subtle text-secondary">Chưa có FAQ</span>
                                                        <a href="/student/tickets/new?categoryId=<c:out value='${cat.id()}'/>" class="btn btn-sm btn-primary">
                                                            <i class="bi bi-chat-dots me-1"></i>Hỏi tư vấn viên
                                                        </a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-secondary small fst-italic py-2">
                                Hiện tại chưa có chuyên mục nào thuộc phòng ban này.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

