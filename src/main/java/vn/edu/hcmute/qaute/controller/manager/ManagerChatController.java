package vn.edu.hcmute.qaute.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Tiếp nhận và điều phối các yêu cầu giao diện cho ManagerChat.
@Controller
public class ManagerChatController {
    @GetMapping("/manager/chat")
    // Hiển thị bàn tư vấn trực tuyến cho quản lý.
    public String desk(Model model) {
        model.addAttribute("pageTitle", "Bàn tư vấn trực tuyến");
        return "manager/chat-desk";
    }

    @GetMapping("/manager/chat-history")
    public String history(Model model) {
        model.addAttribute("pageTitle", "Lịch sử tư vấn trực tuyến");
        model.addAttribute("historyApiUrl", "/api/chat/manager-history");
        return "common/chat-history";
    }
}
