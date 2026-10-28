<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="visibleCount" value="${fn:length(page.content)}"/>

<head>
    <title>Thông báo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/student/notifications.css">
</head>
<div class="qa-page-student-notifications" data-qa-view="student-notifications">


<section class="notification-hero page-header d-print-none">
    <div class="notification-hero-copy">
        <span class="notification-eyebrow"><i class="bi bi-stars" aria-hidden="true"></i>Trung tâm cập nhật cá nhân</span>
        <div class="notification-title-wrap">
            <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-bell-fill"></i></span>
            <div class="min-width-0">
                <h2 class="qa-view-title" data-qa-page="student-notifications">Thông báo của bạn</h2>
                <p class="notification-description">
                    Theo dõi cập nhật về yêu cầu hỗ trợ, lịch hẹn, trao đổi và tài khoản của bạn.
                </p>
            </div>
        </div>
        <div class="notification-page-actions">
            <c:if test="${unreadCount > 0}">
                <form method="post" action="${ctx}${notificationBaseUrl}/read-all" class="m-0">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="hidden" name="unread" value="${unread}"/>
                    <button class="btn btn-outline-primary" type="submit">
                        <i class="bi bi-check2-all me-1"></i>Đánh dấu đã đọc hết
                    </button>
                </form>
            </c:if>
            <c:if test="${not empty notificationPreferencesUrl}">
                <a class="btn btn-outline-secondary" href="${ctx}${notificationPreferencesUrl}">
                    <i class="bi bi-sliders2 me-1"></i>Tùy chỉnh thông báo
                </a>
            </c:if>
        </div>
    </div>
</section>

<div class="notification-toolbar">
    <ul class="nav nav-tabs notification-filter-bar" role="tablist" aria-label="Lọc thông báo">
        <li class="nav-item">
            <a class="nav-link ${not unread ? 'active' : ''}" href="${ctx}${notificationBaseUrl}">
                <i class="bi bi-collection me-1"></i>Tất cả
                <span class="badge bg-primary"><c:out value="${visibleCount}"/></span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${unread ? 'active' : ''}" href="${ctx}${notificationBaseUrl}?unread=true">
                <i class="bi bi-envelope-paper-heart me-1"></i>Chưa đọc
                <span class="badge bg-primary"><c:out value="${unreadCount}"/></span>
            </a>
        </li>
    </ul>
</div>

<section class="notification-board card">
    <div class="notification-board-header">
        <h3 class="notification-board-title">Dòng cập nhật cá nhân</h3>
        <div class="notification-board-badge">
            <i class="bi bi-broadcast-pin" aria-hidden="true"></i>
            ${unread ? 'Đang lọc mục chưa đọc' : 'Hiển thị toàn bộ thông báo'}
        </div>
    </div>
    <c:choose>
        <c:when test="${not empty page.content}">
            <div class="list-group list-group-flush notification-list">
                <c:forEach var="notification" items="${page.content}" varStatus="loop">
                    <div class="list-group-item notification-list-item ${notification.read() ? 'is-read' : 'is-unread'}">
                        <div class="notification-icon-shell">
                            <div class="avatar avatar-md ${notification.read() ? 'text-secondary' : 'text-primary'}">
                                <c:choose>
                                    <c:when test="${fn:startsWith(notification.type(), 'TICKET')}"><i class="bi bi-ticket-detailed-fill"></i></c:when>
                                    <c:when test="${fn:startsWith(notification.type(), 'SLA')}"><i class="bi bi-alarm-fill"></i></c:when>
                                    <c:when test="${fn:startsWith(notification.type(), 'APPOINTMENT')}"><i class="bi bi-calendar2-check-fill"></i></c:when>
                                    <c:when test="${fn:startsWith(notification.type(), 'CHAT')}"><i class="bi bi-chat-square-dots-fill"></i></c:when>
                                    <c:when test="${fn:startsWith(notification.type(), 'FAQ') or notification.type() eq 'REVIEW_LOW_RATING'}"><i class="bi bi-patch-question-fill"></i></c:when>
                                    <c:when test="${fn:startsWith(notification.type(), 'SECURITY')}"><i class="bi bi-shield-lock-fill"></i></c:when>
                                    <c:otherwise><i class="bi bi-bell-fill"></i></c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="notification-item-main">
                            <div class="notification-meta">
                                <c:if test="${not notification.read()}">
                                    <span class="notification-chip notification-chip--new"><i class="bi bi-sparkle"></i>Mới</span>
                                </c:if>
                                <span class="notification-chip notification-chip--type">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(notification.type(), 'TICKET')}">Yêu cầu hỗ trợ</c:when>
                                        <c:when test="${fn:startsWith(notification.type(), 'SLA')}">SLA</c:when>
                                        <c:when test="${fn:startsWith(notification.type(), 'APPOINTMENT')}">Lịch hẹn</c:when>
                                        <c:when test="${fn:startsWith(notification.type(), 'CHAT')}">Trò chuyện</c:when>
                                        <c:when test="${fn:startsWith(notification.type(), 'FAQ') or notification.type() eq 'REVIEW_LOW_RATING'}">Kiến thức</c:when>
                                        <c:when test="${fn:startsWith(notification.type(), 'SECURITY')}">Bảo mật</c:when>
                                        <c:otherwise>Hệ thống</c:otherwise>
                                    </c:choose>
                                </span>
                                <span class="notification-chip notification-chip--state ${notification.read() ? '' : 'is-unread'}">
                                    ${notification.read() ? 'Đã đọc' : 'Chưa đọc'}
                                </span>
                            </div>
                            <form method="post" action="${ctx}${notificationBaseUrl}/${notification.id()}/open" class="notification-open-form m-0">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="btn btn-link notification-open-button text-reset text-decoration-none shadow-none border-0" type="submit">
                                    <span class="notification-title-row">
                                        <span class="notification-title ${notification.read() ? '' : 'fw-bold'}"><c:out value="${notification.title()}"/></span>
                                        <span class="notification-open-hint">Mở cập nhật <i class="bi bi-arrow-up-right"></i></span>
                                    </span>
                                    <c:if test="${not empty notification.body()}">
                                        <span class="notification-body"><c:out value="${notification.body()}"/></span>
                                    </c:if>
                                    <span class="notification-footer-row">
                                        <span class="notification-time" data-relative-time="${notification.createdAt()}"><c:out value="${notification.createdAt()}"/></span>

                                    </span>
                                </button>
                            </form>
                        </div>
                        <div class="notification-side-actions">
                            <form method="post" action="${ctx}${notificationBaseUrl}/${notification.id()}/hide" class="notification-hide-form">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="hidden" name="unread" value="${unread}"/>
                                <button class="btn btn-sm" type="submit" title="Ẩn thông báo" aria-label="Ẩn thông báo">
                                    <i class="bi bi-x-lg"></i>
                                </button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="notification-card-footer card-footer d-flex justify-content-center">
                <qa:pagination page="${page}" baseUrl="${ctx}${notificationBaseUrl}" params="${unread ? 'unread=true' : ''}"/>
            </div>
        </c:when>
        <c:otherwise>
            <div class="notification-empty card-body text-center">
                <qa:emptyState icon="bi-bell-slash" title="Không có thông báo"
                               message="${unread ? 'Bạn đã đọc tất cả thông báo.' : 'Các cập nhật mới dành cho bạn sẽ xuất hiện tại đây.'}"
                               actionUrl="${unread ? ctx.concat(notificationBaseUrl) : ctx.concat(notificationDashboardUrl)}"
                               actionLabel="${unread ? 'Xem tất cả thông báo' : 'Về trang tổng quan'}"/>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<script src="${pageContext.request.contextPath}/assets/js/pages/student/notifications.js?v=20261029a"></script>
</div>
