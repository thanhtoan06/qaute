package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác TicketNote.
@Getter
@Setter
public class TicketNoteForm {
    @NotBlank(message = "Nhập nội dung ghi chú")
    @Size(max = 30000, message = "Nội dung ghi chú quá dài")
    private String contentHtml;
}
