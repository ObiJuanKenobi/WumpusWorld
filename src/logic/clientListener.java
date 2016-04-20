package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class clientListener extends Thread{
	Socket client;
	BufferedReader fromServer;
	PrintWriter toServer;
	Game_Main game;
	
	public clientListener(Socket s){
		client = s;
		try{
			fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
			toServer = new PrintWriter(s.getOutputStream());
		}catch(Exception e){
			System.err.println("Error in creating PrintServer or BufferedReader");
			System.exit(-1);
		}
		
	}
	
	public void run(){
		String toSend, recieved;
		try {
			while((recieved = fromServer.readLine()) != null){
				
				
				
				
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	

}
