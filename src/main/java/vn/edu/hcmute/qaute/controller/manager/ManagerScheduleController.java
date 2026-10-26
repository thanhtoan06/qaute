package vn.edu.hcmute.qaute.controller.manager;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.manager.AvailabilityBlockForm;
import vn.edu.hcmute.qaute.dto.request.manager.AvailabilityRuleForm;
import vn.edu.hcmute.qaute.dto.view.ManagerScheduleRow;
import vn.edu.hcmute.qaute.dto.view.ManagerScheduleSlotView;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.ManagerScheduleService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho ManagerSchedule.
@Controller
@RequestMapping("/manager/schedule")
public class ManagerScheduleController {
    private final ManagerScheduleService schedule;

    // Khởi tạo bộ điều khiển lịch làm việc.
    public ManagerScheduleController(ManagerScheduleService schedule) {
        this.schedule = schedule;
    }

    @GetMapping
    // Hiển thị lịch tuần, quy tắc và các khung giờ bị chặn.
    public String index(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate week,
            Model model) {
        Long managerId = SecurityUtils.requireUserId();
        LocalDate monday = (week == null ? LocalDate.now() : week);
        monday = monday.minusDays(monday.getDayOfWeek().getValue() - 1L);
        List<LocalDate> days = monday.datesUntil(monday.plusDays(7)).toList();
        List<ManagerScheduleSlotView> slots = schedule.weekView(managerId, monday);
        int firstHour = Math.min(8, slots.stream().mapToInt(slot -> slot.startAt().getHour()).min().orElse(8));
        int lastHour = Math.max(18, slots.stream().mapToInt(slot -> slot.startAt().getHour()).max().orElse(18));
        List<ManagerScheduleRow> rows = new ArrayList<>();
        for (int hour = firstHour; hour <= lastHour; hour++) {
            List<List<ManagerScheduleSlotView>> cells = new ArrayList<>();
            for (LocalDate day : days) {
                int cellHour = hour;
                cells.add(slots.stream().filter(slot -> slot.startAt().toLocalDate().equals(day)
                        && slot.startAt().getHour() == cellHour).toList());
            }
            rows.add(new ManagerScheduleRow(hour, cells));
        }
        model.addAttribute("weekStart", monday);
        model.addAttribute("previousWeek", monday.minusWeeks(1));
        model.addAttribute("nextWeek", monday.plusWeeks(1));
        model.addAttribute("days", days);
        model.addAttribute("rows", rows);
        model.addAttribute("rules", schedule.rules(managerId));
        model.addAttribute("blocks", schedule.blocks(managerId));
        if (!model.containsAttribute("ruleForm")) model.addAttribute("ruleForm", new AvailabilityRuleForm());
        if (!model.containsAttribute("blockForm")) model.addAttribute("blockForm", new AvailabilityBlockForm());
        return "manager/schedule";
    }

    @PostMapping("/rules")
    // Lưu quy tắc tạo khung giờ hẹn.
    public String saveRule(@Valid @ModelAttribute("ruleForm") AvailabilityRuleForm form,
            BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("ruleForm", form);
            redirect.addFlashAttribute("ruleErrors", FormErrors.of(result));
            return "redirect:/manager/schedule";
        }
        try {
            schedule.saveRule(form, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", "Đã lưu quy tắc và tạo khung giờ hẹn");
        } catch (AppException exception) {
            redirect.addFlashAttribute("ruleForm", form);
            redirect.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/manager/schedule";
    }

    @PostMapping("/rules/{id}/delete")
    // Tắt quy tắc lịch và xử lý các khung giờ liên quan.
    public String deleteRule(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            int booked = schedule.deleteRule(id, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", booked == 0
                    ? "Đã tắt quy tắc và xóa các khung giờ còn trống"
                    : "Đã tắt quy tắc; còn " + booked + " lịch đã đặt được giữ nguyên");
        } catch (AppException exception) {
            redirect.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/manager/schedule";
    }

    @PostMapping("/blocks")
    // Chặn một khoảng thời gian trong lịch làm việc.
    public String addBlock(@Valid @ModelAttribute("blockForm") AvailabilityBlockForm form,
            BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("blockForm", form);
            redirect.addFlashAttribute("blockErrors", FormErrors.of(result));
            return "redirect:/manager/schedule";
        }
        try {
            schedule.addBlock(form, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", "Đã chặn khung giờ");
        } catch (AppException exception) {
            redirect.addFlashAttribute("blockForm", form);
            redirect.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/manager/schedule";
    }

    @PostMapping("/blocks/{id}/delete")
    // Bỏ chặn khoảng thời gian đã chọn.
    public String deleteBlock(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            schedule.deleteBlock(id, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", "Đã bỏ khung chặn");
        } catch (AppException exception) {
            redirect.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/manager/schedule";
    }
}
