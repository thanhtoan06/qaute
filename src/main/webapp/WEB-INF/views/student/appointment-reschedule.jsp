<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Đổi lịch hẹn tư vấn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/appointment-reschedule-1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/appointment-reschedule-2.css">
</head>
<div class="qa-page-student-appointment-reschedule" data-qa-view="student-appointment-reschedule"
     data-context-path="<c:out value='${ctx}'/>"
     data-category-id="<c:out value='${appointment.categoryId()}'/>"
     data-modality="<c:out value='${appointment.modality()}'/>"
     data-slot-id="<c:out value='${appointment.slotId()}'/>">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>



<div class="page-header d-print-none">
    <div class="container-xl">
        <div class="row g-2 align-items-center">
            <div class="col">
                <h2 class="page-title qa-view-title" data-qa-page="student-appointment-reschedule"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-calendar-check"></i></span>Đổi lịch hẹn tư vấn</h2>
            </div>
            <div class="col-auto ms-auto">
                <a href="${ctx}/student/appointments/${appointment.id()}" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left me-1"></i>Quay lại chi tiết
                </a>
            </div>
        </div>
    </div>
</div>

<div class="page-body">
    <div class="container-xl">
        <qa:alert/>

        <div class="row justify-content-center">
            <div class="col-12 col-lg-8">
                <%-- Current appointment info --%>
                <div class="card mb-3 shadow-sm border-primary-lt">
                    <div class="card-header bg-primary-lt">
                        <h3 class="card-title text-primary mb-0">
                            <i class="bi bi-info-circle me-2"></i>Lịch hẹn hiện tại
                        </h3>
                    </div>
                    <div class="card-body">
                        <div class="row g-2">
                            <div class="col-12 col-sm-6">
                                <div class="text-secondary small">Chủ đề:</div>
                                <div class="fw-semibold"><c:out value="${appointment.topic()}"/></div>
                            </div>
                            <div class="col-12 col-sm-6">
                                <div class="text-secondary small">Chuyên mục:</div>
                                <div><c:out value="${appointment.categoryPath()}"/></div>
                            </div>
                            <div class="col-12 col-sm-6">
                                <div class="text-secondary small">Tư vấn viên hiện tại:</div>
                                <div class="fw-medium"><c:out value="${appointment.managerName()}"/></div>
                            </div>
                            <div class="col-12 col-sm-6">
                                <div class="text-secondary small">Thời gian hiện tại:</div>
                                <div class="text-danger fw-semibold">
                                    <i class="bi bi-clock me-1"></i>
                                    <time datetime="${appointment.startAt()}" data-qaute-datetime><c:out value="${appointment.startAt()}"/></time> – <c:out value="${appointment.endAt().toLocalTime()}"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- Reschedule Form --%>
                <div class="card shadow-sm">
                    <div class="card-header">
                        <h3 class="card-title mb-0">
                            <i class="bi bi-calendar-event me-2"></i>Chọn khung giờ mới
                        </h3>
                    </div>
                    <div class="card-body">
                        <form id="rescheduleForm" method="post" action="${ctx}/student/appointments/${appointment.id()}/reschedule">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="hidden" id="newSlotId" name="newSlotId" value=""/>

                            <div class="row g-3 mb-4">
                                <div class="col-12 col-sm-6">
                                    <label class="form-label required" for="dateInput">Chọn ngày mới</label>
                                    <input type="date" id="dateInput" class="form-control" required/>
                                    <small class="form-hint">Chọn ngày cần dời lịch hẹn đến.</small>
                                </div>
                                <div class="col-12 col-sm-6">
                                    <label class="form-label" for="managerSelect">Tư vấn viên</label>
                                    <select id="managerSelect" class="form-select">
                                        <option value="">Bất kỳ tư vấn viên phù hợp</option>
                                        <option value="${appointment.managerId()}">
                                            <c:out value="${appointment.managerName()}"/> (Hiện tại)
                                        </option>
                                    </select>
                                    <small class="form-hint">Có thể giữ tư vấn viên hiện tại hoặc người khác.</small>
                                </div>
                            </div>

                            <%-- Slot Grid Area --%>
                            <div class="mb-4">
                                <label class="form-label required">Khung giờ trống</label>

                                <div id="slotPlaceholder" class="text-center text-secondary py-5 bg-light rounded">
                                    <i class="bi bi-calendar3 display-6 mb-2 d-block"></i>
                                    <p class="mb-0">Vui lòng chọn ngày để tìm các khung giờ trống phù hợp.</p>
                                </div>

                                <div id="slotLoading" class="u-display-none" class="text-center py-4 bg-light rounded">
                                    <span class="spinner-border spinner-border-sm me-2"></span>Đang kiểm tra khung giờ trống...
                                </div>

                                <div id="slotEmpty" class="u-display-none" class="text-center text-secondary py-4 bg-light rounded">
                                    <i class="bi bi-calendar-x display-6 mb-2 d-block text-warning"></i>
                                    <p class="mb-0">Không có khung giờ trống nào trong ngày đã chọn. Vui lòng chọn ngày khác.</p>
                                </div>

                                <div id="slotGrid" class="row g-2"></div>
                            </div>

                            <%-- Selected Slot Summary --%>
                            <div id="selectedSlotAlert" class="alert alert-success d-flex align-items-center gap-3 mb-4 u-display-none">
                                <i class="bi bi-check-circle-fill fs-2"></i>
                                <div>
                                    <div class="fw-bold" id="selectedSlotText">—</div>
                                    <div class="small" id="selectedAdvisorText">—</div>
                                </div>
                            </div>

                            <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                                <a href="${ctx}/student/appointments/${appointment.id()}" class="btn btn-outline-secondary">
                                    Hủy bỏ
                                </a>
                                <button type="submit" id="btnSubmitReschedule" class="btn btn-primary" disabled>
                                    <i class="bi bi-check-lg me-1"></i>Xác nhận đổi lịch
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/pages/student/appointment-reschedule.js?v=20261028a"></script>
</div>
