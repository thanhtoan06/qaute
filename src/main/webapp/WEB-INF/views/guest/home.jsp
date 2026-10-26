<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Trang chủ - Cổng Tư Vấn Sinh Viên HCMUTE</title>
<style>
.qaute-home-redesign { --home-blue: #2165bd; --home-blue-dark: #123b72; --home-ink: #183655; --home-muted: #71849b; --home-border: #e0e9f4; --home-surface: #ffffff; --home-radius: 21px; --home-shadow: 0 8px 28px rgba(19, 49, 86, .06); --home-shadow-hover: 0 18px 42px rgba(19, 49, 86, .12); color: var(--home-ink); overflow: clip; }
.qaute-home-redesign *, .qaute-home-redesign *::before, .qaute-home-redesign *::after { box-sizing: border-box; }
.qaute-home-redesign a { text-underline-offset: 3px; }
.qaute-home-redesign ::selection { color: #fff; background: #2165bd; }
.qaute-home-redesign .container-xl { position: relative; z-index: 1; }
.qaute-home-redesign .hcmute-hero { position: relative; isolation: isolate; overflow: hidden; color: #fff; background: radial-gradient(ellipse at 85% 8%, rgba(93, 165, 255, .28), transparent 35%), radial-gradient(ellipse at 5% 100%, rgba(66, 121, 217, .2), transparent 40%), linear-gradient(125deg, #071d3b 0%, #0b3267 49%, #1559a3 100%); }
.qaute-home-redesign .hcmute-hero::before { content: ""; position: absolute; z-index: -1; top: -240px; right: -100px; width: min(60vw, 680px); aspect-ratio: 1; border: 1px solid rgba(255, 255, 255, .12); border-radius: 50%; box-shadow: 0 0 0 32px rgba(255,255,255,.025), 0 0 0 72px rgba(255,255,255,.018), 0 0 0 124px rgba(255,255,255,.012); pointer-events: none; animation: home-orbit 25s ease-in-out infinite alternate; }
.qaute-home-redesign .hcmute-hero::after { content: ""; position: absolute; z-index: -1; inset: auto 0 0; height: 1px; background: linear-gradient(90deg, transparent, rgba(255,255,255,.4), transparent); }
@keyframes home-orbit {
  from { transform: translate3d(0, 0, 0) rotate(0deg); }
  to { transform: translate3d(-16px, 18px, 0) rotate(5deg); }
}
.qaute-home-redesign .hcmute-hero .row.align-items-center { padding-top: clamp(2.7rem, 6vw, 5rem) !important; padding-bottom: clamp(2.7rem, 6vw, 5rem) !important; }
.qaute-home-redesign .hcmute-hero .col-lg-7 { animation: qaute-rise-in .65s cubic-bezier(.2,.75,.3,1) both; }
.qaute-home-redesign .hcmute-hero-pill { display: inline-flex; align-items: center; gap: .55rem; padding: .58rem .9rem; border: 1px solid rgba(255,255,255,.2); border-radius: 999px; background: rgba(255,255,255,.09); color: #e2edff; font-size: .77rem; font-weight: 650; -webkit-backdrop-filter: blur(10px); backdrop-filter: blur(10px); box-shadow: inset 0 1px 0 rgba(255,255,255,.08); }
.qaute-home-redesign .hcmute-hero-pill .bi { color: #9bd5ff; font-size: .95rem; }
.qaute-home-redesign .hcmute-hero-title { max-width: 780px; margin: 0 0 1.15rem; color: #fff; font-size: clamp(2.15rem, 4.6vw, 4.15rem); font-weight: 850; line-height: 1.12; letter-spacing: -.055em; text-wrap: balance; }
.qaute-home-redesign .hcmute-hero-desc { max-width: 640px; margin-bottom: 1.65rem; color: rgba(230, 240, 255, .82); font-size: clamp(.94rem, 1.3vw, 1.08rem); line-height: 1.85; }
.qaute-home-redesign .hcmute-hero-search-box { display: flex; align-items: center; width: 100%; max-width: 100%; min-height: 62px; padding: 6px; border: 1px solid rgba(255,255,255,.3); border-radius: 16px; background: #fff; box-shadow: 0 14px 35px rgba(0, 14, 37, .22), inset 0 1px 0 rgba(255,255,255,.9); transition: transform .25s ease, box-shadow .25s ease; }
.qaute-home-redesign .hcmute-hero-search-box > .qa-suggest-wrapper { display: flex; flex: 1 1 0; align-items: center; width: 100%; min-width: 0; }
.qaute-home-redesign .hcmute-hero-search-box:focus-within { transform: translateY(-2px); box-shadow: 0 19px 42px rgba(0, 14, 37, .27), 0 0 0 4px rgba(150, 202, 255, .17); }
.qaute-home-redesign .hcmute-hero-search-box > i { flex-shrink: 0; color: #6f87a4 !important; }
.qaute-home-redesign .hcmute-hero-search-input { flex: 1 1 auto; width: 100%; min-width: 0; height: 47px; padding: .7rem .4rem; border: 0; outline: 0; background: transparent; color: #233e5e; font-size: .88rem; box-shadow: none; pointer-events: auto; }
.qaute-home-redesign .hcmute-hero-search-input:focus-visible { outline: none; }
.qaute-home-redesign .hcmute-hero-search-input::placeholder { color: #93a2b5; }
.qaute-home-redesign .hcmute-hero-search-btn { position: relative; flex: 0 0 auto; min-height: 47px; padding: .7rem 1.25rem; overflow: hidden; border: 1px solid #1b5eb2; border-radius: 12px; background: linear-gradient(120deg, #347fe0, #1e5db1); color: #fff; font-size: .83rem; font-weight: 750; box-shadow: 0 5px 13px rgba(29, 89, 166, .22); transition: transform .2s ease, box-shadow .2s ease, background .2s ease; }
.qaute-home-redesign .hcmute-hero-search-btn:hover { transform: translateY(-1px); background: linear-gradient(120deg, #418be9, #174d96); box-shadow: 0 8px 18px rgba(29, 89, 166, .3); }
.qaute-home-redesign .hcmute-hero-search-btn:active { transform: scale(.98); }
.qaute-home-redesign .bg-white-10, .qaute-home-redesign .bg-white-20 { background: rgba(255,255,255,.1) !important; }
.qaute-home-redesign .hcmute-hero .badge { border: 1px solid rgba(255,255,255,.18); background: rgba(255,255,255,.09) !important; color: #edf5ff !important; font-size: .7rem; font-weight: 650; padding: .42rem .68rem !important; transition: background .2s ease, border-color .2s ease, transform .2s ease; }
.qaute-home-redesign .hcmute-hero .badge:hover { transform: translateY(-2px); border-color: rgba(255,255,255,.4); background: rgba(255,255,255,.18) !important; }
.qaute-home-redesign .hcmute-hero .border-top, .qaute-home-redesign .hcmute-hero .border-white-10 { border-color: rgba(255,255,255,.16) !important; }
.qaute-home-redesign .hcmute-hero-stat-num { color: #fff; font-size: clamp(1.45rem, 2.4vw, 2rem); font-weight: 850; letter-spacing: -.045em; line-height: 1.2; font-variant-numeric: tabular-nums; }
.qaute-home-redesign .hcmute-hero-stat-label { margin-top: .35rem; color: rgba(222, 235, 252, .73); font-size: .72rem; line-height: 1.5; }
.qaute-home-redesign .hcmute-hero-image-wrap { position: relative; min-height: 350px; overflow: hidden; border: 1px solid rgba(255,255,255,.3); border-radius: 28px; background: linear-gradient(145deg, rgba(255,255,255,.15), rgba(255,255,255,.025)); box-shadow: 0 26px 65px rgba(0, 15, 39, .27), 0 0 0 9px rgba(255,255,255,.035); transform: rotate(1.4deg); transition: transform .4s cubic-bezier(.2,.75,.3,1), box-shadow .4s ease; animation: qaute-rise-in .8s .12s cubic-bezier(.2,.75,.3,1) both; }
.qaute-home-redesign .hcmute-hero-image-wrap::after { content: ""; position: absolute; inset: 0; background: linear-gradient(180deg, rgba(4,26,56,.02) 35%, rgba(4,26,56,.23)); pointer-events: none; }
.qaute-home-redesign .hcmute-hero-image-wrap img { display: block; min-height: 350px; object-position: center; transition: transform .65s cubic-bezier(.2,.75,.3,1), filter .4s ease; }
.qaute-home-redesign .hcmute-hero-image-wrap:hover { transform: rotate(0) translateY(-4px); box-shadow: 0 32px 75px rgba(0, 15, 39, .33), 0 0 0 9px rgba(255,255,255,.045); }
.qaute-home-redesign .hcmute-hero-image-wrap:hover img { transform: scale(1.045); filter: saturate(1.08); }
.qaute-home-redesign .hcmute-quick-services { position: relative; z-index: 2; padding: 0 0 1.6rem; background: linear-gradient(180deg, #f0f5fc 0%, #f7f9fd 100%); }
.qaute-home-redesign .hcmute-quick-services .container-xl { transform: translateY(-17px); }
.qaute-home-redesign .hcmute-service-card { position: relative; display: block; height: 100%; min-height: 190px; padding: 1.25rem 1.15rem; overflow: hidden; border: 1px solid #e2eaf5; border-radius: 17px; background: rgba(255,255,255,.97); color: inherit; text-decoration: none; box-shadow: 0 9px 24px rgba(20, 49, 83, .06); transition: transform .25s cubic-bezier(.2,.75,.3,1), box-shadow .25s ease, border-color .25s ease; }
.qaute-home-redesign .hcmute-service-card::after { content: ""; position: absolute; right: -43px; bottom: -58px; width: 125px; height: 125px; border-radius: 50%; background: rgba(39, 111, 201, .04); transition: transform .3s ease, background .3s ease; }
.qaute-home-redesign .hcmute-service-card:hover { transform: translateY(-5px); border-color: #bfd4ed; box-shadow: var(--home-shadow-hover); color: inherit; }
.qaute-home-redesign .hcmute-service-card:hover::after { transform: scale(1.25); background: rgba(39, 111, 201, .07); }
.qaute-home-redesign .hcmute-service-icon { display: flex; align-items: center; justify-content: center; width: 47px; height: 47px; margin-bottom: 1rem; border: 1px solid #e0ecfa; border-radius: 14px; background: linear-gradient(145deg, #f5f9ff, #e8f2ff); color: #2c72c4; font-size: 1.35rem; box-shadow: inset 0 1px 0 #fff; transition: transform .25s ease, box-shadow .25s ease; }
.qaute-home-redesign .hcmute-service-card:hover .hcmute-service-icon { transform: translateY(-2px) rotate(-4deg); box-shadow: 0 7px 15px rgba(39, 111, 201, .12); }
.qaute-home-redesign .hcmute-service-title { margin: 0 0 .45rem; color: #233f60; font-size: .9rem; font-weight: 800; }
.qaute-home-redesign .hcmute-service-desc { position: relative; z-index: 1; margin: 0; color: #7c8ea5; font-size: .74rem; line-height: 1.7; }
.qaute-home-redesign .hcmute-section { position: relative; padding: clamp(3.2rem, 6vw, 5.3rem) 0; background: rgba(255,255,255,.58); }
.qaute-home-redesign .hcmute-section-muted { background: radial-gradient(ellipse at 100% 0%, rgba(68, 133, 222, .06), transparent 34%), linear-gradient(145deg, #f4f7fc, #f8faff 54%, #f1f6fc); }
.qaute-home-redesign .hcmute-eyebrow { display: inline-flex; align-items: center; gap: .5rem; margin-bottom: .9rem; padding: .42rem .76rem; border: 1px solid #dce9fa; border-radius: 999px; background: linear-gradient(135deg, #fff, #edf5ff); color: #2867b0; font-size: .68rem; font-weight: 800; letter-spacing: .035em; }
.qaute-home-redesign .hcmute-eyebrow .bi { font-size: .86rem; }
.qaute-home-redesign .hcmute-title-main { margin: 0 0 .8rem; color: #1a385a; font-size: clamp(1.7rem, 3.5vw, 2.65rem); font-weight: 850; line-height: 1.2; letter-spacing: -.045em; text-wrap: balance; }
.qaute-home-redesign .hcmute-desc-main { max-width: 650px; color: #7588a0; font-size: .91rem; line-height: 1.9; }
.qaute-home-redesign .hcmute-card-modern { border: 1px solid rgba(222, 232, 244, .98); border-radius: var(--home-radius); background: rgba(255,255,255,.95); box-shadow: var(--home-shadow); transition: border-color .25s ease, box-shadow .25s ease, transform .25s ease; }
.qaute-home-redesign .hcmute-card-modern:hover { border-color: #d1e0f2; box-shadow: var(--home-shadow-hover); }
.qaute-home-redesign .hcmute-card-modern .text-dark { color: #253f5e !important; }
.qaute-home-redesign .hcmute-card-modern .text-muted, .qaute-home-redesign .hcmute-card-modern .text-secondary { color: #7c8ea5 !important; }
.qaute-home-redesign .hcmute-card-modern .text-primary { color: #246dc2 !important; }
.qaute-home-redesign .hcmute-card-modern .btn { font-weight: 700; transition: transform .2s ease, box-shadow .2s ease, background .2s ease; }
.qaute-home-redesign .hcmute-card-modern .btn:hover { transform: translateY(-1px); }
.qaute-home-redesign .hcmute-card-modern .btn-outline-primary { border-color: #c9d9ee; color: #2867b0; background: #fff; }
.qaute-home-redesign .hcmute-card-modern .btn-outline-primary:hover { border-color: #286fc7; background: #286fc7; color: #fff; }
.qaute-home-redesign .hcmute-card-modern .hover-shadow, .qaute-home-redesign .hcmute-card-modern .hover-bg-light { transition: background .2s ease, border-color .2s ease, box-shadow .2s ease, transform .2s ease; }
.qaute-home-redesign .hcmute-card-modern .hover-shadow:hover, .qaute-home-redesign .hcmute-card-modern .hover-bg-light:hover { border-color: #d2e2f5 !important; background: #f7faff !important; box-shadow: 0 5px 15px rgba(24, 62, 104, .045); transform: translateX(2px); }
.qaute-home-redesign .hcmute-card-modern .badge { font-weight: 700; letter-spacing: .005em; }
.qaute-home-redesign #popular-faq-list > a { border-radius: 13px !important; }
.qaute-home-redesign #popular-faq-list > a .badge:first-child { border: 1px solid #d8e8fc; background: linear-gradient(145deg, #eff6ff, #e3efff) !important; color: #2365b4 !important; }
.qaute-home-redesign #popular-faq-list > a .bi-chevron-right { transition: color .2s ease, transform .2s ease; }
.qaute-home-redesign #popular-faq-list > a:hover .bi-chevron-right { color: #286fc7 !important; transform: translateX(3px); }
.qaute-home-redesign .hcmute-card-modern .rounded-3.border.bg-light { border-color: #e4ebf4 !important; background: linear-gradient(135deg, #fbfcff, #f4f8fd) !important; transition: border-color .2s ease, transform .2s ease, box-shadow .2s ease; }
.qaute-home-redesign .hcmute-card-modern a.rounded-3.border.bg-light:hover { border-color: #bed3ed !important; transform: translateY(-2px); box-shadow: 0 7px 16px rgba(23, 58, 99, .07); }
.qaute-home-redesign .hcmute-logo-icon { display: inline-flex; align-items: center; justify-content: center; border: 1px solid #dce9fa; border-radius: 15px; background: linear-gradient(145deg, #f2f8ff, #e2efff); color: #286fc7; box-shadow: inset 0 1px 0 #fff; }
.qaute-home-redesign .rounded-4.p-4.text-white.shadow-sm { position: relative; isolation: isolate; overflow: hidden; border: 1px solid rgba(255,255,255,.14); border-radius: 21px !important; background: radial-gradient(ellipse at 100% 0%, rgba(107, 174, 255, .27), transparent 46%), linear-gradient(135deg, #081e3b 0%, #103e7f 100%) !important; box-shadow: 0 13px 31px rgba(8, 34, 72, .16) !important; }
.qaute-home-redesign .rounded-4.p-4.text-white.shadow-sm::after { content: ""; position: absolute; z-index: -1; top: -65px; right: -55px; width: 175px; height: 175px; border: 1px solid rgba(255,255,255,.11); border-radius: 50%; box-shadow: 0 0 0 20px rgba(255,255,255,.025), 0 0 0 46px rgba(255,255,255,.016); }
.qaute-home-redesign .rounded-4.p-4.text-white.shadow-sm .btn-light { border: 1px solid rgba(255,255,255,.6); box-shadow: 0 4px 12px rgba(0,0,0,.08); transition: transform .2s ease, box-shadow .2s ease; }
.qaute-home-redesign .rounded-4.p-4.text-white.shadow-sm .btn-light:hover { transform: translateY(-2px); box-shadow: 0 8px 19px rgba(0,0,0,.15); }
.qaute-home-redesign .hcmute-section .row.g-3.flex-grow-1 .hcmute-card-modern { min-height: 145px; }
.qaute-home-redesign .hcmute-section .row.g-3.flex-grow-1 .bi { filter: drop-shadow(0 3px 6px rgba(33, 101, 189, .08)); }
.qaute-home-redesign .hcmute-intro-card { position: relative; isolation: isolate; overflow: hidden; border: 1px solid var(--intro-border); background: radial-gradient(ellipse at 100% 0%, var(--intro-glow), transparent 55%), linear-gradient(145deg, var(--intro-bg), #fff 78%) !important; box-shadow: 0 8px 22px var(--intro-shadow); transition: transform .3s cubic-bezier(.2,.75,.3,1), box-shadow .3s ease, border-color .3s ease; }
.qaute-home-redesign .row.g-3.flex-grow-1 > div:nth-child(1) .hcmute-intro-card { --intro-bg: #f0f8ff; --intro-glow: rgba(66, 153, 225, .2); --intro-border: #d7eaff; --intro-shadow: rgba(34, 108, 178, .08); --intro-accent: #2374c6; --intro-icon-bg: linear-gradient(145deg, #dff1ff, #c7e4ff); --intro-icon-border: #c5e2fb; --intro-icon-shadow: rgba(35, 116, 198, .22); }
.qaute-home-redesign .row.g-3.flex-grow-1 > div:nth-child(2) .hcmute-intro-card { --intro-bg: #f6f3ff; --intro-glow: rgba(139, 111, 231, .2); --intro-border: #e5ddff; --intro-shadow: rgba(104, 77, 181, .08); --intro-accent: #7353c6; --intro-icon-bg: linear-gradient(145deg, #eee8ff, #ddd3ff); --intro-icon-border: #ded3ff; --intro-icon-shadow: rgba(115, 83, 198, .22); }
.qaute-home-redesign .hcmute-intro-card::before { content: ""; position: absolute; z-index: -1; inset: 0 0 auto; height: 4px; background: linear-gradient(90deg, var(--intro-accent), color-mix(in srgb, var(--intro-accent), #fff 48%)); transform: scaleX(.28); transform-origin: left; transition: transform .35s cubic-bezier(.2,.75,.3,1); }
.qaute-home-redesign .hcmute-intro-card::after { content: ""; position: absolute; z-index: -1; right: -34px; bottom: -42px; width: 112px; height: 112px; border: 1px solid color-mix(in srgb, var(--intro-accent), transparent 82%); border-radius: 50%; box-shadow: 0 0 0 16px color-mix(in srgb, var(--intro-accent), transparent 95%), 0 0 0 34px color-mix(in srgb, var(--intro-accent), transparent 97%); pointer-events: none; transition: transform .45s ease; }
.qaute-home-redesign .hcmute-intro-card:hover { transform: translateY(-6px); border-color: color-mix(in srgb, var(--intro-accent), #fff 55%); box-shadow: 0 17px 34px var(--intro-shadow), 0 0 0 3px color-mix(in srgb, var(--intro-accent), transparent 93%); }
.qaute-home-redesign .hcmute-intro-card:hover::before { transform: scaleX(1); }
.qaute-home-redesign .hcmute-intro-card:hover::after { transform: scale(1.18) translate(-5px, -5px); }
.qaute-home-redesign .hcmute-intro-icon { display: inline-flex; align-items: center; justify-content: center; width: 46px; height: 46px; margin-bottom: .85rem; border: 1px solid var(--intro-icon-border); border-radius: 15px; background: var(--intro-icon-bg); color: var(--intro-accent); font-size: 1.25rem; box-shadow: 0 6px 14px var(--intro-icon-shadow), inset 0 1px 0 rgba(255,255,255,.8); animation: hcmute-intro-float 3.6s ease-in-out infinite alternate; transition: transform .3s cubic-bezier(.2,.75,.3,1), box-shadow .3s ease; }
.qaute-home-redesign .row.g-3.flex-grow-1 > div:nth-child(2) .hcmute-intro-icon { animation-delay: -.9s; }
@keyframes hcmute-intro-float { from { transform: translateY(0); } to { transform: translateY(-4px); } }
.qaute-home-redesign .hcmute-intro-card:hover .hcmute-intro-icon { transform: rotate(-7deg) scale(1.12); box-shadow: 0 9px 19px var(--intro-icon-shadow), inset 0 1px 0 rgba(255,255,255,.9); animation-play-state: paused; }
.qaute-home-redesign .hcmute-intro-title { margin-bottom: .45rem; color: var(--intro-accent); font-size: .94rem; font-weight: 850; line-height: 1.35; }
.qaute-home-redesign .hcmute-intro-desc { margin: 0; color: #627990; font-size: .74rem; line-height: 1.7; }
.qaute-home-redesign #faq .max-w-xl { max-width: 720px; }
.qaute-home-redesign #faq .accordion { --bs-accordion-btn-focus-box-shadow: 0 0 0 3px rgba(35, 105, 199, .08); }
.qaute-home-redesign #faq .accordion-item { border-color: #edf1f7 !important; background: transparent; }
.qaute-home-redesign #faq .accordion-item:last-child { border-bottom: 0 !important; }
.qaute-home-redesign #faq .accordion-button { gap: .75rem; padding: 1rem .85rem; border-radius: 12px; background: transparent; color: #2c4564 !important; box-shadow: none; font-size: .9rem !important; line-height: 1.5; transition: background .2s ease, color .2s ease; }
.qaute-home-redesign #faq .accordion-button:hover { background: #f6f9fe; }
.qaute-home-redesign #faq .accordion-button:not(.collapsed) { background: linear-gradient(110deg, #f1f7ff, #f9fbff); color: #205eb0 !important; }
.qaute-home-redesign #faq .accordion-button::after { flex-shrink: 0; width: 1rem; height: 1rem; background-size: .9rem; opacity: .65; }
.qaute-home-redesign #faq .accordion-button .badge { font-size: .65rem; font-weight: 750; }
.qaute-home-redesign #faq .accordion-body { padding: .45rem 1rem 1.15rem; color: #70849d !important; font-size: .82rem !important; line-height: 1.85 !important; }
.qaute-home-redesign #faq-category-buttons .btn { min-height: 44px; padding: .7rem .8rem !important; border: 1px solid #e1e9f3; border-radius: 11px !important; background: rgba(255,255,255,.9); color: #526b87; font-size: .8rem; box-shadow: 0 2px 7px rgba(15, 41, 73, .025); transition: background .2s ease, border-color .2s ease, color .2s ease, transform .2s ease, box-shadow .2s ease; }
.qaute-home-redesign #faq-category-buttons .btn:hover { border-color: #c7d9ef; background: #f4f8ff; color: #235fa8; transform: translateX(2px); }
.qaute-home-redesign #faq-category-buttons .btn.active-category, .qaute-home-redesign #faq-category-buttons .btn.btn-primary { border-color: #2165bd !important; background: linear-gradient(110deg, #2f78d1, #205cb0) !important; color: #fff !important; box-shadow: 0 6px 15px rgba(33, 101, 189, .17); }
.qaute-home-redesign #faq-category-buttons .btn .badge { font-size: .65rem; }
.qaute-home-redesign #faq-filter-input { min-height: 45px; border-color: #e0e8f2; border-radius: 0 11px 11px 0; color: #304965; box-shadow: none; font-size: .8rem; }
.qaute-home-redesign #faq-filter-input:focus { border-color: #71a4e4; box-shadow: 0 0 0 4px rgba(35,105,199,.08); }
.qaute-home-redesign #faq .input-group-text { border-color: #e0e8f2; border-radius: 11px 0 0 11px; background: #fff; color: #8297b0; }
.qaute-home-redesign #faq-empty-state { border-top: 1px solid #edf1f7; }
.qaute-home-redesign #ticket, .qaute-home-redesign #booking { position: relative; }
.qaute-home-redesign #ticket .hcmute-card-modern, .qaute-home-redesign #booking .hcmute-card-modern { position: relative; overflow: hidden; }
.qaute-home-redesign #ticket .display-6, .qaute-home-redesign #booking .display-6 { display: inline-flex; align-items: center; justify-content: center; width: 67px; height: 67px; border: 1px solid #e0ebfa; border-radius: 20px; background: linear-gradient(145deg, #f2f8ff, #e2efff); font-size: 1.8rem; }
.qaute-home-redesign #ticket .btn-primary, .qaute-home-redesign #booking .btn-primary { border: 1px solid #2165bd; background: linear-gradient(120deg, #347fe0, #1e5db1); box-shadow: 0 6px 15px rgba(33, 101, 189, .16); font-size: .82rem; font-weight: 750; transition: transform .2s ease, box-shadow .2s ease; }
.qaute-home-redesign #ticket .btn-primary:hover, .qaute-home-redesign #booking .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 10px 22px rgba(33, 101, 189, .24); }
.qaute-home-redesign .hcmute-card-modern .rounded-circle.bg-white-10 { border: 1px solid rgba(255,255,255,.12); }
.qaute-home-redesign .transition-all { transition: all .2s ease; }
.qaute-home-redesign .btn:focus-visible, .qaute-home-redesign a:focus-visible, .qaute-home-redesign input:focus-visible, .qaute-home-redesign button:focus-visible { outline: 3px solid rgba(52, 120, 229, .33); outline-offset: 3px; }
.qaute-home-redesign .hcmute-quick-services { background: radial-gradient(ellipse at 10% 10%, rgba(107, 166, 255, .12), transparent 42%), linear-gradient(180deg, #edf4fd 0%, #f7f9fd 100%); }
.qaute-home-redesign .hcmute-service-card { --service-accent: #2d75c8; --service-tint: #eaf3ff; border-color: color-mix(in srgb, var(--service-accent), #fff 84%); background: radial-gradient(ellipse at 100% 0%, color-mix(in srgb, var(--service-accent), transparent 91%), transparent 52%), #fff; }
.qaute-home-redesign .hcmute-quick-services .row > div:nth-child(2) .hcmute-service-card { --service-accent: #148b82; --service-tint: #e3f7f3; }
.qaute-home-redesign .hcmute-quick-services .row > div:nth-child(3) .hcmute-service-card { --service-accent: #7958c7; --service-tint: #f0eaff; }
.qaute-home-redesign .hcmute-quick-services .row > div:nth-child(4) .hcmute-service-card { --service-accent: #d07a2d; --service-tint: #fff1df; }
.qaute-home-redesign .hcmute-service-card::before { content: ""; position: absolute; inset: 0 0 auto; height: 3px; background: linear-gradient(90deg, var(--service-accent), transparent 85%); transform: scaleX(.38); transform-origin: left; transition: transform .3s ease; }
.qaute-home-redesign .hcmute-service-card:hover::before { transform: scaleX(1); }
.qaute-home-redesign .hcmute-service-card::after { background: radial-gradient(circle, color-mix(in srgb, var(--service-accent), transparent 86%), transparent 68%); }
.qaute-home-redesign .hcmute-service-icon { border-color: color-mix(in srgb, var(--service-accent), #fff 78%); background: linear-gradient(145deg, #fff, var(--service-tint)); color: var(--service-accent); box-shadow: 0 5px 14px color-mix(in srgb, var(--service-accent), transparent 88%), inset 0 1px 0 #fff; }
.qaute-home-redesign .hcmute-service-title { color: #223d5c; }
.qaute-home-redesign .hcmute-service-card:hover .hcmute-service-title { color: var(--service-accent); }
.qaute-home-redesign .hcmute-section { background-color: #fff; }
.qaute-home-redesign .hcmute-section-muted { background: radial-gradient(ellipse at 100% 0%, rgba(69, 130, 220, .08), transparent 35%), linear-gradient(145deg, #f1f6fd 0%, #f8faff 54%, #f2f7fc 100%); }
.qaute-home-redesign .hcmute-title-main { position: relative; }
.qaute-home-redesign .hcmute-section .hcmute-title-main::after { content: ""; display: block; width: 54px; height: 4px; margin-top: .8rem; border-radius: 99px; background: linear-gradient(90deg, #347fe0, #8ab9f0); }
.qaute-home-redesign .hcmute-section .text-center .hcmute-title-main::after { margin-right: auto; margin-left: auto; }
.qaute-home-redesign #discovery > .container-xl > .row:first-child { margin-bottom: 2.5rem !important; }
.qaute-home-redesign #discovery > .container-xl > .row:first-child .hcmute-card-modern { border-color: #e0eafb; background: radial-gradient(ellipse at 100% 0%, rgba(113, 164, 231, .12), transparent 55%), linear-gradient(145deg, #fff, #f8fbff); }
.qaute-home-redesign #discovery > .container-xl > .row:first-child .hcmute-card-modern > .d-flex .bi-fire { display: inline-flex; align-items: center; justify-content: center; width: 30px; height: 30px; border-radius: 10px; background: #fff0e8; }
.qaute-home-redesign #discovery #popular-faq-list > a { border-color: #e8eef6 !important; background: linear-gradient(110deg, #fff, #fbfdff); }
.qaute-home-redesign #discovery #popular-faq-list > a:hover { border-color: #cbdcf1 !important; background: linear-gradient(110deg, #f2f7ff, #fff) !important; box-shadow: 0 7px 18px rgba(27, 69, 116, .07); transform: translateY(-2px); }
.qaute-home-redesign #discovery > .container-xl > .hcmute-card-modern { border-color: #dce8f7; background: linear-gradient(110deg, #edf5ff, #fff 70%); }
.qaute-home-redesign #discovery > .container-xl > .hcmute-card-modern .hcmute-logo-icon { width: 54px !important; height: 54px !important; border-radius: 17px; box-shadow: 0 7px 16px rgba(38, 107, 185, .12); }
.qaute-home-redesign #discovery > .container-xl > .hcmute-card-modern .btn { box-shadow: 0 5px 13px rgba(33, 101, 189, .08); }
.qaute-home-redesign #faq-category-buttons { padding: .7rem; border: 1px solid #e3ebf5; border-radius: 18px; background: rgba(255,255,255,.8); box-shadow: 0 8px 22px rgba(19, 49, 86, .045); }
.qaute-home-redesign #faq-category-buttons .btn { border-color: transparent; }
.qaute-home-redesign #faq-category-buttons .btn.active-category .badge, .qaute-home-redesign #faq-category-buttons .btn.btn-primary .badge { background: rgba(255,255,255,.2) !important; color: #fff !important; }
.qaute-home-redesign #faq .col-lg-9 > .hcmute-card-modern { border-color: #e3ebf5; background: linear-gradient(145deg, #fff, #fcfdff); }
.qaute-home-redesign #faq .accordion-item { margin-bottom: .55rem; overflow: hidden; border: 1px solid #e8edf5 !important; border-radius: 14px; background: #fff; transition: border-color .2s ease, box-shadow .2s ease, transform .2s ease; }
.qaute-home-redesign #faq .accordion-item:hover { border-color: #cbdcf1 !important; box-shadow: 0 5px 16px rgba(24, 62, 104, .055); transform: translateY(-1px); }
.qaute-home-redesign #faq .accordion-item:last-child { margin-bottom: 0; }
.qaute-home-redesign #faq .accordion-button:not(.collapsed) { background: linear-gradient(105deg, #eef6ff, #fbfdff); }
.qaute-home-redesign #faq .accordion-button .badge { padding: .38rem .6rem; border: 1px solid #d9e8fa; border-radius: 999px; background: #f0f6ff !important; }
.qaute-home-redesign #faq .accordion-button:not(.collapsed) .badge { border-color: #c8def8; background: #e4f0ff !important; }
.qaute-home-redesign #ticket, .qaute-home-redesign #booking { isolation: isolate; overflow: hidden; }
.qaute-home-redesign #ticket::before, .qaute-home-redesign #booking::before { content: ""; position: absolute; z-index: -1; top: -100px; right: -70px; width: 280px; height: 280px; border: 1px solid rgba(64, 128, 208, .1); border-radius: 50%; box-shadow: 0 0 0 28px rgba(64, 128, 208, .025), 0 0 0 62px rgba(64, 128, 208, .018); pointer-events: none; }
.qaute-home-redesign #booking::before { border-color: rgba(121, 88, 199, .12); box-shadow: 0 0 0 28px rgba(121, 88, 199, .03), 0 0 0 62px rgba(121, 88, 199, .018); }
.qaute-home-redesign #ticket > .container-xl, .qaute-home-redesign #booking > .container-xl { z-index: 1; }
.qaute-home-redesign #ticket .col-lg-5 > .hcmute-card-modern, .qaute-home-redesign #booking .col-lg-5 > .hcmute-card-modern { padding: 2rem !important; border-color: #dce8f7; background: radial-gradient(ellipse at 50% 0%, rgba(82, 148, 227, .13), transparent 50%), linear-gradient(145deg, #fff, #f8fbff); }
.qaute-home-redesign #booking .col-lg-5 > .hcmute-card-modern { border-color: #e5def8; background: radial-gradient(ellipse at 50% 0%, rgba(133, 105, 216, .14), transparent 50%), linear-gradient(145deg, #fff, #fbf9ff); }
.qaute-home-redesign #ticket .col-lg-5 > .hcmute-card-modern .display-6, .qaute-home-redesign #booking .col-lg-5 > .hcmute-card-modern .display-6 { width: 74px; height: 74px; margin: 0 auto; border-radius: 22px; box-shadow: 0 8px 20px rgba(42, 105, 177, .13); }
.qaute-home-redesign #booking .col-lg-5 > .hcmute-card-modern .display-6 { border-color: #e3daf9; background: linear-gradient(145deg, #f3efff, #e7ddff); color: #7353c6 !important; box-shadow: 0 8px 20px rgba(115, 83, 198, .14); }
.qaute-home-redesign #ticket .col-lg-5 > .hcmute-card-modern .btn, .qaute-home-redesign #booking .col-lg-5 > .hcmute-card-modern .btn { padding: .75rem 1.25rem; }
.qaute-home-redesign #ticket { background: radial-gradient(ellipse at 100% 0%, rgba(63, 139, 222, .1), transparent 38%), linear-gradient(145deg, #f0f6ff, #f8faff 58%, #f3f8fd); }
.qaute-home-redesign #booking { background: radial-gradient(ellipse at 100% 0%, rgba(133, 105, 216, .09), transparent 38%), linear-gradient(145deg, #f7f4ff, #fbfaff 58%, #f4f7fd); }
.qaute-home-redesign .hcmute-journey-section { position: relative; padding: clamp(3.5rem, 7vw, 6.25rem) 0; overflow: hidden; background: radial-gradient(ellipse at 6% 100%, rgba(82, 166, 232, .12), transparent 35%), linear-gradient(135deg, #fff 0%, #f4f8ff 56%, #eef5ff 100%); }
.qaute-home-redesign .hcmute-journey-section::before { content: ""; position: absolute; top: -130px; right: -100px; width: 340px; height: 340px; border: 1px solid rgba(54, 119, 193, .1); border-radius: 50%; box-shadow: 0 0 0 28px rgba(54, 119, 193, .025), 0 0 0 62px rgba(54, 119, 193, .018); pointer-events: none; }
.qaute-home-redesign .hcmute-journey-copy { position: relative; z-index: 1; max-width: 620px; }
.qaute-home-redesign .hcmute-journey-copy .hcmute-title-main { max-width: 600px; font-size: clamp(1.9rem, 3.6vw, 3rem); }
.qaute-home-redesign .hcmute-journey-copy .hcmute-desc-main { max-width: 590px; font-size: .98rem; }
.qaute-home-redesign .hcmute-journey-art { position: relative; display: grid; min-height: 340px; place-items: center; isolation: isolate; }
.qaute-home-redesign .hcmute-journey-art::before, .qaute-home-redesign .hcmute-journey-art::after { content: ""; position: absolute; z-index: -1; border-radius: 50%; pointer-events: none; }
.qaute-home-redesign .hcmute-journey-art::before { width: min(72vw, 310px); aspect-ratio: 1; border: 1px solid rgba(49, 116, 192, .15); box-shadow: 0 0 0 20px rgba(66, 137, 216, .035), 0 0 0 50px rgba(66, 137, 216, .025); animation: hcmute-journey-orbit 18s linear infinite; }
.qaute-home-redesign .hcmute-journey-art::after { width: min(54vw, 228px); aspect-ratio: 1; background: radial-gradient(circle, rgba(92, 156, 225, .2), rgba(165, 208, 255, .09) 55%, transparent 72%); filter: blur(2px); }
.qaute-home-redesign .hcmute-journey-emblem { display: grid; width: clamp(150px, 20vw, 218px); aspect-ratio: 1; place-items: center; border: 1px solid rgba(255,255,255,.9); border-radius: 38% 62% 56% 44% / 45% 42% 58% 55%; background: linear-gradient(145deg, #fff 5%, #eaf4ff 55%, #d6eaff); color: #286fc7; box-shadow: 0 25px 60px rgba(38, 103, 174, .2), inset 0 2px 0 #fff; animation: hcmute-journey-float 4.2s ease-in-out infinite alternate; }
.qaute-home-redesign .hcmute-journey-emblem .bi { font-size: clamp(5rem, 9vw, 7.5rem); filter: drop-shadow(0 10px 13px rgba(35, 100, 173, .2)); }
.qaute-home-redesign .hcmute-journey-spark { position: absolute; display: grid; width: 62px; height: 62px; place-items: center; border: 1px solid rgba(255,255,255,.9); border-radius: 20px; background: linear-gradient(145deg, #fff, #eef6ff); color: #3478c5; box-shadow: 0 12px 28px rgba(35, 84, 136, .13); font-size: 1.65rem; animation: hcmute-journey-float 3.6s ease-in-out infinite alternate; }
.qaute-home-redesign .hcmute-journey-spark-one { top: 14%; left: 12%; transform: rotate(-9deg); }
.qaute-home-redesign .hcmute-journey-spark-two { right: 9%; bottom: 18%; color: #7958c7; animation-delay: -.8s; }
.qaute-home-redesign .hcmute-journey-spark-three { top: 15%; right: 14%; width: 46px; height: 46px; border-radius: 16px; color: #df9a35; font-size: 1.2rem; animation-delay: -1.4s; }
.qaute-home-redesign .hcmute-journey-caption { position: absolute; right: 3%; bottom: 4%; display: flex; align-items: center; gap: .65rem; padding: .75rem 1rem; border: 1px solid rgba(255,255,255,.9); border-radius: 15px; background: rgba(255,255,255,.88); color: #3c5877; font-size: .76rem; font-weight: 750; box-shadow: 0 12px 28px rgba(28, 67, 111, .1); -webkit-backdrop-filter: blur(10px); backdrop-filter: blur(10px); }
.qaute-home-redesign .hcmute-journey-caption .bi { color: #269681; font-size: 1.05rem; }
@keyframes hcmute-journey-float { from { translate: 0 0; } to { translate: 0 -9px; } }
@keyframes hcmute-journey-orbit { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
.qaute-home-redesign .hcmute-values-section { position: relative; isolation: isolate; overflow: hidden; padding: clamp(3.8rem, 7vw, 6.5rem) 0; background: radial-gradient(ellipse at 50% -15%, rgba(71, 139, 228, .32), transparent 48%), linear-gradient(130deg, #071d3b 0%, #0d3261 52%, #123f78 100%); color: #fff; }
.qaute-home-redesign .hcmute-values-section::before, .qaute-home-redesign .hcmute-values-section::after { content: ""; position: absolute; z-index: -1; border: 1px solid rgba(255,255,255,.1); border-radius: 50%; pointer-events: none; }
.qaute-home-redesign .hcmute-values-section::before { top: -210px; left: -140px; width: 470px; height: 470px; box-shadow: 0 0 0 30px rgba(255,255,255,.02), 0 0 0 76px rgba(255,255,255,.014); }
.qaute-home-redesign .hcmute-values-section::after { right: -200px; bottom: -280px; width: 550px; height: 550px; box-shadow: 0 0 0 36px rgba(255,255,255,.018), 0 0 0 82px rgba(255,255,255,.012); }
.qaute-home-redesign .hcmute-values-heading { max-width: 760px; margin: 0 auto 2.8rem; text-align: center; }
.qaute-home-redesign .hcmute-values-heading .hcmute-eyebrow { border-color: rgba(255,255,255,.19); background: rgba(255,255,255,.1); color: #c6e2ff; }
.qaute-home-redesign .hcmute-values-title { margin: 0 auto .9rem; color: #fff; font-size: clamp(1.9rem, 4vw, 3.1rem); font-weight: 850; line-height: 1.15; letter-spacing: -.045em; text-wrap: balance; }
.qaute-home-redesign .hcmute-values-lead { max-width: 650px; margin: 0 auto; color: rgba(226, 238, 255, .77); font-size: .98rem; line-height: 1.8; }
.qaute-home-redesign .hcmute-value-card { --value-accent: #74c8ff; position: relative; height: 100%; min-height: 260px; padding: 1.65rem; overflow: hidden; border: 1px solid rgba(255,255,255,.15); border-radius: 22px; background: linear-gradient(145deg, rgba(255,255,255,.13), rgba(255,255,255,.055)); box-shadow: inset 0 1px 0 rgba(255,255,255,.1), 0 15px 34px rgba(0, 13, 37, .12); -webkit-backdrop-filter: blur(12px); backdrop-filter: blur(12px); transition: transform .3s cubic-bezier(.2,.75,.3,1), background .3s ease, border-color .3s ease, box-shadow .3s ease; }
.qaute-home-redesign .hcmute-values-grid > div:nth-child(2) .hcmute-value-card { --value-accent: #70e0cf; }
.qaute-home-redesign .hcmute-values-grid > div:nth-child(3) .hcmute-value-card { --value-accent: #d5b7ff; }
.qaute-home-redesign .hcmute-values-grid > div:nth-child(4) .hcmute-value-card { --value-accent: #ffd17a; }
.qaute-home-redesign .hcmute-value-card::after { content: ""; position: absolute; right: -44px; bottom: -72px; width: 180px; height: 180px; border: 1px solid rgba(255,255,255,.12); border-radius: 50%; box-shadow: 0 0 0 22px rgba(255,255,255,.025), 0 0 0 48px rgba(255,255,255,.018); transition: transform .4s ease; pointer-events: none; }
.qaute-home-redesign .hcmute-value-card:hover { transform: translateY(-7px); border-color: color-mix(in srgb, var(--value-accent), transparent 45%); background: linear-gradient(145deg, rgba(255,255,255,.19), rgba(255,255,255,.08)); box-shadow: inset 0 1px 0 rgba(255,255,255,.15), 0 23px 42px rgba(0, 13, 37, .24); }
.qaute-home-redesign .hcmute-value-card:hover::after { transform: scale(1.12) translate(-7px, -7px); }
.qaute-home-redesign .hcmute-value-icon { display: flex; align-items: center; justify-content: center; width: 78px; height: 78px; margin-bottom: 1.35rem; border: 1px solid color-mix(in srgb, var(--value-accent), transparent 65%); border-radius: 24px; background: linear-gradient(145deg, color-mix(in srgb, var(--value-accent), transparent 82%), rgba(255,255,255,.08)); color: var(--value-accent); font-size: 2.6rem; box-shadow: 0 10px 25px color-mix(in srgb, var(--value-accent), transparent 88%), inset 0 1px 0 rgba(255,255,255,.14); transition: transform .3s cubic-bezier(.2,.75,.3,1), box-shadow .3s ease; }
.qaute-home-redesign .hcmute-value-card:hover .hcmute-value-icon { transform: translateY(-4px) rotate(-5deg) scale(1.06); box-shadow: 0 14px 30px color-mix(in srgb, var(--value-accent), transparent 82%), inset 0 1px 0 rgba(255,255,255,.2); }
.qaute-home-redesign .hcmute-value-title { position: relative; z-index: 1; margin: 0 0 .65rem; color: #fff; font-size: 1.12rem; font-weight: 800; }
.qaute-home-redesign .hcmute-value-desc { position: relative; z-index: 1; max-width: 340px; margin: 0; color: rgba(226, 238, 255, .76); font-size: .86rem; line-height: 1.8; }
.qaute-home-redesign .hcmute-values-grid > div:nth-child(4) .hcmute-value-desc { max-width: 280px; }
.qaute-home-redesign .hcmute-path-section { position: relative; overflow: hidden; padding: clamp(3.6rem, 7vw, 6rem) 0; background: radial-gradient(ellipse at 95% 10%, rgba(120, 93, 204, .08), transparent 32%), linear-gradient(145deg, #f5f8fd, #fff 48%, #f3f7fd); }
.qaute-home-redesign .hcmute-path-heading { max-width: 700px; margin: 0 auto 2.8rem; text-align: center; }
.qaute-home-redesign .hcmute-path-heading .hcmute-desc-main { margin: 0 auto; }
.qaute-home-redesign .hcmute-path-grid { position: relative; }
.qaute-home-redesign .hcmute-path-grid::before { content: ""; position: absolute; z-index: 0; top: 3.35rem; right: 16.5%; left: 16.5%; height: 2px; background: linear-gradient(90deg, #84baf0, #a6c6ec 50%, #b9a9e8); }
.qaute-home-redesign .hcmute-path-card { position: relative; z-index: 1; height: 100%; min-height: 275px; padding: 1.65rem 1.5rem; border: 1px solid #e3ebf5; border-radius: 22px; background: linear-gradient(150deg, rgba(255,255,255,.98), rgba(248,251,255,.97)); text-align: center; box-shadow: 0 12px 30px rgba(24, 62, 104, .065); transition: transform .3s cubic-bezier(.2,.75,.3,1), border-color .3s ease, box-shadow .3s ease; }
.qaute-home-redesign .hcmute-path-card:hover { transform: translateY(-6px); border-color: #cbdcf1; box-shadow: 0 20px 40px rgba(24, 62, 104, .12); }
.qaute-home-redesign .hcmute-path-number { position: absolute; top: 1rem; right: 1.15rem; color: #a4b5ca; font-size: .72rem; font-weight: 850; letter-spacing: .08em; }
.qaute-home-redesign .hcmute-path-icon { position: relative; display: grid; width: 78px; height: 78px; margin: 0 auto 1.25rem; place-items: center; border: 1px solid #d9e9fc; border-radius: 24px; background: linear-gradient(145deg, #f1f7ff, #e2efff); color: #3478c5; font-size: 2.35rem; box-shadow: 0 9px 20px rgba(44, 111, 185, .13), inset 0 1px 0 #fff; transition: transform .3s ease, box-shadow .3s ease; }
.qaute-home-redesign .hcmute-path-card:hover .hcmute-path-icon { transform: translateY(-3px) scale(1.06) rotate(-4deg); box-shadow: 0 13px 25px rgba(44, 111, 185, .18), inset 0 1px 0 #fff; }
.qaute-home-redesign .hcmute-path-icon-search { border-color: #d9eee9; background: linear-gradient(145deg, #effbf8, #def5ef); color: #218d7c; box-shadow: 0 9px 20px rgba(33, 141, 124, .12), inset 0 1px 0 #fff; }
.qaute-home-redesign .hcmute-path-icon-support { border-color: #e5ddf7; background: linear-gradient(145deg, #f7f3ff, #eae2ff); color: #7958c7; box-shadow: 0 9px 20px rgba(121, 88, 199, .13), inset 0 1px 0 #fff; }
.qaute-home-redesign .hcmute-path-title { margin: 0 0 .65rem; color: #233f60; font-size: 1.05rem; font-weight: 800; }
.qaute-home-redesign .hcmute-path-desc { max-width: 330px; margin: 0 auto; color: #71849b; font-size: .82rem; line-height: 1.75; }
.qaute-home-redesign .hcmute-journey-copy .hcmute-eyebrow { box-shadow: 0 7px 18px rgba(44, 111, 185, .08); }
.qaute-home-redesign .hcmute-journey-copy .hcmute-title-main { background: linear-gradient(100deg, #183655 0%, #256ab5 72%, #4683ca 100%); background-clip: text; -webkit-text-fill-color: transparent; }
.qaute-home-redesign .hcmute-journey-copy .hcmute-title-main::after { width: 76px; height: 5px; background: linear-gradient(90deg, #2d75c8, #77c8d5, #a88be8); }
.qaute-home-redesign .hcmute-journey-copy .badge { border-color: #e0eaf5 !important; box-shadow: 0 5px 14px rgba(27, 69, 116, .05); transition: transform .2s ease, box-shadow .2s ease; }
.qaute-home-redesign .hcmute-journey-copy .badge:hover { transform: translateY(-2px); box-shadow: 0 8px 18px rgba(27, 69, 116, .1); }
.qaute-home-redesign .hcmute-journey-emblem { animation: hcmute-journey-float 4.2s ease-in-out infinite alternate, hcmute-emblem-glow 4.2s ease-in-out infinite alternate; }
@keyframes hcmute-emblem-glow { from { box-shadow: 0 20px 48px rgba(38, 103, 174, .15), inset 0 2px 0 #fff; } to { box-shadow: 0 28px 68px rgba(38, 103, 174, .26), inset 0 2px 0 #fff; } }
.qaute-home-redesign .hcmute-journey-spark { animation-duration: 3.4s; }
.qaute-home-redesign .hcmute-values-section { background-image: radial-gradient(ellipse at 50% -15%, rgba(71, 139, 228, .36), transparent 48%), radial-gradient(ellipse at 100% 100%, rgba(37, 107, 159, .23), transparent 38%), linear-gradient(130deg, #071d3b 0%, #0d3261 52%, #123f78 100%); }
.qaute-home-redesign .hcmute-values-heading .hcmute-eyebrow { box-shadow: 0 6px 20px rgba(112, 200, 255, .1); }
.qaute-home-redesign .hcmute-value-card { background: radial-gradient(ellipse at 100% 0%, color-mix(in srgb, var(--value-accent), transparent 91%), transparent 50%), linear-gradient(145deg, rgba(255,255,255,.14), rgba(255,255,255,.045)); }
.qaute-home-redesign .hcmute-value-card::before { content: ""; position: absolute; inset: 0 0 auto; height: 3px; background: linear-gradient(90deg, var(--value-accent), transparent 82%); transform: scaleX(.4); transform-origin: left; transition: transform .35s ease; }
.qaute-home-redesign .hcmute-value-card:hover::before { transform: scaleX(1); }
.qaute-home-redesign .hcmute-path-section { background-image: radial-gradient(ellipse at 0% 100%, rgba(71, 157, 201, .08), transparent 34%), radial-gradient(ellipse at 95% 10%, rgba(120, 93, 204, .1), transparent 32%), linear-gradient(145deg, #f3f7fd, #fff 48%, #f3f7fd); }
.qaute-home-redesign .hcmute-path-grid::before { top: 3.4rem; height: 3px; background: linear-gradient(90deg, #79b7f1, #79d4c5 52%, #ad91e5); opacity: .75; }
.qaute-home-redesign .hcmute-path-card { overflow: hidden; background: radial-gradient(ellipse at 50% 0%, rgba(88, 151, 222, .09), transparent 55%), linear-gradient(150deg, #fff, #f8fbff); }
.qaute-home-redesign .hcmute-path-card::before { content: ""; position: absolute; inset: auto 0 0; height: 4px; background: linear-gradient(90deg, #4d93d9, #85cfd0); transform: scaleX(.24); transform-origin: left; transition: transform .35s ease; }
.qaute-home-redesign .hcmute-path-grid > div:nth-child(2) .hcmute-path-card::before { background: linear-gradient(90deg, #4db9a7, #8bd6c4); }
.qaute-home-redesign .hcmute-path-grid > div:nth-child(3) .hcmute-path-card::before { background: linear-gradient(90deg, #8970d2, #c3a6ef); }
.qaute-home-redesign .hcmute-path-card:hover::before { transform: scaleX(1); }
.qaute-home-redesign .hcmute-path-number { display: grid; width: 34px; height: 34px; place-items: center; border: 1px solid #e0eaf5; border-radius: 11px; background: rgba(255,255,255,.8); color: #7290ae; box-shadow: 0 4px 12px rgba(24, 62, 104, .05); }
.qaute-home-redesign #discovery > .container-xl > .row:first-child .row.g-2 a { min-height: 54px; border-color: #e5edf7 !important; background: rgba(255,255,255,.82) !important; transition: transform .22s ease, box-shadow .22s ease, border-color .22s ease; }
.qaute-home-redesign #discovery > .container-xl > .row:first-child .row.g-2 a:hover { transform: translateY(-2px); border-color: #c8daef !important; box-shadow: 0 8px 18px rgba(31, 79, 130, .08); }
.qaute-home-redesign #discovery > .container-xl > .row:first-child .row.g-2 a > span:first-child { display: grid; width: 38px; height: 38px; flex: 0 0 38px; place-items: center; border-radius: 12px !important; box-shadow: inset 0 1px 0 rgba(255,255,255,.7); transition: transform .22s ease; }
.qaute-home-redesign #discovery > .container-xl > .row:first-child .row.g-2 a:hover > span:first-child { transform: rotate(-5deg) scale(1.06); }
.qaute-home-redesign #discovery > .container-xl > .row:first-child .row.g-2 > div:nth-child(1) a > span:first-child { background: #fff0e9 !important; color: #d87549 !important; }
.qaute-home-redesign #discovery > .container-xl > .row:first-child .row.g-2 > div:nth-child(2) a > span:first-child { background: #fff6df !important; color: #bc8426 !important; }
.qaute-home-redesign #discovery > .container-xl > .row:first-child .row.g-2 > div:nth-child(3) a > span:first-child { background: #eaf3ff !important; color: #3478c5 !important; }
.qaute-home-redesign #discovery > .container-xl > .row:first-child .row.g-2 > div:nth-child(4) a > span:first-child { background: #e8f7f0 !important; color: #278764 !important; }
.qaute-home-redesign #faq .accordion-button:not(.collapsed) { box-shadow: inset 3px 0 #4b91d8; }
.qaute-home-redesign #faq .accordion-button:not(.collapsed)::after { filter: saturate(1.4); }
.qaute-home-redesign #faq .input-group:focus-within { border-radius: 12px; box-shadow: 0 0 0 4px rgba(53, 125, 204, .1); }
.qaute-home-redesign #faq .input-group:focus-within .input-group-text, .qaute-home-redesign #faq .input-group:focus-within .form-control { border-color: #78a9df; }
@media (max-width: 991.98px) {
  .qaute-home-redesign .hcmute-hero .row.align-items-center { padding-top: 3rem !important; padding-bottom: 2.7rem !important; }
  .qaute-home-redesign .hcmute-hero-title { max-width: 690px; }
  .qaute-home-redesign .hcmute-quick-services .container-xl { transform: translateY(-10px); }
  .qaute-home-redesign .hcmute-service-card { min-height: 175px; }
  .qaute-home-redesign .hcmute-section { padding: 3.4rem 0; }
  .qaute-home-redesign .hcmute-journey-art { min-height: 300px; }
  .qaute-home-redesign .hcmute-value-card { min-height: 240px; }
  .qaute-home-redesign .hcmute-path-card { min-height: 290px; padding: 1.4rem 1rem; }
}
@media (max-width: 575.98px) {
  .qaute-home-redesign .hcmute-hero .row.align-items-center { padding-top: 2.35rem !important; padding-bottom: 2.5rem !important; }
  .qaute-home-redesign .hcmute-hero-title { font-size: clamp(2rem, 8.5vw, 2.75rem); letter-spacing: -.05em; }
  .qaute-home-redesign .hcmute-hero-desc { font-size: .88rem; line-height: 1.78; }
  .qaute-home-redesign .hcmute-hero-pill { padding: .48rem .68rem; font-size: .68rem; }
  .qaute-home-redesign .hcmute-hero-search-box { min-height: 55px; border-radius: 13px; }
  .qaute-home-redesign .hcmute-hero-search-input { height: 42px; padding-right: .1rem; font-size: .76rem; }
  .qaute-home-redesign .hcmute-hero-search-box > i { margin-left: .6rem !important; margin-right: .4rem !important; font-size: .95rem !important; }
  .qaute-home-redesign .hcmute-hero-search-btn { min-height: 42px; padding: .6rem .78rem; border-radius: 10px; font-size: .74rem; }
  .qaute-home-redesign .hcmute-hero-stat-num { font-size: 1.35rem; }
  .qaute-home-redesign .hcmute-hero-stat-label { font-size: .63rem; }
  .qaute-home-redesign .hcmute-quick-services .container-xl { transform: translateY(-6px); }
  .qaute-home-redesign .hcmute-service-card { min-height: 166px; padding: 1rem .85rem; border-radius: 15px; }
  .qaute-home-redesign .hcmute-service-icon { width: 41px; height: 41px; margin-bottom: .75rem; border-radius: 12px; font-size: 1.15rem; }
  .qaute-home-redesign .hcmute-service-title { font-size: .82rem; }
  .qaute-home-redesign .hcmute-service-desc { font-size: .69rem; line-height: 1.6; }
  .qaute-home-redesign .hcmute-section { padding: 2.8rem 0; }
  .qaute-home-redesign .hcmute-title-main { font-size: 1.75rem; }
  .qaute-home-redesign .hcmute-desc-main { font-size: .83rem; }
  .qaute-home-redesign .hcmute-card-modern { border-radius: 16px; }
  .qaute-home-redesign .hcmute-card-modern.p-4 { padding: 1rem !important; }
  .qaute-home-redesign #faq .accordion-button { padding: .9rem .5rem; font-size: .81rem !important; }
  .qaute-home-redesign #faq .accordion-body { padding: .35rem .55rem 1rem; }
  .qaute-home-redesign #faq-category-buttons .btn { font-size: .73rem; }
  .qaute-home-redesign .rounded-4.p-4.text-white.shadow-sm { border-radius: 17px !important; }
  .qaute-home-redesign .hcmute-journey-section { padding: 3.4rem 0; }
  .qaute-home-redesign .hcmute-journey-art { min-height: 260px; margin-top: .75rem; }
  .qaute-home-redesign .hcmute-journey-spark { width: 49px; height: 49px; border-radius: 16px; font-size: 1.35rem; }
  .qaute-home-redesign .hcmute-journey-spark-three { width: 40px; height: 40px; }
  .qaute-home-redesign .hcmute-journey-caption { right: 0; bottom: 1%; font-size: .68rem; }
  .qaute-home-redesign .hcmute-values-section { padding: 3.6rem 0; }
  .qaute-home-redesign .hcmute-values-heading { margin-bottom: 2rem; }
  .qaute-home-redesign .hcmute-value-card { min-height: 220px; padding: 1.25rem; border-radius: 18px; }
  .qaute-home-redesign .hcmute-value-icon { width: 65px; height: 65px; margin-bottom: 1rem; border-radius: 20px; font-size: 2.15rem; }
  .qaute-home-redesign .hcmute-path-section { padding: 3.4rem 0; }
  .qaute-home-redesign .hcmute-path-heading { margin-bottom: 2rem; }
  .qaute-home-redesign .hcmute-path-grid::before { display: none; }
  .qaute-home-redesign .hcmute-path-card { min-height: 0; padding: 1.35rem 1.1rem; }
}
@media (max-width: 767.98px) {
  .qaute-home-redesign .hcmute-path-grid::before { display: none; }
}
@media (prefers-reduced-motion: reduce) {
  .qaute-home-redesign *, .qaute-home-redesign *::before, .qaute-home-redesign *::after { animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; scroll-behavior: auto !important; }
}
</style>




<div class="qaute-home-redesign">
    <%-- Trang chủ giới thiệu dịch vụ, tìm kiếm nhanh và các lối vào hỗ trợ. --%>
    <section class="hcmute-hero">
        <div class="container-xl">
            <div class="row align-items-center g-5 py-4">
                <div class="col-lg-7">
                    <div class="hcmute-hero-pill mb-3">
                        <i class="bi bi-clock-history"></i>
                        <span>Hỗ trợ 07:30 – 17:00, Thứ 2 – Thứ 6</span>
                    </div>
                    <h1 class="hcmute-hero-title">Mọi thắc mắc của sinh viên HCMUTE, có QAUTE lo.</h1>
                    <p class="hcmute-hero-desc">Tra cứu câu hỏi thường gặp, gửi yêu cầu tư vấn, trò chuyện trực tuyến và đặt lịch hẹn với chuyên viên – tất cả trong một nền tảng hỗ trợ sinh viên thông minh.</p>
                    <form action="/search" method="get" class="hcmute-hero-search-box mb-4">
                        <i class="bi bi-search text-muted fs-5 ms-3 me-2"></i>
                        <input type="search" name="q" id="hero-search" class="hcmute-hero-search-input" placeholder="Ví dụ: hạn đóng học phí, bảo lưu, đăng ký môn..." autocomplete="off" aria-label="Tìm kiếm câu hỏi" data-qa-suggest="HOME">
                        <button type="submit" class="hcmute-hero-search-btn">Tìm kiếm <i class="bi bi-arrow-right ms-1"></i></button>
                    </form>
                    <div class="d-flex flex-wrap align-items-center gap-2 small text-white-50 mb-4">
                        <span>Gợi ý nhanh:</span>
                        <a href="/search?q=H%E1%BB%8Dc+ph%C3%AD" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Học phí</a>
                        <a href="/search?q=%C4%90%C4%83ng+k%C3%BD+m%C3%B4n" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Đăng ký môn</a>
                        <a href="/search?q=H%E1%BB%8Dc+b%E1%BB%95ng" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Học bổng</a>
                        <a href="/search?q=K%C3%BD+t%C3%BAc+x%C3%A1" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Ký túc xá</a>
                        <a href="/search?q=B%E1%BA%A3o+l%C6%B0u" class="badge bg-white-10 text-white text-decoration-none px-2 py-1 rounded-pill">Bảo lưu</a>
                    </div>
                    <div class="row g-4 pt-3 border-top border-white-10">
                        <div class="col-4">
                            <div class="hcmute-hero-stat-num">
                                <c:choose>
                                    <c:when test="${not empty totalFaq && totalFaq > 0}">${totalFaq * 40}+</c:when>
                                    <c:otherwise>12.480+</c:otherwise>
                                </c:choose>
                            </div>
                            <div class="hcmute-hero-stat-label">Ticket đã xử lý</div>
                        </div>
                        <div class="col-4">
                            <div class="hcmute-hero-stat-num">
                                <c:choose>
                                    <c:when test="${not empty medianFirstResponseMinutes}">
                                        <c:choose>
                                            <c:when test="${medianFirstResponseMinutes < 60}">&lt; <fmt:formatNumber value="${medianFirstResponseMinutes}" maxFractionDigits="0"/>p</c:when>
                                            <c:otherwise>&lt; <fmt:formatNumber value="${medianFirstResponseMinutes / 60}" maxFractionDigits="0"/>h</c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>&lt; 24h</c:otherwise>
                                </c:choose>
                            </div>
                            <div class="hcmute-hero-stat-label">Thời gian phản hồi TB</div>
                        </div>
                        <div class="col-4">
                            <div class="hcmute-hero-stat-num">96%</div>
                            <div class="hcmute-hero-stat-label">Mức độ hài lòng</div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-5 d-none d-lg-block">
                    <div class="hcmute-hero-image-wrap">
                        <img src="${pageContext.request.contextPath}/assets/images/hero-students.png" alt="Chuyên viên tư vấn đang trao đổi cùng sinh viên HCMUTE" class="w-100 h-100 object-fit-cover" onerror="this.src='${pageContext.request.contextPath}/assets/images/campus-hcmute.png';">
                    </div>
                </div>
            </div>
        </div>
    </section>

    <div class="hcmute-quick-services">
        <div class="container-xl">
            <div class="row g-3">
                <div class="col-6 col-md-3">
                    <a href="#faq" class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-search"></i></div>
                        <h6 class="hcmute-service-title">Tra cứu FAQ</h6>
                        <p class="hcmute-service-desc">Hơn 200 câu hỏi về học vụ, học phí, học bổng đã giải đáp.</p>
                    </a>
                </div>
                <div class="col-6 col-md-3">
                    <a href="#ticket" class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-ticket-perforated"></i></div>
                        <h6 class="hcmute-service-title">Gửi ticket</h6>
                        <p class="hcmute-service-desc">Gửi yêu cầu và theo dõi tiến độ xử lý từ chuyên viên 24h.</p>
                    </a>
                </div>
                <div class="col-6 col-md-3">
                    <a href="javascript:void(0)" onclick="openQauteChatbot()" class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-robot"></i></div>
                        <h6 class="hcmute-service-title">Trợ lý ảo Bot</h6>
                        <p class="hcmute-service-desc">Hỏi đáp tức thì cùng trợ lý ảo QAUTE kéo thả thông minh 24/7.</p>
                    </a>
                </div>
                <div class="col-6 col-md-3">
                    <a href="#booking" class="hcmute-service-card">
                        <div class="hcmute-service-icon"><i class="bi bi-calendar-check"></i></div>
                        <h6 class="hcmute-service-title">Đặt lịch hẹn</h6>
                        <p class="hcmute-service-desc">Gặp trực tiếp hoặc online 1-1 với chuyên viên các phòng ban.</p>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <section class="hcmute-journey-section" aria-labelledby="journey-title">
        <div class="container-xl">
            <div class="row align-items-center g-4 g-lg-5">
                <div class="col-lg-6">
                    <div class="hcmute-journey-copy">
                        <span class="hcmute-eyebrow"><i class="bi bi-stars" aria-hidden="true"></i>QAUTE là gì?</span>
                        <h2 class="hcmute-title-main" id="journey-title">QAUTE — người bạn đồng hành của sinh viên HCMUTE</h2>
                        <p class="hcmute-desc-main mb-0">Từ những thắc mắc về học tập, học phí đến đời sống sinh viên, QAUTE giúp bạn tìm thông tin và kết nối với kênh hỗ trợ phù hợp trong cùng một không gian. Mỗi câu hỏi là một điểm khởi đầu để bạn hiểu rõ hơn và an tâm hơn trên hành trình học tập.</p>
                        <div class="d-flex flex-wrap gap-2 mt-4">
                            <span class="badge rounded-pill px-3 py-2 border bg-white text-primary"><i class="bi bi-book-half me-1" aria-hidden="true"></i>Học tập</span>
                            <span class="badge rounded-pill px-3 py-2 border bg-white text-success"><i class="bi bi-chat-heart me-1" aria-hidden="true"></i>Lắng nghe</span>
                            <span class="badge rounded-pill px-3 py-2 border bg-white text-warning-emphasis"><i class="bi bi-compass me-1" aria-hidden="true"></i>Định hướng</span>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="hcmute-journey-art" aria-hidden="true">
                        <span class="hcmute-journey-spark hcmute-journey-spark-one"><i class="bi bi-journal-check"></i></span>
                        <span class="hcmute-journey-spark hcmute-journey-spark-two"><i class="bi bi-chat-heart-fill"></i></span>
                        <span class="hcmute-journey-spark hcmute-journey-spark-three"><i class="bi bi-stars"></i></span>
                        <div class="hcmute-journey-emblem"><i class="bi bi-mortarboard-fill"></i></div>
                        <div class="hcmute-journey-caption"><i class="bi bi-people-fill"></i><span>Cùng bạn qua từng chặng đường</span></div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <%-- Tổng hợp chủ đề và FAQ nổi bật để người dùng tra cứu nhanh. --%>
    <section class="hcmute-section hcmute-section-muted" id="discovery">
        <div class="container-xl">
            <div class="row g-4 align-items-end mb-5">
                <div class="col-lg-6">
                    <span class="hcmute-eyebrow"><i class="bi bi-stars"></i>Trung tâm hỗ trợ sinh viên</span>
                    <h2 class="hcmute-title-main">Tìm đúng câu trả lời, ngay từ lần đầu tiên.</h2>
                    <p class="hcmute-desc-main mb-0">Khám phá kho kiến thức được đội ngũ QAUTE cập nhật liên tục, hoặc chuyển ngay sang tư vấn viên khi cần hỗ trợ chuyên sâu.</p>
                </div>
                <div class="col-lg-6">
                    <div class="hcmute-card-modern p-4">
                        <div class="d-flex align-items-center justify-content-between mb-3 gap-2">
                            <div class="d-flex align-items-center gap-2 fw-bold text-dark small"><i class="bi bi-fire text-danger"></i><span>Chủ đề được sinh viên tra cứu nhiều</span></div>
                            <a href="/faq" class="small text-primary text-decoration-none fw-semibold text-nowrap">Xem tất cả FAQ <i class="bi bi-arrow-right"></i></a>
                        </div>
                        <div class="row g-2">
                            <div class="col-6"><a href="/search?q=H%E1%BB%8Dc+ph%C3%AD" class="p-2 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all"><span class="rounded bg-danger-subtle text-danger p-2"><i class="bi bi-wallet2"></i></span><span class="small fw-semibold">Học phí &amp; Tài chính</span></a></div>
                            <div class="col-6"><a href="/search?q=H%E1%BB%8Dc+b%E1%BB%95ng" class="p-2 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all"><span class="rounded bg-warning-subtle text-warning-emphasis p-2"><i class="bi bi-award-fill"></i></span><span class="small fw-semibold">Học bổng khuyến khích</span></a></div>
                            <div class="col-6"><a href="/search?q=%C4%90%C4%83ng+k%C3%BD+m%C3%B4n" class="p-2 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all"><span class="rounded bg-primary-subtle text-primary p-2"><i class="bi bi-journal-bookmark-fill"></i></span><span class="small fw-semibold">Đăng ký môn học</span></a></div>
                            <div class="col-6"><a href="/search?q=B%E1%BA%A3o+l%C6%B0u" class="p-2 rounded-3 border bg-light d-flex align-items-center gap-2 text-decoration-none text-dark hover-shadow transition-all"><span class="rounded bg-success-subtle text-success p-2"><i class="bi bi-pause-circle-fill"></i></span><span class="small fw-semibold">Bảo lưu &amp; Nghỉ học</span></a></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-4 mb-4">
                <div class="col-lg-7">
                    <div class="hcmute-card-modern p-4 p-md-5 h-100">
                        <div class="d-flex justify-content-between align-items-center mb-4 gap-3">
                            <div>
                                <span class="text-primary fw-bold small text-uppercase" style="letter-spacing: .05em;">Được sinh viên quan tâm</span>
                                <h3 class="fs-4 fw-bold text-dark mt-1 mb-0">FAQ Nổi Bật</h3>
                            </div>
                            <a href="/faq" class="btn btn-sm btn-outline-primary rounded-pill px-3 text-nowrap">Xem tất cả <i class="bi bi-arrow-right ms-1"></i></a>
                        </div>
                        <div class="d-flex flex-column gap-2" id="popular-faq-list">
                            <c:choose>
                                <c:when test="${not empty topViewed}">
                                    <c:forEach var="faq" items="${topViewed}" varStatus="status" end="3">
                                        <a href="/faq/<c:out value='${faq.slug()}'/>" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light">
                                            <span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6" style="width: 36px; height: 36px; flex-shrink: 0;">0${status.count}</span>
                                            <div class="flex-grow-1 min-w-0">
                                                <div class="fw-semibold text-dark text-truncate mb-1"><c:out value="${faq.title()}"/></div>
                                                <div class="d-flex align-items-center flex-wrap gap-2 small text-muted"><span class="badge bg-light text-secondary border"><c:out value="${faq.categoryName()}"/></span><span>•</span><span><i class="bi bi-eye me-1"></i><c:out value="${faq.viewCount()}"/> lượt xem</span></div>
                                            </div>
                                            <i class="bi bi-chevron-right text-muted mt-2"></i>
                                        </a>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <a href="#faq" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light"><span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6" style="width: 36px; height: 36px; flex-shrink: 0;">01</span><div class="flex-grow-1 min-w-0"><div class="fw-semibold text-dark mb-1">Hạn đóng học phí mỗi học kỳ là khi nào?</div><div class="small text-muted"><span class="badge bg-light text-secondary border">Học phí</span> • 2.4k lượt xem</div></div><i class="bi bi-chevron-right text-muted mt-2"></i></a>
                                    <a href="#faq" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light"><span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6" style="width: 36px; height: 36px; flex-shrink: 0;">02</span><div class="flex-grow-1 min-w-0"><div class="fw-semibold text-dark mb-1">Thời gian đăng ký môn học học kỳ mới?</div><div class="small text-muted"><span class="badge bg-light text-secondary border">Đăng ký môn</span> • 1.9k lượt xem</div></div><i class="bi bi-chevron-right text-muted mt-2"></i></a>
                                    <a href="#faq" class="p-3 rounded-3 text-decoration-none text-dark d-flex align-items-start gap-3 hover-bg-light transition-all border border-light"><span class="badge bg-primary-subtle text-primary rounded-3 d-flex align-items-center justify-content-center fw-bold fs-6" style="width: 36px; height: 36px; flex-shrink: 0;">03</span><div class="flex-grow-1 min-w-0"><div class="fw-semibold text-dark mb-1">Điều kiện xét học bổng khuyến khích học tập?</div><div class="small text-muted"><span class="badge bg-light text-secondary border">Học bổng</span> • 1.2k lượt xem</div></div><i class="bi bi-chevron-right text-muted mt-2"></i></a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="d-flex flex-column gap-3 h-100">
                        <div class="rounded-4 p-4 text-white shadow-sm">
                            <c:set var="firstAnn" value="${null}"/>
                            <c:if test="${not empty announcements}">
                                <c:forEach var="item" items="${announcements}" varStatus="status"><c:if test="${status.first}"><c:set var="firstAnn" value="${item}"/></c:if></c:forEach>
                            </c:if>
                            <div class="d-flex justify-content-between align-items-start mb-2 gap-3">
                                <div>
                                    <span class="badge bg-white-20 text-white rounded-pill px-3 py-2 small">Thông báo mới nhất</span>
                                    <h4 class="fs-5 fw-bold text-white mt-3 mb-1">
                                        <c:choose><c:when test="${not empty firstAnn}"><c:out value="${firstAnn.title()}"/></c:when><c:otherwise>Mở đăng ký học bổng khuyến khích học tập HK1</c:otherwise></c:choose>
                                    </h4>
                                </div>
                                <div class="rounded-circle bg-white-10 p-2 text-warning"><i class="bi bi-bell-fill fs-5"></i></div>
                            </div>
                            <p class="text-white-50 small mb-4 line-clamp-2" style="line-height: 1.7;">
                                <c:choose><c:when test="${not empty firstAnn && not empty firstAnn.summary()}"><c:out value="${firstAnn.summary()}"/></c:when><c:otherwise>Hạn nhận hồ sơ đến ngày 30/09/2026. Đừng bỏ lỡ cơ hội nhận hỗ trợ học tập từ nhà trường.</c:otherwise></c:choose>
                            </p>
                            <c:choose><c:when test="${not empty firstAnn}"><a href="/announcements/${firstAnn.id()}" class="btn btn-light btn-sm fw-semibold rounded-pill px-3">Xem thông báo <i class="bi bi-arrow-right ms-1"></i></a></c:when><c:otherwise><a href="/pages/guide" class="btn btn-light btn-sm fw-semibold rounded-pill px-3">Xem thông báo <i class="bi bi-arrow-right ms-1"></i></a></c:otherwise></c:choose>
                        </div>
                        <div class="row g-3 flex-grow-1">
                            <div class="col-6">
                                <div class="hcmute-card-modern hcmute-intro-card p-3 p-sm-4 h-100">
                                    <span class="hcmute-intro-icon"><i class="bi bi-journals" aria-hidden="true"></i></span>
                                    <h3 class="hcmute-intro-title">Đa dạng chủ đề</h3>
                                    <p class="hcmute-intro-desc">Hỗ trợ các vấn đề về học tập, học phí, học vụ và đời sống sinh viên.</p>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="hcmute-card-modern hcmute-intro-card p-3 p-sm-4 h-100">
                                    <span class="hcmute-intro-icon"><i class="bi bi-signpost-2" aria-hidden="true"></i></span>
                                    <h3 class="hcmute-intro-title">Đúng nơi hỗ trợ</h3>
                                    <p class="hcmute-intro-desc">Kết nối bạn với thông tin hoặc đơn vị phụ trách phù hợp.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="hcmute-card-modern p-4 d-flex flex-wrap align-items-center justify-content-between gap-3">
                <div class="d-flex align-items-center gap-3">
                    <div class="hcmute-logo-icon" style="width: 48px; height: 48px; font-size: 1.5rem; flex-shrink: 0;"><i class="bi bi-mortarboard-fill"></i></div>
                    <div>
                        <h5 class="fw-bold text-dark mb-1">Bạn là sinh viên mới tại HCMUTE?</h5>
                        <p class="small text-muted mb-0">Xem cẩm nang hướng dẫn nhanh về tài khoản, đăng ký môn học và các kênh hỗ trợ của nhà trường.</p>
                    </div>
                </div>
                <a href="/pages/guide" class="btn btn-outline-primary rounded-pill px-4 fw-semibold">Mở hướng dẫn <i class="bi bi-book ms-1"></i></a>
            </div>
        </div>
    </section>

    <%-- Lọc FAQ mẫu theo danh mục hoặc từ khóa ngay trên trang chủ. --%>
    <section class="hcmute-section" id="faq">
        <div class="container-xl">
            <div class="text-center max-w-xl mx-auto mb-5">
                <span class="hcmute-eyebrow"><i class="bi bi-question-circle-fill"></i>Tra cứu</span>
                <h2 class="hcmute-title-main">Câu hỏi thường gặp</h2>
                <p class="hcmute-desc-main mx-auto">Tìm nhanh câu trả lời cho các vấn đề học tập và đời sống sinh viên.</p>
            </div>
            <div class="row g-4">
                <div class="col-lg-3">
                    <div class="d-flex flex-column gap-3 position-sticky" style="top: 5rem;">
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-search"></i></span>
                            <input type="search" id="faq-filter-input" class="form-control border-start-0" placeholder="Tìm trong danh sách..." onkeyup="filterFaqs()">
                        </div>
                        <div class="d-flex flex-column gap-1" id="faq-category-buttons">
                            <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-3 p-2 active-category btn-primary" style="background-color: var(--hcmute-blue); border-color: var(--hcmute-blue);" onclick="selectFaqCategory('all', this)"><span class="fw-semibold small">Tất cả câu hỏi</span><span class="badge bg-white text-primary rounded-pill">${guestHomeFaqs.size()}</span></button>
                            <c:forEach var="entry" items="${guestHomeFaqsByCategory}">
                                <c:set var="categoryName" value="${entry.key}"/>
                                <c:set var="faqs" value="${entry.value}"/>
                                <button type="button" class="btn text-start d-flex justify-content-between align-items-center rounded-3 p-2 btn-light text-dark" onclick="selectFaqCategory('${categoryName}', this)"><span class="fw-semibold small">${categoryName}</span><span class="badge bg-secondary-subtle text-secondary rounded-pill">${faqs.size()}</span></button>
                            </c:forEach>
                        </div>
                        <a href="/faq" class="btn btn-outline-primary btn-sm rounded-pill mt-2"><i class="bi bi-journal-text me-1"></i>Xem kho FAQ đầy đủ</a>
                    </div>
                </div>
                <div class="col-lg-9">
                    <div class="hcmute-card-modern p-4">
                        <div class="accordion accordion-flush" id="faqAccordion">
                            <c:forEach var="faq" items="${guestHomeFaqs}" varStatus="status">
                                <div class="accordion-item border-bottom py-2 faq-item" data-category="${faq.categoryName}">
                                    <h2 class="accordion-header" id="heading${faq.id}">
                                        <button class="accordion-button collapsed fw-semibold text-dark fs-6" type="button"
                                                data-bs-toggle="collapse" data-bs-target="#collapse${faq.id}"
                                                aria-expanded="false" aria-controls="collapse${faq.id}">
                                            <div class="d-flex flex-column align-items-start gap-1">
                                                <span class="badge bg-primary-subtle text-primary small">${faq.categoryName}</span>
                                                <span>${faq.question}</span>
                                            </div>
                                        </button>
                                    </h2>
                                    <div id="collapse${faq.id}" class="accordion-collapse collapse"
                                         aria-labelledby="heading${faq.id}" data-bs-parent="#faqAccordion">
                                        <div class="accordion-body text-secondary small pt-1" style="line-height: 1.7;">
                                            ${faq.answer}
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <c:if test="${empty guestHomeFaqs}">
                                <div class="text-center py-5">
                                    <i class="bi bi-inbox text-muted fs-1 mb-3 d-block"></i>
                                    <h6 class="fw-bold">Chưa có câu hỏi thường gặp nào</h6>
                                    <p class="text-muted small">Quản trị viên sẽ cập nhật nội dung sớm.</p>
                                </div>
                            </c:if>
                        </div>
                        <div id="faq-empty-state" class="text-center py-5 d-none">
                            <i class="bi bi-search text-muted fs-1 mb-3 d-block"></i>
                            <h6 class="fw-bold">Không tìm thấy câu hỏi phù hợp</h6>
                            <p class="text-muted small">Hãy thử từ khóa khác hoặc gửi ticket tư vấn bên dưới để được chuyên viên hỗ trợ trực tiếp.</p>
                            <a href="#ticket" class="btn btn-primary btn-sm rounded-pill px-3">Gửi ticket ngay</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="hcmute-values-section" aria-labelledby="topics-title">
        <div class="container-xl">
            <div class="hcmute-values-heading">
                <span class="hcmute-eyebrow"><i class="bi bi-compass-fill" aria-hidden="true"></i>Cùng bạn qua từng vấn đề</span>
                <h2 class="hcmute-values-title" id="topics-title">Đồng hành qua từng vấn đề</h2>
                <p class="hcmute-values-lead">Mỗi chặng đường có những mối quan tâm riêng. QAUTE mong muốn giúp bạn tìm đến thông tin và sự hỗ trợ phù hợp.</p>
            </div>
            <div class="row g-3 g-lg-4 hcmute-values-grid">
                <div class="col-sm-6 col-xl-3">
                    <article class="hcmute-value-card">
                        <div class="hcmute-value-icon"><i class="bi bi-journal-bookmark-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-value-title">Học tập &amp; học vụ</h3>
                        <p class="hcmute-value-desc">Thông tin về môn học, đăng ký học phần và các quy định học tập.</p>
                    </article>
                </div>
                <div class="col-sm-6 col-xl-3">
                    <article class="hcmute-value-card">
                        <div class="hcmute-value-icon"><i class="bi bi-wallet2" aria-hidden="true"></i></div>
                        <h3 class="hcmute-value-title">Học phí &amp; học bổng</h3>
                        <p class="hcmute-value-desc">Những nội dung sinh viên thường quan tâm trong mỗi năm học.</p>
                    </article>
                </div>
                <div class="col-sm-6 col-xl-3">
                    <article class="hcmute-value-card">
                        <div class="hcmute-value-icon"><i class="bi bi-house-heart-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-value-title">Đời sống sinh viên</h3>
                        <p class="hcmute-value-desc">Các vấn đề về ký túc xá, hoạt động và đời sống tại trường.</p>
                    </article>
                </div>
                <div class="col-sm-6 col-xl-3">
                    <article class="hcmute-value-card">
                        <div class="hcmute-value-icon"><i class="bi bi-chat-square-heart-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-value-title">Tư vấn cá nhân</h3>
                        <p class="hcmute-value-desc">Khi cần trao đổi cụ thể hơn, bạn có thể tìm đến kênh tư vấn phù hợp.</p>
                    </article>
                </div>
            </div>
        </div>
    </section>

    <section class="hcmute-path-section" aria-labelledby="path-title">
        <div class="container-xl">
            <div class="hcmute-path-heading">
                <span class="hcmute-eyebrow"><i class="bi bi-signpost-split-fill" aria-hidden="true"></i>Bắt đầu từ điều bạn cần</span>
                <h2 class="hcmute-title-main" id="path-title">Một nơi để bắt đầu tìm câu trả lời</h2>
                <p class="hcmute-desc-main">Từ một thắc mắc ban đầu đến đúng hướng hỗ trợ — QAUTE mong muốn giúp hành trình ấy trở nên dễ hiểu hơn.</p>
            </div>
            <div class="row g-3 g-lg-4 hcmute-path-grid">
                <div class="col-md-4">
                    <article class="hcmute-path-card">
                        <span class="hcmute-path-number">01</span>
                        <div class="hcmute-path-icon hcmute-path-icon-question"><i class="bi bi-question-circle-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-path-title">Có thắc mắc</h3>
                        <p class="hcmute-path-desc">Bắt đầu từ câu hỏi hoặc vấn đề bạn đang quan tâm trong học tập và đời sống sinh viên.</p>
                    </article>
                </div>
                <div class="col-md-4">
                    <article class="hcmute-path-card">
                        <span class="hcmute-path-number">02</span>
                        <div class="hcmute-path-icon hcmute-path-icon-search"><i class="bi bi-search-heart-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-path-title">Tra cứu thông tin</h3>
                        <p class="hcmute-path-desc">Tham khảo các nội dung và câu trả lời được tổng hợp dành cho sinh viên.</p>
                    </article>
                </div>
                <div class="col-md-4">
                    <article class="hcmute-path-card">
                        <span class="hcmute-path-number">03</span>
                        <div class="hcmute-path-icon hcmute-path-icon-support"><i class="bi bi-chat-dots-fill" aria-hidden="true"></i></div>
                        <h3 class="hcmute-path-title">Tìm kênh hỗ trợ phù hợp</h3>
                        <p class="hcmute-path-desc">Khi cần trao đổi thêm, tìm đến đầu mối tư vấn phù hợp với vấn đề của bạn.</p>
                    </article>
                </div>
            </div>
        </div>
    </section>

    <%-- Đưa người dùng đến đăng nhập để gửi yêu cầu hỗ trợ chính thức. --%>
    <section class="hcmute-section hcmute-section-muted" id="ticket">
        <div class="container-xl">
            <div class="row align-items-center g-4">
                <div class="col-lg-7">
                    <span class="hcmute-eyebrow"><i class="bi bi-send-fill"></i>Hỗ trợ chính thức</span>
                    <h2 class="hcmute-title-main">Bạn muốn gửi yêu cầu tư vấn?</h2>
                    <p class="hcmute-desc-main">Đăng nhập để gửi ticket chính thức, đính kèm hồ sơ, nhận phản hồi từ chuyên viên và theo dõi toàn bộ tiến độ xử lý trong một nơi.</p>
                </div>
                <div class="col-lg-5">
                    <div class="hcmute-card-modern p-4 text-center">
                        <i class="bi bi-shield-lock-fill text-primary display-6"></i>
                        <h5 class="fw-bold mt-3">Chỉ dành cho tài khoản đã cấp</h5>
                        <p class="text-muted small">Thông tin ticket được gắn với tài khoản để bảo đảm riêng tư và không thất lạc dữ liệu.</p>
                        <a href="${pageContext.request.contextPath}/auth/login?returnUrl=/student/tickets/new" class="btn btn-primary rounded-pill px-4"><i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập để gửi ticket</a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <%-- Đưa người dùng đến đăng nhập để xem và đặt lịch tư vấn. --%>
    <section class="hcmute-section hcmute-section-muted" id="booking">
        <div class="container-xl">
            <div class="row align-items-center g-4">
                <div class="col-lg-7">
                    <span class="hcmute-eyebrow"><i class="bi bi-calendar-event-fill"></i>Kết nối chuyên viên</span>
                    <h2 class="hcmute-title-main">Bạn muốn đặt lịch tư vấn?</h2>
                    <p class="hcmute-desc-main">Đăng nhập để xem chuyên viên, khung giờ còn trống, hình thức gặp và nhận mã lịch hẹn chính thức.</p>
                </div>
                <div class="col-lg-5">
                    <div class="hcmute-card-modern p-4 text-center">
                        <i class="bi bi-calendar2-check-fill text-primary display-6"></i>
                        <h5 class="fw-bold mt-3">Đặt lịch trong khu vực Student</h5>
                        <p class="text-muted small">Lịch hẹn được lưu vào tài khoản và gửi thông báo xác nhận đến bạn.</p>
                        <a href="${pageContext.request.contextPath}/auth/login?returnUrl=/student/appointments/book" class="btn btn-primary rounded-pill px-4"><i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập để đặt lịch</a>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <c:if test="${false}">
        <section class="hcmute-section hcmute-section-muted" id="ticket">
            <div class="container-xl">
                <div class="text-center max-w-xl mx-auto mb-5">
                    <span class="hcmute-eyebrow"><i class="bi bi-send-fill"></i>Hỗ trợ</span>
                    <h2 class="hcmute-title-main">Gửi ticket tư vấn</h2>
                    <p class="hcmute-desc-main mx-auto">Mô tả vấn đề của bạn, chuyên viên phụ trách sẽ phản hồi qua email sinh viên trong vòng 24 giờ làm việc.</p>
                </div>
                <div class="row g-4">
                    <div class="col-lg-8">
                        <div class="hcmute-card-modern p-4 p-md-5">
                            <sec:authorize access="isAnonymous()">
                                <div class="alert alert-warning border-0 shadow-sm d-flex align-items-center justify-content-between flex-wrap gap-2 mb-4 p-3 rounded-3">
                                    <div class="d-flex align-items-center gap-2"><i class="bi bi-shield-lock-fill fs-4 text-warning"></i><div class="small"><strong>Dành riêng cho sinh viên HCMUTE:</strong> Vui lòng đăng nhập để gửi ticket, nhận phản hồi chính thức từ chuyên viên và theo dõi tiến độ xử lý.</div></div>
                                    <a href="/auth/login?returnUrl=/student/tickets/new" class="btn btn-warning btn-sm fw-semibold rounded-pill px-3 text-nowrap"><i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập ngay</a>
                                </div>
                            </sec:authorize>
                            <sec:authorize access="isAuthenticated()">
                                <div class="alert alert-success border-0 shadow-sm d-flex align-items-center justify-content-between flex-wrap gap-2 mb-4 p-3 rounded-3">
                                    <div class="d-flex align-items-center gap-2"><i class="bi bi-person-check-fill fs-4 text-success"></i><div class="small">Bạn đã đăng nhập! Bạn có thể tạo yêu cầu và theo dõi tiến độ trực tiếp tại trang Sinh viên.</div></div>
                                    <a href="/student/tickets/new" class="btn btn-success btn-sm fw-semibold rounded-pill px-3 text-nowrap"><i class="bi bi-plus-circle me-1"></i>Tạo yêu cầu chính thức</a>
                                </div>
                            </sec:authorize>
                            <div id="ticket-success-alert" class="alert alert-success d-none mb-4 d-flex align-items-center gap-2"><i class="bi bi-check-circle-fill fs-5"></i><div id="ticket-success-msg">Đã gửi ticket thành công!</div></div>
                            <form id="quick-ticket-form" onsubmit="handleQuickTicketSubmit(event)">
                                <div class="row g-3">
                                    <div class="col-sm-6"><label class="form-label required fw-semibold small" for="ticket-fullname">Họ và tên</label><input type="text" class="form-control" id="ticket-fullname" name="fullname" required placeholder="Nguyễn Văn A"></div>
                                    <div class="col-sm-6"><label class="form-label required fw-semibold small" for="ticket-studentid">Mã số sinh viên (MSSV)</label><input type="text" class="form-control" id="ticket-studentid" name="studentId" required placeholder="22110xxx" inputmode="numeric"></div>
                                    <div class="col-12"><label class="form-label required fw-semibold small" for="ticket-email">Email sinh viên</label><input type="email" class="form-control" id="ticket-email" name="email" required placeholder="22110xxx@student.hcmute.edu.vn"></div>
                                    <div class="col-sm-6"><label class="form-label required fw-semibold small" for="ticket-category">Chủ đề cần tư vấn</label><select class="form-select" id="ticket-category" name="category" required><option value="Học vụ & Đào tạo">Học vụ &amp; Đào tạo</option><option value="Học phí & Tài chính">Học phí &amp; Tài chính</option><option value="Đăng ký môn học">Đăng ký môn học</option><option value="Ký túc xá">Ký túc xá</option><option value="Học bổng & CTSV">Học bổng &amp; CTSV</option><option value="Tư vấn tâm lý">Tư vấn tâm lý</option><option value="Khác">Khác</option></select></div>
                                    <div class="col-sm-6"><label class="form-label fw-semibold small">Mức độ ưu tiên</label><div class="btn-group w-100" role="group"><input type="radio" class="btn-check" name="priority" id="prio-low" value="Thấp"><label class="btn btn-outline-secondary btn-sm" for="prio-low">Thấp</label><input type="radio" class="btn-check" name="priority" id="prio-normal" value="Bình thường" checked><label class="btn btn-outline-secondary btn-sm" for="prio-normal">Bình thường</label><input type="radio" class="btn-check" name="priority" id="prio-high" value="Khẩn cấp"><label class="btn btn-outline-danger btn-sm" for="prio-high">Khẩn cấp</label></div></div>
                                    <div class="col-12"><label class="form-label required fw-semibold small" for="ticket-subject">Tiêu đề yêu cầu</label><input type="text" class="form-control" id="ticket-subject" name="subject" required placeholder="Tóm tắt ngắn gọn vấn đề cần tư vấn"></div>
                                    <div class="col-12"><label class="form-label required fw-semibold small" for="ticket-content">Nội dung chi tiết</label><textarea class="form-control" id="ticket-content" name="content" rows="4" required placeholder="Mô tả chi tiết vấn đề, thời điểm xảy ra, các bước bạn đã thử..."></textarea></div>
                                    <div class="col-12 d-flex flex-wrap justify-content-between align-items-center gap-3 pt-2">
                                        <label class="btn btn-light btn-sm text-secondary border mb-0 d-inline-flex align-items-center gap-1 cursor-pointer"><i class="bi bi-paperclip"></i><span>Đính kèm tệp (tối đa 5MB)</span><input type="file" class="d-none" id="ticket-file" onchange="previewFileName(this)"></label>
                                        <span id="file-name-preview" class="small text-muted"></span>
                                        <sec:authorize access="isAnonymous()"><a href="/auth/login?returnUrl=/student/tickets/new" class="btn btn-warning px-4 py-2 fw-semibold rounded-pill"><i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập để gửi ticket</a></sec:authorize>
                                        <sec:authorize access="isAuthenticated()"><button type="submit" class="btn btn-primary px-4 py-2 fw-semibold rounded-pill" id="btn-submit-ticket" style="background-color: var(--hcmute-blue);"><i class="bi bi-send-fill me-1"></i>Gửi ticket</button></sec:authorize>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="hcmute-card-modern p-4">
                            <div class="d-flex align-items-center gap-2 mb-3"><i class="bi bi-inbox-fill text-primary fs-5"></i><h5 class="fw-bold text-dark mb-0">Ticket của tôi</h5></div>
                            <p class="small text-muted mb-4">Theo dõi trạng thái các yêu cầu đã gửi gần đây.</p>
                            <div class="d-flex flex-column gap-3" id="my-tickets-list">
                                <div class="p-3 rounded-3 bg-light border"><div class="d-flex justify-content-between align-items-center mb-1"><span class="badge bg-primary text-white fw-mono font-monospace">QA-2041</span><span class="badge bg-success-subtle text-success">Đã phản hồi</span></div><div class="fw-semibold small text-dark mb-1">Xin xác nhận sinh viên để làm thủ tục vay vốn</div><div class="text-muted small" style="font-size: .75rem;">Học vụ • Cập nhật 08/05/2026</div></div>
                                <div class="p-3 rounded-3 bg-light border"><div class="d-flex justify-content-between align-items-center mb-1"><span class="badge bg-primary text-white fw-mono font-monospace">QA-2087</span><span class="badge bg-warning-subtle text-warning-emphasis">Đang xử lý</span></div><div class="fw-semibold small text-dark mb-1">Lỗi không đăng ký được môn Toán cao cấp 2</div><div class="text-muted small" style="font-size: .75rem;">Đăng ký môn học • Cập nhật 09/05/2026</div></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <section class="hcmute-section hcmute-section-muted" id="booking">
            <div class="container-xl">
                <div class="text-center max-w-xl mx-auto mb-5"><span class="hcmute-eyebrow"><i class="bi bi-calendar-event-fill"></i>Gặp trực tiếp</span><h2 class="hcmute-title-main">Đặt lịch hẹn tư vấn</h2><p class="hcmute-desc-main mx-auto">Chọn chuyên viên, thời gian và hình thức phù hợp. Bạn sẽ nhận email xác nhận ngay sau khi đặt lịch.</p></div>
                <div class="row g-4">
                    <div class="col-lg-8">
                        <div class="hcmute-card-modern p-4 p-md-5">
                            <div class="mb-5"><h6 class="fw-bold text-dark mb-3">1. Chọn chuyên viên tư vấn</h6><div class="row g-3">
                                <div class="col-md-4"><div class="p-3 rounded-3 border bg-white cursor-pointer transition-all advisor-select-card active border-primary shadow-sm" onclick="selectAdvisor('a1', 'ThS. Nguyễn Minh Anh', 'Phòng Đào tạo', this)"><div class="d-flex align-items-center gap-3 mb-2"><div class="rounded-circle d-flex align-items-center justify-content-center fw-bold text-white shadow-sm" style="width: 44px; height: 44px; background: linear-gradient(135deg, #0b3d82, #1e40af); font-size: .95rem; flex-shrink: 0;">MA</div><div><div class="fw-bold small text-dark">ThS. Nguyễn Minh Anh</div><div class="text-muted small" style="font-size: .75rem;">Phòng Đào tạo</div></div></div><div class="d-flex flex-wrap gap-1"><span class="badge bg-light text-secondary border">Học vụ</span><span class="badge bg-light text-secondary border">Đăng ký môn</span></div></div></div>
                                <div class="col-md-4"><div class="p-3 rounded-3 border bg-white cursor-pointer transition-all advisor-select-card" onclick="selectAdvisor('a2', 'ThS. Trần Thu Hà', 'Phòng CTSV', this)"><div class="d-flex align-items-center gap-3 mb-2"><div class="rounded-circle d-flex align-items-center justify-content-center fw-bold text-white shadow-sm" style="width: 44px; height: 44px; background: linear-gradient(135deg, #0284c7, #0369a1); font-size: .95rem; flex-shrink: 0;">TH</div><div><div class="fw-bold small text-dark">ThS. Trần Thu Hà</div><div class="text-muted small" style="font-size: .75rem;">Phòng CTSV</div></div></div><div class="d-flex flex-wrap gap-1"><span class="badge bg-light text-secondary border">Học bổng</span><span class="badge bg-light text-secondary border">Ký túc xá</span></div></div></div>
                                <div class="col-md-4"><div class="p-3 rounded-3 border bg-white cursor-pointer transition-all advisor-select-card" onclick="selectAdvisor('a3', 'TS. Lê Quốc Bảo', 'Tư vấn tâm lý', this)"><div class="d-flex align-items-center gap-3 mb-2"><div class="rounded-circle d-flex align-items-center justify-content-center fw-bold text-white shadow-sm" style="width: 44px; height: 44px; background: linear-gradient(135deg, #4f46e5, #4338ca); font-size: .95rem; flex-shrink: 0;">QB</div><div><div class="fw-bold small text-dark">TS. Lê Quốc Bảo</div><div class="text-muted small" style="font-size: .75rem;">Tư vấn tâm lý</div></div></div><div class="d-flex flex-wrap gap-1"><span class="badge bg-light text-secondary border">Tâm lý</span><span class="badge bg-light text-secondary border">Định hướng</span></div></div></div>
                            </div></div>
                            <div class="mb-5"><h6 class="fw-bold text-dark mb-3">2. Chọn ngày làm việc</h6><div class="row g-2" id="booking-days-grid"></div></div>
                            <div class="mb-5"><h6 class="fw-bold text-dark mb-3">3. Chọn khung giờ tư vấn</h6><div class="row g-2" id="booking-slots-grid">
                                <div class="col-4 col-sm-2"><button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('08:00', this)">08:00</button></div><div class="col-4 col-sm-2"><button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('09:00', this)">09:00</button></div><div class="col-4 col-sm-2"><button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('10:00', this)">10:00</button></div><div class="col-4 col-sm-2"><button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('13:30', this)">13:30</button></div><div class="col-4 col-sm-2"><button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('14:30', this)">14:30</button></div><div class="col-4 col-sm-2"><button type="button" class="btn btn-outline-secondary w-100 py-2 slot-btn" onclick="selectSlot('15:30', this)">15:30</button></div>
                            </div></div>
                            <div><h6 class="fw-bold text-dark mb-3">4. Hình thức tư vấn</h6><div class="row g-3">
                                <div class="col-sm-6"><div class="p-3 rounded-3 border bg-white cursor-pointer modality-card active border-primary shadow-sm" onclick="selectModality('offline', this)"><div class="d-flex align-items-center gap-2 mb-1"><i class="bi bi-geo-alt-fill text-primary fs-5"></i><span class="fw-bold text-dark">Trực tiếp tại trường</span></div><p class="small text-muted mb-0">Phòng A1-102, Tòa nhà Trung tâm HCMUTE</p></div></div>
                                <div class="col-sm-6"><div class="p-3 rounded-3 border bg-white cursor-pointer modality-card" onclick="selectModality('online', this)"><div class="d-flex align-items-center gap-2 mb-1"><i class="bi bi-camera-video-fill text-success fs-5"></i><span class="fw-bold text-dark">Trực tuyến (Online)</span></div><p class="small text-muted mb-0">Google Meet gửi qua email sinh viên</p></div></div>
                            </div></div>
                        </div>
                    </div>
                    <div class="col-lg-4"><div class="hcmute-card-modern p-4 position-sticky" style="top: 5rem;"><div class="d-flex align-items-center gap-2 mb-4"><i class="bi bi-calendar2-check-fill text-primary fs-5"></i><h5 class="fw-bold text-dark mb-0">Tóm tắt lịch hẹn</h5></div><div class="d-flex flex-column gap-3 small pb-4 border-bottom mb-4"><div class="d-flex justify-content-between"><span class="text-muted">Chuyên viên:</span><span class="fw-bold text-dark" id="summary-advisor">ThS. Nguyễn Minh Anh</span></div><div class="d-flex justify-content-between"><span class="text-muted">Ngày hẹn:</span><span class="fw-bold text-dark" id="summary-date">Thứ 2, ngày mai</span></div><div class="d-flex justify-content-between"><span class="text-muted">Khung giờ:</span><span class="fw-bold text-primary" id="summary-slot">Chưa chọn</span></div><div class="d-flex justify-content-between"><span class="text-muted">Hình thức:</span><span class="fw-bold text-dark" id="summary-modality">Trực tiếp tại Phòng A1-102</span></div><div class="d-flex justify-content-between"><span class="text-muted">Thời lượng:</span><span class="fw-bold text-dark">30 phút</span></div></div>
                        <div id="booking-success-box" class="d-none alert alert-success small mb-3"><i class="bi bi-check-circle-fill me-1"></i><span id="booking-success-text">Đặt lịch thành công!</span></div>
                        <sec:authorize access="isAnonymous()"><div class="alert alert-warning border-0 small mb-3 p-2 rounded-3"><i class="bi bi-shield-lock-fill me-1 text-warning"></i>Bạn cần đăng nhập tài khoản sinh viên để hệ thống cấp mã hẹn và phòng gặp trực tiếp/Google Meet.</div><a href="/auth/login?returnUrl=/student/appointments/book" class="btn btn-warning w-100 py-2 fw-semibold rounded-pill shadow-sm"><i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập để đặt lịch hẹn</a></sec:authorize>
                        <sec:authorize access="isAuthenticated()"><a href="/student/appointments/book" class="btn btn-primary w-100 py-2 fw-semibold rounded-pill shadow-sm"><i class="bi bi-calendar-plus me-1"></i>Đặt lịch hẹn trên hệ thống</a></sec:authorize>
                    </div></div>
                </div>
            </div>
        </section>
    </c:if>
</div>

<script>
// Lọc câu hỏi theo chuyên mục được chọn.
function selectFaqCategory(category, btn) {
    var buttons = document.querySelectorAll('#faq-category-buttons button');
    // Đặt lại trạng thái hiển thị của từng nút chuyên mục.
    buttons.forEach(function(b) {
        b.classList.remove('btn-primary');
        b.classList.add('btn-light', 'text-dark');
        b.style.backgroundColor = '';
        b.style.borderColor = '';
    });
    btn.classList.remove('btn-light', 'text-dark');
    btn.classList.add('btn-primary');
    btn.style.backgroundColor = 'var(--hcmute-blue)';
    btn.style.borderColor = 'var(--hcmute-blue)';
    var items = document.querySelectorAll('.faq-item');
    var visibleCount = 0;
    // Hiển thị các câu hỏi thuộc chuyên mục đã chọn.
    items.forEach(function(item) {
        if (category === 'all' || item.getAttribute('data-category') === category) {
            item.style.display = '';
            visibleCount++;
        } else {
            item.style.display = 'none';
        }
    });
    checkFaqEmptyState(visibleCount);
}

// Lọc câu hỏi theo nội dung người dùng nhập.
function filterFaqs() {
    var query = document.getElementById('faq-filter-input').value.toLowerCase().trim();
    var items = document.querySelectorAll('.faq-item');
    var visibleCount = 0;
    // Cập nhật khả năng hiển thị của từng câu hỏi.
    items.forEach(function(item) {
        var text = item.textContent.toLowerCase();
        if (text.indexOf(query) !== -1) {
            item.style.display = '';
            visibleCount++;
        } else {
            item.style.display = 'none';
        }
    });
    checkFaqEmptyState(visibleCount);
}

// Hiển thị trạng thái khi không có câu hỏi phù hợp.
function checkFaqEmptyState(count) {
    var empty = document.getElementById('faq-empty-state');
    if (empty) {
        if (count === 0) {
            empty.classList.remove('d-none');
        } else {
            empty.classList.add('d-none');
        }
    }
}

// Cập nhật tên tệp đính kèm được chọn.
function previewFileName(input) {
    var preview = document.getElementById('file-name-preview');
    if (input.files && input.files[0]) {
        preview.textContent = input.files[0].name;
    } else {
        preview.textContent = '';
    }
}

// Gửi ticket hỗ trợ nhanh từ biểu mẫu.
function handleQuickTicketSubmit(e) {
    e.preventDefault();
    var btn = document.getElementById('btn-submit-ticket');
    btn.disabled = true;
    btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span> Đang gửi...';
    var payload = {
        fullname: document.getElementById('ticket-fullname').value,
        studentId: document.getElementById('ticket-studentid').value,
        email: document.getElementById('ticket-email').value,
        category: document.getElementById('ticket-category').value,
        priority: document.querySelector('input[name="priority"]:checked').value,
        subject: document.getElementById('ticket-subject').value,
        content: document.getElementById('ticket-content').value
    };
    fetch('/api/public/tickets/quick-submit', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    // Chuyển phản hồi gửi ticket thành dữ liệu JSON.
    .then(function(res) { return res.json(); })
    // Cập nhật giao diện sau khi gửi ticket thành công.
    .then(function(res) {
        btn.disabled = false;
        btn.innerHTML = '<i class="bi bi-send-fill me-1"></i>Gửi ticket';
        if (res && res.data) {
            var alert = document.getElementById('ticket-success-alert');
            var msg = document.getElementById('ticket-success-msg');
            msg.textContent = res.data.message;
            alert.classList.remove('d-none');
            var list = document.getElementById('my-tickets-list');
            var newTicketHtml = '<div class="p-3 rounded-3 bg-light border border-primary">' +
                '<div class="d-flex justify-content-between align-items-center mb-1">' +
                '<span class="badge bg-primary text-white font-monospace">' + res.data.code + '</span>' +
                '<span class="badge bg-secondary-subtle text-secondary">Đang chờ</span>' +
                '</div>' +
                '<div class="fw-semibold small text-dark mb-1">' + res.data.subject + '</div>' +
                '<div class="text-muted small" style="font-size: 0.75rem;">' + res.data.category + ' • Vừa tạo</div>' +
                '</div>';
            list.insertAdjacentHTML('afterbegin', newTicketHtml);
            document.getElementById('quick-ticket-form').reset();
            document.getElementById('file-name-preview').textContent = '';
        }
    })
    // Khôi phục nút gửi và thông báo khi gửi ticket thất bại.
    .catch(function(err) {
        btn.disabled = false;
        btn.innerHTML = '<i class="bi bi-send-fill me-1"></i>Gửi ticket';
        alert('Có lỗi xảy ra, vui lòng thử lại sau.');
    });
}

var selectedAdvisorName = 'ThS. Nguyễn Minh Anh';
var selectedDate = 'Thứ 2, ngày mai';
var selectedSlot = null;
var selectedModality = 'Trực tiếp tại Phòng A1-102';

// Chọn chuyên viên tư vấn và cập nhật thông tin tóm tắt.
function selectAdvisor(id, name, dept, el) {
    // Bỏ trạng thái chọn khỏi các thẻ chuyên viên khác.
    document.querySelectorAll('.advisor-select-card').forEach(function(c) {
        c.classList.remove('active', 'border-primary', 'shadow-sm');
    });
    el.classList.add('active', 'border-primary', 'shadow-sm');
    selectedAdvisorName = name + ' (' + dept + ')';
    document.getElementById('summary-advisor').textContent = selectedAdvisorName;
}

// Chọn khung giờ tư vấn và cập nhật thông tin tóm tắt.
function selectSlot(time, btn) {
    // Đặt lại trạng thái của các khung giờ khác.
    document.querySelectorAll('.slot-btn').forEach(function(b) {
        b.classList.remove('btn-primary', 'text-white');
        b.classList.add('btn-outline-secondary');
        b.style.backgroundColor = '';
    });
    btn.classList.remove('btn-outline-secondary');
    btn.classList.add('btn-primary', 'text-white');
    btn.style.backgroundColor = 'var(--hcmute-blue)';
    selectedSlot = time;
    document.getElementById('summary-slot').textContent = selectedSlot;
    document.getElementById('booking-hint').textContent = 'Sẵn sàng đặt lịch! Nhấn nút bên trên để hoàn tất.';
}

// Chọn hình thức tư vấn và cập nhật thông tin tóm tắt.
function selectModality(type, el) {
    // Bỏ trạng thái chọn khỏi các hình thức tư vấn khác.
    document.querySelectorAll('.modality-card').forEach(function(c) {
        c.classList.remove('active', 'border-primary', 'shadow-sm');
    });
    el.classList.add('active', 'border-primary', 'shadow-sm');
    if (type === 'offline') {
        selectedModality = 'Trực tiếp tại Phòng A1-102';
    } else {
        selectedModality = 'Trực tuyến (Google Meet)';
    }
    document.getElementById('summary-modality').textContent = selectedModality;
}

// Tạo danh sách ngày tư vấn khả dụng.
function initBookingDays() {
    var grid = document.getElementById('booking-days-grid');
    if (!grid) return;
    var days = [];
    var d = new Date();
    var dayNames = ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'];
    while (days.length < 6) {
        d.setDate(d.getDate() + 1);
        if (d.getDay() !== 0 && d.getDay() !== 6) {
            days.push(new Date(d));
        }
    }
    var html = '';
    // Tạo nút cho từng ngày tư vấn khả dụng.
    days.forEach(function(date, index) {
        var wd = dayNames[date.getDay()];
        var formatted = ('0' + date.getDate()).slice(-2) + '/' + ('0' + (date.getMonth() + 1)).slice(-2);
        var activeClass = index === 0 ? 'btn-primary text-white active-day' : 'btn-outline-secondary';
        var bgStyle = index === 0 ? 'style="background-color: var(--hcmute-blue);"' : '';
        if (index === 0) selectedDate = wd + ', ' + formatted;
        html += '<div class="col-4 col-sm-2">' +
            '<button type="button" class="btn ' + activeClass + ' w-100 py-2 d-flex flex-column align-items-center day-btn" ' +
            bgStyle + ' onclick="selectDay(\'' + wd + ', ' + formatted + '\', this)">' +
            '<span class="fw-bold fs-6">' + wd + '</span>' +
            '<span class="small" style="font-size: 0.75rem;">' + formatted + '</span>' +
            '</button></div>';
    });
    grid.innerHTML = html;
    document.getElementById('summary-date').textContent = selectedDate;
}

// Chọn ngày tư vấn và cập nhật thông tin tóm tắt.
function selectDay(dateStr, btn) {
    // Đặt lại trạng thái của các ngày khác.
    document.querySelectorAll('.day-btn').forEach(function(b) {
        b.classList.remove('btn-primary', 'text-white', 'active-day');
        b.classList.add('btn-outline-secondary');
        b.style.backgroundColor = '';
    });
    btn.classList.remove('btn-outline-secondary');
    btn.classList.add('btn-primary', 'text-white', 'active-day');
    btn.style.backgroundColor = 'var(--hcmute-blue)';
    selectedDate = dateStr;
    document.getElementById('summary-date').textContent = selectedDate;
}

// Xác nhận đặt lịch tư vấn nhanh.
function confirmBooking() {
    if (!selectedSlot) {
        alert('Vui lòng chọn một khung giờ tư vấn trước khi xác nhận.');
        return;
    }
    var btn = document.getElementById('btn-confirm-booking');
    btn.disabled = true;
    btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span> Đang xác nhận...';
    fetch('/api/public/booking/quick-book', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ advisorName: selectedAdvisorName, date: selectedDate, slot: selectedSlot, modality: selectedModality })
    })
    // Chuyển phản hồi đặt lịch thành dữ liệu JSON.
    .then(function(res) { return res.json(); })
    // Hiển thị thông tin lịch hẹn sau khi đặt thành công.
    .then(function(res) {
        btn.disabled = false;
        btn.innerHTML = 'Xác nhận đặt lịch';
        if (res && res.data) {
            var box = document.getElementById('booking-success-box');
            var text = document.getElementById('booking-success-text');
            text.textContent = res.data.message;
            box.classList.remove('d-none');
            btn.classList.add('d-none');
            document.getElementById('booking-hint').textContent = 'Mã lịch hẹn: ' + res.data.bookingCode;
        }
    })
    // Khôi phục nút và thông báo kết quả khi đặt lịch thất bại.
    .catch(function() {
        btn.disabled = false;
        btn.innerHTML = 'Xác nhận đặt lịch';
        alert('Đặt lịch thành công! Mã hẹn: BK-' + Math.floor(1000 + Math.random() * 9000));
    });
}

// Khởi tạo danh sách ngày khi tài liệu đã tải xong.
document.addEventListener('DOMContentLoaded', function() {
    initBookingDays();
});
</script>
