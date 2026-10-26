package vn.edu.hcmute.qaute.common.constant;

// Các vai trò được gán cho tài khoản trong hệ thống.
public enum RoleCode {
    STUDENT,
    MANAGER,
    ADMIN;

    // Trả về mã quyền theo định dạng ROLE_<tên quyền>.
    public String authority() {
        return "ROLE_" + name();
    }
}
