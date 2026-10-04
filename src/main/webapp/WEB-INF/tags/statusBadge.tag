<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ attribute name="status" type="java.lang.String" required="true" rtexprvalue="true" %>
<c:set var="statusClass" value="bg-secondary-lt"/>
<c:set var="statusLabel" value="${status}"/>
<c:choose>
    <c:when test="${status == 'NEW'}"><c:set var="statusLabel" value="Mới"/><c:set var="statusClass" value="bg-azure-lt"/></c:when>
    <c:when test="${status == 'ASSIGNED'}"><c:set var="statusLabel" value="Đã nhận"/><c:set var="statusClass" value="bg-blue-lt"/></c:when>
    <c:when test="${status == 'IN_PROGRESS'}"><c:set var="statusLabel" value="Đang xử lý"/><c:set var="statusClass" value="bg-orange-lt"/></c:when>
    <c:when test="${status == 'WAITING_STUDENT'}"><c:set var="statusLabel" value="Chờ sinh viên"/><c:set var="statusClass" value="bg-yellow-lt"/></c:when>
    <c:when test="${status == 'RESOLVED'}"><c:set var="statusLabel" value="Đã giải quyết"/><c:set var="statusClass" value="bg-green-lt"/></c:when>
    <c:when test="${status == 'CLOSED'}"><c:set var="statusLabel" value="Đã đóng"/></c:when>
    <c:when test="${status == 'CANCELLED'}"><c:set var="statusLabel" value="Đã hủy"/><c:set var="statusClass" value="bg-secondary-lt text-decoration-line-through"/></c:when>
</c:choose>
<span class="badge ${statusClass}"><c:out value="${statusLabel}"/></span>
