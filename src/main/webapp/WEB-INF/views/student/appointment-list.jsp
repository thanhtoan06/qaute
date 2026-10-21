<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Lịch hẹn tư vấn của tôi</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="page-header d-print-none">
    <div class="container-xl">
        <div class="row g-2 align-items-center">
            <div class="col">
                <h2 class="page-title">Lịch hẹn tư vấn</h2>
            </div>
            <div class="col-auto ms-auto">
                <a href="${ctx}/student/appointments/book" class="btn btn-primary">
                    <i class="bi bi-plus-lg me-1"></i>Đặt lịch mới
                </a>
            </div>
        </div>
    </div>
</div>

<div class="page-body">
    <div class="container-xl">
        <qa:alert/>

        <%-- Tabs --%>
        <div class="card mb-3">
            <div class="card-header p-0">
                <ul class="nav nav-tabs card-header-tabs">
                    <li class="nav-item">
                        <a class="nav-link ${tab eq 'upcoming' ? 'active' : ''}"
                           href="${ctx}/student/appointments?tab=upcoming">
                            <i class="bi bi-calendar-event me-1"></i>Sắp tới
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${tab eq 'past' ? 'active' : ''}"
                           href="${ctx}/student/appointments?tab=past">
                            <i class="bi bi-calendar-check me-1"></i>Đã qua
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${tab eq 'cancelled' ? 'active' : ''}"
                           href="${ctx}/student/appointments?tab=cancelled">
                            <i class="bi bi-calendar-x me-1"></i>Đã hủy
                        </a>
                    </li>
                </ul>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty appointments.content}">
                <div class="card">
                    <div class="card-body">
                        <qa:emptyState icon="bi-calendar-x"
                                       title="Không có lịch hẹn"
                                       message="Chưa có lịch hẹn nào trong mục này."
                                       actionUrl="${ctx}/student/appointments/book"
                                       actionLabel="Đặt lịch hẹn ngay"/>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-3">
                    <c:forEach var="item" items="${appointments.content}">
                        <div class="col-12 col-md-6 col-lg-6">
                            <div class="card h-100 shadow-sm">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <div class="text-primary fw-semibold">
                                        <i class="bi bi-clock me-1"></i>
                                        <c:out value="${item.startAt()}"/> – <c:out value="${item.endAt().toLocalTime()}"/>
                                    </div>
                                    <div>
                                        <c:choose>
                                            <c:when test="${item.status() == 'CONFIRMED'}">
                                                <span class="badge bg-blue-lt">Đã xác nhận</span>
                                            </c:when>
                                            <c:when test="${item.status() == 'COMPLETED'}">
                                                <span class="badge bg-green-lt">Đã hoàn tất</span>
                                            </c:when>
                                            <c:when test="${item.status() == 'NO_SHOW'}">
                                                <span class="badge bg-yellow-lt">Vắng mặt</span>
                                            </c:when>
                                            <c:when test="${item.status() == 'CANCELLED'}">
                                                <span class="badge bg-danger-lt">Đã hủy</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary-lt"><c:out value="${item.status()}"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="card-body">
                                    <h3 class="card-title mb-2">
                                        <a href="${ctx}/student/appointments/${item.id()}" class="text-reset">
                                            <c:out value="${item.topic()}"/>
                                        </a>
                                    </h3>
                                    <div class="text-secondary small mb-2">
                                        <i class="bi bi-tag me-1"></i><c:out value="${item.categoryPath()}"/>
                                    </div>
                                    <div class="mb-2">
                                        <i class="bi bi-person me-1 text-secondary"></i>Tư vấn viên:
                                        <strong><c:out value="${item.managerName()}"/></strong>
                                    </div>
                                    <div class="mb-2">
                                        <i class="bi bi-broadcast me-1 text-secondary"></i>Hình thức:
                                        <c:choose>
                                            <c:when test="${item.modality() == 'ONLINE'}">
                                                <span class="text-primary fw-medium"><i class="bi bi-camera-video me-1"></i>Trực tuyến</span>
                                            </c:when>
                                            <c:when test="${item.modality() == 'IN_PERSON'}">
                                                <span class="text-success fw-medium"><i class="bi bi-geo-alt me-1"></i>Trực tiếp</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span><c:out value="${item.modality()}"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <c:if test="${not empty item.location()}">
                                        <div class="text-secondary small mb-2">
                                            <i class="bi bi-geo-alt me-1"></i>Địa điểm: <c:out value="${item.location()}"/>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty item.meetingLink()}">
                                        <div class="mb-2">
                                            <a href="<c:out value='${item.meetingLink()}'/>" target="_blank" rel="noopener noreferrer"
                                               class="btn btn-sm btn-primary">
                                                <i class="bi bi-camera-video me-1"></i>Vào phòng họp
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty item.ticketCode()}">
                                        <div class="text-secondary small">
                                            <i class="bi bi-ticket-perforated me-1"></i>Yêu cầu liên quan:
                                            <a href="${ctx}/student/tickets/${item.ticketId()}"><c:out value="${item.ticketCode()}"/></a>
                                        </div>
                                    </c:if>
                                </div>
                                <div class="card-footer bg-transparent d-flex flex-wrap gap-2 align-items-center justify-content-between">
                                    <div class="d-flex gap-2">
                                        <a href="${ctx}/student/appointments/${item.id()}" class="btn btn-sm btn-outline-primary">
                                            <i class="bi bi-eye me-1"></i>Chi tiết
                                        </a>
                                        <a href="${ctx}/student/appointments/${item.id()}/ics" class="btn btn-sm btn-outline-secondary" title="Tải tệp lịch">
                                            <i class="bi bi-calendar-plus me-1"></i>Lịch .ics
                                        </a>
                                    </div>
                                    <div class="d-flex gap-2">
                                        <c:if test="${item.canReschedule()}">
                                            <a href="${ctx}/student/appointments/${item.id()}/reschedule" class="btn btn-sm btn-outline-warning">
                                                <i class="bi bi-clock-history me-1"></i>Đổi lịch
                                            </a>
                                        </c:if>
                                        <c:if test="${item.canCancel()}">
                                            <qa:confirmModal id="cancel-modal-${item.id()}"
                                                             title="Hủy lịch hẹn"
                                                             message="Bạn có chắc chắn muốn hủy lịch hẹn này? Tư vấn viên sẽ nhận được thông báo."
                                                             actionUrl="${ctx}/student/appointments/${item.id()}/cancel"
                                                             confirmLabel="Hủy lịch"
                                                             cssClass="btn-sm btn-outline-danger"
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

