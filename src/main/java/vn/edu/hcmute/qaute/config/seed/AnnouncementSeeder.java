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
        if (repo.count() > 0) {
            return;
        }

        Long adminId = users.findByEmailIgnoreCase("admin@qaute.edu.vn")
                .map(vn.edu.hcmute.qaute.entity.identity.User::getId)
                .orElse(1L);

        List<AnnouncementItem> items = List.of(
            new AnnouncementItem(
                    "Chuẩn bị kế hoạch đăng ký học phần học kỳ kế tiếp",
                    "Sinh viên nên rà soát kế hoạch học tập, điều kiện tiên quyết và lịch học trước khi mở đợt đăng ký.",
                    """
                    <h3>Chuẩn bị trước đợt đăng ký học phần</h3><p>Sinh viên hãy đối chiếu chương trình đào tạo của khóa mình, các học phần đã tích lũy và môn dự kiến học trong kỳ tới. Kiểm tra học phần tiên quyết, học phần song hành, số tín chỉ và các khung giờ có nguy cơ trùng nhau.</p><p>Lịch mở cổng, chỉ tiêu nhóm lớp và thời hạn điều chỉnh phải theo thông báo mới nhất của Phòng Đào tạo. Sau khi đăng ký, tải hoặc lưu phiếu xác nhận để tiện đối chiếu.</p>
                    """
            ),
            new AnnouncementItem(
                    "Rà soát tiến độ học tập và các học phần còn thiếu",
                    "Đối chiếu bảng điểm với chương trình đào tạo để chủ động lập kế hoạch học kỳ sau.",
                    """
                    <h3>Kiểm tra tiến độ học vụ</h3><p>Hãy so sánh bảng điểm tích lũy với khung chương trình áp dụng cho ngành và khóa học của bạn. Chú ý học phần bắt buộc, nhóm tự chọn, tổng tín chỉ và các điều kiện bổ sung được ghi trong chương trình.</p><p>Nếu dữ liệu trên cổng sinh viên khác với kết quả đã công bố, tạo yêu cầu tư vấn và nêu rõ mã môn, học kỳ, trạng thái đang hiển thị cùng tài liệu đối chiếu.</p>
                    """
            ),
            new AnnouncementItem(
                    "Khuyến nghị tự kiểm tra điều kiện xét tốt nghiệp",
                    "Chủ động rà soát tín chỉ, chuẩn đầu ra và nghĩa vụ liên quan trước khi đến đợt xét.",
                    """
                    <h3>Rà soát điều kiện trước đợt xét tốt nghiệp</h3><p>Sinh viên dự kiến tốt nghiệp nên kiểm tra số tín chỉ tích lũy, những học phần chưa đạt, chuẩn đầu ra và các điều kiện của chương trình dành cho khóa mình. Đối chiếu thêm các nghĩa vụ mượn trả và tình trạng tài chính nếu có yêu cầu.</p><p>Thông báo chính thức của trường là căn cứ xác định điều kiện và mốc nộp hồ sơ. Nếu portal báo còn thiếu mục mà bạn cho rằng đã hoàn thành, hãy gửi mã sinh viên và bằng chứng liên quan để Phòng Đào tạo kiểm tra.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông tin chung về học bổng khuyến khích học tập",
                    "Theo dõi tiêu chí, kỳ xét và kết quả qua thông báo chính thức thay vì dựa vào thông tin truyền miệng.",
                    """
                    <h3>Hướng dẫn theo dõi học bổng khuyến khích học tập</h3><p>Điều kiện xét học bổng có thể gắn với kết quả học tập, điểm rèn luyện, tải học tập và những tiêu chí riêng của từng đợt. Sinh viên cần đọc thông báo áp dụng cho đúng học kỳ và nhóm đối tượng.</p><p>Hãy kiểm tra dữ liệu học vụ trước thời hạn đối soát. Nếu thấy thông tin chưa đúng, gửi yêu cầu cho Phòng Công tác sinh viên và nêu tiêu chí cần được rà soát; không tự suy ra mức học bổng từ một tiêu chí riêng lẻ.</p>
                    """
            ),
            new AnnouncementItem(
                    "Nhắc sinh viên tổng hợp hoạt động phục vụ đánh giá rèn luyện",
                    "Ghi lại hoạt động, vai trò tham gia và xác nhận để tránh thiếu minh chứng vào cuối kỳ.",
                    """
                    <h3>Chuẩn bị cho đợt đánh giá điểm rèn luyện</h3><p>Sinh viên nên lưu thông tin hoạt động đã tham gia theo từng học kỳ, gồm tên chương trình, đơn vị tổ chức, thời gian và vai trò. Kiểm tra các tiêu chí trong khung đánh giá hiện hành trước khi tự chấm.</p><p>Việc tham gia hoạt động không tự động bảo đảm được ghi nhận điểm. Khi có sai lệch, phản hồi theo thời hạn được thông báo và cung cấp minh chứng phù hợp qua kênh tiếp nhận chính thức.</p>
                    """
            ),
            new AnnouncementItem(
                    "Nhắc sinh viên theo dõi công nợ học phí trên cổng thông tin",
                    "Đối chiếu kỳ học, khoản thu và trạng thái thanh toán trước khi phát sinh giao dịch mới.",
                    """
                    <h3>Kiểm tra công nợ học phí</h3><p>Trước khi thanh toán, sinh viên cần chọn đúng kỳ học và đọc chi tiết từng khoản phải nộp. Hãy đối chiếu số tiền, mã tham chiếu và hướng dẫn thanh toán đang hiển thị trên cổng chính thức.</p><p>Nếu ngân hàng đã ghi nhận giao dịch nhưng công nợ chưa thay đổi, hãy giữ chứng từ và gửi yêu cầu tra soát. Không thực hiện thanh toán lần hai khi trạng thái giao dịch đầu tiên chưa được xác minh.</p>
                    """
            ),
            new AnnouncementItem(
                    "Giới thiệu kênh tìm hiểu chính sách miễn giảm và hỗ trợ học phí",
                    "Mỗi diện chính sách có điều kiện và hồ sơ riêng; hãy xác nhận đúng nhóm đối tượng trước khi nộp.",
                    """
                    <h3>Tìm hiểu chính sách miễn giảm học phí</h3><p>Sinh viên nên đọc thông báo chính thức để xác định đối tượng, phạm vi hỗ trợ, thời hạn và giấy tờ chứng minh được chấp nhận. Không mặc định hồ sơ đã được duyệt chỉ vì đã nộp đủ tài liệu.</p><p>Nếu chưa xác định mình thuộc diện nào, hãy mô tả hoàn cảnh ở mức cần thiết và hỏi đơn vị phụ trách trước khi tải giấy tờ cá nhân lên hệ thống.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn chuẩn bị hồ sơ đăng ký nội trú ký túc xá",
                    "Kiểm tra đợt tiếp nhận, loại phòng mong muốn và danh sách giấy tờ trước khi gửi hồ sơ.",
                    """
                    <h3>Chuẩn bị đăng ký chỗ ở nội trú</h3><p>Sinh viên hãy theo dõi thông báo của Ban Quản lý Ký túc xá về đợt tiếp nhận, điều kiện ưu tiên, loại phòng và cách xác nhận chỗ ở. Kiểm tra thông tin cá nhân thống nhất giữa biểu mẫu và giấy tờ đính kèm.</p><p>Việc gửi hồ sơ chưa đồng nghĩa đã được bố trí phòng. Hãy theo dõi trạng thái tiếp nhận và chỉ thực hiện thanh toán theo hướng dẫn xuất hiện trên kênh chính thức.</p>
                    """
            ),
            new AnnouncementItem(
                    "Tiếp nhận phản ánh về dịch vụ và tiện ích tại ký túc xá",
                    "Báo rõ khu, tầng, phòng và tình trạng để bộ phận quản lý phân công xử lý đúng đầu mối.",
                    """
                    <h3>Phản ánh dịch vụ ký túc xá</h3><p>Khi cần hỗ trợ về khu sinh hoạt chung, vệ sinh, thiết bị hoặc tiện ích, sinh viên hãy mô tả vị trí cụ thể, thời điểm phát hiện và ảnh hưởng đang gặp phải. Chỉ đính kèm hình ảnh cần thiết, tránh để lộ thông tin cá nhân của người khác.</p><p>Nếu vấn đề có nguy cơ ảnh hưởng an toàn, hãy thông báo trực tiếp cho nhân viên trực theo hướng dẫn tại khu nhà; ticket trên QAUTE giúp lưu vết và theo dõi tiến độ xử lý.</p>
                    """
            ),
            new AnnouncementItem(
                    "Quy trình phản ánh đăng ký nhầm nhóm lớp học phần",
                    "Nêu mã môn, nhóm hiện tại và nhóm mong muốn để Phòng Đào tạo xác minh khả năng điều chỉnh.",
                    """
                    <h3>Đăng ký nhầm nhóm lớp</h3><p>Sau khi phát hiện chọn nhầm nhóm, hãy kiểm tra phiếu đăng ký và thời khóa biểu để xác định chính xác lớp đang được ghi nhận. Ghi lại nhóm hiện tại, nhóm mong muốn và những khung giờ liên quan.</p><p>Nếu chức năng tự điều chỉnh đã đóng, gửi yêu cầu cho Phòng Đào tạo và chờ hướng dẫn. Không tự suy đoán rằng thay đổi đã được lưu chỉ dựa trên nội dung còn hiển thị ở biểu mẫu.</p>
                    """
            ),
            new AnnouncementItem(
                    "Lưu ý khi đề nghị rút hoặc hủy học phần",
                    "Xác nhận thời hạn và hệ quả học vụ trước khi gửi yêu cầu hủy môn.",
                    """
                    <h3>Trước khi rút học phần</h3><p>Hãy đọc kế hoạch đào tạo của đúng học kỳ để biết chức năng rút môn còn mở hay không. Kiểm tra ảnh hưởng đến số tín chỉ đăng ký, tiến độ học tập và các điều kiện đang áp dụng với bạn.</p><p>Nếu cần xem xét trường hợp đặc biệt, trình bày lý do và tài liệu liên quan trong yêu cầu gửi Phòng Đào tạo. Chỉ coi việc rút môn hoàn tất khi hệ thống hoặc đơn vị phụ trách xác nhận trạng thái.</p>
                    """
            ),
            new AnnouncementItem(
                    "Mở kênh rà soát điểm thành phần và điểm tổng kết",
                    "Đối chiếu đề cương, dữ liệu hiển thị và thông báo công bố trước khi phản ánh.",
                    """
                    <h3>Rà soát kết quả học tập</h3><p>Khi thiếu điểm hoặc nghi ngờ sai lệch, hãy xác định đúng học phần, lớp, học kỳ và loại điểm. Đối chiếu các thành phần được quy định trong đề cương cùng tỷ trọng tính điểm nếu có.</p><p>Gửi nội dung cần kiểm tra một cách cụ thể, kèm tài liệu bạn có. Thời hạn phản hồi và cách phúc tra phải theo thông báo chính thức của môn học hoặc đơn vị phụ trách.</p>
                    """
            ),
            new AnnouncementItem(
                    "Tư vấn thủ tục bảo lưu và quay lại học",
                    "Tìm hiểu điều kiện, thời điểm hiệu lực và quy trình trở lại trước khi tạm dừng học.",
                    """
                    <h3>Thông tin cần biết về bảo lưu</h3><p>Trước khi xin bảo lưu, sinh viên nên hỏi rõ điều kiện áp dụng, thời hạn đề nghị, thời điểm bắt đầu hiệu lực và cách xử lý các học phần đã đăng ký. Chuẩn bị giấy tờ theo biểu mẫu được hướng dẫn.</p><p>Khi quay lại học, cần kiểm tra trạng thái sinh viên, chương trình đào tạo hiện hành và quyền đăng ký học phần. Không tự ngừng học khi chưa biết hồ sơ đã được tiếp nhận hoặc phê duyệt đến bước nào.</p>
                    """
            ),
            new AnnouncementItem(
                    "Rà soát thông tin cá nhân trong hồ sơ tốt nghiệp",
                    "Kiểm tra họ tên, ngày sinh, ngành học và tài liệu chuẩn đầu ra trước khi nộp.",
                    """
                    <h3>Chuẩn hóa hồ sơ xét tốt nghiệp</h3><p>Đối chiếu thông tin cá nhân với hồ sơ gốc và rà lại từng thành phần được yêu cầu cho đợt xét. Đặc biệt lưu ý tên chứng chỉ, ngày cấp và trạng thái ghi nhận trên portal.</p><p>Nếu phát hiện sai dữ liệu, ghi rõ trường thông tin, giá trị đang hiển thị và giá trị theo giấy tờ hợp lệ. Gửi yêu cầu điều chỉnh qua kênh chính thức thay vì tự sửa tài liệu do trường phát hành.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn gửi yêu cầu xác nhận tình trạng sinh viên",
                    "Nêu mục đích sử dụng, loại văn bản, số bản và thời gian cần nhận dự kiến.",
                    """
                    <h3>Đề nghị xác nhận sinh viên</h3><p>Để xử lý đúng nhu cầu, hãy nêu cơ quan tiếp nhận, mục đích sử dụng và nội dung cần xác nhận. Nếu có mẫu do nơi nhận cung cấp, đính kèm mẫu đầy đủ để đơn vị phụ trách kiểm tra.</p><p>Trước khi nhận văn bản, hãy rà soát thông tin cá nhân và thời hạn sử dụng nếu có yêu cầu riêng. Không yêu cầu trường xác nhận thông tin ngoài hồ sơ hoặc ngoài thẩm quyền.</p>
                    """
            ),
            new AnnouncementItem(
                    "Kênh hỗ trợ sinh viên khi phát sinh khó khăn đột xuất",
                    "Mô tả vấn đề, ảnh hưởng đến việc học và loại hướng dẫn đang cần.",
                    """
                    <h3>Tiếp nhận đề nghị hỗ trợ</h3><p>Sinh viên có thể gửi yêu cầu khi cần tìm hiểu dịch vụ hỗ trợ hoặc chưa biết đầu mối giải quyết. Hãy mô tả ngắn gọn hoàn cảnh, thời điểm phát sinh và kết quả mong muốn để chuyên viên định tuyến phù hợp.</p><p>Chỉ gửi các tài liệu cần cho việc xem xét và sử dụng kênh bảo mật với thông tin nhạy cảm. Nếu vấn đề thuộc nhiều đơn vị, QAUTE sẽ giúp lưu rõ từng nội dung cần được hướng dẫn.</p>
                    """
            ),
            new AnnouncementItem(
                    "Kiểm tra thành phần hồ sơ học bổng trước khi gửi",
                    "Đối chiếu từng minh chứng với hướng dẫn của đúng chương trình học bổng.",
                    """
                    <h3>Rà soát hồ sơ học bổng</h3><p>Trước khi gửi hồ sơ, hãy kiểm tra biểu mẫu, bảng điểm, thông tin tài khoản nhận hỗ trợ và tài liệu bổ sung được nêu trong thông báo. Đảm bảo tệp dễ đọc, đúng người đứng tên và còn hiệu lực khi quy trình yêu cầu.</p><p>Nếu portal báo thiếu hồ sơ, mở danh sách mục cần bổ sung và xử lý trong cùng hồ sơ đang theo dõi. Không gửi lặp nhiều bộ giống nhau nếu chưa được hướng dẫn.</p>
                    """
            ),
            new AnnouncementItem(
                    "Cách theo dõi kết quả xét học bổng",
                    "Phân biệt danh sách dự kiến, kết quả phê duyệt và thông báo chi trả.",
                    """
                    <h3>Theo dõi tiến trình xét học bổng</h3><p>Khi tra cứu, hãy kiểm tra tên chương trình, học kỳ, ngày đăng thông báo và trạng thái của danh sách. Kết quả sơ bộ có thể còn bước rà soát hoặc phê duyệt trước khi trở thành kết quả chính thức.</p><p>Nếu hồ sơ đủ điều kiện theo thông báo nhưng trạng thái chưa rõ, gửi mã hồ sơ cùng thông tin đợt xét để được đối chiếu. Không yêu cầu dữ liệu cá nhân của ứng viên khác.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn phản hồi điểm rèn luyện chưa chính xác",
                    "Nêu tiêu chí cụ thể và đối chiếu điểm thành phần thay vì chỉ yêu cầu thay đổi tổng điểm.",
                    """
                    <h3>Đối chiếu điểm rèn luyện</h3><p>Sinh viên nên xem kết quả ở từng tiêu chí, trạng thái minh chứng và thông báo chốt điểm của học kỳ. Nếu có sai lệch, mô tả tiêu chí liên quan cùng căn cứ mà bạn muốn đơn vị phụ trách kiểm tra.</p><p>Phản hồi cần gửi trong khoảng thời gian được thông báo. Khi kết quả đã khóa, hãy hỏi rõ quy trình rà soát sau khóa kỳ thay vì tạo nhiều yêu cầu giống nhau.</p>
                    """
            ),
            new AnnouncementItem(
                    "Chuẩn bị minh chứng cho hoạt động rèn luyện",
                    "Tài liệu cần rõ hoạt động, thời gian và căn cứ xác nhận người tham gia.",
                    """
                    <h3>Minh chứng đánh giá rèn luyện</h3><p>Trước khi tải tài liệu, hãy kiểm tra tên chương trình, thời điểm diễn ra, đơn vị xác nhận và thông tin nhận diện người tham gia. Với nhiều hoạt động, đặt tên tệp dễ hiểu để người xét duyệt phân biệt từng minh chứng.</p><p>Không tự tạo hoặc chỉnh sửa giấy xác nhận. Nếu hoạt động trực tuyến không cấp chứng nhận riêng, hãy hỏi đơn vị tổ chức về minh chứng thay thế được chấp nhận.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn yêu cầu cấp lại giấy tờ sinh viên",
                    "Nêu tên văn bản, thông tin cần sửa và mã hoặc ngày cấp bản trước nếu có.",
                    """
                    <h3>Cấp lại hoặc điều chỉnh giấy tờ</h3><p>Đối với giấy xác nhận, bảng điểm hoặc thẻ sinh viên cần cấp lại, hãy nêu rõ lý do và kiểm tra thông tin trên hồ sơ gốc. Nếu văn bản cũ sai dữ liệu, chỉ ra phần sai để đơn vị phát hành xác định cách xử lý.</p><p>Không tự chỉnh sửa tệp PDF hoặc bản giấy do trường cấp. Hỏi rõ có cần nộp lại, thu hồi hay xác minh văn bản trước khi nhận bản mới.</p>
                    """
            ),
            new AnnouncementItem(
                    "Lưu giữ và tra cứu chứng từ học phí",
                    "Giữ biên nhận, mã giao dịch và thông tin kỳ học để phục vụ đối soát về sau.",
                    """
                    <h3>Quản lý chứng từ thanh toán</h3><p>Sau mỗi giao dịch, hãy lưu biên nhận có thông tin số tiền, thời điểm và mã tham chiếu. Đối chiếu dữ liệu ngân hàng với chứng từ được ghi nhận trên cổng sinh viên vì hai nguồn có thể thể hiện trạng thái khác nhau trong thời gian xử lý.</p><p>Nếu cần tải lại chứng từ cũ hoặc điều chỉnh thông tin, nêu đúng kỳ học và giao dịch liên quan. Không sửa nội dung chứng từ bằng phần mềm cá nhân.</p>
                    """
            ),
            new AnnouncementItem(
                    "Khuyến nghị kiểm tra thông tin trước khi thanh toán học phí",
                    "Xác nhận khoản thu, người thụ hưởng, số tiền và mã tham chiếu trước khi xác nhận.",
                    """
                    <h3>Thanh toán đúng khoản cần nộp</h3><p>Trước khi hoàn tất giao dịch, hãy kiểm tra kỳ học, loại khoản thu và số tiền trên trang chính thức. Nếu thanh toán bằng chuyển khoản, dùng đúng nội dung được hướng dẫn cho giao dịch đang thực hiện.</p><p>Nếu thông tin ở portal khác với thông báo thu phí, dừng thao tác và gửi yêu cầu đối chiếu. Sau thanh toán, lưu chứng từ và kiểm tra trạng thái công nợ sau thời gian xử lý được công bố.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông báo về quy trình đối soát khoản học phí đã nộp",
                    "Chuẩn bị chứng từ ngân hàng và dữ liệu công nợ khi khoản nộp chưa được phân bổ.",
                    """
                    <h3>Đối soát giao dịch học phí</h3><p>Để tra soát, cung cấp mã sinh viên, kỳ học, số tiền, thời điểm thanh toán và mã giao dịch. Đính kèm chứng từ thể hiện kết quả giao dịch, đồng thời nêu rõ trạng thái công nợ đang hiển thị.</p><p>Không công khai toàn bộ dữ liệu tài khoản trong nội dung trao đổi. Chỉ thực hiện giao dịch bổ sung sau khi bộ phận tài chính xác nhận kết quả của khoản đã nộp.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn tìm hiểu thủ tục gia hạn học phí",
                    "Tách biệt thời hạn gửi đề nghị và thời hạn thanh toán được ghi trong thông báo.",
                    """
                    <h3>Đề nghị gia hạn nghĩa vụ học phí</h3><p>Sinh viên cần đọc mốc tiếp nhận hồ sơ và thời hạn thanh toán áp dụng cho kỳ hiện tại. Khi gửi đề nghị, nêu rõ kỳ học, khoản xin gia hạn, lý do và kế hoạch dự kiến hoàn thành.</p><p>Hồ sơ đang chờ xử lý không mặc nhiên kéo dài thời hạn. Hãy theo dõi quyết định chính thức và hỏi về phương án phù hợp nếu lịch thanh toán dự kiến thay đổi.</p>
                    """
            ),
            new AnnouncementItem(
                    "Rà soát giấy tờ đề nghị miễn giảm học phí",
                    "Kiểm tra đúng đối tượng, giấy tờ chứng minh và hình thức tiếp nhận trong hướng dẫn.",
                    """
                    <h3>Hoàn thiện hồ sơ miễn giảm</h3><p>Đối chiếu trường hợp của bạn với danh sách đối tượng trong thông báo và chuẩn bị loại giấy tờ được yêu cầu. Kiểm tra thông tin người đứng tên, thời hạn xác nhận và tính thống nhất giữa các tài liệu.</p><p>Nếu chưa thể cung cấp đúng tài liệu, liên hệ bộ phận phụ trách để hỏi về phương án thay thế được chấp nhận. Việc nộp hồ sơ chỉ là bước đề nghị xem xét, không tự động đồng nghĩa được hưởng chính sách.</p>
                    """
            ),
            new AnnouncementItem(
                    "Cách xử lý khi cổng thanh toán học phí báo lỗi",
                    "Kiểm tra lịch sử giao dịch trước khi thử thanh toán lại để tránh ghi nợ trùng.",
                    """
                    <h3>Thanh toán gặp lỗi hoặc mất kết nối</h3><p>Nếu trang thanh toán bị treo, mã QR hết hạn hoặc trình duyệt không nhận phản hồi, hãy kiểm tra lịch sử ngân hàng và portal trước khi khởi tạo lệnh mới. Ghi lại thời điểm, thông báo lỗi và mã giao dịch nếu có.</p><p>Chỉ truy cập đường dẫn thanh toán từ cổng chính thức. Khi hai hệ thống chưa thống nhất trạng thái, gửi yêu cầu cho bộ phận tài chính và chờ hướng dẫn xác minh.</p>
                    """
            ),
            new AnnouncementItem(
                    "Chuẩn bị tài liệu cho hồ sơ đăng ký ký túc xá",
                    "Đọc danh mục giấy tờ của đúng đợt và kiểm tra chất lượng tệp đính kèm.",
                    """
                    <h3>Hoàn thiện hồ sơ nội trú</h3><p>Trước khi gửi, hãy rà soát thông tin sinh viên, lựa chọn khu hoặc loại phòng nếu quy trình yêu cầu và các giấy tờ được liệt kê trong thông báo tiếp nhận. Tài liệu cần đủ trang, rõ chữ và không bị cắt mất phần xác nhận.</p><p>Nếu thuộc diện ưu tiên, chọn đúng nhóm và chỉ nộp minh chứng phù hợp. Lưu mã hồ sơ để theo dõi phản hồi hoặc yêu cầu bổ sung từ Ban Quản lý Ký túc xá.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn yêu cầu gia hạn thời gian lưu trú",
                    "Gửi đề nghị trước khi hết thời hạn hợp đồng và chờ xác nhận của đơn vị quản lý.",
                    """
                    <h3>Gia hạn chỗ ở ký túc xá</h3><p>Sinh viên cần kiểm tra thời hạn lưu trú hiện tại, điều kiện được tiếp tục ở và các khoản cần hoàn tất. Khi gửi đề nghị, nêu mã phòng hoặc thông tin chỗ ở, thời gian mong muốn gia hạn và nội dung cần làm rõ.</p><p>Không mặc định chỗ ở tự động được giữ sau thời điểm kết thúc. Chờ phản hồi chính thức và thực hiện thủ tục theo hướng dẫn của ban quản lý.</p>
                    """
            ),
            new AnnouncementItem(
                    "Tiếp nhận yêu cầu sửa chữa trang thiết bị ký túc xá",
                    "Mô tả hạng mục hỏng, vị trí và mức độ ảnh hưởng để xác định thứ tự xử lý.",
                    """
                    <h3>Yêu cầu sửa chữa trong ký túc xá</h3><p>Khi báo hỏng, hãy ghi khu nhà, tầng, phòng và tên thiết bị; mô tả hiện trạng cùng thời điểm phát hiện. Ảnh hiện trường có thể giúp nhận diện sự cố nhưng cần tránh chụp lộ thông tin riêng tư của người ở cùng.</p><p>Không tự tháo lắp thiết bị điện hoặc hệ thống kỹ thuật. Với sự cố có nguy cơ gây mất an toàn, thông báo ngay cho nhân viên trực ngoài việc tạo ticket theo dõi.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn phản ánh sự cố điện nước tại ký túc xá",
                    "Báo vị trí và biểu hiện sự cố; ưu tiên thông báo trực tiếp khi có nguy cơ mất an toàn.",
                    """
                    <h3>Sự cố điện, nước và tiện ích chung</h3><p>Hãy nêu hiện tượng cụ thể như mất điện cục bộ, nước yếu, rò rỉ hoặc khu vực bị ảnh hưởng. Ghi thời gian xảy ra và cho biết sự cố tác động tới một phòng hay nhiều khu vực.</p><p>Nếu phát hiện nguy cơ chập điện, ngập nước hoặc hư hỏng có thể gây nguy hiểm, tránh tiếp xúc với khu vực đó và báo nhân viên trực ngay. Không tự sửa chữa hệ thống kỹ thuật chung.</p>
                    """
            ),
            new AnnouncementItem(
                    "Nhắc bảo vệ tài khoản truy cập hệ thống sinh viên",
                    "Không chia sẻ mật khẩu, mã xác thực hoặc dùng chung tài khoản cá nhân.",
                    """
                    <h3>An toàn tài khoản người dùng</h3><p>Mỗi sinh viên cần giữ thông tin đăng nhập riêng tư, dùng mật khẩu khó đoán và đăng xuất trên thiết bị dùng chung. Không chia sẻ mã OTP hoặc chấp thuận yêu cầu đăng nhập mà bạn không khởi tạo.</p><p>Nếu nghi ngờ tài khoản bị truy cập trái phép, đổi mật khẩu từ kênh chính thức và gửi yêu cầu hỗ trợ CNTT. Nêu thời điểm bất thường nhưng không đưa mật khẩu hiện tại vào ticket.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông tin vận hành và bảo trì cổng thông tin",
                    "Theo dõi thông báo trạng thái hệ thống trước khi thực hiện thao tác quan trọng.",
                    """
                    <h3>Thông tin vận hành portal</h3><p>Kế hoạch bảo trì hoặc nâng cấp dịch vụ sẽ được cập nhật trên kênh thông báo chính thức khi có lịch xác nhận. Trước thời điểm bảo trì, hãy lưu lại thông tin đang nhập và hoàn tất những thao tác có thể kết thúc an toàn.</p><p>Nếu không truy cập được portal ngoài khoảng bảo trì đã công bố, ghi thời gian, trình duyệt và thông báo lỗi rồi gửi yêu cầu đến bộ phận CNTT. Không gửi thông tin đăng nhập trong ảnh chụp màn hình.</p>
                    """
            ),
            new AnnouncementItem(
                    "Khuyến nghị nhận diện thông báo giả mạo và liên kết đáng ngờ",
                    "Kiểm tra địa chỉ truy cập và không mở tệp lạ từ nguồn chưa được xác minh.",
                    """
                    <h3>Nhận diện nguy cơ an toàn thông tin</h3><p>Hãy kiểm tra kỹ tên miền, người gửi và mục đích của thông báo trước khi đăng nhập hoặc tải tệp. Những thông điệp gây áp lực phải cung cấp mật khẩu, OTP hoặc dữ liệu cá nhân cần được xem xét thận trọng.</p><p>Nếu đã mở một liên kết đáng ngờ, hãy đổi mật khẩu từ thiết bị đáng tin cậy nếu cần và báo bộ phận CNTT để được hướng dẫn. Giữ lại thông tin về thời điểm, địa chỉ trang và biểu hiện quan sát được.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn khôi phục quyền truy cập tài khoản",
                    "Chuẩn bị thông tin nhận diện theo quy trình xác minh chính thức của hệ thống.",
                    """
                    <h3>Khôi phục tài khoản khi không đăng nhập được</h3><p>Sinh viên nên bắt đầu từ chức năng khôi phục tài khoản hoặc mật khẩu trên portal. Làm theo các bước xác minh được hệ thống yêu cầu và kiểm tra cả thư mục thư rác nếu mã xác minh được gửi qua email.</p><p>Nếu thông tin khôi phục không còn sử dụng được, liên hệ bộ phận CNTT bằng kênh hỗ trợ chính thức. Không nhờ người khác cung cấp mã xác minh thay hoặc gửi giấy tờ cá nhân qua kênh không bảo mật.</p>
                    """
            ),
            new AnnouncementItem(
                    "Tiếp nhận báo lỗi kỹ thuật trên cổng sinh viên",
                    "Gửi đường dẫn trang, thao tác trước khi lỗi và thông báo hiển thị để tái hiện sự cố.",
                    """
                    <h3>Báo cáo lỗi trên portal</h3><p>Để việc kiểm tra nhanh hơn, hãy mô tả từng bước trước khi lỗi xuất hiện, thời điểm gặp lỗi, loại thiết bị và trình duyệt. Chụp thông báo lỗi nếu có thể mà không để lộ dữ liệu tài khoản.</p><p>Trước khi thử lại thao tác có thể tạo giao dịch, hãy kiểm tra xem dữ liệu đã được lưu hay chưa. Bộ phận hỗ trợ sẽ dùng thông tin được cung cấp để xác định bước tiếp theo.</p>
                    """
            ),
            new AnnouncementItem(
                    "Nhắc tuân thủ quy trình mượn và trả tài liệu thư viện",
                    "Kiểm tra tình trạng tài liệu và xác nhận giao dịch trên tài khoản thư viện.",
                    """
                    <h3>Mượn và trả tài liệu đúng quy trình</h3><p>Khi nhận tài liệu, hãy kiểm tra tình trạng bản sách và báo nhân viên thư viện nếu phát hiện hư hỏng đã có. Khi trả, xác nhận giao dịch hoàn tất thay vì chỉ để tài liệu tại khu vực trả.</p><p>Hạn trả, giới hạn số lượng và quy định xử lý quá hạn cần căn cứ hướng dẫn hiện hành của thư viện. Theo dõi tài khoản để phát hiện khoản mượn chưa được ghi nhận đã trả.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn tra cứu tài liệu trong danh mục thư viện",
                    "Thử nhiều trường tìm kiếm và kiểm tra trạng thái bản tài liệu trước khi đến mượn.",
                    """
                    <h3>Tra cứu tài liệu hiệu quả</h3><p>Có thể tìm bằng tên sách, tác giả, chủ đề hoặc thông tin nhận diện được cung cấp trong danh mục. Sau khi tìm thấy kết quả, hãy kiểm tra cơ sở lưu trữ, vị trí kệ và trạng thái có thể mượn.</p><p>Nếu tài liệu không xuất hiện, thử bỏ bộ lọc quá hẹp hoặc tìm bằng từ khóa chính. Khi nghi dữ liệu danh mục chưa cập nhật, gửi tên tài liệu và thông tin xuất bản còn biết cho thư viện.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hỗ trợ tài khoản sử dụng dịch vụ thư viện",
                    "Kiểm tra email, quyền truy cập và trạng thái tài khoản trước khi yêu cầu mở lại.",
                    """
                    <h3>Quản lý tài khoản thư viện</h3><p>Nếu không đăng nhập được vào dịch vụ thư viện, hãy kiểm tra đúng tài khoản được cấp và thử quy trình khôi phục chính thức. Một số dịch vụ có thể yêu cầu trạng thái bạn đọc hoặc thông tin sinh viên đã được đồng bộ.</p><p>Khi cần hỗ trợ, cung cấp mã sinh viên và thông báo lỗi nhưng không gửi mật khẩu. Nếu quyền truy cập bị tạm dừng do một khoản mượn hoặc hồ sơ chưa hoàn tất, hỏi rõ điều kiện để mở lại.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn xin gia hạn thời gian mượn tài liệu",
                    "Gửi yêu cầu trước hạn trả và kiểm tra điều kiện gia hạn cho từng tài liệu.",
                    """
                    <h3>Gia hạn tài liệu thư viện</h3><p>Hãy tra cứu các tài liệu đang mượn, hạn trả và tình trạng đặt chỗ của người khác trước khi gửi yêu cầu gia hạn. Việc gia hạn có thể bị giới hạn với một số loại tài liệu hoặc khi có yêu cầu mượn tiếp theo.</p><p>Sau khi thao tác, kiểm tra ngày hạn mới trong tài khoản. Nếu portal không cập nhật, lưu mã tài liệu và thời điểm gửi yêu cầu để thư viện xác minh.</p>
                    """
            ),
            new AnnouncementItem(
                    "Giới thiệu nguồn tài liệu học thuật và cơ sở dữ liệu điện tử",
                    "Ưu tiên nguồn thư viện và tài liệu có thông tin xuất bản rõ ràng khi học tập, nghiên cứu.",
                    """
                    <h3>Tìm tài liệu phục vụ học tập và nghiên cứu</h3><p>Danh mục thư viện có thể bao gồm giáo trình, sách chuyên khảo, luận văn và nguồn tài liệu điện tử theo quyền truy cập. Khi chọn tài liệu, hãy xem tác giả, năm xuất bản, nhà xuất bản và mức độ phù hợp với chủ đề.</p><p>Nếu liên kết điện tử yêu cầu đăng nhập, sử dụng hướng dẫn truy cập của thư viện. Tôn trọng điều kiện sử dụng và bản quyền; không chia sẻ công khai tệp bị giới hạn quyền truy cập.</p>
                    """
            ),
            new AnnouncementItem(
                    "Cập nhật thông tin chuẩn bị thực tập doanh nghiệp",
                    "Sinh viên cần kiểm tra điều kiện thực tập, thời gian và yêu cầu từ đơn vị tiếp nhận.",
                    """
                    <h3>Chuẩn bị cho kỳ thực tập</h3><p>Trước khi đăng ký thực tập, hãy đọc yêu cầu của ngành và kế hoạch do đơn vị phụ trách công bố. Xác nhận điều kiện học vụ, hình thức thực tập, thời gian dự kiến và giấy tờ cần nộp.</p><p>Khi đã có nơi tiếp nhận, lưu thông tin người hướng dẫn, đầu mối liên hệ và các mốc báo cáo. Nếu lịch thực tập thay đổi, cập nhật cho đơn vị phụ trách theo quy trình thay vì chỉ trao đổi miệng.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông tin kênh tiếp cận cơ hội tuyển dụng dành cho sinh viên",
                    "Đọc kỹ yêu cầu vị trí, thời hạn ứng tuyển và cách thức doanh nghiệp tiếp nhận hồ sơ.",
                    """
                    <h3>Tìm kiếm cơ hội tuyển dụng</h3><p>Khi xem tin tuyển dụng, hãy đối chiếu mô tả công việc, kỹ năng yêu cầu, địa điểm, hình thức làm việc và hạn nhận hồ sơ. Chỉ gửi hồ sơ qua kênh được xác minh của doanh nghiệp hoặc đơn vị kết nối.</p><p>Nếu cần hỗ trợ xác minh một cơ hội được chia sẻ, gửi đường dẫn và thông tin công khai của tin tuyển dụng cho Trung tâm Quan hệ Doanh nghiệp. Không chuyển phí tuyển dụng khi chưa xác minh tính hợp lệ.</p>
                    """
            ),
            new AnnouncementItem(
                    "Mời sinh viên tham khảo buổi hướng dẫn CV và phỏng vấn",
                    "Chuẩn bị CV theo vị trí ứng tuyển và theo dõi lịch hoạt động do đơn vị phụ trách công bố.",
                    """
                    <h3>Chuẩn bị CV chuyên nghiệp</h3><p>CV nên làm rõ kỹ năng, dự án, kinh nghiệm thực tập và kết quả có thể kiểm chứng; điều chỉnh thứ tự thông tin theo vị trí ứng tuyển. Sinh viên có thể chuẩn bị thêm một ví dụ cụ thể về cách mình giải quyết vấn đề trong dự án.</p><p>Hoạt động hướng dẫn CV, phỏng vấn hoặc giao lưu doanh nghiệp sẽ được cập nhật theo lịch chính thức. Khi đăng ký tham dự, kiểm tra thông tin thời gian, địa điểm và yêu cầu chuẩn bị.</p>
                    """
            ),
            new AnnouncementItem(
                    "Rà soát hồ sơ trước khi gửi doanh nghiệp tiếp nhận thực tập",
                    "Đảm bảo CV, giấy giới thiệu và thông tin liên hệ thống nhất trước khi gửi.",
                    """
                    <h3>Hoàn thiện hồ sơ thực tập</h3><p>Kiểm tra các thành phần mà doanh nghiệp và nhà trường yêu cầu: CV, thư ứng tuyển, bảng điểm hoặc giấy giới thiệu nếu có. Tên tệp cần rõ ràng, thông tin liên hệ sử dụng được và nội dung không mâu thuẫn giữa các tài liệu.</p><p>Trước khi gửi, xác nhận đúng người nhận và định dạng tệp. Nếu doanh nghiệp yêu cầu bổ sung, ghi lại thời hạn và gửi bản cập nhật qua cùng kênh liên hệ để tránh nhầm phiên bản.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn theo dõi hồ sơ ứng tuyển đã gửi",
                    "Lưu vị trí ứng tuyển, ngày gửi và phản hồi để chủ động theo dõi quá trình tuyển dụng.",
                    """
                    <h3>Quản lý các hồ sơ ứng tuyển</h3><p>Sinh viên nên lưu bản CV đã gửi cùng tên vị trí, doanh nghiệp, ngày nộp và cách thức liên hệ. Khi được mời phỏng vấn, đọc kỹ yêu cầu chuẩn bị và xác nhận thời gian với nhà tuyển dụng.</p><p>Nếu cần chỉnh sửa hồ sơ sau khi nộp, hỏi doanh nghiệp xem có thể cập nhật hay không trước khi gửi thêm bản khác. Không dùng một thư ứng tuyển chung mà chưa rà lại tên vị trí và tổ chức.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông tin đăng ký đề tài nghiên cứu khoa học sinh viên",
                    "Chuẩn bị mục tiêu, câu hỏi nghiên cứu, phạm vi và kế hoạch thực hiện trước khi đăng ký.",
                    """
                    <h3>Đề xuất đề tài nghiên cứu</h3><p>Một đề xuất tốt cần nêu vấn đề muốn nghiên cứu, mục tiêu, phương pháp dự kiến, sản phẩm đầu ra và kế hoạch theo giai đoạn. Sinh viên nên trao đổi với giảng viên hướng dẫn để xác định phạm vi có thể hoàn thành.</p><p>Biểu mẫu, tiêu chí đánh giá và thời hạn đăng ký phải theo thông báo của đơn vị phụ trách nghiên cứu khoa học. Trước khi gửi, kiểm tra tên thành viên và thông tin người hướng dẫn thống nhất.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông tin hội thảo học thuật và chia sẻ kết quả nghiên cứu",
                    "Theo dõi chủ đề, diễn giả, hình thức tham gia và yêu cầu đăng ký trong thông báo chương trình.",
                    """
                    <h3>Tham gia hoạt động học thuật</h3><p>Các hội thảo là dịp để sinh viên trao đổi phương pháp, tiếp cận kết quả mới và kết nối với nhóm nghiên cứu. Trước khi tham gia, hãy đọc mục tiêu chương trình và chuẩn bị câu hỏi liên quan đến chủ đề.</p><p>Thông tin giờ tổ chức, địa điểm hoặc đường dẫn trực tuyến được xác nhận trong thông báo riêng của từng sự kiện. Chỉ sử dụng kênh đăng ký do đơn vị tổ chức cung cấp.</p>
                    """
            ),
            new AnnouncementItem(
                    "Khuyến nghị về đạo đức và tính trung thực trong nghiên cứu",
                    "Trích dẫn nguồn, lưu dữ liệu có trách nhiệm và mô tả trung thực phương pháp thực hiện.",
                    """
                    <h3>Thực hành nghiên cứu có trách nhiệm</h3><p>Khi thực hiện đề tài, cần ghi nguồn tài liệu, phân biệt dữ liệu tự thu thập với dữ liệu từ nguồn khác và lưu lại cách xử lý. Không sao chép nội dung hoặc điều chỉnh kết quả để làm cho kết luận phù hợp với giả thuyết.</p><p>Nếu đề tài sử dụng dữ liệu cá nhân hoặc cần khảo sát người tham gia, hãy hỏi giảng viên hướng dẫn về yêu cầu xin phép, bảo mật và cách lưu trữ phù hợp trước khi thu thập.</p>
                    """
            ),
            new AnnouncementItem(
                    "Rà soát thuyết minh và tài liệu đăng ký đề tài",
                    "Kiểm tra mục tiêu, tiến độ, phân công và các phụ lục bắt buộc trong bộ hồ sơ.",
                    """
                    <h3>Kiểm tra hồ sơ đề tài nghiên cứu</h3><p>Trước khi nộp, hãy đối chiếu thuyết minh với biểu mẫu hiện hành và xác nhận các phần về mục tiêu, phương pháp, kế hoạch, kinh phí nếu có, cùng sản phẩm dự kiến. Những phụ lục được yêu cầu cần đính kèm đúng thứ tự.</p><p>Rà tên đề tài, người hướng dẫn và danh sách thành viên trên mọi trang. Nếu hồ sơ cần chỉnh sửa, giữ rõ phiên bản mới để tránh gửi nhầm tài liệu đã thay thế.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn gửi đăng ký tham dự hội thảo khoa học",
                    "Điền thông tin liên hệ chính xác và chọn đúng hình thức tham gia theo thông báo.",
                    """
                    <h3>Đăng ký tham dự hội thảo</h3><p>Khi đăng ký, chọn đúng phiên hoặc chủ đề nếu chương trình có nhiều nhánh. Kiểm tra email liên hệ, đơn vị học tập và thông tin cần thiết để ban tổ chức gửi xác nhận.</p><p>Nếu không nhận được phản hồi, tra cứu trạng thái đăng ký hoặc kiểm tra thư rác trước khi gửi yêu cầu hỗ trợ. Nếu không thể tham dự sau khi đăng ký, thông báo cho ban tổ chức khi quy trình yêu cầu xác nhận lại.</p>
                    """
            ),
            new AnnouncementItem(
                    "Nhắc sinh viên kiểm tra thông tin bảo hiểm y tế",
                    "Đối chiếu thời hạn và thông tin định danh trên giấy tờ với hướng dẫn của đơn vị phụ trách.",
                    """
                    <h3>Kiểm tra thông tin bảo hiểm y tế</h3><p>Sinh viên nên xem thời hạn sử dụng, dữ liệu định danh và trạng thái tham gia bảo hiểm theo kênh tra cứu chính thức. Nếu thông tin sai hoặc chưa được ghi nhận, chuẩn bị mã định danh và tài liệu liên quan để đề nghị kiểm tra.</p><p>Không đăng công khai ảnh thẻ có đủ mã định danh. Với các câu hỏi về quyền lợi hoặc nơi tiếp nhận khám chữa bệnh, hãy tham khảo hướng dẫn của cơ quan bảo hiểm hoặc đơn vị y tế có thẩm quyền.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông tin khuyến nghị chăm sóc sức khỏe trong thời gian học tập",
                    "Duy trì lịch sinh hoạt hợp lý và tìm hỗ trợ y tế khi vấn đề ảnh hưởng đến học tập.",
                    """
                    <h3>Chủ động chăm sóc sức khỏe</h3><p>Khối lượng học tập và lịch thi có thể khiến thời gian nghỉ ngơi thay đổi. Sinh viên nên duy trì sinh hoạt đều đặn, ăn uống đầy đủ và tìm tư vấn chuyên môn khi triệu chứng kéo dài hoặc ảnh hưởng hoạt động hằng ngày.</p><p>Thông báo này mang tính hướng dẫn chung, không thay thế đánh giá của nhân viên y tế. Nếu cần hỗ trợ tại trường, hãy liên hệ đầu mối y tế được công bố và cung cấp thông tin vừa đủ để được hướng dẫn.</p>
                    """
            ),
            new AnnouncementItem(
                    "Kênh liên hệ khi sinh viên cần hỗ trợ y tế tại trường",
                    "Nêu vị trí, tình trạng và cách liên hệ lại để nhân viên phụ trách phản hồi phù hợp.",
                    """
                    <h3>Tiếp nhận yêu cầu hỗ trợ y tế</h3><p>Khi tìm hỗ trợ, hãy mô tả ngắn gọn tình trạng cần giải quyết, nơi đang có mặt và cách liên hệ lại. Tránh đăng công khai chi tiết sức khỏe riêng tư trong khu vực trao đổi chung.</p><p>Với tình huống khẩn cấp, hãy liên hệ trực tiếp dịch vụ khẩn cấp hoặc nhân viên trực theo hướng dẫn tại cơ sở; không chỉ chờ phản hồi ticket. QAUTE hỗ trợ lưu yêu cầu và theo dõi các câu hỏi hành chính liên quan.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn rà soát hồ sơ liên quan đến bảo hiểm y tế",
                    "Kiểm tra giấy tờ định danh, thông tin tham gia và các mục cần bổ sung theo thông báo.",
                    """
                    <h3>Hoàn thiện hồ sơ bảo hiểm y tế</h3><p>Đọc kỹ yêu cầu cho trường hợp của bạn để xác định hồ sơ cần nộp mới, điều chỉnh hay cập nhật. Đối chiếu thông tin họ tên và mã định danh giữa các giấy tờ; bảo đảm bản điện tử không bị mờ hoặc mất góc.</p><p>Nếu cần gửi tài liệu có dữ liệu cá nhân, sử dụng kênh được hướng dẫn và chỉ cung cấp phần cần thiết. Giữ lại xác nhận tiếp nhận để tra cứu khi hồ sơ chưa được xử lý.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông tin đăng ký tư vấn sức khỏe và hỗ trợ ban đầu",
                    "Nêu chủ đề muốn trao đổi và nhu cầu hỗ trợ để được hướng dẫn đến đúng đầu mối.",
                    """
                    <h3>Tìm hiểu dịch vụ tư vấn sức khỏe</h3><p>Sinh viên có thể gửi câu hỏi về quy trình tiếp cận dịch vụ, lịch tư vấn hoặc giấy tờ hành chính cần chuẩn bị. Chỉ ghi thông tin sức khỏe cần thiết cho việc điều phối; không đưa hồ sơ y tế chi tiết vào nơi công khai.</p><p>Khả năng tiếp nhận và hình thức tư vấn thực tế phụ thuộc lịch làm việc được công bố. Nếu tình trạng cần hỗ trợ khẩn cấp, hãy tìm trợ giúp trực tiếp thay vì chờ lịch hẹn thông thường.</p>
                    """
            ),
            new AnnouncementItem(
                    "Tiếp nhận đề nghị sửa chữa cơ sở vật chất trong trường",
                    "Ghi địa điểm cụ thể, hạng mục và dấu hiệu hư hỏng để đội kỹ thuật kiểm tra.",
                    """
                    <h3>Gửi yêu cầu sửa chữa cơ sở vật chất</h3><p>Khi phát hiện hư hỏng, hãy nêu tòa nhà, tầng, phòng hoặc khu vực và mô tả hiện trạng. Hình ảnh toàn cảnh và cận cảnh phù hợp có thể giúp xác định vị trí mà không cần trao đổi nhiều lần.</p><p>Không tự sửa hạng mục điện, kết cấu hoặc thiết bị chuyên dụng. Nếu phát hiện nguy cơ mất an toàn cho người sử dụng, thông báo trực tiếp cho bộ phận quản lý khu vực ngay khi có thể.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn phản ánh điều kiện sử dụng phòng học",
                    "Nêu mã phòng, thời điểm sử dụng và vấn đề ảnh hưởng đến buổi học.",
                    """
                    <h3>Phản ánh tình trạng phòng học</h3><p>Những vấn đề như bàn ghế hư hỏng, vệ sinh chưa đạt, ánh sáng hoặc thông gió không phù hợp nên được ghi nhận với mã phòng và thời điểm cụ thể. Thông tin chính xác giúp đối chiếu lịch sử sử dụng và phân công xử lý.</p><p>Nếu sự cố ảnh hưởng đến lớp đang diễn ra, báo giảng viên hoặc bộ phận quản lý phòng để xử lý tức thời, đồng thời tạo yêu cầu để theo dõi kết quả sau đó.</p>
                    """
            ),
            new AnnouncementItem(
                    "Quản lý và báo hỏng thiết bị phục vụ giảng dạy",
                    "Mô tả thiết bị, mã nhận diện nếu có và biểu hiện lỗi thay vì tự tháo lắp.",
                    """
                    <h3>Báo cáo lỗi thiết bị giảng dạy</h3><p>Khi máy chiếu, máy tính, micro hoặc thiết bị thực hành gặp lỗi, hãy ghi mã phòng, tên thiết bị, trạng thái đèn báo và thao tác ngay trước khi sự cố xuất hiện. Nếu có mã tài sản, bổ sung mã đó để tránh nhầm thiết bị.</p><p>Không tự cài phần mềm không được phép hoặc thay đổi cấu hình hệ thống. Với thiết bị phục vụ giờ học, thông báo cho người phụ trách phòng để có phương án dự phòng trong khi chờ sửa chữa.</p>
                    """
            ),
            new AnnouncementItem(
                    "Quy trình gửi thông tin báo hỏng và theo dõi sửa chữa",
                    "Dùng một yêu cầu cho một sự cố rõ ràng và bổ sung ảnh khi trạng thái thay đổi.",
                    """
                    <h3>Theo dõi yêu cầu báo hỏng</h3><p>Một báo cáo tốt cần mô tả sự cố, vị trí, thời điểm, mức độ ảnh hưởng và người có thể xác nhận hiện trạng. Khi được yêu cầu bổ sung, trả lời trong ticket cũ để lịch sử trao đổi tập trung.</p><p>Nếu vấn đề đã được khắc phục, cập nhật kết quả quan sát hoặc xác nhận từ đơn vị sử dụng. Nếu sự cố tái diễn, nêu rõ thời điểm và khác biệt so với lần báo trước.</p>
                    """
            ),
            new AnnouncementItem(
                    "Lưu ý khi đề nghị sử dụng phòng học ngoài lịch thường kỳ",
                    "Gửi mục đích, thời gian, số người và nhu cầu thiết bị để đơn vị quản lý kiểm tra.",
                    """
                    <h3>Đề nghị sử dụng phòng học</h3><p>Đối với buổi học bù, sinh hoạt nhóm hoặc hoạt động học thuật, cần gửi mục đích sử dụng, thời gian bắt đầu và kết thúc, số người dự kiến cùng thiết bị cần dùng. Chờ xác nhận phòng trống trước khi thông báo địa điểm cho người tham gia.</p><p>Việc gửi đề nghị không đồng nghĩa phòng đã được cấp. Nếu kế hoạch thay đổi, hãy cập nhật sớm để đơn vị quản lý giải phóng hoặc điều phối lại lịch sử dụng.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông tin rà soát lịch thi theo học kỳ",
                    "Kiểm tra môn thi, ca thi, phòng thi và thông báo điều chỉnh mới nhất.",
                    """
                    <h3>Rà soát lịch thi cá nhân</h3><p>Sinh viên nên đối chiếu lịch thi của từng học phần với thời khóa biểu và các cập nhật sau đó. Kiểm tra ngày, giờ, phòng thi, hình thức thi cùng hướng dẫn nhận diện hoặc giấy tờ cần mang theo.</p><p>Nếu phát hiện trùng lịch hoặc dữ liệu chưa thống nhất, gửi mã lớp học phần và ảnh lịch hiển thị cho đơn vị khảo thí/đào tạo theo hướng dẫn. Không dựa vào ảnh chụp cũ khi đã có thông báo thay đổi.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn tìm hiểu quy trình phúc khảo kết quả thi",
                    "Đọc thời hạn, phạm vi xem xét và cách nộp đơn được công bố cho từng kỳ thi.",
                    """
                    <h3>Thông tin về phúc khảo</h3><p>Trước khi đề nghị phúc khảo, hãy xem thông báo của kỳ thi để hiểu phạm vi xem xét, cách thức nộp và thời hạn nhận hồ sơ. Chuẩn bị đúng thông tin học phần, ca thi và dữ liệu nhận diện bài thi theo yêu cầu.</p><p>Phúc khảo là quy trình rà soát theo quy định, không bảo đảm kết quả sẽ thay đổi. Khi gửi đơn, hãy lưu mã tiếp nhận hoặc xác nhận nộp để theo dõi phản hồi.</p>
                    """
            ),
            new AnnouncementItem(
                    "Mời sinh viên tham gia khảo sát chất lượng đào tạo",
                    "Phản hồi trung thực và cụ thể giúp xác định điểm cần cải thiện trong hoạt động đào tạo.",
                    """
                    <h3>Khảo sát chất lượng đào tạo</h3><p>Khi có đợt khảo sát, sinh viên nên đọc rõ phạm vi câu hỏi và phản hồi dựa trên trải nghiệm thực tế. Những ý kiến mô tả tình huống, môn học hoặc dịch vụ cụ thể giúp đơn vị phụ trách xác định vấn đề tốt hơn đánh giá chung chung.</p><p>Hãy gửi phản hồi qua hệ thống khảo sát chính thức và không đưa thông tin riêng tư của người khác vào phần nhận xét. Kết quả tổng hợp được sử dụng theo mục đích đã nêu trong thông báo khảo sát.</p>
                    """
            ),
            new AnnouncementItem(
                    "Tiếp nhận phản ánh khi lịch thi bị trùng",
                    "Cung cấp các học phần, ca thi và bằng chứng lịch để được đối chiếu.",
                    """
                    <h3>Phản ánh trường hợp trùng lịch thi</h3><p>Nếu hai lịch thi hiển thị trùng ngày hoặc giờ, hãy xác nhận cả hai thuộc đúng học kỳ và phiên bản lịch mới nhất. Ghi mã học phần, lớp, ngày giờ và phòng thi của từng lịch.</p><p>Gửi yêu cầu càng sớm càng tốt theo hướng dẫn chính thức. Không tự bỏ một ca thi hoặc tự chuyển phòng khi chưa có xác nhận từ đơn vị có thẩm quyền.</p>
                    """
            ),
            new AnnouncementItem(
                    "Rà soát hồ sơ đề nghị phúc khảo trước khi nộp",
                    "Kiểm tra thông tin bài thi, đơn đề nghị và minh chứng được yêu cầu.",
                    """
                    <h3>Chuẩn bị hồ sơ phúc khảo</h3><p>Đọc biểu mẫu dành cho đợt thi cụ thể và điền chính xác môn thi, mã lớp, ngày thi cùng thông tin người học. Đính kèm giấy tờ hoặc biên nhận theo yêu cầu, nhưng không gửi những tài liệu không liên quan.</p><p>Trước khi xác nhận, kiểm tra thời hạn nộp và lưu bản sao bộ hồ sơ. Nếu thiếu mục nào, hỏi đầu mối khảo thí để tránh bị trả lại do sai hình thức.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông tin tìm hiểu chương trình và tuyển sinh sau đại học",
                    "Tham khảo thông báo tuyển sinh chính thức về ngành, điều kiện và phương thức xét tuyển.",
                    """
                    <h3>Tìm hiểu tuyển sinh sau đại học</h3><p>Ứng viên cần xác định chương trình đào tạo, ngành phù hợp, điều kiện văn bằng và yêu cầu hồ sơ theo thông báo của từng đợt. Các tiêu chí có thể khác nhau theo ngành và hình thức tuyển sinh.</p><p>Hãy sử dụng trang thông tin chính thức để kiểm tra thời gian, lệ phí nếu có, phương thức xét tuyển và địa chỉ tiếp nhận. Nếu bằng cấp hoặc ngành học trước đây chưa rõ tương đương, gửi câu hỏi trước khi hoàn tất hồ sơ.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn chuẩn bị bộ hồ sơ dự tuyển sau đại học",
                    "Đối chiếu văn bằng, bảng điểm, lý lịch và những tài liệu riêng của chương trình.",
                    """
                    <h3>Kiểm tra hồ sơ dự tuyển</h3><p>Trước khi nộp, hãy đối chiếu danh mục tài liệu của đợt tuyển sinh và kiểm tra tính thống nhất giữa văn bằng, bảng điểm, giấy tờ định danh cùng thông tin kê khai. Những tài liệu cần dịch thuật hoặc chứng thực phải theo yêu cầu được công bố.</p><p>Đặt tên tệp có trật tự, kiểm tra đủ trang và lưu xác nhận sau khi gửi. Nếu hồ sơ đang được rà soát, bổ sung đúng mục được yêu cầu thay vì tạo nhiều bộ hồ sơ mới.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông tin học vụ dành cho học viên sau đại học",
                    "Theo dõi kế hoạch học tập, tiến độ nghiên cứu và các nghĩa vụ theo chương trình.",
                    """
                    <h3>Quản lý tiến độ học vụ sau đại học</h3><p>Học viên nên theo dõi kế hoạch môn học, kết quả học tập, thời hạn hoàn thành đề cương và các mốc nghiên cứu của chương trình. Trao đổi với người hướng dẫn hoặc đơn vị quản lý khi cần làm rõ yêu cầu học vụ.</p><p>Nếu dữ liệu tiến độ trên hệ thống chưa cập nhật, nêu chương trình, học kỳ và bước đang chờ xử lý để được kiểm tra. Không mặc định thời hạn được gia hạn khi chưa có xác nhận chính thức.</p>
                    """
            ),
            new AnnouncementItem(
                    "Rà soát hồ sơ dự tuyển trước khi xác nhận gửi",
                    "Kiểm tra đủ thành phần, đúng chương trình và đúng đợt tuyển sinh.",
                    """
                    <h3>Kiểm tra cuối trước khi gửi hồ sơ tuyển sinh</h3><p>Hãy dùng danh mục kiểm tra của đúng chương trình để rà từng tài liệu và trạng thái hoàn thành. Kiểm tra họ tên, ngành đăng ký, thông tin liên hệ và định dạng tệp trước khi xác nhận.</p><p>Nếu hệ thống báo thiếu tài liệu, xem rõ tên mục cần bổ sung và tải đúng tệp vào vị trí tương ứng. Lưu lại mã hồ sơ để có thể tra cứu mà không gửi trùng.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn bổ sung tài liệu theo yêu cầu hồ sơ sau đại học",
                    "Đọc thông báo bổ sung để xác định đúng giấy tờ, định dạng và hạn hoàn thiện.",
                    """
                    <h3>Bổ sung tài liệu hồ sơ</h3><p>Khi có yêu cầu bổ sung, hãy đọc từng mục bị đánh dấu và đối chiếu với hướng dẫn ban đầu. Tài liệu thay thế phải đúng người đứng tên, đủ trang và còn hiệu lực khi nội dung yêu cầu có giới hạn thời gian.</p><p>Gửi bổ sung trong cùng luồng hồ sơ nếu hệ thống hỗ trợ để người xử lý thấy được lịch sử thay đổi. Nếu không thể đáp ứng đúng hạn, liên hệ đơn vị tiếp nhận trước hạn để hỏi cách xử lý được phép.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông báo theo dõi lịch học, lịch thi và lịch hoạt động chính thức",
                    "Dùng lịch đăng trên kênh chính thức làm căn cứ khi có thay đổi về thời gian hoặc địa điểm.",
                    """
                    <h3>Theo dõi lịch hoạt động của trường</h3><p>Lịch học, lịch thi, buổi tư vấn và hoạt động sinh viên có thể được cập nhật theo từng thông báo. Hãy kiểm tra ngày ban hành và nội dung đính chính trước khi lên kế hoạch di chuyển hoặc nộp hồ sơ.</p><p>Nếu hai nguồn thông tin không thống nhất, ưu tiên kênh chính thức được đơn vị phụ trách công bố và gửi yêu cầu xác minh khi cần.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn sử dụng QAUTE để gửi và theo dõi yêu cầu hỗ trợ",
                    "Mô tả rõ vấn đề và chọn đúng nhóm dịch vụ để yêu cầu được chuyển đến đầu mối phù hợp.",
                    """
                    <h3>Sử dụng cổng tư vấn QAUTE</h3><p>Khi tạo yêu cầu, hãy chọn nội dung gần nhất với vấn đề cần xử lý, ghi tiêu đề rõ ràng và nêu các bước bạn đã thực hiện. Thêm mã hồ sơ hoặc thông báo lỗi có liên quan nếu có.</p><p>Sau khi gửi, theo dõi phản hồi trong cùng yêu cầu và bổ sung thông tin khi chuyên viên đề nghị. Không tạo nhiều ticket giống nhau khi yêu cầu cũ vẫn đang được xử lý.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông báo nguyên tắc bảo vệ thông tin cá nhân khi gửi hồ sơ",
                    "Chỉ đính kèm tài liệu cần thiết và che dữ liệu không liên quan khi có thể.",
                    """
                    <h3>Bảo vệ dữ liệu cá nhân</h3><p>Trước khi gửi tài liệu, hãy kiểm tra rằng nội dung phù hợp với mục đích giải quyết yêu cầu. Không đưa mật khẩu, mã OTP, thông tin ngân hàng nhạy cảm hoặc hồ sơ đầy đủ của người khác vào phần mô tả.</p><p>Nếu cần xác minh danh tính, làm theo kênh và phạm vi giấy tờ được yêu cầu chính thức. Khi gửi nhầm thông tin, báo cho đầu mối hỗ trợ để được hướng dẫn xử lý.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông báo tiếp nhận phản ánh về chất lượng dịch vụ sinh viên",
                    "Nêu trải nghiệm cụ thể, thời gian và kết quả mong muốn để đơn vị liên quan xem xét.",
                    """
                    <h3>Góp ý chất lượng dịch vụ</h3><p>Ý kiến phản ánh nên mô tả dịch vụ đã sử dụng, thời điểm và điểm cần cải thiện. Nếu đã có mã yêu cầu trước đó, hãy dẫn mã này để người tiếp nhận đối chiếu lịch sử và tránh xử lý tách rời.</p><p>QAUTE hỗ trợ ghi nhận và theo dõi yêu cầu; quyết định nghiệp vụ thuộc đơn vị chịu trách nhiệm về dịch vụ. Vui lòng không đăng dữ liệu cá nhân hoặc nội dung chưa được xác minh của người khác.</p>
                    """
            ),
            new AnnouncementItem(
                    "Lưu ý khi nhà trường thông báo bảo trì hệ thống trực tuyến",
                    "Hoàn tất thao tác an toàn trước bảo trì và kiểm tra trạng thái sau khi dịch vụ mở lại.",
                    """
                    <h3>Chuẩn bị trước thời gian bảo trì</h3><p>Khi có thông báo bảo trì, hãy đọc phạm vi dịch vụ bị ảnh hưởng, khoảng thời gian dự kiến và hướng dẫn lưu dữ liệu. Tránh bắt đầu giao dịch hoặc nộp biểu mẫu sát thời điểm hệ thống ngừng phục vụ.</p><p>Sau khi có thông báo hoạt động trở lại, đăng nhập từ cổng chính thức và kiểm tra lịch sử trước khi gửi lại thao tác bị gián đoạn. Nếu lỗi còn tiếp diễn, gửi thời điểm và thông báo lỗi để bộ phận kỹ thuật xác minh.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông tin hoạt động câu lạc bộ và phong trào sinh viên",
                    "Theo dõi thông báo của đơn vị tổ chức về lịch, điều kiện tham gia và cách ghi nhận hoạt động.",
                    """
                    <h3>Tham gia hoạt động sinh viên</h3><p>Các câu lạc bộ và đội nhóm có thể tổ chức hoạt động học thuật, văn hóa, thể thao hoặc tình nguyện trong năm học. Sinh viên nên kiểm tra nội dung, tiêu chí tham gia, yêu cầu điểm danh và quy định đăng ký của từng chương trình.</p><p>Nếu cần dùng hoạt động làm minh chứng rèn luyện, hãy hỏi đơn vị tổ chức về cách xác nhận tham gia được chấp nhận. Không mặc định tất cả hoạt động đều được tính cùng một tiêu chí.</p>
                    """
            ),
            new AnnouncementItem(
                    "Hướng dẫn chuẩn bị cho hội thảo hướng nghiệp và kết nối doanh nghiệp",
                    "Đọc chương trình, chuẩn bị câu hỏi và kiểm tra thông tin đăng ký trước khi tham dự.",
                    """
                    <h3>Chuẩn bị cho hoạt động hướng nghiệp</h3><p>Trước buổi giao lưu với doanh nghiệp, hãy tìm hiểu lĩnh vực hoạt động, xem lại CV và chuẩn bị câu hỏi về vị trí, kỹ năng hoặc lộ trình nghề nghiệp. Nếu có phiên tư vấn cá nhân, kiểm tra tài liệu được yêu cầu mang theo.</p><p>Thời gian và địa điểm chỉ được xác nhận qua thông báo riêng của từng hoạt động. Khi đăng ký xong, lưu xác nhận và cập nhật cho ban tổ chức nếu không thể tham dự.</p>
                    """
            ),
            new AnnouncementItem(
                    "Nhắc kiểm tra thông báo và hạn xử lý hồ sơ trên cổng sinh viên",
                    "Theo dõi trạng thái, yêu cầu bổ sung và mốc thời gian thay vì chỉ dựa vào email.",
                    """
                    <h3>Theo dõi hồ sơ đang xử lý</h3><p>Với mỗi yêu cầu đã gửi, hãy lưu mã hồ sơ, ngày tiếp nhận và danh sách tài liệu đính kèm. Kiểm tra mục thông báo để biết hồ sơ đã chuyển bước, cần bổ sung hay đã có kết quả.</p><p>Nếu thời gian chờ dài hơn mốc được công bố, gửi câu hỏi kèm mã hồ sơ để đơn vị xử lý kiểm tra. Tránh tạo hồ sơ thứ hai khi chưa rõ trạng thái của hồ sơ trước.</p>
                    """
            ),
            new AnnouncementItem(
                    "Thông báo rà soát thông tin liên hệ tài khoản sinh viên",
                    "Cập nhật email và phương thức liên hệ đang sử dụng để không bỏ lỡ phản hồi.",
                    """
                    <h3>Giữ thông tin liên hệ luôn chính xác</h3><p>Sinh viên nên kiểm tra email và thông tin liên hệ trong hồ sơ cá nhân để bảo đảm có thể nhận phản hồi về học vụ, tài chính, lịch hẹn hoặc yêu cầu tư vấn. Nếu dữ liệu đã thay đổi, thực hiện cập nhật qua chức năng chính thức.</p><p>Sau khi chỉnh sửa, kiểm tra thông tin mới đã được lưu và có thể nhận thông báo xác minh. Nếu không thể cập nhật, gửi yêu cầu hỗ trợ mà không chia sẻ mật khẩu.</p>
                    """
            )
        );
        LocalDateTime now = LocalDateTime.now().withNano(0);

        for (int i = 0; i < items.size(); i++) {
            AnnouncementItem item = items.get(i);
            Announcement announcement = new Announcement();
            announcement.setTitle(item.title());
            announcement.setSummary(item.summary());
            announcement.setBodyHtml(item.bodyHtml());
            announcement.setAudience(AnnouncementAudience.ALL);
            announcement.setPinned(i < 2);
            announcement.setStatus(AnnouncementStatus.PUBLISHED);
            announcement.setPublishAt(now.minusDays(i));
            announcement.setNotifyOnPublish(false);
            announcement.setNotified(true);
            announcement.setAutoPublish(false);
            announcement.setCreatedBy(adminId);
            repo.save(announcement);
        }
    }

    private record AnnouncementItem(String title, String summary, String bodyHtml) {
    }
}
