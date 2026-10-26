package vn.edu.hcmute.qaute.dto.view;

// Đóng gói dữ liệu ChatQueueItem để hiển thị hoặc trả về API.
public record ChatQueueItem(Long id, String studentName, String categoryPath,
        String question, long waitingSeconds, int position) {
}
