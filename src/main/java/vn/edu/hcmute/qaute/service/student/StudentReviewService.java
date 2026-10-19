package vn.edu.hcmute.qaute.service.student;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.dto.request.student.TicketReviewForm;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.entity.ticket.TicketReview;
import vn.edu.hcmute.qaute.repository.ticket.TicketReviewRepository;
import vn.edu.hcmute.qaute.service.system.SettingsService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;
import vn.edu.hcmute.qaute.service.ticket.event.TicketEventPublisher;
import vn.edu.hcmute.qaute.service.ticket.event.TicketEventType;

@Service
@Transactional
public class StudentReviewService {
    private static final Set<String> ALLOWED_REASON_CODES = Set.of(
            "FAST", "CLEAR", "FRIENDLY", "NOT_ON_POINT", "SLOW", "RUDE");

    private final TicketAccessService access;
    private final TicketReviewRepository reviews;
    private final TicketEventPublisher events;
    private final SettingsService settings;

    public StudentReviewService(TicketAccessService access, TicketReviewRepository reviews,
                                TicketEventPublisher events, SettingsService settings) {
        this.access = access;
        this.reviews = reviews;
        this.events = events;
        this.settings = settings;
    }

    public void saveReview(Long studentId, Long ticketId, TicketReviewForm form) {
        Ticket ticket = access.requireViewable(ticketId, studentId, RoleCode.STUDENT);
        if (ticket.getStatus() != TicketStatus.RESOLVED && ticket.getStatus() != TicketStatus.CLOSED) {
            throw new InvalidTransitionException("Chỉ có thể đánh giá yêu cầu đã giải quyết hoặc đã đóng");
        }
        if (form == null) throw new BadRequestException("Dữ liệu đánh giá không hợp lệ");

        validateRating(form.getRating());
        List<String> reasonCodes = normalizeReasonCodes(form.getReasonCodes());
        String comment = normalizeComment(form.getComment());
        if (form.getRating() <= 2 && comment.length() < 20) {
            throw new BadRequestException("Vui lòng nhập nhận xét chi tiết", Map.of(
                    "comment", "Đánh giá từ 1 đến 2 sao cần nhận xét ít nhất 20 ký tự"));
        }

        TicketReview review = reviews.findByTicketId(ticketId).orElse(null);
        boolean isNew = review == null;
        int previousRating = isNew ? 0 : review.getRating();
        if (!isNew && review.getCreatedAt() != null && LocalDateTime.now().isAfter(review.getCreatedAt()
                .plusDays(settings.getInt(SettingKeys.TICKET_RATING_EDIT_DAYS)))) {
            throw new AppException(ErrorCode.ERR_LIMIT_EXCEEDED, "Đã quá thời hạn chỉnh sửa đánh giá");
        }
        if (isNew) review = TicketReview.builder().ticketId(ticketId).studentId(studentId).build();
        review.setRating(form.getRating());
        review.setReasonCodes(String.join(",", reasonCodes));
        review.setComment(comment.isEmpty() ? null : comment);
        reviews.save(review);

        if (form.getRating() <= 2 && (isNew || previousRating > 2)) {
            events.publish(TicketEventType.REVIEWED_LOW, ticket, studentId,
                    Map.of("rating", String.valueOf(form.getRating())));
        }
    }

    private static void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Vui lòng chọn số sao từ 1 đến 5", Map.of(
                    "rating", "Vui lòng chọn số sao từ 1 đến 5"));
        }
    }

    private static List<String> normalizeReasonCodes(List<String> source) {
        if (source == null || source.isEmpty()) return List.of();
        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        for (String code : source) {
            String value = code == null ? "" : code.trim().toUpperCase(Locale.ROOT);
            if (!ALLOWED_REASON_CODES.contains(value)) {
                throw new BadRequestException("Lý do đánh giá không hợp lệ", Map.of(
                        "reasonCodes", "Lý do đánh giá không hợp lệ"));
            }
            normalized.add(value);
        }
        return List.copyOf(normalized);
    }

    private static String normalizeComment(String comment) {
        String value = comment == null ? "" : comment.trim();
        if (value.length() > 500) {
            throw new BadRequestException("Nhận xét không được vượt quá 500 ký tự", Map.of(
                    "comment", "Nhận xét không được vượt quá 500 ký tự"));
        }
        return value;
    }
}
