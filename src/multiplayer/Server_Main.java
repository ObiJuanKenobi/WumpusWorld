package multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Primary server that listens for client connections
 * @author Team Bits Please
 *
 */
public class Server_Main {
	//private final static String HOST_NAME = "localhost";
	private final static int PORT_NUMBER = 1234;
	
	private static ServerSocket serverSocket;
	private static Server_Listener client_1;
	private static Server_Listener client_2;

	public static void main(String[] args) {
		Socket sock1 = null;
		Socket sock2 = null;
		serverSocket = null;
		client_1 = null;
		client_2 = null;
		
		// Try to create server socket
		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
		} catch (IOException e1) {
			printf("Unable to create server socket");
		}
		
		printf("Server successfully created");
		
		while(client_2 == null || client_2 == null){
			printf("Waiting for connections...");
			
			// Try to accept first client
			try {
				sock1 = serverSocket.accept();
			} catch (IOException e) {
				printf("Unable to receive connection from client_1");
			}
			
			printf("Client 1 has connected");
			client_1 = new Server_Listener(sock1);
			client_1.start();
			
			// Try to accept second client
			try {
				sock2 = serverSocket.accept();
			} catch (IOException e) {
				printf("Unable to receive connection from client_1");
			}
			
			printf("Client 2 has connected");
			client_2 = new Server_Listener(sock2);
			client_2.start();
		}
		
		printf("Both connections have been received!");
		client_1.setCurrentTurn(true);
		
		while(ready()) {}
	}
	
	/**
	 * Switches the active player
	 * @param listen Listening connection that just made a move
	 */
	public static void adjustTurn() {
		if(client_1.isCurrentTurn()) {
			client_2.setCurrentTurn(true);
		} else if (client_2.isCurrentTurn()) {
			client_1.setCurrentTurn(true);
		}
	}
	
	/**
	 * Checks to see if both connections are present
	 * @return True if both are connected, false otherwise
	 */
	public static boolean ready() {
		return !(client_1 == null || client_2 == null);
	}
	
	/**
	 * Printing helper method
	 * @param msg Message to be printed
	 */
	public static void printf(String msg){
		System.out.println("[SERVER] " + msg);
	}

}
