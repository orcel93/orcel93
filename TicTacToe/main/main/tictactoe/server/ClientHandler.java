package main.tictactoe.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.util.Locale;

import javax.swing.JOptionPane;

public class ClientHandler extends Thread {

	private static final String URL = "jdbc:sqlite::resource:"+ "DB.db";

	private static final String SQL_SELECT_PLAYER_NAME_PASSWORD = "SELECT * FROM `spieler` WHERE `Name` = ? AND `Passwort` = ?";
	private static final String SQL_SELECT_PLAYER_NAME = "SELECT * FROM `spieler` WHERE `Name` = ?";
	private static final String SQL_INSERT_PLAYER = "INSERT INTO `spieler` (`Name`, `Passwort`) VALUES (?,?)";
	private Game game;
	private Player player;
	private Socket clientSocket;

	public ClientHandler(Socket clientSocket, Server server) {
		this.clientSocket = clientSocket;
	}

	public void run() {

		try (PrintWriter serverToClientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader clientToServerReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

			String message;
			while ((message = clientToServerReader.readLine()) != null) {

				// Split message by whitespaces
				String[] splitMessage = message.split("\\s+");

				String action = splitMessage[0];

				switch (action.toLowerCase(Locale.ROOT)) {
				case "create": {
					// create Stefan test

					String username = splitMessage[1];
					String password = splitMessage[2];

					Connection connection = DriverManager.getConnection(URL);
					PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PLAYER_NAME);
					preparedStatement.setString(1, username);

					ResultSet resultSet = preparedStatement.executeQuery();

					if (resultSet.next()) {
						serverToClientWriter.println("no");
					} else {

						preparedStatement = connection.prepareStatement(SQL_INSERT_PLAYER);
						preparedStatement.setString(1, username);
						preparedStatement.setString(2, password);
						preparedStatement.executeUpdate();

						serverToClientWriter.println("yes");
					}

					connection.close();
					break;
					
				}
				case "login": {
					// login Stefan Passwort1234

					String username = splitMessage[1];
					String password = splitMessage[2];

					Connection connection = DriverManager.getConnection(URL);
					PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PLAYER_NAME_PASSWORD);
					preparedStatement.setString(1, username);
					preparedStatement.setString(2, password);

					ResultSet resultSet = preparedStatement.executeQuery();

					if (resultSet.next()) {
						serverToClientWriter.println("yes");
						player = new Player(username);
					} else {
						serverToClientWriter.println("no");
					}
					connection.close();

					break;
				}
				case "waiting":
				{
					game = addPlayerToGames(player);
					
				}
				case "Ready":{

					int numberPlayersReady = 2;

					if (numberPlayersReady == 2)  {

						serverToClientWriter.println("Lets go");
					} else {

						serverToClientWriter.println("Waiting for another player");
					}

					break;
				}
				
				case "logout": {
					
					
					break;
				}

				case "click": {
						
					// click X 1 2
					String player = splitMessage[0];
					String row = splitMessage[1];
					String column = splitMessage[2];
					
					game.addMoveByPlayer(player, Integer.parseInt(row), Integer.parseInt(column));
					
					}
										
					serverToClientWriter.println(" X " + 1 + 1);
					
					break;

				   }
				
				}
			
		} catch (IOException | SQLException e) {

			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Server Problem", JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO: React to exception
			}
		}
	}
	public Game addPlayerToGames(Player player) {
        // First check whether there is a game which is missing a player
        for (Game game : Server.games) {
            if (game.getFirstPlayer() == null || game.getSecondPlayer() == null) {
                // We found a game which is missing a player
                
                // Now add the new player to the game and return the game
                if (game.getFirstPlayer() == null) {
                    game.addFirstPlayer(player);
                    return game;
                    
                } else if (game.getSecondPlayer() == null) {
                    game.addSecondPlayer(player);
                    return game;
                }
            }
        }
		return game;
}
}
