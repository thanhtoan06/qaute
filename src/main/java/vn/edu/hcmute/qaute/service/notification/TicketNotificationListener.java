package vn.edu.hcmute.qaute.service.notification;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.event.TicketDomainEvent;

@Component
public class TicketNotificationListener {

    private final TicketRepository ticketRepository;
    private final NotificationService notificationService;
    private final UserLookupService userLookupService;
    private final ManagerScopeService managerScope;
    private final UserRepository userRepository;

    public TicketNotificationListener(TicketRepository ticketRepository,
                                      NotificationService notificationService,
                                      UserLookupService userLookupService,
                                      ManagerScopeService managerScope,
                                      UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.notificationService = notificationService;
        this.userLookupService = userLookupService;
        this.managerScope = managerScope;
        this.userRepository = userRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onTicketDomainEvent(TicketDomainEvent event) {
        if (event == null || event.ticketId() == null || event.type() == null) {
            return;
        }
        Ticket ticket = ticketRepository.findById(event.ticketId()).orElse(null);
        if (ticket == null) {
            return;
        }

        String code = ticket.getCode();
        String title = ticket.getTitle();

        switch (event.type()) {
            case CREATED -> {
                notifyUser(ticket.getStudentId(), NotificationType.TICKET_CREATED,
                        "Yêu cầu [#" + code + "] đã được tạo thành công",
                        "Yêu cầu \"" + title + "\" của bạn đã được ghi nhận và đang chờ tiếp nhận.", ticket);
                if (ticket.getCurrentAssigneeId() == null) {
                    List<Long> managers = managerScope.findManagerIdsInScope(ticket.getCategoryId());
                    notifyUsers(managers, NotificationType.TICKET_NEW_IN_QUEUE,
                            "Có yêu cầu mới [#" + code + "] trong hàng đợi",
                            "Yêu cầu mới \"" + title + "\" thuộc chuyên mục bạn phụ trách đang chờ tiếp nhận.", ticket);
                }
            }
            case ASSIGNED -> {
                notifyUser(ticket.getStudentId(), NotificationType.TICKET_ASSIGNED,
                        "Yêu cầu [#" + code + "] đã được tiếp nhận",
                        "Yêu cầu \"" + title + "\" đã được phân công cho tư vấn viên xử lý.", ticket);
                Long assigneeId = ticket.getCurrentAssigneeId();
                if (assigneeId != null && !assigneeId.equals(event.actorId())) {
                    notifyUser(assigneeId, NotificationType.TICKET_ASSIGNED,
                            "Bạn được giao xử lý yêu cầu [#" + code + "]",
                            "Yêu cầu \"" + title + "\" đã được giao cho bạn phụ trách.", ticket);
                }
            }
            case ADVISOR_REPLIED -> {
                notifyUser(ticket.getStudentId(), NotificationType.TICKET_ADVISOR_REPLIED,
                        "Tư vấn viên đã phản hồi yêu cầu [#" + code + "]",
                        "Tư vấn viên đã gửi phản hồi mới cho yêu cầu \"" + title + "\".", ticket);
            }
            case STUDENT_REPLIED -> {
                Long assigneeId = ticket.getCurrentAssigneeId();
                if (assigneeId != null && !assigneeId.equals(event.actorId())) {
                    notifyUser(assigneeId, NotificationType.TICKET_STUDENT_REPLIED,
                            "Sinh viên đã phản hồi yêu cầu [#" + code + "]",
                            "Sinh viên đã gửi phản hồi mới cho yêu cầu \"" + title + "\".", ticket);
                }
            }
            case INFO_REQUESTED -> {
                notifyUser(ticket.getStudentId(), NotificationType.TICKET_INFO_REQUESTED,
                        "Cần bổ sung thông tin cho yêu cầu [#" + code + "]",
                        "Tư vấn viên yêu cầu bạn cung cấp thêm thông tin để xử lý yêu cầu \"" + title + "\".", ticket);
            }
            case RESOLVED -> {
                notifyUser(ticket.getStudentId(), NotificationType.TICKET_RESOLVED,
                        "Yêu cầu [#" + code + "] đã được giải quyết",
                        "Yêu cầu \"" + title + "\" đã được giải quyết. Vui lòng kiểm tra và xác nhận đóng yêu cầu.", ticket);
            }
            case AUTO_CLOSED -> {
                notifyUser(ticket.getStudentId(), NotificationType.TICKET_AUTO_CLOSED,
                        "Yêu cầu [#" + code + "] đã tự động đóng",
                        "Yêu cầu \"" + title + "\" đã được đóng tự động do hết thời gian chờ phản hồi.", ticket);
            }
            case REOPENED -> {
                if (ticket.getCurrentAssigneeId() != null) {
                    notifyUser(ticket.getCurrentAssigneeId(), NotificationType.TICKET_REOPENED,
                            "Yêu cầu [#" + code + "] đã được mở lại",
                            "Sinh viên đã mở lại yêu cầu \"" + title + "\".", ticket);
                } else {
                    List<Long> managers = managerScope.findManagerIdsInScope(ticket.getCategoryId());
                    notifyUsers(managers, NotificationType.TICKET_REOPENED,
                            "Yêu cầu [#" + code + "] đã được mở lại",
                            "Yêu cầu \"" + title + "\" đã được mở lại và đang ở hàng đợi.", ticket);
                }
            }
            case ESCALATED -> {
                String toManagerIdStr = event.extra() != null ? event.extra().get("toManagerId") : null;
                Long toManagerId = (toManagerIdStr != null && !toManagerIdStr.isBlank()) ? Long.valueOf(toManagerIdStr) : ticket.getCurrentAssigneeId();
                if (toManagerId != null) {
                    notifyUser(toManagerId, NotificationType.TICKET_ESCALATED,
                            "Yêu cầu [#" + code + "] được chuyển cấp cho bạn",
                            "Yêu cầu \"" + title + "\" đã được chuyển cấp cho bạn xử lý.", ticket);
                } else {
                    List<Long> managers = managerScope.findManagerIdsInScope(ticket.getCategoryId());
                    notifyUsers(managers, NotificationType.TICKET_ESCALATED,
                            "Yêu cầu [#" + code + "] được chuyển cấp tới chuyên mục của bạn",
                            "Yêu cầu \"" + title + "\" đã được chuyển cấp tới chuyên mục bạn phụ trách.", ticket);
                }
                notifyUser(ticket.getStudentId(), NotificationType.TICKET_ESCALATED,
                        "Yêu cầu [#" + code + "] đã được chuyển cấp",
                        "Yêu cầu \"" + title + "\" đã được chuyển cấp tới bộ phận phù hợp để tiếp tục xử lý.", ticket);
            }
            case FORCE_CLOSED -> {
                notifyUser(ticket.getStudentId(), NotificationType.TICKET_FORCE_CLOSED,
                        "Yêu cầu [#" + code + "] đã bị đóng bởi quản trị viên",
                        "Yêu cầu \"" + title + "\" đã bị đóng bởi quản trị viên.", ticket);
            }
            case RETURNED_TO_QUEUE -> {
                List<Long> managers = managerScope.findManagerIdsInScope(ticket.getCategoryId());
                notifyUsers(managers, NotificationType.TICKET_NEW_IN_QUEUE,
                        "Yêu cầu [#" + code + "] được trả về hàng đợi",
                        "Yêu cầu \"" + title + "\" đã được trả về hàng đợi và đang chờ tiếp nhận lại.", ticket);
            }
            case REMINDER_WAITING -> {
                notifyUser(ticket.getStudentId(), NotificationType.TICKET_REMINDER_WAITING,
                        "Nhắc nhở: Yêu cầu [#" + code + "] đang chờ bạn phản hồi",
                        "Yêu cầu \"" + title + "\" đang chờ thông tin từ bạn. Vui lòng phản hồi sớm để được hỗ trợ.", ticket);
            }
            case SLA_AT_RISK -> {
                if (ticket.getCurrentAssigneeId() != null) {
                    notifyUser(ticket.getCurrentAssigneeId(), NotificationType.SLA_AT_RISK,
                            "Cảnh báo: Yêu cầu [#" + code + "] sắp quá hạn xử lý",
                            "Yêu cầu \"" + title + "\" sắp chạm hạn chót xử lý theo cam kết chất lượng dịch vụ.", ticket);
                } else {
                    List<Long> managers = managerScope.findManagerIdsInScope(ticket.getCategoryId());
                    notifyUsers(managers, NotificationType.SLA_AT_RISK,
                            "Cảnh báo: Yêu cầu [#" + code + "] sắp quá hạn xử lý",
                            "Yêu cầu \"" + title + "\" sắp chạm hạn chót xử lý theo cam kết chất lượng dịch vụ.", ticket);
                }
            }
            case SLA_BREACHED -> {
                Set<Long> recipients = new HashSet<>();
                if (ticket.getCurrentAssigneeId() != null) {
                    recipients.add(ticket.getCurrentAssigneeId());
                } else {
                    recipients.addAll(managerScope.findManagerIdsInScope(ticket.getCategoryId()));
                }
                recipients.addAll(userRepository.findIdsByRoleCodeAndStatus(RoleCode.ADMIN, UserStatus.ACTIVE));
                notifyUsers(recipients, NotificationType.SLA_BREACHED,
                        "Khẩn cấp: Yêu cầu [#" + code + "] đã quá hạn xử lý",
                        "Yêu cầu \"" + title + "\" đã vượt quá hạn chót xử lý theo cam kết chất lượng dịch vụ.", ticket);
            }
            case REVIEWED_LOW -> {
                Set<Long> recipients = new HashSet<>();
                if (ticket.getCurrentAssigneeId() != null) {
                    recipients.add(ticket.getCurrentAssigneeId());
                }
                recipients.addAll(userRepository.findIdsByRoleCodeAndStatus(RoleCode.ADMIN, UserStatus.ACTIVE));
                notifyUsers(recipients, NotificationType.REVIEW_LOW_RATING,
                        "Yêu cầu [#" + code + "] nhận đánh giá thấp",
                        "Yêu cầu \"" + title + "\" vừa nhận được đánh giá không hài lòng từ sinh viên.", ticket);
            }
            default -> {
            }
        }
    }

    private void notifyUsers(Collection<Long> userIds, NotificationType type, String title, String body, Ticket ticket) {
        if (userIds == null || userIds.isEmpty()) return;
        Set<Long> uniqueIds = userIds.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (uniqueIds.isEmpty()) return;
        Map<Long, UserSummary> summaries = userLookupService.getSummaries(uniqueIds);
        for (Long uid : uniqueIds) {
            UserSummary summary = summaries.get(uid);
            String link = resolveLink(summary != null ? summary.role() : null, ticket.getId());
            notificationService.notifyUser(uid, type, title, body, link, "TICKET", ticket.getId());
        }
    }

    private void notifyUser(Long userId, NotificationType type, String title, String body, Ticket ticket) {
        if (userId == null) return;
        notifyUsers(List.of(userId), type, title, body, ticket);
    }

    private String resolveLink(RoleCode role, Long ticketId) {
        if (role == RoleCode.STUDENT) {
            return "/student/tickets/" + ticketId;
        } else if (role == RoleCode.MANAGER) {
            return "/manager/tickets/" + ticketId;
        } else if (role == RoleCode.ADMIN) {
            return "/admin/tickets/" + ticketId;
        }
        return "/student/tickets/" + ticketId;
    }
}

