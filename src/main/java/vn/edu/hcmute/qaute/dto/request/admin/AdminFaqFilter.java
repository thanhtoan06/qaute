package vn.edu.hcmute.qaute.dto.request.admin;

import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;

// Đóng gói dữ liệu đầu vào cho thao tác AdminFaqFilter.
@Getter
@Setter
public class AdminFaqFilter {
    private FaqStatus status;
    private Long categoryId;
    private Long authorId;
    private String q;
    private Boolean featured;
    private String sort;
}
