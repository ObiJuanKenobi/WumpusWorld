package logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	public static void main(String[] args){
		String hostName = "localHost";
		int portNumber = 4444;
		
		try{
			Socket clientSocket = new Socket(hostName, portNumber);
			new clientListener(clientSocket).start();
					
		}catch(Exception e){
			System.err.println("Could not create a client or client listener");
			System.exit(-1);
		}
	}

}
