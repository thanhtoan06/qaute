<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Tạo yêu cầu tư vấn mới</title>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.css">

<style>
    .wizard-steps {
        display: flex;
        justify-content: space-between;
        margin-bottom: 2rem;
        position: relative;
    }
    .wizard-step-item {
        flex: 1;
        text-align: center;
        position: relative;
    }
    .wizard-step-item .step-number {
        width: 38px;
        height: 38px;
        line-height: 38px;
        border-radius: 50%;
        background-color: var(--tblr-gray-200);
        color: var(--tblr-gray-600);
        display: inline-block;
        font-weight: 600;
        margin-bottom: 0.5rem;
        transition: all 0.2s ease-in-out;
    }
    .wizard-step-item.active .step-number {
        background-color: var(--tblr-primary);
        color: #fff;
        box-shadow: 0 0 0 4px rgba(32, 107, 196, 0.2);
    }
    .wizard-step-item.completed .step-number {
        background-color: var(--tblr-green);
        color: #fff;
    }
    .wizard-step-item .step-title {
        font-size: 0.875rem;
        color: var(--tblr-secondary);
        display: block;
    }
    .wizard-step-item.active .step-title {
        font-weight: 600;
        color: var(--tblr-dark);
    }
    .wizard-step-item:not(:last-child)::after {
        content: '';
        position: absolute;
        top: 19px;
        left: 50%;
        width: 100%;
        height: 2px;
        background-color: var(--tblr-gray-200);
        z-index: -1;
    }
    .wizard-step-item.completed:not(:last-child)::after {
        background-color: var(--tblr-green);
    }
</style>

<div class="row justify-content-center" id="ticketWizardContainer">
    <div class="col-12 col-xl-10">
        <!-- Breadcrumb & Title -->
        <div class="mb-3 d-flex align-items-center justify-content-between">
            <div>
                <a href="/student/tickets" class="text-secondary small text-decoration-none">
                    <i class="bi bi-arrow-left me-1"></i> Quay lại danh sách yêu cầu
                </a>
                <h2 class="page-title mt-1">Tạo yêu cầu tư vấn mới</h2>
            </div>
        </div>

        <!-- Wizard Step Indicators -->
        <div class="card mb-4">
            <div class="card-body py-3">
                <div class="wizard-steps m-0">
                    <div class="wizard-step-item active" data-step="1">
                        <span class="step-number">1</span>
                        <span class="step-title">Chọn chuyên mục</span>
                    </div>
                    <div class="wizard-step-item" data-step="2">
                        <span class="step-number">2</span>
                        <span class="step-title">Nội dung thắc mắc</span>
                    </div>
                    <div class="wizard-step-item" data-step="3">
                        <span class="step-number">3</span>
                        <span class="step-title">Đính kèm & Tùy chọn</span>
                    </div>
                    <div class="wizard-step-item" data-step="4">
                        <span class="step-number">4</span>
                        <span class="step-title">Xem lại & Gửi</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- Hidden tree data -->
        <div id="categoryTreeData" data-tree="<c:out value='${categoriesJson}' escapeXml='true'/>" class="d-none"></div>

        <!-- Global Errors -->
        <c:if test="${not empty errors['global']}">
            <div class="alert alert-danger mb-3" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${errors['global']}"/>
            </div>
        </c:if>

        <form method="post" action="/student/tickets" id="ticketCreateForm" novalidate>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="submitToken" value="<c:out value='${form.submitToken}'/>"/>
            <input type="hidden" name="categoryId" id="categoryId" value="<c:out value='${form.categoryId}'/>"/>

            <!-- BƯỚC 1: Chọn Chuyên mục -->
            <div class="wizard-step-pane" data-step="1">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <span class="badge bg-primary text-white me-2">Bước 1</span>Chọn lĩnh vực thắc mắc
                        </h3>
                    </div>
                    <div class="card-body">
                        <p class="text-secondary mb-4">
                            Vui lòng chọn chính xác bộ phận tiếp nhận để yêu cầu của bạn được chuyển đến đúng tư vấn viên phụ trách, giúp bạn nhận được phản hồi nhanh nhất.
                        </p>

                        <div id="stepError1" class="alert alert-danger d-none mb-3"></div>
                        <qa:formError field="categoryId"/>

                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label required" for="selectDepartment">1. Phòng ban / Đơn vị tiếp nhận</label>
                                <select class="form-select form-select-lg" id="selectDepartment">
                                    <option value="">-- Chọn phòng ban phụ trách --</option>
                                    <c:forEach var="dept" items="${tree}">
                                        <option value="${dept.id()}"><c:out value="${dept.name()}"/></option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label required" for="selectCategoryLevel1">2. Chuyên mục</label>
                                <select class="form-select" id="selectCategoryLevel1" disabled>
                                    <option value="">-- Chọn chuyên mục --</option>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label" for="selectCategoryLevel2">3. Chủ đề chi tiết</label>
                                <select class="form-select" id="selectCategoryLevel2" disabled>
                                    <option value="">-- Chọn chủ đề chi tiết --</option>
                                </select>
                                <small class="form-hint">Nếu chuyên mục không có chủ đề con, mục này sẽ tự động bỏ qua.</small>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer d-flex justify-content-end">
                        <button type="button" class="btn btn-primary btn-next px-4">
                            Tiếp tục <i class="bi bi-arrow-right ms-1"></i>
                        </button>
                    </div>
                </div>
            </div>

            <!-- BƯỚC 2: Tiêu đề & Nội dung -->
            <div class="wizard-step-pane d-none" data-step="2">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <span class="badge bg-primary text-white me-2">Bước 2</span>Nội dung câu hỏi tư vấn
                        </h3>
                    </div>
                    <div class="card-body">
                        <div id="stepError2" class="alert alert-danger d-none mb-3"></div>

                        <!-- Tiêu đề -->
                        <div class="mb-3">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <label class="form-label required m-0" for="ticketTitle">Tiêu đề yêu cầu</label>
                                <small class="text-secondary"><span id="titleCharCount">0</span>/150 ký tự (tối thiểu 10)</small>
                            </div>
                            <input type="text" class="form-control form-control-lg ${not empty errors['title'] ? 'is-invalid' : ''}"
                                   id="ticketTitle" name="title" value="<c:out value='${form.title}'/>"
                                   placeholder="Tóm tắt ngắn gọn câu hỏi của bạn (ví dụ: Xin cấp bảng điểm học tập học kỳ 1)..."
                                   minlength="10" maxlength="150" required>
                            <qa:formError field="title"/>
                        </div>

                        <!-- FAQ Suggestions box -->
                        <div id="faqSuggestionsBox" class="card card-sm mb-4 border-info d-none bg-info-lt">
                            <div class="card-body py-2 px-3">
                                <div class="d-flex align-items-center text-info fw-semibold mb-2">
                                    <i class="bi bi-lightbulb-fill me-2 fs-4"></i>
                                    <span>Có thể bạn quan tâm (câu hỏi thường gặp tương tự)</span>
                                </div>
                                <div class="list-group list-group-flush rounded bg-white" id="faqSuggestionsList"></div>
                                <small class="text-secondary d-block mt-2">
                                    Xem các câu trả lời có sẵn có thể giải đáp ngay thắc mắc của bạn mà không cần đợi gửi yêu cầu.
                                </small>
                            </div>
                        </div>

                        <!-- Nội dung chi tiết (Rich text) -->
                        <div class="mb-3">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <label class="form-label required m-0" for="descriptionHtml">Nội dung chi tiết</label>
                                <small class="text-secondary"><span id="descCharCount">0</span> ký tự (tối thiểu 30)</small>
                            </div>
                            <textarea id="descriptionHtml" name="descriptionHtml"
                                      class="form-control ${not empty errors['descriptionHtml'] ? 'is-invalid' : ''}"
                                      rows="8" required><c:out value='${form.descriptionHtml}'/></textarea>
                            <qa:formError field="descriptionHtml"/>
                        </div>
                    </div>
                    <div class="card-footer d-flex justify-content-between">
                        <button type="button" class="btn btn-outline-secondary btn-prev">
                            <i class="bi bi-arrow-left me-1"></i> Quay lại
                        </button>
                        <button type="button" class="btn btn-primary btn-next px-4">
                            Tiếp tục <i class="bi bi-arrow-right ms-1"></i>
                        </button>
                    </div>
                </div>
            </div>

            <!-- BƯỚC 3: Đính kèm & Tùy chọn -->
            <div class="wizard-step-pane d-none" data-step="3">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <span class="badge bg-primary text-white me-2">Bước 3</span>Đính kèm tệp & Thiết lập
                        </h3>
                    </div>
                    <div class="card-body">
                        <div id="stepError3" class="alert alert-danger d-none mb-3"></div>

                        <!-- Tệp đính kèm -->
                        <div class="mb-4">
                            <label class="form-label">Tệp đính kèm (Hình ảnh, tài liệu minh chứng)</label>
                            <div id="fileUploaderContainer"></div>
                            <qa:formError field="assetIds"/>
                        </div>

                        <hr class="my-4">

                        <!-- Tùy chọn nâng cao -->
                        <div class="mb-3">
                            <label class="form-check form-switch mb-2">
                                <input class="form-check-input" type="checkbox" id="urgentRequested" name="urgentRequested"
                                       value="true" ${form.urgentRequested ? 'checked' : ''}>
                                <span class="form-check-label fw-semibold text-danger">
                                    <i class="bi bi-lightning-fill me-1"></i> Đề nghị xử lý gấp (Khẩn cấp)
                                </span>
                            </label>
                            <div id="urgentReasonContainer" class="ms-4 ps-2 mb-3 d-none">
                                <label class="form-label required" for="urgentReason">Lý do yêu cầu xử lý khẩn</label>
                                <textarea class="form-control ${not empty errors['urgentReason'] ? 'is-invalid' : ''}"
                                          id="urgentReason" name="urgentReason" rows="3"
                                          placeholder="Nêu rõ lý do cần xử lý gấp (hạn nộp hồ sơ, thủ tục tốt nghiệp, v.v. - 10 đến 500 ký tự)..."
                                          maxlength="500"><c:out value="${form.urgentReason}"/></textarea>
                                <small class="form-hint">Yêu cầu khẩn sẽ được ưu tiên tiếp nhận. Vui lòng chỉ sử dụng khi thực sự cần thiết.</small>
                                <qa:formError field="urgentReason"/>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-check mb-0">
                                <input class="form-check-input" type="checkbox" id="allowPublicFaq" name="allowPublicFaq"
                                       value="true" ${form.allowPublicFaq ? 'checked' : ''}>
                                <span class="form-check-label">
                                    Cho phép Ban tư vấn biên tập nội dung câu hỏi thành FAQ công khai ẩn danh để giúp đỡ các sinh viên khác
                                </span>
                            </label>
                            <small class="form-hint ms-4 ps-2 text-muted">
                                Thông tin cá nhân của bạn (tên, MSSV, email, sđt) sẽ được xóa hoàn toàn khi biên tập FAQ.
                            </small>
                        </div>
                    </div>
                    <div class="card-footer d-flex justify-content-between">
                        <button type="button" class="btn btn-outline-secondary btn-prev">
                            <i class="bi bi-arrow-left me-1"></i> Quay lại
                        </button>
                        <button type="button" class="btn btn-primary btn-next px-4">
                            Xem lại yêu cầu <i class="bi bi-arrow-right ms-1"></i>
                        </button>
                    </div>
                </div>
            </div>

            <!-- BƯỚC 4: Xem lại & Gửi -->
            <div class="wizard-step-pane d-none" data-step="4">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <span class="badge bg-primary text-white me-2">Bước 4</span>Xác nhận thông tin & Gửi yêu cầu
                        </h3>
                    </div>
                    <div class="card-body">
                        <p class="text-secondary mb-3">
                            Vui lòng kiểm tra kỹ lại thông tin trước khi gửi. Sau khi gửi, bạn có thể theo dõi tiến độ xử lý và trao đổi với tư vấn viên tại trang chi tiết yêu cầu.
                        </p>

                        <div class="table-responsive border rounded mb-3">
                            <table class="table table-vcenter card-table m-0">
                                <tbody>
                                    <tr>
                                        <td class="text-secondary text-nowrap" style="width: 180px;">Chuyên mục</td>
                                        <td class="fw-medium text-primary" id="reviewCategory">—</td>
                                    </tr>
                                    <tr>
                                        <td class="text-secondary text-nowrap">Tiêu đề</td>
                                        <td class="fw-bold" id="reviewTitle">—</td>
                                    </tr>
                                    <tr>
                                        <td class="text-secondary text-nowrap align-top">Nội dung thắc mắc</td>
                                        <td>
                                            <div id="reviewContent" class="border rounded p-3 bg-light overflow-auto" style="max-height: 250px;">—</div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="text-secondary text-nowrap">Tệp đính kèm</td>
                                        <td id="reviewFiles">Không có tệp đính kèm</td>
                                    </tr>
                                    <tr>
                                        <td class="text-secondary text-nowrap">Mức ưu tiên</td>
                                        <td id="reviewUrgent">Mức độ bình thường</td>
                                    </tr>
                                    <tr>
                                        <td class="text-secondary text-nowrap">Chia sẻ FAQ</td>
                                        <td id="reviewFaq">—</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer d-flex justify-content-between">
                        <button type="button" class="btn btn-outline-secondary btn-prev">
                            <i class="bi bi-arrow-left me-1"></i> Chỉnh sửa lại
                        </button>
                        <button type="submit" class="btn btn-success px-4" id="submitTicketBtn">
                            <i class="bi bi-send-fill me-1"></i> Gửi yêu cầu tư vấn
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.js"></script>
<script src="/assets/js/common/richtext.js"></script>
<script src="/assets/js/common/file-uploader.js"></script>
<script src="/assets/js/student/ticket-create.js"></script>

