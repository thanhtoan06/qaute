package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.entity.knowledge.FaqTag;
import vn.edu.hcmute.qaute.repository.knowledge.FaqTagRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;

@ExtendWith(MockitoExtension.class)
class AdminTagServiceTest {
    @Mock FaqTagRepository tags;
    @Mock EntityManager entityManager;
    @Mock AuditService audit;
    @Mock Query insert;
    @Mock Query delete;
    @InjectMocks AdminTagService service;

    @Test
    void mergeMovesLinksBeforeDeletingSourceTag() {
        FaqTag source = FaqTag.builder().name("Nguồn").slug("nguon").build();
        source.setId(1L);
        FaqTag target = FaqTag.builder().name("Đích").slug("dich").build();
        target.setId(2L);
        when(tags.findById(1L)).thenReturn(Optional.of(source));
        when(tags.findById(2L)).thenReturn(Optional.of(target));
        when(entityManager.createNativeQuery(org.mockito.ArgumentMatchers.startsWith("INSERT IGNORE")))
                .thenReturn(insert);
        when(entityManager.createNativeQuery(org.mockito.ArgumentMatchers.startsWith("DELETE FROM faq_tag_map")))
                .thenReturn(delete);
        when(insert.setParameter("target", 2L)).thenReturn(insert);
        when(insert.setParameter("source", 1L)).thenReturn(insert);
        when(delete.setParameter("source", 1L)).thenReturn(delete);

        service.merge(1L, 2L);

        var order = inOrder(insert, delete, tags);
        order.verify(insert).executeUpdate();
        order.verify(delete).executeUpdate();
        order.verify(tags).delete(source);
    }

    @Test
    void cannotMergeTagIntoItself() {
        assertThrows(BadRequestException.class, () -> service.merge(1L, 1L));
    }
}
