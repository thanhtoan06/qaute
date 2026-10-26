<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>${pageTitle} - QAUTE</title>
<div class="qa-page-admin-guest-home-faq-form" data-qa-view="admin-guest-home-faq-form">
<style>
.qa-page-admin-guest-home-faq-form { --page-accent: #7456bd; --page-accent-dark: #584091; --page-tint: #f3efff; --page-line: #e1d7fb; min-width: 0; color: #344c67; }
.qa-page-admin-guest-home-faq-form > .page-header, .qa-page-admin-guest-home-faq-form > .qa-page-heading, .qa-page-admin-guest-home-faq-form > .d-flex:has(> .page-title), .qa-page-admin-guest-home-faq-form > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #f3efff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-guest-home-faq-form > .page-header::after, .qa-page-admin-guest-home-faq-form > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #e1d7fb; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-guest-home-faq-form .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-guest-home-faq-form .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #e1d7fb; border-radius: 12px; color: #584091; background: linear-gradient(145deg,#fff,#f3efff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-guest-home-faq-form .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-guest-home-faq-form .card:hover { border-color: #e1d7fb; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-guest-home-faq-form .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#f3efff); }
.qa-page-admin-guest-home-faq-form .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-guest-home-faq-form .card-body { color: #435b75; }
.qa-page-admin-guest-home-faq-form .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#f3efff); }
.qa-page-admin-guest-home-faq-form .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-guest-home-faq-form .form-control, .qa-page-admin-guest-home-faq-form .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-guest-home-faq-form textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-guest-home-faq-form .form-control:focus, .qa-page-admin-guest-home-faq-form .form-select:focus { border-color: #7456bd; box-shadow: 0 0 0 4px #f3efff; }
.qa-page-admin-guest-home-faq-form .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-guest-home-faq-form .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-guest-home-faq-form .btn-primary { border-color: #7456bd; background: linear-gradient(120deg,#7456bd,#584091); box-shadow: 0 5px 13px #e1d7fb; }
.qa-page-admin-guest-home-faq-form .btn-outline-primary { border-color: #e1d7fb; color: #584091; background: #fff; }
.qa-page-admin-guest-home-faq-form .btn-outline-primary:hover { border-color: #7456bd; color: #fff; background: linear-gradient(120deg,#7456bd,#584091); }
.qa-page-admin-guest-home-faq-form .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#f3efff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-guest-home-faq-form .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-guest-home-faq-form .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-guest-home-faq-form .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#f3efff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-guest-home-faq-form .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-guest-home-faq-form .table tbody tr:hover { background: #f3efff; }
.qa-page-admin-guest-home-faq-form .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-guest-home-faq-form .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-guest-home-faq-form .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-guest-home-faq-form .nav-tabs .nav-link.active { color: #584091; box-shadow: inset 0 -2px #7456bd; }
.qa-page-admin-guest-home-faq-form .nav-pills .nav-link.active { color: #584091; background: #f3efff; box-shadow: inset 0 0 0 1px #e1d7fb; }
.qa-page-admin-guest-home-faq-form .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-guest-home-faq-form .progress-bar { border-radius: inherit; }
.qa-page-admin-guest-home-faq-form form:not(.qa-filter-bar) { --form-accent: var(--page-accent); }
.qa-page-admin-guest-home-faq-form form:not(.qa-filter-bar) > .card, .qa-page-admin-guest-home-faq-form form:not(.qa-filter-bar) .card { border-radius: 20px; }
.qa-page-admin-guest-home-faq-form form:not(.qa-filter-bar) .form-control:focus, .qa-page-admin-guest-home-faq-form form:not(.qa-filter-bar) .form-select:focus { border-color: var(--page-accent); box-shadow: 0 0 0 4px var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-admin-guest-home-faq-form > .page-header, .qa-page-admin-guest-home-faq-form > .qa-page-heading, .qa-page-admin-guest-home-faq-form > .d-flex:has(> .page-title), .qa-page-admin-guest-home-faq-form > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-guest-home-faq-form .card { border-radius: 15px; }
  .qa-page-admin-guest-home-faq-form .card-body { padding: 1rem; }
  .qa-page-admin-guest-home-faq-form .table-responsive { border-radius: 11px; }
  .qa-page-admin-guest-home-faq-form .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-guest-home-faq-form *, .qa-page-admin-guest-home-faq-form *::before, .qa-page-admin-guest-home-faq-form *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0 qa-view-title" data-qa-page="admin-guest-home-faq-form"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span>${pageTitle}</h2>
    <a href="/admin/guest-home-faq" class="btn btn-outline-secondary">
        <i class="bi bi-arrow-left me-1"></i>Quay lại
    </a>
</div>

<div class="card shadow-sm">
    <div class="card-body">
        <c:if test="${not empty errors}">
            <div class="alert alert-danger">
                <c:forEach var="error" items="${errors}">
                    <div>${error.value}</div>
                </c:forEach>
            </div>
        </c:if>

        <form action="/admin/guest-home-faq/save" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <c:if test="${form.id != null}">
                <input type="hidden" name="id" value="${form.id}">
            </c:if>

            <div class="mb-3">
                <label for="categoryName" class="form-label">Danh mục <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="categoryName" name="categoryName"
                       value="${form.categoryName}" required placeholder="Ví dụ: Học vụ, Học phí, Đăng ký môn...">
                <div class="form-text">Tên danh mục hiển thị trên trang chủ guest</div>
            </div>

            <div class="mb-3">
                <label for="question" class="form-label">Câu hỏi <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="question" name="question"
                       value="${form.question}" required placeholder="Nhập câu hỏi..." maxlength="300">
            </div>

            <div class="mb-3">
                <label for="answer" class="form-label">Câu trả lời <span class="text-danger">*</span></label>
                <textarea class="form-control" id="answer" name="answer" rows="5"
                          required placeholder="Nhập câu trả lời (có thể dùng HTML)">${form.answer}</textarea>
                <div class="form-text">Có thể sử dụng HTML cơ bản để định dạng nội dung</div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="displayOrder" class="form-label">Thứ tự hiển thị <span class="text-danger">*</span></label>
                    <input type="number" class="form-control" id="displayOrder" name="displayOrder"
                           value="${form.displayOrder}" required min="1">
                    <div class="form-text">Số càng nhỏ càng hiển thị ở trên</div>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label d-block">Trạng thái</label>
                    <div class="form-check mt-2">
                        <input class="form-check-input" type="checkbox" id="isActive" name="isActive"
                               ${form.active ? 'checked' : ''}>
                        <label class="form-check-label" for="isActive">
                            Hiển thị trên trang chủ
                        </label>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end gap-2">
                <a href="/admin/guest-home-faq" class="btn btn-outline-secondary">Hủy</a>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-lg me-1"></i>Lưu FAQ
                </button>
            </div>
        </form>
    </div>
</div>
</div>
