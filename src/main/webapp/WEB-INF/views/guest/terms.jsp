<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Điều khoản sử dụng</title>
<style>
.qaute-terms-page { --terms-navy: #12234a; --terms-blue: #3865e9; --terms-teal: #12b8a7; --terms-muted: #718098; color: #243651; padding-bottom: 3rem; }
.qaute-terms-page .terms-breadcrumb { margin-bottom: 1.25rem; font-size: .88rem; }
.qaute-terms-page .terms-breadcrumb .breadcrumb { margin-bottom: 0; }
.qaute-terms-page .terms-breadcrumb a { color: #70809a; text-decoration: none; transition: color .2s ease; }
.qaute-terms-page .terms-breadcrumb a:hover { color: var(--terms-blue); }
.qaute-terms-page .terms-hero { isolation: isolate; padding: clamp(2.2rem, 5vw, 4rem); margin-bottom: 2rem; background: radial-gradient(ellipse at 90% 5%, rgba(35, 217, 195, .25), transparent 34%), radial-gradient(ellipse at 5% 100%, rgba(77, 116, 255, .3), transparent 43%), linear-gradient(125deg, #101e42 0%, #182f63 52%, #15516e 100%); border: 1px solid rgba(255, 255, 255, .12); box-shadow: 0 24px 58px rgba(18, 39, 83, .18); }
.qaute-terms-page .terms-hero::before, .qaute-terms-page .terms-hero::after { content: ""; position: absolute; z-index: -1; pointer-events: none; border: 1px solid rgba(255, 255, 255, .14); border-radius: 50%; }
.qaute-terms-page .terms-hero::before { width: 350px; height: 350px; top: -185px; right: -85px; box-shadow: 0 0 0 28px rgba(255, 255, 255, .025), 0 0 0 62px rgba(255, 255, 255, .02); animation: qaute-orbit 25s linear infinite; }
.qaute-terms-page .terms-hero::after { width: 225px; height: 225px; right: 15%; bottom: -175px; border-color: rgba(93, 236, 219, .27); box-shadow: 0 0 0 24px rgba(93, 236, 219, .035); animation: qaute-orbit 19s linear infinite reverse; }
.qaute-terms-page .terms-hero-content { position: relative; z-index: 1; display: flex; align-items: center; justify-content: space-between; gap: 2rem; }
.qaute-terms-page .terms-hero-copy { max-width: 760px; }
.qaute-terms-page .terms-eyebrow { display: inline-flex; align-items: center; gap: .55rem; padding: .55rem .9rem; margin-bottom: 1.2rem; color: #d6fffa; background: rgba(255, 255, 255, .09); border: 1px solid rgba(255, 255, 255, .2); border-radius: 999px; backdrop-filter: blur(12px); font-size: .75rem; font-weight: 850; letter-spacing: .13em; text-transform: uppercase; }
.qaute-terms-page .terms-eyebrow i { color: #73f0df; font-size: 1rem; }
.qaute-terms-page .terms-hero h1 { margin: 0 0 1rem; font-size: clamp(2.05rem, 4.2vw, 3.4rem); font-weight: 900; line-height: 1.12; }
.qaute-terms-page .terms-hero h1 span { color: #8cecdf; background: linear-gradient(90deg, #90f2e3, #a9c9ff); -webkit-background-clip: text; background-clip: text; -webkit-text-fill-color: transparent; }
.qaute-terms-page .terms-hero-description { max-width: 650px; margin: 0; color: rgba(241, 247, 255, .8); font-size: 1rem; line-height: 1.9; }
.qaute-terms-page .terms-hero-art { position: relative; display: flex; flex: 0 0 145px; width: 145px; height: 145px; align-items: center; justify-content: center; color: #a6f4e9; background: linear-gradient(145deg, rgba(255,255,255,.16), rgba(255,255,255,.035)); border: 1px solid rgba(255,255,255,.25); border-radius: 40px; box-shadow: inset 0 1px 0 rgba(255,255,255,.2), 0 22px 48px rgba(0,0,0,.16); backdrop-filter: blur(14px); transform: rotate(5deg); animation: qaute-float-rotate-soft 5s ease-in-out infinite; }
.qaute-terms-page .terms-hero-art i { font-size: 4rem; filter: drop-shadow(0 8px 18px rgba(81, 227, 218, .22)); }
.qaute-terms-page .terms-hero-art::before, .qaute-terms-page .terms-hero-art::after { content: ""; position: absolute; inset: -15px; border: 1px solid rgba(151, 245, 232, .34); border-radius: inherit; }
.qaute-terms-page .terms-hero-art::after { inset: -29px; border-color: rgba(255, 255, 255, .1); }
.qaute-terms-page .terms-content { position: relative; overflow: hidden; padding: clamp(1.35rem, 3.5vw, 2.5rem); background: linear-gradient(145deg, #fff, #fbfdff); border: 1px solid #e2eaf5; border-radius: 24px; box-shadow: 0 14px 40px rgba(27, 47, 91, .065); }
.qaute-terms-page .terms-content-heading { display: flex; align-items: center; gap: 1rem; padding-bottom: 1.4rem; margin-bottom: 1.4rem; border-bottom: 1px solid #eaf0f8; }
.qaute-terms-page .terms-content-icon { display: flex; flex: 0 0 54px; width: 54px; height: 54px; align-items: center; justify-content: center; color: #315bd8; background: linear-gradient(145deg, #eaf0ff, #dce8ff); border: 1px solid #dce6ff; border-radius: 17px; font-size: 1.5rem; box-shadow: 0 7px 16px rgba(49, 91, 216, .08); }
.qaute-terms-page .terms-content-heading h2 { margin: 0 0 .3rem; color: var(--terms-navy); font-size: clamp(1.2rem, 2.4vw, 1.55rem); font-weight: 900; letter-spacing: -.025em; }
.qaute-terms-page .terms-content-heading p { margin: 0; color: var(--terms-muted); font-size: .88rem; line-height: 1.7; }
.qaute-terms-page .terms-statement { display: flex; align-items: flex-start; gap: 1rem; padding: 1.25rem; margin-bottom: 1rem; background: linear-gradient(120deg, #f5f8ff, #fbfdff); border: 1px solid #e5ecf8; border-radius: 17px; transition: transform .28s ease, box-shadow .28s ease, border-color .28s ease; }
.qaute-terms-page .terms-statement:last-child { margin-bottom: 0; }
.qaute-terms-page .terms-statement:hover { transform: translateY(-3px); border-color: #cbd9fb; box-shadow: 0 12px 25px rgba(39, 72, 139, .075); }
.qaute-terms-page .terms-statement-icon { display: flex; flex: 0 0 44px; width: 44px; height: 44px; align-items: center; justify-content: center; color: #315bd8; background: #fff; border: 1px solid #e1e9fb; border-radius: 13px; font-size: 1.2rem; box-shadow: 0 4px 12px rgba(30, 57, 113, .04); transition: transform .25s ease; }
.qaute-terms-page .terms-statement:hover .terms-statement-icon { transform: rotate(-5deg) scale(1.05); }
.qaute-terms-page .terms-statement-copy { min-width: 0; flex: 1; }
.qaute-terms-page .terms-statement h3 { margin: .05rem 0 .4rem; color: #203555; font-size: .98rem; font-weight: 850; }
.qaute-terms-page .terms-statement p { margin: 0; color: #64738b; font-size: .93rem; line-height: 1.9; }
.qaute-terms-page .terms-contact { position: relative; isolation: isolate; overflow: hidden; display: flex; align-items: center; justify-content: space-between; gap: 1.5rem; padding: 1.5rem 1.65rem; margin-top: 1.5rem; background: linear-gradient(110deg, #edf3ff, #eafbfa); border: 1px solid #deebf7; border-radius: 20px; }
.qaute-terms-page .terms-contact::after { content: ""; position: absolute; z-index: -1; right: -45px; bottom: -100px; width: 190px; height: 190px; border: 1px solid rgba(55, 101, 220, .12); border-radius: 50%; box-shadow: 0 0 0 23px rgba(55, 101, 220, .025); }
.qaute-terms-page .terms-contact-copy { display: flex; align-items: flex-start; gap: 1rem; }
.qaute-terms-page .terms-contact-icon { display: flex; flex: 0 0 46px; width: 46px; height: 46px; align-items: center; justify-content: center; color: #315bd8; background: #fff; border: 1px solid #dfe8fa; border-radius: 14px; font-size: 1.25rem; box-shadow: 0 5px 15px rgba(42, 76, 155, .06); }
.qaute-terms-page .terms-contact h2 { margin: 0 0 .4rem; color: var(--terms-navy); font-size: 1rem; font-weight: 850; }
.qaute-terms-page .terms-contact p { margin: 0; color: #687892; font-size: .88rem; line-height: 1.8; }
.qaute-terms-page .terms-contact-action { position: relative; z-index: 1; display: inline-flex; flex-shrink: 0; align-items: center; justify-content: center; gap: .5rem; padding: .8rem 1rem; color: #fff; background: linear-gradient(110deg, #3865e9, #4c7deb); border: 0; border-radius: 12px; font-size: .84rem; font-weight: 800; text-decoration: none; box-shadow: 0 8px 20px rgba(56, 101, 233, .2); transition: transform .25s ease, box-shadow .25s ease; }
.qaute-terms-page .terms-contact-action:hover { transform: translateY(-3px); color: #fff; box-shadow: 0 12px 27px rgba(56, 101, 233, .28); }
.qaute-terms-page .terms-bottom-note { display: flex; align-items: center; justify-content: center; gap: .55rem; margin-top: 1.2rem; color: #8491a5; font-size: .78rem; text-align: center; }
.qaute-terms-page .terms-bottom-note i { color: #0ca48f; }
@media (max-width: 767.98px) {
  .qaute-terms-page .terms-hero { padding: 2rem 1.35rem; border-radius: 23px; }
  .qaute-terms-page .terms-hero-content { align-items: flex-start; }
  .qaute-terms-page .terms-hero-art { display: none; }
  .qaute-terms-page .terms-hero::before { right: -220px; }
  .qaute-terms-page .terms-content { padding: 1.2rem; border-radius: 20px; }
  .qaute-terms-page .terms-statement { padding: 1rem; gap: .75rem; }
  .qaute-terms-page .terms-contact { align-items: stretch; flex-direction: column; padding: 1.2rem; }
  .qaute-terms-page .terms-contact-action { width: 100%; }
}
@media (max-width: 420px) {
  .qaute-terms-page .terms-statement-icon { flex-basis: 37px; width: 37px; height: 37px; border-radius: 11px; font-size: 1.05rem; }
  .qaute-terms-page .terms-content-heading { align-items: flex-start; }
  .qaute-terms-page .terms-content-icon { flex-basis: 45px; width: 45px; height: 45px; }
}
@media (prefers-reduced-motion: reduce) {
  .qaute-terms-page *, .qaute-terms-page *::before, .qaute-terms-page *::after { animation-duration: .01ms !important; animation-iteration-count: 1 !important; scroll-behavior: auto !important; transition-duration: .01ms !important; }
}
</style>




<div class="qaute-terms-page">
    <nav aria-label="breadcrumb" class="terms-breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Điều khoản sử dụng</li>
        </ol>
    </nav>

    <%-- Giới thiệu mục đích và phạm vi của điều khoản sử dụng QAUTE. --%>
    <section class="terms-hero">
        <div class="terms-hero-content">
            <div class="terms-hero-copy">
                <div class="terms-eyebrow">
                    <i class="bi bi-file-earmark-lock2-fill"></i>
                    QAUTE · TERMS OF USE
                </div>
                <h1>Sử dụng có trách nhiệm.<br><span>Kết nối hiệu quả.</span></h1>
                <p class="terms-hero-description">
                    Điều khoản sử dụng định hướng việc sử dụng hệ thống QAUTE trong hoạt động tư vấn và hỗ trợ sinh viên.
                </p>
            </div>
            <div class="terms-hero-art" aria-hidden="true">
                <i class="bi bi-file-earmark-check-fill"></i>
            </div>
        </div>
    </section>

    <%-- Trình bày các điều khoản và lưu ý dành cho người sử dụng hệ thống. --%>
    <section class="terms-content">
        <div class="terms-content-heading">
            <div class="terms-content-icon">
                <i class="bi bi-journal-check"></i>
            </div>
            <div>
                <h2>Điều khoản sử dụng QAUTE</h2>
                <p>Thông tin cơ bản dành cho người sử dụng hệ thống tư vấn sinh viên.</p>
            </div>
        </div>

        <div class="terms-statement">
            <div class="terms-statement-icon">
                <i class="bi bi-check2-circle"></i>
            </div>
            <div class="terms-statement-copy">
                <h3>Tuân thủ quy định khi sử dụng hệ thống</h3>
                <p>Bằng việc sử dụng QAUTE, bạn đồng ý tuân thủ các quy định của nhà trường và hệ thống tư vấn sinh viên.</p>
            </div>
        </div>
    </section>

    <%-- Cung cấp liên kết đến trang liên hệ khi cần hỗ trợ thêm. --%>
    <section class="terms-contact">
        <div class="terms-contact-copy">
            <div class="terms-contact-icon">
                <i class="bi bi-headset"></i>
            </div>
            <div>
                <h2>Cần hỗ trợ thêm?</h2>
                <p>Liên hệ với bộ phận phụ trách qua trang Liên hệ nếu bạn cần hỗ trợ.</p>
            </div>
        </div>
        <a href="/pages/contact" class="terms-contact-action">
            <i class="bi bi-arrow-up-right-circle"></i>
            Liên hệ QAUTE
        </a>
    </section>

    <div class="terms-bottom-note">
        <i class="bi bi-shield-check"></i>
        <span>QAUTE · Điều khoản sử dụng</span>
    </div>
</div>