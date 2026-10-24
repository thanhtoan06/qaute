<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Tìm kiếm FAQ</title>
<style>
.qaute-search-page { --search-navy: #12234a; --search-blue: #3865e9; --search-teal: #12b8a7; --search-muted: #718098; --search-border: rgba(105, 127, 174, .17); color: #243651; padding-bottom: 2.5rem; }
.qaute-search-page .search-breadcrumb { margin-bottom: 1.25rem; font-size: .88rem; }
.qaute-search-page .search-breadcrumb .breadcrumb { margin-bottom: 0; }
.qaute-search-page .search-breadcrumb a { color: #70809a; text-decoration: none; transition: color .2s ease; }
.qaute-search-page .search-breadcrumb a:hover { color: var(--search-blue); }
.qaute-search-page .search-hero { isolation: isolate; padding: clamp(2rem, 4.5vw, 3.45rem); margin-bottom: 1.75rem; background: radial-gradient(ellipse at 90% 4%, rgba(35, 217, 195, .24), transparent 34%), radial-gradient(ellipse at 5% 100%, rgba(77, 116, 255, .29), transparent 43%), linear-gradient(125deg, #101e42 0%, #182f63 52%, #15516e 100%); border: 1px solid rgba(255, 255, 255, .12); box-shadow: 0 24px 58px rgba(18, 39, 83, .17); }
.qaute-search-page .search-hero::before, .qaute-search-page .search-hero::after { content: ""; position: absolute; z-index: -1; pointer-events: none; border: 1px solid rgba(255, 255, 255, .14); border-radius: 50%; }
.qaute-search-page .search-hero::before { width: 340px; height: 340px; top: -180px; right: -80px; box-shadow: 0 0 0 28px rgba(255, 255, 255, .025), 0 0 0 60px rgba(255, 255, 255, .02); animation: qaute-orbit 25s linear infinite; }
.qaute-search-page .search-hero::after { width: 230px; height: 230px; right: 13%; bottom: -178px; border-color: rgba(93, 236, 219, .27); box-shadow: 0 0 0 24px rgba(93, 236, 219, .035); animation: qaute-orbit 19s linear infinite reverse; }
.qaute-search-page .search-hero-content { position: relative; z-index: 1; max-width: 900px; margin: 0 auto; }
.qaute-search-page .search-eyebrow { display: inline-flex; align-items: center; gap: .55rem; padding: .55rem .9rem; margin-bottom: 1.1rem; color: #d6fffa; background: rgba(255, 255, 255, .09); border: 1px solid rgba(255, 255, 255, .2); border-radius: 999px; backdrop-filter: blur(12px); font-size: .74rem; font-weight: 850; letter-spacing: .13em; text-transform: uppercase; }
.qaute-search-page .search-eyebrow i { color: #73f0df; font-size: 1rem; }
.qaute-search-page .search-hero h1 { margin: 0 0 .8rem; font-size: clamp(2rem, 4vw, 3.2rem); font-weight: 900; line-height: 1.12; }
.qaute-search-page .search-hero h1 span { color: #8cecdf; background: linear-gradient(90deg, #90f2e3, #a9c9ff); -webkit-background-clip: text; background-clip: text; -webkit-text-fill-color: transparent; }
.qaute-search-page .search-hero-description { max-width: 700px; margin: 0 auto 1.7rem; color: rgba(241, 247, 255, .8); font-size: .97rem; line-height: 1.85; }
.qaute-search-page .search-form-shell { padding: .5rem; background: rgba(255, 255, 255, .97); border: 1px solid rgba(255, 255, 255, .65); border-radius: 18px; box-shadow: 0 15px 40px rgba(0, 0, 0, .17); transition: box-shadow .25s ease, transform .25s ease; }
.qaute-search-page .search-form-shell:focus-within { box-shadow: 0 0 0 4px rgba(133, 214, 255, .2), 0 18px 46px rgba(0, 0, 0, .2); transform: translateY(-1px); }
.qaute-search-page .search-form-shell .input-group { min-width: 0; }
.qaute-search-page .search-form-shell .search-field-icon { display: inline-flex; align-items: center; justify-content: center; padding: 0 1rem; color: #52709e; background: #fff; border: 0; border-radius: 13px 0 0 13px; font-size: 1.2rem; }
.qaute-search-page .search-form-shell .form-control { min-width: 0; min-height: 56px; color: #1d3355; background: #fff; border: 0; border-radius: 0; box-shadow: none; font-size: .98rem; }
.qaute-search-page .search-form-shell .form-control::placeholder { color: #99a5b8; }
.qaute-search-page .search-form-shell .form-control:focus { box-shadow: none; }
.qaute-search-page .search-form-shell .btn { display: inline-flex; align-items: center; justify-content: center; gap: .55rem; min-height: 52px; padding: .75rem 1.25rem; color: #10234b; background: linear-gradient(110deg, #9bf4e5, #a8d5ff); border: 0; border-radius: 13px !important; font-weight: 850; box-shadow: 0 5px 14px rgba(13, 173, 161, .13); transition: transform .22s ease, box-shadow .22s ease, filter .22s ease; }
.qaute-search-page .search-form-shell .btn:hover { transform: translateY(-2px); filter: brightness(1.035); box-shadow: 0 9px 20px rgba(13, 173, 161, .2); }
.qaute-search-page .search-hero-bottom { display: flex; align-items: center; justify-content: center; flex-wrap: wrap; gap: .65rem; margin-top: 1.1rem; }
.qaute-search-page .search-hero-pill { display: inline-flex; align-items: center; gap: .45rem; padding: .45rem .7rem; color: #e5f3ff; background: rgba(255, 255, 255, .055); border: 1px solid rgba(255, 255, 255, .13); border-radius: 9px; font-size: .76rem; }
.qaute-search-page .search-hero-pill i { color: #7de8dd; }
.qaute-search-page .search-error { display: flex; align-items: flex-start; gap: .85rem; padding: 1rem 1.15rem; color: #79530d; background: linear-gradient(110deg, #fff8e7, #fffdf7); border: 1px solid #f1dfae; border-radius: 15px; box-shadow: 0 7px 20px rgba(122, 92, 18, .045); }
.qaute-search-page .search-error > i { margin-top: .1rem; color: #d49a18; font-size: 1.35rem; }
.qaute-search-page .search-error > div { line-height: 1.75; }
.qaute-search-page .search-workspace { align-items: flex-start; }
.qaute-search-page .search-sidebar { display: flex; flex-direction: column; gap: 1rem; }
.qaute-search-page .search-filter-card { overflow: hidden; background: #fff; border: 1px solid var(--search-border); border-radius: 19px; box-shadow: 0 9px 28px rgba(27, 47, 91, .05); transition: border-color .25s ease, box-shadow .25s ease; }
.qaute-search-page .search-filter-card:hover { border-color: rgba(56, 101, 233, .22); box-shadow: 0 13px 30px rgba(27, 47, 91, .075); }
.qaute-search-page .search-filter-heading { display: flex; align-items: center; gap: .75rem; padding: 1rem 1.05rem; background: linear-gradient(120deg, #f7f9ff, #fff); border-bottom: 1px solid #e8eef7; }
.qaute-search-page .search-filter-icon { display: inline-flex; flex: 0 0 37px; width: 37px; height: 37px; align-items: center; justify-content: center; color: #315bd8; background: #eaf0ff; border: 1px solid #dfe8ff; border-radius: 11px; font-size: 1.05rem; }
.qaute-search-page .search-filter-heading h2 { margin: 0; color: #203555; font-size: .96rem; font-weight: 850; }
.qaute-search-page .search-clear-filter { display: flex; align-items: center; justify-content: center; gap: .5rem; width: 100%; min-height: 42px; padding: .65rem .8rem; color: #c34253; background: #fff7f8; border: 1px solid #f1d9dd; border-radius: 12px; font-size: .82rem; font-weight: 800; text-decoration: none; transition: background .2s ease, border-color .2s ease, transform .2s ease; }
.qaute-search-page .search-clear-filter:hover { color: #ae283a; background: #ffedef; border-color: #eebec6; transform: translateY(-1px); }
.qaute-search-page .search-filter-list { display: flex; flex-direction: column; padding: .5rem; }
.qaute-search-page .search-filter-list .list-group-item { position: relative; display: flex; align-items: center; gap: .55rem; padding: .75rem .8rem; color: #5a6b85; background: transparent; border: 0; border-radius: 10px; font-size: .84rem; line-height: 1.5; text-decoration: none; transition: color .2s ease, background .2s ease, padding .2s ease; }
.qaute-search-page .search-filter-list .list-group-item:hover { padding-left: 1rem; color: #2855d4; background: #f2f6ff; }
.qaute-search-page .search-filter-list .list-group-item.active { color: #254fbe; background: linear-gradient(100deg, #eaf0ff, #f5f8ff); font-weight: 850; box-shadow: inset 3px 0 0 #3865e9; }
.qaute-search-page .search-filter-list .list-group-item.active::after { content: ""; width: 6px; height: 6px; flex: 0 0 6px; margin-left: auto; background: #3865e9; border-radius: 50%; }
.qaute-search-page .search-filter-list .search-filter-dept { padding: .8rem .75rem .45rem; margin-top: .35rem; color: #7d8ba0; background: transparent; border: 0; font-size: .68rem; font-weight: 850; letter-spacing: .08em; text-transform: uppercase; line-height: 1.6; overflow-wrap: anywhere; }
.qaute-search-page .search-filter-list .search-filter-category { padding-left: 1.25rem; }
.qaute-search-page .search-filter-list .search-filter-category i { flex-shrink: 0; color: #6985ca; }
.qaute-search-page .search-filter-list .search-filter-category.active i { color: #2855d4; }
.qaute-search-page .search-filter-list .search-filter-category span { min-width: 0; overflow-wrap: anywhere; }
.qaute-search-page .search-main { min-width: 0; }
.qaute-search-page .search-results-toolbar { display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: .8rem; margin-bottom: 1rem; }
.qaute-search-page .search-results-heading { display: flex; align-items: flex-start; gap: .8rem; min-width: 0; }
.qaute-search-page .search-results-heading-icon { display: inline-flex; flex: 0 0 44px; width: 44px; height: 44px; align-items: center; justify-content: center; color: #315bd8; background: linear-gradient(145deg, #edf2ff, #dfe9ff); border: 1px solid #e0e9ff; border-radius: 14px; font-size: 1.2rem; }
.qaute-search-page .search-results-heading h2 { margin: .05rem 0 .25rem; color: var(--search-navy); font-size: clamp(1.15rem, 2vw, 1.45rem); font-weight: 900; letter-spacing: -.025em; overflow-wrap: anywhere; }
.qaute-search-page .search-results-heading p { margin: 0; color: var(--search-muted); font-size: .85rem; line-height: 1.7; overflow-wrap: anywhere; }
.qaute-search-page .search-results-count { display: inline-flex; align-items: center; gap: .4rem; flex-shrink: 0; padding: .55rem .75rem; color: #3555b2; background: #eef3ff; border: 1px solid #dfe8ff; border-radius: 11px; font-size: .77rem; font-weight: 850; }
.qaute-search-page .search-result-card { position: relative; overflow: hidden; margin-bottom: 1rem; background: linear-gradient(155deg, #fff, #fcfdff); border: 1px solid #e2eaf5; border-radius: 19px; box-shadow: 0 8px 25px rgba(27, 47, 91, .045); transition: transform .28s ease, border-color .28s ease, box-shadow .28s ease; }
.qaute-search-page .search-result-card::before { content: ""; position: absolute; inset: 0 auto 0 0; width: 3px; background: linear-gradient(180deg, #3865e9, #18c4b0); opacity: 0; transition: opacity .25s ease; }
.qaute-search-page .search-result-card:hover { transform: translateY(-4px); border-color: rgba(56, 101, 233, .28); box-shadow: 0 17px 35px rgba(27, 47, 91, .09); }
.qaute-search-page .search-result-card:hover::before { opacity: 1; }
.qaute-search-page .search-result-card .card-body { padding: clamp(1.1rem, 2.5vw, 1.55rem); }
.qaute-search-page .search-result-title { margin: 0 0 .65rem; color: #1c3d72; font-size: clamp(1.08rem, 2vw, 1.3rem); font-weight: 850; line-height: 1.55; letter-spacing: -.02em; overflow-wrap: anywhere; }
.qaute-search-page .search-result-title a { color: inherit; text-decoration: none; background-image: linear-gradient(#3865e9, #3865e9); background-repeat: no-repeat; background-position: 0 100%; background-size: 0 2px; transition: color .2s ease, background-size .25s ease; }
.qaute-search-page .search-result-title a:hover { color: #2855d4; background-size: 100% 2px; }
.qaute-search-page .search-result-snippet { margin: 0 0 1rem; color: #66758d; font-size: .91rem; line-height: 1.9; overflow-wrap: anywhere; }
.qaute-search-page .search-result-snippet mark, .qaute-search-page .search-result-snippet em, .qaute-search-page .search-result-title mark { padding: .08rem .22rem; color: #2348ad; background: #fff0b8; border-radius: 4px; font-style: normal; font-weight: 750; }
.qaute-search-page .search-result-meta { display: flex; align-items: center; flex-wrap: wrap; gap: .6rem 1rem; padding-top: 1rem; border-top: 1px solid #edf1f8; color: #7a879a; font-size: .77rem; }
.qaute-search-page .search-result-meta > span { display: inline-flex; align-items: center; gap: .38rem; }
.qaute-search-page .search-result-meta > span i { color: #748dbd; font-size: .9rem; }
.qaute-search-page .search-result-category { display: inline-flex; align-items: center; gap: .4rem; max-width: 100%; padding: .4rem .62rem; color: #3555b2 !important; background: #edf3ff; border: 1px solid #dfe8ff; border-radius: 8px; font-size: .74rem; font-weight: 800; }
.qaute-search-page .search-result-footer { display: flex; align-items: center; justify-content: space-between; gap: .75rem; flex-wrap: wrap; margin-top: 1rem; }
.qaute-search-page .search-read-more { display: inline-flex; align-items: center; gap: .45rem; color: #315bd8; font-size: .79rem; font-weight: 850; text-decoration: none; transition: gap .2s ease, color .2s ease; }
.qaute-search-page .search-read-more:hover { gap: .7rem; color: #193da9; }
.qaute-search-page .search-read-more i { transition: transform .2s ease; }
.qaute-search-page .search-read-more:hover i { transform: translateX(3px); }
.qaute-search-page .search-empty { position: relative; isolation: isolate; overflow: hidden; padding: clamp(1.5rem, 4vw, 2.5rem); margin-top: .3rem; text-align: center; background: linear-gradient(145deg, #fff, #f8faff); border: 1px solid #e1e9f5; border-radius: 24px; box-shadow: 0 12px 32px rgba(27, 47, 91, .055); }
.qaute-search-page .search-empty::before, .qaute-search-page .search-empty::after { content: ""; position: absolute; z-index: -1; width: 190px; height: 190px; border: 1px solid rgba(56, 101, 233, .09); border-radius: 50%; pointer-events: none; }
.qaute-search-page .search-empty::before { top: -125px; right: -75px; box-shadow: 0 0 0 20px rgba(56, 101, 233, .025); }
.qaute-search-page .search-empty::after { left: -120px; bottom: -145px; border-color: rgba(18, 184, 167, .1); }
.qaute-search-page .search-empty-icon { display: inline-flex; align-items: center; justify-content: center; width: 84px; height: 84px; margin-bottom: 1.3rem; color: #4e6cce; background: linear-gradient(145deg, #edf2ff, #dfe9ff); border: 1px solid #dce6ff; border-radius: 27px; font-size: 2.25rem; box-shadow: 0 9px 22px rgba(49, 91, 216, .08); animation: qaute-float 5s ease-in-out infinite; }
.qaute-search-page .search-empty h2 { margin: 0 0 .7rem; color: var(--search-navy); font-size: clamp(1.3rem, 2.5vw, 1.8rem); font-weight: 900; letter-spacing: -.03em; line-height: 1.5; overflow-wrap: anywhere; }
.qaute-search-page .search-empty-lead { margin-bottom: 1.2rem; color: #6b7890; line-height: 1.8; }
.qaute-search-page .search-tips { max-width: 650px; margin: 0 auto 1.5rem; padding: 1.2rem; text-align: left; background: linear-gradient(120deg, #f2f6ff, #fbfcff); border: 1px solid #e2eafb; border-radius: 16px; }
.qaute-search-page .search-tips-heading { display: flex; align-items: center; gap: .55rem; margin-bottom: .8rem; color: #2d477e; font-size: .88rem; font-weight: 850; }
.qaute-search-page .search-tips-heading i { color: #e4a51c; font-size: 1.1rem; }
.qaute-search-page .search-tips ul { margin: 0; padding-left: 1.25rem; color: #697991; font-size: .84rem; line-height: 1.85; }
.qaute-search-page .search-tips li::marker { color: #5478df; }
.qaute-search-page .search-suggestions { max-width: 750px; margin: 0 auto 1.5rem; }
.qaute-search-page .search-suggestions-heading { display: flex; align-items: center; justify-content: center; gap: .5rem; margin-bottom: .8rem; color: #687892; font-size: .76rem; font-weight: 850; letter-spacing: .06em; text-transform: uppercase; }
.qaute-search-page .search-suggestions-heading i { color: #3865e9; }
.qaute-search-page .search-suggestion-link { display: inline-flex; align-items: center; gap: .45rem; max-width: 100%; padding: .55rem .75rem; color: #4a5e7e; background: #fff; border: 1px solid #dfe7f3; border-radius: 10px; font-size: .79rem; font-weight: 700; text-decoration: none; overflow-wrap: anywhere; transition: transform .2s ease, border-color .2s ease, background .2s ease, color .2s ease; }
.qaute-search-page .search-suggestion-link:hover { transform: translateY(-2px); color: #2855d4; background: #f0f5ff; border-color: #cbd9ff; }
.qaute-search-page .search-empty-cta { padding-top: 1.3rem; border-top: 1px solid #e8edf6; }
.qaute-search-page .search-empty-cta p { margin-bottom: .85rem; color: #6c7a91; font-size: .88rem; }
.qaute-search-page .search-empty-cta .btn { display: inline-flex; align-items: center; justify-content: center; gap: .5rem; padding: .75rem 1.1rem; border-radius: 12px; font-weight: 800; box-shadow: 0 6px 16px rgba(56, 101, 233, .15); transition: transform .2s ease, box-shadow .2s ease; }
.qaute-search-page .search-empty-cta .btn:hover { transform: translateY(-2px); box-shadow: 0 10px 22px rgba(56, 101, 233, .22); }
.qaute-search-page .search-pagination { display: flex; justify-content: center; margin-top: 1.5rem; }
.qaute-search-page .search-pagination .pagination { flex-wrap: wrap; gap: .3rem; }
.qaute-search-page .search-pagination .page-link { display: inline-flex; align-items: center; justify-content: center; min-width: 40px; min-height: 40px; color: #526681; background: #fff; border: 1px solid #e0e8f4; border-radius: 11px !important; font-size: .83rem; font-weight: 750; box-shadow: 0 3px 9px rgba(27, 47, 91, .025); transition: transform .2s ease, color .2s ease, background .2s ease, border-color .2s ease, box-shadow .2s ease; }
.qaute-search-page .search-pagination .page-link:hover { transform: translateY(-2px); color: #2855d4; background: #eef3ff; border-color: #ccdafe; box-shadow: 0 6px 14px rgba(27, 47, 91, .06); }
.qaute-search-page .search-pagination .page-item.active .page-link { color: #fff; background: linear-gradient(130deg, #3865e9, #254fc4); border-color: #3865e9; box-shadow: 0 6px 15px rgba(56, 101, 233, .2); }
.qaute-search-page .search-pagination .page-item.disabled .page-link { color: #a5afbf; background: #f7f9fc; box-shadow: none; }
.qaute-search-page .search-bottom-help { display: flex; align-items: center; gap: .85rem; padding: 1rem 1.1rem; margin-top: 1.5rem; color: #536783; background: linear-gradient(110deg, #eef4ff, #ecfbf8); border: 1px solid #e0ebf7; border-radius: 16px; }
.qaute-search-page .search-bottom-help > i { display: inline-flex; flex: 0 0 39px; width: 39px; height: 39px; align-items: center; justify-content: center; color: #315bd8; background: #fff; border: 1px solid #dfe8fa; border-radius: 12px; font-size: 1.1rem; }
.qaute-search-page .search-bottom-help strong { display: block; margin-bottom: .15rem; color: #253b5d; font-size: .84rem; }
.qaute-search-page .search-bottom-help span { display: block; color: #718098; font-size: .78rem; line-height: 1.7; }
@media (min-width: 992px) {
  .qaute-search-page .search-sidebar { position: sticky; top: 100px; }
}
@media (max-width: 767.98px) {
  .qaute-search-page .search-hero { padding: 2rem 1.2rem; border-radius: 23px; }
  .qaute-search-page .search-hero::before { right: -220px; }
  .qaute-search-page .search-form-shell { padding: .4rem; }
  .qaute-search-page .search-form-shell .search-field-icon { padding: 0 .65rem; }
  .qaute-search-page .search-form-shell .form-control { min-height: 48px; font-size: .9rem; }
  .qaute-search-page .search-form-shell .btn { min-height: 47px; padding: .65rem .85rem; font-size: .83rem; }
  .qaute-search-page .search-hero-bottom { align-items: flex-start; justify-content: flex-start; }
  .qaute-search-page .search-hero-pill { font-size: .72rem; }
  .qaute-search-page .search-sidebar { margin-bottom: 1.5rem; }
  .qaute-search-page .search-results-toolbar { align-items: flex-start; }
  .qaute-search-page .search-results-count { margin-left: 3.8rem; }
}
@media (max-width: 480px) {
  .qaute-search-page .search-hero h1 { font-size: 2rem; }
  .qaute-search-page .search-form-shell .input-group { flex-wrap: wrap; }
  .qaute-search-page .search-form-shell .search-field-icon { min-height: 48px; border-radius: 12px 0 0 12px; }
  .qaute-search-page .search-form-shell .form-control { flex: 1 1 0; width: 1%; }
  .qaute-search-page .search-form-shell .input-group > .col-12 { flex: 0 0 100%; width: 100%; }
  .qaute-search-page .search-form-shell .input-group > .btn { width: 100%; }
  .qaute-search-page .search-result-meta { gap: .55rem .8rem; }
  .qaute-search-page .search-empty { padding: 1.3rem .9rem; border-radius: 19px; }
  .qaute-search-page .search-empty-icon { width: 68px; height: 68px; border-radius: 22px; font-size: 1.9rem; }
  .qaute-search-page .search-results-count { margin-left: 0; }
}
@media (prefers-reduced-motion: reduce) {
  .qaute-search-page *, .qaute-search-page *::before, .qaute-search-page *::after { animation-duration: .01ms !important; animation-iteration-count: 1 !important; scroll-behavior: auto !important; transition-duration: .01ms !important; }
}
</style>




<div class="qaute-search-page">
    <nav aria-label="breadcrumb" class="search-breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="/faq">Hỏi đáp</a></li>
            <li class="breadcrumb-item active" aria-current="page">Tìm kiếm</li>
        </ol>
    </nav>

    <section class="search-hero">
        <div class="search-hero-content">
            <div class="search-eyebrow">
                <i class="bi bi-stars"></i>
                QAUTE · KNOWLEDGE SEARCH
            </div>
            <h1>Tìm câu trả lời.<br><span>Khơi thông tri thức.</span></h1>
            <p class="search-hero-description">
                Tra cứu kho câu hỏi thường gặp của QAUTE để tìm thông tin học vụ, thủ tục sinh viên và hướng dẫn hỗ trợ phù hợp.
            </p>

            <div class="search-form-shell">
                <form method="get" action="/search" class="row g-2 align-items-center">
                    <c:if test="${not empty category}">
                        <input type="hidden" name="category" value="<c:out value='${category}'/>"/>
                    </c:if>
                    <c:if test="${not empty tag}">
                        <input type="hidden" name="tag" value="<c:out value='${tag}'/>"/>
                    </c:if>
                    <c:if test="${not empty sort}">
                        <input type="hidden" name="sort" value="<c:out value='${sort}'/>"/>
                    </c:if>
                    <div class="col-md-10">
                        <div class="input-group">
                            <span class="search-field-icon">
                                <i class="bi bi-search"></i>
                            </span>
                            <input type="text" name="q" class="form-control"
                                   placeholder="Nhập từ khóa tìm kiếm FAQ..."
                                   value="<c:out value='${q}'/>"
                                   data-qa-suggest="SEARCH"
                                   autofocus required minlength="2"/>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn w-100">
                            <i class="bi bi-search"></i>
                            Tìm kiếm
                        </button>
                    </div>
                </form>
            </div>

            <div class="search-hero-bottom">
                <span class="search-hero-pill"><i class="bi bi-lightning-charge-fill"></i> Gợi ý tìm kiếm</span>
                <span class="search-hero-pill"><i class="bi bi-folder2-open"></i> Lọc theo chuyên mục</span>
                <span class="search-hero-pill"><i class="bi bi-chat-square-text-fill"></i> Kho tri thức FAQ</span>
            </div>
        </div>
    </section>

    <c:if test="${not empty errorMessage}">
        <div class="search-error mb-4" role="alert">
            <i class="bi bi-exclamation-triangle-fill"></i>
            <div><c:out value="${errorMessage}"/></div>
        </div>
    </c:if>

    <div class="row search-workspace g-4">
        <div class="col-lg-3">
            <aside class="search-sidebar">
                <c:if test="${not empty category or not empty tag or (not empty sort and sort != 'RELEVANCE')}">
                    <a href="<c:url value='/search'><c:param name='q' value='${q}'/></c:url>" class="search-clear-filter">
                        <i class="bi bi-x-circle"></i>
                        Xóa bộ lọc
                    </a>
                </c:if>

                <section class="search-filter-card">
                    <div class="search-filter-heading">
                        <span class="search-filter-icon"><i class="bi bi-sort-down"></i></span>
                        <h2>Sắp xếp kết quả</h2>
                    </div>
                    <div class="search-filter-list list-group list-group-flush">
                        <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='RELEVANCE'/></c:url>"
                           class="list-group-item ${sort == 'RELEVANCE' or empty sort ? 'active fw-bold' : ''}">
                            <i class="bi bi-bullseye"></i>
                            Độ liên quan
                        </a>
                        <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='LATEST'/></c:url>"
                           class="list-group-item ${sort == 'LATEST' ? 'active fw-bold' : ''}">
                            <i class="bi bi-clock-history"></i>
                            Mới cập nhật
                        </a>
                        <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='MOST_VIEWED'/></c:url>"
                           class="list-group-item ${sort == 'MOST_VIEWED' ? 'active fw-bold' : ''}">
                            <i class="bi bi-eye"></i>
                            Xem nhiều nhất
                        </a>
                        <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty category}'><c:param name='category' value='${category}'/></c:if><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:param name='sort' value='MOST_HELPFUL'/></c:url>"
                           class="list-group-item ${sort == 'MOST_HELPFUL' ? 'active fw-bold' : ''}">
                            <i class="bi bi-hand-thumbs-up"></i>
                            Hữu ích nhất
                        </a>
                    </div>
                </section>

                <section class="search-filter-card">
                    <div class="search-filter-heading">
                        <span class="search-filter-icon"><i class="bi bi-grid-3x3-gap-fill"></i></span>
                        <h2>Chuyên mục</h2>
                    </div>
                    <div class="search-filter-list list-group list-group-flush">
                        <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                           class="list-group-item ${empty category ? 'active fw-bold' : ''}">
                            <i class="bi bi-grid"></i>
                            Tất cả chuyên mục
                        </a>

                        <c:forEach var="dept" items="${tree}">
                            <div class="search-filter-dept">
                                <i class="bi bi-building me-1"></i>
                                <c:out value="${dept.name()}"/>
                            </div>
                            <c:forEach var="cat" items="${dept.categories()}">
                                <a href="<c:url value='/search'><c:param name='q' value='${q}'/><c:param name='category' value='${cat.id()}'/><c:if test='${not empty tag}'><c:param name='tag' value='${tag}'/></c:if><c:if test='${not empty sort}'><c:param name='sort' value='${sort}'/></c:if></c:url>"
                                   class="list-group-item search-filter-category ${category == cat.id() ? 'active fw-bold' : ''}">
                                    <i class="bi <c:out value='${empty cat.icon() ? "bi-folder" : cat.icon()}'/>"></i>
                                    <span><c:out value="${cat.name()}"/></span>
                                </a>
                            </c:forEach>
                        </c:forEach>
                    </div>
                </section>
            </aside>
        </div>

        <div class="col-lg-9 search-main">
            <c:choose>
                <c:when test="${not empty q and page.totalElements == 0 and empty errorMessage}">
                    <section class="search-empty">
                        <div class="search-empty-icon">
                            <i class="bi bi-search"></i>
                        </div>
                        <h2>Không tìm thấy kết quả phù hợp</h2>
                        <p class="search-empty-lead">
                            Không tìm thấy kết quả cho từ khóa
                            <strong>“<c:out value="${q}"/>”</strong>.
                            Ông có thể thử từ khóa khác hoặc chọn một chuyên mục gợi ý bên dưới.
                        </p>

                        <div class="search-tips">
                            <div class="search-tips-heading">
                                <i class="bi bi-lightbulb-fill"></i>
                                Mẹo tìm kiếm
                            </div>
                            <ul>
                                <li>Kiểm tra lại chính tả của từ khóa tìm kiếm.</li>
                                <li>Thử dùng các từ khóa ngắn hơn hoặc các từ khóa thông dụng khác.</li>
                                <li>Bỏ bớt các bộ lọc chuyên mục hoặc chọn "Tất cả chuyên mục".</li>
                            </ul>
                        </div>

                        <div class="search-suggestions">
                            <div class="search-suggestions-heading">
                                <i class="bi bi-grid-fill"></i>
                                Các chuyên mục gợi ý
                            </div>
                            <div class="d-flex flex-wrap justify-content-center gap-2">
                                <c:forEach var="dept" items="${tree}">
                                    <c:forEach var="cat" items="${dept.categories()}">
                                        <a href="/faq?category=<c:out value='${cat.id()}'/>" class="search-suggestion-link">
                                            <i class="bi <c:out value='${empty cat.icon() ? "bi-folder" : cat.icon()}'/>"></i>
                                            <c:out value="${cat.name()}"/>
                                        </a>
                                    </c:forEach>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="search-empty-cta">
                            <p>Vẫn chưa tìm thấy thông tin bạn cần?</p>
                            <a href="<c:url value='/student/tickets/new'><c:param name='q' value='${q}'/></c:url>" class="btn btn-primary">
                                <i class="bi bi-pencil-square"></i>
                                Tạo yêu cầu hỗ trợ
                            </a>
                        </div>
                    </section>
                </c:when>

                <c:otherwise>
                    <c:if test="${not empty q and empty errorMessage}">
                        <div class="search-results-toolbar">
                            <div class="search-results-heading">
                                <span class="search-results-heading-icon">
                                    <i class="bi bi-journals"></i>
                                </span>
                                <div>
                                    <h2>Kết quả tìm kiếm</h2>
                                    <p>
                                        Tìm thấy <strong class="text-dark"><c:out value="${page.totalElements}"/></strong>
                                        kết quả cho từ khóa "<strong class="text-dark"><c:out value="${q}"/></strong>"
                                        <c:if test="${not empty selectedCategoryName}">
                                            trong chuyên mục "<strong class="text-dark"><c:out value="${selectedCategoryName}"/></strong>"
                                        </c:if>
                                    </p>
                                </div>
                            </div>
                            <span class="search-results-count">
                                <i class="bi bi-check2-circle"></i>
                                FAQ
                            </span>
                        </div>
                    </c:if>

                    <c:forEach var="item" items="${page.content}">
                        <article class="card search-result-card qa-card-hover">
                            <div class="card-body">
                                <h2 class="search-result-title">
                                    <a href="/faq/<c:out value='${item.slug()}'/>">
                                        <c:out value="${item.titleHtml()}" escapeXml="false"/>
                                    </a>
                                </h2>
                                <p class="search-result-snippet">
                                    <c:out value="${item.snippetHtml()}" escapeXml="false"/>
                                </p>

                                <div class="search-result-meta">
                                    <c:if test="${not empty item.categoryName()}">
                                        <span class="search-result-category">
                                            <i class="bi bi-folder2-open"></i>
                                            <c:out value="${item.categoryName()}"/>
                                        </span>
                                    </c:if>
                                    <span>
                                        <i class="bi bi-eye-fill"></i>
                                        <c:out value="${item.viewCount()}"/> lượt xem
                                    </span>
                                    <span>
                                        <i class="bi bi-hand-thumbs-up-fill"></i>
                                        <c:out value="${item.helpfulCount()}"/> hữu ích
                                    </span>
                                    <c:if test="${not empty item.updatedAt()}">
                                        <span>
                                            <i class="bi bi-clock-history"></i>
                                            Cập nhật: ${item.updatedAt().dayOfMonth}/${item.updatedAt().monthValue}/${item.updatedAt().year}
                                        </span>
                                    </c:if>
                                </div>

                                <div class="search-result-footer">
                                    <span class="text-secondary small">
                                        <i class="bi bi-journal-text me-1"></i>
                                        Bài viết trong kho tri thức QAUTE
                                    </span>
                                    <a href="/faq/<c:out value='${item.slug()}'/>" class="search-read-more">
                                        Đọc bài viết
                                        <i class="bi bi-arrow-right"></i>
                                    </a>
                                </div>
                            </div>
                        </article>
                    </c:forEach>

                    <div class="search-pagination">
                        <qa:pagination page="${page}" baseUrl="/search" params="${queryString}"/>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="search-bottom-help">
                <i class="bi bi-lightbulb-fill"></i>
                <div>
                    <strong>Gợi ý dành cho bạn</strong>
                    <span>Thử kết hợp từ khóa cụ thể với chuyên mục phù hợp để thu hẹp kết quả tìm kiếm.</span>
                </div>
            </div>
        </div>
    </div>
</div>