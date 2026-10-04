package vn.edu.hcmute.qaute.common.constant;

public enum TicketStatus {
    NEW("Mới"),
    ASSIGNED("Đã nhận"),
    IN_PROGRESS("Đang xử lý"),
    WAITING_STUDENT("Chờ sinh viên"),
    RESOLVED("Đã giải quyết"),
    CLOSED("Đã đóng"),
    CANCELLED("Đã hủy");

    private final String label;

    TicketStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean isFinished() {
        return this == CLOSED || this == CANCELLED;
    }

    public boolean isWorking() {
        return this == ASSIGNED || this == IN_PROGRESS || this == WAITING_STUDENT;
    }
}
