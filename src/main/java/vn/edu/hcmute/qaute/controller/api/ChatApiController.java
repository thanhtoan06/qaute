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
import vn.edu.hcmute.qaute.dto.view.ChatConversationHistoryItem;
import vn.edu.hcmute.qaute.dto.view.ChatMessageView;
import vn.edu.hcmute.qaute.dto.view.ChatSessionView;
import vn.edu.hcmute.qaute.dto.view.ChatQueueItem;
import vn.edu.hcmute.qaute.dto.view.ChatStudentContextView;
import vn.edu.hcmute.qaute.dto.view.ManagerChatSessionView;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.chat.ChatDeskService;
import vn.edu.hcmute.qaute.service.chat.ChatService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho ChatApi.
@RestController
@RequestMapping("/api/chat")
public class ChatApiController {
    private final ChatService chat;
    private final ChatDeskService desk;

    // Khởi tạo bộ điều khiển trò chuyện.
    public ChatApiController(ChatService chat, ChatDeskService desk) {
        this.chat = chat;
        this.desk = desk;
    }

    public record StartRequest(@NotNull Long categoryId, @NotBlank @Size(min = 5, max = 500) String question) {
    }

    public record RateRequest(@NotNull @Min(1) @Max(5) Integer rating, @Size(max = 500) String comment) {
    }

    public record HistoryData(List<ChatHistoryItem> content, int totalPages) {
    }

    public record ConversationHistoryData(List<ChatConversationHistoryItem> content, int totalPages, int number) {
    }

    public record TicketData(Long ticketId) {
    }

    @GetMapping("/sessions/current")
    // Lấy phiên trò chuyện hiện tại của sinh viên.
    public ApiResponse<ChatSessionView> current() {
        return ApiResponse.ok(chat.current(requireStudent()).orElse(null));
    }

    @PostMapping("/sessions")
    // Bắt đầu phiên trò chuyện mới cho sinh viên.
    public ApiResponse<ChatSessionView> start(@Valid @RequestBody StartRequest request) {
        return ApiResponse.ok(chat.start(requireStudent(), request.categoryId(), request.question()));
    }

    @PostMapping("/sessions/{id}/request-agent")
    // Yêu cầu nhân viên tiếp nhận phiên trò chuyện.
    public ApiResponse<ChatSessionView> requestAgent(@PathVariable Long id) {
        return ApiResponse.ok(chat.requestAgent(id, requireStudent()));
    }

    @PostMapping("/sessions/{id}/cancel")
    // Hủy phiên trò chuyện của sinh viên.
    public ApiResponse<Void> cancel(@PathVariable Long id) {
        chat.cancel(id, requireStudent());
        return ApiResponse.ok(null);
    }

    @GetMapping("/sessions/{id}/messages")
    // Lấy các tin nhắn của phiên trò chuyện sau mã đã cho.
    public ApiResponse<List<ChatMessageView>> messages(@PathVariable Long id,
            @RequestParam(required = false) Long afterId) {
        return ApiResponse.ok(chat.messages(id, SecurityUtils.requireUserId(), afterId));
    }

    @PostMapping("/sessions/{id}/rate")
    // Gửi đánh giá cho phiên trò chuyện.
    public ApiResponse<Void> rate(@PathVariable Long id, @Valid @RequestBody RateRequest request) {
        chat.rate(id, requireStudent(), request.rating(), request.comment());
        return ApiResponse.ok(null);
    }

    @GetMapping("/sessions/history")
    // Lấy lịch sử phiên trò chuyện theo trang.
    public ApiResponse<HistoryData> history(@RequestParam(defaultValue = "0") int page) {
        Page<ChatHistoryItem> result = chat.history(requireStudent(), PageRequest.of(Math.max(0, page), 15));
        return ApiResponse.ok(new HistoryData(result.getContent(), result.getTotalPages()));
    }

    @GetMapping("/manager-history")
    public ApiResponse<ConversationHistoryData> managerHistory(@RequestParam(defaultValue = "0") int page) {
        Page<ChatConversationHistoryItem> result = chat.managerHistory(requireManager(),
                PageRequest.of(Math.max(0, page), 15));
        return ApiResponse.ok(new ConversationHistoryData(result.getContent(), result.getTotalPages(),
                result.getNumber()));
    }

    @GetMapping("/admin-history")
    public ApiResponse<ConversationHistoryData> adminHistory(@RequestParam(defaultValue = "0") int page) {
        if (!SecurityUtils.hasRole(RoleCode.ADMIN)) {
            throw new ForbiddenException();
        }
        SecurityUtils.requireUserId();
        Page<ChatConversationHistoryItem> result = chat.adminHistory(PageRequest.of(Math.max(0, page), 15));
        return ApiResponse.ok(new ConversationHistoryData(result.getContent(), result.getTotalPages(),
                result.getNumber()));
    }

    @PostMapping("/sessions/{id}/to-ticket")
    // Tạo phiếu hỗ trợ từ phiên trò chuyện.
    public ApiResponse<TicketData> toTicket(@PathVariable Long id) {
        return ApiResponse.ok(new TicketData(chat.createTicketFromChat(id, SecurityUtils.requireUserId())));
    }

    @GetMapping("/queue")
    // Lấy ảnh chụp hàng đợi trò chuyện của quản lý.
    public ApiResponse<List<ChatQueueItem>> queue(@RequestParam(defaultValue = "0") int page) {
        return ApiResponse.ok(desk.queueSnapshot(requireManager(), page));
    }

    @GetMapping("/mine")
    // Lấy các phiên trò chuyện đang phụ trách.
    public ApiResponse<List<ManagerChatSessionView>> mine() {
        return ApiResponse.ok(desk.mine(requireManager()));
    }

    @GetMapping("/sessions/{id}/student")
    // Lấy thông tin sinh viên trong phiên trò chuyện.
    public ApiResponse<ChatStudentContextView> student(@PathVariable Long id) {
        return ApiResponse.ok(desk.studentContext(id, requireManager()));
    }

    @GetMapping("/transfer-targets")
    // Lấy danh sách quản lý có thể nhận chuyển phiên.
    public ApiResponse<List<ManagerOption>> transferTargets(@RequestParam Long sessionId) {
        return ApiResponse.ok(desk.transferTargets(sessionId, requireManager()));
    }

    // Kiểm tra quyền quản lý và lấy mã người dùng.
    private Long requireManager() {
        Long userId = SecurityUtils.requireUserId();
        if (!SecurityUtils.hasRole(RoleCode.MANAGER)) throw new ForbiddenException();
        return userId;
    }

    // Kiểm tra quyền sinh viên và lấy mã người dùng.
    private Long requireStudent() {
        Long userId = SecurityUtils.requireUserId();
        if (!SecurityUtils.hasRole(RoleCode.STUDENT)) {
            throw new ForbiddenException("Chỉ sinh viên được thực hiện thao tác này");
        }
        return userId;
    }
}
