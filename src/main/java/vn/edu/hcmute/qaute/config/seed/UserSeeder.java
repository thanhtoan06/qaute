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

// Tạo các tài khoản demo và hồ sơ sinh viên, tư vấn viên còn thiếu.
@Component
public class UserSeeder implements DemoSeeder {

    private static final String ADMIN_EMAIL = "admin@qaute.edu.vn";

    /** MSSV dải seed mới: 111111 – 111130 (20 sinh viên). */
    private static final int STUDENT_MSSV_START = 111111;

    /** 12 manager, mỗi tài khoản phụ trách một nhóm phòng ban. */
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
        /* 111111 */ "Nguyễn Minh Anh",
        /* 111112 */ "Trần Quốc Bảo",
        /* 111113 */ "Lê Thị Cẩm",
        /* 111114 */ "Phạm Đức Duy",
        /* 111115 */ "Hoàng Thu Hà",
        /* 111116 */ "Vũ Gia Hân",
        /* 111117 */ "Đặng Hữu Khang",
        /* 111118 */ "Bùi Ngọc Lan",
        /* 111119 */ "Đỗ Tuấn Minh",
        /* 111120 */ "Ngô Thảo Nguyên",
        /* 111121 */ "Dương Anh Phúc",
        /* 111122 */ "Lý Khánh Quỳnh",
        /* 111123 */ "Võ Thành Sang",
        /* 111124 */ "Phan Mỹ Tâm",
        /* 111125 */ "Trương Văn Uy",
        /* 111126 */ "Nguyễn Thế Vinh",
        /* 111127 */ "Đinh Thị Xuân",
        /* 111128 */ "Cao Minh Yến",
        /* 111129 */ "Hồ Quang Hải",
        /* 111130 */ "Mai Thùy Linh"
    };

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final FacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;

    // Khởi tạo seeder người dùng với các kho dữ liệu cần thiết.
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

    // Xác định thứ tự chạy của seeder người dùng.
    @Override
    public int order() {
        return 10;
    }

    // Tạo các tài khoản và hồ sơ demo còn thiếu.
    @Override
    @Transactional
    public void seed() {
        LocalDateTime now = LocalDateTime.now();

        // Tạo Role khi chưa tồn tại.
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

        // Tạo 12 manager theo phòng ban.
        // Không tạo lại nếu tài khoản đã tồn tại.
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

        Faculty informationTechnologyFaculty = faculties.stream()
                .filter(faculty -> "CNTT".equalsIgnoreCase(faculty.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Không tìm thấy Khoa Công nghệ thông tin (CNTT) để seed sinh viên"));

        // Tạo 20 tài khoản sinh viên mới: 111111 → 111130.
        // Email: MSSV@qaute.edu.vn.
        for (int i = 0; i < STUDENT_NAMES.length; i++) {
            String mssv = String.valueOf(STUDENT_MSSV_START + i);
            String email = mssv + "@qaute.edu.vn";

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
                    .classCode("111" + faculty.getCode().toUpperCase() + "A")
                    .cohort("2026")
                    .program("Đại học chính quy")
                    .build());
        }
    }

    /**
     * Tạo các Role được khai báo trong RoleCode nếu chưa tồn tại.
     */
    // Bổ sung các vai trò chưa có trong cơ sở dữ liệu.
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

    // Lấy tên hiển thị tương ứng với mã vai trò.
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
    // Tạo dữ liệu khoa mặc định cho môi trường demo.
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

    // Tìm vai trò đã cấu hình theo mã.
    private Role role(RoleCode code) {
        return roleRepository.findByCode(code)
                .orElseThrow(() ->
                        new IllegalStateException("Chưa cấu hình role " + code));
    }

    // Tạo và lưu tài khoản người dùng với thông tin được cung cấp.
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