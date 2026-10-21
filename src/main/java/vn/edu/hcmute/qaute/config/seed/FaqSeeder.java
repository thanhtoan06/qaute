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

@Component
public class FaqSeeder implements DemoSeeder {

    private final FaqRepository faqRepository;
    private final FaqTagRepository faqTagRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public FaqSeeder(FaqRepository faqRepository,
                     FaqTagRepository faqTagRepository,
                     CategoryRepository categoryRepository,
                     UserRepository userRepository) {
        this.faqRepository = faqRepository;
        this.faqTagRepository = faqTagRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public int order() {
        return 30;
    }

    @Override
    @Transactional
    public void seed() {
        if (faqRepository.count() > 0) {
            return;
        }

        User admin = userRepository.findByEmailIgnoreCase("admin@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy admin demo"));
        User manager1 = userRepository.findByEmailIgnoreCase("manager1@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy manager1 demo"));
        User manager2 = userRepository.findByEmailIgnoreCase("manager2@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy manager2 demo"));
        User manager3 = userRepository.findByEmailIgnoreCase("manager3@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy manager3 demo"));
        User manager4 = userRepository.findByEmailIgnoreCase("manager4@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy manager4 demo"));

        Map<String, Category> catMap = new HashMap<>();
        String[] catCodes = {
            "DT_DKHP", "DT_HOCVU", "DT_TOTNGHIEP",
            "CT_HOCBONG", "CT_RENLUYEN",
            "TC_HOCPHI", "TC_MIENGIAM",
            "KTX_DANGKY", "KTX_DICHVU"
        };
        for (String code : catCodes) {
            Category cat = categoryRepository.findByCode(code)
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy chuyên mục demo: " + code));
            catMap.put(code, cat);
        }

        // Tạo pool các thẻ tag phổ biến
        String[] tagNames = {
            "Học phần", "Đăng ký môn", "Thời khóa biểu", "Học vụ", "Điểm thi", "Bảng điểm",
            "Xét tốt nghiệp", "Chứng chỉ", "Chuẩn đầu ra", "Học bổng", "Khuyến khích học tập",
            "Điểm rèn luyện", "Hoạt động phong trào", "Học phí", "Hạn nộp học phí", "Thanh toán online",
            "Miễn giảm học phí", "Chính sách hỗ trợ", "Ký túc xá", "Đăng ký nội trú", "Dịch vụ sinh viên",
            "Sửa chữa trang thiết bị", "Cổng thông tin", "Sinh viên HCMUTE", "Hướng dẫn học vụ"
        };
        List<FaqTag> allTags = new ArrayList<>();
        for (String tagName : tagNames) {
            allTags.add(getOrCreateTag(tagName));
        }

        Random random = new Random(42);
        LocalDateTime now = LocalDateTime.now();

        List<FaqDefinition> definitions = createFaqDefinitions();

        for (int i = 0; i < definitions.size(); i++) {
            FaqDefinition def = definitions.get(i);
            Category category = catMap.get(def.categoryCode);
            User author = getManagerForCategory(def.categoryCode, manager1, manager2, manager3, manager4);

            int viewCount = random.nextInt(401);
            int helpfulCount = random.nextInt(41);
            int notHelpfulCount = random.nextInt(9);

            LocalDateTime publishTime = now.minusDays(random.nextInt(60) + 1).minusHours(random.nextInt(24));
            LocalDateTime pubAt = def.status == FaqStatus.PUBLISHED || def.status == FaqStatus.ARCHIVED ? publishTime : null;

            String baseSlug = SlugUtil.slugify(def.title);
            String slug = baseSlug;
            int counter = 1;
            while (faqRepository.existsBySlug(slug)) {
                slug = baseSlug + "-" + counter++;
            }

            // Chọn ngẫu nhiên 5 đến 8 thẻ tag phù hợp
            Set<FaqTag> tags = pickTags(allTags, def.preferredTags, random, 5, 8);

            Faq faq = Faq.builder()
                    .categoryId(category.getId())
                    .authorId(author.getId())
                    .reviewerId(def.status == FaqStatus.PUBLISHED || def.status == FaqStatus.ARCHIVED ? admin.getId() : null)
                    .title(def.title)
                    .slug(slug)
                    .summary(def.summary)
                    .bodyHtml(def.bodyHtml)
                    .status(def.status)
                    .featured(def.featured)
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

    private User getManagerForCategory(String code, User m1, User m2, User m3, User m4) {
        if (code.startsWith("DT_")) return m1;
        if (code.startsWith("CT_")) return m2;
        if (code.startsWith("TC_")) return m3;
        return m4;
    }

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

    private Set<FaqTag> pickTags(List<FaqTag> allTags, String[] preferredNames, Random random, int min, int max) {
        Set<FaqTag> result = new HashSet<>();
        if (preferredNames != null) {
            for (String pName : preferredNames) {
                String pSlug = SlugUtil.slugify(pName);
                for (FaqTag t : allTags) {
                    if (t.getSlug().equals(pSlug)) {
                        result.add(t);
                    }
                }
            }
        }
        int targetSize = min + random.nextInt(max - min + 1);
        List<FaqTag> shuffled = new ArrayList<>(allTags);
        Collections.shuffle(shuffled, random);
        for (FaqTag tag : shuffled) {
            if (result.size() >= targetSize) {
                break;
            }
            result.add(tag);
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

    private List<FaqDefinition> createFaqDefinitions() {
        List<FaqDefinition> list = new ArrayList<>();

        // 1. DT_DKHP
        list.add(new FaqDefinition(
                "DT_DKHP",
                "Cách đăng ký học phần trên cổng thông tin sinh viên",
                "Hướng dẫn từng bước đăng ký môn học, điều chỉnh học phần và xem thời khóa biểu trên hệ thống portal HCMUTE.",
                """
                <p>Để thực hiện đăng ký học phần trực tuyến, sinh viên thực hiện theo các bước sau:</p>
                <ol>
                    <li>Đăng nhập cổng thông tin đào tạo với tài khoản sinh viên được cấp.</li>
                    <li>Truy cập mục <strong>Đăng ký học phần</strong> trong thời gian mở cổng quy định của từng khóa.</li>
                    <li>Chọn học phần theo kế hoạch đào tạo hoặc tra cứu theo mã môn học.</li>
                    <li>Chọn nhóm lớp học phần phù hợp và nhấn nút <strong>Lưu đăng ký</strong>.</li>
                    <li>Kiểm tra lại thời khóa biểu cá nhân tại mục <em>Lịch học học kỳ</em> để tránh trùng lịch.</li>
                </ol>
                <p>Lưu ý: Sinh viên cần kiểm tra kỹ điều kiện tiên quyết trước khi đăng ký để tránh bị hủy môn học tự động.</p>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Đăng ký môn", "Học phần", "Thời khóa biểu", "Cổng thông tin", "Sinh viên HCMUTE"}
        ));

        list.add(new FaqDefinition(
                "DT_DKHP",
                "Xử lý khi bị trùng lịch học hoặc lớp học phần đã đầy sĩ số",
                "Các phương án giải quyết khi gặp tình trạng lớp học phần kín chỗ hoặc trùng giờ với các môn học bắt buộc.",
                """
                <p>Khi gặp sự cố lớp học phần hết chỗ hoặc trùng lịch:</p>
                <ul>
                    <li><strong>Tìm lớp thay thế:</strong> Tìm các nhóm lớp học phần khác của cùng môn học còn sĩ số trống.</li>
                    <li><strong>Đăng ký môn học tương đương:</strong> Xem danh mục môn học tương đương hoặc thay thế trong chương trình đào tạo.</li>
                    <li><strong>Làm đơn xin mở thêm lớp:</strong> Đại diện ban cán sự lớp hoặc nhóm sinh viên gửi đơn trực tuyến tới Phòng Đào tạo đề nghị mở thêm nhóm học phần mới.</li>
                </ul>
                <p>Mọi thắc mắc cần hỗ trợ khẩn cấp trong đợt đăng ký, sinh viên vui lòng tạo yêu cầu tư vấn trên hệ thống QAUTE.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Đăng ký môn", "Học phần", "Thời khóa biểu", "Hướng dẫn học vụ"}
        ));

        list.add(new FaqDefinition(
                "DT_DKHP",
                "Quy định về số tín chỉ tối thiểu và tối đa được đăng ký trong một học kỳ",
                "Quy chế tín chỉ quy định mức tải học tập tối thiểu và tối đa cho sinh viên học lực bình thường và cảnh báo học vụ.",
                """
                <p>Theo quy chế đào tạo đại học theo học chế tín chỉ của nhà trường:</p>
                <ul>
                    <li>Sinh viên xếp loại học lực bình thường được đăng ký tối thiểu <strong>14 tín chỉ</strong> và tối đa <strong>24 tín chỉ</strong> trong một học kỳ chính.</li>
                    <li>Sinh viên đang bị cảnh báo học vụ chỉ được phép đăng ký tối đa <strong>14 tín chỉ</strong> để tập trung cải thiện kết quả học tập.</li>
                    <li>Trong học kỳ hè, số tín chỉ tối đa được phép đăng ký là <strong>8 tín chỉ</strong>.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phần", "Đăng ký môn", "Học vụ"}
        ));

        list.add(new FaqDefinition(
                "DT_DKHP",
                "Hướng dẫn làm đơn xin mở thêm lớp học phần hoặc tăng sĩ số",
                "Biểu mẫu và quy trình đề xuất mở thêm lớp học phần ngoài kế hoạch khi có đủ số lượng sinh viên có nguyện vọng.",
                """
                <p>Trường hợp cần đề xuất mở thêm nhóm học phần:</p>
                <p>Sinh viên cần tập hợp danh sách tối thiểu 20 sinh viên cùng có nguyện vọng và gửi đơn trực tuyến qua cổng QAUTE cho chuyên viên phụ trách khoa.</p>
                """,
                FaqStatus.DRAFT, false,
                new String[]{"Học phần", "Đăng ký môn"}
        ));

        // 2. DT_HOCVU
        list.add(new FaqDefinition(
                "DT_HOCVU",
                "Quy trình xin phúc khảo bài thi kết thúc học phần và thời hạn tiếp nhận",
                "Thủ tục nộp đơn phúc khảo điểm thi học kỳ, lệ phí và thời gian công bố kết quả chấm phúc khảo.",
                """
                <p>Khi nghi ngờ có sai sót về điểm thi kết thúc học phần, sinh viên có quyền làm đơn phúc khảo:</p>
                <ol>
                    <li><strong>Thời hạn:</strong> Trong vòng 07 ngày làm việc kể từ ngày công bố điểm chính thức trên cổng thông tin.</li>
                    <li><strong>Hình thức:</strong> Nộp đơn phúc khảo trực tuyến trên cổng sinh viên hoặc tại Văn phòng Đào tạo.</li>
                    <li><strong>Lệ phí:</strong> Nộp lệ phí theo quy định (sẽ được hoàn trả nếu kết quả phúc khảo làm tăng điểm).</li>
                    <li><strong>Kết quả:</strong> Được hội đồng chấm lại và công bố trong thời hạn 15 ngày làm việc.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Điểm thi", "Học vụ", "Bảng điểm", "Hướng dẫn học vụ"}
        ));

        list.add(new FaqDefinition(
                "DT_HOCVU",
                "Điều kiện và thủ tục xin tạm hoãn thi hoặc thi bổ sung",
                "Hồ sơ minh chứng y tế, hoàn cảnh gia đình hoặc sự cố bất khả kháng để được bảo lưu và thi bổ sung vào đợt kế tiếp.",
                """
                <p>Sinh viên vì lý do ốm đau, tai nạn hoặc sự kiện bất khả kháng không thể dự thi kết thúc học phần:</p>
                <ul>
                    <li>Cần làm đơn xin hoãn thi kèm theo giấy xác nhận của bệnh viện tuyến quận/huyện trở lên.</li>
                    <li>Nộp đơn về Văn phòng Đào tạo chậm nhất 03 ngày làm việc sau ngày diễn ra buổi thi.</li>
                    <li>Điểm hoãn thi được ký hiệu chữ 'I' và sinh viên được sắp xếp thi bổ sung vào đợt thi gần nhất.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điểm thi", "Học vụ", "Hướng dẫn học vụ"}
        ));

        list.add(new FaqDefinition(
                "DT_HOCVU",
                "Quy chế cảnh báo học vụ và buộc thôi học áp dụng theo học chế tín chỉ",
                "Các mức cảnh báo học vụ dựa trên điểm trung bình tích lũy và số tín chỉ nợ đọng, các biện pháp hỗ trợ sinh viên.",
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
                "Cách tra cứu bảng điểm quá trình và bảng điểm tích lũy học tập",
                "Hướng dẫn xem điểm thi thành phần, điểm thi kết thúc học phần và điểm trung bình tích lũy thang điểm 4 và thang 10.",
                """
                <p>Sinh viên đăng nhập portal để tra cứu điểm thi:</p>
                <p>Vào mục Tra cứu học vụ -> Bảng điểm cá nhân để xem điểm chi tiết các môn đã học.</p>
                """,
                FaqStatus.PENDING_REVIEW, false,
                new String[]{"Bảng điểm", "Điểm thi", "Học vụ"}
        ));

        // 3. DT_TOTNGHIEP
        list.add(new FaqDefinition(
                "DT_TOTNGHIEP",
                "Điều kiện chuẩn đầu ra ngoại ngữ và tin học để xét tốt nghiệp",
                "Danh mục các chứng chỉ tiếng Anh quốc tế, chuẩn đầu ra tin học MOS/IC3 tương đương được trường công nhận.",
                """
                <p>Để đủ điều kiện xét công nhận tốt nghiệp ra trường, sinh viên phải hoàn thành các chuẩn đầu ra sau:</p>
                <ul>
                    <li><strong>Ngoại ngữ:</strong> Đạt chứng chỉ TOEIC tối thiểu 500 điểm (hoặc IELTS 5.0, TOEFL iBT 45, VSTEP B1/B2 tùy theo ngành đào tạo).</li>
                    <li><strong>Tin học:</strong> Đạt chứng chỉ tin học quốc tế MOS (Word, Excel, PowerPoint) hoặc IC3/chứng chỉ ứng dụng CNTT nâng cao.</li>
                    <li><strong>Giáo dục quốc phòng & Thể chất:</strong> Đã hoàn tất và được cấp chứng chỉ theo quy định của Bộ GD&ĐT.</li>
                </ul>
                <p>Chứng chỉ phải nộp cho Phòng Đào tạo xác thực trước thời hạn xét tốt nghiệp tối thiểu 30 ngày.</p>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Chuẩn đầu ra", "Chứng chỉ", "Xét tốt nghiệp", "Học vụ"}
        ));

        list.add(new FaqDefinition(
                "DT_TOTNGHIEP",
                "Hồ sơ và các mốc thời gian nộp đơn xét tốt nghiệp các đợt trong năm",
                "Lịch trình xét tốt nghiệp đợt tháng 3, tháng 6, tháng 9 và tháng 12 cùng các giấy tờ cần chuẩn bị.",
                """
                <p>Hàng năm trường tổ chức 4 đợt xét tốt nghiệp chính:</p>
                <ol>
                    <li><strong>Đợt 1 (Tháng 3):</strong> Nhận đơn từ 15/01 đến 15/02.</li>
                    <li><strong>Đợt 2 (Tháng 6 - Đợt chính):</strong> Nhận đơn từ 15/04 đến 15/05.</li>
                    <li><strong>Đợt 3 (Tháng 9):</strong> Nhận đơn từ 15/07 đến 15/08.</li>
                    <li><strong>Đợt 4 (Tháng 12):</strong> Nhận đơn từ 15/10 đến 15/11.</li>
                </ol>
                <p>Sinh viên kiểm tra danh sách đủ điều kiện trên portal và nộp đơn xét tốt nghiệp trực tuyến.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Xét tốt nghiệp", "Học vụ", "Chứng chỉ"}
        ));

        list.add(new FaqDefinition(
                "DT_TOTNGHIEP",
                "Quy trình nhận bằng tốt nghiệp tạm thời và bằng chính thức",
                "Thời gian cấp phát giấy chứng nhận tốt nghiệp tạm thời, thủ tục mượn lễ phục và nhận bằng gốc tại lễ tốt nghiệp.",
                """
                <p>Sau khi có quyết định công nhận tốt nghiệp của Hiệu trưởng:</p>
                <ul>
                    <li>Sinh viên được cấp <strong>Giấy chứng nhận tốt nghiệp tạm thời</strong> trong vòng 07 ngày làm việc để sử dụng xin việc.</li>
                    <li>Bằng tốt nghiệp chính thức và bảng điểm toàn khóa được trao tại Lễ tốt nghiệp hoặc nhận tại Phòng Đào tạo sau lễ.</li>
                    <li>Khi đến nhận bằng, sinh viên cần mang theo CCCD bản gốc và thẻ sinh viên (hoặc giấy báo mất thẻ).</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Xét tốt nghiệp", "Chứng chỉ", "Bảng điểm"}
        ));

        list.add(new FaqDefinition(
                "DT_TOTNGHIEP",
                "Quy định cấp lại bản sao bằng tốt nghiệp khi bị mất hoặc hư hỏng",
                "Thủ tục xin cấp bản sao từ sổ gốc văn bằng tốt nghiệp theo quy định của Bộ Giáo dục và Đào tạo.",
                """
                <p>Bằng tốt nghiệp bản chính chỉ được cấp 01 lần duy nhất. Trường hợp bị mất hoặc hư hỏng:</p>
                <p>Sinh viên làm thủ tục xin cấp bản sao trích lục từ sổ gốc cấp bằng tại Phòng Đào tạo.</p>
                """,
                FaqStatus.ARCHIVED, false,
                new String[]{"Xét tốt nghiệp", "Chứng chỉ"}
        ));

        // 4. CT_HOCBONG
        list.add(new FaqDefinition(
                "CT_HOCBONG",
                "Điều kiện và tiêu chí xét học bổng khuyến khích học tập theo học kỳ",
                "Mức điểm học tập và điểm rèn luyện tối thiểu để đạt học bổng loại Khá, Giỏi, Xuất sắc cho sinh viên chính quy.",
                """
                <p>Học bổng khuyến khích học tập (KKHT) được xét theo từng học kỳ dựa trên 2 tiêu chí:</p>
                <ul>
                    <li><strong>Học bổng Xuất sắc:</strong> Điểm học tập từ 3.60 trở lên và điểm rèn luyện từ 90 điểm trở lên. Mức học bổng: 120% học phí kỳ.</li>
                    <li><strong>Học bổng Giỏi:</strong> Điểm học tập từ 3.20 đến 3.59 và điểm rèn luyện từ 80 điểm trở lên. Mức học bổng: 110% học phí kỳ.</li>
                    <li><strong>Học bổng Khá:</strong> Điểm học tập từ 2.50 đến 3.19 và điểm rèn luyện từ 70 điểm trở lên. Mức học bổng: 100% học phí kỳ.</li>
                </ul>
                <p>Điều kiện bắt buộc: Trong học kỳ xét học bổng, sinh viên phải đăng ký đủ số tín chỉ tối thiểu và không có môn học nào bị điểm F ở lần thi đầu.</p>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Học bổng", "Khuyến khích học tập", "Điểm rèn luyện", "Chính sách hỗ trợ"}
        ));

        list.add(new FaqDefinition(
                "CT_HOCBONG",
                "Hồ sơ và thủ tục xin học bổng tài trợ từ doanh nghiệp và cựu sinh viên",
                "Cách thức nộp đơn xét tuyển các chương trình học bổng đồng hành, học bổng vượt khó và cơ hội thực tập đi kèm.",
                """
                <p>Hàng năm Phòng Công tác sinh viên thông báo các suất học bổng từ các tập đoàn và cựu sinh viên:</p>
                <ol>
                    <li>Theo dõi thông báo học bổng tài trợ trên fanpage và portal sinh viên.</li>
                    <li>Chuẩn bị hồ sơ gồm: Đơn xin học bổng (theo mẫu), bảng điểm tích lũy, thư giới thiệu, giấy xác nhận hoàn cảnh khó khăn (nếu có).</li>
                    <li>Nộp hồ sơ trực tuyến qua hệ thống hoặc trực tiếp tại Phòng CTSV trước hạn chót.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Chính sách hỗ trợ", "Sinh viên HCMUTE"}
        ));

        list.add(new FaqDefinition(
                "CT_HOCBONG",
                "Thời gian chi trả học bổng vào tài khoản ngân hàng sinh viên",
                "Quy trình chuyển tiền học bổng qua thẻ ATM liên kết, xử lý khi thông tin số tài khoản ngân hàng bị sai.",
                """
                <p>Tiền học bổng khuyến khích học tập và học bổng tài trợ sẽ được chuyển trực tiếp vào tài khoản ngân hàng của sinh viên đã đăng ký trên portal:</p>
                <ul>
                    <li>Thời gian chuyển tiền: Trong vòng 15-20 ngày làm việc sau khi có quyết định khen thưởng chính thức.</li>
                    <li>Sinh viên cần cập nhật chính xác số tài khoản ngân hàng chính chủ tại mục Hồ sơ cá nhân.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học bổng", "Khuyến khích học tập", "Cổng thông tin"}
        ));

        list.add(new FaqDefinition(
                "CT_HOCBONG",
                "Chính sách trợ cấp xã hội và ưu đãi giáo dục cho sinh viên vùng khó khăn",
                "Điều kiện hưởng trợ cấp hàng tháng cho sinh viên mồ côi, người dân tộc thiểu số và sinh viên khuyết tật.",
                """
                <p>Sinh viên thuộc diện chính sách được hưởng trợ cấp xã hội theo quy định hiện hành:</p>
                <p>Hồ sơ bao gồm đơn đề nghị, bản sao công chứng sổ hộ nghèo/cận nghèo hoặc giấy xác nhận khuyết tật nộp định kỳ đầu mỗi học kỳ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Chính sách hỗ trợ", "Học bổng", "Sinh viên HCMUTE"}
        ));

        // 5. CT_RENLUYEN
        list.add(new FaqDefinition(
                "CT_RENLUYEN",
                "Cách thức chấm và tự đánh giá điểm rèn luyện trực tuyến cuối học kỳ",
                "Hướng dẫn tự chấm điểm trên hệ thống, các mốc thời gian họp lớp bình xét và duyệt điểm của Khoa.",
                """
                <p>Quy trình đánh giá kết quả rèn luyện của sinh viên gồm 3 bước:</p>
                <ol>
                    <li><strong>Sinh viên tự đánh giá:</strong> Đăng nhập portal, vào mục Điểm rèn luyện để tự chấm điểm theo các tiêu chí và đính kèm minh chứng.</li>
                    <li><strong>Tập thể lớp họp xét:</strong> Ban cán sự lớp chủ trì họp bình xét, ghi biên bản và thông qua kết quả chấm của từng thành viên.</li>
                    <li><strong>Khoa và Trường phê duyệt:</strong> Ban chủ nhiệm khoa kiểm tra, duyệt và công bố bảng điểm rèn luyện chính thức.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Điểm rèn luyện", "Hoạt động phong trào", "Cổng thông tin"}
        ));

        list.add(new FaqDefinition(
                "CT_RENLUYEN",
                "Quy đổi điểm tham gia hoạt động phong trào, tình nguyện vào điểm rèn luyện",
                "Bảng khung điểm cộng cho các hoạt động hiến máu nhân đạo, Mùa hè xanh, Tiếp sức mùa thi và nghiên cứu khoa học.",
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
                "Thời hạn khiếu nại và điều chỉnh điểm rèn luyện khi có sai sót",
                "Quy trình phúc tra điểm rèn luyện khi sinh viên bị sót hoạt động hoặc tính nhầm tiêu chí chấm điểm.",
                """
                <p>Sau khi có bảng điểm rèn luyện tạm thời được công bố:</p>
                <p>Sinh viên có 07 ngày làm việc để gửi phản hồi khiếu nại kèm minh chứng cho cố vấn học tập hoặc Phòng CTSV.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Điểm rèn luyện", "Hoạt động phong trào", "Hướng dẫn học vụ"}
        ));

        list.add(new FaqDefinition(
                "CT_RENLUYEN",
                "Mức điểm rèn luyện yêu cầu để được xét khen thưởng và tốt nghiệp loại giỏi",
                "Quy định khống chế xếp loại rèn luyện khi xét danh hiệu sinh viên giỏi, xuất sắc và tốt nghiệp thủ khoa.",
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
                "Các kênh thanh toán học phí online và hướng dẫn nộp qua ngân hàng",
                "Chi tiết các phương thức chuyển khoản qua Internet Banking, quét mã VietQR và thanh toán qua cổng VNPAY/MoMo.",
                """
                <p>Nhà trường hỗ trợ nhiều kênh nộp học phí tiện lợi và an toàn:</p>
                <ol>
                    <li><strong>Quét mã VietQR trên Portal:</strong> Đăng nhập portal, vào mục Học phí -> Thanh toán trực tuyến -> Quét mã QR bằng ứng dụng ngân hàng bất kỳ. Gạch nợ tự động ngay lập tức.</li>
                    <li><strong>Chuyển khoản qua số tài khoản định danh:</strong> Chuyển tiền tới số tài khoản chuyên thu học phí của trường, nội dung chuyển khoản bắt buộc ghi: <code>[Mã SV] [Họ và tên] [Hoc phi HK...]</code>.</li>
                    <li><strong>Cổng thanh toán điện tử VNPAY:</strong> Hỗ trợ thẻ ATM nội địa, thẻ quốc tế Visa/Mastercard.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Học phí", "Hạn nộp học phí", "Thanh toán online", "Cổng thông tin"}
        ));

        list.add(new FaqDefinition(
                "TC_HOCPHI",
                "Thời hạn đóng học phí và xử lý khi bị khóa cổng thông tin do nợ học phí",
                "Hậu quả khi nộp học phí trễ hạn, cách mở lại tài khoản đăng ký học phần sau khi hoàn tất nghĩa vụ tài chính.",
                """
                <p>Thời hạn đóng học phí được thông báo cụ thể vào đầu mỗi học kỳ (thường kéo dài 30 ngày):</p>
                <ul>
                    <li>Sinh viên nợ học phí quá hạn sẽ bị tạm khóa tài khoản đăng ký môn học và không có tên trong danh sách thi kết thúc học phần.</li>
                    <li>Sau khi hoàn tất thanh toán, hệ thống sẽ tự động mở lại portal trong vòng 30 phút đối với thanh toán VietQR (hoặc 1 ngày làm việc đối với chuyển khoản liên ngân hàng).</li>
                    <li>Trường hợp có khó khăn đặc biệt, sinh viên cần làm đơn xin gia hạn nộp học phí trước khi hết hạn nộp.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phí", "Hạn nộp học phí", "Thanh toán online"}
        ));

        list.add(new FaqDefinition(
                "TC_HOCPHI",
                "Cách tra cứu hóa đơn điện tử học phí sau khi đã thanh toán thành công",
                "Hướng dẫn tải hóa đơn điện tử VAT có mã xác thực của cơ quan thuế phục vụ thanh toán cơ quan hoặc lưu trữ.",
                """
                <p>Sau khi hệ thống ghi nhận giao dịch nộp học phí:</p>
                <ol>
                    <li>Đăng nhập vào mục <strong>Tra cứu học phí</strong> trên portal.</li>
                    <li>Chọn học kỳ đã nộp và bấm <strong>Tải hóa đơn điện tử</strong>.</li>
                    <li>Hóa đơn được xuất dưới dạng file PDF kèm chữ ký số hợp lệ của nhà trường.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Học phí", "Thanh toán online", "Cổng thông tin"}
        ));

        list.add(new FaqDefinition(
                "TC_HOCPHI",
                "Quy định về đơn giá học phí trên một tín chỉ cho các chương trình đào tạo",
                "Bảng đơn giá tín chỉ môn lý thuyết, thực hành, đồ án đối với chương trình chuẩn và chất lượng cao.",
                """
                <p>Đơn giá tín chỉ được quy định theo từng khóa học và chương trình đào tạo (Đại học đại trà, Chất lượng cao tiếng Việt, Chất lượng cao tiếng Anh):</p>
                <p>Sinh viên xem chi tiết biểu giá tại thông báo biểu mức thu học phí đầu năm học.</p>
                """,
                FaqStatus.PENDING_REVIEW, false,
                new String[]{"Học phí", "Học phần"}
        ));

        // 7. TC_MIENGIAM
        list.add(new FaqDefinition(
                "TC_MIENGIAM",
                "Hồ sơ và thời hạn nộp đơn miễn giảm học phí theo Nghị định của Chính phủ",
                "Điều kiện hưởng miễn 100% hoặc giảm 70%, 50% học phí theo Nghị định 81/2021/NĐ-CP và hướng dẫn làm hồ sơ.",
                """
                <p>Sinh viên thuộc các diện sau được xét miễn, giảm học phí theo quy định nhà nước:</p>
                <ul>
                    <li><strong>Miễn 100% học phí:</strong> Con liệt sĩ, con thương binh/bệnh binh nặng, người khuyết tật nặng, người dân tộc thiểu số thuộc hộ nghèo/cận nghèo.</li>
                    <li><strong>Giảm 70% học phí:</strong> Sinh viên học các ngành nghề nặng nhọc, độc hại theo danh mục quy định.</li>
                    <li><strong>Giảm 50% học phí:</strong> Con cán bộ công nhân viên chức bị tai nạn lao động hoặc mắc bệnh nghề nghiệp đang hưởng trợ cấp thường xuyên.</li>
                </ul>
                <p>Hồ sơ nộp về Phòng Kế hoạch - Tài chính trong vòng 30 ngày kể từ ngày bắt đầu học kỳ.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Miễn giảm học phí", "Học phí", "Chính sách hỗ trợ"}
        ));

        list.add(new FaqDefinition(
                "TC_MIENGIAM",
                "Thủ tục xin rút bớt học phần và hoàn trả tiền học phí đã đóng",
                "Thời hạn cho phép hủy môn học và tỷ lệ hoàn phí (100%, 50%) theo quy định quản lý tài chính của trường.",
                """
                <p>Sinh viên được phép rút bớt học phần và nhận lại học phí theo tỷ lệ:</p>
                <ol>
                    <li>Rút học phần trong 2 tuần đầu học kỳ: Được hoàn trả <strong>100% học phí</strong> của môn học đã rút.</li>
                    <li>Rút học phần trong tuần thứ 3 và thứ 4: Được hoàn trả <strong>50% học phí</strong>.</li>
                    <li>Sau tuần thứ 4: Không được hoàn trả học phí và môn học sẽ bị ghi nhận điểm W.</li>
                </ol>
                <p>Tiền hoàn lại sẽ được trừ vào công nợ học kỳ kế tiếp hoặc chuyển khoản về tài khoản ngân hàng của sinh viên.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Miễn giảm học phí", "Học phí", "Học phần"}
        ));

        list.add(new FaqDefinition(
                "TC_MIENGIAM",
                "Chính sách giảm học phí cho sinh viên có anh chị em ruột cùng học tại trường",
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
                "Quy trình đăng ký nội trú ký túc xá cho tân sinh viên và sinh viên các khóa",
                "Thời gian mở cổng đăng ký chỗ ở ký túc xá online, các loại phòng và biểu phí lưu trú khu A và khu B.",
                """
                <p>Quy trình đăng ký chỗ ở tại Ký túc xá HCMUTE:</p>
                <ol>
                    <li><strong>Đăng ký trực tuyến:</strong> Sinh viên vào portal ký túc xá để chọn khu vực (Khu A hoặc Khu B), loại phòng (phòng 4 người, 6 người hoặc 8 người).</li>
                    <li><strong>Xét duyệt hồ sơ:</strong> Ban quản lý KTX tiến hành xét duyệt ưu tiên theo tiêu chuẩn chính sách và khoảng cách địa lý.</li>
                    <li><strong>Đóng lệ phí phòng:</strong> Sinh viên có tên trong danh sách duyệt tiến hành thanh toán tiền phòng qua ngân hàng.</li>
                    <li><strong>Nhận phòng:</strong> Mang CCCD, biên lai đóng tiền và giấy xác nhận sinh viên đến làm thủ tục bàn giao chìa khóa.</li>
                </ol>
                """,
                FaqStatus.PUBLISHED, true,
                new String[]{"Ký túc xá", "Đăng ký nội trú", "Dịch vụ sinh viên", "Sinh viên HCMUTE"}
        ));

        list.add(new FaqDefinition(
                "KTX_DANGKY",
                "Các đối tượng ưu tiên được xét duyệt vào ở ký túc xá trong năm học",
                "Thứ tự phân bổ chỉ tiêu chỗ ở KTX cho sinh viên diện chính sách, vùng sâu vùng xa và tân sinh viên.",
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
                "Hồ sơ cần chuẩn bị và thời gian làm thủ tục nhận phòng ký túc xá",
                "Danh mục các giấy tờ cần thiết và đồ dùng sinh hoạt cá nhân được phép mang vào ký túc xá.",
                """
                <p>Khi đến làm thủ tục nhận phòng, sinh viên cần chuẩn bị:</p>
                <ul>
                    <li>02 ảnh 3x4 (chụp trong vòng 6 tháng gần nhất).</li>
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
                "Cách báo hỏng thiết bị và yêu cầu sửa chữa điện nước trong phòng KTX",
                "Quy trình gửi phiếu báo hỏng bóng đèn, quạt trần, vòi sen, khóa cửa và thời gian phản hồi của đội kỹ thuật KTX.",
                """
                <p>Khi trang thiết bị trong phòng ở ký túc xá gặp sự cố hư hỏng:</p>
                <ol>
                    <li>Đăng nhập ứng dụng KTX hoặc tạo yêu cầu hỗ trợ trên hệ thống QAUTE (chọn chuyên mục KTX & Dịch vụ).</li>
                    <li>Mô tả rõ sự cố (ví dụ: mất nước, chập điện bóng đèn, nghẹt cống) và ghi số phòng, số nhà.</li>
                    <li>Đội kỹ thuật KTX sẽ có mặt kiểm tra và sửa chữa trong vòng 24 giờ (đối với sự cố thông thường) hoặc trong 2 giờ (đối với sự cố khẩn cấp).</li>
                </ol>
                <p>Lưu ý: Hư hỏng do hao mòn tự nhiên được sửa chữa miễn phí; hư hỏng do cố ý làm hỏng sinh viên phải bồi thường theo quy chế.</p>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Dịch vụ sinh viên", "Sửa chữa trang thiết bị"}
        ));

        list.add(new FaqDefinition(
                "KTX_DICHVU",
                "Quy định về sử dụng mạng internet, gửi xe và dịch vụ giặt ủi tại KTX",
                "Hướng dẫn đăng ký tài khoản wifi ký túc xá, làm vé tháng nhà xe và nội quy sử dụng máy giặt tự động.",
                """
                <p>Các tiện ích dịch vụ hỗ trợ đời sống sinh viên nội trú:</p>
                <ul>
                    <li><strong>Mạng wifi:</strong> Mỗi sinh viên được cấp 01 tài khoản truy cập wifi tốc độ cao miễn phí trong khuôn viên KTX.</li>
                    <li><strong>Gửi xe:</strong> Sinh viên đăng ký vé xe tháng tại văn phòng nhà xe KTX với mức giá ưu đãi.</li>
                    <li><strong>Máy giặt tự động:</strong> Sử dụng hệ thống máy giặt sấy thông minh qua ứng dụng thanh toán trực tuyến.</li>
                </ul>
                """,
                FaqStatus.PUBLISHED, false,
                new String[]{"Ký túc xá", "Dịch vụ sinh viên"}
        ));

        return list;
    }
}

