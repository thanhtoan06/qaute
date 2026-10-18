<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Không gian xử lý yêu cầu</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ticket" value="${detail.ticket()}"/>
<c:set var="student" value="${detail.student()}"/>

<div class="d-flex justify-content-between align-items-center mb-3 gap-2">
  <div><a class="small text-secondary" href="${ctx}/manager/queue">&larr; Hàng đợi</a><h2 class="page-title mb-0"><c:out value="${ticket.title()}"/></h2></div>
  <span class="text-secondary"><c:out value="${ticket.code()}"/></span>
</div>

<div class="row g-3">
  <aside class="col-12 col-lg-3">
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Sinh viên</h3></div><div class="card-body">
      <div class="fw-semibold mb-2"><c:out value="${student.user().fullName()}"/></div>
      <dl class="mb-0">
        <dt>MSSV</dt><dd><c:out value="${student.user().mssv()}"/></dd>
        <dt>Khoa</dt><dd><c:out value="${student.facultyName()}" default="—"/></dd>
        <dt>Lớp</dt><dd><c:out value="${student.classCode()}" default="—"/></dd>
        <dt>Email</dt><dd><a href="mailto:${fn:escapeXml(student.user().email())}"><c:out value="${student.user().email()}"/></a></dd>
        <dt>Điện thoại</dt><dd><c:out value="${student.phone()}" default="—"/></dd>
      </dl>
    </div></div>
    <div class="card"><div class="card-header"><h3 class="card-title">Yêu cầu khác của sinh viên</h3></div><div class="list-group list-group-flush">
      <c:choose><c:when test="${empty detail.otherTickets()}"><div class="list-group-item text-secondary">Không có yêu cầu khác trong phạm vi.</div></c:when>
      <c:otherwise><c:forEach var="other" items="${detail.otherTickets()}">
        <a class="list-group-item list-group-item-action" href="${ctx}/manager/tickets/${other.id()}"><span class="small text-secondary"><c:out value="${other.code()}"/></span><br/><c:out value="${other.title()}"/><br/><span class="small text-secondary"><c:out value="${other.status().label}"/></span></a>
      </c:forEach></c:otherwise></c:choose>
    </div></div>
  </aside>

  <main class="col-12 col-lg-6">
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Nội dung yêu cầu</h3></div><div class="card-body">
      <div class="qa-richtext"><c:out value="${ticket.descriptionHtml()}" escapeXml="false"/></div>
      <c:if test="${not empty detail.descriptionAttachments()}"><ul class="list-unstyled mt-3 mb-0"><c:forEach var="asset" items="${detail.descriptionAttachments()}"><li><a href="${ctx}${asset.downloadUrl()}"><i class="bi bi-paperclip" aria-hidden="true"></i> <c:out value="${asset.name()}"/></a> <span class="text-secondary small">(<c:out value="${asset.sizeBytes()}"/> byte)</span></li></c:forEach></ul></c:if>
      <c:if test="${ticket.urgentRequested()}"><div class="alert alert-warning mt-3 mb-0"><strong>Sinh viên đề nghị gấp.</strong> <c:out value="${ticket.urgentReason()}"/></div></c:if>
      <c:if test="${not empty ticket.resolutionNote()}"><div class="alert alert-success mt-3 mb-0"><strong>Ghi chú giải quyết:</strong> <c:out value="${ticket.resolutionNote()}"/></div></c:if>
    </div></div>
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Hội thoại</h3></div><div class="card-body">
      <c:choose><c:when test="${empty detail.messages()}"><p class="text-secondary mb-0">Chưa có tin nhắn.</p></c:when>
      <c:otherwise><div class="d-flex flex-column gap-3"><c:forEach var="message" items="${detail.messages()}">
        <c:choose><c:when test="${message.senderType() eq 'SYSTEM'}"><div class="text-center small text-secondary border rounded p-2"><c:out value="${message.contentHtml()}" escapeXml="false"/> <span class="d-block"><c:out value="${message.createdAt()}"/></span></div></c:when>
        <c:otherwise><div class="d-flex ${message.senderType() eq 'MANAGER' ? 'justify-content-end' : 'justify-content-start'}"><div class="border rounded p-3 ${message.senderType() eq 'MANAGER' ? 'bg-blue-lt' : 'bg-light'}" style="max-width:90%">
          <div class="small fw-semibold"><c:out value="${message.senderName()}"/> <span class="fw-normal text-secondary"><c:out value="${message.createdAt()}"/></span></div>
          <div class="qa-richtext mt-2"><c:out value="${message.contentHtml()}" escapeXml="false"/></div>
          <c:if test="${not empty message.attachments()}"><ul class="list-unstyled mt-2 mb-0"><c:forEach var="asset" items="${message.attachments()}"><li><a href="${ctx}${asset.downloadUrl()}"><i class="bi bi-paperclip" aria-hidden="true"></i> <c:out value="${asset.name()}"/></a> <span class="text-secondary small">(<c:out value="${asset.sizeBytes()}"/> byte)</span></li></c:forEach></ul></c:if>
        </div></div></c:otherwise></c:choose>
      </c:forEach></div></c:otherwise></c:choose>
    </div></div>
    <div id="workspace-actions"></div>
  </main>

  <aside class="col-12 col-lg-3">
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Thông tin yêu cầu</h3></div><div class="card-body">
      <dl class="mb-0"><dt>Mã</dt><dd><c:out value="${ticket.code()}"/></dd>
        <dt>Chuyên mục</dt><dd><c:out value="${ticket.categoryPath()}"/></dd>
        <dt>Ưu tiên</dt><dd><qa:priorityBadge priority="${ticket.priority()}"/></dd>
        <dt>Trạng thái</dt><dd><qa:statusBadge status="${ticket.status()}"/></dd>
        <dt>SLA</dt><dd><qa:slaBadge sla="${ticket.slaStatus()}"/></dd>
        <dt>Hạn phản hồi đầu tiên</dt><dd><c:out value="${ticket.slaFirstResponseDueAt()}" default="—"/></dd>
        <dt>Hạn giải quyết</dt><dd><c:out value="${ticket.slaResolutionDueAt()}" default="—"/></dd>
        <dt>Người phụ trách</dt><dd><c:out value="${ticket.assigneeName()}"/></dd>
        <dt>Tạo lúc</dt><dd><c:out value="${ticket.createdAt()}"/></dd>
      </dl>
    </div></div>
    <div class="card mb-3"><div class="card-header"><h3 class="card-title">Dòng thời gian</h3></div><div class="card-body">
      <c:choose><c:when test="${empty detail.timeline()}"><p class="text-secondary mb-0">Chưa có sự kiện.</p></c:when><c:otherwise><ol class="ps-3 mb-0"><c:forEach var="event" items="${detail.timeline()}"><li class="mb-2"><div><c:out value="${event.label()}"/></div><small class="text-secondary"><c:out value="${event.at()}"/></small></li></c:forEach></ol></c:otherwise></c:choose>
    </div></div>
    <div class="card"><div class="card-header"><h3 class="card-title">FAQ gợi ý</h3></div><div class="list-group list-group-flush">
      <c:choose><c:when test="${empty detail.suggestions()}"><div class="list-group-item text-secondary">Chưa có bài viết phù hợp.</div></c:when><c:otherwise><c:forEach var="faq" items="${detail.suggestions()}"><a class="list-group-item list-group-item-action" href="${ctx}/faq/${fn:escapeXml(faq.slug())}"><c:out value="${faq.title()}"/></a></c:forEach></c:otherwise></c:choose>
    </div></div>
  </aside>
</div>
