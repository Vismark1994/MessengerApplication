package com.vismark.MessengerApplication.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatAreaPanel extends JPanel {

	private JTextArea chatRoomMessages;
	private JScrollPane scrollPane;

	public ChatAreaPanel() {

		setupChatAreaPanel();
	}

	public void setupChatAreaPanel() {

		// ChatAreaPanel settings
		this.setLayout(new BorderLayout());

		// setting up textarea and scrollpane
		chatRoomMessages = new JTextArea(8, 45);
		chatRoomMessages.setEditable(false);
		scrollPane = new JScrollPane(chatRoomMessages);

		add(scrollPane, BorderLayout.CENTER);
	}

}
