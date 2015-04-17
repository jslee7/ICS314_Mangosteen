package JUnitCalTests;

import static org.junit.Assert.assertEquals;

//import org.junit.Test;

import junit.framework.TestCase;

public class TestFailure extends TestCase 
{
	toICS test  = new toICS();
		
	public void test_getTimeZone()
	{
		assertEquals(test.getTimeZone(), "Pacific/Honolulu");	
	}
	public void test_checkForErrors() throws Exception 
	{
		assertEquals(test.checkForErrors("20150331T033300", "20160331T033300", 0, "PUBLIC"),1);
		assertEquals(test.checkForErrors("20150331T033300", "", 0, "PUBLIC"),0);
		assertEquals(test.checkForErrors("","20150331T033300", 0, "PUBLIC"),0);
		assertEquals(test.checkForErrors("20150331T033300", "20160331T033300", 20, "PUBLIC"),0);
		assertEquals(test.checkForErrors("20150331T033300", "20160331T033300", 0, "OPEN"),0);	
	}
	
	public void test_getDT()
	{
		assertEquals(test.getDT("03/31/2015 03:31:11", "03/31/2015 04:31:11" , 1),"20150331T033111");
		assertEquals(test.getDT("03/31/2015 03:31:11", "03/31/2015 04:31:11" , 0),"20150331T043111");
		assertEquals(test.getDT("03/31/2015 04:31:11", "03/31/2015 03:31:11" , 0),"");
		//no need for more test, because we already tested format problems in checkDate
		//and when option = 1 not 0, it doesn't check for greater than start error
		//to prevent double error printing
		
	}
	
	public void test_checkDate()
	{
		assertEquals(test.checkDate("03/31/2015 03:31:11","start"), true);
		assertEquals(test.checkDate("03/32/2015 03:31:11", "start"), false);
		assertEquals(test.checkDate("03/31/2015 03:31:111","start"), false);
		assertEquals(test.checkDate("3/31/2015 03:31:11","start"), false);
		assertEquals(test.checkDate("03/31/2015 3:31:11","start"), false);
		assertEquals(test.checkDate("3 31 2015 03 31 11", "start"), false);
	}
	
	public void test_compareDateTime()
	{
		assertEquals(test.compareDateTime("03/31/2015 03:31:11", "03/31/2015 03:31:12"), -1);
		assertEquals(test.compareDateTime("03/31/2015 03:31:11", "03/31/2015 03:31:11"), 0);
		assertEquals(test.compareDateTime("03/31/2015 03:31:11", "03/31/2015 03:31:10"), 1);
	}
	
	public void test_convertDateTime()
	{
		assertEquals(test.convertDateTime("03/31/2015", "03:31:12"), "20150331T033112");
		assertEquals(test.convertDateTime("11/11/1111", "11:11:11"), "11111111T111111");
		assertEquals(test.convertDateTime("03/31/2015", "03:31:13"), "20150331T033113");
	}
	

	public void test_findFreeTime()
	{
		String[] sTimeArray = {"060000"};
		String[] eTimeArray  = {"120000"};
		String[][] array = toICS.findFreeTime(sTimeArray, eTimeArray, 1);
		assertEquals(array[0][0],"000000");
		assertEquals(array[0][1],"060000");
		assertEquals(array[1][0],"120000");
		assertEquals(array[1][1],"240000");
	}
	public void test_checkFreeTime()
	{
		//must have stuff.txt in bin folder in eclipse, or same folder in other places 
		//for this to work
		String[][] array = {{"000000","060000"},{"070000","090807"},{"100807","240000"}};
		System.out.println(array[2][1]);
		String[][] array2 = test.checkFreeTime("stuff.txt",true);
		assertEquals(array[0][0],array2[0][0]);
		assertEquals(array[0][1],array2[0][1]);
		assertEquals(array[1][0],array2[1][0]);
		assertEquals(array[1][1],array2[1][1]);
		assertEquals(array[2][0],array2[2][0]);
		assertEquals(array[2][1],array2[2][1]);
	}
	public void test_findMeetingTime()
	{
		String[][] testArray = null;
		
		//check for times with no overlap
		testArray = toICS.findMeetingTime("000000", "070000", "060000","120000", 0, 70000, 60000, 120000, 1, 1, 0, true);
		assertEquals(testArray[0][0],null);
		
		//check for times with 1 hr overlap
		testArray = toICS.findMeetingTime("000000", "060000", "070000","120000", 0, 60000, 70000, 120000, 1, 1, 0, true);
		assertEquals(testArray[0][0],"060000");
		assertEquals(testArray[0][1],"070000");
		
	}
}