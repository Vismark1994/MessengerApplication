package com.vismark.MessengerApplication.panel;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConnectedUsersPanel extends JPanel {

	JTextArea listOfConnectedUsersTextArea;
	JScrollPane listOfConnectedUsersScrollPane;

	public ConnectedUsersPanel() {
		setupConnectedUsersPanel();
	}

	public void setupConnectedUsersPanel() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(new JLabel("Online Users"));

		listOfConnectedUsersTextArea = new JTextArea(8, 5);
		listOfConnectedUsersTextArea.setEditable(false);

		listOfConnectedUsersScrollPane = new JScrollPane(listOfConnectedUsersTextArea);
		listOfConnectedUsersScrollPane.setBackground(Color.GREEN);
		add(listOfConnectedUsersScrollPane);
	}

}
