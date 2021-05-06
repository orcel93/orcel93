package main.tictactoe.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class GameFieldFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int NUMBER_ROWS_COLUMNS = 3;
	private final Client client;
	static public JTextField turn;
	static public String msg;
	static Component panel_banner;
	public static GameButton[][] gameButtons;

	public GameFieldFrame(Client client) {
		this.client = client;
		
		setPreferredSize(new Dimension(50, 50));
		setLocale(Locale.GERMANY);
		getContentPane().setBackground(new Color(176, 196, 222));
		getContentPane().setLayout(null);

				
		JPanel panel_banner = new JPanel();
		panel_banner.setBorder(UIManager.getBorder("FormattedTextField.border"));
		panel_banner.setBackground(new Color(192, 192, 192));
		panel_banner.setBounds(10, 11, 350, 28);
		getContentPane().add(panel_banner);
		panel_banner.setLayout(new GridLayout(1, 4, 0, 0));

		JButton b_reset = new JButton("Reset");
		b_reset.setBackground(new Color(143, 188, 143));
		panel_banner.add(b_reset);
		b_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 

			{
				reset();

			}
		});
		
		JButton b_hilfe = new JButton("Help");
		b_hilfe.setBackground(new Color(143, 188, 143));
		panel_banner.add(b_hilfe);
//
		b_hilfe.addActionListener(new ActionListener() {
//
			public void actionPerformed(ActionEvent e) 
//
			{
//
				help();
//
			}
//
		})
		;
//		tf1 = new JTextField(" ",10);
//		tf1.setFont(new Font("Century Schoolbook", Font.PLAIN, 14));
//		tf1.setBackground(new Color(176, 196, 222));
//		tf1.setEditable(false);
//		tf1.setBounds(154, 95, 147, 28);
//		getContentPane().add(tf1);
//		tf1.setColumns(10);

		turn = new JTextField(" ",10);
		turn.setEditable(false);
		turn.setFont(new Font("Century Schoolbook", Font.PLAIN, 14));
		turn.setColumns(10);
		turn.setBackground(new Color(176, 196, 222));
		turn.setBounds(154, 56, 147, 28);
		getContentPane().add(turn);

		Label turn = new Label("Current Turn:");
		turn.setFont(new Font("Dialog", Font.PLAIN, 14));
		turn.setBounds(34, 58, 97, 22);
		getContentPane().add(turn);

//		Label ltotal = new Label("Game Count:");
//		ltotal.setFont(new Font("Dialog", Font.PLAIN, 14));
//		ltotal.setBounds(34, 95, 97, 22);
//		getContentPane().add(ltotal);
		
		setLocation(new Point(500, 500));
		setBackground(Color.BLACK);
		setTitle("TicTacToe");
		setFont(new Font("Century Schoolbook", Font.PLAIN, 12));
		setForeground(SystemColor.inactiveCaption);
		setResizable(false);

		ImageIcon ImageIcon = new ImageIcon("resources\\Gameicon.png");
	    Image Image = ImageIcon.getImage();
	    this.setIconImage(Image);

      	setBounds(100, 100, 390, 534);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		JPanel panel = new JPanel();
		panel.setBounds(10, 134, 350, 350);
		getContentPane().add(panel);
		panel.setLayout(new GridLayout(0, 3, 0, 0));
		
		// Buttons
		
		gameButtons = new GameButton[NUMBER_ROWS_COLUMNS][NUMBER_ROWS_COLUMNS];

		for (int row = 0; row < NUMBER_ROWS_COLUMNS; ++row) {
			for (int column = 0; column < NUMBER_ROWS_COLUMNS; ++column) {

				GameButton gameButton = new GameButton(this, client, row, column);
						
				gameButtons[row][column] = gameButton;
				gameButton.setFont(new Font("Century Schoolbook", Font.PLAIN, 25));
				gameButton.setBackground(new Color(255, 245, 238));
				panel.add(gameButton);
				
			}
			
		}
		
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(385, 530));
		setMinimumSize(new Dimension(100, 50));
		setVisible(true);
	}


	public void waitForOtherPlayer() {
		disableAllButtons();

		try {
			String response = client.sendMessage("waiting");
			// 1 2 X

			String[] splitResponse = response.split("\\s+");

			String action = splitResponse[0];

			switch (action.toLowerCase(Locale.ROOT)) {
			case "click": {
				// click 3 1 O
				int row = Integer.parseInt(splitResponse[0]);
				int column = Integer.parseInt(splitResponse[1]);
				String updatedText = splitResponse[2];

				gameButtons[row][column].setText(updatedText);

				enableAllButtons();
				
				break;
			}
			case "won": {
				// won 2 3 X
				int row = Integer.parseInt(splitResponse[0]);
				int column = Integer.parseInt(splitResponse[1]);
				String updatedText = splitResponse[2];

				gameButtons[row][column].setText(updatedText);

				reactToOtherPlayerWonGame();
				break;
			}

			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Server Problem", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void reactToPlayerWonGame() {
		// TODO: You won the game -> reset
	}

	private void reactToOtherPlayerWonGame() {
		// TODO: Other player won the game -> reset
	}

	private void disableAllButtons() {
		for (int row = 0; row < NUMBER_ROWS_COLUMNS; ++row) {
			for (int column = 0; column < NUMBER_ROWS_COLUMNS; ++column) {

				gameButtons[row][column].setEnabled(false);

			}
		}
	}

	private void enableAllButtons() {
		for (int row = 0; row < NUMBER_ROWS_COLUMNS; ++row) {
			for (int column = 0; column < NUMBER_ROWS_COLUMNS; ++column) {

				gameButtons[row][column].setEnabled(true);

			}
		}
	}
	
	public static void help()
	{

		msg = "\tTIC TAC TOE PROGRAM \n" +
				"/^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\\\n" +
				"|    Its a double player game. \n" +
				"| Default Symbol:              \n" +
				"| Player 1= X                  \n" +
				"| Player 2= O                  \n" +
				"\\____________________________\n";

		JOptionPane.showMessageDialog(panel_banner, msg, "Help", JOptionPane.INFORMATION_MESSAGE);
		//result_player1++;
		//for help button.
	}
	
	public static void reset()
	{

		msg = "\t Wartungsarbeiten! \n" +
				"/^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\\\n" +
				"|       Sorry!				   \n" +
				"\\____________________________\n";

		JOptionPane.showMessageDialog(panel_banner, msg, "Reset", JOptionPane.INFORMATION_MESSAGE);
		//result_player1++;
		//for help button.
	}
}
