<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Giới thiệu về QAUTE</title>

<nav aria-label="breadcrumb" class="mb-4">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
        <li class="breadcrumb-item active" aria-current="page">Giới thiệu</li>
    </ol>
</nav>

<div class="row justify-content-center">
    <div class="col-lg-10">
        <div class="text-center mb-5">
            <span class="badge bg-primary-subtle text-primary px-3 py-2 mb-2 rounded-pill fs-6">Hệ thống hỏi đáp & tư vấn sinh viên</span>
            <h1 class="display-6 fw-bold mb-3">Về nền tảng hỗ trợ sinh viên QAUTE</h1>
            <p class="lead text-secondary mx-auto" style="max-width: 750px;">
                Cầu nối trực tuyến hiện đại, minh bạch và nhanh chóng giữa sinh viên và các phòng ban, khoa, viện tại Trường Đại học Công nghệ Kỹ thuật TP.HCM (HCMUTE).
            </p>
        </div>

        <div class="card mb-5 border-0 shadow-sm overflow-hidden">
            <div class="card-body p-4 p-md-5">
                <div class="row align-items-center g-4">
                    <div class="col-md-6">
                        <h2 class="h2 text-primary mb-3">Mục tiêu phát triển</h2>
                        <p class="text-secondary mb-3">
                            QAUTE được xây dựng nhằm chuẩn hóa và số hóa toàn diện quy trình tiếp nhận, giải quyết thắc mắc, kiến nghị và các thủ tục hành chính học vụ cho người học.
                        </p>
                        <ul class="list-unstyled mb-0">
                            <li class="d-flex align-items-start mb-3">
                                <i class="bi bi-check-circle-fill text-success fs-5 me-2 mt-1"></i>
                                <div><strong>Rút ngắn thời gian xử lý:</strong> Cam kết thời gian phản hồi rõ ràng theo chuẩn cam kết chất lượng dịch vụ (SLA).</div>
                            </li>
                            <li class="d-flex align-items-start mb-3">
                                <i class="bi bi-check-circle-fill text-success fs-5 me-2 mt-1"></i>
                                <div><strong>Minh bạch tiến độ:</strong> Sinh viên dễ dàng theo dõi chi tiết từng trạng thái xử lý của yêu cầu.</div>
                            </li>
                            <li class="d-flex align-items-start">
                                <i class="bi bi-check-circle-fill text-success fs-5 me-2 mt-1"></i>
                                <div><strong>Hỗ trợ 24/7 thông minh:</strong> Tự động gợi ý câu trả lời từ kho tri thức và hỏi đáp thường gặp (FAQ) phong phú.</div>
                            </li>
                        </ul>
                    </div>
                    <div class="col-md-6">
                        <div class="p-4 bg-light rounded-3 text-center border">
                            <div class="display-4 text-primary mb-2"><i class="bi bi-mortarboard"></i></div>
                            <h3 class="h4 mb-2">Đồng hành cùng sinh viên HCMUTE</h3>
                            <p class="text-secondary small mb-3">
                                Mọi câu hỏi về học phần, học phí, học bổng, rèn luyện, ký túc xá và các thủ tục sinh viên đều được tiếp nhận và xử lý tận tâm.
                            </p>
                            <a href="/faq" class="btn btn-outline-primary btn-sm me-2">Khám phá FAQ</a>
                            <a href="/topics" class="btn btn-primary btn-sm">Xem chủ đề hỗ trợ</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="mb-5">
            <div class="text-center mb-4">
                <h2 class="h2 mb-2">Tính năng chính</h2>
                <p class="text-secondary">Những tiện ích vượt trội mà hệ thống QAUTE mang lại cho người học</p>
            </div>
            <div class="row g-4">
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 p-3 shadow-none border">
                        <div class="fs-1 text-primary mb-2"><i class="bi bi-search"></i></div>
                        <h3 class="h5 mb-2">Tra cứu & Tìm kiếm thông minh</h3>
                        <p class="text-secondary small mb-0">Tìm kiếm FAQ theo ngôn ngữ tự nhiên, gợi ý từ khóa tức thì và mở rộng từ đồng nghĩa tiện lợi.</p>
                    </div>
                </div>
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 p-3 shadow-none border">
                        <div class="fs-1 text-success mb-2"><i class="bi bi-ticket-perforated"></i></div>
                        <h3 class="h5 mb-2">Gửi Ticket yêu cầu hỗ trợ</h3>
                        <p class="text-secondary small mb-0">Tạo yêu cầu gửi đúng phòng ban thụ lý, đính kèm tài liệu minh chứng và theo dõi tiến trình.</p>
                    </div>
                </div>
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 p-3 shadow-none border">
                        <div class="fs-1 text-info mb-2"><i class="bi bi-chat-dots"></i></div>
                        <h3 class="h5 mb-2">Tư vấn trực tuyến (Chat)</h3>
                        <p class="text-secondary small mb-0">Trò chuyện trực tiếp cùng chuyên viên tư vấn trực ca để giải đáp các vấn đề cấp bách.</p>
                    </div>
                </div>
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 p-3 shadow-none border">
                        <div class="fs-1 text-warning mb-2"><i class="bi bi-calendar-check"></i></div>
                        <h3 class="h5 mb-2">Đặt lịch hẹn trực tiếp</h3>
                        <p class="text-secondary small mb-0">Chủ động chọn khung giờ hẹn làm việc trực tiếp tại văn phòng khoa/phòng ban để tránh xếp hàng.</p>
                    </div>
                </div>
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 p-3 shadow-none border">
                        <div class="fs-1 text-danger mb-2"><i class="bi bi-bell"></i></div>
                        <h3 class="h5 mb-2">Thông báo đa kênh</h3>
                        <p class="text-secondary small mb-0">Nhận thông báo cập nhật tiến độ yêu cầu ngay trên trang cá nhân và gửi qua hộp thư điện tử sinh viên.</p>
                    </div>
                </div>
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 p-3 shadow-none border">
                        <div class="fs-1 text-secondary mb-2"><i class="bi bi-star"></i></div>
                        <h3 class="h5 mb-2">Đánh giá chất lượng phục vụ</h3>
                        <p class="text-secondary small mb-0">Chấm điểm độ hài lòng và góp ý phản hồi sau khi yêu cầu được hoàn tất nhằm nâng cao chất lượng.</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="mb-5">
            <div class="text-center mb-4">
                <h2 class="h2 mb-2">Quy trình hỗ trợ 5 bước</h2>
                <p class="text-secondary">Chu trình xử lý yêu cầu nhanh chóng, minh bạch và khép kín</p>
            </div>

            <div class="row g-3">
                <div class="col-md">
                    <div class="card h-100 text-center p-3 border">
                        <div class="avatar bg-primary-subtle text-primary rounded-circle mx-auto mb-3" style="width: 48px; height: 48px; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 1.25rem;">1</div>
                        <h3 class="h6 mb-2">Tra cứu hoặc Đặt câu hỏi</h3>
                        <p class="text-secondary small mb-0">Tìm câu trả lời trong FAQ hoặc tạo yêu cầu hỗ trợ mới.</p>
                    </div>
                </div>
                <div class="col-md">
                    <div class="card h-100 text-center p-3 border">
                        <div class="avatar bg-primary-subtle text-primary rounded-circle mx-auto mb-3" style="width: 48px; height: 48px; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 1.25rem;">2</div>
                        <h3 class="h6 mb-2">Tiếp nhận & Phân loại</h3>
                        <p class="text-secondary small mb-0">Hệ thống phân luồng yêu cầu đến đúng phòng ban chức năng.</p>
                    </div>
                </div>
                <div class="col-md">
                    <div class="card h-100 text-center p-3 border">
                        <div class="avatar bg-primary-subtle text-primary rounded-circle mx-auto mb-3" style="width: 48px; height: 48px; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 1.25rem;">3</div>
                        <h3 class="h6 mb-2">Xử lý & Phản hồi</h3>
                        <p class="text-secondary small mb-0">Cán bộ chuyên trách xử lý và giải đáp theo chuẩn thời gian SLA.</p>
                    </div>
                </div>
                <div class="col-md">
                    <div class="card h-100 text-center p-3 border">
                        <div class="avatar bg-primary-subtle text-primary rounded-circle mx-auto mb-3" style="width: 48px; height: 48px; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 1.25rem;">4</div>
                        <h3 class="h6 mb-2">Nhận kết quả</h3>
                        <p class="text-secondary small mb-0">Sinh viên nhận thông tin giải quyết chi tiết trên hệ thống và email.</p>
                    </div>
                </div>
                <div class="col-md">
                    <div class="card h-100 text-center p-3 border">
                        <div class="avatar bg-primary-subtle text-primary rounded-circle mx-auto mb-3" style="width: 48px; height: 48px; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 1.25rem;">5</div>
                        <h3 class="h6 mb-2">Đóng & Đánh giá</h3>
                        <p class="text-secondary small mb-0">Sinh viên xác nhận hoàn thành và đánh giá mức độ hài lòng.</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="qa-cta-section text-center p-5 rounded-3 mb-4">
            <h2 class="h2 mb-2">Bạn đã sẵn sàng đặt câu hỏi?</h2>
            <p class="text-secondary mb-4 mx-auto" style="max-width: 600px;">
                Đăng nhập tài khoản sinh viên HCMUTE để gửi yêu cầu hoặc tra cứu ngay hàng trăm câu hỏi thường gặp đã được giải đáp.
            </p>
            <div class="d-flex justify-content-center gap-3">
                <a href="/faq" class="btn btn-outline-primary btn-lg">Tra cứu câu hỏi FAQ</a>
                <a href="/auth/login" class="btn btn-primary btn-lg">Đăng nhập tài khoản</a>
            </div>
        </div>
    </div>
</div>
