package vn.edu.hcmute.qaute.controller.manager;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.manager.ManagerDashboardService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho ManagerDashboard.
@Controller
public class ManagerDashboardController {
    private final ManagerDashboardService dashboards;

    // Khởi tạo bộ điều khiển trang tổng quan.
    public ManagerDashboardController(ManagerDashboardService dashboards) {
        this.dashboards = dashboards;
    }

    @GetMapping("/manager/dashboard")
    // Hiển thị trang tổng quan của quản lý.
    public String dashboard(@AuthenticationPrincipal QauteUserDetails me, Model model) {
        model.addAttribute("me", me);
        model.addAttribute("pageTitle", "Tổng quan");
        return "manager/dashboard";
    }

    @GetMapping("/api/manager/dashboard")
    @ResponseBody
    // Trả về dữ liệu tổng quan của quản lý hiện tại.
    public ApiResponse<ManagerDashboardService.Dashboard> data(@AuthenticationPrincipal QauteUserDetails me) {
        return ApiResponse.ok(dashboards.get(me.getId()));
    }
}
