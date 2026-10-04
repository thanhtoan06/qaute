package vn.edu.hcmute.qaute.controller.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController implements ErrorController {

    @RequestMapping("/error")
    public String error(HttpServletRequest request, HttpServletResponse response, Model model) {
        Object statusAttribute = request.getAttribute("jakarta.servlet.error.status_code");
        int status = statusAttribute instanceof Integer ? (Integer) statusAttribute : 500;
        int viewStatus = switch (status) {
            case 400, 401, 403, 404, 429 -> status;
            default -> 500;
        };
        response.setStatus(status);
        model.addAttribute("traceId", request.getAttribute("traceId"));
        return "error/" + viewStatus;
    }
}
