<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Thẻ FAQ</title>
<div class="qa-page-admin-tags" data-qa-view="admin-tags">
<style>
.qa-page-admin-tags { --page-accent: #7456bd; --page-accent-dark: #584091; --page-tint: #f3efff; --page-line: #e1d7fb; min-width: 0; color: #344c67; }
.qa-page-admin-tags > .page-header, .qa-page-admin-tags > .qa-page-heading, .qa-page-admin-tags > .d-flex:has(> .page-title), .qa-page-admin-tags > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #f3efff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-tags > .page-header::after, .qa-page-admin-tags > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #e1d7fb; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-tags .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-tags .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #e1d7fb; border-radius: 12px; color: #584091; background: linear-gradient(145deg,#fff,#f3efff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-tags .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-tags .card:hover { border-color: #e1d7fb; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-tags .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#f3efff); }
.qa-page-admin-tags .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-tags .card-body { color: #435b75; }
.qa-page-admin-tags .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#f3efff); }
.qa-page-admin-tags .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-tags .form-control, .qa-page-admin-tags .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-tags textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-tags .form-control:focus, .qa-page-admin-tags .form-select:focus { border-color: #7456bd; box-shadow: 0 0 0 4px #f3efff; }
.qa-page-admin-tags .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-tags .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-tags .btn-primary { border-color: #7456bd; background: linear-gradient(120deg,#7456bd,#584091); box-shadow: 0 5px 13px #e1d7fb; }
.qa-page-admin-tags .btn-outline-primary { border-color: #e1d7fb; color: #584091; background: #fff; }
.qa-page-admin-tags .btn-outline-primary:hover { border-color: #7456bd; color: #fff; background: linear-gradient(120deg,#7456bd,#584091); }
.qa-page-admin-tags .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#f3efff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-tags .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-tags .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-tags .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#f3efff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-tags .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-tags .table tbody tr:hover { background: #f3efff; }
.qa-page-admin-tags .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-tags .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-tags .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-tags .nav-tabs .nav-link.active { color: #584091; box-shadow: inset 0 -2px #7456bd; }
.qa-page-admin-tags .nav-pills .nav-link.active { color: #584091; background: #f3efff; box-shadow: inset 0 0 0 1px #e1d7fb; }
.qa-page-admin-tags .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-tags .progress-bar { border-radius: inherit; }

@media (max-width: 767.98px) {
  .qa-page-admin-tags > .page-header, .qa-page-admin-tags > .qa-page-heading, .qa-page-admin-tags > .d-flex:has(> .page-title), .qa-page-admin-tags > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-tags .card { border-radius: 15px; }
  .qa-page-admin-tags .card-body { padding: 1rem; }
  .qa-page-admin-tags .table-responsive { border-radius: 11px; }
  .qa-page-admin-tags .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-tags *, .qa-page-admin-tags *::before, .qa-page-admin-tags *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- Tiêu đề trang và nút quay lại -->
<div class="d-flex justify-content-between align-items-center mb-3"><h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-tags"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-tags"></i></span>Thẻ FAQ</h2><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Bài FAQ</a></div>
<!-- Form thêm thẻ mới -->
<div class="card mb-3"><div class="card-body"><h3 class="card-title">Thêm thẻ</h3>
  <form method="post" action="${ctx}/admin/faq/tags" class="d-flex flex-wrap gap-2">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input class="form-control" style="max-width: 24rem" name="name" minlength="2" maxlength="40" placeholder="Tên thẻ" required/>
    <button class="btn btn-primary" type="submit">Thêm</button>
  </form>
</div></div>
<!-- Bảng danh sách thẻ -->
<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>ID / Tên thẻ</th><th>Slug</th><th>Số FAQ dùng</th><th>Đổi tên</th><th>Gộp vào thẻ khác</th><th>Xóa</th></tr></thead>
  <tbody><c:choose><c:when test="${empty tags}"><tr><td colspan="6" class="text-secondary">Chưa có thẻ.</td></tr></c:when>
  <c:otherwise><c:forEach var="tag" items="${tags}"><tr>
    <td>#${tag.id()} · <c:out value="${tag.name()}"/></td><td><code><c:out value="${tag.slug()}"/></code></td><td>${tag.faqCount()}</td>
    <!-- Form đổi tên thẻ -->
    <td><form method="post" action="${ctx}/admin/faq/tags/${tag.id()}/rename" class="d-flex gap-1">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input class="form-control form-control-sm" name="name" value="<c:out value='${tag.name()}'/>" minlength="2" maxlength="40" required aria-label="Tên mới"/>
      <button class="btn btn-sm btn-outline-primary" type="submit">Lưu</button>
    </form></td>
    <!-- Form gộp thẻ -->
    <td><form method="post" action="${ctx}/admin/faq/tags/${tag.id()}/merge" class="d-flex gap-1">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input class="form-control form-control-sm" type="number" name="targetId" min="1" required aria-label="ID thẻ đích" placeholder="ID thẻ đích"/>
      <button class="btn btn-sm btn-outline-warning" type="submit" onclick="return confirm('Gộp mọi FAQ sang thẻ đích và xóa thẻ nguồn?')">Gộp</button>
    </form></td>
    <!-- Nút xóa thẻ (chỉ khi không FAQ nào dùng) -->
    <td><c:if test="${tag.faqCount() eq 0}"><qa:confirmModal id="delete-tag-${tag.id()}" title="Xóa thẻ" message="Xóa thẻ này?" actionUrl="${ctx}/admin/faq/tags/${tag.id()}/delete" confirmLabel="Xóa" cssClass="btn-sm btn-outline-danger"/></c:if></td>
  </tr></c:forEach></c:otherwise></c:choose></tbody>
</table></div></div>

<!-- Phân trang -->
<qa:pagination page="${resultPage}" baseUrl="${ctx}/admin/faq/tags"/>
</div>
