(() => {
  'use strict';
  const root = document.getElementById('manager-dashboard');
  if (!root) return;
  const el = id => document.getElementById(id);
  const number = value => Number(value || 0).toLocaleString('vi-VN', {maximumFractionDigits: 1});
  const set = (id, value) => { el(id).textContent = value; };

  function draw(id, type, labels, values, color) {
    if (!window.Chart) return;
    new Chart(el(id), {type, data: {labels, datasets: [{label: 'Ticket', data: values,
      borderColor: color, backgroundColor: color, tension: 0.25}]},
      options: {responsive: true, maintainAspectRatio: false,
        scales: {y: {beginAtZero: true, ticks: {precision: 0}}}}});
  }

  function task(item) {
    const link = document.createElement('a');
    link.className = 'list-group-item list-group-item-action';
    link.href = root.dataset.context + item.url;
    const badge = document.createElement('span');
    badge.className = 'badge bg-orange-lt me-2'; badge.textContent = item.kind;
    const title = document.createElement('strong'); title.textContent = item.title;
    const detail = document.createElement('div'); detail.className = 'text-secondary small mt-1';
    detail.textContent = item.detail;
    link.append(badge, title, detail);
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
      const empty = document.createElement('div'); empty.className = 'list-group-item text-secondary';
      empty.textContent = 'Chưa có việc cần ưu tiên.'; tasks.append(empty);
    } else d.urgent.forEach(item => tasks.append(task(item)));
    el('mgr-dashboard-content').classList.remove('d-none');
    draw('mgr-chart-daily', 'line', d.daily14.map(item => item.day),
      d.daily14.map(item => item.count), '#206bc4');
    draw('mgr-chart-category', 'bar', d.byCategory.map(item => item.name),
      d.byCategory.map(item => item.count), '#2fb344');
  }).catch(error => {
    const box = el('mgr-dashboard-error'); box.textContent = error.message || 'Không thể tải dashboard.';
    box.classList.remove('d-none');
  }).finally(() => el('mgr-dashboard-loading').classList.add('d-none'));
})();
