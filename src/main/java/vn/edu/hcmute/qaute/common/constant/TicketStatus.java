package vn.edu.hcmute.qaute.common.constant;

// Các trạng thái trong vòng đời của yêu cầu tư vấn.
public enum TicketStatus {
    NEW("Mới"),
    ASSIGNED("Đã nhận"),
    IN_PROGRESS("Đang xử lý"),
    WAITING_STUDENT("Chờ sinh viên"),
    RESOLVED("Đã giải quyết"),
    CLOSED("Đã đóng"),
    CANCELLED("Đã hủy");

    private final String label;

    // Lưu nhãn hiển thị của trạng thái yêu cầu.
    TicketStatus(String label) {
        this.label = label;
    }

    // Trả về nhãn hiển thị của trạng thái yêu cầu.
    public String getLabel() {
        return label;
    }

    // Cho biết yêu cầu đã đóng hoặc bị hủy hay chưa.
    public boolean isFinished() {
        return this == CLOSED || this == CANCELLED;
    }

    // Cho biết yêu cầu đang được tiếp nhận hoặc xử lý hay chờ sinh viên.
    public boolean isWorking() {
        return this == ASSIGNED || this == IN_PROGRESS || this == WAITING_STUDENT;
    }
}
