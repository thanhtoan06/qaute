package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.request.admin.DepartmentForm;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.entity.organization.Department;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.organization.TicketUsageGuard;

@ExtendWith(MockitoExtension.class)
class AdminDepartmentGuardTest {
    @Mock DepartmentRepository departmentRepository;
    @Mock CategoryRepository categoryRepository;
    @Mock TicketUsageGuard ticketUsageGuard;
    @Mock AuditService auditService;
    @InjectMocks AdminDepartmentService service;

    @Test
    void editingDepartmentCannotBypassOpenTicketGuard() {
        Department department = new Department(); department.setId(1L); department.setActive(true);
        Category category = new Category(); category.setId(2L);
        DepartmentForm form = new DepartmentForm(); form.setId(1L); form.setCode("HOCVU"); form.setActive(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(categoryRepository.findByDepartmentIdOrderBySortOrderAscNameAsc(1L)).thenReturn(List.of(category));
        when(ticketUsageGuard.countOpenTicketsByCategoryIds(List.of(2L))).thenReturn(3L);

        assertThrows(BadRequestException.class, () -> service.save(form, 9L));

        verify(departmentRepository, never()).save(any());
        org.junit.jupiter.api.Assertions.assertTrue(department.isActive());
    }
}
