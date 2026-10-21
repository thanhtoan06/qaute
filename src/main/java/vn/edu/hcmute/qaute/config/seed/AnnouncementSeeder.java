package vn.edu.hcmute.qaute.config.seed;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.common.constant.AnnouncementStatus;
import vn.edu.hcmute.qaute.entity.governance.Announcement;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.governance.AnnouncementRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;

@Component
@Profile("demo")
@RequiredArgsConstructor
public class AnnouncementSeeder implements DemoSeeder {
    private final AnnouncementRepository announcements;
    private final UserRepository users;

    @Override
    public int order() {
        return 70;
    }

    @Override
    @Transactional
    public void seed() {
        User admin = users.findByEmailIgnoreCase("admin@qaute.edu.vn")
                .orElseThrow(() -> new IllegalStateException("Chưa có Admin demo để tạo thông báo"));
        LocalDateTime now = LocalDateTime.now().withNano(0);
        create(admin, "Lịch đăng ký học phần học kỳ mới", AnnouncementAudience.ALL, true,
                "Sinh viên chuẩn bị kế hoạch học tập và theo dõi lịch đăng ký học phần trên cổng thông tin.",
                "<h2>Chuẩn bị đăng ký học phần</h2><p>Sinh viên rà soát kế hoạch học tập, học phần tiên quyết và số tín chỉ dự kiến trước khi đăng ký.</p>"
                        + "<p>Lịch đăng ký chi tiết theo khóa được công bố trên cổng sinh viên. Nếu gặp lỗi, hãy gửi yêu cầu đến chuyên mục Đăng ký học phần kèm mã học phần và thông báo lỗi.</p>",
                now.minusDays(2), now.plusDays(30), false);
        create(admin, "Thông báo nộp học phí", AnnouncementAudience.STUDENT, false,
                "Sinh viên kiểm tra công nợ, thời hạn thanh toán và lưu chứng từ để thuận tiện đối soát học phí.",
                "<h2>Hướng dẫn thanh toán học phí</h2><p>Sinh viên kiểm tra số tiền cần nộp và thời hạn trong thông báo học phí trên cổng sinh viên.</p>"
                        + "<p>Thanh toán đúng nội dung hướng dẫn và lưu lại chứng từ. Trường hợp giao dịch chưa được cập nhật, gửi yêu cầu đến chuyên mục Học phí để được đối soát.</p>",
                now.minusDays(3), now.plusDays(15), false);
        create(admin, "Quy trình xử lý yêu cầu mới", AnnouncementAudience.MANAGER, false,
                "Tư vấn viên kiểm tra phạm vi phụ trách, ưu tiên yêu cầu gần hạn SLA và ghi nhận kết quả xử lý đầy đủ.",
                "<h2>Quy trình dành cho tư vấn viên</h2><ol><li>Kiểm tra hàng đợi và nhận yêu cầu trong phạm vi phụ trách.</li>"
                        + "<li>Phản hồi rõ ràng, yêu cầu bổ sung thông tin nếu cần và theo dõi hạn SLA.</li>"
                        + "<li>Ghi tóm tắt giải pháp khi hoàn tất; chuyển cấp hoặc chuyển người phụ trách khi cần phối hợp.</li></ol>",
                now.minusDays(1), now.plusDays(30), false);
        create(admin, "Kết thúc đợt đăng ký nội trú học kỳ trước", AnnouncementAudience.ALL, false,
                "Đợt tiếp nhận hồ sơ nội trú của học kỳ trước đã kết thúc; sinh viên theo dõi thông báo đợt tiếp theo.",
                "<h2>Đợt đăng ký đã kết thúc</h2><p>Thông báo này được lưu để tra cứu. Bộ phận quản lý ký túc xá đã hoàn tất tiếp nhận hồ sơ của đợt đăng ký trước.</p>"
                        + "<p>Sinh viên có nhu cầu bổ sung hoặc thay đổi thông tin vui lòng gửi yêu cầu đến chuyên mục Đăng ký ký túc xá.</p>",
                now.minusDays(25), now.minusDays(5), true);
    }

    private void create(User admin, String title, AnnouncementAudience audience, boolean pinned,
                        String summary, String bodyHtml, LocalDateTime publishAt,
                        LocalDateTime expiresAt, boolean expired) {
        if (announcements.existsByTitle(title)) return;
        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setSummary(summary);
        announcement.setBodyHtml(bodyHtml);
        announcement.setAudience(audience);
        announcement.setPinned(pinned);
        announcement.setStatus(expired ? AnnouncementStatus.EXPIRED : AnnouncementStatus.PUBLISHED);
        announcement.setPublishAt(publishAt);
        announcement.setExpiresAt(expiresAt);
        announcement.setCreatedBy(admin.getId());
        // Dữ liệu mẫu không phát thông báo hay email tới tài khoản người dùng.
        announcement.setNotifyOnPublish(false);
        announcement.setNotified(false);
        announcement.setAutoPublish(false);
        announcements.save(announcement);
    }
}
