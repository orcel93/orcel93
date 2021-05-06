package main.tictactoe.client;

import java.io.IOException;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class GameButton extends JButton {

	public GameButton(GameFieldFrame gameFieldFrame, Client client, int row, int column) {
		
		addActionListener(e -> {
			String text = getText();
			if (text.equals("X") || text.equals("O")) {
				JOptionPane.showMessageDialog(null, "You should not have been able to click here!", "Game Error",
						JOptionPane.ERROR_MESSAGE);
			} else {

				try {
					String response = client.sendMessage("click" + row + " " + column);

					String[] splitResponse = response.split("\\s+");

					String action = splitResponse[0];

					switch (action.toLowerCase(Locale.ROOT)) {
					case "yes": {
						// yes X
						String updatedText = splitResponse[1];
						setText(updatedText);
						
						gameFieldFrame.waitForOtherPlayer();
						break;
					}
					case "won": {
						String updatedText = splitResponse[1];
						setText(updatedText);

						gameFieldFrame.reactToPlayerWonGame();
						break;
					}
					case "no": {
						JOptionPane.showMessageDialog(null, "Critical Server Error!", "Game Error",
								JOptionPane.ERROR_MESSAGE);
						break;
					}
					
					}
					
				} catch (IOException ioException) {
					JOptionPane.showMessageDialog(null, ioException.getMessage(),
							"Server Problem", JOptionPane.ERROR_MESSAGE);
					
					ioException.printStackTrace();
					ioException.getClass().getName();
					ioException.toString();
				}
			}
		});
	}
}
