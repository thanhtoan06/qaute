package vn.edu.hcmute.qaute.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/** Giới hạn trang tại tầng service, kể cả khi caller không đi qua controller. */
public final class BoundedPages {
    private BoundedPages() { }

    public static Pageable of(Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) return of(0, 25);
        return PageRequest.of(Math.min(pageable.getPageNumber(), 1_000_000),
                Math.min(pageable.getPageSize(), 100), pageable.getSort());
    }

    public static Pageable of(int page, int size) {
        return PageRequest.of(Math.max(0, Math.min(page, 1_000_000)), Math.max(1, Math.min(size, 100)));
    }

}
