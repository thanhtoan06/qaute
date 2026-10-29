<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Duyệt FAQ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/faq-review.css">
</head>
<div class="qa-page-admin-faq-review" data-qa-view="admin-faq-review">

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- Tiêu đề trang và các nút điều hướng -->
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
  <h2 class="page-title mb-0 qa-view-title" data-qa-page="admin-faq-review"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span>Xem trước FAQ</h2>
  <div class="d-flex gap-2"><a class="btn btn-outline-primary" href="${ctx}/admin/faq/${faq.id()}/revisions">Phiên bản</a><a class="btn btn-outline-secondary" href="${ctx}/admin/faq">Danh sách FAQ</a></div>
</div>

<div class="row g-3">
  <div class="col-lg-8">
    <%-- Xem trước nội dung FAQ và siêu dữ liệu trước khi duyệt. --%>
    <!-- Khu vực xem trước nội dung FAQ -->
    <article class="card card-body">
      <div class="text-secondary small mb-2"><c:out value="${faq.categoryPath()}"/></div>
      <h1 class="h1"><c:out value="${faq.title()}"/></h1>
      <div class="text-secondary small border-bottom pb-3 mb-3">
        Tác giả: <c:out value="${faq.authorName()}"/> · Trạng thái: <c:out value="${faq.status()}"/>
        <c:if test="${not empty faq.publishAt()}"> · Hẹn giờ: <time datetime="${faq.publishAt()}" data-qaute-datetime><c:out value="${faq.publishAt()}"/></time></c:if>
      </div>
      <c:if test="${not empty faq.summary()}"><div class="lead text-secondary bg-light rounded p-3 mb-4"><c:out value="${faq.summary()}"/></div></c:if>
      <div class="qa-faq-content lh-lg"><c:out value="${faq.bodyHtml()}" escapeXml="false"/></div>
      <c:if test="${not empty faq.tags()}"><div class="border-top pt-3 mt-4"><c:forEach var="tag" items="${faq.tags()}"><span class="badge bg-blue-lt me-1">#<c:out value="${tag}"/></span></c:forEach></div></c:if>
    </article>
  </div>

  <%-- Duyệt, từ chối, xuất bản hoặc xem lịch sử ghi chú của bài viết. --%>
  <!-- Khu vực quyết định và lịch sử -->
  <div class="col-lg-4">
    <div class="card mb-3"><div class="card-body">
      <h3 class="card-title">Quyết định</h3>
      <!-- Các hành động theo trạng thái FAQ -->
      <c:choose>
        <c:when test="${faq.status() eq 'PENDING_REVIEW'}">
          <!-- Form duyệt và xuất bản -->
          <form method="post" action="${ctx}/admin/faq/${faq.id()}/approve" class="mb-3">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <label class="form-label" for="approve-at">Hẹn giờ xuất bản (để trống để xuất bản ngay)</label>
            <input class="form-control mb-2" id="approve-at" type="datetime-local" name="publishAt"/>
            <button class="btn btn-success w-100" type="submit">Duyệt và xuất bản</button>
          </form>
          <!-- Form từ chối -->
          <form method="post" action="${ctx}/admin/faq/${faq.id()}/reject">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <label class="form-label required" for="reject-reason">Lý do từ chối</label>
            <textarea class="form-control mb-2" id="reject-reason" name="reason" minlength="10" maxlength="500" rows="3" required></textarea>
            <button class="btn btn-outline-danger w-100" type="submit">Từ chối và trả về nháp</button>
          </form>
        </c:when>
        <c:when test="${faq.status() eq 'DRAFT'}">
          <p class="text-secondary">Bài đang là bản nháp.</p>
          <qa:confirmModal id="submit-review-${faq.id()}" title="Gửi duyệt FAQ" message="Gửi bài viết này tới các quản trị viên để duyệt?" actionUrl="${ctx}/admin/faq/${faq.id()}/submit" confirmLabel="Gửi duyệt" cssClass="btn-outline-primary"/>
          <form method="post" action="${ctx}/admin/faq/${faq.id()}/approve" class="mt-3">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="direct" value="true"/>
            <label class="form-label" for="direct-publish-at">Hẹn giờ xuất bản trực tiếp</label>
            <input class="form-control mb-2" id="direct-publish-at" type="datetime-local" name="publishAt"/>
            <button class="btn btn-primary w-100" type="submit">Xuất bản trực tiếp</button>
          </form>
        </c:when>
        <c:when test="${faq.status() eq 'PUBLISHED'}">
          <qa:confirmModal id="archive-review-${faq.id()}" title="Lưu trữ FAQ" message="Lưu trữ bài viết này?" actionUrl="${ctx}/admin/faq/${faq.id()}/archive" confirmLabel="Lưu trữ" requireReason="${true}"/>
        </c:when>
        <c:otherwise><qa:confirmModal id="restore-review-${faq.id()}" title="Khôi phục FAQ" message="Khôi phục bài viết thành bản nháp?" actionUrl="${ctx}/admin/faq/${faq.id()}/restore" confirmLabel="Khôi phục" cssClass="btn-outline-primary"/></c:otherwise>
      </c:choose>
    </div></div>

    <!-- Lịch sử các phiên bản -->
    <div class="card"><div class="card-body">
      <h3 class="card-title">Lịch sử ghi chú</h3>
      <c:choose><c:when test="${empty faq.revisions()}"><p class="text-secondary mb-0">Chưa có phiên bản lưu.</p></c:when>
      <c:otherwise><div class="list-group list-group-flush" data-tv2-pageable"><c:forEach var="revision" items="${faq.revisions()}"><div class="list-group-item px-0"><div class="fw-semibold">Phiên bản ${revision.versionNo()}</div><div class="small text-secondary"><time datetime="${revision.createdAt()}" data-qaute-datetime><c:out value="${revision.createdAt()}"/></time> · Người sửa #<c:out value="${revision.changedBy()}"/></div><c:if test="${not empty revision.changeNote()}"><p class="mb-0 mt-1"><c:out value="${revision.changeNote()}"/></p></c:if></div></c:forEach></div></c:otherwise></c:choose>
    </div></div>
  </div>
</div>
</div>
