package oldCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalculateGrossSalary2 {

    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);

        // Define the file paths for the employee data and attendance records.
        String employeeDataPath = "src/resources/motorPhEmployeeData.csv";
        String attendanceRecordPath = "src/resources/motorPhAttendanceRecord.csv";

        // Ask the user to input the target year and month for salary calculation.
        System.out.print("Enter year and month (YYYY/MM) to calculate gross salaries: ");
        String yearMonth = scanner.next();

        // Read hourly rates, allowances, and employee IDs from the employee data file.
        Map<String, Double> hourlyRates = readEmployeeData(employeeDataPath);

        // Calculate the total hours worked for each employee for the specified month.
        String employeeDataPath1 = "src/resources/motorPhEmployeeData.csv";
        Map<String, Double> totalHoursWorked = calculateTotalHoursWorkedForMonth(attendanceRecordPath, yearMonth, employeeDataPath1);

        // Calculate and display the gross salary for each employee.
        for (String employeeId : hourlyRates.keySet()) {
            double hourlyRate = hourlyRates.get(employeeId);
            double allowance = readEmployeeAllowance(employeeDataPath1, employeeId, yearMonth);
            double hoursWorked = totalHoursWorked.getOrDefault(employeeId, 0.0);
            double grossSalary = hourlyRate * hoursWorked + allowance;
            System.out.println("Employee ID: " + employeeId + ", Hours Worked: " + hoursWorked + ", Monthly Allowance: " + allowance + ", Gross Salary: " + grossSalary);
            
        }

        scanner.close();
    }

    private static Map<String, Double> calculateTotalHoursWorkedForMonth(String attendanceRecordPath, String yearMonth, String employeeDataPath1) throws ParseException {
        Map<String, Double> totalHoursWorked = new HashMap<>();

        CalculateGrossSalary2 obj = new CalculateGrossSalary2();

        try (BufferedReader br = new BufferedReader(new FileReader(attendanceRecordPath))) {
            br.readLine(); // Skip the header line of the CSV file.
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String employeeId = data[0];
                String date = data[1];
                String timeIn = data[2];
                String timeOut = data[3];

                if (isDateInYearMonth(date, yearMonth)) {
                    double hoursWorked = obj.calculateHoursWorked(timeIn, timeOut);
                    System.out.println("Employee ID: " + employeeId + ", Date: " + date + ", Time In: " + timeIn + ", Time Out: " + timeOut + ", Hours Worked: " + hoursWorked); // Print intermediate results
                    totalHoursWorked.put(employeeId, totalHoursWorked.getOrDefault(employeeId, 0.0) + hoursWorked);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print the accumulated total hours worked for each employee
        for (Map.Entry<String, Double> entry : totalHoursWorked.entrySet()) {
            System.out.println("Employee ID: " + entry.getKey() + ", Total Hours Worked: " + entry.getValue());
        }

        return totalHoursWorked;
    }

    private double calculateHoursWorked(String timeIn, String timeOut) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date dateIn = sdf.parse(timeIn);
        Date dateOut = sdf.parse(timeOut);

        long difference = dateOut.getTime() - dateIn.getTime();
        return (double) difference / (1000 * 60 * 60);
    }
    
	private static Double readEmployeeAllowance(String employeeDataPath, String employeeId, String yearMonth) {
        Map<String, Double> allowances = readAllowances(employeeDataPath, employeeId);
        return allowances.getOrDefault(yearMonth, 0.0);
    }

	private static Map<String, Double> readAllowances(String path, String employeeId) {
	    Map<String, Double> allowances = new HashMap<>();

	    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
	        br.readLine(); // Skip the header line of the CSV file.
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] data = line.split(",");
	            String currentEmployeeId = data[0];
	            if (currentEmployeeId.equals(employeeId)) {
	                String allowanceString = data[data.length - 1];
	                String[] allowanceData = allowanceString.split(";");
	                for (String entry : allowanceData) {
	                    String[] keyValue = entry.split(":");
	                    if (keyValue.length == 2) {
	                        allowances.put(keyValue[0], Double.parseDouble(keyValue[1].replaceAll("\"", "")));
	                    }
	                }
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return allowances;
	}

 // Reads the employee data file and extracts hourly rates, storing them in a map.
	private static Map<String, Double> readEmployeeData(String path) {
	    Map<String, Double> hourlyRates = new HashMap<>();

	    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
	        br.readLine(); // Skip the header line of the CSV file.
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] data = line.split(",");
	            String employeeId = data[0];

	            double hourlyRate = 0.0;
	            if (data.length >= data.length - 1) {
	                hourlyRate = Double.parseDouble(data[data.length - 2].replaceAll("\"", "").trim());
	            }

	            // Insert the print statement here:
	            System.out.println("Employee ID: " + employeeId + ", Hourly Rate: " + hourlyRate);

	            hourlyRates.put(employeeId, hourlyRate); // Store the employee ID and hourly rate in the map.
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return hourlyRates;
	}


    // Determines if a date string falls within a specified year and month.
	private static boolean isDateInYearMonth(String date, String yearMonth) throws ParseException {
	    if (date == null || date.isEmpty() || !date.matches("\\d{2}/\\d{2}/\\d{4}")) {
	        System.err.println("Invalid date format or empty: " + date);
	        return false;
	    }

	    // Check if the date field is not empty or contains invalid characters.
	    if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
	        System.err.println("Invalid date format or empty: " + date);
	        return false;
	    }

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
}

