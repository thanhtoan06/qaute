package vn.edu.hcmute.qaute.repository.identity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.identity.AccountActivationToken;

public interface AccountActivationTokenRepository
        extends JpaRepository<AccountActivationToken, Long> {

    Optional<AccountActivationToken> findByTokenHash(String tokenHash);

    List<AccountActivationToken> findByUserIdAndUsedAtIsNull(Long userId);
}
