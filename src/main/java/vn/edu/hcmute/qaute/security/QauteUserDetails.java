package vn.edu.hcmute.qaute.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.edu.hcmute.qaute.common.constant.RoleCode;

public class QauteUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String fullName;
    private final RoleCode roleCode;
    private final String sessionJti;
    private final boolean enabled;

    public QauteUserDetails(Long id, String email, String fullName, RoleCode roleCode,
                            String sessionJti, boolean enabled) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.roleCode = roleCode;
        this.sessionJti = sessionJti;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roleCode.authority()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public RoleCode getRoleCode() {
        return roleCode;
    }

    public String getSessionJti() {
        return sessionJti;
    }
}
