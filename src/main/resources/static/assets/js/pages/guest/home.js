// Lọc câu hỏi theo chuyên mục được chọn.
function selectFaqCategory(category, btn) {
    var buttons = document.querySelectorAll('#faq-category-buttons button');
    // Đặt lại trạng thái hiển thị của từng nút chuyên mục.
    buttons.forEach(function(b) {
        b.classList.remove('btn-primary');
        b.classList.add('btn-light', 'text-dark');
        b.style.backgroundColor = '';
        b.style.borderColor = '';
    });
    btn.classList.remove('btn-light', 'text-dark');
    btn.classList.add('btn-primary');
    btn.style.backgroundColor = 'var(--hcmute-blue)';
    btn.style.borderColor = 'var(--hcmute-blue)';
    var items = document.querySelectorAll('.faq-item');
    var visibleCount = 0;
    // Hiển thị các câu hỏi thuộc chuyên mục đã chọn.
    items.forEach(function(item) {
        if (category === 'all' || item.getAttribute('data-category') === category) {
            item.style.display = '';
            visibleCount++;
        } else {
            item.style.display = 'none';
        }
    });
    checkFaqEmptyState(visibleCount);
}

// Lọc câu hỏi theo nội dung người dùng nhập.
function filterFaqs() {
    var query = document.getElementById('faq-filter-input').value.toLowerCase().trim();
    var items = document.querySelectorAll('.faq-item');
    var visibleCount = 0;
    // Cập nhật khả năng hiển thị của từng câu hỏi.
    items.forEach(function(item) {
        var text = item.textContent.toLowerCase();
        if (text.indexOf(query) !== -1) {
            item.style.display = '';
            visibleCount++;
        } else {
            item.style.display = 'none';
        }
    });
    checkFaqEmptyState(visibleCount);
}

// Hiển thị trạng thái khi không có câu hỏi phù hợp.
function checkFaqEmptyState(count) {
    var empty = document.getElementById('faq-empty-state');
    if (empty) {
        if (count === 0) {
            empty.classList.remove('d-none');
        } else {
            empty.classList.add('d-none');
        }
    }
}

document.querySelectorAll('#faq-category-buttons [data-faq-category]').forEach(function(button) {
    button.addEventListener('click', function() {
        selectFaqCategory(button.getAttribute('data-faq-category'), button);
    });
});

var faqFilterInput = document.getElementById('faq-filter-input');
if (faqFilterInput) faqFilterInput.addEventListener('input', filterFaqs);

document.querySelectorAll('[data-chatbot-open]').forEach(function(button) {
    button.addEventListener('click', function() {
        window.openQauteChatbot();
    });
});