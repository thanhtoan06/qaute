package vn.edu.hcmute.qaute.service.knowledge;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.student.FaqVoteForm;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;
import vn.edu.hcmute.qaute.entity.knowledge.FaqFeedback;
import vn.edu.hcmute.qaute.repository.knowledge.FaqFeedbackRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqRepository;

@Service
@Transactional
public class FaqFeedbackService {
    private static final Set<String> NOT_HELPFUL_REASONS = Set.of("OUTDATED", "UNCLEAR", "NOT_ANSWER", "OTHER");

    private final FaqRepository faqs;
    private final FaqFeedbackRepository feedbacks;

    public FaqFeedbackService(FaqRepository faqs, FaqFeedbackRepository feedbacks) {
        this.faqs = faqs;
        this.feedbacks = feedbacks;
    }

    public VoteResult vote(Long userId, Long faqId, FaqVoteForm form) {
        if (userId == null || form == null) throw new BadRequestException("Dữ liệu bình chọn không hợp lệ");
        Faq faq = faqs.findById(faqId).filter(this::isPublishedNow)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));

        String reasonCode = normalizeReasonCode(form.getReasonCode());
        String comment = normalizeComment(form.getComment());
        if (!form.isHelpful()) {
            if (!NOT_HELPFUL_REASONS.contains(reasonCode)) {
                throw new BadRequestException("Vui lòng chọn lý do", Map.of("reasonCode", "Vui lòng chọn lý do"));
            }
            if (comment.length() < 20) {
                throw new BadRequestException("Vui lòng nhập nhận xét chi tiết", Map.of(
                        "comment", "Khi đánh giá chưa hữu ích, nhận xét cần ít nhất 20 ký tự"));
            }
        } else {
            reasonCode = null;
            comment = "";
        }

        FaqFeedback feedback = feedbacks.findByFaqIdAndUserId(faqId, userId)
                .orElseGet(() -> FaqFeedback.builder().faqId(faqId).userId(userId).build());
        feedback.setHelpful(form.isHelpful());
        feedback.setReasonCode(reasonCode);
        feedback.setComment(comment.isBlank() ? null : comment);
        feedbacks.saveAndFlush(feedback);

        int helpful = Math.toIntExact(feedbacks.countByFaqIdAndHelpful(faqId, true));
        int notHelpful = Math.toIntExact(feedbacks.countByFaqIdAndHelpful(faqId, false));
        faq.setHelpfulCount(helpful);
        faq.setNotHelpfulCount(notHelpful);
        faqs.save(faq);
        return new VoteResult(helpful, notHelpful, form.isHelpful());
    }

    private boolean isPublishedNow(Faq faq) {
        return faq.getStatus() == FaqStatus.PUBLISHED
                && (faq.getPublishAt() == null || !faq.getPublishAt().isAfter(LocalDateTime.now()));
    }

    private static String normalizeReasonCode(String reasonCode) {
        return reasonCode == null ? "" : reasonCode.trim().toUpperCase(Locale.ROOT);
    }

    private static String normalizeComment(String comment) {
        String value = comment == null ? "" : comment.trim();
        if (value.length() > 500) {
            throw new BadRequestException("Nhận xét không được vượt quá 500 ký tự", Map.of(
                    "comment", "Nhận xét không được vượt quá 500 ký tự"));
        }
        return value;
    }

    public record VoteResult(int helpful, int notHelpful, Boolean myVote) {
    }
}
