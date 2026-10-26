package vn.edu.hcmute.qaute.dto.view;

// Đóng gói dữ liệu AdminTag để hiển thị hoặc trả về API.
public record AdminTagView(Long id, String name, String slug, long faqCount) {
}
