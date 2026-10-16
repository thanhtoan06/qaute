package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * Thông tin hồ sơ và phạm vi chuyên mục của một Manager trong khu vực quản trị.
 */
@Getter
@Setter
public class ManagerScopeForm {

    private Long managerId;

    @Size(max = 100, message = "Chức danh tối đa 100 ký tự")
    private String jobTitle;

    @Size(max = 30, message = "Mã nhân viên tối đa 30 ký tự")
    private String employeeCode;

    private Long departmentId;

    @Size(max = 500, message = "Giới thiệu tối đa 500 ký tự")
    private String bio;

    @Min(value = 1, message = "Số ticket đồng thời phải từ 1 đến 50")
    @Max(value = 50, message = "Số ticket đồng thời phải từ 1 đến 50")
    private int maxConcurrentTickets = 10;

    @Min(value = 1, message = "Số chat đồng thời phải từ 1 đến 10")
    @Max(value = 10, message = "Số chat đồng thời phải từ 1 đến 10")
    private int maxConcurrentChats = 3;

    private List<@NotNull(message = "Chuyên mục không hợp lệ") Long> categoryIds = new ArrayList<>();

    private List<@NotNull(message = "Chuyên mục chính không hợp lệ") Long> primaryCategoryIds = new ArrayList<>();

    /** Chuyên mục chính luôn phải là một trong các chuyên mục Manager được giao. */
    @AssertTrue(message = "Chuyên mục phụ trách chính phải thuộc danh sách chuyên mục phụ trách")
    public boolean isPrimaryCategoryIdsSubset() {
        if (primaryCategoryIds == null || primaryCategoryIds.isEmpty()) {
            return true;
        }
        if (categoryIds == null || categoryIds.isEmpty()) {
            return false;
        }
        Set<Long> assigned = new HashSet<>(categoryIds);
        return primaryCategoryIds.stream().allMatch(assigned::contains);
    }
}
