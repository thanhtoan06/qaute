<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Chi tiết yêu cầu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/ticket-detail.css">
</head>
<div class="qa-page-student-ticket-detail" data-qa-view="student-ticket-detail">

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-bs5.min.css">

<div data-ticket-id="${detail.id()}" class="">
    <div class="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3 qa-page-top-bar">
        <div>
            <a class="text-secondary text-decoration-none small" href="${ctx}/student/tickets"><i class="bi bi-arrow-left"></i> Yêu cầu của tôi</a>
            <h2 class="page-title mt-1 mb-0 qa-view-title" data-qa-page="student-ticket-detail"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-ticket-detailed"></i></span><c:out value="${detail.title()}"/></h2>
            <div class="text-secondary small mt-1"><c:out value="${detail.code()}"/> · <time datetime="${detail.createdAt()}" data-qaute-datetime><c:out value="${detail.createdAt()}"/></time></div>
        </div>
        <div class="d-flex gap-2 align-items-center">
            <qa:statusBadge status="${detail.status()}"/>
            <qa:priorityBadge priority="${detail.priority()}"/>
        </div>
    </div>

    <div class="row g-3">
        <%-- Hiển thị yêu cầu, trao đổi, thao tác trạng thái và đánh giá kết quả. --%>
        <main class="col-12 col-lg-8">
            <%-- Trao đổi với tư vấn viên và gửi phản hồi kèm tệp nếu được phép. --%>
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
                                                <time class="d-block mt-1" datetime="${message.createdAt()}" data-qaute-datetime><c:out value="${message.createdAt()}"/></time>
                                            </div></div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="d-flex ${message.senderType() eq 'STUDENT' ? 'justify-content-end' : 'justify-content-start'}">
                                                <div class="border rounded p-3 ${message.senderType() eq 'STUDENT' ? 'bg-blue-lt' : 'bg-light'} u-max-w-90">
                                                    <div class="small fw-semibold"><c:out value="${message.senderName()}"/> <time class="fw-normal text-secondary" datetime="${message.createdAt()}" data-qaute-datetime><c:out value="${message.createdAt()}"/></time></div>
                                                    <div class="qa-richtext mt-2"><c:out value="${message.contentHtml()}" escapeXml="false"/></div>
                                                    <c:if test="${not empty message.attachments()}">
                                                        <ul class="list-unstyled mb-0 mt-2">
                                                            <c:forEach var="asset" items="${message.attachments()}">
                                                                <li class="small mb-2">
                                                                    <c:choose>
                                                                        <c:when test="${fn:startsWith(asset.mimeType(), 'image/')}">
                                                                            <a href="${ctx}${asset.downloadUrl()}" target="_blank" rel="noopener noreferrer">
                                                                                <img src="${ctx}${asset.downloadUrl()}" alt="<c:out value='${asset.name()}'/>" class="img-fluid rounded border mt-2 u-max-w-100 u-max-h-320px u-object-fit-contain"/>
                                                                            </a>
                                                                            <div><a href="${ctx}${asset.downloadUrl()}" target="_blank" rel="noopener noreferrer"><i class="bi bi-download"></i> <c:out value="${asset.name()}"/></a></div>
                                                                        </c:when>
                                                                        <c:otherwise><a href="${ctx}${asset.downloadUrl()}"><i class="bi bi-paperclip"></i> <c:out value="${asset.name()}"/></a></c:otherwise>
                                                                    </c:choose>
                                                                    <span class="text-secondary">(<c:out value="${asset.sizeBytes()}"/> byte)</span>
                                                                </li>
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
                        <div class="mb-3"><label class="form-label">Tệp đính kèm</label><div id="ticket-reply-uploader" data-upload-url="${ctx}/api/media/upload"></div></div>
                        <button type="submit" class="btn btn-primary"><i class="bi bi-send"></i> Gửi phản hồi</button>
                    </form>
                </section>
            </c:if>

            <c:if test="${detail.canCancel() or detail.canConfirm() or detail.canReopen()}">
                <%-- Cung cấp thao tác hủy hoặc mở lại yêu cầu theo trạng thái. --%>
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

            <c:if test="${detail.canRate() or not empty detail.review()}">
                <%-- Gửi đánh giá về chất lượng phản hồi sau khi yêu cầu được xử lý. --%>
                <section class="card" id="ticket-review">
                    <div class="card-header"><h3 class="card-title">Đánh giá</h3></div>
                    <div class="card-body">
                        <c:if test="${not empty detail.review()}">
                            <div class="d-flex flex-wrap align-items-center gap-2 mb-3">
                                <strong>${detail.review().rating()}/5 sao</strong>
                                <span class="text-warning" aria-label="${detail.review().rating()} sao">
                                    <c:forEach begin="1" end="5" var="star"><i class="bi ${star le detail.review().rating() ? 'bi-star-fill' : 'bi-star'}"></i></c:forEach>
                                </span>
                                <c:if test="${detail.review().editable()}">
                                    <button class="btn btn-sm btn-outline-primary ms-auto" type="button" data-review-edit>Sửa đánh giá</button>
                                </c:if>
                            </div>
                            <c:if test="${not empty detail.review().reasonCodes()}">
                                <p class="small mb-2">Lý do: <c:forEach var="code" items="${detail.review().reasonCodes()}" varStatus="loop"><c:out value="${code}"/><c:if test="${not loop.last}">, </c:if></c:forEach></p>
                            </c:if>
                            <c:if test="${not empty detail.review().comment()}"><p class="mb-3"><c:out value="${detail.review().comment()}"/></p></c:if>
                        </c:if>

                        <c:if test="${detail.canRate()}">
                            <form id="ticket-review-form" method="post" action="${ctx}/student/tickets/${detail.id()}/review" class="${not empty detail.review() ? 'd-none' : ''}" novalidate>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <div class="mb-3">
                                    <label class="form-label required d-block">Mức độ hài lòng</label>
                                    <div class="btn-group" role="group" aria-label="Chọn số sao">
                                        <c:forEach begin="1" end="5" var="star">
                                            <input class="btn-check" type="radio" name="rating" id="ticket-rating-${star}" value="${star}" ${not empty detail.review() and detail.review().rating() eq star ? 'checked' : ''} required/>
                                            <label class="btn btn-outline-warning review-star" for="ticket-rating-${star}" data-rating="${star}" aria-label="${star} sao"><i class="bi bi-star-fill"></i></label>
                                        </c:forEach>
                                    </div>
                                    <div class="form-hint">Chọn từ 1 đến 5 sao.</div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label d-block">Lý do đánh giá <span class="text-secondary">(tùy chọn)</span></label>
                                    <div class="d-flex flex-wrap gap-2">
                                        <label class="form-check"><input class="form-check-input" type="checkbox" name="reasonCodes" value="FAST" ${not empty detail.review() and detail.review().reasonCodes().contains('FAST') ? 'checked' : ''}/><span class="form-check-label">Nhanh chóng</span></label>
                                        <label class="form-check"><input class="form-check-input" type="checkbox" name="reasonCodes" value="CLEAR" ${not empty detail.review() and detail.review().reasonCodes().contains('CLEAR') ? 'checked' : ''}/><span class="form-check-label">Rõ ràng</span></label>
                                        <label class="form-check"><input class="form-check-input" type="checkbox" name="reasonCodes" value="FRIENDLY" ${not empty detail.review() and detail.review().reasonCodes().contains('FRIENDLY') ? 'checked' : ''}/><span class="form-check-label">Thân thiện</span></label>
                                        <label class="form-check"><input class="form-check-input" type="checkbox" name="reasonCodes" value="NOT_ON_POINT" ${not empty detail.review() and detail.review().reasonCodes().contains('NOT_ON_POINT') ? 'checked' : ''}/><span class="form-check-label">Chưa đúng trọng tâm</span></label>
                                        <label class="form-check"><input class="form-check-input" type="checkbox" name="reasonCodes" value="SLOW" ${not empty detail.review() and detail.review().reasonCodes().contains('SLOW') ? 'checked' : ''}/><span class="form-check-label">Chậm phản hồi</span></label>
                                        <label class="form-check"><input class="form-check-input" type="checkbox" name="reasonCodes" value="RUDE" ${not empty detail.review() and detail.review().reasonCodes().contains('RUDE') ? 'checked' : ''}/><span class="form-check-label">Chưa lịch sự</span></label>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label id="review-comment-label" class="form-label" for="ticket-review-comment">Nhận xét</label>
                                    <textarea id="ticket-review-comment" class="form-control" name="comment" maxlength="500" rows="4"><c:out value="${not empty detail.review() ? detail.review().comment() : ''}"/></textarea>
                                    <div id="review-comment-hint" class="form-hint">Tối đa 500 ký tự.</div>
                                    <div class="invalid-feedback">Đánh giá từ 1 đến 2 sao cần nhận xét ít nhất 20 ký tự.</div>
                                </div>
                                <button class="btn btn-primary" type="submit">${not empty detail.review() ? 'Cập nhật đánh giá' : 'Gửi đánh giá'}</button>
                            </form>
                        </c:if>
                    </div>
                </section>
            </c:if>
        </main>

        <aside class="col-12 col-lg-4">
            <%-- Hiển thị trạng thái, thông tin yêu cầu và các sự kiện theo thời gian. --%>
            <section class="card mb-3"><div class="card-header"><h3 class="card-title">Thông tin yêu cầu</h3></div><div class="card-body"><dl class="mb-0">
                <dt>Chuyên mục</dt><dd><c:out value="${detail.categoryPath()}"/></dd>
                <dt>Người phụ trách</dt><dd><c:choose><c:when test="${not empty detail.assigneeName()}"><c:if test="${not empty detail.assigneeAvatarUrl()}"><img class="avatar avatar-xs me-1" src="<c:out value="${detail.assigneeAvatarUrl()}"/>" alt=""/></c:if><c:out value="${detail.assigneeName()}"/></c:when><c:otherwise><span class="text-secondary">Chưa phân công</span></c:otherwise></c:choose></dd>
                <dt>Cho phép tạo FAQ công khai</dt><dd><c:out value="${detail.allowPublicFaq() ? 'Có' : 'Không'}"/></dd>
            </dl></div></section>
            <section class="card"><div class="card-header"><h3 class="card-title">Dòng thời gian</h3></div><div class="card-body">
                <c:choose><c:when test="${empty detail.timeline()}"><p class="text-secondary mb-0">Chưa có sự kiện.</p></c:when><c:otherwise><ol class="ps-3 mb-0"><c:forEach var="event" items="${detail.timeline()}"><li class="mb-3"><div><c:out value="${event.label()}"/></div><time class="small text-secondary" datetime="${event.at()}" data-qaute-datetime><c:out value="${event.at()}"/></time></li></c:forEach></ol></c:otherwise></c:choose>
            </div></section>
        </aside>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js"></script>
<script src="${ctx}/assets/js/common/richtext.js"></script>
<script src="${ctx}/assets/js/common/file-uploader.js?v=20261026c"></script>
<script src="${ctx}/assets/js/student/ticket-detail.js?v=20261026b"></script>
<script src="${ctx}/assets/js/student/review.js"></script>
</div>
