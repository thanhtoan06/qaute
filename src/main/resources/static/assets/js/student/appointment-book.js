(function (window, document) {
    'use strict';

    var ctx = window.APPT_CTX || '';
    var tree = window.APPT_TREE || [];

    var currentStep = 1;
    var selectedAdvisorId = null;
    var selectedAdvisorName = 'Bất kỳ tư vấn viên phù hợp';
    var selectedSlot = null;

    var deptSelect     = document.getElementById('deptSelect');
    var catSelect      = document.getElementById('catSelect');
    var advisorSection = document.getElementById('advisorSection');
    var advisorCards   = document.getElementById('advisorCards');
    var advisorLoading = document.getElementById('advisorLoading');
    var btnNext1  = document.getElementById('btn-next-1');
    var btnPrev2  = document.getElementById('btn-prev-2');
    var btnNext2  = document.getElementById('btn-next-2');
    var btnPrev3  = document.getElementById('btn-prev-3');
    var dateInput = document.getElementById('dateInput');
    var modalitySelect = document.getElementById('modalitySelect');
    var slotPlaceholder = document.getElementById('slotPlaceholder');
    var slotLoading = document.getElementById('slotLoading');
    var slotEmpty   = document.getElementById('slotEmpty');
    var slotGrid    = document.getElementById('slotGrid');
    var fCategoryId = document.getElementById('fCategoryId');
    var fManagerId  = document.getElementById('fManagerId');
    var fSlotId     = document.getElementById('fSlotId');
    var fModality   = document.getElementById('fModality');
    var fDate       = document.getElementById('fDate');
    var topicInput  = document.getElementById('topicInput');
    var noteInput   = document.getElementById('noteInput');
    var topicCount  = document.getElementById('topicCount');
    var noteCount   = document.getElementById('noteCount');
    var summarySlot    = document.getElementById('summarySlot');
    var summaryAdvisor = document.getElementById('summaryAdvisor');
    var summaryModality = document.getElementById('summaryModality');
    var btnSubmit   = document.getElementById('btnSubmit');

    // Set min date = today
    var today = new Date().toISOString().slice(0, 10);
    if (dateInput) { dateInput.min = today; if (!dateInput.value) dateInput.value = today; }

    // ─── Step navigation ─────────────────────────────────────────────────────
    function goStep(n) {
        document.getElementById('panel-' + currentStep).classList.remove('active');
        var indCurrent = document.getElementById('step-ind-' + currentStep);
        indCurrent.classList.remove('active');
        if (n > currentStep) { indCurrent.classList.add('completed'); }
        else { indCurrent.classList.remove('completed'); }
        currentStep = n;
        document.getElementById('panel-' + currentStep).classList.add('active');
        document.getElementById('step-ind-' + currentStep).classList.add('active');
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    // ─── Populate departments ─────────────────────────────────────────────────
    function populateDepts() {
        if (!deptSelect) return;
        tree.forEach(function (dept) {
            var opt = document.createElement('option');
            opt.value = dept.id;
            opt.textContent = dept.name;
            deptSelect.appendChild(opt);
        });
    }

    if (deptSelect) {
        deptSelect.addEventListener('change', function () {
            var deptId = Number(this.value);
            catSelect.innerHTML = '<option value="">-- Chọn chuyên mục --</option>';
            catSelect.disabled = true;
            fCategoryId.value = '';
            advisorSection.style.display = 'none';
            btnNext1.disabled = true;
            selectedAdvisorId = null;
            if (!deptId) return;

            var dept = null;
            for (var i = 0; i < tree.length; i++) {
                if (tree[i].id === deptId) { dept = tree[i]; break; }
            }
            if (!dept) return;

            (dept.categories || []).forEach(function (cat) {
                var opt = document.createElement('option');
                opt.value = cat.id;
                opt.textContent = cat.name;
                catSelect.appendChild(opt);
                (cat.children || []).forEach(function (sub) {
                    var sopt = document.createElement('option');
                    sopt.value = sub.id;
                    sopt.textContent = '\u00a0\u00a0\u00a0\u203a ' + sub.name;
                    catSelect.appendChild(sopt);
                });
            });
            catSelect.disabled = false;
        });
    }

    if (catSelect) {
        catSelect.addEventListener('change', function () {
            var catId = Number(this.value);
            fCategoryId.value = catId || '';
            advisorSection.style.display = 'none';
            btnNext1.disabled = true;
            selectedAdvisorId = null;
            if (!catId) return;
            advisorSection.style.display = 'block';
            btnNext1.disabled = false;
            loadAdvisors(catId);
        });
    }

    // ─── Load advisors ────────────────────────────────────────────────────────
    function loadAdvisors(categoryId) {
        advisorCards.innerHTML = '';
        advisorCards.appendChild(advisorLoading);
        advisorLoading.style.display = 'block';

        fetch(ctx + '/api/appointments/advisors?categoryId=' + categoryId)
            .then(function (r) { return r.json(); })
            .then(function (resp) {
                advisorLoading.style.display = 'none';
                renderAdvisors(resp.data || []);
            })
            .catch(function () {
                advisorLoading.style.display = 'none';
                advisorCards.innerHTML = '<div class="col-12 text-danger small p-2">Không tải được danh sách tư vấn viên.</div>';
            });
    }

    function renderAdvisors(advisors) {
        advisorCards.innerHTML = '';
        // "Any advisor" option
        advisorCards.appendChild(buildAdvisorCard(null, 'Bất kỳ tư vấn viên phù hợp', 'Hệ thống sẽ tự phân công', null, null, true));
        advisors.forEach(function (a) {
            advisorCards.appendChild(buildAdvisorCard(a.id, a.fullName, a.jobTitle, a.avatarUrl, a.status, false));
        });
    }

    function buildAdvisorCard(id, name, title, avatarUrl, status, preSelected) {
        var col = document.createElement('div');
        col.className = 'col-12 col-sm-6';

        var dotClass = status === 'ONLINE' ? 'dot-online' : (status === 'BUSY' ? 'dot-busy' : 'dot-offline');
        var dotLabel = status === 'ONLINE' ? 'Trực tuyến' : (status === 'BUSY' ? 'Đang bận' : (status ? 'Ngoại tuyến' : ''));
        var avatarHtml = avatarUrl
            ? '<img src="' + escH(avatarUrl) + '" class="rounded-circle me-3" width="48" height="48" alt="">'
            : '<span class="avatar avatar-md me-3" style="background-color:var(--tblr-primary-lt)">' + escH(name.charAt(0) || '?') + '</span>';

        col.innerHTML = '<div class="card advisor-card h-100' + (preSelected ? ' selected' : '') + '"' +
            ' data-aid="' + (id || '') + '" data-aname="' + escH(name) + '">' +
            '<div class="card-body d-flex align-items-center">' +
            avatarHtml +
            '<div class="flex-fill">' +
            '<div class="fw-semibold">' + escH(name) + '</div>' +
            '<div class="text-secondary small">' + escH(title || '') + '</div>' +
            (status ? '<div class="mt-1"><span class="' + dotClass + ' me-1"></span><span class="small">' + dotLabel + '</span></div>' : '') +
            '</div>' +
            '<i class="bi ' + (preSelected ? 'bi-check-circle-fill text-primary' : 'bi-circle text-secondary') + ' ms-2 fs-5 adv-check"></i>' +
            '</div></div>';

        col.querySelector('.advisor-card').addEventListener('click', function () {
            document.querySelectorAll('.advisor-card').forEach(function (c) {
                c.classList.remove('selected');
                var ic = c.querySelector('.adv-check');
                if (ic) ic.className = 'bi bi-circle text-secondary ms-2 fs-5 adv-check';
            });
            this.classList.add('selected');
            var ic = this.querySelector('.adv-check');
            if (ic) ic.className = 'bi bi-check-circle-fill text-primary ms-2 fs-5 adv-check';
            selectedAdvisorId = this.getAttribute('data-aid') || null;
            selectedAdvisorName = this.getAttribute('data-aname');
            fManagerId.value = selectedAdvisorId || '';
        });

        if (preSelected) {
            selectedAdvisorId = null;
            selectedAdvisorName = name;
            fManagerId.value = '';
        }
        return col;
    }

    function escH(s) {
        return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }

    if (btnNext1) {
        btnNext1.addEventListener('click', function () {
            if (!fCategoryId.value) return;
            goStep(2);
            if (dateInput && dateInput.value) loadSlots();
        });
    }

    if (btnPrev2) btnPrev2.addEventListener('click', function () { goStep(1); });

    // ─── Load slots ───────────────────────────────────────────────────────────
    function loadSlots() {
        var date = dateInput ? dateInput.value : '';
        var modality = modalitySelect ? modalitySelect.value : '';
        var catId = fCategoryId.value;
        if (!date || !catId) return;

        slotPlaceholder.style.display = 'none';
        slotLoading.style.display = 'block';
        slotEmpty.style.display = 'none';
        slotGrid.innerHTML = '';
        selectedSlot = null;
        fSlotId.value = '';
        btnNext2.disabled = true;

        var url = ctx + '/api/appointments/slots?categoryId=' + catId + '&date=' + date;
        if (fManagerId.value) url += '&managerId=' + fManagerId.value;
        if (modality) url += '&modality=' + modality;

        fetch(url)
            .then(function (r) { return r.json(); })
            .then(function (resp) {
                slotLoading.style.display = 'none';
                var slots = resp.data || [];
                if (slots.length === 0) {
                    slotEmpty.style.display = 'block';
                } else {
                    renderSlots(slots, date);
                }
            })
            .catch(function () {
                slotLoading.style.display = 'none';
                slotGrid.innerHTML = '<div class="col-12 text-danger small p-2">Không tải được khung giờ.</div>';
            });
    }

    function renderSlots(slots, date) {
        slotGrid.innerHTML = '';
        slots.forEach(function (slot) {
            var start = new Date(slot.startAt);
            var end   = new Date(slot.endAt);
            var timeLabel = fmtTime(start) + ' – ' + fmtTime(end);
            var mIcon = slot.modality === 'ONLINE' ? '🖥' : slot.modality === 'IN_PERSON' ? '🏫' : '🔄';
            var mLabel = slot.modality === 'ONLINE' ? 'Online' : slot.modality === 'IN_PERSON' ? 'Trực tiếp' : 'Linh hoạt';

            var col = document.createElement('div');
            col.className = 'col-6 col-sm-4 col-md-3';
            col.innerHTML = '<button type="button" class="btn btn-outline-primary slot-btn w-100 py-2"' +
                ' data-sid="' + escH(String(slot.slotId)) + '"' +
                ' data-slabel="' + escH(timeLabel) + '"' +
                ' data-smanager="' + escH(slot.managerName || '') + '"' +
                ' data-smodality="' + escH(slot.modality || '') + '"' +
                ' data-sstart="' + escH(slot.startAt || '') + '">' +
                '<div class="fw-semibold small">' + escH(timeLabel) + '</div>' +
                '<div class="small text-muted">' + mIcon + ' ' + mLabel + '</div>' +
                (slot.managerName ? '<div class="small text-muted text-truncate" style="max-width:120px">' + escH(slot.managerName) + '</div>' : '') +
                '</button>';

            var btn = col.querySelector('.slot-btn');
            btn.addEventListener('click', function () {
                document.querySelectorAll('.slot-btn').forEach(function (b) { b.classList.remove('selected'); });
                this.classList.add('selected');
                selectedSlot = {
                    id:      this.getAttribute('data-sid'),
                    label:   this.getAttribute('data-slabel'),
                    manager: this.getAttribute('data-smanager'),
                    modality: this.getAttribute('data-smodality'),
                    startAt: this.getAttribute('data-sstart')
                };
                fSlotId.value = selectedSlot.id;
                fDate.value = date;
                fModality.value = selectedSlot.modality;
                btnNext2.disabled = false;
            });
            slotGrid.appendChild(col);
        });
    }

    function fmtTime(d) {
        return d.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit', hour12: false });
    }

    if (dateInput) dateInput.addEventListener('change', loadSlots);
    if (modalitySelect) modalitySelect.addEventListener('change', function () { if (dateInput && dateInput.value) loadSlots(); });

    if (btnNext2) {
        btnNext2.addEventListener('click', function () {
            if (!selectedSlot) return;
            updateSummary();
            goStep(3);
        });
    }

    if (btnPrev3) btnPrev3.addEventListener('click', function () { goStep(2); });

    function updateSummary() {
        if (!selectedSlot) return;
        var dateVal = fDate.value || '';
        var parts = dateVal.split('-');
        var dateLabel = parts.length === 3 ? (parts[2] + '/' + parts[1] + '/' + parts[0]) : dateVal;
        var mLabel = selectedSlot.modality === 'ONLINE' ? 'Trực tuyến (Online)' :
                     (selectedSlot.modality === 'IN_PERSON' ? 'Trực tiếp (Offline)' : 'Linh hoạt');
        if (summarySlot)     summarySlot.textContent    = '🕐 ' + selectedSlot.label + ' — ' + dateLabel;
        if (summaryAdvisor)  summaryAdvisor.textContent  = '👤 ' + (selectedSlot.manager || selectedAdvisorName);
        if (summaryModality) summaryModality.textContent = '📍 Hình thức: ' + mLabel;
    }

    // ─── Char counters ────────────────────────────────────────────────────────
    function charCounter(input, counter) {
        if (!input || !counter) return;
        function upd() { counter.textContent = input.value.length + ' / ' + (input.maxLength > 0 ? input.maxLength : ''); }
        input.addEventListener('input', upd);
        upd();
    }
    charCounter(topicInput, topicCount);
    charCounter(noteInput, noteCount);

    // ─── Submit guard ─────────────────────────────────────────────────────────
    var form = document.getElementById('apptBookForm');
    if (form) {
        form.addEventListener('submit', function (e) {
            if (!fSlotId.value) {
                e.preventDefault();
                alert('Vui lòng chọn khung giờ trước khi đặt lịch.');
                return;
            }
            var topic = topicInput ? topicInput.value.trim() : '';
            if (topic.length < 10) {
                e.preventDefault();
                if (topicInput) topicInput.focus();
                alert('Chủ đề tư vấn phải từ 10 ký tự trở lên.');
                return;
            }
            if (btnSubmit) {
                btnSubmit.disabled = true;
                btnSubmit.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang xử lý...';
            }
        });
    }

    // ─── Init ─────────────────────────────────────────────────────────────────
    populateDepts();

}(window, document));

