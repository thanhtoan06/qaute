<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Hồ sơ sinh viên</title>
<div class="qa-page-manager-student-detail" data-qa-view="manager-student-detail">
<style>
.qa-page-manager-student-detail { --page-accent: #258e89; --page-accent-dark: #1e6865; --page-tint: #eaf8f6; --page-line: #d2eeea; min-width: 0; color: #344c67; }
.qa-page-manager-student-detail > .page-header, .qa-page-manager-student-detail > .qa-page-heading, .qa-page-manager-student-detail > .d-flex:has(> .page-title), .qa-page-manager-student-detail > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #eaf8f6, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-manager-student-detail > .page-header::after, .qa-page-manager-student-detail > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #d2eeea; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-manager-student-detail .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-manager-student-detail .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #d2eeea; border-radius: 12px; color: #1e6865; background: linear-gradient(145deg,#fff,#eaf8f6); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-manager-student-detail .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-manager-student-detail .card:hover { border-color: #d2eeea; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-manager-student-detail .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#eaf8f6); }
.qa-page-manager-student-detail .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-manager-student-detail .card-body { color: #435b75; }
.qa-page-manager-student-detail .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#eaf8f6); }
.qa-page-manager-student-detail .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-manager-student-detail .form-control, .qa-page-manager-student-detail .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-manager-student-detail textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-manager-student-detail .form-control:focus, .qa-page-manager-student-detail .form-select:focus { border-color: #258e89; box-shadow: 0 0 0 4px #eaf8f6; }
.qa-page-manager-student-detail .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-manager-student-detail .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-manager-student-detail .btn-primary { border-color: #258e89; background: linear-gradient(120deg,#258e89,#1e6865); box-shadow: 0 5px 13px #d2eeea; }
.qa-page-manager-student-detail .btn-outline-primary { border-color: #d2eeea; color: #1e6865; background: #fff; }
.qa-page-manager-student-detail .btn-outline-primary:hover { border-color: #258e89; color: #fff; background: linear-gradient(120deg,#258e89,#1e6865); }
.qa-page-manager-student-detail .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#eaf8f6); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-manager-student-detail .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-manager-student-detail .table { margin-bottom: 0; color: #405872; }
.qa-page-manager-student-detail .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#eaf8f6,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-manager-student-detail .table tbody tr { transition: background-color .18s ease; }
.qa-page-manager-student-detail .table tbody tr:hover { background: #eaf8f6; }
.qa-page-manager-student-detail .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-manager-student-detail .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-manager-student-detail .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-manager-student-detail .nav-tabs .nav-link.active { color: #1e6865; box-shadow: inset 0 -2px #258e89; }
.qa-page-manager-student-detail .nav-pills .nav-link.active { color: #1e6865; background: #eaf8f6; box-shadow: inset 0 0 0 1px #d2eeea; }
.qa-page-manager-student-detail .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-manager-student-detail .progress-bar { border-radius: inherit; }
.qa-page-manager-student-detail .avatar { box-shadow: 0 8px 20px rgba(25,70,120,.12); }
@media (max-width: 767.98px) {
  .qa-page-manager-student-detail > .page-header, .qa-page-manager-student-detail > .qa-page-heading, .qa-page-manager-student-detail > .d-flex:has(> .page-title), .qa-page-manager-student-detail > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-manager-student-detail .card { border-radius: 15px; }
  .qa-page-manager-student-detail .card-body { padding: 1rem; }
  .qa-page-manager-student-detail .table-responsive { border-radius: 11px; }
  .qa-page-manager-student-detail .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-student-detail *, .qa-page-manager-student-detail *::before, .qa-page-manager-student-detail *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="mb-3"><a href="${ctx}/manager/students" class="text-decoration-none">← Danh sách sinh viên</a></div>
<h2 class="page-title mb-3 qa-view-title" data-qa-page="manager-student-detail"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-person-lines-fill"></i></span>Hồ sơ sinh viên</h2>

<%-- Hiển thị thông tin sinh viên và các yêu cầu, lịch hẹn, phiên chat liên quan. --%>
<div class="card mb-4"><div class="card-body">
  <div class="row g-3">
    <div class="col-12 col-lg-4">
      <h3 class="card-title mb-2"><c:out value="${detail.student().user().fullName()}"/></h3>
      <div class="text-secondary">MSSV: <c:out value="${detail.student().user().mssv()}"/></div>
    </div>
    <div class="col-12 col-md-6 col-lg-4">
      <div><strong>Email:</strong> <c:out value="${detail.student().user().email()}"/></div>
      <div><strong>Điện thoại:</strong> <c:out value="${detail.student().phone()}" default="Chưa cập nhật"/></div>
      <div><strong>Khoa:</strong> <c:out value="${detail.student().facultyName()}" default="Chưa cập nhật"/></div>
    </div>
    <div class="col-12 col-md-6 col-lg-4">
      <div><strong>Lớp:</strong> <c:out value="${detail.student().classCode()}" default="Chưa cập nhật"/></div>
      <div><strong>Khóa:</strong> <c:out value="${detail.student().cohort()}" default="Chưa cập nhật"/></div>
      <div><strong>Chương trình:</strong> <c:out value="${detail.student().program()}" default="Chưa cập nhật"/></div>
    </div>
  </div>
</div></div>

<ul class="nav nav-tabs mb-3" role="tablist">
  <li class="nav-item"><button class="nav-link active" data-bs-toggle="tab" data-bs-target="#student-tickets" type="button" role="tab">Yêu cầu <span class="badge bg-secondary-lt ms-1">${detail.tickets().totalElements}</span></button></li>
  <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#student-appointments" type="button" role="tab">Lịch hẹn <span class="badge bg-secondary-lt ms-1">${detail.appointments().totalElements}</span></button></li>
  <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#student-chats" type="button" role="tab">Chat <span class="badge bg-secondary-lt ms-1">${detail.chats().totalElements}</span></button></li>
</ul>
<div class="tab-content">
  <div class="tab-pane fade show active" id="student-tickets" role="tabpanel">
    <div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
      <thead><tr><th>Mã</th><th>Tiêu đề</th><th>Trạng thái</th><th>Ngày tạo</th><th></th></tr></thead>
      <tbody><c:choose><c:when test="${empty detail.tickets().content}"><tr><td colspan="5" class="text-center text-secondary py-4">Không có yêu cầu trong phạm vi của bạn.</td></tr></c:when>
      <c:otherwise><c:forEach var="ticket" items="${detail.tickets().content}"><tr>
        <td><c:out value="${ticket.code()}"/></td><td><c:out value="${ticket.title()}"/></td>
        <td><qa:statusBadge status="${ticket.status()}"/></td><td><c:out value="${ticket.createdAt()}"/></td>
        <td><a class="btn btn-sm btn-outline-primary" href="${ctx}/manager/tickets/${ticket.id()}">Mở</a></td>
      </tr></c:forEach></c:otherwise></c:choose></tbody>
    </table></div></div>
    <qa:pagination page="${detail.tickets()}" baseUrl="${ctx}/manager/students/${detail.student().user().id()}"/>
  </div>
  <div class="tab-pane fade" id="student-appointments" role="tabpanel">
    <div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
      <thead><tr><th>Thời gian</th><th>Chủ đề</th><th>Trạng thái</th><th></th></tr></thead>
      <tbody><c:choose><c:when test="${empty detail.appointments().content}"><tr><td colspan="4" class="text-center text-secondary py-4">Chưa có lịch hẹn với bạn.</td></tr></c:when>
      <c:otherwise><c:forEach var="appointment" items="${detail.appointments().content}"><tr>
        <td><c:out value="${appointment.startAt()}"/></td><td><c:out value="${appointment.topic()}"/></td>
        <td><c:out value="${appointment.status()}"/></td>
        <td><a class="btn btn-sm btn-outline-primary" href="${ctx}/manager/appointments/${appointment.id()}">Mở</a></td>
      </tr></c:forEach></c:otherwise></c:choose></tbody>
    </table></div></div>
    <qa:pagination page="${detail.appointments()}" baseUrl="${ctx}/manager/students/${detail.student().user().id()}"/>
  </div>
  <div class="tab-pane fade" id="student-chats" role="tabpanel">
    <div class="card"><div class="table-responsive"><table class="table table-vcenter card-table">
      <thead><tr><th>Bắt đầu</th><th>Câu hỏi</th><th>Trạng thái</th></tr></thead>
      <tbody><c:choose><c:when test="${empty detail.chats().content}"><tr><td colspan="3" class="text-center text-secondary py-4">Chưa có phiên chat với bạn.</td></tr></c:when>
      <c:otherwise><c:forEach var="chat" items="${detail.chats().content}"><tr>
        <td><c:out value="${chat.startedAt()}"/></td><td><c:out value="${chat.question()}"/></td>
        <td><c:out value="${chat.status()}"/></td>
      </tr></c:forEach></c:otherwise></c:choose></tbody>
    </table></div></div>
    <qa:pagination page="${detail.chats()}" baseUrl="${ctx}/manager/students/${detail.student().user().id()}"/>
  </div>
</div>
</div>
