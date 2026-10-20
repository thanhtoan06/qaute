package vn.edu.hcmute.qaute.dto.view;

public record ChatQueueItem(Long id, String studentName, String categoryPath,
        String question, long waitingSeconds, int position) {
}
