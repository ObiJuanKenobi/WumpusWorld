package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles message exchanges between client and server
 * @author Team Bits Please
 *
 */
public class Client_Listener extends Thread {
	private BufferedReader fromServer;
	private PrintWriter toServer;
	private String messageFromServer;
	private boolean isCurrentTurn;
	private boolean running;

	/**
	 * Creates a thread to listen for messages on given socket
	 * @param socket Socket to listen on
	 */
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
	
	/**
	 * Checks if it is this connection's turn
	 * @return True if this connection's turn, false otherwise
	 */
	public boolean isCurrentTurn() {
		return isCurrentTurn;
	}
	
	/**
	 * Change the status of this connection's current turn
	 * @param bool New status of turn
	 */
	public void setCurrentTurn(boolean bool) {
		isCurrentTurn = bool;
	}
	
	/**
	 * Send message to server
	 * @param msg Message to be sent
	 */
	public void sendMessage(String msg) {
		toServer.println(msg);
		toServer.flush();
	}
	
	/**
	 * Send multiple messages to ensure delivery
	 * @param msg Message to be sent
	 */
	public void sendTen(String msg) {
		for (int i = 0; i < 10; i++) {
			sendMessage(msg);
		}
	}
	
	/**
	 * End this thread
	 */
	public void kill() {
		running = false;
	}
	
	/**
	 * Get the status of this thread
	 * @return True if running, false otherwise
	 */
	public boolean running() {
		return running;
	}

	@Override
	public void run() {
		running = true;
		
		while (running) {
			try {
				messageFromServer = fromServer.readLine();
			} catch (IOException e1) {
				System.out.println("Connection with server has been interrupted");
				System.exit(-1);
			}
			
			if (messageFromServer.equals("TURN")) {
				isCurrentTurn = true;
			} else if (messageFromServer.equals("WAIT")) {
				isCurrentTurn = false;
			} else if (messageFromServer.equals("WON")) {
				System.out.println("You have beaten the other player!");
				kill();
			} else if (messageFromServer.equals("LOST")) {
				System.out.println("You have lost to the other player!");
				kill();
			}
		}
	}
	
}
