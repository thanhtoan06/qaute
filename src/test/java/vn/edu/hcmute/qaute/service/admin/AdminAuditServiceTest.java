package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.request.admin.AuditFilter;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.governance.AuditLog;
import vn.edu.hcmute.qaute.repository.governance.AuditLogRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;

@ExtendWith(MockitoExtension.class)
class AdminAuditServiceTest {
    @Mock AuditLogRepository logs;
    @Mock UserLookupService users;
    @InjectMocks AdminAuditService service;

    @Test
    void searchResolvesActorNamesInOneBatch() {
        AuditLog first = row(1L, 7L, "USER_CREATED", "Tạo tài khoản");
        AuditLog second = row(2L, 7L, "USER_UPDATED", "Sửa tài khoản");
        when(logs.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(first, second)));
        when(users.getSummaries(any())).thenReturn(Map.of(7L,
                new UserSummary(7L, "Nguyễn An", "an@example.com", null, null, null, null)));

        var result = service.search(new AuditFilter(), PageRequest.of(0, 25));

        assertEquals(2, result.getTotalElements());
        assertEquals("Nguyễn An", result.getContent().get(0).actorName());
        assertEquals("Nguyễn An", result.getContent().get(1).actorName());
        verify(users).getSummaries(java.util.Set.of(7L));
    }

    @Test
    void exportIsLimitedAndEscapesSpreadsheetFormulas() {
        AuditLog log = row(3L, null, "REPORT_EXPORT", "=SUM(1,2)");
        when(logs.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(log)));

        byte[] bytes = service.exportCsv(new AuditFilter());

        String csv = new String(bytes, StandardCharsets.UTF_8);
        assertTrue(csv.startsWith("\uFEFF"));
        assertTrue(csv.contains("\"'=SUM(1,2)\""));
        ArgumentCaptor<Pageable> page = ArgumentCaptor.forClass(Pageable.class);
        verify(logs).findAll(any(Specification.class), page.capture());
        assertEquals(5000, page.getValue().getPageSize());
    }

    @Test
    void invertedDateRangeIsRejected() {
        AuditFilter filter = new AuditFilter();
        filter.setFrom(LocalDate.of(2026, 10, 22));
        filter.setTo(LocalDate.of(2026, 10, 21));
        assertThrows(BadRequestException.class,
                () -> service.search(filter, PageRequest.of(0, 25)));
    }

    private static AuditLog row(Long id, Long actorId, String action, String after) {
        AuditLog log = AuditLog.builder().actorId(actorId).action(action).summaryAfter(after).build();
        log.setId(id);
        return log;
    }
}
