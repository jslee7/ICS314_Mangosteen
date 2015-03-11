import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class toICS 
{
	PrintWriter pw;
	String sStart, sEnd,classification;
	String summary;
	int priority;
//takes user input and generates a .ics file for them to use in calender
	public toICS(PrintWriter pw,String summary,String sStart,String sEnd,int priority,String classification) throws IOException
	{
		this.pw = pw;
		this.summary = summary;
		this.sStart = getDT(sStart,sEnd,1);
		this.sEnd = getDT(sStart, sEnd,0);
		this.priority = priority;
		this.classification = classification.toUpperCase();
	}
	public int createEvent()
	{
		int check = checkForErrors(sEnd,sStart,priority,classification);
		if(check != 0)
		{
			pw.println("BEGIN:VEVENT");
			pw.println("VERSION:2.0");
			pw.println("DTSTART;TZID=/"+getTimeZone()+":"+sStart);
			pw.println("DTEND;TZID=/"+getTimeZone()+":"+sEnd);
			pw.println("SUMMARY:"+ summary);
			pw.println("CLASS:" + classification);
			pw.println("PRIORITY:"+priority);
			pw.println("END:VEVENT");
		}
		return check;
	}
	public String getTimeZone()
	{
		//gets the timezone
		TimeZone tz = TimeZone.getDefault();
		String ID = tz.getID();
		return ID;
	}

	public int checkForErrors(String sEnd,String sStart,int priority,String classification)
	{
		int check = 1;
		if (sEnd.equals("")||sStart.equals(""))
		{
			check = 0;
		}
		if(priority<0 || priority >9)
		{
			System.out.println("Error: priority not in range between 0 and 9");
			check = 0;
		}
		if(!classification.equals("PUBLIC")&&!classification.equals("PRIVATE")&&!classification.equals("PROTECTED"))
		{
			System.out.println(classification+": not accepted, please enter public, private, or protected");
			check = 0;
		}
		if(check == 0)
		{
			System.out.println("Please renter your event with the specified error(s) corrected.");
		}
		return check;
	}
	//start Fuquan
	public String getDT(String userStart,String userEnd, int option) 
	{
		// TODO Auto-generated method stub
		String error = "";
		String sStart = userStart;
		String sEnd = userEnd;
		if(!checkDate(sStart))
		{
			error = "Invalid Start Date and Time\n";
		}
		if(!checkDate(sEnd))
		{
			error += "Invalid End Date and Time\n";
		}
		
		if(!error.equals(""))
		{
			System.out.println(error);
			return "";
		}
		
		if(compareDateTime(sStart, sEnd) > 0)
		{
			error += "Start Date/Time can't be greater than End Date/Time";
		}
		
		if(!error.equals(""))
		{
			System.out.println(error);
			return "";
		}
		if(option == 1)
		{
			String array[] = sStart.split(" ");
			return convertDateTime(array[0], array[1]);
		}
		else
		{
			//splits sEnd between date and time using the space
			String array[] = sEnd.split(" ");
			return convertDateTime(array[0], array[1]);
		}
	}
	
	public boolean checkDate(String date)
	{
		//checks for formatting? given start or end?
		boolean check = true;
		SimpleDateFormat formateDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		try {
			Date d = formateDate.parse(date);
			if(!date.equals(formateDate.format(d)))
			{
				check = false;
				System.out.println("Formate Error. Please try again");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			check = false;
		}
		
		return check;
	}
	
	public int compareDateTime(String start, String end)
	{
		//checks the overlapping of end and start given end and start
		int check = 0;
		try {
			Date dtStart = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(start);
			Date dtEnd = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(end);
			check = dtStart.compareTo(dtEnd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return check;
	}
	public String convertDateTime(String date, String time)
	{
		//returns the calender form given date and time given either end or start
		String result = "";
		String[] tempDate = date.split("/");
		String[] tempTime = time.split(":");
		result = tempDate[2] + tempDate[0] + tempDate[1] + "T" + tempTime[0] + tempTime[1] + tempTime[2];
		return result;
	}
	//end Fuquan

	private static String writeString(Scanner keyboard)
	{
		String temp, array = "";
		while(!(temp = keyboard.nextLine()).equalsIgnoreCase("end"))
		{
			array = array +" "+ temp;
		}
		array = array.trim();
		return array;
	}
	public static void main(String[] args) 
	{
		String fileName, sStart,sEnd,classification,sPriority;
		String summary;
		int priority = 0, check = 1;
		
		Scanner keyboard = new Scanner(new InputStreamReader(System.in));
		System.out.println("Please enter the name of the file you wish to write to.");
		fileName = keyboard.next();
		PrintWriter pw;
		try 
		{
			pw = new PrintWriter(new FileWriter(fileName+".ics"));
			//pw.println("BEGIN:VCALENDER"); //only neccesary once//do we need?
			while(check == 1)
			{
				//user event input start
				System.out.println("Please enter a start date in the form mm/dd/yyyy");
				sStart = keyboard.next();
				System.out.println("Please enter a start time in the form hh:mm:ss");
				sStart = sStart+" "+keyboard.next();
				System.out.println("Please enter a end date in the form mm/dd/yyyy");
				sEnd = keyboard.next();
				System.out.println("Please enter a end time in the form hh:mm:ss");
				sEnd = sEnd +" "+keyboard.next();
				System.out.println("Please enter a Summary of your event");
				System.out.println("to indicate that you finished summarizing your event,");
				System.out.println("please type the word \"end\" on a new line");
				summary = writeString(keyboard);
				System.out.println("Would you like this event public, private, or confidential?");
				classification = keyboard.next();
				System.out.println("Would you like to enter a priority for this event?");
				System.out.println("If yes, please enter any number from 1-9 with 1 being the highest priority");
				System.out.println("If no, please enter 0");
				sPriority = keyboard.next();
				//user event input end
				//create event
				toICS event = new toICS(pw,summary,sStart,sEnd,priority,classification);
				int check2 = event.createEvent();
				//make sure there are no errors
				if(check2 == 0)
					break;
				else
				{
					//create additional event?
					System.out.println("Would you like to enter another event?");
					System.out.println("If so enter 1, if not enter 0");
					check = keyboard.nextInt();
				}
			}
			
			//pw.println("END:VCALENDER"); //only neccesary once//needed?
			pw.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}
}

