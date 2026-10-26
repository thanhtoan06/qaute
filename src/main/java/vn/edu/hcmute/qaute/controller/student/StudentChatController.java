package vn.edu.hcmute.qaute.controller.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StudentChat.
@Controller
public class StudentChatController {

    private final CategoryQueryService categoryQueryService;

    // Khởi tạo bộ điều khiển trò chuyện với dịch vụ danh mục.
    public StudentChatController(CategoryQueryService categoryQueryService) {
        this.categoryQueryService = categoryQueryService;
    }

    @GetMapping("/student/chat")
    // Hiển thị trang trò chuyện cùng cây danh mục đang hoạt động.
    public String chat(Model model) {
        model.addAttribute("tree", categoryQueryService.getActiveTree());
        return "student/chat";
    }
}

