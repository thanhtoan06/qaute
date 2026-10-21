package vn.edu.hcmute.qaute.config.seed;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

@Component
@Profile("demo")
@RequiredArgsConstructor
public class ChatSeeder implements DemoSeeder {
    private static final String[] CATEGORY_CODES = {"DT_DKHP", "CT_HOCBONG", "TC_HOCPHI", "KTX_DANGKY"};
    private static final int[] RATINGS = {5, 4, 0, 3, 5, 0, 4, 2};
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
        }
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
        for (int index = 0; index < 10; index++) {
            boolean expired = index >= 8;
            int department = index % 4;
            User student = user((24162101 + index) + "@student.hcmute.edu.vn");
            String question = expired ? (index == 8 ? "Em cần hỗ trợ điều chỉnh lịch đăng ký học phần."
                    : "Em muốn được tư vấn về hồ sơ hỗ trợ học tập.") : DIALOGUES[department][0];
            if (sessions.existsByStudentIdAndInitialQuestion(student.getId(), question)) continue;
            Category category = categories.findByCode(CATEGORY_CODES[department])
                    .orElseThrow(() -> new IllegalStateException("Chưa có chuyên mục chat demo"));
            User manager = expired ? null : user("manager" + (department + 1) + "@qaute.edu.vn");
            if (manager != null && !scopes.isInScope(manager.getId(), category.getId())) {
                throw new IllegalStateException("Manager demo chưa được gán chuyên mục chat");
            }
            LocalDateTime started = now.toLocalDate().minusDays(2L + index * 3L).atTime(9 + index % 2, 10);
            int waitSeconds = 30 + index * 35;
            int durationMinutes = 6 + index * 2;
            LocalDateTime accepted = expired ? null : started.plusSeconds(waitSeconds);
            LocalDateTime ended = expired ? started.plusMinutes(index == 8 ? 10 : 15)
                    : accepted.plusMinutes(durationMinutes);
            ChatSession session = new ChatSession();
            session.setStudentId(student.getId());
            session.setManagerId(manager == null ? null : manager.getId());
            session.setCategoryId(category.getId());
            session.setInitialQuestion(question);
            session.setStatus(expired ? ChatSessionStatus.EXPIRED : ChatSessionStatus.ENDED);
            session.setStartedAt(started);
            session.setAcceptedAt(accepted);
            session.setEndedAt(ended);
            session.setEndedBy(expired ? null : (index % 2 == 0 ? student.getId() : manager.getId()));
            session.setEndReason(expired ? (index == 8 ? "TIMEOUT" : "NO_AGENT")
                    : (index % 2 == 0 ? "STUDENT_END" : "MANAGER_END"));
            session.setLastMessageAt(expired ? ended : ended.minusSeconds(10));
            if (!expired && RATINGS[index] > 0) {
                session.setRating(RATINGS[index]);
                session.setRatingComment(RATINGS[index] <= 3 ? "Hướng dẫn rõ ràng, mong thời gian chờ ngắn hơn."
                        : "Tư vấn viên hỗ trợ cụ thể và dễ hiểu.");
            }
            sessions.saveAndFlush(session);
            List<ChatMessage> conversation = new ArrayList<>();
            int count = expired ? 2 : 4 + (index % 4) * 2;
            for (int line = 0; line < count; line++) {
                ChatMessage message = new ChatMessage();
                message.setSessionId(session.getId());
                message.setClientMessageId("seed-chat-" + index + "-" + line);
                boolean system = expired && line == 1;
                boolean studentMessage = line % 2 == 0;
                message.setSenderType(system ? ChatSenderType.SYSTEM
                        : studentMessage ? ChatSenderType.STUDENT : ChatSenderType.MANAGER);
                message.setSenderId(system ? null : studentMessage ? student.getId() : manager.getId());
                message.setContentType(system ? ChatContentType.SYSTEM : ChatContentType.TEXT);
                message.setContent(expired ? (line == 0 ? question
                        : "Phiên chat đã hết hạn. Em có thể tạo yêu cầu tư vấn để được hỗ trợ sau.")
                        : DIALOGUES[department][line]);
                LocalDateTime sent = line == 0 ? started : expired ? ended
                        : accepted.plusSeconds((durationMinutes * 60L - 10) * (line - 1) / (count - 2));
                message.setSentAt(sent);
                message.setReadAt(expired ? null : sent.plusSeconds(5));
                conversation.add(message);
            }
            messages.saveAllAndFlush(conversation);
            entityManager.createQuery("update ChatSession s set s.createdAt = :started, s.updatedAt = :ended where s.id = :id")
                    .setParameter("started", started).setParameter("ended", ended)
                    .setParameter("id", session.getId()).executeUpdate();
            entityManager.createQuery("update ChatMessage m set m.createdAt = m.sentAt, m.updatedAt = m.sentAt where m.sessionId = :id")
                    .setParameter("id", session.getId()).executeUpdate();
        }
    }

    private User user(String email) {
        return users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Thiếu tài khoản demo " + email));
    }
}
