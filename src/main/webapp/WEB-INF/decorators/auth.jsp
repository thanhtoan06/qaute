<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page import="org.sitemesh.content.Content" %>
<%
    Content pageContent = (Content) request.getAttribute("org.sitemesh.content.Content");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title><%= pageContent.getExtractedProperties().getChild("title").getValue() %> - QAUTE</title>
    <%= pageContent.getExtractedProperties().getChild("head").getValue() %>
</head>
<body>
<div class="page page-center">
    <div class="container container-tight py-4">
        <div class="text-center mb-4">
            <a class="navbar-brand navbar-brand-autodark" href="/">QAUTE</a>
        </div>
        <div class="card card-md">
            <div class="card-body">
                <qa:alert/>
                <%= pageContent.getExtractedProperties().getChild("body").getValue() %>
            </div>
        </div>
        <div class="text-center text-secondary mt-3">
            <a href="/">← Về trang chủ</a>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/scripts.jspf" %>
</body>
</html>