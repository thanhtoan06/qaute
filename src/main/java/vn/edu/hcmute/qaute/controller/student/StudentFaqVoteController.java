package vn.edu.hcmute.qaute.controller.student;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.request.student.FaqVoteForm;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.knowledge.FaqFeedbackService;
import vn.edu.hcmute.qaute.service.knowledge.FaqFeedbackService.VoteResult;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StudentFaqVote.
@RestController
public class StudentFaqVoteController {
    private final FaqFeedbackService feedbacks;

    // Khởi tạo bộ điều khiển ghi nhận phản hồi FAQ.
    public StudentFaqVoteController(FaqFeedbackService feedbacks) {
        this.feedbacks = feedbacks;
    }

    @PostMapping("/student/faq/{id}/vote")
    // Ghi nhận lượt bình chọn FAQ của sinh viên.
    public ApiResponse<VoteResult> vote(@PathVariable Long id, @Valid @RequestBody FaqVoteForm form) {
        return ApiResponse.ok(feedbacks.vote(SecurityUtils.requireUserId(), id, form));
    }
}
