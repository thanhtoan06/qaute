package vn.edu.hcmute.qaute.controller.guest;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.HtmlSanitizerUtil;
import vn.edu.hcmute.qaute.dto.view.AnnouncementView;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.governance.AnnouncementQueryService;

@Controller
public class AnnouncementController {

    private final AnnouncementQueryService announcementQueryService;

    public AnnouncementController(AnnouncementQueryService announcementQueryService) {
        this.announcementQueryService = announcementQueryService;
    }

    @GetMapping("/announcements")
    public String listAnnouncements(@RequestParam(defaultValue = "0") int page, Model model) {
        var p = announcementQueryService.findActivePage(AnnouncementAudience.ALL, page, 5);
        model.addAttribute("announcements", p.getContent());
        model.addAttribute("page", p);
        return "guest/announcements";
    }

    @GetMapping("/announcements/{id}")
    public String viewAnnouncement(@PathVariable Long id, Model model) {
        AnnouncementAudience audience = AnnouncementAudience.ALL;
        if (SecurityUtils.currentUser().isPresent()) {
            QauteUserDetails user = SecurityUtils.currentUser().get();
            if (user.getRoleCode() == RoleCode.STUDENT || user.getRoleCode() == RoleCode.MANAGER || user.getRoleCode() == RoleCode.ADMIN) {
                audience = AnnouncementAudience.STUDENT;
            }
        }

        AnnouncementView announcement = announcementQueryService.findPublished(id, audience)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông báo"));

        String sanitizedBodyHtml = HtmlSanitizerUtil.sanitize(announcement.bodyHtml());

        model.addAttribute("announcement", announcement);
        model.addAttribute("sanitizedBodyHtml", sanitizedBodyHtml);
        return "guest/announcement-detail";
    }
}

