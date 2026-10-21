<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Kiểm tra hệ thống</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <div>
    <h2 class="page-title mb-1">Kiểm tra hệ thống</h2>
    <div class="text-secondary">Trạng thái được đo khi tải trang này.</div>
  </div>
  <a class="btn btn-outline-primary" href="${ctx}/admin/health">Làm mới</a>
</div>

<c:if test="${not empty flashSuccess}"><div class="alert alert-success"><c:out value="${flashSuccess}"/></div></c:if>
<c:if test="${not empty flashError}"><div class="alert alert-danger"><c:out value="${flashError}"/></div></c:if>

<div class="row g-3">
  <c:forEach var="entry" items="${checks}">
    <c:set var="check" value="${entry.value}"/>
    <div class="col-12 col-md-6 col-xl-4">
      <div class="card h-100"><div class="card-body">
        <div class="d-flex align-items-start justify-content-between gap-2 mb-3">
          <h3 class="card-title mb-0"><c:out value="${check.name()}"/></h3>
          <c:choose>
            <c:when test="${check.status() eq 'OK'}"><span class="badge bg-green-lt">Tốt</span></c:when>
            <c:when test="${check.status() eq 'WARN'}"><span class="badge bg-yellow-lt">Cảnh báo</span></c:when>
            <c:otherwise><span class="badge bg-red-lt">Lỗi</span></c:otherwise>
          </c:choose>
        </div>
        <p class="text-secondary mb-0"><c:out value="${check.detail()}"/></p>
      </div></div>
    </div>
  </c:forEach>
</div>

<div class="card mt-4"><div class="card-body d-flex flex-wrap align-items-center justify-content-between gap-3">
  <div>
    <h3 class="card-title mb-1">Gửi email thử</h3>
    <div class="text-secondary">Thư được thêm vào hàng đợi để gửi tới email của tài khoản Admin hiện tại.</div>
    <c:if test="${not emailConfigured}"><div class="text-warning mt-1">Cần cấu hình tài khoản gửi email trước khi thử.</div></c:if>
  </div>
  <form method="post" action="${ctx}/admin/health/test-email" class="m-0">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button class="btn btn-primary" type="submit" ${emailConfigured ? '' : 'disabled'}>Gửi thư thử</button>
  </form>
</div></div>
