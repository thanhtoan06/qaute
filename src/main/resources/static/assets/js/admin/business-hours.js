document.addEventListener('DOMContentLoaded',
  // Khởi tạo biểu mẫu giờ làm việc và các khoảng thời gian.
  () => {
    const form = document.getElementById('business-hours-form');
    if (!form) return;

    // Đánh lại chỉ số và tên trường sau khi thay đổi các khoảng giờ.
    function renumber() {
      let index = 0;
      form.querySelectorAll('.hours-day').forEach(day => {
        const rows = day.querySelectorAll('.hour-interval');
        day.querySelector('[data-add-interval]').disabled = rows.length >= 4;
        rows.forEach(row => {
          row.querySelectorAll('[data-field]').forEach(input => {
            const field = input.dataset.field;
            input.name = field === '_active'
              ? `_intervals[${index}].active`
              : `intervals[${index}].${field}`;
          });
          index += 1;
        });
        });
  }

    // Tạo trường nhập liệu cho một khoảng giờ.
    function input(type, field, attributes = {}) {
      const element = document.createElement('input');
      element.type = type;
      element.dataset.field = field;
      Object.assign(element, attributes);
      return element;
    }

    // Thêm một khoảng giờ mới cho ngày được chọn.
    function addRow(day) {
      const rows = day.querySelector('.hour-intervals');
      if (rows.children.length >= 4) return;
      const row = document.createElement('div');
      row.className = 'hour-interval d-flex flex-wrap gap-2 align-items-center mb-2';
      row.append(input('hidden', 'dayOfWeek', { value: day.dataset.day }));
      row.append(input('time', 'start', { className: 'form-control w-auto', required: true, ariaLabel: 'Giờ bắt đầu' }));
      const separator = document.createElement('span');
      separator.textContent = 'đến';
      row.append(separator);
      row.append(input('time', 'end', { className: 'form-control w-auto', required: true, ariaLabel: 'Giờ kết thúc' }));
      row.append(input('hidden', '_active', { value: 'on' }));
      const label = document.createElement('label');
      label.className = 'form-check mb-0';
      label.append(input('checkbox', 'active', { className: 'form-check-input', value: 'true', checked: true }));
      label.append(document.createTextNode(' Bật'));
      row.append(label);
      const remove = document.createElement('button');
      remove.type = 'button';
      remove.className = 'btn btn-sm btn-outline-danger';
      remove.dataset.removeInterval = '';
      remove.textContent = 'Xóa';
      row.append(remove);
      rows.append(row);
      renumber();
    }

    form.addEventListener('click',
      // Xử lý thao tác thêm hoặc xóa khoảng giờ trong biểu mẫu.
      event => {
        const add = event.target.closest('[data-add-interval]');
        if (add) addRow(add.closest('.hours-day'));
        const remove = event.target.closest('[data-remove-interval]');
        if (remove) {
          remove.closest('.hour-interval').remove();
          renumber();
        }
      });
    renumber();
});
