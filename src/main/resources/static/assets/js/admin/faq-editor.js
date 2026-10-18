document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('faq-editor-form');
  const textarea = document.getElementById('faq-body');
  if (!form || !textarea) return;

  const error = document.getElementById('faq-upload-error');
  const assets = document.getElementById('faq-image-assets');
  const submit = form.querySelector('button[type="submit"]');
  const csrf = document.querySelector('meta[name="_csrf"]')?.content;
  let pending = 0;

  async function uploadImage(file) {
    error.textContent = '';
    pending += 1;
    submit.disabled = true;
    try {
      const payload = new FormData();
      payload.append('file', file);
      payload.append('purpose', 'FAQ_IMAGE');
      const response = await fetch(form.dataset.uploadUrl, {
        method: 'POST',
        headers: csrf ? { 'X-XSRF-TOKEN': csrf } : {},
        credentials: 'same-origin',
        body: payload
      });
      if (!response.ok) throw new Error('Không thể tải ảnh lên. Vui lòng thử lại.');
      const result = await response.json();
      if (!result.assetId || !result.publicUrl) throw new Error('Phản hồi tải ảnh không hợp lệ.');

      const img = document.createElement('img');
      img.src = result.publicUrl;
      img.alt = file.name;
      window.jQuery(textarea).summernote('insertNode', img);

      const hidden = document.createElement('input');
      hidden.type = 'hidden';
      hidden.name = 'imageAssetIds';
      hidden.value = result.assetId;
      assets.append(hidden);
    } catch (failure) {
      error.textContent = failure.message || 'Không thể tải ảnh lên.';
    } finally {
      pending -= 1;
      submit.disabled = pending > 0;
    }
  }

  if (window.jQuery?.fn?.summernote) {
    window.jQuery(textarea).summernote({
      height: 360,
      placeholder: 'Viết nội dung FAQ…',
      toolbar: [
        ['style', ['style']],
        ['font', ['bold', 'italic', 'underline', 'clear']],
        ['para', ['ul', 'ol', 'paragraph']],
        ['insert', ['link', 'picture', 'table']],
        ['view', ['codeview']]
      ],
      callbacks: {
        onImageUpload(files) {
          Array.from(files).forEach(uploadImage);
        }
      }
    });
  }

  form.addEventListener('submit', event => {
    if (pending > 0) {
      event.preventDefault();
      error.textContent = 'Vui lòng đợi ảnh tải lên xong.';
      return;
    }
    if (window.jQuery?.fn?.summernote) {
      textarea.value = window.jQuery(textarea).summernote('code');
    }
  });
});
