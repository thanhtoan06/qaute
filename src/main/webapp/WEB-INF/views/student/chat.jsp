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
            <div class="col-12 col-lg-8">
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
                                          placeholder="Mô tả ngắn gọn thắc mắc của bạn (tối thiểu 5 ký tự)..." required></textarea>
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

                    <%-- (D) Phòng chat --%>
                    <div id="chatStateRoom" style="display:none">
                        <div class="p-3 bg-light border-bottom d-flex align-items-center justify-content-between">
                            <div class="d-flex align-items-center gap-2">
                                <span class="avatar avatar-sm bg-success-lt text-success rounded-circle">
                                    <i class="bi bi-person-badge"></i>
                                </span>
                                <div>
                                    <div class="fw-semibold" id="roomAdvisorName">Tư vấn viên</div>
                                    <div class="small text-success d-flex align-items-center gap-1">
                                        <span class="badge badge-dot bg-success"></span> Đang trực tuyến
                                    </div>
                                </div>
                            </div>
                            <div class="d-flex gap-2">
                                <button type="button" id="btnRoomEndChat" class="btn btn-sm btn-outline-danger">
                                    <i class="bi bi-box-arrow-right me-1"></i> Rời phòng
                                </button>
                            </div>
                        </div>

                        <div id="chatMessagesContainer" class="p-4 overflow-auto bg-body" style="height: 380px;">
                            <div class="alert alert-info text-center py-2 mb-3">
                                <i class="bi bi-info-circle me-1"></i>
                                Tư vấn viên đã tham gia cuộc trò chuyện. Bạn có thể bắt đầu gửi tin nhắn trao đổi.
                            </div>
                            <div id="chatMessagesList">
                                <%-- Messages rendered in next step (T1-60) --%>
                            </div>
                        </div>

                        <div class="card-footer bg-light p-3">
                            <div class="input-group">
                                <input type="text" id="roomMessageInput" class="form-control"
                                       placeholder="Nhập tin nhắn... (chức năng chat sẽ hoàn thiện ở bước kế tiếp)" disabled/>
                                <button class="btn btn-primary" type="button" id="btnRoomSend" disabled>
                                    <i class="bi bi-send-fill"></i>
                                </button>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/ws-scripts.jspf" %>
<script src="${ctx}/assets/js/student/chat-student.js"></script>

