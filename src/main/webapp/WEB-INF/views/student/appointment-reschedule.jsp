<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Đổi lịch hẹn tư vấn</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<style>
.slot-btn { min-width: 110px; transition: all 0.15s ease-in-out; }
.slot-btn.selected { background-color: var(--tblr-primary) !important; color: #fff !important; border-color: var(--tblr-primary) !important; }
</style>

<div class="page-header d-print-none">
    <div class="container-xl">
        <div class="row g-2 align-items-center">
            <div class="col">
                <h2 class="page-title">Đổi lịch hẹn tư vấn</h2>
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
            .then(function (r) { return r.json(); })
            .then(function (resp) {
                slotLoading.style.display = 'none';
                var slots = (resp.data || []).filter(function (s) {
                    return String(s.slotId) !== String(currentSlotId);
                });
                if (slots.length === 0) {
                    slotEmpty.style.display = 'block';
                } else {
                    renderSlots(slots, date);
                }
            })
            .catch(function () {
                slotLoading.style.display = 'none';
                slotGrid.innerHTML = '<div class="col-12 text-danger small p-2">Không tải được khung giờ trống.</div>';
            });
    }

    function fmtTime(d) {
        return d.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit', hour12: false });
    }

    function escH(s) {
        return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }

    function renderSlots(slots, dateVal) {
        slotGrid.innerHTML = '';
        slots.forEach(function (slot) {
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
            btn.addEventListener('click', function () {
                document.querySelectorAll('.slot-btn').forEach(function (b) { b.classList.remove('selected'); });
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
            });

            slotGrid.appendChild(col);
        });
    }

    if (dateInput) {
        dateInput.addEventListener('change', loadSlots);
    }
    if (managerSelect) {
        managerSelect.addEventListener('change', loadSlots);
    }

    if (form) {
        form.addEventListener('submit', function (e) {
            if (!newSlotIdInput.value) {
                e.preventDefault();
                alert('Vui lòng chọn một khung giờ mới.');
                return;
            }
            if (btnSubmit) {
                btnSubmit.disabled = true;
                btnSubmit.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang xử lý...';
            }
        });
    }

    // Initial load
    loadSlots();
})();
</script>

