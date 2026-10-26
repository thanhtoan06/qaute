document.addEventListener('DOMContentLoaded',
  // Khởi tạo các trường biểu mẫu theo vai trò tài khoản.
  () => {
    const form = document.querySelector('[data-user-form]');
    if (!form) return;
    const roleSelect = form.querySelector('#role');
    const mssv = form.querySelector('#mssv');
    // Hiển thị và bật các trường tương ứng với vai trò đang chọn.
    const update = () => {
      const role = roleSelect ? roleSelect.value : form.dataset.role;
      form.querySelectorAll('[data-role-fields]').forEach(
        // Đồng bộ trạng thái khả dụng của từng nhóm trường theo vai trò.
        group => {
          const visible = group.dataset.roleFields === role;
          group.hidden = !visible;
          group.querySelectorAll('input, select').forEach(input => { input.disabled = !visible; });
        });
      if (mssv) mssv.required = role === 'STUDENT';
    };
    if (roleSelect) roleSelect.addEventListener('change', update);
    update();
});
