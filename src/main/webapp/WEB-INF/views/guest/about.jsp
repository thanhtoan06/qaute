<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Giới thiệu về QAUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/guest/about.css">
</head>


<div class="qaute-about-page">
<nav aria-label="breadcrumb" class="qa-about-breadcrumb"><ol class="breadcrumb"><li class="breadcrumb-item"><a href="/">Trang chủ</a></li><li class="breadcrumb-item active" aria-current="page">Giới thiệu</li></ol></nav>
<%-- Giới thiệu QAUTE và lối tắt đến FAQ cùng danh mục hỗ trợ. --%>
<section class="qa-about-hero"><div class="qa-about-hero-grid"><div>
<span class="qa-about-eyebrow"><i class="bi bi-stars"></i> Hệ thống hỏi đáp &amp; tư vấn sinh viên</span>
<h1>Về nền tảng hỗ trợ sinh viên <span>QAUTE</span></h1>
<p class="qa-about-hero-copy">Cầu nối trực tuyến hiện đại, minh bạch và nhanh chóng giữa sinh viên và các phòng ban, khoa, viện tại Trường Đại học Công nghệ Kỹ thuật TP.HCM (HCMUTE).</p>
<div class="qa-about-hero-actions"><a href="/faq" class="btn qa-about-btn-light"><i class="bi bi-search"></i> Khám phá FAQ <i class="bi bi-arrow-up-right"></i></a><a href="/topics" class="btn qa-about-btn-glass"><i class="bi bi-grid-1x2"></i> Xem chủ đề hỗ trợ</a></div>
</div><div class="qa-about-hero-art" aria-hidden="true">
<div class="qa-about-orbit"><span class="qa-about-orbit-dot"></span></div><div class="qa-about-campus-mark"><i class="bi bi-mortarboard"></i></div>
</div></div></section>

<%-- Trình bày mục tiêu xây dựng và các nguyên tắc hỗ trợ sinh viên. --%>
<section class="qa-about-section qa-about-mission"><div class="qa-about-mission-body"><div class="row align-items-stretch g-4 g-xl-5">
<div class="col-lg-7 qa-about-mission-copy"><span class="qa-about-kicker">Định hướng phát triển</span><h2>Mục tiêu <span>phát triển</span></h2><p>QAUTE được xây dựng nhằm chuẩn hóa và số hóa toàn diện quy trình tiếp nhận, giải quyết thắc mắc, kiến nghị và các thủ tục hành chính học vụ cho người học.</p>
<div class="qa-about-pillar"><div class="qa-about-pillar-icon"><i class="bi bi-stopwatch"></i></div><div><strong>Rút ngắn thời gian xử lý:</strong> Cam kết thời gian phản hồi rõ ràng theo chuẩn cam kết chất lượng dịch vụ (SLA).</div></div>
<div class="qa-about-pillar"><div class="qa-about-pillar-icon"><i class="bi bi-eye"></i></div><div><strong>Minh bạch tiến độ:</strong> Sinh viên dễ dàng theo dõi chi tiết từng trạng thái xử lý của yêu cầu.</div></div>
<div class="qa-about-pillar"><div class="qa-about-pillar-icon"><i class="bi bi-robot"></i></div><div><strong>Hỗ trợ 24/7 thông minh:</strong> Tự động gợi ý câu trả lời từ kho tri thức và hỏi đáp thường gặp (FAQ) phong phú.</div></div></div>
<div class="col-lg-5"><div class="qa-about-campus-panel"><div class="qa-about-campus-icon"><i class="bi bi-mortarboard"></i></div><h3 class="h4 mb-2">Đồng hành cùng sinh viên HCMUTE</h3><p>Mọi câu hỏi về học phần, học phí, học bổng, rèn luyện, ký túc xá và các thủ tục sinh viên đều được tiếp nhận và xử lý tận tâm.</p><div class="qa-about-actions"><a href="/faq" class="btn btn-outline-primary"><i class="bi bi-search me-1"></i>Khám phá FAQ</a><a href="/topics" class="btn btn-primary"><i class="bi bi-grid me-1"></i>Xem chủ đề hỗ trợ</a></div></div></div>
</div></div></section>

<%-- Tóm tắt các tính năng chính của cổng hỗ trợ. --%>
<section class="qa-about-section"><div class="qa-about-section-heading"><span class="qa-about-kicker">Một nền tảng, nhiều tiện ích</span><h2>Tính năng chính</h2><p>Những tiện ích vượt trội mà hệ thống QAUTE mang lại cho người học</p></div><div class="row g-3 g-xl-4">
<div class="col-md-6 col-lg-4"><div class="qa-about-feature"><div class="qa-about-feature-icon"><i class="bi bi-search"></i></div><h3>Tra cứu &amp; Tìm kiếm thông minh</h3><p>Tìm kiếm FAQ theo ngôn ngữ tự nhiên, gợi ý từ khóa tức thì và mở rộng từ đồng nghĩa tiện lợi.</p><span class="qa-about-feature-index">01</span></div></div>
<div class="col-md-6 col-lg-4"><div class="qa-about-feature qa-feature-green"><div class="qa-about-feature-icon"><i class="bi bi-ticket-perforated"></i></div><h3>Gửi Ticket yêu cầu hỗ trợ</h3><p>Tạo yêu cầu gửi đúng phòng ban thụ lý, đính kèm tài liệu minh chứng và theo dõi tiến trình.</p><span class="qa-about-feature-index">02</span></div></div>
<div class="col-md-6 col-lg-4"><div class="qa-about-feature qa-feature-cyan"><div class="qa-about-feature-icon"><i class="bi bi-chat-dots"></i></div><h3>Tư vấn trực tuyến (Chat)</h3><p>Trò chuyện trực tiếp cùng chuyên viên tư vấn trực ca để giải đáp các vấn đề cấp bách.</p><span class="qa-about-feature-index">03</span></div></div>
<div class="col-md-6 col-lg-4"><div class="qa-about-feature qa-feature-amber"><div class="qa-about-feature-icon"><i class="bi bi-calendar-check"></i></div><h3>Đặt lịch hẹn trực tiếp</h3><p>Chủ động chọn khung giờ hẹn làm việc trực tiếp tại văn phòng khoa/phòng ban để tránh xếp hàng.</p><span class="qa-about-feature-index">04</span></div></div>
<div class="col-md-6 col-lg-4"><div class="qa-about-feature qa-feature-rose"><div class="qa-about-feature-icon"><i class="bi bi-bell"></i></div><h3>Thông báo đa kênh</h3><p>Nhận thông báo cập nhật tiến độ yêu cầu ngay trên trang cá nhân và gửi qua hộp thư điện tử sinh viên.</p><span class="qa-about-feature-index">05</span></div></div>
<div class="col-md-6 col-lg-4"><div class="qa-about-feature qa-feature-violet"><div class="qa-about-feature-icon"><i class="bi bi-star"></i></div><h3>Đánh giá chất lượng phục vụ</h3><p>Chấm điểm độ hài lòng và góp ý phản hồi sau khi yêu cầu được hoàn tất nhằm nâng cao chất lượng.</p><span class="qa-about-feature-index">06</span></div></div>
</div></section>

<%-- Mô tả các bước tiếp nhận và giải quyết yêu cầu hỗ trợ. --%>
<section class="qa-about-section"><div class="qa-about-section-heading"><span class="qa-about-kicker">Từ câu hỏi đến lời giải đáp</span><h2>Quy trình hỗ trợ 5 bước</h2><p>Chu trình xử lý yêu cầu nhanh chóng, minh bạch và khép kín</p></div><div class="qa-about-process">
<div class="qa-about-step"><div class="qa-about-step-number">1</div><h3>Tra cứu hoặc Đặt câu hỏi</h3><p>Tìm câu trả lời trong FAQ hoặc tạo yêu cầu hỗ trợ mới.</p></div>
<div class="qa-about-step"><div class="qa-about-step-number">2</div><h3>Tiếp nhận &amp; Phân loại</h3><p>Hệ thống phân luồng yêu cầu đến đúng phòng ban chức năng.</p></div>
<div class="qa-about-step"><div class="qa-about-step-number">3</div><h3>Xử lý &amp; Phản hồi</h3><p>Cán bộ chuyên trách xử lý và giải đáp theo chuẩn thời gian SLA.</p></div>
<div class="qa-about-step"><div class="qa-about-step-number">4</div><h3>Nhận kết quả</h3><p>Sinh viên nhận thông tin giải quyết chi tiết trên hệ thống và email.</p></div>
<div class="qa-about-step"><div class="qa-about-step-number">5</div><h3>Đóng &amp; Đánh giá</h3><p>Sinh viên xác nhận hoàn thành và đánh giá mức độ hài lòng.</p></div>
</div></section>

<%-- Cung cấp liên kết để tra cứu FAQ hoặc đăng nhập gửi câu hỏi. --%>
<section class="qa-about-cta"><div class="qa-about-cta-copy"><span class="qa-about-cta-label"><i class="bi bi-chat-square-heart"></i> QAUTE luôn sẵn sàng hỗ trợ</span><h2>Bạn đã sẵn sàng đặt câu hỏi?</h2><p>Đăng nhập tài khoản sinh viên HCMUTE để gửi yêu cầu hoặc tra cứu ngay hàng trăm câu hỏi thường gặp đã được giải đáp.</p></div><div class="qa-about-cta-actions"><a href="/faq" class="btn qa-about-cta-secondary"><i class="bi bi-search"></i>Tra cứu câu hỏi FAQ</a><a href="/auth/login" class="btn qa-about-cta-primary"><i class="bi bi-box-arrow-in-right"></i>Đăng nhập tài khoản <i class="bi bi-arrow-up-right"></i></a></div></section>
</div>
