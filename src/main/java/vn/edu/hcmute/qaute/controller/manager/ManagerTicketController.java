package vn.edu.hcmute.qaute.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.ManagerTicketDetailService;

@Controller
public class ManagerTicketController {
    private final ManagerTicketDetailService details;

    public ManagerTicketController(ManagerTicketDetailService details) {
        this.details = details;
    }

    @GetMapping("/manager/tickets/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("detail", details.getDetail(SecurityUtils.requireUserId(), id));
        model.addAttribute("pageTitle", "Không gian xử lý yêu cầu");
        return "manager/ticket-detail";
    }
}
