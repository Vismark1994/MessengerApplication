package com.vismark.MessengerApplication.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import javax.swing.JTextArea;

public class ClientConnection {
	
	private static final Logger LOGGER
	    = Logger.getLogger(ClientConnection.class.getName());

	private static Socket connectionToServer;
	private static PrintWriter out;
	private static String hostname;
	private static int port;
	private JTextArea chatArea;

	public ClientConnection(String hostname, int port, JTextArea chatArea) {
		this.hostname = hostname;
		this.port = port;
		
		//Shows the chat room conversation
		this.chatArea = chatArea;
	}

	public void initializeClient() {
		try {
			LOGGER.info("Initializing Client...");
			connectionToServer = new Socket(hostname, port);
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
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(connectionToServer.getInputStream()));
					while (true) {
						LOGGER.info("[Client]: Waiting for a message from the server...");
						messageFromServer = bufferedReader.readLine();
						LOGGER.info("[Client]: Message from server: " + messageFromServer);
						
						//append the new message to the chat area for this user
						LOGGER.info("Appending the following message to the chat area: " + messageFromServer);
						chatArea.append(messageFromServer);
					}
				} catch (IOException e) {
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
			public void run() {
				//always listen for input from keyboard
				while (true) {	
					BufferedReader bufferedReader
					    = new BufferedReader(new InputStreamReader(System.in));
					String myMessage = "";
					try {
						myMessage = bufferedReader.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					out.println(myMessage);
				}
			}
		});
		return listenerThread;
	}
	
}

