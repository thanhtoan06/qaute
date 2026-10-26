(function (window, document) {
    'use strict';

    const DRAFT_KEY = 'qaute_draft_ticket';

    let currentStep = 1;
    let categoryTree = [];
    let fileUploader = null;
    let summernoteEditor = null;

    // Khởi tạo các phần của biểu mẫu tạo yêu cầu hỗ trợ.
    function init() {
        try { initCategoryTree(); } catch (e) { console.error('initCategoryTree failed', e); }
        try { initRichText(); } catch (e) { console.error('initRichText failed', e); }
        try { initFileUploader(); } catch (e) { console.error('initFileUploader failed', e); }
        try { initWizardNavigation(); } catch (e) { console.error('initWizardNavigation failed', e); }
        try { initFaqSuggestions(); } catch (e) { console.error('initFaqSuggestions failed', e); }
        try { initCharCounters(); } catch (e) { console.error('initCharCounters failed', e); }
        try { initUrgentToggle(); } catch (e) { console.error('initUrgentToggle failed', e); }
        try { initDraftStorage(); } catch (e) { console.error('initDraftStorage failed', e); }
        try { initFormSubmit(); } catch (e) { console.error('initFormSubmit failed', e); }
    }

    // --- 1. Category Tree & Cascade Select ---
    // Nạp cây chuyên mục và thiết lập các ô chọn liên tiếp.
    function initCategoryTree() {
        const treeEl = document.getElementById('categoryTreeData');
        if (!treeEl) return;

        try {
            const raw = treeEl.getAttribute('data-tree');
            categoryTree = raw ? JSON.parse(raw) : [];
        } catch (e) {
            console.error('Failed to parse category tree', e);
            categoryTree = [];
        }

        const deptSelect = document.getElementById('selectDepartment');
        const cat1Select = document.getElementById('selectCategoryLevel1');
        const cat2Select = document.getElementById('selectCategoryLevel2');
        const categoryIdInput = document.getElementById('categoryId');

        if (!deptSelect || !cat1Select || !cat2Select) return;

        // Populate departments
        deptSelect.innerHTML = '<option value="">-- Chọn phòng ban phụ trách --</option>';
        categoryTree.forEach(function (dept) {
            const opt = document.createElement('option');
            opt.value = dept.id;
            opt.textContent = dept.name;
            deptSelect.appendChild(opt);
        });

        // Cập nhật danh sách chuyên mục theo phòng ban đã chọn.
        deptSelect.addEventListener('change', function () {
            const deptId = Number(deptSelect.value);
            cat1Select.innerHTML = '<option value="">-- Chọn chuyên mục --</option>';
            cat2Select.innerHTML = '<option value="">-- Chọn chủ đề chi tiết --</option>';
            cat2Select.disabled = true;
            categoryIdInput.value = '';

            if (!deptId) {
                cat1Select.disabled = true;
                return;
            }

            const dept = categoryTree.find(d => d.id === deptId);
            if (dept && dept.categories && dept.categories.length > 0) {
                cat1Select.disabled = false;
                dept.categories.forEach(function (c1) {
                    const opt = document.createElement('option');
                    opt.value = c1.id;
                    opt.textContent = c1.name;
                    cat1Select.appendChild(opt);
                });
            } else {
                cat1Select.disabled = true;
            }
        });

        // Cập nhật chủ đề chi tiết theo chuyên mục đã chọn.
        cat1Select.addEventListener('change', function () {
            const deptId = Number(deptSelect.value);
            const c1Id = Number(cat1Select.value);
            cat2Select.innerHTML = '<option value="">-- Chọn chủ đề chi tiết --</option>';
            categoryIdInput.value = '';

            if (!c1Id) {
                cat2Select.disabled = true;
                return;
            }

            const dept = categoryTree.find(d => d.id === deptId);
            const c1 = dept && dept.categories ? dept.categories.find(c => c.id === c1Id) : null;

            if (c1 && c1.children && c1.children.length > 0) {
                cat2Select.disabled = false;
                c1.children.forEach(function (c2) {
                    const opt = document.createElement('option');
                    opt.value = c2.id;
                    opt.textContent = c2.name;
                    cat2Select.appendChild(opt);
                });
            } else {
                cat2Select.disabled = true;
                // Category level 1 is leaf
                categoryIdInput.value = String(c1Id);
            }
        });

        // Ghi nhận mã chủ đề chi tiết đã chọn.
        cat2Select.addEventListener('change', function () {
            const c2Id = Number(cat2Select.value);
            if (c2Id) {
                categoryIdInput.value = String(c2Id);
            } else {
                categoryIdInput.value = '';
            }
        });

        // Pre-selection if categoryIdInput has a value
        const initialCategoryId = Number(categoryIdInput.value);
        if (initialCategoryId) {
            preselectCategory(initialCategoryId);
        }
    }

    // Chọn lại phòng ban và chuyên mục theo mã có sẵn.
    function preselectCategory(targetId) {
        const deptSelect = document.getElementById('selectDepartment');
        const cat1Select = document.getElementById('selectCategoryLevel1');
        const cat2Select = document.getElementById('selectCategoryLevel2');

        for (const dept of categoryTree) {
            if (!dept.categories) continue;
            for (const c1 of dept.categories) {
                if (c1.id === targetId && (!c1.children || c1.children.length === 0)) {
                    deptSelect.value = dept.id;
                    deptSelect.dispatchEvent(new Event('change'));
                    cat1Select.value = c1.id;
                    cat1Select.dispatchEvent(new Event('change'));
                    return;
                }
                if (c1.children) {
                    for (const c2 of c1.children) {
                        if (c2.id === targetId) {
                            deptSelect.value = dept.id;
                            deptSelect.dispatchEvent(new Event('change'));
                            cat1Select.value = c1.id;
                            cat1Select.dispatchEvent(new Event('change'));
                            cat2Select.value = c2.id;
                            cat2Select.dispatchEvent(new Event('change'));
                            return;
                        }
                    }
                }
            }
        }
    }

    // Tạo tên đường dẫn chuyên mục đang được chọn.
    function getSelectedCategoryPath() {
        const deptSelect = document.getElementById('selectDepartment');
        const cat1Select = document.getElementById('selectCategoryLevel1');
        const cat2Select = document.getElementById('selectCategoryLevel2');

        const parts = [];
        if (deptSelect && deptSelect.selectedIndex > 0) {
            parts.push(deptSelect.options[deptSelect.selectedIndex].text);
        }
        if (cat1Select && cat1Select.selectedIndex > 0) {
            parts.push(cat1Select.options[cat1Select.selectedIndex].text);
        }
        if (cat2Select && !cat2Select.disabled && cat2Select.selectedIndex > 0) {
            parts.push(cat2Select.options[cat2Select.selectedIndex].text);
        }
        return parts.join(' › ');
    }

    // --- 2. Rich Text & Char Counters ---
    // Khởi tạo trình soạn thảo và cập nhật bộ đếm nội dung.
    function initRichText() {
        if (window.Qaute && typeof window.Qaute.initRichText === 'function') {
            summernoteEditor = window.Qaute.initRichText('#descriptionHtml', {
                height: 220,
                placeholder: 'Mô tả chi tiết câu hỏi hoặc vướng mắc của bạn (tối thiểu 30 ký tự)...',
                // Cập nhật bộ đếm khi nội dung trình soạn thảo thay đổi.
                onChange: function () {
                    updateDescCount();
                }
            });
        }
    }

    // Khởi tạo bộ đếm ký tự cho tiêu đề và nội dung.
    function initCharCounters() {
        const titleInput = document.getElementById('ticketTitle');
        if (titleInput) {
            // Cập nhật bộ đếm tiêu đề khi người dùng nhập.
            titleInput.addEventListener('input', function () {
                const count = titleInput.value.length;
                const counter = document.getElementById('titleCharCount');
                if (counter) counter.textContent = count;
            });
            // Initial count
            const counter = document.getElementById('titleCharCount');
            if (counter) counter.textContent = titleInput.value.length;
        }
        updateDescCount();
    }

    // Đếm ký tự nội dung mô tả hiện tại.
    function updateDescCount() {
        const descArea = document.getElementById('descriptionHtml');
        const counter = document.getElementById('descCharCount');
        if (!descArea || !counter) return;

        let text = '';
        if (window.jQuery && summernoteEditor) {
            const html = window.jQuery(descArea).summernote('code');
            const tmp = document.createElement('div');
            tmp.innerHTML = html;
            text = (tmp.textContent || tmp.innerText || '').trim();
        } else {
            text = descArea.value.trim();
        }
        counter.textContent = text.length;
    }

    // --- 3. File Uploader ---
    // Khởi tạo bộ tải tệp và thử lại nếu thư viện chưa sẵn sàng.
    function initFileUploader() {
        if (window.Qaute && typeof window.Qaute.initUploader === 'function') {
            fileUploader = window.Qaute.initUploader('#fileUploaderContainer', 'TICKET_ATTACHMENT', 5);
            return;
        }
        console.warn('File uploader is not available; retrying after scripts load.');
        // Thử khởi tạo lại bộ tải sau khi các tập lệnh khác tải xong.
        window.setTimeout(function () {
            if (!fileUploader && window.Qaute && typeof window.Qaute.initUploader === 'function') {
                fileUploader = window.Qaute.initUploader('#fileUploaderContainer', 'TICKET_ATTACHMENT', 5);
            }
        }, 0);
    }

    // --- 4. Urgent Toggle ---
    // Thiết lập hiển thị lý do khi yêu cầu xử lý gấp.
    function initUrgentToggle() {
        const checkbox = document.getElementById('urgentRequested');
        const container = document.getElementById('urgentReasonContainer');
        const reasonInput = document.getElementById('urgentReason');
        if (!checkbox || !container) return;

        // Đồng bộ trường lý do với trạng thái chọn xử lý gấp.
        function update() {
            if (checkbox.checked) {
                container.classList.remove('d-none');
                if (reasonInput) reasonInput.setAttribute('required', 'required');
            } else {
                container.classList.add('d-none');
                if (reasonInput) reasonInput.removeAttribute('required');
            }
        }

        checkbox.addEventListener('change', update);
        update();
    }

    // --- 5. FAQ Suggestions ---
    // Thiết lập gợi ý FAQ dựa trên tiêu đề yêu cầu.
    function initFaqSuggestions() {
        const titleInput = document.getElementById('ticketTitle');
        const box = document.getElementById('faqSuggestionsBox');
        const list = document.getElementById('faqSuggestionsList');
        if (!titleInput || !box || !list) return;

        // Dùng bộ trì hoãn dự phòng nếu tiện ích debounce chưa sẵn sàng.
        const debouncedFetch = window.Qaute && typeof window.Qaute.debounce === 'function'
            ? window.Qaute.debounce(fetchFaqSuggestions, 400)
            : function () { setTimeout(fetchFaqSuggestions, 400); };

        // Tải gợi ý khi tiêu đề thay đổi.
        titleInput.addEventListener('input', function () {
            debouncedFetch(titleInput.value.trim());
        });

        // Tải và hiển thị FAQ phù hợp với truy vấn.
        function fetchFaqSuggestions(q) {
            if (!q || q.length < 3) {
                box.classList.add('d-none');
                list.innerHTML = '';
                return;
            }

            fetch('/api/faqs/suggestions?q=' + encodeURIComponent(q) + '&source=TICKET_FORM')
                .then(function (res) { return res.json(); })
                // Vẽ danh sách gợi ý hoặc ẩn vùng kết quả trống.
                .then(function (data) {
                    if (data && data.success && Array.isArray(data.data) && data.data.length > 0) {
                        const items = data.data.slice(0, 5);
                        list.innerHTML = '';
                        // Tạo liên kết cho từng FAQ phù hợp.
                        items.forEach(function (faq) {
                            const a = document.createElement('a');
                            a.href = '/faq/' + encodeURIComponent(faq.slug);
                            a.target = '_blank';
                            a.rel = 'noopener noreferrer';
                            a.className = 'list-group-item list-group-item-action py-2 px-3 d-flex justify-content-between align-items-center';
                            a.innerHTML =
                                '<div class="me-2 text-truncate">' +
                                    '<div class="fw-medium text-truncate">' + window.Qaute.escapeHtml(faq.title) + '</div>' +
                                    (faq.categoryName ? '<small class="text-secondary">' + window.Qaute.escapeHtml(faq.categoryName) + '</small>' : '') +
                                '</div>' +
                                '<i class="bi bi-box-arrow-up-right text-muted small"></i>';
                            list.appendChild(a);
                        });
                        box.classList.remove('d-none');
                    } else {
                        box.classList.add('d-none');
                        list.innerHTML = '';
                    }
                })
                // Ẩn gợi ý nếu không tải được kết quả.
                .catch(function () {
                    box.classList.add('d-none');
                });
        }
    }

    // --- 6. Wizard Navigation ---
    // Gắn thao tác chuyển bước và điều hướng qua các thẻ.
    function initWizardNavigation() {
        // Kiểm tra bước hiện tại trước khi chuyển tiếp.
        document.querySelectorAll('.btn-next').forEach(function (btn) {
            btn.addEventListener('click', function () {
                const nextStep = currentStep + 1;
                if (validateStep(currentStep)) {
                    goToStep(nextStep);
                }
            });
        });

        // Quay về bước trước khi được yêu cầu.
        document.querySelectorAll('.btn-prev').forEach(function (btn) {
            btn.addEventListener('click', function () {
                const prevStep = currentStep - 1;
                if (prevStep >= 1) {
                    goToStep(prevStep);
                }
            });
        });

        // Step tabs click
        // Cho phép chuyển đến các bước đã hoàn tất hoặc hợp lệ.
        document.querySelectorAll('.step-tab-btn').forEach(function (tab) {
            tab.addEventListener('click', function () {
                const target = Number(tab.getAttribute('data-step'));
                if (target < currentStep) {
                    goToStep(target);
                } else if (target > currentStep) {
                    if (validateStep(currentStep)) {
                        goToStep(target);
                    }
                }
            });
        });
    }

    // Kiểm tra dữ liệu bắt buộc của bước hiện tại.
    function validateStep(step) {
        hideStepErrors();

        if (step === 1) {
            const categoryId = document.getElementById('categoryId').value;
            if (!categoryId) {
                showStepError(1, 'Vui lòng chọn phòng ban và chuyên mục/chủ đề phù hợp với thắc mắc của bạn.');
                return false;
            }
            return true;
        }

        if (step === 2) {
            const titleInput = document.getElementById('ticketTitle');
            const title = titleInput ? titleInput.value.trim() : '';
            if (title.length < 10 || title.length > 150) {
                showStepError(2, 'Tiêu đề yêu cầu phải có độ dài từ 10 đến 150 ký tự.');
                if (titleInput) titleInput.focus();
                return false;
            }

            const descArea = document.getElementById('descriptionHtml');
            let text = '';
            if (window.jQuery && summernoteEditor) {
                const html = window.jQuery(descArea).summernote('code');
                const tmp = document.createElement('div');
                tmp.innerHTML = html;
                text = (tmp.textContent || tmp.innerText || '').trim();
            } else {
                text = descArea ? descArea.value.trim() : '';
            }

            if (text.length < 30) {
                showStepError(2, 'Nội dung chi tiết phải có ít nhất 30 ký tự để tư vấn viên có đủ thông tin hỗ trợ.');
                return false;
            }
            return true;
        }

        if (step === 3) {
            if (fileUploader && typeof fileUploader.isUploading === 'function' && fileUploader.isUploading()) {
                showStepError(3, 'Tệp đính kèm đang được tải lên, vui lòng đợi trong giây lát.');
                return false;
            }

            const urgentCheck = document.getElementById('urgentRequested');
            if (urgentCheck && urgentCheck.checked) {
                const reasonInput = document.getElementById('urgentReason');
                const reason = reasonInput ? reasonInput.value.trim() : '';
                if (reason.length < 10 || reason.length > 500) {
                    showStepError(3, 'Khi đề nghị xử lý gấp, bạn cần cung cấp lý do chính đáng từ 10 đến 500 ký tự.');
                    if (reasonInput) reasonInput.focus();
                    return false;
                }
            }
            return true;
        }

        return true;
    }

    // Hiển thị bước biểu mẫu và cập nhật các chỉ báo tiến độ.
    function goToStep(step) {
        currentStep = Math.max(1, Math.min(4, step));

        // Update step panels
        document.querySelectorAll('.wizard-step-pane').forEach(function (pane) {
            const s = Number(pane.getAttribute('data-step'));
            if (s === currentStep) {
                pane.classList.remove('d-none');
            } else {
                pane.classList.add('d-none');
            }
        });

        // Update step tabs/indicators
        document.querySelectorAll('.wizard-step-item').forEach(function (item) {
            const s = Number(item.getAttribute('data-step'));
            item.classList.remove('active', 'completed');
            if (s === currentStep) {
                item.classList.add('active');
            } else if (s < currentStep) {
                item.classList.add('completed');
            }
        });

        // Render summary when on step 4
        if (currentStep === 4) {
            renderReviewSummary();
        }

        // Scroll to top of wizard
        const wizard = document.getElementById('ticketWizardContainer');
        if (wizard) {
            wizard.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }

    // Hiển thị lỗi xác thực cho bước được chỉ định.
    function showStepError(step, message) {
        const errorEl = document.getElementById('stepError' + step);
        if (errorEl) {
            errorEl.textContent = message;
            errorEl.classList.remove('d-none');
        } else if (window.Qaute && typeof window.Qaute.toast === 'function') {
            window.Qaute.toast(message, 'warning');
        }
    }

    // Xóa lỗi đang hiển thị trên các bước của biểu mẫu.
    function hideStepErrors() {
        for (let i = 1; i <= 4; i++) {
            const errorEl = document.getElementById('stepError' + i);
            if (errorEl) {
                errorEl.textContent = '';
                errorEl.classList.add('d-none');
            }
        }
    }

    // Hiển thị bản tóm tắt yêu cầu trước khi gửi.
    function renderReviewSummary() {
        const reviewCategory = document.getElementById('reviewCategory');
        const reviewTitle = document.getElementById('reviewTitle');
        const reviewContent = document.getElementById('reviewContent');
        const reviewFiles = document.getElementById('reviewFiles');
        const reviewUrgent = document.getElementById('reviewUrgent');
        const reviewFaq = document.getElementById('reviewFaq');

        if (reviewCategory) {
            reviewCategory.textContent = getSelectedCategoryPath() || '(Chưa chọn)';
        }

        const titleInput = document.getElementById('ticketTitle');
        if (reviewTitle && titleInput) {
            reviewTitle.textContent = titleInput.value.trim();
        }

        const descArea = document.getElementById('descriptionHtml');
        if (reviewContent && descArea) {
            if (window.jQuery && summernoteEditor) {
                reviewContent.innerHTML = window.jQuery(descArea).summernote('code');
            } else {
                reviewContent.innerHTML = descArea.value;
            }
        }

        if (reviewFiles) {
            const items = fileUploader && typeof fileUploader.getPreviewItems === 'function'
                ? fileUploader.getPreviewItems()
                : [];
            if (items.length > 0) {
                reviewFiles.innerHTML = '<div class="d-flex flex-wrap gap-2">' +
                    // Tạo nội dung xem trước cho từng tệp đính kèm.
                    items.map(function (item) {
                        const name = window.Qaute && typeof window.Qaute.escapeHtml === 'function'
                            ? window.Qaute.escapeHtml(item.name)
                            : String(item.name || '');
                        if (item.previewUrl) {
                            return '<div class="text-center" style="width:72px;">' +
                                '<img src="' + item.previewUrl + '" alt="' + name + '" class="rounded border" style="width:72px;height:72px;object-fit:cover;">' +
                                '<div class="small text-truncate">' + name + '</div></div>';
                        }
                        return '<span class="badge bg-secondary-lt text-secondary">' + name + '</span>';
                    }).join('') +
                    '</div>';
            } else {
                reviewFiles.textContent = 'Không có tệp đính kèm';
            }
        }

        const urgentCheck = document.getElementById('urgentRequested');
        const urgentReason = document.getElementById('urgentReason');
        if (reviewUrgent) {
            if (urgentCheck && urgentCheck.checked) {
                reviewUrgent.innerHTML = '<span class="badge bg-danger text-white me-1">Yêu cầu xử lý gấp</span> ' +
                    '<span class="text-secondary small">' + window.Qaute.escapeHtml(urgentReason ? urgentReason.value.trim() : '') + '</span>';
            } else {
                reviewUrgent.textContent = 'Mức độ bình thường';
            }
        }

        const allowFaqCheck = document.getElementById('allowPublicFaq');
        if (reviewFaq) {
            if (allowFaqCheck && allowFaqCheck.checked) {
                reviewFaq.innerHTML = '<span class="text-success"><i class="bi bi-check-circle me-1"></i>Đồng ý biên tập thành FAQ ẩn danh</span>';
            } else {
                reviewFaq.innerHTML = '<span class="text-secondary"><i class="bi bi-x-circle me-1"></i>Không chia sẻ làm FAQ</span>';
            }
        }
    }

    // --- 7. LocalStorage Draft Save/Restore ---
    // Lưu tự động và khôi phục bản nháp của biểu mẫu.
    function initDraftStorage() {
        const titleInput = document.getElementById('ticketTitle');
        const descArea = document.getElementById('descriptionHtml');
        const urgentCheck = document.getElementById('urgentRequested');
        const urgentReason = document.getElementById('urgentReason');
        const allowFaqCheck = document.getElementById('allowPublicFaq');

        // Ghi nội dung biểu mẫu hiện tại vào bộ nhớ cục bộ.
        function saveDraft() {
            try {
                let content = '';
                if (window.jQuery && summernoteEditor && descArea) {
                    content = window.jQuery(descArea).summernote('code');
                } else if (descArea) {
                    content = descArea.value;
                }

                const draft = {
                    categoryId: document.getElementById('categoryId') ? document.getElementById('categoryId').value : '',
                    title: titleInput ? titleInput.value : '',
                    content: content,
                    urgentRequested: urgentCheck ? urgentCheck.checked : false,
                    urgentReason: urgentReason ? urgentReason.value : '',
                    allowPublicFaq: allowFaqCheck ? allowFaqCheck.checked : true,
                    updatedAt: Date.now()
                };
                localStorage.setItem(DRAFT_KEY, JSON.stringify(draft));
            } catch (e) {
                // Ignore storage quota
            }
        }

        // Auto-save on change
        if (titleInput) titleInput.addEventListener('input', saveDraft);
        if (urgentCheck) urgentCheck.addEventListener('change', saveDraft);
        if (urgentReason) urgentReason.addEventListener('input', saveDraft);
        if (allowFaqCheck) allowFaqCheck.addEventListener('change', saveDraft);

        // Periodically check content change or save on next step
        document.querySelectorAll('.btn-next').forEach(btn => btn.addEventListener('click', saveDraft));

        // Restore draft if present and form is empty
        try {
            const rawDraft = localStorage.getItem(DRAFT_KEY);
            if (rawDraft && titleInput && !titleInput.value) {
                const draft = JSON.parse(rawDraft);
                if (draft && (draft.title || draft.content)) {
                    titleInput.value = draft.title || '';
                    if (draft.content && descArea) {
                        descArea.value = draft.content;
                        if (window.jQuery && summernoteEditor) {
                            window.jQuery(descArea).summernote('code', draft.content);
                        }
                    }
                    if (draft.urgentRequested && urgentCheck) {
                        urgentCheck.checked = true;
                        urgentCheck.dispatchEvent(new Event('change'));
                        if (draft.urgentReason && urgentReason) {
                            urgentReason.value = draft.urgentReason;
                        }
                    }
                    if (draft.categoryId && !document.getElementById('categoryId').value) {
                        document.getElementById('categoryId').value = draft.categoryId;
                        preselectCategory(Number(draft.categoryId));
                    }
                    updateDescCount();
                    if (titleInput.value) {
                        document.getElementById('titleCharCount').textContent = titleInput.value.length;
                    }
                    if (window.Qaute && typeof window.Qaute.toast === 'function') {
                        window.Qaute.toast('Đã khôi phục bản nháp chưa gửi của bạn.', 'info');
                    }
                }
            }
        } catch (e) {
            console.error('Failed to restore draft', e);
        }
    }

    // --- 8. Form Submit Handling ---
    // Đồng bộ nội dung và khóa nút khi gửi biểu mẫu.
    function initFormSubmit() {
        const form = document.getElementById('ticketCreateForm');
        const submitBtn = document.getElementById('submitTicketBtn');
        if (!form || !submitBtn) return;

        // Chuẩn bị nội dung và trạng thái trước khi gửi yêu cầu.
        form.addEventListener('submit', function (e) {
            // Sync summernote code to textarea
            const descArea = document.getElementById('descriptionHtml');
            if (window.jQuery && summernoteEditor && descArea) {
                descArea.value = window.jQuery(descArea).summernote('code');
            }

            // Lock submit button
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Đang gửi yêu cầu...';

            // Clear draft
            try {
                localStorage.removeItem(DRAFT_KEY);
            } catch (err) {}
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})(window, document);

