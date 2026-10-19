package vn.edu.hcmute.qaute.dto.view;

import vn.edu.hcmute.qaute.common.constant.CannedResponseScope;

public record CannedResponseView(Long id, String title, String bodyHtml,
        CannedResponseScope scope, Long categoryId, String categoryPath,
        boolean active, int usageCount, boolean mine) { }
