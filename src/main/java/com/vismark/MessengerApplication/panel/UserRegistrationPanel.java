package com.vismark.MessengerApplication.panel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.vismark.MessengerApplication.networking.HostConnection;

public class UserRegistrationPanel extends JPanel {

	//Logger for debugging purposes:
	private static final Logger LOGGER = Logger.getLogger(UserRegistrationPanel.class.getName());
	
	private JLabel fullNameLabel;
	private JTextField fullNameTextField;

	private JLabel userNameLabel;
	private JTextField userNameTextField;

	private JLabel hostAddressLabel;
	private JTextField hostAddressTextField;

	private JLabel portLabel;
	private JTextField portTextField;

	private JButton connectionButton;
	
	//Networking components
	private Socket serverConnectionSocket = null;
	private HostConnection hostConnection;
	private int portNumber;
	private String host;
	

	// Default constructor
	public UserRegistrationPanel() {
		setupUserRegistrationPanel();
	}

	// method initialized the default components of the UserRegistrationPanel
	public void setupUserRegistrationPanel() {

		// panel settings
		setLayout(new FlowLayout());
		setSize(550, 100);

		// components and their configurations
		// Full name JLabel and JTextField
		fullNameLabel = new JLabel("Full Name:");
		fullNameTextField = new JTextField();
		fullNameTextField.setColumns(10); // length of the JTextField

		add(fullNameLabel);
		add(fullNameTextField);

		// UserName JLabel and JTextField
		userNameLabel = new JLabel("Username:");
		userNameTextField = new JTextField();
		userNameTextField.setColumns(10);

		add(userNameLabel);
		add(userNameTextField);

		// HostIp JLabel and JTextField
		hostAddressLabel = new JLabel("Host IP:");
		hostAddressTextField = new JTextField();
		hostAddressTextField.setColumns(10);

		add(hostAddressLabel);
		add(hostAddressTextField);

		// Port # JLabel and JTextField
		portLabel = new JLabel("Port:");
		portTextField = new JTextField();
		portTextField.setColumns(4);

		add(portLabel);
		add(portTextField);

		// Connection JButton
		connectionButton = new JButton("Connect");
		add(connectionButton);

		
		// JButton configuration
		connectionButton.addActionListener(new ActionListener() {
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
				
				System.out.println("Server connection already exists: " + hostConnectionAlreadyExists);
				
				if(!hostConnectionAlreadyExists) {
					//This client becomes the server, as well as a client.
					hostConnection = new HostConnection(host, portNumber);
					hostConnection.initializeHost();
				}
				
				/* Now, establish a client connection to the host.
				 * This should happen regardless of whether the
				 * client is the host or not.
				 * */
				try {
					serverConnectionSocket = new Socket(host, portNumber);
					System.out.println("Successfully connected to the server.");
					
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// TODO once connection is made, gray-out all of the textfields, and change label on
				// the connectionButton to
				// read "Disconnect"
				
				
			}
		});
	}
	
	/**
	 * Greys out all required user registration fields after validation
	 * is complete.
	 */
	private void greyOutRegistrationFields() {
		fullNameTextField.setEditable(false);
		userNameTextField.setEditable(false);
		hostAddressTextField.setEditable(false);
		portTextField.setEditable(false);
	}

	public void validateUserInput() {
	
		// revert background of all fields to white, if appliclable (this will execute
		// when re-trying after exception
		revertBackgroundColors();

		try {
			
			validateAllInputFields();
			
			//If validation is successful, store data entered by user:
			storeInput();
			
		} 
		catch (ValidationFailedException e) {
			
			//TODO: GUI simply halts execution.  Invalid fields are highlighted in red, suggesting to the user
			//that he/she needs to try again.
		}

	}
	
	public void validateAllInputFields() throws ValidationFailedException {
		
		boolean allInputValuesPassedValidation = true;
		
		if (fullNameTextField.getText().length() < 2) {
			// full name JTextField is invalid
			fullNameTextField.setBackground(Color.RED);
			
			//allInputValuesPassedValidation fails
			allInputValuesPassedValidation = false;
		}
		if (userNameTextField.getText().length() == 0) {
			// username JtextField value is invalid
			userNameTextField.setBackground(Color.RED);
			
			//allInputValuesPassedValidation fails
			allInputValuesPassedValidation = false;
		}
		if (hostAddressTextField.getText().length() < 7) {
			// hostAddress JtextField value is invalid
			hostAddressTextField.setBackground(Color.RED);
			
			//allInputValuesPassedValidation fails
			allInputValuesPassedValidation = false;
		}
		if (Integer.parseInt((portTextField.getText())) < 1) {
			// port JtextField value is invalid
			portTextField.setBackground(Color.RED);
			
			//allInputValuesPassedValidation fails
			allInputValuesPassedValidation = false;
		}
		
		if(!allInputValuesPassedValidation)
			throw new ValidationFailedException();
		
		
	}
	
	public void storeInput() {
		
		setHost(this.getHostAddressTextField().getText());
		setPortNumber(Integer.parseInt(this.getPortTextField().getText()));
		
		LOGGER.log(Level.INFO, "stored port #: " + this.getPortNumber());
		LOGGER.log(Level.INFO, "stored hostname: " + this.getHost());
	}

	private class ValidationFailedException extends Exception {

		public ValidationFailedException() {
			
		}

	}

	public void revertBackgroundColors() {
		fullNameTextField.setBackground(Color.white);
		userNameTextField.setBackground(Color.white);
		hostAddressTextField.setBackground(Color.white);
		portTextField.setBackground(Color.white);
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
	private boolean checkForExistingHostConnection(String host, int portNumber) {
		
		//Attempt the connection
		Socket socket = null;
		
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
	
	
	public JLabel getFullNameLabel() {
		return fullNameLabel;
	}

	public JTextField getFullNameTextField() {
		return fullNameTextField;
	}

	public JLabel getUserNameLabel() {
		return userNameLabel;
	}

	public JTextField getUserNameTextField() {
		return userNameTextField;
	}

	public JLabel getHostAddressLabel() {
		return hostAddressLabel;
	}

	public JTextField getHostAddressTextField() {
		return hostAddressTextField;
	}

	public JLabel getPortLabel() {
		return portLabel;
	}

	public JTextField getPortTextField() {
		return portTextField;
	}

	public JButton getConnectionButton() {
		return connectionButton;
	}

	public Socket getServerConnectionSocket() {
		return serverConnectionSocket;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public String getHost() {
		return host;
	}

	public void setFullNameLabel(JLabel fullNameLabel) {
		this.fullNameLabel = fullNameLabel;
	}

	public void setFullNameTextField(JTextField fullNameTextField) {
		this.fullNameTextField = fullNameTextField;
	}

	public void setUserNameLabel(JLabel userNameLabel) {
		this.userNameLabel = userNameLabel;
	}

	public void setUserNameTextField(JTextField userNameTextField) {
		this.userNameTextField = userNameTextField;
	}

	public void setHostAddressLabel(JLabel hostAddressLabel) {
		this.hostAddressLabel = hostAddressLabel;
	}

	public void setHostAddressTextField(JTextField hostAddressTextField) {
		this.hostAddressTextField = hostAddressTextField;
	}

	public void setPortLabel(JLabel portLabel) {
		this.portLabel = portLabel;
	}

	public void setPortTextField(JTextField portTextField) {
		this.portTextField = portTextField;
	}

	public void setConnectionButton(JButton connectionButton) {
		this.connectionButton = connectionButton;
	}

	public void setServerConnectionSocket(Socket serverConnectionSocket) {
		this.serverConnectionSocket = serverConnectionSocket;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public void setHost(String host) {
		this.host = host;
	}

}

	