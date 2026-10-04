package vn.edu.hcmute.qaute.common.util;

import java.util.regex.Pattern;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public final class HtmlSanitizerUtil {

    private static final PolicyFactory POLICY = new HtmlPolicyBuilder()
            .allowElements("p", "br", "b", "strong", "i", "em", "u", "s",
                    "ul", "ol", "li", "blockquote", "pre", "code", "h3", "h4", "h5",
                    "table", "thead", "tbody", "tr", "th", "td", "a", "img", "span")
            .allowAttributes("href").onElements("a")
            .allowUrlProtocols("http", "https", "mailto")
            .requireRelNofollowOnLinks()
            .allowAttributes("target").matching(Pattern.compile("_blank")).onElements("a")
            .allowAttributes("src").matching(Pattern.compile("https://.*")).onElements("img")
            .allowAttributes("alt").onElements("img")
            .toFactory();

    private HtmlSanitizerUtil() {
    }

    public static String sanitize(String html) {
        return html == null ? "" : POLICY.sanitize(html);
    }
}
