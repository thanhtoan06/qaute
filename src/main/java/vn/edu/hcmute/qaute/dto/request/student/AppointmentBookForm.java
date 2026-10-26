package vn.edu.hcmute.qaute.dto.request.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.hcmute.qaute.common.constant.Modality;

// Đóng gói dữ liệu đầu vào cho thao tác AppointmentBook.
@Getter
@Setter
@NoArgsConstructor
public class AppointmentBookForm {

    private Long categoryId;

    private Long managerId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private Modality modality;

    @NotNull(message = "Vui lòng chọn khung giờ")
    private Long slotId;

    @NotBlank(message = "Chủ đề tư vấn không được để trống")
    @Size(min = 10, max = 200, message = "Chủ đề phải từ 10 đến 200 ký tự")
    private String topic;

    @Size(max = 500, message = "Ghi chú không được quá 500 ký tự")
    private String note;

    private Long ticketId;
}
