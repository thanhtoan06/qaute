package vn.edu.hcmute.qaute.config.seed;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.common.constant.AnnouncementStatus;
import vn.edu.hcmute.qaute.entity.governance.Announcement;
import vn.edu.hcmute.qaute.repository.governance.AnnouncementRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;

@Component
public class AnnouncementSeeder implements DemoSeeder {

    private final AnnouncementRepository repo;
    private final UserRepository users;

    public AnnouncementSeeder(AnnouncementRepository repo, UserRepository users) {
        this.repo = repo;
        this.users = users;
    }

    @Override
    public int order() {
        return 50;
    }

    @Override
    @Transactional
    public void seed() {
        if (repo.count() > 0) return;

        Long adminId = users.findByEmailIgnoreCase("admin@qaute.edu.vn")
                .map(vn.edu.hcmute.qaute.entity.identity.User::getId)
                .orElse(1L);

        LocalDateTime now = LocalDateTime.now();

        List<AnnouncementItem> items = List.of(
            new AnnouncementItem(
                "Mở đăng ký học phần HK1 2026-2027",
                "Thời gian đăng ký từ 01/09 đến 10/09/2026. Sinh viên kiểm tra kế hoạch đào tạo trước khi đăng ký.",
                """
                <h3>THÔNG BÁO VỀ VIỆC ĐĂNG KÝ HỌC PHẦN HỌC KỲ 1 (2026 - 2027)</h3>
                <p>Phòng Đào tạo thông báo đến toàn thể sinh viên các khóa về kế hoạch và lộ trình đăng ký học phần cho học kỳ 1 như sau:</p>
                
                <h4>1. Thời gian và phân luồng đăng ký:</h4>
                <ul>
                    <li><strong>Đợt 1 (Đăng ký đúng tiến độ):</strong> Từ 08:00 ngày 01/09/2026 đến 17:00 ngày 05/09/2026 dành cho sinh viên khóa K23, K24.</li>
                    <li><strong>Đợt 2 (Đăng ký học vượt / học cải thiện):</strong> Từ 08:00 ngày 06/09/2026 đến 17:00 ngày 10/09/2026 cho toàn bộ sinh viên.</li>
                </ul>

                <h4>2. Quy định bắt buộc:</h4>
                <p>Sinh viên cần lưu ý số tín chỉ tối đa được đăng ký trong một học kỳ là <strong>24 tín chỉ</strong> (đối với sinh viên trung bình) và <strong>28 tín chỉ</strong> (đối với sinh viên khá/giỏi). Các học phần có môn tiên quyết bắt buộc phải đạt điểm yêu cầu trước khi hệ thống ghi nhận thành công.</p>
                
                <p>Mọi thắc mắc về lỗi hệ thống hoặc hủy môn ngoài thời hạn, sinh viên vui lòng gửi <strong>Ticket tư vấn cho Phòng Đào tạo</strong> qua QAUTE Portal trước 17:00 ngày 12/09/2026 để được xử lý kịp thời.</p>
                """
            ),
            new AnnouncementItem(
                "Thông báo lịch thi giữa kỳ HK1",
                "Lịch thi giữa kỳ học kỳ 1 công bố trên portal từ 15/10/2026. Sinh viên rà soát ca thi và phòng thi.",
                """
                <h3>LỊCH THI VÀ QUY ĐỊNH KỲ THI GIỮA KỲ HỌC KỲ 1 (2026 - 2027)</h3>
                <p>Phòng Đào tạo đã chính thức cập nhật lịch thi giữa kỳ HK1 trên cổng thông tin sinh viên. Sinh viên thực hiện kiểm tra lịch thi cá nhân theo các bước sau:</p>

                <h4>1. Hướng dẫn xem lịch thi:</h4>
                <ol>
                    <li>Đăng nhập tài khoản cá nhân trên Portal.</li>
                    <li>Thẻ <strong>Học vụ</strong> &rarr; Chọn <strong>Lịch thi theo học kỳ</strong> &rarr; Chọn HK1 2026-2027.</li>
                </ol>

                <h4>2. Quy chế phòng thi:</h4>
                <ul>
                    <li>Sinh viên bắt buộc mang theo <strong>Thẻ sinh viên</strong> hoặc <strong>Giấy tờ tùy thân có ảnh</strong> (CCCD/Gia hạn thẻ).</li>
                    <li>Không mang thiết bị thu phát sóng, tài liệu không được cho phép vào phòng thi. Mọi trường hợp vi phạm sẽ bị lập biên bản đình chỉ thi và ghi nhận điểm 0.</li>
                    <li>Địa điểm thi tập trung tại các tòa nhà <strong>A1, A2, A3 và Xưởng Thực hành</strong>. Vui lòng có mặt trước giờ thi 15 phút.</li>
                </ul>
                <p>Trường hợp trùng lịch thi giữa các môn học phần, sinh viên nộp đơn xin hoãn/đổi ca thi tại Phòng Đào tạo trước ngày 22/10/2026.</p>
                """
            ),
            new AnnouncementItem(
                "Học bổng khuyến khích học tập HK2",
                "Hạn nộp hồ sơ đến 30/09/2026. Mức học bổng lên đến 120% học phí với nhiều suất hỗ trợ hấp dẫn.",
                """
                <h3>THÔNG BÁO XÉT CẤP HỌC BỔNG KHUYẾN KHÍCH HỌC TẬP HỌC KỲ 2</h3>
                <p>Phòng Công tác Sinh viên (CTSV) thông báo kế hoạch xét cấp Học bổng Khuyến khích học tập (KKHT) dựa trên kết quả học tập và rèn luyện của sinh viên:</p>

                <h4>1. Điều kiện xét duyệt:</h4>
                <ul>
                    <li><strong>Mức xuất sắc (120% học phí):</strong> Điểm GPA &ge; 3.6 và Điểm rèn luyện &ge; 90.</li>
                    <li><strong>Mức giỏi (110% học phí):</strong> Điểm GPA &ge; 3.2 và Điểm rèn luyện &ge; 80.</li>
                    <li><strong>Mức khá (100% học phí):</strong> Điểm GPA &ge; 2.5 và Điểm rèn luyện &ge; 70.</li>
                </ul>

                <h4>2. Hồ sơ và thời gian nộp:</h4>
                <p>Hệ thống tự động trích xuất dữ liệu điểm từ CSDL của trường, sinh viên không cần nộp bản giấy trừ các trường hợp ưu tiên chính sách. Danh sách dự kiến sẽ được công khai để sinh viên đối soát từ ngày <strong>20/09 đến 30/09/2026</strong>.</p>
                <p>Mọi thắc mắc về điểm rèn luyện hoặc khiếu nại mức học bổng, vui lòng gửi phản hồi trực tiếp qua tính năng <strong>Gửi Ticket - Phòng CTSV</strong> trên portal QAUTE.</p>
                """
            ),
            new AnnouncementItem(
                "Mở cổng ký túc xá năm học 2026-2027",
                "Đăng ký nội trú KTX năm học 2026-2027 từ 01/08/2026. Hướng dẫn chi tiết quy trình đăng ký trực tuyến.",
                """
                <h3>THÔNG BÁO ĐĂNG KÝ NỘI TRÚ KÝ TÚC XÁ NĂM HỌC 2026 - 2027</h3>
                <p>Ban Quản lý Ký túc xá thông báo mở cổng đăng ký chỗ ở nội trú cho tân sinh viên K24 và sinh viên các khóa cũ tại Khu A và Khu B với thông tin chi tiết như sau:</p>

                <h4>1. Quy trình đăng ký trực tuyến:</h4>
                <ol>
                    <li>Đăng nhập tài khoản Student Portal &rarr; Chọn <strong>Dịch vụ KTX</strong> &rarr; <strong>Đăng ký phòng ở</strong>.</li>
                    <li>Lựa chọn loại phòng (Phòng 4 người, 6 người hoặc 8 người có máy lạnh / quạt).</li>
                    <li>Thanh toán phí lưu trú trực tuyến qua cổng VietQR/VNPAY trong vòng 24 giờ kể từ khi giữ chỗ thành công.</li>
                </ol>

                <h4>2. Các đối tượng được ưu tiên xét duyệt:</h4>
                <p>Sinh viên thuộc diện con thương binh/liệt sĩ, sinh viên vùng sâu vùng xa, hộ nghèo/cận nghèo và tân sinh viên K24 sẽ được ưu tiên xếp phòng trước ngày 15/08/2026. Vui lòng đính kèm bản quét minh chứng khi gửi hồ sơ online.</p>
                <p>Mọi thắc mắc liên quan đến phí phòng và dịch vụ KTX, liên hệ Văn phòng BQL KTX tại Tòa nhà C2 hoặc gửi yêu cầu tư vấn trên QAUTE.</p>
                """
            ),
            new AnnouncementItem(
                "Bảo trì hệ thống máy chủ Portal",
                "Portal tạm ngưng dịch vụ từ 00:00 đến 04:00 ngày 20/10/2026 để nâng cấp và tối ưu CSDL.",
                """
                <h3>THÔNG BÁO BẢO TRÌ NÂNG CẤP HỆ THỐNG MÁY CHỦ PORTAL</h3>
                <p>Trung tâm Máy tính & Mạng thông báo kế hoạch bảo trì và nâng cấp hạ tầng máy chủ định kỳ nhằm tối ưu tốc độ truy cập và mở rộng băng thông phục vụ đợt ĐKMH sắp tới.</p>

                <h4>1. Thời gian gián đoạn dịch vụ:</h4>
                <p>Từ <strong>00:00 đến 04:00 (Rạng sáng) ngày 20/10/2026</strong>.</p>

                <h4>2. Các dịch vụ bị ảnh hưởng:</h4>
                <ul>
                    <li>Trang thông tin sinh viên (Student Portal).</li>
                    <li>Hệ thống hỏi đáp QAUTE & Trợ lý ảo Chatbot.</li>
                    <li>Cổng thanh toán học phí trực tuyến VNPAY/MoMo.</li>
                </ul>
                <p>Sau 04:00 ngày 20/10/2026, toàn bộ hệ thống sẽ hoạt động bình thường. Ban Quản trị rất mong nhận được sự thông cảm của quý Thầy/Cô và các bạn sinh viên về sự gián đoạn này.</p>
                """
            ),
            new AnnouncementItem(
                "Tuyển sinh câu lạc bộ đợt 1 năm 2026",
                "Các CLB học thuật, kỹ năng và nghệ thuật tuyển thành viên mới đến 15/09/2026 với nhiều hoạt động hấp dẫn.",
                """
                <h3>NGÀY HỘI TUYỂN THÀNH VIÊN CÁC CÂU LẠC BỘ - ĐỘI - NHÓM HCMUTE 2026</h3>
                <p>Đoàn Thanh niên - Hội Sinh viên trường phối hợp cùng hơn 40 Câu lạc bộ (CLB) tổ chức đợt tuyển thành viên quy mô lớn nhất trong năm dành cho toàn thể sinh viên HCMUTE!</p>

                <h4>1. Danh sách các mảng CLB tuyển sinh:</h4>
                <ul>
                    <li><strong>Mảng Học thuật & Công nghệ:</strong> CLB Lập trình Tin học, CLB AI & Robotics, CLB Nghiên cứu Khoa học Trẻ, CLB Tiếng Anh...</li>
                    <li><strong>Mảng Văn hóa - Nghệ thuật:</strong> CLB Âm nhạc Guitar, CLB Vũ đạo, CLB Sân khấu Đội kịch...</li>
                    <li><strong>Mảng Thể thao & Kỹ năng:</strong> CLB Vovinam, CLB Basketball, CLB Kỹ năng Phản biện, CLB Môi trường Xanh...</li>
                </ul>

                <h4>2. Hình thức đăng ký:</h4>
                <p>Sinh viên đăng ký trực tiếp tại các gian hàng ở Sân trung tâm Tòa nhà A1 hoặc điền Form online tại fanpage chính thức của từng CLB từ nay đến hết ngày <strong>15/09/2026</strong>. Việc tham gia CLB sẽ được cộng điểm rèn luyện ở mục Kỹ năng & Hoạt động phong trào.</p>
                """
            ),
            new AnnouncementItem(
                "Hội thảo kỹ năng mềm và viết CV chuẩn",
                "Diễn ra lúc 08:00 ngày 25/10/2026 tại Hội trường A. Cơ hội giao lưu cùng các chuyên gia HR hàng đầu.",
                """
                <h3>HỘI THẢO: KỸ NĂNG VIẾT CV NỔI BẬT VÀ BÍ QUYẾT PHỎNG VẤN CHINH PHỤC NHÀ TUYỂN DỤNG</h3>
                <p>Nhằm hỗ trợ sinh viên năm 3, năm 4 chuẩn bị hành trang tốt nhất trước khi bước vào kỳ thực tập và tốt nghiệp, Trung tâm Quan hệ Doanh nghiệp tổ chức chuyên đề Hội thảo Kỹ năng nâng cao:</p>

                <h4>1. Thông tin chương trình:</h4>
                <ul>
                    <li><strong>Thời gian:</strong> 08:00 - 11:30, Chủ Nhật ngày 25/10/2026.</li>
                    <li><strong>Địa điểm:</strong> Hội trường A1 - Tòa nhà trung tâm HCMUTE.</li>
                    <li><strong>Diễn giả:</strong> Giám đốc Nhân sự (HRD) đến từ các Tập đoàn Công nghệ & Kỹ thuật hàng đầu.</li>
                </ul>

                <h4>2. Quyền lợi tham dự:</h4>
                <ul>
                    <li>Được các chuyên gia sửa CV trực tiếp 1-1 tại hội trường.</li>
                    <li>Cộng <strong>+5 điểm rèn luyện</strong> (Mục Kỹ năng sinh viên) khi điểm danh đầu và cuối buổi.</li>
                    <li>Cơ hội nhận 20 suất học bổng khóa học Tiếng Anh & Kỹ năng giao tiếp trị giá 3.000.000 VNĐ.</li>
                </ul>
                <p>Số lượng chỗ ngồi có hạn (tối đa 500 sinh viên), vui lòng đăng ký trước ngày 23/10/2026 qua liên kết trên Portal.</p>
                """
            ),
            new AnnouncementItem(
                "Thông báo nhắc đóng học phí HK1",
                "Hạn đóng học phí HK1 đến 30/09/2026. Hướng dẫn chi tiết các cổng thanh toán online không tốn phí.",
                """
                <h3>THÔNG BÁO THỜI HẠN VÀ PHƯƠNG THỨC THANH TOÁN HỌC PHÍ HỌC KỲ 1</h3>
                <p>Phòng Kế hoạch - Tài chính thông báo đến toàn thể sinh viên về nghĩa vụ hoàn thành học phí học kỳ 1 (Năm học 2026 - 2027) với các nội dung lưu ý sau:</p>

                <h4>1. Hạn chót thanh toán:</h4>
                <p>Trước <strong>17:00 ngày 30/09/2026</strong>. Sau thời hạn này, các tài khoản chưa hoàn tất học phí sẽ bị tạm khóa chức năng đăng ký môn học và không có tên trong danh sách thi giữa kỳ.</p>

                <h4>2. Kênh thanh toán hỗ trợ:</h4>
                <ul>
                    <li><strong>Thanh toán VietQR / Internet Banking:</strong> Quét mã QR động tích hợp sẵn trên trang tra cứu học phí cá nhân (Giao dịch tự động gạch nợ sau 5 phút).</li>
                    <li><strong>Cổng VNPAY / Ví MoMo:</strong> Chọn thanh toán hóa đơn &rarr; Học phí &rarr; Nhập Mã số sinh viên (MSSV).</li>
                </ul>
                <p>Sinh viên có hoàn cảnh khó khăn cần gia hạn đóng học phí vui lòng nộp <em>Đơn xin gia hạn</em> có xác nhận của địa phương về Phòng CTSV trước ngày 25/09/2026 để được xem xét không bị khóa tài khoản.</p>
                """
            ),
            new AnnouncementItem(
                "Lịch nghỉ lễ Quốc khánh 2/9",
                "Sinh viên toàn trường nghỉ từ ngày 01/09 đến hết ngày 02/09/2026. Chúc các bạn có kỳ nghỉ an toàn.",
                """
                <h3>THÔNG BÁO LỊCH NGHỈ LỄ QUỐC KHÁNH 02/09 VÀ ĐẢM BẢO AN NINH TRẬT TỰ</h3>
                <p>Ban Giám hiệu nhà trường thông báo đến toàn thể cán bộ, giảng viên và sinh viên thời gian nghỉ lễ Quốc khánh 2/9 năm 2026 như sau:</p>

                <h4>1. Thời gian nghỉ:</h4>
                <p>Sinh viên toàn trường được nghỉ học từ <strong>Thứ Ba ngày 01/09/2026 đến hết Thứ Tư ngày 02/09/2026</strong>. Các lớp học sẽ hoạt động trở lại bình thường từ Thứ Năm ngày 03/09/2026.</p>

                <h4>2. Một số lưu ý quan trọng:</h4>
                <ul>
                    <li>Sinh viên trước khi rời phòng học, xưởng thực hành hoặc phòng KTX cần tắt toàn bộ hệ thống điện, khóa kỹ cửa sổ và cửa ra vào để phòng chống cháy nổ.</li>
                    <li>Đoàn Thanh niên và Ban Quản lý KTX phối hợp cùng Đội Bảo vệ tăng cường tuần tra, đảm bảo an toàn tuyệt đối trong khuôn viên trường suốt kỳ nghỉ.</li>
                </ul>
                <p>Chúc toàn thể quý Thầy/Cô và các bạn sinh viên có một kỳ nghỉ lễ an toàn, vui vẻ bên gia đình!</p>
                """
            ),
            new AnnouncementItem(
                "Kết quả xét tốt nghiệp đợt 2 năm 2026",
                "Danh sách công bố trên portal từ 20/09/2026. Sinh viên rà soát thông tin văn bằng trước khi in.",
                """
                <h3>THÔNG BÁO CÔNG BỐ KẾT QUẢ XÉT TỐT NGHIỆP VÀ LỊCH NHẬN BẰNG ĐỢT 2 (2026)</h3>
                <p>Hội đồng Xét tốt nghiệp Trường Đại học Sư phạm Kỹ thuật TP.HCM thông báo kết quả công nhận tốt nghiệp cho sinh viên trình độ Đào tạo Đại học đợt 2 năm 2026:</p>

                <h4>1. Tra cứu kết quả và rà soát thông tin:</h4>
                <p>Sinh viên đăng nhập Portal &rarr; Chọn <strong>Học vụ</strong> &rarr; <strong>Tra cứu xét tốt nghiệp</strong>. Vui lòng rà soát kỹ các thông tin cá nhân bao gồm: <em>Họ và tên, Ngày sinh, Nơi sinh, Ngành học và Xếp loại tốt nghiệp</em>.</p>

                <h4>2. Thời gian phản hồi và Lễ trao bằng:</h4>
                <ul>
                    <li><strong>Thời hạn phản hồi sai sót thông tin:</strong> Từ ngày 20/09 đến hết ngày 25/09/2026 qua Ticket dịch vụ Phòng Đào tạo.</li>
                    <li><strong>Thời gian tổ chức Lễ trao bằng tốt nghiệp:</strong> Dự kiến diễn ra vào hai ngày <strong>12/10 và 13/10/2026</strong> tại Hội trường Lớn.</li>
                </ul>
                <p>Xin chúc mừng tất cả các bạn Tân Cử nhân, Tân Kỹ sư đã xuất sắc hoàn thành chặng đường học tập tại HCMUTE!</p>
                """
            )
        );

        for (int i = 0; i < items.size(); i++) {
            AnnouncementItem item = items.get(i);
            Announcement a = new Announcement();
            a.setTitle(item.title());
            a.setSummary(item.summary());
            a.setBodyHtml(item.bodyHtml());
            a.setAudience(AnnouncementAudience.ALL);
            a.setPinned(i < 2);
            a.setStatus(AnnouncementStatus.PUBLISHED);
            a.setPublishAt(now.minusDays(i));
            a.setNotifyOnPublish(false);
            a.setNotified(true);
            a.setAutoPublish(false);
            a.setCreatedBy(adminId);
            repo.save(a);
        }
    }

    private record AnnouncementItem(String title, String summary, String bodyHtml) {}
}