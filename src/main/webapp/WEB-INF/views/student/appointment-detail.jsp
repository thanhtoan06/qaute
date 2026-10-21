<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chi tiết lịch hẹn</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="page-header d-print-none">
    <div class="container-xl">
        <div class="row g-2 align-items-center">
            <div class="col">
                <h2 class="page-title">Chi tiết lịch hẹn</h2>
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

