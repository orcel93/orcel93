package main.tictactoe.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	private final String ip;
	private final int port;

	private Socket clientSocket;
	private PrintWriter clientToServerWriter;
	private BufferedReader serverToClientReader;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void startConnection() throws IOException {
		clientSocket = new Socket(ip, port);
		clientToServerWriter = new PrintWriter(clientSocket.getOutputStream(), true);
		serverToClientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

	public String sendMessage(String message) throws IOException {
		clientToServerWriter.println(message);
		String response = serverToClientReader.readLine();
		return response;
	}

	
	public void stopConnection() throws IOException {
		clientToServerWriter.close();
		serverToClientReader.close();
		clientSocket.close();
	}
	
}
