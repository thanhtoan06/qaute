<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Giám sát yêu cầu</title>
<div class="qa-page-admin-ticket-list" data-qa-view="admin-ticket-list">
<style>
/* ==========================================================================
   ADMIN TICKET MONITORING - MODERN DESIGN SYSTEM 2026
   ========================================================================== */
.qa-page-admin-ticket-list {
  --adm-primary: #1e50c4;
  --adm-primary-rgb: 30, 80, 196;
  --adm-primary-dark: #0f2d7a;
  --adm-accent-purple: #7c3aed;
  --adm-accent-amber: #f59e0b;
  --adm-accent-rose: #ef4444;
  --adm-accent-emerald: #10b981;
  --adm-card-bg: linear-gradient(180deg, rgba(255, 255, 255, 0.99) 0%, rgba(248, 251, 255, 0.96) 100%);
  --adm-border-soft: rgba(175, 202, 236, 0.45);
  --adm-border-hover: rgba(30, 80, 196, 0.35);
  --adm-shadow-subtle: 0 10px 30px rgba(15, 45, 84, 0.05);
  --adm-shadow-card: 0 18px 40px rgba(15, 45, 84, 0.08);
  --adm-shadow-hover: 0 26px 54px rgba(15, 45, 84, 0.14);
  --adm-text-main: #142f50;
  --adm-text-muted: #5e7696;
  min-width: 0;
  color: var(--adm-text-main);
  padding: clamp(1.25rem, 2.5vw, 2.25rem) 0 4.5rem;
}

.qa-page-admin-ticket-list .container-xl {
  max-width: 1600px;
  padding-right: clamp(1rem, 2.5vw, 2.25rem);
  padding-left: clamp(1rem, 2.5vw, 2.25rem);
}

/* ---------- Unified Hero Banner ---------- */
.qa-page-admin-ticket-list .qa-admin-hero {
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
    radial-gradient(circle at 92% 10%, rgba(124, 58, 237, 0.35), transparent 30%),
    radial-gradient(circle at 5% 95%, rgba(6, 182, 212, 0.28), transparent 36%),
    linear-gradient(135deg, #071733 0%, #133b7e 48%, #581c87 100%);
  box-shadow: 0 24px 60px rgba(12, 43, 85, 0.26), inset 0 1px 0 rgba(255, 255, 255, 0.2);
  animation: qaAdmRise 0.55s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.qa-page-admin-ticket-list .qa-admin-hero::before {
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

.qa-page-admin-ticket-list .qa-admin-hero-content {
  position: relative;
  z-index: 1;
  max-width: 780px;
}

.qa-page-admin-ticket-list .qa-dashboard-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 0.55rem;
  margin-bottom: 0.85rem;
  padding: 0.45rem 1rem;
  border: 1px solid rgba(255, 255, 255, 0.24);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  color: #e2e8ff;
  font-size: 0.74rem;
  font-weight: 850;
  letter-spacing: 0.16em;
  text-transform: uppercase;
  backdrop-filter: blur(12px);
}

.qa-page-admin-ticket-list .qa-pulse-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: #a78bfa;
  box-shadow: 0 0 0 0 rgba(167, 139, 250, 0.7);
  animation: qaAdmPulse 2s infinite;
}

.qa-page-admin-ticket-list .qa-view-title {
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

.qa-page-admin-ticket-list .qa-view-title-icon {
  display: inline-grid;
  flex: 0 0 48px;
  width: 48px;
  height: 48px;
  place-items: center;
  border-radius: 16px;
  color: #12377a;
  background: linear-gradient(145deg, #ffffff, #ede9fe);
  box-shadow: 0 10px 22px rgba(3, 20, 48, 0.26);
  font-size: 1.4rem;
}

.qa-page-admin-ticket-list .qa-admin-hero p {
  margin: 0;
  color: #dbe4ff;
  font-size: 0.98rem;
  line-height: 1.7;
}

.qa-page-admin-ticket-list .qa-admin-hero-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.85rem;
}

/* ---------- Filter Bar Grid ---------- */
.qa-page-admin-ticket-list .qa-filter-card {
  border: 1px solid var(--adm-border-soft);
  border-radius: 26px;
  background: var(--adm-card-bg);
  box-shadow: var(--adm-shadow-subtle);
  padding: 1.35rem 1.45rem;
  margin-bottom: 1.5rem;
}

.qa-page-admin-ticket-list .qa-filter-bar {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(210px, 1fr));
  gap: 1rem;
}

.qa-page-admin-ticket-list .form-label {
  color: #1a385c;
  font-size: 0.79rem;
  font-weight: 800;
  letter-spacing: -0.01em;
  margin-bottom: 0.4rem;
}

.qa-page-admin-ticket-list .form-control,
.qa-page-admin-ticket-list .form-select {
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

.qa-page-admin-ticket-list .form-control:focus,
.qa-page-admin-ticket-list .form-select:focus {
  border-color: #1e50c4;
  box-shadow: 0 0 0 4px rgba(30, 80, 196, 0.12);
}

/* ---------- Table Styling ---------- */
.qa-page-admin-ticket-list .qa-table-card {
  position: relative;
  overflow: hidden;
  border: 1px solid var(--adm-border-soft);
  border-radius: 28px;
  background: var(--adm-card-bg);
  box-shadow: var(--adm-shadow-subtle);
}

.qa-page-admin-ticket-list .qa-table-card .card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  min-height: 74px;
  padding: 1.15rem 1.45rem;
  border-bottom: 1px solid rgba(215, 230, 248, 0.7);
  border-radius: 28px 28px 0 0;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98) 0%, rgba(240, 246, 255, 0.9) 100%);
}

.qa-page-admin-ticket-list .table {
  margin-bottom: 0;
  color: #2b4566;
}

.qa-page-admin-ticket-list .table thead th {
  padding: 1rem 1.25rem;
  border-bottom-color: rgba(215, 230, 248, 0.9);
  color: #647b99;
  font-size: 0.74rem;
  font-weight: 850;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  background: linear-gradient(180deg, #f0f6ff 0%, #f9fcff 100%);
}

.qa-page-admin-ticket-list .table tbody tr {
  border-bottom: 1px solid rgba(234, 242, 252, 0.95);
  transition: background 0.18s ease;
}

.qa-page-admin-ticket-list .table tbody td {
  padding: 1.15rem 1.25rem;
  vertical-align: middle;
}

.qa-page-admin-ticket-list .table tbody tr:hover {
  background: linear-gradient(90deg, rgba(240, 247, 255, 0.95) 0%, rgba(255, 255, 255, 0.98) 100%);
}

.qa-page-admin-ticket-list .qa-code-chip {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  padding: 0.35rem 0.65rem;
  border-radius: 9px;
  background: #edf5ff;
  border: 1px solid rgba(190, 215, 245, 0.7);
  color: #0f4699;
  font-weight: 800;
  font-size: 0.84rem;
  font-family: var(--tblr-font-monospace);
  text-decoration: none;
}

/* ---------- Buttons ---------- */
.qa-page-admin-ticket-list .btn {
  border-radius: 14px;
  font-weight: 750;
  padding: 0.65rem 1.35rem;
  transition: all 0.22s cubic-bezier(0.16, 1, 0.3, 1);
}

.qa-page-admin-ticket-list .btn-sm {
  padding: 0.42rem 0.85rem;
  border-radius: 11px;
  font-size: 0.82rem;
}

.qa-page-admin-ticket-list .btn-primary {
  border-color: #1e50c4;
  background: linear-gradient(135deg, #1e50c4 0%, #10348a 100%);
  box-shadow: 0 10px 22px rgba(30, 80, 196, 0.28);
  color: #ffffff;
}

.qa-page-admin-ticket-list .btn-primary:hover {
  background: linear-gradient(135deg, #2460e6 0%, #123e9e 100%);
  box-shadow: 0 14px 28px rgba(30, 80, 196, 0.38);
  transform: translateY(-2px);
  color: #ffffff;
}

.qa-page-admin-ticket-list .btn-outline-light {
  border-color: rgba(255, 255, 255, 0.38);
  background: rgba(255, 255, 255, 0.12);
  color: #ffffff;
  backdrop-filter: blur(12px);
}

.qa-page-admin-ticket-list .btn-outline-light:hover {
  color: #ffffff;
  border-color: rgba(255, 255, 255, 0.65);
  background: rgba(255, 255, 255, 0.22);
  transform: translateY(-2px);
}

/* ---------- Keyframes ---------- */
@keyframes qaAdmPulse {
  0% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(167, 139, 250, 0.7); }
  70% { transform: scale(1); box-shadow: 0 0 0 7px rgba(167, 139, 250, 0); }
  100% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(167, 139, 250, 0); }
}

@keyframes qaAdmRise {
  from { opacity: 0; transform: translateY(14px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 767.98px) {
  .qa-page-admin-ticket-list .qa-admin-hero {
    flex-direction: column;
    align-items: flex-start;
    padding: 1.45rem;
    border-radius: 24px;
  }
}

@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-ticket-list *,
  .qa-page-admin-ticket-list *::before,
  .qa-page-admin-ticket-list *::after {
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
