package vn.edu.hcmute.qaute.controller.guest;

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

// Tiếp nhận và điều phối các yêu cầu giao diện cho Announcement.
@Controller
public class AnnouncementController {

    private final AnnouncementQueryService announcementQueryService;

    // Khởi tạo controller xử lý thông báo công khai.
    public AnnouncementController(AnnouncementQueryService announcementQueryService) {
        this.announcementQueryService = announcementQueryService;
    }

    // Hiển thị danh sách thông báo đang hoạt động.
    @GetMapping("/announcements")
    public String listAnnouncements(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(required = false) String q,
                                     Model model) {
        if (q != null && !q.trim().isEmpty()) {
            // Có từ khóa tìm kiếm
            var searchResults = announcementQueryService.searchByQuery(q, AnnouncementAudience.ALL);
            model.addAttribute("announcements", searchResults);
            model.addAttribute("searchQuery", q);
            model.addAttribute("isSearch", true);
        } else {
            // Không có từ khóa, hiển thị danh sách bình thường
            var p = announcementQueryService.findActivePage(AnnouncementAudience.ALL, page, 5);
            model.addAttribute("announcements", p.getContent());
            model.addAttribute("page", p);
            model.addAttribute("isSearch", false);
        }
        return "guest/announcements";
    }

    // Hiển thị chi tiết thông báo phù hợp với đối tượng người dùng.
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
