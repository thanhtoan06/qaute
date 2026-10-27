<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Đặt lịch hẹn tư vấn mới | QAUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/appointment-book.css">
</head>

<div class="qa-page-student-appointment-book" data-qa-view="student-appointment-book">


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
                    <div class="alert alert-info border-0 rounded-4 d-flex align-items-center p-3 mb-4 u-bg-linear-gradient-135deg-e0f2fe-0-f0f9ff-100">
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

                    <div id="advisorSection" class="u-display-none">
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
                                        <span class="avatar avatar-md bg-primary-lt text-primary rounded-3 u-width-44px u-height-44px u-display-inline-flex u-align-items-center u-justify-content-center u-text-size-1-25rem">
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
                                        <span class="avatar avatar-md bg-success-lt text-success rounded-3 u-width-44px u-height-44px u-display-inline-flex u-align-items-center u-justify-content-center u-text-size-1-25rem">
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

                    <hr class="my-4 u-border-top-1px-dashed-cbd5e1">

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

                        <div id="slotLoading" class="u-display-none" class="text-center py-5 bg-light rounded-4 border">
                            <span class="spinner-border text-primary mb-2 u-width-2-5rem u-height-2-5rem"></span>
                            <div class="fw-bold text-secondary">Đang tải khung giờ khả dụng...</div>
                        </div>

                        <div id="slotEmpty" class="u-display-none" class="text-center text-secondary py-5 bg-light rounded-4 border">
                            <i class="bi bi-calendar-x display-5 mb-2 d-block text-warning"></i>
                            <h5 class="fw-bold text-dark mb-1">Không có khung giờ trống trong ngày này</h5>
                            <p class="small text-muted mb-0">Tất cả khung giờ đã được đặt kín hoặc chưa mở lịch. Vui lòng chọn ngày khác.</p>
                        </div>

                        <div id="slotGrid" class="slot-grid"></div>

                        <div id="slotModalityEmpty" class="u-display-none" class="text-center text-secondary py-4 bg-light rounded-4 border mt-3">
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
                        <span class="step-badge-pill u-bg-linear-gradient-135deg-10b981-0-059669-100">
                            <i class="bi bi-check2-circle me-1"></i> Bước 3
                        </span>
                        <span>Nội Dung Trao Đổi & Xác Nhận Đặt Lịch</span>
                    </h3>
                    <span class="badge bg-success-lt text-success border border-success">Bước cuối cùng</span>
                </div>
                <div class="card-body">
                    <!-- Confirm Summary Card -->
                    <div class="alert alert-success border-0 rounded-4 d-flex align-items-start gap-3 mb-4 p-3 u-bg-linear-gradient-135deg-ecfdf5-0-f0fdf4-100" id="confirmSummary">
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

<div id="appointment-tree-data" class="d-none" data-context-path="<c:out value='${ctx}'/>">
    <c:forEach var="dept" items="${tree}">
        <div data-department-id="${dept.id()}" data-department-name="<c:out value='${dept.name()}'/>">
            <c:forEach var="cat" items="${dept.categories()}">
                <div data-category-id="${cat.id()}" data-category-name="<c:out value='${cat.name()}'/>">
                    <c:forEach var="sub" items="${cat.children()}">
                        <span data-subcategory-id="${sub.id()}" data-subcategory-name="<c:out value='${sub.name()}'/>"></span>
                    </c:forEach>
                </div>
            </c:forEach>
        </div>
    </c:forEach>
</div>
<script src="${ctx}/assets/js/student/appointment-book.js?v=20261026b"></script>
</div>
