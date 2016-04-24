package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client_Listener extends Thread {
	private BufferedReader fromServer;
	private PrintWriter toServer;
	private String messageFromServer;
	private boolean isCurrentTurn;

	public Client_Listener(Socket socket) {
		isCurrentTurn = false;
		
		try {
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Error creating connections with server");
			System.exit(-1);
		}

	}
	
	public boolean isCurrentTurn() {
		return isCurrentTurn;
	}
	
	public void setCurrentTurn() {
		sendMessage("MOVED");
		isCurrentTurn = false;
	}
	
	public void sendMessage(String msg) {
		toServer.println(msg);
		toServer.flush();
	}

	public void run() {
		try {
			messageFromServer = fromServer.readLine();
		} catch (IOException e1) {
			System.out.println("Connection with server has been interrupted");
			System.exit(-1);
		}
		
		if(messageFromServer.equals("TURN")) {
			isCurrentTurn = true;
		} else if (messageFromServer.equals("END")) {
			
		}
	}
}
