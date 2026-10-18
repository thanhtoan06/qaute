package vn.edu.hcmute.qaute.repository.knowledge;

import java.time.LocalDateTime;

public interface FaqSearchProjection {

    Long getId();

    String getTitle();

    String getSlug();

    String getSummary();

    String getBodyText();

    String getCategoryName();

    Long getViewCount();

    Integer getHelpfulCount();

    LocalDateTime getUpdatedAt();

    Double getScore();
}

