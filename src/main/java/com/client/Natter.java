package com.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Natter extends CustomComponent
{
	private static final long serialVersionUID = 1L;
	private JPanel usersPanel, messagePanel;
	private Color transpentColor = new Color(0, 0, 0, 0);
	
	@Override
	void addThemeButton(JPanel buttonPanel)
	{
	}
	
	public Natter()
	{
		super("Natter");
		contentPane.setLayout(new BorderLayout());
		this.setSize(1200, 850);
		this.setLocationRelativeTo(null);
		this.setFocusable(false);
		addGuiComponents();
	}
	
	private void addGuiComponents()
	{
		usersPanel = new JPanel();
		usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
		usersPanel.setBackground(Color.decode(ResourceHandler.getColor("dark_mode", "secondaryColor")));
		usersPanel.setPreferredSize(new Dimension(250, getHeight()));
		usersPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.darkGray)); // Top border only
		
		JLabel userPanelHeading = new JLabel("Chats");
		userPanelHeading.setBorder(new EmptyBorder(10, 10, 10, 10));
		userPanelHeading.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		userPanelHeading.setForeground(Color.decode(ResourceHandler.getColor("dark_mode", "fontColor")));
		usersPanel.add(userPanelHeading);
		
		contentPane.add(usersPanel, BorderLayout.WEST);
		
		////////////////////////////////////////////////////
		
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		chatPanel.setBackground(transpentColor);
		
		messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.setBorder(new EmptyBorder(50, 50, 0, 50));
		messagePanel.setBackground(transpentColor);
		
		contentPane.add(chatPanel, BorderLayout.CENTER);
		
		////////////////////////////////////////////////////
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBackground(transpentColor);
		
		JTextField inputField = new JTextField();
		inputField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e)
			{
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
				{
					String input = inputField.getText();
					
					if (input.isEmpty())
						return;
					
					inputField.setText("");
					messagePanel.add(createChatMessageComponent(input));
					messagePanel.add(Box.createVerticalStrut(10));
					
					repaint();
					revalidate();
				}
			}
		});
		
		inputField.setBackground(Color.decode(ResourceHandler.getColor("dark_mode", "secondaryColor")));
		inputField.setForeground(Color.decode(ResourceHandler.getColor("dark_mode", "fontColor")));
		inputField.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		inputField.setPreferredSize(new Dimension(inputPanel.getWidth(), 50));
		inputPanel.add(inputField, BorderLayout.CENTER);
		chatPanel.add(inputField, BorderLayout.SOUTH);
		
		chatPanel.add(messagePanel, BorderLayout.CENTER);
	}
	
	private JPanel createChatMessageComponent(String message)
	{
		JPanel chatMessage = new JPanel();
		chatMessage.setBackground(Color.BLUE);
		chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
		chatMessage.setBorder(new EmptyBorder(20, 20, 10, 20));
		
		JLabel usernameLabel = new JLabel("lol");
		usernameLabel.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		usernameLabel.setForeground(Color.white);
		chatMessage.add(usernameLabel);
		
		JLabel messageLabel = new JLabel(message);
		messageLabel.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		messageLabel.setForeground(Color.white);
		chatMessage.add(messageLabel);
		
		return chatMessage;
	}
}
