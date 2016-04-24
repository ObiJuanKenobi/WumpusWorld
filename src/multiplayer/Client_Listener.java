package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class clientListener extends Thread {
	Socket client;
	BufferedReader fromServer;
	PrintWriter toServer;
	Game_Main game;

	public clientListener(Socket s) {
		client = s;
		try {
			game = new Game_Main();
			fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
			toServer = new PrintWriter(s.getOutputStream());
		} catch (Exception e) {
			System.err.println("Error in creating PrintServer or BufferedReader");
			System.exit(-1);
		}

	}

	public void run() {
		String toSend, recieved;
		try {
			//wait for a message from the server
			while((recieved = fromServer.readLine()) != null){
				//do the analyzation here
				
				//if it is game over for the other player
				
				//if it the other player has moved
				
				
			}

		} catch (Exception e) {

		}
	}

	

}
