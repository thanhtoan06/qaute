package vn.edu.hcmute.qaute.dto.request.student;

import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketReplyForm {
    @NotBlank(message = "Nội dung phản hồi không được để trống")
    private String contentHtml;

    private List<Long> assetIds = new ArrayList<>();
}
