import java.lang.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.*;
import java.text.DateFormat;

public class toICS 
{
	PrintWriter pw;
	String sStart, sEnd,classification, location, description;
	String summary;
	int priority;
//takes user input and generates a .ics file for them to use in calender
	public toICS(PrintWriter pw,String summary,String sStart,String sEnd,int priority,String classification, String location, String description) throws IOException
	{//set the variables to passed information
		this.pw = pw;
		this.summary = summary;
		this.sStart = getDT(sStart,sEnd,1);
		this.sEnd = getDT(sStart, sEnd,0);
		this.priority = priority;
		this.location = location;
		this.classification = classification.toUpperCase();
		this.description = description;
	}
	public int createEvent()//generate the event
	{
		int check = checkForErrors(this.sEnd,this.sStart,priority,classification);
		if(check != 0)//if there are no errors in user input
		{
			pw.println("BEGIN:VCALENDAR");
			pw.println("BEGIN:VEVENT");
			pw.println("VERSION:2.0");
			pw.println("DTSTART;TZID=/"+getTimeZone()+":"+sStart);
			pw.println("DTEND;TZID=/"+getTimeZone()+":"+sEnd);
			pw.println("LOCATION:" + location);
			pw.println("SUMMARY:"+ summary);
			pw.println("DESCRIPTION:" + description);
			pw.println("CLASS:" + classification);
			pw.println("PRIORITY:"+priority);
			pw.println("END:VEVENT");
			pw.println("END:VCALENDAR");
		}
		return check;//return a value that says if there are errors
	}
	public String getTimeZone()
	{
		//gets the timezone
		TimeZone tz = TimeZone.getDefault();
		String ID = tz.getID();
		return ID;
	}

	public int checkForErrors(String sEnd,String sStart,int priority,String classification)
	{//checks the user input for possible sources of errors, before it is printed to the file
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
			System.out.println("\nPlease renter your event with above the specified error(s) corrected.");
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
		 if(option == 1 &&!checkDate(sStart, "Start Date or Time"))
		{
		    error = "";
		    return "";
		}
		if(option == 0&&!checkDate(sEnd, "End Date or Time"))
		{
		    error = "";
		    return "";
		}

		if(compareDateTime(sStart, sEnd) > 0)
		{
			error += "Start Date/Time can't be greater than End Date/Time";
		}
		
		if(!error.equals("")&& option == 0)
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
	
	public boolean checkDate(String date,String name)
	{
		//checks for formatting? given start or end?
		boolean check = true;
		SimpleDateFormat formateDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		try {
			Date d = formateDate.parse(date);
			if(!date.equals(formateDate.format(d)))
			{
				check = false;
				System.out.println("Format Error "+name+", must be either in the form MM/dd/yyyy for date or HH:mm:ss for time.");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Unparsable input for "+name+", must be either in the form MM/dd/yyyy for date or HH:mm:ss for time.");
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
			//e.printStackTrace();
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

	public String checkFreeTime(String list)
	{
		String currDir;
		String currEvent;
		int strSize;
		String name;
		String sTime = "";
		//String sDateStr = "";
		String eTime = "";
		//String eDateStr = "";
		String currLine;
		int fileCount = 0;
		String sDate = null;
		String eDate = null;
		String freeStart = "000000";
		String freeEnd = "240000";
		FileInputStream fstream1;
		BufferedReader br1;
		FileInputStream fstream2;
		BufferedReader br2;
		HashMap<String, String> map = new HashMap<String, String>();
		ValueComparator vc = new ValueComparator(map);
		TreeMap<String, String> sorted_map = new TreeMap<String, String>();
		
		currDir = System.getProperty("user.dir");
		list = currDir + "/" + list;
		
		try
		{
			fstream1 = new FileInputStream(list);
			br1 = new BufferedReader(new InputStreamReader(fstream1));
		
			while((currEvent = br1.readLine()) != null)
			{
				if(!currEvent.endsWith(".ics"))
					currEvent += ".ics";
				
				fileCount++;
				
				strSize = currEvent.length();
				
				name = currEvent.substring(0, strSize - 4);
				//System.out.println(name);
				
				fstream2 = new FileInputStream(currEvent);
				br2 = new BufferedReader(new InputStreamReader(fstream2));
				
				currLine = br2.readLine();
				
				double[][] freeTimes = new double[fileCount][2];
				
				for(int i = 0; i < fileCount; i++)
				{
					while(currLine != null)
					{
						if(!(currLine.trim().isEmpty()))
						{
							if(currLine.contains("DTSTART"))
							{
								sTime = currLine.substring(31, 46);
								sDate = sTime.substring(0, 8);
								sTime = sTime.substring(9, sTime.length());
								
								//sDate = DateFormat.parse(sDateStr);
								//System.out.println(sTime);
							}
							else if(currLine.contains("DTEND"))
							{
								eTime = currLine.substring(29, 44);
								eDate = eTime.substring(0, 8);
								eTime = eTime.substring(9, eTime.length());
								//eDate = DateFormat.parse(eDateStr);
								//System.out.println(eDate);
								//System.out.println(eTime);
							}
							
							map.put(sTime, eTime);
							
						}
						currLine = br2.readLine();
					}
				}
				
				
				
				
			}
			System.out.println("before sorting");
			System.out.println(map);
			sorted_map.putAll(map);
			sorted_map.remove("");
			System.out.println("after sorting");
			System.out.println(sorted_map);
			
			
			Iterator it = sorted_map.entrySet().iterator();
			String[] sTimeArray = new String[fileCount];
			String[] eTimeArray = new String[fileCount];
			String[][] fTimeArray = new String[fileCount+1][2];
			int lastCount = 0;
			int arrayCount = 0;
			
			while (it.hasNext())
			{
				Map.Entry ent = (Map.Entry)it.next();
				sTimeArray[arrayCount] = (String)ent.getKey();
				eTimeArray[arrayCount] = (String)ent.getValue();
				//System.out.println(sTimeArray[arrayCount]);
				//System.out.println(eTimeArray[arrayCount]);
				arrayCount++;				
			}
			
			if(freeStart.compareTo(sTimeArray[0])  <= 0)
			{			
				//freeStart = sDate + "T000000";
				fTimeArray[0][0] = freeStart;
				fTimeArray[0][1] = sTimeArray[0];
				lastCount++;
				//System.out.println(fTimeArray[0][0]);
				//System.out.println(fTimeArray[0][1]);
			}
			
			for(int i = 0; i < fileCount-1; i++)
			{
				if(eTimeArray[i].compareTo(sTimeArray[i+1]) < 0)
				{
					//end time is not overlapping, put it in array
					fTimeArray[i+1][0] = eTimeArray[i];
					fTimeArray[i+1][1] = sTimeArray[i+1];
					//System.out.println(fTimeArray[i+1][0]);
					//System.out.println(fTimeArray[i+1][1]);
					lastCount++;
				}
				else
				{
					//overlapping, ignore
				}
				
			}
			if(freeEnd.compareTo(eTimeArray[fileCount-1]) >= 0)
			{
				fTimeArray[fileCount][0] = eTimeArray[fileCount-1];
				fTimeArray[fileCount][1] = freeEnd;
				lastCount++;
				//System.out.println(fTimeArray[fileCount-1][0]);
				//System.out.println(fTimeArray[fileCount-1][1]);
			}
			System.out.println(lastCount);
			PrintWriter pw = new PrintWriter("Free Time.ics");
			pw.println("BEGIN:VCALENDAR");
			for(int i = 0; i < lastCount; i++)
			{
				//if(fTimeArray[i][0].isEmpty() && fTimeArray[i][1].isEmpty())
				//{
					//do nothing
				//}
				//else
				//{
					pw.println("BEGIN:VFREEBUSY");
					pw.println("DTSTART;TZID=/Pacific/Honolulu:"+sDate+"T"+fTimeArray[i][0]);
					pw.println("DTEND;TZID=/Pacific/Honolulu:"+sDate+"T"+fTimeArray[i][1]);
					System.out.println(fTimeArray[i][0]);
					System.out.println(fTimeArray[i][1]);
					pw.println("SUMMARY:Free Time");
					pw.println("END:VFREEBUSY");
				//}
			}
			pw.println("END:VCALENDAR");
			
			pw.close();
			//toICS free = new toICS(new PrintWriter("Free Time.ics"), "Free Time", freeStart, freeEnd, 0, "N/A", "Free Time");
			//free.createEvent();
			
			System.out.println("The date for all the events on the list is " + sDate);
			//System.out.println("Free time slots for the day are:");
		}
		catch(Exception e)
		{
			
		}
		
		return null;
		
	}
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
		PrintWriter pw;
		String fileName = "";
		String sStart,sEnd,classification,sPriority, location,description;
		String summary;
		String listName;
		String freeTime;
		toICS event = null;
		int checkFree;
		int priority = 0, check = 1;
		
		Console c = System.console();
		Scanner keyboard = new Scanner(new InputStreamReader(System.in));
		System.out.println("This program generates .ics files containing calender events.");
		System.out.println("It will store the file in the directory this program resides.");
		System.out.println("\nShould you chose to name the file with the same name as existing.");
		System.out.println(".ics file in that directory, this program will overwrite it");
		System.out.println("\nThe name of the file will also be taken as the name of the event.");
		//maybe add error checking to get rid of .ics
		
		
		try 
		{
			while(check == 1)
			{
				//user event input start
				System.out.println("Please enter the name of the event file(without .ics ending).");
				//fileName = keyboard.next();
				fileName = c.readLine();
				summary = fileName;
				pw = new PrintWriter(new FileWriter(fileName+".ics"));
				System.out.println("Please enter a start date in the form mm/dd/yyyy");
				//sStart = keyboard.next();
				sStart = c.readLine();
				System.out.println("Please enter a start time in the form hh:mm:ss");
				//sStart = sStart+" "+keyboard.next();
				sStart = sStart + " " + c.readLine();
				//System.out.println(sStart);
				System.out.println("Please enter a end date in the form mm/dd/yyyy");
				//sEnd = keyboard.next();
				sEnd = c.readLine();
				System.out.println("Please enter a end time in the form hh:mm:ss");
				sEnd = sEnd + " " + c.readLine();
				//sEnd = sEnd +" "+keyboard.next();
				System.out.println("Please enter the location");
				//location = keyboard.next();
				location = c.readLine();
				System.out.println("Please enter a Summary of your event");
				System.out.println("to indicate that you finished summarizing your event,");
				System.out.println("please type the word \"end\" on a new line");
				description = writeString(keyboard);
				System.out.println("Would you like this event public, private, or confidential?");
				//classification = keyboard.next();
				classification = c.readLine();
				System.out.println("Would you like to enter a priority for this event?");
				System.out.println("If yes, please enter any number from 1-9 with 1 being the highest priority");
				System.out.println("If no, please enter 0");
				try{priority = keyboard.nextInt();}
				catch (InputMismatchException e) {
				System.out.println("Entered priority value is not an integer.");
				}
				//user event input end
				//create event
				event = new toICS(pw,summary,sStart,sEnd,priority,classification,location, description);
				int check2 = event.createEvent();
				//make sure there are no errors
				if(check2 == 0)
				{/*do nothing*/}
				else
				{
					//create additional event?
					System.out.println("Would you like to enter another event?");
					System.out.println("If so enter 1, if not enter 0");
					check = keyboard.nextInt();
				}
				pw.close(); 
				
				
				
			}
			//check for free time
			System.out.println("Would you like to check for free time between a list of events? Type 1 for Yes, 0 for No.");
			checkFree = keyboard.nextInt();
			if(checkFree == 1)
			{
				System.out.println("Please enter the list file name. List file must be within the current directory. ");
				listName = c.readLine();
				freeTime = event.checkFreeTime(listName);
				System.out.println(freeTime);
			}
		}
		catch (IOException e) {System.out.println("unable to create file with name: "+fileName);}
		
	}
}
