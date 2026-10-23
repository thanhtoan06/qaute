<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Thông báo</title>
<div class="container py-4">
  <h1 class="h2 mb-4">Thông báo</h1>
  <div class="row g-3">
    <c:forEach var="a" items="${announcements}">
      <div class="col-12">
        <div class="card qa-card-hover">
          <div class="card-body">
            <h3 class="card-title mb-1"><a href="/announcements/${a.id()}" class="text-reset text-decoration-none"><c:out value="${a.title()}"/></a></h3>
            <p class="text-secondary small mb-2"><c:out value="${a.summary()}"/></p>
            <span class="text-muted small"><i class="bi bi-calendar3 me-1"></i>${a.publishAt()}</span>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
  <nav class="mt-4">
    <ul class="pagination justify-content-center">
      <c:if test="${page.hasPrevious()}">
        <li class="page-item"><a class="page-link" href="/announcements?page=${page.number-1}">Trước</a></li>
      </c:if>
      <c:forEach var="i" begin="0" end="${page.totalPages - 1}">
        <li class="page-item ${i == page.number ? 'active' : ''}">
          <a class="page-link" href="/announcements?page=${i}">${i + 1}</a>
        </li>
      </c:forEach>
      <c:if test="${page.hasNext()}">
        <li class="page-item"><a class="page-link" href="/announcements?page=${page.number+1}">Sau</a></li>
      </c:if>
    </ul>
  </nav>
</div>