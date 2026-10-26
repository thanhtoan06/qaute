package vn.edu.hcmute.qaute.config.seed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.util.SlugUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;
import vn.edu.hcmute.qaute.entity.knowledge.FaqTag;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqTagRepository;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.service.knowledge.FaqContentSupport;

/**
 * Seed FAQ cho hệ thống QAUTE mới.
 * Cần chạy sau UserSeeder và OrgSeeder (order 10 và 20).
 * Không sử dụng các tài khoản manager1..manager4 của cấu hình demo cũ.
 */
@Component
public class FaqSeeder implements DemoSeeder {

    private final FaqRepository faqRepository;
    private final FaqTagRepository faqTagRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private static final String ADMIN_EMAIL = "admin@qaute.edu.vn";

    private static final String[][] MANAGER_ACCOUNTS = {
        {"DT", "manager.daotao@qaute.edu.vn"},
        {"CT", "manager.ctsv@qaute.edu.vn"},
        {"TC", "manager.taichinh@qaute.edu.vn"},
        {"KTX", "manager.ktx@qaute.edu.vn"},
        {"CNTT", "manager.cntt@qaute.edu.vn"},
        {"TV", "manager.thuvien@qaute.edu.vn"},
        {"QHDN", "manager.qhdn@qaute.edu.vn"},
        {"KHCN", "manager.khcn@qaute.edu.vn"},
        {"YTE", "manager.yte@qaute.edu.vn"},
        {"CSVC", "manager.csvc@qaute.edu.vn"},
        {"QLCL", "manager.qlcl@qaute.edu.vn"},
        {"SDH", "manager.saudh@qaute.edu.vn"}
    };

    /** Toàn bộ 71 category code do OrgSeeder khởi tạo. */
    private static final String[] CATEGORY_CODES = {
        "DT_DKHP", "DT_HOCVU", "DT_TOTNGHIEP",
        "CT_HOCBONG", "CT_RENLUYEN",
        "TC_HOCPHI", "TC_MIENGIAM",
        "KTX_DANGKY", "KTX_DICHVU",
        "DT_DKHP_SAI", "DT_DKHP_HUY", "DT_HOCVU_DIEM", "DT_HOCVU_BAOLUU", "DT_TOTNGHIEP_HOSO",
        "CT_XACNHAN", "CT_HOTRO", "CT_HOCBONG_HOSO", "CT_HOCBONG_KETQUA", "CT_RENLUYEN_DIEM",
        "CT_RENLUYEN_MINHCHUNG", "CT_XACNHAN_GIAYTO",
        "TC_CHUNGTU", "TC_THANHTOAN", "TC_HOCPHI_DOISOAT", "TC_HOCPHI_GIAHAN", "TC_MIENGIAM_HOSO", "TC_THANHTOAN_LOI",
        "KTX_DANGKY_HOSO", "KTX_DANGKY_GIAHAN", "KTX_DICHVU_SUA", "KTX_DICHVU_DIENNUOC",
        "CNTT_TAIKHOAN", "CNTT_PORTAL", "CNTT_BAOMAT", "CNTT_TAIKHOAN_KHOIPHUC", "CNTT_PORTAL_LOI",
        "TV_MUONTRA", "TV_TRACUU", "TV_TAIKHOAN", "TV_MUONTRA_GIAHAN", "TV_TRACUU_TAILIEU",
        "QHDN_THUCTAP", "QHDN_TUYENDUNG", "QHDN_CV", "QHDN_THUCTAP_HOSO", "QHDN_TUYENDUNG_HOSO",
        "KHCN_DETAI", "KHCN_HOITHAO", "KHCN_NGHIENCUU", "KHCN_DETAI_HOSO", "KHCN_HOITHAO_DANGKY",
        "YTE_BHYT", "YTE_SUCKHOE", "YTE_HOTRO", "YTE_BHYT_HOSO", "YTE_SUCKHOE_TUVAN",
        "CSVC_SUACHUA", "CSVC_PHONGHOC", "CSVC_THIETBI", "CSVC_SUACHUA_BAOHONG", "CSVC_PHONGHOC_DANGKY",
        "QLCL_LICHTHI", "QLCL_PHUCKHAO", "QLCL_KHAOSAT", "QLCL_LICHTHI_TRUNGLICH", "QLCL_PHUCKHAO_HOSO",
        "SDH_TUYENSINH", "SDH_HOSO", "SDH_HOCVU", "SDH_TUYENSINH_HOSO", "SDH_HOSO_BOSUNG"
    };

    // Khởi tạo seeder FAQ với các kho dữ liệu cần thiết.
    public FaqSeeder(FaqRepository faqRepository,
                     FaqTagRepository faqTagRepository,
                     CategoryRepository categoryRepository,
                     UserRepository userRepository) {
        this.faqRepository = faqRepository;
        this.faqTagRepository = faqTagRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // Xác định thứ tự chạy của seeder FAQ.
    @Override
    public int order() {
        return 30;
    }

    // Nạp dữ liệu FAQ demo cùng thẻ và chuyên mục liên quan.
    @Override
    @Transactional
    public void seed() {
        if (faqRepository.count() > 0) {
            return;
        }

        User admin = userRepository.findByEmailIgnoreCase(ADMIN_EMAIL)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy tài khoản admin mới: " + ADMIN_EMAIL));

        Map<String, User> managerByDepartment = new HashMap<>();
        for (String[] account : MANAGER_ACCOUNTS) {
            User manager = userRepository.findByEmailIgnoreCase(account[1])
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy tài khoản quản lý mới: " + account[1]));
            managerByDepartment.put(account[0], manager);
        }

        Map<String, Category> categoryMap = new HashMap<>();
        for (String code : CATEGORY_CODES) {
            Category category = categoryRepository.findByCode(code)
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy chuyên mục do OrgSeeder tạo: " + code));
            categoryMap.put(code, category);
        }

        String[] tagNames = {
            "Học phần", "Đăng ký môn", "Hủy học phần", "Thời khóa biểu", "Học vụ", "Điểm thi", "Bảng điểm",
            "Bảo lưu", "Xét tốt nghiệp", "Hồ sơ tốt nghiệp", "Chứng chỉ", "Chuẩn đầu ra",
            "Học bổng", "Khuyến khích học tập", "Điểm rèn luyện", "Hoạt động phong trào", "Minh chứng rèn luyện",
            "Học phí", "Hạn nộp học phí", "Thanh toán online", "Đối soát học phí", "Miễn giảm học phí", "Chính sách hỗ trợ",
            "Ký túc xá", "Đăng ký nội trú", "Gia hạn nội trú", "Dịch vụ sinh viên", "Sửa chữa trang thiết bị", "Điện nước",
            "Cổng thông tin", "Tài khoản", "Bảo mật", "Khôi phục tài khoản", "Lỗi hệ thống", "Sinh viên HCMUTE",
            "Thư viện", "Mượn trả", "Tra cứu tài liệu", "Gia hạn tài liệu", "Thực tập", "Tuyển dụng", "Hồ sơ ứng tuyển",
            "Nghiên cứu khoa học", "Đề tài", "Hội thảo", "Bảo hiểm y tế", "Sức khỏe", "Tư vấn y tế", "Cơ sở vật chất",
            "Phòng học", "Thiết bị", "Lịch thi", "Phúc khảo", "Khảo sát chất lượng", "Sau đại học", "Tuyển sinh",
            "Hướng dẫn học vụ", "Cổng thông tin sinh viên", "Quy trình xử lý", "Hồ sơ", "Thời hạn", "Lỗi thường gặp"
        };

        List<FaqTag> allTags = new ArrayList<>();
        for (String tagName : tagNames) {
            allTags.add(getOrCreateTag(tagName));
        }

        Random random = new Random(42);
        LocalDateTime now = LocalDateTime.now();
        List<FaqDefinition> definitions = new ArrayList<>();
        definitions.addAll(createFaqDefinitions());

        Set<String> knownTagSlugs = new HashSet<>();
        for (FaqTag tag : allTags) {
            knownTagSlugs.add(tag.getSlug());
        }
        for (FaqDefinition definition : definitions) {
            String[] preferredTags = definition.preferredTags();
            if (preferredTags == null) {
                continue;
            }
            for (String tagName : preferredTags) {
                String tagSlug = SlugUtil.slugify(tagName);
                if (knownTagSlugs.add(tagSlug)) {
                    allTags.add(getOrCreateTag(tagName));
                }
            }
        }

        for (FaqDefinition def : definitions) {
            Category category = categoryMap.get(def.categoryCode());
            if (category == null) {
                throw new IllegalStateException("FAQ đang trỏ tới chuyên mục chưa được khai báo: " + def.categoryCode());
            }
            User author = getManagerForCategory(def.categoryCode(), managerByDepartment);

            // Set viewCount cao cho 10 bài FAQ phổ biến nhất
            int viewCount;
            String title = def.title();
            if (title.contains("đăng ký học phần") || title.contains("số tín chỉ") ||
                title.contains("phúc khảo") || title.contains("cảnh báo học vụ") ||
                title.contains("chuẩn ngoại ngữ") || title.contains("học bổng khuyến khích") ||
                title.contains("điểm rèn luyện") || title.contains("thanh toán học phí") ||
                title.contains("đăng ký ở ký túc xá") || title.contains("điểm W")) {
                viewCount = 5000 + random.nextInt(5000); // 5000-10000 views
            } else {
                viewCount = random.nextInt(401); // 0-400 views cho các bài còn lại
            }

            int helpfulCount = random.nextInt(41);
            int notHelpfulCount = random.nextInt(9);
            LocalDateTime publishTime = now.minusDays(random.nextInt(60) + 1).minusHours(random.nextInt(24));
            LocalDateTime pubAt = def.status() == FaqStatus.PUBLISHED || def.status() == FaqStatus.ARCHIVED
                    ? publishTime : null;

            String baseSlug = SlugUtil.slugify(def.title());
            String slug = baseSlug;
            int counter = 1;
            while (faqRepository.existsBySlug(slug)) {
                slug = baseSlug + "-" + counter++;
            }

            Set<FaqTag> tags = pickTags(allTags, def.preferredTags());
            Faq faq = Faq.builder()
                    .categoryId(category.getId())
                    .authorId(author.getId())
                    .reviewerId(def.status() == FaqStatus.PUBLISHED || def.status() == FaqStatus.ARCHIVED
                            ? admin.getId() : null)
                    .title(def.title())
                    .slug(slug)
                    .summary(def.summary())
                    .bodyHtml(def.bodyHtml())
                    .status(def.status())
                    .featured(def.featured())
                    .publishAt(pubAt)
                    .publishedAt(pubAt)
                    .viewCount(viewCount)
                    .helpfulCount(helpfulCount)
                    .notHelpfulCount(notHelpfulCount)
                    .versionNo(1)
                    .tags(tags)
                    .build();

            FaqContentSupport.prepare(faq);
            faqRepository.save(faq);
        }
    }

    // Chọn quản lý phụ trách chuyên mục FAQ.
    private User getManagerForCategory(String code, Map<String, User> managerByDepartment) {
        for (String[] account : MANAGER_ACCOUNTS) {
            String prefix = account[0] + "_";
            if (code.startsWith(prefix)) {
                User manager = managerByDepartment.get(account[0]);
                if (manager != null) {
                    return manager;
                }
            }
        }
        throw new IllegalStateException("Không xác định được quản lý phụ trách chuyên mục: " + code);
    }

    // Tìm hoặc tạo thẻ FAQ theo tên.
    private FaqTag getOrCreateTag(String name) {
        String slug = SlugUtil.slugify(name);
        return faqTagRepository.findBySlug(slug).orElseGet(() -> {
            FaqTag tag = FaqTag.builder()
                    .name(name)
                    .nameNorm(TextUtil.normalizeVi(name))
                    .slug(slug)
                    .build();
            return faqTagRepository.save(tag);
        });
    }

    // Chọn các thẻ phù hợp cho một FAQ từ danh sách định nghĩa.
    private Set<FaqTag> pickTags(List<FaqTag> allTags, String[] preferredNames) {
        Set<FaqTag> result = new HashSet<>();
        if (preferredNames != null) {
            for (String preferredName : preferredNames) {
                String preferredSlug = SlugUtil.slugify(preferredName);
                for (FaqTag tag : allTags) {
                    if (tag.getSlug().equals(preferredSlug)) {
                        result.add(tag);
                    }
                }
            }
        }
        return result;
    }

    private record FaqDefinition(
            String categoryCode,
            String title,
            String summary,
            String bodyHtml,
            FaqStatus status,
            boolean featured,
            String[] preferredTags
    ) {}

    /** Giữ 32 FAQ gốc và bổ sung 248 FAQ riêng biệt trong cùng một danh sách. */
    // Tạo danh sách định nghĩa FAQ dùng cho dữ liệu demo.
    private List<FaqDefinition> createFaqDefinitions() {
        List<FaqDefinition> list = new ArrayList<>();

        // 1. DT_DKHP
        list.add(new FaqDefinition(
                "DT_DKHP",
                "Cách đăng ký học phần trên cổng thông tin sinh viên?",
                "Các bước đăng ký môn học, điều chỉnh học phần và xem thời khóa biểu trên hệ thống cổng thông tin sinh viên HCMUTE.",
                """
                <p>Để đăng ký học phần trực tuyến, bạn có thể làm theo các bước sau:</p>
                <ol>
                    <li>Đăng nhập cổng thông tin đào tạo với tài khoản sinh viên được cấp.</li>
                    <li>Mở mục<strong>Đăng ký học phần</strong>trong thời gian mở cổng quy định của từng khóa.</li>
                    <li>Chọn học phần theo kế hoạch đào tạo hoặc tra cứu theo mã môn học.</li>
                    <li>Chọn nhóm lớp học phần phù hợp và nhấn nút<strong>Lưu đăng ký</strong>.</li>
                    <li>Kiểm tra lại thời khóa biểu cá nhân tại mục<em>Lịch học học kỳ</em>để tránh trùng lịch.</li>
                </ol>
                <p>Lưu ý: Bạn cần kiểm tra kỹ điều kiện tiên quyết trước khi đăng ký để tránh bị hủy môn học tự động.</p>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Đăng ký môn", "Học phần", "Thời khóa biểu", "Cổng thông tin", "Sinh viên HCMUTE"}
        ));

        list.add(new FaqDefinition(
                "DT_DKHP",
                "Làm gì khi bị trùng lịch học hoặc lớp học phần đã đầy sĩ số?",
                "Cách xử lý khi lớp học phần kín chỗ hoặc trùng giờ với các môn học bắt buộc.",
                """
                <p>Khi gặp sự cố lớp học phần hết chỗ hoặc trùng lịch:</p>
                <ul>
                    <li><strong>Tìm lớp thay thế:</strong>Tìm các nhóm lớp học phần khác của cùng môn học còn sĩ số trống.</li>
                    <li><strong>Đăng ký môn học tương đương:</strong>Xem danh mục môn học tương đương hoặc thay thế trong chương trình đào tạo.</li>
                    <li><strong>Làm đơn xin mở thêm lớp:</strong>Đại diện ban cán sự lớp hoặc nhóm sinh viên gửi đơn trực tuyến tới Phòng Đào tạo đề nghị mở thêm nhóm học phần mới.</li>
                </ul>
                <p>Mọi thắc mắc cần hỗ trợ khẩn cấp trong đợt đăng ký, bạn hãy gửi yêu cầu tư vấn trên QAUTE.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đăng ký môn", "Học phần", "Thời khóa biểu", "Hướng dẫn học vụ"}
        ));

        list.add(new FaqDefinition(
                "DT_DKHP",
                "Quy định về số tín chỉ tối thiểu và tối đa được đăng ký trong một học kỳ như thế nào?",
                "Quy định đào tạo theo tín chỉ nêu mức tải học tập tối thiểu và tối đa cho sinh viên học lực bình thường và cảnh báo học vụ.",
                """
                <p>Theo quy chế đào tạo đại học theo học chế tín chỉ của nhà trường:</p>
                <ul>
                    <li>Sinh viên xếp loại học lực bình thường được đăng ký tối thiểu<strong>14 tín chỉ</strong>và tối đa<strong>24 tín chỉ</strong>trong một học kỳ chính.</li>
                    <li>Sinh viên đang bị cảnh báo học vụ chỉ có thể đăng ký tối đa<strong>14 tín chỉ</strong>để tập trung cải thiện kết quả học tập.</li>
                    <li>Trong học kỳ hè, số tín chỉ tối đa có thể đăng ký là<strong>8 tín chỉ</strong>.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phần", "Đăng ký môn", "Học vụ"}
        ));

        list.add(new FaqDefinition(
                "DT_DKHP",
                "Làm thế nào để đề xuất mở thêm lớp học phần hoặc tăng sĩ số?",
                "Cách đề xuất mở thêm lớp học phần ngoài kế hoạch khi có đủ số lượng sinh viên có nguyện vọng.",
                """
                <p>Nếu cần đề xuất mở thêm nhóm học phần:</p>
                <p>Bạn cần tập hợp danh sách tối thiểu 20 sinh viên cùng có nguyện vọng và gửi đơn trực tuyến qua cổng QAUTE cho chuyên viên phụ trách khoa.</p>
                """,
                FaqStatus.DRAFT, false,
                new String[]{"Học phần", "Đăng ký môn"}
        ));

        // 2. DT_HOCVU
        list.add(new FaqDefinition(
                "DT_HOCVU",
                "Cách xin phúc khảo bài thi kết thúc học phần và thời hạn nộp đơn là khi nào?",
                "Cách nộp đơn phúc khảo, thời hạn tiếp nhận và thời gian nhận kết quả.",
                """
                <p>Khi nghi ngờ có sai sót về điểm thi kết thúc học phần, sinh viên có quyền làm đơn phúc khảo:</p>
                <ol>
                    <li><strong>Thời hạn:</strong>Trong vòng 07 ngày làm việc kể từ ngày công bố điểm chính thức trên cổng thông tin.</li>
                    <li><strong>Hình thức:</strong>Nộp đơn phúc khảo trực tuyến trên cổng sinh viên hoặc tại Văn phòng Đào tạo.</li>
                    <li><strong>Lệ phí:</strong>Nộp lệ phí theo quy định (sẽ được hoàn trả nếu kết quả phúc khảo làm tăng điểm).</li>
                    <li><strong>Kết quả:</strong>Được hội đồng chấm lại và công bố trong thời hạn 15 ngày làm việc.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Điểm thi", "Học vụ", "Bảng điểm", "Hướng dẫn học vụ"}
        ));

        list.add(new FaqDefinition(
                "DT_HOCVU",
                "Cần đáp ứng điều kiện gì và làm thủ tục thế nào để xin tạm hoãn thi hoặc thi bổ sung?",
                "Hồ sơ minh chứng y tế, hoàn cảnh gia đình hoặc sự cố bất khả kháng để được bảo lưu và thi bổ sung vào đợt kế tiếp.",
                """
                <p>Sinh viên vì lý do ốm đau, tai nạn hoặc sự kiện bất khả kháng không thể dự thi kết thúc học phần:</p>
                <ul>
                    <li>Cần làm đơn xin hoãn thi kèm theo giấy xác nhận của bệnh viện tuyến quận/huyện trở lên.</li>
                    <li>Gửi đơn đến Văn phòng Đào tạo chậm nhất 03 ngày làm việc sau ngày diễn ra buổi thi.</li>
                    <li>Điểm hoãn thi được ký hiệu chữ 'I' và sinh viên được sắp xếp thi bổ sung vào đợt thi gần nhất.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điểm thi", "Học vụ", "Hướng dẫn học vụ"}
        ));

        list.add(new FaqDefinition(
                "DT_HOCVU",
                "Khi nào sinh viên bị cảnh báo học vụ hoặc buộc thôi học theo hệ thống tín chỉ?",
                "Các mức cảnh báo học vụ được xác định dựa trên điểm trung bình tích lũy và số tín chỉ nợ đọng, các biện pháp hỗ trợ sinh viên.",
                """
                <p>Sinh viên sẽ bị cảnh báo kết quả học tập nếu thuộc một trong các trường hợp sau:</p>
                <ul>
                    <li>Điểm trung bình chung học kỳ đạt dưới 1.00 đối với học kỳ đầu tiên, dưới 1.10 đối với các học kỳ tiếp theo.</li>
                    <li>Điểm trung bình chung tích lũy đạt dưới 1.20 đối với sinh viên năm thứ nhất, dưới 1.40 đối với sinh viên năm thứ hai, dưới 1.60 đối với sinh viên năm thứ ba.</li>
                    <li>Sinh viên bị cảnh báo học vụ 3 lần liên tiếp sẽ bị xem xét buộc thôi học theo quy chế đào tạo.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học vụ", "Bảng điểm", "Điểm thi"}
        ));

        list.add(new FaqDefinition(
                "DT_HOCVU",
                "Cách tra cứu bảng điểm quá trình và bảng điểm tích lũy học tập?",
                "Cách xem điểm thi thành phần, điểm thi kết thúc học phần và điểm trung bình tích lũy thang điểm 4 và thang 10.",
                """
                <p>Bạn đăng nhập cổng thông tin sinh viên để tra cứu điểm thi:</p>
                <p>Vào mục Tra cứu học vụ → Bảng điểm cá nhân để xem điểm chi tiết các môn đã học.</p>
                """,
                FaqStatus.PENDING_REVIEW, false,
                new String[]{"Bảng điểm", "Điểm thi", "Học vụ"}
        ));

        // 3. DT_TOTNGHIEP
        list.add(new FaqDefinition(
                "DT_TOTNGHIEP",
                "Cần đạt chuẩn ngoại ngữ và tin học nào để xét tốt nghiệp?",
                "Các chứng chỉ tiếng Anh quốc tế, chuẩn đầu ra tin học MOS/IC3 tương đương được trường công nhận.",
                """
                <p>Để đủ điều kiện xét công nhận tốt nghiệp ra trường, bạn cần hoàn thành các chuẩn đầu ra sau:</p>
                <ul>
                    <li><strong>Ngoại ngữ:</strong>Đạt chứng chỉ TOEIC tối thiểu 500 điểm (hoặc IELTS 5.0, TOEFL iBT 45, VSTEP B1/B2 tùy theo ngành đào tạo).</li>
                    <li><strong>Tin học:</strong>Đạt chứng chỉ tin học quốc tế MOS (Word, Excel, PowerPoint) hoặc IC3/chứng chỉ ứng dụng CNTT nâng cao.</li>
                    <li><strong>Giáo dục quốc phòng &amp; Thể chất:</strong>Đã hoàn tất và được cấp chứng chỉ theo quy định của Bộ GD&amp;ĐT.</li>
                </ul>
                <p>Chứng chỉ phải nộp cho Phòng Đào tạo xác thực trước thời hạn xét tốt nghiệp tối thiểu 30 ngày.</p>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Chuẩn đầu ra", "Chứng chỉ", "Xét tốt nghiệp", "Học vụ"}
        ));

        list.add(new FaqDefinition(
                "DT_TOTNGHIEP",
                "Cần chuẩn bị hồ sơ gì và nộp đơn xét tốt nghiệp vào thời gian nào?",
                "Các đợt xét tốt nghiệp trong năm, giấy tờ cần chuẩn bị và thời gian nộp đơn.",
                """
                <p>Hàng năm trường tổ chức 4 đợt xét tốt nghiệp chính:</p>
                <ol>
                    <li><strong>Đợt 1 (Tháng 3):</strong>Nhận đơn từ 15/01 đến 15/02.</li>
                    <li><strong>Đợt 2 (Tháng 6 - Đợt chính):</strong>Nhận đơn từ 15/04 đến 15/05.</li>
                    <li><strong>Đợt 3 (Tháng 9):</strong>Nhận đơn từ 15/07 đến 15/08.</li>
                    <li><strong>Đợt 4 (Tháng 12):</strong>Nhận đơn từ 15/10 đến 15/11.</li>
                </ol>
                <p>Bạn kiểm tra danh sách đủ điều kiện trên cổng thông tin sinh viên và nộp đơn xét tốt nghiệp trực tuyến.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Xét tốt nghiệp", "Học vụ", "Chứng chỉ"}
        ));

        list.add(new FaqDefinition(
                "DT_TOTNGHIEP",
                "Khi nào nhận được bằng tốt nghiệp tạm thời và bằng chính thức?",
                "Thời gian nhận giấy chứng nhận tốt nghiệp tạm thời, thủ tục mượn lễ phục và nhận bằng gốc tại lễ tốt nghiệp.",
                """
                <p>Sau khi có quyết định công nhận tốt nghiệp của Hiệu trưởng:</p>
                <ul>
                    <li>Sinh viên được cấp<strong>Giấy chứng nhận tốt nghiệp tạm thời</strong>trong khoảng 07 ngày làm việc để sử dụng xin việc.</li>
                    <li>Bằng tốt nghiệp chính thức và bảng điểm toàn khóa được trao tại Lễ tốt nghiệp hoặc nhận tại Phòng Đào tạo sau lễ.</li>
                    <li>Khi đến nhận bằng, bạn cần mang theo CCCD bản gốc và thẻ sinh viên (hoặc giấy báo mất thẻ).</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Xét tốt nghiệp", "Chứng chỉ", "Bảng điểm"}
        ));

        list.add(new FaqDefinition(
                "DT_TOTNGHIEP",
                "Làm thế nào để xin cấp lại bản sao bằng tốt nghiệp khi bị mất hoặc hư hỏng?",
                "Cách xin cấp bản sao từ sổ gốc văn bằng tốt nghiệp theo quy định của Bộ Giáo dục và Đào tạo.",
                """
                <p>Bằng tốt nghiệp bản chính chỉ được cấp 01 lần duy nhất. Nếu bị mất hoặc hư hỏng:</p>
                <p>Sinh viên làm thủ tục xin cấp bản sao trích lục từ sổ gốc cấp bằng tại Phòng Đào tạo.</p>
                """,
                FaqStatus.ARCHIVED, false,
                new String[]{"Xét tốt nghiệp", "Chứng chỉ"}
        ));

        // 4. CT_HOCBONG
        list.add(new FaqDefinition(
                "CT_HOCBONG",
                "Điều kiện xét học bổng khuyến khích học tập mỗi học kỳ là gì?",
                "Điểm học tập và điểm rèn luyện cần đạt để được xét học bổng loại Khá, Giỏi, Xuất sắc cho sinh viên chính quy.",
                """
                <p>Học bổng khuyến khích học tập (KKHT) được xét theo từng học kỳ dựa trên 2 tiêu chí:</p>
                <ul>
                    <li><strong>Học bổng Xuất sắc:</strong>Điểm học tập từ 3.60 trở lên và điểm rèn luyện từ 90 điểm trở lên. Mức học bổng: 120% học phí kỳ.</li>
                    <li><strong>Học bổng Giỏi:</strong>Điểm học tập từ 3.20 đến 3.59 và điểm rèn luyện từ 80 điểm trở lên. Mức học bổng: 110% học phí kỳ.</li>
                    <li><strong>Học bổng Khá:</strong>Điểm học tập từ 2.50 đến 3.19 và điểm rèn luyện từ 70 điểm trở lên. Mức học bổng: 100% học phí kỳ.</li>
                </ul>
                <p>Điều kiện bắt buộc: Trong học kỳ xét học bổng, bạn cần đăng ký đủ số tín chỉ tối thiểu và không có môn học nào bị điểm F ở lần thi đầu.</p>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Học bổng", "Khuyến khích học tập", "Điểm rèn luyện", "Chính sách hỗ trợ"}
        ));

        list.add(new FaqDefinition(
                "CT_HOCBONG",
                "Cần chuẩn bị hồ sơ gì để xin học bổng tài trợ từ doanh nghiệp và cựu sinh viên?",
                "Cách nộp đơn xét tuyển các chương trình học bổng đồng hành, học bổng vượt khó và cơ hội thực tập đi kèm.",
                """
                <p>Hàng năm Phòng Công tác sinh viên thông báo các suất học bổng từ các tập đoàn và cựu sinh viên:</p>
                <ol>
                    <li>Theo dõi thông báo học bổng tài trợ trên trang thông tin chính thức và cổng thông tin sinh viên.</li>
                    <li>Chuẩn bị hồ sơ gồm: Đơn xin học bổng (theo mẫu), bảng điểm tích lũy, thư giới thiệu, giấy xác nhận hoàn cảnh khó khăn (nếu có).</li>
                    <li>Nộp hồ sơ trực tuyến qua hệ thống hoặc trực tiếp tại Phòng CTSV trước hạn chót.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Chính sách hỗ trợ", "Sinh viên HCMUTE"}
        ));

        list.add(new FaqDefinition(
                "CT_HOCBONG",
                "Học bổng được chuyển vào tài khoản ngân hàng khi nào?",
                "Các bước chuyển tiền học bổng qua thẻ ATM liên kết, xử lý khi thông tin số tài khoản ngân hàng bị sai.",
                """
                <p>Tiền học bổng khuyến khích học tập và học bổng tài trợ sẽ được chuyển trực tiếp vào tài khoản ngân hàng của sinh viên đã đăng ký trên cổng thông tin sinh viên:</p>
                <ul>
                    <li>Thời gian chuyển tiền: Trong vòng 15-20 ngày làm việc sau khi có quyết định khen thưởng chính thức.</li>
                    <li>Bạn cần cập nhật chính xác số tài khoản ngân hàng chính chủ tại mục Hồ sơ cá nhân.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Khuyến khích học tập", "Cổng thông tin"}
        ));

        list.add(new FaqDefinition(
                "CT_HOCBONG",
                "Sinh viên thuộc diện nào được nhận trợ cấp xã hội hoặc ưu đãi giáo dục?",
                "Điều kiện hưởng trợ cấp hàng tháng cho sinh viên mồ côi, người dân tộc thiểu số và sinh viên khuyết tật.",
                """
                <p>Sinh viên thuộc diện chính sách được hưởng trợ cấp xã hội theo quy định đang áp dụng:</p>
                <p>Hồ sơ bao gồm đơn đề nghị, bản sao công chứng sổ hộ nghèo/cận nghèo hoặc giấy xác nhận khuyết tật nộp định kỳ đầu mỗi học kỳ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chính sách hỗ trợ", "Học bổng", "Sinh viên HCMUTE"}
        ));

        // 5. CT_RENLUYEN
        list.add(new FaqDefinition(
                "CT_RENLUYEN",
                "Cách tự đánh giá điểm rèn luyện trực tuyến vào cuối học kỳ?",
                "Cách tự chấm điểm trên hệ thống, các thời hạn họp lớp bình xét và duyệt điểm của Khoa.",
                """
                <p>Quy trình đánh giá kết quả rèn luyện của sinh viên gồm 3 bước:</p>
                <ol>
                    <li><strong>Sinh viên tự đánh giá:</strong>Đăng nhập cổng thông tin sinh viên, vào mục Điểm rèn luyện để tự chấm điểm theo các tiêu chí và đính kèm minh chứng.</li>
                    <li><strong>Tập thể lớp họp xét:</strong>Ban cán sự lớp chủ trì họp bình xét, ghi biên bản và thông qua kết quả chấm của từng thành viên.</li>
                    <li><strong>Khoa và Trường phê duyệt:</strong>Ban chủ nhiệm khoa kiểm tra, duyệt và công bố bảng điểm rèn luyện chính thức.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Điểm rèn luyện", "Hoạt động phong trào", "Cổng thông tin"}
        ));

        list.add(new FaqDefinition(
                "CT_RENLUYEN",
                "Hoạt động phong trào và tình nguyện được tính vào điểm rèn luyện như thế nào?",
                "Mức điểm cộng cho các hoạt động hiến máu nhân đạo, Mùa hè xanh, Tiếp sức mùa thi và nghiên cứu khoa học.",
                """
                <p>Các hoạt động phong trào được cộng điểm rèn luyện theo quy chế:</p>
                <ul>
                    <li>Hiến máu tình nguyện: Cộng 05 điểm/lần (tiêu chí tham gia hoạt động xã hội).</li>
                    <li>Chiến dịch Mùa hè xanh, Tiếp sức mùa thi: Cộng 08 - 10 điểm (hoàn thành xuất sắc nhiệm vụ).</li>
                    <li>Tham gia nghiên cứu khoa học sinh viên: Cộng 05 - 10 điểm tùy theo cấp đề tài và giải thưởng đạt được.</li>
                    <li>Tham gia các buổi hội thảo chuyên đề, tọa đàm kỹ năng: Cộng 02 - 03 điểm/buổi.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điểm rèn luyện", "Hoạt động phong trào"}
        ));

        list.add(new FaqDefinition(
                "CT_RENLUYEN",
                "Khi nào có thể khiếu nại hoặc đề nghị điều chỉnh điểm rèn luyện?",
                "Cách đề nghị kiểm tra lại điểm rèn luyện khi sinh viên bị sót hoạt động hoặc tính nhầm tiêu chí chấm điểm.",
                """
                <p>Sau khi có bảng điểm rèn luyện tạm thời được công bố:</p>
                <p>Sinh viên có 07 ngày làm việc để gửi phản hồi khiếu nại kèm minh chứng cho cố vấn học tập hoặc Phòng CTSV.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điểm rèn luyện", "Hoạt động phong trào", "Hướng dẫn học vụ"}
        ));

        list.add(new FaqDefinition(
                "CT_RENLUYEN",
                "Cần đạt điểm rèn luyện bao nhiêu để được khen thưởng hoặc tốt nghiệp loại Giỏi?",
                "Điều kiện về điểm rèn luyện khi xét danh hiệu sinh viên giỏi, xuất sắc và tốt nghiệp thủ khoa.",
                """
                <p>Tiêu chuẩn điểm rèn luyện khi xét tốt nghiệp:</p>
                <p>Sinh viên muốn được công nhận tốt nghiệp loại Giỏi trở lên phải có điểm rèn luyện toàn khóa đạt từ loại Khá trở lên.</p>
                """,
                FaqStatus.DRAFT, false,
                new String[]{"Điểm rèn luyện", "Xét tốt nghiệp"}
        ));

        // 6. TC_HOCPHI
        list.add(new FaqDefinition(
                "TC_HOCPHI",
                "Có thể thanh toán học phí bằng những cách nào?",
                "Các cách chuyển khoản qua ngân hàng trực tuyến, quét mã VietQR và thanh toán qua cổng VNPAY hoặc MoMo.",
                """
                <p>Nhà trường hỗ trợ nhiều kênh nộp học phí tiện lợi và an toàn:</p>
                <ol>
                    <li><strong>Quét mã VietQR trên cổng thông tin sinh viên:</strong>Đăng nhập cổng thông tin sinh viên, vào mục Học phí → Thanh toán trực tuyến → Quét mã QR bằng ứng dụng ngân hàng bất kỳ. Gạch nợ tự động ngay lập tức.</li>
                    <li><strong>Chuyển khoản qua số tài khoản định danh:</strong>Chuyển tiền tới số tài khoản chuyên thu học phí của trường, nội dung chuyển khoản bắt buộc ghi:<code>[Mã SV] [Họ và tên] [Hoc phi HK...]</code>.</li>
                    <li><strong>Cổng thanh toán điện tử VNPAY:</strong>Hỗ trợ thẻ ATM nội địa, thẻ quốc tế Visa/Mastercard.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Học phí", "Hạn nộp học phí", "Thanh toán online", "Cổng thông tin"}
        ));

        list.add(new FaqDefinition(
                "TC_HOCPHI",
                "Hạn đóng học phí là khi nào và phải làm gì nếu bị khóa cổng thông tin do nợ học phí?",
                "Điều có thể xảy ra khi nộp học phí trễ hạn và cách mở lại tài khoản đăng ký học phần sau khi hoàn tất nghĩa vụ tài chính.",
                """
                <p>Thời hạn đóng học phí được thông báo cụ thể vào đầu mỗi học kỳ (thường kéo dài 30 ngày):</p>
                <ul>
                    <li>Sinh viên nợ học phí quá hạn sẽ bị tạm khóa tài khoản đăng ký môn học và có thể không có tên trong danh sách thi kết thúc học phần.</li>
                    <li>Sau khi hoàn tất thanh toán, hệ thống sẽ tự động mở lại cổng thông tin sinh viên trong khoảng 30 phút đối với thanh toán VietQR (hoặc 1 ngày làm việc đối với chuyển khoản liên ngân hàng).</li>
                    <li>Nếu có khó khăn đặc biệt, bạn cần làm đơn xin gia hạn nộp học phí trước khi hết hạn nộp.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phí", "Hạn nộp học phí", "Thanh toán online"}
        ));

        list.add(new FaqDefinition(
                "TC_HOCPHI",
                "Cách tải hóa đơn điện tử học phí sau khi thanh toán thành công?",
                "Cách tải hóa đơn điện tử thuế giá trị gia tăng (VAT) có mã xác thực của cơ quan thuế phục vụ thanh toán cơ quan hoặc lưu trữ.",
                """
                <p>Sau khi hệ thống cập nhật giao dịch nộp học phí:</p>
                <ol>
                    <li>Đăng nhập vào mục<strong>Tra cứu học phí</strong>trên cổng thông tin sinh viên.</li>
                    <li>Chọn học kỳ đã nộp và bấm<strong>Tải hóa đơn điện tử</strong>.</li>
                    <li>Hóa đơn được xuất dưới dạng tệp PDF kèm chữ ký số hợp lệ của nhà trường.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phí", "Thanh toán online", "Cổng thông tin"}
        ));

        list.add(new FaqDefinition(
                "TC_HOCPHI",
                "Đơn giá học phí mỗi tín chỉ được tính như thế nào?",
                "Thông tin đơn giá tín chỉ môn lý thuyết, thực hành, đồ án đối với chương trình chuẩn và chất lượng cao.",
                """
                <p>Đơn giá tín chỉ được quy định theo từng khóa học và chương trình đào tạo (Đại học đại trà, Chất lượng cao tiếng Việt, Chất lượng cao tiếng Anh):</p>
                <p>Bạn xem chi tiết biểu giá tại thông báo biểu mức thu học phí đầu năm học.</p>
                """,
                FaqStatus.PENDING_REVIEW, false,
                new String[]{"Học phí", "Học phần"}
        ));

        // 7. TC_MIENGIAM
        list.add(new FaqDefinition(
                "TC_MIENGIAM",
                "Cần chuẩn bị giấy tờ gì và nộp hồ sơ miễn, giảm học phí khi nào?",
                "Điều kiện được miễn 100% hoặc giảm 70%, 50% học phí theo Nghị định 81/2021/NĐ-CP và cách làm hồ sơ.",
                """
                <p>Sinh viên thuộc các diện sau được xét miễn, giảm học phí theo quy định nhà nước:</p>
                <ul>
                    <li><strong>Miễn 100% học phí:</strong>Con liệt sĩ, con thương binh/bệnh binh nặng, người khuyết tật nặng, người dân tộc thiểu số thuộc hộ nghèo/cận nghèo.</li>
                    <li><strong>Giảm 70% học phí:</strong>Sinh viên học các ngành nghề nặng nhọc, độc hại theo danh mục quy định.</li>
                    <li><strong>Giảm 50% học phí:</strong>Con cán bộ công nhân viên chức bị tai nạn lao động hoặc mắc bệnh nghề nghiệp đang hưởng trợ cấp thường xuyên.</li>
                </ul>
                <p>Hồ sơ nộp về Phòng Kế hoạch - Tài chính trong khoảng 30 ngày kể từ ngày bắt đầu học kỳ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Miễn giảm học phí", "Học phí", "Chính sách hỗ trợ"}
        ));

        list.add(new FaqDefinition(
                "TC_MIENGIAM",
                "Có thể rút học phần và được hoàn lại học phí trong trường hợp nào?",
                "Thời hạn cho phép hủy môn học và tỷ lệ hoàn phí (100%, 50%) theo quy định quản lý tài chính của trường.",
                """
                <p>Bạn có thể rút bớt học phần và nhận lại học phí theo tỷ lệ:</p>
                <ol>
                    <li>Rút học phần trong 2 tuần đầu học kỳ: Được hoàn trả<strong>100% học phí</strong>của môn học đã rút.</li>
                    <li>Rút học phần trong tuần thứ 3 và thứ 4: Được hoàn trả<strong>50% học phí</strong>.</li>
                    <li>Sau tuần thứ 4: Không được hoàn trả học phí và môn học sẽ bị cập nhật điểm W.</li>
                </ol>
                <p>Tiền hoàn lại sẽ được trừ vào công nợ học kỳ kế tiếp hoặc chuyển khoản về tài khoản ngân hàng của sinh viên.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Miễn giảm học phí", "Học phí", "Học phần"}
        ));

        list.add(new FaqDefinition(
                "TC_MIENGIAM",
                "Sinh viên có anh chị em ruột cùng học tại trường có được giảm học phí không?",
                "Mức ưu đãi và hồ sơ minh chứng mối quan hệ ruột thịt để được giảm trừ học phí trong cùng năm học.",
                """
                <p>Gia đình có 2 anh/chị/em ruột cùng học chính quy tại HCMUTE trong cùng thời điểm:</p>
                <p>Được xét giảm 10% học phí cho người em. Hồ sơ gồm bản sao giấy khai sinh và đơn đề nghị xác nhận.</p>
                """,
                FaqStatus.ARCHIVED, false,
                new String[]{"Miễn giảm học phí", "Học phí", "Chính sách hỗ trợ"}
        ));

        // 8. KTX_DANGKY
        list.add(new FaqDefinition(
                "KTX_DANGKY",
                "Cách đăng ký ở ký túc xá cho tân sinh viên và sinh viên các khóa?",
                "Các bước đăng ký chỗ ở ký túc xá, loại phòng và mức phí cần tìm hiểu.",
                """
                <p>Quy trình đăng ký chỗ ở tại Ký túc xá HCMUTE:</p>
                <ol>
                    <li><strong>Đăng ký trực tuyến:</strong>Sinh viên vào cổng thông tin ký túc xá để chọn khu vực (Khu A hoặc Khu B), loại phòng (phòng 4 người, 6 người hoặc 8 người).</li>
                    <li><strong>Xét duyệt hồ sơ:</strong>Ban quản lý ký túc xá xét duyệt ưu tiên theo tiêu chuẩn chính sách và khoảng cách địa lý.</li>
                    <li><strong>Đóng lệ phí phòng:</strong>Sinh viên có tên trong danh sách duyệt thanh toán tiền phòng qua ngân hàng.</li>
                    <li><strong>Nhận phòng:</strong>Mang CCCD, biên lai đóng tiền và giấy xác nhận sinh viên đến làm thủ tục bàn giao chìa khóa.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Ký túc xá", "Đăng ký nội trú", "Dịch vụ sinh viên", "Sinh viên HCMUTE"}
        ));

        list.add(new FaqDefinition(
                "KTX_DANGKY",
                "Những ai được ưu tiên xét chỗ ở ký túc xá trong năm học?",
                "Thứ tự phân bổ chỉ tiêu chỗ ở ký túc xá cho sinh viên diện chính sách, vùng sâu vùng xa và tân sinh viên.",
                """
                <p>Thứ tự ưu tiên xét duyệt chỗ ở Ký túc xá:</p>
                <ul>
                    <li>Ưu tiên 1: Sinh viên là con liệt sĩ, thương binh, bệnh binh, sinh viên khuyết tật.</li>
                    <li>Ưu tiên 2: Sinh viên diện hộ nghèo, cận nghèo, mồ côi cả cha lẫn mẹ.</li>
                    <li>Ưu tiên 3: Sinh viên người dân tộc thiểu số, sinh viên có hộ khẩu thường trú tại vùng sâu, hải đảo.</li>
                    <li>Ưu tiên 4: Tân sinh viên khóa mới trúng tuyển vào trường có khoảng cách địa lý xa.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Đăng ký nội trú", "Chính sách hỗ trợ"}
        ));

        list.add(new FaqDefinition(
                "KTX_DANGKY",
                "Cần chuẩn bị giấy tờ gì và làm thủ tục nhận phòng ký túc xá khi nào?",
                "Danh mục các giấy tờ cần thiết và đồ dùng sinh hoạt cá nhân được phép mang vào ký túc xá.",
                """
                <p>Khi đến làm thủ tục nhận phòng, bạn cần chuẩn bị:</p>
                <ul>
                    <li>02 ảnh 3x4 (chụp trong khoảng 6 tháng gần nhất).</li>
                    <li>Bản sao CCCD có công chứng.</li>
                    <li>Biên nhận nộp tiền phòng ký túc xá.</li>
                    <li>Bản cam kết chấp hành nội quy Ký túc xá có chữ ký của sinh viên.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Đăng ký nội trú"}
        ));

        // 9. KTX_DICHVU
        list.add(new FaqDefinition(
                "KTX_DICHVU",
                "Làm gì khi thiết bị hoặc hệ thống điện nước trong phòng ký túc xá bị hỏng?",
                "Các bước gửi phiếu báo hỏng bóng đèn, quạt trần, vòi sen, khóa cửa và thời gian phản hồi của đội kỹ thuật ký túc xá.",
                """
                <p>Khi trang thiết bị trong phòng ở ký túc xá gặp sự cố hư hỏng:</p>
                <ol>
                    <li>Đăng nhập ứng dụng ký túc xá hoặc tạo yêu cầu hỗ trợ trên hệ thống QAUTE (chọn chuyên mục ký túc xá &amp; Dịch vụ).</li>
                    <li>Mô tả rõ sự cố (ví dụ: mất nước, chập điện bóng đèn, nghẹt cống) và ghi số phòng, số nhà.</li>
                    <li>Đội kỹ thuật ký túc xá sẽ có mặt kiểm tra và sửa chữa trong khoảng 24 giờ (đối với sự cố thông thường) hoặc trong 2 giờ (đối với sự cố khẩn cấp).</li>
                </ol>
                <p>Lưu ý: Hư hỏng do hao mòn tự nhiên được sửa chữa miễn phí; hư hỏng do cố ý làm hỏng bạn cần bồi thường theo quy chế.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Dịch vụ sinh viên", "Sửa chữa trang thiết bị"}
        ));

        list.add(new FaqDefinition(
                "KTX_DICHVU",
                "Nội quy sử dụng internet, bãi gửi xe và dịch vụ giặt ủi ở ký túc xá là gì?",
                "Cách đăng ký tài khoản wifi ký túc xá, làm vé tháng nhà xe và nội quy sử dụng máy giặt tự động.",
                """
                <p>Các tiện ích dịch vụ hỗ trợ đời sống sinh viên nội trú:</p>
                <ul>
                    <li><strong>Mạng wifi:</strong>Mỗi sinh viên được cấp 01 tài khoản truy cập wifi tốc độ cao miễn phí trong khuôn viên ký túc xá.</li>
                    <li><strong>Gửi xe:</strong>Sinh viên đăng ký vé xe tháng tại văn phòng nhà xe ký túc xá với mức giá ưu đãi.</li>
                    <li><strong>Máy giặt tự động:</strong>Sử dụng hệ thống máy giặt sấy thông minh qua ứng dụng thanh toán trực tuyến.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Dịch vụ sinh viên"}
        ));



        // FAQ mở rộng: mỗi câu hỏi được khai báo riêng theo chuyên mục.
        list.add(new FaqDefinition(
                "DT_DKHP_SAI",
                "Cách đổi nhóm lớp học phần khi đăng ký nhầm?",
                "Cách xử lý khi đã chọn nhầm mã nhóm lớp trong thời gian còn điều chỉnh.",
                """
                <p>So sánh mã môn, mã nhóm, giảng viên và khung thời gian trên thời khóa biểu. Nếu cổng còn mở chỉnh sửa, thay nhóm rồi kiểm tra lại phiếu đăng ký sau khi lưu.</p><p>Nếu đã khóa chức năng, gửi mã học phần, nhóm hiện tại và nhóm mong muốn cho Phòng Đào tạo để được hướng dẫn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phần", "Đăng ký môn"}
        ));
        list.add(new FaqDefinition(
                "DT_DKHP_SAI",
                "Làm gì khi thông tin học phần trên thời khóa biểu khác với phiếu đăng ký?",
                "Đối chiếu giờ học, phòng hoặc nhóm lớp hiển thị không khớp xác nhận đăng ký.",
                """
                <p>So sánh phiếu xác nhận với thời khóa biểu mới nhất, chú ý học kỳ và mã nhóm. Ghi lại thời điểm bạn kiểm tra và chụp hai màn hình có liên quan.</p><p>Trong yêu cầu QAUTE, chỉ rõ trường dữ liệu đang lệch để đơn vị đào tạo so sánh nguồn dữ liệu gốc.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thời khóa biểu", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "DT_DKHP_SAI",
                "Làm gì khi không thấy môn đã đăng ký trong danh sách học phần?",
                "Cách phân biệt đăng ký chưa lưu thành công với trang danh sách chưa cập nhật.",
                """
                <p>Mở lại lịch sử đăng ký và kiểm tra trạng thái của môn sau khi tải mới trang. Nếu có thông báo thao tác thất bại, chỉ thực hiện lại sau khi xác định chưa tạo đăng ký thành công.</p><p>Nếu hệ thống từng báo lưu thành công nhưng môn biến mất, gửi mã học phần, thời điểm thao tác và thông báo lỗi để kiểm tra lại.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phần", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "DT_DKHP_SAI",
                "Làm gì khi môn tiên quyết đã hoàn thành nhưng hệ thống vẫn chặn đăng ký?",
                "Cách xử lý khi điều kiện học phần chưa đồng bộ với bảng điểm.",
                """
                <p>Kiểm tra bảng điểm xem môn tiên quyết đã có kết quả chính thức chưa. Nếu điểm vừa cập nhật, dữ liệu điều kiện đăng ký có thể chưa đồng bộ.</p><p>Gửi tên môn tiên quyết, học kỳ hoàn thành và nội dung cảnh báo cho Phòng Đào tạo; không chọn môn thay thế chỉ để vượt qua thông báo hệ thống.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phần", "Bảng điểm"}
        ));
        list.add(new FaqDefinition(
                "DT_DKHP_HUY",
                "Làm thế nào để kiểm tra còn trong thời hạn được rút học phần hay không?",
                "Xác định đợt điều chỉnh hiện hành trước khi gửi yêu cầu hủy.",
                """
                <p>Đọc thông báo kế hoạch đào tạo của đúng học kỳ và khóa vì thời gian rút môn có thể khác nhau. Lưu lại phiếu đăng ký sau khi thay đổi.</p><p>Nếu chức năng tự điều chỉnh đã đóng, hỏi Phòng Đào tạo về thủ tục ngoại lệ; không dùng lịch của kỳ trước để suy ra hạn hiện tại.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hủy học phần", "Học phần"}
        ));
        list.add(new FaqDefinition(
                "DT_DKHP_HUY",
                "Rút một môn có ảnh hưởng đến điều kiện nhận học bổng không?",
                "Những yếu tố cần kiểm tra trước khi giảm số tín chỉ trong kỳ.",
                """
                <p>Tính lại tổng số tín chỉ còn đăng ký và đọc tiêu chí học bổng của đúng kỳ xét. Điều kiện có thể phụ thuộc tải học tập, kết quả và trạng thái học phần theo thông báo chính thức.</p><p>Nếu đang nhận học bổng hoặc hỗ trợ tài chính, hỏi đơn vị phụ trách trước khi xác nhận rút môn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phần", "Học bổng"}
        ));
        list.add(new FaqDefinition(
                "DT_DKHP_HUY",
                "Làm gì khi đã hủy học phần nhưng môn vẫn xuất hiện trong lịch?",
                "Kiểm tra trạng thái giao dịch khi giao diện chưa đồng bộ.",
                """
                <p>Tải lại trang đăng ký, xem danh sách học phần đã đăng ký và lịch sử thao tác nếu có. Việc môn vẫn còn hiển thị chưa đủ để kết luận giao dịch hủy thất bại.</p><p>Nếu trạng thái chưa rõ, gửi mã môn, nhóm lớp, thời điểm thao tác và ảnh thông báo; chờ xác nhận trước khi đăng ký nhóm thay thế có lịch trùng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hủy học phần", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "DT_DKHP_HUY",
                "Có thể xin rút học phần vì lý do sức khỏe hoặc bất khả kháng như thế nào?",
                "Thông tin cần chuẩn bị khi gửi đề nghị xem xét một trường hợp đặc biệt.",
                """
                <p>Nêu học phần bị ảnh hưởng, thời điểm phát sinh sự cố và lý do không thể tiếp tục học. Chuẩn bị tài liệu chứng minh phù hợp, chỉ gửi phần cần thiết và bảo vệ thông tin sức khỏe riêng tư.</p><p>Nộp yêu cầu sớm để được hướng dẫn về thẩm quyền và điều kiện áp dụng; việc chấp thuận phụ thuộc quy định và hồ sơ thực tế.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phần", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "DT_HOCVU_DIEM",
                "Làm gì khi một cột điểm thành phần bị bỏ trống?",
                "Xác minh điểm chưa được nhập hoặc chưa hiển thị sau công bố.",
                """
                <p>Kiểm tra đúng lớp học phần, kỳ học và loại điểm; so sánh đề cương để biết thành phần đó có áp dụng cho môn hay không.</p><p>Nếu điểm vẫn trống sau thời điểm công bố, ghi mã lớp, tên thành phần và ngày tra cứu để đơn vị học vụ kiểm tra dữ liệu.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điểm thi", "Bảng điểm"}
        ));
        list.add(new FaqDefinition(
                "DT_HOCVU_DIEM",
                "Làm gì khi điểm tổng kết trên cổng thông tin không khớp với cách tính đã công bố?",
                "Đối chiếu điểm thành phần và tỷ trọng trước khi báo lại thông tin chưa khớp.",
                """
                <p>Tập hợp các điểm thành phần, tỷ trọng theo đề cương và điểm tổng kết hiện trên hệ thống. Chú ý điều kiện dự thi, thành phần bắt buộc hoặc quy tắc làm tròn nếu môn có quy định riêng.</p><p>Gửi bảng so sánh cùng mã học phần và nêu rõ điểm nào không khớp để giảng viên hoặc Phòng Đào tạo xác minh.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điểm thi", "Học vụ"}
        ));
        list.add(new FaqDefinition(
                "DT_HOCVU_DIEM",
                "Làm gì khi điểm đã công bố nhưng chưa cập nhật trên hệ thống?",
                "Cách phân biệt ngày công bố kết quả với ngày đồng bộ bảng điểm.",
                """
                <p>Kiểm tra thông báo chính thức về thời gian khóa sổ điểm, sau đó tải lại bảng điểm hoặc đăng nhập lại. Thông báo của lớp và dữ liệu cổng thông tin bạn có thể cập nhật ở các thời điểm khác nhau.</p><p>Nếu đã qua mốc cập nhật được công bố, gửi học kỳ, mã lớp và bằng chứng công bố để bộ phận đào tạo tra cứu.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảng điểm", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "DT_HOCVU_DIEM",
                "Làm gì khi điểm môn học cải thiện chưa xuất hiện trong bảng điểm tích lũy?",
                "Kiểm tra cách ghi nhận kết quả của lần học hoặc thi cải thiện.",
                """
                <p>Xem cả lịch sử học phần và bảng điểm tích lũy vì từng màn hình có thể thể hiện các lần học theo cách khác nhau. So sánh quy chế áp dụng cho khóa của bạn về việc thay thế hoặc tính lại kết quả.</p><p>Gửi học kỳ của lần học trước, kỳ cải thiện và điểm tương ứng để Phòng Đào tạo kiểm tra cách cập nhật.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảng điểm", "Học vụ"}
        ));
        list.add(new FaqDefinition(
                "DT_HOCVU_BAOLUU",
                "Khi nào nên tìm hiểu thủ tục bảo lưu kết quả học tập?",
                "Cách phân biệt bảo lưu với nghỉ học không làm thủ tục hoặc rút riêng một môn.",
                """
                <p>Bảo lưu cần được đơn vị đào tạo xem xét theo quy định đang áp dụng khi bạn không thể tiếp tục học trong một khoảng thời gian. Trước khi gửi đơn, hỏi rõ phạm vi và ngày bắt đầu hiệu lực.</p><p>Không tự ý ngừng dự học hoặc dự thi khi chưa biết quyết định xử lý hồ sơ, vì có thể ảnh hưởng kết quả và tiến độ đào tạo.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo lưu", "Học vụ"}
        ));
        list.add(new FaqDefinition(
                "DT_HOCVU_BAOLUU",
                "Hồ sơ đề nghị bảo lưu cần ghi những nội dung nào?",
                "Thông tin giúp đơn vị đào tạo xác định đúng trường hợp và thời gian xin bảo lưu.",
                """
                <p>Nêu mã sinh viên, chương trình đang học, lý do, thời gian đề nghị bắt đầu và dự kiến quay lại. Tài liệu chứng minh cần phù hợp với lý do trình bày và còn đọc được.</p><p>Dùng đúng biểu mẫu và nơi tiếp nhận theo thông báo của trường; lưu mã yêu cầu để theo dõi việc bổ sung hồ sơ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo lưu", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "DT_HOCVU_BAOLUU",
                "Sau thời gian bảo lưu, cần kiểm tra những gì trước khi quay lại học?",
                "Tránh lệch kế hoạch đào tạo khi tiếp tục chương trình.",
                """
                <p>Xác nhận thời hạn bảo lưu còn hiệu lực và hỏi đơn vị đào tạo về thủ tục tiếp nhận trở lại. Kiểm tra các môn đã hoàn thành cùng thay đổi chương trình trong thời gian tạm dừng.</p><p>Sau khi trạng thái sinh viên được cập nhật, kiểm tra cổng thông tin sinh viên, kế hoạch kỳ học và quyền đăng ký môn. Nếu chưa mở quyền, gửi quyết định bảo lưu để kiểm tra lại.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo lưu", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "DT_HOCVU_BAOLUU",
                "Bảo lưu có ảnh hưởng đến học phí đã phát sinh không?",
                "Các khoản cần đối chiếu khi bảo lưu trong lúc học kỳ đã bắt đầu.",
                """
                <p>Liệt kê học phần đã đăng ký, khoản phí phát sinh và giao dịch đã thanh toán. Không mặc định mọi khoản đều được hoàn vì cách xử lý phụ thuộc quyết định bảo lưu và quy định tài chính.</p><p>Gửi yêu cầu so sánh cho đơn vị đào tạo và tài chính khi cần; giữ biên nhận cùng quyết định làm căn cứ tra cứu.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo lưu", "Học phí"}
        ));
        list.add(new FaqDefinition(
                "DT_TOTNGHIEP_HOSO",
                "Cần kiểm tra những điều kiện nào trước khi nộp hồ sơ xét tốt nghiệp?",
                "Các nhóm điều kiện nên kiểm tra trong hồ sơ học tập.",
                """
                <p>So sánh số tín chỉ tích lũy với chương trình áp dụng, học phần bắt buộc, chuẩn đầu ra nếu có, nghĩa vụ tài chính và các khoản mượn trả.</p><p>Nếu cổng thông tin sinh viên có mục kiểm tra điều kiện tốt nghiệp, lưu kết quả tra cứu. Tiêu chí cụ thể phải dựa trên quy định dành cho khóa học của bạn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Xét tốt nghiệp", "Hồ sơ tốt nghiệp"}
        ));
        list.add(new FaqDefinition(
                "DT_TOTNGHIEP_HOSO",
                "Làm gì khi chứng chỉ chuẩn đầu ra chưa được cập nhật?",
                "Phản ánh khi đã nộp chứng chỉ nhưng điều kiện tốt nghiệp vẫn chưa hoàn thành.",
                """
                <p>Kiểm tra ngày cấp, đơn vị cấp, thông tin bạn và yêu cầu định dạng. Tìm biên nhận để xác định chứng chỉ đã được tiếp nhận hay chỉ mới được gửi đi.</p><p>Nếu trạng thái chưa thay đổi, gửi mã sinh viên, tên chứng chỉ và ngày nộp. Không sửa bản scan; hỏi quy trình xác minh nếu tên hoặc số định danh khác hồ sơ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chứng chỉ", "Xét tốt nghiệp"}
        ));
        list.add(new FaqDefinition(
                "DT_TOTNGHIEP_HOSO",
                "Làm gì khi họ tên hoặc ngày sinh trong danh sách dự kiến tốt nghiệp bị sai?",
                "Giấy tờ cần có để yêu cầu hiệu chỉnh trước khi cấp văn bằng.",
                """
                <p>So sánh danh sách với giấy tờ định danh hợp lệ và hồ sơ nhập học. Ghi chính xác trường thông tin sai, giá trị đang hiển thị và giá trị theo giấy tờ gốc.</p><p>Gửi yêu cầu sớm kèm bản sao theo hướng dẫn bảo mật và giữ xác nhận tiếp nhận; không tự sửa file danh sách do trường phát hành.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ tốt nghiệp", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "DT_TOTNGHIEP_HOSO",
                "Cách theo dõi hồ sơ xét tốt nghiệp sau khi đã nộp?",
                "Cách xử lý trạng thái yêu cầu bổ sung và hồ sơ đang chờ rà soát.",
                """
                <p>Lưu mã hồ sơ, ngày gửi và danh sách tài liệu đã nộp. Khi được yêu cầu bổ sung, đọc từng mục và tải đúng tài liệu vào cùng luồng để giữ lịch sử xử lý.</p><p>Nếu hồ sơ chờ lâu hơn mốc công bố, hỏi đơn vị phụ trách bằng mã hồ sơ thay vì nộp một bộ giống hệt lần nữa.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Xét tốt nghiệp", "Thời hạn"}
        ));
        list.add(new FaqDefinition(
                "CT_XACNHAN",
                "Cách xin giấy xác nhận sinh viên để bổ sung hồ sơ cá nhân?",
                "Nêu rõ loại xác nhận, mục đích dùng và số bản cần cấp.",
                """
                <p>Ghi loại xác nhận, mục đích sử dụng, số bản và hình thức nhận nếu có nhiều lựa chọn. Kiểm tra họ tên, mã sinh viên và chương trình học trước khi gửi.</p><p>Nếu nơi nhận yêu cầu mẫu riêng, đính kèm mẫu hoặc nêu các nội dung bắt buộc để đơn vị phụ trách xem xét.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ", "Sinh viên HCMUTE"}
        ));
        list.add(new FaqDefinition(
                "CT_XACNHAN",
                "Cách xin giấy xác nhận sinh viên bằng tiếng Anh?",
                "Thông tin cần hỏi trước khi cơ quan tiếp nhận yêu cầu văn bản ngoại ngữ.",
                """
                <p>Nêu tổ chức tiếp nhận và mục đích sử dụng, sau đó hỏi đơn vị phụ trách có mẫu tiếng Anh chính thức hay không. So sánh cách viết họ tên với giấy tờ định danh nếu nơi nhận yêu cầu.</p><p>Không tự dịch rồi dùng bản dịch như văn bản do trường cấp. Hỏi quy trình dịch thuật hoặc xác nhận được chấp nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ", "Thời hạn"}
        ));
        list.add(new FaqDefinition(
                "CT_XACNHAN",
                "Cách xin xác nhận sinh viên để làm hồ sơ vay vốn học tập?",
                "Xác định đúng mẫu và thông tin mà cơ quan tiếp nhận yêu cầu.",
                """
                <p>Lấy danh sách nội dung cần xác nhận từ ngân hàng hoặc cơ quan tiếp nhận. Nếu có mẫu chính thức, đính kèm đủ các trang liên quan và kiểm tra thời hạn chấp nhận.</p><p>Chỉ yêu cầu trường xác nhận dữ liệu thuộc hồ sơ sinh viên; không yêu cầu xác nhận thay thông tin tài chính ngoài thẩm quyền.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chính sách hỗ trợ", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CT_XACNHAN",
                "Làm gì khi giấy xác nhận đã hết hạn hoặc không còn phù hợp?",
                "Cách xin cấp lại văn bản theo yêu cầu mới của cơ quan tiếp nhận.",
                """
                <p>Đọc lý do tài liệu cũ bị từ chối để xác định giấy đã hết hạn, sai thông tin hay thiếu nội dung. Khi nộp yêu cầu mới, ghi mã hoặc ngày cấp của bản trước và nội dung cần cập nhật.</p><p>Không tự sửa ngày trên giấy cũ; hỏi đơn vị phát hành về việc hủy hoặc thay thế văn bản nếu cần.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "CT_HOTRO",
                "Cách chọn loại yêu cầu hỗ trợ khi gặp khó khăn đột xuất?",
                "Mô tả vấn đề để bộ phận phụ trách xác định đúng hình thức hỗ trợ.",
                """
                <p>Trình bày vấn đề, thời điểm phát sinh, ảnh hưởng đến việc học và loại hỗ trợ muốn tìm hiểu. Nếu liên quan nhiều đơn vị, tách rõ các vấn đề để được hướng dẫn đúng đầu mối.</p><p>Chỉ gửi tài liệu cần thiết; nếu chưa biết hồ sơ nào phù hợp, hỏi trước thay vì tải lên toàn bộ giấy tờ cá nhân.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chính sách hỗ trợ", "Dịch vụ sinh viên"}
        ));
        list.add(new FaqDefinition(
                "CT_HOTRO",
                "Làm thế nào để xin hỗ trợ khi gặp khó khăn tài chính?",
                "Thông tin nên chuẩn bị khi tìm hiểu điều kiện và hồ sơ hỗ trợ.",
                """
                <p>Nêu hoàn cảnh cần được xem xét và chương trình hỗ trợ đang quan tâm. Tài liệu chứng minh cần đúng người đứng tên, còn hiệu lực và thể hiện thông tin liên quan.</p><p>Việc gửi yêu cầu không đồng nghĩa tự động được duyệt; theo dõi phản hồi chính thức về tiêu chí hoặc tài liệu còn thiếu.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chính sách hỗ trợ", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CT_HOTRO",
                "Cách phản hồi khi câu trả lời chưa giải đáp đủ vấn đề?",
                "Đề nghị làm rõ phản hồi trong yêu cầu đang mở.",
                """
                <p>Mở lại yêu cầu cũ, trích phần chưa rõ và đặt câu hỏi cụ thể về tài liệu, thời gian hoặc đơn vị tiếp nhận. Cách hỏi tập trung giúp chuyên viên trả lời đúng trọng tâm.</p><p>Nếu tình hình thay đổi, bổ sung thông tin mới vào cùng luồng trao đổi thay vì gửi lại toàn bộ nội dung đã có.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Dịch vụ sinh viên", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CT_HOTRO",
                "Khi nào nên mở yêu cầu riêng cho vấn đề mới?",
                "Cách phân biệt câu hỏi tiếp nối với vụ việc cần quy trình độc lập.",
                """
                <p>Nếu vấn đề mới liên quan trực tiếp đến hồ sơ đang xử lý, trả lời trong luồng hiện có. Nếu khác đơn vị, đối tượng hoặc cần quy trình riêng, tạo yêu cầu mới và dẫn mã yêu cầu trước.</p><p>Không gộp nhiều việc không liên quan vào cùng một mô tả, vì điều đó làm khó phân công và theo dõi kết quả.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Dịch vụ sinh viên", "Quy trình xử lý"}
        ));
        list.add(new FaqDefinition(
                "CT_HOCBONG_HOSO",
                "Cách chuẩn bị bảng điểm khi nộp hồ sơ học bổng?",
                "Chọn đúng bảng điểm và kiểm tra kỳ học được yêu cầu.",
                """
                <p>Đọc thông báo để xác định cần bảng điểm học kỳ hay bảng điểm tích lũy. Tải tài liệu từ hệ thống chính thức và kiểm tra tên, mã sinh viên cùng kỳ học.</p><p>Nếu kết quả vừa cập nhật, bảo đảm hồ sơ phản ánh bản mới nhất; không gửi ảnh chụp bị cắt mất thông tin nhận diện.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Bảng điểm"}
        ));
        list.add(new FaqDefinition(
                "CT_HOCBONG_HOSO",
                "Cần chứng minh hoàn cảnh như thế nào trong hồ sơ học bổng tài trợ?",
                "Chuẩn bị giấy tờ chứng minh theo yêu cầu riêng của chương trình.",
                """
                <p>Chỉ dùng loại giấy tờ được liệt kê trong thông báo. Kiểm tra thời hạn, cơ quan xác nhận và người đứng tên để tài liệu đúng với trường hợp của gia đình.</p><p>Thông tin hoàn cảnh là dữ liệu riêng tư; tải lên kênh chính thức và không gửi công khai trong nhóm trao đổi.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Chính sách hỗ trợ"}
        ));
        list.add(new FaqDefinition(
                "CT_HOCBONG_HOSO",
                "Cách khai báo tài khoản nhận học bổng an toàn?",
                "Kiểm tra người thụ hưởng và hạn chế sai số khi khai báo thanh toán.",
                """
                <p>Điền tài khoản theo hướng dẫn, so sánh tên chủ tài khoản, ngân hàng và số tài khoản trước khi gửi. Chỉ công khai một phần thông tin khi cần trao đổi về lỗi.</p><p>Nếu khai sai, báo ngay qua kênh chính thức và yêu cầu xác nhận cập nhật. Không gửi mật khẩu ngân hàng hoặc mã OTP.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CT_HOCBONG_HOSO",
                "Làm gì khi hồ sơ học bổng bị yêu cầu bổ sung minh chứng?",
                "Hoàn thiện đúng phần tài liệu còn thiếu mà không tạo hồ sơ trùng.",
                """
                <p>Đọc từng mục cần bổ sung, đặt tên tệp dễ nhận biết và kiểm tra ảnh đủ góc, rõ chữ, đủ mặt giấy tờ liên quan.</p><p>Gửi tài liệu trong hồ sơ đang xử lý và giải thích ngắn nếu thay thế bản cũ. Nếu không thể lấy đúng giấy tờ đúng hạn, liên hệ đơn vị phụ trách trước mốc bổ sung.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CT_HOCBONG_KETQUA",
                "Cách tra cứu danh sách học bổng đúng học kỳ?",
                "Tránh nhầm danh sách sơ bộ, chính thức và kết quả của kỳ khác.",
                """
                <p>Kiểm tra tên chương trình, học kỳ, ngày công bố và ghi chú sửa đổi. Nếu có nhiều đợt xét, xác nhận đúng đợt trước khi so sánh tên mình.</p><p>Nếu chưa có tên trong danh sách, đọc điều kiện và trạng thái hồ sơ rồi gửi phản ánh cụ thể; không đăng danh sách có dữ liệu của người khác lên kênh công khai.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Thời hạn"}
        ));
        list.add(new FaqDefinition(
                "CT_HOCBONG_KETQUA",
                "Làm gì khi đủ điều kiện ban đầu nhưng chưa có tên trong danh sách?",
                "Thông tin cần gửi để kiểm tra trường hợp có thể bị bỏ sót.",
                """
                <p>Giữ thông báo đủ điều kiện, mã hồ sơ và minh chứng đã gửi. Ghi chương trình, học kỳ, thời điểm nhận thông báo và danh sách đã kiểm tra.</p><p>Đề nghị so sánh trạng thái hồ sơ; đủ điều kiện sơ bộ có thể chưa đồng nghĩa với quyết định trao nếu còn bước xếp hạng hoặc phê duyệt.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CT_HOCBONG_KETQUA",
                "Làm gì khi muốn làm rõ tiêu chí xếp hạng khi không đạt học bổng?",
                "Yêu cầu giải thích căn cứ áp dụng cho hồ sơ của chính mình.",
                """
                <p>Đọc tiêu chí công bố và xác định nội dung cần làm rõ như điều kiện loại trừ, cách tính điểm hoặc trạng thái minh chứng. Gửi tên chương trình và học kỳ cùng dữ liệu liên quan của bạn.</p><p>Không yêu cầu thông tin riêng tư của ứng viên khác; tập trung vào quy trình phản hồi nếu phát hiện sai sót ở hồ sơ của mình.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Chính sách hỗ trợ"}
        ));
        list.add(new FaqDefinition(
                "CT_HOCBONG_KETQUA",
                "Cách theo dõi thời điểm chi trả sau quyết định học bổng?",
                "Tra cứu thanh toán và phản ánh khoản hỗ trợ chưa được nhận.",
                """
                <p>Đọc quyết định trao học bổng, kiểm tra giai đoạn chi trả và xác minh tài khoản đã khai báo. Xem hồ sơ có còn mục cần bổ sung hay không.</p><p>Nếu thời điểm dự kiến đã qua, gửi mã hồ sơ và chương trình cho bộ phận phụ trách. Không gửi mật khẩu hoặc mã xác thực ngân hàng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Thanh toán online"}
        ));
        list.add(new FaqDefinition(
                "CT_RENLUYEN_DIEM",
                "Làm gì khi hoạt động đã tham gia nhưng chưa được tính điểm rèn luyện?",
                "Kiểm tra hoạt động và gửi minh chứng để đối chiếu.",
                """
                <p>Xác nhận hoạt động có thuộc tiêu chí kỳ hiện tại không và ban tổ chức đã cập nhật người tham gia chưa. Chuẩn bị tên hoạt động, ngày diễn ra, vai trò và xác nhận nếu có.</p><p>Nêu đúng tiêu chí cần kiểm tra; tham gia hoạt động không tự động bảo đảm được cộng điểm nếu hoạt động không nằm trong khung đánh giá.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điểm rèn luyện", "Hoạt động phong trào"}
        ));
        list.add(new FaqDefinition(
                "CT_RENLUYEN_DIEM",
                "Làm gì khi điểm sau họp lớp khác điểm cá nhân tự đánh giá?",
                "Hiểu các giai đoạn tự đánh giá, họp lớp và phê duyệt.",
                """
                <p>Điểm tự đánh giá là một bước; kết quả có thể được rà soát minh chứng và điều chỉnh theo tiêu chí ở bước tiếp theo. Kiểm tra nhận xét hoặc biên bản nếu có.</p><p>Nếu tiêu chí bị cập nhật sai, gửi phản hồi theo hạn công bố và yêu cầu so sánh từng nội dung thay vì chỉ yêu cầu tăng tổng điểm.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điểm rèn luyện", "Học vụ"}
        ));
        list.add(new FaqDefinition(
                "CT_RENLUYEN_DIEM",
                "Làm gì khi điểm rèn luyện chưa cập nhật sau khi được phê duyệt?",
                "Cách phân biệt chậm đồng bộ với kết quả chưa chốt chính thức.",
                """
                <p>Kiểm tra thông báo công bố và trạng thái kết quả, sau đó tải mới trang để loại trừ lỗi hiển thị.</p><p>Nếu đã có xác nhận phê duyệt nhưng cổng thông tin sinh viên vẫn hiện kết quả cũ, gửi học kỳ, lớp và bằng chứng thông báo để đơn vị phụ trách kiểm tra.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điểm rèn luyện", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "CT_RENLUYEN_DIEM",
                "Làm gì khi tổng điểm rèn luyện bị nhập nhầm sau khi khóa kỳ?",
                "Thông tin cần ghi lại khi yêu cầu rà soát kết quả đã chốt.",
                """
                <p>So sánh tổng điểm với các tiêu chí thành phần đã được phê duyệt. Nếu có ảnh chụp trước và sau cập nhật, gửi cả hai để xác định thời điểm thay đổi.</p><p>Hỏi quy trình điều chỉnh sau khóa kỳ và thời hạn nhận phản ánh; không tự sửa ảnh hoặc văn bản kết quả.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điểm rèn luyện", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CT_RENLUYEN_MINHCHUNG",
                "Ảnh chứng nhận hoạt động cần thể hiện những thông tin nào?",
                "Kiểm tra ảnh minh chứng trước khi tải lên hệ thống đánh giá.",
                """
                <p>Ảnh cần đọc được tên hoạt động, đơn vị tổ chức, thời gian và xác nhận người tham gia nếu giấy tờ có các mục đó. Tránh ảnh lóa, mất góc hoặc bị cắt phần ký xác nhận.</p><p>Nếu có nhiều trang, sắp xếp theo thứ tự dễ đọc và chỉ tải tài liệu liên quan đến tiêu chí đang khai báo.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Minh chứng rèn luyện", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CT_RENLUYEN_MINHCHUNG",
                "Nếu hoạt động trực tuyến không có giấy chứng nhận riêng thì cần nộp minh chứng gì?",
                "Hỏi ban tổ chức về minh chứng thay thế được chấp nhận.",
                """
                <p>Kiểm tra hướng dẫn tiêu chí; nếu ban tổ chức có danh sách tham dự hoặc email xác nhận, xin xác nhận thể hiện tên hoạt động, ngày diễn ra và tên người tham gia.</p><p>Ảnh màn hình chỉ nên dùng nếu quy trình cho phép và có đủ ngữ cảnh. Hỏi cán bộ phụ trách trước hạn thay vì tự tạo giấy xác nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Minh chứng rèn luyện", "Hoạt động phong trào"}
        ));
        list.add(new FaqDefinition(
                "CT_RENLUYEN_MINHCHUNG",
                "Cách bổ sung minh chứng sau khi gửi bản tự đánh giá?",
                "Cập nhật tài liệu mà không làm mất nội dung đã hoàn thành.",
                """
                <p>Mở đúng kỳ đánh giá và kiểm tra hồ sơ còn quyền chỉnh sửa không. Nếu còn, bổ sung tệp vào tiêu chí tương ứng rồi lưu lại để xác nhận tài liệu mới được cập nhật.</p><p>Nếu hồ sơ đã chuyển sang bước xét duyệt, gửi yêu cầu kèm mã hồ sơ và nói rõ minh chứng thuộc tiêu chí nào thay vì tạo bản đánh giá mới.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Minh chứng rèn luyện", "Điểm rèn luyện"}
        ));
        list.add(new FaqDefinition(
                "CT_RENLUYEN_MINHCHUNG",
                "Cách sắp xếp nhiều minh chứng cho cùng một tiêu chí?",
                "Trình bày tài liệu theo từng hoạt động để người xét duyệt dễ kiểm tra.",
                """
                <p>Đặt tên tệp theo ngày và tên hoạt động, sau đó sắp xếp theo thời gian hoặc nhóm tiêu chí. Nếu chỉ được tải một tệp, gộp trang theo thứ tự và thêm mục lục ngắn.</p><p>Mở lại tệp trước khi gửi để kiểm tra trang xoay, chữ nhỏ hoặc trùng lặp; không dùng một minh chứng cho nhiều mục nếu quy định yêu cầu bằng chứng riêng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Minh chứng rèn luyện", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CT_XACNHAN_GIAYTO",
                "Cách xin bản sao bảng điểm có xác nhận của trường?",
                "Nêu đúng loại bảng điểm và mục đích sử dụng văn bản.",
                """
                <p>Ghi rõ cần bảng điểm học kỳ hay toàn khóa, ngôn ngữ, số bản và mục đích nộp. Kiểm tra dữ liệu cổng thông tin sinh viên trước khi xin cấp để phát hiện sai sót sớm.</p><p>Nếu nơi nhận yêu cầu dấu, chữ ký hoặc thời hạn tài liệu, nêu rõ trong yêu cầu và hỏi khả năng đáp ứng; không chỉnh sửa file bảng điểm đã tải.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ", "Bảng điểm"}
        ));
        list.add(new FaqDefinition(
                "CT_XACNHAN_GIAYTO",
                "Cách xin cấp lại thẻ sinh viên khi mất hoặc hư hỏng?",
                "Thông tin cần có để yêu cầu cấp thẻ mới và xử lý thẻ cũ.",
                """
                <p>Nêu tình trạng thẻ và xác nhận lại thông tin liên hệ. Thực hiện bước xác minh danh tính hoặc lệ phí nếu quy trình chính thức yêu cầu.</p><p>Nếu thẻ có chức năng ra vào hoặc thanh toán, hỏi đơn vị quản lý về cách vô hiệu hóa thẻ thất lạc. Không đăng công khai ảnh thẻ có mã định danh đầy đủ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ", "Sinh viên HCMUTE"}
        ));
        list.add(new FaqDefinition(
                "CT_XACNHAN_GIAYTO",
                "Cách xin giấy xác nhận kết quả học tập cho đơn vị ngoài trường?",
                "Làm rõ phạm vi dữ liệu học tập mà cơ quan tiếp nhận yêu cầu.",
                """
                <p>Đính kèm yêu cầu của cơ quan tiếp nhận nếu có và xác định họ cần xác nhận tình trạng học, điểm từng kỳ hay số tín chỉ tích lũy. Chỉ yêu cầu xác nhận dữ liệu thuộc hồ sơ và thẩm quyền gửi.</p><p>Rà soát kỳ học trước khi gửi; nếu cần văn bản đóng dấu, hỏi thời gian cấp để chủ động hạn nộp cho bên ngoài.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ", "Bảng điểm"}
        ));
        list.add(new FaqDefinition(
                "CT_XACNHAN_GIAYTO",
                "Làm gì khi thông tin giấy xác nhận khác hồ sơ sinh viên hiện tại?",
                "Các bước đối chiếu và yêu cầu điều chỉnh văn bản đã cấp.",
                """
                <p>So sánh giấy xác nhận với dữ liệu cá nhân và giấy tờ gốc; nêu rõ mục sai như họ tên, chương trình hoặc tình trạng học tập. Giữ bản đã cấp để đơn vị phát hành xác định văn bản cần điều chỉnh.</p><p>Gửi yêu cầu qua kênh chính thức, không tự sửa PDF hoặc bản giấy. Hỏi có cần nộp lại văn bản cũ trước khi nhận bản thay thế hay không.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "TC_CHUNGTU",
                "Làm gì khi không tìm thấy biên nhận sau khi thanh toán học phí?",
                "Kiểm tra lịch sử giao dịch và dữ liệu cần có để tra cứu chứng từ.",
                """
                <p>Kiểm tra trạng thái giao dịch trong ứng dụng ngân hàng hoặc cổng thanh toán, tìm email xác nhận và so sánh số tiền, thời điểm, mã giao dịch cùng nội dung chuyển khoản.</p><p>Nếu giao dịch thành công nhưng biên nhận của trường chưa xuất hiện, gửi mã sinh viên và mã giao dịch cho bộ phận tài chính để đối soát. Không gửi OTP hay mật khẩu ngân hàng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phí", "Chứng từ"}
        ));
        list.add(new FaqDefinition(
                "TC_CHUNGTU",
                "Làm gì khi biên nhận học phí ghi sai tên hoặc học kỳ?",
                "Yêu cầu điều chỉnh chứng từ được tạo không chính xác.",
                """
                <p>So sánh biên nhận với giao dịch gốc và hồ sơ sinh viên, ghi trường sai cùng giá trị đúng. Đính kèm chứng từ có lỗi và mã giao dịch để tìm đúng bản ghi.</p><p>Không sửa PDF bằng phần mềm cá nhân; việc phát hành lại cần do đơn vị có thẩm quyền thực hiện.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chứng từ", "Học phí"}
        ));
        list.add(new FaqDefinition(
                "TC_CHUNGTU",
                "Cách tải lại chứng từ học phí của học kỳ trước?",
                "Tra cứu biên nhận cũ để chứng minh nghĩa vụ tài chính đã hoàn thành.",
                """
                <p>Chọn đúng kỳ và loại giao dịch trong khu vực tra cứu tài chính. Lưu bản gốc với tên tệp có kỳ học và ngày thanh toán để dễ tìm lại.</p><p>Nếu dữ liệu kỳ cũ không hiện trực tuyến, gửi khoảng thời gian, mã sinh viên và thông tin giao dịch còn lưu. Không tạo giao dịch mới chỉ vì chưa tìm thấy chứng từ cũ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chứng từ", "Thanh toán online"}
        ));
        list.add(new FaqDefinition(
                "TC_CHUNGTU",
                "Phân biệt xác nhận ngân hàng với chứng từ do trường ghi nhận?",
                "Lý do giao dịch ngân hàng thành công nhưng công nợ cổng thông tin sinh viên chưa đổi.",
                """
                <p>Biên nhận ngân hàng thể hiện trạng thái chuyển tiền; chứng từ của trường phản ánh khoản tiền đã được hạch toán vào hồ sơ sinh viên. Hai hệ thống có thể cập nhật ở các thời điểm khác nhau.</p><p>Giữ cả hai chứng từ, kiểm tra nội dung chuyển khoản và mã sinh viên. Cung cấp tài liệu cho bộ phận tài chính qua kênh bảo mật.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chứng từ", "Đối soát học phí"}
        ));
        list.add(new FaqDefinition(
                "TC_THANHTOAN",
                "Cách chọn đúng khoản phải nộp khi có nhiều mục học phí?",
                "Xem chi tiết công nợ trước khi xác nhận thanh toán.",
                """
                <p>Mở màn hình công nợ, đọc kỳ học và tên khoản rồi so sánh với thông báo thu phí chính thức. Nếu có nhiều dòng, ghi lại mã tham chiếu mà cổng yêu cầu.</p><p>Không dùng nội dung chuyển khoản của kỳ trước nếu hệ thống đã cấp mã mới. Khi số tiền không khớp thông báo, yêu cầu so sánh trước khi chuyển.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phí", "Thanh toán online"}
        ));
        list.add(new FaqDefinition(
                "TC_THANHTOAN",
                "Cách chuyển khoản học phí cần ghi nội dung gì?",
                "Đối chiếu nội dung chuyển tiền để giao dịch định danh đúng người học.",
                """
                <p>Dùng chính xác nội dung do trường hoặc cổng thanh toán hướng dẫn ở thời điểm giao dịch. Kiểm tra mã sinh viên, kỳ hoặc mã công nợ nếu được yêu cầu; không tự rút gọn làm mất mã so sánh.</p><p>Lưu biên nhận sau khi chuyển. Nếu ghi sai nội dung, liên hệ tài chính để hỏi cách xác minh thay vì chuyển thêm tiền ngay.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phí", "Chứng từ"}
        ));
        list.add(new FaqDefinition(
                "TC_THANHTOAN",
                "Nên thanh toán từng khoản hay toàn bộ số dư đang hiển thị?",
                "Chọn phương án trả tiền khi các khoản có thời hạn khác nhau.",
                """
                <p>So sánh thông báo để xác định khoản nào đến hạn và cổng có hỗ trợ thanh toán gộp hay không. Nếu các khoản thuộc kỳ hoặc mục thu khác nhau, kiểm tra mã tham chiếu riêng.</p><p>Khi chưa chắc khoản nào đã thanh toán, tra cứu lịch sử giao dịch trước khi tạo lệnh mới; hỏi tài chính nếu tổng số dư và chi tiết không khớp.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phí", "Hạn nộp học phí"}
        ));
        list.add(new FaqDefinition(
                "TC_THANHTOAN",
                "Làm gì khi đã thanh toán thành công nhưng cổng thông tin vẫn báo còn nợ?",
                "Xác định công nợ chưa đồng bộ hay khoản tiền chưa được phân bổ.",
                """
                <p>So sánh trạng thái giao dịch với mã công nợ và kiểm tra nội dung chuyển khoản. Lưu biên nhận, số tiền, thời điểm và mã giao dịch để kiểm tra lại.</p><p>Nếu trạng thái chưa đổi sau thời gian xử lý được thông báo, gửi yêu cầu cho tài chính. Không trả lần hai chỉ dựa vào cảnh báo nợ khi chưa kiểm tra lịch sử.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đối soát học phí", "Thanh toán online"}
        ));
        list.add(new FaqDefinition(
                "TC_HOCPHI_DOISOAT",
                "Làm gì khi ngân hàng đã trừ tiền nhưng mã giao dịch vẫn chờ xử lý?",
                "Lưu thông tin và kiểm tra lại khi chưa có trạng thái cuối cùng.",
                """
                <p>Ghi mã giao dịch, số tiền và trạng thái hiển thị; không chia sẻ thông tin đăng nhập hay mã xác thực. Kiểm tra thời gian phản hồi của kênh thanh toán trước khi thao tác lại.</p><p>Nếu trạng thái vẫn chờ sau mốc thông báo, gửi mã giao dịch qua kênh tài chính chính thức. Tránh tạo lệnh mới cho cùng khoản khi kết quả lần đầu chưa rõ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đối soát học phí", "Thanh toán online"}
        ));
        list.add(new FaqDefinition(
                "TC_HOCPHI_DOISOAT",
                "Làm gì khi số tiền đã nộp lớn hơn khoản công nợ đang hiển thị?",
                "Yêu cầu xác minh khoản nộp dư và phương án xử lý.",
                """
                <p>So sánh biên nhận với công nợ tại thời điểm nộp và kiểm tra có khoản phí khác được thu cùng lúc không. Gửi mã giao dịch, kỳ học và ảnh chi tiết công nợ để tài chính xác minh.</p><p>Không tự trừ khoản dư vào kỳ sau khi chưa có hướng dẫn. Việc hoàn trả hoặc phân bổ phụ thuộc quy định và kết quả hạch toán.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đối soát học phí", "Học phí"}
        ));
        list.add(new FaqDefinition(
                "TC_HOCPHI_DOISOAT",
                "Cách thanh toán đúng số tiền nhưng cổng thông tin sinh viên phân bổ nhầm kỳ?",
                "Cung cấp dữ liệu để chuyển khoản đã thu vào đúng công nợ.",
                """
                <p>Kiểm tra nội dung lệnh thanh toán và mã tham chiếu để xác định kỳ được cập nhật. Chuẩn bị biên nhận ngân hàng và ảnh màn hình công nợ đang phân bổ sai.</p><p>Nêu rõ kỳ cần điều chỉnh. Không gửi thanh toán lần nữa cho kỳ đó cho đến khi tài chính xác nhận cách xử lý khoản đã nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đối soát học phí", "Chứng từ"}
        ));
        list.add(new FaqDefinition(
                "TC_HOCPHI_DOISOAT",
                "Cần xác nhận không còn công nợ trước thủ tục khác?",
                "Yêu cầu kiểm tra các khoản đang mở và nhận kết quả đối chiếu.",
                """
                <p>Tra cứu công nợ theo từng kỳ, lưu các giao dịch gần nhất và nêu mục đích cần xác nhận. Nếu cổng thông tin sinh viên còn khoản nợ đã thanh toán, đính kèm chứng từ đúng khoản đó.</p><p>Yêu cầu đơn vị tài chính xác nhận kết quả cuối cùng thay vì chỉ dựa vào số dư tổng trên màn hình.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đối soát học phí", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "TC_HOCPHI_GIAHAN",
                "Nên nộp đơn xin gia hạn học phí trước hạn bao lâu?",
                "Xác định đúng mốc tiếp nhận và hạn thanh toán của học kỳ.",
                """
                <p>Đọc thông báo thu học phí hiện tại để tách hạn thanh toán và hạn gửi đề nghị gia hạn; hai mốc có thể khác nhau. Nếu cần gia hạn, gửi trước hạn và nêu thời điểm dự kiến hoàn thành nghĩa vụ.</p><p>Hồ sơ đang chờ không tự động kéo dài thời hạn. Kiểm tra quyết định chấp thuận trước khi dựa vào phương án gia hạn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hạn nộp học phí", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "TC_HOCPHI_GIAHAN",
                "Đơn xin gia hạn học phí cần ghi những thông tin gì?",
                "Mô tả lý do và kế hoạch thanh toán để đơn vị có cơ sở xem xét.",
                """
                <p>Nêu mã sinh viên, kỳ học, khoản xin gia hạn, lý do và ngày dự kiến thanh toán. Tài liệu minh chứng phải phù hợp tình huống, dễ đọc và không chứa dữ liệu không cần thiết.</p><p>Nếu muốn thanh toán một phần, hỏi tài chính xem có được chấp nhận không. Việc duyệt phụ thuộc quy định hiện hành và hồ sơ thực tế.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phí", "Chính sách hỗ trợ"}
        ));
        list.add(new FaqDefinition(
                "TC_HOCPHI_GIAHAN",
                "Có thể đổi ngày thanh toán sau khi đã gửi đơn gia hạn không?",
                "Cập nhật kế hoạch khi tình hình thay đổi sau khi nộp hồ sơ.",
                """
                <p>Mở yêu cầu gia hạn hiện tại và nêu mã hồ sơ, ngày dự kiến ban đầu cùng ngày muốn thay thế. Giải thích ngắn lý do đổi kế hoạch để chuyên viên hiểu thay đổi.</p><p>Gửi cập nhật không đồng nghĩa được gia hạn thêm; chờ phản hồi chính thức và xác nhận điều kiện áp dụng trong thời gian chờ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phí", "Thời hạn"}
        ));
        list.add(new FaqDefinition(
                "TC_HOCPHI_GIAHAN",
                "Làm gì khi đã được gia hạn nhưng cổng thông tin sinh viên vẫn hiện cảnh báo quá hạn?",
                "Kiểm tra trạng thái trên cổng chưa phản ánh quyết định gia hạn.",
                """
                <p>So sánh quyết định với mã sinh viên, kỳ học và khoản phí; xem có điều kiện hoặc ngày hiệu lực riêng không.</p><p>Nếu cảnh báo vẫn còn, gửi quyết định và ảnh màn hình cho tài chính để cập nhật. Không bỏ qua thông báo khác khi chưa được xác nhận cảnh báo đã xử lý.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hạn nộp học phí", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "TC_MIENGIAM_HOSO",
                "Cách xác định hồ sơ miễn giảm học phí phù hợp với đối tượng?",
                "Đối chiếu nhóm đối tượng và giấy tờ được chấp nhận.",
                """
                <p>Đọc danh sách đối tượng của chính sách hiện hành và xác định trường hợp của mình. Chuẩn bị đúng giấy tờ theo thông báo; tài liệu người thân chỉ cần khi quy định yêu cầu chứng minh quan hệ hoặc hoàn cảnh hộ gia đình.</p><p>Nếu có nhiều diện phù hợp, hỏi bộ phận phụ trách nên nộp hồ sơ nào. Điều kiện hưởng phải căn cứ văn bản chính thức.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Miễn giảm học phí", "Chính sách hỗ trợ"}
        ));
        list.add(new FaqDefinition(
                "TC_MIENGIAM_HOSO",
                "Giấy tờ miễn giảm cần chứng thực theo hình thức nào?",
                "Kiểm tra cách tiếp nhận bản điện tử, bản sao hoặc bản đối chiếu trực tiếp.",
                """
                <p>Đọc hướng dẫn để biết bản scan, bản sao chứng thực hay bản so sánh trực tiếp được chấp nhận. Kiểm tra thời hạn giấy xác nhận, tên người đứng tên và dấu hoặc chữ ký cần thiết.</p><p>Nếu giấy tờ đã cũ hoặc dữ liệu thay đổi, hỏi liệu cần cấp mới. Không tải ảnh mờ hoặc thiếu mặt giấy có thông tin.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Miễn giảm học phí", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "TC_MIENGIAM_HOSO",
                "Làm thế nào để gia hạn chế độ miễn, giảm học phí cho học kỳ tiếp theo?",
                "Xác định chính sách có yêu cầu nộp lại hồ sơ định kỳ hay không.",
                """
                <p>Kiểm tra quyết định đã cấp xem có giới hạn thời gian, kỳ học hoặc điều kiện duy trì không. Một số trường hợp cần cập nhật giấy xác nhận khi hoàn cảnh hoặc thời hạn tài liệu thay đổi.</p><p>Gửi mã hồ sơ cũ cùng thông tin kỳ mới để hỏi tài liệu cần bổ sung. Không mặc định chế độ tự kéo dài nếu thông báo yêu cầu xét lại.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Miễn giảm học phí", "Thời hạn"}
        ));
        list.add(new FaqDefinition(
                "TC_MIENGIAM_HOSO",
                "Làm gì khi hồ sơ bị yêu cầu bổ sung giấy tờ chứng minh quan hệ gia đình?",
                "Bổ sung căn cứ quan hệ khi tài liệu hiện tại chưa đủ xác minh.",
                """
                <p>Đọc phản hồi để biết mối quan hệ nào cần chứng minh và loại giấy tờ còn thiếu. Chuẩn bị trích lục hoặc giấy tờ hộ tịch phù hợp, kiểm tra thông tin hai bên khớp nhau.</p><p>Gửi tài liệu trong hồ sơ cũ kèm lời giải thích ngắn. Nếu không thể lấy đúng giấy tờ, hỏi loại chứng cứ thay thế được phép dùng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Miễn giảm học phí", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "TC_THANHTOAN_LOI",
                "Làm gì khi trang thanh toán không mở sau khi nhấn tiếp tục?",
                "Kiểm tra phiên đăng nhập, trình duyệt và trạng thái cổng.",
                """
                <p>Tải mới trang, kiểm tra kết nối và mở lại từ mục học phí trong phiên hợp lệ. Nếu cửa sổ thanh toán bị chặn, kiểm tra thiết lập trình duyệt; không nhập dữ liệu ngân hàng vào trang không rõ nguồn.</p><p>Nếu lỗi lặp lại, ghi thời điểm, trình duyệt và thông báo. Kiểm tra lịch sử giao dịch trước khi thử lại.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Lỗi thường gặp", "Thanh toán online"}
        ));
        list.add(new FaqDefinition(
                "TC_THANHTOAN_LOI",
                "Làm gì khi mã QR thanh toán hết hạn trước khi quét?",
                "Tạo mã mới và xác nhận nội dung trước khi chuyển tiền.",
                """
                <p>Quay về trang công nợ và tạo mã mới nếu hệ thống cho phép. Trước khi xác nhận trên ứng dụng ngân hàng, kiểm tra người thụ hưởng, số tiền và mã tham chiếu đi kèm QR.</p><p>Không dùng ảnh QR cũ nếu mã có thời hạn. Nếu mã mới cũng lỗi, gửi thông báo và thời điểm phát sinh cho bộ phận hỗ trợ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thanh toán online", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "TC_THANHTOAN_LOI",
                "Làm gì khi ứng dụng báo lỗi nhưng ngân hàng đã ghi nợ?",
                "Tra soát khi thông báo lỗi và trạng thái tài khoản không trùng nhau.",
                """
                <p>Không thanh toán lại ngay. Kiểm tra lịch sử ngân hàng, lưu mã giao dịch và xác định trạng thái thành công, đang xử lý hay bị đảo. Sau đó kiểm tra lịch sử trên cổng thông tin sinh viên.</p><p>Gửi cả hai trạng thái qua kênh chính thức; chỉ thanh toán lại khi đã xác nhận giao dịch đầu tiên không được cập nhật.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thanh toán online", "Đối soát học phí"}
        ));
        list.add(new FaqDefinition(
                "TC_THANHTOAN_LOI",
                "Làm gì khi không nhận phản hồi sau bước xác thực thanh toán?",
                "Kiểm tra trạng thái khi giao dịch dừng ở trang chờ hoặc quay lại trang trước.",
                """
                <p>Chờ cổng phản hồi theo hướng dẫn và tránh tải lại liên tục khi giao dịch đang xử lý. Sau đó kiểm tra lịch sử giao dịch ngân hàng cùng lịch sử thanh toán cổng thông tin sinh viên.</p><p>Nếu hai hệ thống không đồng nhất, gửi mã đơn hàng và thời điểm giao dịch. Không gửi OTP đã dùng hoặc thông tin bảo mật ngân hàng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thanh toán online", "Lỗi hệ thống"}
        ));
        list.add(new FaqDefinition(
                "KTX_DANGKY_HOSO",
                "Cách chuẩn bị giấy tờ trước khi đăng ký nội trú?",
                "Lập danh sách tài liệu theo thông báo chính thức của đúng đợt.",
                """
                <p>Phân biệt giấy tờ bắt buộc với tài liệu chỉ áp dụng cho nhóm ưu tiên. Kiểm tra thông tin cá nhân thống nhất trên biểu mẫu và giấy tờ đính kèm, đặc biệt thời hạn xác nhận nếu có.</p><p>Đặt tên tệp dễ nhận biết, tải đúng mục và giữ bản gốc để so sánh. Ưu tiên danh sách chính thức của đợt đăng ký hơn hướng dẫn truyền miệng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "KTX_DANGKY_HOSO",
                "Cách chọn loại phòng nhưng hệ thống không ghi nhận?",
                "Xác minh lựa chọn đã lưu trước khi đăng ký lại.",
                """
                <p>Sau khi chọn khu và loại phòng, bấm lưu hoặc xác nhận cuối cùng rồi kiểm tra trạng thái. Nếu trang báo hết chỗ hoặc phiên hết hạn, đọc thông báo trước khi thao tác lại.</p><p>Nếu lựa chọn hiện trong bản tóm tắt nhưng hồ sơ vẫn chưa hoàn thành, gửi ảnh màn hình và thời điểm thao tác cho ban quản lý ký túc xá.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "KTX_DANGKY_HOSO",
                "Cách khai báo diện ưu tiên khi đăng ký chỗ ở ký túc xá?",
                "Chỉ chọn nhóm ưu tiên có thể chứng minh bằng giấy tờ hợp lệ.",
                """
                <p>Đọc mô tả từng diện ưu tiên và chọn nhóm phù hợp với tài liệu mình có. Kiểm tra thông tin về quan hệ gia đình hoặc nơi cư trú nếu tiêu chí yêu cầu.</p><p>Nếu giấy tờ có tên cũ hoặc dữ liệu khác hồ sơ, hỏi cách cập nhật trước hạn. Không chọn diện ưu tiên dựa trên suy đoán.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Chính sách hỗ trợ"}
        ));
        list.add(new FaqDefinition(
                "KTX_DANGKY_HOSO",
                "Cách sửa hồ sơ nội trú sau khi đã gửi?",
                "Xử lý thông tin liên hệ, loại phòng hoặc tài liệu bị khai sai.",
                """
                <p>Kiểm tra trạng thái xem còn quyền chỉnh sửa không. Nếu chức năng đã khóa, gửi mã hồ sơ, trường thông tin sai và giá trị đúng để ban quản lý xác minh.</p><p>Không tạo hồ sơ mới trừ khi được hướng dẫn vì hồ sơ trùng có thể ảnh hưởng xét duyệt. Theo dõi phản hồi trước khi đóng phí hoặc nhận phòng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "KTX_DANGKY_GIAHAN",
                "Làm thế nào để đăng ký gia hạn thời gian ở ký túc xá?",
                "Tìm đúng đợt gia hạn và chuẩn bị thông tin lưu trú hiện tại.",
                """
                <p>Kiểm tra thông báo của khu ký túc xá về hạn nộp, điều kiện tiếp tục ở và khoản phí kỳ mới. Chuẩn bị mã phòng, kỳ lưu trú hiện tại và thông tin liên hệ đã cập nhật.</p><p>Gửi đăng ký trong thời gian công bố và lưu lại xác nhận; việc đang ở trong phòng không đồng nghĩa kỳ sau đã được duyệt.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Gia hạn nội trú"}
        ));
        list.add(new FaqDefinition(
                "KTX_DANGKY_GIAHAN",
                "Có thể đổi phòng khi đồng thời gia hạn nội trú không?",
                "Nêu rõ nhu cầu tiếp tục ở và nhu cầu chuyển phòng để được hướng dẫn.",
                """
                <p>Ghi phòng hiện tại, lý do xin đổi và nguyện vọng mới nếu hệ thống cho phép. Hỏi xem đổi phòng xử lý cùng hồ sơ gia hạn hay cần gửi đề nghị riêng.</p><p>Không tự chuyển đồ sang phòng khác trước khi có xác nhận phân phòng; giữ thông báo bàn giao để tránh nhầm trách nhiệm và khoản phí.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "KTX_DANGKY_GIAHAN",
                "Cách nộp hồ sơ gia hạn sau hạn công bố?",
                "Hỏi về khả năng xử lý hồ sơ muộn và tình trạng chỗ ở kỳ tới.",
                """
                <p>Liên hệ ban quản lý, nêu mã phòng, thời gian mong muốn tiếp tục ở và lý do nộp muộn. Hỏi liệu còn suất, quy trình bổ sung hoặc danh sách chờ hay không.</p><p>Không mặc định hồ sơ muộn được chấp nhận; xác minh bằng văn bản thời hạn lưu trú hợp lệ trong thời gian chờ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Thời hạn"}
        ));
        list.add(new FaqDefinition(
                "KTX_DANGKY_GIAHAN",
                "Làm gì khi đã đóng phí gia hạn nhưng trạng thái nội trú chưa đổi?",
                "Đối chiếu giao dịch với hồ sơ gia hạn trước khi yêu cầu cập nhật.",
                """
                <p>Kiểm tra biên nhận, mã phòng và kỳ lưu trú trên giao dịch. Mở hồ sơ để xem còn bước xác nhận hoặc tài liệu nào chưa hoàn tất.</p><p>Nếu mọi thông tin đúng nhưng trạng thái chưa đổi, gửi mã yêu cầu và chứng từ cho ban quản lý. Không chuyển tiền lần nữa khi giao dịch đầu tiên còn đang được xác minh.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Đối soát học phí"}
        ));
        list.add(new FaqDefinition(
                "KTX_DICHVU_SUA",
                "Cách báo hỏng tài sản trong phòng ký túc xá?",
                "Thông tin giúp đội kỹ thuật xác định vị trí và chuẩn bị sửa chữa.",
                """
                <p>Ghi khu, tầng, phòng, tên thiết bị và hiện tượng cụ thể. Nếu an toàn, chụp ảnh vị trí hỏng; không tháo thiết bị điện hoặc tự sửa phần có nguy cơ.</p><p>Nêu khung thời gian có thể tiếp cận phòng và lưu mã yêu cầu để bổ sung khi kỹ thuật viên hỏi thêm.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Sửa chữa trang thiết bị"}
        ));
        list.add(new FaqDefinition(
                "KTX_DICHVU_SUA",
                "Làm gì khi thiết bị hỏng lần thứ hai sau khi đã sửa?",
                "Liên kết báo hỏng mới với lịch sử sửa chữa trước đó.",
                """
                <p>Mở yêu cầu cũ và ghi thời điểm sửa lần trước, biểu hiện tái diễn cùng mã tài sản nếu có. Đính kèm biên bản bàn giao hoặc mã phiếu cũ.</p><p>Không tự tháo hoặc thay linh kiện trước khi được kiểm tra; yêu cầu đánh giá nguyên nhân tái diễn để giữ lịch sử xử lý thống nhất.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sửa chữa trang thiết bị", "Ký túc xá"}
        ));
        list.add(new FaqDefinition(
                "KTX_DICHVU_SUA",
                "Làm sao để hẹn lịch cho kỹ thuật viên kiểm tra phòng?",
                "Cách sắp xếp quyền tiếp cận và xác nhận khi công việc hoàn thành.",
                """
                <p>Kiểm tra phản hồi để biết khung thời gian dự kiến. Nếu không thể có mặt, báo trước và hỏi phương án hẹn lại hoặc người được phép hỗ trợ mở phòng.</p><p>Sau khi sửa, kiểm tra thiết bị và phản hồi trong phiếu. Không xác nhận hoàn thành nếu sự cố vẫn tồn tại hoặc hạng mục chưa được giải thích.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Dịch vụ sinh viên"}
        ));
        list.add(new FaqDefinition(
                "KTX_DICHVU_SUA",
                "Ai chịu trách nhiệm khi tài sản trong phòng bị hư?",
                "Cách phân biệt hao mòn, lỗi kỹ thuật và hư hỏng cần xác minh trách nhiệm.",
                """
                <p>Ghi hiện trạng, thời điểm phát hiện và dấu hiệu bất thường trước đó. Báo ban quản lý để kiểm tra nguyên nhân, tránh tự nhận lỗi hoặc quy kết cho người khác khi chưa có kết luận.</p><p>Chi phí sửa chữa phụ thuộc nội quy và kết quả kiểm tra; hỏi rõ căn cứ tính phí và biên bản bàn giao nếu phát sinh khoản thu.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Sửa chữa trang thiết bị"}
        ));
        list.add(new FaqDefinition(
                "KTX_DICHVU_DIENNUOC",
                "Làm gì khi mất điện trong phòng nhưng khu vực chung vẫn có điện?",
                "Báo sự cố tại phòng và tuân thủ các bước an toàn ban đầu.",
                """
                <p>Kiểm tra thông báo của khu nhà và các thiết bị đang sử dụng; không tự mở tủ điện hoặc chạm dây hở. Nếu có mùi khét hay nguy cơ mất an toàn, rời khu vực và báo người quản lý trực.</p><p>Khi gửi phiếu, ghi phòng, thời điểm mất điện và thiết bị bị ảnh hưởng. Chỉ sử dụng lại sau khi tình trạng an toàn được xác nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điện nước", "Ký túc xá"}
        ));
        list.add(new FaqDefinition(
                "KTX_DICHVU_DIENNUOC",
                "Làm gì khi nước sinh hoạt yếu hoặc mất nước theo giờ?",
                "Ghi thông tin để phân biệt sự cố trong phòng và toàn khu.",
                """
                <p>Kiểm tra các vòi trong phòng để xác định lỗi xảy ra ở một vị trí hay tất cả. Hỏi thông báo bảo trì nếu có; không tháo đường ống chung hoặc van kỹ thuật.</p><p>Ghi giờ bắt đầu, vị trí và việc sự cố có lặp lại theo khung thời gian hay không để kỹ thuật viên tìm nguyên nhân.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điện nước", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "KTX_DICHVU_DIENNUOC",
                "Làm gì khi tiền điện nước tăng bất thường so với kỳ trước?",
                "Đề nghị kiểm tra chỉ số và giải thích khoản thu dựa trên dữ liệu.",
                """
                <p>So sánh kỳ cập nhật, chỉ số đầu/cuối kỳ nếu có và số ngày tính phí. Kiểm tra thay đổi số người ở, thiết bị sử dụng hoặc thời gian lưu trú.</p><p>Nếu chênh lệch chưa rõ, gửi mã phòng, kỳ thu và thông tin công tơ nếu được phép cập nhật. Đề nghị ban quản lý kiểm tra trước khi kết luận nguyên nhân.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điện nước", "Chứng từ"}
        ));
        list.add(new FaqDefinition(
                "KTX_DICHVU_DIENNUOC",
                "Làm gì khi rò nước gần ổ điện hoặc thiết bị điện?",
                "Báo sự cố có nguy cơ an toàn và giữ người ở xa khu vực.",
                """
                <p>Không chạm vào nước gần thiết bị điện, không cắm/rút phích trong vùng ẩm ướt và ngăn người khác đi vào. Báo trực tiếp cho quản lý ký túc xá hoặc bộ phận khẩn cấp tại chỗ.</p><p>Sau khi đã cảnh báo và bảo đảm an toàn, gửi vị trí cùng mô tả nguồn rò rỉ; không trì hoãn việc báo cáo để chụp ảnh.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điện nước", "Dịch vụ sinh viên"}
        ));
        list.add(new FaqDefinition(
                "CNTT_TAIKHOAN",
                "Các bước kích hoạt tài khoản sinh viên lần đầu gồm những gì?",
                "Các bước nhận thông tin đăng nhập, đổi mật khẩu ban đầu và kiểm tra khả năng truy cập các dịch vụ trực tuyến.",
                """
                <p>Sinh viên mở thông báo cấp tài khoản do nhà trường gửi, so sánh mã số sinh viên và địa chỉ email được cấp. Tại trang đăng nhập, nhập đúng tên tài khoản và mật khẩu khởi tạo; nếu hệ thống yêu cầu đổi mật khẩu thì đặt mật khẩu mới theo các điều kiện hiển thị.</p><p>Sau khi đăng nhập thành công, kiểm tra thông tin hồ sơ và thử mở các dịch vụ được phân quyền. Không gửi mật khẩu cho bạn bè hoặc nhân viên hỗ trợ. Nếu thông tin cấp tài khoản sai, tạo yêu cầu đến bộ phận CNTT và đính kèm ảnh thông báo lỗi đã che thông tin bí mật.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tài khoản", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "CNTT_TAIKHOAN",
                "Cách đổi mật khẩu khi vẫn đăng nhập được?",
                "Cách đổi mật khẩu chủ động, kiểm tra mật khẩu mới và cập nhật các thiết bị đang lưu thông tin đăng nhập.",
                """
                <p>Mở mục Hồ sơ hoặc Bảo mật tài khoản, chọn Đổi mật khẩu, nhập mật khẩu hiện tại rồi nhập mật khẩu mới theo yêu cầu về độ dài và độ phức tạp. Nhập lại mật khẩu mới để xác nhận và lưu thay đổi.</p><p>Sau khi đổi thành công, đăng nhập lại trên các thiết bị đang sử dụng. Nếu một ứng dụng tiếp tục báo sai mật khẩu, hãy đăng xuất rồi đăng nhập lại thay vì thử liên tục nhiều lần. Không dùng chung mật khẩu với email cá nhân hoặc các dịch vụ khác.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tài khoản", "Bảo mật"}
        ));
        list.add(new FaqDefinition(
                "CNTT_TAIKHOAN",
                "Làm gì khi hệ thống thông báo tài khoản bị khóa?",
                "Các bước xác định nguyên nhân khóa tài khoản và gửi yêu cầu mở khóa đúng bộ phận tiếp nhận.",
                """
                <p>Đọc đầy đủ thông báo trên màn hình để phân biệt khóa do nhập sai nhiều lần, tài khoản hết hiệu lực hay vấn đề về trạng thái sinh viên. Dừng việc thử mật khẩu liên tục vì thao tác đó có thể kéo dài thời gian khóa.</p><p>Gửi yêu cầu hỗ trợ kèm mã số sinh viên, thời điểm xảy ra lỗi và ảnh chụp thông báo. Không đính kèm mật khẩu hoặc mã xác thực. Bộ phận CNTT kiểm tra nhật ký và phối hợp đơn vị quản lý tài khoản để xác minh trước khi mở khóa.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tài khoản", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "CNTT_TAIKHOAN",
                "Cách kiểm tra thông tin liên hệ gắn với tài khoản?",
                "Cách rà soát email, số điện thoại và thông tin khôi phục để tránh mất quyền truy cập.",
                """
                <p>Trong mục Hồ sơ cá nhân, kiểm tra địa chỉ email, số điện thoại và phương thức khôi phục đang được hệ thống cập nhật. Nếu thông tin sai hoặc đã ngừng sử dụng, thực hiện quy trình cập nhật theo yêu cầu xác minh danh tính của trường.</p><p>Sau khi cập nhật, xác nhận email hoặc số điện thoại nếu hệ thống gửi mã kiểm tra. Không dùng địa chỉ của người khác làm thông tin khôi phục. Nếu không thể tự chỉnh sửa, gửi yêu cầu cho quản trị tài khoản kèm giấy tờ xác minh theo hướng dẫn chính thức.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tài khoản", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CNTT_PORTAL",
                "Làm gì khi trang cổng thông tin sinh viên không tải hết nội dung?",
                "Cách kiểm tra kết nối, phiên đăng nhập và dữ liệu trình duyệt khi cổng thông tin chỉ hiển thị một phần.",
                """
                <p>Thử tải lại trang, kiểm tra kết nối mạng và đăng nhập lại. Nếu lỗi chỉ xuất hiện ở một trình duyệt, mở cửa sổ riêng tư hoặc thử trình duyệt được nhà trường hỗ trợ để xác định vấn đề do bộ nhớ đệm hay tiện ích mở rộng.</p><p>Nếu vẫn lỗi, ghi lại địa chỉ trang, thời điểm, thao tác ngay trước khi lỗi xuất hiện và gửi ảnh chụp màn hình. Không gửi cookie, mã phiên hoặc tệp chứa dữ liệu xác thực. Đơn vị CNTT sẽ dựa trên thông tin này để phân biệt lỗi trình duyệt với sự cố dịch vụ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Cổng thông tin", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "CNTT_PORTAL",
                "Cách tìm đúng chức năng trên cổng thông tin sinh viên?",
                "Cách sử dụng menu, ô tìm kiếm và liên kết dịch vụ theo vai trò tài khoản.",
                """
                <p>Sau khi đăng nhập, chọn nhóm chức năng phù hợp như học vụ, tài chính, công tác sinh viên hoặc hỗ trợ kỹ thuật. Nếu không thấy chức năng, kiểm tra xem tài khoản có đúng vai trò và trạng thái hoạt động hay không.</p><p>Không truy cập các đường dẫn được chia sẻ không rõ nguồn gốc. Khi gửi yêu cầu hỗ trợ, ghi tên chức năng cần dùng và mục menu mà bạn đã mở để nhân viên hướng dẫn chính xác đường dẫn thuộc hệ thống chính thức.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Cổng thông tin", "Hướng dẫn học vụ"}
        ));
        list.add(new FaqDefinition(
                "CNTT_PORTAL",
                "Làm gì khi phiên đăng nhập hết hạn giữa chừng?",
                "Nguyên nhân phổ biến khiến hệ thống yêu cầu đăng nhập lại và cách bảo toàn nội dung đang nhập.",
                """
                <p>Phiên đăng nhập có thể hết hạn sau một khoảng thời gian không hoạt động nhằm bảo vệ tài khoản. Nếu biểu mẫu đang mở, sao chép phần nội dung đã nhập vào nơi an toàn trước khi đăng nhập lại, nếu thao tác đó không làm lộ thông tin cá nhân.</p><p>Đăng nhập qua trang chính thức và gửi lại biểu mẫu sau khi xác nhận dữ liệu vẫn chính xác. Tránh mở nhiều cửa sổ đăng nhập đồng thời nếu hệ thống không hỗ trợ. Nếu phiên liên tục hết hạn khi đang thao tác, ghi thời điểm và chức năng đang dùng để báo CNTT.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Cổng thông tin", "Tài khoản"}
        ));
        list.add(new FaqDefinition(
                "CNTT_PORTAL",
                "Cách báo lỗi liên kết từ cổng thông tin sinh viên sang dịch vụ khác?",
                "Thông tin cần cung cấp khi một liên kết từ cổng sinh viên mở sai trang hoặc trả về lỗi.",
                """
                <p>Kiểm tra lại tên dịch vụ trên menu và thử mở từ trang chủ của cổng thông tin sinh viên thay vì dùng dấu trang cũ. Nếu liên kết chuyển đến trang trắng, báo lỗi quyền truy cập hoặc quay vòng liên tục, ghi lại đường dẫn đã ẩn các tham số có thể chứa mã phiên.</p><p>Trong nội dung yêu cầu, nêu trình duyệt, thời gian phát sinh, tên chức năng và kết quả mong đợi. Không gửi đường dẫn chứa token xác thực. Bộ phận CNTT sẽ kiểm tra cấu hình điều hướng và quyền truy cập giữa các dịch vụ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Cổng thông tin", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "CNTT_BAOMAT",
                "Cách nhận biết email giả mạo yêu cầu cung cấp tài khoản?",
                "Dấu hiệu nhận biết thư lừa đảo giả danh nhà trường và quy trình báo cáo an toàn.",
                """
                <p>Thận trọng với thư yêu cầu gửi mật khẩu, mã OTP, bấm vào đường dẫn lạ hoặc gửi thông tin gấp để tránh bị khóa tài khoản. Kiểm tra địa chỉ người gửi, tên miền, nội dung bất thường và đường dẫn trước khi thao tác.</p><p>Nhà trường không cần sinh viên gửi mật khẩu qua email. Nếu nhận thư đáng ngờ, không trả lời hoặc tải tệp đính kèm; chuyển thông tin thư đến bộ phận CNTT theo kênh chính thức. Nếu đã nhập thông tin, đổi mật khẩu và báo sự cố ngay.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo mật", "Tài khoản"}
        ));
        list.add(new FaqDefinition(
                "CNTT_BAOMAT",
                "Làm gì khi nghi ngờ tài khoản bị truy cập trái phép?",
                "Cách bảo vệ tài khoản, kiểm tra hoạt động bất thường và báo sự cố bảo mật.",
                """
                <p>Nếu phát hiện thông báo đăng nhập lạ hoặc thay đổi hồ sơ không do mình thực hiện, hãy đổi mật khẩu từ thiết bị đáng tin cậy, đăng xuất các phiên khác nếu hệ thống có chức năng này và kiểm tra email khôi phục.</p><p>Báo bộ phận CNTT thời điểm bất thường, thiết bị đang dùng và thông báo nhận được. Không tự xóa email cảnh báo vì đây có thể là bằng chứng phục vụ kiểm tra. Nếu cùng mật khẩu được sử dụng ở dịch vụ khác, cần thay đổi mật khẩu ở các dịch vụ đó.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo mật", "Tài khoản"}
        ));
        list.add(new FaqDefinition(
                "CNTT_BAOMAT",
                "Cần lưu ý gì khi sử dụng mã xác thực một lần (OTP)?",
                "Cách nhận, nhập và bảo vệ mã OTP trong quá trình xác minh danh tính.",
                """
                <p>Chỉ nhập OTP trên màn hình xác minh mà bạn chủ động mở từ hệ thống chính thức. Kiểm tra mục đích của yêu cầu trước khi nhập mã, đặc biệt khi mã được gửi cùng lúc có thông báo đăng nhập hoặc đổi mật khẩu.</p><p>Không đọc, chụp gửi hoặc chuyển tiếp OTP cho người khác, kể cả người tự xưng là nhân viên hỗ trợ. Nếu liên tục nhận OTP dù không thao tác, không xác nhận yêu cầu và gửi báo cáo bảo mật để kiểm tra hoạt động trên tài khoản.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo mật", "Tài khoản"}
        ));
        list.add(new FaqDefinition(
                "CNTT_BAOMAT",
                "Cách xử lý khi thiết bị dùng chung còn lưu phiên đăng nhập?",
                "Biện pháp bảo vệ thông tin cá nhân khi sử dụng máy tính tại thư viện hoặc phòng thực hành.",
                """
                <p>Sau khi sử dụng thiết bị dùng chung, đăng xuất khỏi cổng thông tin sinh viên và các dịch vụ liên quan, đóng cửa sổ trình duyệt và không chọn lưu mật khẩu. Nếu trình duyệt hỏi có lưu thông tin đăng nhập hay không, chọn không lưu.</p><p>Nếu quên đăng xuất, dùng thiết bị cá nhân để đổi mật khẩu hoặc kết thúc phiên từ phần bảo mật nếu hệ thống hỗ trợ. Thông báo CNTT khi nghi ngờ người khác đã mở được tài khoản. Không lưu tệp bảng điểm, hồ sơ hoặc minh chứng trong thư mục công cộng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo mật", "Tài khoản"}
        ));
        list.add(new FaqDefinition(
                "CNTT_TAIKHOAN_KHOIPHUC",
                "Điều kiện để sử dụng chức năng khôi phục mật khẩu là gì?",
                "Những thông tin cần có để xác minh chủ tài khoản trước khi đặt lại mật khẩu.",
                """
                <p>Chức năng khôi phục thường yêu cầu tên tài khoản cùng email hoặc số điện thoại đã đăng ký và còn sử dụng được. Nhập thông tin trên đúng trang chính thức, sau đó làm theo hướng dẫn xác minh hiển thị.</p><p>Nếu thông tin liên hệ cũ không còn truy cập được, không thử dùng địa chỉ của người khác. Gửi yêu cầu xác minh danh tính đến bộ phận phụ trách tài khoản. Kết quả khôi phục phụ thuộc vào việc so sánh hồ sơ và quy định bảo mật của trường.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Khôi phục tài khoản", "Tài khoản"}
        ));
        list.add(new FaqDefinition(
                "CNTT_TAIKHOAN_KHOIPHUC",
                "Làm gì khi email khôi phục không nhận được mã xác minh?",
                "Các nguyên nhân thường gặp và cách xem lại trước khi tạo yêu cầu hỗ trợ.",
                """
                <p>Kiểm tra chính xác địa chỉ email đã nhập, thư mục spam, quảng cáo và bộ lọc thư. Chờ trong khoảng thời gian được hệ thống thông báo trước khi yêu cầu gửi lại mã; không gửi quá nhiều yêu cầu liên tiếp.</p><p>Nếu mã vẫn không đến, xác nhận hộp thư còn hoạt động và dung lượng chưa đầy. Khi báo CNTT, gửi thời điểm yêu cầu, tên tài khoản và địa chỉ email đã che bớt ký tự; tuyệt đối không gửi mật khẩu hoặc mã xác minh cũ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Khôi phục tài khoản", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "CNTT_TAIKHOAN_KHOIPHUC",
                "Cách đặt mật khẩu mới sau khi xác minh thành công?",
                "Trình tự hoàn tất khôi phục và các bước kiểm tra sau khi hệ thống chấp nhận mật khẩu mới.",
                """
                <p>Sử dụng liên kết hoặc màn hình đặt lại mật khẩu được cấp sau bước xác minh. Chọn mật khẩu mới chưa dùng cho dịch vụ khác, đáp ứng yêu cầu hiển thị và không chứa thông tin dễ đoán như ngày sinh.</p><p>Sau khi lưu, đăng nhập lại tại trang chính thức bằng mật khẩu mới. Nếu hệ thống thông báo liên kết hết hạn, quay lại quy trình khôi phục để tạo yêu cầu mới thay vì sử dụng liên kết không rõ nguồn. Không chia sẻ liên kết khôi phục cho bất kỳ ai.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Khôi phục tài khoản", "Bảo mật"}
        ));
        list.add(new FaqDefinition(
                "CNTT_TAIKHOAN_KHOIPHUC",
                "Khi nào cần yêu cầu quản trị viên khôi phục tài khoản thủ công?",
                "Các trường hợp không thể tự khôi phục và thông tin cần chuẩn bị để xác minh.",
                """
                <p>Cần nhờ quản trị viên khi email và số điện thoại khôi phục đều không còn sử dụng được, hồ sơ có sai lệch hoặc chức năng tự động báo lỗi sau nhiều lần thử đúng hướng dẫn.</p><p>Yêu cầu nên nêu mã số sinh viên, tình trạng truy cập và cách liên hệ hiện tại. Chỉ gửi giấy tờ xác minh thông qua kênh được trường chỉ định. Quản trị viên cần xác minh chủ tài khoản trước khi thay đổi thông tin bảo mật.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Khôi phục tài khoản", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CNTT_PORTAL_LOI",
                "Cách xử lý mã lỗi truy cập 403 trên cổng thông tin sinh viên?",
                "Ý nghĩa thường gặp của lỗi không đủ quyền và cách phân biệt lỗi phân quyền với sự cố đăng nhập.",
                """
                <p>Lỗi 403 thường cho biết yêu cầu đã đến máy chủ nhưng tài khoản không được phép thực hiện thao tác hoặc mở tài nguyên đó. Kiểm tra xem bạn đã đăng nhập đúng vai trò, chức năng có dành cho nhóm của mình và tài khoản có đang hoạt động hay không.</p><p>Không cố đổi đường dẫn để vượt qua giới hạn quyền truy cập. Gửi tên chức năng, thao tác vừa thực hiện và ảnh màn hình cho CNTT. Nếu chức năng thuộc đơn vị khác, yêu cầu cần được chuyển đúng bộ phận để kiểm tra quyền.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Cổng thông tin", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "CNTT_PORTAL_LOI",
                "Làm gì khi cổng thông tin sinh viên báo lỗi máy chủ hoặc dịch vụ tạm thời không khả dụng?",
                "Các bước thử lại an toàn và thông tin cần ghi nhận khi hệ thống gặp sự cố.",
                """
                <p>Khi trang báo máy chủ lỗi hoặc dịch vụ không khả dụng, lưu lại nội dung thông báo và thời điểm xảy ra. Thử lại sau một khoảng ngắn theo hướng dẫn của trường; không liên tục gửi cùng một biểu mẫu nếu chưa rõ yêu cầu trước đã được cập nhật hay chưa.</p><p>Nếu lỗi kéo dài, kiểm tra thông báo bảo trì chính thức rồi gửi báo cáo kèm trang bị ảnh hưởng, loại thao tác và tần suất xảy ra. Không tự ý tải lên lại nhiều lần các tệp hoặc thanh toán khi chưa xác nhận giao dịch trước đó.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Cổng thông tin", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "CNTT_PORTAL_LOI",
                "Làm gì khi trang hiển thị sai tiếng Việt hoặc ký tự bị lỗi?",
                "Cách xác định lỗi mã hóa, trình duyệt và nhập liệu khi nội dung tiếng Việt không hiển thị đúng.",
                """
                <p>Thử tải lại trang bằng trình duyệt cập nhật và kiểm tra xem lỗi xảy ra với mọi trang hay chỉ một biểu mẫu. Nếu nội dung nhập từ tệp bên ngoài bị lỗi, thử gõ lại một đoạn tiếng Việt ngắn để so sánh.</p><p>Gửi ảnh màn hình, tên trình duyệt và đường dẫn chức năng bị lỗi cho bộ phận CNTT. Không thay đổi cài đặt hệ thống hoặc cài tiện ích không rõ nguồn. Nếu lỗi xuất hiện trong dữ liệu hồ sơ, đề nghị đơn vị quản lý kiểm tra bản ghi gốc trước khi chỉnh sửa.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Cổng thông tin", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "CNTT_PORTAL_LOI",
                "Cách báo lỗi khi biểu mẫu trực tuyến không gửi được?",
                "Cách kiểm tra trường bắt buộc, dung lượng tệp và trạng thái kết nối trước khi gửi hỗ trợ.",
                """
                <p>Kiểm tra các trường bắt buộc, định dạng dữ liệu, giới hạn dung lượng tệp và thông báo ngay cạnh trường nhập liệu. Đảm bảo mạng ổn định trước khi bấm gửi. Nếu nút gửi không phản hồi, tránh bấm lặp nhiều lần khi chưa biết yêu cầu đã được cập nhật.</p><p>Ghi lại tên biểu mẫu và thông báo lỗi chính xác, sau đó gửi báo cáo cho bộ phận phụ trách. Không đính kèm giấy tờ gốc chứa dữ liệu nhạy cảm vào kênh không chính thức; hãy làm theo hướng dẫn tải hồ sơ bảo mật của hệ thống.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Cổng thông tin", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "TV_MUONTRA",
                "Điều kiện mượn giáo trình bản in tại thư viện là gì?",
                "Giới thiệu cách kiểm tra quyền mượn, thời hạn và hạn mức tài liệu theo tài khoản bạn đọc.",
                """
                <p>Đăng nhập tài khoản thư viện bằng thông tin do trường cấp, tìm giáo trình theo nhan đề hoặc mã tài liệu và kiểm tra trạng thái bản in. Trang chi tiết sẽ cho biết tài liệu có thể mượn, chỉ đọc tại chỗ hay đang được bạn đọc khác sử dụng.</p><p>Trước khi đến quầy, chuẩn bị thẻ sinh viên còn hiệu lực và kiểm tra hạn mức mượn hiển thị trên tài khoản. Nếu hệ thống cập nhật thẻ hết hạn hoặc còn tài liệu quá hạn, xử lý trạng thái đó theo hướng dẫn thư viện trước khi yêu cầu mượn thêm.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thư viện", "Mượn trả"}
        ));
        list.add(new FaqDefinition(
                "TV_MUONTRA",
                "Cách gia hạn tài liệu đang mượn khi chưa đến hạn trả?",
                "Các điều kiện cần kiểm tra trước khi gửi yêu cầu gia hạn sách hoặc giáo trình.",
                """
                <p>Mở danh sách tài liệu đang mượn, chọn tài liệu muốn gia hạn và sử dụng chức năng gia hạn nếu trạng thái cho phép. Kiểm tra ngày trả mới sau khi thao tác hoàn tất, không dựa vào ngày dự kiến trước khi hệ thống xác nhận.</p><p>Gia hạn có thể không được chấp nhận nếu tài liệu đã có người đặt trước, tài khoản vượt hạn mức hoặc chính sách tài liệu không cho phép gia hạn. Khi nút gia hạn không xuất hiện, liên hệ thư viện và gửi mã tài liệu để được giải thích rõ lý do.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Mượn trả", "Gia hạn tài liệu"}
        ));
        list.add(new FaqDefinition(
                "TV_MUONTRA",
                "Làm gì khi trả sách nhưng tài khoản vẫn báo đang mượn?",
                "Các bước đối chiếu biên nhận trả tài liệu và yêu cầu cập nhật trạng thái trên hệ thống.",
                """
                <p>Giữ biên nhận hoặc thông tin xác nhận trả sách và kiểm tra thời điểm giao dịch. Có thể cần một khoảng thời gian để trạng thái trên tài khoản được cập nhật sau khi quầy tiếp nhận tài liệu.</p><p>Nếu sau thời gian xử lý được thư viện thông báo mà tài liệu vẫn hiện là đang mượn, gửi mã bạn đọc, mã tài liệu, thời điểm trả và bằng chứng giao nhận tới quầy hỗ trợ. Không tiếp tục tạo giao dịch giả định hoặc thanh toán khoản phạt khi chưa có kết quả so sánh.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Mượn trả", "Tài khoản"}
        ));
        list.add(new FaqDefinition(
                "TV_MUONTRA",
                "Các bước báo mất hoặc làm hỏng tài liệu thư viện gồm những gì?",
                "Thông tin cần khai báo và cách phối hợp với thư viện để xử lý trách nhiệm tài liệu.",
                """
                <p>Thông báo sớm cho bộ phận thư viện về tài liệu bị mất hoặc hư hỏng, gửi mã tài liệu, ngày mượn và tình trạng thực tế. Không tự thay thế tài liệu bằng một ấn bản khác trước khi thư viện xác nhận cách xử lý.</p><p>Nhân viên sẽ hướng dẫn quy trình bồi hoàn hoặc khắc phục theo quy định đang áp dụng và cập nhật kết quả vào tài khoản bạn đọc. Lưu biên nhận hoàn tất nghĩa vụ để sử dụng khi cần so sánh hạn mức mượn về sau.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Mượn trả", "Thư viện"}
        ));
        list.add(new FaqDefinition(
                "TV_TRACUU",
                "Cách tìm sách bằng nhan đề và tên tác giả?",
                "Cách kết hợp từ khóa để thu hẹp kết quả tra cứu tài liệu trong danh mục thư viện.",
                """
                <p>Nhập nhan đề chính hoặc một cụm từ đặc trưng vào ô tìm kiếm, sau đó lọc kết quả theo loại tài liệu hoặc tên tác giả nếu hệ thống có hỗ trợ. Nếu không tìm thấy, thử bỏ dấu câu hoặc tìm bằng họ tác giả và một phần nhan đề.</p><p>Mở bản ghi phù hợp để xem ký hiệu xếp giá, vị trí lưu trữ và tình trạng mượn. Kết quả tìm kiếm chỉ xác định bản ghi trong danh mục; hãy kiểm tra trạng thái bản in cụ thể trước khi đến khu vực kệ sách.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thư viện", "Tra cứu tài liệu"}
        ));
        list.add(new FaqDefinition(
                "TV_TRACUU",
                "Phân biệt tài liệu bản in, tài liệu số và tài liệu chỉ đọc tại chỗ?",
                "Cách đọc thông tin hình thức lưu trữ và điều kiện khai thác trên trang kết quả tra cứu.",
                """
                <p>Bản ghi có thể đại diện cho sách bản in, luận văn điện tử, bài báo hoặc tài liệu chỉ được đọc tại chỗ. Xem trường loại tài liệu, vị trí lưu trữ và nút truy cập để biết hình thức sử dụng được cấp phép.</p><p>Nếu có liên kết tài liệu số nhưng truy cập bị từ chối, kiểm tra việc đăng nhập bằng tài khoản trường và điều kiện truy cập từ xa. Không chia sẻ bản tải xuống ra ngoài phạm vi được phép; khi thông tin loại tài liệu chưa rõ, yêu cầu thư viện xác nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tra cứu tài liệu", "Thư viện"}
        ));
        list.add(new FaqDefinition(
                "TV_TRACUU",
                "Cách tìm tài liệu theo mã phân loại hoặc ký hiệu xếp giá?",
                "Cách sử dụng ký hiệu trong bản ghi thư viện để định vị đúng tài liệu trên kệ.",
                """
                <p>Mở thông tin chi tiết của bản ghi và ghi lại đầy đủ ký hiệu xếp giá, bao gồm các chữ cái, số và phần bổ sung nếu có. Dùng ký hiệu này để so sánh bảng chỉ dẫn khu vực, tầng hoặc dãy kệ tại thư viện.</p><p>Nhiều bản cùng nhan đề có thể nằm ở các vị trí khác nhau hoặc thuộc ấn bản khác nhau. Hãy kiểm tra năm xuất bản và số tập trước khi lấy tài liệu. Nếu không tìm thấy đúng ký hiệu, nhờ nhân viên tra cứu kho thay vì lấy sách có tên gần giống.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tra cứu tài liệu", "Thư viện"}
        ));
        list.add(new FaqDefinition(
                "TV_TRACUU",
                "Cách báo tài liệu có thông tin thư mục sai?",
                "Những trường thông tin cần đối chiếu khi tên sách, tác giả hoặc năm xuất bản trên hệ thống bị sai.",
                """
                <p>So sánh bản ghi với trang nhan đề và trang thông tin xuất bản của tài liệu thực tế. Ghi lại trường dữ liệu sai, giá trị đang hiển thị và thông tin đúng có căn cứ từ ấn phẩm.</p><p>Gửi yêu cầu chỉnh sửa cho thư viện kèm mã tài liệu và ảnh minh họa cần thiết. Không tự sửa dữ liệu thư mục nếu tài khoản không được cấp quyền biên mục. Nhân viên phụ trách sẽ kiểm tra bản gốc trước khi cập nhật danh mục chung.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tra cứu tài liệu", "Thư viện"}
        ));
        list.add(new FaqDefinition(
                "TV_TAIKHOAN",
                "Cách kích hoạt tài khoản bạn đọc thư viện?",
                "Các bước đăng nhập lần đầu và kiểm tra hồ sơ bạn đọc sau khi tài khoản được cấp.",
                """
                <p>Truy cập cổng thư viện chính thức, đăng nhập bằng thông tin tài khoản do trường gửi và kiểm tra tên, mã số sinh viên cùng trạng thái bạn đọc. Nếu lần đầu dùng hệ thống yêu cầu cập nhật mật khẩu hoặc chấp nhận điều khoản sử dụng, hoàn tất các bước đó.</p><p>Nếu tài khoản chưa có quyền mượn tài liệu, kiểm tra thẻ sinh viên và trạng thái học tập đã được đồng bộ hay chưa. Gửi mã số sinh viên tới quầy hỗ trợ khi dữ liệu không khớp; không đăng ký thêm tài khoản cá nhân để thay thế tài khoản trường.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thư viện", "Tài khoản"}
        ));
        list.add(new FaqDefinition(
                "TV_TAIKHOAN",
                "Cách cập nhật email nhận thông báo quá hạn và đặt trước tài liệu?",
                "Cách cập nhật email để nhận thông báo quá hạn và thông báo tài liệu đặt trước từ thư viện.",
                """
                <p>Mở hồ sơ bạn đọc, xem địa chỉ email nhận thông báo và cập nhật nếu hệ thống cho phép. Kiểm tra hộp thư đến, mục spam và quy tắc lọc thư để bảo đảm thông báo về hạn trả hoặc đặt trước không bị bỏ sót.</p><p>Nếu địa chỉ do trường quản lý không thể tự sửa, thực hiện thủ tục cập nhật thông tin liên hệ theo hướng dẫn thư viện. Việc đổi email không tự xóa các khoản quá hạn hoặc nghĩa vụ tài liệu đang cập nhật trên tài khoản.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thư viện", "Tài khoản"}
        ));
        list.add(new FaqDefinition(
                "TV_TAIKHOAN",
                "Làm gì khi tài khoản thư viện bị tạm ngừng quyền mượn?",
                "Các lý do có thể làm quyền mượn bị hạn chế và cách xác minh trạng thái thực tế.",
                """
                <p>Kiểm tra thông báo trên hồ sơ bạn đọc để xem có tài liệu quá hạn, khoản phí chưa xử lý, thông tin thẻ chưa đồng bộ hoặc giới hạn theo quy định hay không. Đọc kỹ ngày và mã tài liệu được nêu trong thông báo.</p><p>Nếu cho rằng hạn chế xuất hiện nhầm, liên hệ thư viện kèm thông tin tài khoản và bằng chứng trả tài liệu hoặc thanh toán. Bộ phận thư viện sẽ so sánh giao dịch và cập nhật quyền sử dụng sau khi xác định nguyên nhân, không cần tạo tài khoản mới.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thư viện", "Mượn trả"}
        ));
        list.add(new FaqDefinition(
                "TV_TAIKHOAN",
                "Cách xem lịch sử mượn và các khoản đang chờ xử lý?",
                "Cách kiểm tra lịch sử giao dịch để tự đối chiếu tài liệu, thời hạn và trạng thái nghĩa vụ.",
                """
                <p>Trong mục tài khoản, mở lịch sử mượn trả và danh sách nghĩa vụ đang chờ xử lý. So sánh mã tài liệu, ngày mượn, ngày trả dự kiến, trạng thái gia hạn và các ghi chú của thư viện.</p><p>Nếu một giao dịch không khớp với thực tế, lưu mã giao dịch và gửi đề nghị so sánh. Không chỉ dựa vào email nhắc hạn vì bản ghi tài khoản là nơi cần kiểm tra trạng thái hiện hành; khi có tranh chấp, biên nhận và lịch sử giao dịch sẽ giúp làm rõ vấn đề.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thư viện", "Tài khoản"}
        ));
        list.add(new FaqDefinition(
                "TV_MUONTRA_GIAHAN",
                "Khi nào có thể gửi yêu cầu gia hạn sách?",
                "Cách phân biệt thời hạn cho phép gia hạn với ngày hết hạn trả đã ghi trên tài khoản.",
                """
                <p>Kiểm tra ngày trả hiện tại và quy tắc gia hạn áp dụng cho loại tài liệu trước khi gửi yêu cầu. Nên thực hiện khi tài liệu vẫn đang trong hạn và hệ thống đã cho phép thao tác, không chờ đến sau hạn mới xử lý.</p><p>Nếu hệ thống khóa nút gia hạn, xem có yêu cầu đặt trước từ bạn đọc khác hoặc giới hạn số lần gia hạn hay không. Trạng thái được hệ thống xác nhận mới là căn cứ sử dụng; ghi chú ngày trả mới sau khi thao tác hoàn tất.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Gia hạn tài liệu", "Mượn trả"}
        ));
        list.add(new FaqDefinition(
                "TV_MUONTRA_GIAHAN",
                "Vì sao yêu cầu gia hạn bị từ chối dù sách chưa đến hạn trả?",
                "Các nguyên nhân liên quan đến lượt đặt trước, loại tài liệu và giới hạn tài khoản.",
                """
                <p>Tài liệu chưa quá hạn vẫn có thể không được gia hạn nếu có người khác đặt trước, ấn phẩm thuộc nhóm không cho gia hạn hoặc tài khoản đã sử dụng hết số lần gia hạn được phép. Kiểm tra thông báo hệ thống để xác định đúng nguyên nhân.</p><p>Nếu thông báo không giải thích rõ, gửi mã tài liệu và ảnh trạng thái cho thư viện. Hãy chuẩn bị trả tài liệu đúng hạn trong thời gian chờ phản hồi để tránh phát sinh quá hạn do giả định rằng yêu cầu đã được chấp nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Gia hạn tài liệu", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "TV_MUONTRA_GIAHAN",
                "Cách xác nhận ngày trả mới sau khi gia hạn thành công?",
                "Những vị trí cần kiểm tra để chắc chắn hệ thống đã cập nhật thời hạn mượn.",
                """
                <p>Sau khi nhận thông báo gia hạn thành công, quay lại danh sách đang mượn và kiểm tra ngày trả mới bên cạnh đúng mã tài liệu. Lưu email hoặc thông báo xác nhận nếu thư viện gửi.</p><p>Nếu trang vẫn hiển thị ngày cũ, tải lại dữ liệu hoặc đăng xuất rồi đăng nhập lại. Không coi thao tác bấm nút là bằng chứng hoàn tất nếu hệ thống chưa xác nhận. Liên hệ quầy thư viện khi trạng thái giữa thông báo và hồ sơ tài khoản khác nhau.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Gia hạn tài liệu", "Tài khoản"}
        ));
        list.add(new FaqDefinition(
                "TV_MUONTRA_GIAHAN",
                "Làm gì khi sách đã được gia hạn nhưng email vẫn nhắc hạn cũ?",
                "Cách kiểm tra dữ liệu hiện hành và phân biệt email nhắc hạn gửi trễ với giao dịch chưa cập nhật.",
                """
                <p>So sánh ngày trả trên tài khoản thư viện trước, sau đó so với thời điểm của email nhắc hạn. Một email có thể đã được tạo trước khi gia hạn hoàn tất và không phản ánh trạng thái mới nhất.</p><p>Nếu tài khoản cũng hiển thị ngày cũ, gửi mã giao dịch gia hạn và thời gian thực hiện tới thư viện để kiểm tra. Giữ xác nhận giao dịch trong khi chờ phản hồi và không bỏ qua hạn trả nếu chưa có bằng chứng hệ thống chấp nhận gia hạn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Gia hạn tài liệu", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "TV_TRACUU_TAILIEU",
                "Cách tìm luận văn và đồ án theo ngành hoặc chuyên ngành?",
                "Cách lọc tài liệu học thuật để tìm các công trình phù hợp với lĩnh vực nghiên cứu.",
                """
                <p>Trong danh mục thư viện, chọn loại tài liệu luận văn hoặc đồ án nếu bộ lọc này có sẵn. Kết hợp tên ngành, từ khóa đề tài, tên tác giả và năm hoàn thành để thu hẹp kết quả.</p><p>Đọc thông tin tóm tắt và điều kiện truy cập trước khi mở toàn văn. Một số tài liệu chỉ gửi thông tin thư mục hoặc chỉ cho phép xem tại chỗ theo quy định của trường. Khi kết quả quá rộng, thử dùng cụm từ chuyên môn cụ thể trong tên đề tài.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tra cứu tài liệu", "Thư viện"}
        ));
        list.add(new FaqDefinition(
                "TV_TRACUU_TAILIEU",
                "Cách tìm bài báo khoa học theo tên tạp chí hoặc DOI?",
                "Cách tra cứu bài báo bằng dữ liệu thư mục chuẩn khi tìm kiếm theo tiêu đề chưa hiệu quả.",
                """
                <p>Nếu đã có DOI, nhập mã định danh chính xác vào ô tìm kiếm hoặc dùng trường DOI nếu danh mục hỗ trợ. Nếu tìm theo tên tạp chí, kết hợp năm xuất bản, tên tác giả và một phần tiêu đề để giảm kết quả trùng.</p><p>Kiểm tra thông tin tạp chí, tập, số, trang và DOI trong bản ghi trước khi trích dẫn. Nếu liên kết toàn văn không hoạt động, xác định xem thư viện có quyền truy cập bài báo đó hay không rồi gửi yêu cầu hỗ trợ tra cứu.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tra cứu tài liệu", "Nghiên cứu khoa học"}
        ));
        list.add(new FaqDefinition(
                "TV_TRACUU_TAILIEU",
                "Làm gì khi tìm thấy tên tài liệu nhưng không mở được toàn văn?",
                "Các bước xác định giới hạn bản quyền, quyền truy cập tài khoản và lỗi liên kết tệp.",
                """
                <p>Mở bản ghi thư mục và xác định tài liệu có gửi liên kết toàn văn hay chỉ có thông tin mô tả. Nếu có toàn văn, đăng nhập tài khoản trường và kiểm tra xem quyền truy cập chỉ áp dụng trong mạng trường hay được hỗ trợ từ xa.</p><p>Nếu liên kết trả lỗi, ghi lại mã tài liệu, tên tệp và thông báo xuất hiện rồi báo thư viện. Không tìm cách vượt cơ chế kiểm soát truy cập hoặc chia sẻ bản sao trái điều kiện sử dụng; thư viện sẽ kiểm tra liên kết và quyền khai thác hợp lệ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tra cứu tài liệu", "Thư viện"}
        ));
        list.add(new FaqDefinition(
                "TV_TRACUU_TAILIEU",
                "Cách đánh giá độ phù hợp của tài liệu trước khi sử dụng?",
                "Những tiêu chí cần xem gồm năm xuất bản, nguồn xuất bản, phạm vi nghiên cứu và loại tài liệu.",
                """
                <p>Đọc tóm tắt, từ khóa, mục lục và năm xuất bản để xác định tài liệu có trả lời câu hỏi nghiên cứu hay không. Với nội dung kỹ thuật thay đổi nhanh, chú ý phiên bản và ngày công bố; với lý thuyết nền tảng, kiểm tra mức độ uy tín và nguồn trích dẫn.</p><p>So sánh thông tin của tài liệu với đề cương môn học hoặc hướng dẫn của giảng viên. Không coi thứ tự kết quả tìm kiếm là đánh giá chất lượng học thuật. Ghi đầy đủ tác giả, nhan đề, năm và thông tin xuất bản để thuận tiện trích dẫn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tra cứu tài liệu", "Nghiên cứu khoa học"}
        ));
        list.add(new FaqDefinition(
                "QHDN_THUCTAP",
                "Điều kiện đăng ký thực tập doanh nghiệp là gì?",
                "Các thông tin cần kiểm tra về số tín chỉ tích lũy, học phần tiên quyết và thời gian thực tập.",
                """
                <p>Bạn kiểm tra kế hoạch đào tạo và đề cương thực tập để xác định số tín chỉ đã hoàn thành, môn học tiên quyết và yêu cầu của đơn vị tiếp nhận. Điều kiện có thể khác nhau theo ngành và chương trình, vì vậy cần dùng thông báo hiện hành của khoa làm căn cứ.</p><p>Trước khi đăng ký, chuẩn bị thông tin doanh nghiệp dự kiến, thời gian thực tập và người hướng dẫn. Nếu chưa đáp ứng điều kiện học vụ, trao đổi với khoa để biết học phần còn thiếu thay vì tự đăng ký một vị trí chưa đủ điều kiện.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thực tập", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QHDN_THUCTAP",
                "Các bước xin giấy giới thiệu thực tập gồm những gì?",
                "Các bước cung cấp thông tin nơi thực tập và nhận giấy giới thiệu theo mẫu của trường.",
                """
                <p>Chuẩn bị tên pháp lý của doanh nghiệp, địa chỉ, bộ phận tiếp nhận, người liên hệ và thời gian dự kiến thực tập. Điền yêu cầu cấp giấy giới thiệu trên kênh được khoa hoặc đơn vị quan hệ doanh nghiệp hướng dẫn.</p><p>Kiểm tra chính tả tên đơn vị và thông tin người nhận trước khi gửi vì sai thông tin có thể khiến doanh nghiệp không tiếp nhận hồ sơ. Theo dõi trạng thái yêu cầu; khi nhận giấy, kiểm tra chữ ký, dấu hoặc thông tin xác nhận theo quy định của trường.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thực tập", "Hồ sơ ứng tuyển"}
        ));
        list.add(new FaqDefinition(
                "QHDN_THUCTAP",
                "Cách xử lý khi doanh nghiệp thay đổi người hướng dẫn?",
                "Thủ tục cập nhật người cách tại doanh nghiệp để hồ sơ đánh giá thực tập không bị sai lệch.",
                """
                <p>Thông báo cho giảng viên hướng dẫn hoặc đầu mối phụ trách thực tập khi doanh nghiệp thay đổi người hướng dẫn. Cung cấp họ tên, chức vụ, email công việc và thời điểm thay đổi của người thay thế để đơn vị quản lý cập nhật.</p><p>Không tự sửa chữ ký hoặc nhận xét trên biểu mẫu đã xác nhận trước đó. Kiểm tra xem có cần cấp lại phiếu đánh giá hoặc điều chỉnh thông tin trên hồ sơ thực tập hay không, đồng thời lưu trao đổi xác nhận giữa sinh viên, doanh nghiệp và khoa.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thực tập", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QHDN_THUCTAP",
                "Nội dung cần có trong nhật ký thực tập?",
                "Cách ghi nhận công việc, kết quả học tập và xác nhận định kỳ trong thời gian thực tập.",
                """
                <p>Nhật ký cần ghi ngày hoặc khoảng thời gian, nhiệm vụ được giao, công cụ sử dụng, kết quả đạt được và vấn đề đã học được. Nội dung nên phản ánh công việc thực tế, có liên hệ với mục tiêu thực tập và tránh sao chép nguyên văn nhật ký của người khác.</p><p>Tuân thủ quy định bảo mật của doanh nghiệp; không đưa dữ liệu khách hàng, mã nguồn nội bộ hay tài liệu hạn chế vào báo cáo nếu chưa được cho phép. Thực hiện xác nhận của người hướng dẫn theo mốc thời gian do khoa quy định.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thực tập", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QHDN_TUYENDUNG",
                "Cách tìm thông tin tuyển dụng dành cho sinh viên HCMUTE?",
                "Các kênh tìm việc và tiêu chí kiểm tra thông tin tuyển dụng trước khi ứng tuyển.",
                """
                <p>Theo dõi thông báo tuyển dụng do nhà trường, khoa và đơn vị quan hệ doanh nghiệp công bố; đồng thời kiểm tra trang tuyển dụng chính thức của doanh nghiệp. Đọc kỹ vị trí, địa điểm, hình thức làm việc, yêu cầu kỹ năng và hạn nộp hồ sơ.</p><p>Không trả phí để được phỏng vấn hoặc nhận việc khi chưa xác minh tính hợp pháp của đơn vị tuyển dụng. Nếu tin đăng sử dụng tên trường nhưng có dấu hiệu bất thường, gửi đường dẫn và ảnh chụp cho bộ phận phụ trách để kiểm tra.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tuyển dụng", "Hồ sơ ứng tuyển"}
        ));
        list.add(new FaqDefinition(
                "QHDN_TUYENDUNG",
                "Cách đọc yêu cầu tuyển dụng vị trí thực tập sinh?",
                "Cách đối chiếu yêu cầu về chuyên môn, thời gian, tiếng Anh và sản phẩm cá nhân với hồ sơ hiện có.",
                """
                <p>Đọc phần nhiệm vụ trước để hiểu công việc chính, sau đó phân loại yêu cầu thành bắt buộc và ưu tiên. So sánh từng yêu cầu với môn học, dự án, công cụ đã sử dụng và thời gian có thể cam kết đi làm.</p><p>Nếu chưa đáp ứng một vài yêu cầu ưu tiên, vẫn có thể cân nhắc ứng tuyển khi điều kiện bắt buộc phù hợp. Không khai kỹ năng hoặc kinh nghiệm chưa có; thay vào đó nêu rõ dự án thực tế, vai trò đảm nhiệm và phần việc có thể trình bày khi phỏng vấn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tuyển dụng", "Thực tập"}
        ));
        list.add(new FaqDefinition(
                "QHDN_TUYENDUNG",
                "CV ứng tuyển vị trí đầu tiên cần có những nội dung nào?",
                "Gợi ý cấu trúc CV cho sinh viên chưa có kinh nghiệm làm việc chính thức.",
                """
                <p>CV nên nêu thông tin liên hệ cần thiết, định hướng vị trí, học vấn, kỹ năng có thể chứng minh, dự án học tập và hoạt động liên quan. Với mỗi dự án, mô tả mục tiêu, công nghệ, phần việc cá nhân và kết quả cụ thể thay vì chỉ liệt kê tên dự án.</p><p>Giữ bố cục dễ đọc, kiểm tra lỗi chính tả và dùng liên kết kho mã nguồn hoặc hồ sơ cá nhân còn truy cập được. Không đưa số giấy tờ tùy thân, mật khẩu hoặc thông tin riêng tư không cần thiết vào CV gửi rộng rãi.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ ứng tuyển", "Tuyển dụng"}
        ));
        list.add(new FaqDefinition(
                "QHDN_TUYENDUNG",
                "Cách chuẩn bị cho buổi phỏng vấn thực tập?",
                "Những việc nên chuẩn bị trước buổi phỏng vấn và cách trình bày dự án cá nhân trung thực.",
                """
                <p>Đọc mô tả vị trí, tìm hiểu sản phẩm của doanh nghiệp và chuẩn bị phần giới thiệu ngắn về bản thân. Với mỗi dự án, luyện giải thích vấn đề, lựa chọn kỹ thuật, phần việc do mình thực hiện và điều đã học được.</p><p>Chuẩn bị câu hỏi về nhiệm vụ, người hướng dẫn, thời gian làm việc và tiêu chí đánh giá thực tập. Nếu chưa biết câu trả lời kỹ thuật, trình bày cách tiếp cận thay vì đoán. Kiểm tra trước thời gian, địa điểm hoặc đường dẫn họp để tránh đến muộn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tuyển dụng", "Thực tập"}
        ));
        list.add(new FaqDefinition(
                "QHDN_CV",
                "Cách trình bày dự án học tập trong CV ngành công nghệ?",
                "Cách biến mô tả dự án thành thông tin có thể đánh giá về vai trò, kỹ thuật và kết quả.",
                """
                <p>Mỗi dự án nên có tên, khoảng thời gian, mục tiêu, công nghệ chính và vai trò cá nhân. Chọn một đến ba đóng góp cụ thể như thiết kế API, xây dựng giao diện, viết kiểm thử hoặc triển khai cơ sở dữ liệu.</p><p>Nếu dự án làm theo nhóm, phân biệt rõ phần mình phụ trách và phần nhóm cùng thực hiện. Chỉ nêu số liệu hiệu năng, lượng người dùng hoặc kết quả định lượng khi đã đo hoặc có bằng chứng. Đính kèm kho mã nguồn khi nội dung được phép công khai.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ ứng tuyển", "Tuyển dụng"}
        ));
        list.add(new FaqDefinition(
                "QHDN_CV",
                "Cách mô tả kỹ năng khi chưa có kinh nghiệm doanh nghiệp?",
                "Cách phân biệt kỹ năng tự đánh giá với năng lực đã được thể hiện trong bài tập và dự án.",
                """
                <p>Chỉ liệt kê công nghệ đã trực tiếp sử dụng và có thể giải thích ở mức phù hợp. Có thể chia thành ngôn ngữ lập trình, framework, cơ sở dữ liệu, công cụ cộng tác và kỹ năng bổ trợ để nhà tuyển dụng dễ quét thông tin.</p><p>Thay vì tự nhận “thành thạo” chung chung, hãy nêu bằng chứng như tính năng đã xây dựng, bài toán đã giải quyết hoặc bài kiểm thử đã viết. Cập nhật kỹ năng sau khi hoàn thành dự án mới và bỏ mục không còn phản ánh đúng năng lực hiện tại.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ ứng tuyển", "Tuyển dụng"}
        ));
        list.add(new FaqDefinition(
                "QHDN_CV",
                "Nên đưa đường dẫn GitHub hoặc portfolio vào hồ sơ ứng tuyển như thế nào?",
                "Những điểm cần kiểm tra để liên kết dự án công khai hỗ trợ việc đánh giá hồ sơ ứng tuyển.",
                """
                <p>Chọn các dự án có README giải thích mục tiêu, cách chạy, công nghệ và phần việc của mình. Kiểm tra đường dẫn công khai hoạt động, loại bỏ khóa API, mật khẩu, dữ liệu cá nhân và tài liệu không có quyền chia sẻ.</p><p>Nếu kho mã nguồn không thể công khai vì làm theo nhóm hoặc theo yêu cầu môn học, dùng ảnh minh họa đã được phép chia sẻ hoặc mô tả kỹ thuật trong CV. Không tạo commit hoặc số liệu giả để làm dự án có vẻ lớn hơn thực tế.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ ứng tuyển", "Tuyển dụng"}
        ));
        list.add(new FaqDefinition(
                "QHDN_CV",
                "Cách điều chỉnh CV theo từng vị trí ứng tuyển?",
                "Phương pháp sắp xếp thông tin CV theo yêu cầu tuyển dụng thay vì gửi một bản giống nhau cho mọi vị trí.",
                """
                <p>Đọc kỹ yêu cầu của từng tin tuyển dụng, xác định ba đến năm năng lực liên quan nhất rồi đưa dự án và kỹ năng chứng minh các năng lực đó lên trước. Giữ thông tin học vấn và kinh nghiệm trung thực, chỉ điều chỉnh cách nhấn mạnh nội dung phù hợp.</p><p>Không thêm từ khóa không có căn cứ chỉ để vượt bộ lọc hồ sơ. Trước khi gửi, kiểm tra tên công ty, tên vị trí, tên tệp và thông tin liên hệ để tránh dùng nhầm CV đã chỉnh cho một nhà tuyển dụng khác.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ ứng tuyển", "Tuyển dụng"}
        ));
        list.add(new FaqDefinition(
                "QHDN_THUCTAP_HOSO",
                "Hồ sơ xin xác nhận hoàn thành thực tập gồm những gì?",
                "Danh mục giấy tờ cần đối chiếu trước khi đề nghị trường xác nhận kết quả thực tập.",
                """
                <p>Kiểm tra yêu cầu của khoa về báo cáo thực tập, nhật ký công việc, phiếu nhận xét của doanh nghiệp, xác nhận của giảng viên và các biểu mẫu kèm theo. Tên biểu mẫu và số bản có thể thay đổi theo chương trình nên cần dùng danh mục được thông báo cho khóa học của mình.</p><p>Đảm bảo các trường thông tin, chữ ký và mốc thời gian đã đầy đủ trước khi nộp. Nếu doanh nghiệp gửi nhận xét qua email hoặc hệ thống riêng, kiểm tra cách thức xác nhận được khoa chấp nhận để tránh hồ sơ bị trả về.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thực tập", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QHDN_THUCTAP_HOSO",
                "Cách bổ sung báo cáo thực tập khi hồ sơ bị yêu cầu chỉnh sửa?",
                "Các bước xử lý góp ý và nộp lại báo cáo, tránh tạo nhiều bản không rõ phiên bản.",
                """
                <p>Đọc từng góp ý trong thông báo, xác định phần cần sửa và trao đổi với giảng viên nếu yêu cầu chưa rõ. Lưu một bản làm việc, dùng tên phiên bản có ngày cập nhật và kiểm tra mục lục, hình ảnh, tài liệu tham khảo sau khi sửa.</p><p>Khi nộp lại, đính kèm đúng tệp cuối cùng và ghi ngắn gọn những điểm đã điều chỉnh. Không xóa phản hồi hoặc thay đổi nội dung đã được doanh nghiệp xác nhận nếu việc đó làm sai lệch kết quả thực tập.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thực tập", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QHDN_THUCTAP_HOSO",
                "Làm gì khi phiếu đánh giá của doanh nghiệp thiếu chữ ký?",
                "Cách xác minh người ký và bổ sung phần xác nhận còn thiếu trong bộ hồ sơ thực tập.",
                """
                <p>Kiểm tra biểu mẫu yêu cầu chữ ký của người hướng dẫn, đại diện đơn vị hay dấu xác nhận của doanh nghiệp. Liên hệ đầu mối doanh nghiệp đã hướng dẫn để bổ sung đúng phần còn thiếu, không tự thêm chữ ký hoặc chỉnh sửa bản xác nhận đã phát hành.</p><p>Nếu người hướng dẫn đã nghỉ việc hoặc không thể ký, hỏi khoa về hình thức xác nhận thay thế được chấp thuận. Nộp kèm giải trình và bằng chứng liên hệ theo hướng dẫn chính thức để hồ sơ được xem xét thống nhất.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thực tập", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QHDN_THUCTAP_HOSO",
                "Cách theo dõi tình trạng xét duyệt hồ sơ thực tập?",
                "Cách kiểm tra hồ sơ đã tiếp nhận, đang xử lý hay cần bổ sung và thời điểm nên liên hệ.",
                """
                <p>Tra cứu trạng thái tại nơi đã nộp hồ sơ hoặc kiểm tra thông báo từ khoa. Ghi lại mã yêu cầu, ngày nộp, trạng thái hiện tại và các tài liệu đã đính kèm để không gửi trùng nhiều bộ hồ sơ.</p><p>Nếu quá thời gian xử lý được thông báo mà trạng thái không thay đổi, liên hệ đúng đầu mối phụ trách với mã yêu cầu và câu hỏi cụ thể. Khi được yêu cầu bổ sung, nộp đúng thành phần còn thiếu và kiểm tra xác nhận tiếp nhận sau khi gửi.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thực tập", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QHDN_TUYENDUNG_HOSO",
                "Ứng tuyển thực tập cần chuẩn bị những tài liệu gì?",
                "Cách chuẩn bị CV, bảng điểm và sản phẩm cá nhân theo đúng yêu cầu từng nhà tuyển dụng.",
                """
                <p>Đọc tin tuyển dụng để biết cần CV, bảng điểm, thư giới thiệu, portfolio hoặc đường dẫn dự án nào. Chuẩn bị tệp rõ tên, dễ mở và cập nhật; không tự gửi thêm giấy tờ nhạy cảm khi nhà tuyển dụng chưa yêu cầu hợp lý.</p><p>Trước khi gửi, mở lại từng tệp để kiểm tra nội dung và quyền truy cập của liên kết. Nếu doanh nghiệp dùng biểu mẫu trực tuyến, giữ bản sao câu trả lời đã gửi và cập nhật email xác nhận để tiện theo dõi tiến trình.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ ứng tuyển", "Tuyển dụng"}
        ));
        list.add(new FaqDefinition(
                "QHDN_TUYENDUNG_HOSO",
                "Cách gửi email ứng tuyển có tiêu đề rõ ràng?",
                "Cách viết tiêu đề, phần mở đầu và danh sách tệp đính kèm chuyên nghiệp.",
                """
                <p>Đặt tiêu đề nêu vị trí ứng tuyển và tên ứng viên theo định dạng mà doanh nghiệp yêu cầu. Phần nội dung cần giới thiệu ngắn gọn, nói rõ vị trí mong muốn, lý do phù hợp và các tài liệu được đính kèm.</p><p>Kiểm tra tên người nhận, địa chỉ email, lỗi chính tả và định dạng tệp trước khi gửi. Tránh gửi một email chung cho nhiều doanh nghiệp trong cùng chuỗi người nhận. Giữ bản sao email để theo dõi phản hồi.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ ứng tuyển", "Tuyển dụng"}
        ));
        list.add(new FaqDefinition(
                "QHDN_TUYENDUNG_HOSO",
                "Làm gì khi nhà tuyển dụng yêu cầu bổ sung bảng điểm?",
                "Các bước gửi đúng bảng điểm và bảo vệ thông tin không liên quan trong hồ sơ học tập.",
                """
                <p>Xác định doanh nghiệp cần bảng điểm toàn khóa, bảng điểm tạm thời hay kết quả của một số học phần. Tải bản từ cổng chính thức nếu có và kiểm tra thông tin cá nhân, học kỳ cùng trạng thái xác nhận trên tài liệu.</p><p>Chỉ gửi qua địa chỉ hoặc hệ thống tuyển dụng đã được xác minh. Nếu bảng điểm có dữ liệu không cần thiết cho mục đích tuyển dụng, hỏi đầu mối nhân sự về phạm vi tài liệu chấp nhận thay vì tự sửa nội dung làm thay đổi tính xác thực.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ ứng tuyển", "Tuyển dụng"}
        ));
        list.add(new FaqDefinition(
                "QHDN_TUYENDUNG_HOSO",
                "Cách theo dõi kết quả sau khi nộp hồ sơ tuyển dụng?",
                "Phương pháp lưu trạng thái ứng tuyển và gửi thư hỏi thăm đúng thời điểm.",
                """
                <p>Ghi lại tên doanh nghiệp, vị trí, ngày nộp, kênh liên hệ và thời hạn phản hồi đã nêu trong tin đăng. Kiểm tra hộp thư rác và cuộc gọi nhỡ trong thời gian chờ kết quả.</p><p>Nếu thời hạn phản hồi đã qua, gửi một email ngắn nhắc lại vị trí và ngày ứng tuyển, hỏi lịch trình tiếp theo một cách lịch sự. Tránh gửi liên tục nhiều tin nhắn hoặc gửi lại giấy tờ cá nhân qua tài khoản chưa xác minh.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hồ sơ ứng tuyển", "Tuyển dụng"}
        ));
        list.add(new FaqDefinition(
                "KHCN_DETAI",
                "Điều kiện đăng ký đề tài nghiên cứu khoa học sinh viên là gì?",
                "Các yêu cầu cơ bản về nhóm thực hiện, giảng viên cách và phạm vi đề tài trước khi đăng ký.",
                """
                <p>Sinh viên đọc thông báo nghiên cứu khoa học của trường và kiểm tra điều kiện tham gia, quy mô nhóm, thời gian thực hiện cùng yêu cầu có giảng viên hướng dẫn. Đề tài nên có câu hỏi nghiên cứu rõ, phạm vi khả thi và phương pháp có thể thực hiện trong thời gian được cấp.</p><p>Trước khi nộp, trao đổi với giảng viên về tính mới, nguồn lực, dữ liệu và rủi ro đạo đức nghiên cứu. Không đăng ký đề tài chỉ dựa trên tên hấp dẫn nếu chưa xác định được mục tiêu và sản phẩm đầu ra.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đề tài", "Nghiên cứu khoa học"}
        ));
        list.add(new FaqDefinition(
                "KHCN_DETAI",
                "Cách xác định mục tiêu và câu hỏi nghiên cứu cho đề tài?",
                "Cách thu hẹp vấn đề nghiên cứu thành mục tiêu có thể kiểm chứng và hoàn thành.",
                """
                <p>Bắt đầu từ một vấn đề cụ thể trong lĩnh vực học tập, tổng hợp những gì đã biết và chỉ ra điểm còn cần khảo sát. Viết câu hỏi nghiên cứu ở phạm vi vừa đủ, sau đó xác định dữ liệu, phương pháp và tiêu chí dùng để trả lời câu hỏi đó.</p><p>Mục tiêu nên mô tả kết quả cần đạt, không chỉ liệt kê hoạt động như đọc tài liệu hoặc lập trình. Trao đổi với người hướng dẫn để kiểm tra tính khả thi và tránh đặt quá nhiều mục tiêu vượt quá thời gian, thiết bị hoặc dữ liệu sẵn có.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Nghiên cứu khoa học", "Đề tài"}
        ));
        list.add(new FaqDefinition(
                "KHCN_DETAI",
                "Cách quản lý phiên bản và dữ liệu trong quá trình nghiên cứu?",
                "Nguyên tắc ghi lại thay đổi, nguồn dữ liệu và kết quả thử nghiệm để có thể tái lập nghiên cứu.",
                """
                <p>Sử dụng kho quản lý phiên bản cho mã nguồn và tài liệu phù hợp; đặt tên phiên bản theo quy ước thống nhất. Ghi lại nguồn dữ liệu, ngày thu thập, bước làm sạch, tham số thử nghiệm và môi trường chạy để thành viên khác có thể hiểu cách tạo ra kết quả.</p><p>Không đưa dữ liệu cá nhân, khóa truy cập hoặc tài liệu hạn chế vào kho công khai. Giữ một bản sao dự phòng theo quy định của nhóm và phân quyền truy cập cho người cần thiết. Khi thay đổi phương pháp, nêu rõ lý do và ảnh hưởng đến kết quả trước đó.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Nghiên cứu khoa học", "Đề tài"}
        ));
        list.add(new FaqDefinition(
                "KHCN_DETAI",
                "Cách xây dựng tiến độ thực hiện đề tài theo các mốc?",
                "Phương pháp chia công việc nghiên cứu thành giai đoạn có sản phẩm và tiêu chí hoàn thành.",
                """
                <p>Chia đề tài thành các giai đoạn như khảo sát tài liệu, thiết kế phương pháp, thu thập dữ liệu, triển khai, đánh giá và viết báo cáo. Mỗi giai đoạn cần có người phụ trách, hạn hoàn thành và sản phẩm kiểm tra được.</p><p>Dành thời gian dự phòng cho việc xin quyền truy cập dữ liệu, sửa lỗi công cụ và nhận phản hồi của giảng viên. Cập nhật tiến độ định kỳ bằng kết quả đã hoàn thành, không chỉ bằng số giờ đã làm. Khi trễ mốc, đề xuất điều chỉnh phạm vi hoặc nguồn lực sớm.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đề tài", "Nghiên cứu khoa học"}
        ));
        list.add(new FaqDefinition(
                "KHCN_HOITHAO",
                "Cách đăng ký tham dự hội thảo khoa học của trường?",
                "Các thông tin cần kiểm tra trong thông báo và cách hoàn tất biểu mẫu tham dự.",
                """
                <p>Đọc kỹ chủ đề, đối tượng được tham dự, hình thức tổ chức, hạn đăng ký và yêu cầu chuẩn bị trước hội thảo. Điền thông tin cá nhân đúng hồ sơ trường, chọn phiên tham dự nếu có và lưu thông báo xác nhận đăng ký.</p><p>Nếu hội thảo giới hạn chỗ, không coi việc gửi biểu mẫu là xác nhận thành công cho đến khi nhận phản hồi. Kiểm tra email trước ngày diễn ra để biết phòng tổ chức, đường dẫn trực tuyến hoặc tài liệu cần chuẩn bị.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hội thảo", "Nghiên cứu khoa học"}
        ));
        list.add(new FaqDefinition(
                "KHCN_HOITHAO",
                "Quy định gửi tóm tắt bài báo cho hội thảo sinh viên như thế nào?",
                "Cấu trúc tóm tắt, từ khóa và thông tin tác giả cần rà soát trước khi nộp.",
                """
                <p>Tóm tắt hãy nêu vấn đề, mục tiêu, cách tiếp cận, kết quả chính và đóng góp trong giới hạn từ mà ban tổ chức quy định. Chọn từ khóa đại diện cho nội dung, kiểm tra tên tác giả, đơn vị và thông tin liên hệ trước khi gửi.</p><p>So sánh mẫu định dạng của đúng hội thảo thay vì dùng định dạng từ sự kiện khác. Tất cả đồng tác giả nên xem và xác nhận bản cuối; không gửi nội dung chưa được nhóm thống nhất hoặc chứa hình ảnh, dữ liệu chưa có quyền công bố.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hội thảo", "Đề tài"}
        ));
        list.add(new FaqDefinition(
                "KHCN_HOITHAO",
                "Cách chuẩn bị poster trình bày kết quả nghiên cứu?",
                "Cách lựa chọn nội dung trực quan và bố cục poster khoa học ngắn gọn.",
                """
                <p>Poster nên làm nổi bật câu hỏi nghiên cứu, phương pháp, kết quả chính và kết luận. Dùng biểu đồ hoặc sơ đồ có chú thích rõ, hạn chế đoạn văn dài và bảo đảm chữ đủ lớn để đọc trong không gian trưng bày.</p><p>Kiểm tra số liệu trong hình với báo cáo gốc, ghi nguồn cho hình ảnh sử dụng và tuân thủ kích thước mà ban tổ chức gửi. Chuẩn bị phần trình bày ngắn, sẵn sàng giải thích giới hạn nghiên cứu và trả lời câu hỏi về phương pháp.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hội thảo", "Nghiên cứu khoa học"}
        ));
        list.add(new FaqDefinition(
                "KHCN_HOITHAO",
                "Làm gì khi thông tin tác giả trên chương trình hội thảo bị sai?",
                "Các bước thông báo sai lệch tên, đơn vị hoặc tiêu đề trước khi tài liệu chương trình được chốt.",
                """
                <p>So sánh chương trình với bản đăng ký hoặc bản tóm tắt đã được chấp nhận để xác định trường thông tin sai. Gửi email đến ban tổ chức, nêu rõ mục cần sửa, nội dung hiện tại và nội dung chính xác.</p><p>Đính kèm mã bài hoặc email chấp nhận nếu có, đồng thời kiểm tra cách viết họ tên của tất cả đồng tác giả. Không tạo nhiều yêu cầu trùng lặp; nếu chương trình đã phát hành, hỏi ban tổ chức về khả năng cập nhật bản trực tuyến hoặc đính chính tại sự kiện.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hội thảo", "Nghiên cứu khoa học"}
        ));
        list.add(new FaqDefinition(
                "KHCN_NGHIENCUU",
                "Cách lựa chọn phương pháp nghiên cứu phù hợp câu hỏi?",
                "Nguyên tắc cân nhắc phương pháp định lượng, định tính, thực nghiệm hoặc kết hợp theo mục tiêu.",
                """
                <p>Phương pháp cần xuất phát từ câu hỏi nghiên cứu và loại bằng chứng cần thu thập. Nếu cần đo lường sự khác biệt, cân nhắc thiết kế định lượng hoặc thực nghiệm; nếu cần hiểu trải nghiệm và bối cảnh, xem xét phỏng vấn hoặc phân tích định tính.</p><p>Đánh giá yêu cầu về cỡ mẫu, công cụ, quyền truy cập dữ liệu và năng lực thực hiện. Nêu rõ giới hạn của phương pháp, cách giảm sai lệch và tiêu chí đánh giá kết quả; không lựa chọn phương pháp chỉ vì dễ triển khai nếu nó không trả lời được câu hỏi.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Nghiên cứu khoa học", "Đề tài"}
        ));
        list.add(new FaqDefinition(
                "KHCN_NGHIENCUU",
                "Cần trích dẫn tài liệu thế nào để tránh đạo văn trong báo cáo?",
                "Cách ghi nguồn cho ý tưởng, dữ liệu, hình ảnh và đoạn văn tham khảo.",
                """
                <p>Mọi ý tưởng, số liệu, hình ảnh hoặc nội dung không do nhóm tự tạo cần được ghi nguồn theo chuẩn trích dẫn được yêu cầu. Khi diễn giải lại tài liệu, vẫn phải dẫn nguồn; thay một vài từ không biến nội dung của người khác thành kết quả của nhóm.</p><p>Dùng công cụ quản lý tài liệu tham khảo để giảm sai sót và kiểm tra danh mục nguồn trước khi nộp. Chỉ trích dẫn tài liệu đã thực sự đọc hoặc kiểm tra. Trao đổi với giảng viên về cách cập nhật đóng góp của các thành viên và công cụ hỗ trợ được sử dụng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Nghiên cứu khoa học", "Đề tài"}
        ));
        list.add(new FaqDefinition(
                "KHCN_NGHIENCUU",
                "Cách đánh giá độ tin cậy của nguồn dữ liệu nghiên cứu?",
                "Các tiêu chí xem xét nguồn gốc, quy trình thu thập, tính đại diện và giới hạn của dữ liệu.",
                """
                <p>Ghi rõ ai gửi dữ liệu, thời điểm thu thập, định nghĩa các trường và quy trình làm sạch. Xem dữ liệu có đại diện cho đối tượng cần nghiên cứu không, có thiếu giá trị hoặc sai lệch chọn mẫu hay không.</p><p>Nếu dữ liệu từ nguồn thứ cấp, đọc tài liệu mô tả và điều kiện sử dụng trước khi phân tích. Không kết luận rộng hơn phạm vi dữ liệu cho phép. Trong báo cáo, mô tả giới hạn, các giả định và cách kiểm tra chất lượng dữ liệu để người đọc hiểu mức độ tin cậy.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Nghiên cứu khoa học", "Đề tài"}
        ));
        list.add(new FaqDefinition(
                "KHCN_NGHIENCUU",
                "Cách báo cáo kết quả âm hoặc không đạt giả thuyết ban đầu?",
                "Nguyên tắc trình bày kết quả trung thực khi thử nghiệm không tạo ra hiệu quả như dự kiến.",
                """
                <p>Báo cáo đầy đủ phương pháp, điều kiện thử nghiệm và kết quả quan sát được, kể cả khi kết quả không ủng hộ giả thuyết. Kiểm tra lỗi triển khai, độ phù hợp của phép đo và khả năng dữ liệu chưa đủ để kết luận.</p><p>Không loại bỏ lần chạy bất lợi chỉ để làm kết quả đẹp hơn. Phân biệt rõ kết quả thực nghiệm với cách giải thích của nhóm, nêu giới hạn và đề xuất bước kiểm tra tiếp theo. Một kết quả âm có thể vẫn hữu ích nếu thiết kế và dữ liệu được mô tả minh bạch.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Nghiên cứu khoa học", "Đề tài"}
        ));
        list.add(new FaqDefinition(
                "KHCN_DETAI_HOSO",
                "Danh mục hồ sơ đăng ký đề tài nghiên cứu khoa học?",
                "Những thành phần thường có trong bộ hồ sơ đăng ký và các mục cần đối chiếu theo biểu mẫu hiện hành.",
                """
                <p>Đọc thông báo của đợt tuyển chọn để xác định mẫu thuyết minh, danh sách thành viên, xác nhận của giảng viên hướng dẫn và các cam kết cần nộp. Nội dung đề cương cần thống nhất về tên đề tài, mục tiêu, phương pháp, tiến độ và sản phẩm dự kiến.</p><p>Kiểm tra chữ ký, định dạng tệp, giới hạn số trang và hạn nộp. Không sử dụng mẫu của năm trước nếu biểu mẫu mới đã được ban hành. Giữ bản sao đầy đủ của hồ sơ gửi đi cùng xác nhận tiếp nhận từ đơn vị phụ trách.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đề tài", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "KHCN_DETAI_HOSO",
                "Cách sửa hồ sơ đề tài sau khi hội đồng yêu cầu bổ sung?",
                "Các bước phản hồi góp ý và cập nhật bản thuyết minh mà không làm mất dấu thay đổi.",
                """
                <p>Tập hợp toàn bộ góp ý của hội đồng, phân loại theo mục tiêu, phương pháp, ngân sách, tiến độ và sản phẩm. Lập bảng so sánh nêu từng góp ý, phần đã sửa và vị trí trong bản thuyết minh mới.</p><p>Lưu bản trước chỉnh sửa và bản mới với tên phiên bản rõ ràng. Nếu không tiếp thu một góp ý, giải thích lý do chuyên môn trong phần phản hồi theo hướng dẫn của hội đồng. Nộp đúng hạn và kiểm tra thông báo tiếp nhận của đơn vị quản lý.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đề tài", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "KHCN_DETAI_HOSO",
                "Làm gì khi hồ sơ đề tài thiếu xác nhận của giảng viên hướng dẫn?",
                "Cách hoàn thiện phần xác nhận và kiểm tra vai trò người cách trước khi nộp lại.",
                """
                <p>Đọc biểu mẫu để xác định vị trí cần xác nhận và hình thức chữ ký được chấp nhận. Gửi bản thuyết minh hoàn chỉnh cho giảng viên hướng dẫn xem xét trước khi xin xác nhận; không tự thêm chữ ký hoặc sửa tệp đã ký.</p><p>Nếu người hướng dẫn thay đổi hoặc vắng mặt, liên hệ đơn vị khoa học và công nghệ để biết quy trình thay thế hợp lệ. Sau khi bổ sung, kiểm tra lại toàn bộ hồ sơ để bảo đảm tên đề tài và thông tin nhóm thống nhất giữa các trang.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đề tài", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "KHCN_DETAI_HOSO",
                "Cách kiểm tra kết quả tiếp nhận hồ sơ nghiên cứu?",
                "Cách theo dõi mã hồ sơ, trạng thái xét duyệt và thông báo bổ sung giấy tờ.",
                """
                <p>Lưu mã hồ sơ, ngày gửi và email xác nhận tiếp nhận. Theo dõi trạng thái tại cổng quản lý hoặc kênh thông báo của đơn vị phụ trách; phân biệt trạng thái đã nhận hồ sơ với trạng thái được duyệt thực hiện.</p><p>Nếu bị yêu cầu bổ sung, nêu rõ hạn hoàn thiện và tài liệu cần nộp. Khi quá thời gian phản hồi được công bố, gửi câu hỏi kèm mã hồ sơ cho đầu mối chính thức. Không gửi nhiều bản trùng lặp nếu hệ thống chưa xác nhận bản trước bị lỗi.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đề tài", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "KHCN_HOITHAO_DANGKY",
                "Cần chuẩn bị hồ sơ gì để đăng ký báo cáo tại hội thảo khoa học?",
                "Các dữ liệu cần chuẩn bị khi đăng ký tên bài, tác giả, hình thức báo cáo và nhu cầu thiết bị.",
                """
                <p>Chuẩn bị tên bài, tóm tắt, danh sách tác giả, đơn vị, thông tin liên hệ và lựa chọn hình thức báo cáo theo yêu cầu của ban tổ chức. Nếu đăng ký báo cáo miệng hoặc poster, kiểm tra giới hạn thời lượng và quy cách trình bày tương ứng.</p><p>Đảm bảo tất cả tác giả thống nhất phiên bản gửi đi và thứ tự tên. Kiểm tra định dạng tài liệu, cách đặt tên tệp và ngày hết hạn nộp. Lưu email xác nhận để sử dụng khi cần hỏi về lịch trình hoặc thông tin trong chương trình.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hội thảo", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "KHCN_HOITHAO_DANGKY",
                "Cách cập nhật người trình bày sau khi đã đăng ký hội thảo?",
                "Các bước xin đổi người trình bày và bảo đảm người thay thế nắm đúng nội dung bài báo cáo.",
                """
                <p>Nếu người trình bày ban đầu không thể tham dự, liên hệ ban tổ chức sớm, nêu mã bài, lý do thay đổi và thông tin người thay thế. Kiểm tra quy định hội thảo về việc người trình bày có bắt buộc là tác giả hay phải hoàn tất đăng ký tham dự.</p><p>Người thay thế cần nhận bản trình bày cuối, hiểu phương pháp và kết quả, đồng thời được nhóm thống nhất trước khi báo cáo. Lưu xác nhận của ban tổ chức về thay đổi để tránh nhầm tên trong lịch trình và giấy chứng nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hội thảo", "Nghiên cứu khoa học"}
        ));
        list.add(new FaqDefinition(
                "KHCN_HOITHAO_DANGKY",
                "Cách xác nhận tham dự sau khi gửi biểu mẫu hội thảo?",
                "Cách phân biệt biểu mẫu đã gửi với việc đăng ký đã được ban tổ chức chấp nhận.",
                """
                <p>Sau khi gửi biểu mẫu, kiểm tra trang xác nhận hoặc email phản hồi. Nếu sự kiện yêu cầu duyệt đăng ký, trạng thái gửi thành công chưa đồng nghĩa với việc đã có suất tham dự.</p><p>Kiểm tra hộp thư rác và ghi lại mã đăng ký nếu có. Nếu không nhận phản hồi trong thời hạn được thông báo, liên hệ ban tổ chức với họ tên, email đăng ký và thời điểm gửi. Không gửi lại nhiều lần nếu chưa kiểm tra trạng thái bản đăng ký đầu tiên.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hội thảo", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "KHCN_HOITHAO_DANGKY",
                "Thủ tục xin giấy chứng nhận tham dự hoặc báo cáo khoa học gồm những gì?",
                "Thông tin cần đối chiếu để giấy chứng nhận ghi đúng vai trò và tên người tham dự.",
                """
                <p>Kiểm tra điều kiện cấp chứng nhận của sự kiện, chẳng hạn yêu cầu điểm danh, hoàn thành phiên báo cáo hoặc có tên trong chương trình. Lưu mã đăng ký và bằng chứng tham dự khi ban tổ chức có yêu cầu xác minh.</p><p>Nếu chứng nhận ghi sai họ tên, tên bài hoặc vai trò tham dự/báo cáo, gửi đề nghị chỉnh sửa kèm thông tin chính xác. Dùng kênh liên hệ của ban tổ chức và giữ lại chứng nhận cũ để so sánh khi cần cấp lại.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Hội thảo", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "YTE_BHYT",
                "Cách kiểm tra thời hạn thẻ bảo hiểm y tế sinh viên?",
                "Cách đối chiếu thông tin thẻ, thời hạn sử dụng và dữ liệu được cập nhật trên hồ sơ sinh viên.",
                """
                <p>Kiểm tra thông tin thẻ BHYT trên cổng chính thức của cơ quan bảo hiểm hoặc nơi tra cứu được trường hướng dẫn. So sánh họ tên, ngày sinh, mã số bảo hiểm và thời hạn sử dụng với dữ liệu cá nhân hiện có.</p><p>Nếu thẻ sắp hết hạn, xem thông báo về thời điểm đóng và gia hạn theo đợt của nhà trường. Nếu thông tin không trùng khớp, lưu ảnh màn hình đã che dữ liệu không cần thiết và gửi yêu cầu đến đầu mối BHYT phụ trách sinh viên.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo hiểm y tế", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "YTE_BHYT",
                "Làm thế nào để thay đổi nơi đăng ký khám, chữa bệnh ban đầu?",
                "Các bước kiểm tra thời hạn cho phép điều chỉnh và giấy tờ cần thiết để xin thay đổi cơ sở khám chữa bệnh.",
                """
                <p>Kiểm tra quy định hiện hành về thời điểm được đổi nơi khám chữa bệnh ban đầu và danh sách cơ sở tiếp nhận đăng ký. Chuẩn bị mã số BHYT và thông tin cơ sở dự kiến đăng ký theo biểu mẫu được hướng dẫn.</p><p>Nộp yêu cầu qua kênh của cơ quan hoặc đơn vị tiếp nhận được công bố. Sau khi được xử lý, kiểm tra thông tin nơi khám mới trên dữ liệu thẻ. Không mặc định việc gửi yêu cầu đã hoàn tất thay đổi nếu hệ thống chưa cập nhật kết quả.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo hiểm y tế", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "YTE_BHYT",
                "Làm gì khi thông tin thẻ BHYT không khớp thông tin sinh viên?",
                "Cách xác định trường thông tin sai và gửi đề nghị điều chỉnh dữ liệu bảo hiểm.",
                """
                <p>So sánh họ tên, ngày sinh, mã số BHYT và thông tin định danh giữa thẻ với hồ sơ sinh viên. Ghi rõ trường dữ liệu không khớp và chuẩn bị giấy tờ chứng minh thông tin chính xác theo hướng dẫn của đầu mối tiếp nhận.</p><p>Không tự sửa ảnh thẻ hoặc sử dụng giấy tờ đã chỉnh sửa. Gửi yêu cầu điều chỉnh qua kênh chính thức, lưu mã tiếp nhận và kiểm tra lại dữ liệu sau khi có kết quả để tránh tiếp tục phát sinh sai lệch trong lần khám chữa bệnh sau.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo hiểm y tế", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "YTE_BHYT",
                "Nên kiểm tra những quyền lợi nào trước khi sử dụng thẻ bảo hiểm y tế?",
                "Những thông tin nên xác minh về hiệu lực thẻ, mức hưởng và tuyến khám chữa bệnh.",
                """
                <p>Trước khi sử dụng thẻ, kiểm tra thẻ còn hiệu lực, thông tin cá nhân chính xác và cơ sở dự định đến có phù hợp với quy định áp dụng hay không. Mức hưởng thực tế còn phụ thuộc trường hợp khám chữa bệnh và quy định bảo hiểm hiện hành.</p><p>Khi cần tư vấn quyền lợi cụ thể, liên hệ cơ sở khám chữa bệnh hoặc cơ quan bảo hiểm qua kênh chính thức, gửi thông tin cần thiết nhưng không đăng tải công khai mã số bảo hiểm. Giữ hóa đơn và giấy tờ liên quan nếu cần hỏi về quá trình tiếp nhận quyền lợi.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo hiểm y tế", "Chính sách hỗ trợ"}
        ));
        list.add(new FaqDefinition(
                "YTE_SUCKHOE",
                "Cách đăng ký khám tư vấn sức khỏe tại đơn vị y tế trường?",
                "Các bước gửi yêu cầu tư vấn, lựa chọn thời gian và cung cấp thông tin ban đầu phù hợp.",
                """
                <p>Kiểm tra thông báo lịch làm việc của đơn vị y tế trường và các hình thức đăng ký được hỗ trợ. Điền thông tin liên hệ cần thiết, mô tả ngắn lý do cần tư vấn và chọn khung thời gian nếu hệ thống có gửi lựa chọn.</p><p>Không gửi hồ sơ sức khỏe chi tiết qua kênh không được bảo vệ. Nếu triệu chứng cần xử trí khẩn cấp, không chờ đến lịch tư vấn thông thường mà tìm trợ giúp y tế phù hợp ngay. Lưu thông tin xác nhận lịch hẹn để so sánh khi đến khám.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sức khỏe", "Tư vấn y tế"}
        ));
        list.add(new FaqDefinition(
                "YTE_SUCKHOE",
                "Cách chuẩn bị thông tin trước buổi tư vấn sức khỏe?",
                "Những thông tin giúp buổi tư vấn hiệu quả như diễn biến, thuốc đang sử dụng và tiền sử liên quan.",
                """
                <p>Ghi lại vấn đề cần hỏi, thời điểm bắt đầu, diễn biến, yếu tố làm triệu chứng thay đổi và các thông tin sức khỏe liên quan. Mang theo danh sách thuốc đang sử dụng hoặc giấy tờ khám trước đây nếu có và cần thiết cho buổi tư vấn.</p><p>Trao đổi trung thực với nhân viên y tế, chỉ gửi thông tin trong phạm vi cần thiết và hỏi lại nếu chưa hiểu hướng dẫn. Không tự ngừng thuốc hoặc thay đổi điều trị dựa trên lời khuyên không rõ nguồn; với tình trạng khẩn cấp, tìm dịch vụ cấp cứu phù hợp.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sức khỏe", "Tư vấn y tế"}
        ));
        list.add(new FaqDefinition(
                "YTE_SUCKHOE",
                "Làm sao để bảo vệ thông tin sức khỏe khi gửi yêu cầu tư vấn?",
                "Cách chia sẻ dữ liệu vừa đủ cho việc tiếp nhận và tránh làm lộ hồ sơ y tế cá nhân.",
                """
                <p>Sử dụng kênh đăng ký chính thức và chỉ gửi những thông tin được yêu cầu cho việc bố trí tư vấn. Tránh đăng công khai kết quả xét nghiệm, ảnh giấy tờ hoặc mô tả sức khỏe riêng tư trên diễn đàn sinh viên.</p><p>Nếu cần gửi tệp, kiểm tra đúng người nhận và quy định bảo mật của kênh tiếp nhận. Khi có thắc mắc về ai được quyền xem hồ sơ, hỏi đơn vị y tế về phạm vi sử dụng thông tin và cách yêu cầu điều chỉnh dữ liệu không chính xác.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sức khỏe", "Tư vấn y tế"}
        ));
        list.add(new FaqDefinition(
                "YTE_SUCKHOE",
                "Cách tìm đầu mối hỗ trợ khi gặp tình huống sức khỏe trong khuôn viên?",
                "Cách xác định nơi liên hệ theo mức độ khẩn cấp và vị trí xảy ra sự việc.",
                """
                <p>Đối với tình huống không khẩn cấp, liên hệ đơn vị y tế trường theo lịch và kênh đã công bố. Khi có dấu hiệu cần trợ giúp ngay hoặc tình trạng diễn biến nhanh, báo nhân sự phụ trách gần nhất và liên hệ dịch vụ cấp cứu phù hợp thay vì chờ phản hồi qua biểu mẫu trực tuyến.</p><p>Khi thông báo sự việc, nêu vị trí cụ thể, thời điểm và nhu cầu hỗ trợ ở mức cần thiết. Không tự di chuyển người bị nạn khi có nguy cơ làm tình trạng nặng hơn; làm theo hướng dẫn của người có chuyên môn hoặc nhân viên cấp cứu.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sức khỏe", "Tư vấn y tế"}
        ));
        list.add(new FaqDefinition(
                "YTE_HOTRO",
                "Cách xin xác nhận hồ sơ để hưởng chính sách hỗ trợ sinh viên?",
                "Cách tìm đúng chương trình hỗ trợ và chuẩn bị bằng chứng theo tiêu chí xét duyệt.",
                """
                <p>Đọc thông báo của chương trình để xác định đối tượng, thời hạn, mẫu đơn và loại giấy tờ chứng minh. Các chương trình khác nhau có thể yêu cầu xác nhận hoàn cảnh, kết quả học tập hoặc thông tin liên quan khác; không dùng một bộ hồ sơ chung nếu tiêu chí không giống nhau.</p><p>Kiểm tra hạn hiệu lực và yêu cầu bản sao hoặc bản gốc trước khi nộp. Nếu giấy xác nhận do đơn vị khác cấp, dự trù thời gian xin giấy. Lưu bản sao hồ sơ và mã tiếp nhận để theo dõi kết quả xét hỗ trợ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chính sách hỗ trợ", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "YTE_HOTRO",
                "Liên hệ ai khi cần hỗ trợ khẩn cấp trong trường?",
                "Cách liên hệ bộ phận phù hợp khi phát sinh vấn đề cần được tiếp nhận sớm.",
                """
                <p>Xác định loại hỗ trợ đang cần như y tế, công tác sinh viên, tài chính hay hỗ trợ an toàn. Dùng danh bạ và thông báo chính thức của trường để chọn đầu mối đúng, gửi vị trí và cách liên lạc lại.</p><p>Nếu nguy cơ đang diễn ra hoặc cần xử trí khẩn cấp, sử dụng dịch vụ khẩn cấp phù hợp và báo người có trách nhiệm tại chỗ; không phụ thuộc duy nhất vào hệ thống ticket hoặc email. Sau khi tình huống ổn định, có thể gửi hồ sơ bổ sung theo hướng dẫn của đơn vị tiếp nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chính sách hỗ trợ", "Tư vấn y tế"}
        ));
        list.add(new FaqDefinition(
                "YTE_HOTRO",
                "Cách đề nghị điều chỉnh thông tin hồ sơ hỗ trợ?",
                "Những nội dung cần nêu khi phát hiện họ tên, mã sinh viên hoặc dữ liệu xét duyệt có sai sót.",
                """
                <p>So sánh thông tin trong thông báo xét duyệt với hồ sơ gốc và chỉ ra trường dữ liệu cụ thể cần điều chỉnh. Chuẩn bị giấy tờ chứng minh theo yêu cầu, tránh gửi nhiều bản chụp không cần thiết hoặc tài liệu không liên quan đến nội dung sửa.</p><p>Gửi yêu cầu qua kênh được đơn vị quản lý chương trình công bố và lưu mã tiếp nhận. Nếu đợt xét duyệt sắp kết thúc, nêu rõ hạn liên quan để cán bộ có thể hướng dẫn cách xử lý phù hợp, nhưng không mặc định yêu cầu điều chỉnh làm thay đổi kết quả xét duyệt.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chính sách hỗ trợ", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "YTE_HOTRO",
                "Khi nào cần cập nhật thông tin liên hệ của người nhận hỗ trợ?",
                "Các trường hợp cần sửa thông tin liên hệ và cách xem lại thông báo giải ngân hoặc kết quả.",
                """
                <p>Nên cập nhật khi email hoặc số điện thoại không còn sử dụng, thông tin nhận thông báo thay đổi hoặc hệ thống phản hồi không thể liên hệ. Thực hiện qua hồ sơ chính thức và hoàn thành bước xác minh nếu được yêu cầu.</p><p>Sau khi lưu, kiểm tra lại thông tin đã cập nhật và theo dõi thông báo về hồ sơ hỗ trợ. Việc sửa thông tin liên hệ không tự thay đổi nội dung xét duyệt hoặc thông tin thanh toán; các thay đổi thuộc phần khác cần được yêu cầu riêng theo quy trình.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chính sách hỗ trợ", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "YTE_BHYT_HOSO",
                "Lần đầu tham gia bảo hiểm y tế, sinh viên cần chuẩn bị hồ sơ gì?",
                "Các trường dữ liệu và giấy tờ thường cần đối chiếu khi lập hồ sơ tham gia bảo hiểm y tế.",
                """
                <p>Đọc hướng dẫn của đợt thu để biết thông tin cần khai báo, thời hạn, mức đóng và cách xử lý trường hợp đã có thẻ theo nhóm đối tượng khác. So sánh họ tên, ngày sinh, mã số bảo hiểm và thông tin định danh trước khi xác nhận hồ sơ.</p><p>Nếu đã tham gia BHYT theo diện khác, báo rõ tình trạng để đơn vị phụ trách kiểm tra khả năng đồng bộ và tránh khai báo trùng. Lưu biên nhận hoặc xác nhận nộp hồ sơ để theo dõi trạng thái sau đó.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo hiểm y tế", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "YTE_BHYT_HOSO",
                "Cách bổ sung giấy tờ khi hồ sơ BHYT bị yêu cầu hoàn thiện?",
                "Cách xác định tài liệu còn thiếu và gửi bổ sung đúng hồ sơ đang xử lý.",
                """
                <p>Mở thông báo yêu cầu bổ sung và so sánh tên giấy tờ, định dạng, thời hạn hiệu lực cùng hình thức xác nhận. Tập hợp đúng tài liệu được yêu cầu thay vì gửi lại toàn bộ hồ sơ không liên quan.</p><p>Khi nộp bổ sung, ghi mã hồ sơ hoặc thông tin giúp đơn vị tiếp nhận ghép tài liệu vào đúng bản ghi. Lưu xác nhận đã tiếp nhận bổ sung. Nếu không thể gửi đúng giấy tờ theo thông báo, liên hệ đầu mối trước hạn để được hướng dẫn phương án hợp lệ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo hiểm y tế", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "YTE_BHYT_HOSO",
                "Làm gì khi phát hiện hồ sơ BHYT bị ghi nhận trùng?",
                "Các bước thông báo tình trạng tham gia bảo hiểm ở nhiều nhóm và nhờ kiểm tra bản ghi.",
                """
                <p>Kiểm tra thông tin trên thẻ đang sử dụng và hồ sơ trường, sau đó gửi mã số bảo hiểm và thông tin đợt tham gia có liên quan cho đầu mối phụ trách. Không tự yêu cầu hủy thẻ đang có hiệu lực khi chưa xác định nhóm tham gia đúng theo quy định.</p><p>Đề nghị đơn vị tiếp nhận kiểm tra dữ liệu trùng và hướng dẫn cập nhật. Lưu mã yêu cầu, kết quả xử lý và thông tin xác nhận sau điều chỉnh để tránh sai lệch tiếp tục xuất hiện ở kỳ thu sau.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo hiểm y tế", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "YTE_BHYT_HOSO",
                "Cách kiểm tra trạng thái hồ sơ BHYT sau khi đã nộp?",
                "Cách dùng biên nhận, mã hồ sơ và thời hạn xử lý để theo dõi tiến độ tham gia bảo hiểm.",
                """
                <p>Sau khi nộp, lưu mã hồ sơ, ngày nộp và biên nhận thanh toán nếu có. Tra cứu trạng thái theo kênh được hướng dẫn để biết hồ sơ đã tiếp nhận, đang xác minh, cần bổ sung hay đã hoàn tất.</p><p>Nếu quá thời hạn được thông báo mà trạng thái không thay đổi, liên hệ đơn vị phụ trách kèm mã hồ sơ. Trước khi gửi lại, kiểm tra xem yêu cầu trước có được cập nhật để không tạo bản trùng. Sau khi hoàn tất, kiểm tra lại thời hạn và dữ liệu thẻ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Bảo hiểm y tế", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "YTE_SUCKHOE_TUVAN",
                "Cách đặt lịch tư vấn khi cần trao đổi vấn đề sức khỏe riêng tư?",
                "Cách chọn kênh tiếp nhận phù hợp và mô tả nhu cầu mà không công khai dữ liệu cá nhân.",
                """
                <p>Kiểm tra các hình thức tư vấn được đơn vị y tế công bố, thời gian tiếp nhận và cách bảo mật hồ sơ. Khi đăng ký, mô tả ngắn chủ đề cần trao đổi cùng khoảng thời gian có thể tham dự; chỉ gửi thông tin chi tiết qua kênh được chỉ định.</p><p>Nếu muốn đổi hoặc hủy lịch, báo sớm theo hướng dẫn để nhường khung thời gian cho người khác. Không sử dụng mục chat công khai hoặc diễn đàn chung để gửi thông tin sức khỏe riêng tư. Trong tình huống khẩn cấp, cần tìm hỗ trợ y tế ngay thay vì đợi lịch tư vấn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tư vấn y tế", "Sức khỏe"}
        ));
        list.add(new FaqDefinition(
                "YTE_SUCKHOE_TUVAN",
                "Cách chuẩn bị câu hỏi cho buổi tư vấn sức khỏe tinh thần và thể chất?",
                "Cách ghi lại mối quan tâm và các yếu tố liên quan để trao đổi rõ ràng với chuyên viên.",
                """
                <p>Trước buổi hẹn, ghi lại điều muốn hỏi, thời điểm vấn đề bắt đầu và ảnh hưởng đến sinh hoạt hoặc học tập. Chỉ chia sẻ mức thông tin bạn thấy phù hợp trong môi trường tư vấn và hỏi về quy trình bảo mật nếu còn băn khoăn.</p><p>Chuyên viên có thể cần hỏi thêm để hiểu bối cảnh; nếu câu hỏi chưa rõ, đề nghị giải thích lại. Ghi lại các bước tiếp theo được đề xuất và kênh liên hệ nếu cần hỗ trợ tiếp tục. Không coi thông tin trên câu hỏi thường gặp thay thế đánh giá trực tiếp của chuyên gia.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tư vấn y tế", "Sức khỏe"}
        ));
        list.add(new FaqDefinition(
                "YTE_SUCKHOE_TUVAN",
                "Cách đổi lịch tư vấn đã đặt?",
                "Các bước thông báo không thể tham dự và xác nhận lại lịch mới.",
                """
                <p>Mở xác nhận lịch hẹn để tìm hướng dẫn đổi lịch hoặc liên hệ đầu mối đã tiếp nhận đăng ký. Cung cấp mã hẹn, ngày giờ hiện tại và các khung thời gian thay thế phù hợp, tránh gửi chi tiết sức khỏe riêng tư trong nội dung thông báo lịch.</p><p>Chỉ coi lịch mới được xác nhận khi nhận phản hồi từ hệ thống hoặc đơn vị y tế. Nếu không thể tham dự, thông báo hủy sớm để khung thời gian được sử dụng cho người khác. Ghi lại thời gian mới và những giấy tờ cần chuẩn bị trước cuộc hẹn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tư vấn y tế", "Sức khỏe"}
        ));
        list.add(new FaqDefinition(
                "YTE_SUCKHOE_TUVAN",
                "Làm gì khi chưa hiểu rõ hướng dẫn sau buổi tư vấn?",
                "Cách xin giải thích lại, xác nhận bước tiếp theo và phân biệt hỏi đáp thông thường với tình trạng khẩn cấp.",
                """
                <p>Nếu chưa hiểu chỉ dẫn đã nhận, ghi lại điểm chưa rõ và liên hệ theo kênh theo dõi được chuyên viên gửi. Nêu ngắn gọn nội dung cần xác minh và thời điểm buổi tư vấn để người tiếp nhận tra cứu đúng thông tin.</p><p>Không tự suy đoán liều dùng hoặc thay đổi hướng dẫn chuyên môn dựa trên trao đổi không chính thức. Nếu tình trạng trở nên khẩn cấp hoặc diễn biến nhanh, tìm dịch vụ y tế thích hợp ngay thay vì chờ trả lời qua email hoặc hệ thống trực tuyến.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tư vấn y tế", "Sức khỏe"}
        ));
        list.add(new FaqDefinition(
                "CSVC_SUACHUA",
                "Cách gửi yêu cầu sửa chữa khi thiết bị lớp học bị hỏng?",
                "Cách ghi nhận vị trí, mã thiết bị và mức độ ảnh hưởng để đội kỹ thuật tiếp nhận chính xác.",
                """
                <p>Ghi rõ tòa nhà, tầng, phòng học và tên hoặc mã thiết bị nếu có. Mô tả dấu hiệu hư hỏng, thời điểm phát hiện và việc lỗi có ảnh hưởng đến an toàn hay làm gián đoạn buổi học không.</p><p>Gửi yêu cầu qua kênh cơ sở vật chất được trường công bố, kèm ảnh minh họa nếu có thể chụp an toàn. Không tự tháo lắp thiết bị điện hoặc di chuyển vật nặng. Nếu có nguy cơ mất an toàn, báo ngay cho người phụ trách tại chỗ thay vì chỉ chờ ticket.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Cơ sở vật chất", "Sửa chữa trang thiết bị"}
        ));
        list.add(new FaqDefinition(
                "CSVC_SUACHUA",
                "Cách theo dõi tiến độ phiếu sửa chữa cơ sở vật chất?",
                "Các trạng thái cần kiểm tra và cách bổ sung thông tin khi yêu cầu chưa được xử lý.",
                """
                <p>Lưu mã phiếu và kiểm tra trạng thái như đã tiếp nhận, đang phân công, đang xử lý hoặc đã hoàn thành nếu hệ thống có các trạng thái này. Đọc ghi chú của đội kỹ thuật để biết có cần gửi ảnh, vị trí hoặc thời gian tiếp cận thiết bị hay không.</p><p>Nếu trạng thái không cập nhật trong khoảng thời gian được thông báo, phản hồi trên cùng phiếu để giữ lịch sử xử lý liền mạch. Khi vấn đề đã khắc phục, kiểm tra trực tiếp thiết bị và phản hồi nếu lỗi vẫn còn thay vì tạo phiếu mới ngay lập tức.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Cơ sở vật chất", "Sửa chữa trang thiết bị"}
        ));
        list.add(new FaqDefinition(
                "CSVC_SUACHUA",
                "Cách báo sự cố có nguy cơ mất an toàn trong khuôn viên?",
                "Cách phân biệt sự cố khẩn cấp với yêu cầu sửa chữa thông thường và cách thông báo đúng đầu mối.",
                """
                <p>Sự cố như dây điện hở, mùi khét, trần có nguy cơ rơi hoặc nước tràn vào khu vực điện cần được báo ngay cho người phụ trách gần nhất và đầu mối khẩn cấp được trường công bố. Giữ khoảng cách an toàn, cảnh báo người xung quanh và không tự xử lý khi không có chuyên môn.</p><p>Sau khi đã báo trực tiếp, có thể bổ sung phiếu trên hệ thống với vị trí chính xác, thời gian và mô tả hiện tượng. Không chạm vào thiết bị nghi mất an toàn hoặc cố bật lại nguồn điện trước khi được kiểm tra.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Cơ sở vật chất", "Sửa chữa trang thiết bị"}
        ));
        list.add(new FaqDefinition(
                "CSVC_SUACHUA",
                "Làm gì khi thiết bị vẫn gặp lỗi sau khi sửa?",
                "Cách cập nhật phiếu cũ và cung cấp thông tin để kỹ thuật viên kiểm tra nguyên nhân tái phát.",
                """
                <p>Mở lại mã phiếu đã xử lý, nêu rõ thời điểm lỗi xuất hiện trở lại và điều kiện xảy ra, chẳng hạn khi bật thiết bị hoặc sau một khoảng sử dụng. Nếu có thông báo hoàn thành trước đó, giữ lại để đội kỹ thuật tra lịch sử sửa chữa.</p><p>Không tự tháo thiết bị hoặc lặp đi lặp lại thao tác có thể làm tăng rủi ro. Đề nghị kiểm tra lại trên cùng phiếu để tránh phân tán lịch sử. Khi cần thay thế linh kiện hoặc ngừng sử dụng tạm thời, làm theo hướng dẫn của người phụ trách.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sửa chữa trang thiết bị", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "CSVC_PHONGHOC",
                "Cách đăng ký sử dụng phòng học ngoài thời khóa biểu?",
                "Các thông tin cần nêu khi đề nghị dùng phòng cho học nhóm, sinh hoạt học thuật hoặc hoạt động được phép.",
                """
                <p>Kiểm tra quy định sử dụng phòng, thời gian mở cửa và phòng còn trống trước khi gửi đề nghị. Cung cấp mục đích sử dụng, ngày giờ bắt đầu và kết thúc, số người dự kiến, đơn vị chịu trách nhiệm cùng nhu cầu thiết bị.</p><p>Chỉ sử dụng phòng sau khi nhận xác nhận theo quy trình của trường. Người đăng ký cần bảo đảm hoạt động đúng mục đích, bàn ghế được sắp xếp lại và phòng được bàn giao đúng giờ. Nếu thay đổi lịch, báo đầu mối quản lý để cập nhật việc sử dụng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phòng học", "Cơ sở vật chất"}
        ));
        list.add(new FaqDefinition(
                "CSVC_PHONGHOC",
                "Cách báo phòng học bị trùng lịch hoặc không mở được?",
                "Cách đối chiếu lịch sử dụng và xử lý khi phòng trên thông báo khác với thực tế.",
                """
                <p>Kiểm tra mã phòng, ngày giờ và lịch được đơn vị quản lý công bố. Nếu lớp được xếp vào phòng đang có hoạt động khác hoặc cửa phòng không mở được, báo giảng viên và đầu mối quản lý phòng để xác minh lịch.</p><p>Không tự ý chuyển lớp sang phòng khác nếu chưa có xác nhận vì có thể ảnh hưởng đến lớp tiếp theo. Ghi rõ môn học, thời điểm, mã phòng và nội dung thông báo để bộ phận phụ trách xử lý điều phối hoặc cập nhật dữ liệu lịch.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phòng học", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "CSVC_PHONGHOC",
                "Cách yêu cầu bố trí phòng cho hoạt động học thuật của sinh viên?",
                "Thông tin về quy mô, thời lượng và thiết bị cần nêu để đơn vị quản lý đánh giá yêu cầu.",
                """
                <p>Trong đề nghị, nêu tên hoạt động, đơn vị tổ chức, số người, thời gian chuẩn bị, thời gian sử dụng và nhu cầu như máy chiếu, bảng hoặc âm thanh. Kiểm tra hoạt động có cần phê duyệt của khoa hay đơn vị quản lý khác trước khi yêu cầu phòng hay không.</p><p>Gửi yêu cầu trước thời hạn quy định và chờ xác nhận. Nếu có thay đổi số lượng người tham gia hoặc khung thời gian, cập nhật sớm để đơn vị cân đối phòng. Người tổ chức chịu trách nhiệm bàn giao không gian sau khi kết thúc.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phòng học", "Cơ sở vật chất"}
        ));
        list.add(new FaqDefinition(
                "CSVC_PHONGHOC",
                "Làm gì khi phòng học thiếu bàn ghế hoặc thiết bị giảng dạy?",
                "Cách ghi nhận số lượng thiếu và phối hợp để hạn chế ảnh hưởng tới buổi học.",
                """
                <p>Ghi mã phòng, thời điểm lớp học diễn ra và số lượng bàn ghế hoặc thiết bị đang thiếu hay không hoạt động. Báo giảng viên hoặc nhân viên phụ trách tại chỗ để xác định phương án tạm thời phù hợp.</p><p>Đồng thời tạo yêu cầu cơ sở vật chất nếu sự cố cần xử lý lâu dài. Không tự mang thiết bị từ phòng khác khi chưa được cho phép. Sau buổi học, bổ sung mã phiếu hoặc thông tin xử lý để theo dõi việc khắc phục.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phòng học", "Sửa chữa trang thiết bị"}
        ));
        list.add(new FaqDefinition(
                "CSVC_THIETBI",
                "Cách đăng ký mượn thiết bị phục vụ hoạt động học tập?",
                "Các thông tin cần chuẩn bị khi mượn máy chiếu, micro hoặc thiết bị hỗ trợ theo quy trình đơn vị quản lý.",
                """
                <p>Xác định loại thiết bị, số lượng, thời gian nhận và hoàn trả, nơi sử dụng cùng người chịu trách nhiệm. Kiểm tra thiết bị thuộc kho nào và liệu hoạt động của bạn có cần xác nhận của giảng viên hoặc đơn vị tổ chức hay không.</p><p>Khi nhận thiết bị, kiểm tra tình trạng bên ngoài và phụ kiện đi kèm, báo ngay hư hỏng có sẵn. Trả thiết bị đúng thời gian và nhận xác nhận bàn giao để tránh nhầm trạng thái còn mượn trên hệ thống.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thiết bị", "Cơ sở vật chất"}
        ));
        list.add(new FaqDefinition(
                "CSVC_THIETBI",
                "Cách báo thiết bị trình chiếu không kết nối được với máy tính?",
                "Các bước kiểm tra dây kết nối, nguồn điện và cấu hình hiển thị trước khi gọi hỗ trợ.",
                """
                <p>Kiểm tra nguồn điện, đầu cắm, lựa chọn ngõ vào của màn hình hoặc máy chiếu và chế độ xuất hình trên máy tính. Thử dây hoặc cổng khác nếu có sẵn và được phép sử dụng, nhưng không kéo mạnh cáp hoặc tháo thiết bị cố định.</p><p>Nếu vẫn không hoạt động, báo mã phòng, loại thiết bị và dấu hiệu cụ thể cho kỹ thuật viên. Nêu rõ buổi học đang diễn ra để đơn vị biết mức độ ảnh hưởng. Không tự cài phần mềm không rõ nguồn lên máy tính dùng chung của trường.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thiết bị", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "CSVC_THIETBI",
                "Các bước báo mất hoặc thất lạc thiết bị được bàn giao gồm những gì?",
                "Thông tin cần cung cấp khi kiểm tra tài sản, thời điểm bàn giao và người chịu trách nhiệm.",
                """
                <p>Thông báo sớm cho đơn vị quản lý tài sản, nêu tên thiết bị, mã tài sản nếu có, lần cuối nhìn thấy và thông tin bàn giao gần nhất. Kiểm tra lại biên nhận, phòng sử dụng và các đầu mối có liên quan trước khi kết luận thiết bị bị mất.</p><p>Thực hiện theo quy trình kiểm kê và báo cáo sự cố của trường; không tự thay đổi hồ sơ bàn giao hoặc đưa ra cam kết bồi hoàn khi chưa có hướng dẫn. Giữ lại thông tin trao đổi để việc xác minh chính xác.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thiết bị", "Cơ sở vật chất"}
        ));
        list.add(new FaqDefinition(
                "CSVC_THIETBI",
                "Cách kiểm tra tình trạng thiết bị trước khi tổ chức sự kiện?",
                "Danh mục kiểm tra cơ bản cho âm thanh, trình chiếu, dây nguồn và phụ kiện trước giờ sử dụng.",
                """
                <p>Trước sự kiện, kiểm tra số lượng thiết bị theo phiếu mượn, tình trạng hoạt động, dây nguồn, đầu chuyển và pin phụ kiện. Thử trình chiếu nội dung mẫu và âm thanh ở mức phù hợp trong thời gian cho phép.</p><p>Cập nhật lỗi hoặc phụ kiện thiếu trước khi bắt đầu và báo kỹ thuật viên. Phân công một người chịu trách nhiệm theo dõi thiết bị trong sự kiện, không để thiết bị không người giám sát ở khu vực công cộng. Sau hoạt động, so sánh số lượng khi hoàn trả.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Thiết bị", "Cơ sở vật chất"}
        ));
        list.add(new FaqDefinition(
                "CSVC_SUACHUA_BAOHONG",
                "Phiếu báo hỏng tài sản cần ghi những thông tin gì?",
                "Cách mô tả rõ vị trí, thiết bị, tình trạng và ảnh hưởng của sự cố để tránh yêu cầu thiếu thông tin.",
                """
                <p>Một phiếu hữu ích nên có tòa nhà, tầng, phòng, mã hoặc tên thiết bị, dấu hiệu hỏng, thời điểm phát hiện và mức độ ảnh hưởng. Nếu có thể chụp ảnh an toàn, chọn ảnh thể hiện vị trí hoặc hiện trạng cần kiểm tra.</p><p>Không tháo vỏ thiết bị để chụp bên trong và không ghi thông tin phỏng đoán như nguyên nhân chắc chắn nếu chưa được kiểm tra. Sau khi gửi, lưu mã phiếu để bổ sung thông tin trong cùng yêu cầu khi được kỹ thuật viên hỏi thêm.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sửa chữa trang thiết bị", "Cơ sở vật chất"}
        ));
        list.add(new FaqDefinition(
                "CSVC_SUACHUA_BAOHONG",
                "Cách phân loại báo hỏng khẩn cấp và thông thường?",
                "Tiêu chí nhận diện sự cố có nguy cơ an toàn so với hư hỏng không gây nguy hiểm tức thời.",
                """
                <p>Sự cố có dấu hiệu gây nguy hiểm cho người hoặc có thể làm tình trạng xấu đi nhanh cần được báo trực tiếp ngay cho người phụ trách tại chỗ, đồng thời dùng kênh khẩn cấp nếu trường có quy định. Hư hỏng thông thường có thể gửi ticket, kèm mô tả và vị trí đầy đủ.</p><p>Nếu chưa chắc mức độ nguy hiểm, tránh tiếp xúc với thiết bị và xin hướng dẫn từ nhân viên có trách nhiệm. Không chỉ đánh dấu ưu tiên cao trên biểu mẫu rồi chờ phản hồi khi đang có nguy cơ trước mắt.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sửa chữa trang thiết bị", "Cơ sở vật chất"}
        ));
        list.add(new FaqDefinition(
                "CSVC_SUACHUA_BAOHONG",
                "Cách bổ sung ảnh và thông tin vào phiếu báo hỏng đã gửi?",
                "Cách cập nhật minh chứng vào yêu cầu đang xử lý thay vì tạo nhiều phiếu cho cùng sự cố.",
                """
                <p>Mở phiếu đang tồn tại bằng mã yêu cầu, dùng chức năng bổ sung nội dung hoặc phản hồi nếu hệ thống hỗ trợ. Chọn ảnh rõ, không chứa thông tin cá nhân không liên quan và chú thích ảnh thể hiện góc chụp hoặc bộ phận đang gặp lỗi.</p><p>Nếu không thể đính kèm tệp, nêu rõ lỗi tải lên và liên hệ đầu mối tiếp nhận để được hướng dẫn gửi theo kênh được chấp thuận. Giữ một mã phiếu chính cho sự cố để lịch sử trao đổi và phân công không bị tách rời.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sửa chữa trang thiết bị", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CSVC_SUACHUA_BAOHONG",
                "Cách xác nhận kết quả khắc phục sau khi báo hỏng?",
                "Các bước kiểm tra thiết bị sau sửa chữa và phản hồi nếu lỗi chưa được giải quyết đầy đủ.",
                """
                <p>Khi nhận thông báo hoàn thành, kiểm tra thiết bị trong điều kiện sử dụng bình thường nếu việc kiểm tra an toàn và được phép. So sánh với lỗi đã báo ban đầu, chẳng hạn chức năng hoạt động lại, tiếng ồn bất thường đã hết hoặc bộ phận được thay thế đã ổn định.</p><p>Nếu vẫn còn vấn đề, phản hồi ngay trên phiếu với dấu hiệu mới và thời điểm tái xuất hiện. Nếu đã khắc phục, xác nhận theo hướng dẫn để kết thúc yêu cầu. Không tự đánh dấu hoàn thành chỉ vì phiếu chuyển trạng thái nếu thực tế chưa được kiểm tra.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sửa chữa trang thiết bị", "Cơ sở vật chất"}
        ));
        list.add(new FaqDefinition(
                "CSVC_PHONGHOC_DANGKY",
                "Cách đăng ký phòng học nhóm theo khung thời gian cố định?",
                "Cách đề nghị sử dụng phòng định kỳ và các thông tin cần xác nhận trước khi được xếp lịch.",
                """
                <p>Ghi rõ mục đích học nhóm, số người, các ngày lặp lại, giờ bắt đầu và kết thúc, cùng khoảng thời gian cần sử dụng phòng. Kiểm tra quy định đăng ký định kỳ và thời hạn gửi yêu cầu của đơn vị quản lý phòng.</p><p>Chỉ xem lịch là hợp lệ sau khi nhận xác nhận chính thức. Nếu buổi học bị hủy hoặc thay đổi, cập nhật để tránh giữ phòng không sử dụng. Tuân thủ số người tối đa và nội quy không gian học tập trong suốt thời gian đăng ký.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phòng học", "Cơ sở vật chất"}
        ));
        list.add(new FaqDefinition(
                "CSVC_PHONGHOC_DANGKY",
                "Làm thế nào để đổi ngày giờ sau khi đã được cấp phòng?",
                "Các bước xin thay đổi lịch sử dụng phòng mà không ảnh hưởng tới những đơn vị đặt trước khác.",
                """
                <p>Mở thông tin yêu cầu đã được chấp thuận, ghi mã đăng ký và nêu ngày giờ cũ cùng lựa chọn thay thế. Gửi đề nghị thay đổi trước thời điểm sử dụng theo quy định để đơn vị quản lý kiểm tra lịch trống.</p><p>Không tự chuyển sang phòng khác khi chưa có xác nhận cập nhật. Theo dõi phản hồi và thông báo cho những người tham gia sau khi lịch mới được chấp thuận. Nếu không còn phòng phù hợp, hỏi đơn vị quản lý về lựa chọn thay thế khả dụng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phòng học", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "CSVC_PHONGHOC_DANGKY",
                "Cách bổ sung nhu cầu máy chiếu hoặc âm thanh vào đăng ký phòng?",
                "Thông tin thiết bị cần khai báo và cách xác nhận thiết bị có sẵn trong phòng hay phải mượn riêng.",
                """
                <p>Kiểm tra danh mục thiết bị được trang bị sẵn ở phòng dự kiến sử dụng. Nếu cần thêm máy chiếu, micro hoặc hệ thống âm thanh, nêu rõ số lượng, thời gian cần và người phụ trách sử dụng trong yêu cầu đăng ký.</p><p>Đợi xác nhận của đơn vị quản lý trước khi thông báo rằng thiết bị đã được bảo đảm. Nếu có phương án dự phòng, nêu rõ để dễ xử lý khi thiết bị không sẵn có. Kiểm tra kết nối trước giờ hoạt động và tuân thủ hướng dẫn bàn giao sau khi sử dụng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phòng học", "Thiết bị"}
        ));
        list.add(new FaqDefinition(
                "CSVC_PHONGHOC_DANGKY",
                "Cách hủy đăng ký phòng không còn nhu cầu sử dụng?",
                "Các bước giải phóng khung thời gian đã đặt và kiểm tra xác nhận hủy thành công.",
                """
                <p>Vào yêu cầu đặt phòng đã được xác nhận và thực hiện hủy theo hướng dẫn của hệ thống hoặc liên hệ đơn vị quản lý. Nêu mã đăng ký, ngày và khung thời gian để phòng có thể được phân bổ cho nhu cầu khác.</p><p>Kiểm tra trạng thái hủy hoặc nhận xác nhận qua kênh chính thức. Nếu lịch đã sát giờ, thông báo trực tiếp cho đầu mối quản lý để hạn chế phòng bị bỏ trống. Lưu thông tin hủy nếu cần so sánh lịch sử đăng ký.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phòng học", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QLCL_LICHTHI",
                "Cách tra cứu lịch thi chính thức của học kỳ?",
                "Cách kiểm tra phòng thi, ngày giờ, hình thức thi và thông tin cập nhật mới nhất.",
                """
                <p>Đăng nhập cổng sinh viên và mở mục lịch thi theo học kỳ, sau đó so sánh mã học phần, nhóm lớp, ngày, giờ, phòng thi và hình thức thi. Kiểm tra lại lịch gần ngày thi vì lịch có thể được cập nhật theo thông báo chính thức.</p><p>Nếu một học phần chưa có lịch, xem hướng dẫn của đơn vị phụ trách trước khi kết luận hệ thống bị lỗi. Khi thông tin trên lịch cá nhân khác thông báo của khoa, lưu ảnh chụp cả hai nguồn và gửi yêu cầu xác minh để tránh dựa trên lịch chưa được xác nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Lịch thi", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "QLCL_LICHTHI",
                "Làm gì khi lịch thi cá nhân thiếu một học phần?",
                "Các bước đối chiếu đăng ký dự thi, trạng thái học phần và yêu cầu xác nhận lịch thi.",
                """
                <p>Kiểm tra học phần có nằm trong danh sách đăng ký hợp lệ và các điều kiện dự thi đã được đáp ứng hay chưa. Xem thông báo từ khoa và đơn vị tổ chức thi để xác định lịch đã được công bố riêng hay chưa.</p><p>Nếu học phần vẫn không xuất hiện sau thời điểm công bố, gửi mã số sinh viên, mã học phần, nhóm lớp và ảnh màn hình lịch hiện tại tới đầu mối phụ trách. Không tự suy đoán phòng thi hoặc dựa vào lịch của bạn khác khi thông tin cá nhân chưa được xác nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Lịch thi", "Học vụ"}
        ));
        list.add(new FaqDefinition(
                "QLCL_LICHTHI",
                "Cách báo lịch thi bị trùng thời gian giữa hai học phần?",
                "Thông tin cần cung cấp để đơn vị phụ trách kiểm tra xung đột lịch thi.",
                """
                <p>Ghi lại mã của cả hai học phần, nhóm lớp, thời gian và nguồn hiển thị lịch. Kiểm tra xem một lịch có phải bản cũ hoặc lịch dự kiến hay không trước khi gửi yêu cầu.</p><p>Gửi thông tin cho đầu mối tổ chức thi theo hướng dẫn, nêu rõ hai ca thi bị trùng và đính kèm bằng chứng cần thiết. Không tự ý bỏ một buổi thi hoặc trao đổi đổi ca với sinh viên khác khi chưa có quyết định chính thức; theo dõi phản hồi để biết lịch đã được điều chỉnh hay chưa.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Lịch thi", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "QLCL_LICHTHI",
                "Cách xác minh phòng thi thay đổi trước ngày thi?",
                "Cách nhận biết thông báo thay đổi hợp lệ và kiểm tra phòng thi được cập nhật.",
                """
                <p>Kiểm tra thông báo trên cổng sinh viên và kênh chính thức của đơn vị tổ chức thi. So sánh mã học phần, ngày giờ và phòng được cập nhật; chú ý thời điểm phát hành để phân biệt lịch mới với bản lưu cũ.</p><p>Nếu hai nguồn chính thức chưa thống nhất, liên hệ cán bộ coi thi hoặc đơn vị tổ chức thi qua thông tin công bố để xác nhận. Không chia sẻ lại tin đồn về thay đổi phòng nếu chưa được xác minh, vì điều đó có thể khiến nhiều sinh viên đến nhầm địa điểm.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Lịch thi", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "QLCL_PHUCKHAO",
                "Cách nộp đơn phúc khảo sau khi điểm thi được công bố?",
                "Các bước kiểm tra hạn nộp, điền thông tin bài thi và lưu xác nhận tiếp nhận đơn.",
                """
                <p>Đọc thông báo điểm thi để xác định hạn phúc khảo, hình thức nộp và các khoản phí nếu có. Chọn đúng học phần, lần thi và thông tin bài thi trên biểu mẫu; trình bày yêu cầu rõ ràng theo trường thông tin được gửi.</p><p>Nộp đơn qua kênh chính thức và lưu mã tiếp nhận hoặc biên nhận. Không gửi đơn sau hạn mà không liên hệ đầu mối để hỏi chính sách áp dụng. Phúc khảo là quy trình kiểm tra theo quy định, không bảo đảm điểm sẽ tăng hoặc giữ nguyên.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phúc khảo", "Điểm thi"}
        ));
        list.add(new FaqDefinition(
                "QLCL_PHUCKHAO",
                "Cách theo dõi kết quả phúc khảo bài thi?",
                "Cách kiểm tra trạng thái xử lý và đọc thông báo kết quả sau khi đơn được tiếp nhận.",
                """
                <p>Lưu mã đơn, học phần và ngày nộp để tra cứu trạng thái. Theo dõi thông báo chính thức về thời gian công bố kết quả; nếu có yêu cầu bổ sung thông tin, thực hiện đúng hạn nêu trong thông báo.</p><p>Khi kết quả được công bố, so sánh điểm trước và sau phúc khảo cùng ghi chú nếu có. Nếu có thắc mắc về việc cập nhật điểm trên cổng sinh viên, gửi yêu cầu xác minh với thông tin bài thi và kết quả đã nhận, không tạo nhiều đơn trùng nhau.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phúc khảo", "Điểm thi"}
        ));
        list.add(new FaqDefinition(
                "QLCL_PHUCKHAO",
                "Phúc khảo điểm thi khác khiếu nại điểm quá trình như thế nào?",
                "Làm rõ hai loại yêu cầu có thể thuộc quy trình và đầu mối tiếp nhận khác nhau.",
                """
                <p>Phúc khảo thường liên quan đến bài thi theo quy trình dành riêng cho bài thi, trong khi điểm quá trình có thể gồm chuyên cần, bài tập, thực hành hoặc đánh giá thành phần. Kiểm tra hướng dẫn của học phần để chọn đúng quy trình và thời hạn tương ứng.</p><p>Chuẩn bị minh chứng phù hợp: thông tin bài thi đối với phúc khảo hoặc bảng điểm thành phần, bài nộp và trao đổi liên quan đối với điểm quá trình. Nếu chưa rõ đầu mối, hỏi giảng viên phụ trách hoặc đơn vị học vụ trước khi nộp để tránh dùng nhầm biểu mẫu.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phúc khảo", "Học vụ"}
        ));
        list.add(new FaqDefinition(
                "QLCL_PHUCKHAO",
                "Làm gì khi điểm sau phúc khảo chưa hiển thị trên bảng điểm?",
                "Cách đối chiếu thông báo kết quả với dữ liệu học vụ sau khi quy trình chấm lại hoàn tất.",
                """
                <p>Kiểm tra thông báo kết quả phúc khảo, học kỳ và thời gian cập nhật được đơn vị công bố. Nếu đã có kết quả nhưng bảng điểm cá nhân vẫn hiển thị giá trị cũ, lưu mã yêu cầu và ảnh chụp trang điểm để so sánh.</p><p>Gửi yêu cầu đến đầu mối quản lý điểm hoặc đơn vị tổ chức thi, nêu rõ học phần và ngày nhận thông báo kết quả. Không tự chỉnh sửa bảng điểm tải về để thay thế bản chính thức; chờ hệ thống được cập nhật hoặc nhận xác nhận từ đơn vị có thẩm quyền.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phúc khảo", "Bảng điểm"}
        ));
        list.add(new FaqDefinition(
                "QLCL_KHAOSAT",
                "Cách tham gia khảo sát chất lượng học phần?",
                "Cách truy cập biểu mẫu khảo sát, chọn đúng học phần và hoàn tất phản hồi.",
                """
                <p>Mở khảo sát từ cổng sinh viên hoặc đường dẫn chính thức được nhà trường thông báo. So sánh tên học phần, lớp học phần và học kỳ trước khi trả lời để bảo đảm phản hồi được cập nhật cho đúng đối tượng.</p><p>Đọc từng câu hỏi và gửi nhận xét dựa trên trải nghiệm thực tế, sử dụng ngôn ngữ cụ thể, tôn trọng. Sau khi gửi, kiểm tra thông báo hoàn tất nếu hệ thống gửi. Không gửi lại nhiều lần nếu chưa xác minh trạng thái bản phản hồi đầu tiên.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Khảo sát chất lượng", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "QLCL_KHAOSAT",
                "Cách gửi góp ý về nội dung giảng dạy trong khảo sát?",
                "Những yếu tố nên mô tả để phản hồi có thể dùng cho cải tiến học phần.",
                """
                <p>Góp ý hữu ích có thể nêu mức độ rõ ràng của đề cương, cách tổ chức bài giảng, khối lượng bài tập, tính phù hợp của tài liệu và cách đánh giá. Mô tả tình huống cụ thể thay vì chỉ dùng nhận xét chung, đồng thời phân biệt trải nghiệm cá nhân với kết luận về toàn bộ lớp.</p><p>Không đưa thông tin riêng tư của sinh viên khác vào câu trả lời. Nếu phản ánh một sự việc cần giải quyết riêng hoặc có bằng chứng kèm theo, sử dụng kênh tiếp nhận phù hợp thay vì chỉ dựa vào khảo sát tổng hợp.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Khảo sát chất lượng", "Học vụ"}
        ));
        list.add(new FaqDefinition(
                "QLCL_KHAOSAT",
                "Phản hồi khảo sát được sử dụng như thế nào và thông tin cá nhân có được bảo vệ không?",
                "Giải thích các điểm cần đọc trong thông báo về mục đích thu thập và phạm vi sử dụng dữ liệu khảo sát.",
                """
                <p>Trước khi tham gia, đọc phần giới thiệu để biết mục tiêu khảo sát, dữ liệu được thu thập và cách nhà trường sử dụng kết quả. Không giả định khảo sát ẩn danh nếu thông báo không nêu rõ điều đó.</p><p>Chỉ gửi nội dung liên quan đến câu hỏi và tránh ghi thông tin cá nhân không cần thiết. Nếu cần biết ai có quyền truy cập phản hồi hoặc thời gian lưu trữ dữ liệu, liên hệ đơn vị tổ chức khảo sát qua đầu mối chính thức.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Khảo sát chất lượng", "Chính sách hỗ trợ"}
        ));
        list.add(new FaqDefinition(
                "QLCL_KHAOSAT",
                "Làm gì khi khảo sát không ghi nhận câu trả lời đã gửi?",
                "Các bước kiểm tra xác nhận, trạng thái biểu mẫu và hỗ trợ khi có lỗi gửi dữ liệu.",
                """
                <p>Kiểm tra xem hệ thống có hiển thị thông báo hoàn tất hay mã xác nhận sau khi gửi. Nếu trang bị đóng hoặc mất kết nối trước khi nhận xác nhận, quay lại mục khảo sát để kiểm tra trạng thái trước khi điền lại.</p><p>Nếu vẫn chưa rõ phản hồi đã được cập nhật hay chưa, báo đơn vị tổ chức kèm thời gian thực hiện, tên khảo sát và ảnh lỗi nếu có. Tránh gửi nội dung phản hồi nhạy cảm qua kênh không phù hợp; chỉ chia sẻ mã khảo sát hoặc thông tin cần thiết để kiểm tra.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Khảo sát chất lượng", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "QLCL_LICHTHI_TRUNGLICH",
                "Cách báo xung đột lịch thi giữa hai nhóm lớp?",
                "Thông tin giúp đơn vị tổ chức thi xác định chính xác các lịch có khả năng chồng lấn.",
                """
                <p>Ghi mã học phần, nhóm lớp, ngày, giờ bắt đầu và kết thúc của cả hai lịch. Đính kèm ảnh từ nguồn chính thức và nêu rõ đây là lịch cá nhân hay lịch của nhóm lớp để tránh nhầm hai ca khác nhau.</p><p>Gửi báo cáo càng sớm càng tốt theo đầu mối được công bố. Theo dõi phản hồi và kiểm tra bản lịch mới trước ngày thi. Không tự chuyển sang ca khác hoặc chỉ dựa trên trao đổi giữa sinh viên khi chưa có xác nhận của đơn vị tổ chức.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Lịch thi", "Lỗi thường gặp"}
        ));
        list.add(new FaqDefinition(
                "QLCL_LICHTHI_TRUNGLICH",
                "Làm gì khi phát hiện lịch thi bị trùng sau khi lịch đã cập nhật?",
                "Cách so sánh phiên bản lịch và thông báo thay đổi để xác định xung đột mới phát sinh.",
                """
                <p>Kiểm tra ngày cập nhật của từng thông báo và so sánh lịch hiện tại với bản trước đó nếu còn lưu. Xác định lịch nào vừa thay đổi, thời gian chồng lấn thực tế và liệu một ca có được điều chỉnh cho nhóm lớp khác hay không.</p><p>Gửi cả hai nguồn cùng mô tả vấn đề cho đơn vị phụ trách. Khi nhận phản hồi, kiểm tra lại mã học phần và đối tượng áp dụng để bảo đảm điều chỉnh giải quyết đúng xung đột của bạn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Lịch thi", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "QLCL_LICHTHI_TRUNGLICH",
                "Cách phản hồi khi đơn vị yêu cầu làm rõ xung đột lịch thi?",
                "Các dữ liệu cần bổ sung để cán bộ có thể tái hiện và xác minh vấn đề lịch thi.",
                """
                <p>Nếu được hỏi thêm, gửi đầy đủ ngày thi, khung thời gian, mã học phần, nhóm lớp và trang lịch đã tra cứu. Nêu rõ múi giờ hoặc định dạng giờ nếu thông tin đến từ nhiều nguồn, đồng thời làm rõ ca nào đã được xác nhận chính thức.</p><p>Tránh chỉ gửi ảnh cắt mất ngày hoặc tên học phần vì khó kiểm chứng. Sau khi bổ sung, giữ mã yêu cầu ban đầu và chờ phản hồi trong cùng luồng trao đổi để lịch sử xử lý không bị chia thành nhiều yêu cầu.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Lịch thi", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QLCL_LICHTHI_TRUNGLICH",
                "Cách kiểm tra kết quả điều chỉnh sau khi báo lịch thi trùng?",
                "Cách xác nhận lịch cuối cùng đã được cập nhật đúng cho cá nhân và nhóm lớp liên quan.",
                """
                <p>Sau khi đơn vị thông báo đã điều chỉnh, mở lại lịch thi cá nhân và so sánh cả hai học phần bị xung đột. Kiểm tra ngày, giờ, phòng thi và nhóm lớp trên bản hiện hành, không chỉ dựa vào nội dung email cũ.</p><p>Nếu còn chồng lấn hoặc lịch cá nhân chưa đồng bộ, phản hồi trong mã yêu cầu cũ và chỉ rõ trường thông tin chưa đúng. Lưu thông báo kết quả để sử dụng khi cần xác minh, đồng thời chỉ chuẩn bị theo lịch đã được đơn vị có thẩm quyền xác nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Lịch thi", "Cổng thông tin"}
        ));
        list.add(new FaqDefinition(
                "QLCL_PHUCKHAO_HOSO",
                "Hồ sơ phúc khảo cần chuẩn bị những gì?",
                "Những dữ liệu cần kiểm tra để đơn được tiếp nhận đúng bài thi và đúng đợt công bố điểm.",
                """
                <p>Đọc thông báo phúc khảo của học kỳ để xác định biểu mẫu, mã học phần, nhóm lớp, đợt thi và thời hạn. Chuẩn bị thông tin định danh cần thiết cùng minh chứng được yêu cầu, không tự bổ sung giấy tờ ngoài danh mục nếu có thể chứa dữ liệu nhạy cảm.</p><p>Kiểm tra đơn đã điền đủ trường bắt buộc và đúng bài thi trước khi gửi. Lưu bản đơn cuối, biên nhận hoặc mã tiếp nhận để có thể tra cứu kết quả và bổ sung thông tin khi đơn vị xử lý yêu cầu.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phúc khảo", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QLCL_PHUCKHAO_HOSO",
                "Cách bổ sung minh chứng cho đơn phúc khảo đang xử lý?",
                "Các bước gửi thêm tài liệu vào hồ sơ hiện có và giữ liên kết với mã yêu cầu ban đầu.",
                """
                <p>Đọc thông báo yêu cầu bổ sung để biết loại minh chứng và định dạng cần gửi. Chuẩn bị tệp dễ đọc, đặt tên rõ ràng và bảo đảm nội dung liên quan trực tiếp đến bài thi hoặc thông tin trong đơn.</p><p>Gửi bổ sung trên cùng mã hồ sơ nếu hệ thống hỗ trợ, hoặc làm theo hướng dẫn của cán bộ tiếp nhận. Lưu xác nhận đã bổ sung. Không gửi bản chỉnh sửa làm thay đổi nội dung tài liệu gốc; nếu cần giải trình, viết rõ phần giải thích kèm theo.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phúc khảo", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QLCL_PHUCKHAO_HOSO",
                "Cách sửa sai thông tin học phần trên đơn phúc khảo?",
                "Cách yêu cầu điều chỉnh mã môn, nhóm lớp hoặc thông tin bài thi khi hồ sơ chưa được xử lý xong.",
                """
                <p>Nếu phát hiện thông tin sai sau khi nộp, liên hệ đầu mối phúc khảo ngay với mã đơn và nêu trường dữ liệu cần sửa. Cung cấp thông tin đúng theo lịch thi hoặc bảng điểm chính thức để cán bộ xác minh.</p><p>Không tự nộp nhiều đơn mới nếu đơn cũ vẫn đang xử lý, vì có thể tạo hồ sơ trùng. Sau khi được xác nhận cập nhật, kiểm tra thông tin trong hồ sơ hoặc lưu thư phản hồi làm bằng chứng so sánh.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phúc khảo", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "QLCL_PHUCKHAO_HOSO",
                "Cách theo dõi hồ sơ phúc khảo bị trả về để hoàn thiện?",
                "Cách đọc lý do trả hồ sơ và gửi lại đúng thành phần còn thiếu trong hạn.",
                """
                <p>Đọc kỹ lý do trả hồ sơ, phân biệt thiếu trường thông tin, sai mã học phần, hết hạn tiếp nhận hay tệp không đúng định dạng. Sửa đúng nội dung được nêu và tránh thay đổi các phần không liên quan.</p><p>Kiểm tra thời hạn hoàn thiện và gửi lại theo hướng dẫn chính thức. Nếu không hiểu lý do hoặc cho rằng thông tin đã đủ, liên hệ đầu mối cùng mã đơn để được giải thích trước khi nộp lại. Lưu cả thông báo trả hồ sơ và xác nhận gửi lại.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Phúc khảo", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_TUYENSINH",
                "Cách tra cứu điều kiện dự tuyển chương trình sau đại học?",
                "Những điều cần kiểm tra trong thông báo tuyển sinh như văn bằng, ngành phù hợp và ngoại ngữ.",
                """
                <p>Đọc thông báo tuyển sinh chính thức của chương trình và xác định điều kiện về văn bằng, ngành đào tạo, kinh nghiệm nếu có, ngoại ngữ cùng các yêu cầu bổ sung. Điều kiện cụ thể khác nhau theo chương trình và từng kỳ tuyển sinh nên cần dùng thông báo hiện hành.</p><p>Nếu ngành tốt nghiệp không trùng hoàn toàn với ngành dự tuyển, kiểm tra danh mục ngành phù hợp hoặc yêu cầu học bổ sung kiến thức. Liên hệ bộ phận tuyển sinh sau đại học trước hạn nếu cần xác nhận tính tương thích của văn bằng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Tuyển sinh"}
        ));
        list.add(new FaqDefinition(
                "SDH_TUYENSINH",
                "Cách chọn ngành hoặc chuyên ngành dự tuyển sau đại học?",
                "Cách đối chiếu mục tiêu học tập, nền tảng chuyên môn và định hướng nghiên cứu với chương trình.",
                """
                <p>Đọc mục tiêu đào tạo, danh sách học phần, hướng nghiên cứu và yêu cầu luận văn của từng chương trình. So sánh những nội dung đó với nền tảng đã học và mục tiêu nghề nghiệp để xác định lựa chọn phù hợp.</p><p>Nếu chưa rõ hướng chuyên môn, xem thông tin giảng viên, nhóm nghiên cứu và các chủ đề có thể thực hiện. Chuẩn bị câu hỏi cụ thể cho buổi tư vấn tuyển sinh thay vì chỉ dựa vào tên ngành hoặc thông tin quảng bá ngắn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Tuyển sinh"}
        ));
        list.add(new FaqDefinition(
                "SDH_TUYENSINH",
                "Trong kỳ tuyển sinh sau đại học, cần lưu ý những mốc thời gian nào?",
                "Những thời điểm quan trọng từ mở đăng ký đến xét tuyển, công bố kết quả và nhập học.",
                """
                <p>Lập danh sách các mốc được thông báo gồm hạn đăng ký trực tuyến, hạn nộp hồ sơ giấy nếu có, lịch thi hoặc phỏng vấn, thời điểm công bố kết quả và ngày nhập học. Đặt nhắc lịch cá nhân và kiểm tra cập nhật chính thức từ đơn vị tuyển sinh.</p><p>Không sử dụng lịch của kỳ trước để suy ra hạn năm nay. Nếu lịch thay đổi, lưu thông báo mới nhất và kiểm tra những mốc liên quan đến hồ sơ của mình, đặc biệt là thời hạn bổ sung giấy tờ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Thời hạn"}
        ));
        list.add(new FaqDefinition(
                "SDH_TUYENSINH",
                "Lệ phí dự tuyển là bao nhiêu và có thể thanh toán bằng cách nào?",
                "Cách kiểm tra mức phí được công bố, thông tin thanh toán và cách lưu chứng từ.",
                """
                <p>Tra cứu mức lệ phí và phương thức thanh toán trong thông báo tuyển sinh hoặc cổng đăng ký chính thức. Kiểm tra nội dung chuyển khoản, mã hồ sơ và thời hạn thanh toán trước khi thực hiện.</p><p>Lưu biên lai và trạng thái thanh toán sau khi giao dịch hoàn tất. Nếu đã thanh toán nhưng hồ sơ vẫn chưa cập nhật, gửi mã đăng ký, thời gian giao dịch và chứng từ đã che thông tin không cần thiết để bộ phận tuyển sinh kiểm tra.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOSO",
                "Hồ sơ dự tuyển sau đại học cần những giấy tờ nào?",
                "Cách lập bảng kiểm các giấy tờ theo yêu cầu của chương trình và tình trạng bản sao hoặc bản chính.",
                """
                <p>So sánh danh mục trong thông báo tuyển sinh, có thể gồm đơn dự tuyển, văn bằng, bảng điểm, giấy tờ định danh, chứng chỉ ngoại ngữ và tài liệu khác tùy chương trình. Kiểm tra yêu cầu công chứng, xác thực hoặc bản chính để so sánh.</p><p>Đánh dấu từng giấy tờ đã có, còn thiếu và cần xin cấp lại. Xem thời hạn hiệu lực của chứng chỉ nếu có yêu cầu. Giữ một bản sao bộ hồ sơ hoàn chỉnh và xác nhận tiếp nhận sau khi nộp.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOSO",
                "Làm gì khi văn bằng hoặc bảng điểm chưa có bản hợp lệ?",
                "Cách xác định loại giấy tờ tạm thời được chấp nhận và xin cách trước hạn nộp.",
                """
                <p>Kiểm tra thông báo tuyển sinh xem có chấp nhận giấy chứng nhận tốt nghiệp tạm thời, bản sao có xác thực hoặc bản điện tử hợp lệ hay không. Không tự cho rằng bản chụp thông thường thay thế được giấy tờ theo yêu cầu.</p><p>Nếu cơ sở đào tạo cũ chưa cấp văn bằng, liên hệ đầu mối tuyển sinh để hỏi phương án và hạn bổ sung được phép. Lưu phản hồi bằng văn bản để bảo đảm hồ sơ được xử lý theo cùng một hướng dẫn.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOSO",
                "Cách cập nhật thông tin cá nhân trong hồ sơ dự tuyển?",
                "Các bước sửa họ tên, ngày sinh, số liên hệ hoặc thông tin văn bằng đã khai báo.",
                """
                <p>Phát hiện thông tin sai thì báo đơn vị tuyển sinh trước khi hồ sơ chuyển sang giai đoạn xét duyệt tiếp theo. Nêu rõ trường thông tin hiện tại, nội dung cần sửa và tài liệu hợp lệ dùng để so sánh.</p><p>Không tạo tài khoản đăng ký mới để thay thế hồ sơ đang tồn tại nếu chưa được hướng dẫn, vì có thể gây trùng hồ sơ. Sau khi được cập nhật, kiểm tra bản xác nhận hoặc thông tin hiển thị để chắc rằng dữ liệu đã chính xác.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOSO",
                "Cách theo dõi hồ sơ dự tuyển đang chờ xác minh?",
                "Cách đọc trạng thái hồ sơ và xử lý khi đơn vị tuyển sinh yêu cầu kiểm tra thêm tài liệu.",
                """
                <p>Đăng nhập cổng tuyển sinh và kiểm tra trạng thái cùng ghi chú của hồ sơ. Trạng thái chờ xác minh có thể cho biết đơn vị cần so sánh văn bằng, dữ liệu thanh toán hoặc thành phần tài liệu; đọc nội dung cụ thể trước khi hành động.</p><p>Nếu không có ghi chú nhưng trạng thái kéo dài vượt thời gian thông báo, gửi mã hồ sơ đến đầu mối tuyển sinh để hỏi tiến độ. Không gửi lại toàn bộ tài liệu qua nhiều kênh khi chưa rõ yêu cầu, nhằm tránh phân tán dữ liệu và hồ sơ trùng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOCVU",
                "Cách đăng ký học phần trong chương trình sau đại học?",
                "Các bước kiểm tra kế hoạch học tập, tiên quyết và lịch học trước khi đăng ký môn.",
                """
                <p>Tra cứu kế hoạch học tập của chương trình, danh sách học phần mở trong kỳ, môn tiên quyết và các lớp có thể đăng ký. So sánh lịch cá nhân để tránh trùng lịch học hoặc các hoạt động bắt buộc khác.</p><p>Sau khi gửi đăng ký, kiểm tra danh sách học phần đã cập nhật và thời gian điều chỉnh nếu có. Nếu môn cần học không xuất hiện, liên hệ bộ phận quản lý chương trình để xác minh điều kiện tiên quyết hoặc kế hoạch mở lớp.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Học vụ"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOCVU",
                "Thủ tục xin tạm dừng hoặc bảo lưu kết quả học tập sau đại học gồm những gì?",
                "Các vấn đề cần hỏi về điều kiện, thời hạn, ảnh hưởng tiến độ và hồ sơ xin bảo lưu.",
                """
                <p>Đọc quy định đào tạo sau đại học và xác định lý do, thời gian dự kiến tạm dừng, tình trạng học phần và tiến độ luận văn. Liên hệ bộ phận quản lý chương trình để biết điều kiện áp dụng và biểu mẫu cần có.</p><p>Trước khi nộp, hỏi rõ thời gian bảo lưu có ảnh hưởng đến thời hạn tối đa hoàn thành chương trình hay các học phần đã tích lũy hay không. Lưu quyết định được phê duyệt vì yêu cầu trao đổi miệng không thay thế xác nhận chính thức.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Học vụ"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOCVU",
                "Cách đăng ký đề cương và người hướng dẫn luận văn?",
                "Các nội dung cần thống nhất về chủ đề, phạm vi nghiên cứu, giảng viên và mốc nộp đề cương.",
                """
                <p>Tham khảo định hướng nghiên cứu của chương trình, xác định vấn đề có thể triển khai và trao đổi với giảng viên có chuyên môn phù hợp. Đề cương hãy nêu mục tiêu, câu hỏi nghiên cứu, phương pháp, nguồn dữ liệu và kế hoạch thực hiện.</p><p>Kiểm tra biểu mẫu, chữ ký và thời hạn trình duyệt theo lịch của chương trình. Khi có thay đổi chủ đề hoặc người hướng dẫn, hỏi đơn vị quản lý về quy trình phê duyệt lại để hồ sơ và tiến độ đào tạo được cập nhật hợp lệ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Nghiên cứu khoa học"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOCVU",
                "Làm gì khi kết quả học phần sau đại học chưa được cập nhật?",
                "Cách đối chiếu thông báo kết quả, học phần và dữ liệu trên hệ thống quản lý học tập.",
                """
                <p>So sánh học phần, lớp, học kỳ và thông báo kết quả được giảng viên hoặc chương trình công bố. Nếu điểm đã có nhưng hồ sơ học tập chưa cập nhật, lưu thông tin thông báo và ảnh chụp màn hình hiện tại.</p><p>Liên hệ bộ phận quản lý đào tạo theo kênh chính thức, gửi mã học viên, học phần và thời điểm công bố. Không tự thay đổi bảng điểm tải về hoặc coi bản ghi chưa xác nhận là kết quả chính thức; chờ đơn vị có thẩm quyền kiểm tra và cập nhật.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Học vụ"}
        ));
        list.add(new FaqDefinition(
                "SDH_TUYENSINH_HOSO",
                "Cách kiểm tra hồ sơ trực tuyến trước khi xác nhận nộp?",
                "Các bước rà soát dữ liệu khai báo, tệp đính kèm và xác nhận cam kết trước khi gửi hồ sơ.",
                """
                <p>Rà soát họ tên, thông tin liên hệ, chương trình dự tuyển, quá trình học tập và các trường bắt buộc. Mở từng tệp đính kèm để chắc rằng đúng giấy tờ, đủ trang, dễ đọc và không bị đặt nhầm vào mục khác.</p><p>Đọc phần cam kết và xác nhận chỉ khi thông tin khai báo trung thực. Sau khi gửi, tải hoặc lưu bản xác nhận nếu cổng tuyển sinh gửi. Không coi hồ sơ đã hoàn tất chỉ vì đã tạo tài khoản hoặc lưu bản nháp.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tuyển sinh", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_TUYENSINH_HOSO",
                "Cách bổ sung giấy tờ dự tuyển theo yêu cầu của hội đồng?",
                "Cách gửi tài liệu thiếu đúng mã hồ sơ và theo dõi xác nhận tiếp nhận bổ sung.",
                """
                <p>Đọc nội dung yêu cầu để xác định đúng giấy tờ còn thiếu, hình thức bản sao và hạn bổ sung. Kiểm tra tài liệu trước khi tải lên hoặc nộp trực tiếp theo hướng dẫn đã công bố.</p><p>Ghi mã hồ sơ trong phản hồi và giữ xác nhận đã gửi tài liệu. Nếu tài liệu chưa thể cấp trước hạn, liên hệ đơn vị tuyển sinh để hỏi phương án được chấp nhận thay vì tự thay thế bằng tài liệu khác chưa được xác minh.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tuyển sinh", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_TUYENSINH_HOSO",
                "Làm gì khi tệp văn bằng tải lên không đạt yêu cầu?",
                "Cách kiểm tra định dạng, dung lượng, độ rõ và tính đầy đủ của tệp trước khi tải lại.",
                """
                <p>Kiểm tra loại tệp được chấp nhận, giới hạn dung lượng và yêu cầu màu sắc hoặc số mặt giấy. Quét hoặc chụp đầy đủ các trang có thông tin cần thiết, bảo đảm chữ rõ và không bị cắt mép.</p><p>Đặt tên tệp thể hiện loại giấy tờ, tránh ký tự gây lỗi và tải lại đúng mục được yêu cầu. Nếu hệ thống tiếp tục báo lỗi, ghi lại thông báo và gửi cho hỗ trợ tuyển sinh; không gửi tài liệu có thông tin cá nhân qua đường dẫn không chính thức.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tuyển sinh", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_TUYENSINH_HOSO",
                "Cách xác minh đã nộp đủ hồ sơ dự tuyển?",
                "Dấu hiệu nhận biết hồ sơ được tiếp nhận và cách lưu lại bằng chứng gửi thành công.",
                """
                <p>Kiểm tra trạng thái cổng tuyển sinh, danh mục tài liệu đã nhận và thông báo xác nhận qua email nếu có. Phân biệt trạng thái đã gửi, đã tiếp nhận và đủ điều kiện dự tuyển vì mỗi trạng thái có thể yêu cầu các bước kiểm tra khác nhau.</p><p>Lưu mã hồ sơ cùng ngày nộp và bản xác nhận. Nếu trạng thái chưa rõ, liên hệ đầu mối tuyển sinh kèm mã hồ sơ để hỏi thành phần còn thiếu. Không gửi lại bộ hồ sơ đầy đủ trước khi xác minh vì có thể tạo bản ghi trùng.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Tuyển sinh", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOSO_BOSUNG",
                "Cách xử lý thông báo hồ sơ sau đại học cần bổ sung?",
                "Các bước đọc yêu cầu, xác định hạn và nộp đúng thành phần đang thiếu.",
                """
                <p>Đọc từng mục trong thông báo bổ sung, ghi lại tài liệu phải nộp, hình thức chứng thực và thời hạn. So sánh với bản hồ sơ đã gửi để xác định phần chưa có hoặc chưa đáp ứng yêu cầu, tránh thay thế toàn bộ hồ sơ khi chỉ thiếu một tài liệu.</p><p>Nộp bổ sung theo đúng mã hồ sơ và lưu lại xác nhận đã tiếp nhận. Nếu có khó khăn khách quan khiến không thể hoàn thiện đúng hạn, liên hệ đơn vị tuyển sinh trước hạn để hỏi có phương án nào được chấp nhận theo quy định hay không.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOSO_BOSUNG",
                "Làm gì khi đã nộp hồ sơ nhưng tải nhầm giấy tờ?",
                "Cách xử lý khi bạn đã nộp hồ sơ nhưng tải giấy tờ vào nhầm mục.",
                """
                <p>Chuẩn bị mã hồ sơ, xác định giấy tờ đã tải nhầm và mục cần sửa. Kiểm tra xem cổng tuyển sinh có cho phép chỉnh sửa sau khi nộp hay bạn cần liên hệ cán bộ phụ trách để được mở quyền cập nhật.</p>
                <p>Đừng tạo hồ sơ thứ hai hoặc xóa hồ sơ hiện tại khi chưa được hướng dẫn. Sau khi thay tệp, kiểm tra lại giấy tờ và lưu xác nhận cập nhật. Nếu không còn quyền chỉnh sửa, liên hệ đầu mối tuyển sinh và gửi tài liệu qua kênh bảo mật được đơn vị chấp nhận.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOSO_BOSUNG",
                "Cách bổ sung bản dịch hoặc xác thực văn bằng theo quy định?",
                "Những điểm cần xác minh về ngôn ngữ, chứng thực và yêu cầu đối với giấy tờ học tập nước ngoài.",
                """
                <p>So sánh yêu cầu trong thông báo tuyển sinh đối với văn bằng hoặc bảng điểm bằng tiếng nước ngoài, bao gồm bản dịch, cơ quan chứng thực và định dạng bản sao. Không tự giả định bản dịch không chứng thực được chấp nhận.</p><p>Nếu chưa rõ loại xác thực cần thiết, gửi câu hỏi đến đơn vị tuyển sinh trước khi thực hiện để tránh phải làm lại giấy tờ. Theo dõi thời hạn bổ sung và lưu cả bản gốc, bản dịch cùng xác nhận đã nộp theo yêu cầu.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Hồ sơ"}
        ));
        list.add(new FaqDefinition(
                "SDH_HOSO_BOSUNG",
                "Cách theo dõi kết quả kiểm tra hồ sơ sau khi đã bổ sung?",
                "Cách xác nhận bộ hồ sơ đã đủ thành phần và đã chuyển sang bước xét tuyển tiếp theo.",
                """
                <p>Sau khi bổ sung, kiểm tra trạng thái hồ sơ và danh mục tài liệu được đánh dấu đã nhận. Nếu trạng thái vẫn là cần bổ sung, đọc lại ghi chú để xác định có tài liệu khác chưa đạt yêu cầu hay bản cập nhật chưa được cập nhật.</p><p>Nếu không có ghi chú rõ ràng, liên hệ đơn vị tuyển sinh với mã hồ sơ và thời điểm bổ sung. Lưu phản hồi xác nhận để so sánh nếu trạng thái trên cổng chưa đổi. Chỉ dựa vào thông báo chính thức để xác định hồ sơ đủ điều kiện chuyển bước tiếp theo.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Sau đại học", "Hồ sơ"}
        ));
        return list;
    }
}
