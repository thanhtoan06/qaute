<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Đặt lịch hẹn tư vấn mới | QAUTE</title>

<div class="qa-page-student-appointment-book" data-qa-view="student-appointment-book">
<style>
/* ==========================================================================
   STUDENT APPOINTMENT BOOKING - MODERN DESIGN SYSTEM 2026
   Palette: Azure Navy (#091e3e) -> Royal Blue (#124991) -> Deep Teal (#087d92)
   Accent: Electric Cyan (#00d2ff), Emerald (#10b981), Amber (#f59e0b)
   ========================================================================== */
.qa-page-student-appointment-book {
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
.qa-page-student-appointment-book .student-hero-banner {
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
.qa-page-student-appointment-book .student-hero-banner::before {
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
.qa-page-student-appointment-book .student-hero-banner::after {
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
.qa-page-student-appointment-book .hero-content-wrap {
    position: relative;
    z-index: 2;
}
.qa-page-student-appointment-book .hero-eyebrow-pill {
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
.qa-page-student-appointment-book .hero-eyebrow-dot {
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
.qa-page-student-appointment-book .hero-title {
    font-size: clamp(1.45rem, 2.5vw, 2.15rem);
    font-weight: 800;
    letter-spacing: -0.03em;
    line-height: 1.25;
    margin-bottom: 0.5rem;
    color: #ffffff;
}
.qa-page-student-appointment-book .hero-subtitle {
    font-size: 0.95rem;
    color: rgba(255, 255, 255, 0.85);
    max-width: 650px;
    line-height: 1.55;
    margin: 0;
}
.qa-page-student-appointment-book .hero-back-btn {
    background: rgba(255, 255, 255, 0.14);
    border: 1px solid rgba(255, 255, 255, 0.25);
    color: #ffffff;
    backdrop-filter: blur(6px);
    border-radius: 12px;
    padding: 0.6rem 1.15rem;
    font-size: 0.85rem;
    font-weight: 600;
    transition: all 0.22s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
}
.qa-page-student-appointment-book .hero-back-btn:hover {
    background: rgba(255, 255, 255, 0.28);
    color: #ffffff;
    transform: translateX(-3px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

/* 2. STEPPER PROGRESS BAR */
.qa-page-student-appointment-book .wizard-stepper-card {
    background: #ffffff;
    border: 1px solid var(--student-border);
    border-radius: 20px;
    box-shadow: var(--student-card-shadow);
    padding: 1.25rem 1.5rem;
    margin-bottom: 2rem;
}
.qa-page-student-appointment-book .wizard-steps {
    display: flex;
    justify-content: space-between;
    position: relative;
    margin: 0;
    padding: 0;
}
.qa-page-student-appointment-book .wizard-step-item {
    flex: 1;
    text-align: center;
    position: relative;
    user-select: none;
    transition: all 0.2s ease;
    padding: 0.35rem 0.5rem;
}
.qa-page-student-appointment-book .wizard-step-item .step-number {
    width: 44px;
    height: 44px;
    line-height: 44px;
    border-radius: 14px;
    background-color: #f1f5f9;
    color: #64748b;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-weight: 750;
    font-size: 1rem;
    border: 2px solid #e2e8f0;
    margin-bottom: 0.5rem;
    transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
}
.qa-page-student-appointment-book .wizard-step-item.active .step-number {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%);
    color: #ffffff;
    border-color: #00d2ff;
    box-shadow: 0 0 0 5px rgba(0, 210, 255, 0.25), 0 8px 18px rgba(18, 73, 145, 0.35);
    transform: translateY(-2px);
}
.qa-page-student-appointment-book .wizard-step-item.completed .step-number {
    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
    color: #ffffff;
    border-color: #34d399;
    box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
}
.qa-page-student-appointment-book .wizard-step-item.completed .step-number::after {
    content: '\F272';
    font-family: 'bootstrap-icons';
    font-size: 1.15rem;
    font-weight: bold;
}
.qa-page-student-appointment-book .wizard-step-item.completed .step-number > span {
    display: none;
}
.qa-page-student-appointment-book .wizard-step-item .step-title {
    font-size: 0.88rem;
    font-weight: 650;
    color: #64748b;
    display: block;
    line-height: 1.35;
}
.qa-page-student-appointment-book .wizard-step-item.active .step-title {
    font-weight: 800;
    color: #091e3e;
}
.qa-page-student-appointment-book .wizard-step-item.completed .step-title {
    color: #10b981;
    font-weight: 700;
}
.qa-page-student-appointment-book .wizard-step-item:not(:last-child)::after {
    content: '';
    position: absolute;
    top: 25px;
    left: 55%;
    width: 90%;
    height: 3px;
    background-color: #e2e8f0;
    z-index: 0;
    transition: background-color 0.3s ease;
}
.qa-page-student-appointment-book .wizard-step-item.completed:not(:last-child)::after {
    background: linear-gradient(90deg, #10b981, #059669);
}

/* 3. WIZARD PANELS & CARDS */
.qa-page-student-appointment-book .wizard-panel {
    display: none;
}
.qa-page-student-appointment-book .wizard-panel.active {
    display: block;
}
.qa-page-student-appointment-book .card {
    border: 1px solid var(--student-border);
    border-radius: 20px;
    background: #ffffff;
    box-shadow: var(--student-card-shadow);
    transition: all 0.25s ease;
    overflow: hidden;
}
.qa-page-student-appointment-book .card-header {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border-bottom: 1px solid #e2e8f0;
    padding: 1.25rem 1.6rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
}
.qa-page-student-appointment-book .card-title {
    color: #091e3e;
    font-size: 1.15rem;
    font-weight: 800;
    letter-spacing: -0.02em;
    display: inline-flex;
    align-items: center;
    gap: 0.75rem;
    margin: 0;
}
.qa-page-student-appointment-book .step-badge-pill {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%);
    color: #ffffff;
    border-radius: 999px;
    padding: 0.25rem 0.75rem;
    font-size: 0.78rem;
    font-weight: 750;
    letter-spacing: 0.04em;
    box-shadow: 0 3px 8px rgba(18, 73, 145, 0.2);
}
.qa-page-student-appointment-book .card-body {
    padding: clamp(1.25rem, 2.5vw, 2rem);
}
.qa-page-student-appointment-book .card-footer {
    background: #f8fafc;
    border-top: 1px solid #e2e8f0;
    padding: 1.15rem 1.6rem;
}

/* 4. ADVISOR CARDS */
.qa-page-student-appointment-book .advisor-card {
    cursor: pointer;
    border: 2px solid #e2e8f0;
    border-radius: 16px;
    transition: all 0.22s ease;
    background: #ffffff;
}
.qa-page-student-appointment-book .advisor-card:hover {
    border-color: #124991;
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(18, 73, 145, 0.08);
}
.qa-page-student-appointment-book .advisor-card.selected {
    border-color: #124991 !important;
    background: linear-gradient(135deg, #eff6ff 0%, #ffffff 100%);
    box-shadow: 0 0 0 3px rgba(18, 73, 145, 0.15);
}
.qa-page-student-appointment-book .dot-online {
    display: inline-block;
    width: 9px;
    height: 9px;
    border-radius: 50%;
    background-color: var(--student-emerald);
    box-shadow: 0 0 6px var(--student-emerald);
}
.qa-page-student-appointment-book .dot-busy {
    display: inline-block;
    width: 9px;
    height: 9px;
    border-radius: 50%;
    background-color: #f59e0b;
}
.qa-page-student-appointment-book .dot-offline {
    display: inline-block;
    width: 9px;
    height: 9px;
    border-radius: 50%;
    background-color: #94a3b8;
}

/* 5. QUICK DATE & SLOTS */
.qa-page-student-appointment-book .quick-date-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 0.75rem;
    margin-bottom: 0.75rem;
}
.qa-page-student-appointment-book .quick-date-btn {
    min-width: 0;
    min-height: 72px;
    padding: 0.65rem 0.5rem;
    border-radius: 14px;
    border: 1.5px solid #cbd5e1;
    background: #ffffff;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    transition: all 0.2s ease;
}
.qa-page-student-appointment-book .quick-date-btn:hover {
    border-color: #124991;
    background: #f8fafc;
    transform: translateY(-2px);
}
.qa-page-student-appointment-book .quick-date-btn.selected {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%) !important;
    border-color: #124991 !important;
    color: #ffffff !important;
    box-shadow: 0 6px 16px rgba(18, 73, 145, 0.28);
}
.qa-page-student-appointment-book .slot-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 0.75rem;
}
.qa-page-student-appointment-book .slot-btn {
    min-height: 76px;
    border-radius: 14px;
    border: 1.5px solid #cbd5e1;
    background: #ffffff;
    color: #0f172a;
    padding: 0.65rem;
    transition: all 0.2s ease;
}
.qa-page-student-appointment-book .slot-btn:hover {
    border-color: #124991;
    background: #eff6ff;
    transform: translateY(-2px);
}
.qa-page-student-appointment-book .slot-btn.selected {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%) !important;
    border-color: #124991 !important;
    color: #ffffff !important;
    box-shadow: 0 6px 16px rgba(18, 73, 145, 0.28);
}
.qa-page-student-appointment-book .slot-btn.selected .slot-modality,
.qa-page-student-appointment-book .slot-btn.selected .slot-manager {
    color: rgba(255, 255, 255, 0.85) !important;
}

/* 6. MODALITY CHOICE CARDS */
.qa-page-student-appointment-book .appointment-choice-card {
    cursor: pointer;
    border: 2px solid #e2e8f0;
    border-radius: 16px;
    padding: 1.25rem;
    transition: all 0.22s ease;
    background: #ffffff;
}
.qa-page-student-appointment-book .appointment-choice-card:hover {
    border-color: #124991;
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(18, 73, 145, 0.08);
}
.qa-page-student-appointment-book .appointment-choice-card.selected {
    border-color: #124991;
    background: linear-gradient(135deg, #eff6ff 0%, #ffffff 100%);
    box-shadow: 0 0 0 3px rgba(18, 73, 145, 0.15);
}

/* 7. FORM INPUTS & BUTTONS */
.qa-page-student-appointment-book .form-label {
    color: #1e293b;
    font-size: 0.85rem;
    font-weight: 700;
    margin-bottom: 0.45rem;
}
.qa-page-student-appointment-book .form-label.required::after {
    content: " *";
    color: #ef4444;
    font-weight: bold;
}
.qa-page-student-appointment-book .form-control,
.qa-page-student-appointment-book .form-select {
    border: 1.5px solid #cbd5e1;
    border-radius: 12px;
    padding: 0.65rem 0.95rem;
    color: #1e293b;
    font-size: 0.92rem;
    background-color: #ffffff;
    transition: all 0.2s ease;
}
.qa-page-student-appointment-book .form-control:focus,
.qa-page-student-appointment-book .form-select:focus {
    border-color: #124991;
    box-shadow: 0 0 0 4px rgba(18, 73, 145, 0.12);
    outline: none;
}
.qa-page-student-appointment-book .btn {
    border-radius: 12px;
    font-weight: 700;
    padding: 0.65rem 1.45rem;
    transition: all 0.22s cubic-bezier(0.16, 1, 0.3, 1);
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
}
.qa-page-student-appointment-book .btn:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
}
.qa-page-student-appointment-book .btn-primary {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%);
    border: none;
    color: #ffffff;
    box-shadow: 0 6px 16px rgba(18, 73, 145, 0.28);
}
.qa-page-student-appointment-book .btn-primary:hover:not(:disabled) {
    background: linear-gradient(135deg, #091e3e 0%, #124991 100%);
    box-shadow: 0 8px 22px rgba(18, 73, 145, 0.38);
}
.qa-page-student-appointment-book .btn-success {
    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
    border: none;
    color: #ffffff;
    box-shadow: 0 6px 18px rgba(16, 185, 129, 0.3);
}
.qa-page-student-appointment-book .btn-success:hover:not(:disabled) {
    background: linear-gradient(135deg, #059669 0%, #047857 100%);
    box-shadow: 0 8px 24px rgba(16, 185, 129, 0.4);
}
.qa-page-student-appointment-book .btn-outline-secondary {
    border: 1.5px solid #cbd5e1;
    color: #475569;
    background: #ffffff;
}

@media (min-width: 576px) {
    .qa-page-student-appointment-book .quick-date-grid {
        grid-template-columns: repeat(3, minmax(0, 1fr));
    }
}
@media (min-width: 992px) {
    .qa-page-student-appointment-book .slot-grid {
        grid-template-columns: repeat(3, minmax(0, 1fr));
    }
}
@media (max-width: 767.98px) {
    .qa-page-student-appointment-book .student-hero-banner {
        border-radius: 18px;
        padding: 1.25rem;
    }
    .qa-page-student-appointment-book .wizard-stepper-card {
        padding: 1rem 0.5rem;
        border-radius: 16px;
    }
    .qa-page-student-appointment-book .wizard-step-item .step-number {
        width: 36px;
        height: 36px;
        line-height: 36px;
        font-size: 0.9rem;
        border-radius: 10px;
    }
    .qa-page-student-appointment-book .wizard-step-item .step-title {
        font-size: 0.78rem;
    }
}
</style>

<div class="row justify-content-center">
    <div class="col-12 col-xl-10">

        <!-- 1. HERO BANNER -->
        <div class="student-hero-banner">
            <div class="hero-content-wrap d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3">
                <div>
                    <div class="hero-eyebrow-pill">
                        <span class="hero-eyebrow-dot"></span>
                        <span>Đặt Lịch Hẹn Trực Tuyến • Tư Vấn 1:1 Với Thầy Cô</span>
                    </div>
                    <h1 class="hero-title">
                        <i class="bi bi-calendar-plus-fill text-info me-2"></i>Đặt Lịch Hẹn Tư Vấn Mới
                    </h1>
                    <p class="hero-subtitle">
                        Chủ động chọn chuyên mục, tư vấn viên mong muốn, chọn ngày giờ thuận tiện và hình thức gặp mặt (Trực tiếp tại trường hoặc Online qua Google Meet).
                    </p>
                </div>
                <div>
                    <a href="${ctx}/student/appointments" class="hero-back-btn">
                        <i class="bi bi-arrow-left"></i>
                        <span>Lịch hẹn của tôi</span>
                    </a>
                </div>
            </div>
        </div>

        <qa:alert/>

        <!-- 2. WIZARD STEPPERS -->
        <div class="wizard-stepper-card">
            <div class="wizard-steps">
                <div class="wizard-step-item active" id="step-ind-1">
                    <div class="step-number"><span>1</span></div>
                    <span class="step-title">1. Chuyên mục & Tư vấn viên</span>
                </div>
                <div class="wizard-step-item" id="step-ind-2">
                    <div class="step-number"><span>2</span></div>
                    <span class="step-title">2. Chọn ngày & Khung giờ</span>
                </div>
                <div class="wizard-step-item" id="step-ind-3">
                    <div class="step-number"><span>3</span></div>
                    <span class="step-title">3. Nội dung & Xác nhận</span>
                </div>
            </div>
        </div>

        <form id="apptBookForm" method="post" action="${ctx}/student/appointments/book">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="hidden" id="fCategoryId" name="categoryId" value="${form.categoryId}">
            <input type="hidden" id="fManagerId" name="managerId" value="${form.managerId}">
            <input type="hidden" id="fSlotId" name="slotId" value="${form.slotId}">
            <input type="hidden" id="fModality" name="modality" value="${form.modality}">
            <input type="hidden" id="fDate" name="date" value="${form.date}">

            <!-- ==========================================
                 BƯỚC 1: CHUYÊN MỤC & TƯ VẤN VIÊN
                 ========================================== -->
            <div class="wizard-panel active card" id="panel-1">
                <div class="card-header">
                    <h3 class="card-title">
                        <span class="step-badge-pill"><i class="bi bi-person-badge me-1"></i> Bước 1</span>
                        <span>Chọn Chuyên Mục Và Tư Vấn Viên</span>
                    </h3>
                    <span class="badge bg-light text-secondary border">Bước 1/3</span>
                </div>
                <div class="card-body">
                    <div class="alert alert-info border-0 rounded-4 d-flex align-items-center p-3 mb-4" style="background: linear-gradient(135deg, #e0f2fe 0%, #f0f9ff 100%);">
                        <i class="bi bi-info-circle-fill text-info fs-3 me-3"></i>
                        <div class="text-secondary small">
                            <strong class="text-dark d-block">Lựa chọn chuyên trách:</strong>
                            Chọn phòng ban phụ trách vấn đề của bạn, sau đó chọn chuyên mục cụ thể để hệ thống hiển thị danh sách các thầy cô tư vấn viên có lịch hẹn phù hợp.
                        </div>
                    </div>
                    <c:if test="${empty tree}">
                        <div class="alert alert-warning d-flex align-items-start gap-2" role="alert">
                            <i class="bi bi-exclamation-triangle-fill mt-1" aria-hidden="true"></i>
                            <div>
                                <strong>Hiện chưa có phòng ban hoạt động để đặt lịch.</strong>
                                Vui lòng liên hệ quản trị viên để bật phòng ban và chuyên mục tư vấn.
                            </div>
                        </div>
                    </c:if>

                    <div class="row g-3 mb-4">
                        <div class="col-12 col-md-6">
                            <label class="form-label required" for="deptSelect">
                                <i class="bi bi-building me-1 text-primary"></i>Phòng ban tiếp nhận
                            </label>
                            <select id="deptSelect" class="form-select form-select-lg" ${empty tree ? 'disabled' : ''}>
                                <option value="">${empty tree ? 'Không có phòng ban đang hoạt động' : '-- Chọn phòng ban --'}</option>
                                <c:forEach var="dept" items="${tree}">
                                    <option value="${dept.id()}"><c:out value="${dept.name()}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-12 col-md-6">
                            <label class="form-label required" for="catSelect">
                                <i class="bi bi-folder2-open me-1 text-primary"></i>Chuyên mục tư vấn
                            </label>
                            <select id="catSelect" class="form-select form-select-lg" disabled>
                                <option value="">-- Chọn chuyên mục --</option>
                            </select>
                        </div>
                    </div>

                    <div id="advisorSection" style="display:none">
                        <div class="d-flex align-items-center justify-content-between mb-2">
                            <label class="form-label fw-bold m-0">
                                <i class="bi bi-people-fill me-1 text-primary"></i>Chọn tư vấn viên mong muốn (Tùy chọn)
                            </label>
                            <span class="badge bg-secondary-lt text-secondary">Tùy chọn</span>
                        </div>
                        <p class="text-secondary small mb-3">
                            Bạn có thể chọn một tư vấn viên cụ thể hoặc để mặc định <strong>"Bất kỳ tư vấn viên phù hợp"</strong> để hệ thống tự động tìm khung giờ trống sớm nhất.
                        </p>
                        <div class="row g-3" id="advisorCards">
                            <div class="col-12 text-center text-secondary py-4" id="advisorLoading">
                                <span class="spinner-border spinner-border-sm me-2 text-primary"></span>Đang tải danh sách tư vấn viên...
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card-footer d-flex justify-content-between align-items-center">
                    <a href="${ctx}/student/appointments" class="btn btn-outline-secondary">
                        <i class="bi bi-x-lg"></i> Hủy bỏ
                    </a>
                    <button type="button" class="btn btn-primary px-4" id="btn-next-1" disabled>
                        <span>Tiếp theo</span>
                        <i class="bi bi-arrow-right"></i>
                    </button>
                </div>
            </div>

            <!-- ==========================================
                 BƯỚC 2: NGÀY & KHUNG GIỜ
                 ========================================== -->
            <div class="wizard-panel card" id="panel-2">
                <div class="card-header">
                    <h3 class="card-title">
                        <span class="step-badge-pill"><i class="bi bi-calendar3 me-1"></i> Bước 2</span>
                        <span>Chọn Ngày Làm Việc & Khung Giờ Hẹn</span>
                    </h3>
                    <span class="badge bg-light text-secondary border">Bước 2/3</span>
                </div>
                <div class="card-body">
                    <!-- Quick Date Selector -->
                    <div class="mb-4">
                        <label class="form-label required">
                            <i class="bi bi-calendar-event me-1 text-primary"></i>Chọn ngày làm việc sắp tới
                        </label>
                        <div id="quickDateGrid" class="quick-date-grid"></div>
                        <input type="date" id="dateInput" class="form-control visually-hidden" value="${form.date}" aria-label="Ngày hẹn">
                    </div>

                    <!-- Modality Selector -->
                    <div class="mb-4">
                        <label class="form-label required">
                            <i class="bi bi-broadcast me-1 text-primary"></i>Hình thức tư vấn mong muốn
                        </label>
                        <select id="modalitySelect" class="visually-hidden" aria-label="Hình thức tư vấn">
                            <option value="">Tất cả hình thức</option>
                            <option value="ONLINE">Trực tuyến (Online)</option>
                            <option value="IN_PERSON">Trực tiếp (Offline)</option>
                        </select>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <div class="appointment-choice-card h-100" data-modality-choice="IN_PERSON">
                                    <div class="d-flex align-items-start gap-3">
                                        <span class="avatar avatar-md bg-primary-lt text-primary rounded-3" style="width: 44px; height: 44px; display: inline-flex; align-items: center; justify-content: center; font-size: 1.25rem;">
                                            <i class="bi bi-geo-alt-fill"></i>
                                        </span>
                                        <div>
                                            <div class="fw-bold text-dark fs-6">Trực tiếp tại trường</div>
                                            <div class="small text-secondary mt-1">Gặp mặt trực tiếp tại Văn phòng tư vấn HCMUTE</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="appointment-choice-card h-100" data-modality-choice="ONLINE">
                                    <div class="d-flex align-items-start gap-3">
                                        <span class="avatar avatar-md bg-success-lt text-success rounded-3" style="width: 44px; height: 44px; display: inline-flex; align-items: center; justify-content: center; font-size: 1.25rem;">
                                            <i class="bi bi-camera-video-fill"></i>
                                        </span>
                                        <div>
                                            <div class="fw-bold text-dark fs-6">Trực tuyến (Online)</div>
                                            <div class="small text-secondary mt-1">Phòng họp Google Meet gửi tự động vào email sinh viên</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <hr class="my-4" style="border-top: 1px dashed #cbd5e1;">

                    <!-- Available Slots Grid -->
                    <div>
                        <div class="d-flex align-items-center justify-content-between mb-3">
                            <label class="form-label required m-0">
                                <i class="bi bi-clock-history me-1 text-primary"></i>Chọn khung giờ trống
                            </label>
                            <span class="text-muted small"><i class="bi bi-info-circle me-1"></i>Bấm vào khung giờ để chọn</span>
                        </div>

                        <div id="slotPlaceholder" class="text-center text-secondary py-5 bg-light rounded-4 border">
                            <i class="bi bi-calendar3 display-5 mb-2 d-block text-muted"></i>
                            <p class="mb-0 fw-medium">Vui lòng chọn ngày để hiển thị các khung giờ trống</p>
                        </div>

                        <div id="slotLoading" style="display:none" class="text-center py-5 bg-light rounded-4 border">
                            <span class="spinner-border text-primary mb-2" style="width: 2.5rem; height: 2.5rem;"></span>
                            <div class="fw-bold text-secondary">Đang tải khung giờ khả dụng...</div>
                        </div>

                        <div id="slotEmpty" style="display:none" class="text-center text-secondary py-5 bg-light rounded-4 border">
                            <i class="bi bi-calendar-x display-5 mb-2 d-block text-warning"></i>
                            <h5 class="fw-bold text-dark mb-1">Không có khung giờ trống trong ngày này</h5>
                            <p class="small text-muted mb-0">Tất cả khung giờ đã được đặt kín hoặc chưa mở lịch. Vui lòng chọn ngày khác.</p>
                        </div>

                        <div id="slotGrid" class="slot-grid"></div>

                        <div id="slotModalityEmpty" style="display:none" class="text-center text-secondary py-4 bg-light rounded-4 border mt-3">
                            <i class="bi bi-exclamation-triangle text-warning me-1"></i>
                            Không có khung giờ phù hợp với hình thức đã chọn. Hãy thử chọn hình thức khác hoặc ngày khác.
                        </div>
                    </div>
                </div>
                <div class="card-footer d-flex justify-content-between align-items-center">
                    <button type="button" class="btn btn-outline-secondary" id="btn-prev-2">
                        <i class="bi bi-arrow-left"></i>
                        <span>Quay lại bước 1</span>
                    </button>
                    <button type="button" class="btn btn-primary px-4" id="btn-next-2" disabled>
                        <span>Tiếp theo</span>
                        <i class="bi bi-arrow-right"></i>
                    </button>
                </div>
            </div>

            <!-- ==========================================
                 BƯỚC 3: NỘI DUNG & XÁC NHẬN
                 ========================================== -->
            <div class="wizard-panel card" id="panel-3">
                <div class="card-header">
                    <h3 class="card-title">
                        <span class="step-badge-pill" style="background: linear-gradient(135deg, #10b981 0%, #059669 100%);">
                            <i class="bi bi-check2-circle me-1"></i> Bước 3
                        </span>
                        <span>Nội Dung Trao Đổi & Xác Nhận Đặt Lịch</span>
                    </h3>
                    <span class="badge bg-success-lt text-success border border-success">Bước cuối cùng</span>
                </div>
                <div class="card-body">
                    <!-- Confirm Summary Card -->
                    <div class="alert alert-success border-0 rounded-4 d-flex align-items-start gap-3 mb-4 p-3" id="confirmSummary" style="background: linear-gradient(135deg, #ecfdf5 0%, #f0fdf4 100%);">
                        <i class="bi bi-calendar-check-fill fs-2 text-success mt-1"></i>
                        <div class="flex-fill">
                            <div class="fw-bold text-dark fs-6" id="summarySlot">—</div>
                            <div class="text-secondary small mt-1" id="summaryAdvisor">—</div>
                            <div class="text-secondary small" id="summaryModality">—</div>
                        </div>
                    </div>

                    <div class="mb-4">
                        <div class="d-flex justify-content-between align-items-center mb-1">
                            <label class="form-label required m-0" for="topicInput">
                                <i class="bi bi-chat-left-quote me-1 text-primary"></i>Chủ đề cần tư vấn
                            </label>
                            <span class="badge bg-light text-secondary border rounded-pill" id="topicCount">0 / 200</span>
                        </div>
                        <input type="text" name="topic" class="form-control form-control-lg" id="topicInput"
                               maxlength="200" placeholder="Tóm tắt ngắn gọn nội dung cần trao đổi (tối thiểu 10 ký tự)..."
                               value="<c:out value='${form.topic}'/>">
                        <small class="form-hint text-muted mt-1 d-block">
                            <i class="bi bi-info-circle me-1"></i>Nêu rõ vấn đề giúp thầy cô chuẩn bị trước hồ sơ và tài liệu giải đáp.
                        </small>
                    </div>

                    <div class="mb-4">
                        <div class="d-flex justify-content-between align-items-center mb-1">
                            <label class="form-label m-0" for="noteInput">
                                <i class="bi bi-journal-text me-1 text-primary"></i>Ghi chú thêm (Tùy chọn)
                            </label>
                            <span class="badge bg-light text-secondary border rounded-pill" id="noteCount">0 / 500</span>
                        </div>
                        <textarea name="note" class="form-control" id="noteInput" rows="3"
                                  maxlength="500"
                                  placeholder="Thông tin bổ sung bạn muốn thầy cô biết trước (hoàn cảnh cụ thể, mã học phần, kết quả kỳ trước... tối đa 500 ký tự)"><c:out value="${form.note}"/></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label" for="ticketSelect">
                            <i class="bi bi-ticket-perforated me-1 text-primary"></i>Gắn với yêu cầu tư vấn đang mở (Tùy chọn)
                        </label>
                        <select name="ticketId" class="form-select form-select-lg" id="ticketSelect">
                            <option value="">-- Không gắn yêu cầu nào --</option>
                            <c:forEach var="t" items="${openTickets}">
                                <option value="${t.id()}"
                                        ${form.ticketId != null and form.ticketId == t.id() ? 'selected' : ''}>
                                    [<c:out value="${t.code()}"/>] <c:out value="${t.title()}"/>
                                </option>
                            </c:forEach>
                        </select>
                        <small class="form-hint text-muted mt-1 d-block">
                            Chọn nếu cuộc hẹn này nhằm giải quyết tiếp một phiếu yêu cầu bạn đã gửi trước đó.
                        </small>
                    </div>
                </div>
                <div class="card-footer d-flex justify-content-between align-items-center">
                    <button type="button" class="btn btn-outline-secondary" id="btn-prev-3">
                        <i class="bi bi-arrow-left"></i>
                        <span>Quay lại bước 2</span>
                    </button>
                    <button type="submit" class="btn btn-success px-4" id="btnSubmit">
                        <i class="bi bi-check-lg me-1"></i>
                        <span>Xác Nhận Đặt Lịch Hẹn Ngay</span>
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
window.APPT_TREE = [<c:forEach var="dept" items="${tree}" varStatus="ds">{id:${dept.id()},name:"<c:out value='${dept.name()}'/>",categories:[<c:forEach var="cat" items="${dept.categories()}" varStatus="cs">{id:${cat.id()},name:"<c:out value='${cat.name()}'/>",children:[<c:forEach var="sub" items="${cat.children()}" varStatus="ss">{id:${sub.id()},name:"<c:out value='${sub.name()}'/>"}${!ss.last?',':''}</c:forEach>]}${!cs.last?',':''}</c:forEach>]}${!ds.last?',':''}</c:forEach>];
window.APPT_CTX = '${ctx}';
</script>
<script src="${ctx}/assets/js/student/appointment-book.js?v=20261026b"></script>
</div>
