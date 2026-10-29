// Khởi tạo luồng chọn ngày và đổi khung giờ hẹn.
(function () {
    var root = document.querySelector('[data-qa-view="student-appointment-reschedule"]');
    if (!root) return;
    var ctx = root.dataset.contextPath;
    var catId = root.dataset.categoryId;
    var modality = root.dataset.modality;
    var currentSlotId = root.dataset.slotId;

    var dateInput = document.getElementById('dateInput');
    var managerSelect = document.getElementById('managerSelect');
    var slotPlaceholder = document.getElementById('slotPlaceholder');
    var slotLoading = document.getElementById('slotLoading');
    var slotEmpty = document.getElementById('slotEmpty');
    var slotGrid = document.getElementById('slotGrid');
    var newSlotIdInput = document.getElementById('newSlotId');
    var selectedSlotAlert = document.getElementById('selectedSlotAlert');
    var selectedSlotText = document.getElementById('selectedSlotText');
    var selectedAdvisorText = document.getElementById('selectedAdvisorText');
    var btnSubmit = document.getElementById('btnSubmitReschedule');
    var form = document.getElementById('rescheduleForm');

    // Min date = today
    var today = Qaute.todayDateValue();
    if (dateInput) {
        dateInput.min = today;
        dateInput.value = today;
    }

    // Tải các khung giờ còn trống theo ngày và tư vấn viên đã chọn.
    function loadSlots() {
        var date = dateInput ? dateInput.value : '';
        if (!date || !catId) return;

        var mgrId = managerSelect ? managerSelect.value : '';

        slotPlaceholder.style.display = 'none';
        slotLoading.style.display = 'block';
        slotEmpty.style.display = 'none';
        slotGrid.innerHTML = '';
        newSlotIdInput.value = '';
        btnSubmit.disabled = true;
        selectedSlotAlert.style.display = 'none';

        var url = ctx + '/api/appointments/slots?categoryId=' + encodeURIComponent(catId) + '&date=' + encodeURIComponent(date);
        if (mgrId) {
            url += '&managerId=' + encodeURIComponent(mgrId);
        }
        if (modality) {
            url += '&modality=' + encodeURIComponent(modality);
        }

        fetch(url)
            .then(
                // Chuyển phản hồi HTTP thành dữ liệu JSON.
                function (r) { return r.json(); }
            )
            .then(
                // Hiển thị các khung giờ phù hợp từ phản hồi.
                function (resp) {
                    slotLoading.style.display = 'none';
                    var slots = (resp.data || []).filter(
                        // Loại khung giờ hiện tại khỏi danh sách lựa chọn.
                        function (s) {
                            return String(s.slotId) !== String(currentSlotId);
                        }
                    );
                    if (slots.length === 0) {
                        slotEmpty.style.display = 'block';
                    } else {
                        renderSlots(slots, date);
                    }
                }
            )
            .catch(
                // Báo lỗi khi không tải được các khung giờ trống.
                function () {
                    slotLoading.style.display = 'none';
                    slotGrid.innerHTML = '<div class="col-12 text-danger small p-2">Không tải được khung giờ trống.</div>';
                }
            );
    }

    // Mã hóa ký tự HTML trước khi chèn nội dung vào giao diện.
    function escH(s) {
        return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }

    // Tạo nút lựa chọn cho các khung giờ còn trống.
    function renderSlots(slots, dateVal) {
        slotGrid.innerHTML = '';
        slots.forEach(
            // Hiển thị một nút cho từng khung giờ khả dụng.
            function (slot) {
                var timeLabel = Qaute.formatTime(slot.startAt) + ' – ' + Qaute.formatTime(slot.endAt);
                var mIcon = slot.modality === 'ONLINE' ? '🖥' : slot.modality === 'IN_PERSON' ? '🏫' : '🔄';
                var mLabel = slot.modality === 'ONLINE' ? 'Online' : slot.modality === 'IN_PERSON' ? 'Trực tiếp' : 'Linh hoạt';

                var col = document.createElement('div');
                col.className = 'col-6 col-sm-4 col-md-3';
                col.innerHTML = '<button type="button" class="btn btn-outline-primary slot-btn w-100 py-2"' +
                    ' data-sid="' + escH(String(slot.slotId)) + '"' +
                    ' data-slabel="' + escH(timeLabel) + '"' +
                    ' data-smanager="' + escH(slot.managerName || '') + '"' +
                    ' data-smodality="' + escH(slot.modality || '') + '">' +
                    '<div class="fw-semibold small">' + escH(timeLabel) + '</div>' +
                    '<div class="small text-muted">' + mIcon + ' ' + mLabel + '</div>' +
                    (slot.managerName ? '<div class="small text-muted text-truncate" style="max-width:130px">' + escH(slot.managerName) + '</div>' : '') +
                    '</button>';

                var btn = col.querySelector('.slot-btn');
                btn.addEventListener(
                    'click',
                    // Cập nhật trạng thái và thông tin của khung giờ được chọn.
                    function () {
                        document.querySelectorAll('.slot-btn').forEach(
                            // Xóa đánh dấu chọn trên từng nút khung giờ.
                            function (b) { b.classList.remove('selected'); }
                        );
                        this.classList.add('selected');

                        var sid = this.getAttribute('data-sid');
                        var slabel = this.getAttribute('data-slabel');
                        var smanager = this.getAttribute('data-smanager');

                        newSlotIdInput.value = sid;

                        var parts = dateVal.split('-');
                        var dateFormatted = parts.length === 3 ? (parts[2] + '/' + parts[1] + '/' + parts[0]) : dateVal;

                        selectedSlotText.textContent = 'Khung giờ mới: ' + slabel + ' (Ngày ' + dateFormatted + ')';
                        selectedAdvisorText.textContent = 'Tư vấn viên: ' + (smanager || 'Hệ thống tự phân công');
                        selectedSlotAlert.style.display = 'flex';
                        btnSubmit.disabled = false;
                    }
                );

                slotGrid.appendChild(col);
            }
        );
    }

    if (dateInput) {
        dateInput.addEventListener('change', loadSlots);
    }
    if (managerSelect) {
        managerSelect.addEventListener('change', loadSlots);
    }

    if (form) {
        form.addEventListener(
            'submit',
            // Kiểm tra lựa chọn và khóa nút trước khi gửi biểu mẫu.
            function (e) {
                if (!newSlotIdInput.value) {
                    e.preventDefault();
                    alert('Vui lòng chọn một khung giờ mới.');
                    return;
                }
                if (btnSubmit) {
                    btnSubmit.disabled = true;
                    btnSubmit.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang xử lý...';
                }
            }
        );
    }

    // Initial load
    loadSlots();
})();