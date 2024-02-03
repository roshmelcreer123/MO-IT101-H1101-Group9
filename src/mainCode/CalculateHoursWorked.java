package mainCode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalculateHoursWorked {
	
	// Need to use Attendance Record .csv file, and read through it
	// Need to parse date, time-in, and time out to int, double, double. 
	
	public static void main(String[] args) {
        // Relative path to the .csv file from the project directory
        String path = "src/resources/motorPhAttendanceRecord.csv";
        // String variable to store each line read from the file
        String line = "";

        // Try block for handling file reading and potential exceptions
        try {
            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new FileReader(path));

            // Loop through each line in the file until the end is reached
            while ((line = br.readLine()) != null) {
                // Split the line into an array of values using ',' as a delimiter
                String[] csvValues = line.split(",");

                // Display specific values from the CSV line
                		System.out.println(
                				"Employee #: " + csvValues[0] +
                                "; Last Name: " + csvValues[1] +
                                "; First Name: " + csvValues[2] +
                                "; Date: " + csvValues[3] +
                                "; Time-in: " + csvValues[4] +
                                "; Time-out: " + csvValues[5] +

                                // Calculate hours worked based on "Time-in" and "Time-out" columns
                                calculateAndPrintHoursWorked(csvValues[4], csvValues[5])
                );
            }

            // Close the BufferedReader to release system resources
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Print the error to the console
        } catch (IOException e) {
            e.printStackTrace(); // Print the error to the console
        }
    }
	
	// Function to calculate and print hours worked
    private static String calculateAndPrintHoursWorked(String timeIn, String timeOut) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date dateIn = sdf.parse(timeIn);
            Date dateOut = sdf.parse(timeOut);

            // Calculate the difference in milliseconds
            long difference = dateOut.getTime() - dateIn.getTime();

            // Convert the difference to hours
            double hoursWorked = (double) difference / (1000 * 60 * 60);

            return "\nHours Worked: " + hoursWorked;
        } catch (ParseException e) {
            e.printStackTrace();
            return "Failed to calculate hours worked for this record.";
        }
    }
}
