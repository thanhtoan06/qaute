package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.MediaDeliveryType;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.dto.request.admin.AdminProfileForm;
import vn.edu.hcmute.qaute.dto.request.student.ChangePasswordForm;
import vn.edu.hcmute.qaute.entity.identity.AccountActivationToken;
import vn.edu.hcmute.qaute.entity.identity.PasswordResetToken;
import vn.edu.hcmute.qaute.entity.identity.Role;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.identity.AccountActivationTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.PasswordResetTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.PasswordService;
import vn.edu.hcmute.qaute.service.media.MediaService;

@ExtendWith(MockitoExtension.class)
class AdminProfileServiceTest {
    private static final ValidatorFactory VALIDATORS = Validation.buildDefaultValidatorFactory();
    @Mock UserRepository users;
    @Mock MediaAssetRepository assets;
    @Mock MediaService media;
    @Mock PasswordService passwords;
    @Mock AuditService audit;
    @Mock PasswordResetTokenRepository resetTokens;
    @Mock AccountActivationTokenRepository activationTokens;
    private AdminProfileService service;

    @BeforeEach
    void setUp() {
        service = new AdminProfileService(users, assets, media, passwords, audit,
                VALIDATORS.getValidator(), resetTokens, activationTokens);
        authenticate(7L, RoleCode.ADMIN);
    }

    @AfterEach
    void clearSecurity() { SecurityContextHolder.clearContext(); }

    @AfterAll
    static void closeValidator() { VALIDATORS.close(); }

    @Test
    void nonAdminCannotReadOrChangeAdminProfile() {
        authenticate(7L, RoleCode.STUDENT);
        assertThrows(ForbiddenException.class, () -> service.get(7L));
        assertThrows(ForbiddenException.class, () -> service.update(7L, profileForm()));
        assertThrows(ForbiddenException.class, () -> service.changePassword(7L, passwordForm()));
        verifyNoInteractions(users, passwords, audit);
    }

    @Test
    void adminCannotEditAnotherAdminsProfileThroughService() {
        assertThrows(ForbiddenException.class, () -> service.update(8L, profileForm()));
        verifyNoInteractions(users);
    }

    @Test
    void updateNormalizesEmailAndNameAndAuditsChanges() {
        User user = admin();
        when(users.findById(7L)).thenReturn(Optional.of(user));
        AdminProfileForm form = profileForm();
        form.setFullName("  Nguyễn Quản Trị  ");
        form.setEmail("  NEW@EXAMPLE.COM  ");

        service.update(7L, form);

        assertEquals("Nguyễn Quản Trị", user.getFullName());
        assertEquals("new@example.com", user.getEmail());
        verify(users).saveAndFlush(user);
        verify(audit).log(eq(AuditActions.USER_UPDATED), eq("user"), eq(7L),
                eq("fullName=Admin;email=admin@example.com;phone=null"),
                eq("fullName=Nguyễn Quản Trị;email=new@example.com;phone=0912345678"));
    }

    @Test
    void duplicateEmailIsRejectedBeforeSaving() {
        when(users.findById(7L)).thenReturn(Optional.of(admin()));
        User other = admin(); other.setId(8L);
        when(users.findByEmailIgnoreCase("new@example.com")).thenReturn(Optional.of(other));
        DuplicateException exception = assertThrows(DuplicateException.class,
                () -> service.update(7L, profileForm()));
        assertEquals("email", exception.getField());
        verify(users, never()).saveAndFlush(any());
        verifyNoInteractions(audit);
    }

    @Test
    void invalidEmailAndPhoneAreRejectedByServiceValidation() {
        when(users.findById(7L)).thenReturn(Optional.of(admin()));
        AdminProfileForm form = profileForm();
        form.setEmail("bad-address"); form.setPhone("123");
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.update(7L, form));
        assertNotNull(exception.getFieldErrors().get("email"));
        assertNotNull(exception.getFieldErrors().get("phone"));
        verify(users, never()).saveAndFlush(any());
    }

    @Test
    void avatarOwnedByAnotherUserCannotBeAttached() {
        when(users.findById(7L)).thenReturn(Optional.of(admin()));
        when(assets.findById(20L)).thenReturn(Optional.of(MediaAsset.builder().ownerId(8L)
                .usageType(MediaUsageType.AVATAR).status(MediaStatus.ORPHAN).build()));
        assertThrows(ForbiddenException.class, () -> service.updateAvatar(7L, 20L));
        verifyNoInteractions(media, audit);
    }

    @Test
    void unavailableOldAvatarDoesNotInvokeTransactionalMediaService() {
        User user = admin(); user.setAvatarAssetId(10L);
        when(users.findById(7L)).thenReturn(Optional.of(user));
        when(assets.findById(10L)).thenReturn(Optional.of(MediaAsset.builder()
                .deliveryType(MediaDeliveryType.UPLOAD).status(MediaStatus.DELETED)
                .secureUrl("https://images.example.com/old.jpg").build()));

        assertNull(service.get(7L).avatarUrl());

        verifyNoInteractions(media);
    }

    @Test
    void unexpectedAvatarLookupFailurePropagatesInsteadOfHidingTransactionRollback() {
        User user = admin(); user.setAvatarAssetId(10L);
        MediaAsset asset = MediaAsset.builder().deliveryType(MediaDeliveryType.UPLOAD)
                .status(MediaStatus.ACTIVE).secureUrl("https://images.example.com/avatar.jpg").build();
        asset.setId(10L);
        when(users.findById(7L)).thenReturn(Optional.of(user));
        when(assets.findById(10L)).thenReturn(Optional.of(asset));
        when(media.publicUrl(10L)).thenThrow(new BadRequestException("Không thể đọc ảnh"));

        assertThrows(BadRequestException.class, () -> service.get(7L));
    }

    @Test
    void avatarReplacementAttachesOwnedImageAndDeletesPrevious() {
        User user = admin(); user.setAvatarAssetId(10L);
        when(users.findById(7L)).thenReturn(Optional.of(user));
        when(assets.findById(20L)).thenReturn(Optional.of(MediaAsset.builder().ownerId(7L)
                .usageType(MediaUsageType.AVATAR).status(MediaStatus.ORPHAN).build()));
        service.updateAvatar(7L, 20L);
        assertEquals(20L, user.getAvatarAssetId());
        verify(media).attach(20L, MediaUsageType.AVATAR, 7L);
        verify(media).delete(10L);
        verify(audit).log(AuditActions.USER_UPDATED, "user", 7L,
                "avatarAssetId=10", "avatarAssetId=20");
    }

    @Test
    void oldAvatarDeleteFailurePropagatesAndDoesNotAuditSuccess() {
        User user = admin(); user.setAvatarAssetId(10L);
        when(users.findById(7L)).thenReturn(Optional.of(user));
        when(assets.findById(20L)).thenReturn(Optional.of(MediaAsset.builder().ownerId(7L)
                .usageType(MediaUsageType.AVATAR).status(MediaStatus.ORPHAN).build()));
        doThrow(new BadRequestException("Không thể xóa ảnh cũ")).when(media).delete(10L);

        assertThrows(BadRequestException.class, () -> service.updateAvatar(7L, 20L));

        verifyNoInteractions(audit);
    }

    @Test
    void weakOrMismatchingSelfPasswordNeverCallsPasswordService() {
        when(users.findById(7L)).thenReturn(Optional.of(admin()));
        ChangePasswordForm weak = passwordForm();
        weak.setNewPassword("weak"); weak.setConfirmPassword("weak");
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.changePassword(7L, weak));
        assertNotNull(exception.getFieldErrors().get("newPassword"));
        ChangePasswordForm mismatch = passwordForm(); mismatch.setConfirmPassword("Other@12345");
        assertThrows(BadRequestException.class, () -> service.changePassword(7L, mismatch));
        verifyNoInteractions(passwords, resetTokens, activationTokens);
    }

    @Test
    void selfPasswordChangeReusesExistingServiceAndInvalidatesOldRecoveryTokens() {
        when(users.findById(7L)).thenReturn(Optional.of(admin()));
        PasswordResetToken reset = new PasswordResetToken();
        AccountActivationToken activation = new AccountActivationToken();
        when(resetTokens.findByUserIdAndUsedAtIsNull(7L)).thenReturn(List.of(reset));
        when(activationTokens.findByUserIdAndUsedAtIsNull(7L)).thenReturn(List.of(activation));

        service.changePassword(7L, passwordForm());

        verify(passwords).changePassword(7L, "Current@12345", "Changed@12345");
        assertNotNull(reset.getUsedAt());
        assertNotNull(activation.getUsedAt());
    }

    @Test
    void multibytePasswordBeyondBcryptLimitIsRejectedBeforeHashing() {
        when(users.findById(7L)).thenReturn(Optional.of(admin()));
        ChangePasswordForm form = passwordForm();
        form.setNewPassword("Ạ".repeat(25) + "a1@");
        form.setConfirmPassword(form.getNewPassword());
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.changePassword(7L, form));
        assertNotNull(exception.getFieldErrors().get("newPassword"));
        verifyNoInteractions(passwords, resetTokens, activationTokens);
    }

    @Test
    void incorrectCurrentPasswordDoesNotInvalidateRecoveryTokens() {
        when(users.findById(7L)).thenReturn(Optional.of(admin()));
        doThrow(new BadRequestException("Mật khẩu hiện tại không đúng"))
                .when(passwords).changePassword(7L, "Current@12345", "Changed@12345");
        assertThrows(BadRequestException.class, () -> service.changePassword(7L, passwordForm()));
        verifyNoInteractions(resetTokens, activationTokens);
    }

    private static User admin() {
        User user = User.builder().fullName("Admin").email("admin@example.com")
                .role(Role.builder().code(RoleCode.ADMIN).build()).build();
        user.setId(7L);
        return user;
    }

    private static AdminProfileForm profileForm() {
        AdminProfileForm form = new AdminProfileForm();
        form.setFullName("Quản trị viên"); form.setEmail("new@example.com"); form.setPhone("0912345678");
        return form;
    }

    private static ChangePasswordForm passwordForm() {
        ChangePasswordForm form = new ChangePasswordForm();
        form.setCurrentPassword("Current@12345"); form.setNewPassword("Changed@12345");
        form.setConfirmPassword("Changed@12345");
        return form;
    }

    private static void authenticate(Long id, RoleCode role) {
        QauteUserDetails user = new QauteUserDetails(id, "admin@example.com", "Admin", role, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
