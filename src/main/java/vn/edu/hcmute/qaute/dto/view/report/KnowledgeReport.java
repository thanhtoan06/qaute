package vn.edu.hcmute.qaute.dto.view.report;

import java.util.List;

// Đóng gói dữ liệu KnowledgeReport để hiển thị hoặc trả về API.
public record KnowledgeReport(
        long faqViews,
        double helpfulPercent,
        double noResultPercent,
        double searchToClickPercent,
        List<NamedCount> topNoResultQueries,
        List<NamedCount> lowHelpfulFaqs,
        double deflectionProxyPercent) {}
