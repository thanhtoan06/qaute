package vn.edu.hcmute.qaute.controller.student;

import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.student.ProfileForm;
import vn.edu.hcmute.qaute.dto.view.ProfileView;
import vn.edu.hcmute.qaute.repository.identity.FacultyRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.identity.StudentProfileService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StudentProfile.
@Controller
@RequestMapping("/student/profile")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;
    private final FacultyRepository facultyRepository;

    // Khởi tạo bộ điều khiển hồ sơ cùng dịch vụ và kho dữ liệu khoa.
    public StudentProfileController(StudentProfileService studentProfileService,
                                   FacultyRepository facultyRepository) {
        this.studentProfileService = studentProfileService;
        this.facultyRepository = facultyRepository;
    }

    @GetMapping
    // Hiển thị hồ sơ và điền biểu mẫu với thông tin hiện tại.
    public String profile(Model model) {
        Long userId = SecurityUtils.requireUserId();
        ProfileView profile = studentProfileService.getProfile(userId);
        model.addAttribute("profile", profile);
        model.addAttribute("faculties", facultyRepository.findByActiveTrueOrderByNameAsc());

        if (!model.containsAttribute("form")) {
            ProfileForm form = ProfileForm.builder()
                    .fullName(profile.fullName())
                    .phone(profile.phone())
                    .facultyId(profile.facultyId())
                    .classCode(profile.classCode())
                    .cohort(profile.cohort())
                    .program(profile.program())
                    .build();
            model.addAttribute("form", form);
        }
        return "student/profile";
    }

    @PostMapping
    // Kiểm tra và cập nhật thông tin hồ sơ sinh viên.
    public String updateProfile(@Valid @ModelAttribute("form") ProfileForm form,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.requireUserId();

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(bindingResult));
            model.addAttribute("profile", studentProfileService.getProfile(userId));
            model.addAttribute("faculties", facultyRepository.findByActiveTrueOrderByNameAsc());
            return "student/profile";
        }

        try {
            studentProfileService.updateProfile(userId, form);
            redirectAttributes.addFlashAttribute("flashSuccess", "Cập nhật thông tin hồ sơ thành công");
            return "redirect:/student/profile";
        } catch (BadRequestException e) {
            Map<String, String> errors = new LinkedHashMap<>(FormErrors.of(bindingResult));
            errors.putAll(e.getFieldErrors());
            model.addAttribute("errors", errors);
            model.addAttribute("profile", studentProfileService.getProfile(userId));
            model.addAttribute("faculties", facultyRepository.findByActiveTrueOrderByNameAsc());
            return "student/profile";
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/student/profile";
        }
    }

    @PostMapping("/avatar")
    // Cập nhật ảnh đại diện theo mã tài nguyên được gửi lên.
    public String updateAvatar(@RequestParam("assetId") Long assetId,
                               RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.requireUserId();
        try {
            studentProfileService.updateAvatar(userId, assetId);
            redirectAttributes.addFlashAttribute("flashSuccess", "Cập nhật ảnh đại diện thành công");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/student/profile";
    }
}

