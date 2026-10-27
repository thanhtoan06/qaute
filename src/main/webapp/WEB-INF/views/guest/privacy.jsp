<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Chính sách bảo mật</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/guest/privacy.css">
</head>





<div class="qaute-privacy-page">
    <nav aria-label="breadcrumb" class="privacy-breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Chính sách bảo mật</li>
        </ol>
    </nav>

    <%-- Tóm tắt cam kết bảo vệ quyền riêng tư của sinh viên. --%>
    <section class="privacy-hero">
        <div class="privacy-hero-content">
            <div class="privacy-hero-copy">
                <div class="privacy-eyebrow">
                    <i class="bi bi-shield-lock-fill"></i>
                    QAUTE · PRIVACY CENTER
                </div>
                <h1>Quyền riêng tư<br><span>là điều quan trọng.</span></h1>
                <p class="privacy-hero-description">
                    QAUTE cam kết bảo vệ thông tin cá nhân của sinh viên. Dữ liệu chỉ được sử dụng cho mục đích hỗ trợ tư vấn và không chia sẻ cho bên thứ ba.
                </p>
            </div>
            <div class="privacy-hero-art" aria-hidden="true">
                <i class="bi bi-shield-lock-fill"></i>
            </div>
        </div>
    </section>

    <%-- Mô tả cam kết bảo vệ và phạm vi sử dụng dữ liệu cá nhân. --%>
    <section class="privacy-content">
        <div class="privacy-content-heading">
            <div class="privacy-content-icon">
                <i class="bi bi-shield-check"></i>
            </div>
            <div>
                <h2>Cam kết bảo mật thông tin</h2>
                <p>Thông tin về cách QAUTE sử dụng dữ liệu trong hoạt động hỗ trợ sinh viên.</p>
            </div>
        </div>

        <div class="privacy-statement">
            <div class="privacy-statement-icon">
                <i class="bi bi-person-lock"></i>
            </div>
            <div>
                <h3>Bảo vệ thông tin cá nhân</h3>
                <p>QAUTE cam kết bảo vệ thông tin cá nhân của sinh viên.</p>
            </div>
        </div>

        <div class="privacy-statement">
            <div class="privacy-statement-icon">
                <i class="bi bi-database-lock"></i>
            </div>
            <div>
                <h3>Phạm vi sử dụng dữ liệu</h3>
                <p>Dữ liệu chỉ được sử dụng cho mục đích hỗ trợ tư vấn và không chia sẻ cho bên thứ ba.</p>
            </div>
        </div>
    </section>

    <%-- Liên kết đến kênh liên hệ để giải đáp thắc mắc về quyền riêng tư. --%>
    <section class="privacy-contact">
        <div class="privacy-contact-copy">
            <div class="privacy-contact-icon">
                <i class="bi bi-envelope-paper-heart"></i>
            </div>
            <div>
                <h2>Bạn có thắc mắc về quyền riêng tư?</h2>
                <p>Nếu có thắc mắc, vui lòng liên hệ qua trang Liên hệ để tìm kênh hỗ trợ phù hợp.</p>
            </div>
        </div>
        <a href="/pages/contact" class="privacy-contact-action">
            <i class="bi bi-arrow-up-right-circle"></i>
            Liên hệ QAUTE
        </a>
    </section>

    <div class="privacy-bottom-note">
        <i class="bi bi-shield-check"></i>
        <span>QAUTE · Chính sách bảo mật</span>
    </div>
</div>