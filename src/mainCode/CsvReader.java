package mainCode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static void main(String[] args) {
        // Relative path to the .csv file from the project directory
        String path = "src/resources/motorPhEmployeeData.csv";

        // Create a scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        // Loop to keep the program running until the user decides to exit
        while (true) {
            // Prompt the user to choose an option
            System.out.println("Choose an option:");
            System.out.println("1. View Entire Table Data");
            System.out.println("2. Search for an Employee");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    // View Entire Table Data
                    viewEntireTable(path);
                    break;
                case 2:
                    // Search for an Employee
                    System.out.print("Enter employee ID Number: ");
                    String desiredEmployeeId = scanner.nextLine();
                    searchForEmployee(path, desiredEmployeeId);
                    break;
                case 3:
                    // Exit the program
                    System.out.println("Exiting the program.");
                    scanner.close();
                    return; // Exit the main method
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }


    public static void viewEntireTable(String path) {
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

    /**
     * Parses a CSV line, taking into account quoted fields that may contain commas.
     * @param line The CSV line to parse.
     * @return A List of values for each column.
     */
    public static List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes; // Toggle the inQuotes flag
            } else if (c == ',' && !inQuotes) {
                // End of a field
                values.add(sb.toString());
                sb.setLength(0); // Clear the StringBuilder
            } else {
                sb.append(c);
            }
        }

        // Add the last field
        values.add(sb.toString());
        return values;
    }




    public static void searchForEmployee(String path, String desiredEmployeeId) {
    	String formatString = "| %-10s | %-15s | %-15s | %-9s | %-76s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-30s | %-30s | %-12s | %-12s | %-15s | %-20s | %-23s | %-11s |%n";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            boolean employeeFound = false;

            // We delay printing the header until we find a match.
            String header = String.format(formatString, "Employee #", "Last Name", "First Name", "Birthday", 
            											"Address", "Phone Number", "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", 
            											"Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", 
            											"Phone Allowance", "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate");

            while ((line = br.readLine()) != null) {
                List<String> values = parseCsvLine(line);

                if (!values.isEmpty() && values.get(0).equals(desiredEmployeeId)) {
                    // Print the header only if a matching employee is found
                    if (!employeeFound) {
                        System.out.print(header); // Print the header now
                        employeeFound = true;
                    }
                    System.out.printf(formatString, values.toArray(new String[0]));
                    break; // Break out of the loop once the employee is found
                }
            }

            if (!employeeFound) {
                System.out.println("Employee not found.");
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	}
    }
