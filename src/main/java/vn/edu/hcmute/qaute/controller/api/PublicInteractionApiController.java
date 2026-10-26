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

// Tiếp nhận và điều phối các yêu cầu giao diện cho PublicInteractionApi.
@RestController
@RequestMapping("/api/public")
public class PublicInteractionApiController {

    private final FaqQueryService faqQueryService;

    // Khởi tạo bộ điều khiển tương tác công khai.
    public PublicInteractionApiController(FaqQueryService faqQueryService) {
        this.faqQueryService = faqQueryService;
    }

    @PostMapping("/chat/message")
    // Trả lời câu hỏi công khai dựa trên các câu hỏi thường gặp phù hợp.
    public ApiResponse<Map<String, Object>> chatMessage(@RequestBody Map<String, String> payload) {
        String message = payload.getOrDefault("message", "").trim();
        Map<String, Object> result = new HashMap<>();

        if (message.isBlank()) {
            result.put("reply", "Bạn vui lòng nhập câu hỏi để QAUTE có thể hỗ trợ nhé!");
            return ApiResponse.ok(result);
        }

        List<FaqSuggestion> suggestions = findSmartSuggestions(message);

        if (!suggestions.isEmpty()) {
            FaqSuggestion top = suggestions.get(0);
            try {
                FaqDetailView detail = faqQueryService.getPublishedBySlug(top.slug());
                // Trả lời bằng nội dung chi tiết (bodyHtml), không dùng tóm tắt (summary)
                String content = detail.bodyHtml();
                if (content == null || content.isBlank()) {
                    content = detail.summary();
                }
                result.put("reply", content != null ? content.trim() : "");
                result.put("faqTitle", detail.title());
                result.put("faqSlug", detail.slug());
                result.put("categoryName", detail.categoryPath());
            } catch (Exception e) {
                result.put("reply", "Bạn có thể tham khảo bài viết: " + top.title());
                result.put("faqTitle", top.title());
                result.put("faqSlug", top.slug());
            }
        } else {
            // Câu trả lời thông minh dựa trên ngữ cảnh câu hỏi khi chưa tìm thấy bài viết
            String lower = message.toLowerCase();
            if (lower.contains("tiếng anh") || lower.contains("ngoại ngữ") || lower.contains("chuẩn đầu ra")
                    || lower.contains("toeic") || lower.contains("ielts")) {
                result.put("reply", "Chuẩn đầu ra tốt nghiệp HCMUTE yêu cầu chứng chỉ ngoại ngữ (TOEIC tối thiểu 500 điểm hoặc IELTS 5.0 tùy theo ngành) và chuẩn tin học quốc tế (MOS hoặc IC3) còn thời hạn hiệu lực. Bạn vui lòng tra cứu thêm tại mục Đào tạo hoặc liên hệ Phòng Đào tạo để được hướng dẫn chi tiết!");
            } else if (lower.contains("học phí") || lower.contains("tiền học") || lower.contains("đóng phí")) {
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

    // Tìm kiếm gợi ý bài viết linh hoạt theo ngôn ngữ tự nhiên và chủ đề
    private List<FaqSuggestion> findSmartSuggestions(String message) {
        if (message == null || message.isBlank()) {
            return List.of();
        }

        // 1. Dọn dẹp dấu câu kết thúc
        String clean = message.replaceAll("[\\?\\!\\.\\,\\:\\;]+", " ").trim();
        List<FaqSuggestion> suggestions = faqQueryService.suggest(clean, 4);
        if (!suggestions.isEmpty()) {
            return suggestions;
        }

        // 2. Nhận diện chủ đề theo từ khóa câu hỏi tự nhiên
        String lower = message.toLowerCase();

        // Chuẩn đầu ra ngoại ngữ / tiếng Anh / tin học
        if (lower.contains("tiếng anh") || lower.contains("ngoại ngữ") || lower.contains("chuẩn đầu ra")
                || lower.contains("toeic") || lower.contains("ielts") || lower.contains("vstep")
                || lower.contains("tin học") || lower.contains("mos") || lower.contains("ic3")) {
            suggestions = faqQueryService.suggest("chuẩn ngoại ngữ", 4);
            if (!suggestions.isEmpty()) return suggestions;
            suggestions = faqQueryService.suggest("ngoại ngữ", 4);
            if (!suggestions.isEmpty()) return suggestions;
            suggestions = faqQueryService.suggest("tiếng anh", 4);
            if (!suggestions.isEmpty()) return suggestions;
        }

        // Học phí / hạn đóng
        if (lower.contains("hạn đóng") || lower.contains("đóng học phí") || lower.contains("hạn nộp") || lower.contains("tiền học")) {
            suggestions = faqQueryService.suggest("hạn đóng học phí", 4);
            if (!suggestions.isEmpty()) return suggestions;
            suggestions = faqQueryService.suggest("học phí", 4);
            if (!suggestions.isEmpty()) return suggestions;
        } else if (lower.contains("học phí")) {
            suggestions = faqQueryService.suggest("học phí", 4);
            if (!suggestions.isEmpty()) return suggestions;
        }

        // Học bổng
        if (lower.contains("học bổng") || lower.contains("khuyến khích")) {
            suggestions = faqQueryService.suggest("học bổng khuyến khích", 4);
            if (!suggestions.isEmpty()) return suggestions;
            suggestions = faqQueryService.suggest("học bổng", 4);
            if (!suggestions.isEmpty()) return suggestions;
        }

        // Bảo lưu kết quả học tập
        if (lower.contains("bảo lưu") || lower.contains("tạm dừng")) {
            suggestions = faqQueryService.suggest("bảo lưu", 4);
            if (!suggestions.isEmpty()) return suggestions;
        }

        // Đăng ký học phần / môn học
        if (lower.contains("đăng ký môn") || lower.contains("đăng ký học phần") || lower.contains("dkhp") || lower.contains("hủy môn") || lower.contains("rút môn")) {
            suggestions = faqQueryService.suggest("đăng ký học phần", 4);
            if (!suggestions.isEmpty()) return suggestions;
            suggestions = faqQueryService.suggest("học phần", 4);
            if (!suggestions.isEmpty()) return suggestions;
        }

        // Điểm rèn luyện
        if (lower.contains("rèn luyện") || lower.contains("drl")) {
            suggestions = faqQueryService.suggest("điểm rèn luyện", 4);
            if (!suggestions.isEmpty()) return suggestions;
        }

        // Phúc khảo bài thi
        if (lower.contains("phúc khảo")) {
            suggestions = faqQueryService.suggest("phúc khảo", 4);
            if (!suggestions.isEmpty()) return suggestions;
        }

        // Giấy xác nhận sinh viên
        if (lower.contains("giấy xác nhận") || lower.contains("xác nhận sinh viên")) {
            suggestions = faqQueryService.suggest("giấy xác nhận", 4);
            if (!suggestions.isEmpty()) return suggestions;
        }

        // Ký túc xá
        if (lower.contains("ký túc xá") || lower.contains("ktx")) {
            suggestions = faqQueryService.suggest("ký túc xá", 4);
            if (!suggestions.isEmpty()) return suggestions;
        }

        // Bảo hiểm y tế
        if (lower.contains("bảo hiểm y tế") || lower.contains("bhyt")) {
            suggestions = faqQueryService.suggest("bảo hiểm y tế", 4);
            if (!suggestions.isEmpty()) return suggestions;
        }

        // 3. Fallback: Lọc bỏ stop words để tìm kiếm từ khóa cốt lõi
        String withoutStopWords = clean.replaceAll("(?i)\\b(cho|em|hỏi|về|cách|làm|sao|thế|nào|là|gì|khi|như|được|không|có|phải|để|xin|với|ở|này|ạ|ơi|tại|sao)\\b", " ")
                .replaceAll("\\s+", " ").trim();
        if (withoutStopWords.length() >= 3 && !withoutStopWords.equalsIgnoreCase(clean)) {
            suggestions = faqQueryService.suggest(withoutStopWords, 4);
            if (!suggestions.isEmpty()) return suggestions;
        }

        return List.of();
    }

}
