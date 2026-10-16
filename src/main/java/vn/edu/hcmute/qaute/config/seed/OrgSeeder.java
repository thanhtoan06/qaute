package vn.edu.hcmute.qaute.config.seed;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.constant.ManagerCategoryLevel;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.entity.organization.Department;
import vn.edu.hcmute.qaute.entity.organization.ManagerCategory;
import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerCategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerProfileRepository;

/** Nạp dữ liệu demo cho miền tổ chức sau khi UserSeeder đã tạo các tài khoản Manager. */
@Component
public class OrgSeeder implements DemoSeeder {

    private static final String DAOTAO = "DAOTAO";
    private static final String CTSV = "CTSV";
    private static final String TAICHINH = "TAICHINH";
    private static final String KTX = "KTX";

    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;
    private final ManagerProfileRepository managerProfileRepository;
    private final ManagerCategoryRepository managerCategoryRepository;
    private final UserRepository userRepository;

    public OrgSeeder(DepartmentRepository departmentRepository,
                     CategoryRepository categoryRepository,
                     ManagerProfileRepository managerProfileRepository,
                     ManagerCategoryRepository managerCategoryRepository,
                     UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.categoryRepository = categoryRepository;
        this.managerProfileRepository = managerProfileRepository;
        this.managerCategoryRepository = managerCategoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public int order() {
        return 20;
    }

    /**
     * Mốc idempotent là phòng ban DAOTAO: nếu đã tồn tại, toàn bộ seed tổ chức được coi là đã nạp.
     */
    @Override
    @Transactional
    public void seed() {
        if (departmentRepository.existsByCode(DAOTAO)) {
            return;
        }

        Department daoTao = saveDepartment(DAOTAO, "Phòng Đào tạo",
                "daotao@hcmute.edu.vn", "Tòa A, tầng 1", 1);
        Department congTacSinhVien = saveDepartment(CTSV, "Phòng Công tác sinh viên",
                "ctsv@hcmute.edu.vn", "Tòa A, tầng 2", 2);
        Department taiChinh = saveDepartment(TAICHINH, "Phòng Kế hoạch – Tài chính",
                "taichinh@hcmute.edu.vn", "Tòa B, tầng 1", 3);
        Department kyTucXa = saveDepartment(KTX, "Ký túc xá & Dịch vụ sinh viên",
                "ktx@hcmute.edu.vn", "Khu KTX A", 4);

        Category dangKyHocPhan = saveRootCategory("DT_DKHP", "Đăng ký học phần", daoTao,
                "bi-journal-check", 1);
        saveRootCategory("DT_HOCVU", "Học vụ & điểm", daoTao, "bi-mortarboard", 2);
        saveRootCategory("DT_TOTNGHIEP", "Xét tốt nghiệp", daoTao, "bi-award", 3);
        saveRootCategory("CT_HOCBONG", "Học bổng & chính sách", congTacSinhVien, "bi-gift", 1);
        saveRootCategory("CT_RENLUYEN", "Điểm rèn luyện", congTacSinhVien, "bi-star", 2);
        saveRootCategory("TC_HOCPHI", "Học phí", taiChinh, "bi-cash-coin", 1);
        saveRootCategory("TC_MIENGIAM", "Miễn giảm & hoàn trả", taiChinh, "bi-receipt", 2);
        saveRootCategory("KTX_DANGKY", "Đăng ký ký túc xá", kyTucXa, "bi-house-door", 1);
        saveRootCategory("KTX_DICHVU", "Dịch vụ & sửa chữa", kyTucXa, "bi-tools", 2);
        saveChildCategory("DT_DKHP_SAI", "Đăng ký sai / rút học phần", dangKyHocPhan, 1);

        createProfiles(daoTao, congTacSinhVien, taiChinh, kyTucXa);
        createAssignments();
    }

    private Department saveDepartment(String code, String name, String email, String location, int sortOrder) {
        return departmentRepository.save(Department.builder()
                .code(code)
                .name(name)
                .email(email)
                .location(location)
                .sortOrder(sortOrder)
                .active(true)
                .build());
    }

    private Category saveRootCategory(String code, String name, Department department,
                                      String icon, int sortOrder) {
        return categoryRepository.save(Category.builder()
                .departmentId(department.getId())
                .depth(1)
                .code(code)
                .name(name)
                .icon(icon)
                .sortOrder(sortOrder)
                .active(true)
                .build());
    }

    private void saveChildCategory(String code, String name, Category parent, int sortOrder) {
        categoryRepository.save(Category.builder()
                .departmentId(parent.getDepartmentId())
                .parentId(parent.getId())
                .depth(2)
                .code(code)
                .name(name)
                .sortOrder(sortOrder)
                .active(true)
                .build());
    }

    private void createProfiles(Department daoTao, Department congTacSinhVien,
                                Department taiChinh, Department kyTucXa) {
        managerProfileRepository.saveAll(List.of(
                profile(manager(1), "CB001", daoTao, AvailabilityStatus.ONLINE),
                profile(manager(2), "CB002", congTacSinhVien, AvailabilityStatus.ONLINE),
                profile(manager(3), "CB003", taiChinh, AvailabilityStatus.OFFLINE),
                profile(manager(4), "CB004", kyTucXa, AvailabilityStatus.OFFLINE)));
    }

    private void createAssignments() {
        User manager1 = manager(1);
        User manager2 = manager(2);
        User manager3 = manager(3);
        User manager4 = manager(4);

        managerCategoryRepository.saveAll(List.of(
                assignment(manager1, "DT_DKHP", ManagerCategoryLevel.PRIMARY),
                assignment(manager1, "DT_HOCVU", ManagerCategoryLevel.PRIMARY),
                assignment(manager1, "DT_TOTNGHIEP", ManagerCategoryLevel.PRIMARY),
                assignment(manager2, "CT_HOCBONG", ManagerCategoryLevel.SECONDARY),
                assignment(manager2, "CT_RENLUYEN", ManagerCategoryLevel.SECONDARY),
                assignment(manager3, "TC_HOCPHI", ManagerCategoryLevel.SECONDARY),
                assignment(manager3, "TC_MIENGIAM", ManagerCategoryLevel.SECONDARY),
                assignment(manager4, "KTX_DANGKY", ManagerCategoryLevel.SECONDARY),
                assignment(manager4, "KTX_DICHVU", ManagerCategoryLevel.SECONDARY)));
    }

    private ManagerProfile profile(User manager, String employeeCode, Department department,
                                   AvailabilityStatus availabilityStatus) {
        return ManagerProfile.builder()
                .userId(manager.getId())
                .employeeCode(employeeCode)
                .jobTitle("Chuyên viên tư vấn")
                .departmentId(department.getId())
                .availabilityStatus(availabilityStatus)
                .maxConcurrentTickets(10)
                .maxConcurrentChats(3)
                .build();
    }

    private ManagerCategory assignment(User manager, String categoryCode, ManagerCategoryLevel level) {
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy chuyên mục demo " + categoryCode));
        return ManagerCategory.builder()
                .managerId(manager.getId())
                .categoryId(category.getId())
                .level(level)
                .build();
    }

    private User manager(int number) {
        String email = "manager" + number + "@qaute.edu.vn";
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy tài khoản demo " + email));
    }
}
