package vn.edu.hcmute.qaute.dto.view;

import java.math.BigDecimal;
import java.util.List;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;

public record SlaPolicyMatrix(List<PolicyRow> defaults, List<CategoryOverride> overrides) {
    public record PolicyRow(TicketPriority priority, BigDecimal firstResponseHours,
                            BigDecimal resolutionHours, boolean active) {
    }

    public record CategoryOverride(Long categoryId, String categoryName, List<PolicyRow> rows) {
    }
}
