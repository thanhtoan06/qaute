package vn.edu.hcmute.qaute.dto.request.student;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác TicketAction.
@Getter
@Setter
public class TicketActionForm {

    @Size(max = 500, message = "Lý do không được vượt quá 500 ký tự")
    private String reason;
}
