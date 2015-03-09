import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TimeZone;

public class toICS 
{
	PrintWriter pw;
	String summary, timeEnd, timeStart, priority, protection;
//takes user input and generates a .ics file for them to use in calender
	public toICS(PrintWriter pw,String summary,String timeStart,String timeEnd,String priority,String protection) throws IOException
	{
		this.pw = pw;
		this.summary = summary;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.priority = priority;
		this.protection = protection;
	}
	public void createEvent()
	{
		pw.println("BEGIN: VEVENT");
		pw.println("VERSION:2.0");
		pw.println("SUMMARY:"+summary);
		pw.println("DTSTART;TZID=/"+getTimeZone()+":"+timeStart);
		pw.println("DTSTART;TZID=/"+getTimeZone()+":"+timeEnd);
		//body
		pw.println("CLASS:"+protection);
		pw.println("END:VEVENT");
	}
	public String getTimeZone()
	{
		TimeZone tz = TimeZone.getDefault();
		String ID = tz.getID();
		return ID;
	}
	
	public static void main(String[] args) 
	{
		//random test values just to see if it generates a file
		//it does and it saves it in the current d
		String fileName = "",summary = "meh", timeEnd = "6", timeStart = "5",priority = "1",protection = "PRIVATE";
		int check = 1;
		
		Scanner keyboard = new Scanner(System.in);
		System.out.println("This is a program that generates .ics files which can be uploaded to a calender.");
		System.out.println("The .ics files will be saved to the directory that this program resides in.");
		System.out.println("Please enter the name of the file you wish to write to(don't include .ics).");
		fileName = keyboard.next();
		PrintWriter pw;
		try 
		{
			pw = new PrintWriter(new FileWriter(fileName+".ics"));
			pw.println("BEGIN:VCALENDER"); //only neccesary once, but not quite sure if we need this
			while(check == 1)
			{
				System.out.println("Please enter a Summary of your event");
				//user enters summary,timeStart,timeEnd,priority, level of protection(public,private,protected)
				toICS event = new toICS(pw,summary,timeEnd,timeStart,priority,protection);
				event.createEvent();
				System.out.println("Would you like to enter another event?");
				System.out.println("If so enter 1, if not enter 0");
				check = keyboard.nextInt();
			}
			
			pw.println("END:VCALENDER"); //only neccesary once
			pw.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}
}

