<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<title>Thông tin liên hệ</title>

<nav aria-label="breadcrumb" class="mb-4">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/">Trang chủ</a></li>
        <li class="breadcrumb-item active" aria-current="page">Liên hệ</li>
    </ol>
</nav>

<div class="mb-4 pb-2 border-bottom">
    <h1 class="h2 mb-1">Thông tin liên hệ</h1>
    <p class="text-secondary mb-0">Địa chỉ, đường dây nóng và thông tin liên hệ của các đơn vị phòng ban hỗ trợ sinh viên tại HCMUTE</p>
</div>

<div class="row g-4 mb-5">
    <div class="col-md-4">
        <div class="card h-100 p-4 border shadow-sm">
            <div class="d-flex align-items-center mb-3">
                <div class="avatar bg-primary-subtle text-primary rounded-circle me-3" style="width: 48px; height: 48px; display: flex; align-items: center; justify-content: center; font-size: 1.5rem;">
                    <i class="bi bi-geo-alt"></i>
                </div>
                <div>
                    <h3 class="h5 mb-0">Địa chỉ trường</h3>
                    <div class="small text-secondary">Trụ sở chính</div>
                </div>
            </div>
            <div class="text-secondary">
                <c:choose>
                    <c:when test="${not empty contactAddress}">
                        <c:out value="${contactAddress}"/>
                    </c:when>
                    <c:otherwise>
                        Số 1 Võ Văn Ngân, Phường Linh Chiểu, TP. Thủ Đức, TP. Hồ Chí Minh
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card h-100 p-4 border shadow-sm">
            <div class="d-flex align-items-center mb-3">
                <div class="avatar bg-success-subtle text-success rounded-circle me-3" style="width: 48px; height: 48px; display: flex; align-items: center; justify-content: center; font-size: 1.5rem;">
                    <i class="bi bi-telephone"></i>
                </div>
                <div>
                    <h3 class="h5 mb-0">Đường dây nóng</h3>
                    <div class="small text-secondary">Tổng đài hỗ trợ</div>
                </div>
            </div>
            <div class="text-secondary">
                <c:choose>
                    <c:when test="${not empty contactPhone}">
                        <a href="tel:<c:out value='${contactPhone}'/>" class="text-decoration-none text-dark fw-bold fs-5">
                            <c:out value="${contactPhone}"/>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <span class="text-dark fw-bold fs-5">(028) 3896 8641</span>
                    </c:otherwise>
                </c:choose>
                <div class="small text-secondary mt-1">Giờ hành chính (07:30 - 17:00)</div>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card h-100 p-4 border shadow-sm">
            <div class="d-flex align-items-center mb-3">
                <div class="avatar bg-info-subtle text-info rounded-circle me-3" style="width: 48px; height: 48px; display: flex; align-items: center; justify-content: center; font-size: 1.5rem;">
                    <i class="bi bi-envelope"></i>
                </div>
                <div>
                    <h3 class="h5 mb-0">Hộp thư hỗ trợ</h3>
                    <div class="small text-secondary">Tiếp nhận ý kiến</div>
                </div>
            </div>
            <div class="text-secondary">
                <c:choose>
                    <c:when test="${not empty contactEmail}">
                        <a href="mailto:<c:out value='${contactEmail}'/>" class="text-decoration-none text-primary fw-semibold">
                            <c:out value="${contactEmail}"/>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="mailto:hotrosinhvien@hcmute.edu.vn" class="text-decoration-none text-primary fw-semibold">
                            hotrosinhvien@hcmute.edu.vn
                        </a>
                    </c:otherwise>
                </c:choose>
                <div class="small text-secondary mt-1">Phản hồi trong vòng 24 - 48 giờ làm việc</div>
            </div>
        </div>
    </div>
</div>

<div class="mb-4">
    <h2 class="h3 mb-1">Danh bạ các đơn vị & phòng ban chuyên trách</h2>
    <p class="text-secondary mb-4">Liên hệ trực tiếp theo từng mảng công việc để được giải quyết nhanh nhất</p>

    <c:choose>
        <c:when test="${empty tree}">
            <qa:emptyState icon="bi-building" title="Chưa có dữ liệu đơn vị" message="Hiện tại chưa có thông tin phòng ban nào được công khai."/>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <c:forEach var="dept" items="${tree}">
                    <div class="col-lg-6">
                        <div class="card h-100 shadow-sm border">
                            <div class="card-header bg-light-subtle py-3 d-flex justify-content-between align-items-center">
                                <h3 class="h4 card-title mb-0 text-primary">
                                    <i class="bi bi-building me-2"></i><c:out value="${dept.name()}"/>
                                </h3>
                                <c:if test="${not empty dept.code()}">
                                    <span class="badge bg-secondary-subtle text-secondary border">
                                        <c:out value="${dept.code()}"/>
                                    </span>
                                </c:if>
                            </div>
                            <div class="card-body">
                                <c:if test="${not empty dept.description()}">
                                    <p class="text-secondary small mb-3">
                                        <c:out value="${dept.description()}"/>
                                    </p>
                                </c:if>

                                <div class="list-group list-group-flush mb-3">
                                    <c:if test="${not empty dept.location()}">
                                        <div class="list-group-item px-0 py-2 d-flex align-items-center small">
                                            <i class="bi bi-geo-alt-fill text-danger me-2 fs-5"></i>
                                            <div>
                                                <strong>Văn phòng:</strong> <c:out value="${dept.location()}"/>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty dept.email()}">
                                        <div class="list-group-item px-0 py-2 d-flex align-items-center small">
                                            <i class="bi bi-envelope-fill text-primary me-2 fs-5"></i>
                                            <div>
                                                <strong>Email:</strong> <a href="mailto:<c:out value='${dept.email()}'/>" class="text-decoration-none"><c:out value="${dept.email()}"/></a>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty dept.phone()}">
                                        <div class="list-group-item px-0 py-2 d-flex align-items-center small">
                                            <i class="bi bi-telephone-fill text-success me-2 fs-5"></i>
                                            <div>
                                                <strong>Điện thoại:</strong> <a href="tel:<c:out value='${dept.phone()}'/>" class="text-decoration-none"><c:out value="${dept.phone()}"/></a>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>

                                <c:if test="${not empty dept.categories()}">
                                    <div class="pt-2 border-top">
                                        <div class="small fw-semibold text-secondary mb-2">Lĩnh vực tiếp nhận hỗ trợ:</div>
                                        <div class="d-flex flex-wrap gap-1">
                                            <c:forEach var="cat" items="${dept.categories()}">
                                                <a href="/faq?category=<c:out value='${cat.id()}'/>" class="badge bg-light text-dark border text-decoration-none" title="Xem câu hỏi FAQ">
                                                    <c:out value="${cat.name()}"/>
                                                </a>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

