package mainCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalculateNetSalary {

    // Constants for allowances, copied from CalculateGrossSalary3
    private static final double RICE_SUBSIDY = 1500.0;
    private static final double PHONE_ALLOWANCE = 2000.0;
    private static final double CLOTHING_ALLOWANCE = 1000.0;

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

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Choose an Option:");
                System.out.println("1. View Net Salary of Specific Employee");
                System.out.println("2. View All Net Salary Records for a Specific Month");
                System.out.println("3. View Gross Salaries for a Specific Month");
                System.out.println("4. Exit");
                System.out.print("> Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline left-over

                switch (choice) {
                    case 1:
                    	viewNetSalaryOfSpecificEmployee(scanner);
                        break;
                    case 2:
                    	viewAllNetSalariesForSpecificMonth(scanner);
                        break;
                    case 3:
                        System.out.print("Enter year and month (YYYY/MM) to calculate gross salaries: ");
                        String yearMonth = scanner.nextLine();
                        displayGrossSalariesForMonth(yearMonth);
                        break;
                    case 4:
                        System.out.println("Exiting the program.");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please enter a valid choice.");
                        break;
                }
            }
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

                // Calculate total monthly allowances
                double totalMonthlyAllowance = RICE_SUBSIDY + PHONE_ALLOWANCE + CLOTHING_ALLOWANCE;

                // Calculate gross salary (prorated salary + monthly allowances)
                double grossSalary = proratedSalary + totalMonthlyAllowance;

                // Display the result with the total gross salary including allowances
                System.out.println("Employee ID: " + employeeId + "; Hours Worked: " + String.format("%.2f", hoursWorked) +
                        "; Gross Salary: " + String.format("%,.2f", grossSalary));
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

    // Copied from CalculateGrossSalary3
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

    // Copied from CalculateGrossSalary3
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

    // Copied from CalculateGrossSalary3
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

    // Copied from CalculateGrossSalary3
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

}