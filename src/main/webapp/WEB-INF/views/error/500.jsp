<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title>Đã có lỗi hệ thống - QAUTE</title>
</head>
<body>
<div class="page page-center">
    <div class="container container-tight py-4">
        <div class="empty">
            <div class="empty-header">500</div>
            <p class="empty-title">Đã có lỗi hệ thống</p>
            <p class="empty-subtitle text-secondary">Hệ thống gặp sự cố. Vui lòng thử lại sau.</p>
            <c:if test="${not empty traceId}">
                <p class="text-secondary">Mã tham chiếu: <c:out value="${traceId}"/></p>
            </c:if>
            <div class="empty-action"><a class="btn btn-primary" href="/">Về trang chủ</a></div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>
