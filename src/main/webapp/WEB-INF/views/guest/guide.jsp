<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Hướng dẫn sử dụng QAUTE</title>
<style>
.qaute-guide-page { --guide-navy: #12234a; --guide-blue: #3865e9; --guide-teal: #12b8a7; --guide-muted: #718098; --guide-border: rgba(105, 127, 174, .17); color: #243651; padding-bottom: 2.5rem; }
.qaute-guide-page .guide-breadcrumb { margin-bottom: 1.25rem; font-size: .88rem; }
.qaute-guide-page .guide-breadcrumb .breadcrumb { margin-bottom: 0; }
.qaute-guide-page .guide-breadcrumb a { color: #70809a; text-decoration: none; transition: color .2s ease; }
.qaute-guide-page .guide-breadcrumb a:hover { color: var(--guide-blue); }
.qaute-guide-page .guide-breadcrumb .breadcrumb-item.active { color: #243651; font-weight: 600; }
.qaute-guide-page .guide-hero { position: relative; display: flex; align-items: center; box-sizing: border-box; isolation: isolate; height: 400px; min-height: 400px; margin-bottom: 2rem; padding: var(--guest-hero-padding); border-radius: 28px; background: radial-gradient(ellipse at 90% 4%, rgba(35, 217, 195, .25), transparent 34%), radial-gradient(ellipse at 4% 100%, rgba(77, 116, 255, .3), transparent 43%), linear-gradient(125deg, #101e42 0%, #182f63 52%, #15516e 100%); border: 1px solid rgba(255, 255, 255, .12); box-shadow: 0 24px 58px rgba(18, 39, 83, .18); }
.qaute-guide-page .guide-hero::before, .qaute-guide-page .guide-hero::after { content: ""; position: absolute; z-index: -1; pointer-events: none; border: 1px solid rgba(255, 255, 255, .14); border-radius: 50%; }
.qaute-guide-page .guide-hero::before { width: 355px; height: 355px; top: -185px; right: -95px; box-shadow: 0 0 0 28px rgba(255, 255, 255, .025), 0 0 0 62px rgba(255, 255, 255, .02); animation: qaute-orbit 25s linear infinite; }
.qaute-guide-page .guide-hero::after { width: 235px; height: 235px; right: 12%; bottom: -178px; border-color: rgba(93, 236, 219, .27); box-shadow: 0 0 0 24px rgba(93, 236, 219, .035); animation: qaute-orbit 19s linear infinite reverse; }
.qaute-guide-page .guide-hero-grid { position: relative; z-index: 1; display: flex; width: 100%; align-items: center; justify-content: space-between; gap: var(--guest-hero-gap); }
.qaute-guide-page .guide-hero-copy { max-width: 760px; }
.qaute-guide-page .guide-eyebrow { display: inline-flex; align-items: center; gap: .55rem; padding: .55rem .9rem; margin-bottom: 1.2rem; color: #d6fffa; background: rgba(255, 255, 255, .09); border: 1px solid rgba(255, 255, 255, .2); border-radius: 999px; backdrop-filter: blur(12px); font-size: .76rem; font-weight: 850; letter-spacing: .13em; text-transform: uppercase; }
.qaute-guide-page .guide-eyebrow i { color: #73f0df; font-size: 1rem; }
.qaute-guide-page .guide-hero h1 { margin: 0 0 1rem; font-size: clamp(2.05rem, 4.2vw, 3.5rem); font-weight: 900; line-height: 1.12; }
.qaute-guide-page .guide-hero h1 span { color: #8cecdf; background: linear-gradient(90deg, #90f2e3, #a9c9ff); -webkit-background-clip: text; background-clip: text; -webkit-text-fill-color: transparent; }
.qaute-guide-page .guide-hero-description { max-width: 680px; margin: 0; color: rgba(241, 247, 255, .8); font-size: 1rem; line-height: 1.9; }
.qaute-guide-page .guide-hero-meta { display: flex; flex-wrap: wrap; gap: .65rem; margin-top: 1.55rem; }
.qaute-guide-page .guide-meta-pill { display: inline-flex; align-items: center; gap: .5rem; padding: .6rem .8rem; color: #e6f4ff; background: rgba(255, 255, 255, .07); border: 1px solid rgba(255, 255, 255, .15); border-radius: 11px; font-size: .8rem; }
.qaute-guide-page .guide-meta-pill i { color: #83eddd; }
.qaute-guide-page .guide-hero-art { position: relative; flex: 0 0 var(--guest-hero-art-size); display: flex; width: var(--guest-hero-art-size); height: var(--guest-hero-art-size); align-items: center; justify-content: center; color: #a6f4e9; background: linear-gradient(145deg, rgba(255,255,255,.16), rgba(255,255,255,.035)); border: 1px solid rgba(255,255,255,.25); border-radius: 40px; box-shadow: inset 0 1px 0 rgba(255,255,255,.2), 0 22px 48px rgba(0,0,0,.16); backdrop-filter: blur(14px); transform: rotate(5deg); animation: qaute-float-rotate 5s ease-in-out infinite; }
@media (min-width: 768px) and (max-width: 991.98px) {
  .qaute-guide-page .guide-hero-art { flex-basis: var(--guest-hero-art-size-tablet); width: var(--guest-hero-art-size-tablet); height: var(--guest-hero-art-size-tablet); }
}
.qaute-guide-page .guide-hero-art i { font-size: var(--guest-hero-icon-size); filter: drop-shadow(0 8px 18px rgba(81, 227, 218, .22)); }
.qaute-guide-page .guide-hero-art::before, .qaute-guide-page .guide-hero-art::after { content: ""; position: absolute; border: 1px solid rgba(151, 245, 232, .35); border-radius: inherit; }
.qaute-guide-page .guide-hero-art::before { inset: -15px; }
.qaute-guide-page .guide-hero-art::after { inset: -29px; border-color: rgba(255, 255, 255, .1); }
.qaute-guide-page .guide-intro-heading { margin-bottom: 1.5rem; }
.qaute-guide-page .guide-section-kicker { display: inline-flex; align-items: center; gap: .45rem; margin-bottom: .55rem; color: var(--guide-blue); font-size: .75rem; font-weight: 850; letter-spacing: .13em; text-transform: uppercase; }
.qaute-guide-page .guide-intro-heading h2 { margin: 0 0 .45rem; color: var(--guide-navy); font-size: clamp(1.4rem, 2.5vw, 1.9rem); font-weight: 900; letter-spacing: -.035em; }
.qaute-guide-page .guide-intro-heading p { margin: 0; color: var(--guide-muted); line-height: 1.8; }
.qaute-guide-page #guideAccordion { display: grid; gap: 1rem; margin-bottom: 2rem !important; }
.qaute-guide-page #guideAccordion .accordion-item { position: relative; overflow: hidden; background: #fff; border: 1px solid #e2e9f4 !important; border-radius: 20px !important; box-shadow: 0 8px 25px rgba(27, 47, 91, .045); transition: border-color .3s ease, box-shadow .3s ease, transform .3s ease; }
.qaute-guide-page #guideAccordion .accordion-item:hover { border-color: #ccdafa !important; box-shadow: 0 14px 34px rgba(27, 47, 91, .085); }
.qaute-guide-page #guideAccordion .accordion-item:has(.accordion-button:not(.collapsed)) { border-color: rgba(56, 101, 233, .3) !important; box-shadow: 0 16px 38px rgba(33, 75, 164, .09); }
.qaute-guide-page #guideAccordion .accordion-header { margin: 0; }
.qaute-guide-page #guideAccordion .accordion-button { position: relative; display: flex; align-items: center; gap: 1rem; padding: 1.25rem 1.4rem; color: #243a5b; background: #fff; border: 0; box-shadow: none; font-size: clamp(.98rem, 1.7vw, 1.12rem); font-weight: 800 !important; line-height: 1.55; transition: color .25s ease, background .25s ease; }
.qaute-guide-page #guideAccordion .accordion-button::after { flex: 0 0 auto; width: 1rem; height: 1rem; background-size: 1rem; opacity: .65; transition: transform .25s ease, opacity .25s ease; }
.qaute-guide-page #guideAccordion .accordion-button:not(.collapsed) { color: #174d9c; background: linear-gradient(110deg, #f0f5ff 0%, #fff 90%); }
.qaute-guide-page #guideAccordion .accordion-button:not(.collapsed)::after { opacity: 1; }
.qaute-guide-page #guideAccordion .guide-step-icon { display: inline-flex; flex: 0 0 46px; width: 46px; height: 46px; align-items: center; justify-content: center; color: #3562df; background: linear-gradient(145deg, #edf2ff, #dfe9ff); border: 1px solid #e0e8ff; border-radius: 14px; font-size: 1.3rem; box-shadow: 0 5px 12px rgba(49, 91, 216, .07); transition: transform .3s ease, color .3s ease, background .3s ease; }
.qaute-guide-page #guideAccordion .accordion-button:not(.collapsed) .guide-step-icon { color: #fff; background: linear-gradient(145deg, #4774ed, #2852c8); border-color: transparent; transform: rotate(-4deg); box-shadow: 0 7px 16px rgba(49, 91, 216, .2); }
.qaute-guide-page #guideAccordion .guide-step-number { display: inline-flex; align-items: center; justify-content: center; flex: 0 0 27px; width: 27px; height: 27px; margin-left: auto; color: #70809a; background: #f2f5fa; border: 1px solid #e5eaf2; border-radius: 9px; font-size: .7rem; font-weight: 850; transition: color .25s ease, background .25s ease, border-color .25s ease; }
.qaute-guide-page #guideAccordion .accordion-button:not(.collapsed) .guide-step-number { color: #2557ce; background: #e4edff; border-color: #d0dfff; }
.qaute-guide-page #guideAccordion .accordion-body { padding: 0 1.5rem 1.5rem; color: #61718a; font-size: .94rem; line-height: 1.9; }
.qaute-guide-page #guideAccordion .accordion-body > p:first-child { padding-top: 1.1rem; border-top: 1px solid #edf1f8; }
.qaute-guide-page #guideAccordion .accordion-body p { margin-bottom: 1rem; }
.qaute-guide-page #guideAccordion .accordion-body strong { color: #2b405f; font-weight: 800; }
.qaute-guide-page #guideAccordion .accordion-body ol, .qaute-guide-page #guideAccordion .accordion-body ul { padding-left: 1.35rem; }
.qaute-guide-page #guideAccordion .accordion-body li { padding-left: .2rem; }
.qaute-guide-page #guideAccordion .accordion-body li::marker { color: #4775df; font-weight: 800; }
.qaute-guide-page #guideAccordion .accordion-body code { padding: .2rem .42rem; color: #2354c7; background: #edf3ff; border: 1px solid #dee8ff; border-radius: 6px; font-size: .86em; overflow-wrap: anywhere; }
.qaute-guide-page #guideAccordion .accordion-body .alert { gap: .65rem; border-radius: 13px; border: 1px solid rgba(32, 107, 196, .13); line-height: 1.8; }
.qaute-guide-page #guideAccordion .accordion-body .alert-info { background: linear-gradient(110deg, #eef6ff, #f6fbff); }
.qaute-guide-page #guideAccordion .accordion-body .alert-warning { background: linear-gradient(110deg, #fff8e9, #fffcf3); border-color: rgba(220, 160, 37, .2); }
.qaute-guide-page #guideAccordion .accordion-body .alert i { flex-shrink: 0; }
.qaute-guide-page #guideAccordion .accordion-body .badge { display: inline-flex; align-items: center; padding: .35rem .55rem; border-radius: 7px; font-size: .74rem; font-weight: 750; }
.qaute-guide-page .guide-status-list { display: grid; gap: .65rem; padding: 0 !important; margin: .8rem 0 1.1rem; list-style: none; }
.qaute-guide-page .guide-status-list li { display: flex; align-items: start; gap: .75rem; padding: .8rem; background: #f8faff; border: 1px solid #e8edf7; border-radius: 12px; }
.qaute-guide-page .guide-status-list li::marker { content: ""; }
.qaute-guide-page .guide-status-list .badge { flex-shrink: 0; margin-top: .1rem; }
.qaute-guide-page .guide-status-list span:last-child { min-width: 0; }
.qaute-guide-page .guide-cta { position: relative; isolation: isolate; overflow: hidden; padding: clamp(1.75rem, 4vw, 2.7rem); text-align: center; background: radial-gradient(ellipse at 100% 0%, rgba(42, 111, 228, .1), transparent 34%), linear-gradient(135deg, #f1f6ff, #e9f8f8); border: 1px solid #e0eaf6; border-radius: 24px; }
.qaute-guide-page .guide-cta::before, .qaute-guide-page .guide-cta::after { content: ""; position: absolute; z-index: -1; width: 180px; height: 180px; border: 1px solid rgba(57, 101, 222, .1); border-radius: 50%; pointer-events: none; }
.qaute-guide-page .guide-cta::before { left: -95px; top: -110px; box-shadow: 0 0 0 20px rgba(57, 101, 222, .025); }
.qaute-guide-page .guide-cta::after { right: -105px; bottom: -125px; border-color: rgba(18, 184, 167, .15); box-shadow: 0 0 0 22px rgba(18, 184, 167, .025); }
.qaute-guide-page .guide-cta-icon { display: inline-flex; align-items: center; justify-content: center; width: 54px; height: 54px; margin-bottom: 1rem; color: #315bd8; background: rgba(255,255,255,.9); border: 1px solid #dae6ff; border-radius: 17px; font-size: 1.5rem; box-shadow: 0 8px 20px rgba(35, 73, 160, .08); }
.qaute-guide-page .guide-cta h2 { margin: 0 0 .7rem; color: var(--guide-navy); font-size: clamp(1.35rem, 2.4vw, 1.8rem); font-weight: 900; letter-spacing: -.03em; }
.qaute-guide-page .guide-cta p { max-width: 650px; margin: 0 auto 1.25rem; color: #687892; line-height: 1.85; }
.qaute-guide-page .guide-cta .btn { display: inline-flex; align-items: center; justify-content: center; gap: .5rem; padding: .75rem 1.2rem; border-radius: 11px; font-weight: 800; transition: transform .25s ease, box-shadow .25s ease; }
.qaute-guide-page .guide-cta .btn:hover { transform: translateY(-3px); }
.qaute-guide-page .guide-cta .btn-outline-primary { background: rgba(255,255,255,.75); }
.qaute-guide-page .guide-cta .btn-outline-primary:hover { box-shadow: 0 8px 20px rgba(49, 91, 216, .13); }
@media (max-width: 767.98px) {
  .qaute-guide-page .guide-hero { height: 440px; min-height: 440px; align-items: flex-start; padding: 1.5rem 1.25rem; border-radius: var(--guest-hero-radius-mobile); }
  .qaute-guide-page .guide-hero-grid { align-items: flex-start; }
  .qaute-guide-page .guide-hero-art { display: none; }
  .qaute-guide-page .guide-hero::before { right: -220px; }
  .qaute-guide-page #guideAccordion .accordion-button { gap: .75rem; padding: 1rem; font-size: .98rem; }
  .qaute-guide-page #guideAccordion .guide-step-icon { flex-basis: 40px; width: 40px; height: 40px; font-size: 1.15rem; border-radius: 12px; }
  .qaute-guide-page #guideAccordion .guide-step-number { flex-basis: 25px; width: 25px; height: 25px; }
  .qaute-guide-page #guideAccordion .accordion-body { padding: 0 1rem 1.2rem; font-size: .91rem; }
  .qaute-guide-page #guideAccordion .accordion-body > p:first-child { padding-top: 1rem; }
  .qaute-guide-page .guide-hero-meta { gap: .5rem; }
  .qaute-guide-page .guide-meta-pill { font-size: .75rem; }
}
@media (max-width: 420px) {
  .qaute-guide-page .guide-hero { height: 440px; min-height: 440px; }
  .qaute-guide-page #guideAccordion .accordion-button { gap: .55rem; padding: .9rem .75rem; font-size: .91rem; }
  .qaute-guide-page #guideAccordion .guide-step-icon { flex-basis: 35px; width: 35px; height: 35px; font-size: 1rem; }
  .qaute-guide-page #guideAccordion .guide-step-number { display: none; }
  .qaute-guide-page .guide-hero-meta { flex-direction: column; align-items: flex-start; }
  .qaute-guide-page .guide-cta .d-flex { flex-direction: column; }
  .qaute-guide-page .guide-cta .btn { width: 100%; }
}
@media (prefers-reduced-motion: reduce) {
  .qaute-guide-page *, .qaute-guide-page *::before, .qaute-guide-page *::after { animation-duration: .01ms !important; animation-iteration-count: 1 !important; scroll-behavior: auto !important; transition-duration: .01ms !important; }
}
</style>




<div class="qaute-guide-page">
    <nav aria-label="breadcrumb" class="guide-breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Hướng dẫn</li>
        </ol>
    </nav>

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