package logic;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	String hostName = "localhost";
	

	public static void main(String[] args) {
	int portNumber = 4444;
	boolean listening = true;
	
		try(ServerSocket serverSocket = new ServerSocket(portNumber)){
			while(listening){
				//need to have 2 clients to do this
				//TODO: have a better way of handling this
				Socket client1 = serverSocket.accept();
				Socket client2 = serverSocket.accept();
				
				new ServerThread(client1).start();
				new ServerThread(client2).start();
		
			}
		}catch(Exception e){
			System.err.println("Could not create server socket");
			System.exit(-1);
		}

	}

}
