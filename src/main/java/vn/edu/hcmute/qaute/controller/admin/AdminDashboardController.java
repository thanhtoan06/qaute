package vn.edu.hcmute.qaute.controller.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.admin.AdminDashboardService;

@Controller
public class AdminDashboardController {
    private final AdminDashboardService dashboards;

    public AdminDashboardController(AdminDashboardService dashboards) { this.dashboards = dashboards; }

    @GetMapping("/admin/dashboard")
    public String dashboard(@AuthenticationPrincipal QauteUserDetails me, Model model) {
        model.addAttribute("me", me);
        model.addAttribute("pageTitle", "Tổng quan");
        return "admin/dashboard";
    }

    @GetMapping("/api/admin/dashboard")
    @ResponseBody
    public ApiResponse<AdminDashboardService.Dashboard> data() {
        if (!SecurityUtils.hasRole(RoleCode.ADMIN)) throw new ForbiddenException();
        return ApiResponse.ok(dashboards.get());
    }
}
