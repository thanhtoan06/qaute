package vn.edu.hcmute.qaute.config.seed;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.ChatContentType;
import vn.edu.hcmute.qaute.common.constant.ChatSenderType;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.entity.chat.ChatMessage;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.repository.chat.ChatMessageRepository;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

/**
 * Dữ liệu demo hội thoại chat cho hệ thống QAUTE mở rộng.
 * Chạy sau UserSeeder và OrgSeeder; mỗi kịch bản có nội dung riêng,
 * sử dụng 12 tài khoản quản lý mới và bao phủ đủ 71 chuyên mục.
 */
@Component
@Profile("demo")
@RequiredArgsConstructor
public class ChatSeeder implements DemoSeeder {

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

    private static final String[] CATEGORY_CODES = {
        "DT_DKHP",
        "CT_HOCBONG",
        "TC_HOCPHI",
        "KTX_DANGKY",
        "DT_HOCVU",
        "DT_TOTNGHIEP",
        "CT_RENLUYEN",
        "TC_MIENGIAM",
        "KTX_DICHVU",
        "DT_DKHP_SAI",
        "DT_DKHP_HUY",
        "DT_HOCVU_DIEM",
        "DT_HOCVU_BAOLUU",
        "DT_TOTNGHIEP_HOSO",
        "CT_XACNHAN",
        "CT_HOTRO",
        "CT_HOCBONG_HOSO",
        "CT_HOCBONG_KETQUA",
        "CT_RENLUYEN_DIEM",
        "CT_RENLUYEN_MINHCHUNG",
        "CT_XACNHAN_GIAYTO",
        "TC_CHUNGTU",
        "TC_THANHTOAN",
        "TC_HOCPHI_DOISOAT",
        "TC_HOCPHI_GIAHAN",
        "TC_MIENGIAM_HOSO",
        "TC_THANHTOAN_LOI",
        "KTX_DANGKY_HOSO",
        "KTX_DANGKY_GIAHAN",
        "KTX_DICHVU_SUA",
        "KTX_DICHVU_DIENNUOC",
        "CNTT_TAIKHOAN",
        "CNTT_PORTAL",
        "CNTT_BAOMAT",
        "CNTT_TAIKHOAN_KHOIPHUC",
        "CNTT_PORTAL_LOI",
        "TV_MUONTRA",
        "TV_TRACUU",
        "TV_TAIKHOAN",
        "TV_MUONTRA_GIAHAN",
        "TV_TRACUU_TAILIEU",
        "QHDN_THUCTAP",
        "QHDN_TUYENDUNG",
        "QHDN_CV",
        "QHDN_THUCTAP_HOSO",
        "QHDN_TUYENDUNG_HOSO",
        "KHCN_DETAI",
        "KHCN_HOITHAO",
        "KHCN_NGHIENCUU",
        "KHCN_DETAI_HOSO",
        "KHCN_HOITHAO_DANGKY",
        "YTE_BHYT",
        "YTE_SUCKHOE",
        "YTE_HOTRO",
        "YTE_BHYT_HOSO",
        "YTE_SUCKHOE_TUVAN",
        "CSVC_SUACHUA",
        "CSVC_PHONGHOC",
        "CSVC_THIETBI",
        "CSVC_SUACHUA_BAOHONG",
        "CSVC_PHONGHOC_DANGKY",
        "QLCL_LICHTHI",
        "QLCL_PHUCKHAO",
        "QLCL_KHAOSAT",
        "QLCL_LICHTHI_TRUNGLICH",
        "QLCL_PHUCKHAO_HOSO",
        "SDH_TUYENSINH",
        "SDH_HOSO",
        "SDH_HOCVU",
        "SDH_TUYENSINH_HOSO",
        "SDH_HOSO_BOSUNG",
    };

    private static final int[] STUDENT_NUMBERS = {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
        11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
        21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
        11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
        21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
        11,
    };

    private static final String[] END_REASONS = {
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "STUDENT_END",
        "TIMEOUT",
        "MANAGER_END",
        "STUDENT_END",
        "STUDENT_END",
        "MANAGER_END",
        "NO_AGENT",
        "STUDENT_END",
        "TIMEOUT",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "NO_AGENT",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "TIMEOUT",
        "STUDENT_END",
        "MANAGER_END",
        "NO_AGENT",
        "MANAGER_END",
        "STUDENT_END",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "NO_AGENT",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "TIMEOUT",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "NO_AGENT",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "TIMEOUT",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "NO_AGENT",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "TIMEOUT",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "STUDENT_END",
        "MANAGER_END",
        "NO_AGENT",
    };

    private static final boolean[] ENDED_BY_STUDENT = {
        true, false, true, false, true, false, true, false, true, true,
        false, false, true, true, false, false, true, false, false, true,
        false, true, false, true, false, true, false, true, false, true,
        false, true, false, false, false, true, true, false, true, false,
        false, true, false, true, false, false, true, false, true, false,
        false, true, false, true, false, false, true, false, true, false,
        false, true, false, true, false, false, true, false, true, false,
        false,
    };

    private static final String[] RATING_COMMENTS = {
        "Tư vấn viên hỗ trợ cụ thể và dễ hiểu.",
        "Thông tin hồ sơ rõ ràng, tôi biết bước tiếp theo cần làm.",
        null,
        "Được hướng dẫn đủ bước, nhưng cần rút ngắn thời gian phản hồi.",
        "Hướng dẫn đối chiếu chương trình học giúp em tự kiểm tra được.",
        "Câu trả lời giúp tôi phân biệt điều kiện và thời điểm xét.",
        "Giải thích từng bước đánh giá dễ hiểu và sát tình huống của em.",
        "Hướng dẫn tôi biết cần hỏi đúng đơn vị và kiểm tra điều kiện.",
        "Đã nắm được cách gửi yêu cầu bảo trì và cung cấp mã phòng.",
        "Đã rõ các thông tin cần gửi để đối chiếu nhóm lớp.",
        null,
        "Đã biết cách cung cấp dữ liệu đối chiếu điểm.",
        "Phản hồi giúp em biết không nên tự ngừng học khi hồ sơ chưa duyệt.",
        "Danh sách rà soát tốt nghiệp cụ thể và dễ áp dụng.",
        "Đã hiểu cách nêu mục đích và số bản xác nhận cần cấp.",
        null,
        "Hướng dẫn hữu ích, em biết cách kiểm tra bản hồ sơ trước khi nộp.",
        null,
        "Đã chỉ rõ cần đối chiếu tiêu chí thay vì chỉ hỏi tổng điểm.",
        "Cách sắp xếp minh chứng rõ, tránh thiếu trang.",
        "Được hướng dẫn đầy đủ, nhưng việc cấp giấy cần theo lịch của trường.",
        "Đã phân biệt được biên nhận ngân hàng và chứng từ hạch toán.",
        "Tư vấn giúp tôi tránh chuyển nhầm khoản thu.",
        "Quy trình tra soát rõ và nhấn mạnh không chuyển lại ngay.",
        null,
        "Đã biết cách bổ sung giấy tờ đúng hồ sơ hiện tại.",
        "Chỉ dẫn xử lý an toàn và không tạo giao dịch trùng.",
        "Danh sách hồ sơ dễ làm theo và có nhắc kiểm tra tính nhất quán.",
        "Đã giải thích rõ sự khác nhau giữa gia hạn và đăng ký mới.",
        "Đã nắm cách mô tả hư hỏng để tổ kỹ thuật xử lý đúng.",
        null,
        "Hướng dẫn từng bước đăng nhập lại và không yêu cầu thông tin nhạy cảm.",
        "Đã xác định được cách kiểm tra trạng thái trước khi gửi lại.",
        null,
        "Các bước khôi phục được giải thích rõ và an toàn.",
        "Thông tin tái hiện lỗi đầy đủ, dễ chuyển bộ phận kỹ thuật.",
        "Đã rõ cách tra cứu hạn trả từng tài liệu.",
        "Gợi ý từ khóa giúp tôi tìm được tài liệu gần đúng.",
        "Đã hiểu cần đồng bộ mã sinh viên với tài khoản thư viện.",
        "Đã giải thích điều kiện gia hạn và trường hợp ngoại lệ.",
        null,
        "Các mốc chuẩn bị thực tập được sắp xếp dễ theo dõi.",
        "Đã biết kiểm tra thông tin tuyển dụng và hạn ứng tuyển.",
        "Gợi ý tập trung vào kết quả dự án thay vì liệt kê chung chung.",
        null,
        "Đã giải thích cách chuẩn hóa hồ sơ theo yêu cầu từng vị trí.",
        "Đã biết cần xác nhận đề tài được đăng ký chính thức.",
        "Thông tin rõ về việc phân biệt hội thảo và hoạt động ngoại khóa.",
        "Các lưu ý về trích dẫn và dữ liệu nghiên cứu hữu ích.",
        "Đã xác định được tài liệu còn thiếu trong hồ sơ đề tài.",
        null,
        "Được hướng dẫn kiểm tra thời hạn bảo hiểm qua nguồn chính thức.",
        "Thông tin chung hữu ích, không thay thế tư vấn y tế trực tiếp.",
        "Được hướng dẫn về bảo mật và đầu mối hỗ trợ phù hợp.",
        null,
        "Phản hồi lịch sự và phân biệt rõ tư vấn hành chính với khám chữa bệnh.",
        "Đã biết cung cấp mã tài sản và vị trí cụ thể để sửa chữa.",
        "Được hướng dẫn cách ghi đủ thông tin thay vì chỉ nêu tên lớp.",
        "Cách mô tả thiết bị cụ thể sẽ giúp xử lý nhanh hơn.",
        "Thông tin đủ để phân biệt sự cố mới và yêu cầu cũ.",
        null,
        "Đã biết phải dùng lịch thi chính thức theo mã học phần.",
        "Quy trình phúc khảo được giải thích theo từng mốc.",
        "Đã hiểu cách kiểm tra kỳ khảo sát và phạm vi bảo mật.",
        null,
        "Đã biết cách theo dõi hồ sơ và giữ bằng chứng tiếp nhận.",
        "Đã biết cách xác định thông tin tuyển sinh đúng đợt.",
        "Danh sách giấy tờ giúp em rà soát hồ sơ trước hạn.",
        "Thông tin thực tế về kế hoạch học tập sau đại học rất hữu ích.",
        "Đã nắm rõ cách dùng một hồ sơ đang tồn tại để bổ sung dữ liệu.",
        null,
    };

    private static final String[] SYSTEM_MESSAGES = {
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "Phiên chat chưa được tiếp nhận trong thời gian chờ. Em có thể tạo yêu cầu tư vấn, kèm mã học phần và hạn điều chỉnh để được theo dõi.",
        null,
        null,
        null,
        null,
        "Hiện chưa có tư vấn viên tiếp nhận phiên chat. Em có thể mở yêu cầu tư vấn, mô tả khó khăn và ghi thời hạn cần hỗ trợ.",
        null,
        "Phiên chờ đã hết hạn trước khi có chuyên viên tham gia. Em kiểm tra đúng tên chương trình, học kỳ và kênh công bố trong thông báo xét học bổng.",
        null,
        null,
        null,
        null,
        null,
        null,
        "Chưa có chuyên viên tham gia phiên chat. Em xem hạn tiếp nhận đơn gia hạn trong thông báo học phí và tạo yêu cầu tài chính nếu cần hướng dẫn hồ sơ.",
        null,
        null,
        null,
        null,
        null,
        "Phiên chat hết thời gian chờ. Em ghi tòa nhà, tầng và khoảng thời gian mất nước trong yêu cầu dịch vụ để ban quản lý kiểm tra hệ thống.",
        null,
        null,
        "Chưa có tư vấn viên tiếp nhận. Đừng mở đường dẫn hoặc cung cấp mật khẩu; em hãy chuyển tiếp email đáng ngờ đến kênh báo cáo bảo mật chính thức.",
        null,
        null,
        null,
        null,
        null,
        null,
        "Phiên tư vấn đã hết thời gian chờ. Em lưu tên bài, tác giả, năm và mã DOI nếu có để thư viện tra cứu nguồn truy cập phù hợp.",
        null,
        null,
        null,
        "Phiên chat hết hạn trước khi có người phụ trách tham gia. Em chuẩn bị thông tin doanh nghiệp, thời gian thực tập và biểu mẫu do khoa hoặc trường yêu cầu để tạo yêu cầu mới.",
        null,
        null,
        null,
        null,
        null,
        "Chưa có chuyên viên nhận phiên chat. Em kiểm tra thư rác, hộp thư đã đăng ký và lưu mã đăng ký để ban tổ chức đối chiếu.",
        null,
        null,
        null,
        "Phiên chat đã hết thời gian chờ. Em kiểm tra danh sách giấy tờ còn thiếu trong thông báo và tạo yêu cầu mới với mã hồ sơ bảo hiểm hiện tại.",
        null,
        null,
        null,
        null,
        null,
        "Chưa có nhân viên nhận phiên chat. Em chuẩn bị thời gian, số người, mục đích và thiết bị cần dùng để gửi yêu cầu đặt phòng theo quy trình.",
        null,
        null,
        null,
        "Phiên chat kết thúc do hết thời gian chờ. Em cung cấp hai mã học phần, ngày thi và ảnh lịch cùng thời điểm tra cứu để khảo thí kiểm tra.",
        null,
        null,
        null,
        null,
        null,
        "Chưa có tư vấn viên tiếp nhận. Em đọc ghi chú của hồ sơ, lưu mã đăng ký và liệt kê từng tài liệu đang bị yêu cầu bổ sung trước khi gửi yêu cầu mới.",
    };

    private static final ChatSessionStatus[] SESSION_STATUSES = {
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.ENDED,
        ChatSessionStatus.EXPIRED,
    };

    private static final int[] RATINGS = {
        5, 4, 0, 3, 5, 4, 5, 4, 5, 4,
        0, 5, 4, 5, 4, 0, 5, 0, 4, 5,
        3, 5, 4, 5, 0, 4, 5, 4, 5, 4,
        0, 5, 4, 0, 5, 4, 5, 4, 5, 4,
        0, 5, 4, 5, 0, 4, 5, 4, 5, 4,
        0, 5, 4, 5, 0, 4, 5, 4, 5, 4,
        0, 5, 4, 5, 0, 4, 5, 4, 5, 4,
        0,
    };

    private static final String[][] DIALOGUES = {
        {
            "Em cần hướng dẫn đăng ký học phần cho học kỳ mới.",
            "Chào em, em kiểm tra kế hoạch học tập và các học phần tiên quyết trước khi đăng ký nhé.",
            "Em đã hoàn thành học phần tiên quyết nhưng hệ thống vẫn báo chưa đủ điều kiện.",
            "Em kiểm tra lại điểm tổng kết đã được cập nhật chưa và gửi yêu cầu kèm ảnh thông báo nếu còn lỗi.",
            "Điểm đã được cập nhật hôm qua, em cần ghi thông tin gì trong yêu cầu?",
            "Em ghi mã học phần, nhóm lớp và thời điểm gặp lỗi để phòng Đào tạo kiểm tra.",
            "Trong thời gian chờ kiểm tra em có thể đăng ký các môn khác không?",
            "Em vẫn có thể đăng ký các môn đủ điều kiện và còn chỗ trong đợt đăng ký hiện tại.",
            "Em đã hiểu và sẽ bổ sung đầy đủ thông tin. Em cảm ơn thầy cô.",
            "Phòng Đào tạo sẽ phản hồi trên yêu cầu của em. Chúc em đăng ký học phần thuận lợi."
        },
        {
            "Em muốn hỏi hồ sơ xét học bổng khuyến khích học tập.",
            "Chào em, hồ sơ và điều kiện xét học bổng được công bố trong thông báo của học kỳ.",
            "Em có cần nộp lại bảng điểm và minh chứng hoạt động không ạ?",
            "Em đối chiếu danh mục minh chứng trong thông báo và nộp những giấy tờ còn thiếu trên cổng sinh viên.",
            "Em chưa thấy kết quả điểm rèn luyện của học kỳ vừa rồi.",
            "Em liên hệ cố vấn học tập để xác nhận thời gian chốt điểm rèn luyện trước hạn xét học bổng.",
            "Nếu thông tin tài khoản ngân hàng của em thay đổi thì cần làm gì?",
            "Em cập nhật tài khoản chính chủ và kiểm tra lại họ tên, số tài khoản trước khi xác nhận hồ sơ.",
            "Em sẽ kiểm tra lại hồ sơ và thời hạn theo thông báo.",
            "Nếu còn vướng mắc, em gửi yêu cầu cho phòng Công tác sinh viên để được kiểm tra cụ thể."
        },
        {
            "Em cần kiểm tra học phí đã thanh toán qua ngân hàng.",
            "Chào em, em kiểm tra trạng thái giao dịch và nội dung chuyển khoản trên chứng từ ngân hàng nhé.",
            "Ngân hàng đã báo thành công nhưng cổng sinh viên chưa cập nhật.",
            "Hệ thống cần thời gian đối soát. Em lưu chứng từ và gửi yêu cầu nếu đã quá thời gian ghi trong hướng dẫn.",
            "Chứng từ của em có mã giao dịch và thời gian thanh toán đầy đủ.",
            "Em đính kèm chứng từ trong yêu cầu và che các thông tin tài khoản không liên quan.",
            "Em có cần thanh toán lại để kịp hạn không ạ?",
            "Em chưa nên thanh toán trùng; phòng Tài chính sẽ kiểm tra giao dịch đã có trước.",
            "Em sẽ gửi yêu cầu đối soát theo hướng dẫn. Em cảm ơn.",
            "Em theo dõi phản hồi trên cổng tư vấn và giữ chứng từ cho đến khi hoàn tất đối soát."
        },
        {
            "Em muốn tìm hiểu thủ tục đăng ký chỗ ở ký túc xá.",
            "Chào em, em xem đợt tiếp nhận hồ sơ và danh mục giấy tờ trong thông báo ký túc xá nhé.",
            "Em là sinh viên năm nhất và muốn đăng ký phòng cùng bạn.",
            "Em có thể ghi nguyện vọng ở cùng trong hồ sơ; việc sắp xếp còn phụ thuộc số chỗ trống.",
            "Em cần chuẩn bị những giấy tờ nào khi đến nhận phòng?",
            "Em chuẩn bị giấy tờ tùy thân, thông báo được tiếp nhận và các giấy tờ theo hướng dẫn nhận phòng.",
            "Nếu đến muộn so với lịch tiếp nhận thì em cần báo ở đâu?",
            "Em liên hệ bộ phận quản lý ký túc xá để được xác nhận thời gian nhận phòng phù hợp.",
            "Em đã nắm được thủ tục và sẽ theo dõi thông báo mới.",
            "Chúc em hoàn tất hồ sơ thuận lợi. Em có thể gửi yêu cầu nếu cần hỗ trợ thêm."
        },
        {
            "Em muốn đối chiếu số tín chỉ tích lũy với chương trình đào tạo.",
            "Em mở chương trình áp dụng cho khóa mình và so sánh từng nhóm học phần đã hoàn thành.",
            "Portal ghi đủ số tín chỉ nhưng em chưa rõ môn tự chọn có bị tính trùng không.",
            "Em lập bảng gồm mã môn, số tín chỉ và nhóm kiến thức; không cộng một học phần vào hai nhóm nếu quy định không cho phép.",
            "Nếu có môn tương đương được duyệt thì em tra ở đâu ạ?",
            "Em kiểm tra quyết định công nhận tương đương hoặc gửi mã hai học phần để Phòng Đào tạo đối chiếu."
        },
        {
            "Em muốn biết cách theo dõi đợt xét tốt nghiệp gần nhất.",
            "Em xem thông báo chính thức để biết mốc rà soát điều kiện, hạn bổ sung và ngày công bố dự kiến.",
            "Nếu em còn thiếu một chứng chỉ thì có nộp hồ sơ trước được không?",
            "Em kiểm tra điều kiện của đợt hiện tại; nếu hệ thống cho phép lưu hồ sơ nháp, hãy ghi rõ mục còn thiếu và hạn hoàn tất.",
            "Kết quả xét có được gửi qua email cá nhân không ạ?",
            "Em theo dõi cổng sinh viên và email trường; thông báo chính thức sẽ nêu kênh công bố được sử dụng."
        },
        {
            "Em muốn biết quy trình tự đánh giá điểm rèn luyện cuối kỳ.",
            "Em đọc bộ tiêu chí của học kỳ, chọn mức phù hợp với hoạt động thực tế và đính kèm minh chứng theo từng mục.",
            "Em tham gia nhiều hoạt động nhưng một số không có giấy chứng nhận.",
            "Em hỏi ban tổ chức về xác nhận tham dự hoặc loại minh chứng thay thế được chấp nhận trong tiêu chí đó.",
            "Sau khi gửi, em có thể chỉnh lại phần tự đánh giá không?",
            "Em kiểm tra trạng thái hồ sơ; nếu đã chuyển bước xét duyệt, hãy gửi đề nghị cập nhật theo kênh được công bố."
        },
        {
            "Em muốn tìm hiểu diện miễn giảm học phí áp dụng cho mình.",
            "Em xem thông báo chính sách hiện hành và đối chiếu điều kiện của từng nhóm đối tượng.",
            "Nếu gia đình em có hoàn cảnh thuộc hai diện thì chọn diện nào?",
            "Em nên hỏi bộ phận tài chính hoặc công tác sinh viên để xác định cách tiếp nhận hồ sơ; không tự nộp trùng khi chưa rõ.",
            "Kết quả miễn giảm được thể hiện trên công nợ ra sao ạ?",
            "Khi hồ sơ được duyệt, em kiểm tra khoản điều chỉnh trên portal và liên hệ tài chính nếu số tiền không khớp thông báo."
        },
        {
            "Vòi nước trong khu vệ sinh tầng em ở bị rò rỉ, em báo thế nào?",
            "Em chọn yêu cầu sửa chữa KTX, ghi tòa nhà, tầng, số phòng và mô tả vị trí hỏng.",
            "Em có nên tự tháo đầu vòi để kiểm tra trước không?",
            "Em không cần tự tháo thiết bị; gửi mô tả và ảnh khu vực an toàn để nhân viên kỹ thuật xác định dụng cụ cần mang.",
            "Em có được biết thời gian dự kiến xử lý không ạ?",
            "Bộ phận quản lý sẽ cập nhật lịch kiểm tra theo mức độ sự cố và tình trạng nhân sự kỹ thuật."
        },
        {
            "Em chọn nhầm nhóm thực hành dù mã môn học đúng.",
            "Em kiểm tra thời gian điều chỉnh đăng ký còn mở hay không, sau đó ghi cả mã môn và mã nhóm đang chọn.",
            "Nhóm em muốn chuyển sang còn chỗ trên portal nhưng nút đổi nhóm bị khóa.",
            "Em chụp thông báo khóa thao tác và gửi yêu cầu cho Phòng Đào tạo để kiểm tra trạng thái đợt đăng ký.",
            "Em có cần hủy nhóm cũ trước không?",
            "Chưa nên hủy khi chưa xác nhận cách chuyển nhóm, tránh mất chỗ hiện tại mà chưa đăng ký được nhóm thay thế."
        },
        {
            "Em cần hỗ trợ điều chỉnh lịch đăng ký học phần."
        },
        {
            "Điểm giữa kỳ của em đang để trống trong khi giảng viên đã thông báo kết quả.",
            "Em kiểm tra đúng lớp học phần và thành phần điểm; đôi khi bảng điểm cập nhật theo đợt.",
            "Em đã tải lại trang và chọn đúng học kỳ nhưng ô điểm vẫn trống.",
            "Em ghi mã lớp, tên thành phần điểm và ngày công bố; đính kèm thông báo liên quan để đơn vị học vụ tra cứu.",
            "Em gửi cả ảnh bảng điểm và thông báo lớp có được không?",
            "Được, em nên che thông tin của bạn khác và chỉ giữ phần cần thiết để xác minh kết quả của em."
        },
        {
            "Em cần tạm dừng học một học kỳ vì lý do gia đình.",
            "Em kiểm tra quy định bảo lưu dành cho khóa học và lấy đúng biểu mẫu đang được áp dụng.",
            "Trong lúc chờ duyệt đơn, em có cần tiếp tục theo dõi lịch học không?",
            "Có. Em vẫn nên theo dõi trạng thái học vụ và hỏi đơn vị đào tạo về nghĩa vụ trong thời gian chờ quyết định.",
            "Nếu đơn được chấp thuận, em xác nhận ngày bắt đầu hiệu lực ở đâu?",
            "Em lưu quyết định hoặc thông báo xử lý hồ sơ; khi quay lại học, dùng văn bản đó để đối chiếu trạng thái sinh viên."
        },
        {
            "Em phát hiện họ tên bị thiếu dấu trong danh sách dự kiến tốt nghiệp.",
            "Em đối chiếu danh sách với hồ sơ định danh và ghi chính xác trường thông tin đang sai.",
            "Em nên gửi ảnh căn cước qua chat này không ạ?",
            "Em chỉ tải giấy tờ qua kênh chính thức được chỉ định, tránh gửi dữ liệu định danh đầy đủ trong cuộc trò chuyện không dành cho hồ sơ.",
            "Em cần làm trước khi danh sách chốt vào tuần tới.",
            "Em gửi yêu cầu sớm, ghi rõ hạn chốt danh sách để đơn vị phụ trách ưu tiên kiểm tra theo quy trình."
        },
        {
            "Em cần giấy xác nhận sinh viên để hoàn thiện hồ sơ thuê nhà.",
            "Em ghi mục đích sử dụng, số bản và thông tin bên tiếp nhận nếu họ có yêu cầu cụ thể.",
            "Bên cho thuê yêu cầu có ngày dự kiến tốt nghiệp, nhưng em không biết trường có xác nhận mục này không.",
            "Em nêu chính xác nội dung bên ngoài yêu cầu để đơn vị phụ trách xem xét dữ liệu thuộc thẩm quyền xác nhận.",
            "Em có nên tự ghi thêm nội dung đó vào mẫu không?",
            "Không tự chỉnh mẫu do trường phát hành; hãy hỏi nơi tiếp nhận hồ sơ về văn bản bổ sung được chấp nhận."
        },
        {
            "Em muốn được tư vấn về hồ sơ hỗ trợ học tập."
        },
        {
            "Em đang chuẩn bị bộ hồ sơ học bổng tài trợ từ doanh nghiệp.",
            "Em mở thông báo chương trình đó và lập danh sách riêng các giấy tờ bắt buộc, biểu mẫu và thời hạn.",
            "Bảng điểm tải từ portal không có dấu xác nhận, vậy có dùng được không?",
            "Em kiểm tra yêu cầu của đơn vị tài trợ; nếu cần bản xác nhận riêng, hãy xin cấp theo thủ tục của trường.",
            "Em có thể nộp trước các giấy tờ còn lại không ạ?",
            "Nếu cổng cho lưu hồ sơ nháp thì em có thể chuẩn bị trước, nhưng cần hoàn tất mọi mục bắt buộc trước khi gửi chính thức."
        },
        {
            "Em đã nộp học bổng nhưng chưa biết kết quả được công bố ở đâu."
        },
        {
            "Điểm rèn luyện của em thấp hơn tổng điểm các tiêu chí em tự đánh giá.",
            "Em xem chi tiết từng tiêu chí sau bước họp lớp hoặc xét duyệt, không chỉ so sánh điểm tổng.",
            "Một hoạt động tình nguyện của em không được ghi nhận.",
            "Em cung cấp tên chương trình, ngày tham gia và minh chứng xác nhận để cán bộ phụ trách kiểm tra tiêu chí tương ứng.",
            "Nếu thời hạn phản ánh đã gần hết thì sao ạ?",
            "Em nộp phản hồi theo đúng kênh và ghi rõ thời hạn để đơn vị có thông tin kiểm tra kịp thời."
        },
        {
            "Em có ba giấy chứng nhận cho một tiêu chí điểm rèn luyện.",
            "Em kiểm tra cổng cho phép tải nhiều tệp hay chỉ một tệp ở tiêu chí đó.",
            "Nếu chỉ nhận một file thì em nên gộp theo thứ tự nào?",
            "Em sắp xếp theo ngày diễn ra, đặt trang bìa ghi tên các hoạt động rồi kiểm tra mọi chữ ký và thời gian đều đọc được.",
            "Em có nên tải ảnh chụp màn hình nhóm chat làm minh chứng không?",
            "Chỉ dùng ảnh đó khi hướng dẫn cho phép và ảnh chứng minh được việc tham gia; nếu không, xin xác nhận từ ban tổ chức."
        },
        {
            "Em cần bản sao bảng điểm có xác nhận để nộp hồ sơ tuyển dụng.",
            "Em xác định bên tuyển dụng cần bảng điểm toàn khóa hay bảng điểm theo học kỳ và cần bao nhiêu bản.",
            "Họ yêu cầu bản tiếng Anh, em có thể tự dịch bảng điểm tiếng Việt không?",
            "Em hỏi đơn vị cấp về bản tiếng Anh hoặc quy trình dịch thuật được chấp nhận; bản tự dịch không thay thế văn bản do trường cấp.",
            "Em nên ghi hạn nộp của công ty vào yêu cầu không?",
            "Có, ghi ngày cần nhận giúp đơn vị tiếp nhận thông tin, nhưng thời gian cấp vẫn theo quy trình chính thức."
        },
        {
            "Em cần lấy lại biên nhận học phí đã thanh toán từ đầu học kỳ.",
            "Em tra lịch sử thanh toán theo kỳ, sau đó đối chiếu ngày, số tiền và mã giao dịch.",
            "Lịch sử ngân hàng còn nhưng cổng trường không có file biên nhận.",
            "Em gửi mã sinh viên và mã giao dịch đến kênh tài chính chính thức để được kiểm tra bản ghi hạch toán.",
            "Em gửi ảnh giao dịch đã che số dư tài khoản được không?",
            "Được, chỉ giữ thông tin giao dịch cần tra soát và che số tài khoản hoặc giao dịch khác không liên quan."
        },
        {
            "Portal có hai khoản phải nộp với hạn khác nhau, em nên chọn khoản nào trước?",
            "Em xem hạn và hướng dẫn của từng dòng công nợ; ưu tiên theo thông báo chính thức thay vì chỉ nhìn tổng số tiền.",
            "Một dòng không có mã tham chiếu riêng.",
            "Em không tự điền mã của khoản khác. Hãy hỏi tài chính cách nhận diện khoản thu đó trước khi xác nhận thanh toán.",
            "Nếu đã thanh toán một phần thì portal có tự trừ không?",
            "Em kiểm tra chi tiết số dư sau giao dịch; nếu chưa phân bổ đúng, lưu chứng từ và gửi yêu cầu đối soát."
        },
        {
            "Ứng dụng ngân hàng ghi giao dịch học phí thành công nhưng portal vẫn đang chờ.",
            "Em giữ mã giao dịch, thời điểm, số tiền và nội dung chuyển khoản để đối chiếu hai hệ thống.",
            "Đã qua một ngày làm việc, em có nên thao tác thanh toán lần nữa không?",
            "Em nên kiểm tra thời gian xử lý được công bố và yêu cầu tra soát trước; không tạo giao dịch trùng khi kết quả lần đầu chưa rõ.",
            "Em cần gửi những dữ liệu gì trong yêu cầu?",
            "Gửi mã sinh viên, mã giao dịch, số tiền và ảnh trạng thái liên quan; tuyệt đối không gửi mật khẩu hoặc mã OTP."
        },
        {
            "Em muốn hỏi gia hạn học phí vì chưa thể hoàn tất khoản thu đúng hạn."
        },
        {
            "Hồ sơ miễn giảm học phí của em bị yêu cầu bổ sung giấy xác nhận.",
            "Em đọc chính xác tên giấy tờ và thời hạn cần bổ sung trong phản hồi của hồ sơ.",
            "Giấy xác nhận em đang có được cấp từ năm ngoái.",
            "Em kiểm tra yêu cầu về thời hạn hiệu lực; nếu không rõ, hỏi bộ phận tiếp nhận trước khi xin cấp mới.",
            "Em nên gửi lại cả bộ giấy tờ cũ không?",
            "Em bổ sung tài liệu còn thiếu trong hồ sơ hiện có, chỉ gửi lại những phần được yêu cầu hoặc đã thay đổi."
        },
        {
            "Màn hình thanh toán bị treo ngay sau bước xác thực ngân hàng.",
            "Em chưa thao tác lại vội; trước hết kiểm tra lịch sử ngân hàng và lịch sử đơn thanh toán.",
            "Ngân hàng báo đang xử lý nhưng portal quay về trang đầu.",
            "Em lưu mã đơn hoặc mã giao dịch, đợi trạng thái cuối cùng theo hướng dẫn cổng rồi gửi tra soát nếu hai hệ thống khác nhau.",
            "Có cần gửi mã OTP đã nhập để kiểm tra không?",
            "Không. Mã OTP không cần thiết cho tra soát và không được chia sẻ; chỉ gửi mã giao dịch và thông tin đối chiếu."
        },
        {
            "Em đã điền đăng ký nội trú nhưng trạng thái hồ sơ vẫn là bản nháp.",
            "Em mở phần tóm tắt, kiểm tra các mục bắt buộc và xác nhận bước gửi cuối cùng nếu còn hiển thị.",
            "Có một tệp giấy tờ đã tải nhưng xem trước bị trắng.",
            "Em thử mở file trên thiết bị khác để kiểm tra tệp gốc, sau đó tải lại bản rõ nét nếu cổng chưa ghi nhận hợp lệ.",
            "Em có cần gửi lại từ đầu không?",
            "Không nên tạo hồ sơ trùng; trước hết kiểm tra mã đăng ký và trạng thái của hồ sơ hiện có."
        },
        {
            "Hợp đồng nội trú của em sắp hết hạn nhưng em muốn ở tiếp kỳ sau.",
            "Em xem mốc đăng ký gia hạn và điều kiện của đợt tiếp nhận mới trong thông báo KTX.",
            "Nếu em đã chuyển phòng trong kỳ thì ghi phòng nào trên đơn?",
            "Em khai theo thông tin chỗ ở hiện tại trên hệ thống và nêu rõ lịch sử chuyển phòng nếu biểu mẫu yêu cầu.",
            "Em cần thanh toán phí kỳ mới trước khi nộp đơn không?",
            "Em đối chiếu hướng dẫn của đợt gia hạn; nếu trình tự chưa rõ, hỏi ban quản lý trước khi thanh toán khoản mới."
        },
        {
            "Bản lề cửa tủ trong phòng KTX bị lỏng và khó đóng.",
            "Em tạo yêu cầu sửa chữa, ghi số phòng, vị trí tủ và thời điểm bắt đầu phát hiện.",
            "Em có thể gửi ảnh nhưng không muốn chụp đồ dùng cá nhân trong phòng.",
            "Em chỉ chụp cận cảnh phần bản lề và vùng hư hỏng, tránh để lộ đồ dùng hoặc thông tin riêng tư.",
            "Nếu cần kiểm tra khi em đang đi học thì sao ạ?",
            "Em ghi khung giờ phù hợp và cách liên hệ; bộ phận quản lý sẽ trao đổi lịch tiếp cận theo quy định KTX."
        },
        {
            "Khu vực rửa tay ở tầng em đang ở không có nước từ sáng."
        },
        {
            "Em nhập đúng mật khẩu nhưng cổng sinh viên vẫn báo sai thông tin đăng nhập.",
            "Em kiểm tra bàn phím, kiểu gõ và địa chỉ cổng đang truy cập trước khi thử lại.",
            "Em vừa đổi mật khẩu hôm qua trên điện thoại.",
            "Em thử đăng nhập bằng mật khẩu mới và đóng phiên cũ nếu trang còn lưu thông tin tự động; không gửi mật khẩu cho bộ phận hỗ trợ.",
            "Nếu vẫn lỗi thì em cung cấp thông tin gì?",
            "Em ghi mã sinh viên, thời điểm lỗi, trình duyệt và ảnh thông báo đã che dữ liệu nhạy cảm."
        },
        {
            "Trang đăng ký môn bị đăng xuất khi em bấm lưu lựa chọn.",
            "Em đăng nhập lại và kiểm tra danh sách học phần đã chọn trước khi thao tác tiếp.",
            "Em không rõ lựa chọn vừa rồi đã được lưu chưa.",
            "Em kiểm tra lịch sử hoặc bản tóm tắt đăng ký; nếu không có dấu hiệu thành công, chụp thông báo và gửi bộ phận CNTT cùng mã học phần.",
            "Em nên thử lại trên trình duyệt khác không?",
            "Có thể thử một trình duyệt cập nhật khác sau khi xác nhận trạng thái giao dịch, tránh gửi lặp cùng một yêu cầu."
        },
        {
            "Em nhận được email yêu cầu đăng nhập lại qua một đường dẫn lạ."
        },
        {
            "Em mất quyền truy cập số điện thoại dùng để xác minh tài khoản.",
            "Em dùng quy trình khôi phục tài khoản chính thức và chuẩn bị thông tin định danh mà hệ thống yêu cầu.",
            "Em có thể mượn số điện thoại bạn để nhận mã không?",
            "Không nên dùng số của người khác nếu quy trình không cho phép; hãy chọn phương thức xác minh thay thế hoặc liên hệ CNTT.",
            "Nếu cần xác minh trực tiếp, em đặt lịch ở đâu?",
            "Em gửi yêu cầu hỗ trợ tài khoản để nhận hướng dẫn kênh xác minh và giấy tờ phù hợp."
        },
        {
            "Trang xem thời khóa biểu cứ hiện vòng xoay tải dữ liệu.",
            "Em thử làm mới trang và kiểm tra kết nối, sau đó ghi thời điểm lỗi còn xảy ra.",
            "Trang khác vẫn chạy bình thường nhưng riêng thời khóa biểu không mở.",
            "Em gửi đường dẫn trang, trình duyệt, thiết bị và ảnh lỗi; không gửi cookie hay dữ liệu phiên đăng nhập.",
            "Em có cần đính kèm nhật ký trình duyệt không?",
            "Chỉ làm theo hướng dẫn của bộ phận CNTT nếu họ cần dữ liệu kỹ thuật; không chia sẻ tệp chứa thông tin xác thực."
        },
        {
            "Em muốn kiểm tra danh sách sách đang mượn và hạn trả.",
            "Em mở tài khoản thư viện, vào mục tài liệu đang mượn rồi đối chiếu mã từng cuốn.",
            "Một cuốn em đã trả ở quầy vẫn hiện đang mượn.",
            "Em giữ biên nhận hoặc thời điểm trả và yêu cầu thư viện kiểm tra trạng thái lưu thông của đúng mã tài liệu.",
            "Em có nên gia hạn cuốn đó trước không?",
            "Hãy xác minh giao dịch trả trước để tránh tạo thao tác gia hạn không cần thiết cho cuốn đã được nhận lại."
        },
        {
            "Em tìm tài liệu về an toàn mạng nhưng kết quả có quá nhiều mục.",
            "Em thử kết hợp từ khóa chuyên ngành với loại tài liệu hoặc năm xuất bản.",
            "Em cần tài liệu có thể truy cập toàn văn trực tuyến.",
            "Em dùng bộ lọc quyền truy cập hoặc loại tài nguyên nếu danh mục hỗ trợ, rồi mở chi tiết từng kết quả để kiểm tra.",
            "Nếu chỉ có bản in thì em xem vị trí ở đâu?",
            "Trang chi tiết thường thể hiện thông tin bản in và vị trí lưu trữ; em ghi mã phân loại để tìm tại thư viện."
        },
        {
            "Em đăng nhập thư viện điện tử được nhưng không thấy quyền truy cập tài liệu thuê bao.",
            "Em kiểm tra tài khoản có được xác thực là sinh viên hiện tại và đã đồng bộ quyền truy cập chưa.",
            "Em vừa được cấp lại mã sinh viên sau khi điều chỉnh hồ sơ.",
            "Em gửi thông tin tài khoản và thời điểm cập nhật cho thư viện qua kênh chính thức để đối chiếu dữ liệu; không gửi mật khẩu.",
            "Trong lúc chờ, em có thể dùng tài khoản cũ không?",
            "Em nên dùng tài khoản hợp lệ hiện tại theo hướng dẫn của thư viện và tránh tạo thêm tài khoản trùng."
        },
        {
            "Em cần gia hạn một cuốn sách nhưng hạn trả đã qua hôm qua.",
            "Em xem trạng thái lưu thông của cuốn sách và quy định gia hạn cho tài liệu đó.",
            "Hệ thống không hiện nút gia hạn cho cuốn này.",
            "Có thể tài liệu thuộc nhóm không gia hạn hoặc đã có người đặt trước; em hỏi thủ thư để biết lựa chọn xử lý.",
            "Nếu em đã bị tính phí quá hạn thì sao?",
            "Em đề nghị đối chiếu ngày trả, lịch sử gia hạn và thông báo phí trước khi thanh toán."
        },
        {
            "Em tìm được tên bài báo nhưng đường dẫn toàn văn không mở."
        },
        {
            "Em muốn tìm kỳ thực tập phù hợp với ngành đang học.",
            "Em theo dõi thông báo của khoa và kênh Quan hệ doanh nghiệp, chú ý yêu cầu kiến thức và thời gian thực tập.",
            "Có tin tuyển thực tập ghi thời gian toàn thời gian trong khi em còn học hai môn.",
            "Em xác minh lịch làm với doanh nghiệp và hỏi khoa về điều kiện công nhận thực tập trước khi nhận lời.",
            "Em nên chuẩn bị CV trước hay đợi đến khi có vị trí?",
            "Em có thể chuẩn bị CV, bảng kỹ năng và dự án cá nhân trước để kịp ứng tuyển khi đợt phù hợp mở."
        },
        {
            "Em thấy tin tuyển dụng yêu cầu nộp hồ sơ qua biểu mẫu ngoài trường.",
            "Em xác minh tên doanh nghiệp, vị trí, hạn nộp và kênh liên hệ qua nguồn chính thức trước khi gửi dữ liệu.",
            "Biểu mẫu yêu cầu ảnh giấy tờ định danh ngay từ bước đầu.",
            "Em chỉ cung cấp dữ liệu cần thiết sau khi xác minh tính hợp lệ của đơn vị tuyển dụng; không gửi thông tin nhạy cảm khi chưa rõ mục đích.",
            "Nếu muốn hỏi trường tin đó có đáng tin không thì sao?",
            "Em gửi liên kết tin tuyển dụng cho bộ phận Quan hệ doanh nghiệp kiểm tra nguồn và thông tin công bố."
        },
        {
            "CV sinh viên của em chưa có kinh nghiệm làm việc chính thức.",
            "Em có thể trình bày dự án môn học, trách nhiệm cụ thể, công nghệ sử dụng và kết quả kiểm chứng được.",
            "Em nên đưa điểm GPA lên đầu CV không?",
            "Em ưu tiên yêu cầu của vị trí; GPA có thể đặt ở phần học vấn, còn dự án liên quan nên dễ nhìn nếu vị trí đánh giá kỹ năng thực hành.",
            "Em có cần đưa mọi môn đã học vào CV không?",
            "Không cần; hãy chọn học phần, đồ án và chứng chỉ liên quan trực tiếp đến công việc đang ứng tuyển."
        },
        {
            "Em muốn hỏi giấy xác nhận thực tập cần nộp kèm những tài liệu nào."
        },
        {
            "Em nộp CV qua cổng việc làm nhưng không thấy trạng thái hồ sơ.",
            "Em kiểm tra email xác nhận và mục hồ sơ đã gửi, chú ý tài khoản đăng nhập và vị trí ứng tuyển.",
            "Em nhận thông báo lỗi sau khi tệp PDF tải lên hoàn tất.",
            "Em kiểm tra dung lượng, tên tệp và khả năng mở PDF; sau đó chụp lại thông báo lỗi để bộ phận hỗ trợ tra cứu.",
            "Em có nên gửi CV trực tiếp cho doanh nghiệp để phòng trường hợp thất lạc?",
            "Em đọc hướng dẫn tuyển dụng trước; nếu cho phép gửi trực tiếp, ghi rõ mã vị trí và tránh tạo nhiều hồ sơ không nhất quán."
        },
        {
            "Nhóm em muốn đăng ký đề tài nghiên cứu khoa học sinh viên.",
            "Em tìm thông báo đợt đăng ký hiện hành để kiểm tra biểu mẫu, điều kiện nhóm và thời hạn.",
            "Nhóm em chưa thống nhất người hướng dẫn.",
            "Em trao đổi với giảng viên phù hợp chuyên môn trước khi gửi hồ sơ, rồi xác nhận vai trò từng thành viên theo mẫu.",
            "Tên đề tài có thể thay đổi sau khi đăng ký không?",
            "Em hỏi quy trình điều chỉnh đề tài của đợt đó; thay đổi tên hoặc phạm vi có thể cần xác nhận của người hướng dẫn và đơn vị phụ trách."
        },
        {
            "Em muốn biết hội thảo nghiên cứu nào cho phép sinh viên đăng ký tham dự.",
            "Em theo dõi thông báo chính thức, đọc đối tượng tham gia, phí nếu có và hạn đăng ký.",
            "Hội thảo có yêu cầu gửi tóm tắt trước khi tham dự.",
            "Em kiểm tra mẫu tóm tắt và quy cách định dạng, sau đó xác nhận người nhận và thời hạn trên thông báo.",
            "Em có nhận giấy chứng nhận sau chương trình không?",
            "Em xem mục quyền lợi hoặc hỏi ban tổ chức; việc cấp chứng nhận phụ thuộc quy định của từng sự kiện."
        },
        {
            "Em đang viết báo cáo nghiên cứu và chưa rõ cách ghi nguồn dữ liệu.",
            "Em ghi đầy đủ tác giả hoặc đơn vị, năm, tên tài liệu và đường dẫn hay mã định danh theo chuẩn được yêu cầu.",
            "Một bộ dữ liệu tải về không ghi rõ giấy phép sử dụng.",
            "Em kiểm tra trang phát hành hoặc liên hệ đơn vị cung cấp trước khi sử dụng, đặc biệt khi định công bố kết quả.",
            "Nếu dữ liệu có thông tin cá nhân thì em cần lưu ý gì?",
            "Em không đưa dữ liệu định danh vào báo cáo công khai nếu chưa có căn cứ và biện pháp xử lý phù hợp."
        },
        {
            "Hồ sơ đề tài của nhóm em bị trả lại vì thiếu xác nhận người hướng dẫn.",
            "Em mở phản hồi hồ sơ và kiểm tra biểu mẫu yêu cầu ký, xác nhận nội dung và vị trí chữ ký.",
            "Bản nhóm tải lên là bản scan từ bản giấy đã ký.",
            "Em kiểm tra yêu cầu về định dạng và tính xác thực; nếu cần bản khác, hỏi bộ phận tiếp nhận trước khi nộp lại.",
            "Em gửi lại hồ sơ ở mục nào để không tạo mã mới?",
            "Em dùng chức năng bổ sung của hồ sơ đang xử lý nếu có, và lưu mã hồ sơ cũ để theo dõi lịch sử."
        },
        {
            "Em đã đăng ký hội thảo nhưng chưa nhận thư xác nhận tham dự."
        },
        {
            "Em muốn kiểm tra thẻ bảo hiểm y tế sinh viên còn hiệu lực không.",
            "Em tra cứu bằng thông tin trên thẻ qua kênh chính thức của đơn vị bảo hiểm hoặc hướng dẫn của nhà trường.",
            "Thông tin trên portal ghi ngày kết thúc khác với ứng dụng tra cứu.",
            "Em chụp hai trạng thái có che thông tin không cần thiết và gửi đơn vị phụ trách để đối chiếu dữ liệu cập nhật.",
            "Em có nên mua lại thẻ mới ngay không?",
            "Em xác minh thời hạn và trạng thái đóng phí trước khi thực hiện thủ tục mới để tránh trùng hoặc sai thông tin."
        },
        {
            "Em muốn tìm hiểu nơi tiếp nhận sơ cứu khi bị đau trong giờ học.",
            "Em xác định trạm y tế hoặc đầu mối hỗ trợ gần nơi đang học theo thông tin chính thức của trường.",
            "Nếu tình trạng trở nặng thì em có nên chờ trả lời trên portal không?",
            "Không nên chờ tư vấn qua portal khi có dấu hiệu khẩn cấp; em cần báo người ở gần và liên hệ dịch vụ cấp cứu phù hợp.",
            "Sau khi được hỗ trợ, em có thể hỏi giấy xác nhận để xin phép nghỉ học không?",
            "Em hỏi cơ sở tiếp nhận về giấy tờ có thể cấp và thực hiện thủ tục nghỉ học theo quy định của trường."
        },
        {
            "Em cần hỏi thông tin về dịch vụ tư vấn sức khỏe dành cho sinh viên.",
            "Em liên hệ trạm y tế hoặc kênh hỗ trợ chính thức để biết phạm vi dịch vụ, lịch tiếp nhận và cách đặt lịch.",
            "Em muốn đặt lịch nhưng không muốn ghi chi tiết riêng tư trong phần mô tả.",
            "Em chỉ cung cấp thông tin cần thiết để đặt lịch và trao đổi nội dung riêng tư trực tiếp với nhân viên có trách nhiệm.",
            "Thông tin em gửi trong yêu cầu có hiển thị công khai không?",
            "Em đọc thông tin quyền riêng tư của hệ thống và chọn kênh chính thức dành cho dữ liệu sức khỏe; không đăng thông tin cá nhân trong mục công khai."
        },
        {
            "Em cần bổ sung giấy tờ để cập nhật thông tin bảo hiểm y tế."
        },
        {
            "Em muốn hỏi về việc xin tư vấn sức khỏe trong thời gian ôn thi.",
            "Em có thể hỏi trạm y tế về giờ tiếp nhận và hình thức đặt lịch được nhà trường công bố.",
            "Em đang có triệu chứng kéo dài nhưng chưa biết nên đến đâu.",
            "Em nên trao đổi trực tiếp với nhân viên y tế để được hướng dẫn phù hợp; phần chat hành chính không thể chẩn đoán bệnh.",
            "Em cần mang thông tin gì khi đến?",
            "Em chuẩn bị giấy tờ cần thiết theo hướng dẫn tiếp nhận và liệt kê ngắn gọn thời điểm, diễn biến triệu chứng để trình bày với nhân viên y tế."
        },
        {
            "Máy chiếu trong phòng em không nhận tín hiệu từ máy tính giảng viên.",
            "Em báo mã phòng, tiết học, loại thiết bị và nội dung thông báo đang hiển thị.",
            "Lớp đã thử đổi cáp nhưng hình ảnh vẫn không lên.",
            "Em ghi lại những bước kiểm tra đã làm để kỹ thuật viên không mất thời gian lặp lại và tránh tự tháo thiết bị.",
            "Nếu lớp sắp bắt đầu thì báo qua đâu nhanh nhất?",
            "Em dùng đầu mối sự cố khẩn cấp được niêm yết tại phòng hoặc theo hướng dẫn quản lý cơ sở vật chất."
        },
        {
            "Lớp em được chuyển sang phòng khác nhưng thời khóa biểu chưa đổi.",
            "Em đối chiếu thông báo của khoa với thời khóa biểu trên portal và ghi mã lớp, môn học, tiết học.",
            "Hai thông báo ghi hai phòng khác nhau.",
            "Em gửi cả thời điểm và nguồn thông báo để đơn vị phụ trách xác nhận thông tin có hiệu lực trước giờ học.",
            "Em có nên tự chọn một trong hai phòng không?",
            "Em không nên tự suy đoán; hãy liên hệ giảng viên hoặc đầu mối xếp phòng để tránh làm gián đoạn lớp khác."
        },
        {
            "Ổ cắm ở bàn cuối phòng thực hành không có điện.",
            "Em gửi mã phòng, vị trí ổ cắm và thời điểm phát hiện; tạm thời không sử dụng ổ cắm có dấu hiệu mất an toàn.",
            "Em có thể dùng dây nối từ bàn bên cạnh không?",
            "Không tự nối dây hoặc quá tải ổ cắm khác. Em báo giảng viên và chờ nhân viên kỹ thuật kiểm tra.",
            "Em có cần đánh dấu vị trí trên ảnh không?",
            "Ảnh chụp từ khoảng cách an toàn có thể giúp nhận biết vị trí, miễn là không có người hoặc thông tin riêng tư không cần thiết."
        },
        {
            "Em muốn báo ghế trong phòng học bị nứt chân.",
            "Em ghi mã phòng, vị trí ghế và mô tả mức độ hư hỏng để quản lý phân loại yêu cầu.",
            "Ghế vẫn dùng được nhưng em lo có thể gãy khi ngồi.",
            "Em tránh sử dụng ghế đó, báo giảng viên hoặc nhân viên gần nhất và gửi yêu cầu bảo trì để kiểm tra an toàn.",
            "Nếu có nhiều ghế hỏng cùng phòng thì tạo từng yêu cầu sao?",
            "Em liệt kê số lượng và vị trí từng ghế trong một yêu cầu cùng phòng nếu chúng thuộc cùng đợt kiểm tra."
        },
        {
            "Câu lạc bộ em muốn đăng ký phòng họp cho buổi sinh hoạt tuần tới."
        },
        {
            "Em muốn kiểm tra lịch thi cuối kỳ của hai môn cùng khối kiến thức.",
            "Em xem lịch thi công bố mới nhất và đối chiếu mã học phần, nhóm lớp, ngày cùng ca thi.",
            "Portal và file PDF em lưu tuần trước không trùng giờ.",
            "Em kiểm tra ngày cập nhật của thông báo và dùng phiên bản mới nhất; nếu vẫn mâu thuẫn, gửi mã môn để đơn vị khảo thí xác nhận.",
            "Em có cần liên hệ giảng viên luôn không?",
            "Em có thể thông báo cho giảng viên, đồng thời yêu cầu đầu mối khảo thí xác nhận lịch chính thức để tránh dựa vào bản cũ."
        },
        {
            "Em muốn đăng ký phúc khảo bài thi nhưng chưa biết thời hạn.",
            "Em đọc thông báo kết quả thi để xác định cửa sổ tiếp nhận đơn và hình thức nộp được quy định.",
            "Em không đồng ý một câu trong bài nhưng không có bản chụp bài thi.",
            "Em nêu môn, ca thi và nội dung muốn làm rõ; đơn vị phụ trách sẽ hướng dẫn cách tiếp cận bài thi theo quy trình.",
            "Nộp đơn có đồng nghĩa điểm chắc chắn tăng không ạ?",
            "Không. Phúc khảo là quy trình kiểm tra lại theo quy định; kết quả phụ thuộc việc rà soát thực tế."
        },
        {
            "Em nhận được nhiều khảo sát môn học và không rõ cái nào bắt buộc.",
            "Em mở từng thông báo, xem đơn vị gửi, thời hạn và nội dung yêu cầu; không dựa riêng vào tiêu đề email.",
            "Một đường dẫn khảo sát yêu cầu đăng nhập bằng tài khoản lạ.",
            "Em không nhập thông tin vào đường dẫn chưa xác minh. Hãy truy cập cổng chính thức hoặc hỏi đơn vị tổ chức để xác nhận liên kết.",
            "Phản hồi khảo sát có ảnh hưởng điểm môn không?",
            "Em đọc mô tả của khảo sát cụ thể; nếu thông báo không nói rõ, hỏi đơn vị tổ chức thay vì suy đoán."
        },
        {
            "Hai lịch thi của em hiện trùng nhau trên portal."
        },
        {
            "Em đã nộp đơn phúc khảo nhưng không tìm thấy mã hồ sơ.",
            "Em kiểm tra email xác nhận, lịch sử yêu cầu và mục đơn đã gửi trên cổng.",
            "Em chỉ có ảnh chụp màn hình báo gửi thành công.",
            "Em gửi thời điểm nộp, học phần và ảnh thông báo cho đơn vị khảo thí để tra cứu hồ sơ, không cần tạo đơn mới ngay.",
            "Nếu hết hạn mà vẫn chưa có phản hồi thì sao ạ?",
            "Em liên hệ theo đầu mối và thời hạn giải quyết trong thông báo, kèm dữ liệu giúp xác định đơn ban đầu."
        },
        {
            "Em muốn tìm thông tin tuyển sinh sau đại học cho ngành gần với bằng đại học.",
            "Em xem đề án tuyển sinh của đúng đợt để đối chiếu ngành phù hợp, điều kiện dự tuyển và yêu cầu ngoại ngữ.",
            "Tên ngành đại học của em khác tên ngành thạc sĩ dự định đăng ký.",
            "Em kiểm tra danh mục ngành phù hợp và quy định về học bổ sung kiến thức; nếu còn vướng, gửi tên văn bằng và ngành dự tuyển để được hướng dẫn.",
            "Em có thể dùng chứng chỉ ngoại ngữ đã thi từ hai năm trước không?",
            "Em xem thời hạn chấp nhận chứng chỉ nêu trong đề án hiện hành và loại chứng chỉ được công nhận."
        },
        {
            "Em đã tạo hồ sơ dự tuyển sau đại học nhưng còn thiếu bản sao văn bằng.",
            "Em mở danh sách thành phần hồ sơ và xem loại bản sao hoặc hình thức chứng thực được chấp nhận.",
            "Bản scan của em rõ nhưng bản giấy sẽ có sau hạn đăng ký.",
            "Em hỏi bộ phận tuyển sinh liệu có hình thức bổ sung sau hay không; không mặc định hồ sơ sẽ hợp lệ khi thiếu giấy tờ bắt buộc.",
            "Em có thể ghi chú sẽ bổ sung trong phần mô tả không?",
            "Em có thể giải thích tình trạng, nhưng cần nhận xác nhận chính thức về việc chấp nhận bổ sung và thời hạn."
        },
        {
            "Em đang học cao học và muốn đăng ký học phần nghiên cứu trong kỳ tiếp theo.",
            "Em kiểm tra kế hoạch học tập của khóa, học phần tiên quyết và thời gian đăng ký được thông báo.",
            "Môn nghiên cứu không xuất hiện trong danh sách chọn học phần.",
            "Em kiểm tra mình có đủ điều kiện và đúng giai đoạn đăng ký; nếu có, gửi mã chương trình và tên học phần để đơn vị sau đại học đối chiếu.",
            "Nếu tiến độ đề tài thay đổi thì có cần báo không?",
            "Em hỏi người hướng dẫn và bộ phận phụ trách về thủ tục cập nhật tiến độ, đặc biệt nếu ảnh hưởng thời hạn hoàn thành chương trình."
        },
        {
            "Hồ sơ dự tuyển sau đại học của em bị báo thiếu ảnh văn bằng.",
            "Em mở phản hồi hồ sơ và xác nhận yêu cầu về định dạng, dung lượng, mặt giấy tờ cần tải.",
            "Em đã tải ảnh nhưng hệ thống vẫn đánh dấu chưa có tệp.",
            "Em kiểm tra tên tệp, định dạng được hỗ trợ và trạng thái lưu sau khi tải; nếu còn lỗi, gửi mã hồ sơ và thông báo hiển thị.",
            "Em có nên tạo bộ hồ sơ thứ hai để thử không?",
            "Không nên tạo hồ sơ trùng; hãy xử lý trên hồ sơ hiện tại hoặc hỏi bộ phận tuyển sinh cách cập nhật an toàn."
        },
        {
            "Em nhận được yêu cầu bổ sung giấy tờ nhưng chưa rõ tệp nào chưa đạt."
        },
    };

    private final UserRepository users;
    private final CategoryRepository categories;
    private final ChatSessionRepository sessions;
    private final ChatMessageRepository messages;
    private final ManagerScopeService scopes;
    private final EntityManager entityManager;

    @Override
    public int order() {
        return 60;
    }

    @Override
    @Transactional
    public void seed() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        Map<String, User> managers = new HashMap<>();
        for (String[] account : MANAGER_ACCOUNTS) {
            managers.put(account[0], user(account[1]));
        }

        Map<String, Category> categoryMap = new HashMap<>();
        for (String code : CATEGORY_CODES) {
            Category category = categories.findByCode(code)
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy chuyên mục chat demo: " + code));
            categoryMap.put(code, category);
        }
        if (CATEGORY_CODES.length != DIALOGUES.length
                || CATEGORY_CODES.length != SESSION_STATUSES.length
                || CATEGORY_CODES.length != STUDENT_NUMBERS.length
                || CATEGORY_CODES.length != RATINGS.length
                || CATEGORY_CODES.length != END_REASONS.length
                || CATEGORY_CODES.length != ENDED_BY_STUDENT.length
                || CATEGORY_CODES.length != RATING_COMMENTS.length
                || CATEGORY_CODES.length != SYSTEM_MESSAGES.length) {
            throw new IllegalStateException("Các mảng dữ liệu chat demo không cùng số phần tử");
        }

        for (int index = 0; index < CATEGORY_CODES.length; index++) {
            String categoryCode = CATEGORY_CODES[index];
            String[] dialogue = DIALOGUES[index];
            int studentNumber = STUDENT_NUMBERS[index];
            String initialQuestion = dialogue[0];
            User student = user("241621" + String.format("%02d", studentNumber) + "@student.hcmute.edu.vn");
            if (sessions.existsByStudentIdAndInitialQuestion(student.getId(), initialQuestion)) {
                continue;
            }

            Category category = categoryMap.get(categoryCode);
            if (category == null) {
                throw new IllegalStateException("Không tìm thấy chuyên mục chat demo: " + categoryCode);
            }
            boolean expired = SESSION_STATUSES[index] == ChatSessionStatus.EXPIRED;
            User manager = expired ? null : getManagerForCategory(categoryCode, managers);
            if (manager != null && !scopes.isInScope(manager.getId(), category.getId())) {
                throw new IllegalStateException("Quản lý chưa được gán chuyên mục chat: " + categoryCode);
            }

            LocalDateTime started = now.toLocalDate()
                    .minusDays(2L + index * 2L)
                    .atTime(8 + index % 10, (index * 7) % 60);
            int waitSeconds = 25 + (index % 9) * 23;
            int durationMinutes = 6 + (index % 11) * 2;
            LocalDateTime accepted = expired ? null : started.plusSeconds(waitSeconds);
            LocalDateTime ended = expired
                    ? started.plusMinutes(10 + index % 7)
                    : accepted.plusMinutes(durationMinutes);

            ChatSession session = new ChatSession();
            session.setStudentId(student.getId());
            session.setManagerId(manager == null ? null : manager.getId());
            session.setCategoryId(category.getId());
            session.setInitialQuestion(initialQuestion);
            session.setStatus(SESSION_STATUSES[index]);
            session.setStartedAt(started);
            session.setAcceptedAt(accepted);
            session.setEndedAt(ended);
            session.setEndedBy(expired ? null : (ENDED_BY_STUDENT[index] ? student.getId() : manager.getId()));
            session.setEndReason(END_REASONS[index]);
            session.setLastMessageAt(expired ? ended : ended.minusSeconds(10));
            if (!expired && RATINGS[index] > 0) {
                session.setRating(RATINGS[index]);
                session.setRatingComment(RATING_COMMENTS[index]);
            }
            sessions.saveAndFlush(session);

            List<ChatMessage> conversation = new ArrayList<>();
            if (expired) {
                ChatMessage first = new ChatMessage();
                first.setSessionId(session.getId());
                first.setClientMessageId("seed-chat-" + index + "-0");
                first.setSenderType(ChatSenderType.STUDENT);
                first.setSenderId(student.getId());
                first.setContentType(ChatContentType.TEXT);
                first.setContent(dialogue[0]);
                first.setSentAt(started);
                first.setReadAt(null);
                conversation.add(first);

                ChatMessage system = new ChatMessage();
                system.setSessionId(session.getId());
                system.setClientMessageId("seed-chat-" + index + "-1");
                system.setSenderType(ChatSenderType.SYSTEM);
                system.setSenderId(null);
                system.setContentType(ChatContentType.SYSTEM);
                system.setContent(SYSTEM_MESSAGES[index]);
                system.setSentAt(ended);
                system.setReadAt(null);
                conversation.add(system);
            } else {
                if (dialogue.length < 2 || dialogue.length % 2 != 0) {
                    throw new IllegalStateException("Hội thoại chat phải có số lượt chẵn: " + categoryCode);
                }
                for (int line = 0; line < dialogue.length; line++) {
                    boolean studentMessage = line % 2 == 0;
                    LocalDateTime sent = line == 0
                            ? started
                            : accepted.plusSeconds((durationMinutes * 60L - 10) * (line - 1L) / (dialogue.length - 2L));
                    ChatMessage message = new ChatMessage();
                    message.setSessionId(session.getId());
                    message.setClientMessageId("seed-chat-" + index + "-" + line);
                    message.setSenderType(studentMessage ? ChatSenderType.STUDENT : ChatSenderType.MANAGER);
                    message.setSenderId(studentMessage ? student.getId() : manager.getId());
                    message.setContentType(ChatContentType.TEXT);
                    message.setContent(dialogue[line]);
                    message.setSentAt(sent);
                    message.setReadAt(sent.plusSeconds(5));
                    conversation.add(message);
                }
            }
            messages.saveAllAndFlush(conversation);

            entityManager.createQuery(
                    "update ChatSession s set s.createdAt = :started, s.updatedAt = :ended where s.id = :id")
                    .setParameter("started", started)
                    .setParameter("ended", ended)
                    .setParameter("id", session.getId())
                    .executeUpdate();
            entityManager.createQuery(
                    "update ChatMessage m set m.createdAt = m.sentAt, m.updatedAt = m.sentAt where m.sessionId = :id")
                    .setParameter("id", session.getId())
                    .executeUpdate();
        }
    }

    private User getManagerForCategory(String categoryCode, Map<String, User> managers) {
        int separator = categoryCode.indexOf('_');
        if (separator <= 0) {
            throw new IllegalStateException("Mã chuyên mục chat không hợp lệ: " + categoryCode);
        }
        String prefix = categoryCode.substring(0, separator);
        User manager = managers.get(prefix);
        if (manager == null) {
            throw new IllegalStateException("Không tìm thấy quản lý phụ trách chuyên mục: " + categoryCode);
        }
        return manager;
    }

    private User user(String email) {
        return users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Thiếu tài khoản demo " + email));
    }

}
