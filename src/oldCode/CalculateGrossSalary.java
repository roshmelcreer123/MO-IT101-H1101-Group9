package mainCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalculateGrossSalary {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Define the file paths for the employee data and attendance records.
        String employeeDataPath = "src/resources/motorPhEmployeeData.csv";
        String attendanceRecordPath = "src/resources/motorPhAttendanceRecord.csv";

        // Ask the user to input the target year and month for salary calculation.
        System.out.print("Enter year and month (YYYY/MM) to calculate gross salaries: ");
        String yearMonth = scanner.next();

        // Read hourly rates from the employee data file and store them in a map.
        Map<String, Double> hourlyRates = readEmployeeData(employeeDataPath);
        
        // Calculate the total hours worked for each employee for the specified month.
        Map<String, Double> totalHoursWorked = calculateTotalHoursWorkedForMonth(attendanceRecordPath, yearMonth);
        
        // Calculate and display the gross salary for each employee.
        for (String employeeId : hourlyRates.keySet()) {
            double hourlyRate = hourlyRates.getOrDefault(employeeId, 0.0);
            double hoursWorked = totalHoursWorked.getOrDefault(employeeId, 0.0);
            double grossSalary = hourlyRate * hoursWorked;
            System.out.println("Employee ID: " + employeeId + ", Hours Worked: " + hoursWorked + ", Gross Salary: " + grossSalary);
        }
        
        scanner.close();
    }

    // Reads the employee data file and extracts hourly rates, storing them in a HashMap.
    private static Map<String, Double> readEmployeeData(String path) {
    	
        Map<String, Double> hourlyRates = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip the header line of the CSV file.
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String employeeId = data[0];
                double hourlyRate = Double.parseDouble(data[data.length - 1]); // Assuming hourly rate is the last column.
                hourlyRates.put(employeeId, hourlyRate); // Store the employee ID and hourly rate in the map.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return hourlyRates;
    }

    // Calculates total hours worked for each employee for a given month.
    private static Map<String, Double> calculateTotalHoursWorkedForMonth(String path, String yearMonth) {
       
    	Map<String, Double> totalHours = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip the header line of the CSV file.
            String line;
            
            	while ((line = br.readLine()) != null) {
                	
            			String[] data = line.split(",");
            			String employeeId = data[0];
            			String date = data[3];
            			double hoursWorked = calculateHoursWorked(data[4], data[5]);

            			// Check if the record's date falls in the specified year and month.
            			if (isDateInYearMonth(date, yearMonth)) {
            				// If so, add the hours worked to the total for that employee.
            					totalHours.put(employeeId, totalHours.getOrDefault(employeeId, 0.0) + hoursWorked);
            			}
            	}
            
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return totalHours;
    }

    // Determines if a date string falls within a specified year and month.
    private static boolean isDateInYearMonth(String date, String yearMonth) throws ParseException {
        // Define the format for parsing the date string.
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        // Parse the date string into a Date object.
        Date recordDate = sdf.parse(date);
        
        // Use a Calendar to extract the year and month from the parsed date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(recordDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Adjust for 0-indexed months (0-11).
        
        // Construct a string in the format "YYYY/MM" from the year and month.
        String recordYearMonth = year + "/" + String.format("%02d", month);
        
        // Return true if the constructed string matches the input yearMonth, false otherwise.
        return recordYearMonth.equals(yearMonth);
    }

    // Calculates the number of hours worked between two time strings.
    private static double calculateHoursWorked(String timeIn, String timeOut) throws ParseException {
        // Define the format for parsing the time strings.
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        
        // Parse the time-in and time-out strings into Date objects.
        Date dateIn = sdf.parse(timeIn);
        Date dateOut = sdf.parse(timeOut);
        
        // Calculate the difference in milliseconds between time-out and time-in.
        long difference = dateOut.getTime() - dateIn.getTime();
        
        // Convert the difference from milliseconds to hours and return the result.
        return (double) difference / (1000 * 60 * 60);
    }
}
