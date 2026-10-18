package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;

@Getter @Setter
public class PriorityForm {
    @NotNull(message = "Vui lòng chọn mức ưu tiên")
    private TicketPriority priority;
    @NotBlank(message = "Vui lòng nhập lý do")
    @Size(min = 10, max = 500, message = "Lý do phải từ 10 đến 500 ký tự")
    private String reason;
}
