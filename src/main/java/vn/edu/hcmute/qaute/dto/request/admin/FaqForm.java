package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class FaqForm {
    private Long id;
    private Long version;

    @NotBlank(message = "Vui lòng nhập tiêu đề")
    @Size(min = 10, max = 200, message = "Tiêu đề phải từ 10 đến 200 ký tự")
    private String title;

    @Size(max = 500, message = "Tóm tắt tối đa 500 ký tự")
    private String summary;

    @NotNull(message = "Vui lòng chọn chuyên mục")
    private Long categoryId;

    @NotBlank(message = "Vui lòng nhập nội dung")
    private String bodyHtml;

    private String tags;
    private boolean featured;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime publishAt;

    private List<Long> imageAssetIds = new ArrayList<>();

    @Size(max = 255, message = "Ghi chú thay đổi tối đa 255 ký tự")
    private String changeNote;
}
