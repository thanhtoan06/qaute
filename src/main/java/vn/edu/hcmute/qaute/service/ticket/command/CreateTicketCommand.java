package vn.edu.hcmute.qaute.service.ticket.command;

import java.util.List;

public record CreateTicketCommand(Long categoryId, String title, String descriptionHtml,
                                  boolean urgentRequested, String urgentReason,
                                  boolean allowPublicFaq, String submitToken, List<Long> assetIds) {
}
