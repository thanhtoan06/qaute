package vn.edu.hcmute.qaute.dto.request.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCreateForm {

    @NotNull(message = "Vui lòng chọn chuyên mục")
    private Long categoryId;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 10, max = 150, message = "Tiêu đề phải từ 10 đến 150 ký tự")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String descriptionHtml;

    private boolean urgentRequested;

    @Size(max = 500, message = "Lý do khẩn tối đa 500 ký tự")
    private String urgentReason;

    @Builder.Default
    private boolean allowPublicFaq = true;

    private String submitToken;

    @Builder.Default
    private List<Long> assetIds = new ArrayList<>();
}

