<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Từ đồng nghĩa FAQ</title>
<div class="qa-page-admin-synonyms" data-qa-view="admin-synonyms">
<style>
.qa-page-admin-synonyms { --page-accent: #7456bd; --page-accent-dark: #584091; --page-tint: #f3efff; --page-line: #e1d7fb; min-width: 0; color: #344c67; }
.qa-page-admin-synonyms > .page-header, .qa-page-admin-synonyms > .qa-page-heading, .qa-page-admin-synonyms > .d-flex:has(> .page-title), .qa-page-admin-synonyms > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #f3efff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-synonyms > .page-header::after, .qa-page-admin-synonyms > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #e1d7fb; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-synonyms .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-synonyms .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #e1d7fb; border-radius: 12px; color: #584091; background: linear-gradient(145deg,#fff,#f3efff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-synonyms .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-synonyms .card:hover { border-color: #e1d7fb; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-synonyms .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#f3efff); }
.qa-page-admin-synonyms .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-synonyms .card-body { color: #435b75; }
.qa-page-admin-synonyms .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#f3efff); }
.qa-page-admin-synonyms .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-synonyms .form-control, .qa-page-admin-synonyms .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-synonyms textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-synonyms .form-control:focus, .qa-page-admin-synonyms .form-select:focus { border-color: #7456bd; box-shadow: 0 0 0 4px #f3efff; }
.qa-page-admin-synonyms .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-synonyms .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-synonyms .btn-primary { border-color: #7456bd; background: linear-gradient(120deg,#7456bd,#584091); box-shadow: 0 5px 13px #e1d7fb; }
.qa-page-admin-synonyms .btn-outline-primary { border-color: #e1d7fb; color: #584091; background: #fff; }
.qa-page-admin-synonyms .btn-outline-primary:hover { border-color: #7456bd; color: #fff; background: linear-gradient(120deg,#7456bd,#584091); }
.qa-page-admin-synonyms .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#f3efff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-synonyms .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-synonyms .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-synonyms .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#f3efff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-synonyms .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-synonyms .table tbody tr:hover { background: #f3efff; }
.qa-page-admin-synonyms .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-synonyms .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-synonyms .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-synonyms .nav-tabs .nav-link.active { color: #584091; box-shadow: inset 0 -2px #7456bd; }
.qa-page-admin-synonyms .nav-pills .nav-link.active { color: #584091; background: #f3efff; box-shadow: inset 0 0 0 1px #e1d7fb; }
.qa-page-admin-synonyms .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-synonyms .progress-bar { border-radius: inherit; }

@media (max-width: 767.98px) {
  .qa-page-admin-synonyms > .page-header, .qa-page-admin-synonyms > .qa-page-heading, .qa-page-admin-synonyms > .d-flex:has(> .page-title), .qa-page-admin-synonyms > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-synonyms .card { border-radius: 15px; }
  .qa-page-admin-synonyms .card-body { padding: 1rem; }
  .qa-page-admin-synonyms .table-responsive { border-radius: 11px; }
  .qa-page-admin-synonyms .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-synonyms *, .qa-page-admin-synonyms *::before, .qa-page-admin-synonyms *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- Tiêu đề trang và nút quay lại -->
<div class="d-flex justify-content-between align-items-center mb-3"><h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-synonyms"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-arrow-left-right"></i></span>Từ đồng nghĩa FAQ</h2><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Bài FAQ</a></div>
<!-- Form thêm cặp từ đồng nghĩa -->
<div class="card mb-3"><div class="card-body"><h3 class="card-title">Thêm cặp từ</h3>
  <form method="post" action="${ctx}/admin/faq/synonyms" class="row g-2 align-items-end">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div class="col-md-5"><label class="form-label" for="syn-term">Từ khóa</label><input class="form-control" id="syn-term" name="term" value="<c:out value='${prefillTerm}'/>" minlength="2" maxlength="100" required/></div>
    <div class="col-md-5"><label class="form-label" for="syn-other">Từ đồng nghĩa</label><input class="form-control" id="syn-other" name="synonym" minlength="2" maxlength="100" required/></div>
    <div class="col-md-2"><button class="btn btn-primary w-100" type="submit">Thêm</button></div>
  </form>
</div></div>
<!-- Bảng danh sách từ đồng nghĩa -->
<div class="card mb-3"><div class="table-responsive"><table class="table table-vcenter card-table">
  <thead><tr><th>Từ khóa</th><th>Từ đồng nghĩa</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
  <tbody><c:choose><c:when test="${empty synonyms}"><tr><td colspan="4" class="text-secondary">Chưa có từ đồng nghĩa.</td></tr></c:when>
  <c:otherwise><c:forEach var="item" items="${synonyms}"><tr>
    <td><c:out value="${item.term}"/></td><td><c:out value="${item.synonym}"/></td>
    <td><span class="badge ${item.active ? 'bg-green-lt' : 'bg-secondary-lt'}">${item.active ? 'Bật' : 'Tắt'}</span></td>
    <!-- Nút bật/tắt và xóa -->
    <td><div class="d-flex gap-1"><form method="post" action="${ctx}/admin/faq/synonyms/${item.id}/toggle"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-primary" type="submit">${item.active ? 'Tắt' : 'Bật'}</button></form>
      <qa:confirmModal id="delete-synonym-${item.id}" title="Xóa từ đồng nghĩa" message="Xóa cặp từ này?" actionUrl="${ctx}/admin/faq/synonyms/${item.id}/delete" confirmLabel="Xóa" cssClass="btn-sm btn-outline-danger"/>
    </div></td>
  </tr></c:forEach></c:otherwise></c:choose></tbody>
</table></div></div>
<!-- Bảng truy vấn không có kết quả -->
<div class="card"><div class="card-header"><h3 class="card-title">Truy vấn không có kết quả 30 ngày qua</h3></div>
  <div class="table-responsive"><table class="table table-vcenter card-table"><thead><tr><th>Truy vấn</th><th>Số lần</th><th>Thao tác</th></tr></thead>
    <tbody><c:choose><c:when test="${empty noResultQueries}"><tr><td colspan="3" class="text-secondary">Chưa có truy vấn không có kết quả.</td></tr></c:when>
    <c:otherwise><c:forEach var="query" items="${noResultQueries}"><tr>
      <td><c:out value="${query.query()}"/></td><td>${query.count()}</td>
      <!-- Nút thêm từ đồng nghĩa hoặc soạn FAQ -->
      <td><c:url var="synonymUrl" value="/admin/faq/synonyms"><c:param name="term" value="${query.query()}"/></c:url><c:url var="newFaqUrl" value="/admin/faq/new"><c:param name="title" value="${query.query()}"/></c:url>
        <a class="btn btn-sm btn-outline-primary" href="<c:out value='${synonymUrl}'/>">Thêm từ đồng nghĩa</a>
        <a class="btn btn-sm btn-outline-secondary" href="<c:out value='${newFaqUrl}'/>">Soạn FAQ</a>
      </td>
    </tr></c:forEach></c:otherwise></c:choose></tbody>
  </table></div>
</div>

<!-- Phân trang -->
<qa:pagination page="${resultPage}" baseUrl="${ctx}/admin/faq/synonyms"/>
</div>
