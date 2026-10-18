package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.dto.request.admin.AdminFaqFilter;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;
import vn.edu.hcmute.qaute.repository.governance.AdminFaqRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@ExtendWith(MockitoExtension.class)
class AdminFaqServiceTest {
    @Mock AdminFaqRepository faqs;
    @Mock CategoryQueryService categories;
    @Mock UserRepository users;
    @InjectMocks AdminFaqService service;

    @Test
    void searchUsesDescendantsAndWhitelistedSortThenMapsNames() {
        AdminFaqFilter filter = new AdminFaqFilter();
        filter.setCategoryId(10L);
        filter.setSort("untrustedField");
        when(categories.getDescendantIds(10L)).thenReturn(List.of(10L, 11L));
        Faq faq = Faq.builder().categoryId(11L).authorId(7L).title("Học phí")
                .slug("hoc-phi").status(FaqStatus.DRAFT).build();
        faq.setId(1L);
        when(faqs.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(faq)));
        User author = User.builder().fullName("Nguyễn An").build();
        author.setId(7L);
        when(users.findAllById(List.of(7L))).thenReturn(List.of(author));
        when(categories.getDisplayPath(11L)).thenReturn("Đào tạo › Học phí");

        var result = service.search(filter, PageRequest.of(0, 15));

        assertEquals("Nguyễn An", result.getContent().getFirst().authorName());
        assertEquals("Đào tạo › Học phí", result.getContent().getFirst().categoryPath());
        verify(categories).getDescendantIds(10L);
        ArgumentCaptor<Pageable> pageable = ArgumentCaptor.forClass(Pageable.class);
        verify(faqs).findAll(any(Specification.class), pageable.capture());
        assertEquals("updatedAt", pageable.getValue().getSort().iterator().next().getProperty());
        assertFalse(pageable.getValue().getSort().iterator().next().isAscending());
    }

    @Test
    void countIncludesStatusesWithoutRows() {
        when(faqs.countGroupedByStatus()).thenReturn(List.<Object[]>of(
                new Object[]{FaqStatus.DRAFT, 3L}, new Object[]{FaqStatus.PUBLISHED, 2L}));

        Map<FaqStatus, Long> counts = service.countByStatus();

        assertEquals(3L, counts.get(FaqStatus.DRAFT));
        assertEquals(2L, counts.get(FaqStatus.PUBLISHED));
        assertEquals(0L, counts.get(FaqStatus.PENDING_REVIEW));
        assertEquals(0L, counts.get(FaqStatus.ARCHIVED));
    }
}
