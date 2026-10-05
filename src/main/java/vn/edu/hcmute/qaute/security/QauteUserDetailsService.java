package vn.edu.hcmute.qaute.security;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;

@Service
public class QauteUserDetailsService {

    private final UserRepository userRepository;

    public QauteUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public QauteUserDetails loadById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new DisabledException("Tài khoản không hoạt động");
        }
        return fromUser(user, null);
    }

    public QauteUserDetails fromUser(User user, String jti) {
        if (user == null || user.getId() == null || user.getRole() == null
                || user.getRole().getCode() == null) {
            throw new UsernameNotFoundException("Thông tin tài khoản không hợp lệ");
        }
        return new QauteUserDetails(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().getCode(),
                jti,
                user.getStatus() == UserStatus.ACTIVE);
    }
}
