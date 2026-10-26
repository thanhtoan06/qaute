package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO hiển thị câu hỏi thường gặp trên trang chủ guest.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestHomeFaqView {

    private Long id;
    private String categoryName;
    private String question;
    private String answer;
    private int displayOrder;
    private boolean isActive;
    private LocalDateTime updatedAt;
}
