<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Lịch hẹn tư vấn của tôi | QAUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/appointment-list.css">
</head>

<div class="qa-page-student-appointment-list" data-qa-view="student-appointment-list">


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
                    <div class="avatar avatar-xl bg-light text-muted rounded-circle mx-auto mb-3 u-width-72px u-height-72px u-display-inline-flex u-align-items-center u-justify-content-center u-text-size-2-2rem">
                        <i class="bi bi-calendar-x"></i>
                    </div>
                    <h3 class="fw-bold text-dark mb-1">Chưa có lịch hẹn nào</h3>
                    <p class="text-secondary small mb-4 mx-auto u-max-w-420px">
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
                                        <span><time datetime="${item.startAt()}" data-qaute-datetime><c:out value="${item.startAt()}"/></time> – <c:out value="${item.endAt().toLocalTime()}"/></span>
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
