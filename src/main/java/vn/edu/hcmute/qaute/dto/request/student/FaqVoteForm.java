package vn.edu.hcmute.qaute.dto.request.student;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqVoteForm {

    private boolean helpful;

    @Pattern(regexp = "OUTDATED|UNCLEAR|NOT_ANSWER|OTHER", message = "Lý do đánh giá không hợp lệ")
    private String reasonCode;

    @Size(max = 500, message = "Nhận xét không được vượt quá 500 ký tự")
    private String comment;
}
