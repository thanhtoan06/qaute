(function (window, document) {
    'use strict';

    // Khởi tạo quy trình chọn và tải ảnh đại diện.
    function init() {
        const btnChangeAvatar = document.getElementById('btnChangeAvatar');
        const fileInput = document.getElementById('avatarFileInput');
        const avatarPreview = document.getElementById('avatarPreview');
        const avatarPreviewPlaceholder = document.getElementById('avatarPreviewPlaceholder');
        const avatarSpinner = document.getElementById('avatarSpinner');
        const avatarError = document.getElementById('avatarError');
        const avatarForm = document.getElementById('avatarForm');
        const avatarAssetIdInput = document.getElementById('avatarAssetIdInput');

        if (!btnChangeAvatar || !fileInput) {
            return;
        }

        // Mở hộp thoại chọn tệp ảnh.
        btnChangeAvatar.addEventListener('click', function () {
            fileInput.click();
        });

        // Kiểm tra và xử lý ảnh vừa được chọn.
        fileInput.addEventListener('change', function (event) {
            const files = event.target.files;
            if (!files || files.length === 0) {
                return;
            }
            const file = files[0];

            if (!file.type.match(/^image\/(jpeg|png|webp|gif)$/i)) {
                showError('Vui lòng chọn tệp hình ảnh hợp lệ (JPG, PNG, WebP, GIF)');
                fileInput.value = '';
                return;
            }

            hideError();
            setLoading(true);

            const reader = new FileReader();
            // Tạo ảnh từ dữ liệu tệp để cắt và xem trước.
            reader.onload = function (e) {
                const img = new Image();
                // Cắt ảnh thành hình vuông và chuẩn bị tải lên.
                img.onload = function () {
                    try {
                        const canvas = document.createElement('canvas');
                        canvas.width = 400;
                        canvas.height = 400;
                        const ctx = canvas.getContext('2d');

                        const minSide = Math.min(img.width, img.height);
                        const sx = (img.width - minSide) / 2;
                        const sy = (img.height - minSide) / 2;

                        ctx.drawImage(img, sx, sy, minSide, minSide, 0, 0, 400, 400);

                        // Show preview immediately
                        const previewDataUrl = canvas.toDataURL('image/jpeg', 0.9);
                        if (avatarPreview) {
                            avatarPreview.src = previewDataUrl;
                            avatarPreview.classList.remove('d-none');
                        }
                        if (avatarPreviewPlaceholder) {
                            avatarPreviewPlaceholder.classList.add('d-none');
                        }

                        // Convert canvas to blob and upload
                        // Chuyển ảnh đã xử lý thành tệp để tải lên.
                        canvas.toBlob(function (blob) {
                            if (!blob) {
                                showError('Không thể xử lý hình ảnh');
                                setLoading(false);
                                return;
                            }
                            uploadAvatarBlob(blob);
                        }, 'image/jpeg', 0.9);
                    } catch (err) {
                        showError('Có lỗi xảy ra khi xử lý ảnh');
                        setLoading(false);
                    }
                };
                // Báo lỗi nếu trình duyệt không đọc được ảnh.
                img.onerror = function () {
                    showError('Không thể nạp tệp hình ảnh');
                    setLoading(false);
                };
                img.src = e.target.result;
            };
            // Báo lỗi nếu không thể đọc dữ liệu tệp.
            reader.onerror = function () {
                showError('Không thể đọc tệp hình ảnh');
                setLoading(false);
            };
            reader.readAsDataURL(file);
        });

        // Tải ảnh đã xử lý lên và gửi biểu mẫu khi thành công.
        function uploadAvatarBlob(blob) {
            const formData = new FormData();
            formData.append('file', blob, 'avatar.jpg');
            formData.append('purpose', 'AVATAR');

            const headers = {};
            if (window.Qaute && typeof window.Qaute.csrfToken === 'function') {
                const token = window.Qaute.csrfToken();
                if (token) {
                    headers[window.Qaute.csrfHeader()] = token;
                }
            }

            fetch('/api/media/upload', {
                method: 'POST',
                headers: headers,
                body: formData
            })
                // Kiểm tra phản hồi tải ảnh trước khi xử lý dữ liệu.
                .then(function (response) {
                    // Đọc nội dung JSON để kiểm tra lỗi từ máy chủ.
                    return response.json().then(function (data) {
                        if (!response.ok || !data.success) {
                            const message = (data && data.message) ? data.message : 'Tải ảnh lên thất bại';
                            throw new Error(message);
                        }
                        return data;
                    });
                })
                // Lưu mã tài nguyên ảnh và gửi biểu mẫu.
                .then(function (json) {
                    const asset = json.data;
                    if (asset && asset.id && avatarAssetIdInput && avatarForm) {
                        avatarAssetIdInput.value = asset.id;
                        avatarForm.submit();
                    } else {
                        throw new Error('Dữ liệu ảnh trả về không hợp lệ');
                    }
                })
                // Hiển thị lỗi tải ảnh và khôi phục trạng thái nhập.
                .catch(function (error) {
                    showError(error.message || 'Không thể tải ảnh đại diện lên, vui lòng thử lại');
                    if (window.Qaute && typeof window.Qaute.toast === 'function') {
                        window.Qaute.toast(error.message || 'Tải ảnh đại diện thất bại', 'danger');
                    }
                    setLoading(false);
                    fileInput.value = '';
                });
        }

        // Bật hoặc tắt trạng thái tải ảnh trên giao diện.
        function setLoading(loading) {
            if (btnChangeAvatar) {
                btnChangeAvatar.disabled = loading;
            }
            if (avatarSpinner) {
                if (loading) {
                    avatarSpinner.classList.remove('d-none');
                } else {
                    avatarSpinner.classList.add('d-none');
                }
            }
        }

        // Hiển thị thông báo lỗi ảnh.
        function showError(message) {
            if (avatarError) {
                avatarError.textContent = message;
                avatarError.classList.remove('d-none');
            }
        }

        // Xóa thông báo lỗi ảnh đang hiển thị.
        function hideError() {
            if (avatarError) {
                avatarError.textContent = '';
                avatarError.classList.add('d-none');
            }
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})(window, document);

