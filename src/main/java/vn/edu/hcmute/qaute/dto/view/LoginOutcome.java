package vn.edu.hcmute.qaute.dto.view;

public record LoginOutcome(Kind kind, SessionTokens tokens, String email, String redirectRole,
                           long lockedMinutes) {

    public enum Kind {
        OK,
        NEED_VERIFY,
        BAD_CREDENTIALS,
        LOCKED_ADMIN,
        LOCKED_TEMP
    }
}
