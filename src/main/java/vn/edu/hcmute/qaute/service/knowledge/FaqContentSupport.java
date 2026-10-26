package vn.edu.hcmute.qaute.service.knowledge;

import vn.edu.hcmute.qaute.common.util.HtmlSanitizerUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;

// Xử lý nghiệp vụ FaqContentSupport trong hệ thống.
public final class FaqContentSupport {

    // Ngăn tạo đối tượng hỗ trợ nội dung FAQ.
    private FaqContentSupport() {
    }

    /**
     * Chuẩn bị dữ liệu nội dung FAQ trước khi lưu (sanitize HTML, strip HTML sang text thô,
     * chuẩn hóa tiếng Việt cho tiêu đề và nội dung để phục vụ tìm kiếm).
     * Thành viên khác sẽ gọi trước khi lưu FAQ.
     *
     * @param faq đối tượng Faq cần chuẩn hóa dữ liệu
     */
    // Chuẩn hóa nội dung FAQ trước khi lưu.
    public static void prepare(Faq faq) {
        if (faq == null) {
            return;
        }
        String sanitizedHtml = HtmlSanitizerUtil.sanitize(faq.getBodyHtml());
        faq.setBodyHtml(sanitizedHtml);

        String strippedText = TextUtil.stripHtml(sanitizedHtml);
        faq.setBodyText(strippedText);

        faq.setTitleNorm(TextUtil.normalizeVi(faq.getTitle()));
        faq.setBodyNorm(TextUtil.normalizeVi(strippedText));
    }
}
