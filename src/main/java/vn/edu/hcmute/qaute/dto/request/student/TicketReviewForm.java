package vn.edu.hcmute.qaute.dto.request.student;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác TicketReview.
@Getter
@Setter
public class TicketReviewForm {

    @Min(value = 1, message = "Vui lòng chọn số sao từ 1 đến 5")
    @Max(value = 5, message = "Vui lòng chọn số sao từ 1 đến 5")
    private int rating;

    @Size(max = 6, message = "Lý do đánh giá không hợp lệ")
    private List<@Pattern(regexp = "FAST|CLEAR|FRIENDLY|NOT_ON_POINT|SLOW|RUDE",
            message = "Lý do đánh giá không hợp lệ") String> reasonCodes;

    @Size(max = 500, message = "Nhận xét không được vượt quá 500 ký tự")
    private String comment;
}
