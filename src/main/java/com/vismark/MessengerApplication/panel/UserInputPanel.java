package com.vismark.MessengerApplication.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UserInputPanel extends JPanel{
	
	private JTextArea userInputTextArea;
	private JScrollPane scrollPane;
	private JButton sendButton;
	
	public UserInputPanel() {
		
		//configure panel settings
		setLayout(new FlowLayout());
		
		//configure user-input JTextArea
		userInputTextArea = new JTextArea(5, 40);
		userInputTextArea.setEditable(true);
		
		//wrap user-input JTextArea into JScollPane
		scrollPane = new JScrollPane(userInputTextArea);
		add(scrollPane);
		
		//configure JButton
		sendButton = new JButton("Send");
		add(sendButton);		
	}
}
