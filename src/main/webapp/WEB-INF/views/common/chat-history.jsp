<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title><c:out value="${pageTitle}"/></title>
<div id="chat-history" data-api-url="${ctx}<c:out value='${historyApiUrl}'/>" data-context="${ctx}">
    <div class="qa-page-heading">
        <div>
            <span class="qa-page-eyebrow">Tư vấn trực tuyến</span>
            <h1 class="page-title mb-0"><c:out value="${pageTitle}"/></h1>
            <p>Xem lại nội dung các phiên trò chuyện đã diễn ra.</p>
        </div>
    </div>
    <div class="row g-3">
        <div class="col-12 col-lg-5">
            <div class="card">
                <div class="card-header"><h2 class="card-title mb-0">Danh sách hội thoại</h2></div>
                <div id="chat-history-list" class="list-group list-group-flush" aria-live="polite"></div>
                <div class="card-footer d-flex justify-content-between align-items-center">
                    <button id="chat-history-prev" class="btn btn-sm btn-outline-secondary" type="button">Trước</button>
                    <span id="chat-history-page" class="small text-secondary">Trang 1</span>
                    <button id="chat-history-next" class="btn btn-sm btn-outline-secondary" type="button">Sau</button>
                </div>
            </div>
        </div>
        <div class="col-12 col-lg-7">
            <div class="card">
                <div class="card-header">
                    <div>
                        <h2 id="chat-history-title" class="card-title mb-0">Chọn hội thoại</h2>
                        <div id="chat-history-subtitle" class="small text-secondary"></div>
                    </div>
                </div>
                <div id="chat-history-messages" class="card-body overflow-auto u-height-65vh"
                     aria-live="polite">
                    <div class="text-secondary">Chọn một phiên để xem nội dung.</div>
                </div>
            </div>
        </div>
    </div>
    <div id="chat-history-error" class="alert alert-danger mt-3 d-none" role="alert"></div>
</div>
<script src="${ctx}/assets/js/common/chat-history.js" defer></script>
