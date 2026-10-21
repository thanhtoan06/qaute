<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page import="org.sitemesh.content.Content" %>
<%
    Content pageContent = (Content) request.getAttribute("org.sitemesh.content.Content");
    String pageTitle = "Xác thực tài khoản - QAUTE";
    if (pageContent != null && pageContent.getExtractedProperties().getChild("title") != null) {
        String t = pageContent.getExtractedProperties().getChild("title").getValue();
        if (t != null && !t.isBlank()) {
            pageTitle = t + " - QAUTE HCMUTE";
        }
    }
    String currentUri = request.getRequestURI();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title><%= pageTitle %></title>
    <%= pageContent != null && pageContent.getExtractedProperties().getChild("head") != null ? pageContent.getExtractedProperties().getChild("head").getValue() : "" %>
</head>
<body class="bg-light">

<div class="hcmute-auth-wrapper">
    <div class="hcmute-auth-card">
        <%-- Bên trái: Hình ảnh khuôn viên trường HCMUTE + Nhận diện thương hiệu --%>
        <div class="hcmute-auth-left">
            <div class="hcmute-auth-left-bg" style="background-image: url('${pageContext.request.contextPath}/assets/images/campus-hcmute.png');"></div>

            <div class="hcmute-auth-left-content">
                <div class="d-flex align-items-center gap-2 mb-4">
                    <img src="${pageContext.request.contextPath}/assets/images/hcmute-logo.png"
                         alt="HCMUTE Logo"
                         class="rounded bg-white p-1"
                         style="height: 42px; width: auto;"
                         onerror="this.style.display='none';">
                    <div class="hcmute-logo-icon" style="background: rgba(255,255,255,0.2);">
                        <i class="bi bi-mortarboard-fill"></i>
                    </div>
                    <span class="fs-4 fw-bold text-white">
                        QA<span class="hcmute-brand-red">UTE</span>
                    </span>
                </div>

                <span class="badge bg-white text-primary px-3 py-1.5 rounded-pill fw-semibold mb-3">
                    <i class="bi bi-patch-check-fill me-1 text-primary"></i>Hệ thống tư vấn sinh viên HCMUTE
                </span>
                <h2 class="display-6 fw-bold text-white mb-3" style="line-height: 1.25;">
                    Đồng hành cùng sinh viên Công nghệ Kỹ thuật.
                </h2>
                <p class="text-white-50 leading-relaxed mb-4">
                    Tra cứu thông tin học vụ, gửi yêu cầu hỗ trợ, trò chuyện trực tuyến và kết nối chuyên viên tư vấn dễ dàng, tiện lợi.
                </p>

                <div class="d-flex flex-column gap-3 pt-2">
                    <div class="d-flex align-items-center gap-3">
                        <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center" style="width: 32px; height: 32px; flex-shrink: 0;">
                            <i class="bi bi-check-lg fw-bold"></i>
                        </div>
                        <span class="text-white small">Phản hồi nhanh chóng trong vòng 24 giờ làm việc</span>
                    </div>
                    <div class="d-flex align-items-center gap-3">
                        <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center" style="width: 32px; height: 32px; flex-shrink: 0;">
                            <i class="bi bi-check-lg fw-bold"></i>
                        </div>
                        <span class="text-white small">Đặt lịch hẹn tư vấn 1-1 trực tiếp hoặc Google Meet</span>
                    </div>
                    <div class="d-flex align-items-center gap-3">
                        <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center" style="width: 32px; height: 32px; flex-shrink: 0;">
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

        <%-- Bên phải: Khung đăng nhập dùng chung cho mọi vai trò --%>
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
            <%= pageContent != null && pageContent.getExtractedProperties().getChild("body") != null ? pageContent.getExtractedProperties().getChild("body").getValue() : "" %>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>
