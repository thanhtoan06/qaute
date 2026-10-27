<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page import="org.sitemesh.content.Content" %>
<%-- Lấy nội dung trang, xác định tiêu đề và URI hiện tại cho trang xác thực.
--%><%
    Content pageContent = (Content) request.getAttribute("org.sitemesh.content.Content");
    String pageTitle = "Xác thực tài khoản - QAUTE";
    if (pageContent != null && pageContent.getExtractedProperties().getChild("title") != null) {
        String t = pageContent.getExtractedProperties().getChild("title").getValue();
        if (t != null && !"null".equals(t) && !t.isBlank()) {
            pageTitle = t + " - QAUTE HCMUTE";
        }
    }
    String currentUri = request.getRequestURI();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layouts/auth.css">

    <title><%= pageTitle %></title>
    <%= pageContent != null && pageContent.getExtractedProperties().getChild("head") != null
            && pageContent.getExtractedProperties().getChild("head").getValue() != null
            && !"null".equals(pageContent.getExtractedProperties().getChild("head").getValue())
            ? pageContent.getExtractedProperties().getChild("head").getValue() : "" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/inline-utilities.css">

</head>
<body class="bg-light">
    <%-- Khung đăng nhập/đăng ký gồm giới thiệu dịch vụ và nội dung xác thực hiện tại. --%>
    <div class="hcmute-auth-wrapper">
        <%-- Bảng giới thiệu thương hiệu, tiện ích hỗ trợ và thông tin trường. --%>
        <div class="hcmute-auth-card">
            <div class="hcmute-auth-left">
                <div class="hcmute-auth-left-bg"></div>
                <div class="hcmute-auth-left-content">
                    <div class="d-flex align-items-center gap-2 mb-4">
                        <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png" alt="HCMUTE Logo" class="rounded bg-white p-1 u-height-42px u-width-auto" data-image-error="hide">
                        <div class="hcmute-logo-icon u-bg-rgba-255-255-255-0-2">
                            <i class="bi bi-mortarboard-fill"></i>
                        </div>
                        <span class="fs-4 fw-bold text-white">
                            QA<span class="hcmute-brand-red">UTE</span>
                        </span>
                    </div>

                    <span class="badge bg-white text-primary px-3 py-1.5 rounded-pill fw-semibold mb-3">
                        <i class="bi bi-patch-check-fill me-1 text-primary"></i>Hệ thống tư vấn sinh viên HCMUTE
                    </span>

                    <h2 class="display-6 fw-bold text-white mb-3 u-line-height-1-25">
                        Đồng hành cùng sinh viên Công nghệ Kỹ thuật.
                    </h2>

                    <p class="text-white-50 leading-relaxed mb-4">
                        Tra cứu thông tin học vụ, gửi yêu cầu hỗ trợ, trò chuyện trực tuyến và kết nối chuyên viên tư vấn dễ dàng, tiện lợi.
                    </p>

                    <div class="d-flex flex-column gap-3 pt-2">
                        <div class="d-flex align-items-center gap-3">
                            <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center u-width-32px u-height-32px u-flex-shrink-0">
                                <i class="bi bi-check-lg fw-bold"></i>
                            </div>
                            <span class="text-white small">Phản hồi nhanh chóng trong vòng 24 giờ làm việc</span>
                        </div>

                        <div class="d-flex align-items-center gap-3">
                            <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center u-width-32px u-height-32px u-flex-shrink-0">
                                <i class="bi bi-check-lg fw-bold"></i>
                            </div>
                            <span class="text-white small">Đặt lịch hẹn tư vấn 1-1 trực tiếp hoặc Google Meet</span>
                        </div>

                        <div class="d-flex align-items-center gap-3">
                            <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center u-width-32px u-height-32px u-flex-shrink-0">
                                <i class="bi bi-check-lg fw-bold"></i>
                            </div>
                            <span class="text-white small">Bảo mật thông tin sinh viên tuyệt đối</span>
                        </div>
                    </div>
                </div>

                <div class="hcmute-auth-left-content pt-4 border-top border-white-10">
                    <p class="text-white-50 small mb-0">
                        <i class="bi bi-geo-alt me-1 text-warning"></i>01 Võ Văn Ngân, P. Linh Chiểu, TP. Thủ Đức, TP.HCM
                    </p>
                </div>
            </div>

            <%-- Khu vực hiển thị thông báo và biểu mẫu xác thực của trang con. --%>
            <div class="hcmute-auth-right">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <a href="/" class="text-secondary text-decoration-none small d-inline-flex align-items-center hover-primary">
                        <i class="bi bi-arrow-left me-1"></i>Trang chủ QAUTE
                    </a>
                    <div class="d-lg-none d-flex align-items-center gap-1">
                        <span class="fw-bold text-primary">QA<span class="text-danger">UTE</span></span>
                    </div>
                </div>

                <qa:alert/>

                <%= pageContent != null && pageContent.getExtractedProperties().getChild("body") != null
                        && pageContent.getExtractedProperties().getChild("body").getValue() != null
                        && !"null".equals(pageContent.getExtractedProperties().getChild("body").getValue())
                        ? pageContent.getExtractedProperties().getChild("body").getValue() : "" %>
            </div>
        </div>
    </div>

    <%-- Nạp các script dùng chung cho trang xác thực. --%>
    <%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>