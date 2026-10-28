// Khởi tạo bộ lọc, báo cáo và biểu đồ quản trị.
(() => {
  'use strict';
  const root = document.getElementById('admin-reports');
  if (!root) return;
  const form = document.getElementById('admin-report-filter');
  const range = document.getElementById('admin-report-range');
  const from = document.getElementById('admin-report-from');
  const to = document.getElementById('admin-report-to');
  const department = document.getElementById('admin-report-department');
  const category = document.getElementById('admin-report-category');
  // Lấy phần tử báo cáo theo mã định danh.
  const get = id => document.getElementById(id);
  const charts = new Map();
  let pending = null, requestId = 0;
  // Định dạng số theo quy ước tiếng Việt.
  const format = value => Number(value || 0).toLocaleString('vi-VN', {maximumFractionDigits: 1});
  // Định dạng giá trị phần trăm.
  const pct = value => `${format(value)}%`;

  // Chuyển ngày thành chuỗi YYYY-MM-DD theo ngày Việt Nam.
  function localDate(date) {
    const year = date.getUTCFullYear(), month = String(date.getUTCMonth() + 1).padStart(2, '0');
    return `${year}-${month}-${String(date.getUTCDate()).padStart(2, '0')}`;
  }

  // Cập nhật ngày bắt đầu và kết thúc theo khoảng thời gian định sẵn.
  function updatePresetDates() {
    if (range.value === 'custom') return;
    const end = new Date(Qaute.todayDateValue() + 'T00:00:00Z');
    const start = new Date(end);
    start.setUTCDate(end.getUTCDate() - Number(range.value) + 1);
    from.value = localDate(start);
    to.value = localDate(end);
  }

  // Đồng bộ bộ lọc ngày và danh sách chuyên mục theo phòng ban.
  function toggleFilters() {
    const custom = range.value === 'custom';
    document.querySelectorAll('.admin-custom-date').forEach(node => node.classList.toggle('d-none', !custom));
    from.disabled = !custom; to.disabled = !custom;
    from.required = custom; to.required = custom;
    [...category.options].forEach(option => {
      if (!option.value) return;
      const hidden = !!department.value && option.dataset.department !== department.value;
      option.hidden = hidden; option.disabled = hidden;
    });
    if (category.selectedOptions[0]?.disabled) category.value = '';
  }

  // Hủy các biểu đồ hiện tại trước khi dựng biểu đồ mới.
  function destroyCharts() { charts.forEach(chart => chart.destroy()); charts.clear(); }
  // Tạo biểu đồ và lưu lại để quản lý vòng đời.
  function chart(id, type, labels, datasets, extra = {}) {
    const canvas = get(id);
    if (!window.Chart || !canvas) return;
    charts.set(id, new Chart(canvas, {type, data: {labels, datasets}, options: {
      responsive: true, maintainAspectRatio: false, indexAxis: extra.horizontal ? 'y' : 'x',
      scales: {y: {beginAtZero: true, ticks: {precision: 0}}}
    }}));
  }

  // Đổ dữ liệu vào bảng hoặc hiển thị nội dung rỗng.
  function table(id, items, cells, emptyText) {
    const body = get(id); body.replaceChildren();
    if (!items?.length) {
      const tr = document.createElement('tr'), td = document.createElement('td');
      td.colSpan = cells.length; td.className = 'text-secondary text-center py-3'; td.textContent = emptyText;
      tr.append(td); body.append(tr); return;
    }
    items.forEach(
      // Tạo một hàng bảng cho từng mục dữ liệu.
      item => {
        const tr = document.createElement('tr');
        cells.forEach(read => {
          const td = document.createElement('td');
          const value = read(item); td.textContent = value == null ? '—' : String(value);
          tr.append(td);
        });
        body.append(tr);
      });
  }

  // Tạo một dòng chỉ số gồm nhãn và giá trị.
  function metric(label, value) {
    const line = document.createElement('div'); line.className = 'd-flex justify-content-between border-bottom py-2 gap-2';
    const name = document.createElement('span'); name.textContent = label;
    const number = document.createElement('strong'); number.textContent = value;
    line.append(name, number); return line;
  }

  // Hiển thị các chỉ số, bảng và biểu đồ từ dữ liệu báo cáo.
  function render(data) {
    destroyCharts();
    const {ticket, knowledge, departments, managers, chat, appointment, weekly, hourly} = data;
    const total = (ticket.perDay || []).reduce((sum, item) => sum + item.count, 0);
    get('adm-kpi-tickets').textContent = format(total);
    get('adm-kpi-frt').textContent = ticket.firstResponse.samples ? `${format(ticket.firstResponse.medianMinutes)} phút` : '—';
    get('adm-kpi-resolution').textContent = ticket.resolution.samples ? `${format(ticket.resolution.medianMinutes)} phút` : '—';
    get('adm-kpi-sla').textContent = ticket.sla.total ? pct(ticket.sla.percent) : '—';
    get('adm-kpi-csat').textContent = ticket.csat.count ? `${format(ticket.csat.avg)}/5` : '—';
    get('adm-kpi-backlog').textContent = format(ticket.backlog.open);
    get('adm-k-views').textContent = format(knowledge.faqViews);
    get('adm-k-helpful').textContent = pct(knowledge.helpfulPercent);
    get('adm-k-noresult').textContent = pct(knowledge.noResultPercent);
    get('adm-k-deflection').textContent = pct(knowledge.deflectionProxyPercent);
    get('admin-report-empty').classList.toggle('d-none', total > 0 || chat.total > 0 || appointment.booked > 0);
    get('admin-report-content').classList.remove('d-none');

    table('admin-manager-table', managers, [item => item.managerName, item => format(item.resolved),
      item => `${format(item.avgFirstResponseMinutes)} phút`, item => `${format(item.csat)}/5`, item => format(item.chats)],
    'Không có Manager phù hợp.');
    table('admin-low-faq-table', knowledge.lowHelpfulFaqs, [item => item.name, item => format(item.count)],
      'Không có FAQ dưới ngưỡng hữu ích.');
    table('admin-noresult-table', knowledge.topNoResultQueries, [item => item.name, item => format(item.count)],
      'Không có truy vấn không kết quả.');
    get('admin-chat-stats').replaceChildren(
      metric('Phiên chat', format(chat.total)), metric('Chờ trung bình', `${format(chat.avgWaitSeconds)} giây`),
      metric('Thời lượng trung bình', `${format(chat.avgDurationMinutes)} phút`),
      metric('Bỏ cuộc', pct(chat.abandonedPercent)));
    get('admin-appointment-stats').replaceChildren(
      metric('Lịch đã đặt', format(appointment.booked)), metric('Hoàn thành', format(appointment.completed)),
      metric('Không đến', pct(appointment.noShowPercent)), metric('Hủy', pct(appointment.cancelPercent)),
      metric('Sử dụng slot', pct(appointment.utilizationPercent)));

    chart('admin-chart-weekly', 'line', weekly.map(item => item.weekStart),
      [{label: 'Ticket', data: weekly.map(item => item.count), borderColor: '#206bc4', tension: 0.25}]);
    chart('admin-chart-hourly', 'bar', hourly.map(item => `${String(item.hour).padStart(2, '0')}:00`), [
      {label: 'Ticket', data: hourly.map(item => item.tickets), backgroundColor: '#206bc4'},
      {label: 'Chat', data: hourly.map(item => item.chats), backgroundColor: '#2fb344'}]);
    chart('admin-chart-departments', 'bar', departments.map(item => item.departmentName),
      [{label: 'Ticket', data: departments.map(item => item.tickets), backgroundColor: '#206bc4'}],
      {horizontal: true});
  }

  // Tải báo cáo theo bộ lọc hiện tại và xử lý trạng thái yêu cầu.
  async function load(updateUrl) {
    toggleFilters();
    if (!form.reportValidity()) return;
    const params = new URLSearchParams(new FormData(form));
    if (updateUrl) history.replaceState(null, '', `${form.action}?${params}`);
    if (pending) pending.abort();
    pending = new AbortController();
    const current = ++requestId;
    get('admin-report-loading').classList.remove('d-none');
    get('admin-report-error').classList.add('d-none');
    try {
      const response = await Qaute.fetchJson(`${root.dataset.apiUrl}?${params}`, {signal: pending.signal});
      if (current !== requestId) return;
      if (!response.success || !response.data) throw new Error(response.message || 'Không có dữ liệu báo cáo.');
      render(response.data);
    } catch (error) {
      if (error.name === 'AbortError' || current !== requestId) return;
      destroyCharts(); get('admin-report-content').classList.add('d-none');
      const box = get('admin-report-error'); box.textContent = error.message || 'Không thể tải báo cáo.';
      box.classList.remove('d-none');
    } finally {
      if (current === requestId) get('admin-report-loading').classList.add('d-none');
    }
  }

  form.addEventListener('submit',
    // Ngăn gửi biểu mẫu mặc định và tải báo cáo theo bộ lọc.
    event => { event.preventDefault(); load(true); });
  range.addEventListener('change',
    // Cập nhật ngày theo khoảng mới và tải lại báo cáo khi đủ điều kiện.
    () => {
      updatePresetDates(); toggleFilters();
      if (range.value !== 'custom' || (from.value && to.value)) load(true);
    });
  [department, category].forEach(node => node.addEventListener('change', () => load(true)));
  document.querySelectorAll('.js-report-export').forEach(button => button.addEventListener('click',
    // Xuất báo cáo theo bộ lọc hiện tại.
    () => {
      toggleFilters();
      if (!form.reportValidity()) return;
      updatePresetDates();
      const params = new URLSearchParams({type: button.dataset.type, from: from.value, to: to.value});
      if (department.value) params.set('departmentId', department.value);
      if (category.value) params.set('categoryId', category.value);
      window.location.assign(`${root.dataset.exportUrl}?${params}`);
    }));
  document.querySelectorAll('[data-bs-toggle="tab"]').forEach(button =>
    button.addEventListener('shown.bs.tab', () => charts.forEach(instance => instance.resize())));
  toggleFilters();
  load(false);
})();
