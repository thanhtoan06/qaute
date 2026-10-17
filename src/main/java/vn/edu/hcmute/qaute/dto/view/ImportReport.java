package vn.edu.hcmute.qaute.dto.view;

import java.util.List;

public record ImportReport(int total, int ok, int failed, List<RowResult> rows) {
    public record RowResult(int line, String mssv, String email, boolean ok, String message) {
    }
}
