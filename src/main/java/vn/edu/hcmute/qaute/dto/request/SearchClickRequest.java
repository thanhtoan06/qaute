package vn.edu.hcmute.qaute.dto.request;

public record SearchClickRequest(
        String query,
        Long faqId,
        String source
) {
}

