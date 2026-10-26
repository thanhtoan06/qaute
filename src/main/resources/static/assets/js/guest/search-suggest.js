// Khởi tạo chức năng gợi ý tìm kiếm cho trang khách.
(function (window, document) {
    'use strict';

    // Gắn và quản lý danh sách gợi ý cho một ô tìm kiếm.
    function initSuggest(input) {
        if (!input || input.dataset.qaSuggestInitialized) {
            return;
        }
        input.dataset.qaSuggestInitialized = 'true';
        input.setAttribute('autocomplete', 'off');

        const source = input.getAttribute('data-qa-suggest') || 'SEARCH';

        let wrapper = input.parentElement;
        if (!wrapper.classList.contains('qa-suggest-wrapper')) {
            const container = document.createElement('div');
            container.className = 'qa-suggest-wrapper position-relative flex-grow-1';
            input.parentNode.insertBefore(container, input);
            container.appendChild(input);
            wrapper = container;
        }

        const dropdown = document.createElement('div');
        dropdown.className = 'qa-suggest-dropdown dropdown-menu w-100 shadow border';
        dropdown.style.cssText = 'display: none; position: absolute; top: 100%; left: 0; z-index: 1050; max-height: 380px; overflow-y: auto; margin-top: 2px;';
        wrapper.appendChild(dropdown);

        let activeIndex = -1;
        let currentSuggestions = [];
        let lastQuery = '';

        // Ẩn danh sách gợi ý và xóa trạng thái hiện tại.
        function hideDropdown() {
            dropdown.style.display = 'none';
            dropdown.innerHTML = '';
            activeIndex = -1;
            currentSuggestions = [];
        }

        // Hiển thị các kết quả gợi ý phù hợp với truy vấn.
        function renderSuggestions(suggestions, query) {
            currentSuggestions = suggestions || [];
            activeIndex = -1;
            dropdown.innerHTML = '';

            if (currentSuggestions.length === 0) {
                hideDropdown();
                return;
            }

            // Tạo liên kết hiển thị cho từng kết quả gợi ý.
            currentSuggestions.forEach(function (item, index) {
                const a = document.createElement('a');
                a.href = '/faq/' + (item.slug || '');
                a.className = 'dropdown-item d-flex flex-column py-2 px-3 border-bottom text-wrap';
                a.setAttribute('data-index', String(index));

                const titleDiv = document.createElement('div');
                titleDiv.className = 'fw-semibold text-truncate';
                titleDiv.innerHTML = window.Qaute ? window.Qaute.escapeHtml(item.title) : item.title;
                a.appendChild(titleDiv);

                if (item.categoryName) {
                    const catDiv = document.createElement('div');
                    catDiv.className = 'small text-secondary mt-1 text-truncate';
                    catDiv.innerHTML = '<i class="bi bi-folder me-1 text-primary"></i>' +
                        (window.Qaute ? window.Qaute.escapeHtml(item.categoryName) : item.categoryName);
                    a.appendChild(catDiv);
                }

                // Chọn gợi ý khi nhấn chuột xuống liên kết.
                a.addEventListener('mousedown', function (e) {
                    e.preventDefault();
                    selectItem(item, query);
                });

                dropdown.appendChild(a);
            });

            dropdown.style.display = 'block';
        }

        // Gửi lượt chọn gợi ý và chuyển đến trang câu hỏi.
        function selectItem(item, query) {
            if (!item) {
                return;
            }
            const payload = JSON.stringify({
                query: query || input.value.trim(),
                faqId: item.id,
                source: source
            });

            try {
                if (navigator.sendBeacon) {
                    const blob = new Blob([payload], { type: 'application/json' });
                    navigator.sendBeacon('/api/public/search-click', blob);
                } else {
                    fetch('/api/public/search-click', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: payload,
                        keepalive: true
                    });
                }
            } catch (ignored) {
            }

            window.location.href = '/faq/' + encodeURIComponent(item.slug);
        }

        // Cập nhật mục gợi ý đang được chọn bằng bàn phím.
        function highlightItem(index) {
            const items = dropdown.querySelectorAll('.dropdown-item');
            items.forEach(function (el) {
                el.classList.remove('active');
            });
            if (index >= 0 && index < items.length) {
                items[index].classList.add('active');
                items[index].scrollIntoView({ block: 'nearest' });
            }
        }

        const debouncedFetch = (window.Qaute && window.Qaute.debounce)
            ? window.Qaute.debounce(fetchSuggestions, 300)
            // Lên lịch gọi API sau khoảng debounce.
            : function () {
                window.clearTimeout(input._suggestTimer);
                input._suggestTimer = window.setTimeout(fetchSuggestions, 300);
            };

        // Tải gợi ý từ API cho truy vấn hiện tại.
        function fetchSuggestions() {
            const q = input.value.trim();
            if (q.length < 2) {
                hideDropdown();
                return;
            }
            lastQuery = q;
            const url = '/api/faqs/suggestions?q=' + encodeURIComponent(q) + '&source=' + encodeURIComponent(source);
            if (window.Qaute && window.Qaute.fetchJson) {
                window.Qaute.fetchJson(url)
                    // Hiển thị phản hồi nếu truy vấn vẫn còn khớp.
                    .then(function (res) {
                        if (input.value.trim() === q) {
                            renderSuggestions(res.data, q);
                        }
                    })
                    // Ẩn gợi ý khi yêu cầu tải dữ liệu thất bại.
                    .catch(function () {
                        hideDropdown();
                    });
            } else {
                fetch(url)
                    // Chuyển phản hồi HTTP thành dữ liệu JSON.
                    .then(function (res) { return res.json(); })
                    // Hiển thị phản hồi nếu truy vấn vẫn còn khớp.
                    .then(function (res) {
                        if (input.value.trim() === q) {
                            renderSuggestions(res.data, q);
                        }
                    })
                    // Ẩn gợi ý khi yêu cầu tải dữ liệu thất bại.
                    .catch(function () {
                        hideDropdown();
                    });
            }
        }

        // Tải gợi ý khi nội dung ô tìm kiếm thay đổi.
        input.addEventListener('input', function () {
            debouncedFetch();
        });

        // Điều hướng và chọn gợi ý bằng các phím mũi tên, Enter hoặc Escape.
        input.addEventListener('keydown', function (e) {
            const items = dropdown.querySelectorAll('.dropdown-item');
            if (dropdown.style.display !== 'none' && items.length > 0) {
                if (e.key === 'ArrowDown') {
                    e.preventDefault();
                    activeIndex = (activeIndex + 1) % items.length;
                    highlightItem(activeIndex);
                } else if (e.key === 'ArrowUp') {
                    e.preventDefault();
                    activeIndex = (activeIndex - 1 + items.length) % items.length;
                    highlightItem(activeIndex);
                } else if (e.key === 'Enter') {
                    if (activeIndex >= 0 && activeIndex < currentSuggestions.length) {
                        e.preventDefault();
                        selectItem(currentSuggestions[activeIndex], lastQuery);
                    }
                } else if (e.key === 'Escape') {
                    e.preventDefault();
                    hideDropdown();
                }
            }
        });

        // Ẩn danh sách khi nhấp bên ngoài ô tìm kiếm.
        document.addEventListener('click', function (e) {
            if (!wrapper.contains(e.target)) {
                hideDropdown();
            }
        });
    }

    // Khởi tạo gợi ý cho tất cả ô tìm kiếm trên trang.
    function initAll() {
        const inputs = document.querySelectorAll('input[data-qa-suggest]');
        inputs.forEach(initSuggest);
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initAll);
    } else {
        initAll();
    }
}(window, document));

