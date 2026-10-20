package vn.edu.hcmute.qaute.service.chat;

import java.util.Objects;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;

@Component
public class ChatAccessCheckerImpl implements ChatAccessChecker {
    private final ChatSessionRepository sessions;

    public ChatAccessCheckerImpl(ChatSessionRepository sessions) {
        this.sessions = sessions;
    }

    @Override
    public boolean isParticipant(Long sessionId, Long userId) {
        return sessions.findById(sessionId).map(session -> Objects.equals(session.getStudentId(), userId)
                || Objects.equals(session.getManagerId(), userId)).orElse(false);
    }
}
