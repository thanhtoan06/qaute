<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Duyệt FAQ</title>
<div class="qa-page-admin-faq-review" data-qa-view="admin-faq-review">
<style>
.qa-page-admin-faq-review { --page-accent: #7456bd; --page-accent-dark: #584091; --page-tint: #f3efff; --page-line: #e1d7fb; min-width: 0; color: #344c67; }
.qa-page-admin-faq-review > .page-header, .qa-page-admin-faq-review > .qa-page-heading, .qa-page-admin-faq-review > .d-flex:has(> .page-title), .qa-page-admin-faq-review > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #f3efff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-faq-review > .page-header::after, .qa-page-admin-faq-review > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #e1d7fb; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-faq-review .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-faq-review .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #e1d7fb; border-radius: 12px; color: #584091; background: linear-gradient(145deg,#fff,#f3efff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-faq-review .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-faq-review .card:hover { border-color: #e1d7fb; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-faq-review .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#f3efff); }
.qa-page-admin-faq-review .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-faq-review .card-body { color: #435b75; }
.qa-page-admin-faq-review .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#f3efff); }
.qa-page-admin-faq-review .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-faq-review .form-control, .qa-page-admin-faq-review .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-faq-review textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-faq-review .form-control:focus, .qa-page-admin-faq-review .form-select:focus { border-color: #7456bd; box-shadow: 0 0 0 4px #f3efff; }
.qa-page-admin-faq-review .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-faq-review .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-faq-review .btn-primary { border-color: #7456bd; background: linear-gradient(120deg,#7456bd,#584091); box-shadow: 0 5px 13px #e1d7fb; }
.qa-page-admin-faq-review .btn-outline-primary { border-color: #e1d7fb; color: #584091; background: #fff; }
.qa-page-admin-faq-review .btn-outline-primary:hover { border-color: #7456bd; color: #fff; background: linear-gradient(120deg,#7456bd,#584091); }
.qa-page-admin-faq-review .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#f3efff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-faq-review .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-faq-review .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-faq-review .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#f3efff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-faq-review .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-faq-review .table tbody tr:hover { background: #f3efff; }
.qa-page-admin-faq-review .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-faq-review .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-faq-review .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-faq-review .nav-tabs .nav-link.active { color: #584091; box-shadow: inset 0 -2px #7456bd; }
.qa-page-admin-faq-review .nav-pills .nav-link.active { color: #584091; background: #f3efff; box-shadow: inset 0 0 0 1px #e1d7fb; }
.qa-page-admin-faq-review .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-faq-review .progress-bar { border-radius: inherit; }

@media (max-width: 767.98px) {
  .qa-page-admin-faq-review > .page-header, .qa-page-admin-faq-review > .qa-page-heading, .qa-page-admin-faq-review > .d-flex:has(> .page-title), .qa-page-admin-faq-review > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-faq-review .card { border-radius: 15px; }
  .qa-page-admin-faq-review .card-body { padding: 1rem; }
  .qa-page-admin-faq-review .table-responsive { border-radius: 11px; }
  .qa-page-admin-faq-review .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-faq-review *, .qa-page-admin-faq-review *::before, .qa-page-admin-faq-review *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và các nút điều hướng -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-faq-review"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span>Xem trước FAQ</h2>
  <div class="d-flex gap-2"><a class="btn btn-outline-primary" href="${ctx}/admin/faq/${faq.id()}/revisions">Phiên bản</a><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Danh sách FAQ</a></div>
</div>

<div class="row g-3">
  <div class="col-lg-8">
    <%-- Xem trước nội dung FAQ và siêu dữ liệu trước khi duyệt. --%>
    <!-- Khu vực xem trước nội dung FAQ -->
    <article class="card card-body">
      <div class="text-secondary small mb-2"><c:out value="${faq.categoryPath()}"/></div>
      <h1 class="h1"><c:out value="${faq.title()}"/></h1>
      <div class="text-secondary small border-bottom pb-3 mb-3">
        Tác giả: <c:out value="${faq.authorName()}"/> · Trạng thái: <c:out value="${faq.status()}"/>
        <c:if test="${not empty faq.publishAt()}"> · Hẹn giờ: <c:out value="${faq.publishAt()}"/></c:if>
      </div>
      <c:if test="${not empty faq.summary()}"><div class="lead text-secondary bg-light rounded p-3 mb-4"><c:out value="${faq.summary()}"/></div></c:if>
      <div class="qa-faq-content lh-lg"><c:out value="${faq.bodyHtml()}" escapeXml="false"/></div>
      <c:if test="${not empty faq.tags()}"><div class="border-top pt-3 mt-4"><c:forEach var="tag" items="${faq.tags()}"><span class="badge bg-blue-lt me-1">#<c:out value="${tag}"/></span></c:forEach></div></c:if>
    </article>
  </div>

  <%-- Duyệt, từ chối, xuất bản hoặc xem lịch sử ghi chú của bài viết. --%>
  <!-- Khu vực quyết định và lịch sử -->
  <div class="col-lg-4">
    <div class="card mb-3"><div class="card-body">
      <h3 class="card-title">Quyết định</h3>
      <!-- Các hành động theo trạng thái FAQ -->
      <c:choose>
        <c:when test="${faq.status() eq 'PENDING_REVIEW'}">
          <!-- Form duyệt và xuất bản -->
          <form method="post" action="${ctx}/admin/faq/${faq.id()}/approve" class="mb-3">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <label class="form-label" for="approve-at">Hẹn giờ xuất bản (để trống để xuất bản ngay)</label>
            <input class="form-control mb-2" id="approve-at" type="datetime-local" name="publishAt"/>
            <button class="btn btn-success w-100" type="submit">Duyệt và xuất bản</button>
          </form>
          <!-- Form từ chối -->
          <form method="post" action="${ctx}/admin/faq/${faq.id()}/reject">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <label class="form-label required" for="reject-reason">Lý do từ chối</label>
            <textarea class="form-control mb-2" id="reject-reason" name="reason" minlength="10" maxlength="500" rows="3" required></textarea>
            <button class="btn btn-outline-danger w-100" type="submit">Từ chối và trả về nháp</button>
          </form>
        </c:when>
        <c:when test="${faq.status() eq 'DRAFT'}">
          <p class="text-secondary">Bài đang là bản nháp.</p>
          <qa:confirmModal id="submit-review-${faq.id()}" title="Gửi duyệt FAQ" message="Gửi bài viết này tới các quản trị viên để duyệt?" actionUrl="${ctx}/admin/faq/${faq.id()}/submit" confirmLabel="Gửi duyệt" cssClass="btn-outline-primary"/>
          <form method="post" action="${ctx}/admin/faq/${faq.id()}/approve" class="mt-3">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="direct" value="true"/>
            <label class="form-label" for="direct-publish-at">Hẹn giờ xuất bản trực tiếp</label>
            <input class="form-control mb-2" id="direct-publish-at" type="datetime-local" name="publishAt"/>
            <button class="btn btn-primary w-100" type="submit">Xuất bản trực tiếp</button>
          </form>
        </c:when>
        <c:when test="${faq.status() eq 'PUBLISHED'}">
          <qa:confirmModal id="archive-review-${faq.id()}" title="Lưu trữ FAQ" message="Lưu trữ bài viết này?" actionUrl="${ctx}/admin/faq/${faq.id()}/archive" confirmLabel="Lưu trữ" requireReason="${true}"/>
        </c:when>
        <c:otherwise><qa:confirmModal id="restore-review-${faq.id()}" title="Khôi phục FAQ" message="Khôi phục bài viết thành bản nháp?" actionUrl="${ctx}/admin/faq/${faq.id()}/restore" confirmLabel="Khôi phục" cssClass="btn-outline-primary"/></c:otherwise>
      </c:choose>
    </div></div>

    <!-- Lịch sử các phiên bản -->
    <div class="card"><div class="card-body">
      <h3 class="card-title">Lịch sử ghi chú</h3>
      <c:choose><c:when test="${empty faq.revisions()}"><p class="text-secondary mb-0">Chưa có phiên bản lưu.</p></c:when>
      <c:otherwise><div class="list-group list-group-flush" data-tv2-pageable"><c:forEach var="revision" items="${faq.revisions()}"><div class="list-group-item px-0"><div class="fw-semibold">Phiên bản ${revision.versionNo()}</div><div class="small text-secondary"><c:out value="${revision.createdAt()}"/> · Người sửa #<c:out value="${revision.changedBy()}"/></div><c:if test="${not empty revision.changeNote()}"><p class="mb-0 mt-1"><c:out value="${revision.changeNote()}"/></p></c:if></div></c:forEach></div></c:otherwise></c:choose>
    </div></div>
  </div>
</div>
</div>
