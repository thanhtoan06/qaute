package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class HolidayForm {
    @NotNull(message = "Vui lòng chọn ngày nghỉ")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotBlank(message = "Vui lòng nhập tên ngày nghỉ")
    @Size(max = 150, message = "Tên ngày nghỉ tối đa 150 ký tự")
    private String name;
}
