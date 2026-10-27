<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="isHistoryView" value="${param.view eq 'history'}"/>

<c:choose>
    <c:when test="${isHistoryView}">

<head>
    <title>Lịch sử tư vấn trực tuyến | QAUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/chat.css">
</head>
    </c:when>
    <c:otherwise>
        <title>Tư vấn trực tuyến | QAUTE</title>
    </c:otherwise>
</c:choose>

<div class="qa-page-student-chat" data-qa-view="student-chat">


<div class="row justify-content-center">
    <div class="col-12 col-xl-10">

        <!-- 1. HERO BANNER (Dành riêng cho từng chế độ: Tư vấn trực tuyến HOẶC Lịch sử) -->
        <c:choose>
            <c:when test="${isHistoryView}">
                <!-- BANNER: LỊCH SỬ TƯ VẤN (Trang ?view=history) -->
                <div class="student-hero-banner">
                    <div class="hero-content-wrap d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3">
                        <div>
                            <div class="hero-eyebrow-pill">
                                <span class="hero-eyebrow-dot"></span>
                                <span>Lịch Sử Hội Thoại • Trợ Lý Ảo & Tư Vấn Viên</span>
                            </div>
                            <h1 class="hero-title">
                                <i class="bi bi-clock-history text-info me-2"></i>Lịch Sử Tư Vấn Trực Tuyến
                            </h1>
                            <p class="hero-subtitle">
                                Tra cứu và xem lại toàn bộ nội dung các cuộc trao đổi trực tuyến đã kết thúc, theo dõi giải pháp hướng dẫn hoặc chuyển thành yêu cầu hỗ trợ (ticket) nếu cần.
                            </p>
                        </div>
                        <div class="d-flex flex-wrap align-items-center gap-2">
                            <a href="${ctx}/student/chat" class="hero-action-btn btn-active-white">
                                <i class="bi bi-chat-dots-fill text-primary"></i>
                                <span>Bắt đầu tư vấn mới</span>
                            </a>
                            <a href="${ctx}/student/tickets/new" class="hero-action-btn">
                                <i class="bi bi-ticket-perforated"></i>
                                <span>Gửi Ticket hỗ trợ</span>
                            </a>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <!-- BANNER: TƯ VẤN TRỰC TUYẾN (Trang /student/chat) -->
                <div class="student-hero-banner">
                    <div class="hero-content-wrap d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3">
                        <div>
                            <div class="hero-eyebrow-pill">
                                <span class="hero-eyebrow-dot"></span>
                                <span>Kênh Tư Vấn Trực Tuyến • Hỗ Trợ Sinh Viên 24/7</span>
                            </div>
                            <h1 class="hero-title">
                                <i class="bi bi-headset text-info me-2"></i>Tư Vấn Trực Tuyến Sinh Viên
                            </h1>
                            <p class="hero-subtitle">
                                Kết nối nhanh với trợ lý ảo QAUTE để giải đáp tức thì các thắc mắc phổ biến hoặc xếp hàng kết nối trực tiếp với đội ngũ tư vấn viên phụ trách.
                            </p>
                        </div>
                        <div class="d-flex flex-wrap align-items-center gap-2">
                            <a href="${ctx}/student/chat?view=history" class="hero-action-btn">
                                <i class="bi bi-clock-history"></i>
                                <span>Xem lịch sử tư vấn</span>
                            </a>
                            <a href="${ctx}/student/tickets/new" class="hero-action-btn">
                                <i class="bi bi-ticket-perforated"></i>
                                <span>Gửi Ticket hỗ trợ</span>
                            </a>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

        <qa:alert/>

        <!-- Hidden Tab Controls for chat-student.js compatibility -->
        <ul class="d-none" id="chatMainTabs">
            <li class="nav-item">
                <a class="nav-link ${not isHistoryView ? 'active' : ''}" id="tabLiveChat" href="javascript:void(0)">Tư vấn trực tuyến</a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${isHistoryView ? 'active' : ''}" id="tabHistory" href="javascript:void(0)">Lịch sử tư vấn trực tuyến</a>
            </li>
        </ul>

        <!-- =======================================================
             CHẾ ĐỘ 1: TƯ VẤN TRỰC TUYẾN (Ẩn hoàn toàn khi view=history)
             ======================================================= -->
        <div id="chatLiveView" class="${isHistoryView ? 'd-none' : ''}">
            <div class="card shadow-sm border-0">
                <div class="card-header d-flex align-items-center justify-content-between">
                    <div class="d-flex align-items-center gap-3">
                        <span class="avatar avatar-md bg-white text-primary rounded-3 shadow-sm u-width-44px u-height-44px u-display-inline-flex u-align-items-center u-justify-content-center u-text-size-1-25rem">
                            <i class="bi bi-headset"></i>
                        </span>
                        <div>
                            <h3 class="card-title text-white mb-0" id="chatHeaderTitle">Kênh tư vấn trực tuyến</h3>
                            <div class="text-white-50 small mt-1" id="chatHeaderSubtitle">Hỗ trợ học tập, học vụ và đời sống sinh viên</div>
                        </div>
                    </div>
                    <span class="badge bg-white text-primary rounded-pill px-3 py-2 fw-bold shadow-sm" id="chatHeaderBadge">Sẵn sàng</span>
                </div>

                <%-- Loading state while checking current session --%>
                <div id="chatStateLoading" class="card-body text-center py-5">
                    <div class="spinner-border text-primary mb-3 u-width-3rem u-height-3rem" role="status"></div>
                    <div class="text-secondary fw-bold fs-6">Đang kiểm tra phiên tư vấn của bạn...</div>
                    <div class="text-muted small mt-1">Vui lòng chờ trong giây lát</div>
                </div>

                <%-- (A) Form bắt đầu --%>
                <div id="chatStateStart" class="card-body p-4 u-display-none">
                    <div class="alert alert-info border-0 rounded-4 d-flex align-items-center p-3 mb-4 u-bg-linear-gradient-135deg-e0f2fe-0-f0f9ff-100">
                        <i class="bi bi-info-circle-fill text-info fs-2 me-3"></i>
                        <div class="text-secondary small">
                            <strong class="text-dark d-block">Hướng dẫn kết nối tư vấn:</strong>
                            Hãy chọn đúng chuyên mục và mô tả ngắn gọn câu hỏi. Hệ thống sẽ tự động tìm kiếm các bài viết hướng dẫn phù hợp để bạn tham khảo ngay trước khi chuyển tiếp tới tư vấn viên.
                        </div>
                    </div>

                    <form id="formStartChat">
                        <div class="mb-3">
                            <label class="form-label required" for="chatCategoryId">Chuyên mục cần tư vấn</label>
                            <select id="chatCategoryId" class="form-select form-select-lg" required>
                                <option value="">-- Chọn chuyên mục cần tư vấn --</option>
                                <c:forEach var="department" items="${tree}">
                                    <optgroup label="<c:out value='${department.name()}'/>">
                                        <c:forEach var="root" items="${department.categories()}">
                                            <option value="${root.id()}"><c:out value="${root.name()}"/></option>
                                            <c:forEach var="child" items="${root.children()}">
                                                <option value="${child.id()}">&nbsp;&nbsp;└ <c:out value="${child.name()}"/></option>
                                            </c:forEach>
                                        </c:forEach>
                                    </optgroup>
                                </c:forEach>
                            </select>
                            <small class="form-hint text-muted mt-1 d-block">
                                <i class="bi bi-diagram-3 me-1"></i>Chọn đúng chuyên mục giúp chuyển câu hỏi tới đúng tư vấn viên chuyên trách.
                            </small>
                        </div>

                        <div class="mb-4">
                            <label class="form-label required" for="chatQuestion">Câu hỏi hoặc vấn đề của bạn</label>
                            <textarea id="chatQuestion" class="form-control" rows="4" minlength="5" maxlength="500"
                                      placeholder="Mô tả ngắn gọn thắc mắc của bạn (từ 5 đến 500 ký tự)..." required></textarea>
                            <div class="d-flex justify-content-between align-items-center mt-2">
                                <small class="text-muted"><i class="bi bi-chat-left-dots me-1"></i>Tối thiểu 5 ký tự, tối đa 500 ký tự</small>
                                <span class="badge bg-light text-secondary border rounded-pill" id="chatQuestionCount">0 / 500</span>
                            </div>
                        </div>

                        <div class="d-grid">
                            <button type="submit" id="btnStartChat" class="btn btn-primary btn-lg justify-content-center py-3 shadow">
                                <i class="bi bi-send-fill me-2"></i> Bắt đầu tư vấn ngay
                            </button>
                        </div>
                    </form>
                </div>

                <%-- (B) Bot gợi ý FAQ --%>
                <div id="chatStateBot" class="card-body p-4 u-display-none">
                    <div class="d-flex align-items-center gap-3 mb-4 pb-3 border-bottom">
                        <span class="avatar avatar-lg bg-info-lt text-info rounded-circle shadow-sm u-width-52px u-height-52px u-display-inline-flex u-align-items-center u-justify-content-center u-text-size-1-6rem">
                            <i class="bi bi-robot"></i>
                        </span>
                        <div>
                            <h4 class="mb-1 text-dark fw-bold">Trợ lý ảo QAUTE</h4>
                            <div class="text-secondary small">Tự động gợi ý câu trả lời từ kho tri thức hỏi đáp nhà trường</div>
                        </div>
                    </div>

                    <div class="bg-light p-3 rounded-4 mb-4 border">
                        <div class="text-secondary small mb-1 fw-bold">
                            <i class="bi bi-question-circle me-1 text-primary"></i>Câu hỏi của bạn:
                        </div>
                        <div class="fw-semibold text-dark fs-6" id="botQuestionText">—</div>
                    </div>

                    <div class="mb-4">
                        <div class="fw-bold text-dark mb-3 d-flex align-items-center">
                            <i class="bi bi-lightbulb-fill text-warning fs-5 me-2"></i>Các bài viết hướng dẫn có thể giải đáp ngay thắc mắc:
                        </div>
                        <div id="botSuggestionsList" class="list-group rounded-3 shadow-sm border overflow-hidden">
                            <%-- Dynamically populated via JS --%>
                        </div>
                        <div id="botNoSuggestions" class="text-secondary small p-3 bg-light rounded-3 border u-display-none">
                            <i class="bi bi-info-circle me-1"></i>Chưa tìm thấy bài viết FAQ trực tiếp cho câu hỏi này. Bạn có thể kết nối ngay với tư vấn viên bên dưới.
                        </div>
                    </div>

                    <div class="d-flex flex-wrap gap-2 justify-content-between pt-3 border-top">
                        <button type="button" id="btnResolved" class="btn btn-outline-success">
                            <i class="bi bi-check-circle-fill me-1"></i> Đã giải quyết được rồi
                        </button>
                        <button type="button" id="btnRequestAgent" class="btn btn-primary">
                            <i class="bi bi-headset me-1"></i> Kết nối gặp tư vấn viên
                        </button>
                    </div>

                    <%-- Cảm ơn khi đã giải quyết --%>
                    <div id="botResolvedSuccess" class="alert alert-success mt-4 p-4 rounded-4 text-center shadow-sm u-display-none">
                        <i class="bi bi-emoji-smile-fill fs-1 text-success d-block mb-2"></i>
                        <h4 class="fw-bold text-success mb-1">Rất vui vì đã hỗ trợ được bạn!</h4>
                        <div class="small text-secondary mb-3">Phiên tư vấn đã kết thúc thành công. Chúc bạn một ngày học tập nhiều niềm vui.</div>
                        <button type="button" id="btnStartNewAfterResolved" class="btn btn-sm btn-outline-success rounded-pill px-4">
                            <i class="bi bi-plus-lg me-1"></i>Bắt đầu câu hỏi mới
                        </button>
                    </div>
                </div>

                <%-- (C) Hàng chờ --%>
                <div id="chatStateQueue" class="card-body p-4 text-center u-display-none">
                    <%-- View khi đang chờ trong hàng --%>
                    <div id="queueWaitingView" class="py-4">
                        <div class="spinner-border text-primary mb-3 u-width-4rem u-height-4rem" role="status">
                            <span class="visually-hidden">Đang chờ...</span>
                        </div>
                        <h3 class="mb-2 fw-bold text-dark">Đang tìm tư vấn viên phụ trách...</h3>
                        <p class="text-secondary small mb-4">Vui lòng giữ cửa sổ này mở, hệ thống sẽ kết nối ngay khi đến lượt bạn.</p>

                        <div class="d-inline-block p-4 rounded-4 mb-4 shadow-sm border u-bg-linear-gradient-135deg-e0f2fe-0-f0f9ff-100 u-min-w-200px">
                            <div class="text-secondary small text-uppercase fw-bold letter-spacing-1">Vị trí của bạn</div>
                            <div class="display-4 fw-extrabold text-primary my-1">
                                #<span id="queuePositionNum">1</span>
                            </div>
                            <div class="small text-secondary fw-medium">trong danh sách hàng chờ</div>
                        </div>

                        <div>
                            <button type="button" id="btnCancelQueue" class="btn btn-outline-danger px-4 rounded-pill">
                                <i class="bi bi-x-circle me-1"></i> Hủy yêu cầu tư vấn
                            </button>
                        </div>
                    </div>

                    <%-- View khi ngoài giờ hoặc hết hạn --%>
                    <div id="queueUnavailableView" class="py-4 u-display-none">
                        <div class="avatar avatar-xl bg-warning-lt rounded-circle mx-auto mb-3 u-width-64px u-height-64px u-display-inline-flex u-align-items-center u-justify-content-center">
                            <i class="bi bi-clock-history fs-1 text-warning"></i>
                        </div>
                        <h3 class="text-warning mb-2 fw-bold" id="unavailableTitle">Hiện không có tư vấn viên trực</h3>
                        <p class="text-secondary mb-4 mx-auto u-max-w-520px" id="unavailableMsg">
                            Hiện tại ngoài giờ làm việc hoặc tất cả tư vấn viên thuộc chuyên mục này đang bận. Bạn có thể chuyển câu hỏi thành yêu cầu hỗ trợ (ticket) để nhận phản hồi chi tiết qua email.
                        </p>

                        <div class="d-flex justify-content-center gap-2">
                            <a id="btnConvertToTicket" href="${ctx}/student/tickets/new" class="btn btn-primary px-4">
                                <i class="bi bi-ticket-perforated me-1"></i> Chuyển thành yêu cầu tư vấn (Ticket)
                            </a>
                            <button type="button" id="btnBackToStart" class="btn btn-outline-secondary px-4">
                                Quay lại
                            </button>
                        </div>
                    </div>
                </div>

                <%-- (D) Phòng chat Realtime --%>
                <div id="chatStateRoom" class="qa-chat-room u-display-none">
                    <div class="p-3 bg-light border-bottom d-flex align-items-center justify-content-between">
                        <div class="d-flex align-items-center gap-2">
                            <span class="avatar avatar-sm bg-success-lt text-success rounded-circle" id="roomAdvisorAvatar">
                                <i class="bi bi-person-badge"></i>
                            </span>
                            <div>
                                <div class="fw-bold text-dark" id="roomAdvisorName">Tư vấn viên</div>
                                <div class="small text-success d-flex align-items-center gap-1" id="roomAdvisorStatus">
                                    <span class="badge badge-dot bg-success"></span> Đang trực tuyến
                                </div>
                            </div>
                        </div>
                        <div class="d-flex gap-2">
                            <button type="button" id="btnRoomEndChat" class="btn btn-sm btn-outline-danger rounded-pill px-3">
                                <i class="bi bi-box-arrow-right me-1"></i> Kết thúc chat
                            </button>
                        </div>
                    </div>

                    <%-- Khung tin nhắn cuộn --%>
                    <div id="chatMessagesContainer" class="qa-chat-messages">
                        <div class="qa-chat-row qa-chat-system">
                            <div class="qa-chat-bubble">
                                <i class="bi bi-shield-check me-1 text-success"></i> Phiên tư vấn trực tuyến đã bắt đầu. Mọi thông tin trao đổi được lưu trữ để hỗ trợ bạn tốt nhất.
                            </div>
                        </div>
                        <div id="chatMessagesList" class="d-flex flex-column gap-2">
                            <%-- Tin nhắn render bằng JS --%>
                        </div>
                    </div>

                    <%-- Chỉ báo đang nhập --%>
                    <div id="roomTypingIndicator" class="qa-chat-typing u-display-none">
                        <span class="spinner-grow spinner-grow-sm me-1 text-primary" role="status"></span>
                        <span id="roomTypingText">Tư vấn viên đang soạn câu trả lời...</span>
                    </div>

                    <%-- Xem trước tệp đính kèm chuẩn bị gửi --%>
                    <div id="chatAttachPreview" class="p-2 bg-light border-top d-flex align-items-center justify-content-between u-display-none">
                        <div class="d-flex align-items-center gap-2 text-truncate">
                            <i class="bi bi-paperclip text-primary fs-4"></i>
                            <span id="chatAttachFileName" class="small fw-bold text-truncate"></span>
                            <span id="chatAttachFileSize" class="badge bg-secondary-lt text-secondary small"></span>
                            <span id="chatAttachStatus" class="small text-secondary u-display-none" role="status"></span>
                        </div>
                        <button type="button" id="btnRemoveAttach" class="btn btn-sm btn-link text-danger p-0 ms-2" title="Gỡ tệp">
                            <i class="bi bi-x-circle-fill fs-5"></i>
                        </button>
                    </div>

                    <%-- Khung nhập tin nhắn --%>
                    <div class="card-footer bg-light p-3" id="chatInputArea">
                        <input type="file" id="chatFileInput" class="u-display-none"
                               accept="image/*,.pdf,.doc,.docx,.xls,.xlsx,.zip,.rar,.txt"/>

                        <div class="input-group shadow-sm rounded-3 overflow-hidden">
                            <button type="button" class="btn btn-outline-secondary bg-white border-end-0" id="btnChatAttach" title="Đính kèm tệp">
                                <i class="bi bi-paperclip fs-5"></i>
                            </button>
                            <textarea id="roomMessageInput" class="form-control border-start-0 border-end-0 u-resize-none u-max-h-120px" rows="1" maxlength="1000"
                                      placeholder="Nhập tin nhắn (Enter để gửi, Shift+Enter xuống dòng)..."></textarea>
                            <button class="btn btn-primary px-3" type="button" id="btnRoomSend" title="Gửi tin nhắn">
                                <i class="bi bi-send-fill"></i>
                            </button>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mt-2 px-1">
                            <small class="text-muted"><i class="bi bi-info-circle me-1"></i>Nhấn Enter để gửi tin, Shift + Enter để xuống dòng</small>
                            <span class="badge bg-light text-secondary border rounded-pill" id="chatInputCharCount">0 / 1000</span>
                        </div>
                    </div>

                    <%-- Khối sau khi kết thúc phiên: Đánh giá & Tạo ticket --%>
                    <div id="chatPostSessionBlock" class="p-4 bg-light border-top u-display-none">
                        <div class="card border-primary-lt shadow-none">
                            <div class="card-body">
                                <div class="text-center mb-3">
                                    <div class="avatar avatar-md bg-blue-lt text-primary rounded-circle mx-auto mb-2 u-width-48px u-height-48px u-display-inline-flex u-align-items-center u-justify-content-center">
                                        <i class="bi bi-check-lg fs-2"></i>
                                    </div>
                                    <h4 class="mb-1 text-dark fw-bold">Cuộc trò chuyện đã kết thúc</h4>
                                    <div class="text-secondary small">Xin vui lòng đánh giá chất lượng buổi tư vấn để giúp chúng tôi phục vụ tốt hơn:</div>
                                </div>

                                <div class="text-center mb-3">
                                    <div class="qa-chat-star-rating" id="ratingStars">
                                        <i class="bi bi-star-fill qa-star" data-rating="1"></i>
                                        <i class="bi bi-star-fill qa-star" data-rating="2"></i>
                                        <i class="bi bi-star-fill qa-star" data-rating="3"></i>
                                        <i class="bi bi-star-fill qa-star" data-rating="4"></i>
                                        <i class="bi bi-star-fill qa-star" data-rating="5"></i>
                                    </div>
                                    <div class="small text-secondary mt-1 fw-medium" id="ratingLabel">Chọn số sao (1 đến 5 sao)</div>
                                </div>

                                <div class="mb-3">
                                    <textarea id="ratingComment" class="form-control" rows="2" maxlength="500"
                                              placeholder="Nhận xét hoặc góp ý thêm của bạn (tùy chọn)..."></textarea>
                                </div>

                                <div class="d-flex flex-wrap gap-2 justify-content-between align-items-center">
                                    <button type="button" id="btnSubmitRating" class="btn btn-primary" disabled>
                                        <i class="bi bi-star me-1"></i> Gửi đánh giá
                                    </button>

                                    <div class="d-flex gap-2">
                                        <button type="button" id="btnConvertToTicketPost" class="btn btn-outline-primary">
                                            <i class="bi bi-ticket-perforated me-1"></i> Tạo yêu cầu từ hội thoại này
                                        </button>
                                        <button type="button" id="btnNewChatAfterEnd" class="btn btn-outline-secondary">
                                            <i class="bi bi-plus-lg me-1"></i> Cuộc trò chuyện mới
                                        </button>
                                    </div>
                                </div>

                                <div id="postSessionTicketResult" class="alert alert-success mt-3 u-display-none">
                                    <i class="bi bi-check-circle me-1"></i> Đã tạo thành công yêu cầu tư vấn!
                                    <a id="postSessionTicketLink" href="#" class="fw-bold ms-1 text-decoration-underline">Mở yêu cầu ngay</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <!-- =======================================================
             CHẾ ĐỘ 2: LỊCH SỬ TƯ VẤN (Chỉ hiển thị khi view=history)
             ======================================================= -->
        <div id="chatHistoryView" class="${isHistoryView ? '' : 'd-none'}">
            <div class="card shadow-sm border-0">
                <div class="history-card-header d-flex justify-content-between align-items-center">
                    <div>
                        <h3 class="card-title mb-0 d-flex align-items-center gap-2">
                            <i class="bi bi-clock-history text-primary fs-5"></i>
                            <span>Danh Sách Các Cuộc Hội Thoại Đã Kết Thúc</span>
                        </h3>
                        <div class="text-muted small mt-1">Lưu trữ toàn bộ nội dung trao đổi giữa sinh viên và ban tư vấn</div>
                    </div>
                    <button type="button" class="btn btn-sm btn-outline-secondary rounded-pill px-3" id="btnRefreshHistory">
                        <i class="bi bi-arrow-clockwise me-1"></i>Làm mới
                    </button>
                </div>

                <div id="historyLoading" class="card-body text-center py-5">
                    <div class="spinner-border text-primary mb-3 u-width-3rem u-height-3rem" role="status"></div>
                    <div class="text-secondary fw-bold">Đang tải danh sách lịch sử tư vấn...</div>
                </div>

                <div id="historyEmpty" class="card-body text-center py-5 u-display-none">
                    <div class="avatar avatar-xl bg-light text-muted rounded-circle mx-auto mb-3 u-width-64px u-height-64px u-display-inline-flex u-align-items-center u-justify-content-center u-text-size-2rem">
                        <i class="bi bi-chat-left-dots"></i>
                    </div>
                    <h4 class="text-dark fw-bold mb-1">Chưa có lịch sử tư vấn</h4>
                    <p class="text-muted small mb-4">Bạn chưa có cuộc trò chuyện trực tuyến nào đã kết thúc trong hệ thống.</p>
                    <a href="${ctx}/student/chat" class="btn btn-primary rounded-pill px-4">
                        <i class="bi bi-chat-dots-fill me-1"></i>Bắt đầu tư vấn ngay
                    </a>
                </div>

                <div id="historyTableContainer" class="table-responsive u-display-none">
                    <table class="history-table">
                        <thead>
                            <tr>
                                <th>Thời gian</th>
                                <th>Chuyên mục</th>
                                <th>Tư vấn viên</th>
                                <th>Trạng thái</th>
                                <th class="text-end">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody id="historyTableBody">
                            <%-- Populated by JS --%>
                        </tbody>
                    </table>
                </div>

                <div class="card-footer d-flex justify-content-center border-top u-display-none" id="historyPaginationContainer">
                    <ul class="pagination mb-0" id="historyPagination">
                        <%-- Pagination links by JS --%>
                    </ul>
                </div>
            </div>
        </div>

    </div>
</div>

<%-- Modal xem lại lịch sử cuộc hội thoại --%>
<div class="modal modal-blur fade" id="chatHistoryModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg">
        <div class="modal-content shadow border-0 rounded-4 overflow-hidden">
            <div class="modal-header bg-light border-bottom p-3">
                <div class="d-flex align-items-center gap-2">
                    <span class="avatar avatar-sm bg-primary-lt text-primary rounded-circle">
                        <i class="bi bi-chat-text"></i>
                    </span>
                    <div>
                        <h5 class="modal-title fw-bold text-dark mb-0" id="historyModalTitle">Chi tiết cuộc hội thoại</h5>
                        <div class="text-secondary small" id="historyModalSubtitle">—</div>
                    </div>
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body p-0">
                <div id="historyModalLoading" class="text-center py-5">
                    <div class="spinner-border text-primary mb-2"></div>
                    <div class="text-secondary small">Đang tải nội dung tin nhắn...</div>
                </div>
                <div id="historyMessagesContainer" class="qa-chat-messages u-height-480px u-display-none">
                    <%-- Messages dynamically populated --%>
                </div>
            </div>
            <div class="modal-footer bg-light border-top p-3">
                <button type="button" class="btn btn-secondary rounded-pill px-4" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script src="${ctx}/assets/js/student/chat-student.js?v=20261030"></script>
</div>
