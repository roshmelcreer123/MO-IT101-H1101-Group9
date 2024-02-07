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
        // Paths to employee data and attendance record files
        String employeeDataPath = "src/resources/motorPhEmployeeData.csv";
        String attendanceRecordPath = "src/resources/motorPhAttendanceRecord.csv";

        // Prompt the user to enter the year and month
        System.out.print("Enter year and month (YYYY/MM) to calculate gross salaries: ");
        String yearMonth = scanner.next();

        // Read hourly rates for each employee from the employee data file
        Map<String, Double> hourlyRates = readEmployeeData(employeeDataPath);
        // Calculate total hours worked for each employee for the specified month from the attendance record file
        Map<String, Double> totalHoursWorked = calculateTotalHoursWorkedForMonth(attendanceRecordPath, yearMonth);
        
        // Calculate gross salary for each employee and display the results
        for (String employeeId : hourlyRates.keySet()) {
            double hourlyRate = hourlyRates.getOrDefault(employeeId, 0.0);
            double hoursWorked = totalHoursWorked.getOrDefault(employeeId, 0.0);
            double grossSalary = hourlyRate * hoursWorked;
            // Output employee ID, total hours worked, and gross salary
            System.out.println("Employee ID: " + employeeId + ", Hours Worked: " + hoursWorked + ", Gross Salary: " + grossSalary);
        }
        
        scanner.close();
    }

    // Read employee data from a CSV file and store hourly rates in a map
    private static Map<String, Double> readEmployeeData(String path) {
        Map<String, Double> hourlyRates = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String employeeId = data[0];
                double hourlyRate = Double.parseDouble(data[data.length - 1]); // Assuming hourly rate is the last column
                hourlyRates.put(employeeId, hourlyRate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hourlyRates;
    }

    // Calculate total hours worked for each employee for a given month from the attendance record file
    private static Map<String, Double> calculateTotalHoursWorkedForMonth(String path, String yearMonth) {
        Map<String, Double> totalHours = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String employeeId = data[0];
                String date = data[3];
                double hoursWorked = calculateHoursWorked(data[4], data[5]);

                // Check if the date falls within the specified year and month
                if (isDateInYearMonth(date, yearMonth)) {
                    totalHours.put(employeeId, totalHours.getOrDefault(employeeId, 0.0) + hoursWorked);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return totalHours;
    }

    // Check if a given date falls within the specified year and month
    private static boolean isDateInYearMonth(String date, String yearMonth) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date recordDate = sdf.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(recordDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is 0-indexed
        String recordYearMonth = year + "/" + String.format("%02d", month);
        return recordYearMonth.equals(yearMonth);
    }

    // Calculate the difference in hours between two given times
    private static double calculateHoursWorked(String timeIn, String timeOut) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date dateIn = sdf.parse(timeIn);
        Date dateOut = sdf.parse(timeOut);
        long difference = dateOut.getTime() - dateIn.getTime();
        return (double) difference / (1000 * 60 * 60);
    }
}
