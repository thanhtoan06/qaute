<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ attribute name="icon" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="title" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@ attribute name="message" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@ attribute name="actionUrl" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="actionLabel" type="java.lang.String" required="false" rtexprvalue="true" %>
<c:set var="emptyIcon" value="${empty icon ? 'bi-inbox' : icon}"/>
<div class="empty">
    <div class="empty-img"><i class="bi ${emptyIcon} display-4 text-secondary"></i></div>
    <p class="empty-title"><c:out value="${title}"/></p>
    <p class="empty-subtitle text-secondary"><c:out value="${message}"/></p>
    <c:if test="${not empty actionUrl and not empty actionLabel}">
        <div class="empty-action"><a class="btn btn-primary" href="${actionUrl}"><c:out value="${actionLabel}"/></a></div>
    </c:if>
</div>
