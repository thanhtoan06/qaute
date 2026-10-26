package vn.edu.hcmute.qaute.service.chat;

import java.util.Objects;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;

// Xử lý nghiệp vụ ChatAccessCheckerImpl trong hệ thống.
@Component
public class ChatAccessCheckerImpl implements ChatAccessChecker {
    private final ChatSessionRepository sessions;

    // Khởi tạo bộ kiểm tra với kho dữ liệu phiên chat.
    public ChatAccessCheckerImpl(ChatSessionRepository sessions) {
        this.sessions = sessions;
    }

    @Override
    // Kiểm tra người dùng có phải một bên của phiên chat hay không.
    public boolean isParticipant(Long sessionId, Long userId) {
        if (sessionId == null || userId == null) return false;
        return sessions.findById(sessionId).map(session -> Objects.equals(session.getStudentId(), userId)
                || Objects.equals(session.getManagerId(), userId)).orElse(false);
    }
}
