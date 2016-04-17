package logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	PrintWriter outToServer;
	static BufferedReader fromServer;
	
	
	public static void main(String[] args){
		String hostName = "localHost";
		int portNumber = 4444;
		
		try{
			Socket clientSocket = new Socket(hostName, portNumber);
			fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream());
			String fromServer, toServer;
			
			
			
		}catch(Exception e){
			
		}
	}

}
