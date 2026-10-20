package vn.edu.hcmute.qaute.controller.ws;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.request.chat.ChatSessionPayload;
import vn.edu.hcmute.qaute.service.chat.ChatAccessChecker;
import vn.edu.hcmute.qaute.service.chat.ChatDeskService;
import vn.edu.hcmute.qaute.service.chat.ChatEventPublisher;
import vn.edu.hcmute.qaute.service.chat.ChatMessagingService;

@ExtendWith(MockitoExtension.class)
class ChatStompControllerTest {
    @Mock ChatMessagingService messaging;
    @Mock ChatDeskService desk;
    @Mock ChatAccessChecker access;
    @Mock ChatEventPublisher publisher;
    @InjectMocks ChatStompController controller;
    private final Principal user = () -> "3";

    @Test
    void errorIsNeverPublishedToSessionForOutsider() {
        org.mockito.Mockito.doThrow(new BadRequestException("Lỗi"))
                .when(messaging).end(8L, 3L);
        controller.end(new ChatSessionPayload(8L), user);
        verify(publisher, never()).sendToSession(any(), any());
    }

    @Test
    void participantReceivesErrorEvent() {
        org.mockito.Mockito.doThrow(new BadRequestException("Lỗi"))
                .when(messaging).end(8L, 3L);
        when(access.isParticipant(8L, 3L)).thenReturn(true);
        controller.end(new ChatSessionPayload(8L), user);
        verify(publisher).sendToSession(eq(8L), any());
    }
}
