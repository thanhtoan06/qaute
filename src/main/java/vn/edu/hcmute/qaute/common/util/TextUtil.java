package vn.edu.hcmute.qaute.common.util;

import java.text.Normalizer;
import java.util.Locale;

// Cung cấp tiện ích dùng chung cho TextUtil.
public final class TextUtil {

    /** Ngăn khởi tạo lớp tiện ích. */
    private TextUtil() {
    }

    /** Loại bỏ thẻ HTML và chuẩn hóa khoảng trắng. */
    public static String stripHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("<[^>]*>", "")
                .replace("&nbsp;", " ")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replaceAll("\\s+", " ")
                .trim();
    }

    /** Chuẩn hóa văn bản tiếng Việt để so sánh không dấu. */
    public static String normalizeVi(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace('đ', 'd');
        return normalized.replaceAll("\\s+", " ").trim();
    }

    /** Thoát các ký tự đặc biệt trong biểu thức LIKE. */
    public static String escapeLike(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

    /** Cắt chuỗi về độ dài tối đa đã cho. */
    public static String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        if (maxLength <= 0) {
            return "";
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
