<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title>Không tìm thấy trang - QAUTE</title>
</head>
<body>
<%-- Thông báo trang không tồn tại và cung cấp liên kết trang chủ, FAQ. --%>
<div class="page page-center">
    <div class="container container-tight py-4">
        <div class="empty">
            <div class="empty-header">404</div>
            <p class="empty-title">Không tìm thấy trang</p>
            <p class="empty-subtitle text-secondary">Trang bạn đang tìm kiếm không tồn tại hoặc đã được thay đổi.</p>
            <div class="empty-action">
                <a class="btn btn-primary me-2" href="/">Về trang chủ</a>
                <a class="btn btn-outline-primary" href="/faq">Tìm kiếm FAQ</a>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>
