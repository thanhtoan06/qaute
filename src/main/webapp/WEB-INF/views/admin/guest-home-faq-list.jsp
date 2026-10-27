<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Quản lý FAQ trang chủ - QAUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/guest-home-faq-list.css">
</head>
<div class="qa-page-admin-guest-home-faq-list" data-qa-view="admin-guest-home-faq-list">


<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0 qa-view-title" data-qa-page="admin-guest-home-faq-list"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span>Quản lý FAQ trang chủ</h2>
    <a href="/admin/guest-home-faq/new" class="btn btn-primary">
        <i class="bi bi-plus-lg me-1"></i>Thêm FAQ mới
    </a>
</div>

<div class="card shadow-sm">
    <div class="card-body">
        <c:if test="${not empty flashSuccess}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${flashSuccess}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${flashError}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="table-responsive">
            <table class="table table-hover align-middle">
                <thead class="table-light">
                    <tr>
                        <th class="u-width-50px">#</th>
                        <th>Danh mục</th>
                        <th>Câu hỏi</th>
                        <th class="u-width-80px">Thứ tự</th>
                        <th class="u-width-100px">Trạng thái</th>
                        <th class="u-width-180px">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="faq" items="${faqs}" varStatus="status">
                        <tr>
                            <td class="text-muted">${status.count}</td>
                            <td><span class="badge bg-primary-lt text-primary">${faq.categoryName}</span></td>
                            <td><strong>${faq.question}</strong></td>
                            <td>${faq.displayOrder}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${faq.active}">
                                        <span class="badge bg-success-lt text-success">Hiển thị</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary-lt text-secondary">Ẩn</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="btn-group btn-group-sm">
                                    <form action="/admin/guest-home-faq/${faq.id}/move-up" method="post" class="u-display-inline">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                        <button type="submit" class="btn btn-outline-secondary" title="Đưa lên trên">
                                            <i class="bi bi-arrow-up"></i>
                                        </button>
                                    </form>
                                    <form action="/admin/guest-home-faq/${faq.id}/move-down" method="post" class="u-display-inline">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                        <button type="submit" class="btn btn-outline-secondary" title="Đưa xuống dưới">
                                            <i class="bi bi-arrow-down"></i>
                                        </button>
                                    </form>
                                    <a href="/admin/guest-home-faq/${faq.id}/edit" class="btn btn-outline-primary" title="Sửa">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <form action="/admin/guest-home-faq/${faq.id}/toggle" method="post" class="u-display-inline">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                        <button type="submit" class="btn btn-outline-${faq.active ? 'warning' : 'success'}" title="${faq.active ? 'Ẩn' : 'Hiển thị'}">
                                            <i class="bi bi-eye${faq.active ? '-slash' : ''}"></i>
                                        </button>
                                    </form>
                                    <form action="/admin/guest-home-faq/${faq.id}/delete" method="post" class="u-display-inline" data-confirm-message="Bạn có chắc muốn xóa FAQ này?">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                        <button type="submit" class="btn btn-outline-danger" title="Xóa">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty faqs}">
                        <tr>
                            <td colspan="6" class="text-center py-4 text-muted">
                                Chưa có FAQ nào. <a href="/admin/guest-home-faq/new">Thêm FAQ mới</a>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
</div>
