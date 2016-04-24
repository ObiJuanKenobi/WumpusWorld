package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server_Listener extends Thread {
	private PrintWriter toClient;
	private BufferedReader fromClient;
	private String messageFromClient;
	private boolean isCurrentTurn = false;

	public Server_Listener(Socket socket) {
		try {
			toClient = new PrintWriter(socket.getOutputStream());
			fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			System.err.println("Could not create Print Writer or Buffered Reader in Server thread");
		}
	}
	
	public boolean isCurrentTurn() {
		return isCurrentTurn;
	}
	
	public void setCurrentTurn(boolean bool) {
		isCurrentTurn = bool;
	}

	@Override
	public void run() {
		try {
			messageFromClient = fromClient.readLine();
		} catch (IOException e1) {
			System.out.println("Connection with client has been interrupted");
			System.exit(-1);
		}
		
		printf(messageFromClient);
		
		if(isCurrentTurn) {
			System.out.println("Sending TURN");
			toClient.println("TURN");
			toClient.flush();
			
			if(messageFromClient.equals("MOVED")) {
				isCurrentTurn = false;
				Server_Main.adjustTurn();
			}
		}
		
		if(!isCurrentTurn) {
			toClient.println("WAIT");
			toClient.flush();
		}

	}
	
	/**
	 * Printing helper method
	 * @param msg Message to be printed
	 */
	public static void printf(String msg){
		System.out.println("[SERVER_LISTENER] " + msg);
	}

}