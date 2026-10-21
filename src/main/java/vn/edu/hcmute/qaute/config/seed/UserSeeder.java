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

    /** MSSV dải seed: 24162101 – 24162130 (30 sinh viên) */
    private static final int STUDENT_MSSV_START = 24162101;

    private static final String[] MANAGER_NAMES = {
        "Nguyễn Văn An", "Trần Thị Bình", "Lê Hoàng Cường", "Phạm Thu Dung"
    };

    private static final String[] STUDENT_NAMES = {
        /* 24162101 */ "Nguyễn Minh Anh",
        /* 24162102 */ "Trần Quốc Bảo",
        /* 24162103 */ "Lê Thị Cẩm",
        /* 24162104 */ "Phạm Đức Duy",
        /* 24162105 */ "Hoàng Thu Hà",
        /* 24162106 */ "Vũ Gia Hân",
        /* 24162107 */ "Đặng Hữu Khang",
        /* 24162108 */ "Bùi Ngọc Lan",
        /* 24162109 */ "Đỗ Tuấn Minh",
        /* 24162110 */ "Ngô Thảo Nguyên",
        /* 24162111 */ "Dương Anh Phúc",
        /* 24162112 */ "Lý Khánh Quỳnh",
        /* 24162113 */ "Võ Thành Sang",
        /* 24162114 */ "Phan Mỹ Tâm",
        /* 24162115 */ "Trương Văn Uy",
        /* 24162116 */ "Nguyễn Thế Vinh",
        /* 24162117 */ "Đinh Thị Xuân",
        /* 24162118 */ "Cao Minh Yến",
        /* 24162119 */ "Hồ Quang Hải",
        /* 24162120 */ "Mai Thùy Linh",
        /* 24162121 */ "Tô Đức Mạnh",
        /* 24162122 */ "Châu Ngọc Nhi",
        /* 24162123 */ "Lê Văn Phong",
        /* 24162124 */ "Trịnh Hải Quân",
        /* 24162125 */ "Huỳnh Thị Sen",
        /* 24162126 */ "Phan Văn Thịnh",
        /* 24162127 */ "Ngô Minh Tuấn",
        /* 24162128 */ "Đoàn Thị Út",
        /* 24162129 */ "Bùi Trọng Vĩnh",
        /* 24162130 */ "Lưu Khánh Vy"
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
        LocalDateTime now = LocalDateTime.now();
        Role adminRole    = role(RoleCode.ADMIN);
        Role managerRole  = role(RoleCode.MANAGER);
        Role studentRole  = role(RoleCode.STUDENT);

        // Admin
        if (userRepository.findByEmailIgnoreCase(ADMIN_EMAIL).isEmpty()) {
            saveUser(adminRole, ADMIN_EMAIL, "Quản trị viên QAUTE", null, "Admin@12345", now);
        }

        // Manager accounts
        for (int i = 0; i < MANAGER_NAMES.length; i++) {
            String email = "manager" + (i + 1) + "@qaute.edu.vn";
            if (userRepository.findByEmailIgnoreCase(email).isEmpty()) {
                saveUser(managerRole, email, MANAGER_NAMES[i], null, "Manager@12345", now);
            }
        }

        // Student accounts: 24162101 → 24162130 (idempotent — bỏ qua nếu đã tồn tại)
        List<Faculty> faculties = facultyRepository.findByActiveTrueOrderByNameAsc();
        if (faculties.isEmpty()) {
            throw new IllegalStateException("Không có khoa đang hoạt động để seed sinh viên");
        }
        for (int i = 0; i < STUDENT_NAMES.length; i++) {
            String mssv  = String.valueOf(STUDENT_MSSV_START + i);
            String email = mssv + "@student.hcmute.edu.vn";
            if (userRepository.findByEmailIgnoreCase(email).isPresent()
                    || userRepository.findByMssv(mssv).isPresent()) {
                continue; // đã tồn tại → bỏ qua
            }
            User student = saveUser(studentRole, email, STUDENT_NAMES[i], mssv, "Student@12345", now);
            Faculty faculty = faculties.get(i % faculties.size());
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