package vn.edu.hcmute.qaute.service.admin;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.PageUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.request.admin.UserFilter;
import vn.edu.hcmute.qaute.dto.view.AdminUserDetailView;
import vn.edu.hcmute.qaute.dto.view.AdminUserListItem;
import vn.edu.hcmute.qaute.entity.identity.Faculty;
import vn.edu.hcmute.qaute.entity.identity.StudentProfile;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.entity.organization.Department;
import vn.edu.hcmute.qaute.entity.organization.ManagerCategory;
import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;
import vn.edu.hcmute.qaute.repository.identity.FacultyRepository;
import vn.edu.hcmute.qaute.repository.identity.StudentProfileRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerCategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerProfileRepository;

/** Read-only queries for the administrator user directory (ADM-02). */
@Service
@Transactional(readOnly = true)
public class AdminUserQueryService {

    public static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("createdAt", "fullName", "lastLoginAt");
    public static final String DEFAULT_SORT_FIELD = "createdAt";

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final FacultyRepository facultyRepository;
    private final ManagerProfileRepository managerProfileRepository;
    private final DepartmentRepository departmentRepository;
    private final ManagerCategoryRepository managerCategoryRepository;
    private final CategoryRepository categoryRepository;
    @PersistenceContext
    private EntityManager entityManager;

    // Khởi tạo dịch vụ truy vấn danh sách người dùng quản trị.
    public AdminUserQueryService(UserRepository userRepository,
                                 StudentProfileRepository studentProfileRepository,
                                 FacultyRepository facultyRepository,
                                 ManagerProfileRepository managerProfileRepository,
                                 DepartmentRepository departmentRepository,
                                 ManagerCategoryRepository managerCategoryRepository,
                                 CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.facultyRepository = facultyRepository;
        this.managerProfileRepository = managerProfileRepository;
        this.departmentRepository = departmentRepository;
        this.managerCategoryRepository = managerCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    /** Searches users by identity fields, role, status, and inclusive account-created dates. */
    // Tìm người dùng theo bộ lọc và phân trang.
    public Page<AdminUserListItem> search(UserFilter filter, Pageable pageable) {
        UserFilter safeFilter = filter == null ? new UserFilter() : filter;
        return userRepository.findAll(specification(safeFilter), safePageable(safeFilter, pageable))
                .map(this::toListItem);
    }

    /** Returns account details and the role-specific profile, or a 404 when the account is absent. */
    // Lấy thông tin tài khoản cùng hồ sơ theo vai trò.
    public AdminUserDetailView getDetail(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        StudentProfile studentProfile = null;
        Faculty faculty = null;
        ManagerProfile managerProfile = null;
        Department department = null;
        List<String> categoryNames = List.of();

        RoleCode role = roleOf(user);
        if (role == RoleCode.STUDENT) {
            studentProfile = studentProfileRepository.findByUserId(user.getId()).orElse(null);
            if (studentProfile != null && studentProfile.getFacultyId() != null) {
                faculty = facultyRepository.findById(studentProfile.getFacultyId()).orElse(null);
            }
        } else if (role == RoleCode.MANAGER) {
            managerProfile = managerProfileRepository.findByUserId(user.getId()).orElse(null);
            if (managerProfile != null && managerProfile.getDepartmentId() != null) {
                department = departmentRepository.findById(managerProfile.getDepartmentId()).orElse(null);
            }
            categoryNames = categoryNames(user.getId());
        }

        return new AdminUserDetailView(
                user.getId(), user.getFullName(), user.getEmail(), user.getMssv(), user.getPhone(),
                role, user.getStatus(), user.getLastLoginAt(), user.getCreatedAt(),
                faculty == null ? null : faculty.getName(),
                studentProfile == null ? null : studentProfile.getClassCode(),
                studentProfile == null ? null : studentProfile.getCohort(),
                studentProfile == null ? null : studentProfile.getProgram(),
                department == null ? null : department.getName(),
                managerProfile == null ? null : managerProfile.getJobTitle(),
                categoryNames, relatedTicketCount(user, role));
    }

    // Tạo điều kiện truy vấn người dùng từ bộ lọc.
    private Specification<User> specification(UserFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new java.util.ArrayList<>();
            String keyword = clean(filter.getQ());
            if (keyword != null) {
                String pattern = "%" + TextUtil.escapeLike(keyword).toLowerCase(java.util.Locale.ROOT) + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), pattern, '\\'),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern, '\\'),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("mssv")), pattern, '\\')));
            }
            if (filter.getRole() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.join("role", JoinType.INNER).get("code"), filter.getRole()));
            }
            if (filter.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }
            if (filter.getFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"), filter.getFrom().atStartOfDay()));
            }
            if (filter.getTo() != null) {
                LocalDateTime nextDay = filter.getTo().plusDays(1).atStartOfDay();
                predicates.add(criteriaBuilder.lessThan(root.get("createdAt"), nextDay));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    // Tạo phân trang an toàn với trường sắp xếp cho phép.
    private Pageable safePageable(UserFilter filter, Pageable pageable) {
        int page = pageable == null ? 0 : pageable.getPageNumber();
        int size = pageable == null ? 15 : pageable.getPageSize();
        String requestedSort = filter.getSort();
        return PageUtil.of(page, size, requestedSort, !"fullName".equals(requestedSort),
                ALLOWED_SORT_FIELDS, DEFAULT_SORT_FIELD);
    }

    // Chuyển tài khoản thành mục hiển thị trong danh sách.
    private AdminUserListItem toListItem(User user) {
        return new AdminUserListItem(user.getId(), user.getFullName(), user.getEmail(), user.getMssv(),
                roleOf(user), user.getStatus(), user.getLastLoginAt(), user.getCreatedAt());
    }

    // Lấy tên chuyên mục được giao cho Manager.
    private List<String> categoryNames(Long managerId) {
        List<Long> categoryIds = managerCategoryRepository.findByManagerId(managerId).stream()
                .map(ManagerCategory::getCategoryId)
                .toList();
        if (categoryIds.isEmpty()) {
            return List.of();
        }
        Map<Long, Category> categoriesById = categoryRepository.findAllById(categoryIds).stream()
                .collect(Collectors.toMap(Category::getId, category -> category));
        return categoryIds.stream()
                .map(categoriesById::get)
                .filter(java.util.Objects::nonNull)
                .map(Category::getName)
                .sorted(Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    // Đếm ticket liên quan đến tài khoản theo vai trò.
    private int relatedTicketCount(User user, RoleCode role) {
        // The Ticket entity is supplied by the student module in a later integration step.
        boolean ticketAvailable = entityManager.getMetamodel().getEntities().stream()
                .anyMatch(entity -> "Ticket".equals(entity.getName()));
        if (!ticketAvailable || (role != RoleCode.STUDENT && role != RoleCode.MANAGER)) {
            return 0;
        }
        String ownerField = role == RoleCode.STUDENT ? "studentId" : "currentAssigneeId";
        Long count = entityManager.createQuery(
                        "select count(t) from Ticket t where t." + ownerField + " = :userId", Long.class)
                .setParameter("userId", user.getId())
                .getSingleResult();
        return Math.toIntExact(count);
    }

    // Lấy mã vai trò của tài khoản.
    private static RoleCode roleOf(User user) {
        return user.getRole() == null ? null : user.getRole().getCode();
    }

    // Cắt khoảng trắng và chuyển chuỗi rỗng thành null.
    private static String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
