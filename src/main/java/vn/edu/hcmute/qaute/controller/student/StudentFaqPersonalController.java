package vn.edu.hcmute.qaute.controller.student;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.view.FaqListItem;
import vn.edu.hcmute.qaute.repository.knowledge.FaqViewRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.knowledge.FaqPersonalService;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StudentFaqPersonal.
@Controller
public class StudentFaqPersonalController {
    private static final int PAGE_SIZE = 20;
    private static final DateTimeFormatter VIEWED_AT_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final FaqPersonalService personal;
    private final FaqQueryService faqs;
    private final FaqViewRepository views;

    // Khởi tạo bộ điều khiển FAQ cá nhân với các dịch vụ truy vấn.
    public StudentFaqPersonalController(FaqPersonalService personal, FaqQueryService faqs,
                                        FaqViewRepository views) {
        this.personal = personal;
        this.faqs = faqs;
        this.views = views;
    }

    @PostMapping("/student/faq/{id}/bookmark")
    @ResponseBody
    // Bật hoặc tắt trạng thái đánh dấu FAQ của sinh viên.
    public ApiResponse<Boolean> toggleBookmark(@PathVariable Long id) {
        return ApiResponse.ok(personal.toggleBookmark(SecurityUtils.requireUserId(), id));
    }

    @GetMapping("/student/faq/recent")
    // Hiển thị các FAQ đã xem gần đây cùng thời điểm xem.
    public String recent(@RequestParam(defaultValue = "0") int page,
                         Model model) {
        Long userId = SecurityUtils.requireUserId();
        Page<FaqListItem> recent = personal.recent(userId, PageRequest.of(Math.max(0, page), PAGE_SIZE));
        List<Long> faqIds = recent.getContent().stream().map(FaqListItem::id).toList();
        Set<Long> bookmarkedIds = faqs.bookmarkedIds(userId, faqIds);
        Map<Long, String> viewedAt = views.findByUserIdAndFaqIdIn(userId, faqIds).stream()
                .collect(Collectors.toMap(view -> view.getFaqId(),
                        view -> view.getLastViewedAt().format(VIEWED_AT_FORMAT)));
        model.addAttribute("page", recent);
        model.addAttribute("bookmarkedIds", bookmarkedIds);
        model.addAttribute("viewedAt", viewedAt);
        return "student/faq-recent";
    }

    @GetMapping("/student/faq/saved")
    // Chuyển đến danh sách FAQ đã lưu.
    public String saved() {
        return "redirect:/student/faq?tab=saved";
    }
}
