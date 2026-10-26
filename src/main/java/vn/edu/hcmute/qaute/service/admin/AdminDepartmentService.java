package vn.edu.hcmute.qaute.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import vn.edu.hcmute.qaute.common.util.BoundedPages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.admin.DepartmentForm;
import vn.edu.hcmute.qaute.dto.view.DepartmentListItem;
import vn.edu.hcmute.qaute.entity.organization.Department;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;

/**
 * Quản trị phòng ban (ADM-05). Không xóa cứng phòng ban đã có dữ liệu,
 * chỉ tắt bằng cờ active để giữ nguyên liên kết của dữ liệu cũ.
 */
@Service
@Transactional
public class AdminDepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;
    private final AuditService auditService;

    // Khởi tạo dịch vụ quản trị phòng ban.
    public AdminDepartmentService(DepartmentRepository departmentRepository,
                                  CategoryRepository categoryRepository,
                                  AuditService auditService) {
        this.departmentRepository = departmentRepository;
        this.categoryRepository = categoryRepository;
        this.auditService = auditService;
    }

    // Liệt kê phòng ban cùng số lượng chuyên mục.
    @Transactional(readOnly = true)
    public List<DepartmentListItem> list() {
        List<Department> departments = departmentRepository.findAllByOrderBySortOrderAscNameAsc();
        List<DepartmentListItem> result = new ArrayList<>(departments.size());
        for (Department department : departments) {
            result.add(toListItem(department,
                    categoryRepository.countByDepartmentId(department.getId())));
        }
        return result;
    }

    // Lấy trang phòng ban đã giới hạn kích thước.
    @Transactional(readOnly = true)
    public Page<DepartmentListItem> page(Pageable requested) {
        Pageable page = BoundedPages.of(requested);
        return departmentRepository.findAll(PageRequest.of(page.getPageNumber(), page.getPageSize(),
                Sort.by("sortOrder", "name", "id"))).map(department ->
                toListItem(department, categoryRepository.countByDepartmentId(department.getId())));
    }

    // Lấy biểu mẫu của phòng ban cần sửa.
    @Transactional(readOnly = true)
    public DepartmentForm get(Long id) {
        return toForm(requireDepartment(id));
    }

    /**
     * Tạo mới khi form.id null, ngược lại cập nhật. Mã phòng ban phải duy nhất.
     * Chuẩn hóa mã thành chữ hoa để mã "DAOTAO" và "daotao" không thành hai phòng ban.
     */
    // Tạo hoặc cập nhật phòng ban từ biểu mẫu.
    public DepartmentForm save(DepartmentForm form, Long actorId) {
        String code = form.getCode() == null ? null : form.getCode().trim().toUpperCase();
        if (form.getId() == null) {
            if (departmentRepository.existsByCode(code)) {
                throw new DuplicateException("code", "Mã phòng ban đã được sử dụng");
            }
            Department department = new Department();
            applyForm(department, form, code);
            Department saved = departmentRepository.save(department);
            auditService.log(AuditActions.DEPARTMENT_CREATED, "department", saved.getId(),
                    null, summarize(saved));
            DepartmentForm savedForm = toForm(saved);
            savedForm.setCode(saved.getCode());
            return savedForm;
        }
        Department department = requireDepartment(form.getId());
        if (departmentRepository.existsByCodeAndIdNot(code, department.getId())) {
            throw new DuplicateException("code", "Mã phòng ban đã được sử dụng");
        }
        String before = summarize(department);
        applyForm(department, form, code);
        Department saved = departmentRepository.save(department);
        auditService.log(AuditActions.DEPARTMENT_UPDATED, "department", saved.getId(),
                before, summarize(saved));
        return toForm(saved);
    }

    /** Bật/tắt phòng ban mà không thay đổi liên kết của các yêu cầu hiện hữu. */
    // Đổi trạng thái hoạt động của phòng ban.
    public void toggleActive(Long id, Long actorId) {
        Department department = requireDepartment(id);
        boolean before = department.isActive();
        department.setActive(!before);
        departmentRepository.save(department);
        auditService.log(AuditActions.DEPARTMENT_TOGGLED, "department", id,
                "active=" + before, "active=" + (!before));
    }

    // Lấy phòng ban hoặc báo không tìm thấy.
    private Department requireDepartment(Long id) {
        if (id == null) {
            throw new NotFoundException("Không tìm thấy phòng ban");
        }
        return departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phòng ban"));
    }

    // Áp dụng dữ liệu biểu mẫu lên phòng ban.
    private void applyForm(Department department, DepartmentForm form, String code) {
        department.setCode(code);
        department.setName(form.getName() == null ? null : form.getName().trim());
        department.setDescription(blankToNull(form.getDescription()));
        department.setEmail(blankToNull(form.getEmail()) == null
                ? null : form.getEmail().trim().toLowerCase());
        department.setPhone(blankToNull(form.getPhone()) == null ? null : form.getPhone().trim());
        department.setLocation(blankToNull(form.getLocation()) == null
                ? null : form.getLocation().trim());
        department.setSortOrder(form.getSortOrder());
        department.setActive(form.isActive());
    }

    // Chuyển phòng ban thành biểu mẫu chỉnh sửa.
    private DepartmentForm toForm(Department department) {
        DepartmentForm form = new DepartmentForm();
        form.setId(department.getId());
        form.setCode(department.getCode());
        form.setName(department.getName());
        form.setDescription(department.getDescription());
        form.setEmail(department.getEmail());
        form.setPhone(department.getPhone());
        form.setLocation(department.getLocation());
        form.setSortOrder(department.getSortOrder());
        form.setActive(department.isActive());
        return form;
    }

    // Tạo mục danh sách phòng ban kèm số chuyên mục.
    private DepartmentListItem toListItem(Department department, long categoryCount) {
        return new DepartmentListItem(
                department.getId(),
                department.getCode(),
                department.getName(),
                department.getDescription(),
                department.getEmail(),
                department.getPhone(),
                department.getLocation(),
                department.getSortOrder(),
                department.isActive(),
                categoryCount);
    }

    // Tạo nội dung tóm tắt phòng ban để ghi nhật ký.
    private static String summarize(Department department) {
        return "code=" + department.getCode() + ", name=" + department.getName()
                + ", active=" + department.isActive();
    }

    // Chuyển chuỗi rỗng thành null sau khi cắt khoảng trắng.
    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
