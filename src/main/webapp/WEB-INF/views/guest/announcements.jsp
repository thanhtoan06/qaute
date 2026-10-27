<%@ page pageEncoding="UTF-8" %>
  <%@ include file="/WEB-INF/views/common/taglibs.jspf" %>

<head>
    <title>Thông báo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/guest/announcements.css">
</head>



    <div class="qaute-announcements">
      <nav aria-label="breadcrumb" class="qa-announcement-breadcrumb">
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
          <li class="breadcrumb-item active" aria-current="page">Thông báo</li>
        </ol>
      </nav>
      <%-- Giới thiệu bảng tin thông báo mới nhất dành cho sinh viên. --%>
      <header class="qa-announcement-hero">
        <div class="qa-hero-copy">
          <div class="qa-eyebrow"><i class="bi bi-broadcast-pin"></i> QAUTE • CAMPUS UPDATES</div>
          <h1 class="qa-hero-title">Thông báo <span>mới nhất</span></h1>
          <p class="qa-hero-description">Cập nhật những thông tin quan trọng, tin tức mới và thông báo dành cho cộng
            đồng sinh viên. Theo dõi thường xuyên để không bỏ lỡ điều cần biết.</p>
        </div>
        <div class="qa-hero-art" aria-hidden="true"><i class="bi bi-megaphone-fill"></i></div>
      </header>
      <%-- Liệt kê thông báo đã công bố và liên kết xem nội dung chi tiết. --%>
      <section aria-labelledby="qa-announcement-list-title">
        <div class="qa-section-heading">
          <div>
            <span class="qa-section-kicker">INFORMATION CENTER</span>
            <h2 class="qa-section-title" id="qa-announcement-list-title">Bảng tin dành cho bạn</h2>
          </div>
          <div class="qa-section-mark"><i class="bi bi-shield-check"></i> Thông tin từ QAUTE</div>
        </div>

        <%-- Ô tìm kiếm thông báo --%>
        <div class="mb-4">
          <form action="/announcements" method="get" class="input-group">
            <input type="search" name="q" class="form-control" placeholder="Tìm kiếm thông báo..." value="${searchQuery}" aria-label="Tìm kiếm thông báo">
            <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i></button>
            <c:if test="${isSearch}">
              <a href="/announcements" class="btn btn-outline-secondary">Xóa tìm kiếm</a>
            </c:if>
          </form>
        </div>

        <c:if test="${isSearch && empty announcements}">
          <div class="alert alert-info">
            <i class="bi bi-info-circle"></i> Không tìm thấy thông báo nào phù hợp với từ khóa "${searchQuery}".
          </div>
        </c:if>

        <div class="row g-3 g-xl-4">
          <c:forEach var="a" items="${announcements}">
            <div class="col-12">
              <article class="qa-announcement-card">
                <div class="card-body">
                  <div class="qa-card-topline">
                    <div class="qa-announcement-icon"><i class="bi bi-file-earmark-richtext"></i></div>
                    <c:choose>
                      <c:when test="${a.pinned()}">
                        <span class="qa-announcement-label qa-announcement-pinned"><i class="bi bi-pin-angle-fill"></i> ĐÃ GHIM</span>
                      </c:when>
                      <c:otherwise>
                        <span class="qa-announcement-label"><i class="bi bi-check-circle-fill"></i> THÔNG BÁO</span>
                      </c:otherwise>
                    </c:choose>
                  </div>
                  <h3 class="qa-announcement-title"><a href="/announcements/${a.id()}" class="text-decoration-none">
                      <c:out value="${a.title()}" />
                    </a></h3>
                  <p class="qa-announcement-summary">
                    <c:out value="${a.summary()}" />
                  </p>
                  <div class="qa-card-footer">
                    <span class="qa-announcement-date"><i class="bi bi-calendar3"></i><time datetime="<c:out value='${a.publishAt()}'/>" data-qaute-datetime><c:out value="${a.publishAt()}"/></time></span>
                    <a href="/announcements/${a.id()}" class="qa-read-more text-decoration-none">Đọc thông báo <i
                        class="bi bi-arrow-right"></i></a>
                  </div>
                </div>
              </article>
            </div>
          </c:forEach>
        </div>
      </section>
      <%-- Cho phép chuyển trang khi danh sách thông báo có nhiều trang. --%>
      <c:if test="${page.totalPages > 1}">
        <div class="mt-4 pt-2">
          <div class="qa-guest-pagination qa-announcement-pagination">
            <qa:pagination page="${page}" baseUrl="/announcements" />
          </div>
          <div class="qa-pagination-caption">Cổng thông tin và thông báo sinh viên QAUTE</div>
        </div>
      </c:if>
    </div>