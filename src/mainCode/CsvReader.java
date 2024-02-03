package mainCode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CsvReader {

	public static void main(String[] args) {
		// Relative path to the .csv file from the project directory
        String path = "src/resources/motorPhEmployeeData.csv";
        // String variable to store each line read from the file
        String line = "";
        
        // Try block for handling file reading and potential exceptions
        try {
            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new FileReader(path));
            
            // Loop through each line in the file until the end is reached
            while((line = br.readLine()) != null) {
                // Split the line into an array of values using ',' as a delimiter
                String[] csvValues = line.split(",");
                
                // Display specific values from the CSV line
                System.out.println(
                		"Employee #: " + csvValues[0] + 
                		"; Last Name: " + csvValues[1] +
                		"; First Name: " + csvValues[2] +
                		"; Birthday: " + csvValues[3] +		
                		"; Address: " + csvValues[4] +
                		"; Phone Number: " + csvValues[5] +
                		"; SSS #: " + csvValues[6] +
                		"; Philhealth #: " + csvValues[7] +
                		"; Tin #: " + csvValues[8] +
                		"; Pag-Ibig #: " + csvValues[9] +
                		"; Status: " + csvValues[10] +
                		"; Position: " + csvValues[11] +
                		"; Immediate Supervisor: " + csvValues[12] +
                		"; Basic Salary: " + csvValues[13] +
                		"; Rice Subsidy: " + csvValues[14] +
                		"; Phone Allowance: " + csvValues[15] +
                		"; Clothing Allowance: " + csvValues[16] +
                		"; Gross Semi-monthly Rate: " + csvValues[17] +
                		"; Gross Hourly Rate: " + csvValues[18]
                		);
            }
            
            // Close the BufferedReader to release system resources
            br.close();
            
        } 
        
        // FileNotFoundException: Handle if the specified file is not found
        catch (FileNotFoundException e) {
            e.printStackTrace(); // Print the error to the console
        } 
        
        // IOException: Handle general input/output errors
        catch (IOException e) {
            e.printStackTrace(); // Print the error to the console
        }

	}

}
