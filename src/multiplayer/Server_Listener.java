package multiplayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server_Listener extends Thread {
	Socket socket = null;
	PrintWriter toClient;
	BufferedReader fromClient;

	public Server_Listener(Socket s) {
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
				String lines[] = inputLine.split(":");
				if(lines[0] == "STATUS"){
					if(lines[1] == "connection"){
						if(Server.connectedClients[1] != null){
							toClient.print("true");
							toClient.flush();
						}else{
							toClient.print("false");
							toClient.flush();
						}
					}
					
				}else if(lines[0] == "COMMAND"){
					
				}
			}
		}

		catch (Exception e) {
			System.err.println("Error in Server thread");
			e.printStackTrace();
		}

	}

}