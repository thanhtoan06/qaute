<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title>Tổng quan hệ thống</title>
<div class="qa-page-admin-dashboard" data-qa-view="admin-dashboard">
<style>
/* ==========================================================================
   ADMIN DASHBOARD - MODERN DESIGN SYSTEM 2026
   ========================================================================== */
.qa-page-admin-dashboard {
  --adm-primary: #1e50c4;
  --adm-primary-rgb: 30, 80, 196;
  --adm-primary-dark: #0f2d7a;
  --adm-accent-purple: #7c3aed;
  --adm-accent-amber: #f59e0b;
  --adm-accent-rose: #ef4444;
  --adm-accent-emerald: #10b981;
  --adm-accent-cyan: #06b6d4;
  --adm-accent-indigo: #6366f1;
  --adm-accent-teal: #0d9488;
  --adm-card-bg: linear-gradient(180deg, rgba(255, 255, 255, 0.98) 0%, rgba(248, 251, 255, 0.95) 100%);
  --adm-border-soft: rgba(175, 202, 236, 0.45);
  --adm-border-hover: rgba(30, 80, 196, 0.35);
  --adm-shadow-subtle: 0 10px 30px rgba(15, 45, 84, 0.05);
  --adm-shadow-card: 0 18px 40px rgba(15, 45, 84, 0.08);
  --adm-shadow-hover: 0 26px 54px rgba(15, 45, 84, 0.14);
  --adm-text-main: #163252;
  --adm-text-muted: #5e7696;
  position: relative;
  isolation: isolate;
  min-height: 100%;
  padding: clamp(1.25rem, 2.8vw, 2.5rem) 0 4.5rem;
  color: var(--adm-text-main);
  background:
    radial-gradient(ellipse 60% 40% at 6% 0%, rgba(30, 80, 196, 0.14), transparent 45%),
    radial-gradient(ellipse 50% 35% at 95% 18%, rgba(124, 58, 237, 0.13), transparent 42%),
    radial-gradient(ellipse 40% 30% at 50% 85%, rgba(6, 182, 212, 0.08), transparent 50%),
    linear-gradient(180deg, #f4f8fe 0%, #edf3fb 45%, #f7fbff 100%);
}

.qa-page-admin-dashboard #admin-dashboard {
  max-width: 1600px;
  margin: 0 auto;
  padding: 0 clamp(1rem, 2.5vw, 2.25rem);
}

/* ---------- Typography & Utility Enhancements ---------- */
.qa-page-admin-dashboard .fw-800 { font-weight: 800; }
.qa-page-admin-dashboard .fw-850 { font-weight: 850; }

/* ---------- Unified Hero Banner ---------- */
.qa-page-admin-dashboard .qa-dashboard-hero {
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
    radial-gradient(circle at 92% 10%, rgba(124, 58, 237, 0.35), transparent 30%),
    radial-gradient(circle at 5% 95%, rgba(6, 182, 212, 0.28), transparent 36%),
    linear-gradient(135deg, #071733 0%, #133b7e 48%, #581c87 100%);
  box-shadow: 0 28px 65px rgba(12, 43, 85, 0.28), inset 0 1px 0 rgba(255, 255, 255, 0.2);
  animation: qaAdmRise 0.6s cubic-bezier(0.16, 1, 0.3, 1) both;
}

.qa-page-admin-dashboard .qa-dashboard-hero::before {
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

.qa-page-admin-dashboard .qa-dashboard-hero::after {
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

.qa-page-admin-dashboard .qa-dashboard-hero-content {
  position: relative;
  z-index: 1;
  max-width: 780px;
}

.qa-page-admin-dashboard .qa-dashboard-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 0.55rem;
  margin-bottom: 1.1rem;
  padding: 0.45rem 1rem;
  border: 1px solid rgba(255, 255, 255, 0.24);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  color: #e2e8ff;
  font-size: 0.74rem;
  font-weight: 850;
  letter-spacing: 0.16em;
  text-transform: uppercase;
  backdrop-filter: blur(12px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

.qa-page-admin-dashboard .qa-dashboard-eyebrow .qa-pulse-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: #a78bfa;
  box-shadow: 0 0 0 0 rgba(167, 139, 250, 0.7);
  animation: qaAdmPulse 2s infinite;
}

.qa-page-admin-dashboard .qa-view-title {
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

.qa-page-admin-dashboard .qa-view-title-icon {
  display: inline-grid;
  flex: 0 0 54px;
  width: 54px;
  height: 54px;
  place-items: center;
  border-radius: 18px;
  color: #12377a;
  background: linear-gradient(145deg, #ffffff, #ede9fe);
  box-shadow: 0 12px 24px rgba(3, 20, 48, 0.28);
  font-size: 1.55rem;
}

.qa-page-admin-dashboard .qa-dashboard-hero p {
  max-width: 670px;
  margin: 0 0 1.35rem;
  color: #dbe4ff;
  font-size: 1.02rem;
  line-height: 1.75;
}

.qa-page-admin-dashboard .qa-admin-hero-badges {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
  margin: 0 0 1.6rem;
}

.qa-page-admin-dashboard .qa-admin-hero-badge {
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

.qa-page-admin-dashboard .qa-admin-hero-badge:hover {
  transform: translateY(-2px);
  background: rgba(255, 255, 255, 0.16);
}

.qa-page-admin-dashboard .qa-admin-hero-badge .bi {
  display: inline-grid;
  width: 32px;
  height: 32px;
  place-items: center;
  border-radius: 11px;
  background: rgba(255, 255, 255, 0.18);
  font-size: 0.98rem;
  color: #c4b5fd;
}

.qa-page-admin-dashboard .qa-dashboard-hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 0.9rem;
}

/* ---------- Button Enhancements ---------- */
.qa-page-admin-dashboard .btn {
  border-radius: 16px;
  font-weight: 750;
  letter-spacing: -0.01em;
  padding: 0.75rem 1.35rem;
  transition: all 0.24s cubic-bezier(0.16, 1, 0.3, 1);
}

.qa-page-admin-dashboard .btn-sm {
  padding: 0.45rem 0.95rem;
  border-radius: 12px;
  font-size: 0.82rem;
}

.qa-page-admin-dashboard .btn:not(:disabled):not(.btn-close):hover {
  transform: translateY(-2px);
}

.qa-page-admin-dashboard .btn-white {
  color: #0b3470;
  border: 1px solid rgba(255, 255, 255, 0.9);
  background: linear-gradient(180deg, #ffffff 0%, #edf2ff 100%);
  box-shadow: 0 10px 24px rgba(4, 21, 48, 0.22);
}

.qa-page-admin-dashboard .btn-white:hover {
  color: #072654;
  background: #ffffff;
  box-shadow: 0 14px 28px rgba(4, 21, 48, 0.28);
}

.qa-page-admin-dashboard .btn-outline-light {
  border-color: rgba(255, 255, 255, 0.38);
  background: rgba(255, 255, 255, 0.12);
  color: #ffffff;
  backdrop-filter: blur(12px);
}

.qa-page-admin-dashboard .btn-outline-light:hover {
  color: #ffffff;
  border-color: rgba(255, 255, 255, 0.65);
  background: rgba(255, 255, 255, 0.22);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

/* ---------- Hero Aside & Visual ---------- */
.qa-page-admin-dashboard .qa-dashboard-hero-aside {
  position: relative;
  z-index: 1;
  display: grid;
  align-content: space-between;
  gap: 1.1rem;
}

.qa-page-admin-dashboard .qa-dashboard-date {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  width: fit-content;
  margin-left: 0;
  padding: 0.6rem 1.1rem;
  border: 1px solid rgba(255, 255, 255, 0.25);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  color: #eef2ff;
  font-size: 0.82rem;
  font-weight: 750;
  backdrop-filter: blur(14px);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.qa-page-admin-dashboard .qa-dashboard-hero-meta {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 0.75rem;
}

.qa-page-admin-dashboard .qa-dashboard-hero-mark {
  display: grid;
  flex: 0 0 50px;
  width: 50px;
  height: 50px;
  place-items: center;
  border: 1px solid rgba(255, 255, 255, 0.22);
  border-radius: 17px;
  background: rgba(255, 255, 255, 0.14);
  color: rgba(232, 230, 255, 0.94);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.18), 0 10px 24px rgba(3, 16, 38, 0.16);
  backdrop-filter: blur(14px);
  font-size: 1.55rem;
  animation: qaAdmFloat 4.6s ease-in-out infinite;
}

.qa-page-admin-dashboard .qa-admin-hero-visual {
  position: relative;
  min-height: 270px;
  padding: 1.4rem;
  border: 1px solid rgba(255, 255, 255, 0.18);
  border-radius: 28px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.14) 0%, rgba(255, 255, 255, 0.05) 100%);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.18), 0 20px 40px rgba(4, 18, 42, 0.2);
  backdrop-filter: blur(18px);
}

.qa-page-admin-dashboard .qa-admin-hero-stack {
  position: absolute;
  left: 1.25rem;
  right: 1.25rem;
  bottom: 1.25rem;
  display: grid;
  gap: 0.75rem;
}

.qa-page-admin-dashboard .qa-admin-hero-note {
  padding: 0.95rem 1.05rem;
  border: 1px solid rgba(255, 255, 255, 0.16);
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.12);
  color: #f0f7ff;
  backdrop-filter: blur(12px);
}

.qa-page-admin-dashboard .qa-admin-hero-note strong {
  display: flex;
  align-items: center;
  gap: 0.55rem;
  margin-bottom: 0.3rem;
  font-weight: 850;
  font-size: 0.88rem;
  letter-spacing: -0.01em;
}

.qa-page-admin-dashboard .qa-admin-hero-note strong .bi {
  display: inline-grid;
  width: 28px;
  height: 28px;
  place-items: center;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.18);
  font-size: 0.88rem;
  color: #c4b5fd;
}

.qa-page-admin-dashboard .qa-admin-hero-note p {
  margin: 0;
  color: #dbe4ff;
  font-size: 0.82rem;
  line-height: 1.65;
}

/* ---------- 3 Highlights Cards (Synchronized with Student & Manager) ---------- */
.qa-page-admin-dashboard .qa-admin-overview-strip {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 1.15rem;
  margin-bottom: 1.65rem;
}

.qa-page-admin-dashboard .qa-admin-overview-card {
  position: relative;
  overflow: hidden;
  min-height: 156px;
  padding: 1.35rem 1.4rem;
  border: 1px solid var(--adm-border-soft);
  border-radius: 26px;
  background: linear-gradient(160deg, rgba(255, 255, 255, 0.98) 0%, rgba(246, 250, 255, 0.94) 100%);
  box-shadow: var(--adm-shadow-subtle);
  transition: transform 0.24s ease, box-shadow 0.24s ease, border-color 0.24s ease;
}

.qa-page-admin-dashboard .qa-admin-overview-card:hover {
  transform: translateY(-4px);
  border-color: var(--adm-border-hover);
  box-shadow: var(--adm-shadow-card);
}

.qa-page-admin-dashboard .qa-admin-overview-card::after {
  content: "";
  position: absolute;
  right: -32px;
  bottom: -44px;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 1px solid rgba(30, 80, 196, 0.08);
  box-shadow: 0 0 0 20px rgba(30, 80, 196, 0.03);
  pointer-events: none;
}

.qa-page-admin-dashboard .qa-admin-overview-card h3 {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin: 0 0 0.55rem;
  color: #123158;
  font-size: 1.05rem;
  font-weight: 850;
  letter-spacing: -0.02em;
}

.qa-page-admin-dashboard .qa-admin-overview-card h3 .bi {
  display: inline-grid;
  width: 42px;
  height: 42px;
  place-items: center;
  border-radius: 15px;
  color: #ffffff;
  background: linear-gradient(135deg, #1e50c4 0%, #10348a 100%);
  box-shadow: 0 10px 20px rgba(30, 80, 196, 0.25);
  font-size: 1.15rem;
}

.qa-page-admin-dashboard .qa-admin-overview-card:nth-child(2) h3 .bi {
  background: linear-gradient(135deg, #7c3aed 0%, #5b21b6 100%);
  box-shadow: 0 10px 20px rgba(124, 58, 237, 0.25);
}

.qa-page-admin-dashboard .qa-admin-overview-card:nth-child(3) h3 .bi {
  background: linear-gradient(135deg, #0d9488 0%, #0f766e 100%);
  box-shadow: 0 10px 20px rgba(13, 148, 136, 0.25);
}

.qa-page-admin-dashboard .qa-admin-overview-card p {
  margin: 0 0 1rem;
  color: var(--adm-text-muted);
  font-size: 0.88rem;
  line-height: 1.7;
}

.qa-page-admin-dashboard .qa-admin-overview-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 0.6rem;
}

.qa-page-admin-dashboard .qa-admin-overview-meta span {
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
.qa-page-admin-dashboard .qa-dashboard-section-label {
  display: flex;
  align-items: end;
  justify-content: space-between;
  gap: 1rem;
  margin: 0 0 1.15rem;
  padding: 0 0.25rem;
}

.qa-page-admin-dashboard .qa-dashboard-section-label h2 {
  display: flex;
  align-items: center;
  gap: 0.85rem;
  margin: 0;
  color: #123158;
  font-size: 1.25rem;
  font-weight: 850;
  letter-spacing: -0.03em;
}

.qa-page-admin-dashboard .qa-dashboard-section-label h2 .bi {
  display: inline-grid;
  width: 42px;
  height: 42px;
  place-items: center;
  border-radius: 14px;
  color: #ffffff;
  background: linear-gradient(135deg, #1e50c4 0%, #0f358c 100%);
  box-shadow: 0 12px 24px rgba(30, 80, 196, 0.25);
  font-size: 1.15rem;
}

.qa-page-admin-dashboard .qa-dashboard-section-label > span {
  color: var(--adm-text-muted);
  font-size: 0.85rem;
  font-weight: 600;
}

/* ---------- KPI Grid (Synchronized Metric Cards) ---------- */
.qa-page-admin-dashboard .qa-kpi-grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 1.1rem;
  margin-bottom: 2rem;
}

.qa-page-admin-dashboard .qa-kpi {
  position: relative;
  isolation: isolate;
  overflow: hidden;
  min-height: 175px;
  display: flex;
  flex-direction: column;
  padding: 1.25rem 1.25rem 1.1rem;
  border: 1px solid var(--adm-border-soft);
  border-radius: 24px;
  background:
    radial-gradient(circle at 100% 0%, var(--adm-kpi-tint, rgba(30, 80, 196, 0.08)) 0%, transparent 55%),
    linear-gradient(180deg, rgba(255, 255, 255, 0.99) 0%, rgba(247, 251, 255, 0.96) 100%);
  box-shadow: var(--adm-shadow-subtle);
  text-decoration: none;
  color: inherit;
  transition: transform 0.26s cubic-bezier(0.16, 1, 0.3, 1), box-shadow 0.26s cubic-bezier(0.16, 1, 0.3, 1), border-color 0.26s ease;
}

.qa-page-admin-dashboard .qa-kpi::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--adm-kpi-accent), color-mix(in srgb, var(--adm-kpi-accent) 40%, transparent));
}

.qa-page-admin-dashboard .qa-kpi::after {
  content: "";
  position: absolute;
  right: -24px;
  bottom: -32px;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 1px solid color-mix(in srgb, var(--adm-kpi-accent) 15%, transparent);
  box-shadow: 0 0 0 18px color-mix(in srgb, var(--adm-kpi-accent) 4%, transparent);
  pointer-events: none;
  transition: transform 0.3s ease;
}

.qa-page-admin-dashboard .qa-kpi:hover {
  transform: translateY(-5px);
  border-color: color-mix(in srgb, var(--adm-kpi-accent) 35%, var(--adm-border-soft));
  box-shadow: 0 24px 50px rgba(15, 45, 84, 0.13);
  color: inherit;
}

.qa-page-admin-dashboard .qa-kpi:hover::after {
  transform: scale(1.1);
}

.qa-page-admin-dashboard .qa-kpi:nth-child(1) { --adm-kpi-accent: #2563eb; --adm-kpi-tint: rgba(37, 99, 235, 0.12); }
.qa-page-admin-dashboard .qa-kpi:nth-child(2) { --adm-kpi-accent: #7c3aed; --adm-kpi-tint: rgba(124, 58, 237, 0.12); }
.qa-page-admin-dashboard .qa-kpi:nth-child(3) { --adm-kpi-accent: #ea580c; --adm-kpi-tint: rgba(234, 88, 12, 0.12); }
.qa-page-admin-dashboard .qa-kpi:nth-child(4) { --adm-kpi-accent: #ef4444; --adm-kpi-tint: rgba(239, 68, 68, 0.12); }
.qa-page-admin-dashboard .qa-kpi:nth-child(5) { --adm-kpi-accent: #10b981; --adm-kpi-tint: rgba(16, 185, 129, 0.12); }
.qa-page-admin-dashboard .qa-kpi:nth-child(6) { --adm-kpi-accent: #f59e0b; --adm-kpi-tint: rgba(245, 158, 11, 0.12); }
.qa-page-admin-dashboard .qa-kpi:nth-child(7) { --adm-kpi-accent: #06b6d4; --adm-kpi-tint: rgba(6, 182, 212, 0.12); }
.qa-page-admin-dashboard .qa-kpi:nth-child(8) { --adm-kpi-accent: #6366f1; --adm-kpi-tint: rgba(99, 102, 241, 0.12); }
.qa-page-admin-dashboard .qa-kpi:nth-child(9) { --adm-kpi-accent: #0d9488; --adm-kpi-tint: rgba(13, 148, 136, 0.12); }
.qa-page-admin-dashboard .qa-kpi:nth-child(10) { --adm-kpi-accent: #dc2626; --adm-kpi-tint: rgba(220, 38, 38, 0.12); }

.qa-page-admin-dashboard .qa-kpi-heading {
  display: flex;
  align-items: start;
  justify-content: space-between;
  gap: 0.75rem;
  min-height: 48px;
}

.qa-page-admin-dashboard .qa-kpi-label {
  max-width: 76%;
  color: #55708f;
  font-size: 0.73rem;
  font-weight: 850;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  line-height: 1.5;
}

.qa-page-admin-dashboard .qa-kpi-icon {
  display: inline-grid;
  flex: 0 0 44px;
  width: 44px;
  height: 44px;
  place-items: center;
  border: 1px solid color-mix(in srgb, var(--adm-kpi-accent) 22%, transparent);
  border-radius: 15px;
  color: var(--adm-kpi-accent);
  background: color-mix(in srgb, var(--adm-kpi-accent) 10%, #ffffff);
  font-size: 1.3rem;
  box-shadow: 0 6px 16px color-mix(in srgb, var(--adm-kpi-accent) 12%, transparent);
}

.qa-page-admin-dashboard .qa-kpi-icon--purple { --adm-kpi-accent: #7c3aed; }
.qa-page-admin-dashboard .qa-kpi-icon--orange { --adm-kpi-accent: #ea580c; }
.qa-page-admin-dashboard .qa-kpi-icon--red { --adm-kpi-accent: #ef4444; }
.qa-page-admin-dashboard .qa-kpi-icon--green { --adm-kpi-accent: #10b981; }

.qa-page-admin-dashboard .qa-kpi-value {
  position: relative;
  z-index: 1;
  margin: 0.75rem 0 0.3rem;
  color: #123158;
  font-size: clamp(1.9rem, 2.4vw, 2.6rem);
  font-weight: 850;
  letter-spacing: -0.06em;
  line-height: 1;
}

.qa-page-admin-dashboard .qa-kpi-foot {
  position: relative;
  z-index: 1;
  color: var(--adm-text-muted);
  font-size: 0.8rem;
  line-height: 1.55;
}

/* ---------- Card & Header Standard ---------- */
.qa-page-admin-dashboard .card {
  position: relative;
  overflow: hidden;
  border: 1px solid var(--adm-border-soft);
  border-radius: 28px;
  background: var(--adm-card-bg);
  box-shadow: var(--adm-shadow-subtle);
  transition: transform 0.24s ease, box-shadow 0.24s ease, border-color 0.24s ease;
}

.qa-page-admin-dashboard .card:hover {
  border-color: var(--adm-border-hover);
  box-shadow: var(--adm-shadow-card);
}

.qa-page-admin-dashboard .card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.85rem;
  min-height: 74px;
  padding: 1.2rem 1.4rem;
  border-bottom: 1px solid rgba(215, 230, 248, 0.7);
  border-radius: 28px 28px 0 0;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98) 0%, rgba(240, 246, 255, 0.9) 100%);
}

.qa-page-admin-dashboard .card-title {
  margin: 0;
  color: #123158;
  font-size: 1.05rem;
  font-weight: 850;
  letter-spacing: -0.02em;
}

.qa-page-admin-dashboard .qa-card-heading-icon {
  display: inline-grid;
  flex: 0 0 42px;
  width: 42px;
  height: 42px;
  place-items: center;
  border-radius: 15px;
  color: #ffffff;
  background: linear-gradient(135deg, #1e50c4 0%, #103890 100%);
  box-shadow: 0 10px 20px rgba(30, 80, 196, 0.22);
  font-size: 1.1rem;
}

.qa-page-admin-dashboard .qa-card-period {
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

.qa-page-admin-dashboard .qa-card-period:hover {
  color: #1e50c4;
  border-color: rgba(30, 80, 196, 0.35);
  background: #ffffff;
}

.qa-page-admin-dashboard .qa-dashboard-chart {
  min-height: 310px;
  padding: 1.35rem;
}

.qa-page-admin-dashboard .qa-dashboard-chart canvas {
  max-width: 100%;
}

/* ---------- Alert & Monitoring Grid ---------- */
.qa-page-admin-dashboard .qa-dashboard-alert-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 1.35rem;
  margin: 0 0 1.5rem;
}

.qa-page-admin-dashboard .qa-dashboard-alert-grid > [class*="col-"] {
  width: auto;
  max-width: none;
  padding: 0;
}

.qa-page-admin-dashboard .qa-dashboard-alert-grid > .col-12 {
  grid-column: 1 / -1;
}

.qa-page-admin-dashboard .list-group-item {
  margin: 0.55rem 0.85rem;
  padding: 1.05rem 1.25rem;
  border: 1px solid rgba(224, 235, 248, 0.95);
  border-radius: 20px !important;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.99) 0%, rgba(248, 252, 255, 0.96) 100%);
  transition: transform 0.22s ease, border-color 0.22s ease, box-shadow 0.22s ease;
}

.qa-page-admin-dashboard .list-group-item:hover {
  transform: translateY(-3px);
  border-color: rgba(30, 80, 196, 0.28);
  box-shadow: 0 16px 30px rgba(15, 45, 84, 0.08);
}

.qa-page-admin-dashboard .list-group-item strong {
  display: block;
  margin-bottom: 0.2rem;
  color: #123158;
  font-size: 0.96rem;
  font-weight: 750;
}

.qa-page-admin-dashboard .qa-dashboard-empty {
  display: flex;
  align-items: center;
  gap: 0.85rem;
  min-height: 86px;
  color: #0f766e;
  background: linear-gradient(110deg, #ecfdf5 0%, #ffffff 100%);
  border-radius: 20px;
  font-weight: 650;
}

.qa-page-admin-dashboard .qa-dashboard-empty .bi {
  color: #10b981;
  font-size: 1.35rem;
}

.qa-page-admin-dashboard .qa-dashboard-loading {
  margin-bottom: 1.5rem;
  padding: 1.25rem;
  border: 1px solid rgba(185, 210, 240, 0.45);
  border-radius: 26px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98) 0%, rgba(242, 248, 255, 0.96) 100%);
  box-shadow: 0 16px 32px rgba(15, 45, 84, 0.07);
}

.qa-page-admin-dashboard .qa-dashboard-loading .qa-skeleton {
  min-height: 150px;
  border-radius: 20px;
}

.qa-page-admin-dashboard #adm-alert-email {
  padding: 1.25rem 1.45rem;
  color: #4b627e;
  line-height: 1.7;
  font-weight: 550;
}

/* ---------- Keyframes ---------- */
@keyframes qaAdmPulse {
  0% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(167, 139, 250, 0.7); }
  70% { transform: scale(1); box-shadow: 0 0 0 7px rgba(167, 139, 250, 0); }
  100% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(167, 139, 250, 0); }
}

@keyframes qaAdmRise {
  from { opacity: 0; transform: translateY(14px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes qaAdmFloat {
  0%, 100% { transform: translateY(0) rotate(-3deg); }
  50% { transform: translateY(-10px) rotate(4deg); }
}

@media (max-width: 1199.98px) {
  .qa-page-admin-dashboard .qa-dashboard-hero { grid-template-columns: minmax(0, 1fr); }
  .qa-page-admin-dashboard .qa-admin-overview-strip { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .qa-page-admin-dashboard .qa-kpi-grid { grid-template-columns: repeat(3, minmax(0, 1fr)); }
  .qa-page-admin-dashboard .qa-dashboard-alert-grid { grid-template-columns: minmax(0, 1fr); }
}

@media (max-width: 767.98px) {
  .qa-page-admin-dashboard #admin-dashboard { padding: 0 0.85rem; }
  .qa-page-admin-dashboard .qa-dashboard-hero { padding: 1.45rem; border-radius: 24px; }
  .qa-page-admin-dashboard .qa-view-title { font-size: clamp(1.5rem, 6.2vw, 2.2rem); }
  .qa-page-admin-dashboard .qa-view-title-icon { flex-basis: 44px; width: 44px; height: 44px; }
  .qa-page-admin-dashboard .qa-admin-hero-visual { min-height: 210px; }
  .qa-page-admin-dashboard .qa-admin-overview-strip { grid-template-columns: minmax(0, 1fr); }
  .qa-page-admin-dashboard .qa-dashboard-section-label { flex-direction: column; align-items: flex-start; }
  .qa-page-admin-dashboard .qa-kpi-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .qa-page-admin-dashboard .qa-dashboard-alert-grid > .col-12 { grid-column: auto; }
}

@media (prefers-reduced-motion: reduce) {
  .qa-page-admin-dashboard *,
  .qa-page-admin-dashboard *::before,
  .qa-page-admin-dashboard *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
    scroll-behavior: auto !important;
  }
}
</style>

<div id="admin-dashboard" data-api="${ctx}/api/admin/dashboard" data-context="${ctx}">
  <%-- ==================== HERO BANNER ==================== --%>
  <section class="qa-dashboard-hero" aria-labelledby="admin-welcome">
    <div class="qa-dashboard-hero-content">
      <span class="qa-dashboard-eyebrow">
        <span class="qa-pulse-dot" aria-hidden="true"></span>
        <i class="bi bi-shield-lock-fill me-1" aria-hidden="true"></i>Không gian quản trị
      </span>
      <h1 id="admin-welcome" class="qa-view-title" data-qa-page="admin-dashboard">
        <span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-speedometer2"></i></span>
        Tổng quan hệ thống QAUTE
      </h1>
      <p>Giám sát toàn diện hoạt động tiếp nhận và hỗ trợ sinh viên, cam kết thời hạn SLA, phân bố tải theo phòng ban và phát hiện sớm các rủi ro vận hành trên toàn trường.</p>
      <div class="qa-admin-hero-badges">
        <span class="qa-admin-hero-badge"><i class="bi bi-activity"></i>Giám sát liên tục 24/7</span>
        <span class="qa-admin-hero-badge"><i class="bi bi-bar-chart-line-fill"></i>Phân tích xu hướng thông minh</span>
        <span class="qa-admin-hero-badge"><i class="bi bi-shield-check"></i>Đảm bảo an toàn & tuân thủ</span>
      </div>
      <div class="qa-dashboard-hero-actions">
        <a class="btn btn-white shadow-sm" href="${ctx}/admin/reports"><i class="bi bi-bar-chart-line-fill me-2"></i>Báo cáo chi tiết</a>
        <a class="btn btn-outline-light" href="${ctx}/admin/users/new"><i class="bi bi-person-plus-fill me-2"></i>Thêm người dùng</a>
      </div>
    </div>
    <div class="qa-dashboard-hero-aside" aria-hidden="true">
      <div class="qa-dashboard-hero-meta">
        <span class="qa-dashboard-date" data-workspace-date><i class="bi bi-calendar3 me-1"></i>Hôm nay</span>
        <span class="qa-dashboard-hero-mark"><i class="bi bi-shield-fill-check"></i></span>
      </div>
      <div class="qa-admin-hero-visual">
        <div class="qa-admin-hero-stack">
          <div class="qa-admin-hero-note">
            <strong><i class="bi bi-cpu-fill"></i>Hệ thống</strong>
            <p>10 chỉ số trọng tâm giúp nhận diện tức thì khối lượng tiếp nhận và sức tải dịch vụ.</p>
          </div>
          <div class="qa-admin-hero-note">
            <strong><i class="bi bi-shield-exclamation"></i>Cảnh báo sớm</strong>
            <p>Tự động tổng hợp SLA trễ hạn, FAQ kém hiệu quả và hàng đợi email cần xử lý.</p>
          </div>
        </div>
      </div>
    </div>
  </section>

  <%-- ==================== 3 HIGHLIGHTS CARDS ==================== --%>
  <section class="qa-admin-overview-strip" aria-label="Điểm nhấn quản trị">
    <div class="qa-admin-overview-card">
      <h3><i class="bi bi-diagram-3-fill"></i>Quản trị tập trung</h3>
      <p>Bao quát toàn bộ luồng yêu cầu, người dùng hoạt động, các kênh chat và lịch hẹn tư vấn trong một điểm nhìn duy nhất.</p>
      <div class="qa-admin-overview-meta">
        <span><i class="bi bi-globe2"></i>Toàn hệ thống</span>
        <span><i class="bi bi-people-fill"></i>Người dùng online</span>
      </div>
    </div>
    <div class="qa-admin-overview-card">
      <h3><i class="bi bi-shield-fill-exclamation"></i>Kiểm soát SLA & Cảnh báo</h3>
      <p>Phát hiện tức thì các ticket vi phạm thời hạn cam kết, từ khóa chưa có đáp án và chuyên mục chưa được phân công quản lý.</p>
      <div class="qa-admin-overview-meta">
        <span><i class="bi bi-alarm-fill"></i>SLA rủi ro</span>
        <span><i class="bi bi-bell-fill"></i>Cảnh báo tức thời</span>
      </div>
    </div>
    <div class="qa-admin-overview-card">
      <h3><i class="bi bi-graph-up-arrow"></i>Báo cáo & Tối ưu hóa</h3>
      <p>Trực quan hóa khối lượng yêu cầu 30 ngày, phân bổ theo phòng ban và thời gian phản hồi đầu tiên để tối ưu phân công nhân sự.</p>
      <div class="qa-admin-overview-meta">
        <span><i class="bi bi-pie-chart-fill"></i>Phòng ban</span>
        <span><i class="bi bi-stopwatch-fill"></i>FRT tuần</span>
      </div>
    </div>
  </section>

  <%-- Trạng thái tải hoặc lỗi trước khi dữ liệu tổng quan sẵn sàng --%>
  <div id="adm-dashboard-loading" class="qa-dashboard-loading" role="status">
    <span class="visually-hidden">Đang tải dữ liệu tổng quan…</span>
    <div class="row g-3">
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
      <div class="col-6 col-lg-3"><div class="qa-skeleton"></div></div>
    </div>
  </div>
  <div id="adm-dashboard-error" class="alert alert-danger d-none" role="alert"></div>

  <%-- ==================== MAIN DASHBOARD CONTENT ==================== --%>
  <div id="adm-dashboard-content" class="d-none">
    <div class="qa-dashboard-section-label">
      <h2><i class="bi bi-activity" aria-hidden="true"></i>Hoạt động & chất lượng dịch vụ</h2>
      <span>Dữ liệu toàn hệ thống</span>
    </div>

    <%-- 10 KPI Metric Cards --%>
    <div class="qa-kpi-grid qa-kpi-grid--admin">
      <a class="qa-kpi" href="${ctx}/admin/tickets">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Yêu cầu hôm nay</span>
          <span class="qa-kpi-icon"><i class="bi bi-ticket-detailed-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="adm-d-today" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Tiếp nhận trong ngày</span>
      </a>

      <a class="qa-kpi" href="${ctx}/admin/reports">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Yêu cầu tháng này</span>
          <span class="qa-kpi-icon qa-kpi-icon--purple"><i class="bi bi-calendar3" aria-hidden="true"></i></span>
        </div>
        <strong id="adm-d-month" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Khối lượng tư vấn trong tháng</span>
      </a>

      <a class="qa-kpi" href="${ctx}/admin/tickets?status=NEW">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Yêu cầu chưa nhận</span>
          <span class="qa-kpi-icon qa-kpi-icon--orange"><i class="bi bi-inbox-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="adm-d-unassigned" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Đang chờ được phân công</span>
      </a>

      <a class="qa-kpi" href="${ctx}/admin/tickets">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Tồn đọng trên 24 giờ</span>
          <span class="qa-kpi-icon qa-kpi-icon--red"><i class="bi bi-hourglass-split" aria-hidden="true"></i></span>
        </div>
        <strong id="adm-d-backlog" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Các yêu cầu cần được chú ý</span>
      </a>

      <a class="qa-kpi" href="${ctx}/admin/reports">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">SLA đạt trong 30 ngày</span>
          <span class="qa-kpi-icon qa-kpi-icon--green"><i class="bi bi-shield-check" aria-hidden="true"></i></span>
        </div>
        <strong id="adm-d-sla" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Chất lượng theo thời hạn xử lý</span>
      </a>

      <a class="qa-kpi" href="${ctx}/admin/reports">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Mức độ hài lòng</span>
          <span class="qa-kpi-icon qa-kpi-icon--orange"><i class="bi bi-star-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="adm-d-csat" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Điểm đánh giá trong 30 ngày</span>
      </a>

      <a class="qa-kpi" href="${ctx}/admin/chat-history">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Chat hoạt động / chờ</span>
          <span class="qa-kpi-icon"><i class="bi bi-chat-dots-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="adm-d-chat" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Tình hình tư vấn trực tuyến</span>
      </a>

      <a class="qa-kpi" href="${ctx}/admin/reports">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Lịch hẹn hôm nay</span>
          <span class="qa-kpi-icon qa-kpi-icon--purple"><i class="bi bi-calendar-check-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="adm-d-appt" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Các buổi tư vấn trong ngày</span>
      </a>

      <a class="qa-kpi" href="${ctx}/admin/users">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Người dùng hoạt động</span>
          <span class="qa-kpi-icon qa-kpi-icon--green"><i class="bi bi-people-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="adm-d-users" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Đăng nhập trong 7 ngày qua</span>
      </a>

      <a class="qa-kpi" href="${ctx}/admin/health">
        <div class="qa-kpi-heading">
          <span class="qa-kpi-label">Email gửi thất bại</span>
          <span class="qa-kpi-icon qa-kpi-icon--red"><i class="bi bi-envelope-exclamation-fill" aria-hidden="true"></i></span>
        </div>
        <strong id="adm-d-email" class="qa-kpi-value">—</strong>
        <span class="qa-kpi-foot">Kiểm tra hàng đợi gửi thư</span>
      </a>
    </div>

    <%-- Biểu đồ xu hướng và phân bố --%>
    <div class="row g-3 mb-4">
      <div class="col-lg-7">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-graph-up" aria-hidden="true"></i></span>
              <h2 class="card-title mb-0">Yêu cầu theo ngày</h2>
            </div>
            <span class="qa-card-period">30 ngày gần nhất</span>
          </div>
          <div class="card-body qa-dashboard-chart"><canvas id="adm-d-chart-daily" role="img" aria-label="Yêu cầu theo ngày"></canvas></div>
        </div>
      </div>
      <div class="col-lg-5">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-buildings-fill" aria-hidden="true"></i></span>
              <h2 class="card-title mb-0">Phân bố theo phòng ban</h2>
            </div>
            <span class="qa-card-period">Trong kỳ báo cáo</span>
          </div>
          <div class="card-body qa-dashboard-chart"><canvas id="adm-d-chart-departments" role="img" aria-label="Phân bố theo phòng ban"></canvas></div>
        </div>
      </div>
    </div>

    <%-- Biểu đồ thời gian phản hồi đầu --%>
    <div class="mb-4">
      <div class="card h-100">
        <div class="card-header">
          <div class="d-flex align-items-center gap-2">
            <span class="qa-card-heading-icon"><i class="bi bi-stopwatch-fill" aria-hidden="true"></i></span>
            <h2 class="card-title mb-0">Thời gian phản hồi đầu theo tuần</h2>
          </div>
          <span class="qa-card-period">Trung bình · phút làm việc</span>
        </div>
        <div class="card-body qa-dashboard-chart"><canvas id="adm-d-chart-frt" role="img" aria-label="Thời gian phản hồi đầu theo tuần"></canvas></div>
      </div>
    </div>

    <%-- Cảnh báo và hoạt động gần đây --%>
    <div class="qa-dashboard-section-label">
      <h2><i class="bi bi-shield-exclamation" aria-hidden="true"></i>Cảnh báo & hoạt động gần đây</h2>
      <span>Giúp bạn chủ động điều phối hệ thống</span>
    </div>

    <div class="row g-3 qa-dashboard-alert-grid">
      <div class="col-xl-6">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-exclamation-triangle-fill"></i></span>
              <h2 class="card-title mb-0">Yêu cầu vi phạm SLA</h2>
            </div>
            <a class="qa-card-period" href="${ctx}/admin/tickets?slaStatus=BREACHED" aria-label="Xem Yêu cầu vi phạm SLA">Xem danh sách <i class="bi bi-arrow-up-right ms-1"></i></a>
          </div>
          <div id="adm-alert-sla" class="list-group list-group-flush"></div>
        </div>
      </div>

      <div class="col-xl-6">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-diagram-3-fill"></i></span>
              <h2 class="card-title mb-0">Chuyên mục chưa có tư vấn viên</h2>
            </div>
            <a class="qa-card-period" href="${ctx}/admin/managers" aria-label="Xem Chuyên mục chưa có tư vấn viên">Phân công <i class="bi bi-arrow-up-right ms-1"></i></a>
          </div>
          <div id="adm-alert-categories" class="list-group list-group-flush"></div>
        </div>
      </div>

      <div class="col-xl-6">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-journal-x"></i></span>
              <h2 class="card-title mb-0">FAQ cần cải thiện</h2>
            </div>
            <a class="qa-card-period" href="${ctx}/admin/faq" aria-label="Xem FAQ cần cải thiện">Quản lý FAQ <i class="bi bi-arrow-up-right ms-1"></i></a>
          </div>
          <div id="adm-alert-faq" class="list-group list-group-flush"></div>
        </div>
      </div>

      <div class="col-xl-6">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-search"></i></span>
              <h2 class="card-title mb-0">Tìm kiếm chưa có kết quả</h2>
            </div>
            <a class="qa-card-period" href="${ctx}/admin/faq/synonyms" aria-label="Xem Tìm kiếm chưa có kết quả">Từ đồng nghĩa <i class="bi bi-arrow-up-right ms-1"></i></a>
          </div>
          <div id="adm-alert-search" class="list-group list-group-flush"></div>
        </div>
      </div>

      <div class="col-xl-6">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-star-half"></i></span>
              <h2 class="card-title mb-0">Đánh giá thấp gần đây</h2>
            </div>
            <a class="qa-card-period" href="${ctx}/admin/tickets?maxRating=2" aria-label="Xem Đánh giá thấp gần đây">Kiểm tra <i class="bi bi-arrow-up-right ms-1"></i></a>
          </div>
          <div id="adm-alert-ratings" class="list-group list-group-flush"></div>
        </div>
      </div>

      <div class="col-xl-6">
        <div class="card h-100">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-clock-history"></i></span>
              <h2 class="card-title mb-0">Hoạt động gần đây</h2>
            </div>
            <a class="qa-card-period" href="${ctx}/admin/audit-logs" aria-label="Xem Hoạt động gần đây">Nhật ký <i class="bi bi-arrow-up-right ms-1"></i></a>
          </div>
          <div id="adm-alert-audit" class="list-group list-group-flush"></div>
        </div>
      </div>

      <div class="col-12">
        <div class="card">
          <div class="card-header">
            <div class="d-flex align-items-center gap-2">
              <span class="qa-card-heading-icon"><i class="bi bi-envelope-exclamation-fill"></i></span>
              <h2 class="card-title mb-0">Tình trạng gửi email</h2>
            </div>
            <a class="qa-card-period" href="${ctx}/admin/health">Kiểm tra hệ thống <i class="bi bi-arrow-right ms-1"></i></a>
          </div>
          <div class="card-body text-secondary" id="adm-alert-email"></div>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js" defer></script>
<script src="${ctx}/assets/js/admin/dashboard.js" defer></script>
</div>
