<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Yêu cầu của tôi</title>
<div class="qa-page-student-ticket-list" data-qa-view="student-ticket-list">
<style>
/* ==========================================================================
   STUDENT TICKET LIST - MODERN DESIGN SYSTEM 2026
   ========================================================================== */
.qa-page-student-ticket-list {
  --stu-primary: #1d72f2;
  --stu-primary-rgb: 29, 114, 242;
  --stu-primary-dark: #0f4699;
  --stu-accent-cyan: #06b6d4;
  --stu-accent-amber: #f59e0b;
  --stu-accent-emerald: #10b981;
  --stu-accent-purple: #8b5cf6;
  --stu-card-bg: linear-gradient(180deg, rgba(255, 255, 255, 0.99) 0%, rgba(248, 251, 255, 0.96) 100%);
  --stu-border-soft: rgba(186, 210, 240, 0.45);
  --stu-border-hover: rgba(29, 114, 242, 0.35);
  --stu-shadow-subtle: 0 10px 30px rgba(15, 45, 84, 0.05);
  --stu-shadow-card: 0 18px 40px rgba(15, 45, 84, 0.08);
  --stu-shadow-hover: 0 26px 54px rgba(15, 45, 84, 0.14);
  --stu-text-main: #18385c;
  --stu-text-muted: #5e7696;
  min-width: 0;
  color: var(--stu-text-main);
  padding: clamp(1.25rem, 2.5vw, 2.25rem) 0 4.5rem;
}

.qa-page-student-ticket-list .container-xl {
  max-width: 1560px;
  padding-right: clamp(1rem, 2.5vw, 2.25rem);
  padding-left: clamp(1rem, 2.5vw, 2.25rem);
}

/* ---------- Unified Hero Banner ---------- */
.qa-page-student-ticket-list .qa-ticket-hero {
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
    radial-gradient(circle at 92% 10%, rgba(56, 189, 248, 0.32), transparent 30%),
    radial-gradient(circle at 5% 95%, rgba(129, 140, 248, 0.28), transparent 36%),
    linear-gradient(135deg, #091e3e 0%, #124991 48%, #087d92 100%);
  box-shadow: 0 24px 60px rgba(12, 43, 85, 0.26), inset 0 1px 0 rgba(255, 255, 255, 0.2);
  animation: qaStuRise 0.55s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.qa-page-student-ticket-list .qa-ticket-hero::before {
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

.qa-page-student-ticket-list .qa-ticket-hero-content {
  position: relative;
  z-index: 1;
  max-width: 780px;
}

.qa-page-student-ticket-list .qa-dashboard-eyebrow {
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

.qa-page-student-ticket-list .qa-pulse-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: #38bdf8;
  box-shadow: 0 0 0 0 rgba(56, 189, 248, 0.7);
  animation: qaStuPulse 2s infinite;
}

.qa-page-student-ticket-list .qa-view-title {
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

.qa-page-student-ticket-list .qa-view-title-icon {
  display: inline-grid;
  flex: 0 0 48px;
  width: 48px;
  height: 48px;
  place-items: center;
  border-radius: 16px;
  color: #0d4b8f;
  background: linear-gradient(145deg, #ffffff, #e0f2fe);
  box-shadow: 0 10px 22px rgba(3, 20, 48, 0.26);
  font-size: 1.4rem;
}

.qa-page-student-ticket-list .qa-ticket-hero p {
  margin: 0;
  color: #d7eaff;
  font-size: 0.98rem;
  line-height: 1.7;
}

.qa-page-student-ticket-list .qa-ticket-hero-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.85rem;
}

/* ---------- Status Tabs Styling ---------- */
.qa-page-student-ticket-list .qa-status-card {
  border: 1px solid var(--stu-border-soft);
  border-radius: 24px;
  background: var(--stu-card-bg);
  box-shadow: var(--stu-shadow-subtle);
  overflow: hidden;
  margin-bottom: 1.45rem;
}

.qa-page-student-ticket-list .qa-status-tabs {
  display: flex;
  flex-wrap: nowrap;
  overflow-x: auto;
  gap: 0.45rem;
  padding: 0.65rem 0.85rem;
  border-bottom: 0;
  scrollbar-width: thin;
}

.qa-page-student-ticket-list .tab-status-link {
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
}

.qa-page-student-ticket-list .tab-status-link:hover {
  background: rgba(29, 114, 242, 0.08);
  color: #1d72f2;
}

.qa-page-student-ticket-list .tab-status-link.active {
  background: linear-gradient(135deg, #1d72f2 0%, #0d54b8 100%) !important;
  color: #ffffff !important;
  box-shadow: 0 8px 18px rgba(29, 114, 242, 0.28);
}

.qa-page-student-ticket-list .tab-status-link .badge {
  font-size: 0.72rem;
  font-weight: 800;
  padding: 0.3rem 0.55rem;
  border-radius: 999px;
}

.qa-page-student-ticket-list .tab-status-link.active .badge {
  background: rgba(255, 255, 255, 0.25) !important;
  color: #ffffff !important;
}

/* ---------- Search & Filter Card ---------- */
.qa-page-student-ticket-list .qa-filter-card {
  border: 1px solid var(--stu-border-soft);
  border-radius: 26px;
  background: var(--stu-card-bg);
  box-shadow: var(--stu-shadow-subtle);
  padding: 1.35rem 1.45rem;
  margin-bottom: 1.5rem;
}

.qa-page-student-ticket-list .form-label {
  color: #1a385c;
  font-size: 0.8rem;
  font-weight: 800;
  letter-spacing: -0.01em;
  margin-bottom: 0.4rem;
}

.qa-page-student-ticket-list .form-control,
.qa-page-student-ticket-list .form-select {
  min-height: 44px;
  border: 1px solid rgba(188, 212, 242, 0.7);
  border-radius: 14px;
  color: #16365b;
  font-size: 0.9rem;
  font-weight: 600;
  background-color: #ffffff;
  box-shadow: 0 2px 6px rgba(15, 45, 84, 0.02);
  transition: all 0.2s ease;
  padding: 0.58rem 0.95rem;
}

.qa-page-student-ticket-list .form-control:focus,
.qa-page-student-ticket-list .form-select:focus {
  border-color: #1d72f2;
  box-shadow: 0 0 0 4px rgba(29, 114, 242, 0.12);
}

/* ---------- Results Table Card ---------- */
.qa-page-student-ticket-list .qa-results-card {
  position: relative;
  overflow: hidden;
  border: 1px solid var(--stu-border-soft);
  border-radius: 28px;
  background: var(--stu-card-bg);
  box-shadow: var(--stu-shadow-subtle);
  transition: transform 0.24s ease, box-shadow 0.24s ease;
}

.qa-page-student-ticket-list .qa-results-card .card-header {
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

.qa-page-student-ticket-list .table {
  margin-bottom: 0;
  color: #2b4566;
}

.qa-page-student-ticket-list .table thead th {
  padding: 1rem 1.25rem;
  border-bottom-color: rgba(215, 230, 248, 0.9);
  color: #647b99;
  font-size: 0.74rem;
  font-weight: 850;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  background: linear-gradient(180deg, #f0f6ff 0%, #f9fcff 100%);
}

.qa-page-student-ticket-list .table tbody tr {
  border-bottom: 1px solid rgba(234, 242, 252, 0.95);
  transition: background 0.18s ease;
}

.qa-page-student-ticket-list .table tbody td {
  padding: 1.15rem 1.25rem;
  vertical-align: middle;
}

.qa-page-student-ticket-list .table tbody tr:hover {
  background: linear-gradient(90deg, rgba(240, 247, 255, 0.95) 0%, rgba(255, 255, 255, 0.98) 100%);
}

.qa-page-student-ticket-list .qa-code-chip {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  padding: 0.35rem 0.7rem;
  border-radius: 10px;
  background: #edf5ff;
  border: 1px solid rgba(190, 215, 245, 0.7);
  color: #0f4699;
  font-weight: 800;
  text-decoration: none;
  font-family: var(--tblr-font-monospace);
  transition: all 0.2s ease;
}

.qa-page-student-ticket-list .qa-code-chip:hover {
  background: #1d72f2;
  color: #ffffff;
  border-color: #1d72f2;
  box-shadow: 0 4px 12px rgba(29, 114, 242, 0.25);
}

.qa-page-student-ticket-list .qa-ticket-title-link {
  color: #14355c;
  font-weight: 750;
  font-size: 0.98rem;
  text-decoration: none;
  transition: color 0.2s ease;
}

.qa-page-student-ticket-list .qa-ticket-title-link:hover {
  color: #1d72f2;
}

/* ---------- Mobile Cards ---------- */
@media (max-width: 767.98px) {
  .qa-page-student-ticket-list .table-responsive-card table { display: none; }
  .qa-page-student-ticket-list .ticket-cards { display: block; }
  .qa-page-student-ticket-list .qa-ticket-hero {
    flex-direction: column;
    align-items: flex-start;
    padding: 1.45rem;
    border-radius: 24px;
  }
}

@media (min-width: 768px) {
  .qa-page-student-ticket-list .ticket-cards { display: none; }
}

.qa-page-student-ticket-list .ticket-card-item {
  border: 1px solid rgba(220, 233, 248, 0.95);
  border-radius: 20px;
  background: linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
  padding: 1.15rem;
  margin-bottom: 0.85rem;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.qa-page-student-ticket-list .ticket-card-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 24px rgba(15, 45, 84, 0.08);
}

/* ---------- Buttons ---------- */
.qa-page-student-ticket-list .btn {
  border-radius: 14px;
  font-weight: 750;
  padding: 0.65rem 1.35rem;
  transition: all 0.22s cubic-bezier(0.16, 1, 0.3, 1);
}

.qa-page-student-ticket-list .btn-primary {
  border-color: #1d72f2;
  background: linear-gradient(135deg, #1d72f2 0%, #0d54b8 100%);
  box-shadow: 0 10px 22px rgba(29, 114, 242, 0.28);
  color: #ffffff;
}

.qa-page-student-ticket-list .btn-primary:hover {
  background: linear-gradient(135deg, #257dfd 0%, #0e5dc9 100%);
  box-shadow: 0 14px 28px rgba(29, 114, 242, 0.38);
  transform: translateY(-2px);
  color: #ffffff;
}

.qa-page-student-ticket-list .btn-white {
  color: #0b3874;
  border: 1px solid rgba(255, 255, 255, 0.9);
  background: linear-gradient(180deg, #ffffff 0%, #eaf3fe 100%);
  box-shadow: 0 10px 24px rgba(4, 21, 48, 0.22);
}

.qa-page-student-ticket-list .btn-white:hover {
  color: #082d5e;
  background: #ffffff;
  transform: translateY(-2px);
}

.qa-page-student-ticket-list .btn-outline-light {
  border-color: rgba(255, 255, 255, 0.38);
  background: rgba(255, 255, 255, 0.12);
  color: #ffffff;
  backdrop-filter: blur(12px);
}

.qa-page-student-ticket-list .btn-outline-light:hover {
  color: #ffffff;
  border-color: rgba(255, 255, 255, 0.65);
  background: rgba(255, 255, 255, 0.22);
  transform: translateY(-2px);
}

/* ---------- Keyframes ---------- */
@keyframes qaStuPulse {
  0% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(56, 189, 248, 0.7); }
  70% { transform: scale(1); box-shadow: 0 0 0 7px rgba(56, 189, 248, 0); }
  100% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(56, 189, 248, 0); }
}

@keyframes qaStuRise {
  from { opacity: 0; transform: translateY(14px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (prefers-reduced-motion: reduce) {
  .qa-page-student-ticket-list *,
  .qa-page-student-ticket-list *::before,
  .qa-page-student-ticket-list *::after {
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
                <select class="form-select form-select-sm" style="width:auto"
                        onchange="location.href='${ctx}/student/tickets?status=${filter.status}&q=${fn:escapeXml(filter.q)}&categoryId=${filter.categoryId}&from=${filter.from}&to=${filter.to}&sort='+this.value">
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
</div>
