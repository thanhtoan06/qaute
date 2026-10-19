(function (window) {
    'use strict';

    window.Qaute = window.Qaute || {};

    window.Qaute.initRichText = function (selector, options) {
        options = options || {};
        const $ = window.jQuery;
        if (!$ || !selector) {
            console.warn('jQuery is required for Summernote richtext initialization.');
            return null;
        }

        const $el = $(selector);
        if (!$el.length) {
            return null;
        }

        if (typeof $el.summernote !== 'function') {
            console.warn('Summernote library is not loaded.');
            return null;
        }

        $el.summernote({
            height: options.height || 220,
            placeholder: options.placeholder || 'Nhập nội dung chi tiết tại đây...',
            toolbar: [
                ['font', ['bold', 'italic', 'underline', 'clear']],
                ['para', ['ul', 'ol']],
                ['insert', ['link']],
                ['view', ['codeview']]
            ],
            disableDragAndDrop: true,
            callbacks: {
                onImageUpload: function () {
                    if (window.Qaute && typeof window.Qaute.toast === 'function') {
                        window.Qaute.toast('Không cho phép chèn ảnh trực tiếp vào nội dung. Vui lòng đính kèm tệp ở bước 3.', 'warning');
                    }
                },
                onPaste: function (e) {
                    const clipboardData = (e.originalEvent || e).clipboardData;
                    if (clipboardData && clipboardData.items) {
                        for (let i = 0; i < clipboardData.items.length; i++) {
                            const item = clipboardData.items[i];
                            if (item.type && item.type.indexOf('image') !== -1) {
                                e.preventDefault();
                                if (window.Qaute && typeof window.Qaute.toast === 'function') {
                                    window.Qaute.toast('Không hỗ trợ dán hình ảnh trực tiếp. Vui lòng đính kèm tệp ở bước tiếp theo.', 'warning');
                                }
                                return;
                            }
                        }
                    }
                },
                onChange: function (contents) {
                    if (typeof options.onChange === 'function') {
                        options.onChange(contents);
                    }
                }
            }
        });

        return $el;
    };
})(window);

