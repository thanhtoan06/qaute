package vn.edu.hcmute.qaute.common.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class CsvUtilTest {
    @Test
    void parsesBomQuotesCommasAndMultilineCells() {
        var rows = CsvUtil.parse("\uFEFFmssv,fullName\r\n12345678,\"Nguyễn, \"\"An\"\"\nA\"\r\n");

        assertEquals(2, rows.size());
        assertEquals(2, rows.get(1).line());
        assertEquals("Nguyễn, \"An\"\nA", rows.get(1).cells().get(1));
    }

    @Test
    void rejectsBrokenQuotesAndProtectsSpreadsheetCells() {
        assertThrows(IllegalArgumentException.class, () -> CsvUtil.parse("a,\"unclosed"));
        assertEquals("'=1+1", CsvUtil.safeCell("=1+1"));
        assertEquals("'  @cmd", CsvUtil.safeCell("  @cmd"));
        assertEquals("\"'=1+1\"\r\n", CsvUtil.row("=1+1"));
        assertArrayEquals("\uFEFFx".getBytes(StandardCharsets.UTF_8), CsvUtil.withBom("x"));
    }
}
