document.addEventListener('DOMContentLoaded', () => {
  const workspace = document.getElementById('workspace-actions');
  if (!workspace) return;

  const csrf = workspace.querySelector('input[name="_csrf"]')?.value;
  const editor = document.getElementById('reply-content');

  function htmlText(html) {
    const doc = new DOMParser().parseFromString(html, 'text/html');
    return (doc.body.textContent || '').trim();
  }

  function editorCode(textarea) {
    return window.jQuery?.fn?.summernote && window.jQuery(textarea).next('.note-editor').length
      ? window.jQuery(textarea).summernote('code') : textarea.value;
  }

  workspace.querySelectorAll('.js-ticket-editor').forEach(textarea => {
    if (!window.jQuery?.fn?.summernote) return;
    window.jQuery(textarea).summernote({
      height: textarea.id === 'reply-content' ? 220 : 180,
      placeholder: 'Nhập nội dung gửi sinh viên…',
      toolbar: [
        ['style', ['bold', 'italic', 'underline', 'clear']],
        ['para', ['ul', 'ol', 'paragraph']],
        ['insert', ['link']],
        ['view', ['codeview']]
      ]
    });
  });

  workspace.querySelectorAll('.qa-workspace-form').forEach(form => {
    const files = form.querySelector('.js-ticket-files');
    const list = form.querySelector('.js-upload-list');
    const uploadError = form.querySelector('.js-upload-error');
    const assets = form.querySelector('.js-asset-ids');
    const textarea = form.querySelector('.js-ticket-editor');
    const editorError = form.querySelector('.js-editor-error');
    const submit = form.querySelector('button[type="submit"]');
    let pending = 0;
    let failed = false;
    let submitted = false;

    function refreshSubmit() {
      if (submit) submit.disabled = pending > 0 || failed || submitted;
    }

    if (files) files.addEventListener('change', async () => {
      failed = false;
      uploadError.textContent = '';
      const selected = Array.from(files.files || []);
      files.value = '';
      for (const file of selected) {
        pending += 1;
        refreshSubmit();
        try {
          const payload = new FormData();
          payload.append('file', file);
          payload.append('purpose', 'TICKET_ATTACHMENT');
          const response = await fetch(workspace.dataset.uploadUrl, {
            method: 'POST',
            headers: csrf ? { 'X-XSRF-TOKEN': csrf } : {},
            credentials: 'same-origin',
            body: payload
          });
          const result = await response.json().catch(() => null);
          if (!response.ok || !result?.success || !result.data?.id) {
            throw new Error(result?.message || `Không thể tải tệp ${file.name} lên.`);
          }
          const hidden = document.createElement('input');
          hidden.type = 'hidden';
          hidden.name = 'assetIds';
          hidden.value = result.data.id;
          assets.append(hidden);
          const item = document.createElement('div');
          item.textContent = `✓ ${file.name}`;
          list.append(item);
        } catch (error) {
          failed = true;
          uploadError.textContent = error.message || `Không thể tải tệp ${file.name} lên.`;
        } finally {
          pending -= 1;
          refreshSubmit();
        }
      }
    });

    form.addEventListener('submit', event => {
      if (submitted || pending > 0 || failed) {
        event.preventDefault();
        if (pending > 0) uploadError.textContent = 'Vui lòng đợi tệp tải lên xong.';
        return;
      }
      if (textarea) {
        textarea.value = editorCode(textarea);
        if (!htmlText(textarea.value)) {
          event.preventDefault();
          editorError.textContent = 'Vui lòng nhập nội dung trước khi gửi.';
          return;
        }
        editorError.textContent = '';
      }
      submitted = true;
      refreshSubmit();
    });
  });

  workspace.closest('.row')?.querySelectorAll('.js-insert-faq').forEach(button => {
    button.addEventListener('click', () => {
      if (!editor) return;
      const link = document.createElement('a');
      link.href = button.dataset.faqUrl;
      link.textContent = button.dataset.faqTitle || 'FAQ';
      if (window.jQuery?.fn?.summernote && window.jQuery(editor).next('.note-editor').length) {
        window.jQuery(editor).summernote('focus');
        window.jQuery(editor).summernote('pasteHTML', link.outerHTML + ' ');
      } else {
        editor.value += (editor.value ? '\n' : '') + link.outerHTML;
        editor.focus();
      }
    });
  });
});
