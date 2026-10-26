<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>FAQ của tôi</title>
<div class="qa-page-manager-faq-list" data-qa-view="manager-faq-list">
<style>
.qa-page-manager-faq-list { --page-accent: #7456bd; --page-accent-dark: #584091; --page-tint: #f3efff; --page-line: #e1d7fb; min-width: 0; color: #344c67; }
.qa-page-manager-faq-list > .page-header, .qa-page-manager-faq-list > .qa-page-heading, .qa-page-manager-faq-list > .d-flex:has(> .page-title), .qa-page-manager-faq-list > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #f3efff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-manager-faq-list > .page-header::after, .qa-page-manager-faq-list > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #e1d7fb; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-manager-faq-list .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-manager-faq-list .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #e1d7fb; border-radius: 12px; color: #584091; background: linear-gradient(145deg,#fff,#f3efff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-manager-faq-list .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-manager-faq-list .card:hover { border-color: #e1d7fb; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-manager-faq-list .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#f3efff); }
.qa-page-manager-faq-list .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-manager-faq-list .card-body { color: #435b75; }
.qa-page-manager-faq-list .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#f3efff); }
.qa-page-manager-faq-list .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-manager-faq-list .form-control, .qa-page-manager-faq-list .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-manager-faq-list textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-manager-faq-list .form-control:focus, .qa-page-manager-faq-list .form-select:focus { border-color: #7456bd; box-shadow: 0 0 0 4px #f3efff; }
.qa-page-manager-faq-list .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-manager-faq-list .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-manager-faq-list .btn-primary { border-color: #7456bd; background: linear-gradient(120deg,#7456bd,#584091); box-shadow: 0 5px 13px #e1d7fb; }
.qa-page-manager-faq-list .btn-outline-primary { border-color: #e1d7fb; color: #584091; background: #fff; }
.qa-page-manager-faq-list .btn-outline-primary:hover { border-color: #7456bd; color: #fff; background: linear-gradient(120deg,#7456bd,#584091); }
.qa-page-manager-faq-list .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#f3efff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-manager-faq-list .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-manager-faq-list .table { margin-bottom: 0; color: #405872; }
.qa-page-manager-faq-list .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#f3efff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-manager-faq-list .table tbody tr { transition: background-color .18s ease; }
.qa-page-manager-faq-list .table tbody tr:hover { background: #f3efff; }
.qa-page-manager-faq-list .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-manager-faq-list .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-manager-faq-list .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-manager-faq-list .nav-tabs .nav-link.active { color: #584091; box-shadow: inset 0 -2px #7456bd; }
.qa-page-manager-faq-list .nav-pills .nav-link.active { color: #584091; background: #f3efff; box-shadow: inset 0 0 0 1px #e1d7fb; }
.qa-page-manager-faq-list .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-manager-faq-list .progress-bar { border-radius: inherit; }
.qa-page-manager-faq-list .table-responsive { overflow: auto; border: 1px solid #e3ebf5; border-radius: 15px; background: #fff; box-shadow: 0 8px 24px rgba(16,48,84,.05); }
.qa-page-manager-faq-list .table { margin: 0; } .qa-page-manager-faq-list .table thead th { position: sticky; top: 0; z-index: 1; background: linear-gradient(110deg,var(--page-tint),#f7faff); }
.qa-page-manager-faq-list .table tbody tr { transition: background-color .18s ease; } .qa-page-manager-faq-list .table tbody tr:hover { background: var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-manager-faq-list > .page-header, .qa-page-manager-faq-list > .qa-page-heading, .qa-page-manager-faq-list > .d-flex:has(> .page-title), .qa-page-manager-faq-list > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-manager-faq-list .card { border-radius: 15px; }
  .qa-page-manager-faq-list .card-body { padding: 1rem; }
  .qa-page-manager-faq-list .table-responsive { border-radius: 11px; }
  .qa-page-manager-faq-list .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-faq-list *, .qa-page-manager-faq-list *::before, .qa-page-manager-faq-list *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="manager-faq-list"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span>FAQ của tôi</h2>
  <a class="btn btn-primary" href="${ctx}/manager/faq/new">Đề xuất FAQ mới</a>
</div>

<%-- Lọc FAQ theo trạng thái và mở thao tác sửa hoặc xem bài đã xuất bản. --%>
<nav class="nav nav-tabs mb-3" aria-label="Lọc trạng thái FAQ">
  <a class="nav-link ${empty status ? 'active' : ''}" href="${ctx}/manager/faq">Tất cả</a>
  <c:forEach var="state" items="${statuses}"><a class="nav-link ${status eq state ? 'active' : ''}" href="${ctx}/manager/faq?status=${state}">
    <c:choose><c:when test="${state eq 'DRAFT'}">Nháp</c:when><c:when test="${state eq 'PENDING_REVIEW'}">Chờ duyệt</c:when><c:when test="${state eq 'PUBLISHED'}">Đã xuất bản</c:when><c:otherwise>Lưu trữ</c:otherwise></c:choose>
  </a></c:forEach>
</nav>

<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>Tiêu đề</th><th>Chuyên mục</th><th>Trạng thái</th><th>Cập nhật</th><th>Thao tác</th></tr></thead>
  <tbody><c:choose><c:when test="${empty faqs.content}"><tr><td colspan="5" class="text-secondary">Chưa có FAQ ở trạng thái này.</td></tr></c:when><c:otherwise>
    <c:forEach var="item" items="${faqs.content}"><tr>
      <td class="fw-semibold"><c:out value="${item.title()}"/></td>
      <td><c:out value="${item.categoryPath()}"/></td>
      <td><c:choose><c:when test="${item.status() eq 'DRAFT'}"><span class="badge bg-secondary-lt">Nháp</span></c:when><c:when test="${item.status() eq 'PENDING_REVIEW'}"><span class="badge bg-yellow-lt">Chờ duyệt</span></c:when><c:when test="${item.status() eq 'PUBLISHED'}"><span class="badge bg-green-lt">Đã xuất bản</span></c:when><c:otherwise><span class="badge bg-gray-lt">Lưu trữ</span></c:otherwise></c:choose></td>
      <td><c:out value="${item.updatedAt()}"/></td>
      <td><c:choose><c:when test="${item.status() eq 'DRAFT'}"><a class="btn btn-sm btn-outline-primary" href="${ctx}/manager/faq/${item.id()}/edit">Sửa và gửi duyệt</a></c:when><c:when test="${item.status() eq 'PUBLISHED'}"><a class="btn btn-sm btn-outline-primary" href="${ctx}/faq/${fn:escapeXml(item.slug())}">Xem bài</a></c:when><c:otherwise><span class="text-secondary">—</span></c:otherwise></c:choose></td>
    </tr></c:forEach>
  </c:otherwise></c:choose></tbody>
</table></div></div>
<c:set var="paginationParams" value=""/>
<c:if test="${not empty status}"><c:set var="paginationParams" value="status=${status}"/></c:if>
<qa:pagination page="${faqs}" baseUrl="${ctx}/manager/faq" params="${paginationParams}"/>
</div>
