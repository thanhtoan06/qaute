package vn.edu.hcmute.qaute.controller.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.view.ChatHistoryItem;
import vn.edu.hcmute.qaute.dto.view.ChatMessageView;
import vn.edu.hcmute.qaute.dto.view.ChatSessionView;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.chat.ChatService;

@RestController
@RequestMapping("/api/chat/sessions")
public class ChatApiController {
    private final ChatService chat;

    public ChatApiController(ChatService chat) {
        this.chat = chat;
    }

    public record StartRequest(@NotNull Long categoryId, @NotBlank @Size(min = 5, max = 500) String question) {
    }

    public record RateRequest(@NotNull @Min(1) @Max(5) Integer rating, @Size(max = 500) String comment) {
    }

    public record HistoryData(List<ChatHistoryItem> content, int totalPages) {
    }

    public record TicketData(Long ticketId) {
    }

    @GetMapping("/current")
    public ApiResponse<ChatSessionView> current() {
        return ApiResponse.ok(chat.current(requireStudent()).orElse(null));
    }

    @PostMapping
    public ApiResponse<ChatSessionView> start(@Valid @RequestBody StartRequest request) {
        return ApiResponse.ok(chat.start(requireStudent(), request.categoryId(), request.question()));
    }

    @PostMapping("/{id}/request-agent")
    public ApiResponse<ChatSessionView> requestAgent(@PathVariable Long id) {
        return ApiResponse.ok(chat.requestAgent(id, requireStudent()));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id) {
        chat.cancel(id, requireStudent());
        return ApiResponse.ok(null);
    }

    @GetMapping("/{id}/messages")
    public ApiResponse<List<ChatMessageView>> messages(@PathVariable Long id,
            @RequestParam(required = false) Long afterId) {
        return ApiResponse.ok(chat.messages(id, SecurityUtils.requireUserId(), afterId));
    }

    @PostMapping("/{id}/rate")
    public ApiResponse<Void> rate(@PathVariable Long id, @Valid @RequestBody RateRequest request) {
        chat.rate(id, requireStudent(), request.rating(), request.comment());
        return ApiResponse.ok(null);
    }

    @GetMapping("/history")
    public ApiResponse<HistoryData> history(@RequestParam(defaultValue = "0") int page) {
        Page<ChatHistoryItem> result = chat.history(requireStudent(), PageRequest.of(Math.max(0, page), 15));
        return ApiResponse.ok(new HistoryData(result.getContent(), result.getTotalPages()));
    }

    @PostMapping("/{id}/to-ticket")
    public ApiResponse<TicketData> toTicket(@PathVariable Long id) {
        return ApiResponse.ok(new TicketData(chat.createTicketFromChat(id, SecurityUtils.requireUserId())));
    }

    private Long requireStudent() {
        Long userId = SecurityUtils.requireUserId();
        if (!SecurityUtils.hasRole(RoleCode.STUDENT)) {
            throw new ForbiddenException("Chỉ sinh viên được thực hiện thao tác này");
        }
        return userId;
    }
}
