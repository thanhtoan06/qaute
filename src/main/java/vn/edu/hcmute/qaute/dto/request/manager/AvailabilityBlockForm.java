package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

// Đóng gói dữ liệu đầu vào cho thao tác AvailabilityBlock.
@Getter
@Setter
public class AvailabilityBlockForm {
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startAt;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endAt;

    @Size(max = 200)
    private String reason;
}
