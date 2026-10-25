<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Thông báo</title>

<style>
    .notification-item {
        transition: background-color .15s ease;
    }
    .notification-item:hover {
        background-color: #f7faff;
    }
    .notification-item.bg-blue-lt:hover {
        background-color: rgba(32, 107, 196, 0.08) !important;
    }
</style>

<div class="page-header d-print-none mb-3">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
        <div>
            <h2 class="page-title mb-1">Thông báo</h2>
            <div class="text-secondary">Cập nhật về yêu cầu, lịch hẹn và hoạt động tài khoản của bạn.</div>
        </div>
        <div class="d-flex flex-wrap align-items-center gap-2">
            <c:if test="${unreadCount > 0}">
                <form method="post" action="${ctx}/student/notifications/read-all" class="m-0">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="hidden" name="unread" value="${unread}"/>
                    <button class="btn btn-outline-primary" type="submit">
                        <i class="bi bi-check2-all me-1"></i>Đánh dấu đã đọc hết
                    </button>
                </form>
            </c:if>
            <a class="btn btn-outline-secondary" href="${ctx}/student/notifications/preferences">
                <i class="bi bi-gear me-1"></i>Tùy chọn
            </a>
        </div>
    </div>
</div>

<ul class="nav nav-tabs mb-3" role="tablist">
    <li class="nav-item">
        <a class="nav-link ${not unread ? 'active' : ''}" href="${ctx}/student/notifications">
            <i class="bi bi-bell me-1"></i>Tất cả
        </a>
    </li>
    <li class="nav-item">
        <a class="nav-link ${unread ? 'active' : ''}" href="${ctx}/student/notifications?unread=true">
            <i class="bi bi-envelope me-1"></i>Chưa đọc
            <c:if test="${unreadCount > 0}">
                <span class="badge bg-primary ms-1"><c:out value="${unreadCount}"/></span>
            </c:if>
        </a>
    </li>
</ul>

<section class="card overflow-hidden">
    <c:choose>
        <c:when test="${not empty page.content}">
            <div class="list-group list-group-flush">
                <c:forEach var="notification" items="${page.content}">
                    <div class="list-group-item px-3 py-3 notification-item ${notification.read() ? '' : 'bg-blue-lt'}">
                        <div class="d-flex align-items-start gap-3">
                            <div class="avatar avatar-md ${notification.read() ? 'bg-secondary-lt text-secondary' : 'bg-primary-lt text-primary'} flex-shrink-0">
                                <c:choose>
                                    <c:when test="${fn:startsWith(notification.type(), 'TICKET')}"><i class="bi bi-ticket-detailed"></i></c:when>
                                    <c:when test="${fn:startsWith(notification.type(), 'SLA')}"><i class="bi bi-exclamation-triangle"></i></c:when>
                                    <c:when test="${fn:startsWith(notification.type(), 'APPOINTMENT')}"><i class="bi bi-calendar-event"></i></c:when>
                                    <c:when test="${fn:startsWith(notification.type(), 'CHAT')}"><i class="bi bi-chat-dots"></i></c:when>
                                    <c:when test="${fn:startsWith(notification.type(), 'FAQ') or notification.type() eq 'REVIEW_LOW_RATING'}"><i class="bi bi-question-circle"></i></c:when>
                                    <c:when test="${fn:startsWith(notification.type(), 'SECURITY')}"><i class="bi bi-shield-lock"></i></c:when>
                                    <c:otherwise><i class="bi bi-bell"></i></c:otherwise>
                                </c:choose>
                            </div>
                            <form method="post" action="${ctx}/student/notifications/${notification.id()}/open" class="flex-fill min-w-0 m-0">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="btn btn-link text-start text-reset text-decoration-none w-100 p-0 shadow-none border-0" type="submit">
                                    <span class="d-block ${notification.read() ? '' : 'fw-bold'}">
                                        <c:if test="${not notification.read()}">
                                            <span class="badge bg-primary rounded-pill me-1">Mới</span>
                                        </c:if>
                                        <c:out value="${notification.title()}"/>
                                    </span>
                                    <c:if test="${not empty notification.body()}">
                                        <span class="d-block text-secondary small mt-1 text-break"><c:out value="${notification.body()}"/></span>
                                    </c:if>
                                    <span class="d-block text-secondary small mt-1" data-relative-time="${notification.createdAt()}"><c:out value="${notification.createdAt()}"/></span>
                                </button>
                            </form>
                            <form method="post" action="${ctx}/student/notifications/${notification.id()}/hide" class="m-0 flex-shrink-0">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="hidden" name="unread" value="${unread}"/>
                                <button class="btn btn-sm btn-ghost-secondary rounded-circle" type="submit" title="Ẩn thông báo" aria-label="Ẩn thông báo">
                                    <i class="bi bi-x-lg"></i>
                                </button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="card-footer d-flex justify-content-center">
                <qa:pagination page="${page}" baseUrl="${ctx}/student/notifications" params="${unread ? 'unread=true' : ''}"/>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card-body py-5 text-center">
                <qa:emptyState icon="bi-bell-slash" title="Không có thông báo"
                               message="${unread ? 'Bạn đã đọc tất cả thông báo.' : 'Các cập nhật mới sẽ xuất hiện tại đây.'}"
                               actionUrl="${unread ? ctx.concat('/student/notifications') : ctx.concat('/student/dashboard')}"
                               actionLabel="${unread ? 'Xem tất cả thông báo' : 'Về trang tổng quan'}"/>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<script>
    (function () {
        function relativeTime(value) {
            var date = new Date(value);
            if (Number.isNaN(date.getTime())) return value;
            var seconds = Math.max(0, Math.floor((Date.now() - date.getTime()) / 1000));
            if (seconds < 60) return 'Vừa xong';
            if (seconds < 3600) return Math.floor(seconds / 60) + ' phút trước';
            if (seconds < 86400) return Math.floor(seconds / 3600) + ' giờ trước';
            if (seconds < 604800) return Math.floor(seconds / 86400) + ' ngày trước';
            return date.toLocaleDateString('vi-VN', {day: '2-digit', month: '2-digit', year: 'numeric'});
        }
        document.querySelectorAll('[data-relative-time]').forEach(function (element) {
            element.textContent = relativeTime(element.dataset.relativeTime);
        });
    }());
</script>

</script>