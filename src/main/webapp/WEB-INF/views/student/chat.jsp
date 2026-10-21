<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Tư vấn trực tuyến</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="page-header d-print-none">
    <div class="container-xl">
        <div class="row g-2 align-items-center">
            <div class="col">
                <h2 class="page-title">
                    <i class="bi bi-chat-dots-fill text-primary me-2"></i>Tư vấn trực tuyến
                </h2>
                <div class="text-secondary small mt-1">Kết nối nhanh với trợ lý ảo và đội ngũ tư vấn viên nhà trường</div>
            </div>
            <div class="col-auto ms-auto">
                <a href="${ctx}/student/tickets/new" class="btn btn-outline-secondary">
                    <i class="bi bi-ticket-perforated me-1"></i>Gửi yêu cầu hỗ trợ (Ticket)
                </a>
            </div>
        </div>
    </div>
</div>

<div class="page-body">
    <div class="container-xl">
        <qa:alert/>

        <div class="row justify-content-center">
            <div class="col-12 col-lg-9">
                <%-- Tabs: Trực tuyến / Lịch sử chat --%>
                <div class="card mb-3 shadow-sm border-0">
                    <div class="card-header p-0">
                        <ul class="nav nav-tabs card-header-tabs" id="chatMainTabs">
                            <li class="nav-item">
                                <a class="nav-link active" id="tabLiveChat" href="javascript:void(0)">
                                    <i class="bi bi-chat-dots me-1"></i>Tư vấn trực tuyến
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" id="tabHistory" href="javascript:void(0)">
                                    <i class="bi bi-clock-history me-1"></i>Lịch sử chat
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>

                <%-- Tab 1: Tư vấn trực tuyến (Live Chat) --%>
                <div id="chatLiveView">
                    <div class="card shadow-sm border-0">
                        <div class="card-header bg-primary text-white d-flex align-items-center justify-content-between">
                            <div class="d-flex align-items-center gap-2">
                                <span class="avatar avatar-sm bg-white text-primary rounded-circle">
                                    <i class="bi bi-headset"></i>
                                </span>
                                <div>
                                    <h3 class="card-title text-white mb-0" id="chatHeaderTitle">Kênh tư vấn trực tuyến</h3>
                                    <div class="text-white-50 small" id="chatHeaderSubtitle">Hỗ trợ học tập, học vụ và đời sống sinh viên</div>
                                </div>
                            </div>
                            <span class="badge bg-white text-primary" id="chatHeaderBadge">Sẵn sàng</span>
                        </div>

                        <%-- Loading state while checking current session --%>
                        <div id="chatStateLoading" class="card-body text-center py-5">
                            <div class="spinner-border text-primary mb-3" role="status"></div>
                            <div class="text-secondary fw-medium">Đang kiểm tra phiên tư vấn...</div>
                        </div>

                        <%-- (A) Form bắt đầu --%>
                        <div id="chatStateStart" class="card-body p-4" style="display:none">
                            <div class="alert alert-info d-flex align-items-center gap-2 mb-4">
                                <i class="bi bi-info-circle fs-3"></i>
                                <div>
                                    Hãy chọn chuyên mục và nhập câu hỏi của bạn. Hệ thống sẽ tự động tìm kiếm các bài viết hướng dẫn phù hợp trước khi chuyển tới tư vấn viên.
                                </div>
                            </div>

                            <form id="formStartChat">
                                <div class="mb-3">
                                    <label class="form-label required" for="chatCategoryId">Chuyên mục cần tư vấn</label>
                                    <select id="chatCategoryId" class="form-select" required>
                                        <option value="">-- Chọn chuyên mục --</option>
                                        <c:forEach var="department" items="${tree}">
                                            <optgroup label="${department.name()}">
                                                <c:forEach var="root" items="${department.categories()}">
                                                    <option value="${root.id()}"><c:out value="${root.name()}"/></option>
                                                    <c:forEach var="child" items="${root.children()}">
                                                        <option value="${child.id()}">&nbsp;&nbsp;└ <c:out value="${child.name()}"/></option>
                                                    </c:forEach>
                                                </c:forEach>
                                            </optgroup>
                                        </c:forEach>
                                    </select>
                                    <small class="form-hint">Chọn đúng chuyên mục giúp chuyển câu hỏi tới tư vấn viên chuyên trách.</small>
                                </div>

                                <div class="mb-4">
                                    <label class="form-label required" for="chatQuestion">Câu hỏi hoặc vấn đề của bạn</label>
                                    <textarea id="chatQuestion" class="form-control" rows="4" minlength="5" maxlength="500"
                                              placeholder="Mô tả ngắn gọn thắc mắc của bạn (từ 5 đến 500 ký tự)..." required></textarea>
                                    <div class="d-flex justify-content-between mt-1">
                                        <small class="text-secondary">Tối thiểu 5 ký tự, tối đa 500 ký tự</small>
                                        <small class="text-secondary" id="chatQuestionCount">0 / 500</small>
                                    </div>
                                </div>

                                <div class="d-grid">
                                    <button type="submit" id="btnStartChat" class="btn btn-primary btn-lg">
                                        <i class="bi bi-send-fill me-1"></i> Bắt đầu tư vấn
                                    </button>
                                </div>
                            </form>
                        </div>

                        <%-- (B) Bot gợi ý FAQ --%>
                        <div id="chatStateBot" class="card-body p-4" style="display:none">
                            <div class="d-flex align-items-center gap-3 mb-3 pb-3 border-bottom">
                                <span class="avatar avatar-md bg-azure-lt rounded-circle">
                                    <i class="bi bi-robot fs-2"></i>
                                </span>
                                <div>
                                    <h4 class="mb-0">Trợ lý ảo QAUTE</h4>
                                    <div class="text-secondary small">Gợi ý câu trả lời từ kho tri thức hỏi đáp</div>
                                </div>
                            </div>

                            <div class="bg-light p-3 rounded mb-3">
                                <div class="text-secondary small mb-1">
                                    <i class="bi bi-question-circle me-1"></i>Câu hỏi của bạn:
                                </div>
                                <div class="fw-semibold text-dark" id="botQuestionText">—</div>
                            </div>

                            <div class="mb-4">
                                <div class="fw-semibold mb-2">
                                    <i class="bi bi-lightbulb text-warning me-1"></i>Bài viết hướng dẫn có thể giúp ích cho bạn:
                                </div>
                                <div id="botSuggestionsList" class="list-group">
                                    <%-- Dynamically populated via JS --%>
                                </div>
                                <div id="botNoSuggestions" class="text-secondary small fst-italic p-3 bg-light rounded" style="display:none">
                                    Chưa tìm thấy bài viết FAQ trực tiếp cho câu hỏi này. Bạn có thể kết nối ngay với tư vấn viên bên dưới.
                                </div>
                            </div>

                            <div class="d-flex flex-wrap gap-2 justify-content-between pt-2 border-top">
                                <button type="button" id="btnResolved" class="btn btn-outline-success">
                                    <i class="bi bi-check-circle me-1"></i> Đã giải quyết được rồi
                                </button>
                                <button type="button" id="btnRequestAgent" class="btn btn-primary">
                                    <i class="bi bi-headset me-1"></i> Vẫn muốn nói chuyện với tư vấn viên
                                </button>
                            </div>

                            <%-- Cảm ơn khi đã giải quyết --%>
                            <div id="botResolvedSuccess" class="alert alert-success mt-3 text-center" style="display:none">
                                <i class="bi bi-emoji-smile fs-2 d-block mb-1"></i>
                                <div class="fw-bold">Rất vui vì đã hỗ trợ được bạn!</div>
                                <div class="small">Phiên tư vấn đã kết thúc. Chúc bạn một ngày học tập tốt lành.</div>
                                <button type="button" id="btnStartNewAfterResolved" class="btn btn-sm btn-outline-success mt-2">
                                    Bắt đầu câu hỏi mới
                                </button>
                            </div>
                        </div>

                        <%-- (C) Hàng chờ --%>
                        <div id="chatStateQueue" class="card-body p-4 text-center" style="display:none">
                            <%-- View khi đang chờ trong hàng --%>
                            <div id="queueWaitingView" class="py-4">
                                <div class="spinner-border text-primary mb-3" style="width: 3.5rem; height: 3.5rem;" role="status">
                                    <span class="visually-hidden">Đang chờ...</span>
                                </div>
                                <h3 class="mb-1">Đang tìm tư vấn viên...</h3>
                                <p class="text-secondary small mb-3">Vui lòng giữ cửa sổ này mở, hệ thống sẽ kết nối ngay khi đến lượt bạn.</p>

                                <div class="d-inline-block bg-primary-lt px-4 py-3 rounded-3 mb-4">
                                    <div class="text-secondary small text-uppercase fw-semibold">Vị trí của bạn</div>
                                    <div class="display-5 fw-bold text-primary my-1">
                                        #<span id="queuePositionNum">1</span>
                                    </div>
                                    <div class="small text-secondary">trong hàng chờ</div>
                                </div>

                                <div>
                                    <button type="button" id="btnCancelQueue" class="btn btn-outline-danger">
                                        <i class="bi bi-x-circle me-1"></i> Hủy yêu cầu tư vấn
                                    </button>
                                </div>
                            </div>

                            <%-- View khi ngoài giờ hoặc hết hạn (workingNow = false hoặc EXPIRED) --%>
                            <div id="queueUnavailableView" class="py-4" style="display:none">
                                <div class="avatar avatar-xl bg-warning-lt rounded-circle mx-auto mb-3">
                                    <i class="bi bi-clock-history fs-1 text-warning"></i>
                                </div>
                                <h3 class="text-warning mb-2" id="unavailableTitle">Hiện không có tư vấn viên trực</h3>
                                <p class="text-secondary mb-4 mx-auto" style="max-width: 500px;" id="unavailableMsg">
                                    Hiện tại ngoài giờ làm việc hoặc tất cả tư vấn viên thuộc chuyên mục này đang bận. Bạn có thể chuyển câu hỏi thành yêu cầu hỗ trợ (ticket) để nhận phản hồi chi tiết qua email.
                                </p>

                                <div class="d-flex justify-content-center gap-2">
                                    <a id="btnConvertToTicket" href="${ctx}/student/tickets/new" class="btn btn-primary">
                                        <i class="bi bi-ticket-perforated me-1"></i> Chuyển thành yêu cầu tư vấn
                                    </a>
                                    <button type="button" id="btnBackToStart" class="btn btn-outline-secondary">
                                        Quay lại
                                    </button>
                                </div>
                            </div>
                        </div>

                        <%-- (D) Phòng chat Realtime --%>
                        <div id="chatStateRoom" class="qa-chat-room" style="display:none">
                            <div class="p-3 bg-light border-bottom d-flex align-items-center justify-content-between">
                                <div class="d-flex align-items-center gap-2">
                                    <span class="avatar avatar-sm bg-success-lt text-success rounded-circle" id="roomAdvisorAvatar">
                                        <i class="bi bi-person-badge"></i>
                                    </span>
                                    <div>
                                        <div class="fw-semibold" id="roomAdvisorName">Tư vấn viên</div>
                                        <div class="small text-success d-flex align-items-center gap-1" id="roomAdvisorStatus">
                                            <span class="badge badge-dot bg-success"></span> Đang trực tuyến
                                        </div>
                                    </div>
                                </div>
                                <div class="d-flex gap-2">
                                    <button type="button" id="btnRoomEndChat" class="btn btn-sm btn-outline-danger">
                                        <i class="bi bi-box-arrow-right me-1"></i> Kết thúc chat
                                    </button>
                                </div>
                            </div>

                            <%-- Khung tin nhắn cuộn --%>
                            <div id="chatMessagesContainer" class="qa-chat-messages">
                                <div class="qa-chat-row qa-chat-system">
                                    <div class="qa-chat-bubble">
                                        <i class="bi bi-shield-check me-1"></i> Phiên tư vấn trực tuyến đã bắt đầu. Mọi thông tin trao đổi được lưu trữ để hỗ trợ bạn.
                                    </div>
                                </div>
                                <div id="chatMessagesList" class="d-flex flex-column gap-2">
                                    <%-- Tin nhắn render bằng JS --%>
                                </div>
                            </div>

                            <%-- Chỉ báo đang nhập --%>
                            <div id="roomTypingIndicator" class="qa-chat-typing" style="display:none;">
                                <span class="spinner-grow spinner-grow-sm me-1" role="status"></span>
                                <span id="roomTypingText">Tư vấn viên đang nhập...</span>
                            </div>

                            <%-- Xem trước tệp đính kèm chuẩn bị gửi --%>
                            <div id="chatAttachPreview" class="p-2 bg-light border-top d-flex align-items-center justify-content-between" style="display:none;">
                                <div class="d-flex align-items-center gap-2 text-truncate">
                                    <i class="bi bi-paperclip text-primary fs-4"></i>
                                    <span id="chatAttachFileName" class="small fw-medium text-truncate"></span>
                                    <span id="chatAttachFileSize" class="badge bg-secondary-lt text-secondary small"></span>
                                </div>
                                <button type="button" id="btnRemoveAttach" class="btn btn-sm btn-link text-danger p-0 ms-2" title="Gỡ tệp">
                                    <i class="bi bi-x-circle-fill fs-4"></i>
                                </button>
                            </div>

                            <%-- Khung nhập tin nhắn --%>
                            <div class="card-footer bg-light p-3" id="chatInputArea">
                                <input type="file" id="chatFileInput" style="display:none;"
                                       accept="image/*,.pdf,.doc,.docx,.xls,.xlsx,.zip,.rar,.txt"/>

                                <div class="input-group">
                                    <button type="button" class="btn btn-outline-secondary" id="btnChatAttach" title="Đính kèm tệp">
                                        <i class="bi bi-paperclip fs-4"></i>
                                    </button>
                                    <textarea id="roomMessageInput" class="form-control" rows="1" maxlength="1000"
                                              placeholder="Nhập tin nhắn (Enter để gửi, Shift+Enter xuống dòng)..."
                                              style="resize:none; max-height: 120px;"></textarea>
                                    <button class="btn btn-primary px-3" type="button" id="btnRoomSend" title="Gửi tin nhắn">
                                        <i class="bi bi-send-fill"></i>
                                    </button>
                                </div>
                                <div class="d-flex justify-content-between mt-1">
                                    <small class="text-secondary">Nhấn Enter để gửi tin</small>
                                    <small class="text-secondary" id="chatInputCharCount">0 / 1000</small>
                                </div>
                            </div>

                            <%-- Khối sau khi kết thúc phiên: Đánh giá & Tạo ticket --%>
                            <div id="chatPostSessionBlock" class="p-4 bg-light border-top" style="display:none;">
                                <div class="card border-primary-lt shadow-none">
                                    <div class="card-body">
                                        <div class="text-center mb-3">
                                            <div class="avatar avatar-md bg-blue-lt text-primary rounded-circle mx-auto mb-2">
                                                <i class="bi bi-check-lg fs-2"></i>
                                            </div>
                                            <h4 class="mb-1">Cuộc trò chuyện đã kết thúc</h4>
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
                                            <div class="small text-secondary mt-1" id="ratingLabel">Chọn số sao (1 đến 5 sao)</div>
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

                                        <div id="postSessionTicketResult" class="alert alert-success mt-3" style="display:none;">
                                            <i class="bi bi-check-circle me-1"></i> Đã tạo thành công yêu cầu tư vấn!
                                            <a id="postSessionTicketLink" href="#" class="fw-bold ms-1">Mở yêu cầu ngay</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

                <%-- Tab 2: Lịch sử tư vấn (Chat History) --%>
                <div id="chatHistoryView" style="display:none;">
                    <div class="card shadow-sm border-0">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h3 class="card-title mb-0">
                                <i class="bi bi-clock-history me-2 text-primary"></i>Các cuộc hội thoại đã kết thúc
                            </h3>
                            <button type="button" class="btn btn-sm btn-outline-secondary" id="btnRefreshHistory">
                                <i class="bi bi-arrow-clockwise me-1"></i>Làm mới
                            </button>
                        </div>

                        <div id="historyLoading" class="card-body text-center py-5">
                            <div class="spinner-border text-primary mb-2"></div>
                            <div class="text-secondary small">Đang tải lịch sử tư vấn...</div>
                        </div>

                        <div id="historyEmpty" class="card-body text-center py-5" style="display:none;">
                            <qa:emptyState icon="bi-chat-left-dots"
                                           title="Chưa có lịch sử tư vấn"
                                           message="Bạn chưa có cuộc trò chuyện trực tuyến nào đã kết thúc."/>
                        </div>

                        <div id="historyTableContainer" class="table-responsive" style="display:none;">
                            <table class="table table-vcenter card-table table-hover">
                                <thead>
                                    <tr>
                                        <th>Thời gian</th>
                                        <th>Chuyên mục</th>
                                        <th>Tư vấn viên</th>
                                        <th>Trạng thái</th>
                                        <th class="w-1"></th>
                                    </tr>
                                </thead>
                                <tbody id="historyTableBody">
                                    <%-- Populated by JS --%>
                                </tbody>
                            </table>
                        </div>

                        <div class="card-footer d-flex justify-content-center border-top" id="historyPaginationContainer" style="display:none;">
                            <ul class="pagination mb-0" id="historyPagination">
                                <%-- Pagination links by JS --%>
                            </ul>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<%-- Modal xem lại lịch sử cuộc hội thoại --%>
<div class="modal modal-blur fade" id="chatHistoryModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg">
        <div class="modal-content shadow">
            <div class="modal-header bg-light">
                <div>
                    <h5 class="modal-title" id="historyModalTitle">Chi tiết cuộc hội thoại</h5>
                    <div class="text-secondary small" id="historyModalSubtitle">—</div>
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body p-0">
                <div id="historyModalLoading" class="text-center py-5">
                    <div class="spinner-border text-primary mb-2"></div>
                    <div class="text-secondary small">Đang tải nội dung tin nhắn...</div>
                </div>
                <div id="historyMessagesContainer" class="qa-chat-messages" style="height: 450px; display:none;">
                    <%-- Messages dynamically populated --%>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/ws-scripts.jspf" %>
<script src="${ctx}/assets/js/student/chat-student.js"></script>
