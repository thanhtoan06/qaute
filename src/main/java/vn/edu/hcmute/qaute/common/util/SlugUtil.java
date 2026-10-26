package vn.edu.hcmute.qaute.common.util;

// Cung cấp tiện ích dùng chung cho SlugUtil.
public final class SlugUtil {

    /** Ngăn khởi tạo lớp tiện ích. */
    private SlugUtil() {
    }

    /** Tạo slug không dấu từ văn bản. */
    public static String slugify(String value) {
        String normalized = TextUtil.normalizeVi(value);
        String slug = normalized.replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
        return slug.isEmpty() ? "bai-viet" : slug;
    }
}
