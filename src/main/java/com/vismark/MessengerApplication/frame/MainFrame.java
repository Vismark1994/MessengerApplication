package com.vismark.MessengerApplication.frame;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.vismark.MessengerApplication.panel.ChatAreaPanel;
import com.vismark.MessengerApplication.panel.ConnectedUsersPanel;
import com.vismark.MessengerApplication.panel.UserInputPanel;
import com.vismark.MessengerApplication.panel.UserRegistrationPanel;

import javafx.scene.paint.Color;

public class MainFrame extends JFrame {

	private ConnectedUsersPanel connectedUsersPanel;
	private ChatAreaPanel chatAreaPanel;
	private UserInputPanel userInputPanel;
	private UserRegistrationPanel userRegistrationPanel;

	public MainFrame(String frameName) {
		super(frameName);
		initializeFrame();
	}

	/**
	 * Builds the chat frame and adds all the necessary pannels onto it.
	 */
	public void initializeFrame() {
		this.setSize(900, 500);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// adding user registration panel to the very top of the MainFrame
		userRegistrationPanel = new UserRegistrationPanel();
		this.add(userRegistrationPanel, BorderLayout.NORTH);
		
		//adding the list of connected users 
		connectedUsersPanel = new ConnectedUsersPanel();
		add(connectedUsersPanel, BorderLayout.EAST);
		
		//adding chat-room JTextArea
		chatAreaPanel = new ChatAreaPanel();
		add(chatAreaPanel, BorderLayout.CENTER);
		
		//add user-input panel
		userInputPanel = new UserInputPanel();
		add(userInputPanel, BorderLayout.SOUTH);
		
		//set JFrame visible
		this.setVisible(true);
	}

	public ConnectedUsersPanel getConnectedUsersPanel() {
		return connectedUsersPanel;
	}

	public void setConnectedUsersPanel(ConnectedUsersPanel connectedUsersPanel) {
		this.connectedUsersPanel = connectedUsersPanel;
	}

	public ChatAreaPanel getChatAreaPanel() {
		return chatAreaPanel;
	}

	public void setChatAreaPanel(ChatAreaPanel chatAreaPanel) {
		this.chatAreaPanel = chatAreaPanel;
	}

	public UserRegistrationPanel getUserRegistrationPanel() {
		return userRegistrationPanel;
	}

	public UserInputPanel getUserInputPanel() {
		return userInputPanel;
	}

	public void setUserInputPanel(UserInputPanel userInputPanel) {
		this.userInputPanel = userInputPanel;
	}

}
