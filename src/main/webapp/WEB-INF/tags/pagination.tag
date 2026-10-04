<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true" %>
<%@ attribute name="baseUrl" type="java.lang.String" required="true" %>
<%@ attribute name="params" type="java.lang.String" required="false" rtexprvalue="true" %>
<c:if test="${page.totalPages > 1}">
    <c:set var="queryPrefix" value="${empty params ? '' : params.concat('&')}"/>
    <nav aria-label="Phân trang">
        <ul class="pagination">
            <li class="page-item${page.first ? ' disabled' : ''}">
                <a class="page-link" href="${baseUrl}?${queryPrefix}page=${page.number - 1}" aria-label="Trang trước">Trước</a>
            </li>
            <c:choose>
                <c:when test="${page.totalPages <= 7}">
                    <c:forEach begin="0" end="${page.totalPages - 1}" var="pageNumber">
                        <li class="page-item${pageNumber == page.number ? ' active' : ''}">
                            <a class="page-link" href="${baseUrl}?${queryPrefix}page=${pageNumber}">${pageNumber + 1}</a>
                        </li>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <li class="page-item${page.number == 0 ? ' active' : ''}">
                        <a class="page-link" href="${baseUrl}?${queryPrefix}page=0">1</a>
                    </li>
                    <c:if test="${page.number > 3}">
                        <li class="page-item disabled"><span class="page-link">…</span></li>
                    </c:if>
                    <c:forEach begin="${page.number > 2 ? page.number - 1 : 1}"
                               end="${page.number < page.totalPages - 3 ? page.number + 1 : page.totalPages - 2}"
                               var="pageNumber">
                        <li class="page-item${pageNumber == page.number ? ' active' : ''}">
                            <a class="page-link" href="${baseUrl}?${queryPrefix}page=${pageNumber}">${pageNumber + 1}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${page.number < page.totalPages - 4}">
                        <li class="page-item disabled"><span class="page-link">…</span></li>
                    </c:if>
                    <li class="page-item${page.number == page.totalPages - 1 ? ' active' : ''}">
                        <a class="page-link" href="${baseUrl}?${queryPrefix}page=${page.totalPages - 1}">${page.totalPages}</a>
                    </li>
                </c:otherwise>
            </c:choose>
            <li class="page-item${page.last ? ' disabled' : ''}">
                <a class="page-link" href="${baseUrl}?${queryPrefix}page=${page.number + 1}" aria-label="Trang sau">Sau</a>
            </li>
        </ul>
    </nav>
</c:if>
