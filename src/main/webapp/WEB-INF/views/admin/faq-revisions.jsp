<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Phiên bản FAQ</title>
<div class="qa-page-admin-faq-revisions" data-qa-view="admin-faq-revisions">
<style>
.qa-page-admin-faq-revisions { --page-accent: #7456bd; --page-accent-dark: #584091; --page-tint: #f3efff; --page-line: #e1d7fb; min-width: 0; color: #344c67; }
.qa-page-admin-faq-revisions > .page-header, .qa-page-admin-faq-revisions > .qa-page-heading, .qa-page-admin-faq-revisions > .d-flex:has(> .page-title), .qa-page-admin-faq-revisions > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #f3efff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-faq-revisions > .page-header::after, .qa-page-admin-faq-revisions > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #e1d7fb; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-faq-revisions .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-faq-revisions .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #e1d7fb; border-radius: 12px; color: #584091; background: linear-gradient(145deg,#fff,#f3efff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-faq-revisions .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-faq-revisions .card:hover { border-color: #e1d7fb; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-faq-revisions .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#f3efff); }
.qa-page-admin-faq-revisions .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-faq-revisions .card-body { color: #435b75; }
.qa-page-admin-faq-revisions .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#f3efff); }
.qa-page-admin-faq-revisions .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-faq-revisions .form-control, .qa-page-admin-faq-revisions .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-faq-revisions textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-faq-revisions .form-control:focus, .qa-page-admin-faq-revisions .form-select:focus { border-color: #7456bd; box-shadow: 0 0 0 4px #f3efff; }
.qa-page-admin-faq-revisions .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-faq-revisions .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-faq-revisions .btn-primary { border-color: #7456bd; background: linear-gradient(120deg,#7456bd,#584091); box-shadow: 0 5px 13px #e1d7fb; }
.qa-page-admin-faq-revisions .btn-outline-primary { border-color: #e1d7fb; color: #584091; background: #fff; }
.qa-page-admin-faq-revisions .btn-outline-primary:hover { border-color: #7456bd; color: #fff; background: linear-gradient(120deg,#7456bd,#584091); }
.qa-page-admin-faq-revisions .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#f3efff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-faq-revisions .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-faq-revisions .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-faq-revisions .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#f3efff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-faq-revisions .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-faq-revisions .table tbody tr:hover { background: #f3efff; }
.qa-page-admin-faq-revisions .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-faq-revisions .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-faq-revisions .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-faq-revisions .nav-tabs .nav-link.active { color: #584091; box-shadow: inset 0 -2px #7456bd; }
.qa-page-admin-faq-revisions .nav-pills .nav-link.active { color: #584091; background: #f3efff; box-shadow: inset 0 0 0 1px #e1d7fb; }
.qa-page-admin-faq-revisions .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-faq-revisions .progress-bar { border-radius: inherit; }

@media (max-width: 767.98px) {
  .qa-page-admin-faq-revisions > .page-header, .qa-page-admin-faq-revisions > .qa-page-heading, .qa-page-admin-faq-revisions > .d-flex:has(> .page-title), .qa-page-admin-faq-revisions > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-faq-revisions .card { border-radius: 15px; }
  .qa-page-admin-faq-revisions .card-body { padding: 1rem; }
  .qa-page-admin-faq-revisions .table-responsive { border-radius: 11px; }
  .qa-page-admin-faq-revisions .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-faq-revisions *, .qa-page-admin-faq-revisions *::before, .qa-page-admin-faq-revisions *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và các nút điều hướng -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <div><h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-faq-revisions"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span>Phiên bản FAQ</h2><div class="text-secondary"><c:out value="${faq.title()}"/></div></div>
  <div class="d-flex gap-2"><a class="btn btn-outline-secondary" href="${ctx}/admin/faq/${faq.id()}/review">Xem bài hiện tại</a><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Danh sách FAQ</a></div>
</div>

<%-- Chọn hai phiên bản để so sánh nội dung đã lưu trước đây. --%>
<!-- Form so sánh hai phiên bản -->
<div class="card mb-3"><div class="card-body">
  <form method="get" action="${ctx}/admin/faq/${faq.id()}/revisions/compare" class="row g-2 align-items-end">
    <div class="col-sm-5"><label class="form-label" for="version-a">Phiên bản A</label><input class="form-control" id="version-a" name="a" type="number" min="1" required aria-label="Số phiên bản A"/></div>
    <div class="col-sm-5"><label class="form-label" for="version-b">Phiên bản B</label><input class="form-control" id="version-b" name="b" type="number" min="1" required aria-label="Số phiên bản B"/></div>
    <div class="col-sm-2"><button class="btn btn-outline-primary w-100" type="submit" ${empty history ? 'disabled' : ''}>So sánh</button></div>
  </form>
</div></div>

<!-- Khu vực hiển thị kết quả so sánh -->
<c:if test="${not empty left and not empty right}">
  <div class="row g-3 mb-3">
    <c:forEach var="version" items="${comparison}"><div class="col-lg-6"><article class="card card-body h-100">
      <h3>Phiên bản ${version.versionNo()}</h3><h4><c:out value="${version.title()}"/></h4>
      <c:if test="${not empty version.summary()}"><p class="text-secondary"><c:out value="${version.summary()}"/></p></c:if>
      <div class="qa-faq-content border-top pt-3"><c:out value="${version.bodyHtml()}" escapeXml="false"/></div>
    </article></div></c:forEach>
  </div>
</c:if>

<!-- Khu vực hiển thị phiên bản đã chọn -->
<c:if test="${not empty selected}"><article class="card card-body mb-3">
  <h3>Phiên bản ${selected.versionNo()}</h3><h4><c:out value="${selected.title()}"/></h4>
  <c:if test="${not empty selected.summary()}"><p class="text-secondary"><c:out value="${selected.summary()}"/></p></c:if>
  <div class="qa-faq-content border-top pt-3"><c:out value="${selected.bodyHtml()}" escapeXml="false"/></div>
</article></c:if>

<%-- Liệt kê lịch sử phiên bản và cho phép xem hoặc khôi phục phiên bản. --%>
<!-- Bảng lịch sử các phiên bản -->
<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>Phiên bản</th><th>Người sửa</th><th>Ghi chú</th><th>Thời gian</th><th>Thao tác</th></tr></thead>
  <tbody><c:choose><c:when test="${empty history}"><tr><td colspan="5" class="text-secondary">Chưa có phiên bản.</td></tr></c:when>
  <c:otherwise><c:forEach var="item" items="${history}"><tr>
    <td>#${item.versionNo()}</td><td><c:out value="${item.changedByName()}"/></td><td><c:out value="${item.changeNote()}"/></td><td><c:out value="${item.createdAt()}"/></td>
    <!-- Nút xem và khôi phục phiên bản -->
    <td><div class="d-flex gap-1"><a class="btn btn-sm btn-outline-secondary" href="${ctx}/admin/faq/${faq.id()}/revisions/${item.versionNo()}">Xem</a>
      <c:if test="${faq.status() ne 'ARCHIVED'}"><qa:confirmModal id="restore-revision-${item.versionNo()}" title="Khôi phục phiên bản" message="Tạo phiên bản mới từ phiên bản ${item.versionNo()}? Trạng thái FAQ hiện tại được giữ nguyên." actionUrl="${ctx}/admin/faq/${faq.id()}/revisions/${item.versionNo()}/restore" confirmLabel="Khôi phục" cssClass="btn-sm btn-outline-primary"/></c:if>
    </div></td>
  </tr></c:forEach></c:otherwise></c:choose></tbody>
</table></div></div>

<!-- Phân trang -->
<qa:pagination page="${historyPage}" baseUrl="${ctx}/admin/faq/${faq.id()}/revisions"/>
</div>
