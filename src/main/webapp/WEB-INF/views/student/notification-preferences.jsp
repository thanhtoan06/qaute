<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Tùy chọn thông báo</title>

<div class="page-header d-print-none mb-3">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
        <div>
            <h2 class="page-title mb-1">Tùy chọn thông báo</h2>
            <div class="text-secondary">Chọn cách bạn muốn nhận từng loại thông báo.</div>
        </div>
        <a class="btn btn-outline-secondary" href="${ctx}/student/notifications"><i class="bi bi-arrow-left me-1"></i>Quay lại thông báo</a>
    </div>
</div>

<form method="post" action="${ctx}/student/notifications/preferences">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <section class="card">
        <div class="card-header"><h3 class="card-title">Kênh nhận thông báo</h3></div>
        <div class="table-responsive">
            <table class="table table-vcenter card-table">
                <thead><tr><th>Loại sự kiện</th><th class="text-center" style="width: 150px">Trong ứng dụng</th><th class="text-center" style="width: 130px">Email</th></tr></thead>
                <tbody>
                    <c:forEach var="preference" items="${preferences}">
                        <tr>
                            <td>
                                <div class="fw-medium"><c:out value="${preference.label()}"/></div>
                                <c:if test="${preference.emailLocked()}"><div class="text-secondary small">Bắt buộc vì lý do bảo mật/quan trọng.</div></c:if>
                            </td>
                            <td class="text-center">
                                <label class="form-check form-switch d-inline-flex"><input class="form-check-input" type="checkbox" name="inApp.${preference.type()}" value="true" ${preference.inApp() ? 'checked' : ''}/><span class="visually-hidden">Trong ứng dụng</span></label>
                            </td>
                            <td class="text-center">
                                <label class="form-check form-switch d-inline-flex"><input class="form-check-input" type="checkbox" name="email.${preference.type()}" value="true" ${preference.email() ? 'checked' : ''} ${preference.emailLocked() ? 'disabled' : ''}/><span class="visually-hidden">Email</span></label>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="card-footer d-flex justify-content-end"><button class="btn btn-primary" type="submit"><i class="bi bi-check2 me-1"></i>Lưu</button></div>
    </section>
</form>
