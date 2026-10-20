package vn.edu.hcmute.qaute.service.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.dto.request.FaqFilter;
import vn.edu.hcmute.qaute.dto.view.FaqDetailView;
import vn.edu.hcmute.qaute.dto.view.FaqListItem;
import vn.edu.hcmute.qaute.dto.view.FaqSuggestion;
import vn.edu.hcmute.qaute.dto.view.TagView;

public interface FaqQueryService {

    Page<FaqListItem> list(FaqFilter filter, Pageable pageable);

    Page<FaqListItem> listBookmarked(Long userId, Pageable pageable);

    Set<Long> bookmarkedIds(Long userId, Collection<Long> faqIds);

    Map<Long, Boolean> myVotes(Long userId, Collection<Long> faqIds);

    FaqDetailView getPublishedBySlug(String slug);

    List<FaqListItem> topViewed(long minViews, int limit);

    List<FaqListItem> featured(int limit);

    List<FaqListItem> latest(int limit);

    List<FaqListItem> related(Long faqId, int limit);

    List<TagView> popularTags(int limit);

    long countPublished();

    Map<Long, Long> countPublishedByCategory();

    List<FaqSuggestion> suggest(String query, int limit);
}

