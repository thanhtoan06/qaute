<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Hàng đợi yêu cầu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/manager/queue.css">
</head>
<div class="qa-page-manager-queue" data-qa-view="manager-queue">


<div class="container-xl">
  <qa:alert/>

  <%-- ==================== HERO BANNER ==================== --%>
  <section class="qa-queue-hero" aria-labelledby="manager-queue-title">
    <div class="qa-queue-hero-content">
      <span class="qa-dashboard-eyebrow">
        <span class="qa-pulse-dot" aria-hidden="true"></span>
        <i class="bi bi-person-workspace me-1" aria-hidden="true"></i>Điều phối tư vấn
      </span>
      <h1 id="manager-queue-title" class="qa-view-title" data-qa-page="manager-queue">
        <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-inboxes-fill"></i></span>
        Hàng đợi yêu cầu hỗ trợ
      </h1>
      <p>Theo dõi, tiếp nhận và điều phối các yêu cầu từ sinh viên trong phạm vi chuyên môn phụ trách với cam kết chất lượng SLA.</p>
    </div>
    <div class="qa-queue-hero-actions">
      <a class="btn btn-outline-light" href="${ctx}/manager/chat">
        <i class="bi bi-chat-dots-fill me-2"></i>Tư vấn trực tuyến
      </a>
    </div>
  </section>

  <%-- ==================== QUEUE TABS ==================== --%>
  <div class="card qa-status-card mb-3">
    <div class="qa-status-tabs" role="tablist" aria-label="Hàng đợi yêu cầu">
      <a class="qa-status-tab-link ${filter.tab eq 'unassigned' ? 'active' : ''}" href="${ctx}/manager/queue?${tabParams}tab=unassigned">
        <i class="bi bi-inbox-fill"></i> Chưa nhận <span class="badge bg-secondary-lt ms-1">${counts.unassigned}</span>
      </a>
      <a class="qa-status-tab-link ${filter.tab eq 'mine' ? 'active' : ''}" href="${ctx}/manager/queue?${tabParams}tab=mine">
        <i class="bi bi-person-workspace"></i> Của tôi <span class="badge bg-secondary-lt ms-1">${counts.mine}</span>
      </a>
      <a class="qa-status-tab-link ${filter.tab eq 'waiting' ? 'active' : ''}" href="${ctx}/manager/queue?${tabParams}tab=waiting">
        <i class="bi bi-hourglass-split"></i> Chờ sinh viên <span class="badge bg-secondary-lt ms-1">${counts.waiting}</span>
      </a>
      <a class="qa-status-tab-link ${filter.tab eq 'slarisk' ? 'active' : ''}" href="${ctx}/manager/queue?${tabParams}tab=slarisk">
        <i class="bi bi-exclamation-triangle-fill"></i> Nguy cơ SLA <span class="badge bg-secondary-lt ms-1">${counts.slarisk}</span>
      </a>
      <a class="qa-status-tab-link ${filter.tab eq 'all' ? 'active' : ''}" href="${ctx}/manager/queue?${tabParams}tab=all">
        <i class="bi bi-grid-fill"></i> Tất cả trong phạm vi <span class="badge bg-secondary-lt ms-1">${counts.all}</span>
      </a>
    </div>

    <c:if test="${filter.tab eq 'mine'}">
      <div class="qa-sub-pills" aria-label="Phân loại yêu cầu của tôi">
        <span class="text-secondary small fw-bold me-2"><i class="bi bi-filter me-1"></i>Trạng thái xử lý:</span>
        <a class="btn btn-sm ${filter.mineStatus eq 'active' ? 'btn-primary' : 'btn-outline-secondary'}" href="${ctx}/manager/queue?${tabParams}tab=mine&amp;mineStatus=active">
          Chưa xử lý <span class="badge bg-secondary-lt ms-1">${counts.mineActive}</span>
        </a>
        <a class="btn btn-sm ${filter.mineStatus eq 'processed' ? 'btn-primary' : 'btn-outline-secondary'}" href="${ctx}/manager/queue?${tabParams}tab=mine&amp;mineStatus=processed">
          Đã xử lý <span class="badge bg-secondary-lt ms-1">${counts.mineProcessed}</span>
        </a>
      </div>
    </c:if>
  </div>

  <%-- ==================== FILTER BAR ==================== --%>
  <form method="get" action="${ctx}/manager/queue" class="qa-filter-bar mb-3">
    <input type="hidden" name="tab" value="<c:out value='${filter.tab}'/>"/>
    <c:if test="${filter.tab eq 'mine'}"><input type="hidden" name="mineStatus" value="<c:out value='${filter.mineStatus}'/>"/></c:if>
    <div>
      <label class="form-label" for="queue-q"><i class="bi bi-search me-1 text-primary"></i>Mã hoặc tiêu đề</label>
      <input class="form-control" id="queue-q" type="search" name="q" maxlength="150" value="<c:out value='${filter.q}'/>" placeholder="Tìm yêu cầu..."/>
    </div>
    <div>
      <label class="form-label" for="queue-category"><i class="bi bi-folder2-open me-1 text-primary"></i>Chuyên mục</label>
      <select class="form-select" id="queue-category" name="categoryId">
        <option value="">Tất cả chuyên mục</option>
        <c:forEach var="category" items="${categories}">
          <option value="${category.key}" ${filter.categoryId eq category.key ? 'selected' : ''}><c:out value="${category.value}"/></option>
        </c:forEach>
      </select>
    </div>
    <div>
      <label class="form-label" for="queue-priority"><i class="bi bi-flag-fill me-1 text-primary"></i>Độ ưu tiên</label>
      <select class="form-select" id="queue-priority" name="priority">
        <option value="">Tất cả mức độ</option>
        <c:forEach var="priority" items="${priorities}">
          <option value="${priority}" ${filter.priority eq priority ? 'selected' : ''}><c:out value="${priority.label}"/></option>
        </c:forEach>
      </select>
    </div>
    <div>
      <label class="form-label" for="queue-status"><i class="bi bi-check2-circle me-1 text-primary"></i>Trạng thái</label>
      <select class="form-select" id="queue-status" name="status">
        <option value="">Tất cả trạng thái</option>
        <c:forEach var="status" items="${statuses}">
          <option value="${status}" ${filter.status eq status ? 'selected' : ''}><c:out value="${status.label}"/></option>
        </c:forEach>
      </select>
    </div>
    <div>
      <label class="form-label" for="queue-urgent"><i class="bi bi-lightning-charge-fill me-1 text-danger"></i>SV đề nghị gấp</label>
      <select class="form-select" id="queue-urgent" name="urgentRequested">
        <option value="">Tất cả</option>
        <option value="true" ${filter.urgentRequested eq true ? 'selected' : ''}>Có đề nghị gấp</option>
        <option value="false" ${filter.urgentRequested eq false ? 'selected' : ''}>Bình thường</option>
      </select>
    </div>
    <div>
      <label class="form-label" for="queue-assignee"><i class="bi bi-person-badge me-1 text-primary"></i>ID người phụ trách</label>
      <input class="form-control" id="queue-assignee" type="number" min="1" name="assigneeId" value="<c:out value='${filter.assigneeId}'/>" placeholder="Nhập ID..."/>
    </div>
    <div>
      <label class="form-label" for="queue-age"><i class="bi bi-clock-history me-1 text-primary"></i>Tuổi tối thiểu (giờ)</label>
      <input class="form-control" id="queue-age" type="number" min="0" max="100000" name="minAgeHours" value="<c:out value='${filter.minAgeHours}'/>" placeholder="Số giờ..."/>
    </div>
    <div>
      <label class="form-label" for="queue-sort"><i class="bi bi-arrow-down-up me-1 text-primary"></i>Sắp xếp</label>
      <select class="form-select" id="queue-sort" name="sort">
        <option value="" ${empty filter.sort ? 'selected' : ''}>Ưu tiên xử lý</option>
        <option value="createdAt" ${filter.sort eq 'createdAt' ? 'selected' : ''}>Tạo mới nhất</option>
        <option value="lastActivityAt" ${filter.sort eq 'lastActivityAt' ? 'selected' : ''}>Hoạt động mới nhất</option>
      </select>
    </div>
    <div class="d-flex align-items-end gap-2">
      <button class="btn btn-primary flex-fill shadow-sm" type="submit">
        <i class="bi bi-filter me-1"></i>Lọc
      </button>
      <a class="btn btn-outline-secondary" href="${ctx}/manager/queue?tab=${filter.tab}&amp;mineStatus=${filter.mineStatus}" title="Bỏ lọc">
        <i class="bi bi-arrow-clockwise"></i>
      </a>
    </div>
  </form>

  <%-- ==================== TABLE RESULTS ==================== --%>
  <div class="card qa-queue-card mb-3">
    <div class="table-responsive">
      <table class="table table-vcenter card-table table-hover">
        <thead>
          <tr>
            <th>Yêu cầu tư vấn</th>
            <th>Sinh viên</th>
            <th>Chuyên mục</th>
            <th>Tình trạng & SLA</th>
            <th>Người phụ trách</th>
            <th>Thời gian</th>
            <th class="text-end">Thao tác</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty tickets.content}">
              <tr>
                <td colspan="7" class="py-5">
                  <qa:emptyState icon="bi-inbox" title="Không có yêu cầu phù hợp" message="Hãy thử đổi điều kiện lọc hoặc chọn tab trạng thái khác."/>
                </td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="item" items="${tickets.content}">
                <tr>
                  <td class="qa-ticket-cell">
                    <span class="qa-ticket-code"><i class="bi bi-hash"></i><c:out value="${item.code()}"/></span>
                    <a class="qa-ticket-title" href="${ctx}/manager/tickets/${item.id()}">
                      <c:out value="${item.title()}"/>
                    </a>
                    <c:if test="${item.urgentRequested()}">
                      <span class="badge bg-red-lt ms-1"><i class="bi bi-lightning-charge-fill me-1"></i>GẤP</span>
                    </c:if>
                  </td>
                  <td>
                    <div class="fw-semibold text-dark"><i class="bi bi-mortarboard me-1 text-primary"></i><c:out value="${item.studentName()}"/></div>
                  </td>
                  <td>
                    <span class="badge bg-secondary-lt"><c:out value="${item.categoryPath()}"/></span>
                  </td>
                  <td>
                    <div class="qa-queue-status">
                      <qa:priorityBadge priority="${item.priority()}"/>
                      <qa:statusBadge status="${item.status()}"/>
                      <qa:slaBadge sla="${item.slaStatus()}"/>
                    </div>
                  </td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty item.assigneeName()}">
                        <span class="fw-semibold text-dark"><c:out value="${item.assigneeName()}"/></span>
                        <c:if test="${item.mine()}">
                          <span class="badge bg-blue-lt ms-1"><i class="bi bi-check me-1"></i>Tôi</span>
                        </c:if>
                      </c:when>
                      <c:otherwise>
                        <span class="text-muted"><i class="bi bi-dash-circle me-1"></i>Chưa nhận</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td>
                    <span class="text-nowrap small text-muted"><i class="bi bi-hourglass me-1"></i>${item.ageHours()}h trước</span>
                    <div class="small text-secondary mt-1">
                      <time datetime="${item.lastActivityAt()}" data-workspace-datetime><c:out value="${item.lastActivityAt()}"/></time>
                    </div>
                  </td>
                  <td class="text-end">
                    <div class="d-flex justify-content-end flex-wrap gap-1">
                      <a class="btn btn-sm btn-outline-secondary" href="${ctx}/manager/tickets/${item.id()}">
                        <i class="bi bi-eye me-1"></i>Mở
                      </a>
                      <c:if test="${item.status() eq 'NEW'}">
                        <form method="post" action="${ctx}/manager/tickets/${item.id()}/claim" class="m-0">
                          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                          <input type="hidden" name="redirect" value="<c:out value='${queueReturn}'/>"/>
                          <button class="btn btn-sm btn-primary shadow-sm" type="submit">
                            <i class="bi bi-hand-index-thumb me-1"></i>Nhận
                          </button>
                        </form>
                      </c:if>
                      <c:if test="${item.status() eq 'NEW' or item.status() eq 'ASSIGNED' or item.status() eq 'IN_PROGRESS' or item.status() eq 'WAITING_STUDENT'}">
                        <button class="btn btn-sm btn-outline-primary js-assign-ticket" type="button"
                                data-bs-toggle="modal" data-bs-target="#assign-ticket-modal"
                                data-ticket-id="${item.id()}"
                                data-advisors-url="${ctx}/manager/tickets/${item.id()}/advisors"
                                data-assign-url="${ctx}/manager/tickets/${item.id()}/assign">
                          <i class="bi bi-arrow-right-circle me-1"></i>Chuyển
                        </button>
                      </c:if>
                      <c:if test="${item.mine() and (item.status() eq 'ASSIGNED' or item.status() eq 'IN_PROGRESS' or item.status() eq 'WAITING_STUDENT')}">
                        <qa:confirmModal id="return-ticket-${item.id()}" title="Trả về hàng đợi" message="Trả yêu cầu này về hàng đợi chưa nhận?" actionUrl="${ctx}/manager/tickets/${item.id()}/return?redirect=${queueReturnEncoded}" confirmLabel="Trả về hàng đợi" cssClass="btn-sm btn-outline-warning" requireReason="${true}"/>
                      </c:if>
                    </div>
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
    <qa:pagination page="${tickets}" baseUrl="${ctx}/manager/queue" params="${paginationParams}"/>
  </div>

  <%-- Assign Modal --%>
  <div class="modal modal-blur fade" id="assign-ticket-modal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content u-rounded-24px u-overflow-hidden u-border-1px-solid-var-mgr-border-soft">
        <div class="modal-header u-bg-linear-gradient-135deg-ffffff-0-f0f7ff-100">
          <h3 class="modal-title fw-bold"><i class="bi bi-person-plus-fill me-2 text-primary"></i>Chuyển yêu cầu <span id="assign-ticket-label"></span></h3>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
        </div>
        <form id="assign-ticket-form" method="post">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <input type="hidden" name="redirect" value="<c:out value='${queueReturn}'/>"/>
          <div class="modal-body p-4">
            <div id="assign-ticket-error" class="alert alert-danger d-none" role="alert"></div>
            <div class="mb-3">
              <label class="form-label required" for="assign-to-manager">Tư vấn viên nhận</label>
              <select class="form-select" id="assign-to-manager" name="toManagerId" required disabled>
                <option value="">Đang tải danh sách…</option>
              </select>
            </div>
            <div>
              <label class="form-label required" for="assign-reason">Lý do chuyển</label>
              <textarea class="form-control" id="assign-reason" name="reason" minlength="10" maxlength="500" rows="3" required placeholder="Nhập lý do chuyển yêu cầu cho chuyên viên khác..."></textarea>
              <small class="form-hint">Từ 10 đến 500 ký tự.</small>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-link link-secondary" data-bs-dismiss="modal">Hủy</button>
            <button class="btn btn-primary shadow-sm" id="assign-ticket-submit" type="submit" disabled>
              <i class="bi bi-check2 me-1"></i>Xác nhận chuyển
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
<script src="${ctx}/assets/js/manager/queue-actions.js" defer></script>
</div>
