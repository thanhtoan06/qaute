<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title>Bạn không có quyền truy cập - QAUTE</title>
</head>
<body>
<div class="page page-center">
    <div class="container container-tight py-4">
        <div class="empty">
            <div class="empty-header">403</div>
            <p class="empty-title">Bạn không có quyền truy cập</p>
            <p class="empty-subtitle text-secondary">Bạn không được phép thực hiện thao tác hoặc xem nội dung này.</p>
            <div class="empty-action"><a class="btn btn-primary" href="/">Về trang chủ</a></div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>
