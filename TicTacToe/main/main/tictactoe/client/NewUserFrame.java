package main.tictactoe.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class NewUserFrame extends JFrame {

	private final JTextField ipField;
	private final JTextField portField;

	private final JTextField usernameField;
	private final JPasswordField passwordField;

	private final JButton createUserButton;

	public NewUserFrame() {
		super("New User");

		setLayout(new BorderLayout());


		JLabel usernameLabel = new JLabel("New Username:");
		usernameField = new JTextField();
		usernameField.setColumns(10);

		JLabel passwordLabel = new JLabel("New Password:");
		passwordField = new JPasswordField();
		passwordField.setColumns(10);

		JPanel userPanel = new JPanel();
		userPanel.setLayout(new FlowLayout());
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

		createUserButton = new JButton("Create User");

		createUserButton.addActionListener(actionEvent -> {
			String ip = ipField.getText();
			String portText = portField.getText();
			String username = usernameField.getText();
			String password = passwordField.getText();

			try {
				int port = Integer.parseInt(portText);

				// TODO: Check that ip makes sense
				// TODO: Check that port is between 0 and 65353

				createUser(ip, port, username, password);

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "The port has to be a number!", "Invalid Port",
						JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "There was a problem connecting to the server!", "Server Problem",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(createUserButton);

		getContentPane().add(serverPanel, BorderLayout.NORTH);
		getContentPane().add(userPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().setBackground(new Color(176, 196, 222));

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(500, 150));
		setResizable(true);
		setVisible(true);
		ImageIcon ImageIcon = new ImageIcon("resources\\Server.png");
		Image Image = ImageIcon.getImage();
		this.setIconImage(Image);

	}

	private void createUser(String ip, int port, String username, String password) throws IOException {
		Client client = new Client(ip, port);
		client.startConnection();

		String response = client.sendMessage("create " + username + " " + password);
		String[] splitResponse = response.split("\\s+");
		String action = splitResponse[0];

		switch (action) {
		case "yes":
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			break;
		case "no":
			JOptionPane.showMessageDialog(null, "Username is already taken!", "Login Problem",
					JOptionPane.WARNING_MESSAGE);
			break;
		}

		client.stopConnection();
	}
}
