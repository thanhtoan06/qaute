package vn.edu.hcmute.qaute.controller.admin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.dto.request.admin.BusinessHoursForm;
import vn.edu.hcmute.qaute.dto.request.admin.HolidayForm;
import vn.edu.hcmute.qaute.dto.request.admin.SlaPolicyForm;
import vn.edu.hcmute.qaute.dto.view.CategoryNode;
import vn.edu.hcmute.qaute.dto.view.DepartmentNode;
import vn.edu.hcmute.qaute.dto.view.SlaCategoryOption;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminSlaService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho AdminSla.
@Controller
public class AdminSlaController {
    private final AdminSlaService sla;
    private final CategoryQueryService categories;

    // Khởi tạo bộ điều khiển chính sách SLA.
    public AdminSlaController(AdminSlaService sla, CategoryQueryService categories) {
        this.sla = sla;
        this.categories = categories;
    }

    @GetMapping("/admin/sla")
    // Hiển thị trang SLA theo thẻ được chọn.
    public String page(@RequestParam(required = false, defaultValue = "policy") String tab, Model model) {
        populate(model, sla.getForm(null), Map.of());
        model.addAttribute("activeTab", List.of("policy", "hours", "holidays").contains(tab) ? tab : "policy");
        return "admin/sla";
    }

    @GetMapping("/admin/sla/policy")
    // Hiển thị chính sách SLA cho chuyên mục.
    public String policy(@RequestParam(required = false) Long categoryId, Model model) {
        populate(model, sla.getForm(categoryId), Map.of());
        return "admin/sla";
    }

    @PostMapping("/admin/sla/policy")
    // Kiểm tra và lưu chính sách SLA.
    public String save(@Valid @ModelAttribute("form") SlaPolicyForm form,
                       BindingResult result, Model model, RedirectAttributes redirect) {
        Map<String, String> errors = new LinkedHashMap<>();
        result.getFieldErrors().forEach(error -> errors.putIfAbsent(error.getField(),
                error.getDefaultMessage()));
        if (result.hasGlobalErrors() || errors.containsKey("rows")
                || errors.keySet().stream().anyMatch(key -> key.endsWith(".priority"))) {
            model.addAttribute("formError", "Chính sách phải có đủ bốn mức ưu tiên");
        }
        if (!result.hasErrors()) {
            try {
                sla.save(form, SecurityUtils.requireUserId());
                redirect.addFlashAttribute("flashSuccess", "Đã lưu chính sách SLA");
                return form.getCategoryId() == null ? "redirect:/admin/sla"
                        : "redirect:/admin/sla/policy?categoryId=" + form.getCategoryId();
            } catch (BadRequestException e) {
                errors.putAll(e.getFieldErrors());
                if (e.getFieldErrors().isEmpty()) {
                    model.addAttribute("formError", e.getMessage());
                }
            }
        }
        populate(model, form, errors);
        return "admin/sla";
    }

    @PostMapping("/admin/sla/policy/{categoryId}/delete")
    // Xóa chính sách SLA ghi đè của chuyên mục.
    public String delete(@PathVariable Long categoryId, RedirectAttributes redirect) {
        try {
            sla.deleteOverride(categoryId, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", "Đã xóa chính sách ghi đè");
        } catch (BadRequestException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/sla";
    }

    @PostMapping("/admin/sla/hours")
    // Kiểm tra và lưu giờ làm việc.
    public String saveHours(@Valid @ModelAttribute("hoursForm") BusinessHoursForm hoursForm,
                            BindingResult result, Model model, RedirectAttributes redirect) {
        try {
            if (result.hasErrors()) throw new BadRequestException("Kiểm tra ngày và khoảng giờ làm việc");
            sla.saveBusinessHours(hoursForm, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", "Đã lưu giờ làm việc");
            return "redirect:/admin/sla?tab=hours";
        } catch (BadRequestException e) {
            populate(model, sla.getForm(null), e.getFieldErrors());
            model.addAttribute("hoursForm", hoursForm);
            model.addAttribute("activeTab", "hours");
            model.addAttribute("formError", e.getMessage());
            return "admin/sla";
        }
    }

    @PostMapping("/admin/sla/holidays")
    // Kiểm tra và thêm ngày nghỉ.
    public String addHoliday(@Valid @ModelAttribute("holidayForm") HolidayForm holidayForm,
                             BindingResult result, Model model, RedirectAttributes redirect) {
        Map<String, String> holidayErrors = new LinkedHashMap<>();
        result.getFieldErrors().forEach(error -> holidayErrors.putIfAbsent(error.getField(),
                error.getDefaultMessage()));
        if (!result.hasErrors()) {
            try {
                sla.addHoliday(holidayForm, SecurityUtils.requireUserId());
                redirect.addFlashAttribute("flashSuccess", "Đã thêm ngày nghỉ");
                return "redirect:/admin/sla?tab=holidays";
            } catch (DuplicateException e) {
                holidayErrors.put(e.getField(), e.getMessage());
            } catch (BadRequestException e) {
                holidayErrors.put("name", e.getMessage());
            }
        }
        populate(model, sla.getForm(null), Map.of());
        model.addAttribute("holidayForm", holidayForm);
        model.addAttribute("holidayErrors", holidayErrors);
        model.addAttribute("activeTab", "holidays");
        return "admin/sla";
    }

    @PostMapping("/admin/sla/holidays/{id}/delete")
    // Xóa ngày nghỉ đã chọn.
    public String deleteHoliday(@PathVariable Long id, RedirectAttributes redirect) {
        sla.deleteHoliday(id, SecurityUtils.requireUserId());
        redirect.addFlashAttribute("flashSuccess", "Đã xóa ngày nghỉ");
        return "redirect:/admin/sla?tab=holidays";
    }

    // Nạp chính sách, giờ làm và ngày nghỉ vào trang.
    private void populate(Model model, SlaPolicyForm form, Map<String, String> errors) {
        model.addAttribute("form", form);
        model.addAttribute("matrix", sla.matrix());
        model.addAttribute("categoryOptions", categoryOptions(form.getCategoryId()));
        model.addAttribute("errors", errors);
        BusinessHoursForm hoursForm = new BusinessHoursForm();
        sla.businessHours().forEach((day, dayHours) -> dayHours.forEach(hour -> {
            BusinessHoursForm.Interval interval = new BusinessHoursForm.Interval();
            interval.setDayOfWeek(day);
            interval.setStart(hour.getStartTime().toString());
            interval.setEnd(hour.getEndTime().toString());
            interval.setActive(hour.isActive());
            hoursForm.getIntervals().add(interval);
        }));
        model.addAttribute("hoursForm", hoursForm);
        model.addAttribute("holidays", sla.holidays());
        model.addAttribute("holidayForm", new HolidayForm());
        model.addAttribute("activeTab", "policy");
        model.addAttribute("pageTitle", "Chính sách SLA");
    }

    // Tạo danh sách chuyên mục có thể chọn cho SLA.
    private List<SlaCategoryOption> categoryOptions(Long selectedId) {
        List<SlaCategoryOption> options = new ArrayList<>();
        for (DepartmentNode department : categories.getActiveTree()) {
            for (CategoryNode root : department.categories()) {
                String rootLabel = department.name() + " › " + root.name();
                options.add(new SlaCategoryOption(root.id(), rootLabel));
                for (CategoryNode child : root.children()) {
                    options.add(new SlaCategoryOption(child.id(), rootLabel + " › " + child.name()));
                }
            }
        }
        if (selectedId != null && options.stream().noneMatch(option -> option.id().equals(selectedId))) {
            String label = categories.getDisplayPath(selectedId);
            options.add(new SlaCategoryOption(selectedId,
                    label.isBlank() ? "Chuyên mục #" + selectedId : label));
        }
        return List.copyOf(options);
    }
}
