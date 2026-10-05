package vn.edu.hcmute.qaute.config.seed;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.entity.identity.Faculty;
import vn.edu.hcmute.qaute.entity.identity.Role;
import vn.edu.hcmute.qaute.entity.identity.StudentProfile;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.FacultyRepository;
import vn.edu.hcmute.qaute.repository.identity.RoleRepository;
import vn.edu.hcmute.qaute.repository.identity.StudentProfileRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;

@Component
public class UserSeeder implements DemoSeeder {

    private static final String ADMIN_EMAIL = "admin@qaute.edu.vn";
    private static final String[] MANAGER_NAMES = {
        "Nguyễn Văn An", "Trần Thị Bình", "Lê Hoàng Cường", "Phạm Thu Dung"
    };
    private static final String[] STUDENT_NAMES = {
        "Nguyễn Minh Anh", "Trần Quốc Bảo", "Lê Thị Cẩm", "Phạm Đức Duy",
        "Hoàng Thu Hà", "Vũ Gia Hân", "Đặng Hữu Khang", "Bùi Ngọc Lan",
        "Đỗ Tuấn Minh", "Ngô Thảo Nguyên", "Dương Anh Phúc", "Lý Khánh Quỳnh",
        "Võ Thành Sang", "Phan Mỹ Tâm", "Trương Văn Uy"
    };

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final FacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, RoleRepository roleRepository,
                      StudentProfileRepository studentProfileRepository,
                      FacultyRepository facultyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.facultyRepository = facultyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int order() {
        return 10;
    }

    @Override
    @Transactional
    public void seed() {
        if (userRepository.findByEmailIgnoreCase(ADMIN_EMAIL).isPresent()) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        Role adminRole = role(RoleCode.ADMIN);
        Role managerRole = role(RoleCode.MANAGER);
        Role studentRole = role(RoleCode.STUDENT);

        saveUser(adminRole, ADMIN_EMAIL, "Quản trị viên QAUTE", null, "Admin@12345", now);
        for (int index = 0; index < MANAGER_NAMES.length; index++) {
            saveUser(managerRole, "manager" + (index + 1) + "@qaute.edu.vn",
                    MANAGER_NAMES[index], null, "Manager@12345", now);
        }

        List<Faculty> faculties = facultyRepository.findByActiveTrueOrderByNameAsc();
        if (faculties.isEmpty()) {
            throw new IllegalStateException("Không có khoa đang hoạt động để seed sinh viên");
        }
        for (int index = 0; index < STUDENT_NAMES.length; index++) {
            String mssv = String.valueOf(24162101 + index);
            User student = saveUser(studentRole, mssv + "@student.hcmute.edu.vn",
                    STUDENT_NAMES[index], mssv, "Student@12345", now);
            Faculty faculty = faculties.get(index % faculties.size());
            studentProfileRepository.save(StudentProfile.builder()
                    .userId(student.getId())
                    .facultyId(faculty.getId())
                    .classCode("241" + faculty.getCode().toUpperCase() + "A")
                    .cohort("2024")
                    .program("Đại học chính quy")
                    .build());
        }
    }

    private Role role(RoleCode code) {
        return roleRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Chưa cấu hình role " + code));
    }

    private User saveUser(Role role, String email, String fullName, String mssv,
                          String password, LocalDateTime now) {
        User user = User.builder()
                .email(email)
                .mssv(mssv)
                .passwordHash(passwordEncoder.encode(password))
                .fullName(fullName)
                .role(role)
                .status(UserStatus.ACTIVE)
                .emailVerifiedAt(now)
                .passwordChangedAt(now)
                .failedLoginCount(0)
                .build();
        return userRepository.save(user);
    }
}
