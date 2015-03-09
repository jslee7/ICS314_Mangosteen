import java.io.*; 
import java.lang.*;
import java.util.*;
import sun.misc.SignalHandler;
import sun.misc.Signal;

 public class classification_location_priority
 {
	 Console c = System.console();
	 String input;
	 
 	public static void main(String[] args)
	{
 		
	}
 	
	public String classification()
	{
		input = c.readLine("Classification: ");
		input = input.trim();
		input = input.toUpperCase();
		
		return input;
		
		//returned object needs to be assigned to CLASS in the object
		
	}
	
	public String location()
	{
		String output = "";
		input = c.readLine("Location: ");
		input = input.trim();
		
		if(input.contains(".vcf")
		{
			output = ";ALTREP=";
			output += input;
		}
		else
		{
			output = ":";
			output += input;
		}
		
		return output;
		
		//returned object needs to be assigned to LOCATION in the object
		
	}
	
	public int priority()
	{
		int output = 0;
		
		input = c.readLine("Classification: ");
		input = input.trim();
		
		try
		{
			output = Integer.parseInt(input);
		}
		catch(NumberFormatException e)
		{
			System.out.println("Not a proper input. Please enter a number from 1 to 4.");
		}
		
		return output;
		
		//returned object needs to be assigned to PRIORITY in the object
	}
	
 }
