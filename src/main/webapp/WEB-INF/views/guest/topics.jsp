<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chủ đề tư vấn</title>
<style>
.qaute-topics-page { --topics-navy: #12234a; --topics-blue: #3865e9; --topics-teal: #12b8a7; --topics-muted: #738199; --topics-border: rgba(105, 127, 174, .17); --topics-shadow: 0 12px 34px rgba(27, 47, 91, .065); color: #1b2b4b; padding-bottom: 2.5rem; }
.qaute-topics-page .topics-breadcrumb { margin-bottom: 1.25rem; font-size: .88rem; }
.qaute-topics-page .topics-breadcrumb .breadcrumb { margin-bottom: 0; flex-wrap: wrap; }
.qaute-topics-page .topics-breadcrumb a { color: #70809a; text-decoration: none; transition: color .2s ease; }
.qaute-topics-page .topics-breadcrumb a:hover { color: var(--topics-blue); }
.qaute-topics-page .topics-breadcrumb .breadcrumb-item.active { color: #243651; font-weight: 600; }
.qaute-topics-page .topics-hero { position: relative; display: flex; align-items: center; box-sizing: border-box; isolation: isolate; justify-content: space-between; gap: var(--guest-hero-gap); height: 400px; min-height: 400px; padding: var(--guest-hero-padding); margin-bottom: 2rem; background: radial-gradient(ellipse at 90% 7%, rgba(35, 217, 195, .25), transparent 34%), radial-gradient(ellipse at 5% 100%, rgba(77, 116, 255, .28), transparent 43%), linear-gradient(125deg, #101e42 0%, #182f63 52%, #15516e 100%); border: 1px solid rgba(255, 255, 255, .12); box-shadow: 0 24px 58px rgba(18, 39, 83, .18); }
.qaute-topics-page .topics-hero::before, .qaute-topics-page .topics-hero::after { content: ""; position: absolute; z-index: -1; pointer-events: none; border: 1px solid rgba(255, 255, 255, .13); border-radius: 50%; }
.qaute-topics-page .topics-hero::before { width: 330px; height: 330px; right: -85px; top: -175px; box-shadow: 0 0 0 27px rgba(255, 255, 255, .025), 0 0 0 57px rgba(255, 255, 255, .025); animation: qaute-orbit 24s linear infinite; }
.qaute-topics-page .topics-hero::after { width: 235px; height: 235px; right: 13%; bottom: -175px; border-color: rgba(85, 237, 219, .27); box-shadow: 0 0 0 24px rgba(85, 237, 219, .035); animation: qaute-orbit 18s linear infinite reverse; }
.qaute-topics-page .topics-hero-copy { position: relative; z-index: 1; max-width: 770px; }
.qaute-topics-page .topics-eyebrow { display: inline-flex; align-items: center; gap: .55rem; padding: .55rem .9rem; margin-bottom: 1.2rem; color: #d6fffa; background: rgba(255, 255, 255, .09); border: 1px solid rgba(255, 255, 255, .2); border-radius: 999px; backdrop-filter: blur(12px); font-size: .76rem; font-weight: 850; letter-spacing: .13em; text-transform: uppercase; }
.qaute-topics-page .topics-eyebrow i { color: #73f0df; font-size: 1rem; }
.qaute-topics-page .topics-hero h1 { margin: 0 0 1rem; font-size: clamp(2.05rem, 4.2vw, 3.5rem); font-weight: 900; line-height: 1.12; }
.qaute-topics-page .topics-hero h1 span { color: #8cecdf; background: linear-gradient(90deg, #90f2e3, #a9c9ff); -webkit-background-clip: text; background-clip: text; -webkit-text-fill-color: transparent; }
.qaute-topics-page .topics-hero-description { max-width: 650px; margin: 0; color: rgba(241, 247, 255, .79); font-size: 1rem; line-height: 1.9; }
.qaute-topics-page .topics-hero-actions { display: flex; flex-wrap: wrap; gap: .75rem; margin-top: 1.6rem; }
.qaute-topics-page .topics-hero-actions .btn { display: inline-flex; align-items: center; justify-content: center; gap: .5rem; padding: .8rem 1.1rem; border-radius: 12px; font-weight: 750; transition: transform .25s ease, box-shadow .25s ease, background .25s ease; }
.qaute-topics-page .topics-hero-actions .btn:hover { transform: translateY(-3px); }
.qaute-topics-page .topics-hero-actions .btn-primary { color: #10234b; background: linear-gradient(110deg, #a0f5e7, #a7d5ff); border-color: transparent; box-shadow: 0 8px 22px rgba(13, 208, 185, .17); }
.qaute-topics-page .topics-hero-actions .btn-primary:hover { color: #10234b; background: linear-gradient(110deg, #b5fff1, #bfddff); box-shadow: 0 12px 26px rgba(13, 208, 185, .25); }
.qaute-topics-page .topics-hero-actions .btn-outline-light { border-color: rgba(255, 255, 255, .32); background: rgba(255, 255, 255, .055); }
.qaute-topics-page .topics-hero-actions .btn-outline-light:hover { color: #fff; background: rgba(255, 255, 255, .14); }
.qaute-topics-page .topics-hero-art { position: relative; z-index: 1; display: flex; flex: 0 0 var(--guest-hero-art-size); width: var(--guest-hero-art-size); height: var(--guest-hero-art-size); align-items: center; justify-content: center; color: #a5f6eb; background: linear-gradient(145deg, rgba(255,255,255,.16), rgba(255,255,255,.035)); border: 1px solid rgba(255,255,255,.26); border-radius: 42px; box-shadow: inset 0 1px 0 rgba(255,255,255,.2), 0 20px 46px rgba(0,0,0,.14); backdrop-filter: blur(14px); transform: rotate(5deg); animation: qaute-float-rotate 5s ease-in-out infinite; }
.qaute-topics-page .topics-hero-art i { font-size: var(--guest-hero-icon-size); filter: drop-shadow(0 8px 18px rgba(81, 227, 218, .2)); }
.qaute-topics-page .topics-hero-art::before, .qaute-topics-page .topics-hero-art::after { content: ""; position: absolute; border: 1px solid rgba(151, 245, 232, .38); border-radius: 50%; }
.qaute-topics-page .topics-hero-art::before { inset: -16px; }
.qaute-topics-page .topics-hero-art::after { inset: -31px; border-color: rgba(255, 255, 255, .1); }
.qaute-topics-page .topics-content-heading { display: flex; align-items: end; justify-content: space-between; flex-wrap: wrap; gap: 1rem; margin-bottom: 1.4rem; }
.qaute-topics-page .topics-section-kicker { display: inline-flex; align-items: center; gap: .45rem; margin-bottom: .55rem; color: var(--topics-blue); font-size: .75rem; font-weight: 850; letter-spacing: .13em; text-transform: uppercase; }
.qaute-topics-page .topics-content-heading h2 { margin: 0 0 .45rem; color: var(--topics-navy); font-size: clamp(1.4rem, 2.6vw, 1.9rem); font-weight: 900; letter-spacing: -.035em; }
.qaute-topics-page .topics-content-heading p { margin: 0; color: var(--topics-muted); line-height: 1.75; }
.qaute-topics-page .topics-directory-mark { display: inline-flex; align-items: center; gap: .5rem; padding: .6rem .85rem; color: #3457bd; background: #eef3ff; border: 1px solid #dfe8ff; border-radius: 12px; font-size: .8rem; font-weight: 800; }
.qaute-topics-page .topics-department { overflow: hidden; margin-bottom: 1.6rem; background: #fff; border: 1px solid var(--topics-border); border-radius: 24px; box-shadow: var(--topics-shadow); transition: box-shadow .3s ease, border-color .3s ease; }
.qaute-topics-page .topics-department:hover { border-color: rgba(56, 101, 233, .27); box-shadow: 0 18px 42px rgba(27, 47, 91, .1); }
.qaute-topics-page .topics-department-header { position: relative; overflow: hidden; padding: 1.55rem 1.6rem 1.35rem; background: radial-gradient(circle at 100% 0%, rgba(84, 119, 231, .12), transparent 34%), linear-gradient(135deg, #f8faff 0%, #f0f5ff 100%); border-bottom: 1px solid #e5ecf9; }
.qaute-topics-page .topics-department-header::after { content: ""; position: absolute; top: -62px; right: -40px; width: 165px; height: 165px; border: 1px solid rgba(75, 111, 223, .1); border-radius: 50%; box-shadow: 0 0 0 20px rgba(75, 111, 223, .025); pointer-events: none; }
.qaute-topics-page .topics-department-heading { position: relative; z-index: 1; display: flex; align-items: start; justify-content: space-between; gap: 1rem; }
.qaute-topics-page .topics-department-identity { display: flex; align-items: start; gap: 1rem; min-width: 0; }
.qaute-topics-page .topics-department-icon { display: inline-flex; flex: 0 0 52px; width: 52px; height: 52px; align-items: center; justify-content: center; color: #315bd8; background: linear-gradient(145deg, #e5edff, #d4e2ff); border: 1px solid rgba(255,255,255,.9); border-radius: 17px; font-size: 1.45rem; box-shadow: 0 6px 15px rgba(48, 91, 207, .1); transition: transform .3s ease; }
.qaute-topics-page .topics-department:hover .topics-department-icon { transform: translateY(-2px) rotate(-5deg); }
.qaute-topics-page .topics-department-name { margin: .05rem 0 .45rem; color: var(--topics-navy); font-size: clamp(1.15rem, 2.2vw, 1.45rem); font-weight: 900; line-height: 1.4; letter-spacing: -.025em; overflow-wrap: anywhere; }
.qaute-topics-page .topics-department-description { max-width: 780px; color: #6a7890; font-size: .9rem; line-height: 1.8; }
.qaute-topics-page .topics-department-code { position: relative; z-index: 1; flex-shrink: 0; padding: .45rem .7rem; color: #526481; background: rgba(255,255,255,.8); border: 1px solid #dce5f5; border-radius: 10px; font-size: .74rem; font-weight: 850; letter-spacing: .04em; }
.qaute-topics-page .topics-contact-strip { position: relative; z-index: 1; display: flex; flex-wrap: wrap; gap: .6rem; margin-top: 1.2rem; padding-top: 1.1rem; border-top: 1px solid #e2eafa; }
.qaute-topics-page .topics-contact-item { display: inline-flex; align-items: center; gap: .5rem; max-width: 100%; padding: .5rem .7rem; color: #53647f; background: rgba(255,255,255,.72); border: 1px solid #e4eaf5; border-radius: 10px; font-size: .78rem; line-height: 1.55; overflow-wrap: anywhere; transition: background .2s ease, border-color .2s ease; }
.qaute-topics-page .topics-contact-item:hover { background: #fff; border-color: #cbd9fa; }
.qaute-topics-page .topics-contact-item i { flex-shrink: 0; font-size: .94rem; }
.qaute-topics-page .topics-contact-item a { color: inherit; text-decoration: none; overflow-wrap: anywhere; }
.qaute-topics-page .topics-contact-item a:hover { color: #2855d6; text-decoration: underline; }
.qaute-topics-page .topics-department-body { padding: 1.55rem; }
.qaute-topics-page .topics-category-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 1rem; }
.qaute-topics-page .topics-category-card { position: relative; display: flex; flex-direction: column; min-width: 0; height: 100%; padding: 1.15rem; overflow: hidden; background: linear-gradient(155deg, #fff, #fbfcff); border: 1px solid #e4eaf5; border-radius: 19px; transition: transform .3s ease, box-shadow .3s ease, border-color .3s ease, background .3s ease; }
.qaute-topics-page .topics-category-card::before { content: ""; position: absolute; top: 0; left: 0; width: 100%; height: 3px; opacity: 0; background: linear-gradient(90deg, #3c65e9, #18c4b0); transition: opacity .3s ease; }
.qaute-topics-page .topics-category-card:not(.topics-category-empty):hover { transform: translateY(-5px); border-color: rgba(56, 101, 233, .3); box-shadow: 0 16px 32px rgba(35, 64, 125, .1); }
.qaute-topics-page .topics-category-card:not(.topics-category-empty):hover::before { opacity: 1; }
.qaute-topics-page .topics-category-card.topics-category-empty { background: linear-gradient(145deg, #fafbfe, #f4f6fa); border-style: dashed; border-color: #d9e0eb; }
.qaute-topics-page .topics-category-top { display: flex; align-items: start; gap: .8rem; min-width: 0; margin-bottom: .9rem; }
.qaute-topics-page .topics-category-icon { display: inline-flex; flex: 0 0 44px; width: 44px; height: 44px; align-items: center; justify-content: center; color: #355fe0; background: linear-gradient(145deg, #edf2ff, #e0e9ff); border: 1px solid #e0e9ff; border-radius: 14px; font-size: 1.3rem; transition: transform .3s ease, border-radius .3s ease; }
.qaute-topics-page .topics-category-card:not(.topics-category-empty):hover .topics-category-icon { transform: rotate(-5deg) scale(1.05); border-radius: 17px; }
.qaute-topics-page .topics-category-empty .topics-category-icon { color: #929db0; background: #edf0f5; border-color: #e7eaf0; }
.qaute-topics-page .topics-category-copy { min-width: 0; flex: 1; }
.qaute-topics-page .topics-category-name { margin: .1rem 0 .45rem; color: #203455; font-size: .98rem; font-weight: 850; line-height: 1.5; overflow-wrap: anywhere; }
.qaute-topics-page .topics-category-name a { color: inherit; text-decoration: none; transition: color .2s ease; }
.qaute-topics-page .topics-category-name a:hover { color: #315bd8; }
.qaute-topics-page .topics-category-empty .topics-category-name { color: #77849a; }
.qaute-topics-page .topics-faq-count { display: inline-flex; align-items: center; gap: .38rem; color: #77859a; font-size: .76rem; line-height: 1.6; }
.qaute-topics-page .topics-faq-count i { color: #4371e7; }
.qaute-topics-page .topics-category-empty .topics-faq-count i { color: #9ba6b7; }
.qaute-topics-page .topics-subcategories { padding-top: .9rem; margin-top: .1rem; border-top: 1px solid #e9eef7; }
.qaute-topics-page .topics-subcategories-heading { display: flex; align-items: center; gap: .4rem; margin-bottom: .6rem; color: #71809a; font-size: .73rem; font-weight: 850; }
.qaute-topics-page .topics-subcategories-heading i { color: #5777d8; }
.qaute-topics-page .topics-subcategory-list { display: flex; flex-wrap: wrap; gap: .4rem; }
.qaute-topics-page .topics-subcategory-link, .qaute-topics-page .topics-subcategory-empty { display: inline-flex; align-items: center; gap: .35rem; max-width: 100%; padding: .43rem .58rem; border-radius: 9px; font-size: .72rem; font-weight: 700; line-height: 1.45; text-decoration: none; overflow-wrap: anywhere; transition: transform .2s ease, background .2s ease, border-color .2s ease; }
.qaute-topics-page .topics-subcategory-link { color: #465a7a; background: #f4f7ff; border: 1px solid #e2eafa; }
.qaute-topics-page .topics-subcategory-link:hover { transform: translateY(-2px); color: #2852ce; background: #e9efff; border-color: #cbd9ff; }
.qaute-topics-page .topics-subcategory-link .topics-subcategory-count { color: #315bd8; white-space: nowrap; }
.qaute-topics-page .topics-subcategory-empty { color: #939eb0; background: #f5f6f8; border: 1px solid #eceef2; }
.qaute-topics-page .topics-category-footer { display: flex; flex-wrap: wrap; align-items: center; justify-content: space-between; gap: .6rem; margin-top: auto; padding-top: 1rem; }
.qaute-topics-page .topics-category-action { display: inline-flex; align-items: center; justify-content: center; gap: .45rem; padding: .55rem .75rem; color: #315bd8; background: #edf2ff; border: 1px solid #dce6ff; border-radius: 10px; font-size: .77rem; font-weight: 800; text-decoration: none; transition: transform .2s ease, color .2s ease, background .2s ease, box-shadow .2s ease; }
.qaute-topics-page .topics-category-action:hover { transform: translateY(-2px); color: #fff; background: #315bd8; border-color: #315bd8; box-shadow: 0 7px 16px rgba(49, 91, 216, .18); }
.qaute-topics-page .topics-category-action i { transition: transform .2s ease; }
.qaute-topics-page .topics-category-action:hover i { transform: translateX(3px); }
.qaute-topics-page .topics-category-action.topics-action-ticket { color: #fff; background: linear-gradient(110deg, #3865e9, #4b81ee); border-color: transparent; }
.qaute-topics-page .topics-category-action.topics-action-ticket:hover { background: linear-gradient(110deg, #284fd0, #386bd9); }
.qaute-topics-page .topics-empty { padding: 1.5rem; background: linear-gradient(140deg, #fbfcff, #f1f6ff); border: 1px dashed #d0dcf1; border-radius: 22px; }
.qaute-topics-page .topics-empty-icon { display: inline-flex; width: 70px; height: 70px; align-items: center; justify-content: center; margin-bottom: .9rem; color: #3964dc; background: #e7efff; border: 1px solid #d8e5ff; border-radius: 22px; font-size: 1.9rem; }
.qaute-topics-page .topics-empty .qa-empty-state, .qaute-topics-page .topics-empty .empty-state { background: transparent; }
.qaute-topics-page .topics-help-banner { position: relative; isolation: isolate; overflow: hidden; display: flex; align-items: center; justify-content: space-between; gap: 1.5rem; margin-top: 2.2rem; padding: 1.5rem 1.7rem; background: linear-gradient(110deg, #edf3ff, #eafbfa); border: 1px solid #e0eaf8; border-radius: 20px; }
.qaute-topics-page .topics-help-banner::after { content: ""; position: absolute; z-index: -1; right: -35px; bottom: -95px; width: 200px; height: 200px; border: 1px solid rgba(55, 101, 220, .12); border-radius: 50%; box-shadow: 0 0 0 22px rgba(55, 101, 220, .025); }
.qaute-topics-page .topics-help-copy { display: flex; align-items: start; gap: 1rem; }
.qaute-topics-page .topics-help-icon { display: inline-flex; flex: 0 0 48px; width: 48px; height: 48px; align-items: center; justify-content: center; color: #315bd8; background: #fff; border: 1px solid #e1eaff; border-radius: 15px; font-size: 1.3rem; box-shadow: 0 5px 15px rgba(42, 76, 155, .07); }
.qaute-topics-page .topics-help-banner h3 { margin: 0 0 .35rem; color: var(--topics-navy); font-size: 1rem; font-weight: 850; }
.qaute-topics-page .topics-help-banner p { margin: 0; color: #687892; font-size: .86rem; line-height: 1.75; }
.qaute-topics-page .topics-help-action { position: relative; z-index: 1; flex-shrink: 0; display: inline-flex; align-items: center; justify-content: center; gap: .5rem; padding: .8rem 1rem; color: #fff; background: linear-gradient(110deg, #3865e9, #4c7deb); border: 0; border-radius: 12px; font-size: .83rem; font-weight: 800; text-decoration: none; box-shadow: 0 8px 20px rgba(56, 101, 233, .2); transition: transform .25s ease, box-shadow .25s ease; }
.qaute-topics-page .topics-help-action:hover { transform: translateY(-3px); color: #fff; box-shadow: 0 12px 27px rgba(56, 101, 233, .28); }
@media (max-width: 991.98px) {
  .qaute-topics-page .topics-hero { height: 400px; min-height: 400px; }
  .qaute-topics-page .topics-hero-art { flex-basis: var(--guest-hero-art-size-tablet); width: var(--guest-hero-art-size-tablet); height: var(--guest-hero-art-size-tablet); border-radius: 32px; }
  .qaute-topics-page .topics-hero-art i { font-size: 3.2rem; }
  .qaute-topics-page .topics-category-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
}
@media (max-width: 767.98px) {
  .qaute-topics-page .topics-hero { height: 440px; min-height: 440px; align-items: flex-start; padding: 1.5rem 1.25rem; border-radius: var(--guest-hero-radius-mobile); }
  .qaute-topics-page .topics-hero-art { display: none; }
  .qaute-topics-page .topics-hero::before { right: -210px; }
  .qaute-topics-page .topics-hero-actions { align-items: stretch; flex-direction: column; }
  .qaute-topics-page .topics-hero-actions .btn { width: 100%; }
  .qaute-topics-page .topics-department-header { padding: 1.2rem; }
  .qaute-topics-page .topics-department-body { padding: 1rem; }
  .qaute-topics-page .topics-department-heading { flex-direction: column; }
  .qaute-topics-page .topics-department-code { margin-left: 3.8rem; }
  .qaute-topics-page .topics-help-banner { align-items: stretch; flex-direction: column; padding: 1.25rem; }
  .qaute-topics-page .topics-help-action { width: 100%; }
}
@media (max-width: 575.98px) {
  .qaute-topics-page .topics-category-grid { grid-template-columns: minmax(0, 1fr); }
  .qaute-topics-page .topics-contact-strip { align-items: stretch; flex-direction: column; }
  .qaute-topics-page .topics-contact-item { align-items: flex-start; }
  .qaute-topics-page .topics-content-heading { align-items: flex-start; flex-direction: column; }
}
@media (prefers-reduced-motion: reduce) {
  .qaute-topics-page *, .qaute-topics-page *::before, .qaute-topics-page *::after { animation-duration: .01ms !important; animation-iteration-count: 1 !important; scroll-behavior: auto !important; transition-duration: .01ms !important; }
}
</style>




<div class="qaute-topics-page">
    <nav aria-label="breadcrumb" class="topics-breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Chủ đề tư vấn</li>
        </ol>
    </nav>

    <%-- Giới thiệu danh mục chủ đề và liên kết tra cứu FAQ. --%>
    <section class="topics-hero">
        <div class="topics-hero-copy">
            <div class="topics-eyebrow">
                <i class="bi bi-diagram-3-fill"></i>
                QAUTE · KNOWLEDGE DIRECTORY
            </div>
            <h1>Khám phá chủ đề,<br><span>tìm đúng nơi hỗ trợ.</span></h1>
            <p class="topics-hero-description">
                Danh sách chuyên mục và chủ đề tư vấn được phân theo từng đơn vị, phòng ban, giúp bạn dễ dàng xác định đúng lĩnh vực cần tra cứu và tìm câu trả lời phù hợp.
            </p>
            <div class="topics-hero-actions">
                <a href="/faq" class="btn btn-primary">
                    <i class="bi bi-search"></i>
                    Xem tất cả FAQ
                </a>
                <a href="#topics-department-directory" class="btn btn-outline-light">
                    <i class="bi bi-grid-3x3-gap-fill"></i>
                    Khám phá chủ đề
                </a>
            </div>
        </div>
        <div class="topics-hero-art" aria-hidden="true">
            <i class="bi bi-diagram-3-fill"></i>
        </div>
    </section>

    <div class="topics-content-heading" id="topics-department-directory">
        <div>
            <div class="topics-section-kicker">
                <i class="bi bi-buildings-fill"></i>
                DEPARTMENT DIRECTORY
            </div>
            <h2>Chuyên mục theo đơn vị</h2>
            <p>Chọn lĩnh vực phù hợp để xem các câu hỏi thường gặp hoặc gửi yêu cầu tư vấn.</p>
        </div>
        <div class="topics-directory-mark">
            <i class="bi bi-layers-fill"></i>
            Chủ đề tư vấn QAUTE
        </div>
    </div>

    <%-- Ô tìm kiếm chủ đề --%>
    <div class="mb-4">
        <form action="/topics" method="get" class="input-group">
            <input type="search" name="q" class="form-control" placeholder="Tìm kiếm chủ đề, phòng ban..." value="${searchQuery}" aria-label="Tìm kiếm chủ đề">
            <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i></button>
            <c:if test="${isSearch}">
                <a href="/topics" class="btn btn-outline-secondary">Xóa tìm kiếm</a>
            </c:if>
        </form>
    </div>

    <c:choose>
        <c:when test="${empty tree}">
            <div class="topics-empty text-center">
                <div class="topics-empty-icon">
                    <i class="bi bi-folder2-open"></i>
                </div>
                <qa:emptyState icon="bi-folder2-open" title="Chưa có chủ đề" message="Hiện tại chưa có danh mục phòng ban hay chủ đề tư vấn nào được công khai."/>
            </div>
        </c:when>
        <c:otherwise>
            <c:if test="${isSearch}">
                <c:set var="hasResults" value="false"/>
            </c:if>
            <%-- Liệt kê chủ đề theo từng đơn vị và các câu hỏi tương ứng. --%>
            <c:forEach var="dept" items="${tree}">
                <c:set var="deptMatches" value="${false}"/>
                <c:if test="${not empty searchQuery}">
                    <c:if test="${fn:containsIgnoreCase(dept.name, searchQuery) or fn:containsIgnoreCase(dept.description, searchQuery)}">
                        <c:set var="deptMatches" value="true"/>
                    </c:if>
                </c:if>
                <c:if test="${not isSearch or deptMatches}">
                    <c:if test="${deptMatches}">
                        <c:set var="hasResults" value="true"/>
                    </c:if>
                <section class="topics-department">
                    <div class="topics-department-header">
                        <div class="topics-department-heading">
                            <div class="topics-department-identity">
                                <div class="topics-department-icon">
                                    <i class="bi bi-building-fill"></i>
                                </div>
                                <div class="min-width-0">
                                    <h2 class="topics-department-name">
                                        <c:out value="${dept.name}"/>
                                    </h2>
                                    <c:if test="${not empty dept.description}">
                                        <div class="topics-department-description">
                                            <c:out value="${dept.description}"/>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <c:if test="${not empty dept.code}">
                                <span class="topics-department-code">
                                    <c:out value="${dept.code}"/>
                                </span>
                            </c:if>
                        </div>

                        <c:if test="${not empty dept.location or not empty dept.email or not empty dept.phone}">
                            <div class="topics-contact-strip">
                                <c:if test="${not empty dept.location}">
                                    <span class="topics-contact-item">
                                        <i class="bi bi-geo-alt-fill text-danger"></i>
                                        <c:out value="${dept.location}"/>
                                    </span>
                                </c:if>
                                <c:if test="${not empty dept.email}">
                                    <span class="topics-contact-item">
                                        <i class="bi bi-envelope-fill text-primary"></i>
                                        <a href="mailto:<c:out value='${dept.email}'/>">
                                            <c:out value="${dept.email}"/>
                                        </a>
                                    </span>
                                </c:if>
                                <c:if test="${not empty dept.phone}">
                                    <span class="topics-contact-item">
                                        <i class="bi bi-telephone-fill text-success"></i>
                                        <a href="tel:<c:out value='${dept.phone}'/>">
                                            <c:out value="${dept.phone}"/>
                                        </a>
                                    </span>
                                </c:if>
                            </div>
                        </c:if>
                    </div>

                    <div class="topics-department-body">
                        <c:choose>
                            <c:when test="${not empty dept.categories}">
                                <c:set var="hasMatchingCats" value="false"/>
                                <div class="topics-category-grid">
                                    <c:forEach var="cat" items="${dept.categories}">
                                        <c:set var="catMatches" value="false"/>
                                        <c:if test="${not empty searchQuery}">
                                            <c:if test="${fn:containsIgnoreCase(cat.name, searchQuery)}">
                                                <c:set var="catMatches" value="true"/>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${not isSearch or catMatches}">
                                            <c:set var="hasMatchingCats" value="true"/>
                                            <c:if test="${catMatches}">
                                                <c:set var="hasResults" value="true"/>
                                            </c:if>
                                            <c:set var="faqCount" value="${counts[cat.id] != null ? counts[cat.id] : 0}"/>
                                            <div class="topics-category-card ${faqCount == 0 ? 'topics-category-empty' : ''}">
                                                <div class="topics-category-top">
                                                    <div class="topics-category-icon">
                                                        <i class="bi <c:out value='${empty cat.icon ? "bi-folder" : cat.icon}'/>"></i>
                                                    </div>
                                                    <div class="topics-category-copy">
                                                        <c:choose>
                                                            <c:when test="${faqCount > 0}">
                                                                <h3 class="topics-category-name">
                                                                    <a href="/faq?category=<c:out value='${cat.id}'/>">
                                                                        <c:out value="${cat.name}"/>
                                                                    </a>
                                                                </h3>
                                                                <div class="topics-faq-count">
                                                                    <i class="bi bi-chat-square-text-fill"></i>
                                                                    <c:out value="${faqCount}"/> câu hỏi FAQ
                                                                </div>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <h3 class="topics-category-name">
                                                                    <c:out value="${cat.name}"/>
                                                                </h3>
                                                                <div class="topics-faq-count">
                                                                    <i class="bi bi-chat-square-text"></i>
                                                                    0 câu hỏi FAQ
                                                                </div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>

                                                <c:if test="${not empty cat.children}">
                                                    <div class="topics-subcategories">
                                                        <div class="topics-subcategories-heading">
                                                            <i class="bi bi-diagram-2-fill"></i>
                                                            Chủ đề con
                                                        </div>
                                                        <div class="topics-subcategory-list">
                                                            <c:forEach var="sub" items="${cat.children}">
                                                                <c:set var="subCount" value="${counts[sub.id] != null ? counts[sub.id] : 0}"/>
                                                                <c:choose>
                                                                    <c:when test="${subCount > 0}">
                                                                        <a href="/faq?category=<c:out value='${sub.id}'/>"
                                                                           class="topics-subcategory-link"
                                                                           title="<c:out value='${subCount}'/> câu hỏi">
                                                                            <c:out value="${sub.name}"/>
                                                                            <span class="topics-subcategory-count">(<c:out value="${subCount}"/>)</span>
                                                                        </a>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="topics-subcategory-empty">
                                                                            <c:out value="${sub.name}"/>
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </c:if>

                                                <div class="topics-category-footer">
                                                    <c:choose>
                                                        <c:when test="${faqCount > 0}">
                                                            <a href="/faq?category=<c:out value='${cat.id}'/>" class="topics-category-action">
                                                                Xem FAQ
                                                                <i class="bi bi-arrow-right"></i>
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary-subtle text-secondary">
                                                                Chưa có FAQ
                                                            </span>
                                                            <a href="/student/tickets/new?categoryId=<c:out value='${cat.id}'/>" class="topics-category-action topics-action-ticket">
                                                                <i class="bi bi-chat-dots-fill"></i>
                                                                Hỏi tư vấn viên
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${isSearch and not hasMatchingCats}">
                                        <div class="text-secondary small fst-italic py-2">
                                            <i class="bi bi-info-circle me-1"></i>
                                            Không tìm thấy chuyên mục nào phù hợp với từ khóa "${searchQuery}".
                                        </div>
                                    </c:if>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${not isSearch}">
                                    <div class="text-secondary small fst-italic py-2">
                                        <i class="bi bi-info-circle me-1"></i>
                                        Hiện tại chưa có chuyên mục nào thuộc phòng ban này.
                                    </div>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </section>
                </c:if>
            </c:forEach>
            <c:if test="${isSearch and not hasResults}">
                <div class="alert alert-info">
                    <i class="bi bi-info-circle"></i> Không tìm thấy phòng ban hoặc chuyên mục nào phù hợp với từ khóa "${searchQuery}".
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>

    <%-- Hướng dẫn lựa chọn kênh hỗ trợ khi chưa tìm thấy chủ đề phù hợp. --%>
    <section class="topics-help-banner">
        <div class="topics-help-copy">
            <div class="topics-help-icon">
                <i class="bi bi-lightbulb-fill"></i>
            </div>
            <div>
                <h3>Chưa tìm thấy câu trả lời phù hợp?</h3>
                <p>Tra cứu toàn bộ FAQ hoặc chọn chuyên mục để tìm nội dung hỗ trợ liên quan đến vấn đề của bạn.</p>
            </div>
        </div>
        <a href="/faq" class="topics-help-action">
            Khám phá FAQ
            <i class="bi bi-arrow-up-right"></i>
        </a>
    </section>
</div>