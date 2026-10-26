package vn.edu.hcmute.qaute.common.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

// Cung cấp tiện ích dùng chung cho TokenUtil.
public final class TokenUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /** Ngăn khởi tạo lớp tiện ích. */
    private TokenUtil() {
    }

    /** Tạo token ngẫu nhiên có độ dài xác định theo byte. */
    public static String randomToken(int bytes) {
        if (bytes < 0) {
            throw new IllegalArgumentException("Số byte phải lớn hơn hoặc bằng 0");
        }
        byte[] value = new byte[bytes];
        SECURE_RANDOM.nextBytes(value);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    /** Tính mã băm SHA-256 và trả về dạng thập lục phân. */
    public static String sha256Hex(String value) {
        return hexDigest("SHA-256", value.getBytes(StandardCharsets.UTF_8));
    }

    /** Tạo chữ ký HMAC-SHA-256 và trả về dạng thập lục phân. */
    public static String hmacSha256Hex(String secret, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return toHex(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Không thể tạo chữ ký HMAC", exception);
        }
    }

    /** So sánh hai chuỗi theo thời gian không phụ thuộc nội dung. */
    public static boolean constantTimeEquals(String first, String second) {
        if (first == null || second == null) {
            return first == second;
        }
        return MessageDigest.isEqual(first.getBytes(StandardCharsets.UTF_8),
                second.getBytes(StandardCharsets.UTF_8));
    }

    /** Tính mã băm bằng thuật toán được chỉ định. */
    private static String hexDigest(String algorithm, byte[] value) {
        try {
            return toHex(MessageDigest.getInstance(algorithm).digest(value));
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Không thể băm dữ liệu", exception);
        }
    }

    /** Chuyển mảng byte thành chuỗi thập lục phân. */
    private static String toHex(byte[] value) {
        StringBuilder result = new StringBuilder(value.length * 2);
        for (byte item : value) {
            result.append(String.format("%02x", item));
        }
        return result.toString();
    }
}
