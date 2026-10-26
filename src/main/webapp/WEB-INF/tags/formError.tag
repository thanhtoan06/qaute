<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="field" type="java.lang.String" required="true" rtexprvalue="true" %>
<%-- Hiển thị lỗi xác thực gắn với trường biểu mẫu được yêu cầu. --%>
<c:if test="${not empty errors[field]}">
    <div class="invalid-feedback d-block"><c:out value="${errors[field]}"/></div>
</c:if>
