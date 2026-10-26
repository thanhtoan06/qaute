package vn.edu.hcmute.qaute.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.hcmute.qaute.common.util.BoundedPages;
import vn.edu.hcmute.qaute.common.util.TextUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.constant.ManagerCategoryLevel;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.admin.ManagerScopeForm;
import vn.edu.hcmute.qaute.dto.view.DepartmentOption;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
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
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

/**
 * Quản trị hồ sơ, giới hạn đồng thời và phạm vi chuyên mục của Manager (ADM-07).
 */
@Service
@Transactional
public class AdminManagerScopeService {

    private static final int MAX_CATEGORY_DEPTH = 2;

    private final UserRepository userRepository;
    private final UserLookupService userLookupService;
    private final ManagerProfileRepository managerProfileRepository;
    private final ManagerCategoryRepository managerCategoryRepository;
    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;
    private final ManagerScopeService managerScopeService;
    private final AuditService auditService;

    // Khởi tạo dịch vụ quản trị hồ sơ và phạm vi Manager.
    public AdminManagerScopeService(UserRepository userRepository,
                                    UserLookupService userLookupService,
                                    ManagerProfileRepository managerProfileRepository,
                                    ManagerCategoryRepository managerCategoryRepository,
                                    DepartmentRepository departmentRepository,
                                    CategoryRepository categoryRepository,
                                    ManagerScopeService managerScopeService,
                                    AuditService auditService) {
        this.userRepository = userRepository;
        this.userLookupService = userLookupService;
        this.managerProfileRepository = managerProfileRepository;
        this.managerCategoryRepository = managerCategoryRepository;
        this.departmentRepository = departmentRepository;
        this.categoryRepository = categoryRepository;
        this.managerScopeService = managerScopeService;
        this.auditService = auditService;
    }

    /** Danh sách toàn bộ Manager, có thể lọc không phân biệt hoa thường theo tên hoặc email. */
    // Liệt kê Manager theo từ khóa.
    @Transactional(readOnly = true)
    public List<ManagerListItem> list(String keyword) {
        return page(keyword, BoundedPages.of(0, 100)).getContent();
    }

    // Lấy trang Manager cùng hồ sơ và phạm vi phụ trách.
    @Transactional(readOnly = true)
    public Page<ManagerListItem> page(String keyword, Pageable requested) {
        Specification<User> spec = (root, query, cb) -> {
            var role = cb.equal(root.get("role").get("code"), RoleCode.MANAGER);
            if (keyword == null || keyword.isBlank()) return role;
            String pattern = "%" + TextUtil.escapeLike(keyword.trim().toLowerCase(java.util.Locale.ROOT)) + "%";
            return cb.and(role, cb.or(cb.like(cb.lower(root.get("fullName")), pattern, '\\'),
                    cb.like(cb.lower(root.get("email")), pattern, '\\')));
        };
        Pageable page = BoundedPages.of(requested);
        Page<User> managers = userRepository.findAll(spec, PageRequest.of(page.getPageNumber(),
                page.getPageSize(), Sort.by("fullName", "id")));
        return new PageImpl<>(summaries(managers.getContent()), managers.getPageable(), managers.getTotalElements());
    }

    // Tạo dữ liệu tóm tắt cho danh sách Manager.
    private List<ManagerListItem> summaries(List<User> managers) {
        if (managers.isEmpty()) {
            return List.of();
        }

        List<Long> managerIds = managers.stream().map(User::getId).toList();
        Map<Long, ManagerProfile> profiles = managerProfileRepository.findByUserIdIn(managerIds).stream()
                .collect(Collectors.toMap(ManagerProfile::getUserId, profile -> profile));
        Map<Long, Department> departments = departmentsById(profiles.values().stream()
                .map(ManagerProfile::getDepartmentId)
                .filter(java.util.Objects::nonNull)
                .toList());

        Map<Long, List<ManagerCategory>> assignmentsByManager = new HashMap<>();
        for (ManagerCategory assignment : managerCategoryRepository.findByManagerIdIn(managerIds)) {
            assignmentsByManager.computeIfAbsent(assignment.getManagerId(), ignored -> new ArrayList<>())
                    .add(assignment);
        }
        Set<Long> assignedCategoryIds = assignmentsByManager.values().stream()
                .flatMap(Collection::stream)
                .map(ManagerCategory::getCategoryId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Map<Long, Category> categories = categoriesById(assignedCategoryIds);

        List<ManagerListItem> result = new ArrayList<>(managers.size());
        for (User manager : managers) {
            ManagerProfile profile = profiles.get(manager.getId());
            Department department = profile == null || profile.getDepartmentId() == null
                    ? null : departments.get(profile.getDepartmentId());
            List<String> categoryNames = assignmentsByManager
                    .getOrDefault(manager.getId(), List.of())
                    .stream()
                    .map(ManagerCategory::getCategoryId)
                    .map(categories::get)
                    .filter(java.util.Objects::nonNull)
                    .map(Category::getName)
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList();
            AvailabilityStatus availability = profile == null || profile.getAvailabilityStatus() == null
                    ? AvailabilityStatus.OFFLINE : profile.getAvailabilityStatus();
            result.add(new ManagerListItem(
                    manager.getId(), manager.getFullName(), manager.getEmail(), manager.getStatus(),
                    department == null ? null : department.getName(), categoryNames,
                    managerScopeService.currentOpenTicketCount(manager.getId()), availability));
        }
        return List.copyOf(result);
    }

    /** Thông tin gọn dùng làm tiêu đề trang cấu hình, đồng thời chặn id không phải Manager. */
    // Lấy thông tin tiêu đề hồ sơ Manager.
    @Transactional(readOnly = true)
    public ManagerHeader getManagerHeader(Long managerId) {
        requireManager(managerId);
        UserSummary summary = userLookupService.getSummary(managerId);
        return new ManagerHeader(summary.id(), summary.fullName(), summary.email(), summary.status());
    }

    /** Lấy form hiện tại; Manager chưa có profile nhận các giới hạn mặc định. */
    // Tạo biểu mẫu cấu hình cho Manager.
    @Transactional(readOnly = true)
    public ManagerScopeForm getForm(Long managerId) {
        requireManager(managerId);
        ManagerScopeForm form = new ManagerScopeForm();
        form.setManagerId(managerId);
        managerProfileRepository.findByUserId(managerId).ifPresent(profile -> applyProfileToForm(profile, form));

        List<ManagerCategory> assignments = managerCategoryRepository.findByManagerId(managerId);
        form.setCategoryIds(assignments.stream().map(ManagerCategory::getCategoryId).toList());
        form.setPrimaryCategoryIds(assignments.stream()
                .filter(assignment -> assignment.getLevel() == ManagerCategoryLevel.PRIMARY)
                .map(ManagerCategory::getCategoryId)
                .toList());
        return form;
    }

    /** Danh sách phòng ban cho phần hồ sơ; vẫn hiển thị cả phòng ban đã tắt để sửa dữ liệu cũ. */
    // Liệt kê phòng ban để chọn cho Manager.
    @Transactional(readOnly = true)
    public List<DepartmentOption> departmentOptions() {
        return departmentRepository.findAllByOrderBySortOrderAscNameAsc().stream()
                .map(department -> new DepartmentOption(
                        department.getId(), department.getName(), department.isActive()))
                .toList();
    }

    /**
     * Lưu hồ sơ và thay toàn bộ phạm vi. Chuyên mục đã chọn phải tồn tại, đang hoạt động,
     * và chuyên mục chính phải là tập con của danh sách được giao.
     */
    // Cập nhật hồ sơ và chuyên mục được giao cho Manager.
    public void save(ManagerScopeForm form, Long actorId) {
        if (form == null || form.getManagerId() == null) {
            throw new BadRequestException("Manager không hợp lệ", Map.of("managerId", "Manager không hợp lệ"));
        }
        Long managerId = form.getManagerId();
        requireManager(managerId);
        validateDepartment(form.getDepartmentId());
        validateConcurrencyLimits(form);

        LinkedHashSet<Long> selectedCategoryIds = normalizedIds(form.getCategoryIds(), "categoryIds");
        LinkedHashSet<Long> primaryCategoryIds = normalizedIds(form.getPrimaryCategoryIds(), "primaryCategoryIds");
        if (!selectedCategoryIds.containsAll(primaryCategoryIds)) {
            throw new BadRequestException(
                    "Chuyên mục phụ trách chính phải thuộc danh sách chuyên mục phụ trách",
                    Map.of("primaryCategoryIds",
                            "Chuyên mục chính phải thuộc danh sách chuyên mục phụ trách"));
        }
        Map<Long, Category> selectedCategories = validateActiveCategories(selectedCategoryIds);

        List<ManagerCategory> beforeAssignments = managerCategoryRepository.findByManagerId(managerId);
        String before = categoryCodes(beforeAssignments.stream()
                .map(ManagerCategory::getCategoryId).toList());

        ManagerProfile profile = managerProfileRepository.findByUserId(managerId)
                .orElseGet(() -> {
                    ManagerProfile created = new ManagerProfile();
                    created.setUserId(managerId);
                    return created;
                });
        applyFormToProfile(form, profile);
        managerProfileRepository.save(profile);

        // Xả lệnh xóa trước khi thêm lại để không va unique(manager_id, category_id).
        managerCategoryRepository.deleteByManagerId(managerId);
        managerCategoryRepository.flush();

        List<ManagerCategory> replacements = new ArrayList<>(selectedCategoryIds.size());
        for (Long categoryId : selectedCategoryIds) {
            replacements.add(ManagerCategory.builder()
                    .managerId(managerId)
                    .categoryId(categoryId)
                    .level(primaryCategoryIds.contains(categoryId)
                            ? ManagerCategoryLevel.PRIMARY : ManagerCategoryLevel.SECONDARY)
                    .build());
        }
        managerCategoryRepository.saveAll(replacements);

        auditService.log(AuditActions.MANAGER_SCOPE_CHANGED, "manager_scope", managerId,
                before, categoryCodes(selectedCategories.keySet()));
    }

    /** Cây checkbox theo phòng ban, chỉ bao gồm các chuyên mục đang hoạt động. */
    // Tạo cây chuyên mục đang hoạt động cho cấu hình phạm vi.
    @Transactional(readOnly = true)
    public List<ScopeDepartment> categoryTree() {
        List<ScopeDepartment> result = new ArrayList<>();
        for (Department department : departmentRepository.findAllByOrderBySortOrderAscNameAsc()) {
            List<Category> activeCategories = categoryRepository
                    .findByDepartmentIdOrderBySortOrderAscNameAsc(department.getId()).stream()
                    .filter(Category::isActive)
                    .toList();
            if (activeCategories.isEmpty()) {
                continue;
            }
            Map<Long, Category> activeById = activeCategories.stream()
                    .collect(Collectors.toMap(Category::getId, category -> category));
            Map<Long, List<Category>> childrenByParent = new HashMap<>();
            List<Category> roots = new ArrayList<>();
            for (Category category : activeCategories) {
                if (category.getParentId() != null && activeById.containsKey(category.getParentId())) {
                    childrenByParent.computeIfAbsent(category.getParentId(), ignored -> new ArrayList<>())
                            .add(category);
                } else {
                    roots.add(category);
                }
            }
            List<ScopeCategory> categories = roots.stream()
                    .map(root -> toScopeCategory(root, childrenByParent.getOrDefault(root.getId(), List.of())))
                    .toList();
            result.add(new ScopeDepartment(department.getId(), department.getName(),
                    department.isActive(), categories));
        }
        return List.copyOf(result);
    }

    /** Các chuyên mục active không có dòng giao trực tiếp, cũng không được bao phủ bởi chuyên mục cha. */
    // Tìm chuyên mục chưa được Manager nào bao phủ.
    @Transactional(readOnly = true)
    public List<UncoveredCategory> uncoveredCategories() {
        List<Category> activeCategories = categoryRepository.findByActiveTrueOrderBySortOrderAscNameAsc();
        if (activeCategories.isEmpty()) {
            return List.of();
        }
        Set<Long> assignedCategoryIds = managerCategoryRepository.findAll().stream()
                .map(ManagerCategory::getCategoryId)
                .collect(Collectors.toSet());
        // Cần toàn bộ cây (kể cả tổ tiên đã tắt) để xét đúng điều kiện "có tổ tiên được giao".
        Map<Long, Category> categoriesById = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, category -> category));
        Map<Long, Department> departments = departmentsById(activeCategories.stream()
                .map(Category::getDepartmentId)
                .distinct()
                .toList());

        List<UncoveredCategory> result = new ArrayList<>();
        for (Category category : activeCategories) {
            if (assignedCategoryIds.contains(category.getId())
                    || hasAssignedAncestor(category, categoriesById, assignedCategoryIds)) {
                continue;
            }
            Department department = departments.get(category.getDepartmentId());
            result.add(new UncoveredCategory(category.getId(),
                    department == null ? null : department.getName(),
                    category.getCode(), displayCategoryName(category, categoriesById)));
        }
        return List.copyOf(result);
    }

    // Lấy tài khoản Manager hoặc báo không tìm thấy.
    private User requireManager(Long managerId) {
        if (managerId == null) {
            throw new NotFoundException("Không tìm thấy Manager");
        }
        User user = userRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Manager"));
        if (user.getRole() == null || user.getRole().getCode() != RoleCode.MANAGER) {
            throw new NotFoundException("Không tìm thấy Manager");
        }
        return user;
    }

    // Kiểm tra phòng ban được chỉ định có tồn tại.
    private void validateDepartment(Long departmentId) {
        if (departmentId != null && !departmentRepository.existsById(departmentId)) {
            throw new BadRequestException("Phòng ban không tồn tại",
                    Map.of("departmentId", "Phòng ban không tồn tại"));
        }
    }

    // Lấy chuyên mục giao hợp lệ và đang hoạt động.
    private Map<Long, Category> validateActiveCategories(Set<Long> categoryIds) {
        Map<Long, Category> categories = categoriesById(categoryIds);
        if (categories.size() != categoryIds.size()) {
            throw new BadRequestException("Có chuyên mục không tồn tại",
                    Map.of("categoryIds", "Có chuyên mục không tồn tại"));
        }
        List<Category> inactive = categories.values().stream().filter(category -> !category.isActive()).toList();
        if (!inactive.isEmpty()) {
            throw new BadRequestException("Chỉ được giao chuyên mục đang hoạt động",
                    Map.of("categoryIds", "Chỉ được giao chuyên mục đang hoạt động"));
        }
        return categories;
    }

    // Kiểm tra giới hạn ticket và chat đồng thời.
    private static void validateConcurrencyLimits(ManagerScopeForm form) {
        Map<String, String> fieldErrors = new HashMap<>();
        if (form.getMaxConcurrentTickets() < 1 || form.getMaxConcurrentTickets() > 50) {
            fieldErrors.put("maxConcurrentTickets", "Số ticket đồng thời phải từ 1 đến 50");
        }
        if (form.getMaxConcurrentChats() < 1 || form.getMaxConcurrentChats() > 10) {
            fieldErrors.put("maxConcurrentChats", "Số chat đồng thời phải từ 1 đến 10");
        }
        if (!fieldErrors.isEmpty()) {
            throw new BadRequestException("Giới hạn đồng thời không hợp lệ", fieldErrors);
        }
    }

    // Kiểm tra và khử trùng lặp danh sách mã chuyên mục.
    private static LinkedHashSet<Long> normalizedIds(List<Long> ids, String field) {
        if (ids == null || ids.isEmpty()) {
            return new LinkedHashSet<>();
        }
        if (ids.stream().anyMatch(java.util.Objects::isNull)) {
            throw new BadRequestException("Chuyên mục không hợp lệ",
                    Map.of(field, "Chuyên mục không hợp lệ"));
        }
        return new LinkedHashSet<>(ids);
    }

    // Kiểm tra một tổ tiên của chuyên mục đã được giao.
    private boolean hasAssignedAncestor(Category category, Map<Long, Category> categoriesById,
                                        Set<Long> assignedCategoryIds) {
        Long parentId = category.getParentId();
        for (int level = 0; parentId != null && level < MAX_CATEGORY_DEPTH; level++) {
            if (assignedCategoryIds.contains(parentId)) {
                return true;
            }
            Category parent = categoriesById.get(parentId);
            parentId = parent == null ? null : parent.getParentId();
        }
        return false;
    }

    // Chuyển chuyên mục thành nút cây phạm vi.
    private ScopeCategory toScopeCategory(Category category, List<Category> children) {
        return new ScopeCategory(category.getId(), category.getCode(), category.getName(),
                children.stream().map(child -> toScopeCategory(child, List.of())).toList());
    }

    // Tạo tên chuyên mục kèm tên chuyên mục cha.
    private String displayCategoryName(Category category, Map<Long, Category> categoriesById) {
        if (category.getParentId() == null) {
            return category.getName();
        }
        Category parent = categoriesById.get(category.getParentId());
        return parent == null ? category.getName() : parent.getName() + " › " + category.getName();
    }

    // Lấy phòng ban theo các mã được yêu cầu.
    private Map<Long, Department> departmentsById(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return departmentRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Department::getId, department -> department));
    }

    // Lấy chuyên mục theo các mã được yêu cầu.
    private Map<Long, Category> categoriesById(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return categoryRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Category::getId, category -> category));
    }

    // Chép cấu hình hồ sơ Manager vào biểu mẫu.
    private static void applyProfileToForm(ManagerProfile profile, ManagerScopeForm form) {
        form.setEmployeeCode(profile.getEmployeeCode());
        form.setJobTitle(profile.getJobTitle());
        form.setDepartmentId(profile.getDepartmentId());
        form.setBio(profile.getBio());
        form.setMaxConcurrentTickets(profile.getMaxConcurrentTickets());
        form.setMaxConcurrentChats(profile.getMaxConcurrentChats());
    }

    // Áp dụng biểu mẫu lên hồ sơ Manager.
    private static void applyFormToProfile(ManagerScopeForm form, ManagerProfile profile) {
        profile.setEmployeeCode(clean(form.getEmployeeCode()));
        profile.setJobTitle(clean(form.getJobTitle()));
        profile.setDepartmentId(form.getDepartmentId());
        profile.setBio(clean(form.getBio()));
        profile.setMaxConcurrentTickets(form.getMaxConcurrentTickets());
        profile.setMaxConcurrentChats(form.getMaxConcurrentChats());
    }

    // Tạo chuỗi mã chuyên mục để ghi nhật ký.
    private String categoryCodes(Collection<Long> categoryIds) {
        return categoriesById(categoryIds).values().stream()
                .map(Category::getCode)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList()
                .toString();
    }

    // Chuẩn hóa chuỗi rỗng thành null và cắt khoảng trắng.
    private static String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public record ManagerListItem(Long managerId, String fullName, String email, UserStatus userStatus,
                                  String departmentName, List<String> categoryNames, int openTicketCount,
                                  AvailabilityStatus availabilityStatus) {
    }

    public record ManagerHeader(Long managerId, String fullName, String email, UserStatus userStatus) {
    }

    public record ScopeDepartment(Long departmentId, String departmentName, boolean departmentActive,
                                  List<ScopeCategory> categories) {
    }

    public record ScopeCategory(Long categoryId, String code, String name, List<ScopeCategory> children) {
    }

    public record UncoveredCategory(Long categoryId, String departmentName, String code, String name) {
    }
}
