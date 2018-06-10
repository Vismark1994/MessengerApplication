package com.vismark.MessengerApplication.networking;

import java.util.logging.Logger;

import com.vismark.MessengerApplication.frame.MainFrame;

/**
 * Will manage all connections (host and client connections)
 * for the user.
 *
 */
//TODO Heavy refactoring needed. All connections
//and GUI frame building should be managed by this class.
public class ConnectionManager {
	private static final Logger LOGGER
	    = Logger.getLogger(ConnectionManager.class.getName());
	private MainFrame mainFrame;
	private ClientConnection clientConnection;
	private HostConnection hostConnection;
	
	public ConnectionManager() {
		initializeGUIComponents();
	}
	
	/**
	 * Initializes the application's GUI components.
	 */
	private void initializeGUIComponents() {
		mainFrame = new MainFrame("MessengerApplication");
		LOGGER.info("GUI Main Frame initialized.");
	}
	
	
}
