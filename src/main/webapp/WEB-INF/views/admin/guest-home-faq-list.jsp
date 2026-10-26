<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Quản lý FAQ trang chủ - QAUTE</title>
<div class="qa-page-admin-guest-home-faq-list" data-qa-view="admin-guest-home-faq-list">
<style>
.qa-page-admin-guest-home-faq-list { --page-accent: #7456bd; --page-accent-dark: #584091; --page-tint: #f3efff; --page-line: #e1d7fb; min-width: 0; color: #344c67; }
.qa-page-admin-guest-home-faq-list > .page-header, .qa-page-admin-guest-home-faq-list > .qa-page-heading, .qa-page-admin-guest-home-faq-list > .d-flex:has(> .page-title), .qa-page-admin-guest-home-faq-list > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #f3efff, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-guest-home-faq-list > .page-header::after, .qa-page-admin-guest-home-faq-list > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #e1d7fb; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-guest-home-faq-list .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-guest-home-faq-list .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #e1d7fb; border-radius: 12px; color: #584091; background: linear-gradient(145deg,#fff,#f3efff); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-guest-home-faq-list .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-guest-home-faq-list .card:hover { border-color: #e1d7fb; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-guest-home-faq-list .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#f3efff); }
.qa-page-admin-guest-home-faq-list .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-guest-home-faq-list .card-body { color: #435b75; }
.qa-page-admin-guest-home-faq-list .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#f3efff); }
.qa-page-admin-guest-home-faq-list .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-guest-home-faq-list .form-control, .qa-page-admin-guest-home-faq-list .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-guest-home-faq-list textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-guest-home-faq-list .form-control:focus, .qa-page-admin-guest-home-faq-list .form-select:focus { border-color: #7456bd; box-shadow: 0 0 0 4px #f3efff; }
.qa-page-admin-guest-home-faq-list .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-guest-home-faq-list .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-guest-home-faq-list .btn-primary { border-color: #7456bd; background: linear-gradient(120deg,#7456bd,#584091); box-shadow: 0 5px 13px #e1d7fb; }
.qa-page-admin-guest-home-faq-list .btn-outline-primary { border-color: #e1d7fb; color: #584091; background: #fff; }
.qa-page-admin-guest-home-faq-list .btn-outline-primary:hover { border-color: #7456bd; color: #fff; background: linear-gradient(120deg,#7456bd,#584091); }
.qa-page-admin-guest-home-faq-list .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#f3efff); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-guest-home-faq-list .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-guest-home-faq-list .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-guest-home-faq-list .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#f3efff,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-guest-home-faq-list .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-guest-home-faq-list .table tbody tr:hover { background: #f3efff; }
.qa-page-admin-guest-home-faq-list .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-guest-home-faq-list .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-guest-home-faq-list .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-guest-home-faq-list .nav-tabs .nav-link.active { color: #584091; box-shadow: inset 0 -2px #7456bd; }
.qa-page-admin-guest-home-faq-list .nav-pills .nav-link.active { color: #584091; background: #f3efff; box-shadow: inset 0 0 0 1px #e1d7fb; }
.qa-page-admin-guest-home-faq-list .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-guest-home-faq-list .progress-bar { border-radius: inherit; }
.qa-page-admin-guest-home-faq-list .table-responsive { overflow: auto; border: 1px solid #e3ebf5; border-radius: 15px; background: #fff; box-shadow: 0 8px 24px rgba(16,48,84,.05); }
.qa-page-admin-guest-home-faq-list .table { margin: 0; } .qa-page-admin-guest-home-faq-list .table thead th { position: sticky; top: 0; z-index: 1; background: linear-gradient(110deg,var(--page-tint),#f7faff); }
.qa-page-admin-guest-home-faq-list .table tbody tr { transition: background-color .18s ease; } .qa-page-admin-guest-home-faq-list .table tbody tr:hover { background: var(--page-tint); }
@media (max-width: 767.98px) {
  .qa-page-admin-guest-home-faq-list > .page-header, .qa-page-admin-guest-home-faq-list > .qa-page-heading, .qa-page-admin-guest-home-faq-list > .d-flex:has(> .page-title), .qa-page-admin-guest-home-faq-list > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-guest-home-faq-list .card { border-radius: 15px; }
  .qa-page-admin-guest-home-faq-list .card-body { padding: 1rem; }
  .qa-page-admin-guest-home-faq-list .table-responsive { border-radius: 11px; }
  .qa-page-admin-guest-home-faq-list .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-guest-home-faq-list *, .qa-page-admin-guest-home-faq-list *::before, .qa-page-admin-guest-home-faq-list *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0 qa-view-title" data-qa-page="admin-guest-home-faq-list"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span>Quản lý FAQ trang chủ</h2>
    <a href="/admin/guest-home-faq/new" class="btn btn-primary">
        <i class="bi bi-plus-lg me-1"></i>Thêm FAQ mới
    </a>
</div>

<div class="card shadow-sm">
    <div class="card-body">
        <c:if test="${not empty flashSuccess}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${flashSuccess}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${flashError}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="table-responsive">
            <table class="table table-hover align-middle">
                <thead class="table-light">
                    <tr>
                        <th style="width: 50px;">#</th>
                        <th>Danh mục</th>
                        <th>Câu hỏi</th>
                        <th style="width: 80px;">Thứ tự</th>
                        <th style="width: 100px;">Trạng thái</th>
                        <th style="width: 180px;">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="faq" items="${faqs}" varStatus="status">
                        <tr>
                            <td class="text-muted">${status.count}</td>
                            <td><span class="badge bg-primary-lt text-primary">${faq.categoryName}</span></td>
                            <td><strong>${faq.question}</strong></td>
                            <td>${faq.displayOrder}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${faq.active}">
                                        <span class="badge bg-success-lt text-success">Hiển thị</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary-lt text-secondary">Ẩn</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="btn-group btn-group-sm">
                                    <form action="/admin/guest-home-faq/${faq.id}/move-up" method="post" style="display: inline;">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                        <button type="submit" class="btn btn-outline-secondary" title="Đưa lên trên">
                                            <i class="bi bi-arrow-up"></i>
                                        </button>
                                    </form>
                                    <form action="/admin/guest-home-faq/${faq.id}/move-down" method="post" style="display: inline;">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                        <button type="submit" class="btn btn-outline-secondary" title="Đưa xuống dưới">
                                            <i class="bi bi-arrow-down"></i>
                                        </button>
                                    </form>
                                    <a href="/admin/guest-home-faq/${faq.id}/edit" class="btn btn-outline-primary" title="Sửa">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <form action="/admin/guest-home-faq/${faq.id}/toggle" method="post" style="display: inline;">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                        <button type="submit" class="btn btn-outline-${faq.active ? 'warning' : 'success'}" title="${faq.active ? 'Ẩn' : 'Hiển thị'}">
                                            <i class="bi bi-eye${faq.active ? '-slash' : ''}"></i>
                                        </button>
                                    </form>
                                    <form action="/admin/guest-home-faq/${faq.id}/delete" method="post" style="display: inline;" onsubmit="return confirm('Bạn có chắc muốn xóa FAQ này?');">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                        <button type="submit" class="btn btn-outline-danger" title="Xóa">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty faqs}">
                        <tr>
                            <td colspan="6" class="text-center py-4 text-muted">
                                Chưa có FAQ nào. <a href="/admin/guest-home-faq/new">Thêm FAQ mới</a>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
</div>
