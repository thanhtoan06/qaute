package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagNameForm {
    @NotBlank(message = "Nhập tên thẻ") @Size(min=2, max=40, message="Tên thẻ phải từ 2 đến 40 ký tự") private String name;
}
