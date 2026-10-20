package vn.edu.hcmute.qaute.dto.view.report;

import java.util.List;
public record KnowledgeReport(long faqViews, double helpfulPercent, double noResultPercent, double searchToClickPercent, List<NamedCount> topNoResultQueries, List<NamedCount> lowHelpfulFaqs, double deflectionProxyPercent) {}
