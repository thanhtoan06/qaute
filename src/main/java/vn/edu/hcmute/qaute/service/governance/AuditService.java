package vn.edu.hcmute.qaute.service.governance;

/**
 * Ghi nhật ký hệ thống (audit). Việc ghi chạy trong giao dịch riêng và KHÔNG BAO GIỜ ném lỗi
 * ra ngoài, nên gọi AuditService không thể làm hỏng nghiệp vụ đang xử lý.
 */
public interface AuditService {

    /**
     * Ghi một dòng nhật ký.
     *
     * @param action     mã hành động, dùng hằng trong AuditActions (tối đa 60 ký tự)
     * @param entityType loại đối tượng bị tác động, ví dụ "User", "Ticket"; có thể null
     * @param entityId   id đối tượng bị tác động; có thể null
     * @param before     tóm tắt trạng thái trước (tối đa 1000 ký tự, dài hơn sẽ bị cắt); có thể null
     * @param after      tóm tắt trạng thái sau (tối đa 1000 ký tự, dài hơn sẽ bị cắt); có thể null
     */
    void log(String action, String entityType, Long entityId, String before, String after);
}
