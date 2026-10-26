<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="isHistoryView" value="${param.view eq 'history'}"/>

<c:choose>
    <c:when test="${isHistoryView}">
        <title>Lịch sử tư vấn trực tuyến | QAUTE</title>
    </c:when>
    <c:otherwise>
        <title>Tư vấn trực tuyến | QAUTE</title>
    </c:otherwise>
</c:choose>

<div class="qa-page-student-chat" data-qa-view="student-chat">
<style>
/* ==========================================================================
   STUDENT CHAT & CHAT HISTORY - MODERN DESIGN SYSTEM 2026
   Palette: Azure Navy (#091e3e) -> Royal Blue (#124991) -> Deep Teal (#087d92)
   Accent: Electric Cyan (#00d2ff), Emerald (#10b981), Amber (#f59e0b)
   ========================================================================== */
.qa-page-student-chat {
    --student-primary: #124991;
    --student-primary-dark: #091e3e;
    --student-teal: #087d92;
    --student-cyan: #00d2ff;
    --student-emerald: #10b981;
    --student-amber: #f59e0b;
    --student-border: #e2e8f0;
    --student-card-shadow: 0 10px 30px -5px rgba(9, 30, 62, 0.07), 0 4px 10px -2px rgba(9, 30, 62, 0.04);
    min-width: 0;
    color: #1e293b;
    font-feature-settings: "cv02", "cv03", "cv04", "cv11";
}

/* 1. HERO BANNER */
.qa-page-student-chat .student-hero-banner {
    position: relative;
    background: linear-gradient(135deg, #091e3e 0%, #124991 55%, #087d92 100%);
    border-radius: 24px;
    padding: clamp(1.5rem, 3vw, 2.25rem);
    color: #ffffff;
    overflow: hidden;
    margin-bottom: 2rem;
    box-shadow: 0 20px 40px -15px rgba(9, 30, 62, 0.35);
    border: 1px solid rgba(255, 255, 255, 0.12);
}
.qa-page-student-chat .student-hero-banner::before {
    content: '';
    position: absolute;
    top: -40%;
    right: -10%;
    width: 420px;
    height: 420px;
    background: radial-gradient(circle, rgba(0, 210, 255, 0.22) 0%, rgba(18, 73, 145, 0) 70%);
    border-radius: 50%;
    pointer-events: none;
}
.qa-page-student-chat .student-hero-banner::after {
    content: '';
    position: absolute;
    bottom: -50%;
    left: 20%;
    width: 350px;
    height: 350px;
    background: radial-gradient(circle, rgba(16, 185, 129, 0.18) 0%, rgba(9, 30, 62, 0) 70%);
    border-radius: 50%;
    pointer-events: none;
}
.qa-page-student-chat .hero-content-wrap {
    position: relative;
    z-index: 2;
}
.qa-page-student-chat .hero-eyebrow-pill {
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
    background: rgba(255, 255, 255, 0.12);
    backdrop-filter: blur(8px);
    -webkit-backdrop-filter: blur(8px);
    border: 1px solid rgba(255, 255, 255, 0.22);
    border-radius: 999px;
    padding: 0.35rem 0.95rem;
    font-size: 0.78rem;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.08em;
    color: #bbf2f6;
    margin-bottom: 0.75rem;
}
.qa-page-student-chat .hero-eyebrow-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background-color: var(--student-cyan);
    box-shadow: 0 0 10px var(--student-cyan);
    animation: studentPulse 2s infinite ease-in-out;
}
@keyframes studentPulse {
    0%, 100% { transform: scale(1); opacity: 1; }
    50% { transform: scale(1.35); opacity: 0.7; }
}
.qa-page-student-chat .hero-title {
    font-size: clamp(1.45rem, 2.5vw, 2.15rem);
    font-weight: 800;
    letter-spacing: -0.03em;
    line-height: 1.25;
    margin-bottom: 0.5rem;
    color: #ffffff;
}
.qa-page-student-chat .hero-subtitle {
    font-size: 0.95rem;
    color: rgba(255, 255, 255, 0.85);
    max-width: 650px;
    line-height: 1.55;
    margin: 0;
}
.qa-page-student-chat .hero-action-btn {
    background: rgba(255, 255, 255, 0.14);
    border: 1px solid rgba(255, 255, 255, 0.25);
    color: #ffffff;
    backdrop-filter: blur(6px);
    border-radius: 12px;
    padding: 0.6rem 1.15rem;
    font-size: 0.85rem;
    font-weight: 600;
    transition: all 0.22s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
}
.qa-page-student-chat .hero-action-btn:hover {
    background: rgba(255, 255, 255, 0.28);
    color: #ffffff;
    transform: translateY(-2px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}
.qa-page-student-chat .hero-action-btn.btn-active-white {
    background: #ffffff;
    color: #091e3e;
    border-color: #ffffff;
    box-shadow: 0 6px 18px rgba(0, 0, 0, 0.2);
}
.qa-page-student-chat .hero-action-btn.btn-active-white:hover {
    background: #f8fafc;
    color: #124991;
}

/* 2. MAIN CARD STYLING */
.qa-page-student-chat .card {
    border: 1px solid var(--student-border);
    border-radius: 20px;
    background: #ffffff;
    box-shadow: var(--student-card-shadow);
    transition: all 0.25s ease;
    overflow: hidden;
}
.qa-page-student-chat .card-header {
    background: linear-gradient(135deg, #091e3e 0%, #124991 70%, #087d92 100%);
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    padding: 1.25rem 1.6rem;
    color: #ffffff;
}
.qa-page-student-chat .history-card-header {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border-bottom: 1px solid #e2e8f0;
    padding: 1.25rem 1.6rem;
    color: #091e3e;
}
.qa-page-student-chat .card-title {
    font-size: 1.15rem;
    font-weight: 800;
    letter-spacing: -0.02em;
    margin: 0;
}
.qa-page-student-chat .card-body {
    padding: clamp(1.25rem, 2.5vw, 2rem);
}
.qa-page-student-chat .card-footer {
    background: #f8fafc;
    border-top: 1px solid #e2e8f0;
    padding: 1.15rem 1.6rem;
}

/* 3. FORM CONTROLS */
.qa-page-student-chat .form-label {
    color: #1e293b;
    font-size: 0.85rem;
    font-weight: 700;
    margin-bottom: 0.45rem;
}
.qa-page-student-chat .form-label.required::after {
    content: " *";
    color: #ef4444;
    font-weight: bold;
}
.qa-page-student-chat .form-control,
.qa-page-student-chat .form-select {
    border: 1.5px solid #cbd5e1;
    border-radius: 12px;
    padding: 0.65rem 0.95rem;
    color: #1e293b;
    font-size: 0.92rem;
    background-color: #ffffff;
    transition: all 0.2s ease;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
}
.qa-page-student-chat .form-control:focus,
.qa-page-student-chat .form-select:focus {
    border-color: #124991;
    box-shadow: 0 0 0 4px rgba(18, 73, 145, 0.12);
    outline: none;
}

/* 4. BUTTONS */
.qa-page-student-chat .btn {
    border-radius: 12px;
    font-weight: 700;
    padding: 0.65rem 1.45rem;
    transition: all 0.22s cubic-bezier(0.16, 1, 0.3, 1);
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
}
.qa-page-student-chat .btn:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
}
.qa-page-student-chat .btn-primary {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%);
    border: none;
    color: #ffffff;
    box-shadow: 0 6px 16px rgba(18, 73, 145, 0.28);
}
.qa-page-student-chat .btn-primary:hover:not(:disabled) {
    background: linear-gradient(135deg, #091e3e 0%, #124991 100%);
    box-shadow: 0 8px 22px rgba(18, 73, 145, 0.38);
}
.qa-page-student-chat .btn-outline-secondary {
    border: 1.5px solid #cbd5e1;
    color: #475569;
    background: #ffffff;
}
.qa-page-student-chat .btn-outline-secondary:hover:not(:disabled) {
    background: #f1f5f9;
    border-color: #94a3b8;
    color: #0f172a;
}

/* 5. CHAT ROOM & MESSAGES */
.qa-page-student-chat .qa-chat-room {
    display: flex;
    flex-direction: column;
    background: #ffffff;
    border-radius: 20px;
    overflow: hidden;
}
.qa-page-student-chat .qa-chat-messages {
    height: 460px;
    max-height: 62vh;
    overflow-y: auto;
    padding: 1.25rem;
    background: radial-gradient(circle at 100% 0%, rgba(18, 73, 145, 0.05), transparent 40%), #f8fafc;
    display: flex;
    flex-direction: column;
    gap: 0.85rem;
}
.qa-page-student-chat .qa-chat-row {
    display: flex;
    width: 100%;
}
.qa-page-student-chat .qa-chat-row.qa-chat-me {
    justify-content: flex-end;
}
.qa-page-student-chat .qa-chat-row.qa-chat-other {
    justify-content: flex-start;
}
.qa-page-student-chat .qa-chat-row.qa-chat-system {
    justify-content: center;
}
.qa-page-student-chat .qa-chat-bubble {
    max-width: 78%;
    padding: 0.75rem 1rem;
    border-radius: 16px;
    position: relative;
    word-break: break-word;
    white-space: pre-wrap;
    line-height: 1.55;
    font-size: 0.92rem;
}
.qa-page-student-chat .qa-chat-me .qa-chat-bubble {
    background: linear-gradient(135deg, #124991 0%, #087d92 100%);
    color: #ffffff;
    border-bottom-right-radius: 4px;
    box-shadow: 0 5px 15px rgba(18, 73, 145, 0.2);
}
.qa-page-student-chat .qa-chat-other .qa-chat-bubble {
    background: #ffffff;
    color: #1e293b;
    border: 1px solid #e2e8f0;
    border-bottom-left-radius: 4px;
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.04);
}
.qa-page-student-chat .qa-chat-system .qa-chat-bubble {
    background-color: #f1f5f9;
    color: #64748b;
    font-size: 0.8rem;
    padding: 0.4rem 0.95rem;
    border-radius: 999px;
    text-align: center;
    border: 1px solid #e2e8f0;
}
.qa-page-student-chat .qa-chat-sender {
    font-size: 0.75rem;
    font-weight: 700;
    margin-bottom: 0.25rem;
    display: block;
    color: #087d92;
}
.qa-page-student-chat .qa-chat-meta {
    font-size: 0.7rem;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 0.3rem;
    margin-top: 0.35rem;
    opacity: 0.85;
}
.qa-page-student-chat .qa-chat-typing {
    font-size: 0.82rem;
    color: #64748b;
    font-style: italic;
    padding: 0.4rem 1.25rem;
    min-height: 1.8rem;
    background-color: #f8fafc;
    border-top: 1px solid #f1f5f9;
}
.qa-page-student-chat .qa-chat-star-rating {
    display: inline-flex;
    gap: 0.35rem;
    font-size: 2rem;
    color: #cbd5e1;
    cursor: pointer;
}
.qa-page-student-chat .qa-chat-star-rating .qa-star {
    transition: all 0.15s ease;
}
.qa-page-student-chat .qa-chat-star-rating .qa-star:hover,
.qa-page-student-chat .qa-chat-star-rating .qa-star.active {
    color: #f59e0b;
    transform: scale(1.15);
}

/* 6. HISTORY TABLE STYLING */
.qa-page-student-chat .history-table {
    border-collapse: separate;
    border-spacing: 0;
    width: 100%;
}
.qa-page-student-chat .history-table thead th {
    background: #f8fafc;
    color: #475569;
    font-size: 0.78rem;
    font-weight: 800;
    text-transform: uppercase;
    letter-spacing: 0.05em;
    padding: 1rem 1.25rem;
    border-bottom: 2px solid #e2e8f0;
}
.qa-page-student-chat .history-table tbody td {
    padding: 1rem 1.25rem;
    border-bottom: 1px solid #f1f5f9;
    vertical-align: middle;
}
.qa-page-student-chat .history-table tbody tr {
    transition: background-color 0.2s ease;
}
.qa-page-student-chat .history-table tbody tr:hover {
    background-color: #f8faff;
}

/* RESPONSIVE */
@media (max-width: 767.98px) {
    .qa-page-student-chat .student-hero-banner {
        border-radius: 18px;
        padding: 1.25rem;
    }
    .qa-page-student-chat .qa-chat-messages {
        height: 380px;
    }
}
</style>

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
                        <span class="avatar avatar-md bg-white text-primary rounded-3 shadow-sm" style="width: 44px; height: 44px; display: inline-flex; align-items: center; justify-content: center; font-size: 1.25rem;">
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
                    <div class="spinner-border text-primary mb-3" role="status" style="width: 3rem; height: 3rem;"></div>
                    <div class="text-secondary fw-bold fs-6">Đang kiểm tra phiên tư vấn của bạn...</div>
                    <div class="text-muted small mt-1">Vui lòng chờ trong giây lát</div>
                </div>

                <%-- (A) Form bắt đầu --%>
                <div id="chatStateStart" class="card-body p-4" style="display:none">
                    <div class="alert alert-info border-0 rounded-4 d-flex align-items-center p-3 mb-4" style="background: linear-gradient(135deg, #e0f2fe 0%, #f0f9ff 100%);">
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
                <div id="chatStateBot" class="card-body p-4" style="display:none">
                    <div class="d-flex align-items-center gap-3 mb-4 pb-3 border-bottom">
                        <span class="avatar avatar-lg bg-info-lt text-info rounded-circle shadow-sm" style="width: 52px; height: 52px; display: inline-flex; align-items: center; justify-content: center; font-size: 1.6rem;">
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
                        <div id="botNoSuggestions" class="text-secondary small p-3 bg-light rounded-3 border" style="display:none">
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
                    <div id="botResolvedSuccess" class="alert alert-success mt-4 p-4 rounded-4 text-center shadow-sm" style="display:none">
                        <i class="bi bi-emoji-smile-fill fs-1 text-success d-block mb-2"></i>
                        <h4 class="fw-bold text-success mb-1">Rất vui vì đã hỗ trợ được bạn!</h4>
                        <div class="small text-secondary mb-3">Phiên tư vấn đã kết thúc thành công. Chúc bạn một ngày học tập nhiều niềm vui.</div>
                        <button type="button" id="btnStartNewAfterResolved" class="btn btn-sm btn-outline-success rounded-pill px-4">
                            <i class="bi bi-plus-lg me-1"></i>Bắt đầu câu hỏi mới
                        </button>
                    </div>
                </div>

                <%-- (C) Hàng chờ --%>
                <div id="chatStateQueue" class="card-body p-4 text-center" style="display:none">
                    <%-- View khi đang chờ trong hàng --%>
                    <div id="queueWaitingView" class="py-4">
                        <div class="spinner-border text-primary mb-3" style="width: 4rem; height: 4rem;" role="status">
                            <span class="visually-hidden">Đang chờ...</span>
                        </div>
                        <h3 class="mb-2 fw-bold text-dark">Đang tìm tư vấn viên phụ trách...</h3>
                        <p class="text-secondary small mb-4">Vui lòng giữ cửa sổ này mở, hệ thống sẽ kết nối ngay khi đến lượt bạn.</p>

                        <div class="d-inline-block p-4 rounded-4 mb-4 shadow-sm border" style="background: linear-gradient(135deg, #e0f2fe 0%, #f0f9ff 100%); min-width: 200px;">
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
                    <div id="queueUnavailableView" class="py-4" style="display:none">
                        <div class="avatar avatar-xl bg-warning-lt rounded-circle mx-auto mb-3" style="width: 64px; height: 64px; display: inline-flex; align-items: center; justify-content: center;">
                            <i class="bi bi-clock-history fs-1 text-warning"></i>
                        </div>
                        <h3 class="text-warning mb-2 fw-bold" id="unavailableTitle">Hiện không có tư vấn viên trực</h3>
                        <p class="text-secondary mb-4 mx-auto" style="max-width: 520px;" id="unavailableMsg">
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
                <div id="chatStateRoom" class="qa-chat-room" style="display:none">
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
                    <div id="roomTypingIndicator" class="qa-chat-typing" style="display:none;">
                        <span class="spinner-grow spinner-grow-sm me-1 text-primary" role="status"></span>
                        <span id="roomTypingText">Tư vấn viên đang soạn câu trả lời...</span>
                    </div>

                    <%-- Xem trước tệp đính kèm chuẩn bị gửi --%>
                    <div id="chatAttachPreview" class="p-2 bg-light border-top d-flex align-items-center justify-content-between" style="display:none;">
                        <div class="d-flex align-items-center gap-2 text-truncate">
                            <i class="bi bi-paperclip text-primary fs-4"></i>
                            <span id="chatAttachFileName" class="small fw-bold text-truncate"></span>
                            <span id="chatAttachFileSize" class="badge bg-secondary-lt text-secondary small"></span>
                            <span id="chatAttachStatus" class="small text-secondary" style="display:none;" role="status"></span>
                        </div>
                        <button type="button" id="btnRemoveAttach" class="btn btn-sm btn-link text-danger p-0 ms-2" title="Gỡ tệp">
                            <i class="bi bi-x-circle-fill fs-5"></i>
                        </button>
                    </div>

                    <%-- Khung nhập tin nhắn --%>
                    <div class="card-footer bg-light p-3" id="chatInputArea">
                        <input type="file" id="chatFileInput" style="display:none;"
                               accept="image/*,.pdf,.doc,.docx,.xls,.xlsx,.zip,.rar,.txt"/>

                        <div class="input-group shadow-sm rounded-3 overflow-hidden">
                            <button type="button" class="btn btn-outline-secondary bg-white border-end-0" id="btnChatAttach" title="Đính kèm tệp">
                                <i class="bi bi-paperclip fs-5"></i>
                            </button>
                            <textarea id="roomMessageInput" class="form-control border-start-0 border-end-0" rows="1" maxlength="1000"
                                      placeholder="Nhập tin nhắn (Enter để gửi, Shift+Enter xuống dòng)..."
                                      style="resize:none; max-height: 120px;"></textarea>
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
                    <div id="chatPostSessionBlock" class="p-4 bg-light border-top" style="display:none;">
                        <div class="card border-primary-lt shadow-none">
                            <div class="card-body">
                                <div class="text-center mb-3">
                                    <div class="avatar avatar-md bg-blue-lt text-primary rounded-circle mx-auto mb-2" style="width: 48px; height: 48px; display: inline-flex; align-items: center; justify-content: center;">
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

                                <div id="postSessionTicketResult" class="alert alert-success mt-3" style="display:none;">
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
                    <div class="spinner-border text-primary mb-3" role="status" style="width: 3rem; height: 3rem;"></div>
                    <div class="text-secondary fw-bold">Đang tải danh sách lịch sử tư vấn...</div>
                </div>

                <div id="historyEmpty" class="card-body text-center py-5" style="display:none;">
                    <div class="avatar avatar-xl bg-light text-muted rounded-circle mx-auto mb-3" style="width: 64px; height: 64px; display: inline-flex; align-items: center; justify-content: center; font-size: 2rem;">
                        <i class="bi bi-chat-left-dots"></i>
                    </div>
                    <h4 class="text-dark fw-bold mb-1">Chưa có lịch sử tư vấn</h4>
                    <p class="text-muted small mb-4">Bạn chưa có cuộc trò chuyện trực tuyến nào đã kết thúc trong hệ thống.</p>
                    <a href="${ctx}/student/chat" class="btn btn-primary rounded-pill px-4">
                        <i class="bi bi-chat-dots-fill me-1"></i>Bắt đầu tư vấn ngay
                    </a>
                </div>

                <div id="historyTableContainer" class="table-responsive" style="display:none;">
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

                <div class="card-footer d-flex justify-content-center border-top" id="historyPaginationContainer" style="display:none;">
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
                <div id="historyMessagesContainer" class="qa-chat-messages" style="height: 480px; display:none;">
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
