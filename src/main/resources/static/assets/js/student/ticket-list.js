(function () {
    'use strict';

    var sortSelect = document.querySelector('[data-ticket-sort]');
    if (!sortSelect) return;

    sortSelect.addEventListener('change', function () {
        var query = new URLSearchParams({
            status: sortSelect.getAttribute('data-status') || '',
            q: sortSelect.getAttribute('data-query') || '',
            categoryId: sortSelect.getAttribute('data-category-id') || '',
            from: sortSelect.getAttribute('data-from') || '',
            to: sortSelect.getAttribute('data-to') || '',
            sort: sortSelect.value
        });
        window.location.href = sortSelect.getAttribute('data-context-path')
            + '/student/tickets?' + query.toString();
    });
}());
