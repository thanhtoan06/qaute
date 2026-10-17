package vn.edu.hcmute.qaute.service.admin;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
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
import vn.edu.hcmute.qaute.service.identity.InvitationService;

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
    private final AuditService audit;

    public AdminUserService(UserRepository users, RoleRepository roles,
                            StudentProfileRepository students, ManagerProfileRepository managers,
                            FacultyRepository faculties, DepartmentRepository departments,
                            InvitationService invitations, AuditService audit) {
        this.users = users;
        this.roles = roles;
        this.students = students;
        this.managers = managers;
        this.faculties = faculties;
        this.departments = departments;
        this.invitations = invitations;
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
