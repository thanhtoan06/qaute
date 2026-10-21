(() => {
  'use strict';
  const root = document.getElementById('manager-dashboard');
  if (!root) return;
  const el = id => document.getElementById(id);
  const number = value => Number(value || 0).toLocaleString('vi-VN', {maximumFractionDigits: 1});
  const set = (id, value) => { el(id).textContent = value; };

  function draw(id, type, labels, values, color) {
    if (!window.Chart) return;
    const horizontal = type === 'bar';
    const gradient = el(id).getContext('2d').createLinearGradient(0, 0, 0, 280);
    gradient.addColorStop(0, 'rgba(32, 107, 196, 0.16)');
    gradient.addColorStop(1, 'rgba(32, 107, 196, 0.01)');
    new Chart(el(id), {type, data: {labels, datasets: [{label: 'Ticket', data: values,
      borderColor: color, backgroundColor: horizontal ? color : gradient,
      fill: !horizontal, borderWidth: horizontal ? 0 : 2.5, borderRadius: 5,
      maxBarThickness: 24, pointRadius: 2, pointHoverRadius: 5, tension: 0.35}]},
      options: {responsive: true, maintainAspectRatio: false, indexAxis: horizontal ? 'y' : 'x',
        plugins: {legend: {display: false}, tooltip: {backgroundColor: '#0b1f3a', padding: 12, cornerRadius: 8}},
        scales: {
          x: {grid: {display: horizontal, color: '#eef2f7'}, border: {display: false}, ticks: {color: '#8a99ac', maxTicksLimit: 7, precision: 0, font: {size: 10}}},
          y: {beginAtZero: true, grid: {display: !horizontal, color: '#eef2f7'}, border: {display: false}, ticks: {color: '#8a99ac', precision: 0, font: {size: 10}}}
        }}});
  }

  function task(item) {
    const link = document.createElement('a');
    link.className = 'list-group-item list-group-item-action qa-dashboard-task';
    link.href = root.dataset.context + item.url;
    const badge = document.createElement('span');
    const appointment = item.kind === 'Lịch hẹn';
    badge.className = 'qa-kpi-icon ' + (appointment ? 'qa-kpi-icon--purple' : 'qa-kpi-icon--orange');
    const icon = document.createElement('i');
    icon.className = 'bi ' + (appointment ? 'bi-calendar-check' : 'bi-exclamation-triangle');
    icon.setAttribute('aria-hidden', 'true'); badge.append(icon);
    const body = document.createElement('div'); body.className = 'qa-dashboard-task-body';
    const title = document.createElement('strong'); title.textContent = item.title;
    const detail = document.createElement('div'); detail.className = 'text-secondary small mt-1';
    detail.textContent = item.kind + ' · ' + item.detail;
    body.append(title, detail);
    const arrow = document.createElement('i'); arrow.className = 'bi bi-chevron-right'; arrow.setAttribute('aria-hidden', 'true');
    link.append(badge, body, arrow);
    return link;
  }

  Qaute.fetchJson(root.dataset.api).then(response => {
    if (!response.success || !response.data) throw new Error(response.message || 'Không có dữ liệu.');
    const d = response.data;
    set('mgr-k-unassigned', number(d.unassigned)); set('mgr-k-mine', number(d.mine));
    set('mgr-k-waiting', number(d.waitingStudent)); set('mgr-k-sla', number(d.slaRisk));
    set('mgr-k-appointments', number(d.appointmentsToday)); set('mgr-k-chats', number(d.waitingChats));
    set('mgr-k-csat', d.csatSamples ? `${number(d.csat30)}/5` : '—');
    set('mgr-k-frt', d.frtSamples ? `${number(d.medianFrt30)} phút` : '—');
    const tasks = el('mgr-urgent-list');
    if (!d.urgent?.length) {
      const empty = document.createElement('div'); empty.className = 'list-group-item qa-dashboard-empty';
      const icon = document.createElement('i'); icon.className = 'bi bi-check2-circle'; icon.setAttribute('aria-hidden', 'true');
      empty.append(icon, document.createTextNode('Hiện không có công việc cần ưu tiên.')); tasks.append(empty);
    } else d.urgent.forEach(item => tasks.append(task(item)));
    el('mgr-dashboard-content').classList.remove('d-none');
    draw('mgr-chart-daily', 'line', d.daily14.map(item => new Date(item.day + 'T00:00:00').toLocaleDateString('vi-VN', {day: '2-digit', month: '2-digit'})),
      d.daily14.map(item => item.count), '#206bc4');
    draw('mgr-chart-category', 'bar', d.byCategory.map(item => item.name),
      d.byCategory.map(item => item.count), '#61b59a');
  }).catch(error => {
    const box = el('mgr-dashboard-error'); box.textContent = error.message || 'Không thể tải dashboard.';
    box.classList.remove('d-none');
  }).finally(() => el('mgr-dashboard-loading').classList.add('d-none'));
})();
