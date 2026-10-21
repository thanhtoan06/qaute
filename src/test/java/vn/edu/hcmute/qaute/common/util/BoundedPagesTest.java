package vn.edu.hcmute.qaute.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class BoundedPagesTest {
    @Test
    void rejectsUnboundedAndOversizedPageRequestsAtServiceBoundary() {
        assertEquals(100, BoundedPages.of(PageRequest.of(2, 10000)).getPageSize());
        assertEquals(2, BoundedPages.of(PageRequest.of(2, 10000)).getPageNumber());
        assertEquals(25, BoundedPages.of(Pageable.unpaged()).getPageSize());
        assertEquals(0, BoundedPages.of(-1, 15).getPageNumber());
        assertEquals(1, BoundedPages.of(0, -1).getPageSize());
    }
}
