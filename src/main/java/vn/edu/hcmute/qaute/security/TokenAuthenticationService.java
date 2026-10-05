package vn.edu.hcmute.qaute.security;

import java.util.Optional;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

@Service
public class TokenAuthenticationService {

    private final JwtService jwtService;
    private final AuthSessionService authSessionService;
    private final QauteUserDetailsService userDetailsService;

    public TokenAuthenticationService(JwtService jwtService, AuthSessionService authSessionService,
                                      QauteUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.authSessionService = authSessionService;
        this.userDetailsService = userDetailsService;
    }

    public Optional<QauteUserDetails> authenticateAccessToken(String jwt) {
        try {
            JwtClaimsView claims = jwtService.parse(jwt);
            if (claims.jti() == null || !authSessionService.isSessionActive(claims.jti())) {
                return Optional.empty();
            }
            QauteUserDetails user = userDetailsService.loadById(claims.userId());
            return Optional.of(new QauteUserDetails(user.getId(), user.getEmail(), user.getFullName(),
                    user.getRoleCode(), claims.jti(), user.isEnabled()));
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }
}
