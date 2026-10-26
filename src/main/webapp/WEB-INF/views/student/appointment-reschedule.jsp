<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Đổi lịch hẹn tư vấn</title>
<div class="qa-page-student-appointment-reschedule" data-qa-view="student-appointment-reschedule">
<style>
.qa-page-student-appointment-reschedule { --page-accent: #118c78; --page-accent-dark: #0b6e60; --page-tint: #e8f8f4; --page-line: #ccece3; min-width: 0; color: #344c67; }
.qa-page-student-appointment-reschedule > .page-header, .qa-page-student-appointment-reschedule > .qa-page-heading, .qa-page-student-appointment-reschedule > .d-flex:has(> .page-title), .qa-page-student-appointment-reschedule > .d-flex:has(> div > .page-title) { position: relative; isolation: isolate; display: flex; align-items: center; gap: 1rem; margin: 0 0 1.4rem !important; padding: clamp(1rem,2vw,1.45rem); overflow: hidden; border: 1px solid #dfe8f3; border-radius: 20px; background: radial-gradient(circle at 96% 0%, #e8f8f4, transparent 34%), linear-gradient(125deg,#fff,#f7faff); box-shadow: 0 9px 26px rgba(16,48,84,.06); }
.qa-page-student-appointment-reschedule > .page-header::after, .qa-page-student-appointment-reschedule > .qa-page-heading::after { content: ""; position: absolute; z-index: -1; right: -42px; bottom: -88px; width: 160px; height: 160px; border: 1px solid #ccece3; border-radius: 50%; box-shadow: 0 0 0 18px rgba(32,107,196,.014),0 0 0 40px rgba(32,107,196,.009); pointer-events: none; }
.qa-page-student-appointment-reschedule .qa-view-title { display: inline-flex; align-items: center; gap: .68rem; max-width: 100%; color: #193858; font-size: clamp(1.3rem,2vw,1.75rem); font-weight: 850; line-height: 1.3; letter-spacing: -.045em; }
.qa-page-student-appointment-reschedule .qa-view-title-icon { display: inline-flex; flex: 0 0 36px; width: 36px; height: 36px; align-items: center; justify-content: center; border: 1px solid #ccece3; border-radius: 12px; color: #0b6e60; background: linear-gradient(145deg,#fff,#e8f8f4); box-shadow: 0 6px 15px rgba(20,55,92,.08); }
.qa-page-student-appointment-reschedule .card { position: relative; border: 1px solid #e0e9f3; border-radius: 18px; background: rgba(255,255,255,.98); box-shadow: 0 7px 22px rgba(16,48,84,.05); transition: border-color .22s ease,box-shadow .22s ease,transform .22s ease; }
.qa-page-student-appointment-reschedule .card:hover { border-color: #ccece3; box-shadow: 0 13px 30px rgba(16,48,84,.085); }
.qa-page-student-appointment-reschedule .card-header { display: flex; align-items: center; gap: .65rem; min-height: 56px; border-bottom: 1px solid #eaf0f7; border-radius: 18px 18px 0 0; background: linear-gradient(115deg,#fff,#e8f8f4); }
.qa-page-student-appointment-reschedule .card-title { color: #294664; font-weight: 800; letter-spacing: -.015em; }
.qa-page-student-appointment-reschedule .card-body { color: #435b75; }
.qa-page-student-appointment-reschedule .card-footer { border-top: 1px solid #eaf0f7; background: linear-gradient(110deg,#fbfdff,#e8f8f4); }
.qa-page-student-appointment-reschedule .form-label { color: #405a76; font-size: .79rem; font-weight: 750; }
.qa-page-student-appointment-reschedule .form-control, .qa-page-student-appointment-reschedule .form-select { min-height: 42px; border-color: #d7e3f1; border-radius: 10px; color: #2c4562; background-color: #fff; box-shadow: 0 2px 5px rgba(20,50,84,.025); transition: border-color .2s ease,box-shadow .2s ease; }
.qa-page-student-appointment-reschedule textarea.form-control { min-height: 112px; line-height: 1.65; }
.qa-page-student-appointment-reschedule .form-control:focus, .qa-page-student-appointment-reschedule .form-select:focus { border-color: #118c78; box-shadow: 0 0 0 4px #e8f8f4; }
.qa-page-student-appointment-reschedule .btn { border-radius: 10px; font-weight: 650; transition: transform .18s ease,box-shadow .18s ease,background-color .18s ease; }
.qa-page-student-appointment-reschedule .btn:not(:disabled):not(.btn-close):hover { transform: translateY(-1px); }
.qa-page-student-appointment-reschedule .btn-primary { border-color: #118c78; background: linear-gradient(120deg,#118c78,#0b6e60); box-shadow: 0 5px 13px #ccece3; }
.qa-page-student-appointment-reschedule .btn-outline-primary { border-color: #ccece3; color: #0b6e60; background: #fff; }
.qa-page-student-appointment-reschedule .btn-outline-primary:hover { border-color: #118c78; color: #fff; background: linear-gradient(120deg,#118c78,#0b6e60); }
.qa-page-student-appointment-reschedule .qa-filter-bar { gap: .9rem; padding: clamp(.95rem,1.8vw,1.25rem); border: 1px solid #dfe8f3; border-radius: 16px; background: linear-gradient(135deg,#fff,#e8f8f4); box-shadow: 0 7px 20px rgba(16,48,84,.04); }
.qa-page-student-appointment-reschedule .table-responsive { border: 1px solid #e3ebf5; border-radius: 14px; background: #fff; }
.qa-page-student-appointment-reschedule .table { margin-bottom: 0; color: #405872; }
.qa-page-student-appointment-reschedule .table thead th { color: #607892; font-size: .72rem; font-weight: 800; letter-spacing: .045em; background: linear-gradient(110deg,#e8f8f4,#f8faff); border-bottom-color: #dfe8f3; }
.qa-page-student-appointment-reschedule .table tbody tr { transition: background-color .18s ease; }
.qa-page-student-appointment-reschedule .table tbody tr:hover { background: #e8f8f4; }
.qa-page-student-appointment-reschedule .alert { border-radius: 13px; box-shadow: 0 5px 15px rgba(16,48,84,.04); }
.qa-page-student-appointment-reschedule .nav-tabs { gap: .25rem; border-bottom-color: #dce7f3; }
.qa-page-student-appointment-reschedule .nav-tabs .nav-link { border-radius: 10px 10px 0 0; font-weight: 650; }
.qa-page-student-appointment-reschedule .nav-tabs .nav-link.active { color: #0b6e60; box-shadow: inset 0 -2px #118c78; }
.qa-page-student-appointment-reschedule .nav-pills .nav-link.active { color: #0b6e60; background: #e8f8f4; box-shadow: inset 0 0 0 1px #ccece3; }
.qa-page-student-appointment-reschedule .progress { overflow: hidden; border-radius: 999px; background: #e9eff6; }
.qa-page-student-appointment-reschedule .progress-bar { border-radius: inherit; }
.qa-page-student-appointment-reschedule form:not(.qa-filter-bar) { --form-accent: var(--page-accent); }
.qa-page-student-appointment-reschedule form:not(.qa-filter-bar) > .card, .qa-page-student-appointment-reschedule form:not(.qa-filter-bar) .card { border-radius: 20px; }
.qa-page-student-appointment-reschedule form:not(.qa-filter-bar) .form-control:focus, .qa-page-student-appointment-reschedule form:not(.qa-filter-bar) .form-select:focus { border-color: var(--page-accent); box-shadow: 0 0 0 4px var(--page-tint); }
.qa-page-student-appointment-reschedule .slot-btn, .qa-page-student-appointment-reschedule .appointment-choice-card, .qa-page-student-appointment-reschedule .advisor-card { border-radius: 14px; transition: transform .2s ease, box-shadow .2s ease, border-color .2s ease; } .qa-page-student-appointment-reschedule .slot-btn:hover, .qa-page-student-appointment-reschedule .appointment-choice-card:hover, .qa-page-student-appointment-reschedule .advisor-card:hover { transform: translateY(-2px); }
@media (max-width: 767.98px) {
  .qa-page-student-appointment-reschedule > .page-header, .qa-page-student-appointment-reschedule > .qa-page-heading, .qa-page-student-appointment-reschedule > .d-flex:has(> .page-title), .qa-page-student-appointment-reschedule > .d-flex:has(> div > .page-title) { align-items: flex-start; padding: .95rem; }
  .qa-page-student-appointment-reschedule .card { border-radius: 15px; }
  .qa-page-student-appointment-reschedule .card-body { padding: 1rem; }
  .qa-page-student-appointment-reschedule .table-responsive { border-radius: 11px; }
  .qa-page-student-appointment-reschedule .qa-view-title-icon { flex-basis: 31px; width: 31px; height: 31px; border-radius: 10px; }
}
@media (prefers-reduced-motion: reduce) {
  .qa-page-student-appointment-reschedule *, .qa-page-student-appointment-reschedule *::before, .qa-page-student-appointment-reschedule *::after { scroll-behavior: auto !important; animation-duration: .01ms !important; animation-iteration-count: 1 !important; transition-duration: .01ms !important; }
}
</style>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<style>
.slot-btn { min-width: 110px; transition: all 0.15s ease-in-out; }
.slot-btn.selected { background-color: var(--tblr-primary) !important; color: #fff !important; border-color: var(--tblr-primary) !important; }
</style>

<div class="page-header d-print-none">
    <div class="container-xl">
        <div class="row g-2 align-items-center">
            <div class="col">
                <h2 class="page-title qa-view-title" data-qa-page="student-appointment-reschedule"><span class="qa-view-title-icon" aria-hidden="true"><i class="bi bi-calendar-check"></i></span>Đổi lịch hẹn tư vấn</h2>
            </div>
            <div class="col-auto ms-auto">
                <a href="${ctx}/student/appointments/${appointment.id()}" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left me-1"></i>Quay lại chi tiết
                </a>
            </div>
        </div>
    </div>
</div>

<div class="page-body">
    <div class="container-xl">
        <qa:alert/>

        <div class="row justify-content-center">
            <div class="col-12 col-lg-8">
                <%-- Current appointment info --%>
                <div class="card mb-3 shadow-sm border-primary-lt">
                    <div class="card-header bg-primary-lt">
                        <h3 class="card-title text-primary mb-0">
                            <i class="bi bi-info-circle me-2"></i>Lịch hẹn hiện tại
                        </h3>
                    </div>
                    <div class="card-body">
                        <div class="row g-2">
                            <div class="col-12 col-sm-6">
                                <div class="text-secondary small">Chủ đề:</div>
                                <div class="fw-semibold"><c:out value="${appointment.topic()}"/></div>
                            </div>
                            <div class="col-12 col-sm-6">
                                <div class="text-secondary small">Chuyên mục:</div>
                                <div><c:out value="${appointment.categoryPath()}"/></div>
                            </div>
                            <div class="col-12 col-sm-6">
                                <div class="text-secondary small">Tư vấn viên hiện tại:</div>
                                <div class="fw-medium"><c:out value="${appointment.managerName()}"/></div>
                            </div>
                            <div class="col-12 col-sm-6">
                                <div class="text-secondary small">Thời gian hiện tại:</div>
                                <div class="text-danger fw-semibold">
                                    <i class="bi bi-clock me-1"></i>
                                    <c:out value="${appointment.startAt()}"/> – <c:out value="${appointment.endAt().toLocalTime()}"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- Reschedule Form --%>
                <div class="card shadow-sm">
                    <div class="card-header">
                        <h3 class="card-title mb-0">
                            <i class="bi bi-calendar-event me-2"></i>Chọn khung giờ mới
                        </h3>
                    </div>
                    <div class="card-body">
                        <form id="rescheduleForm" method="post" action="${ctx}/student/appointments/${appointment.id()}/reschedule">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="hidden" id="newSlotId" name="newSlotId" value=""/>

                            <div class="row g-3 mb-4">
                                <div class="col-12 col-sm-6">
                                    <label class="form-label required" for="dateInput">Chọn ngày mới</label>
                                    <input type="date" id="dateInput" class="form-control" required/>
                                    <small class="form-hint">Chọn ngày cần dời lịch hẹn đến.</small>
                                </div>
                                <div class="col-12 col-sm-6">
                                    <label class="form-label" for="managerSelect">Tư vấn viên</label>
                                    <select id="managerSelect" class="form-select">
                                        <option value="">Bất kỳ tư vấn viên phù hợp</option>
                                        <option value="${appointment.managerId()}">
                                            <c:out value="${appointment.managerName()}"/> (Hiện tại)
                                        </option>
                                    </select>
                                    <small class="form-hint">Có thể giữ tư vấn viên hiện tại hoặc người khác.</small>
                                </div>
                            </div>

                            <%-- Slot Grid Area --%>
                            <div class="mb-4">
                                <label class="form-label required">Khung giờ trống</label>

                                <div id="slotPlaceholder" class="text-center text-secondary py-5 bg-light rounded">
                                    <i class="bi bi-calendar3 display-6 mb-2 d-block"></i>
                                    <p class="mb-0">Vui lòng chọn ngày để tìm các khung giờ trống phù hợp.</p>
                                </div>

                                <div id="slotLoading" style="display:none" class="text-center py-4 bg-light rounded">
                                    <span class="spinner-border spinner-border-sm me-2"></span>Đang kiểm tra khung giờ trống...
                                </div>

                                <div id="slotEmpty" style="display:none" class="text-center text-secondary py-4 bg-light rounded">
                                    <i class="bi bi-calendar-x display-6 mb-2 d-block text-warning"></i>
                                    <p class="mb-0">Không có khung giờ trống nào trong ngày đã chọn. Vui lòng chọn ngày khác.</p>
                                </div>

                                <div id="slotGrid" class="row g-2"></div>
                            </div>

                            <%-- Selected Slot Summary --%>
                            <div id="selectedSlotAlert" class="alert alert-success d-flex align-items-center gap-3 mb-4" style="display:none">
                                <i class="bi bi-check-circle-fill fs-2"></i>
                                <div>
                                    <div class="fw-bold" id="selectedSlotText">—</div>
                                    <div class="small" id="selectedAdvisorText">—</div>
                                </div>
                            </div>

                            <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                                <a href="${ctx}/student/appointments/${appointment.id()}" class="btn btn-outline-secondary">
                                    Hủy bỏ
                                </a>
                                <button type="submit" id="btnSubmitReschedule" class="btn btn-primary" disabled>
                                    <i class="bi bi-check-lg me-1"></i>Xác nhận đổi lịch
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Khởi tạo luồng chọn ngày và đổi khung giờ hẹn.
(function () {
    var ctx = '<c:out value="${ctx}"/>';
    var catId = '${appointment.categoryId()}';
    var modality = '${appointment.modality()}';
    var currentSlotId = '${appointment.slotId()}';

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
    var today = new Date().toISOString().slice(0, 10);
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

    // Định dạng thời gian theo quy ước tiếng Việt.
    function fmtTime(d) {
        return d.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit', hour12: false });
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
                var start = new Date(slot.startAt);
                var end = new Date(slot.endAt);
                var timeLabel = fmtTime(start) + ' – ' + fmtTime(end);
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
</script>
</div>
