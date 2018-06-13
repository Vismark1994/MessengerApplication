package com.vismark.MessengerApplication.networking;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextField;

import com.vismark.MessengerApplication.frame.MainFrame;

/**
 * Will manage all connections (host and client connections) for the user.
 */
public class ConnectionManager {
	
	//Logger for logging relevant application events
	private static final Logger LOGGER
	    = Logger.getLogger(ConnectionManager.class.getName());
	
	//GUI components encapsulated in the mainFrame
	private MainFrame mainFrame;

	// Networking components
	private boolean isHost;
	private Socket serverConnectionSocket = null;
	private int portNumber;
	private String host;
	private ClientConnection clientConnection;
	private HostConnection hostConnection;

	public ConnectionManager() {
		initializeGUIComponents();
		listenForConnectionButtonClick();
	}
	
	private class ValidationFailedException extends Exception {

		public ValidationFailedException() {
			
		}

	}

	/**
	 * Initializes the application's GUI components.
	 */
	private void initializeGUIComponents() {
		mainFrame = new MainFrame("MessengerApplication");
		LOGGER.info("GUI Main Frame initialized.");
	}
	
	private void listenForConnectionButtonClick() {
		mainFrame
		.getUserRegistrationPanel()
		.getConnectionButton()
		.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				validateUserInput();
				
				/*
				 * grey-out registration fields (make them un-editable)
				 * now that user input has been validated.
				 * */
				greyOutRegistrationFields();
				
				/* TODO Button should also be greyed out.
				 * TODO User should be notified that they are
				 * the chat's host. 
				 * */
				boolean hostConnectionAlreadyExists = 
						checkForExistingHostConnection(host, portNumber);
				
				if(!hostConnectionAlreadyExists) {
					
					/*
					 * If no host connection exists, then this client is the
					 * first user to join the groupchat.  By default, the
					 * first user becomes the host of the groupchat.
					 * */
					setIsHost(true);
					setUpHostConnection(host, portNumber);
				} else
					setIsHost(false);
				
				/* Now, establish a client connection to the host.
				 * This should happen regardless of whether the
				 * client is the host or not.
				 * */
				LOGGER.info("Setting up the client connection.");
				setupClientConnection();
				
				/*
				 * Once the connection is made, gray-out all of the textfields
				 * and change the label on the connection button to read
				 * "Disconnect".
				 * */
			}

		});
	}
	
	private void setUpHostConnection(String host, int port) {
		hostConnection = new HostConnection(host, portNumber);
		hostConnection.initializeHost();
	}
	
	private void setupClientConnection() {
		clientConnection = new ClientConnection(host, portNumber,mainFrame);
		clientConnection.initializeClient();
	}
	
	/**
	 * Attempts a socket connection to the specified host and port.
	 * If no connection is open, the method returns 'false' -- returns
	 * 'true' otherwise.
	 * 
	 * @param host hostname to attempt connection to.
	 * @param portNumber port number to attempt connection to.
	 * @return
	 */
	private boolean checkForExistingHostConnection(String host,
			int portNumber) {
		
		Socket socket = null;
		
		//Attempt the connection
		try {
			socket = new Socket(host, portNumber);
			
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
			
			return true;
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		} 
		
	}
	
	/**
	 * Greys out all required user registration fields after validation
	 * is complete.
	 */
	private void greyOutRegistrationFields() {
		mainFrame
		.getUserRegistrationPanel()
		.getFullNameTextField()
		.setEditable(false);
		
		mainFrame
		.getUserRegistrationPanel()
		.getUserNameTextField()
		.setEditable(false);
		
		mainFrame
		.getUserRegistrationPanel()
		.getHostAddressTextField()
		.setEditable(false);
		
		mainFrame
		.getUserRegistrationPanel()
		.getPortTextField()
		.setEditable(false);
	}
	
	public void storeInput() {
		int port = Integer
				   .parseInt(mainFrame
				   .getUserRegistrationPanel()
				   .getPortTextField()
				   .getText());
		
		String hostName = mainFrame
				         .getUserRegistrationPanel()
				         .getHostAddressTextField()
				         .getText();
		
		setHost(hostName);
		setPortNumber(port);

		LOGGER.log(Level.INFO, "stored port #: "
		    + this.getPortNumber());
		
		LOGGER.log(Level.INFO, "stored hostname: "
		    + this.getHost());
	}
	
	public void validateUserInput() {
		
		// revert background of all fields to white,
		// if appliclable (this will execute
		// when re-trying after exception
		revertBackgroundColors();

		try {
			
			validateAllInputFields();
			
			//If validation is successful, store data entered by user:
			storeInput();
			
		} 
		catch (ValidationFailedException e) {
			
			//TODO: GUI simply halts execution.
			//Invalid fields are highlighted in red, suggesting to the user
			//that he/she needs to try again.
		}

	}
	
	
public void validateAllInputFields() throws ValidationFailedException {
		
		boolean allInputValuesPassedValidation = true;
		
		JTextField userFullNameTextField = mainFrame
						      .getUserRegistrationPanel()
						      .getFullNameTextField();
		
		if (userFullNameTextField.getText().length() < 2) {
			/* The full name entered by the user is invalid.
			 * Prompt the user by changing the text box color
			 * to red.
			 * */
			
			userFullNameTextField.setBackground(Color.RED);
			
			//allInputValuesPassedValidation fails
			allInputValuesPassedValidation = false;
		}
		
		JTextField userNameTextField = mainFrame
			      .getUserRegistrationPanel()
			      .getUserNameTextField();
		
		if (userNameTextField.getText().length() == 0) {
			// username JtextField value is invalid
			userNameTextField.setBackground(Color.RED);
			
			//allInputValuesPassedValidation fails
			allInputValuesPassedValidation = false;
		}
		
		JTextField hostAddressTextField = mainFrame
			      .getUserRegistrationPanel()
			      .getHostAddressTextField();
		
		if (hostAddressTextField.getText().length() < 7) {
			// hostAddress JtextField value is invalid
			hostAddressTextField.setBackground(Color.RED);
			
			//allInputValuesPassedValidation fails
			allInputValuesPassedValidation = false;
		}
		
		JTextField portTextField = mainFrame
			      .getUserRegistrationPanel()
			      .getPortTextField();
		
		if (Integer.parseInt((portTextField.getText())) < 1) {
			// port JtextField value is invalid
			portTextField.setBackground(Color.RED);
			
			//allInputValuesPassedValidation fails
			allInputValuesPassedValidation = false;
		}
		
		if(!allInputValuesPassedValidation)
			throw new ValidationFailedException();
		
	}
	
	public void revertBackgroundColors() {
		mainFrame
		.getUserRegistrationPanel()
		.getFullNameTextField()
		.setBackground(Color.white);
		
		mainFrame
		.getUserRegistrationPanel()
		.getUserNameTextField().setBackground(Color.white);
		
		mainFrame
		.getUserRegistrationPanel()
		.getHostAddressTextField()
		.setBackground(Color.white);
		
		mainFrame
		.getUserRegistrationPanel()
		.getPortTextField()
		.setBackground(Color.white);
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public void setIsHost(boolean yesOrNo) {
		this.isHost = yesOrNo;
	}
	
	public boolean getIsHost() {
		return isHost;
	}
	
}
