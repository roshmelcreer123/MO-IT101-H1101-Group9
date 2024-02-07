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
        String employeeDataPath = "src/resources/motorPhEmployeeData.csv";
        String attendanceRecordPath = "src/resources/motorPhAttendanceRecord.csv";

        System.out.print("Enter year and month (YYYY/MM) to calculate gross salaries: ");
        String yearMonth = scanner.next();

        Map<String, Double> hourlyRates = readEmployeeData(employeeDataPath);
        Map<String, Double> totalHoursWorked = calculateTotalHoursWorkedForMonth(attendanceRecordPath, yearMonth);
        
        for (String employeeId : hourlyRates.keySet()) {
            double hourlyRate = hourlyRates.getOrDefault(employeeId, 0.0);
            double hoursWorked = totalHoursWorked.getOrDefault(employeeId, 0.0);
            double grossSalary = hourlyRate * hoursWorked;
            System.out.println("Employee ID: " + employeeId + ", Hours Worked: " + hoursWorked + ", Gross Salary: " + grossSalary);
        }
        
        scanner.close();
    }

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

                if (isDateInYearMonth(date, yearMonth)) {
                    totalHours.put(employeeId, totalHours.getOrDefault(employeeId, 0.0) + hoursWorked);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return totalHours;
    }

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

    private static double calculateHoursWorked(String timeIn, String timeOut) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date dateIn = sdf.parse(timeIn);
        Date dateOut = sdf.parse(timeOut);
        long difference = dateOut.getTime() - dateIn.getTime();
        return (double) difference / (1000 * 60 * 60);
    }
}
