<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Lịch hẹn tư vấn của tôi | QAUTE</title>

<div class="qa-page-student-appointment-list" data-qa-view="student-appointment-list">
<style>
/* ==========================================================================
   STUDENT APPOINTMENTS LIST - MODERN DESIGN SYSTEM 2026
   Palette: Azure Navy (#091e3e) -> Royal Blue (#124991) -> Deep Teal (#087d92)
   Accent: Electric Cyan (#00d2ff), Emerald (#10b981), Amber (#f59e0b)
   ========================================================================== */
.qa-page-student-appointment-list {
    --student-primary: #124991;
    --student-primary-dark: #091e3e;
    --student-teal: #087d92;
    --student-cyan: #00d2ff;
    --student-emerald: #10b981;
    --student-border: #e2e8f0;
    --student-card-shadow: 0 10px 30px -5px rgba(9, 30, 62, 0.07), 0 4px 10px -2px rgba(9, 30, 62, 0.04);
    min-width: 0;
    color: #1e293b;
    font-feature-settings: "cv02", "cv03", "cv04", "cv11";
}

/* 1. HERO BANNER */
.qa-page-student-appointment-list .student-hero-banner {
    position: relative;
    background: linear-gradient(135deg, #091e3e 0%, #124991 55%, #087d92 100%);
    border-radius: 24px;
    padding: clamp(1.5rem, 3vw, 2.25rem);
    color: #ffffff;
    overflow: hidden;
    margin-bottom: 2rem;
    box-shadow: 0 20px 40px -15px rgba(9, 30, 62, 0.35);
    border: 1px solid rgba(255, 255, 255, 0.12);
}
.qa-page-student-appointment-list .student-hero-banner::before {
    content: '';
    position: absolute;
    top: -40%;
    right: -10%;
    width: 420px;
    height: 420px;
    background: radial-gradient(circle, rgba(0, 210, 255, 0.22) 0%, rgba(18, 73, 145, 0) 70%);
    border-radius: 50%;
    pointer-events: none;
}
.qa-page-student-appointment-list .student-hero-banner::after {
    content: '';
    position: absolute;
    bottom: -50%;
    left: 20%;
    width: 350px;
    height: 350px;
    background: radial-gradient(circle, rgba(16, 185, 129, 0.18) 0%, rgba(9, 30, 62, 0) 70%);
    border-radius: 50%;
    pointer-events: none;
}
.qa-page-student-appointment-list .hero-content-wrap {
    position: relative;
    z-index: 2;
}
.qa-page-student-appointment-list .hero-eyebrow-pill {
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
    background: rgba(255, 255, 255, 0.12);
    backdrop-filter: blur(8px);
    -webkit-backdrop-filter: blur(8px);
    border: 1px solid rgba(255, 255, 255, 0.22);
    border-radius: 999px;
    padding: 0.35rem 0.95rem;
    font-size: 0.78rem;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.08em;
    color: #bbf2f6;
    margin-bottom: 0.75rem;
}
.qa-page-student-appointment-list .hero-eyebrow-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background-color: var(--student-cyan);
    box-shadow: 0 0 10px var(--student-cyan);
    animation: studentPulse 2s infinite ease-in-out;
}
@keyframes studentPulse {
    0%, 100% { transform: scale(1); opacity: 1; }
    50% { transform: scale(1.35); opacity: 0.7; }
}
.qa-page-student-appointment-list .hero-title {
    font-size: clamp(1.45rem, 2.5vw, 2.15rem);
    font-weight: 800;
    letter-spacing: -0.03em;
    line-height: 1.25;
    margin-bottom: 0.5rem;
    color: #ffffff;
}
.qa-page-student-appointment-list .hero-subtitle {
    font-size: 0.95rem;
    color: rgba(255, 255, 255, 0.85);
    max-width: 650px;
    line-height: 1.55;
    margin: 0;
}
.qa-page-student-appointment-list .hero-action-btn {
    background: #ffffff;
    color: #091e3e;
    border: 1px solid #ffffff;
    border-radius: 14px;
    padding: 0.7rem 1.4rem;
    font-size: 0.92rem;
    font-weight: 750;
    transition: all 0.22s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 0.6rem;
    box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.25);
}
.qa-page-student-appointment-list .hero-action-btn:hover {
    background: #f8fafc;
    color: #124991;
    transform: translateY(-2px);
    box-shadow: 0 12px 28px -4px rgba(0, 0, 0, 0.35);
}

/* 2. MODERN NAV TABS CARD */
.qa-page-student-appointment-list .nav-tabs-card {
    background: #ffffff;
    border: 1px solid var(--student-border);
    border-radius: 18px;
    padding: 0.5rem;
    box-shadow: var(--student-card-shadow);
    margin-bottom: 1.75rem;
}
.qa-page-student-appointment-list .nav-tabs-card .nav-pills {
    display: flex;
    gap: 0.5rem;
}
.qa-page-student-appointment-list .nav-tabs-card .nav-link {
    border-radius: 12px;
    font-weight: 700;
    font-size: 0.88rem;
    color: #64748b;
    padding: 0.65rem 1.25rem;
    transition: all 0.2s ease;
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
    border: none;
    background: transparent;
}
.qa-page-student-appointment-list .nav-tabs-card .nav-link:hover {
    color: #091e3e;
    background: #f1f5f9;
}
.qa-page-student-appointment-list .nav-tabs-card .nav-link.active {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%);
    color: #ffffff;
    box-shadow: 0 4px 12px rgba(18, 73, 145, 0.25);
}

/* 3. APPOINTMENT CARD */
.qa-page-student-appointment-list .appointment-card {
    border: 1px solid var(--student-border);
    border-radius: 20px;
    background: #ffffff;
    box-shadow: var(--student-card-shadow);
    transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
    overflow: hidden;
    display: flex;
    flex-direction: column;
}
.qa-page-student-appointment-list .appointment-card:hover {
    transform: translateY(-3px);
    border-color: #cbd5e1;
    box-shadow: 0 16px 36px -8px rgba(9, 30, 62, 0.12), 0 4px 12px -2px rgba(9, 30, 62, 0.06);
}
.qa-page-student-appointment-list .appointment-card-header {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border-bottom: 1px solid #e2e8f0;
    padding: 1rem 1.35rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
}
.qa-page-student-appointment-list .time-chip {
    display: inline-flex;
    align-items: center;
    gap: 0.45rem;
    font-weight: 750;
    font-size: 0.88rem;
    color: #124991;
    background: #ffffff;
    border: 1px solid #dbeafe;
    border-radius: 999px;
    padding: 0.35rem 0.85rem;
    box-shadow: 0 2px 6px rgba(18, 73, 145, 0.06);
}
.qa-page-student-appointment-list .appointment-card-body {
    padding: 1.35rem;
    flex: 1 1 auto;
}
.qa-page-student-appointment-list .appointment-title {
    font-size: 1.05rem;
    font-weight: 800;
    line-height: 1.4;
    color: #0f172a;
    margin-bottom: 0.65rem;
    display: block;
    text-decoration: none;
    transition: color 0.18s ease;
}
.qa-page-student-appointment-list .appointment-title:hover {
    color: #124991;
}
.qa-page-student-appointment-list .appointment-meta-row {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    font-size: 0.85rem;
    color: #475569;
    margin-bottom: 0.5rem;
}
.qa-page-student-appointment-list .appointment-card-footer {
    background: #f8fafc;
    border-top: 1px solid #e2e8f0;
    padding: 0.95rem 1.35rem;
    display: flex;
    flex-wrap: wrap;
    gap: 0.5rem;
    align-items: center;
    justify-content: space-between;
}

/* 4. BUTTONS & CHIPS */
.qa-page-student-appointment-list .btn {
    border-radius: 10px;
    font-weight: 650;
    padding: 0.45rem 0.95rem;
    font-size: 0.83rem;
    transition: all 0.2s ease;
    display: inline-flex;
    align-items: center;
    gap: 0.4rem;
}
.qa-page-student-appointment-list .btn:hover:not(:disabled) {
    transform: translateY(-1px);
}
.qa-page-student-appointment-list .btn-primary {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%);
    border: none;
    color: #ffffff;
    box-shadow: 0 4px 12px rgba(18, 73, 145, 0.2);
}
.qa-page-student-appointment-list .btn-primary:hover:not(:disabled) {
    background: linear-gradient(135deg, #091e3e 0%, #124991 100%);
}
.qa-page-student-appointment-list .btn-outline-primary {
    border-color: #cbd5e1;
    color: #124991;
    background: #ffffff;
}
.qa-page-student-appointment-list .btn-outline-primary:hover:not(:disabled) {
    background: #eff6ff;
    border-color: #124991;
    color: #124991;
}
.qa-page-student-appointment-list .btn-outline-secondary {
    border-color: #cbd5e1;
    color: #475569;
    background: #ffffff;
}
.qa-page-student-appointment-list .btn-outline-secondary:hover:not(:disabled) {
    background: #f1f5f9;
    color: #0f172a;
}
.qa-page-student-appointment-list .btn-outline-warning {
    border-color: #fed7aa;
    color: #ea580c;
    background: #fff7ed;
}
.qa-page-student-appointment-list .btn-outline-warning:hover:not(:disabled) {
    background: #ea580c;
    color: #ffffff;
    border-color: #ea580c;
}

@media (max-width: 767.98px) {
    .qa-page-student-appointment-list .student-hero-banner {
        border-radius: 18px;
        padding: 1.25rem;
    }
    .qa-page-student-appointment-list .nav-tabs-card {
        padding: 0.35rem;
    }
    .qa-page-student-appointment-list .nav-tabs-card .nav-link {
        padding: 0.5rem 0.85rem;
        font-size: 0.8rem;
    }
}
</style>

<div class="row justify-content-center">
    <div class="col-12 col-xl-11">

        <!-- 1. HERO BANNER -->
        <div class="student-hero-banner">
            <div class="hero-content-wrap d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3">
                <div>
                    <div class="hero-eyebrow-pill">
                        <span class="hero-eyebrow-dot"></span>
                        <span>Lịch Hẹn Tư Vấn • Gặp Trực Tiếp Hoặc Trực Tuyến</span>
                    </div>
                    <h1 class="hero-title">
                        <i class="bi bi-calendar-check-fill text-info me-2"></i>Lịch Hẹn Tư Vấn Của Tôi
                    </h1>
                    <p class="hero-subtitle">
                        Theo dõi thời gian gặp mặt trực tiếp tại văn phòng phòng ban hoặc tham gia phòng họp Google Meet với các thầy cô tư vấn viên nhà trường.
                    </p>
                </div>
                <div>
                    <a href="${ctx}/student/appointments/book" class="hero-action-btn">
                        <i class="bi bi-plus-circle-fill text-primary fs-5"></i>
                        <span>Đặt Lịch Hẹn Mới</span>
                    </a>
                </div>
            </div>
        </div>

        <qa:alert/>

        <!-- 2. STATUS TABS -->
        <div class="nav-tabs-card">
            <ul class="nav nav-pills" id="appointmentTabNav">
                <li class="nav-item">
                    <a class="nav-link ${tab eq 'upcoming' ? 'active' : ''}"
                       href="${ctx}/student/appointments?tab=upcoming">
                        <i class="bi bi-calendar-event"></i>
                        <span>Lịch hẹn sắp tới</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${tab eq 'past' ? 'active' : ''}"
                       href="${ctx}/student/appointments?tab=past">
                        <i class="bi bi-calendar-check"></i>
                        <span>Lịch hẹn đã qua</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${tab eq 'cancelled' ? 'active' : ''}"
                       href="${ctx}/student/appointments?tab=cancelled">
                        <i class="bi bi-calendar-x"></i>
                        <span>Lịch hẹn đã hủy</span>
                    </a>
                </li>
            </ul>
        </div>

        <!-- 3. APPOINTMENT CARDS LIST -->
        <c:choose>
            <c:when test="${empty appointments.content}">
                <div class="card p-5 border-0 shadow-sm rounded-4 text-center">
                    <div class="avatar avatar-xl bg-light text-muted rounded-circle mx-auto mb-3" style="width: 72px; height: 72px; display: inline-flex; align-items: center; justify-content: center; font-size: 2.2rem;">
                        <i class="bi bi-calendar-x"></i>
                    </div>
                    <h3 class="fw-bold text-dark mb-1">Chưa có lịch hẹn nào</h3>
                    <p class="text-secondary small mb-4 mx-auto" style="max-width: 420px;">
                        Không có lịch hẹn nào trong mục này. Nếu bạn cần trao đổi trực tiếp với tư vấn viên, hãy chọn khung giờ phù hợp để đặt lịch.
                    </p>
                    <div>
                        <a href="${ctx}/student/appointments/book" class="btn btn-primary rounded-pill px-4 py-2 shadow-sm">
                            <i class="bi bi-calendar-plus me-1"></i> Đặt lịch hẹn ngay
                        </a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-4">
                    <c:forEach var="item" items="${appointments.content}">
                        <div class="col-12 col-md-6 col-lg-6">
                            <div class="appointment-card h-100">
                                <div class="appointment-card-header">
                                    <div class="time-chip">
                                        <i class="bi bi-clock-fill text-primary"></i>
                                        <span><c:out value="${item.startAt()}"/> – <c:out value="${item.endAt().toLocalTime()}"/></span>
                                    </div>
                                    <div>
                                        <c:choose>
                                            <c:when test="${item.status() == 'CONFIRMED'}">
                                                <span class="badge bg-blue-lt px-2 py-1 rounded-pill">Đã xác nhận</span>
                                            </c:when>
                                            <c:when test="${item.status() == 'COMPLETED'}">
                                                <span class="badge bg-green-lt px-2 py-1 rounded-pill">Đã hoàn tất</span>
                                            </c:when>
                                            <c:when test="${item.status() == 'NO_SHOW'}">
                                                <span class="badge bg-yellow-lt px-2 py-1 rounded-pill">Vắng mặt</span>
                                            </c:when>
                                            <c:when test="${item.status() == 'CANCELLED'}">
                                                <span class="badge bg-danger-lt px-2 py-1 rounded-pill">Đã hủy</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary-lt px-2 py-1 rounded-pill"><c:out value="${item.status()}"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="appointment-card-body">
                                    <a href="${ctx}/student/appointments/${item.id()}" class="appointment-title">
                                        <c:out value="${item.topic()}"/>
                                    </a>

                                    <div class="appointment-meta-row text-truncate">
                                        <i class="bi bi-tag-fill text-primary"></i>
                                        <span class="text-truncate"><c:out value="${item.categoryPath()}"/></span>
                                    </div>

                                    <div class="appointment-meta-row">
                                        <i class="bi bi-person-badge-fill text-secondary"></i>
                                        <span>Tư vấn viên: <strong class="text-dark"><c:out value="${item.managerName()}"/></strong></span>
                                    </div>

                                    <div class="appointment-meta-row">
                                        <i class="bi bi-broadcast text-secondary"></i>
                                        <span>Hình thức:</span>
                                        <c:choose>
                                            <c:when test="${item.modality() == 'ONLINE'}">
                                                <span class="badge bg-success-lt text-success fw-bold"><i class="bi bi-camera-video me-1"></i>Trực tuyến (Meet)</span>
                                            </c:when>
                                            <c:when test="${item.modality() == 'IN_PERSON'}">
                                                <span class="badge bg-primary-lt text-primary fw-bold"><i class="bi bi-geo-alt me-1"></i>Trực tiếp (Tại trường)</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary-lt"><c:out value="${item.modality()}"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <c:if test="${not empty item.location()}">
                                        <div class="appointment-meta-row small text-secondary">
                                            <i class="bi bi-geo-alt text-danger"></i>
                                            <span>Địa điểm: <strong class="text-dark"><c:out value="${item.location()}"/></strong></span>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty item.meetingLink()}">
                                        <div class="mt-3">
                                            <a href="<c:out value='${item.meetingLink()}'/>" target="_blank" rel="noopener noreferrer"
                                               class="btn btn-sm btn-success rounded-pill px-3 shadow-sm">
                                                <i class="bi bi-camera-video-fill me-1"></i>Vào phòng họp Meet ngay
                                            </a>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty item.ticketCode()}">
                                        <div class="appointment-meta-row small mt-2">
                                            <i class="bi bi-ticket-perforated text-muted"></i>
                                            <span class="text-muted">Gắn với yêu cầu:</span>
                                            <a href="${ctx}/student/tickets/${item.ticketId()}" class="badge bg-light text-primary border">
                                                <c:out value="${item.ticketCode()}"/>
                                            </a>
                                        </div>
                                    </c:if>
                                </div>
                                <div class="appointment-card-footer">
                                    <div class="d-flex gap-2">
                                        <a href="${ctx}/student/appointments/${item.id()}" class="btn btn-outline-primary">
                                            <i class="bi bi-eye"></i>Chi tiết
                                        </a>
                                        <a href="${ctx}/student/appointments/${item.id()}/ics" class="btn btn-outline-secondary" title="Tải tệp lịch iCalendar">
                                            <i class="bi bi-calendar-plus"></i>.ics
                                        </a>
                                    </div>
                                    <div class="d-flex gap-2">
                                        <c:if test="${item.canReschedule()}">
                                            <a href="${ctx}/student/appointments/${item.id()}/reschedule" class="btn btn-outline-warning">
                                                <i class="bi bi-clock-history"></i>Đổi lịch
                                            </a>
                                        </c:if>
                                        <c:if test="${item.canCancel()}">
                                            <qa:confirmModal id="cancel-modal-${item.id()}"
                                                             title="Hủy lịch hẹn tư vấn"
                                                             message="Bạn có chắc chắn muốn hủy lịch hẹn này? Tư vấn viên sẽ nhận được thông báo để sắp xếp lại lịch làm việc."
                                                             actionUrl="${ctx}/student/appointments/${item.id()}/cancel"
                                                             confirmLabel="Xác nhận hủy"
                                                             cssClass="btn btn-outline-danger"
                                                             requireReason="${true}"/>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="mt-4 d-flex justify-content-center">
                    <c:set var="paginationParams" value="tab=${tab}"/>
                    <qa:pagination page="${appointments}" baseUrl="${ctx}/student/appointments" params="${paginationParams}"/>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</div>
