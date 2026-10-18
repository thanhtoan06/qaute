<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Tìm kiếm FAQ</title>

<nav aria-label="breadcrumb" class="mb-4">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
        <li class="breadcrumb-item"><a href="/faq">Hỏi đáp</a></li>
        <li class="breadcrumb-item active" aria-current="page">Tìm kiếm</li>
    </ol>
</nav>

<div class="card mb-4 border-0 shadow-sm">
    <div class="card-body p-4 bg-light-subtle rounded">
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
                <div class="input-icon">
                    <span class="input-icon-addon">
                        <i class="bi bi-search"></i>
                    </span>
                    <input type="text" name="q" class="form-control form-control-lg"
                           placeholder="Nhập từ khóa tìm kiếm FAQ..."
                           value="<c:out value='${q}'/>" autofocus required minlength="2"/>
                </div>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary btn-lg w-100">
                    <i class="bi bi-search me-1"></i>Tìm kiếm
                </button>
            </div>
        </form>
    </div>
</div>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-warning d-flex align-items-center mb-4" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2 fs-4"></i>
        <div><c:out value="${errorMessage}"/></div>
    </div>
</c:if>

<div class="row">
    <div class="col-lg-3 mb-4">
        <c:if test="${not empty category or not empty tag or (not empty sort and sort != 'RELEVANCE')}">
            <a href="<c:url value='/search'><c:param name='q' value='${q}'/></c:url>" class="btn btn-outline-danger btn-sm w-100 mb-3">
                <i class="bi bi-x-circle me-1"></i>Xóa bộ lọc
            </a>
        </c:if>

        <div class="card mb-3">
            <div class="card-header py-2">
                <h3 class="card-title fs-5"><i class="bi bi-sort-down me-2 text-primary"></i>Sắp xếp</h3>
            </div>
            <div class="list-group list-group-flush">
                <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='RELEVANCE'/></c:url>"
                   class="list-group-item list-group-item-action ${sort == 'RELEVANCE' or empty sort ? 'active fw-bold' : ''}">
                    Độ liên quan
                </a>
                <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='LATEST'/></c:url>"
                   class="list-group-item list-group-item-action ${sort == 'LATEST' ? 'active fw-bold' : ''}">
                    Mới cập nhật
                </a>
                <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='MOST_VIEWED'/></c:url>"
                   class="list-group-item list-group-item-action ${sort == 'MOST_VIEWED' ? 'active fw-bold' : ''}">
                    Xem nhiều nhất
                </a>
                <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='MOST_HELPFUL'/></c:url>"
                   class="list-group-item list-group-item-action ${sort == 'MOST_HELPFUL' ? 'active fw-bold' : ''}">
                    Hữu ích nhất
                </a>
            </div>
        </div>

        <div class="card mb-3">
            <div class="card-header py-2">
                <h3 class="card-title fs-5"><i class="bi bi-grid me-2 text-primary"></i>Chuyên mục</h3>
            </div>
            <div class="list-group list-group-flush">
                <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                   class="list-group-item list-group-item-action ${empty category ? 'active fw-bold' : ''}">
                    Tất cả chuyên mục
                </a>
                <c:forEach var="dept" items="${tree}">
                    <div class="list-group-item bg-light text-uppercase fw-bold small text-secondary py-1">
                        <c:out value="${dept.name()}"/>
                    </div>
                    <c:forEach var="cat" items="${dept.categories()}">
                        <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:param name='category' value='${cat.id()}'/><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                           class="list-group-item list-group-item-action ps-4 ${category == cat.id() ? 'active fw-bold' : ''}">
                            <i class="bi <c:out value='${empty cat.icon() ? "bi-folder" : cat.icon()}'/> me-1"></i>
                            <c:out value="${cat.name()}"/>
                        </a>
                    </c:forEach>
                </c:forEach>
            </div>
        </div>
    </div>

    <div class="col-lg-9">
        <c:if test="${not empty q and empty errorMessage}">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <div class="text-secondary">
                    Tìm thấy <strong class="text-dark"><c:out value="${page.totalElements}"/></strong> kết quả cho từ khóa "<strong class="text-dark"><c:out value="${q}"/></strong>"
                    <c:if test="${not empty selectedCategoryName}">
                        trong chuyên mục "<strong class="text-dark"><c:out value="${selectedCategoryName}"/></strong>"
                    </c:if>
                </div>
            </div>
        </c:if>

        <c:forEach var="item" items="${page.content}">
            <div class="card mb-3 qa-card-hover">
                <div class="card-body">
                    <h2 class="card-title h3 mb-2">
                        <a href="/faq/<c:out value='${item.slug()}'/>" class="text-primary text-decoration-none">
                            <c:out value="${item.titleHtml()}" escapeXml="false"/>
                        </a>
                    </h2>
                    <p class="text-secondary mb-3">
                        <c:out value="${item.snippetHtml()}" escapeXml="false"/>
                    </p>
                    <div class="d-flex flex-wrap align-items-center gap-3 text-secondary small pt-2 border-top">
                        <c:if test="${not empty item.categoryName()}">
                            <span class="badge bg-light text-secondary border">
                                <i class="bi bi-folder me-1"></i><c:out value="${item.categoryName()}"/>
                            </span>
                        </c:if>
                        <span>
                            <i class="bi bi-eye me-1"></i><c:out value="${item.viewCount()}"/> lượt xem
                        </span>
                        <span>
                            <i class="bi bi-hand-thumbs-up me-1"></i><c:out value="${item.helpfulCount()}"/> hữu ích
                        </span>
                        <c:if test="${not empty item.updatedAt()}">
                            <span>
                                <i class="bi bi-clock-history me-1"></i>Cập nhật: ${item.updatedAt().dayOfMonth}/${item.updatedAt().monthValue}/${item.updatedAt().year}
                            </span>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>

        <qa:pagination page="${page}" baseUrl="/search" params="${queryString}"/>
    </div>
</div>

