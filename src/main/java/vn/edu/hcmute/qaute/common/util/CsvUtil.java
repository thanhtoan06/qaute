package vn.edu.hcmute.qaute.common.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class CsvUtil {
    private CsvUtil() {
    }

    public record Row(int line, List<String> cells) {
    }

    public static List<Row> parse(String csv) {
        if (csv == null) {
            throw new IllegalArgumentException("Nội dung CSV không được để trống");
        }
        String input = csv.startsWith("\uFEFF") ? csv.substring(1) : csv;
        List<Row> rows = new ArrayList<>();
        List<String> cells = new ArrayList<>();
        StringBuilder cell = new StringBuilder();
        int line = 1;
        int rowLine = 1;
        boolean quoted = false;
        boolean closedQuote = false;
        boolean rowStarted = false;
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (quoted) {
                if (ch == '"') {
                    if (i + 1 < input.length() && input.charAt(i + 1) == '"') {
                        cell.append('"');
                        i++;
                    } else {
                        quoted = false;
                        closedQuote = true;
                    }
                } else if (ch == '\r' || ch == '\n') {
                    if (ch == '\r' && i + 1 < input.length() && input.charAt(i + 1) == '\n') {
                        i++;
                    }
                    cell.append('\n');
                    line++;
                } else {
                    cell.append(ch);
                }
                continue;
            }
            if (ch == '"') {
                if (closedQuote || cell.length() > 0) {
                    throw new IllegalArgumentException("CSV sai cú pháp ở dòng " + line);
                }
                quoted = true;
                rowStarted = true;
            } else if (ch == ',') {
                cells.add(cell.toString());
                cell.setLength(0);
                closedQuote = false;
                rowStarted = true;
            } else if (ch == '\r' || ch == '\n') {
                if (ch == '\r' && i + 1 < input.length() && input.charAt(i + 1) == '\n') {
                    i++;
                }
                cells.add(cell.toString());
                rows.add(new Row(rowLine, List.copyOf(cells)));
                cells.clear();
                cell.setLength(0);
                closedQuote = false;
                rowStarted = false;
                line++;
                rowLine = line;
            } else {
                if (closedQuote) {
                    throw new IllegalArgumentException("CSV sai cú pháp ở dòng " + line);
                }
                cell.append(ch);
                rowStarted = true;
            }
        }
        if (quoted) {
            throw new IllegalArgumentException("CSV thiếu dấu ngoặc kép đóng ở dòng " + rowLine);
        }
        if (rowStarted || !cells.isEmpty()) {
            cells.add(cell.toString());
            rows.add(new Row(rowLine, List.copyOf(cells)));
        }
        return rows;
    }

    public static String safeCell(String value) {
        if (value == null || value.isEmpty()) {
            return value == null ? "" : value;
        }
        String leadingTrimmed = value.stripLeading();
        if (!leadingTrimmed.isEmpty() && "=+-@".indexOf(leadingTrimmed.charAt(0)) >= 0) {
            return "'" + value;
        }
        return value;
    }

    public static String row(String... values) {
        List<String> encoded = new ArrayList<>(values.length);
        for (String value : values) {
            String safe = safeCell(value);
            encoded.add('"' + safe.replace("\"", "\"\"") + '"');
        }
        return String.join(",", encoded) + "\r\n";
    }

    public static byte[] withBom(String csv) {
        return ("\uFEFF" + (csv == null ? "" : csv)).getBytes(StandardCharsets.UTF_8);
    }
}
