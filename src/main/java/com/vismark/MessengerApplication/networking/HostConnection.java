package com.vismark.MessengerApplication.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will create a ServerSocker connection for clients to connect to.
 * Instead of having to manually launch a separate "server" application, the
 * first client to attempt a connection to any given host/port combination will
 * automatically become the Chat host. If the host leaves the conversation,
 * another user becomes the host. This process will continue such that the
 * connection is not terminated until the last user exits the chat.
 */
public class HostConnection {

	private ServerSocket serverSocket;
	private int portNumber;
	private String hostName;
	private volatile boolean listenForNewConnections;
	private volatile boolean keepHostAlive;
	private List<Socket> clientConnections;
	private List<BufferedWriter> newConnectionBroadcastChanels;
	private List<BufferedReader> newMessageListeneningChanels;

	public HostConnection(String hostName, int portNumber) {
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.listenForNewConnections = true;
		this.keepHostAlive = true;
		clientConnections = new ArrayList<Socket>();
		newConnectionBroadcastChanels = new ArrayList<BufferedWriter>();
	}

	/**
	 * Listens for new connections. When a new connection is established,
	 * communication channels are opened for bi-directional communication
	 * between the server and each of the connected clients.
	 */
	public void initializeHost() {

		try {
			serverSocket = new ServerSocket(portNumber);
			/**
			 * Listen for new connection attempts and add all successful connections into a
			 * list of all connections.
			 */
			
			Thread listenForNewConnections = new Thread(new Runnable() {
				public void run() {
					while (keepHostAlive) {
						
						/*
						 * TODO: This is not the best way to handle multiple clients.
						 * a new thread should be spun up after each connection is made.
						 * */
						
						try {
							// Stop and wait for new connections to be made
							System.out.println("Waiting for a new connection...");
							Socket newClientConnection;
							
							newClientConnection = serverSocket.accept();
							
							// Create a new connectionBroadcastChannel
							createNewConnectionNotificationChannel(newClientConnection);

							/*
							 * Broadcast to the entire chat room that a new user has joined the chat.
							 */
							// broadCastNewUser();
							System.out.println("newConnection notification channels: " + newConnectionBroadcastChanels.size());

							System.out.println("A new connection has been made!: " + newClientConnection.getInetAddress());
							clientConnections.add(newClientConnection);
							System.out.println("Added a new connection from ip: " + newClientConnection.getInetAddress());
							
							Thread listenForNewMessages = listenForNewMessages(newClientConnection);
							
							listenForNewMessages.start();
							listenForNewMessages.join();
							
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}
			});
			
			listenForNewConnections.start();
			//Is a join needed? Join will halt execution.
			//Leaving it out for now.
			
		} catch(Exception e) {
			System.out.println("Something went wrong.");
		}
		
		
			
			
	}

	private Thread listenForNewMessages(final Socket connectionWithClient) {
		Thread listener = new Thread(new Runnable() {
			public void run() {

				/*
				 * Establish a BufferedReader stream for input from any of the clients.
				 */
				try {
					InputStream inputStream = connectionWithClient.getInputStream();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

					// listen for new messages from the client
					while (listenForNewConnections) {
						System.out.println("Waiting for messages from the client.");
						String newMessageReceived = bufferedReader.readLine();
						System.out.println("Message received from client: " + newMessageReceived);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		return listener;
	}

	/**
	 * Opens a "channel" (BufferedWriter) between the server and each individual
	 * client, and stores all of these connections in a List. This allows the server
	 * to notify each individual client of a new connection when a new client has
	 * joined the group chat.
	 */
	public void createNewConnectionNotificationChannel(Socket newClientSocket) {

		try {
			OutputStream outputStream = newClientSocket.getOutputStream();
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

			newConnectionBroadcastChanels.add(bufferedWriter);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Notifies all current clients that a new user has joined the chat room.
	 */
	public void broadcastNewConnection() {

	}

	public void stopListeningForNewConnections() {
		listenForNewConnections = false;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
}
