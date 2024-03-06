package mainCode;

public class EmployeeDetails {
	
	 public String employeeId;
	    public String lastName;
	    public String firstName;
	    public String birthday;
	    public String address;
	    public String phoneNumber;
	    public String sssNumber;
	    public String philhealthNumber;
	    public String tinNumber;
	    public String pagIbigNumber;
	    public String status;
	    public String position;
	    private String immediateSupervisor;
	    public double basicSalary;
	    public double riceSubsidy;
	    public double phoneAllowance;
	    public double clothingAllowance;
	    public double grossSemiMonthlyRate;
	    public double hourlyRate;

	    // Constructor
	    public EmployeeDetails(String employeeId, String lastName, String firstName, 
	    					   String birthday, String address,
	    					   String phoneNumber, String sssNumber, String philhealthNumber,
	    					   String tinNumber, String pagIbigNumber, String status,
	    					   String position, String immediateSupervisor, double basicSalary,double riceSubsidy,
	    					   double phoneAllowance, double clothingAllowance, double grossSemiMonthlyRate, 
	    					   double hourlyRate) {
	    	
	    	
	        this.employeeId = employeeId;
	        this.lastName = lastName;
	        this.firstName = firstName;
	        this.birthday = birthday;
	        this.address = address;
	        this.phoneNumber = phoneNumber;
	        this.sssNumber = sssNumber;
	        this.philhealthNumber = philhealthNumber;
	        this.tinNumber = tinNumber;
	        this.pagIbigNumber = pagIbigNumber;
	        this.status = status;
	        this.position = position;
	        this.immediateSupervisor = immediateSupervisor;
	        this.basicSalary = basicSalary;
	        this.riceSubsidy = riceSubsidy;
	        this.phoneAllowance = phoneAllowance;
	        this.clothingAllowance = clothingAllowance;
	        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
	        this.hourlyRate = hourlyRate;
	        
	    }

	    // Getters (and possibly setters)
	    public String getEmployeeId() { return employeeId; }
	    public String getLastName() { return lastName; }
	    public String getFirstName() { return firstName; }
	    public String getBirthday() { return birthday; }
	    public String getAddress() { return address; }
	    public String getPhoneNumber() { return phoneNumber; }
	    public String getSSSNumber() { return sssNumber; }
	    public String getPhilhealthNumber() { return philhealthNumber; }
	    public String getTinNumber() { return tinNumber; }
	    public String getpagIbigNumber() { return pagIbigNumber; }
	    public String getStatus() { return status; }
	    public String getPosition() { return position; }
	    public String getImmediateSupervisor() { return immediateSupervisor; }
	    public double getBasicSalary() { return basicSalary; }
	    public double getRiceSubsidy() { return riceSubsidy; }
	    public double getPhoneAllowance() { return phoneAllowance; }
	    public double getClothingAllowance() { return clothingAllowance; }
	    public double getGrossSemiMonthlyRate() { return grossSemiMonthlyRate; }
	    public double gethourlyRate() { return hourlyRate; }
	    

	    // You might want to override toString() method for easy printing.
	    @Override
	    public String toString() {
	        // Assuming all values are already properly formatted as strings
	        return String.format("| %-10s | %-15s | %-15s | %-10s | %-77s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-30s | %-30s | %-12s | %-12s | %-15s | %-20s | %-25s | %-10s |",
	                employeeId, lastName, firstName, birthday, address, phoneNumber, sssNumber, philhealthNumber, tinNumber, pagIbigNumber, status, position, immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, grossSemiMonthlyRate, hourlyRate);
	    }
}
