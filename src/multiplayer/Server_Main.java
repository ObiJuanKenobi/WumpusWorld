package multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Main {
	//private final static String HOST_NAME = "localhost";
	private final static int PORT_NUMBER = 1234;
	
	private static ServerSocket serverSocket;
	private static Socket client_1;
	private static Socket client_2;

	public static void main(String[] args) {
		serverSocket = null;
		client_1 = null;
		client_2 = null;
		
		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
		} catch (IOException e1) {
			printf("Unable to create server socket");
		}
		
		printf("Server successfully created");
		
		while(client_2 == null || client_2 == null){
			printf("Waiting for connections...");
			
			try {
				client_1 = serverSocket.accept();
			} catch (IOException e) {
				printf("Unable to receive connection from client_1");
			}
			
			printf("Client 1 has connected");
			new Server_Listener(client_1).start();
			
			try {
				client_2 = serverSocket.accept();
			} catch (IOException e) {
				printf("Unable to receive connection from client_1");
			}
			
			printf("Client 2 has connected");
			new Server_Listener(client_2).start();
		}

	}
	
	/**
	 * Checks to see if both connections are present
	 * @return True if both are connected, false otherwise
	 */
	public static boolean ready() {
		return (client_1 == null || client_2 == null);
	}
	
	/**
	 * Printing helper method
	 * @param msg Message to be printed
	 */
	public static void printf(String msg){
		System.out.println("[SERVER] " + msg);
	}

}
