package com.vismark.MessengerApplication.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {

	private static Socket connectionToServer;
	private static PrintWriter out;
	private static String hostname;
	private static int port;

	public ClientConnection(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void initializeClient() {
		try {
			System.out.println("ENTERED initialize Client.");
			connectionToServer = new Socket(hostname, port);
			out = new PrintWriter(connectionToServer.getOutputStream(), true);
			Thread listenForMessagesFromServer = listenForMessagesFromServer();
			Thread listenForKeyboardInput = listenForKeyboardInput();
			
			listenForMessagesFromServer.start();
			System.out.println("starting to listen for msgs from keyboard.");
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
					System.out.println("[Client]: Waiting for message from server...");
					while (true) {

						messageFromServer = bufferedReader.readLine();
						System.out.println("[Client]: Message from server: " + messageFromServer);

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
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
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

