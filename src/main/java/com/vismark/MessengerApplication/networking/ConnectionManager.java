package com.vismark.MessengerApplication.networking;

import com.vismark.MessengerApplication.frame.MainFrame;

/**
 * Will manage all connections (host and client connections)
 * for the user.
 *
 */
//TODO Heavy refactoring needed. All connections
//and GUI frame building should be managed by this class.
public class ConnectionManager {
	private MainFrame mainFrame;
	private ClientConnection clientConnection;
	private HostConnection hostConnection;
	
	public ConnectionManager() {
	}
	
	
}
