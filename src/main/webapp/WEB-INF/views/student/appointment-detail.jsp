<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chi tiết lịch hẹn</title>
<div class="qa-page-student-appointment-detail" data-qa-view="student-appointment-detail">
<style>
.qa-page-student-appointment-detail { --page-accent: #118c78; --page-accent-dark: #0b6e60; --page-tint: #e8f8f4; --page-line: #ccece3; min-width: 0; color: #344c67; }
.qa-page-student-appointment-detail > .page-header, .qa-page-student-appointment-detail > .qa-page-heading, .qa-page-student-appointment-detail > .d-flex:has(> .page-title), .qa-page-student-appointment-detail > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #e8f8f4, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-student-appointment-detail > .page-header::after, .qa-page-student-appointment-detail > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #ccece3; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-student-appointment-detail .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-student-appointment-detail .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #ccece3; border-radius: 12px; color: #0b6e60; background: linear-gradient(145deg,#fff,#e8f8f4); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-student-appointment-detail .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-student-appointment-detail .card:hover { border-color: #ccece3; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-student-appointment-detail .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#e8f8f4); }
.qa-page-student-appointment-detail .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-student-appointment-detail .card-body { color: #435b75; }
.qa-page-student-appointment-detail .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#e8f8f4); }
.qa-page-student-appointment-detail .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-student-appointment-detail .form-control, .qa-page-student-appointment-detail .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-student-appointment-detail textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-student-appointment-detail .form-control:focus, .qa-page-student-appointment-detail .form-select:focus { border-color: #118c78; box-shadow: 0 0 0 4px #e8f8f4; }
.qa-page-student-appointment-detail .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-student-appointment-detail .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-student-appointment-detail .btn-primary { border-color: #118c78; background: linear-gradient(120deg,#118c78,#0b6e60); box-shadow: 0 5px 13px #ccece3; }
.qa-page-student-appointment-detail .btn-outline-primary { border-color: #ccece3; color: #0b6e60; background: #fff; }
.qa-page-student-appointment-detail .btn-outline-primary:hover { border-color: #118c78; color: #fff; background: linear-gradient(120deg,#118c78,#0b6e60); }
.qa-page-student-appointment-detail .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#e8f8f4); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-student-appointment-detail .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-student-appointment-detail .table { margin-bottom: 0; color: #405872; }
.qa-page-student-appointment-detail .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#e8f8f4,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-student-appointment-detail .table tbody tr { transition: background-color .18s ease; }
.qa-page-student-appointment-detail .table tbody tr:hover { background: #e8f8f4; }
.qa-page-student-appointment-detail .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-student-appointment-detail .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-student-appointment-detail .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-student-appointment-detail .nav-tabs .nav-link.active { color: #0b6e60; box-shadow: inset 0 -2px #118c78; }
.qa-page-student-appointment-detail .nav-pills .nav-link.active { color: #0b6e60; background: #e8f8f4; box-shadow: inset 0 0 0 1px #ccece3; }
.qa-page-student-appointment-detail .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-student-appointment-detail .progress-bar { border-radius: inherit; }
.qa-page-student-appointment-detail .slot-btn, .qa-page-student-appointment-detail .appointment-choice-card, .qa-page-student-appointment-detail .advisor-card { border-radius: 14px; transition: transform .2s ease, box-shadow .2s ease, border-color .2s ease; } .qa-page-student-appointment-detail .slot-btn:hover, .qa-page-student-appointment-detail .appointment-choice-card:hover, .qa-page-student-appointment-detail .advisor-card:hover { transform: translateY(-2px); }
@media (max-width: 767.98px) {
  .qa-page-student-appointment-detail > .page-header, .qa-page-student-appointment-detail > .qa-page-heading, .qa-page-student-appointment-detail > .d-flex:has(> .page-title), .qa-page-student-appointment-detail > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-student-appointment-detail .card { border-radius: 15px; }
  .qa-page-student-appointment-detail .card-body { padding: 1rem; }
  .qa-page-student-appointment-detail .table-responsive { border-radius: 11px; }
  .qa-page-student-appointment-detail .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-student-appointment-detail *, .qa-page-student-appointment-detail *::before, .qa-page-student-appointment-detail *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="page-header d-print-none">
    <div class="container-xl">
        <div class="row g-2 align-items-center">
            <div class="col">
                <h2 class="page-title qa-view-title" data-qa-page="student-appointment-detail"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-calendar-check"></i></span>Chi tiết lịch hẹn</h2>
            </div>
            <div class="col-auto ms-auto">
                <a href="${ctx}/student/appointments" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left me-1"></i>Danh sách lịch hẹn
                </a>
            </div>
        </div>
    </div>
</div>

<div class="page-body">
    <div class="container-xl">
        <qa:alert/>

        <div class="row g-3">
            <%-- Left Column: Chi tiết --%>
            <div class="col-12 col-lg-8">
                <div class="card mb-3 shadow-sm">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h3 class="card-title mb-0"><c:out value="${appointment.topic()}"/></h3>
                        <div>
                            <c:choose>
                                <c:when test="${appointment.status() == 'CONFIRMED'}">
                                    <span class="badge bg-blue-lt">Đã xác nhận</span>
                                </c:when>
                                <c:when test="${appointment.status() == 'COMPLETED'}">
                                    <span class="badge bg-green-lt">Đã hoàn tất</span>
                                </c:when>
                                <c:when test="${appointment.status() == 'NO_SHOW'}">
                                    <span class="badge bg-yellow-lt">Vắng mặt</span>
                                </c:when>
                                <c:when test="${appointment.status() == 'CANCELLED'}">
                                    <span class="badge bg-danger-lt">Đã hủy</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary-lt"><c:out value="${appointment.status()}"/></span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="card-body">
                        <dl class="row mb-0">
                            <dt class="col-sm-4 text-secondary">Thời gian hẹn</dt>
                            <dd class="col-sm-8 fw-semibold text-primary">
                                <i class="bi bi-clock me-1"></i>
                                <c:out value="${appointment.startAt()}"/> – <c:out value="${appointment.endAt().toLocalTime()}"/>
                            </dd>

                            <dt class="col-sm-4 text-secondary">Chuyên mục</dt>
                            <dd class="col-sm-8"><c:out value="${appointment.categoryPath()}"/></dd>

                            <dt class="col-sm-4 text-secondary">Hình thức</dt>
                            <dd class="col-sm-8">
                                <c:choose>
                                    <c:when test="${appointment.modality() == 'ONLINE'}">
                                        <span class="badge bg-azure-lt"><i class="bi bi-camera-video me-1"></i>Trực tuyến (Online)</span>
                                    </c:when>
                                    <c:when test="${appointment.modality() == 'IN_PERSON'}">
                                        <span class="badge bg-green-lt"><i class="bi bi-geo-alt me-1"></i>Trực tiếp (Offline)</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary-lt"><c:out value="${appointment.modality()}"/></span>
                                    </c:otherwise>
                                </c:choose>
                            </dd>

                            <c:if test="${not empty appointment.location()}">
                                <dt class="col-sm-4 text-secondary">Địa điểm</dt>
                                <dd class="col-sm-8"><i class="bi bi-geo-alt text-danger me-1"></i><c:out value="${appointment.location()}"/></dd>
                            </c:if>

                            <c:if test="${not empty appointment.meetingLink()}">
                                <dt class="col-sm-4 text-secondary">Phòng họp trực tuyến</dt>
                                <dd class="col-sm-8">
                                    <a href="<c:out value='${appointment.meetingLink()}'/>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-primary">
                                        <i class="bi bi-camera-video me-1"></i>Vào phòng họp ngay
                                    </a>
                                    <div class="text-secondary small mt-1 text-break">
                                        <c:out value="${appointment.meetingLink()}"/>
                                    </div>
                                </dd>
                            </c:if>

                            <c:if test="${not empty appointment.ticketCode()}">
                                <dt class="col-sm-4 text-secondary">Yêu cầu liên quan</dt>
                                <dd class="col-sm-8">
                                    <a href="${ctx}/student/tickets/${appointment.ticketId()}">
                                        [<c:out value="${appointment.ticketCode()}"/>] Xem chi tiết yêu cầu
                                    </a>
                                </dd>
                            </c:if>

                            <c:if test="${not empty appointment.note()}">
                                <dt class="col-sm-4 text-secondary">Ghi chú của bạn</dt>
                                <dd class="col-sm-8 text-break"><c:out value="${appointment.note()}"/></dd>
                            </c:if>

                            <c:if test="${not empty appointment.cancelReason()}">
                                <dt class="col-sm-4 text-danger">Lý do hủy</dt>
                                <dd class="col-sm-8 text-danger fw-medium"><c:out value="${appointment.cancelReason()}"/></dd>
                            </c:if>
                        </dl>
                    </div>
                </div>

                <%-- Tóm tắt kết luận từ tư vấn viên (nếu có) --%>
                <c:if test="${not empty appointment.studentVisibleSummary()}">
                    <div class="card mb-3 border-success shadow-sm">
                        <div class="card-header bg-success-lt">
                            <h3 class="card-title text-success mb-0">
                                <i class="bi bi-journal-check me-2"></i>Kết luận buổi tư vấn
                            </h3>
                        </div>
                        <div class="card-body">
                            <div class="p-3 bg-light rounded text-body">
                                <c:out value="${appointment.studentVisibleSummary()}"/>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>

            <%-- Right Column: Tư vấn viên & Thao tác --%>
            <div class="col-12 col-lg-4">
                <div class="card mb-3 shadow-sm">
                    <div class="card-header">
                        <h3 class="card-title mb-0"><i class="bi bi-person me-2"></i>Tư vấn viên</h3>
                    </div>
                    <div class="card-body">
                        <div class="d-flex align-items-center mb-3">
                            <span class="avatar avatar-lg me-3" style="background-color:var(--tblr-primary-lt)">
                                <c:out value="${fn:substring(appointment.managerName(), 0, 1)}"/>
                            </span>
                            <div>
                                <div class="fw-bold fs-3 text-dark"><c:out value="${appointment.managerName()}"/></div>
                                <div class="text-secondary small"><c:out value="${appointment.categoryPath()}"/></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="card shadow-sm">
                    <div class="card-header">
                        <h3 class="card-title mb-0"><i class="bi bi-gear me-2"></i>Thao tác</h3>
                    </div>
                    <div class="card-body d-grid gap-2">
                        <a href="${ctx}/student/appointments/${appointment.id()}/ics" class="btn btn-outline-secondary">
                            <i class="bi bi-calendar-plus me-1"></i>Thêm vào lịch (.ics)
                        </a>

                        <c:if test="${appointment.canReschedule()}">
                            <a href="${ctx}/student/appointments/${appointment.id()}/reschedule" class="btn btn-warning">
                                <i class="bi bi-clock-history me-1"></i>Đổi khung giờ
                            </a>
                        </c:if>

                        <c:if test="${appointment.canCancel()}">
                            <qa:confirmModal id="cancel-detail-modal"
                                             title="Hủy lịch hẹn"
                                             message="Bạn có chắc chắn muốn hủy lịch hẹn này? Tư vấn viên sẽ nhận được thông báo."
                                             actionUrl="${ctx}/student/appointments/${appointment.id()}/cancel"
                                             confirmLabel="Hủy lịch hẹn"
                                             cssClass="btn-danger w-100"
                                             requireReason="${true}"/>
                        </c:if>

                        <a href="${ctx}/student/appointments" class="btn btn-link text-secondary">
                            <i class="bi bi-arrow-left me-1"></i>Quay lại danh sách
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
