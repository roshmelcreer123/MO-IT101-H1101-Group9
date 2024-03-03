package oldCode;

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

public class CalculateHoursWorked3 {

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
                // Format the hours with two decimal places
                String formattedHours = String.format("%.2f", entry.getValue());
                System.out.println(entry.getKey() + ": " + formattedHours + " hours");
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

            // Skip the header line
            String header = br.readLine();

            // Map to store total hours worked per day
            Map<String, Double> dailyHoursMap = new HashMap<>();

            // Map to store total hours worked per week
            Map<Integer, Double> weeklyHoursMap = new HashMap<>();

            // Loop through each line in the file until the end is reached
            String line;
            while ((line = br.readLine()) != null) {
                String[] csvValues = line.split(",");

                // Parse the date from the CSV
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Date recordDate = sdf.parse(csvValues[3]);

                // Get the year, month, and day of week from the record date
                Calendar recordCalendar = Calendar.getInstance();
                recordCalendar.setTime(recordDate);
                int recordYear = recordCalendar.get(Calendar.YEAR);
                int recordMonth = recordCalendar.get(Calendar.MONTH);
                int recordDayOfWeek = recordCalendar.get(Calendar.DAY_OF_WEEK);

                // Calculate and print hours worked
                double hoursWorked = calculateHoursWorked(csvValues[4], csvValues[5]);
                System.out.println(line + " - Hours Worked: " + hoursWorked);

                // Update daily hours map
                String day = sdf.format(recordDate);
                dailyHoursMap.put(day, dailyHoursMap.getOrDefault(day, 0.0) + hoursWorked);

                // Update weekly hours map
                int weekNumber = (recordCalendar.get(Calendar.WEEK_OF_YEAR) - 1) % 4; // 0-based index of the week number in the year
                weeklyHoursMap.put(weekNumber, weeklyHoursMap.getOrDefault(weekNumber, 0.0) + hoursWorked);
            }

            // Print total hours worked per day
            System.out.println("\nTotal Hours Worked per Day:");
            for (Map.Entry<String, Double> entry : dailyHoursMap.entrySet()) {
                // Format the hours with two decimal places
                String formattedHours = String.format("%.2f", entry.getValue());
                System.out.println(entry.getKey() + ": " + formattedHours + " hours");
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

            // Prompt user for additional option
            System.out.println("\nChoose an additional option:");
            System.out.println("1. List per day for the month");
            System.out.println("2. Total hours worked for the month");
            System.out.println("3. Hours Worked by Employee " + employeeId + " per Week");
            System.out.print("Enter your choice: ");
            int additionalChoice = scanner.nextInt();

            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new FileReader(path));

            // Skip the header line
            String header = br.readLine();

            // Variables to store total hours worked by the employee for the month and week
            double totalHoursThisMonth = 0;
            Map<Integer, Double> weeklyHoursMap = new HashMap<>();

            // Map to store total hours worked per day
            Map<String, Double> dailyHoursMap = new HashMap<>();

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
                    // Update total hours for the month
                    totalHoursThisMonth += hoursWorked;

                    // Calculate weekly hours
                    int weekNumber = (recordCalendar.get(Calendar.WEEK_OF_YEAR) - 1) % 4; // 0-based index of the week number in the month
                    weeklyHoursMap.put(weekNumber, weeklyHoursMap.getOrDefault(weekNumber, 0.0) + hoursWorked);

                    // Update daily hours map
                    String day = sdf.format(recordDate);
                    dailyHoursMap.put(day, dailyHoursMap.getOrDefault(day, 0.0) + hoursWorked);
                }
            }

            // Print the chosen information based on the additional choice
            if (additionalChoice == 1) {
                // List per day for the month
                System.out.println("\nHours Worked by Employee " + employeeId + " per Day for the Month:");
                for (Map.Entry<String, Double> entry : dailyHoursMap.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue() + " hours");
                }
            } else if (additionalChoice == 2) {
                // Total hours worked for the month
                System.out.println("\nTotal Hours Worked by Employee " + employeeId + " for the Month: " + totalHoursThisMonth + " hours");
            } else if (additionalChoice == 3) {
                // Hours Worked by Employee per Week
                System.out.println("\nHours Worked by Employee " + employeeId + " per Week:");
                for (Map.Entry<Integer, Double> entry : weeklyHoursMap.entrySet()) {
                    System.out.println("Week " + (entry.getKey() + 1) + ": " + entry.getValue() + " hours");
                }
            } else {
                System.out.println("Invalid additional option. Please enter a valid option.");
            }

            // Add a newline for spacing
            System.out.println();

            // Close the BufferedReader to release system resources
            br.close();

        } catch (IOException | ParseException e) {
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

            // Convert the difference to hours with two decimal places
            double hoursWorked = Double.parseDouble(String.format("%.2f", (double) difference / (1000 * 60 * 60)));

            return hoursWorked;

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

