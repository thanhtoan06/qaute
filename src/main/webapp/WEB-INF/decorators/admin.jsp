<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page import="org.sitemesh.content.Content" %>
<%
    Content pageContent = (Content) request.getAttribute("org.sitemesh.content.Content");
%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<c:set var="rawUri" value="${not empty requestScope['jakarta.servlet.forward.request_uri'] ? requestScope['jakarta.servlet.forward.request_uri'] : pageContext.request.requestURI}"/>
<c:set var="currentPath" value="${fn:startsWith(rawUri, ctx) ? fn:substring(rawUri, fn:length(ctx), fn:length(rawUri)) : rawUri}"/>

<c:set var="menuPaths" value="/admin/dashboard,/admin/users,/admin/users/import,/admin/departments,/admin/categories,/admin/managers,/admin/faq,/admin/faq/tags,/admin/faq/synonyms,/admin/tickets,/admin/sla,/admin/settings,/admin/announcements,/admin/audit-logs,/admin/reports,/admin/health"/>
<c:set var="activePath" value=""/>
<c:forTokens items="${menuPaths}" delims="," var="menuPath">
  <c:if test="${currentPath == menuPath or fn:startsWith(currentPath, menuPath.concat('/'))}">
    <c:if test="${fn:length(menuPath) gt fn:length(activePath)}">
      <c:set var="activePath" value="${menuPath}"/>
    </c:if>
  </c:if>
</c:forTokens>

<c:set var="grpUsers" value="${activePath == '/admin/users' or activePath == '/admin/users/import'}"/>
<c:set var="grpOrg" value="${activePath == '/admin/departments' or activePath == '/admin/categories' or activePath == '/admin/managers'}"/>
<c:set var="grpKnowledge" value="${activePath == '/admin/faq' or activePath == '/admin/faq/tags' or activePath == '/admin/faq/synonyms'}"/>
<c:set var="grpPolicy" value="${activePath == '/admin/sla' or activePath == '/admin/settings'}"/>
<c:set var="grpMonitor" value="${activePath == '/admin/audit-logs' or activePath == '/admin/reports' or activePath == '/admin/health'}"/>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
  <title><%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE</title>
  <%= pageContent.getExtractedProperties().getChild("head").getValue() %>
</head>
<body>
<div class="page">

  <aside class="navbar navbar-vertical navbar-expand-lg" data-bs-theme="dark">
    <div class="container-fluid">
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#qa-sidebar-menu"
              aria-controls="qa-sidebar-menu" aria-expanded="false" aria-label="Mở menu">
        <span class="navbar-toggler-icon"></span>
      </button>
      <h1 class="navbar-brand navbar-brand-autodark">
        <a href="${ctx}/admin/dashboard" class="text-decoration-none">
          <i class="bi bi-mortarboard-fill me-2"></i>QAUTE <small class="text-secondary">Admin</small>
        </a>
      </h1>
      <div class="collapse navbar-collapse" id="qa-sidebar-menu">
        <ul class="navbar-nav pt-lg-3">

          <li class="nav-item ${activePath == '/admin/dashboard' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/admin/dashboard">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-speedometer2"></i></span>
              <span class="nav-link-title">Tổng quan</span>
            </a>
          </li>

          <li class="nav-item dropdown ${grpUsers ? 'active' : ''}">
            <a class="nav-link dropdown-toggle ${grpUsers ? 'show' : ''}" href="#qa-nav-users" data-bs-toggle="dropdown"
               data-bs-auto-close="false" role="button" aria-expanded="${grpUsers}">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-people"></i></span>
              <span class="nav-link-title">Người dùng</span>
            </a>
            <div class="dropdown-menu ${grpUsers ? 'show' : ''}">
              <a class="dropdown-item ${activePath == '/admin/users' ? 'active' : ''}" href="${ctx}/admin/users">Danh sách người dùng</a>
              <a class="dropdown-item ${activePath == '/admin/users/import' ? 'active' : ''}" href="${ctx}/admin/users/import">Nhập hàng loạt</a>
            </div>
          </li>

          <li class="nav-item dropdown ${grpOrg ? 'active' : ''}">
            <a class="nav-link dropdown-toggle ${grpOrg ? 'show' : ''}" href="#qa-nav-org" data-bs-toggle="dropdown"
               data-bs-auto-close="false" role="button" aria-expanded="${grpOrg}">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-diagram-3"></i></span>
              <span class="nav-link-title">Tổ chức</span>
            </a>
            <div class="dropdown-menu ${grpOrg ? 'show' : ''}">
              <a class="dropdown-item ${activePath == '/admin/departments' ? 'active' : ''}" href="${ctx}/admin/departments">Phòng ban</a>
              <a class="dropdown-item ${activePath == '/admin/categories' ? 'active' : ''}" href="${ctx}/admin/categories">Chuyên mục</a>
              <a class="dropdown-item ${activePath == '/admin/managers' ? 'active' : ''}" href="${ctx}/admin/managers">Manager và phạm vi</a>
            </div>
          </li>

          <li class="nav-item dropdown ${grpKnowledge ? 'active' : ''}">
            <a class="nav-link dropdown-toggle ${grpKnowledge ? 'show' : ''}" href="#qa-nav-knowledge" data-bs-toggle="dropdown"
               data-bs-auto-close="false" role="button" aria-expanded="${grpKnowledge}">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-journal-text"></i></span>
              <span class="nav-link-title">Tri thức</span>
            </a>
            <div class="dropdown-menu ${grpKnowledge ? 'show' : ''}">
              <a class="dropdown-item ${activePath == '/admin/faq' ? 'active' : ''}" href="${ctx}/admin/faq">Bài FAQ</a>
              <a class="dropdown-item ${activePath == '/admin/faq/tags' ? 'active' : ''}" href="${ctx}/admin/faq/tags">Thẻ (tag)</a>
              <a class="dropdown-item ${activePath == '/admin/faq/synonyms' ? 'active' : ''}" href="${ctx}/admin/faq/synonyms">Từ đồng nghĩa</a>
            </div>
          </li>

          <li class="nav-item ${activePath == '/admin/tickets' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/admin/tickets">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-ticket-detailed"></i></span>
              <span class="nav-link-title">Yêu cầu tư vấn</span>
            </a>
          </li>

          <li class="nav-item dropdown ${grpPolicy ? 'active' : ''}">
            <a class="nav-link dropdown-toggle ${grpPolicy ? 'show' : ''}" href="#qa-nav-policy" data-bs-toggle="dropdown"
               data-bs-auto-close="false" role="button" aria-expanded="${grpPolicy}">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-sliders"></i></span>
              <span class="nav-link-title">Chính sách</span>
            </a>
            <div class="dropdown-menu ${grpPolicy ? 'show' : ''}">
              <a class="dropdown-item ${activePath == '/admin/sla' ? 'active' : ''}" href="${ctx}/admin/sla">SLA và giờ làm việc</a>
              <a class="dropdown-item ${activePath == '/admin/settings' ? 'active' : ''}" href="${ctx}/admin/settings">Cấu hình hệ thống</a>
            </div>
          </li>

          <li class="nav-item ${activePath == '/admin/announcements' ? 'active' : ''}">
            <a class="nav-link" href="${ctx}/admin/announcements">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-megaphone"></i></span>
              <span class="nav-link-title">Thông báo chính thức</span>
            </a>
          </li>

          <li class="nav-item dropdown ${grpMonitor ? 'active' : ''}">
            <a class="nav-link dropdown-toggle ${grpMonitor ? 'show' : ''}" href="#qa-nav-monitor" data-bs-toggle="dropdown"
               data-bs-auto-close="false" role="button" aria-expanded="${grpMonitor}">
              <span class="nav-link-icon d-md-none d-lg-inline-block"><i class="bi bi-shield-check"></i></span>
              <span class="nav-link-title">Giám sát</span>
            </a>
            <div class="dropdown-menu ${grpMonitor ? 'show' : ''}">
              <a class="dropdown-item ${activePath == '/admin/audit-logs' ? 'active' : ''}" href="${ctx}/admin/audit-logs">Nhật ký hệ thống</a>
              <a class="dropdown-item ${activePath == '/admin/reports' ? 'active' : ''}" href="${ctx}/admin/reports">Báo cáo</a>
              <a class="dropdown-item ${activePath == '/admin/health' ? 'active' : ''}" href="${ctx}/admin/health">Sức khỏe hệ thống</a>
            </div>
          </li>

        </ul>
      </div>
    </div>
  </aside>

  <header class="navbar navbar-expand-md d-print-none">
    <div class="container-xl">
      <div class="navbar-nav flex-row ms-auto align-items-center gap-3">

        <div class="nav-item dropdown">
          <a href="#" class="nav-link d-flex lh-1 text-reset p-0" data-bs-toggle="dropdown" aria-label="Mở menu người dùng">
            <span class="avatar avatar-sm bg-blue-lt"><i class="bi bi-person-fill"></i></span>
            <div class="d-none d-xl-block ps-2">
              <div><sec:authentication property="principal.fullName"/></div>
              <div class="mt-1 small text-secondary">Quản trị viên</div>
            </div>
          </a>
          <div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
            <form method="post" action="${ctx}/auth/logout" class="m-0">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
              <button type="submit" class="dropdown-item"><i class="bi bi-box-arrow-right me-2"></i>Đăng xuất</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </header>

  <div class="page-wrapper">

    <c:if test="${not empty requestScope.pageTitle}">
      <div class="page-header d-print-none">
        <div class="container-xl">
          <ol class="breadcrumb breadcrumb-arrows" aria-label="breadcrumbs">
            <li class="breadcrumb-item"><a href="${ctx}/admin/dashboard">Quản trị</a></li>
            <li class="breadcrumb-item active" aria-current="page"><c:out value="${requestScope.pageTitle}"/></li>
          </ol>
        </div>
      </div>
    </c:if>

    <div class="page-body">
      <div class="container-xl">
        <qa:alert/>
        <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
      </div>
    </div>
    <footer class="footer footer-transparent d-print-none">
      <div class="container-xl text-center text-secondary small">&copy; QAUTE - Student Support Hub (HCMUTE)</div>
    </footer>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/assets/js/tv2-layout.js" defer></script>
</body>
</html>
