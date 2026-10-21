package vn.edu.hcmute.qaute.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.edu.hcmute.qaute.common.util.BoundedPages;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.view.NoResultQueryView;
import vn.edu.hcmute.qaute.entity.knowledge.SearchSynonym;
import vn.edu.hcmute.qaute.repository.knowledge.SearchLogRepository;
import vn.edu.hcmute.qaute.repository.knowledge.SearchSynonymRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;

@Service
@Transactional(readOnly = true)
public class AdminSynonymService {
    private final SearchSynonymRepository synonyms;
    private final SearchLogRepository logs;
    private final AuditService audit;

    public AdminSynonymService(SearchSynonymRepository synonyms, SearchLogRepository logs,
                               AuditService audit) {
        this.synonyms = synonyms;
        this.logs = logs;
        this.audit = audit;
    }

    public List<SearchSynonym> list() {
        return synonyms.findAllByOrderByTermAscSynonymAsc();
    }

    public Page<SearchSynonym> page(Pageable requested) {
        Pageable bounded = BoundedPages.of(requested);
        return synonyms.findAll(PageRequest.of(bounded.getPageNumber(), bounded.getPageSize(),
                Sort.by("term", "synonym", "id")));
    }

    public List<NoResultQueryView> noResultQueries() {
        return logs.findTopNoResultQueries(LocalDateTime.now().minusDays(30), PageRequest.of(0, 30))
                .stream().map(row -> new NoResultQueryView((String) row[0],
                        ((Number) row[1]).longValue())).toList();
    }

    @Transactional
    public void create(String term, String synonym) {
        String cleanedTerm = clean(term, "Từ khóa");
        String cleanedSynonym = clean(synonym, "Từ đồng nghĩa");
        if (TextUtil.normalizeVi(cleanedTerm).equals(TextUtil.normalizeVi(cleanedSynonym))) {
            throw new BadRequestException("Hai từ phải khác nhau");
        }
        if (synonyms.findByTermAndSynonym(cleanedTerm, cleanedSynonym).isPresent()) {
            throw new DuplicateException("synonym", "Cặp từ này đã tồn tại");
        }
        try {
            SearchSynonym saved = synonyms.saveAndFlush(SearchSynonym.builder()
                    .term(cleanedTerm).synonym(cleanedSynonym).active(true).build());
            audit.log(AuditActions.SYNONYM_CHANGED, "SearchSynonym", saved.getId(), null,
                    cleanedTerm + " ↔ " + cleanedSynonym);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateException("synonym", "Cặp từ này đã tồn tại");
        }
    }

    @Transactional
    public void toggle(Long id) {
        SearchSynonym pair = require(id);
        boolean before = pair.isActive();
        pair.setActive(!before);
        synonyms.save(pair);
        audit.log(AuditActions.SYNONYM_CHANGED, "SearchSynonym", id,
                Boolean.toString(before), Boolean.toString(pair.isActive()));
    }

    @Transactional
    public void delete(Long id) {
        SearchSynonym pair = require(id);
        synonyms.delete(pair);
        audit.log(AuditActions.SYNONYM_CHANGED, "SearchSynonym", id,
                pair.getTerm() + " ↔ " + pair.getSynonym(), null);
    }

    private SearchSynonym require(Long id) {
        return synonyms.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cặp từ đồng nghĩa"));
    }

    private static String clean(String value, String label) {
        String cleaned = value == null ? "" : value.trim().replaceAll("\\s+", " ");
        if (cleaned.length() < 2 || cleaned.length() > 100) {
            throw new BadRequestException(label + " phải từ 2 đến 100 ký tự");
        }
        return cleaned;
    }
}
