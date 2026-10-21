<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Đặt lịch hẹn tư vấn</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<style>
.wizard-steps { display: flex; justify-content: space-between; margin-bottom: 2rem; position: relative; }
.wizard-step-item { flex: 1; text-align: center; position: relative; }
.wizard-step-item .step-number { width: 38px; height: 38px; line-height: 38px; border-radius: 50%; background-color: var(--tblr-gray-200); color: var(--tblr-gray-600); display: inline-block; font-weight: 600; margin-bottom: 0.5rem; transition: all 0.2s ease-in-out; }
.wizard-step-item.active .step-number { background-color: var(--tblr-primary); color: #fff; box-shadow: 0 0 0 4px rgba(32,107,196,0.2); }
.wizard-step-item.completed .step-number { background-color: var(--tblr-green); color: #fff; }
.wizard-step-item .step-title { font-size: 0.875rem; color: var(--tblr-secondary); display: block; }
.wizard-step-item.active .step-title { font-weight: 600; color: var(--tblr-dark); }
.wizard-step-item:not(:last-child)::after { content: ''; position: absolute; top: 19px; left: 50%; width: 100%; height: 2px; background-color: var(--tblr-gray-200); z-index: -1; }
.wizard-step-item.completed:not(:last-child)::after { background-color: var(--tblr-green); }
.wizard-panel { display: none; }
.wizard-panel.active { display: block; }
.advisor-card { cursor: pointer; border: 2px solid transparent; transition: border-color 0.2s; }
.advisor-card.selected { border-color: var(--tblr-primary) !important; }
.advisor-card:hover { border-color: rgba(32,107,196,0.3); }
.slot-btn { min-width: 110px; }
.slot-btn.selected { background-color: var(--tblr-primary); color: #fff; border-color: var(--tblr-primary); }
.dot-online { display:inline-block; width:9px; height:9px; border-radius:50%; background-color:var(--tblr-green); }
.dot-busy   { display:inline-block; width:9px; height:9px; border-radius:50%; background-color:var(--tblr-orange); }
.dot-offline{ display:inline-block; width:9px; height:9px; border-radius:50%; background-color:var(--tblr-gray-400); }
</style>

<div class="page-header d-print-none">
    <div class="container-xl">
        <div class="row g-2 align-items-center">
            <div class="col">
                <h2 class="page-title">Đặt lịch hẹn tư vấn</h2>
            </div>
            <div class="col-auto">
                <a href="${ctx}/student/appointments" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left me-1"></i>Lịch hẹn của tôi
                </a>
            </div>
        </div>
    </div>
</div>

<div class="page-body">
    <div class="container-xl">
        <qa:alert/>
        <div class="row justify-content-center">
            <div class="col-12 col-lg-9">
                <div class="wizard-steps mb-4">
                    <div class="wizard-step-item active" id="step-ind-1">
                        <span class="step-number">1</span>
                        <span class="step-title">Chọn tư vấn viên</span>
                    </div>
                    <div class="wizard-step-item" id="step-ind-2">
                        <span class="step-number">2</span>
                        <span class="step-title">Chọn khung giờ</span>
                    </div>
                    <div class="wizard-step-item" id="step-ind-3">
                        <span class="step-number">3</span>
                        <span class="step-title">Xác nhận</span>
                    </div>
                </div>

                <form id="apptBookForm" method="post" action="${ctx}/student/appointments/book">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <input type="hidden" id="fCategoryId" name="categoryId" value="${form.categoryId}">
                    <input type="hidden" id="fManagerId" name="managerId" value="${form.managerId}">
                    <input type="hidden" id="fSlotId" name="slotId" value="${form.slotId}">
                    <input type="hidden" id="fModality" name="modality" value="${form.modality}">
                    <input type="hidden" id="fDate" name="date" value="${form.date}">

                    <%-- Panel 1: Chuyên mục + Tư vấn viên --%>
                    <div class="wizard-panel active card" id="panel-1">
                        <div class="card-header">
                            <h3 class="card-title">Bước 1: Chọn chuyên mục và tư vấn viên</h3>
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label class="form-label required">Phòng ban</label>
                                <select id="deptSelect" class="form-select">
                                    <option value="">-- Chọn phòng ban --</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label required">Chuyên mục</label>
                                <select id="catSelect" class="form-select" disabled>
                                    <option value="">-- Chọn chuyên mục --</option>
                                </select>
                            </div>
                            <div id="advisorSection" style="display:none">
                                <hr>
                                <label class="form-label fw-semibold">Tư vấn viên (tùy chọn)</label>
                                <p class="text-secondary small mb-3">Bỏ qua để hệ thống tự chọn tư vấn viên phù hợp bất kỳ.</p>
                                <div class="row g-3" id="advisorCards">
                                    <div class="col-12 text-center text-secondary py-3" id="advisorLoading">
                                        <span class="spinner-border spinner-border-sm me-2"></span>Đang tải tư vấn viên...
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card-footer d-flex justify-content-between">
                            <a href="${ctx}/student/appointments" class="btn btn-outline-secondary">Hủy</a>
                            <button type="button" class="btn btn-primary" id="btn-next-1" disabled>
                                Tiếp theo <i class="bi bi-arrow-right ms-1"></i>
                            </button>
                        </div>
                    </div>

                    <%-- Panel 2: Ngày + Khung giờ --%>
                    <div class="wizard-panel card" id="panel-2">
                        <div class="card-header">
                            <h3 class="card-title">Bước 2: Chọn ngày và khung giờ</h3>
                        </div>
                        <div class="card-body">
                            <div class="row g-3 mb-4">
                                <div class="col-12 col-md-6">
                                    <label class="form-label required">Ngày hẹn</label>
                                    <input type="date" id="dateInput" class="form-control" value="${form.date}">
                                </div>
                                <div class="col-12 col-md-6">
                                    <label class="form-label">Hình thức</label>
                                    <select id="modalitySelect" class="form-select">
                                        <option value="">Tất cả hình thức</option>
                                        <option value="ONLINE">Trực tuyến (Online)</option>
                                        <option value="IN_PERSON">Trực tiếp (Offline)</option>
                                    </select>
                                </div>
                            </div>
                            <div id="slotPlaceholder" class="text-center text-secondary py-5">
                                <i class="bi bi-calendar3 display-5 mb-2 d-block"></i>
                                <p>Chọn ngày để xem khung giờ trống</p>
                            </div>
                            <div id="slotLoading" style="display:none" class="text-center py-4">
                                <span class="spinner-border spinner-border-sm me-2"></span>Đang tải khung giờ...
                            </div>
                            <div id="slotEmpty" style="display:none" class="text-center text-secondary py-4">
                                <i class="bi bi-calendar-x display-5 mb-2 d-block"></i>
                                <p>Không có khung giờ trống trong ngày này. Vui lòng chọn ngày khác.</p>
                            </div>
                            <div id="slotGrid" class="row g-2"></div>
                        </div>
                        <div class="card-footer d-flex justify-content-between">
                            <button type="button" class="btn btn-outline-secondary" id="btn-prev-2">
                                <i class="bi bi-arrow-left me-1"></i> Quay lại
                            </button>
                            <button type="button" class="btn btn-primary" id="btn-next-2" disabled>
                                Tiếp theo <i class="bi bi-arrow-right ms-1"></i>
                            </button>
                        </div>
                    </div>

                    <%-- Panel 3: Nội dung + Xác nhận --%>
                    <div class="wizard-panel card" id="panel-3">
                        <div class="card-header">
                            <h3 class="card-title">Bước 3: Nội dung tư vấn và xác nhận</h3>
                        </div>
                        <div class="card-body">
                            <div class="alert alert-info d-flex align-items-start gap-3 mb-4" id="confirmSummary">
                                <i class="bi bi-calendar-check fs-3 mt-1"></i>
                                <div>
                                    <div class="fw-semibold" id="summarySlot">—</div>
                                    <div class="text-secondary small" id="summaryAdvisor">—</div>
                                    <div class="text-secondary small" id="summaryModality">—</div>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label required">Chủ đề tư vấn</label>
                                <input type="text" name="topic" class="form-control" id="topicInput"
                                       maxlength="200" placeholder="Tóm tắt nội dung cần tư vấn (10–200 ký tự)"
                                       value="<c:out value='${form.topic}'/>">
                                <small class="form-hint text-end d-block" id="topicCount">0 / 200</small>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Ghi chú thêm (tùy chọn)</label>
                                <textarea name="note" class="form-control" id="noteInput" rows="3"
                                          maxlength="500"
                                          placeholder="Thông tin bổ sung bạn muốn tư vấn viên biết trước (tối đa 500 ký tự)"><c:out value="${form.note}"/></textarea>
                                <small class="form-hint text-end d-block" id="noteCount">0 / 500</small>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Gắn với yêu cầu tư vấn đang mở (tùy chọn)</label>
                                <select name="ticketId" class="form-select">
                                    <option value="">-- Không gắn yêu cầu nào --</option>
                                    <c:forEach var="t" items="${openTickets}">
                                        <option value="${t.id()}"
                                                ${form.ticketId != null and form.ticketId == t.id() ? 'selected' : ''}>
                                            [<c:out value="${t.code()}"/>] <c:out value="${t.title()}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="card-footer d-flex justify-content-between">
                            <button type="button" class="btn btn-outline-secondary" id="btn-prev-3">
                                <i class="bi bi-arrow-left me-1"></i> Quay lại
                            </button>
                            <button type="submit" class="btn btn-success" id="btnSubmit">
                                <i class="bi bi-check-lg me-1"></i> Xác nhận đặt lịch
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
window.APPT_TREE = [<c:forEach var="dept" items="${tree}" varStatus="ds">{id:${dept.id()},name:"<c:out value='${dept.name()}'/>",categories:[<c:forEach var="cat" items="${dept.categories()}" varStatus="cs">{id:${cat.id()},name:"<c:out value='${cat.name()}'/>",children:[<c:forEach var="sub" items="${cat.children()}" varStatus="ss">{id:${sub.id()},name:"<c:out value='${sub.name()}'/>"}${!ss.last?',':''}</c:forEach>]}${!cs.last?',':''}</c:forEach>]}${!ds.last?',':''}</c:forEach>];
window.APPT_CTX = '${ctx}';
</script>
<script src="${ctx}/assets/js/student/appointment-book.js"></script>

