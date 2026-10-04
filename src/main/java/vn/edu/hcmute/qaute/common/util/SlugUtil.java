package vn.edu.hcmute.qaute.common.util;

public final class SlugUtil {

    private SlugUtil() {
    }

    public static String slugify(String value) {
        String normalized = TextUtil.normalizeVi(value);
        String slug = normalized.replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
        return slug.isEmpty() ? "bai-viet" : slug;
    }
}
