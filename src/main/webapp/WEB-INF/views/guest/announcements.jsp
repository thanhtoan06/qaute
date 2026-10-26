<%@ page pageEncoding="UTF-8" %>
  <%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
    <title>Thông báo</title>
<style>
.qaute-announcements { --qa-ink: #14213d; --qa-muted: #65748b; --qa-blue: #3458db; --qa-teal: #15b8a6; position: relative; isolation: isolate; padding: 0 0 3rem; color: var(--qa-ink) }
.qaute-announcements .qa-announcement-breadcrumb { margin-bottom: 1.25rem; font-size: .88rem }
.qaute-announcements .qa-announcement-breadcrumb .breadcrumb { margin-bottom: 0; flex-wrap: wrap }
.qaute-announcements .qa-announcement-breadcrumb a { color: #70809a; text-decoration: none; transition: color .2s ease }
.qaute-announcements .qa-announcement-breadcrumb a:hover { color: var(--qa-blue) }
.qaute-announcements .qa-announcement-breadcrumb .breadcrumb-item.active { color: #243651; font-weight: 600 }
.qaute-announcements:before { content: ""; position: absolute; z-index: -1; top: 3.5rem; left: 0; width: 100%; height: 28rem; background: radial-gradient(ellipse at 12% 8%, rgba(85, 132, 255, .17), transparent 35%), radial-gradient(ellipse at 92% 20%, rgba(21, 184, 166, .14), transparent 33%), linear-gradient(180deg, #f4f7ff 0%, rgba(247, 250, 255, .7) 66%, transparent 100%); pointer-events: none }
.qaute-announcements .qa-announcement-hero { position: relative; display: flex; align-items: flex-start; height: 400px; min-height: 400px; box-sizing: border-box; justify-content: space-between; gap: var(--guest-hero-gap); padding: var(--guest-hero-padding); margin-bottom: 2rem; border: 1px solid rgba(255, 255, 255, .7); background: linear-gradient(125deg, #101f49 0%, #2346a2 54%, #167f9d 100%); box-shadow: 0 22px 55px rgba(29, 55, 120, .2); }
.qaute-announcements .qa-announcement-hero:before, .qaute-announcements .qa-announcement-hero:after { content: ""; position: absolute; border: 1px solid rgba(255, 255, 255, .15); border-radius: 50%; pointer-events: none }
.qaute-announcements .qa-announcement-hero:before { width: 24rem; height: 24rem; right: -5rem; top: -13rem; box-shadow: 0 0 0 22px rgba(255, 255, 255, .025), 0 0 0 48px rgba(255, 255, 255, .025), 0 0 0 76px rgba(255, 255, 255, .02); animation: qaute-orbit 22s linear infinite }
.qaute-announcements .qa-announcement-hero:after { width: 13rem; height: 13rem; right: 12%; bottom: -10rem; background: rgba(255, 255, 255, .04) }
.qaute-announcements .qa-hero-copy { position: relative; z-index: 1; max-width: 770px }
.qaute-announcements .qa-eyebrow { display: inline-flex; align-items: center; gap: .55rem; padding: .55rem .9rem; margin-bottom: 1.2rem; border: 1px solid rgba(255, 255, 255, .22); border-radius: 999px; background: rgba(255, 255, 255, .09); color: #dfe9ff; font-size: .76rem; font-weight: 850; letter-spacing: .13em; text-transform: uppercase; backdrop-filter: blur(12px) }
.qaute-announcements .qa-eyebrow i { color: #70f1d6; font-size: 1rem }
.qaute-announcements .qa-hero-title { margin: 0 0 1rem; font-size: clamp(2.05rem, 4.2vw, 3.5rem); font-weight: 900; line-height: 1.12; text-wrap: balance }
.qaute-announcements .qa-hero-title span { background: linear-gradient(90deg, #fff 0%, #b8f5ed 100%); -webkit-background-clip: text; background-clip: text; -webkit-text-fill-color: transparent }
.qaute-announcements .qa-hero-description { max-width: 650px; margin: 0; color: rgba(239, 245, 255, .78); font-size: 1rem; line-height: 1.9 }
.qaute-announcements .qa-hero-art { position: relative; z-index: 1; align-self: center; flex: 0 0 var(--guest-hero-art-size); display: grid; place-items: center; width: var(--guest-hero-art-size); height: var(--guest-hero-art-size); border: 1px solid rgba(255, 255, 255, .23); border-radius: 38% 62% 55% 45% / 43% 42% 58% 57%; background: linear-gradient(145deg, rgba(255, 255, 255, .18), rgba(255, 255, 255, .04)); box-shadow: inset 0 1px 0 rgba(255, 255, 255, .2), 0 20px 45px rgba(5, 16, 49, .2); animation: qa-float 6s ease-in-out infinite }
@media (min-width: 768px) and (max-width: 991.98px) {
  .qaute-announcements .qa-hero-art { flex-basis: var(--guest-hero-art-size-tablet); width: var(--guest-hero-art-size-tablet); height: var(--guest-hero-art-size-tablet); }
}
.qaute-announcements .qa-hero-art:before { content: ""; position: absolute; inset: 11px; border: 1px solid rgba(255, 255, 255, .17); border-radius: inherit; transform: rotate(-18deg) }
.qaute-announcements .qa-hero-art i { font-size: var(--guest-hero-icon-size); color: #d5fff8; filter: drop-shadow(0 8px 18px rgba(3, 14, 48, .25)) }
.qaute-announcements .qa-section-heading { display: flex; align-items: end; justify-content: space-between; gap: 1rem; margin: 0 0 1.15rem }
.qaute-announcements .qa-section-kicker { display: block; margin-bottom: .35rem; color: var(--qa-blue); font-size: .72rem; font-weight: 850; letter-spacing: .13em; text-transform: uppercase }
.qaute-announcements .qa-section-title { margin: 0; color: var(--qa-ink); font-size: clamp(1.35rem, 2.4vw, 1.8rem); font-weight: 850; letter-spacing: -.035em }
.qaute-announcements .qa-section-mark { display: inline-flex; align-items: center; gap: .5rem; color: var(--qa-muted); font-size: .88rem; font-weight: 650 }
.qaute-announcements .qa-section-mark i { color: var(--qa-teal); font-size: 1.05rem }
.qaute-announcements .qa-announcement-card { position: relative; height: 100%; overflow: hidden; border: 1px solid #e7ecf5; border-radius: 21px; background: rgba(255, 255, 255, .9); box-shadow: 0 8px 24px rgba(23, 43, 88, .055); transition: transform .32s cubic-bezier(.2, .75, .25, 1), box-shadow .32s ease, border-color .32s ease; animation: qa-card-in .6s both }
.qaute-announcements .row>[class*="col-"]:nth-child(2) .qa-announcement-card { animation-delay: 55ms }
.qaute-announcements .row>[class*="col-"]:nth-child(3) .qa-announcement-card { animation-delay: 110ms }
.qaute-announcements .row>[class*="col-"]:nth-child(4) .qa-announcement-card { animation-delay: 165ms }
.qaute-announcements .row>[class*="col-"]:nth-child(5) .qa-announcement-card { animation-delay: 220ms }
.qaute-announcements .row>[class*="col-"]:nth-child(6) .qa-announcement-card { animation-delay: 275ms }
.qaute-announcements .qa-announcement-card:before { content: ""; position: absolute; top: 0; left: 0; right: 0; height: 3px; background: linear-gradient(90deg, var(--qa-blue), #6989ff 50%, var(--qa-teal)); transform: scaleX(.18); transform-origin: left; transition: transform .35s ease }
.qaute-announcements .qa-announcement-card:after { content: ""; position: absolute; width: 180px; height: 180px; right: -110px; top: -115px; border-radius: 50%; background: radial-gradient(circle, rgba(76, 110, 245, .09), transparent 68%); pointer-events: none; transition: transform .4s ease }
.qaute-announcements .qa-announcement-card:hover { transform: translateY(-6px); border-color: #d2dcff; box-shadow: 0 20px 42px rgba(30, 57, 120, .12) }
.qaute-announcements .qa-announcement-card:hover:before { transform: scaleX(1) }
.qaute-announcements .qa-announcement-card:hover:after { transform: scale(1.5) }
.qaute-announcements .qa-announcement-card .card-body { position: relative; z-index: 1; padding: clamp(1.25rem, 2.6vw, 1.8rem) }
.qaute-announcements .qa-card-topline { display: flex; align-items: center; justify-content: space-between; gap: .75rem; margin-bottom: 1.05rem }
.qaute-announcements .qa-announcement-icon { display: grid; place-items: center; width: 43px; height: 43px; flex: 0 0 43px; border: 1px solid #e0e7ff; border-radius: 14px; background: linear-gradient(145deg, #f2f5ff, #eaf0ff); color: var(--qa-blue); font-size: 1.25rem; transition: transform .3s ease, background .3s ease, color .3s ease }
.qaute-announcements .qa-announcement-card:hover .qa-announcement-icon { transform: rotate(-7deg) scale(1.06); background: linear-gradient(145deg, #3458db, #5074f0); color: #fff }
.qaute-announcements .qa-announcement-label { display: inline-flex; align-items: center; gap: .4rem; padding: .36rem .62rem; border: 1px solid #e3f5f2; border-radius: 999px; background: #effbf9; color: #178b82; font-size: .7rem; font-weight: 800; letter-spacing: .04em }
.qaute-announcements .qa-announcement-label i { font-size: .8rem }
.qaute-announcements .qa-announcement-title { margin: 0 0 .7rem; font-size: clamp(1.08rem, 1.7vw, 1.3rem); font-weight: 800; line-height: 1.45; letter-spacing: -.02em }
.qaute-announcements .qa-announcement-title a { color: #172748; background-image: linear-gradient(currentColor, currentColor); background-size: 0 1px; background-repeat: no-repeat; background-position: 0 100%; transition: color .25s ease, background-size .3s ease }
.qaute-announcements .qa-announcement-title a:hover { color: var(--qa-blue); background-size: 100% 1px }
.qaute-announcements .qa-announcement-summary { display: -webkit-box; overflow: hidden; -webkit-line-clamp: 3; -webkit-box-orient: vertical; margin: 0; color: #6b7890; font-size: .94rem; line-height: 1.8 }
.qaute-announcements .qa-card-footer { display: flex; align-items: center; justify-content: space-between; gap: .75rem; margin-top: 1.25rem; padding-top: 1rem; border-top: 1px solid #edf0f7 }
.qaute-announcements .qa-announcement-date { display: inline-flex; align-items: center; gap: .5rem; color: #7a879b; font-size: .8rem; font-weight: 650 }
.qaute-announcements .qa-announcement-date i { display: grid; place-items: center; width: 29px; height: 29px; border-radius: 10px; background: #f2f5fb; color: #5268a8; font-size: .9rem }
.qaute-announcements .qa-read-more { display: inline-flex; align-items: center; gap: .38rem; color: var(--qa-blue); font-size: .84rem; font-weight: 800; white-space: nowrap; transition: gap .25s ease }
.qaute-announcements .qa-announcement-card:hover .qa-read-more { gap: .7rem }
.qaute-announcements .qa-pagination-caption { margin-top: .85rem; color: #8792a7; text-align: center; font-size: .78rem }
@keyframes qa-float {
  0%, 100% { transform: translateY(0) rotate(5deg) }
  50% { transform: translateY(-9px) rotate(2deg) }
}
@media (max-width:767.98px) {
  .qaute-announcements .qa-announcement-hero { height: 440px; min-height: 440px; align-items: flex-start; padding: 1.5rem 1.25rem; border-radius: var(--guest-hero-radius-mobile) }
  .qaute-announcements .qa-hero-art { display: none }
  .qaute-announcements .qa-section-heading { align-items: flex-start; flex-direction: column; gap: .45rem }
  .qaute-announcements .qa-announcement-card .card-body { padding: 1.2rem }
}
@media (max-width:420px) {
  .qaute-announcements .qa-announcement-hero { height: 440px; min-height: 440px; padding: 1.5rem 1.25rem }
  .qaute-announcements .qa-card-footer { align-items: flex-start; flex-direction: column }
  .qaute-announcements .qa-read-more { align-self: flex-end }
}
@media (prefers-reduced-motion:reduce) {
  .qaute-announcements *, .qaute-announcements *:before, .qaute-announcements *:after { animation-duration: .01ms !important; animation-iteration-count: 1 !important; scroll-behavior: auto !important; transition-duration: .01ms !important }
}
</style>


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
                    <span class="qa-announcement-label"><i class="bi bi-check-circle-fill"></i> THÔNG BÁO</span>
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