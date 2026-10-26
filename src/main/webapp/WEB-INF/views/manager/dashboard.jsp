<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Tổng quan tư vấn viên</title>
<div class="qa-page-manager-dashboard" data-qa-view="manager-dashboard">
<style>
/* ==========================================================================
   MANAGER DASHBOARD - MODERN DESIGN SYSTEM 2026
   ========================================================================== */
.qa-page-manager-dashboard {
  --mgr-primary: #1d60d8;
  --mgr-primary-rgb: 29, 96, 216;
  --mgr-primary-dark: #0f3d8a;
  --mgr-teal: #0d9488;
  --mgr-accent-amber: #f59e0b;
  --mgr-accent-rose: #ef4444;
  --mgr-accent-purple: #8b5cf6;
  --mgr-accent-indigo: #6366f1;
  --mgr-accent-emerald: #10b981;
  --mgr-accent-cyan: #06b6d4;
  --mgr-card-bg: linear-gradient(180deg, rgba(255, 255, 255, 0.98) 0%, rgba(248, 251, 255, 0.95) 100%);
  --mgr-border-soft: rgba(180, 206, 238, 0.45);
  --mgr-border-hover: rgba(29, 96, 216, 0.35);
  --mgr-shadow-subtle: 0 10px 30px rgba(15, 45, 84, 0.05);
  --mgr-shadow-card: 0 18px 40px rgba(15, 45, 84, 0.08);
  --mgr-shadow-hover: 0 26px 54px rgba(15, 45, 84, 0.14);
  --mgr-text-main: #19385c;
  --mgr-text-muted: #5e7696;
  position: relative;
  isolation: isolate;
  min-height: 100%;
  padding: clamp(1.25rem, 2.8vw, 2.5rem) 0 4.5rem;
  color: var(--mgr-text-main);
  background:
    radial-gradient(ellipse 60% 40% at 8% 0%, rgba(29, 96, 216, 0.14), transparent 45%),
    radial-gradient(ellipse 50% 35% at 95% 20%, rgba(13, 148, 136, 0.13), transparent 42%),
    radial-gradient(ellipse 40% 30% at 50% 85%, rgba(99, 102, 241, 0.08), transparent 50%),
    linear-gradient(180deg, #f4f8fe 0%, #edf3fb 45%, #f7fbff 100%);
}

.qa-page-manager-dashboard #manager-dashboard {
  max-width: 1600px;
  margin: 0 auto;
  padding: 0 clamp(1rem, 2.5vw, 2.25rem);
}

/* ---------- Typography & Utility Enhancements ---------- */
.qa-page-manager-dashboard .fw-800 { font-weight: 800; }
.qa-page-manager-dashboard .fw-850 { font-weight: 850; }

/* ---------- Unified Hero Banner ---------- */
.qa-page-manager-dashboard .qa-dashboard-hero {
  position: relative;
  isolation: isolate;
  overflow: hidden;
  display: grid;
  grid-template-columns: minmax(0, 1.4fr) minmax(290px, 0.85fr);
  gap: 1.75rem;
  margin-bottom: 1.6rem;
  padding: clamp(1.8rem, 3.2vw, 2.75rem);
  border: 1px solid rgba(255, 255, 255, 0.32);
  border-radius: 32px;
  color: #ffffff;
  background:
    radial-gradient(circle at 92% 10%, rgba(13, 148, 136, 0.32), transparent 30%),
    radial-gradient(circle at 5% 95%, rgba(139, 92, 246, 0.28), transparent 36%),
    linear-gradient(135deg, #0a1b38 0%, #12458f 48%, #0f766e 100%);
  box-shadow: 0 28px 65px rgba(12, 43, 85, 0.28), inset 0 1px 0 rgba(255, 255, 255, 0.2);
  animation: qaMgrRise 0.6s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.qa-page-manager-dashboard .qa-dashboard-hero::before {
  content: "";
  position: absolute;
  z-index: -1;
  top: -140px;
  right: -50px;
  width: 380px;
  height: 380px;
  border-radius: 50%;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 0 0 28px rgba(255, 255, 255, 0.03), 0 0 0 72px rgba(255, 255, 255, 0.015);
  pointer-events: none;
}

.qa-page-manager-dashboard .qa-dashboard-hero::after {
  content: "";
  position: absolute;
  z-index: -1;
  left: 48%;
  bottom: -160px;
  width: 320px;
  height: 320px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.12), transparent 68%);
  pointer-events: none;
}

.qa-page-manager-dashboard .qa-dashboard-hero-content {
  position: relative;
  z-index: 1;
  max-width: 780px;
}

.qa-page-manager-dashboard .qa-dashboard-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 0.55rem;
  margin-bottom: 1.1rem;
  padding: 0.45rem 1rem;
  border: 1px solid rgba(255, 255, 255, 0.24);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  color: #d8eeff;
  font-size: 0.74rem;
  font-weight: 850;
  letter-spacing: 0.16em;
  text-transform: uppercase;
  backdrop-filter: blur(12px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

.qa-page-manager-dashboard .qa-dashboard-eyebrow .qa-pulse-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: #2dd4bf;
  box-shadow: 0 0 0 0 rgba(45, 212, 191, 0.7);
  animation: qaMgrPulse 2s infinite;
}

.qa-page-manager-dashboard .qa-view-title {
  display: inline-flex;
  align-items: center;
  gap: 0.95rem;
  margin: 0 0 0.85rem;
  color: #ffffff;
  font-size: clamp(1.85rem, 3.2vw, 3rem);
  font-weight: 850;
  letter-spacing: -0.04em;
  line-height: 1.15;
}

.qa-page-manager-dashboard .qa-view-title-icon {
  display: inline-grid;
  flex: 0 0 54px;
  width: 54px;
  height: 54px;
  place-items: center;
  border-radius: 18px;
  color: #0c4384;
  background: linear-gradient(145deg, #ffffff, #dff6f5);
  box-shadow: 0 12px 24px rgba(3, 20, 48, 0.28);
  font-size: 1.55rem;
}

.qa-page-manager-dashboard .qa-dashboard-hero p {
  max-width: 670px;
  margin: 0 0 1.35rem;
  color: #d7eaff;
  font-size: 1.02rem;
  line-height: 1.75;
}

.qa-page-manager-dashboard .qa-manager-hero-badges {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
  margin: 0 0 1.6rem;
}

.qa-page-manager-dashboard .qa-manager-hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.6rem;
  padding: 0.68rem 1rem;
  border: 1px solid rgba(255, 255, 255, 0.18);
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.1);
  color: #f1f8ff;
  font-size: 0.85rem;
  font-weight: 650;
  backdrop-filter: blur(14px);
  transition: transform 0.2s ease, background 0.2s ease;
}

.qa-page-manager-dashboard .qa-manager-hero-badge:hover {
  transform: translateY(-2px);
  background: rgba(255, 255, 255, 0.16);
}

.qa-page-manager-dashboard .qa-manager-hero-badge .bi {
  display: inline-grid;
  width: 32px;
  height: 32px;
  place-items: center;
  border-radius: 11px;
  background: rgba(255, 255, 255, 0.18);
  font-size: 0.98rem;
  color: #2dd4bf;
}

.qa-page-manager-dashboard .qa-dashboard-hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 0.9rem;
}

/* ---------- Button Enhancements ---------- */
.qa-page-manager-dashboard .btn {
  border-radius: 16px;
  font-weight: 750;
  letter-spacing: -0.01em;
  padding: 0.75rem 1.35rem;
  transition: all 0.24s cubic-bezier(0.16, 1, 0.3, 1);
}

.qa-page-manager-dashboard .btn-sm {
  padding: 0.45rem 0.95rem;
  border-radius: 12px;
  font-size: 0.82rem;
}

.qa-page-manager-dashboard .btn:not(:disabled):not(.btn-close):hover {
  transform: translateY(-2px);
}

.qa-page-manager-dashboard .btn-white {
  color: #0b3874;
  border: 1px solid rgba(255, 255, 255, 0.9);
  background: linear-gradient(180deg, #ffffff 0%, #eaf3fe 100%);
  box-shadow: 0 10px 24px rgba(4, 21, 48, 0.22);
}

.qa-page-manager-dashboard .btn-white:hover {
  color: #082d5e;
  background: #ffffff;
  box-shadow: 0 14px 28px rgba(4, 21, 48, 0.28);
}

.qa-page-manager-dashboard .btn-outline-light {
  border-color: rgba(255, 255, 255, 0.38);
  background: rgba(255, 255, 255, 0.12);
  color: #ffffff;
  backdrop-filter: blur(12px);
}

.qa-page-manager-dashboard .btn-outline-light:hover {
  color: #ffffff;
  border-color: rgba(255, 255, 255, 0.65);
  background: rgba(255, 255, 255, 0.22);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

/* ---------- Hero Aside & Visual ---------- */
.qa-page-manager-dashboard .qa-dashboard-hero-aside {
  position: relative;
  z-index: 1;
  display: grid;
  align-content: space-between;
  gap: 1.1rem;
}

.qa-page-manager-dashboard .qa-dashboard-date {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  width: fit-content;
  margin-left: 0;
  padding: 0.6rem 1.1rem;
  border: 1px solid rgba(255, 255, 255, 0.25);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  color: #eaf4ff;
  font-size: 0.82rem;
  font-weight: 750;
  backdrop-filter: blur(14px);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.qa-page-manager-dashboard .qa-dashboard-hero-meta {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 0.75rem;
}

.qa-page-manager-dashboard .qa-dashboard-hero-mark {
  display: grid;
  flex: 0 0 50px;
  width: 50px;
  height: 50px;
  place-items: center;
  border: 1px solid rgba(255, 255, 255, 0.22);
  border-radius: 17px;
  background: rgba(255, 255, 255, 0.14);
  color: rgba(214, 248, 255, 0.92);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.18), 0 10px 24px rgba(3, 16, 38, 0.16);
  backdrop-filter: blur(14px);
  font-size: 1.55rem;
  animation: qaMgrFloat 4.6s ease-in-out infinite;
}

.qa-page-manager-dashboard .qa-manager-hero-visual {
  position: relative;
  min-height: 270px;
  padding: 1.4rem;
  border: 1px solid rgba(255, 255, 255, 0.18);
  border-radius: 28px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.14) 0%, rgba(255, 255, 255, 0.05) 100%);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.18), 0 20px 40px rgba(4, 18, 42, 0.2);
  backdrop-filter: blur(18px);
}

.qa-page-manager-dashboard .qa-manager-hero-stack {
  position: absolute;
  left: 1.25rem;
  right: 1.25rem;
  bottom: 1.25rem;
  display: grid;
  gap: 0.75rem;
}

.qa-page-manager-dashboard .qa-manager-hero-note {
  padding: 0.95rem 1.05rem;
  border: 1px solid rgba(255, 255, 255, 0.16);
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.12);
  color: #f0f7ff;
  backdrop-filter: blur(12px);
}

.qa-page-manager-dashboard .qa-manager-hero-note strong {
  display: flex;
  align-items: center;
  gap: 0.55rem;
  margin-bottom: 0.3rem;
  font-weight: 850;
  font-size: 0.88rem;
  letter-spacing: -0.01em;
}

.qa-page-manager-dashboard .qa-manager-hero-note strong .bi {
  display: inline-grid;
  width: 28px;
  height: 28px;
  place-items: center;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.18);
  font-size: 0.88rem;
  color: #2dd4bf;
}

.qa-page-manager-dashboard .qa-manager-hero-note p {
  margin: 0;
  color: #d6eaff;
  font-size: 0.82rem;
  line-height: 1.65;
}

/* ---------- 3 Highlights Cards (Synchronized with Student & Admin) ---------- */
.qa-page-manager-dashboard .qa-manager-overview-strip {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 1.15rem;
  margin-bottom: 1.65rem;
}

.qa-page-manager-dashboard .qa-manager-overview-card {
  position: relative;
  overflow: hidden;
  min-height: 156px;
  padding: 1.35rem 1.4rem;
  border: 1px solid var(--mgr-border-soft);
  border-radius: 26px;
  background: linear-gradient(160deg, rgba(255, 255, 255, 0.98) 0%, rgba(245, 250, 255, 0.94) 100%);
  box-shadow: var(--mgr-shadow-subtle);
  transition: transform 0.24s ease, box-shadow 0.24s ease, border-color 0.24s ease;
}

.qa-page-manager-dashboard .qa-manager-overview-card:hover {
  transform: translateY(-4px);
  border-color: var(--mgr-border-hover);
  box-shadow: var(--mgr-shadow-card);
}

.qa-page-manager-dashboard .qa-manager-overview-card::after {
  content: "";
  position: absolute;
  right: -32px;
  bottom: -44px;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 1px solid rgba(29, 96, 216, 0.08);
  box-shadow: 0 0 0 20px rgba(29, 96, 216, 0.03);
  pointer-events: none;
}

.qa-page-manager-dashboard .qa-manager-overview-card h3 {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin: 0 0 0.55rem;
  color: #12345e;
  font-size: 1.05rem;
  font-weight: 850;
  letter-spacing: -0.02em;
}

.qa-page-manager-dashboard .qa-manager-overview-card h3 .bi {
  display: inline-grid;
  width: 42px;
  height: 42px;
  place-items: center;
  border-radius: 15px;
  color: #ffffff;
  background: linear-gradient(135deg, #ea580c 0%, #c2410c 100%);
  box-shadow: 0 10px 20px rgba(234, 88, 12, 0.25);
  font-size: 1.15rem;
}

.qa-page-manager-dashboard .qa-manager-overview-card:nth-child(2) h3 .bi {
  background: linear-gradient(135deg, #0d9488 0%, #0f766e 100%);
  box-shadow: 0 10px 20px rgba(13, 148, 136, 0.25);
}

.qa-page-manager-dashboard .qa-manager-overview-card:nth-child(3) h3 .bi {
  background: linear-gradient(135deg, #1d60d8 0%, #0e3f94 100%);
  box-shadow: 0 10px 20px rgba(29, 96, 216, 0.25);
}

.qa-page-manager-dashboard .qa-manager-overview-card p {
  margin: 0 0 1rem;
  color: var(--mgr-text-muted);
  font-size: 0.88rem;
  line-height: 1.7;
}

.qa-page-manager-dashboard .qa-manager-overview-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 0.6rem;
}

.qa-page-manager-dashboard .qa-manager-overview-meta span {
  display: inline-flex;
  align-items: center;
  gap: 0.45rem;
  padding: 0.42rem 0.82rem;
  border-radius: 999px;
  background: #edf5ff;
  color: #275283;
  font-size: 0.77rem;
  font-weight: 750;
  border: 1px solid rgba(190, 215, 245, 0.6);
}

/* ---------- Section Header Labels ---------- */
.qa-page-manager-dashboard .qa-dashboard-section-label {
  display: flex;
  align-items: end;
  justify-content: space-between;
  gap: 1rem;
  margin: 0 0 1.15rem;
  padding: 0 0.25rem;
}

.qa-page-manager-dashboard .qa-dashboard-section-label h2 {
  display: flex;
  align-items: center;
  gap: 0.85rem;
  margin: 0;
  color: #12345e;
  font-size: 1.25rem;
  font-weight: 850;
  letter-spacing: -0.03em;
}

.qa-page-manager-dashboard .qa-dashboard-section-label h2 .bi {
  display: inline-grid;
  width: 42px;
  height: 42px;
  place-items: center;
  border-radius: 14px;
  color: #ffffff;
  background: linear-gradient(135deg, #1d60d8 0%, #0d428d 100%);
  box-shadow: 0 12px 24px rgba(29, 96, 216, 0.25);
  font-size: 1.15rem;
}

.qa-page-manager-dashboard .qa-dashboard-section-label > span {
  color: var(--mgr-text-muted);
  font-size: 0.85rem;
  font-weight: 600;
}

/* ---------- KPI Grid (Synchronized Metric Cards) ---------- */
.qa-page-manager-dashboard .qa-kpi-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 1.15rem;
  margin-bottom: 2rem;
}

.qa-page-manager-dashboard .qa-kpi {
  position: relative;
  isolation: isolate;
  overflow: hidden;
  min-height: 195px;
  display: flex;
  flex-direction: column;
  padding: 1.35rem 1.35rem 1.2rem;
  border: 1px solid var(--mgr-border-soft);
  border-radius: 26px;
  background:
    radial-gradient(circle at 100% 0%, var(--mgr-kpi-tint, rgba(29, 96, 216, 0.08)) 0%, transparent 55%),
    linear-gradient(180deg, rgba(255, 255, 255, 0.99) 0%, rgba(247, 251, 255, 0.96) 100%);
  box-shadow: var(--mgr-shadow-subtle);
  text-decoration: none;
  color: inherit;
  transition: transform 0.26s cubic-bezier(0.16, 1, 0.3, 1), box-shadow 0.26s cubic-bezier(0.16, 1, 0.3, 1), border-color 0.26s ease;
}

.qa-page-manager-dashboard .qa-kpi::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--mgr-kpi-accent), color-mix(in srgb, var(--mgr-kpi-accent) 40%, transparent));
}

.qa-page-manager-dashboard .qa-kpi::after {
  content: "";
  position: absolute;
  right: -24px;
  bottom: -32px;
  width: 130px;
  height: 130px;
  border-radius: 50%;
  border: 1px solid color-mix(in srgb, var(--mgr-kpi-accent) 15%, transparent);
  box-shadow: 0 0 0 20px color-mix(in srgb, var(--mgr-kpi-accent) 4%, transparent);
  pointer-events: none;
  transition: transform 0.3s ease;
}

.qa-page-manager-dashboard .qa-kpi:hover {
  transform: translateY(-5px);
  border-color: color-mix(in srgb, var(--mgr-kpi-accent) 35%, var(--mgr-border-soft));
  box-shadow: 0 24px 50px rgba(15, 45, 84, 0.13);
  color: inherit;
}

.qa-page-manager-dashboard .qa-kpi:hover::after {
  transform: scale(1.1);
}

.qa-page-manager-dashboard .qa-kpi:nth-child(1) { --mgr-kpi-accent: #ea580c; --mgr-kpi-tint: rgba(234, 88, 12, 0.12); }
.qa-page-manager-dashboard .qa-kpi:nth-child(2) { --mgr-kpi-accent: #2563eb; --mgr-kpi-tint: rgba(37, 99, 235, 0.12); }
.qa-page-manager-dashboard .qa-kpi:nth-child(3) { --mgr-kpi-accent: #8b5cf6; --mgr-kpi-tint: rgba(139, 92, 246, 0.12); }
.qa-page-manager-dashboard .qa-kpi:nth-child(4) { --mgr-kpi-accent: #ef4444; --mgr-kpi-tint: rgba(239, 68, 68, 0.12); }
.qa-page-manager-dashboard .qa-kpi:nth-child(5) { --mgr-kpi-accent: #6366f1; --mgr-kpi-tint: rgba(99, 102, 241, 0.12); }
.qa-page-manager-dashboard .qa-kpi:nth-child(6) { --mgr-kpi-accent: #10b981; --mgr-kpi-tint: rgba(16, 185, 129, 0.12); }
.qa-page-manager-dashboard .qa-kpi:nth-child(7) { --mgr-kpi-accent: #f59e0b; --mgr-kpi-tint: rgba(245, 158, 11, 0.12); }
.qa-page-manager-dashboard .qa-kpi:nth-child(8) { --mgr-kpi-accent: #06b6d4; --mgr-kpi-tint: rgba(6, 182, 212, 0.12); }

.qa-page-manager-dashboard .qa-kpi-heading {
  display: flex;
  align-items: start;
  justify-content: space-between;
  gap: 0.75rem;
  min-height: 52px;
}

.qa-page-manager-dashboard .qa-kpi-label {
  max-width: 76%;
  color: #55708f;
  font-size: 0.75rem;
  font-weight: 850;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  line-height: 1.5;
}

.qa-page-manager-dashboard .qa-kpi-icon {
  display: inline-grid;
  flex: 0 0 48px;
  width: 48px;
  height: 48px;
  place-items: center;
  border: 1px solid color-mix(in srgb, var(--mgr-kpi-accent) 22%, transparent);
  border-radius: 16px;
  color: var(--mgr-kpi-accent);
  background: color-mix(in srgb, var(--mgr-kpi-accent) 10%, #ffffff);
  font-size: 1.35rem;
  box-shadow: 0 6px 16px color-mix(in srgb, var(--mgr-kpi-accent) 12%, transparent);
}

.qa-page-manager-dashboard .qa-kpi-value {
  position: relative;
  z-index: 1;
  margin: 0.85rem 0 0.35rem;
  color: #12345e;
  font-size: clamp(2.1rem, 2.7vw, 2.85rem);
  font-weight: 850;
  letter-spacing: -0.06em;
  line-height: 1;
}

.qa-page-manager-dashboard .qa-kpi-foot {
  position: relative;
  z-index: 1;
  color: var(--mgr-text-muted);
  font-size: 0.83rem;
  line-height: 1.6;
}

.qa-page-manager-dashboard .qa-kpi-trail {
  display: flex;
  align-items: center;
  gap: 0.45rem;
  margin-top: auto;
  padding-top: 0.85rem;
  color: #6a82a0;
  font-size: 0.76rem;
  font-weight: 750;
  transition: transform 0.2s ease, color 0.2s ease;
}

.qa-page-manager-dashboard .qa-kpi:hover .qa-kpi-trail {
  color: var(--mgr-kpi-accent);
  transform: translateX(3px);
}

.qa-page-manager-dashboard .qa-kpi-trail .bi {
  color: var(--mgr-kpi-accent);
  font-size: 0.95rem;
}

/* ---------- Card & Header Standard ---------- */
.qa-page-manager-dashboard .card {
  position: relative;
  overflow: hidden;
  border: 1px solid var(--mgr-border-soft);
  border-radius: 28px;
  background: var(--mgr-card-bg);
  box-shadow: var(--mgr-shadow-subtle);
  transition: transform 0.24s ease, box-shadow 0.24s ease, border-color 0.24s ease;
}

.qa-page-manager-dashboard .card:hover {
  border-color: var(--mgr-border-hover);
  box-shadow: var(--mgr-shadow-card);
}

.qa-page-manager-dashboard .card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.85rem;
  min-height: 78px;
  padding: 1.2rem 1.4rem;
  border-bottom: 1px solid rgba(215, 230, 248, 0.7);
  border-radius: 28px 28px 0 0;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98) 0%, rgba(240, 246, 255, 0.9) 100%);
}

.qa-page-manager-dashboard .card-title {
  margin: 0;
  color: #12345e;
  font-size: 1.05rem;
  font-weight: 850;
  letter-spacing: -0.02em;
}

.qa-page-manager-dashboard .qa-card-heading-icon {
  display: inline-grid;
  flex: 0 0 42px;
  width: 42px;
  height: 42px;
  place-items: center;
  border-radius: 15px;
  color: #ffffff;
  background: linear-gradient(135deg, #1d60d8 0%, #0d428d 100%);
  box-shadow: 0 10px 20px rgba(29, 96, 216, 0.22);
  font-size: 1.1rem;
}

.qa-page-manager-dashboard .qa-card-period {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.45rem 0.85rem;
  border: 1px solid rgba(200, 220, 245, 0.8);
  border-radius: 999px;
  background: rgba(245, 250, 255, 0.92);
  color: #4b6b90;
  font-size: 0.74rem;
  font-weight: 800;
  text-decoration: none;
  white-space: nowrap;
}

.qa-page-manager-dashboard .qa-card-period:hover {
  color: #1d60d8;
  border-color: rgba(29, 96, 216, 0.35);
  background: #ffffff;
}

/* ---------- Charts Layout ---------- */
.qa-page-manager-dashboard .qa-manager-main-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.5fr) minmax(330px, 0.88fr);
  gap: 1.35rem;
  margin-bottom: 1.5rem;
}

.qa-page-manager-dashboard .qa-manager-side-stack {
  display: grid;
  gap: 1.35rem;
}

.qa-page-manager-dashboard .qa-dashboard-chart {
  min-height: 330px;
  padding: 1.35rem;
}

.qa-page-manager-dashboard .qa-dashboard-chart canvas {
  max-width: 100%;
}

.qa-page-manager-dashboard .qa-manager-side-panel .card-body {
  padding: 1.35rem;
}

.qa-page-manager-dashboard .qa-manager-side-list {
  display: grid;
  gap: 0.8rem;
}

.qa-page-manager-dashboard .qa-manager-side-link {
  display: flex;
  align-items: center;
  gap: 0.95rem;
  padding: 1rem 1.15rem;
  border: 1px solid rgba(220, 233, 248, 0.95);
  border-radius: 20px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.99) 0%, rgba(248, 252, 255, 0.96) 100%);
  color: inherit;
  text-decoration: none;
  transition: transform 0.22s ease, border-color 0.22s ease, box-shadow 0.22s ease;
}

.qa-page-manager-dashboard .qa-manager-side-link:hover {
  color: inherit;
  transform: translateY(-3px);
  border-color: rgba(29, 96, 216, 0.3);
  box-shadow: 0 16px 30px rgba(15, 45, 84, 0.08);
}

.qa-page-manager-dashboard .qa-manager-side-link .qa-kpi-icon {
  flex: 0 0 46px;
  width: 46px;
  height: 46px;
  border-radius: 15px;
}

.qa-page-manager-dashboard .qa-manager-side-link strong {
  display: block;
  color: #12345e;
  font-size: 0.98rem;
  font-weight: 750;
}

/* ---------- Work Grid & Urgent Tasks ---------- */
.qa-page-manager-dashboard .qa-dashboard-work-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.45fr) minmax(320px, 0.9fr);
  gap: 1.35rem;
}

.qa-page-manager-dashboard .qa-dashboard-work-grid > [class*="col-"] {
  width: auto;
  max-width: none;
  padding: 0;
}

.qa-page-manager-dashboard .list-group-item {
  margin: 0.55rem 0.85rem;
  padding: 1.1rem 1.25rem;
  border: 1px solid rgba(224, 235, 248, 0.95);
  border-radius: 20px !important;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.99) 0%, rgba(248, 252, 255, 0.96) 100%);
  transition: transform 0.22s ease, border-color 0.22s ease, box-shadow 0.22s ease;
}

.qa-page-manager-dashboard .list-group-item:hover {
  transform: translateY(-3px);
  border-color: rgba(29, 96, 216, 0.28);
  box-shadow: 0 16px 30px rgba(15, 45, 84, 0.08);
}

.qa-page-manager-dashboard .qa-dashboard-task {
  display: flex;
  align-items: center;
  gap: 0.95rem;
  min-height: 80px;
}

.qa-page-manager-dashboard .qa-dashboard-task-body {
  min-width: 0;
  flex: 1 1 auto;
}

.qa-page-manager-dashboard .qa-dashboard-task-body strong {
  display: block;
  margin-bottom: 0.25rem;
  color: #12345e;
  font-size: 0.98rem;
  font-weight: 750;
}

.qa-page-manager-dashboard .qa-dashboard-empty {
  display: flex;
  align-items: center;
  gap: 0.85rem;
  min-height: 96px;
  color: #0f766e;
  background: linear-gradient(110deg, #ecfdf5 0%, #ffffff 100%);
  border-radius: 20px;
  font-weight: 650;
}

.qa-page-manager-dashboard .qa-dashboard-empty .bi {
  color: #10b981;
  font-size: 1.45rem;
}

.qa-page-manager-dashboard .qa-dashboard-loading {
  margin-bottom: 1.5rem;
  padding: 1.25rem;
  border: 1px solid rgba(185, 210, 240, 0.45);
  border-radius: 26px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98) 0%, rgba(242, 248, 255, 0.96) 100%);
  box-shadow: 0 16px 32px rgba(15, 45, 84, 0.07);
}

.qa-page-manager-dashboard .qa-dashboard-loading .qa-skeleton {
  min-height: 150px;
  border-radius: 20px;
}

/* ---------- Keyframes ---------- */
@keyframes qaMgrPulse {
  0% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(45, 212, 191, 0.7); }
  70% { transform: scale(1); box-shadow: 0 0 0 7px rgba(45, 212, 191, 0); }
  100% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(45, 212, 191, 0); }
}

@keyframes qaMgrRise {
  from { opacity: 0; transform: translateY(14px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes qaMgrFloat {
  0%, 100% { transform: translateY(0) rotate(-3deg); }
  50% { transform: translateY(-10px) rotate(4deg); }
}

@media (max-width: 1199.98px) {
  .qa-page-manager-dashboard .qa-dashboard-hero { grid-template-columns: minmax(0, 1fr); }
  .qa-page-manager-dashboard .qa-manager-overview-strip { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .qa-page-manager-dashboard .qa-kpi-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .qa-page-manager-dashboard .qa-manager-main-grid,
  .qa-page-manager-dashboard .qa-dashboard-work-grid { grid-template-columns: minmax(0, 1fr); }
}

@media (max-width: 767.98px) {
  .qa-page-manager-dashboard #manager-dashboard { padding: 0 0.85rem; }
  .qa-page-manager-dashboard .qa-dashboard-hero { padding: 1.45rem; border-radius: 24px; }
  .qa-page-manager-dashboard .qa-view-title { font-size: clamp(1.5rem, 6.2vw, 2.2rem); }
  .qa-page-manager-dashboard .qa-view-title-icon { flex-basis: 44px; width: 44px; height: 44px; }
  .qa-page-manager-dashboard .qa-manager-hero-visual { min-height: 210px; }
  .qa-page-manager-dashboard .qa-manager-overview-strip { grid-template-columns: minmax(0, 1fr); }
  .qa-page-manager-dashboard .qa-dashboard-section-label { flex-direction: column; align-items: flex-start; }
  .qa-page-manager-dashboard .qa-kpi-grid { grid-template-columns: minmax(0, 1fr); }
}

@media (prefers-reduced-motion: reduce) {
  .qa-page-manager-dashboard *,
  .qa-page-manager-dashboard *::before,
  .qa-page-manager-dashboard *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
    scroll-behavior: auto !important;
  }
}
</style>

<div id="manager-dashboard" data-api="${ctx}/api/manager/dashboard" data-context="${ctx}">
  <%-- ==================== HERO BANNER ==================== --%>
  <section class="qa-dashboard-hero" aria-labelledby="manager-welcome">
    <div class="qa-dashboard-hero-content">
      <span class="qa-dashboard-eyebrow">
        <span class="qa-pulse-dot" aria-hidden="true"></span>
        <i class="bi bi-person-workspace me-1" aria-hidden="true"></i>Không gian tư vấn viên
      </span>
      <h1 id="manager-welcome" class="qa-view-title" data-qa-page="manager-dashboard">
        <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-stars"></i></span>
        Xin chào, <c:out value="${me.fullName}"/>!
      </h1>
      <p>Bảng điều khiển chuyên trách giúp bạn quản lý hàng đợi, theo dõi cam kết SLA, phản hồi tư vấn trực tuyến và nâng cao chất lượng phục vụ sinh viên mỗi ngày.</p>
      <div class="qa-manager-hero-badges">
        <span class="qa-manager-hero-badge"><i class="bi bi-lightning-charge-fill"></i>Ưu tiên việc cần xử lý ngay</span>
        <span class="qa-manager-hero-badge"><i class="bi bi-bar-chart-fill"></i>Theo dõi hiệu suất trực quan</span>
        <span class="qa-manager-hero-badge"><i class="bi bi-shield-check"></i>Đồng bộ dữ liệu thời gian thực</span>
      </div>
      <div class="qa-dashboard-hero-actions">
        <a class="btn btn-white shadow-sm" href="${ctx}/manager/queue"><i class="bi bi-inbox-fill me-2"></i>Mở hàng đợi</a>
        <a class="btn btn-outline-light" href="${ctx}/manager/chat"><i class="bi bi-chat-dots-fill me-2"></i>Tư vấn trực tuyến</a>
      </div>
    </div>
    <div class="qa-dashboard-hero-aside" aria-hidden="true">
      <div class="qa-dashboard-hero-meta">
        <span class="qa-dashboard-date" data-workspace-date><i class="bi bi-calendar3 me-1"></i>Hôm nay</span>
        <span class="qa-dashboard-hero-mark"><i class="bi bi-chat-square-heart-fill"></i></span>
      </div>
      <div class="qa-manager-hero-visual">
        <div class="qa-manager-hero-stack">
          <div class="qa-manager-hero-note">
            <strong><i class="bi bi-clipboard2-pulse-fill"></i>Trọng tâm</strong>
            <p>KPI, biểu đồ và danh sách ưu tiên được gom thành cụm rõ ràng để bạn nắm bắt nhanh nhất.</p>
          </div>
          <div class="qa-manager-hero-note">
            <strong><i class="bi bi-calendar2-check-fill"></i>Nhịp làm việc</strong>
            <p>Lịch tư vấn, chat chờ và hàng đợi xử lý hiển thị trực quan và hỗ trợ thao tác mượt mà.</p>
          </div>
        </div>
      </div>
    </div>
  </section>

  <%-- ==================== 3 HIGHLIGHTS CARDS ==================== --%>
  <section class="qa-manager-overview-strip" aria-label="Điểm nhấn vận hành">
    <div class="qa-manager-overview-card">
      <h3><i class="bi bi-inboxes-fill"></i>Điều phối hàng đợi</h3>
      <p>Thấy nhanh ticket chưa nhận, ticket đang phụ trách và nhóm rủi ro SLA để vào việc đúng thứ tự ưu tiên.</p>
      <div class="qa-manager-overview-meta">
        <span><i class="bi bi-exclamation-triangle-fill"></i>SLA nổi bật</span>
        <span><i class="bi bi-arrow-left-right"></i>Luồng nhận việc rõ</span>
      </div>
    </div>
    <div class="qa-manager-overview-card">
      <h3><i class="bi bi-chat-heart-fill"></i>Hỗ trợ đa kênh</h3>
      <p>Chat trực tuyến, lịch hẹn và queue được đặt trong cùng ngữ cảnh giúp chuyển tác vụ nhanh và liền mạch.</p>
      <div class="qa-manager-overview-meta">
        <span><i class="bi bi-chat-dots-fill"></i>Chat chờ</span>
        <span><i class="bi bi-calendar-week-fill"></i>Lịch trong ngày</span>
      </div>
    </div>
    <div class="qa-manager-overview-card">
      <h3><i class="bi bi-graph-up-arrow"></i>Theo dõi chất lượng</h3>
      <p>Chỉ số CSAT, tốc độ phản hồi đầu và phân bố chuyên mục được biểu thị sinh động nhằm cải thiện dịch vụ liên tục.</p>
      <div class="qa-manager-overview-meta">
        <span><i class="bi bi-star-fill"></i>CSAT</span>
        <span><i class="bi bi-stopwatch-fill"></i>FRT trung vị</span>
      </div>
    </div>
  </section>

  <%-- Trạng thái tải hoặc lỗi trước khi dữ liệu tổng quan sẵn sàng --%>
  <div id="mgr-dashboard-loading" class="qa-dashboard-loading" role="status">
    <span class="visually-hidden">Đang tải dữ liệu tổng quan…</span>
    <div class="row g-3">
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
    </div>
  </div>
  <div id="mgr-dashboard-error" class="alert alert-danger d-none" role="alert"></div>

  <%-- ==================== MAIN DASHBOARD CONTENT ==================== --%>
  <div id="mgr-dashboard-content" class="d-none">
    <div class="qa-dashboard-section-label">
      <h2><i class="bi bi-clipboard2-pulse-fill" aria-hidden="true"></i>Công việc & chất lượng tư vấn</h2>
      <span>Trong phạm vi phụ trách của bạn</span>
    </div>

    <%-- 8 KPI Metric Cards --%>
    <div class="qa-kpi-grid">
      <a class="qa-kpi" href="${ctx}/manager/queue?tab=unassigned">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Chưa nhận trong phạm vi</span>
          <span class="qa-kpi-icon"><i class="bi bi-inbox-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-unassigned" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Các yêu cầu đang sẵn sàng để bạn tiếp nhận.</span>
        <span class="qa-kpi-trail"><i class="bi bi-arrow-up-right-circle-fill"></i>Mở nhóm chưa nhận</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/queue?tab=mine">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Yêu cầu đang phụ trách</span>
          <span class="qa-kpi-icon"><i class="bi bi-person-workspace" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-mine" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Khối lượng công việc hiện tại bạn đang theo dõi trực tiếp.</span>
        <span class="qa-kpi-trail"><i class="bi bi-person-check-fill"></i>Xem ticket của tôi</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/queue?tab=waiting">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Chờ sinh viên phản hồi</span>
          <span class="qa-kpi-icon"><i class="bi bi-hourglass-split" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-waiting" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Nhóm yêu cầu cần theo dõi thông tin bổ sung từ sinh viên.</span>
        <span class="qa-kpi-trail"><i class="bi bi-reply-fill"></i>Theo dõi phản hồi</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/queue?tab=slarisk">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">SLA rủi ro / quá hạn</span>
          <span class="qa-kpi-icon"><i class="bi bi-exclamation-triangle-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-sla" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Những trường hợp cần ưu tiên để tránh trễ hạn hoặc vượt SLA.</span>
        <span class="qa-kpi-trail"><i class="bi bi-fire"></i>Ưu tiên xử lý</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/appointments?tab=today">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Lịch hẹn hôm nay</span>
          <span class="qa-kpi-icon"><i class="bi bi-calendar-check-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-appointments" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Số buổi tư vấn trong ngày bạn cần chuẩn bị và theo dõi.</span>
        <span class="qa-kpi-trail"><i class="bi bi-calendar2-week-fill"></i>Mở lịch trong ngày</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/chat">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Hội thoại đang chờ</span>
          <span class="qa-kpi-icon"><i class="bi bi-chat-dots-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-chats" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Phiên chat đang chờ phản hồi để bạn hỗ trợ sinh viên kịp thời.</span>
        <span class="qa-kpi-trail"><i class="bi bi-chat-left-heart-fill"></i>Vào kênh chat</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/reports">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Mức độ hài lòng</span>
          <span class="qa-kpi-icon"><i class="bi bi-star-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-csat" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Điểm đánh giá dịch vụ trong 30 ngày gần nhất.</span>
        <span class="qa-kpi-trail"><i class="bi bi-bar-chart-line-fill"></i>Xem hiệu quả phục vụ</span>
      </a>

      <a class="qa-kpi" href="${ctx}/manager/reports">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Phản hồi đầu trung vị</span>
          <span class="qa-kpi-icon"><i class="bi bi-stopwatch-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="mgr-k-frt" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Tốc độ phản hồi đầu trong 30 ngày, theo phút làm việc.</span>
        <span class="qa-kpi-trail"><i class="bi bi-speedometer2"></i>Theo dõi tốc độ</span>
      </a>
    </div>

    <%-- Charts & Operations Grid --%>
    <div class="qa-manager-main-grid">
      <div class="card h-100">
        <div class="card-header">
          <div class="d-flex align-items-center gap-2">
            <span class="qa-card-heading-icon"><i class="bi bi-graph-up" aria-hidden="true"></i></span>
            <h2 class="card-title mb-0">Yêu cầu theo ngày</h2>
          </div>
          <span class="qa-card-period">14 ngày gần nhất</span>
        </div>
        <div class="card-body qa-dashboard-chart"><canvas id="mgr-chart-daily" role="img" aria-label="Yêu cầu theo ngày"></canvas></div>
      </div>
      <div class="qa-manager-side-stack">
        <div class="card qa-manager-side-panel h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-grid-1x2-fill" aria-hidden="true"></i></span>
              <h2 class="card-title mb-0">Bức tranh chuyên mục</h2>
            </div>
            <span class="qa-card-period">14 ngày gần nhất</span>
          </div>
          <div class="card-body qa-dashboard-chart"><canvas id="mgr-chart-category" role="img" aria-label="Phân bố theo chuyên mục"></canvas></div>
        </div>
        <div class="card qa-manager-side-panel">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-compass-fill"></i></span>
              <h2 class="card-title mb-0">Nhịp vận hành</h2>
            </div>
          </div>
          <div class="card-body">
            <p class="text-secondary small mb-3">Truy cập nhanh vào các công cụ quản lý lịch hẹn cá nhân, kho câu trả lời mẫu và đề xuất FAQ cho sinh viên.</p>
            <div class="qa-manager-side-list">
              <a class="qa-manager-side-link" href="${ctx}/manager/schedule">
                <span class="qa-kpi-icon"><i class="bi bi-calendar-week-fill"></i></span>
                <div><strong>Lịch làm việc</strong><div class="small text-secondary">Quản lý thời gian tư vấn của bạn</div></div>
              </a>
              <a class="qa-manager-side-link" href="${ctx}/manager/canned-responses">
                <span class="qa-kpi-icon" style="--mgr-kpi-accent: #8b5cf6;"><i class="bi bi-chat-left-text-fill"></i></span>
                <div><strong>Mẫu trả lời</strong><div class="small text-secondary">Hỗ trợ sinh viên nhanh và nhất quán</div></div>
              </a>
              <a class="qa-manager-side-link" href="${ctx}/manager/faq">
                <span class="qa-kpi-icon" style="--mgr-kpi-accent: #10b981;"><i class="bi bi-journal-text"></i></span>
                <div><strong>FAQ đề xuất</strong><div class="small text-secondary">Chia sẻ kiến thức hữu ích cho sinh viên</div></div>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <%-- Work List & Quick Access --%>
    <div class="qa-dashboard-work-grid">
      <div class="col-xl-8">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-lightning-charge-fill"></i></span>
              <h2 class="card-title mb-0">Cần làm ngay</h2>
            </div>
            <a class="qa-card-period" href="${ctx}/manager/queue?tab=slarisk">Xem hàng đợi <i class="bi bi-arrow-right ms-1"></i></a>
          </div>
          <div id="mgr-urgent-list" class="list-group list-group-flush"></div>
        </div>
      </div>
      <div class="col-xl-4">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-grid-fill"></i></span>
              <h2 class="card-title mb-0">Truy cập nhanh</h2>
            </div>
          </div>
          <div class="list-group list-group-flush">
            <a class="list-group-item list-group-item-action d-flex align-items-center gap-3" href="${ctx}/manager/schedule">
              <span class="qa-kpi-icon"><i class="bi bi-calendar-week-fill"></i></span>
              <div>
                <strong>Lịch làm việc</strong>
                <div class="small text-secondary mt-1">Quản lý thời gian tư vấn của bạn</div>
              </div>
            </a>
            <a class="list-group-item list-group-item-action d-flex align-items-center gap-3" href="${ctx}/manager/canned-responses">
              <span class="qa-kpi-icon" style="--mgr-kpi-accent: #8b5cf6;"><i class="bi bi-chat-left-text-fill"></i></span>
              <div>
                <strong>Mẫu trả lời</strong>
                <div class="small text-secondary mt-1">Hỗ trợ sinh viên nhanh và nhất quán</div>
              </div>
            </a>
            <a class="list-group-item list-group-item-action d-flex align-items-center gap-3" href="${ctx}/manager/faq">
              <span class="qa-kpi-icon" style="--mgr-kpi-accent: #10b981;"><i class="bi bi-journal-text"></i></span>
              <div>
                <strong>FAQ đề xuất</strong>
                <div class="small text-secondary mt-1">Chia sẻ kiến thức hữu ích cho sinh viên</div>
              </div>
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js" defer></script>
<script src="${ctx}/assets/js/manager/dashboard.js" defer></script>
</div>
