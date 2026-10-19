<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Chi tiết yêu cầu</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.css">

<div data-ticket-id="${detail.id()}">
    <div class="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3">
        <div>
            <a class="text-secondary text-decoration-none small" href="${ctx}/student/tickets"><i class="bi bi-arrow-left"></i> Yêu cầu của tôi</a>
            <h2 class="page-title mt-1 mb-0"><c:out value="${detail.title()}"/></h2>
            <div class="text-secondary small mt-1"><c:out value="${detail.code()}"/> · <c:out value="${detail.createdAt()}"/></div>
        </div>
        <div class="d-flex gap-2 align-items-center">
            <qa:statusBadge status="${detail.status()}"/>
            <qa:priorityBadge priority="${detail.priority()}"/>
        </div>
    </div>

    <div class="row g-3">
        <main class="col-12 col-lg-8">
            <section class="card mb-3">
                <div class="card-header"><h3 class="card-title">Nội dung yêu cầu</h3></div>
                <div class="card-body"><div class="qa-richtext"><c:out value="${detail.descriptionHtml()}" escapeXml="false"/></div></div>
            </section>

            <section class="card mb-3">
                <div class="card-header"><h3 class="card-title">Trao đổi</h3></div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty detail.messages()}"><p class="text-secondary mb-0">Chưa có trao đổi.</p></c:when>
                        <c:otherwise>
                            <div class="d-flex flex-column gap-3">
                                <c:forEach var="message" items="${detail.messages()}">
                                    <c:choose>
                                        <c:when test="${message.senderType() eq 'SYSTEM'}">
                                            <div class="text-center"><div class="d-inline-block border rounded px-3 py-2 text-secondary small bg-light">
                                                <c:out value="${message.contentHtml()}" escapeXml="false"/>
                                                <span class="d-block mt-1"><c:out value="${message.createdAt()}"/></span>
                                            </div></div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="d-flex ${message.senderType() eq 'STUDENT' ? 'justify-content-end' : 'justify-content-start'}">
                                                <div class="border rounded p-3 ${message.senderType() eq 'STUDENT' ? 'bg-blue-lt' : 'bg-light'}" style="max-width: 90%;">
                                                    <div class="small fw-semibold"><c:out value="${message.senderName()}"/> <span class="fw-normal text-secondary"><c:out value="${message.createdAt()}"/></span></div>
                                                    <div class="qa-richtext mt-2"><c:out value="${message.contentHtml()}" escapeXml="false"/></div>
                                                    <c:if test="${not empty message.attachments()}">
                                                        <ul class="list-unstyled mb-0 mt-2">
                                                            <c:forEach var="asset" items="${message.attachments()}">
                                                                <li class="small"><a href="${ctx}${asset.downloadUrl()}"><i class="bi bi-paperclip"></i> <c:out value="${asset.name()}"/></a> <span class="text-secondary">(<c:out value="${asset.sizeBytes()}"/> byte)</span></li>
                                                            </c:forEach>
                                                        </ul>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>

            <c:if test="${detail.canReply()}">
                <section class="card mb-3">
                    <div class="card-header"><h3 class="card-title">Phản hồi</h3></div>
                    <form method="post" action="${ctx}/student/tickets/${detail.id()}/reply" class="card-body" novalidate>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="mb-3"><label class="form-label required" for="ticket-reply-content">Nội dung</label><textarea id="ticket-reply-content" class="form-control" name="contentHtml" rows="6" required></textarea></div>
                        <div class="mb-3"><label class="form-label">Tệp đính kèm</label><div id="ticket-reply-uploader"></div></div>
                        <button type="submit" class="btn btn-primary"><i class="bi bi-send"></i> Gửi phản hồi</button>
                    </form>
                </section>
            </c:if>

            <c:if test="${detail.canCancel() or detail.canConfirm() or detail.canReopen()}">
                <section class="card mb-3"><div class="card-header"><h3 class="card-title">Thao tác yêu cầu</h3></div><div class="card-body d-flex flex-wrap gap-2">
                    <c:if test="${detail.canCancel()}">
                        <button class="btn btn-outline-danger" type="button" data-bs-toggle="modal" data-bs-target="#cancel-ticket-modal">Hủy yêu cầu</button>
                    </c:if>
                    <c:if test="${detail.canConfirm()}">
                        <qa:confirmModal id="confirm-ticket-modal" title="Xác nhận đã giải quyết" message="Bạn xác nhận yêu cầu này đã được giải quyết?" actionUrl="${ctx}/student/tickets/${detail.id()}/confirm" confirmLabel="Xác nhận đã giải quyết" cssClass="btn-outline-success"/>
                    </c:if>
                    <c:if test="${detail.canReopen()}">
                        <button class="btn btn-outline-primary" type="button" data-bs-toggle="modal" data-bs-target="#reopen-ticket-modal">Chưa giải quyết — Mở lại</button>
                    </c:if>
                </div></section>
            </c:if>

            <c:if test="${detail.canCancel()}">
                <div class="modal modal-blur fade" id="cancel-ticket-modal" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-dialog-centered"><div class="modal-content">
                    <div class="modal-header"><h3 class="modal-title">Hủy yêu cầu</h3><button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Đóng"></button></div>
                    <form method="post" action="${ctx}/student/tickets/${detail.id()}/cancel"><div class="modal-body"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <p>Bạn có chắc muốn hủy yêu cầu này? Thao tác chỉ áp dụng khi yêu cầu chưa được nhận.</p>
                        <label class="form-label" for="cancel-reason">Lý do <span class="text-secondary">(tùy chọn)</span></label><textarea class="form-control" id="cancel-reason" name="reason" maxlength="500" rows="3"></textarea>
                    </div><div class="modal-footer"><button class="btn btn-link" type="button" data-bs-dismiss="modal">Đóng</button><button class="btn btn-danger" type="submit">Hủy yêu cầu</button></div></form>
                </div></div></div>
            </c:if>

            <c:if test="${detail.canReopen()}">
                <div class="modal modal-blur fade" id="reopen-ticket-modal" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-dialog-centered"><div class="modal-content">
                    <div class="modal-header"><h3 class="modal-title">Mở lại yêu cầu</h3><button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Đóng"></button></div>
                    <form method="post" action="${ctx}/student/tickets/${detail.id()}/reopen"><div class="modal-body"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <p class="mb-2">Nếu vấn đề chưa được giải quyết, hãy cho tư vấn viên biết thêm chi tiết. Bạn còn <strong><c:out value="${reopenRemaining}"/></strong> lần mở lại.</p>
                        <label class="form-label required" for="reopen-reason">Lý do mở lại</label><textarea class="form-control" id="reopen-reason" name="reason" minlength="10" maxlength="500" rows="4" required></textarea><div class="form-hint">Từ 10 đến 500 ký tự.</div>
                    </div><div class="modal-footer"><button class="btn btn-link" type="button" data-bs-dismiss="modal">Đóng</button><button class="btn btn-primary" type="submit">Mở lại yêu cầu</button></div></form>
                </div></div></div>
            </c:if>

            <c:if test="${detail.canRate()}">
                <section class="card"><div class="card-header"><h3 class="card-title">Đánh giá</h3></div><div class="card-body text-secondary">Khối đánh giá sẽ được bổ sung ở bước tiếp theo.</div></section>
            </c:if>
        </main>

        <aside class="col-12 col-lg-4">
            <section class="card mb-3"><div class="card-header"><h3 class="card-title">Thông tin yêu cầu</h3></div><div class="card-body"><dl class="mb-0">
                <dt>Chuyên mục</dt><dd><c:out value="${detail.categoryPath()}"/></dd>
                <dt>Người phụ trách</dt><dd><c:choose><c:when test="${not empty detail.assigneeName()}"><c:if test="${not empty detail.assigneeAvatarUrl()}"><span class="avatar avatar-xs me-1" style="background-image:url('<c:out value="${detail.assigneeAvatarUrl()}"/>')"></span></c:if><c:out value="${detail.assigneeName()}"/></c:when><c:otherwise><span class="text-secondary">Chưa phân công</span></c:otherwise></c:choose></dd>
                <dt>Cho phép tạo FAQ công khai</dt><dd><c:out value="${detail.allowPublicFaq() ? 'Có' : 'Không'}"/></dd>
            </dl></div></section>
            <section class="card"><div class="card-header"><h3 class="card-title">Dòng thời gian</h3></div><div class="card-body">
                <c:choose><c:when test="${empty detail.timeline()}"><p class="text-secondary mb-0">Chưa có sự kiện.</p></c:when><c:otherwise><ol class="ps-3 mb-0"><c:forEach var="event" items="${detail.timeline()}"><li class="mb-3"><div><c:out value="${event.label()}"/></div><small class="text-secondary"><c:out value="${event.at()}"/></small></li></c:forEach></ol></c:otherwise></c:choose>
            </div></section>
        </aside>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js"></script>
<script src="${ctx}/assets/js/common/richtext.js"></script>
<script src="${ctx}/assets/js/common/file-uploader.js"></script>
<script src="${ctx}/assets/js/student/ticket-detail.js"></script>
