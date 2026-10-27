<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>${pageTitle} - QAUTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/admin/guest-home-faq-form.css">
</head>
<div class="qa-page-admin-guest-home-faq-form" data-qa-view="admin-guest-home-faq-form">


<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0 qa-view-title" data-qa-page="admin-guest-home-faq-form"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-patch-question"></i></span>${pageTitle}</h2>
    <a href="/admin/guest-home-faq" class="btn btn-outline-secondary">
        <i class="bi bi-arrow-left me-1"></i>Quay lại
    </a>
</div>

<div class="card shadow-sm">
    <div class="card-body">
        <c:if test="${not empty errors}">
            <div class="alert alert-danger">
                <c:forEach var="error" items="${errors}">
                    <div>${error.value}</div>
                </c:forEach>
            </div>
        </c:if>

        <form action="/admin/guest-home-faq/save" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <c:if test="${form.id != null}">
                <input type="hidden" name="id" value="${form.id}">
            </c:if>

            <div class="mb-3">
                <label for="categoryName" class="form-label">Danh mục <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="categoryName" name="categoryName"
                       value="${form.categoryName}" required placeholder="Ví dụ: Học vụ, Học phí, Đăng ký môn...">
                <div class="form-text">Tên danh mục hiển thị trên trang chủ guest</div>
            </div>

            <div class="mb-3">
                <label for="question" class="form-label">Câu hỏi <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="question" name="question"
                       value="${form.question}" required placeholder="Nhập câu hỏi..." maxlength="300">
            </div>

            <div class="mb-3">
                <label for="answer" class="form-label">Câu trả lời <span class="text-danger">*</span></label>
                <textarea class="form-control" id="answer" name="answer" rows="5"
                          required placeholder="Nhập câu trả lời (có thể dùng HTML)">${form.answer}</textarea>
                <div class="form-text">Có thể sử dụng HTML cơ bản để định dạng nội dung</div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="displayOrder" class="form-label">Thứ tự hiển thị <span class="text-danger">*</span></label>
                    <input type="number" class="form-control" id="displayOrder" name="displayOrder"
                           value="${form.displayOrder}" required min="1">
                    <div class="form-text">Số càng nhỏ càng hiển thị ở trên</div>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label d-block">Trạng thái</label>
                    <div class="form-check mt-2">
                        <input class="form-check-input" type="checkbox" id="isActive" name="isActive"
                               ${form.active ? 'checked' : ''}>
                        <label class="form-check-label" for="isActive">
                            Hiển thị trên trang chủ
                        </label>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end gap-2">
                <a href="/admin/guest-home-faq" class="btn btn-outline-secondary">Hủy</a>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-lg me-1"></i>Lưu FAQ
                </button>
            </div>
        </form>
    </div>
</div>
</div>
