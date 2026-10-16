/*
 * Phạm vi Manager: chọn chuyên mục cha sẽ chọn các chuyên mục con hiện có.
 * Chuyên mục con vẫn có thể được bỏ chọn độc lập; ô "phụ trách chính" chỉ hợp lệ
 * khi chính chuyên mục đó đang được giao.
 */
(function () {
  'use strict';

  var form = document.getElementById('managerScopeForm');
  if (!form) {
    return;
  }

  var categoryCheckboxes = Array.prototype.slice.call(
    form.querySelectorAll('.js-scope-category'));

  function selectorForCategory(categoryId) {
    return '.js-scope-category[data-category-id="' + categoryId + '"]';
  }

  function syncPrimary(categoryId) {
    var category = form.querySelector(selectorForCategory(categoryId));
    var primary = form.querySelector('.js-primary-category[data-category-id="' + categoryId + '"]');
    if (!primary) {
      return;
    }
    var assigned = category && category.checked;
    primary.disabled = !assigned;
    if (!assigned) {
      primary.checked = false;
    }
  }

  function syncAllPrimary() {
    categoryCheckboxes.forEach(function (category) {
      syncPrimary(category.getAttribute('data-category-id'));
    });
  }

  categoryCheckboxes.forEach(function (category) {
    category.addEventListener('change', function () {
      var categoryId = category.getAttribute('data-category-id');

      // Bỏ/chọn cha tác động tới toàn bộ con; sau đó người dùng có thể chỉnh từng con.
      if (category.classList.contains('js-parent-category')) {
        form.querySelectorAll('.js-child-category[data-parent-id="' + categoryId + '"]')
          .forEach(function (child) {
            child.checked = category.checked;
            syncPrimary(child.getAttribute('data-category-id'));
          });
      }
      syncPrimary(categoryId);
    });
  });

  syncAllPrimary();
})();
