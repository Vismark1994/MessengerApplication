package com.vismark.MessengerApplication.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
	private List<Socket> clientConnections;
	
	public HostConnection(String hostName, int portNumber) {
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.listenForNewConnections = true;
		clientConnections = new ArrayList<Socket>();
	}
	
	/**
	 * Listen for new connection attempts and add all successful
	 * connections into a list of all connections.
	 */
	public void listenForConnections() {
		
		try {
			/**
			 * Create a new ServerSocket and connect to the specified
			 * port number.
			 **/
			serverSocket = new ServerSocket(portNumber);
			
			Thread newConnectionListenerThread = new Thread(new Runnable() {
				public void run() {
					while(listenForNewConnections) {
						try {
							System.out.println("You are now the host server.");
							Socket newClientConnection = serverSocket.accept();
							System.out.println("A new connection has been made!: " + newClientConnection.getInetAddress());
							clientConnections.add(newClientConnection);
							System.out.println("Added a new connection from ip: " + newClientConnection.getInetAddress());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			
			//Start the listener thread
			newConnectionListenerThread.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
