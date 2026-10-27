<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Giám sát yêu cầu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/ticket-list.css">
</head>
<div class="qa-page-admin-ticket-list" data-qa-view="admin-ticket-list">


<div class="container-xl">
  <qa:alert/>

  <%-- ==================== HERO BANNER ==================== --%>
  <section class="qa-admin-hero" aria-labelledby="admin-ticket-title">
    <div class="qa-admin-hero-content">
      <span class="qa-dashboard-eyebrow">
        <span class="qa-pulse-dot" aria-hidden="true"></span>
        <i class="bi bi-shield-lock-fill me-1" aria-hidden="true"></i>Giám sát toàn trường
      </span>
      <h1 id="admin-ticket-title" class="qa-view-title" data-qa-page="admin-ticket-list">
        <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-ticket-detailed-fill"></i></span>
        Giám sát & Quản trị yêu cầu
      </h1>
      <p>Theo dõi toàn bộ luồng yêu cầu hỗ trợ sinh viên trong toàn hệ thống, giám sát mức độ cam kết SLA, phân loại phòng ban và chỉ số hài lòng.</p>
    </div>
    <div class="qa-admin-hero-actions">
      <a class="btn btn-outline-light" href="${ctx}/admin/reports">
        <i class="bi bi-bar-chart-line-fill me-2"></i>Báo cáo dịch vụ
      </a>
      <a class="btn btn-outline-light" href="${ctx}/admin/dashboard">
        <i class="bi bi-arrow-left me-1"></i> Dashboard
      </a>
    </div>
  </section>

  <%-- ==================== ADVANCED FILTER ==================== --%>
  <div class="qa-filter-card mb-3">
    <form method="get" action="${ctx}/admin/tickets" class="qa-filter-bar">
      <div>
        <label class="form-label" for="ticket-q"><i class="bi bi-search me-1 text-primary"></i>Mã hoặc tiêu đề</label>
        <input class="form-control" id="ticket-q" type="search" name="q" maxlength="150" value="<c:out value='${filter.q}'/>" placeholder="Tìm theo mã hoặc từ khóa..."/>
      </div>
      <div>
        <label class="form-label" for="ticket-status"><i class="bi bi-check2-circle me-1 text-primary"></i>Trạng thái</label>
        <select class="form-select" id="ticket-status" name="status">
          <option value="">Tất cả trạng thái</option>
          <c:forEach var="item" items="${statuses}">
            <option value="${item}" ${filter.status eq item ? 'selected' : ''}><c:out value="${item.label}"/></option>
          </c:forEach>
        </select>
      </div>
      <div>
        <label class="form-label" for="ticket-priority"><i class="bi bi-flag-fill me-1 text-primary"></i>Ưu tiên</label>
        <select class="form-select" id="ticket-priority" name="priority">
          <option value="">Tất cả mức độ</option>
          <c:forEach var="item" items="${priorities}">
            <option value="${item}" ${filter.priority eq item ? 'selected' : ''}><c:out value="${item.label}"/></option>
          </c:forEach>
        </select>
      </div>
      <div>
        <label class="form-label" for="ticket-department"><i class="bi bi-building me-1 text-primary"></i>Phòng ban</label>
        <select class="form-select" id="ticket-department" name="departmentId">
          <option value="">Tất cả phòng ban</option>
          <c:forEach var="item" items="${departments}">
            <option value="${item.id}" ${filter.departmentId eq item.id ? 'selected' : ''}><c:out value="${item.name}"/></option>
          </c:forEach>
        </select>
      </div>
      <div>
        <label class="form-label" for="ticket-category"><i class="bi bi-folder2-open me-1 text-primary"></i>Chuyên mục</label>
        <select class="form-select" id="ticket-category" name="categoryId">
          <option value="">Tất cả chuyên mục</option>
          <c:forEach var="item" items="${categories}">
            <option value="${item.key}" ${filter.categoryId eq item.key ? 'selected' : ''}><c:out value="${item.value}"/></option>
          </c:forEach>
        </select>
      </div>
      <div>
        <label class="form-label" for="ticket-assignee"><i class="bi bi-person-badge me-1 text-primary"></i>Người phụ trách</label>
        <select class="form-select" id="ticket-assignee" name="assigneeId">
          <option value="">Tất cả chuyên viên</option>
          <c:forEach var="item" items="${managers}">
            <option value="${item.id}" ${filter.assigneeId eq item.id ? 'selected' : ''}><c:out value="${item.fullName}"/></option>
          </c:forEach>
        </select>
      </div>
      <div>
        <label class="form-label" for="ticket-sla"><i class="bi bi-shield-exclamation me-1 text-primary"></i>SLA</label>
        <select class="form-select" id="ticket-sla" name="slaStatus">
          <option value="">Tất cả cam kết</option>
          <c:forEach var="item" items="${slaStatuses}">
            <option value="${item}" ${filter.slaStatus eq item ? 'selected' : ''}><c:out value="${item}"/></option>
          </c:forEach>
        </select>
      </div>
      <div>
        <label class="form-label" for="ticket-from"><i class="bi bi-calendar-event me-1 text-primary"></i>Tạo từ</label>
        <input class="form-control" id="ticket-from" type="date" name="from" value="<c:out value='${filter.from}'/>"/>
      </div>
      <div>
        <label class="form-label" for="ticket-to"><i class="bi bi-calendar-check me-1 text-primary"></i>Đến ngày</label>
        <input class="form-control" id="ticket-to" type="date" name="to" value="<c:out value='${filter.to}'/>"/>
      </div>
      <div>
        <label class="form-label" for="ticket-min-rating"><i class="bi bi-star-half me-1 text-primary"></i>Đánh giá từ</label>
        <select class="form-select" id="ticket-min-rating" name="minRating">
          <option value="">Tất cả</option>
          <c:forEach var="rating" begin="1" end="5">
            <option value="${rating}" ${filter.minRating eq rating ? 'selected' : ''}>${rating} sao</option>
          </c:forEach>
        </select>
      </div>
      <div>
        <label class="form-label" for="ticket-max-rating"><i class="bi bi-star-fill me-1 text-primary"></i>Đánh giá đến</label>
        <select class="form-select" id="ticket-max-rating" name="maxRating">
          <option value="">Tất cả</option>
          <c:forEach var="rating" begin="1" end="5">
            <option value="${rating}" ${filter.maxRating eq rating ? 'selected' : ''}>${rating} sao</option>
          </c:forEach>
        </select>
      </div>
      <div>
        <label class="form-label" for="ticket-sort"><i class="bi bi-arrow-down-up me-1 text-primary"></i>Sắp xếp</label>
        <select class="form-select" id="ticket-sort" name="sort">
          <option value="createdAt" ${empty filter.sort or filter.sort eq 'createdAt' ? 'selected' : ''}>Mới tạo</option>
          <option value="lastActivityAt" ${filter.sort eq 'lastActivityAt' ? 'selected' : ''}>Mới hoạt động</option>
          <option value="priority" ${filter.sort eq 'priority' ? 'selected' : ''}>Độ ưu tiên</option>
        </select>
      </div>
      <div class="d-flex align-items-end gap-2">
        <button class="btn btn-primary flex-fill shadow-sm" type="submit">
          <i class="bi bi-filter me-1"></i>Lọc
        </button>
        <a class="btn btn-outline-secondary" href="${ctx}/admin/tickets" title="Bỏ lọc">
          <i class="bi bi-arrow-clockwise"></i>
        </a>
      </div>
    </form>
  </div>

  <%-- ==================== TABLE RESULTS ==================== --%>
  <div class="card qa-table-card mb-3">
    <div class="card-header">
      <div class="d-flex align-items-center gap-2">
        <span class="badge bg-primary-lt px-3 py-1 font-monospace fw-bold">
          <i class="bi bi-collection me-1"></i>Tổng cộng: ${tickets.totalElements} yêu cầu
        </span>
      </div>
      <div class="text-secondary small">
        Toàn trường HCMUTE
      </div>
    </div>
    <div class="table-responsive">
      <table class="table table-vcenter card-table table-hover">
        <thead>
          <tr>
            <th>Mã</th>
            <th>Tiêu đề yêu cầu</th>
            <th>Sinh viên</th>
            <th>Chuyên mục</th>
            <th>Ưu tiên</th>
            <th>Trạng thái</th>
            <th>SLA</th>
            <th>Phụ trách</th>
            <th>Tạo lúc</th>
            <th class="text-end">Thao tác</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty tickets.content}">
              <tr>
                <td colspan="10" class="py-5">
                  <qa:emptyState icon="bi-inbox" title="Không có yêu cầu phù hợp" message="Thử thay đổi điều kiện lọc để tìm kiếm yêu cầu khác."/>
                </td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="item" items="${tickets.content}">
                <tr>
                  <td class="text-nowrap">
                    <span class="qa-code-chip"><i class="bi bi-hash"></i><c:out value="${item.code()}"/></span>
                  </td>
                  <td>
                    <a href="${ctx}/admin/tickets/${item.id()}" class="fw-bold text-dark text-decoration-none">
                      <c:out value="${item.title()}"/>
                    </a>
                  </td>
                  <td>
                    <div class="fw-semibold"><i class="bi bi-person me-1 text-secondary"></i><c:out value="${item.studentName()}"/></div>
                  </td>
                  <td class="small text-secondary">
                    <span class="badge bg-secondary-lt"><c:out value="${item.categoryPath()}"/></span>
                  </td>
                  <td><qa:priorityBadge priority="${item.priority()}"/></td>
                  <td><qa:statusBadge status="${item.status()}"/></td>
                  <td><qa:slaBadge sla="${item.slaStatus()}"/></td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty item.assigneeName()}">
                        <span class="fw-semibold text-dark"><i class="bi bi-person-circle me-1 text-teal"></i><c:out value="${item.assigneeName()}"/></span>
                      </c:when>
                      <c:otherwise>
                        <span class="text-muted"><i class="bi bi-dash-circle me-1"></i>Chưa nhận</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td class="small text-secondary text-nowrap">
                    <i class="bi bi-clock me-1 text-muted"></i><c:out value="${item.createdAt()}"/>
                  </td>
                  <td class="text-end text-nowrap">
                    <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/tickets/${item.id()}">
                      <i class="bi bi-eye me-1"></i>Mở
                    </a>
                  </td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>
  </div>

  <div class="d-flex justify-content-center">
    <qa:pagination page="${tickets}" baseUrl="${ctx}/admin/tickets" params="${paginationParams}"/>
  </div>
</div>
</div>
