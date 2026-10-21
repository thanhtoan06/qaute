<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page import="org.sitemesh.content.Content" %>
<%
    Content pageContent = (Content) request.getAttribute("org.sitemesh.content.Content");
%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="/WEB-INF/views/common/tv2-workspace-user.jspf" %>

<c:set var="rawUri" value="${not empty requestScope['jakarta.servlet.forward.request_uri'] ? requestScope['jakarta.servlet.forward.request_uri'] : pageContext.request.requestURI}"/>
<c:set var="currentPath" value="${fn:startsWith(rawUri, ctx) ? fn:substring(rawUri, fn:length(ctx), fn:length(rawUri)) : rawUri}"/>
<c:set var="navPath" value="${fn:startsWith(currentPath, '/manager/tickets') ? '/manager/queue' : currentPath}"/>

<c:set var="menuPaths" value="/manager/dashboard,/manager/queue,/manager/chat,/manager/schedule,/manager/appointments,/manager/students,/manager/canned-responses,/manager/faq,/manager/reports,/manager/profile"/>
<c:set var="activePath" value=""/>
<c:forTokens items="${menuPaths}" delims="," var="menuPath">
  <c:if test="${navPath == menuPath or fn:startsWith(navPath, menuPath.concat('/'))}">
    <c:if test="${fn:length(menuPath) gt fn:length(activePath)}">
      <c:set var="activePath" value="${menuPath}"/>
    </c:if>
  </c:if>
</c:forTokens>

<c:set var="mgrStatus" value="${fn:toUpperCase(managerStatus)}"/>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
  <link rel="stylesheet" href="${ctx}/assets/css/tv2-workspace.css">
  <title><%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE</title>
  <%= pageContent.getExtractedProperties().getChild("head").getValue() %>
</head>
<body class="qa-workspace qa-workspace--manager">
<a class="qa-workspace-skip" href="#workspace-main">Chuyển đến nội dung</a>
<button class="qa-workspace-overlay" type="button" data-workspace-close hidden aria-label="Đóng menu điều hướng"></button>
<div class="page">

  <aside class="qa-workspace-sidebar" id="workspace-sidebar" data-bs-theme="dark" aria-label="Điều hướng tư vấn viên">
    <div class="container-fluid">
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#qa-sidebar-menu"
              aria-controls="qa-sidebar-menu" aria-expanded="false" aria-label="Mở menu">
        <span class="navbar-toggler-icon"></span>
      </button>
      <h1 class="navbar-brand navbar-brand-autodark">
        <a href="${ctx}/manager/dashboard" class="text-decoration-none">
          <img src="${ctx}/assets/images/hcmute-logo.png" alt="Logo HCMUTE" class="qa-role-logo">
          <span class="qa-workspace-brand-copy">QA<span>UTE</span><small>CỔNG HỖ TRỢ SINH VIÊN</small></span>
        </a>
      </h1>
      <a class="qa-workspace-user-card" href="${ctx}/manager/profile">
        <span class="qa-workspace-avatar"><c:out value="${workspaceInitial}"/></span>
        <span class="qa-workspace-user-copy"><strong><c:out value="${workspaceName}"/></strong><small>Tư vấn viên</small></span>
        <i class="bi bi-chevron-right" aria-hidden="true"></i>
      </a>
      <div class="collapse navbar-collapse" id="qa-sidebar-menu">
        <ul class="navbar-nav pt-lg-3">
          <li class="qa-workspace-nav-label">KHÔNG GIAN LÀM VIỆC</li>
          <li class="nav-item">
            <a class="nav-link" href="${ctx}/" aria-label="Quay về trang chủ Guest">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-globe2"></i></span>
              <span class="nav-link-title">Trang chủ</span>
            </a>
          </li>
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
              <span class="nav-link-title">Tư vấn trực tuyến</span>
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
          <li class="qa-workspace-nav-label">TRI THỨC & CÁ NHÂN</li>
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
      <div class="qa-workspace-sidebar-footer"><span class="qa-workspace-role-dot"></span>Không gian tư vấn viên<a href="${ctx}/guide" aria-label="Hướng dẫn sử dụng"><i class="bi bi-question-circle"></i></a></div>
    </div>
  </aside>

  <header class="navbar navbar-expand-md d-print-none qa-workspace-topbar">
    <div class="container-xl">
      <button class="btn qa-sidebar-toggle" type="button" data-workspace-toggle
              aria-controls="workspace-sidebar" aria-expanded="true" aria-label="Thu gọn hoặc mở menu">
        <i class="bi bi-list"></i>
      </button>
      <a href="${ctx}/manager/dashboard" class="qa-topbar-brand d-lg-none">
        <img src="${ctx}/assets/images/hcmute-logo.png" alt="Logo HCMUTE">
        <strong>QA<span>UTE</span></strong>
      </a>
      <div class="qa-workspace-location"><small>KHÔNG GIAN TƯ VẤN</small><strong><c:out value="${workspacePageTitle}"/></strong></div>
      <div class="navbar-nav flex-row ms-auto align-items-center gap-3">

        <c:if test="${not empty mgrStatus}">
          <div class="dropdown qa-workspace-presence">
            <button class="btn qa-presence-toggle dropdown-toggle" data-bs-toggle="dropdown" type="button" aria-expanded="false"><span class="qa-presence-dot qa-presence-dot--${mgrStatus}"></span><c:choose><c:when test="${mgrStatus == 'ONLINE'}">Trực tuyến</c:when><c:when test="${mgrStatus == 'BUSY'}">Đang bận</c:when><c:otherwise>Ngoại tuyến</c:otherwise></c:choose></button>
            <form method="post" action="${ctx}/manager/profile/status" class="dropdown-menu dropdown-menu-end p-2">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="redirect" value="<c:out value='${currentPath}'/>"/>
            <div class="d-grid gap-1" role="group" aria-label="Trạng thái trực">
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
          </div>
        </c:if>

        <div class="nav-item dropdown">
          <a href="#" class="nav-link d-flex lh-1 text-reset p-0" data-bs-toggle="dropdown" aria-label="Mở menu người dùng">
            <span class="qa-workspace-avatar qa-workspace-avatar--small"><c:out value="${workspaceInitial}"/></span>
            <div class="d-none d-xl-block ps-2">
              <div><c:out value="${workspaceName}"/></div>
              <div class="mt-1 small text-secondary">Tư vấn viên</div>
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

  <main class="page-wrapper" id="workspace-main" tabindex="-1">
    <div class="page-body">
      <div class="container-xl qa-workspace-container">
        <qa:alert/>
        <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
      </div>
    </div>
    <footer class="footer footer-transparent d-print-none">
      <div class="container-xl qa-workspace-footer"><span>&copy; QAUTE · Cổng hỗ trợ sinh viên</span><a href="${ctx}/guide">Hướng dẫn sử dụng <i class="bi bi-arrow-up-right"></i></a></div>
    </footer>
  </main>
</div>

<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/assets/js/tv2-layout.js" defer></script>
</body>
</html>
