package main.tictactoe.server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class ServerControlFrame extends JFrame {

	private final JTextField portField;

	private final JButton startButton;
	private final JButton stopButton;

	private Server server;
	private SwingWorker<Void, Void> worker;

	private ServerControlFrame() {
		super("Server Control Panel");

		setLayout(new BorderLayout());

		JLabel portLabel = new JLabel("Server Port:");
		portField = new JTextField();
		portField.setColumns(10);

		JPanel textFieldPanel = new JPanel();
		textFieldPanel.setLayout(new FlowLayout());
		textFieldPanel.add(portLabel);
		textFieldPanel.add(portField);

		startButton = new JButton("Start Server");
		stopButton = new JButton("Stop Server");

		startButton.addActionListener(actionEvent -> {
			String portText = portField.getText();

			try {
				int port = Integer.parseInt(portText);

				// TODO: Check that port is between 0 and 65353

				startServer(port);

				startButton.setEnabled(false);
				stopButton.setEnabled(true);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "The port has to be a number!", "Invalid Port", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "There was a problem starting the server!", "Server Problem", JOptionPane.ERROR_MESSAGE);
			}
		});

		stopButton.addActionListener(actionEvent -> {
			try {
				stopServer();

				stopButton.setEnabled(false);
				startButton.setEnabled(true);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "There was a problem stopping the server!", "Server Problem", JOptionPane.ERROR_MESSAGE);
			}
		});
		stopButton.setEnabled(false);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(startButton);
		buttonPanel.add(stopButton);

		getContentPane().add(textFieldPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		ImageIcon ImageIcon = new ImageIcon("resources\\Server.png");
		Image Image = ImageIcon.getImage();
		this.setIconImage(Image);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(ServerControlFrame::createAndShowGUI);
	}

	private static void createAndShowGUI() {
		ServerControlFrame serverControlFrame = new ServerControlFrame();

		serverControlFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		serverControlFrame.setSize(new Dimension(350, 150));
		serverControlFrame.setMinimumSize(new Dimension(100, 50));
		serverControlFrame.setVisible(true);
	}

	private void startServer(int port) throws IOException {
		worker = new SwingWorker<>() {
			@Override
			protected Void doInBackground() throws Exception {
				server = new Server(port);
				server.start();
				return null;
				
			}
		};
		worker.execute();
		System.out.println("Server gestartet");
	}

	private void stopServer() throws IOException {
		server.stop();
		if (worker != null) {
			worker.cancel(true);
		}
	}
}
