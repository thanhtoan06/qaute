package vn.edu.hcmute.qaute.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.FaqSort;

// Đóng gói dữ liệu đầu vào cho thao tác FaqFilter.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqFilter {

    private List<Long> categoryIds;
    private String tagSlug;
    private String query;
    private FaqSort sort;
}

