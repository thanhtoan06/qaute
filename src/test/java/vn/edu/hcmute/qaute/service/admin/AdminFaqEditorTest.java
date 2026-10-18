package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ConflictException;
import vn.edu.hcmute.qaute.dto.request.admin.FaqForm;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
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
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@ExtendWith(MockitoExtension.class)
class AdminFaqEditorTest {
    @Mock AdminFaqRepository adminFaqs;
    @Mock CategoryQueryService categories;
    @Mock UserRepository users;
    @Mock FaqRepository faqRepository;
    @Mock FaqTagRepository tags;
    @Mock FaqRevisionRepository revisions;
    @Mock MediaAssetRepository assets;
    @Mock MediaService media;
    @Mock AuditService audit;
    @InjectMocks AdminFaqService service;

    @AfterEach
    void clearAuth() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void newDraftGetsUniqueSlugSanitizedContentAndRevision() {
        FaqForm form = form();
        form.setTags("Học phí, Đăng ký");
        form.setImageAssetIds(List.of(31L));
        activeCategory();
        when(faqRepository.existsBySlug("hoc-phi-sinh-vien")).thenReturn(true);
        when(faqRepository.existsBySlug("hoc-phi-sinh-vien-2")).thenReturn(false);
        when(faqRepository.saveAndFlush(any(Faq.class))).thenAnswer(invocation -> {
            Faq faq = invocation.getArgument(0);
            faq.setId(12L);
            return faq;
        });
        when(tags.findBySlug("hoc-phi")).thenReturn(Optional.empty());
        when(tags.findBySlug("dang-ky")).thenReturn(Optional.empty());
        when(tags.save(any(FaqTag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Long id = service.save(form, 7L);

        assertEquals(12L, id);
        ArgumentCaptor<Faq> saved = ArgumentCaptor.forClass(Faq.class);
        verify(faqRepository).saveAndFlush(saved.capture());
        assertEquals(FaqStatus.DRAFT, saved.getValue().getStatus());
        assertEquals("hoc-phi-sinh-vien-2", saved.getValue().getSlug());
        assertEquals("<p>Nội dung</p>", saved.getValue().getBodyHtml());
        assertEquals(2, saved.getValue().getTags().size());
        assertTrue(saved.getValue().getTags().stream()
                .anyMatch(tag -> "hoc phi".equals(tag.getNameNorm())));
        ArgumentCaptor<FaqRevision> revision = ArgumentCaptor.forClass(FaqRevision.class);
        verify(revisions).save(revision.capture());
        assertEquals(1, revision.getValue().getVersionNo());
        assertEquals(7L, revision.getValue().getChangedBy());
        verify(media).attach(31L, MediaUsageType.FAQ_IMAGE, 12L);
        verify(audit).log(AuditActions.FAQ_CREATED, "Faq", 12L, null,
                "Học phí sinh viên | v1");
    }

    @Test
    void publishedEditKeepsSlugForAdminAndIncrementsVersion() {
        authenticate(RoleCode.ADMIN);
        FaqForm form = form();
        form.setId(12L);
        form.setVersion(4L);
        Faq existing = Faq.builder().title("Tiêu đề cũ").slug("slug-co")
                .status(FaqStatus.PUBLISHED).versionNo(2).bodyHtml("<p>Cũ</p>").build();
        existing.setId(12L);
        existing.setVersion(4L);
        activeCategory();
        when(faqRepository.findById(12L)).thenReturn(Optional.of(existing));

        service.save(form, 7L);

        assertEquals("slug-co", existing.getSlug());
        assertEquals(3, existing.getVersionNo());
        verify(faqRepository, never()).existsBySlugAndIdNot(any(), any());
        verify(audit).log(AuditActions.FAQ_UPDATED, "Faq", 12L,
                "Tiêu đề cũ | v2", "Học phí sinh viên | v3");
    }

    @Test
    void staleEditIsRejectedBeforeWriting() {
        authenticate(RoleCode.ADMIN);
        FaqForm form = form();
        form.setId(12L);
        form.setVersion(3L);
        Faq existing = Faq.builder().status(FaqStatus.DRAFT).build();
        existing.setVersion(4L);
        activeCategory();
        when(faqRepository.findById(12L)).thenReturn(Optional.of(existing));

        assertThrows(ConflictException.class, () -> service.save(form, 7L));

        verify(faqRepository, never()).saveAndFlush(any());
    }

    @Test
    void inactiveCategoryIsRejected() {
        when(categories.findCategory(5L)).thenReturn(Optional.of(category(false)));

        BadRequestException error = assertThrows(BadRequestException.class,
                () -> service.save(form(), 7L));

        assertTrue(error.getFieldErrors().containsKey("categoryId"));
        verify(faqRepository, never()).saveAndFlush(any());
    }

    @Test
    void managerCannotEditSomeoneElsesDraft() {
        authenticate(RoleCode.MANAGER);
        FaqForm form = form();
        form.setId(12L);
        form.setVersion(4L);
        Faq existing = Faq.builder().authorId(8L).status(FaqStatus.DRAFT).build();
        existing.setVersion(4L);
        activeCategory();
        when(faqRepository.findById(12L)).thenReturn(Optional.of(existing));

        assertThrows(BadRequestException.class, () -> service.save(form, 7L));

        verify(faqRepository, never()).saveAndFlush(any());
    }

    private void activeCategory() {
        when(categories.findCategory(5L)).thenReturn(Optional.of(category(true)));
    }

    private static CategoryView category(boolean active) {
        return new CategoryView(5L, 1L, "Đào tạo", null, "HOC_PHI", "Học phí", null, 1, active);
    }

    private static FaqForm form() {
        FaqForm form = new FaqForm();
        form.setTitle("Học phí sinh viên");
        form.setCategoryId(5L);
        form.setBodyHtml("<p>Nội dung</p><script>alert(1)</script>");
        return form;
    }

    private static void authenticate(RoleCode role) {
        QauteUserDetails user = new QauteUserDetails(7L, "admin@example.com", "Admin", role, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
