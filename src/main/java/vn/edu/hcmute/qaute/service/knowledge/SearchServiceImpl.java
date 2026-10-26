package vn.edu.hcmute.qaute.service.knowledge;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.FaqSort;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.util.HighlightUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.view.SearchResultItem;
import vn.edu.hcmute.qaute.entity.knowledge.SearchLog;
import vn.edu.hcmute.qaute.entity.knowledge.SearchSynonym;
import vn.edu.hcmute.qaute.repository.knowledge.FaqSearchProjection;
import vn.edu.hcmute.qaute.repository.knowledge.FaqSearchRepository;
import vn.edu.hcmute.qaute.repository.knowledge.SearchLogRepository;
import vn.edu.hcmute.qaute.repository.knowledge.SearchSynonymRepository;

// Xử lý nghiệp vụ Search trong hệ thống.
@Service
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final FaqSearchRepository faqSearchRepository;
    private final SearchSynonymRepository searchSynonymRepository;
    private final SearchLogRepository searchLogRepository;

    // Khởi tạo dịch vụ tìm kiếm FAQ và các kho dữ liệu liên quan.
    public SearchServiceImpl(FaqSearchRepository faqSearchRepository,
                             SearchSynonymRepository searchSynonymRepository,
                             SearchLogRepository searchLogRepository) {
        this.faqSearchRepository = faqSearchRepository;
        this.searchSynonymRepository = searchSynonymRepository;
        this.searchLogRepository = searchLogRepository;
    }

    @Override
    @Transactional
    // Tìm kiếm FAQ và ghi nhật ký truy vấn.
    public Page<SearchResultItem> search(String rawQuery,
                                         List<Long> categoryIds,
                                         String tagSlug,
                                         FaqSort sort,
                                         Pageable p,
                                         Long userId,
                                         String ipHash,
                                         String source) {
        if (rawQuery == null || rawQuery.isBlank()) {
            throw new BadRequestException("Hãy nhập ít nhất 2 ký tự");
        }

        String q = TextUtil.normalizeVi(rawQuery);
        if (q.length() < 2) {
            throw new BadRequestException("Hãy nhập ít nhất 2 ký tự");
        }
        if (q.length() > 100) {
            q = q.substring(0, 100).trim();
        }

        List<SearchSynonym> synonyms = searchSynonymRepository.findByActiveTrue();
        String synonymQuery = "";
        for (SearchSynonym syn : synonyms) {
            String termNorm = TextUtil.normalizeVi(syn.getTerm());
            String synNorm = TextUtil.normalizeVi(syn.getSynonym());
            if (!termNorm.isBlank() && !synNorm.isBlank()) {
                if (q.contains(termNorm) && !q.contains(synNorm)) {
                    synonymQuery = synNorm;
                    break;
                } else if (q.contains(synNorm) && !q.contains(termNorm)) {
                    synonymQuery = termNorm;
                    break;
                }
            }
        }

        FaqSort activeSort = sort != null ? sort : FaqSort.RELEVANCE;
        String sortParam = activeSort.name();

        Pageable unsorted = PageRequest.of(p.getPageNumber(), p.getPageSize());
        Page<FaqSearchProjection> projectionPage;

        if (categoryIds != null && !categoryIds.isEmpty()) {
            projectionPage = faqSearchRepository.searchWithCategories(
                    q, synonymQuery, categoryIds, tagSlug, sortParam, unsorted);
        } else {
            projectionPage = faqSearchRepository.searchWithoutCategories(
                    q, synonymQuery, tagSlug, sortParam, unsorted);
        }

        List<SearchResultItem> items = projectionPage.getContent().stream()
                .map(item -> new SearchResultItem(
                        item.getId(),
                        item.getTitle(),
                        item.getSlug(),
                        item.getSummary(),
                        item.getCategoryName(),
                        item.getViewCount() != null ? item.getViewCount() : 0L,
                        item.getHelpfulCount() != null ? item.getHelpfulCount() : 0,
                        item.getUpdatedAt(),
                        HighlightUtil.highlight(item.getTitle(), rawQuery),
                        HighlightUtil.highlight(
                                (item.getSummary() != null && !item.getSummary().isBlank())
                                        ? TextUtil.truncate(item.getSummary(), 200)
                                        : TextUtil.truncate(item.getBodyText(), 200),
                                rawQuery
                        ),
                        item.getScore() != null ? item.getScore() : 0.0
                ))
                .toList();

        Page<SearchResultItem> resultPage = new PageImpl<>(items, p, projectionPage.getTotalElements());

        try {
            SearchLog log = SearchLog.builder()
                    .userId(userId)
                    .queryNorm(q)
                    .resultCount((int) resultPage.getTotalElements())
                    .source(source != null ? source : "SEARCH")
                    .ipHash(ipHash)
                    .build();
            searchLogRepository.save(log);
        } catch (Exception ignored) {
        }

        return resultPage;
    }
}
