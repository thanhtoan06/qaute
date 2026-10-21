<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Tổng quan sinh viên</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="page-header d-print-none">
    <div class="container-xl">
        <div class="row g-2 align-items-center">
            <div class="col">
                <div class="page-pretitle">Bảng điều khiển</div>
                <h2 class="page-title">
                    Xin chào, <c:out value="${me.fullName}"/>
                </h2>
            </div>
            <%-- 3 Nút thao tác nhanh --%>
            <div class="col-auto ms-auto d-flex flex-wrap gap-2">
                <a href="${ctx}/student/tickets/new" class="btn btn-primary shadow-sm">
                    <i class="bi bi-pencil-square me-1"></i>Tạo yêu cầu
                </a>
                <a href="${ctx}/student/chat" class="btn btn-teal shadow-sm">
                    <i class="bi bi-chat-dots me-1"></i>Chat trực tuyến
                </a>
                <a href="${ctx}/student/appointments/book" class="btn btn-outline-primary shadow-sm">
                    <i class="bi bi-calendar-plus me-1"></i>Đặt lịch hẹn
                </a>
            </div>
        </div>
    </div>
</div>

<div class="page-body">
    <div class="container-xl">
        <qa:alert/>

        <%-- Khu vực dành riêng cho thông báo chung / banner hệ thống (T1-62) --%>
        <div id="system-announcement-area" class="mb-3">
            <c:if test="${not empty announcements}">
                <c:forEach var="ann" items="${announcements}">
                    <div class="alert alert-info alert-dismissible shadow-sm mb-2" role="alert">
                        <div class="d-flex align-items-start">
                            <i class="bi bi-megaphone-fill me-3 fs-3 text-info"></i>
                            <div>
                                <h4 class="alert-title mb-1"><c:out value="${ann.title}"/></h4>
                                <div class="text-secondary"><c:out value="${not empty ann.summary ? ann.summary : ann.content}" escapeXml="false"/></div>
                            </div>
                        </div>
                        <a class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></a>
                    </div>
                </c:forEach>
            </c:if>
        </div>

        <%-- Khối / badge thông báo chưa đọc --%>
        <c:if test="${dashboard.unreadNotifications() > 0}">
            <div class="alert alert-warning alert-important d-flex align-items-center justify-content-between mb-3 shadow-sm" role="alert">
                <div class="d-flex align-items-center">
                    <i class="bi bi-bell-fill fs-2 me-3"></i>
                    <div>
                        <strong>Thông báo mới!</strong> Bạn có <strong>${dashboard.unreadNotifications()}</strong> thông báo chưa đọc.
                    </div>
                </div>
                <div>
                    <a href="${ctx}/student/notifications" class="btn btn-sm btn-light text-nowrap">
                        Xem thông báo <i class="bi bi-arrow-right ms-1"></i>
                    </a>
                </div>
            </div>
        </c:if>

        <%-- Hàng thẻ số liệu thống kê yêu cầu --%>
        <div class="row row-cards mb-3">
            <div class="col-6 col-md-3">
                <a href="${ctx}/student/tickets?status=NEW" class="card card-link card-link-pop text-reset h-100 shadow-sm border-0 border-start border-azure border-3">
                    <div class="card-body">
                        <div class="d-flex align-items-center">
                            <div class="subheader text-azure">Mới tạo</div>
                            <div class="ms-auto lh-1">
                                <span class="badge bg-azure-lt"><i class="bi bi-file-earmark-plus me-1"></i>MỚI</span>
                            </div>
                        </div>
                        <div class="h1 mb-1 mt-2 text-azure">${dashboard.getNewCount()}</div>
                        <div class="text-secondary small">Chờ tiếp nhận xử lý</div>
                    </div>
                </a>
            </div>
            <div class="col-6 col-md-3">
                <a href="${ctx}/student/tickets?status=IN_PROGRESS" class="card card-link card-link-pop text-reset h-100 shadow-sm border-0 border-start border-orange border-3">
                    <div class="card-body">
                        <div class="d-flex align-items-center">
                            <div class="subheader text-orange">Đang xử lý</div>
                            <div class="ms-auto lh-1">
                                <span class="badge bg-orange-lt"><i class="bi bi-arrow-repeat me-1"></i>TIẾN ĐỘ</span>
                            </div>
                        </div>
                        <div class="h1 mb-1 mt-2 text-orange">${dashboard.getInProgressCount()}</div>
                        <div class="text-secondary small">Tư vấn viên đang hỗ trợ</div>
                    </div>
                </a>
            </div>
            <div class="col-6 col-md-3">
                <a href="${ctx}/student/tickets?status=WAITING_STUDENT" class="card card-link card-link-pop text-reset h-100 shadow-sm border-0 border-start border-warning border-3">
                    <div class="card-body">
                        <div class="d-flex align-items-center">
                            <div class="subheader text-warning">Chờ tôi</div>
                            <div class="ms-auto lh-1">
                                <span class="badge bg-yellow-lt"><i class="bi bi-hourglass-split me-1"></i>CHỜ TÔI</span>
                            </div>
                        </div>
                        <div class="h1 mb-1 mt-2 text-warning">${dashboard.getWaitingStudentCount()}</div>
                        <div class="text-secondary small">Cần bạn phản hồi</div>
                    </div>
                </a>
            </div>
            <div class="col-6 col-md-3">
                <a href="${ctx}/student/tickets?status=RESOLVED" class="card card-link card-link-pop text-reset h-100 shadow-sm border-0 border-start border-success border-3">
                    <div class="card-body">
                        <div class="d-flex align-items-center">
                            <div class="subheader text-success">Đã giải quyết</div>
                            <div class="ms-auto lh-1">
                                <span class="badge bg-green-lt"><i class="bi bi-check2-circle me-1"></i>XÁC NHẬN</span>
                            </div>
                        </div>
                        <div class="h1 mb-1 mt-2 text-success">${dashboard.getResolvedCount()}</div>
                        <div class="text-secondary small">Chờ xác nhận & đánh giá</div>
                    </div>
                </a>
            </div>
        </div>

        <div class="row g-3">
            <%-- Cột chính bên trái: Cần bạn xử lý & FAQ gợi ý --%>
            <div class="col-12 col-lg-8">
                <%-- Khối Cần bạn xử lý --%>
                <div class="card mb-3 shadow-sm">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <div class="d-flex align-items-center">
                            <h3 class="card-title mb-0">
                                <i class="bi bi-exclamation-triangle-fill text-warning me-2"></i>Cần bạn xử lý
                            </h3>
                            <c:if test="${not empty dashboard.needMyAction()}">
                                <span class="badge bg-danger-lt ms-2">${fn:length(dashboard.needMyAction())}</span>
                            </c:if>
                        </div>
                        <a href="${ctx}/student/tickets" class="btn btn-sm btn-ghost-secondary">
                            Xem tất cả <i class="bi bi-arrow-right ms-1"></i>
                        </a>
                    </div>
                    <c:choose>
                        <c:when test="${not empty dashboard.needMyAction()}">
                            <div class="table-responsive">
                                <table class="table table-vcenter card-table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Mã</th>
                                            <th>Tiêu đề & Chuyên mục</th>
                                            <th>Trạng thái</th>
                                            <th class="text-end">Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${dashboard.needMyAction()}">
                                            <tr>
                                                <td class="text-nowrap">
                                                    <a href="${ctx}/student/tickets/${item.id()}" class="font-monospace fw-bold text-reset">
                                                        <c:out value="${item.code()}"/>
                                                    </a>
                                                </td>
                                                <td>
                                                    <div class="fw-semibold">
                                                        <a href="${ctx}/student/tickets/${item.id()}" class="text-reset">
                                                            <c:out value="${item.title()}"/>
                                                        </a>
                                                    </div>
                                                    <div class="text-secondary small">
                                                        <i class="bi bi-folder2-open me-1"></i><c:out value="${item.categoryName()}"/>
                                                        <c:if test="${not empty item.assigneeName()}">
                                                            <span class="ms-2"><i class="bi bi-person me-1"></i><c:out value="${item.assigneeName()}"/></span>
                                                        </c:if>
                                                    </div>
                                                </td>
                                                <td class="text-nowrap">
                                                    <qa:statusBadge status="${item.status().name()}"/>
                                                </td>
                                                <td class="text-end text-nowrap">
                                                    <c:choose>
                                                        <c:when test="${item.status() == 'WAITING_STUDENT'}">
                                                            <a href="${ctx}/student/tickets/${item.id()}" class="btn btn-sm btn-warning">
                                                                <i class="bi bi-reply me-1"></i>Phản hồi ngay
                                                            </a>
                                                        </c:when>
                                                        <c:when test="${item.status() == 'RESOLVED'}">
                                                            <a href="${ctx}/student/tickets/${item.id()}" class="btn btn-sm btn-success">
                                                                <i class="bi bi-check-circle me-1"></i>Xác nhận & Đánh giá
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="${ctx}/student/tickets/${item.id()}" class="btn btn-sm btn-outline-primary">
                                                                Chi tiết <i class="bi bi-chevron-right ms-1"></i>
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="card-body text-center py-4">
                                <div class="avatar avatar-md rounded-circle bg-green-lt mb-3">
                                    <i class="bi bi-check-lg fs-2"></i>
                                </div>
                                <h4 class="text-success mb-1">Không có việc cần xử lý</h4>
                                <p class="text-secondary mb-0">Tuyệt vời! Hiện tại không có yêu cầu nào đang chờ bạn phản hồi hoặc xác nhận hoàn tất.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <%-- Khối FAQ gợi ý --%>
                <div class="card shadow-sm">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <div>
                            <h3 class="card-title mb-0">
                                <i class="bi bi-lightbulb text-yellow me-2"></i>Câu hỏi thường gặp gợi ý
                            </h3>
                            <div class="card-subtitle mt-1">Dựa trên các chuyên mục bạn từng quan tâm hoặc câu hỏi phổ biến</div>
                        </div>
                        <a href="${ctx}/student/faq" class="btn btn-sm btn-ghost-secondary">
                            Xem tất cả <i class="bi bi-arrow-right ms-1"></i>
                        </a>
                    </div>
                    <c:choose>
                        <c:when test="${not empty dashboard.suggestedFaqs()}">
                            <div class="list-group list-group-flush">
                                <c:forEach var="faq" items="${dashboard.suggestedFaqs()}">
                                    <div class="list-group-item list-group-item-action d-flex align-items-start justify-content-between p-3">
                                        <div class="me-3">
                                            <div class="fw-semibold mb-1">
                                                <a href="${ctx}/faq/${faq.slug()}" class="text-reset">
                                                    <c:out value="${faq.title()}"/>
                                                </a>
                                            </div>
                                            <c:if test="${not empty faq.summary()}">
                                                <div class="text-secondary small mb-2 text-truncate" style="max-width: 600px;">
                                                    <c:out value="${faq.summary()}"/>
                                                </div>
                                            </c:if>
                                            <div class="d-flex align-items-center gap-3 small text-secondary">
                                                <c:if test="${not empty faq.categoryName()}">
                                                    <span class="badge bg-secondary-lt">
                                                        <i class="bi bi-folder2 me-1"></i><c:out value="${faq.categoryName()}"/>
                                                    </span>
                                                </c:if>
                                                <span><i class="bi bi-eye me-1"></i>${faq.viewCount()} lượt xem</span>
                                                <span><i class="bi bi-hand-thumbs-up me-1"></i>${faq.helpfulCount()} hữu ích</span>
                                            </div>
                                        </div>
                                        <div>
                                            <a href="${ctx}/faq/${faq.slug()}" class="btn btn-sm btn-ghost-primary text-nowrap">
                                                Xem <i class="bi bi-chevron-right ms-1"></i>
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="card-body text-center py-4">
                                <div class="avatar avatar-md rounded-circle bg-secondary-lt mb-3">
                                    <i class="bi bi-question-circle fs-2"></i>
                                </div>
                                <h4 class="text-secondary mb-1">Chưa có câu hỏi gợi ý</h4>
                                <p class="text-secondary small mb-3">Tra cứu nhanh câu trả lời cho các thắc mắc thường gặp về đào tạo, học phí, học vụ.</p>
                                <a href="${ctx}/student/faq" class="btn btn-sm btn-outline-primary">
                                    Khám phá kho FAQ
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <%-- Cột phụ bên phải: Lịch hẹn sắp tới & Trung tâm trợ giúp --%>
            <div class="col-12 col-lg-4">
                <%-- Card Lịch hẹn sắp tới --%>
                <div class="card mb-3 shadow-sm">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h3 class="card-title mb-0">
                            <i class="bi bi-calendar2-event text-primary me-2"></i>Lịch hẹn sắp tới
                        </h3>
                        <c:if test="${dashboard.nextAppointment() != null}">
                            <a href="${ctx}/student/appointments" class="btn btn-sm btn-ghost-secondary">
                                Tất cả <i class="bi bi-arrow-right ms-1"></i>
                            </a>
                        </c:if>
                    </div>
                    <c:choose>
                        <c:when test="${dashboard.nextAppointment() != null}">
                            <div class="card-body">
                                <div class="card bg-primary-lt p-2 mb-3 border-0">
                                    <div class="fw-bold text-primary">
                                        <i class="bi bi-clock me-1"></i>
                                        <c:out value="${dashboard.nextAppointment().startAt()}"/> – <c:out value="${dashboard.nextAppointment().endAt().toLocalTime()}"/>
                                    </div>
                                </div>
                                <h4 class="card-title mb-2">
                                    <a href="${ctx}/student/appointments/${dashboard.nextAppointment().id()}" class="text-reset">
                                        <c:out value="${dashboard.nextAppointment().topic()}"/>
                                    </a>
                                </h4>
                                <c:if test="${not empty dashboard.nextAppointment().categoryPath()}">
                                    <div class="text-secondary small mb-2">
                                        <i class="bi bi-tag me-1"></i><c:out value="${dashboard.nextAppointment().categoryPath()}"/>
                                    </div>
                                </c:if>
                                <div class="small mb-2">
                                    <i class="bi bi-person me-1 text-secondary"></i>Tư vấn viên:
                                    <strong><c:out value="${dashboard.nextAppointment().managerName()}"/></strong>
                                </div>
                                <div class="small mb-3">
                                    <i class="bi bi-broadcast me-1 text-secondary"></i>Hình thức:
                                    <c:choose>
                                        <c:when test="${dashboard.nextAppointment().modality() == 'ONLINE'}">
                                            <span class="badge bg-blue-lt"><i class="bi bi-camera-video me-1"></i>Trực tuyến</span>
                                        </c:when>
                                        <c:when test="${dashboard.nextAppointment().modality() == 'IN_PERSON'}">
                                            <span class="badge bg-green-lt"><i class="bi bi-geo-alt me-1"></i>Trực tiếp</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary-lt"><c:out value="${dashboard.nextAppointment().modality()}"/></span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <c:if test="${not empty dashboard.nextAppointment().meetingLink()}">
                                    <a href="<c:out value='${dashboard.nextAppointment().meetingLink()}'/>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-primary w-100 mb-2">
                                        <i class="bi bi-camera-video me-1"></i>Vào phòng họp trực tuyến
                                    </a>
                                </c:if>
                                <c:if test="${not empty dashboard.nextAppointment().location()}">
                                    <div class="small text-secondary mb-3">
                                        <i class="bi bi-geo-alt me-1"></i>Địa điểm: <strong><c:out value="${dashboard.nextAppointment().location()}"/></strong>
                                    </div>
                                </c:if>
                                <div class="d-flex gap-2">
                                    <a href="${ctx}/student/appointments/${dashboard.nextAppointment().id()}" class="btn btn-sm btn-outline-primary w-100">
                                        Chi tiết
                                    </a>
                                    <a href="${ctx}/student/appointments" class="btn btn-sm btn-ghost-secondary w-100">
                                        Lịch của tôi
                                    </a>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="card-body text-center py-4">
                                <div class="avatar avatar-md rounded-circle bg-secondary-lt mb-3">
                                    <i class="bi bi-calendar-x fs-2"></i>
                                </div>
                                <h4 class="text-secondary mb-1">Không có lịch hẹn</h4>
                                <p class="text-secondary small mb-3">Bạn chưa có lịch hẹn tư vấn nào sắp diễn ra.</p>
                                <a href="${ctx}/student/appointments/book" class="btn btn-sm btn-outline-primary w-100">
                                    <i class="bi bi-calendar-plus me-1"></i>Đặt lịch tư vấn mới
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <%-- Card Hướng dẫn & Kênh hỗ trợ nhanh --%>
                <div class="card shadow-sm">
                    <div class="card-header">
                        <h3 class="card-title mb-0">
                            <i class="bi bi-headset text-azure me-2"></i>Kênh hỗ trợ sinh viên
                        </h3>
                    </div>
                    <div class="list-group list-group-flush">
                        <a href="${ctx}/student/chat" class="list-group-item list-group-item-action d-flex align-items-center p-3">
                            <div class="avatar bg-teal-lt me-3">
                                <i class="bi bi-chat-dots fs-3"></i>
                            </div>
                            <div class="flex-fill">
                                <div class="fw-semibold">Chat trực tuyến</div>
                                <div class="text-secondary small">Trò chuyện với Chatbot hoặc tư vấn viên trong giờ hành chính</div>
                            </div>
                            <i class="bi bi-chevron-right text-secondary"></i>
                        </a>
                        <a href="${ctx}/student/tickets/new" class="list-group-item list-group-item-action d-flex align-items-center p-3">
                            <div class="avatar bg-primary-lt me-3">
                                <i class="bi bi-envelope-paper fs-3"></i>
                            </div>
                            <div class="flex-fill">
                                <div class="fw-semibold">Gửi yêu cầu tư vấn</div>
                                <div class="text-secondary small">Gửi yêu cầu chi tiết kèm tệp tin tài liệu minh chứng</div>
                            </div>
                            <i class="bi bi-chevron-right text-secondary"></i>
                        </a>
                        <a href="${ctx}/student/appointments/book" class="list-group-item list-group-item-action d-flex align-items-center p-3">
                            <div class="avatar bg-cyan-lt me-3">
                                <i class="bi bi-calendar-check fs-3"></i>
                            </div>
                            <div class="flex-fill">
                                <div class="fw-semibold">Đặt lịch hẹn 1-1</div>
                                <div class="text-secondary small">Gặp trực tiếp hoặc qua video call với chuyên viên tư vấn</div>
                            </div>
                            <i class="bi bi-chevron-right text-secondary"></i>
                        </a>
                        <a href="${ctx}/student/faq" class="list-group-item list-group-item-action d-flex align-items-center p-3">
                            <div class="avatar bg-yellow-lt me-3">
                                <i class="bi bi-search fs-3"></i>
                            </div>
                            <div class="flex-fill">
                                <div class="fw-semibold">Tra cứu FAQ</div>
                                <div class="text-secondary small">Tìm câu trả lời nhanh cho các vấn đề phổ biến</div>
                            </div>
                            <i class="bi bi-chevron-right text-secondary"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
