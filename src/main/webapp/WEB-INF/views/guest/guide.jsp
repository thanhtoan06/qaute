<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Hướng dẫn sử dụng QAUTE</title>

<nav aria-label="breadcrumb" class="mb-4">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
        <li class="breadcrumb-item active" aria-current="page">Hướng dẫn</li>
    </ol>
</nav>

<div class="row justify-content-center">
    <div class="col-lg-10">
        <div class="text-center mb-5">
            <span class="badge bg-primary-subtle text-primary px-3 py-2 mb-2 rounded-pill fs-6">Sổ tay hướng dẫn</span>
            <h1 class="display-6 fw-bold mb-3">Hướng dẫn sử dụng hệ thống QAUTE</h1>
            <p class="lead text-secondary mx-auto" style="max-width: 750px;">
                Tài liệu chi tiết hướng dẫn sinh viên khai thác tối đa các tiện ích tư vấn và hỗ trợ học vụ trực tuyến.
            </p>
        </div>

        <div class="accordion mb-5" id="guideAccordion">
            <div class="accordion-item mb-3 border rounded shadow-none">
                <h2 class="accordion-header" id="headingOne">
                    <button class="accordion-button fs-4 fw-semibold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                        <i class="bi bi-box-arrow-in-right me-3 text-primary"></i>1. Đăng nhập hệ thống
                    </button>
                </h2>
                <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#guideAccordion">
                    <div class="accordion-body pt-0 text-secondary">
                        <p>Để sử dụng đầy đủ các tính năng như gửi yêu cầu, trò chuyện trực tuyến và đặt lịch hẹn, sinh viên sử dụng tài khoản do quản trị viên cấp:</p>
                        <ol class="mb-3">
                            <li class="mb-2"><strong>Đăng nhập bằng MSSV:</strong> Nhập MSSV và mật khẩu được cấp trên trang đăng nhập.</li>
                            <li class="mb-2"><strong>Đăng nhập bằng Google:</strong> Sinh viên có thể dùng email <code>@student.hcmute.edu.vn</code> nếu Google SSO đã được bật.</li>
                            <li class="mb-2"><strong>Cần hỗ trợ tài khoản:</strong> Liên hệ quản trị viên để tạo, cập nhật hoặc khóa tài khoản.</li>
                        </ol>
                        <div class="alert alert-info py-2 px-3 small d-flex align-items-center mb-0">
                            <i class="bi bi-info-circle-fill me-2 fs-5"></i>
                            <div>Tài khoản và quyền truy cập được quản lý tập trung bởi quản trị viên QAUTE.</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="accordion-item mb-3 border rounded shadow-none">
                <h2 class="accordion-header" id="headingTwo">
                    <button class="accordion-button collapsed fs-4 fw-semibold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                        <i class="bi bi-search me-3 text-primary"></i>2. Tra cứu & Tìm kiếm FAQ thông minh
                    </button>
                </h2>
                <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#guideAccordion">
                    <div class="accordion-body pt-0 text-secondary">
                        <p>Kho tri thức FAQ tổng hợp hàng trăm câu hỏi thường gặp về học phí, đăng ký môn học, điểm rèn luyện, bảo hiểm y tế và các thủ tục khác:</p>
                        <ul class="mb-3">
                            <li class="mb-2"><strong>Thanh tìm kiếm:</strong> Nhập từ khóa cần tra cứu vào ô tìm kiếm ở Trang chủ, thanh menu hoặc trang Hỏi đáp FAQ.</li>
                            <li class="mb-2"><strong>Gợi ý tự động (Autocomplete):</strong> Khi gõ từ 2 ký tự trở lên, hệ thống sẽ tự động gợi ý danh sách các bài viết sát nhất với nhu cầu của bạn. Bạn có thể dùng phím mũi tên và Enter để chọn nhanh.</li>
                            <li class="mb-2"><strong>Bộ lọc nâng cao:</strong> Lọc bài viết theo từng phòng ban, chuyên mục cấp 1, cấp 2 hoặc các từ khóa (tags) phổ biến.</li>
                            <li class="mb-2"><strong>Đánh giá hữu ích:</strong> Sau khi đọc bài viết, bạn có thể nhấn "Hữu ích" để giúp hệ thống xếp hạng bài viết tốt hơn cho các bạn sinh viên khác.</li>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="accordion-item mb-3 border rounded shadow-none">
                <h2 class="accordion-header" id="headingThree">
                    <button class="accordion-button collapsed fs-4 fw-semibold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                        <i class="bi bi-ticket-perforated me-3 text-primary"></i>3. Tạo yêu cầu hỗ trợ (Ticket)
                    </button>
                </h2>
                <div id="collapseThree" class="accordion-collapse collapse" aria-labelledby="headingThree" data-bs-parent="#guideAccordion">
                    <div class="accordion-body pt-0 text-secondary">
                        <p>Nếu câu hỏi của bạn chưa có lời giải đáp trong FAQ hoặc bạn gặp vấn đề cá nhân cần giải quyết cụ thể:</p>
                        <ol class="mb-3">
                            <li class="mb-2">Đăng nhập tài khoản sinh viên và truy cập mục <strong>"Tạo yêu cầu"</strong> (đường dẫn <code>/student/tickets/new</code>).</li>
                            <li class="mb-2"><strong>Chọn phòng ban & chuyên mục:</strong> Lựa chọn đúng đơn vị quản lý (ví dụ: Phòng Đào tạo cho vấn đề đăng ký học phần, Phòng Tuyển sinh & CTSV cho chế độ chính sách/học bổng).</li>
                            <li class="mb-2"><strong>Soạn thảo nội dung:</strong> Điền tiêu đề ngắn gọn, súc tích và mô tả chi tiết hoàn cảnh, thông tin lớp học phần, mã môn... cần hỗ trợ.</li>
                            <li class="mb-2"><strong>Đính kèm minh chứng:</strong> Tải lên hình ảnh chụp lỗi màn hình, biên lai hoặc tài liệu PDF liên quan (dung lượng tối đa 10MB/tệp).</li>
                            <li class="mb-2">Nhấn <strong>"Gửi yêu cầu"</strong>. Hệ thống sẽ cấp một mã Ticket định danh để theo dõi xuyên suốt.</li>
                        </ol>
                    </div>
                </div>
            </div>

            <div class="accordion-item mb-3 border rounded shadow-none">
                <h2 class="accordion-header" id="headingFour">
                    <button class="accordion-button collapsed fs-4 fw-semibold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                        <i class="bi bi-chat-dots me-3 text-primary"></i>4. Trò chuyện trực tuyến (Chat)
                    </button>
                </h2>
                <div id="collapseFour" class="accordion-collapse collapse" aria-labelledby="headingFour" data-bs-parent="#guideAccordion">
                    <div class="accordion-body pt-0 text-secondary">
                        <p>Dành cho các trường hợp cần phản hồi nhanh chóng trong ngày:</p>
                        <ul class="mb-0">
                            <li class="mb-2"><strong>Khung giờ hoạt động:</strong> Kênh chat mở trong giờ làm việc hành chính (Sáng: 07:30 - 11:30, Chiều: 13:00 - 17:00, từ Thứ Hai đến Thứ Sáu).</li>
                            <li class="mb-2"><strong>Kết nối tư vấn viên:</strong> Chọn chủ đề cần tư vấn, hệ thống sẽ xếp hàng và kết nối bạn với chuyên viên tư vấn đang trực tuyến.</li>
                            <li class="mb-2"><strong>Lưu lịch sử:</strong> Toàn bộ nội dung trao đổi được lưu lại để đối chiếu và có thể chuyển thành Ticket xử lý tiếp nếu vấn đề phức tạp.</li>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="accordion-item mb-3 border rounded shadow-none">
                <h2 class="accordion-header" id="headingFive">
                    <button class="accordion-button collapsed fs-4 fw-semibold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFive" aria-expanded="false" aria-controls="collapseFive">
                        <i class="bi bi-calendar-check me-3 text-primary"></i>5. Đặt lịch hẹn tư vấn trực tiếp (Appointment)
                    </button>
                </h2>
                <div id="collapseFive" class="accordion-collapse collapse" aria-labelledby="headingFive" data-bs-parent="#guideAccordion">
                    <div class="accordion-body pt-0 text-secondary">
                        <p>Áp dụng cho các thủ tục hành chính bắt buộc nộp hồ sơ gốc, ký nhận giấy tờ hoặc phỏng vấn trực tiếp:</p>
                        <ol class="mb-3">
                            <li class="mb-2">Vào mục <strong>"Lịch hẹn"</strong> trên trang sinh viên.</li>
                            <li class="mb-2">Chọn đơn vị phòng ban cần đến làm việc và nội dung công việc.</li>
                            <li class="mb-2">Chọn ngày và khung giờ (slot) còn trống phù hợp với thời khóa biểu của bạn.</li>
                            <li class="mb-2">Xác nhận đặt lịch. Hệ thống gửi thông tin phiếu hẹn kèm mã QR qua email.</li>
                        </ol>
                        <div class="alert alert-warning py-2 px-3 small d-flex align-items-center mb-0">
                            <i class="bi bi-exclamation-triangle-fill me-2 fs-5"></i>
                            <div>Vui lòng đến đúng văn phòng trước giờ hẹn 5-10 phút. Nếu không thể đến, bạn có thể hủy lịch hẹn trước ít nhất 2 giờ để nhường khung giờ cho sinh viên khác.</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="accordion-item border rounded shadow-none">
                <h2 class="accordion-header" id="headingSix">
                    <button class="accordion-button collapsed fs-4 fw-semibold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseSix" aria-expanded="false" aria-controls="collapseSix">
                        <i class="bi bi-clock-history me-3 text-primary"></i>6. Theo dõi tiến độ & Đánh giá kết quả
                    </button>
                </h2>
                <div id="collapseSix" class="accordion-collapse collapse" aria-labelledby="headingSix" data-bs-parent="#guideAccordion">
                    <div class="accordion-body pt-0 text-secondary">
                        <p>QAUTE minh bạch toàn bộ quy trình giải quyết để sinh viên an tâm:</p>
                        <ul class="mb-3">
                            <li class="mb-2"><strong>Trạng thái Ticket:</strong>
                                <ul>
                                    <li><span class="badge bg-secondary me-1">Mới</span>: Yêu cầu vừa tạo, đang chờ chuyên viên tiếp nhận.</li>
                                    <li><span class="badge bg-primary me-1">Đang xử lý</span>: Chuyên viên đang thụ lý và phối hợp giải quyết.</li>
                                    <li><span class="badge bg-warning me-1">Chờ sinh viên</span>: Cần sinh viên bổ sung thêm thông tin hoặc minh chứng.</li>
                                    <li><span class="badge bg-success me-1">Đã giải quyết</span>: Vấn đề đã được phản hồi phương án xử lý chi tiết.</li>
                                    <li><span class="badge bg-dark me-1">Đã đóng</span>: Hoàn tất chu trình hỗ trợ.</li>
                                </ul>
                            </li>
                            <li class="mb-2"><strong>Đánh giá dịch vụ:</strong> Sau khi Ticket được giải quyết, bạn vui lòng cho điểm đánh giá mức độ hài lòng (từ 1 đến 5 sao) và nhận xét để nhà trường không ngừng nâng cao chất lượng phục vụ.</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="card bg-light border-0 p-4 text-center">
            <h3 class="h4 mb-2">Bạn vẫn còn câu hỏi chưa rõ?</h3>
            <p class="text-secondary mb-3">Tra cứu thông tin liên hệ các phòng ban chuyên trách hoặc liên hệ trực tiếp với chúng tôi.</p>
            <div class="d-flex justify-content-center gap-2">
                <a href="/faq" class="btn btn-outline-primary">Tìm kiếm FAQ</a>
            </div>
        </div>
    </div>
</div>
