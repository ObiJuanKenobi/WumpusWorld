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
public class Server_Listener extends Thread {
	private PrintWriter toClient;
	private BufferedReader fromClient;
	private String messageFromClient;
	private boolean isCurrentTurn = false;
	private boolean running;
	private boolean hasLost;
	private boolean hasWon;

	/**
	 * Creates a thread to listen for messages on given socket
	 * @param socket Socket to listen on
	 */
	public Server_Listener(Socket socket) {
		try {
			toClient = new PrintWriter(socket.getOutputStream());
			fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			System.err.println("Could not create Print Writer or Buffered Reader in Server thread");
		}
	}
	
	/**
	 * Checks if this connection has lost the game
	 * @return True if lost, false otherwise
	 */
	public boolean hasLost() {
		return hasLost;
	}
	
	/**
	 * Checks if this connection has won the game
	 * @return True if won, false otherwise
	 */
	public boolean hasWon() {
		return hasWon;
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
	 * End this thread
	 */
	public void kill() {
		running = false;
	}

	@Override
	public void run() {
		running = true;
		hasLost = false;
		hasWon = false;
		
		while (running) {
			try {
				messageFromClient = fromClient.readLine();
			} catch (IOException e1) {
				System.out.println("Connection with client has been interrupted");
				System.exit(-1);
			}
			
			if (isCurrentTurn) {
				sendMessage("TURN");
				
				if (messageFromClient.equals("MOVED")) {
					Server_Main.adjustTurn();
					isCurrentTurn = false;
				} else if (messageFromClient.equals("LOSE")) {
					hasLost = true;
					Server_Main.handleEndOfGame();
				} else if (messageFromClient.equals("WIN")) {
					hasWon = true;
					Server_Main.handleEndOfGame();
				}
			}
			
			if(!isCurrentTurn) {
				sendMessage("WAIT");
				
				if (messageFromClient.equals("LOSE")) {
					hasLost = true;
					Server_Main.handleEndOfGame();
				} else if (messageFromClient.equals("WIN")) {
					hasWon = true;
					Server_Main.handleEndOfGame();
				}
			}
		}
	}
	
	/**
	 * Send message to client
	 * @param msg Message to be sent
	 */
	public void sendMessage(String msg) {
		toClient.println(msg);
		toClient.flush();
	}
	
	/**
	 * Printing helper method
	 * @param msg Message to be printed
	 */
	public static void printf(String msg){
		System.out.println("[SERVER_LISTENER] " + msg);
	}

}