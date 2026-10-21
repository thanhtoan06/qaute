package vn.edu.hcmute.qaute.service.student;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.FaqSort;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.dto.request.FaqFilter;
import vn.edu.hcmute.qaute.dto.view.AppointmentView;
import vn.edu.hcmute.qaute.dto.view.FaqListItem;
import vn.edu.hcmute.qaute.dto.view.StudentDashboardView;
import vn.edu.hcmute.qaute.dto.view.TicketListItem;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.appointment.AppointmentService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Service
public class StudentDashboardServiceImpl implements StudentDashboardService {

    private static final List<TicketStatus> ACTION_STATUSES = List.of(
            TicketStatus.WAITING_STUDENT,
            TicketStatus.RESOLVED
    );

    private final StudentTicketQueryService ticketQueryService;
    private final TicketRepository ticketRepository;
    private final NotificationService notificationService;
    private final AppointmentService appointmentService;
    private final FaqQueryService faqQueryService;
    private final CategoryQueryService categoryQueryService;
    private final UserLookupService userLookupService;

    public StudentDashboardServiceImpl(StudentTicketQueryService ticketQueryService,
                                       TicketRepository ticketRepository,
                                       NotificationService notificationService,
                                       AppointmentService appointmentService,
                                       FaqQueryService faqQueryService,
                                       CategoryQueryService categoryQueryService,
                                       UserLookupService userLookupService) {
        this.ticketQueryService = ticketQueryService;
        this.ticketRepository = ticketRepository;
        this.notificationService = notificationService;
        this.appointmentService = appointmentService;
        this.faqQueryService = faqQueryService;
        this.categoryQueryService = categoryQueryService;
        this.userLookupService = userLookupService;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDashboardView getDashboard(Long studentId) {
        if (studentId == null) {
            return new StudentDashboardView(Map.of(), List.of(), null, 0L, List.of());
        }

        // 1. Ticket counts by status
        Map<TicketStatus, Long> ticketCounts = ticketQueryService.countByStatus(studentId);

        // 2. Tickets needing student action (WAITING_STUDENT and RESOLVED, max 5)
        List<Ticket> actionTickets = ticketRepository.findByStudentIdAndStatusInOrderByLastActivityAtDesc(
                studentId, ACTION_STATUSES, PageRequest.of(0, 5)
        );
        List<TicketListItem> needMyAction = mapToTicketListItems(actionTickets);

        // 3. Next upcoming appointment
        Page<AppointmentView> upcomingAppointments = appointmentService.listMine(
                studentId, "upcoming", PageRequest.of(0, 1)
        );
        AppointmentView nextAppointment = (upcomingAppointments != null && upcomingAppointments.hasContent())
                ? upcomingAppointments.getContent().get(0)
                : null;

        // 4. Unread notifications count
        long unreadNotifications = notificationService.unreadCount(studentId);

        // 5. Suggested FAQs (top viewed in categories student asked; fallback to topViewed)
        List<Long> askedCategoryIds = ticketRepository.findDistinctCategoryIdsByStudentId(studentId);
        List<FaqListItem> suggestedFaqs = List.of();
        if (askedCategoryIds != null && !askedCategoryIds.isEmpty()) {
            FaqFilter filter = FaqFilter.builder()
                    .categoryIds(askedCategoryIds)
                    .sort(FaqSort.MOST_VIEWED)
                    .build();
            Page<FaqListItem> faqPage = faqQueryService.list(filter, PageRequest.of(0, 5));
            if (faqPage != null) {
                suggestedFaqs = faqPage.getContent();
            }
        }

        if (suggestedFaqs.isEmpty()) {
            suggestedFaqs = faqQueryService.topViewed(-1, 5);
        }
        if (suggestedFaqs == null || suggestedFaqs.isEmpty()) {
            suggestedFaqs = faqQueryService.latest(5);
        }
        if (suggestedFaqs == null) {
            suggestedFaqs = List.of();
        }

        return new StudentDashboardView(
                ticketCounts != null ? ticketCounts : Map.of(),
                needMyAction,
                nextAppointment,
                unreadNotifications,
                suggestedFaqs
        );
    }

    private List<TicketListItem> mapToTicketListItems(List<Ticket> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            return List.of();
        }
        Set<Long> assigneeIds = tickets.stream()
                .map(Ticket::getCurrentAssigneeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, UserSummary> assignees = userLookupService.getSummaries(assigneeIds);

        return tickets.stream().map(t -> {
            String categoryName = categoryQueryService.getDisplayPath(t.getCategoryId());
            UserSummary assignee = t.getCurrentAssigneeId() != null
                    ? assignees.get(t.getCurrentAssigneeId())
                    : null;
            String assigneeName = assignee != null ? assignee.fullName() : null;
            return new TicketListItem(
                    t.getId(),
                    t.getCode(),
                    t.getTitle(),
                    categoryName,
                    t.getStatus(),
                    t.getPriority(),
                    assigneeName,
                    t.getCreatedAt(),
                    t.getLastActivityAt()
            );
        }).toList();
    }
}

