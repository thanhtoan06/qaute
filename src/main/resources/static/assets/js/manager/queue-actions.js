// Khởi tạo hộp thoại phân công yêu cầu.
(function () {
  'use strict';

  const modal = document.getElementById('assign-ticket-modal');
  if (!modal) return;

  const form = document.getElementById('assign-ticket-form');
  const label = document.getElementById('assign-ticket-label');
  const select = document.getElementById('assign-to-manager');
  const reason = document.getElementById('assign-reason');
  const submit = document.getElementById('assign-ticket-submit');
  const error = document.getElementById('assign-ticket-error');
  let pendingRequest = null;

  // Hiển thị lỗi trong hộp thoại phân công.
  function showError(message) {
    error.textContent = message;
    error.classList.remove('d-none');
  }

  // Xóa lỗi hiện tại khỏi hộp thoại.
  function clearError() {
    error.textContent = '';
    error.classList.add('d-none');
  }

  // Tạo nhãn tư vấn viên gồm trạng thái và chức danh.
  function optionLabel(advisor) {
    const state = { ONLINE: 'Trực tuyến', BUSY: 'Bận', OFFLINE: 'Ngoại tuyến' };
    const parts = [advisor.fullName || ('#' + advisor.id)];
    if (advisor.jobTitle) parts.push(advisor.jobTitle);
    if (advisor.status) parts.push(state[advisor.status] || advisor.status);
    return parts.join(' · ');
  }

  // Tải danh sách tư vấn viên khi hộp thoại mở.
  modal.addEventListener('show.bs.modal', async function (event) {
    const trigger = event.relatedTarget;
    if (!trigger) return;
    if (pendingRequest) pendingRequest.abort();
    pendingRequest = new AbortController();
    form.action = trigger.dataset.assignUrl;
    label.textContent = '#' + trigger.dataset.ticketId;
    reason.value = '';
    clearError();
    select.replaceChildren(new Option('Đang tải danh sách…', ''));
    select.disabled = true;
    submit.disabled = true;

    try {
      const response = await fetch(trigger.dataset.advisorsUrl, {
        credentials: 'same-origin',
        headers: { Accept: 'application/json' },
        signal: pendingRequest.signal
      });
      if (!response.ok) throw new Error('Không tải được danh sách tư vấn viên. Vui lòng tải lại trang.');
      const body = await response.json();
      if (!body.success || !Array.isArray(body.data)) {
        throw new Error(body.message || 'Không tải được danh sách tư vấn viên.');
      }
      select.replaceChildren(new Option('Chọn tư vấn viên', ''));
      body.data.forEach(function (advisor) {
        select.add(new Option(optionLabel(advisor), String(advisor.id)));
      });
      if (body.data.length === 0) {
        showError('Không có tư vấn viên phù hợp với chuyên mục của yêu cầu.');
      } else {
        select.disabled = false;
        submit.disabled = false;
      }
    } catch (failure) {
      if (failure.name !== 'AbortError') showError(failure.message || 'Không tải được danh sách tư vấn viên.');
    }
  });

  // Hủy yêu cầu đang chờ khi đóng hộp thoại.
  modal.addEventListener('hidden.bs.modal', function () {
    if (pendingRequest) pendingRequest.abort();
    pendingRequest = null;
  });

  // Xác thực dữ liệu trước khi gửi yêu cầu phân công.
  form.addEventListener('submit', function (event) {
    const length = reason.value.trim().length;
    if (!select.value || length < 10 || length > 500) {
      event.preventDefault();
      showError('Vui lòng chọn tư vấn viên và nhập lý do từ 10 đến 500 ký tự.');
      return;
    }
    clearError();
  });
})();
