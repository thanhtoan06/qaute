package vn.edu.hcmute.qaute.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/** Giới hạn trang tại tầng service, kể cả khi caller không đi qua controller. */
public final class BoundedPages {

    /** Ngăn khởi tạo lớp tiện ích. */
    private BoundedPages() {
    }

    /** Giới hạn chỉ số trang và kích thước của yêu cầu phân trang. */
    public static Pageable of(Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            return of(0, 25);
        }
        return PageRequest.of(Math.min(pageable.getPageNumber(), 1_000_000),
                Math.min(pageable.getPageSize(), 100), pageable.getSort());
    }

    /** Tạo yêu cầu phân trang với chỉ số trang và kích thước bị giới hạn. */
    public static Pageable of(int page, int size) {
        return PageRequest.of(Math.max(0, Math.min(page, 1_000_000)), Math.max(1, Math.min(size, 100)));
    }
}
