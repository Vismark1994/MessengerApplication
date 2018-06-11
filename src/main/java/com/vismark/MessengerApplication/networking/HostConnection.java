package com.vismark.MessengerApplication.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class will create a ServerSocker connection for clients
 * to connect to. Instead of having to manually launch a separate
 * "server" application, the first client to attempt a connection 
 * to any given host/port combination will automatically become
 * the Chat host. If the host leaves the conversation, another 
 * user becomes the host. This process will continue such that the
 * connection is not terminated until the last user exits the chat.
 */
public class HostConnection {
	private static final Logger LOGGER
	    = Logger.getLogger(HostConnection.class.getName());
	private ServerSocket serverSocket;
	private int portNumber;
	private String hostName;
	private volatile boolean listenForNewConnections;
	private volatile boolean keepHostAlive;
	private List<Socket> clientConnections;

	public HostConnection(String hostName, int portNumber) {
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.listenForNewConnections = true;
		this.keepHostAlive = true;
		clientConnections = new ArrayList<Socket>();
	}

	/**
	 * Listens for new connections. When a new connection is established,
	 * communication channels are opened for bi-directional communication
	 * between the server and each of the connected clients.
	 */
	public void initializeHost() {

		try {
			//create the server
			serverSocket = new ServerSocket(portNumber);
			/**
			 * Listen for new connection attempts and add all successful
			 * connections into a list of all connections.
			 */
			
			//TODO Each client should provide their username when
			//connecting.
			Thread listenForNewConnections = new Thread(new Runnable() {
				public void run() {
					Socket newClientConnection;
					try {
						//Pause and wait for new connections to be made
						LOGGER.info("[Server]: Waiting for a new connection...");
						
						while(true) {
							newClientConnection = serverSocket.accept();
							clientConnections.add(newClientConnection);
							
							LOGGER.info("[Server]: Added a new connection from ip: "
							    + newClientConnection.getInetAddress()
							    + "Total chat participants is now: "
							    + clientConnections.size());
							
							Thread listenForNewMessages
							    = listenForNewMessages(newClientConnection);
							
							listenForNewMessages.start();
							
							System.out.println("returned from the listen for new messages from the client thread initiation.");
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			/**
			 * listen for new connections in its own thread.
			 **/
			listenForNewConnections.start();
			System.out.println("Returned from listenForNewClientConnections()"); //never returned from line 58
		} catch(Exception e) {
			System.out.println("Something went wrong.");
		}
	}

	

	//last method executed.
	private Thread listenForNewMessages(final Socket connectionWithClient) {
		Thread listener = new Thread(new Runnable() {
			
			InputStream inputStream = null;
			InputStreamReader inputStreamReader = null;
			BufferedReader bufferedReader = null;
			
			public void run() {
				/*
				 * Establish a BufferedReader stream for input from the clients.
				 */
				try {
					inputStream
					    = connectionWithClient
					      .getInputStream();
					
					inputStreamReader
					    = new InputStreamReader(inputStream);
					
					bufferedReader
					    = new BufferedReader(inputStreamReader);

					// listen for new messages from the client
					while (listenForNewConnections) {
						LOGGER.info("[Server]: Waiting for messages from the client.");
						
						String newMessageReceived = bufferedReader.readLine();
						
						LOGGER.info("[Server]: Message received from client: "
						    + newMessageReceived);
						
						//broadcast the newly received message
						broadcastNewMessage(newMessageReceived);
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						//Close input streams
						inputStream.close();
						inputStreamReader.close();
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		return listener;
	}
	
	private void broadcastNewMessage(String messageToBroadcast) {
		for(int i = 0; i < clientConnections.size(); i++) {
				try {
					//WRITING FROM THE HOST SERVER TO THE CLIENT SOCKET
					Socket currentSocket = clientConnections.get(i);
					PrintWriter printWriter;
					
					printWriter = new PrintWriter(currentSocket
				    		.getOutputStream(), true);
					
					printWriter.println(messageToBroadcast);
					
					LOGGER.info("[Server]: Message " + "\"" + messageToBroadcast + "\"" + " broadcast to "
								+ clientConnections.get(i).toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * Notifies all current clients that a new user has
	 * joined the chat room.
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
