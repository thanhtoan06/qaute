package vn.edu.hcmute.qaute.common.util;

import java.security.SecureRandom;

// Cung cấp tiện ích dùng chung cho OtpGenerator.
public final class OtpGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    /** Ngăn khởi tạo lớp tiện ích. */
    private OtpGenerator() {
    }

    /** Sinh mã OTP gồm đúng sáu chữ số. */
    public static String sixDigits() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}
