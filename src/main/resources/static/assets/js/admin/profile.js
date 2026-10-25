document.addEventListener('DOMContentLoaded', () => {
  const passwordForm = document.getElementById('admin-password-form');
  const newPassword = document.getElementById('admin-new-password');
  const confirmPassword = document.getElementById('admin-confirm-password');
  const validateConfirmation = () => {
    confirmPassword?.setCustomValidity(newPassword.value !== confirmPassword.value
      ? 'Mật khẩu xác nhận không khớp' : '');
  };
  newPassword?.addEventListener('input', validateConfirmation);
  confirmPassword?.addEventListener('input', validateConfirmation);
  passwordForm?.addEventListener('submit', () => {
    passwordForm.querySelector('button[type="submit"]').disabled = true;
  });

  const button = document.getElementById('btnChangeAvatar');
  const input = document.getElementById('avatarFileInput');
  const preview = document.getElementById('avatarPreview');
  const placeholder = document.getElementById('avatarPreviewPlaceholder');
  const spinner = document.getElementById('avatarSpinner');
  const error = document.getElementById('avatarError');
  const form = document.getElementById('avatarForm');
  const assetId = document.getElementById('avatarAssetIdInput');
  if (!button || !input || !form) return;

  const loading = value => {
    button.disabled = value;
    spinner.classList.toggle('d-none', !value);
  };
  button.addEventListener('click', () => input.click());
  input.addEventListener('change', async () => {
    const file = input.files?.[0];
    if (!file) return;
    error.classList.add('d-none');
    if (!/^image\/(jpeg|png|webp|gif)$/i.test(file.type) || file.size > 10 * 1024 * 1024) {
      error.textContent = 'Vui lòng chọn ảnh JPG, PNG, WebP hoặc GIF không quá 10 MB.';
      error.classList.remove('d-none');
      input.value = '';
      return;
    }
    loading(true);
    const objectUrl = URL.createObjectURL(file);
    try {
      const image = new Image();
      image.src = objectUrl;
      await image.decode();
      const canvas = document.createElement('canvas');
      canvas.width = 400;
      canvas.height = 400;
      const side = Math.min(image.naturalWidth, image.naturalHeight);
      canvas.getContext('2d').drawImage(image,
        (image.naturalWidth - side) / 2, (image.naturalHeight - side) / 2,
        side, side, 0, 0, 400, 400);
      const blob = await new Promise(resolve => canvas.toBlob(resolve, 'image/jpeg', 0.9));
      if (!blob) throw new Error('Không thể xử lý ảnh.');
      const body = new FormData();
      body.append('file', blob, 'avatar.jpg');
      body.append('purpose', 'AVATAR');
      const csrf = form.querySelector('input[type="hidden"]')?.value;
      const response = await fetch(form.dataset.uploadUrl, {
        method: 'POST', credentials: 'same-origin',
        headers: csrf ? { 'X-XSRF-TOKEN': csrf } : {}, body
      });
      const result = await response.json().catch(() => null);
      if (!response.ok || !result?.success || !result.data?.id) {
        throw new Error(result?.message || 'Không thể tải ảnh lên.');
      }
      preview.src = canvas.toDataURL('image/jpeg', 0.9);
      preview.classList.remove('d-none');
      placeholder?.classList.add('d-none');
      assetId.value = result.data.id;
      form.submit();
    } catch (failure) {
      error.textContent = failure.message || 'Không thể cập nhật ảnh đại diện.';
      error.classList.remove('d-none');
      loading(false);
      input.value = '';
    } finally {
      URL.revokeObjectURL(objectUrl);
    }
  });
});
