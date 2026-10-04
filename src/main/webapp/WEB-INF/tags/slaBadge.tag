<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ attribute name="sla" type="java.lang.String" required="true" rtexprvalue="true" %>
<c:if test="${not empty sla}">
    <c:set var="slaClass" value="bg-secondary-lt"/>
    <c:set var="slaLabel" value="${sla}"/>
    <c:choose>
        <c:when test="${sla == 'ON_TRACK'}"><c:set var="slaLabel" value="Đúng hạn"/><c:set var="slaClass" value="bg-green-lt"/></c:when>
        <c:when test="${sla == 'AT_RISK'}"><c:set var="slaLabel" value="Sắp quá hạn"/><c:set var="slaClass" value="bg-yellow-lt"/></c:when>
        <c:when test="${sla == 'BREACHED'}"><c:set var="slaLabel" value="Quá hạn"/><c:set var="slaClass" value="bg-red-lt"/></c:when>
    </c:choose>
    <span class="badge ${slaClass}"><c:out value="${slaLabel}"/></span>
</c:if>
