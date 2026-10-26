<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="visibleCount" value="${fn:length(page.content)}"/>
<title>Thông báo</title>
<div class="qa-page-student-notifications" data-qa-view="student-notifications">
<style>
.qa-page-student-notifications {
    --notification-accent: #5f74ff;
    --notification-accent-strong: #4359da;
    --notification-accent-soft: #f1f3ff;
    --notification-accent-soft-2: #fbf7ff;
    --notification-highlight: #d951a8;
    --notification-success: #16a56f;
    --notification-ink: #213451;
    --notification-text: #60718b;
    --notification-muted: #8b99ae;
    --notification-border: rgba(118, 131, 192, .18);
    position: relative;
    min-width: 0;
    color: var(--notification-ink);
}
.qa-page-student-notifications::before,
.qa-page-student-notifications::after {
    position: fixed;
    z-index: 0;
    pointer-events: none;
    content: "";
}
.qa-page-student-notifications::before {
    top: 110px;
    right: -100px;
    width: 300px;
    height: 300px;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(95, 116, 255, .16), transparent 68%);
    filter: blur(8px);
}
.qa-page-student-notifications::after {
    left: -120px;
    bottom: 22px;
    width: 260px;
    height: 260px;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(217, 81, 168, .12), transparent 70%);
    filter: blur(10px);
}
.qa-page-student-notifications > * {
    position: relative;
    z-index: 1;
}
.qa-page-student-notifications .notification-hero {
    position: relative;
    display: grid;
    grid-template-columns: minmax(0, 1fr);
    margin-bottom: 1.35rem;
    padding: clamp(1.2rem, 2vw, 1.8rem);
    border: 1px solid rgba(118, 131, 192, .16);
    border-radius: 28px;
    background:
        radial-gradient(circle at top right, rgba(217, 81, 168, .15), transparent 31%),
        radial-gradient(circle at left center, rgba(95, 116, 255, .15), transparent 26%),
        linear-gradient(145deg, rgba(255, 255, 255, .98), rgba(248, 246, 255, .97));
    box-shadow: 0 24px 60px rgba(42, 53, 103, .1);
    overflow: hidden;
}
.qa-page-student-notifications .notification-hero::before {
    position: absolute;
    inset: auto auto -110px -90px;
    width: 240px;
    height: 240px;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(95, 116, 255, .16), transparent 70%);
    content: "";
}
.qa-page-student-notifications .notification-hero::after {
    position: absolute;
    top: -70px;
    right: -30px;
    width: 200px;
    height: 200px;
    border: 1px solid rgba(95, 116, 255, .16);
    border-radius: 50%;
    box-shadow: 0 0 0 24px rgba(95, 116, 255, .05), 0 0 0 54px rgba(95, 116, 255, .04);
    content: "";
}
.qa-page-student-notifications .notification-hero-copy {
    position: relative;
    z-index: 1;
}
.qa-page-student-notifications .notification-hero-copy {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    min-width: 0;
}
.qa-page-student-notifications .notification-eyebrow {
    display: inline-flex;
    align-items: center;
    gap: .55rem;
    width: fit-content;
    max-width: 100%;
    padding: .5rem .9rem;
    border: 1px solid rgba(95, 116, 255, .16);
    border-radius: 999px;
    background: rgba(255, 255, 255, .7);
    color: #5c61df;
    font-size: .73rem;
    font-weight: 800;
    letter-spacing: .12em;
    text-transform: uppercase;
    box-shadow: 0 10px 24px rgba(42, 53, 103, .08);
}
.qa-page-student-notifications .notification-title-wrap {
    display: flex;
    align-items: flex-start;
    gap: 1rem;
}
.qa-page-student-notifications .qa-view-title-icon {
    display: inline-flex;
    flex: 0 0 56px;
    align-items: center;
    justify-content: center;
    width: 56px;
    height: 56px;
    border-radius: 20px;
    background: linear-gradient(135deg, rgba(95, 116, 255, .2), rgba(217, 81, 168, .14));
    color: var(--notification-accent-strong);
    box-shadow: inset 0 1px 0 rgba(255, 255, 255, .85), 0 16px 30px rgba(95, 116, 255, .14);
    font-size: 1.5rem;
}
.qa-page-student-notifications .qa-view-title {
    display: block;
    margin: 0;
    color: #213451;
    font-size: clamp(1.65rem, 2vw, 2.35rem);
    font-weight: 900;
    line-height: 1.12;
    letter-spacing: -.05em;
}
.qa-page-student-notifications .notification-description {
    max-width: 760px;
    margin: 0;
    color: var(--notification-text);
    font-size: .98rem;
    line-height: 1.8;
}
.qa-page-student-notifications .notification-page-actions {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: .75rem;
}
.qa-page-student-notifications .notification-page-actions .btn {
    min-height: 44px;
    padding: .72rem 1.1rem;
    border-radius: 14px;
    font-weight: 700;
    box-shadow: 0 12px 28px rgba(42, 53, 103, .08);
    transition: transform .24s ease, box-shadow .24s ease, border-color .24s ease, background .24s ease;
}
.qa-page-student-notifications .notification-page-actions .btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 18px 34px rgba(42, 53, 103, .12);
}
.qa-page-student-notifications .notification-page-actions .btn-outline-primary {
    color: #fff;
    border: 0;
    background: linear-gradient(135deg, var(--notification-accent), var(--notification-highlight));
}
.qa-page-student-notifications .notification-page-actions .btn-outline-secondary {
    border: 1px solid rgba(118, 131, 192, .16);
    background: rgba(255, 255, 255, .82);
    color: #5b628e;
}
.qa-page-student-notifications .notification-page-actions .btn-outline-secondary:hover {
    background: #fff;
    color: #424f85;
}
.qa-page-student-notifications .notification-toolbar {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: space-between;
    gap: .95rem;
    margin-bottom: 1rem;
}
.qa-page-student-notifications .notification-toolbar-copy {
    min-width: 0;
}
.qa-page-student-notifications .notification-toolbar-title {
    margin: 0 0 .22rem;
    color: #213451;
    font-size: 1.02rem;
    font-weight: 800;
    letter-spacing: -.03em;
}
.qa-page-student-notifications .notification-toolbar-copy p {
    margin: 0;
    color: #7e88a4;
    font-size: .9rem;
}
.qa-page-student-notifications .notification-filter-bar {
    display: inline-flex;
    flex-wrap: wrap;
    gap: .55rem;
    margin: 0;
    padding: .55rem;
    border: 1px solid rgba(118, 131, 192, .13);
    border-radius: 18px;
    background: rgba(255, 255, 255, .78);
    box-shadow: 0 12px 28px rgba(42, 53, 103, .08);
    list-style: none;
}
.qa-page-student-notifications .notification-filter-bar .nav-item {
    margin: 0;
}
.qa-page-student-notifications .notification-filter-bar .nav-link {
    display: inline-flex;
    align-items: center;
    gap: .5rem;
    min-height: 42px;
    padding: .72rem 1rem;
    border: 0;
    border-radius: 14px;
    color: #68739a;
    font-size: .86rem;
    font-weight: 800;
    transition: transform .22s ease, box-shadow .22s ease, color .22s ease, background .22s ease;
}
.qa-page-student-notifications .notification-filter-bar .nav-link:hover {
    color: #4f58c9;
    background: rgba(242, 242, 255, .96);
    transform: translateY(-1px);
}
.qa-page-student-notifications .notification-filter-bar .nav-link.active {
    color: #fff;
    background: linear-gradient(135deg, var(--notification-accent), var(--notification-highlight));
    box-shadow: 0 16px 28px rgba(95, 116, 255, .2);
}
.qa-page-student-notifications .notification-filter-bar .nav-link .badge {
    padding: .33rem .55rem;
    border-radius: 999px;
    font-size: .72rem;
    font-weight: 800;
}
.qa-page-student-notifications .notification-filter-bar .nav-link.active .badge {
    color: #495bd8;
    background: #fff !important;
}
.qa-page-student-notifications .notification-board {
    position: relative;
    overflow: hidden;
    border: 1px solid rgba(118, 131, 192, .13);
    border-radius: 26px;
    background: linear-gradient(180deg, rgba(255, 255, 255, .95), rgba(252, 249, 255, .96));
    box-shadow: 0 24px 52px rgba(42, 53, 103, .09);
}
.qa-page-student-notifications .notification-board::before {
    position: absolute;
    inset: 0 auto auto 0;
    width: 100%;
    height: 160px;
    background: linear-gradient(180deg, rgba(244, 242, 255, .82), transparent);
    content: "";
    pointer-events: none;
}
.qa-page-student-notifications .notification-board-header {
    position: relative;
    z-index: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 1rem;
    padding: 1.1rem 1.2rem 0;
}
.qa-page-student-notifications .notification-board-header .notification-board-title {
    margin: 0;
    color: #213451;
    font-size: 1rem;
    font-weight: 800;
}
.qa-page-student-notifications .notification-board-header .notification-board-badge {
    display: inline-flex;
    align-items: center;
    gap: .45rem;
    padding: .55rem .85rem;
    border: 1px solid rgba(118, 131, 192, .13);
    border-radius: 999px;
    background: rgba(255, 255, 255, .82);
    color: #5d628e;
    font-size: .82rem;
    font-weight: 800;
    white-space: nowrap;
}
.qa-page-student-notifications .notification-list {
    position: relative;
    z-index: 1;
    display: grid;
    gap: 1rem;
    padding: 1.2rem;
}
.qa-page-student-notifications .notification-list-item {
    position: relative;
    display: grid;
    grid-template-columns: auto minmax(0, 1fr) auto;
    align-items: start;
    gap: 1rem;
    padding: 1rem 1rem 1rem 1.05rem !important;
    border: 1px solid rgba(118, 131, 192, .12) !important;
    border-radius: 22px;
    background:
        linear-gradient(135deg, rgba(255, 255, 255, .98), rgba(250, 248, 255, .96)),
        #fff;
    box-shadow: 0 18px 36px rgba(42, 53, 103, .07);
    opacity: 0;
    transform: translateY(18px);
    transition: transform .24s ease, box-shadow .24s ease, border-color .24s ease, background .24s ease;
}
.qa-page-student-notifications .notification-list-item.is-visible {
    opacity: 1;
    transform: translateY(0);
    animation: qa-student-notification-enter .5s ease both;
    animation-delay: var(--notification-delay, 0ms);
}
.qa-page-student-notifications .notification-list-item::before {
    position: absolute;
    top: 12px;
    bottom: 12px;
    left: 0;
    width: 4px;
    border-radius: 0 999px 999px 0;
    background: linear-gradient(180deg, rgba(168, 176, 214, .45), rgba(168, 176, 214, .08));
    content: "";
}
.qa-page-student-notifications .notification-list-item.is-unread {
    border-color: rgba(95, 116, 255, .24) !important;
    background:
        radial-gradient(circle at top right, rgba(217, 81, 168, .08), transparent 28%),
        linear-gradient(135deg, rgba(255, 255, 255, 1), rgba(244, 240, 255, .98));
}
.qa-page-student-notifications .notification-list-item.is-unread::before {
    background: linear-gradient(180deg, var(--notification-accent), var(--notification-highlight));
}
.qa-page-student-notifications .notification-list-item:hover {
    transform: translateY(-4px);
    border-color: rgba(95, 116, 255, .24) !important;
    box-shadow: 0 24px 40px rgba(42, 53, 103, .11);
}
.qa-page-student-notifications .notification-icon-shell {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 62px;
    height: 62px;
    margin-top: .1rem;
    border-radius: 22px;
    background: linear-gradient(135deg, rgba(95, 116, 255, .16), rgba(217, 81, 168, .13));
    box-shadow: inset 0 1px 0 rgba(255, 255, 255, .75), 0 14px 26px rgba(95, 116, 255, .12);
}
.qa-page-student-notifications .notification-icon-shell::after {
    position: absolute;
    inset: 8px;
    border-radius: 16px;
    border: 1px solid rgba(255, 255, 255, .65);
    content: "";
}
.qa-page-student-notifications .notification-icon-shell .avatar {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    border-radius: inherit;
    color: var(--notification-accent-strong);
    font-size: 1.35rem;
    background: transparent;
}
.qa-page-student-notifications .notification-item-main {
    min-width: 0;
}
.qa-page-student-notifications .notification-meta {
    display: flex;
    flex-wrap: wrap;
    gap: .45rem;
    margin-bottom: .72rem;
}
.qa-page-student-notifications .notification-chip {
    display: inline-flex;
    align-items: center;
    gap: .35rem;
    min-height: 30px;
    padding: .42rem .7rem;
    border-radius: 999px;
    font-size: .75rem;
    font-weight: 800;
    letter-spacing: .02em;
}
.qa-page-student-notifications .notification-chip--new {
    color: #fff;
    background: linear-gradient(135deg, var(--notification-accent), var(--notification-highlight));
    box-shadow: 0 12px 22px rgba(95, 116, 255, .22);
}
.qa-page-student-notifications .notification-chip--type {
    color: #5b628e;
    background: rgba(243, 242, 255, .98);
}
.qa-page-student-notifications .notification-chip--state {
    color: #59657f;
    background: rgba(246, 247, 252, .96);
}
.qa-page-student-notifications .notification-chip--state.is-unread {
    color: #136f4b;
    background: rgba(222, 250, 239, .96);
}
.qa-page-student-notifications .notification-open-form {
    width: 100%;
    min-width: 0;
}
.qa-page-student-notifications .notification-open-button {
    display: block;
    width: 100%;
    min-width: 0;
    padding: 0 !important;
    color: inherit;
    text-align: left;
    text-decoration: none;
    white-space: normal;
}
.qa-page-student-notifications .notification-open-button:hover .notification-title {
    color: #4a54d1;
}
.qa-page-student-notifications .notification-title-row,
.qa-page-student-notifications .notification-footer-row {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: .75rem;
}
.qa-page-student-notifications .notification-title-row {
    align-items: flex-start;
    margin-bottom: .45rem;
}
.qa-page-student-notifications .notification-title {
    display: block;
    min-width: 0;
    color: #213451;
    font-size: 1rem;
    font-weight: 800;
    line-height: 1.55;
    overflow-wrap: anywhere;
    transition: color .24s ease;
}
.qa-page-student-notifications .notification-body {
    display: block;
    margin-bottom: .8rem;
    color: #66748f !important;
    font-size: .91rem;
    line-height: 1.75;
    overflow-wrap: anywhere;
}
.qa-page-student-notifications .notification-open-hint {
    display: inline-flex;
    align-items: center;
    gap: .35rem;
    color: #6b58b4;
    font-size: .8rem;
    font-weight: 800;
    white-space: nowrap;
}
.qa-page-student-notifications .notification-open-hint i {
    transition: transform .24s ease;
}
.qa-page-student-notifications .notification-open-button:hover .notification-open-hint i {
    transform: translate(2px, -2px);
}
.qa-page-student-notifications .notification-time {
    display: inline-flex;
    align-items: center;
    gap: .4rem;
    color: var(--notification-muted) !important;
    font-size: .82rem;
    font-weight: 600;
}
.qa-page-student-notifications .notification-time::before {
    width: 7px;
    height: 7px;
    border-radius: 50%;
    background: rgba(95, 116, 255, .28);
    content: "";
    flex-shrink: 0;
}
.qa-page-student-notifications .notification-side-actions {
    display: flex;
    align-items: center;
    justify-content: center;
    align-self: center;
}
.qa-page-student-notifications .notification-hide-form {
    margin: 0;
}
.qa-page-student-notifications .notification-hide-form .btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 42px;
    height: 42px;
    padding: 0;
    border: 1px solid rgba(118, 131, 192, .13);
    border-radius: 14px;
    background: rgba(255, 255, 255, .9);
    color: #8a96ab;
    transition: transform .22s ease, color .22s ease, background .22s ease, border-color .22s ease;
}
.qa-page-student-notifications .notification-hide-form .btn:hover {
    transform: rotate(90deg);
    color: #d14a67;
    border-color: rgba(209, 74, 103, .2);
    background: rgba(255, 242, 246, .98);
}
.qa-page-student-notifications .notification-card-footer {
    position: relative;
    z-index: 1;
    padding: 1.05rem 1.2rem 1.2rem;
    border-top: 1px solid rgba(118, 131, 192, .12);
    background: linear-gradient(180deg, rgba(251, 250, 255, .84), rgba(255, 255, 255, .96));
}
.qa-page-student-notifications .notification-empty {
    padding: 2.4rem 1.2rem;
    background:
        radial-gradient(circle at top center, rgba(95, 116, 255, .08), transparent 34%),
        linear-gradient(180deg, rgba(252, 249, 255, .96), rgba(255, 255, 255, .98));
}
.qa-page-student-notifications .notification-empty .empty-state {
    padding: 1.2rem 0;
}
.qa-page-student-notifications .badge.bg-primary {
    background: linear-gradient(135deg, var(--notification-accent), var(--notification-highlight)) !important;
}
@keyframes qa-student-notification-enter {
    from {
        opacity: 0;
        transform: translateY(18px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}
@media (max-width: 1199.98px) {
    .qa-page-student-notifications .notification-hero {
        grid-template-columns: 1fr;
    }
}
@media (max-width: 767.98px) {
    .qa-page-student-notifications .notification-hero {
        padding: 1rem;
        border-radius: 24px;
    }
    .qa-page-student-notifications .notification-title-wrap {
        gap: .8rem;
    }
    .qa-page-student-notifications .qa-view-title-icon {
        flex-basis: 48px;
        width: 48px;
        height: 48px;
        border-radius: 18px;
        font-size: 1.25rem;
    }
    .qa-page-student-notifications .notification-board-header {
        flex-direction: column;
        align-items: flex-start;
        padding: 1rem 1rem 0;
    }
    .qa-page-student-notifications .notification-list {
        padding: 1rem;
    }
    .qa-page-student-notifications .notification-list-item {
        grid-template-columns: 1fr;
        gap: .85rem;
    }
    .qa-page-student-notifications .notification-icon-shell {
        width: 56px;
        height: 56px;
    }
    .qa-page-student-notifications .notification-side-actions {
        justify-content: flex-end;
    }
    .qa-page-student-notifications .notification-title-row,
    .qa-page-student-notifications .notification-footer-row {
        flex-direction: column;
        align-items: flex-start;
    }
}
@media (max-width: 575.98px) {
    .qa-page-student-notifications .notification-page-actions {
        width: 100%;
    }
    .qa-page-student-notifications .notification-page-actions > form,
    .qa-page-student-notifications .notification-page-actions > a,
    .qa-page-student-notifications .notification-page-actions .btn {
        flex: 1 1 auto;
        width: 100%;
        justify-content: center;
    }
    .qa-page-student-notifications .notification-filter-bar {
        width: 100%;
    }
    .qa-page-student-notifications .notification-filter-bar .nav-item,
    .qa-page-student-notifications .notification-filter-bar .nav-link {
        width: 100%;
    }
    .qa-page-student-notifications .notification-list {
        padding: .85rem;
    }
    .qa-page-student-notifications .notification-list-item {
        padding: .95rem !important;
        border-radius: 18px;
    }
}
@media (prefers-reduced-motion: reduce) {
    .qa-page-student-notifications *,
    .qa-page-student-notifications *::before,
    .qa-page-student-notifications *::after {
        animation-duration: .01ms !important;
        animation-iteration-count: 1 !important;
        transition-duration: .01ms !important;
        scroll-behavior: auto !important;
    }
}
</style>

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
                    <div class="list-group-item notification-list-item ${notification.read() ? 'is-read' : 'is-unread'}" style="--notification-delay:${loop.index * 70}ms;">
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

        document.querySelectorAll('.qa-page-student-notifications [data-relative-time]').forEach(function (element) {
            element.textContent = relativeTime(element.dataset.relativeTime);
        });

        document.querySelectorAll('.qa-page-student-notifications .notification-list-item').forEach(function (item, index) {
            item.style.setProperty('--notification-delay', (index * 70) + 'ms');
            requestAnimationFrame(function () {
                item.classList.add('is-visible');
            });
        });
    }());
</script>
</div>
