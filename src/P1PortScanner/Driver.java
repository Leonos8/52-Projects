package P1PortScanner;

import javax.swing.SwingUtilities;

public class Driver 
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				PortScanner ps=new PortScanner();
				
				ps.run();
			}
			
		});
	}
}
