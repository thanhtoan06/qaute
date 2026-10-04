<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="successMessage" value="${not empty requestScope.flashSuccess ? requestScope.flashSuccess : sessionScope.flashSuccess}"/>
<c:set var="errorMessage" value="${not empty requestScope.flashError ? requestScope.flashError : sessionScope.flashError}"/>
<c:set var="warningMessage" value="${not empty requestScope.flashWarning ? requestScope.flashWarning : sessionScope.flashWarning}"/>
<c:if test="${not empty successMessage}">
    <div class="alert alert-success alert-dismissible" role="alert">
        <c:out value="${successMessage}"/>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button>
    </div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger alert-dismissible" role="alert">
        <c:out value="${errorMessage}"/>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button>
    </div>
</c:if>
<c:if test="${not empty warningMessage}">
    <div class="alert alert-warning alert-dismissible" role="alert">
        <c:out value="${warningMessage}"/>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button>
    </div>
</c:if>
