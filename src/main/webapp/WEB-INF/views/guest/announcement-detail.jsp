<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title><c:out value="${announcement.title()}"/></title>

<nav aria-label="breadcrumb" class="mb-3">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
        <li class="breadcrumb-item active" aria-current="page">Thông báo</li>
    </ol>
</nav>

<div class="row g-4">
    <div class="col-lg-8">
        <article class="card p-4 mb-4 shadow-sm">
            <div class="mb-3">
                <c:if test="${announcement.pinned()}">
                    <span class="badge bg-danger-lt mb-2"><i class="bi bi-pin-angle-fill me-1"></i>Thông báo quan trọng (Đã ghim)</span>
                </c:if>
                <h1 class="h1 mb-2"><c:out value="${announcement.title()}"/></h1>
                <c:if test="${not empty announcement.publishAt()}">
                    <div class="text-secondary small">
                        <i class="bi bi-calendar3 me-1"></i>Ngày đăng:
                        ${announcement.publishAt().dayOfMonth}/${announcement.publishAt().monthValue}/${announcement.publishAt().year}
                    </div>
                </c:if>
            </div>

            <c:if test="${not empty announcement.coverUrl()}">
                <div class="mb-4 text-center">
                    <img src="<c:out value='${announcement.coverUrl()}'/>"
                         alt="<c:out value='${announcement.title()}'/>"
                         class="img-fluid rounded shadow-sm w-100"
                         style="max-height: 420px; object-fit: cover;">
                </div>
            </c:if>

            <c:if test="${not empty announcement.summary()}">
                <div class="lead text-secondary mb-4 p-3 bg-light rounded border-start border-primary border-3">
                    <c:out value="${announcement.summary()}"/>
                </div>
            </c:if>

            <div class="announcement-content markdown py-2">
                <c:out value="${sanitizedBodyHtml}" escapeXml="false"/>
            </div>
        </article>
    </div>

    <div class="col-lg-4">
        <div class="card mb-3 shadow-sm">
            <div class="card-header">
                <h3 class="card-title mb-0"><i class="bi bi-compass me-2 text-primary"></i>Điều hướng</h3>
            </div>
            <div class="card-body d-flex flex-column gap-2">
                <a href="/" class="btn btn-outline-secondary w-100">
                    <i class="bi bi-arrow-left me-1"></i>Về trang chủ
                </a>
                <sec:authorize access="hasRole('STUDENT')">
                    <a href="/student/dashboard" class="btn btn-primary w-100">
                        <i class="bi bi-speedometer2 me-1"></i>Về bảng điều khiển sinh viên
                    </a>
                </sec:authorize>
            </div>
        </div>

        <div class="card shadow-sm">
            <div class="card-header">
                <h3 class="card-title mb-0"><i class="bi bi-headset me-2 text-azure"></i>Cần trợ giúp thêm?</h3>
            </div>
            <div class="card-body">
                <p class="text-secondary small mb-3">Nếu bạn có câu hỏi liên quan đến thông báo này hoặc cần được tư vấn chi tiết, vui lòng kết nối với chúng tôi.</p>
                <div class="d-flex flex-column gap-2">
                    <a href="/faq" class="btn btn-sm btn-ghost-primary text-start">
                        <i class="bi bi-question-circle me-2"></i>Tra cứu câu hỏi thường gặp (FAQ)
                    </a>
                    <a href="/student/chat" class="btn btn-sm btn-ghost-teal text-start">
                        <i class="bi bi-chat-dots me-2"></i>Chat trực tuyến với tư vấn viên
                    </a>
                    <a href="/student/tickets/new" class="btn btn-sm btn-ghost-primary text-start">
                        <i class="bi bi-pencil-square me-2"></i>Gửi yêu cầu hỗ trợ mới
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

