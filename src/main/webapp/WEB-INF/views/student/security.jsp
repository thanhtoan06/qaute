<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Bảo mật tài khoản</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="row g-4">
    <div class="col-12 col-lg-5">
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">Đổi mật khẩu</h3>
            </div>
            <div class="card-body">
                <form method="post" action="${ctx}/student/profile/security/password" id="register-form" novalidate>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <c:if test="${not empty errors['global']}">
                        <div class="alert alert-danger mb-3"><c:out value="${errors['global']}"/></div>
                    </c:if>
                    <div class="mb-3">
                        <label class="form-label required" for="currentPassword">Mật khẩu hiện tại</label>
                        <div class="input-group input-group-flat">
                            <input type="password" class="form-control ${not empty errors['currentPassword'] ? 'is-invalid' : ''}"
                                   id="currentPassword" name="currentPassword" autocomplete="current-password" required>
                            <button class="btn btn-icon" type="button" data-toggle-password="#currentPassword"
                                    aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
                        </div>
                        <qa:formError field="currentPassword"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label required" for="password">Mật khẩu mới</label>
                        <div class="input-group input-group-flat">
                            <input type="password" class="form-control ${not empty errors['newPassword'] ? 'is-invalid' : ''}"
                                   id="password" name="newPassword" autocomplete="new-password" minlength="8" maxlength="64" required>
                            <button class="btn btn-icon" type="button" data-toggle-password="#password"
                                    aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
                        </div>
                        <div class="progress mt-2" style="height: 5px;"><div id="password-strength" class="progress-bar"></div></div>
                        <small id="password-strength-text" class="form-hint"></small>
                        <qa:formError field="newPassword"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label required" for="confirmPassword">Nhập lại mật khẩu mới</label>
                        <div class="input-group input-group-flat">
                            <input type="password" class="form-control ${not empty errors['confirmPassword'] ? 'is-invalid' : ''}"
                                   id="confirmPassword" name="confirmPassword" autocomplete="new-password" required>
                            <button class="btn btn-icon" type="button" data-toggle-password="#confirmPassword"
                                    aria-label="Hiện hoặc ẩn mật khẩu"><i class="bi bi-eye"></i></button>
                        </div>
                        <qa:formError field="confirmPassword"/>
                    </div>
                    <button type="submit" class="btn btn-primary w-100" id="register-submit" data-loading-text="Đang đổi mật khẩu...">
                        <i class="bi bi-shield-lock me-1"></i> Đổi mật khẩu
                    </button>
                </form>
            </div>
        </div>
    </div>
    <div class="col-12 col-lg-7">
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h3 class="card-title m-0">Phiên đăng nhập</h3>
                <qa:confirmModal id="revoke-others-modal"
                                 title="Đăng xuất tất cả thiết bị khác"
                                 message="Bạn có chắc chắn muốn đăng xuất khỏi tất cả các thiết bị khác không? Phiên đăng nhập hiện tại sẽ được giữ lại."
                                 actionUrl="${ctx}/student/profile/sessions/revoke-others"
                                 confirmLabel="Đăng xuất tất cả thiết bị khác"
                                 cssClass="btn-outline-danger btn-sm"/>
            </div>
            <div class="table-responsive">
                <table class="table card-table table-vcenter">
                    <thead>
                        <tr>
                            <th>Thiết bị</th>
                            <th>Địa chỉ IP</th>
                            <th>Tạo lúc</th>
                            <th>Hoạt động cuối</th>
                            <th>Trạng thái</th>
                            <th class="w-1"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="session" items="${sessions}">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <i class="bi bi-laptop me-2 fs-3 text-secondary"></i>
                                        <div><c:out value="${empty session.deviceInfo() ? 'Không xác định' : session.deviceInfo()}"/></div>
                                    </div>
                                </td>
                                <td><c:out value="${empty session.ip() ? '—' : session.ip()}"/></td>
                                <td><c:out value="${session.createdAt()}"/></td>
                                <td><c:out value="${empty session.lastUsedAt() ? '—' : session.lastUsedAt()}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${session.current()}">
                                            <span class="badge bg-success-lt"><i class="bi bi-check-circle me-1"></i>Phiên hiện tại</span>
                                        </c:when>
                                        <c:when test="${session.revoked()}">
                                            <span class="badge bg-secondary-lt">Đã đăng xuất</span>
                                        </c:when>
                                        <c:when test="${session.expired()}">
                                            <span class="badge bg-warning-lt">Hết hạn</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-primary-lt">Đang hoạt động</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:if test="${!session.current() and !session.revoked() and !session.expired()}">
                                        <form method="post" action="${ctx}/student/profile/sessions/${session.id()}/revoke" class="d-inline">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <button type="submit" class="btn btn-sm btn-outline-danger text-nowrap">
                                                Đăng xuất thiết bị
                                            </button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty sessions}">
                            <tr>
                                <td colspan="6" class="text-center text-secondary py-3">Chưa có phiên đăng nhập nào.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script src="/assets/js/auth/register.js"></script>

