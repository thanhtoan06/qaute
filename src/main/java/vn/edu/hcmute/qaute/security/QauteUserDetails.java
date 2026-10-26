package vn.edu.hcmute.qaute.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.edu.hcmute.qaute.common.constant.RoleCode;

// Hỗ trợ xác thực và kiểm soát truy cập qua QauteUserDetails.
public class QauteUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String fullName;
    private final RoleCode roleCode;
    private final String sessionJti;
    private final boolean enabled;

    // Khởi tạo thông tin xác thực của người dùng QAUTE.
    public QauteUserDetails(Long id, String email, String fullName, RoleCode roleCode,
                            String sessionJti, boolean enabled) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.roleCode = roleCode;
        this.sessionJti = sessionJti;
        this.enabled = enabled;
    }

    // Trả về quyền tương ứng với vai trò của người dùng.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roleCode.authority()));
    }

    // Trả về mật khẩu; thông tin này không được lưu trong principal.
    @Override
    public String getPassword() {
        return null;
    }

    // Trả về mã người dùng dưới dạng tên đăng nhập.
    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    // Cho biết tài khoản còn hiệu lực hay không.
    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    // Cho biết tài khoản có bị khóa hay không.
    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    // Cho biết thông tin xác thực còn hiệu lực hay không.
    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    // Cho biết tài khoản có được kích hoạt hay không.
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Trả về mã định danh người dùng.
    public Long getId() {
        return id;
    }

    // Trả về email người dùng.
    public String getEmail() {
        return email;
    }

    // Trả về họ tên người dùng.
    public String getFullName() {
        return fullName;
    }

    // Trả về mã vai trò người dùng.
    public RoleCode getRoleCode() {
        return roleCode;
    }

    // Trả về mã định danh phiên đăng nhập.
    public String getSessionJti() {
        return sessionJti;
    }
}
