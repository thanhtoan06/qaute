<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title>Bạn cần đăng nhập - QAUTE</title>
</head>
<body>
<div class="page page-center">
    <div class="container container-tight py-4">
        <div class="empty">
            <div class="empty-header">401</div>
            <p class="empty-title">Bạn cần đăng nhập</p>
            <p class="empty-subtitle text-secondary">Vui lòng đăng nhập để tiếp tục sử dụng chức năng này.</p>
            <div class="empty-action"><a class="btn btn-primary" href="/auth/login">Đăng nhập</a></div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>
