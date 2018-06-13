package com.vismark.MessengerApplication;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.vismark.MessengerApplication.frame.MainFrame;
import com.vismark.MessengerApplication.networking.ConnectionManager;

public class Driver 
{
	public static void main(String[] args) {
		ConnectionManager connectionManager
		    = new ConnectionManager();
	}
}
