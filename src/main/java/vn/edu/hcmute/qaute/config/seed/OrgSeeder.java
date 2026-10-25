package vn.edu.hcmute.qaute.config.seed;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.constant.ManagerCategoryLevel;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.entity.organization.Department;
import vn.edu.hcmute.qaute.entity.organization.ManagerCategory;
import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerCategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerProfileRepository;

/** Nạp dữ liệu demo cho miền tổ chức sau khi UserSeeder đã tạo 12 manager mới. */
@Component
public class OrgSeeder implements DemoSeeder {

    private static final String DAOTAO = "DAOTAO";
    private static final String CTSV = "CTSV";
    private static final String TAICHINH = "TAICHINH";
    private static final String KTX = "KTX";

    private static final String CNTT = "CNTT";
    private static final String THUVIEN = "THUVIEN";
    private static final String QHDN = "QHDN";
    private static final String KHCN = "KHCN";
    private static final String YTE = "YTE";
    private static final String CSVC = "CSVC";
    private static final String QLCL = "QLCL";
    private static final String SAUDH = "SAUDH";

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

    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;
    private final ManagerProfileRepository managerProfileRepository;
    private final ManagerCategoryRepository managerCategoryRepository;
    private final UserRepository userRepository;

    public OrgSeeder(
            DepartmentRepository departmentRepository,
            CategoryRepository categoryRepository,
            ManagerProfileRepository managerProfileRepository,
            ManagerCategoryRepository managerCategoryRepository,
            UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.categoryRepository = categoryRepository;
        this.managerProfileRepository = managerProfileRepository;
        this.managerCategoryRepository = managerCategoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public int order() {
        return 20;
    }

    /**
     * Nạp dữ liệu tổ chức từ database demo trống.
     * DAOTAO là mốc kiểm tra dữ liệu đã được nạp.
     */
    @Override
    @Transactional
    public void seed() {
        if (departmentRepository.existsByCode(DAOTAO)) {
            return;
        }

        // =====================================================
        // 1. TẠO 12 PHÒNG BAN
        // =====================================================

        Department daoTao = saveDepartment(
                DAOTAO,
                "Phòng Đào tạo",
                "daotao@hcmute.edu.vn",
                "Tòa A, tầng 1",
                1);

        Department congTacSinhVien = saveDepartment(
                CTSV,
                "Phòng Công tác sinh viên",
                "ctsv@hcmute.edu.vn",
                "Tòa A, tầng 2",
                2);

        Department taiChinh = saveDepartment(
                TAICHINH,
                "Phòng Kế hoạch – Tài chính",
                "taichinh@hcmute.edu.vn",
                "Tòa B, tầng 1",
                3);

        Department kyTucXa = saveDepartment(
                KTX,
                "Ký túc xá & Dịch vụ sinh viên",
                "ktx@hcmute.edu.vn",
                "Khu KTX A",
                4);

        Department trungTamCNTT = saveDepartment(
                CNTT,
                "Trung tâm Thông tin - Máy tính",
                "cntt@hcmute.edu.vn",
                "Tòa A, khu kỹ thuật",
                5);

        Department thuVien = saveDepartment(
                THUVIEN,
                "Trung tâm Thông tin - Thư viện",
                "thuvien@hcmute.edu.vn",
                "Trung tâm thư viện",
                6);

        Department quanHeDoanhNghiep = saveDepartment(
                QHDN,
                "Phòng Quan hệ Doanh nghiệp",
                "qhdn@hcmute.edu.vn",
                "Tòa A, khu hỗ trợ sinh viên",
                7);

        Department khoaHocCongNghe = saveDepartment(
                KHCN,
                "Phòng Khoa học Công nghệ",
                "khcn@hcmute.edu.vn",
                "Tòa B, khu nghiên cứu",
                8);

        Department tramYTe = saveDepartment(
                YTE,
                "Trạm Y tế",
                "yte@hcmute.edu.vn",
                "Khu dịch vụ sinh viên",
                9);

        Department coSoVatChat = saveDepartment(
                CSVC,
                "Phòng Quản trị Cơ sở vật chất",
                "csvc@hcmute.edu.vn",
                "Tòa hành chính",
                10);

        Department khaoThi = saveDepartment(
                QLCL,
                "Phòng Khảo thí và Đảm bảo chất lượng",
                "qlcl@hcmute.edu.vn",
                "Tòa A, khu học vụ",
                11);

        Department sauDaiHoc = saveDepartment(
                SAUDH,
                "Viện Sau đại học",
                "saudh@hcmute.edu.vn",
                "Tòa B, khu đào tạo sau đại học",
                12);

        // =====================================================
        // 2. PHÒNG ĐÀO TẠO
        // Giữ nguyên các mã DT_DKHP, DT_HOCVU, DT_TOTNGHIEP.
        // =====================================================

        Category dangKyHocPhan = saveRootCategory(
                "DT_DKHP",
                "Đăng ký học phần",
                daoTao,
                "bi-journal-check",
                1);

        Category hocVu = saveRootCategory(
                "DT_HOCVU",
                "Học vụ & điểm",
                daoTao,
                "bi-mortarboard",
                2);

        Category totNghiep = saveRootCategory(
                "DT_TOTNGHIEP",
                "Xét tốt nghiệp",
                daoTao,
                "bi-award",
                3);

        saveChildCategory(
                "DT_DKHP_SAI",
                "Đăng ký sai / rút học phần",
                dangKyHocPhan,
                1);

        saveChildCategory(
                "DT_DKHP_HUY",
                "Hủy học phần",
                dangKyHocPhan,
                2);

        saveChildCategory(
                "DT_HOCVU_DIEM",
                "Tra cứu và điều chỉnh điểm",
                hocVu,
                1);

        saveChildCategory(
                "DT_HOCVU_BAOLUU",
                "Bảo lưu và tiếp tục học",
                hocVu,
                2);

        saveChildCategory(
                "DT_TOTNGHIEP_HOSO",
                "Hồ sơ xét tốt nghiệp",
                totNghiep,
                1);

        // =====================================================
        // 3. PHÒNG CÔNG TÁC SINH VIÊN
        // =====================================================

        Category hocBong = saveRootCategory(
                "CT_HOCBONG",
                "Học bổng & chính sách",
                congTacSinhVien,
                "bi-gift",
                1);

        Category renLuyen = saveRootCategory(
                "CT_RENLUYEN",
                "Điểm rèn luyện",
                congTacSinhVien,
                "bi-star",
                2);

        Category xacNhanSinhVien = saveRootCategory(
                "CT_XACNHAN",
                "Giấy xác nhận sinh viên",
                congTacSinhVien,
                "bi-file-earmark-text",
                3);

        Category hoTroSinhVien = saveRootCategory(
                "CT_HOTRO",
                "Hỗ trợ sinh viên",
                congTacSinhVien,
                "bi-heart",
                4);

        saveChildCategory(
                "CT_HOCBONG_HOSO",
                "Hồ sơ xét học bổng",
                hocBong,
                1);

        saveChildCategory(
                "CT_HOCBONG_KETQUA",
                "Kết quả xét học bổng",
                hocBong,
                2);

        saveChildCategory(
                "CT_RENLUYEN_DIEM",
                "Tra cứu điểm rèn luyện",
                renLuyen,
                1);

        saveChildCategory(
                "CT_RENLUYEN_MINHCHUNG",
                "Minh chứng điểm rèn luyện",
                renLuyen,
                2);

        saveChildCategory(
                "CT_XACNHAN_GIAYTO",
                "Thủ tục giấy xác nhận",
                xacNhanSinhVien,
                1);

        // =====================================================
        // 4. PHÒNG KẾ HOẠCH – TÀI CHÍNH
        // =====================================================

        Category hocPhi = saveRootCategory(
                "TC_HOCPHI",
                "Học phí",
                taiChinh,
                "bi-cash-coin",
                1);

        Category mienGiam = saveRootCategory(
                "TC_MIENGIAM",
                "Miễn giảm & hoàn trả",
                taiChinh,
                "bi-receipt",
                2);

        Category chungTu = saveRootCategory(
                "TC_CHUNGTU",
                "Chứng từ và biên nhận",
                taiChinh,
                "bi-file-earmark",
                3);

        Category thanhToan = saveRootCategory(
                "TC_THANHTOAN",
                "Thanh toán và đối soát",
                taiChinh,
                "bi-credit-card",
                4);

        saveChildCategory(
                "TC_HOCPHI_DOISOAT",
                "Đối soát học phí",
                hocPhi,
                1);

        saveChildCategory(
                "TC_HOCPHI_GIAHAN",
                "Gia hạn học phí",
                hocPhi,
                2);

        saveChildCategory(
                "TC_MIENGIAM_HOSO",
                "Hồ sơ miễn giảm học phí",
                mienGiam,
                1);

        saveChildCategory(
                "TC_THANHTOAN_LOI",
                "Lỗi giao dịch thanh toán",
                thanhToan,
                1);

        // =====================================================
        // 5. KÝ TÚC XÁ
        // =====================================================

        Category dangKyKTX = saveRootCategory(
                "KTX_DANGKY",
                "Đăng ký ký túc xá",
                kyTucXa,
                "bi-house-door",
                1);

        Category dichVuKTX = saveRootCategory(
                "KTX_DICHVU",
                "Dịch vụ & sửa chữa",
                kyTucXa,
                "bi-tools",
                2);

        saveChildCategory(
                "KTX_DANGKY_HOSO",
                "Hồ sơ đăng ký nội trú",
                dangKyKTX,
                1);

        saveChildCategory(
                "KTX_DANGKY_GIAHAN",
                "Gia hạn lưu trú",
                dangKyKTX,
                2);

        saveChildCategory(
                "KTX_DICHVU_SUA",
                "Sửa chữa phòng ở",
                dichVuKTX,
                1);

        saveChildCategory(
                "KTX_DICHVU_DIENNUOC",
                "Điện, nước và tiện ích",
                dichVuKTX,
                2);

        // =====================================================
        // 6. TRUNG TÂM THÔNG TIN - MÁY TÍNH
        // =====================================================

        Category taiKhoanCNTT = saveRootCategory(
                "CNTT_TAIKHOAN",
                "Tài khoản và đăng nhập",
                trungTamCNTT,
                "bi-person-lock",
                1);

        Category portalCNTT = saveRootCategory(
                "CNTT_PORTAL",
                "Hỗ trợ QAUTE Portal",
                trungTamCNTT,
                "bi-globe",
                2);

        saveRootCategory(
                "CNTT_BAOMAT",
                "Bảo mật tài khoản",
                trungTamCNTT,
                "bi-shield-lock",
                3);

        saveChildCategory(
                "CNTT_TAIKHOAN_KHOIPHUC",
                "Khôi phục tài khoản",
                taiKhoanCNTT,
                1);

        saveChildCategory(
                "CNTT_PORTAL_LOI",
                "Lỗi sử dụng Portal",
                portalCNTT,
                1);

        // =====================================================
        // 7. TRUNG TÂM THÔNG TIN - THƯ VIỆN
        // =====================================================

        Category muonTra = saveRootCategory(
                "TV_MUONTRA",
                "Mượn và trả tài liệu",
                thuVien,
                "bi-book",
                1);

        Category traCuu = saveRootCategory(
                "TV_TRACUU",
                "Tra cứu tài liệu",
                thuVien,
                "bi-search",
                2);

        saveRootCategory(
                "TV_TAIKHOAN",
                "Thẻ và tài khoản thư viện",
                thuVien,
                "bi-person-vcard",
                3);

        saveChildCategory(
                "TV_MUONTRA_GIAHAN",
                "Gia hạn mượn tài liệu",
                muonTra,
                1);

        saveChildCategory(
                "TV_TRACUU_TAILIEU",
                "Hỗ trợ tra cứu tài liệu",
                traCuu,
                1);

        // =====================================================
        // 8. PHÒNG QUAN HỆ DOANH NGHIỆP
        // =====================================================

        Category thucTap = saveRootCategory(
                "QHDN_THUCTAP",
                "Thực tập doanh nghiệp",
                quanHeDoanhNghiep,
                "bi-briefcase",
                1);

        Category tuyenDung = saveRootCategory(
                "QHDN_TUYENDUNG",
                "Thông tin tuyển dụng",
                quanHeDoanhNghiep,
                "bi-person-workspace",
                2);

        saveRootCategory(
                "QHDN_CV",
                "CV và kỹ năng phỏng vấn",
                quanHeDoanhNghiep,
                "bi-file-person",
                3);

        saveChildCategory(
                "QHDN_THUCTAP_HOSO",
                "Hồ sơ thực tập",
                thucTap,
                1);

        saveChildCategory(
                "QHDN_TUYENDUNG_HOSO",
                "Hồ sơ ứng tuyển",
                tuyenDung,
                1);

        // =====================================================
        // 9. PHÒNG KHOA HỌC CÔNG NGHỆ
        // =====================================================

        Category deTai = saveRootCategory(
                "KHCN_DETAI",
                "Đề tài nghiên cứu khoa học",
                khoaHocCongNghe,
                "bi-lightbulb",
                1);

        Category hoiThao = saveRootCategory(
                "KHCN_HOITHAO",
                "Hội thảo khoa học",
                khoaHocCongNghe,
                "bi-easel",
                2);

        saveRootCategory(
                "KHCN_NGHIENCUU",
                "Hoạt động nghiên cứu",
                khoaHocCongNghe,
                "bi-journal-richtext",
                3);

        saveChildCategory(
                "KHCN_DETAI_HOSO",
                "Hồ sơ đăng ký đề tài",
                deTai,
                1);

        saveChildCategory(
                "KHCN_HOITHAO_DANGKY",
                "Đăng ký hội thảo",
                hoiThao,
                1);

        // =====================================================
        // 10. TRẠM Y TẾ
        // =====================================================

        Category baoHiemYTe = saveRootCategory(
                "YTE_BHYT",
                "Bảo hiểm y tế",
                tramYTe,
                "bi-heart-pulse",
                1);

        Category sucKhoe = saveRootCategory(
                "YTE_SUCKHOE",
                "Thông tin sức khỏe",
                tramYTe,
                "bi-clipboard2-pulse",
                2);

        saveRootCategory(
                "YTE_HOTRO",
                "Hỗ trợ y tế sinh viên",
                tramYTe,
                "bi-hospital",
                3);

        saveChildCategory(
                "YTE_BHYT_HOSO",
                "Hồ sơ bảo hiểm y tế",
                baoHiemYTe,
                1);

        saveChildCategory(
                "YTE_SUCKHOE_TUVAN",
                "Tư vấn sức khỏe",
                sucKhoe,
                1);

        // =====================================================
        // 11. PHÒNG QUẢN TRỊ CƠ SỞ VẬT CHẤT
        // =====================================================

        Category suaChua = saveRootCategory(
                "CSVC_SUACHUA",
                "Sửa chữa cơ sở vật chất",
                coSoVatChat,
                "bi-wrench-adjustable",
                1);

        Category phongHoc = saveRootCategory(
                "CSVC_PHONGHOC",
                "Phòng học và phòng chức năng",
                coSoVatChat,
                "bi-building",
                2);

        saveRootCategory(
                "CSVC_THIETBI",
                "Thiết bị và tiện ích",
                coSoVatChat,
                "bi-pc-display",
                3);

        saveChildCategory(
                "CSVC_SUACHUA_BAOHONG",
                "Báo hỏng thiết bị",
                suaChua,
                1);

        saveChildCategory(
                "CSVC_PHONGHOC_DANGKY",
                "Thông tin sử dụng phòng",
                phongHoc,
                1);

        // =====================================================
        // 12. PHÒNG KHẢO THÍ VÀ ĐẢM BẢO CHẤT LƯỢNG
        // =====================================================

        Category lichThi = saveRootCategory(
                "QLCL_LICHTHI",
                "Lịch thi và phòng thi",
                khaoThi,
                "bi-calendar-check",
                1);

        Category phucKhao = saveRootCategory(
                "QLCL_PHUCKHAO",
                "Phúc khảo",
                khaoThi,
                "bi-file-earmark-check",
                2);

        saveRootCategory(
                "QLCL_KHAOSAT",
                "Khảo sát chất lượng đào tạo",
                khaoThi,
                "bi-clipboard-data",
                3);

        saveChildCategory(
                "QLCL_LICHTHI_TRUNGLICH",
                "Xử lý trùng lịch thi",
                lichThi,
                1);

        saveChildCategory(
                "QLCL_PHUCKHAO_HOSO",
                "Hồ sơ phúc khảo",
                phucKhao,
                1);

        // =====================================================
        // 13. VIỆN SAU ĐẠI HỌC
        // =====================================================

        Category tuyenSinhSauDH = saveRootCategory(
                "SDH_TUYENSINH",
                "Tuyển sinh sau đại học",
                sauDaiHoc,
                "bi-mortarboard",
                1);

        Category hoSoSauDH = saveRootCategory(
                "SDH_HOSO",
                "Hồ sơ sau đại học",
                sauDaiHoc,
                "bi-folder2-open",
                2);

        saveRootCategory(
                "SDH_HOCVU",
                "Học vụ sau đại học",
                sauDaiHoc,
                "bi-journal-bookmark",
                3);

        saveChildCategory(
                "SDH_TUYENSINH_HOSO",
                "Hồ sơ dự tuyển sau đại học",
                tuyenSinhSauDH,
                1);

        saveChildCategory(
                "SDH_HOSO_BOSUNG",
                "Bổ sung hồ sơ sau đại học",
                hoSoSauDH,
                1);

        // =====================================================
        // 14. TẠO HỒ SƠ MANAGER
        // =====================================================

        createProfiles(
                daoTao,
                congTacSinhVien,
                taiChinh,
                kyTucXa);

        createAdditionalProfiles(
                trungTamCNTT,
                thuVien,
                quanHeDoanhNghiep,
                khoaHocCongNghe,
                tramYTe,
                coSoVatChat,
                khaoThi,
                sauDaiHoc);

        // =====================================================
        // 15. PHÂN CÔNG MANAGER VÀO CHUYÊN MỤC
        // =====================================================

        createAssignments();
        createAdditionalAssignments();

        System.out.println(
                "[OrgSeeder] Đã tạo 12 phòng ban, "
                        + "71 chuyên mục và phân công 12 manager.");
    }

    private Department saveDepartment(
            String code,
            String name,
            String email,
            String location,
            int sortOrder) {
        return departmentRepository.save(Department.builder()
                .code(code)
                .name(name)
                .email(email)
                .location(location)
                .sortOrder(sortOrder)
                .active(true)
                .build());
    }

    private Category saveRootCategory(
            String code,
            String name,
            Department department,
            String icon,
            int sortOrder) {
        return categoryRepository.save(Category.builder()
                .departmentId(department.getId())
                .depth(1)
                .code(code)
                .name(name)
                .icon(icon)
                .sortOrder(sortOrder)
                .active(true)
                .build());
    }

    private void saveChildCategory(
            String code,
            String name,
            Category parent,
            int sortOrder) {
        categoryRepository.save(Category.builder()
                .departmentId(parent.getDepartmentId())
                .parentId(parent.getId())
                .depth(2)
                .code(code)
                .name(name)
                .sortOrder(sortOrder)
                .active(true)
                .build());
    }

    private void createProfiles(
            Department daoTao,
            Department congTacSinhVien,
            Department taiChinh,
            Department kyTucXa) {
        managerProfileRepository.saveAll(List.of(
                profile(manager(1), "CB001", daoTao, AvailabilityStatus.ONLINE),
                profile(manager(2), "CB002", congTacSinhVien, AvailabilityStatus.ONLINE),
                profile(manager(3), "CB003", taiChinh, AvailabilityStatus.OFFLINE),
                profile(manager(4), "CB004", kyTucXa, AvailabilityStatus.OFFLINE)));
    }

    private void createAdditionalProfiles(
            Department cntt,
            Department thuVien,
            Department qhDoanhNghiep,
            Department khoaHocCongNghe,
            Department yTe,
            Department coSoVatChat,
            Department khaoThi,
            Department sauDaiHoc) {
        managerProfileRepository.saveAll(List.of(
                profile(manager(5), "CB005", cntt, AvailabilityStatus.ONLINE),
                profile(manager(6), "CB006", thuVien, AvailabilityStatus.OFFLINE),
                profile(manager(7), "CB007", qhDoanhNghiep, AvailabilityStatus.ONLINE),
                profile(manager(8), "CB008", khoaHocCongNghe, AvailabilityStatus.OFFLINE),
                profile(manager(9), "CB009", yTe, AvailabilityStatus.ONLINE),
                profile(manager(10), "CB010", coSoVatChat, AvailabilityStatus.OFFLINE),
                profile(manager(11), "CB011", khaoThi, AvailabilityStatus.ONLINE),
                profile(manager(12), "CB012", sauDaiHoc, AvailabilityStatus.OFFLINE)));
    }

    private void createAssignments() {
        User manager1 = manager(1);
        User manager2 = manager(2);
        User manager3 = manager(3);
        User manager4 = manager(4);

        // Giữ nguyên 9 phân công nghiệp vụ gốc.
        managerCategoryRepository.saveAll(List.of(
                assignment(manager1, "DT_DKHP", ManagerCategoryLevel.PRIMARY),
                assignment(manager1, "DT_HOCVU", ManagerCategoryLevel.PRIMARY),
                assignment(manager1, "DT_TOTNGHIEP", ManagerCategoryLevel.PRIMARY),
                assignment(manager2, "CT_HOCBONG", ManagerCategoryLevel.SECONDARY),
                assignment(manager2, "CT_RENLUYEN", ManagerCategoryLevel.SECONDARY),
                assignment(manager3, "TC_HOCPHI", ManagerCategoryLevel.SECONDARY),
                assignment(manager3, "TC_MIENGIAM", ManagerCategoryLevel.SECONDARY),
                assignment(manager4, "KTX_DANGKY", ManagerCategoryLevel.SECONDARY),
                assignment(manager4, "KTX_DICHVU", ManagerCategoryLevel.SECONDARY)));
    }

    private void createAdditionalAssignments() {
        User manager1 = manager(1);
        User manager2 = manager(2);
        User manager3 = manager(3);
        User manager4 = manager(4);
        User manager5 = manager(5);
        User manager6 = manager(6);
        User manager7 = manager(7);
        User manager8 = manager(8);
        User manager9 = manager(9);
        User manager10 = manager(10);
        User manager11 = manager(11);
        User manager12 = manager(12);

        managerCategoryRepository.saveAll(List.of(
                // Phòng Đào tạo: chuyên mục con.
                assignment(manager1, "DT_DKHP_SAI", ManagerCategoryLevel.PRIMARY),
                assignment(manager1, "DT_DKHP_HUY", ManagerCategoryLevel.PRIMARY),
                assignment(manager1, "DT_HOCVU_DIEM", ManagerCategoryLevel.PRIMARY),
                assignment(manager1, "DT_HOCVU_BAOLUU", ManagerCategoryLevel.PRIMARY),
                assignment(manager1, "DT_TOTNGHIEP_HOSO", ManagerCategoryLevel.PRIMARY),

                // Công tác sinh viên.
                assignment(manager2, "CT_XACNHAN", ManagerCategoryLevel.SECONDARY),
                assignment(manager2, "CT_HOTRO", ManagerCategoryLevel.SECONDARY),
                assignment(manager2, "CT_HOCBONG_HOSO", ManagerCategoryLevel.SECONDARY),
                assignment(manager2, "CT_HOCBONG_KETQUA", ManagerCategoryLevel.SECONDARY),
                assignment(manager2, "CT_RENLUYEN_DIEM", ManagerCategoryLevel.SECONDARY),
                assignment(manager2, "CT_RENLUYEN_MINHCHUNG", ManagerCategoryLevel.SECONDARY),
                assignment(manager2, "CT_XACNHAN_GIAYTO", ManagerCategoryLevel.SECONDARY),

                // Kế hoạch - Tài chính.
                assignment(manager3, "TC_CHUNGTU", ManagerCategoryLevel.SECONDARY),
                assignment(manager3, "TC_THANHTOAN", ManagerCategoryLevel.SECONDARY),
                assignment(manager3, "TC_HOCPHI_DOISOAT", ManagerCategoryLevel.SECONDARY),
                assignment(manager3, "TC_HOCPHI_GIAHAN", ManagerCategoryLevel.SECONDARY),
                assignment(manager3, "TC_MIENGIAM_HOSO", ManagerCategoryLevel.SECONDARY),
                assignment(manager3, "TC_THANHTOAN_LOI", ManagerCategoryLevel.SECONDARY),

                // Ký túc xá.
                assignment(manager4, "KTX_DANGKY_HOSO", ManagerCategoryLevel.SECONDARY),
                assignment(manager4, "KTX_DANGKY_GIAHAN", ManagerCategoryLevel.SECONDARY),
                assignment(manager4, "KTX_DICHVU_SUA", ManagerCategoryLevel.SECONDARY),
                assignment(manager4, "KTX_DICHVU_DIENNUOC", ManagerCategoryLevel.SECONDARY),

                // Trung tâm Thông tin - Máy tính.
                assignment(manager5, "CNTT_TAIKHOAN", ManagerCategoryLevel.PRIMARY),
                assignment(manager5, "CNTT_PORTAL", ManagerCategoryLevel.SECONDARY),
                assignment(manager5, "CNTT_BAOMAT", ManagerCategoryLevel.SECONDARY),
                assignment(manager5, "CNTT_TAIKHOAN_KHOIPHUC", ManagerCategoryLevel.SECONDARY),
                assignment(manager5, "CNTT_PORTAL_LOI", ManagerCategoryLevel.SECONDARY),

                // Trung tâm Thông tin - Thư viện.
                assignment(manager6, "TV_MUONTRA", ManagerCategoryLevel.PRIMARY),
                assignment(manager6, "TV_TRACUU", ManagerCategoryLevel.SECONDARY),
                assignment(manager6, "TV_TAIKHOAN", ManagerCategoryLevel.SECONDARY),
                assignment(manager6, "TV_MUONTRA_GIAHAN", ManagerCategoryLevel.SECONDARY),
                assignment(manager6, "TV_TRACUU_TAILIEU", ManagerCategoryLevel.SECONDARY),

                // Phòng Quan hệ Doanh nghiệp.
                assignment(manager7, "QHDN_THUCTAP", ManagerCategoryLevel.PRIMARY),
                assignment(manager7, "QHDN_TUYENDUNG", ManagerCategoryLevel.SECONDARY),
                assignment(manager7, "QHDN_CV", ManagerCategoryLevel.SECONDARY),
                assignment(manager7, "QHDN_THUCTAP_HOSO", ManagerCategoryLevel.SECONDARY),
                assignment(manager7, "QHDN_TUYENDUNG_HOSO", ManagerCategoryLevel.SECONDARY),

                // Phòng Khoa học Công nghệ.
                assignment(manager8, "KHCN_DETAI", ManagerCategoryLevel.PRIMARY),
                assignment(manager8, "KHCN_HOITHAO", ManagerCategoryLevel.SECONDARY),
                assignment(manager8, "KHCN_NGHIENCUU", ManagerCategoryLevel.SECONDARY),
                assignment(manager8, "KHCN_DETAI_HOSO", ManagerCategoryLevel.SECONDARY),
                assignment(manager8, "KHCN_HOITHAO_DANGKY", ManagerCategoryLevel.SECONDARY),

                // Trạm Y tế.
                assignment(manager9, "YTE_BHYT", ManagerCategoryLevel.PRIMARY),
                assignment(manager9, "YTE_SUCKHOE", ManagerCategoryLevel.SECONDARY),
                assignment(manager9, "YTE_HOTRO", ManagerCategoryLevel.SECONDARY),
                assignment(manager9, "YTE_BHYT_HOSO", ManagerCategoryLevel.SECONDARY),
                assignment(manager9, "YTE_SUCKHOE_TUVAN", ManagerCategoryLevel.SECONDARY),

                // Quản trị Cơ sở vật chất.
                assignment(manager10, "CSVC_SUACHUA", ManagerCategoryLevel.PRIMARY),
                assignment(manager10, "CSVC_PHONGHOC", ManagerCategoryLevel.SECONDARY),
                assignment(manager10, "CSVC_THIETBI", ManagerCategoryLevel.SECONDARY),
                assignment(manager10, "CSVC_SUACHUA_BAOHONG", ManagerCategoryLevel.SECONDARY),
                assignment(manager10, "CSVC_PHONGHOC_DANGKY", ManagerCategoryLevel.SECONDARY),

                // Khảo thí và Đảm bảo chất lượng.
                assignment(manager11, "QLCL_LICHTHI", ManagerCategoryLevel.PRIMARY),
                assignment(manager11, "QLCL_PHUCKHAO", ManagerCategoryLevel.SECONDARY),
                assignment(manager11, "QLCL_KHAOSAT", ManagerCategoryLevel.SECONDARY),
                assignment(manager11, "QLCL_LICHTHI_TRUNGLICH", ManagerCategoryLevel.SECONDARY),
                assignment(manager11, "QLCL_PHUCKHAO_HOSO", ManagerCategoryLevel.SECONDARY),

                // Viện Sau đại học.
                assignment(manager12, "SDH_TUYENSINH", ManagerCategoryLevel.PRIMARY),
                assignment(manager12, "SDH_HOSO", ManagerCategoryLevel.SECONDARY),
                assignment(manager12, "SDH_HOCVU", ManagerCategoryLevel.SECONDARY),
                assignment(manager12, "SDH_TUYENSINH_HOSO", ManagerCategoryLevel.SECONDARY),
                assignment(manager12, "SDH_HOSO_BOSUNG", ManagerCategoryLevel.SECONDARY)
        ));
    }

    private ManagerProfile profile(
            User manager,
            String employeeCode,
            Department department,
            AvailabilityStatus availabilityStatus) {
        return ManagerProfile.builder()
                .userId(manager.getId())
                .employeeCode(employeeCode)
                .jobTitle("Chuyên viên tư vấn")
                .departmentId(department.getId())
                .availabilityStatus(availabilityStatus)
                .maxConcurrentTickets(10)
                .maxConcurrentChats(3)
                .build();
    }

    private ManagerCategory assignment(
            User manager,
            String categoryCode,
            ManagerCategoryLevel level) {
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new IllegalStateException(
                        "Không tìm thấy chuyên mục demo " + categoryCode));

        return ManagerCategory.builder()
                .managerId(manager.getId())
                .categoryId(category.getId())
                .level(level)
                .build();
    }

    private User manager(int number) {
        if (number < 1 || number > MANAGER_EMAILS.length) {
            throw new IllegalArgumentException(
                    "Số thứ tự manager không hợp lệ: " + number);
        }

        String email = MANAGER_EMAILS[number - 1];

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException(
                        "Không tìm thấy tài khoản demo " + email));
    }
}