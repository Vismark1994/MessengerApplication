package com.vismark.MessengerApplication.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection extends Thread {

	private static Socket connectionToServer;
	private static PrintWriter out;
	private static String hostname;
	private static int port;

	public ClientConnection(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void run() {
		try {
			connectionToServer = new Socket(hostname, port);

			out = new PrintWriter(connectionToServer.getOutputStream(), true);

			Thread listenForMessagesFromServer = new Thread(new Runnable() {

				public void run() {

					String messageFromServer;

					try {
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(connectionToServer.getInputStream()));
						System.out.println("Waiting for message from server...");
						while (true) {

							messageFromServer = bufferedReader.readLine();
							System.out.println("Message from server: " + messageFromServer);

						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						try {
							connectionToServer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			});

			listenForMessagesFromServer.start();

			//always listen for input from keyboard
			while (true) {	
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
				String myMessage = bufferedReader.readLine();
				out.println(myMessage);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

