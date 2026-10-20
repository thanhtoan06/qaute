package vn.edu.hcmute.qaute.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerChatController {
    @GetMapping("/manager/chat")
    public String desk(Model model) {
        model.addAttribute("pageTitle", "Bàn tư vấn trực tuyến");
        return "manager/chat-desk";
    }
}
