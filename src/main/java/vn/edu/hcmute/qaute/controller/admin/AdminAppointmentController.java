package vn.edu.hcmute.qaute.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.util.PageUtil;
import vn.edu.hcmute.qaute.dto.request.admin.AdminAppointmentFilter;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.admin.AdminAppointmentService;

@Controller
public class AdminAppointmentController {
    private final AdminAppointmentService service;
    private final UserRepository users;

    public AdminAppointmentController(AdminAppointmentService service, UserRepository users) {
        this.service = service;
        this.users = users;
    }

    @GetMapping("/admin/appointments")
    public String list(@ModelAttribute("filter") AdminAppointmentFilter filter,
            @RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageUtil.of(page, 20, null, true, java.util.Set.of(), "createdAt");
        model.addAttribute("appointments", service.search(filter, pageable));
        model.addAttribute("paginationParams", params(filter));
        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("modalities", new Modality[] {Modality.ONLINE, Modality.IN_PERSON, Modality.BOTH});
        model.addAttribute("managers", users.searchByRoleAndKeyword(RoleCode.MANAGER, null));
        model.addAttribute("pageTitle", "Lịch hẹn toàn hệ thống");
        return "admin/appointments";
    }

    private static String params(AdminAppointmentFilter filter) {
        StringJoiner params = new StringJoiner("&amp;");
        add(params, "q", filter.getQ());
        add(params, "status", filter.getStatus());
        add(params, "modality", filter.getModality());
        add(params, "managerId", filter.getManagerId());
        add(params, "from", filter.getFrom());
        add(params, "to", filter.getTo());
        return params.toString();
    }

    private static void add(StringJoiner params, String key, Object value) {
        if (value != null && !value.toString().isBlank()) {
            params.add(key + "=" + URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
        }
    }
}
