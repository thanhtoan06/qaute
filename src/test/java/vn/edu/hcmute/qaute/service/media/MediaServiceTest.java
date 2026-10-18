package vn.edu.hcmute.qaute.service.media;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.security.QauteUserDetails;

@ExtendWith(MockitoExtension.class)
class MediaServiceTest {
    @Mock MediaAssetRepository assets;

    @AfterEach
    void clearAuth() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void ownerCanAttachOrphanFaqImage() {
        authenticate(7L);
        MediaAsset asset = MediaAsset.builder().ownerId(7L).usageType(MediaUsageType.FAQ_IMAGE)
                .status(MediaStatus.ORPHAN).build();
        when(assets.findById(31L)).thenReturn(Optional.of(asset));

        new MediaService(assets, "").attach(31L, MediaUsageType.FAQ_IMAGE, 12L);

        assertEquals(12L, asset.getUsageRefId());
        assertEquals(MediaStatus.ACTIVE, asset.getStatus());
        verify(assets).save(asset);
    }

    @Test
    void cannotAttachImageFromAnotherAccountOrFaq() {
        authenticate(7L);
        MediaAsset foreign = MediaAsset.builder().ownerId(8L).usageType(MediaUsageType.FAQ_IMAGE)
                .status(MediaStatus.ORPHAN).build();
        when(assets.findById(31L)).thenReturn(Optional.of(foreign));
        MediaService service = new MediaService(assets, "");

        assertThrows(BadRequestException.class,
                () -> service.attach(31L, MediaUsageType.FAQ_IMAGE, 12L));

        foreign.setUsageRefId(13L);
        foreign.setStatus(MediaStatus.ACTIVE);
        assertThrows(BadRequestException.class,
                () -> service.attach(31L, MediaUsageType.FAQ_IMAGE, 12L));
        verify(assets, never()).save(foreign);
    }

    private static void authenticate(Long id) {
        QauteUserDetails user = new QauteUserDetails(id, "admin@example.com", "Admin",
                RoleCode.ADMIN, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
