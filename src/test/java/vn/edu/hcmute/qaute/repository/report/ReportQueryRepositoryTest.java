package vn.edu.hcmute.qaute.repository.report;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;

class ReportQueryRepositoryTest {
    private final EntityManager em = Mockito.mock(EntityManager.class);
    @SuppressWarnings("unchecked")
    private final TypedQuery<Object[]> query = Mockito.mock(TypedQuery.class);
    private final ReportQueryRepository repository = new ReportQueryRepository(em);
    private final ReportRange range = new ReportRange(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2));

    @Test
    void managerScopeIncludesAssignedTicketsOutsideCategories() {
        when(em.createQuery(anyString(), eq(Object[].class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());
        repository.createdPerDay(new ReportScope(7L, Set.of(4L)), range);

        ArgumentCaptor<String> jpql = ArgumentCaptor.forClass(String.class);
        verify(em).createQuery(jpql.capture(), eq(Object[].class));
        assertTrue(jpql.getValue().contains("t.categoryId in :categoryIds or t.currentAssigneeId = :scopeManagerId"));
        verify(query).setParameter("categoryIds", Set.of(4L));
        verify(query).setParameter("scopeManagerId", 7L);
    }

    @Test
    void emptyCategoryScopeCannotSeeSystemTickets() {
        when(em.createQuery(anyString(), eq(Object[].class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());
        repository.createdPerDay(new ReportScope(null, Set.of()), range);

        ArgumentCaptor<String> jpql = ArgumentCaptor.forClass(String.class);
        verify(em).createQuery(jpql.capture(), eq(Object[].class));
        assertTrue(jpql.getValue().contains("where 1 = 0"));
    }
}
