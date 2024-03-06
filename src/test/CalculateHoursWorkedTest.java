package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import mainCode.MainCode;

import java.text.ParseException;

public class CalculateHoursWorkedTest {
	
	@Test
    void testCalculateHoursWorkedFullDay() throws ParseException {
        String timeIn = "09:00";
        String timeOut = "18:00"; // 9 hours including lunch
        double expectedHours = 8; // 9 hours minus 1 hour lunch
        assertEquals(expectedHours, MainCode.calculateHoursWorked(timeIn, timeOut));
    }

}
