<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ attribute name="priority" type="java.lang.String" required="true" rtexprvalue="true" %>
<c:set var="priorityClass" value="bg-secondary-lt"/>
<c:set var="priorityLabel" value="${priority}"/>
<c:choose>
    <c:when test="${priority == 'LOW'}"><c:set var="priorityLabel" value="Thấp"/></c:when>
    <c:when test="${priority == 'NORMAL'}"><c:set var="priorityLabel" value="Bình thường"/><c:set var="priorityClass" value="bg-blue-lt"/></c:when>
    <c:when test="${priority == 'HIGH'}"><c:set var="priorityLabel" value="Cao"/><c:set var="priorityClass" value="bg-orange-lt"/></c:when>
    <c:when test="${priority == 'URGENT'}"><c:set var="priorityLabel" value="Khẩn cấp"/><c:set var="priorityClass" value="bg-red-lt"/></c:when>
</c:choose>
<span class="badge ${priorityClass}"><c:out value="${priorityLabel}"/></span>
