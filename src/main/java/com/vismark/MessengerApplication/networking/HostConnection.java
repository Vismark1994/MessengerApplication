package com.vismark.MessengerApplication.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
			
			Thread listenForNewConnections = new Thread(new Runnable() {
				public void run() {
					Socket newClientConnection;
					try {
						//Pause and wait for new connections to be made
						LOGGER.info("[Server]: Waiting for a "
								+ "new connection...");
						
						while(true) {
							newClientConnection = serverSocket.accept();
							clientConnections.add(newClientConnection);
							System.out.println("A new connection was just added");
							LOGGER.info("[Server]: Added a new connection "
									+ "from ip: "
							    + newClientConnection.getInetAddress()
							    + "Total chat participants is now: "
							    + clientConnections.size());
							
							Thread listenForNewMessages
							    = listenForNewMessages(newClientConnection);
							
							listenForNewMessages.start();
							
							System.out.println("returned from "
									+ "the listen for new messages "
									+ "from the client thread initiation.");
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
					while (true) {
						LOGGER.info("[Server]: Waiting for messages "
								+ "from the client.");
						
						/*first check to make sure the connection is even open.
						 * if not, remove the current connection out of the list
						 * of connections, close the buffered readers, 
						 * and break out of the while loop to stop waiting for
						 * new messages from the client.
						*/
						String newMessageReceived = null;
						
						if(isLiveConnection(connectionWithClient)) {
							newMessageReceived
						    = bufferedReader.readLine();
						
						LOGGER.info("[Server]: Message received from client: "
						    + newMessageReceived);
						
						//if shutdown signal is received, stop listening.
						if("shut_down".equals(newMessageReceived)) {
							inputStream.close();
							inputStreamReader.close();
							bufferedReader.close();
							System.out.println("[Server]: shutting connection down.");
						}
						else {
							//broadcast the newly received message
							broadcastNewMessage(newMessageReceived);
						}
						
						}
						else {
							System.out.println("[Server]: Connection is not live. Breaking.");
							break;
						}//not a live connection. don't try to lsten for new msgs.

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
	
	//TODO Should not attempt to broadcast a new message to
	//a connection that has been closed.
	private void broadcastNewMessage(String messageToBroadcast) {
		for(int i = 0; i < clientConnections.size(); i++) {
				try {
					Socket currentSocket = clientConnections.get(i);
					
					if(isLiveConnection(currentSocket)) {
						PrintWriter printWriter;
						
						printWriter = new PrintWriter(currentSocket
					    		.getOutputStream(), true);
						
						printWriter.println(messageToBroadcast);
						
						LOGGER.info("[Server]: Message " + "\"" 
						    + messageToBroadcast + "\"" + " broadcast to "
							+ clientConnections.get(i).toString());
					}
					else
					{
						//remove current socket from the list of connections
						clientConnections.remove(i);
						System.out.println("Just removed index " + i
								+ " from the list of connections.");
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * @param socket The socket to probe for liveness.
	 * @return whether the provided socket is open or not.
	 */
	private boolean isLiveConnection(Socket socket) {
		if(socket.isClosed())
			return false;
		else
			return true;
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
