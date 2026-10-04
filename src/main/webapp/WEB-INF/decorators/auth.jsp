<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ taglib prefix="sitemesh" uri="http://www.sitemesh.org/sitemesh-3.0" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ include file="/WEB-INF/views/common/head-assets.jspf" %>
    <title><sitemesh:write property="title"/> - QAUTE</title>
    <sitemesh:write property="head"/>
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
                <sitemesh:write property="body"/>
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
