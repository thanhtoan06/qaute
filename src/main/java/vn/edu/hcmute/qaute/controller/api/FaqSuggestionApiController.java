package vn.edu.hcmute.qaute.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.common.util.TokenUtil;
import vn.edu.hcmute.qaute.common.web.ClientIpResolver;
import vn.edu.hcmute.qaute.dto.request.SearchClickRequest;
import vn.edu.hcmute.qaute.dto.view.FaqSuggestion;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.knowledge.SearchLogService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho FaqSuggestionApi.
@RestController
public class FaqSuggestionApiController {

    private final FaqQueryService faqQueryService;
    private final SearchLogService searchLogService;

    // Khởi tạo bộ điều khiển gợi ý câu hỏi thường gặp.
    public FaqSuggestionApiController(FaqQueryService faqQueryService,
                                     SearchLogService searchLogService) {
        this.faqQueryService = faqQueryService;
        this.searchLogService = searchLogService;
    }

    @GetMapping("/api/faqs/suggestions")
    // Lấy các gợi ý câu hỏi thường gặp phù hợp với từ khóa.
    public ApiResponse<List<FaqSuggestion>> suggestions(@RequestParam(required = false) String q,
                                                         @RequestParam(required = false) String source) {
        if (q == null || q.trim().length() < 2) {
            return ApiResponse.ok(List.of());
        }
        List<FaqSuggestion> suggestions = faqQueryService.suggest(q, 8);
        return ApiResponse.ok(suggestions);
    }

    @PostMapping("/api/public/search-click")
    // Ghi nhận lượt nhấp vào kết quả tìm kiếm.
    public ApiResponse<Void> recordClick(@RequestBody(required = false) SearchClickRequest request,
                                         HttpServletRequest httpRequest) {
        if (request != null) {
            String clientIp = ClientIpResolver.resolve(httpRequest);
            String ipHash = TokenUtil.sha256Hex(clientIp != null ? clientIp : "");
            Long userId = SecurityUtils.currentUser().map(QauteUserDetails::getId).orElse(null);
            searchLogService.recordClick(request.query(), request.faqId(), request.source(), userId, ipHash);
        }
        return ApiResponse.ok(null);
    }
}
