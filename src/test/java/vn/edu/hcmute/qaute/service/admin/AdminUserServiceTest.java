package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.entity.identity.Role;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.FacultyRepository;
import vn.edu.hcmute.qaute.repository.identity.RoleRepository;
import vn.edu.hcmute.qaute.repository.identity.StudentProfileRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerProfileRepository;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;
import vn.edu.hcmute.qaute.service.identity.InvitationService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {
    @Mock UserRepository users;
    @Mock RoleRepository roles;
    @Mock StudentProfileRepository students;
    @Mock ManagerProfileRepository managers;
    @Mock FacultyRepository faculties;
    @Mock DepartmentRepository departments;
    @Mock InvitationService invitations;
    @Mock AuthSessionService sessions;
    @Mock ManagerScopeService managerScope;
    @Mock EmailService emails;
    @Mock AuditService audit;
    @InjectMocks AdminUserService service;

    @Test
    void cannotLockOwnAccount() {
        User user = user(1L, RoleCode.ADMIN, UserStatus.ACTIVE, "hash");
        when(users.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class,
                () -> service.lock(1L, "Lý do khóa hợp lệ", 1L));

        assertEquals(UserStatus.ACTIVE, user.getStatus());
        verifyNoInteractions(sessions, emails);
    }

    @Test
    void cannotDeactivateLastActiveAdmin() {
        User user = user(1L, RoleCode.ADMIN, UserStatus.ACTIVE, "hash");
        when(users.findById(1L)).thenReturn(Optional.of(user));
        when(users.findIdsByRoleCodeAndStatus(RoleCode.ADMIN, UserStatus.ACTIVE))
                .thenReturn(List.of(1L));

        assertThrows(BadRequestException.class,
                () -> service.deactivate(1L, "Lý do vô hiệu hóa hợp lệ", 2L));

        assertEquals(UserStatus.ACTIVE, user.getStatus());
        verifyNoInteractions(sessions, emails);
    }

    @Test
    void cannotLockManagerWithOpenTickets() {
        User user = user(3L, RoleCode.MANAGER, UserStatus.ACTIVE, "hash");
        when(users.findById(3L)).thenReturn(Optional.of(user));
        when(managerScope.currentOpenTicketCount(3L)).thenReturn(4);

        BadRequestException error = assertThrows(BadRequestException.class,
                () -> service.lock(3L, "Lý do khóa hợp lệ", 1L));

        assertTrue(error.getMessage().contains("4 yêu cầu"));
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        verifyNoInteractions(sessions, emails);
    }

    @Test
    void lockRevokesSessionsAndQueuesMandatoryEmail() {
        User user = user(4L, RoleCode.STUDENT, UserStatus.ACTIVE, "hash");
        when(users.findById(4L)).thenReturn(Optional.of(user));

        service.lock(4L, "Vi phạm quy định sử dụng", 1L);

        assertEquals(UserStatus.LOCKED, user.getStatus());
        verify(sessions).revokeAllSessions(4L, "admin_locked");
        verify(emails).sendTemplated(eq("user4@example.com"), any(),
                eq("security-account-locked"), any());
    }

    @Test
    void unlockClearsLoginFailureState() {
        User user = user(5L, RoleCode.STUDENT, UserStatus.LOCKED, "hash");
        user.setFailedLoginCount(5);
        user.setLockedUntil(LocalDateTime.now().plusHours(1));
        when(users.findById(5L)).thenReturn(Optional.of(user));

        service.unlock(5L, 1L);

        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals(0, user.getFailedLoginCount());
        assertNull(user.getLockedUntil());
    }

    @Test
    void reactivationWithoutPasswordSendsInvitation() {
        User user = user(6L, RoleCode.STUDENT, UserStatus.DEACTIVATED, null);
        when(users.findById(6L)).thenReturn(Optional.of(user));

        service.reactivate(6L, 1L);

        assertEquals(UserStatus.PENDING_ACTIVATION, user.getStatus());
        verify(invitations).sendInvitation(6L);
    }

    @Test
    void forceResetRequiresActiveAccount() {
        User user = user(7L, RoleCode.STUDENT, UserStatus.LOCKED, "hash");
        when(users.findById(7L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> service.forcePasswordReset(7L, 1L));
        verifyNoInteractions(invitations, sessions);
    }

    private static User user(Long id, RoleCode role, UserStatus status, String passwordHash) {
        User user = User.builder()
                .fullName("Người dùng " + id)
                .email("user" + id + "@example.com")
                .role(Role.builder().code(role).name(role.name()).build())
                .status(status)
                .passwordHash(passwordHash)
                .build();
        user.setId(id);
        return user;
    }
}
