package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.dto.request.admin.BusinessHoursForm;
import vn.edu.hcmute.qaute.dto.request.admin.HolidayForm;
import vn.edu.hcmute.qaute.entity.governance.BusinessHour;
import vn.edu.hcmute.qaute.entity.governance.Holiday;
import vn.edu.hcmute.qaute.repository.governance.BusinessHourRepository;
import vn.edu.hcmute.qaute.repository.governance.HolidayRepository;
import vn.edu.hcmute.qaute.repository.governance.SlaPolicyRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@ExtendWith(MockitoExtension.class)
class AdminSlaCalendarTest {
    @Mock SlaPolicyRepository policies;
    @Mock CategoryQueryService categories;
    @Mock AuditService audit;
    @Mock BusinessHourRepository hours;
    @Mock HolidayRepository holidays;
    @InjectMocks AdminSlaService service;

    @Test
    void adjacentIntervalsAreAllowedAndReplaceAllRows() {
        BusinessHoursForm form = form(interval(1, "08:00", "12:00"),
                interval(1, "12:00", "17:00"));
        when(hours.findAllByOrderByDayOfWeekAscStartTimeAsc()).thenReturn(List.of());

        service.saveBusinessHours(form, 3L);

        verify(hours).deleteAllInBatch();
        verify(hours).saveAll(any());
        verify(audit).log(AuditActions.BUSINESS_HOURS_CHANGED, "BusinessHour", null,
                "", "1=08:00-12:00/true;1=12:00-17:00/true");
    }

    @Test
    void overlapAndFifthIntervalAreRejectedBeforeDelete() {
        BusinessHoursForm overlap = form(interval(1, "08:00", "12:00"),
                interval(1, "11:00", "15:00"));
        BadRequestException overlapError = assertThrows(BadRequestException.class,
                () -> service.saveBusinessHours(overlap, 3L));
        assertEquals("Các khoảng giờ cùng ngày không được chồng lấn",
                overlapError.getFieldErrors().get("day1"));

        BusinessHoursForm tooMany = form(interval(2, "08:00", "09:00"),
                interval(2, "09:00", "10:00"), interval(2, "10:00", "11:00"),
                interval(2, "11:00", "12:00"), interval(2, "12:00", "13:00"));
        BadRequestException limitError = assertThrows(BadRequestException.class,
                () -> service.saveBusinessHours(tooMany, 3L));
        assertEquals("Mỗi ngày tối đa 4 khoảng giờ", limitError.getFieldErrors().get("day2"));
        verify(hours, never()).deleteAllInBatch();
    }

    @Test
    void duplicateHolidayIsRejectedWithoutInsert() {
        HolidayForm form = new HolidayForm();
        form.setDate(LocalDate.of(2027, 1, 1));
        form.setName("Tết Dương lịch");
        when(holidays.existsByDate(form.getDate())).thenReturn(true);

        DuplicateException error = assertThrows(DuplicateException.class,
                () -> service.addHoliday(form, 3L));

        assertEquals("date", error.getField());
        verify(holidays, never()).saveAndFlush(any());
    }

    @Test
    void deletingHolidayIsAudited() {
        Holiday holiday = Holiday.builder().date(LocalDate.of(2027, 1, 1))
                .name("Tết Dương lịch").build();
        holiday.setId(9L);
        when(holidays.findById(9L)).thenReturn(Optional.of(holiday));

        service.deleteHoliday(9L, 3L);

        verify(holidays).delete(holiday);
        verify(audit).log(AuditActions.HOLIDAY_CHANGED, "Holiday", 9L,
                "2027-01-01 Tết Dương lịch", null);
    }

    private static BusinessHoursForm form(BusinessHoursForm.Interval... intervals) {
        BusinessHoursForm form = new BusinessHoursForm();
        form.setIntervals(new ArrayList<>(List.of(intervals)));
        return form;
    }

    private static BusinessHoursForm.Interval interval(int day, String start, String end) {
        BusinessHoursForm.Interval interval = new BusinessHoursForm.Interval();
        interval.setDayOfWeek(day);
        interval.setStart(start);
        interval.setEnd(end);
        interval.setActive(true);
        return interval;
    }
}
