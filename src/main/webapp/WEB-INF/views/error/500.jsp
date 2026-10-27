<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title>Đã có lỗi hệ thống - QAUTE</title>
</head>
<body>
<%-- Thông báo lỗi hệ thống và mã tham chiếu nếu có để hỗ trợ tra cứu. --%>
<div class="page page-center">
    <div class="container container-tight py-4">
        <div class="empty">
            <div class="empty-header">500</div>
            <p class="empty-title">Đã có lỗi hệ thống</p>
            <p class="empty-subtitle text-secondary">Hệ thống gặp sự cố. Vui lòng thử lại sau.</p>
            <%-- Chỉ hiển thị mã tham chiếu khi có sẵn. --%>
            <c:if test="${not empty traceId}">
                <p class="text-secondary">Mã tham chiếu: <c:out value="${traceId}"/></p>
            </c:if>
<pre class="u-color-red u-text-align-left u-max-w-900px u-margin-20px-auto u-overflow-auto u-bg-fff u-padding-15px u-border-1px-solid-ccc">
Exception: ${requestScope['jakarta.servlet.error.exception']}
Message: ${requestScope['jakarta.servlet.error.message']}
<%
    // In dấu vết ngoại lệ khi yêu cầu chứa ngoại lệ.
    Throwable t = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
    if (t != null) {
        java.io.StringWriter sw = new java.io.StringWriter();
        t.printStackTrace(new java.io.PrintWriter(sw));
        out.print(sw.toString());
    }
%>
</pre>
            <div class="empty-action"><a class="btn btn-primary" href="/">Về trang chủ</a></div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>
