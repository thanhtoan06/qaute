package vn.edu.hcmute.qaute.service.governance;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import vn.edu.hcmute.qaute.common.web.ClientIpResolver;
import vn.edu.hcmute.qaute.entity.governance.AuditLog;
import vn.edu.hcmute.qaute.repository.governance.AuditLogRepository;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;

// Xử lý nghiệp vụ Audit trong hệ thống.
@Service
public class AuditServiceImpl implements AuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditServiceImpl.class);

    private static final int MAX_ACTION = 60;
    private static final int MAX_ENTITY_TYPE = 40;
    private static final int MAX_SUMMARY = 1000;
    private static final int MAX_IP = 64;
    private static final int MAX_USER_AGENT = 255;
    private static final int MAX_TRACE_ID = 16;

    private final AuditLogRepository auditLogRepository;

    /** Giao dịch riêng (REQUIRES_NEW): nhật ký vẫn được lưu dù giao dịch nghiệp vụ gọi vào sau đó bị hoàn tác. */
    private final TransactionTemplate requiresNewTx;

    // Khởi tạo bộ ghi audit với giao dịch độc lập.
    public AuditServiceImpl(AuditLogRepository auditLogRepository, PlatformTransactionManager transactionManager) {
        this.auditLogRepository = auditLogRepository;
        this.requiresNewTx = new TransactionTemplate(transactionManager);
        this.requiresNewTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    /**
     * Dùng TransactionTemplate thay cho @Transactional trên chính phương thức này vì try/catch nằm
     * BÊN NGOÀI giao dịch: bắt được cả lỗi xảy ra lúc commit và không bao giờ bị UnexpectedRollbackException
     * (nếu đặt @Transactional + catch bên trong thì hai trường hợp đó vẫn làm hỏng nghiệp vụ gọi vào).
     */
    // Ghi audit trong giao dịch riêng và bỏ qua lỗi ghi nhật ký.
    @Override
    public void log(String action, String entityType, Long entityId, String before, String after) {
        try {
            AuditLog entry = buildEntry(action, entityType, entityId, before, after);
            requiresNewTx.executeWithoutResult(status -> auditLogRepository.save(entry));
        } catch (Exception ex) {
            LOGGER.warn("Không ghi được nhật ký audit (action={}, entityType={}, entityId={}): {}",
                    action, entityType, entityId, ex.toString());
        }
    }

    // Tạo bản ghi audit từ thông tin hành động và ngữ cảnh hiện tại.
    private AuditLog buildEntry(String action, String entityType, Long entityId, String before, String after) {
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("action không được để trống");
        }
        Optional<QauteUserDetails> actor = SecurityUtils.currentUser();
        HttpServletRequest request = currentRequest();
        return AuditLog.builder()
                .actorId(actor.map(QauteUserDetails::getId).orElse(null))
                .actorRole(actor.map(AuditServiceImpl::roleName).orElse(null))
                .action(truncate(action, MAX_ACTION))
                .entityType(truncate(entityType, MAX_ENTITY_TYPE))
                .entityId(entityId)
                .summaryBefore(truncate(before, MAX_SUMMARY))
                .summaryAfter(truncate(after, MAX_SUMMARY))
                .ip(request == null ? null : truncate(ClientIpResolver.resolve(request), MAX_IP))
                .userAgent(request == null ? null : truncate(request.getHeader("User-Agent"), MAX_USER_AGENT))
                .traceId(truncate(MDC.get("traceId"), MAX_TRACE_ID))
                .build();
    }

    /** Request hiện tại nếu đang xử lý một yêu cầu web; null khi chạy trong job hoặc luồng nền. */
    // Lấy yêu cầu HTTP hiện tại nếu có.
    private static HttpServletRequest currentRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (attributes instanceof ServletRequestAttributes servletAttributes)
                ? servletAttributes.getRequest()
                : null;
    }

    // Lấy tên vai trò của người dùng hoặc null nếu chưa có vai trò.
    private static String roleName(QauteUserDetails user) {
        return user.getRoleCode() == null ? null : user.getRoleCode().name();
    }

    /** Cắt chuỗi tối đa max ký tự; không để lại nửa cặp ký tự thay thế (emoji) ở cuối vì MySQL sẽ từ chối. */
    // Giới hạn độ dài chuỗi mà không cắt đôi ký tự thay thế.
    static String truncate(String value, int max) {
        if (value == null || value.length() <= max) {
            return value;
        }
        int end = max;
        if (Character.isHighSurrogate(value.charAt(end - 1))) {
            end--;
        }
        return value.substring(0, end);
    }
}
