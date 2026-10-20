package vn.edu.hcmute.qaute.controller.api;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.view.ChatQueueItem;
import vn.edu.hcmute.qaute.dto.view.ManagerChatSessionView;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.chat.ChatDeskService;

@RestController
@RequestMapping("/api/chat")
public class ManagerChatApiController {
    private final ChatDeskService desk;

    public ManagerChatApiController(ChatDeskService desk) { this.desk = desk; }

    @GetMapping("/queue")
    public ApiResponse<List<ChatQueueItem>> queue() {
        return ApiResponse.ok(desk.queueSnapshot(managerId()));
    }

    @GetMapping("/mine")
    public ApiResponse<List<ManagerChatSessionView>> mine() {
        return ApiResponse.ok(desk.mine(managerId()));
    }

    @GetMapping("/categories/{categoryId}/advisors")
    public ApiResponse<List<ManagerOption>> advisors(@PathVariable Long categoryId) {
        return ApiResponse.ok(desk.transferTargets(categoryId, managerId()));
    }

    private Long managerId() {
        Long id = SecurityUtils.requireUserId();
        if (!SecurityUtils.hasRole(RoleCode.MANAGER)) throw new ForbiddenException();
        return id;
    }
}
