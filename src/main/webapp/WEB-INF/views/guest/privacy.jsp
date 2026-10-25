<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Chính sách bảo mật</title>
<style>
.qaute-privacy-page { --privacy-navy: #12234a; --privacy-blue: #3865e9; --privacy-teal: #12b8a7; --privacy-muted: #718098; color: #243651; padding-bottom: 3rem; }
.qaute-privacy-page .privacy-breadcrumb { margin-bottom: 1.25rem; font-size: .88rem; }
.qaute-privacy-page .privacy-breadcrumb .breadcrumb { margin-bottom: 0; }
.qaute-privacy-page .privacy-breadcrumb a { color: #70809a; text-decoration: none; transition: color .2s ease; }
.qaute-privacy-page .privacy-breadcrumb a:hover { color: var(--privacy-blue); }
.qaute-privacy-page .privacy-hero { position: relative; display: flex; align-items: center; box-sizing: border-box; isolation: isolate; height: 400px; min-height: 400px; padding: var(--guest-hero-padding); margin-bottom: 2rem; border-radius: 28px; background: radial-gradient(ellipse at 90% 5%, rgba(35, 217, 195, .25), transparent 34%), radial-gradient(ellipse at 5% 100%, rgba(77, 116, 255, .3), transparent 43%), linear-gradient(125deg, #101e42 0%, #182f63 52%, #15516e 100%); border: 1px solid rgba(255, 255, 255, .12); box-shadow: 0 24px 58px rgba(18, 39, 83, .18); }
.qaute-privacy-page .privacy-hero::before, .qaute-privacy-page .privacy-hero::after { content: ""; position: absolute; z-index: -1; pointer-events: none; border: 1px solid rgba(255, 255, 255, .14); border-radius: 50%; }
.qaute-privacy-page .privacy-hero::before { width: 340px; height: 340px; top: -185px; right: -80px; box-shadow: 0 0 0 28px rgba(255, 255, 255, .025), 0 0 0 60px rgba(255, 255, 255, .02); animation: qaute-orbit 25s linear infinite; }
.qaute-privacy-page .privacy-hero::after { width: 220px; height: 220px; right: 16%; bottom: -170px; border-color: rgba(93, 236, 219, .27); box-shadow: 0 0 0 24px rgba(93, 236, 219, .035); animation: qaute-orbit 19s linear infinite reverse; }
.qaute-privacy-page .privacy-hero-content { position: relative; z-index: 1; display: flex; width: 100%; align-items: center; justify-content: space-between; gap: var(--guest-hero-gap); }
.qaute-privacy-page .privacy-hero-copy { max-width: 750px; }
.qaute-privacy-page .privacy-eyebrow { display: inline-flex; align-items: center; gap: .55rem; padding: .55rem .9rem; margin-bottom: 1.2rem; color: #d6fffa; background: rgba(255, 255, 255, .09); border: 1px solid rgba(255, 255, 255, .2); border-radius: 999px; backdrop-filter: blur(12px); font-size: .75rem; font-weight: 850; letter-spacing: .13em; text-transform: uppercase; }
.qaute-privacy-page .privacy-eyebrow i { color: #73f0df; font-size: 1rem; }
.qaute-privacy-page .privacy-hero h1 { margin: 0 0 1rem; font-size: clamp(2.05rem, 4.2vw, 3.4rem); font-weight: 900; line-height: 1.12; }
.qaute-privacy-page .privacy-hero h1 span { color: #8cecdf; background: linear-gradient(90deg, #90f2e3, #a9c9ff); -webkit-background-clip: text; background-clip: text; -webkit-text-fill-color: transparent; }
.qaute-privacy-page .privacy-hero-description { max-width: 650px; margin: 0; color: rgba(241, 247, 255, .8); font-size: 1rem; line-height: 1.9; }
.qaute-privacy-page .privacy-hero-art { position: relative; display: flex; flex: 0 0 var(--guest-hero-art-size); width: var(--guest-hero-art-size); height: var(--guest-hero-art-size); align-items: center; justify-content: center; color: #a6f4e9; background: linear-gradient(145deg, rgba(255,255,255,.16), rgba(255,255,255,.035)); border: 1px solid rgba(255,255,255,.25); border-radius: 40px; box-shadow: inset 0 1px 0 rgba(255,255,255,.2), 0 22px 48px rgba(0,0,0,.16); backdrop-filter: blur(14px); transform: rotate(5deg); animation: qaute-float-rotate-soft 5s ease-in-out infinite; }
.qaute-privacy-page .privacy-hero-art i { font-size: var(--guest-hero-icon-size); filter: drop-shadow(0 8px 18px rgba(81, 227, 218, .22)); }
.qaute-privacy-page .privacy-hero-art::before, .qaute-privacy-page .privacy-hero-art::after { content: ""; position: absolute; inset: -15px; border: 1px solid rgba(151, 245, 232, .34); border-radius: inherit; }
.qaute-privacy-page .privacy-hero-art::after { inset: -29px; border-color: rgba(255, 255, 255, .1); }
.qaute-privacy-page .privacy-content { position: relative; overflow: hidden; padding: clamp(1.4rem, 3.5vw, 2.6rem); background: linear-gradient(145deg, #fff, #fbfdff); border: 1px solid #e2eaf5; border-radius: 24px; box-shadow: 0 14px 40px rgba(27, 47, 91, .065); }
.qaute-privacy-page .privacy-content-heading { display: flex; align-items: center; gap: 1rem; margin-bottom: 1.5rem; }
.qaute-privacy-page .privacy-content-icon { display: flex; flex: 0 0 54px; width: 54px; height: 54px; align-items: center; justify-content: center; color: #315bd8; background: linear-gradient(145deg, #eaf0ff, #dce8ff); border: 1px solid #dce6ff; border-radius: 17px; font-size: 1.5rem; box-shadow: 0 7px 16px rgba(49, 91, 216, .08); }
.qaute-privacy-page .privacy-content-heading h2 { margin: 0 0 .3rem; color: var(--privacy-navy); font-size: clamp(1.2rem, 2.4vw, 1.55rem); font-weight: 900; letter-spacing: -.025em; }
.qaute-privacy-page .privacy-content-heading p { margin: 0; color: var(--privacy-muted); font-size: .88rem; line-height: 1.7; }
.qaute-privacy-page .privacy-statement { position: relative; display: flex; align-items: flex-start; gap: 1rem; padding: 1.2rem; margin-bottom: 1rem; background: linear-gradient(120deg, #f5f8ff, #fbfdff); border: 1px solid #e5ecf8; border-radius: 17px; transition: transform .28s ease, box-shadow .28s ease, border-color .28s ease; }
.qaute-privacy-page .privacy-statement:last-child { margin-bottom: 0; }
.qaute-privacy-page .privacy-statement:hover { transform: translateY(-3px); border-color: #cbd9fb; box-shadow: 0 12px 25px rgba(39, 72, 139, .075); }
.qaute-privacy-page .privacy-statement-icon { display: flex; flex: 0 0 43px; width: 43px; height: 43px; align-items: center; justify-content: center; color: #315bd8; background: #fff; border: 1px solid #e1e9fb; border-radius: 13px; font-size: 1.2rem; box-shadow: 0 4px 12px rgba(30, 57, 113, .04); }
.qaute-privacy-page .privacy-statement:nth-of-type(2) .privacy-statement-icon { color: #0c9887; border-color: #d8f2ed; }
.qaute-privacy-page .privacy-statement h3 { margin: .05rem 0 .35rem; color: #203555; font-size: .98rem; font-weight: 850; }
.qaute-privacy-page .privacy-statement p { margin: 0; color: #64738b; font-size: .92rem; line-height: 1.85; }
.qaute-privacy-page .privacy-contact { position: relative; isolation: isolate; overflow: hidden; display: flex; align-items: center; justify-content: space-between; gap: 1.5rem; padding: 1.5rem 1.65rem; margin-top: 1.5rem; background: linear-gradient(110deg, #edf3ff, #eafbfa); border: 1px solid #deebf7; border-radius: 20px; }
.qaute-privacy-page .privacy-contact::after { content: ""; position: absolute; z-index: -1; right: -45px; bottom: -100px; width: 190px; height: 190px; border: 1px solid rgba(55, 101, 220, .12); border-radius: 50%; box-shadow: 0 0 0 23px rgba(55, 101, 220, .025); }
.qaute-privacy-page .privacy-contact-copy { display: flex; align-items: flex-start; gap: 1rem; }
.qaute-privacy-page .privacy-contact-icon { display: flex; flex: 0 0 46px; width: 46px; height: 46px; align-items: center; justify-content: center; color: #315bd8; background: #fff; border: 1px solid #dfe8fa; border-radius: 14px; font-size: 1.25rem; box-shadow: 0 5px 15px rgba(42, 76, 155, .06); }
.qaute-privacy-page .privacy-contact h2 { margin: 0 0 .4rem; color: var(--privacy-navy); font-size: 1rem; font-weight: 850; }
.qaute-privacy-page .privacy-contact p { margin: 0; color: #687892; font-size: .88rem; line-height: 1.8; }
.qaute-privacy-page .privacy-contact-action { position: relative; z-index: 1; flex-shrink: 0; display: inline-flex; align-items: center; justify-content: center; gap: .5rem; padding: .8rem 1rem; color: #fff; background: linear-gradient(110deg, #3865e9, #4c7deb); border: 0; border-radius: 12px; font-size: .84rem; font-weight: 800; text-decoration: none; box-shadow: 0 8px 20px rgba(56, 101, 233, .2); transition: transform .25s ease, box-shadow .25s ease; }
.qaute-privacy-page .privacy-contact-action:hover { transform: translateY(-3px); color: #fff; box-shadow: 0 12px 27px rgba(56, 101, 233, .28); }
.qaute-privacy-page .privacy-bottom-note { display: flex; align-items: center; justify-content: center; gap: .55rem; margin-top: 1.2rem; color: #8491a5; font-size: .78rem; text-align: center; }
.qaute-privacy-page .privacy-bottom-note i { color: #0ca48f; }
@media (max-width: 767.98px) {
  .qaute-privacy-page .privacy-hero { height: 440px; min-height: 440px; align-items: flex-start; padding: 1.5rem 1.25rem; border-radius: var(--guest-hero-radius-mobile); }
  .qaute-privacy-page .privacy-hero-content { align-items: flex-start; }
  .qaute-privacy-page .privacy-hero-art { display: none; }
  .qaute-privacy-page .privacy-hero::before { right: -220px; }
  .qaute-privacy-page .privacy-content { padding: 1.2rem; border-radius: 20px; }
  .qaute-privacy-page .privacy-statement { padding: 1rem; gap: .75rem; }
  .qaute-privacy-page .privacy-contact { align-items: stretch; flex-direction: column; padding: 1.2rem; }
  .qaute-privacy-page .privacy-contact-action { width: 100%; }
}
@media (max-width: 420px) {
  .qaute-privacy-page .privacy-statement-icon { flex-basis: 37px; width: 37px; height: 37px; border-radius: 11px; font-size: 1.05rem; }
  .qaute-privacy-page .privacy-content-heading { align-items: flex-start; }
  .qaute-privacy-page .privacy-content-icon { flex-basis: 45px; width: 45px; height: 45px; }
}
@media (prefers-reduced-motion: reduce) {
  .qaute-privacy-page *, .qaute-privacy-page *::before, .qaute-privacy-page *::after { animation-duration: .01ms !important; animation-iteration-count: 1 !important; scroll-behavior: auto !important; transition-duration: .01ms !important; }
}
</style>




<div class="qaute-privacy-page">
    <nav aria-label="breadcrumb" class="privacy-breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Chính sách bảo mật</li>
        </ol>
    </nav>

    <section class="privacy-hero">
        <div class="privacy-hero-content">
            <div class="privacy-hero-copy">
                <div class="privacy-eyebrow">
                    <i class="bi bi-shield-lock-fill"></i>
                    QAUTE · PRIVACY CENTER
                </div>
                <h1>Quyền riêng tư<br><span>là điều quan trọng.</span></h1>
                <p class="privacy-hero-description">
                    QAUTE cam kết bảo vệ thông tin cá nhân của sinh viên. Dữ liệu chỉ được sử dụng cho mục đích hỗ trợ tư vấn và không chia sẻ cho bên thứ ba.
                </p>
            </div>
            <div class="privacy-hero-art" aria-hidden="true">
                <i class="bi bi-shield-lock-fill"></i>
            </div>
        </div>
    </section>

    <section class="privacy-content">
        <div class="privacy-content-heading">
            <div class="privacy-content-icon">
                <i class="bi bi-shield-check"></i>
            </div>
            <div>
                <h2>Cam kết bảo mật thông tin</h2>
                <p>Thông tin về cách QAUTE sử dụng dữ liệu trong hoạt động hỗ trợ sinh viên.</p>
            </div>
        </div>

        <div class="privacy-statement">
            <div class="privacy-statement-icon">
                <i class="bi bi-person-lock"></i>
            </div>
            <div>
                <h3>Bảo vệ thông tin cá nhân</h3>
                <p>QAUTE cam kết bảo vệ thông tin cá nhân của sinh viên.</p>
            </div>
        </div>

        <div class="privacy-statement">
            <div class="privacy-statement-icon">
                <i class="bi bi-database-lock"></i>
            </div>
            <div>
                <h3>Phạm vi sử dụng dữ liệu</h3>
                <p>Dữ liệu chỉ được sử dụng cho mục đích hỗ trợ tư vấn và không chia sẻ cho bên thứ ba.</p>
            </div>
        </div>
    </section>

    <section class="privacy-contact">
        <div class="privacy-contact-copy">
            <div class="privacy-contact-icon">
                <i class="bi bi-envelope-paper-heart"></i>
            </div>
            <div>
                <h2>Bạn có thắc mắc về quyền riêng tư?</h2>
                <p>Nếu có thắc mắc, vui lòng liên hệ qua trang Liên hệ để tìm kênh hỗ trợ phù hợp.</p>
            </div>
        </div>
        <a href="/pages/contact" class="privacy-contact-action">
            <i class="bi bi-arrow-up-right-circle"></i>
            Liên hệ QAUTE
        </a>
    </section>

    <div class="privacy-bottom-note">
        <i class="bi bi-shield-check"></i>
        <span>QAUTE · Chính sách bảo mật</span>
    </div>
</div>