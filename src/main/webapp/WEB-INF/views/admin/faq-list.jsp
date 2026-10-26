<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Quản lý FAQ</title>
<div class="qa-page-admin-faq-list" data-qa-view="admin-faq-list">
<style>
.qa-page-admin-faq-list { --page-accent: #7456bd; --page-accent-dark: #584091; --page-tint: #f3efff; --page-line: #e1d7fb; min-width: 0; color: #344c67; }
.qa-page-admin-faq-list > .page-header, .qa-page-admin-faq-list > .qa-page-heading, .qa-page-admin-faq-list > .d-flex:has(> .page-title), .qa-page-admin-faq-list > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #f3efff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-faq-list > .page-header::after, .qa-page-admin-faq-list > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #e1d7fb; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-faq-list .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-faq-list .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #e1d7fb; border-radius: 12px; color: #584091; background: linear-gradient(145deg,#fff,#f3efff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-faq-list .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-faq-list .card:hover { border-color: #e1d7fb; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-faq-list .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#f3efff); }
.qa-page-admin-faq-list .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-faq-list .card-body { color: #435b75; }
.qa-page-admin-faq-list .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#f3efff); }
.qa-page-admin-faq-list .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-faq-list .form-control, .qa-page-admin-faq-list .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-faq-list textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-faq-list .form-control:focus, .qa-page-admin-faq-list .form-select:focus { border-color: #7456bd; box-shadow: 0 0 0 4px #f3efff; }
.qa-page-admin-faq-list .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-faq-list .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-faq-list .btn-primary { border-color: #7456bd; background: linear-gradient(120deg,#7456bd,#584091); box-shadow: 0 5px 13px #e1d7fb; }
.qa-page-admin-faq-list .btn-outline-primary { border-color: #e1d7fb; color: #584091; background: #fff; }
.qa-page-admin-faq-list .btn-outline-primary:hover { border-color: #7456bd; color: #fff; background: linear-gradient(120deg,#7456bd,#584091); }
.qa-page-admin-faq-list .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#f3efff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-faq-list .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-faq-list .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-faq-list .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#f3efff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-faq-list .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-faq-list .table tbody tr:hover { background: #f3efff; }
.qa-page-admin-faq-list .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-faq-list .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-faq-list .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-faq-list .nav-tabs .nav-link.active { color: #584091; box-shadow: inset 0 -2px #7456bd; }
.qa-page-admin-faq-list .nav-pills .nav-link.active { color: #584091; background: #f3efff; box-shadow: inset 0 0 0 1px #e1d7fb; }
.qa-page-admin-faq-list .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-faq-list .progress-bar { border-radius: inherit; }
.qa-page-admin-faq-list .table-responsive { overflow: auto; border: 1px solid #e3ebf5; border-radius: 15px; background: #fff; box-shadow: 0 8px 24px rgba(16,48,84,.05); }
.qa-page-admin-faq-list .table { margin: 0; } .qa-page-admin-faq-list .table thead th { position: sticky; top: 0; z-index: 1; background: linear-gradient(110deg,var(--page-tint),#f7faff); }
.qa-page-admin-faq-list .table tbody tr { transition: background-color .18s ease; } .qa-page-admin-faq-list .table tbody tr:hover { background: var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-admin-faq-list > .page-header, .qa-page-admin-faq-list > .qa-page-heading, .qa-page-admin-faq-list > .d-flex:has(> .page-title), .qa-page-admin-faq-list > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-faq-list .card { border-radius: 15px; }
  .qa-page-admin-faq-list .card-body { padding: 1rem; }
  .qa-page-admin-faq-list .table-responsive { border-radius: 11px; }
  .qa-page-admin-faq-list .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-faq-list *, .qa-page-admin-faq-list *::before, .qa-page-admin-faq-list *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và nút tạo FAQ mới -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-faq-list"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span>Quản lý FAQ</h2>
  <a class="btn btn-primary" href="${ctx}/admin/faq/new"><i class="bi bi-plus-lg me-1"></i>Soạn bài mới</a>
</div>

<%-- Lọc danh sách FAQ theo trạng thái và các thuộc tính quản trị. --%>
<!-- Tab lọc theo trạng thái FAQ -->
<div class="nav nav-tabs mb-3" role="tablist" aria-label="Trạng thái FAQ">
  <a class="nav-link ${empty filter.status ? 'active' : ''}" href="${ctx}/admin/faq?${tabParams}page=0">Tất cả <span class="badge bg-secondary-lt ms-1">${totalCount}</span></a>
  <c:forEach var="status" items="${statuses}">
    <a class="nav-link ${filter.status eq status ? 'active' : ''}" href="${ctx}/admin/faq?${tabParams}status=${status}&amp;page=0">
      <c:choose><c:when test="${status eq 'DRAFT'}">Nháp</c:when><c:when test="${status eq 'PENDING_REVIEW'}">Chờ duyệt</c:when><c:when test="${status eq 'PUBLISHED'}">Đã xuất bản</c:when><c:otherwise>Lưu trữ</c:otherwise></c:choose>
      <span class="badge bg-secondary-lt ms-1">${counts[status.name()]}</span>
    </a>
  </c:forEach>
</div>

<!-- Form lọc FAQ -->
<form method="get" action="${ctx}/admin/faq" class="qa-filter-bar mb-3">
  <c:if test="${not empty filter.status}"><input type="hidden" name="status" value="${filter.status}"/></c:if>
  <div><label class="form-label" for="faqSearch">Tìm kiếm</label><input class="form-control" id="faqSearch" name="q" type="search" maxlength="200" value="<c:out value='${filter.q}'/>" placeholder="Tiêu đề hoặc slug"/></div>
  <div><label class="form-label" for="faqCategory">Chuyên mục</label><select class="form-select" id="faqCategory" name="categoryId"><option value="">Tất cả chuyên mục</option><c:forEach var="option" items="${categoryOptions}"><option value="${option.id()}" ${filter.categoryId eq option.id() ? 'selected' : ''}><c:out value="${option.label()}"/></option></c:forEach></select></div>
  <div><label class="form-label" for="faqAuthor">Tác giả</label><select class="form-select" id="faqAuthor" name="authorId"><option value="">Tất cả tác giả</option><c:forEach var="author" items="${authors}"><option value="${author.key}" ${filter.authorId eq author.key ? 'selected' : ''}><c:out value="${author.value}"/></option></c:forEach></select></div>
  <div><label class="form-label" for="faqFeatured">Nổi bật</label><select class="form-select" id="faqFeatured" name="featured"><option value="">Tất cả</option><option value="true" ${filter.featured eq true ? 'selected' : ''}>Có</option><option value="false" ${filter.featured eq false ? 'selected' : ''}>Không</option></select></div>
  <div><label class="form-label" for="faqSort">Sắp xếp</label><select class="form-select" id="faqSort" name="sort"><option value="updatedAt" ${empty filter.sort or filter.sort eq 'updatedAt' ? 'selected' : ''}>Cập nhật mới nhất</option><option value="viewCount" ${filter.sort eq 'viewCount' ? 'selected' : ''}>Xem nhiều nhất</option><option value="helpfulCount" ${filter.sort eq 'helpfulCount' ? 'selected' : ''}>Hữu ích nhiều nhất</option><option value="title" ${filter.sort eq 'title' ? 'selected' : ''}>Tiêu đề A–Z</option></select></div>
  <div class="d-flex gap-2"><button class="btn btn-primary" type="submit">Lọc</button><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Bỏ lọc</a></div>
</form>

<%-- Hiển thị bài viết và các thao tác sửa, duyệt, lưu trữ hoặc khôi phục. --%>
<!-- Bảng danh sách FAQ -->
<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table qa-admin-table">
  <thead><tr><th>Tiêu đề</th><th>Chuyên mục</th><th>Tác giả</th><th>Trạng thái</th><th>Lượt xem</th><th>Hữu ích</th><th>Cập nhật</th><th>Thao tác</th></tr></thead>
  <tbody>
    <c:choose><c:when test="${empty faqs.content}"><tr><td colspan="8"><qa:emptyState icon="bi-journal-text" title="Không có FAQ phù hợp" message="Hãy thử thay đổi điều kiện lọc."/></td></tr></c:when>
    <c:otherwise><c:forEach var="item" items="${faqs.content}"><tr>
      <!-- Tiêu đề, slug và badge nổi bật -->
      <td><div class="fw-semibold"><c:choose><c:when test="${item.status() eq 'ARCHIVED'}"><c:out value="${item.title()}"/></c:when><c:otherwise><a href="${ctx}/admin/faq/${item.id()}/edit"><c:out value="${item.title()}"/></a></c:otherwise></c:choose></div><div class="small text-secondary"><c:out value="${item.slug()}"/></div><c:if test="${item.featured()}"><span class="badge bg-yellow-lt mt-1">Nổi bật</span></c:if></td>
      <td><c:out value="${item.categoryPath()}"/></td>
      <td><c:out value="${item.authorName()}"/></td>
      <td><c:choose><c:when test="${item.status() eq 'DRAFT'}"><span class="badge bg-secondary-lt">Nháp</span></c:when><c:when test="${item.status() eq 'PENDING_REVIEW'}"><span class="badge bg-yellow-lt">Chờ duyệt</span></c:when><c:when test="${item.status() eq 'PUBLISHED'}"><span class="badge bg-green-lt">Đã xuất bản</span></c:when><c:otherwise><span class="badge bg-gray-lt">Lưu trữ</span></c:otherwise></c:choose></td>
      <td><c:out value="${item.viewCount()}"/></td>
      <td><c:set var="voteCount" value="${item.helpfulCount() + item.notHelpfulCount()}"/><c:choose><c:when test="${voteCount gt 0}"><fmt:formatNumber value="${100.0 * item.helpfulCount() / voteCount}" maxFractionDigits="0"/>% <span class="small text-secondary">(${voteCount})</span></c:when><c:otherwise><span class="text-secondary">—</span></c:otherwise></c:choose></td>
      <td><c:out value="${item.updatedAt()}"/></td>
      <!-- Các nút thao tác theo trạng thái -->
      <td><div class="d-flex flex-wrap gap-1">
        <c:if test="${item.status() ne 'ARCHIVED'}"><a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/faq/${item.id()}/edit">Sửa</a></c:if>
        <a class="btn btn-sm btn-outline-secondary" href="${ctx}/admin/faq/${item.id()}/revisions">Phiên bản</a>
        <c:choose>
          <c:when test="${item.status() eq 'DRAFT'}">
            <a class="btn btn-sm btn-outline-secondary" href="${ctx}/admin/faq/${item.id()}/review">Xem trước</a>
            <qa:confirmModal id="submit-faq-${item.id()}" title="Gửi duyệt FAQ" message="Gửi bài viết này tới quản trị viên để duyệt?" actionUrl="${ctx}/admin/faq/${item.id()}/submit" confirmLabel="Gửi duyệt" cssClass="btn-sm btn-outline-primary" requirePersonalInfoConfirmation="${not empty item.sourceTicketId()}"/>
          </c:when>
          <c:when test="${item.status() eq 'PENDING_REVIEW'}"><a class="btn btn-sm btn-success" href="${ctx}/admin/faq/${item.id()}/review">Duyệt</a></c:when>
          <c:when test="${item.status() eq 'PUBLISHED'}">
            <qa:confirmModal id="archive-faq-${item.id()}" title="Lưu trữ FAQ" message="Lưu trữ bài viết này?" actionUrl="${ctx}/admin/faq/${item.id()}/archive" confirmLabel="Lưu trữ" cssClass="btn-sm btn-outline-danger" requireReason="${true}"/>
            <qa:confirmModal id="featured-faq-${item.id()}" title="Bài nổi bật" message="Thay đổi trạng thái nổi bật của bài viết?" actionUrl="${ctx}/admin/faq/${item.id()}/featured" confirmLabel="${item.featured() ? 'Bỏ ghim' : 'Ghim nổi bật'}" cssClass="btn-sm btn-outline-warning"/>
          </c:when>
          <c:otherwise><qa:confirmModal id="restore-faq-${item.id()}" title="Khôi phục FAQ" message="Khôi phục bài viết thành bản nháp?" actionUrl="${ctx}/admin/faq/${item.id()}/restore" confirmLabel="Khôi phục" cssClass="btn-sm btn-outline-primary"/></c:otherwise>
        </c:choose>
      </div></td>
    </tr></c:forEach></c:otherwise></c:choose>
  </tbody>
</table></div></div>

<!-- Phân trang -->
<qa:pagination page="${faqs}" baseUrl="${ctx}/admin/faq" params="${paginationParams}"/>
</div>
