// Khởi tạo và quản lý chức năng tải tệp dùng chung.
(function (window, document) {
    'use strict';

    window.Qaute = window.Qaute || {};

    const ALLOWED_EXTENSIONS = {
        'TICKET_ATTACHMENT': ['jpg', 'jpeg', 'png', 'webp', 'gif', 'pdf', 'docx', 'xlsx', 'pptx', 'txt'],
        'CHAT_ATTACHMENT': ['jpg', 'jpeg', 'png', 'webp', 'gif', 'pdf', 'docx', 'xlsx', 'pptx', 'txt'],
        'AVATAR': ['jpg', 'jpeg', 'png', 'webp', 'gif'],
        'FAQ_IMAGE': ['jpg', 'jpeg', 'png', 'webp', 'gif'],
        'ANNOUNCEMENT_COVER': ['jpg', 'jpeg', 'png', 'webp', 'gif']
    };

    const MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10MB

    // Mã hóa nội dung để hiển thị an toàn trong HTML.
    function escapeHtml(value) {
        if (window.Qaute && typeof window.Qaute.escapeHtml === 'function') {
            return window.Qaute.escapeHtml(value);
        }
        const element = document.createElement('div');
        element.textContent = value == null ? '' : String(value);
        return element.innerHTML;
    }

    // Kiểm tra tên tệp có phần mở rộng là ảnh hay không.
    function isImageName(name) {
        const ext = String(name || '').split('.').pop().toLowerCase();
        return ['jpg', 'jpeg', 'png', 'webp', 'gif'].includes(ext);
    }

    // Khởi tạo vùng tải tệp và trả về các thao tác quản lý tệp.
    window.Qaute.initUploader = function (container, purpose, maxCount) {
        const root = typeof container === 'string' ? document.querySelector(container) : container;
        if (!root) {
            console.warn('Uploader container not found.');
            return null;
        }

        purpose = purpose || 'TICKET_ATTACHMENT';
        maxCount = maxCount || 5;

        const allowedExts = ALLOWED_EXTENSIONS[purpose] || ALLOWED_EXTENSIONS['TICKET_ATTACHMENT'];

        if (!root.querySelector('.qa-uploader')) {
            root.innerHTML =
                '<div class="qa-uploader">' +
                    '<div class="card card-dashed text-center p-3 mb-2 uploader-dropzone" style="cursor: pointer; border-style: dashed; border-width: 2px;">' +
                        '<div class="py-2">' +
                            '<i class="bi bi-cloud-arrow-up fs-1 text-primary mb-2 d-block"></i>' +
                            '<div class="fw-semibold">Kéo thả tệp vào đây hoặc <span class="text-primary text-decoration-underline">chọn tệp từ máy tính</span></div>' +
                            '<small class="text-secondary d-block mt-1">' +
                                'Định dạng: ' + allowedExts.join(', ').toUpperCase() + ' (Tối đa ' + (MAX_FILE_SIZE_BYTES / (1024 * 1024)) + 'MB/tệp, tối đa ' + maxCount + ' tệp)' +
                            '</small>' +
                            '<button type="button" class="btn btn-primary btn-sm mt-2 uploader-pick-btn"><i class="bi bi-paperclip me-1"></i>Chọn tệp để tải lên</button>' +
                        '</div>' +
                        '<input type="file" multiple class="d-none uploader-file-input" accept="' + allowedExts.map(ext => '.' + ext).join(',') + '">' +
                    '</div>' +
                    '<div class="uploader-file-list d-flex flex-column gap-2"></div>' +
                    '<div class="uploader-hidden-inputs"></div>' +
                '</div>';
        }

        const dropzone = root.querySelector('.uploader-dropzone');
        const fileInput = root.querySelector('.uploader-file-input');
        const fileList = root.querySelector('.uploader-file-list');
        const hiddenInputs = root.querySelector('.uploader-hidden-inputs');
        if (!dropzone || !fileInput || !fileList || !hiddenInputs) {
            console.warn('Uploader markup is incomplete.');
            return null;
        }
        if (root.dataset.qauteUploader === '1' && root._qauteUploader) {
            return root._qauteUploader;
        }
        root.dataset.qauteUploader = '1';

        const state = {
            files: [], // { uid, name, size, assetId, status: 'uploading'|'success'|'error', progress: 0 }
            isUploading: false
        };

        // Chọn biểu tượng tương ứng với loại tệp.
        function getFileIcon(name) {
            const ext = name.split('.').pop().toLowerCase();
            if (['jpg', 'jpeg', 'png', 'webp', 'gif'].includes(ext)) {
                return 'bi-file-earmark-image text-info';
            }
            if (ext === 'pdf') {
                return 'bi-file-earmark-pdf text-danger';
            }
            if (['docx', 'txt'].includes(ext)) {
                return 'bi-file-earmark-word text-primary';
            }
            if (['xlsx'].includes(ext)) {
                return 'bi-file-earmark-excel text-success';
            }
            if (['pptx'].includes(ext)) {
                return 'bi-file-earmark-ppt text-warning';
            }
            return 'bi-file-earmark text-secondary';
        }

        // Định dạng dung lượng tệp để hiển thị.
        function formatSize(bytes) {
            if (bytes < 1024) return bytes + ' B';
            if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
            return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
        }

        const pickBtn = root.querySelector('.uploader-pick-btn');
        if (pickBtn) {
            // Mở hộp chọn tệp mà không kích hoạt sự kiện của vùng thả.
            pickBtn.addEventListener('click', function (e) {
                e.preventDefault();
                e.stopPropagation();
                fileInput.click();
            });
        }

        // Mở hộp chọn tệp khi người dùng nhấn vùng thả.
        dropzone.addEventListener('click', function (e) {
            if (e.target && e.target.closest && e.target.closest('.uploader-pick-btn')) {
                return;
            }
            fileInput.click();
        });

        // Đánh dấu vùng thả khi tệp được kéo qua.
        dropzone.addEventListener('dragover', function (e) {
            e.preventDefault();
            e.stopPropagation();
            dropzone.classList.add('bg-light');
        });

        // Gỡ đánh dấu khi tệp rời khỏi vùng thả.
        dropzone.addEventListener('dragleave', function (e) {
            e.preventDefault();
            e.stopPropagation();
            dropzone.classList.remove('bg-light');
        });

        // Nhận các tệp được thả vào vùng tải lên.
        dropzone.addEventListener('drop', function (e) {
            e.preventDefault();
            e.stopPropagation();
            dropzone.classList.remove('bg-light');
            if (e.dataTransfer && e.dataTransfer.files) {
                handleFiles(e.dataTransfer.files);
            }
        });

        // Xử lý lựa chọn tệp và đặt lại trường chọn.
        fileInput.addEventListener('change', function () {
            if (fileInput.files) {
                handleFiles(fileInput.files);
            }
            fileInput.value = '';
        });

        // Kiểm tra tệp và bắt đầu tải các tệp hợp lệ.
        function handleFiles(files) {
            const activeFiles = state.files.filter(f => f.status !== 'error');
            if (activeFiles.length + files.length > maxCount) {
                if (window.Qaute && typeof window.Qaute.toast === 'function') {
                    window.Qaute.toast('Bạn chỉ được đính kèm tối đa ' + maxCount + ' tệp.', 'danger');
                }
                return;
            }

            // Kiểm tra từng tệp trước khi đưa vào hàng đợi tải lên.
            Array.from(files).forEach(function (file) {
                const ext = file.name.split('.').pop().toLowerCase();
                const uid = 'file_' + Math.random().toString(36).substring(2, 10);

                if (!allowedExts.includes(ext)) {
                    renderErrorItem(file.name, 'Định dạng .' + ext + ' không được hỗ trợ');
                    return;
                }

                if (file.size > MAX_FILE_SIZE_BYTES) {
                    renderErrorItem(file.name, 'Dung lượng tệp vượt quá ' + (MAX_FILE_SIZE_BYTES / (1024 * 1024)) + 'MB');
                    return;
                }

                const item = {
                    uid: uid,
                    name: file.name,
                    size: file.size,
                    assetId: null,
                    status: 'uploading',
                    progress: 0
                };
                state.files.push(item);

                renderFileItem(item, file);
                uploadFile(file, item);
            });
        }

        // Hiển thị mục lỗi cho tệp không hợp lệ.
        function renderErrorItem(name, errorMsg) {
            const el = document.createElement('div');
            el.className = 'border rounded p-2 bg-danger-lt d-flex justify-content-between align-items-center small';
            el.innerHTML =
                '<div class="d-flex align-items-center text-truncate me-2">' +
                    '<i class="bi bi-exclamation-triangle-fill text-danger me-2"></i>' +
                    '<span class="fw-medium text-truncate me-2">' + escapeHtml(name) + '</span>' +
                    '<span class="text-danger">(' + escapeHtml(errorMsg) + ')</span>' +
                '</div>' +
                '<button type="button" class="btn btn-sm btn-link text-danger p-0 ms-auto" aria-label="Đóng">' +
                    '<i class="bi bi-x-lg"></i>' +
                '</button>';
            // Xóa mục lỗi khỏi danh sách.
            el.querySelector('button').addEventListener('click', function () {
                el.remove();
            });
            fileList.appendChild(el);
        }

        // Hiển thị thông tin và trạng thái của tệp đang tải.
        function renderFileItem(item, file) {
            const el = document.createElement('div');
            el.className = 'border rounded p-2 bg-white small position-relative file-item-' + item.uid;
            let thumbHtml = '<i class="bi ' + getFileIcon(item.name) + ' fs-2 me-2"></i>';
            if (file && isImageName(item.name)) {
                try {
                    item.previewUrl = URL.createObjectURL(file);
                    thumbHtml = '<img src="' + item.previewUrl + '" alt="" class="rounded border me-2 flex-shrink-0" style="width:56px;height:56px;object-fit:cover;">';
                } catch (err) {
                    item.previewUrl = null;
                }
            }
            el.innerHTML =
                '<div class="d-flex justify-content-between align-items-center mb-1">' +
                    '<div class="d-flex align-items-center text-truncate me-2">' +
                        thumbHtml +
                        '<div class="text-truncate">' +
                            '<div class="fw-medium text-truncate">' + escapeHtml(item.name) + '</div>' +
                            '<div class="text-secondary small">' + formatSize(item.size) + '</div>' +
                        '</div>' +
                    '</div>' +
                    '<div class="d-flex align-items-center gap-2">' +
                        '<span class="status-indicator-badge text-secondary small upload-status">Đang tải 0%</span>' +
                        '<button type="button" class="btn btn-sm btn-link text-danger p-0 btn-remove-file" title="Xóa tệp">' +
                            '<i class="bi bi-trash"></i>' +
                        '</button>' +
                    '</div>' +
                '</div>' +
                '<div class="progress progress-xs upload-progress-bar">' +
                    '<div class="progress-bar progress-bar-indeterminate bg-primary" style="width: 0%;"></div>' +
                '</div>';

            // Gỡ tệp khỏi danh sách khi người dùng nhấn xóa.
            el.querySelector('.btn-remove-file').addEventListener('click', function () {
                removeFile(item.uid);
            });

            fileList.appendChild(el);
        }

        // Gửi tệp lên máy chủ và cập nhật trạng thái tải.
        function uploadFile(file, item) {
            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            formData.append('file', file);
            formData.append('purpose', purpose);

            item.xhr = xhr;
            updateStateUploading();

            // Cập nhật tiến độ tải lên trên giao diện.
            xhr.upload.addEventListener('progress', function (e) {
                if (e.lengthComputable) {
                    const percent = Math.round((e.loaded / e.total) * 100);
                    item.progress = percent;
                    const el = fileList.querySelector('.file-item-' + item.uid);
                    if (el) {
                        const bar = el.querySelector('.upload-progress-bar .progress-bar');
                        const status = el.querySelector('.upload-status');
                        if (bar) {
                            bar.classList.remove('progress-bar-indeterminate');
                            bar.style.width = percent + '%';
                        }
                        if (status) {
                            status.textContent = 'Đang tải ' + percent + '%';
                        }
                    }
                }
            });

            // Xử lý kết quả trả về sau khi tải lên.
            xhr.addEventListener('load', function () {
                const el = fileList.querySelector('.file-item-' + item.uid);
                if (xhr.status >= 200 && xhr.status < 300) {
                    try {
                        const res = JSON.parse(xhr.responseText);
                        if (res.success && res.data && res.data.id) {
                            item.assetId = res.data.id;
                            item.status = 'success';
                            if (el) {
                                const bar = el.querySelector('.upload-progress-bar');
                                const status = el.querySelector('.upload-status');
                                const previewImg = el.querySelector('img');
                                if (bar) bar.remove();
                                if (status) {
                                    status.innerHTML = '<i class="bi bi-check-circle-fill text-success me-1"></i>Hoàn tất';
                                    status.className = 'text-success small';
                                }
                                if (previewImg && res.data.publicUrl) {
                                    previewImg.src = res.data.publicUrl;
                                }
                            }
                            // Add hidden input
                            const hidden = document.createElement('input');
                            hidden.type = 'hidden';
                            hidden.name = 'assetIds';
                            hidden.value = item.assetId;
                            hidden.id = 'input-asset-' + item.uid;
                            hiddenInputs.appendChild(hidden);
                        } else {
                            throw new Error(res.message || 'Lỗi lưu tệp');
                        }
                    } catch (err) {
                        handleUploadError(item, err.message || 'Lỗi phản hồi máy chủ');
                    }
                } else {
                    let errMsg = 'Tải tệp thất bại (' + xhr.status + ')';
                    try {
                        const res = JSON.parse(xhr.responseText);
                        if (res.message) errMsg = res.message;
                    } catch (e) {}
                    handleUploadError(item, errMsg);
                }
                updateStateUploading();
            });

            // Hiển thị lỗi khi kết nối tải tệp thất bại.
            xhr.addEventListener('error', function () {
                handleUploadError(item, 'Lỗi kết nối mạng khi tải tệp');
                updateStateUploading();
            });

            xhr.open('POST', root.dataset.uploadUrl || '/api/media/upload');
            xhr.withCredentials = true;
            const formToken = root.closest('form')?.querySelector('input[name="_csrf"]')?.value;
            const token = window.Qaute && typeof window.Qaute.csrfToken === 'function'
                ? window.Qaute.csrfToken() || formToken
                : formToken;
            if (token) {
                const header = window.Qaute && typeof window.Qaute.csrfHeader === 'function'
                    ? window.Qaute.csrfHeader()
                    : document.querySelector('meta[name="_csrf_header"]')?.content || 'X-XSRF-TOKEN';
                xhr.setRequestHeader(header, token);
                formData.append('_csrf', token);
            }
            xhr.send(formData);
        }

        // Đánh dấu mục tệp khi quá trình tải lên gặp lỗi.
        function handleUploadError(item, message) {
            item.status = 'error';
            const el = fileList.querySelector('.file-item-' + item.uid);
            if (el) {
                el.className = 'border border-danger rounded p-2 bg-danger-lt small position-relative file-item-' + item.uid;
                const bar = el.querySelector('.upload-progress-bar');
                const status = el.querySelector('.upload-status');
                if (bar) bar.remove();
                if (status) {
                    status.innerHTML = '<i class="bi bi-exclamation-circle-fill text-danger me-1"></i>' + escapeHtml(message);
                    status.className = 'text-danger small';
                }
            }
        }

        // Hủy tải lên và xóa tệp khỏi giao diện cùng trạng thái.
        function removeFile(uid) {
            const index = state.files.findIndex(f => f.uid === uid);
            if (index !== -1) {
                const item = state.files[index];
                if (item.xhr && item.status === 'uploading') {
                    item.xhr.abort();
                }
                if (item.previewUrl) {
                    try { URL.revokeObjectURL(item.previewUrl); } catch (e) {}
                }
                state.files.splice(index, 1);
            }
            const el = fileList.querySelector('.file-item-' + uid);
            if (el) el.remove();
            const hidden = hiddenInputs.querySelector('#input-asset-' + uid);
            if (hidden) hidden.remove();
            updateStateUploading();
        }

        // Đồng bộ cờ tải lên với trạng thái các tệp.
        function updateStateUploading() {
            state.isUploading = state.files.some(f => f.status === 'uploading');
        }

        const api = {
            // Trả về mã tài sản của các tệp tải thành công.
            getAssetIds: function () {
                return state.files
                    .filter(f => f.status === 'success' && f.assetId)
                    .map(f => f.assetId);
            },
            // Trả về tên tệp đang tải hoặc đã tải thành công.
            getFileNames: function () {
                return state.files
                    .filter(f => f.status === 'success' || f.status === 'uploading')
                    .map(f => f.name);
            },
            // Trả về dữ liệu xem trước của các tệp chưa lỗi.
            getPreviewItems: function () {
                return state.files
                    .filter(f => f.status !== 'error')
                    // Chọn các trường dùng để hiển thị bản xem trước.
                    .map(function (f) {
                        return { name: f.name, previewUrl: f.previewUrl || '', status: f.status };
                    });
            },
            // Đếm các tệp tải lên thành công.
            getFilesCount: function () {
                return state.files.filter(f => f.status === 'success').length;
            },
            // Cho biết còn tệp nào đang tải lên hay không.
            isUploading: function () {
                return state.isUploading;
            }
        };
        root._qauteUploader = api;
        return api;
    };
})(window, document);
