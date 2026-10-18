package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReplyForm {
    @NotBlank(message = "Vui lòng nhập nội dung trả lời")
    private String contentHtml;
    private List<Long> assetIds;
}
