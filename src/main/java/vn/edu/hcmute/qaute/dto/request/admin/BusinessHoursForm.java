package vn.edu.hcmute.qaute.dto.request.admin;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessHoursForm {
    private List<Interval> intervals = new ArrayList<>();

    @Getter
    @Setter
    public static class Interval {
        private Integer dayOfWeek;
        private String start;
        private String end;
        private boolean active = true;
    }
}
