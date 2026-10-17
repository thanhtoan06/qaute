package vn.edu.hcmute.qaute.repository.identity;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.identity.AccountActivationToken;

public interface AccountActivationTokenRepository
        extends JpaRepository<AccountActivationToken, Long> {

    Optional<AccountActivationToken> findByTokenHash(String tokenHash);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select token from AccountActivationToken token where token.tokenHash = :hash")
    Optional<AccountActivationToken> findByTokenHashForUpdate(@Param("hash") String hash);

    List<AccountActivationToken> findByUserIdAndUsedAtIsNull(Long userId);
}
