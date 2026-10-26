<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title>Bạn thao tác quá nhanh - QAUTE</title>
</head>
<body>
<%-- Thông báo giới hạn tần suất và thời gian chờ nếu máy chủ có cung cấp. --%>
<div class="page page-center">
    <div class="container container-tight py-4">
        <div class="empty">
            <div class="empty-header">429</div>
            <p class="empty-title">Bạn thao tác quá nhanh</p>
            <p class="empty-subtitle text-secondary">Vui lòng chờ một lát rồi thử lại.</p>
            <%-- Hiển thị thời gian chờ khi máy chủ cung cấp giá trị. --%>
            <c:if test="${not empty retryAfter}">
                <p class="text-secondary">Bạn có thể thử lại sau <c:out value="${retryAfter}"/> giây.</p>
            </c:if>
            <div class="empty-action"><a class="btn btn-primary" href="/">Về trang chủ</a></div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>
