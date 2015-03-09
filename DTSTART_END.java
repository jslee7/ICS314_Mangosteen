import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class DTSTART_END {
	
	private JFrame frame;
	private JTextField dStart;
	private JTextField dEnd;
	private JTextField tStart;
	private JTextField tEnd;
	public DTSTART_END()
	{
		Start();
	}
	
	public static void main(String[] args) {
		new DTSTART_END();
	}

	private void Start()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 500);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		JLabel start_label = new JLabel("Start Date and Time: ");
		start_label.setBounds(20, 20, 200, 23);
		frame.add(start_label);
		
		JLabel end_label = new JLabel("End Date and Time: ");
		end_label.setBounds(20, 60, 200, 23);
		frame.add(end_label);
		
		dStart = new JTextField();
		dStart.setToolTipText("mm/dd/yyyy");
		dStart.setBounds(150, 20, 100, 23);
		dStart.setColumns(10);
		frame.add(dStart);
		
		dEnd = new JTextField();
		dEnd.setToolTipText("mm/dd/yyyy");
		dEnd.setBounds(150, 60, 100, 23);
		dEnd.setColumns(10);
		frame.add(dEnd);
		
		tStart = new JTextField();
		tStart.setToolTipText("hh:mm:ss");
		tStart.setBounds(300, 20, 100, 23);
		frame.add(tStart);
		
		tEnd = new JTextField();
		tEnd.setToolTipText("hh:mm:ss");
		tEnd.setBounds(300, 60, 100, 23);
		frame.add(tEnd);
		
		JLabel startTip = new JLabel("Ex: 03/08/2015");
		startTip.setBounds(160, 40, 200, 23);
		frame.add(startTip);
		
		JLabel endTip = new JLabel("Ex: 13:55:00");
		endTip.setBounds(310, 40, 200, 23);
		frame.add(endTip);
		
		JButton submit = new JButton("Sumbit");
		submit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String error = "";
				String sStart = dStart.getText()+" "+tStart.getText();
				String sEnd = dEnd.getText()+" "+tEnd.getText();
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
					JOptionPane.showMessageDialog(null, error);
					return;
				}
				
				if(compareDateTime(sStart, sEnd) > 0)
				{
					error += "Start Date/Time can't be greater than End Date/Time";
				}
				
				if(!error.equals(""))
				{
					JOptionPane.showMessageDialog(null, error);
					return;
				}
				System.out.println(convertDateTime(dStart.getText(), tStart.getText()));
				System.out.println(convertDateTime(dEnd.getText(), tEnd.getText()));
			}
		});
		submit.setBounds(150, 300, 200, 23);
		frame.add(submit);
	}
	
	//check user input is valid or not
	//parameter- date[String]: include date and time of start/end
	//return- check[boolean]: true if date is valid, false otherwise
	public boolean checkDate(String date)
	{
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
	
	//compare the date/time for start and end
	//parameter- start[String]: include date and time of start date/time
	//parameter- end[String]: include date and time of end date/time
	//return- check[int]: greater than 0 is invalid
	public int compareDateTime(String start, String end)
	{
		int check = 0;
		try {
			Date dtStart = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(start);
			Date dtEnd = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(end);
			check =  dtStart.compareTo(dtEnd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return check;
	}

	//convert date and time to ics file format
	//parameter- date[String] : taken from text filed string of Start/End
	//parameter- time[String] : taken from text filed string of Start/End
	//return- result[String] : date and time in .ics file format
	public String convertDateTime(String date, String time)
	{
		String result = "";
		String[] tempDate = date.split("/");
		String[] tempTime = time.split(":");
		result = tempDate[2] + tempDate[0] + tempDate[1] + "T" + tempTime[0] + tempTime[1] + tempTime[2];
		return result;
	}
}
