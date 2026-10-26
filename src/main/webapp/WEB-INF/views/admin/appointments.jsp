<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Lịch hẹn toàn hệ thống</title>
<div class="qa-page-admin-appointments" data-qa-view="admin-appointments">
<style>
.qa-page-admin-appointments { --page-accent: #118c78; --page-accent-dark: #0b6e60; --page-tint: #e8f8f4; --page-line: #ccece3; min-width: 0; color: #344c67; }
.qa-page-admin-appointments > .page-header, .qa-page-admin-appointments > .qa-page-heading, .qa-page-admin-appointments > .d-flex:has(> .page-title), .qa-page-admin-appointments > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #e8f8f4, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-admin-appointments > .page-header::after, .qa-page-admin-appointments > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #ccece3; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-admin-appointments .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-admin-appointments .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #ccece3; border-radius: 12px; color: #0b6e60; background: linear-gradient(145deg,#fff,#e8f8f4); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-admin-appointments .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-admin-appointments .card:hover { border-color: #ccece3; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-admin-appointments .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#e8f8f4); }
.qa-page-admin-appointments .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-admin-appointments .card-body { color: #435b75; }
.qa-page-admin-appointments .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#e8f8f4); }
.qa-page-admin-appointments .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-admin-appointments .form-control, .qa-page-admin-appointments .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-admin-appointments textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-admin-appointments .form-control:focus, .qa-page-admin-appointments .form-select:focus { border-color: #118c78; box-shadow: 0 0 0 4px #e8f8f4; }
.qa-page-admin-appointments .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-admin-appointments .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-admin-appointments .btn-primary { border-color: #118c78; background: linear-gradient(120deg,#118c78,#0b6e60); box-shadow: 0 5px 13px #ccece3; }
.qa-page-admin-appointments .btn-outline-primary { border-color: #ccece3; color: #0b6e60; background: #fff; }
.qa-page-admin-appointments .btn-outline-primary:hover { border-color: #118c78; color: #fff; background: linear-gradient(120deg,#118c78,#0b6e60); }
.qa-page-admin-appointments .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#e8f8f4); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-admin-appointments .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-admin-appointments .table { margin-bottom: 0; color: #405872; }
.qa-page-admin-appointments .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#e8f8f4,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-admin-appointments .table tbody tr { transition: background-color .18s ease; }
.qa-page-admin-appointments .table tbody tr:hover { background: #e8f8f4; }
.qa-page-admin-appointments .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-admin-appointments .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-admin-appointments .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-admin-appointments .nav-tabs .nav-link.active { color: #0b6e60; box-shadow: inset 0 -2px #118c78; }
.qa-page-admin-appointments .nav-pills .nav-link.active { color: #0b6e60; background: #e8f8f4; box-shadow: inset 0 0 0 1px #ccece3; }
.qa-page-admin-appointments .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-admin-appointments .progress-bar { border-radius: inherit; }
.qa-page-admin-appointments .table-responsive { overflow: auto; border: 1px solid #e3ebf5; border-radius: 15px; background: #fff; box-shadow: 0 8px 24px rgba(16,48,84,.05); }
.qa-page-admin-appointments .table { margin: 0; } .qa-page-admin-appointments .table thead th { position: sticky; top: 0; z-index: 1; background: linear-gradient(110deg,var(--page-tint),#f7faff); }
.qa-page-admin-appointments .table tbody tr { transition: background-color .18s ease; } .qa-page-admin-appointments .table tbody tr:hover { background: var(--page-tint); }
.qa-page-admin-appointments .slot-btn, .qa-page-admin-appointments .appointment-choice-card, .qa-page-admin-appointments .advisor-card { border-radius: 14px; transition: transform .2s ease, box-shadow .2s ease, border-color .2s ease; } .qa-page-admin-appointments .slot-btn:hover, .qa-page-admin-appointments .appointment-choice-card:hover, .qa-page-admin-appointments .advisor-card:hover { transform: translateY(-2px); }
@media (max-width: 767.98px) {
  .qa-page-admin-appointments > .page-header, .qa-page-admin-appointments > .qa-page-heading, .qa-page-admin-appointments > .d-flex:has(> .page-title), .qa-page-admin-appointments > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-admin-appointments .card { border-radius: 15px; }
  .qa-page-admin-appointments .card-body { padding: 1rem; }
  .qa-page-admin-appointments .table-responsive { border-radius: 11px; }
  .qa-page-admin-appointments .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-appointments *, .qa-page-admin-appointments *::before, .qa-page-admin-appointments *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-appointments"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-calendar-check"></i></span>Lịch hẹn toàn hệ thống</h2>
  <span class="text-secondary">${appointments.totalElements} lịch hẹn</span>
</div>

<form method="get" action="${ctx}/admin/appointments" class="qa-filter-bar mb-3">
  <div><label class="form-label" for="appointment-q">Chủ đề</label><input class="form-control" id="appointment-q" type="search" name="q" maxlength="200" value="<c:out value='${filter.q}'/>" placeholder="Tìm theo chủ đề"/></div>
  <div><label class="form-label" for="appointment-status">Trạng thái</label><select class="form-select" id="appointment-status" name="status"><option value="">Tất cả</option><c:forEach var="item" items="${statuses}"><option value="${item}" ${filter.status eq item ? 'selected' : ''}><c:out value="${item}"/></option></c:forEach></select></div>
  <div><label class="form-label" for="appointment-modality">Hình thức</label><select class="form-select" id="appointment-modality" name="modality"><option value="">Tất cả</option><c:forEach var="item" items="${modalities}"><option value="${item}" ${filter.modality eq item ? 'selected' : ''}><c:choose><c:when test="${item eq 'ONLINE'}">Trực tuyến</c:when><c:when test="${item eq 'IN_PERSON'}">Trực tiếp</c:when><c:otherwise>Linh hoạt</c:otherwise></c:choose></option></c:forEach></select></div>
  <div><label class="form-label" for="appointment-manager">Tư vấn viên</label><select class="form-select" id="appointment-manager" name="managerId"><option value="">Tất cả</option><c:forEach var="item" items="${managers}"><option value="${item.id}" ${filter.managerId eq item.id ? 'selected' : ''}><c:out value="${item.fullName}"/></option></c:forEach></select></div>
  <div><label class="form-label" for="appointment-from">Ngày hẹn từ</label><input class="form-control" id="appointment-from" type="date" name="from" value="<c:out value='${filter.from}'/>"/></div>
  <div><label class="form-label" for="appointment-to">Đến ngày</label><input class="form-control" id="appointment-to" type="date" name="to" value="<c:out value='${filter.to}'/>"/></div>
  <div class="d-flex gap-2"><button class="btn btn-primary" type="submit">Lọc</button><a class="btn btn-outline-secondary" href="${ctx}/admin/appointments">Bỏ lọc</a></div>
</form>

<div class="card"><div class="table-responsive"><table class="table table-vcenter card-table qa-admin-table">
  <thead><tr><th>Thời gian</th><th>Sinh viên</th><th>Tư vấn viên</th><th>Chủ đề</th><th>Chuyên mục</th><th>Hình thức</th><th>Địa điểm</th><th>Trạng thái</th></tr></thead>
  <tbody><c:choose><c:when test="${empty appointments.content}"><tr><td colspan="8"><qa:emptyState icon="bi-calendar-x" title="Chưa có lịch hẹn phù hợp" message="Thử thay đổi điều kiện lọc."/></td></tr></c:when><c:otherwise>
    <c:forEach var="item" items="${appointments.content}"><tr>
      <td class="text-nowrap"><time datetime="<c:out value='${item.startAt()}'/>" data-qaute-datetime><c:out value="${item.startAt()}"/></time><div class="small text-secondary"><time datetime="<c:out value='${item.endAt()}'/>" data-qaute-datetime><c:out value="${item.endAt()}"/></time></div></td>
      <td><div class="fw-semibold"><c:out value="${item.studentName()}"/></div><small class="text-secondary"><c:out value="${item.studentEmail()}"/></small></td>
      <td><div class="fw-semibold"><c:out value="${item.managerName()}"/></div><small class="text-secondary"><c:out value="${item.managerEmail()}"/></small></td>
      <td><c:out value="${item.topic()}"/></td>
      <td><c:out value="${item.categoryPath()}"/></td>
      <td><c:choose><c:when test="${item.modality() eq 'ONLINE'}">Trực tuyến</c:when><c:when test="${item.modality() eq 'IN_PERSON'}">Trực tiếp</c:when><c:otherwise>Linh hoạt</c:otherwise></c:choose></td>
      <td><c:out value="${item.location()}"/></td>
      <td><span class="badge ${item.status() eq 'CONFIRMED' ? 'bg-blue-lt' : item.status() eq 'COMPLETED' ? 'bg-green-lt' : item.status() eq 'CANCELLED' ? 'bg-red-lt' : 'bg-orange-lt'}"><c:choose><c:when test="${item.status() eq 'CONFIRMED'}">Đã xác nhận</c:when><c:when test="${item.status() eq 'COMPLETED'}">Hoàn thành</c:when><c:when test="${item.status() eq 'NO_SHOW'}">Sinh viên vắng</c:when><c:otherwise>Đã hủy</c:otherwise></c:choose></span></td>
    </tr></c:forEach>
  </c:otherwise></c:choose></tbody>
</table></div></div>
<qa:pagination page="${appointments}" baseUrl="${ctx}/admin/appointments" params="${paginationParams}"/>
</div>
