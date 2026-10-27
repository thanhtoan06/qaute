<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Hướng dẫn sử dụng QAUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/guest/guide.css">
</head>





<div class="qaute-guide-page">
    <nav aria-label="breadcrumb" class="guide-breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Hướng dẫn</li>
        </ol>
    </nav>

    <%-- Giới thiệu tài liệu hướng dẫn sử dụng các tính năng QAUTE. --%>
    <section class="guide-hero">
        <div class="guide-hero-grid">
            <div class="guide-hero-copy">
                <div class="guide-eyebrow">
                    <i class="bi bi-compass-fill"></i>
                    QAUTE · USER GUIDE
                </div>
                <h1>Sử dụng QAUTE<br><span>tự tin và hiệu quả.</span></h1>
                <p class="guide-hero-description">
                    Tài liệu chi tiết hướng dẫn sinh viên khai thác tối đa các tiện ích tư vấn và hỗ trợ học vụ trực tuyến.
                </p>
                <div class="guide-hero-meta">
                    <span class="guide-meta-pill"><i class="bi bi-journal-check"></i> Sổ tay hướng dẫn</span>
                    <span class="guide-meta-pill"><i class="bi bi-mouse"></i> 6 mục nội dung</span>
                    <span class="guide-meta-pill"><i class="bi bi-lightning-charge-fill"></i> Tra cứu thuận tiện</span>
                </div>
            </div>
            <div class="guide-hero-art" aria-hidden="true">
                <i class="bi bi-journal-bookmark-fill"></i>
            </div>
        </div>
    </section>

    <div class="guide-intro-heading">
        <div class="guide-section-kicker">
            <i class="bi bi-book-half"></i>
            USER MANUAL
        </div>
        <h2>Khám phá từng tính năng</h2>
        <p>Chọn từng mục để xem hướng dẫn thao tác và những lưu ý quan trọng trong quá trình sử dụng hệ thống.</p>
    </div>

    <%-- Các hướng dẫn thao tác được nhóm trong danh sách có thể mở rộng. --%>
    <div class="accordion mb-5" id="guideAccordion">
        <div class="accordion-item mb-3 border rounded shadow-none">
            <h2 class="accordion-header" id="headingOne">
                <button class="accordion-button fs-4 fw-semibold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                    <span class="guide-step-icon"><i class="bi bi-box-arrow-in-right"></i></span>
                    <span class="flex-grow-1">1. Đăng nhập hệ thống</span>
                    <span class="guide-step-number">01</span>
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
                    <span class="guide-step-icon"><i class="bi bi-search"></i></span>
                    <span class="flex-grow-1">2. Tra cứu &amp; Tìm kiếm FAQ thông minh</span>
                    <span class="guide-step-number">02</span>
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
                    <span class="guide-step-icon"><i class="bi bi-ticket-perforated"></i></span>
                    <span class="flex-grow-1">3. Tạo yêu cầu hỗ trợ (Ticket)</span>
                    <span class="guide-step-number">03</span>
                </button>
            </h2>
            <div id="collapseThree" class="accordion-collapse collapse" aria-labelledby="headingThree" data-bs-parent="#guideAccordion">
                <div class="accordion-body pt-0 text-secondary">
                    <p>Nếu câu hỏi của bạn chưa có lời giải đáp trong FAQ hoặc bạn gặp vấn đề cá nhân cần giải quyết cụ thể:</p>
                    <ol class="mb-3">
                        <li class="mb-2">Đăng nhập tài khoản sinh viên và truy cập mục <strong>"Tạo yêu cầu"</strong> (đường dẫn <code>/student/tickets/new</code>).</li>
                        <li class="mb-2"><strong>Chọn phòng ban &amp; chuyên mục:</strong> Lựa chọn đúng đơn vị quản lý (ví dụ: Phòng Đào tạo cho vấn đề đăng ký học phần, Phòng Tuyển sinh &amp; CTSV cho chế độ chính sách/học bổng).</li>
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
                    <span class="guide-step-icon"><i class="bi bi-chat-dots"></i></span>
                    <span class="flex-grow-1">4. Trò chuyện trực tuyến (Chat)</span>
                    <span class="guide-step-number">04</span>
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
                    <span class="guide-step-icon"><i class="bi bi-calendar-check"></i></span>
                    <span class="flex-grow-1">5. Đặt lịch hẹn tư vấn trực tiếp (Appointment)</span>
                    <span class="guide-step-number">05</span>
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
                    <span class="guide-step-icon"><i class="bi bi-clock-history"></i></span>
                    <span class="flex-grow-1">6. Theo dõi tiến độ &amp; Đánh giá kết quả</span>
                    <span class="guide-step-number">06</span>
                </button>
            </h2>
            <div id="collapseSix" class="accordion-collapse collapse" aria-labelledby="headingSix" data-bs-parent="#guideAccordion">
                <div class="accordion-body pt-0 text-secondary">
                    <p>QAUTE minh bạch toàn bộ quy trình giải quyết để sinh viên an tâm:</p>
                    <ul class="mb-3">
                        <li class="mb-2">
                            <strong>Trạng thái Ticket:</strong>
                            <ul class="guide-status-list">
                                <li><span class="badge bg-secondary">Mới</span><span>Yêu cầu vừa tạo, đang chờ chuyên viên tiếp nhận.</span></li>
                                <li><span class="badge bg-primary">Đang xử lý</span><span>Chuyên viên đang thụ lý và phối hợp giải quyết.</span></li>
                                <li><span class="badge bg-warning text-dark">Chờ sinh viên</span><span>Cần sinh viên bổ sung thêm thông tin hoặc minh chứng.</span></li>
                                <li><span class="badge bg-success">Đã giải quyết</span><span>Vấn đề đã được phản hồi phương án xử lý chi tiết.</span></li>
                                <li><span class="badge bg-dark">Đã đóng</span><span>Hoàn tất chu trình hỗ trợ.</span></li>
                            </ul>
                        </li>
                        <li class="mb-2"><strong>Đánh giá dịch vụ:</strong> Sau khi Ticket được giải quyết, bạn vui lòng cho điểm đánh giá mức độ hài lòng (từ 1 đến 5 sao) và nhận xét để nhà trường không ngừng nâng cao chất lượng phục vụ.</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <%-- Liên kết đến các chức năng và kênh hỗ trợ sau khi đọc hướng dẫn. --%>
    <section class="guide-cta">
        <div class="guide-cta-icon">
            <i class="bi bi-chat-square-heart-fill"></i>
        </div>
        <h2>Bạn vẫn còn câu hỏi chưa rõ?</h2>
        <p>Tra cứu thông tin liên hệ các phòng ban chuyên trách hoặc liên hệ trực tiếp với chúng tôi.</p>
        <div class="d-flex justify-content-center gap-2">
            <a href="/faq" class="btn btn-outline-primary">
                <i class="bi bi-search"></i>
                Tìm kiếm FAQ
            </a>
        </div>
    </section>
</div>