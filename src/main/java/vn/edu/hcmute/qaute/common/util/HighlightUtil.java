package vn.edu.hcmute.qaute.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.util.HtmlUtils;

public final class HighlightUtil {

    private HighlightUtil() {
    }

    public static String highlight(String text, String rawQuery) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String escaped = HtmlUtils.htmlEscape(text);
        if (rawQuery == null || rawQuery.isBlank()) {
            return escaped;
        }

        List<String> rawTokens = Arrays.stream(rawQuery.trim().split("\\s+"))
                .map(String::trim)
                .filter(token -> token.length() >= 2)
                .distinct()
                .toList();

        if (rawTokens.isEmpty() && rawQuery.trim().length() < 2) {
            return escaped;
        }

        List<String> tokens = new ArrayList<>(rawTokens);
        String fullTrimmed = rawQuery.trim();
        if (fullTrimmed.contains(" ") && fullTrimmed.length() >= 2 && !tokens.contains(fullTrimmed)) {
            tokens.add(fullTrimmed);
        }

        tokens.sort(Comparator.comparingInt(String::length).reversed());

        List<String> regexTokens = new ArrayList<>();
        for (String token : tokens) {
            regexTokens.add(buildAccentInsensitivePattern(token));
        }

        if (regexTokens.isEmpty()) {
            return escaped;
        }

        Pattern pattern = Pattern.compile("(" + String.join("|", regexTokens) + ")",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        Matcher matcher = pattern.matcher(escaped);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (isInsideEntity(escaped, start, end)) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group()));
            } else {
                matcher.appendReplacement(sb, "<mark>" + Matcher.quoteReplacement(matcher.group()) + "</mark>");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static boolean isInsideEntity(String s, int start, int end) {
        int lastAmp = s.lastIndexOf('&', start);
        if (lastAmp != -1) {
            int nextSemi = s.indexOf(';', lastAmp);
            return nextSemi != -1 && nextSemi >= end - 1;
        }
        return false;
    }

    private static String buildAccentInsensitivePattern(String word) {
        StringBuilder pattern = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = Character.toLowerCase(word.charAt(i));
            switch (c) {
                case 'a', 'à', 'á', 'ả', 'ã', 'ạ', 'â', 'ầ', 'ấ', 'ẩ', 'ẫ', 'ậ', 'ă', 'ằ', 'ắ', 'ẳ', 'ẵ', 'ặ' ->
                    pattern.append("[aàáảãạâầấẩẫậăằắẳẵặ]");
                case 'e', 'è', 'é', 'ẻ', 'ẽ', 'ẹ', 'ê', 'ề', 'ế', 'ể', 'ễ', 'ệ' ->
                    pattern.append("[eèéẻẽẹêềếểễệ]");
                case 'i', 'ì', 'í', 'ỉ', 'ĩ', 'ị' ->
                    pattern.append("[iìíỉĩị]");
                case 'o', 'ò', 'ó', 'ỏ', 'õ', 'ọ', 'ô', 'ồ', 'ố', 'ổ', 'ỗ', 'ộ', 'ơ', 'ờ', 'ớ', 'ở', 'ỡ', 'ợ' ->
                    pattern.append("[oòóỏõọôồốổỗộơờớởỡợ]");
                case 'u', 'ù', 'ú', 'ủ', 'ũ', 'ụ', 'ư', 'ừ', 'ứ', 'ử', 'ữ', 'ự' ->
                    pattern.append("[uùúủũụưừứửữự]");
                case 'y', 'ỳ', 'ý', 'ỷ', 'ỹ', 'ỵ' ->
                    pattern.append("[yỳýỷỹỵ]");
                case 'd', 'đ' ->
                    pattern.append("[dđ]");
                default ->
                    pattern.append(Pattern.quote(String.valueOf(c)));
            }
        }
        return pattern.toString();
    }
}

