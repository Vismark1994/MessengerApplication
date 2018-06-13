package com.vismark.MessengerApplication.panel;

import java.awt.FlowLayout;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

}
