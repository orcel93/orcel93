package main.tictactoe.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ServerConnectionFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTextField ipField;
	private final JTextField portField;

	private final JTextField usernameField;
	private final JPasswordField passwordField;

	private final JButton connectButton;
	private final JButton createUserButton;

	private static ServerConnectionFrame serverConnectionFrame;

	private ServerConnectionFrame() {
		super("Server Connection");

		setLayout(new BorderLayout());

		JLabel usernameLabel = new JLabel("Username:");
		usernameField = new JTextField();
		usernameField.setColumns(10);

		JLabel passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField();
		passwordField.setColumns(10);

		JPanel userPanel = new JPanel();
		userPanel.setBackground(SystemColor.inactiveCaption);
		userPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		userPanel.add(usernameLabel);
		userPanel.add(usernameField);
		userPanel.add(passwordLabel);
		userPanel.add(passwordField);

		JLabel ipLabel = new JLabel("Server IP:");
		ipField = new JTextField();
		ipField.setColumns(10);

		JLabel portLabel = new JLabel("Server Port:");
		portField = new JTextField();
		portField.setColumns(10);

		JPanel serverPanel = new JPanel();
		serverPanel.setLayout(new FlowLayout());
		serverPanel.add(ipLabel);
		serverPanel.add(ipField);
		serverPanel.add(portLabel);
		serverPanel.add(portField);

		connectButton = new JButton("Connect to Server");

		connectButton.addActionListener(actionEvent -> {
			String ip = ipField.getText();
			String portText = portField.getText();
			String username = usernameField.getText();
			String password = passwordField.getText();

			try {
				int port = Integer.parseInt(portText);

				// TODO: Check that ip makes sense
				// TODO: Check that port is between 0 and 65353

				connectToServer(ip, port, username, password);
				System.out.println("Verbunden");

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "The port has to be a number!", "Invalid Port",
						JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "There was a problem connecting to the server!", "Server Problem",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		createUserButton = new JButton("Create new User");

		createUserButton.addActionListener(actionEvent -> {
			NewUserFrame newUserFrame = new NewUserFrame();
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(connectButton);
		buttonPanel.add(createUserButton);

		getContentPane().add(serverPanel, BorderLayout.NORTH);
		getContentPane().add(userPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(ServerConnectionFrame::createAndShowGUI);
	}

	private static void createAndShowGUI() {
		serverConnectionFrame = new ServerConnectionFrame();

		serverConnectionFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		serverConnectionFrame.setSize(new Dimension(450, 150));
		serverConnectionFrame.setMinimumSize(new Dimension(100, 50));
		serverConnectionFrame.setResizable(false);
		serverConnectionFrame.setVisible(true);
		ImageIcon ImageIcon = new ImageIcon("resources\\Gameicon.png");
		Image Image = ImageIcon.getImage();
		serverConnectionFrame.setIconImage(Image);
	}

	private void connectToServer(String ip, int port, String username, String password) throws IOException {
		serverConnectionFrame.setVisible(false);

		Client client = new Client(ip, port);
		client.startConnection();

		String response = client.sendMessage("login " + username + " " + password);
		String[] splitResponse = response.split("\\s+");
		String action = splitResponse[0];

		switch (action) {
		case "yes":
			GameFieldFrame gameFieldFrame = new GameFieldFrame(client);

			break;
		case "no":
			JOptionPane.showMessageDialog(null, "Wrong username or password!", "Login Problem",
					JOptionPane.WARNING_MESSAGE);
			break;
		}
		 {
		client.stopConnection();
		}
		serverConnectionFrame.setVisible(true);
	}
	
}
