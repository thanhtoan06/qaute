<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Hàng đợi yêu cầu</title>
<div class="qa-page-manager-queue" data-qa-view="manager-queue">
<style>
/* ==========================================================================
   MANAGER QUEUE - MODERN DESIGN SYSTEM 2026
   ========================================================================== */
.qa-page-manager-queue {
  --mgr-primary: #1d60d8;
  --mgr-primary-rgb: 29, 96, 216;
  --mgr-primary-dark: #0f3d8a;
  --mgr-teal: #0d9488;
  --mgr-accent-purple: #8b5cf6;
  --mgr-accent-amber: #f59e0b;
  --mgr-accent-rose: #ef4444;
  --mgr-card-bg: linear-gradient(180deg, rgba(255, 255, 255, 0.99) 0%, rgba(248, 251, 255, 0.96) 100%);
  --mgr-border-soft: rgba(180, 206, 238, 0.45);
  --mgr-border-hover: rgba(29, 96, 216, 0.35);
  --mgr-shadow-subtle: 0 10px 30px rgba(15, 45, 84, 0.05);
  --mgr-shadow-card: 0 18px 40px rgba(15, 45, 84, 0.08);
  --mgr-shadow-hover: 0 26px 54px rgba(15, 45, 84, 0.14);
  --mgr-text-main: #163456;
  --mgr-text-muted: #5e7696;
  min-width: 0;
  color: var(--mgr-text-main);
  padding: clamp(1.25rem, 2.5vw, 2.25rem) 0 4.5rem;
}

.qa-page-manager-queue .container-xl {
  max-width: 1600px;
  padding-right: clamp(1rem, 2.5vw, 2.25rem);
  padding-left: clamp(1rem, 2.5vw, 2.25rem);
}

/* ---------- Unified Hero Banner ---------- */
.qa-page-manager-queue .qa-queue-hero {
  position: relative;
  isolation: isolate;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1.5rem;
  margin-bottom: 1.75rem;
  padding: clamp(1.6rem, 2.8vw, 2.4rem);
  border: 1px solid rgba(255, 255, 255, 0.32);
  border-radius: 30px;
  color: #ffffff;
  background:
    radial-gradient(circle at 92% 10%, rgba(13, 148, 136, 0.32), transparent 30%),
    radial-gradient(circle at 5% 95%, rgba(139, 92, 246, 0.28), transparent 36%),
    linear-gradient(135deg, #0a1b38 0%, #12458f 48%, #0f766e 100%);
  box-shadow: 0 24px 60px rgba(12, 43, 85, 0.26), inset 0 1px 0 rgba(255, 255, 255, 0.2);
  animation: qaMgrRise 0.55s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.qa-page-manager-queue .qa-queue-hero::before {
  content: "";
  position: absolute;
  z-index: -1;
  top: -120px;
  right: -40px;
  width: 320px;
  height: 320px;
  border-radius: 50%;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 0 0 24px rgba(255, 255, 255, 0.03), 0 0 0 60px rgba(255, 255, 255, 0.015);
  pointer-events: none;
}

.qa-page-manager-queue .qa-queue-hero-content {
  position: relative;
  z-index: 1;
  max-width: 780px;
}

.qa-page-manager-queue .qa-dashboard-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 0.55rem;
  margin-bottom: 0.85rem;
  padding: 0.45rem 1rem;
  border: 1px solid rgba(255, 255, 255, 0.24);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  color: #d8eeff;
  font-size: 0.74rem;
  font-weight: 850;
  letter-spacing: 0.16em;
  text-transform: uppercase;
  backdrop-filter: blur(12px);
}

.qa-page-manager-queue .qa-pulse-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: #2dd4bf;
  box-shadow: 0 0 0 0 rgba(45, 212, 191, 0.7);
  animation: qaMgrPulse 2s infinite;
}

.qa-page-manager-queue .qa-view-title {
  display: inline-flex;
  align-items: center;
  gap: 0.85rem;
  margin: 0 0 0.65rem;
  color: #ffffff;
  font-size: clamp(1.75rem, 2.8vw, 2.5rem);
  font-weight: 850;
  letter-spacing: -0.04em;
  line-height: 1.2;
}

.qa-page-manager-queue .qa-view-title-icon {
  display: inline-grid;
  flex: 0 0 48px;
  width: 48px;
  height: 48px;
  place-items: center;
  border-radius: 16px;
  color: #0c4384;
  background: linear-gradient(145deg, #ffffff, #dff6f5);
  box-shadow: 0 10px 22px rgba(3, 20, 48, 0.26);
  font-size: 1.4rem;
}

.qa-page-manager-queue .qa-queue-hero p {
  margin: 0;
  color: #d7eaff;
  font-size: 0.98rem;
  line-height: 1.7;
}

.qa-page-manager-queue .qa-queue-hero-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.85rem;
}

/* ---------- Status Tabs Styling ---------- */
.qa-page-manager-queue .qa-status-card {
  border: 1px solid var(--mgr-border-soft);
  border-radius: 24px;
  background: var(--mgr-card-bg);
  box-shadow: var(--mgr-shadow-subtle);
  overflow: hidden;
  margin-bottom: 1.45rem;
}

.qa-page-manager-queue .qa-status-tabs {
  display: flex;
  flex-wrap: nowrap;
  overflow-x: auto;
  gap: 0.45rem;
  padding: 0.65rem 0.85rem;
  border-bottom: 0;
  scrollbar-width: thin;
}

.qa-page-manager-queue .qa-status-tab-link {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.62rem 1.15rem;
  border-radius: 16px !important;
  border: 1px solid transparent !important;
  color: #486689;
  font-size: 0.85rem;
  font-weight: 750;
  white-space: nowrap;
  transition: all 0.22s ease;
  background: transparent;
  text-decoration: none;
}

.qa-page-manager-queue .qa-status-tab-link:hover {
  background: rgba(29, 96, 216, 0.08);
  color: #1d60d8;
}

.qa-page-manager-queue .qa-status-tab-link.active {
  background: linear-gradient(135deg, #1d60d8 0%, #0d428d 100%) !important;
  color: #ffffff !important;
  box-shadow: 0 8px 18px rgba(29, 96, 216, 0.28);
}

.qa-page-manager-queue .qa-status-tab-link .badge {
  font-size: 0.72rem;
  font-weight: 800;
  padding: 0.3rem 0.55rem;
  border-radius: 999px;
}

.qa-page-manager-queue .qa-status-tab-link.active .badge {
  background: rgba(255, 255, 255, 0.25) !important;
  color: #ffffff !important;
}

.qa-page-manager-queue .qa-sub-pills {
  display: flex;
  align-items: center;
  gap: 0.55rem;
  padding: 0.6rem 0.85rem;
  border-top: 1px solid rgba(220, 233, 248, 0.7);
  background: rgba(245, 250, 255, 0.7);
}

/* ---------- Filter Bar Grid ---------- */
.qa-page-manager-queue .qa-filter-bar {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1rem;
  padding: 1.35rem 1.45rem;
  border: 1px solid var(--mgr-border-soft);
  border-radius: 26px;
  background: var(--mgr-card-bg);
  box-shadow: var(--mgr-shadow-subtle);
  margin-bottom: 1.5rem;
}

.qa-page-manager-queue .form-label {
  color: #1a385c;
  font-size: 0.79rem;
  font-weight: 800;
  letter-spacing: -0.01em;
  margin-bottom: 0.4rem;
}

.qa-page-manager-queue .form-control,
.qa-page-manager-queue .form-select {
  min-height: 44px;
  border: 1px solid rgba(188, 212, 242, 0.7);
  border-radius: 14px;
  color: #16365b;
  font-size: 0.88rem;
  font-weight: 600;
  background-color: #ffffff;
  box-shadow: 0 2px 6px rgba(15, 45, 84, 0.02);
  transition: all 0.2s ease;
  padding: 0.55rem 0.9rem;
}

.qa-page-manager-queue .form-control:focus,
.qa-page-manager-queue .form-select:focus {
  border-color: #1d60d8;
  box-shadow: 0 0 0 4px rgba(29, 96, 216, 0.12);
}

/* ---------- Table Styling ---------- */
.qa-page-manager-queue .qa-queue-card {
  position: relative;
  overflow: hidden;
  border: 1px solid var(--mgr-border-soft);
  border-radius: 28px;
  background: var(--mgr-card-bg);
  box-shadow: var(--mgr-shadow-subtle);
}

.qa-page-manager-queue .table {
  margin-bottom: 0;
  color: #2b4566;
}

.qa-page-manager-queue .table thead th {
  padding: 1rem 1.25rem;
  border-bottom-color: rgba(215, 230, 248, 0.9);
  color: #647b99;
  font-size: 0.74rem;
  font-weight: 850;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  background: linear-gradient(180deg, #f0f6ff 0%, #f9fcff 100%);
}

.qa-page-manager-queue .table tbody tr {
  border-bottom: 1px solid rgba(234, 242, 252, 0.95);
  transition: background 0.18s ease;
}

.qa-page-manager-queue .table tbody td {
  padding: 1.15rem 1.25rem;
  vertical-align: middle;
}

.qa-page-manager-queue .table tbody tr:hover {
  background: linear-gradient(90deg, rgba(240, 247, 255, 0.95) 0%, rgba(255, 255, 255, 0.98) 100%);
}

.qa-page-manager-queue .qa-ticket-cell {
  min-width: 280px;
}

.qa-page-manager-queue .qa-ticket-code {
  display: inline-block;
  padding: 0.25rem 0.6rem;
  border-radius: 8px;
  background: #edf5ff;
  border: 1px solid rgba(190, 215, 245, 0.7);
  color: #0f4699;
  font-weight: 800;
  font-size: 0.8rem;
  font-family: var(--tblr-font-monospace);
  margin-right: 0.5rem;
}

.qa-page-manager-queue .qa-ticket-title {
  color: #14355c;
  font-weight: 750;
  font-size: 0.96rem;
  text-decoration: none;
  transition: color 0.2s ease;
}

.qa-page-manager-queue .qa-ticket-title:hover {
  color: #1d60d8;
}

.qa-page-manager-queue .qa-queue-status {
  display: flex;
  flex-wrap: wrap;
  gap: 0.35rem;
}

/* ---------- Buttons ---------- */
.qa-page-manager-queue .btn {
  border-radius: 14px;
  font-weight: 750;
  padding: 0.65rem 1.35rem;
  transition: all 0.22s cubic-bezier(0.16, 1, 0.3, 1);
}

.qa-page-manager-queue .btn-sm {
  padding: 0.42rem 0.85rem;
  border-radius: 11px;
  font-size: 0.82rem;
}

.qa-page-manager-queue .btn-primary {
  border-color: #1d60d8;
  background: linear-gradient(135deg, #1d60d8 0%, #0d428d 100%);
  box-shadow: 0 10px 22px rgba(29, 96, 216, 0.28);
  color: #ffffff;
}

.qa-page-manager-queue .btn-primary:hover {
  background: linear-gradient(135deg, #246ff0 0%, #0f4bb0 100%);
  box-shadow: 0 14px 28px rgba(29, 96, 216, 0.38);
  transform: translateY(-2px);
  color: #ffffff;
}

.qa-page-manager-queue .btn-outline-light {
  border-color: rgba(255, 255, 255, 0.38);
  background: rgba(255, 255, 255, 0.12);
  color: #ffffff;
  backdrop-filter: blur(12px);
}

.qa-page-manager-queue .btn-outline-light:hover {
  color: #ffffff;
  border-color: rgba(255, 255, 255, 0.65);
  background: rgba(255, 255, 255, 0.22);
  transform: translateY(-2px);
}

/* ---------- Keyframes ---------- */
@keyframes qaMgrPulse {
  0% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(45, 212, 191, 0.7); }
  70% { transform: scale(1); box-shadow: 0 0 0 7px rgba(45, 212, 191, 0); }
  100% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(45, 212, 191, 0); }
}

@keyframes qaMgrRise {
  from { opacity: 0; transform: translateY(14px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 767.98px) {
  .qa-page-manager-queue .qa-queue-hero {
    flex-direction: column;
    align-items: flex-start;
    padding: 1.45rem;
    border-radius: 24px;
  }
}

@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-queue *,
  .qa-page-manager-queue *::before,
  .qa-page-manager-queue *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
    scroll-behavior: auto !important;
  }
}
</style>

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
      <div class="modal-content" style="border-radius: 24px; overflow: hidden; border: 1px solid var(--mgr-border-soft);">
        <div class="modal-header" style="background: linear-gradient(135deg, #ffffff 0%, #f0f7ff 100%);">
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
