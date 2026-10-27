<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Yêu cầu của tôi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/ticket-list.css">
</head>
<div class="qa-page-student-ticket-list" data-qa-view="student-ticket-list">


<div class="container-xl">
    <qa:alert/>

    <%-- ==================== HERO BANNER ==================== --%>
    <section class="qa-ticket-hero" aria-labelledby="student-ticket-title">
        <div class="qa-ticket-hero-content">
            <span class="qa-dashboard-eyebrow">
                <span class="qa-pulse-dot" aria-hidden="true"></span>
                <i class="bi bi-ticket-perforated-fill me-1" aria-hidden="true"></i>Kênh tư vấn trực tuyến
            </span>
            <h1 id="student-ticket-title" class="qa-view-title" data-qa-page="student-ticket-list">
                <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-ticket-detailed-fill"></i></span>
                Danh sách yêu cầu của tôi
            </h1>
            <p>Theo dõi tiến độ giải quyết, nhận thông báo phản hồi từ chuyên viên tư vấn và đánh giá kết quả hỗ trợ một cách minh bạch.</p>
        </div>
        <div class="qa-ticket-hero-actions">
            <a href="${ctx}/student/tickets/new" class="btn btn-white shadow-sm">
                <i class="bi bi-plus-lg me-1"></i> Tạo yêu cầu mới
            </a>
            <a href="${ctx}/student/chat" class="btn btn-outline-light">
                <i class="bi bi-chat-dots-fill me-1"></i> Chat trực tuyến
            </a>
            <a href="${ctx}/student/appointments/book" class="btn btn-outline-light">
                <i class="bi bi-calendar-plus-fill me-1"></i> Đặt lịch hẹn
            </a>
        </div>
    </section>

    <%-- ==================== STATUS TABS ==================== --%>
    <div class="card qa-status-card mb-3">
        <div class="qa-status-tabs">
            <a class="tab-status-link ${empty filter.status ? 'active' : ''}"
               href="${ctx}/student/tickets?q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                <i class="bi bi-grid-fill"></i> Tất cả
                <c:set var="totalCount" value="0"/>
                <c:forEach var="entry" items="${statusCounts}">
                    <c:set var="totalCount" value="${totalCount + entry.value}"/>
                </c:forEach>
                <c:if test="${totalCount > 0}">
                    <span class="badge bg-secondary-lt ms-1">${totalCount}</span>
                </c:if>
            </a>

            <a class="tab-status-link ${filter.status == 'NEW' ? 'active' : ''}"
               href="${ctx}/student/tickets?status=NEW&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                <i class="bi bi-file-earmark-plus-fill text-azure"></i> Mới
                <c:if test="${not empty statusCounts['NEW'] and statusCounts['NEW'] > 0}">
                    <span class="badge bg-azure-lt ms-1">${statusCounts['NEW']}</span>
                </c:if>
            </a>

            <a class="tab-status-link ${filter.status == 'ASSIGNED' ? 'active' : ''}"
               href="${ctx}/student/tickets?status=ASSIGNED&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                <i class="bi bi-person-check-fill text-blue"></i> Đã nhận
                <c:if test="${not empty statusCounts['ASSIGNED'] and statusCounts['ASSIGNED'] > 0}">
                    <span class="badge bg-blue-lt ms-1">${statusCounts['ASSIGNED']}</span>
                </c:if>
            </a>

            <a class="tab-status-link ${filter.status == 'IN_PROGRESS' ? 'active' : ''}"
               href="${ctx}/student/tickets?status=IN_PROGRESS&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                <i class="bi bi-arrow-repeat text-orange"></i> Đang xử lý
                <c:if test="${not empty statusCounts['IN_PROGRESS'] and statusCounts['IN_PROGRESS'] > 0}">
                    <span class="badge bg-orange-lt ms-1">${statusCounts['IN_PROGRESS']}</span>
                </c:if>
            </a>

            <a class="tab-status-link ${filter.status == 'WAITING_STUDENT' ? 'active' : ''}"
               href="${ctx}/student/tickets?status=WAITING_STUDENT&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                <i class="bi bi-hourglass-split text-warning"></i> Chờ tôi phản hồi
                <c:if test="${not empty statusCounts['WAITING_STUDENT'] and statusCounts['WAITING_STUDENT'] > 0}">
                    <span class="badge bg-yellow-lt ms-1">${statusCounts['WAITING_STUDENT']}</span>
                </c:if>
            </a>

            <a class="tab-status-link ${filter.status == 'RESOLVED' ? 'active' : ''}"
               href="${ctx}/student/tickets?status=RESOLVED&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                <i class="bi bi-check2-circle text-success"></i> Đã giải quyết
                <c:if test="${not empty statusCounts['RESOLVED'] and statusCounts['RESOLVED'] > 0}">
                    <span class="badge bg-green-lt ms-1">${statusCounts['RESOLVED']}</span>
                </c:if>
            </a>

            <a class="tab-status-link ${filter.status == 'CLOSED' ? 'active' : ''}"
               href="${ctx}/student/tickets?status=CLOSED&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                <i class="bi bi-archive-fill text-secondary"></i> Đã đóng
                <c:if test="${not empty statusCounts['CLOSED'] and statusCounts['CLOSED'] > 0}">
                    <span class="badge bg-secondary-lt ms-1">${statusCounts['CLOSED']}</span>
                </c:if>
            </a>

            <a class="tab-status-link ${filter.status == 'CANCELLED' ? 'active' : ''}"
               href="${ctx}/student/tickets?status=CANCELLED&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                <i class="bi bi-x-circle-fill text-danger"></i> Đã hủy
                <c:if test="${not empty statusCounts['CANCELLED'] and statusCounts['CANCELLED'] > 0}">
                    <span class="badge bg-secondary-lt ms-1">${statusCounts['CANCELLED']}</span>
                </c:if>
            </a>
        </div>
    </div>

    <%-- ==================== SEARCH & FILTER ==================== --%>
    <div class="qa-filter-card mb-3">
        <form method="get" action="${ctx}/student/tickets" class="row g-3 align-items-end">
            <c:if test="${not empty filter.status}">
                <input type="hidden" name="status" value="${filter.status}">
            </c:if>
            <div class="col-12 col-md-4">
                <label class="form-label"><i class="bi bi-search me-1 text-primary"></i>Tìm kiếm</label>
                <input type="text" name="q" class="form-control"
                       placeholder="Nhập mã hoặc tiêu đề yêu cầu..."
                       value="<c:out value='${filter.q}'/>">
            </div>
            <div class="col-12 col-md-3">
                <label class="form-label"><i class="bi bi-folder2-open me-1 text-primary"></i>Chuyên mục</label>
                <select name="categoryId" class="form-select">
                    <option value="">Tất cả chuyên mục</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.id()}" ${filter.categoryId == cat.id() ? 'selected' : ''}>
                            <c:out value="${cat.departmentName()} › ${cat.name()}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-6 col-md-2">
                <label class="form-label"><i class="bi bi-calendar-event me-1 text-primary"></i>Từ ngày</label>
                <input type="date" name="from" class="form-control" value="${filter.from}">
            </div>
            <div class="col-6 col-md-2">
                <label class="form-label"><i class="bi bi-calendar-check me-1 text-primary"></i>Đến ngày</label>
                <input type="date" name="to" class="form-control" value="${filter.to}">
            </div>
            <div class="col-12 col-md-1 d-flex gap-2">
                <button type="submit" class="btn btn-primary flex-fill shadow-sm" title="Tìm kiếm">
                    <i class="bi bi-search"></i>
                </button>
                <a href="${ctx}/student/tickets${not empty filter.status ? '?status='.concat(filter.status) : ''}"
                   class="btn btn-outline-secondary" title="Xóa bộ lọc">
                    <i class="bi bi-arrow-clockwise"></i>
                </a>
            </div>
        </form>
    </div>

    <%-- ==================== RESULTS ==================== --%>
    <div class="card qa-results-card">
        <div class="card-header">
            <div class="d-flex align-items-center gap-2">
                <span class="badge bg-primary-lt px-3 py-1 font-monospace fw-bold">
                    <i class="bi bi-collection me-1"></i>Tổng: ${tickets.totalElements} yêu cầu
                </span>
            </div>
            <div class="d-flex align-items-center gap-2">
                <label class="form-label mb-0 text-secondary small fw-bold"><i class="bi bi-arrow-down-up me-1"></i>Sắp xếp:</label>
                <select class="form-select form-select-sm u-width-auto" data-ticket-sort
                        data-context-path="<c:out value='${ctx}'/>"
                        data-status="<c:out value='${filter.status}'/>"
                        data-query="<c:out value='${filter.q}'/>"
                        data-category-id="<c:out value='${filter.categoryId}'/>"
                        data-from="<c:out value='${filter.from}'/>"
                        data-to="<c:out value='${filter.to}'/>">
                    <option value="lastActivityAt" ${empty filter.sort or filter.sort == 'lastActivityAt' ? 'selected' : ''}>Hoạt động gần nhất</option>
                    <option value="createdAt" ${filter.sort == 'createdAt' ? 'selected' : ''}>Ngày tạo mới nhất</option>
                </select>
            </div>
        </div>

        <c:choose>
            <c:when test="${tickets.totalElements == 0}">
                <div class="card-body py-5">
                    <qa:emptyState icon="bi-ticket-perforated"
                                   title="Chưa có yêu cầu nào"
                                   message="Bạn chưa gửi yêu cầu tư vấn nào phù hợp với bộ lọc. Hãy tạo yêu cầu để được hỗ trợ!"
                                   actionUrl="${ctx}/student/tickets/new"
                                   actionLabel="Tạo yêu cầu mới"/>
                </div>
            </c:when>
            <c:otherwise>
                <%-- Desktop Table View --%>
                <div class="table-responsive table-responsive-card">
                    <table class="table table-vcenter table-hover card-table">
                        <thead>
                            <tr>
                                <th>Mã yêu cầu</th>
                                <th>Tiêu đề</th>
                                <th>Chuyên mục</th>
                                <th>Trạng thái</th>
                                <th>Người xử lý</th>
                                <th>Cập nhật lúc</th>
                                <th class="text-end">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${tickets.content}">
                                <tr>
                                    <td class="text-nowrap">
                                        <a href="${ctx}/student/tickets/${item.id()}" class="qa-code-chip">
                                            <i class="bi bi-hash"></i><c:out value="${item.code()}"/>
                                        </a>
                                    </td>
                                    <td>
                                        <a href="${ctx}/student/tickets/${item.id()}" class="qa-ticket-title-link">
                                            <c:out value="${item.title()}"/>
                                        </a>
                                    </td>
                                    <td class="text-secondary small">
                                        <span class="badge bg-secondary-lt">
                                            <i class="bi bi-folder2 me-1"></i><c:out value="${item.categoryName()}"/>
                                        </span>
                                    </td>
                                    <td class="text-nowrap">
                                        <qa:statusBadge status="${item.status()}"/>
                                    </td>
                                    <td class="text-secondary small">
                                        <c:choose>
                                            <c:when test="${not empty item.assigneeName()}">
                                                <div class="d-flex align-items-center gap-1 text-dark fw-semibold">
                                                    <i class="bi bi-person-circle text-teal"></i>
                                                    <c:out value="${item.assigneeName()}"/>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted"><i class="bi bi-hourglass me-1"></i>Đang chờ tiếp nhận</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-secondary small text-nowrap">
                                        <i class="bi bi-clock-history me-1 text-muted"></i><c:out value="${item.lastActivityAt()}"/>
                                    </td>
                                    <td class="text-end text-nowrap">
                                        <a href="${ctx}/student/tickets/${item.id()}" class="btn btn-sm btn-outline-primary">
                                            Chi tiết <i class="bi bi-arrow-right ms-1"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <%-- Mobile Cards View --%>
                <div class="ticket-cards p-3">
                    <c:forEach var="item" items="${tickets.content}">
                        <a href="${ctx}/student/tickets/${item.id()}" class="text-reset text-decoration-none d-block">
                            <div class="ticket-card-item">
                                <div class="d-flex justify-content-between align-items-start mb-2">
                                    <span class="qa-code-chip">
                                        <i class="bi bi-hash"></i><c:out value="${item.code()}"/>
                                    </span>
                                    <qa:statusBadge status="${item.status()}"/>
                                </div>
                                <div class="fw-bold mb-1 text-dark fs-3"><c:out value="${item.title()}"/></div>
                                <div class="text-secondary small mb-2"><i class="bi bi-folder2 me-1 text-primary"></i><c:out value="${item.categoryName()}"/></div>
                                <div class="d-flex justify-content-between align-items-center pt-2 border-top">
                                    <span class="text-secondary small">
                                        <c:if test="${not empty item.assigneeName()}">
                                            <i class="bi bi-person-fill me-1 text-teal"></i><c:out value="${item.assigneeName()}"/>
                                        </c:if>
                                    </span>
                                    <span class="text-muted small">
                                        <i class="bi bi-clock me-1"></i><c:out value="${item.lastActivityAt()}"/>
                                    </span>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </div>

                <div class="card-footer d-flex justify-content-center py-3">
                    <c:set var="paginationParams" value="status=${filter.status}&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}&size=${tickets.size}"/>
                    <qa:pagination page="${tickets}" baseUrl="${ctx}/student/tickets" params="${paginationParams}"/>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
    <script src="${pageContext.request.contextPath}/assets/js/student/ticket-list.js"></script>
</div>
