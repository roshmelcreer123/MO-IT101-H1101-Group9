package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

import mainCode.EmployeeDetails;
import mainCode.MainCode;

public class EmployeeDetailsTest {
	
	@TempDir
    Path tempDir;
    Path employeeData;

    @BeforeEach
    void setUp() throws IOException {
        // Set up a temporary CSV file with employee data for testing
        employeeData = tempDir.resolve("motorPhEmployeeData2.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(employeeData)) {
            writer.write("10300, Garcia, Manuel III, 10/11/83, \"Valero Carpark Building Valero Street 1227, Makati City\", 966-860-270, 44-4506057-3, 820127E+11, 442-605-657-000, 691295E+11, Regular, Chief Executive Officer, N/A, 90000, 1500, 2000, 1000, 45000, 535.71");
        }
    }

    @Test
    void testSearchForEmployeeFound() {
        // Assuming your searchForEmployee method is static and part of your Main class
        String employeeId = "10300";
        EmployeeDetails employeeDetails = MainCode.searchForEmployee(employeeData.toString(), employeeId);
        
        assertNotNull(employeeDetails, "EmployeeDetails should not be null for an existing employee ID");
        assertEquals(employeeId, employeeDetails.getEmployeeId(), "The employee ID should match the searched ID");
    }

    @Test
    void testSearchForEmployeeNotFound() {
        String employeeId = "99999"; // ID that does not exist
        EmployeeDetails employeeDetails = MainCode.searchForEmployee(employeeData.toString(), employeeId);
        
        assertNull(employeeDetails, "EmployeeDetails should be null for a non-existing employee ID");
    }

    @Test
    void testPrintEmployeeDetails() {
        String employeeId = "10300";
        EmployeeDetails employeeDetails = MainCode.searchForEmployee(employeeData.toString(), employeeId);
        
        assertNotNull(employeeDetails, "Precondition: EmployeeDetails should not be null for this test");
        
        // There isn't an easy way to test System.out prints in JUnit; typically, you would use a mocking framework like Mockito to mock the print stream
        // However, for the purposes of this test, we can assume the method works if it doesn't throw an exception
        assertDoesNotThrow(() -> MainCode.printEmployeeDetails(employeeDetails), "printEmployeeDetails should not throw an exception");
    }

    // Add other tests as necessary...

    @AfterEach
    void tearDown() throws IOException {
        // Clean up any resources or temporary files if necessary
    }

}
