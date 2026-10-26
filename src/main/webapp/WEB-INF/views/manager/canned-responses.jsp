<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Mẫu trả lời nhanh</title>
<div class="qa-page-manager-canned-responses" data-qa-view="manager-canned-responses">
<style>
.qa-page-manager-canned-responses { --page-accent: #cf7b27; --page-accent-dark: #98571b; --page-tint: #fff4e8; --page-line: #f5dfca; min-width: 0; color: #344c67; }
.qa-page-manager-canned-responses > .page-header, .qa-page-manager-canned-responses > .qa-page-heading, .qa-page-manager-canned-responses > .d-flex:has(> .page-title), .qa-page-manager-canned-responses > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #fff4e8, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-manager-canned-responses > .page-header::after, .qa-page-manager-canned-responses > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #f5dfca; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-manager-canned-responses .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-manager-canned-responses .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #f5dfca; border-radius: 12px; color: #98571b; background: linear-gradient(145deg,#fff,#fff4e8); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-manager-canned-responses .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-manager-canned-responses .card:hover { border-color: #f5dfca; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-manager-canned-responses .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#fff4e8); }
.qa-page-manager-canned-responses .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-manager-canned-responses .card-body { color: #435b75; }
.qa-page-manager-canned-responses .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#fff4e8); }
.qa-page-manager-canned-responses .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-manager-canned-responses .form-control, .qa-page-manager-canned-responses .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-manager-canned-responses textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-manager-canned-responses .form-control:focus, .qa-page-manager-canned-responses .form-select:focus { border-color: #cf7b27; box-shadow: 0 0 0 4px #fff4e8; }
.qa-page-manager-canned-responses .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-manager-canned-responses .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-manager-canned-responses .btn-primary { border-color: #cf7b27; background: linear-gradient(120deg,#cf7b27,#98571b); box-shadow: 0 5px 13px #f5dfca; }
.qa-page-manager-canned-responses .btn-outline-primary { border-color: #f5dfca; color: #98571b; background: #fff; }
.qa-page-manager-canned-responses .btn-outline-primary:hover { border-color: #cf7b27; color: #fff; background: linear-gradient(120deg,#cf7b27,#98571b); }
.qa-page-manager-canned-responses .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#fff4e8); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-manager-canned-responses .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-manager-canned-responses .table { margin-bottom: 0; color: #405872; }
.qa-page-manager-canned-responses .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#fff4e8,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-manager-canned-responses .table tbody tr { transition: background-color .18s ease; }
.qa-page-manager-canned-responses .table tbody tr:hover { background: #fff4e8; }
.qa-page-manager-canned-responses .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-manager-canned-responses .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-manager-canned-responses .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-manager-canned-responses .nav-tabs .nav-link.active { color: #98571b; box-shadow: inset 0 -2px #cf7b27; }
.qa-page-manager-canned-responses .nav-pills .nav-link.active { color: #98571b; background: #fff4e8; box-shadow: inset 0 0 0 1px #f5dfca; }
.qa-page-manager-canned-responses .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-manager-canned-responses .progress-bar { border-radius: inherit; }
.qa-page-manager-canned-responses .table-responsive { overflow: auto; border: 1px solid #e3ebf5; border-radius: 15px; background: #fff; box-shadow: 0 8px 24px rgba(16,48,84,.05); }
.qa-page-manager-canned-responses .table { margin: 0; } .qa-page-manager-canned-responses .table thead th { position: sticky; top: 0; z-index: 1; background: linear-gradient(110deg,var(--page-tint),#f7faff); }
.qa-page-manager-canned-responses .table tbody tr { transition: background-color .18s ease; } .qa-page-manager-canned-responses .table tbody tr:hover { background: var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-manager-canned-responses > .page-header, .qa-page-manager-canned-responses > .qa-page-heading, .qa-page-manager-canned-responses > .d-flex:has(> .page-title), .qa-page-manager-canned-responses > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-manager-canned-responses .card { border-radius: 15px; }
  .qa-page-manager-canned-responses .card-body { padding: 1rem; }
  .qa-page-manager-canned-responses .table-responsive { border-radius: 11px; }
  .qa-page-manager-canned-responses .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-canned-responses *, .qa-page-manager-canned-responses *::before, .qa-page-manager-canned-responses *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.css"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="manager-canned-responses"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-lightning-charge"></i></span>Mẫu trả lời nhanh</h2>
  <a class="btn btn-outline-primary" href="${ctx}/manager/canned-responses">Soạn mẫu mới</a>
</div>

<%-- Liệt kê các mẫu trả lời hiện có và biểu mẫu tạo hoặc cập nhật mẫu. --%>
<div class="row g-3">
  <div class="col-12 col-xl-7"><div class="card"><div class="card-header"><h3 class="card-title">Mẫu có thể sử dụng</h3></div><div class="table-responsive"><table class="table table-vcenter card-table">
    <thead><tr><th>Tiêu đề</th><th>Phạm vi</th><th>Chuyên mục</th><th>Trạng thái</th><th>Lượt dùng</th><th>Thao tác</th></tr></thead>
    <tbody><c:choose><c:when test="${empty items}"><tr><td colspan="6" class="text-secondary">Chưa có mẫu trả lời.</td></tr></c:when><c:otherwise><c:forEach var="item" items="${items}"><tr>
      <td class="fw-semibold"><c:out value="${item.title()}"/></td>
      <td>${item.scope() eq 'PERSONAL' ? 'Cá nhân' : 'Dùng chung'}</td>
      <td><c:choose><c:when test="${empty item.categoryPath()}">Tất cả chuyên mục</c:when><c:otherwise><c:out value="${item.categoryPath()}"/></c:otherwise></c:choose></td>
      <td><span class="badge ${item.active() ? 'bg-green-lt' : 'bg-secondary-lt'}">${item.active() ? 'Đang dùng' : 'Đã tắt'}</span></td>
      <td>${item.usageCount()}</td>
      <td><c:if test="${item.mine()}"><div class="d-flex gap-1"><a class="btn btn-sm btn-outline-primary" href="${ctx}/manager/canned-responses?edit=${item.id()}">Sửa</a><form method="post" action="${ctx}/manager/canned-responses/${item.id()}/delete"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/><button class="btn btn-sm btn-outline-danger" type="submit" onclick="return confirm('Xóa mẫu trả lời này?')">Xóa</button></form></div></c:if></td>
    </tr></c:forEach></c:otherwise></c:choose></tbody>
  </table></div><div class="card-body"><qa:pagination page="${resultPage}" baseUrl="${ctx}/manager/canned-responses"/></div></div></div>

  <div class="col-12 col-xl-5"><div class="card"><div class="card-header"><h3 class="card-title">${empty form.id ? 'Tạo mẫu mới' : 'Sửa mẫu trả lời'}</h3></div><div class="card-body">
    <form method="post" action="${ctx}/manager/canned-responses" id="canned-response-form" novalidate>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input type="hidden" name="id" value="<c:out value='${form.id}'/>"/>
      <div class="mb-3"><label class="form-label required" for="canned-title">Tiêu đề</label><input class="form-control" id="canned-title" name="title" type="text" minlength="2" maxlength="120" value="<c:out value='${form.title}'/>" required/></div>
      <div class="mb-3"><label class="form-label required" for="canned-scope">Phạm vi</label><select class="form-select" id="canned-scope" name="scope" required><option value="PERSONAL" ${form.scope eq 'PERSONAL' ? 'selected' : ''}>Cá nhân</option><option value="SHARED" ${form.scope eq 'SHARED' ? 'selected' : ''}>Dùng chung</option></select></div>
      <div class="mb-3" id="canned-category-group"><label class="form-label required" for="canned-category">Chuyên mục</label><select class="form-select" id="canned-category" name="categoryId"><option value="">Chọn chuyên mục trong phạm vi</option><c:forEach var="option" items="${categoryOptions}"><option value="${option.key}" ${form.categoryId eq option.key ? 'selected' : ''}><c:out value="${option.value}"/></option></c:forEach></select><div class="form-hint">Mẫu dùng chung chỉ hiển thị cho Manager được giao chuyên mục này.</div></div>
      <div class="mb-3"><label class="form-label required" for="canned-body">Nội dung mẫu</label><textarea class="form-control" id="canned-body" name="bodyHtml" rows="9" maxlength="10000" required><c:out value="${form.bodyHtml}"/></textarea><div class="form-hint">Biến có thể dùng: <code>{{student_name}}</code>, <code>{{ticket_code}}</code>, <code>{{advisor_name}}</code>, <code>{{category}}</code>.</div><div id="canned-body-error" class="text-danger small" role="alert"></div></div>
      <label class="form-check form-switch mb-3"><input type="hidden" name="_active" value="on"/><input class="form-check-input" name="active" type="checkbox" value="true" ${form.active ? 'checked' : ''}/><span class="form-check-label">Đang dùng</span></label>
      <button class="btn btn-primary" type="submit">Lưu mẫu</button>
    </form>
  </div></div></div>
</div>

<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js" defer></script>
<script src="${ctx}/assets/js/manager/canned-responses.js" defer></script>
</div>
