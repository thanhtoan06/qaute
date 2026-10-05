<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%--
  QAUTE - Decorator khu vực Manager (SiteMesh áp dụng cho /manager/*).
  Bố cục giống student.jsp: menu dọc bên trái + header trên cùng + vùng nội dung.
  SiteMesh 3 không có taglib JSP riêng: thẻ <sitemesh:write> được bộ lọc SiteMesh thay thế
  khi trả về, nên ở đây không khai báo prefix "sitemesh".
--%>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%-- Đường dẫn hiện tại (bỏ context path). Sau forward thì lấy từ thuộc tính forward.request_uri. --%>
<c:set var="rawUri" value="${not empty requestScope['jakarta.servlet.forward.request_uri'] ? requestScope['jakarta.servlet.forward.request_uri'] : pageContext.request.requestURI}"/>
<c:set var="currentPath" value="${fn:startsWith(rawUri, ctx) ? fn:substring(rawUri, fn:length(ctx), fn:length(rawUri)) : rawUri}"/>
<%-- Trang chi tiết yêu cầu (/manager/tickets/{id}) thuộc mục "Hàng đợi yêu cầu". --%>
<c:set var="navPath" value="${fn:startsWith(currentPath, '/manager/tickets') ? '/manager/queue' : currentPath}"/>

<%--
  Tô sáng menu: chọn đường dẫn menu KHỚP DÀI NHẤT với đường dẫn hiện tại.
  Khi thêm mục menu mới, nhớ thêm đường dẫn của nó vào menuPaths.
--%>
<c:set var="menuPaths" value="/manager/dashboard,/manager/queue,/manager/chat,/manager/schedule,/manager/appointments,/manager/students,/manager/canned-responses,/manager/faq,/manager/reports,/manager/profile"/>
<c:set var="activePath" value=""/>
<c:forTokens items="${menuPaths}" delims="," var="menuPath">
  <c:if test="${navPath == menuPath or fn:startsWith(navPath, menuPath.concat('/'))}">
    <c:if test="${fn:length(menuPath) gt fn:length(activePath)}">
      <c:set var="activePath" value="${menuPath}"/>
    </c:if>
  </c:if>
</c:forTokens>

<%-- Trạng thái trực hiện tại (ONLINE/BUSY/OFFLINE) do ManagerContextAdvice nạp ở bước T2-31; chưa có thì ẩn nhóm nút. --%>
<c:set var="mgrStatus" value="${fn:toUpperCase(managerStatus)}"/>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
  <title><sitemesh:write property="title"/> - QAUTE</title>
  <sitemesh:write property="head"/>
</head>
<body>
<div class="page">

  <%-- ===== Menu dọc bên trái ===== --%>
  <aside class="navbar navbar-vertical navbar-expand-lg" data-bs-theme="dark">
    <div class="container-fluid">
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#qa-sidebar-menu"
              aria-controls="qa-sidebar-menu" aria-expanded="false" aria-label="Mở menu">
        <span class="navbar-toggler-icon"></span>
      </button>
      <h1 class="navbar-brand navbar-brand-autodark">
        <a href="${ctx}/manager/dashboard" class="text-decoration-none">
          <i class="bi bi-mortarboard-fill me-2"></i>QAUTE <small class="text-secondary">Manager</small>
        </a>
      </h1>
      <div class="collapse navbar-collapse" id="qa-sidebar-menu">
        <ul class="navbar-nav pt-lg-3">
          <li class="nav-item ${activePath == '/manager/dashboard' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/manager/dashboard">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-speedometer2"></i></span>
              <span class="nav-link-title">Tổng quan</span>
            </a>
          </li>
          <li class="nav-item ${activePath == '/manager/queue' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/manager/queue">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-inbox"></i></span>
              <span class="nav-link-title">Hàng đợi yêu cầu</span>
            </a>
          </li>
          <li class="nav-item ${activePath == '/manager/chat' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/manager/chat">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-chat-dots"></i></span>
              <span class="nav-link-title">Chat desk</span>
            </a>
          </li>
          <li class="nav-item ${activePath == '/manager/schedule' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/manager/schedule">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-calendar-week"></i></span>
              <span class="nav-link-title">Lịch làm việc</span>
            </a>
          </li>
          <li class="nav-item ${activePath == '/manager/appointments' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/manager/appointments">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-calendar-check"></i></span>
              <span class="nav-link-title">Lịch hẹn</span>
            </a>
          </li>
          <li class="nav-item ${activePath == '/manager/students' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/manager/students">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-people"></i></span>
              <span class="nav-link-title">Sinh viên</span>
            </a>
          </li>
          <li class="nav-item ${activePath == '/manager/canned-responses' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/manager/canned-responses">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-chat-left-text"></i></span>
              <span class="nav-link-title">Mẫu trả lời</span>
            </a>
          </li>
          <li class="nav-item ${activePath == '/manager/faq' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/manager/faq">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-lightbulb"></i></span>
              <span class="nav-link-title">FAQ đề xuất</span>
            </a>
          </li>
          <li class="nav-item ${activePath == '/manager/reports' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/manager/reports">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-bar-chart-line"></i></span>
              <span class="nav-link-title">Báo cáo</span>
            </a>
          </li>
          <li class="nav-item ${activePath == '/manager/profile' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/manager/profile">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-person-circle"></i></span>
              <span class="nav-link-title">Hồ sơ</span>
            </a>
          </li>
        </ul>
      </div>
    </div>
  </aside>

  <%-- ===== Header trên cùng: trạng thái trực + chuông + người dùng ===== --%>
  <header class="navbar navbar-expand-md d-print-none">
    <div class="container-xl">
      <div class="navbar-nav flex-row ms-auto align-items-center gap-3">

        <%-- Nhóm nút trạng thái trực: chỉ hiện khi đã có requestScope.managerStatus (nạp ở T2-31). --%>
        <c:if test="${not empty mgrStatus}">
          <form method="post" action="${ctx}/manager/profile/status" class="m-0">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="btn-group btn-group-sm" role="group" aria-label="Trạng thái trực">
              <button type="submit" name="status" value="ONLINE"
                      class="btn qa-status-online ${mgrStatus == 'ONLINE' ? 'active' : ''}"
                      aria-pressed="${mgrStatus == 'ONLINE'}" title="Sẵn sàng nhận yêu cầu và chat">Trực tuyến</button>
              <button type="submit" name="status" value="BUSY"
                      class="btn qa-status-busy ${mgrStatus == 'BUSY' ? 'active' : ''}"
                      aria-pressed="${mgrStatus == 'BUSY'}" title="Tạm bận, không nhận chat mới">Bận</button>
              <button type="submit" name="status" value="OFFLINE"
                      class="btn qa-status-offline ${mgrStatus == 'OFFLINE' ? 'active' : ''}"
                      aria-pressed="${mgrStatus == 'OFFLINE'}" title="Không trực">Ngoại tuyến</button>
            </div>
          </form>
        </c:if>

        <%--
          TODO (T1-44): khi TV1 đã gộp notification-bell.jspf vào main thì thay khối comment này bằng 2 dòng:
            <c:set var="notificationsUrl" value="/manager/notifications" scope="request"/>
            <%@ include file="/WEB-INF/views/common/notification-bell.jspf" %>
          (Kế hoạch hiện chưa có trang thông báo cho Manager; cần thống nhất với TV1 giá trị notificationsUrl.)
        --%>

        <div class="nav-item dropdown">
          <a href="#" class="nav-link d-flex lh-1 text-reset p-0" data-bs-toggle="dropdown" aria-label="Mở menu người dùng">
            <span class="avatar avatar-sm bg-blue-lt"><i class="bi bi-person-fill"></i></span>
            <div class="d-none d-xl-block ps-2">
              <div><sec:authentication property="principal.fullName"/></div>
              <div class="mt-1 small text-secondary">Manager</div>
            </div>
          </a>
          <div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
            <a href="${ctx}/manager/profile" class="dropdown-item"><i class="bi bi-person me-2"></i>Hồ sơ</a>
            <a href="${ctx}/manager/profile/security" class="dropdown-item"><i class="bi bi-shield-lock me-2"></i>Bảo mật</a>
            <div class="dropdown-divider"></div>
            <form method="post" action="${ctx}/auth/logout" class="m-0">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
              <button type="submit" class="dropdown-item"><i class="bi bi-box-arrow-right me-2"></i>Đăng xuất</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </header>

  <%-- ===== Vùng nội dung ===== --%>
  <div class="page-wrapper">
    <div class="page-body">
      <div class="container-xl">
        <qa:alert/>
        <sitemesh:write property="body"/>
      </div>
    </div>
    <footer class="footer footer-transparent d-print-none">
      <div class="container-xl text-center text-secondary small">&copy; QAUTE - Student Support Hub (HCMUTE)</div>
    </footer>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
<%--
  TODO (T1-44): khi TV1 đã gộp ws-scripts.jspf vào main thì thay khối comment này bằng dòng:
    <%@ include file="/WEB-INF/views/common/ws-scripts.jspf" %>
--%>
</body>
</html>
