package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.request.admin.SlaPolicyForm;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.entity.governance.SlaPolicy;
import vn.edu.hcmute.qaute.repository.governance.SlaPolicyRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@ExtendWith(MockitoExtension.class)
class AdminSlaServiceTest {
    @Mock SlaPolicyRepository policies;
    @Mock CategoryQueryService categories;
    @Mock AuditService audit;
    @InjectMocks AdminSlaService service;

    @Test
    void formConvertsMinutesToDecimalHours() {
        SlaPolicy low = policy(null, TicketPriority.LOW, 61, 121, true);
        when(policies.findFirstByCategoryIdIsNullAndPriorityOrderByIdAsc(TicketPriority.LOW))
                .thenReturn(Optional.of(low));

        SlaPolicyForm form = service.getForm(null);

        assertEquals(4, form.getRows().size());
        assertEquals(0, form.getRows().get(0).getFirstResponseHours()
                .compareTo(new BigDecimal("1.02")));
        assertEquals(0, form.getRows().get(0).getResolutionHours()
                .compareTo(new BigDecimal("2.02")));
    }

    @Test
    void saveUpsertsFourPrioritiesAndRoundsToMinutes() {
        when(categories.findCategory(10L)).thenReturn(Optional.of(category(10L)));
        SlaPolicy existing = policy(10L, TicketPriority.LOW, 60, 120, true);
        existing.setId(7L);
        when(policies.findByCategoryIdAndPriority(10L, TicketPriority.LOW))
                .thenReturn(Optional.of(existing));
        SlaPolicyForm form = form(10L);
        form.getRows().get(0).setFirstResponseHours(new BigDecimal("1.01"));
        form.getRows().get(0).setResolutionHours(new BigDecimal("2.02"));
        form.getRows().get(0).setActive(false);

        service.save(form, 1L);

        assertEquals(7L, existing.getId());
        assertEquals(61, existing.getFirstResponseMinutes());
        assertEquals(121, existing.getResolutionMinutes());
        assertFalse(existing.isActive());
        verify(policies, org.mockito.Mockito.times(4)).save(any(SlaPolicy.class));
        verify(audit).log(AuditActions.SLA_POLICY_CHANGED, "SlaPolicy", 10L, "", "");
    }

    @Test
    void invalidResolutionIsRejectedBeforeWriting() {
        SlaPolicyForm form = form(null);
        form.getRows().get(2).setResolutionHours(new BigDecimal("0.5"));

        BadRequestException error = assertThrows(BadRequestException.class,
                () -> service.save(form, 1L));

        assertTrue(error.getFieldErrors().containsKey("rows[2].resolutionHours"));
        verify(policies, never()).save(any());
    }

    @Test
    void deleteOverrideNeverDeletesDefaults() {
        assertThrows(BadRequestException.class, () -> service.deleteOverride(null, 1L));
        SlaPolicy override = policy(10L, TicketPriority.LOW, 60, 120, true);
        when(categories.findCategory(10L)).thenReturn(Optional.of(category(10L)));
        when(policies.findByCategoryId(10L)).thenReturn(List.of(override));

        service.deleteOverride(10L, 1L);

        verify(policies).deleteAll(List.of(override));
        verify(audit).log(AuditActions.SLA_POLICY_CHANGED, "SlaPolicy", 10L,
                "LOW=60/120/true", "override deleted");
    }

    private static SlaPolicyForm form(Long categoryId) {
        SlaPolicyForm form = new SlaPolicyForm();
        form.setCategoryId(categoryId);
        for (SlaPolicyForm.Row row : form.getRows()) {
            row.setFirstResponseHours(new BigDecimal("1"));
            row.setResolutionHours(new BigDecimal("2"));
        }
        return form;
    }

    private static SlaPolicy policy(Long categoryId, TicketPriority priority,
                                    int response, int resolution, boolean active) {
        return SlaPolicy.builder().categoryId(categoryId).priority(priority)
                .firstResponseMinutes(response).resolutionMinutes(resolution).active(active).build();
    }

    private static CategoryView category(Long id) {
        return new CategoryView(id, 1L, "Phòng", null, "CODE", "Chuyên mục",
                null, 1, true);
    }
}
