<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Kích hoạt tài khoản</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="row justify-content-center"><div class="col-md-6 col-lg-5"><div class="card">
  <div class="card-header"><h2 class="card-title">Kích hoạt tài khoản</h2></div>
  <div class="card-body">
    <c:choose>
      <c:when test="${!validToken}"><div class="alert alert-warning">Liên kết đã hết hạn, hãy nhờ quản trị gửi lại lời mời.</div></c:when>
      <c:otherwise>
        <c:if test="${not empty errors['global']}"><div class="alert alert-danger"><c:out value="${errors['global']}"/></div></c:if>
        <form method="post" action="${ctx}/auth/activate">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <input type="hidden" name="token" value="<c:out value='${form.token}'/>"/>
          <div class="mb-3"><label class="form-label" for="newPassword">Mật khẩu mới</label>
            <input class="form-control" id="newPassword" name="newPassword" type="password" autocomplete="new-password" required minlength="8" maxlength="64"/>
            <qa:formError field="newPassword"/></div>
          <div class="mb-3"><label class="form-label" for="confirmPassword">Nhập lại mật khẩu</label>
            <input class="form-control" id="confirmPassword" name="confirmPassword" type="password" autocomplete="new-password" required/>
            <qa:formError field="confirmPassword"/></div>
          <button class="btn btn-primary w-100" type="submit">Đặt mật khẩu và kích hoạt</button>
        </form>
      </c:otherwise>
    </c:choose>
  </div>
</div></div></div>
