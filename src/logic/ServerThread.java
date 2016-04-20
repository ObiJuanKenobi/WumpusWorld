package logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	Socket socket = null;
	PrintWriter toClient;
	BufferedReader fromClient;

	public ServerThread(Socket s) {
		socket = s;
		try {
			toClient = new PrintWriter(socket.getOutputStream());
			fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			System.err.println("Could not create Print Writer or Buffered Reader in Server thread");
		}
	}

	@Override
	public void run() {
		try {
			String inputLine, outputLine;
			while ((inputLine = fromClient.readLine()) != null) {
				//do some stuff
			}
		}

		catch (Exception e) {
			System.err.println("Error in Server thread");
		}

	}

}