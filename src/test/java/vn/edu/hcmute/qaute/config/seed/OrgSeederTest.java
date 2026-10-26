package vn.edu.hcmute.qaute.config.seed;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
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

class OrgSeederTest {

    @Test
    void upgradesExistingDemoWithoutReplacingOrganizationOrManagerSettings() {
        var departments = mock(DepartmentRepository.class);
        var categories = mock(CategoryRepository.class);
        var profiles = mock(ManagerProfileRepository.class);
        var assignments = mock(ManagerCategoryRepository.class);
        var users = mock(UserRepository.class);
        Map<String, Department> departmentData = new HashMap<>();
        Map<String, Category> categoryData = new HashMap<>();
        Map<Long, ManagerProfile> profileData = new HashMap<>();
        List<ManagerCategory> assignmentData = new ArrayList<>();
        Map<String, User> userData = new HashMap<>();
        String[] names = {"daotao", "ctsv", "taichinh", "ktx", "cntt", "thuvien",
                "qhdn", "khcn", "yte", "csvc", "qlcl", "saudh"};
        for (int i = 0; i < names.length; i++) {
            User user = new User();
            user.setId(i + 1L);
            userData.put("manager." + names[i] + "@qaute.edu.vn", user);
        }
        Department existingDepartment = Department.builder().code("DAOTAO").name("Tên đã chỉnh sửa").build();
        existingDepartment.setId(91L);
        departmentData.put(existingDepartment.getCode(), existingDepartment);
        Category existingCategory = Category.builder().code("DT_DKHP").name("Chuyên mục hiện có")
                .departmentId(91L).depth(1).build();
        existingCategory.setId(92L);
        categoryData.put(existingCategory.getCode(), existingCategory);
        ManagerProfile existingProfile = ManagerProfile.builder().userId(1L).departmentId(91L)
                .maxConcurrentTickets(42).availabilityStatus(AvailabilityStatus.OFFLINE).build();
        profileData.put(1L, existingProfile);
        ManagerCategory existingAssignment = ManagerCategory.builder().managerId(1L).categoryId(92L)
                .level(ManagerCategoryLevel.SECONDARY).build();
        assignmentData.add(existingAssignment);

        when(users.findByEmailIgnoreCase(anyString())).thenAnswer(c -> Optional.ofNullable(userData.get(c.getArgument(0))));
        when(departments.existsByCode(anyString())).thenAnswer(c -> departmentData.containsKey(c.getArgument(0)));
        when(departments.findByCode(anyString())).thenAnswer(c -> Optional.ofNullable(departmentData.get(c.getArgument(0))));
        when(departments.save(any(Department.class))).thenAnswer(c -> {
            Department department = c.getArgument(0);
            department.setId(100L + departmentData.size());
            departmentData.put(department.getCode(), department);
            return department;
        });
        when(categories.findByCode(anyString())).thenAnswer(c -> Optional.ofNullable(categoryData.get(c.getArgument(0))));
        when(categories.existsByCode(anyString())).thenAnswer(c -> categoryData.containsKey(c.getArgument(0)));
        when(categories.save(any(Category.class))).thenAnswer(c -> {
            Category category = c.getArgument(0);
            category.setId(100L + categoryData.size());
            categoryData.put(category.getCode(), category);
            return category;
        });
        when(profiles.findByUserId(anyLong())).thenAnswer(c -> Optional.ofNullable(profileData.get(c.getArgument(0))));
        when(profiles.save(any(ManagerProfile.class))).thenAnswer(c -> {
            ManagerProfile profile = c.getArgument(0);
            assertNull(profileData.putIfAbsent(profile.getUserId(), profile));
            return profile;
        });
        when(assignments.findByManagerId(anyLong())).thenAnswer(c -> assignmentData.stream()
                .filter(a -> a.getManagerId().equals(c.getArgument(0))).toList());
        when(assignments.save(any(ManagerCategory.class))).thenAnswer(c -> {
            ManagerCategory assignment = c.getArgument(0);
            assertTrue(assignmentData.stream().noneMatch(a -> a.getManagerId().equals(assignment.getManagerId())
                    && a.getCategoryId().equals(assignment.getCategoryId())));
            assignmentData.add(assignment);
            return assignment;
        });

        OrgSeeder seeder = new OrgSeeder(departments, categories, profiles, assignments, users);
        seeder.seed();
        int categoryCount = categoryData.size();
        int assignmentCount = assignmentData.size();
        assertEquals(12, departmentData.size());
        assertEquals(12, profileData.size());
        assertNotNull(categoryData.get("DT_DKHP_HUY"));
        assertTrue(assignmentData.stream().anyMatch(a -> a.getManagerId().equals(12L)));

        seeder.seed();
        assertEquals(12, departmentData.size());
        assertEquals(12, profileData.size());
        assertEquals(categoryCount, categoryData.size());
        assertEquals(assignmentCount, assignmentData.size());
        assertSame(existingDepartment, departmentData.get("DAOTAO"));
        assertEquals("Tên đã chỉnh sửa", existingDepartment.getName());
        assertSame(existingCategory, categoryData.get("DT_DKHP"));
        assertSame(existingProfile, profileData.get(1L));
        assertEquals(42, existingProfile.getMaxConcurrentTickets());
        assertEquals(AvailabilityStatus.OFFLINE, existingProfile.getAvailabilityStatus());
        assertEquals(ManagerCategoryLevel.SECONDARY, existingAssignment.getLevel());
    }
}
