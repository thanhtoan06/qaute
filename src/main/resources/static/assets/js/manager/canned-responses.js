// Khởi tạo biểu mẫu mẫu trả lời sau khi tài liệu được tải.
document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('canned-response-form');
  if (!form) return;
  const scope = document.getElementById('canned-scope');
  const categoryGroup = document.getElementById('canned-category-group');
  const category = document.getElementById('canned-category');
  const body = document.getElementById('canned-body');
  const error = document.getElementById('canned-body-error');
  const submit = form.querySelector('button[type="submit"]');

  // Cập nhật các trường chuyên mục theo phạm vi sử dụng.
  function updateScope() {
    const shared = scope.value === 'SHARED';
    categoryGroup.hidden = !shared;
    category.required = shared;
    if (!shared) category.value = '';
  }
  scope.addEventListener('change', updateScope);
  updateScope();

  if (window.jQuery?.fn?.summernote) {
    window.jQuery(body).summernote({
      height: 230,
      placeholder: 'Soạn mẫu trả lời…',
      toolbar: [
        ['style', ['bold', 'italic', 'underline', 'clear']],
        ['para', ['ul', 'ol', 'paragraph']],
        ['insert', ['link']],
        ['view', ['codeview']]
      ]
    });
  }

  // Kiểm tra nội dung trước khi gửi biểu mẫu.
  form.addEventListener('submit', event => {
    if (window.jQuery?.fn?.summernote) body.value = window.jQuery(body).summernote('code');
    const text = new DOMParser().parseFromString(body.value, 'text/html').body.textContent.trim();
    if (!text || !form.querySelector('#canned-title').value.trim() ||
        (scope.value === 'SHARED' && !category.value)) {
      event.preventDefault();
      error.textContent = 'Vui lòng nhập tiêu đề, nội dung và chuyên mục nếu dùng chung.';
      return;
    }
    error.textContent = '';
    submit.disabled = true;
  });
});
