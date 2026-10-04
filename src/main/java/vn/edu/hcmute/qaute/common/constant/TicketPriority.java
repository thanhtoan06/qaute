package vn.edu.hcmute.qaute.common.constant;

public enum TicketPriority {
    LOW("Thấp"),
    NORMAL("Bình thường"),
    HIGH("Cao"),
    URGENT("Khẩn cấp");

    private final String label;

    TicketPriority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
