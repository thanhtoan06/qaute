package vn.edu.hcmute.qaute.common.constant;

// Các mức độ ưu tiên của yêu cầu tư vấn.
public enum TicketPriority {
    LOW("Thấp"),
    NORMAL("Bình thường"),
    HIGH("Cao"),
    URGENT("Khẩn cấp");

    private final String label;

    // Lưu nhãn hiển thị của mức độ ưu tiên.
    TicketPriority(String label) {
        this.label = label;
    }

    // Trả về nhãn hiển thị của mức độ ưu tiên.
    public String getLabel() {
        return label;
    }
}
