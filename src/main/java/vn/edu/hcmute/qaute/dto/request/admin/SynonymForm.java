package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác Synonym.
@Getter
@Setter
public class SynonymForm {
    @NotBlank
    @Size(min = 2, max = 100, message = "Từ khóa phải từ 2 đến 100 ký tự")
    private String term;

    @NotBlank
    @Size(min = 2, max = 100, message = "Từ đồng nghĩa phải từ 2 đến 100 ký tự")
    private String synonym;
}
