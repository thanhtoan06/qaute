<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Điều khoản sử dụng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/guest/terms.css">
</head>





<div class="qaute-terms-page">
    <nav aria-label="breadcrumb" class="terms-breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Điều khoản sử dụng</li>
        </ol>
    </nav>

    <%-- Giới thiệu mục đích và phạm vi của điều khoản sử dụng QAUTE. --%>
    <section class="terms-hero">
        <div class="terms-hero-content">
            <div class="terms-hero-copy">
                <div class="terms-eyebrow">
                    <i class="bi bi-file-earmark-lock2-fill"></i>
                    QAUTE · TERMS OF USE
                </div>
                <h1>Sử dụng có trách nhiệm.<br><span>Kết nối hiệu quả.</span></h1>
                <p class="terms-hero-description">
                    Điều khoản sử dụng định hướng việc sử dụng hệ thống QAUTE trong hoạt động tư vấn và hỗ trợ sinh viên.
                </p>
            </div>
            <div class="terms-hero-art" aria-hidden="true">
                <i class="bi bi-file-earmark-check-fill"></i>
            </div>
        </div>
    </section>

    <%-- Trình bày các điều khoản và lưu ý dành cho người sử dụng hệ thống. --%>
    <section class="terms-content">
        <div class="terms-content-heading">
            <div class="terms-content-icon">
                <i class="bi bi-journal-check"></i>
            </div>
            <div>
                <h2>Điều khoản sử dụng QAUTE</h2>
                <p>Thông tin cơ bản dành cho người sử dụng hệ thống tư vấn sinh viên.</p>
            </div>
        </div>

        <div class="terms-statement">
            <div class="terms-statement-icon">
                <i class="bi bi-check2-circle"></i>
            </div>
            <div class="terms-statement-copy">
                <h3>Tuân thủ quy định khi sử dụng hệ thống</h3>
                <p>Bằng việc sử dụng QAUTE, bạn đồng ý tuân thủ các quy định của nhà trường và hệ thống tư vấn sinh viên.</p>
            </div>
        </div>
    </section>

    <%-- Cung cấp liên kết đến trang liên hệ khi cần hỗ trợ thêm. --%>
    <section class="terms-contact">
        <div class="terms-contact-copy">
            <div class="terms-contact-icon">
                <i class="bi bi-headset"></i>
            </div>
            <div>
                <h2>Cần hỗ trợ thêm?</h2>
                <p>Liên hệ với bộ phận phụ trách qua trang Liên hệ nếu bạn cần hỗ trợ.</p>
            </div>
        </div>
        <a href="/pages/contact" class="terms-contact-action">
            <i class="bi bi-arrow-up-right-circle"></i>
            Liên hệ QAUTE
        </a>
    </section>

    <div class="terms-bottom-note">
        <i class="bi bi-shield-check"></i>
        <span>QAUTE · Điều khoản sử dụng</span>
    </div>
</div>