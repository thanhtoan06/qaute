(function () {
    'use strict';
    // Configuration tables and small, already loaded histories also need usable pages.
    // Large search screens use their own database pagination.
    function paginate(container) {
        const size = 25;
        let page = 0;
        let controls;
        function refresh(reset) {
            if (reset) page = 0;
            const rows = Array.from(container.children);
            const total = Math.ceil(rows.length / size);
            page = Math.max(0, Math.min(page, total - 1));
            rows.forEach((row, index) => { row.hidden = total > 1 && Math.floor(index / size) !== page; });
            if (controls) controls.remove();
            if (total <= 1) return;
            controls = document.createElement('nav');
            controls.className = 'd-flex flex-wrap align-items-center gap-2 my-3';
            controls.setAttribute('aria-label', 'Phân trang danh sách');
            function button(label, target, disabled) {
                const element = document.createElement('button');
                element.type = 'button'; element.className = 'btn btn-sm btn-outline-secondary';
                element.textContent = label; element.disabled = disabled;
                element.addEventListener('click', () => { page = target; refresh(false); });
                return element;
            }
            const label = document.createElement('span');
            label.textContent = 'Trang ' + (page + 1) + '/' + total + ' · ' + rows.length + ' mục';
            controls.append(button('Trước', page - 1, page === 0), label,
                button('Sau', page + 1, page === total - 1));
            const parent = container.tagName === 'TBODY' ? container.closest('.table-responsive') : container;
            parent.after(controls);
        }
        refresh(false);
        new MutationObserver(() => refresh(true)).observe(container, {childList: true});
    }
    function init() {
        document.querySelectorAll('table.table').forEach(table => {
            if (!table.closest('.table-responsive')) {
                const wrapper = document.createElement('div'); wrapper.className = 'table-responsive';
                table.before(wrapper); wrapper.append(table);
            }
            if (table.tBodies[0]) paginate(table.tBodies[0]);
        });
        document.querySelectorAll('[data-tv2-pageable]').forEach(paginate);
        document.querySelectorAll('.modal-dialog').forEach(dialog => dialog.classList.add('modal-dialog-scrollable'));
    }
    if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', init);
    else init();
}());
