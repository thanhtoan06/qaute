<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Tạo yêu cầu tư vấn mới | QAUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/ticket-create.css">
</head>

<div class="qa-page-student-ticket-create" data-qa-view="student-ticket-create">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.css">



<div class="row justify-content-center" id="ticketWizardContainer">
    <div class="col-12 col-xl-10">

        <!-- 1. HERO BANNER -->
        <div class="student-hero-banner">
            <div class="hero-content-wrap d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3">
                <div>
                    <div class="hero-eyebrow-pill">
                        <span class="hero-eyebrow-dot"></span>
                        <span>Cổng Tư Vấn Sinh Viên • Hỗ Trợ 24/7</span>
                    </div>
                    <h1 class="hero-title">
                        <i class="bi bi-patch-question-fill text-info me-2"></i>Tạo Yêu Cầu Tư Vấn Mới
                    </h1>
                    <p class="hero-subtitle">
                        Điền thông tin theo 4 bước hướng dẫn trực quan bên dưới để gửi câu hỏi trực tiếp đến phòng ban phụ trách và nhận phản hồi chính thức nhanh chóng.
                    </p>
                </div>
                <div class="d-flex align-items-center gap-2">
                    <a href="/student/tickets" class="hero-back-btn">
                        <i class="bi bi-arrow-left"></i>
                        <span>Quay lại danh sách</span>
                    </a>
                </div>
            </div>
        </div>

        <!-- 2. WIZARD STEPPERS -->
        <div class="wizard-stepper-card">
            <div class="wizard-steps">
                <div class="wizard-step-item step-tab-btn active" data-step="1">
                    <div class="step-badge-wrap">
                        <div class="step-number"><span>1</span></div>
                    </div>
                    <span class="step-title">1. Chuyên mục</span>
                    <span class="step-subtitle">Phòng ban & Lĩnh vực</span>
                </div>
                <div class="wizard-step-item step-tab-btn" data-step="2">
                    <div class="step-badge-wrap">
                        <div class="step-number"><span>2</span></div>
                    </div>
                    <span class="step-title">2. Nội dung</span>
                    <span class="step-subtitle">Tiêu đề & Câu hỏi</span>
                </div>
                <div class="wizard-step-item step-tab-btn" data-step="3">
                    <div class="step-badge-wrap">
                        <div class="step-number"><span>3</span></div>
                    </div>
                    <span class="step-title">3. Đính kèm</span>
                    <span class="step-subtitle">Tệp & Tùy chọn</span>
                </div>
                <div class="wizard-step-item step-tab-btn" data-step="4">
                    <div class="step-badge-wrap">
                        <div class="step-number"><span>4</span></div>
                    </div>
                    <span class="step-title">4. Xác nhận</span>
                    <span class="step-subtitle">Xem lại & Gửi</span>
                </div>
            </div>
        </div>

        <!-- Hidden tree data for JS cascade logic -->
        <div id="categoryTreeData" data-tree="<c:out value='${categoriesJson}' escapeXml='true'/>" class="d-none"></div>

        <!-- Global Errors -->
        <c:if test="${not empty errors['global']}">
            <div class="alert alert-danger d-flex align-items-center mb-4 p-3 rounded-4 shadow-sm" role="alert">
                <i class="bi bi-exclamation-triangle-fill fs-3 me-3 text-danger"></i>
                <div>
                    <strong class="d-block">Có lỗi xảy ra:</strong>
                    <span><c:out value="${errors['global']}"/></span>
                </div>
            </div>
        </c:if>

        <form method="post" action="/student/tickets" id="ticketCreateForm" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="submitToken" value="<c:out value='${form.submitToken}'/>"/>
            <input type="hidden" name="categoryId" id="categoryId" value="<c:out value='${form.categoryId}'/>"/>

            <!-- ==========================================
                 BƯỚC 1: CHỌN CHUYÊN MỤC & PHÒNG BAN
                 ========================================== -->
            <div class="wizard-step-pane" data-step="1">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <span class="step-badge-pill"><i class="bi bi-diagram-3-fill me-1"></i> Bước 1</span>
                            <span>Chọn Lĩnh Vực & Phòng Ban Tiếp Nhận</span>
                        </h3>
                        <span class="badge bg-light text-secondary border">Bước 1/4</span>
                    </div>
                    <div class="card-body">
                        <!-- Helpful hint banner -->
                        <div class="alert alert-info border-0 rounded-4 d-flex align-items-center p-3 mb-4 u-bg-linear-gradient-135deg-e0f2fe-0-f0f9ff-100">
                            <div class="me-3 fs-2 text-info">
                                <i class="bi bi-lightbulb-fill"></i>
                            </div>
                            <div class="text-secondary small">
                                <strong class="text-dark d-block">Mẹo hướng dẫn tiếp nhận nhanh:</strong>
                                Vui lòng chọn chính xác bộ phận tiếp nhận để yêu cầu được điều hướng đến đúng cán bộ chuyên trách, giúp thắc mắc của bạn được giải đáp nhanh hơn.
                            </div>
                        </div>

                        <div id="stepError1" class="alert alert-danger d-none mb-3 rounded-3"></div>
                        <qa:formError field="categoryId"/>

                        <div class="row g-4">
                            <!-- 1. Phòng ban -->
                            <div class="col-12">
                                <div class="selector-tile-card">
                                    <div class="d-flex align-items-center gap-2 mb-2">
                                        <span class="qa-page-student-tile-icon bg-primary text-white">
                                            <i class="bi bi-building"></i>
                                        </span>
                                        <div>
                                            <label class="form-label required m-0" for="selectDepartment">1. Phòng ban / Đơn vị tiếp nhận</label>
                                            <div class="text-muted small">Cơ quan phụ trách chuyên môn trực tiếp</div>
                                        </div>
                                    </div>
                                    <select class="form-select form-select-lg" id="selectDepartment">
                                        <option value="">-- Chọn phòng ban phụ trách --</option>
                                        <c:forEach var="dept" items="${tree}">
                                            <option value="${dept.id()}"><c:out value="${dept.name()}"/></option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <!-- 2. Chuyên mục cấp 1 -->
                            <div class="col-md-6">
                                <div class="selector-tile-card">
                                    <div class="d-flex align-items-center gap-2 mb-2">
                                        <span class="qa-page-student-tile-icon bg-info text-white">
                                            <i class="bi bi-folder2-open"></i>
                                        </span>
                                        <div>
                                            <label class="form-label required m-0" for="selectCategoryLevel1">2. Chuyên mục chính</label>
                                            <div class="text-muted small">Nhóm nghiệp vụ liên quan</div>
                                        </div>
                                    </div>
                                    <select class="form-select" id="selectCategoryLevel1" disabled>
                                        <option value="">-- Chọn chuyên mục --</option>
                                    </select>
                                </div>
                            </div>

                            <!-- 3. Chủ đề chi tiết cấp 2 -->
                            <div class="col-md-6">
                                <div class="selector-tile-card">
                                    <div class="d-flex align-items-center gap-2 mb-2">
                                        <span class="qa-page-student-tile-icon bg-teal text-white u-bg-color-var-student-teal">
                                            <i class="bi bi-tags"></i>
                                        </span>
                                        <div>
                                            <label class="form-label m-0" for="selectCategoryLevel2">3. Chủ đề chi tiết</label>
                                            <div class="text-muted small">Phân loại chi tiết (nếu có)</div>
                                        </div>
                                    </div>
                                    <select class="form-select" id="selectCategoryLevel2" disabled>
                                        <option value="">-- Chọn chủ đề chi tiết --</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer d-flex justify-content-between align-items-center">
                        <span class="text-muted small"><i class="bi bi-shield-check me-1 text-success"></i>Thông tin yêu cầu được bảo mật theo quy định</span>
                        <button type="button" class="btn btn-primary btn-next px-4">
                            <span>Tiếp tục bước 2</span>
                            <i class="bi bi-arrow-right"></i>
                        </button>
                    </div>
                </div>
            </div>

            <!-- ==========================================
                 BƯỚC 2: TIÊU ĐỀ & NỘI DUNG CÂU HỎI
                 ========================================== -->
            <div class="wizard-step-pane d-none" data-step="2">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <span class="step-badge-pill"><i class="bi bi-pencil-square me-1"></i> Bước 2</span>
                            <span>Nội Dung Câu Hỏi & Thắc Mắc</span>
                        </h3>
                        <span class="badge bg-light text-secondary border">Bước 2/4</span>
                    </div>
                    <div class="card-body">
                        <div id="stepError2" class="alert alert-danger d-none mb-3 rounded-3"></div>

                        <!-- Tiêu đề -->
                        <div class="mb-4">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <label class="form-label required m-0" for="ticketTitle">
                                    <i class="bi bi-chat-left-text me-1 text-primary"></i>Tiêu đề câu hỏi
                                </label>
                                <span class="badge bg-light text-secondary border rounded-pill">
                                    <span id="titleCharCount" class="text-primary fw-bold">0</span> / 150 ký tự (tối thiểu 10)
                                </span>
                            </div>
                            <input type="text" class="form-control form-control-lg ${not empty errors['title'] ? 'is-invalid' : ''}"
                                   id="ticketTitle" name="title" value="<c:out value='${form.title}'/>"
                                   placeholder="Tóm tắt ngắn gọn câu hỏi của bạn (ví dụ: Xin cấp bảng điểm học tập học kỳ 1)..."
                                   minlength="10" maxlength="150" required>
                            <qa:formError field="title"/>
                        </div>

                        <!-- Smart FAQ Suggestions Box -->
                        <div id="faqSuggestionsBox" class="card card-sm mb-4 d-none">
                            <div class="card-body py-3 px-3">
                                <div class="d-flex align-items-center text-primary fw-bold mb-2">
                                    <i class="bi bi-stars me-2 fs-5 text-warning"></i>
                                    <span>Gợi ý thông minh: Có thể câu hỏi của bạn đã có lời giải đáp sẵn!</span>
                                </div>
                                <div class="list-group list-group-flush rounded-3 bg-white shadow-sm overflow-hidden" id="faqSuggestionsList"></div>
                                <div class="d-flex align-items-center gap-1 text-muted small mt-2">
                                    <i class="bi bi-info-circle"></i>
                                    <span>Bấm vào để xem ngay câu trả lời mà không cần phải đợi giải quyết yêu cầu.</span>
                                </div>
                            </div>
                        </div>

                        <!-- Nội dung chi tiết (Rich text) -->
                        <div class="mb-3">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <label class="form-label required m-0" for="descriptionHtml">
                                    <i class="bi bi-file-text me-1 text-primary"></i>Nội dung chi tiết
                                </label>
                                <span class="badge bg-light text-secondary border rounded-pill">
                                    <span id="descCharCount" class="text-primary fw-bold">0</span> ký tự (tối thiểu 30)
                                </span>
                            </div>
                            <textarea id="descriptionHtml" name="descriptionHtml"
                                      class="form-control ${not empty errors['descriptionHtml'] ? 'is-invalid' : ''}"
                                      rows="8" required><c:out value='${form.descriptionHtml}'/></textarea>
                            <qa:formError field="descriptionHtml"/>
                            <div class="form-hint mt-2 text-muted small">
                                <i class="bi bi-check2-circle text-success me-1"></i>
                                Khuyên dùng: Mô tả rõ hoàn cảnh, mốc thời gian, MSSV hoặc đính kèm mã học phần nếu liên quan để tư vấn viên xử lý chuẩn xác nhất.
                            </div>
                        </div>
                    </div>
                    <div class="card-footer d-flex justify-content-between align-items-center">
                        <button type="button" class="btn btn-outline-secondary btn-prev">
                            <i class="bi bi-arrow-left"></i>
                            <span>Quay lại bước 1</span>
                        </button>
                        <button type="button" class="btn btn-primary btn-next px-4">
                            <span>Tiếp tục bước 3</span>
                            <i class="bi bi-arrow-right"></i>
                        </button>
                    </div>
                </div>
            </div>

            <!-- ==========================================
                 BƯỚC 3: ĐÍNH KÈM TỆP & THIẾT LẬP
                 ========================================== -->
            <div class="wizard-step-pane d-none" data-step="3">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <span class="step-badge-pill"><i class="bi bi-paperclip me-1"></i> Bước 3</span>
                            <span>Đính Kèm Tài Liệu Minh Chứng & Thiết Lập</span>
                        </h3>
                        <span class="badge bg-light text-secondary border">Bước 3/4</span>
                    </div>
                    <div class="card-body">
                        <div id="stepError3" class="alert alert-danger d-none mb-3 rounded-3"></div>

                        <!-- Dropzone File Uploader -->
                        <div class="mb-4">
                            <label class="form-label d-flex align-items-center justify-content-between">
                                <span><i class="bi bi-cloud-arrow-up text-primary me-1"></i>Tệp đính kèm minh chứng (Hình ảnh, đơn từ, bảng điểm)</span>
                                <span class="badge bg-secondary-lt text-secondary">Tối đa 5 tệp (10MB/tệp)</span>
                            </label>

                            <div id="fileUploaderContainer" data-upload-url="${pageContext.request.contextPath}/api/media/upload">
                                <div class="qa-uploader">
                                    <div class="uploader-dropzone text-center mb-3">
                                        <div class="py-2">
                                            <div class="uploader-icon-halo">
                                                <i class="bi bi-cloud-arrow-up fs-2 text-primary"></i>
                                            </div>
                                            <div class="fw-bold text-dark fs-6 mb-1">
                                                Kéo thả tệp vào đây hoặc <span class="text-primary text-decoration-underline">chọn tệp từ thiết bị</span>
                                            </div>
                                            <p class="text-muted small mb-3">
                                                Hỗ trợ: JPG, PNG, WEBP, GIF, PDF, DOCX, XLSX, PPTX, TXT
                                            </p>
                                            <button type="button" class="btn btn-sm btn-primary rounded-pill px-3 uploader-pick-btn">
                                                <i class="bi bi-paperclip me-1"></i>Chọn tệp tải lên
                                            </button>
                                        </div>
                                        <input type="file" multiple class="d-none uploader-file-input" accept=".jpg,.jpeg,.png,.webp,.gif,.pdf,.docx,.xlsx,.pptx,.txt">
                                    </div>
                                    <div class="uploader-file-list d-flex flex-column gap-2"></div>
                                    <div class="uploader-hidden-inputs"></div>
                                </div>
                            </div>
                            <qa:formError field="assetIds"/>
                        </div>

                        <hr class="my-4 u-border-top-1px-dashed-cbd5e1">

                        <!-- Tùy chọn nâng cao -->
                        <div class="row g-3">
                            <!-- Xử lý gấp (Urgent) -->
                            <div class="col-12">
                                <div class="option-box-card urgent-card">
                                    <div class="form-check form-switch m-0">
                                        <input class="form-check-input u-cursor-pointer u-width-2-5em u-height-1-3em" type="checkbox" id="urgentRequested" name="urgentRequested"
                                               value="true" ${form.urgentRequested ? 'checked' : ''}>
                                        <label class="form-check-label fw-bold text-danger ms-2 u-cursor-pointer" for="urgentRequested">
                                            <i class="bi bi-lightning-charge-fill me-1"></i>Đề nghị xử lý khẩn cấp (Gấp)
                                        </label>
                                    </div>
                                    <div id="urgentReasonContainer" class="mt-3 pt-2 ps-md-4 d-none border-top">
                                        <label class="form-label required" for="urgentReason">
                                            Lý do yêu cầu xử lý gấp <span class="text-muted fw-normal">(10 - 500 ký tự)</span>
                                        </label>
                                        <textarea class="form-control ${not empty errors['urgentReason'] ? 'is-invalid' : ''}"
                                                  id="urgentReason" name="urgentReason" rows="3"
                                                  placeholder="Nêu rõ lý do cần xử lý gấp (ví dụ: hạn nộp hồ sơ xét học bổng còn 24h, thủ tục bảo lưu cấp bách...)"
                                                  maxlength="500"><c:out value="${form.urgentReason}"/></textarea>
                                        <small class="form-hint text-danger mt-1 d-block">
                                            <i class="bi bi-exclamation-circle me-1"></i>Yêu cầu khẩn sẽ được báo động ưu tiên. Vui lòng chỉ sử dụng khi thật sự cấp bách.
                                        </small>
                                        <qa:formError field="urgentReason"/>
                                    </div>
                                </div>
                            </div>

                            <!-- FAQ Sharing Authorization -->
                            <div class="col-12">
                                <div class="option-box-card faq-card">
                                    <div class="form-check m-0">
                                        <input class="form-check-input u-cursor-pointer" type="checkbox" id="allowPublicFaq" name="allowPublicFaq"
                                               value="true" ${form.allowPublicFaq ? 'checked' : ''}>
                                        <label class="form-check-label fw-semibold text-dark ms-1 u-cursor-pointer" for="allowPublicFaq">
                                            <i class="bi bi-shield-lock-fill text-success me-1"></i>Cho phép Ban tư vấn biên tập nội dung này thành FAQ công khai ẩn danh
                                        </label>
                                    </div>
                                    <div class="ps-md-4 pt-1">
                                        <small class="text-muted">
                                            Toàn bộ thông tin định danh cá nhân của bạn (Họ tên, MSSV, Email, Số điện thoại) sẽ được tự động xóa hoàn toàn để bảo vệ quyền riêng tư.
                                        </small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer d-flex justify-content-between align-items-center">
                        <button type="button" class="btn btn-outline-secondary btn-prev">
                            <i class="bi bi-arrow-left"></i>
                            <span>Quay lại bước 2</span>
                        </button>
                        <button type="button" class="btn btn-primary btn-next px-4">
                            <span>Xem lại & Xác nhận</span>
                            <i class="bi bi-arrow-right"></i>
                        </button>
                    </div>
                </div>
            </div>

            <!-- ==========================================
                 BƯỚC 4: XEM LẠI & GỬI YÊU CẦU
                 ========================================== -->
            <div class="wizard-step-pane d-none" data-step="4">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <span class="step-badge-pill u-bg-linear-gradient-135deg-10b981-0-059669-100">
                                <i class="bi bi-check2-circle me-1"></i> Bước 4
                            </span>
                            <span>Xác Nhận Thông Tin & Gửi Yêu Cầu</span>
                        </h3>
                        <span class="badge bg-success-lt text-success border border-success">Bước cuối cùng</span>
                    </div>
                    <div class="card-body">
                        <div class="alert alert-success border-0 rounded-4 d-flex align-items-center p-3 mb-4 u-bg-linear-gradient-135deg-ecfdf5-0-f0fdf4-100">
                            <i class="bi bi-check-circle-fill fs-2 text-success me-3"></i>
                            <div class="small text-secondary">
                                <strong class="text-success d-block">Kiểm tra thông tin trước khi hoàn tất:</strong>
                                Vui lòng kiểm tra kỹ các thông tin đã nhập bên dưới. Sau khi bấm <strong>"Gửi yêu cầu tư vấn"</strong>, hệ thống sẽ cấp mã phiếu (Ticket Code) để bạn theo dõi trực tiếp tiến độ phản hồi.
                            </div>
                        </div>

                        <!-- Review Table -->
                        <div class="table-responsive mb-4">
                            <table class="review-summary-table">
                                <tbody>
                                    <tr>
                                        <td class="review-label-col">
                                            <i class="bi bi-diagram-3 me-1 text-primary"></i> Lĩnh vực & Phòng ban
                                        </td>
                                        <td class="fw-bold text-primary" id="reviewCategory">—</td>
                                    </tr>
                                    <tr>
                                        <td class="review-label-col">
                                            <i class="bi bi-chat-left-text me-1 text-primary"></i> Tiêu đề câu hỏi
                                        </td>
                                        <td class="fw-bold text-dark fs-6" id="reviewTitle">—</td>
                                    </tr>
                                    <tr>
                                        <td class="review-label-col align-top">
                                            <i class="bi bi-file-earmark-richtext me-1 text-primary"></i> Nội dung thắc mắc
                                        </td>
                                        <td>
                                            <div id="reviewContent" class="review-content-box">—</div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="review-label-col">
                                            <i class="bi bi-paperclip me-1 text-primary"></i> Tệp đính kèm
                                        </td>
                                        <td id="reviewFiles">
                                            <span class="text-muted">Không có tệp đính kèm</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="review-label-col">
                                            <i class="bi bi-flag me-1 text-primary"></i> Mức độ ưu tiên
                                        </td>
                                        <td id="reviewUrgent">
                                            <span class="badge bg-light text-secondary border">Mức độ bình thường</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="review-label-col">
                                            <i class="bi bi-share me-1 text-primary"></i> Quyền FAQ ẩn danh
                                        </td>
                                        <td id="reviewFaq">
                                            <span class="text-muted">—</span>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer d-flex justify-content-between align-items-center">
                        <button type="button" class="btn btn-outline-secondary btn-prev">
                            <i class="bi bi-arrow-left"></i>
                            <span>Chỉnh sửa lại</span>
                        </button>
                        <button type="submit" class="btn btn-success px-4" id="submitTicketBtn">
                            <i class="bi bi-send-fill me-1"></i>
                            <span>Gửi Yêu Cầu Tư Vấn Ngay</span>
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.js"></script>
<script src="/assets/js/common/richtext.js?v=20261023b"></script>
<script src="${pageContext.request.contextPath}/assets/js/common/file-uploader.js?v=20261026c"></script>
<script src="/assets/js/student/ticket-create.js?v=20261023b"></script>
</div>
