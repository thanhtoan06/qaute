document.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector('[data-user-form]');
  if (!form) return;
  const roleSelect = form.querySelector('#role');
  const mssv = form.querySelector('#mssv');
  const update = () => {
    const role = roleSelect ? roleSelect.value : form.dataset.role;
    form.querySelectorAll('[data-role-fields]').forEach(group => {
      const visible = group.dataset.roleFields === role;
      group.hidden = !visible;
      group.querySelectorAll('input, select').forEach(input => { input.disabled = !visible; });
    });
    if (mssv) mssv.required = role === 'STUDENT';
  };
  if (roleSelect) roleSelect.addEventListener('change', update);
  update();
});
