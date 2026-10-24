<%@ page pageEncoding="UTF-8" %>
    <%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
        <c:set var="ctx" value="${pageContext.request.contextPath}" />
        <title>
            <c:out value="${announcement.title()}" />
        </title>
<style>
.qaute-announcement-detail { --qad-ink: #122448; --qad-muted: #687894; --qad-blue: #315ee8; --qad-teal: #08a99a; --qad-line: rgba(87, 112, 160, .16); --qad-glass: rgba(255, 255, 255, .82); position: relative; isolation: isolate; color: var(--qad-ink); padding-bottom: clamp(2rem, 5vw, 4.5rem); }
.qaute-announcement-detail::before { position: absolute; content: ""; z-index: -1; top: -2rem; left: 50%; width: min(100vw, 1500px); height: 650px; transform: translateX(-50%); pointer-events: none; background: radial-gradient(ellipse at 13% 9%, rgba(64, 106, 242, .12), transparent 34%), radial-gradient(ellipse at 87% 20%, rgba(16, 190, 174, .12), transparent 31%), linear-gradient(180deg, rgba(246, 249, 255, .96), rgba(255, 255, 255, 0)); }
.qaute-announcement-detail a { transition: color .25s ease, background-color .25s ease, border-color .25s ease, box-shadow .25s ease, transform .25s ease; }
.qaute-announcement-detail .qad-breadcrumb { display: flex; flex-wrap: wrap; align-items: center; gap: .35rem; padding: .7rem 1rem; margin: 0 0 1.25rem; width: fit-content; max-width: 100%; list-style: none; border: 1px solid rgba(255, 255, 255, .9); border-radius: 999px; background: rgba(255, 255, 255, .72); box-shadow: 0 8px 25px rgba(34, 61, 111, .055); backdrop-filter: blur(14px); }
.qaute-announcement-detail .breadcrumb-item, .qaute-announcement-detail .breadcrumb-item a { color: #71809b; font-size: .84rem; font-weight: 650; text-decoration: none; }
.qaute-announcement-detail .breadcrumb-item a:hover { color: var(--qad-blue); }
.qaute-announcement-detail .breadcrumb-item.active { color: var(--qad-blue); }
.qaute-announcement-detail .breadcrumb-item+.breadcrumb-item::before { color: #a5b1c7; }
.qaute-announcement-detail .qad-layout { align-items: flex-start; }
.qaute-announcement-detail .qad-main-card { position: relative; overflow: hidden; border: 1px solid rgba(255, 255, 255, .92); border-radius: 28px; background: rgba(255, 255, 255, .94); box-shadow: 0 22px 65px rgba(30, 54, 100, .09), 0 3px 12px rgba(30, 54, 100, .035); animation: qaute-rise-in .7s cubic-bezier(.2, .75, .25, 1) both; }
.qaute-announcement-detail .qad-main-card::before { position: absolute; top: 0; left: 0; right: 0; height: 5px; content: ""; background: linear-gradient(90deg, #325de8 0%, #6f70f1 38%, #14c6b4 100%); }
.qaute-announcement-detail .qad-article-head { position: relative; padding: clamp(1.4rem, 3.5vw, 2.8rem) clamp(1.25rem, 3.5vw, 2.75rem) 1.25rem; }
.qaute-announcement-detail .qad-eyebrow { display: inline-flex; align-items: center; gap: .55rem; margin-bottom: 1rem; color: var(--qad-blue); font-size: .75rem; font-weight: 850; letter-spacing: .15em; text-transform: uppercase; }
.qaute-announcement-detail .qad-eyebrow::before { width: 24px; height: 2px; border-radius: 9px; background: linear-gradient(90deg, var(--qad-blue), var(--qad-teal)); content: ""; }
.qaute-announcement-detail .qad-pinned { display: inline-flex; align-items: center; gap: .5rem; padding: .55rem .85rem; border: 1px solid rgba(230, 67, 88, .18); border-radius: 999px; color: #bd2945; background: linear-gradient(135deg, #fff0f3, #fff7f7); box-shadow: 0 6px 18px rgba(202, 50, 75, .08); font-size: .78rem; font-weight: 800; animation: qad-soft-pulse 3.2s ease-in-out infinite; }
.qaute-announcement-detail .qad-title { max-width: 930px; margin: 0; color: #14294d; font-size: clamp(1.85rem, 3.7vw, 3.35rem); font-weight: 850; letter-spacing: -.045em; line-height: 1.15; text-wrap: pretty; overflow-wrap: anywhere; }
.qaute-announcement-detail .qad-title-rule { width: min(150px, 35%); height: 5px; margin: 1.45rem 0 1.25rem; border-radius: 8px; background: linear-gradient(90deg, #315ee8, #7d79f3 58%, #20c8b1); }
.qaute-announcement-detail .qad-meta { display: flex; flex-wrap: wrap; align-items: center; gap: .65rem; color: var(--qad-muted); font-size: .86rem; font-weight: 600; }
.qaute-announcement-detail .qad-meta-item { display: inline-flex; align-items: center; gap: .55rem; padding: .58rem .8rem; border: 1px solid var(--qad-line); border-radius: 12px; background: #f8faff; }
.qaute-announcement-detail .qad-meta-item i { color: var(--qad-blue); font-size: 1rem; }
.qaute-announcement-detail .qad-cover-wrap { position: relative; overflow: hidden; margin: .25rem clamp(1.25rem, 3.5vw, 2.75rem) 1.6rem; border-radius: 21px; background: #eaf0ff; box-shadow: 0 14px 35px rgba(24, 46, 92, .14); }
.qaute-announcement-detail .qad-cover-wrap::after { position: absolute; inset: 0; border: 1px solid rgba(255, 255, 255, .3); border-radius: inherit; content: ""; pointer-events: none; }
.qaute-announcement-detail .qad-cover { display: block; width: 100%; max-height: 480px; object-fit: cover; transition: transform .8s cubic-bezier(.2, .7, .2, 1), filter .6s ease; }
.qaute-announcement-detail .qad-cover-wrap:hover .qad-cover { transform: scale(1.025); filter: saturate(1.07); }
.qaute-announcement-detail .qad-summary { position: relative; display: flex; gap: 1rem; margin: 0 clamp(1.25rem, 3.5vw, 2.75rem) 1.5rem; padding: 1.15rem 1.25rem; border: 1px solid rgba(49, 94, 232, .13); border-radius: 16px; background: linear-gradient(120deg, rgba(238, 243, 255, .95), rgba(239, 253, 250, .8)); color: #4c5d79; font-size: 1.05rem; line-height: 1.8; }
.qaute-announcement-detail .qad-summary-icon { display: grid; flex: 0 0 42px; width: 42px; height: 42px; place-items: center; border-radius: 13px; color: #fff; background: linear-gradient(145deg, #315ee8, #5f7af0); box-shadow: 0 7px 18px rgba(49, 94, 232, .22); font-size: 1.1rem; }
.qaute-announcement-detail .qad-content { padding: .5rem clamp(1.25rem, 3.5vw, 2.75rem) clamp(1.8rem, 4vw, 3rem); color: #344563; font-size: 1.025rem; line-height: 1.95; overflow-wrap: anywhere; }
.qaute-announcement-detail .qad-content> :first-child { margin-top: 0; }
.qaute-announcement-detail .qad-content h1, .qaute-announcement-detail .qad-content h2, .qaute-announcement-detail .qad-content h3, .qaute-announcement-detail .qad-content h4, .qaute-announcement-detail .qad-content h5, .qaute-announcement-detail .qad-content h6 { margin: 1.9em 0 .65em; color: #172d53; font-weight: 800; letter-spacing: -.025em; line-height: 1.35; }
.qaute-announcement-detail .qad-content h2 { padding-left: .85rem; border-left: 4px solid #3e67ea; }
.qaute-announcement-detail .qad-content h3 { color: #234c9b; }
.qaute-announcement-detail .qad-content p { margin-bottom: 1.15em; }
.qaute-announcement-detail .qad-content a { color: #315ee8; font-weight: 650; text-decoration-thickness: 1px; text-underline-offset: 3px; }
.qaute-announcement-detail .qad-content a:hover { color: #087e82; }
.qaute-announcement-detail .qad-content ul, .qaute-announcement-detail .qad-content ol { padding-left: 1.5rem; margin: 0 0 1.25rem; }
.qaute-announcement-detail .qad-content li { padding-left: .25rem; margin: .35rem 0; }
.qaute-announcement-detail .qad-content li::marker { color: #3f67e8; font-weight: 800; }
.qaute-announcement-detail .qad-content blockquote { margin: 1.5rem 0; padding: 1rem 1.25rem; border-left: 4px solid #15b7aa; border-radius: 0 14px 14px 0; background: #f0fbf9; color: #4a6075; }
.qaute-announcement-detail .qad-content img { max-width: 100%; height: auto; border-radius: 15px; box-shadow: 0 12px 30px rgba(29, 50, 89, .12); }
.qaute-announcement-detail .qad-content table { width: 100%; margin: 1.4rem 0; border-collapse: separate; border-spacing: 0; overflow: hidden; border: 1px solid var(--qad-line); border-radius: 14px; }
.qaute-announcement-detail .qad-content th { color: #1b3767; background: #eff4ff; font-weight: 800; }
.qaute-announcement-detail .qad-content th, .qaute-announcement-detail .qad-content td { padding: .75rem .9rem; border-right: 1px solid var(--qad-line); border-bottom: 1px solid var(--qad-line); }
.qaute-announcement-detail .qad-content tr:last-child td { border-bottom: 0; }
.qaute-announcement-detail .qad-content pre { overflow: auto; padding: 1.1rem 1.2rem; border: 1px solid #273957; border-radius: 15px; background: #14213a; color: #ecf3ff; font-size: .9rem; line-height: 1.7; }
.qaute-announcement-detail .qad-content code { padding: .15em .4em; border-radius: 5px; background: #eef2fa; color: #b12d68; font-size: .9em; }
.qaute-announcement-detail .qad-content pre code { padding: 0; background: transparent; color: inherit; }
.qaute-announcement-detail .qad-sidebar { position: sticky; top: 1.25rem; }
.qaute-announcement-detail .qad-side-card { position: relative; overflow: hidden; margin-bottom: 1.1rem; border: 1px solid rgba(255, 255, 255, .92); border-radius: 23px; background: var(--qad-glass); box-shadow: 0 15px 42px rgba(30, 54, 100, .075); backdrop-filter: blur(16px); animation: qaute-rise-in .7s cubic-bezier(.2, .75, .25, 1) both; }
.qaute-announcement-detail .qad-side-card:nth-child(2) { animation-delay: .1s; }
.qaute-announcement-detail .qad-side-head { display: flex; align-items: center; gap: .85rem; padding: 1.25rem 1.25rem 1rem; border-bottom: 1px solid rgba(87, 112, 160, .1); }
.qaute-announcement-detail .qad-side-icon { display: grid; flex: 0 0 44px; width: 44px; height: 44px; place-items: center; border-radius: 14px; color: #fff; background: linear-gradient(145deg, #315ee8, #697af0); box-shadow: 0 8px 20px rgba(49, 94, 232, .22); font-size: 1.15rem; }
.qaute-announcement-detail .qad-side-icon.qad-teal { background: linear-gradient(145deg, #079d91, #26cbbb); box-shadow: 0 8px 20px rgba(8, 169, 154, .2); }
.qaute-announcement-detail .qad-side-title { margin: 0; color: #1b3157; font-size: 1.08rem; font-weight: 850; letter-spacing: -.025em; }
.qaute-announcement-detail .qad-side-subtitle { margin: .2rem 0 0; color: #7b8aa3; font-size: .77rem; }
.qaute-announcement-detail .qad-side-body { display: flex; flex-direction: column; gap: .65rem; padding: 1.1rem 1.25rem 1.3rem; }
.qaute-announcement-detail .qad-nav-link { display: flex; align-items: center; justify-content: space-between; gap: .75rem; min-height: 48px; padding: .75rem .85rem; border: 1px solid rgba(90, 116, 167, .14); border-radius: 13px; color: #425575; background: rgba(249, 251, 255, .8); font-size: .88rem; font-weight: 700; text-decoration: none; }
.qaute-announcement-detail .qad-nav-link .qad-nav-label { display: flex; align-items: center; gap: .7rem; }
.qaute-announcement-detail .qad-nav-link .qad-nav-label i { color: var(--qad-blue); font-size: 1rem; }
.qaute-announcement-detail .qad-nav-link .qad-arrow { color: #9aa8c0; transition: transform .25s ease, color .25s ease; }
.qaute-announcement-detail .qad-nav-link:hover { transform: translateY(-2px); border-color: rgba(49, 94, 232, .3); color: #254fc9; background: #fff; box-shadow: 0 9px 20px rgba(49, 94, 232, .09); }
.qaute-announcement-detail .qad-nav-link:hover .qad-arrow { transform: translateX(3px); color: var(--qad-blue); }
.qaute-announcement-detail .qad-nav-link.qad-primary { color: #fff; border-color: transparent; background: linear-gradient(110deg, #315ee8, #5278f2); box-shadow: 0 9px 23px rgba(49, 94, 232, .2); }
.qaute-announcement-detail .qad-nav-link.qad-primary .qad-nav-label i, .qaute-announcement-detail .qad-nav-link.qad-primary .qad-arrow { color: rgba(255, 255, 255, .85); }
.qaute-announcement-detail .qad-nav-link.qad-primary:hover { color: #fff; background: linear-gradient(110deg, #264fcf, #3f66e8); box-shadow: 0 12px 26px rgba(49, 94, 232, .28); }
.qaute-announcement-detail .qad-nav-link.qad-teal-link .qad-nav-label i { color: #079d91; }
.qaute-announcement-detail .qad-help-copy { margin: 0; color: #697995; font-size: .87rem; line-height: 1.8; }
.qaute-announcement-detail .qad-help-note { display: flex; align-items: flex-start; gap: .7rem; margin-top: .25rem; padding: .85rem; border: 1px solid rgba(8, 169, 154, .14); border-radius: 14px; color: #57716f; background: linear-gradient(125deg, #f0fbf9, #f7fcff); font-size: .8rem; line-height: 1.65; }
.qaute-announcement-detail .qad-help-note i { margin-top: .15rem; color: #069d91; font-size: 1rem; }
@keyframes qad-soft-pulse {
  0%, 100% { box-shadow: 0 6px 18px rgba(202, 50, 75, .07); }
  50% { box-shadow: 0 7px 24px rgba(202, 50, 75, .15); }
}
@media (max-width: 991.98px) {
  .qaute-announcement-detail .qad-sidebar { position: static; }
  .qaute-announcement-detail .qad-title { font-size: clamp(1.8rem, 5vw, 2.6rem); }
}
@media (max-width: 575.98px) {
  .qaute-announcement-detail .qad-breadcrumb { border-radius: 14px; padding: .65rem .8rem; }
  .qaute-announcement-detail .qad-main-card { border-radius: 21px; }
  .qaute-announcement-detail .qad-article-head { padding-top: 1.5rem; }
  .qaute-announcement-detail .qad-meta { gap: .45rem; }
  .qaute-announcement-detail .qad-meta-item { width: 100%; }
  .qaute-announcement-detail .qad-cover-wrap { border-radius: 14px; }
  .qaute-announcement-detail .qad-summary { gap: .7rem; padding: .9rem; font-size: .96rem; }
  .qaute-announcement-detail .qad-summary-icon { flex-basis: 36px; width: 36px; height: 36px; }
  .qaute-announcement-detail .qad-side-card { border-radius: 19px; }
  .qaute-announcement-detail .qad-content { font-size: .98rem; }
}
@media (prefers-reduced-motion: reduce) {
  .qaute-announcement-detail *, .qaute-announcement-detail *::before, .qaute-announcement-detail *::after { animation-duration: .01ms !important; animation-iteration-count: 1 !important; scroll-behavior: auto !important; transition-duration: .01ms !important; }
}
</style>




        <div class="qaute-announcement-detail">
            <nav aria-label="breadcrumb" class="mb-3">
                <ol class="breadcrumb qad-breadcrumb">
                    <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Thông báo</li>
                </ol>
            </nav>

            <div class="row g-4 qad-layout">
                <div class="col-lg-8">
                    <article class="qad-main-card mb-4">
                        <header class="qad-article-head">
                            <div class="qad-eyebrow"><i class="bi bi-broadcast-pin"></i> Cổng thông tin QAUTE</div>
                            <c:if test="${announcement.pinned()}">
                                <div class="mb-3">
                                    <span class="qad-pinned"><i class="bi bi-pin-angle-fill"></i> Thông báo quan trọng
                                        (Đã ghim)</span>
                                </div>
                            </c:if>
                            <h1 class="qad-title">
                                <c:out value="${announcement.title()}" />
                            </h1>
                            <div class="qad-title-rule"></div>
                            <c:if test="${not empty announcement.publishAt()}">
                                <div class="qad-meta">
                                    <span class="qad-meta-item"><i class="bi bi-calendar2-check"></i>Ngày đăng:
                                        ${announcement.publishAt().dayOfMonth}/${announcement.publishAt().monthValue}/${announcement.publishAt().year}</span>
                                    <span class="qad-meta-item"><i class="bi bi-shield-check"></i>Thông tin chính
                                        thức</span>
                                </div>
                            </c:if>
                        </header>

                        <c:if test="${not empty announcement.coverUrl()}">
                            <div class="qad-cover-wrap">
                                <img src="<c:out value='${announcement.coverUrl()}'/>"
                                    alt="<c:out value='${announcement.title()}'/>" class="qad-cover">
                            </div>
                        </c:if>

                        <c:if test="${not empty announcement.summary()}">
                            <div class="qad-summary">
                                <span class="qad-summary-icon"><i class="bi bi-stars"></i></span>
                                <div>
                                    <c:out value="${announcement.summary()}" />
                                </div>
                            </div>
                        </c:if>

                        <div class="qad-content announcement-content markdown">
                            <c:out value="${sanitizedBodyHtml}" escapeXml="false" />
                        </div>
                    </article>
                </div>

                <aside class="col-lg-4 qad-sidebar">
                    <section class="qad-side-card">
                        <div class="qad-side-head">
                            <span class="qad-side-icon"><i class="bi bi-compass"></i></span>
                            <div>
                                <h2 class="qad-side-title">Điều hướng</h2>
                                <p class="qad-side-subtitle">Tiếp tục khám phá QAUTE</p>
                            </div>
                        </div>
                        <div class="qad-side-body">
                            <a href="/" class="qad-nav-link">
                                <span class="qad-nav-label"><i class="bi bi-house-door"></i>Về trang chủ</span>
                                <i class="bi bi-arrow-up-right qad-arrow"></i>
                            </a>
                            <sec:authorize access="hasRole('STUDENT')">
                                <a href="/student/dashboard" class="qad-nav-link qad-primary">
                                    <span class="qad-nav-label"><i class="bi bi-speedometer2"></i>Về bảng điều khiển
                                        sinh viên</span>
                                    <i class="bi bi-arrow-right qad-arrow"></i>
                                </a>
                            </sec:authorize>
                        </div>
                    </section>

                    <section class="qad-side-card">
                        <div class="qad-side-head">
                            <span class="qad-side-icon qad-teal"><i class="bi bi-headset"></i></span>
                            <div>
                                <h2 class="qad-side-title">Cần trợ giúp thêm?</h2>
                                <p class="qad-side-subtitle">Luôn có kênh hỗ trợ dành cho bạn</p>
                            </div>
                        </div>
                        <div class="qad-side-body">
                            <p class="qad-help-copy">Nếu bạn có câu hỏi liên quan đến thông báo này hoặc cần được tư vấn
                                chi tiết, vui lòng kết nối với chúng tôi.</p>
                            <a href="/faq" class="qad-nav-link">
                                <span class="qad-nav-label"><i class="bi bi-question-circle"></i>Tra cứu câu hỏi thường
                                    gặp (FAQ)</span>
                                <i class="bi bi-chevron-right qad-arrow"></i>
                            </a>
                            <a href="/student/chat" class="qad-nav-link qad-teal-link">
                                <span class="qad-nav-label"><i class="bi bi-chat-dots"></i>Chat trực tuyến với tư vấn
                                    viên</span>
                                <i class="bi bi-chevron-right qad-arrow"></i>
                            </a>
                            <a href="/student/tickets/new" class="qad-nav-link">
                                <span class="qad-nav-label"><i class="bi bi-pencil-square"></i>Gửi yêu cầu hỗ trợ
                                    mới</span>
                                <i class="bi bi-chevron-right qad-arrow"></i>
                            </a>
                            <div class="qad-help-note"><i class="bi bi-lightbulb-fill"></i><span>Hãy chọn kênh phù hợp
                                    để tìm câu trả lời hoặc kết nối với đội ngũ tư vấn.</span></div>
                        </div>
                    </section>
                </aside>
            </div>
        </div>