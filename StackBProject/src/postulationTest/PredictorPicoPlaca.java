package postulationTest;

import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Ginger Preciado 
 * Class that validates if cars can be on road in an specific day and hour. 
 * It depends of its last digit of License Plate Number 
 */

public class PredictorPicoPlaca {
	private String licPlateNumStr, userDateStr, userTimeStr;
	private String messageToUser = "";
	private int lastDigitLicNum;
	private Date userDate, userTime;
	private Calendar calendarDate;
	
	//======================================================================
	/**
	 * Constructor  
	 */
	public PredictorPicoPlaca (){
		System.out.println("Welcome to Predictor Pico & Placa");
		System.out.println("=================================");
		
		System.out.println("Restrictions for Last Digit License Number:");
		System.out.println("	Monday		1, 2");
		System.out.println("	Tuesday		3, 4");
	    System.out.println("	Wedsneday	5, 6");
	    System.out.println("	Thursday	7, 8");
	    System.out.println("	Friday		9, 0");

	    System.out.println("Restriction Hours:");
	    System.out.println("	07H00 - 09H30 at morning");
	    System.out.println("	16H00 - 19H30 at afternoon");		
	}
		
	//======================================================================
	/**
	 * Method that invokes the methods to validate input data
	 */
	public void usePredictorPicoYPlaca (){

		inputData();
		
		printData();

		if (!areUserDataValid()){
			System.out.println("Predictor Response: No. Reason: " + getMessageToUser());
			return;
		}
		if (!canBeOnTheRoad()){
			System.out.println("Predictor Response: No. Reason: " + getMessageToUser());				
			return;
		}
		
		System.out.println("Predictor Response: Yes. \n The Car can be on the road. " + getMessageToUser());
	}	
		
	//======================================================================

	/**
	 * Method that asks the user data   
	 */
	private void inputData(){
		
		licPlateNumStr =
				JOptionPane.showInputDialog( "Welcome to Pico & Placa Predictor. \n\n Input License Plate Number: " );

		userDateStr =
				JOptionPane.showInputDialog( "Input Date (format DD/MM/YYYY) " );
		
		userTimeStr =
				JOptionPane.showInputDialog( "Input Time (24Hrs.Format HH:MM) " );
	}

	//======================================================================
	
	/**
	 * Method that invokes other methods to validate license, date and time input.
	 * @return returns a boolean to control if data is valid
	 */
	private boolean areUserDataValid() {
		if (!isLicenseNumValid())
			return false;

		if (!isUserDateValid())
			return false;

	    if (!isUserTimeValid())
	    	return false;

		return true;
	}

	//======================================================================
	
	/**
	 * Validates if the the license number is a number
	 * @return Returns a boolean to control the result
	 */
	private boolean isLicenseNumValid() {

	    licPlateNumStr = licPlateNumStr.trim();
		
		if ((licPlateNumStr == "") || (licPlateNumStr.equals(""))){ 
	      messageToUser = "It's not a valid License Number";
	      return false;
	    }
	    if (!licPlateNumStr.matches("[0-9]+")){
	      messageToUser = "Please, input only numbers in License Number";    	 
	      return false;
	    }

		return true;
	}	

	//======================================================================

	/**
     * Method that validates if the date given has a format 'dd/mm/yyyy'.
	 * @return
	 */
	private boolean isUserDateValid() {

	  try{
		if ((userDateStr.trim() == null) || (userDateStr.trim().equals(""))){
	      messageToUser = "It's not a valid Date";
  		  return false;
		}

		userDateStr = userDateStr.replace("-", "/");
		userDateStr = userDateStr.replace(".", "/");
		userDateStr = userDateStr.replace(",", "/");
		userDateStr = userDateStr.replace(";", "/");
		userDateStr = userDateStr.replace(":", "/");

		// this parse validates the date. 
		userDate = new SimpleDateFormat("dd/MM/yyyy").parse(userDateStr);

		//The above parse fails when you use day, month or year that not exist, then this validation solves it, example: 34/10/2016
		//It consists in convert date to String and compare with the first string. The date must be equals when the date is correct.
		String dateInverseStr = new SimpleDateFormat("dd/MM/yyyy").format(userDate);
		if (!userDateStr.equals(dateInverseStr)){
			messageToUser = "Please, input a valid Date. Incorrect value for day, month or year.";
			return false;
		}

		return true;
  	  }
	  catch (Exception e){
		messageToUser = "It's not a valid Date. " + e.getMessage();
		return false;
	  }
	}

	//======================================================================

	/**
	 * Method that validates if the time given has a format 'HH:MM'
	 * @return a boolean to control validation
	 */
	private boolean isUserTimeValid() {

	  try{

		userTimeStr = userTimeStr.trim();
		  
		if ((userTimeStr == null) || (userTimeStr.equals(""))){
	      messageToUser = "It's not a valid Time";
  		  return false;
		}

		userTimeStr = userTimeStr.replace("H", ":");
		userTimeStr = userTimeStr.replace("t", ":");
		userTimeStr = userTimeStr.replace(".", ":");
		userTimeStr = userTimeStr.replace("-", ":");
		userTimeStr = userTimeStr.replace(";", ":");
		userTimeStr = userTimeStr.replace(",", ":");
		userTimeStr = userTimeStr.replace("/", ":");
		
		// this parse validates the time. 
		userTime = new SimpleDateFormat("HH:mm").parse(userTimeStr);
		
		//The above parse fails when you use hour or minutes that not exist, then this validation solves it.
		//Time Example 18:10		
		int position = userTimeStr.indexOf(":");
		int hour     = Integer.parseInt( userTimeStr.substring(0, position) );
		int minute   = Integer.parseInt( userTimeStr.substring( (position + 1), userTimeStr.length()) );
		
		if ((hour   >= 24 || hour   < 0) || 
   		    (minute >= 60 || minute < 0) )  {			
		      messageToUser = "Please. Input a valid time. Incorrect value for hour or minute.";
	  		  return false;
			}
		
		return true;
  	  }
	  catch (Exception e){
		messageToUser = "It's not a valid Time. " + e.getMessage();
		return false;
	  }
	}

	//======================================================================

	/**
	 * Method that validates if the last digit of license number is restricted and the hour given too.
	 * @return returns a boolean to specify if the car can be on road
	 */
	private boolean canBeOnTheRoad() {
		
		// validate if the last digit of license have restriction for day of week of date given 
		if (isLastDigitLicNumRestricted()){
		    // if the digit of license is restricted, it validates if the hour given has restriction.
			if (isTimeRestricted()){
			   // if there is a restriction then the car cannot be on road
			   return false;
			}
		}
		
		//if there is not a restriction then the car can be on road
		return true;
	}
	
	//======================================================================

	/**
	 * Uses the last digit of the license plate number given
	 * Gets the date given to get day of week
	 * Sets the calendar with the date given to do the conditions
     *
	 * Each day has a different number of restriction.
     * The Restrictions used for the last Digit License by day are: 
	 * 		Monday 		1, 2
	 * 		Tuesday 	3, 4
	 * 		Wednesday 	5, 6
	 * 		Thursday	7, 8
	 * 		Friday		9, 0
	 * 
	 * @return returns a boolean to specify if the last digit of license number is restricted 
	 */
	private boolean isLastDigitLicNumRestricted() {
   
		boolean isRestricted = false, isWeekend = false;

		calendarDate = Calendar.getInstance();
		calendarDate.setTime(userDate);

	    lastDigitLicNum = Integer.parseInt( licPlateNumStr.substring( licPlateNumStr.length()-1) );
		
		switch (calendarDate.get(Calendar.DAY_OF_WEEK)){
		case Calendar.MONDAY:
			if (lastDigitLicNum == 1 || lastDigitLicNum == 2) 
				isRestricted = true;
			break;
		case Calendar.TUESDAY:
			if (lastDigitLicNum == 3 || lastDigitLicNum == 4 )
				isRestricted = true;
			break;
		case Calendar.WEDNESDAY:
			if (lastDigitLicNum == 5 || lastDigitLicNum == 6 )
				isRestricted = true;
			break;
		case Calendar.THURSDAY:
			if (lastDigitLicNum == 7 || lastDigitLicNum == 8 )
				isRestricted = true;
			break;
		case Calendar.FRIDAY:
			if (lastDigitLicNum == 9 || lastDigitLicNum == 0 )
				isRestricted = true;
			break;
		default: 
			messageToUser = "Cars don't have restrictions at weekends";
			isWeekend = true;
			break;
		}
		
		if (!isRestricted){
			if (!isWeekend) 
		       messageToUser = "Cars with last digit " + lastDigitLicNum + " don't have restrictions today " + new SimpleDateFormat("EEEEEEEE").format(userDate); 
		}
		return isRestricted;
	}

	//======================================================================

	/**
	 * Uses the hour given to validate that it has a restriction
	 * @return returns a boolean to specify if the hour given is restricted 
	 */
	private boolean isTimeRestricted() {
	  try{
		boolean isRestricted = false;
		
		String dayText = new SimpleDateFormat("EEEEEEEE").format(userDate);
		
		String minHourMrnngStr = "06:59";  
		String maxHourMrnngStr = "09:31";  
		String minHourAftrnStr = "15:59";  
		String maxHourAftrnStr = "19:31";  
				
		// this parse validates the time. 
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		userTime = sdf.parse(userTimeStr);
		
		Date minHourMrnng = sdf.parse(minHourMrnngStr);
		Date maxHourMrnng = sdf.parse(maxHourMrnngStr);
		Date minHourAftrn = sdf.parse(minHourAftrnStr);
		Date maxHourAftrn = sdf.parse(maxHourAftrnStr);		

		if ( (userTime.after(minHourMrnng) && userTime.before(maxHourMrnng)) ||
				(userTime.after(minHourAftrn) && userTime.before(maxHourAftrn)) ) {
			messageToUser = "The car with digit " + lastDigitLicNum + " IS ON restriction hours today " + dayText;
			isRestricted  = true;
		}else
			messageToUser = "The car with digit " + lastDigitLicNum + " is out of restriction hours today " + dayText;
		
		return isRestricted;
  	  }
	  catch (Exception e){
		messageToUser = "It's not a valid Time. " + e.getMessage();
		return false;
	  }		
	}

	//======================================================================
	
	/**
	 * @return Returns a String with the message text to the user
	 */
	private String getMessageToUser(){
		return messageToUser;
	}

	/**
	 * print user data
	 */
	private void printData(){
		System.out.println("User Data");
		System.out.println("=================================");
		System.out.println("Licence Number        : " + licPlateNumStr);
		System.out.println("Date                  : " + userDateStr);
		System.out.println("Time                  : " + userTimeStr);
		System.out.println("=================================");
	}
}