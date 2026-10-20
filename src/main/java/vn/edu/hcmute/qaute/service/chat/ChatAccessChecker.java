package vn.edu.hcmute.qaute.service.chat;

public interface ChatAccessChecker {
    boolean isParticipant(Long sessionId, Long userId);
}
