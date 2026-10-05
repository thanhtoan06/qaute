package vn.edu.hcmute.qaute.common.util;

import java.security.SecureRandom;

public final class OtpGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private OtpGenerator() {
    }

    public static String sixDigits() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}
