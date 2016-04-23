package logic;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	String hostName = "localhost";
	static Socket connectedClients[] = new Socket[2];

	public static void main(String[] args) {
	int portNumber = 1234;
	boolean listening = true;
	
		try(ServerSocket serverSocket = new ServerSocket(portNumber)){
			System.out.println("Server successfully created");
			while(listening){
				//need to have 2 clients to do this
				//TODO: have a better way of handling this
				Socket client1 = serverSocket.accept();
				printf("Client 1 has connected");
				connectedClients[0] = client1;
				new ServerThread(client1).start();
				
				
				Socket client2 = serverSocket.accept();	
				printf("Client 2 has connected");
				connectedClients[1] = client2;
				new ServerThread(client2).start();
		
			}
		}catch(Exception e){
			System.err.println("Could not create server socket");
			System.exit(-1);
		}

	}
	
	public static void printf(String x){
		System.out.println(x);
	}

}
