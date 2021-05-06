package main.tictactoe.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

	private final int port;
	private ServerSocket serverSocket;
	private Socket servSocket;
	private boolean keepRunning;

	public static CopyOnWriteArrayList<Game> games;

	public Server(int port) {
		this.port = port;
		games = new CopyOnWriteArrayList<>();

	}

	public void start() throws IOException {
		serverSocket = new ServerSocket(port);
		keepRunning = true;
		while (keepRunning) {
			Socket clientSocket = serverSocket.accept();
			ClientHandler clientHandler = new ClientHandler(clientSocket, null);
			clientHandler.start();
		}
	}

	public void stop() throws IOException {
		keepRunning = false;
		serverSocket.close();
	}

	public void run() throws IOException {

		try (PrintWriter serverToClientWriter = new PrintWriter(servSocket.getOutputStream(), true);
				BufferedReader clientToServerReader = new BufferedReader(new InputStreamReader(servSocket.getInputStream()))) {

			String message;
			while ((message = clientToServerReader.readLine()) != null) {

				// Split message by whitespaces
				String[] splitMessage = message.split("\\s+");

				String action = splitMessage[0];

				switch (action.toLowerCase(Locale.ROOT)) {

				case "Ready":

					int numberPlayersReady = 2;

					if (numberPlayersReady == 2)  {

						serverToClientWriter.println("Lets go");
					} else {

						serverToClientWriter.println("Waiting for another player");
					}

					break;

				case "player": {

					String username = splitMessage[1];
					String password = splitMessage[2];

					// neuer Spieler login
				}

				case "click":{
					
					String player =splitMessage[0];
					String row = splitMessage[1];
					String column = splitMessage[2];
					
					
					// Rückgabe für O oder X
					}
				
				}
			}
		}
	}


}
