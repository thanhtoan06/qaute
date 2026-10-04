package vn.edu.hcmute.qaute.service.organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.edu.hcmute.qaute.dto.view.CategoryNode;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.dto.view.DepartmentNode;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.entity.organization.Department;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;

@Service
@Transactional(readOnly = true)
public class CategoryQueryServiceImpl implements CategoryQueryService {

    /** Chuyên mục cấp 1 (con trực tiếp của phòng ban). */
    private static final int ROOT_DEPTH = 1;

    /** Cây tối đa 2 cấp: chuyên mục (1) → chủ đề (2). */
    private static final int MAX_DEPTH = 2;

    /** Dấu phân cách đường dẫn: " › " (viết bằng mã Unicode để không phụ thuộc encoding file). */
    private static final String PATH_SEPARATOR = " \u203A ";

    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;

    public CategoryQueryServiceImpl(DepartmentRepository departmentRepository,
                                    CategoryRepository categoryRepository) {
        this.departmentRepository = departmentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<DepartmentNode> getActiveTree() {
        ActiveIndex index = loadActiveIndex();
        List<DepartmentNode> tree = new ArrayList<>(index.departments().size());
        for (Department department : index.departments()) {
            List<CategoryNode> categories = new ArrayList<>();
            for (Category root : index.rootsOf(department.getId())) {
                categories.add(toNode(root, index.childrenOf(root.getId())));
            }
            tree.add(new DepartmentNode(
                    department.getId(),
                    department.getCode(),
                    department.getName(),
                    department.getDescription(),
                    department.getEmail(),
                    department.getPhone(),
                    department.getLocation(),
                    List.copyOf(categories)));
        }
        return List.copyOf(tree);
    }

    @Override
    public Optional<CategoryView> findCategory(Long categoryId) {
        if (categoryId == null) {
            return Optional.empty();
        }
        return categoryRepository.findById(categoryId)
                .map(category -> toView(category, findDepartment(category)));
    }

    @Override
    public List<Long> getDescendantIds(Long categoryId) {
        if (categoryId == null) {
            return List.of();
        }
        Optional<Category> found = categoryRepository.findById(categoryId);
        if (found.isEmpty()) {
            return List.of();
        }
        Category self = found.get();
        Set<Long> ids = new LinkedHashSet<>();
        ids.add(self.getId());
        // Cây chỉ có 2 cấp nên chỉ cấp 1 mới có con; cấp 2 thì dừng, không truy vấn thêm.
        if (self.getDepth() < MAX_DEPTH) {
            for (Category child : categoryRepository.findByParentIdOrderBySortOrderAscNameAsc(self.getId())) {
                ids.add(child.getId());
            }
        }
        return List.copyOf(ids);
    }

    @Override
    public String getDisplayPath(Long categoryId) {
        if (categoryId == null) {
            return "";
        }
        return categoryRepository.findById(categoryId)
                .map(this::buildDisplayPath)
                .orElse("");
    }

    @Override
    public List<CategoryView> findAllActiveLeaf() {
        ActiveIndex index = loadActiveIndex();
        List<CategoryView> leaves = new ArrayList<>();
        for (Department department : index.departments()) {
            for (Category root : index.rootsOf(department.getId())) {
                List<Category> activeChildren = index.childrenOf(root.getId());
                if (activeChildren.isEmpty()) {
                    // Chuyên mục cấp 1 không có chủ đề con active → chính nó là lá.
                    leaves.add(toView(root, department));
                } else {
                    for (Category child : activeChildren) {
                        leaves.add(toView(child, department));
                    }
                }
            }
        }
        return List.copyOf(leaves);
    }

    @Override
    public List<Long> getAncestorIdsIncludingSelf(Long categoryId) {
        if (categoryId == null) {
            return List.of();
        }
        return categoryRepository.findById(categoryId)
                .map(category -> loadChainFromRoot(category).stream().map(Category::getId).toList())
                .orElse(List.of());
    }

    // ------------------------------------------------------------------
    // Hàm hỗ trợ
    // ------------------------------------------------------------------

    /**
     * Nạp dữ liệu đang hoạt động bằng đúng 2 truy vấn rồi gom nhóm trong bộ nhớ (tránh N+1).
     * Thứ tự sortOrder → tên được giữ nguyên từ repository.
     */
    private ActiveIndex loadActiveIndex() {
        List<Department> departments = departmentRepository.findByActiveTrueOrderBySortOrderAscNameAsc();
        Map<Long, List<Category>> rootsByDepartment = new HashMap<>();
        Map<Long, List<Category>> childrenByParent = new HashMap<>();
        if (!departments.isEmpty()) {
            for (Category category : categoryRepository.findByActiveTrueOrderBySortOrderAscNameAsc()) {
                if (category.getDepth() == ROOT_DEPTH) {
                    rootsByDepartment
                            .computeIfAbsent(category.getDepartmentId(), key -> new ArrayList<>())
                            .add(category);
                } else if (category.getParentId() != null) {
                    childrenByParent
                            .computeIfAbsent(category.getParentId(), key -> new ArrayList<>())
                            .add(category);
                }
            }
        }
        return new ActiveIndex(departments, rootsByDepartment, childrenByParent);
    }

    private CategoryNode toNode(Category category, List<Category> activeChildren) {
        List<CategoryNode> children = activeChildren.stream()
                .map(child -> new CategoryNode(
                        child.getId(), child.getCode(), child.getName(), child.getIcon(),
                        child.getDepth(), List.<CategoryNode>of()))
                .toList();
        return new CategoryNode(
                category.getId(), category.getCode(), category.getName(), category.getIcon(),
                category.getDepth(), children);
    }

    private CategoryView toView(Category category, Department department) {
        // "active" của view = chuyên mục bật VÀ phòng ban bật, để khớp với cây đang hoạt động.
        boolean active = isActive(category) && department != null && isActive(department);
        return new CategoryView(
                category.getId(),
                category.getDepartmentId(),
                department != null ? department.getName() : null,
                category.getParentId(),
                category.getCode(),
                category.getName(),
                category.getIcon(),
                category.getDepth(),
                active);
    }

    private Department findDepartment(Category category) {
        return departmentRepository.findById(category.getDepartmentId()).orElse(null);
    }

    private String buildDisplayPath(Category category) {
        List<String> parts = new ArrayList<>(MAX_DEPTH + 1);
        Department department = findDepartment(category);
        if (department != null) {
            parts.add(department.getName());
        }
        for (Category node : loadChainFromRoot(category)) {
            parts.add(node.getName());
        }
        return String.join(PATH_SEPARATOR, parts);
    }

    /** Chuỗi từ chuyên mục gốc xuống chính nó; vòng lặp bị chặn bởi MAX_DEPTH nên dữ liệu lỗi cũng không treo. */
    private List<Category> loadChainFromRoot(Category self) {
        LinkedList<Category> chain = new LinkedList<>();
        Category current = self;
        for (int level = 0; level < MAX_DEPTH && current != null; level++) {
            chain.addFirst(current);
            Long parentId = current.getParentId();
            current = (parentId == null) ? null : categoryRepository.findById(parentId).orElse(null);
        }
        return chain;
    }

    // Nếu entity khai báo kiểu Boolean (không phải boolean) thì đổi thành
    // Boolean.TRUE.equals(category.getActive()) / Boolean.TRUE.equals(department.getActive()).
    private static boolean isActive(Category category) {
        return category.isActive();
    }

    private static boolean isActive(Department department) {
        return department.isActive();
    }

    /** Chỉ mục dữ liệu đang hoạt động, dùng chung cho getActiveTree và findAllActiveLeaf. */
    private record ActiveIndex(
            List<Department> departments,
            Map<Long, List<Category>> rootsByDepartment,
            Map<Long, List<Category>> childrenByParent) {

        List<Category> rootsOf(Long departmentId) {
            return rootsByDepartment.getOrDefault(departmentId, List.of());
        }

        List<Category> childrenOf(Long parentId) {
            return childrenByParent.getOrDefault(parentId, List.of());
        }
    }
}
