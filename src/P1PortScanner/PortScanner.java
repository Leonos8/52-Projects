package P1PortScanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PortScanner 
{
	public void run()
	{
		scanPorts();
	}
	
	public String getPublicIP(String url)
	{
		String ip="";
		
		try
		{
			URL url_name = new URL(url);
			
			BufferedReader br=
					new BufferedReader(new InputStreamReader(url_name.openStream()));
			
			ip=br.readLine().trim();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return ip;
	}
	
	public void scanPorts()
	{
		try {
            InetAddress ip = InetAddress.getLocalHost();
            String ipAddress = ip.getHostAddress().trim();
            System.out.println("Current IP address: " + ipAddress);
            
            int max=65535;
            scanPorts(ipAddress, max);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void scanPorts(int max)
	{
		try {
            InetAddress ip = InetAddress.getLocalHost();
            String ipAddress = ip.getHostAddress().trim();
            System.out.println("Current IP address: " + ipAddress);
            
            scanPorts(ipAddress, max);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void scanPorts(String ip)
	{
		int max=65535;
		
		scanPorts(ip, max);
	}
	
	public void scanPorts(String ip, int nbrPortMaxToScan)
	{
		int poolSize=1000; //Number of threads created by the Executor Service
		int timeOut=500; //Amount of time before socket times out
		
		//Stores the open ports
		ConcurrentLinkedQueue openPorts = new ConcurrentLinkedQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        AtomicInteger port = new AtomicInteger(0);
        while (port.get() < nbrPortMaxToScan) 
        {
        	final int currentPort = port.getAndIncrement();
          	executorService.submit(() -> 
         	{
         		try 
             	{
         			Socket socket = new Socket();
                   	socket.connect(new InetSocketAddress(ip, currentPort), timeOut);
                 	socket.close();
                  	openPorts.add(currentPort);
                 	System.out.println(ip + " ,port open: " + currentPort);
             	} catch (IOException e) 
         		{
             		//System.err.println(e);
             	}
         	});
        }
        executorService.shutdown();
        try 
        {
        	executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) 
        {
        	throw new RuntimeException(e);
        }
        List openPortList = new ArrayList<>();
        System.out.println("openPortsQueue: " + openPorts.size());
        while (!openPorts.isEmpty()) 
        {
        	openPortList.add(openPorts.poll());
        }
        
        openPortList.forEach(p -> System.out.println("port " + p + " is open"));
	}
}
