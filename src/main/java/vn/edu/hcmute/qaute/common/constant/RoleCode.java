package vn.edu.hcmute.qaute.common.constant;

public enum RoleCode {
    STUDENT,
    MANAGER,
    ADMIN;

    public String authority() {
        return "ROLE_" + name();
    }
}
