package vn.edu.hcmute.qaute.dto.request.admin;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

// Đóng gói dữ liệu đầu vào cho thao tác FaqPublish.
@Getter
@Setter
public class FaqPublishForm {
    private boolean direct;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime publishAt;
}
