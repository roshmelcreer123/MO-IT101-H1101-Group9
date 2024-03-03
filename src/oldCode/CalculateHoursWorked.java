package mainCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class CalculateHoursWorked {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Relative path to the .csv file from the project directory
        String path = "src/resources/motorPhAttendanceRecord.csv";

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. View entire table for specific year and month");
            System.out.println("2. View entire table (all records)");
            System.out.println("3. View specific employee ID");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                // View entire table for specific year and month
                viewEntireTableForMonth(path, scanner);
            } else if (choice == 2) {
                // View entire table (all records)
                viewEntireTable(path);
            } else if (choice == 3) {
                // View specific employee ID
                viewSpecificEmployee(path, scanner);
            } else if (choice == 4) {
                // Exit
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        scanner.close();
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

    private static void viewEntireTable(String path) {
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

                // Print the record
                System.out.println(line + " - Hours Worked: " + hoursWorked);
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


    private static double calculateHoursWorked(String timeIn, String timeOut) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date dateIn = sdf.parse(timeIn);
            Date dateOut = sdf.parse(timeOut);

            // Calculate the difference in milliseconds
            long difference = dateOut.getTime() - dateIn.getTime();

            // Convert the difference to hours
            return (double) difference / (1000 * 60 * 60);

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}