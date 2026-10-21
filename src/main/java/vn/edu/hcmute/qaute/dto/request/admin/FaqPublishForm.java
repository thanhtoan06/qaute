package vn.edu.hcmute.qaute.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqPublishForm {
    private boolean direct;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") private java.time.LocalDateTime publishAt;
}
