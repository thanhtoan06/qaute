package vn.edu.hcmute.qaute.service.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.admin.CategoryForm;
import vn.edu.hcmute.qaute.dto.view.AdminCategoryTree;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.dto.view.DepartmentOption;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.entity.organization.Department;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.organization.TicketUsageGuard;

/**
 * Quản trị chuyên mục (ADM-06). Cây tối đa 2 cấp, không xóa cứng.
 */
@Service
@Transactional
public class AdminCategoryService {

    private static final int ROOT_DEPTH = 1;
    private static final int MAX_DEPTH = 2;

    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;
    private final TicketUsageGuard ticketUsageGuard;
    private final AuditService auditService;

    @PersistenceContext
    private EntityManager entityManager;

    // Khởi tạo dịch vụ quản trị chuyên mục.
    public AdminCategoryService(DepartmentRepository departmentRepository,
                                CategoryRepository categoryRepository,
                                TicketUsageGuard ticketUsageGuard,
                                AuditService auditService) {
        this.departmentRepository = departmentRepository;
        this.categoryRepository = categoryRepository;
        this.ticketUsageGuard = ticketUsageGuard;
        this.auditService = auditService;
    }

    // ------------------------------------------------------------------
    // Đọc
    // ------------------------------------------------------------------

    /** Cây theo phòng ban, kể cả phòng ban/chuyên mục đã tắt. departmentId null = tất cả. */
    // Tạo cây chuyên mục theo phòng ban.
    @Transactional(readOnly = true)
    public List<AdminCategoryTree> tree(Long departmentId) {
        List<AdminCategoryTree> result = new ArrayList<>();
        for (Department department : departmentRepository.findAllByOrderBySortOrderAscNameAsc()) {
            if (departmentId != null && !departmentId.equals(department.getId())) {
                continue;
            }
            List<Category> roots = new ArrayList<>();
            Map<Long, List<Category>> childrenByParent = new HashMap<>();
            for (Category category : categoryRepository
                    .findByDepartmentIdOrderBySortOrderAscNameAsc(department.getId())) {
                if (category.getDepth() == ROOT_DEPTH || category.getParentId() == null) {
                    roots.add(category);
                } else {
                    childrenByParent.computeIfAbsent(category.getParentId(), key -> new ArrayList<>())
                            .add(category);
                }
            }
            List<AdminCategoryTree.Node> nodes = new ArrayList<>();
            for (Category root : roots) {
                List<Category> children = childrenByParent.getOrDefault(root.getId(), List.of());
                List<Long> allIds = new ArrayList<>();
                allIds.add(root.getId());
                children.forEach(child -> allIds.add(child.getId()));
                List<AdminCategoryTree.Node> childNodes = children.stream()
                        .map(child -> toNode(child, List.of(child.getId()), List.of()))
                        .toList();
                nodes.add(toNode(root, allIds, childNodes));
            }
            result.add(new AdminCategoryTree(department.getId(), department.getCode(),
                    department.getName(), department.isActive(), nodes));
        }
        return result;
    }

    // Lấy biểu mẫu của chuyên mục cần sửa.
    @Transactional(readOnly = true)
    public CategoryForm get(Long id) {
        return toForm(requireCategory(id));
    }

    /** Form trống cho trang thêm mới; có parentId hợp lệ thì tự chọn phòng ban theo cha. */
    // Tạo biểu mẫu chuyên mục mới với phòng ban hoặc cha mặc định.
    @Transactional(readOnly = true)
    public CategoryForm newForm(Long departmentId, Long parentId) {
        CategoryForm form = new CategoryForm();
        if (parentId != null) {
            categoryRepository.findById(parentId).ifPresent(parent -> {
                if (parent.getDepth() == ROOT_DEPTH) {
                    form.setParentId(parent.getId());
                    form.setDepartmentId(parent.getDepartmentId());
                }
            });
        }
        if (form.getDepartmentId() == null) {
            form.setDepartmentId(departmentId);
        }
        return form;
    }

    // Liệt kê phòng ban để chọn trong biểu mẫu.
    @Transactional(readOnly = true)
    public List<DepartmentOption> departmentOptions() {
        return departmentRepository.findAllByOrderBySortOrderAscNameAsc().stream()
                .map(d -> new DepartmentOption(d.getId(), d.getName(), d.isActive()))
                .toList();
    }

    /** Các chuyên mục cấp 1 có thể làm cha (mọi phòng ban, bỏ chính nó); JS lọc theo phòng ban. */
    // Liệt kê chuyên mục cấp một có thể làm cha.
    @Transactional(readOnly = true)
    public List<CategoryView> parentOptions(Long excludeId) {
        List<CategoryView> options = new ArrayList<>();
        for (Department department : departmentRepository.findAllByOrderBySortOrderAscNameAsc()) {
            for (Category category : categoryRepository
                    .findByDepartmentIdOrderBySortOrderAscNameAsc(department.getId())) {
                if (category.getDepth() == ROOT_DEPTH && !category.getId().equals(excludeId)) {
                    options.add(new CategoryView(category.getId(), department.getId(),
                            department.getName(), null, category.getCode(), category.getName(),
                            category.getIcon(), category.getDepth(), category.isActive()));
                }
            }
        }
        return options;
    }

    // ------------------------------------------------------------------
    // Ghi
    // ------------------------------------------------------------------

    // Tạo hoặc cập nhật chuyên mục từ biểu mẫu.
    public CategoryForm save(CategoryForm form, Long actorId) {
        String code = form.getCode() == null ? null : form.getCode().trim().toUpperCase();
        departmentRepository.findById(form.getDepartmentId())
                .orElseThrow(() -> new BadRequestException("Phòng ban không tồn tại"));
        Category existing = form.getId() == null ? null : requireCategory(form.getId());

        int depth = resolveDepth(form, existing);

        boolean duplicated = existing == null
                ? categoryRepository.existsByCode(code)
                : categoryRepository.existsByCodeAndIdNot(code, existing.getId());
        if (duplicated) {
            throw new DuplicateException("code", "Mã chuyên mục đã được sử dụng");
        }

        if (existing == null) {
            Category category = new Category();
            applyForm(category, form, code, depth);
            Category saved = categoryRepository.save(category);
            auditService.log(AuditActions.CATEGORY_CREATED, "category", saved.getId(),
                    null, summarize(saved));
            return toForm(saved);
        }

        boolean hasChildren = !categoryRepository.findByParentId(existing.getId()).isEmpty();
        if (hasChildren && depth != ROOT_DEPTH) {
            throw new BadRequestException(
                    "Chuyên mục còn chủ đề con nên không thể chuyển thành chủ đề con");
        }
        if (hasChildren && !existing.getDepartmentId().equals(form.getDepartmentId())) {
            throw new BadRequestException(
                    "Chuyên mục còn chủ đề con nên không thể đổi phòng ban");
        }
        if (existing.isActive() && !form.isActive()) {
            ensureCanDisable(existing);
        }
        String before = summarize(existing);
        applyForm(existing, form, code, depth);
        Category saved = categoryRepository.save(existing);
        auditService.log(AuditActions.CATEGORY_UPDATED, "category", saved.getId(),
                before, summarize(saved));
        return toForm(saved);
    }

    // Đổi trạng thái hoạt động của chuyên mục.
    public void toggleActive(Long id, Long actorId) {
        Category category = requireCategory(id);
        boolean before = category.isActive();
        if (before) {
            ensureCanDisable(category);
        }
        category.setActive(!before);
        categoryRepository.save(category);
        auditService.log(AuditActions.CATEGORY_TOGGLED, "category", id,
                "active=" + before, "active=" + (!before));
    }

    // ------------------------------------------------------------------
    // Quy tắc
    // ------------------------------------------------------------------

    /** Tính depth mới; kiểm tra cha cùng phòng ban, tối đa 2 cấp và chống vòng lặp. */
    // Tính cấp chuyên mục và kiểm tra quan hệ cha.
    private int resolveDepth(CategoryForm form, Category existing) {
        Long parentId = form.getParentId();
        if (parentId == null) {
            return ROOT_DEPTH;
        }
        if (existing != null && parentId.equals(existing.getId())) {
            throw new BadRequestException("Không thể chọn chính chuyên mục này làm chuyên mục cha");
        }
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new BadRequestException("Chuyên mục cha không tồn tại"));
        if (!parent.getDepartmentId().equals(form.getDepartmentId())) {
            throw new BadRequestException("Chuyên mục cha phải thuộc cùng phòng ban");
        }
        if (existing != null && isDescendantOf(parent, existing.getId())) {
            throw new BadRequestException("Không thể chọn chuyên mục con làm chuyên mục cha");
        }
        int depth = parent.getDepth() + 1;
        if (depth > MAX_DEPTH) {
            throw new BadRequestException("Chỉ hỗ trợ tối đa 2 cấp chuyên mục");
        }
        return depth;
    }

    /** candidate có nằm dưới ancestorId không. Vòng lặp bị chặn bởi MAX_DEPTH nên dữ liệu lỗi cũng không treo. */
    // Kiểm tra chuyên mục có nằm dưới tổ tiên chỉ định không.
    private boolean isDescendantOf(Category candidate, Long ancestorId) {
        Category current = candidate;
        for (int level = 0; level <= MAX_DEPTH && current != null; level++) {
            Long parentId = current.getParentId();
            if (parentId == null) {
                return false;
            }
            if (parentId.equals(ancestorId)) {
                return true;
            }
            current = categoryRepository.findById(parentId).orElse(null);
        }
        return false;
    }

    /** Dùng chung cho toggle và cho trường hợp bỏ tích "active" trong form sửa. */
    // Không tắt chuyên mục cha khi vẫn còn chủ đề con đang bật.
    private void ensureCanDisable(Category category) {
        List<Category> children = categoryRepository.findByParentId(category.getId());
        long activeChildren = children.stream().filter(Category::isActive).count();
        if (activeChildren > 0) {
            throw new BadRequestException("Còn " + activeChildren
                    + " chủ đề con đang bật, hãy tắt chúng trước");
        }
    }

    // ------------------------------------------------------------------
    // Hỗ trợ
    // ------------------------------------------------------------------

    // Lấy chuyên mục hoặc báo không tìm thấy.
    private Category requireCategory(Long id) {
        if (id == null) {
            throw new NotFoundException("Không tìm thấy chuyên mục");
        }
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy chuyên mục"));
    }

    // Áp dụng dữ liệu biểu mẫu lên chuyên mục.
    private void applyForm(Category category, CategoryForm form, String code, int depth) {
        category.setDepartmentId(form.getDepartmentId());
        category.setParentId(form.getParentId());
        category.setDepth(depth);
        category.setCode(code);
        category.setName(form.getName() == null ? null : form.getName().trim());
        category.setDescription(clean(form.getDescription()));
        category.setIcon(clean(form.getIcon()));
        category.setSortOrder(form.getSortOrder());
        category.setActive(form.isActive());
    }

    // Chuyển chuyên mục thành biểu mẫu chỉnh sửa.
    private CategoryForm toForm(Category category) {
        CategoryForm form = new CategoryForm();
        form.setId(category.getId());
        form.setDepartmentId(category.getDepartmentId());
        form.setParentId(category.getParentId());
        form.setCode(category.getCode());
        form.setName(category.getName());
        form.setDescription(category.getDescription());
        form.setIcon(category.getIcon());
        form.setSortOrder(category.getSortOrder());
        form.setActive(category.isActive());
        return form;
    }

    // Tạo nút cây kèm số liệu chuyên mục.
    private AdminCategoryTree.Node toNode(Category category, List<Long> countIds,
                                           List<AdminCategoryTree.Node> children) {
        return new AdminCategoryTree.Node(
                category.getId(), category.getParentId(), category.getCode(), category.getName(),
                category.getIcon(), category.getDepth(), category.getSortOrder(), category.isActive(),
                countFaqs(countIds),
                ticketUsageGuard.countOpenTicketsByCategoryIds(countIds),
                children);
    }

    /**
     * Đếm FAQ theo chuyên mục. Dự án chưa có entity FAQ nên kiểm tra metamodel trước,
     * chưa có (hoặc chưa có trường categoryId) thì trả 0, không chạy JPQL lỗi.
     * Khi có entity FAQ, đổi tên "Faq" bên dưới nếu entity đặt tên khác.
     */
    // Đếm FAQ thuộc các chuyên mục được chỉ định.
    private long countFaqs(Collection<Long> categoryIds) {
        if (categoryIds.isEmpty() || !faqEntityHasCategoryId()) {
            return 0;
        }
        return entityManager.createQuery(
                        "select count(f) from Faq f where f.categoryId in :ids", Long.class)
                .setParameter("ids", categoryIds)
                .getSingleResult();
    }

    // Kiểm tra entity FAQ có thuộc tính categoryId.
    private boolean faqEntityHasCategoryId() {
        return entityManager.getMetamodel().getEntities().stream()
                .filter(entity -> "Faq".equals(entity.getName()))
                .anyMatch(entity -> entity.getAttributes().stream()
                        .anyMatch(attribute -> "categoryId".equals(attribute.getName())));
    }

    // Tạo nội dung tóm tắt chuyên mục để ghi nhật ký.
    private static String summarize(Category category) {
        return "code=" + category.getCode() + ", name=" + category.getName()
                + ", departmentId=" + category.getDepartmentId()
                + ", parentId=" + category.getParentId()
                + ", depth=" + category.getDepth()
                + ", active=" + category.isActive();
    }

    // Chuẩn hóa chuỗi rỗng thành null và cắt khoảng trắng.
    private static String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
