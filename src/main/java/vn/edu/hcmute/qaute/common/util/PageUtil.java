package vn.edu.hcmute.qaute.common.util;

import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageUtil {

    private PageUtil() {
    }

    public static Pageable of(int page, int size, String sortField, boolean desc,
                              Set<String> allowedSortFields, String defaultSortField) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 10 : Math.min(size, 100);
        String safeSortField = allowedSortFields.contains(sortField) ? sortField : defaultSortField;
        Sort.Direction direction = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(safePage, safeSize, Sort.by(direction, safeSortField));
    }
}
