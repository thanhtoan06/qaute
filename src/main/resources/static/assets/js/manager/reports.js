(() => {
  'use strict';
  const root = document.getElementById('manager-reports');
  if (!root) return;
  const form = document.getElementById('report-filter');
  const range = document.getElementById('report-range');
  const mine = document.getElementById('report-mine');
  const category = document.getElementById('report-category');
  const dateInputs = [document.getElementById('report-from'), document.getElementById('report-to')];
  const charts = new Map();
  let requestId = 0;
  let pending = null;
  const byId = id => document.getElementById(id);
  const format = value => Number(value || 0).toLocaleString('vi-VN', {maximumFractionDigits: 1});
  const percentage = value => `${format(value)}%`;
  const row = (label, value) => {
    const node = document.createElement('div');
    node.className = 'd-flex justify-content-between border-bottom py-2 gap-2';
    const name = document.createElement('span'); name.textContent = label;
    const number = document.createElement('strong'); number.textContent = value;
    node.append(name, number);
    return node;
  };

  function toggleFilters() {
    const custom = range.value === 'custom';
    document.querySelectorAll('.report-custom-date').forEach(node => node.classList.toggle('d-none', !custom));
    dateInputs.forEach(input => { input.disabled = !custom; input.required = custom; });
    category.disabled = mine.checked;
  }

  function destroyCharts() {
    charts.forEach(chart => chart.destroy());
    charts.clear();
  }

  function draw(id, type, labels, values, color, extra = {}) {
    const canvas = byId(id);
    if (!canvas || !window.Chart) return;
    charts.set(id, new Chart(canvas, {
      type,
      data: {labels, datasets: [{label: extra.label || 'Yêu cầu', data: values,
        borderColor: color, backgroundColor: extra.colors || color, fill: false, tension: 0.25}]},
      options: {responsive: true, maintainAspectRatio: false, indexAxis: extra.horizontal ? 'y' : 'x',
        plugins: {legend: {display: type === 'doughnut'}},
        scales: type === 'doughnut' ? {} : {y: {beginAtZero: true, ticks: {precision: 0}}}}
    }));
  }

  function showKpis(ticket) {
    const total = (ticket.perDay || []).reduce((sum, day) => sum + day.count, 0);
    byId('kpi-tickets').textContent = format(total);
    byId('kpi-frt').textContent = ticket.firstResponse.samples ? `${format(ticket.firstResponse.medianMinutes)} phút` : '—';
    byId('kpi-resolution').textContent = ticket.resolution.samples ? `${format(ticket.resolution.medianMinutes)} phút` : '—';
    byId('kpi-sla').textContent = ticket.sla.total ? percentage(ticket.sla.percent) : '—';
    byId('kpi-csat').textContent = ticket.csat.count ? `${format(ticket.csat.avg)}/5` : '—';
    byId('kpi-backlog').textContent = format(ticket.backlog.open);
    byId('kpi-reopen').textContent = percentage(ticket.reopenRate);
    return total;
  }

  function showWorkload(items) {
    const tbody = byId('report-workload');
    tbody.replaceChildren();
    if (!items?.length) {
      const tr = document.createElement('tr'), td = document.createElement('td');
      td.colSpan = 4; td.className = 'text-secondary text-center py-3'; td.textContent = 'Chưa có tư vấn viên trong phạm vi.';
      tr.append(td); tbody.append(tr); return;
    }
    items.forEach(item => {
      const tr = document.createElement('tr');
      [item.managerName, item.open, item.capacity, item.resolvedInRange].forEach(value => {
        const td = document.createElement('td'); td.textContent = value == null ? '—' : String(value); tr.append(td);
      });
      tbody.append(tr);
    });
  }

  function showOther(chat, appointment) {
    const target = byId('report-other');
    target.replaceChildren(
      row('Phiên chat', format(chat.total)),
      row('Chờ trung bình', `${format(chat.avgWaitSeconds)} giây`),
      row('Thời lượng chat', `${format(chat.avgDurationMinutes)} phút`),
      row('Bỏ cuộc', percentage(chat.abandonedPercent)),
      row('Lịch đã đặt', format(appointment.booked)),
      row('Lịch hoàn thành', format(appointment.completed)),
      row('Không đến', percentage(appointment.noShowPercent)),
      row('Hủy lịch', percentage(appointment.cancelPercent)),
      row('Sử dụng slot', percentage(appointment.utilizationPercent))
    );
  }

  function render(data) {
    destroyCharts();
    const {ticket, chat, appointment} = data;
    const total = showKpis(ticket);
    const empty = total === 0 && Number(ticket.backlog.open) === 0 && Number(ticket.csat.count) === 0
      && Number(chat.total) === 0 && Number(appointment.booked) === 0;
    byId('report-empty').classList.toggle('d-none', !empty);
    byId('report-content').classList.toggle('d-none', empty);
    if (empty) return;
    showWorkload(ticket.workload);
    showOther(chat, appointment);
    const daily = ticket.perDay || [], categories = ticket.byCategory || [], statuses = ticket.byStatus || [];
    draw('chart-daily', 'line', daily.map(item => item.day), daily.map(item => item.count), '#206bc4');
    draw('chart-category', 'bar', categories.map(item => item.name), categories.map(item => item.count), '#4299e1', {horizontal: true});
    draw('chart-status', 'doughnut', statuses.map(item => item.name), statuses.map(item => item.count), '#206bc4',
      {colors: ['#206bc4', '#f59f00', '#2fb344', '#d63939', '#ae3ec9', '#6c757d', '#17a2b8']});
    const distribution = ticket.csat.distribution || [0, 0, 0, 0, 0];
    draw('chart-csat', 'bar', ['1 sao', '2 sao', '3 sao', '4 sao', '5 sao'], distribution,
      '#2fb344', {label: 'Lượt đánh giá'});
  }

  async function load(updateUrl) {
    toggleFilters();
    if (!form.reportValidity()) return;
    const params = new URLSearchParams(new FormData(form));
    if (updateUrl) history.replaceState(null, '', `${form.action}?${params}`);
    if (pending) pending.abort();
    pending = new AbortController();
    const current = ++requestId;
    byId('report-loading').classList.remove('d-none');
    byId('report-error').classList.add('d-none');
    try {
      const response = await Qaute.fetchJson(`${root.dataset.apiUrl}?${params}`, {signal: pending.signal});
      if (current !== requestId) return;
      if (!response.success || !response.data) throw new Error(response.message || 'Không có dữ liệu báo cáo.');
      render(response.data);
    } catch (error) {
      if (error.name === 'AbortError' || current !== requestId) return;
      destroyCharts();
      byId('report-content').classList.add('d-none');
      byId('report-empty').classList.add('d-none');
      const box = byId('report-error'); box.textContent = error.message || 'Không thể tải báo cáo.';
      box.classList.remove('d-none');
    } finally {
      if (current === requestId) byId('report-loading').classList.add('d-none');
    }
  }

  form.addEventListener('submit', event => { event.preventDefault(); load(true); });
  [range, category, mine].forEach(input => input.addEventListener('change', () => {
    toggleFilters();
    if (range.value !== 'custom' || (dateInputs[0].value && dateInputs[1].value)) load(true);
  }));
  toggleFilters();
  load(false);
})();
