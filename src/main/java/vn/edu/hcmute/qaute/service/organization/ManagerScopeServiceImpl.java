package vn.edu.hcmute.qaute.service.organization;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.organization.ManagerCategory;
import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;
import vn.edu.hcmute.qaute.repository.organization.ManagerCategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerProfileRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;

@Service
@Transactional(readOnly = true)
public class ManagerScopeServiceImpl implements ManagerScopeService {

    /** Ticket đang phụ trách, tính vào tải công việc của Manager. */
    private static final Set<TicketStatus> OPEN_ASSIGNEE_STATUSES =
            Set.of(TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS, TicketStatus.WAITING_STUDENT);

    /** Giới hạn ticket đồng thời khi Manager chưa có hồ sơ. */
    private static final int DEFAULT_MAX_CONCURRENT_TICKETS = 10;

    private final ManagerProfileRepository managerProfileRepository;
    private final ManagerCategoryRepository managerCategoryRepository;
    private final CategoryQueryService categoryQueryService;
    private final UserLookupService userLookupService;

    @PersistenceContext
    private EntityManager entityManager;

    public ManagerScopeServiceImpl(ManagerProfileRepository managerProfileRepository,
                                   ManagerCategoryRepository managerCategoryRepository,
                                   CategoryQueryService categoryQueryService,
                                   UserLookupService userLookupService) {
        this.managerProfileRepository = managerProfileRepository;
        this.managerCategoryRepository = managerCategoryRepository;
        this.categoryQueryService = categoryQueryService;
        this.userLookupService = userLookupService;
    }

    @Override
    public boolean isInScope(Long managerId, Long categoryId) {
        if (managerId == null || categoryId == null) {
            return false;
        }
        Set<Long> ancestors = new LinkedHashSet<>(categoryQueryService.getAncestorIdsIncludingSelf(categoryId));
        if (ancestors.isEmpty()) {
            return false;
        }
        for (ManagerCategory assignment : managerCategoryRepository.findByManagerId(managerId)) {
            if (ancestors.contains(assignment.getCategoryId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Long> getScopeCategoryIds(Long managerId) {
        if (managerId == null) {
            return List.of();
        }
        LinkedHashSet<Long> scope = new LinkedHashSet<>();
        for (ManagerCategory assignment : managerCategoryRepository.findByManagerId(managerId)) {
            scope.addAll(categoryQueryService.getDescendantIds(assignment.getCategoryId()));
        }
        return List.copyOf(scope);
    }

    @Override
    public List<Long> findManagerIdsInScope(Long categoryId) {
        if (categoryId == null) {
            return List.of();
        }
        Set<Long> ancestors = new LinkedHashSet<>(categoryQueryService.getAncestorIdsIncludingSelf(categoryId));
        if (ancestors.isEmpty()) {
            return List.of();
        }
        LinkedHashSet<Long> candidateIds = new LinkedHashSet<>();
        for (ManagerCategory assignment
                : managerCategoryRepository.findByCategoryIdIn(ancestors)) {
            candidateIds.add(assignment.getManagerId());
        }
        if (candidateIds.isEmpty()) {
            return List.of();
        }
        Map<Long, UserSummary> summaries = userLookupService.getSummaries(candidateIds);
        List<Long> result = new ArrayList<>();
        for (Long id : candidateIds) {
            UserSummary summary = summaries.get(id);
            if (summary != null
                    && summary.role() == RoleCode.MANAGER
                    && summary.status() == UserStatus.ACTIVE) {
                result.add(id);
            }
        }
        return result;
    }

    @Override
    public List<ManagerOption> findAdvisors(Long categoryId) {
        List<Long> managerIds = findManagerIdsInScope(categoryId);
        if (managerIds.isEmpty()) {
            return List.of();
        }
        Map<Long, UserSummary> summaries = userLookupService.getSummaries(managerIds);
        Map<Long, ManagerProfile> profiles = new HashMap<>();
        for (ManagerProfile profile : managerProfileRepository.findByUserIdIn(managerIds)) {
            profiles.put(profile.getUserId(), profile);
        }
        List<ManagerOption> advisors = new ArrayList<>(managerIds.size());
        for (Long id : managerIds) {
            UserSummary summary = summaries.get(id);
            if (summary == null) {
                continue;
            }
            ManagerProfile profile = profiles.get(id);
            advisors.add(new ManagerOption(
                    id,
                    summary.fullName(),
                    profile == null ? null : profile.getJobTitle(),
                    summary.avatarUrl(),
                    profile == null || profile.getAvailabilityStatus() == null
                            ? AvailabilityStatus.OFFLINE
                            : profile.getAvailabilityStatus()));
        }
        advisors.sort(Comparator
                .comparingInt((ManagerOption option) -> option.status().ordinal())
                .thenComparing(ManagerOption::fullName,
                        Comparator.nullsLast(String::compareToIgnoreCase)));
        return advisors;
    }

    @Override
    public boolean isAvailableForAssignment(Long managerId) {
        if (managerId == null) {
            return false;
        }
        UserSummary summary;
        try {
            summary = userLookupService.getSummary(managerId);
        } catch (NotFoundException e) {
            return false;
        }
        if (summary.role() != RoleCode.MANAGER || summary.status() != UserStatus.ACTIVE) {
            return false;
        }
        int limit = managerProfileRepository.findByUserId(managerId)
                .map(ManagerProfile::getMaxConcurrentTickets)
                .orElse(DEFAULT_MAX_CONCURRENT_TICKETS);
        return currentOpenTicketCount(managerId) < limit;
    }

    @Override
    public int currentOpenTicketCount(Long managerId) {
        if (managerId == null) {
            return 0;
        }
        try {
            Long count = entityManager.createQuery(
                    "select count(t) from Ticket t"
                            + " where t.currentAssigneeId = :id and t.status in :st",
                    Long.class)
                    .setParameter("id", managerId)
                    .setParameter("st", OPEN_ASSIGNEE_STATUSES)
                    .getSingleResult();
            return count == null ? 0 : count.intValue();
        } catch (IllegalArgumentException e) {
            // Entity Ticket của TV1 chưa có trong dự án: coi như chưa có ticket nào.
            return 0;
        }
    }
}
