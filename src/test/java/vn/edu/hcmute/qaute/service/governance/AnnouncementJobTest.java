package vn.edu.hcmute.qaute.service.governance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.common.constant.AnnouncementStatus;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.entity.governance.Announcement;
import vn.edu.hcmute.qaute.repository.governance.AnnouncementRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.notification.NotificationService;

@ExtendWith(MockitoExtension.class)
class AnnouncementJobTest {
    @Mock AnnouncementRepository announcements;
    @Mock UserRepository users;
    @Mock NotificationService notifications;
    @InjectMocks AnnouncementJob.Publisher publisher;

    @Test
    void autoPublishesAndNotifiesOnlySelectedAudience() {
        Announcement a = new Announcement();
        a.setId(7L);
        a.setTitle("Lịch học mới");
        a.setSummary("Thông tin lịch học mới");
        a.setStatus(AnnouncementStatus.DRAFT);
        a.setAudience(AnnouncementAudience.STUDENT);
        a.setAutoPublish(true);
        a.setNotifyOnPublish(true);
        a.setPublishAt(LocalDateTime.now().minusMinutes(1));
        when(announcements.findByStatusAndPublishAtLessThanEqual(eq(AnnouncementStatus.DRAFT), any()))
                .thenReturn(List.of(a));
        when(announcements.findByStatusAndPublishAtLessThanEqual(eq(AnnouncementStatus.PUBLISHED), any()))
                .thenReturn(List.of(a));
        when(users.findIdsByRoleCodeAndStatus(RoleCode.STUDENT, UserStatus.ACTIVE)).thenReturn(List.of(11L));

        publisher.process();

        assertEquals(AnnouncementStatus.PUBLISHED, a.getStatus());
        assertTrue(a.isNotified());
        verify(notifications).notifyUsers(Set.of(11L), NotificationType.ANNOUNCEMENT_PUBLISHED,
                a.getTitle(), a.getSummary(), "/announcements/7", "announcement", 7L);
    }

    @Test
    void expiresWithoutSendingLateNotification() {
        Announcement a = new Announcement();
        a.setId(8L);
        a.setStatus(AnnouncementStatus.PUBLISHED);
        a.setNotifyOnPublish(true);
        a.setExpiresAt(LocalDateTime.now().minusSeconds(1));
        when(announcements.findByStatusAndPublishAtLessThanEqual(eq(AnnouncementStatus.DRAFT), any()))
                .thenReturn(List.of());
        when(announcements.findByStatusAndPublishAtLessThanEqual(eq(AnnouncementStatus.PUBLISHED), any()))
                .thenReturn(List.of(a));
        when(announcements.findByStatusAndExpiresAtLessThanEqual(eq(AnnouncementStatus.PUBLISHED), any()))
                .thenReturn(List.of());

        publisher.process();

        assertEquals(AnnouncementStatus.EXPIRED, a.getStatus());
        verifyNoInteractions(notifications);
    }
}
