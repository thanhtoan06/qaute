<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ attribute name="id" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@ attribute name="title" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@ attribute name="message" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@ attribute name="actionUrl" type="java.lang.String" required="true" rtexprvalue="true" %>
<%@ attribute name="confirmLabel" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="cssClass" type="java.lang.String" required="false" rtexprvalue="true" %>
<c:set var="modalConfirmLabel" value="${empty confirmLabel ? 'Xác nhận' : confirmLabel}"/>
<c:set var="modalCssClass" value="${empty cssClass ? 'btn-danger' : cssClass}"/>
<button type="button" class="btn ${modalCssClass}" data-bs-toggle="modal" data-bs-target="#${id}">
    <c:out value="${modalConfirmLabel}"/>
</button>
<div class="modal modal-blur fade" id="${id}" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><c:out value="${title}"/></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body"><c:out value="${message}"/></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-link" data-bs-dismiss="modal">Hủy</button>
                <form method="post" action="${actionUrl}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="btn ${modalCssClass}"><c:out value="${modalConfirmLabel}"/></button>
                </form>
            </div>
        </div>
    </div>
</div>
