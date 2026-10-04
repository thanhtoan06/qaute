<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ attribute name="field" type="java.lang.String" required="true" rtexprvalue="true" %>
<c:if test="${not empty errors[field]}">
    <div class="invalid-feedback d-block"><c:out value="${errors[field]}"/></div>
</c:if>
