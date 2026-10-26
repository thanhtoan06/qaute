package vn.edu.hcmute.qaute.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.StringJoiner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.dto.request.admin.AuditFilter;
import vn.edu.hcmute.qaute.dto.view.AuditLogView;
import vn.edu.hcmute.qaute.service.admin.AdminAuditService;
import vn.edu.hcmute.qaute.service.governance.AuditService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho AdminAudit.
@Controller
@RequestMapping("/admin/audit-logs")
public class AdminAuditController {
    public record ActionOption(String code, String label) {
    }

    private final AdminAuditService logs;
    private final AuditService audit;

    // Khởi tạo bộ điều khiển nhật ký kiểm toán.
    public AdminAuditController(AdminAuditService logs, AuditService audit) {
        this.logs = logs;
        this.audit = audit;
    }

    @GetMapping
    // Tìm kiếm và hiển thị nhật ký kiểm toán.
    public String list(@ModelAttribute("filter") AuditFilter filter,
                       @RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("logs", logs.search(filter, PageRequest.of(Math.max(0, page), 25,
                Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")))));
        model.addAttribute("actions", logs.actions().stream()
                .map(action -> new ActionOption(action, logs.actionLabel(action))).toList());
        model.addAttribute("paginationParams", params(filter));
        model.addAttribute("pageTitle", "Nhật ký hệ thống");
        return "admin/audit-logs";
    }

    @GetMapping("/{id}")
    @ResponseBody
    // Lấy chi tiết một mục nhật ký.
    public AuditLogView detail(@PathVariable Long id) {
        return logs.get(id);
    }

    @GetMapping("/export")
    // Xuất nhật ký kiểm toán thành tệp CSV.
    public ResponseEntity<byte[]> export(@ModelAttribute AuditFilter filter) {
        byte[] csv = logs.exportCsv(filter);
        audit.log(AuditActions.REPORT_EXPORT, "audit_log", null, null,
                "Xuất nhật ký hệ thống (tối đa 5000 dòng)");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("nhat-ky-audit-" + LocalDate.now() + ".csv", StandardCharsets.UTF_8).build());
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        return ResponseEntity.ok().headers(headers).body(csv);
    }

    // Tạo chuỗi tham số lọc cho phân trang.
    private static String params(AuditFilter filter) {
        StringJoiner values = new StringJoiner("&amp;");
        add(values, "actorId", filter.getActorId());
        add(values, "actorQ", filter.getActorQ());
        add(values, "action", filter.getAction());
        add(values, "entityType", filter.getEntityType());
        add(values, "entityId", filter.getEntityId());
        add(values, "from", filter.getFrom());
        add(values, "to", filter.getTo());
        return values.toString();
    }

    // Thêm tham số không rỗng vào chuỗi lọc.
    private static void add(StringJoiner values, String key, Object value) {
        if (value != null && !value.toString().isBlank()) {
            values.add(key + "=" + URLEncoder.encode(value.toString().trim(), StandardCharsets.UTF_8));
        }
    }
}
