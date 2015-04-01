import java.lang.*;
import java.io.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TimeZone;

public class ValueComparator implements Comparator<String>
{
	Map<String, String> base;
	
	public ValueComparator(Map<String, String> base)
	{
		this.base = base;
	}
	
	public int compare(String a, String b)
	{
		if(base.get(a).compareTo(base.get(b)) > 0)
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
	
}
