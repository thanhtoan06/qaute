document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('announcement-form');
  if (!form) return;
  const body = document.getElementById('announcement-body');
  if (window.jQuery?.fn?.summernote) window.jQuery(body).summernote({height: 300});
  const uploader = window.Qaute?.initUploader?.('#announcement-cover-uploader', 'ANNOUNCEMENT_COVER', 1);
  form.addEventListener('submit', event => {
    if (uploader?.isUploading()) {
      event.preventDefault();
      document.getElementById('announcement-upload-error').textContent = 'Vui lòng chờ ảnh tải xong.';
      return;
    }
    const ids = uploader?.getAssetIds() || [];
    if (ids.length) document.getElementById('cover-asset-id').value = ids[0];
    if (window.jQuery?.fn?.summernote) body.value = window.jQuery(body).summernote('code');
  });
});
