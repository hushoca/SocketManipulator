package com.hhoca;
import com.hhoca.*;

public class Main {

	public static void main(String[] args) {
		if(args.length < 3) {
			System.out.println("Not enough arguments: ");
			System.out.println("Usage : SManipulator {TargetIP} {TargetPort} {ListeningPort}\n");
			System.out.println("Target IP : The IP address of the end point which all requests will be redirected to.");
			System.out.println("Target Port : The port of the end point which all requests will be redirected to.");
			System.out.println("Listening Port : The port number which will be opened on this machine and will wait for requests.");
			System.out.println("\n\nCreated by H.HOCA, 2018");
			return;
		}
			
		int targetPort = 0;
		try {
			targetPort = Integer.parseInt(args[1]);
		}catch(Exception e) {
			System.err.println("Target port must be a numerical value!");
			System.exit(-1);
		}
		
		int localPort = 0;
		try {
			 localPort = Integer.parseInt(args[2]);
		}catch(Exception e) {
			System.err.println("Local listening port must be a numerical value!");
			System.exit(-1);
		}

		Thread currThread = Thread.currentThread();
		Sniffer sniffer = new Sniffer(args[0], targetPort, localPort);
		Runtime.getRuntime().addShutdownHook(new Thread() {
	        public void run() {
	        	System.out.println("Exiting...");
	        	sniffer.Stop();
	        }
	    });
		sniffer.Start();

	}

}
