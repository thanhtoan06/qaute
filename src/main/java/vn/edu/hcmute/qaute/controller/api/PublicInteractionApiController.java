package vn.edu.hcmute.qaute.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.view.FaqDetailView;
import vn.edu.hcmute.qaute.dto.view.FaqSuggestion;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;

@RestController
@RequestMapping("/api/public")
public class PublicInteractionApiController {

    private final FaqQueryService faqQueryService;

    public PublicInteractionApiController(FaqQueryService faqQueryService) {
        this.faqQueryService = faqQueryService;
    }

    @PostMapping("/chat/message")
    public ApiResponse<Map<String, Object>> chatMessage(@RequestBody Map<String, String> payload) {
        String message = payload.getOrDefault("message", "").trim();
        Map<String, Object> result = new HashMap<>();

        if (message.isBlank()) {
            result.put("reply", "Bạn vui lòng nhập câu hỏi để QAUTE có thể hỗ trợ nhé!");
            return ApiResponse.ok(result);
        }

        List<FaqSuggestion> suggestions = faqQueryService.suggest(message, 4);

        if (!suggestions.isEmpty()) {
            FaqSuggestion top = suggestions.get(0);
            try {
                FaqDetailView detail = faqQueryService.getPublishedBySlug(top.slug());
                String snippet = detail.summary();
                if (snippet == null || snippet.isBlank()) {
                    snippet = detail.bodyHtml().replaceAll("<[^>]*>", " ");
                    if (snippet.length() > 220) {
                        snippet = snippet.substring(0, 220) + "...";
                    }
                }
                result.put("reply", snippet.trim());
                result.put("faqTitle", detail.title());
                result.put("faqSlug", detail.slug());
                result.put("categoryName", detail.categoryPath());
            } catch (Exception e) {
                result.put("reply", "Bạn có thể tham khảo bài viết: " + top.title());
                result.put("faqTitle", top.title());
                result.put("faqSlug", top.slug());
            }
        } else {
            // Câu trả lời thông minh dựa trên ngữ cảnh câu hỏi
            String lower = message.toLowerCase();
            if (lower.contains("học phí") || lower.contains("tiền học") || lower.contains("đóng phí")) {
                result.put("reply", "Học phí học kỳ thường được nộp trong vòng 4 tuần kể từ khi bắt đầu học kỳ qua cổng thanh toán online.hcmute.edu.vn hoặc ngân hàng liên kết. Bạn có thể tra cứu chi tiết mục Học phí bên dưới!");
            } else if (lower.contains("học bổng") || lower.contains("khuyến khích")) {
                result.put("reply", "Học bổng khuyến khích học tập được xét dựa trên điểm rèn luyện (từ loại Tốt) và điểm trung bình học kỳ (từ 3.2 trở lên). Phòng CTSV sẽ thông báo danh sách sau khi kết thúc học kỳ.");
            } else if (lower.contains("đăng ký môn") || lower.contains("học phần") || lower.contains("dkhp")) {
                result.put("reply", "Lịch đăng ký môn học được Phòng Đào tạo công bố trên trang online.hcmute.edu.vn trước 3–4 tuần. Hãy lưu ý khung giờ mở cổng theo từng khóa.");
            } else if (lower.contains("bảo lưu") || lower.contains("tạm dừng")) {
                result.put("reply", "Thủ tục bảo lưu kết quả học tập thực hiện tại Phòng Đào tạo (kèm hồ sơ chứng minh hợp lệ). Thời gian bảo lưu tối đa theo quy chế đào tạo hiện hành.");
            } else if (lower.contains("ký túc xá") || lower.contains("ktx")) {
                result.put("reply", "Sinh viên chính quy HCMUTE có thể đăng ký ở KTX qua cổng thông tin. Nhà trường ưu tiên sinh viên năm nhất và các bạn diện chính sách xã hội.");
            } else {
                result.put("reply", "QAUTE chưa tìm thấy câu trả lời chính xác cho thắc mắc này trong kho tri thức nhanh. Bạn có thể gửi ticket tư vấn ngay bên dưới hoặc đặt lịch hẹn trực tiếp với chuyên viên để được hỗ trợ cụ thể nhé!");
            }
        }

        List<Map<String, String>> suggList = new ArrayList<>();
        for (FaqSuggestion s : suggestions) {
            suggList.add(Map.of(
                    "id", String.valueOf(s.id()),
                    "title", s.title(),
                    "slug", s.slug(),
                    "category", s.categoryName() != null ? s.categoryName() : ""
            ));
        }
        result.put("suggestions", suggList);

        return ApiResponse.ok(result);
    }

}
