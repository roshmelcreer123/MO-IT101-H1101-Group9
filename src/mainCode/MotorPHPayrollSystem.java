package mainCode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MotorPHPayrollSystem {

	// 2 Dimensional Array for SSS Contribution
    private static final double[][] SSS_CONTRIBUTION_TABLE = {
    	    {0.00, 3250.00, 135.00}, // Below 3,250
    	    {3250.00, 3750.00, 157.50},
    	    {3750.00, 4250.00, 180.00},
    	    {4250.00, 4750.00, 202.50},
    	    {4750.00, 5250.00, 225.00},
    	    {5250.00, 5750.00, 247.50},
    	    {5750.00, 6250.00, 270.00},
    	    {6250.00, 6750.00, 292.50},
    	    {6750.00, 7250.00, 315.00},
    	    {7250.00, 7750.00, 337.50},
    	    {7750.00, 8250.00, 360.00},
    	    {8250.00, 8750.00, 382.50},
    	    {8750.00, 9250.00, 405.00},
    	    {9250.00, 9750.00, 427.50},
    	    {9750.00, 10250.00, 450.00},
    	    {10250.00, 10750.00, 472.50},
    	    {10750.00, 11250.00, 495.00},
    	    {11250.00, 11750.00, 517.50},
    	    {11750.00, 12250.00, 540.00},
    	    {12250.00, 12750.00, 562.50},
    	    {12750.00, 13250.00, 585.00},
    	    {13250.00, 13750.00, 607.50},
    	    {13750.00, 14250.00, 630.00},
    	    {14250.00, 14750.00, 652.50},
    	    {14750.00, 15250.00, 675.00},
    	    {15250.00, 15750.00, 697.50},
    	    {15750.00, 16250.00, 720.00},
    	    {16250.00, 16750.00, 742.50},
    	    {16750.00, 17250.00, 765.00},
    	    {17250.00, 17750.00, 787.50},
    	    {17750.00, 18250.00, 810.00},
    	    {18250.00, 18750.00, 832.50},
    	    {18750.00, 19250.00, 855.00},
    	    {19250.00, 19750.00, 877.50},
    	    {19750.00, 20250.00, 900.00},
    	    {20250.00, 20750.00, 922.50},
    	    {20750.00, 21250.00, 945.00},
    	    {21250.00, 21750.00, 967.50},
    	    {21750.00, 22250.00, 990.00},
    	    {22250.00, 22750.00, 1012.50},
    	    {22750.00, 23250.00, 1035.00},
    	    {23250.00, 23750.00, 1057.50},
    	    {23750.00, 24250.00, 1080.00},
    	    {24250.00, 24750.00, 1102.50},
    	    // The last bracket covers everything above 24750.00
    	    {24750.00, Double.POSITIVE_INFINITY, 1125.00}
    	    };


        // Philhealth constants
        private static final double PHILHEALTH_LOWER_SALARY_CAP = 10000.00;
        private static final double PHILHEALTH_UPPER_SALARY_CAP = 60000.00;
        private static final double PHILHEALTH_MINIMUM_PREMIUM = 300.00;
        private static final double PHILHEALTH_MAXIMUM_PREMIUM = 1800.00;

        // Pag-ibig constants
        private static final double PAGIBIG_LOWER_RATE = 0.01;
        private static final double PAGIBIG_UPPER_RATE = 0.02;
        private static final double PAGIBIG_LOWER_SALARY_CAP = 1500.00;
        private static final double PAGIBIG_MAXIMUM_CONTRIBUTION = 100.00;

        // Tax brackets as provided in the table
        private static final double[] TAX_BRACKETS = {20833, 33333, 66667, 166667, 666667};
        private static final double[] TAX_BASE = {0, 2500, 10833, 40833.33, 200833.33};
        private static final double[] TAX_RATES = {0.2, 0.25, 0.3, 0.32, 0.35};
	
	
	// Global Values - Allowances
	private static final double RICE_SUBSIDY = 1500.0;
    private static final double PHONE_ALLOWANCE = 2000.0;
    private static final double CLOTHING_ALLOWANCE = 1000.0;

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
            System.out.println("7. View Net Salary of Specific Employee");
            System.out.println("8. View All Net Salary Records for a Specific Month");
            System.out.println("9. View Gross Salaries for a Specific Month");
            System.out.println("10. View Payslip of Specific Employee");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt(); // Read user's choice
            scanner.nextLine(); // Consume the remaining newline

            // Switch case to handle different user choices
            switch (choice) {
                case 1:
                    viewEntireEmployeeTable(employeeDataPath); // View entire employee data table
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
                    viewEntireAttendanceTable(attendanceRecordPath); // View all attendance records
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
                    viewNetSalaryOfSpecificEmployee(scanner);
                    break;
                case 8:
                    viewAllNetSalariesForSpecificMonth(scanner);
                    break;
                case 9:
                    System.out.print("Enter year and month (YYYY/MM) to calculate gross salaries: ");
                    String yearMonth1 = scanner.nextLine();
                    displayGrossSalariesForMonth(yearMonth1);
                    break;    
                case 10:
                    viewPayslipOfSpecificEmployee(scanner, employeeDataPath);
                    break;
                case 11:
                    System.out.println("Exiting the program.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice!"); // Handle invalid choices
            }
        }
    }
   



	public static void viewEntireEmployeeTable(String path) {
    	// Defines a format string for printing each row of the employee table. 
    	// Each placeholder e.g. "%-15s" corresponds to a column in the table. 
    	// The "-15s" format specifier means that the string will be left-aligned within a 15-character wide field, ensuring uniform column widths for most fields.
    	// The "|" characters are used as separators between columns to enhance readability. 
    	// The "%n" at the end inserts a newline, ensuring each row of the table starts on a new line. 
    	// This format string ensures that the table's output is neatly aligned, with each column's width tailored to the content it displays or the space it is allocated.
        String formatString = "| %-10s | %-15s | %-15s | %-9s | %-76s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-30s | %-30s | %-12s | %-12s | %-15s | %-20s | %-23s | %-11s |%n";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;

            
            while ((line = br.readLine()) != null) {
                // Handle quoted fields in line
                List<String> values = parseCsvLine(line);
                System.out.printf(formatString, values.toArray());
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void viewEntireAttendanceTable(String path) {
    	String formatString = "| %-10s | %-20s | %-20s | %-20s | %-20s | %-20s |%n";
    	
    	try {
            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new FileReader(path));

            // Print the header
            System.out.println("Entire table:");

            // Map to store total hours worked per year
            Map<Integer, Double> yearlyHoursMap = new HashMap<>();

            // Loop through each line in the file until the end is reached
            String line;
            while ((line = br.readLine()) != null) {
                String[] csvValues = line.split(",");

                // Skip any line that does not start with a valid date
                if (!csvValues[3].matches("\\d{2}/\\d{2}/\\d{4}")) {
                    continue;
                }

                // Parse the date from the CSV
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Date recordDate = sdf.parse(csvValues[3]);

                // Get the year from the record date
                Calendar recordCalendar = Calendar.getInstance();
                recordCalendar.setTime(recordDate);
                int recordYear = recordCalendar.get(Calendar.YEAR);

                // Calculate and add hours worked to yearly hours map
                double hoursWorked = calculateHoursWorked(csvValues[4], csvValues[5]);
                yearlyHoursMap.put(recordYear, yearlyHoursMap.getOrDefault(recordYear, 0.0) + hoursWorked);

                // Print each line with formatted output
                System.out.printf(formatString, csvValues[0], csvValues[1], csvValues[2], sdf.format(recordDate), csvValues[4], csvValues[5], String.format("%.2f", hoursWorked));
            }

            // Print total hours worked per year
            System.out.println("\nTotal Hours Worked per Year:");
            for (Map.Entry<Integer, Double> entry : yearlyHoursMap.entrySet()) {
                System.out.println("Total Hours worked " + entry.getKey() + ": " + entry.getValue() + " hours");
            }

            // Close the BufferedReader to release system resources
            br.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace(); // Print the error to the console
        }
    }
    
    private static void viewEntireTableForMonth(String path, Scanner scanner) {
        System.out.print("Enter year and month (YYYY/MM): ");
        String yearMonth = scanner.next();
        String formatString = "| %-10s | %-20s | %-20s | %-20s | %-20s | %-20s |%n";

        try {
            String[] yearMonthParts = yearMonth.split("/");
            int year = Integer.parseInt(yearMonthParts[0]);
            int month = Integer.parseInt(yearMonthParts[1]);

            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new FileReader(path));
            
            // Format string for displaying table data
            // Employee ID (10), Last Name (20), First Name (20), Date (20), Time-in (20), Time-out (20)
            
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
                    
                    // Print each line with formatted output
                    System.out.printf(formatString, csvValues[0], csvValues[1], csvValues[2], sdf.format(recordDate), csvValues[4], csvValues[5], String.format("%.2f", hoursWorked));


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
        
        String formatString = "| %-10s | %-20s | %-20s | %-20s | %-20s | %-20s |%n";

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
                	
                	// Print each line with formatted output
                	System.out.printf(formatString, csvValues[0], csvValues[1], csvValues[2], sdf.format(recordDate), csvValues[4], csvValues[5], String.format("%.2f", hoursWorked));
                    
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

            // Calculate prorated salary (total hrs worked x hourly pay)
            double proratedSalary = hourlyRate * hoursWorked;
            String formattedHoursWorked = String.format("%.2f", hoursWorked);
            String formattedProratedSalary = String.format("%,.2f", proratedSalary);

            // Calculate and format total monthly allowances
            double totalMonthlyAllowance = RICE_SUBSIDY + PHONE_ALLOWANCE + CLOTHING_ALLOWANCE;
            String formattedTotalMonthlyAllowance = String.format("%,.2f", totalMonthlyAllowance);

            // Calculate and format gross salary (prorated salary + monthly allowances)
            double grossSalary = proratedSalary + totalMonthlyAllowance;
            String formattedGrossSalary = String.format("%,.2f", grossSalary);


            // Display the result with the total gross salary including allowances
            System.out.println("Employee ID: " + employeeId + "; Hours Worked: " + formattedHoursWorked +
                    "; Prorated Salary: " + formattedProratedSalary + " + Monthly Allowances: " +
                    formattedTotalMonthlyAllowance + "; Gross Salary: " + formattedGrossSalary);
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
    
    
    private static void displayGrossSalariesForMonth(String yearMonth) {
        // The paths should be adjusted to where your files are located in your project structure.
        String employeeDataPath = "src/resources/motorPhEmployeeData.csv";
        String attendanceRecordPath = "src/resources/motorPhAttendanceRecord.csv";

        // Read hourly rates from the employee data file and store them in a map.
        Map<String, Double> hourlyRates = readEmployeeData(employeeDataPath);

        // Calculate the total hours worked for each employee for the specified month.
        Map<String, Double> totalHoursWorked = calculateTotalHoursWorkedForMonth(attendanceRecordPath, yearMonth);

     // Calculate and display the gross salary for each employee.
        for (String employeeId : hourlyRates.keySet()) {
            double hourlyRate = hourlyRates.getOrDefault(employeeId, 0.0);
            double hoursWorked = totalHoursWorked.getOrDefault(employeeId, 0.0);

            // Calculate prorated salary (total hrs worked x hourly pay)
            double proratedSalary = hourlyRate * hoursWorked;
            String formattedHoursWorked = String.format("%.2f", hoursWorked);
            String formattedProratedSalary = String.format("%,.2f", proratedSalary);

            // Calculate and format total monthly allowances
            double totalMonthlyAllowance = RICE_SUBSIDY + PHONE_ALLOWANCE + CLOTHING_ALLOWANCE;
            String formattedTotalMonthlyAllowance = String.format("%,.2f", totalMonthlyAllowance);

            // Calculate and format gross salary (prorated salary + monthly allowances)
            double grossSalary = proratedSalary + totalMonthlyAllowance;
            String formattedGrossSalary = String.format("%,.2f", grossSalary);


            // Display the result with the total gross salary including allowances
            System.out.println("Employee ID: " + employeeId + "; Hours Worked: " + formattedHoursWorked +
                    "; Prorated Salary: " + formattedProratedSalary + " + Monthly Allowances: " +
                    formattedTotalMonthlyAllowance + "; Gross Salary: " + formattedGrossSalary);
        }
    }


	// Calculate and display the gross salary for each employee.
	// This method has been copied and adjusted from CalculateGrossSalary3
	public static double calculateGrossSalaryForEmployee(String employeeId, String yearMonth) {
	    Map<String, Double> hourlyRates = readEmployeeData("src/resources/motorPhEmployeeData.csv");
	    Map<String, Double> totalHoursWorked = calculateTotalHoursWorkedForMonth("src/resources/motorPhAttendanceRecord.csv", yearMonth);
	
	    double hourlyRate = hourlyRates.getOrDefault(employeeId, 0.0);
	    double hoursWorked = totalHoursWorked.getOrDefault(employeeId, 0.0);
	    double proratedSalary = hourlyRate * hoursWorked;
	    double totalMonthlyAllowance = RICE_SUBSIDY + PHONE_ALLOWANCE + CLOTHING_ALLOWANCE;
	
	    return proratedSalary + totalMonthlyAllowance;
	}
	
	private static void viewNetSalaryOfSpecificEmployee(Scanner scanner) {
        System.out.print("Enter Employee ID: ");
        String employeeId = scanner.nextLine();
        System.out.print("Enter Year and Month (YYYY/MM): ");
        String yearMonth = scanner.nextLine();

        // Calculate the gross salary for the employee
        double grossSalary = calculateGrossSalaryForEmployee(employeeId, yearMonth);

        // Calculate each type of deduction
        double sssDeduction = calculateSssDeduction(grossSalary);
        double philhealthDeduction = calculatePhilhealthDeduction(grossSalary);
        double pagibigDeduction = calculatePagibigDeduction(grossSalary);
        
        // Assuming taxable income is grossSalary - total deductions (for simplicity)
        double taxableIncome = grossSalary - (sssDeduction + philhealthDeduction + pagibigDeduction);
        double taxDeduction = calculateTaxDeduction(taxableIncome);

        // Calculate the net salary
        double netSalary = grossSalary - (sssDeduction + philhealthDeduction + pagibigDeduction + taxDeduction);

     // Display detailed salary information for the current employee
        System.out.println("Employee ID: " + employeeId + " | Net Salary for " + yearMonth + ": " + netSalary);
        System.out.println("Gross Salary: " + grossSalary);
        System.out.println("Total Deductions: ");
        System.out.println("SSS: " + sssDeduction);
        System.out.println("Philhealth: " + philhealthDeduction);
        System.out.println("Pagibig: " + pagibigDeduction);
        System.out.println("Tax: " + taxDeduction);
        System.out.println(); // Adding a blank line for better readability
        
        // Ask if the user wants to view the breakdown per week
        System.out.print("View Employee’s Net Salary per Week (Y/N): ");
        String viewPerWeek = scanner.nextLine();

        if (viewPerWeek.equalsIgnoreCase("Y")) {
            // Display the employee's net salary broken down per week for the chosen month
            displayNetSalaryPerWeek(employeeId, yearMonth, netSalary);
        } else {
            // Simply re-show the total net salary of the specific employee for the chosen month
            System.out.println("Total Net Salary for the month: " + netSalary);
        }
    }
    
    
    private static void viewAllNetSalariesForSpecificMonth(Scanner scanner) {
        System.out.print("Enter Year and Month (YYYY/MM) to view all net salaries: ");
        String yearMonth = scanner.nextLine();
        // Assuming we have a method to get all employee IDs, we need to implement it
        List<String> allEmployeeIds = getAllEmployeeIds(); // This needs to be implemented

        // Now we iterate over all employee IDs to calculate and display their net salaries
        for (String employeeId : allEmployeeIds) {
            double grossSalary = calculateGrossSalaryForEmployee(employeeId, yearMonth);
            double sssDeduction = calculateSssDeduction(grossSalary);
            double philhealthDeduction = calculatePhilhealthDeduction(grossSalary);
            double pagibigDeduction = calculatePagibigDeduction(grossSalary);
            double taxableIncome = grossSalary - (sssDeduction + philhealthDeduction + pagibigDeduction);
            double taxDeduction = calculateTaxDeduction(taxableIncome);
            double netSalary = grossSalary - (sssDeduction + philhealthDeduction + pagibigDeduction + taxDeduction);

            // Display the net salary for the current employee
            System.out.println("Employee ID: " + employeeId + " | Net Salary for " + yearMonth + ": " + netSalary);
        }
    }
    
    private static List<String> getAllEmployeeIds() {
        List<String> employeeIds = new ArrayList<>();
        String path = "src/resources/motorPhEmployeeData.csv"; // Path to your employee data CSV

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String employeeId = data[0];
                if (!employeeIds.contains(employeeId)) {
                    employeeIds.add(employeeId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return employeeIds;
    }


	private static double calculateTaxDeduction(double taxableIncome) {
        double taxDue = 0;
        
        for (int i = 0; i < TAX_BRACKETS.length; i++) {
            if (taxableIncome > TAX_BRACKETS[i]) {
                if (i + 1 < TAX_BRACKETS.length && taxableIncome >= TAX_BRACKETS[i + 1]) {
                    // If taxable income is greater than the current bracket but less than the next bracket
                    // Continue to next iteration to find the correct bracket
                    continue;
                }
                
                // Calculate tax for the bracket the taxable income falls into
                taxDue = TAX_BASE[i] + ((taxableIncome - TAX_BRACKETS[i]) * TAX_RATES[i]);
                break;
            }
        }
        
        return taxDue;
    }


    private static double calculatePagibigDeduction(double grossSalary) {
        double contributionRate = grossSalary > PAGIBIG_LOWER_SALARY_CAP ? PAGIBIG_UPPER_RATE : PAGIBIG_LOWER_RATE;
        double contribution = grossSalary * contributionRate;

        // Ensure the contribution does not exceed the maximum amount
        contribution = Math.min(contribution, PAGIBIG_MAXIMUM_CONTRIBUTION);

        return contribution;
    }


    private static double calculatePhilhealthDeduction(double grossSalary) {
        double employeeShare;

        if (grossSalary <= PHILHEALTH_LOWER_SALARY_CAP) {
            // If salary is at or below the lower cap, the minimum premium applies
            employeeShare = PHILHEALTH_MINIMUM_PREMIUM / 2; // Employee's share is half of the total premium
        } else if (grossSalary > PHILHEALTH_LOWER_SALARY_CAP && grossSalary < PHILHEALTH_UPPER_SALARY_CAP) {
            // For salaries between the lower and upper cap, calculate the premium as 3% of the salary, divided by 2 for the employee's share
            double totalPremium = (grossSalary * 0.03);
            employeeShare = totalPremium / 2;
            // Ensure the employee share does not exceed the maximum or fall below the minimum
            employeeShare = Math.max(employeeShare, PHILHEALTH_MINIMUM_PREMIUM / 2);
            employeeShare = Math.min(employeeShare, PHILHEALTH_MAXIMUM_PREMIUM / 2);
        } else {
            // For salaries at or above the upper cap, the maximum premium applies
            employeeShare = PHILHEALTH_MAXIMUM_PREMIUM / 2; // Employee's share is half of the total premium
        }

        return employeeShare;
    }


    private static double calculateSssDeduction(double grossSalary) {
        for (double[] bracket : SSS_CONTRIBUTION_TABLE) {
            if (grossSalary >= bracket[0] && grossSalary < bracket[1]) {
                return bracket[2];
            }
        }
        // Default contribution if salary is beyond the table
        return SSS_CONTRIBUTION_TABLE[SSS_CONTRIBUTION_TABLE.length - 1][2];
    }

	private static void displayNetSalaryPerWeek(String employeeId, String yearMonth, double netSalary) {
        // Assuming a fixed 4-week month for simplicity. Adjust this logic based on your actual requirements.
        double weeklyNetSalary = netSalary / 4;
        for (int week = 1; week <= 4; week++) {
            System.out.println("Week " + week + ": " + weeklyNetSalary);
        }
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
    
    private static void viewPayslipOfSpecificEmployee(Scanner scanner, String employeeDataPath) {
        System.out.print("Enter Employee ID: ");
        String employeeId = scanner.nextLine();
        System.out.print("Enter Year and Month (YYYY/MM): ");
        String yearMonth = scanner.nextLine();

        try {
            BufferedReader br = new BufferedReader(new FileReader(employeeDataPath));
            String line;
            boolean employeeFound = false;
            
            while ((line = br.readLine()) != null) {
                List<String> values = parseCsvLine(line);
                if (!values.isEmpty() && values.get(0).equals(employeeId)) {
                    employeeFound = true;
                    
                    String firstName = values.get(2); // Assuming First Name is at index 2
                    String lastName = values.get(1); // Assuming Last Name is at index 1
                    String position = values.get(11); // Assuming Position is at index 11

                    // Calculate the gross salary for the employee
                    double grossSalary = calculateGrossSalaryForEmployee(employeeId, yearMonth);

                    // Calculate each type of deduction
                    double sssDeduction = calculateSssDeduction(grossSalary);
                    double philhealthDeduction = calculatePhilhealthDeduction(grossSalary);
                    double pagibigDeduction = calculatePagibigDeduction(grossSalary);
                    double taxableIncome = grossSalary - (sssDeduction + philhealthDeduction + pagibigDeduction);
                    double taxDeduction = calculateTaxDeduction(taxableIncome);

                    // Calculate the net salary
                    double netSalary = grossSalary - (sssDeduction + philhealthDeduction + pagibigDeduction + taxDeduction);

                    // Print the payslip with the calculated values
                    System.out.println("MOTOR PH PAYSLIP");
                    System.out.println("--------------------------");
                    System.out.println("Period: " + yearMonth);
                    System.out.println("\nName: " + lastName + ", " + firstName + "\t\tEmployee ID: " + employeeId);
                    System.out.println("Position: " + position);
                    System.out.println("\nGross Salary: " + String.format("%,.2f", grossSalary));
                    System.out.println("\nDeductions:");
                    System.out.println("\tSSS: " + String.format("%,.2f", sssDeduction));
                    System.out.println("\tPhilhealth: " + String.format("%,.2f", philhealthDeduction));
                    System.out.println("\tPagibig: " + String.format("%,.2f", pagibigDeduction));
                    System.out.println("\tWithholding Tax: " + String.format("%,.2f", taxDeduction));
                    System.out.println("\nTotal NET SALARY: " + String.format("%,.2f", netSalary));
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
}
