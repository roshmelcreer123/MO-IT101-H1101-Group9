package mainCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainCode {

    // Main method - Entry point of the program
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Scanner object for reading user input

        // Define file paths for employee data and attendance records
        String employeeDataPath = "src/resources/motorPhEmployeeData.csv";
        String attendanceRecordPath = "src/resources/motorPhAttendanceRecord.csv";

        while (true) { // Infinite loop to keep the program running
            // Display options to the user
            System.out.println("Choose an option:");
            System.out.println("1. View Entire Employee Data Table");
            System.out.println("2. Search for an Employee in Employee Data");
            System.out.println("3. View Entire Attendance Record for Specific Year and Month");
            System.out.println("4. View Entire Attendance Record (All Records)");
            System.out.println("5. View Specific Employee Attendance Record");
            System.out.println("6. Calculate Gross Salaries");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt(); // Read user's choice
            scanner.nextLine(); // Consume the remaining newline

            // Switch case to handle different user choices
            switch (choice) {
                case 1:
                    viewEntireTable(employeeDataPath); // View entire employee data table
                    break;
                case 2:
                    System.out.print("Enter employee ID Number: ");
                    String desiredEmployeeId = scanner.nextLine();
                    searchForEmployee(employeeDataPath, desiredEmployeeId); // Search for an employee
                    break;
                case 3:
                    viewEntireTableForMonth(attendanceRecordPath, scanner); // View attendance record for a specific year and month
                    break;
                case 4:
                    viewEntireTable(attendanceRecordPath); // View all attendance records
                    break;
                case 5:
                    viewSpecificEmployee(attendanceRecordPath, scanner); // View attendance record for a specific employee
                    break;
                case 6:
                    System.out.print("Enter year and month (YYYY/MM) to calculate gross salaries: ");
                    String yearMonth = scanner.next();
                    Map<String, Double> hourlyRates = readEmployeeData(employeeDataPath);
                    Map<String, Double> totalHoursWorked = calculateTotalHoursWorkedForMonth(attendanceRecordPath, yearMonth);
                    calculateAndDisplayGrossSalaries(hourlyRates, totalHoursWorked); // Calculate and display gross salaries
                    break;
                case 7:
                    System.out.println("Exiting the program.");
                    scanner.close(); // Close the scanner
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice!"); // Handle invalid choices
            }
        }
    }
    
    private static void viewEntireTable(String path) {
        // Format string for displaying table data
        String formatString = "| %-10s | %-15s | %-15s | %-10s | %-8s | %-8s |%n";
        
        // Header string for the columns
        String header = String.format(formatString, "Employee ID", "Last Name", "First Name", "Date", "Time In", "Time Out");

        try {
            // Opening the file for reading
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            
            // Print the header
            System.out.print(header);

            // Reading each line from the file
            while ((line = br.readLine()) != null) {
                // Parsing the CSV line and formatting the output
                List<String> values = parseCsvLine(line);
                if (values.size() == 6) { // Ensure there are 6 values per line
                    System.out.printf(formatString, values.toArray());
                }
            }
            // Closing the file reader
            br.close();
        } catch (IOException e) {
            e.printStackTrace(); // Printing any IO exceptions
        }
    }
    
    private static void viewEntireTableForMonth(String path, Scanner scanner) {
        System.out.print("Enter year and month (YYYY/MM): ");
        String yearMonth = scanner.next();

        try {
            String[] yearMonthParts = yearMonth.split("/");
            int year = Integer.parseInt(yearMonthParts[0]);
            int month = Integer.parseInt(yearMonthParts[1]);

            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new FileReader(path));

            // Skip the header line
            String header = br.readLine();

            // Print the header
            System.out.println(header);

            // Map to store total hours worked per day
            Map<String, Double> dailyHoursMap = new HashMap<>();

            // Loop through each line in the file until the end is reached
            String line;
            while ((line = br.readLine()) != null) {
                String[] csvValues = line.split(",");

                // Parse the date from the CSV
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Date recordDate = sdf.parse(csvValues[3]);

                // Get the year and month from the record date
                Calendar recordCalendar = Calendar.getInstance();
                recordCalendar.setTime(recordDate);
                int recordYear = recordCalendar.get(Calendar.YEAR);
                int recordMonth = recordCalendar.get(Calendar.MONTH);

                // Check if the record belongs to the specified year and month
                if (recordYear == year && recordMonth == (month - 1)) {
                    // Calculate and print hours worked
                    double hoursWorked = calculateHoursWorked(csvValues[4], csvValues[5]);
                    System.out.println(line + " - Hours Worked: " + hoursWorked);

                    // Update daily hours map
                    String day = sdf.format(recordDate);
                    dailyHoursMap.put(day, dailyHoursMap.getOrDefault(day, 0.0) + hoursWorked);
                }
            }

            // Print total hours worked per day
            System.out.println("\nTotal Hours Worked per Day:");
            for (Map.Entry<String, Double> entry : dailyHoursMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " hours");
            }

            // Close the BufferedReader to release system resources
            br.close();

        } catch (IOException | ParseException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace(); // Print the error to the console
        }
    }
    
    private static void viewSpecificEmployee(String path, Scanner scanner) {
        System.out.print("Enter employee ID: ");
        String employeeId = scanner.next();
        System.out.print("Enter year and month (YYYY/MM): ");
        String yearMonth = scanner.next();

        try {
            String[] yearMonthParts = yearMonth.split("/");
            int year = Integer.parseInt(yearMonthParts[0]);
            int month = Integer.parseInt(yearMonthParts[1]);

            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new FileReader(path));

            // Skip the header line
            String header = br.readLine();

            // Variables to store total hours worked by the employee for the month
            double totalHoursThisMonth = 0;

            // Map to store hours worked by the employee on each date
            TreeMap<String, Double> dailyHoursMap = new TreeMap<>();

            // Loop through each line in the file until the end is reached
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into an array of values using ',' as a delimiter
                String[] csvValues = line.split(",");

                // Extract employee ID and hours worked
                String recordEmployeeId = csvValues[0];
                double hoursWorked = calculateHoursWorked(csvValues[4], csvValues[5]);

                // Parse the date from the CSV
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Date recordDate = sdf.parse(csvValues[3]);

                // Get the year and month from the record date
                Calendar recordCalendar = Calendar.getInstance();
                recordCalendar.setTime(recordDate);
                int recordYear = recordCalendar.get(Calendar.YEAR);
                int recordMonth = recordCalendar.get(Calendar.MONTH);

                // Check if the record belongs to the specified employee and month
                if (recordEmployeeId.equals(employeeId) && recordYear == year && recordMonth == (month - 1)) {
                    // Print the record
                    System.out.println(line + " - Hours Worked: " + hoursWorked);
                    
                    // Update total hours for the month
                    totalHoursThisMonth += hoursWorked;

                    // Store hours worked by the employee on each date
                    String dateString = new SimpleDateFormat("MM/dd/yyyy").format(recordDate);
                    dailyHoursMap.put(dateString, dailyHoursMap.getOrDefault(dateString, 0.0) + hoursWorked);
                }
            }

            // Print total hours worked this month by the employee
            System.out.println("\nTotal Hours Worked this Month by Employee " + employeeId + ": " + totalHoursThisMonth + " hours");

            // Print hours worked by the employee on each date
            System.out.println("\nHours Worked by Employee " + employeeId + " on Each Date:");
            for (Map.Entry<String, Double> entry : dailyHoursMap.entrySet()) {
                System.out.println(entry.getKey() + " - Hours Worked: " + entry.getValue());
            }

            // Close the BufferedReader to release system resources
            br.close();

        } catch (IOException | ParseException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace(); // Print the error to the console
        }
    }
    
    private static void calculateAndDisplayGrossSalaries(Map<String, Double> hourlyRates, Map<String, Double> totalHoursWorked) {
        for (String employeeId : hourlyRates.keySet()) {
            double hourlyRate = hourlyRates.getOrDefault(employeeId, 0.0);
            double hoursWorked = totalHoursWorked.getOrDefault(employeeId, 0.0);
            double grossSalary = hourlyRate * hoursWorked;
            System.out.println("Employee ID: " + employeeId + ", Hours Worked: " + hoursWorked + ", Gross Salary: " + grossSalary);
        }
    }

    // Method to search for an employee in employee data
    private static void searchForEmployee(String path, String desiredEmployeeId) {
        // Format string for displaying employee data
        String formatString = "| %-10s | %-15s | %-15s | %-9s | %-76s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-30s | %-30s | %-12s | %-12s | %-15s | %-20s | %-23s | %-11s |%n";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            boolean employeeFound = false; // Flag to check if employee is found
            // Header format for the table
            String header = String.format(formatString, "Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number", "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate");
            while ((line = br.readLine()) != null) {
                List<String> values = parseCsvLine(line);
                // Check if the current line is the desired employee
                if (!values.isEmpty() && values.get(0).equals(desiredEmployeeId)) {
                    if (!employeeFound) {
                        System.out.print(header);
                        employeeFound = true;
                    }
                    System.out.printf(formatString, values.toArray(new String[0]));
                    break; // Break the loop as employee is found
                }
            }
            if (!employeeFound) {
                System.out.println("Employee not found."); // Inform if employee is not found
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to parse a CSV line considering quoted fields
    private static List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder sb = new StringBuilder(); // StringBuilder to accumulate field values
        boolean inQuotes = false; // Flag to handle quotes in CSV

        // Iterate over each character in the line
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes; // Toggle the inQuotes flag
            } else if (c == ',' && !inQuotes) {
                values.add(sb.toString()); // Add value to list when comma is encountered outside quotes
                sb.setLength(0); // Reset StringBuilder for next value
            } else {
                sb.append(c); // Append character to current value
            }
        }
        values.add(sb.toString()); // Add the last field
        return values;
    }
    
    // Method to read employee data from CSV and extract hourly rates
    private static Map<String, Double> readEmployeeData(String path) {
        Map<String, Double> hourlyRates = new HashMap<>(); // Store hourly rates mapped to employee IDs
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip the header line
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Split CSV line into parts
                String employeeId = data[0]; // Assume first element is employee ID
                double hourlyRate = Double.parseDouble(data[data.length - 1]); // Assume last element is hourly rate
                hourlyRates.put(employeeId, hourlyRate); // Map employee ID to hourly rate
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print any IO exceptions
        }
        return hourlyRates; // Return the map of hourly rates
    }

    // Method to calculate total hours worked for each employee for a specific month
    private static Map<String, Double> calculateTotalHoursWorkedForMonth(String path, String yearMonth) {
        Map<String, Double> totalHours = new HashMap<>(); // Store total hours worked mapped to employee IDs
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip the header line
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Split CSV line into parts
                String employeeId = data[0]; // Assume first element is employee ID
                String date = data[3]; // Assume fourth element is date
                double hoursWorked = calculateHoursWorked(data[4], data[5]); // Calculate hours worked from time in and time out

                if (isDateInYearMonth(date, yearMonth)) { // Check if date falls within specified year and month
                    totalHours.put(employeeId, totalHours.getOrDefault(employeeId, 0.0) + hoursWorked); // Add hours worked to total for the employee
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace(); // Print any exceptions
        }
        return totalHours; // Return the map of total hours worked
    }

    // Method to check if a given date string falls within a specified year and month
    private static boolean isDateInYearMonth(String date, String yearMonth) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // Date format for parsing
        Date recordDate = sdf.parse(date); // Parse the date string
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(recordDate); // Set calendar to parsed date
        int year = calendar.get(Calendar.YEAR); // Extract year
        int month = calendar.get(Calendar.MONTH) + 1; // Extract month (adding 1 as Calendar.MONTH is zero-based)
        String recordYearMonth = year + "/" + String.format("%02d", month); // Format year and month into "YYYY/MM"
        return recordYearMonth.equals(yearMonth); // Compare with specified yearMonth
    }

    // Method to calculate the number of hours worked between two time strings
    private static double calculateHoursWorked(String timeIn, String timeOut) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // Time format for parsing
        Date dateIn = sdf.parse(timeIn); // Parse time-in string
        Date dateOut = sdf.parse(timeOut); // Parse time-out string
        long difference = dateOut.getTime() - dateIn.getTime(); // Calculate difference in milliseconds
        return (double) difference / (1000 * 60 * 60); // Convert milliseconds to hours and return
    }
}


