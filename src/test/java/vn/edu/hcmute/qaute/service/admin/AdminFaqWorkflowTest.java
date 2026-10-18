package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.ConflictException;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.entity.governance.FaqRevision;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;
import vn.edu.hcmute.qaute.entity.knowledge.FaqTag;
import vn.edu.hcmute.qaute.repository.governance.AdminFaqRepository;
import vn.edu.hcmute.qaute.repository.governance.FaqRevisionRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqTagRepository;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.media.MediaService;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@ExtendWith(MockitoExtension.class)
class AdminFaqWorkflowTest {
    @Mock AdminFaqRepository adminFaqs;
    @Mock CategoryQueryService categories;
    @Mock UserRepository users;
    @Mock FaqRepository faqRepository;
    @Mock FaqTagRepository tags;
    @Mock FaqRevisionRepository revisions;
    @Mock MediaAssetRepository assets;
    @Mock MediaService media;
    @Mock AuditService audit;
    @Mock ObjectProvider<NotificationService> notifications;
    @Mock NotificationService notification;
    @InjectMocks AdminFaqService service;

    @AfterEach
    void clearAuth() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authorSubmitsDraftAndAllActiveAdminsAreNotified() {
        authenticate(7L, RoleCode.MANAGER);
        Faq faq = faq(FaqStatus.DRAFT);
        when(faqRepository.findById(12L)).thenReturn(Optional.of(faq));
        when(notifications.getIfAvailable()).thenReturn(notification);
        when(users.findIdsByRoleCodeAndStatus(RoleCode.ADMIN, UserStatus.ACTIVE))
                .thenReturn(List.of(1L, 2L));

        service.submitForReview(12L, 7L);

        assertEquals(FaqStatus.PENDING_REVIEW, faq.getStatus());
        verify(notification).notifyUser(eq(1L), eq(NotificationType.FAQ_REVIEW_REQUESTED), any());
        verify(notification).notifyUser(eq(2L), eq(NotificationType.FAQ_REVIEW_REQUESTED), any());
        verify(audit).log(AuditActions.FAQ_SUBMITTED, "Faq", 12L, "DRAFT", "PENDING_REVIEW");
    }

    @Test
    void reviewIncludesPreviewAndRevisionNotes() {
        Faq faq = faq(FaqStatus.PENDING_REVIEW);
        faq.setCategoryId(5L);
        faq.setSummary("Tóm tắt học phí");
        faq.getTags().add(FaqTag.builder().name("Học phí").slug("hoc-phi").build());
        FaqRevision revision = FaqRevision.builder().versionNo(2).changeNote("Cập nhật nội dung")
                .changedBy(7L).build();
        when(faqRepository.findById(12L)).thenReturn(Optional.of(faq));
        when(revisions.findByFaqIdOrderByVersionNoDesc(12L)).thenReturn(List.of(revision));
        when(categories.getDisplayPath(5L)).thenReturn("Đào tạo › Học phí");

        var view = service.review(12L);

        assertEquals("Học phí sinh viên", view.title());
        assertEquals("<p>Nội dung</p>", view.bodyHtml());
        assertEquals("Đào tạo › Học phí", view.categoryPath());
        assertEquals(List.of("Học phí"), view.tags());
        assertEquals("Cập nhật nội dung", view.revisions().getFirst().changeNote());
    }

    @Test
    void scheduledApprovalSetsFutureEffectiveTimeAndNotifiesAuthor() {
        authenticate(1L, RoleCode.ADMIN);
        Faq faq = faq(FaqStatus.PENDING_REVIEW);
        LocalDateTime future = LocalDateTime.now().plusDays(3);
        when(faqRepository.findById(12L)).thenReturn(Optional.of(faq));
        when(notifications.getIfAvailable()).thenReturn(notification);

        service.approve(12L, 1L, future);

        assertEquals(FaqStatus.PUBLISHED, faq.getStatus());
        assertEquals(future, faq.getPublishAt());
        assertEquals(future, faq.getPublishedAt());
        assertEquals(1L, faq.getReviewerId());
        verify(notification).notifyUser(eq(7L), eq(NotificationType.FAQ_APPROVED), any());
        verify(audit).log(eq(AuditActions.FAQ_PUBLISHED), eq("Faq"), eq(12L),
                eq("PENDING_REVIEW"), any());
    }

    @Test
    void adminCanPublishDraftImmediately() {
        authenticate(1L, RoleCode.ADMIN);
        Faq faq = faq(FaqStatus.DRAFT);
        when(faqRepository.findById(12L)).thenReturn(Optional.of(faq));

        service.publishDirect(12L, 1L, null);

        assertEquals(FaqStatus.PUBLISHED, faq.getStatus());
        assertNull(faq.getPublishAt());
        assertTrue(faq.getPublishedAt() != null);
        verify(audit).log(eq(AuditActions.FAQ_PUBLISHED), eq("Faq"), eq(12L),
                eq("DRAFT"), any());
    }

    @Test
    void rejectionPreservesFullFiveHundredCharacterReasonOnLatestRevision() {
        authenticate(1L, RoleCode.ADMIN);
        Faq faq = faq(FaqStatus.PENDING_REVIEW);
        FaqRevision latest = FaqRevision.builder().faqId(12L).versionNo(2)
                .title("Học phí sinh viên").bodyHtml("<p>Nội dung</p>").build();
        when(faqRepository.findById(12L)).thenReturn(Optional.of(faq));
        when(revisions.findByFaqIdOrderByVersionNoDesc(12L)).thenReturn(List.of(latest));
        String reason = "L".repeat(500);

        service.reject(12L, 1L, reason);

        assertEquals(FaqStatus.DRAFT, faq.getStatus());
        assertEquals(reason, latest.getChangeNote());
        verify(revisions).save(latest);
        verify(audit).log(eq(AuditActions.FAQ_REJECTED), eq("Faq"), eq(12L),
                eq("PENDING_REVIEW"), any());
    }

    @Test
    void invalidTransitionIsRejectedBeforeWrite() {
        authenticate(1L, RoleCode.ADMIN);
        Faq faq = faq(FaqStatus.DRAFT);
        when(faqRepository.findById(12L)).thenReturn(Optional.of(faq));

        assertThrows(InvalidTransitionException.class, () -> service.approve(12L, 1L, null));

        verify(faqRepository, never()).saveAndFlush(any());
    }

    @Test
    void optimisticFailureBecomesConflict() {
        authenticate(1L, RoleCode.ADMIN);
        Faq faq = faq(FaqStatus.PUBLISHED);
        when(faqRepository.findById(12L)).thenReturn(Optional.of(faq));
        when(faqRepository.saveAndFlush(any())).thenThrow(new OptimisticLockingFailureException("stale"));

        assertThrows(ConflictException.class,
                () -> service.archive(12L, 1L, "Nội dung không còn phù hợp"));

        verify(audit, never()).log(eq(AuditActions.FAQ_ARCHIVED), any(), any(), any(), any());
    }

    @Test
    void restoreReturnsArchiveToUnpublishedDraft() {
        authenticate(1L, RoleCode.ADMIN);
        Faq faq = faq(FaqStatus.ARCHIVED);
        faq.setPublishAt(LocalDateTime.now().plusDays(1));
        faq.setPublishedAt(LocalDateTime.now().minusDays(1));
        when(faqRepository.findById(12L)).thenReturn(Optional.of(faq));

        service.restore(12L, 1L);

        assertEquals(FaqStatus.DRAFT, faq.getStatus());
        assertNull(faq.getPublishAt());
        assertNull(faq.getPublishedAt());
        verify(audit).log(AuditActions.FAQ_RESTORED, "Faq", 12L, "ARCHIVED", "DRAFT");
    }

    @Test
    void featuredToggleRequiresPublishedStateAndIsAudited() {
        authenticate(1L, RoleCode.ADMIN);
        Faq faq = faq(FaqStatus.PUBLISHED);
        when(faqRepository.findById(12L)).thenReturn(Optional.of(faq));

        service.toggleFeatured(12L, 1L);

        assertTrue(faq.isFeatured());
        verify(audit).log(AuditActions.FAQ_FEATURED_CHANGED, "Faq", 12L, "false", "true");
    }

    @Test
    void restoringRevisionCreatesNewVersionAndKeepsPublishedStatus() {
        authenticate(1L, RoleCode.ADMIN);
        Faq faq = faq(FaqStatus.PUBLISHED);
        faq.setVersionNo(4);
        FaqRevision source = FaqRevision.builder().faqId(12L).versionNo(1)
                .title("Bài viết cũ").summary("Tóm tắt cũ")
                .bodyHtml("<p>Nội dung cũ</p><script>alert(1)</script>").build();
        when(faqRepository.findById(12L)).thenReturn(Optional.of(faq));
        when(revisions.findByFaqIdAndVersionNo(12L, 1)).thenReturn(Optional.of(source));

        service.restoreRevision(12L, 1, 1L);

        assertEquals(FaqStatus.PUBLISHED, faq.getStatus());
        assertEquals(5, faq.getVersionNo());
        assertEquals("hoc-phi-sinh-vien", faq.getSlug());
        assertTrue(!faq.getBodyHtml().contains("<script>"));
        ArgumentCaptor<FaqRevision> saved = ArgumentCaptor.forClass(FaqRevision.class);
        verify(revisions).save(saved.capture());
        assertEquals(5, saved.getValue().getVersionNo());
        assertEquals(faq.getBodyHtml(), saved.getValue().getBodyHtml());
        verify(audit).log(AuditActions.FAQ_REVISION_RESTORED, "Faq", 12L,
                "v4", "v5 từ v1");
    }

    @Test
    void archivedFaqCannotRestoreRevision() {
        authenticate(1L, RoleCode.ADMIN);
        when(faqRepository.findById(12L)).thenReturn(Optional.of(faq(FaqStatus.ARCHIVED)));

        assertThrows(InvalidTransitionException.class,
                () -> service.restoreRevision(12L, 1, 1L));

        verify(revisions, never()).findByFaqIdAndVersionNo(any(), any());
    }

    private static Faq faq(FaqStatus status) {
        Faq faq = Faq.builder().status(status).authorId(7L).title("Học phí sinh viên")
                .slug("hoc-phi-sinh-vien").bodyHtml("<p>Nội dung</p>").versionNo(2).build();
        faq.setId(12L);
        return faq;
    }

    private static void authenticate(Long id, RoleCode role) {
        QauteUserDetails user = new QauteUserDetails(id, "admin@example.com", "Admin", role, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
