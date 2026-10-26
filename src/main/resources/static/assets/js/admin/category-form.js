/*
 * Form chuyên mục (admin): lọc danh sách "Chuyên mục cha" theo phòng ban đã chọn,
 * tự viết hoa ô mã và xem trước biểu tượng. Không cần thư viện ngoài.
 */
// Khởi tạo bộ lọc chuyên mục, chuẩn hóa mã và xem trước biểu tượng.
(function () {
  'use strict';

  var department = document.getElementById('departmentId');
  var parent = document.getElementById('parentId');
  var code = document.getElementById('code');
  var icon = document.getElementById('icon');
  var iconPreview = document.getElementById('iconPreview');

  // ---- Chuyên mục cha: chỉ hiện các chuyên mục cấp 1 thuộc phòng ban đang chọn ----
  if (department && parent) {
    var emptyOption = parent.querySelector('option[value=""]');
    // Lưu toàn bộ lựa chọn gốc để dựng lại mỗi lần đổi phòng ban.
    var allOptions = Array.prototype.slice.call(parent.querySelectorAll('option[data-department-id]'));

    // Hiển thị các chuyên mục cha thuộc phòng ban đang chọn.
    var renderParents = function (selectedValue) {
      var departmentId = department.value;
      parent.innerHTML = '';
      parent.appendChild(emptyOption);
      allOptions.forEach(
        // Giữ lại các lựa chọn thuộc phòng ban hiện tại.
        function (option) {
          if (departmentId !== '' && option.getAttribute('data-department-id') === departmentId) {
            option.selected = false;
            parent.appendChild(option);
          }
        });
      parent.value = selectedValue;
      if (parent.value !== selectedValue) {
        parent.value = '';
      }
    };

    // Lần đầu: giữ nguyên lựa chọn do máy chủ đặt (khi sửa hoặc khi thêm chủ đề con).
    renderParents(parent.getAttribute('data-selected') || '');

    // Đổi phòng ban thì chuyên mục cha cũ không còn hợp lệ nên về "không có".
    department.addEventListener('change',
      // Làm mới danh sách chuyên mục cha khi đổi phòng ban.
      function () {
        renderParents('');
      });
  }

  // ---- Mã: viết hoa khi rời ô (máy chủ cũng chuẩn hóa nhưng kiểm tra định dạng chạy trước) ----
  if (code) {
    code.addEventListener('blur',
      // Chuẩn hóa mã chuyên mục khi người dùng rời ô nhập.
      function () {
        code.value = code.value.trim().toUpperCase();
      });
  }

  // ---- Biểu tượng: xem trước ngay khi gõ, chỉ nhận đúng dạng bi-xxx ----
  if (icon && iconPreview) {
    // Cập nhật biểu tượng xem trước theo giá trị hợp lệ trong ô nhập.
    var updatePreview = function () {
      var value = icon.value.trim();
      iconPreview.className = /^bi-[a-z0-9-]{1,37}$/.test(value) ? 'bi ' + value : 'bi';
    };
    icon.addEventListener('input', updatePreview);
    updatePreview();
  }
})();
