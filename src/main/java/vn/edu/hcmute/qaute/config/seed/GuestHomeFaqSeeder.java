package vn.edu.hcmute.qaute.config.seed;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.entity.publicsite.GuestHomeFaq;
import vn.edu.hcmute.qaute.repository.publicsite.GuestHomeFaqRepository;

/**
 * Seed câu hỏi thường gặp trên trang chủ guest.
 * Cần chạy sau khi bảng guest_home_faqs được tạo (migration V023).
 */
@Component
public class GuestHomeFaqSeeder implements DemoSeeder {

    private final GuestHomeFaqRepository repository;

    // Khởi tạo seeder GuestHomeFaq.
    public GuestHomeFaqSeeder(GuestHomeFaqRepository repository) {
        this.repository = repository;
    }

    // Xác định thứ tự chạy của seeder.
    @Override
    public int order() {
        return 40;
    }

    // Nạp dữ liệu FAQ demo cho trang chủ guest.
    @Override
    @Transactional
    public void seed() {
        if (repository.count() > 0) {
            return;
        }

        // === ĐĂNG KÝ MÔN HỌC ===
        repository.save(GuestHomeFaq.builder()
                .categoryName("Đăng ký môn")
                .question("Thời gian đăng ký môn học học kỳ mới là khi nào?")
                .answer("Lịch đăng ký môn học được Phòng Đào tạo công bố trên trang online.hcmute.edu.vn khoảng 3–4 tuần trước khi học kỳ bắt đầu. Sinh viên đăng ký theo khung giờ được phân bổ theo khóa và ngành đào tạo.")
                .displayOrder(1)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Đăng ký môn")
                .question("Tôi có thể hủy môn học đã đăng ký không?")
                .answer("Sinh viên được rút môn trong thời gian điều chỉnh đăng ký (thường là 2 tuần đầu học kỳ). Sau thời gian này, môn học sẽ được ghi nhận điểm W nếu rút theo quy định đào tạo.")
                .displayOrder(2)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Đăng ký môn")
                .question("Điều kiện tiên quyết để đăng ký môn học là gì?")
                .answer("Sinh viên phải qua các môn học tiên quyết quy định trong chương trình đào tạo. Hệ thống sẽ tự động chặn đăng ký nếu chưa đáp ứng điều kiện. Bạn nên kiểm tra kỹ trước khi đăng ký.")
                .displayOrder(3)
                .isActive(true)
                .build());

        // === HỌC PHÍ ===
        repository.save(GuestHomeFaq.builder()
                .categoryName("Học phí")
                .question("Hạn đóng học phí mỗi học kỳ là khi nào?")
                .answer("Hạn đóng học phí thường trong vòng 4 tuần kể từ ngày bắt đầu học kỳ. Sinh viên có thể nộp qua ngân hàng liên kết hoặc cổng thanh toán trực tuyến của trường.")
                .displayOrder(4)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Học phí")
                .question("Làm thế nào để xin gia hạn nộp học phí?")
                .answer("Sinh viên nộp đơn xin gia hạn học phí tại Phòng Kế hoạch – Tài chính kèm minh chứng hoàn cảnh. Bạn cũng có thể gửi ticket tư vấn ngay bên dưới để được hướng dẫn chi tiết.")
                .displayOrder(5)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Học phí")
                .question("Học phí học kỳ hè tính như thế nào?")
                .answer("Học phí học kỳ hè được tính theo số tín chỉ thực đăng ký, mức phí có thể khác với học kỳ chính. Chi tiết được công bố trước khi mở cổng đăng ký.")
                .displayOrder(6)
                .isActive(true)
                .build());

        // === HỌC VỤ ===
        repository.save(GuestHomeFaq.builder()
                .categoryName("Học vụ")
                .question("Cảnh báo học vụ được xét như thế nào?")
                .answer("Sinh viên bị cảnh báo học vụ khi điểm trung bình học kỳ dưới 1.0 (thang 4) hoặc điểm trung bình tích lũy thấp hơn mức quy định theo năm học. Hãy liên hệ cố vấn học tập để được định hướng cải thiện.")
                .displayOrder(7)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Học vụ")
                .question("Thủ tục bảo lưu kết quả học tập gồm những gì?")
                .answer("Sinh viên nộp đơn bảo lưu tại Phòng Đào tạo kèm giấy tờ minh chứng (giấy khám sức khỏe, lệnh nhập ngũ...). Thời gian bảo lưu tối đa theo quy chế đào tạo hiện hành.")
                .displayOrder(8)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Học vụ")
                .question("Quy định về số tín chỉ tối thiểu và tối đa mỗi học kỳ?")
                .answer("Sinh viên bình thường đăng ký tối thiểu 14 tín chỉ và tối đa 24 tín chỉ mỗi học kỳ chính. Sinh viên bị cảnh báo học vụ chỉ đăng ký tối đa 14 tín chỉ.")
                .displayOrder(9)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Học vụ")
                .question("Điều kiện để được xét tốt nghiệp là gì?")
                .answer("Sinh viên cần hoàn thành đủ số tín chỉ chương trình đào tạo, điểm trung bình tích lũy từ 2.0 trở lên, đạt các chuẩn đầu ra và không còn môn nợ.")
                .displayOrder(10)
                .isActive(true)
                .build());

        // === KÝ TÚC XÁ ===
        repository.save(GuestHomeFaq.builder()
                .categoryName("Ký túc xá")
                .question("Đăng ký ở ký túc xá cần những điều kiện gì?")
                .answer("Sinh viên chính quy đang học tại trường được đăng ký ở KTX. Ưu tiên sinh viên năm nhất, sinh viên diện chính sách xã hội và hoàn cảnh khó khăn.")
                .displayOrder(11)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Ký túc xá")
                .question("Chi phí ở ký túc xá bao gồm những gì?")
                .answer("Chi phí bao gồm tiền phòng, tiền điện nước (tính theo đồng hồ), phí dịch vụ chung và các khoản phí khác theo quy định của nhà trường.")
                .displayOrder(12)
                .isActive(true)
                .build());

        // === HỌC BỔNG ===
        repository.save(GuestHomeFaq.builder()
                .categoryName("Học bổng")
                .question("Điều kiện xét học bổng khuyến khích học tập là gì?")
                .answer("Sinh viên cần có điểm trung bình học kỳ từ 3.2 trở lên, điểm rèn luyện từ loại Tốt, không có môn nào dưới điểm D và đăng ký đủ số tín chỉ tối thiểu theo quy định.")
                .displayOrder(13)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Học bổng")
                .question("Các loại học bổng sinh viên có thể đăng ký?")
                .answer("HCMUTE có nhiều loại học bổng: Khuyến khích học tập, Xuất sắc, Tài trợ từ doanh nghiệp, Hỗ trợ sinh viên nghèo, và các học bổng đặc biệt khác.")
                .displayOrder(14)
                .isActive(true)
                .build());

        // === ĐIỂM THI & BẢNG ĐIỂM ===
        repository.save(GuestHomeFaq.builder()
                .categoryName("Điểm thi")
                .question("Cách xin phúc khảo bài thi kết thúc học phần?")
                .answer("Sinh viên nộp đơn phúc khảo trong vòng 7 ngày làm việc kể từ ngày công bố điểm, tại Văn phòng Đào tạo hoặc qua cổng sinh viên. Lệ phí sẽ được hoàn trả nếu điểm phúc khảo tăng.")
                .displayOrder(15)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Điểm thi")
                .question("Xem bảng điểm và điểm tích lũy ở đâu?")
                .answer("Đăng nhập cổng thông tin sinh viên (online.hcmute.edu.vn), chọn mục Tra cứu học vụ → Bảng điểm cá nhân để xem điểm chi tiết và điểm trung bình tích lũy.")
                .displayOrder(16)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Điểm thi")
                .question("Quy định về điểm W và điểm F?")
                .answer("Điểm W là rút môn trong thời gian quy định, không tính vào điểm tích lũy. Điểm F là không đạt môn học (dưới 4.0 thang 10), sẽ tính vào điểm tích lũy và phải học lại.")
                .displayOrder(17)
                .isActive(true)
                .build());

        // === TÀI KHOẢN & CỔNG THÔNG TIN ===
        repository.save(GuestHomeFaq.builder()
                .categoryName("Tài khoản")
                .question("Làm sao khôi phục mật khẩu cổng thông tin sinh viên?")
                .answer("Sử dụng tính năng Quên mật khẩu trên trang đăng nhập, hệ thống sẽ gửi link reset về email sinh viên. Nếu email không hoạt động, liên hệ Phòng CNTT để hỗ trợ.")
                .displayOrder(18)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Tài khoản")
                .question("Email sinh viên được cấp khi nào?")
                .answer("Email sinh viên với đuôi @student.hcmute.edu.vn được cấp tự động khi bạn nhập học. Đây là email chính thức để nhận thông báo từ nhà trường.")
                .displayOrder(19)
                .isActive(true)
                .build());

        // === THẺ SINH VIÊN ===
        repository.save(GuestHomeFaq.builder()
                .categoryName("Thẻ sinh viên")
                .question("Làm thẻ sinh viên ở đâu và cần giấy tờ gì?")
                .answer("Sau khi nhập học hoàn tất, sinh viên đến Phòng Công tác sinh viên để làm thẻ. Cần mang theo 2 ảnh 3x4 và giấy tờ tùy thân (CCCD/CMND).")
                .displayOrder(20)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Thẻ sinh viên")
                .question("Làm lại thẻ sinh viên bị mất hoặc hỏng như thế nào?")
                .answer("Đến Phòng Công tác sinh viên để làm lại thẻ, cần nộp lệ phí theo quy định. Thẻ sinh viên được sử dụng để xác nhận danh tính và nhận các ưu đãi.")
                .displayOrder(21)
                .isActive(true)
                .build());

        // === LỊCH THI ===
        repository.save(GuestHomeFaq.builder()
                .categoryName("Lịch thi")
                .question("Lịch thi được công bố khi nào?")
                .answer("Lịch thi cuối kỳ được công bố khoảng 2 tuần trước khi thi. Sinh viên cần kiểm tra thường xuyên trên cổng thông tin sinh viên để tránh trùng lịch.")
                .displayOrder(22)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Lịch thi")
                .question("Điều kiện được dự thi học phần là gì?")
                .answer("Sinh viên phải đăng ký môn học, không bị cấm thi do vi phạm kỷ luật, và đã hoàn thành các yêu cầu chuyên môn (nếu có). Điểm danh đến lớp theo quy định cũng là điều kiện quan trọng.")
                .displayOrder(23)
                .isActive(true)
                .build());

        // === CHỨNG CHỈ & CHUẨN ĐẦU RA ===
        repository.save(GuestHomeFaq.builder()
                .categoryName("Chứng chỉ")
                .question("Các chứng chỉ ngoại ngữ bắt buộc để tốt nghiệp?")
                .answer("Tùy theo chương trình đào tạo, sinh viên cần đạt chứng chỉ tiếng Anh (TOEIC/IELTS) hoặc tin học theo chuẩn đầu ra của nhà trường. Chi tiết được công bố trong quy chế đào tạo.")
                .displayOrder(24)
                .isActive(true)
                .build());

        repository.save(GuestHomeFaq.builder()
                .categoryName("Chứng chỉ")
                .question("Làm sao xác nhận chuẩn đầu ra đã đạt?")
                .answer("Qua cổng thông tin sinh viên, chọn mục Chuẩn đầu ra để xem danh sách và cập nhật các chứng chỉ đã đạt. Phòng Đào tạo sẽ xác nhận khi hồ sơ hợp lệ.")
                .displayOrder(25)
                .isActive(true)
                .build());
    }
}
