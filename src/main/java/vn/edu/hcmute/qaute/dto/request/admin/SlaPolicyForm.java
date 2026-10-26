package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;

// Đóng gói dữ liệu đầu vào cho thao tác SlaPolicy.
@Getter
@Setter
public class SlaPolicyForm {
    private Long categoryId;

    @Valid
    @NotNull
    @Size(min = 4, max = 4)
    private List<Row> rows = new ArrayList<>();

    // Khởi tạo các dòng chính sách cho từng mức độ ưu tiên.
    public SlaPolicyForm() {
        for (TicketPriority priority : TicketPriority.values()) {
            Row row = new Row();
            row.setPriority(priority);
            row.setActive(true);
            rows.add(row);
        }
    }

    @Getter
    @Setter
    public static class Row {
        @NotNull
        private TicketPriority priority;

        @NotNull(message = "Vui lòng nhập hạn phản hồi đầu tiên")
        @DecimalMin(value = "0.5", message = "Hạn phản hồi tối thiểu 0,5 giờ")
        @DecimalMax(value = "720", message = "Hạn phản hồi tối đa 720 giờ")
        private BigDecimal firstResponseHours;

        @NotNull(message = "Vui lòng nhập hạn giải quyết")
        @DecimalMin(value = "0.5", message = "Hạn giải quyết tối thiểu 0,5 giờ")
        @DecimalMax(value = "2160", message = "Hạn giải quyết tối đa 2160 giờ")
        private BigDecimal resolutionHours;

        private boolean active;
    }
}
