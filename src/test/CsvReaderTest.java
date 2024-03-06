package test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import oldCode.CsvReader;

import java.util.List;

public class CsvReaderTest {
	
	@Test
    void testParseCsvLine() {
        String csvLine = "\"John, Doe\", \"Developer\", \"10000\"";
        List<String> parsedLine = CsvReader.parseCsvLine(csvLine);
        assertAll("Should parse CSV line with quotes correctly",
            () -> assertEquals("John, Doe", parsedLine.get(0)),
            () -> assertEquals("Developer", parsedLine.get(1)),
            () -> assertEquals("10000", parsedLine.get(2))
        );
    }

}
