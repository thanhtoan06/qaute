package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác BusinessHours.
@Getter
@Setter
public class BusinessHoursForm {
    @Valid
    @NotNull
    @Size(max = 28, message = "Tối đa 4 khoảng mỗi ngày")
    private List<Interval> intervals = new ArrayList<>();

    @Getter
    @Setter
    public static class Interval {
        @NotNull
        @Min(1)
        @Max(7)
        private Integer dayOfWeek;
        @NotBlank
        @Pattern(regexp = "(?:[01]\\d|2[0-3]):[0-5]\\d", message = "Giờ không hợp lệ")
        private String start;
        @NotBlank
        @Pattern(regexp = "(?:[01]\\d|2[0-3]):[0-5]\\d", message = "Giờ không hợp lệ")
        private String end;
        private boolean active = true;
    }
}
