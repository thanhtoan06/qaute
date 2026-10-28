<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Tổng quan sinh viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/dashboard.css">
</head>
<div class="qa-page-student-dashboard" data-qa-view="student-dashboard">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="vi_VN"/>
<jsp:useBean id="studentDashboardDate" class="java.util.Date"/>

<div class="page-body">
    <div class="container-xl">
        <qa:alert/>

        <%-- ==================== HERO BANNER ==================== --%>
        <section class="qa-dashboard-hero" aria-labelledby="student-welcome">
            <div class="qa-dashboard-hero-content">
                <span class="qa-dashboard-eyebrow">
                    <span class="qa-pulse-dot" aria-hidden="true"></span>
                    <i class="bi bi-mortarboard-fill me-1" aria-hidden="true"></i>Không gian sinh viên
                </span>
                <h1 id="student-welcome" class="qa-view-title" data-qa-page="student-dashboard">
                    <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-stars"></i></span>
                    Xin chào, <c:out value="${me.fullName}"/>!
                </h1>
                <p>
                    Trung tâm giải đáp thắc mắc và hỗ trợ học vụ một cửa của bạn. Theo dõi yêu cầu, lịch hẹn tư vấn và tra cứu thông tin nhanh chóng, chính xác.
                </p>
                <div class="qa-hero-badges">
                    <span class="qa-hero-badge"><i class="bi bi-lightning-charge-fill"></i>Theo dõi tiến độ thời gian thực</span>
                    <span class="qa-hero-badge"><i class="bi bi-shield-check"></i>Bảo mật và hỗ trợ chính thức</span>
                    <span class="qa-hero-badge"><i class="bi bi-compass-fill"></i>Tập trung vào việc cần xử lý ngay</span>
                </div>
                <div class="qa-dashboard-hero-actions">
                    <a href="${ctx}/student/tickets/new" class="btn btn-white shadow-sm">
                        <i class="bi bi-pencil-square me-1"></i>Tạo yêu cầu
                    </a>
                    <a href="${ctx}/student/chat" class="btn btn-outline-light">
                        <i class="bi bi-chat-dots-fill me-1"></i>Chat trực tuyến
                    </a>
                    <a href="${ctx}/student/appointments/book" class="btn btn-outline-light">
                        <i class="bi bi-calendar-plus-fill me-1"></i>Đặt lịch hẹn
                    </a>
                </div>
            </div>
            <div class="qa-dashboard-hero-aside" aria-hidden="true">
                <div class="qa-dashboard-hero-meta">
                    <span class="qa-dashboard-date"><i class="bi bi-calendar3 me-1"></i><fmt:formatDate value="${studentDashboardDate}" pattern="EEEE, dd/MM/yyyy" timeZone="Asia/Ho_Chi_Minh"/></span>
                    <span class="qa-dashboard-hero-mark"><i class="bi bi-mortarboard-fill"></i></span>
                </div>
                <div class="qa-dashboard-hero-visual">
                    <div class="qa-hero-note">
                        <div class="qa-hero-note-card">
                            <div class="qa-hero-note-title"><i class="bi bi-clipboard-check"></i>Ưu tiên hôm nay</div>
                            <p>Xử lý nhanh các yêu cầu đang chờ phản hồi và xem lịch hẹn sắp tới ngay bên dưới.</p>
                        </div>
                        <div class="qa-hero-note-card">
                            <div class="qa-hero-note-title"><i class="bi bi-journal-richtext"></i>Kho kiến thức</div>
                            <p>FAQ gợi ý được đặt gần vùng công việc để bạn tra cứu ngắn đường thao tác.</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <%-- ==================== 3 HIGHLIGHTS CARDS ==================== --%>
        <section class="qa-student-overview-strip" aria-label="Điểm nhấn hỗ trợ">
            <div class="qa-overview-card">
                <h3><i class="bi bi-send-check-fill"></i>Tạo & theo dõi yêu cầu</h3>
                <p>Khởi tạo yêu cầu mới, theo dõi người phụ trách và phản hồi ngay khi hệ thống cần thêm thông tin.</p>
                <div class="qa-overview-meta">
                    <span><i class="bi bi-ticket-detailed-fill"></i>Ticket tập trung</span>
                    <span><i class="bi bi-arrow-repeat"></i>Cập nhật liên tục</span>
                </div>
            </div>
            <div class="qa-overview-card">
                <h3><i class="bi bi-calendar2-week-fill"></i>Lịch hẹn rõ ràng</h3>
                <p>Phiên tư vấn sắp tới, hình thức tham gia và điểm vào phòng họp được ưu tiên hiển thị nổi bật hơn.</p>
                <div class="qa-overview-meta">
                    <span><i class="bi bi-clock-history"></i>Nhìn nhanh thời gian</span>
                    <span><i class="bi bi-camera-video-fill"></i>Online hoặc trực tiếp</span>
                </div>
            </div>
            <div class="qa-overview-card">
                <h3><i class="bi bi-life-preserver"></i>Trợ giúp đa kênh</h3>
                <p>Chat trực tuyến, gửi yêu cầu, đặt lịch và tra cứu FAQ luôn sẵn sàng đồng hành cùng học tập của bạn.</p>
                <div class="qa-overview-meta">
                    <span><i class="bi bi-headset"></i>Chat nhanh</span>
                    <span><i class="bi bi-search-heart-fill"></i>FAQ hữu ích</span>
                </div>
            </div>
        </section>

        <%-- ==================== ANNOUNCEMENTS ==================== --%>
        <div id="system-announcement-area" class="mb-3">
            <c:if test="${not empty announcements}">
                <c:forEach var="ann" items="${announcements}">
                    <div class="alert alert-info alert-dismissible qa-student-announcement" role="alert">
                        <div class="d-flex align-items-start">
                            <i class="bi bi-megaphone-fill me-3 fs-3 text-info"></i>
                            <div class="flex-fill">
                                <div class="d-flex align-items-center gap-2 mb-1 flex-wrap">
                                    <c:if test="${ann.pinned()}">
                                        <span class="badge bg-danger-lt"><i class="bi bi-pin-angle-fill me-1"></i>Đã ghim</span>
                                    </c:if>
                                    <h4 class="alert-title mb-0">
                                        <a href="/announcements/${ann.id()}" class="text-reset text-decoration-none">
                                            <c:out value="${ann.title()}"/>
                                        </a>
                                    </h4>
                                </div>
                                <c:if test="${not empty ann.summary()}">
                                    <div class="text-secondary small mb-1"><c:out value="${ann.summary()}"/></div>
                                </c:if>
                                <a href="/announcements/${ann.id()}" class="text-primary small fw-semibold">
                                    Xem chi tiết <i class="bi bi-arrow-right ms-1"></i>
                                </a>
                            </div>
                        </div>
                        <a class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></a>
                    </div>
                </c:forEach>
            </c:if>
        </div>

        <%-- ==================== KPI PROGRESS ==================== --%>
        <div class="qa-dashboard-section-label">
            <h2><i class="bi bi-graph-up-arrow" aria-hidden="true"></i>Tiến độ yêu cầu</h2>
            <span>Bốn trạng thái quan trọng nhất được làm rõ để bạn biết việc nào cần chú ý trước</span>
        </div>
        <div class="row row-cards qa-student-kpi-grid">
            <div class="col-12 col-sm-6 col-xl-3">
                <a href="${ctx}/student/tickets?status=NEW" class="card card-link card-link-pop text-reset h-100 qa-student-kpi qa-student-kpi--new">
                    <div class="card-body">
                        <div class="qa-student-kpi-header">
                            <div>
                                <div class="subheader text-azure">Mới tạo</div>
                                <span class="badge bg-azure-lt"><i class="bi bi-file-earmark-plus me-1"></i>MỚI</span>
                            </div>
                            <span class="qa-student-kpi-icon" aria-hidden="true"><i class="bi bi-file-earmark-plus-fill"></i></span>
                        </div>
                        <div class="h1 text-azure">${dashboard.getNewCount()}</div>
                        <div class="small">Các yêu cầu vừa được gửi và đang chờ hệ thống tiếp nhận xử lý.</div>
                        <div class="qa-student-kpi-foot"><i class="bi bi-arrow-up-right-circle-fill"></i>Xem danh sách mới tạo</div>
                    </div>
                </a>
            </div>
            <div class="col-12 col-sm-6 col-xl-3">
                <a href="${ctx}/student/tickets?status=IN_PROGRESS" class="card card-link card-link-pop text-reset h-100 qa-student-kpi qa-student-kpi--progress">
                    <div class="card-body">
                        <div class="qa-student-kpi-header">
                            <div>
                                <div class="subheader text-orange">Đang xử lý</div>
                                <span class="badge bg-orange-lt"><i class="bi bi-arrow-repeat me-1"></i>TIẾN ĐỘ</span>
                            </div>
                            <span class="qa-student-kpi-icon" aria-hidden="true"><i class="bi bi-arrow-repeat"></i></span>
                        </div>
                        <div class="h1 text-orange">${dashboard.getInProgressCount()}</div>
                        <div class="small">Tư vấn viên đang theo dõi và hỗ trợ, bạn có thể mở chi tiết để xem cập nhật.</div>
                        <div class="qa-student-kpi-foot"><i class="bi bi-chat-square-text-fill"></i>Theo dõi hỗ trợ đang diễn ra</div>
                    </div>
                </a>
            </div>
            <div class="col-12 col-sm-6 col-xl-3">
                <a href="${ctx}/student/tickets?status=WAITING_STUDENT" class="card card-link card-link-pop text-reset h-100 qa-student-kpi qa-student-kpi--waiting">
                    <div class="card-body">
                        <div class="qa-student-kpi-header">
                            <div>
                                <div class="subheader text-warning">Chờ tôi</div>
                                <span class="badge bg-yellow-lt"><i class="bi bi-hourglass-split me-1"></i>CHỜ TÔI</span>
                            </div>
                            <span class="qa-student-kpi-icon" aria-hidden="true"><i class="bi bi-hourglass-split"></i></span>
                        </div>
                        <div class="h1 text-warning">${dashboard.getWaitingStudentCount()}</div>
                        <div class="small">Những yêu cầu đang cần bạn phản hồi thêm thông tin hoặc xác nhận bước tiếp theo.</div>
                        <div class="qa-student-kpi-foot"><i class="bi bi-reply-fill"></i>Ưu tiên xử lý càng sớm càng tốt</div>
                    </div>
                </a>
            </div>
            <div class="col-12 col-sm-6 col-xl-3">
                <a href="${ctx}/student/tickets?status=RESOLVED" class="card card-link card-link-pop text-reset h-100 qa-student-kpi qa-student-kpi--resolved">
                    <div class="card-body">
                        <div class="qa-student-kpi-header">
                            <div>
                                <div class="subheader text-success">Đã giải quyết</div>
                                <span class="badge bg-green-lt"><i class="bi bi-check2-circle me-1"></i>XÁC NHẬN</span>
                            </div>
                            <span class="qa-student-kpi-icon" aria-hidden="true"><i class="bi bi-check2-circle"></i></span>
                        </div>
                        <div class="h1 text-success">${dashboard.getResolvedCount()}</div>
                        <div class="small">Các yêu cầu đã hoàn tất xử lý và sẵn sàng để bạn xác nhận hoặc đánh giá.</div>
                        <div class="qa-student-kpi-foot"><i class="bi bi-star-fill"></i>Hoàn tất đánh giá dịch vụ</div>
                    </div>
                </a>
            </div>
        </div>

        <%-- ==================== MAIN CONTENT ==================== --%>
        <div class="row g-3 qa-student-dashboard-grid">
            <div class="col-12 col-lg-8">
                <%-- Bảng yêu cầu cần sinh viên xử lý --%>
                <div class="card qa-student-panel qa-student-panel--actions">
                    <div class="card-header">
                        <div class="qa-student-panel-heading">
                            <span class="qa-student-panel-heading-icon qa-student-panel-heading-icon--warning"><i class="bi bi-exclamation-triangle-fill"></i></span>
                            <div>
                                <h3 class="card-title mb-1">Cần bạn xử lý</h3>
                                <div class="card-subtitle">Những yêu cầu đang chờ bạn phản hồi hoặc xác nhận hoàn tất</div>
                            </div>
                            <c:if test="${not empty dashboard.needMyAction()}">
                                <span class="badge bg-danger-lt ms-2 font-monospace fw-bold">${fn:length(dashboard.needMyAction())}</span>
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
                                                        <a href="${ctx}/student/tickets/${item.id()}" class="text-reset text-decoration-none">
                                                            <c:out value="${item.title()}"/>
                                                        </a>
                                                    </div>
                                                    <div class="text-secondary small mt-1">
                                                        <span><i class="bi bi-folder2-open me-1 text-primary"></i><c:out value="${item.categoryName()}"/></span>
                                                        <c:if test="${not empty item.assigneeName()}">
                                                            <span class="ms-2"><i class="bi bi-person-fill me-1 text-teal"></i><c:out value="${item.assigneeName()}"/></span>
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
                                                                <i class="bi bi-reply-fill me-1"></i>Phản hồi ngay
                                                            </a>
                                                        </c:when>
                                                        <c:when test="${item.status() == 'RESOLVED'}">
                                                            <a href="${ctx}/student/tickets/${item.id()}" class="btn btn-sm btn-success">
                                                                <i class="bi bi-check-circle-fill me-1"></i>Xác nhận & Đánh giá
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
                            <div class="card-body text-center qa-student-empty">
                                <div class="avatar avatar-md rounded-circle bg-green-lt mb-3">
                                    <i class="bi bi-check-lg fs-2"></i>
                                </div>
                                <h4 class="text-success mb-1">Không có việc cần xử lý</h4>
                                <p class="text-secondary mb-0">Tuyệt vời! Hiện tại không có yêu cầu nào đang chờ bạn phản hồi hoặc xác nhận hoàn tất.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <%-- Câu hỏi thường gặp gợi ý --%>
                <div class="card qa-student-panel qa-student-panel--faq">
                    <div class="card-header">
                        <div class="qa-student-panel-heading">
                            <span class="qa-student-panel-heading-icon qa-student-panel-heading-icon--yellow"><i class="bi bi-lightbulb-fill"></i></span>
                            <div>
                                <h3 class="card-title mb-1">Câu hỏi thường gặp gợi ý</h3>
                                <div class="card-subtitle">Dựa trên chuyên mục bạn từng quan tâm và những thắc mắc phổ biến nhất</div>
                            </div>
                        </div>
                        <a href="${ctx}/student/faq" class="btn btn-sm btn-ghost-secondary">
                            Xem tất cả <i class="bi bi-arrow-right ms-1"></i>
                        </a>
                    </div>
                    <c:choose>
                        <c:when test="${not empty dashboard.suggestedFaqs()}">
                            <div class="list-group list-group-flush">
                                <c:forEach var="faq" items="${dashboard.suggestedFaqs()}">
                                    <div class="list-group-item list-group-item-action">
                                        <div class="me-3">
                                            <div class="fw-semibold">
                                                <a href="${ctx}/faq/${faq.slug()}" class="text-reset text-decoration-none">
                                                    <c:out value="${faq.title()}"/>
                                                </a>
                                            </div>
                                            <c:if test="${not empty faq.summary()}">
                                                <div class="text-secondary small mb-2 text-truncate u-max-w-640px">
                                                    <c:out value="${faq.summary()}"/>
                                                </div>
                                            </c:if>
                                            <div class="d-flex align-items-center gap-3 small text-secondary">
                                                <c:if test="${not empty faq.categoryName()}">
                                                    <span class="badge bg-secondary-lt">
                                                        <i class="bi bi-folder2 me-1"></i><c:out value="${faq.categoryName()}"/>
                                                    </span>
                                                </c:if>
                                                <span><i class="bi bi-eye-fill me-1 text-muted"></i>${faq.viewCount()} lượt xem</span>
                                                <span><i class="bi bi-hand-thumbs-up-fill me-1 text-success"></i>${faq.helpfulCount()} hữu ích</span>
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
                            <div class="card-body text-center qa-student-empty">
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

            <div class="col-12 col-lg-4">
                <div class="qa-student-dashboard-side">
                    <%-- Lịch hẹn sắp tới --%>
                    <div class="card qa-student-panel qa-student-panel--appointment">
                        <div class="card-header">
                            <div class="qa-student-panel-heading">
                                <span class="qa-student-panel-heading-icon qa-student-panel-heading-icon--violet"><i class="bi bi-calendar2-event-fill"></i></span>
                                <div>
                                    <h3 class="card-title mb-1">Lịch hẹn sắp tới</h3>
                                    <div class="card-subtitle">Thời gian, hình thức và điểm vào cuộc hẹn</div>
                                </div>
                            </div>
                            <c:if test="${dashboard.nextAppointment() != null}">
                                <a href="${ctx}/student/appointments" class="btn btn-sm btn-ghost-secondary">
                                    Tất cả <i class="bi bi-arrow-right ms-1"></i>
                                </a>
                            </c:if>
                        </div>
                        <c:choose>
                            <c:when test="${dashboard.nextAppointment() != null}">
                                <div class="card-body">
                                    <div class="card bg-primary-lt p-2 border-0">
                                        <div class="fw-bold text-primary">
                                            <i class="bi bi-clock-fill me-1"></i>
                                            <time datetime="${dashboard.nextAppointment().startAt()}" data-qaute-datetime><c:out value="${dashboard.nextAppointment().startAt()}"/></time> – <c:out value="${dashboard.nextAppointment().endAt().toLocalTime()}"/>
                                        </div>
                                    </div>
                                    <h4 class="card-title">
                                        <a href="${ctx}/student/appointments/${dashboard.nextAppointment().id()}" class="text-reset">
                                            <c:out value="${dashboard.nextAppointment().topic()}"/>
                                        </a>
                                    </h4>
                                    <c:if test="${not empty dashboard.nextAppointment().categoryPath()}">
                                        <div class="text-secondary small">
                                            <i class="bi bi-tag-fill me-1 text-primary"></i><c:out value="${dashboard.nextAppointment().categoryPath()}"/>
                                        </div>
                                    </c:if>
                                    <div class="small">
                                        <i class="bi bi-person-badge-fill me-1 text-secondary"></i>Tư vấn viên:
                                        <strong><c:out value="${dashboard.nextAppointment().managerName()}"/></strong>
                                    </div>
                                    <div class="small">
                                        <i class="bi bi-broadcast me-1 text-secondary"></i>Hình thức:
                                        <c:choose>
                                            <c:when test="${dashboard.nextAppointment().modality() == 'ONLINE'}">
                                                <span class="badge bg-blue-lt"><i class="bi bi-camera-video-fill me-1"></i>Trực tuyến</span>
                                            </c:when>
                                            <c:when test="${dashboard.nextAppointment().modality() == 'IN_PERSON'}">
                                                <span class="badge bg-green-lt"><i class="bi bi-geo-alt-fill me-1"></i>Trực tiếp</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary-lt"><c:out value="${dashboard.nextAppointment().modality()}"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <c:if test="${not empty dashboard.nextAppointment().meetingLink()}">
                                        <a href="<c:out value='${dashboard.nextAppointment().meetingLink()}'/>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-primary w-100">
                                            <i class="bi bi-camera-video-fill me-1"></i>Vào phòng họp trực tuyến
                                        </a>
                                    </c:if>
                                    <c:if test="${not empty dashboard.nextAppointment().location()}">
                                        <div class="small text-secondary">
                                            <i class="bi bi-geo-alt-fill me-1 text-danger"></i>Địa điểm: <strong><c:out value="${dashboard.nextAppointment().location()}"/></strong>
                                        </div>
                                    </c:if>
                                    <div class="d-flex gap-2 mt-1">
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
                                <div class="card-body text-center qa-student-empty">
                                    <div class="avatar avatar-md rounded-circle bg-secondary-lt mb-3">
                                        <i class="bi bi-calendar-x fs-2"></i>
                                    </div>
                                    <h4 class="text-secondary mb-1">Không có lịch hẹn</h4>
                                    <p class="text-secondary small mb-3">Bạn chưa có lịch hẹn tư vấn nào sắp diễn ra.</p>
                                    <a href="${ctx}/student/appointments/book" class="btn btn-sm btn-outline-primary w-100">
                                        <i class="bi bi-calendar-plus-fill me-1"></i>Đặt lịch tư vấn mới
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <%-- Kênh hỗ trợ sinh viên --%>
                    <div class="card qa-student-panel qa-student-panel--support">
                        <div class="card-header">
                            <div class="qa-student-panel-heading">
                                <span class="qa-student-panel-heading-icon qa-student-panel-heading-icon--teal"><i class="bi bi-headset"></i></span>
                                <div>
                                    <h3 class="card-title mb-1">Kênh hỗ trợ sinh viên</h3>
                                    <div class="card-subtitle">Lối vào các dịch vụ hỗ trợ trực tuyến chính thức</div>
                                </div>
                            </div>
                        </div>
                        <div class="list-group list-group-flush">
                            <a href="${ctx}/student/chat" class="list-group-item list-group-item-action d-flex align-items-center">
                                <div class="avatar bg-teal-lt me-3">
                                    <i class="bi bi-chat-dots-fill fs-3"></i>
                                </div>
                                <div class="flex-fill">
                                    <div class="fw-semibold">Chat trực tuyến</div>
                                    <div class="text-secondary small">Trò chuyện với Chatbot hoặc tư vấn viên trong giờ hành chính</div>
                                </div>
                                <i class="bi bi-chevron-right text-secondary"></i>
                            </a>
                            <a href="${ctx}/student/tickets/new" class="list-group-item list-group-item-action d-flex align-items-center">
                                <div class="avatar bg-primary-lt me-3">
                                    <i class="bi bi-envelope-paper-fill fs-3"></i>
                                </div>
                                <div class="flex-fill">
                                    <div class="fw-semibold">Gửi yêu cầu tư vấn</div>
                                    <div class="text-secondary small">Gửi yêu cầu chi tiết kèm tệp tin tài liệu minh chứng</div>
                                </div>
                                <i class="bi bi-chevron-right text-secondary"></i>
                            </a>
                            <a href="${ctx}/student/appointments/book" class="list-group-item list-group-item-action d-flex align-items-center">
                                <div class="avatar bg-cyan-lt me-3">
                                    <i class="bi bi-calendar-check-fill fs-3"></i>
                                </div>
                                <div class="flex-fill">
                                    <div class="fw-semibold">Đặt lịch hẹn 1-1</div>
                                    <div class="text-secondary small">Gặp trực tiếp hoặc qua video call với chuyên viên tư vấn</div>
                                </div>
                                <i class="bi bi-chevron-right text-secondary"></i>
                            </a>
                            <a href="${ctx}/student/faq" class="list-group-item list-group-item-action d-flex align-items-center">
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
</div>
</div>
