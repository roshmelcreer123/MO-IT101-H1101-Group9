package mainCode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

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

    private static void viewEntireTable(String path) {
        try {
            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new FileReader(path));

            // Loop through each line in the file until the end is reached
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            // Close the BufferedReader to release system resources
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Print the error to the console
        } catch (IOException e) {
            e.printStackTrace(); // Print the error to the console
        }
    }

    private static void searchForEmployee(String path, String desiredEmployeeId) {
        try {
            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new FileReader(path));

            // Loop through each line in the file until the end is reached
            String line;
            boolean employeeFound = false;
            while ((line = br.readLine()) != null) {
                // Split the line into an array of values using ',' as a delimiter
                String[] csvValues = line.split(",");

                // Check if the first element (employee ID) matches the desired ID
                if (csvValues[0].equals(desiredEmployeeId)) {
                    // Display specific values from the CSV line
                    System.out.println(line);
                    employeeFound = true;
                    break; // Break out of the loop once the employee is found
                }
            }

            if (!employeeFound) {
                System.out.println("Employee not found.");
            }

            // Close the BufferedReader to release system resources
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Print the error to the console
        } catch (IOException e) {
            e.printStackTrace(); // Print the error to the console
        }
    }
}
