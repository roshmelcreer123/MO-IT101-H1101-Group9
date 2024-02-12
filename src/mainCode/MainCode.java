package mainCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainCode {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Define the file paths for the employee data and attendance records.
        String employeeDataPath = "src/resources/motorPhEmployeeData.csv";
        String attendanceRecordPath = "src/resources/motorPhAttendanceRecord.csv";
        
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. View Entire Table Data");
            System.out.println("2. Search for an Employee");
            System.out.println("3. Calculate Gross Salary");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    viewEntireTable(employeeDataPath);
                    break;
                case 2:
                    System.out.print("Enter employee ID Number: ");
                    String desiredEmployeeId = scanner.nextLine();
                    searchForEmployee(employeeDataPath, desiredEmployeeId);
                    break;
                case 3:
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
                    break;
                case 4:
                    System.out.println("Exiting the program.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void viewEntireTable(String path) {
        String formatString = "| %-10s | %-15s | %-15s | %-9s | %-76s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-30s | %-30s | %-12s | %-12s | %-15s | %-20s | %-23s | %-11s |%n";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                List<String> values = parseCsvLine(line);
                System.out.printf(formatString, values.toArray());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void searchForEmployee(String path, String desiredEmployeeId) {
        String formatString = "| %-10s | %-15s | %-15s | %-9s | %-76s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-30s | %-30s | %-12s | %-12s | %-15s | %-20s | %-23s | %-11s |%n";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            boolean employeeFound = false;
            String header = String.format(formatString, "Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number", "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate");
            while ((line = br.readLine()) != null) {
                List<String> values = parseCsvLine(line);
                if (!values.isEmpty() && values.get(0).equals(desiredEmployeeId)) {
                    if (!employeeFound) {
                        System.out.print(header);
                        employeeFound = true;
                    }
                    System.out.printf(formatString, values.toArray(new String[0]));
                    break;
                }
            }
            if (!employeeFound) {
                System.out.println("Employee not found.");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes; // Toggle the inQuotes flag
            } else if (c == ',' && !inQuotes) {
                values.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        values.add(sb.toString());
        return values;
    }

    private static Map<String, Double> readEmployeeData(String path) {
        Map<String, Double> hourlyRates = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip the header
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String employeeId = data[0];
                double hourlyRate = Double.parseDouble(data[data.length - 1]);
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
            br.readLine(); // Skip the header
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
        int month = calendar.get(Calendar.MONTH) + 1; // Adjust for 0-indexed months
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

