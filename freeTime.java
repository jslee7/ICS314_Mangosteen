import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class freeTime //finds freeTimes from a group of events
{
		public double start;
		public double end;
		public static double latestEnd;
		public String name;
		
		//constructor
		freeTime(double start, double end, String name)
		{
			this.start = start;
			this.end = end;
			this.name = name;
			latestEnd = 0;
		}
		
		//sorts this start times in ascending order
		public static freeTime[] sortStart(freeTime[] array)
		{//uses insertion sort
			int n = (array.length);
			for(int j = 1;j< n;j++)
			{
				int i = j;
				while((i>0)&&(array[i].start<array[i-1].start))
				{
					freeTime temp = array[i-1];
					array[i-1] = array[i];
					array[i] = temp;
					i--;
				}
			}
			return array;
		}
		//this methods checks an array of objects with ordered start times
		public static double[][] checkEnd(freeTime[] array)
		{
			double[][] freeTimes = new double[array.length-1][2];
			double endComp;
			for(int i = 0;i<array.length-1;i++)
			{	//set the latest end time
				if(array[i].end > latestEnd)
					{endComp = array[i].end;}
				else
					{endComp = latestEnd;}
				
				//check for overlap and if none add a free time to array
				if(endComp > array[i+1].start)
				{
					//move on
				}
				else if(endComp < array[i+1].start)
				{
					//set free time from end to start
					freeTimes[i][0] = endComp;
					freeTimes[i][1] = array[i+1].start;
				}
				else//if I forgot a case
				{System.err.println("unaccounted for case");}
			}
			//returns 2d array with each element containing a start and finish time
			return freeTimes;
		}
		public static void writeBusyFile(double[][] array,PrintWriter pw)
		{
			pw.println("BEGIN:VCALENDER");
			for(int eventWrite = 0; eventWrite < array.length;eventWrite++)
			{
				if(array[eventWrite][0] != 0)
				{
					String begin = getTime(array[eventWrite][0]);
					String end = getTime(array[eventWrite][1]);
					
					pw.println("BEGIN:VFREEBUSY");
					pw.println("DTSTART;TZID=/Pacific/Honolulu:"+begin);
					pw.println("DTEND;TZID=/Pacific/Honolulu:"+end);
					pw.println("SUMMARY:Busy");
					pw.println("END:VFREEBUSY");
				}
			}
			pw.println("END:VCALENDER");
		}
		//takes a double that is in scientific notation and turns it back into proper t string form
		public static String getTime(double timeD)
		{
			//for example 2015033T180000
			String timeS = "";
			//getting rid of scientific notation
			timeS = String.format("%.0f",timeD);
			//putting in T
			timeS = timeS.substring(0, 7)+"T"+timeS.substring(8);
			return timeS;
		}
		//main
		public static void main(String[] args) throws IOException 
		{	String fileName = "freetimePOLF";
			PrintWriter pw = new PrintWriter(new FileWriter("testing.ics"));
			freeTime[] theEvents = new freeTime[4];//this will have to be variable
			double[][] freeTimes = new double[theEvents.length][2];
			theEvents[0] = new freeTime(20150330123000.0,20150330130000.0,"physics.ics");
			theEvents[1] = new freeTime(20150330200000.0,20150331060000.0,"operatingSystems.ics");
			theEvents[2] = new freeTime(20150330093000.0,20150330104500.0,"lunch.ics");
			theEvents[3] = new freeTime(20150330180000.0,20150330203000.0,"fast.ics");
			
			theEvents = sortStart(theEvents);
			freeTimes = checkEnd(theEvents);
			writeBusyFile(freeTimes, pw);
			pw.close();
			//now I need to check for overlap and break times
			
		}

	
}
