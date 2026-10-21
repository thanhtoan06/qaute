(() => {
  'use strict';
  const root = document.getElementById('admin-dashboard');
  if (!root) return;
  const el = id => document.getElementById(id);
  const number = value => Number(value || 0).toLocaleString('vi-VN', {maximumFractionDigits: 1});
  const set = (id, value) => { el(id).textContent = value; };

  function list(id, items, map) {
    const container = el(id);
    if (!items?.length) {
      const empty = document.createElement('div'); empty.className = 'list-group-item qa-dashboard-empty';
      const icon = document.createElement('i'); icon.className = 'bi bi-check2-circle'; icon.setAttribute('aria-hidden', 'true');
      empty.append(icon, document.createTextNode('Chưa có mục cần chú ý.')); container.append(empty); return;
    }
    items.forEach(item => {
      const data = map(item);
      const row = document.createElement(data.url ? 'a' : 'div');
      row.className = 'list-group-item' + (data.url ? ' list-group-item-action' : '');
      if (data.url) row.href = root.dataset.context + data.url;
      const title = document.createElement('strong'); title.textContent = data.title;
      const detail = document.createElement('div'); detail.className = 'text-secondary small';
      detail.textContent = data.detail;
      row.append(title, detail); container.append(row);
    });
  }

  function chart(id, type, labels, values, color) {
    if (!window.Chart) return;
    const horizontal = type === 'bar';
    const gradient = el(id).getContext('2d').createLinearGradient(0, 0, 0, 280);
    gradient.addColorStop(0, 'rgba(32, 107, 196, 0.14)');
    gradient.addColorStop(1, 'rgba(32, 107, 196, 0.01)');
    new Chart(el(id), {type, data: {labels, datasets: [{label: 'Giá trị', data: values,
      borderColor: color, backgroundColor: horizontal ? color : gradient,
      fill: !horizontal, borderWidth: horizontal ? 0 : 2.5, borderRadius: 5,
      maxBarThickness: 26, pointRadius: 2, pointHoverRadius: 5, tension: 0.35}]},
      options: {responsive: true, maintainAspectRatio: false, indexAxis: horizontal ? 'y' : 'x',
        plugins: {legend: {display: false}, tooltip: {backgroundColor: '#0b1f3a', padding: 12, cornerRadius: 8}},
        scales: {
          x: {grid: {display: horizontal, color: '#eef2f7'}, border: {display: false}, ticks: {color: '#8a99ac', maxTicksLimit: 7, precision: 0, font: {size: 10}}},
          y: {beginAtZero: true, grid: {display: !horizontal, color: '#eef2f7'}, border: {display: false}, ticks: {color: '#8a99ac', font: {size: 10}}}
        }}});
  }

  Qaute.fetchJson(root.dataset.api).then(response => {
    if (!response.success || !response.data) throw new Error(response.message || 'Không có dữ liệu.');
    const d = response.data;
    set('adm-d-today', number(d.ticketsToday)); set('adm-d-month', number(d.ticketsMonth));
    set('adm-d-unassigned', number(d.unassigned)); set('adm-d-backlog', number(d.backlogOver24h));
    set('adm-d-sla', d.slaSamples ? `${number(d.slaPercent)}%` : '—');
    set('adm-d-csat', d.csatSamples ? `${number(d.csat)}/5` : '—');
    set('adm-d-chat', `${number(d.activeChats)} / ${number(d.waitingChats)}`);
    set('adm-d-appt', number(d.appointmentsToday)); set('adm-d-users', number(d.activeUsers7d));
    set('adm-d-email', number(d.failedEmails));
    set('adm-alert-email', d.failedEmails
      ? `${number(d.failedEmails)} email đang ở trạng thái gửi thất bại. Kiểm tra cấu hình và hàng đợi gửi thư.`
      : 'Không có email gửi thất bại.');
    list('adm-alert-sla', d.slaAlerts, item => item);
    list('adm-alert-categories', d.uncoveredCategories, item => item);
    list('adm-alert-faq', d.lowHelpfulFaqs,
      item => ({title: item.name, detail: `${number(item.count)} phiếu`, url: '/admin/faq'}));
    list('adm-alert-search', d.noResultQueries,
      item => ({title: item.name, detail: `${number(item.count)} lượt`, url: '/admin/faq/synonyms'}));
    list('adm-alert-ratings', d.lowRatings, item => item);
    list('adm-alert-audit', d.recentActivity,
      item => ({title: item.action, detail: item.at ? new Date(item.at).toLocaleString('vi-VN') : ''}));
    el('adm-dashboard-content').classList.remove('d-none');
    chart('adm-d-chart-daily', 'line', d.daily30.map(item => new Date(item.day + 'T00:00:00').toLocaleDateString('vi-VN', {day: '2-digit', month: '2-digit'})),
      d.daily30.map(item => item.count), '#206bc4');
    chart('adm-d-chart-departments', 'bar', d.departments.map(item => item.departmentName),
      d.departments.map(item => item.tickets), '#61b59a');
    chart('adm-d-chart-frt', 'line', d.frtWeekly.map(item => item.weekStart),
      d.frtWeekly.map(item => item.minutes), '#f59f00');
  }).catch(error => {
    const box = el('adm-dashboard-error'); box.textContent = error.message || 'Không thể tải dashboard.';
    box.classList.remove('d-none');
  }).finally(() => el('adm-dashboard-loading').classList.add('d-none'));
})();
