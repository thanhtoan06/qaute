package vn.edu.hcmute.qaute.controller.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Controller
public class StudentChatController {

    private final CategoryQueryService categoryQueryService;

    public StudentChatController(CategoryQueryService categoryQueryService) {
        this.categoryQueryService = categoryQueryService;
    }

    @GetMapping("/student/chat")
    public String chat(Model model) {
        model.addAttribute("tree", categoryQueryService.getActiveTree());
        return "student/chat";
    }
}

