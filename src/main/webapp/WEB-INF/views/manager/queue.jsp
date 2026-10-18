<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Hàng đợi yêu cầu</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0">Hàng đợi yêu cầu</h2>
  <span class="text-secondary small">Ưu tiên theo SLA, mức độ và thời gian tạo</span>
</div>

<div class="nav nav-tabs mb-3" role="tablist" aria-label="Hàng đợi yêu cầu">
  <a class="nav-link ${filter.tab eq 'unassigned' ? 'active' : ''}" href="${ctx}/manager/queue?${tabParams}tab=unassigned">Chưa nhận <span class="badge bg-secondary-lt ms-1">${counts.unassigned}</span></a>
  <a class="nav-link ${filter.tab eq 'mine' ? 'active' : ''}" href="${ctx}/manager/queue?${tabParams}tab=mine">Của tôi <span class="badge bg-secondary-lt ms-1">${counts.mine}</span></a>
  <a class="nav-link ${filter.tab eq 'waiting' ? 'active' : ''}" href="${ctx}/manager/queue?${tabParams}tab=waiting">Chờ sinh viên <span class="badge bg-secondary-lt ms-1">${counts.waiting}</span></a>
  <a class="nav-link ${filter.tab eq 'slarisk' ? 'active' : ''}" href="${ctx}/manager/queue?${tabParams}tab=slarisk">Nguy cơ SLA <span class="badge bg-secondary-lt ms-1">${counts.slarisk}</span></a>
  <a class="nav-link ${filter.tab eq 'all' ? 'active' : ''}" href="${ctx}/manager/queue?${tabParams}tab=all">Tất cả trong phạm vi <span class="badge bg-secondary-lt ms-1">${counts.all}</span></a>
</div>

<form method="get" action="${ctx}/manager/queue" class="qa-filter-bar mb-3">
  <input type="hidden" name="tab" value="<c:out value='${filter.tab}'/>"/>
  <div><label class="form-label" for="queue-q">Mã hoặc tiêu đề</label><input class="form-control" id="queue-q" type="search" name="q" maxlength="150" value="<c:out value='${filter.q}'/>" placeholder="Tìm yêu cầu"/></div>
  <div><label class="form-label" for="queue-category">Chuyên mục</label><select class="form-select" id="queue-category" name="categoryId"><option value="">Tất cả</option><c:forEach var="category" items="${categories}"><option value="${category.key}" ${filter.categoryId eq category.key ? 'selected' : ''}><c:out value="${category.value}"/></option></c:forEach></select></div>
  <div><label class="form-label" for="queue-priority">Ưu tiên</label><select class="form-select" id="queue-priority" name="priority"><option value="">Tất cả</option><c:forEach var="priority" items="${priorities}"><option value="${priority}" ${filter.priority eq priority ? 'selected' : ''}><c:out value="${priority.label}"/></option></c:forEach></select></div>
  <div><label class="form-label" for="queue-status">Trạng thái</label><select class="form-select" id="queue-status" name="status"><option value="">Tất cả</option><c:forEach var="status" items="${statuses}"><option value="${status}" ${filter.status eq status ? 'selected' : ''}><c:out value="${status.label}"/></option></c:forEach></select></div>
  <div><label class="form-label" for="queue-urgent">SV đề nghị gấp</label><select class="form-select" id="queue-urgent" name="urgentRequested"><option value="">Tất cả</option><option value="true" ${filter.urgentRequested eq true ? 'selected' : ''}>Có</option><option value="false" ${filter.urgentRequested eq false ? 'selected' : ''}>Không</option></select></div>
  <div><label class="form-label" for="queue-assignee">ID người phụ trách</label><input class="form-control" id="queue-assignee" type="number" min="1" name="assigneeId" value="<c:out value='${filter.assigneeId}'/>"/></div>
  <div><label class="form-label" for="queue-age">Tuổi tối thiểu (giờ)</label><input class="form-control" id="queue-age" type="number" min="0" max="100000" name="minAgeHours" value="<c:out value='${filter.minAgeHours}'/>"/></div>
  <div><label class="form-label" for="queue-sort">Sắp xếp</label><select class="form-select" id="queue-sort" name="sort"><option value="" ${empty filter.sort ? 'selected' : ''}>Ưu tiên xử lý</option><option value="createdAt" ${filter.sort eq 'createdAt' ? 'selected' : ''}>Tạo mới nhất</option><option value="lastActivityAt" ${filter.sort eq 'lastActivityAt' ? 'selected' : ''}>Hoạt động mới nhất</option></select></div>
  <div class="d-flex gap-2"><button class="btn btn-primary" type="submit">Lọc</button><a class="btn btn-outline-secondary" href="${ctx}/manager/queue?tab=${filter.tab}">Bỏ lọc</a></div>
</form>

<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table qa-admin-table">
  <thead><tr><th>Mã</th><th>Tiêu đề</th><th>Sinh viên</th><th>Chuyên mục</th><th>Ưu tiên</th><th>Trạng thái</th><th>SLA</th><th>Người phụ trách</th><th>Tuổi</th><th>Đề nghị gấp</th><th>Hoạt động cuối</th><th>Thao tác</th></tr></thead>
  <tbody><c:choose><c:when test="${empty tickets.content}"><tr><td colspan="12"><qa:emptyState icon="bi-inbox" title="Không có yêu cầu phù hợp" message="Hãy thử đổi điều kiện lọc hoặc chọn tab khác."/></td></tr></c:when>
  <c:otherwise><c:forEach var="item" items="${tickets.content}"><tr>
    <td class="fw-semibold"><c:out value="${item.code()}"/></td>
    <td><c:out value="${item.title()}"/></td>
    <td><c:out value="${item.studentName()}"/></td>
    <td><c:out value="${item.categoryPath()}"/></td>
    <td><qa:priorityBadge priority="${item.priority()}"/></td>
    <td><qa:statusBadge status="${item.status()}"/></td>
    <td><qa:slaBadge sla="${item.slaStatus()}"/></td>
    <td><c:out value="${item.assigneeName()}"/><c:if test="${item.mine()}"><span class="badge bg-blue-lt ms-1">Tôi</span></c:if></td>
    <td>${item.ageHours()} giờ</td>
    <td><c:if test="${item.urgentRequested()}"><span class="badge bg-red-lt">SV đề nghị gấp</span></c:if></td>
    <td><c:out value="${item.lastActivityAt()}"/></td>
    <td><div class="d-flex flex-wrap gap-1">
      <a class="btn btn-sm btn-outline-secondary" href="${ctx}/manager/tickets/${item.id()}">Mở</a>
      <c:if test="${item.status() eq 'NEW'}">
        <form method="post" action="${ctx}/manager/tickets/${item.id()}/claim" class="m-0">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <input type="hidden" name="redirect" value="<c:out value='${queueReturn}'/>"/>
          <button class="btn btn-sm btn-primary" type="submit">Nhận</button>
        </form>
      </c:if>
      <c:if test="${item.status() eq 'NEW' or item.status() eq 'ASSIGNED' or item.status() eq 'IN_PROGRESS' or item.status() eq 'WAITING_STUDENT'}">
        <button class="btn btn-sm btn-outline-primary js-assign-ticket" type="button"
                data-bs-toggle="modal" data-bs-target="#assign-ticket-modal"
                data-ticket-id="${item.id()}"
                data-advisors-url="${ctx}/manager/tickets/${item.id()}/advisors"
                data-assign-url="${ctx}/manager/tickets/${item.id()}/assign">Chuyển</button>
      </c:if>
      <c:if test="${item.mine() and (item.status() eq 'ASSIGNED' or item.status() eq 'IN_PROGRESS' or item.status() eq 'WAITING_STUDENT')}">
        <qa:confirmModal id="return-ticket-${item.id()}" title="Trả về hàng đợi" message="Trả yêu cầu này về hàng đợi chưa nhận?" actionUrl="${ctx}/manager/tickets/${item.id()}/return?redirect=${queueReturnEncoded}" confirmLabel="Trả về hàng đợi" cssClass="btn-sm btn-outline-warning" requireReason="${true}"/>
      </c:if>
    </div></td>
  </tr></c:forEach></c:otherwise></c:choose></tbody>
</table></div></div>

<qa:pagination page="${tickets}" baseUrl="${ctx}/manager/queue" params="${paginationParams}"/>

<div class="modal modal-blur fade" id="assign-ticket-modal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered"><div class="modal-content">
    <div class="modal-header"><h3 class="modal-title">Chuyển yêu cầu <span id="assign-ticket-label"></span></h3><button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button></div>
    <form id="assign-ticket-form" method="post">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input type="hidden" name="redirect" value="<c:out value='${queueReturn}'/>"/>
      <div class="modal-body">
        <div id="assign-ticket-error" class="alert alert-danger d-none" role="alert"></div>
        <div class="mb-3"><label class="form-label required" for="assign-to-manager">Tư vấn viên nhận</label><select class="form-select" id="assign-to-manager" name="toManagerId" required disabled><option value="">Đang tải danh sách…</option></select></div>
        <div><label class="form-label required" for="assign-reason">Lý do chuyển</label><textarea class="form-control" id="assign-reason" name="reason" minlength="10" maxlength="500" rows="3" required></textarea><small class="form-hint">Từ 10 đến 500 ký tự.</small></div>
      </div>
      <div class="modal-footer"><button type="button" class="btn btn-link" data-bs-dismiss="modal">Hủy</button><button class="btn btn-primary" id="assign-ticket-submit" type="submit" disabled>Chuyển yêu cầu</button></div>
    </form>
  </div></div>
</div>
<script src="${ctx}/assets/js/manager/queue-actions.js" defer></script>
