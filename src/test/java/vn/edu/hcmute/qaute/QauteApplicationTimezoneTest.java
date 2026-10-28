package vn.edu.hcmute.qaute;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;

class QauteApplicationTimezoneTest {

    @Test
    void applicationSetsVietnamTimezoneBeforeUsingLocalDates() throws ClassNotFoundException {
        TimeZone original = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));

            Class.forName("vn.edu.hcmute.qaute.QauteApplication");

            assertEquals(ZoneId.of("Asia/Ho_Chi_Minh"), ZoneId.systemDefault());
            assertEquals(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")), LocalDate.now());
        } finally {
            TimeZone.setDefault(original);
        }
    }
}
