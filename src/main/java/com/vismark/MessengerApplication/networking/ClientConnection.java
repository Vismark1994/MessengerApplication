package com.vismark.MessengerApplication.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Logger;

import javax.swing.JTextArea;

import com.vismark.MessengerApplication.frame.MainFrame;

public class ClientConnection {

	private static final Logger LOGGER = Logger.getLogger(ClientConnection.class.getName());

	private Socket connectionToServer;
	private PrintWriter out;
	private BufferedReader bufferedReader;
	private String hostname;
	private int port;
	private MainFrame mainFrame;
	private final UUID clientUUID = UUID.randomUUID();

	public ClientConnection(String hostname, int port, MainFrame mainFrame) {
		this.hostname = hostname;
		this.port = port;
		this.mainFrame = mainFrame;
	}

	public void initializeClient() {
		try {
			LOGGER.info("Initializing Client...");
			connectionToServer = new Socket(hostname, port);
			
			//Give the host the client's uuid
			out = new PrintWriter(connectionToServer.getOutputStream(), true);
			
			Thread listenForMessagesFromServer = listenForMessagesFromServer();
			Thread listenForKeyboardInput = listenForKeyboardInput();

			listenForMessagesFromServer.start();
			LOGGER.info("starting to listen for msgs from keyboard...");
			listenForKeyboardInput.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Thread listenForMessagesFromServer() {
		Thread listenerThread = new Thread(new Runnable() {
			public void run() {
				String messageFromServer;
				try {
					bufferedReader = new BufferedReader(
							new InputStreamReader(connectionToServer
									.getInputStream()));
					while (true) {
						LOGGER.info("[Client]: Waiting for a message "
								+ "from the server...");
						
						try {
							messageFromServer = bufferedReader.readLine();
							LOGGER.info("[Client]: Message from server: "
							    + messageFromServer);
							
							//append the new message to the chat area for this user
							LOGGER.info("Appending the following message"
									+ "to the chat area: " + messageFromServer);
							JTextArea chatArea
							    = mainFrame.getChatAreaPanel().getChatRoomMessages();
							
							chatArea.append(messageFromServer + '\n');
						} catch (Exception e) {
							out.close();
							bufferedReader.close();
							connectionToServer.close();
							System.out.println("[Server]: Breaking out of new message listener loop.");
							break;
						}
					}
				}
			 catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						connectionToServer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		});

	return listenerThread;

	}

	private Thread listenForKeyboardInput() {
		Thread listenerThread = new Thread(new Runnable() {
			
			String myUsername = mainFrame
					.getUserRegistrationPanel()
					.getUserNameTextField().getText();
			
			public void run() {
				//always listen for input from keyboard
				while (true) {	
					BufferedReader bufferedReader
					    = new BufferedReader(new InputStreamReader(System.in));
					String myMessage = myUsername;
					try {
						myMessage = bufferedReader.readLine();
					} catch (IOException e) {
						try {
							bufferedReader.close();
						} catch (IOException io) {
							e.printStackTrace();
						}
						out.close();
					} 
					
					
					if("shut_down".equals(myMessage)) {
						try {
							out.println("shut_down");
							bufferedReader.close();
							out.close();
							System.out.println("[Client]: Input and output channels closed.");
							System.out.println("[Client]: Breaking out of new message listener loop");
							break;
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
					
					else
						out.println(myUsername + ": " + myMessage);
				}
			}
		});
		return listenerThread;
	}

	/*
	 * @param socket The socket to probe for liveness.
	 * 
	 * @return whether the provided socket is open or not.
	 */
	private boolean isLiveConnection(Socket socket) {
		if (socket.isClosed())
			return false;
		else
			return true;
	}

}
