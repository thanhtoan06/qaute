package vn.edu.hcmute.qaute.service.admin;

import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.admin.UserCreateForm;
import vn.edu.hcmute.qaute.dto.request.admin.UserEditForm;
import vn.edu.hcmute.qaute.entity.identity.StudentProfile;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;
import vn.edu.hcmute.qaute.repository.identity.FacultyRepository;
import vn.edu.hcmute.qaute.repository.identity.RoleRepository;
import vn.edu.hcmute.qaute.repository.identity.StudentProfileRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerProfileRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;
import vn.edu.hcmute.qaute.service.identity.InvitationService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

@Service
@Transactional
public class AdminUserService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final StudentProfileRepository students;
    private final ManagerProfileRepository managers;
    private final FacultyRepository faculties;
    private final DepartmentRepository departments;
    private final InvitationService invitations;
    private final AuthSessionService sessions;
    private final ManagerScopeService managerScope;
    private final EmailService emails;
    private final AuditService audit;

    public AdminUserService(UserRepository users, RoleRepository roles,
                            StudentProfileRepository students, ManagerProfileRepository managers,
                            FacultyRepository faculties, DepartmentRepository departments,
                            InvitationService invitations, AuthSessionService sessions,
                            ManagerScopeService managerScope, EmailService emails, AuditService audit) {
        this.users = users;
        this.roles = roles;
        this.students = students;
        this.managers = managers;
        this.faculties = faculties;
        this.departments = departments;
        this.invitations = invitations;
        this.sessions = sessions;
        this.managerScope = managerScope;
        this.emails = emails;
        this.audit = audit;
    }

    public User create(UserCreateForm form, Long actorId) {
        if (form.getRole() == null) {
            throw fieldError("role", "Vui lòng chọn vai trò");
        }
        String email = form.getEmail().trim().toLowerCase(java.util.Locale.ROOT);
        if (users.existsByEmailIgnoreCase(email)) {
            throw new DuplicateException("email", "Email đã được sử dụng");
        }
        String mssv = checkedMssv(form.getRole(), form.getMssv(), null);
        validateProfileIds(form.getRole(), form.getFacultyId(), form.getDepartmentId());
        User user = users.save(User.builder()
                .fullName(form.getFullName().trim())
                .email(email)
                .mssv(mssv)
                .phone(blankToNull(form.getPhone()))
                .role(roles.findByCode(form.getRole())
                        .orElseThrow(() -> fieldError("role", "Vai trò chưa được cấu hình")))
                .status(UserStatus.PENDING_ACTIVATION)
                .createdBy(actorId)
                .build());
        if (form.getRole() == RoleCode.STUDENT) {
            StudentProfile profile = StudentProfile.builder().userId(user.getId()).build();
            applyStudent(profile, form.getFacultyId(), form.getClassCode(), form.getCohort(), form.getProgram());
            students.save(profile);
        } else if (form.getRole() == RoleCode.MANAGER) {
            ManagerProfile profile = ManagerProfile.builder()
                    .userId(user.getId()).availabilityStatus(AvailabilityStatus.OFFLINE).build();
            applyManager(profile, form.getDepartmentId(), form.getJobTitle(), form.getEmployeeCode());
            managers.save(profile);
        }
        invitations.sendInvitation(user.getId());
        audit.log(AuditActions.USER_CREATED, "User", user.getId(), null, summary(user));
        return user;
    }

    @Transactional(readOnly = true)
    public UserEditForm getEditForm(Long id) {
        User user = requireUser(id);
        UserEditForm form = new UserEditForm();
        form.setFullName(user.getFullName());
        form.setPhone(user.getPhone());
        form.setMssv(user.getMssv());
        if (user.getRole().getCode() == RoleCode.STUDENT) {
            students.findByUserId(id).ifPresent(profile -> {
                form.setFacultyId(profile.getFacultyId());
                form.setClassCode(profile.getClassCode());
                form.setCohort(profile.getCohort());
                form.setProgram(profile.getProgram());
            });
        } else if (user.getRole().getCode() == RoleCode.MANAGER) {
            managers.findByUserId(id).ifPresent(profile -> {
                form.setDepartmentId(profile.getDepartmentId());
                form.setJobTitle(profile.getJobTitle());
                form.setEmployeeCode(profile.getEmployeeCode());
            });
        }
        return form;
    }

    public void update(Long id, UserEditForm form, Long actorId) {
        User user = requireUser(id);
        RoleCode role = user.getRole().getCode();
        String mssv = checkedMssv(role, form.getMssv(), id);
        validateProfileIds(role, form.getFacultyId(), form.getDepartmentId());
        String before = summary(user) + profileSummary(id, role);
        user.setFullName(form.getFullName().trim());
        user.setPhone(blankToNull(form.getPhone()));
        user.setMssv(mssv);
        if (role == RoleCode.STUDENT) {
            StudentProfile profile = students.findByUserId(id)
                    .orElseGet(() -> StudentProfile.builder().userId(id).build());
            applyStudent(profile, form.getFacultyId(), form.getClassCode(), form.getCohort(), form.getProgram());
            students.save(profile);
        } else if (role == RoleCode.MANAGER) {
            ManagerProfile profile = managers.findByUserId(id)
                    .orElseGet(() -> ManagerProfile.builder().userId(id)
                            .availabilityStatus(AvailabilityStatus.OFFLINE).build());
            applyManager(profile, form.getDepartmentId(), form.getJobTitle(), form.getEmployeeCode());
            managers.save(profile);
        }
        users.save(user);
        audit.log(AuditActions.USER_UPDATED, "User", id, before,
                summary(user) + profileSummary(id, role));
    }

    public void resendInvitation(Long id, Long actorId) {
        User user = requireUser(id);
        if (user.getStatus() != UserStatus.PENDING_ACTIVATION) {
            throw new BadRequestException("Chỉ tài khoản chờ kích hoạt mới được gửi lại lời mời");
        }
        invitations.sendInvitation(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void lock(Long id, String reason, Long actorId) {
        User user = requireUser(id);
        String normalizedReason = requireReason(reason);
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BadRequestException("Chỉ tài khoản đang hoạt động mới có thể bị khóa");
        }
        protectAccountRemoval(user, actorId);
        String before = summary(user);
        user.setStatus(UserStatus.LOCKED);
        sessions.revokeAllSessions(id, "admin_locked");
        emails.sendTemplated(user.getEmail(),
                "QAUTE - " + NotificationType.SECURITY_ACCOUNT_LOCKED.getLabel(),
                "security-account-locked",
                Map.of("fullName", user.getFullName(), "reason", normalizedReason));
        audit.log(AuditActions.USER_LOCKED, "User", id, before,
                summary(user) + ",reason=" + normalizedReason);
    }

    public void unlock(Long id, Long actorId) {
        User user = requireUser(id);
        if (user.getStatus() != UserStatus.LOCKED) {
            throw new BadRequestException("Chỉ tài khoản đã khóa mới có thể mở khóa");
        }
        String before = summary(user);
        user.setStatus(UserStatus.ACTIVE);
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        audit.log(AuditActions.USER_UNLOCKED, "User", id, before, summary(user));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deactivate(Long id, String reason, Long actorId) {
        User user = requireUser(id);
        String normalizedReason = requireReason(reason);
        if (user.getStatus() == UserStatus.DEACTIVATED) {
            throw new BadRequestException("Tài khoản đã được vô hiệu hóa");
        }
        protectAccountRemoval(user, actorId);
        String before = summary(user);
        user.setStatus(UserStatus.DEACTIVATED);
        sessions.revokeAllSessions(id, "admin_deactivated");
        audit.log(AuditActions.USER_DEACTIVATED, "User", id, before,
                summary(user) + ",reason=" + normalizedReason);
    }

    public void reactivate(Long id, Long actorId) {
        User user = requireUser(id);
        if (user.getStatus() != UserStatus.DEACTIVATED) {
            throw new BadRequestException("Chỉ tài khoản đã vô hiệu hóa mới có thể kích hoạt lại");
        }
        String before = summary(user);
        boolean hasPassword = user.getPasswordHash() != null && !user.getPasswordHash().isBlank();
        user.setStatus(hasPassword ? UserStatus.ACTIVE : UserStatus.PENDING_ACTIVATION);
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        if (!hasPassword) {
            invitations.sendInvitation(id);
        }
        audit.log(AuditActions.USER_REACTIVATED, "User", id, before, summary(user));
    }

    public void forcePasswordReset(Long id, Long actorId) {
        User user = requireUser(id);
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BadRequestException("Chỉ tài khoản đang hoạt động mới có thể đặt lại mật khẩu");
        }
        invitations.sendInvitation(id);
        sessions.revokeAllSessions(id, "admin_force_reset");
        audit.log(AuditActions.USER_FORCE_RESET, "User", id, summary(user),
                "invitationSent=true,sessionsRevoked=true");
    }

    private void protectAccountRemoval(User user, Long actorId) {
        if (Objects.equals(user.getId(), actorId)) {
            throw new BadRequestException("Không thể khóa hoặc vô hiệu hóa tài khoản của chính mình");
        }
        RoleCode role = user.getRole().getCode();
        if (role == RoleCode.ADMIN && user.getStatus() == UserStatus.ACTIVE
                && users.findIdsByRoleCodeAndStatus(RoleCode.ADMIN, UserStatus.ACTIVE).size() <= 1) {
            throw new BadRequestException("Không thể khóa hoặc vô hiệu hóa Admin cuối cùng đang hoạt động");
        }
        if (role == RoleCode.MANAGER) {
            int openTickets = managerScope.currentOpenTicketCount(user.getId());
            if (openTickets > 0) {
                throw new BadRequestException("Hãy chuyển giao " + openTickets
                        + " yêu cầu đang phụ trách trước");
            }
        }
    }

    private static String requireReason(String reason) {
        String normalized = reason == null ? "" : reason.trim();
        if (normalized.length() < 10 || normalized.length() > 500) {
            throw fieldError("reason", "Lý do phải từ 10 đến 500 ký tự");
        }
        return normalized;
    }

    private User requireUser(Long id) {
        return users.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
    }

    private String checkedMssv(RoleCode role, String raw, Long currentUserId) {
        if (role != RoleCode.STUDENT) {
            return null;
        }
        String mssv = blankToNull(raw);
        if (mssv == null || !mssv.matches("\\d{8}")) {
            throw fieldError("mssv", "MSSV phải gồm đúng 8 chữ số");
        }
        users.findByMssv(mssv).filter(existing -> !existing.getId().equals(currentUserId))
                .ifPresent(existing -> { throw new DuplicateException("mssv", "MSSV đã được sử dụng"); });
        return mssv;
    }

    private void validateProfileIds(RoleCode role, Long facultyId, Long departmentId) {
        if (role == RoleCode.STUDENT && facultyId != null && !faculties.existsById(facultyId)) {
            throw fieldError("facultyId", "Khoa không tồn tại");
        }
        if (role == RoleCode.MANAGER && departmentId != null && !departments.existsById(departmentId)) {
            throw fieldError("departmentId", "Phòng ban không tồn tại");
        }
    }

    private static void applyStudent(StudentProfile profile, Long facultyId, String classCode,
                                     String cohort, String program) {
        profile.setFacultyId(facultyId);
        profile.setClassCode(blankToNull(classCode));
        profile.setCohort(blankToNull(cohort));
        profile.setProgram(blankToNull(program));
    }

    private static void applyManager(ManagerProfile profile, Long departmentId, String jobTitle,
                                     String employeeCode) {
        profile.setDepartmentId(departmentId);
        profile.setJobTitle(blankToNull(jobTitle));
        profile.setEmployeeCode(blankToNull(employeeCode));
    }

    private String profileSummary(Long id, RoleCode role) {
        if (role == RoleCode.STUDENT) {
            return students.findByUserId(id).map(profile -> ",facultyId=" + profile.getFacultyId()
                    + ",class=" + profile.getClassCode() + ",cohort=" + profile.getCohort()
                    + ",program=" + profile.getProgram()).orElse("");
        }
        if (role == RoleCode.MANAGER) {
            return managers.findByUserId(id).map(profile -> ",departmentId=" + profile.getDepartmentId()
                    + ",jobTitle=" + profile.getJobTitle() + ",employeeCode=" + profile.getEmployeeCode())
                    .orElse("");
        }
        return "";
    }

    private static String summary(User user) {
        return "name=" + user.getFullName() + ",email=" + user.getEmail() + ",mssv=" + user.getMssv()
                + ",phone=" + user.getPhone() + ",role=" + user.getRole().getCode()
                + ",status=" + user.getStatus();
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static BadRequestException fieldError(String field, String message) {
        return new BadRequestException(message, Map.of(field, message));
    }
}
