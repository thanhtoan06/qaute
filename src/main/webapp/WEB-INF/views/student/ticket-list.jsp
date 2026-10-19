<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Yêu cầu của tôi</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<style>
    .tab-status-link {
        white-space: nowrap;
    }
    @media (max-width: 767.98px) {
        .table-responsive-card table {
            display: none;
        }
        .ticket-cards {
            display: block;
        }
    }
    @media (min-width: 768px) {
        .ticket-cards {
            display: none;
        }
    }
</style>

<div class="page-header d-print-none">
    <div class="container-xl">
        <div class="row g-2 align-items-center">
            <div class="col">
                <h2 class="page-title">Yêu cầu của tôi</h2>
            </div>
            <div class="col-auto ms-auto">
                <a href="${ctx}/student/tickets/new" class="btn btn-primary">
                    <i class="bi bi-plus-lg me-1"></i>Tạo yêu cầu mới
                </a>
            </div>
        </div>
    </div>
</div>

<div class="page-body">
    <div class="container-xl">
        <qa:alert/>

        <%-- Status tabs --%>
        <div class="card mb-3">
            <div class="card-header p-0">
                <ul class="nav nav-tabs card-header-tabs flex-nowrap overflow-auto">
                    <li class="nav-item">
                        <a class="nav-link tab-status-link ${empty filter.status ? 'active' : ''}"
                           href="${ctx}/student/tickets?q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                            Tất cả
                            <c:set var="totalCount" value="0"/>
                            <c:forEach var="entry" items="${statusCounts}">
                                <c:set var="totalCount" value="${totalCount + entry.value}"/>
                            </c:forEach>
                            <c:if test="${totalCount > 0}">
                                <span class="badge bg-secondary-lt ms-1">${totalCount}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link tab-status-link ${filter.status == 'NEW' ? 'active' : ''}"
                           href="${ctx}/student/tickets?status=NEW&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                            Mới
                            <c:if test="${not empty statusCounts['NEW'] and statusCounts['NEW'] > 0}">
                                <span class="badge bg-azure-lt ms-1">${statusCounts['NEW']}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link tab-status-link ${filter.status == 'ASSIGNED' ? 'active' : ''}"
                           href="${ctx}/student/tickets?status=ASSIGNED&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                            Đã nhận
                            <c:if test="${not empty statusCounts['ASSIGNED'] and statusCounts['ASSIGNED'] > 0}">
                                <span class="badge bg-blue-lt ms-1">${statusCounts['ASSIGNED']}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link tab-status-link ${filter.status == 'IN_PROGRESS' ? 'active' : ''}"
                           href="${ctx}/student/tickets?status=IN_PROGRESS&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                            Đang xử lý
                            <c:if test="${not empty statusCounts['IN_PROGRESS'] and statusCounts['IN_PROGRESS'] > 0}">
                                <span class="badge bg-orange-lt ms-1">${statusCounts['IN_PROGRESS']}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link tab-status-link ${filter.status == 'WAITING_STUDENT' ? 'active' : ''}"
                           href="${ctx}/student/tickets?status=WAITING_STUDENT&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                            Chờ tôi phản hồi
                            <c:if test="${not empty statusCounts['WAITING_STUDENT'] and statusCounts['WAITING_STUDENT'] > 0}">
                                <span class="badge bg-yellow-lt ms-1">${statusCounts['WAITING_STUDENT']}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link tab-status-link ${filter.status == 'RESOLVED' ? 'active' : ''}"
                           href="${ctx}/student/tickets?status=RESOLVED&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                            Đã giải quyết
                            <c:if test="${not empty statusCounts['RESOLVED'] and statusCounts['RESOLVED'] > 0}">
                                <span class="badge bg-green-lt ms-1">${statusCounts['RESOLVED']}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link tab-status-link ${filter.status == 'CLOSED' ? 'active' : ''}"
                           href="${ctx}/student/tickets?status=CLOSED&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                            Đã đóng
                            <c:if test="${not empty statusCounts['CLOSED'] and statusCounts['CLOSED'] > 0}">
                                <span class="badge bg-secondary-lt ms-1">${statusCounts['CLOSED']}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link tab-status-link ${filter.status == 'CANCELLED' ? 'active' : ''}"
                           href="${ctx}/student/tickets?status=CANCELLED&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}">
                            Đã hủy
                            <c:if test="${not empty statusCounts['CANCELLED'] and statusCounts['CANCELLED'] > 0}">
                                <span class="badge bg-secondary-lt ms-1">${statusCounts['CANCELLED']}</span>
                            </c:if>
                        </a>
                    </li>
                </ul>
            </div>
        </div>

        <%-- Search & filter --%>
        <div class="card mb-3">
            <div class="card-body">
                <form method="get" action="${ctx}/student/tickets" class="row g-2 align-items-end">
                    <c:if test="${not empty filter.status}">
                        <input type="hidden" name="status" value="${filter.status}">
                    </c:if>
                    <div class="col-12 col-md-4">
                        <label class="form-label">Tìm kiếm</label>
                        <input type="text" name="q" class="form-control"
                               placeholder="Mã hoặc tiêu đề yêu cầu..."
                               value="<c:out value='${filter.q}'/>">
                    </div>
                    <div class="col-12 col-md-3">
                        <label class="form-label">Chuyên mục</label>
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
                        <label class="form-label">Từ ngày</label>
                        <input type="date" name="from" class="form-control" value="${filter.from}">
                    </div>
                    <div class="col-6 col-md-2">
                        <label class="form-label">Đến ngày</label>
                        <input type="date" name="to" class="form-control" value="${filter.to}">
                    </div>
                    <div class="col-12 col-md-1 d-flex gap-2">
                        <button type="submit" class="btn btn-primary flex-fill">
                            <i class="bi bi-search"></i>
                        </button>
                        <a href="${ctx}/student/tickets${not empty filter.status ? '?status='.concat(filter.status) : ''}"
                           class="btn btn-outline-secondary" title="Xóa bộ lọc">
                            <i class="bi bi-x-lg"></i>
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <%-- Results --%>
        <div class="card">
            <div class="card-header d-flex align-items-center justify-content-between">
                <span class="text-secondary">
                    Tổng: <strong>${tickets.totalElements}</strong> yêu cầu
                </span>
                <div class="d-flex align-items-center gap-2">
                    <label class="form-label mb-0 text-secondary small">Sắp xếp:</label>
                    <select class="form-select form-select-sm" style="width:auto"
                            onchange="location.href='${ctx}/student/tickets?status=${filter.status}&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort='+this.value">
                        <option value="lastActivityAt" ${empty filter.sort or filter.sort == 'lastActivityAt' ? 'selected' : ''}>Hoạt động gần nhất</option>
                        <option value="createdAt" ${filter.sort == 'createdAt' ? 'selected' : ''}>Ngày tạo</option>
                    </select>
                </div>
            </div>

            <c:choose>
                <c:when test="${tickets.totalElements == 0}">
                    <div class="card-body">
                        <qa:emptyState icon="bi-ticket-perforated"
                                       title="Chưa có yêu cầu nào"
                                       message="Bạn chưa gửi yêu cầu tư vấn nào. Hãy tạo yêu cầu đầu tiên!"
                                       actionUrl="${ctx}/student/tickets/new"
                                       actionLabel="Tạo yêu cầu mới"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <%-- Desktop table --%>
                    <div class="table-responsive table-responsive-card">
                        <table class="table table-vcenter table-hover card-table">
                            <thead>
                                <tr>
                                    <th>Mã</th>
                                    <th>Tiêu đề</th>
                                    <th>Chuyên mục</th>
                                    <th>Trạng thái</th>
                                    <th>Người xử lý</th>
                                    <th>Cập nhật lúc</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${tickets.content}">
                                    <tr>
                                        <td class="text-nowrap">
                                            <a href="${ctx}/student/tickets/${item.id()}"
                                               class="text-reset fw-medium font-monospace">
                                                <c:out value="${item.code()}"/>
                                            </a>
                                        </td>
                                        <td>
                                            <a href="${ctx}/student/tickets/${item.id()}" class="text-reset">
                                                <c:out value="${item.title()}"/>
                                            </a>
                                        </td>
                                        <td class="text-secondary small">
                                            <c:out value="${item.categoryName()}"/>
                                        </td>
                                        <td class="text-nowrap">
                                            <qa:statusBadge status="${item.status()}"/>
                                        </td>
                                        <td class="text-secondary small">
                                            <c:choose>
                                                <c:when test="${not empty item.assigneeName()}">
                                                    <c:out value="${item.assigneeName()}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">—</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-secondary small text-nowrap">
                                            <c:out value="${item.lastActivityAt()}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <%-- Mobile cards --%>
                    <div class="ticket-cards p-2">
                        <c:forEach var="item" items="${tickets.content}">
                            <a href="${ctx}/student/tickets/${item.id()}" class="text-reset text-decoration-none">
                                <div class="card mb-2">
                                    <div class="card-body py-2 px-3">
                                        <div class="d-flex justify-content-between align-items-start mb-1">
                                            <span class="font-monospace text-secondary small">
                                                <c:out value="${item.code()}"/>
                                            </span>
                                            <qa:statusBadge status="${item.status()}"/>
                                        </div>
                                        <div class="fw-medium mb-1"><c:out value="${item.title()}"/></div>
                                        <div class="text-secondary small"><c:out value="${item.categoryName()}"/></div>
                                        <div class="d-flex justify-content-between mt-1">
                                            <span class="text-secondary small">
                                                <c:if test="${not empty item.assigneeName()}">
                                                    <i class="bi bi-person me-1"></i><c:out value="${item.assigneeName()}"/>
                                                </c:if>
                                            </span>
                                            <span class="text-muted small">
                                                <c:out value="${item.lastActivityAt()}"/>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                    </div>

                    <div class="card-footer d-flex justify-content-center">
                        <c:set var="paginationParams" value="status=${filter.status}&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort=${filter.sort}&size=${tickets.size}"/>
                        <qa:pagination page="${tickets}" baseUrl="${ctx}/student/tickets" params="${paginationParams}"/>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
