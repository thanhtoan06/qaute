package vn.edu.hcmute.qaute.config.seed;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    /** MSSV dải seed: 24162101 – 24162130 (30 sinh viên). */
    private static final int STUDENT_MSSV_START = 24162101;

    /** 12 manager mới, mỗi tài khoản phụ trách một nhóm phòng ban. */
    private static final String[] MANAGER_EMAILS = {
        "manager.daotao@qaute.edu.vn",
        "manager.ctsv@qaute.edu.vn",
        "manager.taichinh@qaute.edu.vn",
        "manager.ktx@qaute.edu.vn",
        "manager.cntt@qaute.edu.vn",
        "manager.thuvien@qaute.edu.vn",
        "manager.qhdn@qaute.edu.vn",
        "manager.khcn@qaute.edu.vn",
        "manager.yte@qaute.edu.vn",
        "manager.csvc@qaute.edu.vn",
        "manager.qlcl@qaute.edu.vn",
        "manager.saudh@qaute.edu.vn"
    };

    private static final String[] MANAGER_NAMES = {
        "PGS. TS. Võ Thị Ngà - Phòng Đào tạo",
        "TS. Trần Thanh Thưởng - Phòng Tuyển sinh và Công tác sinh viên",
        "CN. Hồ Thành Công - Phòng Kế hoạch - Tài chính",
        "Nguyễn Thanh Giang - Ban Quản lý Ký túc xá",
        "TS. Huỳnh Nguyên Chính - Trung tâm Thông tin - Máy tính",
        "ThS. Bùi Thị Lan - Trung tâm Thông tin - Thư viện",
        "ThS. Lê Phan Chí Hùng - Phòng Quan hệ Doanh nghiệp",
        "ThS. Vũ Văn Phong - Phòng Khoa học Công nghệ",
        "BS. Nguyễn Từ Vũ - Trạm Y tế",
        "ThS. Quách Văn Thiêm - Phòng Quản trị Cơ sở vật chất",
        "PGS. TS. Đỗ Thành Trung - Phòng Khảo thí và Đảm bảo chất lượng",
        "TS. Nguyễn Thị Tịnh Ấu - Viện Sau đại học"
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
        /* 24162129 */ "Lê Thanh Toàn",
        /* 24162130 */ "Hà Quốc Toản"
    };

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final FacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(
            UserRepository userRepository,
            RoleRepository roleRepository,
            StudentProfileRepository studentProfileRepository,
            FacultyRepository facultyRepository,
            PasswordEncoder passwordEncoder) {
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

        // Tạo Role khi bảng roles trống.
        seedRoles();

        Role adminRole = role(RoleCode.ADMIN);
        Role managerRole = role(RoleCode.MANAGER);
        Role studentRole = role(RoleCode.STUDENT);

        // Admin
        if (userRepository.findByEmailIgnoreCase(ADMIN_EMAIL).isEmpty()) {
            saveUser(
                    adminRole,
                    ADMIN_EMAIL,
                    "Quản trị viên QAUTE",
                    null,
                    "Admin@12345",
                    now);
        }

        // Chỉ tạo 12 manager mới theo phòng ban.
        // Không tạo lại các tài khoản manager1, manager2, manager3, manager4.
        for (int i = 0; i < MANAGER_NAMES.length; i++) {
            String email = MANAGER_EMAILS[i];

            if (userRepository.findByEmailIgnoreCase(email).isEmpty()) {
                saveUser(
                        managerRole,
                        email,
                        MANAGER_NAMES[i],
                        null,
                        "Manager@12345",
                        now);
            }
        }

        // Tạo Faculty demo nếu bảng faculties chưa có dữ liệu.
        List<Faculty> faculties =
                facultyRepository.findByActiveTrueOrderByNameAsc();

        if (faculties.isEmpty() && facultyRepository.count() == 0) {
            seedFaculties();
            faculties = facultyRepository.findByActiveTrueOrderByNameAsc();
        }

        if (faculties.isEmpty()) {
            throw new IllegalStateException(
                    "Không có khoa đang hoạt động để seed sinh viên");
        }

        // Student accounts: 24162101 → 24162130.
        for (int i = 0; i < STUDENT_NAMES.length; i++) {
            String mssv = String.valueOf(STUDENT_MSSV_START + i);
            String email = mssv + "@student.hcmute.edu.vn";

            if (userRepository.findByEmailIgnoreCase(email).isPresent()
                    || userRepository.findByMssv(mssv).isPresent()) {
                continue;
            }

            User student = saveUser(
                    studentRole,
                    email,
                    STUDENT_NAMES[i],
                    mssv,
                    "Student@12345",
                    now);

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

    /**
     * Tạo các Role được khai báo trong RoleCode nếu chưa tồn tại.
     */
    private void seedRoles() {
        for (RoleCode code : RoleCode.values()) {
            if (roleRepository.findByCode(code).isPresent()) {
                continue;
            }

            Role role = Role.builder()
                    .code(code)
                    .name(roleName(code))
                    .build();

            roleRepository.save(role);
        }
    }

    private String roleName(RoleCode code) {
        if (code == RoleCode.ADMIN) {
            return "Quản trị viên";
        }

        if (code == RoleCode.MANAGER) {
            return "Tư vấn viên";
        }

        if (code == RoleCode.STUDENT) {
            return "Sinh viên";
        }

        return code.name().replace('_', ' ');
    }

    /**
     * Tạo các khoa demo cho sinh viên khi bảng faculties trống.
     */
    private void seedFaculties() {
        List<Faculty> faculties = new ArrayList<>();

        faculties.add(Faculty.builder()
                .code("CNTT")
                .name("Khoa Công nghệ thông tin")
                .active(true)
                .build());

        faculties.add(Faculty.builder()
                .code("DDT")
                .name("Khoa Điện - Điện tử")
                .active(true)
                .build());

        faculties.add(Faculty.builder()
                .code("CKM")
                .name("Khoa Cơ khí Chế tạo máy")
                .active(true)
                .build());

        faculties.add(Faculty.builder()
                .code("CKDL")
                .name("Khoa Cơ khí Động lực")
                .active(true)
                .build());

        faculties.add(Faculty.builder()
                .code("CNHH")
                .name("Khoa Công nghệ Hóa học và Thực phẩm")
                .active(true)
                .build());

        faculties.add(Faculty.builder()
                .code("XD")
                .name("Khoa Xây dựng")
                .active(true)
                .build());

        faculties.add(Faculty.builder()
                .code("KT")
                .name("Khoa Kinh tế")
                .active(true)
                .build());

        faculties.add(Faculty.builder()
                .code("CNMT")
                .name("Khoa Công nghệ May và Thời trang")
                .active(true)
                .build());

        faculties.add(Faculty.builder()
                .code("IN")
                .name("Khoa In và Truyền thông")
                .active(true)
                .build());

        faculties.add(Faculty.builder()
                .code("CNDT")
                .name("Khoa Công nghệ điện tử")
                .active(true)
                .build());

        faculties.add(Faculty.builder()
                .code("SPKT")
                .name("Khoa Sư phạm kỹ thuật")
                .active(true)
                .build());

        faculties.add(Faculty.builder()
                .code("CLC")
                .name("Chương trình đào tạo chất lượng cao")
                .active(true)
                .build());

        facultyRepository.saveAll(faculties);
    }

    private Role role(RoleCode code) {
        return roleRepository.findByCode(code)
                .orElseThrow(() ->
                        new IllegalStateException("Chưa cấu hình role " + code));
    }

    private User saveUser(
            Role role,
            String email,
            String fullName,
            String mssv,
            String password,
            LocalDateTime now) {
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