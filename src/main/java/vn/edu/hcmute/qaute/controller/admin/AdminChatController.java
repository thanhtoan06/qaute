package vn.edu.hcmute.qaute.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminChatController {
    @GetMapping("/admin/chat-history")
    public String history(Model model) {
        model.addAttribute("pageTitle", "Lịch sử tư vấn trực tuyến toàn hệ thống");
        model.addAttribute("historyApiUrl", "/api/chat/admin-history");
        return "common/chat-history";
    }
}
