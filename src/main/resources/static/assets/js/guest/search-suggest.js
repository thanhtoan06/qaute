(function (window, document) {
    'use strict';

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

        function hideDropdown() {
            dropdown.style.display = 'none';
            dropdown.innerHTML = '';
            activeIndex = -1;
            currentSuggestions = [];
        }

        function renderSuggestions(suggestions, query) {
            currentSuggestions = suggestions || [];
            activeIndex = -1;
            dropdown.innerHTML = '';

            if (currentSuggestions.length === 0) {
                hideDropdown();
                return;
            }

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

                a.addEventListener('mousedown', function (e) {
                    e.preventDefault();
                    selectItem(item, query);
                });

                dropdown.appendChild(a);
            });

            dropdown.style.display = 'block';
        }

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
            : function () {
                window.clearTimeout(input._suggestTimer);
                input._suggestTimer = window.setTimeout(fetchSuggestions, 300);
            };

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
                    .then(function (res) {
                        if (input.value.trim() === q) {
                            renderSuggestions(res.data, q);
                        }
                    })
                    .catch(function () {
                        hideDropdown();
                    });
            } else {
                fetch(url)
                    .then(function (res) { return res.json(); })
                    .then(function (res) {
                        if (input.value.trim() === q) {
                            renderSuggestions(res.data, q);
                        }
                    })
                    .catch(function () {
                        hideDropdown();
                    });
            }
        }

        input.addEventListener('input', function () {
            debouncedFetch();
        });

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

        document.addEventListener('click', function (e) {
            if (!wrapper.contains(e.target)) {
                hideDropdown();
            }
        });
    }

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

