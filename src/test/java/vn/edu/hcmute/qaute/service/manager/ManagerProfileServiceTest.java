package vn.edu.hcmute.qaute.service.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.dto.request.manager.ManagerProfileForm;
import vn.edu.hcmute.qaute.entity.identity.Role;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerProfileRepository;
import vn.edu.hcmute.qaute.service.media.MediaService;

@ExtendWith(MockitoExtension.class)
class ManagerProfileServiceTest {
    @Mock UserRepository users;
    @Mock ManagerProfileRepository profiles;
    @Mock DepartmentRepository departments;
    @Mock MediaAssetRepository assets;
    @Mock MediaService media;
    @InjectMocks ManagerProfileService service;

    @Test
    void updateCreatesMissingProfileAndTrimsEditableFields() {
        User user = manager();
        when(users.findById(7L)).thenReturn(Optional.of(user));
        ManagerProfileForm form = new ManagerProfileForm();
        form.setFullName("  Nguyễn Văn B  ");
        form.setPhone("0912345678");
        form.setJobTitle("  Tư vấn viên  ");
        form.setBio("  Hỗ trợ sinh viên  ");

        service.update(7L, form);

        assertEquals("Nguyễn Văn B", user.getFullName());
        assertEquals("0912345678", user.getPhone());
        org.mockito.ArgumentCaptor<ManagerProfile> saved = org.mockito.ArgumentCaptor.forClass(ManagerProfile.class);
        verify(profiles).save(saved.capture());
        assertEquals(7L, saved.getValue().getUserId());
        assertEquals("Tư vấn viên", saved.getValue().getJobTitle());
        assertEquals("Hỗ trợ sinh viên", saved.getValue().getBio());
    }

    @Test
    void avatarMustBelongToManagerAndBeUnused() {
        when(users.findById(7L)).thenReturn(Optional.of(manager()));
        when(assets.findById(20L)).thenReturn(Optional.of(MediaAsset.builder()
                .ownerId(8L).usageType(MediaUsageType.AVATAR)
                .status(MediaStatus.ORPHAN).build()));

        assertThrows(ForbiddenException.class, () -> service.updateAvatar(7L, 20L));
        verify(media, never()).attach(any(), any(), any());
    }

    @Test
    void avatarReplacementAttachesNewAndDeletesOld() {
        User user = manager();
        user.setAvatarAssetId(10L);
        when(users.findById(7L)).thenReturn(Optional.of(user));
        when(assets.findById(20L)).thenReturn(Optional.of(MediaAsset.builder()
                .ownerId(7L).usageType(MediaUsageType.AVATAR)
                .status(MediaStatus.ORPHAN).build()));

        service.updateAvatar(7L, 20L);

        verify(media).attach(20L, MediaUsageType.AVATAR, 7L);
        assertEquals(20L, user.getAvatarAssetId());
        verify(users).save(user);
        verify(media).delete(10L);
    }

    @Test
    void statusSetsHeartbeatAndStaleJobOnlyTargetsOnlineOrBusy() {
        when(users.findById(7L)).thenReturn(Optional.of(manager()));
        ManagerProfile profile = ManagerProfile.builder().userId(7L).build();
        when(profiles.findByUserId(7L)).thenReturn(Optional.of(profile));

        service.setStatus(7L, AvailabilityStatus.BUSY);

        assertEquals(AvailabilityStatus.BUSY, profile.getAvailabilityStatus());
        assertNotNull(profile.getLastSeenAt());
        verify(profiles).save(profile);
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(5);
        service.expireStalePresence(cutoff);
        verify(profiles).markStaleOffline(List.of(AvailabilityStatus.ONLINE,
                AvailabilityStatus.BUSY), AvailabilityStatus.OFFLINE, cutoff);
    }

    private static User manager() {
        return User.builder().fullName("Manager").role(Role.builder().code(RoleCode.MANAGER).build()).build();
    }
}
