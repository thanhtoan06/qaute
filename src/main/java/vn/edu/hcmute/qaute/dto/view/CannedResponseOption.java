package vn.edu.hcmute.qaute.dto.view;

import vn.edu.hcmute.qaute.common.constant.CannedResponseScope;

// Đóng gói dữ liệu CannedResponseOption để hiển thị hoặc trả về API.
public record CannedResponseOption(Long id, String title, CannedResponseScope scope) { }
